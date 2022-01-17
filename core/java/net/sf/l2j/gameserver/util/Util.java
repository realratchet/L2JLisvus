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
package net.sf.l2j.gameserver.util;

import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * General Utility functions related to GameServer.
 */
public final class Util
{
	private static Logger _log = Logger.getLogger(Util.class.getName());
	
    public static void handleIllegalPlayerAction(L2PcInstance actor, String message, int punishment)
    {
    	ThreadPoolManager.getInstance().scheduleGeneral(new IllegalPlayerAction(actor,message, punishment), 5000);
    }
    
    public static String getRelativePath(File base,File file)
    {
        return file.toURI().getPath().substring(base.toURI().getPath().length());
    }

    /** 
     * Return degree value of object 2 to the horizontal line with object 1 being the origin.
     * 
     * @param obj1 
     * @param obj2 
     * @return 
     */
    public static double calculateAngleFrom(L2Object obj1, L2Object obj2)
    {
    	return calculateAngleFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
    }

    /** 
     * Return degree value of object 2 to the horizontal line with object 1 being the origin.
     * 
     * @param obj1X 
     * @param obj1Y 
     * @param obj2X 
     * @param obj2Y 
     * @return 
     */
    public final static double calculateAngleFrom(int obj1X, int obj1Y, int obj2X, int obj2Y)
    {
        double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
        if (angleTarget < 0) angleTarget = 360 + angleTarget;
        return angleTarget;
    }

    public final static double convertHeadingToDegree(int clientHeading)
    {
        double degree = clientHeading / 182.044444444;
        return degree;
    }

    public final static int convertDegreeToClientHeading(double degree)
    {
        if (degree < 0) degree = 360 + degree;
        return (int)(degree*182.044444444);
    }

    public static int calculateHeadingFrom(L2Object obj1, L2Object obj2)
    {
        return calculateHeadingFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
    }

    public static int calculateHeadingFrom(int obj1X, int obj1Y, int obj2X, int obj2Y)
    {
        double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
        if (angleTarget < 0) angleTarget = 360 + angleTarget;
        return (int)(angleTarget*182.044444444);
    }

    public final static int calculateHeadingFrom(double dx, double dy)
    {
        double angleTarget = Math.toDegrees(Math.atan2(dy, dx));
        if (angleTarget < 0) angleTarget = 360 + angleTarget;
        return (int)(angleTarget*182.044444444);
    }

    public static double calculateDistance(int x1, int y1, int x2, int y2)
    {
        return calculateDistance(x1, y1, 0, x2, y2, 0, false);
    }

    public static double calculateDistance(int x1, int y1, int z1, int x2, int y2, int z2, boolean includeZAxis)
    {
        double dx = (double)x1 - x2;
        double dy = (double)y1 - y2;

        if (includeZAxis)
        {
            double dz = z1 - z2;
            return Math.sqrt((dx*dx) + (dy*dy) + (dz*dz));
        }
		return Math.sqrt((dx*dx) + (dy*dy));
    }

    public static double calculateDistance(L2Object obj1, L2Object obj2, boolean includeZAxis)
    {
        if (obj1 == null || obj2 == null) return 1000000;
        return calculateDistance(obj1.getPosition().getX(), obj1.getPosition().getY(), obj1.getPosition().getZ(), obj2.getPosition().getX(), obj2.getPosition().getY(), obj2.getPosition().getZ(), includeZAxis);
    }
    
    /**
     * Capitalizes the first letter of a string, and returns the result.<BR>
     * (Based on ucfirst() function of PHP)
     * 
     * @param str
     * @return String containing the modified string.
     */
    public static String capitalizeFirst(String str)
    {
        str = str.trim();

        if (str.length() > 0 && Character.isLetter(str.charAt(0)))
            return str.substring(0, 1).toUpperCase() + str.substring(1);

        return str;
    }
    
    /**
     * Capitalizes the first letter of every "word" in a string.<BR>
     * (Based on ucwords() function of PHP)
     * 
     * @param str
     * @return String containing the modified string.
     */
    public static String capitalizeWords(String str) 
    {
        char[] charArray = str.toCharArray();
        String result = "";
       
        // Capitalize the first letter in the given string!
        charArray[0] = Character.toUpperCase(charArray[0]);
       
        for (int i = 0; i < charArray.length; i++) 
        {
            if (Character.isWhitespace(charArray[i])) 
                charArray[i + 1] = Character.toUpperCase(charArray[i + 1]);

            result += Character.toString(charArray[i]);
        }
       
        return result;
    }

    /**
     * Checks if object is within range, adding collisionRadius.
     * 
     * @param range 
     * @param obj1 
     * @param obj2 
     * @param includeZAxis 
     * @return 
     */
    public static boolean checkIfInRange(int range, L2Object obj1, L2Object obj2, boolean includeZAxis)
    {
        if (obj1 == null || obj2 == null) return false;

        if (range == -1) return true; // not limited

        int rad = 0;
        if (obj1 instanceof L2Character)
            rad += ((L2Character)obj1).getTemplate().collisionRadius;
        if (obj2 instanceof L2Character)
            rad += ((L2Character)obj2).getTemplate().collisionRadius;

        double dx = obj1.getX() - obj2.getX();
        double dy = obj1.getY() - obj2.getY();

        if (includeZAxis)
        {
            double dz = obj1.getZ() - obj2.getZ();
            double d = dx*dx + dy*dy +dz*dz;

            return d <= range*range + 2*range*rad + rad*rad;
        }
		double d = dx*dx + dy*dy;

		return d <= range*range + 2*range*rad + rad*rad;
    }

    /**
     * Checks if object is within short (sqrt(int.max_value)) radius, 
     * not using collisionRadius. Faster calculation than checkIfInRange
     * if distance is short and collisionRadius isn't needed.
     * Not for long distance checks (potential teleports, far away castles etc).
     *  
     * @param radius 
     * @param obj1 
     * @param obj2 
     * @param includeZAxis 
     * @return 
     */
    public static boolean checkIfInShortRadius(int radius, L2Object obj1, L2Object obj2, boolean includeZAxis)
    {
        if (obj1 == null || obj2 == null) return false;
        if (radius == -1) return true; // not limited

        int dx = obj1.getX() - obj2.getX();
        int dy = obj1.getY() - obj2.getY();

        if (includeZAxis)
        {
        	int dz = obj1.getZ() - obj2.getZ();
            return dx*dx + dy*dy + dz*dz <= radius*radius;
        }
		return dx*dx + dy*dy <= radius*radius;
    }

    /**
     * Returns the number of "words" in a given string.
     * 
     * @param str
     * @return int numWords
     */
    public static int countWords(String str)
    {
        return str.trim().split(" ").length;
    }
    
    /**
     * Returns a delimited string for an given array of string elements.<BR>
     * (Based on implode() in PHP)
     * 
     * @param strArray
     * @param strDelim
     * @return String implodedString
     */
    public static String implodeString(String[] strArray, String strDelim)
    {
        String result = "";
        
        for (String strValue : strArray)
            result += strValue + strDelim;
            
        return result;
    }
    
    /**
     * Returns a delimited string for an given collection of string elements.<BR>
     * (Based on implode() in PHP)
     * 
     * @param strCollection
     * @param strDelim
     * @return String implodedString
     */
    public static String implodeString(Collection<String> strCollection, String strDelim)
    {
        return implodeString(strCollection.toArray(new String[strCollection.size()]), strDelim);
    }
    
    /**
     * Returns the rounded value of val to specified number of digits 
     * after the decimal point.<BR>
     * (Based on round() in PHP) 
     * 
     * @param val
     * @param numPlaces
     * @return float roundedVal
     */
    public static float roundTo(float val, int numPlaces)
    {
        if (numPlaces <= 1)
            return Math.round(val);
        float exponent = (float) Math.pow(10, numPlaces);
        return (Math.round(val * exponent) / exponent);
    }

    /**
	 * @param text - the text to check
	 * @return {@code true} if {@code text} contains only numbers, {@code false} otherwise
	 */
	public static boolean isDigit(String text)
	{
		if (text == null || text.isEmpty())
		{
			return false;
		}
		
		for (char c : text.toCharArray())
		{
			if (!Character.isDigit(c))
			{
				return false;
			}
		}
		return true;
	}
    
	/**
	 * @param text - the text to check
	 * @return {@code true} if {@code text} contains only alphanumeric characters, {@code false} otherwise
	 */
    public static boolean isAlphaNumeric(String text)
    {
        if (text == null || text.isEmpty())
        {
            return false;
        }

        for (char c : text.toCharArray())
        {
            if (!Character.isLetterOrDigit(c))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if given name matches with given expression pattern.
     * 
     * @param name
     * @param expression
     * @return
     */
    public static boolean isValidName(String name, String expression)
	{
		Pattern pattern;
		try
		{
			pattern = Pattern.compile(expression);
		}
		catch (PatternSyntaxException e) // Case of illegal pattern
		{
			_log.warning("Name pattern is wrong! Setting name pattern to .*");
			pattern = Pattern.compile(".*");
		}
		return pattern.matcher(name).matches();
	}
    
    /**
     * Checks if given array of objects contains given object.
     * 
	 * @param <T> 
     * @param array - the array to look into
	 * @param object - The object to search for.
	 * @return
	 */
	public static <T> boolean contains(T[] array, T object)
	{
		for (T element : array)
		{
			if (element == object)
			{
				return true;
			}
		}
		return false;
	}
    
    /**
     * Checks if given array of integers contains given integer.
     * 
     * @param array
     * @param object - The object to search for.
     * @return
     */
    public static boolean contains(int[] array, int object)
	{
		for (int i : array)
		{
			if (i == object)
			{
				return true;
			}
		}
		return false;
	}
    
    /**
     * Checks if given array of objects contains given object and returns its index.
     * 
	 * @param <T> 
     * @param array - the array to look into
	 * @param object - The object to search for.
	 * @return
	 */
	public static <T> int indexOf(T[] array, T object)
	{
		for (int i = 0; i < array.length; i++)
		{
			T element = array[i];
			if (element == object)
			{
				return i;
			}
		}
		return -1;
	}
    
    /**
     * Checks if given array of integers contains given integer and returns its index.
     * 
     * @param array
     * @param object - The object to search for.
     * @return
     */
    public static int indexOf(int[] array, int object)
	{
    	for (int i = 0; i < array.length; i++)
		{
			int element = array[i];
			if (element == object)
			{
				return i;
			}
		}
		return -1;
	}
}