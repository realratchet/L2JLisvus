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
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.eventgame.L2Event;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.1.6.4 $ $Date: 2005/04/06 18:25:18 $
 */
public class Scrolls implements IItemHandler
{
	private static int[] _itemIds =
	{
		3926,
		3927,
		3928,
		3929,
		3930,
		3931,
		3932,
		3933,
		3934,
		3935,
		4218,
		5593,
		5594,
		5595,

		6037,
		5703,
		5803,
		5804,
		5805,
		5806,
		5807,
		6652,
		6655
	};

	@Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
	{
		L2PcInstance activeChar;
		if (playable instanceof L2PcInstance)
		{
			activeChar = (L2PcInstance) playable;
		}
		else if (playable instanceof L2PetInstance)
		{
			activeChar = ((L2PetInstance) playable).getOwner();
		}
		else
		{
			return;
		}

		if (activeChar.isAllSkillsDisabled())
		{

			activeChar.sendPacket(new ActionFailed());
			return;
		}

		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
			return;
		}

		L2Event event = activeChar.getEvent();
		if (event != null && event.isStarted())
		{
			activeChar.sendMessage("This item cannot be used in events.");
			return;
		}

		int itemId = item.getItemId();
		if ((itemId == 5703) || ((itemId >= 5803) && (itemId <= 5807)))
		{
			if (((itemId == 5703) && (activeChar.getExpertiseIndex() == 0)) || // Lucky Charm (No Grade)
				((itemId == 5803) && (activeChar.getExpertiseIndex() == 1)) || // Lucky Charm (D Grade)
				((itemId == 5804) && (activeChar.getExpertiseIndex() == 2)) || // Lucky Charm (C Grade)
				((itemId == 5805) && (activeChar.getExpertiseIndex() == 3)) || // Lucky Charm (B Grade)
				((itemId == 5806) && (activeChar.getExpertiseIndex() == 4)) || // Lucky Charm (A Grade)
				((itemId == 5807) && (activeChar.getExpertiseIndex() == 5))) // Lucky Charm (S Grade)
			{
				if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
				{
					return;
				}

				activeChar.broadcastPacket(new MagicSkillUse(playable, playable, 2168, activeChar.getExpertiseIndex() + 1, 1, 0));
				useScroll(activeChar, 2168, activeChar.getExpertiseIndex() + 1);
				activeChar.setCharmOfLuck(true);
			}
			else
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_CANNOT_BE_USED);
				sm.addItemName(item.getItemId());
				activeChar.sendPacket(sm);

				sm = null;
			}
			return;
		}

		if (itemId == 3926) // Scroll of Guidance XML:2050
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2050, 1, 1, 0));
			useScroll(activeChar, 2050, 1);
		}
		else if (itemId == 3927) // Scroll of Death Whipser XML:2051
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2051, 1, 1, 0));
			useScroll(activeChar, 2051, 1);
		}
		else if (itemId == 3928) // Scroll of Focus XML:2052
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2052, 1, 1, 0));
			useScroll(activeChar, 2052, 1);
		}
		else if (itemId == 3929) // Scroll of Greater Acumen XML:2053
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2053, 1, 1, 0));
			useScroll(activeChar, 2053, 1);
		}
		else if (itemId == 3930) // Scroll of Haste XML:2054
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2054, 1, 1, 0));
			useScroll(activeChar, 2054, 1);
		}
		else if (itemId == 3931) // Scroll of Agility XML:2055
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2055, 1, 1, 0));
			useScroll(activeChar, 2055, 1);
		}
		else if (itemId == 3932) // Scroll of Mystic Enpower XML:2056
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2056, 1, 1, 0));
			useScroll(activeChar, 2056, 1);
		}
		else if (itemId == 3933) // Scroll of Might XML:2057
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2057, 1, 1, 0));
			useScroll(activeChar, 2057, 1);
		}
		else if (itemId == 3934) // Scroll of Wind Walk XML:2058
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2058, 1, 1, 0));
			useScroll(activeChar, 2058, 1);
		}
		else if (itemId == 3935) // Scroll of Shield XML:2059
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2059, 1, 1, 0));
			useScroll(activeChar, 2059, 1);
		}
		else if (itemId == 4218) // Scroll of Mana Regeneration XML:2064
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, activeChar, 2064, 1, 1, 0));
			useScroll(activeChar, 2064, 1);
		}
		else if (itemId == 6037) // Scroll of Waking XML:2170
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, playable, 2170, 1, 1, 0));
			useScroll(activeChar, 2170, 1);
		}
		else if (itemId == 6652) // Amulet: Protection of Valakas XML:2231
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, playable, 2231, 1, 1, 0));
			useScroll(activeChar, 2231, 1);
		}
		else if (itemId == 6655) // Amulet: Slay Valakas XML:2232
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			activeChar.broadcastPacket(new MagicSkillUse(playable, playable, 2232, 1, 1, 0));
			useScroll(activeChar, 2232, 1);
		}
		else if ((itemId == 5593) || (itemId == 5594) || (itemId == 5595)) // SP Scrolls
		{
			if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
			{
				return;
			}

			int amountSP = 0;

			switch (itemId)
			{
				case 5593: // Low Grade
					amountSP = 500;
					break;
				case 5594: // Medium Grade
					amountSP = 5000;
					break;
				case 5595: // High Grade
					amountSP = 100000;
					break;
			}

			activeChar.sendPacket(new MagicSkillUse(playable, playable, 2167, 1, 1, 0));
			activeChar.broadcastPacket(new MagicSkillUse(playable, playable, 2167, 1, 1, 0));

			activeChar.addExpAndSp(0, amountSP);
		}
	}

	public void useScroll(L2PcInstance activeChar, int magicId, int level)
	{
		L2Skill skill = SkillTable.getInstance().getInfo(magicId, level);
		if (skill != null)
		{
			activeChar.doCast(skill);
		}
	}

	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}