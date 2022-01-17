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
package net.sf.l2j.gameserver.model;

import java.util.concurrent.Future;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PenaltyMonsterInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExFishingHpRegen;
import net.sf.l2j.gameserver.network.serverpackets.ExFishingStartCombat;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Rnd;

public class L2Fishing implements Runnable
{
	// =========================================================
	// Data Field
	private L2PcInstance _fisher;
	private int _time;
    private int _stop = 0;
	private int _gooduse = 0;
	private int _animation = 0;
	private int _mode = 0;
	private Future<?> _fishAItask;
	private boolean thinking;
	// Fish data
	private int _fishID;
	private int _fishMaxHP;
	private int _fishCurHP;
	private double _regenHP;
    private int _lureType;

	@Override
	public void run()
	{
		if (_fishCurHP >= _fishMaxHP * 2)
		{
			// The fish got away
            _fisher.sendPacket(new SystemMessage(SystemMessage.BAIT_STOLEN_BY_FISH));
			doDie(false, false);
		}
		else if (_time <= 0)
		{
			// Time is up, so that fish got away
            _fisher.sendPacket(new SystemMessage(SystemMessage.FISH_SPIT_THE_HOOK));
			doDie(false, false);
		}
		else
            aiTask();
	}

	// =========================================================
	public L2Fishing(L2PcInstance fisher, FishData fish, boolean isNoob)
	{
		_fisher = fisher;
		_fishMaxHP = fish.getHP();
		_fishCurHP = _fishMaxHP;
		_regenHP = fish.getHpRegen();
		_fishID = fish.getId();
        _time = fish.getCombatTime() / 1000;
        _lureType = isNoob ? 0 : 1;
        _mode = Rnd.get(100) >= 80 ? 1 : 0;

        ExFishingStartCombat efsc = new ExFishingStartCombat(_fisher, _time, _fishMaxHP, _mode, _lureType);
		_fisher.broadcastPacket(efsc);

		// Succeeded in getting a bite
		_fisher.sendPacket(new SystemMessage(SystemMessage.GOT_A_BITE));

		if (_fishAItask == null)
			_fishAItask = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(this, 1000, 1000);
	}

	public void changeHp(int hp, int pen)
	{
		_fishCurHP -= hp;
		if (_fishCurHP < 0) _fishCurHP = 0;
		
		ExFishingHpRegen efhr = new ExFishingHpRegen(_fisher, _time, _fishCurHP, _mode, _gooduse, _animation, pen);
		_fisher.broadcastPacket(efhr);
		_animation = 0;
		if (_fishCurHP > _fishMaxHP * 2)
		{
			_fishCurHP = _fishMaxHP * 2;
			doDie(false, false);
			return;
		}
		else if (_fishCurHP == 0)
		{
			doDie(true, false);
			return;
		}
	}

	public synchronized void doDie(boolean win, boolean force)
	{
        if (_fishAItask != null)
        {
            _fishAItask.cancel(false);
            _fishAItask = null;
        }

        if (_fisher == null)
            return;

        if (win)
        {
            int check = Rnd.get(100);
            if (check <= 5)
                penaltyMonster();
            else
            {
                _fisher.sendPacket(new SystemMessage(SystemMessage.YOU_CAUGHT_SOMETHING));
                _fisher.addItem("Fishing", _fishID, 1, null, true);
            }
        }
        _fisher.endFishing(win, force);
        _fisher = null;
	}

	protected void aiTask()
	{
		if (thinking)
            return;

		thinking = true;
		_time--;

		try
		{
			if (_mode == 1)
                _fishCurHP += (int) _regenHP;
            if (_stop == 0)
            {
                _stop = 1;
                int check = Rnd.get(100);
                if (check >= 70)
                    _mode = _mode == 0 ? 1 : 0;
            }
            else
                _stop--;
        }
        finally
        {
            thinking = false;
            ExFishingHpRegen efhr = new ExFishingHpRegen(_fisher, _time, _fishCurHP, _mode, 0, _animation, 0);
            if (_animation != 0)
                _fisher.broadcastPacket(efhr);
			else
                _fisher.sendPacket(efhr);
		}
	}

	public void useRealing(int dmg, int pen)
	{
        _animation = 2;
        if (Rnd.get(100) > 90)
        {
            _fisher.sendPacket(new SystemMessage(SystemMessage.FISH_RESISTED_ATTEMPT_TO_BRING_IT_IN));
            _gooduse = 0;
            changeHp(0, pen);
            return;
        }

		if (_fisher == null)
            return;

		if (_mode == 1)
		{
            // Reeling is successful, Damage: $s1
            SystemMessage sm = new SystemMessage(SystemMessage.REELING_SUCCESFUL_S1_DAMAGE);
            sm.addNumber(dmg);
            _fisher.sendPacket(sm);
            if (pen == 50)
            {
                sm = new SystemMessage(SystemMessage.REELING_SUCCESSFUL_PENALTY_S1);
                sm.addNumber(pen);
                _fisher.sendPacket(sm);
            }
            _gooduse = 1;
            changeHp(dmg , pen);
		}
		else
		{
            // Reeling failed, Damage: $s1
            SystemMessage sm = new SystemMessage(SystemMessage.FISH_RESISTED_REELING_S1_HP_REGAINED);
            sm.addNumber(dmg);
            _fisher.sendPacket(sm);
            _gooduse = 2;
            changeHp(-dmg, pen);
		}
	}

	public void usePumping(int dmg, int pen)
	{
        _animation = 1;
        if (Rnd.get(100) > 90)
        {
            _fisher.sendPacket(new SystemMessage(SystemMessage.FISH_RESISTED_ATTEMPT_TO_BRING_IT_IN));
            _gooduse = 0;
            changeHp(0, pen);
            return;
        }

		if (_fisher == null)
            return;

		if (_mode == 0)
		{
            // Pumping is successful. Damage: $s1
            SystemMessage sm = new SystemMessage(SystemMessage.PUMPING_SUCCESFUL_S1_DAMAGE);
            sm.addNumber(dmg);
            _fisher.sendPacket(sm);
            if (pen == 50)
            {
                sm = new SystemMessage(SystemMessage.PUMPING_SUCCESSFUL_PENALTY_S1);
                sm.addNumber(pen);
                _fisher.sendPacket(sm);
            }
            _gooduse = 1;
            changeHp(dmg, pen);
		}
		else
		{
            // Pumping failed, Regained: $s1
            SystemMessage sm = new SystemMessage(SystemMessage.FISH_RESISTED_PUMPING_S1_HP_REGAINED);
            sm.addNumber(dmg);
            _fisher.sendPacket(sm);
            _gooduse = 2;
            changeHp(-dmg, pen);
        }
	}

	private void penaltyMonster()
	{
		int lvl = (int)Math.round(_fisher.getLevel()*0.1);
		int npcid;

        _fisher.sendPacket(new SystemMessage(SystemMessage.YOU_CAUGHT_SOMETHING_SMELLY_THROW_IT_BACK));

		switch (lvl)
		{
			case 0:
			case 1:
				npcid = 13245;
				break;
			case 2:
				npcid = 13246;
				break;
			case 3:
				npcid = 13247;
				break;
			case 4:
				npcid = 13248;
				break;
			case 5:
				npcid = 13249;
				break;
			case 6:
				npcid = 13250;
				break;
			case 7:
				npcid = 13251;
				break;
			case 8:
				npcid = 13252;
				break;
			default:
				npcid = 13245;
			    break;
		}

		L2NpcTemplate temp;
		temp = NpcTable.getInstance().getTemplate(npcid);
		if (temp != null)
		{
			try
			{
				L2Spawn spawn = new L2Spawn(temp);
				spawn.setLocX(_fisher.getFishx());
				spawn.setLocY(_fisher.getFishy());
				spawn.setLocZ(_fisher.getFishz());
				spawn.setAmount(1);
				spawn.setHeading(_fisher.getHeading());
				spawn.stopRespawn();
				((L2PenaltyMonsterInstance)spawn.doSpawn()).setPlayerToKill(_fisher);
			}
			catch (Exception e)
			{
				// Nothing
			}
		}
	}
}