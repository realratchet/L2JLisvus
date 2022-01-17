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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.datatables.CharTemplateTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SkillTreeTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.quest.Quest;
import net.sf.l2j.gameserver.model.quest.QuestState;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.network.L2GameClient.GameClientState;
import net.sf.l2j.gameserver.network.serverpackets.CharCreateFail;
import net.sf.l2j.gameserver.network.serverpackets.CharCreateOk;
import net.sf.l2j.gameserver.network.serverpackets.CharSelectInfo;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2PcTemplate;
import net.sf.l2j.gameserver.templates.L2PcTemplate.InitialItem;
import net.sf.l2j.gameserver.util.Util;

/**
 * This class ...
 * @version $Revision: 1.9.2.3.2.8 $ $Date: 2005/03/27 15:29:30 $
 */
@SuppressWarnings("unused")
public class CharacterCreate extends L2GameClientPacket
{
	private static final String _C__0B_CHARACTERCREATE = "[C] 0B CharacterCreate";
	private static Logger _log = Logger.getLogger(CharacterCreate.class.getName());

	// cSdddddddddddd
	private String _name;
	private int _race;
	private byte _sex;
	private int _classId;
	private int _int;
	private int _str;
	private int _con;
	private int _men;
	private int _dex;
	private int _wit;
	private byte _hairStyle;
	private byte _hairColor;
	private byte _face;

	@Override
	protected void readImpl()
	{
		_name = readS();
		_race = readD();
		_sex = (byte) readD();
		_classId = readD();
		_int = readD();
		_str = readD();
		_con = readD();
		_men = readD();
		_dex = readD();
		_wit = readD();
		_hairStyle = (byte) readD();
		_hairColor = (byte) readD();
		_face = (byte) readD();
	}

	@Override
	public void runImpl()
	{
		if (getClient().getState() != GameClientState.AUTHED)
		{
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}
		
		if (_name.isEmpty() || _name.length() > 16 || !Util.isAlphaNumeric(_name) || !Util.isValidName(_name, Config.CHAR_NAME_TEMPLATE))
		{
			if (Config.DEBUG)
			{
				_log.fine("Character name '" + _name + "' is invalid. Creation failed.");
			}
			sendPacket(new CharCreateFail(CharCreateFail.REASON_16_ENG_CHARS));
			return;
		}
		
		if (_face > 2 || _face < 0)
		{
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}
		
		if (_hairStyle < 0 || _sex == 0 && _hairStyle > 4 || _sex != 0 && _hairStyle > 6)
		{
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}
		
		if (_hairColor > 3 || _hairColor < 0)
		{
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}
		
		L2PcInstance newChar = null;

		/*
		 * DrHouse: Since checks for duplicate names are done using SQL, lock must be held until data is written to DB as well.
		 */
		synchronized (CharNameTable.getInstance())
		{
			if ((CharNameTable.getInstance().getAccountCharacterCount(getClient().getAccountName()) >= Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT) && (Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT != 0))
			{
				if (Config.DEBUG)
				{
					_log.fine("Max number of characters reached. Creation failed.");
				}
				sendPacket(new CharCreateFail(CharCreateFail.REASON_TOO_MANY_CHARACTERS));
				return;
			}
			
			if (CharNameTable.getInstance().doesCharNameExist(_name))
			{
				if (Config.DEBUG)
				{
					_log.fine("Character name '" + _name + "' already in use. Creation failed.");
				}
				sendPacket(new CharCreateFail(CharCreateFail.REASON_NAME_ALREADY_EXISTS));
				return;
			}

			L2PcTemplate template = CharTemplateTable.getInstance().getTemplate(_classId);

			if (Config.DEBUG)
			{
				_log.fine("CharName " + _name + " classId: " + _classId + " template: " + template);
			}

			if (template == null || template.classBaseLevel > 1)
			{
				sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
				return;
			}

			int objectId = IdFactory.getInstance().getNextId();
			newChar = L2PcInstance.create(objectId, template, getClient().getAccountName(), _name, _hairStyle, _hairColor, _face, _sex != 0);
		}
		
		// HP and MP are at maximum and CP is zero by default
		newChar.setCurrentHp(newChar.getMaxHp());
		newChar.setCurrentMp(newChar.getMaxMp());
		
		// Send acknowledgement
		sendPacket(new CharCreateOk());
		
		initNewChar(getClient(), newChar);
	}

	private void initNewChar(L2GameClient client, L2PcInstance newChar)
	{
		if (Config.DEBUG)
		{
			_log.fine("Character init start");
		}
		L2World.getInstance().storeObject(newChar);

		L2PcTemplate template = newChar.getTemplate();

		if (Config.STARTING_ADENA > 0)
		{
			newChar.addAdena("Init", Config.STARTING_ADENA, null, false);
		}

		if (Config.CUSTOM_STARTING_SPAWN)
		{
			newChar.setXYZInvisible(Config.CUSTOM_SPAWN_X, Config.CUSTOM_SPAWN_Y, Config.CUSTOM_SPAWN_Z);
		}
		else
		{
			newChar.setXYZInvisible(template.spawnX, template.spawnY, template.spawnZ);
		}

		newChar.setTitle("");

		L2ShortCut shortcut;
		// add attack shortcut
		shortcut = new L2ShortCut(0, 0, 3, 2, -1, 1);
		newChar.registerShortCut(shortcut);
		// add take shortcut
		shortcut = new L2ShortCut(3, 0, 3, 5, -1, 1);
		newChar.registerShortCut(shortcut);
		// add sit shortcut
		shortcut = new L2ShortCut(10, 0, 3, 0, -1, 1);
		newChar.registerShortCut(shortcut);

		InitialItem[] items = template.getItems();
		for (InitialItem it : items)
		{
			L2Item t = ItemTable.getInstance().getTemplate(it.getItemId());
			if (t == null)
			{
				_log.warning("Could not create item during char creation: Item ID " + it.getItemId() + ".");
				continue;
			}
			
			// Create items
			if (t.isStackable())
			{
				L2ItemInstance item = newChar.getInventory().addItem("Init", it.getItemId(), it.getCount(), newChar, null);
				if (item == null)
				{
					continue;
				}
				
				// Add item shortcut
				if (it.getShortcutPage() >= 0 && it.getShortcutSlot() >= 0)
				{
					shortcut = new L2ShortCut(it.getShortcutSlot(), it.getShortcutPage(), 1, item.getObjectId(), -1, 1);
					newChar.registerShortCut(shortcut);
				}
				
				if (item.isEquipable() && it.toEquip())
				{
					newChar.getInventory().equipItemAndRecord(item);
				}
			}
			else
			{
				for (int i = 0; i < it.getCount(); i++)
				{
					L2ItemInstance item = newChar.getInventory().addItem("Init", it.getItemId(), 1, newChar, null);
					if (item == null)
					{
						continue;
					}
					
					// Add item shortcut
					if (it.getShortcutPage() >= 0 && it.getShortcutSlot() >= 0)
					{
						shortcut = new L2ShortCut(it.getShortcutSlot(), it.getShortcutPage(), 1, item.getObjectId(), -1, 1);
						newChar.registerShortCut(shortcut);
					}
					
					if (item.isEquipable() && it.toEquip())
					{
						newChar.getInventory().equipItemAndRecord(item);
					}
				}
			}
		}

		L2SkillLearn[] startSkills = SkillTreeTable.getInstance().getAvailableSkills(newChar, newChar.getClassId());
		for (L2SkillLearn startSkill : startSkills)
		{
			newChar.addSkill(SkillTable.getInstance().getInfo(startSkill.getId(), startSkill.getLevel()), true);
			if ((startSkill.getId() == 1001) || (startSkill.getId() == 1177))
			{
				shortcut = new L2ShortCut(1, 0, 2, startSkill.getId(), startSkill.getLevel(), 1);
				newChar.registerShortCut(shortcut);
			}

			if (startSkill.getId() == 1216)
			{
				shortcut = new L2ShortCut(9, 0, 2, startSkill.getId(), startSkill.getLevel(), 1);
				newChar.registerShortCut(shortcut);
			}

			if (Config.DEBUG)
			{
				_log.fine("adding starter skill:" + startSkill.getId() + " / " + startSkill.getLevel());
			}
		}

		if (Config.ALT_ENABLE_TUTORIAL)
		{
			startTutorialQuest(newChar);
		}

		newChar.logout();

		// Send character list
		CharSelectInfo cl = new CharSelectInfo(client.getAccountName(), client.getSessionId().playOkID1);
		client.getConnection().sendPacket(cl);
		client.setCharSelection(cl.getCharInfo());
		if (Config.DEBUG)
		{
			_log.fine("Character init end");
		}
	}

	private void startTutorialQuest(L2PcInstance player)
	{
		final Quest q = QuestManager.getInstance().getQuest("255_Tutorial");
		if (q != null)
		{
			q.newQuestState(player);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.clientpackets.L2GameClientPacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__0B_CHARACTERCREATE;
	}
}