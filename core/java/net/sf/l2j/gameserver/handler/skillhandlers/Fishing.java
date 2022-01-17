/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.geoengine.GeoData;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.zone.L2ZoneType;
import net.sf.l2j.gameserver.model.zone.type.L2FishingZone;
import net.sf.l2j.gameserver.model.zone.type.L2WaterZone;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2Weapon;
import net.sf.l2j.gameserver.templates.L2WeaponType;
import net.sf.l2j.gameserver.util.Util;
import net.sf.l2j.util.Rnd;

public class Fishing implements ISkillHandler
{
	// private static Logger _log = Logger.getLogger(SiegeFlag.class.getName());
	protected SkillType[] _skillIds =
	{
		SkillType.FISHING
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets, boolean isFirstCritical)
	{
		if ((activeChar == null) || !(activeChar instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance player = (L2PcInstance) activeChar;
		
		/**
		 * If fishing is disabled, there isn't much point in doing anything else, unless you are GM. So this got moved up here, before anything else.
		 */
		if (!Config.ALLOW_FISHING && !player.isGM())
		{
			player.sendMessage("Fishing system is currently disabled.");
			return;
		}

		if (player.isFishing())
		{
			if (player.getFishCombat() != null)
			{
				player.getFishCombat().doDie(false, true);
			}
			else
			{
				player.endFishing(false, true);
			}
			return;
		}
		
		L2Weapon weaponItem = player.getActiveWeaponItem();
		if ((weaponItem == null) || (weaponItem.getItemType() != L2WeaponType.FISHINGROD))
		{
			// Fishing poles are not equipped
			player.sendPacket(new SystemMessage(SystemMessage.FISHING_POLE_NOT_EQUIPPED));
			return;
		}
		
		L2ItemInstance lure = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if (lure == null)
		{
			// Bait not equipped.
			player.sendPacket(new SystemMessage(SystemMessage.BAIT_ON_HOOK_BEFORE_FISHING));
			return;
		}
		
		player.setLureId(lure.getItemId());
		
		if (player.isInBoat())
		{
			// You can't fish while you are on boat
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_FISH_ON_BOAT));
			
			return;
		}
		
		if (player.isInCraftMode() || player.isInStoreMode())
		{
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_FISH_WHILE_USING_RECIPE_BOOK));
			return;
		}
		
		if (player.isInsideZone(L2Character.ZONE_WATER))
		{
			// You can't fish in water
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_FISH_UNDER_WATER));
			return;
		}
		
		int rnd = Rnd.get(150) + 50;
		double angle = Util.convertHeadingToDegree(player.getHeading());
		double radian = Math.toRadians(angle);
		double sin = Math.sin(radian);
		double cos = Math.cos(radian);
		int x = player.getX() + (int) (cos * rnd);
		int y = player.getY() + (int) (sin * rnd);
		int z = player.getZ() + 50;
		
		/*
		 * ...and if the spot is in a fishing zone.
		 * If it is, it will then position the hook on the water surface.
		 * If not, you have to be GM to proceed past here... in that case, the hook will be positioned using the old Z lookup method.
		 */
		L2FishingZone aimingTo = null;
		L2WaterZone water = null;
		boolean canFish = false;
		for (L2ZoneType zone : ZoneManager.getInstance().getZones(x, y))
		{
			if (zone instanceof L2FishingZone)
			{
				aimingTo = (L2FishingZone) zone;
				continue;
			}
			
			if (zone instanceof L2WaterZone)
			{
				water = (L2WaterZone) zone;
			}
		}
		
		// Fishing zone found, we can fish here
		if (aimingTo != null)
		{
			// Checking if we can see end of the pole
			if (GeoData.getInstance().canSeeTarget(player.getX(), player.getY(), player.getZ(), x, y, z))
			{
				int geoHeight = GeoData.getInstance().getHeight(x, y, z);
				
				// finding z level for hook
				if (water != null)
				{
					// Water zone exists
					if (geoHeight == z || geoHeight < water.getWaterZ())
					{
						// Water Z is higher than geo Z
						z = water.getWaterZ() + 10;
						canFish = true;
					}
				}
				else
				{
					// No water zone, using fishing zone
					if (geoHeight == z || geoHeight < aimingTo.getWaterZ())
					{
						// Fishing Z is higher than geo Z
						z = aimingTo.getWaterZ() + 10;
						canFish = true;
					}
				}
			}
		}
		
		if (!canFish)
		{
			// You can't fish here
			player.sendPacket(new SystemMessage(SystemMessage.CANNOT_FISH_HERE));
			return;
		}
		
		// Has enough bait, consume 1 and start fishing.
		if (!player.destroyItem("Consume", lure, 1, null, false))
		{
			return;
		}
		
		// If everything else checks out, actually cast the hook and start fishing
		player.startFishing(x, y, z);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return _skillIds;
	}
}