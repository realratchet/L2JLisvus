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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2CommandChannel;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author -Wooden-
 */
public class RequestExAcceptJoinMPCC extends L2GameClientPacket
{
	private static final String _C__D0_0E_REQUESTEXASKJOINMPCC = "[C] D0:0E RequestExAcceptJoinMPCC";
	private int _response;
	
	@Override
	protected void readImpl()
	{
		_response = readD();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	public void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		L2PcInstance requestor = player.getActiveRequester();
		if (requestor == null)
		{
			return;
		}
		
		if (_response == 1)
		{
			if (requestor.isInParty() && player.isInParty())
			{
				if (!requestor.getParty().isInCommandChannel())
				{
					new L2CommandChannel(requestor); // Create new CC
					requestor.getParty().getCommandChannel().addParty(player.getParty());
					
					if (requestor.getParty().getCommandChannel().getParties().size() < Config.ALT_CHANNEL_ACTIVATION_COUNT)
					{
						requestor.getParty().getCommandChannel().broadcastToChannelMembers(new SystemMessage(SystemMessage.COMMAND_CHANNEL_FORMED));
					}
				}
				else
				{
					if (requestor.getParty().getCommandChannel().getChannelLeader().equals(requestor))
					{
						if (requestor.getParty().getCommandChannel().getParties().size() < 50)
						{
							player.sendPacket(new SystemMessage(SystemMessage.JOINED_COMMAND_CHANNEL));
							requestor.getParty().getCommandChannel().addParty(player.getParty());
							if (requestor.getParty().getCommandChannel().getParties().size() < Config.ALT_CHANNEL_ACTIVATION_COUNT)
							{
								requestor.sendMessage("The number of remaining parties is " + (Config.ALT_CHANNEL_ACTIVATION_COUNT - requestor.getParty().getCommandChannel().getParties().size()) + " until a channel is activated.");
							}
						}
					}
					else
					{
						requestor.sendPacket(new SystemMessage(SystemMessage.CANT_OPEN_CHANNELS_ANYMORE));
					}
				}
			}
		}
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessage.S1_DECLINED_CHANNEL_INVITATION);
			sm.addString(player.getName());
			requestor.sendPacket(sm);
			sm = null;
		}
		
		player.setActiveRequester(null);
		requestor.onTransactionResponse();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_0E_REQUESTEXASKJOINMPCC;
	}
}