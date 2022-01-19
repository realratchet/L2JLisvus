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
package net.sf.l2j.accountmanager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.Server;

/**
 * This class SQL Account Manager
 * 
 * @author netimperia
 * @version $Revision: 2.3.2.1.2.3 $ $Date: 2005/08/08 22:47:12 $
 */
public class SQLAccountManager
{
    private static String _uname = "";
    private static String _pass = "";
    private static String _level = "";
    private static String _mode = "";

    public static void main(String[] args) throws SQLException, IOException, NoSuchAlgorithmException
    {
        Server.SERVER_MODE = Server.MODE_LOGINSERVER;
        Config.load();

        L2DatabaseFactory.getInstance();

        try (InputStreamReader ir = new InputStreamReader(System.in);
            LineNumberReader _in = new LineNumberReader(ir))
        {
            while (true)
            {
                System.out.println("Please choose an option:");
                System.out.println("");
                System.out.println("1 - Create new account or update existing one (change pass and access level).");
                System.out.println("2 - Change access level.");
                System.out.println("3 - List accounts & access levels.");		
                System.out.println("4 - Exit.");

                while (!(_mode.equals("1") || _mode.equals("2") || _mode.equals("3") 
                        || _mode.equals("4")))
                {
                    System.out.print("Your choice: ");
                    _mode = _in.readLine();
                }

                if (_mode.equals("1") || _mode.equals("2"))
                {
                    if (_mode.equals("1") || _mode.equals("2"))
                    {
                        while (_uname.trim().length() == 0)
                        {
                            System.out.print("Account name: ");
                            _uname = _in.readLine().toLowerCase();
                        }
                    }

                    if (_mode.equals("1"))
                    {
                        while (_pass.trim().length() == 0)
                        {
                            System.out.print("Password: ");
                            _pass = _in.readLine();
                        }
                    }

                    if (_mode.equals("1") || _mode.equals("2"))
                    {
                        while (_level.trim().length() == 0)
                        {
                            System.out.print("Access level: ");
                            _level = _in.readLine();
                        }
                    }
                }

                if (_mode.equals("1"))
                {
                    // Add or Update
                    AddOrUpdateAccount(_uname.trim(),_pass.trim(),_level.trim());
                }
                else if (_mode.equals("2"))
                {
                    // Change Level
                    ChangeAccountLevel(_uname.trim(),_level.trim());
                }
                else if (_mode.equals("3"))
                {
                    // List
                    _mode="";
                    System.out.println("");
                    System.out.println("Please choose a listing mode:");
                    System.out.println("");
                    System.out.println("1 - Banned accounts only (accessLevel < 0)");
                    System.out.println("2 - GM/privileged accounts (accessLevel > 0)");
                    System.out.println("3 - Regular accounts only (accessLevel = 0)");
                    System.out.println("4 - List all");
                    while (!(_mode.equals("1") || _mode.equals("2") || _mode.equals("3") || _mode.equals("4")))
                    {
                        System.out.print("Your choice: ");
                        _mode = _in.readLine();
                    }

                    System.out.println("");
                    printAccInfo(_mode);			
                }
                else if (_mode.equals("4"))
                    System.exit(0);

                _uname = "";
                _pass = "";
                _level = "";
                _mode = "";
                System.out.println();
            }
        }
    }

    private static void printAccInfo(String m) throws SQLException 
    {
        int count = 0;

        String q = "SELECT login, access_level FROM accounts ";
        if (m.equals("1"))
            q=q.concat("WHERE access_level<0");
        else if (m.equals("2"))
            q=q.concat("WHERE access_level>0");
        else if (m.equals("3"))
            q=q.concat("WHERE access_level=0");
        q=q.concat(" ORDER BY login ASC");

        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(q);
            ResultSet rset = statement.executeQuery())
        {
            while (rset.next())
            {
                System.out.println(rset.getString("login")	+ " -> " + rset.getInt("access_level"));
                count++;
            }

            System.out.println("Displayed accounts: " + count + ".");
        }
    }

    private static void AddOrUpdateAccount(String account, String password, String level) throws IOException, SQLException, NoSuchAlgorithmException		
    {
        // Encode Password		
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] newpass;
        newpass = password.getBytes("UTF-8");
        newpass = md.digest(newpass);

        // Add to Base
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();	
            PreparedStatement statement = con.prepareStatement("REPLACE accounts (login, password, access_level) VALUES (?,?,?)"))
        {
            statement.setString(1, account);
            statement.setString(2, Base64.getEncoder().encodeToString(newpass));
            statement.setString(3, level);
            statement.executeUpdate();
        }
    }

    private static void ChangeAccountLevel(String account, String level) throws SQLException		
    {
        // Check if Account Exists
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();		
            PreparedStatement statement = con.prepareStatement("SELECT COUNT(*) FROM accounts WHERE login=?;"))
        {
            statement.setString(1, account);
            try (ResultSet rset = statement.executeQuery())
            {
                if (rset.next())
                {
                    // Exists
                    // Update
                    try (PreparedStatement statement2 = con.prepareStatement("UPDATE accounts SET access_level=? WHERE login=?;"))
                    {
                        statement2.setEscapeProcessing(true);
                        statement2.setString(1, level);
                        statement2.setString(2, account);
                        statement2.executeUpdate();
                    }

                    System.out.println("Account " + account + " has been updated.");				
                }
                else
                {		
                    // Not Exist
                    System.out.println("Account " + account + " does not exist.");		
                }
            }
        }
    }
}