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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.ai.L2CharacterAI;
import net.sf.l2j.gameserver.ai.L2SiegeGuardAI;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.gameserver.model.actor.knownlist.SiegeGuardKnownList;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;

/**
 * This class represents all guards in the world. It inherits all methods from
 * L2Attackable and adds some more such as tracking PK's or custom interactions.
 * 
 * @version $Revision: 1.11.2.1.2.7 $ $Date: 2005/04/06 16:13:40 $
 */
public final class L2SiegeGuardInstance extends L2Attackable
{
    public L2SiegeGuardInstance(int objectId, L2NpcTemplate template)
    {
        super(objectId, template);
        getKnownList();
    }

    @Override
	public final SiegeGuardKnownList getKnownList()
    {
        if (super.getKnownList() == null || !(super.getKnownList() instanceof SiegeGuardKnownList))
            this.setKnownList(new SiegeGuardKnownList(this));
        return (SiegeGuardKnownList)super.getKnownList();
    }

	@Override
	public L2CharacterAI getAI() 
	{
		synchronized(this)
		{
			if (_ai == null)
				_ai = new L2SiegeGuardAI(new AIAccessor());
		}
		return _ai;
	}    
    
	/**
	 * Return True if a siege is in progress and the L2Character attacker isn't a Defender.<BR><BR>
	 * 
	 * @param attacker The L2Character that the L2SiegeGuardInstance try to attack
	 * 
	 */
    @Override
	public boolean isAutoAttackable(L2Character attacker) 
    {
        L2PcInstance attackingPlayer = attacker.getActingPlayer();
        // Attackable during siege by all except defenders
        return (attackingPlayer != null
		        && getCastle() != null
		        && getCastle().getCastleId() > 0
		        && getCastle().getSiege().getIsInProgress()
		        && !getCastle().getSiege().checkIsDefender(attackingPlayer.getClan()));
    }

    /**
     * This method forces guard to return to home location previously set.
     *
     */
    public void returnHome()
    {
        if (!isInsideRadius(getSpawn().getLocX(), getSpawn().getLocY(), 40, false))
        {
            if (Config.DEBUG)
            	_log.fine(getObjectId()+": moving home");
            
            clearAggroList();
            
            if (hasAI())
            {
            	getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(getSpawn().getLocX(), getSpawn().getLocY(), getSpawn().getLocZ()));
            }
        }
    }

    @Override
	public void addDamageHate(L2Character attacker, int damage, int aggro)
    {
        if (attacker == null)
            return;
        
        if (!(attacker instanceof L2SiegeGuardInstance))
        {
            super.addDamageHate(attacker, damage, aggro);
        }
    }

    @Override
	public boolean hasRandomAnimation()
    {
        return false;
    }
}