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
package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2FolkInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.templates.L2NpcTemplate.AIType;

/**
 * This class ...
 * @version $Revision: 1.7.2.4.2.9 $ $Date: 2005/04/11 10:05:54 $
 */
public class NpcInfo extends L2GameServerPacket
{
	// ddddddddddddddddddffffdddcccccSSddd dddddc
	// ddddddddddddddddddffffdddcccccSSddd dddddccffd
	
	private static final String _S__22_NPCINFO = "[S] 16 NpcInfo";
	private final L2Character _cha;
	private final int _x, _y, _z, _heading;
	private final int _idTemplate;
	private final boolean _isAttackable, _isAlikeDead, _showSpawnAnimation;
	private final int _mAtkSpd, _pAtkSpd;
	private final int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd;
	private int _flRunSpd;
	private int _flWalkSpd;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private final float _movementMultiplier;
	private final float _attackSpeedMultiplier;
	private final int _rhand, _lhand, _chest, _val;
	private final double collisionHeight, collisionRadius;
	private String _name = "";
	private String _title = "";
	
	// Npc Crest
	private int _clanCrest = 0;
	private int _allyCrest = 0;
	private int _allyId = 0;
	private int _clanId = 0;
	
	/**
	 * @param cha
	 * @param attacker
	 */
	public NpcInfo(L2NpcInstance cha, L2Character attacker)
	{
		_cha = cha;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_heading = cha.getHeading();
		_idTemplate = cha.getTemplate().idTemplate;
		_isAttackable = cha.isAutoAttackable(attacker);
		_isAlikeDead = cha.isAlikeDead() && cha.getTemplate().AI != AIType.CORPSE;
		_showSpawnAnimation = cha.isShowSummonAnimation();
		_mAtkSpd = cha.getMAtkSpd();
		_pAtkSpd = cha.getPAtkSpd();
		_runSpd = cha.getStat().getBaseRunSpeed();
		_walkSpd = cha.getStat().getBaseWalkSpeed();
		_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
		_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
		_movementMultiplier = cha.getMovementSpeedMultiplier();
		_attackSpeedMultiplier = cha.getAttackSpeedMultiplier();
		_rhand = cha.getTemplate().rhand;
		_lhand = cha.getTemplate().lhand;
		_chest = 0;
		_val = 0;
		collisionHeight = cha.getCollisionHeight();
		collisionRadius = cha.getCollisionRadius();
		
		if (cha.getTemplate().serverSideName)
		{
			_name = cha.getTemplate().name;
		}
		
		if (Config.CHAMPION_ENABLE && cha.isChampion())
		{
			_title = (Config.CHAMPION_TITLE);
		}
		else if (cha.getTemplate().serverSideTitle)
		{
			_title = cha.getTemplate().title;
		}
		else
		{
			_title = cha.getTitle();
		}
		
		if (Config.SHOW_NPC_LVL && (cha instanceof L2MonsterInstance))
		{
			String t = "Lv " + cha.getLevel() + (cha.getAggroRange() > 0 ? "*" : "");
			if (_title != null)
			{
				t += " " + _title;
			}
			
			_title = t;
		}

		// Show NPC crest
		if (Config.SHOW_NPC_CREST && cha instanceof L2FolkInstance)
		{
			// Check if NPC is inside town
			if (cha.getIsInCastleTown())
			{
				int ownerId = cha.getCastle().getOwnerId();

				// Check if castle is owned
				if (ownerId != 0)
				{
					L2Clan clan = ClanTable.getInstance().getClan(ownerId);
					if (clan != null)
					{
						_clanCrest = clan.getCrestId();
						_clanId = clan.getClanId();
						_allyCrest = clan.getAllyCrestId();
						_allyId = clan.getAllyId();
					}
				}
			}
		}
	}
	
	public NpcInfo(L2Summon cha, L2Character attacker, int val)
	{
		_cha = cha;
		_x = _cha.getX();
		_y = _cha.getY();
		_z = _cha.getZ();
		_heading = _cha.getHeading();
		_idTemplate = cha.getTemplate().idTemplate;
		_isAttackable = cha.isAutoAttackable(attacker);
		_isAlikeDead = cha.isAlikeDead();
		_showSpawnAnimation = cha.isShowSummonAnimation();
		_mAtkSpd = _cha.getMAtkSpd();
		_pAtkSpd = _cha.getPAtkSpd();
		_runSpd = cha.getStat().getBaseRunSpeed();
		_walkSpd = cha.getStat().getBaseWalkSpeed();
		_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
		_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
		_movementMultiplier = cha.getMovementSpeedMultiplier();
		_attackSpeedMultiplier = cha.getAttackSpeedMultiplier();
		_rhand = cha.getWeapon();
		_lhand = 0;
		_chest = cha.getArmor();
		_val = val;
		collisionHeight = _cha.getTemplate().collisionHeight;
		collisionRadius = _cha.getTemplate().collisionRadius;
		_name = _cha.getName();
		_title = cha.getOwner() != null && cha.getOwner().isOnline() ? cha.getOwner().getName() : "";
	}
	
	@Override
	protected final void writeImpl()
	{
		if (_cha instanceof L2Summon)
		{
			final L2PcInstance tmp = getClient().getActiveChar();
			if (tmp != null)
			{
				final L2PcInstance owner = ((L2Summon) _cha).getOwner();
				if (owner != null)
				{
					// This is an olympiad protection that prevents outsiders from targetting olympiad participants (useful for servers without geodata)
					if (owner.isInOlympiadMode() && !tmp.isGM() && !tmp.isInOlympiadMode() && !tmp.inObserverMode())
					{
						return;
					}
				}
			}
		}
		
		writeC(0x16);
		writeD(_cha.getObjectId());
		writeD(_idTemplate + 1000000); // npc type id
		writeD(_isAttackable ? 1 : 0);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_heading);
		writeD(0x00);
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_swimRunSpd); // swimspeed
		writeD(_swimWalkSpd); // swimspeed
		writeD(_flRunSpd);
		writeD(_flWalkSpd);
		writeD(_flyRunSpd);
		writeD(_flyWalkSpd);
		writeF(_movementMultiplier);
		writeF(_attackSpeedMultiplier);
		writeF(collisionRadius);
		writeF(collisionHeight);
		writeD(_rhand); // right hand weapon
		writeD(_chest);
		writeD(_lhand); // left hand weapon
		writeC(1); // name above char 1=true ... ??
		writeC(_cha.isRunning() ? 1 : 0); // char always running
		writeC(_cha.isInCombat() ? 1 : 0);
		writeC(_isAlikeDead ? 1 : 0);
		writeC(_showSpawnAnimation ? 2 : _val); // 0=teleported 1=default 2=summoned
		writeS(_name);
		writeS(_title);
		if (_cha instanceof L2Summon)
		{
			writeD(1);
			writeD(((L2Summon) _cha).getOwner().getPvpFlag());
			writeD(((L2Summon) _cha).getOwner().getKarma()); // hmm karma ??
		}
		else
		{
			writeD(0);
			writeD(0);
			writeD(0000); // hmm karma ??
		}
		
		writeD(_cha.getAbnormalEffect()); // C2
		writeD(_clanId); // clan id
		writeD(_clanCrest); // crest id
		writeD(_allyId); // ally id
		writeD(_allyCrest); // all crest
		writeC(0000); // C2
		
		if ((_cha instanceof L2Summon) && (((L2Summon) _cha).getOwner().getEventTeam() > 0))
		{
			writeC(((L2Summon) _cha).getOwner().getEventTeam());// Title color 0=client default
		}
		else
		{
			writeC(_cha.getAuraColor());
		}
		
		writeF(collisionRadius);
		writeF(collisionHeight);
		writeD(0x00); // C4
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__22_NPCINFO;
	}
}