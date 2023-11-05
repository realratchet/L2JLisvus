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
package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.L2Attackable;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2MonsterInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PlayableInstance;
import net.sf.l2j.gameserver.model.holder.SkillHolder;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.2.4 $ $Date: 2005/08/14 21:31:07 $
 */
public class SoulCrystals implements IItemHandler
{
    // Our main method, where everything goes on
    @Override
    public void useItem(L2PlayableInstance playable, L2ItemInstance item)
    {
        if (!(playable instanceof L2PcInstance))
            return;
        
        L2PcInstance activeChar = (L2PcInstance) playable;
        L2Object target = activeChar.getTarget();
        if (!(target instanceof L2MonsterInstance))
        {
            // Send a System Message to the caster
            activeChar.sendPacket(new SystemMessage(109));
            activeChar.sendPacket(new ActionFailed());
            return;
        }
        
        // You can use soul crystal only when target hp goes to < 50%
        if (((L2MonsterInstance) target).getCurrentHp() > ((L2MonsterInstance) target).getMaxHp() / 2)
        {
            activeChar.sendPacket(new ActionFailed());
            return;
        }
        
        if (item.getItem().getSkills() != null)
        {
            SkillHolder holder = item.getItem().getSkills()[0];
            L2Skill skill = holder.getSkill();
            activeChar.useMagic(skill, false, false);
            
            // Continue execution later
            CrystalFinalizer cf = new CrystalFinalizer(activeChar, target, item.getItemId());
            ThreadPoolManager.getInstance().scheduleEffect(cf, skill.getHitTime());
        }
    }
    
    static class CrystalFinalizer implements Runnable
    {
        private L2PcInstance _activeChar;
        private L2Attackable _target;
        private int _crystalId;
        
        CrystalFinalizer(L2PcInstance activeChar, L2Object target, int crystalId)
        {
            _activeChar = activeChar;
            _target = (L2Attackable) target;
            _crystalId = crystalId;
        }
        
        @Override
        public void run()
        {
            if (_activeChar.isDead() || _target.isDead())
                return;
            
            _activeChar.enableAllSkills();
            
            try
            {
                _target.addAbsorber(_activeChar, _crystalId);
                _activeChar.setTarget(_target);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
    }
}