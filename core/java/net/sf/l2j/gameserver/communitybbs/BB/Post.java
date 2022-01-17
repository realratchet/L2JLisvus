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
 *
 */
public class Post
{
	private static Logger _log = Logger.getLogger(Post.class.getName());
	public class CPost
	{
	        public int _PostID;
	        public String _PostOwner;
	        public int _PostOwnerID;
	        public long _PostDate;
	        public int _PostTopicID;
	        public int _PostForumID;
	        public String _PostTxt;
	}

	private List<CPost> _post;

	public Post(String _PostOwner,int _PostOwnerID,long date,int tid,int _PostForumID,String txt)
	{				
			_post = new ArrayList<>();
			CPost cp = new CPost();
			cp._PostID = 0;
			cp._PostOwner = _PostOwner;
			cp._PostOwnerID = _PostOwnerID;
			cp._PostDate = date;
			cp._PostTopicID = tid;
			cp._PostForumID = _PostForumID;
			cp._PostTxt = txt;
			_post.add(cp);
			insertindb(cp);
			
	}

	public void insertindb(CPost cp)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
                        PreparedStatement statement = con.prepareStatement("INSERT INTO posts (post_id,post_owner_name,post_ownerid,post_date,post_topic_id,post_forum_id,post_txt) values (?,?,?,?,?,?,?)"))
		{
			statement.setInt(1, cp._PostID);
			statement.setString(2, cp._PostOwner);
			statement.setInt(3, cp._PostOwnerID);
			statement.setLong(4, cp._PostDate);
			statement.setInt(5, cp._PostTopicID);
			statement.setInt(6, cp._PostForumID);
			statement.setString(7, cp._PostTxt);			
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("error while saving new Post to db " + e);
		}
	}

	public Post(Topic t)
	{
		_post = new ArrayList<>();
		load(t);
	}
	
	public CPost getCPost(int id)
	{
		int i = 0;
		for(CPost cp : _post)
		{
			if (i++ == id)
			{
				return cp;
			}
		}
		return null;
	}

	public void deleteme(Topic t)
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
			        while(result.next())
			        {
				        CPost cp = new CPost();
				        cp._PostID = Integer.parseInt(result.getString("post_id"));
				        cp._PostOwner = result.getString("post_owner_name");
				        cp._PostOwnerID = Integer.parseInt(result.getString("post_ownerid"));
				        cp._PostDate = Long.parseLong(result.getString("post_date"));
				        cp._PostTopicID = Integer.parseInt(result.getString("post_topic_id"));
				        cp._PostForumID = Integer.parseInt(result.getString("post_forum_id"));
				        cp._PostTxt = result.getString("post_txt");
				        _post.add(cp);
			        }
                        }
		}
		catch (Exception e)
		{
			_log.warning("data error on Post " + t.getForumID() + "/"+t.getID()+" : " + e);
			e.printStackTrace();
		}
	}

	/**
	 * @param i
	 */
	public void updatetxt(int i)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("UPDATE posts SET post_txt=? WHERE post_id=? AND post_topic_id=? AND post_forum_id=?"))
		{
			CPost cp = getCPost(i);

			statement.setString(1, cp._PostTxt);
			statement.setInt(2, cp._PostID);
			statement.setInt(3, cp._PostTopicID);
			statement.setInt(4, cp._PostForumID);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warning("error while saving new Post to db " + e);
		}
	}
}