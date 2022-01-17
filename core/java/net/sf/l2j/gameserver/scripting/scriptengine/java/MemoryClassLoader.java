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
package net.sf.l2j.gameserver.scripting.scriptengine.java;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * ClassLoader that loads .class bytes from memory.
 * @author A. Sundararajan
 */
public final class MemoryClassLoader extends URLClassLoader
{
	private final Map<String, byte[]> _classBytes;
	
	public MemoryClassLoader(Map<String, byte[]> classBytes, String classPath, ClassLoader parent)
	{
		super(toURLs(classPath), parent);
		_classBytes = classBytes;
	}
	
	public MemoryClassLoader(Map<String, byte[]> classBytes, String classPath)
	{
		this(classBytes, classPath, null);
	}
	
	public Class<?> load(String className) throws ClassNotFoundException
	{
		return loadClass(className);
	}
	
	public Iterable<Class<?>> loadAll() throws ClassNotFoundException
	{
		List<Class<?>> classes = new ArrayList<>(_classBytes.size());
		for (String name : _classBytes.keySet())
		{
			classes.add(loadClass(name));
		}
		return classes;
	}
	
	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException
	{
		byte buf[] = _classBytes.get(className);
		if (buf != null)
		{
			// clear the bytes in map -- we don't need it anymore
			_classBytes.put(className, null);
			return defineClass(className, buf, 0, buf.length);
		}
		return super.findClass(className);
	}
	
	private static URL[] toURLs(String classPath)
	{
		if (classPath == null)
		{
			return new URL[0];
		}
		
		List<URL> list = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(classPath, File.pathSeparator);
		while (st.hasMoreTokens())
		{
			String token = st.nextToken();
			File file = new File(token);
			if (file.exists())
			{
				try
				{
					list.add(file.toURI().toURL());
				}
				catch (MalformedURLException mue)
				{
					//
				}
			}
			else
			{
				try
				{
					list.add(new URL(token));
				}
				catch (MalformedURLException mue)
				{
					//
				}
			}
		}
		
		URL res[] = new URL[list.size()];
		list.toArray(res);
		return res;
	}
}
