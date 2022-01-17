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

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.MultiSellList;
import net.sf.l2j.gameserver.templates.L2Armor;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2Weapon;

/**
 * Multisell list manager
 *
 */
public class L2Multisell
{
    private static Logger _log = Logger.getLogger(L2Multisell.class.getName());
    private List<MultiSellListContainer> _entries = new CopyOnWriteArrayList<>();
    public MultiSellListContainer getList(int id)
    {
        synchronized (_entries)
        {
            for (MultiSellListContainer list : _entries)
            {
                if (list.getListId() == id)
                {
                	return list;
                }
            }
        }

        _log.warning("[L2Multisell] cant find list with id: " + id);
        return null;
    }

    public L2Multisell()
    {
        parseData();
    }

    public void reload()
    {
        parseData();
    }

    public static L2Multisell getInstance()
    {
        return SingletonHolder._instance;
    }

    private void parseData()
    {
        _entries.clear();
        parse();
        _log.config("L2Multisell: Loaded " + _entries.size() + " lists.");
    }

    public class MultiSellEntry
    {
        private int _entryId;

        private List<MultiSellIngredient> _products = new ArrayList<>();
        private List<MultiSellIngredient> _ingredients = new ArrayList<>();

        /**
         * @param entryId The entryId to set.
         */
        public void setEntryId(int entryId)
        {
            _entryId = entryId;
        }

        /**
         * @return Returns the entryId.
         */
        public int getEntryId()
        {
            return _entryId;
        }
        
        /**
         * @param product The product to add.
         */
        public void addProduct(MultiSellIngredient product)
        {
            _products.add(product);
        }
        
        /**
         * @return Returns the products.
         */
        public List<MultiSellIngredient> getProducts()
        {
            return _products;
        }
        
        /**
         * @param ingredient The ingredients to set.
         */
        public void addIngredient(MultiSellIngredient ingredient)
        {
            _ingredients.add(ingredient);
        }

        /**
         * @return Returns the ingredients.
         */
        public List<MultiSellIngredient> getIngredients()
        {
            return _ingredients;
        }
    }

    public class MultiSellIngredient
    {
        private int _itemId;
        private int _itemCount;
        private int _enchantmentLevel;
        private boolean _isTaxIngredient;
        private boolean _maintainIngredient;
        
        public MultiSellIngredient(int itemId, int itemCount, int enchantmentLevel, boolean isTaxIngredient, boolean maintainIngredient)
        {
            setItemId(itemId);
            setItemCount(itemCount);
            setEnchantmentLevel(enchantmentLevel);
            setIsTaxIngredient(isTaxIngredient);
            setMaintainIngredient(maintainIngredient);
        }
        public MultiSellIngredient(MultiSellIngredient e)
        {
        	_itemId = e.getItemId();
        	_itemCount = e.getItemCount();
        	_enchantmentLevel = e.getEnchantmentLevel();
        	_isTaxIngredient = e.isTaxIngredient();
        	_maintainIngredient = e.getMaintainIngredient();
        }
        /**
         * @param itemId The itemId to set.
         */
        public void setItemId(int itemId)
        {
            _itemId = itemId;
        }

        /**
         * @return Returns the itemId.
         */
        public int getItemId()
        {
            return _itemId;
        }

        /**
         * @param itemCount The itemCount to set.
         */
        public void setItemCount(int itemCount)
        {
            _itemCount = itemCount;
        }

        /**
         * @return Returns the itemCount.
         */
        public int getItemCount()
        {
            return _itemCount;
        }
        
        /**
         * @param enchantmentLevel 
         */
        public void setEnchantmentLevel(int enchantmentLevel)
        {
        	_enchantmentLevel = enchantmentLevel;
        }

        /**
         * @return Returns the enchantmentLevel.
         */
        public int getEnchantmentLevel()
        {
            return _enchantmentLevel;
        }
        
        public void setIsTaxIngredient(boolean isTaxIngredient)
        {
        	_isTaxIngredient = isTaxIngredient;
        }

        public boolean isTaxIngredient()
        {
        	return _isTaxIngredient;
        }

        public void setMaintainIngredient(boolean maintainIngredient)
        {
        	_maintainIngredient = maintainIngredient;
        }

        public boolean getMaintainIngredient()
        {
        	return _maintainIngredient;
        }
    }

    public class MultiSellListContainer
    {
        private int _listId;
        private boolean _applyTaxes = false;
        private boolean _maintainEnchantment = false;
        private List<Integer> _npcIds;
        
        private List<MultiSellEntry> _msEntries;
        
        public MultiSellListContainer()
        {
        	_msEntries = new LinkedList<>();
        }

        /**
         * @param listId The listId to set.
         */
        public void setListId(int listId)
        {
            _listId = listId;
        }
        
        public void setApplyTaxes(boolean applyTaxes)
        {
            _applyTaxes = applyTaxes;
        }

        public void setMaintainEnchantment(boolean maintainEnchantment)
        {
            _maintainEnchantment = maintainEnchantment;
        }

        public void addNpcId(int objId)
        {
            _npcIds.add(objId);
        }

        /**
         * @return Returns the listId.
         */
        public int getListId()
        {
            return _listId;
        }
        
        public boolean getApplyTaxes()
        {
            return _applyTaxes;
        }

        public boolean getMaintainEnchantment()
        {
            return _maintainEnchantment;
        }

        public boolean checkNpcId(int npcId)
        {
            if (_npcIds == null)
            {
                synchronized (this)
                {
                    if (_npcIds == null)
                        _npcIds = new ArrayList<>();
                }
                return false;
            }
            return _npcIds.contains(npcId);
        }

        public void addEntry(MultiSellEntry e)
        {
        	_msEntries.add(e);
        }

        public List<MultiSellEntry> getEntries()
        {
            return _msEntries;
        }
    }

    private void hashFiles(String dirname, List<File> hash)
    {
        File dir = new File(Config.DATAPACK_ROOT, "data/" + dirname);
        if (!dir.exists())
        {
            _log.config("Dir " + dir.getAbsolutePath() + " not exists");
            return;
        }
        File[] files = dir.listFiles();
        for (File f : files)
        {
            if (f.getName().endsWith(".xml")) hash.add(f);
        }
    }

    private void parse()
    {
        Document doc = null;
        int id = 0;
        List<File> files = new ArrayList<>();
        hashFiles("multisell", files);
        if (Config.CUSTOM_MULTISELL_LOAD)
        {
        	hashFiles("multisell/custom", files);
        }

        for (File f : files)
        {
            id = Integer.parseInt(f.getName().replaceAll(".xml", ""));
            try
            {

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                factory.setIgnoringComments(true);
                doc = factory.newDocumentBuilder().parse(f);
            }
            catch (Exception e)
            {
                _log.log(Level.SEVERE, "Error loading file " + f, e);
            }
            try
            {
                MultiSellListContainer list = parseDocument(doc);
                list.setListId(id);
                _entries.add(list);
            }
            catch (Exception e)
            {
                _log.log(Level.SEVERE, "Error in file " + f, e);
            }
        }
    }

    protected MultiSellListContainer parseDocument(Document doc)
    {
        MultiSellListContainer list = new MultiSellListContainer();

        for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
        {
            if ("list".equalsIgnoreCase(n.getNodeName()))
            {
            	Node attribute;
            	attribute = n.getAttributes().getNamedItem("applyTaxes");
            	if(attribute == null)
            		list.setApplyTaxes(false);
            	else
            		list.setApplyTaxes(Boolean.parseBoolean(attribute.getNodeValue()));
            	attribute = n.getAttributes().getNamedItem("maintainEnchantment");
            	if(attribute == null)
            		list.setMaintainEnchantment(false);
            	else
            		list.setMaintainEnchantment(Boolean.parseBoolean(attribute.getNodeValue()));

                for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
                {
                    if ("item".equalsIgnoreCase(d.getNodeName()))
                    {
                        MultiSellEntry e = parseEntry(d);
                        list.addEntry(e);
                    }
                }
            }
            else if ("item".equalsIgnoreCase(n.getNodeName()))
            {
                MultiSellEntry e = parseEntry(n);
                list.addEntry(e);
            }
        }

        return list;
    }

    protected MultiSellEntry parseEntry(Node n)
    {
        int entryId = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());

        Node first = n.getFirstChild();
        MultiSellEntry entry = new MultiSellEntry();

        for (n = first; n != null; n = n.getNextSibling())
        {
            if ("ingredient".equalsIgnoreCase(n.getNodeName()))
            {
                int id = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
                int count = Integer.parseInt(n.getAttributes().getNamedItem("count").getNodeValue());
                
                Node attribute;
                int enchantLevel = 0;
                boolean isTaxIngredient = false;
                boolean maintainIngredient = false;
                
                attribute = n.getAttributes().getNamedItem("enchantmentLevel");
                if (attribute != null)
                {
                	enchantLevel = Integer.parseInt(attribute.getNodeValue());
                }
                
                attribute = n.getAttributes().getNamedItem("isTaxIngredient");
                if (attribute != null)
                {
                	isTaxIngredient = Boolean.parseBoolean(attribute.getNodeValue());
                }
                                
                attribute = n.getAttributes().getNamedItem("maintainIngredient");
                if (attribute != null)
                {
                	maintainIngredient = Boolean.parseBoolean(attribute.getNodeValue());
                }
                
                MultiSellIngredient e = new MultiSellIngredient(id, count, enchantLevel, isTaxIngredient, maintainIngredient);
                entry.addIngredient(e);
            }
            else if ("production".equalsIgnoreCase(n.getNodeName()))
            {
                int id = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
                int count = Integer.parseInt(n.getAttributes().getNamedItem("count").getNodeValue());
                
                Node attribute;
                int enchantLevel = 0;
                
                attribute = n.getAttributes().getNamedItem("enchantmentLevel");
                if (attribute != null)
                {
                	enchantLevel = Integer.parseInt(attribute.getNodeValue());
                }

                MultiSellIngredient e = new MultiSellIngredient(id, count, enchantLevel, false, false);
                entry.addProduct(e);
            }
        }
        
        entry.setEntryId(entryId);

        return entry;
    }

    /**
     * This will generate the multisell list for the items.  There exist various
     * parameters in multisells that affect the way they will appear:
     * 1) inventory only: 
     * 		* if true, only show items of the multisell for which the
     * 		  "primary" ingredients are already in the player's inventory.  By "primary"
     * 		  ingredients we mean weapon and armor. 
     * 		* if false, show the entire list.
     * 2) maintain enchantment: presumably, only lists with "inventory only" set to true 
     * 		should sometimes have this as true.  This makes no sense otherwise...
     * 		* If true, then the product will match the enchantment level of the ingredient.
     * 		  if the player has multiple items that match the ingredient list but the enchantment
     * 		  levels differ, then the entries need to be duplicated to show the products and 
     * 		  ingredients for each enchantment level.
     * 		  For example: If the player has a crystal staff +1 and a crystal staff +3 and goes
     * 		  to exchange it at the mammon, the list should have all exchange possibilities for 
     * 		  the +1 staff, followed by all possibilities for the +3 staff.
     * 		* If false, then any level ingredient will be considered equal and product will always
     * 		  be at +0 		
     * 3) apply taxes: affects the amount of adena and ancient adena in ingredients.     
     * 
     * @param listId 
     * @param inventoryOnly 
     * @param player 
     * @param merchant 
     * @return 
     *
     */
    private MultiSellListContainer generateMultiSell(int listId, boolean inventoryOnly, L2PcInstance player, L2NpcInstance merchant)
    {
        MultiSellListContainer listTemplate = getList(listId);
        MultiSellListContainer list = new MultiSellListContainer();

        if (listTemplate == null)
            return list;

        list = new MultiSellListContainer();
        list.setListId(listId);
        if (merchant != null && !listTemplate.checkNpcId(merchant.getNpcId()))
            listTemplate.addNpcId(merchant.getNpcId());

        if (inventoryOnly)
        {
        	if (player == null)
        		return list;
        	
        	L2ItemInstance[] items = listTemplate.getMaintainEnchantment() ? player.getInventory().getUniqueArmoryByEnchantLevel() : player.getInventory().getUniqueArmory();

        	// loop through the entries to see which ones we wish to include
            for (MultiSellEntry ent : listTemplate.getEntries())
            {
            	// Check inventory items to see if they are included inside entry ingredients
            	for (L2ItemInstance item : items)
            	{
            		boolean doInclude = false;
            		
	            	// Check ingredients of this entry to see if it's an entry we'd like to include.
	                for (MultiSellIngredient ing : ent.getIngredients())
	                {
	                    if (item.getItemId() == ing.getItemId())
	                    {
	                    	doInclude = true;
	                        break;
	                    }
	                }
	                
	                // Manipulate the ingredients of the template entry for this particular instance shown
	                // i.e: Assign enchant levels and/or apply taxes as needed.
	                if (doInclude)
	                {
	                	list.addEntry(prepareEntry(ent, listTemplate.getApplyTaxes(), listTemplate.getMaintainEnchantment(), item.getEnchantLevel(), merchant));
	                	break;
	                }
            	}
            }
        }
        else  // This is a list-all type
        {
        	// if no taxes are applied, no modifications are needed
    		for (MultiSellEntry ent : listTemplate.getEntries())
    		{
    			list.addEntry(prepareEntry(ent, listTemplate.getApplyTaxes(), false, 0, merchant));
    		}
        }

        return list;
    }

    // Regarding taxation, the following appears to be the case:
    // a) The count of aa remains unchanged (taxes do not affect aa directly).
    // b) 5/6 of the amount of aa is taxed by the normal tax rate.
    // c) the resulting taxes are added as normal adena value.
    // d) normal adena are taxed fully.
    // e) Items other than adena and ancient adena are not taxed even when the list is taxable.
    // example: If the template has an item worth 120aa, and the tax is 10%,
    // then from 120aa, take 5/6 so that is 100aa, apply the 10% tax in adena (10a)
    // so the final price will be 120aa and 10a!
    private MultiSellEntry prepareEntry(MultiSellEntry templateEntry, boolean applyTaxes, boolean maintainEnchantment, int enchantLevel, L2NpcInstance merchant)
    {
    	MultiSellEntry newEntry = new MultiSellEntry();
    	newEntry.setEntryId(templateEntry.getEntryId() * 100000 + (maintainEnchantment ? enchantLevel : 0));
    	long totalAdenaCount = 0;
    	int adenaIndex = -1;

    	int size = templateEntry.getIngredients().size();
        for (int i = 0; i < size; i++)
        {
        	MultiSellIngredient ing = templateEntry.getIngredients().get(i);
        	// Load the ingredient from the template
        	MultiSellIngredient newIngredient = new MultiSellIngredient(ing);

        	// If taxes are to be applied, modify/add the adena count based on the template adena/ancient adena count
        	if (ing.getItemId() == Inventory.ADENA_ID)
        	{
        		if (ing.isTaxIngredient())
        		{
	        		double taxRate = 0.0;
					if (applyTaxes)
					{
						if (merchant != null && merchant.getIsInCastleTown())
						{
							taxRate = merchant.getCastle().getTaxRate();
						}
					}
					totalAdenaCount += (int)Math.round(ing.getItemCount() * taxRate);
        		}
        		else
        		{
        			totalAdenaCount += ing.getItemCount();
        		}
        		
        		// Keep adena position in ingredients list
        		if (adenaIndex < 0)
        		{
        			adenaIndex = i;
        		}
        		
        		continue;
        	}
        	// If it is an armor/weapon, modify the enchantment level appropriately, if necessary
        	else if (maintainEnchantment && newIngredient.getItemId() > 0 && newIngredient.getEnchantmentLevel() == 0)
        	{
            	L2Item tempItem = ItemTable.getInstance().createDummyItem(ing.getItemId()).getItem();
            	if ((tempItem instanceof L2Armor) || (tempItem instanceof L2Weapon))
            	{
            		newIngredient.setEnchantmentLevel(enchantLevel);
            	}
        	}
        	
        	// Finally, add this ingredient to the entry
        	newEntry.addIngredient(newIngredient);
        }
        
        // Next add the adena amount, if any
        if (totalAdenaCount > 0)
        {
        	if (totalAdenaCount > L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID))
			{
				totalAdenaCount = L2ItemInstance.getMaxItemCount(Inventory.ADENA_ID);
			}
        	newEntry.getIngredients().add(adenaIndex, new MultiSellIngredient(Inventory.ADENA_ID, (int) totalAdenaCount, 0, false, false));
        }
        
        // Now modify the enchantment level of products, if necessary
        for (MultiSellIngredient ing : templateEntry.getProducts())
        {
        	// load the ingredient from the template
        	MultiSellIngredient newIngredient = new MultiSellIngredient(ing);

        	if (maintainEnchantment && newIngredient.getItemId() > 0 && newIngredient.getEnchantmentLevel() == 0)
            {
            	// if it is an armor/weapon, modify the enchantment level appropriately
            	// (note, if maintain enchantment is "false" this modification will result to a +0)
            	L2Item tempItem = ItemTable.getInstance().createDummyItem(ing.getItemId()).getItem();
            	if ((tempItem instanceof L2Armor) || (tempItem instanceof L2Weapon))
            	{
            		newIngredient.setEnchantmentLevel(enchantLevel);
            	}
            }
        	newEntry.addProduct(newIngredient);
        }
        return newEntry;
    }

    public void createMultiSell(int listId, L2PcInstance player, boolean inventoryOnly, L2NpcInstance merchant)
    {
        MultiSellListContainer list = generateMultiSell(listId, inventoryOnly, player, merchant);
        MultiSellListContainer temp = new MultiSellListContainer();
        int page = 1;

        temp.setListId(list.getListId());
        for (MultiSellEntry e : list.getEntries())
        {
            if (temp.getEntries().size() == 40)
            {
                player.sendPacket(new MultiSellList(temp, page++, 0));
                temp = new MultiSellListContainer();
                temp.setListId(list.getListId());
            }
            temp.addEntry(e);
        }
        player.sendPacket(new MultiSellList(temp, page, 1));
    }
    
    private static class SingletonHolder
	{
		protected static final L2Multisell _instance = new L2Multisell();
	}
}