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
package net.sf.l2j.gameserver.handler.itemhandlers;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.ExtractableItemsData;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2ExtractableItem;
import net.sf.l2j.gameserver.model.L2ExtractableProductItem;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.Rnd;

/**
 * 
 * @author FBIagent 11/12/2006
 * 
 */
public class ExtractableItems implements IItemHandler
{
    private static Logger _log = Logger.getLogger(ExtractableItems.class.getName());
    
    @Override
	public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
        if (!(playable instanceof L2PcInstance))
        	return;

        L2PcInstance activeChar = (L2PcInstance)playable;
        int itemID = item.getItemId();
        boolean isFish = (itemID >= 6411 && itemID <= 6518) || (itemID >= 7726 && itemID <= 7806);
        
        L2ExtractableItem exitem = ExtractableItemsData.getInstance().getExtractableItem(itemID);
        if (exitem == null)
            return;
        
        int rndNum = Rnd.get(100), chanceFrom = 0;
        int[][] productData = new int[0][0];
		
		// Calculate extraction
		for (L2ExtractableProductItem expi : exitem.getProductItemsArray())
		{
			int chance = expi.getChance();
			if (rndNum >= chanceFrom && rndNum <= chance + chanceFrom)
			{
				productData = new int[expi.getId().length][2];
				for (int i = 0; i < productData.length; i++)
				{					
					productData[i][0] = expi.getId()[i];
					productData[i][1] = isFish ? (int)(expi.getAmount()[i] * Config.RATE_EXTRACT_FISH) : expi.getAmount()[i];
				}
				break;
			}
			
			chanceFrom += chance;
		}
		
		// Destroy extractable item
        if (!activeChar.destroyItemByItemId("Extract", itemID, 1, activeChar.getTarget(), true))
        {
            return;
        }
		
		if (productData.length == 0 || productData[0][0] <= 0)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.NOTHING_INSIDE_THAT));
		}
		else
		{
			for (int[] product : productData)
			{
				int id = product[0];
				int amount = product[1];
				if (id <= 0)
				{
					continue;
				}
				
				if (ItemTable.getInstance().createDummyItem(id) == null)
				{
					_log.warning(getClass().getSimpleName() + ": Item " + id + " doesn't have a template!");
					activeChar.sendPacket(new SystemMessage(SystemMessage.NOTHING_INSIDE_THAT));
					continue;
				}

				if (ItemTable.getInstance().createDummyItem(id).isStackable())
				{
					activeChar.addItem("Extract", id, amount, activeChar, false);
				}
				else
				{
					for (int i = 0; i < amount; i++)
					{
						activeChar.addItem("Extract", id, 1, activeChar, false);
					}
				}
				
				SystemMessage sm;
				if (id == Inventory.ADENA_ID)
				{
					sm = new SystemMessage(SystemMessage.EARNED_ADENA);
				}
				else
				{
					sm = new SystemMessage(SystemMessage.EARNED_S2_S1_s);
					sm.addItemName(id);
				}
				sm.addNumber(amount);
				activeChar.sendPacket(sm);
			}
		}
    }
    
    @Override
	public int[] getItemIds()
    {
    	return ExtractableItemsData.getInstance().itemIDs();
    }
}