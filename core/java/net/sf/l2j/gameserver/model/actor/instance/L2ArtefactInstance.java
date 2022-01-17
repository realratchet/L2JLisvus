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

import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * This class manages all Castle Siege Artefacts.<BR><BR>
 * 
 * @version $Revision: 1.11.2.1.2.7 $ $Date: 2005/04/06 16:13:40 $
 */
public final class L2ArtefactInstance extends L2NpcInstance
{
    /**
     * Constructor of L2ArtefactInstance (use L2Character and L2NpcInstance constructor).<BR><BR>
     *  
     * <B><U> Actions</U> :</B><BR><BR>
     * <li>Call the L2Character constructor to set the _template of the L2ArtefactInstance (copy skills from template to object and link _calculators to NPC_STD_CALCULATOR) </li>
     * <li>Set the name of the L2ArtefactInstance</li>
     * <li>Create a RandomAnimation Task that will be launched after the calculated delay if the server allow it </li><BR><BR>
     * 
     * @param objectId Identifier of the object to initialized
     * @param template 
     */
    public L2ArtefactInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
    }
    
    @Override
	public void onSpawn()
	{
		super.onSpawn();
		Castle castle = getCastle();
		if (castle != null)
		{
			castle.registerArtefact(this);
		}
	}

    @Override
	public boolean isAutoAttackable(L2Character attacker)
    {
        return false;
    }

    @Override
	public boolean isAttackable()
    {
        return false;
    }
    
    @Override
    public void onInteract(L2PcInstance player)
    {
    	// Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
        player.sendPacket(new ActionFailed());
    }

    @Override
    public void onForcedAttack(L2PcInstance player)
    {
        // Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
        player.sendPacket(new ActionFailed());
    }

    @Override
	public void reduceCurrentHp(double damage, L2Character attacker){}
    public void reduceCurrentHp(double damage, L2Character attacker, boolean awake){}
}
