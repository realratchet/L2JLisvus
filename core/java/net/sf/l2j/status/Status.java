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
package net.sf.l2j.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sf.l2j.Config;
import net.sf.l2j.Server;
import net.sf.l2j.util.Rnd;

public class Status extends Thread
{
    private ServerSocket    statusServerSocket;
    
    private int             		_uptime;
    private String          		_StatusPW;
    private int	                        _mode;
    private List<LoginStatusThread> _loginStatus;
    
    @Override
	public void run()
    {
        setPriority(Thread.MAX_PRIORITY);
        while (true)
        {
            try
            {
                Socket connection = statusServerSocket.accept();

                if (_mode == Server.MODE_GAMESERVER)
                {
                	new GameStatusThread(connection, _uptime, _StatusPW);
                }
                else if(_mode == Server.MODE_LOGINSERVER)
                {
                	LoginStatusThread lst = new LoginStatusThread(connection, _uptime, _StatusPW);
                	if(lst.isAlive())
                	{
                		_loginStatus.add(lst);
                	}
                }
                if (this.isInterrupted())
                {
                    try
                    {
                        statusServerSocket.close();
                    }
                    catch (IOException io) { io.printStackTrace(); }
                    break;
                }
            }
            catch (IOException e)
            {
                if (this.isInterrupted())
                {
                    try
                    {
                        statusServerSocket.close();
                    }
                    catch (IOException io) { io.printStackTrace(); }
                    break;
                }
            }
        }
    }
    
    public Status(int mode) throws IOException
    {
        super("Status");
        _mode= mode;
        Properties telnetSettings = new Properties();
        try (InputStream is = new FileInputStream(new File(Config.TELNET_FILE)))
        {
            telnetSettings.load(is);
        }

        _StatusPW = telnetSettings.getProperty("StatusPW");
        if (_mode == Server.MODE_GAMESERVER || _mode == Server.MODE_LOGINSERVER)
        {
		    if (_StatusPW == null)
		    {
		        System.out.println("Server's Telnet Function Has No Password Defined!");
		        System.out.println("A Password Has Been Automatically Created!");
		        _StatusPW = rndPW(10);
		        System.out.println("Password Has Been Set To: " + _StatusPW);
		    }
	
		    System.out.println("Telnet StatusServer started successfully, listening on Port: " + Config.TELNET_PORT);
        }

        statusServerSocket = new ServerSocket(Config.TELNET_PORT);
        _uptime = (int) System.currentTimeMillis();
        _loginStatus = new ArrayList<>();
    }

    private String rndPW(int length)
    {
        StringBuilder password = new StringBuilder(length);
        String lowerChar= "qwertyuiopasdfghjklzxcvbnm";
        String upperChar = "QWERTYUIOPASDFGHJKLZXCVBNM";
        String digits = "1234567890";

        for (int i = 0; i < length; i++)
        {
            int charSet = Rnd.nextInt(3);
            switch (charSet)
            {
                case 0:
                    password.append(lowerChar.charAt(Rnd.nextInt(lowerChar.length()-1)));
                    break;
                case 1:
                    password.append(upperChar.charAt(Rnd.nextInt(upperChar.length()-1)));
                    break;
                case 2:
                    password.append(digits.charAt(Rnd.nextInt(digits.length()-1)));
                    break;
            }
        }
        return password.toString();
    }
    
    public void sendMessageToTelnets(String msg)
    {
    	List<LoginStatusThread> lsToRemove = new ArrayList<>();
    	for(LoginStatusThread ls : _loginStatus)
    	{
    		if(ls.isInterrupted())
    		{
    			lsToRemove.add(ls);
    		}
    		else
    		{
    			ls.printToTelnet(msg);
    		}
    	}
    }
}