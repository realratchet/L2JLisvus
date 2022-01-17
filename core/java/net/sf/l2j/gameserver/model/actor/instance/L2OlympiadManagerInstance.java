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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Multisell;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.zone.type.L2OlympiadStadiumZone;
import net.sf.l2j.gameserver.network.serverpackets.ExHeroList;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * Olympiad Npc's Instance
 * 
 * @author godson
 */

public class L2OlympiadManagerInstance extends L2FolkInstance
{
    private static final int GATE_PASS = Config.ALT_OLY_COMP_RITEM;
    private static final String FEWER_THAN = "Fewer than " + String.valueOf(Config.ALT_OLY_REG_DISPLAY);
	private static final String MORE_THAN = "More than " + String.valueOf(Config.ALT_OLY_REG_DISPLAY);
    
    public L2OlympiadManagerInstance (int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }
    
    @Override
	public void onBypassFeedback (L2PcInstance player, String command)
    {  
        if (command.startsWith("OlympiadDesc"))
        {
            int val = Integer.parseInt(command.substring(13,14));
            String suffix = command.substring(14);
            showChatWindow(player, val, suffix);
        }
        else if (command.startsWith("OlympiadNoble"))
        {
            if (!player.isNoble() || player.getClassId().level() < 3)
                return;
            
            int val = Integer.parseInt(command.substring(14));
            NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
            
            switch(val)
            {
                case 1:
                    Olympiad.getInstance().unRegisterNoble(player);
                    break;
                case 2:
                	int classed = 0;
					int nonClassed = 0;
					int[] array = Olympiad.getInstance().getWaitingList();

					if (array != null)
					{
						classed = array[0];
						nonClassed = array[1];
					}
					html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_registered.htm");
					if (Config.ALT_OLY_REG_DISPLAY > 0)
					{
						html.replace("%listClassed%", classed < Config.ALT_OLY_REG_DISPLAY ? FEWER_THAN : MORE_THAN);
						html.replace("%listNonClassed%", nonClassed < Config.ALT_OLY_REG_DISPLAY ? FEWER_THAN : MORE_THAN);
					}
					else
					{
						html.replace("%listClassed%", String.valueOf(classed));
						html.replace("%listNonClassed%", String.valueOf(nonClassed));
					}
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
					break;
                case 3:
                	int points = Olympiad.getInstance().getNoblePoints(player.getObjectId());
					html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_points1.htm");
					html.replace("%points%", String.valueOf(points));
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
					break;
                case 4:
                    Olympiad.getInstance().registerNoble(player, false);
                    break;
                case 5:
                    Olympiad.getInstance().registerNoble(player, true);
                    break;
                case 6:
                	int passes = Olympiad.getInstance().getNoblessePasses(player, false);
					if (passes > 0)
					{
						html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_settle.htm");
						html.replace("%objectId%", String.valueOf(getObjectId()));
						player.sendPacket(html);
					}
					else
					{
						html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_nopoints.htm");
						html.replace("%objectId%", String.valueOf(getObjectId()));
						player.sendPacket(html);
					}
					break;
                case 7:
                    L2Multisell.getInstance().createMultiSell(102, player, false, this);
                    break;
                case 8:
					int point = Olympiad.getInstance().getLastNobleOlympiadPoints(player.getObjectId());
					html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_points2.htm");
					html.replace("%points%", String.valueOf(point));
					html.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(html);
					break;
				case 9:
					passes = Olympiad.getInstance().getNoblessePasses(player, true);
					if (passes > 0)
					{
						L2ItemInstance item = player.getInventory().addItem("Olympiad", GATE_PASS, passes, player, this);

						InventoryUpdate iu = new InventoryUpdate();
						iu.addModifiedItem(item);
						player.sendPacket(iu);

						SystemMessage sm = new SystemMessage(SystemMessage.EARNED_ITEM);
						sm.addNumber(passes);
						sm.addItemName(item.getItemId());
						player.sendPacket(sm);
					}
					break;
                default:
                    _log.warning("Olympiad System: Could not send packet for request " + val);
                    break;
            }
        }
        else if (command.startsWith("Olympiad"))
        { 
            int val = Integer.parseInt(command.substring(9, 10));
            NpcHtmlMessage reply = new NpcHtmlMessage(getObjectId());
            
            switch (val)
            {
                case 1:
                	Map<Integer, String> matches = Olympiad.getInstance().getMatchList();
					reply.setFile(Olympiad.OLYMPIAD_HTML_PATH + "olympiad_observe1.htm");

					Collection<L2OlympiadStadiumZone> stadiums = ZoneManager.getInstance().getAllZones(L2OlympiadStadiumZone.class);
					for (L2OlympiadStadiumZone stadium : stadiums)
					{
						// &$906; -> \\&$906;
						reply.replace("%title" + stadium.getStadiumId() + "%", matches.containsKey(stadium.getStadiumId()) ? matches.get(stadium.getStadiumId()) : "\\&$906;");
					}
					reply.replace("%objectId%", String.valueOf(getObjectId()));
					player.sendPacket(reply);
                    break;
                case 2:
                    // For example >> Olympiad 1_88
                    int classId = Integer.parseInt(command.substring(11));
                    if (classId >= 88)
                    {
                    	List<String> names = Olympiad.getInstance().getClassLeaderBoard(classId);
						reply.setFile(Olympiad.OLYMPIAD_HTML_PATH + "olympiad_ranking.htm");

						int index = 1;
						for (String name : names)
						{
							reply.replace("%place"+index+"%", String.valueOf(index));
							reply.replace("%rank"+index+"%", name);
							index++;
							if (index > 10)
								break;
						}
						for (; index <= 10; index++)
						{
							reply.replace("%place"+index+"%", "");
							reply.replace("%rank"+index+"%", "");
						}

						reply.replace("%objectId%", String.valueOf(getObjectId()));
						player.sendPacket(reply);
                    }
                    break;
                case 3:
                    int id = Integer.parseInt(command.substring(11));
                    Olympiad.getInstance().addSpectator(id, player, true);
                    break;
                case 4:
                    player.sendPacket(new ExHeroList());
                    break;
                default:
                    _log.warning("Olympiad System: Couldnt send packet for request " + val);
                    break;
            }
        }
        else
            super.onBypassFeedback(player, command);
    }
    
    @Override
	public void showChatWindow(L2PcInstance player, int val)
    {
    	int npcId = getTemplate().npcId;
    	
    	String filename = null;
    	switch (getNpcId())
    	{
    		case 8688:
    			if (player.isNoble() && player.getClassId().level() == 3)
    			{
    				filename = Olympiad.OLYMPIAD_HTML_PATH + "noble_main.htm";
    			}
    			else
    			{
    				filename = (getHtmlPath(npcId, val));
    			}
    			break;
    		case 8690:
    		case 8769:
    		case 8770:
    		case 8771:
    		case 8772:
    			if (player.isHero())
    			{
    				filename = Olympiad.OLYMPIAD_HTML_PATH + "hero_main.htm";
    			}
    			else
    			{
    				filename = (getHtmlPath(npcId, val));
    			}
    			break;
    	}
    	
    	if (filename != null)
    	{
    		showChatWindow(player, filename);
    	}
    }
    
    private void showChatWindow(L2PcInstance player, int val, String suffix)
    {
        String filename = Olympiad.OLYMPIAD_HTML_PATH;
        
        filename += "noble_desc" + val;
        filename += (suffix != null) ? suffix + ".htm" : ".htm";
        
        if (filename.equals(Olympiad.OLYMPIAD_HTML_PATH + "noble_desc0.htm"))
            filename = Olympiad.OLYMPIAD_HTML_PATH + "noble_main.htm";
        
        showChatWindow(player, filename);
    }
}