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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * This is script engine for Java programming language.
 * @author A. Sundararajan
 */
public class JavaScriptEngine extends AbstractScriptEngine implements Compilable
{
	// for certain variables, we look for System properties. This is
	// the prefix used for such System properties
	private static final String SYSPROP_PREFIX = "com.sun.script.java.";
	
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final String ARGUMENTS = "arguments";
	
	private static final String MAINCLASS = "mainClass";
	
	private static final String FILEPATH = "filepath";
	private static final String SOURCEPATH = "sourcepath";
	private static final String CLASSPATH = "classpath";
	private static final String PARENTLOADER = "parentLoader";
	
	// Java compiler
	private final JavaCompiler _compiler;
	
	public JavaScriptEngine()
	{
		_compiler = new JavaCompiler();
	}
	
	// Factory, may be null
	private ScriptEngineFactory _factory;
	
	// Implementation for CompiledScript
	private static class JavaCompiledScript extends CompiledScript implements Serializable
	{
		private static final long serialVersionUID = 1L;
		private final transient JavaScriptEngine _engine;
		private transient Class<?> _class;
		private final Map<String, byte[]> _classBytes;
		private final String _classPath;
		
		JavaCompiledScript(JavaScriptEngine engine, Map<String, byte[]> classBytes, String classPath)
		{
			_engine = engine;
			_classBytes = classBytes;
			_classPath = classPath;
		}
		
		@Override
		public ScriptEngine getEngine()
		{
			return _engine;
		}
		
		@Override
		public Object eval(ScriptContext ctx) throws ScriptException
		{
			if (_class == null)
			{
				Map<String, byte[]> classBytesCopy = new HashMap<>();
				classBytesCopy.putAll(_classBytes);
				MemoryClassLoader loader = new MemoryClassLoader(classBytesCopy, _classPath, JavaScriptEngine.getParentLoader(ctx));
				_class = JavaScriptEngine.parseMain(loader, ctx);
			}
			return JavaScriptEngine.evalClass(_class, ctx);
		}
	}
	
	@Override
	public CompiledScript compile(String script) throws ScriptException
	{
		return compile(script, context);
	}
	
	@Override
	public CompiledScript compile(Reader reader) throws ScriptException
	{
		return compile(readFully(reader));
	}
	
	@Override
	public Object eval(String str, ScriptContext ctx) throws ScriptException
	{
		Class<?> clazz = parse(str, ctx);
		return evalClass(clazz, ctx);
	}
	
	@Override
	public Object eval(Reader reader, ScriptContext ctx) throws ScriptException
	{
		return eval(readFully(reader), ctx);
	}
	
	@Override
	public ScriptEngineFactory getFactory()
	{
		return _factory;
	}
	
	@Override
	public Bindings createBindings()
	{
		return new SimpleBindings();
	}
	
	void setFactory(ScriptEngineFactory factory)
	{
		_factory = factory;
	}
	
	// Internals only below this point
	
	private Class<?> parse(String str, ScriptContext ctx) throws ScriptException
	{
		String filePath = getFilePath(ctx);
		String sourcePath = getSourcePath(ctx);
		String classPath = getClassPath(ctx);
		
		Writer err = ctx.getErrorWriter();
		if (err == null)
		{
			err = new StringWriter();
		}
		
		Map<String, byte[]> classBytes = _compiler.compile(filePath, str, err, sourcePath, classPath);
		
		if (classBytes == null)
		{
			if (err instanceof StringWriter)
			{
				throw new ScriptException(((StringWriter) err).toString());
			}
			throw new ScriptException("compilation failed");
		}
		
		// Create a ClassLoader to load classes from MemoryJavaFileManager
		MemoryClassLoader loader = new MemoryClassLoader(classBytes, classPath, getParentLoader(ctx));
		return parseMain(loader, ctx);
	}
	
	protected static Class<?> parseMain(MemoryClassLoader loader, ScriptContext ctx) throws ScriptException
	{
		String mainClassName = getMainClassName(ctx);
		if (mainClassName != null)
		{
			try
			{
				Class<?> clazz = loader.load(mainClassName);
				Method mainMethod = findMainMethod(clazz);
				if (mainMethod == null)
				{
					throw new ScriptException("No main method in " + mainClassName);
				}
				return clazz;
			}
			catch (ClassNotFoundException cnfe)
			{
				cnfe.printStackTrace();
				throw new ScriptException(cnfe);
			}
		}
		
		// No main class configured - load all compiled classes
		Iterable<Class<?>> classes;
		try
		{
			classes = loader.loadAll();
		}
		catch (ClassNotFoundException exp)
		{
			throw new ScriptException(exp);
		}
		
		// Search for class with main method
		Class<?> c = findMainClass(classes);
		if (c != null)
		{
			return c;
		}
		
		/**
		 * If class has "main" method, then
		 * return first class.
		 */
		Iterator<Class<?>> itr = classes.iterator();
		if (itr.hasNext())
		{
			return itr.next();
		}
		return null;
	}
	
	private JavaCompiledScript compile(String str, ScriptContext ctx) throws ScriptException
	{
		String filePath = getFilePath(ctx);
		String sourcePath = getSourcePath(ctx);
		String classPath = getClassPath(ctx);
		
		Writer err = ctx.getErrorWriter();
		if (err == null)
		{
			err = new StringWriter();
		}
		
		Map<String, byte[]> classBytes = _compiler.compile(filePath, str, err, sourcePath, classPath);
		if (classBytes == null)
		{
			if (err instanceof StringWriter)
			{
				throw new ScriptException(((StringWriter) err).toString());
			}
			throw new ScriptException("compilation failed");
		}
		
		return new JavaCompiledScript(this, classBytes, classPath);
	}
	
	private static Class<?> findMainClass(Iterable<Class<?>> classes)
	{
		// Find a public class with public static main method
		for (Class<?> clazz : classes)
		{
			int modifiers = clazz.getModifiers();
			if (Modifier.isPublic(modifiers))
			{
				Method mainMethod = findMainMethod(clazz);
				if (mainMethod != null)
				{
					return clazz;
				}
			}
		}
		
		/**
		 * Okay, try to find package private class that
		 * has public static main method.
		 */
		for (Class<?> clazz : classes)
		{
			Method mainMethod = findMainMethod(clazz);
			if (mainMethod != null)
			{
				return clazz;
			}
		}
		
		// No main class found!
		return null;
	}
	
	/**
	 * Finds public static void main(String[]) method, if any.
	 * 
	 * @param clazz
	 * @return
	 */
	private static Method findMainMethod(Class<?> clazz)
	{
		try
		{
			Method mainMethod = clazz.getMethod("main", new Class[]
			{
				String[].class
			});
			int modifiers = mainMethod.getModifiers();
			if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers))
			{
				return mainMethod;
			}
		}
		catch (NoSuchMethodException nsme)
		{
		}
		return null;
	}
	
	/**
	 * Finds public static void setScriptContext(ScriptContext) method, if any.
	 * 
	 * @param clazz
	 * @return
	 */
	private static Method findSetScriptContextMethod(Class<?> clazz)
	{
		try
		{
			Method setCtxMethod = clazz.getMethod("setScriptContext", new Class[]
			{
				ScriptContext.class
			});
			int modifiers = setCtxMethod.getModifiers();
			if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers))
			{
				return setCtxMethod;
			}
		}
		catch (NoSuchMethodException nsme)
		{
		}
		return null;
	}
	
	private static String[] getArguments(ScriptContext ctx)
	{
		int scope = ctx.getAttributesScope(ARGUMENTS);
		if (scope != -1)
		{
			Object obj = ctx.getAttribute(ARGUMENTS, scope);
			if (obj instanceof String[])
			{
				return (String[]) obj;
			}
		}
		// return zero length array
		return EMPTY_STRING_ARRAY;
	}
	
	@SuppressWarnings("unused")
	private static String getFileName(ScriptContext ctx)
	{
		int scope = ctx.getAttributesScope("javax.script.filename");
		if (scope != -1)
		{
			return ctx.getAttribute("javax.script.filename", scope).toString();
		}
		return "$unnamed.java";
	}
	
	private static String getFilePath(ScriptContext ctx)
	{
		int scope = ctx.getAttributesScope(FILEPATH);
		if (scope != -1)
		{
			return ctx.getAttribute(FILEPATH).toString();
		}
		
		// look for "com.sun.script.java.sourcepath"
		return System.getProperty(SYSPROP_PREFIX + FILEPATH);
	}
	
	private static String getSourcePath(ScriptContext ctx)
	{
		int scope = ctx.getAttributesScope(SOURCEPATH);
		if (scope != -1)
		{
			return ctx.getAttribute(SOURCEPATH).toString();
		}
		
		// look for "com.sun.script.java.sourcepath"
		return System.getProperty(SYSPROP_PREFIX + SOURCEPATH);
	}
	
	private static String getClassPath(ScriptContext ctx)
	{
		int scope = ctx.getAttributesScope(CLASSPATH);
		if (scope != -1)
		{
			return ctx.getAttribute(CLASSPATH).toString();
		}
		
		// look for "com.sun.script.java.classpath"
		String res = System.getProperty(SYSPROP_PREFIX + CLASSPATH);
		if (res == null)
		{
			res = System.getProperty("java.class.path");
		}
		return res;
	}
	
	private static String getMainClassName(ScriptContext ctx)
	{
		int scope = ctx.getAttributesScope(MAINCLASS);
		if (scope != -1)
		{
			return ctx.getAttribute(MAINCLASS).toString();
		}
		
		// look for "com.sun.script.java.mainClass"
		return System.getProperty("com.sun.script.java.mainClass");
	}
	
	protected static ClassLoader getParentLoader(ScriptContext ctx)
	{
		int scope = ctx.getAttributesScope(PARENTLOADER);
		if (scope != -1)
		{
			Object loader = ctx.getAttribute(PARENTLOADER);
			if (loader instanceof ClassLoader)
			{
				return (ClassLoader) loader;
			}
		}
		return ClassLoader.getSystemClassLoader();
	}
	
	protected static Object evalClass(Class<?> clazz, ScriptContext ctx) throws ScriptException
	{
		// JSR-223 requirement
		ctx.setAttribute("context", ctx, 100);
		if (clazz == null)
		{
			return null;
		}
		try
		{
			boolean isPublicClazz = Modifier.isPublic(clazz.getModifiers());
			
			// find the setScriptContext method
			Method setCtxMethod = findSetScriptContextMethod(clazz);
			// call setScriptContext and pass current ctx variable
			if (setCtxMethod != null)
			{
				if (!isPublicClazz)
				{
					// try to relax access
					setCtxMethod.setAccessible(true);
				}
				setCtxMethod.invoke(null, new Object[]
				{
					ctx
				});
			}
			
			// find the main method
			Method mainMethod = findMainMethod(clazz);
			if (mainMethod != null)
			{
				if (!isPublicClazz)
				{
					// try to relax access
					mainMethod.setAccessible(true);
				}
				
				// get "command line" args for the main method
				String args[] = getArguments(ctx);
				
				// call main method
				mainMethod.invoke(null, new Object[]
				{
					args
				});
			}
			
			// return main class as eval's result
			return clazz;
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
			throw new ScriptException(exp);
		}
	}
	
	/**
	 * Reads a Reader fully and return the content as string.
	 * 
	 * @param reader
	 * @return
	 * @throws ScriptException
	 */
	private String readFully(Reader reader) throws ScriptException
	{
		char[] arr = new char[8 * 1024]; // 8K at a time
		StringBuilder buf = new StringBuilder();
		int numChars;
		try
		{
			while ((numChars = reader.read(arr, 0, arr.length)) > 0)
			{
				buf.append(arr, 0, numChars);
			}
		}
		catch (IOException exp)
		{
			throw new ScriptException(exp);
		}
		return buf.toString();
	}
}
