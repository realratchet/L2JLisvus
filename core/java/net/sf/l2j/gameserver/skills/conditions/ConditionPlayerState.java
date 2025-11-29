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
package net.sf.l2j.gameserver.skills.conditions;

import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.skills.Env;

/**
 * @author mkizub
 */
public class ConditionPlayerState extends Condition
{
	public enum CheckPlayerState
	{
		RESTING,
		MOVING,
		RUNNING,
		FLYING,
		BEHIND,
		FRONT,
		CHAOTIC,
		OLYMPIAD,
		CLAN_LEADER,
		SIEGE_ATTACKER,
		SIEGE_DEFENDER,
	}
	
	final CheckPlayerState _check;
	final boolean _required;
	
	public ConditionPlayerState(CheckPlayerState check, boolean required)
	{
		_check = check;
		_required = required;
	}
	
	@Override
	public boolean testImpl(Env env, Object owner)
	{
		L2PcInstance player;
		switch (_check)
		{
			case RESTING:
				player = env.character.getActingPlayer();
				if (player != null)
				{
					return player.isSitting() == _required;
				}
				return !_required;
			case MOVING:
				return env.character.isMoving() == _required;
			case RUNNING:
				return env.character.isMoving() == _required && env.character.isRunning() == _required;
			case FLYING:
				return env.character.isFlying() == _required;
			case BEHIND:
				return env.character.isBehindTarget() == _required;
			case FRONT:
				return env.character.isInFrontOfTarget() == _required;
			case CHAOTIC:
				player = env.character.getActingPlayer();
				if (player != null)
				{
					return (player.getKarma() > 0) == _required;
				}
				return !_required;
			case OLYMPIAD:
				player = env.character.getActingPlayer();
				if (player != null)
				{
					return player.isInOlympiadMode() == _required;
				}
				return !_required;
			case CLAN_LEADER:
				player = env.character.getActingPlayer();
				if (player != null)
				{
					return player.isClanLeader() == _required;
				}
				return !_required;
			case SIEGE_ATTACKER:
				player = env.character.getActingPlayer();
				if (player != null)
				{
					Siege siege = SiegeManager.getInstance().getSiege(player);
					return (player.getClanId() != 0 && siege != null && siege.getAttackerClan(player.getClanId()) != null) == _required;
				}
				return !_required;
			case SIEGE_DEFENDER:
				player = env.character.getActingPlayer();
				if (player != null)
				{
					Siege siege = SiegeManager.getInstance().getSiege(player);
					return (player.getClanId() != 0 && siege != null && siege.getDefenderClan(player.getClanId()) != null) == _required;
				}
				return !_required;
		}
		return !_required;
	}
}