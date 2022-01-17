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
package net.sf.l2j.mmocore;

import java.nio.ByteBuffer;

/**
 * @author KenM
 * @param <T>
 */
public abstract class MMOClient<T extends MMOConnection<?>>
{
	private final T _con;
	
	public MMOClient(final T con)
	{
		_con = con;
	}
	
	public T getConnection()
	{
		return _con;
	}
	
	public abstract boolean decrypt(final ByteBuffer buf, final int size);
	
	public abstract boolean encrypt(final ByteBuffer buf, final int size);
	
	protected abstract void onDisconnection();
	
	protected abstract void onForcedDisconnection();
}