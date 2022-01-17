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
package net.sf.l2j.gameserver.geoengine.geoeditorcon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dezmond
 */
public class GeoEditorListener extends Thread
{
    private static Logger _log = Logger.getLogger(GeoEditorListener.class.getName());
    
    private static final int PORT = 9011;
    
    public static GeoEditorListener getInstance()
    {
        return SingletonHolder._instance;
    }
    
    private ServerSocket _serverSocket;
    private GeoEditorThread _geoEditor;
    private boolean _isRunning = false;

    private GeoEditorListener()
    {
    	try
        {
    		_serverSocket = new ServerSocket(PORT);
            start();
            _log.info("GeoEditorListener Initialized.");
        }
        catch (IOException e)
        {
            _log.severe("Error creating geoeditor listener! " + e.getMessage());
            System.exit(1);
        }
    }

    public GeoEditorThread getThread()
    {
        return _geoEditor;
    }

    public String getStatus()
    {
        if (_geoEditor != null && _geoEditor.isWorking())
            return "Geoeditor connected.";

        return "Geoeditor not connected.";
    }

    @Override
	public void run()
    {
        try (Socket connection = _serverSocket.accept())
        {
        	_isRunning = true;
            while (_isRunning)
            {
                if (_geoEditor != null && _geoEditor.isWorking())
                {
                    _log.warning("Geoeditor already connected!");
                    connection.close();
                    continue;
                }

                _log.info("Received geoeditor connection from: " + connection.getInetAddress().getHostAddress());
                _geoEditor = new GeoEditorThread(connection);
                _geoEditor.start();
            }
        }
        catch (Exception e)
        {
            _log.info("GeoEditorListener: " + e.getMessage());
        }
        finally
        {
            quit();
        }
    }
    
    public void quit()
    {
    	_isRunning = false;
    	
    	if (_serverSocket != null)
    	{
	    	try
	        {
	            _serverSocket.close();
	            _serverSocket = null;
	            _log.warning("GeoEditorListener Closed!");
	        }
	        catch (IOException io)
	        {
	            _log.log(Level.INFO, "", io);
	        }
    	}
    }
    
    private static class SingletonHolder
	{
		protected static final GeoEditorListener _instance = new GeoEditorListener();
	}
}