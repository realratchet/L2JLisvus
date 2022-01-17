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
package net.sf.l2j.gameserver.templates;

/**
 * @author DnR
 *
 */
public class L2SoulCrystal
{
	public static enum CrystalColor
	{
		RED(4662),
		GREEN(4663),
		BLUE(4664);
		
		private final int _brokenCrystalId;
		
		private CrystalColor(int itemId)
		{
			_brokenCrystalId = itemId;
		}
		
		public int getBrokenCrystalId()
		{
			return _brokenCrystalId;
		}
	}
	
	public final int itemId;
	public final CrystalColor color;
	public final int level;
	public final int leveledItemId;
	
	public L2SoulCrystal(StatsSet set)
	{
		itemId = set.getInteger("itemId");
		color = set.getEnum("color", CrystalColor.class);
		level = set.getInteger("level");
		leveledItemId = set.getInteger("leveledItemId");
	}
}
