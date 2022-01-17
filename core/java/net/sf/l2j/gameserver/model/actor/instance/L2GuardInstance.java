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
package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.L2WorldRegion;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.knownlist.GuardKnownList;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.util.Rnd;

/**
 * This class manages all Guards in the world.
 * It inherits all methods from L2Attackable and adds some more such as tracking PK and aggressive L2MonsterInstance.<BR><BR>
 * 
 * @version $Revision: 1.11.2.1.2.7 $ $Date: 2005/04/06 16:13:40 $
 */
public final class L2GuardInstance extends L2Attackable
{
    private static final int RETURN_INTERVAL = 60000;

    public class ReturnTask implements Runnable
    {
        @Override
		public void run()
        {
            if (getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE)
                returnHome();
        }
    }
    
	/**
	 * Constructor of L2GuardInstance (use L2Character and L2NpcInstance constructor).<BR><BR>
	 *  
	 * <B><U> Actions</U> :</B><BR><BR>
	 * <li>Call the L2Character constructor to set the _template of the L2GuardInstance (copy skills from template to object and link _calculators to NPC_STD_CALCULATOR) </li>
	 * <li>Set the name of the L2GuardInstance</li>
	 * <li>Create a RandomAnimation Task that will be launched after the calculated delay if the server allow it </li><BR><BR>
	 * 
	 * @param objectId Identifier of the object to initialized
	 * @param template The template to apply to the NPC
	 */
	public L2GuardInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
        getKnownList();
        ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new ReturnTask(),RETURN_INTERVAL,RETURN_INTERVAL+Rnd.nextInt(60000));
	}

    @Override
	public final GuardKnownList getKnownList()
    {
        if (super.getKnownList() == null || !(super.getKnownList() instanceof GuardKnownList))
            setKnownList(new GuardKnownList(this));
        return (GuardKnownList)super.getKnownList();
    }

	/**
	 * Return True if the attacker is a L2MonsterInstance.<BR><BR>
	 */
	@Override
	public boolean isAutoAttackable(L2Character attacker)
	{
		return attacker instanceof L2MonsterInstance;
	}

	/**
	 * Notify the L2GuardInstance to return to its home location (AI_INTENTION_MOVE_TO) and clear its _aggroList.<BR><BR>
	 */
	public void returnHome()
	{
        if (!isInsideRadius(getSpawn().getLocX(), getSpawn().getLocY(), 150, false))
		{
			clearAggroList();
			getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(getSpawn().getLocX(), getSpawn().getLocY(), getSpawn().getLocZ()));
		}
	}
	
	/**
	 * Set the home location of its L2GuardInstance.<BR><BR>
	 */
	@Override
	public void onSpawn()
	{
        super.onSpawn();

        // check the region where this mob is, do not activate the AI if region is inactive.
        L2WorldRegion region = L2World.getInstance().getRegion(getX(),getY());
        if (region != null && !region.isActive())
        {
            getAI().stopAITask();
        }
	}

	/**
	 * Return the path file of the selected HTML file in function of the L2GuardInstance Identifier and of the page number.<BR><BR>
	 *   
	 * <B><U> Format of the path file </U> :</B><BR><BR>
	 * <li> if page number = 0 : <B>data/html/guard/12006.htm</B> (npcId-page number)</li>
	 * <li> if page number > 0 : <B>data/html/guard/12006-1.htm</B> (npcId-page number)</li><BR><BR>
	 * 
	 * @param npcId The Identifier of the L2NpcInstance whose text must be display
	 * @param val The number of the page to display
	 * 
	 */
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		if (val == 0)
		{
			pom = "" + npcId;
		} 
		else 
		{
			pom = npcId + "-" + val;
		}
		return "data/html/guard/" + pom + ".htm";
	}
}