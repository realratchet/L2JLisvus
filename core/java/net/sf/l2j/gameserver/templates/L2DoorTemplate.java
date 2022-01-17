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

public class L2DoorTemplate extends L2CharTemplate
{
	public final int doorId;
	public final String name;
	public final boolean isUnlockable;
	public final boolean isOpenByDefault;

    public L2DoorTemplate(StatsSet set)
    {
    	super(set);
    	
    	doorId = set.getInteger("id");
    	name = set.getString("name");
    	isUnlockable = set.getBool("isUnlockable", false);
    	isOpenByDefault = set.getBool("isOpenByDefault", false);
    }
}