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
package net.sf.l2j.loginserver.serverpackets;

public final class AccountKicked extends L2LoginServerPacket
{
	public static enum AccountKickedReason
	{
		REASON_ILLEGAL_USE(0x01),
		REASON_GENERAL_VIOLATION(0x08);
		
		private final int _code;
		
		AccountKickedReason(int code)
		{
			_code = code;
		}
		
		public final int getCode()
		{
			return _code;
		}
	}
	
	private final AccountKickedReason _reason;
	
	public AccountKicked(AccountKickedReason reason)
	{
		_reason = reason;
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.mmocore.SendablePacket#write()
	 */
	@Override
	protected void write()
	{
		writeC(0x02);
		writeD(_reason.getCode());
	}
}