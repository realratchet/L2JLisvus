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

/**
 * This class defines all classes (ex : Human fighter, darkFighter...) that a player can chose.<BR><BR>
 * 
 * Data :<BR><BR>
 * <li>id : The Identifier of the class</li>
 * <li>isMage : True if the class is a mage class</li>
 * <li>race : The race of this class</li>
 * <li>parent : The parent ClassId or null if this class is the root</li><BR><BR>
 * 
 * @version $Revision: 1.4.4.4 $ $Date: 2005/03/27 15:29:33 $
 * 
 */
public enum ClassId
{
    fighter(0, false, Race.HUMAN, null),

    warrior(1, false, Race.HUMAN, fighter), gladiator(2, false, Race.HUMAN, warrior), warlord(
            3, false, Race.HUMAN, warrior), knight(4, false, Race.HUMAN, fighter), paladin(5,
            false, Race.HUMAN, knight), darkAvenger(6, false, Race.HUMAN, knight), rogue(7, false,
            Race.HUMAN, fighter), treasureHunter(8, false, Race.HUMAN, rogue), hawkeye(9, false,
            Race.HUMAN, rogue),

    mage(10, true, Race.HUMAN, null), wizard(11, true, Race.HUMAN, mage), sorceror(12, true,
            Race.HUMAN, wizard), necromancer(13, true, Race.HUMAN, wizard), warlock(14, true,
            Race.HUMAN, wizard), cleric(15, true, Race.HUMAN, mage), bishop(16, true, Race.HUMAN,
            cleric), prophet(17, true, Race.HUMAN, cleric),

    elvenFighter(18, false, Race.ELF, null), elvenKnight(19, false, Race.ELF, elvenFighter), templeKnight(
            20, false, Race.ELF, elvenKnight), swordSinger(21, false, Race.ELF, elvenKnight), elvenScout(
            22, false, Race.ELF, elvenFighter), plainsWalker(23, false, Race.ELF, elvenScout), silverRanger(
            24, false, Race.ELF, elvenScout),

    elvenMage(25, true, Race.ELF, null), elvenWizard(26, true, Race.ELF, elvenMage), spellsinger(
            27, true, Race.ELF, elvenWizard), elementalSummoner(28, true, Race.ELF, elvenWizard), oracle(
            29, true, Race.ELF, elvenMage), elder(30, true, Race.ELF, oracle),

    darkFighter(31, false, Race.DARK_ELF, null), palusKnight(32, false, Race.DARK_ELF, darkFighter), shillienKnight(
            33, false, Race.DARK_ELF, palusKnight), bladedancer(34, false, Race.DARK_ELF, palusKnight), assassin(
            35, false, Race.DARK_ELF, darkFighter), abyssWalker(36, false, Race.DARK_ELF, assassin), phantomRanger(
            37, false, Race.DARK_ELF, assassin),

    darkMage(38, true, Race.DARK_ELF, null), darkWizard(39, true, Race.DARK_ELF, darkMage), spellhowler(
            40, true, Race.DARK_ELF, darkWizard), phantomSummoner(41, true, Race.DARK_ELF, darkWizard), shillienOracle(
            42, true, Race.DARK_ELF, darkMage), shillienElder(43, true, Race.DARK_ELF, shillienOracle),

    orcFighter(44, false, Race.ORC, null), orcRaider(45, false, Race.ORC, orcFighter), destroyer(
            46, false, Race.ORC, orcRaider), orcMonk(47, false, Race.ORC, orcFighter), tyrant(48,
            false, Race.ORC, orcMonk),

    orcMage(49, false, Race.ORC, null), orcShaman(50, true, Race.ORC, orcMage), overlord(51, true,
            Race.ORC, orcShaman), warcryer(52, true, Race.ORC, orcShaman),

    dwarvenFighter(53, false, Race.DWARF, null), scavenger(54, false, Race.DWARF, dwarvenFighter), bountyHunter(
            55, false, Race.DWARF, scavenger), artisan(56, false, Race.DWARF, dwarvenFighter), warsmith(
            57, false, Race.DWARF, artisan),

    /*
     * Dummy Entries
     * <START>
     */
    dummyEntry1(58, false, null, null), dummyEntry2(59, false, null, null), dummyEntry3(60, false, null,
            null), dummyEntry4(61, false, null, null), dummyEntry5(62, false, null, null), dummyEntry6(
            63, false, null, null), dummyEntry7(64, false, null, null), dummyEntry8(65, false, null,
            null), dummyEntry9(66, false, null, null), dummyEntry10(67, false, null, null), dummyEntry11(
            68, false, null, null), dummyEntry12(69, false, null, null), dummyEntry13(70, false, null,
            null), dummyEntry14(71, false, null, null), dummyEntry15(72, false, null, null), dummyEntry16(
            73, false, null, null), dummyEntry17(74, false, null, null), dummyEntry18(75, false, null,
            null), dummyEntry19(76, false, null, null), dummyEntry20(77, false, null, null), dummyEntry21(
            78, false, null, null), dummyEntry22(79, false, null, null), dummyEntry23(80, false, null,
            null), dummyEntry24(81, false, null, null), dummyEntry25(82, false, null, null), dummyEntry26(
            83, false, null, null), dummyEntry27(84, false, null, null), dummyEntry28(85, false, null,
            null), dummyEntry29(86, false, null, null), dummyEntry30(87, false, null, null),
    /*
     * <END>
     * Of Dummy entries
     */

    /*
     * Now the bad boys! new class ids :)) (3rd classes)
     */
    duelist(88, false, Race.HUMAN, gladiator), dreadnought(89, false, Race.HUMAN, warlord), phoenixKnight(
            90, false, Race.HUMAN, paladin), hellKnight(91, false, Race.HUMAN, darkAvenger), sagittarius(
            92, false, Race.HUMAN, hawkeye), adventurer(93, false, Race.HUMAN, treasureHunter), archmage(
            94, true, Race.HUMAN, sorceror), soultaker(95, true, Race.HUMAN, necromancer), arcanaLord(
            96, true, Race.HUMAN, warlock), cardinal(97, true, Race.HUMAN, bishop), hierophant(98,
            true, Race.HUMAN, prophet),

    evaTemplar(99, false, Race.ELF, templeKnight), swordMuse(100, false, Race.ELF, swordSinger), windRider(
            101, false, Race.ELF, plainsWalker), moonlightSentinel(102, false, Race.ELF, silverRanger), mysticMuse(
            103, true, Race.ELF, spellsinger), elementalMaster(104, true, Race.ELF, elementalSummoner), evaSaint(
            105, true, Race.ELF, elder),

    shillienTemplar(106, false, Race.DARK_ELF, shillienKnight), spectralDancer(107, false,
            Race.DARK_ELF, bladedancer), ghostHunter(108, false, Race.DARK_ELF, abyssWalker), ghostSentinel(
            109, false, Race.DARK_ELF, phantomRanger), stormScreamer(110, true, Race.DARK_ELF,
            spellhowler), spectralMaster(111, true, Race.DARK_ELF, phantomSummoner), shillienSaint(112,
            true, Race.DARK_ELF, shillienElder),

    titan(113, false, Race.ORC, destroyer), grandKhauatari(114, false, Race.ORC, tyrant), dominator(
            115, true, Race.ORC, overlord), doomcryer(116, true, Race.ORC, warcryer),

    fortuneSeeker(117, false, Race.DWARF, bountyHunter), maestro(118, false, Race.DWARF, warsmith);

    /** The Identifier of the Class */
    private final int _id;

    /** True if the class is a mage class */
    private final boolean _isMage;

    /** The Race object of the class */
    private final Race _race;

    /** The parent ClassId or null if this class is a root */
    private final ClassId _parent;

    /**
     * Constructor of ClassId.<BR><BR>
     * @param pId 
     * @param pIsMage 
     * @param pRace 
     * @param pParent 
     */
    private ClassId(int pId, boolean pIsMage, Race pRace, ClassId pParent)
    {
        _id = pId;
        _isMage = pIsMage;
        _race = pRace;
        _parent = pParent;
    }

    /**
     * Return the Identifier of the Class.<BR><BR>
     * @return 
     */
    public final int getId()
    {
        return _id;
    }

    /**
     * Return True if the class is a mage class.<BR><BR>
     * @return 
     */
    public final boolean isMage()
    {
        return _isMage;
    }

    /**
     * Return the Race object of the class.<BR><BR>
     * @return 
     */
    public final Race getRace()
    {
        return _race;
    }

    /**
     * Return True if this Class is a child of the selected ClassId.<BR><BR>
     * 
     * @param cid The parent ClassId to check
     * @return 
     * 
     */
    public final boolean childOf(ClassId cid)
    {
        if (_parent == null)
        {
        	return false;
        }

        if (_parent == cid)
        {
        	return true;
        }

        return _parent.childOf(cid);

    }
    
    /**
     * Return True if this Class is equal to the selected ClassId or a child of the selected ClassId.<BR><BR>
     * 
     * @param cid The parent ClassId to check
     * @return 
     * 
     */
    public final boolean equalsOrChildOf(ClassId cid)
    {
        return this == cid || childOf(cid);
    }

    /**
     * Return the child level of this Class (0=root, 1=child level 1...).<BR><BR>
     * 
     * @return 
     * 
     */
    public final int level()
    {
        if (_parent == null)
        {
        	return 0;
        }

        return 1 + _parent.level();
    }

    /**
     * Return its parent ClassId<BR><BR>
     * @return 
     * 
     */
    public final ClassId getParent()
    {
        return _parent;
    }
}
