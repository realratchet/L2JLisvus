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
	// type
	public static final int ROOT = 0;
	public static final int NORMAL = 1;
	public static final int CLAN = 2;
	public static final int MEMO = 3;
	public static final int MAIL = 4;
	
	// perm
	public static final int INVISIBLE = 0;
	public static final int ALL = 1;
	public static final int CLANMEMBERONLY = 2;
	public static final int OWNERONLY = 3;
	
	private static Logger _log = Logger.getLogger(Forum.class.getName());

	private List<Forum> _children;
	private Map<Integer, Topic> _topic;
	private int _forumId;
	private String _forumName;
	private int _forumType;
	private int _forumPost;
	private int _forumPerm;
	private Forum _fParent;
	private int _ownerID;
	private boolean _loaded = false;
	
	/**
	 * @param forumId
	 * @param fParent
	 */
	public Forum(int forumId, Forum fParent)
	{
		_forumId = forumId;
		_fParent = fParent;
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
		_forumName = name;
		_forumId = ForumsBBSManager.getInstance().getANewID();
		_forumType = type;
		_forumPost = 0;
		_forumPerm = perm;
		_fParent = parent;
		_ownerID = OwnerID;
		_children = new ArrayList<>();
		_topic = new HashMap<>();

		parent._children.add(this);
		ForumsBBSManager.getInstance().addForum(this);

		_loaded = true;
	}
	
	/**
	 * 
	 */
	private void load()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM forums WHERE forum_id=?"))
		{
			statement.setInt(1, _forumId);
			try (ResultSet result = statement.executeQuery())
			{
				if (result.next())
				{
					_forumName = result.getString("forum_name");
					_forumPost = result.getInt("forum_post");
					_forumType = result.getInt("forum_type");
					_forumPerm = result.getInt("forum_perm");
					_ownerID = result.getInt("forum_owner_id");
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("data error on Forum " + _forumId + " : " + e);
			e.printStackTrace();
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM topic WHERE topic_forum_id=? ORDER BY topic_id DESC"))
		{
			statement.setInt(1, _forumId);
			try (ResultSet result = statement.executeQuery())
			{
				while (result.next())
				{
					Topic t = new Topic(Topic.ConstructorType.RESTORE, result.getInt("topic_id"), result.getInt("topic_forum_id"), result.getString("topic_name"), result.getLong("topic_date"), result.getString("topic_ownername"), result.getInt("topic_ownerID"), result.getInt("topic_type"), result.getInt("topic_reply"));
					_topic.put(t.getID(), t);
					if (t.getID() > TopicBBSManager.getInstance().getMaxID(this))
						TopicBBSManager.getInstance().setMaxID(t.getID(), this);
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("data error on Forum " + _forumId + " : " + e);
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
			statement.setInt(1, _forumId);
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
	
	public Topic getTopic(int j)
	{
		vload();
		return _topic.get(j);
	}
	
	public void addTopic(Topic t)
	{
		vload();
		_topic.put(t.getID(), t);
	}
	
	/**
	 * @return
	 */
	public int getID()
	{
		return _forumId;
	}
	
	public String getName()
	{
		vload();
		return _forumName;
	}
	
	public int getType()
	{
		vload();
		return _forumType;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public Forum getChildByName(String name)
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
	public void removeTopicByID(int id)
	{
		_topic.remove(id);
	}
	
	/**
	 * 
	 */
	public void insertIntoDB()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO forums (forum_id,forum_name,forum_parent,forum_post,forum_type,forum_perm,forum_owner_id) values (?,?,?,?,?,?,?)"))
		{
			statement.setInt(1, _forumId);
			statement.setString(2, _forumName);
			statement.setInt(3, _fParent.getID());
			statement.setInt(4, _forumPost);
			statement.setInt(5, _forumType);
			statement.setInt(6, _forumPerm);
			statement.setInt(7, _ownerID);
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
		if (!_loaded)
		{
			load();
			getChildren();
			_loaded = true;
		}
	}
}