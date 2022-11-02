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
package net.sf.l2j.gameserver.model.olympiad;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.instancemanager.ZoneManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.olympiad.Olympiad.COMP_TYPE;
import net.sf.l2j.gameserver.model.zone.type.L2OlympiadStadiumZone;
import net.sf.l2j.util.Rnd;

/**
 * @author DnR
 *
 */
class OlympiadManager implements Runnable
{
    private Map<Integer, OlympiadGame> _olympiadInstances;
    
    public static OlympiadManager getInstance()
	{
		return SingletonHolder._instance;
	}
    
	private OlympiadManager()
	{
        _olympiadInstances = new ConcurrentHashMap<>();
	}

    @Override
	public synchronized void run()
    {
        if (Olympiad.getInstance().isOlympiadEnd())
            return;

        Collection<L2OlympiadStadiumZone> temp = ZoneManager.getInstance().getAllZones(L2OlympiadStadiumZone.class);
        L2OlympiadStadiumZone[] stadiums = temp.toArray(new L2OlympiadStadiumZone[temp.size()]);
        
        Map<Integer, OlympiadGameTask> _gamesQueue = new HashMap<>();
        while (Olympiad.getInstance().getCompPeriodState() != Olympiad.NONE)
        {
            if (Olympiad.getNobleCount() == 0)
            {
                try
                {
                    wait(60000);
                }
                catch(InterruptedException e)
                {
                    return;
                }
                continue;
            }

            List<Integer> readyClasses = Olympiad.hasEnoughRegisteredClassed();
			boolean readyNonClassed = Olympiad.hasEnoughRegisteredNonClassed();

			if (readyClasses != null || readyNonClassed)
            {
                // Set up the games queue
            	for (int i = 0; i < stadiums.length; i++)
                {
	                if (!existNextOpponents(Olympiad.getRegisteredNonClassBased()) && !existNextOpponents(getRandomClassList(Olympiad.getRegisteredClassBased(), readyClasses)))
                        break;

	                L2OlympiadStadiumZone stadium = stadiums[i];
	                if (stadium == null)
	                {
	                	continue;
	                }
	                
                    if (stadium.isFreeToUse())
                    {
                    	if (readyNonClassed && existNextOpponents(Olympiad.getRegisteredNonClassBased()))
                        {
                            try
                            {
                                _olympiadInstances.put(stadium.getStadiumId(), new OlympiadGame(stadium, COMP_TYPE.NON_CLASSED, nextOpponents(Olympiad.getRegisteredNonClassBased())));
                                _gamesQueue.put(stadium.getStadiumId(), new OlympiadGameTask(_olympiadInstances.get(stadium.getStadiumId())));
                                stadium.setStadiaBusy();
                            }
                            catch(Exception e)
                            {
                                if (_olympiadInstances.containsKey(stadium.getStadiumId()))
                                {
                                    final OlympiadGame game = _olympiadInstances.remove(stadium.getStadiumId());
                                    for (L2PcInstance player : game.getPlayers())
                                    {
                                        if (player == null)
                                        {
                                            continue;
                                        }
                                        
                                        player.sendMessage("Your olympiad registration was cancelled due to an error.");
                                        player.setIsInOlympiadMode(false);
                                        player.setIsOlympiadStart(false);
                                        player.setOlympiadSide(-1);
                                        player.setOlympiadGameId(-1);
                                    }
                                }

                                if (_gamesQueue.containsKey(stadium.getStadiumId()))
                                    _gamesQueue.remove(stadium.getStadiumId());
                                stadium.setStadiaFree();

                                // Try to reuse this stadium next time
                                i--;
                            }
                        }
                    	else if (readyClasses != null && existNextOpponents(getRandomClassList(Olympiad.getRegisteredClassBased(), readyClasses)))
                        {
                            try
                            {
                                _olympiadInstances.put(stadium.getStadiumId(), new OlympiadGame(stadium, COMP_TYPE.CLASSED, nextOpponents(getRandomClassList(Olympiad.getRegisteredClassBased(), readyClasses))));
                                _gamesQueue.put(stadium.getStadiumId(), new OlympiadGameTask(_olympiadInstances.get(stadium.getStadiumId())));
                                stadium.setStadiaBusy();
                            }
                            catch(Exception e)
                            {
                                if (_olympiadInstances.containsKey(stadium.getStadiumId()))
                                {
                                    final OlympiadGame game = _olympiadInstances.remove(stadium.getStadiumId());
                                    for (L2PcInstance player : game.getPlayers())
                                    {
                                        if (player == null)
                                        {
                                            continue;
                                        }

                                        player.sendMessage("Your olympiad registration was cancelled due to an error.");
                                        player.setIsInOlympiadMode(false);
                                        player.setIsOlympiadStart(false);
                                        player.setOlympiadSide(-1);
                                        player.setOlympiadGameId(-1);
                                    }
                                }

                                if (_gamesQueue.containsKey(stadium.getStadiumId()))
                                    _gamesQueue.remove(stadium.getStadiumId());
                                stadium.setStadiaFree();

                                // Try to reuse this stadium next time
                                i--;
                            }
                        }
                    }
                    else
                    {
                        if (!_gamesQueue.containsKey(stadium.getStadiumId()) || _gamesQueue.get(stadium.getStadiumId()).isTerminated() || _gamesQueue.get(stadium.getStadiumId())._game == null)
                        {
                            try
                            {
                                _olympiadInstances.remove(stadium.getStadiumId());
                                _gamesQueue.remove(stadium.getStadiumId());
                                stadium.setStadiaFree();
                                // Try to reuse this stadium next time
                                i--;
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // Start games
                for (OlympiadGameTask game : _gamesQueue.values())
                {
                    if (game != null && !game.isTerminated() && !game.isStarted())
                    {
                        // start new games
                        Thread T = new Thread(game);
                        T.start();
                    }
                }
            }

            // Wait 30 sec due to server stress
            try
            {
                wait(30000);
            }
            catch (InterruptedException e)
            {
                return;
            }
        }

        // When competition time finishes wait for all games to be terminated before executing the cleanup code
        boolean allGamesTerminated = false;
        // Wait for all games to be terminated
        while (!allGamesTerminated)
        {
            try
            {
                wait(30000);
            }
            catch (InterruptedException e)
            {
            }

            if (_gamesQueue.size() == 0)
                allGamesTerminated = true;
            else
            {
                for (OlympiadGameTask game : _gamesQueue.values())
                    allGamesTerminated = allGamesTerminated || game.isTerminated();
            }
        }

        // When all games are terminated clean up everything
        _gamesQueue.clear();

        // Wait 20 seconds
        _olympiadInstances.clear();
        Olympiad.clearRegistered();

		OlympiadGame._battleStarted = false;
	}
    
    protected OlympiadGame getOlympiadGame(int id)
	{
		return _olympiadInstances.get(id);
	}

    protected void removeGame(int stadiumId)
 	{
    	_olympiadInstances.remove(stadiumId);
    }
    
    protected Map<Integer, OlympiadGame> getOlympiadGames()
    {
        return _olympiadInstances;
    }

    protected List<L2PcInstance> getRandomClassList(Map<Integer, List<L2PcInstance>> list, List<Integer> classList)
	{
		if (list == null || classList == null || list.isEmpty() || classList.isEmpty())
		{
			return null;
		}
		
		return list.get(classList.get(Rnd.nextInt(classList.size())));
	}

    private L2PcInstance[] nextOpponents(List<L2PcInstance> participants)
    {
        final int count = participants.size();
        if (count < 2) {
            return null;
        }

        final L2PcInstance[] opponents = new L2PcInstance[2];
        for (int i = 0; i < opponents.length; i++) {
            int randomIndex = Rnd.nextInt(count - i);
            opponents[i] = participants.remove(randomIndex);
        }
        return opponents;
    }

    private boolean existNextOpponents(List<L2PcInstance> participants)
    {
        return participants != null && participants.size() > 1;
    }

    protected Map<Integer, String> getAllTitles()
	{
		Map<Integer, String> titles = new HashMap<>();
		for (OlympiadGame instance : _olympiadInstances.values())
		{
			if (instance.getState() == OlympiadGame.INITIAL)
				continue;
			
			titles.put(instance.getStadium().getStadiumId(), instance.getTitle());
		}
		
		return titles;
	}
    
    private static class SingletonHolder
	{
		protected static final OlympiadManager _instance = new OlympiadManager();
	}
}