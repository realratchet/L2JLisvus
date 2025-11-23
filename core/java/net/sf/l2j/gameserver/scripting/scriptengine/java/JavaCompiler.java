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

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

/**
 * Simple interface to Java compiler using JSR 199 Compiler API.
 * @author A. Sundararajan
 */
public class JavaCompiler
{
	public static final String JAVA_LANGUAGE_VERSION = "17";
	
	private final javax.tools.JavaCompiler _tool;
	
	public JavaCompiler()
	{
		_tool = new EclipseCompiler();
	}
	
	public Map<String, byte[]> compile(String source, String fileName)
	{
		Map<String, byte[]> result;
		
		try (PrintWriter err = new PrintWriter(System.err))
		{
			result = compile(source, fileName, err, null, null);
		}
		catch (Exception e)
		{
			result = null;
		}

		return result;
	}
	
	public Map<String, byte[]> compile(String fileName, String source, Writer err)
	{
		return compile(fileName, source, err, null, null);
	}
	
	public Map<String, byte[]> compile(String fileName, String source, Writer err, String sourcePath)
	{
		return compile(fileName, source, err, sourcePath, null);
	}
	
	/**
	 * Compile given String source and return bytecodes as a Map.
	 * @param filePath source filePath to be used for error messages etc.
	 * @param source Java source as String
	 * @param err error writer where diagnostic messages are written
	 * @param sourcePath location of additional .java source files
	 * @param classPath location of additional .class files
	 * @return
	 */
	public Map<String, byte[]> compile(String filePath, String source, Writer err, String sourcePath, String classPath)
	{
		Map<String, byte[]> result;
		
		// This instance is used by compiler to collect errors, warnings etc.
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		
		// Prepare the compilation unit
		List<JavaFileObject> compUnits = new ArrayList<>(1);
		compUnits.add(MemoryJavaFileManager.makeStringSource(filePath, source));
		
		// javac options
		List<String> options = new ArrayList<>();
		options.add("-warn:-enumSwitch");
		options.add("-g");
		options.add("-deprecation");
		options.add("-" + JAVA_LANGUAGE_VERSION);
		if (sourcePath != null)
		{
			options.add("-sourcepath");
			options.add(sourcePath);
		}
		if (classPath != null)
		{
			options.add("-classpath");
			options.add(classPath);
		}

		// Create a new memory JavaFileManager
		try (MemoryJavaFileManager manager = new MemoryJavaFileManager())
		{
			// Create a compilation task
			CompilationTask task = _tool.getTask(err, manager, diagnostics, options, null, compUnits);
			
			if (task.call())
			{
				result = manager.getClassBytes();
			}
			else
			{
				try (PrintWriter pw = new PrintWriter(err))
				{
					for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics())
					{
						pw.println(diagnostic.getMessage(Locale.ROOT));
					}

					pw.flush();
				}
				
				result = null;
			}
		}
		catch (Exception e)
		{
			result = null;
		}

		return result;
	}
}
