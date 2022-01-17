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
package net.sf.l2j.loginserver.crypt;

import java.io.IOException;

/**
 *
 * @author  KenM
 */
public class LoginCrypt
{
	private NewCrypt _crypt;
	private boolean _initialized = false;

	public void setKey(byte[] key)
	{
		_crypt = new NewCrypt(key);
	}
	
	public boolean decrypt(byte[] raw, final int offset, final int size) throws IOException
	{
   		_crypt.decrypt(raw, offset, size);
   		return NewCrypt.verifyChecksum(raw, offset, size);
	}

	public int encrypt(byte[] raw, final int offset, int size) throws IOException
	{
		// Reserve checksum
		size += 4;
		
		if (_initialized)
		{
			// Padding
			size += 8 - size % 8;
	       	
			NewCrypt.appendChecksum(raw, offset, size);
	   		_crypt.crypt(raw, offset, size);
		}
		else
		{
	   		_initialized = true;
		}
   		
	   	return size;
	}
}