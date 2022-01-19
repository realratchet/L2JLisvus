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
package net.sf.l2j.gsregistering;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.Server;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.loginserver.GameServerTable;

public class GameServerRegister
{
	private static String _choice;

	public static void main(String[] args) throws IOException
	{
		Server.SERVER_MODE = Server.MODE_LOGINSERVER;
		Config.load();

		L2DatabaseFactory.getInstance();

		GameServerTable gameServerTable = GameServerTable.getInstance();
		System.out.println("Welcome to L2JLisvus GameServer Registration.");
		System.out.println("Enter the ID of the server you want to register.");
		System.out.println("Type 'help' to get a list of IDs.");
		System.out.println("Type 'clean' to unregister all currently registered gameservers on this LoginServer.");

		try (InputStreamReader ir = new InputStreamReader(System.in);
			LineNumberReader _in = new LineNumberReader(ir))
		{
			while (true)
			{
				System.out.println("Your choice:");
				_choice = _in.readLine();

				if (_choice.equalsIgnoreCase("help"))
				{
					for (Map.Entry<Integer, String> entry : gameServerTable.getServerNames().entrySet())
					{
						System.out.println("Server: ID: " + entry.getKey() + "\t- " + entry.getValue() + " - In Use: " + (gameServerTable.hasRegisteredGameServerOnId(entry.getKey()) ? "YES" : "NO"));
					}
					System.out.println("You can also see servername.xml");
				}
				else if (_choice.equalsIgnoreCase("clean"))
				{
					System.out.print("This is going to UNREGISTER ALL servers from this LoginServer. Are you sure? (y/n) ");
					_choice = _in.readLine();
					if (_choice.equals("y"))
					{
						GameServerRegister.cleanRegisteredGameServersFromDB();
						gameServerTable.getRegisteredGameServers().clear();
					}
					else
					{
						System.out.println("ABORTED");
					}
				}
				else
				{
					try
					{
						int id = Integer.valueOf(_choice).intValue();
						int size = gameServerTable.getServerNames().size();
						
						if (size == 0)
						{
							System.out.println("No server names available, please make sure that servername.xml is in the LoginServer directory.");
							System.exit(1);
						}
						
						String name = gameServerTable.getServerNameById(id);
						if (name == null)
						{
							System.out.println("No server name for id: " + id);
							continue;
						}
						
						if (gameServerTable.hasRegisteredGameServerOnId(id))
						{
							System.out.println("This id is not free!");
						}
						else
						{
							byte[] hexId = LoginServerThread.generateHex(16);
							gameServerTable.registerServerOnDB(hexId, id, "");
							Config.saveHexid(id, new BigInteger(hexId).toString(16), "hexid.txt");
							System.out.println("Server Registered hexid saved to 'hexid.txt'");
							System.out.println("Put this file in the /config folder of your gameserver.");
							System.exit(0);
						}
					}
					catch (NumberFormatException nfe)
					{
						System.out.println("Please type a number or 'help'.");
					}
				}
			}
		}
	}
	
	public static void cleanRegisteredGameServersFromDB()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM gameservers"))
		{
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println("SQL error while cleaning registered servers: " + e);
		}
	}
}