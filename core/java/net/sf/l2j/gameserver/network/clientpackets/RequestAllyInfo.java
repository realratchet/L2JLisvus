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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class RequestAllyInfo extends L2GameClientPacket
{
	private static final String _C__8E_REQUESTALLYINFO = "[C] 8E RequestAllyInfo";
	
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	public void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		final int allyId = activeChar.getAllyId();
		if (allyId == 0)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.NO_CURRENT_ALLIANCES));
			return;
		}
		
		// ======<AllyInfo>======
        SystemMessage sm = new SystemMessage(SystemMessage.ALLIANCE_INFO_HEAD);
        activeChar.sendPacket(sm);
        
        // ======<Ally Name>======
        sm = new SystemMessage(SystemMessage.ALLIANCE_NAME_S1);
        sm.addString(activeChar.getClan().getAllyName());
        activeChar.sendPacket(sm);
        
        int totalMembers = 0;
		int totalOnlineMembers = 0;
		
		List<L2Clan> allies = new ArrayList<>();
		for (L2Clan clan : ClanTable.getInstance().getClans())
		{
			if (clan != null && clan.getAllyId() == allyId)
			{
				// Alliance leader
				if (clan.getClanId() == allyId)
				{
					sm = new SystemMessage(SystemMessage.ALLIANCE_LEADER_S2_OF_S1);
			        sm.addString(clan.getName());
			        sm.addString(clan.getLeaderName());
			        activeChar.sendPacket(sm);
				}
				totalMembers += clan.getMembersCount();
				totalOnlineMembers += clan.getOnlineMembers(0).length;
				allies.add(clan);
			}
		}
        
        // Connection
        sm = new SystemMessage(SystemMessage.CONNECTION_S1_TOTAL_S2);
        sm.addNumber(totalOnlineMembers);
        sm.addNumber(totalMembers);
        activeChar.sendPacket(sm);
        
        // Clan count
        sm = new SystemMessage(SystemMessage.ALLIANCE_CLAN_TOTAL_S1);
        sm.addNumber(allies.size());
        activeChar.sendPacket(sm);
        
        // Clan information
        sm = new SystemMessage(SystemMessage.CLAN_INFO_HEAD);
        
        for (L2Clan clan : allies)
        {
        	activeChar.sendPacket(sm);
        	
            // Clan name
            sm = new SystemMessage(SystemMessage.CLAN_INFO_NAME);
            sm.addString(clan.getName());
            activeChar.sendPacket(sm);
            
            // Clan leader name
            sm = new SystemMessage(SystemMessage.CLAN_INFO_LEADER);
            sm.addString(clan.getLeaderName());
            activeChar.sendPacket(sm);
            
            // Clan level
            sm = new SystemMessage(SystemMessage.CLAN_INFO_LEVEL);
            sm.addNumber(clan.getLevel());
            activeChar.sendPacket(sm);
            
            // Connection
            sm = new SystemMessage(SystemMessage.CONNECTION_S1_TOTAL_S2);
			sm.addNumber(clan.getOnlineMembers(0).length);
			sm.addNumber(clan.getMembersCount());
			activeChar.sendPacket(sm);
            
            sm = new SystemMessage(SystemMessage.CLAN_INFO_SEPARATOR);
        }
        
        // =========================
        sm = new SystemMessage(SystemMessage.CLAN_INFO_FOOT);
        activeChar.sendPacket(sm);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__8E_REQUESTALLYINFO;
	}
}