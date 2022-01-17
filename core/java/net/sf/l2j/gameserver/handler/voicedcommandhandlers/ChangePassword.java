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
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Nik
 */
public class ChangePassword implements IVoicedCommandHandler
{
	private static final Logger _log = Logger.getLogger(ChangePassword.class.getName());
	
	private static final String[] _voicedCommands =
	{
		"changepassword"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (!Config.PASSWORD_CHANGE_ENABLE)
		{
			return false;
		}
		
		if (target.isEmpty())
		{
			NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile("data/html/mods/ChangePassword.htm");
			activeChar.sendPacket(html);
			return true;
		}
		
		final StringTokenizer st = new StringTokenizer(target);
		try
		{
			String curPass = null, newPass = null, repeatNewPass = null;
			if (st.hasMoreTokens())
			{
				curPass = st.nextToken();
			}
			if (st.hasMoreTokens())
			{
				newPass = st.nextToken();
			}
			if (st.hasMoreTokens())
			{
				repeatNewPass = st.nextToken();
			}
			
			if (curPass != null && newPass != null && repeatNewPass != null)
			{
				if (!newPass.equals(repeatNewPass))
				{
					activeChar.sendMessage("The new password does not match with the repeated one!");
					return false;
				}
				
				if (newPass.isEmpty())
				{
					activeChar.sendMessage("The new password is empty! Please fill a new password.");
					return false;
				}
				
				if (newPass.length() > 30)
				{
					activeChar.sendMessage("The new password is longer than 30 chars! Please try with a shorter one.");
					return false;
				}
				
				// Change password
				tryChangePassword(activeChar, curPass, newPass);
			}
			else
			{
				activeChar.sendMessage("Invalid password data! You have to fill all boxes.");
				return false;
			}
		}
		catch (Exception e)
		{
			activeChar.sendMessage("A problem occured while changing password!");
			_log.log(Level.WARNING, "", e);
		}
		return true;
	}
	
	private void tryChangePassword(L2PcInstance activeChar, String curPass, String newPass) throws Exception
	{
		String accountName = activeChar.getAccountName();
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] raw = curPass.getBytes("UTF-8");
			byte[] hash = md.digest(raw);
			
			byte[] expected = null;
			
			// Get current password from database
			try (PreparedStatement statement = con.prepareStatement("SELECT password FROM accounts WHERE login=?"))
			{
				statement.setString(1, accountName);
				try (ResultSet rset = statement.executeQuery())
				{
					if (rset.next())
					{
						expected = Base64.getDecoder().decode(rset.getString("password"));
					}
				}
			}
			
			if (expected == null)
			{
				_log.warning("Failed to retrieve data for account '" + accountName + "' of player '" + activeChar.getName() + "'.");
				return;
			}
			
			boolean isValid = true;
			// Check if given password matches current password
			for (int i = 0; i < expected.length; i++)
			{
				if (hash[i] != expected[i])
				{
					isValid = false;
					break;
				}
			}
			
			// Password is not correct
			if (!isValid)
			{
				activeChar.sendMessage("Your current password does not match with given password.");
				return;
			}
			
			if (isValid)
			{
				// Hash new password
				raw = newPass.getBytes("UTF-8");
				hash = md.digest(raw);
				
				try (PreparedStatement statement = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?"))
				{
					statement.setString(1, Base64.getEncoder().encodeToString(hash));
					statement.setString(2, accountName);
					statement.execute();
				}
				
				activeChar.sendMessage("Your password has been successfully updated.");
			}
		}
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
