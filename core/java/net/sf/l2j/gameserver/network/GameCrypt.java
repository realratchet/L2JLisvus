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
package net.sf.l2j.gameserver.network;

/**
 * @author KenM
 */
public class GameCrypt
{
	private final byte[] _inKey = new byte[8];
	private final byte[] _outKey = new byte[8];
	private boolean _isEnabled;
	
	public void setKey(byte[] key)
	{
		System.arraycopy(key, 0, _inKey, 0, 8);
		System.arraycopy(key, 0, _outKey, 0, 8);
	}
	
	public void decrypt(byte[] raw, final int offset, final int size)
	{
		if (!_isEnabled)
		{
			return;
		}
		
		int temp = 0;
		for (int i = 0; i < size; i++)
		{
			int temp2 = raw[offset + i] & 0xFF;
			raw[offset + i] = (byte) (temp2 ^ _inKey[i & 7] ^ temp);
			temp = temp2;
		}
		
		int old = _inKey[0] & 0xff;
		old |= (_inKey[1] << 8) & 0xff00;
		old |= (_inKey[2] << 0x10) & 0xff0000;
		old |= (_inKey[3] << 0x18) & 0xff000000;
		
		old += size;
		
		_inKey[0] = (byte) (old & 0xff);
		_inKey[1] = (byte) ((old >> 0x08) & 0xff);
		_inKey[2] = (byte) ((old >> 0x10) & 0xff);
		_inKey[3] = (byte) ((old >> 0x18) & 0xff);
	}
	
	public void encrypt(byte[] raw, final int offset, final int size)
	{
		if (!_isEnabled)
		{
			_isEnabled = true;
			return;
		}
		
		int temp = 0;
		for (int i = 0; i < size; i++)
		{
			int temp2 = raw[offset + i] & 0xFF;
			temp = temp2 ^ _outKey[i & 7] ^ temp;
			raw[offset + i] = (byte) temp;
		}
		
		int old = _outKey[0] & 0xff;
		old |= (_outKey[1] << 8) & 0xff00;
		old |= (_outKey[2] << 0x10) & 0xff0000;
		old |= (_outKey[3] << 0x18) & 0xff000000;
		
		old += size;
		
		_outKey[0] = (byte) (old & 0xff);
		_outKey[1] = (byte) ((old >> 0x08) & 0xff);
		_outKey[2] = (byte) ((old >> 0x10) & 0xff);
		_outKey[3] = (byte) ((old >> 0x18) & 0xff);
	}
}