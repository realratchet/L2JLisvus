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
package net.sf.l2j.gameserver.communitybbs.BB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.communitybbs.Manager.ForumsBBSManager;
import net.sf.l2j.gameserver.communitybbs.Manager.TopicBBSManager;

public class Forum
{
	//type
	public static final int ROOT = 0;
	public static final int NORMAL = 1;
	public static final int CLAN = 2;
	public static final int MEMO = 3;
	public static final int MAIL = 4;

	//perm
	public static final int INVISIBLE = 0;
	public static final int ALL = 1;
	public static final int CLANMEMBERONLY = 2;
	public static final int OWNERONLY = 3;

	private static Logger _log = Logger.getLogger(Forum.class.getName());
	private List<Forum> _children;
	private Map<Integer,Topic> _topic;
	private int _ForumId;
	private String _ForumName;
	//private int _ForumParent;
	private int _ForumType;
	private int _ForumPost;
	private int _ForumPerm;
	private Forum _FParent;
	private int _OwnerID;
	private boolean loaded = false;

	/**
	 * @param Forumid 
	 * @param FParent 
	 */
	public Forum(int Forumid, Forum FParent)
	{
		_ForumId = Forumid;
		_FParent = FParent;
		_children = new ArrayList<>();
		_topic = new HashMap<>();
	}

	/**
	 * @param name
	 * @param parent
	 * @param type
	 * @param perm
	 * @param OwnerID 
	 */
	public Forum(String name, Forum parent, int type, int perm, int OwnerID)
	{
		_ForumName = name;
		_ForumId = ForumsBBSManager.getInstance().GetANewID();
		//_ForumParent = parent.getID();
		_ForumType = type;
		_ForumPost = 0;
		_ForumPerm = perm;
		_FParent = parent;
		_OwnerID = OwnerID;
		_children = new ArrayList<>();
		_topic = new HashMap<>();
		parent._children.add(this);
		ForumsBBSManager.getInstance().addForum(this);
		loaded = true;
	}

	/**
	 * 
	 */
	private void load()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                        PreparedStatement statement = con.prepareStatement("SELECT * FROM forums WHERE forum_id=?"))
		{
			statement.setInt(1, _ForumId);
			try (ResultSet result = statement.executeQuery())
                        {
			        if (result.next())
			        {
				        _ForumName = result.getString("forum_name");
				        _ForumPost = result.getInt("forum_post");
				        _ForumType = result.getInt("forum_type");
				        _ForumPerm = result.getInt("forum_perm");
				        _OwnerID = result.getInt("forum_owner_id");
			        }
                        }
		}
		catch (Exception e)
		{
			_log.warning("data error on Forum " + _ForumId + " : " + e);
			e.printStackTrace();
		}

		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                        PreparedStatement statement = con.prepareStatement("SELECT * FROM topic WHERE topic_forum_id=? ORDER BY topic_id DESC"))
		{
			statement.setInt(1, _ForumId);
			try (ResultSet result = statement.executeQuery())
                        {
			        while (result.next())
			        {
				        Topic t = new Topic(Topic.ConstructorType.RESTORE,result.getInt("topic_id"),result.getInt("topic_forum_id"),result.getString("topic_name"),result.getLong("topic_date"),result.getString("topic_ownername"),result.getInt("topic_ownerid"),result.getInt("topic_type"),result.getInt("topic_reply"));
				        _topic.put(t.getID(),t);				
				        if (t.getID() > TopicBBSManager.getInstance().getMaxID(this))
					        TopicBBSManager.getInstance().setMaxID(t.getID(),this);
			        }
                        }
		}
		catch (Exception e)
		{
			_log.warning("data error on Forum " + _ForumId + " : " + e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void getChildren()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                        PreparedStatement statement = con.prepareStatement("SELECT forum_id FROM forums WHERE forum_parent=?"))
		{
			statement.setInt(1, _ForumId);
			try (ResultSet result = statement.executeQuery())
                        {
			        while (result.next())
			        {
                                        Forum f = new Forum(result.getInt("forum_id"), this);
				        _children.add(f);
                                        ForumsBBSManager.getInstance().addForum(f);
			        }
                        }
		}
		catch (Exception e)
		{
			_log.warning("data error on Forum (children): " + e);
			e.printStackTrace();
		}
	}

	public int getTopicSize()
	{
		vload();
		return _topic.size();
	}

	public Topic gettopic(int j)
	{
		vload();
		return _topic.get(j);
	}

	public void addtopic(Topic t)
	{
		vload();
		_topic.put(t.getID(), t);
	}

	/**
	* @return
	*/
	public int getID()
	{		
		return _ForumId;
	}

	public String getName()
	{
		vload();
		return _ForumName;
	}

	public int getType()
	{
		vload();
		return _ForumType;
	}

	/**
	 * @param name
	 * @return
	 */
	public Forum GetChildByName(String name)
	{
		vload();
		for (Forum f : _children)
		{
			if (f.getName().equals(name))
			{
				return f;
			}
		}		
		return null;
	}

	/**
	 * @param id
	 */
	public void RmTopicByID(int id)
	{
		_topic.remove(id);
	}

	/**
	 * 
	 */
	public void insertindb()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                        PreparedStatement statement = con.prepareStatement("INSERT INTO forums (forum_id,forum_name,forum_parent,forum_post,forum_type,forum_perm,forum_owner_id) values (?,?,?,?,?,?,?)"))
		{
			statement.setInt(1, _ForumId);
			statement.setString(2, _ForumName);
			statement.setInt(3, _FParent.getID());
			statement.setInt(4, _ForumPost);
			statement.setInt(5, _ForumType);
			statement.setInt(6, _ForumPerm);
			statement.setInt(7, _OwnerID);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("error while saving new Forum to db " + e);
		}
	}

	/**
	 * 
	 */
	public void vload()
	{		
		if (loaded == false)
		{
			load();
			getChildren();
			loaded = true;
		}
	}
}