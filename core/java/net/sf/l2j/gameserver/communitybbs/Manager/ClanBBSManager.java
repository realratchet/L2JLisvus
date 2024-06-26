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
package net.sf.l2j.gameserver.communitybbs.Manager;

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.datatables.ClanTable;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.util.StringUtil;

public class ClanBBSManager extends BaseBBSManager
{
	public static ClanBBSManager getInstance()
	{
		return SingletonHolder._instance;
	}

	/**
	 * @param command
	 * @param activeChar
	 */
	@Override
	public void parseCmd(String command, L2PcInstance activeChar)
	{
		if (command.equals("_bbsclan"))
		{
			if (activeChar.getClan() != null)
			{
				if (activeChar.getClan().getLevel() >= 2)
				{
					clanHome(activeChar);
				}
				else
				{
					clanList(activeChar, 1);
				}
			}
			else
			{
				clanList(activeChar, 1);
			}
		}
		else if (command.startsWith("_bbsclan_clanlist"))
		{
			if (command.equals("_bbsclan_clanlist"))
			{
				clanList(activeChar, 1);
			}
			else if (command.startsWith("_bbsclan_clanlist;"))
			{
				StringTokenizer st = new StringTokenizer(command, ";");
				st.nextToken();
				int index = Integer.parseInt(st.nextToken());
				clanList(activeChar, index);
			}
		}
		else if (command.startsWith("_bbsclan_clanhome"))
		{
			if (command.equals("_bbsclan_clanhome"))
			{
				clanHome(activeChar);
			}
			else if (command.startsWith("_bbsclan_clanhome;"))
			{
				StringTokenizer st = new StringTokenizer(command, ";");
				st.nextToken();
				int index = Integer.parseInt(st.nextToken());
				clanHome(activeChar, index);
			}
		}
		else
		{
			separateAndSend("<html><body><br><br><center>Command : " + command + " is not implemented yet</center><br><br></body></html>", activeChar);
		}
	}

	/**
	 * @param activeChar
	 * @param index 
	 */
	private void clanList(L2PcInstance activeChar, int index)
	{
		if (index < 1)
		{
			index = 1;
		}
		//header
        StringBuilder html = new StringBuilder(2000);
		StringUtil.append(html, 
			"<html><body><br><br><center>",
			"<br1><br1><table border=0 cellspacing=0 cellpadding=0>",
			"<tr><td FIXWIDTH=15>&nbsp;</td>",
			"<td width=610 height=30 align=left>",
			"<a action=\"bypass _bbsclan_clanlist\"> CLAN COMMUNITY </a>",
			"</td></tr></table>",
			"<table border=0 cellspacing=0 cellpadding=0 width=610 bgcolor=434343>",
			"<tr><td height=10></td></tr>",
			"<tr>",
			"<td fixWIDTH=5></td>",
			"<td fixWIDTH=600>",
			"<a action=\"bypass _bbsclan_clanhome;" + ((activeChar.getClan() != null) ? activeChar.getClan().getClanId() : 0) + "\">[GO TO MY CLAN]</a>&nbsp;&nbsp;",
			"</td>",
			"<td fixWIDTH=5></td>",
			"</tr>",
			"<tr><td height=10></td></tr>",
			"</table>",
			"<br>",
			"<table border=0 cellspacing=0 cellpadding=2 bgcolor=5A5A5A width=610>",
			"<tr>",
			"<td FIXWIDTH=5></td>",
			"<td FIXWIDTH=200 align=center>CLAN NAME</td>",
			"<td FIXWIDTH=200 align=center>CLAN LEADER</td>",
			"<td FIXWIDTH=100 align=center>CLAN LEVEL</td>",
			"<td FIXWIDTH=100 align=center>CLAN MEMBERS</td>",
			"<td FIXWIDTH=5></td>",
			"</tr>",
			"</table>",
			"<img src=\"L2UI.Squareblank\" width=\"1\" height=\"5\">");

		int i = 0;
		String content = "";
		for (L2Clan cl : ClanTable.getInstance().getClans())
		{
			if(i > (index + 1)*7)
			{
				break;
			}
			if (i++ >= (index - 1)*7)
			{
				content += "<img src=\"L2UI.SquareBlank\" width=\"610\" height=\"3\">";
				content += "<table border=0 cellspacing=0 cellpadding=0 width=610>";
				content += "<tr> ";
				content += "<td FIXWIDTH=5></td>";
				content += "<td FIXWIDTH=200 align=center><a action=\"bypass _bbsclan_clanhome;"
					+ cl.getClanId() + "\">" + cl.getName() + "</a></td>";
				content += "<td FIXWIDTH=200 align=center>" + cl.getLeaderName() + "</td>";
				content += "<td FIXWIDTH=100 align=center>" + cl.getLevel() + "</td>";
				content += "<td FIXWIDTH=100 align=center>" + cl.getMembersCount() + "</td>";
				content += "<td FIXWIDTH=5></td>";
				content += "</tr>";
				content += "<tr><td height=5></td></tr>";
				content += "</table>";
				content += "<img src=\"L2UI.SquareBlank\" width=\"610\" height=\"3\">";
				content += "<img src=\"L2UI.SquareGray\" width=\"610\" height=\"1\">";
			}
		}
		StringUtil.append(html, content);

		StringUtil.append(html, "<img src=\"L2UI.SquareBlank\" width=\"610\" height=\"2\">");
		StringUtil.append(html, "<table cellpadding=0 cellspacing=2 border=0><tr>");
		if (index == 1)
		{
			StringUtil.append(html, "<td><button action=\"\" back=\"l2ui_ch3.prev1_down\" fore=\"l2ui_ch3.prev1\" width=16 height=16 ></td>");
		}
		else
		{
			StringUtil.append(html, "<td><button action=\"_bbsclan_clanlist;" + (index - 1)
				+ "\" back=\"l2ui_ch3.prev1_down\" fore=\"l2ui_ch3.prev1\" width=16 height=16 ></td>");
		}
		i = 0;
		int nbp;
		nbp = ClanTable.getInstance().getClans().length / 8;
		if (nbp * 8 != ClanTable.getInstance().getClans().length)
		{
			nbp++;
		}
		for (i = 1; i <= nbp; i++)
		{
			if (i == index)
			{
				StringUtil.append(html, "<td> " + i + " </td>");
			}
			else
			{
				StringUtil.append(html, "<td><a action=\"bypass _bbsclan_clanlist;" + i + "\"> " + i + " </a></td>");
			}

		}
		if (index == nbp)
		{
			StringUtil.append(html, "<td><button action=\"\" back=\"l2ui_ch3.next1_down\" fore=\"l2ui_ch3.next1\" width=16 height=16 ></td>");
		}
		else
		{
			StringUtil.append(html, "<td><button action=\"bypass _bbsclan_clanlist;" + (index + 1)
				+ "\" back=\"l2ui_ch3.next1_down\" fore=\"l2ui_ch3.next1\" width=16 height=16 ></td>");
		}
		StringUtil.append(html, 
			"</tr></table>",
			"<table border=0 cellspacing=0 cellpadding=0>",
			"<tr><td width=610><img src=\"sek.cbui141\" width=\"610\" height=\"1\"></td></tr>",
			"</table>",
			"<table border=0><tr><td><combobox width=65 var=keyword list=\"Name;Ruler\"></td><td><edit var = \"Search\" width=130 height=11 length=\"16\"></td>",

			//TODO: search (Write in BBS)
			"<td><button value=\"&$420;\" action=\"Write 5 -1 0 Search keyword keyword\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\"> </td> </tr></table>",
			"<br><br>",
			"</center>",
			"</body>",
			"</html>");
		separateAndSend(html.toString(), activeChar);
	}

	/**
	 * @param activeChar
	 */
	private void clanHome(L2PcInstance activeChar)
	{
		clanHome(activeChar, activeChar.getClan().getClanId());
	}

	/**
	 * @param activeChar
	 * @param clanId
	 */
	private void clanHome(L2PcInstance activeChar, int clanId)
	{
		L2Clan cl = ClanTable.getInstance().getClan(clanId);
		if (cl != null)
		{
			if (cl.getLevel() < 2)
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.NO_CB_IN_MY_CLAN));
				parseCmd("_bbsclan_clanlist",activeChar);
			}
			else
			{
                String html = StringUtil.concat(
                	"<html><body><center><br><br>",
                	"<br1><br1><table border=0 cellspacing=0 cellpadding=0>",
					"<tr><td FIXWIDTH=15>&nbsp;</td>",
					"<td width=610 height=30 align=left>",
					"<a action=\"bypass _bbshome\">HOME</a> &gt; <a action=\"bypass _bbsclan_clanlist\"> CLAN COMMUNITY </a>  &gt; <a action=\"bypass _bbsclan_clanhome;"
						+ clanId + "\"> &amp;$802; </a>",
					"</td></tr></table>",
					"<table border=0 cellspacing=0 cellpadding=0 width=610 bgcolor=434343>",
					"<tr><td height=10></td></tr>",
					"<tr>",
					"<td fixWIDTH=5></td>",
					"<td fixwidth=600>",
					"<a action=\"bypass _bbsclan_clanhome;" + clanId
						+ ";announce\">[CLAN ANNOUNCEMENT]</a> <a action=\"bypass _bbsclan_clanhome;"
						+ clanId + ";cbb\">[CLAN BULLETIN BOARD]</a>",
					"<a action=\"bypass _bbsclan_clanhome;" + clanId
						+ ";cmail\">[CLAN MAIL]</a>&nbsp;&nbsp;",
					"<a action=\"bypass _bbsclan_clanhome;" + clanId
						+ ";cnotice\">[CLAN NOTICE]</a>&nbsp;&nbsp;",
					"</td>",
					"<td fixWIDTH=5></td>",
					"</tr>",
					"<tr><td height=10></td></tr>",
					"</table>",
					"<table border=0 cellspacing=0 cellpadding=0 width=610>",
					"<tr><td height=10></td></tr>",
					"<tr><td fixWIDTH=5></td>",
					"<td fixwidth=290 valign=top>",
					"</td>",
					"<td fixWIDTH=5></td>",
					"<td fixWIDTH=5 align=center valign=top><img src=\"l2ui.squaregray\" width=2  height=128></td>",
					"<td fixWIDTH=5></td>",
					"<td fixwidth=295>",
					"<table border=0 cellspacing=0 cellpadding=0 width=295>",
					"<tr>",
					"<td fixWIDTH=100 align=left>CLAN NAME</td>",
					"<td fixWIDTH=195 align=left>" + cl.getName() + "</td>",
					"</tr>",
					"<tr><td height=7></td></tr>",
					"<tr>",
					"<td fixWIDTH=100 align=left>CLAN LEVEL</td>",
					"<td fixWIDTH=195 align=left height=16>" + cl.getLevel() + "</td>",
					"</tr>",
					"<tr><td height=7></td></tr>",
					"<tr>",
					"<td fixWIDTH=100 align=left>CLAN MEMBERS</td>",
					"<td fixWIDTH=195 align=left height=16>" + cl.getMembersCount() + "</td>",
					"</tr>",
					"<tr><td height=7></td></tr>",
					"<tr>",
					"<td fixWIDTH=100 align=left>CLAN LEADER</td>",
					"<td fixWIDTH=195 align=left height=16>" + cl.getLeaderName() + "</td>",
					"</tr>",
					"<tr><td height=7></td></tr>",
					//ADMINISTRATOR ??
					/*html.append("<tr>");
					 html.append("<td fixWIDTH=100 align=left>ADMINISTRATOR</td>");
					 html.append("<td fixWIDTH=195 align=left height=16>"+cl.getLeaderName()+"</td>");
					 html.append("</tr>");*/
					"<tr><td height=7></td></tr>",
					"<tr>",
					"<td fixWIDTH=100 align=left>ALLIANCE</td>",
					"<td fixWIDTH=195 align=left height=16>"
						+ ((cl.getAllyName() != null) ? cl.getAllyName() : "") + "</td>",
					"</tr>",
					"</table>",
					"</td>",
					"<td fixWIDTH=5></td>",
					"</tr>",
					"<tr><td height=10></td></tr>",
					"</table>",
					//TODO: the BB for clan :)
					//html.append("<table border=0 cellspacing=0 cellpadding=0 width=610  bgcolor=333333>");
					"<img src=\"L2UI.squareblank\" width=\"1\" height=\"5\">",
					"<img src=\"L2UI.squaregray\" width=\"610\" height=\"1\">",
					"<br>",
					"</center>",
					"<br> <br>",
					"</body>",
					"</html>");
				separateAndSend(html, activeChar);
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.communitybbs.Manager.BaseBBSManager#parsewrite(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, net.sf.l2j.gameserver.model.actor.instance.L2PcInstance)
	 */
	@Override
	public void parseWrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
	}
	
	private static class SingletonHolder
	{
		protected static final ClanBBSManager _instance = new ClanBBSManager();
	}
}