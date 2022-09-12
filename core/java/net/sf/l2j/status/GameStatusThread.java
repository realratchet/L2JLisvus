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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.Announcements;
import net.sf.l2j.gameserver.GameTimeController;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.Shutdown;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.datatables.GmListTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.TeleportLocationTable;
import net.sf.l2j.gameserver.instancemanager.Manager;
import net.sf.l2j.gameserver.instancemanager.QuestManager;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Multisell;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.TradeList;
import net.sf.l2j.gameserver.model.TradeList.TradeItem;
import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance.PrivateStoreType;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.taskmanager.DecayTaskManager;
import net.sf.l2j.gameserver.util.DynamicExtension;
import net.sf.l2j.util.StringUtil;

public class GameStatusThread extends Thread
{
    //private static final Logger _log = Logger.getLogger(AdminTeleport.class.getName());
    
    private Socket                  _csocket;
    
    private PrintWriter             _print;
    private BufferedReader          _read;
    
    private int                     _uptime;
      
    private void telnetOutput(int type, String text) 
    {
    	if (Config.DEVELOPER)
    	{
    		if ( type == 1 ) System.out.println("TELNET | "+text);
    		else if ( type == 2 ) System.out.print("TELNET | "+text);
    		else if ( type == 3 ) System.out.print(text);
    		else if ( type == 4 ) System.out.println(text);
    		else System.out.println("TELNET | "+text);
    	}
    	else
    	{
    		//only print output if the message is rejected
    		if ( type == 5 ) System.out.println("TELNET | "+text);
    	}
    }
    
    private boolean isValidIP(Socket client)
    {
        boolean result = false;        
        InetAddress ClientIP = client.getInetAddress();
               
        // convert IP to String, and compare with list
        String clientStringIP = ClientIP.getHostAddress();
        
        telnetOutput(1, "Connection from: "+clientStringIP);
        
        // read and loop thru list of IPs, compare with newIP
        if ( Config.DEVELOPER ) telnetOutput(2, "");

        Properties telnetSettings = new Properties();
        try (InputStream telnetIS = new FileInputStream(new File(Config.TELNET_FILE)))
        {
            
            telnetSettings.load(telnetIS);

            String HostList = telnetSettings.getProperty("ListOfHosts", "127.0.0.1,localhost,::1");

            if (Config.DEVELOPER)
                telnetOutput(3, "Comparing ip to list...");

            // compare
            String ipToCompare = null;
            for (String ip:HostList.split(","))
            {
                if (!result)
                {
                    ipToCompare = InetAddress.getByName(ip).getHostAddress();
                    if (clientStringIP.equals(ipToCompare))
                        result = true;
                    if (Config.DEVELOPER)
                        telnetOutput(3, clientStringIP + " = " + ipToCompare + "("+ip+") = " + result);
                }
            }
        }
        catch (IOException e)
        {
            if (Config.DEVELOPER)
                telnetOutput(4, "");
            telnetOutput(1, "Error: "+e);
        }

        if (Config.DEVELOPER)
            telnetOutput(4, "Allow IP: "+result);

        return result;
    }
    
    public GameStatusThread(Socket client, int uptime, String StatusPW) throws IOException
    {
        setPriority(Thread.MAX_PRIORITY);
        _csocket = client;
        _uptime = uptime;
        
        _print = new PrintWriter(_csocket.getOutputStream());
        _read  = new BufferedReader(new InputStreamReader(_csocket.getInputStream()));
        
        if (isValidIP(client))
        {    
            telnetOutput(1, client.getInetAddress().getHostAddress()+" accepted.");
            _print.println("Welcome To The L2JLisvus Telnet Session.");
            _print.println("Please Insert Your Password!");
            _print.print("Password: ");
            _print.flush();
            String tmpLine = _read.readLine();
            if ( tmpLine == null)
            {
                _print.println("Error.");
                _print.println("Disconnected...");
                _print.flush();
                _csocket.close();
            }
            else
            {
                if (!tmpLine.equals(StatusPW))
                {
                    _print.println("Incorrect Password!");
                    _print.println("Disconnected...");
                    _print.flush();
                    _csocket.close();
                }
                else
                {
                    _print.println("Password Correct!");
                    _print.println("[L2JLisvus GameServer]");
                    _print.print("");
                    _print.flush();
                    start();
                }
            }
        }
        else
        {
            telnetOutput(5, "Connection attempt from "+ client.getInetAddress().getHostAddress() +" rejected.");
            _csocket.close();
        }
    }

    @Override
    public void run()
    {
        String _usrCommand = "";
        try
        {
            while (_usrCommand.compareTo("quit") != 0 && _usrCommand.compareTo("exit") != 0)
            {
                _usrCommand = _read.readLine();
                if(_usrCommand == null)
                {
                	_csocket.close();
                	break;
                }
                if (_usrCommand.equals("help"))
                {
                    _print.println("The following is a list of all available commands: ");
                    _print.println("help                - shows this help.");
                    _print.println("status              - displays basic server statistics.");
                    _print.println("performance         - shows server performance statistics.");
                    _print.println("forcegc             - forced garbage collection.");
                    _print.println("purge               - removes finished threads from thread pools.");
                    _print.println("announce <text>     - announces <text> in game.");
                    _print.println("msg <nick> <text>   - Sends a whisper to char <nick> with <text>.");
                    _print.println("gmchat <text>       - Sends a message to all GMs with <text>.");
                    _print.println("gmlist              - lists all gms online.");
                    _print.println("kick                - kick player <name> from server.");
                    _print.println("shutdown <time>     - shuts down server in <time> seconds.");
                    _print.println("restart <time>      - restarts down server in <time> seconds.");
                    _print.println("abort               - aborts shutdown/restart.");
                    _print.println("give <player> <itemid> <amount>");
                    _print.println("extlist             - list all loaded extension classes");
                    _print.println("extreload <name>    - reload and initializes the named extension or all if used without argument");
                    _print.println("extinit <name>      - initilizes the named extension or all if used without argument");
                    _print.println("extunload <name>    - unload the named extension or all if used without argument");
                    _print.println("debug <cmd>         - executes the debug command (see 'help debug').");
                    _print.println("jail <player> [time]");
                    _print.println("unjail <player>");
                    _print.println("quit                - closes telnet session.");
                }
                else if(_usrCommand.equals("help debug"))
                {
                	_print.println("The following is a list of all available debug commands: ");
                        _print.println("full                - Dumps complete debug information to an file (recommended)");
                	_print.println("decay               - prints info about the DecayManager");
                	_print.println("PacketTP            - prints info about the General Packet ThreadPool");
                	_print.println("IOPacketTP          - prints info about the I/O Packet ThreadPool");
                	_print.println("GeneralTP           - prints info about the General ThreadPool");
                }
                else if (_usrCommand.equals("status"))
                {
                    _print.print(getServerStatus());
                    _print.flush();
                }
                else if (_usrCommand.equals("forcegc"))
                {
                    System.gc();
                    StringBuilder sb = new StringBuilder();
                    sb.append("RAM Used: "+((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576)); // 1024 * 1024 = 1048576
                    _print.println(sb.toString());
                }
                else if (_usrCommand.equals("performance"))
                {
                    for (String line : ThreadPoolManager.getInstance().getStats())
                    {
                        _print.println(line);
                    }
                    _print.flush();
                }
                else if (_usrCommand.equals("purge"))
                {
                	ThreadPoolManager.getInstance().purge();
                	_print.println("STATUS OF THREAD POOLS AFTER PURGE COMMAND:");
                	_print.println("");
                	for (String line : ThreadPoolManager.getInstance().getStats())
                	{
                		_print.println(line);
                	}
                	_print.flush();
                }
                else if (_usrCommand.startsWith("announce"))
                {
                    try
                    {
                        _usrCommand = _usrCommand.substring(9);
                        Announcements.getInstance().announceToAll(_usrCommand);
                        _print.println("Announcement Sent!");
                    }
                    catch (StringIndexOutOfBoundsException e)
                    {
                        _print.println("Please Enter Some Text To Announce!");
                    }
                }
                else if (_usrCommand.startsWith("msg"))
                {
                    try
                    {
                        String val = _usrCommand.substring(4);
                        StringTokenizer st = new StringTokenizer(val);
                        String name = st.nextToken();
                        String message = val.substring(name.length()+1);
                        L2PcInstance reciever = L2World.getInstance().getPlayer(name);
                        CreatureSay cs = new CreatureSay(0, Say2.TELL, "Telnet Priv", message);
                        if(reciever != null)
                        {
                            reciever.sendPacket(cs);
                            _print.println("Telnet Priv->" + name + ": " + message);
                            _print.println("Message Sent!");
                        }
                        else
                        {
                            _print.println("Unable To Find Username: " + name);
                        }
                    }
                    catch (StringIndexOutOfBoundsException e)
                    {
                        _print.println("Please Enter Some Text!");
                    }
                }
                else if (_usrCommand.startsWith("gmchat"))
                {
                    try
                    {
                        _usrCommand = _usrCommand.substring(7);
                        CreatureSay cs = new CreatureSay(0, 9, "Telnet GM Broadcast from " + _csocket.getInetAddress().getHostAddress(), _usrCommand);
                        GmListTable.broadcastToGMs(cs);
                        _print.println("Your Message Has Been Sent To " + getOnlineGms() + " GM(s).");
                    }
                    catch (StringIndexOutOfBoundsException e)
                    {
                        _print.println("Please Enter Some Text To Announce!");
                    }
                }
                else if (_usrCommand.equals("gmlist"))
                {
                    int igm = 0;
                    String gmList = "";
                    
                    for (String player : GmListTable.getInstance().getAllGmNames(true))
                    {
                            gmList = gmList + ", " + player;
                            igm++;
                    }
                    _print.println("There are currently " + igm +" GM(s) online...");
                    if (!gmList.isEmpty())
                        _print.println(gmList);
                }
                else if (_usrCommand.startsWith("kick"))
                {
                    try
                    {
                        _usrCommand = _usrCommand.substring(5);
                        L2PcInstance player = L2World.getInstance().getPlayer(_usrCommand);
                        if(player != null)
                        {
                            player.sendMessage("You are kicked by gm.");
                            player.logout();
                            _print.println("Player kicked.");
                        }
                    }
                    catch (StringIndexOutOfBoundsException e)
                    {
                        _print.println("Please enter player name to kick");
                    }
                }
                else if (_usrCommand.startsWith("shutdown"))
                {
                    try
                    {
                        int val = Integer.parseInt(_usrCommand.substring(9)); 
                        Shutdown.getInstance().startTelnetShutdown(_csocket.getInetAddress().getHostAddress(), val, false);
                        _print.println("Server Will Shutdown In " + val + " Seconds!");
                        _print.println("Type \"abort\" To Abort Shutdown!");
                    }
                    catch (StringIndexOutOfBoundsException e)
                    {
                        _print.println("Please Enter * amount of seconds to shutdown!");
                    }
                    catch (Exception NumberFormatException)
                    {
                        _print.println("Numbers Only!");
                    }
                }
                else if (_usrCommand.startsWith("restart"))
                {
                    try
                    {
                        int val = Integer.parseInt(_usrCommand.substring(8)); 
                        Shutdown.getInstance().startTelnetShutdown(_csocket.getInetAddress().getHostAddress(), val, true);
                        _print.println("Server Will Restart In " + val + " Seconds!");
                        _print.println("Type \"abort\" To Abort Restart!");
                    }
                    catch (StringIndexOutOfBoundsException e)
                    {
                        _print.println("Please Enter * amount of seconds to restart!");
                    }
                    catch (Exception NumberFormatException)
                    {
                        _print.println("Numbers Only!");
                    }
                }
                else if (_usrCommand.startsWith("abort"))
                {
                    Shutdown.getInstance().telnetAbort(_csocket.getInetAddress().getHostAddress());
                    _print.println("OK! - Shutdown/Restart Aborted.");
                }
                else if (_usrCommand.equals("quit"))
                {
                    /* Do Nothing :p - Just here to save us from the "Command Not Understood" Text */
                }
                else if (_usrCommand.startsWith("give"))
                {
                    StringTokenizer st = new StringTokenizer(_usrCommand.substring(5));
                    
                    try
                    {
                        L2PcInstance player = L2World.getInstance().getPlayer(st.nextToken());
                        int itemId = Integer.parseInt(st.nextToken());
                        int amount = Integer.parseInt(st.nextToken());
                        
                        if (player != null)
                        {
                            L2ItemInstance item = player.getInventory().addItem("Status-Give", itemId, amount, null, null);
                            InventoryUpdate iu = new InventoryUpdate();
                            iu.addItem(item);
                            player.sendPacket(iu);
                            SystemMessage sm = new SystemMessage(SystemMessage.YOU_PICKED_UP_S1_S2);
                            sm.addItemName(itemId);
                            sm.addNumber(amount);
                            player.sendPacket(sm);
                            _print.println("ok");
                            GMAudit.auditGMAction("Telnet Admin", "Give Item", player.getName(), "item: "+itemId+" amount: "+amount);
                        }
                    }
                    catch(Exception e) {}
                }
                else if (_usrCommand.startsWith("jail"))
                {
                    StringTokenizer st = new StringTokenizer(_usrCommand.substring(5));
                    try
                    {
                        String playerName = st.nextToken();
                        L2PcInstance playerObj = L2World.getInstance().getPlayer(playerName);
                        int delay = 0;
                        try
                        {
                            delay = Integer.parseInt(st.nextToken());
                        }
                        catch (NumberFormatException nfe) {}
                        catch (NoSuchElementException nsee) {}

                        if (playerObj != null)
                        {
                            playerObj.setInJail(true, delay);
                            _print.println("Character "+playerObj.getName()+" jailed for "+(delay>0 ? delay+" minutes." : "ever!"));
                        } else
                        	jailOfflinePlayer(playerName, delay);
                    }
                    catch (NoSuchElementException nsee) 
                    {
                    	_print.println("Specify a character name.");
                    }
                    catch(Exception e)
                    {
                        if (Config.DEBUG) e.printStackTrace();
                    }
                }
                else if (_usrCommand.startsWith("unjail"))
                {
                    StringTokenizer st = new StringTokenizer(_usrCommand.substring(7));
                    try
                    {
                        String playerName = st.nextToken();
                        L2PcInstance playerObj = L2World.getInstance().getPlayer(playerName);

                        if (playerObj != null)
                        {
                            playerObj.stopJailTask(false);
                            playerObj.setInJail(false, 0);
                            _print.println("Character "+playerObj.getName()+" removed from jail");
                        } else
                        	unjailOfflinePlayer(playerName);
                    }
                    catch (NoSuchElementException nsee) 
                    {
                    	_print.println("Specify a character name.");
                    }
                    catch(Exception e)
                    {
                        if (Config.DEBUG)
                            e.printStackTrace();
                    }
                }
                else if (_usrCommand.startsWith("debug") && _usrCommand.length() > 6)
                {
                	StringTokenizer st = new StringTokenizer(_usrCommand.substring(6));
                	try
                	{
                		String dbg = st.nextToken();
                		
                		if (dbg.equals("decay"))
                		{
                			_print.print(DecayTaskManager.getInstance().toString());
                		}
                		else if (dbg.equals("ai"))
                		{
                			// do nothing
                		}
                		else if (dbg.equals("aiflush"))
                		{
                			// do nothing
                		}
                		else if (dbg.equals("PacketTP"))
                		{
                			String str = ThreadPoolManager.getInstance().getPacketStats();
                			_print.println(str);
                			int i = 0;
                			File f = new File("./log/StackTrace-PacketTP-"+i+".txt");
                			while(f.exists())
                			{
                				i++;
                				f = new File("./log/StackTrace-PacketTP-"+i+".txt");
                			}
                			f.getParentFile().mkdirs();
                			try (FileOutputStream fos = new FileOutputStream(f);
                			    OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8"))
                            {
                			    out.write(str);
                			    out.flush();
                			}
                		}
                		else if (dbg.equals("IOPacketTP"))
                		{
                			String str = ThreadPoolManager.getInstance().getIOPacketStats();
                			_print.println(str);
                			int i = 0;
                			File f = new File("./log/StackTrace-IOPacketTP-"+i+".txt");
                			while(f.exists())
                			{
                				i++;
                				f = new File("./log/StackTrace-IOPacketTP-"+i+".txt");
                			}
                			f.getParentFile().mkdirs();
                			try (FileOutputStream fos = new FileOutputStream(f);
                				OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8"))
                            {
                			    out.write(str);
                			    out.flush();
                			}
                		}
                		else if (dbg.equals("GeneralTP"))
                		{
                			String str = ThreadPoolManager.getInstance().getGeneralStats();
                			_print.println(str);
                			int i = 0;
                			File f = new File("./log/StackTrace-GeneralTP-"+i+".txt");
                			while(f.exists())
                			{
                				i++;
                				f = new File("./log/StackTrace-GeneralTP-"+i+".txt");
                			}
                			f.getParentFile().mkdirs();
                			try (FileOutputStream fos = new FileOutputStream(f);
                			    OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8"))
                            {
                			    out.write(str);
                			    out.flush();
                			}
                		}
                        else if (dbg.equals("full"))
                        {
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

                            StringBuilder sb = new StringBuilder(2000);
                            StringUtil.append(sb, sdf.format(cal.getTime()));
                            StringUtil.append(sb, "\n\nProject Tag: " + Config.PROJECT_TAG);
                            StringUtil.append(sb, "\n\n");
                            StringUtil.append(sb, getServerStatus());
                            StringUtil.append(sb, "\n\n");
                            StringUtil.append(sb, "\n## Java Platform Information ##");
                            StringUtil.append(sb, "\nJava Runtime Name: "+System.getProperty("java.runtime.name"));
                            StringUtil.append(sb, "\nJava Version: "+System.getProperty("java.version"));
                            StringUtil.append(sb, "\nJava Class Version: "+System.getProperty("java.class.version"));
                            StringUtil.append(sb, "\n");
                            StringUtil.append(sb, "\n## Virtual Machine Information ##");
                            StringUtil.append(sb, "\nVM Name: "+System.getProperty("java.vm.name"));
                            StringUtil.append(sb, "\nVM Version: "+System.getProperty("java.vm.version"));
                            StringUtil.append(sb, "\nVM Vendor: "+System.getProperty("java.vm.vendor"));
                            StringUtil.append(sb, "\nVM Info: "+System.getProperty("java.vm.info"));
                            StringUtil.append(sb, "\n");
                            StringUtil.append(sb, "\n## OS Information ##");
                            StringUtil.append(sb, "\nName: "+System.getProperty("os.name"));
                            StringUtil.append(sb, "\nArchiteture: "+System.getProperty("os.arch"));
                            StringUtil.append(sb, "\nVersion: "+System.getProperty("os.version"));
                            StringUtil.append(sb, "\n");
                            StringUtil.append(sb, "\n## Runtime Information ##");
                            StringUtil.append(sb, "\nCPU Count: "+Runtime.getRuntime().availableProcessors());
                            StringUtil.append(sb, "\nCurrent Free Heap Size: "+(Runtime.getRuntime().freeMemory() / 1024 / 1024)+" mb");
                            StringUtil.append(sb, "\nCurrent Heap Size: "+(Runtime.getRuntime().totalMemory() / 1024 / 1024)+" mb");
                            StringUtil.append(sb, "\nMaximum Heap Size: "+(Runtime.getRuntime().maxMemory() / 1024 / 1024)+" mb");

                            StringUtil.append(sb, "\n");
                            StringUtil.append(sb, "\n## Class Path Information ##\n");
                            String cp = System.getProperty("java.class.path");
                            String[] libs = cp.split(File.pathSeparator);
                            for (String lib : libs)
                            {
                                StringUtil.append(sb, lib);
                                StringUtil.append(sb, "\n");
                            }

                            StringUtil.append(sb, "\n");
                            StringUtil.append(sb, "## Threads Information ##\n");
                            Map<Thread, StackTraceElement[]> allThread = Thread.getAllStackTraces();
                            List<Entry<Thread, StackTraceElement[]>> entries = new ArrayList<>();
                            entries.addAll(allThread.entrySet());
                            entries.sort(new Comparator<Entry<Thread, StackTraceElement[]>>()
                        	{
                                @Override
                                public int compare(Entry<Thread, StackTraceElement[]> e1, Entry<Thread, StackTraceElement[]> e2)
                                {
                                    return e1.getKey().getName().compareTo(e2.getKey().getName());
                                }
                            });

                            for (Entry<Thread, StackTraceElement[]> entry : entries)
                            {
                                StackTraceElement[] stes = entry.getValue();
                                Thread t = entry.getKey();
                                StringUtil.append(sb, "--------------\n");
                                StringUtil.append(sb, t.toString()+" ("+t.getId()+")\n");
                                StringUtil.append(sb, "State: "+t.getState()+'\n');
                                StringUtil.append(sb, "isAlive: "+t.isAlive()+" | isDaemon: "+t.isDaemon()+" | isInterrupted: "+t.isInterrupted()+'\n');
                                StringUtil.append(sb, "\n");
                                for (StackTraceElement ste : stes)
                                {
                                    StringUtil.append(sb, ste.toString());
                                    StringUtil.append(sb, "\n");
                                }
                                StringUtil.append(sb, "\n");
                            }

                            StringUtil.append(sb, "\n");
                            ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
                            long[] ids = findDeadlockedThreads(mbean);
                            if (ids != null && ids.length > 0)
                            {
                                Thread[] threads = new Thread[ids.length];
                                for (int i = 0; i < threads.length; i++)
                                {
                                    threads[i] = findMatchingThread(mbean.getThreadInfo(ids[i]));
                                }
                                StringUtil.append(sb, "Deadlocked Threads:\n");
                                StringUtil.append(sb, "-------------------\n");
                                for (Thread thread : threads)
                                {
                                    System.err.println(thread);
                                    for (StackTraceElement ste : thread.getStackTrace())
                                    {
                                        StringUtil.append(sb, "\t" + ste);
                                        StringUtil.append(sb, "\n");
                                    }
                                }
                            }

                            StringUtil.append(sb, "\n\n## Thread Pool Manager Statistics ##\n");
                            for (String line : ThreadPoolManager.getInstance().getStats())
                            {
                                StringUtil.append(sb, line);
                                StringUtil.append(sb, "\n");
                            }

                            int i = 0;
                            File f = new File("./log/Debug-"+i+".txt");
                            while(f.exists())
                            {
                                i++;
                                f = new File("./log/Debug-"+i+".txt");
                            }
                            f.getParentFile().mkdirs();
                            try (FileOutputStream fos = new FileOutputStream(f);
                                OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8"))
                            {
                                out.write(sb.toString());
                                out.flush();
                            }

                            _print.println("Debug output saved to log/"+f.getName());
                            _print.flush();
                        }
                	}
                	catch(Exception e){}
                }
                else if (_usrCommand.startsWith("reload"))
                {
                	StringTokenizer st = new StringTokenizer(_usrCommand.substring(7));
                	try
                	{
                		String type = st.nextToken();
                		
                		if(type.equals("multisell"))
                		{
                			_print.print("Reloading multisell... ");
                			L2Multisell.getInstance().reload();
                			_print.print("done\n");
                		}
                		else if(type.equals("skill"))
                		{
                			_print.print("Reloading skills... ");
                			SkillTable.getInstance().reload();
                			_print.print("done\n");
                		}
                		else if(type.equals("npc"))
                		{
                			_print.print("Reloading npc templates along with quests... ");
                			NpcTable.getInstance().reloadAllNpc();
                                        QuestManager.getInstance().reloadAllQuests();
                			_print.print("done\n");
                		}
                		else if(type.equals("html"))
                		{
                			_print.print("Reloading html cache... ");
                			HtmCache.getInstance().reload();
                			_print.print("done\n");
                		}
                		else if(type.equals("item"))
                		{
                			_print.print("Reloading item templates... ");
                			ItemTable.getInstance().load();
                			_print.print("done\n");
                		}
                		else if(type.equals("instancemanager"))
                		{
                			_print.print("Reloading instance managers... ");
                			Manager.reloadAll();
                			_print.print("done\n");
                		}
                        else if(type.equals("teleports"))
                        {
                            _print.print("Reloading telport location table... ");
                            TeleportLocationTable.getInstance().reloadAll();
                            _print.print("done\n");
                        }
                	}
                	catch(Exception e){}
                }
                else if (_usrCommand.startsWith("gamestat"))
                {
                	StringTokenizer st = new StringTokenizer(_usrCommand.substring(9));
                	try
                	{
                		String type = st.nextToken();
                		
                		// name;type;x;y;itemId:enchant:price...
                		if (type.equals("privatestore"))
                		{
                			Collection<L2PcInstance> players = L2World.getInstance().getAllPlayers();
                			for(L2PcInstance player : players)
                			{
                				if (player.getPrivateStoreType() == PrivateStoreType.NONE)
                					continue;
                				
                				TradeList list = null;
                				String content = "";

                				if (player.getPrivateStoreType() == PrivateStoreType.SELL || player.getPrivateStoreType() == PrivateStoreType.PACKAGE_SELL)
                				{
                					list = player.getSellList();                					
                					for(TradeItem item : list.getItems())
                					{
                						content += item.getItem().getItemId()+":"+item.getEnchant()+":"+item.getPrice()+":";
                					}
                					content = player.getName()+";"+"sell;"+player.getX()+";"+player.getY()+";"+content;
                					_print.println(content);
                					continue;
                				}
                				else if (player.getPrivateStoreType() == PrivateStoreType.BUY)
                				{
                					list = player.getBuyList();                					
                					for(TradeItem item : list.getItems())
                					{
                						content += item.getItem().getItemId()+":"+item.getEnchant()+":"+item.getPrice()+":";
                					}
                					content = player.getName()+";"+"buy;"+player.getX()+";"+player.getY()+";"+content;
                					_print.println(content);
                					continue;
                				}

                			}
                		}
                	}
                	catch(Exception e){}
                }
                else if (_usrCommand.startsWith("extreload"))
                {
                    String[] args = _usrCommand.split("\\s+");
                    if (args.length > 1)
                    {
                        for (int i = 1; i < args.length; i++)
                            DynamicExtension.getInstance().reload(args[i]);
                    }
                    else
                    {
                        DynamicExtension.getInstance().reload();
                    }
                }
                else if (_usrCommand.startsWith("extinit"))
                {
                    String[] args = _usrCommand.split("\\s+");
                    if (args.length > 1)
                    {
                        for (int i = 1; i < args.length; i++)
                            _print.print(DynamicExtension.getInstance().initExtension(args[i]) + "\r\n");
                    }
                    else
                    {
                        _print.print(DynamicExtension.getInstance().initExtensions());
                    }
                }
                else if (_usrCommand.startsWith("extunload"))
                {
                    String[] args = _usrCommand.split("\\s+");
                    if (args.length > 1)
                    {
                        for (int i = 1; i < args.length; i++)
                            _print.print(DynamicExtension.getInstance().unloadExtension(args[i]) + "\r\n");
                    }
                    else
                    {
                        _print.print(DynamicExtension.getInstance().unloadExtensions());
                    }
                }
                else if (_usrCommand.startsWith("extlist"))
                {
                    for (String e : DynamicExtension.getInstance().getExtensions())
                        _print.print(e + "\r\n");
                }
                else if (_usrCommand.startsWith("get"))
                {
                    Object o = null;
                    try
                    {
                        String[] args = _usrCommand.substring(3).split("\\s+");
                        if (args.length == 1)
                            o = DynamicExtension.getInstance().get(args[0], null);
                        else
                            o = DynamicExtension.getInstance().get(args[0], args[1]);
                    }
                    catch (Exception ex)
                    {
                        _print.print(ex.toString() + "\r\n");
                    }
                    if (o != null)
                        _print.print(o.toString() + "\r\n");
                }
                else if (_usrCommand.length() > 0)
                {
                    try
                    {
                        String[] args = _usrCommand.split("\\s+");
                        if (args.length == 1)
                            DynamicExtension.getInstance().set(args[0], null, null);
                        else if (args.length == 2)
                            DynamicExtension.getInstance().set(args[0], null, args[1]);
                        else
                            DynamicExtension.getInstance().set(args[0], args[1], args[2]);
                    }
                    catch (Exception ex)
                    {
                        _print.print(ex.toString());
                    }
                }
                else if (_usrCommand.length() == 0) { /* Do Nothing Again - Same reason as the quit part */ }
                _print.print("");
                _print.flush();
            }

            if (!_csocket.isClosed())
            {
	            _print.println("Bye Bye!");
	            _print.flush();
	            _csocket.close();
            }
            telnetOutput(1, "Connection from "+_csocket.getInetAddress().getHostAddress()+" was closed by client.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void jailOfflinePlayer(String name, int delay)
    {
    	try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=?, in_jail=?, jail_timer=? WHERE char_name=?"))
       	{
            statement.setInt(1, -114356);
       	    statement.setInt(2, -249645);
            statement.setInt(3, -2984);
       	    statement.setInt(4, 1);
       	    statement.setLong(5, delay * 60000);
       	    statement.setString(6, name);
            statement.execute();

            int count = statement.getUpdateCount();
            if (count == 0)
                _print.println("Character not found!");
            else
                _print.println("Character "+name+" jailed for "+(delay>0 ? delay+" minutes." : "ever!"));
       	}
        catch (SQLException se)
       	{
            _print.println("SQLException while jailing player");
            if (Config.DEBUG)
                se.printStackTrace();
       	}
    }
    
    private void unjailOfflinePlayer(String name)
    {
    	try (Connection con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=?, in_jail=?, jail_timer=? WHERE char_name=?"))
       	{
            statement.setInt(1, 17836);
       	    statement.setInt(2, 170178);
            statement.setInt(3, -3507);
            statement.setInt(4, 0);
            statement.setLong(5, 0);
            statement.setString(6, name);
            statement.execute();

            int count = statement.getUpdateCount();
            if (count == 0)
                _print.println("Character not found!");
            else
                _print.println("Character "+name+" set free.");
       	}
        catch (SQLException se)
       	{
            _print.println("SQLException while jailing player");
            if (Config.DEBUG)
                se.printStackTrace();
       	}
    }
    
    private int getOnlineGms()
    {
        return GmListTable.getInstance().getAllGms(true).size();
    }
    
    private String getUptime(int time)
    {
        int uptime = (int)System.currentTimeMillis() - time;
        uptime = uptime / 1000;
        int h = uptime / 3600;
        int m = (uptime-(h*3600))/60;
        int s = ((uptime-(h*3600))-(m*60));
        return h + "hrs " + m + "mins " + s + "secs";
    }
    
    private String getGameTime()
    {
        int t = GameTimeController.getInstance().getGameTime();
        int h = t/60;
        int m = t%60;
        SimpleDateFormat format = new SimpleDateFormat("H:mm");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
        return format.format(cal.getTime());
    }

    public String getServerStatus()
    {
        int playerCount = 0, objectCount = 0;
        int max = LoginServerThread.getInstance().getMaxPlayer();

        playerCount = L2World.getInstance().getAllPlayersCount();
        objectCount = L2World.getInstance().getAllVisibleObjectsCount();

        int itemCount=0;
        int itemVoidCount=0;
        int monsterCount=0;
        int minionCount = 0;
        int minionsGroupCount = 0;
        int npcCount=0;
        int charCount=0;
        int pcCount=0;
        int doorCount=0;
        int summonCount=0;
        int AICount=0;

        for (L2Object obj : L2World.getInstance().getAllVisibleObjects())
        {
            if(obj == null)
                continue;
            if (obj instanceof L2Character)
                if (((L2Character)obj).hasAI())
                    AICount++;
            if (obj instanceof L2ItemInstance)
                if (((L2ItemInstance)obj).getLocation() == L2ItemInstance.ItemLocation.VOID)
                    itemVoidCount++;
                else
                    itemCount++;

            else if (obj instanceof L2MonsterInstance)
            {
                monsterCount++;
                minionCount += ((L2MonsterInstance)obj).getTotalSpawnedMinionsInstances();
                minionsGroupCount += ((L2MonsterInstance)obj).getTotalSpawnedMinionsGroups();
            }
            else if (obj instanceof L2NpcInstance)
                npcCount++;
            else if (obj instanceof L2PcInstance)
                pcCount++;
            else if (obj instanceof L2Summon)
                summonCount++;
            else if (obj instanceof L2DoorInstance)
                doorCount++;
            else if (obj instanceof L2Character)
                charCount++;
        }
        String sb = StringUtil.concat(
        	"Server Status: ",
       		"\r\n  --->  Player Count: " + playerCount + "/" + max,
        	"\r\n  +-->  Object Count: " + objectCount,
        	"\r\n  +-->      AI Count: " + AICount,
        	"\r\n  +.... L2Item(Void): " + itemVoidCount,
        	"\r\n  +.......... L2Item: " + itemCount,
        	"\r\n  +....... L2Monster: " + monsterCount,
        	"\r\n  +......... Minions: " + minionCount,
        	"\r\n  +.. Minions Groups: " + minionsGroupCount,
        	"\r\n  +........... L2Npc: " + npcCount,
        	"\r\n  +............ L2Pc: " + pcCount,
        	"\r\n  +........ L2Summon: " + summonCount,
        	"\r\n  +.......... L2Door: " + doorCount,
        	"\r\n  +.......... L2Char: " + charCount,
        	"\r\n  --->   Ingame Time: " + getGameTime(),
        	"\r\n  ---> Server Uptime: " + getUptime(_uptime),
        	"\r\n  --->      GM Count: " + getOnlineGms(),
        	"\r\n  --->       Threads: " + Thread.activeCount(),
        	"\r\n  RAM Used: "+((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1048576), // 1024 * 1024 = 1048576
        	"\r\n");
        
        return sb;
    }
    
    private long[] findDeadlockedThreads(ThreadMXBean mbean)
    {
        // JDK 1.5 only supports the findMonitorDeadlockedThreads()
        // method, so you need to comment out the following three lines
        if (mbean.isSynchronizerUsageSupported())
        {
            return mbean.findDeadlockedThreads();
        }
		return mbean.findMonitorDeadlockedThreads();
    }
    
    private Thread findMatchingThread(ThreadInfo inf)
    {
        for (Thread thread : Thread.getAllStackTraces().keySet())
        {
            if (thread.getId() == inf.getThreadId())
            {
                return thread;
            }
        }
        throw new IllegalStateException("Deadlocked Thread not found");
    }
}