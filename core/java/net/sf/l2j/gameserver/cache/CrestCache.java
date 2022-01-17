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
package net.sf.l2j.gameserver.cache;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2Clan;

/**
 * @author Layane, reworked by Java-man
 */
public class CrestCache
{
	private static final Logger _log = Logger.getLogger(CrestCache.class.getName());
	
	private final static List<CrestData> _cache = new ArrayList<>();

	private long _bytesBuffLen;
	
	public static enum CrestType
	{
		PLEDGE("Crest_"),
		PLEDGE_LARGE("LargeCrest_"),
		PLEDGE_OLD("Pledge_"),
		ALLY("AllyCrest_");
		
		private final String _dirPrefix;
		
		CrestType(String dirPrefix)
		{
			_dirPrefix = dirPrefix;
		}
		
		public String getDirPrefix()
		{
			return _dirPrefix;
		}
	}
	
	public static CrestCache getInstance()
    {
        return SingletonHolder._instance;
    }
	
	public CrestCache()
	{
		convertOldPledgeFiles();
		load();
	}
	
	public int load()
	{
		_cache.clear();
		// Reset memory usage variable
		_bytesBuffLen = 0;
		
		final File dir = new File(Config.DATAPACK_ROOT, "data/crests/");
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		
		File[] files = dir.listFiles(new BmpFilter());
		if (files == null)
		{
			files = new File[0];
		}
		
		String fileName;
		byte[] content;
		
		CrestType crestType = null;
		int crestId = 0;
		
		for (File file : files)
		{
			fileName = file.getName();
			try (RandomAccessFile f = new RandomAccessFile(file, "r"))
			{
				content = new byte[(int) f.length()];
				f.readFully(content);
				
				for (CrestType type : CrestType.values())
				{
					if (fileName.startsWith(type.getDirPrefix()))
					{
						crestType = type;
						crestId = Integer.valueOf(fileName.substring(type.getDirPrefix().length(), fileName.length() - 4));
					}
				}
				_cache.add(new CrestData(crestType, crestId, content));
				_bytesBuffLen += content.length;
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "Problem with loading crest bmp file: " + file, e);
			}
		}
		
		int count = _cache.size();
		_log.info("Cache[Crest]: " + String.format("%.3f", getMemoryUsage()) + "MB on " + count + " files loaded.");
		
		return count;
	}
	
	public void convertOldPledgeFiles()
	{
		int clanId, newId;
		L2Clan clan;
		
		final File dir = new File(Config.DATAPACK_ROOT, "data/crests/");
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		
		File[] files = dir.listFiles(new OldPledgeFilter());
		if (files == null)
		{
			files = new File[0];
		}
		
		for (File file : files)
		{
			clanId = Integer.parseInt(file.getName().substring(CrestType.PLEDGE_OLD.getDirPrefix().length(), file.getName().length() - 4));
			newId = IdFactory.getInstance().getNextId();
			clan = ClanTable.getInstance().getClan(clanId);
			
			_log.info("Found old crest file '" + file.getName() + "' for clanId " + clanId);
			
			if (clan != null)
			{
				removeCrest(CrestType.PLEDGE_OLD, clan.getCrestId());
				
				file.renameTo(new File(Config.DATAPACK_ROOT, "data/crests/" + CrestType.PLEDGE.getDirPrefix() + newId + ".bmp"));
				_log.info("Renamed Clan crest to new format: " + CrestType.PLEDGE.getDirPrefix() + newId + ".bmp");
				
				try (Connection con = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET crest_id = ? WHERE clan_id = ?"))
				{
					statement.setInt(1, newId);
					statement.setInt(2, clan.getClanId());
					statement.executeUpdate();
				}
				catch (SQLException e)
				{
					_log.log(Level.WARNING, "Could not update the crest id:", e);
				}
				
				clan.setCrestId(newId);
			}
			else
			{
				_log.info("Clan Id: " + clanId + " does not exist in table.. deleting.");
				file.delete();
			}
		}
	}
	
	public byte[] getCrest(CrestType crestType, int id)
	{
		for (CrestData crest : _cache)
		{
			if (crest.getCrestType() == crestType && crest.getCrestId() == id)
			{
				return crest.getHash();
			}
		}
		
		return null;
	}
	
	public void removeCrest(CrestType crestType, int id)
	{
		String crestDirPrefix = crestType.getDirPrefix();
		
		if (crestType != CrestType.PLEDGE_OLD)
		{
			for (CrestData crestData : _cache)
			{
				if (crestData.getCrestType() == crestType && crestData.getCrestId() == id)
				{
					_cache.remove(crestData);
					break;
				}
			}
		}
		
		File crestFile = new File(Config.DATAPACK_ROOT, "data/crests/" + crestDirPrefix + id + ".bmp");
		if (!crestFile.delete())
			_log.log(Level.WARNING, "CrestCache: Failed to delete " + crestDirPrefix + id + ".bmp");
	}
	
	public boolean saveCrest(CrestType crestType, int newId, byte[] data)
	{
		File crestFile = new File(Config.DATAPACK_ROOT, "data/crests/" + crestType.getDirPrefix() + newId + ".bmp");
		try (FileOutputStream out = new FileOutputStream(crestFile))
		{
			out.write(data);
			_cache.add(new CrestData(crestType, newId, data));
			return true;
		}
		catch (IOException e)
		{
			_log.log(Level.INFO, "Error saving pledge crest " + crestFile + ":", e);
			return false;
		}
	}
	
	public float getMemoryUsage()
    {
        return ((float) _bytesBuffLen / 1048576);
    }
	
	class BmpFilter implements FileFilter
    {
        @Override
		public boolean accept(File file)
        {
            return (file.getName().endsWith(".bmp"));
        }
    }

    class OldPledgeFilter implements FileFilter
    {
        @Override
		public boolean accept(File file)
        {
            return (file.getName().startsWith("Pledge_"));
        }
    }
	
	private class CrestData
	{
		private final CrestType _crestType;
		private final int _crestId;
		private final byte[] _hash;
		
		CrestData(CrestType crestType, int crestId, byte[] hash)
		{
			_crestType = crestType;
			_crestId = crestId;
			_hash = hash;
		}
		
		public CrestType getCrestType()
		{
			return _crestType;
		}
		
		public int getCrestId()
		{
			return _crestId;
		}
		
		public byte[] getHash()
		{
			return _hash;
		}
	}
	
	private static class SingletonHolder
	{
		protected static final CrestCache _instance = new CrestCache();
	}
}