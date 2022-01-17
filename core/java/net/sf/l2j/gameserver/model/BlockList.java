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

import java.util.HashSet;
import java.util.Set;

import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:12:59 $
 */

public class BlockList
{
	private final Set<Integer> _blockSet;
	private boolean _isBlockAll;
    
    public BlockList(L2PcInstance owner)
    {
        _blockSet = new HashSet<>();
        _isBlockAll = false;
    }

    private void addToBlockList(L2PcInstance character)
    {
        if (character != null)
        {
            _blockSet.add(Integer.valueOf(character.getObjectId()));
        }
    }
   
    private void removeFromBlockList(L2PcInstance character)
    {
        if (character != null)
        {
            _blockSet.remove(Integer.valueOf(character.getObjectId()));
        }
    }
    
    private boolean isInBlockList(L2PcInstance character)
    {
        return _blockSet.contains(character.getObjectId());        
    }
    
    private boolean isBlockAll()
    {
        return _isBlockAll;
    }
    
    public static boolean isBlocked(L2PcInstance listOwner, L2PcInstance character)
    {
    	BlockList blockList = listOwner.getBlockList();
        return !character.isGM() && (blockList.isBlockAll() || blockList.isInBlockList(character));
    }
    
    private void setBlockAll(boolean state)
    {
    	_isBlockAll = state;
    }
    
    private Set<Integer> getBlockList()
    {
        return _blockSet;
    }
    
    public static void addToBlockList(L2PcInstance listOwner, L2PcInstance character)
    {
    	if (listOwner.getFriendList().contains(character.getObjectId()))
		{
			SystemMessage sm = new SystemMessage(SystemMessage.S1_ALREADY_IN_FRIENDS_LIST);
			sm.addString(character.getName());
			listOwner.sendPacket(sm);
			return;
		}
		
		if (listOwner.getBlockList().getBlockList().contains(character.getObjectId()))
		{
			listOwner.sendMessage("Already in ignore list.");
			return;
		}
		
        listOwner.getBlockList().addToBlockList(character);
        
        SystemMessage sm = new SystemMessage(SystemMessage.S1_HAS_ADDED_YOU_TO_IGNORE_LIST);
        sm.addString(listOwner.getName());
        character.sendPacket(sm);
        
        sm = new SystemMessage(SystemMessage.S1_WAS_ADDED_TO_YOUR_IGNORE_LIST);
        sm.addString(character.getName());
        listOwner.sendPacket(sm);
    }
    
    public static void removeFromBlockList(L2PcInstance listOwner, L2PcInstance character)
    {
    	if (!listOwner.getBlockList().getBlockList().contains(character.getObjectId()))
		{
			listOwner.sendPacket(new SystemMessage(SystemMessage.TARGET_IS_INCORRECT));
			return;
		}
    	
        listOwner.getBlockList().removeFromBlockList(character);
        
        SystemMessage sm = new SystemMessage(SystemMessage.S1_WAS_REMOVED_FROM_YOUR_IGNORE_LIST);
        sm.addString(character.getName());
        listOwner.sendPacket(sm);
    }
    
    public static boolean isInBlockList(L2PcInstance listOwner, L2PcInstance character)
    {
        return listOwner.getBlockList().isInBlockList(character);
    }
    
    public static boolean isBlockAll(L2PcInstance listOwner)
    {
        return listOwner.getBlockList().isBlockAll();
    }
    
    public static void setBlockAll(L2PcInstance listOwner, boolean newValue)
    {
    	// Notify player that command executed
    	if (newValue)
    	{
    		listOwner.sendPacket(new SystemMessage(SystemMessage.BLOCKING_ALL));
    	}
    	else
    	{
    		listOwner.sendPacket(new SystemMessage(SystemMessage.NOT_BLOCKING_ALL));
    	}
        listOwner.getBlockList().setBlockAll(newValue);
    }
    
    public static void sendListToOwner(L2PcInstance listOwner)
    {
    	int i = 1;
		listOwner.sendPacket(new SystemMessage(SystemMessage.BLOCK_LIST_HEADER));
		for (int playerId : listOwner.getBlockList().getBlockList())
		{
			listOwner.sendMessage((i++) + ". " + CharNameTable.getInstance().getNameById(playerId));
		}
		listOwner.sendPacket(new SystemMessage(SystemMessage.FRIEND_LIST_FOOTER));
    }
}
