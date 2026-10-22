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
package net.sf.l2j.gameserver.network.clientpackets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager.SeedProduction;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2Manor;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.actor.instance.L2CastleChamberlainInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.Castle;

/**
 * Format: (ch) dd [ddd]
 * @author l3x
 */
public class RequestSetSeed extends L2GameClientPacket
{
	private static final String _C__D0_0A_REQUESTSETSEED = "[C] D0:0A RequestSetSeed";

	private int _size;
	private int _manorId;
	private int[] _items; // _size*3

	@Override
	protected void readImpl()
	{
		_manorId = readD();
		_size = readD();
		if (((_size * 12) > _buf.remaining()) || (_size > 500))
		{
			_size = 0;
			return;
		}

		_items = new int[_size * 3];
		for (int i = 0; i < _size; i++)
		{
			int itemId = readD();
			_items[(i * 3) + 0] = itemId;
			int sales = readD();
			_items[(i * 3) + 1] = sales;
			int price = readD();
			_items[(i * 3) + 2] = price;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.network.clientpackets.L2GameClientPacket#runImpl()
	 */
	@Override
	public void runImpl()
	{
		if (_size < 1 || _manorId < 1)
		{
			return;
		}

		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}

		if (CastleManorManager.getInstance().isDisabled() || CastleManorManager.getInstance().isUnderMaintenance())
		{
			return;
		}

		Castle castle = CastleManager.getInstance().getCastleById(_manorId);
		if (castle == null || castle.isNextPeriodApproved())
		{
			return;
		}

		// Only castle owner clan members with castle rights, talking to that castle's chamberlain
		if ((player.getClan() == null) || (castle.getOwnerId() != player.getClanId()) || (!player.isClanLeader() && ((player.getClanPrivileges() & L2Clan.CP_CS_OTHER_RIGHTS) != L2Clan.CP_CS_OTHER_RIGHTS)))
		{
			return;
		}

		L2Object npc = player.getLastFolkNPC();
		if (!(npc instanceof L2CastleChamberlainInstance) || (((L2CastleChamberlainInstance) npc).getCastle() != castle) || !((L2CastleChamberlainInstance) npc).canInteract(player))
		{
			return;
		}

		List<Integer> castleSeeds = L2Manor.getInstance().getSeedsForCastle(_manorId);
		List<SeedProduction> seeds = new ArrayList<>();
		Set<Integer> seen = new HashSet<>();
		for (int i = 0; i < _size; i++)
		{
			int id = _items[(i * 3) + 0];
			int sales = _items[(i * 3) + 1];
			int price = _items[(i * 3) + 2];
			if (id > 0)
			{
				// Reject the whole list on any invalid entry
				if (!castleSeeds.contains(id) || !seen.add(id) || (sales < 0) || (sales > L2Manor.getInstance().getSeedSaleLimit(id)))
				{
					return;
				}
				int basePrice = L2Manor.getInstance().getSeedBasicPrice(id);
				if ((sales > 0) && ((price < ((basePrice * 60) / 100)) || (price > (basePrice * 10))))
				{
					return;
				}
				seeds.add(CastleManorManager.getInstance().getNewSeedProduction(id, sales, price, sales));
			}
		}

		castle.setSeedProduction(seeds, CastleManorManager.PERIOD_NEXT);
		
		if (Config.ALT_MANOR_SAVE_ALL_ACTIONS)
		{
			castle.saveSeedData(CastleManorManager.PERIOD_NEXT);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_0A_REQUESTSETSEED;
	}
}