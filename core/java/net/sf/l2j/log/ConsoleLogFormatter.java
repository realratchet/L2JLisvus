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
package net.sf.l2j.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import net.sf.l2j.util.StringUtil;

/**
 * This class ...
 * 
 * @version $Revision: 1.1.4.2 $ $Date: 2005/03/27 15:30:08 $
 */
public class ConsoleLogFormatter extends Formatter
{
    /* (non-Javadoc)
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    private static final String CRLF = "\r\n";

    @Override
	public String format(LogRecord record)
    {
        StringBuilder output = new StringBuilder(400);
        StringUtil.append(output, record.getMessage(), CRLF);
        if (record.getThrown() != null)
        {
            try (StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw))
            {
                record.getThrown().printStackTrace(pw);
                StringUtil.append(output, sw.toString(), CRLF);
            }
            catch (Exception ex)
            {
            }
        }
        return output.toString();
    }
}