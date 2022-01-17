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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

import org.eclipse.jdt.internal.compiler.tool.EclipseFileManager;

/**
 * JavaFileManager that keeps compiled .class bytes in memory.
 * @author A. Sundararajan
 */
public final class MemoryJavaFileManager extends EclipseFileManager
{
	private static final String EXT = ".java";
	protected Map<String, byte[]> _classBytes;
	
	public MemoryJavaFileManager()
	{
		super(null, null);
		_classBytes = new HashMap<>();
	}
	
	public Map<String, byte[]> getClassBytes()
	{
		return _classBytes;
	}
	
	@Override
	public void close()
	{
		_classBytes = new HashMap<>();
	}
	
	@Override
	public void flush()
	{
	}
	
	/**
	 * A file object used to represent Java source coming from a string.
	 */
	private static class StringInputBuffer extends SimpleJavaFileObject
	{
		final String code;
		
		StringInputBuffer(String name, String code)
		{
			super(toURI(name), Kind.SOURCE);
			this.code = code;
		}
		
		@Override
		public CharBuffer getCharContent(boolean ignoreEncodingErrors)
		{
			return CharBuffer.wrap(code);
		}
	}
	
	/**
	 * A file object that stores Java bytecode into the classBytes map.
	 */
	private class ClassOutputBuffer extends SimpleJavaFileObject
	{
		protected final String name;
		
		ClassOutputBuffer(String name)
		{
			super(toURI(name), Kind.CLASS);
			this.name = name;
		}
		
		@Override
		public OutputStream openOutputStream()
		{
			return new FilterOutputStream(new ByteArrayOutputStream())
			{
				@Override
				public void close() throws IOException
				{
					out.close();
					ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
					_classBytes.put(name, bos.toByteArray());
				}
			};
		}
	}
	
	@Override
	public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, Kind kind, FileObject sibling) throws IOException
	{
		if (kind == Kind.CLASS)
		{
			return new ClassOutputBuffer(className.replace('/', '.'));
		}
		return super.getJavaFileForOutput(location, className, kind, sibling);
	}
	
	static JavaFileObject makeStringSource(String name, String code)
	{
		return new StringInputBuffer(name, code);
	}
	
	static URI toURI(String name)
	{
		File file = new File(name);
		if (file.exists())
		{
			return file.toURI();
		}
		
		try
		{
			final StringBuilder newUri = new StringBuilder();
			newUri.append("file:///");
			newUri.append(name.replace('.', '/'));
			if (name.endsWith(EXT))
			{
				newUri.replace(newUri.length() - EXT.length(), newUri.length(), EXT);
			}
			return URI.create(newUri.toString());
		}
		catch (Exception exp)
		{
			return URI.create("file:///com/sun/script/java/java_source");
		}
	}
}
