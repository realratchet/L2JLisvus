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
package net.sf.l2j.gameserver.model.base;

import static net.sf.l2j.gameserver.model.base.ClassLevel.First;
import static net.sf.l2j.gameserver.model.base.ClassLevel.Fourth;
import static net.sf.l2j.gameserver.model.base.ClassLevel.Second;
import static net.sf.l2j.gameserver.model.base.ClassLevel.Third;
import static net.sf.l2j.gameserver.model.base.ClassType.Fighter;
import static net.sf.l2j.gameserver.model.base.ClassType.Mystic;
import static net.sf.l2j.gameserver.model.base.ClassType.Priest;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class ...
 * 
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:12:59 $
 */
public enum PlayerClass
{
    HumanFighter(Race.HUMAN, Fighter, First), Warrior(Race.HUMAN, Fighter, Second), Gladiator(Race.HUMAN, Fighter,
            Third), Warlord(Race.HUMAN, Fighter, Third), HumanKnight(Race.HUMAN, Fighter, Second), Paladin(Race.HUMAN,
            Fighter, Third), DarkAvenger(Race.HUMAN, Fighter, Third), Rogue(Race.HUMAN, Fighter, Second), TreasureHunter(
            Race.HUMAN, Fighter, Third), Hawkeye(Race.HUMAN, Fighter, Third), HumanMystic(Race.HUMAN, Mystic, First), HumanWizard(
            Race.HUMAN, Mystic, Second), Sorceror(Race.HUMAN, Mystic, Third), Necromancer(Race.HUMAN, Mystic, Third), Warlock(
            Race.HUMAN, Mystic, Third), Cleric(Race.HUMAN, Priest, Second), Bishop(Race.HUMAN, Priest, Third), Prophet(
            Race.HUMAN, Priest, Third),

    ElvenFighter(Race.ELF, Fighter, First), ElvenKnight(Race.ELF, Fighter, Second), TempleKnight(
            Race.ELF, Fighter, Third), Swordsinger(Race.ELF, Fighter, Third), ElvenScout(Race.ELF,
            Fighter, Second), Plainswalker(Race.ELF, Fighter, Third), SilverRanger(Race.ELF, Fighter,
            Third), ElvenMystic(Race.ELF, Mystic, First), ElvenWizard(Race.ELF, Mystic, Second), Spellsinger(
            Race.ELF, Mystic, Third), ElementalSummoner(Race.ELF, Mystic, Third), ElvenOracle(Race.ELF,
            Priest, Second), ElvenElder(Race.ELF, Priest, Third),

    DarkElvenFighter(Race.DARK_ELF, Fighter, First), PalusKnight(Race.DARK_ELF, Fighter, Second), ShillienKnight(
            Race.DARK_ELF, Fighter, Third), Bladedancer(Race.DARK_ELF, Fighter, Third), Assassin(Race.DARK_ELF, Fighter,
            Second), AbyssWalker(Race.DARK_ELF, Fighter, Third), PhantomRanger(Race.DARK_ELF, Fighter, Third), DarkElvenMystic(
            Race.DARK_ELF, Mystic, First), DarkElvenWizard(Race.DARK_ELF, Mystic, Second), Spellhowler(Race.DARK_ELF,
            Mystic, Third), PhantomSummoner(Race.DARK_ELF, Mystic, Third), ShillienOracle(Race.DARK_ELF, Priest,
            Second), ShillienElder(Race.DARK_ELF, Priest, Third),

    OrcFighter(Race.ORC, Fighter, First), OrcRaider(Race.ORC, Fighter, Second), Destroyer(Race.ORC, Fighter, Third), OrcMonk(
            Race.ORC, Fighter, Second), Tyrant(Race.ORC, Fighter, Third), OrcMystic(Race.ORC, Mystic, First), OrcShaman(
            Race.ORC, Mystic, Second), Overlord(Race.ORC, Mystic, Third), Warcryer(Race.ORC, Mystic, Third),

    DwarvenFighter(Race.DWARF, Fighter, First), DwarvenScavenger(Race.DWARF, Fighter, Second), BountyHunter(Race.DWARF,
            Fighter, Third), DwarvenArtisan(Race.DWARF, Fighter, Second), Warsmith(Race.DWARF, Fighter, Third),

    dummyEntry1(null, null, null), dummyEntry2(null, null, null), dummyEntry3(null, null, null), dummyEntry4(
            null, null, null), dummyEntry5(null, null, null), dummyEntry6(null, null, null), dummyEntry7(
            null, null, null), dummyEntry8(null, null, null), dummyEntry9(null, null, null), dummyEntry10(
            null, null, null), dummyEntry11(null, null, null), dummyEntry12(null, null, null), dummyEntry13(
            null, null, null), dummyEntry14(null, null, null), dummyEntry15(null, null, null), dummyEntry16(
            null, null, null), dummyEntry17(null, null, null), dummyEntry18(null, null, null), dummyEntry19(
            null, null, null), dummyEntry20(null, null, null), dummyEntry21(null, null, null), dummyEntry22(
            null, null, null), dummyEntry23(null, null, null), dummyEntry24(null, null, null), dummyEntry25(
            null, null, null), dummyEntry26(null, null, null), dummyEntry27(null, null, null), dummyEntry28(
            null, null, null), dummyEntry29(null, null, null), dummyEntry30(null, null, null),

    /*
     * (3rd classes)
     */
    duelist(Race.HUMAN, Fighter, Fourth), dreadnought(Race.HUMAN, Fighter, Fourth), phoenixKnight(Race.HUMAN, Fighter,
            Fourth), hellKnight(Race.HUMAN, Fighter, Fourth), sagittarius(Race.HUMAN, Fighter, Fourth), adventurer(
            Race.HUMAN, Fighter, Fourth), archmage(Race.HUMAN, Mystic, Fourth), soultaker(Race.HUMAN, Mystic, Fourth), arcanaLord(
            Race.HUMAN, Mystic, Fourth), cardinal(Race.HUMAN, Priest, Fourth), hierophant(Race.HUMAN, Priest, Fourth),

    evaTemplar(Race.ELF, Fighter, Fourth), swordMuse(Race.ELF, Fighter, Fourth), windRider(Race.ELF,
            Fighter, Fourth), moonlightSentinel(Race.ELF, Fighter, Fourth), mysticMuse(Race.ELF, Mystic,
            Fourth), elementalMaster(Race.ELF, Mystic, Fourth), evaSaint(Race.ELF, Priest, Fourth),

    shillienTemplar(Race.DARK_ELF, Fighter, Fourth), spectralDancer(Race.DARK_ELF, Fighter, Fourth), ghostHunter(
            Race.DARK_ELF, Fighter, Fourth), ghostSentinel(Race.DARK_ELF, Fighter, Fourth), stormScreamer(Race.DARK_ELF,
            Mystic, Fourth), spectralMaster(Race.DARK_ELF, Mystic, Fourth), shillienSaint(Race.DARK_ELF, Priest,
            Fourth),

    titan(Race.ORC, Fighter, Fourth), grandKhauatari(Race.ORC, Fighter, Fourth), dominator(Race.ORC, Mystic, Fourth), doomcryer(
            Race.ORC, Mystic, Fourth),

    fortuneSeeker(Race.DWARF, Fighter, Fourth), maestro(Race.DWARF, Fighter, Fourth);

    private Race _race;
    private ClassLevel _level;
    private ClassType _type;

    private static final Set<PlayerClass> mainSubclassSet;
    private static final Set<PlayerClass> neverSubclassed = EnumSet.of(Overlord, Warsmith);

    private static final Set<PlayerClass> subclasseSet1 = EnumSet.of(DarkAvenger, Paladin, TempleKnight,
                                                                     ShillienKnight);
    private static final Set<PlayerClass> subclasseSet2 = EnumSet.of(TreasureHunter, AbyssWalker,
                                                                     Plainswalker);
    private static final Set<PlayerClass> subclasseSet3 = EnumSet.of(Hawkeye, SilverRanger,
                                                                     PhantomRanger);
    private static final Set<PlayerClass> subclasseSet4 = EnumSet.of(Warlock, ElementalSummoner,
                                                                     PhantomSummoner);
    private static final Set<PlayerClass> subclasseSet5 = EnumSet.of(Sorceror, Spellsinger, Spellhowler);

    private static final EnumMap<PlayerClass, Set<PlayerClass>> subclassSetMap = new EnumMap<>(PlayerClass.class);

    static
    {
        Set<PlayerClass> subclasses = getSet(null, Third);
        subclasses.removeAll(neverSubclassed);

        mainSubclassSet = subclasses;

        subclassSetMap.put(DarkAvenger, subclasseSet1);
        subclassSetMap.put(Paladin, subclasseSet1);
        subclassSetMap.put(TempleKnight, subclasseSet1);
        subclassSetMap.put(ShillienKnight, subclasseSet1);

        subclassSetMap.put(TreasureHunter, subclasseSet2);
        subclassSetMap.put(AbyssWalker, subclasseSet2);
        subclassSetMap.put(Plainswalker, subclasseSet2);

        subclassSetMap.put(Hawkeye, subclasseSet3);
        subclassSetMap.put(SilverRanger, subclasseSet3);
        subclassSetMap.put(PhantomRanger, subclasseSet3);

        subclassSetMap.put(Warlock, subclasseSet4);
        subclassSetMap.put(ElementalSummoner, subclasseSet4);
        subclassSetMap.put(PhantomSummoner, subclasseSet4);

        subclassSetMap.put(Sorceror, subclasseSet5);
        subclassSetMap.put(Spellsinger, subclasseSet5);
        subclassSetMap.put(Spellhowler, subclasseSet5);
    }

    private PlayerClass(Race pRace, ClassType pType, ClassLevel pLevel)
    {
        _race = pRace;
        _level = pLevel;
        _type = pType;
    }

    public final Set<PlayerClass> getAvailableSubclasses(L2PcInstance player)
    {
        Set<PlayerClass> subclasses = null;

        if (_level == Third)
        {
            subclasses = EnumSet.copyOf(mainSubclassSet);
            subclasses.remove(this);

            switch (player.getRace())
            {
                case ELF:
                    subclasses.removeAll(getSet(Race.DARK_ELF, Third));
                    break;
                case DARK_ELF:
                    subclasses.removeAll(getSet(Race.ELF, Third));
                    break;
            }

            Set<PlayerClass> unavailableClasses = subclassSetMap.get(this);

            if (unavailableClasses != null)
            {
                subclasses.removeAll(unavailableClasses);
            }
        }

        return subclasses;
    }

    public static final EnumSet<PlayerClass> getSet(Race race, ClassLevel level)
    {
        EnumSet<PlayerClass> allOf = EnumSet.noneOf(PlayerClass.class);

        for (PlayerClass playerClass : EnumSet.allOf(PlayerClass.class))
        {
            if (race == null || playerClass.isOfRace(race))
            {
                if (level == null || playerClass.isOfLevel(level))
                {
                    allOf.add(playerClass);
                }
            }
        }

        return allOf;
    }

    public final boolean isOfRace(Race pRace)
    {
        return _race == pRace;
    }

    public final boolean isOfType(ClassType pType)
    {
        return _type == pType;
    }

    public final Race getRace()
    {
    	return _race;
    }
    
    public final boolean isOfLevel(ClassLevel pLevel)
    {
        return _level == pLevel;
    }
    
    public final ClassLevel getLevel()
    {
        return _level;
    }
}