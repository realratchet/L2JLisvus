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
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.communitybbs.Manager.PostBBSManager;

/**
 * @author Maktakien
 */
public class Post
{
	private static Logger _log = Logger.getLogger(Post.class.getName());
	
	public class CPost
	{
		public int _postID;
		public String _postOwner;
		public int _postOwnerID;
		public long _postDate;
		public int _postTopicID;
		public int _postForumID;
		public String _postTxt;
	}
	
	private List<CPost> _posts;
	
	public Post(String postOwner, int postOwnerID, long date, int tId, int postForumID, String txt)
	{
		_posts = new ArrayList<>();
		CPost cp = new CPost();
		cp._postID = 0;
		cp._postOwner = postOwner;
		cp._postOwnerID = postOwnerID;
		cp._postDate = date;
		cp._postTopicID = tId;
		cp._postForumID = postForumID;
		cp._postTxt = txt;
		_posts.add(cp);
		insertIntoDB(cp);
		
	}
	
	public void insertIntoDB(CPost cp)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO posts (post_id,post_owner_name,post_ownerid,post_date,post_topic_id,post_forum_id,post_txt) values (?,?,?,?,?,?,?)"))
		{
			statement.setInt(1, cp._postID);
			statement.setString(2, cp._postOwner);
			statement.setInt(3, cp._postOwnerID);
			statement.setLong(4, cp._postDate);
			statement.setInt(5, cp._postTopicID);
			statement.setInt(6, cp._postForumID);
			statement.setString(7, cp._postTxt);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("error while saving new Post to db " + e);
		}
	}
	
	public Post(Topic t)
	{
		_posts = new ArrayList<>();
		load(t);
	}
	
	public CPost getCPost(int id)
	{
		int i = 0;
		for (CPost cp : _posts)
		{
			if (i++ == id)
			{
				return cp;
			}
		}
		return null;
	}
	
	public void deleteMe(Topic t)
	{
		PostBBSManager.getInstance().delPostByTopic(t);
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("DELETE FROM posts WHERE post_forum_id=? AND post_topic_id=?"))
		{
			statement.setInt(1, t.getForumID());
			statement.setInt(2, t.getID());
			statement.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @param t
	 */
	private void load(Topic t)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM posts WHERE post_forum_id=? AND post_topic_id=? ORDER BY post_id ASC"))
		{
			statement.setInt(1, t.getForumID());
			statement.setInt(2, t.getID());
			try (ResultSet result = statement.executeQuery())
			{
				while (result.next())
				{
					CPost cp = new CPost();
					cp._postID = Integer.parseInt(result.getString("post_id"));
					cp._postOwner = result.getString("post_owner_name");
					cp._postOwnerID = Integer.parseInt(result.getString("post_ownerid"));
					cp._postDate = Long.parseLong(result.getString("post_date"));
					cp._postTopicID = Integer.parseInt(result.getString("post_topic_id"));
					cp._postForumID = Integer.parseInt(result.getString("post_forum_id"));
					cp._postTxt = result.getString("post_txt");
					_posts.add(cp);
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("data error on Post " + t.getForumID() + "/" + t.getID() + " : " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @param i
	 */
	public void updateTxt(int i)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE posts SET post_txt=? WHERE post_id=? AND post_topic_id=? AND post_forum_id=?"))
		{
			CPost cp = getCPost(i);
			
			statement.setString(1, cp._postTxt);
			statement.setInt(2, cp._postID);
			statement.setInt(3, cp._postTopicID);
			statement.setInt(4, cp._postForumID);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("error while saving new Post to db " + e);
		}
	}
}