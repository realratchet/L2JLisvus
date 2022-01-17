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
import net.sf.l2j.gameserver.instancemanager.SiegeManager;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.eventgame.L2Event;

/**
 * sample 0b 952a1048 objectId 00000000 00000000 00000000 00000000 00000000 00000000 format dddddd rev 377 format ddddddd rev 417
 * @version $Revision: 1.3.2.1.2.5 $ $Date: 2005/03/27 18:46:18 $
 */
public class Die extends L2GameServerPacket
{
	private static final String _S__0B_DIE = "[S] 06 Die";
	private final int _chaId;
	private final boolean _fake;
	private boolean _inEvent;
	private boolean _sweepable;

	private int _accessLevel;
	private L2Clan _clan;
	private L2Character _cha;

	/**
	 * @param cha
	 */
	public Die(L2Character cha)
	{
		
		_cha = cha;
		if (cha instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) cha;
			_accessLevel = player.getAccessLevel();
			_clan = player.getClan();
			
			L2Event event = player.getEvent();
			_inEvent = event != null && event.isStarted();
		}
		_chaId = cha.getObjectId();
		_fake = !cha.isDead();
		if (cha instanceof L2Attackable)
		{
			_sweepable = ((L2Attackable) cha).isSweepActive();
		}

	}

	@Override
	protected final void writeImpl()
	{
		if (_fake || _inEvent)
		{
			return;
		}

		writeC(0x06);

		writeD(_chaId);
		// NOTE:
		// 6d 00 00 00 00 - to nearest village
		// 6d 01 00 00 00 - to hide away
		// 6d 02 00 00 00 - to castle
		// 6d 03 00 00 00 - to siege HQ
		// sweepable
		// 6d 04 00 00 00 - FIXED

		writeD(0x01); // 6d 00 00 00 00 - to nearest village
		if (_clan != null)
		{
			boolean isAttackerWithFlag = false;
			boolean isDefender = false;

			Siege siege = SiegeManager.getInstance().getSiege(_cha);
			if (siege != null)

			{
				isAttackerWithFlag = (siege.getAttackerClan(_clan) != null) && (!siege.getAttackerClan(_clan).getFlags().isEmpty()) && !siege.checkIsDefender(_clan);
				isDefender = (siege.getAttackerClan(_clan) == null) && siege.checkIsDefender(_clan);

			}

			writeD(_clan.getHasHideout() > 0 ? 0x01 : 0x00); // 6d 01 00 00 00 - to hide away
			writeD((_clan.getHasCastle() > 0) || isDefender ? 0x01 : 0x00); // 6d 02 00 00 00 - to castle
			writeD(isAttackerWithFlag ? 0x01 : 0x00); // 6d 03 00 00 00 - to siege HQ
		}
		else
		{
			writeD(0x00); // 6d 01 00 00 00 - to hide away
			writeD(0x00); // 6d 02 00 00 00 - to castle
			writeD(0x00); // 6d 03 00 00 00 - to siege HQ
		}

		writeD(_sweepable ? 0x01 : 0x00); // sweepable (blue glow)
		writeD(_accessLevel >= Config.GM_FIXED ? 0x01 : 0x00); // 6d 04 00 00 00 - to FIXED
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.L2GameServerPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__0B_DIE;
	}
}