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
package net.sf.l2j.gameserver.instancemanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.StringUtil;

/**
 * Petition Manager
 * 
 * @author Tempy
 *
 */
public final class PetitionManager
{
	private final static Logger _log = Logger.getLogger(PetitionManager.class.getName());
	
	private Map<Integer, Petition> _pendingPetitions;
	private Map<Integer, Petition> _completedPetitions;
	
	private static enum PetitionState 
	{
		Pending,
		Responder_Cancel,
		Responder_Missing,
		Responder_Reject,
		Responder_Complete,
		Petitioner_Cancel,
		Petitioner_Missing,
		In_Process,
		Completed
	}
	
	private static enum PetitionType
	{
		Immobility,
		Recovery_Related,
		Bug_Report,
		Quest_Related,
		Bad_User,	
		Suggestions,
		Game_Tip,
		Operation_Related,
		Other
	}
	
	public static PetitionManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private class Petition
	{
		private long _submitTime = System.currentTimeMillis();
		private int _id;
		private PetitionType _type;
		private PetitionState _state = PetitionState.Pending;
		private String _content;
		
		private final List<CreatureSay> _messageLogs = new ArrayList<>();
		
		private L2PcInstance _petitioner;
		private L2PcInstance _responder;
		
		public Petition(L2PcInstance petitioner, String petitionText, int petitionType)
		{
			petitionType--;
			_id = IdFactory.getInstance().getNextId();
			if (petitionType >= PetitionType.values().length)
			{
				_log.warning("PetitionManager: Invalid petition type (received type was +1) : " + petitionType);
			}
			_type = PetitionType.values()[petitionType];
			_content = petitionText;
			
			_petitioner = petitioner;
		}
		
        protected boolean addLogMessage(CreatureSay cs)
		{
			return _messageLogs.add(cs);
		}
		
		protected List<CreatureSay> getLogMessages()
		{
			return _messageLogs;
		}
		
		public boolean endPetitionConsultation(PetitionState endState)
		{
			setState(endState);
			
			if (getResponder() != null && getResponder().isOnline())
			{
				if (endState == PetitionState.Responder_Reject) 
				{
					getPetitioner().sendMessage("Your petition was rejected. Please try again later.");
				}
				else 
				{
                    // Ending petition consultation with <Player>.
					SystemMessage sm = new SystemMessage(395);
					sm.addString(getPetitioner().getName());
					getResponder().sendPacket(sm);
					
					if (endState == PetitionState.Petitioner_Cancel)
					{
                        // Receipt No. <ID> petition cancelled. 
						sm = new SystemMessage(391);
						sm.addNumber(getId());
						getResponder().sendPacket(sm);
					}
				}
			}
			
            // End petition consultation and inform them, if they are still online.
			if (getPetitioner() != null && getPetitioner().isOnline())
			{
				getPetitioner().sendPacket(new SystemMessage(387));
			}
			
			getCompletedPetitions().put(getId(), this);
			return (getPendingPetitions().remove(getId()) != null);
		}
		
		public String getContent()
		{
			return _content;
		}
		
		public int getId()
		{
			return _id;
		}
		
		public L2PcInstance getPetitioner()
		{
			return _petitioner;
		}
		
		public L2PcInstance getResponder()
		{
			return _responder;
		}
		
		public long getSubmitTime()
		{
			return _submitTime;
		}
		
		public PetitionState getState()
		{
			return _state;
		}
		
		public String getTypeAsString()
		{
			return _type.toString().replace("_", " ");
		}
		
		public void sendPetitionerPacket(L2GameServerPacket responsePacket)
		{
			if (getPetitioner() == null || !getPetitioner().isOnline())
			{
                // Allows petitioners to see the results of their petition when 
                // they log back into the game.
				return;
			}
			
			getPetitioner().sendPacket(responsePacket);
		}
		
		public void sendResponderPacket(L2GameServerPacket responsePacket)
		{
			if (getResponder() == null || !getResponder().isOnline())
			{
				endPetitionConsultation(PetitionState.Responder_Missing);
				return;
			}
			
			getResponder().sendPacket(responsePacket);
		}
		
		public void setState(PetitionState state)
		{
			_state = state;
		}
		
		public void setResponder(L2PcInstance respondingAdmin)
		{
			if (_responder == null)
			{
				_responder = respondingAdmin;
			}
		}
	}
	
	private PetitionManager()
	{
		_log.info("Initializing PetitionManager");
		_pendingPetitions = new HashMap<>();
		_completedPetitions = new HashMap<>();
	}
	
	public void clearCompletedPetitions()
	{
		int numPetitions = getPendingPetitionCount();
		
		getCompletedPetitions().clear();
		_log.info("PetitionManager: Completed petition data cleared. " + numPetitions + " petition(s) removed.");
	}
	
	public void clearPendingPetitions()
	{
		int numPetitions = getPendingPetitionCount();
		
		getPendingPetitions().clear();
		_log.info("PetitionManager: Pending petition queue cleared. " + numPetitions + " petition(s) removed.");
	}
	
	public boolean acceptPetition(L2PcInstance respondingAdmin, int petitionId, boolean forced)
	{
		if (!isValidPetition(petitionId))
			return false;
		
		Petition currPetition = getPendingPetitions().get(petitionId);
		
		if (currPetition.getResponder() != null)
			return false;

		currPetition.setResponder(respondingAdmin);
		currPetition.setState(PetitionState.In_Process);
        
		if (forced)
		{
			// A forcible petition from GM has been received.
			currPetition.sendPetitionerPacket(new SystemMessage(SystemMessage.A_FORCIBLE_PETITION_HAS_BEEN_RECEIVED));
		}
		
		// Petition application accepted. (Send to Petitioner)
		currPetition.sendPetitionerPacket(new SystemMessage(SystemMessage.PETITION_APPLICATION_ACCEPTED));
		
        // Petition application accepted. Receipt No. is <ID>
		SystemMessage sm = new SystemMessage(SystemMessage.PETITION_ACCEPTED_RECEIPT_S1);
		sm.addNumber(currPetition.getId());
		currPetition.sendResponderPacket(sm);
        
        // Petition consultation with <Player> underway.
		sm = new SystemMessage(SystemMessage.PETITION_WITH_S1_UNDER_WAY);
		sm.addString(currPetition.getPetitioner().getName());
		currPetition.sendResponderPacket(sm);
		return true;
	}
	
	public boolean cancelActivePetition(L2PcInstance player)
	{
        for (Petition currPetition : getPendingPetitions().values())
    	{
            if (currPetition.getPetitioner() != null && currPetition.getPetitioner().getObjectId() == player.getObjectId())
                return (currPetition.endPetitionConsultation(PetitionState.Petitioner_Cancel));
    			
            if (currPetition.getResponder() != null && currPetition.getResponder().getObjectId() == player.getObjectId())
                return (currPetition.endPetitionConsultation(PetitionState.Responder_Cancel));
        }
		
		return false;
	}
	
	public void checkPetitionMessages(L2PcInstance petitioner)
	{
        if (petitioner != null)
        {
    		for (Petition currPetition : getPendingPetitions().values())
    		{
                if (currPetition == null)
                    continue;
                
    			if (currPetition.getPetitioner() != null && currPetition.getPetitioner().getObjectId() == petitioner.getObjectId())
    			{
    				for (CreatureSay logMessage : currPetition.getLogMessages())
    					petitioner.sendPacket(logMessage);
    				
    				return;
    			}
    		}
        }
	}
	
	public boolean endActivePetition(L2PcInstance player)
	{
		if (!player.isGM())
			return false;
		
		for (Petition currPetition : getPendingPetitions().values())
        {
            if (currPetition == null)
                continue;
            
			if (currPetition.getResponder() != null && currPetition.getResponder().getObjectId() == player.getObjectId())
				return (currPetition.endPetitionConsultation(PetitionState.Completed));
        }
		
		return false;
	}
	
    protected Map<Integer, Petition> getCompletedPetitions()
	{
		return _completedPetitions;
	}
	
    protected Map<Integer, Petition> getPendingPetitions()
	{
		return _pendingPetitions;
	}
	
	public int getPendingPetitionCount()
	{
		return getPendingPetitions().size();
	}
	
	public int getPlayerTotalPetitionCount(L2PcInstance player)
	{
        if (player == null)
            return 0;
        
		int petitionCount = 0;
		
		for (Petition currPetition : getPendingPetitions().values())
        {
            if (currPetition == null)
                continue;
            
			if (currPetition.getPetitioner() != null && currPetition.getPetitioner().getObjectId() == player.getObjectId())
				petitionCount++;
        }
		
		for (Petition currPetition : getCompletedPetitions().values())
        {
            if (currPetition == null)
                continue;
            
			if (currPetition.getPetitioner() != null && currPetition.getPetitioner().getObjectId() == player.getObjectId())
				petitionCount++;
        }
		
		return petitionCount;
	}
	
	public boolean isPetitionInProcess()
	{
		for (Petition currPetition : getPendingPetitions().values())
        {
            if (currPetition == null)
                continue;
            
			if (currPetition.getState() == PetitionState.In_Process)
				return true;
        }
		
		return false;
	}
	
	public boolean isPetitionInProcess(int petitionId)
	{
		if (!isValidPetition(petitionId))
			return false;
		
		Petition currPetition = getPendingPetitions().get(petitionId);
		return (currPetition.getState() == PetitionState.In_Process);
	}
	
	public boolean isPlayerInConsultation(L2PcInstance player)
	{
        if (player != null)
        {
    	    for (Petition currPetition : getPendingPetitions().values())
    	    {
                if (currPetition == null)
                    continue;
                
                if (currPetition.getState() != PetitionState.In_Process)
                    continue;
                
                if ((currPetition.getPetitioner() != null && currPetition.getPetitioner().getObjectId() == player.getObjectId()) || 
                        (currPetition.getResponder() != null && currPetition.getResponder().getObjectId() == player.getObjectId()))
    	            return true;
    	    }
        }
		return false;
	}
	
	public boolean isPetitioningAllowed()
	{
		return Config.PETITIONING_ALLOWED;
	}
	
	public boolean isPlayerPetitionPending(L2PcInstance petitioner)
	{
        if (petitioner != null)
        {
    		for (Petition currPetition : getPendingPetitions().values())
            {
                if (currPetition == null)
                    continue;
                
    			if (currPetition.getPetitioner() != null && currPetition.getPetitioner().getObjectId() == petitioner.getObjectId())
    				return true;
            }
        }
		return false;
	}
	
	private boolean isValidPetition(int petitionId)
	{
		return getPendingPetitions().containsKey(petitionId);
	}
	
	public boolean rejectPetition(L2PcInstance respondingAdmin, int petitionId)
	{
		if (!isValidPetition(petitionId))
			return false;
		
		Petition currPetition = getPendingPetitions().get(petitionId);
		
		if (currPetition.getResponder() != null)
			return false;
		
		currPetition.setResponder(respondingAdmin);
		return (currPetition.endPetitionConsultation(PetitionState.Responder_Reject));
	}
	
	public boolean sendActivePetitionMessage(L2PcInstance player, String messageText)
	{
		CreatureSay cs;
		
		for (Petition currPetition : getPendingPetitions().values())
		{
            if (currPetition == null)
                continue;
            
			if (currPetition.getPetitioner() != null && currPetition.getPetitioner().getObjectId() == player.getObjectId())
			{
				cs = new CreatureSay(player.getObjectId(), Say2.PETITION_PLAYER, player.getName(), messageText);
				currPetition.addLogMessage(cs);
				
				currPetition.sendResponderPacket(cs);
				currPetition.sendPetitionerPacket(cs);
				return true;
			}
			
			if (currPetition.getResponder() != null && currPetition.getResponder().getObjectId() == player.getObjectId())
			{
				cs = new CreatureSay(player.getObjectId(), Say2.PETITION_GM, player.getName(), messageText);
				currPetition.addLogMessage(cs);
				
				currPetition.sendResponderPacket(cs);
				currPetition.sendPetitionerPacket(cs);
				return true;
			}
		}
		
		return false;
	}
	
	public void sendPendingPetitionList(L2PcInstance activeChar)
	{
		NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
        StringBuilder htmlContent = new StringBuilder();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM HH:mm z");
		
		StringUtil.append(htmlContent, "<html><body>" + "<center><font color=\"LEVEL\">Current Petitions</font><br><table width=\"300\">");
		if (getPendingPetitionCount() == 0)
			StringUtil.append(htmlContent, "<tr><td colspan=\"4\">There are no currently pending petitions.</td></tr>");
		else
			StringUtil.append(htmlContent, "<tr><td></td><td><font color=\"999999\">Petitioner</font></td>" +
				"<td><font color=\"999999\">Petition Type</font></td><td><font color=\"999999\">Submitted</font></td></tr>");
		
		for (Petition currPetition : getPendingPetitions().values())
		{
            if (currPetition == null)
                continue;
            
            StringUtil.append(htmlContent, "<tr><td>");
			
			if (currPetition.getState() != PetitionState.In_Process)
				StringUtil.append(htmlContent, "<button value=\"View\" action=\"bypass -h admin_view_petition " + currPetition.getId() + "\" " +
					"width=\"40\" height=\"15\" back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			else 
				StringUtil.append(htmlContent, "<font color=\"999999\">In Process</font>");
			
			StringUtil.append(htmlContent, "</td><td>" + currPetition.getPetitioner().getName() + 
			                   "</td><td>" + currPetition.getTypeAsString() + "</td><td>" + 
			                   dateFormat.format(new Date(currPetition.getSubmitTime())) + "</td></tr>");
		}
		
		StringUtil.append(htmlContent, "</table><br><button value=\"Refresh\" action=\"bypass -h admin_view_petitions\" width=\"50\" " +
		                   "height=\"15\" back=\"sek.cbui94\" fore=\"sek.cbui92\"><br><button value=\"Back\" action=\"bypass -h admin_admin\" " +
		"width=\"40\" height=\"15\" back=\"sek.cbui94\" fore=\"sek.cbui92\"></center></body></html>");

		htmlMsg.setHtml(htmlContent.toString());
		activeChar.sendPacket(htmlMsg);
	}
	
	public int submitPetition(L2PcInstance petitioner, String petitionText, int petitionType)
	{
		// Create a new petition instance and add it to the list of pending petitions.
		Petition newPetition = new Petition(petitioner, petitionText, petitionType);
		int newPetitionId = newPetition.getId();
		getPendingPetitions().put(newPetitionId, newPetition);
		
		// Notify all GMs that a new petition has been submitted.
		String msgContent = petitioner.getName() + " has submitted a new petition."; //(ID: " + newPetitionId + ")."; 
		GmListTable.broadcastToGMs(new CreatureSay(petitioner.getObjectId(), 17, "Petition System", msgContent));
		
		return newPetitionId;
	}
	
	public void viewPetition(L2PcInstance activeChar, int petitionId)
	{
		if (!activeChar.isGM())
			return;
		
		if (!isValidPetition(petitionId))
			return;
		
		Petition currPetition = getPendingPetitions().get(petitionId);
		NpcHtmlMessage htmlMsg = new NpcHtmlMessage(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM HH:mm z");
		
		String htmlContent = StringUtil.concat(
			"<html><body>",
			"<center><br><font color=\"LEVEL\">Petition #" + currPetition.getId() + "</font><br1>",
			"<img src=\"L2UI.SquareGray\" width=\"200\" height=\"1\"></center><br>",
			"Submit Time: " + dateFormat.format(new Date(currPetition.getSubmitTime())) + "<br1>",
			"Petitioner: " + currPetition.getPetitioner().getName() + "<br1>",
			"Petition Type: " + currPetition.getTypeAsString() + "<br>" + currPetition.getContent() + "<br>",
			"<center><button value=\"Accept\" action=\"bypass -h admin_accept_petition " + currPetition.getId() + "\"" +
				"width=\"50\" height=\"15\" back=\"sek.cbui94\" fore=\"sek.cbui92\"><br1>",
				"<button value=\"Reject\" action=\"bypass -h admin_reject_petition " + currPetition.getId() + "\" " +
				"width=\"50\" height=\"15\" back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>",
			"<button value=\"Back\" action=\"bypass -h admin_view_petitions\" width=\"40\" height=\"15\" back=\"sek.cbui94\" " +
				"fore=\"sek.cbui92\"></center>",
			"</body></html>");
		
		htmlMsg.setHtml(htmlContent);
		activeChar.sendPacket(htmlMsg);
	}
	
	private static class SingletonHolder
	{
		protected static final PetitionManager _instance = new PetitionManager();
	}
}