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

package net.sf.l2j.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author Balancer
 * balancer@balancer.ru
 * http://balancer.ru
 */
public class Log
{
    private static final Logger _log = Logger.getLogger(Log.class.getName());

    public static final void addGame(String text, String cat)
    {
    	add(text, cat, "game");
    }
    
    public static final void addLogin(String text, String cat)
    {
    	add(text, cat, "login");
    }
    
	private static final void add(String text, String cat, String dir)
	{
		String date = (new SimpleDateFormat("yy.MM.dd H:mm:ss")).format(new Date());
		
		File directory = new File("log/" + dir);
		if (!directory.exists())
		{
			directory.mkdirs();
		}
		directory = null;
		
		File file = new File("log/" + dir + "/" + (cat != null ? cat : "_all") + ".txt");
		try (FileWriter save = new FileWriter(file, true))
		{
			String out = "[" + date + "] '---': " + text + "\n";
			save.write(out);
			save.flush();
			file = null;
		}
		catch (IOException e)
		{
			_log.warning("saving chat log failed: " + e);
			e.printStackTrace();
		}
		
		if (cat != null)
		{
			add(text, null, dir);
		}
	}
}