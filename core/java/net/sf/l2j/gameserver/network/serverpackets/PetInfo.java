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

import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PetInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2SummonInstance;

/**
 * This class ...
 * @version $Revision: 1.6.2.5.2.12 $ $Date: 2005/03/31 09:19:16 $
 */
public class PetInfo extends L2GameServerPacket
{
	private static final String _S__CA_PETINFO = "[S] b1 PetInfo";
	private final L2Summon _summon;
	private final int _x, _y, _z, _heading;
	private final boolean _isSummoned;
	private final int _mAtkSpd, _pAtkSpd;
	private final int _runSpd, _walkSpd, _swimRunSpd, _swimWalkSpd;
	private int _flRunSpd;
	private int _flWalkSpd;
	private int _flyRunSpd;
	private int _flyWalkSpd;
	private final int _maxHp, _maxMp;
	private int _maxFed, _curFed;
	private final int _val;
	
	/**
	 * rev 478 dddddddddddddddddddffffdddcccccSSdddddddddddddddddddddddddddhc
	 * @param summon 
	 * @param val 
	 */
	public PetInfo(L2Summon summon, int val)
	{
		_summon = summon;
		_isSummoned = summon.isShowSummonAnimation();
		_x = summon.getX();
		_y = summon.getY();
		_z = summon.getZ();
		_heading = summon.getHeading();
		_mAtkSpd = summon.getMAtkSpd();
		_pAtkSpd = summon.getPAtkSpd();
		_runSpd = summon.getStat().getBaseRunSpeed();
		_walkSpd = summon.getStat().getBaseWalkSpeed();
		_swimRunSpd = _flRunSpd = _flyRunSpd = _runSpd;
		_swimWalkSpd = _flWalkSpd = _flyWalkSpd = _walkSpd;
		_maxHp = summon.getMaxHp();
		_maxMp = summon.getMaxMp();
		_val = val;
		if (summon instanceof L2PetInstance)
		{
			_curFed = summon.getCurrentFed(); // how fed it is
			_maxFed = summon.getMaxFed(); // max fed it can be
		}
		else if (summon instanceof L2SummonInstance)
		{
			L2SummonInstance servitor = (L2SummonInstance) summon;
			_curFed = servitor.getTimeRemaining();
			_maxFed = servitor.getTotalLifeTime();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb1);
		writeD(_summon.getSummonType());
		writeD(_summon.getObjectId());
		writeD(_summon.getTemplate().idTemplate + 1000000);
		writeD(0); // 1=attackable
		
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_heading);
		writeD(0);
		writeD(_mAtkSpd);
		writeD(_pAtkSpd);
		writeD(_runSpd);
		writeD(_walkSpd);
		writeD(_swimRunSpd);
		writeD(_swimWalkSpd);
		writeD(_flRunSpd);
		writeD(_flWalkSpd);
		writeD(_flyRunSpd);
		writeD(_flyWalkSpd);
		
		writeF(_summon.getMovementSpeedMultiplier()); // speed multiplier
		writeF(1); // atk speed multiplier
		writeF(_summon.getTemplate().collisionRadius);
		writeF(_summon.getTemplate().collisionHeight);
		writeD(_summon.getWeapon()); // right hand weapon
		writeD(_summon.getArmor());
		writeD(0); // left hand weapon
		writeC(_summon.getOwner() != null ? 1 : 0); // master name above pet 1=true ...
		writeC(1); // running=1
		writeC(_summon.isInCombat() ? 1 : 0); // attacking 1=true
		writeC(_summon.isAlikeDead() ? 1 : 0); // dead 1=true
		writeC(_isSummoned ? 2 : _val); // 0=teleported 1=default 2=summoned
		writeS(_summon.getName());
		writeS(_summon.getTitle());
		writeD(1);
		writeD(_summon.getOwner().getPvpFlag()); // 0 = white,2= purpleblink, if its greater then karma = purple
		writeD(_summon.getOwner().getKarma()); // hmm karma ??
		writeD(_curFed); // how fed it is
		writeD(_maxFed); // max fed it can be
		writeD((int) _summon.getCurrentHp());// current hp
		writeD(_maxHp);// max hp
		writeD((int) _summon.getCurrentMp());// current mp
		writeD(_maxMp);// max mp
		writeD(_summon.getStat().getSp()); // sp
		writeD(_summon.getLevel());// lvl
		writeD((int) _summon.getStat().getExp());
		writeD((int) _summon.getExpForThisLevel());// 0% absolute value
		writeD((int) _summon.getExpForNextLevel());// 100% absolute value
		
		if (_summon instanceof L2PetInstance)
		{
			writeD(((L2PetInstance) _summon).getInventory().getTotalWeight());// weight
			writeD(((L2PetInstance) _summon).getMaxLoad());// max weight it can carry
		}
		else
		{
			writeD(0);
			writeD(0);
		}
		
		writeD(_summon.getPAtk(null));// patk
		writeD(_summon.getPDef(null));// pdef
		writeD(_summon.getMAtk(null, null));// matk
		writeD(_summon.getMDef(null, null));// mdef
		writeD(_summon.getAccuracy());// accuracy
		writeD(_summon.getEvasionRate(null));// evasion
		writeD(_summon.getCriticalHit(null, null));// critical
		writeD((int) _summon.getStat().getMoveSpeed());// speed
		writeD(_summon.getPAtkSpd());// atkspeed
		writeD(_summon.getMAtkSpd());// casting speed
		
		writeD(_summon.getAbnormalEffect());// c2 abnormal visual effect... bleed=1; poison=2; poison & bleed=3; flame=4;
		writeH(_summon.isMountable() ? 1 : 0);// c2 ride button
		
		writeC(0); // c2
		
		// Following all added in C4.
		writeH(0); // ??
		
		if ((_summon.getOwner() != null) && (_summon.getOwner().getEventTeam() > 0))
		{
			writeC(_summon.getOwner().getEventTeam()); // team aura (1 = blue, 2 = red)
		}
		else
		{
			writeC(_summon.getAuraColor());
		}
		
		writeD(_summon.getSoulShotsPerHit());
		writeD(_summon.getSpiritShotsPerHit());
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__CA_PETINFO;
	}
}