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
package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.GMAudit;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class handles following admin commands: - delete = deletes target
 * 
 * @version $Revision: 1.1.2.6.2.3 $ $Date: 2005/04/11 10:05:59 $
 */
public class AdminRepairChar implements IAdminCommandHandler
{
    private static Logger _log = Logger.getLogger(AdminRepairChar.class.getName());

    private static String[] _adminCommands = { "admin_restore", "admin_repair" };

    @Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        String target = (activeChar.getTarget() != null?activeChar.getTarget().getName():"no-target");
        GMAudit.auditGMAction(activeChar.getName(), command, target, "");

        handleRepair(command);
        return true;
    }

    private void handleRepair(String command)
    {
        String[] parts = command.split(" ");
        if (parts.length != 2)
        {
            return;
        }
        
        String cmd = "UPDATE characters SET x=-84318, y=244579, z=-3730 WHERE char_name=?";
        try (Connection connection = L2DatabaseFactory.getInstance().getConnection())
        {
            try (PreparedStatement statement = connection.prepareStatement(cmd))
            {
                statement.setString(1,parts[1]);
                statement.execute();
            }

            int objId = 0;

            try (PreparedStatement statement = connection.prepareStatement("SELECT obj_id FROM characters where char_name=?"))
            {
                statement.setString(1,parts[1]);
                try (ResultSet rset = statement.executeQuery())
                {
                    if (rset.next())
                        objId = rset.getInt(1);
                }
            }

            if (objId != 0)
            {
                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM character_shortcuts WHERE char_obj_id=?"))
                {
                    statement.setInt(1, objId);
                    statement.execute();
                }

                try (PreparedStatement statement = connection.prepareStatement("UPDATE items SET loc=\"INVENTORY\" WHERE owner_id=?"))
                {
                    statement.setInt(1, objId);
                    statement.execute();
                }
            }
        }
        catch (Exception e)
        {
            _log.log(Level.WARNING, "could not repair char:", e);
        }
    }
    
    @Override
	public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}