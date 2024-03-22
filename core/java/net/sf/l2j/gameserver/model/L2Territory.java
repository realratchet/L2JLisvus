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
package net.sf.l2j.gameserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.util.Rnd;

public class L2Territory
{
	private static final Logger _log = Logger.getLogger(L2Territory.class.getName());
	
	protected class Point
	{
		private int _x, _y, _zMin, _zMax, _proc;
		
		Point(int x, int y, int zMin, int zMax, int proc)
		{
			_x = x;
			_y = y;
			_zMin = zMin;
			_zMax = zMax;
			_proc = proc;
		}
		
		public int getX()
		{
			return _x;
		}
		
		public int getY()
		{
			return _y;
		}
		
		public int getZMin()
		{
			return _zMin;
		}
		
		public int getZMax()
		{
			return _zMax;
		}
		
		public int getProc()
		{
			return _proc;
		}
	}
	
	private List<Point> _points;
	private int _terr;
	private int _xMin;
	private int _xMax;
	private int _yMin;
	private int _yMax;
	private int _zMin;
	private int _zMax;
	private int _procMax;
	
	public L2Territory(int terr)
	{
		_points = new ArrayList<>();
		_terr = terr;
		_xMin = 999999;
		_xMax = -999999;
		_yMin = 999999;
		_yMax = -999999;
		_zMin = 999999;
		_zMax = -999999;
		_procMax = 0;
	}
	
	public void add(int x, int y, int zMin, int zMax, int proc)
	{
		_points.add(new Point(x, y, zMin, zMax, proc));
		
		if (x < _xMin)
			_xMin = x;
		if (y < _yMin)
			_yMin = y;
		if (x > _xMax)
			_xMax = x;
		if (y > _yMax)
			_yMax = y;
		if (zMin < _zMin)
			_zMin = zMin;
		if (zMax > _zMax)
			_zMax = zMax;
		_procMax += proc;
	}
	
	public void print()
	{
		for (Point p : _points)
			System.out.println("(" + p.getX() + "," + p.getY() + ")");
	}
	
	public boolean isIntersect(int x, int y, Point p1, Point p2)
	{
		double dy1 = p1.getY() - y;
		double dy2 = p2.getY() - y;
		
		if (Math.signum(dy1) == Math.signum(dy2))
			return false;
		
		double dx1 = p1.getX() - x;
		double dx2 = p2.getX() - x;
		
		if (dx1 >= 0 && dx2 >= 0)
			return true;
		
		if (dx1 < 0 && dx2 < 0)
			return false;
		
		double dx0 = (dy1 * (p1.getX() - p2.getX())) / (p1.getY() - p2.getY());
		
		return dx0 <= dx1;
	}
	
	public boolean isInside(int x, int y)
	{
		int intersectCount = 0;
		
		int size = _points.size();
		for (int i = 0; i < size; i++)
		{
			Point p1 = _points.get(i > 0 ? i - 1 : size - 1);
			Point p2 = _points.get(i);
			
			if (isIntersect(x, y, p1, p2))
				intersectCount++;
		}
		
		return intersectCount % 2 == 1;
	}
	
	public int[] getRandomPoint()
	{
		int i;
		int[] p = new int[4];
		
		if (_procMax > 0)
		{
			int pos = 0;
			int rnd = Rnd.nextInt(_procMax);
			for (Point point : _points)
			{
				pos += point.getProc();
				if (rnd <= pos)
				{
					p[0] = point.getX();
					p[1] = point.getY();
					p[2] = point.getZMin();
					p[3] = point.getZMax();
					return p;
				}
			}
			
		}
		
		for (i = 0; i < 100; i++)
		{
			p[0] = Rnd.get(_xMin, _xMax);
			p[1] = Rnd.get(_yMin, _yMax);
			
			if (isInside(p[0], p[1]))
			{
				double curdistance = 0;
				p[2] = _zMin + 100;
				p[3] = _zMax;
				
				for (Point point : _points)
				{
					double dx = point.getX() - p[0];
					double dy = point.getY() - p[1];
					double distance = Math.sqrt(dx * dx + dy * dy);
					if (curdistance == 0 || distance < curdistance)
					{
						curdistance = distance;
						p[2] = point.getZMin() + 100;
					}
				}
				return p;
			}
		}
		_log.warning("Can't make point for territory" + _terr);
		return p;
	}
	
	public int getProcMax()
	{
		return _procMax;
	}
}