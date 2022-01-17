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
package net.sf.l2j.gameserver.model.actor.instance;

import java.util.concurrent.Future;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.instancemanager.FourSepulchersManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.network.serverpackets.NpcSay;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * @author sandman
 */
public class L2SepulcherMonsterInstance extends L2MonsterInstance
{
	private int _mysteriousBoxId = 0;
	private boolean _isVictimRescued = false;

	protected Future<?> _victimSpawnKeyBoxTask = null;
	protected Future<?> _victimShout = null;
	protected Future<?> _changeImmortalTask = null;
	protected Future<?> _onDeadEventTask = null;

	public L2SepulcherMonsterInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);

		// Set Raid Boss state
		switch (template.npcId)
		{
			case 10339:
			case 10342:
			case 10346:
			case 10349:
				setIsRaid(true);
				break;
		}
	}

	@Override
	public void onSpawn()
	{
		super.onSpawn();

		switch (getNpcId())
		{
			case 12985:
			case 12986:
			case 12987:
			case 12988:
			case 12989:
			case 12990:
			case 12991:
			case 12992:
				if (_victimSpawnKeyBoxTask != null)
				{
					_victimSpawnKeyBoxTask.cancel(true);
				}
				_victimSpawnKeyBoxTask = ThreadPoolManager.getInstance().scheduleEffect(new VictimSpawnKeyBox(this), 300000);
				if (_victimShout != null)
				{
					_victimShout.cancel(true);
				}
				_victimShout = ThreadPoolManager.getInstance().scheduleEffect(new VictimShout(this), 5000);
				break;
			case 13031:
			case 13032:
			case 13033:
			case 13034:
			case 13035:
			case 13036:
			case 13037:
			case 13038:
			case 13039:
			case 13040:
			case 13041:
			case 13042:
			case 13043:
			case 13044:
			case 13045:
			case 13046:
				break;
			case 13066:
			case 13067:
			case 13068:
			case 13069:
			case 13070:
			case 13071:
			case 13072:
			case 13073:
			case 13074:
			case 13075:
			case 13076:
			case 13077:
			case 13078:
				if (_changeImmortalTask != null)
				{
					_changeImmortalTask.cancel(true);
				}
				_changeImmortalTask = ThreadPoolManager.getInstance().scheduleEffect(new ChangeImmortal(this), 1600);
				break;
			case 13091:
				break;
		}
	}

	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}

		switch (getNpcId())
		{
			// Halisha\'s Officer
			case 12955:
			case 12956:
			case 12957:
			case 12958:
			case 12959:
			case 12960:
			case 12961:
			case 12962:
			case 12963:
			case 12964:
			case 12965:
			case 12966:
				// Beetle of Grave
			case 12984:
				// Executioner of Halisha
			case 12993:
			case 12994:
			case 12995:
			case 12996:
			case 12997:
			case 12998:
			case 12999:
			case 13000:
				// Halisha\'s Foreman
			case 13018:
			case 13019:
				// Archon of Halisha
			case 13047:
			case 13048:
			case 13049:
			case 13050:
			case 13051:
			case 13052:
			case 13053:
			case 13054:
				if (_onDeadEventTask != null)
				{
					_onDeadEventTask.cancel(true);
				}
				_onDeadEventTask = ThreadPoolManager.getInstance().scheduleEffect(new OnDeadEvent(this), 3500);
				break;
			// Victim
			case 12985:
			case 12986:
			case 12987:
			case 12988:
			case 12989:
			case 12990:
			case 12991:
			case 12992:
				if (_victimSpawnKeyBoxTask != null)
				{
					_victimSpawnKeyBoxTask.cancel(true);
					_victimSpawnKeyBoxTask = null;
				}

				// do not spawn executioner if victim is already rescued
				if (!_isVictimRescued)
				{
					if (_victimShout != null)
					{
						_victimShout.cancel(true);
						_victimShout = null;
					}

					if (_onDeadEventTask != null)
					{
						_onDeadEventTask.cancel(true);
					}
					_onDeadEventTask = ThreadPoolManager.getInstance().scheduleEffect(new OnDeadEvent(this), 3500);
				}
				break;
			// Beetle of Grave
			case 12976:
			case 12977:
			case 12978:
			case 12979:
			case 12980:
			case 12981:
			case 12982:
			case 12983:
				if (FourSepulchersManager.getInstance().isViscountMobsAnnihilated(_mysteriousBoxId))
				{
					if (_onDeadEventTask != null)
					{
						_onDeadEventTask.cancel(true);
					}
					_onDeadEventTask = ThreadPoolManager.getInstance().scheduleEffect(new OnDeadEvent(this), 3500);
				}
				break;
			case 13055:
			case 13056:
			case 13057:
			case 13058:
			case 13059:
			case 13060:
			case 13061:
			case 13062:
			case 13063:
			case 13064:
			case 13065:
			case 13066:
			case 13067:
			case 13068:
			case 13069:
			case 13070:
			case 13071:
			case 13072:
			case 13073:
			case 13074:
			case 13075:
				if (FourSepulchersManager.getInstance().isDukeMobsAnnihilated(_mysteriousBoxId))
				{
					if (_onDeadEventTask != null)
					{
						_onDeadEventTask.cancel(true);
					}
					_onDeadEventTask = ThreadPoolManager.getInstance().scheduleEffect(new OnDeadEvent(this), 3500);
				}
				break;
			case 10339:
			case 10342:
			case 10346:
			case 10349:
				giveCup((L2PcInstance) killer);
				if (_onDeadEventTask != null)
				{
					_onDeadEventTask.cancel(true);
				}
				_onDeadEventTask = ThreadPoolManager.getInstance().scheduleEffect(new OnDeadEvent(this), 8500);
				break;
		}
		return true;
	}

	@Override
	public void deleteMe()
	{
		if (_victimSpawnKeyBoxTask != null)
		{
			_victimSpawnKeyBoxTask.cancel(true);
			_victimSpawnKeyBoxTask = null;
		}

		if (_victimShout != null)
		{
			_victimShout.cancel(true);
			_victimShout = null;
		}

		if (_onDeadEventTask != null)
		{
			_onDeadEventTask.cancel(true);
			_onDeadEventTask = null;
		}

		super.deleteMe();
	}

	private void giveCup(L2PcInstance player)
	{
		String questId = "620_FourGoblets";
		int cupId = 0;
		int oldBrooch = 7262;

		switch (getNpcId())
		{
			case 10339:
				cupId = 7256;
				break;
			case 10342:
				cupId = 7257;
				break;
			case 10346:
				cupId = 7258;
				break;
			case 10349:
				cupId = 7259;
				break;
		}

		if (player.getParty() != null)
		{
			for (L2PcInstance mem : player.getParty().getPartyMembers())
			{
				QuestState qs = mem.getQuestState(questId);
				if ((qs != null) && (qs.isStarted() || qs.isCompleted()) && (mem.getInventory().getItemByItemId(oldBrooch) == null))
				{
					mem.addItem("Quest", cupId, 1, mem, true);
				}
			}
		}
		else
		{
			QuestState qs = player.getQuestState(questId);
			if ((qs != null) && (qs.isStarted() || qs.isCompleted()) && (player.getInventory().getItemByItemId(oldBrooch) == null))
			{
				player.addItem("Quest", cupId, 1, player, true);
			}
		}
	}
	
	private class VictimShout implements Runnable
	{
		private final L2SepulcherMonsterInstance _activeChar;

		public VictimShout(L2SepulcherMonsterInstance activeChar)
		{
			_activeChar = activeChar;
		}

		@Override
		public void run()
		{
			if (_activeChar.isDead())
			{
				return;
			}

			if (!_activeChar.isVisible())
			{
				return;
			}

			broadcastPacket(new NpcSay(getObjectId(), 0, getNpcId(), "Please save me!!"));
		}
	}

	private class VictimSpawnKeyBox implements Runnable
	{
		private final L2SepulcherMonsterInstance _activeChar;

		public VictimSpawnKeyBox(L2SepulcherMonsterInstance activeChar)
		{
			_activeChar = activeChar;
		}

		@Override
		public void run()
		{
			if (_activeChar.isDead())
			{
				return;
			}

			if (!_activeChar.isVisible())
			{
				return;
			}

			// A happy ending for Victim
			_isVictimRescued = true;

			FourSepulchersManager.getInstance().spawnKeyBox(_activeChar);
			broadcastPacket(new NpcSay(getObjectId(), 0, getNpcId(), "Many thanks for rescuing me."));

			if (_victimShout != null)
			{
				_victimShout.cancel(true);
				_victimShout = null;
			}
		}
	}

	private class OnDeadEvent implements Runnable
	{
		L2SepulcherMonsterInstance _activeChar;

		public OnDeadEvent(L2SepulcherMonsterInstance activeChar)
		{
			_activeChar = activeChar;
		}

		@Override
		public void run()
		{
			switch (_activeChar.getNpcId())
			{
				// Halisha\'s Officer
				case 12955:
				case 12956:
				case 12957:
				case 12958:
				case 12959:
				case 12960:
				case 12961:
				case 12962:
				case 12963:
				case 12964:
				case 12965:
				case 12966:
					// Beetle of Grave
				case 12984:
					// Executioner of Halisha
				case 12993:
				case 12994:
				case 12995:
				case 12996:
				case 12997:
				case 12998:
				case 12999:
				case 13000:
					// Halisha\'s Foreman
				case 13018:
				case 13019:
					// Archon of Halisha
				case 13047:
				case 13048:
				case 13049:
				case 13050:
				case 13051:
				case 13052:
				case 13053:
				case 13054:
					FourSepulchersManager.getInstance().spawnKeyBox(_activeChar);
					break;
				// Victim
				case 12985:
				case 12986:
				case 12987:
				case 12988:
				case 12989:
				case 12990:
				case 12991:
				case 12992:
					if (!_isVictimRescued)
					{
						FourSepulchersManager.getInstance().spawnExecutionerOfHalisha(_activeChar);
					}
					break;
				// Beetle of Grave
				case 12976:
				case 12977:
				case 12978:
				case 12979:
				case 12980:
				case 12981:
				case 12982:
				case 12983:
					FourSepulchersManager.getInstance().spawnMonster(_activeChar.getMysteriousBoxId());
					break;
				case 13055:
				case 13056:
				case 13057:
				case 13058:
				case 13059:
				case 13060:
				case 13061:
				case 13062:
				case 13063:
				case 13064:
				case 13065:
				case 13066:
				case 13067:
				case 13068:
				case 13069:
				case 13070:
				case 13071:
				case 13072:
				case 13073:
				case 13074:
				case 13075:
					FourSepulchersManager.getInstance().spawnArchonOfHalisha(_activeChar.getMysteriousBoxId());
					break;
				case 10339:
				case 10342:
				case 10346:
				case 10349:
					FourSepulchersManager.getInstance().spawnEmperorsGraveNpc(_activeChar.getMysteriousBoxId());
					break;
			}
		}
	}

	private class ChangeImmortal implements Runnable
	{
		L2SepulcherMonsterInstance activeChar;

		public ChangeImmortal(L2SepulcherMonsterInstance mob)
		{
			activeChar = mob;
		}

		@Override
		public void run()
		{
			L2Skill fp = SkillTable.getInstance().getInfo(4616, 1); // Invulnerable by petrification
			fp.getEffects(activeChar, activeChar);
		}
	}
	
	/**
	 * @return the mysteriousBoxId
	 */
	public int getMysteriousBoxId()
	{
		return _mysteriousBoxId;
	}

	/**
	 * @param mysteriousBoxId the mysteriousBoxId to set
	 */
	public void setMysteriousBoxId(int mysteriousBoxId)
	{
		_mysteriousBoxId = mysteriousBoxId;
	}
}