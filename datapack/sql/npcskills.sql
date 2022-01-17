-- 
-- Table structure for table `npcskills`
-- 
DROP TABLE IF EXISTS npcskills;
CREATE TABLE npcskills (
  npcid int(11) NOT NULL default '0',
  skillid int(11) NOT NULL default '0',
  level int(11) NOT NULL default '0',
  PRIMARY KEY  (npcid,skillid,level)
);

-- 
-- Dumping data for table `npcskills`
-- 

INSERT INTO npcskills VALUES
-- Gremlin
(1, 4302, 1), -- Race
-- Rabbit
(2, 4293, 1), -- Race
-- Goblin
(3, 4295, 1), -- Race
-- Imp
(4, 4302, 1), -- Race
(4, 4281, 2), -- Wind Attack Weak Point
(4, 4276, 1), -- Archery Attack Weak Point
-- Imp Elder
(5, 4302, 1), -- Race
(5, 4281, 2), -- Wind Attack Weak Point
(5, 4276, 1), -- Archery Attack Weak Point
-- Orc Archer
(6, 4295, 1), -- Race
-- Green Fungus
(7, 4294, 1), -- Race
(7, 4279, 2), -- Fire Attack Weak Point
(7, 4277, 3), -- Resist Poison
-- Felim Lizardman
(8, 4295, 1), -- Race
-- Vuku Orc
(9, 4295, 1), -- Race
-- Felim Lizardman Scout
(10, 4295, 1), -- Race
-- Vuku Orc Archer
(11, 4295, 1), -- Race
-- Gora Werewolf
(12, 4295, 1), -- Race
-- Dryad
(13, 4296, 1), -- Race
(13, 4279, 2), -- Fire Attack Weak Point
(13, 4277, 3), -- Resist Poison
-- Felim Lizardman Warrior
(14, 4295, 1), -- Race
-- Marsh Zombie
(15, 4290, 1), -- Race
(15, 4275, 3), -- Sacred Attack Weak Point
(15, 4278, 1), -- Dark Attack
(15, 4071, 3), -- Resist Archery
(15, 4116, 8), -- Resist M. Atk.
(15, 4284, 3), -- Resist Bleeding
(15, 4248, 1), -- NPC HP Drain - Slow
-- Stone Golem
(16, 4291, 1), -- Race
(16, 4071, 4), -- Resist Archery
(16, 4273, 2), -- Resist Dagger
(16, 4274, 1), -- Blunt Attack Weak Point
(16, 4116, 8), -- Resist M. Atk.
(16, 4284, 3), -- Resist Bleeding
(16, 4247, 1), -- NPC Windstrike - Slow
-- Vuku Orc Fighter
(17, 4295, 1), -- Race
-- Carnivorous Fungus
(18, 4294, 1), -- Race
(18, 4279, 2), -- Fire Attack Weak Point
(18, 4277, 3), -- Resist Poison
-- Dryad Elder
(19, 4296, 1), -- Race
(19, 4279, 2), -- Fire Attack Weak Point
(19, 4277, 3), -- Resist Poison
-- Marsh Zombie Lord
(20, 4290, 1), -- Race
(20, 4275, 3), -- Sacred Attack Weak Point
(20, 4278, 1), -- Dark Attack
(20, 4071, 3), -- Resist Archery
(20, 4116, 8), -- Resist M. Atk.
(20, 4284, 3), -- Resist Bleeding
(20, 4248, 1), -- NPC HP Drain - Slow
-- Red Bear
(21, 4293, 1), -- Race
-- Misery Skeleton
(22, 4290, 1), -- Race
(22, 4275, 3), -- Sacred Attack Weak Point
(22, 4278, 1), -- Dark Attack
(22, 4274, 1), -- Blunt Attack Weak Point
-- Shadow Beast
(23, 4292, 1), -- Race
-- Langk Lizardman Warrior
(24, 4295, 1), -- Race
-- Lesser Dark Horror
(25, 4290, 1), -- Race
(25, 4275, 3), -- Sacred Attack Weak Point
(25, 4278, 1), -- Dark Attack
-- Ruin Zombie
(26, 4290, 1), -- Race
(26, 4275, 3), -- Sacred Attack Weak Point
(26, 4278, 1), -- Dark Attack
(26, 4071, 3), -- Resist Archery
(26, 4116, 8), -- Resist M. Atk.
(26, 4284, 3), -- Resist Bleeding
(26, 4249, 1), -- Decrease Speed
-- Langk Lizardman Scout
(27, 4295, 1), -- Race
-- Pincher
(28, 4301, 1), -- Race
-- Ruin Zombie Leader
(29, 4290, 1), -- Race
(29, 4275, 3), -- Sacred Attack Weak Point
(29, 4278, 1), -- Dark Attack
(29, 4071, 3), -- Resist Archery
(29, 4116, 8), -- Resist M. Atk.
(29, 4284, 3), -- Resist Bleeding
(29, 4247, 1), -- NPC Windstrike - Slow
-- Langk Lizardman
(30, 4295, 1), -- Race
-- Omen Beast
(31, 4292, 1), -- Race
-- Pinrul
(32, 4301, 1), -- Race
(32, 4279, 2), -- Fire Attack Weak Point
-- Shade Horror
(33, 4290, 1), -- Race
(33, 4275, 3), -- Sacred Attack Weak Point
(33, 4278, 1), -- Dark Attack
(33, 4076, 1), -- Reduction in movement speed
-- Prowler
(34, 4301, 1), -- Race
(34, 4279, 2), -- Fire Attack Weak Point
-- Tracker Skeleton
(35, 4290, 1), -- Race
(35, 4275, 3), -- Sacred Attack Weak Point
(35, 4278, 1), -- Dark Attack
(35, 4274, 1), -- Blunt Attack Weak Point
-- Lirein
(36, 4296, 1), -- Race
(36, 4011, 3), -- Resist Wind
(36, 4282, 2), -- Earth Attack Weak Point
(36, 4076, 1), -- Reduction in movement speed
-- Mana Seeker
(37, 4291, 1), -- Race
(37, 4303, 1), -- Strong Type
(37, 4039, 1), -- NPC MP Drain
-- Poison Spider
(38, 4301, 1), -- Race
(38, 4279, 2), -- Fire Attack Weak Point
(38, 4035, 1), -- Poison
-- Scavenger Wererat
(39, 4295, 1), -- Race
-- Sukar Wererat
(40, 4295, 1), -- Race
-- Tainted Zombie
(41, 4290, 1), -- Race
(41, 4275, 3), -- Sacred Attack Weak Point
(41, 4278, 1), -- Dark Attack
(41, 4071, 3), -- Resist Archery
(41, 4116, 8), -- Resist M. Atk.
(41, 4284, 3), -- Resist Bleeding
(41, 4249, 1), -- Decrease Speed
-- Tracker Skeleton Leader
(42, 4290, 1), -- Race
(42, 4275, 3), -- Sacred Attack Weak Point
(42, 4278, 1), -- Dark Attack
(42, 4274, 1), -- Blunt Attack Weak Point
-- Arachnid Tracker
(43, 4301, 1), -- Race
(43, 4279, 2), -- Fire Attack Weak Point
(43, 4035, 1), -- Poison
-- Lirein Elder
(44, 4296, 1), -- Race
(44, 4011, 3), -- Resist Wind
(44, 4282, 2), -- Earth Attack Weak Point
(44, 4151, 1), -- NPC Windstrike - Magic
(44, 4160, 1), -- NPC Aura Burn - Magic
-- Skeleton Scout
(45, 4290, 1), -- Race
(45, 4275, 3), -- Sacred Attack Weak Point
(45, 4278, 1), -- Dark Attack
(45, 4274, 1), -- Blunt Attack Weak Point
-- Stink Zombie
(46, 4290, 1), -- Race
(46, 4275, 3), -- Sacred Attack Weak Point
(46, 4278, 1), -- Dark Attack
(46, 4071, 3), -- Resist Archery
(46, 4116, 8), -- Resist M. Atk.
(46, 4284, 3), -- Resist Bleeding
(46, 4248, 1), -- NPC HP Drain - Slow
-- Sukar Wererat Leader
(47, 4295, 1), -- Race
-- Lesser Succubus
(48, 4298, 1), -- Race
(48, 4278, 1), -- Dark Attack
(48, 4333, 3), -- Resist Dark Attack
(48, 4046, 2), -- Sleep
-- Lycanthrope
(49, 4295, 1), -- Race
-- Arachnid Predator
(50, 4301, 1), -- Race
(50, 4279, 2), -- Fire Attack Weak Point
(50, 4035, 2), -- Poison
-- Skeleton Bowman
(51, 4290, 1), -- Race
(51, 4275, 3), -- Sacred Attack Weak Point
(51, 4278, 1), -- Dark Attack
(51, 4274, 1), -- Blunt Attack Weak Point
-- Lesser Succubus Turen
(52, 4298, 1), -- Race
(52, 4278, 1), -- Dark Attack
(52, 4333, 3), -- Resist Dark Attack
(52, 4152, 2), -- NPC HP Drain - Magic
(52, 4160, 2), -- NPC Aura Burn - Magic
-- Ol Mahum Patrol
(53, 4295, 1), -- Race
-- Ruin Spartoi
(54, 4290, 1), -- Race
(54, 4275, 3), -- Sacred Attack Weak Point
(54, 4278, 1), -- Dark Attack
(54, 4274, 1), -- Blunt Attack Weak Point
-- Wandering Eye
(55, 4291, 1), -- Race
(55, 4281, 2), -- Wind Attack Weak Point
(55, 4276, 1), -- Archery Attack Weak Point
(55, 4151, 2), -- NPC Windstrike - Magic
(55, 4160, 2), -- NPC Aura Burn - Magic
-- Dre Vanul Disposer
(56, 4298, 1), -- Race
(56, 4278, 1), -- Dark Attack
(56, 4333, 3), -- Resist Dark Attack
(56, 4001, 2), -- NPC Windstrike
(56, 4002, 2), -- NPC HP Drain
(56, 4104, 2), -- Flame
-- Lesser Succubus Tilfo
(57, 4298, 1), -- Race
(57, 4278, 1), -- Dark Attack
(57, 4333, 3), -- Resist Dark Attack
(57, 4047, 2), -- Hold
-- Ol Mahum Guard
(58, 4295, 1), -- Race
-- Hungry Eye
(59, 4291, 1), -- Race
(59, 4281, 2), -- Wind Attack Weak Point
(59, 4276, 1), -- Archery Attack Weak Point
(59, 4152, 2), -- NPC HP Drain - Magic
(59, 4160, 2), -- NPC Aura Burn - Magic
-- Raging Spartoi
(60, 4290, 1), -- Race
(60, 4275, 3), -- Sacred Attack Weak Point
(60, 4278, 1), -- Dark Attack
(60, 4274, 1), -- Blunt Attack Weak Point
-- Ol Mahum Straggler
(61, 4295, 1), -- Race
-- Tumran Bugbear
(62, 4295, 1), -- Race
-- Ol Mahum Shooter
(63, 4295, 1), -- Race
-- Tumran Bugbear Warrior
(64, 4295, 1), -- Race
-- Ol Mahum Patrolman
(65, 4295, 1), -- Race
-- Ol Mahum Captain
(66, 4295, 1), -- Race
(66, 4031, 2), -- Enhance P. Def.
-- Monster Eye Watcher
(67, 4291, 1), -- Race
(67, 4281, 2), -- Wind Attack Weak Point
(67, 4276, 1), -- Archery Attack Weak Point
(67, 4158, 2), -- NPC Prominence - Magic
(67, 4160, 2), -- NPC Aura Burn - Magic
(67, 4076, 2), -- Reduction in movement speed
-- Monster Eye Destroyer
(68, 4291, 1), -- Race
(68, 4281, 2), -- Wind Attack Weak Point
(68, 4276, 1), -- Archery Attack Weak Point
(68, 4157, 2), -- NPC Blaze - Magic
(68, 4160, 2), -- NPC Aura Burn - Magic
(68, 4076, 2), -- Reduction in movement speed
-- Selu Lizardman Scout
(69, 4295, 1), -- Race
-- Lesser Basilisk
(70, 4292, 1), -- Race
-- Selu Lizardman Warrior
(71, 4295, 1), -- Race
-- Basilisk
(72, 4292, 1), -- Race
-- Ol Mahum Legionnaire
(73, 4295, 1), -- Race
-- Androscorpio
(74, 4292, 1), -- Race
(74, 4279, 2), -- Fire Attack Weak Point
-- Ant Larva
(75, 4301, 1), -- Race
(75, 4279, 2), -- Fire Attack Weak Point
(75, 4035, 2), -- Poison
-- Ol Mahum Commander
(76, 4295, 1), -- Race
(76, 4030, 2), -- Enhance P. Atk.
-- Androscorpio Hunter
(77, 4292, 1), -- Race
(77, 4279, 2), -- Fire Attack Weak Point
-- Whispering Wind
(78, 4296, 1), -- Race
(78, 4011, 3), -- Resist Wind
(78, 4282, 2), -- Earth Attack Weak Point
(78, 4046, 3), -- Sleep
-- Ant
(79, 4301, 1), -- Race
(79, 4279, 2), -- Fire Attack Weak Point
-- Ant Captain
(80, 4301, 1), -- Race
(80, 4279, 2), -- Fire Attack Weak Point
-- Ant Overseer
(81, 4301, 1), -- Race
(81, 4303, 1), -- Strong Type
(81, 4279, 2), -- Fire Attack Weak Point
(81, 4030, 2), -- Enhance P. Atk.
-- Ant Recruit
(82, 4301, 1), -- Race
(82, 4279, 2), -- Fire Attack Weak Point
(82, 4035, 3), -- Poison
-- Granite Golem
(83, 4291, 1), -- Race
(83, 4071, 4), -- Resist Archery
(83, 4273, 2), -- Resist Dagger
(83, 4274, 1), -- Blunt Attack Weak Point
(83, 4116, 8), -- Resist M. Atk.
(83, 4284, 3), -- Resist Bleeding
(83, 4247, 3), -- NPC Windstrike - Slow
-- Ant Patrol
(84, 4301, 1), -- Race
(84, 4279, 2), -- Fire Attack Weak Point
(84, 4067, 3), -- NPC Mortal Blow
-- Puncher
(85, 4291, 1), -- Race
(85, 4071, 4), -- Resist Archery
(85, 4273, 2), -- Resist Dagger
(85, 4274, 1), -- Blunt Attack Weak Point
(85, 4116, 8), -- Resist M. Atk.
(85, 4284, 3), -- Resist Bleeding
(85, 4250, 3), -- NPC Twister - Slow
-- Ant Guard
(86, 4301, 1), -- Race
(86, 4279, 2), -- Fire Attack Weak Point
(86, 4317, 1), -- Increase Rage Might
-- Ant Soldier
(87, 4301, 1), -- Race
(87, 4303, 1), -- Strong Type
(87, 4279, 2), -- Fire Attack Weak Point
(87, 4035, 3), -- Poison
-- Ant Warrior Captain
(88, 4301, 1), -- Race
(88, 4303, 1), -- Strong Type
(88, 4279, 2), -- Fire Attack Weak Point
(88, 4074, 2), -- NPC Haste
-- Noble Ant
(89, 4301, 1), -- Race
(89, 4279, 2), -- Fire Attack Weak Point
-- Noble Ant Leader
(90, 4301, 1), -- Race
(90, 4303, 1), -- Strong Type
(90, 4279, 2), -- Fire Attack Weak Point
(90, 4032, 3), -- NPC Strike
-- Young Fox
(91, 4293, 1), -- Race
-- Monster Eye
(92, 4291, 1), -- Race
(92, 4281, 2), -- Wind Attack Weak Point
(92, 4276, 1), -- Archery Attack Weak Point
(92, 4151, 1), -- NPC Windstrike - Magic
(92, 4160, 1), -- NPC Aura Burn - Magic
-- Orc Fighter
(93, 4295, 1), -- Race
-- Orc Marksman
(94, 4295, 1), -- Race
-- Vampire Bat
(95, 4292, 1), -- Race
(95, 4281, 2), -- Wind Attack Weak Point
(95, 4276, 1), -- Archery Attack Weak Point
-- Orc Lieutenant
(96, 4295, 1), -- Race
-- Drill Bat
(97, 4292, 1), -- Race
(97, 4281, 2), -- Wind Attack Weak Point
(97, 4276, 1), -- Archery Attack Weak Point
-- Orc Fighter Leader
(98, 4295, 1), -- Race
(98, 4028, 1), -- Enhance P. Atk.
-- Skeleton
(99, 4290, 1), -- Race
(99, 4275, 3), -- Sacred Attack Weak Point
(99, 4278, 1), -- Dark Attack
(99, 4274, 1), -- Blunt Attack Weak Point
-- Skeleton Archer
(100, 4290, 1), -- Race
(100, 4275, 3), -- Sacred Attack Weak Point
(100, 4278, 1), -- Dark Attack
(100, 4274, 1), -- Blunt Attack Weak Point
-- Crasher
(101, 4291, 1), -- Race
(101, 4071, 4), -- Resist Archery
(101, 4273, 2), -- Resist Dagger
(101, 4274, 1), -- Blunt Attack Weak Point
(101, 4116, 8), -- Resist M. Atk.
(101, 4284, 3), -- Resist Bleeding
(101, 4247, 1), -- NPC Windstrike - Slow
-- Skeleton Marksman
(102, 4290, 1), -- Race
(102, 4275, 3), -- Sacred Attack Weak Point
(102, 4278, 1), -- Dark Attack
(102, 4274, 1), -- Blunt Attack Weak Point
-- Giant Spider
(103, 4301, 1), -- Race
(103, 4279, 2), -- Fire Attack Weak Point
-- Skeleton Lord
(104, 4290, 1), -- Race
(104, 4275, 3), -- Sacred Attack Weak Point
(104, 4278, 1), -- Dark Attack
(104, 4274, 1), -- Blunt Attack Weak Point
-- Dark Horror
(105, 4290, 1), -- Race
(105, 4275, 3), -- Sacred Attack Weak Point
(105, 4278, 1), -- Dark Attack
(105, 4047, 1), -- Hold
-- Talon Spider
(106, 4301, 1), -- Race
(106, 4279, 2), -- Fire Attack Weak Point
-- Wererat
(107, 4295, 1), -- Race
-- Blade Spider
(108, 4301, 1), -- Race
(108, 4279, 2), -- Fire Attack Weak Point
-- Salamander
(109, 4296, 1), -- Race
(109, 4303, 1), -- Strong Type
(109, 4009, 3), -- Resist Fire
(109, 4280, 2), -- Water Attack Weak Point
(109, 4100, 1), -- NPC Prominence
(109, 4001, 1), -- NPC Windstrike
(109, 4104, 1), -- Flame
-- Undine
(110, 4296, 1), -- Race
(110, 4303, 1), -- Strong Type
(110, 4010, 3), -- Resist Water
(110, 4279, 2), -- Fire Attack Weak Point
(110, 4001, 1), -- NPC Windstrike
-- Wererat Leader
(111, 4295, 1), -- Race
(111, 4303, 1), -- Strong Type
-- Salamander Elder
(112, 4296, 1), -- Race
(112, 4303, 1), -- Strong Type
(112, 4009, 3), -- Resist Fire
(112, 4280, 2), -- Water Attack Weak Point
-- Undine Elder
(113, 4296, 1), -- Race
(113, 4303, 1), -- Strong Type
(113, 4010, 3), -- Resist Water
(113, 4279, 2), -- Fire Attack Weak Point
(113, 4001, 1), -- NPC Windstrike
-- Salamander Noble
(114, 4296, 1), -- Race
(114, 4303, 1), -- Strong Type
(114, 4009, 3), -- Resist Fire
(114, 4280, 2), -- Water Attack Weak Point
-- Undine Noble
(115, 4296, 1), -- Race
(115, 4303, 1), -- Strong Type
(115, 4010, 3), -- Resist Water
(115, 4279, 2), -- Fire Attack Weak Point
(115, 4001, 1), -- NPC Windstrike
-- Fox
(116, 4293, 1), -- Race
-- Dre Vanul
(117, 4298, 1), -- Race
(117, 4278, 1), -- Dark Attack
(117, 4333, 3), -- Resist Dark Attack
(117, 4001, 2), -- NPC Windstrike
(117, 4100, 2), -- NPC Prominence
(117, 4104, 2), -- Flame
-- Dre Vanul Scout
(118, 4298, 1), -- Race
(118, 4278, 1), -- Dark Attack
(118, 4333, 3), -- Resist Dark Attack
(118, 4100, 2), -- NPC Prominence
(118, 4002, 2), -- NPC HP Drain
(118, 4038, 3), -- Decrease Atk. Spd.
-- Elder Fox
(119, 4293, 1), -- Race
-- Wolf
(120, 4293, 1), -- Race
-- Giant Toad
(121, 4292, 1), -- Race
-- Arachne
(122, 4301, 1), -- Race
(122, 4279, 2), -- Fire Attack Weak Point
-- Bloody Pixy
(123, 4301, 1), -- Race
-- Dark Pan
(124, 4301, 1), -- Race
-- Pan
(125, 4302, 1), -- Race
-- Pixy
(126, 4302, 1), -- Race
-- Red Arachne
(127, 4301, 1), -- Race
(127, 4279, 2), -- Fire Attack Weak Point
-- Blight Treant
(128, 4296, 1), -- Race
(128, 4279, 2), -- Fire Attack Weak Point
(128, 4277, 3), -- Resist Poison
-- Treant
(129, 4296, 1), -- Race
(129, 4279, 2), -- Fire Attack Weak Point
(129, 4277, 3), -- Resist Poison
-- Orc
(130, 4295, 1), -- Race
-- Orc Grunt
(131, 4295, 1), -- Race
-- Werewolf
(132, 4295, 1), -- Race
-- Bugbear
(133, 4295, 1), -- Race
-- Cave Maiden
(134, 4292, 1), -- Race
(134, 4047, 5), -- Hold
-- Alligator
(135, 4292, 1), -- Race
(135, 4071, 3), -- Resist Archery
-- Death Knight
(136, 4290, 1), -- Race
(136, 4303, 1), -- Strong Type
(136, 4275, 3), -- Sacred Attack Weak Point
(136, 4278, 1), -- Dark Attack
(136, 4084, 8), -- Resist Physical Attack
(136, 4072, 5), -- Shock
(136, 4091, 1), -- NPC Ogre Stun
(136, 4067, 5), -- NPC Mortal Blow
-- Drake
(137, 4299, 1), -- Race
(137, 4071, 3), -- Resist Archery
(137, 4078, 5), -- NPC Flamestrike
-- Gargoyle
(138, 4291, 1), -- Race
(138, 4071, 4), -- Resist Archery
(138, 4273, 2), -- Resist Dagger
(138, 4274, 1), -- Blunt Attack Weak Point
-- Giant Bee
(139, 4301, 1), -- Race
-- Giant Leech
(140, 4301, 1), -- Race
(140, 4279, 2), -- Fire Attack Weak Point
(140, 4071, 3), -- Resist Archery
-- Giant Wild Hog
(141, 4293, 1), -- Race
-- Griffon
(142, 4292, 1), -- Race
-- Grizzly
(143, 4293, 1), -- Race
-- Hangman Tree
(144, 4294, 1), -- Race
(144, 4275, 3), -- Sacred Attack Weak Point
(144, 4278, 1), -- Dark Attack
(144, 4279, 2), -- Fire Attack Weak Point
(144, 4277, 3), -- Resist Poison
(144, 4047, 3), -- Hold
-- Harpy
(145, 4292, 1), -- Race
(145, 4281, 2), -- Wind Attack Weak Point
(145, 4276, 1), -- Archery Attack Weak Point
(145, 4076, 2), -- Reduction in movement speed
-- Headless Knight
(146, 4290, 1), -- Race
(146, 4275, 3), -- Sacred Attack Weak Point
(146, 4278, 1), -- Dark Attack
(146, 4078, 5), -- NPC Flamestrike
-- Hobgoblin
(147, 4295, 1), -- Race
-- Hungry Spirit
(148, 4290, 1), -- Race
(148, 4275, 3), -- Sacred Attack Weak Point
(148, 4278, 1), -- Dark Attack
-- Iron Golem
(149, 4291, 1), -- Race
(149, 4071, 4), -- Resist Archery
(149, 4273, 2), -- Resist Dagger
(149, 4274, 1), -- Blunt Attack Weak Point
(149, 4116, 8), -- Resist M. Atk.
(149, 4284, 3), -- Resist Bleeding
(149, 4249, 3), -- Decrease Speed
-- King Bugbear
(150, 4295, 1), -- Race
-- Kobold
(151, 4295, 1), -- Race
-- Lizardman
(152, 4295, 1), -- Race
-- Magical Weaver
(153, 4291, 1), -- Race
(153, 4281, 2), -- Wind Attack Weak Point
(153, 4276, 1), -- Archery Attack Weak Point
(153, 4039, 1), -- NPC MP Drain
-- Mandragora Sprout
(154, 4294, 1), -- Race
(154, 4279, 2), -- Fire Attack Weak Point
(154, 4277, 3), -- Resist Poison
(154, 4001, 2), -- NPC Windstrike
(154, 4002, 2), -- NPC HP Drain
(154, 4076, 2), -- Reduction in movement speed
-- Mandragora Sapling
(155, 4294, 1), -- Race
(155, 4279, 2), -- Fire Attack Weak Point
(155, 4277, 3), -- Resist Poison
-- Mandragora Blossom
(156, 4294, 1), -- Race
(156, 4279, 2), -- Fire Attack Weak Point
(156, 4277, 3), -- Resist Poison
-- Marsh Stakato
(157, 4301, 1), -- Race
(157, 4279, 2), -- Fire Attack Weak Point
(157, 4032, 2), -- NPC Strike
-- Medusa
(158, 4292, 1), -- Race
(158, 4320, 3), -- Poison
-- Minotaur
(159, 4292, 1), -- Race
-- Neer Crawler
(160, 4290, 1), -- Race
(160, 4275, 3), -- Sacred Attack Weak Point
(160, 4278, 1), -- Dark Attack
(160, 4001, 2), -- NPC Windstrike
(160, 4002, 2), -- NPC HP Drain
(160, 4037, 1), -- Weaken P. Atk.
-- Oel Mahum
(161, 4295, 1), -- Race
-- Ogre
(162, 4295, 1), -- Race
-- Ol Mahum
(163, 4295, 1), -- Race
-- Ol Mahum Archer
(164, 4295, 1), -- Race
-- Ol Mahum Champion
(165, 4295, 1), -- Race
(165, 4032, 2), -- NPC Strike
-- Succubus
(166, 4298, 1), -- Race
(166, 4278, 1), -- Dark Attack
(166, 4333, 3), -- Resist Dark Attack
-- Serpent Slave
(167, 4292, 1), -- Race
-- Silenos
(168, 4295, 1), -- Race
-- Skeleton Royal Guard
(169, 4290, 1), -- Race
(169, 4275, 3), -- Sacred Attack Weak Point
(169, 4278, 1), -- Dark Attack
(169, 4274, 1), -- Blunt Attack Weak Point
-- Spartoi
(170, 4290, 1), -- Race
(170, 4275, 3), -- Sacred Attack Weak Point
(170, 4278, 1), -- Dark Attack
(170, 4274, 1), -- Blunt Attack Weak Point
-- Specter
(171, 4290, 1), -- Race
(171, 4275, 3), -- Sacred Attack Weak Point
(171, 4278, 1), -- Dark Attack
(171, 4047, 2), -- Hold
-- Sylph
(172, 4296, 1), -- Race
(172, 4011, 3), -- Resist Wind
(172, 4282, 2), -- Earth Attack Weak Point
(172, 4001, 1), -- NPC Windstrike
-- Troll
(173, 4292, 1), -- Race
-- Two-Headed Giant
(174, 4295, 1), -- Race
-- Walking Fungus
(175, 4294, 1), -- Race
(175, 4279, 2), -- Fire Attack Weak Point
(175, 4277, 3), -- Resist Poison
-- Wyrm
(176, 4299, 1), -- Race
(176, 4071, 3), -- Resist Archery
(176, 4087, 3), -- NPC Blaze
-- Zombie
(177, 4290, 1), -- Race
(177, 4275, 3), -- Sacred Attack Weak Point
(177, 4278, 1), -- Dark Attack
(177, 4002, 1), -- NPC HP Drain
-- Skeleton Knight
(178, 4292, 1), -- Race
-- Lichking Nassen
(179, 4292, 1), -- Race
-- Greater Stone Golem
(180, 4292, 1), -- Race
-- Grace Unicorn
(181, 4292, 1), -- Race
-- Grace Dryad
(182, 4292, 1), -- Race
-- Hellfire Salamander
(183, 4292, 1), -- Race
-- Clearwind Sylph
(184, 4292, 1), -- Race
-- Maelstrom Undine
(185, 4292, 1), -- Race
-- Greater Dre Vanul
(186, 4292, 1), -- Race
-- Voiceless Knight
(187, 4292, 1), -- Race
-- Sting Spider
(188, 4292, 1), -- Race
-- Puma
(189, 4292, 1), -- Race
-- Skeleton Marauder
(190, 4290, 1), -- Race
(190, 4275, 3), -- Sacred Attack Weak Point
(190, 4278, 1), -- Dark Attack
(190, 4274, 1), -- Blunt Attack Weak Point
-- Skeleton Raider
(191, 4290, 1), -- Race
(191, 4275, 3), -- Sacred Attack Weak Point
(191, 4278, 1), -- Dark Attack
(191, 4274, 1), -- Blunt Attack Weak Point
-- Tyrant
(192, 4292, 1), -- Race
(192, 4279, 2), -- Fire Attack Weak Point
-- Tyrant Kingpin
(193, 4292, 1), -- Race
(193, 4279, 2), -- Fire Attack Weak Point
-- Queen Undine
(194, 4296, 1), -- Race
(194, 4010, 3), -- Resist Water
(194, 4279, 2), -- Fire Attack Weak Point
(194, 4001, 5), -- NPC Windstrike
-- Pixy Murika
(195, 4296, 1), -- Race
-- Treant Bremec
(196, 4296, 1), -- Race
-- Sorrow Maiden
(197, 4296, 1), -- Race
(197, 4151, 2), -- NPC Windstrike - Magic
(197, 4160, 2), -- NPC Aura Burn - Magic
-- Neer Ghoul Berserker
(198, 4290, 1), -- Race
(198, 4275, 3), -- Sacred Attack Weak Point
(198, 4278, 1), -- Dark Attack
(198, 4274, 1), -- Blunt Attack Weak Point
(198, 4001, 2), -- NPC Windstrike
(198, 4002, 2), -- NPC HP Drain
(198, 4038, 3), -- Decrease Atk. Spd.
-- Amber Basilisk
(199, 4292, 1), -- Race
(199, 4073, 3), -- Shock
-- Strain
(200, 4290, 1), -- Race
(200, 4275, 3), -- Sacred Attack Weak Point
(200, 4278, 1), -- Dark Attack
-- Ghoul
(201, 4290, 1), -- Race
(201, 4275, 3), -- Sacred Attack Weak Point
(201, 4278, 1), -- Dark Attack
(201, 4071, 3), -- Resist Archery
(201, 4116, 8), -- Resist M. Atk.
(201, 4284, 3), -- Resist Bleeding
(201, 4248, 3), -- NPC HP Drain - Slow
-- Dead Seeker
(202, 4292, 1), -- Race
(202, 4001, 3), -- NPC Windstrike
(202, 4002, 3), -- NPC HP Drain
(202, 4076, 2), -- Reduction in movement speed
-- Dion Grizzly
(203, 4293, 1), -- Race
-- Bloody Bee
(204, 4301, 1), -- Race
(204, 4279, 2), -- Fire Attack Weak Point
(204, 4035, 2), -- Poison
-- Dire Wolf
(205, 4293, 1), -- Race
-- Kadif Werewolf
(206, 4295, 1), -- Race
-- Ol Mahum Guerilla
(207, 4295, 1), -- Race
(207, 4303, 1), -- Strong Type
-- Ol Mahum Raider
(208, 4295, 1), -- Race
(208, 4303, 1), -- Strong Type
-- Ol Mahum Marksman
(209, 4295, 1), -- Race
(209, 4303, 1), -- Strong Type
-- Ol Mahum Sergeant
(210, 4295, 1), -- Race
(210, 4303, 1), -- Strong Type
-- Ol Mahum Captain
(211, 4295, 1), -- Race
(211, 4303, 1), -- Strong Type
-- Bloody Axe Turmak
(212, 4295, 1), -- Race
(212, 4303, 1), -- Strong Type
(212, 4033, 3), -- NPC Burn
-- Porta
(213, 4291, 1), -- Race
(213, 4303, 1), -- Strong Type
(213, 4274, 1), -- Blunt Attack Weak Point
(213, 4084, 6), -- Resist Physical Attack
(213, 4161, 1), -- Summon PC
(213, 4073, 4), -- Shock
-- Excuro
(214, 4291, 1), -- Race
(214, 4303, 1), -- Strong Type
(214, 4093, 1), -- Evasion
(214, 4087, 4), -- NPC Blaze
(214, 4094, 4), -- NPC Cancel Magic
(214, 4046, 4), -- Sleep
-- Mordeo
(215, 4292, 1), -- Race
(215, 4303, 1), -- Strong Type
(215, 4116, 4), -- Resist M. Atk.
(215, 4077, 4), -- NPC Aura Burn
-- Ricenseo
(216, 4291, 1), -- Race
(216, 4303, 1), -- Strong Type
(216, 4281, 2), -- Wind Attack Weak Point
(216, 4276, 1), -- Archery Attack Weak Point
(216, 4157, 4), -- NPC Blaze - Magic
(216, 4160, 4), -- NPC Aura Burn - Magic
(216, 4076, 3), -- Reduction in movement speed
-- Krator
(217, 4291, 1), -- Race
(217, 4303, 1), -- Strong Type
(217, 4084, 6), -- Resist Physical Attack
(217, 4284, 3), -- Resist Bleeding
(217, 4274, 1), -- Blunt Attack Weak Point
(217, 4247, 4), -- NPC Windstrike - Slow
-- Premo
(218, 4292, 1), -- Race
(218, 4303, 1), -- Strong Type
(218, 4084, 4), -- Resist Physical Attack
(218, 4073, 4), -- Shock
-- Validus
(219, 4292, 1), -- Race
(219, 4303, 1), -- Strong Type
(219, 4071, 4), -- Resist Archery
(219, 4273, 2), -- Resist Dagger
(219, 4274, 1), -- Blunt Attack Weak Point
(219, 4098, 4), -- Magic Skill Block
(219, 4046, 4), -- Sleep
(219, 4047, 4), -- Hold
(219, 4069, 4), -- NPC Curve Beam Cannon
(219, 4094, 4), -- NPC Cancel Magic
-- Dicor
(220, 4294, 1), -- Race
(220, 4303, 1), -- Strong Type
(220, 4275, 3), -- Sacred Attack Weak Point
(220, 4278, 1), -- Dark Attack
(220, 4116, 4), -- Resist M. Atk.
(220, 4160, 4), -- NPC Aura Burn - Magic
(220, 4155, 4), -- NPC Twister - Magic
(220, 4098, 4), -- Magic Skill Block
-- Perum
(221, 4291, 1), -- Race
(221, 4303, 1), -- Strong Type
(221, 4274, 1), -- Blunt Attack Weak Point
(221, 4084, 6), -- Resist Physical Attack
(221, 4161, 1), -- Summon PC
(221, 4073, 4), -- Shock
-- Torfe
(222, 4292, 1), -- Race
(222, 4303, 1), -- Strong Type
(222, 4071, 4), -- Resist Archery
(222, 4273, 2), -- Resist Dagger
(222, 4274, 1), -- Blunt Attack Weak Point
(222, 4116, 4), -- Resist M. Atk.
(222, 4002, 4), -- NPC HP Drain
(222, 4098, 4), -- Magic Skill Block
(222, 4047, 4), -- Hold
-- Mandragora Sprout
(223, 4294, 1), -- Race
(223, 4279, 2), -- Fire Attack Weak Point
(223, 4277, 3), -- Resist Poison
(223, 4001, 2), -- NPC Windstrike
(223, 4002, 2), -- NPC HP Drain
(223, 4104, 2), -- Flame
-- Ol Mahum Ranger
(224, 4295, 1), -- Race
(224, 4040, 2), -- NPC Bow Attack
-- Giant Mist Leech
(225, 4301, 1), -- Race
(225, 4279, 2), -- Fire Attack Weak Point
(225, 4071, 3), -- Resist Archery
(225, 4002, 2), -- NPC HP Drain
(225, 4039, 2), -- NPC MP Drain
(225, 4038, 3), -- Decrease Atk. Spd.
-- Gray Ant
(226, 4301, 1), -- Race
(226, 4279, 2), -- Fire Attack Weak Point
-- Horror Mist Ripper
(227, 4290, 1), -- Race
(227, 4275, 3), -- Sacred Attack Weak Point
(227, 4278, 1), -- Dark Attack
(227, 4002, 2), -- NPC HP Drain
-- Giant Crimson Ant
(228, 4301, 1), -- Race
(228, 4279, 2), -- Fire Attack Weak Point
(228, 4035, 2), -- Poison
-- Stinger Wasp
(229, 4301, 1), -- Race
(229, 4279, 2), -- Fire Attack Weak Point
(229, 4035, 3), -- Poison
-- Marsh Stakato Worker
(230, 4301, 1), -- Race
(230, 4279, 2), -- Fire Attack Weak Point
(230, 4032, 3), -- NPC Strike
-- Toad Lord
(231, 4292, 1), -- Race
(231, 4035, 3), -- Poison
-- Marsh Stakato Soldier
(232, 4301, 1), -- Race
(232, 4279, 2), -- Fire Attack Weak Point
-- Marsh Spider
(233, 4301, 1), -- Race
(233, 4279, 2), -- Fire Attack Weak Point
(233, 4035, 3), -- Poison
-- Marsh Stakato Drone
(234, 4301, 1), -- Race
(234, 4279, 2), -- Fire Attack Weak Point
(234, 4028, 2), -- Enhance P. Atk.
-- Shackle
(235, 4290, 1), -- Race
(235, 4275, 3), -- Sacred Attack Weak Point
(235, 4278, 1), -- Dark Attack
(235, 4002, 4), -- NPC HP Drain
-- Cave Servant
(236, 4290, 1), -- Race
(236, 4275, 3), -- Sacred Attack Weak Point
(236, 4278, 1), -- Dark Attack
(236, 4274, 1), -- Blunt Attack Weak Point
-- Cave Servant Archer
(237, 4290, 1), -- Race
(237, 4275, 3), -- Sacred Attack Weak Point
(237, 4278, 1), -- Dark Attack
(237, 4274, 1), -- Blunt Attack Weak Point
(237, 4040, 4), -- NPC Bow Attack
-- Cave Servant Warrior
(238, 4290, 1), -- Race
(238, 4275, 3), -- Sacred Attack Weak Point
(238, 4278, 1), -- Dark Attack
(238, 4274, 1), -- Blunt Attack Weak Point
(238, 4075, 4), -- Shock
-- Cave Servant Captain
(239, 4290, 1), -- Race
(239, 4275, 3), -- Sacred Attack Weak Point
(239, 4278, 1), -- Dark Attack
(239, 4274, 1), -- Blunt Attack Weak Point
(239, 4075, 4), -- Shock
-- Royal Cave Servant
(240, 4290, 1), -- Race
(240, 4275, 3), -- Sacred Attack Weak Point
(240, 4278, 1), -- Dark Attack
(240, 4274, 1), -- Blunt Attack Weak Point
(240, 4067, 5), -- NPC Mortal Blow
-- Hunter Gargoyle
(241, 4291, 1), -- Race
(241, 4071, 4), -- Resist Archery
(241, 4273, 2), -- Resist Dagger
(241, 4274, 1), -- Blunt Attack Weak Point
(241, 4073, 5), -- Shock
-- Dustwind Gargoyle
(242, 4291, 1), -- Race
(242, 4071, 4), -- Resist Archery
(242, 4273, 2), -- Resist Dagger
(242, 4274, 1), -- Blunt Attack Weak Point
(242, 4073, 5), -- Shock
-- Thunder Wyrm
(243, 4299, 1), -- Race
(243, 4071, 3), -- Resist Archery
-- Malruk Succubus
(244, 4298, 1), -- Race
(244, 4278, 1), -- Dark Attack
(244, 4333, 3), -- Resist Dark Attack
(244, 4039, 5), -- NPC MP Drain
-- Malruk Succubus Turen
(245, 4298, 1), -- Race
(245, 4278, 1), -- Dark Attack
(245, 4333, 3), -- Resist Dark Attack
(245, 4034, 5), -- Decrease Speed
-- Cave Keeper
(246, 4292, 1), -- Race
(246, 4032, 5), -- NPC Strike
-- Bloody Queen
(247, 4298, 1), -- Race
-- Turak Bugbear
(248, 4295, 1), -- Race
(248, 4317, 1), -- Increase Rage Might
-- Turak Bugbear Warrior
(249, 4295, 1), -- Race
(249, 4303, 1), -- Strong Type
-- Glass Jaguar
(250, 4292, 1), -- Race
-- Delu Lizardman
(251, 4295, 1), -- Race
-- Delu Lizardman Scout
(252, 4295, 1), -- Race
-- Delu Lizardman Warrior
(253, 4295, 1), -- Race
-- Skeleton Axeman
(254, 4290, 1), -- Race
(254, 4275, 3), -- Sacred Attack Weak Point
(254, 4278, 1), -- Dark Attack
(254, 4274, 1), -- Blunt Attack Weak Point
-- Batur Orc
(255, 4295, 1), -- Race
-- Batur Orc Archer
(256, 4295, 1), -- Race
(256, 4040, 3), -- NPC Bow Attack
-- Batur Orc Warrior
(257, 4295, 1), -- Race
(257, 4032, 3), -- NPC Strike
-- Batur Orc Shaman
(258, 4295, 1), -- Race
(258, 4152, 3), -- NPC HP Drain - Magic
(258, 4160, 3), -- NPC Aura Burn - Magic
(258, 4076, 2), -- Reduction in movement speed
-- Batur Orc Overlord
(259, 4295, 1), -- Race
(259, 4032, 3), -- NPC Strike
-- Orc Champion
(260, 4295, 1), -- Race
(260, 4028, 1), -- Enhance P. Atk.
-- Orc Shaman
(261, 4295, 1), -- Race
(261, 4037, 1), -- Weaken P. Atk.
-- Out of Use
(262, 4295, 1), -- Race
-- Ol Mahum Novice
(263, 4295, 1), -- Race
-- Dark Succubus
(264, 4298, 1), -- Race
(264, 4278, 1), -- Dark Attack
(264, 4333, 3), -- Resist Dark Attack
(264, 4035, 2), -- Poison
-- Monster Eye Searcher
(265, 4291, 1), -- Race
(265, 4303, 1), -- Strong Type
(265, 4281, 2), -- Wind Attack Weak Point
(265, 4276, 1), -- Archery Attack Weak Point
(265, 4151, 2), -- NPC Windstrike - Magic
(265, 4160, 2), -- NPC Aura Burn - Magic
-- Monster Eye Gazer
(266, 4291, 1), -- Race
(266, 4281, 2), -- Wind Attack Weak Point
(266, 4276, 1), -- Archery Attack Weak Point
(266, 4155, 2), -- NPC Twister - Magic
(266, 4160, 2), -- NPC Aura Burn - Magic
(266, 4076, 2), -- Reduction in movement speed
-- Breka Orc
(267, 4295, 1), -- Race
(267, 4317, 1), -- Increase Rage Might
-- Breka Orc Archer
(268, 4295, 1), -- Race
(268, 4040, 3), -- NPC Bow Attack
-- Breka Orc Shaman
(269, 4295, 1), -- Race
(269, 4153, 3), -- Decrease Speed
(269, 4160, 3), -- NPC Aura Burn - Magic
-- Breka Orc Overlord
(270, 4295, 1), -- Race
(270, 4032, 3), -- NPC Strike
-- Breka Orc Warrior
(271, 4295, 1), -- Race
(271, 4032, 3), -- NPC Strike
-- Cave Servant
(272, 4290, 1), -- Race
(272, 4275, 3), -- Sacred Attack Weak Point
(272, 4278, 1), -- Dark Attack
(272, 4274, 1), -- Blunt Attack Weak Point
-- Cave Servant Archer
(273, 4290, 1), -- Race
(273, 4275, 3), -- Sacred Attack Weak Point
(273, 4278, 1), -- Dark Attack
(273, 4274, 1), -- Blunt Attack Weak Point
-- Cave Servant Warrior
(274, 4290, 1), -- Race
(274, 4275, 3), -- Sacred Attack Weak Point
(274, 4278, 1), -- Dark Attack
(274, 4274, 1), -- Blunt Attack Weak Point
-- Cave Servant Captain
(275, 4290, 1), -- Race
(275, 4275, 3), -- Sacred Attack Weak Point
(275, 4278, 1), -- Dark Attack
(275, 4274, 1), -- Blunt Attack Weak Point
-- Royal Cave Servant
(276, 4290, 1), -- Race
(276, 4275, 3), -- Sacred Attack Weak Point
(276, 4278, 1), -- Dark Attack
(276, 4274, 1), -- Blunt Attack Weak Point
(276, 4046, 5), -- Sleep
-- Cave Keeper
(277, 4292, 1), -- Race
(277, 4002, 5), -- NPC HP Drain
-- Bloody Queen
(278, 4292, 1), -- Race
-- Shackle
(279, 4290, 1), -- Race
(279, 4275, 3), -- Sacred Attack Weak Point
(279, 4278, 1), -- Dark Attack
(279, 4002, 4), -- NPC HP Drain
-- Headless Knight
(280, 4290, 1), -- Race
(280, 4275, 3), -- Sacred Attack Weak Point
(280, 4278, 1), -- Dark Attack
-- Dustwind Gargoyle
(281, 4291, 1), -- Race
(281, 4071, 4), -- Resist Archery
(281, 4273, 2), -- Resist Dagger
(281, 4274, 1), -- Blunt Attack Weak Point
-- Thunder Wyrm
(282, 4299, 1), -- Race
-- Malruk Succubus
(283, 4298, 1), -- Race
(283, 4278, 1), -- Dark Attack
(283, 4333, 3), -- Resist Dark Attack
(283, 4046, 5), -- Sleep
-- Malruk Succubus Turen
(284, 4298, 1), -- Race
(284, 4278, 1), -- Dark Attack
(284, 4333, 3), -- Resist Dark Attack
(284, 4046, 5), -- Sleep
-- Drake
(285, 4299, 1), -- Race
-- Hunter Gargoyle
(286, 4291, 1), -- Race
(286, 4071, 4), -- Resist Archery
(286, 4273, 2), -- Resist Dagger
(286, 4274, 1), -- Blunt Attack Weak Point
-- Cave Maiden
(287, 4292, 1), -- Race
-- Cat's Eye Bandit
(288, 4295, 1), -- Race
-- Ol Mahum Novice
(289, 4295, 1), -- Race
-- Dark Succubus
(290, 4298, 1), -- Race
(290, 4035, 2), -- Poison
-- Enku Orc Champion
(291, 4295, 1), -- Race
(291, 4032, 2), -- NPC Strike
-- Enku Orc Shaman
(292, 4295, 1), -- Race
(292, 4151, 2), -- NPC Windstrike - Magic
(292, 4160, 2), -- NPC Aura Burn - Magic
(292, 4065, 2), -- NPC Heal
-- Virud Lizardman
(293, 4295, 1), -- Race
-- Virud Lizardman Matriarch
(294, 4295, 1), -- Race
(294, 4033, 2), -- NPC Burn
-- Virud Lizardman Warrior
(295, 4295, 1), -- Race
-- Virud Lizardman Scout
(296, 4295, 1), -- Race
-- Virud Lizardman Shaman 
(297, 4295, 1), -- Race
-- Virud Lizardman Shaman 
(298, 4295, 1), -- Race
-- Zenta Lizardman
(299, 4295, 1), -- Race
-- Zenta Lizardman Matriarch
(300, 4295, 1), -- Race
(300, 4032, 4), -- NPC Strike
-- Zenta Lizardman Scout
(301, 4295, 1), -- Race
-- Zenta Lizardman Warrior
(302, 4295, 1), -- Race
-- Zenta Lizardman Shaman
(303, 4295, 1), -- Race
-- Marsh Stakato Queen
(304, 4301, 1), -- Race
(304, 4279, 2), -- Fire Attack Weak Point
(304, 4032, 3), -- NPC Strike
-- Grave
(305, 4292, 1), -- Race
(305, 4279, 2), -- Fire Attack Weak Point
-- Out of Use
(306, 4291, 1), -- Race
(306, 4071, 4), -- Resist Archery
(306, 4273, 2), -- Resist Dagger
(306, 4274, 1), -- Blunt Attack Weak Point
(306, 4285, 4), -- Resist Sleep
(306, 4287, 4), -- Resist Hold
(306, 4073, 4), -- Shock
-- Garum Werewolf
(307, 4295, 1), -- Race
-- Hook Spider
(308, 4301, 1), -- Race
-- Brown Fox
(309, 4293, 1), -- Race
-- Brown Bear
(310, 4293, 1), -- Race
-- Rakeclaw Imp
(311, 4302, 1), -- Race
(311, 4281, 2), -- Wind Attack Weak Point
(311, 4276, 1), -- Archery Attack Weak Point
-- Rakeclaw Imp Hunter
(312, 4302, 1), -- Race
(312, 4281, 2), -- Wind Attack Weak Point
(312, 4276, 1), -- Archery Attack Weak Point
-- Rakeclaw Imp Chieftain
(313, 4302, 1), -- Race
(313, 4281, 2), -- Wind Attack Weak Point
(313, 4276, 1), -- Archery Attack Weak Point
-- Great White Shark
(314, 4292, 1), -- Race
-- Out of Use
(315, 4292, 1), -- Race
(315, 4033, 3), -- NPC Burn
-- Darkwing Bat
(316, 4292, 1), -- Race
(316, 4281, 2), -- Wind Attack Weak Point
(316, 4276, 1), -- Archery Attack Weak Point
-- Black Wolf
(317, 4293, 1), -- Race
-- Black Timber Wolf
(318, 4293, 1), -- Race
-- Goblin Grave Robber
(319, 4295, 1), -- Race
-- Goblin Tomb Raider Leader
(320, 4295, 1), -- Race
-- Goblin Thief
(321, 4295, 1), -- Race
-- Goblin Brigand
(322, 4295, 1), -- Race
-- Goblin Brigand Leader
(323, 4295, 1), -- Race
-- Goblin Brigand Lieutenant
(324, 4295, 1), -- Race
-- Goblin Raider
(325, 4295, 1), -- Race
-- Goblin Scout
(326, 4295, 1), -- Race
-- Goblin Snooper
(327, 4295, 1), -- Race
-- Goblin Lookout
(328, 4295, 1), -- Race
-- Ghost Guardian
(329, 4290, 1), -- Race
(329, 4275, 3), -- Sacred Attack Weak Point
(329, 4278, 1), -- Dark Attack
-- Mineshaft Bat
(330, 4292, 1), -- Race
(330, 4281, 2), -- Wind Attack Weak Point
(330, 4276, 1), -- Archery Attack Weak Point
-- Monster Eye Tracker
(331, 4291, 1), -- Race
(331, 4281, 2), -- Wind Attack Weak Point
(331, 4276, 1), -- Archery Attack Weak Point
(331, 4151, 1), -- NPC Windstrike - Magic
(331, 4160, 1), -- NPC Aura Burn - Magic
-- Out of Use
(332, 4295, 1), -- Race
(332, 4278, 1), -- Dark Attack
(332, 4333, 3), -- Resist Dark Attack
(332, 4285, 4), -- Resist Sleep
(332, 4287, 4), -- Resist Hold
(332, 4160, 2), -- NPC Aura Burn - Magic
(332, 4153, 2), -- Decrease Speed
(332, 4036, 2), -- Poison
-- Greystone Golem
(333, 4291, 1), -- Race
(333, 4071, 4), -- Resist Archery
(333, 4273, 2), -- Resist Dagger
(333, 4274, 1), -- Blunt Attack Weak Point
(333, 4116, 8), -- Resist M. Atk.
(333, 4284, 3), -- Resist Bleeding
(333, 4254, 1), -- NPC Prominence - Slow
-- Gremlin Scavenger
(334, 4302, 1), -- Race
-- Grizzly Bear
(335, 4293, 1), -- Race
-- Green Dryad
(336, 4296, 1), -- Race
-- Longtail Fox
(337, 4293, 1), -- Race
-- Nightmare Weaver
(338, 4291, 1), -- Race
(338, 4035, 2), -- Poison
-- Out of Use
(339, 4292, 1), -- Race
(339, 4281, 2), -- Wind Attack Weak Point
(339, 4276, 1), -- Archery Attack Weak Point
-- Slave Skeleton
(340, 4290, 1), -- Race
(340, 4275, 3), -- Sacred Attack Weak Point
(340, 4278, 1), -- Dark Attack
(340, 4274, 1), -- Blunt Attack Weak Point
-- Undead Slave
(341, 4290, 1), -- Race
(341, 4275, 3), -- Sacred Attack Weak Point
(341, 4278, 1), -- Dark Attack
(341, 4071, 3), -- Resist Archery
(341, 4116, 8), -- Resist M. Atk.
(341, 4284, 3), -- Resist Bleeding
(341, 4247, 1), -- NPC Windstrike - Slow
-- Werewolf Chieftain
(342, 4295, 1), -- Race
-- Werewolf Hunter
(343, 4295, 1), -- Race
-- Out of Use
(344, 4298, 1), -- Race
(344, 4278, 1), -- Dark Attack
(344, 4333, 3), -- Resist Dark Attack
-- Dark Terror
(345, 4290, 1), -- Race
(345, 4275, 3), -- Sacred Attack Weak Point
(345, 4278, 1), -- Dark Attack
(345, 4035, 1), -- Poison
-- Darkstone Golem
(346, 4291, 1), -- Race
(346, 4071, 4), -- Resist Archery
(346, 4273, 2), -- Resist Dagger
(346, 4274, 1), -- Blunt Attack Weak Point
(346, 4116, 8), -- Resist M. Atk.
(346, 4284, 3), -- Resist Bleeding
(346, 4249, 1), -- Decrease Speed
-- Death Wraith
(347, 4290, 1), -- Race
(347, 4275, 3), -- Sacred Attack Weak Point
(347, 4278, 1), -- Dark Attack
(347, 4001, 5), -- NPC Windstrike
-- Cave Spider
(348, 4301, 1), -- Race
-- Cave Bat
(349, 4292, 1), -- Race
(349, 4281, 2), -- Wind Attack Weak Point
(349, 4276, 1), -- Archery Attack Weak Point
-- Cave Fang Spider
(350, 4301, 1), -- Race
-- Dre Vanul Tracker
(351, 4298, 1), -- Race
(351, 4278, 1), -- Dark Attack
(351, 4333, 3), -- Resist Dark Attack
(351, 4047, 2), -- Hold
-- Dre Vanul Slayer
(352, 4298, 1), -- Race
(352, 4278, 1), -- Dark Attack
(352, 4333, 3), -- Resist Dark Attack
(352, 4100, 2), -- NPC Prominence
(352, 4001, 2), -- NPC Windstrike
(352, 4104, 2), -- Flame
-- Dre Vanul Beholder
(353, 4298, 1), -- Race
(353, 4278, 1), -- Dark Attack
(353, 4333, 3), -- Resist Dark Attack
(353, 4046, 2), -- Sleep
-- Dwarf Ghost
(354, 4290, 1), -- Race
(354, 4275, 3), -- Sacred Attack Weak Point
(354, 4278, 1), -- Dark Attack
(354, 4002, 2), -- NPC HP Drain
-- Out of Use
(355, 4292, 1), -- Race
-- Langk Lizardman Leader
(356, 4295, 1), -- Race
-- Langk Lizardman Lieutenant
(357, 4295, 1), -- Race
-- Ratman Hunter
(358, 4295, 1), -- Race
-- Ratman Warrior
(359, 4295, 1), -- Race
-- Ratman Spy
(360, 4295, 1), -- Race
-- Tunath Orc Marksman
(361, 4295, 1), -- Race
-- Tunath Orc Warrior
(362, 4295, 1), -- Race
-- Maraku Werewolf
(363, 4295, 1), -- Race
-- Maraku Werewolf Chieftain
(364, 4295, 1), -- Race
-- Mountain Fungus
(365, 4294, 1), -- Race
(365, 4279, 2), -- Fire Attack Weak Point
(365, 4277, 3), -- Resist Poison
-- Out of Use
(366, 4292, 1), -- Race
-- Merkenis Escort
(367, 4290, 1), -- Race
(367, 4275, 3), -- Sacred Attack Weak Point
(367, 4278, 1), -- Dark Attack
(367, 4274, 1), -- Blunt Attack Weak Point
-- Grave Keeper
(368, 4291, 1), -- Race
(368, 4071, 4), -- Resist Archery
(368, 4273, 2), -- Resist Dagger
(368, 4274, 1), -- Blunt Attack Weak Point
(368, 4116, 8), -- Resist M. Atk.
(368, 4284, 3), -- Resist Bleeding
(368, 4247, 1), -- NPC Windstrike - Slow
-- Moonstone Beast
(369, 4292, 1), -- Race
-- Barbed Bat
(370, 4292, 1), -- Race
(370, 4281, 2), -- Wind Attack Weak Point
(370, 4276, 1), -- Archery Attack Weak Point
-- Mist Terror
(371, 4290, 1), -- Race
(371, 4275, 3), -- Sacred Attack Weak Point
(371, 4278, 1), -- Dark Attack
-- Baraq Orc Fighter
(372, 4295, 1), -- Race
-- Baraq Orc Warrior Leader
(373, 4295, 1), -- Race
-- Baranka's Guard
(374, 4290, 1), -- Race
(374, 4275, 3), -- Sacred Attack Weak Point
(374, 4278, 1), -- Dark Attack
(374, 4274, 1), -- Blunt Attack Weak Point
(374, 4077, 2), -- NPC Aura Burn
-- Baranka's Escort
(375, 4290, 1), -- Race
(375, 4275, 3), -- Sacred Attack Weak Point
(375, 4278, 1), -- Dark Attack
(375, 4274, 1), -- Blunt Attack Weak Point
-- Varikan Brigand Leader
(376, 4295, 1), -- Race
(376, 4303, 1), -- Strong Type
-- Varika's Bandit
(377, 4295, 1), -- Race
(377, 4311, 1), -- Feeble Type
-- Stone Giant
(378, 4291, 1), -- Race
(378, 4071, 4), -- Resist Archery
(378, 4273, 2), -- Resist Dagger
(378, 4274, 1), -- Blunt Attack Weak Point
(378, 4116, 8), -- Resist M. Atk.
(378, 4284, 3), -- Resist Bleeding
(378, 4247, 1), -- NPC Windstrike - Slow
-- Stone Soldier
(379, 4291, 1), -- Race
(379, 4071, 4), -- Resist Archery
(379, 4273, 2), -- Resist Dagger
(379, 4274, 1), -- Blunt Attack Weak Point
(379, 4116, 8), -- Resist M. Atk.
(379, 4284, 3), -- Resist Bleeding
(379, 4249, 1), -- Decrease Speed
-- Stone Guardian
(380, 4291, 1), -- Race
(380, 4071, 4), -- Resist Archery
(380, 4273, 2), -- Resist Dagger
(380, 4274, 1), -- Blunt Attack Weak Point
(380, 4116, 8), -- Resist M. Atk.
(380, 4284, 3), -- Resist Bleeding
(380, 4247, 1), -- NPC Windstrike - Slow
-- Vaiss Orc
(381, 4295, 1), -- Race
-- Vaiss Orc Warrior Leader
(382, 4295, 1), -- Race
-- Arachnid Hunter
(383, 4290, 1), -- Race
(383, 4275, 3), -- Sacred Attack Weak Point
(383, 4278, 1), -- Dark Attack
(383, 4035, 3), -- Poison
-- Out of Use
(384, 4291, 1), -- Race
(384, 4071, 4), -- Resist Archery
(384, 4273, 2), -- Resist Dagger
(384, 4274, 1), -- Blunt Attack Weak Point
(384, 4116, 8), -- Resist M. Atk.
(384, 4284, 3), -- Resist Bleeding
(384, 4247, 2), -- NPC Windstrike - Slow
-- Balor Orc Archer
(385, 4295, 1), -- Race
-- Balor Orc Fighter
(386, 4295, 1), -- Race
-- Balor Orc Fighter Leader
(387, 4295, 1), -- Race
-- Balor Orc Lieutenant
(388, 4295, 1), -- Race
-- Boogle Ratman
(389, 4295, 1), -- Race
-- Boogle Ratman Leader
(390, 4295, 1), -- Race
-- Red Fox
(391, 4293, 1), -- Race
-- Redeye Bat
(392, 4292, 1), -- Race
(392, 4281, 2), -- Wind Attack Weak Point
(392, 4276, 1), -- Archery Attack Weak Point
-- Red Scavenger Spider
(393, 4301, 1), -- Race
-- Crimson Tarantula
(394, 4301, 1), -- Race
(394, 4279, 2), -- Fire Attack Weak Point
-- Out of Use
(395, 4295, 1), -- Race
-- Out of Use
(396, 4295, 1), -- Race
(396, 4317, 1), -- Increase Rage Might
-- Out of Use
(397, 4295, 1), -- Race
(397, 4032, 2), -- NPC Strike
-- Vrykolakas
(398, 4295, 1), -- Race
(398, 4303, 1), -- Strong Type
(398, 4032, 1), -- NPC Strike
-- Vrykolakas Wolfkin
(399, 4293, 1), -- Race
(399, 4311, 1), -- Feeble Type
-- Blood Fungus
(400, 4294, 1), -- Race
(400, 4279, 2), -- Fire Attack Weak Point
(400, 4277, 3), -- Resist Poison
-- Veelan Bugbear
(401, 4295, 1), -- Race
-- Veelan Bugbear Warrior
(402, 4295, 1), -- Race
-- Hunter Tarantula
(403, 4301, 1), -- Race
-- Silent Horror
(404, 4290, 1), -- Race
(404, 4275, 3), -- Sacred Attack Weak Point
(404, 4278, 1), -- Dark Attack
-- Young Brown Fox
(405, 4293, 1), -- Race
-- Young Red Fox
(406, 4293, 1), -- Race
-- Young Crimson Fox
(407, 4293, 1), -- Race
-- Sharuk's Henchmen
(408, 4295, 1), -- Race
-- Soul Scavenger
(409, 4290, 1), -- Race
(409, 4275, 3), -- Sacred Attack Weak Point
(409, 4278, 1), -- Dark Attack
(409, 4078, 2), -- NPC Flamestrike
-- Scavenger Spider
(410, 4301, 1), -- Race
-- Scavenger Bat
(411, 4292, 1), -- Race
(411, 4281, 2), -- Wind Attack Weak Point
(411, 4276, 1), -- Archery Attack Weak Point
(411, 4001, 1), -- NPC Windstrike
(411, 4002, 1), -- NPC HP Drain
(411, 4038, 2), -- Decrease Atk. Spd.
-- Susceptor
(412, 4291, 1), -- Race
(412, 4001, 4), -- NPC Windstrike
-- Out of Use
(413, 4295, 1), -- Race
-- Sukar Wererat Chief
(414, 4295, 1), -- Race
(414, 4032, 2), -- NPC Strike
-- Scarlet Salamander
(415, 4296, 1), -- Race
(415, 4009, 3), -- Resist Fire
(415, 4280, 2), -- Water Attack Weak Point
(415, 4100, 1), -- NPC Prominence
(415, 4002, 1), -- NPC HP Drain
(415, 4104, 1), -- Flame
-- Scarlet Salamander Elder
(416, 4296, 1), -- Race
(416, 4009, 3), -- Resist Fire
(416, 4280, 2), -- Water Attack Weak Point
(416, 4100, 1), -- NPC Prominence
(416, 4001, 1), -- NPC Windstrike
(416, 4104, 1), -- Flame
-- Sirocco Gargoyle
(417, 4291, 1), -- Race
(417, 4071, 4), -- Resist Archery
(417, 4273, 2), -- Resist Dagger
(417, 4274, 1), -- Blunt Attack Weak Point
(417, 4285, 4), -- Resist Sleep
(417, 4076, 2), -- Reduction in movement speed
-- Crystalline Beast
(418, 4292, 1), -- Race
-- Akaste Succubus
(419, 4298, 1), -- Race
(419, 4278, 1), -- Dark Attack
(419, 4333, 3), -- Resist Dark Attack
(419, 4047, 2), -- Hold
-- Akaste Succubus Turen
(420, 4298, 1), -- Race
(420, 4278, 1), -- Dark Attack
(420, 4333, 3), -- Resist Dark Attack
(420, 4152, 2), -- NPC HP Drain - Magic
(420, 4160, 2), -- NPC Aura Burn - Magic
-- Akaste Succubus Tilfo
(421, 4298, 1), -- Race
(421, 4278, 1), -- Dark Attack
(421, 4333, 3), -- Resist Dark Attack
(421, 4002, 2), -- NPC HP Drain
(421, 4039, 2), -- NPC MP Drain
(421, 4076, 2), -- Reduction in movement speed
-- Akaste Bone Warlord
(422, 4290, 1), -- Race
(422, 4275, 3), -- Sacred Attack Weak Point
(422, 4278, 1), -- Dark Attack
(422, 4274, 1), -- Blunt Attack Weak Point
(422, 4075, 1), -- Shock
-- Akaste Bone Archer
(423, 4290, 1), -- Race
(423, 4275, 3), -- Sacred Attack Weak Point
(423, 4278, 1), -- Dark Attack
(423, 4274, 1), -- Blunt Attack Weak Point
-- Akaste Bone Lord
(424, 4290, 1), -- Race
(424, 4275, 3), -- Sacred Attack Weak Point
(424, 4278, 1), -- Dark Attack
(424, 4274, 1), -- Blunt Attack Weak Point
(424, 4028, 1), -- Enhance P. Atk.
-- Akaste Bone Soldier
(425, 4290, 1), -- Race
(425, 4275, 3), -- Sacred Attack Weak Point
(425, 4278, 1), -- Dark Attack
(425, 4274, 1), -- Blunt Attack Weak Point
-- Evil Eye
(426, 4291, 1), -- Race
(426, 4281, 2), -- Wind Attack Weak Point
(426, 4276, 1), -- Archery Attack Weak Point
(426, 4151, 1), -- NPC Windstrike - Magic
(426, 4160, 1), -- NPC Aura Burn - Magic
-- Evil Eye Watcher
(427, 4291, 1), -- Race
(427, 4281, 2), -- Wind Attack Weak Point
(427, 4276, 1), -- Archery Attack Weak Point
(427, 4152, 1), -- NPC HP Drain - Magic
(427, 4160, 1), -- NPC Aura Burn - Magic
-- Evil Eye Patroller
(428, 4291, 1), -- Race
(428, 4281, 2), -- Wind Attack Weak Point
(428, 4276, 1), -- Archery Attack Weak Point
(428, 4151, 1), -- NPC Windstrike - Magic
(428, 4160, 1), -- NPC Aura Burn - Magic
-- Evil Eye Lookout
(429, 4291, 1), -- Race
(429, 4281, 2), -- Wind Attack Weak Point
(429, 4276, 1), -- Archery Attack Weak Point
(429, 4152, 1), -- NPC HP Drain - Magic
(429, 4160, 1), -- NPC Aura Burn - Magic
-- Abyss Flyer
(430, 4292, 1), -- Race
(430, 4281, 2), -- Wind Attack Weak Point
(430, 4276, 1), -- Archery Attack Weak Point
-- Elf Ghost
(431, 4290, 1), -- Race
(431, 4275, 3), -- Sacred Attack Weak Point
(431, 4278, 1), -- Dark Attack
(431, 4002, 2), -- NPC HP Drain
-- Elpy
(432, 4293, 1), -- Race
-- Festering Bat
(433, 4292, 1), -- Race
(433, 4281, 2), -- Wind Attack Weak Point
(433, 4276, 1), -- Archery Attack Weak Point
-- Yellow Hornet
(434, 4301, 1), -- Race
(434, 4279, 2), -- Fire Attack Weak Point
(434, 4035, 2), -- Poison
-- Opal Beast
(435, 4292, 1), -- Race
-- Ol Mahum Supplier
(436, 4295, 1), -- Race
-- Ol Mahum Recruit
(437, 4295, 1), -- Race
-- Ol Mahum General
(438, 4295, 1), -- Race
-- Ol Mahum Officer
(439, 4295, 1), -- Race
-- Elder Brown Fox
(440, 4293, 1), -- Race
-- Elder Longtail Fox
(441, 4293, 1), -- Race
-- Elder Wolf
(442, 4293, 1), -- Race
-- Elder Red Fox
(443, 4293, 1), -- Race
-- Elder Prairie Fox
(444, 4293, 1), -- Race
-- Uthanka Pirate
(445, 4295, 1), -- Race
(445, 4311, 1), -- Feeble Type
-- Utuku Orc
(446, 4295, 1), -- Race
-- Utuku Orc Archer
(447, 4295, 1), -- Race
-- Utuku Orc Grunt
(448, 4295, 1), -- Race
-- Will-O-Wisp
(449, 4291, 1), -- Race
(449, 4033, 1), -- NPC Burn
-- Relic Werewolf
(450, 4295, 1), -- Race
-- Relic Spartoi
(451, 4290, 1), -- Race
(451, 4303, 1), -- Strong Type
(451, 4275, 3), -- Sacred Attack Weak Point
(451, 4278, 1), -- Dark Attack
(451, 4274, 1), -- Blunt Attack Weak Point
-- Kaysha Herald Of Ikaros
(452, 4298, 1), -- Race
(452, 4278, 1), -- Dark Attack
(452, 4333, 3), -- Resist Dark Attack
-- Human Ghost
(453, 4290, 1), -- Race
(453, 4275, 3), -- Sacred Attack Weak Point
(453, 4278, 1), -- Dark Attack
(453, 4002, 2), -- NPC HP Drain
-- Oblivion Watcher
(454, 4291, 1), -- Race
(454, 4311, 1), -- Feeble Type
(454, 4281, 2), -- Wind Attack Weak Point
(454, 4276, 1), -- Archery Attack Weak Point
-- Doom Soldier
(455, 4290, 1), -- Race
(455, 4275, 3), -- Sacred Attack Weak Point
(455, 4278, 1), -- Dark Attack
(455, 4274, 1), -- Blunt Attack Weak Point
-- Ashen Wolf
(456, 4293, 1), -- Race
-- Zombie Soldier
(457, 4290, 1), -- Race
(457, 4275, 3), -- Sacred Attack Weak Point
(457, 4278, 1), -- Dark Attack
(457, 4071, 3), -- Resist Archery
(457, 4116, 8), -- Resist M. Atk.
(457, 4284, 3), -- Resist Bleeding
(457, 4253, 1), -- NPC Blaze - Slow
-- Zombie Warrior
(458, 4290, 1), -- Race
(458, 4275, 3), -- Sacred Attack Weak Point
(458, 4278, 1), -- Dark Attack
(458, 4071, 3), -- Resist Archery
(458, 4116, 8), -- Resist M. Atk.
(458, 4284, 3), -- Resist Bleeding
(458, 4247, 2), -- NPC Windstrike - Slow
-- Zombie Lord Farakelsus
(459, 4290, 1), -- Race
(459, 4275, 3), -- Sacred Attack Weak Point
(459, 4278, 1), -- Dark Attack
(459, 4071, 3), -- Resist Archery
(459, 4116, 8), -- Resist M. Atk.
(459, 4284, 3), -- Resist Bleeding
(459, 4248, 2), -- NPC HP Drain - Slow
-- Crimson Spider
(460, 4301, 1), -- Race
-- Dungeon Spider
(461, 4301, 1), -- Race
-- Cave Blade Spider
(462, 4301, 1), -- Race
-- Dungeon Skeleton Archer
(463, 4290, 1), -- Race
(463, 4275, 3), -- Sacred Attack Weak Point
(463, 4278, 1), -- Dark Attack
(463, 4274, 1), -- Blunt Attack Weak Point
-- Dungeon Skeleton
(464, 4290, 1), -- Race
(464, 4275, 3), -- Sacred Attack Weak Point
(464, 4278, 1), -- Dark Attack
(464, 4274, 1), -- Blunt Attack Weak Point
-- Prairie Fox
(465, 4293, 1), -- Race
-- Pincer Spider
(466, 4301, 1), -- Race
-- Tracker Sharuk
(467, 4295, 1), -- Race
(467, 4032, 2), -- NPC Strike
-- Kaboo Orc
(468, 4295, 1), -- Race
-- Kaboo Orc Archer
(469, 4295, 1), -- Race
-- Kaboo Orc Grunt
(470, 4295, 1), -- Race
-- Kaboo Orc Fighter
(471, 4295, 1), -- Race
-- Kaboo Orc Fighter Leader
(472, 4295, 1), -- Race
-- Kaboo Orc Fighter Lieutenant
(473, 4295, 1), -- Race
-- Kasha Spider
(474, 4301, 1), -- Race
-- Kasha Wolf
(475, 4293, 1), -- Race
-- Kasha Fang Spider
(476, 4301, 1), -- Race
-- Kasha Timber Wolf
(477, 4293, 1), -- Race
-- Kasha Blade Spider
(478, 4301, 1), -- Race
(478, 4035, 1), -- Poison
-- Kasha Bear
(479, 4293, 1), -- Race
-- Blade Bat
(480, 4292, 1), -- Race
(480, 4281, 2), -- Wind Attack Weak Point
(480, 4276, 1), -- Archery Attack Weak Point
-- Bearded Keltir
(481, 4293, 1), -- Race
-- Corpse Scavenger
(482, 4290, 1), -- Race
(482, 4275, 3), -- Sacred Attack Weak Point
(482, 4278, 1), -- Dark Attack
(482, 4274, 1), -- Blunt Attack Weak Point
-- Corpse Candle
(483, 4291, 1), -- Race
(483, 4033, 1), -- NPC Burn
-- Follower Of Kuroboros
(484, 4295, 1), -- Race
-- Priest Of Kuroboros
(485, 4295, 1), -- Race
(485, 4160, 2), -- NPC Aura Burn - Magic
(485, 4153, 2), -- Decrease Speed
(485, 4097, 2), -- NPC Chant of Life
-- Soldier Of Kuroboros
(486, 4295, 1), -- Race
(486, 4285, 4), -- Resist Sleep
-- Kuruka Ratman
(487, 4295, 1), -- Race
-- Kuruka Ratman Hunter
(488, 4295, 1), -- Race
-- Quicksilver Beast
(489, 4292, 1), -- Race
-- Death Doll
(490, 4290, 1), -- Race
(490, 4275, 3), -- Sacred Attack Weak Point
(490, 4278, 1), -- Dark Attack
(490, 4035, 4), -- Poison
-- Crypt Horror
(491, 4290, 1), -- Race
(491, 4275, 3), -- Sacred Attack Weak Point
(491, 4278, 1), -- Dark Attack
-- Kirunak's Guards
(492, 4290, 1), -- Race
(492, 4275, 3), -- Sacred Attack Weak Point
(492, 4278, 1), -- Dark Attack
(492, 4274, 1), -- Blunt Attack Weak Point
-- Tiger Hornet
(493, 4301, 1), -- Race
(493, 4279, 2), -- Fire Attack Weak Point
(493, 4035, 2), -- Poison
-- Turek War Hound
(494, 4293, 1), -- Race
(494, 4303, 1), -- Strong Type
-- Turek Orc Warlord
(495, 4295, 1), -- Race
(495, 4303, 1), -- Strong Type
-- Turek Orc Archer
(496, 4295, 1), -- Race
(496, 4303, 1), -- Strong Type
-- Turek Orc Skirmisher
(497, 4295, 1), -- Race
(497, 4303, 1), -- Strong Type
-- Turek Orc Supplier
(498, 4295, 1), -- Race
(498, 4303, 1), -- Strong Type
-- Turek Orc Footman
(499, 4295, 1), -- Race
(499, 4303, 1), -- Strong Type
-- Turek Orc Sentinel
(500, 4295, 1), -- Race
(500, 4303, 1), -- Strong Type
-- Turek Orc Shaman
(501, 4295, 1), -- Race
(501, 4303, 1), -- Strong Type
(501, 4002, 2), -- NPC HP Drain
-- Twink Puma
(502, 4292, 1), -- Race
-- Farakelsus Zombie
(503, 4290, 1), -- Race
(503, 4275, 3), -- Sacred Attack Weak Point
(503, 4278, 1), -- Dark Attack
(503, 4071, 3), -- Resist Archery
(503, 4116, 8), -- Resist M. Atk.
(503, 4284, 3), -- Resist Bleeding
(503, 4248, 2), -- NPC HP Drain - Slow
-- Dread Soldier
(504, 4290, 1), -- Race
(504, 4275, 3), -- Sacred Attack Weak Point
(504, 4278, 1), -- Dark Attack
(504, 4274, 1), -- Blunt Attack Weak Point
-- Ruin Bat
(505, 4292, 1), -- Race
(505, 4281, 2), -- Wind Attack Weak Point
(505, 4276, 1), -- Archery Attack Weak Point
(505, 4001, 2), -- NPC Windstrike
(505, 4002, 2), -- NPC HP Drain
(505, 4038, 3), -- Decrease Atk. Spd.
-- Ruin Imp
(506, 4302, 1), -- Race
(506, 4281, 2), -- Wind Attack Weak Point
(506, 4276, 1), -- Archery Attack Weak Point
(506, 4076, 1), -- Reduction in movement speed
-- Ruin Imp Elder
(507, 4302, 1), -- Race
(507, 4281, 2), -- Wind Attack Weak Point
(507, 4276, 1), -- Archery Attack Weak Point
(507, 4152, 2), -- NPC HP Drain - Magic
(507, 4160, 2), -- NPC Aura Burn - Magic
-- Plunder Tarantula
(508, 4301, 1), -- Race
-- Spore Fungus
(509, 4294, 1), -- Race
(509, 4279, 2), -- Fire Attack Weak Point
(509, 4277, 3), -- Resist Poison
-- Puma
(510, 4292, 1), -- Race
-- Pitchstone Golem
(511, 4291, 1), -- Race
(511, 4071, 4), -- Resist Archery
(511, 4273, 2), -- Resist Dagger
(511, 4274, 1), -- Blunt Attack Weak Point
(511, 4116, 8), -- Resist M. Atk.
(511, 4284, 3), -- Resist Bleeding
(511, 4249, 1), -- Decrease Speed
-- Field Stalker
(512, 4291, 1), -- Race
(512, 4033, 3), -- NPC Burn
-- Skeleton Knight
(513, 4290, 1), -- Race
(513, 4275, 3), -- Sacred Attack Weak Point
(513, 4278, 1), -- Dark Attack
(513, 4002, 3), -- NPC HP Drain
-- Shield Skeleton
(514, 4290, 1), -- Race
(514, 4275, 3), -- Sacred Attack Weak Point
(514, 4278, 1), -- Dark Attack
(514, 4274, 1), -- Blunt Attack Weak Point
-- Skeleton Infantryman
(515, 4290, 1), -- Race
(515, 4275, 3), -- Sacred Attack Weak Point
(515, 4278, 1), -- Dark Attack
(515, 4274, 1), -- Blunt Attack Weak Point
-- Skeleton Sentinel
(516, 4290, 1), -- Race
(516, 4275, 3), -- Sacred Attack Weak Point
(516, 4278, 1), -- Dark Attack
(516, 4274, 1), -- Blunt Attack Weak Point
-- Skeleton Hunter
(517, 4290, 1), -- Race
(517, 4275, 3), -- Sacred Attack Weak Point
(517, 4278, 1), -- Dark Attack
(517, 4274, 1), -- Blunt Attack Weak Point
-- Skeleton Hunter Archer
(518, 4290, 1), -- Race
(518, 4275, 3), -- Sacred Attack Weak Point
(518, 4278, 1), -- Dark Attack
(518, 4274, 1), -- Blunt Attack Weak Point
-- Skeleton Pikeman
(519, 4290, 1), -- Race
(519, 4275, 3), -- Sacred Attack Weak Point
(519, 4278, 1), -- Dark Attack
-- Pirate Captain Uthanka
(520, 4295, 1), -- Race
(520, 4303, 1), -- Strong Type
-- Whinstone Golem
(521, 4291, 1), -- Race
(521, 4071, 4), -- Resist Archery
(521, 4273, 2), -- Resist Dagger
(521, 4274, 1), -- Blunt Attack Weak Point
(521, 4116, 8), -- Resist M. Atk.
(521, 4284, 3), -- Resist Bleeding
(521, 4247, 1), -- NPC Windstrike - Slow
-- White Fang
(522, 4293, 1), -- Race
(522, 4303, 1), -- Strong Type
-- Wasteland Basilisk
(523, 4292, 1), -- Race
-- Grey Wolf Elder
(524, 4293, 1), -- Race
(524, 4311, 1), -- Feeble Type
-- Gray Wolf
(525, 4293, 1), -- Race
-- Obsidian Golem
(526, 4291, 1), -- Race
(526, 4071, 4), -- Resist Archery
(526, 4273, 2), -- Resist Dagger
(526, 4274, 1), -- Blunt Attack Weak Point
(526, 4116, 8), -- Resist M. Atk.
(526, 4284, 3), -- Resist Bleeding
(526, 4249, 1), -- Decrease Speed
-- White Wolf
(527, 4293, 1), -- Race
-- Goblin Lord
(528, 4295, 1), -- Race
-- Young Brown Keltir
(529, 4293, 1), -- Race
-- Young Red Keltir
(530, 4293, 1), -- Race
-- Young Prairie Keltir
(531, 4293, 1), -- Race
-- Brown Keltir
(532, 4293, 1), -- Race
-- Longtail Keltir
(533, 4293, 1), -- Race
-- Red Keltir
(534, 4293, 1), -- Race
-- Prairie Keltir
(535, 4293, 1), -- Race
-- Elder Brown Keltir
(536, 4293, 1), -- Race
-- Elder Red Keltir
(537, 4293, 1), -- Race
-- Elder Prairie Keltir
(538, 4293, 1), -- Race
-- Elder Longtail Keltir
(539, 4293, 1), -- Race
-- Gemstone Beast
(540, 4292, 1), -- Race
-- Ore Bat
(541, 4292, 1), -- Race
(541, 4281, 2), -- Wind Attack Weak Point
(541, 4276, 1), -- Archery Attack Weak Point
(541, 4076, 1), -- Reduction in movement speed
-- Skeleton Longbowman
(542, 4290, 1), -- Race
(542, 4275, 3), -- Sacred Attack Weak Point
(542, 4278, 1), -- Dark Attack
-- Ghost
(543, 4290, 1), -- Race
(543, 4275, 3), -- Sacred Attack Weak Point
(543, 4278, 1), -- Dark Attack
-- Elder Keltir
(544, 4293, 1), -- Race
-- Young Keltir
(545, 4293, 1), -- Race
-- Turek Orc Elder
(546, 4295, 1), -- Race
(546, 4002, 3), -- NPC HP Drain
-- Ol Mahum Reserve
(547, 4295, 1), -- Race
-- Ol Mahum Deserter
(548, 4295, 1), -- Race
-- Ol Mahum Lord
(549, 4295, 1), -- Race
(549, 4303, 1), -- Strong Type
(549, 4033, 3), -- NPC Burn
-- Guardian Basilisk
(550, 4292, 1), -- Race
(550, 4029, 2), -- Enhance P. Def.
-- Road Scavenger
(551, 4295, 1), -- Race
-- Fettered Soul
(552, 4290, 1), -- Race
(552, 4303, 1), -- Strong Type
(552, 4275, 3), -- Sacred Attack Weak Point
(552, 4278, 1), -- Dark Attack
(552, 4274, 1), -- Blunt Attack Weak Point
(552, 4074, 2), -- NPC Haste
-- Windsus
(553, 4293, 1), -- Race
-- Grandis
(554, 4295, 1), -- Race
-- Giant Fungus
(555, 4294, 1), -- Race
(555, 4279, 2), -- Fire Attack Weak Point
(555, 4277, 3), -- Resist Poison
(555, 4076, 3), -- Reduction in movement speed
-- Giant Monstereye
(556, 4291, 1), -- Race
(556, 4281, 2), -- Wind Attack Weak Point
(556, 4276, 1), -- Archery Attack Weak Point
(556, 4155, 4), -- NPC Twister - Magic
(556, 4160, 4), -- NPC Aura Burn - Magic
(556, 4076, 3), -- Reduction in movement speed
-- Dire Wyrm
(557, 4299, 1), -- Race
-- Rotting tree
(558, 4294, 1), -- Race
(558, 4275, 3), -- Sacred Attack Weak Point
(558, 4278, 1), -- Dark Attack
(558, 4274, 1), -- Blunt Attack Weak Point
(558, 4076, 3), -- Reduction in movement speed
-- Rotting Golem
(559, 4291, 1), -- Race
(559, 4071, 4), -- Resist Archery
(559, 4273, 2), -- Resist Dagger
(559, 4274, 1), -- Blunt Attack Weak Point
(559, 4116, 8), -- Resist M. Atk.
(559, 4284, 3), -- Resist Bleeding
(559, 4250, 4), -- NPC Twister - Slow
-- Trisalim Spider
(560, 4301, 1), -- Race
-- Trisalim Tarantula
(561, 4301, 1), -- Race
(561, 4035, 4), -- Poison
-- Spore Zombie
(562, 4290, 1), -- Race
(562, 4275, 3), -- Sacred Attack Weak Point
(562, 4278, 1), -- Dark Attack
(562, 4116, 4), -- Resist M. Atk.
(562, 4284, 3), -- Resist Bleeding
(562, 4250, 4), -- NPC Twister - Slow
-- Manashen Gargoyle
(563, 4291, 1), -- Race
(563, 4071, 4), -- Resist Archery
(563, 4273, 2), -- Resist Dagger
(563, 4274, 1), -- Blunt Attack Weak Point
(563, 4073, 4), -- Shock
-- Enchanted Monstereye
(564, 4291, 1), -- Race
(564, 4281, 2), -- Wind Attack Weak Point
(564, 4276, 1), -- Archery Attack Weak Point
(564, 4151, 4), -- NPC Windstrike - Magic
(564, 4160, 4), -- NPC Aura Burn - Magic
(564, 4076, 3), -- Reduction in movement speed
-- Enchanted Stone Golem
(565, 4291, 1), -- Race
(565, 4071, 4), -- Resist Archery
(565, 4273, 2), -- Resist Dagger
(565, 4274, 1), -- Blunt Attack Weak Point
(565, 4116, 8), -- Resist M. Atk.
(565, 4284, 3), -- Resist Bleeding
(565, 4254, 4), -- NPC Prominence - Slow
-- Enchanted Iron Golem
(566, 4291, 1), -- Race
(566, 4071, 4), -- Resist Archery
(566, 4273, 2), -- Resist Dagger
(566, 4274, 1), -- Blunt Attack Weak Point
(566, 4116, 8), -- Resist M. Atk.
(566, 4284, 3), -- Resist Bleeding
(566, 4252, 4), -- NPC Flamestrike - Slow
-- Enchanted Gargoyle
(567, 4291, 1), -- Race
(567, 4071, 4), -- Resist Archery
(567, 4273, 2), -- Resist Dagger
(567, 4274, 1), -- Blunt Attack Weak Point
(567, 4073, 4), -- Shock
-- Formor
(568, 4292, 1), -- Race
-- Formor Elder
(569, 4292, 1), -- Race
(569, 4028, 2), -- Enhance P. Atk.
-- Tarlk Bugbear
(570, 4295, 1), -- Race
-- Tarlk Bugbear Warrior
(571, 4295, 1), -- Race
(571, 4074, 2), -- NPC Haste
-- Tarlk Bugbear High Warrior
(572, 4295, 1), -- Race
(572, 4032, 4), -- NPC Strike
-- Tarlk Basilisk
(573, 4292, 1), -- Race
-- Elder Tarlk Basilisk
(574, 4292, 1), -- Race
-- Oel Mahum Warrior
(575, 4295, 1), -- Race
-- Oel Mahum Witch Doctor
(576, 4295, 1), -- Race
(576, 4303, 1), -- Strong Type
(576, 4151, 5), -- NPC Windstrike - Magic
(576, 4160, 5), -- NPC Aura Burn - Magic
(576, 4065, 5), -- NPC Heal
-- Leto Lizardman
(577, 4295, 1), -- Race
-- Leto Lizardman Archer
(578, 4295, 1), -- Race
-- Leto Lizardman Soldier
(579, 4295, 1), -- Race
-- Leto Lizardman Warrior
(580, 4295, 1), -- Race
(580, 4303, 1), -- Strong Type
-- Leto Lizardman Shaman
(581, 4295, 1), -- Race
(581, 4152, 3), -- NPC HP Drain - Magic
(581, 4160, 3), -- NPC Aura Burn - Magic
(581, 4076, 2), -- Reduction in movement speed
-- Leto Lizardman Overlord
(582, 4295, 1), -- Race
(582, 4303, 1), -- Strong Type
-- Timak Orc
(583, 4295, 1), -- Race
-- Timak Orc Archer
(584, 4295, 1), -- Race
(584, 4040, 4), -- NPC Bow Attack
-- Timak Orc Soldier
(585, 4295, 1), -- Race
(585, 4032, 4), -- NPC Strike
-- Timak Orc Warrior
(586, 4295, 1), -- Race
(586, 4074, 2), -- NPC Haste
-- Timak Orc Shaman
(587, 4295, 1), -- Race
(587, 4153, 4), -- Decrease Speed
(587, 4160, 4), -- NPC Aura Burn - Magic
(587, 4076, 3), -- Reduction in movement speed
-- Timak Orc Overlord
(588, 4295, 1), -- Race
(588, 4089, 1), -- NPC Bear Stun
(588, 4090, 1), -- NPC Wolf Stun
(588, 4091, 1), -- NPC Ogre Stun
(588, 4092, 1), -- NPC Puma Stun
-- Fline
(589, 4296, 1), -- Race
(589, 4303, 1), -- Strong Type
(589, 4011, 3), -- Resist Wind
(589, 4282, 2), -- Earth Attack Weak Point
(589, 4001, 4), -- NPC Windstrike
-- Liele
(590, 4296, 1), -- Race
(590, 4303, 1), -- Strong Type
(590, 4010, 3), -- Resist Water
(590, 4279, 2), -- Fire Attack Weak Point
(590, 4001, 4), -- NPC Windstrike
-- Valley Treant
(591, 4296, 1), -- Race
(591, 4303, 1), -- Strong Type
(591, 4279, 2), -- Fire Attack Weak Point
(591, 4277, 3), -- Resist Poison
(591, 4071, 3), -- Resist Archery
(591, 4116, 8), -- Resist M. Atk.
(591, 4284, 3), -- Resist Bleeding
(591, 4248, 4), -- NPC HP Drain - Slow
-- Satyr
(592, 4302, 1), -- Race
(592, 4304, 1), -- Strong Type
-- Unicorn
(593, 4296, 1), -- Race
(593, 4304, 1), -- Strong Type
(593, 4030, 3), -- Enhance P. Atk.
-- Forest Runner
(594, 4294, 1), -- Race
(594, 4303, 1), -- Strong Type
(594, 4279, 2), -- Fire Attack Weak Point
(594, 4277, 3), -- Resist Poison
-- Fline Elder
(595, 4296, 1), -- Race
(595, 4303, 1), -- Strong Type
(595, 4011, 3), -- Resist Wind
(595, 4282, 2), -- Earth Attack Weak Point
(595, 4001, 5), -- NPC Windstrike
-- Liele Elder
(596, 4296, 1), -- Race
(596, 4303, 1), -- Strong Type
(596, 4010, 3), -- Resist Water
(596, 4279, 2), -- Fire Attack Weak Point
(596, 4001, 5), -- NPC Windstrike
-- Valley treant Elder
(597, 4296, 1), -- Race
(597, 4304, 1), -- Strong Type
(597, 4279, 2), -- Fire Attack Weak Point
(597, 4277, 3), -- Resist Poison
(597, 4071, 3), -- Resist Archery
(597, 4116, 8), -- Resist M. Atk.
(597, 4284, 3), -- Resist Bleeding
(597, 4250, 5), -- NPC Twister - Slow
-- Satyr Elder
(598, 4302, 1), -- Race
(598, 4304, 1), -- Strong Type
-- Unicorn Elder
(599, 4296, 1), -- Race
(599, 4304, 1), -- Strong Type
(599, 4001, 5), -- NPC Windstrike
-- Karul Bugbear
(600, 4295, 1), -- Race
(600, 4073, 4), -- Shock
-- Tamlin Orc
(601, 4295, 1), -- Race
(601, 4317, 1), -- Increase Rage Might
-- Tamlin Orc Archer
(602, 4295, 1), -- Race
(602, 4040, 4), -- NPC Bow Attack
-- Kronbe Spider
(603, 4301, 1), -- Race
-- Lakin
(604, 4295, 1), -- Race
(604, 4073, 4), -- Shock
-- Weird Drake
(605, 4299, 1), -- Race
(605, 4071, 3), -- Resist Archery
(605, 4078, 4), -- NPC Flamestrike
-- Kuran Kobold
(606, 4295, 1), -- Race
(606, 4303, 1), -- Strong Type
(606, 4032, 2), -- NPC Strike
-- Kuran Kobold Warrior
(607, 4295, 1), -- Race
(607, 4303, 1), -- Strong Type
(607, 4073, 2), -- Shock
-- Patin Archer
(608, 4290, 1), -- Race
(608, 4303, 1), -- Strong Type
(608, 4275, 3), -- Sacred Attack Weak Point
(608, 4278, 1), -- Dark Attack
(608, 4274, 1), -- Blunt Attack Weak Point
-- Lakin Salamander
(609, 4296, 1), -- Race
(609, 4303, 1), -- Strong Type
(609, 4009, 3), -- Resist Fire
(609, 4280, 2), -- Water Attack Weak Point
(609, 4100, 3), -- NPC Prominence
(609, 4001, 3), -- NPC Windstrike
(609, 4104, 3), -- Flame
-- Sentinel Of Water
(610, 4291, 1), -- Race
(610, 4303, 1), -- Strong Type
(610, 4046, 3), -- Sleep
(610, 4098, 3), -- Magic Skill Block
(610, 4002, 3), -- NPC HP Drain
(610, 4094, 3), -- NPC Cancel Magic
-- Dre Vanul Warrior
(611, 4298, 1), -- Race
(611, 4303, 1), -- Strong Type
(611, 4278, 1), -- Dark Attack
(611, 4333, 3), -- Resist Dark Attack
(611, 4002, 3), -- NPC HP Drain
(611, 4098, 3), -- Magic Skill Block
(611, 4047, 3), -- Hold
-- Salamander Rowin
(612, 4296, 1), -- Race
(612, 4303, 1), -- Strong Type
(612, 4009, 3), -- Resist Fire
(612, 4280, 2), -- Water Attack Weak Point
(612, 4100, 3), -- NPC Prominence
(612, 4001, 3), -- NPC Windstrike
(612, 4035, 3), -- Poison
-- Lafi Lizardman
(613, 4295, 1), -- Race
(613, 4303, 1), -- Strong Type
(613, 4067, 2), -- NPC Mortal Blow
-- Lafi Lizardman Scout
(614, 4295, 1), -- Race
(614, 4303, 1), -- Strong Type
-- Ritmal Swordsman
(615, 4290, 1), -- Race
(615, 4303, 1), -- Strong Type
(615, 4275, 3), -- Sacred Attack Weak Point
(615, 4278, 1), -- Dark Attack
(615, 4274, 1), -- Blunt Attack Weak Point
(615, 4073, 2), -- Shock
-- Lakin Undine
(616, 4296, 1), -- Race
(616, 4303, 1), -- Strong Type
(616, 4010, 3), -- Resist Water
(616, 4279, 2), -- Fire Attack Weak Point
(616, 4071, 3), -- Resist Archery
(616, 4116, 8), -- Resist M. Atk.
(616, 4284, 3), -- Resist Bleeding
(616, 4247, 3), -- NPC Windstrike - Slow
-- Sentinel Of Water
(617, 4291, 1), -- Race
(617, 4303, 1), -- Strong Type
(617, 4088, 3), -- Bleed
(617, 4066, 3), -- NPC Twister
(617, 4094, 3), -- NPC Cancel Magic
(617, 4046, 3), -- Sleep
-- Kanil Succubus
(618, 4298, 1), -- Race
(618, 4303, 1), -- Strong Type
(618, 4278, 1), -- Dark Attack
(618, 4333, 3), -- Resist Dark Attack
(618, 4001, 3), -- NPC Windstrike
(618, 4066, 3), -- NPC Twister
(618, 4088, 3), -- Bleed
-- Rowin Undine
(619, 4296, 1), -- Race
(619, 4303, 1), -- Strong Type
(619, 4010, 3), -- Resist Water
(619, 4279, 2), -- Fire Attack Weak Point
(619, 4151, 3), -- NPC Windstrike - Magic
(619, 4160, 3), -- NPC Aura Burn - Magic
-- Cave Beast
(620, 4292, 1), -- Race
(620, 4304, 1), -- Strong Type
(620, 4028, 3), -- Enhance P. Atk.
-- Death Wave
(621, 4291, 1), -- Race
(621, 4304, 1), -- Strong Type
(621, 4100, 6), -- NPC Prominence
(621, 4039, 6), -- NPC MP Drain
(621, 4117, 6), -- Paralysis
-- Malruk Soldier
(622, 4290, 1), -- Race
(622, 4304, 1), -- Strong Type
(622, 4275, 3), -- Sacred Attack Weak Point
(622, 4278, 1), -- Dark Attack
(622, 4085, 1), -- Critical Power
(622, 4086, 1), -- Critical Chance
(622, 4285, 4), -- Resist Sleep
(622, 4067, 6), -- NPC Mortal Blow
-- Plando
(623, 4295, 1), -- Race
(623, 4305, 1), -- Strong Type
(623, 4124, 6), -- NPC Spear Attack
-- Cave Howler
(624, 4295, 1), -- Race
(624, 4305, 1), -- Strong Type
(624, 4074, 2), -- NPC Haste
-- Malruk Knight
(625, 4290, 1), -- Race
(625, 4305, 1), -- Strong Type
(625, 4275, 3), -- Sacred Attack Weak Point
(625, 4278, 1), -- Dark Attack
(625, 4073, 6), -- Shock
-- Malruk Berserker
(626, 4290, 1), -- Race
(626, 4305, 1), -- Strong Type
(626, 4275, 3), -- Sacred Attack Weak Point
(626, 4278, 1), -- Dark Attack
(626, 4084, 8), -- Resist Physical Attack
(626, 4032, 6), -- NPC Strike
-- Malruk Lord
(627, 4290, 1), -- Race
(627, 4306, 1), -- Strong Type
(627, 4275, 3), -- Sacred Attack Weak Point
(627, 4278, 1), -- Dark Attack
(627, 4098, 6), -- Magic Skill Block
(627, 4002, 6), -- NPC HP Drain
(627, 4094, 6), -- NPC Cancel Magic
(627, 4046, 6), -- Sleep
-- Limal Karinness
(628, 4298, 1), -- Race
(628, 4306, 1), -- Strong Type
(628, 4278, 1), -- Dark Attack
(628, 4333, 3), -- Resist Dark Attack
(628, 4002, 6), -- NPC HP Drain
(628, 4117, 6), -- Paralysis
(628, 4047, 6), -- Hold
-- Karik
(629, 4298, 1), -- Race
(629, 4306, 1), -- Strong Type
(629, 4278, 1), -- Dark Attack
(629, 4333, 3), -- Resist Dark Attack
(629, 4085, 1), -- Critical Power
(629, 4086, 1), -- Critical Chance
(629, 4084, 4), -- Resist Physical Attack
(629, 4091, 1), -- NPC Ogre Stun
(629, 4072, 7), -- Shock
(629, 4032, 7), -- NPC Strike
-- Taik Orc
(630, 4295, 1), -- Race
-- Taik Orc Archer
(631, 4295, 1), -- Race
(631, 4040, 4), -- NPC Bow Attack
-- Taik Orc Warrior
(632, 4295, 1), -- Race
(632, 4032, 4), -- NPC Strike
-- Taik Orc Shaman
(633, 4295, 1), -- Race
(633, 4157, 4), -- NPC Blaze - Magic
(633, 4160, 4), -- NPC Aura Burn - Magic
(633, 4065, 4), -- NPC Heal
-- Taik Orc Captain
(634, 4295, 1), -- Race
(634, 4303, 1), -- Strong Type
(634, 4032, 4), -- NPC Strike
-- Carinkain
(635, 4291, 1), -- Race
(635, 4039, 4), -- NPC MP Drain
-- Forrest of Mirrors Ghost
(636, 4290, 1), -- Race
(636, 4275, 3), -- Sacred Attack Weak Point
(636, 4278, 1), -- Dark Attack
(636, 4093, 1), -- Evasion
-- Forrest of Mirrors Ghost
(637, 4290, 1), -- Race
(637, 4275, 3), -- Sacred Attack Weak Point
(637, 4278, 1), -- Dark Attack
(637, 4093, 1), -- Evasion
-- Forrest of Mirrors Ghost
(638, 4290, 1), -- Race
(638, 4275, 3), -- Sacred Attack Weak Point
(638, 4278, 1), -- Dark Attack
(638, 4093, 1), -- Evasion
-- Mirror
(639, 4291, 1), -- Race
(639, 4303, 1), -- Strong Type
(639, 4095, 1), -- Damage Shield
(639, 4039, 4), -- NPC MP Drain
(639, 4094, 4), -- NPC Cancel Magic
(639, 4046, 4), -- Sleep
-- Harit Lizardman
(640, 4295, 1), -- Race
-- Harit Lizardman Grunt
(641, 4295, 1), -- Race
(641, 4032, 5), -- NPC Strike
-- Harit Lizardman Archer
(642, 4295, 1), -- Race
(642, 4085, 1), -- Critical Power
(642, 4096, 3), -- NPC Hawkeye
-- Harit Lizardman Warrior
(643, 4295, 1), -- Race
(643, 4030, 3), -- Enhance P. Atk.
-- Harit Lizardman Shaman
(644, 4295, 1), -- Race
(644, 4155, 5), -- NPC Twister - Magic
(644, 4160, 5), -- NPC Aura Burn - Magic
(644, 4076, 3), -- Reduction in movement speed
-- Harit Lizardman Matriarch
(645, 4295, 1), -- Race
(645, 4303, 1), -- Strong Type
(645, 4085, 1), -- Critical Power
(645, 4086, 1), -- Critical Chance
(645, 4067, 5), -- NPC Mortal Blow
-- Halingka
(646, 4293, 1), -- Race
(646, 4303, 1), -- Strong Type
(646, 4088, 5), -- Bleed
-- Yintzu
(647, 4291, 1), -- Race
(647, 4303, 1), -- Strong Type
(647, 4281, 2), -- Wind Attack Weak Point
(647, 4276, 1), -- Archery Attack Weak Point
(647, 4001, 5), -- NPC Windstrike
-- Paliote
(648, 4291, 1), -- Race
(648, 4303, 1), -- Strong Type
(648, 4084, 4), -- Resist Physical Attack
(648, 4034, 5), -- Decrease Speed
-- Hamrut
(649, 4301, 1), -- Race
(649, 4303, 1), -- Strong Type
(649, 4035, 5), -- Poison
-- Kranrot
(650, 4293, 1), -- Race
(650, 4303, 1), -- Strong Type
(650, 4073, 5), -- Shock
-- Gamlin
(651, 4291, 1), -- Race
(651, 4303, 1), -- Strong Type
-- Leogul
(652, 4291, 1), -- Race
(652, 4303, 1), -- Strong Type
(652, 4037, 2), -- Weaken P. Atk.
-- Lesser Giant
(653, 4300, 1), -- Race
(653, 4304, 1), -- Strong Type
-- Lesser Giant Soldier
(654, 4300, 1), -- Race
(654, 4304, 1), -- Strong Type
(654, 4084, 8), -- Resist Physical Attack
(654, 4287, 4), -- Resist Hold
(654, 4073, 6), -- Shock
-- Lesser Giant Shooter
(655, 4300, 1), -- Race
(655, 4304, 1), -- Strong Type
(655, 4071, 3), -- Resist Archery
(655, 4084, 4), -- Resist Physical Attack
-- Lesser Giant Scout
(656, 4300, 1), -- Race
(656, 4304, 1), -- Strong Type
(656, 4086, 1), -- Critical Chance
(656, 4285, 4), -- Resist Sleep
(656, 4105, 6), -- NPC Straight Beam Cannon
-- Lesser Giant Mage
(657, 4300, 1), -- Race
(657, 4303, 1), -- Strong Type
(657, 4116, 4), -- Resist M. Atk.
(657, 4156, 6), -- NPC Curve Beam Cannon - Magic
(657, 4160, 6), -- NPC Aura Burn - Magic
(657, 4065, 6), -- NPC Heal
-- Lesser Giant Elder
(658, 4300, 1), -- Race
(658, 4303, 1), -- Strong Type
(658, 4116, 4), -- Resist M. Atk.
(658, 4071, 3), -- Resist Archery
(658, 4273, 2), -- Resist Dagger
(658, 4285, 4), -- Resist Sleep
(658, 4287, 4), -- Resist Hold
(658, 4036, 6), -- Poison
(658, 4046, 6), -- Sleep
(658, 4066, 6), -- NPC Twister
(658, 4094, 6), -- NPC Cancel Magic
-- Grave Wanderer
(659, 4290, 1), -- Race
(659, 4275, 3), -- Sacred Attack Weak Point
(659, 4278, 1), -- Dark Attack
(659, 4071, 3), -- Resist Archery
(659, 4116, 8), -- Resist M. Atk.
(659, 4284, 3), -- Resist Bleeding
(659, 4248, 4), -- NPC HP Drain - Slow
-- Archer of Greed
(660, 4290, 1), -- Race
(660, 4275, 3), -- Sacred Attack Weak Point
(660, 4278, 1), -- Dark Attack
(660, 4120, 4), -- Shock
-- Hatar Ratman Thief
(661, 4295, 1), -- Race
(661, 4067, 4), -- NPC Mortal Blow
-- Hatar Ratman Boss
(662, 4295, 1), -- Race
(662, 4067, 4), -- NPC Mortal Blow
-- Hatar Hanishee
(663, 4292, 1), -- Race
(663, 4047, 4), -- Hold
-- Deprive
(664, 4291, 1), -- Race
(664, 4039, 5), -- NPC MP Drain
-- Taik Orc Supply
(665, 4295, 1), -- Race
(665, 4032, 5), -- NPC Strike
-- Taik Orc Seeker
(666, 4295, 1), -- Race
(666, 4073, 5), -- Shock
-- Farcran
(667, 4293, 1), -- Race
(667, 4073, 5), -- Shock
-- Grave Guard
(668, 4291, 1), -- Race
(668, 4071, 4), -- Resist Archery
(668, 4273, 2), -- Resist Dagger
(668, 4274, 1), -- Blunt Attack Weak Point
(668, 4078, 5), -- NPC Flamestrike
-- Taik Orc Supply Leader
(669, 4295, 1), -- Race
(669, 4073, 5), -- Shock
-- Crimson Drake
(670, 4299, 1), -- Race
(670, 4071, 3), -- Resist Archery
(670, 4100, 6), -- NPC Prominence
-- Kadios
(671, 4301, 1), -- Race
(671, 4098, 6), -- Magic Skill Block
-- Trives
(672, 4292, 1), -- Race
(672, 4098, 6), -- Magic Skill Block
(672, 4046, 6), -- Sleep
(672, 4039, 6), -- NPC MP Drain
(672, 4094, 6), -- NPC Cancel Magic
-- Falibati
(673, 4292, 1), -- Race
(673, 4046, 6), -- Sleep
-- Doom Knight
(674, 4290, 1), -- Race
(674, 4275, 3), -- Sacred Attack Weak Point
(674, 4278, 1), -- Dark Attack
(674, 4101, 6), -- NPC Spinning Slasher
(674, 4090, 1), -- NPC Wolf Stun
(674, 4073, 6), -- Shock
-- Tairim
(675, 4291, 1), -- Race
(675, 4029, 3), -- Enhance P. Def.
-- Judge of Marsh
(676, 4294, 1), -- Race
(676, 4279, 2), -- Fire Attack Weak Point
(676, 4277, 3), -- Resist Poison
(676, 4102, 1), -- Become weak against line of fire.
-- Tulben
(677, 4292, 1), -- Race
(677, 4281, 2), -- Wind Attack Weak Point
(677, 4276, 1), -- Archery Attack Weak Point
(677, 4088, 5), -- Bleed
-- Punishment of Undead
(678, 4290, 1), -- Race
(678, 4275, 3), -- Sacred Attack Weak Point
(678, 4278, 1), -- Dark Attack
(678, 4078, 5), -- NPC Flamestrike
-- Marsh Stalker
(679, 4291, 1), -- Race
(679, 4071, 2), -- Resist Archery
(679, 4273, 2), -- Resist Dagger
(679, 4274, 1), -- Blunt Attack Weak Point
(679, 4281, 2), -- Wind Attack Weak Point
(679, 4102, 1), -- Become weak against line of fire.
-- Marsh Drake
(680, 4299, 1), -- Race
(680, 4071, 2), -- Resist Archery
(680, 4281, 2), -- Wind Attack Weak Point
(680, 4100, 5), -- NPC Prominence
-- Vanor Silenos
(681, 4295, 1), -- Race
(681, 4029, 3), -- Enhance P. Def.
-- Vanor Silenos Grunt
(682, 4295, 1), -- Race
(682, 4099, 2), -- NPC Berserk
-- Vanor Silenos Scout
(683, 4295, 1), -- Race
(683, 4085, 1), -- Critical Power
(683, 4032, 4), -- NPC Strike
-- Vanor Silenos Warrior
(684, 4295, 1), -- Race
(684, 4093, 1), -- Evasion
(684, 4073, 4), -- Shock
-- Vanor Silenos Shaman
(685, 4295, 1), -- Race
(685, 4155, 4), -- NPC Twister - Magic
(685, 4160, 4), -- NPC Aura Burn - Magic
(685, 4076, 3), -- Reduction in movement speed
-- Vanor Silenos Chieftain
(686, 4295, 1), -- Race
(686, 4303, 1), -- Strong Type
(686, 4065, 5), -- NPC Heal
-- Revenant of Sir Calibus
(687, 4290, 1), -- Race
(687, 4275, 3), -- Sacred Attack Weak Point
(687, 4278, 1), -- Dark Attack
(687, 4032, 4), -- NPC Strike
-- Squire of Calibus
(688, 4290, 1), -- Race
(688, 4275, 3), -- Sacred Attack Weak Point
(688, 4278, 1), -- Dark Attack
-- Demon Tempest
(689, 4298, 1), -- Race
(689, 4278, 1), -- Dark Attack
(689, 4333, 3), -- Resist Dark Attack
(689, 4002, 3), -- NPC HP Drain
-- Revenant of The Executed
(690, 4290, 1), -- Race
(690, 4275, 3), -- Sacred Attack Weak Point
(690, 4278, 1), -- Dark Attack
-- Trakia
(691, 4295, 1), -- Race
(691, 4032, 4), -- NPC Strike
-- Red Eye Guards
(692, 4295, 1), -- Race
(692, 4031, 3), -- Enhance P. Def.
-- Nurka's Messenger
(693, 4295, 1), -- Race
(693, 4032, 3), -- NPC Strike
-- Messenger Escort
(694, 4295, 1), -- Race
-- Captain of Queen's Royal Guard
(695, 4301, 1), -- Race
(695, 4100, 4), -- NPC Prominence
-- Marsh Stakato Noble
(696, 4301, 1), -- Race
-- Premo Prime
(697, 4295, 1), -- Race
(697, 4071, 4), -- Resist Archery
(697, 4273, 2), -- Resist Dagger
(697, 4274, 1), -- Blunt Attack Weak Point
-- Titan's Creation, Bemos
(698, 4295, 1), -- Race
(698, 4071, 4), -- Resist Archery
(698, 4273, 2), -- Resist Dagger
(698, 4274, 1), -- Blunt Attack Weak Point
-- Archon Susceptor
(699, 4291, 1), -- Race
(699, 4071, 4), -- Resist Archery
(699, 4273, 2), -- Resist Dagger
(699, 4274, 1), -- Blunt Attack Weak Point
(699, 4033, 5), -- NPC Burn
-- Gustos Susceptor
(700, 4291, 1), -- Race
(700, 4071, 4), -- Resist Archery
(700, 4273, 2), -- Resist Dagger
(700, 4274, 1), -- Blunt Attack Weak Point
-- Eye of Beleth
(701, 4291, 1), -- Race
(701, 4157, 3), -- NPC Blaze - Magic
(701, 4160, 3), -- NPC Aura Burn - Magic
(701, 4035, 3), -- Poison
-- Follower of The Eye
(702, 4291, 1), -- Race
-- Skyla
(703, 4295, 1), -- Race
(703, 4341, 1), -- Ultimate Buff, 3rd
(703, 4067, 3), -- NPC Mortal Blow
-- Skyla's Retainer
(704, 4295, 1), -- Race
-- Corsair Captain Kylon
(705, 4290, 1), -- Race
(705, 4275, 3), -- Sacred Attack Weak Point
(705, 4278, 1), -- Dark Attack
-- Kylon's Pirate
(706, 4290, 1), -- Race
(706, 4275, 3), -- Sacred Attack Weak Point
(706, 4278, 1), -- Dark Attack
-- Lord Ishka
(707, 4290, 1), -- Race
(707, 4275, 3), -- Sacred Attack Weak Point
(707, 4278, 1), -- Dark Attack
(707, 4067, 6), -- NPC Mortal Blow
-- Cave Servant General
(708, 4290, 1), -- Race
(708, 4275, 3), -- Sacred Attack Weak Point
(708, 4278, 1), -- Dark Attack
-- Rinoket
(709, 4295, 1), -- Race
(709, 4073, 4), -- Shock
-- Rinoket's Henchman
(710, 4295, 1), -- Race
(710, 4317, 1), -- Increase Rage Might
-- Necrosentinel Guard
(711, 4290, 1), -- Race
(711, 4275, 3), -- Sacred Attack Weak Point
(711, 4278, 1), -- Dark Attack
(711, 4067, 3), -- NPC Mortal Blow
-- Necrosentinel Archer
(712, 4290, 1), -- Race
(712, 4275, 3), -- Sacred Attack Weak Point
(712, 4278, 1), -- Dark Attack
-- Nakondas
(713, 4299, 1), -- Race
(713, 4071, 3), -- Resist Archery
(713, 4073, 4), -- Shock
-- Nakonda's Slave
(714, 4299, 1), -- Race
(714, 4071, 3), -- Resist Archery
-- Dread Avenger Kraven
(715, 4290, 1), -- Race
(715, 4275, 3), -- Sacred Attack Weak Point
(715, 4278, 1), -- Dark Attack
(715, 4032, 4), -- NPC Strike
-- Dread Panther
(716, 4292, 1), -- Race
-- Handmaiden of Orfen
(717, 4301, 1), -- Race
(717, 4036, 4), -- Poison
-- Trisalim Escort
(718, 4301, 1), -- Race
-- Fairy Queen Timiniel
(719, 4302, 1), -- Race
(719, 4158, 6), -- NPC Prominence - Magic
(719, 4160, 6), -- NPC Aura Burn - Magic
(719, 4076, 3), -- Reduction in movement speed
-- Timiniel's Escort
(720, 4302, 1), -- Race
(720, 4001, 5), -- NPC Windstrike
(720, 4065, 5), -- NPC Heal
-- Out of Use
(721, 4295, 1), -- Race
-- Out of Use
(722, 4292, 1), -- Race
(722, 4281, 2), -- Wind Attack Weak Point
(722, 4276, 1), -- Archery Attack Weak Point
-- Centaurus
(723, 4292, 1), -- Race
-- Crimson Bear
(724, 4293, 1), -- Race
-- Devastator
(725, 4301, 1), -- Race
-- Saber Toothed Tiger
(726, 4293, 1), -- Race
-- Liviona
(727, 4291, 1), -- Race
(727, 4071, 3), -- Resist Archery
-- Heltor Silenos
(728, 4295, 1), -- Race
-- Heltor Silenos Chieftain
(729, 4295, 1), -- Race
-- Heltor Silenos Hunter
(730, 4295, 1), -- Race
-- Heltor Silenos Shaman
(731, 4295, 1), -- Race
-- Heltor Silenos Warrior
(732, 4295, 1), -- Race
(732, 4085, 1), -- Critical Power
(732, 4086, 1), -- Critical Chance
-- Ketra Orc
(733, 4295, 1), -- Race
-- Ketra Orc Archer
(734, 4295, 1), -- Race
-- Ketra Orc Overload
(735, 4295, 1), -- Race
-- Ketra Orc Shaman
(736, 4295, 1), -- Race
-- Ketra Orc Warrior
(737, 4295, 1), -- Race
-- Kobold Looter Bepook
(738, 4295, 1), -- Race
(738, 4303, 1), -- Strong Type
(738, 4073, 1), -- Shock
-- Bepook's Pet
(739, 4293, 1), -- Race
(739, 4311, 1), -- Feeble Type
-- Leader Talloth
(740, 4291, 1), -- Race
(740, 4071, 4), -- Resist Archery
(740, 4273, 2), -- Resist Dagger
(740, 4274, 1), -- Blunt Attack Weak Point
(740, 4116, 8), -- Resist M. Atk.
(740, 4284, 3), -- Resist Bleeding
(740, 4250, 2), -- NPC Twister - Slow
-- Rampage Golem
(741, 4291, 1), -- Race
(741, 4071, 4), -- Resist Archery
(741, 4273, 2), -- Resist Dagger
(741, 4274, 1), -- Blunt Attack Weak Point
(741, 4116, 8), -- Resist M. Atk.
(741, 4284, 3), -- Resist Bleeding
(741, 4247, 2), -- NPC Windstrike - Slow
-- Mystical Weaver
(742, 4291, 1), -- Race
-- Howler
(743, 4295, 1), -- Race
-- Red Eye Vampire Bat
(744, 4292, 1), -- Race
(744, 4281, 2), -- Wind Attack Weak Point
(744, 4276, 1), -- Archery Attack Weak Point
-- Wild Desperado
(745, 4301, 1), -- Race
(745, 4073, 3), -- Shock
-- Wild Desperado Cohort
(746, 4301, 1), -- Race
(746, 4032, 3), -- NPC Strike
-- Roxide
(747, 4298, 1), -- Race
(747, 4303, 1), -- Strong Type
(747, 4278, 1), -- Dark Attack
(747, 4333, 3), -- Resist Dark Attack
(747, 4002, 3), -- NPC HP Drain
-- Roxide Cohort
(748, 4290, 1), -- Race
(748, 4303, 1), -- Strong Type
(748, 4275, 3), -- Sacred Attack Weak Point
(748, 4278, 1), -- Dark Attack
(748, 4065, 2), -- NPC Heal
(748, 4035, 2), -- Poison
-- Death Fire
(749, 4298, 1), -- Race
(749, 4303, 1), -- Strong Type
(749, 4278, 1), -- Dark Attack
(749, 4333, 3), -- Resist Dark Attack
(749, 4087, 3), -- NPC Blaze
-- Fire Archer
(750, 4290, 1), -- Race
(750, 4303, 1), -- Strong Type
(750, 4275, 3), -- Sacred Attack Weak Point
(750, 4278, 1), -- Dark Attack
(750, 4120, 2), -- Shock
-- Snipe
(751, 4292, 1), -- Race
(751, 4303, 1), -- Strong Type
(751, 4086, 1), -- Critical Chance
(751, 4071, 3), -- Resist Archery
(751, 4073, 4), -- Shock
-- Snipe Cohort
(752, 4292, 1), -- Race
(752, 4303, 1), -- Strong Type
(752, 4116, 4), -- Resist M. Atk.
(752, 4032, 4), -- NPC Strike
-- Dark Lord
(753, 4290, 1), -- Race
(753, 4303, 1), -- Strong Type
(753, 4275, 3), -- Sacred Attack Weak Point
(753, 4278, 1), -- Dark Attack
(753, 4225, 1), -- Resist Shock
(753, 4078, 5), -- NPC Flamestrike
-- Dark Knight
(754, 4290, 1), -- Race
(754, 4303, 1), -- Strong Type
(754, 4275, 3), -- Sacred Attack Weak Point
(754, 4278, 1), -- Dark Attack
(754, 4084, 4), -- Resist Physical Attack
(754, 4077, 4), -- NPC Aura Burn
-- Talakin
(755, 4295, 1), -- Race
(755, 4032, 3), -- NPC Strike
-- Talakin Archer
(756, 4295, 1), -- Race
(756, 4120, 2), -- Shock
-- Talakin Raider
(757, 4295, 1), -- Race
(757, 4096, 2), -- NPC Hawkeye
-- Dragon Bearer Chief
(758, 4290, 1), -- Race
(758, 4275, 3), -- Sacred Attack Weak Point
(758, 4278, 1), -- Dark Attack
(758, 4032, 4), -- NPC Strike
-- Dragon Bearer Warrior
(759, 4290, 1), -- Race
(759, 4275, 3), -- Sacred Attack Weak Point
(759, 4278, 1), -- Dark Attack
(759, 4073, 4), -- Shock
-- Dragon Bearer Archer
(760, 4290, 1), -- Race
(760, 4275, 3), -- Sacred Attack Weak Point
(760, 4278, 1), -- Dark Attack
(760, 4120, 4), -- Shock
-- Pytan
(761, 4298, 1), -- Race
(761, 4304, 1), -- Strong Type
(761, 4278, 1), -- Dark Attack
(761, 4333, 3), -- Resist Dark Attack
(761, 4285, 4), -- Resist Sleep
(761, 4002, 6), -- NPC HP Drain
-- Pytan Knight
(762, 4290, 1), -- Race
(762, 4303, 1), -- Strong Type
(762, 4275, 3), -- Sacred Attack Weak Point
(762, 4278, 1), -- Dark Attack
(762, 4097, 6), -- NPC Chant of Life
-- Lord of Plain
(763, 4295, 1), -- Race
(763, 4032, 4), -- NPC Strike
-- Shaman of Plain
(764, 4295, 1), -- Race
(764, 4066, 3), -- NPC Twister
(764, 4065, 3), -- NPC Heal
-- Warrior of Plain
(765, 4295, 1), -- Race
(765, 4030, 2), -- Enhance P. Atk.
-- Scout of Plain
(766, 4295, 1), -- Race
-- Timak Orc Troop Leader
(767, 4295, 1), -- Race
(767, 4032, 4), -- NPC Strike
-- Timak Orc Troop Shaman
(768, 4295, 1), -- Race
(768, 4065, 4), -- NPC Heal
(768, 4036, 4), -- Poison
-- Timak Orc Troop Warrior
(769, 4295, 1), -- Race
(769, 4099, 2), -- NPC Berserk
-- Timak Orc Troop Archer
(770, 4295, 1), -- Race
(770, 4120, 4), -- Shock
-- Barif
(771, 4300, 1), -- Race
(771, 4303, 1), -- Strong Type
(771, 4066, 6), -- NPC Twister
-- Barif's Pet
(772, 4291, 1), -- Race
(772, 4303, 1), -- Strong Type
(772, 4032, 6), -- NPC Strike
-- Conjurer Bat Lord
(773, 4292, 1), -- Race
(773, 4281, 2), -- Wind Attack Weak Point
(773, 4276, 1), -- Archery Attack Weak Point
(773, 4038, 5), -- Decrease Atk. Spd.
-- Conjurer Bat 
(774, 4292, 1), -- Race
(774, 4281, 2), -- Wind Attack Weak Point
(774, 4276, 1), -- Archery Attack Weak Point
(774, 4065, 5), -- NPC Heal
-- Bugbear Raider
(775, 4295, 1), -- Race
-- Dark Succubus
(776, 4298, 1), -- Race
(776, 4278, 1), -- Dark Attack
(776, 4333, 3), -- Resist Dark Attack
(776, 4035, 2), -- Poison
-- Hunter Bear
(777, 4293, 1), -- Race
-- Ragna Orc Overlord
(778, 4295, 1), -- Race
-- Ragna Orc Seer
(779, 4295, 1), -- Race
-- Bloody Axe Elite
(780, 4295, 1), -- Race
-- Delu Lizardman Shaman
(781, 4295, 1), -- Race
(781, 4158, 3), -- NPC Prominence - Magic
(781, 4160, 3), -- NPC Aura Burn - Magic
(781, 4076, 2), -- Reduction in movement speed
-- Ol Mahum Novice
(782, 4295, 1), -- Race
-- Dread Wolf
(783, 4293, 1), -- Race
(783, 4311, 1), -- Feeble Type
-- Tasaba Lizardman
(784, 4295, 1), -- Race
(784, 4311, 1), -- Feeble Type
(784, 4093, 1), -- Evasion
-- Tasaba Lizardman Shaman
(785, 4295, 1), -- Race
(785, 4311, 1), -- Feeble Type
(785, 4034, 3), -- Decrease Speed
-- Lienrik
(786, 4292, 1), -- Race
(786, 4311, 1), -- Feeble Type
(786, 4034, 3), -- Decrease Speed
-- Lienrik Lad
(787, 4292, 1), -- Race
(787, 4311, 1), -- Feeble Type
(787, 4036, 4), -- Poison
-- Rakul
(788, 4290, 1), -- Race
(788, 4275, 3), -- Sacred Attack Weak Point
(788, 4278, 1), -- Dark Attack
(788, 4032, 3), -- NPC Strike
-- Crokian
(789, 4292, 1), -- Race
(789, 4030, 2), -- Enhance P. Atk.
-- Dailaon
(790, 4292, 1), -- Race
(790, 4071, 3), -- Resist Archery
(790, 4228, 3), -- NPC Double Dagger Attack
-- Crokian Warrior
(791, 4292, 1), -- Race
(791, 4303, 1), -- Strong Type
(791, 4074, 2), -- NPC Haste
-- Farhite
(792, 4292, 1), -- Race
(792, 4303, 1), -- Strong Type
(792, 4151, 3), -- NPC Windstrike - Magic
(792, 4160, 3), -- NPC Aura Burn - Magic
(792, 4076, 2), -- Reduction in movement speed
-- Nos
(793, 4292, 1), -- Race
(793, 4303, 1), -- Strong Type
(793, 4085, 1), -- Critical Power
(793, 4099, 2), -- NPC Berserk
-- Blade Stakato
(794, 4301, 1), -- Race
(794, 4303, 1), -- Strong Type
(794, 4279, 2), -- Fire Attack Weak Point
(794, 4074, 2), -- NPC Haste
-- Blade Stakato Worker
(795, 4301, 1), -- Race
(795, 4303, 1), -- Strong Type
(795, 4279, 2), -- Fire Attack Weak Point
(795, 4076, 3), -- Reduction in movement speed
-- Blade Stakato Warrior
(796, 4301, 1), -- Race
(796, 4303, 1), -- Strong Type
(796, 4279, 2), -- Fire Attack Weak Point
(796, 4287, 4), -- Resist Hold
(796, 4098, 4), -- Magic Skill Block
-- Blade Stakato Drone
(797, 4301, 1), -- Race
(797, 4304, 1), -- Strong Type
(797, 4279, 2), -- Fire Attack Weak Point
(797, 4085, 1), -- Critical Power
(797, 4285, 4), -- Resist Sleep
(797, 4098, 4), -- Magic Skill Block
(797, 4076, 3), -- Reduction in movement speed
(797, 4034, 4), -- Decrease Speed
(797, 4094, 4), -- NPC Cancel Magic
(797, 4046, 4), -- Sleep
-- Water Giant
(798, 4295, 1), -- Race
(798, 4303, 1), -- Strong Type
(798, 4086, 1), -- Critical Chance
(798, 4073, 4), -- Shock
-- Queen Undine Lad
(799, 4296, 1), -- Race
(799, 4303, 1), -- Strong Type
(799, 4093, 1), -- Evasion
(799, 4285, 4), -- Resist Sleep
(799, 4034, 4), -- Decrease Speed
(799, 4066, 4), -- NPC Twister
(799, 4118, 4), -- Paralysis
-- Eva's Seeker
(800, 4291, 1), -- Race
(800, 4303, 1), -- Strong Type
(800, 4281, 2), -- Wind Attack Weak Point
(800, 4276, 1), -- Archery Attack Weak Point
(800, 4119, 3), -- Fall in accuracy
-- Theeder Piker
(801, 4296, 1), -- Race
(801, 4303, 1), -- Strong Type
(801, 4071, 3), -- Resist Archery
(801, 4085, 1), -- Critical Power
(801, 4244, 4), -- NPC Wild Sweep
-- Theeder Mage
(802, 4296, 1), -- Race
(802, 4303, 1), -- Strong Type
(802, 4116, 4), -- Resist M. Atk.
(802, 4118, 4), -- Paralysis
(802, 4046, 4), -- Sleep
(802, 4039, 4), -- NPC MP Drain
(802, 4094, 4), -- NPC Cancel Magic
-- Doll Master
(803, 4290, 1), -- Race
(803, 4304, 1), -- Strong Type
(803, 4275, 3), -- Sacred Attack Weak Point
(803, 4278, 1), -- Dark Attack
(803, 4274, 1), -- Blunt Attack Weak Point
(803, 4084, 7), -- Resist Physical Attack
(803, 4074, 2), -- NPC Haste
-- Crokian Lad
(804, 4292, 1), -- Race
(804, 4303, 1), -- Strong Type
(804, 4074, 2), -- NPC Haste
-- Dailaon Lad
(805, 4292, 1), -- Race
(805, 4303, 1), -- Strong Type
(805, 4228, 4), -- NPC Double Dagger Attack
-- Crokian Lad Warrior
(806, 4292, 1), -- Race
(806, 4303, 1), -- Strong Type
(806, 4030, 3), -- Enhance P. Atk.
-- Farhite Lad
(807, 4292, 1), -- Race
(807, 4303, 1), -- Strong Type
(807, 4153, 4), -- Decrease Speed
(807, 4160, 4), -- NPC Aura Burn - Magic
(807, 4065, 4), -- NPC Heal
-- Nos Lad
(808, 4292, 1), -- Race
(808, 4303, 1), -- Strong Type
(808, 4071, 3), -- Resist Archery
(808, 4085, 1), -- Critical Power
(808, 4086, 1), -- Critical Chance
-- Ghost of the Tower
(809, 4290, 1), -- Race
(809, 4304, 1), -- Strong Type
(809, 4275, 3), -- Sacred Attack Weak Point
(809, 4278, 1), -- Dark Attack
(809, 4285, 4), -- Resist Sleep
(809, 4158, 6), -- NPC Prominence - Magic
(809, 4160, 6), -- NPC Aura Burn - Magic
(809, 4098, 6), -- Magic Skill Block
-- Seer of Hallate
(810, 4291, 1), -- Race
(810, 4304, 1), -- Strong Type
(810, 4281, 2), -- Wind Attack Weak Point
(810, 4046, 6), -- Sleep
(810, 4098, 6), -- Magic Skill Block
(810, 4088, 6), -- Bleed
(810, 4002, 6), -- NPC HP Drain
(810, 4094, 6), -- NPC Cancel Magic
-- Ghastly Warrior
(811, 4290, 1), -- Race
(811, 4304, 1), -- Strong Type
(811, 4275, 3), -- Sacred Attack Weak Point
(811, 4278, 1), -- Dark Attack
(811, 4072, 6), -- Shock
(811, 4092, 1), -- NPC Puma Stun
(811, 4032, 6), -- NPC Strike
-- Archer of Despair
(812, 4290, 1), -- Race
(812, 4304, 1), -- Strong Type
(812, 4275, 3), -- Sacred Attack Weak Point
(812, 4278, 1), -- Dark Attack
(812, 4285, 4), -- Resist Sleep
(812, 4040, 6), -- NPC Bow Attack
-- Crendion
(813, 4291, 1), -- Race
(813, 4304, 1), -- Strong Type
(813, 4281, 2), -- Wind Attack Weak Point
(813, 4157, 6), -- NPC Blaze - Magic
(813, 4160, 6), -- NPC Aura Burn - Magic
-- Blader of Despair
(814, 4290, 1), -- Race
(814, 4304, 1), -- Strong Type
(814, 4275, 3), -- Sacred Attack Weak Point
(814, 4278, 1), -- Dark Attack
(814, 4033, 6), -- NPC Burn
(814, 4091, 1), -- NPC Ogre Stun
(814, 4032, 6), -- NPC Strike
-- Hound Dog of Hallate
(815, 4293, 1), -- Race
(815, 4305, 1), -- Strong Type
(815, 4072, 6), -- Shock
-- Hallate's Royal Guard
(816, 4290, 1), -- Race
(816, 4304, 1), -- Strong Type
(816, 4275, 3), -- Sacred Attack Weak Point
(816, 4278, 1), -- Dark Attack
(816, 4285, 4), -- Resist Sleep
(816, 4033, 6), -- NPC Burn
(816, 4092, 1), -- NPC Puma Stun
(816, 4067, 6), -- NPC Mortal Blow
-- Corrupt Sage
(817, 4298, 1), -- Race
(817, 4304, 1), -- Strong Type
(817, 4278, 1), -- Dark Attack
(817, 4333, 3), -- Resist Dark Attack
(817, 4116, 4), -- Resist M. Atk.
(817, 4155, 6), -- NPC Twister - Magic
(817, 4160, 6), -- NPC Aura Burn - Magic
(817, 4118, 6), -- Paralysis
-- Hallate's Warrior
(818, 4290, 1), -- Race
(818, 4304, 1), -- Strong Type
(818, 4275, 3), -- Sacred Attack Weak Point
(818, 4278, 1), -- Dark Attack
(818, 4287, 4), -- Resist Hold
(818, 4032, 6), -- NPC Strike
-- Archer of Abyss
(819, 4298, 1), -- Race
(819, 4304, 1), -- Strong Type
(819, 4278, 1), -- Dark Attack
(819, 4333, 3), -- Resist Dark Attack
(819, 4071, 3), -- Resist Archery
(819, 4389, 4), -- Resist Mental Derangement
(819, 4141, 6), -- NPC Wind Fist
-- Hallate's Knight
(820, 4290, 1), -- Race
(820, 4305, 1), -- Strong Type
(820, 4275, 3), -- Sacred Attack Weak Point
(820, 4278, 1), -- Dark Attack
(820, 4086, 1), -- Critical Chance
(820, 4085, 1), -- Critical Power
(820, 4281, 2), -- Wind Attack Weak Point
(820, 4084, 8), -- Resist Physical Attack
(820, 4072, 6), -- Shock
(820, 4092, 1), -- NPC Puma Stun
(820, 4032, 6), -- NPC Strike
-- Erin Ediunce
(821, 4298, 1), -- Race
(821, 4305, 1), -- Strong Type
(821, 4278, 1), -- Dark Attack
(821, 4333, 3), -- Resist Dark Attack
(821, 4116, 4), -- Resist M. Atk.
(821, 4100, 6), -- NPC Prominence
(821, 4119, 3), -- Fall in accuracy
(821, 4047, 6), -- Hold
-- Hallate's Maid
(822, 4298, 1), -- Race
(822, 4305, 1), -- Strong Type
(822, 4278, 1), -- Dark Attack
(822, 4333, 3), -- Resist Dark Attack
(822, 4071, 3), -- Resist Archery
(822, 4085, 1), -- Critical Power
(822, 4086, 1), -- Critical Chance
(822, 4046, 6), -- Sleep
(822, 4039, 6), -- NPC MP Drain
(822, 4094, 6), -- NPC Cancel Magic
-- Platinum Tribe Soldier
(823, 4297, 1), -- Race
(823, 4305, 1), -- Strong Type
(823, 4084, 4), -- Resist Physical Attack
(823, 4073, 6), -- Shock
-- Hallate's Commander
(824, 4290, 1), -- Race
(824, 4305, 1), -- Strong Type
(824, 4275, 3), -- Sacred Attack Weak Point
(824, 4278, 1), -- Dark Attack
(824, 4287, 4), -- Resist Hold
(824, 4073, 6), -- Shock
-- Hallate's Inspector
(825, 4298, 1), -- Race
(825, 4305, 1), -- Strong Type
(825, 4278, 1), -- Dark Attack
(825, 4333, 3), -- Resist Dark Attack
(825, 4071, 3), -- Resist Archery
(825, 4085, 1), -- Critical Power
(825, 4086, 1), -- Critical Chance
(825, 4078, 6), -- NPC Flamestrike
(825, 4069, 6), -- NPC Curve Beam Cannon
(825, 4118, 6), -- Paralysis
-- Platinum Tribe Archer
(826, 4297, 1), -- Race
(826, 4304, 1), -- Strong Type
(826, 4095, 1), -- Damage Shield
(826, 4086, 1), -- Critical Chance
(826, 4040, 6), -- NPC Bow Attack
-- Platinum Tribe Warrior
(827, 4297, 1), -- Race
(827, 4305, 1), -- Strong Type
(827, 4071, 3), -- Resist Archery
(827, 4085, 1), -- Critical Power
(827, 4086, 1), -- Critical Chance
(827, 4287, 4), -- Resist Hold
(827, 4072, 6), -- Shock
(827, 4091, 1), -- NPC Ogre Stun
(827, 4032, 6), -- NPC Strike
-- Platinum Tribe Shaman
(828, 4297, 1), -- Race
(828, 4306, 1), -- Strong Type
(828, 4116, 4), -- Resist M. Atk.
(828, 4285, 4), -- Resist Sleep
(828, 4046, 7), -- Sleep
(828, 4073, 7), -- Shock
(828, 4066, 7), -- NPC Twister
(828, 4094, 7), -- NPC Cancel Magic
-- Platinum Tribe Overlord
(829, 4297, 1), -- Race
(829, 4306, 1), -- Strong Type
(829, 4085, 1), -- Critical Power
(829, 4086, 1), -- Critical Chance
(829, 4084, 8), -- Resist Physical Attack
(829, 4033, 7), -- NPC Burn
(829, 4092, 1), -- NPC Puma Stun
(829, 4073, 7), -- Shock
-- Guardian Angel
(830, 4297, 1), -- Race
(830, 4305, 1), -- Strong Type
(830, 4281, 2), -- Wind Attack Weak Point
(830, 4085, 1), -- Critical Power
(830, 4086, 1), -- Critical Chance
(830, 4033, 7), -- NPC Burn
(830, 4092, 1), -- NPC Puma Stun
(830, 4073, 7), -- Shock
-- Seal Angel
(831, 4297, 1), -- Race
(831, 4306, 1), -- Strong Type
(831, 4281, 2), -- Wind Attack Weak Point
(831, 4085, 1), -- Critical Power
(831, 4086, 1), -- Critical Chance
(831, 4084, 4), -- Resist Physical Attack
(831, 4072, 7), -- Shock
(831, 4090, 1), -- NPC Wolf Stun
(831, 4232, 7), -- NPC AE Strike
-- Zaken's Pikeman
(832, 4290, 1), -- Race
(832, 4275, 3), -- Sacred Attack Weak Point
(832, 4278, 1), -- Dark Attack
(832, 4244, 4), -- NPC Wild Sweep
-- Zaken's Archer
(833, 4290, 1), -- Race
(833, 4275, 3), -- Sacred Attack Weak Point
(833, 4278, 1), -- Dark Attack
(833, 4040, 4), -- NPC Bow Attack
-- Mardian
(834, 4301, 1), -- Race
(834, 4303, 1), -- Strong Type
(834, 4074, 2), -- NPC Haste
-- Zaken's Seer
(835, 4291, 1), -- Race
(835, 4071, 4), -- Resist Archery
(835, 4273, 2), -- Resist Dagger
(835, 4274, 1), -- Blunt Attack Weak Point
(835, 4151, 4), -- NPC Windstrike - Magic
(835, 4160, 4), -- NPC Aura Burn - Magic
-- Pirate Zombie
(836, 4290, 1), -- Race
(836, 4303, 1), -- Strong Type
(836, 4275, 3), -- Sacred Attack Weak Point
(836, 4278, 1), -- Dark Attack
(836, 4084, 6), -- Resist Physical Attack
(836, 4067, 4), -- NPC Mortal Blow
-- Tainted Ogre
(837, 4295, 1), -- Race
(837, 4303, 1), -- Strong Type
(837, 4225, 1), -- Resist Shock
(837, 4073, 4), -- Shock
-- Bloody Bat
(838, 4292, 1), -- Race
(838, 4303, 1), -- Strong Type
(838, 4088, 4), -- Bleed
-- Unpleasant Humming
(839, 4290, 1), -- Race
(839, 4275, 3), -- Sacred Attack Weak Point
(839, 4278, 1), -- Dark Attack
(839, 4285, 4), -- Resist Sleep
(839, 4088, 4), -- Bleed
-- Death Flyer
(840, 4290, 1), -- Race
(840, 4275, 3), -- Sacred Attack Weak Point
(840, 4278, 1), -- Dark Attack
(840, 4285, 4), -- Resist Sleep
(840, 4287, 4), -- Resist Hold
(840, 4074, 2), -- NPC Haste
-- Fiend Archer
(841, 4298, 1), -- Race
(841, 4278, 1), -- Dark Attack
(841, 4333, 3), -- Resist Dark Attack
(841, 4071, 3), -- Resist Archery
(841, 4085, 1), -- Critical Power
(841, 4086, 1), -- Critical Chance
(841, 4389, 4), -- Resist Mental Derangement
(841, 4141, 4), -- NPC Wind Fist
-- Musveren
(842, 4291, 1), -- Race
(842, 4071, 4), -- Resist Archery
(842, 4273, 2), -- Resist Dagger
(842, 4274, 1), -- Blunt Attack Weak Point
(842, 4002, 4), -- NPC HP Drain
-- Zaken's Royal Guard
(843, 4290, 1), -- Race
(843, 4275, 3), -- Sacred Attack Weak Point
(843, 4278, 1), -- Dark Attack
(843, 4071, 3), -- Resist Archery
(843, 4273, 2), -- Resist Dagger
(843, 4274, 1), -- Blunt Attack Weak Point
(843, 4067, 5), -- NPC Mortal Blow
-- Kaim Vanul
(844, 4298, 1), -- Race
(844, 4278, 1), -- Dark Attack
(844, 4333, 3), -- Resist Dark Attack
(844, 4085, 1), -- Critical Power
(844, 4086, 1), -- Critical Chance
(844, 4047, 5), -- Hold
-- Pirate's Zombie Captain
(845, 4290, 1), -- Race
(845, 4275, 3), -- Sacred Attack Weak Point
(845, 4278, 1), -- Dark Attack
(845, 4084, 7), -- Resist Physical Attack
(845, 4067, 5), -- NPC Mortal Blow
-- Doll Blader
(846, 4298, 1), -- Race
(846, 4278, 1), -- Dark Attack
(846, 4333, 3), -- Resist Dark Attack
(846, 4085, 1), -- Critical Power
(846, 4086, 1), -- Critical Chance
(846, 4285, 4), -- Resist Sleep
(846, 4287, 4), -- Resist Hold
(846, 4074, 2), -- NPC Haste
-- Vale Master
(847, 4298, 1), -- Race
(847, 4278, 1), -- Dark Attack
(847, 4333, 3), -- Resist Dark Attack
(847, 4085, 1), -- Critical Power
(847, 4086, 1), -- Critical Chance
(847, 4076, 3), -- Reduction in movement speed
(847, 4046, 5), -- Sleep
(847, 4087, 5), -- NPC Blaze
(847, 4094, 5), -- NPC Cancel Magic
-- Light Bringer
(848, 4292, 1), -- Race
(848, 4303, 1), -- Strong Type
(848, 4076, 3), -- Reduction in movement speed
-- Light Worm
(849, 4301, 1), -- Race
(849, 4303, 1), -- Strong Type
(849, 4279, 2), -- Fire Attack Weak Point
(849, 4285, 4), -- Resist Sleep
(849, 4287, 4), -- Resist Hold
(849, 4076, 3), -- Reduction in movement speed
-- Golden Stag Lord
(850, 4293, 1), -- Race
(850, 4071, 3), -- Resist Archery
(850, 4085, 1), -- Critical Power
(850, 4086, 1), -- Critical Chance
(850, 4076, 3), -- Reduction in movement speed
-- Theeder
(851, 4296, 1), -- Race
(851, 4085, 1), -- Critical Power
(851, 4086, 1), -- Critical Chance
-- Banshee
(852, 4298, 1), -- Race
(852, 4275, 3), -- Sacred Attack Weak Point
(852, 4278, 1), -- Dark Attack
-- Kel Mahum
(853, 4297, 1), -- Race
(853, 4084, 4), -- Resist Physical Attack
(853, 4090, 1), -- NPC Wolf Stun
(853, 4072, 6), -- Shock
(853, 4032, 6), -- NPC Strike
-- Kel Mahum Warrior
(854, 4297, 1), -- Race
(854, 4071, 3), -- Resist Archery
(854, 4085, 1), -- Critical Power
(854, 4086, 1), -- Critical Chance
(854, 4090, 1), -- NPC Wolf Stun
(854, 4033, 6), -- NPC Burn
(854, 4073, 6), -- Shock
-- Kel Mahum Archer
(855, 4297, 1), -- Race
(855, 4095, 1), -- Damage Shield
(855, 4086, 1), -- Critical Chance
(855, 4040, 6), -- NPC Bow Attack
-- Kel Mahum Shaman
(856, 4297, 1), -- Race
(856, 4116, 4), -- Resist M. Atk.
(856, 4087, 6), -- NPC Blaze
(856, 4038, 5), -- Decrease Atk. Spd.
(856, 4047, 6), -- Hold
-- Kel Mahum Champion
(857, 4297, 1), -- Race
(857, 4071, 3), -- Resist Archery
(857, 4273, 2), -- Resist Dagger
(857, 4085, 1), -- Critical Power
(857, 4086, 1), -- Critical Chance
(857, 4285, 4), -- Resist Sleep
(857, 4287, 4), -- Resist Hold
(857, 4274, 1), -- Blunt Attack Weak Point
(857, 4077, 7), -- NPC Aura Burn
-- Angel
(858, 4297, 1), -- Race
(858, 4281, 2), -- Wind Attack Weak Point
(858, 4085, 1), -- Critical Power
(858, 4086, 1), -- Critical Chance
(858, 4091, 1), -- NPC Ogre Stun
(858, 4072, 7), -- Shock
(858, 4032, 7), -- NPC Strike
-- Guardian Angel
(859, 4297, 1), -- Race
(859, 4305, 1), -- Strong Type
(859, 4281, 2), -- Wind Attack Weak Point
(859, 4085, 1), -- Critical Power
(859, 4086, 1), -- Critical Chance
(859, 4046, 7), -- Sleep
(859, 4087, 7), -- NPC Blaze
(859, 4094, 7), -- NPC Cancel Magic
-- Seal Angel
(860, 4297, 1), -- Race
(860, 4306, 1), -- Strong Type
(860, 4281, 2), -- Wind Attack Weak Point
(860, 4085, 1), -- Critical Power
(860, 4086, 1), -- Critical Chance
(860, 4084, 4), -- Resist Physical Attack
(860, 4087, 7), -- NPC Blaze
(860, 4066, 7), -- NPC Twister
(860, 4118, 7), -- Paralysis
-- Oel Mahum Champion
(861, 4295, 1), -- Race
(861, 4071, 3), -- Resist Archery
(861, 4273, 2), -- Resist Dagger
(861, 4274, 1), -- Blunt Attack Weak Point
(861, 4085, 1), -- Critical Power
(861, 4086, 1), -- Critical Chance
(861, 4090, 1), -- NPC Wolf Stun
(861, 4033, 5), -- NPC Burn
(861, 4032, 5), -- NPC Strike
-- Death Lord
(862, 4290, 1), -- Race
(862, 4275, 3), -- Sacred Attack Weak Point
(862, 4278, 1), -- Dark Attack
(862, 4071, 3), -- Resist Archery
(862, 4273, 2), -- Resist Dagger
(862, 4274, 1), -- Blunt Attack Weak Point
(862, 4085, 1), -- Critical Power
(862, 4086, 1), -- Critical Chance
(862, 4034, 8), -- Decrease Speed
(862, 4078, 8), -- NPC Flamestrike
(862, 4118, 8), -- Paralysis
-- Magic Golem Gonhirim
(863, 4291, 1), -- Race
(863, 4071, 4), -- Resist Archery
(863, 4273, 2), -- Resist Dagger
(863, 4274, 1), -- Blunt Attack Weak Point
(863, 4077, 5), -- NPC Aura Burn
-- Gonhirim's Magic Golem
(864, 4291, 1), -- Race
(864, 4071, 4), -- Resist Archery
(864, 4273, 2), -- Resist Dagger
(864, 4274, 1), -- Blunt Attack Weak Point
(864, 4001, 4), -- NPC Windstrike
(864, 4065, 4), -- NPC Heal
-- Gonhirim's Golem
(865, 4291, 1), -- Race
(865, 4071, 4), -- Resist Archery
(865, 4273, 2), -- Resist Dagger
(865, 4274, 1), -- Blunt Attack Weak Point
-- Bandit Leader Barda
(866, 4295, 1), -- Race
(866, 4032, 5), -- NPC Strike
-- Barda's Bandit
(867, 4295, 1), -- Race
(867, 4032, 5), -- NPC Strike
-- Timak Orc Gosmos
(868, 4295, 1), -- Race
(868, 4071, 3), -- Resist Archery
(868, 4273, 2), -- Resist Dagger
(868, 4073, 5), -- Shock
-- Gosmos's Inferiors
(869, 4295, 1), -- Race
(869, 4317, 1), -- Increase Rage Might
-- Thief Kelbar
(870, 4295, 1), -- Race
(870, 4071, 3), -- Resist Archery
(870, 4032, 4), -- NPC Strike
-- Kelbar's Inferiors
(871, 4295, 1), -- Race
-- Evil Creature Cyrion
(872, 4299, 1), -- Race
(872, 4071, 3), -- Resist Archery
(872, 4072, 5), -- Shock
-- Evil Creatures of Forest
(873, 4293, 1), -- Race
(873, 4073, 4), -- Shock
-- Enmity Ghost Ramdal
(874, 4290, 1), -- Race
(874, 4275, 3), -- Sacred Attack Weak Point
(874, 4278, 1), -- Dark Attack
(874, 4285, 4), -- Resist Sleep
(874, 4087, 6), -- NPC Blaze
-- Enmity Ghosts
(875, 4290, 1), -- Race
(875, 4278, 1), -- Dark Attack
(875, 4333, 3), -- Resist Dark Attack
(875, 4099, 2), -- NPC Berserk
-- Immortal Savior Mardil
(876, 4298, 1), -- Race
(876, 4278, 1), -- Dark Attack
(876, 4333, 3), -- Resist Dark Attack
(876, 4085, 1), -- Critical Power
(876, 4086, 1), -- Critical Chance
(876, 4033, 8), -- NPC Burn
-- Immortal Savior
(877, 4290, 1), -- Race
(877, 4275, 3), -- Sacred Attack Weak Point
(877, 4278, 1), -- Dark Attack
(877, 4099, 2), -- NPC Berserk
-- Cherub Galaxia
(878, 4291, 1), -- Race
(878, 4071, 3), -- Resist Archery
(878, 4033, 9), -- NPC Burn
-- Galaxia's Escort
(879, 4297, 1), -- Race
(879, 4071, 3), -- Resist Archery
(879, 4163, 9), -- NPC Self Damage Shield
-- Cherub's Messenger
(880, 4297, 1), -- Race
(880, 4071, 3), -- Resist Archery
(880, 4066, 9), -- NPC Twister
(880, 4097, 9), -- NPC Chant of Life
-- Meanas Anor
(881, 4299, 1), -- Race
(881, 4071, 3), -- Resist Archery
(881, 4078, 6), -- NPC Flamestrike
-- Marsh's Wyvern
(882, 4299, 1), -- Race
(882, 4071, 3), -- Resist Archery
(882, 4067, 5), -- NPC Mortal Blow
-- Oblivion's Mirror
(883, 4291, 1), -- Race
(883, 4095, 1), -- Damage Shield
(883, 4033, 5), -- NPC Burn
-- Deprived Men
(884, 4290, 1), -- Race
(884, 4275, 3), -- Sacred Attack Weak Point
(884, 4278, 1), -- Dark Attack
(884, 4093, 1), -- Evasion
(884, 4065, 5), -- NPC Heal
-- Deprived Men
(885, 4290, 1), -- Race
(885, 4275, 3), -- Sacred Attack Weak Point
(885, 4278, 1), -- Dark Attack
(885, 4093, 1), -- Evasion
(885, 4065, 5), -- NPC Heal
(885, 4098, 5), -- Magic Skill Block
-- Deprived Men
(886, 4290, 1), -- Race
(886, 4275, 3), -- Sacred Attack Weak Point
(886, 4278, 1), -- Dark Attack
(886, 4093, 1), -- Evasion
(886, 4073, 5), -- Shock
-- Deadman Ereve
(887, 4290, 1), -- Race
(887, 4275, 3), -- Sacred Attack Weak Point
(887, 4278, 1), -- Dark Attack
(887, 4073, 6), -- Shock
-- Ereve's Knight
(888, 4290, 1), -- Race
(888, 4275, 3), -- Sacred Attack Weak Point
(888, 4278, 1), -- Dark Attack
(888, 4032, 5), -- NPC Strike
-- Ereve's Follower
(889, 4290, 1), -- Race
(889, 4275, 3), -- Sacred Attack Weak Point
(889, 4278, 1), -- Dark Attack
-- Harit Guardian Garangky
(890, 4293, 1), -- Race
(890, 4095, 1), -- Damage Shield
(890, 4073, 6), -- Shock
-- Garangky's Guard Leader
(891, 4295, 1), -- Race
(891, 4030, 3), -- Enhance P. Atk.
-- Garangky's Guard Shaman
(892, 4295, 1), -- Race
(892, 4065, 5), -- NPC Heal
(892, 4118, 5), -- Paralysis
-- Garangky's Guard
(893, 4295, 1), -- Race
-- Gorgolos
(894, 4291, 1), -- Race
(894, 4281, 2), -- Wind Attack Weak Point
(894, 4276, 1), -- Archery Attack Weak Point
(894, 4084, 4), -- Resist Physical Attack
(894, 4034, 6), -- Decrease Speed
-- Cursed Souls
(895, 4291, 1), -- Race
(895, 4281, 2), -- Wind Attack Weak Point
(895, 4276, 1), -- Archery Attack Weak Point
(895, 4287, 4), -- Resist Hold
(895, 4159, 6), -- NPC Straight Beam Cannon - Magic
(895, 4160, 6), -- NPC Aura Burn - Magic
-- Last Titan Utenus
(896, 4300, 1), -- Race
(896, 4071, 4), -- Resist Archery
(896, 4273, 2), -- Resist Dagger
(896, 4274, 1), -- Blunt Attack Weak Point
(896, 4032, 7), -- NPC Strike
-- Utenus' Guard
(897, 4291, 1), -- Race
(897, 4285, 4), -- Resist Sleep
(897, 4065, 7), -- NPC Heal
(897, 4117, 7), -- Paralysis
-- Grave Robber Kim
(898, 4295, 1), -- Race
(898, 4085, 1), -- Critical Power
(898, 4086, 1), -- Critical Chance
(898, 4073, 5), -- Shock
-- Kim's Gang
(899, 4295, 1), -- Race
-- Ghost Knight Kabed
(900, 4290, 1), -- Race
(900, 4275, 3), -- Sacred Attack Weak Point
(900, 4278, 1), -- Dark Attack
(900, 4032, 6), -- NPC Strike
-- Kabed's Soldiers
(901, 4290, 1), -- Race
(901, 4275, 3), -- Sacred Attack Weak Point
(901, 4278, 1), -- Dark Attack
(901, 4099, 2), -- NPC Berserk
-- Priest of Shilen, Hisilrome
(902, 4292, 1), -- Race
(902, 4157, 7), -- NPC Blaze - Magic
(902, 4160, 7), -- NPC Aura Burn - Magic
(902, 4097, 7), -- NPC Chant of Life
-- Hisilrome's Servitor
(903, 4292, 1), -- Race
(903, 4163, 6), -- NPC Self Damage Shield
-- Magician Kenishee
(904, 4298, 1), -- Race
(904, 4278, 1), -- Dark Attack
(904, 4333, 3), -- Resist Dark Attack
(904, 4100, 5), -- NPC Prominence
-- Kenishee's Servitor
(905, 4298, 1), -- Race
(905, 4278, 1), -- Dark Attack
(905, 4333, 3), -- Resist Dark Attack
(905, 4065, 5), -- NPC Heal
(905, 4088, 5), -- Bleed
-- Zaken's Chief Mate Tillion
(906, 4290, 1), -- Race
(906, 4275, 3), -- Sacred Attack Weak Point
(906, 4278, 1), -- Dark Attack
(906, 4085, 1), -- Critical Power
(906, 4086, 1), -- Critical Chance
(906, 4032, 6), -- NPC Strike
-- Tilion's Inferiors
(907, 4290, 1), -- Race
(907, 4275, 3), -- Sacred Attack Weak Point
(907, 4278, 1), -- Dark Attack
(907, 4163, 6), -- NPC Self Damage Shield
-- Tilion's Bat
(908, 4292, 1), -- Race
(908, 4281, 2), -- Wind Attack Weak Point
(908, 4276, 1), -- Archery Attack Weak Point
(908, 4065, 6), -- NPC Heal
(908, 4118, 6), -- Paralysis
-- Water Spirit Lian
(909, 4302, 1), -- Race
(909, 4010, 3), -- Resist Water
(909, 4279, 2), -- Fire Attack Weak Point
(909, 4066, 3), -- NPC Twister
-- Lian's Water Spirit
(910, 4302, 1), -- Race
(910, 4010, 3), -- Resist Water
(910, 4279, 2), -- Fire Attack Weak Point
(910, 4065, 3), -- NPC Heal
(910, 4038, 4), -- Decrease Atk. Spd.
-- Gwindorr
(911, 4292, 1), -- Race
(911, 4067, 4), -- NPC Mortal Blow
-- Gwindorr's Snake
(912, 4292, 1), -- Race
-- Eva's Spirit Niniel
(913, 4293, 1), -- Race
(913, 4001, 5), -- NPC Windstrike
-- Niniel's Spirits
(914, 4302, 1), -- Race
(914, 4074, 2), -- NPC Haste
-- Pingolpin
(915, 4301, 1), -- Race
(915, 4085, 1), -- Critical Power
(915, 4086, 1), -- Critical Chance
(915, 4287, 4), -- Resist Hold
(915, 4279, 2), -- Fire Attack Weak Point
(915, 4073, 5), -- Shock
-- Fafurion's Envoy
(916, 4301, 1), -- Race
(916, 4163, 4), -- NPC Self Damage Shield
-- Istary
(917, 4292, 1), -- Race
(917, 4001, 5), -- NPC Windstrike
-- Istary's Alligators
(918, 4292, 1), -- Race
(918, 4073, 4), -- Shock
-- Maille Lizardman
(919, 4295, 1), -- Race
(919, 4311, 1), -- Feeble Type
-- Maille Lizardman Scout
(920, 4295, 1), -- Race
(920, 4311, 1), -- Feeble Type
(920, 4124, 2), -- NPC Spear Attack
-- Maille Lizardman Guard
(921, 4295, 1), -- Race
(921, 4311, 1), -- Feeble Type
(921, 4071, 3), -- Resist Archery
-- Maille Lizardman Warrior
(922, 4295, 1), -- Race
(922, 4311, 1), -- Feeble Type
-- Maille Lizardman Shaman
(923, 4295, 1), -- Race
(923, 4311, 1), -- Feeble Type
(923, 4160, 2), -- NPC Aura Burn - Magic
(923, 4257, 2), -- NPC Hydroblast - Magic
(923, 4065, 2), -- NPC Heal
-- Maille Lizardman Matriarch
(924, 4295, 1), -- Race
(924, 4311, 1), -- Feeble Type
(924, 4071, 3), -- Resist Archery
(924, 4032, 3), -- NPC Strike
-- Giant Araneid
(925, 4301, 1), -- Race
(925, 4311, 1), -- Feeble Type
(925, 4031, 2), -- Enhance P. Def.
-- Poison Araneid
(926, 4301, 1), -- Race
(926, 4311, 1), -- Feeble Type
(926, 4036, 2), -- Poison
-- King of the Araneid
(927, 4301, 1), -- Race
(927, 4311, 1), -- Feeble Type
(927, 4071, 3), -- Resist Archery
(927, 4067, 2), -- NPC Mortal Blow
-- Hatu Weird Bee
(928, 4301, 1), -- Race
(928, 4311, 1), -- Feeble Type
(928, 4281, 2), -- Wind Attack Weak Point
(928, 4276, 1), -- Archery Attack Weak Point
(928, 4035, 3), -- Poison
-- Hatu Dire Wolf
(929, 4293, 1), -- Race
(929, 4311, 1), -- Feeble Type
(929, 4030, 2), -- Enhance P. Atk.
-- Hatu Brown Bear
(930, 4293, 1), -- Race
(930, 4311, 1), -- Feeble Type
(930, 4032, 3), -- NPC Strike
-- Hatu Onyx Beast
(931, 4292, 1), -- Race
(931, 4311, 1), -- Feeble Type
(931, 4576, 3), -- NPC Clan Buff - Damage Shield
-- Hatu Crimson Bear
(932, 4293, 1), -- Race
(932, 4311, 1), -- Feeble Type
(932, 4032, 3), -- NPC Strike
-- Hatu Windsus
(933, 4293, 1), -- Race
(933, 4311, 1), -- Feeble Type
(933, 4086, 1), -- Critical Chance
(933, 4073, 4), -- Shock
-- Wasp Worker
(934, 4301, 1), -- Race
(934, 4311, 1), -- Feeble Type
(934, 4030, 2), -- Enhance P. Atk.
-- Wasp Leader
(935, 4301, 1), -- Race
(935, 4311, 1), -- Feeble Type
(935, 4039, 3), -- NPC MP Drain
-- Tanor Silenos
(936, 4295, 1), -- Race
(936, 4311, 1), -- Feeble Type
-- Tanor Silenos Grunt
(937, 4295, 1), -- Race
(937, 4311, 1), -- Feeble Type
(937, 4073, 4), -- Shock
-- Tanor Silenos Scout
(938, 4295, 1), -- Race
(938, 4311, 1), -- Feeble Type
(938, 4071, 3), -- Resist Archery
(938, 4273, 2), -- Resist Dagger
(938, 4287, 4), -- Resist Hold
(938, 4032, 4), -- NPC Strike
-- Tanor Silenos Warrior
(939, 4295, 1), -- Race
(939, 4311, 1), -- Feeble Type
(939, 4071, 3), -- Resist Archery
(939, 4273, 2), -- Resist Dagger
(939, 4032, 4), -- NPC Strike
-- Tanor Silenos Shaman
(940, 4295, 1), -- Race
(940, 4311, 1), -- Feeble Type
(940, 4095, 1), -- Damage Shield
(940, 4285, 4), -- Resist Sleep
(940, 4065, 4), -- NPC Heal
(940, 4037, 2), -- Weaken P. Atk.
-- Tanor Silenos Chieftain
(941, 4295, 1), -- Race
(941, 4311, 1), -- Feeble Type
(941, 4071, 3), -- Resist Archery
(941, 4273, 2), -- Resist Dagger
(941, 4285, 4), -- Resist Sleep
(941, 4287, 4), -- Resist Hold
(941, 4032, 5), -- NPC Strike
-- Nightmare Guide
(942, 4292, 1), -- Race
(942, 4311, 1), -- Feeble Type
(942, 4099, 2), -- NPC Berserk
-- Nightmare Keeper
(943, 4292, 1), -- Race
(943, 4311, 1), -- Feeble Type
(943, 4100, 4), -- NPC Prominence
(943, 4097, 4), -- NPC Chant of Life
-- Nightmare Lord
(944, 4298, 1), -- Race
(944, 4311, 1), -- Feeble Type
(944, 4278, 1), -- Dark Attack
(944, 4333, 3), -- Resist Dark Attack
(944, 4087, 4), -- NPC Blaze
-- Cadeine
(945, 4295, 1), -- Race
(945, 4311, 1), -- Feeble Type
(945, 4099, 2), -- NPC Berserk
-- Sanhidro
(946, 4292, 1), -- Race
(946, 4311, 1), -- Feeble Type
(946, 4067, 5), -- NPC Mortal Blow
-- Connabi
(947, 4295, 1), -- Race
(947, 4311, 1), -- Feeble Type
(947, 4032, 5), -- NPC Strike
-- Bartal
(948, 4291, 1), -- Race
(948, 4311, 1), -- Feeble Type
(948, 4100, 5), -- NPC Prominence
(948, 4065, 5), -- NPC Heal
-- Luminun
(949, 4295, 1), -- Race
(949, 4311, 1), -- Feeble Type
(949, 4085, 1), -- Critical Power
(949, 4032, 5), -- NPC Strike
-- Innersen
(950, 4295, 1), -- Race
(950, 4311, 1), -- Feeble Type
(950, 4073, 6), -- Shock
-- Pobby's Maid
(951, 4294, 1), -- Race
(951, 4311, 1), -- Feeble Type
(951, 4071, 3), -- Resist Archery
(951, 4279, 2), -- Fire Attack Weak Point
(951, 4277, 3), -- Resist Poison
(951, 4163, 5), -- NPC Self Damage Shield
-- Pobby Escort
(952, 4294, 1), -- Race
(952, 4311, 1), -- Feeble Type
(952, 4071, 3), -- Resist Archery
(952, 4279, 2), -- Fire Attack Weak Point
(952, 4277, 3), -- Resist Poison
(952, 4077, 5), -- NPC Aura Burn
-- Pobby
(953, 4294, 1), -- Race
(953, 4311, 1), -- Feeble Type
(953, 4071, 3), -- Resist Archery
(953, 4279, 2), -- Fire Attack Weak Point
(953, 4277, 3), -- Resist Poison
(953, 4073, 5), -- Shock
-- Hungry Corpse
(954, 4290, 1), -- Race
(954, 4311, 1), -- Feeble Type
(954, 4275, 3), -- Sacred Attack Weak Point
(954, 4278, 1), -- Dark Attack
(954, 4071, 3), -- Resist Archery
(954, 4274, 1), -- Blunt Attack Weak Point
(954, 4073, 6), -- Shock
-- Ghost War
(955, 4290, 1), -- Race
(955, 4311, 1), -- Feeble Type
(955, 4275, 3), -- Sacred Attack Weak Point
(955, 4278, 1), -- Dark Attack
(955, 4071, 3), -- Resist Archery
(955, 4274, 1), -- Blunt Attack Weak Point
(955, 4116, 8), -- Resist M. Atk.
(955, 4073, 6), -- Shock
-- Past Knight
(956, 4290, 1), -- Race
(956, 4311, 1), -- Feeble Type
(956, 4275, 3), -- Sacred Attack Weak Point
(956, 4278, 1), -- Dark Attack
(956, 4274, 1), -- Blunt Attack Weak Point
(956, 4032, 6), -- NPC Strike
-- Nihil Invader
(957, 4290, 1), -- Race
(957, 4311, 1), -- Feeble Type
(957, 4275, 3), -- Sacred Attack Weak Point
(957, 4278, 1), -- Dark Attack
(957, 4071, 3), -- Resist Archery
(957, 4274, 1), -- Blunt Attack Weak Point
(957, 4032, 6), -- NPC Strike
-- Death Blade
(958, 4290, 1), -- Race
(958, 4311, 1), -- Feeble Type
(958, 4275, 3), -- Sacred Attack Weak Point
(958, 4278, 1), -- Dark Attack
(958, 4071, 3), -- Resist Archery
(958, 4099, 2), -- NPC Berserk
-- Dark Guard
(959, 4290, 1), -- Race
(959, 4311, 1), -- Feeble Type
(959, 4275, 3), -- Sacred Attack Weak Point
(959, 4278, 1), -- Dark Attack
(959, 4116, 6), -- Resist M. Atk.
(959, 4073, 7), -- Shock
-- Bloody Ghost
(960, 4298, 1), -- Race
(960, 4311, 1), -- Feeble Type
(960, 4275, 3), -- Sacred Attack Weak Point
(960, 4278, 1), -- Dark Attack
(960, 4099, 2), -- NPC Berserk
-- Bloody Knight
(961, 4298, 1), -- Race
(961, 4311, 1), -- Feeble Type
(961, 4278, 1), -- Dark Attack
(961, 4333, 3), -- Resist Dark Attack
(961, 4073, 6), -- Shock
-- Bloody Priest
(962, 4298, 1), -- Race
(962, 4311, 1), -- Feeble Type
(962, 4278, 1), -- Dark Attack
(962, 4333, 3), -- Resist Dark Attack
(962, 4065, 6), -- NPC Heal
(962, 4076, 3), -- Reduction in movement speed
-- Bloody Lord
(963, 4298, 1), -- Race
(963, 4311, 1), -- Feeble Type
(963, 4278, 1), -- Dark Attack
(963, 4333, 3), -- Resist Dark Attack
(963, 4086, 1), -- Critical Chance
(963, 4116, 8), -- Resist M. Atk.
(963, 4100, 6), -- NPC Prominence
-- Huge Footmark
(964, 4291, 1), -- Race
(964, 4311, 1), -- Feeble Type
(964, 4071, 4), -- Resist Archery
(964, 4273, 2), -- Resist Dagger
(964, 4274, 1), -- Blunt Attack Weak Point
(964, 4116, 8), -- Resist M. Atk.
(964, 4284, 3), -- Resist Bleeding
(964, 4250, 7), -- NPC Twister - Slow
-- Chimera Piece
(965, 4293, 1), -- Race
(965, 4311, 1), -- Feeble Type
(965, 4073, 7), -- Shock
-- Black Shadow
(966, 4299, 1), -- Race
(966, 4311, 1), -- Feeble Type
(966, 4281, 2), -- Wind Attack Weak Point
(966, 4276, 1), -- Archery Attack Weak Point
(966, 4072, 7), -- Shock
-- Past Creature
(967, 4291, 1), -- Race
(967, 4311, 1), -- Feeble Type
(967, 4032, 8), -- NPC Strike
-- Nonexistent Man
(968, 4291, 1), -- Race
(968, 4311, 1), -- Feeble Type
(968, 4065, 8), -- NPC Heal
(968, 4037, 2), -- Weaken P. Atk.
-- Giant's Shadow
(969, 4300, 1), -- Race
(969, 4311, 1), -- Feeble Type
(969, 4071, 3), -- Resist Archery
(969, 4086, 1), -- Critical Chance
(969, 4105, 8), -- NPC Straight Beam Cannon
-- Soldier of Ancient Times
(970, 4300, 1), -- Race
(970, 4311, 1), -- Feeble Type
(970, 4030, 3), -- Enhance P. Atk.
-- Warrior of Ancient Times
(971, 4300, 1), -- Race
(971, 4311, 1), -- Feeble Type
(971, 4073, 8), -- Shock
-- Shaman of Ancient Times
(972, 4300, 1), -- Race
(972, 4311, 1), -- Feeble Type
(972, 4156, 8), -- NPC Curve Beam Cannon - Magic
(972, 4160, 8), -- NPC Aura Burn - Magic
(972, 4076, 3), -- Reduction in movement speed
-- Forgotten Ancient People
(973, 4300, 1), -- Race
(973, 4311, 1), -- Feeble Type
(973, 4071, 3), -- Resist Archery
(973, 4033, 8), -- NPC Burn
-- Spiteful Soul Leader
(974, 4290, 1), -- Race
(974, 4275, 3), -- Sacred Attack Weak Point
(974, 4278, 1), -- Dark Attack
(974, 4274, 1), -- Blunt Attack Weak Point
(974, 4073, 6), -- Shock
-- Spiteful Soul Wizard
(975, 4290, 1), -- Race
(975, 4275, 3), -- Sacred Attack Weak Point
(975, 4278, 1), -- Dark Attack
(975, 4087, 6), -- NPC Blaze
(975, 4065, 6), -- NPC Heal
-- Spiteful Soul Fighter
(976, 4290, 1), -- Race
(976, 4275, 3), -- Sacred Attack Weak Point
(976, 4278, 1), -- Dark Attack
(976, 4099, 2), -- NPC Berserk
-- Elmo-Aden's Lady
(977, 4298, 1), -- Race
(977, 4303, 1), -- Strong Type
(977, 4278, 1), -- Dark Attack
(977, 4333, 3), -- Resist Dark Attack
(977, 4116, 4), -- Resist M. Atk.
(977, 4285, 4), -- Resist Sleep
(977, 4287, 4), -- Resist Hold
(977, 4087, 6), -- NPC Blaze
-- Elmo-Aden's Archer Escort
(978, 4290, 1), -- Race
(978, 4303, 1), -- Strong Type
(978, 4275, 3), -- Sacred Attack Weak Point
(978, 4278, 1), -- Dark Attack
(978, 4071, 3), -- Resist Archery
(978, 4285, 4), -- Resist Sleep
(978, 4040, 6), -- NPC Bow Attack
-- Elmo-Aden's Maid
(979, 4298, 1), -- Race
(979, 4303, 1), -- Strong Type
(979, 4278, 1), -- Dark Attack
(979, 4333, 3), -- Resist Dark Attack
(979, 4116, 4), -- Resist M. Atk.
(979, 4099, 2), -- NPC Berserk
-- Hallate's Follower Mul
(980, 4298, 1), -- Race
(980, 4303, 1), -- Strong Type
(980, 4278, 1), -- Dark Attack
(980, 4333, 3), -- Resist Dark Attack
(980, 4085, 1), -- Critical Power
(980, 4086, 1), -- Critical Chance
(980, 4073, 6), -- Shock
-- Mul's Wizard
(981, 4298, 1), -- Race
(981, 4303, 1), -- Strong Type
(981, 4278, 1), -- Dark Attack
(981, 4333, 3), -- Resist Dark Attack
(981, 4084, 4), -- Resist Physical Attack
(981, 4281, 2), -- Wind Attack Weak Point
(981, 4276, 1), -- Archery Attack Weak Point
(981, 4078, 6), -- NPC Flamestrike
(981, 4065, 6), -- NPC Heal
-- Mul's Knight
(982, 4298, 1), -- Race
(982, 4303, 1), -- Strong Type
(982, 4278, 1), -- Dark Attack
(982, 4333, 3), -- Resist Dark Attack
(982, 4281, 2), -- Wind Attack Weak Point
(982, 4276, 1), -- Archery Attack Weak Point
(982, 4118, 6), -- Paralysis
-- Binder
(983, 4297, 1), -- Race
(983, 4303, 1), -- Strong Type
(983, 4084, 4), -- Resist Physical Attack
(983, 4073, 7), -- Shock
-- Bound Warrior
(984, 4297, 1), -- Race
(984, 4303, 1), -- Strong Type
(984, 4116, 4), -- Resist M. Atk.
(984, 4287, 4), -- Resist Hold
(984, 4073, 7), -- Shock
-- Bound Archer
(985, 4297, 1), -- Race
(985, 4303, 1), -- Strong Type
(985, 4071, 3), -- Resist Archery
(985, 4085, 1), -- Critical Power
(985, 4086, 1), -- Critical Chance
(985, 4120, 7), -- Shock
-- Sairon
(986, 4298, 1), -- Race
(986, 4303, 1), -- Strong Type
(986, 4278, 1), -- Dark Attack
(986, 4333, 3), -- Resist Dark Attack
(986, 4071, 3), -- Resist Archery
(986, 4033, 5), -- NPC Burn
-- Sairon's Doll
(987, 4298, 1), -- Race
(987, 4303, 1), -- Strong Type
(987, 4278, 1), -- Dark Attack
(987, 4333, 3), -- Resist Dark Attack
(987, 4281, 2), -- Wind Attack Weak Point
(987, 4276, 1), -- Archery Attack Weak Point
(987, 4087, 5), -- NPC Blaze
(987, 4065, 5), -- NPC Heal
-- Sairon's Puppet
(988, 4298, 1), -- Race
(988, 4303, 1), -- Strong Type
(988, 4278, 1), -- Dark Attack
(988, 4333, 3), -- Resist Dark Attack
(988, 4281, 2), -- Wind Attack Weak Point
(988, 4276, 1), -- Archery Attack Weak Point
(988, 4097, 5), -- NPC Chant of Life
(988, 4117, 5), -- Paralysis
-- Lageos
(989, 4301, 1), -- Race
(989, 4281, 2), -- Wind Attack Weak Point
(989, 4276, 1), -- Archery Attack Weak Point
(989, 4067, 3), -- NPC Mortal Blow
-- Pageos
(990, 4301, 1), -- Race
(990, 4097, 3), -- NPC Chant of Life
-- Swamp Tribe
(991, 4292, 1), -- Race
(991, 4303, 1), -- Strong Type
(991, 4085, 1), -- Critical Power
(991, 4086, 1), -- Critical Chance
(991, 4073, 4), -- Shock
-- Swamp Alligator
(992, 4292, 1), -- Race
(992, 4303, 1), -- Strong Type
(992, 4067, 4), -- NPC Mortal Blow
-- Swamp Warrior
(993, 4292, 1), -- Race
(993, 4303, 1), -- Strong Type
(993, 4228, 4), -- NPC Double Dagger Attack
-- Garden Guard Leader
(994, 4290, 1), -- Race
(994, 4303, 1), -- Strong Type
(994, 4275, 3), -- Sacred Attack Weak Point
(994, 4278, 1), -- Dark Attack
(994, 4085, 1), -- Critical Power
(994, 4086, 1), -- Critical Chance
(994, 4084, 7), -- Resist Physical Attack
(994, 4073, 5), -- Shock
-- Garden Guard
(995, 4296, 1), -- Race
(995, 4303, 1), -- Strong Type
(995, 4281, 2), -- Wind Attack Weak Point
(995, 4276, 1), -- Archery Attack Weak Point
(995, 4067, 4), -- NPC Mortal Blow
-- Spiteful Ghost of Ruins
(996, 4290, 1), -- Race
(996, 4275, 3), -- Sacred Attack Weak Point
(996, 4278, 1), -- Dark Attack
(996, 4085, 1), -- Critical Power
(996, 4071, 3), -- Resist Archery
(996, 4116, 8), -- Resist M. Atk.
(996, 4284, 3), -- Resist Bleeding
(996, 4034, 5), -- Decrease Speed
(996, 4254, 5), -- NPC Prominence - Slow
-- Soldier of Grief
(997, 4290, 1), -- Race
(997, 4275, 3), -- Sacred Attack Weak Point
(997, 4278, 1), -- Dark Attack
(997, 4034, 5), -- Decrease Speed
(997, 4001, 5), -- NPC Windstrike
-- Cruel Punishment
(998, 4290, 1), -- Race
(998, 4275, 3), -- Sacred Attack Weak Point
(998, 4278, 1), -- Dark Attack
(998, 4036, 5), -- Poison
-- Roving Soul
(999, 4290, 1), -- Race
(999, 4275, 3), -- Sacred Attack Weak Point
(999, 4278, 1), -- Dark Attack
(999, 4085, 1), -- Critical Power
(999, 4032, 5); -- NPC Strike

INSERT INTO npcskills VALUES
-- Soul of Ruins
(1000, 4290, 1), -- Race
(1000, 4275, 3), -- Sacred Attack Weak Point
(1000, 4278, 1), -- Dark Attack
(1000, 4002, 5), -- NPC HP Drain
-- Wretched Archer
(1001, 4290, 1), -- Race
(1001, 4275, 3), -- Sacred Attack Weak Point
(1001, 4278, 1), -- Dark Attack
(1001, 4085, 1), -- Critical Power
(1001, 4086, 1), -- Critical Chance
(1001, 4120, 5), -- Shock
-- Doom Scout
(1002, 4290, 1), -- Race
(1002, 4275, 3), -- Sacred Attack Weak Point
(1002, 4278, 1), -- Dark Attack
(1002, 4074, 2), -- NPC Haste
-- Grave Lich
(1003, 4291, 1), -- Race
(1003, 4275, 3), -- Sacred Attack Weak Point
(1003, 4278, 1), -- Dark Attack
(1003, 4035, 5), -- Poison
(1003, 4046, 5), -- Sleep
(1003, 4002, 5), -- NPC HP Drain
(1003, 4094, 5), -- NPC Cancel Magic
-- Dismal Pole
(1004, 4294, 1), -- Race
(1004, 4275, 3), -- Sacred Attack Weak Point
(1004, 4278, 1), -- Dark Attack
(1004, 4279, 2), -- Fire Attack Weak Point
(1004, 4277, 3), -- Resist Poison
(1004, 4071, 3), -- Resist Archery
(1004, 4028, 3), -- Enhance P. Atk.
-- Grave Predator
(1005, 4301, 1), -- Race
(1005, 4074, 2), -- NPC Haste
-- Doom Servant
(1006, 4290, 1), -- Race
(1006, 4275, 3), -- Sacred Attack Weak Point
(1006, 4278, 1), -- Dark Attack
(1006, 4085, 1), -- Critical Power
(1006, 4034, 6), -- Decrease Speed
(1006, 4100, 6), -- NPC Prominence
(1006, 4002, 6), -- NPC HP Drain
(1006, 4038, 5), -- Decrease Atk. Spd.
-- Doom Guard
(1007, 4290, 1), -- Race
(1007, 4275, 3), -- Sacred Attack Weak Point
(1007, 4278, 1), -- Dark Attack
(1007, 4085, 1), -- Critical Power
(1007, 4036, 6), -- Poison
-- Doom Archer
(1008, 4290, 1), -- Race
(1008, 4275, 3), -- Sacred Attack Weak Point
(1008, 4278, 1), -- Dark Attack
(1008, 4085, 1), -- Critical Power
(1008, 4120, 6), -- Shock
-- Doom Trooper
(1009, 4290, 1), -- Race
(1009, 4275, 3), -- Sacred Attack Weak Point
(1009, 4278, 1), -- Dark Attack
(1009, 4072, 6), -- Shock
(1009, 4091, 1), -- NPC Ogre Stun
(1009, 4244, 6), -- NPC Wild Sweep
-- Doom Warrior
(1010, 4290, 1), -- Race
(1010, 4275, 3), -- Sacred Attack Weak Point
(1010, 4278, 1), -- Dark Attack
(1010, 4071, 3), -- Resist Archery
(1010, 4085, 1), -- Critical Power
(1010, 4030, 3), -- Enhance P. Atk.
-- Ol Mahum Van Grunt
(1011, 4295, 1), -- Race
-- Magical Eye
(1012, 4291, 1), -- Race
(1012, 4116, 4), -- Resist M. Atk.
(1012, 4281, 2), -- Wind Attack Weak Point
(1012, 4276, 1), -- Archery Attack Weak Point
(1012, 4088, 3), -- Bleed
-- Ol Mahum Van Archer
(1013, 4295, 1), -- Race
(1013, 4040, 3), -- NPC Bow Attack
-- Lesser Warlike Tyrant
(1014, 4292, 1), -- Race
(1014, 4085, 1), -- Critical Power
(1014, 4086, 1), -- Critical Chance
(1014, 4279, 2), -- Fire Attack Weak Point
(1014, 4067, 3), -- NPC Mortal Blow
-- Ol Mahum Van Leader
(1015, 4295, 1), -- Race
(1015, 4071, 3), -- Resist Archery
(1015, 4032, 3), -- NPC Strike
-- Warlike Tyrant
(1016, 4292, 1), -- Race
(1016, 4085, 1), -- Critical Power
(1016, 4086, 1), -- Critical Chance
(1016, 4279, 2), -- Fire Attack Weak Point
(1016, 4067, 3), -- NPC Mortal Blow
-- Fallen Orc
(1017, 4295, 1), -- Race
-- Ancient Gargoyle
(1018, 4291, 1), -- Race
(1018, 4071, 2), -- Resist Archery
(1018, 4281, 2), -- Wind Attack Weak Point
(1018, 4067, 5), -- NPC Mortal Blow
-- Fallen Orc Archer
(1019, 4295, 1), -- Race
(1019, 4071, 2), -- Resist Archery
(1019, 4040, 5), -- NPC Bow Attack
-- Fallen Orc Shaman
(1020, 4295, 1), -- Race
(1020, 4095, 1), -- Damage Shield
(1020, 4001, 5), -- NPC Windstrike
(1020, 4066, 5), -- NPC Twister
(1020, 4035, 5), -- Poison
-- Sharp Talon Tiger
(1021, 4293, 1), -- Race
(1021, 4085, 1), -- Critical Power
(1021, 4086, 1), -- Critical Chance
(1021, 4073, 5), -- Shock
-- Fallen Orc Captain
(1022, 4295, 1), -- Race
(1022, 4085, 1), -- Critical Power
(1022, 4032, 6), -- NPC Strike
-- Sobbing Wind
(1023, 4296, 1), -- Race
(1023, 4276, 1), -- Archery Attack Weak Point
(1023, 4011, 3), -- Resist Wind
(1023, 4282, 2), -- Earth Attack Weak Point
(1023, 4046, 2), -- Sleep
-- Babbling Wind
(1024, 4296, 1), -- Race
(1024, 4276, 1), -- Archery Attack Weak Point
(1024, 4011, 3), -- Resist Wind
(1024, 4282, 2), -- Earth Attack Weak Point
(1024, 4151, 2), -- NPC Windstrike - Magic
(1024, 4160, 2), -- NPC Aura Burn - Magic
-- Giggling Wind
(1025, 4296, 1), -- Race
(1025, 4276, 1), -- Archery Attack Weak Point
(1025, 4011, 3), -- Resist Wind
(1025, 4282, 2), -- Earth Attack Weak Point
(1025, 4001, 2), -- NPC Windstrike
(1025, 4098, 2), -- Magic Skill Block
(1025, 4047, 2), -- Hold
-- Singing Wind
(1026, 4296, 1), -- Race
(1026, 4276, 1), -- Archery Attack Weak Point
(1026, 4011, 3), -- Resist Wind
(1026, 4282, 2), -- Earth Attack Weak Point
(1026, 4001, 2), -- NPC Windstrike
(1026, 4066, 2), -- NPC Twister
(1026, 4038, 3), -- Decrease Atk. Spd.
-- Bear Prince Malcolm
(1027, 4293, 1), -- Race
-- Succubus Handmaiden
(1028, 4298, 1), -- Race
(1028, 4278, 1), -- Dark Attack
(1028, 4333, 3), -- Resist Dark Attack
(1028, 4071, 3), -- Resist Archery
-- Goblin Servant
(1029, 4295, 1), -- Race
-- Julie The Ripper 
(1030, 4298, 1), -- Race
(1030, 4278, 1), -- Dark Attack
(1030, 4333, 3), -- Resist Dark Attack
(1030, 4071, 3), -- Resist Archery
(1030, 4085, 1), -- Critical Power
(1030, 4086, 1), -- Critical Chance
-- Lesser Noble Ant
(1031, 4301, 1), -- Race
(1031, 4279, 2), -- Fire Attack Weak Point
-- Blueback Alligator
(1032, 4292, 1), -- Race
(1032, 4071, 3), -- Resist Archery
-- Jewel Alligator
(1033, 4292, 1), -- Race
(1033, 4071, 3), -- Resist Archery
-- Ogre
(1034, 4295, 1), -- Race
(1034, 4311, 1), -- Feeble Type
(1034, 4085, 1), -- Critical Power
(1034, 4225, 1), -- Resist Shock
(1034, 4599, 3), -- Decrease Speed
(1034, 4091, 1), -- NPC Ogre Stun
(1034, 4032, 3), -- NPC Strike
-- Catherok
(1035, 4301, 1), -- Race
(1035, 4303, 1), -- Strong Type
(1035, 4279, 2), -- Fire Attack Weak Point
(1035, 4085, 1), -- Critical Power
(1035, 4072, 4), -- Shock
-- Shindebarn
(1036, 4291, 1), -- Race
(1036, 4303, 1), -- Strong Type
(1036, 4093, 1), -- Evasion
(1036, 4085, 1), -- Critical Power
(1036, 4091, 1), -- NPC Ogre Stun
(1036, 4033, 4), -- NPC Burn
(1036, 4032, 4), -- NPC Strike
-- Ossiud
(1037, 4291, 1), -- Race
(1037, 4303, 1), -- Strong Type
(1037, 4095, 1), -- Damage Shield
-- Liangma
(1038, 4291, 1), -- Race
(1038, 4303, 1), -- Strong Type
(1038, 4093, 1), -- Evasion
(1038, 4154, 4), -- NPC MP Drain - Magic
(1038, 4160, 4), -- NPC Aura Burn - Magic
-- Snipe Cohort
(1039, 4292, 1), -- Race
(1039, 4303, 1), -- Strong Type
(1039, 4078, 4), -- NPC Flamestrike
(1039, 4065, 4), -- NPC Heal
-- Soldier of Darkness
(1040, 4290, 1), -- Race
(1040, 4303, 1), -- Strong Type
(1040, 4275, 3), -- Sacred Attack Weak Point
(1040, 4278, 1), -- Dark Attack
(1040, 4084, 6), -- Resist Physical Attack
(1040, 4065, 4), -- NPC Heal
(1040, 4038, 4), -- Decrease Atk. Spd.
-- Kusion Suscepter
(1041, 4291, 1), -- Race
(1041, 4071, 4), -- Resist Archery
(1041, 4273, 2), -- Resist Dagger
(1041, 4274, 1), -- Blunt Attack Weak Point
(1041, 4031, 3), -- Enhance P. Def.
-- Treasure Chest
(1042, 4291, 1), -- Race
-- Treasure Chest
(1043, 4291, 1), -- Race
-- Treasure Chest
(1044, 4291, 1), -- Race
-- Treasure Chest
(1045, 4291, 1), -- Race
-- Treasure Chest
(1046, 4291, 1), -- Race
-- Treasure Chest
(1047, 4291, 1), -- Race
-- Treasure Chest
(1048, 4291, 1), -- Race
-- Treasure Chest
(1049, 4291, 1), -- Race
-- Treasure Chest
(1050, 4291, 1), -- Race
-- Treasure Chest
(1051, 4291, 1), -- Race
-- Treasure Chest
(1052, 4291, 1), -- Race
-- Treasure Chest
(1053, 4291, 1), -- Race
-- Treasure Chest
(1054, 4291, 1), -- Race
-- Treasure Chest
(1055, 4291, 1), -- Race
-- Treasure Chest
(1056, 4291, 1), -- Race
-- Treasure Chest
(1057, 4291, 1), -- Race
-- Beast Lord
(1058, 4293, 1), -- Race
(1058, 4303, 1), -- Strong Type
(1058, 4085, 1), -- Critical Power
(1058, 4086, 1), -- Critical Chance
(1058, 4116, 4), -- Resist M. Atk.
(1058, 4072, 6), -- Shock
-- Beast Guardian
(1059, 4292, 1), -- Race
(1059, 4303, 1), -- Strong Type
(1059, 4281, 2), -- Wind Attack Weak Point
(1059, 4276, 1), -- Archery Attack Weak Point
(1059, 4039, 5), -- NPC MP Drain
(1059, 4097, 5), -- NPC Chant of Life
-- Beast Seer
(1060, 4291, 1), -- Race
(1060, 4303, 1), -- Strong Type
(1060, 4281, 2), -- Wind Attack Weak Point
(1060, 4276, 1), -- Archery Attack Weak Point
(1060, 4065, 5), -- NPC Heal
(1060, 4098, 5), -- Magic Skill Block
-- Hallate's Guardian
(1061, 4298, 1), -- Race
(1061, 4305, 1), -- Strong Type
(1061, 4278, 1), -- Dark Attack
(1061, 4333, 3), -- Resist Dark Attack
(1061, 4084, 4), -- Resist Physical Attack
(1061, 4072, 6), -- Shock
(1061, 4090, 1), -- NPC Wolf Stun
(1061, 4032, 6), -- NPC Strike
-- Messenger Angel
(1062, 4297, 1), -- Race
(1062, 4305, 1), -- Strong Type
(1062, 4281, 2), -- Wind Attack Weak Point
(1062, 4085, 1), -- Critical Power
(1062, 4086, 1), -- Critical Chance
(1062, 4084, 4), -- Resist Physical Attack
(1062, 4033, 7), -- NPC Burn
(1062, 4092, 1), -- NPC Puma Stun
(1062, 4073, 7), -- Shock
-- Messenger Angel
(1063, 4297, 1), -- Race
(1063, 4305, 1), -- Strong Type
(1063, 4281, 2), -- Wind Attack Weak Point
(1063, 4085, 1), -- Critical Power
(1063, 4086, 1), -- Critical Chance
(1063, 4084, 4), -- Resist Physical Attack
(1063, 4066, 7), -- NPC Twister
(1063, 4098, 7), -- Magic Skill Block
(1063, 4047, 7), -- Hold
-- Platinum Guardian Archer
(1064, 4297, 1), -- Race
(1064, 4305, 1), -- Strong Type
(1064, 4095, 1), -- Damage Shield
(1064, 4086, 1), -- Critical Chance
(1064, 4040, 7), -- NPC Bow Attack
-- Platinum Guardian Warrior
(1065, 4297, 1), -- Race
(1065, 4306, 1), -- Strong Type
(1065, 4071, 3), -- Resist Archery
(1065, 4085, 1), -- Critical Power
(1065, 4086, 1), -- Critical Chance
(1065, 4073, 7), -- Shock
-- Platinum Guardian Shaman
(1066, 4297, 1), -- Race
(1066, 4306, 1), -- Strong Type
(1066, 4116, 4), -- Resist M. Atk.
(1066, 4100, 7), -- NPC Prominence
(1066, 4039, 7), -- NPC MP Drain
(1066, 4118, 7), -- Paralysis
-- Guardian Archangel
(1067, 4297, 1), -- Race
(1067, 4306, 1), -- Strong Type
(1067, 4281, 2), -- Wind Attack Weak Point
(1067, 4085, 1), -- Critical Power
(1067, 4086, 1), -- Critical Chance
(1067, 4084, 4), -- Resist Physical Attack
(1067, 4033, 7), -- NPC Burn
(1067, 4091, 1), -- NPC Ogre Stun
(1067, 4032, 7), -- NPC Strike
-- Guardian Archangel
(1068, 4297, 1), -- Race
(1068, 4306, 1), -- Strong Type
(1068, 4281, 2), -- Wind Attack Weak Point
(1068, 4085, 1), -- Critical Power
(1068, 4086, 1), -- Critical Chance
(1068, 4084, 4), -- Resist Physical Attack
(1068, 4046, 7), -- Sleep
(1068, 4087, 7), -- NPC Blaze
(1068, 4094, 7), -- NPC Cancel Magic
-- Platinum Guardian Prefect
(1069, 4297, 1), -- Race
(1069, 4306, 1), -- Strong Type
(1069, 4085, 1), -- Critical Power
(1069, 4086, 1), -- Critical Chance
(1069, 4084, 8), -- Resist Physical Attack
(1069, 4073, 7), -- Shock
-- Seal Archangel
(1070, 4297, 1), -- Race
(1070, 4306, 1), -- Strong Type
(1070, 4281, 2), -- Wind Attack Weak Point
(1070, 4085, 1), -- Critical Power
(1070, 4086, 1), -- Critical Chance
(1070, 4084, 4), -- Resist Physical Attack
(1070, 4033, 8), -- NPC Burn
(1070, 4092, 1), -- NPC Puma Stun
(1070, 4032, 8), -- NPC Strike
-- Seal Archangel
(1071, 4297, 1), -- Race
(1071, 4306, 1), -- Strong Type
(1071, 4281, 2), -- Wind Attack Weak Point
(1071, 4085, 1), -- Critical Power
(1071, 4086, 1), -- Critical Chance
(1071, 4084, 4), -- Resist Physical Attack
(1071, 4087, 8), -- NPC Blaze
(1071, 4066, 8), -- NPC Twister
(1071, 4118, 8), -- Paralysis
-- Platinum Guardian Chief
(1072, 4297, 1), -- Race
(1072, 4306, 1), -- Strong Type
(1072, 4116, 4), -- Resist M. Atk.
(1072, 4158, 8), -- NPC Prominence - Magic
(1072, 4160, 8), -- NPC Aura Burn - Magic
(1072, 4102, 2), -- Become weak against line of fire.
-- Guardian Warrior of Elmoreden
(1073, 4290, 1), -- Race
(1073, 4303, 1), -- Strong Type
(1073, 4275, 3), -- Sacred Attack Weak Point
(1073, 4278, 1), -- Dark Attack
(1073, 4032, 6), -- NPC Strike
-- Bound Shaman
(1074, 4297, 1), -- Race
(1074, 4303, 1), -- Strong Type
(1074, 4116, 4), -- Resist M. Atk.
(1074, 4285, 4), -- Resist Sleep
(1074, 4087, 7), -- NPC Blaze
(1074, 4065, 7), -- NPC Heal
-- Slaughter Bathin
(1075, 4290, 1), -- Race
(1075, 4303, 1), -- Strong Type
(1075, 4275, 3), -- Sacred Attack Weak Point
(1075, 4278, 1), -- Dark Attack
(1075, 4086, 1), -- Critical Chance
(1075, 4281, 2), -- Wind Attack Weak Point
(1075, 4084, 4), -- Resist Physical Attack
(1075, 4073, 6), -- Shock
-- Bathin's Knight
(1076, 4290, 1), -- Race
(1076, 4303, 1), -- Strong Type
(1076, 4275, 3), -- Sacred Attack Weak Point
(1076, 4278, 1), -- Dark Attack
(1076, 4084, 4), -- Resist Physical Attack
(1076, 4093, 1), -- Evasion
(1076, 4073, 6), -- Shock
-- Bathin's Wizard
(1077, 4298, 1), -- Race
(1077, 4303, 1), -- Strong Type
(1077, 4278, 1), -- Dark Attack
(1077, 4333, 3), -- Resist Dark Attack
(1077, 4116, 4), -- Resist M. Atk.
(1077, 4071, 3), -- Resist Archery
(1077, 4285, 4), -- Resist Sleep
(1077, 4065, 6), -- NPC Heal
(1077, 4118, 6), -- Paralysis
-- Magus Valac
(1078, 4298, 1), -- Race
(1078, 4303, 1), -- Strong Type
(1078, 4278, 1), -- Dark Attack
(1078, 4333, 3), -- Resist Dark Attack
(1078, 4084, 4), -- Resist Physical Attack
(1078, 4281, 2), -- Wind Attack Weak Point
(1078, 4276, 1), -- Archery Attack Weak Point
(1078, 4072, 7), -- Shock
-- Valac's Creature
(1079, 4292, 1), -- Race
(1079, 4303, 1), -- Strong Type
(1079, 4278, 1), -- Dark Attack
(1079, 4333, 3), -- Resist Dark Attack
(1079, 4281, 2), -- Wind Attack Weak Point
(1079, 4276, 1), -- Archery Attack Weak Point
(1079, 4285, 4), -- Resist Sleep
(1079, 4287, 4), -- Resist Hold
(1079, 4077, 6), -- NPC Aura Burn
-- Valac's Guardian Spirit
(1080, 4298, 1), -- Race
(1080, 4303, 1), -- Strong Type
(1080, 4278, 1), -- Dark Attack
(1080, 4333, 3), -- Resist Dark Attack
(1080, 4281, 2), -- Wind Attack Weak Point
(1080, 4276, 1), -- Archery Attack Weak Point
(1080, 4002, 6), -- NPC HP Drain
(1080, 4065, 6), -- NPC Heal
-- Power Angel Amon
(1081, 4297, 1), -- Race
(1081, 4303, 1), -- Strong Type
(1081, 4093, 1), -- Evasion
(1081, 4118, 8), -- Paralysis
-- Amon's Captain of the Guards
(1082, 4297, 1), -- Race
(1082, 4303, 1), -- Strong Type
(1082, 4281, 2), -- Wind Attack Weak Point
(1082, 4276, 1), -- Archery Attack Weak Point
(1082, 4086, 1), -- Critical Chance
(1082, 4032, 7), -- NPC Strike
-- Amon's Spirits
(1083, 4302, 1), -- Race
(1083, 4303, 1), -- Strong Type
(1083, 4084, 4), -- Resist Physical Attack
(1083, 4281, 2), -- Wind Attack Weak Point
(1083, 4276, 1), -- Archery Attack Weak Point
(1083, 4116, 4), -- Resist M. Atk.
(1083, 4065, 7), -- NPC Heal
(1083, 4098, 7), -- Magic Skill Block
-- Bloody Lady
(1084, 4298, 1), -- Race
(1084, 4304, 1), -- Strong Type
(1084, 4278, 1), -- Dark Attack
(1084, 4333, 3), -- Resist Dark Attack
(1084, 4039, 6), -- NPC MP Drain
(1084, 4035, 6), -- Poison
-- Bloody Sniper
(1085, 4298, 1), -- Race
(1085, 4306, 1), -- Strong Type
(1085, 4278, 1), -- Dark Attack
(1085, 4333, 3), -- Resist Dark Attack
(1085, 4071, 3), -- Resist Archery
(1085, 4389, 4), -- Resist Mental Derangement
(1085, 4141, 7), -- NPC Wind Fist
-- Bloody Liviona
(1086, 4291, 1), -- Race
(1086, 4306, 1), -- Strong Type
(1086, 4281, 2), -- Wind Attack Weak Point
(1086, 4276, 1), -- Archery Attack Weak Point
(1086, 4157, 7), -- NPC Blaze - Magic
(1086, 4160, 7), -- NPC Aura Burn - Magic
-- Bloody Knight
(1087, 4290, 1), -- Race
(1087, 4306, 1), -- Strong Type
(1087, 4275, 3), -- Sacred Attack Weak Point
(1087, 4278, 1), -- Dark Attack
(1087, 4085, 1), -- Critical Power
(1087, 4086, 1), -- Critical Chance
(1087, 4084, 8), -- Resist Physical Attack
(1087, 4033, 7), -- NPC Burn
(1087, 4092, 1), -- NPC Puma Stun
(1087, 4073, 7), -- Shock
-- Bloody Banshee
(1088, 4298, 1), -- Race
(1088, 4306, 1), -- Strong Type
(1088, 4278, 1), -- Dark Attack
(1088, 4333, 3), -- Resist Dark Attack
(1088, 4257, 7), -- NPC Hydroblast - Magic
(1088, 4160, 7), -- NPC Aura Burn - Magic
-- Bloody Lord
(1089, 4298, 1), -- Race
(1089, 4306, 1), -- Strong Type
(1089, 4278, 1), -- Dark Attack
(1089, 4333, 3), -- Resist Dark Attack
(1089, 4085, 1), -- Critical Power
(1089, 4086, 1), -- Critical Chance
(1089, 4072, 8), -- Shock
(1089, 4090, 1), -- NPC Wolf Stun
(1089, 4032, 8), -- NPC Strike
-- Bloody Guardian
(1090, 4290, 1), -- Race
(1090, 4304, 1), -- Strong Type
(1090, 4275, 3), -- Sacred Attack Weak Point
(1090, 4278, 1), -- Dark Attack
(1090, 4085, 1), -- Critical Power
(1090, 4086, 1), -- Critical Chance
(1090, 4152, 8), -- NPC HP Drain - Magic
(1090, 4160, 8), -- NPC Aura Burn - Magic
(1090, 4117, 8), -- Paralysis
-- Bloody Keeper
(1091, 4298, 1), -- Race
(1091, 4303, 1), -- Strong Type
(1091, 4278, 1), -- Dark Attack
(1091, 4333, 3), -- Resist Dark Attack
(1091, 4085, 1), -- Critical Power
(1091, 4086, 1), -- Critical Chance
(1091, 4032, 7), -- NPC Strike
-- Bloody Mystic
(1092, 4298, 1), -- Race
(1092, 4303, 1), -- Strong Type
(1092, 4278, 1), -- Dark Attack
(1092, 4333, 3), -- Resist Dark Attack
(1092, 4085, 1), -- Critical Power
(1092, 4086, 1), -- Critical Chance
(1092, 4285, 4), -- Resist Sleep
(1092, 4287, 4), -- Resist Hold
(1092, 4065, 7), -- NPC Heal
(1092, 4098, 7), -- Magic Skill Block
-- Ratman Sub Chieftain
(1093, 4295, 1), -- Race
-- Ratman Chieftain
(1094, 4295, 1), -- Race
-- Giant Poison Bee
(1095, 4301, 1), -- Race
(1095, 4035, 2), -- Poison
-- Cloudy Beast
(1096, 4292, 1), -- Race
-- Young Araneid
(1097, 4301, 1), -- Race
(1097, 4035, 2), -- Poison
-- Plain Grizzly
(1098, 4293, 1), -- Race
-- Cloudy Beast Turen
(1099, 4292, 1), -- Race
-- Langk Lizardman Sentinel
(1100, 4295, 1), -- Race
-- Langk Lizardman Shaman
(1101, 4295, 1), -- Race
(1101, 4152, 2), -- NPC HP Drain - Magic
(1101, 4160, 2), -- NPC Aura Burn - Magic
(1101, 4076, 1), -- Reduction in movement speed
-- Plain Watchman
(1102, 4291, 1), -- Race
(1102, 4281, 2), -- Wind Attack Weak Point
(1102, 4276, 1), -- Archery Attack Weak Point
(1102, 4151, 3), -- NPC Windstrike - Magic
(1102, 4160, 3), -- NPC Aura Burn - Magic
-- Rough Hewn Rock Golem
(1103, 4291, 1), -- Race
(1103, 4071, 4), -- Resist Archery
(1103, 4273, 2), -- Resist Dagger
(1103, 4274, 1), -- Blunt Attack Weak Point
(1103, 4254, 3), -- NPC Prominence - Slow
-- Delu Lizardman Supplier
(1104, 4295, 1), -- Race
(1104, 4032, 3), -- NPC Strike
-- Delu Lizardman Special Agent
(1105, 4295, 1), -- Race
(1105, 4124, 3), -- NPC Spear Attack
-- Cursed Seer
(1106, 4291, 1), -- Race
(1106, 4281, 2), -- Wind Attack Weak Point
(1106, 4276, 1), -- Archery Attack Weak Point
(1106, 4151, 3), -- NPC Windstrike - Magic
(1106, 4160, 3), -- NPC Aura Burn - Magic
(1106, 4038, 1), -- Decrease Atk. Spd.
-- Delu Lizardman Commander
(1107, 4295, 1), -- Race
(1107, 4303, 1), -- Strong Type
(1107, 4033, 3), -- NPC Burn
-- Glow Wisp
(1108, 4291, 1), -- Race
(1108, 4280, 2), -- Water Attack Weak Point
(1108, 4276, 1), -- Archery Attack Weak Point
(1108, 4100, 6), -- NPC Prominence
(1108, 4039, 6), -- NPC MP Drain
(1108, 4076, 3), -- Reduction in movement speed
-- Hames Orc Scout
(1109, 4295, 1), -- Race
(1109, 4067, 6), -- NPC Mortal Blow
-- Marsh Predator
(1110, 4301, 1), -- Race
(1110, 4152, 7), -- NPC HP Drain - Magic
(1110, 4160, 7), -- NPC Aura Burn - Magic
-- Lava Wyrm
(1111, 4299, 1), -- Race
(1111, 4158, 7), -- NPC Prominence - Magic
(1111, 4160, 7), -- NPC Aura Burn - Magic
-- Hames Orc Footman
(1112, 4295, 1), -- Race
(1112, 4032, 7), -- NPC Strike
-- Hames Orc Sniper
(1113, 4295, 1), -- Race
(1113, 4040, 7), -- NPC Bow Attack
-- Cursed Guardian
(1114, 4290, 1), -- Race
(1114, 4275, 3), -- Sacred Attack Weak Point
(1114, 4278, 1), -- Dark Attack
(1114, 4033, 7), -- NPC Burn
-- Hames Orc Shaman
(1115, 4295, 1), -- Race
(1115, 4087, 7), -- NPC Blaze
(1115, 4037, 2), -- Weaken P. Atk.
(1115, 4047, 7), -- Hold
-- Hames Orc Overlord
(1116, 4295, 1), -- Race
(1116, 4273, 2), -- Resist Dagger
(1116, 4091, 1), -- NPC Ogre Stun
(1116, 4033, 8), -- NPC Burn
(1116, 4032, 8), -- NPC Strike
-- Kasha Imp
(1117, 4302, 1), -- Race
-- Baar Dre Vanul
(1118, 4298, 1), -- Race
(1118, 4278, 1), -- Dark Attack
(1118, 4333, 3), -- Resist Dark Attack
-- Hobgoblin
(1119, 4295, 1), -- Race
-- Kasha Imp Turen
(1120, 4302, 1), -- Race
(1120, 4032, 2), -- NPC Strike
-- Kasha Dire Wolf
(1121, 4293, 1), -- Race
-- Kasha Bugbear
(1122, 4295, 1), -- Race
-- Baar Dre Vanul Destroyer
(1123, 4298, 1), -- Race
(1123, 4278, 1), -- Dark Attack
(1123, 4333, 3), -- Resist Dark Attack
(1123, 4229, 2), -- NPC Double Wind Fist
-- Red Eye Barbed Bat
(1124, 4292, 1), -- Race
(1124, 4281, 2), -- Wind Attack Weak Point
(1124, 4276, 1), -- Archery Attack Weak Point
-- Northern Trimden
(1125, 4301, 1), -- Race
-- Kerope Werewolf
(1126, 4295, 1), -- Race
-- Northern Goblin
(1127, 4295, 1), -- Race
-- Spine Golem
(1128, 4291, 1), -- Race
(1128, 4071, 4), -- Resist Archery
(1128, 4273, 2), -- Resist Dagger
(1128, 4274, 1), -- Blunt Attack Weak Point
(1128, 4254, 2), -- NPC Prominence - Slow
-- Kerope Werewolf Chief
(1129, 4295, 1), -- Race
-- Northern Goblin Leader
(1130, 4295, 1), -- Race
-- Enchanted Spine Golem
(1131, 4291, 1), -- Race
(1131, 4071, 4), -- Resist Archery
(1131, 4273, 2), -- Resist Dagger
(1131, 4274, 1), -- Blunt Attack Weak Point
(1131, 4250, 3), -- NPC Twister - Slow
-- Underground Kobold
(1132, 4295, 1), -- Race
-- Dead Pit Skeleton Archer
(1133, 4290, 1), -- Race
(1133, 4275, 3), -- Sacred Attack Weak Point
(1133, 4278, 1), -- Dark Attack
(1133, 4274, 1), -- Blunt Attack Weak Point
(1133, 4273, 2), -- Resist Dagger
-- Dead Pit Spartoi
(1134, 4290, 1), -- Race
(1134, 4275, 3), -- Sacred Attack Weak Point
(1134, 4278, 1), -- Dark Attack
(1134, 4274, 1), -- Blunt Attack Weak Point
(1134, 4273, 2), -- Resist Dagger
-- Underground Kobold Warrior
(1135, 4295, 1), -- Race
(1135, 4032, 2), -- NPC Strike
-- Dead Pit Horror
(1136, 4290, 1), -- Race
(1136, 4275, 3), -- Sacred Attack Weak Point
(1136, 4278, 1), -- Dark Attack
-- Iron Ore Golem
(1137, 4291, 1), -- Race
(1137, 4071, 4), -- Resist Archery
(1137, 4273, 2), -- Resist Dagger
(1137, 4274, 1), -- Blunt Attack Weak Point
(1137, 4250, 4), -- NPC Twister - Slow
-- Greedy Geist
(1138, 4290, 1), -- Race
(1138, 4275, 3), -- Sacred Attack Weak Point
(1138, 4278, 1), -- Dark Attack
(1138, 4028, 2), -- Enhance P. Atk.
-- Catacomb Barbed Bat
(1139, 4292, 1), -- Race
(1139, 4305, 1), -- Strong Type
(1139, 4281, 2), -- Wind Attack Weak Point
(1139, 4276, 1), -- Archery Attack Weak Point
-- Catacomb Wisp
(1140, 4291, 1), -- Race
(1140, 4305, 1), -- Strong Type
(1140, 4281, 2), -- Wind Attack Weak Point
(1140, 4276, 1), -- Archery Attack Weak Point
(1140, 4151, 2), -- NPC Windstrike - Magic
(1140, 4160, 2), -- NPC Aura Burn - Magic
-- Catacomb Serpent
(1141, 4292, 1), -- Race
(1141, 4305, 1), -- Strong Type
(1141, 4101, 2), -- NPC Spinning Slasher
-- Grave Keeper Spartoi
(1142, 4290, 1), -- Race
(1142, 4305, 1), -- Strong Type
(1142, 4275, 3), -- Sacred Attack Weak Point
(1142, 4278, 1), -- Dark Attack
(1142, 4071, 3), -- Resist Archery
(1142, 4273, 2), -- Resist Dagger
-- Catacomb Scavenger Bat
(1143, 4292, 1), -- Race
(1143, 4305, 1), -- Strong Type
(1143, 4281, 2), -- Wind Attack Weak Point
(1143, 4276, 1), -- Archery Attack Weak Point
(1143, 4002, 3), -- NPC HP Drain
-- Catacomb Shadow
(1144, 4298, 1), -- Race
(1144, 4305, 1), -- Strong Type
(1144, 4281, 2), -- Wind Attack Weak Point
(1144, 4276, 1), -- Archery Attack Weak Point
(1144, 4158, 3), -- NPC Prominence - Magic
(1144, 4160, 3), -- NPC Aura Burn - Magic
(1144, 4076, 2), -- Reduction in movement speed
-- Catacomb Stakato Soldier
(1145, 4301, 1), -- Race
(1145, 4305, 1), -- Strong Type
(1145, 4035, 3), -- Poison
-- Grave Keeper Dark Horror
(1146, 4290, 1), -- Race
(1146, 4305, 1), -- Strong Type
(1146, 4275, 3), -- Sacred Attack Weak Point
(1146, 4278, 1), -- Dark Attack
(1146, 4066, 4), -- NPC Twister
-- Catacomb Gargoyle
(1147, 4291, 1), -- Race
(1147, 4305, 1), -- Strong Type
(1147, 4287, 2), -- Resist Hold
(1147, 4285, 2), -- Resist Sleep
(1147, 4273, 2), -- Resist Dagger
(1147, 4072, 4), -- Shock
-- Catacomb Liviona
(1148, 4291, 1), -- Race
(1148, 4305, 1), -- Strong Type
(1148, 4281, 2), -- Wind Attack Weak Point
(1148, 4276, 1), -- Archery Attack Weak Point
(1148, 4157, 4), -- NPC Blaze - Magic
(1148, 4160, 4), -- NPC Aura Burn - Magic
(1148, 4038, 4), -- Decrease Atk. Spd.
-- Decayed Ancient Pikeman
(1149, 4290, 1), -- Race
(1149, 4305, 1), -- Strong Type
(1149, 4275, 3), -- Sacred Attack Weak Point
(1149, 4278, 1), -- Dark Attack
(1149, 4287, 2), -- Resist Hold
(1149, 4285, 2), -- Resist Sleep
(1149, 4071, 3), -- Resist Archery
(1149, 4101, 5), -- NPC Spinning Slasher
-- Decayed Ancient Soldier
(1150, 4290, 1), -- Race
(1150, 4305, 1), -- Strong Type
(1150, 4275, 3), -- Sacred Attack Weak Point
(1150, 4278, 1), -- Dark Attack
(1150, 4073, 4), -- Shock
-- Decayed Ancient Knight
(1151, 4290, 1), -- Race
(1151, 4305, 1), -- Strong Type
(1151, 4275, 3), -- Sacred Attack Weak Point
(1151, 4278, 1), -- Dark Attack
(1151, 4287, 2), -- Resist Hold
(1151, 4285, 2), -- Resist Sleep
(1151, 4071, 3), -- Resist Archery
(1151, 4244, 5), -- NPC Wild Sweep
-- Purgatory Wisp
(1152, 4291, 1), -- Race
(1152, 4305, 1), -- Strong Type
(1152, 4281, 2), -- Wind Attack Weak Point
(1152, 4276, 1), -- Archery Attack Weak Point
(1152, 4151, 5), -- NPC Windstrike - Magic
(1152, 4160, 5), -- NPC Aura Burn - Magic
-- Purgatory Serpent
(1153, 4292, 1), -- Race
(1153, 4305, 1), -- Strong Type
(1153, 4101, 5), -- NPC Spinning Slasher
-- Hell Keeper Medusa
(1154, 4292, 1), -- Race
(1154, 4305, 1), -- Strong Type
(1154, 4035, 5), -- Poison
-- Purgatory Conjurer
(1155, 4292, 1), -- Race
(1155, 4305, 1), -- Strong Type
(1155, 4281, 2), -- Wind Attack Weak Point
(1155, 4276, 1), -- Archery Attack Weak Point
(1155, 4002, 5), -- NPC HP Drain
-- Purgatory Shadow
(1156, 4298, 1), -- Race
(1156, 4305, 1), -- Strong Type
(1156, 4281, 2), -- Wind Attack Weak Point
(1156, 4276, 1), -- Archery Attack Weak Point
(1156, 4158, 6), -- NPC Prominence - Magic
(1156, 4160, 6), -- NPC Aura Burn - Magic
(1156, 4076, 3), -- Reduction in movement speed
-- Purgatory Tarantula
(1157, 4301, 1), -- Race
(1157, 4305, 1), -- Strong Type
(1157, 4035, 6), -- Poison
-- Hell Keeper Crimson Doll
(1158, 4290, 1), -- Race
(1158, 4305, 1), -- Strong Type
(1158, 4275, 3), -- Sacred Attack Weak Point
(1158, 4278, 1), -- Dark Attack
(1158, 4287, 2), -- Resist Hold
(1158, 4285, 2), -- Resist Sleep
(1158, 4073, 4), -- Shock
-- Purgatory Gargoyle
(1159, 4291, 1), -- Race
(1159, 4305, 1), -- Strong Type
(1159, 4287, 2), -- Resist Hold
(1159, 4285, 2), -- Resist Sleep
(1159, 4273, 2), -- Resist Dagger
(1159, 4072, 6), -- Shock
-- Purgatory Liviona
(1160, 4291, 1), -- Race
(1160, 4305, 1), -- Strong Type
(1160, 4281, 2), -- Wind Attack Weak Point
(1160, 4276, 1), -- Archery Attack Weak Point
(1160, 4157, 7), -- NPC Blaze - Magic
(1160, 4160, 7), -- NPC Aura Burn - Magic
(1160, 4038, 5), -- Decrease Atk. Spd.
-- Lesser Ancient Soldier
(1161, 4292, 1), -- Race
(1161, 4305, 1), -- Strong Type
(1161, 4035, 7), -- Poison
-- Lesser Ancient Scout
(1162, 4301, 1), -- Race
(1162, 4305, 1), -- Strong Type
(1162, 4287, 2), -- Resist Hold
(1162, 4285, 2), -- Resist Sleep
(1162, 4069, 7), -- NPC Curve Beam Cannon
-- Lesser Ancient Shaman
(1163, 4298, 1), -- Race
(1163, 4305, 1), -- Strong Type
(1163, 4281, 2), -- Wind Attack Weak Point
(1163, 4276, 1), -- Archery Attack Weak Point
(1163, 4076, 3), -- Reduction in movement speed
-- Guardian Spirit of Ancient Holy Ground
(1164, 4290, 1), -- Race
(1164, 4305, 1), -- Strong Type
(1164, 4275, 3), -- Sacred Attack Weak Point
(1164, 4278, 1), -- Dark Attack
(1164, 4287, 2), -- Resist Hold
(1164, 4285, 2), -- Resist Sleep
(1164, 4101, 8), -- NPC Spinning Slasher
-- Lesser Ancient Warrior
(1165, 4298, 1), -- Race
(1165, 4305, 1), -- Strong Type
(1165, 4084, 4), -- Resist Physical Attack
(1165, 4098, 9), -- Magic Skill Block
(1165, 4046, 9), -- Sleep
(1165, 4105, 9), -- NPC Straight Beam Cannon
(1165, 4094, 9), -- NPC Cancel Magic
-- Lith Scout
(1166, 4298, 1), -- Race
(1166, 4305, 1), -- Strong Type
(1166, 4278, 1), -- Dark Attack
(1166, 4333, 3), -- Resist Dark Attack
(1166, 4287, 3), -- Resist Hold
(1166, 4067, 2), -- NPC Mortal Blow
-- Lith Witch
(1167, 4298, 1), -- Race
(1167, 4305, 1), -- Strong Type
(1167, 4278, 1), -- Dark Attack
(1167, 4333, 3), -- Resist Dark Attack
(1167, 4285, 3), -- Resist Sleep
(1167, 4001, 2), -- NPC Windstrike
-- Lith Warrior
(1168, 4298, 1), -- Race
(1168, 4305, 1), -- Strong Type
(1168, 4278, 1), -- Dark Attack
(1168, 4333, 3), -- Resist Dark Attack
(1168, 4287, 2), -- Resist Hold
(1168, 4285, 2), -- Resist Sleep
(1168, 4071, 2), -- Resist Archery
(1168, 4273, 2), -- Resist Dagger
(1168, 4029, 2), -- Enhance P. Def.
-- Lith Guard
(1169, 4298, 1), -- Race
(1169, 4305, 1), -- Strong Type
(1169, 4278, 1), -- Dark Attack
(1169, 4333, 3), -- Resist Dark Attack
(1169, 4287, 3), -- Resist Hold
(1169, 4084, 4), -- Resist Physical Attack
(1169, 4067, 3), -- NPC Mortal Blow
-- Lith Medium
(1170, 4298, 1), -- Race
(1170, 4305, 1), -- Strong Type
(1170, 4278, 1), -- Dark Attack
(1170, 4333, 3), -- Resist Dark Attack
(1170, 4285, 3), -- Resist Sleep
(1170, 4116, 4), -- Resist M. Atk.
(1170, 4001, 3), -- NPC Windstrike
-- Lith Overlord
(1171, 4298, 1), -- Race
(1171, 4305, 1), -- Strong Type
(1171, 4278, 1), -- Dark Attack
(1171, 4333, 3), -- Resist Dark Attack
(1171, 4287, 2), -- Resist Hold
(1171, 4285, 2), -- Resist Sleep
(1171, 4084, 5), -- Resist Physical Attack
(1171, 4029, 2), -- Enhance P. Def.
-- Lith Patrolman
(1172, 4298, 1), -- Race
(1172, 4305, 1), -- Strong Type
(1172, 4278, 1), -- Dark Attack
(1172, 4333, 3), -- Resist Dark Attack
(1172, 4287, 3), -- Resist Hold
(1172, 4084, 5), -- Resist Physical Attack
(1172, 4067, 3), -- NPC Mortal Blow
-- Lith Shaman
(1173, 4298, 1), -- Race
(1173, 4305, 1), -- Strong Type
(1173, 4278, 1), -- Dark Attack
(1173, 4333, 3), -- Resist Dark Attack
(1173, 4285, 3), -- Resist Sleep
(1173, 4116, 4), -- Resist M. Atk.
(1173, 4001, 4), -- NPC Windstrike
-- Lith Commander
(1174, 4298, 1), -- Race
(1174, 4305, 1), -- Strong Type
(1174, 4278, 1), -- Dark Attack
(1174, 4333, 3), -- Resist Dark Attack
(1174, 4287, 2), -- Resist Hold
(1174, 4285, 2), -- Resist Sleep
(1174, 4084, 6), -- Resist Physical Attack
(1174, 4029, 3), -- Enhance P. Def.
-- Lilim Butcher
(1175, 4298, 1), -- Race
(1175, 4305, 1), -- Strong Type
(1175, 4278, 1), -- Dark Attack
(1175, 4333, 3), -- Resist Dark Attack
(1175, 4287, 3), -- Resist Hold
(1175, 4084, 6), -- Resist Physical Attack
(1175, 4067, 4), -- NPC Mortal Blow
-- Lilim Magus
(1176, 4298, 1), -- Race
(1176, 4305, 1), -- Strong Type
(1176, 4278, 1), -- Dark Attack
(1176, 4333, 3), -- Resist Dark Attack
(1176, 4285, 3), -- Resist Sleep
(1176, 4116, 4), -- Resist M. Atk.
(1176, 4098, 5), -- Magic Skill Block
(1176, 4046, 5), -- Sleep
(1176, 4002, 5), -- NPC HP Drain
(1176, 4094, 5), -- NPC Cancel Magic
-- Lilim Knight Errant
(1177, 4298, 1), -- Race
(1177, 4305, 1), -- Strong Type
(1177, 4278, 1), -- Dark Attack
(1177, 4333, 3), -- Resist Dark Attack
(1177, 4287, 2), -- Resist Hold
(1177, 4285, 2), -- Resist Sleep
(1177, 4084, 7), -- Resist Physical Attack
(1177, 4072, 5), -- Shock
(1177, 4092, 1), -- NPC Puma Stun
(1177, 4032, 5), -- NPC Strike
-- Lilim Marauder
(1178, 4298, 1), -- Race
(1178, 4305, 1), -- Strong Type
(1178, 4278, 1), -- Dark Attack
(1178, 4333, 3), -- Resist Dark Attack
(1178, 4287, 3), -- Resist Hold
(1178, 4084, 7), -- Resist Physical Attack
(1178, 4067, 5), -- NPC Mortal Blow
-- Lilim Priest
(1179, 4298, 1), -- Race
(1179, 4305, 1), -- Strong Type
(1179, 4278, 1), -- Dark Attack
(1179, 4333, 3), -- Resist Dark Attack
(1179, 4285, 3), -- Resist Sleep
(1179, 4116, 4), -- Resist M. Atk.
(1179, 4098, 6), -- Magic Skill Block
(1179, 4046, 6), -- Sleep
(1179, 4002, 6), -- NPC HP Drain
(1179, 4094, 6), -- NPC Cancel Magic
-- Lilim Knight
(1180, 4298, 1), -- Race
(1180, 4305, 1), -- Strong Type
(1180, 4278, 1), -- Dark Attack
(1180, 4333, 3), -- Resist Dark Attack
(1180, 4287, 2), -- Resist Hold
(1180, 4285, 2), -- Resist Sleep
(1180, 4084, 8), -- Resist Physical Attack
(1180, 4072, 6), -- Shock
(1180, 4092, 1), -- NPC Puma Stun
(1180, 4032, 6), -- NPC Strike
-- Lilim Assassin
(1181, 4298, 1), -- Race
(1181, 4305, 1), -- Strong Type
(1181, 4278, 1), -- Dark Attack
(1181, 4333, 3), -- Resist Dark Attack
(1181, 4287, 3), -- Resist Hold
(1181, 4084, 8), -- Resist Physical Attack
(1181, 4067, 6), -- NPC Mortal Blow
-- Lilim Soldier
(1182, 4298, 1), -- Race
(1182, 4305, 1), -- Strong Type
(1182, 4278, 1), -- Dark Attack
(1182, 4333, 3), -- Resist Dark Attack
(1182, 4285, 3), -- Resist Sleep
(1182, 4116, 4), -- Resist M. Atk.
(1182, 4098, 6), -- Magic Skill Block
(1182, 4046, 6), -- Sleep
(1182, 4002, 6), -- NPC HP Drain
(1182, 4094, 6), -- NPC Cancel Magic
-- Lilim Knight Captain
(1183, 4298, 1), -- Race
(1183, 4305, 1), -- Strong Type
(1183, 4278, 1), -- Dark Attack
(1183, 4333, 3), -- Resist Dark Attack
(1183, 4287, 2), -- Resist Hold
(1183, 4285, 2), -- Resist Sleep
(1183, 4084, 8), -- Resist Physical Attack
(1183, 4072, 7), -- Shock
(1183, 4092, 1), -- NPC Puma Stun
(1183, 4032, 7), -- NPC Strike
-- Lilim Slayer
(1184, 4298, 1), -- Race
(1184, 4305, 1), -- Strong Type
(1184, 4278, 1), -- Dark Attack
(1184, 4333, 3), -- Resist Dark Attack
(1184, 4287, 3), -- Resist Hold
(1184, 4084, 8), -- Resist Physical Attack
(1184, 4067, 8), -- NPC Mortal Blow
-- Lilim Great Mystic
(1185, 4298, 1), -- Race
(1185, 4305, 1), -- Strong Type
(1185, 4278, 1), -- Dark Attack
(1185, 4333, 3), -- Resist Dark Attack
(1185, 4285, 3), -- Resist Sleep
(1185, 4116, 4), -- Resist M. Atk.
(1185, 4098, 8), -- Magic Skill Block
(1185, 4046, 8), -- Sleep
(1185, 4002, 8), -- NPC HP Drain
(1185, 4094, 8), -- NPC Cancel Magic
-- Lilim Court Knight
(1186, 4298, 1), -- Race
(1186, 4305, 1), -- Strong Type
(1186, 4278, 1), -- Dark Attack
(1186, 4333, 3), -- Resist Dark Attack
(1186, 4287, 2), -- Resist Hold
(1186, 4285, 2), -- Resist Sleep
(1186, 4084, 8), -- Resist Physical Attack
(1186, 4072, 9), -- Shock
(1186, 4092, 1), -- NPC Puma Stun
(1186, 4032, 9), -- NPC Strike
-- Gigant Slave
(1187, 4297, 1), -- Race
(1187, 4305, 1), -- Strong Type
(1187, 4336, 3), -- Dark Attack Weak Point
(1187, 4287, 3), -- Resist Hold
(1187, 4032, 2), -- NPC Strike
-- Gigant Acolyte
(1188, 4297, 1), -- Race
(1188, 4305, 1), -- Strong Type
(1188, 4336, 3), -- Dark Attack Weak Point
(1188, 4285, 3), -- Resist Sleep
(1188, 4078, 2), -- NPC Flamestrike
-- Gigant Overseer
(1189, 4297, 1), -- Race
(1189, 4305, 1), -- Strong Type
(1189, 4336, 3), -- Dark Attack Weak Point
(1189, 4287, 2), -- Resist Hold
(1189, 4285, 2), -- Resist Sleep
(1189, 4071, 2), -- Resist Archery
(1189, 4273, 2), -- Resist Dagger
(1189, 4029, 2), -- Enhance P. Def.
-- Gigant Footman
(1190, 4297, 1), -- Race
(1190, 4305, 1), -- Strong Type
(1190, 4336, 3), -- Dark Attack Weak Point
(1190, 4287, 3), -- Resist Hold
(1190, 4084, 4), -- Resist Physical Attack
(1190, 4032, 3), -- NPC Strike
-- Gigant Cleric
(1191, 4297, 1), -- Race
(1191, 4305, 1), -- Strong Type
(1191, 4336, 3), -- Dark Attack Weak Point
(1191, 4285, 3), -- Resist Sleep
(1191, 4116, 4), -- Resist M. Atk.
(1191, 4078, 3), -- NPC Flamestrike
-- Gigant Officer
(1192, 4297, 1), -- Race
(1192, 4305, 1), -- Strong Type
(1192, 4336, 3), -- Dark Attack Weak Point
(1192, 4287, 2), -- Resist Hold
(1192, 4285, 2), -- Resist Sleep
(1192, 4084, 5), -- Resist Physical Attack
(1192, 4029, 2), -- Enhance P. Def.
-- Gigant Raider
(1193, 4297, 1), -- Race
(1193, 4305, 1), -- Strong Type
(1193, 4336, 3), -- Dark Attack Weak Point
(1193, 4287, 3), -- Resist Hold
(1193, 4084, 5), -- Resist Physical Attack
(1193, 4032, 3), -- NPC Strike
-- Gigant Confessor
(1194, 4297, 1), -- Race
(1194, 4305, 1), -- Strong Type
(1194, 4336, 3), -- Dark Attack Weak Point
(1194, 4285, 3), -- Resist Sleep
(1194, 4116, 4), -- Resist M. Atk.
(1194, 4078, 4), -- NPC Flamestrike
-- Gigant Commander
(1195, 4297, 1), -- Race
(1195, 4305, 1), -- Strong Type
(1195, 4336, 3), -- Dark Attack Weak Point
(1195, 4287, 2), -- Resist Hold
(1195, 4285, 2), -- Resist Sleep
(1195, 4084, 6), -- Resist Physical Attack
(1195, 4029, 3), -- Enhance P. Def.
-- Nephilim Sentinel
(1196, 4297, 1), -- Race
(1196, 4305, 1), -- Strong Type
(1196, 4336, 3), -- Dark Attack Weak Point
(1196, 4287, 3), -- Resist Hold
(1196, 4084, 6), -- Resist Physical Attack
(1196, 4032, 4), -- NPC Strike
-- Nephilim Priest
(1197, 4297, 1), -- Race
(1197, 4305, 1), -- Strong Type
(1197, 4336, 3), -- Dark Attack Weak Point
(1197, 4285, 3), -- Resist Sleep
(1197, 4116, 4), -- Resist M. Atk.
(1197, 4098, 5), -- Magic Skill Block
(1197, 4046, 5), -- Sleep
(1197, 4065, 5), -- NPC Heal
(1197, 4094, 5), -- NPC Cancel Magic
(1197, 4030, 3), -- Enhance P. Atk.
-- Nephilim Swordsman
(1198, 4297, 1), -- Race
(1198, 4305, 1), -- Strong Type
(1198, 4336, 3), -- Dark Attack Weak Point
(1198, 4287, 2), -- Resist Hold
(1198, 4285, 2), -- Resist Sleep
(1198, 4084, 7), -- Resist Physical Attack
(1198, 4072, 5), -- Shock
(1198, 4091, 1), -- NPC Ogre Stun
(1198, 4032, 5), -- NPC Strike
-- Nephilim Guard
(1199, 4297, 1), -- Race
(1199, 4305, 1), -- Strong Type
(1199, 4336, 3), -- Dark Attack Weak Point
(1199, 4287, 3), -- Resist Hold
(1199, 4084, 7), -- Resist Physical Attack
(1199, 4032, 5), -- NPC Strike
-- Nephilim Bishop
(1200, 4297, 1), -- Race
(1200, 4305, 1), -- Strong Type
(1200, 4336, 3), -- Dark Attack Weak Point
(1200, 4285, 3), -- Resist Sleep
(1200, 4116, 4), -- Resist M. Atk.
(1200, 4098, 6), -- Magic Skill Block
(1200, 4046, 6), -- Sleep
(1200, 4065, 6), -- NPC Heal
(1200, 4094, 6), -- NPC Cancel Magic
(1200, 4030, 3), -- Enhance P. Atk.
-- Nephilim Centurion
(1201, 4297, 1), -- Race
(1201, 4305, 1), -- Strong Type
(1201, 4336, 3), -- Dark Attack Weak Point
(1201, 4287, 2), -- Resist Hold
(1201, 4285, 2), -- Resist Sleep
(1201, 4084, 8), -- Resist Physical Attack
(1201, 4072, 6), -- Shock
(1201, 4091, 1), -- NPC Ogre Stun
(1201, 4032, 6), -- NPC Strike
-- Nephilim Scout
(1202, 4297, 1), -- Race
(1202, 4305, 1), -- Strong Type
(1202, 4336, 3), -- Dark Attack Weak Point
(1202, 4287, 3), -- Resist Hold
(1202, 4084, 8), -- Resist Physical Attack
(1202, 4032, 6), -- NPC Strike
-- Nephilim Archbishop
(1203, 4297, 1), -- Race
(1203, 4305, 1), -- Strong Type
(1203, 4336, 3), -- Dark Attack Weak Point
(1203, 4285, 3), -- Resist Sleep
(1203, 4116, 4), -- Resist M. Atk.
(1203, 4098, 6), -- Magic Skill Block
(1203, 4046, 6), -- Sleep
(1203, 4065, 6), -- NPC Heal
(1203, 4094, 6), -- NPC Cancel Magic
(1203, 4030, 3), -- Enhance P. Atk.
-- Nephilim Praetorian
(1204, 4297, 1), -- Race
(1204, 4305, 1), -- Strong Type
(1204, 4336, 3), -- Dark Attack Weak Point
(1204, 4287, 2), -- Resist Hold
(1204, 4285, 2), -- Resist Sleep
(1204, 4084, 8), -- Resist Physical Attack
(1204, 4072, 7), -- Shock
(1204, 4091, 1), -- NPC Ogre Stun
(1204, 4032, 7), -- NPC Strike
-- Nephilim Royal Guard
(1205, 4297, 1), -- Race
(1205, 4305, 1), -- Strong Type
(1205, 4336, 3), -- Dark Attack Weak Point
(1205, 4287, 3), -- Resist Hold
(1205, 4084, 8), -- Resist Physical Attack
(1205, 4032, 8), -- NPC Strike
-- Nephilim Cardinal
(1206, 4297, 1), -- Race
(1206, 4305, 1), -- Strong Type
(1206, 4336, 3), -- Dark Attack Weak Point
(1206, 4285, 3), -- Resist Sleep
(1206, 4116, 4), -- Resist M. Atk.
(1206, 4098, 8), -- Magic Skill Block
(1206, 4046, 8), -- Sleep
(1206, 4065, 8), -- NPC Heal
(1206, 4094, 8), -- NPC Cancel Magic
(1206, 4030, 3), -- Enhance P. Atk.
-- Nephilim Commander
(1207, 4297, 1), -- Race
(1207, 4305, 1), -- Strong Type
(1207, 4336, 3), -- Dark Attack Weak Point
(1207, 4287, 2), -- Resist Hold
(1207, 4285, 2), -- Resist Sleep
(1207, 4084, 8), -- Resist Physical Attack
(1207, 4072, 9), -- Shock
(1207, 4091, 1), -- NPC Ogre Stun
(1207, 4032, 9), -- NPC Strike
-- Holy Land Watchman
(1208, 4290, 1), -- Race
(1208, 4305, 1), -- Strong Type
(1208, 4275, 3), -- Sacred Attack Weak Point
(1208, 4278, 1), -- Dark Attack
(1208, 4274, 1), -- Blunt Attack Weak Point
(1208, 4317, 1), -- Increase Rage Might
-- Holy Land Seer
(1209, 4290, 1), -- Race
(1209, 4305, 1), -- Strong Type
(1209, 4275, 3), -- Sacred Attack Weak Point
(1209, 4278, 1), -- Dark Attack
(1209, 4099, 1), -- NPC Berserk
-- Vault Guardian
(1210, 4302, 1), -- Race
(1210, 4305, 1), -- Strong Type
(1210, 4278, 1), -- Dark Attack
(1210, 4333, 3), -- Resist Dark Attack
(1210, 4032, 2), -- NPC Strike
-- Vault Seer
(1211, 4298, 1), -- Race
(1211, 4305, 1), -- Strong Type
(1211, 4278, 1), -- Dark Attack
(1211, 4333, 3), -- Resist Dark Attack
(1211, 4002, 2), -- NPC HP Drain
-- Holy Land Sentinel
(1212, 4290, 1), -- Race
(1212, 4305, 1), -- Strong Type
(1212, 4275, 3), -- Sacred Attack Weak Point
(1212, 4278, 1), -- Dark Attack
(1212, 4274, 1), -- Blunt Attack Weak Point
(1212, 4317, 1), -- Increase Rage Might
-- Holy Land Monk
(1213, 4290, 1), -- Race
(1213, 4305, 1), -- Strong Type
(1213, 4275, 3), -- Sacred Attack Weak Point
(1213, 4278, 1), -- Dark Attack
(1213, 4099, 1), -- NPC Berserk
-- Vault Sentinel
(1214, 4302, 1), -- Race
(1214, 4305, 1), -- Strong Type
(1214, 4278, 1), -- Dark Attack
(1214, 4333, 3), -- Resist Dark Attack
(1214, 4032, 3), -- NPC Strike
-- Vault Monk
(1215, 4298, 1), -- Race
(1215, 4305, 1), -- Strong Type
(1215, 4278, 1), -- Dark Attack
(1215, 4333, 3), -- Resist Dark Attack
(1215, 4002, 3), -- NPC HP Drain
-- Holy Land Overlord
(1216, 4290, 1), -- Race
(1216, 4305, 1), -- Strong Type
(1216, 4275, 3), -- Sacred Attack Weak Point
(1216, 4278, 1), -- Dark Attack
(1216, 4274, 1), -- Blunt Attack Weak Point
(1216, 4317, 1), -- Increase Rage Might
-- Holy Land Priest
(1217, 4298, 1), -- Race
(1217, 4305, 1), -- Strong Type
(1217, 4278, 1), -- Dark Attack
(1217, 4333, 3), -- Resist Dark Attack
(1217, 4099, 2), -- NPC Berserk
-- Vault Overlord
(1218, 4290, 1), -- Race
(1218, 4305, 1), -- Strong Type
(1218, 4275, 3), -- Sacred Attack Weak Point
(1218, 4278, 1), -- Dark Attack
(1218, 4032, 4), -- NPC Strike
-- Vault Priest
(1219, 4298, 1), -- Race
(1219, 4305, 1), -- Strong Type
(1219, 4278, 1), -- Dark Attack
(1219, 4333, 3), -- Resist Dark Attack
(1219, 4002, 4), -- NPC HP Drain
-- Sepulcher Archon
(1220, 4290, 1), -- Race
(1220, 4305, 1), -- Strong Type
(1220, 4275, 3), -- Sacred Attack Weak Point
(1220, 4278, 1), -- Dark Attack
(1220, 4274, 1), -- Blunt Attack Weak Point
(1220, 4317, 1), -- Increase Rage Might
-- Sepulcher Inquisitor
(1221, 4298, 1), -- Race
(1221, 4305, 1), -- Strong Type
(1221, 4278, 1), -- Dark Attack
(1221, 4333, 3), -- Resist Dark Attack
(1221, 4099, 2), -- NPC Berserk
-- Sepulcher Archon
(1222, 4290, 1), -- Race
(1222, 4305, 1), -- Strong Type
(1222, 4275, 3), -- Sacred Attack Weak Point
(1222, 4278, 1), -- Dark Attack
(1222, 4032, 5), -- NPC Strike
-- Sepulcher Inquisitor
(1223, 4298, 1), -- Race
(1223, 4305, 1), -- Strong Type
(1223, 4278, 1), -- Dark Attack
(1223, 4333, 3), -- Resist Dark Attack
(1223, 4002, 5), -- NPC HP Drain
-- Sepulcher Guardian
(1224, 4290, 1), -- Race
(1224, 4305, 1), -- Strong Type
(1224, 4275, 3), -- Sacred Attack Weak Point
(1224, 4278, 1), -- Dark Attack
(1224, 4274, 1), -- Blunt Attack Weak Point
(1224, 4071, 4), -- Resist Archery
(1224, 4273, 2), -- Resist Dagger
(1224, 4317, 1), -- Increase Rage Might
-- Sepulcher Sage
(1225, 4298, 1), -- Race
(1225, 4305, 1), -- Strong Type
(1225, 4275, 3), -- Sacred Attack Weak Point
(1225, 4278, 1), -- Dark Attack
(1225, 4099, 2), -- NPC Berserk
-- Sepulcher Guardian
(1226, 4290, 1), -- Race
(1226, 4305, 1), -- Strong Type
(1226, 4275, 3), -- Sacred Attack Weak Point
(1226, 4278, 1), -- Dark Attack
(1226, 4032, 6), -- NPC Strike
-- Sepulcher Sage
(1227, 4290, 1), -- Race
(1227, 4305, 1), -- Strong Type
(1227, 4275, 3), -- Sacred Attack Weak Point
(1227, 4278, 1), -- Dark Attack
(1227, 4274, 1), -- Blunt Attack Weak Point
(1227, 4084, 4), -- Resist Physical Attack
(1227, 4002, 6), -- NPC HP Drain
-- Sepulcher Guard 
(1228, 4290, 1), -- Race
(1228, 4305, 1), -- Strong Type
(1228, 4275, 3), -- Sacred Attack Weak Point
(1228, 4278, 1), -- Dark Attack
(1228, 4274, 1), -- Blunt Attack Weak Point
(1228, 4071, 4), -- Resist Archery
(1228, 4273, 2), -- Resist Dagger
(1228, 4317, 1), -- Increase Rage Might
-- Sepulcher Preacher
(1229, 4298, 1), -- Race
(1229, 4305, 1), -- Strong Type
(1229, 4278, 1), -- Dark Attack
(1229, 4333, 3), -- Resist Dark Attack
(1229, 4099, 2), -- NPC Berserk
-- Sepulcher Guard
(1230, 4290, 1), -- Race
(1230, 4305, 1), -- Strong Type
(1230, 4275, 3), -- Sacred Attack Weak Point
(1230, 4278, 1), -- Dark Attack
(1230, 4032, 8), -- NPC Strike
-- Sepulcher Preacher
(1231, 4290, 1), -- Race
(1231, 4305, 1), -- Strong Type
(1231, 4275, 3), -- Sacred Attack Weak Point
(1231, 4278, 1), -- Dark Attack
(1231, 4274, 1), -- Blunt Attack Weak Point
(1231, 4002, 8), -- NPC HP Drain
-- Barrow Guardian
(1232, 4290, 1), -- Race
(1232, 4305, 1), -- Strong Type
(1232, 4275, 3), -- Sacred Attack Weak Point
(1232, 4278, 1), -- Dark Attack
(1232, 4274, 1), -- Blunt Attack Weak Point
(1232, 4317, 1), -- Increase Rage Might
-- Barrow Seer
(1233, 4290, 1), -- Race
(1233, 4305, 1), -- Strong Type
(1233, 4275, 3), -- Sacred Attack Weak Point
(1233, 4278, 1), -- Dark Attack
(1233, 4099, 1), -- NPC Berserk
-- Grave Guardian
(1234, 4302, 1), -- Race
(1234, 4305, 1), -- Strong Type
(1234, 4278, 1), -- Dark Attack
(1234, 4333, 3), -- Resist Dark Attack
(1234, 4032, 2), -- NPC Strike
-- Grave Seer
(1235, 4298, 1), -- Race
(1235, 4305, 1), -- Strong Type
(1235, 4278, 1), -- Dark Attack
(1235, 4333, 3), -- Resist Dark Attack
(1235, 4002, 2), -- NPC HP Drain
-- Barrow Sentinel
(1236, 4290, 1), -- Race
(1236, 4305, 1), -- Strong Type
(1236, 4275, 3), -- Sacred Attack Weak Point
(1236, 4278, 1), -- Dark Attack
(1236, 4274, 1), -- Blunt Attack Weak Point
(1236, 4317, 1), -- Increase Rage Might
-- Barrow Monk
(1237, 4290, 1), -- Race
(1237, 4305, 1), -- Strong Type
(1237, 4275, 3), -- Sacred Attack Weak Point
(1237, 4278, 1), -- Dark Attack
(1237, 4099, 1), -- NPC Berserk
-- Grave Sentinel
(1238, 4302, 1), -- Race
(1238, 4305, 1), -- Strong Type
(1238, 4278, 1), -- Dark Attack
(1238, 4333, 3), -- Resist Dark Attack
(1238, 4032, 3), -- NPC Strike
-- Grave Monk
(1239, 4298, 1), -- Race
(1239, 4305, 1), -- Strong Type
(1239, 4278, 1), -- Dark Attack
(1239, 4333, 3), -- Resist Dark Attack
(1239, 4002, 3), -- NPC HP Drain
-- Barrow Overlord
(1240, 4290, 1), -- Race
(1240, 4305, 1), -- Strong Type
(1240, 4275, 3), -- Sacred Attack Weak Point
(1240, 4278, 1), -- Dark Attack
(1240, 4274, 1), -- Blunt Attack Weak Point
(1240, 4317, 1), -- Increase Rage Might
-- Barrow Priest
(1241, 4298, 1), -- Race
(1241, 4305, 1), -- Strong Type
(1241, 4278, 1), -- Dark Attack
(1241, 4333, 3), -- Resist Dark Attack
(1241, 4099, 2), -- NPC Berserk
-- Grave Overlord
(1242, 4290, 1), -- Race
(1242, 4305, 1), -- Strong Type
(1242, 4275, 3), -- Sacred Attack Weak Point
(1242, 4278, 1), -- Dark Attack
(1242, 4032, 4), -- NPC Strike
-- Grave Priest
(1243, 4298, 1), -- Race
(1243, 4305, 1), -- Strong Type
(1243, 4278, 1), -- Dark Attack
(1243, 4333, 3), -- Resist Dark Attack
(1243, 4002, 4), -- NPC HP Drain
-- Crypt Archon
(1244, 4290, 1), -- Race
(1244, 4305, 1), -- Strong Type
(1244, 4275, 3), -- Sacred Attack Weak Point
(1244, 4278, 1), -- Dark Attack
(1244, 4274, 1), -- Blunt Attack Weak Point
(1244, 4317, 1), -- Increase Rage Might
-- Crypt Inquisitor
(1245, 4298, 1), -- Race
(1245, 4305, 1), -- Strong Type
(1245, 4278, 1), -- Dark Attack
(1245, 4333, 3), -- Resist Dark Attack
(1245, 4099, 2), -- NPC Berserk
-- Tomb Archon
(1246, 4290, 1), -- Race
(1246, 4305, 1), -- Strong Type
(1246, 4275, 3), -- Sacred Attack Weak Point
(1246, 4278, 1), -- Dark Attack
(1246, 4032, 5), -- NPC Strike
-- Tomb Inquisitor
(1247, 4298, 1), -- Race
(1247, 4305, 1), -- Strong Type
(1247, 4278, 1), -- Dark Attack
(1247, 4333, 3), -- Resist Dark Attack
(1247, 4002, 5), -- NPC HP Drain
-- Crypt Guardian
(1248, 4290, 1), -- Race
(1248, 4305, 1), -- Strong Type
(1248, 4275, 3), -- Sacred Attack Weak Point
(1248, 4278, 1), -- Dark Attack
(1248, 4274, 1), -- Blunt Attack Weak Point
(1248, 4071, 4), -- Resist Archery
(1248, 4273, 2), -- Resist Dagger
(1248, 4317, 1), -- Increase Rage Might
-- Crypt Sage
(1249, 4298, 1), -- Race
(1249, 4305, 1), -- Strong Type
(1249, 4275, 3), -- Sacred Attack Weak Point
(1249, 4278, 1), -- Dark Attack
(1249, 4099, 2), -- NPC Berserk
-- Tomb Guardian
(1250, 4290, 1), -- Race
(1250, 4305, 1), -- Strong Type
(1250, 4275, 3), -- Sacred Attack Weak Point
(1250, 4278, 1), -- Dark Attack
(1250, 4032, 6), -- NPC Strike
-- Tomb Sage
(1251, 4290, 1), -- Race
(1251, 4305, 1), -- Strong Type
(1251, 4275, 3), -- Sacred Attack Weak Point
(1251, 4278, 1), -- Dark Attack
(1251, 4274, 1), -- Blunt Attack Weak Point
(1251, 4084, 4), -- Resist Physical Attack
(1251, 4002, 6), -- NPC HP Drain
-- Crypt Guard 
(1252, 4290, 1), -- Race
(1252, 4305, 1), -- Strong Type
(1252, 4275, 3), -- Sacred Attack Weak Point
(1252, 4278, 1), -- Dark Attack
(1252, 4274, 1), -- Blunt Attack Weak Point
(1252, 4071, 4), -- Resist Archery
(1252, 4273, 2), -- Resist Dagger
(1252, 4317, 1), -- Increase Rage Might
-- Crypt Preacher
(1253, 4298, 1), -- Race
(1253, 4305, 1), -- Strong Type
(1253, 4278, 1), -- Dark Attack
(1253, 4333, 3), -- Resist Dark Attack
(1253, 4099, 2), -- NPC Berserk
-- Tomb Guard
(1254, 4290, 1), -- Race
(1254, 4305, 1), -- Strong Type
(1254, 4275, 3), -- Sacred Attack Weak Point
(1254, 4278, 1), -- Dark Attack
(1254, 4032, 8), -- NPC Strike
-- Tomb Preacher
(1255, 4290, 1), -- Race
(1255, 4305, 1), -- Strong Type
(1255, 4275, 3), -- Sacred Attack Weak Point
(1255, 4278, 1), -- Dark Attack
(1255, 4274, 1), -- Blunt Attack Weak Point
(1255, 4084, 4), -- Resist Physical Attack
(1255, 4002, 8), -- NPC HP Drain
-- Underground Werewolf
(1256, 4295, 1), -- Race
-- Evil Eye Seer
(1257, 4291, 1), -- Race
(1257, 4281, 2), -- Wind Attack Weak Point
(1257, 4276, 1), -- Archery Attack Weak Point
(1257, 4152, 1), -- NPC HP Drain - Magic
(1257, 4160, 1), -- NPC Aura Burn - Magic
-- Fallen Orc Shaman
(1258, 4295, 1), -- Race
(1258, 4095, 1), -- Damage Shield
-- Fallen Orc Shaman
(1259, 4293, 1), -- Race
(1259, 4085, 1), -- Critical Power
(1259, 4086, 1), -- Critical Chance
(1259, 4073, 5), -- Shock
-- Betrayer Orc Hero
(1260, 4295, 1), -- Race
(1260, 4085, 1), -- Critical Power
(1260, 4086, 1), -- Critical Chance
(1260, 4073, 5), -- Shock
-- Ol Mahum Transcender
(1261, 4295, 1), -- Race
(1261, 4281, 2), -- Wind Attack Weak Point
(1261, 4276, 1), -- Archery Attack Weak Point
(1261, 4032, 5), -- NPC Strike
-- Ol Mahum Transcender
(1262, 4295, 1), -- Race
(1262, 4281, 2), -- Wind Attack Weak Point
(1262, 4276, 1), -- Archery Attack Weak Point
(1262, 4072, 5), -- Shock
(1262, 4091, 1), -- NPC Ogre Stun
(1262, 4032, 5), -- NPC Strike
-- Ol Mahum Transcender
(1263, 4295, 1), -- Race
(1263, 4281, 2), -- Wind Attack Weak Point
(1263, 4276, 1), -- Archery Attack Weak Point
(1263, 4072, 5), -- Shock
(1263, 4091, 1), -- NPC Ogre Stun
(1263, 4032, 5), -- NPC Strike
-- Ol Mahum Transcender
(1264, 4297, 1), -- Race
(1264, 4085, 1), -- Critical Power
(1264, 4086, 1), -- Critical Chance
(1264, 4072, 6), -- Shock
(1264, 4091, 1), -- NPC Ogre Stun
(1264, 4032, 6), -- NPC Strike
-- Cave Ant Larva
(1265, 4301, 1), -- Race
(1265, 4279, 2), -- Fire Attack Weak Point
-- Cave Ant Larva
(1266, 4301, 1), -- Race
(1266, 4279, 2), -- Fire Attack Weak Point
-- Cave Ant Larva
(1267, 4301, 1), -- Race
(1267, 4279, 2), -- Fire Attack Weak Point
-- Cave Ant Larva
(1268, 4301, 1), -- Race
(1268, 4303, 1), -- Strong Type
(1268, 4279, 2), -- Fire Attack Weak Point
(1268, 4151, 3), -- NPC Windstrike - Magic
(1268, 4160, 3), -- NPC Aura Burn - Magic
-- Cave Ant
(1269, 4301, 1), -- Race
(1269, 4279, 2), -- Fire Attack Weak Point
(1269, 4317, 1), -- Increase Rage Might
-- Cave Ant Soldier
(1270, 4301, 1), -- Race
(1270, 4279, 2), -- Fire Attack Weak Point
(1270, 4032, 3), -- NPC Strike
-- Cave Ant
(1271, 4301, 1), -- Race
(1271, 4279, 2), -- Fire Attack Weak Point
(1271, 4317, 1), -- Increase Rage Might
-- Cave Ant Soldier
(1272, 4301, 1), -- Race
(1272, 4279, 2), -- Fire Attack Weak Point
(1272, 4032, 3), -- NPC Strike
-- Cave Noble Ant
(1273, 4301, 1), -- Race
(1273, 4303, 1), -- Strong Type
(1273, 4279, 2), -- Fire Attack Weak Point
(1273, 4085, 1), -- Critical Power
(1273, 4086, 1), -- Critical Chance
(1273, 4072, 3), -- Shock
(1273, 4091, 1), -- NPC Ogre Stun
(1273, 4032, 3), -- NPC Strike
-- Kookaburra
(1274, 4293, 1), -- Race
(1274, 4311, 1), -- Feeble Type
(1274, 4067, 6), -- NPC Mortal Blow
-- Kookaburra
(1275, 4293, 1), -- Race
(1275, 4311, 1), -- Feeble Type
(1275, 4032, 6), -- NPC Strike
-- Kookaburra
(1276, 4293, 1), -- Race
(1276, 4311, 1), -- Feeble Type
(1276, 4244, 6), -- NPC Wild Sweep
-- Kookaburra
(1277, 4293, 1), -- Race
(1277, 4311, 1), -- Feeble Type
(1277, 4157, 6), -- NPC Blaze - Magic
(1277, 4160, 6), -- NPC Aura Burn - Magic
-- Antelope
(1278, 4293, 1), -- Race
(1278, 4311, 1), -- Feeble Type
(1278, 4032, 6), -- NPC Strike
-- Antelope
(1279, 4293, 1), -- Race
(1279, 4311, 1), -- Feeble Type
(1279, 4073, 6), -- Shock
-- Antelope
(1280, 4293, 1), -- Race
(1280, 4311, 1), -- Feeble Type
(1280, 4232, 6), -- NPC AE Strike
-- Antelope
(1281, 4293, 1), -- Race
(1281, 4311, 1), -- Feeble Type
(1281, 4257, 6), -- NPC Hydroblast - Magic
(1281, 4160, 6), -- NPC Aura Burn - Magic
-- Bandersnatch
(1282, 4293, 1), -- Race
(1282, 4311, 1), -- Feeble Type
(1282, 4067, 6), -- NPC Mortal Blow
-- Bandersnatch
(1283, 4293, 1), -- Race
(1283, 4311, 1), -- Feeble Type
(1283, 4073, 6), -- Shock
-- Bandersnatch
(1284, 4293, 1), -- Race
(1284, 4311, 1), -- Feeble Type
(1284, 4072, 6), -- Shock
-- Bandersnatch
(1285, 4293, 1), -- Race
(1285, 4311, 1), -- Feeble Type
(1285, 4158, 6), -- NPC Prominence - Magic
(1285, 4160, 6), -- NPC Aura Burn - Magic
-- Buffalo
(1286, 4293, 1), -- Race
(1286, 4311, 1), -- Feeble Type
(1286, 4073, 7), -- Shock
-- Buffalo
(1287, 4293, 1), -- Race
(1287, 4311, 1), -- Feeble Type
(1287, 4072, 7), -- Shock
-- Buffalo
(1288, 4293, 1), -- Race
(1288, 4311, 1), -- Feeble Type
(1288, 4244, 7), -- NPC Wild Sweep
-- Buffalo
(1289, 4293, 1), -- Race
(1289, 4311, 1), -- Feeble Type
(1289, 4157, 7), -- NPC Blaze - Magic
(1289, 4160, 7), -- NPC Aura Burn - Magic
-- Grendel
(1290, 4293, 1), -- Race
(1290, 4311, 1), -- Feeble Type
(1290, 4789, 2), -- NPC High Level
(1290, 4073, 7), -- Shock
-- Grendel
(1291, 4293, 1), -- Race
(1291, 4311, 1), -- Feeble Type
(1291, 4789, 2), -- NPC High Level
(1291, 4072, 7), -- Shock
-- Grendel
(1292, 4293, 1), -- Race
(1292, 4311, 1), -- Feeble Type
(1292, 4789, 2), -- NPC High Level
(1292, 4232, 7), -- NPC AE Strike
-- Grendel
(1293, 4293, 1), -- Race
(1293, 4311, 1), -- Feeble Type
(1293, 4789, 2), -- NPC High Level
(1293, 4158, 7), -- NPC Prominence - Magic
(1293, 4160, 7), -- NPC Aura Burn - Magic
-- Canyon Antelope 
(1294, 4293, 1), -- Race
(1294, 4303, 1), -- Strong Type
(1294, 4032, 6), -- NPC Strike
-- Canyon Antelope Slave
(1295, 4293, 1), -- Race
(1295, 4303, 1), -- Strong Type
(1295, 4032, 6), -- NPC Strike
(1295, 4092, 1), -- NPC Puma Stun
-- Canyon Bandersnatch 
(1296, 4293, 1), -- Race
(1296, 4304, 1), -- Strong Type
(1296, 4579, 7), -- Bleed
-- Canyon Bandersnatch Slave
(1297, 4293, 1), -- Race
(1297, 4304, 1), -- Strong Type
(1297, 4789, 2), -- NPC High Level
(1297, 4032, 7), -- NPC Strike
(1297, 4090, 1), -- NPC Wolf Stun
-- Eye of Restrainer 
(1298, 4291, 1), -- Race
(1298, 4303, 1), -- Strong Type
(1298, 4281, 2), -- Wind Attack Weak Point
(1298, 4276, 1), -- Archery Attack Weak Point
(1298, 4789, 2), -- NPC High Level
(1298, 4038, 5), -- Decrease Atk. Spd.
-- Buffalo Slave
(1299, 4293, 1), -- Race
(1299, 4304, 1), -- Strong Type
(1299, 4789, 2), -- NPC High Level
(1299, 4032, 7), -- NPC Strike
(1299, 4091, 1), -- NPC Ogre Stun
-- Eye of Guide
(1300, 4291, 1), -- Race
(1300, 4304, 1), -- Strong Type
(1300, 4281, 2), -- Wind Attack Weak Point
(1300, 4276, 1), -- Archery Attack Weak Point
(1300, 4789, 2), -- NPC High Level
(1300, 4158, 7), -- NPC Prominence - Magic
(1300, 4160, 7), -- NPC Aura Burn - Magic
(1300, 4613, 7), -- NPC Clan Heal
-- Gaze of Nightmare
(1301, 4291, 1), -- Race
(1301, 4305, 1), -- Strong Type
(1301, 4789, 2), -- NPC High Level
(1301, 4098, 7), -- Magic Skill Block
(1301, 4046, 7), -- Sleep
(1301, 4105, 7), -- NPC Straight Beam Cannon
(1301, 4094, 7), -- NPC Cancel Magic
(1301, 4047, 7), -- Hold
(1301, 4650, 1), -- NPC AE - Dispel Hold
-- Eye of Watchman
(1302, 4291, 1), -- Race
(1302, 4304, 1), -- Strong Type
(1302, 4281, 2), -- Wind Attack Weak Point
(1302, 4276, 1), -- Archery Attack Weak Point
(1302, 4789, 3), -- NPC High Level
(1302, 4098, 7), -- Magic Skill Block
(1302, 4046, 7), -- Sleep
(1302, 4105, 7), -- NPC Straight Beam Cannon
(1302, 4094, 7), -- NPC Cancel Magic
(1302, 4657, 7), -- Hold
(1302, 4650, 1), -- NPC AE - Dispel Hold
-- Homunculus
(1303, 4291, 1), -- Race
(1303, 4304, 1), -- Strong Type
(1303, 4279, 2), -- Fire Attack Weak Point
(1303, 4276, 1), -- Archery Attack Weak Point
(1303, 4789, 3), -- NPC High Level
(1303, 4563, 7), -- NPC Solar Flare - Magic
(1303, 4561, 7), -- NPC Fire Burn - Magic
-- Grendel Slave
(1304, 4293, 1), -- Race
(1304, 4305, 1), -- Strong Type
(1304, 4789, 3), -- NPC High Level
(1304, 4089, 1), -- NPC Bear Stun
(1304, 4090, 1), -- NPC Wolf Stun
(1304, 4092, 1), -- NPC Puma Stun
(1304, 4091, 1), -- NPC Ogre Stun
-- Eye of Pilgrim
(1305, 4291, 1), -- Race
(1305, 4304, 1), -- Strong Type
(1305, 4281, 2), -- Wind Attack Weak Point
(1305, 4276, 1), -- Archery Attack Weak Point
(1305, 4789, 3), -- NPC High Level
(1305, 4158, 7), -- NPC Prominence - Magic
(1305, 4160, 7), -- NPC Aura Burn - Magic
(1305, 4613, 7), -- NPC Clan Heal
-- Disciples of Protection
(1306, 4297, 1), -- Race
(1306, 4306, 1), -- Strong Type
(1306, 4336, 3), -- Dark Attack Weak Point
(1306, 4335, 1), -- Sacred Attack
(1306, 4337, 4), -- Resist Sacred Attack
(1306, 4085, 1), -- Critical Power
(1306, 4086, 1), -- Critical Chance
(1306, 4789, 3), -- NPC High Level
(1306, 4585, 3), -- NPC Clan Buff - Berserk Might
-- Elder Homunculus
(1307, 4291, 1), -- Race
(1307, 4305, 1), -- Strong Type
(1307, 4279, 2), -- Fire Attack Weak Point
(1307, 4276, 1), -- Archery Attack Weak Point
(1307, 4789, 4), -- NPC High Level
(1307, 4257, 8), -- NPC Hydroblast - Magic
(1307, 4561, 8), -- NPC Fire Burn - Magic
(1307, 4038, 5), -- Decrease Atk. Spd.
-- Disciples of Punishment
(1308, 4297, 1), -- Race
(1308, 4306, 1), -- Strong Type
(1308, 4336, 3), -- Dark Attack Weak Point
(1308, 4335, 1), -- Sacred Attack
(1308, 4337, 4), -- Resist Sacred Attack
(1308, 4085, 1), -- Critical Power
(1308, 4086, 1), -- Critical Chance
(1308, 4789, 4), -- NPC High Level
(1308, 4158, 8), -- NPC Prominence - Magic
(1308, 4599, 8), -- Decrease Speed
-- Disciples of Punishment
(1309, 4297, 1), -- Race
(1309, 4306, 1), -- Strong Type
(1309, 4336, 3), -- Dark Attack Weak Point
(1309, 4335, 1), -- Sacred Attack
(1309, 4337, 4), -- Resist Sacred Attack
(1309, 4085, 1), -- Critical Power
(1309, 4086, 1), -- Critical Chance
(1309, 4789, 4), -- NPC High Level
(1309, 4033, 8), -- NPC Burn
(1309, 4091, 1), -- NPC Ogre Stun
(1309, 4580, 8), -- Decrease P.Atk
-- Disciples of Authority
(1310, 4297, 1), -- Race
(1310, 4306, 1), -- Strong Type
(1310, 4336, 3), -- Dark Attack Weak Point
(1310, 4335, 1), -- Sacred Attack
(1310, 4337, 4), -- Resist Sacred Attack
(1310, 4085, 1), -- Critical Power
(1310, 4086, 1), -- Critical Chance
(1310, 4789, 4), -- NPC High Level
(1310, 4158, 8), -- NPC Prominence - Magic
(1310, 4599, 8), -- Decrease Speed
-- Disciples of Authority
(1311, 4297, 1), -- Race
(1311, 4306, 1), -- Strong Type
(1311, 4336, 3), -- Dark Attack Weak Point
(1311, 4335, 1), -- Sacred Attack
(1311, 4337, 4), -- Resist Sacred Attack
(1311, 4789, 4), -- NPC High Level
(1311, 4158, 8), -- NPC Prominence - Magic
(1311, 4160, 8), -- NPC Aura Burn - Magic
-- Eye of Ruler
(1312, 4291, 1), -- Race
(1312, 4304, 1), -- Strong Type
(1312, 4281, 2), -- Wind Attack Weak Point
(1312, 4276, 1), -- Archery Attack Weak Point
(1312, 4789, 3), -- NPC High Level
(1312, 4156, 7), -- NPC Curve Beam Cannon - Magic
(1312, 4160, 7), -- NPC Aura Burn - Magic
(1312, 4609, 4), -- NPC Clan Buff - Vampiric Rage
(1312, 4047, 7), -- Hold
(1312, 4650, 1), -- NPC AE - Dispel Hold
(1312, 4571, 7), -- NPC Blazing Circle
-- Sly Hound Dog
(1313, 4293, 1), -- Race
(1313, 4304, 1), -- Strong Type
(1313, 4789, 2), -- NPC High Level
(1313, 4579, 7), -- Bleed
-- Hot Springs Bandersnatchling
(1314, 4293, 1), -- Race
(1314, 4303, 1), -- Strong Type
(1314, 4555, 1), -- NPC Resist Mutant
(1314, 4085, 1), -- Critical Power
(1314, 4086, 1), -- Critical Chance
(1314, 4789, 3), -- NPC High Level
(1314, 4002, 7), -- NPC HP Drain
(1314, 4073, 7), -- Shock
(1314, 4074, 2), -- NPC Haste
(1314, 4096, 3), -- NPC Hawkeye
(1314, 4551, 1), -- Hot Springs Rheumatism
(1314, 4554, 1), -- Hot Spring Malaria
-- Hot Springs Buffalo
(1315, 4293, 1), -- Race
(1315, 4303, 1), -- Strong Type
(1315, 4555, 1), -- NPC Resist Mutant
(1315, 4085, 1), -- Critical Power
(1315, 4086, 1), -- Critical Chance
(1315, 4071, 4), -- Resist Archery
(1315, 4789, 3), -- NPC High Level
(1315, 4092, 1), -- NPC Puma Stun
(1315, 4072, 7), -- Shock
(1315, 4073, 7), -- Shock
-- Hot Springs Flava
(1316, 4294, 1), -- Race
(1316, 4303, 1), -- Strong Type
(1316, 4555, 1), -- NPC Resist Mutant
(1316, 4279, 2), -- Fire Attack Weak Point
(1316, 4277, 3), -- Resist Poison
(1316, 4085, 1), -- Critical Power
(1316, 4086, 1), -- Critical Chance
(1316, 4116, 6), -- Resist M. Atk.
(1316, 4789, 3), -- NPC High Level
(1316, 4073, 7), -- Shock
(1316, 4099, 2), -- NPC Berserk
(1316, 4074, 2), -- NPC Haste
(1316, 4552, 1), -- Hot Springs Cholera
(1316, 4554, 1), -- Hot Spring Malaria
-- Hot Springs Atroxspawn
(1317, 4301, 1), -- Race
(1317, 4303, 1), -- Strong Type
(1317, 4555, 1), -- NPC Resist Mutant
(1317, 4085, 1), -- Critical Power
(1317, 4086, 1), -- Critical Chance
(1317, 4789, 3), -- NPC High Level
(1317, 4002, 7), -- NPC HP Drain
(1317, 4073, 7), -- Shock
(1317, 4074, 2), -- NPC Haste
(1317, 4096, 3), -- NPC Hawkeye
(1317, 4553, 1), -- Hot Springs Flu
(1317, 4554, 1), -- Hot Spring Malaria
-- Hot Springs Antelope
(1318, 4293, 1), -- Race
(1318, 4303, 1), -- Strong Type
(1318, 4555, 1), -- NPC Resist Mutant
(1318, 4085, 1), -- Critical Power
(1318, 4086, 1), -- Critical Chance
(1318, 4071, 3), -- Resist Archery
(1318, 4789, 3), -- NPC High Level
(1318, 4092, 1), -- NPC Puma Stun
(1318, 4033, 7), -- NPC Burn
(1318, 4073, 7), -- Shock
-- Hot Springs Nepenthes
(1319, 4294, 1), -- Race
(1319, 4303, 1), -- Strong Type
(1319, 4555, 1), -- NPC Resist Mutant
(1319, 4279, 2), -- Fire Attack Weak Point
(1319, 4277, 3), -- Resist Poison
(1319, 4085, 1), -- Critical Power
(1319, 4086, 1), -- Critical Chance
(1319, 4116, 6), -- Resist M. Atk.
(1319, 4789, 4), -- NPC High Level
(1319, 4073, 8), -- Shock
(1319, 4072, 8), -- Shock
(1319, 4074, 2), -- NPC Haste
(1319, 4099, 2), -- NPC Berserk
(1319, 4552, 1), -- Hot Springs Cholera
(1319, 4554, 1), -- Hot Spring Malaria
-- Hot Springs Yeti
(1320, 4295, 1), -- Race
(1320, 4303, 1), -- Strong Type
(1320, 4555, 1), -- NPC Resist Mutant
(1320, 4085, 1), -- Critical Power
(1320, 4086, 1), -- Critical Chance
(1320, 4071, 4), -- Resist Archery
(1320, 4789, 4), -- NPC High Level
(1320, 4092, 1), -- NPC Puma Stun
(1320, 4072, 8), -- Shock
(1320, 4073, 8), -- Shock
-- Hot Springs Atrox
(1321, 4301, 1), -- Race
(1321, 4303, 1), -- Strong Type
(1321, 4555, 1), -- NPC Resist Mutant
(1321, 4085, 1), -- Critical Power
(1321, 4086, 1), -- Critical Chance
(1321, 4116, 6), -- Resist M. Atk.
(1321, 4789, 4), -- NPC High Level
(1321, 4002, 8), -- NPC HP Drain
(1321, 4073, 8), -- Shock
(1321, 4074, 2), -- NPC Haste
(1321, 4096, 3), -- NPC Hawkeye
(1321, 4551, 1), -- Hot Springs Rheumatism
(1321, 4554, 1), -- Hot Spring Malaria
-- Hot Springs Bandersnatch
(1322, 4293, 1), -- Race
(1322, 4303, 1), -- Strong Type
(1322, 4555, 1), -- NPC Resist Mutant
(1322, 4085, 1), -- Critical Power
(1322, 4086, 1), -- Critical Chance
(1322, 4116, 6), -- Resist M. Atk.
(1322, 4789, 4), -- NPC High Level
(1322, 4002, 8), -- NPC HP Drain
(1322, 4073, 8), -- Shock
(1322, 4074, 2), -- NPC Haste
(1322, 4099, 2), -- NPC Berserk
(1322, 4553, 1), -- Hot Springs Flu
(1322, 4554, 1), -- Hot Spring Malaria
-- Hot Springs Grendel
(1323, 4293, 1), -- Race
(1323, 4303, 1), -- Strong Type
(1323, 4555, 1), -- NPC Resist Mutant
(1323, 4085, 1), -- Critical Power
(1323, 4086, 1), -- Critical Chance
(1323, 4071, 4), -- Resist Archery
(1323, 4789, 4), -- NPC High Level
(1323, 4092, 1), -- NPC Puma Stun
(1323, 4033, 8), -- NPC Burn
(1323, 4073, 8), -- Shock
-- Ketra Orc Footman
(1324, 4295, 1), -- Race
(1324, 4303, 1), -- Strong Type
(1324, 4789, 5), -- NPC High Level
(1324, 4099, 2), -- NPC Berserk
(1324, 4072, 8), -- Shock
(1324, 4573, 8), -- NPC Sonic Blaster
(1324, 4578, 1), -- Petrification
-- Ketra's War Hound
(1325, 4293, 1), -- Race
(1325, 4303, 1), -- Strong Type
(1325, 4789, 5), -- NPC High Level
(1325, 4032, 8), -- NPC Strike
(1325, 4578, 1), -- Petrification
-- Grazing Kookaburra
(1326, 4293, 1), -- Race
(1326, 4303, 1), -- Strong Type
(1326, 4789, 5), -- NPC High Level
(1326, 4073, 8), -- Shock
-- Ketra Orc Raider
(1327, 4295, 1), -- Race
(1327, 4303, 1), -- Strong Type
(1327, 4085, 1), -- Critical Power
(1327, 4086, 1), -- Critical Chance
(1327, 4789, 5), -- NPC High Level
(1327, 4317, 1), -- Increase Rage Might
(1327, 4072, 8), -- Shock
(1327, 4572, 8), -- NPC Triple Sonic Slash
(1327, 4038, 5), -- Decrease Atk. Spd.
(1327, 4578, 1), -- Petrification
-- Ketra Orc Scout
(1328, 4295, 1), -- Race
(1328, 4303, 1), -- Strong Type
(1328, 4789, 5), -- NPC High Level
(1328, 4040, 8), -- NPC Bow Attack
(1328, 4578, 1), -- Petrification
-- Ketra Orc Shaman
(1329, 4295, 1), -- Race
(1329, 4303, 1), -- Strong Type
(1329, 4789, 6), -- NPC High Level
(1329, 4157, 8), -- NPC Blaze - Magic
(1329, 4561, 8), -- NPC Fire Burn - Magic
(1329, 4030, 3), -- Enhance P. Atk.
(1329, 4031, 3), -- Enhance P. Def.
(1329, 4035, 8), -- Poison
(1329, 4578, 1), -- Petrification
-- Grazing Kookaburra
(1330, 4293, 1), -- Race
(1330, 4303, 1), -- Strong Type
(1330, 4789, 6), -- NPC High Level
(1330, 4073, 8), -- Shock
-- Ketra Orc Warrior
(1331, 4295, 1), -- Race
(1331, 4303, 1), -- Strong Type
(1331, 4789, 6), -- NPC High Level
(1331, 4078, 9), -- NPC Flamestrike
(1331, 4596, 9), -- Bleed
(1331, 4119, 3), -- Fall in accuracy
(1331, 4578, 1), -- Petrification
-- Ketra Orc Lieutenant
(1332, 4295, 1), -- Race
(1332, 4303, 1), -- Strong Type
(1332, 4085, 1), -- Critical Power
(1332, 4086, 1), -- Critical Chance
(1332, 4789, 6), -- NPC High Level
(1332, 4317, 1), -- Increase Rage Might
(1332, 4072, 9), -- Shock
(1332, 4573, 9), -- NPC Sonic Blaster
(1332, 4038, 5), -- Decrease Atk. Spd.
(1332, 4578, 1), -- Petrification
-- Grazing Windsus
(1333, 4293, 1), -- Race
(1333, 4303, 1), -- Strong Type
(1333, 4789, 7), -- NPC High Level
(1333, 4067, 9), -- NPC Mortal Blow
-- Ketra Orc Medium
(1334, 4295, 1), -- Race
(1334, 4303, 1), -- Strong Type
(1334, 4789, 7), -- NPC High Level
(1334, 4158, 9), -- NPC Prominence - Magic
(1334, 4561, 9), -- NPC Fire Burn - Magic
(1334, 4571, 9), -- NPC Blazing Circle
(1334, 4575, 2), -- NPC Clan Buff - Haste
(1334, 4576, 3), -- NPC Clan Buff - Damage Shield
(1334, 4035, 9), -- Poison
(1334, 4578, 1), -- Petrification
-- Ketra Orc Elite Soldier
(1335, 4295, 1), -- Race
(1335, 4303, 1), -- Strong Type
(1335, 4789, 7), -- NPC High Level
(1335, 4099, 2), -- NPC Berserk
(1335, 4571, 9), -- NPC Blazing Circle
(1335, 4573, 9), -- NPC Sonic Blaster
(1335, 4578, 1), -- Petrification
-- Ketra Orc White Captain
(1336, 4295, 1), -- Race
(1336, 4303, 1), -- Strong Type
(1336, 4085, 1), -- Critical Power
(1336, 4086, 1), -- Critical Chance
(1336, 4789, 7), -- NPC High Level
(1336, 4317, 1), -- Increase Rage Might
(1336, 4072, 9), -- Shock
(1336, 4572, 9), -- NPC Triple Sonic Slash
(1336, 4038, 5), -- Decrease Atk. Spd.
(1336, 4578, 1), -- Petrification
-- Grazing Elder Buffalo
(1337, 4293, 1), -- Race
(1337, 4303, 1), -- Strong Type
(1337, 4789, 8), -- NPC High Level
(1337, 4032, 9), -- NPC Strike
-- Ketra Orc Seer
(1338, 4295, 1), -- Race
(1338, 4303, 1), -- Strong Type
(1338, 4789, 8), -- NPC High Level
(1338, 4158, 9), -- NPC Prominence - Magic
(1338, 4561, 9), -- NPC Fire Burn - Magic
(1338, 4030, 3), -- Enhance P. Atk.
(1338, 4575, 2), -- NPC Clan Buff - Haste
(1338, 4035, 9), -- Poison
(1338, 4578, 1), -- Petrification
-- Ketra Orc General
(1339, 4295, 1), -- Race
(1339, 4303, 1), -- Strong Type
(1339, 4085, 1), -- Critical Power
(1339, 4086, 1), -- Critical Chance
(1339, 4789, 8), -- NPC High Level
(1339, 4317, 1), -- Increase Rage Might
(1339, 4571, 9), -- NPC Blazing Circle
(1339, 4573, 9), -- NPC Sonic Blaster
(1339, 4038, 5), -- Decrease Atk. Spd.
(1339, 4578, 1), -- Petrification
-- Ketra Orc Battalion Commander
(1340, 4295, 1), -- Race
(1340, 4303, 1), -- Strong Type
(1340, 4085, 1), -- Critical Power
(1340, 4086, 1), -- Critical Chance
(1340, 4789, 8), -- NPC High Level
(1340, 4317, 1), -- Increase Rage Might
(1340, 4571, 9), -- NPC Blazing Circle
(1340, 4573, 9), -- NPC Sonic Blaster
(1340, 4038, 5), -- Decrease Atk. Spd.
(1340, 4578, 1), -- Petrification
-- Grazing Elder Kookaburra
(1341, 4293, 1), -- Race
(1341, 4303, 1), -- Strong Type
(1341, 4789, 9), -- NPC High Level
(1341, 4580, 9), -- Decrease P.Atk
-- Ketra Orc Grand Seer
(1342, 4295, 1), -- Race
(1342, 4303, 1), -- Strong Type
(1342, 4789, 9), -- NPC High Level
(1342, 4158, 9), -- NPC Prominence - Magic
(1342, 4561, 9), -- NPC Fire Burn - Magic
(1342, 4571, 9), -- NPC Blazing Circle
(1342, 4575, 2), -- NPC Clan Buff - Haste
(1342, 4031, 3), -- Enhance P. Def.
(1342, 4035, 9), -- Poison
(1342, 4578, 1), -- Petrification
-- Ketra Commander
(1343, 4295, 1), -- Race
(1343, 4303, 1), -- Strong Type
(1343, 4085, 1), -- Critical Power
(1343, 4086, 1), -- Critical Chance
(1343, 4789, 7), -- NPC High Level
(1343, 4572, 9), -- NPC Triple Sonic Slash
(1343, 4575, 2), -- NPC Clan Buff - Haste
(1343, 4038, 5), -- Decrease Atk. Spd.
(1343, 4578, 1), -- Petrification
-- Ketra Elite Guard
(1344, 4295, 1), -- Race
(1344, 4303, 1), -- Strong Type
(1344, 4789, 6), -- NPC High Level
(1344, 4573, 9), -- NPC Sonic Blaster
(1344, 4578, 1), -- Petrification
-- Ketra's Head Shaman
(1345, 4295, 1), -- Race
(1345, 4303, 1), -- Strong Type
(1345, 4789, 9), -- NPC High Level
(1345, 4100, 9), -- NPC Prominence
(1345, 4578, 1), -- Petrification
-- Ketra's Head Guard
(1346, 4295, 1), -- Race
(1346, 4303, 1), -- Strong Type
(1346, 4789, 8), -- NPC High Level
(1346, 4573, 9), -- NPC Sonic Blaster
(1346, 4578, 1), -- Petrification
-- Ketra Prophet
(1347, 4295, 1), -- Race
(1347, 4303, 1), -- Strong Type
(1347, 4789, 10), -- NPC High Level
(1347, 4158, 9), -- NPC Prominence - Magic
(1347, 4561, 9), -- NPC Fire Burn - Magic
(1347, 4030, 3), -- Enhance P. Atk.
(1347, 4575, 2), -- NPC Clan Buff - Haste
(1347, 4035, 9), -- Poison
(1347, 4578, 1), -- Petrification
-- Prophet's Guard
(1348, 4295, 1), -- Race
(1348, 4303, 1), -- Strong Type
(1348, 4789, 9), -- NPC High Level
(1348, 4573, 9), -- NPC Sonic Blaster
(1348, 4578, 1), -- Petrification
-- Prophet's Aide
(1349, 4295, 1), -- Race
(1349, 4303, 1), -- Strong Type
(1349, 4789, 9), -- NPC High Level
(1349, 4100, 9), -- NPC Prominence
(1349, 4035, 9), -- Poison
(1349, 4578, 1), -- Petrification
-- Varka Silenos Recruit
(1350, 4295, 1), -- Race
(1350, 4303, 1), -- Strong Type
(1350, 4789, 5), -- NPC High Level
(1350, 4092, 1), -- NPC Puma Stun
(1350, 4072, 8), -- Shock
(1350, 4032, 8), -- NPC Strike
(1350, 4578, 1), -- Petrification
-- Varka Silenos Footman
(1351, 4295, 1), -- Race
(1351, 4303, 1), -- Strong Type
(1351, 4789, 5), -- NPC High Level
(1351, 4573, 8), -- NPC Sonic Blaster
(1351, 4578, 1), -- Petrification
-- Grazing Antelope
(1352, 4293, 1), -- Race
(1352, 4303, 1), -- Strong Type
(1352, 4789, 5), -- NPC High Level
(1352, 4067, 8), -- NPC Mortal Blow
-- Varka Silenos Scout
(1353, 4295, 1), -- Race
(1353, 4303, 1), -- Strong Type
(1353, 4085, 1), -- Critical Power
(1353, 4086, 1), -- Critical Chance
(1353, 4789, 5), -- NPC High Level
(1353, 4317, 1), -- Increase Rage Might
(1353, 4072, 8), -- Shock
(1353, 4573, 8), -- NPC Sonic Blaster
(1353, 4038, 5), -- Decrease Atk. Spd.
(1353, 4578, 1), -- Petrification
-- Varka Silenos Hunter
(1354, 4295, 1), -- Race
(1354, 4303, 1), -- Strong Type
(1354, 4789, 5), -- NPC High Level
(1354, 4040, 8), -- NPC Bow Attack
(1354, 4578, 1), -- Petrification
-- Varka Silenos Shaman
(1355, 4295, 1), -- Race
(1355, 4303, 1), -- Strong Type
(1355, 4789, 6), -- NPC High Level
(1355, 4563, 8), -- NPC Solar Flare - Magic
(1355, 4160, 8), -- NPC Aura Burn - Magic
(1355, 4030, 3), -- Enhance P. Atk.
(1355, 4031, 3), -- Enhance P. Def.
(1355, 4098, 8), -- Magic Skill Block
(1355, 4578, 1), -- Petrification
-- Grazing Nepenthes
(1356, 4294, 1), -- Race
(1356, 4303, 1), -- Strong Type
(1356, 4279, 2), -- Fire Attack Weak Point
(1356, 4277, 3), -- Resist Poison
(1356, 4789, 6), -- NPC High Level
(1356, 4073, 8), -- Shock
-- Varka Silenos Priest
(1357, 4295, 1), -- Race
(1357, 4303, 1), -- Strong Type
(1357, 4789, 6), -- NPC High Level
(1357, 4562, 9), -- NPC Solar Flare
(1357, 4560, 9), -- NPC Fire Burn
(1357, 4119, 3), -- Fall in accuracy
(1357, 4578, 1), -- Petrification
-- Varka Silenos Warrior
(1358, 4295, 1), -- Race
(1358, 4303, 1), -- Strong Type
(1358, 4085, 1), -- Critical Power
(1358, 4086, 1), -- Critical Chance
(1358, 4789, 6), -- NPC High Level
(1358, 4317, 1), -- Increase Rage Might
(1358, 4072, 9), -- Shock
(1358, 4572, 9), -- NPC Triple Sonic Slash
(1358, 4038, 5), -- Decrease Atk. Spd.
(1358, 4578, 1), -- Petrification
-- Grazing Bandersnatch
(1359, 4293, 1), -- Race
(1359, 4303, 1), -- Strong Type
(1359, 4789, 7), -- NPC High Level
(1359, 4032, 9), -- NPC Strike
-- Varka Silenos Medium
(1360, 4295, 1), -- Race
(1360, 4303, 1), -- Strong Type
(1360, 4789, 7), -- NPC High Level
(1360, 4563, 9), -- NPC Solar Flare - Magic
(1360, 4160, 9), -- NPC Aura Burn - Magic
(1360, 4033, 9), -- NPC Burn
(1360, 4575, 2), -- NPC Clan Buff - Haste
(1360, 4576, 3), -- NPC Clan Buff - Damage Shield
(1360, 4035, 9), -- Poison
(1360, 4578, 1), -- Petrification
-- Varka Silenos Magus
(1361, 4295, 1), -- Race
(1361, 4303, 1), -- Strong Type
(1361, 4789, 7), -- NPC High Level
(1361, 4099, 2), -- NPC Berserk
(1361, 4571, 9), -- NPC Blazing Circle
(1361, 4573, 9), -- NPC Sonic Blaster
(1361, 4578, 1), -- Petrification
-- Varka Silenos Officer
(1362, 4295, 1), -- Race
(1362, 4303, 1), -- Strong Type
(1362, 4085, 1), -- Critical Power
(1362, 4086, 1), -- Critical Chance
(1362, 4789, 7), -- NPC High Level
(1362, 4317, 1), -- Increase Rage Might
(1362, 4072, 9), -- Shock
(1362, 4572, 9), -- NPC Triple Sonic Slash
(1362, 4038, 5), -- Decrease Atk. Spd.
(1362, 4578, 1), -- Petrification
-- Grazing Flava
(1363, 4294, 1), -- Race
(1363, 4303, 1), -- Strong Type
(1363, 4279, 2), -- Fire Attack Weak Point
(1363, 4277, 3), -- Resist Poison
(1363, 4789, 8), -- NPC High Level
(1363, 4032, 9), -- NPC Strike
-- Varka Silenos Seer
(1364, 4295, 1), -- Race
(1364, 4303, 1), -- Strong Type
(1364, 4789, 8), -- NPC High Level
(1364, 4562, 9), -- NPC Solar Flare
(1364, 4160, 9), -- NPC Aura Burn - Magic
(1364, 4030, 3), -- Enhance P. Atk.
(1364, 4575, 2), -- NPC Clan Buff - Haste
(1364, 4119, 3), -- Fall in accuracy
(1364, 4578, 1), -- Petrification
-- Varka Silenos Great Magus
(1365, 4295, 1), -- Race
(1365, 4303, 1), -- Strong Type
(1365, 4085, 1), -- Critical Power
(1365, 4086, 1), -- Critical Chance
(1365, 4789, 8), -- NPC High Level
(1365, 4317, 1), -- Increase Rage Might
(1365, 4033, 9), -- NPC Burn
(1365, 4573, 9), -- NPC Sonic Blaster
(1365, 4038, 5), -- Decrease Atk. Spd.
(1365, 4578, 1), -- Petrification
-- Varka Silenos General
(1366, 4295, 1), -- Race
(1366, 4303, 1), -- Strong Type
(1366, 4085, 1), -- Critical Power
(1366, 4086, 1), -- Critical Chance
(1366, 4789, 8), -- NPC High Level
(1366, 4317, 1), -- Increase Rage Might
(1366, 4072, 9), -- Shock
(1366, 4077, 9), -- NPC Aura Burn
(1366, 4038, 5), -- Decrease Atk. Spd.
(1366, 4578, 1), -- Petrification
-- Grazing Elder Antelope
(1367, 4293, 1), -- Race
(1367, 4303, 1), -- Strong Type
(1367, 4789, 9), -- NPC High Level
(1367, 4580, 9), -- Decrease P.Atk
-- Varka Silenos Great Seer
(1368, 4295, 1), -- Race
(1368, 4303, 1), -- Strong Type
(1368, 4789, 9), -- NPC High Level
(1368, 4563, 9), -- NPC Solar Flare - Magic
(1368, 4160, 9), -- NPC Aura Burn - Magic
(1368, 4033, 9), -- NPC Burn
(1368, 4575, 2), -- NPC Clan Buff - Haste
(1368, 4576, 3), -- NPC Clan Buff - Damage Shield
(1368, 4119, 3), -- Fall in accuracy
(1368, 4578, 1), -- Petrification
-- Varka's Commander
(1369, 4295, 1), -- Race
(1369, 4303, 1), -- Strong Type
(1369, 4085, 1), -- Critical Power
(1369, 4086, 1), -- Critical Chance
(1369, 4789, 7), -- NPC High Level
(1369, 4573, 9), -- NPC Sonic Blaster
(1369, 4575, 2), -- NPC Clan Buff - Haste
(1369, 4038, 5), -- Decrease Atk. Spd.
(1369, 4578, 1), -- Petrification
-- Varka's Elite Guard
(1370, 4295, 1), -- Race
(1370, 4303, 1), -- Strong Type
(1370, 4789, 6), -- NPC High Level
(1370, 4572, 9), -- NPC Triple Sonic Slash
(1370, 4578, 1), -- Petrification
-- Varka's Head Magus
(1371, 4295, 1), -- Race
(1371, 4303, 1), -- Strong Type
(1371, 4789, 9), -- NPC High Level
(1371, 4562, 9), -- NPC Solar Flare
(1371, 4578, 1), -- Petrification
-- Varka's Head Guard
(1372, 4295, 1), -- Race
(1372, 4303, 1), -- Strong Type
(1372, 4789, 8), -- NPC High Level
(1372, 4573, 9), -- NPC Sonic Blaster
(1372, 4578, 1), -- Petrification
-- Varka's Prophet
(1373, 4295, 1), -- Race
(1373, 4303, 1), -- Strong Type
(1373, 4789, 10), -- NPC High Level
(1373, 4563, 9), -- NPC Solar Flare - Magic
(1373, 4160, 9), -- NPC Aura Burn - Magic
(1373, 4030, 3), -- Enhance P. Atk.
(1373, 4575, 2), -- NPC Clan Buff - Haste
(1373, 4119, 3), -- Fall in accuracy
(1373, 4578, 1), -- Petrification
-- Prophet Guard
(1374, 4295, 1), -- Race
(1374, 4303, 1), -- Strong Type
(1374, 4789, 9), -- NPC High Level
(1374, 4573, 9), -- NPC Sonic Blaster
(1374, 4578, 1), -- Petrification
-- Disciple of Prophet
(1375, 4295, 1), -- Race
(1375, 4303, 1), -- Strong Type
(1375, 4789, 9), -- NPC High Level
(1375, 4066, 9), -- NPC Twister
(1375, 4098, 9), -- Magic Skill Block
(1375, 4578, 1), -- Petrification
-- Scarlet Stakato Walker
(1376, 4301, 1), -- Race
(1376, 4306, 1), -- Strong Type
(1376, 4071, 3), -- Resist Archery
(1376, 4274, 1), -- Blunt Attack Weak Point
(1376, 4789, 5), -- NPC High Level
(1376, 4579, 8), -- Bleed
-- Scarlet Stakato Soldier
(1377, 4301, 1), -- Race
(1377, 4306, 1), -- Strong Type
(1377, 4071, 3), -- Resist Archery
(1377, 4274, 1), -- Blunt Attack Weak Point
(1377, 4789, 5), -- NPC High Level
(1377, 4580, 8), -- Decrease P.Atk
-- Scarlet Stakato Noble
(1378, 4301, 1), -- Race
(1378, 4306, 1), -- Strong Type
(1378, 4084, 8), -- Resist Physical Attack
(1378, 4789, 6), -- NPC High Level
(1378, 4581, 8), -- Hold
-- Tepra Scorpion
(1379, 4301, 1), -- Race
(1379, 4306, 1), -- Strong Type
(1379, 4284, 5), -- Resist Bleeding
(1379, 4071, 3), -- Resist Archery
(1379, 4274, 1), -- Blunt Attack Weak Point
(1379, 4789, 6), -- NPC High Level
(1379, 4117, 8), -- Paralysis
(1379, 4037, 2), -- Weaken P. Atk.
-- Tepra Scarab
(1380, 4301, 1), -- Race
(1380, 4306, 1), -- Strong Type
(1380, 4284, 5), -- Resist Bleeding
(1380, 4071, 3), -- Resist Archery
(1380, 4274, 1), -- Blunt Attack Weak Point
(1380, 4789, 6), -- NPC High Level
(1380, 4582, 8), -- Poison
(1380, 4119, 3), -- Fall in accuracy
-- Assassin Beetle
(1381, 4301, 1), -- Race
(1381, 4306, 1), -- Strong Type
(1381, 4284, 5), -- Resist Bleeding
(1381, 4084, 5), -- Resist Physical Attack
(1381, 4274, 1), -- Blunt Attack Weak Point
(1381, 4789, 6), -- NPC High Level
(1381, 4573, 9), -- NPC Sonic Blaster
(1381, 4244, 9), -- NPC Wild Sweep
-- Mercenary of Destruction
(1382, 4290, 1), -- Race
(1382, 4306, 1), -- Strong Type
(1382, 4275, 3), -- Sacred Attack Weak Point
(1382, 4278, 1), -- Dark Attack
(1382, 4116, 8), -- Resist M. Atk.
(1382, 4274, 1), -- Blunt Attack Weak Point
(1382, 4287, 3), -- Resist Hold
(1382, 4789, 6), -- NPC High Level
(1382, 4317, 1), -- Increase Rage Might
(1382, 4072, 9), -- Shock
(1382, 4584, 9), -- Reducing P.Def Shock
(1382, 4037, 2), -- Weaken P. Atk.
-- Knight of Destruction
(1383, 4290, 1), -- Race
(1383, 4306, 1), -- Strong Type
(1383, 4275, 3), -- Sacred Attack Weak Point
(1383, 4278, 1), -- Dark Attack
(1383, 4084, 8), -- Resist Physical Attack
(1383, 4274, 1), -- Blunt Attack Weak Point
(1383, 4789, 6), -- NPC High Level
(1383, 4579, 9), -- Bleed
(1383, 4586, 9), -- Decrease Evasion
-- Necromancer of Destruction
(1384, 4298, 1), -- Race
(1384, 4306, 1), -- Strong Type
(1384, 4278, 1), -- Dark Attack
(1384, 4333, 3), -- Resist Dark Attack
(1384, 4084, 5), -- Resist Physical Attack
(1384, 4285, 2), -- Resist Sleep
(1384, 4789, 7), -- NPC High Level
(1384, 4153, 9), -- Decrease Speed
(1384, 4072, 9), -- Shock
(1384, 4031, 3), -- Enhance P. Def.
-- Lavastone Golem
(1385, 4291, 1), -- Race
(1385, 4306, 1), -- Strong Type
(1385, 4084, 6), -- Resist Physical Attack
(1385, 4009, 2), -- Resist Fire
(1385, 4280, 2), -- Water Attack Weak Point
(1385, 4274, 1), -- Blunt Attack Weak Point
(1385, 4789, 7), -- NPC High Level
(1385, 4594, 9), -- Decrease P.Def
-- Magma Golem
(1386, 4291, 1), -- Race
(1386, 4306, 1), -- Strong Type
(1386, 4084, 7), -- Resist Physical Attack
(1386, 4009, 2), -- Resist Fire
(1386, 4280, 2), -- Water Attack Weak Point
(1386, 4274, 1), -- Blunt Attack Weak Point
(1386, 4789, 7), -- NPC High Level
(1386, 4591, 9), -- Decrease Speed
-- Arimanes of Destruction
(1387, 4298, 1), -- Race
(1387, 4306, 1), -- Strong Type
(1387, 4278, 1), -- Dark Attack
(1387, 4333, 3), -- Resist Dark Attack
(1387, 4285, 2), -- Resist Sleep
(1387, 4071, 4), -- Resist Archery
(1387, 4273, 2), -- Resist Dagger
(1387, 4789, 7), -- NPC High Level
(1387, 4157, 9), -- NPC Blaze - Magic
(1387, 4589, 9), -- Decrease Speed
-- Iblis of Destruction
(1388, 4298, 1), -- Race
(1388, 4307, 1), -- Strong Type
(1388, 4278, 1), -- Dark Attack
(1388, 4333, 3), -- Resist Dark Attack
(1388, 4285, 3), -- Resist Sleep
(1388, 4071, 3), -- Resist Archery
(1388, 4789, 7), -- NPC High Level
(1388, 4389, 4), -- Resist Mental Derangement
(1388, 4141, 9), -- NPC Wind Fist
(1388, 4595, 3), -- NPC Clan Buff - Acumen Shield
-- Balrog of Destruction
(1389, 4298, 1), -- Race
(1389, 4307, 1), -- Strong Type
(1389, 4278, 1), -- Dark Attack
(1389, 4333, 3), -- Resist Dark Attack
(1389, 4116, 7), -- Resist M. Atk.
(1389, 4789, 8), -- NPC High Level
(1389, 4597, 9), -- Bleed
(1389, 4599, 9), -- Decrease Speed
-- Ashuras of Destruction
(1390, 4298, 1), -- Race
(1390, 4307, 1), -- Strong Type
(1390, 4278, 1), -- Dark Attack
(1390, 4333, 3), -- Resist Dark Attack
(1390, 4285, 4), -- Resist Sleep
(1390, 4084, 8), -- Resist Physical Attack
(1390, 4789, 8), -- NPC High Level
(1390, 4072, 9), -- Shock
(1390, 4030, 3), -- Enhance P. Atk.
-- Lavasillisk
(1391, 4292, 1), -- Race
(1391, 4307, 1), -- Strong Type
(1391, 4789, 8), -- NPC High Level
(1391, 4078, 9), -- NPC Flamestrike
(1391, 4601, 3), -- NPC Clan Buff - Acumen Focus
-- Blazing Ifrit
(1392, 4296, 1), -- Race
(1392, 4307, 1), -- Strong Type
(1392, 4084, 8), -- Resist Physical Attack
(1392, 4009, 2), -- Resist Fire
(1392, 4280, 2), -- Water Attack Weak Point
(1392, 4789, 8), -- NPC High Level
(1392, 4229, 9), -- NPC Double Wind Fist
(1392, 4605, 9), -- Fire Weakness
-- Magma Drake
(1393, 4299, 1), -- Race
(1393, 4307, 1), -- Strong Type
(1393, 4084, 6), -- Resist Physical Attack
(1393, 4009, 2), -- Resist Fire
(1393, 4280, 2), -- Water Attack Weak Point
(1393, 4789, 9), -- NPC High Level
(1393, 4590, 9), -- Decrease Speed
(1393, 4605, 9), -- Fire Weakness
-- Lavasaurus
(1394, 4291, 1), -- Race
(1394, 4306, 1), -- Strong Type
(1394, 4084, 7), -- Resist Physical Attack
(1394, 4009, 2), -- Resist Fire
(1394, 4280, 2), -- Water Attack Weak Point
(1394, 4274, 1), -- Blunt Attack Weak Point
(1394, 4789, 6), -- NPC High Level
(1394, 4389, 4), -- Resist Mental Derangement
(1394, 4607, 1), -- Magma Attack
-- Elder Lavasaurus
(1395, 4291, 1), -- Race
(1395, 4307, 1), -- Strong Type
(1395, 4084, 7), -- Resist Physical Attack
(1395, 4009, 2), -- Resist Fire
(1395, 4280, 2), -- Water Attack Weak Point
(1395, 4274, 1), -- Blunt Attack Weak Point
(1395, 4789, 8), -- NPC High Level
(1395, 4389, 4), -- Resist Mental Derangement
(1395, 4607, 1), -- Magma Attack
-- Carrion Scarab
(1396, 4301, 1), -- Race
(1396, 4304, 1), -- Strong Type
(1396, 4284, 5), -- Resist Bleeding
(1396, 4071, 3), -- Resist Archery
(1396, 4274, 1), -- Blunt Attack Weak Point
(1396, 4287, 4), -- Resist Hold
(1396, 4084, 6), -- Resist Physical Attack
(1396, 4789, 5), -- NPC High Level
(1396, 4001, 8), -- NPC Windstrike
(1396, 4002, 8), -- NPC HP Drain
(1396, 4047, 8), -- Sleep
-- Carrion Scarab
(1397, 4301, 1), -- Race
(1397, 4304, 1), -- Strong Type
(1397, 4284, 5), -- Resist Bleeding
(1397, 4071, 3), -- Resist Archery
(1397, 4274, 1), -- Blunt Attack Weak Point
(1397, 4285, 4), -- Resist Sleep
(1397, 4789, 5), -- NPC High Level
(1397, 4032, 8), -- NPC Strike
-- Soldier Scarab
(1398, 4301, 1), -- Race
(1398, 4307, 1), -- Strong Type
(1398, 4284, 5), -- Resist Bleeding
(1398, 4071, 3), -- Resist Archery
(1398, 4274, 1), -- Blunt Attack Weak Point
(1398, 4789, 5), -- NPC High Level
(1398, 4579, 8), -- Bleed
-- Soldier Scarab
(1399, 4301, 1), -- Race
(1399, 4306, 1), -- Strong Type
(1399, 4284, 5), -- Resist Bleeding
(1399, 4071, 3), -- Resist Archery
(1399, 4274, 1), -- Blunt Attack Weak Point
(1399, 4084, 6), -- Resist Physical Attack
(1399, 4285, 4), -- Resist Sleep
(1399, 4789, 5), -- NPC High Level
(1399, 4157, 8), -- NPC Blaze - Magic
(1399, 4160, 8), -- NPC Aura Burn - Magic
-- Hexa Beetle
(1400, 4301, 1), -- Race
(1400, 4306, 1), -- Strong Type
(1400, 4284, 5), -- Resist Bleeding
(1400, 4071, 3), -- Resist Archery
(1400, 4274, 1), -- Blunt Attack Weak Point
(1400, 4789, 6), -- NPC High Level
(1400, 4573, 8), -- NPC Sonic Blaster
-- Hexa Beetle
(1401, 4301, 1), -- Race
(1401, 4305, 1), -- Strong Type
(1401, 4284, 5), -- Resist Bleeding
(1401, 4071, 3), -- Resist Archery
(1401, 4274, 1), -- Blunt Attack Weak Point
(1401, 4789, 6), -- NPC High Level
(1401, 4573, 8), -- NPC Sonic Blaster
(1401, 4072, 8), -- Shock
-- Katraxis
(1402, 4301, 1), -- Race
(1402, 4305, 1), -- Strong Type
(1402, 4284, 5), -- Resist Bleeding
(1402, 4071, 3), -- Resist Archery
(1402, 4274, 1), -- Blunt Attack Weak Point
(1402, 4789, 6), -- NPC High Level
(1402, 4072, 8), -- Shock
(1402, 4090, 1), -- NPC Wolf Stun
(1402, 4032, 8), -- NPC Strike
-- Katraxis
(1403, 4301, 1), -- Race
(1403, 4306, 1), -- Strong Type
(1403, 4284, 5), -- Resist Bleeding
(1403, 4071, 3), -- Resist Archery
(1403, 4274, 1), -- Blunt Attack Weak Point
(1403, 4789, 6), -- NPC High Level
(1403, 4643, 8), -- Decrease Speed
-- Tera Beetle
(1404, 4301, 1), -- Race
(1404, 4305, 1), -- Strong Type
(1404, 4284, 5), -- Resist Bleeding
(1404, 4071, 3), -- Resist Archery
(1404, 4274, 1), -- Blunt Attack Weak Point
(1404, 4084, 9), -- Resist Physical Attack
(1404, 4789, 6), -- NPC High Level
(1404, 4157, 9), -- NPC Blaze - Magic
(1404, 4561, 9), -- NPC Fire Burn - Magic
(1404, 4576, 3), -- NPC Clan Buff - Damage Shield
-- Tera Beetle
(1405, 4301, 1), -- Race
(1405, 4305, 1), -- Strong Type
(1405, 4284, 5), -- Resist Bleeding
(1405, 4071, 3), -- Resist Archery
(1405, 4274, 1), -- Blunt Attack Weak Point
(1405, 4789, 6), -- NPC High Level
(1405, 4001, 9), -- NPC Windstrike
(1405, 4029, 3), -- Enhance P. Def.
-- Knight of Empire
(1406, 4290, 1), -- Race
(1406, 4306, 1), -- Strong Type
(1406, 4084, 6), -- Resist Physical Attack
(1406, 4275, 3), -- Sacred Attack Weak Point
(1406, 4278, 1), -- Dark Attack
(1406, 4274, 1), -- Blunt Attack Weak Point
(1406, 4279, 1), -- Fire Attack Weak Point
(1406, 4789, 6), -- NPC High Level
(1406, 4157, 9), -- NPC Blaze - Magic
(1406, 4561, 9), -- NPC Fire Burn - Magic
(1406, 4117, 9), -- Paralysis
-- Knight of Empire
(1407, 4290, 1), -- Race
(1407, 4304, 1), -- Strong Type
(1407, 4084, 6), -- Resist Physical Attack
(1407, 4275, 3), -- Sacred Attack Weak Point
(1407, 4278, 1), -- Dark Attack
(1407, 4274, 1), -- Blunt Attack Weak Point
(1407, 4279, 1), -- Fire Attack Weak Point
(1407, 4789, 6), -- NPC High Level
(1407, 4032, 9), -- NPC Strike
(1407, 4663, 1), -- NPC Hate
-- Royal Guard of Empire
(1408, 4290, 1), -- Race
(1408, 4306, 1), -- Strong Type
(1408, 4275, 3), -- Sacred Attack Weak Point
(1408, 4278, 1), -- Dark Attack
(1408, 4274, 1), -- Blunt Attack Weak Point
(1408, 4279, 1), -- Fire Attack Weak Point
(1408, 4285, 4), -- Resist Sleep
(1408, 4789, 6), -- NPC High Level
(1408, 4579, 9), -- Bleed
(1408, 4036, 9), -- Poison
-- Royal Guard of Empire
(1409, 4290, 1), -- Race
(1409, 4307, 1), -- Strong Type
(1409, 4275, 3), -- Sacred Attack Weak Point
(1409, 4278, 1), -- Dark Attack
(1409, 4274, 1), -- Blunt Attack Weak Point
(1409, 4279, 1), -- Fire Attack Weak Point
(1409, 4789, 6), -- NPC High Level
(1409, 4592, 9), -- Decrease P.Def
(1409, 4036, 9), -- Poison
-- Guardian Scarab
(1410, 4301, 1), -- Race
(1410, 4306, 1), -- Strong Type
(1410, 4284, 5), -- Resist Bleeding
(1410, 4071, 3), -- Resist Archery
(1410, 4274, 1), -- Blunt Attack Weak Point
(1410, 4789, 7), -- NPC High Level
(1410, 4317, 1), -- Increase Rage Might
(1410, 4599, 9), -- Decrease Speed
(1410, 4073, 9), -- Shock
(1410, 4047, 9), -- Sleep
-- Guardian Scarab
(1411, 4301, 1), -- Race
(1411, 4305, 1), -- Strong Type
(1411, 4284, 5), -- Resist Bleeding
(1411, 4071, 3), -- Resist Archery
(1411, 4274, 1), -- Blunt Attack Weak Point
(1411, 4789, 7), -- NPC High Level
(1411, 4077, 9), -- NPC Aura Burn
-- Ustralith
(1412, 4301, 1), -- Race
(1412, 4306, 1), -- Strong Type
(1412, 4284, 5), -- Resist Bleeding
(1412, 4071, 3), -- Resist Archery
(1412, 4274, 1), -- Blunt Attack Weak Point
(1412, 4789, 7), -- NPC High Level
(1412, 4613, 9), -- NPC Clan Heal
(1412, 4575, 2), -- NPC Clan Buff - Haste
(1412, 4577, 9), -- Decrease Accuracy
-- Ustralith
(1413, 4301, 1), -- Race
(1413, 4306, 1), -- Strong Type
(1413, 4284, 5), -- Resist Bleeding
(1413, 4071, 3), -- Resist Archery
(1413, 4274, 1), -- Blunt Attack Weak Point
(1413, 4789, 7), -- NPC High Level
(1413, 4582, 9), -- Poison
(1413, 4047, 9), -- Sleep
-- Assassin of Empire
(1414, 4290, 1), -- Race
(1414, 4306, 1), -- Strong Type
(1414, 4275, 3), -- Sacred Attack Weak Point
(1414, 4278, 1), -- Dark Attack
(1414, 4274, 1), -- Blunt Attack Weak Point
(1414, 4279, 1), -- Fire Attack Weak Point
(1414, 4789, 7), -- NPC High Level
(1414, 4603, 9), -- Decrease P.Atk
(1414, 4561, 9), -- NPC Fire Burn - Magic
(1414, 4613, 9), -- NPC Clan Heal
-- Assassin of Empire
(1415, 4290, 1), -- Race
(1415, 4306, 1), -- Strong Type
(1415, 4275, 3), -- Sacred Attack Weak Point
(1415, 4278, 1), -- Dark Attack
(1415, 4274, 1), -- Blunt Attack Weak Point
(1415, 4279, 1), -- Fire Attack Weak Point
(1415, 4789, 7), -- NPC High Level
(1415, 4579, 9), -- Bleed
-- Imperial Commander
(1416, 4290, 1), -- Race
(1416, 4306, 1), -- Strong Type
(1416, 4116, 6), -- Resist M. Atk.
(1416, 4275, 3), -- Sacred Attack Weak Point
(1416, 4278, 1), -- Dark Attack
(1416, 4274, 1), -- Blunt Attack Weak Point
(1416, 4279, 1), -- Fire Attack Weak Point
(1416, 4789, 7), -- NPC High Level
(1416, 4665, 9), -- NPC 100% HP Drain - Magic
(1416, 4160, 9), -- NPC Aura Burn - Magic
-- Imperial Commander
(1417, 4290, 1), -- Race
(1417, 4307, 1), -- Strong Type
(1417, 4084, 9), -- Resist Physical Attack
(1417, 4275, 3), -- Sacred Attack Weak Point
(1417, 4278, 1), -- Dark Attack
(1417, 4274, 1), -- Blunt Attack Weak Point
(1417, 4279, 1), -- Fire Attack Weak Point
(1417, 4789, 7), -- NPC High Level
(1417, 4002, 9), -- NPC HP Drain
-- Imperial Royal Guard
(1418, 4290, 1), -- Race
(1418, 4305, 1), -- Strong Type
(1418, 4084, 3), -- Resist Physical Attack
(1418, 4275, 3), -- Sacred Attack Weak Point
(1418, 4278, 1), -- Dark Attack
(1418, 4274, 1), -- Blunt Attack Weak Point
(1418, 4789, 7), -- NPC High Level
(1418, 4572, 9), -- NPC Triple Sonic Slash
-- Imperial Royal Guard
(1419, 4290, 1), -- Race
(1419, 4305, 1), -- Strong Type
(1419, 4084, 3), -- Resist Physical Attack
(1419, 4275, 3), -- Sacred Attack Weak Point
(1419, 4278, 1), -- Dark Attack
(1419, 4274, 1), -- Blunt Attack Weak Point
(1419, 4789, 7), -- NPC High Level
(1419, 4573, 9), -- NPC Sonic Blaster
(1419, 4572, 9), -- NPC Triple Sonic Slash
-- Ashuras
(1420, 4298, 1), -- Race
(1420, 4306, 1), -- Strong Type
(1420, 4278, 1), -- Dark Attack
(1420, 4333, 3), -- Resist Dark Attack
(1420, 4285, 2), -- Resist Sleep
(1420, 4275, 1), -- Sacred Attack Weak Point
(1420, 4789, 7), -- NPC High Level
(1420, 4317, 1), -- Increase Rage Might
(1420, 4072, 7), -- Shock
(1420, 4572, 7), -- NPC Triple Sonic Slash
(1420, 4076, 3), -- Reduction in movement speed
-- Ashuras
(1421, 4298, 1), -- Race
(1421, 4306, 1), -- Strong Type
(1421, 4278, 1), -- Dark Attack
(1421, 4333, 3), -- Resist Dark Attack
(1421, 4285, 2), -- Resist Sleep
(1421, 4275, 1), -- Sacred Attack Weak Point
(1421, 4789, 7), -- NPC High Level
(1421, 4592, 9), -- Decrease P.Def
-- Dancer of Empire
(1422, 4298, 1), -- Race
(1422, 4307, 1), -- Strong Type
(1422, 4278, 1), -- Dark Attack
(1422, 4333, 3), -- Resist Dark Attack
(1422, 4285, 2), -- Resist Sleep
(1422, 4275, 1), -- Sacred Attack Weak Point
(1422, 4233, 1), -- Vampiric Attack
(1422, 4789, 7), -- NPC High Level
(1422, 4566, 9), -- NPC Eruption - Magic
(1422, 4160, 9), -- NPC Aura Burn - Magic
(1422, 4657, 9), -- Hold
(1422, 4037, 2), -- Weaken P. Atk.
-- Dancer of Empire
(1423, 4298, 1), -- Race
(1423, 4307, 1), -- Strong Type
(1423, 4278, 1), -- Dark Attack
(1423, 4333, 3), -- Resist Dark Attack
(1423, 4285, 2), -- Resist Sleep
(1423, 4275, 1), -- Sacred Attack Weak Point
(1423, 4233, 1), -- Vampiric Attack
(1423, 4789, 7), -- NPC High Level
(1423, 4566, 9), -- NPC Eruption - Magic
(1423, 4160, 9), -- NPC Aura Burn - Magic
(1423, 4613, 9), -- NPC Clan Heal
-- Ashkenas
(1424, 4298, 1), -- Race
(1424, 4307, 1), -- Strong Type
(1424, 4278, 1), -- Dark Attack
(1424, 4333, 3), -- Resist Dark Attack
(1424, 4285, 2), -- Resist Sleep
(1424, 4275, 1), -- Sacred Attack Weak Point
(1424, 4789, 7), -- NPC High Level
(1424, 4077, 9), -- NPC Aura Burn
(1424, 4596, 9), -- Bleed
(1424, 4076, 3), -- Reduction in movement speed
(1424, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Ashkenas
(1425, 4298, 1), -- Race
(1425, 4307, 1), -- Strong Type
(1425, 4278, 1), -- Dark Attack
(1425, 4333, 3), -- Resist Dark Attack
(1425, 4285, 2), -- Resist Sleep
(1425, 4275, 1), -- Sacred Attack Weak Point
(1425, 4789, 7), -- NPC High Level
(1425, 4584, 9), -- Reducing P.Def Shock
(1425, 4565, 9), -- NPC Eruption
(1425, 4098, 9), -- Magic Skill Block
(1425, 4632, 3), -- NPC Buff - Acumen Empower WildMagic
-- Abraxion
(1426, 4298, 1), -- Race
(1426, 4305, 1), -- Strong Type
(1426, 4084, 6), -- Resist Physical Attack
(1426, 4278, 1), -- Dark Attack
(1426, 4333, 3), -- Resist Dark Attack
(1426, 4285, 2), -- Resist Sleep
(1426, 4275, 1), -- Sacred Attack Weak Point
(1426, 4789, 8), -- NPC High Level
(1426, 4576, 3), -- NPC Clan Buff - Damage Shield
(1426, 4585, 3), -- NPC Clan Buff - Berserk Might
(1426, 4595, 3), -- NPC Clan Buff - Acumen Shield
(1426, 4609, 4), -- NPC Clan Buff - Vampiric Rage
-- Abraxion
(1427, 4298, 1), -- Race
(1427, 4305, 1), -- Strong Type
(1427, 4084, 6), -- Resist Physical Attack
(1427, 4278, 1), -- Dark Attack
(1427, 4333, 3), -- Resist Dark Attack
(1427, 4285, 2), -- Resist Sleep
(1427, 4275, 1), -- Sacred Attack Weak Point
(1427, 4789, 8), -- NPC High Level
(1427, 4560, 9), -- NPC Fire Burn
-- Hasturan
(1428, 4298, 1), -- Race
(1428, 4306, 1), -- Strong Type
(1428, 4278, 1), -- Dark Attack
(1428, 4333, 3), -- Resist Dark Attack
(1428, 4285, 2), -- Resist Sleep
(1428, 4275, 1), -- Sacred Attack Weak Point
(1428, 4233, 1), -- Vampiric Attack
(1428, 4789, 8), -- NPC High Level
(1428, 4571, 9), -- NPC Blazing Circle
-- Hasturan
(1429, 4298, 1), -- Race
(1429, 4306, 1), -- Strong Type
(1429, 4278, 1), -- Dark Attack
(1429, 4333, 3), -- Resist Dark Attack
(1429, 4285, 2), -- Resist Sleep
(1429, 4275, 1), -- Sacred Attack Weak Point
(1429, 4233, 1), -- Vampiric Attack
(1429, 4789, 8), -- NPC High Level
(1429, 4151, 9), -- NPC Windstrike - Magic
(1429, 4072, 9), -- Shock
-- Arimanes
(1430, 4298, 1), -- Race
(1430, 4307, 1), -- Strong Type
(1430, 4278, 1), -- Dark Attack
(1430, 4333, 3), -- Resist Dark Attack
(1430, 4285, 2), -- Resist Sleep
(1430, 4275, 1), -- Sacred Attack Weak Point
(1430, 4789, 8), -- NPC High Level
(1430, 4257, 9), -- NPC Hydroblast - Magic
(1430, 4160, 9), -- NPC Aura Burn - Magic
(1430, 4585, 3), -- NPC Clan Buff - Berserk Might
(1430, 4601, 3), -- NPC Clan Buff - Acumen Focus
(1430, 4577, 9), -- Decrease Accuracy
-- Arimanes
(1431, 4298, 1), -- Race
(1431, 4307, 1), -- Strong Type
(1431, 4278, 1), -- Dark Attack
(1431, 4333, 3), -- Resist Dark Attack
(1431, 4285, 2), -- Resist Sleep
(1431, 4275, 1), -- Sacred Attack Weak Point
(1431, 4789, 8), -- NPC High Level
(1431, 4560, 9), -- NPC Fire Burn
-- Chakram Beetle
(1432, 4301, 1), -- Race
(1432, 4307, 1), -- Strong Type
(1432, 4284, 5), -- Resist Bleeding
(1432, 4071, 3), -- Resist Archery
(1432, 4274, 1), -- Blunt Attack Weak Point
(1432, 4084, 6), -- Resist Physical Attack
(1432, 4116, 6), -- Resist M. Atk.
(1432, 4789, 6), -- NPC High Level
(1432, 4040, 9), -- NPC Bow Attack
(1432, 4065, 9), -- NPC Heal
(1432, 4341, 1), -- Ultimate Buff, 3rd
-- Jamadar Beetle
(1433, 4301, 1), -- Race
(1433, 4307, 1), -- Strong Type
(1433, 4284, 5), -- Resist Bleeding
(1433, 4071, 3), -- Resist Archery
(1433, 4274, 1), -- Blunt Attack Weak Point
(1433, 4084, 6), -- Resist Physical Attack
(1433, 4116, 6), -- Resist M. Atk.
(1433, 4789, 6), -- NPC High Level
(1433, 4579, 8), -- Bleed
(1433, 4065, 7), -- NPC Heal
(1433, 4340, 1), -- Ultimate Buff, 2nd
-- Seer of Blood
(1434, 4298, 1), -- Race
(1434, 4307, 1), -- Strong Type
(1434, 4084, 9), -- Resist Physical Attack
(1434, 4278, 1), -- Dark Attack
(1434, 4333, 3), -- Resist Dark Attack
(1434, 4285, 2), -- Resist Sleep
(1434, 4275, 1), -- Sacred Attack Weak Point
(1434, 4789, 7), -- NPC High Level
(1434, 4605, 9), -- Fire Weakness
-- Guide of Offering
(1435, 4290, 1), -- Race
(1435, 4307, 1), -- Strong Type
(1435, 4275, 3), -- Sacred Attack Weak Point
(1435, 4278, 1), -- Dark Attack
(1435, 4274, 1), -- Blunt Attack Weak Point
(1435, 4279, 1), -- Fire Attack Weak Point
(1435, 4789, 6), -- NPC High Level
(1435, 4663, 1), -- NPC Hate
(1435, 4091, 1), -- NPC Ogre Stun
-- Leader of Offering
(1436, 4290, 1), -- Race
(1436, 4307, 1), -- Strong Type
(1436, 4275, 3), -- Sacred Attack Weak Point
(1436, 4278, 1), -- Dark Attack
(1436, 4274, 1), -- Blunt Attack Weak Point
(1436, 4279, 1), -- Fire Attack Weak Point
(1436, 4789, 6), -- NPC High Level
(1436, 4605, 9), -- Fire Weakness
-- Offering Bug
(1437, 4301, 1), -- Race
(1437, 4305, 1), -- Strong Type
(1437, 4284, 5), -- Resist Bleeding
(1437, 4071, 3), -- Resist Archery
(1437, 4274, 1), -- Blunt Attack Weak Point
(1437, 4789, 6), -- NPC High Level
(1437, 4605, 8), -- Fire Weakness
(1437, 4613, 8), -- NPC Clan Heal
(1437, 4614, 8), -- NPC Death Bomb
-- Heathen Warrior
(1438, 4290, 1), -- Race
(1438, 4381, 1), -- Magic Skill Block
(1438, 4275, 3), -- Sacred Attack Weak Point
(1438, 4278, 1), -- Dark Attack
(1438, 4274, 1), -- Blunt Attack Weak Point
(1438, 4279, 1), -- Fire Attack Weak Point
(1438, 4032, 6), -- NPC Strike
-- Heathen Executed 
(1439, 4290, 1), -- Race
(1439, 4381, 1), -- Magic Skill Block
(1439, 4275, 3), -- Sacred Attack Weak Point
(1439, 4278, 1), -- Dark Attack
(1439, 4073, 6), -- Shock
-- Heathen Archer
(1440, 4290, 1), -- Race
(1440, 4381, 1), -- Magic Skill Block
(1440, 4275, 3), -- Sacred Attack Weak Point
(1440, 4278, 1), -- Dark Attack
(1440, 4274, 1), -- Blunt Attack Weak Point
(1440, 4279, 1), -- Fire Attack Weak Point
(1440, 4071, 2), -- Resist Archery
(1440, 4040, 6), -- NPC Bow Attack
-- Heathen Grunt
(1441, 4290, 1), -- Race
(1441, 4381, 1), -- Magic Skill Block
(1441, 4275, 3), -- Sacred Attack Weak Point
(1441, 4278, 1), -- Dark Attack
(1441, 4274, 1), -- Blunt Attack Weak Point
(1441, 4279, 1), -- Fire Attack Weak Point
(1441, 4072, 6), -- Shock
-- Heathen Knight
(1442, 4290, 1), -- Race
(1442, 4381, 1), -- Magic Skill Block
(1442, 4275, 3), -- Sacred Attack Weak Point
(1442, 4278, 1), -- Dark Attack
(1442, 4274, 1), -- Blunt Attack Weak Point
(1442, 4279, 1), -- Fire Attack Weak Point
(1442, 4232, 6), -- NPC AE Strike
-- Raider of Pastureland
(1443, 4295, 1), -- Race
(1443, 4032, 6), -- NPC Strike
-- Raider of Pastureland
(1444, 4295, 1), -- Race
-- Alpen Bandersnatch
(1445, 4293, 1), -- Race
-- Raider of Pastureland
(1446, 4295, 1), -- Race
(1446, 4085, 1), -- Critical Power
(1446, 4086, 1), -- Critical Chance
(1446, 4073, 6), -- Shock
-- Raider of Pastureland
(1447, 4295, 1), -- Race
(1447, 4067, 6), -- NPC Mortal Blow
-- Raider of Pastureland
(1448, 4295, 1), -- Race
-- Raider of Pastureland
(1449, 4295, 1), -- Race
(1449, 4085, 1), -- Critical Power
(1449, 4086, 1), -- Critical Chance
(1449, 4232, 6), -- NPC AE Strike
-- Alpen Grendel
(1450, 4293, 1), -- Race
(1450, 4072, 6), -- Shock
-- Alpen Kookaburra
(1451, 4293, 1), -- Race
(1451, 4311, 1), -- Feeble Type
(1451, 4285, 4), -- Resist Sleep
(1451, 2188, 1), -- Golden Spice
(1451, 2189, 1), -- Crystal Spice
-- Alpen Kookaburra
(1452, 4293, 1), -- Race
(1452, 4285, 4), -- Resist Sleep
(1452, 4032, 6), -- NPC Strike
(1452, 4092, 1), -- NPC Puma Stun
-- Alpen Kookaburra
(1453, 4293, 1), -- Race
(1453, 4285, 4), -- Resist Sleep
(1453, 4257, 6), -- NPC Hydroblast - Magic
(1453, 4160, 6), -- NPC Aura Burn - Magic
(1453, 4092, 1), -- NPC Puma Stun
-- Alpen Kookaburra
(1454, 4293, 1), -- Race
(1454, 4285, 4), -- Resist Sleep
(1454, 4067, 6), -- NPC Mortal Blow
(1454, 4092, 1), -- NPC Puma Stun
-- Alpen Kookaburra
(1455, 4293, 1), -- Race
(1455, 4285, 4), -- Resist Sleep
(1455, 4157, 6), -- NPC Blaze - Magic
(1455, 4160, 6), -- NPC Aura Burn - Magic
(1455, 4092, 1), -- NPC Puma Stun
-- Alpen Kookaburra
(1456, 4293, 1), -- Race
(1456, 4285, 4), -- Resist Sleep
(1456, 4032, 6), -- NPC Strike
(1456, 4092, 1), -- NPC Puma Stun
-- Alpen Kookaburra
(1457, 4293, 1), -- Race
(1457, 4285, 4), -- Resist Sleep
(1457, 4257, 6), -- NPC Hydroblast - Magic
(1457, 4160, 6), -- NPC Aura Burn - Magic
(1457, 4092, 1), -- NPC Puma Stun
-- Alpen Kookaburra
(1458, 4293, 1), -- Race
(1458, 4285, 4), -- Resist Sleep
(1458, 4067, 6), -- NPC Mortal Blow
(1458, 4092, 1), -- NPC Puma Stun
-- Alpen Kookaburra
(1459, 4293, 1), -- Race
(1459, 4285, 4), -- Resist Sleep
(1459, 4157, 6), -- NPC Blaze - Magic
(1459, 4160, 6), -- NPC Aura Burn - Magic
(1459, 4092, 1), -- NPC Puma Stun
-- Alpen Kookaburra
(1460, 4293, 1), -- Race
(1460, 4303, 1), -- Strong Type
(1460, 4285, 4), -- Resist Sleep
(1460, 4073, 6), -- Shock
(1460, 4232, 6), -- NPC AE Strike
(1460, 4074, 2), -- NPC Haste
-- Alpen Kookaburra
(1461, 4293, 1), -- Race
(1461, 4303, 1), -- Strong Type
(1461, 4285, 4), -- Resist Sleep
(1461, 4566, 6), -- NPC Eruption - Magic
(1461, 4160, 6), -- NPC Aura Burn - Magic
(1461, 4232, 6), -- NPC AE Strike
(1461, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Kookaburra
(1462, 4293, 1), -- Race
(1462, 4303, 1), -- Strong Type
(1462, 4285, 4), -- Resist Sleep
(1462, 4067, 6), -- NPC Mortal Blow
(1462, 4072, 6), -- Shock
(1462, 4074, 2), -- NPC Haste
-- Alpen Kookaburra
(1463, 4293, 1), -- Race
(1463, 4303, 1), -- Strong Type
(1463, 4285, 4), -- Resist Sleep
(1463, 4566, 6), -- NPC Eruption - Magic
(1463, 4160, 6), -- NPC Aura Burn - Magic
(1463, 4571, 6), -- NPC Blazing Circle
(1463, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Kookaburra
(1464, 4293, 1), -- Race
(1464, 4303, 1), -- Strong Type
(1464, 4285, 4), -- Resist Sleep
(1464, 4073, 6), -- Shock
(1464, 4232, 6), -- NPC AE Strike
(1464, 4074, 2), -- NPC Haste
-- Alpen Kookaburra
(1465, 4293, 1), -- Race
(1465, 4303, 1), -- Strong Type
(1465, 4285, 4), -- Resist Sleep
(1465, 4566, 6), -- NPC Eruption - Magic
(1465, 4160, 6), -- NPC Aura Burn - Magic
(1465, 4232, 6), -- NPC AE Strike
(1465, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Kookaburra
(1466, 4293, 1), -- Race
(1466, 4303, 1), -- Strong Type
(1466, 4285, 4), -- Resist Sleep
(1466, 4067, 6), -- NPC Mortal Blow
(1466, 4072, 6), -- Shock
(1466, 4074, 2), -- NPC Haste
-- Alpen Kookaburra
(1467, 4293, 1), -- Race
(1467, 4303, 1), -- Strong Type
(1467, 4285, 4), -- Resist Sleep
(1467, 4566, 6), -- NPC Eruption - Magic
(1467, 4160, 6), -- NPC Aura Burn - Magic
(1467, 4571, 6), -- NPC Blazing Circle
(1467, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Kookaburra
(1468, 4293, 1), -- Race
(1468, 4305, 1), -- Strong Type
(1468, 4285, 4), -- Resist Sleep
(1468, 4600, 6), -- Reducing P.Def Shock
(1468, 4589, 6), -- Decrease Speed
-- Alpen Kookaburra
(1469, 4293, 1), -- Race
(1469, 4305, 1), -- Strong Type
(1469, 4285, 4), -- Resist Sleep
(1469, 4566, 6), -- NPC Eruption - Magic
(1469, 4160, 6), -- NPC Aura Burn - Magic
(1469, 4571, 6), -- NPC Blazing Circle
(1469, 4657, 6), -- Hold
-- Alpen Buffalo
(1470, 4293, 1), -- Race
(1470, 4311, 1), -- Feeble Type
(1470, 4285, 4), -- Resist Sleep
(1470, 2188, 1), -- Golden Spice
(1470, 2189, 1), -- Crystal Spice
-- Alpen Buffalo
(1471, 4293, 1), -- Race
(1471, 4285, 4), -- Resist Sleep
(1471, 4032, 6), -- NPC Strike
(1471, 4099, 2), -- NPC Berserk
-- Alpen Buffalo
(1472, 4293, 1), -- Race
(1472, 4285, 4), -- Resist Sleep
(1472, 4257, 6), -- NPC Hydroblast - Magic
(1472, 4160, 6), -- NPC Aura Burn - Magic
(1472, 4099, 2), -- NPC Berserk
-- Alpen Buffalo
(1473, 4293, 1), -- Race
(1473, 4285, 4), -- Resist Sleep
(1473, 4067, 6), -- NPC Mortal Blow
(1473, 4099, 2), -- NPC Berserk
-- Alpen Buffalo
(1474, 4293, 1), -- Race
(1474, 4285, 4), -- Resist Sleep
(1474, 4157, 6), -- NPC Blaze - Magic
(1474, 4160, 6), -- NPC Aura Burn - Magic
(1474, 4099, 2), -- NPC Berserk
-- Alpen Buffalo
(1475, 4293, 1), -- Race
(1475, 4285, 4), -- Resist Sleep
(1475, 4032, 6), -- NPC Strike
(1475, 4099, 2), -- NPC Berserk
-- Alpen Buffalo
(1476, 4293, 1), -- Race
(1476, 4285, 4), -- Resist Sleep
(1476, 4257, 6), -- NPC Hydroblast - Magic
(1476, 4160, 6), -- NPC Aura Burn - Magic
(1476, 4099, 2), -- NPC Berserk
-- Alpen Buffalo
(1477, 4293, 1), -- Race
(1477, 4285, 4), -- Resist Sleep
(1477, 4067, 6), -- NPC Mortal Blow
(1477, 4099, 2), -- NPC Berserk
-- Alpen Buffalo
(1478, 4293, 1), -- Race
(1478, 4285, 4), -- Resist Sleep
(1478, 4157, 6), -- NPC Blaze - Magic
(1478, 4160, 6), -- NPC Aura Burn - Magic
(1478, 4099, 2), -- NPC Berserk
-- Alpen Buffalo
(1479, 4293, 1), -- Race
(1479, 4303, 1), -- Strong Type
(1479, 4285, 4), -- Resist Sleep
(1479, 4073, 6), -- Shock
(1479, 4232, 6), -- NPC AE Strike
(1479, 4074, 2), -- NPC Haste
-- Alpen Buffalo
(1480, 4293, 1), -- Race
(1480, 4303, 1), -- Strong Type
(1480, 4285, 4), -- Resist Sleep
(1480, 4566, 6), -- NPC Eruption - Magic
(1480, 4160, 6), -- NPC Aura Burn - Magic
(1480, 4232, 6), -- NPC AE Strike
(1480, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Buffalo
(1481, 4293, 1), -- Race
(1481, 4303, 1), -- Strong Type
(1481, 4285, 4), -- Resist Sleep
(1481, 4067, 6), -- NPC Mortal Blow
(1481, 4072, 6), -- Shock
(1481, 4074, 2), -- NPC Haste
-- Alpen Buffalo
(1482, 4293, 1), -- Race
(1482, 4303, 1), -- Strong Type
(1482, 4285, 4), -- Resist Sleep
(1482, 4566, 6), -- NPC Eruption - Magic
(1482, 4160, 6), -- NPC Aura Burn - Magic
(1482, 4571, 6), -- NPC Blazing Circle
(1482, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Buffalo
(1483, 4293, 1), -- Race
(1483, 4303, 1), -- Strong Type
(1483, 4285, 4), -- Resist Sleep
(1483, 4073, 6), -- Shock
(1483, 4232, 6), -- NPC AE Strike
(1483, 4074, 2), -- NPC Haste
-- Alpen Buffalo
(1484, 4293, 1), -- Race
(1484, 4303, 1), -- Strong Type
(1484, 4285, 4), -- Resist Sleep
(1484, 4566, 6), -- NPC Eruption - Magic
(1484, 4160, 6), -- NPC Aura Burn - Magic
(1484, 4232, 6), -- NPC AE Strike
(1484, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Buffalo
(1485, 4293, 1), -- Race
(1485, 4303, 1), -- Strong Type
(1485, 4285, 4), -- Resist Sleep
(1485, 4067, 6), -- NPC Mortal Blow
(1485, 4072, 6), -- Shock
(1485, 4074, 2), -- NPC Haste
-- Alpen Buffalo
(1486, 4293, 1), -- Race
(1486, 4303, 1), -- Strong Type
(1486, 4285, 4), -- Resist Sleep
(1486, 4566, 6), -- NPC Eruption - Magic
(1486, 4160, 6), -- NPC Aura Burn - Magic
(1486, 4571, 6), -- NPC Blazing Circle
(1486, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Buffalo
(1487, 4293, 1), -- Race
(1487, 4305, 1), -- Strong Type
(1487, 4285, 4), -- Resist Sleep
(1487, 4600, 6), -- Reducing P.Def Shock
(1487, 4589, 6), -- Decrease Speed
-- Alpen Buffalo
(1488, 4293, 1), -- Race
(1488, 4305, 1), -- Strong Type
(1488, 4285, 4), -- Resist Sleep
(1488, 4566, 6), -- NPC Eruption - Magic
(1488, 4160, 6), -- NPC Aura Burn - Magic
(1488, 4571, 6), -- NPC Blazing Circle
(1488, 4657, 6), -- Hold
-- Alpen Cougar
(1489, 4293, 1), -- Race
(1489, 4311, 1), -- Feeble Type
(1489, 4285, 4), -- Resist Sleep
(1489, 2188, 1), -- Golden Spice
(1489, 2189, 1), -- Crystal Spice
-- Alpen Cougar
(1490, 4293, 1), -- Race
(1490, 4285, 4), -- Resist Sleep
(1490, 4032, 6), -- NPC Strike
(1490, 4092, 1), -- NPC Puma Stun
-- Alpen Cougar
(1491, 4293, 1), -- Race
(1491, 4285, 4), -- Resist Sleep
(1491, 4257, 6), -- NPC Hydroblast - Magic
(1491, 4160, 6), -- NPC Aura Burn - Magic
(1491, 4092, 1), -- NPC Puma Stun
-- Alpen Cougar
(1492, 4293, 1), -- Race
(1492, 4285, 4), -- Resist Sleep
(1492, 4067, 6), -- NPC Mortal Blow
(1492, 4092, 1), -- NPC Puma Stun
-- Alpen Cougar
(1493, 4293, 1), -- Race
(1493, 4285, 4), -- Resist Sleep
(1493, 4157, 6), -- NPC Blaze - Magic
(1493, 4160, 6), -- NPC Aura Burn - Magic
(1493, 4092, 1), -- NPC Puma Stun
-- Alpen Cougar
(1494, 4293, 1), -- Race
(1494, 4285, 4), -- Resist Sleep
(1494, 4032, 6), -- NPC Strike
(1494, 4092, 1), -- NPC Puma Stun
-- Alpen Cougar
(1495, 4293, 1), -- Race
(1495, 4285, 4), -- Resist Sleep
(1495, 4257, 6), -- NPC Hydroblast - Magic
(1495, 4160, 6), -- NPC Aura Burn - Magic
(1495, 4092, 1), -- NPC Puma Stun
-- Alpen Cougar
(1496, 4293, 1), -- Race
(1496, 4285, 4), -- Resist Sleep
(1496, 4067, 6), -- NPC Mortal Blow
(1496, 4092, 1), -- NPC Puma Stun
-- Alpen Cougar
(1497, 4293, 1), -- Race
(1497, 4285, 4), -- Resist Sleep
(1497, 4157, 6), -- NPC Blaze - Magic
(1497, 4160, 6), -- NPC Aura Burn - Magic
(1497, 4092, 1), -- NPC Puma Stun
-- Alpen Cougar
(1498, 4293, 1), -- Race
(1498, 4303, 1), -- Strong Type
(1498, 4285, 4), -- Resist Sleep
(1498, 4073, 6), -- Shock
(1498, 4232, 6), -- NPC AE Strike
(1498, 4074, 2), -- NPC Haste
-- Alpen Cougar
(1499, 4293, 1), -- Race
(1499, 4303, 1), -- Strong Type
(1499, 4285, 4), -- Resist Sleep
(1499, 4566, 6), -- NPC Eruption - Magic
(1499, 4160, 6), -- NPC Aura Burn - Magic
(1499, 4232, 6), -- NPC AE Strike
(1499, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Cougar
(1500, 4293, 1), -- Race
(1500, 4303, 1), -- Strong Type
(1500, 4285, 4), -- Resist Sleep
(1500, 4067, 6), -- NPC Mortal Blow
(1500, 4072, 6), -- Shock
(1500, 4074, 2), -- NPC Haste
-- Alpen Cougar
(1501, 4293, 1), -- Race
(1501, 4303, 1), -- Strong Type
(1501, 4285, 4), -- Resist Sleep
(1501, 4566, 6), -- NPC Eruption - Magic
(1501, 4160, 6), -- NPC Aura Burn - Magic
(1501, 4571, 6), -- NPC Blazing Circle
(1501, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Cougar
(1502, 4293, 1), -- Race
(1502, 4303, 1), -- Strong Type
(1502, 4285, 4), -- Resist Sleep
(1502, 4073, 6), -- Shock
(1502, 4232, 6), -- NPC AE Strike
(1502, 4074, 2), -- NPC Haste
-- Alpen Cougar
(1503, 4293, 1), -- Race
(1503, 4303, 1), -- Strong Type
(1503, 4285, 4), -- Resist Sleep
(1503, 4566, 6), -- NPC Eruption - Magic
(1503, 4160, 6), -- NPC Aura Burn - Magic
(1503, 4232, 6), -- NPC AE Strike
(1503, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Cougar
(1504, 4293, 1), -- Race
(1504, 4303, 1), -- Strong Type
(1504, 4285, 4), -- Resist Sleep
(1504, 4067, 6), -- NPC Mortal Blow
(1504, 4072, 6), -- Shock
(1504, 4074, 2), -- NPC Haste
-- Alpen Cougar
(1505, 4293, 1), -- Race
(1505, 4303, 1), -- Strong Type
(1505, 4285, 4), -- Resist Sleep
(1505, 4566, 6), -- NPC Eruption - Magic
(1505, 4160, 6), -- NPC Aura Burn - Magic
(1505, 4571, 6), -- NPC Blazing Circle
(1505, 4633, 3), -- NPC Buff - Acumen Empower Berserk
-- Alpen Cougar
(1506, 4293, 1), -- Race
(1506, 4305, 1), -- Strong Type
(1506, 4285, 4), -- Resist Sleep
(1506, 4600, 6), -- Reducing P.Def Shock
(1506, 4589, 6), -- Decrease Speed
-- Alpen Cougar
(1507, 4293, 1), -- Race
(1507, 4305, 1), -- Strong Type
(1507, 4285, 4), -- Resist Sleep
(1507, 4566, 6), -- NPC Eruption - Magic
(1507, 4160, 6), -- NPC Aura Burn - Magic
(1507, 4571, 6), -- NPC Blazing Circle
(1507, 4657, 6), -- Hold
-- Splinter Stakato
(1508, 4301, 1), -- Race
(1508, 4071, 3), -- Resist Archery
(1508, 4116, 4), -- Resist M. Atk.
(1508, 4287, 2), -- Resist Hold
(1508, 4274, 1), -- Blunt Attack Weak Point
(1508, 4643, 6), -- Decrease Speed
-- Splinter Stakato Walker
(1509, 4301, 1), -- Race
(1509, 4071, 3), -- Resist Archery
(1509, 4116, 4), -- Resist M. Atk.
(1509, 4287, 2), -- Resist Hold
(1509, 4274, 1), -- Blunt Attack Weak Point
(1509, 4389, 4), -- Resist Mental Derangement
(1509, 4142, 6), -- NPC Fast Wind Fist
-- Splinter Stakato Soldier
(1510, 4301, 1), -- Race
(1510, 4071, 3), -- Resist Archery
(1510, 4116, 4), -- Resist M. Atk.
(1510, 4285, 4), -- Resist Sleep
(1510, 4274, 1), -- Blunt Attack Weak Point
(1510, 4073, 6), -- Shock
-- Splinter Stakato Drone
(1511, 4301, 1), -- Race
(1511, 4071, 3), -- Resist Archery
(1511, 4116, 9), -- Resist M. Atk.
(1511, 4285, 4), -- Resist Sleep
(1511, 4274, 1), -- Blunt Attack Weak Point
(1511, 4579, 6), -- Bleed
-- Splinter Stakato Drone
(1512, 4301, 1), -- Race
(1512, 4067, 6), -- NPC Mortal Blow
-- Needle Stakato
(1513, 4301, 1), -- Race
(1513, 4071, 3), -- Resist Archery
(1513, 4116, 4), -- Resist M. Atk.
(1513, 4287, 2), -- Resist Hold
(1513, 4274, 1), -- Blunt Attack Weak Point
(1513, 4643, 6), -- Decrease Speed
-- Needle Stakato Walker
(1514, 4301, 1), -- Race
(1514, 4071, 3), -- Resist Archery
(1514, 4116, 4), -- Resist M. Atk.
(1514, 4287, 2), -- Resist Hold
(1514, 4274, 1), -- Blunt Attack Weak Point
(1514, 4389, 4), -- Resist Mental Derangement
(1514, 4789, 2), -- NPC High Level
(1514, 4142, 7), -- NPC Fast Wind Fist
-- Needle Stakato Soldier
(1515, 4301, 1), -- Race
(1515, 4071, 3), -- Resist Archery
(1515, 4116, 4), -- Resist M. Atk.
(1515, 4285, 4), -- Resist Sleep
(1515, 4274, 1), -- Blunt Attack Weak Point
(1515, 4789, 2), -- NPC High Level
(1515, 4073, 7), -- Shock
-- Needle Stakato Drone
(1516, 4301, 1), -- Race
(1516, 4071, 3), -- Resist Archery
(1516, 4116, 9), -- Resist M. Atk.
(1516, 4285, 4), -- Resist Sleep
(1516, 4274, 1), -- Blunt Attack Weak Point
(1516, 4789, 3), -- NPC High Level
(1516, 4072, 7), -- Shock
-- Needle Stakato Drone
(1517, 4301, 1), -- Race
(1517, 4789, 3), -- NPC High Level
(1517, 4244, 7), -- NPC Wild Sweep
-- Frenzy Stakato Soldier
(1518, 4301, 1), -- Race
(1518, 4285, 4), -- Resist Sleep
(1518, 4032, 6), -- NPC Strike
-- Frenzy Stakato Drone
(1519, 4301, 1), -- Race
(1519, 4285, 4), -- Resist Sleep
(1519, 4789, 2), -- NPC High Level
(1519, 4067, 7), -- NPC Mortal Blow
-- Eye of Splendor
(1520, 4297, 1), -- Race
(1520, 4642, 3), -- NPC Fast Spell Casting
(1520, 4561, 6), -- NPC Fire Burn - Magic
(1520, 4631, 3), -- NPC Buff - Acumen Shield WildMagic
(1520, 4035, 6), -- Poison
-- Claws of Splendor
(1521, 4297, 1), -- Race
(1521, 4642, 3), -- NPC Fast Spell Casting
(1521, 4160, 6), -- NPC Aura Burn - Magic
(1521, 4638, 3), -- NPC Clan Buff - Acumen Empower WildMagic
(1521, 4035, 6), -- Poison
-- Claws of Splendor
(1522, 4293, 1), -- Race
(1522, 4085, 1), -- Critical Power
(1522, 4086, 1), -- Critical Chance
(1522, 4073, 6), -- Shock
(1522, 4571, 6), -- NPC Blazing Circle
(1522, 4094, 6), -- NPC Cancel Magic
-- Flash of Splendor
(1523, 4297, 1), -- Race
(1523, 4642, 3), -- NPC Fast Spell Casting
(1523, 4563, 6), -- NPC Solar Flare - Magic
(1523, 4160, 6), -- NPC Aura Burn - Magic
(1523, 4569, 6), -- NPC AE Solar Flare - Magic
(1523, 4633, 3), -- NPC Buff - Acumen Empower Berserk
(1523, 4035, 6), -- Poison
-- Blade of Splendor
(1524, 4297, 1), -- Race
(1524, 4085, 1), -- Critical Power
(1524, 4086, 1), -- Critical Chance
(1524, 4641, 6), -- NPC Super Strike
(1524, 4671, 1), -- AV - Teleport
-- Blade of Splendor
(1525, 4297, 1), -- Race
(1525, 4085, 1), -- Critical Power
(1525, 4086, 1), -- Critical Chance
(1525, 4641, 6), -- NPC Super Strike
(1525, 4671, 1), -- AV - Teleport
-- Wisdom of Splendor
(1526, 4297, 1), -- Race
(1526, 4642, 3), -- NPC Fast Spell Casting
(1526, 4561, 6), -- NPC Fire Burn - Magic
(1526, 4634, 3), -- NPC Buff - Acumen Empower DamageShield
(1526, 4104, 6), -- Flame
-- Anger of Splendor
(1527, 4297, 1), -- Race
(1527, 4084, 3), -- Resist Physical Attack
(1527, 4642, 3), -- NPC Fast Spell Casting
(1527, 4566, 6), -- NPC Eruption - Magic
(1527, 4160, 6), -- NPC Aura Burn - Magic
(1527, 4632, 3), -- NPC Buff - Acumen Empower WildMagic
(1527, 4102, 2), -- Become weak against line of fire.
-- Anger of Splendor
(1528, 4293, 1), -- Race
(1528, 4085, 1), -- Critical Power
(1528, 4086, 1), -- Critical Chance
(1528, 4067, 6), -- NPC Mortal Blow
(1528, 4571, 6), -- NPC Blazing Circle
(1528, 4094, 6), -- NPC Cancel Magic
-- Soul of Splendor
(1529, 4297, 1), -- Race
(1529, 4642, 3), -- NPC Fast Spell Casting
(1529, 4630, 6), -- NPC MR - Twister
(1529, 4561, 6), -- NPC Fire Burn - Magic
(1529, 4571, 6), -- NPC Blazing Circle
(1529, 4635, 3), -- NPC Buff - Acumen Berserk WildMagic
(1529, 4035, 6), -- Poison
-- Victory of Splendor
(1530, 4297, 1), -- Race
(1530, 4642, 3), -- NPC Fast Spell Casting
(1530, 4160, 6), -- NPC Aura Burn - Magic
(1530, 4636, 3), -- NPC Buff - Acumen Berserk DamageShield
(1530, 4119, 3), -- Fall in accuracy
-- Punishment of Splendor
(1531, 4297, 1), -- Race
(1531, 4085, 1), -- Critical Power
(1531, 4086, 1), -- Critical Chance
(1531, 4641, 6), -- NPC Super Strike
(1531, 4671, 1), -- AV - Teleport
-- Shout of Splendor
(1532, 4297, 1), -- Race
(1532, 4084, 3), -- Resist Physical Attack
(1532, 4642, 3), -- NPC Fast Spell Casting
(1532, 4158, 6), -- NPC Prominence - Magic
(1532, 4160, 6), -- NPC Aura Burn - Magic
(1532, 4566, 6), -- NPC Eruption - Magic
(1532, 4638, 3), -- NPC Clan Buff - Acumen Empower WildMagic
(1532, 4104, 6), -- Flame
-- Alliance of Splendor
(1533, 4297, 1), -- Race
(1533, 4642, 3), -- NPC Fast Spell Casting
(1533, 4561, 6), -- NPC Fire Burn - Magic
(1533, 4633, 3), -- NPC Buff - Acumen Empower Berserk
(1533, 4076, 3), -- Reduction in movement speed
-- Alliance of Splendor
(1534, 4293, 1), -- Race
(1534, 4085, 1), -- Critical Power
(1534, 4086, 1), -- Critical Chance
(1534, 4032, 6), -- NPC Strike
(1534, 4571, 6), -- NPC Blazing Circle
(1534, 4094, 6), -- NPC Cancel Magic
-- Signet of Splendor
(1535, 4297, 1), -- Race
(1535, 4642, 3), -- NPC Fast Spell Casting
(1535, 4630, 6), -- NPC MR - Twister
(1535, 4160, 6), -- NPC Aura Burn - Magic
(1535, 4571, 6), -- NPC Blazing Circle
(1535, 4636, 3), -- NPC Buff - Acumen Berserk DamageShield
(1535, 4104, 6), -- Flame
-- Crown of Splendor
(1536, 4297, 1), -- Race
(1536, 4084, 3), -- Resist Physical Attack
(1536, 4642, 3), -- NPC Fast Spell Casting
(1536, 4561, 6), -- NPC Fire Burn - Magic
(1536, 4637, 3), -- NPC Buff - Acumen WildMagic DamageShield
(1536, 4102, 2), -- Become weak against line of fire.
-- Fang of Splendor
(1537, 4297, 1), -- Race
(1537, 4642, 3), -- NPC Fast Spell Casting
(1537, 4155, 6), -- NPC Twister - Magic
(1537, 4160, 6), -- NPC Aura Burn - Magic
(1537, 4639, 3), -- NPC Clan Buff - Acumen Empower Berserk
(1537, 4104, 6), -- Flame
-- Fang of Splendor
(1538, 4293, 1), -- Race
(1538, 4085, 1), -- Critical Power
(1538, 4086, 1), -- Critical Chance
(1538, 4073, 6), -- Shock
(1538, 4571, 6), -- NPC Blazing Circle
(1538, 4094, 6), -- NPC Cancel Magic
-- Wailing of Splendor
(1539, 4297, 1), -- Race
(1539, 4085, 1), -- Critical Power
(1539, 4086, 1), -- Critical Chance
(1539, 4641, 6), -- NPC Super Strike
(1539, 4671, 1), -- AV - Teleport
-- Wailing of Splendor
(1540, 4297, 1), -- Race
(1540, 4085, 1), -- Critical Power
(1540, 4086, 1), -- Critical Chance
(1540, 4641, 6), -- NPC Super Strike
(1540, 4671, 1), -- AV - Teleport
-- Pilgrim of Splendor
(1541, 4297, 1), -- Race
(1541, 4642, 3), -- NPC Fast Spell Casting
(1541, 4158, 6), -- NPC Prominence - Magic
(1541, 4566, 6), -- NPC Eruption - Magic
(1541, 4638, 3), -- NPC Clan Buff - Acumen Empower WildMagic
(1541, 4036, 6), -- Poison
-- Disciple of Pilgrim
(1542, 4297, 1), -- Race
(1542, 4642, 3), -- NPC Fast Spell Casting
(1542, 4158, 6), -- NPC Prominence - Magic
(1542, 4561, 6), -- NPC Fire Burn - Magic
-- Page of Pilgrim
(1543, 4297, 1), -- Race
(1543, 4642, 3), -- NPC Fast Spell Casting
(1543, 4158, 6), -- NPC Prominence - Magic
(1543, 4561, 6), -- NPC Fire Burn - Magic
(1543, 4097, 6), -- NPC Chant of Life
-- Judge of Splendor
(1544, 4297, 1), -- Race
(1544, 4084, 3), -- Resist Physical Attack
(1544, 4642, 3), -- NPC Fast Spell Casting
(1544, 4155, 6), -- NPC Twister - Magic
(1544, 4569, 6), -- NPC AE Solar Flare - Magic
(1544, 4635, 3), -- NPC Buff - Acumen Berserk WildMagic
(1544, 4640, 6), -- Sleep
-- Judge of Fire
(1545, 4297, 1), -- Race
(1545, 4642, 3), -- NPC Fast Spell Casting
(1545, 4155, 6), -- NPC Twister - Magic
(1545, 4160, 6), -- NPC Aura Burn - Magic
-- Judge of Light
(1546, 4297, 1), -- Race
(1546, 4642, 3), -- NPC Fast Spell Casting
(1546, 4563, 6), -- NPC Solar Flare - Magic
(1546, 4561, 6), -- NPC Fire Burn - Magic
(1546, 4097, 6), -- NPC Chant of Life
-- Corrupted Knight
(1547, 4290, 1), -- Race
(1547, 4084, 6), -- Resist Physical Attack
(1547, 4275, 3), -- Sacred Attack Weak Point
(1547, 4278, 1), -- Dark Attack
(1547, 4274, 1), -- Blunt Attack Weak Point
(1547, 4671, 1), -- AV - Teleport
(1547, 4649, 6), -- Poison
(1547, 4032, 6), -- NPC Strike
-- Resurrected Knight
(1548, 4290, 1), -- Race
(1548, 4084, 6), -- Resist Physical Attack
(1548, 4275, 3), -- Sacred Attack Weak Point
(1548, 4278, 1), -- Dark Attack
(1548, 4274, 1), -- Blunt Attack Weak Point
(1548, 4649, 6), -- Poison
-- Corrupted Guard
(1549, 4290, 1), -- Race
(1549, 4084, 6), -- Resist Physical Attack
(1549, 4275, 3), -- Sacred Attack Weak Point
(1549, 4278, 1), -- Dark Attack
(1549, 4274, 1), -- Blunt Attack Weak Point
(1549, 4649, 6), -- Poison
(1549, 4067, 6), -- NPC Mortal Blow
-- Corrupted Guard
(1550, 4290, 1), -- Race
(1550, 4084, 6), -- Resist Physical Attack
(1550, 4275, 3), -- Sacred Attack Weak Point
(1550, 4278, 1), -- Dark Attack
(1550, 4274, 1), -- Blunt Attack Weak Point
(1550, 4671, 1), -- AV - Teleport
(1550, 4649, 6), -- Poison
(1550, 4073, 6), -- Shock
-- Resurrected Guard
(1551, 4290, 1), -- Race
(1551, 4084, 6), -- Resist Physical Attack
(1551, 4275, 3), -- Sacred Attack Weak Point
(1551, 4278, 1), -- Dark Attack
(1551, 4274, 1), -- Blunt Attack Weak Point
(1551, 4649, 6), -- Poison
(1551, 4579, 6), -- Bleed
-- Resurrected Guard
(1552, 4290, 1), -- Race
(1552, 4084, 6), -- Resist Physical Attack
(1552, 4275, 3), -- Sacred Attack Weak Point
(1552, 4278, 1), -- Dark Attack
(1552, 4274, 1), -- Blunt Attack Weak Point
(1552, 4671, 1), -- AV - Teleport
(1552, 4649, 6), -- Poison
(1552, 4073, 6), -- Shock
-- Trampled Man
(1553, 4290, 1), -- Race
(1553, 4275, 3), -- Sacred Attack Weak Point
(1553, 4278, 1), -- Dark Attack
(1553, 4279, 1), -- Fire Attack Weak Point
(1553, 4649, 6), -- Poison
(1553, 4001, 6), -- NPC Windstrike
-- Trampled Man
(1554, 4290, 1), -- Race
(1554, 4275, 3), -- Sacred Attack Weak Point
(1554, 4278, 1), -- Dark Attack
(1554, 4279, 1), -- Fire Attack Weak Point
(1554, 4671, 1), -- AV - Teleport
(1554, 4649, 6), -- Poison
-- Slaughter Executioner
(1555, 4290, 1), -- Race
(1555, 4275, 3), -- Sacred Attack Weak Point
(1555, 4278, 1), -- Dark Attack
(1555, 4279, 1), -- Fire Attack Weak Point
(1555, 4028, 3), -- Enhance P. Atk.
(1555, 4581, 6), -- Hold
-- Slaughter Executioner
(1556, 4290, 1), -- Race
(1556, 4275, 3), -- Sacred Attack Weak Point
(1556, 4278, 1), -- Dark Attack
(1556, 4279, 1), -- Fire Attack Weak Point
(1556, 4074, 2), -- NPC Haste
(1556, 4596, 6), -- Bleed
-- Bone Snatcher
(1557, 4295, 1), -- Race
(1557, 4672, 1), -- NPC Corpse Remove
(1557, 4155, 6), -- NPC Twister - Magic
(1557, 4561, 6), -- NPC Fire Burn - Magic
(1557, 4654, 6), -- NPC Death Link
(1557, 4047, 6), -- Hold
(1557, 4650, 1), -- NPC AE - Dispel Hold
-- Bone Snatcher
(1558, 4295, 1), -- Race
(1558, 4155, 6), -- NPC Twister - Magic
(1558, 4561, 6), -- NPC Fire Burn - Magic
(1558, 4654, 6), -- NPC Death Link
(1558, 4098, 6), -- Magic Skill Block
(1558, 4652, 1), -- NPC AE - Dispel Silence
-- Bone Maker
(1559, 4295, 1), -- Race
(1559, 4672, 1), -- NPC Corpse Remove
(1559, 4597, 6), -- Bleed
(1559, 4561, 6), -- NPC Fire Burn - Magic
(1559, 4654, 6), -- NPC Death Link
(1559, 4047, 6), -- Hold
(1559, 4650, 1), -- NPC AE - Dispel Hold
-- Bone Shaper
(1560, 4295, 1), -- Race
(1560, 4597, 6), -- Bleed
(1560, 4561, 6), -- NPC Fire Burn - Magic
(1560, 4654, 6), -- NPC Death Link
(1560, 4098, 6), -- Magic Skill Block
(1560, 4652, 1), -- NPC AE - Dispel Silence
-- Sacrificed Man
(1561, 4290, 1), -- Race
(1561, 4275, 3), -- Sacred Attack Weak Point
(1561, 4278, 1), -- Dark Attack
(1561, 4279, 1), -- Fire Attack Weak Point
(1561, 4671, 1), -- AV - Teleport
(1561, 4649, 6), -- Poison
(1561, 4073, 6), -- Shock
-- Guillotine's Ghost
(1562, 4291, 1), -- Race
(1562, 4281, 2), -- Wind Attack Weak Point
(1562, 4276, 1), -- Archery Attack Weak Point
(1562, 4635, 3), -- NPC Buff - Acumen Berserk WildMagic
(1562, 4596, 6), -- Bleed
-- Bone Collector
(1563, 4295, 1), -- Race
(1563, 4672, 1), -- NPC Corpse Remove
(1563, 4597, 6), -- Bleed
(1563, 4561, 6), -- NPC Fire Burn - Magic
(1563, 4654, 6), -- NPC Death Link
(1563, 4047, 6), -- Hold
(1563, 4650, 1), -- NPC AE - Dispel Hold
-- Skull Collector
(1564, 4295, 1), -- Race
(1564, 4672, 1), -- NPC Corpse Remove
(1564, 4658, 6), -- Hold
(1564, 4561, 6), -- NPC Fire Burn - Magic
(1564, 4654, 6), -- NPC Death Link
(1564, 4076, 3), -- Reduction in movement speed
(1564, 4651, 3), -- NPC AE - Dispel Slow
-- Bone Animator
(1565, 4295, 1), -- Race
(1565, 4155, 6), -- NPC Twister - Magic
(1565, 4561, 6), -- NPC Fire Burn - Magic
(1565, 4654, 6), -- NPC Death Link
(1565, 4047, 6), -- Hold
(1565, 4650, 1), -- NPC AE - Dispel Hold
-- Skull Animator
(1566, 4295, 1), -- Race
(1566, 4597, 6), -- Bleed
(1566, 4561, 6), -- NPC Fire Burn - Magic
(1566, 4654, 6), -- NPC Death Link
(1566, 4098, 6), -- Magic Skill Block
(1566, 4652, 1), -- NPC AE - Dispel Silence
-- Bone Slayer
(1567, 4295, 1), -- Race
(1567, 4573, 6), -- NPC Sonic Blaster
(1567, 4654, 6), -- NPC Death Link
(1567, 4138, 6), -- NPC AE - Corpse Burst
-- Devil Bat
(1568, 4292, 1), -- Race
(1568, 4281, 2), -- Wind Attack Weak Point
(1568, 4276, 1), -- Archery Attack Weak Point
(1568, 4584, 6), -- Reducing P.Def Shock
(1568, 4664, 6), -- NPC 100% HP Drain
(1568, 4029, 3), -- Enhance P. Def.
(1568, 4596, 6), -- Bleed
-- Devil Bat
(1569, 4292, 1), -- Race
(1569, 4281, 2), -- Wind Attack Weak Point
(1569, 4276, 1), -- Archery Attack Weak Point
(1569, 4664, 6), -- NPC 100% HP Drain
-- Ghost of Betrayer
(1570, 4290, 1), -- Race
(1570, 4275, 3), -- Sacred Attack Weak Point
(1570, 4278, 1), -- Dark Attack
(1570, 4279, 1), -- Fire Attack Weak Point
(1570, 4671, 1), -- AV - Teleport
(1570, 4649, 6), -- Poison
(1570, 4592, 6), -- Decrease P.Def
-- Ghost of Rebellion Soldier
(1571, 4290, 1), -- Race
(1571, 4084, 6), -- Resist Physical Attack
(1571, 4275, 3), -- Sacred Attack Weak Point
(1571, 4278, 1), -- Dark Attack
(1571, 4274, 1), -- Blunt Attack Weak Point
(1571, 4074, 2), -- NPC Haste
(1571, 4581, 6), -- Hold
-- Bone Sweeper
(1572, 4295, 1), -- Race
(1572, 4672, 1), -- NPC Corpse Remove
(1572, 4597, 6), -- Bleed
(1572, 4561, 6), -- NPC Fire Burn - Magic
(1572, 4654, 6), -- NPC Death Link
(1572, 4076, 3), -- Reduction in movement speed
(1572, 4651, 3), -- NPC AE - Dispel Slow
-- Atrox
(1573, 4301, 1), -- Race
(1573, 4279, 2), -- Fire Attack Weak Point
(1573, 4582, 6), -- Poison
(1573, 4664, 6), -- NPC 100% HP Drain
(1573, 4028, 3), -- Enhance P. Atk.
(1573, 4596, 6), -- Bleed
-- Bone Grinder
(1574, 4295, 1), -- Race
(1574, 4573, 7), -- NPC Sonic Blaster
(1574, 4654, 7), -- NPC Death Link
(1574, 4138, 7), -- NPC AE - Corpse Burst
-- Bone Grinder
(1575, 4295, 1), -- Race
(1575, 4658, 7), -- Hold
(1575, 4561, 7), -- NPC Fire Burn - Magic
-- Ghost of Guillotine 
(1576, 4291, 1), -- Race
(1576, 4281, 2), -- Wind Attack Weak Point
(1576, 4276, 1), -- Archery Attack Weak Point
(1576, 4622, 7), -- NPC AE - 80% HP Drain - Magic
(1576, 4561, 7), -- NPC Fire Burn - Magic
(1576, 4664, 7), -- NPC 100% HP Drain
(1576, 4596, 7), -- Bleed
-- Ghost of Guillotine 
(1577, 4291, 1), -- Race
(1577, 4281, 2), -- Wind Attack Weak Point
(1577, 4276, 1), -- Archery Attack Weak Point
(1577, 4593, 7), -- Decrease P.Def
(1577, 4561, 7), -- NPC Fire Burn - Magic
(1577, 4596, 7), -- Bleed
-- Behemoth Zombie
(1578, 4290, 1), -- Race
(1578, 4084, 6), -- Resist Physical Attack
(1578, 4275, 3), -- Sacred Attack Weak Point
(1578, 4278, 1), -- Dark Attack
(1578, 4279, 1), -- Fire Attack Weak Point
(1578, 4789, 2), -- NPC High Level
(1578, 4671, 1), -- AV - Teleport
(1578, 4649, 7), -- Poison
(1578, 4073, 7), -- Shock
-- Ghost of Rebellion Leader
(1579, 4290, 1), -- Race
(1579, 4084, 6), -- Resist Physical Attack
(1579, 4275, 3), -- Sacred Attack Weak Point
(1579, 4278, 1), -- Dark Attack
(1579, 4274, 1), -- Blunt Attack Weak Point
(1579, 4789, 2), -- NPC High Level
(1579, 4635, 3), -- NPC Buff - Acumen Berserk WildMagic
(1579, 4596, 7), -- Bleed
-- Bone Caster
(1580, 4295, 1), -- Race
(1580, 4789, 2), -- NPC High Level
(1580, 4155, 7), -- NPC Twister - Magic
(1580, 4561, 7), -- NPC Fire Burn - Magic
(1580, 4654, 7), -- NPC Death Link
(1580, 4047, 7), -- Hold
(1580, 4650, 1), -- NPC AE - Dispel Hold
-- Bone Puppeteer
(1581, 4295, 1), -- Race
(1581, 4789, 2), -- NPC High Level
(1581, 4597, 7), -- Bleed
(1581, 4561, 7), -- NPC Fire Burn - Magic
(1581, 4654, 7), -- NPC Death Link
(1581, 4098, 7), -- Magic Skill Block
(1581, 4652, 1), -- NPC AE - Dispel Silence
-- Vampire Soldier
(1582, 4298, 1), -- Race
(1582, 4278, 1), -- Dark Attack
(1582, 4333, 3), -- Resist Dark Attack
(1582, 4285, 2), -- Resist Sleep
(1582, 4275, 1), -- Sacred Attack Weak Point
(1582, 4789, 2), -- NPC High Level
(1582, 4582, 7), -- Poison
(1582, 4664, 7), -- NPC 100% HP Drain
(1582, 4028, 3), -- Enhance P. Atk.
(1582, 4596, 7), -- Bleed
(1582, 4663, 1), -- NPC Hate
-- Bone Scavenger
(1583, 4295, 1), -- Race
(1583, 4789, 2), -- NPC High Level
(1583, 4672, 1), -- NPC Corpse Remove
(1583, 4597, 7), -- Bleed
(1583, 4561, 7), -- NPC Fire Burn - Magic
(1583, 4654, 7), -- NPC Death Link
(1583, 4047, 7), -- Hold
(1583, 4650, 1), -- NPC AE - Dispel Hold
-- Bone Scavenger
(1584, 4295, 1), -- Race
(1584, 4789, 2), -- NPC High Level
(1584, 4257, 7), -- NPC Hydroblast - Magic
(1584, 4561, 7), -- NPC Fire Burn - Magic
(1584, 4654, 7), -- NPC Death Link
(1584, 4098, 7), -- Magic Skill Block
(1584, 4652, 1), -- NPC AE - Dispel Silence
-- Vampire Magician
(1585, 4298, 1), -- Race
(1585, 4278, 1), -- Dark Attack
(1585, 4333, 3), -- Resist Dark Attack
(1585, 4285, 2), -- Resist Sleep
(1585, 4275, 1), -- Sacred Attack Weak Point
(1585, 4789, 2), -- NPC High Level
(1585, 4590, 7), -- Decrease Speed
(1585, 4561, 7), -- NPC Fire Burn - Magic
(1585, 4664, 7), -- NPC 100% HP Drain
(1585, 4596, 7), -- Bleed
(1585, 4047, 6), -- Hold
(1585, 4650, 1), -- NPC AE - Dispel Hold
-- Vampire Adept
(1586, 4298, 1), -- Race
(1586, 4278, 1), -- Dark Attack
(1586, 4333, 3), -- Resist Dark Attack
(1586, 4285, 2), -- Resist Sleep
(1586, 4275, 1), -- Sacred Attack Weak Point
(1586, 4789, 2), -- NPC High Level
(1586, 4152, 7), -- NPC HP Drain - Magic
(1586, 4561, 7), -- NPC Fire Burn - Magic
(1586, 4664, 7), -- NPC 100% HP Drain
(1586, 4596, 7), -- Bleed
(1586, 4033, 7), -- NPC Burn
-- Vampire Warrior
(1587, 4298, 1), -- Race
(1587, 4278, 1), -- Dark Attack
(1587, 4333, 3), -- Resist Dark Attack
(1587, 4285, 2), -- Resist Sleep
(1587, 4275, 1), -- Sacred Attack Weak Point
(1587, 4789, 2), -- NPC High Level
(1587, 4582, 7), -- Poison
(1587, 4664, 7), -- NPC 100% HP Drain
(1587, 4099, 2), -- NPC Berserk
(1587, 4596, 7), -- Bleed
(1587, 4663, 1), -- NPC Hate
-- Vampire Wizard
(1588, 4298, 1), -- Race
(1588, 4278, 1), -- Dark Attack
(1588, 4333, 3), -- Resist Dark Attack
(1588, 4285, 2), -- Resist Sleep
(1588, 4275, 1), -- Sacred Attack Weak Point
(1588, 4789, 3), -- NPC High Level
(1588, 4671, 1), -- AV - Teleport
(1588, 4152, 7), -- NPC HP Drain - Magic
(1588, 4561, 7), -- NPC Fire Burn - Magic
(1588, 4664, 7), -- NPC 100% HP Drain
(1588, 4596, 7), -- Bleed
-- Vampire Wizard
(1589, 4298, 1), -- Race
(1589, 4278, 1), -- Dark Attack
(1589, 4333, 3), -- Resist Dark Attack
(1589, 4285, 2), -- Resist Sleep
(1589, 4275, 1), -- Sacred Attack Weak Point
(1589, 4789, 3), -- NPC High Level
(1589, 4671, 1), -- AV - Teleport
(1589, 4622, 7), -- NPC AE - 80% HP Drain - Magic
(1589, 4561, 7), -- NPC Fire Burn - Magic
(1589, 4664, 7), -- NPC 100% HP Drain
(1589, 4596, 7), -- Bleed
-- Vampire Magister
(1590, 4298, 1), -- Race
(1590, 4278, 1), -- Dark Attack
(1590, 4333, 3), -- Resist Dark Attack
(1590, 4285, 2), -- Resist Sleep
(1590, 4275, 1), -- Sacred Attack Weak Point
(1590, 4789, 3), -- NPC High Level
(1590, 4157, 7), -- NPC Blaze - Magic
(1590, 4561, 7), -- NPC Fire Burn - Magic
(1590, 4664, 7), -- NPC 100% HP Drain
(1590, 4596, 7), -- Bleed
-- Vampire Magister
(1591, 4298, 1), -- Race
(1591, 4278, 1), -- Dark Attack
(1591, 4333, 3), -- Resist Dark Attack
(1591, 4285, 2), -- Resist Sleep
(1591, 4275, 1), -- Sacred Attack Weak Point
(1591, 4789, 3), -- NPC High Level
(1591, 4157, 7), -- NPC Blaze - Magic
(1591, 4561, 7), -- NPC Fire Burn - Magic
(1591, 4664, 7), -- NPC 100% HP Drain
(1591, 4596, 7), -- Bleed
(1591, 4033, 7), -- NPC Burn
-- Vampire Magister
(1592, 4298, 1), -- Race
(1592, 4278, 1), -- Dark Attack
(1592, 4333, 3), -- Resist Dark Attack
(1592, 4285, 2), -- Resist Sleep
(1592, 4275, 1), -- Sacred Attack Weak Point
(1592, 4789, 3), -- NPC High Level
(1592, 4257, 7), -- NPC Hydroblast - Magic
(1592, 4561, 7), -- NPC Fire Burn - Magic
-- Vampire Warlord
(1593, 4298, 1), -- Race
(1593, 4278, 1), -- Dark Attack
(1593, 4333, 3), -- Resist Dark Attack
(1593, 4285, 2), -- Resist Sleep
(1593, 4275, 1), -- Sacred Attack Weak Point
(1593, 4789, 3), -- NPC High Level
(1593, 4582, 7), -- Poison
(1593, 4664, 7), -- NPC 100% HP Drain
(1593, 4585, 3), -- NPC Clan Buff - Berserk Might
(1593, 4596, 7), -- Bleed
-- Vampire Warlord
(1594, 4298, 1), -- Race
(1594, 4278, 1), -- Dark Attack
(1594, 4333, 3), -- Resist Dark Attack
(1594, 4285, 2), -- Resist Sleep
(1594, 4275, 1), -- Sacred Attack Weak Point
(1594, 4789, 3), -- NPC High Level
(1594, 4582, 7), -- Poison
(1594, 4664, 7), -- NPC 100% HP Drain
(1594, 4028, 3), -- Enhance P. Atk.
(1594, 4596, 7), -- Bleed
(1594, 4663, 1), -- NPC Hate
-- Vampire Warlord
(1595, 4298, 1), -- Race
(1595, 4278, 1), -- Dark Attack
(1595, 4333, 3), -- Resist Dark Attack
(1595, 4285, 2), -- Resist Sleep
(1595, 4275, 1), -- Sacred Attack Weak Point
(1595, 4789, 3), -- NPC High Level
(1595, 4664, 7), -- NPC 100% HP Drain
(1595, 4078, 7), -- NPC Flamestrike
(1595, 4663, 1), -- NPC Hate
-- Requiem Lord
(1596, 4295, 1), -- Race
(1596, 4672, 1), -- NPC Corpse Remove
(1596, 4593, 6), -- Decrease P.Def
(1596, 4561, 6), -- NPC Fire Burn - Magic
(1596, 4654, 6), -- NPC Death Link
(1596, 4138, 6), -- NPC AE - Corpse Burst
-- Requiem Behemoth
(1597, 4290, 1), -- Race
(1597, 4084, 6), -- Resist Physical Attack
(1597, 4275, 3), -- Sacred Attack Weak Point
(1597, 4278, 1), -- Dark Attack
(1597, 4279, 1), -- Fire Attack Weak Point
(1597, 4671, 1), -- AV - Teleport
(1597, 4649, 6), -- Poison
(1597, 4592, 6), -- Decrease P.Def
-- Requiem Behemoth
(1598, 4290, 1), -- Race
(1598, 4084, 6), -- Resist Physical Attack
(1598, 4275, 3), -- Sacred Attack Weak Point
(1598, 4278, 1), -- Dark Attack
(1598, 4279, 1), -- Fire Attack Weak Point
(1598, 4671, 1), -- AV - Teleport
(1598, 4649, 6), -- Poison
(1598, 4032, 6), -- NPC Strike
-- Requiem Priest
(1599, 4295, 1), -- Race
(1599, 4789, 2), -- NPC High Level
(1599, 4672, 1), -- NPC Corpse Remove
(1599, 4257, 7), -- NPC Hydroblast - Magic
(1599, 4561, 7), -- NPC Fire Burn - Magic
(1599, 4654, 7), -- NPC Death Link
(1599, 4138, 7), -- NPC AE - Corpse Burst
-- Requiem Behemoth
(1600, 4290, 1), -- Race
(1600, 4084, 6), -- Resist Physical Attack
(1600, 4275, 3), -- Sacred Attack Weak Point
(1600, 4278, 1), -- Dark Attack
(1600, 4279, 1), -- Fire Attack Weak Point
(1600, 4671, 1), -- AV - Teleport
(1600, 4649, 7), -- Poison
(1600, 4592, 7), -- Decrease P.Def
-- Requiem Behemoth
(1601, 4290, 1), -- Race
(1601, 4084, 6), -- Resist Physical Attack
(1601, 4275, 3), -- Sacred Attack Weak Point
(1601, 4278, 1), -- Dark Attack
(1601, 4279, 1), -- Fire Attack Weak Point
(1601, 4671, 1), -- AV - Teleport
(1601, 4649, 7), -- Poison
(1601, 4579, 7), -- Bleed
-- Zaken's Pikeman
(1602, 4290, 1), -- Race
(1602, 4275, 3), -- Sacred Attack Weak Point
(1602, 4278, 1), -- Dark Attack
(1602, 4244, 4), -- NPC Wild Sweep
-- Zaken's Pikeman
(1603, 4290, 1), -- Race
(1603, 4275, 3), -- Sacred Attack Weak Point
(1603, 4278, 1), -- Dark Attack
(1603, 4244, 4), -- NPC Wild Sweep
-- Zaken's Elite Pikeman
(1604, 4290, 1), -- Race
(1604, 4305, 1), -- Strong Type
(1604, 4275, 3), -- Sacred Attack Weak Point
(1604, 4278, 1), -- Dark Attack
(1604, 4244, 4), -- NPC Wild Sweep
-- Zaken's Archer
(1605, 4290, 1), -- Race
(1605, 4275, 3), -- Sacred Attack Weak Point
(1605, 4278, 1), -- Dark Attack
(1605, 4040, 4), -- NPC Bow Attack
-- Zaken's Archer
(1606, 4290, 1), -- Race
(1606, 4275, 3), -- Sacred Attack Weak Point
(1606, 4278, 1), -- Dark Attack
(1606, 4040, 4), -- NPC Bow Attack
-- Zaken's Elite Archer
(1607, 4290, 1), -- Race
(1607, 4305, 1), -- Strong Type
(1607, 4275, 3), -- Sacred Attack Weak Point
(1607, 4278, 1), -- Dark Attack
(1607, 4040, 4), -- NPC Bow Attack
-- Zaken's Watchman
(1608, 4291, 1), -- Race
(1608, 4071, 4), -- Resist Archery
(1608, 4273, 2), -- Resist Dagger
(1608, 4274, 1), -- Blunt Attack Weak Point
(1608, 4151, 4), -- NPC Windstrike - Magic
(1608, 4160, 4), -- NPC Aura Burn - Magic
-- Zaken's Watchman
(1609, 4291, 1), -- Race
(1609, 4071, 4), -- Resist Archery
(1609, 4273, 2), -- Resist Dagger
(1609, 4274, 1), -- Blunt Attack Weak Point
(1609, 4151, 4), -- NPC Windstrike - Magic
(1609, 4160, 4), -- NPC Aura Burn - Magic
-- Zaken's High Grade Watchman
(1610, 4291, 1), -- Race
(1610, 4305, 1), -- Strong Type
(1610, 4071, 4), -- Resist Archery
(1610, 4273, 2), -- Resist Dagger
(1610, 4274, 1), -- Blunt Attack Weak Point
(1610, 4151, 4), -- NPC Windstrike - Magic
(1610, 4160, 4), -- NPC Aura Burn - Magic
-- Unpleasant Humming
(1611, 4290, 1), -- Race
(1611, 4275, 3), -- Sacred Attack Weak Point
(1611, 4278, 1), -- Dark Attack
(1611, 4285, 4), -- Resist Sleep
(1611, 4088, 4), -- Bleed
-- Unpleasant Humming
(1612, 4290, 1), -- Race
(1612, 4275, 3), -- Sacred Attack Weak Point
(1612, 4278, 1), -- Dark Attack
(1612, 4285, 4), -- Resist Sleep
(1612, 4088, 4), -- Bleed
-- Unpleasant Shout
(1613, 4290, 1), -- Race
(1613, 4305, 1), -- Strong Type
(1613, 4275, 3), -- Sacred Attack Weak Point
(1613, 4278, 1), -- Dark Attack
(1613, 4285, 4), -- Resist Sleep
(1613, 4088, 4), -- Bleed
-- Death Flyer
(1614, 4290, 1), -- Race
(1614, 4275, 3), -- Sacred Attack Weak Point
(1614, 4278, 1), -- Dark Attack
(1614, 4285, 4), -- Resist Sleep
(1614, 4287, 4), -- Resist Hold
(1614, 4074, 2), -- NPC Haste
-- Death Flyer
(1615, 4290, 1), -- Race
(1615, 4275, 3), -- Sacred Attack Weak Point
(1615, 4278, 1), -- Dark Attack
(1615, 4285, 4), -- Resist Sleep
(1615, 4287, 4), -- Resist Hold
(1615, 4074, 2), -- NPC Haste
-- Gigantic Flyer
(1616, 4290, 1), -- Race
(1616, 4305, 1), -- Strong Type
(1616, 4275, 3), -- Sacred Attack Weak Point
(1616, 4278, 1), -- Dark Attack
(1616, 4285, 4), -- Resist Sleep
(1616, 4287, 4), -- Resist Hold
(1616, 4074, 2), -- NPC Haste
-- Fiend Archer
(1617, 4298, 1), -- Race
(1617, 4278, 1), -- Dark Attack
(1617, 4333, 3), -- Resist Dark Attack
(1617, 4071, 3), -- Resist Archery
(1617, 4085, 1), -- Critical Power
(1617, 4086, 1), -- Critical Chance
(1617, 4389, 4), -- Resist Mental Derangement
(1617, 4141, 4), -- NPC Wind Fist
-- Fiend Archer
(1618, 4298, 1), -- Race
(1618, 4278, 1), -- Dark Attack
(1618, 4333, 3), -- Resist Dark Attack
(1618, 4071, 3), -- Resist Archery
(1618, 4085, 1), -- Critical Power
(1618, 4086, 1), -- Critical Chance
(1618, 4389, 4), -- Resist Mental Derangement
(1618, 4141, 4), -- NPC Wind Fist
-- Pit Archer
(1619, 4291, 1), -- Race
(1619, 4305, 1), -- Strong Type
(1619, 4278, 1), -- Dark Attack
(1619, 4333, 3), -- Resist Dark Attack
(1619, 4071, 3), -- Resist Archery
(1619, 4085, 1), -- Critical Power
(1619, 4086, 1), -- Critical Chance
(1619, 4389, 4), -- Resist Mental Derangement
(1619, 4141, 4), -- NPC Wind Fist
-- Musveren
(1620, 4291, 1), -- Race
(1620, 4071, 4), -- Resist Archery
(1620, 4273, 2), -- Resist Dagger
(1620, 4274, 1), -- Blunt Attack Weak Point
(1620, 4002, 4), -- NPC HP Drain
-- Musveren
(1621, 4291, 1), -- Race
(1621, 4071, 4), -- Resist Archery
(1621, 4273, 2), -- Resist Dagger
(1621, 4274, 1), -- Blunt Attack Weak Point
(1621, 4002, 4), -- NPC HP Drain
-- Greater Musveren
(1622, 4291, 1), -- Race
(1622, 4305, 1), -- Strong Type
(1622, 4071, 4), -- Resist Archery
(1622, 4273, 2), -- Resist Dagger
(1622, 4274, 1), -- Blunt Attack Weak Point
(1622, 4002, 4), -- NPC HP Drain
-- Zaken's Guard
(1623, 4290, 1), -- Race
(1623, 4275, 3), -- Sacred Attack Weak Point
(1623, 4278, 1), -- Dark Attack
(1623, 4071, 3), -- Resist Archery
(1623, 4273, 2), -- Resist Dagger
(1623, 4274, 1), -- Blunt Attack Weak Point
(1623, 4067, 5), -- NPC Mortal Blow
-- Zaken's Guard
(1624, 4290, 1), -- Race
(1624, 4275, 3), -- Sacred Attack Weak Point
(1624, 4278, 1), -- Dark Attack
(1624, 4071, 3), -- Resist Archery
(1624, 4273, 2), -- Resist Dagger
(1624, 4274, 1), -- Blunt Attack Weak Point
(1624, 4067, 5), -- NPC Mortal Blow
-- Zaken's Elite Guard
(1625, 4290, 1), -- Race
(1625, 4305, 1), -- Strong Type
(1625, 4275, 3), -- Sacred Attack Weak Point
(1625, 4278, 1), -- Dark Attack
(1625, 4071, 3), -- Resist Archery
(1625, 4273, 2), -- Resist Dagger
(1625, 4274, 1), -- Blunt Attack Weak Point
(1625, 4067, 5), -- NPC Mortal Blow
-- Kaim Vanul
(1626, 4298, 1), -- Race
(1626, 4278, 1), -- Dark Attack
(1626, 4333, 3), -- Resist Dark Attack
(1626, 4085, 1), -- Critical Power
(1626, 4086, 1), -- Critical Chance
(1626, 4047, 5), -- Hold
-- Kaim Vanul
(1627, 4298, 1), -- Race
(1627, 4278, 1), -- Dark Attack
(1627, 4333, 3), -- Resist Dark Attack
(1627, 4085, 1), -- Critical Power
(1627, 4086, 1), -- Critical Chance
(1627, 4047, 5), -- Hold
-- Kaim Vanul Ladd
(1628, 4298, 1), -- Race
(1628, 4305, 1), -- Strong Type
(1628, 4278, 1), -- Dark Attack
(1628, 4333, 3), -- Resist Dark Attack
(1628, 4085, 1), -- Critical Power
(1628, 4086, 1), -- Critical Chance
(1628, 4047, 5), -- Hold
-- Pirate Zombie Captain
(1629, 4290, 1), -- Race
(1629, 4275, 3), -- Sacred Attack Weak Point
(1629, 4278, 1), -- Dark Attack
(1629, 4084, 7), -- Resist Physical Attack
(1629, 4067, 5), -- NPC Mortal Blow
-- Pirate Zombie Captain
(1630, 4290, 1), -- Race
(1630, 4275, 3), -- Sacred Attack Weak Point
(1630, 4278, 1), -- Dark Attack
(1630, 4084, 7), -- Resist Physical Attack
(1630, 4067, 5), -- NPC Mortal Blow
-- Zombie Captain's Spiritual Body
(1631, 4290, 1), -- Race
(1631, 4305, 1), -- Strong Type
(1631, 4275, 3), -- Sacred Attack Weak Point
(1631, 4278, 1), -- Dark Attack
(1631, 4084, 7), -- Resist Physical Attack
(1631, 4067, 5), -- NPC Mortal Blow
-- Doll Blader
(1632, 4298, 1), -- Race
(1632, 4278, 1), -- Dark Attack
(1632, 4333, 3), -- Resist Dark Attack
(1632, 4085, 1), -- Critical Power
(1632, 4086, 1), -- Critical Chance
(1632, 4285, 4), -- Resist Sleep
(1632, 4287, 4), -- Resist Hold
(1632, 4074, 2), -- NPC Haste
-- Doll Blader
(1633, 4298, 1), -- Race
(1633, 4278, 1), -- Dark Attack
(1633, 4333, 3), -- Resist Dark Attack
(1633, 4085, 1), -- Critical Power
(1633, 4086, 1), -- Critical Chance
(1633, 4285, 4), -- Resist Sleep
(1633, 4287, 4), -- Resist Hold
(1633, 4074, 2), -- NPC Haste
-- Crimson Doll Blader
(1634, 4298, 1), -- Race
(1634, 4305, 1), -- Strong Type
(1634, 4278, 1), -- Dark Attack
(1634, 4333, 3), -- Resist Dark Attack
(1634, 4085, 1), -- Critical Power
(1634, 4086, 1), -- Critical Chance
(1634, 4285, 4), -- Resist Sleep
(1634, 4287, 4), -- Resist Hold
(1634, 4074, 2), -- NPC Haste
-- Vale Master
(1635, 4298, 1), -- Race
(1635, 4278, 1), -- Dark Attack
(1635, 4333, 3), -- Resist Dark Attack
(1635, 4085, 1), -- Critical Power
(1635, 4086, 1), -- Critical Chance
(1635, 4076, 3), -- Reduction in movement speed
(1635, 4046, 5), -- Sleep
(1635, 4087, 5), -- NPC Blaze
(1635, 4094, 5), -- NPC Cancel Magic
-- Vale Master
(1636, 4298, 1), -- Race
(1636, 4278, 1), -- Dark Attack
(1636, 4333, 3), -- Resist Dark Attack
(1636, 4085, 1), -- Critical Power
(1636, 4086, 1), -- Critical Chance
(1636, 4076, 3), -- Reduction in movement speed
(1636, 4046, 5), -- Sleep
(1636, 4087, 5), -- NPC Blaze
(1636, 4094, 5), -- NPC Cancel Magic
-- Crimson Vale Master
(1637, 4298, 1), -- Race
(1637, 4305, 1), -- Strong Type
(1637, 4278, 1), -- Dark Attack
(1637, 4333, 3), -- Resist Dark Attack
(1637, 4085, 1), -- Critical Power
(1637, 4086, 1), -- Critical Chance
(1637, 4076, 3), -- Reduction in movement speed
(1637, 4046, 5), -- Sleep
(1637, 4087, 5), -- NPC Blaze
(1637, 4094, 5), -- NPC Cancel Magic
-- Dread Wolf
(1638, 4293, 1), -- Race
(1638, 4311, 1), -- Feeble Type
(1638, 4032, 3), -- NPC Strike
-- Tasaba Lizardman
(1639, 4295, 1), -- Race
(1639, 4311, 1), -- Feeble Type
(1639, 4071, 4), -- Resist Archery
(1639, 4273, 2), -- Resist Dagger
(1639, 4274, 1), -- Blunt Attack Weak Point
(1639, 4124, 3), -- NPC Spear Attack
-- Tasaba Lizardman Shaman 
(1640, 4295, 1), -- Race
(1640, 4311, 1), -- Feeble Type
(1640, 4116, 4), -- Resist M. Atk.
(1640, 4153, 3), -- Decrease Speed
(1640, 4160, 3), -- NPC Aura Burn - Magic
-- Ogre
(1641, 4295, 1), -- Race
(1641, 4311, 1), -- Feeble Type
(1641, 4071, 4), -- Resist Archery
(1641, 4273, 2), -- Resist Dagger
(1641, 4274, 1), -- Blunt Attack Weak Point
(1641, 4599, 3), -- Decrease Speed
(1641, 4092, 1), -- NPC Puma Stun
(1641, 4032, 3), -- NPC Strike
-- Tasaba Lizardman Sniper
(1642, 4295, 1), -- Race
(1642, 4311, 1), -- Feeble Type
(1642, 4071, 3), -- Resist Archery
(1642, 4124, 3), -- NPC Spear Attack
-- Tasaba Lizardman Sniper
(1643, 4295, 1), -- Race
(1643, 4311, 1), -- Feeble Type
(1643, 4071, 3), -- Resist Archery
(1643, 4124, 3), -- NPC Spear Attack
(1643, 4244, 3), -- NPC Wild Sweep
(1643, 4091, 1), -- NPC Ogre Stun
-- Lienlik
(1644, 4292, 1), -- Race
(1644, 4311, 1), -- Feeble Type
(1644, 4565, 3), -- NPC Eruption
-- Lienlik Ladd
(1645, 4292, 1), -- Race
(1645, 4311, 1), -- Feeble Type
(1645, 4116, 4), -- Resist M. Atk.
(1645, 4157, 4), -- NPC Blaze - Magic
(1645, 4560, 4), -- NPC Fire Burn
(1645, 4076, 2), -- Reduction in movement speed
-- Grave Scarab
(1646, 4301, 1), -- Race
(1646, 4789, 3), -- NPC High Level
(1646, 4575, 2), -- NPC Clan Buff - Haste
-- Scavenger Scarab
(1647, 4301, 1), -- Race
(1647, 4789, 3), -- NPC High Level
(1647, 4001, 7), -- NPC Windstrike
(1647, 4002, 7), -- NPC HP Drain
(1647, 4035, 7), -- Poison
-- Grave Ant
(1648, 4301, 1), -- Race
(1648, 4789, 3), -- NPC High Level
(1648, 4067, 7), -- NPC Mortal Blow
-- Scavenger Ant
(1649, 4301, 1), -- Race
(1649, 4789, 3), -- NPC High Level
(1649, 4232, 7), -- NPC AE Strike
(1649, 4091, 1), -- NPC Ogre Stun
(1649, 4067, 7), -- NPC Mortal Blow
-- Shrine Knight
(1650, 4290, 1), -- Race
(1650, 4275, 3), -- Sacred Attack Weak Point
(1650, 4278, 1), -- Dark Attack
(1650, 4789, 4), -- NPC High Level
(1650, 4630, 8), -- NPC MR - Twister
(1650, 4160, 8), -- NPC Aura Burn - Magic
(1650, 4571, 8), -- NPC Blazing Circle
(1650, 4635, 3), -- NPC Buff - Acumen Berserk WildMagic
(1650, 4035, 8), -- Poison
-- Shrine Guard
(1651, 4290, 1), -- Race
(1651, 4275, 3), -- Sacred Attack Weak Point
(1651, 4278, 1), -- Dark Attack
(1651, 4789, 4), -- NPC High Level
(1651, 4033, 8), -- NPC Burn
-- Scarlet Stakato Noble
(1652, 4301, 1), -- Race
(1652, 4306, 1), -- Strong Type
(1652, 4084, 8), -- Resist Physical Attack
(1652, 4789, 6), -- NPC High Level
-- Assassin Beetle
(1653, 4301, 1), -- Race
(1653, 4306, 1), -- Strong Type
(1653, 4284, 5), -- Resist Bleeding
(1653, 4071, 5), -- Resist Archery
(1653, 4274, 1), -- Blunt Attack Weak Point
(1653, 4789, 6), -- NPC High Level
-- Necromancer of Destruction
(1654, 4298, 1), -- Race
(1654, 4306, 1), -- Strong Type
(1654, 4278, 1), -- Dark Attack
(1654, 4333, 3), -- Resist Dark Attack
(1654, 4084, 5), -- Resist Physical Attack
(1654, 4285, 2), -- Resist Sleep
(1654, 4789, 7), -- NPC High Level
-- Arimanes of Destruction
(1655, 4298, 1), -- Race
(1655, 4306, 1), -- Strong Type
(1655, 4278, 1), -- Dark Attack
(1655, 4333, 3), -- Resist Dark Attack
(1655, 4285, 2), -- Resist Sleep
(1655, 4071, 4), -- Resist Archery
(1655, 4273, 2), -- Resist Dagger
(1655, 4789, 7), -- NPC High Level
-- Ashuras of Destruction
(1656, 4298, 1), -- Race
(1656, 4307, 1), -- Strong Type
(1656, 4278, 1), -- Dark Attack
(1656, 4333, 3), -- Resist Dark Attack
(1656, 4285, 4), -- Resist Sleep
(1656, 4084, 8), -- Resist Physical Attack
(1656, 4789, 8), -- NPC High Level
-- Magma Drake
(1657, 4299, 1), -- Race
(1657, 4307, 1), -- Strong Type
(1657, 4084, 6), -- Resist Physical Attack
(1657, 4009, 2), -- Resist Fire
(1657, 4280, 2), -- Water Attack Weak Point
(1657, 4789, 9), -- NPC High Level
-- Punishment of Splendor
(1658, 4297, 1), -- Race
(1658, 4085, 1), -- Critical Power
(1658, 4086, 1), -- Critical Chance
(1658, 4641, 6), -- NPC Super Strike
(1658, 4671, 1), -- AV - Teleport
-- Otherworldly Invader Soldier
(1659, 4290, 1), -- Race
(1659, 4304, 1), -- Strong Type
(1659, 4275, 3), -- Sacred Attack Weak Point
(1659, 4278, 1), -- Dark Attack
(1659, 4084, 8), -- Resist Physical Attack
(1659, 4067, 3), -- NPC Mortal Blow
-- Otherworldly Invader Soldier
(1660, 4290, 1), -- Race
(1660, 4304, 1), -- Strong Type
(1660, 4275, 3), -- Sacred Attack Weak Point
(1660, 4278, 1), -- Dark Attack
(1660, 4084, 8), -- Resist Physical Attack
(1660, 4579, 3), -- Bleed
-- Otherworldly Invader Archer
(1661, 4290, 1), -- Race
(1661, 4304, 1), -- Strong Type
(1661, 4275, 3), -- Sacred Attack Weak Point
(1661, 4278, 1), -- Dark Attack
(1661, 4274, 1), -- Blunt Attack Weak Point
(1661, 4084, 6), -- Resist Physical Attack
-- Otherworldly Invader Elite Soldier
(1662, 4290, 1), -- Race
(1662, 4304, 1), -- Strong Type
(1662, 4275, 3), -- Sacred Attack Weak Point
(1662, 4278, 1), -- Dark Attack
(1662, 4116, 6), -- Resist M. Atk.
(1662, 4643, 3), -- Decrease Speed
-- Otherworldly Invader Shaman
(1663, 4290, 1), -- Race
(1663, 4304, 1), -- Strong Type
(1663, 4275, 3), -- Sacred Attack Weak Point
(1663, 4278, 1), -- Dark Attack
(1663, 4116, 8), -- Resist M. Atk.
(1663, 4157, 3), -- NPC Blaze - Magic
(1663, 4160, 3), -- NPC Aura Burn - Magic
(1663, 4675, 1), -- NPC Dispel Fighter Buff
-- Otherworldly Invader Priest
(1664, 4290, 1), -- Race
(1664, 4303, 1), -- Strong Type
(1664, 4275, 3), -- Sacred Attack Weak Point
(1664, 4278, 1), -- Dark Attack
(1664, 4116, 8), -- Resist M. Atk.
(1664, 4065, 3), -- NPC Heal
(1664, 4046, 3), -- Sleep
-- Otherworldly Invader Magus
(1665, 4290, 1), -- Race
(1665, 4304, 1), -- Strong Type
(1665, 4275, 3), -- Sacred Attack Weak Point
(1665, 4278, 1), -- Dark Attack
(1665, 4257, 3), -- NPC Hydroblast - Magic
(1665, 4160, 3), -- NPC Aura Burn - Magic
-- Otherworldly Invader Martyrs
(1666, 4290, 1), -- Race
(1666, 4304, 1), -- Strong Type
(1666, 4275, 3), -- Sacred Attack Weak Point
(1666, 4278, 1), -- Dark Attack
(1666, 4286, 1), -- Greater Resist Sleep
(1666, 4288, 1), -- Greater Resist Hold
(1666, 4614, 3), -- NPC Death Bomb
-- Otherworldly Invader Warrior
(1667, 4290, 1), -- Race
(1667, 4304, 1), -- Strong Type
(1667, 4275, 3), -- Sacred Attack Weak Point
(1667, 4278, 1), -- Dark Attack
-- Otherworldly Invader Soldier
(1668, 4290, 1), -- Race
(1668, 4304, 1), -- Strong Type
(1668, 4275, 3), -- Sacred Attack Weak Point
(1668, 4278, 1), -- Dark Attack
(1668, 4581, 3), -- Hold
-- Otherworldly Invader Discipline
(1669, 4290, 1), -- Race
(1669, 4304, 1), -- Strong Type
(1669, 4275, 3), -- Sacred Attack Weak Point
(1669, 4278, 1), -- Dark Attack
(1669, 4274, 1), -- Blunt Attack Weak Point
(1669, 4117, 3), -- Paralysis
-- Otherworldly Invader Berserker
(1670, 4290, 1), -- Race
(1670, 4304, 1), -- Strong Type
(1670, 4275, 3), -- Sacred Attack Weak Point
(1670, 4278, 1), -- Dark Attack
(1670, 4274, 1), -- Blunt Attack Weak Point
(1670, 4095, 1), -- Damage Shield
(1670, 4119, 2), -- Fall in accuracy
-- Otherworldly Invader Food
(1671, 4291, 1), -- Race
(1671, 4045, 1), -- Resist Full Magic Attack
-- Otherworldly Invader Elite Soldier
(1672, 4290, 1), -- Race
(1672, 4307, 1), -- Strong Type
(1672, 4275, 3), -- Sacred Attack Weak Point
(1672, 4278, 1), -- Dark Attack
(1672, 4274, 1), -- Blunt Attack Weak Point
(1672, 4032, 3), -- NPC Strike
-- Otherworldly Invader Elite Soldier
(1673, 4290, 1), -- Race
(1673, 4307, 1), -- Strong Type
(1673, 4275, 3), -- Sacred Attack Weak Point
(1673, 4278, 1), -- Dark Attack
(1673, 4274, 1), -- Blunt Attack Weak Point
(1673, 4582, 3), -- Poison
-- Otherworldly Invader Elite Soldier
(1674, 4290, 1), -- Race
(1674, 4381, 1), -- Magic Skill Block
(1674, 4275, 3), -- Sacred Attack Weak Point
(1674, 4278, 1), -- Dark Attack
(1674, 4274, 1), -- Blunt Attack Weak Point
(1674, 4573, 3), -- NPC Sonic Blaster
-- Otherworldly Invader Elite Soldier
(1675, 4290, 1), -- Race
(1675, 4311, 1), -- Feeble Type
(1675, 4275, 3), -- Sacred Attack Weak Point
(1675, 4278, 1), -- Dark Attack
(1675, 4274, 1), -- Blunt Attack Weak Point
(1675, 4572, 3), -- NPC Triple Sonic Slash
-- Otherworldly Invader Warrior
(1676, 4290, 1), -- Race
(1676, 4307, 1), -- Strong Type
(1676, 4275, 3), -- Sacred Attack Weak Point
(1676, 4278, 1), -- Dark Attack
(1676, 4274, 1), -- Blunt Attack Weak Point
(1676, 4560, 3), -- NPC Fire Burn
-- Otherworldly Invader Elite Soldier
(1677, 4290, 1), -- Race
(1677, 4307, 1), -- Strong Type
(1677, 4275, 3), -- Sacred Attack Weak Point
(1677, 4278, 1), -- Dark Attack
(1677, 4274, 1), -- Blunt Attack Weak Point
(1677, 4572, 3), -- NPC Triple Sonic Slash
-- Otherworldly Invader Elite Soldier
(1678, 4290, 1), -- Race
(1678, 4307, 1), -- Strong Type
(1678, 4275, 3), -- Sacred Attack Weak Point
(1678, 4278, 1), -- Dark Attack
(1678, 4274, 1), -- Blunt Attack Weak Point
(1678, 4572, 3), -- NPC Triple Sonic Slash
-- Otherworldly Invader Elite Soldier
(1679, 4290, 1), -- Race
(1679, 4307, 1), -- Strong Type
(1679, 4275, 3), -- Sacred Attack Weak Point
(1679, 4278, 1), -- Dark Attack
(1679, 4274, 1), -- Blunt Attack Weak Point
(1679, 4573, 3), -- NPC Sonic Blaster
-- Otherworldly Invader Elite Soldier
(1680, 4290, 1), -- Race
(1680, 4307, 1), -- Strong Type
(1680, 4275, 3), -- Sacred Attack Weak Point
(1680, 4278, 1), -- Dark Attack
(1680, 4274, 1), -- Blunt Attack Weak Point
(1680, 4573, 3), -- NPC Sonic Blaster
-- Otherworldly Invader Magus
(1681, 4290, 1), -- Race
(1681, 4307, 1), -- Strong Type
(1681, 4275, 3), -- Sacred Attack Weak Point
(1681, 4278, 1), -- Dark Attack
(1681, 4563, 3), -- NPC Solar Flare - Magic
(1681, 4561, 3), -- NPC Fire Burn - Magic
-- Dimension Invader Soldier
(1682, 4290, 1), -- Race
(1682, 4304, 1), -- Strong Type
(1682, 4275, 3), -- Sacred Attack Weak Point
(1682, 4278, 1), -- Dark Attack
(1682, 4084, 8), -- Resist Physical Attack
(1682, 4067, 4), -- NPC Mortal Blow
-- Dimension Invader Soldier
(1683, 4290, 1), -- Race
(1683, 4304, 1), -- Strong Type
(1683, 4275, 3), -- Sacred Attack Weak Point
(1683, 4278, 1), -- Dark Attack
(1683, 4084, 8), -- Resist Physical Attack
(1683, 4579, 4), -- Bleed
-- Dimension Invader Archer
(1684, 4290, 1), -- Race
(1684, 4304, 1), -- Strong Type
(1684, 4275, 3), -- Sacred Attack Weak Point
(1684, 4278, 1), -- Dark Attack
(1684, 4274, 1), -- Blunt Attack Weak Point
(1684, 4084, 6), -- Resist Physical Attack
-- Dimension Invader Elite Soldier
(1685, 4290, 1), -- Race
(1685, 4304, 1), -- Strong Type
(1685, 4275, 3), -- Sacred Attack Weak Point
(1685, 4278, 1), -- Dark Attack
(1685, 4116, 6), -- Resist M. Atk.
(1685, 4643, 4), -- Decrease Speed
-- Dimension Invader Shaman
(1686, 4290, 1), -- Race
(1686, 4304, 1), -- Strong Type
(1686, 4275, 3), -- Sacred Attack Weak Point
(1686, 4278, 1), -- Dark Attack
(1686, 4116, 8), -- Resist M. Atk.
(1686, 4157, 4), -- NPC Blaze - Magic
(1686, 4160, 4), -- NPC Aura Burn - Magic
(1686, 4675, 1), -- NPC Dispel Fighter Buff
-- Dimension Invader Priest
(1687, 4290, 1), -- Race
(1687, 4303, 1), -- Strong Type
(1687, 4275, 3), -- Sacred Attack Weak Point
(1687, 4278, 1), -- Dark Attack
(1687, 4116, 8), -- Resist M. Atk.
(1687, 4065, 4), -- NPC Heal
(1687, 4046, 4), -- Sleep
-- Dimension Invader Magus
(1688, 4290, 1), -- Race
(1688, 4304, 1), -- Strong Type
(1688, 4275, 3), -- Sacred Attack Weak Point
(1688, 4278, 1), -- Dark Attack
(1688, 4257, 4), -- NPC Hydroblast - Magic
(1688, 4160, 4), -- NPC Aura Burn - Magic
-- Dimension Invader Martyrs
(1689, 4290, 1), -- Race
(1689, 4304, 1), -- Strong Type
(1689, 4275, 3), -- Sacred Attack Weak Point
(1689, 4278, 1), -- Dark Attack
(1689, 4286, 1), -- Greater Resist Sleep
(1689, 4288, 1), -- Greater Resist Hold
(1689, 4614, 4), -- NPC Death Bomb
-- Dimension Invader Warrior
(1690, 4290, 1), -- Race
(1690, 4304, 1), -- Strong Type
(1690, 4275, 3), -- Sacred Attack Weak Point
(1690, 4278, 1), -- Dark Attack
-- Dimension Invader Soldier
(1691, 4290, 1), -- Race
(1691, 4304, 1), -- Strong Type
(1691, 4275, 3), -- Sacred Attack Weak Point
(1691, 4278, 1), -- Dark Attack
(1691, 4581, 4), -- Hold
-- Dimension Invader Discipline
(1692, 4290, 1), -- Race
(1692, 4304, 1), -- Strong Type
(1692, 4275, 3), -- Sacred Attack Weak Point
(1692, 4278, 1), -- Dark Attack
(1692, 4274, 1), -- Blunt Attack Weak Point
(1692, 4117, 4), -- Paralysis
-- Dimension Invader Berserker 
(1693, 4290, 1), -- Race
(1693, 4304, 1), -- Strong Type
(1693, 4275, 3), -- Sacred Attack Weak Point
(1693, 4278, 1), -- Dark Attack
(1693, 4274, 1), -- Blunt Attack Weak Point
(1693, 4095, 1), -- Damage Shield
(1693, 4119, 3), -- Fall in accuracy
-- Dimension Invader Food
(1694, 4291, 1), -- Race
(1694, 4045, 1), -- Resist Full Magic Attack
-- Dimension Invader Elite Soldier
(1695, 4290, 1), -- Race
(1695, 4307, 1), -- Strong Type
(1695, 4275, 3), -- Sacred Attack Weak Point
(1695, 4278, 1), -- Dark Attack
(1695, 4032, 4), -- NPC Strike
-- Dimension Invader Elite Soldier
(1696, 4290, 1), -- Race
(1696, 4307, 1), -- Strong Type
(1696, 4275, 3), -- Sacred Attack Weak Point
(1696, 4278, 1), -- Dark Attack
(1696, 4582, 4), -- Poison
-- Dimension Invader Elite Soldier
(1697, 4290, 1), -- Race
(1697, 4381, 1), -- Magic Skill Block
(1697, 4275, 3), -- Sacred Attack Weak Point
(1697, 4278, 1), -- Dark Attack
(1697, 4573, 4), -- NPC Sonic Blaster
-- Dimension Invader Elite Soldier
(1698, 4290, 1), -- Race
(1698, 4311, 1), -- Feeble Type
(1698, 4275, 3), -- Sacred Attack Weak Point
(1698, 4278, 1), -- Dark Attack
(1698, 4572, 4), -- NPC Triple Sonic Slash
-- Dimension Invader Warrior
(1699, 4290, 1), -- Race
(1699, 4307, 1), -- Strong Type
(1699, 4275, 3), -- Sacred Attack Weak Point
(1699, 4278, 1), -- Dark Attack
(1699, 4274, 1), -- Blunt Attack Weak Point
(1699, 4560, 4), -- NPC Fire Burn
-- Dimension Invader Elite Soldier
(1700, 4290, 1), -- Race
(1700, 4307, 1), -- Strong Type
(1700, 4275, 3), -- Sacred Attack Weak Point
(1700, 4278, 1), -- Dark Attack
(1700, 4572, 4), -- NPC Triple Sonic Slash
-- Dimension Invader Elite Soldier
(1701, 4290, 1), -- Race
(1701, 4307, 1), -- Strong Type
(1701, 4275, 3), -- Sacred Attack Weak Point
(1701, 4278, 1), -- Dark Attack
(1701, 4572, 4), -- NPC Triple Sonic Slash
-- Dimension Invader Elite Soldier
(1702, 4290, 1), -- Race
(1702, 4307, 1), -- Strong Type
(1702, 4275, 3), -- Sacred Attack Weak Point
(1702, 4278, 1), -- Dark Attack
(1702, 4573, 4), -- NPC Sonic Blaster
-- Dimension Invader Elite Soldier
(1703, 4290, 1), -- Race
(1703, 4307, 1), -- Strong Type
(1703, 4275, 3), -- Sacred Attack Weak Point
(1703, 4278, 1), -- Dark Attack
(1703, 4573, 4), -- NPC Sonic Blaster
-- Dimension Invader Magus
(1704, 4290, 1), -- Race
(1704, 4307, 1), -- Strong Type
(1704, 4275, 3), -- Sacred Attack Weak Point
(1704, 4278, 1), -- Dark Attack
(1704, 4563, 4), -- NPC Solar Flare - Magic
(1704, 4561, 4), -- NPC Fire Burn - Magic
-- Purgatory Invader Soldier
(1705, 4298, 1), -- Race
(1705, 4304, 1), -- Strong Type
(1705, 4275, 3), -- Sacred Attack Weak Point
(1705, 4278, 1), -- Dark Attack
(1705, 4084, 8), -- Resist Physical Attack
(1705, 4067, 5), -- NPC Mortal Blow
-- Purgatory Invader Soldier
(1706, 4298, 1), -- Race
(1706, 4304, 1), -- Strong Type
(1706, 4275, 3), -- Sacred Attack Weak Point
(1706, 4278, 1), -- Dark Attack
(1706, 4084, 8), -- Resist Physical Attack
(1706, 4579, 5), -- Bleed
-- Purgatory Invader Archer
(1707, 4290, 1), -- Race
(1707, 4304, 1), -- Strong Type
(1707, 4275, 3), -- Sacred Attack Weak Point
(1707, 4278, 1), -- Dark Attack
(1707, 4274, 1), -- Blunt Attack Weak Point
(1707, 4084, 6), -- Resist Physical Attack
-- Purgatory Invader Elite Soldier
(1708, 4298, 1), -- Race
(1708, 4304, 1), -- Strong Type
(1708, 4278, 1), -- Dark Attack
(1708, 4333, 3), -- Resist Dark Attack
(1708, 4285, 2), -- Resist Sleep
(1708, 4275, 1), -- Sacred Attack Weak Point
(1708, 4116, 6), -- Resist M. Atk.
(1708, 4643, 5), -- Decrease Speed
-- Purgatory Invader Shaman
(1709, 4298, 1), -- Race
(1709, 4304, 1), -- Strong Type
(1709, 4278, 1), -- Dark Attack
(1709, 4333, 3), -- Resist Dark Attack
(1709, 4285, 2), -- Resist Sleep
(1709, 4275, 1), -- Sacred Attack Weak Point
(1709, 4116, 8), -- Resist M. Atk.
(1709, 4157, 5), -- NPC Blaze - Magic
(1709, 4160, 5), -- NPC Aura Burn - Magic
(1709, 4675, 1), -- NPC Dispel Fighter Buff
-- Purgatory Invader Priest
(1710, 4298, 1), -- Race
(1710, 4303, 1), -- Strong Type
(1710, 4278, 1), -- Dark Attack
(1710, 4333, 3), -- Resist Dark Attack
(1710, 4285, 2), -- Resist Sleep
(1710, 4275, 1), -- Sacred Attack Weak Point
(1710, 4116, 8), -- Resist M. Atk.
(1710, 4065, 5), -- NPC Heal
(1710, 4046, 5), -- Sleep
-- Purgatory Invader Magus
(1711, 4298, 1), -- Race
(1711, 4304, 1), -- Strong Type
(1711, 4278, 1), -- Dark Attack
(1711, 4333, 3), -- Resist Dark Attack
(1711, 4285, 2), -- Resist Sleep
(1711, 4275, 1), -- Sacred Attack Weak Point
(1711, 4257, 5), -- NPC Hydroblast - Magic
(1711, 4160, 5), -- NPC Aura Burn - Magic
-- Purgatory Invader Martyrs
(1712, 4298, 1), -- Race
(1712, 4304, 1), -- Strong Type
(1712, 4278, 1), -- Dark Attack
(1712, 4333, 3), -- Resist Dark Attack
(1712, 4285, 2), -- Resist Sleep
(1712, 4275, 1), -- Sacred Attack Weak Point
(1712, 4614, 5), -- NPC Death Bomb
-- Purgatory Invader Warrior
(1713, 4290, 1), -- Race
(1713, 4304, 1), -- Strong Type
(1713, 4275, 3), -- Sacred Attack Weak Point
(1713, 4278, 1), -- Dark Attack
(1713, 4274, 1), -- Blunt Attack Weak Point
-- Purgatory Invader Soldier
(1714, 4298, 1), -- Race
(1714, 4304, 1), -- Strong Type
(1714, 4278, 1), -- Dark Attack
(1714, 4333, 3), -- Resist Dark Attack
(1714, 4285, 2), -- Resist Sleep
(1714, 4275, 1), -- Sacred Attack Weak Point
(1714, 4581, 5), -- Hold
-- Purgatory Invader Disciples
(1715, 4290, 1), -- Race
(1715, 4304, 1), -- Strong Type
(1715, 4275, 3), -- Sacred Attack Weak Point
(1715, 4278, 1), -- Dark Attack
(1715, 4274, 1), -- Blunt Attack Weak Point
(1715, 4117, 5), -- Paralysis
-- Purgatory Invader Berserker
(1716, 4290, 1), -- Race
(1716, 4304, 1), -- Strong Type
(1716, 4275, 3), -- Sacred Attack Weak Point
(1716, 4278, 1), -- Dark Attack
(1716, 4274, 1), -- Blunt Attack Weak Point
(1716, 4095, 1), -- Damage Shield
(1716, 4119, 3), -- Fall in accuracy
-- Purgatory Invader Food
(1717, 4291, 1), -- Race
(1717, 4045, 1), -- Resist Full Magic Attack
-- Purgatory Invader Elite Soldier
(1718, 4298, 1), -- Race
(1718, 4307, 1), -- Strong Type
(1718, 4278, 1), -- Dark Attack
(1718, 4333, 3), -- Resist Dark Attack
(1718, 4285, 2), -- Resist Sleep
(1718, 4275, 1), -- Sacred Attack Weak Point
(1718, 4032, 5), -- NPC Strike
-- Purgatory Invader Elite Soldier
(1719, 4298, 1), -- Race
(1719, 4307, 1), -- Strong Type
(1719, 4278, 1), -- Dark Attack
(1719, 4333, 3), -- Resist Dark Attack
(1719, 4285, 2), -- Resist Sleep
(1719, 4275, 1), -- Sacred Attack Weak Point
(1719, 4582, 5), -- Poison
-- Purgatory Invader Elite Soldier
(1720, 4298, 1), -- Race
(1720, 4381, 1), -- Magic Skill Block
(1720, 4278, 1), -- Dark Attack
(1720, 4333, 3), -- Resist Dark Attack
(1720, 4285, 2), -- Resist Sleep
(1720, 4275, 1), -- Sacred Attack Weak Point
(1720, 4573, 5), -- NPC Sonic Blaster
-- Purgatory Invader Elite Soldier
(1721, 4298, 1), -- Race
(1721, 4311, 1), -- Feeble Type
(1721, 4278, 1), -- Dark Attack
(1721, 4333, 3), -- Resist Dark Attack
(1721, 4285, 2), -- Resist Sleep
(1721, 4275, 1), -- Sacred Attack Weak Point
(1721, 4572, 5), -- NPC Triple Sonic Slash
-- Purgatory Invader Warrior
(1722, 4290, 1), -- Race
(1722, 4307, 1), -- Strong Type
(1722, 4275, 3), -- Sacred Attack Weak Point
(1722, 4278, 1), -- Dark Attack
(1722, 4274, 1), -- Blunt Attack Weak Point
(1722, 4560, 5), -- NPC Fire Burn
-- Purgatory Invader Elite Soldier
(1723, 4298, 1), -- Race
(1723, 4307, 1), -- Strong Type
(1723, 4278, 1), -- Dark Attack
(1723, 4333, 3), -- Resist Dark Attack
(1723, 4285, 2), -- Resist Sleep
(1723, 4275, 1), -- Sacred Attack Weak Point
(1723, 4572, 5), -- NPC Triple Sonic Slash
-- Purgatory Invader Elite Soldier
(1724, 4298, 1), -- Race
(1724, 4307, 1), -- Strong Type
(1724, 4278, 1), -- Dark Attack
(1724, 4333, 3), -- Resist Dark Attack
(1724, 4285, 2), -- Resist Sleep
(1724, 4275, 1), -- Sacred Attack Weak Point
(1724, 4572, 5), -- NPC Triple Sonic Slash
-- Purgatory Invader Elite Soldier
(1725, 4298, 1), -- Race
(1725, 4307, 1), -- Strong Type
(1725, 4278, 1), -- Dark Attack
(1725, 4333, 3), -- Resist Dark Attack
(1725, 4285, 2), -- Resist Sleep
(1725, 4275, 1), -- Sacred Attack Weak Point
(1725, 4573, 5), -- NPC Sonic Blaster
-- Purgatory Invader Elite Soldier
(1726, 4298, 1), -- Race
(1726, 4307, 1), -- Strong Type
(1726, 4278, 1), -- Dark Attack
(1726, 4333, 3), -- Resist Dark Attack
(1726, 4285, 2), -- Resist Sleep
(1726, 4275, 1), -- Sacred Attack Weak Point
(1726, 4573, 5), -- NPC Sonic Blaster
-- Purgatory Invader Magus
(1727, 4298, 1), -- Race
(1727, 4307, 1), -- Strong Type
(1727, 4278, 1), -- Dark Attack
(1727, 4333, 3), -- Resist Dark Attack
(1727, 4285, 2), -- Resist Sleep
(1727, 4275, 1), -- Sacred Attack Weak Point
(1727, 4563, 5), -- NPC Solar Flare - Magic
(1727, 4561, 5), -- NPC Fire Burn - Magic
-- Forbidden Path Invader Elite Soldier
(1728, 4298, 1), -- Race
(1728, 4304, 1), -- Strong Type
(1728, 4275, 3), -- Sacred Attack Weak Point
(1728, 4278, 1), -- Dark Attack
(1728, 4084, 8), -- Resist Physical Attack
(1728, 4067, 6), -- NPC Mortal Blow
-- Forbidden Path Invader Elite Soldier
(1729, 4298, 1), -- Race
(1729, 4304, 1), -- Strong Type
(1729, 4275, 3), -- Sacred Attack Weak Point
(1729, 4278, 1), -- Dark Attack
(1729, 4084, 8), -- Resist Physical Attack
(1729, 4579, 6), -- Bleed
-- Forbidden Path Invader Archer
(1730, 4290, 1), -- Race
(1730, 4304, 1), -- Strong Type
(1730, 4275, 3), -- Sacred Attack Weak Point
(1730, 4278, 1), -- Dark Attack
(1730, 4274, 1), -- Blunt Attack Weak Point
(1730, 4084, 6), -- Resist Physical Attack
-- Forbidden Path Invader Elite Soldier
(1731, 4298, 1), -- Race
(1731, 4304, 1), -- Strong Type
(1731, 4278, 1), -- Dark Attack
(1731, 4333, 3), -- Resist Dark Attack
(1731, 4285, 2), -- Resist Sleep
(1731, 4275, 1), -- Sacred Attack Weak Point
(1731, 4116, 6), -- Resist M. Atk.
(1731, 4643, 6), -- Decrease Speed
-- Forbidden Path Invader Shaman
(1732, 4298, 1), -- Race
(1732, 4304, 1), -- Strong Type
(1732, 4278, 1), -- Dark Attack
(1732, 4333, 3), -- Resist Dark Attack
(1732, 4285, 2), -- Resist Sleep
(1732, 4275, 1), -- Sacred Attack Weak Point
(1732, 4116, 8), -- Resist M. Atk.
(1732, 4157, 6), -- NPC Blaze - Magic
(1732, 4160, 6), -- NPC Aura Burn - Magic
(1732, 4675, 1), -- NPC Dispel Fighter Buff
-- Forbidden Path Invader Priest
(1733, 4298, 1), -- Race
(1733, 4303, 1), -- Strong Type
(1733, 4278, 1), -- Dark Attack
(1733, 4333, 3), -- Resist Dark Attack
(1733, 4285, 2), -- Resist Sleep
(1733, 4275, 1), -- Sacred Attack Weak Point
(1733, 4116, 8), -- Resist M. Atk.
(1733, 4065, 6), -- NPC Heal
(1733, 4046, 6), -- Sleep
-- Forbidden Path Invader Magus
(1734, 4298, 1), -- Race
(1734, 4304, 1), -- Strong Type
(1734, 4278, 1), -- Dark Attack
(1734, 4333, 3), -- Resist Dark Attack
(1734, 4285, 2), -- Resist Sleep
(1734, 4275, 1), -- Sacred Attack Weak Point
(1734, 4257, 6), -- NPC Hydroblast - Magic
(1734, 4160, 6), -- NPC Aura Burn - Magic
-- Forbidden Path Invader Martyrs
(1735, 4298, 1), -- Race
(1735, 4304, 1), -- Strong Type
(1735, 4278, 1), -- Dark Attack
(1735, 4333, 3), -- Resist Dark Attack
(1735, 4285, 2), -- Resist Sleep
(1735, 4275, 1), -- Sacred Attack Weak Point
(1735, 4614, 6), -- NPC Death Bomb
-- Forbidden Path Invader Warrior
(1736, 4290, 1), -- Race
(1736, 4304, 1), -- Strong Type
(1736, 4275, 3), -- Sacred Attack Weak Point
(1736, 4278, 1), -- Dark Attack
(1736, 4274, 1), -- Blunt Attack Weak Point
-- Forbidden Path Invader Soldier
(1737, 4298, 1), -- Race
(1737, 4304, 1), -- Strong Type
(1737, 4278, 1), -- Dark Attack
(1737, 4333, 3), -- Resist Dark Attack
(1737, 4285, 2), -- Resist Sleep
(1737, 4275, 1), -- Sacred Attack Weak Point
(1737, 4581, 6), -- Hold
-- Forbidden Path Invader Disciple
(1738, 4290, 1), -- Race
(1738, 4304, 1), -- Strong Type
(1738, 4275, 3), -- Sacred Attack Weak Point
(1738, 4278, 1), -- Dark Attack
(1738, 4274, 1), -- Blunt Attack Weak Point
(1738, 4117, 6), -- Paralysis
-- Forbidden Path Invader Berserker
(1739, 4290, 1), -- Race
(1739, 4304, 1), -- Strong Type
(1739, 4275, 3), -- Sacred Attack Weak Point
(1739, 4278, 1), -- Dark Attack
(1739, 4274, 1), -- Blunt Attack Weak Point
(1739, 4095, 1), -- Damage Shield
(1739, 4119, 3), -- Fall in accuracy
-- Forbidden Path Invader Food
(1740, 4291, 1), -- Race
(1740, 4045, 1), -- Resist Full Magic Attack
-- Forbidden Path Invader Elite Soldier
(1741, 4298, 1), -- Race
(1741, 4307, 1), -- Strong Type
(1741, 4278, 1), -- Dark Attack
(1741, 4333, 3), -- Resist Dark Attack
(1741, 4285, 2), -- Resist Sleep
(1741, 4275, 1), -- Sacred Attack Weak Point
(1741, 4032, 6), -- NPC Strike
-- Forbidden Path Invader Elite Soldier
(1742, 4298, 1), -- Race
(1742, 4307, 1), -- Strong Type
(1742, 4278, 1), -- Dark Attack
(1742, 4333, 3), -- Resist Dark Attack
(1742, 4285, 2), -- Resist Sleep
(1742, 4275, 1), -- Sacred Attack Weak Point
(1742, 4582, 6), -- Poison
-- Forbidden Path Invader Elite Soldier
(1743, 4298, 1), -- Race
(1743, 4381, 1), -- Magic Skill Block
(1743, 4278, 1), -- Dark Attack
(1743, 4333, 3), -- Resist Dark Attack
(1743, 4285, 2), -- Resist Sleep
(1743, 4275, 1), -- Sacred Attack Weak Point
(1743, 4573, 6), -- NPC Sonic Blaster
-- Forbidden Path Invader Elite Soldier
(1744, 4298, 1), -- Race
(1744, 4311, 1), -- Feeble Type
(1744, 4278, 1), -- Dark Attack
(1744, 4333, 3), -- Resist Dark Attack
(1744, 4285, 2), -- Resist Sleep
(1744, 4275, 1), -- Sacred Attack Weak Point
(1744, 4572, 6), -- NPC Triple Sonic Slash
-- Forbidden Path Invader Warrior
(1745, 4290, 1), -- Race
(1745, 4307, 1), -- Strong Type
(1745, 4275, 3), -- Sacred Attack Weak Point
(1745, 4278, 1), -- Dark Attack
(1745, 4274, 1), -- Blunt Attack Weak Point
(1745, 4560, 6), -- NPC Fire Burn
-- Forbidden Path Invader Elite Soldier
(1746, 4298, 1), -- Race
(1746, 4307, 1), -- Strong Type
(1746, 4278, 1), -- Dark Attack
(1746, 4333, 3), -- Resist Dark Attack
(1746, 4285, 2), -- Resist Sleep
(1746, 4275, 1), -- Sacred Attack Weak Point
(1746, 4572, 6), -- NPC Triple Sonic Slash
-- Forbidden Path Invader Elite Soldier
(1747, 4298, 1), -- Race
(1747, 4307, 1), -- Strong Type
(1747, 4278, 1), -- Dark Attack
(1747, 4333, 3), -- Resist Dark Attack
(1747, 4285, 2), -- Resist Sleep
(1747, 4275, 1), -- Sacred Attack Weak Point
(1747, 4572, 6), -- NPC Triple Sonic Slash
-- Forbidden Path Invader Elite Soldier
(1748, 4298, 1), -- Race
(1748, 4307, 1), -- Strong Type
(1748, 4278, 1), -- Dark Attack
(1748, 4333, 3), -- Resist Dark Attack
(1748, 4285, 2), -- Resist Sleep
(1748, 4275, 1), -- Sacred Attack Weak Point
(1748, 4573, 6), -- NPC Sonic Blaster
-- Forbidden Path Invader Elite Soldier
(1749, 4298, 1), -- Race
(1749, 4307, 1), -- Strong Type
(1749, 4278, 1), -- Dark Attack
(1749, 4333, 3), -- Resist Dark Attack
(1749, 4285, 2), -- Resist Sleep
(1749, 4275, 1), -- Sacred Attack Weak Point
(1749, 4573, 6), -- NPC Sonic Blaster
-- Forbidden Path Invader Magus
(1750, 4298, 1), -- Race
(1750, 4307, 1), -- Strong Type
(1750, 4278, 1), -- Dark Attack
(1750, 4333, 3), -- Resist Dark Attack
(1750, 4285, 2), -- Resist Sleep
(1750, 4275, 1), -- Sacred Attack Weak Point
(1750, 4563, 6), -- NPC Solar Flare - Magic
(1750, 4561, 6), -- NPC Fire Burn - Magic
-- Dark Omen Invader Soldier
(1751, 4290, 1), -- Race
(1751, 4304, 1), -- Strong Type
(1751, 4275, 3), -- Sacred Attack Weak Point
(1751, 4278, 1), -- Dark Attack
(1751, 4084, 8), -- Resist Physical Attack
(1751, 4067, 7), -- NPC Mortal Blow
-- Dark Omen Invader Soldier
(1752, 4290, 1), -- Race
(1752, 4304, 1), -- Strong Type
(1752, 4275, 3), -- Sacred Attack Weak Point
(1752, 4278, 1), -- Dark Attack
(1752, 4084, 8), -- Resist Physical Attack
(1752, 4789, 4), -- NPC High Level
(1752, 4579, 8), -- Bleed
-- Dark Omen Invader Archer
(1753, 4290, 1), -- Race
(1753, 4304, 1), -- Strong Type
(1753, 4275, 3), -- Sacred Attack Weak Point
(1753, 4278, 1), -- Dark Attack
(1753, 4274, 1), -- Blunt Attack Weak Point
(1753, 4084, 6), -- Resist Physical Attack
(1753, 4789, 3), -- NPC High Level
-- Dark Omen Invader Elite Soldier
(1754, 4290, 1), -- Race
(1754, 4304, 1), -- Strong Type
(1754, 4275, 3), -- Sacred Attack Weak Point
(1754, 4278, 1), -- Dark Attack
(1754, 4116, 6), -- Resist M. Atk.
(1754, 4789, 4), -- NPC High Level
(1754, 4643, 8), -- Decrease Speed
-- Dark Omen Invader Shaman
(1755, 4290, 1), -- Race
(1755, 4304, 1), -- Strong Type
(1755, 4275, 3), -- Sacred Attack Weak Point
(1755, 4278, 1), -- Dark Attack
(1755, 4116, 8), -- Resist M. Atk.
(1755, 4789, 3), -- NPC High Level
(1755, 4157, 7), -- NPC Blaze - Magic
(1755, 4160, 7), -- NPC Aura Burn - Magic
(1755, 4675, 1), -- NPC Dispel Fighter Buff
-- Dark Omen Invader Priest
(1756, 4290, 1), -- Race
(1756, 4303, 1), -- Strong Type
(1756, 4275, 3), -- Sacred Attack Weak Point
(1756, 4278, 1), -- Dark Attack
(1756, 4116, 8), -- Resist M. Atk.
(1756, 4065, 7), -- NPC Heal
(1756, 4046, 7), -- Sleep
-- Dark Omen Invader Magus
(1757, 4290, 1), -- Race
(1757, 4304, 1), -- Strong Type
(1757, 4275, 3), -- Sacred Attack Weak Point
(1757, 4278, 1), -- Dark Attack
(1757, 4789, 4), -- NPC High Level
(1757, 4257, 8), -- NPC Hydroblast - Magic
(1757, 4160, 8), -- NPC Aura Burn - Magic
-- Dark Omen Invader Martyrs
(1758, 4290, 1), -- Race
(1758, 4304, 1), -- Strong Type
(1758, 4275, 3), -- Sacred Attack Weak Point
(1758, 4278, 1), -- Dark Attack
(1758, 4286, 1), -- Greater Resist Sleep
(1758, 4288, 1), -- Greater Resist Hold
(1758, 4614, 7), -- NPC Death Bomb
-- Dark Omen Invader Warrior
(1759, 4290, 1), -- Race
(1759, 4304, 1), -- Strong Type
(1759, 4275, 3), -- Sacred Attack Weak Point
(1759, 4278, 1), -- Dark Attack
(1759, 4274, 1), -- Blunt Attack Weak Point
(1759, 4789, 3), -- NPC High Level
-- Dark Omen Invader Soldier
(1760, 4290, 1), -- Race
(1760, 4304, 1), -- Strong Type
(1760, 4275, 3), -- Sacred Attack Weak Point
(1760, 4278, 1), -- Dark Attack
(1760, 4581, 7), -- Hold
-- Dark Omen Invader Disciple
(1761, 4290, 1), -- Race
(1761, 4304, 1), -- Strong Type
(1761, 4275, 3), -- Sacred Attack Weak Point
(1761, 4278, 1), -- Dark Attack
(1761, 4274, 1), -- Blunt Attack Weak Point
(1761, 4789, 3), -- NPC High Level
(1761, 4117, 7), -- Paralysis
-- Dark Omen Invader Berserker
(1762, 4290, 1), -- Race
(1762, 4304, 1), -- Strong Type
(1762, 4275, 3), -- Sacred Attack Weak Point
(1762, 4278, 1), -- Dark Attack
(1762, 4274, 1), -- Blunt Attack Weak Point
(1762, 4095, 1), -- Damage Shield
(1762, 4789, 4), -- NPC High Level
(1762, 4119, 3), -- Fall in accuracy
-- Dark Omen Invader Food
(1763, 4291, 1), -- Race
(1763, 4045, 1), -- Resist Full Magic Attack
-- Dark Omen Invader Elite Soldier
(1764, 4290, 1), -- Race
(1764, 4307, 1), -- Strong Type
(1764, 4275, 3), -- Sacred Attack Weak Point
(1764, 4278, 1), -- Dark Attack
(1764, 4789, 4), -- NPC High Level
(1764, 4032, 8), -- NPC Strike
-- Dark Omen Invader Elite Soldier
(1765, 4290, 1), -- Race
(1765, 4307, 1), -- Strong Type
(1765, 4275, 3), -- Sacred Attack Weak Point
(1765, 4278, 1), -- Dark Attack
(1765, 4789, 4), -- NPC High Level
(1765, 4582, 8), -- Poison
-- Dark Omen Invader Elite Soldier
(1766, 4290, 1), -- Race
(1766, 4381, 1), -- Magic Skill Block
(1766, 4275, 3), -- Sacred Attack Weak Point
(1766, 4278, 1), -- Dark Attack
(1766, 4789, 4), -- NPC High Level
(1766, 4573, 7), -- NPC Sonic Blaster
-- Dark Omen Invader Elite Soldier
(1767, 4290, 1), -- Race
(1767, 4311, 1), -- Feeble Type
(1767, 4275, 3), -- Sacred Attack Weak Point
(1767, 4278, 1), -- Dark Attack
(1767, 4572, 7), -- NPC Triple Sonic Slash
-- Dark Omen Invader Warrior
(1768, 4290, 1), -- Race
(1768, 4307, 1), -- Strong Type
(1768, 4275, 3), -- Sacred Attack Weak Point
(1768, 4278, 1), -- Dark Attack
(1768, 4274, 1), -- Blunt Attack Weak Point
(1768, 4789, 3), -- NPC High Level
(1768, 4560, 7), -- NPC Fire Burn
-- Dark Omen Invader Elite Soldier
(1769, 4290, 1), -- Race
(1769, 4307, 1), -- Strong Type
(1769, 4275, 3), -- Sacred Attack Weak Point
(1769, 4278, 1), -- Dark Attack
(1769, 4572, 7), -- NPC Triple Sonic Slash
-- Dark Omen Invader Elite Soldier
(1770, 4290, 1), -- Race
(1770, 4307, 1), -- Strong Type
(1770, 4275, 3), -- Sacred Attack Weak Point
(1770, 4278, 1), -- Dark Attack
(1770, 4789, 3), -- NPC High Level
(1770, 4572, 7), -- NPC Triple Sonic Slash
-- Dark Omen Invader Elite Soldier
(1771, 4290, 1), -- Race
(1771, 4307, 1), -- Strong Type
(1771, 4275, 3), -- Sacred Attack Weak Point
(1771, 4278, 1), -- Dark Attack
(1771, 4573, 7), -- NPC Sonic Blaster
-- Dark Omen Invader Elite Soldier
(1772, 4290, 1), -- Race
(1772, 4307, 1), -- Strong Type
(1772, 4275, 3), -- Sacred Attack Weak Point
(1772, 4278, 1), -- Dark Attack
(1772, 4789, 4), -- NPC High Level
(1772, 4573, 8), -- NPC Sonic Blaster
-- Dark Omen Invader Magus
(1773, 4290, 1), -- Race
(1773, 4307, 1), -- Strong Type
(1773, 4275, 3), -- Sacred Attack Weak Point
(1773, 4278, 1), -- Dark Attack
(1773, 4789, 3), -- NPC High Level
(1773, 4563, 7), -- NPC Solar Flare - Magic
(1773, 4561, 7), -- NPC Fire Burn - Magic
-- Messenger Invader Soldier
(1774, 4290, 1), -- Race
(1774, 4304, 1), -- Strong Type
(1774, 4275, 3), -- Sacred Attack Weak Point
(1774, 4278, 1), -- Dark Attack
(1774, 4084, 8), -- Resist Physical Attack
(1774, 4789, 6), -- NPC High Level
(1774, 4067, 9), -- NPC Mortal Blow
-- Messenger Invader Soldier
(1775, 4290, 1), -- Race
(1775, 4304, 1), -- Strong Type
(1775, 4275, 3), -- Sacred Attack Weak Point
(1775, 4278, 1), -- Dark Attack
(1775, 4084, 8), -- Resist Physical Attack
(1775, 4789, 9), -- NPC High Level
(1775, 4579, 10), -- Bleed
-- Messenger Invader Archer
(1776, 4290, 1), -- Race
(1776, 4304, 1), -- Strong Type
(1776, 4275, 3), -- Sacred Attack Weak Point
(1776, 4278, 1), -- Dark Attack
(1776, 4274, 1), -- Blunt Attack Weak Point
(1776, 4084, 6), -- Resist Physical Attack
(1776, 4789, 8), -- NPC High Level
-- Messenger Invader Elite Soldier
(1777, 4290, 1), -- Race
(1777, 4304, 1), -- Strong Type
(1777, 4275, 3), -- Sacred Attack Weak Point
(1777, 4278, 1), -- Dark Attack
(1777, 4116, 6), -- Resist M. Atk.
(1777, 4789, 9), -- NPC High Level
(1777, 4643, 10), -- Decrease Speed
-- Messenger Invader Shaman
(1778, 4290, 1), -- Race
(1778, 4304, 1), -- Strong Type
(1778, 4275, 3), -- Sacred Attack Weak Point
(1778, 4278, 1), -- Dark Attack
(1778, 4116, 8), -- Resist M. Atk.
(1778, 4789, 8), -- NPC High Level
(1778, 4157, 9), -- NPC Blaze - Magic
(1778, 4160, 9), -- NPC Aura Burn - Magic
(1778, 4675, 1), -- NPC Dispel Fighter Buff
-- Messenger Invader Priest
(1779, 4290, 1), -- Race
(1779, 4303, 1), -- Strong Type
(1779, 4275, 3), -- Sacred Attack Weak Point
(1779, 4278, 1), -- Dark Attack
(1779, 4116, 8), -- Resist M. Atk.
(1779, 4789, 6), -- NPC High Level
(1779, 4065, 9), -- NPC Heal
(1779, 4046, 9), -- Sleep
-- Messenger Invader Magus
(1780, 4290, 1), -- Race
(1780, 4304, 1), -- Strong Type
(1780, 4275, 3), -- Sacred Attack Weak Point
(1780, 4278, 1), -- Dark Attack
(1780, 4789, 9), -- NPC High Level
(1780, 4257, 10), -- NPC Hydroblast - Magic
(1780, 4160, 10), -- NPC Aura Burn - Magic
-- Messenger Invader Martyrs
(1781, 4290, 1), -- Race
(1781, 4304, 1), -- Strong Type
(1781, 4275, 3), -- Sacred Attack Weak Point
(1781, 4278, 1), -- Dark Attack
(1781, 4286, 1), -- Greater Resist Sleep
(1781, 4288, 1), -- Greater Resist Hold
(1781, 4789, 6), -- NPC High Level
(1781, 4614, 9), -- NPC Death Bomb
-- Messenger Invader Warrior
(1782, 4290, 1), -- Race
(1782, 4304, 1), -- Strong Type
(1782, 4275, 3), -- Sacred Attack Weak Point
(1782, 4278, 1), -- Dark Attack
(1782, 4274, 1), -- Blunt Attack Weak Point
(1782, 4789, 8), -- NPC High Level
-- Messenger Invader Soldier
(1783, 4290, 1), -- Race
(1783, 4304, 1), -- Strong Type
(1783, 4275, 3), -- Sacred Attack Weak Point
(1783, 4278, 1), -- Dark Attack
(1783, 4789, 6), -- NPC High Level
(1783, 4581, 9), -- Hold
-- Messenger Invader Disciple
(1784, 4290, 1), -- Race
(1784, 4304, 1), -- Strong Type
(1784, 4275, 3), -- Sacred Attack Weak Point
(1784, 4278, 1), -- Dark Attack
(1784, 4274, 1), -- Blunt Attack Weak Point
(1784, 4789, 8), -- NPC High Level
(1784, 4117, 9), -- Paralysis
-- Messenger Invader Berserker
(1785, 4290, 1), -- Race
(1785, 4304, 1), -- Strong Type
(1785, 4275, 3), -- Sacred Attack Weak Point
(1785, 4278, 1), -- Dark Attack
(1785, 4274, 1), -- Blunt Attack Weak Point
(1785, 4095, 1), -- Damage Shield
(1785, 4789, 9), -- NPC High Level
(1785, 4119, 3), -- Fall in accuracy
-- Messenger Invader Food
(1786, 4291, 1), -- Race
(1786, 4045, 1), -- Resist Full Magic Attack
(1786, 4789, 6), -- NPC High Level
-- Messenger Invader Elite Soldier
(1787, 4290, 1), -- Race
(1787, 4307, 1), -- Strong Type
(1787, 4275, 3), -- Sacred Attack Weak Point
(1787, 4278, 1), -- Dark Attack
(1787, 4789, 9), -- NPC High Level
(1787, 4032, 10), -- NPC Strike
-- Messenger Invader Elite Soldier
(1788, 4290, 1), -- Race
(1788, 4307, 1), -- Strong Type
(1788, 4275, 3), -- Sacred Attack Weak Point
(1788, 4278, 1), -- Dark Attack
(1788, 4789, 9), -- NPC High Level
(1788, 4582, 10), -- Poison
-- Messenger Invader Elite Soldier
(1789, 4290, 1), -- Race
(1789, 4381, 1), -- Magic Skill Block
(1789, 4275, 3), -- Sacred Attack Weak Point
(1789, 4278, 1), -- Dark Attack
(1789, 4789, 9), -- NPC High Level
(1789, 4573, 10), -- NPC Sonic Blaster
-- Messenger Invader Elite Soldier
(1790, 4290, 1), -- Race
(1790, 4311, 1), -- Feeble Type
(1790, 4275, 3), -- Sacred Attack Weak Point
(1790, 4278, 1), -- Dark Attack
(1790, 4789, 6), -- NPC High Level
(1790, 4572, 9), -- NPC Triple Sonic Slash
-- Messenger Invader Warrior
(1791, 4290, 1), -- Race
(1791, 4307, 1), -- Strong Type
(1791, 4275, 3), -- Sacred Attack Weak Point
(1791, 4278, 1), -- Dark Attack
(1791, 4274, 1), -- Blunt Attack Weak Point
(1791, 4789, 8), -- NPC High Level
(1791, 4560, 9), -- NPC Fire Burn
-- Messenger Invader Elite Soldier
(1792, 4290, 1), -- Race
(1792, 4307, 1), -- Strong Type
(1792, 4275, 3), -- Sacred Attack Weak Point
(1792, 4278, 1), -- Dark Attack
(1792, 4789, 6), -- NPC High Level
(1792, 4572, 9), -- NPC Triple Sonic Slash
-- Messenger Invader Elite Soldier
(1793, 4290, 1), -- Race
(1793, 4307, 1), -- Strong Type
(1793, 4275, 3), -- Sacred Attack Weak Point
(1793, 4278, 1), -- Dark Attack
(1793, 4789, 8), -- NPC High Level
(1793, 4572, 9), -- NPC Triple Sonic Slash
-- Messenger Invader Elite Soldier
(1794, 4290, 1), -- Race
(1794, 4307, 1), -- Strong Type
(1794, 4275, 3), -- Sacred Attack Weak Point
(1794, 4278, 1), -- Dark Attack
(1794, 4789, 6), -- NPC High Level
(1794, 4573, 9), -- NPC Sonic Blaster
-- Messenger Invader Elite Soldier
(1795, 4290, 1), -- Race
(1795, 4307, 1), -- Strong Type
(1795, 4275, 3), -- Sacred Attack Weak Point
(1795, 4278, 1), -- Dark Attack
(1795, 4789, 9), -- NPC High Level
(1795, 4573, 10), -- NPC Sonic Blaster
-- Messenger Invader Magus
(1796, 4290, 1), -- Race
(1796, 4307, 1), -- Strong Type
(1796, 4275, 3), -- Sacred Attack Weak Point
(1796, 4278, 1), -- Dark Attack
(1796, 4789, 8), -- NPC High Level
(1796, 4563, 9), -- NPC Solar Flare - Magic
(1796, 4561, 9), -- NPC Fire Burn - Magic
-- Spirit of Timiniel
(1797, 4302, 1), -- Race
(1797, 4311, 1), -- Feeble Type
(1797, 4011, 3), -- Resist Wind
(1797, 4282, 2), -- Earth Attack Weak Point
(1797, 4562, 5), -- NPC Solar Flare
-- Ghost of Gatekeeper
(1798, 4290, 1), -- Race
(1798, 4303, 1), -- Strong Type
(1798, 4275, 3), -- Sacred Attack Weak Point
(1798, 4278, 1), -- Dark Attack
(1798, 4274, 1), -- Blunt Attack Weak Point
(1798, 4789, 4), -- NPC High Level
(1798, 4073, 8), -- Shock
-- Ghost of Gatekeeper
(1799, 4290, 1), -- Race
(1799, 4304, 1), -- Strong Type
(1799, 4275, 3), -- Sacred Attack Weak Point
(1799, 4278, 1), -- Dark Attack
(1799, 4274, 1), -- Blunt Attack Weak Point
(1799, 4789, 4), -- NPC High Level
(1799, 4631, 3), -- NPC Buff - Acumen Shield WildMagic
(1799, 4571, 8), -- NPC Blazing Circle
(1799, 4032, 8), -- NPC Strike
-- Ghost of Vassal
(1800, 4290, 1), -- Race
(1800, 4303, 1), -- Strong Type
(1800, 4275, 3), -- Sacred Attack Weak Point
(1800, 4278, 1), -- Dark Attack
(1800, 4274, 1), -- Blunt Attack Weak Point
(1800, 4789, 5), -- NPC High Level
(1800, 4567, 8), -- NPC Eruption - Slow
-- Treasure Chest
(1801, 4291, 1), -- Race
(1801, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1802, 4291, 1), -- Race
(1802, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1803, 4291, 1), -- Race
(1803, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1804, 4291, 1), -- Race
(1804, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1805, 4291, 1), -- Race
(1805, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1806, 4291, 1), -- Race
(1806, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1807, 4291, 1), -- Race
(1807, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1808, 4291, 1), -- Race
(1808, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1809, 4291, 1), -- Race
(1809, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1810, 4291, 1), -- Race
(1810, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1811, 4291, 1), -- Race
(1811, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1812, 4291, 1), -- Race
(1812, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1813, 4291, 1), -- Race
(1813, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1814, 4291, 1), -- Race
(1814, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1815, 4291, 1), -- Race
(1815, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1816, 4291, 1), -- Race
(1816, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1817, 4291, 1), -- Race
(1817, 4045, 1), -- Resist Full Magic Attack
-- Treasure Chest
(1818, 4291, 1), -- Race
(1818, 4045, 1), -- Resist Full Magic Attack
(1818, 4789, 2), -- NPC High Level
-- Treasure Chest
(1819, 4291, 1), -- Race
(1819, 4045, 1), -- Resist Full Magic Attack
(1819, 4789, 4), -- NPC High Level
-- Treasure Chest
(1820, 4291, 1), -- Race
(1820, 4045, 1), -- Resist Full Magic Attack
(1820, 4789, 5), -- NPC High Level
-- Treasure Chest
(1821, 4291, 1), -- Race
(1821, 4045, 1), -- Resist Full Magic Attack
(1821, 4789, 7), -- NPC High Level
-- Treasure Chest
(1822, 4291, 1), -- Race
(1822, 4045, 1), -- Resist Full Magic Attack
(1822, 4789, 8), -- NPC High Level
-- Executor of Sacrificial Offerings
(1823, 4290, 1), -- Race
(1823, 4307, 1), -- Strong Type
(1823, 4275, 3), -- Sacred Attack Weak Point
(1823, 4278, 1), -- Dark Attack
(1823, 4285, 3), -- Resist Sleep
(1823, 4287, 3), -- Resist Hold
(1823, 4789, 6); -- NPC High Level

INSERT INTO npcskills VALUES
-- Orc Sniper
(5001, 4295, 1), -- Race
-- Orc Fighter
(5002, 4295, 1), -- Race
-- Spirit Of Mirrors
(5003, 4292, 1), -- Race
-- Spirit Of Mirrors
(5004, 4295, 1), -- Race
-- Spirit Of Mirrors
(5005, 4295, 1), -- Race
-- Bhato Bloodspear
(5006, 4295, 1), -- Race
-- Tanuki Skullcrusher
(5007, 4295, 1), -- Race
-- Kraacul Blackskull
(5008, 4295, 1), -- Race
-- Taarq Blackskull
(5009, 4295, 1), -- Race
-- Fallen Knight Saythus
(5010, 4292, 1), -- Race
-- Fallen Knight Trayer
(5011, 4292, 1), -- Race
-- Fallen Knight Falimar
(5012, 4292, 1), -- Race
-- Garuuk Blackskull
(5013, 4292, 1), -- Race
-- Madclaw Takuttaku
(5014, 4292, 1), -- Race
-- Rakanos Viperheart
(5015, 4292, 1), -- Race
-- Nerkas
(5016, 4298, 1), -- Race
(5016, 4278, 1), -- Dark Attack
(5016, 4333, 3), -- Resist Dark Attack
-- Plague Zombie
(5017, 4290, 1), -- Race
(5017, 4275, 3), -- Sacred Attack Weak Point
(5017, 4278, 1), -- Dark Attack
(5017, 4071, 3), -- Resist Archery
(5017, 4116, 8), -- Resist M. Atk.
(5017, 4284, 3), -- Resist Bleeding
(5017, 4248, 1), -- NPC HP Drain - Slow
-- Orc Warrior
(5018, 4295, 1), -- Race
-- Orc Warrior Leader
(5019, 4295, 1), -- Race
-- Varool Foulclaw
(5020, 4295, 1), -- Race
-- Kirunak
(5021, 4298, 1), -- Race
(5021, 4278, 1), -- Dark Attack
(5021, 4333, 3), -- Resist Dark Attack
-- Merkenis
(5022, 4298, 1), -- Race
(5022, 4278, 1), -- Dark Attack
(5022, 4333, 3), -- Resist Dark Attack
-- Out of Use
(5023, 4295, 1), -- Race
-- Undead Priest
(5024, 4290, 1), -- Race
(5024, 4275, 3), -- Sacred Attack Weak Point
(5024, 4278, 1), -- Dark Attack
(5024, 4071, 3), -- Resist Archery
(5024, 4116, 8), -- Resist M. Atk.
(5024, 4284, 3), -- Resist Bleeding
(5024, 4248, 1), -- NPC HP Drain - Slow
-- Ol Mahum cat's eye
(5025, 4295, 1), -- Race
-- Ol Mahum eagle eye
(5026, 4295, 1), -- Race
-- Ol Mahum fox eye
(5027, 4295, 1), -- Race
-- Ol Mahum wolf eye
(5028, 4295, 1), -- Race
-- phantom salamander
(5029, 4296, 1), -- Race
-- Water Seer
(5030, 4291, 1), -- Race
-- Ol Mahum Sentry
(5031, 4295, 1), -- Race
-- Lizardman Warrior
(5032, 4295, 1), -- Race
-- Lizardman Scout
(5033, 4295, 1), -- Race
-- Lizardman
(5034, 4295, 1), -- Race
-- Tamil
(5035, 4295, 1), -- Race
-- Calpico
(5036, 4295, 1), -- Race
-- Calpico's Goons
(5037, 4295, 1), -- Race
-- Cat's Eye Bandit
(5038, 4295, 1), -- Race
-- Out of Use
(5039, 4295, 1), -- Race
-- Out of Use
(5040, 4298, 1), -- Race
(5040, 4278, 1), -- Dark Attack
(5040, 4333, 3), -- Resist Dark Attack
-- Baranka's Messenger
(5041, 4295, 1), -- Race
-- Orc Escort
(5042, 4295, 1), -- Race
-- Varangka's Tracker
(5043, 4292, 1), -- Race
-- Kasha Bear Totem Spirit
(5044, 4293, 1), -- Race
(5044, 4278, 1), -- Dark Attack
(5044, 4333, 3), -- Resist Dark Attack
-- Kuruka Ratman Leader
(5045, 4295, 1), -- Race
-- Sumi
(5046, 4295, 1), -- Race
-- Wanuk
(5047, 4295, 1), -- Race
-- Chewba
(5048, 4295, 1), -- Race
-- Heitafu
(5049, 4295, 1), -- Race
-- Picubo
(5050, 4295, 1), -- Race
-- Bumbum
(5051, 4295, 1), -- Race
-- Minsku
(5052, 4295, 1), -- Race
-- Chuchu
(5053, 4295, 1), -- Race
-- Umbar Orc
(5054, 4295, 1), -- Race
-- Zakan
(5055, 4295, 1), -- Race
-- Durka Spirit
(5056, 4291, 1), -- Race
(5056, 4278, 1), -- Dark Attack
(5056, 4333, 3), -- Resist Dark Attack
-- Out of Use
(5057, 4293, 1), -- Race
-- Honey Bear
(5058, 4293, 1), -- Race
-- Uoph
(5059, 4295, 1), -- Race
-- Kracha
(5060, 4295, 1), -- Race
-- Batoh
(5061, 4295, 1), -- Race
-- Tanukia
(5062, 4295, 1), -- Race
-- Tanukia's Warhound
(5063, 4293, 1), -- Race
-- Turel
(5064, 4295, 1), -- Race
-- Roko
(5065, 4295, 1), -- Race
-- Roko's Warhound
(5066, 4293, 1), -- Race
-- Kamut
(5067, 4295, 1), -- Race
-- Murtika
(5068, 4295, 1), -- Race
-- Murtika's Warhound
(5069, 4293, 1), -- Race
-- Tumran Orc Brigand
(5070, 4295, 1), -- Race
-- Out of Use
(5071, 4295, 1), -- Race
-- Out of Use
(5072, 4293, 1), -- Race
-- Out of Use
(5073, 4291, 1), -- Race
-- Out of Use
(5074, 4291, 1), -- Race
-- Out of Use
(5075, 4295, 1), -- Race
(5075, 4275, 3), -- Sacred Attack Weak Point
(5075, 4278, 1), -- Dark Attack
-- Out of Use
(5076, 4292, 1), -- Race
-- Unicorn Of Eva
(5077, 4296, 1), -- Race
-- Trimden Lord 
(5078, 4301, 1), -- Race
-- Black Willow Lurker
(5079, 4294, 1), -- Race
(5079, 4275, 3), -- Sacred Attack Weak Point
(5079, 4278, 1), -- Dark Attack
-- Pashika Son Of Voltar
(5080, 4295, 1), -- Race
-- Vultus Son Of Voltar
(5081, 4295, 1), -- Race
-- Enku Orc Overlord
(5082, 4295, 1), -- Race
-- Makum Bugbear Thug
(5083, 4295, 1), -- Race
-- Out of Use
(5084, 4295, 1), -- Race
-- Out of Use
(5085, 4295, 1), -- Race
-- Revenant of Tantos Chief
(5086, 4295, 1), -- Race
(5086, 4275, 3), -- Sacred Attack Weak Point
(5086, 4278, 1), -- Dark Attack
-- Out of Use
(5087, 4295, 1), -- Race
-- Harpy Matriarch
(5088, 4292, 1), -- Race
-- Road Collector
(5089, 4295, 1), -- Race
-- Serpent Demon Kadesh
(5090, 4292, 1), -- Race
-- Out of Use
(5091, 4295, 1), -- Race
(5091, 4047, 3), -- Hold
-- Neer Bodyguard
(5092, 4290, 1), -- Race
(5092, 4275, 3), -- Sacred Attack Weak Point
(5092, 4278, 1), -- Dark Attack
-- Delu Chief Kalkis
(5093, 4295, 1), -- Race
-- Delu Lizardman Assassin
(5094, 4295, 1), -- Race
-- Singing Flower Phantasm
(5095, 4294, 1), -- Race
-- Singing Flower Nightmare
(5096, 4294, 1), -- Race
-- Singing Flower Darkling
(5097, 4294, 1), -- Race
-- Ghost Fire
(5098, 4291, 1), -- Race
-- Nameless Revenant
(5099, 4290, 1), -- Race
(5099, 4275, 3), -- Sacred Attack Weak Point
(5099, 4278, 1), -- Dark Attack
(5099, 4071, 3), -- Resist Archery
(5099, 4116, 8), -- Resist M. Atk.
(5099, 4284, 3), -- Resist Bleeding
(5099, 4248, 3), -- NPC HP Drain - Slow
-- Skeletal Mercenary
(5100, 4290, 1), -- Race
(5100, 4275, 3), -- Sacred Attack Weak Point
(5100, 4278, 1), -- Dark Attack
-- Drevanul Prince Zeruel
(5101, 4298, 1), -- Race
(5101, 4278, 1), -- Dark Attack
(5101, 4333, 3), -- Resist Dark Attack
-- Pako The Cat
(5102, 4293, 1), -- Race
-- Unicorn Racer
(5103, 4296, 1), -- Race
-- Shadow Turen
(5104, 4298, 1), -- Race
-- Mimi The Cat
(5105, 4293, 1), -- Race
-- Unicorn Phantasm
(5106, 4296, 1), -- Race
-- Silhouette Tilfo
(5107, 4298, 1), -- Race
(5107, 4278, 1), -- Dark Attack
(5107, 4333, 3), -- Resist Dark Attack
-- Stenoa Gorgon Queen
(5108, 4292, 1), -- Race
-- Handmaiden Of Stenoa
(5109, 4292, 1), -- Race
-- Shyslassys
(5110, 4292, 1), -- Race
-- Cave Basilisk
(5111, 4292, 1), -- Race
-- Gorr
(5112, 4292, 1), -- Race
-- Baraham
(5113, 4292, 1), -- Race
-- Succubus Queen
(5114, 4298, 1), -- Race
(5114, 4278, 1), -- Dark Attack
(5114, 4333, 3), -- Resist Dark Attack
-- Claw Of Succubus
(5115, 4298, 1), -- Race
(5115, 4278, 1), -- Dark Attack
(5115, 4333, 3), -- Resist Dark Attack
-- Lava Salamander
(5116, 4296, 1), -- Race
-- Nahir
(5117, 4298, 1), -- Race
(5117, 4278, 1), -- Dark Attack
(5117, 4333, 3), -- Resist Dark Attack
-- Black Willow
(5118, 4294, 1), -- Race
(5118, 4275, 3), -- Sacred Attack Weak Point
(5118, 4278, 1), -- Dark Attack
(5118, 4047, 3), -- Hold
-- Spirit of Sir Herod
(5119, 4290, 1), -- Race
(5119, 4275, 3), -- Sacred Attack Weak Point
(5119, 4278, 1), -- Dark Attack
(5119, 4037, 2), -- Weaken P. Atk.
-- Luell Of Zephyr Winds
(5120, 4296, 1), -- Race
(5120, 4037, 2), -- Weaken P. Atk.
-- Actea Of Verdant Wilds
(5121, 4296, 1), -- Race
-- Leto Lizardman Agent
(5122, 4295, 1), -- Race
-- Leto Lizardman Leader
(5123, 4295, 1), -- Race
-- Leto Lizardman Assassin
(5124, 4295, 1), -- Race
-- Leto Lizardman Sniper
(5125, 4295, 1), -- Race
-- Leto Lizardman Wizard
(5126, 4295, 1), -- Race
(5126, 4002, 3), -- NPC HP Drain
-- Leto Lizardman Lord
(5127, 4295, 1), -- Race
-- Aruraune
(5128, 4294, 1), -- Race
-- Ol Mahum Inspector
(5129, 4295, 1), -- Race
-- Ol Mahum Betrayer
(5130, 4295, 1), -- Race
-- Crimson Werewolf
(5131, 4295, 1), -- Race
-- Krudel Lizardman
(5132, 4295, 1), -- Race
-- Evil Eye Lord
(5133, 4291, 1), -- Race
-- Tatoma
(5134, 4295, 1), -- Race
-- Grima
(5135, 4301, 1), -- Race
-- Succubus Of Seduction
(5136, 4298, 1), -- Race
(5136, 4278, 1), -- Dark Attack
(5136, 4333, 3), -- Resist Dark Attack
-- Demon King
(5137, 4298, 1), -- Race
(5137, 4278, 1), -- Dark Attack
(5137, 4333, 3), -- Resist Dark Attack
-- Great Demon King
(5138, 4290, 1), -- Race
(5138, 4278, 1), -- Dark Attack
(5138, 4333, 3), -- Resist Dark Attack
-- Secret Keeper Tree
(5139, 4296, 1), -- Race
(5139, 4279, 2), -- Fire Attack Weak Point
(5139, 4277, 3), -- Resist Poison
-- Breka Overlord Haka
(5140, 4295, 1), -- Race
(5140, 4032, 3), -- NPC Strike
-- Breka Overlord Jaka
(5141, 4295, 1), -- Race
(5141, 4032, 3), -- NPC Strike
-- Breka Overlord Marka
(5142, 4295, 1), -- Race
(5142, 4032, 3), -- NPC Strike
-- Windsus Aleph
(5143, 4293, 1), -- Race
-- Tarlk Raider Athu
(5144, 4295, 1), -- Race
-- Tarlk Raider Lanka
(5145, 4295, 1), -- Race
-- Tarlk Raider Triska
(5146, 4295, 1), -- Race
-- Tarlk Raider Motura
(5147, 4295, 1), -- Race
-- Tarlk Raider Kalath
(5148, 4295, 1), -- Race
-- Gremlin Filcher
(5149, 4302, 1), -- Race
-- Black Legion Stormtrooper
(5150, 4290, 1), -- Race
(5150, 4275, 3), -- Sacred Attack Weak Point
(5150, 4278, 1), -- Dark Attack
-- Delu Lizardman Headhunter
(5151, 4295, 1), -- Race
-- Marsh Stakato Marquess
(5152, 4301, 1), -- Race
(5152, 4279, 2), -- Fire Attack Weak Point
-- Alexandro Sanches 
(5153, 4302, 1), -- Race
(5153, 4278, 1), -- Dark Attack
(5153, 4333, 3), -- Resist Dark Attack
(5153, 4281, 2), -- Wind Attack Weak Point
(5153, 4276, 1), -- Archery Attack Weak Point
-- Bonaparterius
(5154, 4295, 1), -- Race
(5154, 4278, 1), -- Dark Attack
(5154, 4333, 3), -- Resist Dark Attack
-- Ramsebalius
(5155, 4295, 1), -- Race
(5155, 4278, 1), -- Dark Attack
(5155, 4333, 3), -- Resist Dark Attack
-- Leto Shaman Ketz
(5156, 4295, 1), -- Race
(5156, 4151, 4), -- NPC Windstrike - Magic
(5156, 4160, 4), -- NPC Aura Burn - Magic
-- Leto Chief Narak
(5157, 4295, 1), -- Race
-- Timak Raider Kaikee
(5158, 4295, 1), -- Race
-- Timak Overlord Okun
(5159, 4295, 1), -- Race
-- Gok Magok
(5160, 4295, 1), -- Race
-- Taik Overlord Kakran
(5161, 4295, 1), -- Race
-- Hatar Chieftain Kubel
(5162, 4292, 1), -- Race
-- Vanor Elder Kerunos
(5163, 4295, 1), -- Race
-- Karul Chief Orooto
(5164, 4295, 1), -- Race
-- Abyssal Jewel 1
(5165, 4291, 1), -- Race
(5165, 4310, 1), -- Strong Type
-- Abyssal Jewel 2
(5166, 4291, 1), -- Race
(5166, 4310, 1), -- Strong Type
-- Abyssal Jewel 3
(5167, 4291, 1), -- Race
(5167, 4310, 1), -- Strong Type
-- Jewel Guardian Mara
(5168, 4298, 1), -- Race
-- Jewel Guardian Musfel
(5169, 4298, 1), -- Race
-- Jewel Guardian Pyton
(5170, 4292, 1), -- Race
-- Sacrifice Of The Sacrificed
(5171, 4291, 1), -- Race
(5171, 4275, 3), -- Sacred Attack Weak Point
(5171, 4278, 1), -- Dark Attack
(5171, 4281, 2), -- Wind Attack Weak Point
(5171, 4276, 1), -- Archery Attack Weak Point
-- Harit Lizardman Zealot
(5172, 4295, 1), -- Race
-- Box Of Athrea 1
(5173, 4290, 1), -- Race
-- Box Of Athrea 2
(5174, 4290, 1), -- Race
-- Box Of Athrea 3
(5175, 4290, 1), -- Race
-- Box Of Athrea 4
(5176, 4290, 1), -- Race
-- Box Of Athrea 5
(5177, 4290, 1), -- Race
-- Blitz Wyrm
(5178, 4299, 1), -- Race
-- Grave Keymaster
(5179, 4291, 1), -- Race
(5179, 4071, 4), -- Resist Archery
(5179, 4273, 2), -- Resist Dagger
(5179, 4274, 1), -- Blunt Attack Weak Point
-- Imperial Slave
(5180, 4290, 1), -- Race
(5180, 4275, 3), -- Sacred Attack Weak Point
(5180, 4278, 1), -- Dark Attack
-- Imperial Gravekeeper
(5181, 4290, 1), -- Race
(5181, 4275, 3), -- Sacred Attack Weak Point
(5181, 4278, 1), -- Dark Attack
-- Ark Guardian Elberoth
(5182, 4297, 1), -- Race
(5182, 4071, 3), -- Resist Archery
(5182, 4085, 1), -- Critical Power
(5182, 4086, 1), -- Critical Chance
-- Ark Guardian Shadowfang
(5183, 4297, 1), -- Race
(5183, 4071, 3), -- Resist Archery
(5183, 4085, 1), -- Critical Power
(5183, 4086, 1), -- Critical Chance
-- Angel Killer
(5184, 4290, 1), -- Race
(5184, 4310, 1), -- Strong Type
(5184, 4278, 1), -- Dark Attack
(5184, 4333, 3), -- Resist Dark Attack
(5184, 4071, 3), -- Resist Archery
(5184, 4085, 1), -- Critical Power
(5184, 4086, 1), -- Critical Chance
(5184, 4034, 6), -- Decrease Speed
(5184, 4078, 6), -- NPC Flamestrike
(5184, 4118, 6), -- Paralysis
-- Fairy Tree of Wind
(5185, 4294, 1), -- Race
(5185, 4071, 3), -- Resist Archery
-- Fairy Tree of Star
(5186, 4294, 1), -- Race
(5186, 4071, 3), -- Resist Archery
-- Fairy Tree of Twilight
(5187, 4294, 1), -- Race
(5187, 4071, 3), -- Resist Archery
-- Fairy Tree of Abyss
(5188, 4294, 1), -- Race
(5188, 4071, 3), -- Resist Archery
-- Soul of Tree Guardian
(5189, 4290, 1), -- Race
(5189, 4311, 1), -- Feeble Type
(5189, 4275, 3), -- Sacred Attack Weak Point
(5189, 4278, 1), -- Dark Attack
-- Ol Mahum Support Troop
(5190, 4295, 1), -- Race
(5190, 4032, 2), -- NPC Strike
-- Malcom
(5191, 4293, 1), -- Race
(5191, 4073, 2), -- Shock
-- Succubus Handmaiden
(5192, 4298, 1), -- Race
(5192, 4278, 1), -- Dark Attack
(5192, 4333, 3), -- Resist Dark Attack
(5192, 4071, 3), -- Resist Archery
(5192, 4151, 1), -- NPC Windstrike - Magic
(5192, 4160, 1), -- NPC Aura Burn - Magic
-- Goblin Servant
(5193, 4295, 1), -- Race
-- Julie the Ripper
(5194, 4298, 1), -- Race
(5194, 4303, 1), -- Strong Type
(5194, 4278, 1), -- Dark Attack
(5194, 4333, 3), -- Resist Dark Attack
(5194, 4071, 3), -- Resist Archery
(5194, 4085, 1), -- Critical Power
(5194, 4086, 1), -- Critical Chance
-- Male Ant
(5195, 4301, 1), -- Race
(5195, 4279, 2), -- Fire Attack Weak Point
-- Bluebacked Alligator
(5196, 4292, 1), -- Race
(5196, 4303, 1), -- Strong Type
(5196, 4071, 3), -- Resist Archery
(5196, 4073, 4), -- Shock
-- Bejewelled Alligator
(5197, 4292, 1), -- Race
(5197, 4304, 1), -- Strong Type
(5197, 4071, 3), -- Resist Archery
(5197, 4073, 4), -- Shock
-- Gremlin
(5198, 4302, 1), -- Race
-- Crimson Werewolf
(5199, 4295, 1), -- Race
-- Krudel Lizardman
(5200, 4295, 1), -- Race
-- Dummy - 1
(5201, 4295, 1), -- Race
-- Dummy - 2
(5202, 4295, 1), -- Race
-- Dummy - 3
(5203, 4295, 1), -- Race
-- Dummy - 4
(5204, 4295, 1), -- Race
-- Dummy - 5
(5205, 4295, 1), -- Race
-- Dummy - 6
(5206, 4295, 1), -- Race
-- Dummy - 7
(5207, 4295, 1), -- Race
-- Dummy - 8
(5208, 4295, 1), -- Race
-- Dummy - 9
(5209, 4295, 1), -- Race
-- Dummy - 10
(5210, 4295, 1), -- Race
-- Dummy - 11
(5211, 4295, 1), -- Race
-- Dummy - 12
(5212, 4295, 1), -- Race
-- Dummy - 13
(5213, 4295, 1), -- Race
-- Guardian of Forbidden Knowledge
(5214, 4297, 1), -- Race
-- Guardian of Forbidden Knowledge
(5215, 4297, 1), -- Race
-- Guardian of Forbidden Knowledge
(5216, 4297, 1), -- Race
-- Soul of Well
(5217, 4290, 1), -- Race
(5217, 4073, 6), -- Shock
-- Triol's Pawn
(5218, 4298, 1), -- Race
(5218, 4073, 6), -- Shock
-- Archon of Halisha
(5219, 4298, 1), -- Race
-- Archon of Halisha
(5220, 4298, 1), -- Race
-- Archon of Halisha
(5221, 4298, 1), -- Race
-- Archon of Halisha
(5222, 4298, 1), -- Race
-- Archon of Halisha
(5223, 4298, 1), -- Race
-- Archon of Halisha
(5224, 4298, 1), -- Race
-- Archon of Halisha
(5225, 4298, 1), -- Race
-- Archon of Halisha
(5226, 4298, 1), -- Race
-- Archon of Halisha
(5227, 4298, 1), -- Race
-- Archon of Halisha
(5228, 4298, 1), -- Race
-- Archon of Halisha
(5229, 4298, 1), -- Race
-- Archon of Halisha
(5230, 4298, 1), -- Race
-- Archon of Halisha
(5231, 4298, 1), -- Race
-- Archon of Halisha
(5232, 4298, 1), -- Race
-- Archon of Halisha
(5233, 4298, 1), -- Race
-- Archon of Halisha
(5234, 4298, 1), -- Race
-- Archon of Halisha
(5235, 4298, 1), -- Race
-- Archon of Halisha
(5236, 4298, 1), -- Race
-- Archon of Halisha
(5237, 4298, 1), -- Race
-- Archon of Halisha
(5238, 4298, 1), -- Race
-- Archon of Halisha
(5239, 4298, 1), -- Race
-- Archon of Halisha
(5240, 4298, 1), -- Race
-- Archon of Halisha
(5241, 4298, 1), -- Race
-- Archon of Halisha
(5242, 4298, 1), -- Race
-- Archon of Halisha
(5243, 4298, 1), -- Race
-- Archon of Halisha
(5244, 4298, 1), -- Race
-- Archon of Halisha
(5245, 4298, 1), -- Race
-- Archon of Halisha
(5246, 4298, 1), -- Race
-- Archon of Halisha
(5247, 4298, 1), -- Race
-- Archon of Halisha
(5248, 4298, 1), -- Race
-- Archon of Halisha
(5249, 4298, 1), -- Race
-- Fallen Angel Allector
(5250, 4298, 1), -- Race
(5250, 4303, 1), -- Strong Type
(5250, 4525, 1), -- Quest - BOSS Defend
(5250, 4526, 1), -- Quest - BOSS Summon
(5250, 4527, 1), -- Quest - BOSS Inc HP to Summoned
(5250, 4528, 1), -- Quest - BOSS Movement to Summoned
-- Fallen Angel Allector
(5251, 4298, 1), -- Race
(5251, 4303, 1), -- Strong Type
(5251, 4525, 1), -- Quest - BOSS Defend
(5251, 4526, 1), -- Quest - BOSS Summon
(5251, 4527, 1), -- Quest - BOSS Inc HP to Summoned
(5251, 4528, 1), -- Quest - BOSS Movement to Summoned
-- Fallen Angel Allector
(5252, 4298, 1), -- Race
(5252, 4303, 1), -- Strong Type
(5252, 4525, 1), -- Quest - BOSS Defend
(5252, 4526, 1), -- Quest - BOSS Summon
(5252, 4527, 1), -- Quest - BOSS Inc HP to Summoned
(5252, 4528, 1), -- Quest - BOSS Movement to Summoned
-- Hell Fire
(5253, 4291, 1), -- Race
(5253, 4311, 1), -- Feeble Type
(5253, 4084, 8), -- Resist Physical Attack
(5253, 4529, 1), -- Quest - Summoned Explosion
(5253, 4530, 1), -- Quest - Summoned HP Heal
(5253, 4531, 1), -- Quest - Summoned MP Heal
-- Unknown Stopper
(5254, 4298, 1), -- Race
(5254, 4310, 1), -- Strong Type
-- Unknown Stopper
(5255, 4298, 1), -- Race
(5255, 4310, 1), -- Strong Type
-- Unknown Stopper
(5256, 4298, 1), -- Race
(5256, 4310, 1), -- Strong Type
-- Archangel Iconoclasis
(5257, 4297, 1), -- Race
(5257, 4525, 1), -- Quest - BOSS Defend
(5257, 4526, 1), -- Quest - BOSS Summon
(5257, 4527, 1), -- Quest - BOSS Inc HP to Summoned
(5257, 4528, 1), -- Quest - BOSS Movement to Summoned
-- Archangel Iconoclasis
(5258, 4297, 1), -- Race
(5258, 4084, 8), -- Resist Physical Attack
(5258, 4517, 1), -- Quest - BOSS Defend
(5258, 4518, 1), -- Quest - BOSS Rampage
(5258, 4032, 8), -- NPC Strike
(5258, 4073, 8), -- Shock
(5258, 4572, 8), -- NPC Triple Sonic Slash
-- Archangel Iconoclasis
(5259, 4297, 1), -- Race
-- Archangel Iconoclasis
(5260, 4297, 1), -- Race
-- Bead of Sacred Flame
(5261, 4291, 1), -- Race
(5261, 4542, 1), -- Quest - Henchman 90% Sleep&Shock Weakness
-- Death Lord Hallate
(5262, 4290, 1), -- Race
(5262, 4310, 1), -- Strong Type
-- Death Lord Hallate
(5263, 4290, 1), -- Race
(5263, 4310, 1), -- Strong Type
-- Death Lord Hallate
(5264, 4290, 1), -- Race
(5264, 4310, 1), -- Strong Type
-- Lich King Akron
(5265, 4298, 1), -- Race
(5265, 4310, 1), -- Strong Type
-- Fallen Angel Haures
(5266, 4290, 1), -- Race
(5266, 4303, 1), -- Strong Type
(5266, 4084, 10), -- Resist Physical Attack
-- Fallen Angel Haures
(5267, 4290, 1), -- Race
(5267, 4303, 1), -- Strong Type
(5267, 4084, 10), -- Resist Physical Attack
-- Guard of Haures
(5268, 4290, 1), -- Race
(5268, 4542, 1), -- Quest - Henchman 90% Sleep&Shock Weakness
-- Fallen Angel Naverius
(5269, 4298, 1), -- Race
(5269, 4539, 1), -- Curse of Vague
(5269, 4540, 1), -- Curse of Weakness
(5269, 4541, 1), -- Curse of Nihil
-- Fallen Angel Naverius
(5270, 4298, 1), -- Race
(5270, 4539, 1), -- Curse of Vague
(5270, 4540, 1), -- Curse of Weakness
(5270, 4541, 1), -- Curse of Nihil
-- Chimera Golem
(5271, 4291, 1), -- Race
(5271, 4084, 8), -- Resist Physical Attack
(5271, 4517, 1), -- Quest - BOSS Defend
(5271, 4518, 1), -- Quest - BOSS Rampage
(5271, 4032, 8), -- NPC Strike
(5271, 4073, 8), -- Shock
(5271, 4572, 8), -- NPC Triple Sonic Slash
-- Hallate's Dancer Lillian
(5272, 4298, 1), -- Race
(5272, 4084, 8), -- Resist Physical Attack
(5272, 4533, 1), -- Dance of Resist
(5272, 4534, 1), -- Dance of Nihil
(5272, 4535, 1), -- Dance of Weakness
-- Assassin Pezel
(5273, 4290, 1), -- Race
(5273, 4310, 1), -- Strong Type
-- Fallen angel Tanakia
(5274, 4298, 1), -- Race
(5274, 4310, 1), -- Strong Type
-- Fallen angel Tanakia
(5275, 4298, 1), -- Race
(5275, 4310, 1), -- Strong Type
-- Fallen angel Tanakia
(5276, 4298, 1), -- Race
(5276, 4310, 1), -- Strong Type
-- Fallen angel Tanakia
(5277, 4298, 1), -- Race
(5277, 4310, 1), -- Strong Type
-- Shadow of Beleth
(5278, 4298, 1), -- Race
(5278, 4310, 1), -- Strong Type
-- Cursed Kesadein
(5279, 4290, 1), -- Race
(5279, 4310, 1), -- Strong Type
-- Fallen Angel Narcissus
(5280, 4298, 1), -- Race
(5280, 4310, 1), -- Strong Type
-- Fallen Angel Metellus
(5281, 4298, 1), -- Race
(5281, 4310, 1), -- Strong Type
-- Reverse Angel Odiel
(5282, 4297, 1), -- Race
(5282, 4310, 1), -- Strong Type
-- Grandpapa Askalius
(5283, 4291, 1), -- Race
(5283, 4310, 1), -- Strong Type
-- Flame Evil Spirit Azira
(5284, 4296, 1), -- Race
(5284, 4310, 1), -- Strong Type
-- Lizard's Totem Sharuhi
(5285, 4292, 1), -- Race
(5285, 4310, 1), -- Strong Type
-- Fallen Knight Adhil
(5286, 4290, 1), -- Race
(5286, 4084, 8), -- Resist Physical Attack
(5286, 4517, 1), -- Quest - BOSS Defend
(5286, 4518, 1), -- Quest - BOSS Rampage
(5286, 4032, 8), -- NPC Strike
(5286, 4073, 8), -- Shock
(5286, 4572, 8), -- NPC Triple Sonic Slash
-- Bound Elf Panacea
(5287, 4290, 1), -- Race
(5287, 4084, 8), -- Resist Physical Attack
(5287, 4517, 1), -- Quest - BOSS Defend
(5287, 4518, 1), -- Quest - BOSS Rampage
(5287, 4032, 8), -- NPC Strike
(5287, 4073, 8), -- Shock
(5287, 4572, 8), -- NPC Triple Sonic Slash
-- Sword Player Biel
(5288, 4290, 1), -- Race
(5288, 4084, 8), -- Resist Physical Attack
(5288, 4536, 1), -- Song of Seduce
(5288, 4537, 1), -- Song of Sweet Whisper
(5288, 4538, 1), -- Song of Temptation
-- Ancient Sword Master Iron
(5289, 4298, 1), -- Race
(5289, 4084, 8), -- Resist Physical Attack
(5289, 260, 37), -- Hammer Crush
(5289, 1, 37), -- Triple Slash
(5289, 4545, 1), -- Quest - BOSS Reflect
(5289, 4067, 8), -- NPC Mortal Blow
(5289, 4579, 8), -- Bleed
(5289, 4032, 8), -- NPC Strike
-- White Wing Commander 
(5290, 4297, 1), -- Race
-- White Wing Fighter
(5291, 4297, 1), -- Race
(5291, 4542, 1), -- Quest - Henchman 90% Sleep&Shock Weakness
-- Fallen Noble Orc Muhark
(5292, 4295, 1), -- Race
(5292, 4084, 8), -- Resist Physical Attack
(5292, 190, 37), -- Fatal Strike
(5292, 260, 37), -- Hammer Crush
(5292, 4545, 1), -- Quest - BOSS Reflect
(5292, 4067, 8), -- NPC Mortal Blow
(5292, 4579, 8), -- Bleed
(5292, 4032, 8), -- NPC Strike
-- Khavatari Uruz
(5293, 4295, 1), -- Race
(5293, 4084, 8), -- Resist Physical Attack
(5293, 280, 37), -- Burning Fist
(5293, 281, 37), -- Soul Breaker
(5293, 4545, 1), -- Quest - BOSS Reflect
(5293, 4067, 8), -- NPC Mortal Blow
(5293, 4579, 8), -- Bleed
(5293, 4032, 8), -- NPC Strike
-- Overlord Atrus
(5294, 4295, 1), -- Race
(5294, 4305, 1), -- Strong Type
(5294, 4543, 1), -- Quest - BOSS Shock Weakness
(5294, 4692, 1), -- Quest BOSS Big Body
(5294, 4693, 1), -- Quest BOSS Dispel Big Body
(5294, 4544, 1), -- Quest - BOSS Weakness
(5294, 4544, 2), -- Quest - BOSS Weakness
(5294, 4544, 3), -- Quest - BOSS Weakness
(5294, 4032, 8), -- NPC Strike
(5294, 4073, 8), -- Shock
(5294, 4032, 7), -- NPC Strike
-- Sharuhi Mouth Mudaha
(5295, 4295, 1), -- Race
(5295, 4305, 1), -- Strong Type
(5295, 4543, 1), -- Quest - BOSS Shock Weakness
(5295, 4692, 1), -- Quest BOSS Big Body
(5295, 4693, 1), -- Quest BOSS Dispel Big Body
(5295, 4544, 1), -- Quest - BOSS Weakness
(5295, 4544, 2), -- Quest - BOSS Weakness
(5295, 4544, 3), -- Quest - BOSS Weakness
(5295, 4032, 8), -- NPC Strike
(5295, 4073, 8), -- Shock
(5295, 4032, 7), -- NPC Strike
-- Monument Watcher Ezekiel
(5296, 4297, 1), -- Race
(5296, 4521, 1), -- Quest - BOSS Resist non Bow Weapon
(5296, 19, 37), -- Double Shot
(5296, 101, 40), -- Stun Shot
(5296, 4519, 1), -- Quest - BOSS Defend
(5296, 4520, 1), -- Quest - BOSS Rampage
(5296, 4067, 8), -- NPC Mortal Blow
(5296, 4579, 8), -- Bleed
(5296, 4032, 8), -- NPC Strike
-- Monument Watcher Ezekiel
(5297, 4297, 1), -- Race
(5297, 4521, 1), -- Quest - BOSS Resist non Bow Weapon
(5297, 19, 37), -- Double Shot
(5297, 101, 40), -- Stun Shot
(5297, 4519, 1), -- Quest - BOSS Defend
(5297, 4520, 1), -- Quest - BOSS Rampage
(5297, 4067, 8), -- NPC Mortal Blow
(5297, 4579, 8), -- Bleed
(5297, 4032, 8), -- NPC Strike
-- Monument Watcher Ezekiel
(5298, 4297, 1), -- Race
(5298, 4521, 1), -- Quest - BOSS Resist non Bow Weapon
(5298, 19, 37), -- Double Shot
(5298, 101, 40), -- Stun Shot
(5298, 4519, 1), -- Quest - BOSS Defend
(5298, 4520, 1), -- Quest - BOSS Rampage
(5298, 4067, 8), -- NPC Mortal Blow
(5298, 4579, 8), -- Bleed
(5298, 4032, 8), -- NPC Strike
-- Monument Watcher Ezekiel
(5299, 4297, 1), -- Race
(5299, 4523, 1), -- Quest - BOSS Evasion
(5299, 4524, 1), -- Quest - BOSS Bluff
(5299, 4522, 1), -- Eye of Assassin
(5299, 4101, 8), -- NPC Spinning Slasher
(5299, 4072, 7), -- Shock
(5299, 4032, 8), -- NPC Strike
-- Monument Watcher Ezekiel
(5300, 4297, 1), -- Race
(5300, 4523, 1), -- Quest - BOSS Evasion
(5300, 4524, 1), -- Quest - BOSS Bluff
(5300, 4522, 1), -- Eye of Assassin
(5300, 4101, 8), -- NPC Spinning Slasher
(5300, 4072, 7), -- Shock
(5300, 4032, 8), -- NPC Strike
-- Monument Watcher Ezekiel
(5301, 4297, 1), -- Race
(5301, 4523, 1), -- Quest - BOSS Evasion
(5301, 4524, 1), -- Quest - BOSS Bluff
(5301, 4522, 1), -- Eye of Assassin
(5301, 4101, 8), -- NPC Spinning Slasher
(5301, 4072, 7), -- Shock
(5301, 4032, 8), -- NPC Strike
-- Monument Defender Azrael
(5302, 4297, 1), -- Race
(5302, 4310, 1), -- Strong Type
-- Monument Defender Azrael
(5303, 4297, 1), -- Race
(5303, 4310, 1), -- Strong Type
-- Monument Defender Azrael
(5304, 4297, 1), -- Race
(5304, 4310, 1), -- Strong Type
-- Monument Defender Azrael
(5305, 4297, 1), -- Race
(5305, 4310, 1), -- Strong Type
-- Monument Defender Azrael
(5306, 4297, 1), -- Race
(5306, 4310, 1), -- Strong Type
-- Monument Defender Azrael
(5307, 4297, 1), -- Race
(5307, 4310, 1), -- Strong Type
-- Assassin Frost
(5308, 4298, 1), -- Race
(5308, 4310, 1), -- Strong Type
-- Minervia Van Hacken
(5309, 4298, 1), -- Race
(5309, 4310, 1), -- Strong Type
-- Kitanis Van Hacken
(5310, 4298, 1), -- Race
(5310, 4310, 1), -- Strong Type
-- Judgment Hound Dog Kelvas
(5311, 4292, 1), -- Race
(5311, 4310, 1), -- Strong Type
-- Mysterious Servitor
(5312, 4298, 1), -- Race
(5312, 4310, 1), -- Strong Type
-- Vision Guardian Shakiel
(5313, 4297, 1), -- Race
(5313, 4532, 1), -- Quest - BOSS Reflect
(5313, 4032, 8), -- NPC Strike
(5313, 4076, 3), -- Reduction in movement speed
(5313, 4032, 7), -- NPC Strike
-- Vision Guardian Shakiel
(5314, 4297, 1), -- Race
(5314, 4532, 1), -- Quest - BOSS Reflect
(5314, 4032, 8), -- NPC Strike
(5314, 4076, 3), -- Reduction in movement speed
(5314, 4032, 7), -- NPC Strike
-- Vision Guardian Shakiel
(5315, 4297, 1), -- Race
(5315, 4532, 1), -- Quest - BOSS Reflect
(5315, 4032, 8), -- NPC Strike
(5315, 4076, 3), -- Reduction in movement speed
(5315, 4032, 7), -- NPC Strike
-- Fallen Chieftain Vegus
(5316, 4295, 1), -- Race
-- Restrainer of Glory
(5317, 4297, 1), -- Race
-- Lector
(7001, 4290, 1), -- Race
(7001, 4045, 1), -- Resist Full Magic Attack
-- Jackson
(7002, 4290, 1), -- Race
(7002, 4045, 1), -- Resist Full Magic Attack
-- Silvia
(7003, 4290, 1), -- Race
(7003, 4045, 1), -- Resist Full Magic Attack
-- Katerina
(7004, 4290, 1), -- Race
(7004, 4045, 1), -- Resist Full Magic Attack
-- Wilford
(7005, 4290, 1), -- Race
(7005, 4045, 1), -- Resist Full Magic Attack
-- Roxxy
(7006, 4290, 1), -- Race
(7006, 4045, 1), -- Resist Full Magic Attack
-- Wiri
(7007, 4302, 1), -- Race
(7007, 4045, 1), -- Resist Full Magic Attack
-- Roien
(7008, 4290, 1), -- Race
(7008, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7009, 4290, 1), -- Race
(7009, 4045, 1), -- Resist Full Magic Attack
-- Auron
(7010, 4290, 1), -- Race
(7010, 4045, 1), -- Resist Full Magic Attack
-- Trash
(7011, 4290, 1), -- Race
(7011, 4045, 1), -- Resist Full Magic Attack
-- Trash
(7012, 4290, 1), -- Race
(7012, 4045, 1), -- Resist Full Magic Attack
-- Nedroll
(7013, 4290, 1), -- Race
(7013, 4045, 1), -- Resist Full Magic Attack
-- Celma
(7014, 4290, 1), -- Race
(7014, 4045, 1), -- Resist Full Magic Attack
-- Hildia
(7015, 4290, 1), -- Race
(7015, 4045, 1), -- Resist Full Magic Attack
-- Willem
(7016, 4290, 1), -- Race
(7016, 4045, 1), -- Resist Full Magic Attack
-- Gallint
(7017, 4290, 1), -- Race
(7017, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7018, 4290, 1), -- Race
(7018, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7019, 4290, 1), -- Race
(7019, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7020, 4290, 1), -- Race
(7020, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7021, 4290, 1), -- Race
(7021, 4045, 1), -- Resist Full Magic Attack
-- Zigaunt
(7022, 4290, 1), -- Race
(7022, 4045, 1), -- Resist Full Magic Attack
-- Trash
(7023, 4290, 1), -- Race
(7023, 4045, 1), -- Resist Full Magic Attack
-- Trash
(7024, 4290, 1), -- Race
(7024, 4045, 1), -- Resist Full Magic Attack
-- Trash
(7025, 4290, 1), -- Race
(7025, 4045, 1), -- Resist Full Magic Attack
-- Bitz
(7026, 4290, 1), -- Race
(7026, 4045, 1), -- Resist Full Magic Attack
-- Gwinter
(7027, 4290, 1), -- Race
(7027, 4045, 1), -- Resist Full Magic Attack
-- Pintage
(7028, 4290, 1), -- Race
(7028, 4045, 1), -- Resist Full Magic Attack
-- Minia
(7029, 4290, 1), -- Race
(7029, 4045, 1), -- Resist Full Magic Attack
-- Vivyan
(7030, 4290, 1), -- Race
(7030, 4045, 1), -- Resist Full Magic Attack
-- Biotin
(7031, 4290, 1), -- Race
(7031, 4045, 1), -- Resist Full Magic Attack
-- Yohanes
(7032, 4290, 1), -- Race
(7032, 4045, 1), -- Resist Full Magic Attack
-- Baulro
(7033, 4290, 1), -- Race
(7033, 4045, 1), -- Resist Full Magic Attack
-- Iris
(7034, 4290, 1), -- Race
(7034, 4045, 1), -- Resist Full Magic Attack
-- Harrys
(7035, 4290, 1), -- Race
(7035, 4045, 1), -- Resist Full Magic Attack
-- Petron
(7036, 4290, 1), -- Race
(7036, 4045, 1), -- Resist Full Magic Attack
-- Levian
(7037, 4290, 1), -- Race
(7037, 4045, 1), -- Resist Full Magic Attack
-- Trash
(7038, 4290, 1), -- Race
(7038, 4045, 1), -- Resist Full Magic Attack
-- Gilbert
(7039, 4290, 1), -- Race
(7039, 4045, 1), -- Resist Full Magic Attack
-- Leon
(7040, 4290, 1), -- Race
(7040, 4045, 1), -- Resist Full Magic Attack
-- Arnold
(7041, 4290, 1), -- Race
(7041, 4045, 1), -- Resist Full Magic Attack
-- Abellos
(7042, 4290, 1), -- Race
(7042, 4045, 1), -- Resist Full Magic Attack
-- Johnstone
(7043, 4290, 1), -- Race
(7043, 4045, 1), -- Resist Full Magic Attack
-- Chiperan
(7044, 4290, 1), -- Race
(7044, 4045, 1), -- Resist Full Magic Attack
-- Kenyos
(7045, 4290, 1), -- Race
(7045, 4045, 1), -- Resist Full Magic Attack
-- Hanks
(7046, 4290, 1), -- Race
(7046, 4045, 1), -- Resist Full Magic Attack
-- Firon
(7047, 4290, 1), -- Race
(7047, 4045, 1), -- Resist Full Magic Attack
-- Darin
(7048, 4290, 1), -- Race
(7048, 4045, 1), -- Resist Full Magic Attack
-- Bonnie
(7049, 4290, 1), -- Race
(7049, 4045, 1), -- Resist Full Magic Attack
-- Elias
(7050, 4290, 1), -- Race
(7050, 4045, 1), -- Resist Full Magic Attack
-- Cristel
(7051, 4290, 1), -- Race
(7051, 4045, 1), -- Resist Full Magic Attack
-- trash
(7052, 4290, 1), -- Race
(7052, 4045, 1), -- Resist Full Magic Attack
-- trash
(7053, 4290, 1), -- Race
(7053, 4045, 1), -- Resist Full Magic Attack
-- Rant
(7054, 4290, 1), -- Race
(7054, 4045, 1), -- Resist Full Magic Attack
-- Rolfe
(7055, 4290, 1), -- Race
(7055, 4045, 1), -- Resist Full Magic Attack
-- Trash
(7056, 4290, 1), -- Race
(7056, 4045, 1), -- Resist Full Magic Attack
-- Aldo
(7057, 4290, 1), -- Race
(7057, 4045, 1), -- Resist Full Magic Attack
-- Holvas
(7058, 4290, 1), -- Race
(7058, 4045, 1), -- Resist Full Magic Attack
-- Trisha 
(7059, 4290, 1), -- Race
(7059, 4045, 1), -- Resist Full Magic Attack
-- Sabrin
(7060, 4290, 1), -- Race
(7060, 4045, 1), -- Resist Full Magic Attack
-- Casey
(7061, 4290, 1), -- Race
(7061, 4045, 1), -- Resist Full Magic Attack
-- Sonia
(7062, 4290, 1), -- Race
(7062, 4045, 1), -- Resist Full Magic Attack
-- Lara
(7063, 4290, 1), -- Race
(7063, 4045, 1), -- Resist Full Magic Attack
-- Terry
(7064, 4290, 1), -- Race
(7064, 4045, 1), -- Resist Full Magic Attack
-- Arnelle
(7065, 4290, 1), -- Race
(7065, 4045, 1), -- Resist Full Magic Attack
-- Pabris
(7066, 4290, 1), -- Race
(7066, 4045, 1), -- Resist Full Magic Attack
-- Glyvka
(7067, 4290, 1), -- Race
(7067, 4045, 1), -- Resist Full Magic Attack
-- Shegfield
(7068, 4290, 1), -- Race
(7068, 4045, 1), -- Resist Full Magic Attack
-- Rollant
(7069, 4290, 1), -- Race
(7069, 4045, 1), -- Resist Full Magic Attack
-- Sylvain
(7070, 4290, 1), -- Race
(7070, 4045, 1), -- Resist Full Magic Attack
-- Lucas
(7071, 4290, 1), -- Race
(7071, 4045, 1), -- Resist Full Magic Attack
-- Metty
(7072, 4290, 1), -- Race
(7072, 4045, 1), -- Resist Full Magic Attack
-- Jacob
(7073, 4290, 1), -- Race
(7073, 4045, 1), -- Resist Full Magic Attack
-- Harlan
(7074, 4290, 1), -- Race
(7074, 4045, 1), -- Resist Full Magic Attack
-- Xaber
(7075, 4290, 1), -- Race
(7075, 4045, 1), -- Resist Full Magic Attack
-- Liam
(7076, 4290, 1), -- Race
(7076, 4045, 1), -- Resist Full Magic Attack
-- trash
(7077, 4290, 1), -- Race
(7077, 4045, 1), -- Resist Full Magic Attack
-- Pano
(7078, 4290, 1), -- Race
(7078, 4045, 1), -- Resist Full Magic Attack
-- Barder
(7079, 4290, 1), -- Race
(7079, 4045, 1), -- Resist Full Magic Attack
-- Clarissa
(7080, 4290, 1), -- Race
(7080, 4045, 1), -- Resist Full Magic Attack
-- Helvetia
(7081, 4290, 1), -- Race
(7081, 4045, 1), -- Resist Full Magic Attack
-- Denkus
(7082, 4290, 1), -- Race
(7082, 4045, 1), -- Resist Full Magic Attack
-- Pochi
(7083, 4290, 1), -- Race
(7083, 4045, 1), -- Resist Full Magic Attack
-- Graham
(7084, 4290, 1), -- Race
(7084, 4045, 1), -- Resist Full Magic Attack
-- Stanford
(7085, 4290, 1), -- Race
(7085, 4045, 1), -- Resist Full Magic Attack
-- Taurin
(7086, 4290, 1), -- Race
(7086, 4045, 1), -- Resist Full Magic Attack
--  Peta
(7087, 4290, 1), -- Race
(7087, 4045, 1), -- Resist Full Magic Attack
-- Radia
(7088, 4290, 1), -- Race
(7088, 4045, 1), -- Resist Full Magic Attack
-- trash
(7089, 4290, 1), -- Race
(7089, 4045, 1), -- Resist Full Magic Attack
-- Sandra
(7090, 4290, 1), -- Race
(7090, 4045, 1), -- Resist Full Magic Attack
-- Ellie
(7091, 4290, 1), -- Race
(7091, 4045, 1), -- Resist Full Magic Attack
-- Collob
(7092, 4290, 1), -- Race
(7092, 4045, 1), -- Resist Full Magic Attack
-- Groot
(7093, 4290, 1), -- Race
(7093, 4045, 1), -- Resist Full Magic Attack
-- Gentler
(7094, 4290, 1), -- Race
(7094, 4045, 1), -- Resist Full Magic Attack
-- Randolf
(7095, 4290, 1), -- Race
(7095, 4045, 1), -- Resist Full Magic Attack
-- trash
(7096, 4290, 1), -- Race
(7096, 4045, 1), -- Resist Full Magic Attack
-- Galladucci
(7097, 4290, 1), -- Race
(7097, 4045, 1), -- Resist Full Magic Attack
-- Alexandria
(7098, 4290, 1), -- Race
(7098, 4045, 1), -- Resist Full Magic Attack
-- trash
(7099, 4290, 1), -- Race
(7099, 4045, 1), -- Resist Full Magic Attack
-- trash
(7100, 4290, 1), -- Race
(7100, 4045, 1), -- Resist Full Magic Attack
-- trash
(7101, 4290, 1), -- Race
(7101, 4045, 1), -- Resist Full Magic Attack
-- trash
(7102, 4290, 1), -- Race
(7102, 4045, 1), -- Resist Full Magic Attack
-- Valkon
(7103, 4290, 1), -- Race
(7103, 4045, 1), -- Resist Full Magic Attack
-- Parman
(7104, 4290, 1), -- Race
(7104, 4045, 1), -- Resist Full Magic Attack
-- Genwitter
(7105, 4290, 1), -- Race
(7105, 4045, 1), -- Resist Full Magic Attack
-- Dufner
(7106, 4290, 1), -- Race
(7106, 4045, 1), -- Resist Full Magic Attack
-- Goldian
(7107, 4290, 1), -- Race
(7107, 4045, 1), -- Resist Full Magic Attack
-- Macken
(7108, 4290, 1), -- Race
(7108, 4045, 1), -- Resist Full Magic Attack
-- Hannavalt
(7109, 4290, 1), -- Race
(7109, 4045, 1), -- Resist Full Magic Attack
-- Iker
(7110, 4290, 1), -- Race
(7110, 4045, 1), -- Resist Full Magic Attack
-- Dieter
(7111, 4290, 1), -- Race
(7111, 4045, 1), -- Resist Full Magic Attack
-- Maurius
(7112, 4290, 1), -- Race
(7112, 4045, 1), -- Resist Full Magic Attack
-- Juris
(7113, 4290, 1), -- Race
(7113, 4045, 1), -- Resist Full Magic Attack
-- Roa
(7114, 4290, 1), -- Race
(7114, 4045, 1), -- Resist Full Magic Attack
-- Jurek
(7115, 4290, 1), -- Race
(7115, 4045, 1), -- Resist Full Magic Attack
-- Dustin
(7116, 4290, 1), -- Race
(7116, 4045, 1), -- Resist Full Magic Attack
-- Primos
(7117, 4290, 1), -- Race
(7117, 4045, 1), -- Resist Full Magic Attack
-- Pupina
(7118, 4290, 1), -- Race
(7118, 4045, 1), -- Resist Full Magic Attack
-- Isabellin
(7119, 4290, 1), -- Race
(7119, 4045, 1), -- Resist Full Magic Attack
-- Maximilian
(7120, 4290, 1), -- Race
(7120, 4045, 1), -- Resist Full Magic Attack
-- Jeronin
(7121, 4290, 1), -- Race
(7121, 4045, 1), -- Resist Full Magic Attack
-- Bane
(7122, 4290, 1), -- Race
(7122, 4045, 1), -- Resist Full Magic Attack
-- Vesa
(7123, 4290, 1), -- Race
(7123, 4045, 1), -- Resist Full Magic Attack
-- Zerome
(7124, 4290, 1), -- Race
(7124, 4045, 1), -- Resist Full Magic Attack
-- Belton
(7125, 4290, 1), -- Race
(7125, 4045, 1), -- Resist Full Magic Attack
-- Rath
(7126, 4290, 1), -- Race
(7126, 4045, 1), -- Resist Full Magic Attack
-- trash
(7127, 4290, 1), -- Race
(7127, 4045, 1), -- Resist Full Magic Attack
-- Atanas
(7128, 4290, 1), -- Race
(7128, 4045, 1), -- Resist Full Magic Attack
-- Mitraell
(7129, 4290, 1), -- Race
(7129, 4045, 1), -- Resist Full Magic Attack
-- Undrias
(7130, 4290, 1), -- Race
(7130, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7131, 4290, 1), -- Race
(7131, 4045, 1), -- Resist Full Magic Attack
-- Cecktinon
(7132, 4290, 1), -- Race
(7132, 4045, 1), -- Resist Full Magic Attack
-- Kartia
(7133, 4290, 1), -- Race
(7133, 4045, 1), -- Resist Full Magic Attack
-- Jasmine
(7134, 4290, 1), -- Race
(7134, 4045, 1), -- Resist Full Magic Attack
-- Iria
(7135, 4290, 1), -- Race
(7135, 4045, 1), -- Resist Full Magic Attack
-- Payne
(7136, 4290, 1), -- Race
(7136, 4045, 1), -- Resist Full Magic Attack
-- Vollodos
(7137, 4290, 1), -- Race
(7137, 4045, 1), -- Resist Full Magic Attack
-- Minaless
(7138, 4290, 1), -- Race
(7138, 4045, 1), -- Resist Full Magic Attack
-- Dorankus
(7139, 4290, 1), -- Race
(7139, 4045, 1), -- Resist Full Magic Attack
-- Erviante
(7140, 4290, 1), -- Race
(7140, 4045, 1), -- Resist Full Magic Attack
-- Talloth
(7141, 4290, 1), -- Race
(7141, 4045, 1), -- Resist Full Magic Attack
-- trash
(7142, 4290, 1), -- Race
(7142, 4045, 1), -- Resist Full Magic Attack
-- Trudy
(7143, 4290, 1), -- Race
(7143, 4045, 1), -- Resist Full Magic Attack
-- Harne
(7144, 4290, 1), -- Race
(7144, 4045, 1), -- Resist Full Magic Attack
-- Vlasty
(7145, 4290, 1), -- Race
(7145, 4045, 1), -- Resist Full Magic Attack
-- Mirabel
(7146, 4290, 1), -- Race
(7146, 4045, 1), -- Resist Full Magic Attack
-- Unoren
(7147, 4290, 1), -- Race
(7147, 4045, 1), -- Resist Full Magic Attack
-- Ariel
(7148, 4290, 1), -- Race
(7148, 4045, 1), -- Resist Full Magic Attack
-- Creamees
(7149, 4290, 1), -- Race
(7149, 4045, 1), -- Resist Full Magic Attack
-- Herbiel
(7150, 4290, 1), -- Race
(7150, 4045, 1), -- Resist Full Magic Attack
-- Chad
(7151, 4290, 1), -- Race
(7151, 4045, 1), -- Resist Full Magic Attack
-- Julia
(7152, 4290, 1), -- Race
(7152, 4045, 1), -- Resist Full Magic Attack
-- Markius
(7153, 4290, 1), -- Race
(7153, 4045, 1), -- Resist Full Magic Attack
-- Asterios
(7154, 4290, 1), -- Race
(7154, 4045, 1), -- Resist Full Magic Attack
-- Ellenia
(7155, 4290, 1), -- Race
(7155, 4045, 1), -- Resist Full Magic Attack
-- Cobendell
(7156, 4290, 1), -- Race
(7156, 4045, 1), -- Resist Full Magic Attack
-- Greenis
(7157, 4290, 1), -- Race
(7157, 4045, 1), -- Resist Full Magic Attack
-- Esrandell
(7158, 4290, 1), -- Race
(7158, 4045, 1), -- Resist Full Magic Attack
-- trash
(7159, 4290, 1), -- Race
(7159, 4045, 1), -- Resist Full Magic Attack
-- trash
(7160, 4290, 1), -- Race
(7160, 4045, 1), -- Resist Full Magic Attack
-- trash
(7161, 4290, 1), -- Race
(7161, 4045, 1), -- Resist Full Magic Attack
-- Karin
(7162, 4290, 1), -- Race
(7162, 4045, 1), -- Resist Full Magic Attack
-- Rex
(7163, 4290, 1), -- Race
(7163, 4045, 1), -- Resist Full Magic Attack
-- Ian
(7164, 4290, 1), -- Race
(7164, 4045, 1), -- Resist Full Magic Attack
-- Ralford
(7165, 4290, 1), -- Race
(7165, 4045, 1), -- Resist Full Magic Attack
-- Wesley
(7166, 4290, 1), -- Race
(7166, 4045, 1), -- Resist Full Magic Attack
-- trash
(7167, 4290, 1), -- Race
(7167, 4045, 1), -- Resist Full Magic Attack
-- trash
(7168, 4290, 1), -- Race
(7168, 4045, 1), -- Resist Full Magic Attack
-- Marty
(7169, 4290, 1), -- Race
(7169, 4045, 1), -- Resist Full Magic Attack
-- Radic
(7170, 4290, 1), -- Race
(7170, 4045, 1), -- Resist Full Magic Attack
-- Galios
(7171, 4290, 1), -- Race
(7171, 4045, 1), -- Resist Full Magic Attack
-- trash
(7172, 4290, 1), -- Race
(7172, 4045, 1), -- Resist Full Magic Attack
-- trash
(7173, 4290, 1), -- Race
(7173, 4045, 1), -- Resist Full Magic Attack
-- Arkenias
(7174, 4290, 1), -- Race
(7174, 4045, 1), -- Resist Full Magic Attack
-- Fairen
(7175, 4290, 1), -- Race
(7175, 4045, 1), -- Resist Full Magic Attack
-- Valleria
(7176, 4290, 1), -- Race
(7176, 4045, 1), -- Resist Full Magic Attack
-- Valentina
(7177, 4290, 1), -- Race
(7177, 4045, 1), -- Resist Full Magic Attack
-- Zenkin
(7178, 4290, 1), -- Race
(7178, 4045, 1), -- Resist Full Magic Attack
-- Raudia
(7179, 4290, 1), -- Race
(7179, 4045, 1), -- Resist Full Magic Attack
-- Sara
(7180, 4290, 1), -- Race
(7180, 4045, 1), -- Resist Full Magic Attack
-- Galibredo
(7181, 4290, 1), -- Race
(7181, 4045, 1), -- Resist Full Magic Attack
-- Cliff
(7182, 4290, 1), -- Race
(7182, 4045, 1), -- Resist Full Magic Attack
-- Hagger
(7183, 4290, 1), -- Race
(7183, 4045, 1), -- Resist Full Magic Attack
-- Rigol
(7184, 4290, 1), -- Race
(7184, 4045, 1), -- Resist Full Magic Attack
-- Taniac Blackbird
(7185, 4290, 1), -- Race
(7185, 4045, 1), -- Resist Full Magic Attack
-- Bhan
(7186, 4290, 1), -- Race
(7186, 4045, 1), -- Resist Full Magic Attack
-- Klaus Blackbird
(7187, 4290, 1), -- Race
(7187, 4045, 1), -- Resist Full Magic Attack
-- Vadin
(7188, 4290, 1), -- Race
(7188, 4045, 1), -- Resist Full Magic Attack
-- Rovia
(7189, 4290, 1), -- Race
(7189, 4045, 1), -- Resist Full Magic Attack
-- Phanovia
(7190, 4290, 1), -- Race
(7190, 4045, 1), -- Resist Full Magic Attack
-- Hollint
(7191, 4290, 1), -- Race
(7191, 4045, 1), -- Resist Full Magic Attack
-- Darya
(7192, 4290, 1), -- Race
(7192, 4045, 1), -- Resist Full Magic Attack
-- Erskine
(7193, 4290, 1), -- Race
(7193, 4045, 1), -- Resist Full Magic Attack
-- Gideon
(7194, 4290, 1), -- Race
(7194, 4045, 1), -- Resist Full Magic Attack
-- Brecson
(7195, 4290, 1), -- Race
(7195, 4045, 1), -- Resist Full Magic Attack
-- Mouen
(7196, 4290, 1), -- Race
(7196, 4045, 1), -- Resist Full Magic Attack
-- Hector
(7197, 4290, 1), -- Race
(7197, 4045, 1), -- Resist Full Magic Attack
-- Jerin
(7198, 4290, 1), -- Race
(7198, 4045, 1), -- Resist Full Magic Attack
-- Yates
(7199, 4290, 1), -- Race
(7199, 4045, 1), -- Resist Full Magic Attack
-- Stan
(7200, 4290, 1), -- Race
(7200, 4045, 1), -- Resist Full Magic Attack
-- Pinaps
(7201, 4290, 1), -- Race
(7201, 4045, 1), -- Resist Full Magic Attack
-- trash
(7202, 4290, 1), -- Race
(7202, 4045, 1), -- Resist Full Magic Attack
-- trash
(7203, 4290, 1), -- Race
(7203, 4045, 1), -- Resist Full Magic Attack
-- trash
(7204, 4290, 1), -- Race
(7204, 4045, 1), -- Resist Full Magic Attack
-- trash
(7205, 4290, 1), -- Race
(7205, 4045, 1), -- Resist Full Magic Attack
-- trash
(7206, 4290, 1), -- Race
(7206, 4045, 1), -- Resist Full Magic Attack
-- Arodin
(7207, 4290, 1), -- Race
(7207, 4045, 1), -- Resist Full Magic Attack
-- Damion
(7208, 4290, 1), -- Race
(7208, 4045, 1), -- Resist Full Magic Attack
-- Colleen
(7209, 4290, 1), -- Race
(7209, 4045, 1), -- Resist Full Magic Attack
-- Norman
(7210, 4290, 1), -- Race
(7210, 4045, 1), -- Resist Full Magic Attack
-- trash
(7211, 4290, 1), -- Race
(7211, 4045, 1), -- Resist Full Magic Attack
-- trash
(7212, 4290, 1), -- Race
(7212, 4045, 1), -- Resist Full Magic Attack
-- trash
(7213, 4290, 1), -- Race
(7213, 4045, 1), -- Resist Full Magic Attack
-- trash
(7214, 4290, 1), -- Race
(7214, 4045, 1), -- Resist Full Magic Attack
-- trash
(7215, 4290, 1), -- Race
(7215, 4045, 1), -- Resist Full Magic Attack
-- Wheeler
(7216, 4290, 1), -- Race
(7216, 4045, 1), -- Resist Full Magic Attack
-- Berros
(7217, 4290, 1), -- Race
(7217, 4045, 1), -- Resist Full Magic Attack
-- Kendell
(7218, 4290, 1), -- Race
(7218, 4045, 1), -- Resist Full Magic Attack
-- Veltress
(7219, 4290, 1), -- Race
(7219, 4045, 1), -- Resist Full Magic Attack
-- Starden
(7220, 4290, 1), -- Race
(7220, 4045, 1), -- Resist Full Magic Attack
-- Rayen
(7221, 4290, 1), -- Race
(7221, 4045, 1), -- Resist Full Magic Attack
-- Alshupes
(7222, 4290, 1), -- Race
(7222, 4045, 1), -- Resist Full Magic Attack
-- Arujien 
(7223, 4290, 1), -- Race
(7223, 4045, 1), -- Resist Full Magic Attack
-- Rayla
(7224, 4290, 1), -- Race
(7224, 4045, 1), -- Resist Full Magic Attack
-- trash
(7225, 4290, 1), -- Race
(7225, 4045, 1), -- Resist Full Magic Attack
-- trash
(7226, 4290, 1), -- Race
(7226, 4045, 1), -- Resist Full Magic Attack
-- trash
(7227, 4290, 1), -- Race
(7227, 4045, 1), -- Resist Full Magic Attack
-- trash
(7228, 4290, 1), -- Race
(7228, 4045, 1), -- Resist Full Magic Attack
-- trash
(7229, 4290, 1), -- Race
(7229, 4045, 1), -- Resist Full Magic Attack
-- Edroc
(7230, 4290, 1), -- Race
(7230, 4045, 1), -- Resist Full Magic Attack
-- Garette
(7231, 4290, 1), -- Race
(7231, 4045, 1), -- Resist Full Magic Attack
-- Sorint
(7232, 4290, 1), -- Race
(7232, 4045, 1), -- Resist Full Magic Attack
-- Esmeralda
(7233, 4290, 1), -- Race
(7233, 4045, 1), -- Resist Full Magic Attack
-- trash
(7234, 4290, 1), -- Race
(7234, 4045, 1), -- Resist Full Magic Attack
-- trash
(7235, 4290, 1), -- Race
(7235, 4045, 1), -- Resist Full Magic Attack
-- trash
(7236, 4290, 1), -- Race
(7236, 4045, 1), -- Resist Full Magic Attack
-- trash
(7237, 4290, 1), -- Race
(7237, 4045, 1), -- Resist Full Magic Attack
-- trash
(7238, 4290, 1), -- Race
(7238, 4045, 1), -- Resist Full Magic Attack
-- trash
(7239, 4290, 1), -- Race
(7239, 4045, 1), -- Resist Full Magic Attack
-- trash
(7240, 4290, 1), -- Race
(7240, 4045, 1), -- Resist Full Magic Attack
-- trash
(7241, 4290, 1), -- Race
(7241, 4045, 1), -- Resist Full Magic Attack
-- trash
(7242, 4290, 1), -- Race
(7242, 4045, 1), -- Resist Full Magic Attack
-- trash
(7243, 4290, 1), -- Race
(7243, 4045, 1), -- Resist Full Magic Attack
-- trash
(7244, 4290, 1), -- Race
(7244, 4045, 1), -- Resist Full Magic Attack
-- trash
(7245, 4290, 1), -- Race
(7245, 4045, 1), -- Resist Full Magic Attack
-- trash
(7246, 4290, 1), -- Race
(7246, 4045, 1), -- Resist Full Magic Attack
-- trash
(7247, 4290, 1), -- Race
(7247, 4045, 1), -- Resist Full Magic Attack
-- trash
(7248, 4290, 1), -- Race
(7248, 4045, 1), -- Resist Full Magic Attack
-- trash
(7249, 4290, 1), -- Race
(7249, 4045, 1), -- Resist Full Magic Attack
-- Gludio Holy Artifact
(7250, 4290, 1), -- Race
(7250, 4045, 1), -- Resist Full Magic Attack
-- trash
(7251, 4291, 1), -- Race
(7251, 4045, 1), -- Resist Full Magic Attack
-- trash
(7252, 4290, 1), -- Race
(7252, 4045, 1), -- Resist Full Magic Attack
-- Simplon
(7253, 4290, 1), -- Race
(7253, 4045, 1), -- Resist Full Magic Attack
-- Harmony
(7254, 4290, 1), -- Race
(7254, 4045, 1), -- Resist Full Magic Attack
-- Haprock
(7255, 4290, 1), -- Race
(7255, 4045, 1), -- Resist Full Magic Attack
-- Bella
(7256, 4290, 1), -- Race
(7256, 4045, 1), -- Resist Full Magic Attack
-- trash
(7257, 4290, 1), -- Race
(7257, 4045, 1), -- Resist Full Magic Attack
-- trash
(7258, 4290, 1), -- Race
(7258, 4045, 1), -- Resist Full Magic Attack
-- trash
(7259, 4290, 1), -- Race
(7259, 4045, 1), -- Resist Full Magic Attack
-- trash
(7260, 4290, 1), -- Race
(7260, 4045, 1), -- Resist Full Magic Attack
-- trash
(7261, 4290, 1), -- Race
(7261, 4045, 1), -- Resist Full Magic Attack
-- trash
(7262, 4290, 1), -- Race
(7262, 4045, 1), -- Resist Full Magic Attack
-- trash
(7263, 4290, 1), -- Race
(7263, 4045, 1), -- Resist Full Magic Attack
-- trash
(7264, 4290, 1), -- Race
(7264, 4045, 1), -- Resist Full Magic Attack
-- trash
(7265, 4290, 1), -- Race
(7265, 4045, 1), -- Resist Full Magic Attack
-- trash
(7266, 4290, 1), -- Race
(7266, 4045, 1), -- Resist Full Magic Attack
-- trash
(7267, 4290, 1), -- Race
(7267, 4045, 1), -- Resist Full Magic Attack
-- trash
(7268, 4290, 1), -- Race
(7268, 4045, 1), -- Resist Full Magic Attack
-- trash
(7269, 4290, 1), -- Race
(7269, 4045, 1), -- Resist Full Magic Attack
-- trash
(7270, 4290, 1), -- Race
(7270, 4045, 1), -- Resist Full Magic Attack
-- trash
(7271, 4290, 1), -- Race
(7271, 4045, 1), -- Resist Full Magic Attack
-- trash
(7272, 4290, 1), -- Race
(7272, 4045, 1), -- Resist Full Magic Attack
-- trash
(7273, 4290, 1), -- Race
(7273, 4045, 1), -- Resist Full Magic Attack
-- trash
(7274, 4290, 1), -- Race
(7274, 4045, 1), -- Resist Full Magic Attack
-- trash
(7275, 4290, 1), -- Race
(7275, 4045, 1), -- Resist Full Magic Attack
-- trash
(7276, 4290, 1), -- Race
(7276, 4045, 1), -- Resist Full Magic Attack
-- trash
(7277, 4290, 1), -- Race
(7277, 4045, 1), -- Resist Full Magic Attack
-- trash
(7278, 4290, 1), -- Race
(7278, 4045, 1), -- Resist Full Magic Attack
-- trash
(7279, 4290, 1), -- Race
(7279, 4045, 1), -- Resist Full Magic Attack
-- trash
(7280, 4290, 1), -- Race
(7280, 4045, 1), -- Resist Full Magic Attack
-- trash
(7281, 4290, 1), -- Race
(7281, 4045, 1), -- Resist Full Magic Attack
-- trash
(7282, 4290, 1), -- Race
(7282, 4045, 1), -- Resist Full Magic Attack
-- Altran
(7283, 4290, 1), -- Race
(7283, 4045, 1), -- Resist Full Magic Attack
-- Alberius
(7284, 4290, 1), -- Race
(7284, 4045, 1), -- Resist Full Magic Attack
-- Gartrandell
(7285, 4290, 1), -- Race
(7285, 4045, 1), -- Resist Full Magic Attack
-- trash
(7286, 4290, 1), -- Race
(7286, 4045, 1), -- Resist Full Magic Attack
-- trash
(7287, 4290, 1), -- Race
(7287, 4045, 1), -- Resist Full Magic Attack
-- Rains
(7288, 4290, 1), -- Race
(7288, 4045, 1), -- Resist Full Magic Attack
-- Raymond
(7289, 4290, 1), -- Race
(7289, 4045, 1), -- Resist Full Magic Attack
-- Xenos
(7290, 4290, 1), -- Race
(7290, 4045, 1), -- Resist Full Magic Attack
-- Alex
(7291, 4290, 1), -- Race
(7291, 4045, 1), -- Resist Full Magic Attack
-- trash
(7292, 4290, 1), -- Race
(7292, 4045, 1), -- Resist Full Magic Attack
-- Manuel
(7293, 4290, 1), -- Race
(7293, 4045, 1), -- Resist Full Magic Attack
--  Varan
(7294, 4290, 1), -- Race
(7294, 4045, 1), -- Resist Full Magic Attack
-- trash
(7295, 4290, 1), -- Race
(7295, 4045, 1), -- Resist Full Magic Attack
-- trash
(7296, 4290, 1), -- Race
(7296, 4045, 1), -- Resist Full Magic Attack
-- Tobias
(7297, 4290, 1), -- Race
(7297, 4045, 1), -- Resist Full Magic Attack
-- Pinter
(7298, 4290, 1), -- Race
(7298, 4045, 1), -- Resist Full Magic Attack
-- trash
(7299, 4290, 1), -- Race
(7299, 4045, 1), -- Resist Full Magic Attack
-- Pushkin
(7300, 4290, 1), -- Race
(7300, 4045, 1), -- Resist Full Magic Attack
-- Hally
(7301, 4290, 1), -- Race
(7301, 4045, 1), -- Resist Full Magic Attack
-- trash
(7302, 4290, 1), -- Race
(7302, 4045, 1), -- Resist Full Magic Attack
-- trash
(7303, 4290, 1), -- Race
(7303, 4045, 1), -- Resist Full Magic Attack
-- trash
(7304, 4290, 1), -- Race
(7304, 4045, 1), -- Resist Full Magic Attack
--  Vellior
(7305, 4290, 1), -- Race
(7305, 4045, 1), -- Resist Full Magic Attack
-- trash
(7306, 4290, 1), -- Race
(7306, 4045, 1), -- Resist Full Magic Attack
-- Karrod
(7307, 4290, 1), -- Race
(7307, 4045, 1), -- Resist Full Magic Attack
-- trash
(7308, 4290, 1), -- Race
(7308, 4045, 1), -- Resist Full Magic Attack
-- trash
(7309, 4290, 1), -- Race
(7309, 4045, 1), -- Resist Full Magic Attack
-- trash
(7310, 4290, 1), -- Race
(7310, 4045, 1), -- Resist Full Magic Attack
-- Sir Collin Windawood
(7311, 4290, 1), -- Race
(7311, 4045, 1), -- Resist Full Magic Attack
-- Rockswell
(7312, 4290, 1), -- Race
(7312, 4045, 1), -- Resist Full Magic Attack
--  Asha
(7313, 4290, 1), -- Race
(7313, 4045, 1), -- Resist Full Magic Attack
-- Nestle
(7314, 4290, 1), -- Race
(7314, 4045, 1), -- Resist Full Magic Attack
-- Poesia
(7315, 4290, 1), -- Race
(7315, 4045, 1), -- Resist Full Magic Attack
-- Raut
(7316, 4290, 1), -- Race
(7316, 4045, 1), -- Resist Full Magic Attack
-- Kluto
(7317, 4290, 1), -- Race
(7317, 4045, 1), -- Resist Full Magic Attack
-- trash
(7318, 4290, 1), -- Race
(7318, 4045, 1), -- Resist Full Magic Attack
-- trash
(7319, 4290, 1), -- Race
(7319, 4045, 1), -- Resist Full Magic Attack
-- Richlin
(7320, 4290, 1), -- Race
(7320, 4045, 1), -- Resist Full Magic Attack
-- Sydnia
(7321, 4290, 1), -- Race
(7321, 4045, 1), -- Resist Full Magic Attack
-- Ballin
(7322, 4290, 1), -- Race
(7322, 4045, 1), -- Resist Full Magic Attack
-- trash
(7323, 4290, 1), -- Race
(7323, 4045, 1), -- Resist Full Magic Attack
-- trash
(7324, 4290, 1), -- Race
(7324, 4045, 1), -- Resist Full Magic Attack
-- Audiberti
(7325, 4290, 1), -- Race
(7325, 4045, 1), -- Resist Full Magic Attack
-- Leona
(7326, 4290, 1), -- Race
(7326, 4045, 1), -- Resist Full Magic Attack
-- Sorius
(7327, 4290, 1), -- Race
(7327, 4045, 1), -- Resist Full Magic Attack
-- Reisa
(7328, 4290, 1), -- Race
(7328, 4045, 1), -- Resist Full Magic Attack
-- Virgil
(7329, 4290, 1), -- Race
(7329, 4045, 1), -- Resist Full Magic Attack
-- Sidra
(7330, 4290, 1), -- Race
(7330, 4045, 1), -- Resist Full Magic Attack
-- Thoma
(7331, 4290, 1), -- Race
(7331, 4045, 1), -- Resist Full Magic Attack
-- Bathis
(7332, 4290, 1), -- Race
(7332, 4045, 1), -- Resist Full Magic Attack
-- Praga
(7333, 4290, 1), -- Race
(7333, 4045, 1), -- Resist Full Magic Attack
-- Babenco
(7334, 4290, 1), -- Race
(7334, 4045, 1), -- Resist Full Magic Attack
-- Brynn
(7335, 4290, 1), -- Race
(7335, 4045, 1), -- Resist Full Magic Attack
-- Curtis
(7336, 4290, 1), -- Race
(7336, 4045, 1), -- Resist Full Magic Attack
-- Moretti
(7337, 4290, 1), -- Race
(7337, 4045, 1), -- Resist Full Magic Attack
-- Melville
(7338, 4290, 1), -- Race
(7338, 4045, 1), -- Resist Full Magic Attack
-- trash
(7339, 4290, 1), -- Race
(7339, 4045, 1), -- Resist Full Magic Attack
-- trash
(7340, 4290, 1), -- Race
(7340, 4045, 1), -- Resist Full Magic Attack
-- trash
(7341, 4290, 1), -- Race
(7341, 4045, 1), -- Resist Full Magic Attack
-- Varsak
(7342, 4290, 1), -- Race
(7342, 4045, 1), -- Resist Full Magic Attack
-- Emma
(7343, 4290, 1), -- Race
(7343, 4045, 1), -- Resist Full Magic Attack
-- Rohmer
(7344, 4290, 1), -- Race
(7344, 4045, 1), -- Resist Full Magic Attack
-- Ramoniell
(7345, 4290, 1), -- Race
(7345, 4045, 1), -- Resist Full Magic Attack
-- Kayleen
(7346, 4290, 1), -- Race
(7346, 4045, 1), -- Resist Full Magic Attack
-- Marion
(7347, 4290, 1), -- Race
(7347, 4045, 1), -- Resist Full Magic Attack
-- Nelsya
(7348, 4290, 1), -- Race
(7348, 4045, 1), -- Resist Full Magic Attack
-- Jenna
(7349, 4290, 1), -- Race
(7349, 4045, 1), -- Resist Full Magic Attack
-- Collette
(7350, 4290, 1), -- Race
(7350, 4045, 1), -- Resist Full Magic Attack
-- Astaron
(7351, 4290, 1), -- Race
(7351, 4045, 1), -- Resist Full Magic Attack
-- Karina
(7352, 4290, 1), -- Race
(7352, 4045, 1), -- Resist Full Magic Attack
-- Jughead
(7353, 4290, 1), -- Race
(7353, 4045, 1), -- Resist Full Magic Attack
-- Jewel
(7354, 4290, 1), -- Race
(7354, 4045, 1), -- Resist Full Magic Attack
-- Roselyn
(7355, 4290, 1), -- Race
(7355, 4045, 1), -- Resist Full Magic Attack
-- Altima
(7356, 4290, 1), -- Race
(7356, 4045, 1), -- Resist Full Magic Attack
-- Kristin
(7357, 4290, 1), -- Race
(7357, 4045, 1), -- Resist Full Magic Attack
-- Thifiell
(7358, 4290, 1), -- Race
(7358, 4045, 1), -- Resist Full Magic Attack
-- Kaitar
(7359, 4290, 1), -- Race
(7359, 4045, 1), -- Resist Full Magic Attack
-- Harant
(7360, 4290, 1), -- Race
(7360, 4045, 1), -- Resist Full Magic Attack
-- Rizraell
(7361, 4290, 1), -- Race
(7361, 4045, 1), -- Resist Full Magic Attack
-- Andellia
(7362, 4290, 1), -- Race
(7362, 4045, 1), -- Resist Full Magic Attack
-- Aios
(7363, 4290, 1), -- Race
(7363, 4045, 1), -- Resist Full Magic Attack
-- trash
(7364, 4290, 1), -- Race
(7364, 4045, 1), -- Resist Full Magic Attack
-- trash
(7365, 4290, 1), -- Race
(7365, 4045, 1), -- Resist Full Magic Attack
-- trash
(7366, 4290, 1), -- Race
(7366, 4045, 1), -- Resist Full Magic Attack
-- trash
(7367, 4290, 1), -- Race
(7367, 4045, 1), -- Resist Full Magic Attack
-- Lilith
(7368, 4290, 1), -- Race
(7368, 4045, 1), -- Resist Full Magic Attack
-- Baenedes
(7369, 4290, 1), -- Race
(7369, 4045, 1), -- Resist Full Magic Attack
-- Nerupa
(7370, 4301, 1), -- Race
(7370, 4045, 1), -- Resist Full Magic Attack
-- Thalia
(7371, 4290, 1), -- Race
(7371, 4045, 1), -- Resist Full Magic Attack
-- trash
(7372, 4290, 1), -- Race
(7372, 4045, 1), -- Resist Full Magic Attack
-- Ramos
(7373, 4290, 1), -- Race
(7373, 4045, 1), -- Resist Full Magic Attack
-- Rhodiell
(7374, 4290, 1), -- Race
(7374, 4045, 1), -- Resist Full Magic Attack
-- Adonius
(7375, 4290, 1), -- Race
(7375, 4045, 1), -- Resist Full Magic Attack
-- Nell
(7376, 4290, 1), -- Race
(7376, 4045, 1), -- Resist Full Magic Attack
-- Talbot
(7377, 4290, 1), -- Race
(7377, 4045, 1), -- Resist Full Magic Attack
-- Estella
(7378, 4290, 1), -- Race
(7378, 4045, 1), -- Resist Full Magic Attack
-- Bezique
(7379, 4290, 1), -- Race
(7379, 4045, 1), -- Resist Full Magic Attack
-- Plink
(7380, 4290, 1), -- Race
(7380, 4045, 1), -- Resist Full Magic Attack
-- Alvah
(7381, 4290, 1), -- Race
(7381, 4045, 1), -- Resist Full Magic Attack
-- Leikan
(7382, 4290, 1), -- Race
(7382, 4045, 1), -- Resist Full Magic Attack
-- Scott
(7383, 4290, 1), -- Race
(7383, 4045, 1), -- Resist Full Magic Attack
-- Linus
(7384, 4290, 1), -- Race
(7384, 4045, 1), -- Resist Full Magic Attack
-- Weisz
(7385, 4290, 1), -- Race
(7385, 4045, 1), -- Resist Full Magic Attack
-- Luis
(7386, 4290, 1), -- Race
(7386, 4045, 1), -- Resist Full Magic Attack
-- Clancy
(7387, 4290, 1), -- Race
(7387, 4045, 1), -- Resist Full Magic Attack
-- trash
(7388, 4290, 1), -- Race
(7388, 4045, 1), -- Resist Full Magic Attack
-- trash
(7389, 4290, 1), -- Race
(7389, 4045, 1), -- Resist Full Magic Attack
-- trash
(7390, 4290, 1), -- Race
(7390, 4045, 1), -- Resist Full Magic Attack
-- Parina
(7391, 4290, 1), -- Race
(7391, 4045, 1), -- Resist Full Magic Attack
-- trash
(7392, 4290, 1), -- Race
(7392, 4045, 1), -- Resist Full Magic Attack
-- trash
(7393, 4290, 1), -- Race
(7393, 4045, 1), -- Resist Full Magic Attack
-- trash
(7394, 4290, 1), -- Race
(7394, 4045, 1), -- Resist Full Magic Attack
-- trash
(7395, 4290, 1), -- Race
(7395, 4045, 1), -- Resist Full Magic Attack
-- trash
(7396, 4290, 1), -- Race
(7396, 4045, 1), -- Resist Full Magic Attack
-- trash
(7397, 4290, 1), -- Race
(7397, 4045, 1), -- Resist Full Magic Attack
-- trash
(7398, 4290, 1), -- Race
(7398, 4045, 1), -- Resist Full Magic Attack
-- trash
(7399, 4290, 1), -- Race
(7399, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7400, 4290, 1), -- Race
(7400, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7401, 4290, 1), -- Race
(7401, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7402, 4290, 1), -- Race
(7402, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7403, 4290, 1), -- Race
(7403, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7404, 4290, 1), -- Race
(7404, 4045, 1), -- Resist Full Magic Attack
-- Marius
(7405, 4290, 1), -- Race
(7405, 4045, 1), -- Resist Full Magic Attack
-- Matheo
(7406, 4290, 1), -- Race
(7406, 4045, 1), -- Resist Full Magic Attack
-- Mesella
(7407, 4290, 1), -- Race
(7407, 4045, 1), -- Resist Full Magic Attack
-- Lionel
(7408, 4290, 1), -- Race
(7408, 4045, 1), -- Resist Full Magic Attack
-- Earth Snake
(7409, 4292, 1), -- Race
(7409, 4045, 1), -- Resist Full Magic Attack
-- Lizardman Of The Wasteland
(7410, 4295, 1), -- Race
(7410, 4045, 1), -- Resist Full Magic Attack
-- Flame Salamander
(7411, 4296, 1), -- Race
(7411, 4045, 1), -- Resist Full Magic Attack
-- Wind Sylph
(7412, 4296, 1), -- Race
(7412, 4045, 1), -- Resist Full Magic Attack
-- Water Undine
(7413, 4296, 1), -- Race
(7413, 4045, 1), -- Resist Full Magic Attack
-- Rosella
(7414, 4290, 1), -- Race
(7414, 4045, 1), -- Resist Full Magic Attack
-- Charkeren
(7415, 4290, 1), -- Race
(7415, 4045, 1), -- Resist Full Magic Attack
-- Triskel
(7416, 4290, 1), -- Race
(7416, 4045, 1), -- Resist Full Magic Attack
-- Sir Klaus Vasper
(7417, 4290, 1), -- Race
(7417, 4045, 1), -- Resist Full Magic Attack
-- Annika
(7418, 4290, 1), -- Race
(7418, 4045, 1), -- Resist Full Magic Attack
-- Arkenia
(7419, 4290, 1), -- Race
(7419, 4045, 1), -- Resist Full Magic Attack
-- Tyra
(7420, 4290, 1), -- Race
(7420, 4045, 1), -- Resist Full Magic Attack
-- Varika
(7421, 4290, 1), -- Race
(7421, 4045, 1), -- Resist Full Magic Attack
-- Kalinta
(7422, 4290, 1), -- Race
(7422, 4045, 1), -- Resist Full Magic Attack
-- Northwind
(7423, 4290, 1), -- Race
(7423, 4045, 1), -- Resist Full Magic Attack
-- Allana
(7424, 4290, 1), -- Race
(7424, 4045, 1), -- Resist Full Magic Attack
-- Neti
(7425, 4290, 1), -- Race
(7425, 4045, 1), -- Resist Full Magic Attack
-- Prias
(7426, 4290, 1), -- Race
(7426, 4045, 1), -- Resist Full Magic Attack
-- Siff
(7427, 4290, 1), -- Race
(7427, 4045, 1), -- Resist Full Magic Attack
-- Perrin
(7428, 4290, 1), -- Race
(7428, 4045, 1), -- Resist Full Magic Attack
-- Tiramisa
(7429, 4290, 1), -- Race
(7429, 4045, 1), -- Resist Full Magic Attack
-- Trionell
(7430, 4290, 1), -- Race
(7430, 4045, 1), -- Resist Full Magic Attack
-- Eriel
(7431, 4290, 1), -- Race
(7431, 4045, 1), -- Resist Full Magic Attack
-- Irene
(7432, 4290, 1), -- Race
(7432, 4045, 1), -- Resist Full Magic Attack
-- Kathaway
(7433, 4290, 1), -- Race
(7433, 4045, 1), -- Resist Full Magic Attack
-- Samed
(7434, 4290, 1), -- Race
(7434, 4045, 1), -- Resist Full Magic Attack
-- Leopold
(7435, 4290, 1), -- Race
(7435, 4045, 1), -- Resist Full Magic Attack
-- Sarien
(7436, 4290, 1), -- Race
(7436, 4045, 1), -- Resist Full Magic Attack
-- Rolento
(7437, 4290, 1), -- Race
(7437, 4045, 1), -- Resist Full Magic Attack
-- trash
(7438, 4295, 1), -- Race
(7438, 4045, 1), -- Resist Full Magic Attack
-- trash
(7439, 4290, 1), -- Race
(7439, 4045, 1), -- Resist Full Magic Attack
-- trash
(7440, 4291, 1), -- Race
(7440, 4045, 1), -- Resist Full Magic Attack
-- trash
(7441, 4290, 1), -- Race
(7441, 4045, 1), -- Resist Full Magic Attack
-- trash
(7442, 4290, 1), -- Race
(7442, 4045, 1), -- Resist Full Magic Attack
-- trash
(7443, 4290, 1), -- Race
(7443, 4045, 1), -- Resist Full Magic Attack
-- Duke Lewin Waldner
(7444, 4290, 1), -- Race
(7444, 4045, 1), -- Resist Full Magic Attack
-- Cronenberg
(7445, 4290, 1), -- Race
(7445, 4045, 1), -- Resist Full Magic Attack
-- trash
(7446, 4290, 1), -- Race
(7446, 4045, 1), -- Resist Full Magic Attack
-- trash
(7447, 4290, 1), -- Race
(7447, 4045, 1), -- Resist Full Magic Attack
-- trash
(7448, 4290, 1), -- Race
(7448, 4045, 1), -- Resist Full Magic Attack
-- trash
(7449, 4290, 1), -- Race
(7449, 4045, 1), -- Resist Full Magic Attack
-- trash
(7450, 4290, 1), -- Race
(7450, 4045, 1), -- Resist Full Magic Attack
-- trash
(7451, 4290, 1), -- Race
(7451, 4045, 1), -- Resist Full Magic Attack
-- Kurt
(7452, 4290, 1), -- Race
(7452, 4045, 1), -- Resist Full Magic Attack
-- trash
(7453, 4290, 1), -- Race
(7453, 4045, 1), -- Resist Full Magic Attack
-- trash
(7454, 4290, 1), -- Race
(7454, 4045, 1), -- Resist Full Magic Attack
-- trash
(7455, 4290, 1), -- Race
(7455, 4045, 1), -- Resist Full Magic Attack
-- trash
(7456, 4290, 1), -- Race
(7456, 4045, 1), -- Resist Full Magic Attack
-- trash
(7457, 4290, 1), -- Race
(7457, 4045, 1), -- Resist Full Magic Attack
-- Poitan
(7458, 4290, 1), -- Race
(7458, 4045, 1), -- Resist Full Magic Attack
-- Wandius
(7459, 4290, 1), -- Race
(7459, 4045, 1), -- Resist Full Magic Attack
-- Cardien
(7460, 4290, 1), -- Race
(7460, 4045, 1), -- Resist Full Magic Attack
-- Mirien 
(7461, 4290, 1), -- Race
(7461, 4045, 1), -- Resist Full Magic Attack
-- Tronix
(7462, 4290, 1), -- Race
(7462, 4045, 1), -- Resist Full Magic Attack
-- Ixia
(7463, 4290, 1), -- Race
(7463, 4045, 1), -- Resist Full Magic Attack
-- Clayton
(7464, 4290, 1), -- Race
(7464, 4045, 1), -- Resist Full Magic Attack
-- Herven
(7465, 4290, 1), -- Race
(7465, 4045, 1), -- Resist Full Magic Attack
-- Bright
(7466, 4290, 1), -- Race
(7466, 4045, 1), -- Resist Full Magic Attack
-- trash
(7467, 4290, 1), -- Race
(7467, 4045, 1), -- Resist Full Magic Attack
-- trash
(7468, 4290, 1), -- Race
(7468, 4045, 1), -- Resist Full Magic Attack
-- Jonas
(7469, 4290, 1), -- Race
(7469, 4045, 1), -- Resist Full Magic Attack
-- trash
(7470, 4290, 1), -- Race
(7470, 4045, 1), -- Resist Full Magic Attack
-- Rupio
(7471, 4290, 1), -- Race
(7471, 4045, 1), -- Resist Full Magic Attack
-- Rosheria
(7472, 4290, 1), -- Race
(7472, 4045, 1), -- Resist Full Magic Attack
-- Bandellos
(7473, 4290, 1), -- Race
(7473, 4045, 1), -- Resist Full Magic Attack
-- Angus
(7474, 4290, 1), -- Race
(7474, 4045, 1), -- Resist Full Magic Attack
-- Stapin
(7475, 4290, 1), -- Race
(7475, 4045, 1), -- Resist Full Magic Attack
-- Kaira
(7476, 4290, 1), -- Race
(7476, 4045, 1), -- Resist Full Magic Attack
-- Sir Ortho Lancer
(7477, 4290, 1), -- Race
(7477, 4045, 1), -- Resist Full Magic Attack
-- Reikin
(7478, 4290, 1), -- Race
(7478, 4045, 1), -- Resist Full Magic Attack
-- trash
(7479, 4290, 1), -- Race
(7479, 4045, 1), -- Resist Full Magic Attack
-- trash
(7480, 4290, 1), -- Race
(7480, 4045, 1), -- Resist Full Magic Attack
-- trash
(7481, 4290, 1), -- Race
(7481, 4045, 1), -- Resist Full Magic Attack
-- trash
(7482, 4290, 1), -- Race
(7482, 4045, 1), -- Resist Full Magic Attack
-- Mozella
(7483, 4290, 1), -- Race
(7483, 4045, 1), -- Resist Full Magic Attack
-- Ponti
(7484, 4290, 1), -- Race
(7484, 4045, 1), -- Resist Full Magic Attack
-- Capella
(7485, 4290, 1), -- Race
(7485, 4045, 1), -- Resist Full Magic Attack
-- Hanna
(7486, 4290, 1), -- Race
(7486, 4045, 1), -- Resist Full Magic Attack
-- Penelope
(7487, 4290, 1), -- Race
(7487, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(7488, 4290, 1), -- Race
(7488, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(7489, 4290, 1), -- Race
(7489, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(7490, 4290, 1), -- Race
(7490, 4045, 1), -- Resist Full Magic Attack
-- Mass Gatekeeper
(7491, 4290, 1), -- Race
(7491, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(7492, 4290, 1), -- Race
(7492, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(7493, 4290, 1), -- Race
(7493, 4045, 1), -- Resist Full Magic Attack
-- Brighum
(7494, 4290, 1), -- Race
(7494, 4045, 1), -- Resist Full Magic Attack
-- Tamutak
(7495, 4295, 1), -- Race
(7495, 4045, 1), -- Resist Full Magic Attack
-- Brakel
(7496, 4295, 1), -- Race
(7496, 4045, 1), -- Resist Full Magic Attack
-- Edmond
(7497, 4290, 1), -- Race
(7497, 4045, 1), -- Resist Full Magic Attack
-- Moke
(7498, 4290, 1), -- Race
(7498, 4045, 1), -- Resist Full Magic Attack
-- Tapoy
(7499, 4290, 1), -- Race
(7499, 4045, 1), -- Resist Full Magic Attack
-- Osborn
(7500, 4290, 1), -- Race
(7500, 4045, 1), -- Resist Full Magic Attack
-- Kasman
(7501, 4290, 1), -- Race
(7501, 4045, 1), -- Resist Full Magic Attack
-- Umos
(7502, 4290, 1), -- Race
(7502, 4045, 1), -- Resist Full Magic Attack
-- Rikadio
(7503, 4290, 1), -- Race
(7503, 4045, 1), -- Resist Full Magic Attack
-- Mendio
(7504, 4290, 1), -- Race
(7504, 4045, 1), -- Resist Full Magic Attack
-- Drikus
(7505, 4290, 1), -- Race
(7505, 4045, 1), -- Resist Full Magic Attack
-- Buka
(7506, 4290, 1), -- Race
(7506, 4045, 1), -- Resist Full Magic Attack
-- Racoy
(7507, 4290, 1), -- Race
(7507, 4045, 1), -- Resist Full Magic Attack
-- Castor
(7508, 4290, 1), -- Race
(7508, 4045, 1), -- Resist Full Magic Attack
-- Dowki
(7509, 4290, 1), -- Race
(7509, 4045, 1), -- Resist Full Magic Attack
-- Somak
(7510, 4290, 1), -- Race
(7510, 4045, 1), -- Resist Full Magic Attack
-- Gesto
(7511, 4290, 1), -- Race
(7511, 4045, 1), -- Resist Full Magic Attack
-- Kusto
(7512, 4290, 1), -- Race
(7512, 4045, 1), -- Resist Full Magic Attack
-- Penatus
(7513, 4290, 1), -- Race
(7513, 4045, 1), -- Resist Full Magic Attack
-- Vokian
(7514, 4290, 1), -- Race
(7514, 4045, 1), -- Resist Full Magic Attack
-- Manakia
(7515, 4290, 1), -- Race
(7515, 4045, 1), -- Resist Full Magic Attack
-- Reep
(7516, 4290, 1), -- Race
(7516, 4045, 1), -- Resist Full Magic Attack
-- Shari
(7517, 4290, 1), -- Race
(7517, 4045, 1), -- Resist Full Magic Attack
-- Garita
(7518, 4290, 1), -- Race
(7518, 4045, 1), -- Resist Full Magic Attack
-- Mion
(7519, 4290, 1), -- Race
(7519, 4045, 1), -- Resist Full Magic Attack
-- Reed
(7520, 4290, 1), -- Race
(7520, 4045, 1), -- Resist Full Magic Attack
-- Murdoc
(7521, 4290, 1), -- Race
(7521, 4045, 1), -- Resist Full Magic Attack
-- Airy
(7522, 4290, 1), -- Race
(7522, 4045, 1), -- Resist Full Magic Attack
-- Gouph
(7523, 4290, 1), -- Race
(7523, 4045, 1), -- Resist Full Magic Attack
-- Pippi
(7524, 4290, 1), -- Race
(7524, 4045, 1), -- Resist Full Magic Attack
-- Bronk
(7525, 4290, 1), -- Race
(7525, 4045, 1), -- Resist Full Magic Attack
-- Brunon
(7526, 4290, 1), -- Race
(7526, 4045, 1), -- Resist Full Magic Attack
-- Silvera
(7527, 4290, 1), -- Race
(7527, 4045, 1), -- Resist Full Magic Attack
-- Laferon
(7528, 4290, 1), -- Race
(7528, 4045, 1), -- Resist Full Magic Attack
-- Maron
(7529, 4290, 1), -- Race
(7529, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7530, 4290, 1), -- Race
(7530, 4045, 1), -- Resist Full Magic Attack
-- Lockirin
(7531, 4290, 1), -- Race
(7531, 4045, 1), -- Resist Full Magic Attack
-- Spiron
(7532, 4290, 1), -- Race
(7532, 4045, 1), -- Resist Full Magic Attack
-- Balanki
(7533, 4290, 1), -- Race
(7533, 4045, 1), -- Resist Full Magic Attack
-- Keef
(7534, 4290, 1), -- Race
(7534, 4045, 1), -- Resist Full Magic Attack
-- Filaur
(7535, 4290, 1), -- Race
(7535, 4045, 1), -- Resist Full Magic Attack
-- Arin
(7536, 4290, 1), -- Race
(7536, 4045, 1), -- Resist Full Magic Attack
-- Daichir
(7537, 4290, 1), -- Race
(7537, 4045, 1), -- Resist Full Magic Attack
-- Zimenf
(7538, 4290, 1), -- Race
(7538, 4045, 1), -- Resist Full Magic Attack
-- Chichirin
(7539, 4290, 1), -- Race
(7539, 4045, 1), -- Resist Full Magic Attack
-- Wirphy
(7540, 4290, 1), -- Race
(7540, 4045, 1), -- Resist Full Magic Attack
-- Paion
(7541, 4290, 1), -- Race
(7541, 4045, 1), -- Resist Full Magic Attack
-- Runant
(7542, 4290, 1), -- Race
(7542, 4045, 1), -- Resist Full Magic Attack
-- Ethan
(7543, 4290, 1), -- Race
(7543, 4045, 1), -- Resist Full Magic Attack
-- Cromwell
(7544, 4290, 1), -- Race
(7544, 4045, 1), -- Resist Full Magic Attack
-- Proton
(7545, 4290, 1), -- Race
(7545, 4045, 1), -- Resist Full Magic Attack
-- Dinkey
(7546, 4290, 1), -- Race
(7546, 4045, 1), -- Resist Full Magic Attack
-- Tardyon
(7547, 4290, 1), -- Race
(7547, 4045, 1), -- Resist Full Magic Attack
-- Nathan
(7548, 4290, 1), -- Race
(7548, 4045, 1), -- Resist Full Magic Attack
-- Ghouliff Droopstone
(7549, 4290, 1), -- Race
(7549, 4045, 1), -- Resist Full Magic Attack
-- Gauri Twinklerock
(7550, 4290, 1), -- Race
(7550, 4045, 1), -- Resist Full Magic Attack
-- Tink Wandergold
(7551, 4290, 1), -- Race
(7551, 4045, 1), -- Resist Full Magic Attack
-- Kiril Sparkystone
(7552, 4290, 1), -- Race
(7552, 4045, 1), -- Resist Full Magic Attack
-- Maryse Redbonnet
(7553, 4290, 1), -- Race
(7553, 4045, 1), -- Resist Full Magic Attack
-- Bolter
(7554, 4290, 1), -- Race
(7554, 4045, 1), -- Resist Full Magic Attack
-- Torocco
(7555, 4290, 1), -- Race
(7555, 4045, 1), -- Resist Full Magic Attack
-- Toma
(7556, 4290, 1), -- Race
(7556, 4045, 1), -- Resist Full Magic Attack
-- Torai
(7557, 4290, 1), -- Race
(7557, 4045, 1), -- Resist Full Magic Attack
-- Jakal
(7558, 4290, 1), -- Race
(7558, 4045, 1), -- Resist Full Magic Attack
-- Kunai
(7559, 4290, 1), -- Race
(7559, 4045, 1), -- Resist Full Magic Attack
-- Uska
(7560, 4290, 1), -- Race
(7560, 4045, 1), -- Resist Full Magic Attack
-- Papuma
(7561, 4290, 1), -- Race
(7561, 4045, 1), -- Resist Full Magic Attack
-- Grookin
(7562, 4290, 1), -- Race
(7562, 4045, 1), -- Resist Full Magic Attack
-- Imantu
(7563, 4290, 1), -- Race
(7563, 4045, 1), -- Resist Full Magic Attack
-- Sumari
(7564, 4290, 1), -- Race
(7564, 4045, 1), -- Resist Full Magic Attack
-- Kakai
(7565, 4290, 1), -- Race
(7565, 4045, 1), -- Resist Full Magic Attack
-- Varkees
(7566, 4290, 1), -- Race
(7566, 4045, 1), -- Resist Full Magic Attack
-- Tantus
(7567, 4290, 1), -- Race
(7567, 4045, 1), -- Resist Full Magic Attack
-- Hatos
(7568, 4290, 1), -- Race
(7568, 4045, 1), -- Resist Full Magic Attack
-- Brukurse
(7569, 4290, 1), -- Race
(7569, 4045, 1), -- Resist Full Magic Attack
-- Karukia
(7570, 4290, 1), -- Race
(7570, 4045, 1), -- Resist Full Magic Attack
-- Tanapi
(7571, 4290, 1), -- Race
(7571, 4045, 1), -- Resist Full Magic Attack
-- Livina
(7572, 4290, 1), -- Race
(7572, 4045, 1), -- Resist Full Magic Attack
-- Vulkus
(7573, 4290, 1), -- Race
(7573, 4045, 1), -- Resist Full Magic Attack
-- Trash
(7574, 4290, 1), -- Race
(7574, 4045, 1), -- Resist Full Magic Attack
-- Newbie Helper
(7575, 4290, 1), -- Race
(7575, 4045, 1), -- Resist Full Magic Attack
-- Tamil
(7576, 4290, 1), -- Race
(7576, 4045, 1), -- Resist Full Magic Attack
-- Rukain
(7577, 4290, 1), -- Race
(7577, 4045, 1), -- Resist Full Magic Attack
-- Nakusin
(7578, 4290, 1), -- Race
(7578, 4045, 1), -- Resist Full Magic Attack
-- Tamai
(7579, 4290, 1), -- Race
(7579, 4045, 1), -- Resist Full Magic Attack
-- Parugon
(7580, 4290, 1), -- Race
(7580, 4045, 1), -- Resist Full Magic Attack
-- Orinak
(7581, 4290, 1), -- Race
(7581, 4045, 1), -- Resist Full Magic Attack
-- Tiku
(7582, 4290, 1), -- Race
(7582, 4045, 1), -- Resist Full Magic Attack
-- Petukai
(7583, 4290, 1), -- Race
(7583, 4045, 1), -- Resist Full Magic Attack
-- Vapook
(7584, 4290, 1), -- Race
(7584, 4045, 1), -- Resist Full Magic Attack
-- Tataru Zu Hestui
(7585, 4290, 1), -- Race
(7585, 4045, 1), -- Resist Full Magic Attack
-- Anai Zu Neruga
(7586, 4290, 1), -- Race
(7586, 4045, 1), -- Resist Full Magic Attack
-- Gantaki Zu Urutu
(7587, 4290, 1), -- Race
(7587, 4045, 1), -- Resist Full Magic Attack
-- Takia Zu Duda-Mara
(7588, 4290, 1), -- Race
(7588, 4045, 1), -- Resist Full Magic Attack
-- Kazkin Zu Gandi
(7589, 4290, 1), -- Race
(7589, 4045, 1), -- Resist Full Magic Attack
-- Rosheek
(7590, 4290, 1), -- Race
(7590, 4045, 1), -- Resist Full Magic Attack
-- Toruku
(7591, 4290, 1), -- Race
(7591, 4045, 1), -- Resist Full Magic Attack
-- Hestui Totem Spirit
(7592, 4293, 1), -- Race
(7592, 4045, 1), -- Resist Full Magic Attack
-- Duda-Mara Totem Spirit
(7593, 4301, 1), -- Race
(7593, 4045, 1), -- Resist Full Magic Attack
-- Ranspo
(7594, 4290, 1), -- Race
(7594, 4045, 1), -- Resist Full Magic Attack
-- Opix
(7595, 4290, 1), -- Race
(7595, 4045, 1), -- Resist Full Magic Attack
-- Harkel
(7596, 4295, 1), -- Race
(7596, 4045, 1), -- Resist Full Magic Attack
-- Piotur
(7597, 4290, 1), -- Race
(7597, 4045, 1), -- Resist Full Magic Attack
-- Newbie Guide
(7598, 4290, 1), -- Race
(7598, 4045, 1), -- Resist Full Magic Attack
-- Newbie Guide
(7599, 4290, 1), -- Race
(7599, 4045, 1), -- Resist Full Magic Attack
-- Newbie Guide
(7600, 4290, 1), -- Race
(7600, 4045, 1), -- Resist Full Magic Attack
-- Newbie Guide
(7601, 4290, 1), -- Race
(7601, 4045, 1), -- Resist Full Magic Attack
-- Newbie Guide
(7602, 4290, 1), -- Race
(7602, 4045, 1), -- Resist Full Magic Attack
-- trash
(7603, 4290, 1), -- Race
(7603, 4045, 1), -- Resist Full Magic Attack
-- trash
(7604, 4290, 1), -- Race
(7604, 4045, 1), -- Resist Full Magic Attack
-- Temporary Teleporter
(7605, 4290, 1), -- Race
(7605, 4045, 1), -- Resist Full Magic Attack
-- trash
(7606, 4290, 1), -- Race
(7606, 4045, 1), -- Resist Full Magic Attack
-- trash
(7607, 4290, 1), -- Race
(7607, 4045, 1), -- Resist Full Magic Attack
-- Maria
(7608, 4290, 1), -- Race
(7608, 4045, 1), -- Resist Full Magic Attack
-- Creta
(7609, 4290, 1), -- Race
(7609, 4045, 1), -- Resist Full Magic Attack
-- Cronos
(7610, 4290, 1), -- Race
(7610, 4045, 1), -- Resist Full Magic Attack
-- Triff
(7611, 4290, 1), -- Race
(7611, 4045, 1), -- Resist Full Magic Attack
-- Casian
(7612, 4290, 1), -- Race
(7612, 4045, 1), -- Resist Full Magic Attack
-- Alders Spirit
(7613, 4290, 1), -- Race
(7613, 4045, 1), -- Resist Full Magic Attack
-- Metheus
(7614, 4290, 1), -- Race
(7614, 4045, 1), -- Resist Full Magic Attack
-- Voltar
(7615, 4295, 1), -- Race
(7615, 4045, 1), -- Resist Full Magic Attack
-- Kepra
(7616, 4295, 1), -- Race
(7616, 4045, 1), -- Resist Full Magic Attack
-- Burai
(7617, 4295, 1), -- Race
(7617, 4045, 1), -- Resist Full Magic Attack
-- Harak
(7618, 4295, 1), -- Race
(7618, 4045, 1), -- Resist Full Magic Attack
-- Driko
(7619, 4295, 1), -- Race
(7619, 4045, 1), -- Resist Full Magic Attack
-- Emily
(7620, 4290, 1), -- Race
(7620, 4045, 1), -- Resist Full Magic Attack
-- Nikola
(7621, 4290, 1), -- Race
(7621, 4045, 1), -- Resist Full Magic Attack
-- Box Of Titan
(7622, 4290, 1), -- Race
(7622, 4045, 1), -- Resist Full Magic Attack
(7622, 4390, 1), -- NPC Abnormal Immunity
-- Kaien
(7623, 4290, 1), -- Race
(7623, 4045, 1), -- Resist Full Magic Attack
-- Ascalon
(7624, 4290, 1), -- Race
(7624, 4045, 1), -- Resist Full Magic Attack
-- Mason
(7625, 4290, 1), -- Race
(7625, 4045, 1), -- Resist Full Magic Attack
-- Hamil
(7626, 4290, 1), -- Race
(7626, 4045, 1), -- Resist Full Magic Attack
-- Tree
(7627, 4294, 1), -- Race
(7627, 4045, 1), -- Resist Full Magic Attack
(7627, 4390, 1), -- NPC Abnormal Immunity
-- Strong Wooden Chest
(7628, 4290, 1), -- Race
(7628, 4045, 1), -- Resist Full Magic Attack
(7628, 4390, 1), -- NPC Abnormal Immunity
-- Rukal
(7629, 4290, 1), -- Race
(7629, 4045, 1), -- Resist Full Magic Attack
-- Orim of the Shadow
(7630, 4290, 1), -- Race
(7630, 4045, 1), -- Resist Full Magic Attack
-- Roderik
(7631, 4290, 1), -- Race
(7631, 4045, 1), -- Resist Full Magic Attack
-- Endrigo
(7632, 4290, 1), -- Race
(7632, 4045, 1), -- Resist Full Magic Attack
-- Evert
(7633, 4290, 1), -- Race
(7633, 4045, 1), -- Resist Full Magic Attack
-- Galatea
(7634, 4290, 1), -- Race
(7634, 4045, 1), -- Resist Full Magic Attack
-- Almors
(7635, 4290, 1), -- Race
(7635, 4045, 1), -- Resist Full Magic Attack
-- Camoniell
(7636, 4290, 1), -- Race
(7636, 4045, 1), -- Resist Full Magic Attack
-- Belthus
(7637, 4290, 1), -- Race
(7637, 4045, 1), -- Resist Full Magic Attack
-- Basilla
(7638, 4290, 1), -- Race
(7638, 4045, 1), -- Resist Full Magic Attack
-- Celestiel
(7639, 4290, 1), -- Race
(7639, 4045, 1), -- Resist Full Magic Attack
-- Brynthea
(7640, 4290, 1), -- Race
(7640, 4045, 1), -- Resist Full Magic Attack
-- Takuna
(7641, 4290, 1), -- Race
(7641, 4045, 1), -- Resist Full Magic Attack
-- Chianta
(7642, 4290, 1), -- Race
(7642, 4045, 1), -- Resist Full Magic Attack
-- First Orc
(7643, 4290, 1), -- Race
(7643, 4045, 1), -- Resist Full Magic Attack
-- Kash
(7644, 4290, 1), -- Race
(7644, 4045, 1), -- Resist Full Magic Attack
-- Martien
(7645, 4290, 1), -- Race
(7645, 4045, 1), -- Resist Full Magic Attack
-- Raldo
(7646, 4290, 1), -- Race
(7646, 4045, 1), -- Resist Full Magic Attack
-- Chest Of Shyslassys
(7647, 4290, 1), -- Race
(7647, 4045, 1), -- Resist Full Magic Attack
(7647, 4390, 1), -- NPC Abnormal Immunity
-- Santiago
(7648, 4290, 1), -- Race
(7648, 4045, 1), -- Resist Full Magic Attack
-- Ancestor Martankus
(7649, 4290, 1), -- Race
(7649, 4045, 1), -- Resist Full Magic Attack
-- Geraldine
(7650, 4290, 1), -- Race
(7650, 4045, 1), -- Resist Full Magic Attack
-- Dorf
(7651, 4290, 1), -- Race
(7651, 4045, 1), -- Resist Full Magic Attack
-- Uruha
(7652, 4290, 1), -- Race
(7652, 4045, 1), -- Resist Full Magic Attack
-- Sir Aron Tanford
(7653, 4290, 1), -- Race
(7653, 4045, 1), -- Resist Full Magic Attack
-- Sir Kiel Nighthawk
(7654, 4290, 1), -- Race
(7654, 4045, 1), -- Resist Full Magic Attack
-- Isael Silvershadow
(7655, 4290, 1), -- Race
(7655, 4045, 1), -- Resist Full Magic Attack
-- Spirit Of Sir Talianus
(7656, 4290, 1), -- Race
(7656, 4045, 1), -- Resist Full Magic Attack
-- Seresin
(7657, 4290, 1), -- Race
(7657, 4045, 1), -- Resist Full Magic Attack
-- Gupu
(7658, 4290, 1), -- Race
(7658, 4045, 1), -- Resist Full Magic Attack
-- Orphan Girl
(7659, 4290, 1), -- Race
(7659, 4045, 1), -- Resist Full Magic Attack
-- Windy Shaoring
(7660, 4290, 1), -- Race
(7660, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Dark Elf
(7661, 4290, 1), -- Race
(7661, 4045, 1), -- Resist Full Magic Attack
-- Piper Longbow
(7662, 4290, 1), -- Race
(7662, 4045, 1), -- Resist Full Magic Attack
-- Slein Shining Blade
(7663, 4290, 1), -- Race
(7663, 4045, 1), -- Resist Full Magic Attack
-- Kein Flying Knife
(7664, 4290, 1), -- Race
(7664, 4045, 1), -- Resist Full Magic Attack
-- Kristina
(7665, 4290, 1), -- Race
(7665, 4045, 1), -- Resist Full Magic Attack
-- Sla
(7666, 4290, 1), -- Race
(7666, 4045, 1), -- Resist Full Magic Attack
-- Ramus
(7667, 4290, 1), -- Race
(7667, 4045, 1), -- Resist Full Magic Attack
-- Katari
(7668, 4290, 1), -- Race
(7668, 4045, 1), -- Resist Full Magic Attack
-- Kakan
(7669, 4290, 1), -- Race
(7669, 4045, 1), -- Resist Full Magic Attack
-- Nyakuri
(7670, 4290, 1), -- Race
(7670, 4045, 1), -- Resist Full Magic Attack
-- Croto
(7671, 4290, 1), -- Race
(7671, 4045, 1), -- Resist Full Magic Attack
-- Dubabah
(7672, 4290, 1), -- Race
(7672, 4045, 1), -- Resist Full Magic Attack
-- Lorain
(7673, 4290, 1), -- Race
(7673, 4045, 1), -- Resist Full Magic Attack
-- Daurin Hammercrush
(7674, 4290, 1), -- Race
(7674, 4045, 1), -- Resist Full Magic Attack
-- Corpse Of Kamur
(7675, 4290, 1), -- Race
(7675, 4045, 1), -- Resist Full Magic Attack
-- Croop
(7676, 4290, 1), -- Race
(7676, 4045, 1), -- Resist Full Magic Attack
-- Flutter
(7677, 4290, 1), -- Race
(7677, 4045, 1), -- Resist Full Magic Attack
-- Helton
(7678, 4290, 1), -- Race
(7678, 4045, 1), -- Resist Full Magic Attack
-- Roameria
(7679, 4290, 1), -- Race
(7679, 4045, 1), -- Resist Full Magic Attack
-- Priest Egnos
(7680, 4290, 1), -- Race
(7680, 4045, 1), -- Resist Full Magic Attack
-- Karia
(7681, 4290, 1), -- Race
(7681, 4045, 1), -- Resist Full Magic Attack
-- Pekiron
(7682, 4290, 1), -- Race
(7682, 4045, 1), -- Resist Full Magic Attack
-- Daunt
(7683, 4290, 1), -- Race
(7683, 4045, 1), -- Resist Full Magic Attack
-- Viktor
(7684, 4290, 1), -- Race
(7684, 4045, 1), -- Resist Full Magic Attack
-- Baxt
(7685, 4290, 1), -- Race
(7685, 4045, 1), -- Resist Full Magic Attack
-- Silva
(7686, 4290, 1), -- Race
(7686, 4045, 1), -- Resist Full Magic Attack
-- Vergara
(7687, 4290, 1), -- Race
(7687, 4045, 1), -- Resist Full Magic Attack
-- Duning
(7688, 4290, 1), -- Race
(7688, 4045, 1), -- Resist Full Magic Attack
-- Siria
(7689, 4290, 1), -- Race
(7689, 4045, 1), -- Resist Full Magic Attack
-- Luther
(7690, 4290, 1), -- Race
(7690, 4045, 1), -- Resist Full Magic Attack
-- Aren Atebalt
(7691, 4290, 1), -- Race
(7691, 4045, 1), -- Resist Full Magic Attack
-- Stedmiel
(7692, 4290, 1), -- Race
(7692, 4045, 1), -- Resist Full Magic Attack
-- Queenien
(7693, 4290, 1), -- Race
(7693, 4045, 1), -- Resist Full Magic Attack
-- Scraide
(7694, 4290, 1), -- Race
(7694, 4045, 1), -- Resist Full Magic Attack
-- Moses
(7695, 4290, 1), -- Race
(7695, 4045, 1), -- Resist Full Magic Attack
-- Page
(7696, 4290, 1), -- Race
(7696, 4045, 1), -- Resist Full Magic Attack
-- Videlrien
(7697, 4290, 1), -- Race
(7697, 4045, 1), -- Resist Full Magic Attack
-- Evelyn
(7698, 4290, 1), -- Race
(7698, 4045, 1), -- Resist Full Magic Attack
-- Medown
(7699, 4290, 1), -- Race
(7699, 4045, 1), -- Resist Full Magic Attack
-- Prestan
(7700, 4290, 1), -- Race
(7700, 4045, 1), -- Resist Full Magic Attack
-- Errickin
(7701, 4290, 1), -- Race
(7701, 4045, 1), -- Resist Full Magic Attack
-- Bernard
(7702, 4290, 1), -- Race
(7702, 4045, 1), -- Resist Full Magic Attack
-- Colin
(7703, 4290, 1), -- Race
(7703, 4045, 1), -- Resist Full Magic Attack
-- Garvarentz
(7704, 4290, 1), -- Race
(7704, 4045, 1), -- Resist Full Magic Attack
-- Chakiris
(7705, 4290, 1), -- Race
(7705, 4045, 1), -- Resist Full Magic Attack
-- Lazenby
(7706, 4290, 1), -- Race
(7706, 4045, 1), -- Resist Full Magic Attack
-- Raigen
(7707, 4290, 1), -- Race
(7707, 4045, 1), -- Resist Full Magic Attack
-- Nasign
(7708, 4290, 1), -- Race
(7708, 4045, 1), -- Resist Full Magic Attack
-- Norton
(7709, 4290, 1), -- Race
(7709, 4045, 1), -- Resist Full Magic Attack
-- Weston
(7710, 4290, 1), -- Race
(7710, 4045, 1), -- Resist Full Magic Attack
-- Byron
(7711, 4290, 1), -- Race
(7711, 4045, 1), -- Resist Full Magic Attack
-- Makhis
(7712, 4290, 1), -- Race
(7712, 4045, 1), -- Resist Full Magic Attack
-- Gardner
(7713, 4290, 1), -- Race
(7713, 4045, 1), -- Resist Full Magic Attack
-- Paros
(7714, 4290, 1), -- Race
(7714, 4045, 1), -- Resist Full Magic Attack
-- Marina
(7715, 4290, 1), -- Race
(7715, 4045, 1), -- Resist Full Magic Attack
-- Cecile
(7716, 4290, 1), -- Race
(7716, 4045, 1), -- Resist Full Magic Attack
-- Gauen
(7717, 4290, 1), -- Race
(7717, 4045, 1), -- Resist Full Magic Attack
-- Joan
(7718, 4290, 1), -- Race
(7718, 4045, 1), -- Resist Full Magic Attack
-- Mariell
(7719, 4290, 1), -- Race
(7719, 4045, 1), -- Resist Full Magic Attack
-- Kaiena
(7720, 4290, 1), -- Race
(7720, 4045, 1), -- Resist Full Magic Attack
-- Ladd
(7721, 4290, 1), -- Race
(7721, 4045, 1), -- Resist Full Magic Attack
-- Merian
(7722, 4290, 1), -- Race
(7722, 4045, 1), -- Resist Full Magic Attack
-- Roy
(7723, 4290, 1), -- Race
(7723, 4045, 1), -- Resist Full Magic Attack
-- Tavillian
(7724, 4290, 1), -- Race
(7724, 4045, 1), -- Resist Full Magic Attack
-- Yening
(7725, 4290, 1), -- Race
(7725, 4045, 1), -- Resist Full Magic Attack
-- Tebose
(7726, 4290, 1), -- Race
(7726, 4045, 1), -- Resist Full Magic Attack
-- Verona
(7727, 4290, 1), -- Race
(7727, 4045, 1), -- Resist Full Magic Attack
-- Leirynn
(7728, 4290, 1), -- Race
(7728, 4045, 1), -- Resist Full Magic Attack
-- Borys
(7729, 4290, 1), -- Race
(7729, 4045, 1), -- Resist Full Magic Attack
-- Jax
(7730, 4290, 1), -- Race
(7730, 4045, 1), -- Resist Full Magic Attack
-- Martin
(7731, 4290, 1), -- Race
(7731, 4045, 1), -- Resist Full Magic Attack
-- Ol Mahum Pilgrim
(7732, 4295, 1), -- Race
-- Guard
(7733, 4290, 1), -- Race
(7733, 4045, 1), -- Resist Full Magic Attack
-- Supply Box On Wharf
(7734, 4290, 1), -- Race
(7734, 4045, 1), -- Resist Full Magic Attack
(7734, 4390, 1), -- NPC Abnormal Immunity
-- Sophya
(7735, 4290, 1), -- Race
(7735, 4045, 1), -- Resist Full Magic Attack
-- Redfoot
(7736, 4290, 1), -- Race
(7736, 4045, 1), -- Resist Full Magic Attack
-- Morgan
(7737, 4290, 1), -- Race
(7737, 4045, 1), -- Resist Full Magic Attack
-- Matild
(7738, 4290, 1), -- Race
(7738, 4045, 1), -- Resist Full Magic Attack
-- Keltir
(7739, 4293, 1), -- Race
(7739, 4045, 1), -- Resist Full Magic Attack
-- Toad
(7740, 4292, 1), -- Race
(7740, 4045, 1), -- Resist Full Magic Attack
-- Rabbit
(7741, 4293, 1), -- Race
(7741, 4045, 1), -- Resist Full Magic Attack
-- Rupina
(7742, 4302, 1), -- Race
(7742, 4045, 1), -- Resist Full Magic Attack
-- Wisdom Chest
(7743, 4290, 1), -- Race
(7743, 4045, 1), -- Resist Full Magic Attack
(7743, 4390, 1), -- NPC Abnormal Immunity
-- Grey
(7744, 4290, 1), -- Race
(7744, 4045, 1), -- Resist Full Magic Attack
-- Tor
(7745, 4290, 1), -- Race
(7745, 4045, 1), -- Resist Full Magic Attack
-- Cybellin
(7746, 4290, 1), -- Race
(7746, 4045, 1), -- Resist Full Magic Attack
-- Mymyu
(7747, 4302, 1), -- Race
(7747, 4045, 1), -- Resist Full Magic Attack
-- Exarion
(7748, 4299, 1), -- Race
(7748, 4045, 1), -- Resist Full Magic Attack
-- Zwov
(7749, 4299, 1), -- Race
(7749, 4045, 1), -- Resist Full Magic Attack
-- Kalibran
(7750, 4299, 1), -- Race
(7750, 4045, 1), -- Resist Full Magic Attack
-- Suzet
(7751, 4299, 1), -- Race
(7751, 4045, 1), -- Resist Full Magic Attack
-- Shamhai
(7752, 4299, 1), -- Race
(7752, 4045, 1), -- Resist Full Magic Attack
-- Gabrielle
(7753, 4290, 1), -- Race
(7753, 4045, 1), -- Resist Full Magic Attack
-- Gilmore
(7754, 4290, 1), -- Race
(7754, 4045, 1), -- Resist Full Magic Attack
-- Theodric
(7755, 4290, 1), -- Race
(7755, 4045, 1), -- Resist Full Magic Attack
-- Sir Kristof Rodemai
(7756, 4290, 1), -- Race
(7756, 4045, 1), -- Resist Full Magic Attack
-- Statue Of Offering
(7757, 4290, 1), -- Race
(7757, 4045, 1), -- Resist Full Magic Attack
(7757, 4390, 1), -- NPC Abnormal Immunity
-- Athrea
(7758, 4290, 1), -- Race
(7758, 4045, 1), -- Resist Full Magic Attack
--  Kalis
(7759, 4290, 1), -- Race
(7759, 4045, 1), -- Resist Full Magic Attack
-- Sir Gustaf Athebaldt
(7760, 4290, 1), -- Race
(7760, 4045, 1), -- Resist Full Magic Attack
-- Corpse Of Fritz
(7761, 4290, 1), -- Race
(7761, 4045, 1), -- Resist Full Magic Attack
(7761, 4390, 1), -- NPC Abnormal Immunity
-- Corpse Of Lutz
(7762, 4290, 1), -- Race
(7762, 4045, 1), -- Resist Full Magic Attack
(7762, 4390, 1), -- NPC Abnormal Immunity
-- Corpse Of Kurtz
(7763, 4290, 1), -- Race
(7763, 4045, 1), -- Resist Full Magic Attack
(7763, 4390, 1), -- NPC Abnormal Immunity
-- Balthazar
(7764, 4290, 1), -- Race
(7764, 4045, 1), -- Resist Full Magic Attack
-- Imperial Coffer
(7765, 4290, 1), -- Race
(7765, 4045, 1), -- Resist Full Magic Attack
(7765, 4390, 1), -- NPC Abnormal Immunity
-- Cleo
(7766, 4290, 1), -- Race
(7766, 4045, 1), -- Resist Full Magic Attack
-- Auctioneer
(7767, 4290, 1), -- Race
(7767, 4045, 1), -- Resist Full Magic Attack
-- Auctioneer
(7768, 4290, 1), -- Race
(7768, 4045, 1), -- Resist Full Magic Attack
-- Auctioneer
(7769, 4290, 1), -- Race
(7769, 4045, 1), -- Resist Full Magic Attack
-- Auctioneer
(7770, 4290, 1), -- Race
(7770, 4045, 1), -- Resist Full Magic Attack
-- Auctioneer
(7771, 4290, 1), -- Race
(7771, 4045, 1), -- Resist Full Magic Attack
-- Wilson
(7772, 4290, 1), -- Race
(7772, 4045, 1), -- Resist Full Magic Attack
-- Todd
(7773, 4290, 1), -- Race
(7773, 4045, 1), -- Resist Full Magic Attack
-- Ruben
(7774, 4290, 1), -- Race
(7774, 4045, 1), -- Resist Full Magic Attack
-- Luce
(7775, 4290, 1), -- Race
(7775, 4045, 1), -- Resist Full Magic Attack
-- Horner
(7776, 4290, 1), -- Race
(7776, 4045, 1), -- Resist Full Magic Attack
-- Amiel
(7777, 4290, 1), -- Race
(7777, 4045, 1), -- Resist Full Magic Attack
-- Bremmer
(7778, 4290, 1), -- Race
(7778, 4045, 1), -- Resist Full Magic Attack
-- Faolan
(7779, 4290, 1), -- Race
(7779, 4045, 1), -- Resist Full Magic Attack
-- Callum
(7780, 4290, 1), -- Race
(7780, 4045, 1), -- Resist Full Magic Attack
-- Kogan
(7781, 4290, 1), -- Race
(7781, 4045, 1), -- Resist Full Magic Attack
-- Winker
(7782, 4290, 1), -- Race
(7782, 4045, 1), -- Resist Full Magic Attack
-- Merton
(7783, 4290, 1), -- Race
(7783, 4045, 1), -- Resist Full Magic Attack
-- Black
(7784, 4290, 1), -- Race
(7784, 4045, 1), -- Resist Full Magic Attack
-- Renny
(7785, 4290, 1), -- Race
(7785, 4045, 1), -- Resist Full Magic Attack
-- Dillon
(7786, 4290, 1), -- Race
(7786, 4045, 1), -- Resist Full Magic Attack
-- Latif
(7787, 4290, 1), -- Race
(7787, 4045, 1), -- Resist Full Magic Attack
-- Boyer
(7788, 4290, 1), -- Race
(7788, 4045, 1), -- Resist Full Magic Attack
-- Baback
(7789, 4290, 1), -- Race
(7789, 4045, 1), -- Resist Full Magic Attack
-- Tim
(7790, 4290, 1), -- Race
(7790, 4045, 1), -- Resist Full Magic Attack
-- Loring
(7791, 4290, 1), -- Race
(7791, 4045, 1), -- Resist Full Magic Attack
-- Lowell
(7792, 4290, 1), -- Race
(7792, 4045, 1), -- Resist Full Magic Attack
-- Paranos
(7793, 4290, 1), -- Race
(7793, 4045, 1), -- Resist Full Magic Attack
-- Klingel
(7794, 4290, 1), -- Race
(7794, 4045, 1), -- Resist Full Magic Attack
-- Keffer
(7795, 4290, 1), -- Race
(7795, 4045, 1), -- Resist Full Magic Attack
-- Sand
(7796, 4290, 1), -- Race
(7796, 4045, 1), -- Resist Full Magic Attack
-- Teters
(7797, 4290, 1), -- Race
(7797, 4045, 1), -- Resist Full Magic Attack
-- Seth
(7798, 4290, 1), -- Race
(7798, 4045, 1), -- Resist Full Magic Attack
-- Jabilo
(7799, 4290, 1), -- Race
(7799, 4045, 1), -- Resist Full Magic Attack
-- Ron
(7800, 4290, 1), -- Race
(7800, 4045, 1), -- Resist Full Magic Attack
-- Borna
(7801, 4290, 1), -- Race
(7801, 4045, 1), -- Resist Full Magic Attack
-- Flynn
(7802, 4290, 1), -- Race
(7802, 4045, 1), -- Resist Full Magic Attack
-- Jamal
(7803, 4290, 1), -- Race
(7803, 4045, 1), -- Resist Full Magic Attack
-- Watkins
(7804, 4290, 1), -- Race
(7804, 4045, 1), -- Resist Full Magic Attack
-- Cohen
(7805, 4290, 1), -- Race
(7805, 4045, 1), -- Resist Full Magic Attack
-- Bint
(7806, 4290, 1), -- Race
(7806, 4045, 1), -- Resist Full Magic Attack
-- Bourdon
(7807, 4290, 1), -- Race
(7807, 4045, 1), -- Resist Full Magic Attack
-- Pery
(7808, 4290, 1), -- Race
(7808, 4045, 1), -- Resist Full Magic Attack
-- Gampert
(7809, 4290, 1), -- Race
(7809, 4045, 1), -- Resist Full Magic Attack
-- Gonti
(7810, 4290, 1), -- Race
(7810, 4045, 1), -- Resist Full Magic Attack
-- Baraha
(7811, 4290, 1), -- Race
(7811, 4045, 1), -- Resist Full Magic Attack
-- Vanhal
(7812, 4290, 1), -- Race
(7812, 4045, 1), -- Resist Full Magic Attack
-- Dan
(7813, 4290, 1), -- Race
(7813, 4045, 1), -- Resist Full Magic Attack
-- Briggs
(7814, 4290, 1), -- Race
(7814, 4045, 1), -- Resist Full Magic Attack
-- Stegmann
(7815, 4290, 1), -- Race
(7815, 4045, 1), -- Resist Full Magic Attack
-- Randolph
(7816, 4290, 1), -- Race
(7816, 4045, 1), -- Resist Full Magic Attack
-- Trotter
(7817, 4290, 1), -- Race
(7817, 4045, 1), -- Resist Full Magic Attack
-- Veder
(7818, 4290, 1), -- Race
(7818, 4045, 1), -- Resist Full Magic Attack
-- Danas
(7819, 4290, 1), -- Race
(7819, 4045, 1), -- Resist Full Magic Attack
-- Corey
(7820, 4290, 1), -- Race
(7820, 4045, 1), -- Resist Full Magic Attack
-- Barney
(7821, 4290, 1), -- Race
(7821, 4045, 1), -- Resist Full Magic Attack
-- Krett
(7822, 4290, 1), -- Race
(7822, 4045, 1), -- Resist Full Magic Attack
-- Tairee
(7823, 4290, 1), -- Race
(7823, 4045, 1), -- Resist Full Magic Attack
-- Tanner
(7824, 4290, 1), -- Race
(7824, 4045, 1), -- Resist Full Magic Attack
-- Cresson
(7825, 4290, 1), -- Race
(7825, 4045, 1), -- Resist Full Magic Attack
-- Crothers
(7826, 4290, 1), -- Race
(7826, 4045, 1), -- Resist Full Magic Attack
-- Lundy
(7827, 4290, 1), -- Race
(7827, 4045, 1), -- Resist Full Magic Attack
-- Waters
(7828, 4290, 1), -- Race
(7828, 4045, 1), -- Resist Full Magic Attack
-- Cooper
(7829, 4290, 1), -- Race
(7829, 4045, 1), -- Resist Full Magic Attack
-- Joey
(7830, 4290, 1), -- Race
(7830, 4045, 1), -- Resist Full Magic Attack
-- Nelson
(7831, 4290, 1), -- Race
(7831, 4045, 1), -- Resist Full Magic Attack
-- Hardin
(7832, 4290, 1), -- Race
(7832, 4045, 1), -- Resist Full Magic Attack
-- Kaspar
(7833, 4290, 1), -- Race
(7833, 4045, 1), -- Resist Full Magic Attack
-- Cema
(7834, 4290, 1), -- Race
(7834, 4045, 1), -- Resist Full Magic Attack
-- Icarus
(7835, 4290, 1), -- Race
(7835, 4045, 1), -- Resist Full Magic Attack
-- Minerva
(7836, 4290, 1), -- Race
(7836, 4045, 1), -- Resist Full Magic Attack
-- Woodrow
(7837, 4290, 1), -- Race
(7837, 4045, 1), -- Resist Full Magic Attack
-- Woodley
(7838, 4290, 1), -- Race
(7838, 4045, 1), -- Resist Full Magic Attack
-- Holly
(7839, 4290, 1), -- Race
(7839, 4045, 1), -- Resist Full Magic Attack
-- Lorenzo
(7840, 4290, 1), -- Race
(7840, 4045, 1), -- Resist Full Magic Attack
-- Carson
(7841, 4290, 1), -- Race
(7841, 4045, 1), -- Resist Full Magic Attack
-- Alexis
(7842, 4290, 1), -- Race
(7842, 4045, 1), -- Resist Full Magic Attack
-- Romp
(7843, 4290, 1), -- Race
(7843, 4045, 1), -- Resist Full Magic Attack
-- Walderal
(7844, 4290, 1), -- Race
(7844, 4045, 1), -- Resist Full Magic Attack
-- Klump
(7845, 4290, 1), -- Race
(7845, 4045, 1), -- Resist Full Magic Attack
-- Wilbert
(7846, 4290, 1), -- Race
(7846, 4045, 1), -- Resist Full Magic Attack
-- Ferris
(7847, 4290, 1), -- Race
(7847, 4045, 1), -- Resist Full Magic Attack
-- Elisa
(7848, 4290, 1), -- Race
(7848, 4045, 1), -- Resist Full Magic Attack
-- Sedrick
(7849, 4290, 1), -- Race
(7849, 4045, 1), -- Resist Full Magic Attack
-- Aiken
(7850, 4290, 1), -- Race
(7850, 4045, 1), -- Resist Full Magic Attack
-- Kendra
(7851, 4290, 1), -- Race
(7851, 4045, 1), -- Resist Full Magic Attack
-- Sinden
(7852, 4290, 1), -- Race
(7852, 4045, 1), -- Resist Full Magic Attack
-- Raien
(7853, 4290, 1), -- Race
(7853, 4045, 1), -- Resist Full Magic Attack
-- Drikiyan
(7854, 4290, 1), -- Race
(7854, 4045, 1), -- Resist Full Magic Attack
-- Desmond
(7855, 4290, 1), -- Race
(7855, 4045, 1), -- Resist Full Magic Attack
-- Winonin
(7856, 4290, 1), -- Race
(7856, 4045, 1), -- Resist Full Magic Attack
-- Orven
(7857, 4290, 1), -- Race
(7857, 4045, 1), -- Resist Full Magic Attack
-- Ross
(7858, 4290, 1), -- Race
(7858, 4045, 1), -- Resist Full Magic Attack
-- Vevina
(7859, 4290, 1), -- Race
(7859, 4045, 1), -- Resist Full Magic Attack
-- Flownia
(7860, 4290, 1), -- Race
(7860, 4045, 1), -- Resist Full Magic Attack
-- Linette
(7861, 4290, 1), -- Race
(7861, 4045, 1), -- Resist Full Magic Attack
-- Oltlin
(7862, 4290, 1), -- Race
(7862, 4045, 1), -- Resist Full Magic Attack
-- Ghest
(7863, 4290, 1), -- Race
(7863, 4045, 1), -- Resist Full Magic Attack
-- Hanellin
(7864, 4290, 1), -- Race
(7864, 4045, 1), -- Resist Full Magic Attack
-- Ladanza
(7865, 4290, 1), -- Race
(7865, 4045, 1), -- Resist Full Magic Attack
-- Marestella
(7866, 4290, 1), -- Race
(7866, 4045, 1), -- Resist Full Magic Attack
-- Reva
(7867, 4290, 1), -- Race
(7867, 4045, 1), -- Resist Full Magic Attack
-- Sir Eric Rodemai
(7868, 4290, 1), -- Race
(7868, 4045, 1), -- Resist Full Magic Attack
-- Lemper
(7869, 4290, 1), -- Race
(7869, 4045, 1), -- Resist Full Magic Attack
-- Kurtiz
(7870, 4290, 1), -- Race
(7870, 4045, 1), -- Resist Full Magic Attack
-- Bret
(7871, 4290, 1), -- Race
(7871, 4045, 1), -- Resist Full Magic Attack
-- Conroy
(7872, 4290, 1), -- Race
(7872, 4045, 1), -- Resist Full Magic Attack
-- Coleman
(7873, 4290, 1), -- Race
(7873, 4045, 1), -- Resist Full Magic Attack
-- Aldis
(7874, 4290, 1), -- Race
(7874, 4045, 1), -- Resist Full Magic Attack
-- Carlton
(7875, 4290, 1), -- Race
(7875, 4045, 1), -- Resist Full Magic Attack
-- Eastan
(7876, 4290, 1), -- Race
(7876, 4045, 1), -- Resist Full Magic Attack
-- Grayson
(7877, 4290, 1), -- Race
(7877, 4045, 1), -- Resist Full Magic Attack
-- Angelina
(7878, 4290, 1), -- Race
(7878, 4045, 1), -- Resist Full Magic Attack
-- Felton
(7879, 4290, 1), -- Race
(7879, 4045, 1), -- Resist Full Magic Attack
-- Viktor Van Dake
(7880, 4290, 1), -- Race
(7880, 4045, 1), -- Resist Full Magic Attack
-- Sanders
(7881, 4290, 1), -- Race
(7881, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(7882, 4290, 1), -- Race
(7882, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(7883, 4290, 1), -- Race
(7883, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(7884, 4290, 1), -- Race
(7884, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(7885, 4290, 1), -- Race
(7885, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(7886, 4290, 1), -- Race
(7886, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(7887, 4290, 1), -- Race
(7887, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(7888, 4290, 1), -- Race
(7888, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(7889, 4290, 1), -- Race
(7889, 4045, 1), -- Resist Full Magic Attack
-- Espen
(7890, 4290, 1), -- Race
(7890, 4045, 1), -- Resist Full Magic Attack
-- Verona
(7891, 4290, 1), -- Race
(7891, 4045, 1), -- Resist Full Magic Attack
-- Enverun
(7892, 4290, 1), -- Race
(7892, 4045, 1), -- Resist Full Magic Attack
-- Payel
(7893, 4290, 1), -- Race
(7893, 4045, 1), -- Resist Full Magic Attack
-- Natools
(7894, 4290, 1), -- Race
(7894, 4045, 1), -- Resist Full Magic Attack
-- Kluck
(7895, 4290, 1), -- Race
(7895, 4045, 1), -- Resist Full Magic Attack
-- Mia
(7896, 4290, 1), -- Race
(7896, 4045, 1), -- Resist Full Magic Attack
-- Roman
(7897, 4290, 1), -- Race
(7897, 4045, 1), -- Resist Full Magic Attack
-- Morning
(7898, 4290, 1), -- Race
(7898, 4045, 1), -- Resist Full Magic Attack
-- Flauen
(7899, 4290, 1), -- Race
(7899, 4045, 1), -- Resist Full Magic Attack
-- Marcus
(7900, 4290, 1), -- Race
(7900, 4045, 1), -- Resist Full Magic Attack
-- Arti
(7901, 4290, 1), -- Race
(7901, 4045, 1), -- Resist Full Magic Attack
-- Karuna
(7902, 4290, 1), -- Race
(7902, 4045, 1), -- Resist Full Magic Attack
-- Traus
(7903, 4290, 1), -- Race
(7903, 4045, 1), -- Resist Full Magic Attack
-- Naiel
(7904, 4290, 1), -- Race
(7904, 4045, 1), -- Resist Full Magic Attack
-- Squillari
(7905, 4290, 1), -- Race
(7905, 4045, 1), -- Resist Full Magic Attack
-- Ranton
(7906, 4290, 1), -- Race
(7906, 4045, 1), -- Resist Full Magic Attack
-- Minevia
(7907, 4290, 1), -- Race
(7907, 4045, 1), -- Resist Full Magic Attack
-- Tanios
(7908, 4290, 1), -- Race
(7908, 4045, 1), -- Resist Full Magic Attack
-- Anabel
(7909, 4290, 1), -- Race
(7909, 4045, 1), -- Resist Full Magic Attack
-- Xairakin
(7910, 4290, 1), -- Race
(7910, 4045, 1), -- Resist Full Magic Attack
-- Brikus
(7911, 4290, 1), -- Race
(7911, 4045, 1), -- Resist Full Magic Attack
-- Xenovia
(7912, 4290, 1), -- Race
(7912, 4045, 1), -- Resist Full Magic Attack
-- Tushku
(7913, 4290, 1), -- Race
(7913, 4045, 1), -- Resist Full Magic Attack
-- Sorbo
(7914, 4290, 1), -- Race
(7914, 4045, 1), -- Resist Full Magic Attack
-- Takina
(7915, 4290, 1), -- Race
(7915, 4045, 1), -- Resist Full Magic Attack
-- Gosta
(7916, 4290, 1), -- Race
(7916, 4045, 1), -- Resist Full Magic Attack
-- Dupuis
(7917, 4290, 1), -- Race
(7917, 4045, 1), -- Resist Full Magic Attack
-- Kent
(7918, 4290, 1), -- Race
(7918, 4045, 1), -- Resist Full Magic Attack
-- Rodic
(7919, 4290, 1), -- Race
(7919, 4045, 1), -- Resist Full Magic Attack
-- Kraisen
(7920, 4290, 1), -- Race
(7920, 4045, 1), -- Resist Full Magic Attack
-- Timos
(7921, 4290, 1), -- Race
(7921, 4045, 1), -- Resist Full Magic Attack
-- Cage
(7922, 4290, 1), -- Race
(7922, 4045, 1), -- Resist Full Magic Attack
-- Dunst
(7923, 4290, 1), -- Race
(7923, 4045, 1), -- Resist Full Magic Attack
-- Nedy
(7924, 4290, 1), -- Race
(7924, 4045, 1), -- Resist Full Magic Attack
-- Morelyn
(7925, 4290, 1), -- Race
(7925, 4045, 1), -- Resist Full Magic Attack
-- Restina
(7926, 4290, 1), -- Race
(7926, 4045, 1), -- Resist Full Magic Attack
-- Alicia
(7927, 4290, 1), -- Race
(7927, 4045, 1), -- Resist Full Magic Attack
-- Fenster
(7928, 4290, 1), -- Race
(7928, 4045, 1), -- Resist Full Magic Attack
-- Patrin
(7929, 4290, 1), -- Race
(7929, 4045, 1), -- Resist Full Magic Attack
-- Rogent
(7930, 4290, 1), -- Race
(7930, 4045, 1), -- Resist Full Magic Attack
-- Bentley
(7931, 4290, 1), -- Race
(7931, 4045, 1), -- Resist Full Magic Attack
-- Benica
(7932, 4290, 1), -- Race
(7932, 4045, 1), -- Resist Full Magic Attack
-- Braki
(7933, 4290, 1), -- Race
(7933, 4045, 1), -- Resist Full Magic Attack
-- Marsha
(7934, 4290, 1), -- Race
(7934, 4045, 1), -- Resist Full Magic Attack
-- Trumpin
(7935, 4290, 1), -- Race
(7935, 4045, 1), -- Resist Full Magic Attack
-- Malcom1
(7936, 4292, 1), -- Race
(7936, 4045, 1), -- Resist Full Magic Attack
-- Malcom2
(7937, 4293, 1), -- Race
(7937, 4045, 1), -- Resist Full Magic Attack
-- Malcom3
(7938, 4295, 1), -- Race
(7938, 4045, 1), -- Resist Full Magic Attack
-- Malcom4
(7939, 4290, 1), -- Race
(7939, 4045, 1), -- Resist Full Magic Attack
-- Malcom5
(7940, 4302, 1), -- Race
(7940, 4045, 1), -- Resist Full Magic Attack
-- Malcom6
(7941, 4293, 1), -- Race
(7941, 4045, 1), -- Resist Full Magic Attack
-- Malcom7
(7942, 4291, 1), -- Race
(7942, 4045, 1), -- Resist Full Magic Attack
-- Malcom8
(7943, 4293, 1), -- Race
(7943, 4045, 1), -- Resist Full Magic Attack
-- Malcom9
(7944, 4290, 1), -- Race
(7944, 4045, 1), -- Resist Full Magic Attack
-- Malcom10
(7945, 4290, 1), -- Race
(7945, 4045, 1), -- Resist Full Magic Attack
-- Lucianne Tanford
(7946, 4290, 1), -- Race
(7946, 4045, 1), -- Resist Full Magic Attack
-- Rerikya
(7947, 4290, 1), -- Race
(7947, 4045, 1), -- Resist Full Magic Attack
-- Meridien
(7948, 4290, 1), -- Race
(7948, 4045, 1), -- Resist Full Magic Attack
-- Keplon
(7949, 4290, 1), -- Race
(7949, 4045, 1), -- Resist Full Magic Attack
-- Euclie
(7950, 4290, 1), -- Race
(7950, 4045, 1), -- Resist Full Magic Attack
-- Pithgon
(7951, 4290, 1), -- Race
(7951, 4045, 1), -- Resist Full Magic Attack
-- Dimension Vortex 1
(7952, 4290, 1), -- Race
(7952, 4045, 1), -- Resist Full Magic Attack
(7952, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Vortex 2
(7953, 4290, 1), -- Race
(7953, 4045, 1), -- Resist Full Magic Attack
(7953, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Vortex 3
(7954, 4290, 1), -- Race
(7954, 4045, 1), -- Resist Full Magic Attack
(7954, 4390, 1), -- NPC Abnormal Immunity
-- Gillian's Revenant
(7955, 4290, 1), -- Race
(7955, 4045, 1), -- Resist Full Magic Attack
-- Nanarin
(7956, 4290, 1), -- Race
(7956, 4045, 1), -- Resist Full Magic Attack
-- Swan
(7957, 4290, 1), -- Race
(7957, 4045, 1), -- Resist Full Magic Attack
-- Galion
(7958, 4290, 1), -- Race
(7958, 4045, 1), -- Resist Full Magic Attack
-- Barbado
(7959, 4290, 1), -- Race
(7959, 4045, 1), -- Resist Full Magic Attack
-- Beer Chest
(7960, 4290, 1), -- Race
(7960, 4045, 1), -- Resist Full Magic Attack
(7960, 4390, 1), -- NPC Abnormal Immunity
-- Cloth Chest
(7961, 4290, 1), -- Race
(7961, 4045, 1), -- Resist Full Magic Attack
(7961, 4390, 1), -- NPC Abnormal Immunity
-- Statue of Water
(7962, 4290, 1), -- Race
(7962, 4045, 1), -- Resist Full Magic Attack
(7962, 4390, 1), -- NPC Abnormal Immunity
-- Statue of Fire
(7963, 4290, 1), -- Race
(7963, 4045, 1), -- Resist Full Magic Attack
(7963, 4390, 1), -- NPC Abnormal Immunity
-- Statue of Wind
(7964, 4290, 1), -- Race
(7964, 4045, 1), -- Resist Full Magic Attack
(7964, 4390, 1), -- NPC Abnormal Immunity
-- Statue of Earth
(7965, 4290, 1), -- Race
(7965, 4045, 1), -- Resist Full Magic Attack
(7965, 4390, 1), -- NPC Abnormal Immunity
-- Statue of Darkness
(7966, 4290, 1), -- Race
(7966, 4045, 1), -- Resist Full Magic Attack
(7966, 4390, 1), -- NPC Abnormal Immunity
-- Statue of Light
(7967, 4290, 1), -- Race
(7967, 4045, 1), -- Resist Full Magic Attack
(7967, 4390, 1), -- NPC Abnormal Immunity
-- Jennifer
(7968, 4290, 1), -- Race
(7968, 4045, 1), -- Resist Full Magic Attack
-- Iason Heine
(7969, 4290, 1), -- Race
(7969, 4045, 1), -- Resist Full Magic Attack
-- Dorothy
(7970, 4290, 1), -- Race
(7970, 4045, 1), -- Resist Full Magic Attack
-- Orpheus
(7971, 4290, 1), -- Race
(7971, 4045, 1), -- Resist Full Magic Attack
-- Orpheus Resurrecter
(7972, 4291, 1), -- Race
(7972, 4045, 1), -- Resist Full Magic Attack
-- Medium Jar
(7973, 4290, 1), -- Race
(7973, 4045, 1), -- Resist Full Magic Attack
(7973, 4390, 1), -- NPC Abnormal Immunity
-- Oliver
(7974, 4290, 1), -- Race
(7974, 4045, 1), -- Resist Full Magic Attack
-- Clarine
(7975, 4290, 1), -- Race
(7975, 4045, 1), -- Resist Full Magic Attack
-- Resurrected Town Maiden
(7976, 4290, 1), -- Race
(7976, 4045, 1), -- Resist Full Magic Attack
-- Holy Ark of Secrecy1
(7977, 4290, 1), -- Race
(7977, 4045, 1), -- Resist Full Magic Attack
(7977, 4390, 1), -- NPC Abnormal Immunity
-- Holy Ark of Secrecy2
(7978, 4290, 1), -- Race
(7978, 4045, 1), -- Resist Full Magic Attack
(7978, 4390, 1), -- NPC Abnormal Immunity
-- Holy Ark of Secrecy3
(7979, 4290, 1), -- Race
(7979, 4045, 1), -- Resist Full Magic Attack
(7979, 4390, 1), -- NPC Abnormal Immunity
-- Ark Guardian's Corpse
(7980, 4297, 1), -- Race
(7980, 4045, 1), -- Resist Full Magic Attack
(7980, 4390, 1), -- NPC Abnormal Immunity
-- Black Judge
(7981, 4290, 1), -- Race
(7981, 4045, 1), -- Resist Full Magic Attack
-- Sleeping Ant Larva1
(7982, 4301, 1), -- Race
(7982, 4045, 1), -- Resist Full Magic Attack
-- Sleeping Ant Larva2
(7983, 4301, 1), -- Race
(7983, 4045, 1), -- Resist Full Magic Attack
-- Sleeping Ant Larva3
(7984, 4301, 1), -- Race
(7984, 4045, 1), -- Resist Full Magic Attack
-- Sleeping Ant Larva4
(7985, 4301, 1), -- Race
(7985, 4045, 1), -- Resist Full Magic Attack
-- Sleeping Ant Larva5
(7986, 4301, 1), -- Race
(7986, 4045, 1), -- Resist Full Magic Attack
-- Sleeping Ant Larva6
(7987, 4301, 1), -- Race
(7987, 4045, 1), -- Resist Full Magic Attack
-- Verce
(7988, 4290, 1), -- Race
(7988, 4045, 1), -- Resist Full Magic Attack
-- Chest of Bifrons
(7989, 4290, 1), -- Race
(7989, 4045, 1), -- Resist Full Magic Attack
(7989, 4390, 1), -- NPC Abnormal Immunity
-- Lottery Ticket Seller
(7990, 4290, 1), -- Race
(7990, 4045, 1), -- Resist Full Magic Attack
-- Lottery Ticket Seller
(7991, 4290, 1), -- Race
(7991, 4045, 1), -- Resist Full Magic Attack
-- Lottery Ticket Seller
(7992, 4290, 1), -- Race
(7992, 4045, 1), -- Resist Full Magic Attack
-- Lottery Ticket Seller
(7993, 4290, 1), -- Race
(7993, 4045, 1), -- Resist Full Magic Attack
-- Lottery Ticket Seller
(7994, 4290, 1), -- Race
(7994, 4045, 1), -- Resist Full Magic Attack
-- Race Manager
(7995, 4290, 1), -- Race
(7995, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(7996, 4290, 1), -- Race
(7996, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(7997, 4290, 1), -- Race
(7997, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(7998, 4290, 1), -- Race
(7998, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(7999, 4290, 1), -- Race
(7999, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(8000, 4290, 1), -- Race
(8000, 4045, 1), -- Resist Full Magic Attack
-- Claudia Athebalt
(8001, 4290, 1), -- Race
(8001, 4045, 1), -- Resist Full Magic Attack
-- Reorin
(8002, 4290, 1), -- Race
(8002, 4045, 1), -- Resist Full Magic Attack
-- Here I Come
(8003, 4293, 1), -- Race
(8003, 4045, 1), -- Resist Full Magic Attack
-- Half Moon Love
(8004, 4293, 1), -- Race
(8004, 4045, 1), -- Resist Full Magic Attack
-- Everlasting
(8005, 4291, 1), -- Race
(8005, 4045, 1), -- Resist Full Magic Attack
-- Dark Side of the Moon
(8006, 4298, 1), -- Race
(8006, 4045, 1), -- Resist Full Magic Attack
-- Wind Rider
(8007, 4296, 1), -- Race
(8007, 4045, 1), -- Resist Full Magic Attack
-- Shooting Star
(8008, 4296, 1), -- Race
(8008, 4045, 1), -- Resist Full Magic Attack
-- Cyclone Thunder
(8009, 4293, 1), -- Race
(8009, 4045, 1), -- Resist Full Magic Attack
-- Hungry Baby
(8010, 4299, 1), -- Race
(8010, 4045, 1), -- Resist Full Magic Attack
-- Salty Dog
(8011, 4293, 1), -- Race
(8011, 4045, 1), -- Resist Full Magic Attack
-- Raging Revolution
(8012, 4293, 1), -- Race
(8012, 4045, 1), -- Resist Full Magic Attack
-- Valentine Blue
(8013, 4296, 1), -- Race
(8013, 4045, 1), -- Resist Full Magic Attack
-- Light My Fire
(8014, 4291, 1), -- Race
(8014, 4045, 1), -- Resist Full Magic Attack
-- Red Bullet
(8015, 4293, 1), -- Race
(8015, 4045, 1), -- Resist Full Magic Attack
-- Shining Silver
(8016, 4293, 1), -- Race
(8016, 4045, 1), -- Resist Full Magic Attack
-- Over the top
(8017, 4301, 1), -- Race
(8017, 4045, 1), -- Resist Full Magic Attack
-- Royal Straight
(8018, 4291, 1), -- Race
(8018, 4045, 1), -- Resist Full Magic Attack
-- All Seven
(8019, 4295, 1), -- Race
(8019, 4045, 1), -- Resist Full Magic Attack
-- Nasty Green
(8020, 4296, 1), -- Race
(8020, 4045, 1), -- Resist Full Magic Attack
-- Shortcut
(8021, 4295, 1), -- Race
(8021, 4045, 1), -- Resist Full Magic Attack
-- Typhoon Tiger
(8022, 4301, 1), -- Race
(8022, 4045, 1), -- Resist Full Magic Attack
-- Red Hot
(8023, 4296, 1), -- Race
(8023, 4045, 1), -- Resist Full Magic Attack
-- Galaxy Express
(8024, 4292, 1), -- Race
(8024, 4045, 1), -- Resist Full Magic Attack
-- Best Condition
(8025, 4294, 1), -- Race
(8025, 4045, 1), -- Resist Full Magic Attack
-- Albatross
(8026, 4295, 1), -- Race
(8026, 4045, 1), -- Resist Full Magic Attack
-- Coffer of the Dead
(8027, 4290, 1), -- Race
(8027, 4045, 1), -- Resist Full Magic Attack
(8027, 4390, 1), -- NPC Abnormal Immunity
-- Chest of Kernon
(8028, 4290, 1), -- Race
(8028, 4045, 1), -- Resist Full Magic Attack
(8028, 4390, 1), -- NPC Abnormal Immunity
-- Chest of Golkonda
(8029, 4290, 1), -- Race
(8029, 4045, 1), -- Resist Full Magic Attack
(8029, 4390, 1), -- NPC Abnormal Immunity
-- Chest of Hallate
(8030, 4290, 1), -- Race
(8030, 4045, 1), -- Resist Full Magic Attack
(8030, 4390, 1), -- NPC Abnormal Immunity
-- Broadcasting Tower
(8031, 4290, 1), -- Race
(8031, 4045, 1), -- Resist Full Magic Attack
(8031, 4390, 1), -- NPC Abnormal Immunity
-- Guard
(8032, 4290, 1), -- Race
(8032, 4045, 1), -- Resist Full Magic Attack
-- Sentinel
(8033, 4290, 1), -- Race
(8033, 4045, 1), -- Resist Full Magic Attack
-- Sentry
(8034, 4290, 1), -- Race
(8034, 4045, 1), -- Resist Full Magic Attack
-- Defender
(8035, 4290, 1), -- Race
(8035, 4045, 1), -- Resist Full Magic Attack
-- Centurion
(8036, 4290, 1), -- Race
(8036, 4045, 1), -- Resist Full Magic Attack
-- Lionel Hunter
(8037, 4290, 1), -- Race
(8037, 4045, 1), -- Resist Full Magic Attack
-- Neurath
(8038, 4290, 1), -- Race
(8038, 4045, 1), -- Resist Full Magic Attack
-- Schacht
(8039, 4290, 1), -- Race
(8039, 4045, 1), -- Resist Full Magic Attack
-- Raybell
(8040, 4290, 1), -- Race
(8040, 4045, 1), -- Resist Full Magic Attack
-- Solinus
(8041, 4290, 1), -- Race
(8041, 4045, 1), -- Resist Full Magic Attack
-- Kantabilon
(8042, 4290, 1), -- Race
(8042, 4045, 1), -- Resist Full Magic Attack
-- Octavia
(8043, 4290, 1), -- Race
(8043, 4045, 1), -- Resist Full Magic Attack
-- Galman
(8044, 4290, 1), -- Race
(8044, 4045, 1), -- Resist Full Magic Attack
-- Kitzka
(8045, 4290, 1), -- Race
(8045, 4045, 1), -- Resist Full Magic Attack
-- Marsden
(8046, 4290, 1), -- Race
(8046, 4045, 1), -- Resist Full Magic Attack
-- Kelly
(8047, 4290, 1), -- Race
(8047, 4045, 1), -- Resist Full Magic Attack
-- McDermott
(8048, 4290, 1), -- Race
(8048, 4045, 1), -- Resist Full Magic Attack
-- Pepper
(8049, 4290, 1), -- Race
(8049, 4045, 1), -- Resist Full Magic Attack
-- Thora
(8050, 4290, 1), -- Race
(8050, 4045, 1), -- Resist Full Magic Attack
-- Keach
(8051, 4290, 1), -- Race
(8051, 4045, 1), -- Resist Full Magic Attack
-- Heid
(8052, 4290, 1), -- Race
(8052, 4045, 1), -- Resist Full Magic Attack
-- Kidder
(8053, 4290, 1), -- Race
(8053, 4045, 1), -- Resist Full Magic Attack
-- Biggerstaff
(8054, 4290, 1), -- Race
(8054, 4045, 1), -- Resist Full Magic Attack
-- Doorman of Hell
(8055, 4290, 1), -- Race
(8055, 4045, 1), -- Resist Full Magic Attack
-- Doorman of Hell
(8056, 4290, 1), -- Race
(8056, 4045, 1), -- Resist Full Magic Attack
-- Loken
(8057, 4290, 1), -- Race
(8057, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(8058, 4290, 1), -- Race
(8058, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(8059, 4290, 1), -- Race
(8059, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(8060, 4290, 1), -- Race
(8060, 4045, 1), -- Resist Full Magic Attack
-- Blacksmith
(8061, 4290, 1), -- Race
(8061, 4045, 1), -- Resist Full Magic Attack
-- Blacksmith
(8062, 4290, 1), -- Race
(8062, 4045, 1), -- Resist Full Magic Attack
-- Blacksmith
(8063, 4290, 1), -- Race
(8063, 4045, 1), -- Resist Full Magic Attack
-- Blacksmith
(8064, 4290, 1), -- Race
(8064, 4045, 1), -- Resist Full Magic Attack
-- Blacksmith
(8065, 4290, 1), -- Race
(8065, 4045, 1), -- Resist Full Magic Attack
-- Blacksmith
(8066, 4290, 1), -- Race
(8066, 4045, 1), -- Resist Full Magic Attack
-- Rood
(8067, 4290, 1), -- Race
(8067, 4045, 1), -- Resist Full Magic Attack
-- Warehouse Keeper
(8068, 4290, 1), -- Race
(8068, 4045, 1), -- Resist Full Magic Attack
-- Warehouse Keeper
(8069, 4290, 1), -- Race
(8069, 4045, 1), -- Resist Full Magic Attack
-- Warehouse Keeper
(8070, 4290, 1), -- Race
(8070, 4045, 1), -- Resist Full Magic Attack
-- Warehouse Keeper
(8071, 4290, 1), -- Race
(8071, 4045, 1), -- Resist Full Magic Attack
-- Warehouse Keeper
(8072, 4290, 1), -- Race
(8072, 4045, 1), -- Resist Full Magic Attack
-- Warehouse Keeper
(8073, 4290, 1), -- Race
(8073, 4045, 1), -- Resist Full Magic Attack
-- Corpse of Hutaku
(8074, 4290, 1), -- Race
(8074, 4045, 1), -- Resist Full Magic Attack
-- Sales Cat of Ivory Tower
(8075, 4293, 1), -- Race
(8075, 4045, 1), -- Resist Full Magic Attack
-- Newbie Guide
(8076, 4290, 1), -- Race
(8076, 4045, 1), -- Resist Full Magic Attack
-- Newbie Guide
(8077, 4290, 1), -- Race
(8077, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dawn
(8078, 4290, 1), -- Race
(8078, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dawn
(8079, 4290, 1), -- Race
(8079, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dawn
(8080, 4290, 1), -- Race
(8080, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dawn
(8081, 4290, 1), -- Race
(8081, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dawn
(8082, 4290, 1), -- Race
(8082, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dawn
(8083, 4290, 1), -- Race
(8083, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dawn
(8084, 4290, 1), -- Race
(8084, 4045, 1), -- Resist Full Magic Attack
-- Dusk Priestess
(8085, 4290, 1), -- Race
(8085, 4045, 1), -- Resist Full Magic Attack
-- Dusk Priestess
(8086, 4290, 1), -- Race
(8086, 4045, 1), -- Resist Full Magic Attack
-- Dusk Priestess
(8087, 4290, 1), -- Race
(8087, 4045, 1), -- Resist Full Magic Attack
-- Dusk Priestess
(8088, 4290, 1), -- Race
(8088, 4045, 1), -- Resist Full Magic Attack
-- Dusk Priestess
(8089, 4290, 1), -- Race
(8089, 4045, 1), -- Resist Full Magic Attack
-- Dusk Priestess
(8090, 4290, 1), -- Race
(8090, 4045, 1), -- Resist Full Magic Attack
-- Dusk Priestess
(8091, 4290, 1), -- Race
(8091, 4045, 1), -- Resist Full Magic Attack
-- Black Marketeer of Mammon
(8092, 4290, 1), -- Race
(8092, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8093, 4290, 1), -- Race
(8093, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8094, 4291, 1), -- Race
(8094, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper Ziggurat
(8095, 4290, 1), -- Race
(8095, 4045, 1), -- Resist Full Magic Attack
(8095, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8096, 4290, 1), -- Race
(8096, 4045, 1), -- Resist Full Magic Attack
(8096, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8097, 4290, 1), -- Race
(8097, 4045, 1), -- Resist Full Magic Attack
(8097, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8098, 4290, 1), -- Race
(8098, 4045, 1), -- Resist Full Magic Attack
(8098, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8099, 4290, 1), -- Race
(8099, 4045, 1), -- Resist Full Magic Attack
(8099, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8100, 4290, 1), -- Race
(8100, 4045, 1), -- Resist Full Magic Attack
(8100, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8101, 4290, 1), -- Race
(8101, 4045, 1), -- Resist Full Magic Attack
(8101, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8102, 4290, 1), -- Race
(8102, 4045, 1), -- Resist Full Magic Attack
(8102, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8103, 4290, 1), -- Race
(8103, 4045, 1), -- Resist Full Magic Attack
(8103, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8104, 4290, 1), -- Race
(8104, 4045, 1), -- Resist Full Magic Attack
(8104, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8105, 4290, 1), -- Race
(8105, 4045, 1), -- Resist Full Magic Attack
(8105, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8106, 4290, 1), -- Race
(8106, 4045, 1), -- Resist Full Magic Attack
(8106, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8107, 4290, 1), -- Race
(8107, 4045, 1), -- Resist Full Magic Attack
(8107, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8108, 4290, 1), -- Race
(8108, 4045, 1), -- Resist Full Magic Attack
(8108, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8109, 4290, 1), -- Race
(8109, 4045, 1), -- Resist Full Magic Attack
(8109, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8110, 4290, 1), -- Race
(8110, 4045, 1), -- Resist Full Magic Attack
(8110, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Spirit
(8111, 4290, 1), -- Race
(8111, 4045, 1), -- Resist Full Magic Attack
(8111, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Spirit
(8112, 4290, 1), -- Race
(8112, 4045, 1), -- Resist Full Magic Attack
(8112, 4390, 1), -- NPC Abnormal Immunity
-- Merchant of Mammon
(8113, 4290, 1), -- Race
(8113, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper Ziggurat
(8114, 4290, 1), -- Race
(8114, 4045, 1), -- Resist Full Magic Attack
(8114, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8115, 4290, 1), -- Race
(8115, 4045, 1), -- Resist Full Magic Attack
(8115, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8116, 4290, 1), -- Race
(8116, 4045, 1), -- Resist Full Magic Attack
(8116, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8117, 4290, 1), -- Race
(8117, 4045, 1), -- Resist Full Magic Attack
(8117, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8118, 4290, 1), -- Race
(8118, 4045, 1), -- Resist Full Magic Attack
(8118, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8119, 4290, 1), -- Race
(8119, 4045, 1), -- Resist Full Magic Attack
(8119, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8120, 4290, 1), -- Race
(8120, 4045, 1), -- Resist Full Magic Attack
(8120, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8121, 4290, 1), -- Race
(8121, 4045, 1), -- Resist Full Magic Attack
(8121, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8122, 4290, 1), -- Race
(8122, 4045, 1), -- Resist Full Magic Attack
(8122, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8123, 4290, 1), -- Race
(8123, 4045, 1), -- Resist Full Magic Attack
(8123, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8124, 4290, 1), -- Race
(8124, 4045, 1), -- Resist Full Magic Attack
(8124, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper Ziggurat
(8125, 4290, 1), -- Race
(8125, 4045, 1), -- Resist Full Magic Attack
(8125, 4390, 1), -- NPC Abnormal Immunity
-- Blacksmith of Mammon
(8126, 4290, 1), -- Race
(8126, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8127, 4290, 1), -- Race
(8127, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8128, 4290, 1), -- Race
(8128, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8129, 4290, 1), -- Race
(8129, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8130, 4290, 1), -- Race
(8130, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8131, 4290, 1), -- Race
(8131, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8132, 4290, 1), -- Race
(8132, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8133, 4290, 1), -- Race
(8133, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8134, 4290, 1), -- Race
(8134, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8135, 4290, 1), -- Race
(8135, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8136, 4290, 1), -- Race
(8136, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8137, 4290, 1), -- Race
(8137, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8138, 4290, 1), -- Race
(8138, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8139, 4290, 1), -- Race
(8139, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8140, 4290, 1), -- Race
(8140, 4045, 1), -- Resist Full Magic Attack
-- Festival Guide
(8141, 4290, 1), -- Race
(8141, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8142, 4290, 1), -- Race
(8142, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8143, 4290, 1), -- Race
(8143, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8144, 4290, 1), -- Race
(8144, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8145, 4290, 1), -- Race
(8145, 4045, 1), -- Resist Full Magic Attack
-- Festival Witch
(8146, 4290, 1), -- Race
(8146, 4045, 1), -- Resist Full Magic Attack
-- Sobling
(8147, 4290, 1), -- Race
(8147, 4045, 1), -- Resist Full Magic Attack
-- Pirate's Chest
(8148, 4290, 1), -- Race
(8148, 4045, 1), -- Resist Full Magic Attack
(8148, 4390, 1), -- NPC Abnormal Immunity
-- Alchemist's Mixing Urn
(8149, 4290, 1), -- Race
(8149, 4045, 1), -- Resist Full Magic Attack
(8149, 4390, 1), -- NPC Abnormal Immunity
-- Carey
(8150, 4290, 1), -- Race
(8150, 4045, 1), -- Resist Full Magic Attack
-- Daniel
(8151, 4290, 1), -- Race
(8151, 4045, 1), -- Resist Full Magic Attack
-- Dianne
(8152, 4290, 1), -- Race
(8152, 4045, 1), -- Resist Full Magic Attack
-- Jacques
(8153, 4290, 1), -- Race
(8153, 4045, 1), -- Resist Full Magic Attack
-- Crissy
(8154, 4290, 1), -- Race
(8154, 4045, 1), -- Resist Full Magic Attack
-- Joff
(8155, 4290, 1), -- Race
(8155, 4045, 1), -- Resist Full Magic Attack
-- Albert
(8156, 4290, 1), -- Race
(8156, 4045, 1), -- Resist Full Magic Attack
-- Niels
(8157, 4290, 1), -- Race
(8157, 4045, 1), -- Resist Full Magic Attack
-- Korgen
(8158, 4290, 1), -- Race
(8158, 4045, 1), -- Resist Full Magic Attack
-- Rudy
(8159, 4290, 1), -- Race
(8159, 4045, 1), -- Resist Full Magic Attack
-- Dimaggio
(8160, 4290, 1), -- Race
(8160, 4045, 1), -- Resist Full Magic Attack
-- Gellar
(8161, 4290, 1), -- Race
(8161, 4045, 1), -- Resist Full Magic Attack
-- Grad
(8162, 4290, 1), -- Race
(8162, 4045, 1), -- Resist Full Magic Attack
-- Bryce
(8163, 4290, 1), -- Race
(8163, 4045, 1), -- Resist Full Magic Attack
-- Hodler
(8164, 4290, 1), -- Race
(8164, 4045, 1), -- Resist Full Magic Attack
-- Finrod
(8165, 4290, 1), -- Race
(8165, 4045, 1), -- Resist Full Magic Attack
-- Tate
(8166, 4290, 1), -- Race
(8166, 4045, 1), -- Resist Full Magic Attack
-- Kruger
(8167, 4290, 1), -- Race
(8167, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dawn
(8168, 4290, 1), -- Race
(8168, 4045, 1), -- Resist Full Magic Attack
-- Dusk Priestess
(8169, 4290, 1), -- Race
(8169, 4045, 1), -- Resist Full Magic Attack
-- Crest of Dawn
(8170, 4290, 1), -- Race
(8170, 4045, 1), -- Resist Full Magic Attack
(8170, 4390, 1), -- NPC Abnormal Immunity
-- Crest of Dusk
(8171, 4290, 1), -- Race
(8171, 4045, 1), -- Resist Full Magic Attack
(8171, 4390, 1), -- NPC Abnormal Immunity
-- Preacher of Doom
(8172, 4290, 1), -- Race
(8172, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8173, 4291, 1), -- Race
(8173, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8174, 4290, 1), -- Race
(8174, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8175, 4291, 1), -- Race
(8175, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8176, 4290, 1), -- Race
(8176, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8177, 4291, 1), -- Race
(8177, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8178, 4290, 1), -- Race
(8178, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8179, 4291, 1), -- Race
(8179, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8180, 4290, 1), -- Race
(8180, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8181, 4291, 1), -- Race
(8181, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8182, 4290, 1), -- Race
(8182, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8183, 4291, 1), -- Race
(8183, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8184, 4290, 1), -- Race
(8184, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8185, 4291, 1), -- Race
(8185, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8186, 4290, 1), -- Race
(8186, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8187, 4291, 1), -- Race
(8187, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8188, 4290, 1), -- Race
(8188, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8189, 4291, 1), -- Race
(8189, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8190, 4290, 1), -- Race
(8190, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8191, 4291, 1), -- Race
(8191, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8192, 4290, 1), -- Race
(8192, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8193, 4291, 1), -- Race
(8193, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8194, 4290, 1), -- Race
(8194, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8195, 4291, 1), -- Race
(8195, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8196, 4290, 1), -- Race
(8196, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8197, 4291, 1), -- Race
(8197, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8198, 4290, 1), -- Race
(8198, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8199, 4291, 1), -- Race
(8199, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8200, 4290, 1), -- Race
(8200, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8201, 4291, 1), -- Race
(8201, 4045, 1), -- Resist Full Magic Attack
-- Maximus
(8202, 4293, 1), -- Race
(8202, 4045, 1), -- Resist Full Magic Attack
-- Moon Dancer
(8203, 4293, 1), -- Race
(8203, 4045, 1), -- Resist Full Magic Attack
-- Georgio
(8204, 4293, 1), -- Race
(8204, 4045, 1), -- Resist Full Magic Attack
-- Katz
(8205, 4299, 1), -- Race
(8205, 4045, 1), -- Resist Full Magic Attack
-- Ten Ten
(8206, 4299, 1), -- Race
(8206, 4045, 1), -- Resist Full Magic Attack
-- Sardinia
(8207, 4299, 1), -- Race
(8207, 4045, 1), -- Resist Full Magic Attack
-- La Grange
(8208, 4299, 1), -- Race
(8208, 4045, 1), -- Resist Full Magic Attack
-- Misty Rain
(8209, 4293, 1), -- Race
(8209, 4045, 1), -- Resist Full Magic Attack
-- Race Track Gatekeeper
(8210, 4290, 1), -- Race
(8210, 4045, 1), -- Resist Full Magic Attack
-- Race Track Guide
(8211, 4290, 1), -- Race
(8211, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8212, 4293, 1), -- Race
(8212, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8213, 4293, 1), -- Race
(8213, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8214, 4293, 1), -- Race
(8214, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8215, 4293, 1), -- Race
(8215, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8216, 4293, 1), -- Race
(8216, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8217, 4293, 1), -- Race
(8217, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8218, 4293, 1), -- Race
(8218, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8219, 4293, 1), -- Race
(8219, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8220, 4293, 1), -- Race
(8220, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8221, 4293, 1), -- Race
(8221, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8222, 4293, 1), -- Race
(8222, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8223, 4293, 1), -- Race
(8223, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8224, 4293, 1), -- Race
(8224, 4045, 1), -- Resist Full Magic Attack
-- Arena Manager
(8225, 4290, 1), -- Race
(8225, 4045, 1), -- Resist Full Magic Attack
-- Arena Director
(8226, 4290, 1), -- Race
(8226, 4045, 1), -- Resist Full Magic Attack
-- Puss the Cat
(8227, 4293, 1), -- Race
(8227, 4045, 1), -- Resist Full Magic Attack
-- Roy the Cat
(8228, 4293, 1), -- Race
(8228, 4045, 1), -- Resist Full Magic Attack
-- Winnie the Cat
(8229, 4293, 1), -- Race
(8229, 4045, 1), -- Resist Full Magic Attack
-- Wendy the Cat
(8230, 4293, 1), -- Race
(8230, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8231, 4290, 1), -- Race
(8231, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8232, 4290, 1), -- Race
(8232, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8233, 4290, 1), -- Race
(8233, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8234, 4290, 1), -- Race
(8234, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8235, 4290, 1), -- Race
(8235, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8236, 4290, 1), -- Race
(8236, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8237, 4290, 1), -- Race
(8237, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8238, 4290, 1), -- Race
(8238, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8239, 4290, 1), -- Race
(8239, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8240, 4290, 1), -- Race
(8240, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8241, 4290, 1), -- Race
(8241, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8242, 4290, 1), -- Race
(8242, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8243, 4290, 1), -- Race
(8243, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8244, 4290, 1), -- Race
(8244, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8245, 4290, 1), -- Race
(8245, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8246, 4290, 1), -- Race
(8246, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8247, 4291, 1), -- Race
(8247, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8248, 4291, 1), -- Race
(8248, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8249, 4291, 1), -- Race
(8249, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8250, 4291, 1), -- Race
(8250, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8251, 4291, 1), -- Race
(8251, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8252, 4291, 1), -- Race
(8252, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8253, 4291, 1), -- Race
(8253, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8254, 4291, 1), -- Race
(8254, 4045, 1), -- Resist Full Magic Attack
-- Puss the Cat
(8255, 4293, 1), -- Race
(8255, 4045, 1), -- Resist Full Magic Attack
-- Leon
(8256, 4290, 1), -- Race
(8256, 4045, 1), -- Resist Full Magic Attack
-- Veronica
(8257, 4290, 1), -- Race
(8257, 4045, 1), -- Resist Full Magic Attack
-- Helmut
(8258, 4290, 1), -- Race
(8258, 4045, 1), -- Resist Full Magic Attack
-- Janne
(8259, 4290, 1), -- Race
(8259, 4045, 1), -- Resist Full Magic Attack
-- Judith
(8260, 4290, 1), -- Race
(8260, 4045, 1), -- Resist Full Magic Attack
-- Renee
(8261, 4290, 1), -- Race
(8261, 4045, 1), -- Resist Full Magic Attack
-- Rupert
(8262, 4290, 1), -- Race
(8262, 4045, 1), -- Resist Full Magic Attack
-- Liesel
(8263, 4290, 1), -- Race
(8263, 4045, 1), -- Resist Full Magic Attack
-- Olsun
(8264, 4290, 1), -- Race
(8264, 4045, 1), -- Resist Full Magic Attack
-- Annette
(8265, 4290, 1), -- Race
(8265, 4045, 1), -- Resist Full Magic Attack
-- Kaiser
(8266, 4293, 1), -- Race
(8266, 4045, 1), -- Resist Full Magic Attack
-- Lietta
(8267, 4290, 1), -- Race
(8267, 4045, 1), -- Resist Full Magic Attack
-- Hakon
(8268, 4290, 1), -- Race
(8268, 4045, 1), -- Resist Full Magic Attack
-- Mona
(8269, 4290, 1), -- Race
(8269, 4045, 1), -- Resist Full Magic Attack
-- Stefano
(8270, 4290, 1), -- Race
(8270, 4045, 1), -- Resist Full Magic Attack
-- Hilda
(8271, 4290, 1), -- Race
(8271, 4045, 1), -- Resist Full Magic Attack
-- Noel
(8272, 4290, 1), -- Race
(8272, 4045, 1), -- Resist Full Magic Attack
-- Borodin
(8273, 4290, 1), -- Race
(8273, 4045, 1), -- Resist Full Magic Attack
-- Fundin
(8274, 4290, 1), -- Race
(8274, 4045, 1), -- Resist Full Magic Attack
-- Tatiana
(8275, 4290, 1), -- Race
(8275, 4045, 1), -- Resist Full Magic Attack
-- Bernhard
(8276, 4290, 1), -- Race
(8276, 4045, 1), -- Resist Full Magic Attack
-- Felix
(8277, 4290, 1), -- Race
(8277, 4045, 1), -- Resist Full Magic Attack
-- Bronwyn
(8278, 4290, 1), -- Race
(8278, 4045, 1), -- Resist Full Magic Attack
-- Gregory
(8279, 4290, 1), -- Race
(8279, 4045, 1), -- Resist Full Magic Attack
-- Bastian
(8280, 4290, 1), -- Race
(8280, 4045, 1), -- Resist Full Magic Attack
-- Cerenas
(8281, 4290, 1), -- Race
(8281, 4045, 1), -- Resist Full Magic Attack
-- Justin
(8282, 4290, 1), -- Race
(8282, 4045, 1), -- Resist Full Magic Attack
-- Alminas
(8283, 4290, 1), -- Race
(8283, 4045, 1), -- Resist Full Magic Attack
-- Elena
(8284, 4290, 1), -- Race
(8284, 4045, 1), -- Resist Full Magic Attack
-- Samael
(8285, 4290, 1), -- Race
(8285, 4045, 1), -- Resist Full Magic Attack
-- Drakon
(8286, 4290, 1), -- Race
(8286, 4045, 1), -- Resist Full Magic Attack
-- Kamilen
(8287, 4290, 1), -- Race
(8287, 4045, 1), -- Resist Full Magic Attack
-- Aklan
(8288, 4290, 1), -- Race
(8288, 4045, 1), -- Resist Full Magic Attack
-- Lakan
(8289, 4290, 1), -- Race
(8289, 4045, 1), -- Resist Full Magic Attack
-- Skahi
(8290, 4290, 1), -- Race
(8290, 4045, 1), -- Resist Full Magic Attack
-- Terava
(8291, 4290, 1), -- Race
(8291, 4045, 1), -- Resist Full Magic Attack
-- Andrei
(8292, 4290, 1), -- Race
(8292, 4045, 1), -- Resist Full Magic Attack
-- Gunter
(8293, 4290, 1), -- Race
(8293, 4045, 1), -- Resist Full Magic Attack
-- Sven
(8294, 4290, 1), -- Race
(8294, 4045, 1), -- Resist Full Magic Attack
-- Henrik
(8295, 4290, 1), -- Race
(8295, 4045, 1), -- Resist Full Magic Attack
-- Cadmon
(8296, 4290, 1), -- Race
(8296, 4045, 1), -- Resist Full Magic Attack
-- Bayard
(8297, 4290, 1), -- Race
(8297, 4045, 1), -- Resist Full Magic Attack
-- Ulrich
(8298, 4290, 1), -- Race
(8298, 4045, 1), -- Resist Full Magic Attack
-- Eugen
(8299, 4290, 1), -- Race
(8299, 4045, 1), -- Resist Full Magic Attack
-- Drumond
(8300, 4290, 1), -- Race
(8300, 4045, 1), -- Resist Full Magic Attack
-- Nils
(8301, 4290, 1), -- Race
(8301, 4045, 1), -- Resist Full Magic Attack
-- Vladimir
(8302, 4290, 1), -- Race
(8302, 4045, 1), -- Resist Full Magic Attack
-- Alisha
(8303, 4290, 1), -- Race
(8303, 4045, 1), -- Resist Full Magic Attack
-- Astrid
(8304, 4290, 1), -- Race
(8304, 4045, 1), -- Resist Full Magic Attack
-- Candice
(8305, 4290, 1), -- Race
(8305, 4045, 1), -- Resist Full Magic Attack
-- Natasha
(8306, 4290, 1), -- Race
(8306, 4045, 1), -- Resist Full Magic Attack
-- Weber
(8307, 4290, 1), -- Race
(8307, 4045, 1), -- Resist Full Magic Attack
-- Achim
(8308, 4290, 1), -- Race
(8308, 4045, 1), -- Resist Full Magic Attack
-- Woods
(8309, 4290, 1), -- Race
(8309, 4045, 1), -- Resist Full Magic Attack
-- Rafael
(8310, 4299, 1), -- Race
(8310, 4045, 1), -- Resist Full Magic Attack
-- Hugin
(8311, 4290, 1), -- Race
(8311, 4045, 1), -- Resist Full Magic Attack
-- Durin
(8312, 4290, 1), -- Race
(8312, 4045, 1), -- Resist Full Magic Attack
-- Lunin
(8313, 4290, 1), -- Race
(8313, 4045, 1), -- Resist Full Magic Attack
-- Donal
(8314, 4290, 1), -- Race
(8314, 4045, 1), -- Resist Full Magic Attack
-- Daisy
(8315, 4290, 1), -- Race
(8315, 4045, 1), -- Resist Full Magic Attack
-- Vincenz
(8316, 4290, 1), -- Race
(8316, 4045, 1), -- Resist Full Magic Attack
-- Lombert
(8317, 4290, 1), -- Race
(8317, 4045, 1), -- Resist Full Magic Attack
-- Greta
(8318, 4290, 1), -- Race
(8318, 4045, 1), -- Resist Full Magic Attack
-- Hans
(8319, 4290, 1), -- Race
(8319, 4045, 1), -- Resist Full Magic Attack
-- Ilyana
(8320, 4290, 1), -- Race
(8320, 4045, 1), -- Resist Full Magic Attack
-- Siegmund
(8321, 4290, 1), -- Race
(8321, 4045, 1), -- Resist Full Magic Attack
-- Erian
(8322, 4290, 1), -- Race
(8322, 4045, 1), -- Resist Full Magic Attack
-- Beryl
(8323, 4290, 1), -- Race
(8323, 4045, 1), -- Resist Full Magic Attack
-- Andromeda
(8324, 4290, 1), -- Race
(8324, 4045, 1), -- Resist Full Magic Attack
-- Themis
(8325, 4290, 1), -- Race
(8325, 4045, 1), -- Resist Full Magic Attack
-- Lambac
(8326, 4290, 1), -- Race
(8326, 4045, 1), -- Resist Full Magic Attack
-- Tazki
(8327, 4290, 1), -- Race
(8327, 4045, 1), -- Resist Full Magic Attack
-- Innocentin
(8328, 4290, 1), -- Race
(8328, 4045, 1), -- Resist Full Magic Attack
-- Eliyah
(8329, 4290, 1), -- Race
(8329, 4045, 1), -- Resist Full Magic Attack
-- Wagner
(8330, 4290, 1), -- Race
(8330, 4045, 1), -- Resist Full Magic Attack
--  Valdis
(8331, 4290, 1), -- Race
(8331, 4045, 1), -- Resist Full Magic Attack
-- Amelia
(8332, 4290, 1), -- Race
(8332, 4045, 1), -- Resist Full Magic Attack
-- Rumiel
(8333, 4290, 1), -- Race
(8333, 4045, 1), -- Resist Full Magic Attack
-- Tifaren
(8334, 4290, 1), -- Race
(8334, 4045, 1), -- Resist Full Magic Attack
-- Kayan
(8335, 4290, 1), -- Race
(8335, 4045, 1), -- Resist Full Magic Attack
-- Rahorakti
(8336, 4290, 1), -- Race
(8336, 4045, 1), -- Resist Full Magic Attack
-- Mekara
(8337, 4290, 1), -- Race
(8337, 4045, 1), -- Resist Full Magic Attack
-- Anton
(8338, 4290, 1), -- Race
(8338, 4045, 1), -- Resist Full Magic Attack
-- Hakran
(8339, 4290, 1), -- Race
(8339, 4045, 1), -- Resist Full Magic Attack
-- Mathias
(8340, 4290, 1), -- Race
(8340, 4045, 1), -- Resist Full Magic Attack
-- Richtor
(8341, 4290, 1), -- Race
(8341, 4045, 1), -- Resist Full Magic Attack
-- Dimitri
(8342, 4290, 1), -- Race
(8342, 4045, 1), -- Resist Full Magic Attack
-- Bellard
(8343, 4290, 1), -- Race
(8343, 4045, 1), -- Resist Full Magic Attack
-- Schmidt
(8344, 4290, 1), -- Race
(8344, 4045, 1), -- Resist Full Magic Attack
-- Ian
(8345, 4290, 1), -- Race
(8345, 4045, 1), -- Resist Full Magic Attack
-- Sirius
(8346, 4290, 1), -- Race
(8346, 4045, 1), -- Resist Full Magic Attack
-- Burke
(8347, 4290, 1), -- Race
(8347, 4045, 1), -- Resist Full Magic Attack
-- Agripel
(8348, 4290, 1), -- Race
(8348, 4045, 1), -- Resist Full Magic Attack
-- Benedict
(8349, 4290, 1), -- Race
(8349, 4045, 1), -- Resist Full Magic Attack
-- Dominic
(8350, 4290, 1), -- Race
(8350, 4045, 1), -- Resist Full Magic Attack
-- Volker
(8351, 4290, 1), -- Race
(8351, 4045, 1), -- Resist Full Magic Attack
-- Lambert
(8352, 4290, 1), -- Race
(8352, 4045, 1), -- Resist Full Magic Attack
-- Gerard
(8353, 4290, 1), -- Race
(8353, 4045, 1), -- Resist Full Magic Attack
-- Volfrem
(8354, 4290, 1), -- Race
(8354, 4045, 1), -- Resist Full Magic Attack
-- Kalmer
(8355, 4290, 1), -- Race
(8355, 4045, 1), -- Resist Full Magic Attack
-- Remy
(8356, 4290, 1), -- Race
(8356, 4045, 1), -- Resist Full Magic Attack
-- Leandro
(8357, 4290, 1), -- Race
(8357, 4045, 1), -- Resist Full Magic Attack
-- Kasiel
(8358, 4290, 1), -- Race
(8358, 4045, 1), -- Resist Full Magic Attack
-- Jaradine
(8359, 4290, 1), -- Race
(8359, 4045, 1), -- Resist Full Magic Attack
-- Alhena
(8360, 4290, 1), -- Race
(8360, 4045, 1), -- Resist Full Magic Attack
-- Kreed
(8361, 4290, 1), -- Race
(8361, 4045, 1), -- Resist Full Magic Attack
-- Tate
(8362, 4290, 1), -- Race
(8362, 4045, 1), -- Resist Full Magic Attack
-- Rogin
(8363, 4290, 1), -- Race
(8363, 4045, 1), -- Resist Full Magic Attack
-- Rokar
(8364, 4290, 1), -- Race
(8364, 4045, 1), -- Resist Full Magic Attack
-- Yakand
(8365, 4290, 1), -- Race
(8365, 4045, 1), -- Resist Full Magic Attack
-- Food Seller
(8366, 4290, 1), -- Race
(8366, 4045, 1), -- Resist Full Magic Attack
-- Voice of Glory
(8367, 4297, 1), -- Race
(8367, 4045, 1), -- Resist Full Magic Attack
-- Mikellan
(8368, 4290, 1), -- Race
(8368, 4045, 1), -- Resist Full Magic Attack
-- Mennon
(8369, 4290, 1), -- Race
(8369, 4045, 1), -- Resist Full Magic Attack
-- Kadun Zu Ketra
(8370, 4295, 1), -- Race
(8370, 4045, 1), -- Resist Full Magic Attack
-- Wahkan
(8371, 4295, 1), -- Race
(8371, 4045, 1), -- Resist Full Magic Attack
-- Asefa
(8372, 4295, 1), -- Race
(8372, 4045, 1), -- Resist Full Magic Attack
(8372, 4359, 2), -- Focus
(8372, 4360, 2), -- Death Whisper
(8372, 4345, 3), -- Might
(8372, 4355, 2), -- Acumen
(8372, 4352, 1), -- Berserker Spirit
(8372, 4354, 2), -- Vampiric Rage
(8372, 4356, 1), -- Empower
(8372, 4357, 2), -- Haste
-- Atan
(8373, 4295, 1), -- Race
(8373, 4045, 1), -- Resist Full Magic Attack
-- Jaff
(8374, 4295, 1), -- Race
(8374, 4045, 1), -- Resist Full Magic Attack
-- Jumara
(8375, 4295, 1), -- Race
(8375, 4045, 1), -- Resist Full Magic Attack
-- Kurfa
(8376, 4295, 1), -- Race
(8376, 4045, 1), -- Resist Full Magic Attack
-- Ashas Varka Durai
(8377, 4295, 1), -- Race
(8377, 4045, 1), -- Resist Full Magic Attack
-- Naran Ashanuk
(8378, 4295, 1), -- Race
(8378, 4045, 1), -- Resist Full Magic Attack
-- Udan Mardui
(8379, 4295, 1), -- Race
(8379, 4045, 1), -- Resist Full Magic Attack
(8379, 4359, 2), -- Focus
(8379, 4360, 2), -- Death Whisper
(8379, 4345, 3), -- Might
(8379, 4355, 2), -- Acumen
(8379, 4352, 1), -- Berserker Spirit
(8379, 4354, 2), -- Vampiric Rage
(8379, 4356, 1), -- Empower
(8379, 4357, 2), -- Haste
-- Diyabu
(8380, 4295, 1), -- Race
(8380, 4045, 1), -- Resist Full Magic Attack
-- Hagos
(8381, 4295, 1), -- Race
(8381, 4045, 1), -- Resist Full Magic Attack
-- Shikon
(8382, 4295, 1), -- Race
(8382, 4045, 1), -- Resist Full Magic Attack
-- Teranu Mardui
(8383, 4295, 1), -- Race
(8383, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper of Fire Dragon
(8384, 4290, 1), -- Race
(8384, 4045, 1), -- Resist Full Magic Attack
-- Heart of Volcano
(8385, 4290, 1), -- Race
(8385, 4045, 1), -- Resist Full Magic Attack
(8385, 4390, 1), -- NPC Abnormal Immunity
-- Violet
(8386, 4290, 1), -- Race
(8386, 4045, 1), -- Resist Full Magic Attack
-- Kurstin
(8387, 4290, 1), -- Race
(8387, 4045, 1), -- Resist Full Magic Attack
-- Mina
(8388, 4290, 1), -- Race
(8388, 4045, 1), -- Resist Full Magic Attack
-- Dorian
(8389, 4290, 1), -- Race
(8389, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Boy A
(8390, 4290, 1), -- Race
(8390, 4045, 1), -- Resist Full Magic Attack
-- Dummy  - Boy B
(8391, 4290, 1), -- Race
(8391, 4045, 1), -- Resist Full Magic Attack
-- Dummy  - Boy C
(8392, 4290, 1), -- Race
(8392, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Boy D
(8393, 4290, 1), -- Race
(8393, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Boy E
(8394, 4290, 1), -- Race
(8394, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Lady F
(8395, 4290, 1), -- Race
(8395, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Lady G
(8396, 4290, 1), -- Race
(8396, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Lady H
(8397, 4290, 1), -- Race
(8397, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Lady I
(8398, 4290, 1), -- Race
(8398, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Lady J
(8399, 4290, 1), -- Race
(8399, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Sword Guard
(8400, 4290, 1), -- Race
(8400, 4045, 1), -- Resist Full Magic Attack
-- Dummy - Bow Guard
(8401, 4290, 1), -- Race
(8401, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(8402, 4290, 1), -- Race
(8402, 4045, 1), -- Resist Full Magic Attack
-- Manor Manager
(8403, 4290, 1), -- Race
(8403, 4045, 1), -- Resist Full Magic Attack
-- Monster Race Guide
(8404, 4290, 1), -- Race
(8404, 4045, 1), -- Resist Full Magic Attack
-- Monster Race Guide
(8405, 4290, 1), -- Race
(8405, 4045, 1), -- Resist Full Magic Attack
-- Monster Race Guide
(8406, 4290, 1), -- Race
(8406, 4045, 1), -- Resist Full Magic Attack
-- Monster Race Guide
(8407, 4290, 1), -- Race
(8407, 4045, 1), -- Resist Full Magic Attack
-- Monster Race Guide
(8408, 4290, 1), -- Race
(8408, 4045, 1), -- Resist Full Magic Attack
-- Monster Race Guide
(8409, 4290, 1), -- Race
(8409, 4045, 1), -- Resist Full Magic Attack
-- Monster Race Guide
(8410, 4290, 1), -- Race
(8410, 4045, 1), -- Resist Full Magic Attack
-- Monster Race Guide
(8411, 4290, 1), -- Race
(8411, 4045, 1), -- Resist Full Magic Attack
-- Monster Race Guide
(8412, 4290, 1), -- Race
(8412, 4045, 1), -- Resist Full Magic Attack
-- Antonio
(8413, 4290, 1), -- Race
(8413, 4045, 1), -- Resist Full Magic Attack
-- Lynn
(8414, 4290, 1), -- Race
(8414, 4045, 1), -- Resist Full Magic Attack
-- Triya
(8415, 4290, 1), -- Race
(8415, 4045, 1), -- Resist Full Magic Attack
-- Aren
(8416, 4290, 1), -- Race
(8416, 4045, 1), -- Resist Full Magic Attack
-- Berynel
(8417, 4290, 1), -- Race
(8417, 4045, 1), -- Resist Full Magic Attack
-- Rouke
(8418, 4290, 1), -- Race
(8418, 4045, 1), -- Resist Full Magic Attack
-- Lorel
(8419, 4290, 1), -- Race
(8419, 4045, 1), -- Resist Full Magic Attack
-- Tomanel
(8420, 4290, 1), -- Race
(8420, 4045, 1), -- Resist Full Magic Attack
-- Ratriya
(8421, 4290, 1), -- Race
(8421, 4045, 1), -- Resist Full Magic Attack
-- Migel
(8422, 4290, 1), -- Race
(8422, 4045, 1), -- Resist Full Magic Attack
-- Romas
(8423, 4290, 1), -- Race
(8423, 4045, 1), -- Resist Full Magic Attack
-- Shantra
(8424, 4290, 1), -- Race
(8424, 4045, 1), -- Resist Full Magic Attack
-- Koram
(8425, 4290, 1), -- Race
(8425, 4045, 1), -- Resist Full Magic Attack
-- Jaka
(8426, 4290, 1), -- Race
(8426, 4045, 1), -- Resist Full Magic Attack
-- Urgal
(8427, 4290, 1), -- Race
(8427, 4045, 1), -- Resist Full Magic Attack
-- Shafa
(8428, 4290, 1), -- Race
(8428, 4045, 1), -- Resist Full Magic Attack
-- Shitara
(8429, 4290, 1), -- Race
(8429, 4045, 1), -- Resist Full Magic Attack
-- Donai
(8430, 4290, 1), -- Race
(8430, 4045, 1), -- Resist Full Magic Attack
-- Karai
(8431, 4290, 1), -- Race
(8431, 4045, 1), -- Resist Full Magic Attack
-- Reeya
(8432, 4290, 1), -- Race
(8432, 4045, 1), -- Resist Full Magic Attack
-- Ronaldo
(8433, 4290, 1), -- Race
(8433, 4045, 1), -- Resist Full Magic Attack
-- Shaling
(8434, 4290, 1), -- Race
(8434, 4045, 1), -- Resist Full Magic Attack
-- Daeger
(8435, 4290, 1), -- Race
(8435, 4045, 1), -- Resist Full Magic Attack
-- Dani
(8436, 4290, 1), -- Race
(8436, 4045, 1), -- Resist Full Magic Attack
-- Luka
(8437, 4290, 1), -- Race
(8437, 4045, 1), -- Resist Full Magic Attack
-- Lara
(8438, 4290, 1), -- Race
(8438, 4045, 1), -- Resist Full Magic Attack
-- Rogen
(8439, 4290, 1), -- Race
(8439, 4045, 1), -- Resist Full Magic Attack
-- Lanna
(8440, 4290, 1), -- Race
(8440, 4045, 1), -- Resist Full Magic Attack
-- Gordo
(8441, 4290, 1), -- Race
(8441, 4045, 1), -- Resist Full Magic Attack
-- Kiki
(8442, 4290, 1), -- Race
(8442, 4045, 1), -- Resist Full Magic Attack
-- Raban
(8443, 4290, 1), -- Race
(8443, 4045, 1), -- Resist Full Magic Attack
-- Cona
(8444, 4290, 1), -- Race
(8444, 4045, 1), -- Resist Full Magic Attack
-- Shutner
(8445, 4290, 1), -- Race
(8445, 4045, 1), -- Resist Full Magic Attack
-- Hadley
(8446, 4290, 1), -- Race
(8446, 4045, 1), -- Resist Full Magic Attack
-- Rosconne
(8447, 4290, 1), -- Race
(8447, 4045, 1), -- Resist Full Magic Attack
-- Stewart
(8448, 4290, 1), -- Race
(8448, 4045, 1), -- Resist Full Magic Attack
-- Theobolt
(8449, 4290, 1), -- Race
(8449, 4045, 1), -- Resist Full Magic Attack
-- Theron
(8450, 4290, 1), -- Race
(8450, 4045, 1), -- Resist Full Magic Attack
-- Trey
(8451, 4290, 1), -- Race
(8451, 4045, 1), -- Resist Full Magic Attack
-- Ghost of Wigoth
(8452, 4290, 1), -- Race
(8452, 4045, 1), -- Resist Full Magic Attack
-- Nameless Soul
(8453, 4290, 1), -- Race
(8453, 4045, 1), -- Resist Full Magic Attack
-- Ghost of Wigoth
(8454, 4290, 1), -- Race
(8454, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8455, 4291, 1), -- Race
(8455, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8456, 4291, 1), -- Race
(8456, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8457, 4291, 1), -- Race
(8457, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8458, 4291, 1), -- Race
(8458, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8459, 4291, 1), -- Race
(8459, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8460, 4291, 1), -- Race
(8460, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8461, 4291, 1), -- Race
(8461, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8462, 4291, 1), -- Race
(8462, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8463, 4291, 1), -- Race
(8463, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8464, 4291, 1), -- Race
(8464, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8465, 4291, 1), -- Race
(8465, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8466, 4291, 1), -- Race
(8466, 4045, 1), -- Resist Full Magic Attack
-- Key Box
(8467, 4291, 1), -- Race
(8467, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8468, 4291, 1), -- Race
(8468, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8469, 4291, 1), -- Race
(8469, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8470, 4291, 1), -- Race
(8470, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8471, 4291, 1), -- Race
(8471, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8472, 4291, 1), -- Race
(8472, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8473, 4291, 1), -- Race
(8473, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8474, 4291, 1), -- Race
(8474, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8475, 4291, 1), -- Race
(8475, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8476, 4291, 1), -- Race
(8476, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8477, 4291, 1), -- Race
(8477, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8478, 4291, 1), -- Race
(8478, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8479, 4291, 1), -- Race
(8479, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8480, 4291, 1), -- Race
(8480, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8481, 4291, 1), -- Race
(8481, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8482, 4291, 1), -- Race
(8482, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8483, 4291, 1), -- Race
(8483, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8484, 4291, 1), -- Race
(8484, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8485, 4291, 1), -- Race
(8485, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8486, 4291, 1), -- Race
(8486, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Box
(8487, 4291, 1), -- Race
(8487, 4045, 1), -- Resist Full Magic Attack
-- Rift Post Recruit
(8488, 4290, 1), -- Race
(8488, 4045, 1), -- Resist Full Magic Attack
-- Rift Post Soldier
(8489, 4290, 1), -- Race
(8489, 4045, 1), -- Resist Full Magic Attack
-- Rift Post Officer
(8490, 4290, 1), -- Race
(8490, 4045, 1), -- Resist Full Magic Attack
-- Rift Post Captain
(8491, 4290, 1), -- Race
(8491, 4045, 1), -- Resist Full Magic Attack
-- Rift Post Commander
(8492, 4290, 1), -- Race
(8492, 4045, 1), -- Resist Full Magic Attack
-- Rift Post Hero
(8493, 4290, 1), -- Race
(8493, 4045, 1), -- Resist Full Magic Attack
-- Dimension Keeper
(8494, 4291, 1), -- Race
(8494, 4045, 1), -- Resist Full Magic Attack
(8494, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8495, 4291, 1), -- Race
(8495, 4045, 1), -- Resist Full Magic Attack
(8495, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8496, 4291, 1), -- Race
(8496, 4045, 1), -- Resist Full Magic Attack
(8496, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8497, 4291, 1), -- Race
(8497, 4045, 1), -- Resist Full Magic Attack
(8497, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8498, 4291, 1), -- Race
(8498, 4045, 1), -- Resist Full Magic Attack
(8498, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8499, 4291, 1), -- Race
(8499, 4045, 1), -- Resist Full Magic Attack
(8499, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8500, 4291, 1), -- Race
(8500, 4045, 1), -- Resist Full Magic Attack
(8500, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8501, 4291, 1), -- Race
(8501, 4045, 1), -- Resist Full Magic Attack
(8501, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8502, 4291, 1), -- Race
(8502, 4045, 1), -- Resist Full Magic Attack
(8502, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8503, 4291, 1), -- Race
(8503, 4045, 1), -- Resist Full Magic Attack
(8503, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8504, 4291, 1), -- Race
(8504, 4045, 1), -- Resist Full Magic Attack
(8504, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8505, 4291, 1), -- Race
(8505, 4045, 1), -- Resist Full Magic Attack
(8505, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8506, 4291, 1), -- Race
(8506, 4045, 1), -- Resist Full Magic Attack
(8506, 4390, 1), -- NPC Abnormal Immunity
-- Dimension Keeper
(8507, 4291, 1), -- Race
(8507, 4045, 1), -- Resist Full Magic Attack
(8507, 4390, 1), -- NPC Abnormal Immunity
-- Altar of Saints
(8508, 4290, 1), -- Race
(8508, 4045, 1), -- Resist Full Magic Attack
(8508, 4390, 1), -- NPC Abnormal Immunity
-- Altar of Saints
(8509, 4290, 1), -- Race
(8509, 4045, 1), -- Resist Full Magic Attack
(8509, 4390, 1), -- NPC Abnormal Immunity
-- Altar of Saints
(8510, 4290, 1), -- Race
(8510, 4045, 1), -- Resist Full Magic Attack
(8510, 4390, 1), -- NPC Abnormal Immunity
-- Altar of Saints
(8511, 4290, 1), -- Race
(8511, 4045, 1), -- Resist Full Magic Attack
(8511, 4390, 1), -- NPC Abnormal Immunity
-- Evil Altar
(8512, 4290, 1), -- Race
(8512, 4045, 1), -- Resist Full Magic Attack
(8512, 4390, 1), -- NPC Abnormal Immunity
-- Evil Altar
(8513, 4290, 1), -- Race
(8513, 4045, 1), -- Resist Full Magic Attack
(8513, 4390, 1), -- NPC Abnormal Immunity
-- Evil Altar
(8514, 4290, 1), -- Race
(8514, 4045, 1), -- Resist Full Magic Attack
(8514, 4390, 1), -- NPC Abnormal Immunity
-- Evil Altar
(8515, 4290, 1), -- Race
(8515, 4045, 1), -- Resist Full Magic Attack
(8515, 4390, 1), -- NPC Abnormal Immunity
-- Evil Altar
(8516, 4290, 1), -- Race
(8516, 4045, 1), -- Resist Full Magic Attack
(8516, 4390, 1), -- NPC Abnormal Immunity
-- Hierarch
(8517, 4290, 1), -- Race
(8517, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Necromancer
(8518, 4290, 1), -- Race
(8518, 4045, 1), -- Resist Full Magic Attack
-- Enfeux
(8519, 4290, 1), -- Race
(8519, 4045, 1), -- Resist Full Magic Attack
(8519, 4390, 1), -- NPC Abnormal Immunity
-- Leikar
(8520, 4290, 1), -- Race
(8520, 4045, 1), -- Resist Full Magic Attack
-- Jeremy
(8521, 4290, 1), -- Race
(8521, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Wizard
(8522, 4290, 1), -- Race
(8522, 4045, 1), -- Resist Full Magic Attack
-- Tombstone
(8523, 4290, 1), -- Race
(8523, 4045, 1), -- Resist Full Magic Attack
(8523, 4390, 1), -- NPC Abnormal Immunity
-- Ghost of von Hellmann
(8524, 4290, 1), -- Race
(8524, 4045, 1), -- Resist Full Magic Attack
-- Ghost of von Hellmann's Page
(8525, 4290, 1), -- Race
(8525, 4045, 1), -- Resist Full Magic Attack
-- Broken Bookshelf
(8526, 4290, 1), -- Race
(8526, 4045, 1), -- Resist Full Magic Attack
(8526, 4390, 1), -- NPC Abnormal Immunity
-- Well
(8527, 4290, 1), -- Race
(8527, 4045, 1), -- Resist Full Magic Attack
(8527, 4390, 1), -- NPC Abnormal Immunity
-- Ghost of Priest
(8528, 4290, 1), -- Race
(8528, 4045, 1), -- Resist Full Magic Attack
-- Ghost of Adventurer
(8529, 4290, 1), -- Race
(8529, 4045, 1), -- Resist Full Magic Attack
-- Box
(8530, 4291, 1), -- Race
(8530, 4045, 1), -- Resist Full Magic Attack
-- Tombstone
(8531, 4290, 1), -- Race
(8531, 4045, 1), -- Resist Full Magic Attack
(8531, 4390, 1), -- NPC Abnormal Immunity
-- Maid of Lidia
(8532, 4298, 1), -- Race
(8532, 4045, 1), -- Resist Full Magic Attack
-- Broken Bookshelf
(8533, 4290, 1), -- Race
(8533, 4045, 1), -- Resist Full Magic Attack
(8533, 4390, 1), -- NPC Abnormal Immunity
-- Broken Bookshelf
(8534, 4290, 1), -- Race
(8534, 4045, 1), -- Resist Full Magic Attack
(8534, 4390, 1), -- NPC Abnormal Immunity
-- Broken Bookshelf
(8535, 4290, 1), -- Race
(8535, 4045, 1), -- Resist Full Magic Attack
(8535, 4390, 1), -- NPC Abnormal Immunity
-- Coffin
(8536, 4291, 1), -- Race
(8536, 4045, 1), -- Resist Full Magic Attack
-- Tunatun
(8537, 4290, 1), -- Race
(8537, 4045, 1), -- Resist Full Magic Attack
-- Ghost of Adventurer
(8538, 4290, 1), -- Race
(8538, 4045, 1), -- Resist Full Magic Attack
-- Vulcan
(8539, 4290, 1), -- Race
(8539, 4045, 1), -- Resist Full Magic Attack
-- Klein
(8540, 4290, 1), -- Race
(8540, 4045, 1), -- Resist Full Magic Attack
-- Daimon's Altar
(8541, 4290, 1), -- Race
(8541, 4045, 1), -- Resist Full Magic Attack
(8541, 4390, 1), -- NPC Abnormal Immunity
-- Yeti's Table
(8542, 4290, 1), -- Race
(8542, 4045, 1), -- Resist Full Magic Attack
(8542, 4390, 1), -- NPC Abnormal Immunity
-- Pulin
(8543, 4290, 1), -- Race
(8543, 4045, 1), -- Resist Full Magic Attack
-- Naff
(8544, 4290, 1), -- Race
(8544, 4045, 1), -- Resist Full Magic Attack
-- Crocus
(8545, 4290, 1), -- Race
(8545, 4045, 1), -- Resist Full Magic Attack
-- Kuber
(8546, 4290, 1), -- Race
(8546, 4045, 1), -- Resist Full Magic Attack
-- Beorin
(8547, 4290, 1), -- Race
(8547, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Ancient Tablet
(8548, 4290, 1), -- Race
(8548, 4045, 1), -- Resist Full Magic Attack
(8548, 4390, 1), -- NPC Abnormal Immunity
-- Mysterious Ancient Tablet
(8549, 4290, 1), -- Race
(8549, 4045, 1), -- Resist Full Magic Attack
(8549, 4390, 1), -- NPC Abnormal Immunity
-- Mysterious Ancient Tablet
(8550, 4290, 1), -- Race
(8550, 4045, 1), -- Resist Full Magic Attack
(8550, 4390, 1), -- NPC Abnormal Immunity
-- Mysterious Ancient Tablet
(8551, 4290, 1), -- Race
(8551, 4045, 1), -- Resist Full Magic Attack
(8551, 4390, 1), -- NPC Abnormal Immunity
-- Mysterious Ancient Tablet
(8552, 4290, 1), -- Race
(8552, 4045, 1), -- Resist Full Magic Attack
(8552, 4390, 1), -- NPC Abnormal Immunity
-- Pierce
(8553, 4290, 1), -- Race
(8553, 4045, 1), -- Resist Full Magic Attack
-- Kahman
(8554, 4290, 1), -- Race
(8554, 4045, 1), -- Resist Full Magic Attack
-- Abercrombie
(8555, 4290, 1), -- Race
(8555, 4045, 1), -- Resist Full Magic Attack
-- Selina
(8556, 4290, 1), -- Race
(8556, 4045, 1), -- Resist Full Magic Attack
(8556, 4359, 2), -- Focus
(8556, 4360, 2), -- Death Whisper
(8556, 4345, 3), -- Might
(8556, 4355, 2), -- Acumen
(8556, 4352, 1), -- Berserker Spirit
(8556, 4354, 2), -- Vampiric Rage
(8556, 4356, 1), -- Empower
(8556, 4357, 2), -- Haste
-- Mercenary Sentry
(8557, 4290, 1), -- Race
(8557, 4045, 1), -- Resist Full Magic Attack
-- Ketra's Holy Altar
(8558, 4290, 1), -- Race
(8558, 4045, 1), -- Resist Full Magic Attack
(8558, 4390, 1), -- NPC Abnormal Immunity
-- Box of Asefa
(8559, 4290, 1), -- Race
(8559, 4045, 1), -- Resist Full Magic Attack
(8559, 4390, 1), -- NPC Abnormal Immunity
-- Varka's Holy Altar
(8560, 4290, 1), -- Race
(8560, 4045, 1), -- Resist Full Magic Attack
(8560, 4390, 1), -- NPC Abnormal Immunity
-- Udan Mardui's Box
(8561, 4290, 1), -- Race
(8561, 4045, 1), -- Resist Full Magic Attack
(8561, 4390, 1), -- NPC Abnormal Immunity
-- Klufe
(8562, 4290, 1), -- Race
(8562, 4045, 1), -- Resist Full Magic Attack
-- Perelin
(8563, 4290, 1), -- Race
(8563, 4045, 1), -- Resist Full Magic Attack
-- Mishini
(8564, 4290, 1), -- Race
(8564, 4045, 1), -- Resist Full Magic Attack
-- Ogord
(8565, 4290, 1), -- Race
(8565, 4045, 1), -- Resist Full Magic Attack
-- Ropfi
(8566, 4290, 1), -- Race
(8566, 4045, 1), -- Resist Full Magic Attack
-- Bleaker
(8567, 4290, 1), -- Race
(8567, 4045, 1), -- Resist Full Magic Attack
-- Pamfus
(8568, 4290, 1), -- Race
(8568, 4045, 1), -- Resist Full Magic Attack
-- Cyano
(8569, 4290, 1), -- Race
(8569, 4045, 1), -- Resist Full Magic Attack
-- Lanosco
(8570, 4290, 1), -- Race
(8570, 4045, 1), -- Resist Full Magic Attack
-- Hufs
(8571, 4290, 1), -- Race
(8571, 4045, 1), -- Resist Full Magic Attack
-- O'Fulle
(8572, 4290, 1), -- Race
(8572, 4045, 1), -- Resist Full Magic Attack
-- Monakan
(8573, 4290, 1), -- Race
(8573, 4045, 1), -- Resist Full Magic Attack
-- Willie
(8574, 4290, 1), -- Race
(8574, 4045, 1), -- Resist Full Magic Attack
-- Litulon
(8575, 4290, 1), -- Race
(8575, 4045, 1), -- Resist Full Magic Attack
-- Berix
(8576, 4290, 1), -- Race
(8576, 4045, 1), -- Resist Full Magic Attack
-- Linnaeus
(8577, 4290, 1), -- Race
(8577, 4045, 1), -- Resist Full Magic Attack
-- Hilgendorf
(8578, 4290, 1), -- Race
(8578, 4045, 1), -- Resist Full Magic Attack
-- Klaus
(8579, 4290, 1), -- Race
(8579, 4045, 1), -- Resist Full Magic Attack
-- Galadrid
(8580, 4290, 1), -- Race
(8580, 4045, 1), -- Resist Full Magic Attack
-- Anastia
(8581, 4290, 1), -- Race
(8581, 4045, 1), -- Resist Full Magic Attack
-- Mordred
(8582, 4290, 1), -- Race
(8582, 4045, 1), -- Resist Full Magic Attack
-- Feynn
(8583, 4290, 1), -- Race
(8583, 4045, 1), -- Resist Full Magic Attack
-- Valentine
(8584, 4290, 1), -- Race
(8584, 4045, 1), -- Resist Full Magic Attack
-- Sparky the Cat
(8585, 4293, 1), -- Race
(8585, 4045, 1), -- Resist Full Magic Attack
-- Sparky the Cat
(8586, 4293, 1), -- Race
(8586, 4045, 1), -- Resist Full Magic Attack
-- Gedrik
(8587, 4290, 1), -- Race
(8587, 4045, 1), -- Resist Full Magic Attack
-- Agnes
(8588, 4290, 1), -- Race
(8588, 4045, 1), -- Resist Full Magic Attack
-- Duda-Mara Totem Spirit 
(8589, 4301, 1), -- Race
(8589, 4045, 1), -- Resist Full Magic Attack
-- Truth Scholar Devianne
(8590, 4290, 1), -- Race
(8590, 4045, 1), -- Resist Full Magic Attack
-- Sunset Guide Luna
(8591, 4290, 1), -- Race
(8591, 4045, 1), -- Resist Full Magic Attack
-- Telson
(8592, 4290, 1), -- Race
(8592, 4045, 1), -- Resist Full Magic Attack
-- Dorothy
(8593, 4291, 1), -- Race
(8593, 4045, 1), -- Resist Full Magic Attack
-- Mond
(8594, 4290, 1), -- Race
(8594, 4045, 1), -- Resist Full Magic Attack
-- Leona Blackbird
(8595, 4290, 1), -- Race
(8595, 4045, 1), -- Resist Full Magic Attack
-- Tobald
(8596, 4290, 1), -- Race
(8596, 4045, 1), -- Resist Full Magic Attack
-- Tobald
(8597, 4290, 1), -- Race
(8597, 4045, 1), -- Resist Full Magic Attack
-- Medina Blackheart
(8598, 4290, 1), -- Race
(8598, 4045, 1), -- Resist Full Magic Attack
-- Disgraced Knight Waldstein
(8599, 4290, 1), -- Race
(8599, 4045, 1), -- Resist Full Magic Attack
-- Pamela Aprodia
(8600, 4290, 1), -- Race
(8600, 4045, 1), -- Resist Full Magic Attack
-- Pamela Aprodia
(8601, 4290, 1), -- Race
(8601, 4045, 1), -- Resist Full Magic Attack
-- Sharona Artemia
(8602, 4290, 1), -- Race
(8602, 4045, 1), -- Resist Full Magic Attack
-- Black Cat
(8603, 4290, 1), -- Race
(8603, 4045, 1), -- Resist Full Magic Attack
-- Grimst
(8604, 4290, 1), -- Race
(8604, 4045, 1), -- Resist Full Magic Attack
-- Kinsley
(8605, 4290, 1), -- Race
(8605, 4045, 1), -- Resist Full Magic Attack
-- Alice de Catrina
(8606, 4293, 1), -- Race
(8606, 4045, 1), -- Resist Full Magic Attack
-- Sake Dun Zu Hestui
(8607, 4290, 1), -- Race
(8607, 4045, 1), -- Resist Full Magic Attack
-- Belinda
(8608, 4290, 1), -- Race
(8608, 4045, 1), -- Resist Full Magic Attack
-- Unicorn Aurora
(8609, 4296, 1), -- Race
(8609, 4045, 1), -- Resist Full Magic Attack
-- Shiken Gloomdrake
(8610, 4290, 1), -- Race
(8610, 4045, 1), -- Resist Full Magic Attack
-- Scryde Heartseeker
(8611, 4290, 1), -- Race
(8611, 4045, 1), -- Resist Full Magic Attack
-- Eternity Wanderer Staris
(8612, 4290, 1), -- Race
(8612, 4045, 1), -- Resist Full Magic Attack
-- Noctisse
(8613, 4290, 1), -- Race
(8613, 4045, 1), -- Resist Full Magic Attack
-- Radyss
(8614, 4290, 1), -- Race
(8614, 4045, 1), -- Resist Full Magic Attack
-- Hermit
(8615, 4290, 1), -- Race
(8615, 4045, 1), -- Resist Full Magic Attack
-- Hermit
(8616, 4290, 1), -- Race
(8616, 4045, 1), -- Resist Full Magic Attack
-- Aria Firstmatter
(8617, 4290, 1), -- Race
(8617, 4045, 1), -- Resist Full Magic Attack
-- Moon Voice Irene
(8618, 4290, 1), -- Race
(8618, 4045, 1), -- Resist Full Magic Attack
-- Erica Ken Weber
(8619, 4290, 1), -- Race
(8619, 4045, 1), -- Resist Full Magic Attack
-- Verdure Wiseman Elikia
(8620, 4290, 1), -- Race
(8620, 4045, 1), -- Resist Full Magic Attack
-- Abyss Saint Elcardia
(8621, 4290, 1), -- Race
(8621, 4045, 1), -- Resist Full Magic Attack
-- Keats
(8622, 4292, 1), -- Race
(8622, 4045, 1), -- Resist Full Magic Attack
-- Bavarin
(8623, 4290, 1), -- Race
(8623, 4045, 1), -- Resist Full Magic Attack
-- Donath
(8624, 4290, 1), -- Race
(8624, 4045, 1), -- Resist Full Magic Attack
-- Yeti
(8625, 4295, 1), -- Race
(8625, 4045, 1), -- Resist Full Magic Attack
-- Duncan
(8626, 4290, 1), -- Race
(8626, 4045, 1), -- Resist Full Magic Attack
-- Mist
(8627, 4290, 1), -- Race
(8627, 4045, 1), -- Resist Full Magic Attack
-- Lilly
(8628, 4290, 1), -- Race
(8628, 4045, 1), -- Resist Full Magic Attack
-- Unicorn Kaleidos
(8629, 4296, 1), -- Race
(8629, 4045, 1), -- Resist Full Magic Attack
-- Unicorn Kaleidos
(8630, 4296, 1), -- Race
(8630, 4045, 1), -- Resist Full Magic Attack
-- Eric Ramsheart
(8631, 4290, 1), -- Race
(8631, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Servitor
(8632, 4298, 1), -- Race
(8632, 4045, 1), -- Resist Full Magic Attack
-- Winter Hunter Kadyth
(8633, 4290, 1), -- Race
(8633, 4045, 1), -- Resist Full Magic Attack
-- Winter Hunter Kadyth
(8634, 4290, 1), -- Race
(8634, 4045, 1), -- Resist Full Magic Attack
-- Winter Hunter Kadyth
(8635, 4290, 1), -- Race
(8635, 4045, 1), -- Resist Full Magic Attack
-- Flame Successor Akkan
(8636, 4290, 1), -- Race
(8636, 4045, 1), -- Resist Full Magic Attack
-- Khavatari Kashu
(8637, 4290, 1), -- Race
(8637, 4045, 1), -- Resist Full Magic Attack
-- Starling Knight Kastien
(8638, 4290, 1), -- Race
(8638, 4045, 1), -- Resist Full Magic Attack
-- Kain Van Halter
(8639, 4290, 1), -- Race
(8639, 4045, 1), -- Resist Full Magic Attack
-- Pilgrim of Darkness
(8640, 4290, 1), -- Race
(8640, 4045, 1), -- Resist Full Magic Attack
-- Pilgrim of Darkness
(8641, 4290, 1), -- Race
(8641, 4045, 1), -- Resist Full Magic Attack
-- Tarkai Zu Duda-Mara
(8642, 4290, 1), -- Race
(8642, 4045, 1), -- Resist Full Magic Attack
-- Silver Feyshar
(8643, 4290, 1), -- Race
(8643, 4045, 1), -- Resist Full Magic Attack
-- Dawn Witness Franz
(8644, 4290, 1), -- Race
(8644, 4045, 1), -- Resist Full Magic Attack
-- Hindemith Truevoice
(8645, 4290, 1), -- Race
(8645, 4045, 1), -- Resist Full Magic Attack
-- Tablet of Vision
(8646, 4290, 1), -- Race
(8646, 4045, 1), -- Resist Full Magic Attack
(8646, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8647, 4290, 1), -- Race
(8647, 4045, 1), -- Resist Full Magic Attack
(8647, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8648, 4290, 1), -- Race
(8648, 4045, 1), -- Resist Full Magic Attack
(8648, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8649, 4290, 1), -- Race
(8649, 4045, 1), -- Resist Full Magic Attack
(8649, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8650, 4290, 1), -- Race
(8650, 4045, 1), -- Resist Full Magic Attack
(8650, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8651, 4290, 1), -- Race
(8651, 4045, 1), -- Resist Full Magic Attack
(8651, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8652, 4290, 1), -- Race
(8652, 4045, 1), -- Resist Full Magic Attack
(8652, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8653, 4290, 1), -- Race
(8653, 4045, 1), -- Resist Full Magic Attack
(8653, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8654, 4290, 1), -- Race
(8654, 4045, 1), -- Resist Full Magic Attack
(8654, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8655, 4290, 1), -- Race
(8655, 4045, 1), -- Resist Full Magic Attack
(8655, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8656, 4290, 1), -- Race
(8656, 4045, 1), -- Resist Full Magic Attack
(8656, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8657, 4290, 1), -- Race
(8657, 4045, 1), -- Resist Full Magic Attack
(8657, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8658, 4290, 1), -- Race
(8658, 4045, 1), -- Resist Full Magic Attack
(8658, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8659, 4290, 1), -- Race
(8659, 4045, 1), -- Resist Full Magic Attack
(8659, 4390, 1), -- NPC Abnormal Immunity
-- Tablet of Vision
(8660, 4290, 1), -- Race
(8660, 4045, 1), -- Resist Full Magic Attack
(8660, 4390, 1), -- NPC Abnormal Immunity
-- Forgotten Monument
(8661, 4290, 1), -- Race
(8661, 4045, 1), -- Resist Full Magic Attack
(8661, 4390, 1), -- NPC Abnormal Immunity
-- Forgotten Monument
(8662, 4290, 1), -- Race
(8662, 4045, 1), -- Resist Full Magic Attack
(8662, 4390, 1), -- NPC Abnormal Immunity
-- Forgotten Monument
(8663, 4290, 1), -- Race
(8663, 4045, 1), -- Resist Full Magic Attack
(8663, 4390, 1), -- NPC Abnormal Immunity
-- Forgotten Monument
(8664, 4290, 1), -- Race
(8664, 4045, 1), -- Resist Full Magic Attack
(8664, 4390, 1), -- NPC Abnormal Immunity
-- Corpse of Dwarf
(8665, 4290, 1), -- Race
(8665, 4045, 1), -- Resist Full Magic Attack
(8665, 4390, 1), -- NPC Abnormal Immunity
-- Lumen
(8666, 4290, 1), -- Race
(8666, 4045, 1), -- Resist Full Magic Attack
-- Raik
(8667, 4290, 1), -- Race
(8667, 4045, 1), -- Resist Full Magic Attack
-- Tangen
(8668, 4290, 1), -- Race
(8668, 4045, 1), -- Resist Full Magic Attack
-- Onyx
(8669, 4290, 1), -- Race
(8669, 4045, 1), -- Resist Full Magic Attack
-- Burns
(8670, 4290, 1), -- Race
(8670, 4045, 1), -- Resist Full Magic Attack
-- Patrol
(8671, 4290, 1), -- Race
(8671, 4045, 1), -- Resist Full Magic Attack
-- Patrol
(8672, 4290, 1), -- Race
(8672, 4045, 1), -- Resist Full Magic Attack
-- Patrol
(8673, 4290, 1), -- Race
(8673, 4045, 1), -- Resist Full Magic Attack
-- Patrol
(8674, 4290, 1), -- Race
(8674, 4045, 1), -- Resist Full Magic Attack
-- Viktor Van Deik
(8675, 4290, 1), -- Race
(8675, 4045, 1), -- Resist Full Magic Attack
-- Gregory Athebaldt
(8676, 4290, 1), -- Race
(8676, 4045, 1), -- Resist Full Magic Attack
-- Border Patrol
(8677, 4290, 1), -- Race
(8677, 4045, 1), -- Resist Full Magic Attack
-- Border Patrol
(8678, 4290, 1), -- Race
(8678, 4045, 1), -- Resist Full Magic Attack
-- Eustace Van Essen
(8679, 4290, 1), -- Race
(8679, 4045, 1), -- Resist Full Magic Attack
-- Fleming Van Issen
(8680, 4290, 1), -- Race
(8680, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(8681, 4290, 1), -- Race
(8681, 4045, 1), -- Resist Full Magic Attack
-- Frontier Guard
(8682, 4290, 1), -- Race
(8682, 4045, 1), -- Resist Full Magic Attack
-- Eye of Argos
(8683, 4290, 1), -- Race
(8683, 4045, 1), -- Resist Full Magic Attack
(8683, 4390, 1), -- NPC Abnormal Immunity
-- Eye of Udan Mardui
(8684, 4291, 1), -- Race
(8684, 4045, 1), -- Resist Full Magic Attack
-- Eye of Asefa
(8685, 4291, 1), -- Race
(8685, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper of Fire Dragon
(8686, 4290, 1), -- Race
(8686, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper of Fire Dragon
(8687, 4290, 1), -- Race
(8687, 4045, 1), -- Resist Full Magic Attack
-- Grand Olympiad Manager
(8688, 4290, 1), -- Race
(8688, 4045, 1), -- Resist Full Magic Attack
-- Olympiad Finisher
(8689, 4290, 1), -- Race
(8689, 4045, 1), -- Resist Full Magic Attack
-- Monument of Heroes 
(8690, 4290, 1), -- Race
(8690, 4045, 1), -- Resist Full Magic Attack
(8690, 4390, 1), -- NPC Abnormal Immunity
-- Training Dummy 
(8691, 4290, 1), -- Race
(8691, 4045, 1), -- Resist Full Magic Attack
(8691, 4390, 1), -- NPC Abnormal Immunity
-- Priest of Dawn
(8692, 4290, 1), -- Race
(8692, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dusk
(8693, 4290, 1), -- Race
(8693, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dawn
(8694, 4290, 1), -- Race
(8694, 4045, 1), -- Resist Full Magic Attack
-- Priest of Dusk
(8695, 4290, 1), -- Race
(8695, 4045, 1), -- Resist Full Magic Attack
-- Platis
(8696, 4290, 1), -- Race
(8696, 4045, 1), -- Resist Full Magic Attack
-- Eindarkner
(8697, 4290, 1), -- Race
(8697, 4045, 1), -- Resist Full Magic Attack
-- Arisha
(8698, 4290, 1), -- Race
(8698, 4045, 1), -- Resist Full Magic Attack
-- Stanislava
(8699, 4290, 1), -- Race
(8699, 4045, 1), -- Resist Full Magic Attack
-- Saint's Follower
(8700, 4290, 1), -- Race
(8700, 4045, 1), -- Resist Full Magic Attack
-- Saint's Follower
(8701, 4290, 1), -- Race
(8701, 4045, 1), -- Resist Full Magic Attack
-- Saint's Follower
(8702, 4290, 1), -- Race
(8702, 4045, 1), -- Resist Full Magic Attack
-- Saint's Follower
(8703, 4290, 1), -- Race
(8703, 4045, 1), -- Resist Full Magic Attack
-- Saint's Follower
(8704, 4290, 1), -- Race
(8704, 4045, 1), -- Resist Full Magic Attack
-- Daimon the White-Eyed
(8705, 4291, 1), -- Race
(8705, 4045, 1), -- Resist Full Magic Attack
-- Miki the Cat
(8706, 4293, 1), -- Race
(8706, 4045, 1), -- Resist Full Magic Attack
-- Ketra Van Grunt
(8707, 4295, 1), -- Race
(8707, 4045, 1), -- Resist Full Magic Attack
-- Ketra Van Shaman
(8708, 4295, 1), -- Race
(8708, 4045, 1), -- Resist Full Magic Attack
-- Ketra Van Captain
(8709, 4295, 1), -- Race
(8709, 4045, 1), -- Resist Full Magic Attack
-- Ketra Van Commander
(8710, 4295, 1), -- Race
(8710, 4045, 1), -- Resist Full Magic Attack
-- Ketra Van Shaman
(8711, 4295, 1), -- Race
(8711, 4045, 1), -- Resist Full Magic Attack
-- Varka Raider
(8712, 4295, 1), -- Race
(8712, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8713, 4290, 1), -- Race
(8713, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8714, 4290, 1), -- Race
(8714, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8715, 4290, 1), -- Race
(8715, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8716, 4290, 1), -- Race
(8716, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8717, 4290, 1), -- Race
(8717, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8718, 4290, 1), -- Race
(8718, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8719, 4290, 1), -- Race
(8719, 4045, 1), -- Resist Full Magic Attack
-- Preacher of Doom
(8720, 4290, 1), -- Race
(8720, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8721, 4291, 1), -- Race
(8721, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8722, 4291, 1), -- Race
(8722, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8723, 4291, 1), -- Race
(8723, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8724, 4291, 1), -- Race
(8724, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8725, 4291, 1), -- Race
(8725, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8726, 4291, 1), -- Race
(8726, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8727, 4291, 1), -- Race
(8727, 4045, 1), -- Resist Full Magic Attack
-- Orator of Revelations
(8728, 4291, 1), -- Race
(8728, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8729, 4290, 1), -- Race
(8729, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8730, 4290, 1), -- Race
(8730, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8731, 4290, 1), -- Race
(8731, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8732, 4290, 1), -- Race
(8732, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8733, 4290, 1), -- Race
(8733, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8734, 4290, 1), -- Race
(8734, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8735, 4290, 1), -- Race
(8735, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8736, 4290, 1), -- Race
(8736, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8737, 4290, 1), -- Race
(8737, 4045, 1), -- Resist Full Magic Attack
-- Adventurer Guildsman
(8738, 4290, 1), -- Race
(8738, 4045, 1), -- Resist Full Magic Attack
-- Talien
(8739, 4290, 1), -- Race
(8739, 4045, 1), -- Resist Full Magic Attack
-- Caradine
(8740, 4290, 1), -- Race
(8740, 4045, 1), -- Resist Full Magic Attack
-- Ossian
(8741, 4290, 1), -- Race
(8741, 4045, 1), -- Resist Full Magic Attack
-- Virgil
(8742, 4290, 1), -- Race
(8742, 4045, 1), -- Resist Full Magic Attack
-- Kassandra
(8743, 4290, 1), -- Race
(8743, 4045, 1), -- Resist Full Magic Attack
-- Ogmar
(8744, 4290, 1), -- Race
(8744, 4045, 1), -- Resist Full Magic Attack
-- Lady of the Lake
(8745, 4290, 1), -- Race
(8745, 4045, 1), -- Resist Full Magic Attack
-- Fallen Unicorn
(8746, 4296, 1), -- Race
(8746, 4045, 1), -- Resist Full Magic Attack
-- Pure White Unicorn
(8747, 4296, 1), -- Race
(8747, 4045, 1), -- Resist Full Magic Attack
-- Cornerstone of Restraining
(8748, 4291, 1), -- Race
(8748, 4045, 1), -- Resist Full Magic Attack
(8748, 4390, 1), -- NPC Abnormal Immunity
-- Hephaeston
(8749, 4290, 1), -- Race
(8749, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Woman
(8750, 4290, 1), -- Race
(8750, 4045, 1), -- Resist Full Magic Attack
-- Mysterious Dark Knight
(8751, 4290, 1), -- Race
(8751, 4045, 1), -- Resist Full Magic Attack
-- Corpse of Angel
(8752, 4297, 1), -- Race
(8752, 4045, 1), -- Resist Full Magic Attack
(8752, 4390, 1), -- NPC Abnormal Immunity
-- Blacksmith
(8753, 4290, 1), -- Race
(8753, 4045, 1), -- Resist Full Magic Attack
-- Warehouse Keeper
(8754, 4290, 1), -- Race
(8754, 4045, 1), -- Resist Full Magic Attack
-- Halaster
(8755, 4290, 1), -- Race
(8755, 4045, 1), -- Resist Full Magic Attack
-- Mr. Cat
(8756, 4293, 1), -- Race
(8756, 4045, 1), -- Resist Full Magic Attack
-- Miss Queen
(8757, 4293, 1), -- Race
(8757, 4045, 1), -- Resist Full Magic Attack
-- Rafi
(8758, 4299, 1), -- Race
(8758, 4045, 1), -- Resist Full Magic Attack
-- Teleport Cube
(8759, 4290, 1), -- Race
(8759, 4045, 1), -- Resist Full Magic Attack
(8759, 4390, 1), -- NPC Abnormal Immunity
-- Miss Queen
(8760, 4293, 1), -- Race
(8760, 4045, 1), -- Resist Full Magic Attack
-- Miss Queen
(8761, 4293, 1), -- Race
(8761, 4045, 1), -- Resist Full Magic Attack
-- Miss Queen
(8762, 4293, 1), -- Race
(8762, 4045, 1), -- Resist Full Magic Attack
-- Miss Queen
(8763, 4293, 1), -- Race
(8763, 4045, 1), -- Resist Full Magic Attack
-- Miss Queen
(8764, 4293, 1), -- Race
(8764, 4045, 1), -- Resist Full Magic Attack
-- Miss Queen
(8765, 4293, 1), -- Race
(8765, 4045, 1), -- Resist Full Magic Attack
-- Miss Queen
(8766, 4293, 1), -- Race
(8766, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8767, 4293, 1), -- Race
(8767, 4045, 1), -- Resist Full Magic Attack
-- Event Gatekeeper
(8768, 4293, 1), -- Race
(8768, 4045, 1), -- Resist Full Magic Attack
-- Monument of Heroes
(8769, 4290, 1), -- Race
(8769, 4045, 1), -- Resist Full Magic Attack
(8769, 4390, 1), -- NPC Abnormal Immunity
-- Monument of Heroes
(8770, 4290, 1), -- Race
(8770, 4045, 1), -- Resist Full Magic Attack
(8770, 4390, 1), -- NPC Abnormal Immunity
-- Monument of Heroes
(8771, 4290, 1), -- Race
(8771, 4045, 1), -- Resist Full Magic Attack
(8771, 4390, 1), -- NPC Abnormal Immunity
-- Monument of Heroes
(8772, 4290, 1), -- Race
(8772, 4045, 1), -- Resist Full Magic Attack
(8772, 4390, 1), -- NPC Abnormal Immunity
-- Sonin
(8773, 4290, 1), -- Race
(8773, 4045, 1), -- Resist Full Magic Attack
-- Beryl the Cat
(8774, 4293, 1), -- Race
(8774, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8775, 4290, 1), -- Race
(8775, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8776, 4290, 1), -- Race
(8776, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8777, 4290, 1), -- Race
(8777, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8778, 4290, 1), -- Race
(8778, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8779, 4290, 1), -- Race
(8779, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8780, 4290, 1), -- Race
(8780, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8781, 4290, 1), -- Race
(8781, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8782, 4290, 1), -- Race
(8782, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8783, 4290, 1), -- Race
(8783, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8784, 4290, 1), -- Race
(8784, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8785, 4290, 1), -- Race
(8785, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8786, 4290, 1), -- Race
(8786, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8787, 4290, 1), -- Race
(8787, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8788, 4290, 1), -- Race
(8788, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8789, 4290, 1), -- Race
(8789, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8790, 4290, 1), -- Race
(8790, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8791, 4290, 1), -- Race
(8791, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8792, 4290, 1), -- Race
(8792, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8793, 4290, 1), -- Race
(8793, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8794, 4290, 1), -- Race
(8794, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8795, 4290, 1), -- Race
(8795, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8796, 4290, 1), -- Race
(8796, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8797, 4290, 1), -- Race
(8797, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8798, 4290, 1), -- Race
(8798, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8799, 4290, 1), -- Race
(8799, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8800, 4290, 1), -- Race
(8800, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8801, 4290, 1), -- Race
(8801, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8802, 4290, 1), -- Race
(8802, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8803, 4290, 1), -- Race
(8803, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8804, 4290, 1), -- Race
(8804, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8805, 4290, 1), -- Race
(8805, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8806, 4290, 1), -- Race
(8806, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8807, 4290, 1), -- Race
(8807, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8808, 4290, 1), -- Race
(8808, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8809, 4290, 1), -- Race
(8809, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8810, 4290, 1), -- Race
(8810, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8811, 4290, 1), -- Race
(8811, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8812, 4290, 1), -- Race
(8812, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8813, 4290, 1), -- Race
(8813, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8814, 4290, 1), -- Race
(8814, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8815, 4290, 1), -- Race
(8815, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8816, 4290, 1), -- Race
(8816, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8817, 4290, 1), -- Race
(8817, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8818, 4290, 1), -- Race
(8818, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8819, 4290, 1), -- Race
(8819, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8820, 4290, 1), -- Race
(8820, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8821, 4290, 1), -- Race
(8821, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8822, 4290, 1), -- Race
(8822, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8823, 4290, 1), -- Race
(8823, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8824, 4290, 1), -- Race
(8824, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8825, 4290, 1), -- Race
(8825, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8826, 4290, 1), -- Race
(8826, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8827, 4290, 1), -- Race
(8827, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8828, 4290, 1), -- Race
(8828, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8829, 4290, 1), -- Race
(8829, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8830, 4290, 1), -- Race
(8830, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8831, 4290, 1), -- Race
(8831, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8832, 4290, 1), -- Race
(8832, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8833, 4290, 1), -- Race
(8833, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8834, 4290, 1), -- Race
(8834, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8835, 4290, 1), -- Race
(8835, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8836, 4290, 1), -- Race
(8836, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8837, 4290, 1), -- Race
(8837, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8838, 4290, 1), -- Race
(8838, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8839, 4290, 1), -- Race
(8839, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8840, 4290, 1), -- Race
(8840, 4045, 1), -- Resist Full Magic Attack
-- Adventure Guild Member
(8841, 4290, 1), -- Race
(8841, 4045, 1), -- Resist Full Magic Attack
-- null
(8842, 4293, 1), -- Race
(8842, 4045, 1); -- Resist Full Magic Attack

INSERT INTO npcskills VALUES
-- Greyclaw Kutus
(10001, 4494, 1), -- Raid Boss
(10001, 4790, 1), -- Raid Boss - Level 23
(10001, 4295, 1), -- Race
(10001, 4045, 1), -- Resist Full Magic Attack
(10001, 4197, 2), -- Hold
-- Guard of Kutus
(10002, 4295, 1), -- Race
(10002, 4045, 1), -- Resist Full Magic Attack
(10002, 4191, 2), -- BOSS Windstrike
(10002, 4198, 2), -- Poison
-- Pawn of Kutus
(10003, 4295, 1), -- Race
(10003, 4045, 1), -- Resist Full Magic Attack
(10003, 4727, 2), -- Shock
-- Turek Mercenary Captain
(10004, 4494, 1), -- Raid Boss
(10004, 4791, 1), -- Raid Boss - Level 30
(10004, 4295, 1), -- Race
(10004, 4045, 1), -- Resist Full Magic Attack
(10004, 4739, 3), -- BOSS Strike
-- Turek Mercenary Archer
(10005, 4295, 1), -- Race
(10005, 4045, 1), -- Resist Full Magic Attack
(10005, 4754, 2), -- BOSS Power Shot
-- Turek Mercenary Warrior
(10006, 4295, 1), -- Race
(10006, 4045, 1), -- Resist Full Magic Attack
-- Retreat Spider Cletu
(10007, 4494, 1), -- Raid Boss
(10007, 4792, 1), -- Raid Boss - Level 42
(10007, 4301, 1), -- Race
(10007, 4045, 1), -- Resist Full Magic Attack
(10007, 4178, 4), -- BOSS Flamestrike
-- Cletu's Retainer
(10008, 4301, 1), -- Race
(10008, 4045, 1), -- Resist Full Magic Attack
(10008, 4780, 3), -- BOSS Heal
(10008, 4214, 3), -- BOSS Reflect Damage
-- Cletu's Pawn
(10009, 4301, 1), -- Race
(10009, 4045, 1), -- Resist Full Magic Attack
-- Furious Thieles
(10010, 4494, 1), -- Raid Boss
(10010, 4793, 1), -- Raid Boss - Level 55
(10010, 4296, 1), -- Race
(10010, 4045, 1), -- Resist Full Magic Attack
(10010, 4740, 5), -- BOSS Strike
-- Guard of Thieles
(10011, 4295, 1), -- Race
(10011, 4045, 1), -- Resist Full Magic Attack
(10011, 4760, 5), -- Shock
-- Follower of Thieles
(10012, 4296, 1), -- Race
(10012, 4045, 1), -- Resist Full Magic Attack
-- Ghost of Peasant Leader
(10013, 4494, 1), -- Raid Boss
(10013, 4794, 1), -- Raid Boss - Level 50
(10013, 4290, 1), -- Race
(10013, 4275, 3), -- Sacred Attack Weak Point
(10013, 4278, 1), -- Dark Attack
(10013, 4045, 1), -- Resist Full Magic Attack
(10013, 4192, 5), -- BOSS HP Drain
(10013, 4188, 5), -- Bleed
(10013, 4190, 5), -- Decrease MP
-- Ghost of Peasant
(10014, 4290, 1), -- Race
(10014, 4275, 3), -- Sacred Attack Weak Point
(10014, 4278, 1), -- Dark Attack
(10014, 4045, 1), -- Resist Full Magic Attack
(10014, 4720, 4), -- BOSS Strike
-- Ghost of Peasant
(10015, 4290, 1), -- Race
(10015, 4275, 3), -- Sacred Attack Weak Point
(10015, 4278, 1), -- Dark Attack
(10015, 4045, 1), -- Resist Full Magic Attack
-- The 3rd Underwater Guardian
(10016, 4494, 1), -- Raid Boss
(10016, 4795, 1), -- Raid Boss - Level 60
(10016, 4291, 1), -- Race
(10016, 4045, 1), -- Resist Full Magic Attack
(10016, 4178, 6), -- BOSS Flamestrike
-- Escort of the 3rd Guardian
(10017, 4291, 1), -- Race
(10017, 4045, 1), -- Resist Full Magic Attack
(10017, 4784, 5), -- BOSS Chant of Life
(10017, 4213, 5), -- BOSS Haste
-- Support of the 3rd Guardian
(10018, 4291, 1), -- Race
(10018, 4045, 1), -- Resist Full Magic Attack
-- Pan Dryad
(10019, 4494, 1), -- Raid Boss
(10019, 4796, 1), -- Raid Boss - Level 25
(10019, 4302, 1), -- Race
(10019, 4045, 1), -- Resist Full Magic Attack
(10019, 4175, 2), -- BOSS Haste
(10019, 4732, 2), -- BOSS Mortal Blow
(10019, 4172, 2), -- Shock
-- Breka Warlock Pastu
(10020, 4494, 1), -- Raid Boss
(10020, 4797, 1), -- Raid Boss - Level 34
(10020, 4295, 1), -- Race
(10020, 4045, 1), -- Resist Full Magic Attack
(10020, 4178, 3), -- BOSS Flamestrike
-- Pastu's Silhouette
(10021, 4298, 1), -- Race
(10021, 4278, 1), -- Dark Attack
(10021, 4333, 3), -- Resist Dark Attack
(10021, 4045, 1), -- Resist Full Magic Attack
(10021, 4786, 3), -- BOSS Chant of Life
(10021, 4211, 3), -- BOSS Might
-- Pastu's Shadow
(10022, 4298, 1), -- Race
(10022, 4278, 1), -- Dark Attack
(10022, 4333, 3), -- Resist Dark Attack
(10022, 4045, 1), -- Resist Full Magic Attack
-- Stakato Queen Zyrnna
(10023, 4494, 1), -- Raid Boss
(10023, 4798, 1), -- Raid Boss - Level 34
(10023, 4301, 1), -- Race
(10023, 4045, 1), -- Resist Full Magic Attack
(10023, 4194, 3), -- BOSS Aura Burn
(10023, 4183, 3), -- Decrease P.Atk
(10023, 4189, 3), -- Paralysis
-- Jeruna's Guard Captain
(10024, 4301, 1), -- Race
(10024, 4045, 1), -- Resist Full Magic Attack
(10024, 4730, 3), -- BOSS Mortal Blow
-- Jeruna's Guard
(10025, 4301, 1), -- Race
(10025, 4045, 1), -- Resist Full Magic Attack
-- Katu Van Leader Atui
(10026, 4494, 1), -- Raid Boss
(10026, 4799, 1), -- Raid Boss - Level 49
(10026, 4295, 1), -- Race
(10026, 4045, 1), -- Resist Full Magic Attack
(10026, 4746, 4), -- Shock
-- Katu Van Marksman
(10027, 4295, 1), -- Race
(10027, 4045, 1), -- Resist Full Magic Attack
(10027, 4761, 4), -- Shock
-- Katu Van Raider
(10028, 4295, 1), -- Race
(10028, 4045, 1), -- Resist Full Magic Attack
-- Atraiban
(10029, 4494, 1), -- Raid Boss
(10029, 4800, 1), -- Raid Boss - Level 53
(10029, 4291, 1), -- Race
(10029, 4045, 1), -- Resist Full Magic Attack
(10029, 4178, 5), -- BOSS Flamestrike
-- Atraiban's Top Disciple
(10030, 4291, 1), -- Race
(10030, 4045, 1), -- Resist Full Magic Attack
(10030, 4784, 5), -- BOSS Chant of Life
(10030, 4212, 5), -- BOSS Shield
-- Atraiban's Disciple
(10031, 4291, 1), -- Race
(10031, 4045, 1), -- Resist Full Magic Attack
-- Eva's Guardian Millenu
(10032, 4494, 1), -- Raid Boss
(10032, 4801, 1), -- Raid Boss - Level 65
(10032, 4298, 1), -- Race
(10032, 4278, 1), -- Dark Attack
(10032, 4333, 3), -- Resist Dark Attack
(10032, 4045, 1), -- Resist Full Magic Attack
(10032, 4197, 6), -- Hold
-- Millenu Guard Shaman
(10033, 4295, 1), -- Race
(10033, 4045, 1), -- Resist Full Magic Attack
(10033, 4193, 6), -- BOSS Life Drain
(10033, 4199, 6), -- Decrease P.Atk
-- Mellenu Guard Warrior
(10034, 4290, 1), -- Race
(10034, 4275, 3), -- Sacred Attack Weak Point
(10034, 4278, 1), -- Dark Attack
(10034, 4045, 1), -- Resist Full Magic Attack
(10034, 4170, 6), -- BOSS Mortal Blow
-- Shilen's Messenger Cabrio
(10035, 4494, 1), -- Raid Boss
(10035, 4802, 1), -- Raid Boss - Level 70
(10035, 4298, 1), -- Race
(10035, 4278, 1), -- Dark Attack
(10035, 4333, 3), -- Resist Dark Attack
(10035, 4045, 1), -- Resist Full Magic Attack
(10035, 4180, 7), -- Shock
-- Cabrio Captor
(10036, 4290, 1), -- Race
(10036, 4275, 3), -- Sacred Attack Weak Point
(10036, 4278, 1), -- Dark Attack
(10036, 4045, 1), -- Resist Full Magic Attack
(10036, 4208, 6), -- Shock
-- Cabrio Blader
(10037, 4298, 1), -- Race
(10037, 4278, 1), -- Dark Attack
(10037, 4333, 3), -- Resist Dark Attack
(10037, 4045, 1), -- Resist Full Magic Attack
-- Tirak
(10038, 4494, 1), -- Raid Boss
(10038, 4803, 1), -- Raid Boss - Level 28
(10038, 4298, 1), -- Race
(10038, 4278, 1), -- Dark Attack
(10038, 4333, 3), -- Resist Dark Attack
(10038, 4045, 1), -- Resist Full Magic Attack
(10038, 4192, 2), -- BOSS HP Drain
(10038, 4182, 2), -- Poison
(10038, 4186, 2), -- Hold
-- Tirak's Guard
(10039, 4290, 1), -- Race
(10039, 4275, 3), -- Sacred Attack Weak Point
(10039, 4278, 1), -- Dark Attack
(10039, 4045, 1), -- Resist Full Magic Attack
(10039, 4732, 2), -- BOSS Mortal Blow
-- Tirak's Knight
(10040, 4290, 1), -- Race
(10040, 4275, 3), -- Sacred Attack Weak Point
(10040, 4278, 1), -- Dark Attack
(10040, 4045, 1), -- Resist Full Magic Attack
-- Remmel
(10041, 4494, 1), -- Raid Boss
(10041, 4804, 1), -- Raid Boss - Level 35
(10041, 4301, 1), -- Race
(10041, 4045, 1), -- Resist Full Magic Attack
(10041, 4752, 3), -- BOSS Mortal Blow
-- Remmel's Archer
(10042, 4295, 1), -- Race
(10042, 4045, 1), -- Resist Full Magic Attack
(10042, 4757, 3), -- BOSS Power Shot
-- Remmel's Guard
(10043, 4301, 1), -- Race
(10043, 4045, 1), -- Resist Full Magic Attack
-- Barion
(10044, 4494, 1), -- Raid Boss
(10044, 4805, 1), -- Raid Boss - Level 47
(10044, 4290, 1), -- Race
(10044, 4275, 3), -- Sacred Attack Weak Point
(10044, 4278, 1), -- Dark Attack
(10044, 4045, 1), -- Resist Full Magic Attack
(10044, 4178, 4), -- BOSS Flamestrike
-- Dark Mage's of Barion
(10045, 4295, 1), -- Race
(10045, 4045, 1), -- Resist Full Magic Attack
(10045, 4781, 4), -- BOSS Heal
(10045, 4213, 4), -- BOSS Haste
-- Barion's Grunt
(10046, 4290, 1), -- Race
(10046, 4275, 3), -- Sacred Attack Weak Point
(10046, 4278, 1), -- Dark Attack
(10046, 4045, 1), -- Resist Full Magic Attack
-- Karte
(10047, 4494, 1), -- Raid Boss
(10047, 4806, 1), -- Raid Boss - Level 49
(10047, 4290, 1), -- Race
(10047, 4275, 3), -- Sacred Attack Weak Point
(10047, 4278, 1), -- Dark Attack
(10047, 4045, 1), -- Resist Full Magic Attack
(10047, 4192, 4), -- BOSS HP Drain
(10047, 4188, 4), -- Bleed
(10047, 4190, 4), -- Decrease MP
-- Karte's Chief Knight
(10048, 4290, 1), -- Race
(10048, 4275, 3), -- Sacred Attack Weak Point
(10048, 4278, 1), -- Dark Attack
(10048, 4045, 1), -- Resist Full Magic Attack
(10048, 4722, 4), -- BOSS Strike
-- Karte's Royal Guard
(10049, 4290, 1), -- Race
(10049, 4275, 3), -- Sacred Attack Weak Point
(10049, 4278, 1), -- Dark Attack
(10049, 4045, 1), -- Resist Full Magic Attack
-- Verfa
(10050, 4494, 1), -- Raid Boss
(10050, 4807, 1), -- Raid Boss - Level 51
(10050, 4290, 1), -- Race
(10050, 4275, 3), -- Sacred Attack Weak Point
(10050, 4278, 1), -- Dark Attack
(10050, 4045, 1), -- Resist Full Magic Attack
(10050, 4176, 5), -- BOSS Reflect Damage
(10050, 4728, 5), -- Shock
(10050, 4172, 5), -- Shock
-- Rahha
(10051, 4494, 1), -- Raid Boss
(10051, 4808, 1), -- Raid Boss - Level 65
(10051, 4298, 1), -- Race
(10051, 4278, 1), -- Dark Attack
(10051, 4333, 3), -- Resist Dark Attack
(10051, 4045, 1), -- Resist Full Magic Attack
(10051, 4178, 6), -- BOSS Flamestrike
-- Priestess of Rahha
(10052, 4292, 1), -- Race
(10052, 4045, 1), -- Resist Full Magic Attack
(10052, 4785, 6), -- BOSS Chant of Life
(10052, 4213, 6), -- BOSS Haste
-- Fanatics of Rahha
(10053, 4292, 1), -- Race
(10053, 4045, 1), -- Resist Full Magic Attack
-- Kernon
(10054, 4494, 1), -- Raid Boss
(10054, 4809, 1), -- Raid Boss - Level 75
(10054, 4298, 1), -- Race
(10054, 4278, 1), -- Dark Attack
(10054, 4333, 3), -- Resist Dark Attack
(10054, 4045, 1), -- Resist Full Magic Attack
(10054, 4195, 8), -- BOSS Twister
(10054, 4182, 8), -- Poison
(10054, 4190, 8), -- Decrease MP
-- Taliadon
(10055, 4290, 1), -- Race
(10055, 4275, 3), -- Sacred Attack Weak Point
(10055, 4278, 1), -- Dark Attack
(10055, 4045, 1), -- Resist Full Magic Attack
(10055, 4168, 7), -- BOSS Strike
-- Vemsk
(10056, 4290, 1), -- Race
(10056, 4275, 3), -- Sacred Attack Weak Point
(10056, 4278, 1), -- Dark Attack
(10056, 4045, 1), -- Resist Full Magic Attack
-- Biconne of Blue Sky
(10057, 4494, 1), -- Raid Boss
(10057, 4810, 1), -- Raid Boss - Level 45
(10057, 4292, 1), -- Race
(10057, 4045, 1), -- Resist Full Magic Attack
(10057, 4742, 4), -- BOSS Strike
-- Biconne's Shooter
(10058, 4290, 1), -- Race
(10058, 4275, 3), -- Sacred Attack Weak Point
(10058, 4278, 1), -- Dark Attack
(10058, 4045, 1), -- Resist Full Magic Attack
(10058, 4757, 5), -- BOSS Power Shot
-- Biconne's Warrior
(10059, 4290, 1), -- Race
(10059, 4275, 3), -- Sacred Attack Weak Point
(10059, 4278, 1), -- Dark Attack
(10059, 4045, 1), -- Resist Full Magic Attack
-- Unrequited Kael
(10060, 4494, 1), -- Raid Boss
(10060, 4811, 1), -- Raid Boss - Level 24
(10060, 4298, 1), -- Race
(10060, 4278, 1), -- Dark Attack
(10060, 4333, 3), -- Resist Dark Attack
(10060, 4045, 1), -- Resist Full Magic Attack
(10060, 4197, 2), -- Hold
-- Kael's Bead
(10061, 4291, 1), -- Race
(10061, 4045, 1), -- Resist Full Magic Attack
(10061, 4194, 2), -- BOSS Aura Burn
(10061, 4199, 2), -- Decrease P.Atk
-- Kael's Escort
(10062, 4292, 1), -- Race
(10062, 4045, 1), -- Resist Full Magic Attack
(10062, 4728, 2), -- Shock
-- Chertuba of Great Soul
(10063, 4494, 1), -- Raid Boss
(10063, 4812, 1), -- Raid Boss - Level 35
(10063, 4295, 1), -- Race
(10063, 4045, 1), -- Resist Full Magic Attack
(10063, 4173, 3), -- BOSS Might
(10063, 4722, 3), -- BOSS Strike
(10063, 4737, 3), -- BOSS Spinning Slasher
-- Wizard of Storm Teruk
(10064, 4494, 1), -- Raid Boss
(10064, 4813, 1), -- Raid Boss - Level 40
(10064, 4295, 1), -- Race
(10064, 4045, 1), -- Resist Full Magic Attack
(10064, 4195, 4), -- BOSS Twister
(10064, 4185, 4), -- Sleep
(10064, 4188, 4), -- Bleed
-- Teruk's Escort
(10065, 4295, 1), -- Race
(10065, 4045, 1), -- Resist Full Magic Attack
(10065, 4722, 3), -- BOSS Strike
-- Teruk's Knight
(10066, 4295, 1), -- Race
(10066, 4045, 1), -- Resist Full Magic Attack
-- Captain of Red Flag Shaka
(10067, 4494, 1), -- Raid Boss
(10067, 4814, 1), -- Raid Boss - Level 52
(10067, 4295, 1), -- Race
(10067, 4045, 1), -- Resist Full Magic Attack
(10067, 4740, 5), -- BOSS Strike
-- Shaka's Shooter
(10068, 4295, 1), -- Race
(10068, 4045, 1), -- Resist Full Magic Attack
(10068, 4755, 4), -- BOSS Power Shot
-- Shaka's Follower
(10069, 4295, 1), -- Race
(10069, 4045, 1), -- Resist Full Magic Attack
-- Enchanted Forest Watcher Ruell
(10070, 4494, 1), -- Raid Boss
(10070, 4815, 1), -- Raid Boss - Level 55
(10070, 4296, 1), -- Race
(10070, 4045, 1), -- Resist Full Magic Attack
(10070, 4197, 5), -- Hold
-- Ruell's Wind
(10071, 4296, 1), -- Race
(10071, 4045, 1), -- Resist Full Magic Attack
(10071, 4191, 5), -- BOSS Windstrike
(10071, 4200, 5), -- Decrease Atk.Speed
-- Ruell's Unicorn
(10072, 4296, 1), -- Race
(10072, 4045, 1), -- Resist Full Magic Attack
(10072, 4727, 5), -- Shock
-- Bloody Priest Rudelto
(10073, 4494, 1), -- Raid Boss
(10073, 4816, 1), -- Raid Boss - Level 69
(10073, 4298, 1), -- Race
(10073, 4278, 1), -- Dark Attack
(10073, 4333, 3), -- Resist Dark Attack
(10073, 4045, 1), -- Resist Full Magic Attack
(10073, 4178, 6), -- BOSS Flamestrike
-- Rudelto's Banshee
(10074, 4298, 1), -- Race
(10074, 4275, 3), -- Sacred Attack Weak Point
(10074, 4278, 1), -- Dark Attack
(10074, 4045, 1), -- Resist Full Magic Attack
(10074, 4209, 6), -- BOSS Heal
(10074, 4213, 6), -- BOSS Haste
-- Rudelto's Dre Vanul
(10075, 4298, 1), -- Race
(10075, 4278, 1), -- Dark Attack
(10075, 4333, 3), -- Resist Dark Attack
(10075, 4045, 1), -- Resist Full Magic Attack
-- Princess Molrang
(10076, 4494, 1), -- Raid Boss
(10076, 4817, 1), -- Raid Boss - Level 25
(10076, 4292, 1), -- Race
(10076, 4045, 1), -- Resist Full Magic Attack
(10076, 4194, 2), -- BOSS Aura Burn
(10076, 4182, 2), -- Poison
(10076, 4187, 2), -- Decrease Speed
-- Captain Dogun
(10077, 4293, 1), -- Race
(10077, 4045, 1), -- Resist Full Magic Attack
(10077, 4732, 2), -- BOSS Mortal Blow
-- Princess' Guard
(10078, 4293, 1), -- Race
(10078, 4045, 1), -- Resist Full Magic Attack
-- Cat's Eye Bandit
(10079, 4494, 1), -- Raid Boss
(10079, 4818, 1), -- Raid Boss - Level 30
(10079, 4295, 1), -- Race
(10079, 4045, 1), -- Resist Full Magic Attack
(10079, 4178, 3), -- BOSS Flamestrike
-- Mercenary
(10080, 4295, 1), -- Race
(10080, 4045, 1), -- Resist Full Magic Attack
(10080, 4785, 2), -- BOSS Chant of Life
(10080, 4211, 2), -- BOSS Might
-- Cat's Eye Bandit
(10081, 4295, 1), -- Race
(10081, 4045, 1), -- Resist Full Magic Attack
-- Leader of Cat Gang
(10082, 4494, 1), -- Raid Boss
(10082, 4819, 1), -- Raid Boss - Level 39
(10082, 4293, 1), -- Race
(10082, 4045, 1), -- Resist Full Magic Attack
(10082, 4197, 3), -- Hold
-- Cat Gang
(10083, 4293, 1), -- Race
(10083, 4045, 1), -- Resist Full Magic Attack
(10083, 4196, 3), -- Decrease Speed
(10083, 4200, 3), -- Decrease Atk.Speed
-- Cat Gang
(10084, 4293, 1), -- Race
(10084, 4045, 1), -- Resist Full Magic Attack
(10084, 4727, 3), -- Shock
-- Timak Orc Chief Ranger
(10085, 4494, 1), -- Raid Boss
(10085, 4820, 1), -- Raid Boss - Level 44
(10085, 4295, 1), -- Race
(10085, 4045, 1), -- Resist Full Magic Attack
(10085, 4745, 4), -- Shock
-- Timak Orc Ranger
(10086, 4295, 1), -- Race
(10086, 4045, 1), -- Resist Full Magic Attack
(10086, 4760, 4), -- Shock
-- Timak Orc Ranger
(10087, 4295, 1), -- Race
(10087, 4045, 1), -- Resist Full Magic Attack
-- Crazy Mechanic Golem
(10088, 4494, 1), -- Raid Boss
(10088, 4821, 1), -- Raid Boss - Level 43
(10088, 4291, 1), -- Race
(10088, 4045, 1), -- Resist Full Magic Attack
(10088, 4176, 5), -- BOSS Reflect Damage
(10088, 4724, 5), -- Shock
(10088, 4172, 5), -- Shock
-- Soulless Wild Boar
(10089, 4494, 1), -- Raid Boss
(10089, 4822, 1), -- Raid Boss - Level 59
(10089, 4293, 1), -- Race
(10089, 4045, 1), -- Resist Full Magic Attack
(10089, 4197, 5), -- Hold
-- Soulless Wolf
(10090, 4293, 1), -- Race
(10090, 4045, 1), -- Resist Full Magic Attack
(10090, 4193, 5), -- BOSS Life Drain
(10090, 4205, 5), -- Paralysis
-- Soulless Bear
(10091, 4293, 1), -- Race
(10091, 4045, 1), -- Resist Full Magic Attack
(10091, 4726, 5), -- Shock
-- Korim
(10092, 4494, 1), -- Raid Boss
(10092, 4823, 1), -- Raid Boss - Level 70
(10092, 4295, 1), -- Race
(10092, 4045, 1), -- Resist Full Magic Attack
(10092, 4195, 7), -- BOSS Twister
(10092, 4182, 7), -- Poison
(10092, 4190, 7), -- Decrease MP
-- Korim Chief Guard Kaywon
(10093, 4295, 1), -- Race
(10093, 4045, 1), -- Resist Full Magic Attack
(10093, 4720, 6), -- BOSS Strike
-- Korim Guards
(10094, 4295, 1), -- Race
(10094, 4045, 1), -- Resist Full Magic Attack
-- Elf Renoa
(10095, 4494, 1), -- Raid Boss
(10095, 4824, 1), -- Raid Boss - Level 29
(10095, 4290, 1), -- Race
(10095, 4275, 3), -- Sacred Attack Weak Point
(10095, 4278, 1), -- Dark Attack
(10095, 4045, 1), -- Resist Full Magic Attack
(10095, 4197, 2), -- Hold
-- Renoa's Elpy
(10096, 4293, 1), -- Race
(10096, 4045, 1), -- Resist Full Magic Attack
(10096, 4193, 2), -- BOSS Life Drain
(10096, 4199, 2), -- Decrease P.Atk
-- Renoa's Hog
(10097, 4293, 1), -- Race
(10097, 4045, 1), -- Resist Full Magic Attack
(10097, 4727, 2), -- Shock
-- Sejarr's Servitor
(10098, 4494, 1), -- Raid Boss
(10098, 4825, 1), -- Raid Boss - Level 35
(10098, 4293, 1), -- Race
(10098, 4045, 1), -- Resist Full Magic Attack
(10098, 4173, 3), -- BOSS Might
(10098, 4732, 3), -- BOSS Mortal Blow
(10098, 4737, 3), -- BOSS Spinning Slasher
-- Rotten Tree Repiro
(10099, 4494, 1), -- Raid Boss
(10099, 4826, 1), -- Raid Boss - Level 44
(10099, 4294, 1), -- Race
(10099, 4275, 3), -- Sacred Attack Weak Point
(10099, 4278, 1), -- Dark Attack
(10099, 4045, 1), -- Resist Full Magic Attack
(10099, 4178, 4), -- BOSS Flamestrike
-- Spirit of Sea of Spores
(10100, 4302, 1), -- Race
(10100, 4045, 1), -- Resist Full Magic Attack
(10100, 4782, 4), -- BOSS Heal
(10100, 4213, 4), -- BOSS Haste
-- Fighter of Sea of Spores
(10101, 4292, 1), -- Race
(10101, 4045, 1), -- Resist Full Magic Attack
-- Shacram
(10102, 4494, 1), -- Raid Boss
(10102, 4827, 1), -- Raid Boss - Level 45
(10102, 4292, 1), -- Race
(10102, 4045, 1), -- Resist Full Magic Attack
(10102, 4175, 5), -- BOSS Haste
(10102, 4731, 5), -- BOSS Mortal Blow
(10102, 4172, 5), -- Shock
-- Sorcerer Isirr
(10103, 4494, 1), -- Raid Boss
(10103, 4828, 1), -- Raid Boss - Level 55
(10103, 4295, 1), -- Race
(10103, 4045, 1), -- Resist Full Magic Attack
(10103, 4191, 5), -- BOSS Windstrike
(10103, 4182, 5), -- Poison
(10103, 4185, 5), -- Sleep
-- Isirr's Guard
(10104, 4295, 1), -- Race
(10104, 4045, 1), -- Resist Full Magic Attack
(10104, 4722, 5), -- BOSS Strike
-- Isirr's Guards
(10105, 4295, 1), -- Race
(10105, 4045, 1), -- Resist Full Magic Attack
-- Ghost of the Well Lidia
(10106, 4494, 1), -- Raid Boss
(10106, 4829, 1), -- Raid Boss - Level 63
(10106, 4298, 1), -- Race
(10106, 4278, 1), -- Dark Attack
(10106, 4333, 3), -- Resist Dark Attack
(10106, 4045, 1), -- Resist Full Magic Attack
(10106, 4741, 6), -- BOSS Strike
-- Lidia's Archer
(10107, 4290, 1), -- Race
(10107, 4275, 3), -- Sacred Attack Weak Point
(10107, 4278, 1), -- Dark Attack
(10107, 4045, 1), -- Resist Full Magic Attack
(10107, 4761, 6), -- Shock
-- Lidia's Fighter
(10108, 4290, 1), -- Race
(10108, 4275, 3), -- Sacred Attack Weak Point
(10108, 4278, 1), -- Dark Attack
(10108, 4045, 1), -- Resist Full Magic Attack
-- Antharas Priest Cloe
(10109, 4494, 1), -- Raid Boss
(10109, 4830, 1), -- Raid Boss - Level 74
(10109, 4298, 1), -- Race
(10109, 4278, 1), -- Dark Attack
(10109, 4333, 3), -- Resist Dark Attack
(10109, 4045, 1), -- Resist Full Magic Attack
(10109, 4197, 7), -- Hold
-- Cloe's Servitor
(10110, 4298, 1), -- Race
(10110, 4278, 1), -- Dark Attack
(10110, 4333, 3), -- Resist Dark Attack
(10110, 4045, 1), -- Resist Full Magic Attack
(10110, 4194, 7), -- BOSS Aura Burn
(10110, 4200, 7), -- Decrease Atk.Speed
-- Cloe's Servitor
(10111, 4298, 1), -- Race
(10111, 4278, 1), -- Dark Attack
(10111, 4333, 3), -- Resist Dark Attack
(10111, 4045, 1), -- Resist Full Magic Attack
(10111, 4170, 7), -- BOSS Mortal Blow
-- Agent of Beres, Meana
(10112, 4494, 1), -- Raid Boss
(10112, 4831, 1), -- Raid Boss - Level 30
(10112, 4292, 1), -- Race
(10112, 4045, 1), -- Resist Full Magic Attack
(10112, 4197, 3), -- Hold
-- Meana's Maid
(10113, 4298, 1), -- Race
(10113, 4278, 1), -- Dark Attack
(10113, 4333, 3), -- Resist Dark Attack
(10113, 4045, 1), -- Resist Full Magic Attack
(10113, 4192, 2), -- BOSS HP Drain
(10113, 4199, 2), -- Decrease P.Atk
-- Meana's Guard Doll
(10114, 4290, 1), -- Race
(10114, 4275, 3), -- Sacred Attack Weak Point
(10114, 4278, 1), -- Dark Attack
(10114, 4045, 1), -- Resist Full Magic Attack
(10114, 4726, 2), -- Shock
-- Icarus Sample 1
(10115, 4494, 1), -- Raid Boss
(10115, 4832, 1), -- Raid Boss - Level 40
(10115, 4291, 1), -- Race
(10115, 4045, 1), -- Resist Full Magic Attack
(10115, 4740, 4), -- BOSS Strike
-- Icarus Sample 2
(10116, 4290, 1), -- Race
(10116, 4045, 1), -- Resist Full Magic Attack
(10116, 4755, 3), -- BOSS Power Shot
-- Icarus Sample 3
(10117, 4291, 1), -- Race
(10117, 4045, 1), -- Resist Full Magic Attack
-- Warden of the Execution Ground, Guilotine
(10118, 4494, 1), -- Raid Boss
(10118, 4833, 1), -- Raid Boss - Level 35
(10118, 4290, 1), -- Race
(10118, 4275, 3), -- Sacred Attack Weak Point
(10118, 4278, 1), -- Dark Attack
(10118, 4045, 1), -- Resist Full Magic Attack
(10118, 4175, 4), -- BOSS Haste
(10118, 4726, 4), -- Shock
(10118, 4736, 4), -- BOSS Spinning Slasher
-- Messenger of Fairy Queen Berun
(10119, 4494, 1), -- Raid Boss
(10119, 4834, 1), -- Raid Boss - Level 50
(10119, 4302, 1), -- Race
(10119, 4045, 1), -- Resist Full Magic Attack
(10119, 4197, 5), -- Hold
-- Berun's Officer
(10120, 4302, 1), -- Race
(10120, 4045, 1), -- Resist Full Magic Attack
(10120, 4191, 4), -- BOSS Windstrike
(10120, 4199, 4), -- Decrease P.Atk
-- Berun's Prattler
(10121, 4302, 1), -- Race
(10121, 4045, 1), -- Resist Full Magic Attack
(10121, 4170, 4), -- BOSS Mortal Blow
-- Refugee Hopeful Leo
(10122, 4494, 1), -- Raid Boss
(10122, 4835, 1), -- Raid Boss - Level 56
(10122, 4295, 1), -- Race
(10122, 4045, 1), -- Resist Full Magic Attack
(10122, 4196, 5), -- Decrease Speed
(10122, 4183, 5), -- Decrease P.Atk
(10122, 4190, 5), -- Decrease MP
-- Leo's Servant
(10123, 4300, 1), -- Race
(10123, 4045, 1), -- Resist Full Magic Attack
(10123, 4727, 5), -- Shock
-- Leo's Steward
(10124, 4300, 1), -- Race
(10124, 4045, 1), -- Resist Full Magic Attack
-- Fierce Tiger King Angel
(10125, 4494, 1), -- Raid Boss
(10125, 4836, 1), -- Raid Boss - Level 65
(10125, 4293, 1), -- Race
(10125, 4045, 1), -- Resist Full Magic Attack
(10125, 4176, 6), -- BOSS Reflect Damage
(10125, 4170, 6), -- BOSS Mortal Blow
(10125, 4171, 6), -- BOSS Spinning Slasher
-- Longhorn Golkonda
(10126, 4494, 1), -- Raid Boss
(10126, 4837, 1), -- Raid Boss - Level 79
(10126, 4292, 1), -- Race
(10126, 4045, 1), -- Resist Full Magic Attack
(10126, 4173, 7), -- BOSS Might
(10126, 4169, 7), -- Shock
(10126, 4172, 7), -- Shock
-- Langk Matriarch Rashkos
(10127, 4494, 1), -- Raid Boss
(10127, 4838, 1), -- Raid Boss - Level 24
(10127, 4295, 1), -- Race
(10127, 4045, 1), -- Resist Full Magic Attack
(10127, 4175, 3), -- BOSS Haste
(10127, 4732, 3), -- BOSS Mortal Blow
(10127, 4172, 3), -- Shock
-- Vuku Grand Seer Gharmash
(10128, 4494, 1), -- Raid Boss
(10128, 4839, 1), -- Raid Boss - Level 33
(10128, 4295, 1), -- Race
(10128, 4045, 1), -- Resist Full Magic Attack
(10128, 4197, 3), -- Hold
-- Vuku Shaman
(10129, 4295, 1), -- Race
(10129, 4045, 1), -- Resist Full Magic Attack
(10129, 4194, 3), -- BOSS Aura Burn
(10129, 4203, 3), -- Decrease Speed
-- Gharmash's Pet Trimden
(10130, 4301, 1), -- Race
(10130, 4045, 1), -- Resist Full Magic Attack
(10130, 4722, 3), -- BOSS Strike
-- Carnage Lord Gato
(10131, 4494, 1), -- Raid Boss
(10131, 4840, 1), -- Raid Boss - Level 50
(10131, 4295, 1), -- Race
(10131, 4045, 1), -- Resist Full Magic Attack
(10131, 4741, 5), -- BOSS Strike
-- Gato's Marksman
(10132, 4295, 1), -- Race
(10132, 4045, 1), -- Resist Full Magic Attack
(10132, 4761, 4), -- Shock
-- Gato's Troop Leader
(10133, 4295, 1), -- Race
(10133, 4045, 1), -- Resist Full Magic Attack
-- Leto Chief Talkin
(10134, 4494, 1), -- Raid Boss
(10134, 4841, 1), -- Raid Boss - Level 40
(10134, 4295, 1), -- Race
(10134, 4045, 1), -- Resist Full Magic Attack
(10134, 4178, 4), -- BOSS Flamestrike
-- Talkin's Seer
(10135, 4295, 1), -- Race
(10135, 4045, 1), -- Resist Full Magic Attack
(10135, 4781, 4), -- BOSS Heal
(10135, 4211, 4), -- BOSS Might
-- Talkin's Bodyguard
(10136, 4295, 1), -- Race
(10136, 4045, 1), -- Resist Full Magic Attack
-- Beleth's Seer Sephia
(10137, 4494, 1), -- Raid Boss
(10137, 4842, 1), -- Raid Boss - Level 55
(10137, 4298, 1), -- Race
(10137, 4278, 1), -- Dark Attack
(10137, 4333, 3), -- Resist Dark Attack
(10137, 4045, 1), -- Resist Full Magic Attack
(10137, 4197, 5), -- Hold
-- Soul Drinker
(10138, 4291, 1), -- Race
(10138, 4045, 1), -- Resist Full Magic Attack
(10138, 4191, 5), -- BOSS Windstrike
(10138, 4205, 5), -- Paralysis
-- Sephia's Salve
(10139, 4298, 1), -- Race
(10139, 4278, 1), -- Dark Attack
(10139, 4333, 3), -- Resist Dark Attack
(10139, 4045, 1), -- Resist Full Magic Attack
(10139, 4731, 5), -- BOSS Mortal Blow
-- Hekaton Prime
(10140, 4494, 1), -- Raid Boss
(10140, 4843, 1), -- Raid Boss - Level 
(10140, 4291, 1), -- Race
(10140, 4045, 1), -- Resist Full Magic Attack
(10140, 4195, 6), -- BOSS Twister
(10140, 4184, 6), -- Decrease Atk.Speed
(10140, 4189, 6), -- Paralysis
-- Hekaton Cottus
(10141, 4291, 1), -- Race
(10141, 4045, 1), -- Resist Full Magic Attack
(10141, 4720, 6), -- BOSS Strike
-- Hekaton Chires
(10142, 4300, 1), -- Race
(10142, 4045, 1), -- Resist Full Magic Attack
-- Fire of Wrath Shuriel
(10143, 4494, 1), -- Raid Boss
(10143, 4844, 1), -- Raid Boss - Level 78
(10143, 4297, 1), -- Race
(10143, 4045, 1), -- Resist Full Magic Attack
(10143, 4178, 8), -- BOSS Flamestrike
-- Shuriel's Oracle
(10144, 4295, 1), -- Race
(10144, 4045, 1), -- Resist Full Magic Attack
(10144, 4210, 7), -- BOSS Chant of Life
(10144, 4214, 7), -- BOSS Reflect Damage
-- Shuriel's Paladin
(10145, 4297, 1), -- Race
(10145, 4045, 1), -- Resist Full Magic Attack
-- Serpent Demon Bifrons
(10146, 4494, 1), -- Raid Boss
(10146, 4845, 1), -- Raid Boss - Level 21
(10146, 4292, 1), -- Race
(10146, 4045, 1), -- Resist Full Magic Attack
(10146, 4743, 5), -- BOSS Strike
-- Assassin of Bifrons
(10147, 4295, 1), -- Race
(10147, 4045, 1), -- Resist Full Magic Attack
(10147, 4778, 2), -- BOSS Spear Attack
-- Butcher of Bifrons
(10148, 4295, 1), -- Race
(10148, 4045, 1), -- Resist Full Magic Attack
-- Zombie Lord Crowl
(10149, 4494, 1), -- Raid Boss
(10149, 4846, 1), -- Raid Boss - Level 
(10149, 4290, 1), -- Race
(10149, 4275, 3), -- Sacred Attack Weak Point
(10149, 4278, 1), -- Dark Attack
(10149, 4045, 1), -- Resist Full Magic Attack
(10149, 4746, 2), -- Shock
-- Evil Spirit Archer
(10150, 4290, 1), -- Race
(10150, 4275, 3), -- Sacred Attack Weak Point
(10150, 4278, 1), -- Dark Attack
(10150, 4045, 1), -- Resist Full Magic Attack
(10150, 4756, 2), -- BOSS Power Shot
-- Evil Spirit Warrior
(10151, 4290, 1), -- Race
(10151, 4275, 3), -- Sacred Attack Weak Point
(10151, 4278, 1), -- Dark Attack
(10151, 4045, 1), -- Resist Full Magic Attack
-- Flame Lord Shadar
(10152, 4494, 1), -- Raid Boss
(10152, 4847, 1), -- Raid Boss - Level 
(10152, 4298, 1), -- Race
(10152, 4278, 1), -- Dark Attack
(10152, 4333, 3), -- Resist Dark Attack
(10152, 4045, 1), -- Resist Full Magic Attack
(10152, 4192, 3), -- BOSS HP Drain
(10152, 4182, 3), -- Poison
(10152, 4186, 3), -- Hold
-- Claws of Shadar
(10153, 4302, 1), -- Race
(10153, 4045, 1), -- Resist Full Magic Attack
(10153, 4731, 3), -- BOSS Mortal Blow
-- Whip of Shadar
(10154, 4298, 1), -- Race
(10154, 4278, 1), -- Dark Attack
(10154, 4333, 3), -- Resist Dark Attack
(10154, 4045, 1), -- Resist Full Magic Attack
-- Shaman King Selu
(10155, 4494, 1), -- Raid Boss
(10155, 4848, 1), -- Raid Boss - Level 
(10155, 4295, 1), -- Race
(10155, 4045, 1), -- Resist Full Magic Attack
(10155, 4197, 4), -- Hold
-- Fanatic Shaman
(10156, 4295, 1), -- Race
(10156, 4045, 1), -- Resist Full Magic Attack
(10156, 4194, 3), -- BOSS Aura Burn
(10156, 4199, 3), -- Decrease P.Atk
-- Fanatic Soldier
(10157, 4295, 1), -- Race
(10157, 4045, 1), -- Resist Full Magic Attack
(10157, 4719, 3), -- BOSS Strike
-- King Tarlk
(10158, 4494, 1), -- Raid Boss
(10158, 4849, 1), -- Raid Boss - Level 48
(10158, 4295, 1), -- Race
(10158, 4045, 1), -- Resist Full Magic Attack
(10158, 4174, 5), -- BOSS Shield
(10158, 4720, 5), -- BOSS Strike
(10158, 4735, 5), -- BOSS Spinning Slasher
-- Unicorn Paniel
(10159, 4494, 1), -- Raid Boss
(10159, 4850, 1), -- Raid Boss - Level 
(10159, 4296, 1), -- Race
(10159, 4045, 1), -- Resist Full Magic Attack
(10159, 4178, 5), -- BOSS Flamestrike
-- Unicorn Rapini
(10160, 4296, 1), -- Race
(10160, 4045, 1), -- Resist Full Magic Attack
(10160, 4781, 5), -- BOSS Heal
(10160, 4213, 5), -- BOSS Haste
-- Unicorn Ririf
(10161, 4296, 1), -- Race
(10161, 4045, 1), -- Resist Full Magic Attack
-- Giant Marpanak
(10162, 4494, 1), -- Raid Boss
(10162, 4851, 1), -- Raid Boss - Level 
(10162, 4300, 1), -- Race
(10162, 4045, 1), -- Resist Full Magic Attack
(10162, 4173, 6), -- BOSS Might
(10162, 4730, 6), -- BOSS Mortal Blow
(10162, 4735, 6), -- BOSS Spinning Slasher
-- Roaring Skylancer
(10163, 4494, 1), -- Raid Boss
(10163, 4852, 1), -- Raid Boss - Level 
(10163, 4299, 1), -- Race
(10163, 4045, 1), -- Resist Full Magic Attack
(10163, 4179, 7), -- BOSS Strike
-- Pet of Skylancer
(10164, 4290, 1), -- Race
(10164, 4275, 3), -- Sacred Attack Weak Point
(10164, 4278, 1), -- Dark Attack
(10164, 4045, 1), -- Resist Full Magic Attack
(10164, 4208, 6), -- Shock
-- Slave Warrior of Skylancer
(10165, 4295, 1), -- Race
(10165, 4045, 1), -- Resist Full Magic Attack
-- Ikuntai
(10166, 4494, 1), -- Raid Boss
(10166, 4853, 1), -- Raid Boss - Level 
(10166, 4290, 1), -- Race
(10166, 4275, 3), -- Sacred Attack Weak Point
(10166, 4278, 1), -- Dark Attack
(10166, 4045, 1), -- Resist Full Magic Attack
(10166, 4178, 2), -- BOSS Flamestrike
-- Ikuntai's Servitor
(10167, 4298, 1), -- Race
(10167, 4278, 1), -- Dark Attack
(10167, 4333, 3), -- Resist Dark Attack
(10167, 4045, 1), -- Resist Full Magic Attack
(10167, 4780, 2), -- BOSS Heal
(10167, 4214, 2), -- BOSS Reflect Damage
-- Pawn of Ikuntai
(10168, 4290, 1), -- Race
(10168, 4275, 3), -- Sacred Attack Weak Point
(10168, 4278, 1), -- Dark Attack
(10168, 4045, 1), -- Resist Full Magic Attack
-- Ragraman
(10169, 4494, 1), -- Raid Boss
(10169, 4854, 1), -- Raid Boss - Level 
(10169, 4295, 1), -- Race
(10169, 4045, 1), -- Resist Full Magic Attack
(10169, 4173, 3), -- BOSS Might
(10169, 4720, 3), -- BOSS Strike
(10169, 4172, 3), -- Shock
-- Lizardmen Leader Hellion
(10170, 4494, 1), -- Raid Boss
(10170, 4855, 1), -- Raid Boss - Level 
(10170, 4295, 1), -- Race
(10170, 4045, 1), -- Resist Full Magic Attack
(10170, 4752, 3), -- BOSS Mortal Blow
-- Hellion's Archers
(10171, 4295, 1), -- Race
(10171, 4045, 1), -- Resist Full Magic Attack
(10171, 4757, 3), -- BOSS Power Shot
-- Hellion's Guards
(10172, 4295, 1), -- Race
(10172, 4045, 1), -- Resist Full Magic Attack
-- Tiger King Karuta
(10173, 4494, 1), -- Raid Boss
(10173, 4856, 1), -- Raid Boss - Level 
(10173, 4293, 1), -- Race
(10173, 4045, 1), -- Resist Full Magic Attack
(10173, 4178, 4), -- BOSS Flamestrike
-- Karuta's Follower
(10174, 4295, 1), -- Race
(10174, 4045, 1), -- Resist Full Magic Attack
(10174, 4781, 4), -- BOSS Heal
(10174, 4214, 4), -- BOSS Reflect Damage
-- Karuta's Inferiors
(10175, 4293, 1), -- Race
(10175, 4045, 1), -- Resist Full Magic Attack
-- Witch Wimere
(10176, 4494, 1), -- Raid Boss
(10176, 4857, 1), -- Raid Boss - Level 
(10176, 4298, 1), -- Race
(10176, 4278, 1), -- Dark Attack
(10176, 4333, 3), -- Resist Dark Attack
(10176, 4045, 1), -- Resist Full Magic Attack
(10176, 4192, 5), -- BOSS HP Drain
(10176, 4184, 5), -- Decrease Atk.Speed
(10176, 4188, 5), -- Bleed
-- Wimere's Guard
(10177, 4290, 1), -- Race
(10177, 4275, 3), -- Sacred Attack Weak Point
(10177, 4278, 1), -- Dark Attack
(10177, 4045, 1), -- Resist Full Magic Attack
(10177, 4727, 5), -- Shock
-- Wimere's Servitor
(10178, 4298, 1), -- Race
(10178, 4278, 1), -- Dark Attack
(10178, 4333, 3), -- Resist Dark Attack
(10178, 4045, 1), -- Resist Full Magic Attack
-- Hatos
(10179, 4494, 1), -- Raid Boss
(10179, 4858, 1), -- Raid Boss - Level 
(10179, 4300, 1), -- Race
(10179, 4045, 1), -- Resist Full Magic Attack
(10179, 4197, 6), -- Hold
-- Daughter of Hatos
(10180, 4300, 1), -- Race
(10180, 4045, 1), -- Resist Full Magic Attack
(10180, 4193, 5), -- BOSS Life Drain
(10180, 4198, 5), -- Poison
-- Hatos' Guard
(10181, 4291, 1), -- Race
(10181, 4045, 1), -- Resist Full Magic Attack
(10181, 4721, 5), -- BOSS Strike
-- Demon Kurikups
(10182, 4494, 1), -- Raid Boss
(10182, 4859, 1), -- Raid Boss - Level 
(10182, 4298, 1), -- Race
(10182, 4278, 1), -- Dark Attack
(10182, 4333, 3), -- Resist Dark Attack
(10182, 4045, 1), -- Resist Full Magic Attack
(10182, 4178, 6), -- BOSS Flamestrike
-- Kurikups' Wife
(10183, 4298, 1), -- Race
(10183, 4278, 1), -- Dark Attack
(10183, 4333, 3), -- Resist Dark Attack
(10183, 4045, 1), -- Resist Full Magic Attack
(10183, 4786, 6), -- BOSS Chant of Life
(10183, 4213, 6), -- BOSS Haste
-- Devil Blader of Chaos
(10184, 4290, 1), -- Race
(10184, 4275, 3), -- Sacred Attack Weak Point
(10184, 4278, 1), -- Dark Attack
(10184, 4045, 1), -- Resist Full Magic Attack
-- Tasaba Patriarch Hellena
(10185, 4494, 1), -- Raid Boss
(10185, 4860, 1), -- Raid Boss - Level 
(10185, 4295, 1), -- Race
(10185, 4045, 1), -- Resist Full Magic Attack
(10185, 4751, 3), -- BOSS Mortal Blow
-- Hellena's Marksman
(10186, 4295, 1), -- Race
(10186, 4045, 1), -- Resist Full Magic Attack
(10186, 4761, 3), -- Shock
-- Helena's Soldier
(10187, 4295, 1), -- Race
(10187, 4045, 1), -- Resist Full Magic Attack
-- Apepi
(10188, 4494, 1), -- Raid Boss
(10188, 4861, 1), -- Raid Boss - Level 
(10188, 4292, 1), -- Race
(10188, 4045, 1), -- Resist Full Magic Attack
(10188, 4175, 3), -- BOSS Haste
(10188, 4732, 3), -- BOSS Mortal Blow
(10188, 4172, 3), -- Shock
-- Cronos's Servitor Mumu
(10189, 4494, 1), -- Raid Boss
(10189, 4862, 1), -- Raid Boss - Level 
(10189, 4293, 1), -- Race
(10189, 4045, 1), -- Resist Full Magic Attack
(10189, 4197, 3), -- Hold
-- Mumu's Wizard
(10190, 4293, 1), -- Race
(10190, 4045, 1), -- Resist Full Magic Attack
(10190, 4194, 3), -- BOSS Aura Burn
(10190, 4202, 3), -- Hold
-- Mumu's Warrior
(10191, 4293, 1), -- Race
(10191, 4045, 1), -- Resist Full Magic Attack
(10191, 4722, 3), -- BOSS Strike
-- Earth Protector Panathen
(10192, 4494, 1), -- Raid Boss
(10192, 4863, 1), -- Raid Boss - Level 
(10192, 4292, 1), -- Race
(10192, 4045, 1), -- Resist Full Magic Attack
(10192, 4191, 4), -- BOSS Windstrike
(10192, 4183, 4), -- Decrease P.Atk
(10192, 4189, 4), -- Paralysis
-- Panathen's Knight
(10193, 4292, 1), -- Race
(10193, 4045, 1), -- Resist Full Magic Attack
(10193, 4721, 4), -- BOSS Strike
-- Panathen's Protectors
(10194, 4292, 1), -- Race
(10194, 4045, 1), -- Resist Full Magic Attack
-- Out of Use
(10195, 4292, 1), -- Race
(10195, 4045, 1), -- Resist Full Magic Attack
-- Out of Use
(10196, 4292, 1), -- Race
(10196, 4045, 1), -- Resist Full Magic Attack
-- Out of Use
(10197, 4292, 1), -- Race
(10197, 4045, 1), -- Resist Full Magic Attack
-- Fafurion's Herald Lokness
(10198, 4494, 1), -- Raid Boss
(10198, 4985, 1), -- Raid Boss - Level 
(10198, 4299, 1), -- Race
(10198, 4045, 1), -- Resist Full Magic Attack
(10198, 4176, 6), -- BOSS Reflect Damage
(10198, 4169, 6), -- Shock
(10198, 4172, 6), -- Shock
-- Water Dragon Seer Sheshark
(10199, 4494, 1), -- Raid Boss
(10199, 4986, 1), -- Raid Boss - Level 
(10199, 4295, 1), -- Race
(10199, 4278, 1), -- Dark Attack
(10199, 4333, 3), -- Resist Dark Attack
(10199, 4045, 1), -- Resist Full Magic Attack
(10199, 4197, 7), -- Hold
-- Family of Sheshark
(10200, 4292, 1), -- Race
(10200, 4045, 1), -- Resist Full Magic Attack
(10200, 4195, 7), -- BOSS Twister
(10200, 4200, 7), -- Decrease Atk.Speed
-- Guard of Sheshark
(10201, 4295, 1), -- Race
(10201, 4045, 1), -- Resist Full Magic Attack
(10201, 4724, 7), -- Shock
-- Krokian Padisha Sobekk
(10202, 4494, 1), -- Raid Boss
(10202, 4987, 1), -- Raid Boss - Level 
(10202, 4292, 1), -- Race
(10202, 4045, 1), -- Resist Full Magic Attack
(10202, 4178, 7), -- BOSS Flamestrike
-- Parhit Padisha Sobekk
(10203, 4292, 1), -- Race
(10203, 4045, 1), -- Resist Full Magic Attack
(10203, 4210, 6), -- BOSS Chant of Life
(10203, 4213, 6), -- BOSS Haste
-- Krokian Padisha Sobekk
(10204, 4292, 1), -- Race
(10204, 4045, 1), -- Resist Full Magic Attack
-- Ocean Flame Ashakiel
(10205, 4494, 1), -- Raid Boss
(10205, 4988, 1), -- Raid Boss - Level 
(10205, 4291, 1), -- Race
(10205, 4045, 1), -- Resist Full Magic Attack
(10205, 4193, 8), -- BOSS Life Drain
(10205, 4183, 8), -- Decrease P.Atk
(10205, 4189, 8), -- Paralysis
-- Ashakia's Blade
(10206, 4297, 1), -- Race
(10206, 4045, 1), -- Resist Full Magic Attack
(10206, 4729, 8), -- BOSS Mortal Blow
-- Ashakiel's Rod
(10207, 4297, 1), -- Race
(10207, 4045, 1), -- Resist Full Magic Attack
-- Water Couatle Ateka
(10208, 4494, 1), -- Raid Boss
(10208, 4864, 1), -- Raid Boss - Level 40
(10208, 4292, 1), -- Race
(10208, 4045, 1), -- Resist Full Magic Attack
(10208, 4197, 4), -- Hold
-- Ateka's Shaman
(10209, 4292, 1), -- Race
(10209, 4045, 1), -- Resist Full Magic Attack
(10209, 4193, 3), -- BOSS Life Drain
(10209, 4204, 3), -- Bleed
-- Ateka's Grunt
(10210, 4292, 1), -- Race
(10210, 4045, 1), -- Resist Full Magic Attack
(10210, 4721, 3), -- BOSS Strike
-- Sebek
(10211, 4494, 1), -- Raid Boss
(10211, 4865, 1), -- Raid Boss - Level 36
(10211, 4292, 1), -- Race
(10211, 4045, 1), -- Resist Full Magic Attack
(10211, 4741, 3), -- BOSS Strike
-- Sebek's Priest
(10212, 4295, 1), -- Race
(10212, 4045, 1), -- Resist Full Magic Attack
(10212, 4756, 5), -- BOSS Power Shot
-- Sebek's Fanatic
(10213, 4292, 1), -- Race
(10213, 4045, 1), -- Resist Full Magic Attack
-- Fafurion's Page Sika
(10214, 4494, 1), -- Raid Boss
(10214, 4866, 1), -- Raid Boss - Level 40
(10214, 4295, 1), -- Race
(10214, 4045, 1), -- Resist Full Magic Attack
(10214, 4197, 4), -- Hold
-- Sika's Wizard
(10215, 4296, 1), -- Race
(10215, 4045, 1), -- Resist Full Magic Attack
(10215, 4191, 3), -- BOSS Windstrike
(10215, 4200, 3), -- Decrease Atk.Speed
-- Sika's Fighter
(10216, 4296, 1), -- Race
(10216, 4045, 1), -- Resist Full Magic Attack
(10216, 4732, 3), -- BOSS Mortal Blow
-- Cursed Clara
(10217, 4494, 1), -- Raid Boss
(10217, 4867, 1), -- Raid Boss - Level 
(10217, 4292, 1), -- Race
(10217, 4045, 1), -- Resist Full Magic Attack
(10217, 4752, 5), -- BOSS Mortal Blow
-- Clara's Marksman
(10218, 4295, 1), -- Race
(10218, 4045, 1), -- Resist Full Magic Attack
(10218, 4777, 4), -- BOSS Spear Attack
-- Clara's Pawn
(10219, 4295, 1), -- Race
(10219, 4045, 1), -- Resist Full Magic Attack
-- Death Lord Hallate
(10220, 4494, 1), -- Raid Boss
(10220, 4868, 1), -- Raid Boss - Level 
(10220, 4290, 1), -- Race
(10220, 4278, 1), -- Dark Attack
(10220, 4333, 3), -- Resist Dark Attack
(10220, 4045, 1), -- Resist Full Magic Attack
(10220, 4197, 7), -- Hold
-- Death Mage Krician
(10221, 4290, 1), -- Race
(10221, 4275, 3), -- Sacred Attack Weak Point
(10221, 4278, 1), -- Dark Attack
(10221, 4045, 1), -- Resist Full Magic Attack
(10221, 4194, 7), -- BOSS Aura Burn
(10221, 4204, 7), -- Bleed
-- Death Fighter Harik
(10222, 4290, 1), -- Race
(10222, 4275, 3), -- Sacred Attack Weak Point
(10222, 4278, 1), -- Dark Attack
(10222, 4045, 1), -- Resist Full Magic Attack
(10222, 4169, 7), -- Shock
-- Soul Collector Acheron
(10223, 4494, 1), -- Raid Boss
(10223, 4869, 1), -- Raid Boss - Level 35
(10223, 4298, 1), -- Race
(10223, 4275, 3), -- Sacred Attack Weak Point
(10223, 4278, 1), -- Dark Attack
(10223, 4045, 1), -- Resist Full Magic Attack
(10223, 4197, 3), -- Hold
-- Soul Slasher
(10224, 4290, 1), -- Race
(10224, 4275, 3), -- Sacred Attack Weak Point
(10224, 4278, 1), -- Dark Attack
(10224, 4045, 1), -- Resist Full Magic Attack
(10224, 4196, 3), -- Decrease Speed
(10224, 4203, 3), -- Decrease Speed
-- Soul Strainer
(10225, 4290, 1), -- Race
(10225, 4275, 3), -- Sacred Attack Weak Point
(10225, 4278, 1), -- Dark Attack
(10225, 4045, 1), -- Resist Full Magic Attack
(10225, 4727, 3), -- Shock
-- Roaring Lord Kastor
(10226, 4494, 1), -- Raid Boss
(10226, 4870, 1), -- Raid Boss - Level 
(10226, 4295, 1), -- Race
(10226, 4045, 1), -- Resist Full Magic Attack
(10226, 4178, 6), -- BOSS Flamestrike
-- Kastor's Seer
(10227, 4295, 1), -- Race
(10227, 4045, 1), -- Resist Full Magic Attack
(10227, 4779, 6), -- BOSS Heal
(10227, 4213, 6), -- BOSS Haste
-- Kastor's Prefect
(10228, 4295, 1), -- Race
(10228, 4045, 1), -- Resist Full Magic Attack
(10228, 4273, 2), -- Resist Dagger
-- Storm Winged Naga
(10229, 4494, 1), -- Raid Boss
(10229, 4871, 1), -- Raid Boss - Level 
(10229, 4299, 1), -- Race
(10229, 4045, 1), -- Resist Full Magic Attack
(10229, 4175, 8), -- BOSS Haste
(10229, 4170, 8), -- BOSS Mortal Blow
(10229, 4172, 8), -- Shock
-- Timak Seer Ragoth
(10230, 4494, 1), -- Raid Boss
(10230, 4872, 1), -- Raid Boss - Level 
(10230, 4295, 1), -- Race
(10230, 4045, 1), -- Resist Full Magic Attack
(10230, 4195, 5), -- BOSS Twister
(10230, 4184, 5), -- Decrease Atk.Speed
(10230, 4187, 5), -- Decrease Speed
-- Ragoth's Guard
(10231, 4295, 1), -- Race
(10231, 4045, 1), -- Resist Full Magic Attack
(10231, 4741, 5), -- BOSS Strike
-- Ragoth's Herald
(10232, 4295, 1), -- Race
(10232, 4045, 1), -- Resist Full Magic Attack
-- Spirit of Andras, the Betrayer
(10233, 4494, 1), -- Raid Boss
(10233, 4873, 1), -- Raid Boss - Level 69
(10233, 4290, 1), -- Race
(10233, 4275, 3), -- Sacred Attack Weak Point
(10233, 4278, 1), -- Dark Attack
(10233, 4045, 1), -- Resist Full Magic Attack
(10233, 4273, 2), -- Resist Dagger
(10233, 4274, 1), -- Blunt Attack Weak Point
(10233, 4173, 6), -- BOSS Might
(10233, 4721, 6), -- BOSS Strike
(10233, 4736, 6), -- BOSS Spinning Slasher
-- Ancient Weird Drake
(10234, 4494, 1), -- Raid Boss
(10234, 4874, 1), -- Raid Boss - Level 
(10234, 4299, 1), -- Race
(10234, 4045, 1), -- Resist Full Magic Attack
(10234, 4276, 1), -- Archery Attack Weak Point
(10234, 4175, 6), -- BOSS Haste
(10234, 4731, 6), -- BOSS Mortal Blow
(10234, 4172, 6), -- Shock
-- Vanor Chief Kandra
(10235, 4494, 1), -- Raid Boss
(10235, 4875, 1), -- Raid Boss - Level 
(10235, 4295, 1), -- Race
(10235, 4045, 1), -- Resist Full Magic Attack
(10235, 4178, 7), -- BOSS Flamestrike
-- Kandra's Healer
(10236, 4295, 1), -- Race
(10236, 4045, 1), -- Resist Full Magic Attack
(10236, 4209, 7), -- BOSS Heal
(10236, 4212, 7), -- BOSS Shield
-- Kandra's Guard
(10237, 4295, 1), -- Race
(10237, 4045, 1), -- Resist Full Magic Attack
-- Nightmare Drake
(10238, 4494, 1), -- Raid Boss
(10238, 4876, 1), -- Raid Boss - Level 
(10238, 4299, 1), -- Race
(10238, 4045, 1), -- Resist Full Magic Attack
(10238, 4178, 6), -- BOSS Flamestrike
-- Nightmare Shaman
(10239, 4295, 1), -- Race
(10239, 4045, 1), -- Resist Full Magic Attack
(10239, 4786, 6), -- BOSS Chant of Life
(10239, 4212, 6), -- BOSS Shield
-- Nightmare Beast
(10240, 4293, 1), -- Race
(10240, 4045, 1), -- Resist Full Magic Attack
-- Harit Hero Tamash
(10241, 4494, 1), -- Raid Boss
(10241, 4877, 1), -- Raid Boss - Level 55
(10241, 4295, 1), -- Race
(10241, 4045, 1), -- Resist Full Magic Attack
(10241, 4745, 5), -- Shock
-- Tamash's Advisor
(10242, 4295, 1), -- Race
(10242, 4045, 1), -- Resist Full Magic Attack
(10242, 4192, 5), -- BOSS HP Drain
(10242, 4204, 5), -- Bleed
-- Tamash's Servant
(10243, 4295, 1), -- Race
(10243, 4045, 1), -- Resist Full Magic Attack
(10243, 4730, 5), -- BOSS Mortal Blow
-- Last Lesser Giant Olkuth
(10244, 4494, 1), -- Raid Boss
(10244, 4878, 1), -- Raid Boss - Level 
(10244, 4300, 1), -- Race
(10244, 4045, 1), -- Resist Full Magic Attack
(10244, 4174, 8), -- BOSS Shield
(10244, 4724, 8), -- Shock
(10244, 4734, 8), -- BOSS Spinning Slasher
-- Last Lesser Giant Glaki
(10245, 4494, 1), -- Raid Boss
(10245, 4879, 1), -- Raid Boss - Level 
(10245, 4300, 1), -- Race
(10245, 4045, 1), -- Resist Full Magic Attack
(10245, 4194, 8), -- BOSS Aura Burn
(10245, 4188, 8), -- Bleed
(10245, 4182, 8), -- Poison
-- Glaki's Henchman
(10246, 4300, 1), -- Race
(10246, 4045, 1), -- Resist Full Magic Attack
(10246, 4725, 8), -- Shock
-- Glaki's Servant
(10247, 4300, 1), -- Race
(10247, 4045, 1), -- Resist Full Magic Attack
-- Doom Blade Tanatos
(10248, 4494, 1), -- Raid Boss
(10248, 4880, 1), -- Raid Boss - Level 72
(10248, 4290, 1), -- Race
(10248, 4275, 3), -- Sacred Attack Weak Point
(10248, 4278, 1), -- Dark Attack
(10248, 4045, 1), -- Resist Full Magic Attack
(10248, 4273, 2), -- Resist Dagger
(10248, 4274, 1), -- Blunt Attack Weak Point
(10248, 4175, 7), -- BOSS Haste
(10248, 4720, 7), -- BOSS Strike
(10248, 4172, 7), -- Shock
-- Vermilion, Blood Tree
(10249, 4494, 1), -- Raid Boss
(10249, 4881, 1), -- Raid Boss - Level 
(10249, 4294, 1), -- Race
(10249, 4275, 3), -- Sacred Attack Weak Point
(10249, 4278, 1), -- Dark Attack
(10249, 4045, 1), -- Resist Full Magic Attack
(10249, 4273, 2), -- Resist Dagger
(10249, 4274, 1), -- Blunt Attack Weak Point
(10249, 4197, 8), -- Hold
-- Vermilion Spirit
(10250, 4291, 1), -- Race
(10250, 4045, 1), -- Resist Full Magic Attack
(10250, 4276, 1), -- Archery Attack Weak Point
(10250, 4191, 7), -- BOSS Windstrike
(10250, 4198, 7), -- Poison
-- Vermilion Guard
(10251, 4290, 1), -- Race
(10251, 4275, 3), -- Sacred Attack Weak Point
(10251, 4278, 1), -- Dark Attack
(10251, 4045, 1), -- Resist Full Magic Attack
(10251, 4273, 2), -- Resist Dagger
(10251, 4274, 1), -- Blunt Attack Weak Point
(10251, 4739, 7), -- BOSS Strike
-- Palibati Queen Themis
(10252, 4494, 1), -- Raid Boss
(10252, 4882, 1), -- Raid Boss - Level 
(10252, 4292, 1), -- Race
(10252, 4045, 1), -- Resist Full Magic Attack
(10252, 4191, 7), -- BOSS Windstrike
(10252, 4183, 7), -- Decrease P.Atk
(10252, 4189, 7), -- Paralysis
-- Handmaiden of Themis
(10253, 4298, 1), -- Race
(10253, 4278, 1), -- Dark Attack
(10253, 4333, 3), -- Resist Dark Attack
(10253, 4045, 1), -- Resist Full Magic Attack
(10253, 4192, 6), -- BOSS HP Drain
(10253, 4202, 6), -- Hold
-- Themis's Sentinel
(10254, 4290, 1), -- Race
(10254, 4275, 3), -- Sacred Attack Weak Point
(10254, 4278, 1), -- Dark Attack
(10254, 4045, 1), -- Resist Full Magic Attack
(10254, 4273, 2), -- Resist Dagger
(10254, 4274, 1), -- Blunt Attack Weak Point
(10254, 4739, 6), -- BOSS Strike
-- Gargoyle Lord Tiphon
(10255, 4494, 1), -- Raid Boss
(10255, 4883, 1), -- Raid Boss - Level 
(10255, 4291, 1), -- Race
(10255, 4045, 1), -- Resist Full Magic Attack
(10255, 4175, 6), -- BOSS Haste
(10255, 4730, 6), -- BOSS Mortal Blow
(10255, 4172, 6), -- Shock
-- Taik High Prefect Arak
(10256, 4494, 1), -- Raid Boss
(10256, 4884, 1), -- Raid Boss - Level 
(10256, 4295, 1), -- Race
(10256, 4045, 1), -- Resist Full Magic Attack
(10256, 4741, 6), -- BOSS Strike
-- Arak's Archer
(10257, 4295, 1), -- Race
(10257, 4045, 1), -- Resist Full Magic Attack
(10257, 4761, 6), -- Shock
-- Arak's Footman
(10258, 4295, 1), -- Race
(10258, 4045, 1), -- Resist Full Magic Attack
-- Zaken's Butcher Krantz
(10259, 4494, 1), -- Raid Boss
(10259, 4885, 1), -- Raid Boss - Level 
(10259, 4295, 1), -- Race
(10259, 4045, 1), -- Resist Full Magic Attack
(10259, 4173, 5), -- BOSS Might
(10259, 4725, 5), -- Shock
(10259, 4735, 5), -- BOSS Spinning Slasher
-- Iron Giant Totem
(10260, 4494, 1), -- Raid Boss
(10260, 4886, 1), -- Raid Boss - Level 45
(10260, 4291, 1), -- Race
(10260, 4045, 1), -- Resist Full Magic Attack
(10260, 4071, 4), -- Resist Archery
(10260, 4273, 2), -- Resist Dagger
(10260, 4274, 1), -- Blunt Attack Weak Point
(10260, 4745, 4), -- Shock
-- Totem Guard Archer
(10261, 4295, 1), -- Race
(10261, 4045, 1), -- Resist Full Magic Attack
(10261, 4760, 5), -- Shock
-- Totem Guard
(10262, 4295, 1), -- Race
(10262, 4045, 1), -- Resist Full Magic Attack
-- Malruk's Witch Sekina
(10263, 4494, 1), -- Raid Boss
(10263, 4887, 1), -- Raid Boss - Level 
(10263, 4292, 1), -- Race
(10263, 4045, 1), -- Resist Full Magic Attack
(10263, 4197, 6), -- Hold
-- Sekina's Royal Guard
(10264, 4290, 1), -- Race
(10264, 4275, 3), -- Sacred Attack Weak Point
(10264, 4278, 1), -- Dark Attack
(10264, 4045, 1), -- Resist Full Magic Attack
(10264, 4273, 2), -- Resist Dagger
(10264, 4274, 1), -- Blunt Attack Weak Point
(10264, 4191, 6), -- BOSS Windstrike
(10264, 4204, 6), -- Bleed
-- Sekina's Drake
(10265, 4299, 1), -- Race
(10265, 4045, 1), -- Resist Full Magic Attack
(10265, 4276, 1), -- Archery Attack Weak Point
(10265, 4725, 6), -- Shock
-- Bloody Empress Decarbia
(10266, 4494, 1), -- Raid Boss
(10266, 4888, 1), -- Raid Boss - Level 
(10266, 4298, 1), -- Race
(10266, 4278, 1), -- Dark Attack
(10266, 4333, 3), -- Resist Dark Attack
(10266, 4045, 1), -- Resist Full Magic Attack
(10266, 4192, 8), -- BOSS HP Drain
(10266, 4188, 8), -- Bleed
(10266, 4185, 8), -- Sleep
-- Decarbia's Royal Guard
(10267, 4290, 1), -- Race
(10267, 4275, 3), -- Sacred Attack Weak Point
(10267, 4278, 1), -- Dark Attack
(10267, 4045, 1), -- Resist Full Magic Attack
(10267, 4273, 2), -- Resist Dagger
(10267, 4274, 1), -- Blunt Attack Weak Point
-- Decarbia's Escort
(10268, 4290, 1), -- Race
(10268, 4275, 3), -- Sacred Attack Weak Point
(10268, 4278, 1), -- Dark Attack
(10268, 4045, 1), -- Resist Full Magic Attack
(10268, 4273, 2), -- Resist Dagger
(10268, 4274, 1), -- Blunt Attack Weak Point
(10268, 4170, 7), -- BOSS Mortal Blow
-- Beast Lord Behemoth
(10269, 4494, 1), -- Raid Boss
(10269, 4889, 1), -- Raid Boss - Level 
(10269, 4292, 1), -- Race
(10269, 4045, 1), -- Resist Full Magic Attack
(10269, 4744, 7), -- Shock
-- Behemoth Javeliner
(10270, 4295, 1), -- Race
(10270, 4045, 1), -- Resist Full Magic Attack
(10270, 4774, 7), -- BOSS Spear Attack
-- Behemoth Flare
(10271, 4291, 1), -- Race
(10271, 4045, 1), -- Resist Full Magic Attack
-- Partisan Leader Talakin
(10272, 4494, 1), -- Raid Boss
(10272, 4890, 1), -- Raid Boss - Level 28
(10272, 4295, 1), -- Race
(10272, 4045, 1), -- Resist Full Magic Attack
(10272, 4174, 2), -- BOSS Shield
(10272, 4721, 2), -- BOSS Strike
(10272, 4172, 2), -- Shock
-- Carnamakos
(10273, 4494, 1), -- Raid Boss
(10273, 4891, 1), -- Raid Boss - Level 
(10273, 4291, 1), -- Race
(10273, 4045, 1), -- Resist Full Magic Attack
(10273, 4273, 2), -- Resist Dagger
(10273, 4274, 1), -- Blunt Attack Weak Point
(10273, 4197, 5), -- Hold
-- Carnabarun
(10274, 4291, 1), -- Race
(10274, 4045, 1), -- Resist Full Magic Attack
(10274, 4191, 5), -- BOSS Windstrike
(10274, 4198, 5), -- Poison
-- Carnassiud
(10275, 4291, 1), -- Race
(10275, 4045, 1), -- Resist Full Magic Attack
(10275, 4276, 1), -- Archery Attack Weak Point
(10275, 4739, 5), -- BOSS Strike
-- Death Lord Ipos
(10276, 4494, 1), -- Raid Boss
(10276, 4892, 1), -- Raid Boss - Level 
(10276, 4290, 1), -- Race
(10276, 4275, 3), -- Sacred Attack Weak Point
(10276, 4278, 1), -- Dark Attack
(10276, 4045, 1), -- Resist Full Magic Attack
(10276, 4273, 2), -- Resist Dagger
(10276, 4274, 1), -- Blunt Attack Weak Point
(10276, 4176, 8), -- BOSS Reflect Damage
(10276, 4168, 8), -- BOSS Strike
(10276, 4172, 8), -- Shock
-- Lilith's Witch Marilion
(10277, 4494, 1), -- Raid Boss
(10277, 4893, 1), -- Raid Boss - Level 
(10277, 4298, 1), -- Race
(10277, 4278, 1), -- Dark Attack
(10277, 4333, 3), -- Resist Dark Attack
(10277, 4045, 1), -- Resist Full Magic Attack
(10277, 4178, 5), -- BOSS Flamestrike
-- Dead Soul of Stigma
(10278, 4291, 1), -- Race
(10278, 4278, 1), -- Dark Attack
(10278, 4333, 3), -- Resist Dark Attack
(10278, 4045, 1), -- Resist Full Magic Attack
(10278, 4276, 1), -- Archery Attack Weak Point
(10278, 4779, 4), -- BOSS Heal
(10278, 4211, 4), -- BOSS Might
-- Evil Spirit of Stigma
(10279, 4291, 1), -- Race
(10279, 4278, 1), -- Dark Attack
(10279, 4333, 3), -- Resist Dark Attack
(10279, 4045, 1), -- Resist Full Magic Attack
(10279, 4276, 1), -- Archery Attack Weak Point
-- Pagan Watcher Cerberon
(10280, 4494, 1), -- Raid Boss
(10280, 4894, 1), -- Raid Boss - Level 
(10280, 4297, 1), -- Race
(10280, 4045, 1), -- Resist Full Magic Attack
(10280, 4175, 5), -- BOSS Haste
(10280, 4719, 5), -- BOSS Strike
(10280, 4734, 5), -- BOSS Spinning Slasher
-- Anakim's Nemesis Zakaron
(10281, 4494, 1), -- Raid Boss
(10281, 4895, 1), -- Raid Boss - Level 
(10281, 4298, 1), -- Race
(10281, 4278, 1), -- Dark Attack
(10281, 4333, 3), -- Resist Dark Attack
(10281, 4045, 1), -- Resist Full Magic Attack
(10281, 4175, 7), -- BOSS Haste
(10281, 4724, 7), -- Shock
(10281, 4734, 7), -- BOSS Spinning Slasher
-- Death Lord Shax
(10282, 4494, 1), -- Raid Boss
(10282, 4896, 1), -- Raid Boss - Level 
(10282, 4290, 1), -- Race
(10282, 4275, 3), -- Sacred Attack Weak Point
(10282, 4278, 1), -- Dark Attack
(10282, 4045, 1), -- Resist Full Magic Attack
(10282, 4273, 2), -- Resist Dagger
(10282, 4274, 1), -- Blunt Attack Weak Point
(10282, 4174, 8), -- BOSS Shield
(10282, 4170, 8), -- BOSS Mortal Blow
(10282, 4172, 8), -- Shock
-- Lilith
(10283, 4494, 1), -- Raid Boss
(10283, 4897, 1), -- Raid Boss - Level 
(10283, 4298, 1), -- Race
(10283, 4278, 1), -- Dark Attack
(10283, 4333, 3), -- Resist Dark Attack
(10283, 4045, 1), -- Resist Full Magic Attack
(10283, 4197, 9), -- Hold
-- Lilith's Agent
(10284, 4298, 1), -- Race
(10284, 4278, 1), -- Dark Attack
(10284, 4333, 3), -- Resist Dark Attack
(10284, 4045, 1), -- Resist Full Magic Attack
(10284, 4316, 8), -- BOSS Lilim Drain
(10284, 4198, 8), -- Poison
-- Lilith's Escort
(10285, 4298, 1), -- Race
(10285, 4278, 1), -- Dark Attack
(10285, 4333, 3), -- Resist Dark Attack
(10285, 4045, 1), -- Resist Full Magic Attack
(10285, 4273, 2), -- Resist Dagger
(10285, 4179, 8), -- BOSS Strike
-- Anakim
(10286, 4494, 1), -- Raid Boss
(10286, 4898, 1), -- Raid Boss - Level 
(10286, 4297, 1), -- Race
(10286, 4045, 1), -- Resist Full Magic Attack
(10286, 4314, 9), -- BOSS Holy Light Burst
-- Anakim's Guardian
(10287, 4297, 1), -- Race
(10287, 4045, 1), -- Resist Full Magic Attack
(10287, 4209, 8), -- BOSS Heal
(10287, 4212, 8), -- BOSS Shield
-- Anakim's Royal Guard
(10288, 4297, 1), -- Race
(10288, 4045, 1), -- Resist Full Magic Attack
(10288, 4273, 2), -- Resist Dagger
-- Anakim's Executor
(10289, 4297, 1), -- Race
(10289, 4045, 1), -- Resist Full Magic Attack
-- Daimon the White-Eyed
(10290, 4494, 1), -- Raid Boss
(10290, 4900, 1), -- Raid Boss - Level 
(10290, 4291, 1), -- Race
(10290, 4045, 1), -- Resist Full Magic Attack
(10290, 4192, 8), -- BOSS HP Drain
(10290, 4185, 8), -- Sleep
(10290, 4190, 8), -- Decrease MP
-- Family of Daimon
(10291, 4291, 1), -- Race
(10291, 4045, 1), -- Resist Full Magic Attack
(10291, 4194, 8), -- BOSS Aura Burn
(10291, 4205, 8), -- Paralysis
-- Family of Daimon
(10292, 4291, 1), -- Race
(10292, 4045, 1), -- Resist Full Magic Attack
(10292, 4281, 2), -- Wind Attack Weak Point
(10292, 4276, 1), -- Archery Attack Weak Point
(10292, 4209, 8), -- BOSS Heal
(10292, 4212, 8), -- BOSS Shield
-- Guardian Deity of Hot Springs Hestia
(10293, 4494, 1), -- Raid Boss
(10293, 4901, 1), -- Raid Boss - Level 
(10293, 4302, 1), -- Race
(10293, 4045, 1), -- Resist Full Magic Attack
(10293, 4194, 8), -- BOSS Aura Burn
(10293, 4187, 8), -- Decrease Speed
(10293, 4185, 8), -- Sleep
-- Fighter of Hestia
(10294, 4295, 1), -- Race
(10294, 4045, 1), -- Resist Full Magic Attack
(10294, 4740, 8), -- BOSS Strike
-- Follower of Hestia
(10295, 4295, 1), -- Race
(10295, 4045, 1), -- Resist Full Magic Attack
-- Icicle Emperor Bumbalump
(10296, 4494, 1), -- Raid Boss
(10296, 4902, 1), -- Raid Boss - Level 
(10296, 4295, 1), -- Race
(10296, 4045, 1), -- Resist Full Magic Attack
(10296, 4197, 7), -- Hold
-- Icicle Giant
(10297, 4291, 1), -- Race
(10297, 4045, 1), -- Resist Full Magic Attack
(10297, 4071, 4), -- Resist Archery
(10297, 4273, 2), -- Resist Dagger
(10297, 4274, 1), -- Blunt Attack Weak Point
(10297, 4196, 7), -- Decrease Speed
(10297, 4201, 7), -- Sleep
-- Icicle Giant
(10298, 4291, 1), -- Race
(10298, 4045, 1), -- Resist Full Magic Attack
(10298, 4071, 4), -- Resist Archery
(10298, 4273, 2), -- Resist Dagger
(10298, 4274, 1), -- Blunt Attack Weak Point
(10298, 4169, 7), -- Shock
-- Ketra's Hero Hekaton
(10299, 4494, 1), -- Raid Boss
(10299, 4903, 1), -- Raid Boss - Level 
(10299, 4295, 1), -- Race
(10299, 4045, 1), -- Resist Full Magic Attack
(10299, 4746, 9), -- Shock
-- Scout of Hekaton
(10300, 4295, 1), -- Race
(10300, 4045, 1), -- Resist Full Magic Attack
-- Servant of Hekaton
(10301, 4295, 1), -- Race
(10301, 4045, 1), -- Resist Full Magic Attack
-- Ketra's Commander Tayr
(10302, 4494, 1), -- Raid Boss
(10302, 4904, 1), -- Raid Boss - Level 
(10302, 4295, 1), -- Race
(10302, 4045, 1), -- Resist Full Magic Attack
(10302, 4197, 9), -- Hold
-- Tayr's Aide
(10303, 4295, 1), -- Race
(10303, 4045, 1), -- Resist Full Magic Attack
(10303, 4195, 9), -- BOSS Twister
(10303, 4200, 9), -- Decrease Atk.Speed
-- Tayr's Guard
(10304, 4295, 1), -- Race
(10304, 4045, 1), -- Resist Full Magic Attack
(10304, 4741, 9), -- BOSS Strike
-- Ketra's Chief Brakki
(10305, 4494, 1), -- Raid Boss
(10305, 4905, 1), -- Raid Boss - Level 
(10305, 4292, 1), -- Race
(10305, 4045, 1), -- Resist Full Magic Attack
(10305, 4173, 10), -- BOSS Might
(10305, 4726, 10), -- Shock
(10305, 4736, 10), -- BOSS Spinning Slasher
-- Soul of Fire Nastron
(10306, 4494, 1), -- Raid Boss
(10306, 4906, 1), -- Raid Boss - Level 
(10306, 4296, 1), -- Race
(10306, 4045, 1), -- Resist Full Magic Attack
(10306, 4009, 3), -- Resist Fire
(10306, 4280, 2), -- Water Attack Weak Point
(10306, 4194, 10), -- BOSS Aura Burn
(10306, 4188, 10), -- Bleed
(10306, 4190, 10), -- Decrease MP
-- Family of Nastron
(10307, 4291, 1), -- Race
(10307, 4045, 1), -- Resist Full Magic Attack
(10307, 4009, 3), -- Resist Fire
(10307, 4280, 2), -- Water Attack Weak Point
(10307, 4741, 10), -- BOSS Strike
-- Family of Nastron
(10308, 4291, 1), -- Race
(10308, 4045, 1), -- Resist Full Magic Attack
(10308, 4009, 3), -- Resist Fire
(10308, 4280, 2), -- Water Attack Weak Point
-- Varka's Hero Shadith
(10309, 4494, 1), -- Raid Boss
(10309, 4907, 1), -- Raid Boss - Level 
(10309, 4295, 1), -- Race
(10309, 4045, 1), -- Resist Full Magic Attack
(10309, 4195, 9), -- BOSS Twister
(10309, 4184, 9), -- Decrease Atk.Speed
(10309, 4188, 9), -- Bleed
-- Shadith's Royal Guard Captain
(10310, 4295, 1), -- Race
(10310, 4045, 1), -- Resist Full Magic Attack
(10310, 4741, 8), -- BOSS Strike
-- Shadith's Sentinel
(10311, 4295, 1), -- Race
(10311, 4045, 1), -- Resist Full Magic Attack
-- Varka's Commander Mos
(10312, 4494, 1), -- Raid Boss
(10312, 4908, 1), -- Raid Boss - Level 
(10312, 4295, 1), -- Race
(10312, 4045, 1), -- Resist Full Magic Attack
(10312, 4178, 9), -- BOSS Flamestrike
-- Mos' Aide
(10313, 4295, 1), -- Race
(10313, 4045, 1), -- Resist Full Magic Attack
(10313, 4786, 9), -- BOSS Chant of Life
(10313, 4212, 9), -- BOSS Shield
-- Mos' Guard
(10314, 4295, 1), -- Race
(10314, 4045, 1), -- Resist Full Magic Attack
-- Varka's Chief Horus
(10315, 4494, 1), -- Raid Boss
(10315, 4909, 1), -- Raid Boss - Level 
(10315, 4295, 1), -- Race
(10315, 4045, 1), -- Resist Full Magic Attack
(10315, 4175, 10), -- BOSS Haste
(10315, 4721, 10), -- BOSS Strike
(10315, 4172, 10), -- Shock
-- Soul of Water Ashutar
(10316, 4494, 1), -- Raid Boss
(10316, 4910, 1), -- Raid Boss - Level 
(10316, 4296, 1), -- Race
(10316, 4045, 1), -- Resist Full Magic Attack
(10316, 4010, 3), -- Resist Water
(10316, 4279, 2), -- Fire Attack Weak Point
(10316, 4196, 10), -- Decrease Speed
(10316, 4187, 10), -- Decrease Speed
(10316, 4190, 10), -- Decrease MP
-- Family of Ashutar
(10317, 4291, 1), -- Race
(10317, 4045, 1), -- Resist Full Magic Attack
(10317, 4010, 3), -- Resist Water
(10317, 4279, 2), -- Fire Attack Weak Point
(10317, 4726, 10), -- Shock
-- Family of Ashutar
(10318, 4291, 1), -- Race
(10318, 4045, 1), -- Resist Full Magic Attack
(10318, 4010, 3), -- Resist Water
(10318, 4279, 2), -- Fire Attack Weak Point
-- Ember
(10319, 4494, 1), -- Raid Boss
(10319, 4911, 1), -- Raid Boss - Level 
(10319, 4299, 1), -- Race
(10319, 4045, 1), -- Resist Full Magic Attack
(10319, 4191, 10), -- BOSS Windstrike
(10319, 4188, 10), -- Bleed
(10319, 4189, 10), -- Paralysis
-- Sentinel of Ember
(10320, 4291, 1), -- Race
(10320, 4045, 1), -- Resist Full Magic Attack
(10320, 4071, 4), -- Resist Archery
(10320, 4273, 2), -- Resist Dagger
(10320, 4274, 1), -- Blunt Attack Weak Point
(10320, 4191, 9), -- BOSS Windstrike
(10320, 4199, 9), -- Decrease P.Atk
-- Messenger of Ember
(10321, 4296, 1), -- Race
(10321, 4045, 1), -- Resist Full Magic Attack
(10321, 4209, 9), -- BOSS Heal
(10321, 4213, 9), -- BOSS Haste
-- Demon's Agent Falston
(10322, 4494, 1), -- Raid Boss
(10322, 4912, 1), -- Raid Boss - Level 
(10322, 4298, 1), -- Race
(10322, 4278, 1), -- Dark Attack
(10322, 4333, 3), -- Resist Dark Attack
(10322, 4045, 1), -- Resist Full Magic Attack
(10322, 4197, 6), -- Hold
-- Falston's Disciple
(10323, 4298, 1), -- Race
(10323, 4278, 1), -- Dark Attack
(10323, 4333, 3), -- Resist Dark Attack
(10323, 4045, 1), -- Resist Full Magic Attack
(10323, 4192, 6), -- BOSS HP Drain
(10323, 4204, 6), -- Bleed
-- Servant of Falston
(10324, 4295, 1), -- Race
(10324, 4045, 1), -- Resist Full Magic Attack
(10324, 4169, 6), -- Shock
-- Flame of Splendor Barakiel
(10325, 4494, 1), -- Raid Boss
(10325, 4913, 1), -- Raid Boss - Level 
(10325, 4297, 1), -- Race
(10325, 4045, 1), -- Resist Full Magic Attack
(10325, 4192, 7), -- BOSS HP Drain
(10325, 4188, 7), -- Bleed
(10325, 4190, 7), -- Decrease MP
-- Barakiel's Disciple
(10326, 4297, 1), -- Race
(10326, 4045, 1), -- Resist Full Magic Attack
(10326, 4724, 6), -- Shock
-- Barakiel's Acolyte
(10327, 4297, 1), -- Race
(10327, 4045, 1), -- Resist Full Magic Attack
-- Eilhalder von Hellmann
(10328, 4494, 1), -- Raid Boss
(10328, 4914, 1), -- Raid Boss - Level 
(10328, 4298, 1), -- Race
(10328, 4278, 1), -- Dark Attack
(10328, 4333, 3), -- Resist Dark Attack
(10328, 4045, 1), -- Resist Full Magic Attack
(10328, 4197, 7), -- Hold
-- Violet
(10329, 4298, 1), -- Race
(10329, 4278, 1), -- Dark Attack
(10329, 4333, 3), -- Resist Dark Attack
(10329, 4045, 1), -- Resist Full Magic Attack
(10329, 4193, 7), -- BOSS Life Drain
(10329, 4206, 7), -- Decrease MP
-- Kurstin
(10330, 4298, 1), -- Race
(10330, 4278, 1), -- Dark Attack
(10330, 4333, 3), -- Resist Dark Attack
(10330, 4045, 1), -- Resist Full Magic Attack
(10330, 4193, 7), -- BOSS Life Drain
(10330, 4199, 7), -- Decrease P.Atk
-- Mina
(10331, 4298, 1), -- Race
(10331, 4278, 1), -- Dark Attack
(10331, 4333, 3), -- Resist Dark Attack
(10331, 4045, 1), -- Resist Full Magic Attack
(10331, 4193, 7), -- BOSS Life Drain
(10331, 4203, 7), -- Decrease Speed
-- Dorian
(10332, 4298, 1), -- Race
(10332, 4278, 1), -- Dark Attack
(10332, 4333, 3), -- Resist Dark Attack
(10332, 4045, 1), -- Resist Full Magic Attack
(10332, 4181, 7), -- BOSS Mortal Blow
-- Anakazel
(10333, 4494, 1), -- Raid Boss
(10333, 4915, 1), -- Raid Boss - Level 
(10333, 4298, 1), -- Race
(10333, 4278, 1), -- Dark Attack
(10333, 4333, 3), -- Resist Dark Attack
(10333, 4045, 1), -- Resist Full Magic Attack
(10333, 4175, 2), -- BOSS Haste
(10333, 4723, 2), -- BOSS Strike
(10333, 4172, 2), -- Shock
-- Anakazel
(10334, 4494, 1), -- Raid Boss
(10334, 4916, 1), -- Raid Boss - Level 
(10334, 4298, 1), -- Race
(10334, 4278, 1), -- Dark Attack
(10334, 4333, 3), -- Resist Dark Attack
(10334, 4045, 1), -- Resist Full Magic Attack
(10334, 4175, 3), -- BOSS Haste
(10334, 4723, 3), -- BOSS Strike
(10334, 4172, 3), -- Shock
-- Anakazel
(10335, 4494, 1), -- Raid Boss
(10335, 4917, 1), -- Raid Boss - Level 
(10335, 4298, 1), -- Race
(10335, 4278, 1), -- Dark Attack
(10335, 4333, 3), -- Resist Dark Attack
(10335, 4045, 1), -- Resist Full Magic Attack
(10335, 4175, 4), -- BOSS Haste
(10335, 4723, 4), -- BOSS Strike
(10335, 4172, 4), -- Shock
-- Anakazel
(10336, 4494, 1), -- Raid Boss
(10336, 4918, 1), -- Raid Boss - Level 
(10336, 4298, 1), -- Race
(10336, 4278, 1), -- Dark Attack
(10336, 4333, 3), -- Resist Dark Attack
(10336, 4045, 1), -- Resist Full Magic Attack
(10336, 4175, 5), -- BOSS Haste
(10336, 4723, 5), -- BOSS Strike
(10336, 4172, 5), -- Shock
-- Anakazel
(10337, 4494, 1), -- Raid Boss
(10337, 4919, 1), -- Raid Boss - Level 
(10337, 4298, 1), -- Race
(10337, 4278, 1), -- Dark Attack
(10337, 4333, 3), -- Resist Dark Attack
(10337, 4045, 1), -- Resist Full Magic Attack
(10337, 4175, 6), -- BOSS Haste
(10337, 4723, 6), -- BOSS Strike
(10337, 4172, 6), -- Shock
-- Anakazel
(10338, 4494, 1), -- Raid Boss
(10338, 4920, 1), -- Raid Boss - Level 
(10338, 4298, 1), -- Race
(10338, 4278, 1), -- Dark Attack
(10338, 4333, 3), -- Resist Dark Attack
(10338, 4045, 1), -- Resist Full Magic Attack
(10338, 4175, 8), -- BOSS Haste
(10338, 4723, 8), -- BOSS Strike
(10338, 4172, 8), -- Shock
-- Shadow of Halisha
(10339, 4494, 1), -- Raid Boss
(10339, 4921, 1), -- Raid Boss - Level 81
(10339, 4298, 1), -- Race
(10339, 4278, 1), -- Dark Attack
(10339, 4333, 3), -- Resist Dark Attack
(10339, 4045, 1), -- Resist Full Magic Attack
(10339, 4178, 9), -- BOSS Flamestrike
-- Knight of Shadow
(10340, 4298, 1), -- Race
(10340, 4278, 1), -- Dark Attack
(10340, 4333, 3), -- Resist Dark Attack
(10340, 4045, 1), -- Resist Full Magic Attack
(10340, 4788, 9), -- BOSS Chant of Life
(10340, 4212, 9), -- BOSS Shield
-- Knight of Shadow
(10341, 4292, 1), -- Race
(10341, 4278, 1), -- Dark Attack
(10341, 4333, 3), -- Resist Dark Attack
(10341, 4045, 1), -- Resist Full Magic Attack
-- Shadow of Halisha
(10342, 4494, 1), -- Raid Boss
(10342, 4922, 1), -- Raid Boss - Level 81
(10342, 4298, 1), -- Race
(10342, 4278, 1), -- Dark Attack
(10342, 4333, 3), -- Resist Dark Attack
(10342, 4045, 1), -- Resist Full Magic Attack
(10342, 4748, 9), -- Shock
-- Knight of Shadow
(10343, 4298, 1), -- Race
(10343, 4278, 1), -- Dark Attack
(10343, 4333, 3), -- Resist Dark Attack
(10343, 4045, 1), -- Resist Full Magic Attack
(10343, 4788, 9), -- BOSS Chant of Life
(10343, 4212, 9), -- BOSS Shield
-- Knight of Shadow
(10344, 4298, 1), -- Race
(10344, 4278, 1), -- Dark Attack
(10344, 4333, 3), -- Resist Dark Attack
(10344, 4045, 1), -- Resist Full Magic Attack
-- Knight of shadow
(10345, 4298, 1), -- Race
(10345, 4278, 1), -- Dark Attack
(10345, 4333, 3), -- Resist Dark Attack
(10345, 4045, 1), -- Resist Full Magic Attack
(10345, 4191, 9), -- BOSS Windstrike
(10345, 4203, 9), -- Decrease Speed
-- Shadow of Halisha
(10346, 4494, 1), -- Raid Boss
(10346, 4923, 1), -- Raid Boss - Level 81
(10346, 4298, 1), -- Race
(10346, 4278, 1), -- Dark Attack
(10346, 4333, 3), -- Resist Dark Attack
(10346, 4045, 1), -- Resist Full Magic Attack
(10346, 4197, 9), -- Hold
-- Knight of Shadow
(10347, 4298, 1), -- Race
(10347, 4278, 1), -- Dark Attack
(10347, 4333, 3), -- Resist Dark Attack
(10347, 4045, 1), -- Resist Full Magic Attack
(10347, 4191, 9), -- BOSS Windstrike
(10347, 4199, 9), -- Decrease P.Atk
-- Knight of Shadow
(10348, 4290, 1), -- Race
(10348, 4275, 3), -- Sacred Attack Weak Point
(10348, 4278, 1), -- Dark Attack
(10348, 4045, 1), -- Resist Full Magic Attack
(10348, 4273, 2), -- Resist Dagger
(10348, 4274, 1), -- Blunt Attack Weak Point
(10348, 4743, 9), -- BOSS Strike
-- Shadow of Halisha
(10349, 4494, 1), -- Raid Boss
(10349, 4924, 1), -- Raid Boss - Level 81
(10349, 4298, 1), -- Race
(10349, 4278, 1), -- Dark Attack
(10349, 4333, 3), -- Resist Dark Attack
(10349, 4045, 1), -- Resist Full Magic Attack
(10349, 4195, 9), -- BOSS Twister
(10349, 4184, 9), -- Decrease Atk.Speed
(10349, 4187, 9), -- Decrease Speed
-- Knight of Shadow
(10350, 4298, 1), -- Race
(10350, 4278, 1), -- Dark Attack
(10350, 4333, 3), -- Resist Dark Attack
(10350, 4045, 1), -- Resist Full Magic Attack
(10350, 4191, 9), -- BOSS Windstrike
(10350, 4201, 9), -- Sleep
-- Knight of Shadow
(10351, 4298, 1), -- Race
(10351, 4278, 1), -- Dark Attack
(10351, 4333, 3), -- Resist Dark Attack
(10351, 4045, 1), -- Resist Full Magic Attack
(10351, 4788, 9), -- BOSS Chant of Life
(10351, 4212, 9), -- BOSS Shield
-- Giant Wasteland Basilisk
(10352, 4494, 1), -- Raid Boss
(10352, 4925, 1), -- Raid Boss - Level 
(10352, 4292, 1), -- Race
(10352, 4045, 1), -- Resist Full Magic Attack
(10352, 4197, 3), -- Hold
-- Giant Wasteland Basilisk
(10353, 4292, 1), -- Race
(10353, 4045, 1), -- Resist Full Magic Attack
(10353, 4728, 2), -- Shock
-- Gargoyle Lord Sirocco
(10354, 4494, 1), -- Raid Boss
(10354, 4926, 1), -- Raid Boss - Level 
(10354, 4291, 1), -- Race
(10354, 4045, 1), -- Resist Full Magic Attack
(10354, 4071, 4), -- Resist Archery
(10354, 4273, 2), -- Resist Dagger
(10354, 4274, 1), -- Blunt Attack Weak Point
(10354, 4195, 3), -- BOSS Twister
(10354, 4184, 3), -- Decrease Atk.Speed
(10354, 4186, 3), -- Hold
-- Sirocco's Gargoyle
(10355, 4291, 1), -- Race
(10355, 4045, 1), -- Resist Full Magic Attack
(10355, 4071, 4), -- Resist Archery
(10355, 4273, 2), -- Resist Dagger
(10355, 4274, 1), -- Blunt Attack Weak Point
-- Sirocco's Guards
(10356, 4291, 1), -- Race
(10356, 4045, 1), -- Resist Full Magic Attack
(10356, 4071, 4), -- Resist Archery
(10356, 4273, 2), -- Resist Dagger
(10356, 4274, 1), -- Blunt Attack Weak Point
(10356, 4741, 3), -- BOSS Strike
-- Sukar Wererat Chief
(10357, 4494, 1), -- Raid Boss
(10357, 4927, 1), -- Raid Boss - Level 
(10357, 4295, 1), -- Race
(10357, 4045, 1), -- Resist Full Magic Attack
(10357, 4178, 2), -- BOSS Flamestrike
-- Sukar Wererat Guard
(10358, 4295, 1), -- Race
(10358, 4045, 1), -- Resist Full Magic Attack
(10358, 4733, 2), -- BOSS Mortal Blow
-- Sukar Wererat Priest
(10359, 4295, 1), -- Race
(10359, 4045, 1), -- Resist Full Magic Attack
(10359, 4783, 2), -- BOSS Heal
(10359, 4211, 2), -- BOSS Might
-- Tiger Hornet
(10360, 4494, 1), -- Raid Boss
(10360, 4928, 1), -- Raid Boss - Level 
(10360, 4301, 1), -- Race
(10360, 4045, 1), -- Resist Full Magic Attack
(10360, 4197, 2), -- Hold
-- Yellow Hornet
(10361, 4301, 1), -- Race
(10361, 4045, 1), -- Resist Full Magic Attack
(10361, 4733, 2), -- BOSS Mortal Blow
-- Tracker Leader Sharuk
(10362, 4494, 1), -- Raid Boss
(10362, 4929, 1), -- Raid Boss - Level 
(10362, 4295, 1), -- Race
(10362, 4045, 1), -- Resist Full Magic Attack
(10362, 4743, 2), -- BOSS Strike
-- Sharuk's Tracker
(10363, 4295, 1), -- Race
(10363, 4045, 1), -- Resist Full Magic Attack
-- Sharuk's Marksman
(10364, 4295, 1), -- Race
(10364, 4045, 1), -- Resist Full Magic Attack
-- Patriarch Kuroboros
(10365, 4494, 1), -- Raid Boss
(10365, 4930, 1), -- Raid Boss - Level 
(10365, 4295, 1), -- Race
(10365, 4045, 1), -- Resist Full Magic Attack
(10365, 4174, 2), -- BOSS Shield
(10365, 4723, 2), -- BOSS Strike
(10365, 4738, 2), -- BOSS Spinning Slasher
-- Kuroboros' Priest
(10366, 4494, 1), -- Raid Boss
(10366, 4931, 1), -- Raid Boss - Level 
(10366, 4295, 1), -- Race
(10366, 4045, 1), -- Resist Full Magic Attack
(10366, 4178, 2), -- BOSS Flamestrike
-- Kuroboros' Follower
(10367, 4295, 1), -- Race
(10367, 4045, 1), -- Resist Full Magic Attack
(10367, 4743, 2), -- BOSS Strike
-- Kuroboros' Discipline
(10368, 4295, 1), -- Race
(10368, 4045, 1), -- Resist Full Magic Attack
(10368, 4788, 6), -- BOSS Chant of Life
(10368, 4212, 6), -- BOSS Shield
-- Soul Scavenger
(10369, 4494, 1), -- Raid Boss
(10369, 4932, 1), -- Raid Boss - Level 
(10369, 4290, 1), -- Race
(10369, 4275, 3), -- Sacred Attack Weak Point
(10369, 4278, 1), -- Dark Attack
(10369, 4045, 1), -- Resist Full Magic Attack
(10369, 4273, 2), -- Resist Dagger
(10369, 4274, 1), -- Blunt Attack Weak Point
(10369, 4191, 2), -- BOSS Windstrike
(10369, 4183, 2), -- Decrease P.Atk
(10369, 4185, 2), -- Sleep
-- Corpse Scavenger
(10370, 4290, 1), -- Race
(10370, 4275, 3), -- Sacred Attack Weak Point
(10370, 4278, 1), -- Dark Attack
(10370, 4045, 1), -- Resist Full Magic Attack
(10370, 4273, 2), -- Resist Dagger
(10370, 4274, 1), -- Blunt Attack Weak Point
(10370, 4733, 2), -- BOSS Mortal Blow
-- Anima Scavenger
(10371, 4290, 1), -- Race
(10371, 4275, 3), -- Sacred Attack Weak Point
(10371, 4278, 1), -- Dark Attack
(10371, 4045, 1), -- Resist Full Magic Attack
(10371, 4273, 2), -- Resist Dagger
(10371, 4274, 1), -- Blunt Attack Weak Point
-- Discarded Guardian
(10372, 4494, 1), -- Raid Boss
(10372, 4933, 1), -- Raid Boss - Level 
(10372, 4291, 1), -- Race
(10372, 4045, 1), -- Resist Full Magic Attack
(10372, 4071, 4), -- Resist Archery
(10372, 4273, 2), -- Resist Dagger
(10372, 4274, 1), -- Blunt Attack Weak Point
(10372, 4173, 2), -- BOSS Might
(10372, 4733, 2), -- BOSS Mortal Blow
(10372, 4172, 2), -- Shock
-- Malex Herald of Dagoniel 
(10373, 4494, 1), -- Raid Boss
(10373, 4934, 1), -- Raid Boss - Level 
(10373, 4298, 1), -- Race
(10373, 4278, 1), -- Dark Attack
(10373, 4333, 3), -- Resist Dark Attack
(10373, 4045, 1), -- Resist Full Magic Attack
(10373, 4197, 2), -- Hold
-- Abyss Flyer
(10374, 4292, 1), -- Race
(10374, 4045, 1), -- Resist Full Magic Attack
(10374, 4728, 2), -- Shock
-- Zombie Lord Farakelsus
(10375, 4494, 1), -- Raid Boss
(10375, 4899, 1), -- Raid Boss - Level 
(10375, 4290, 1), -- Race
(10375, 4275, 3), -- Sacred Attack Weak Point
(10375, 4278, 1), -- Dark Attack
(10375, 4045, 1), -- Resist Full Magic Attack
(10375, 4273, 2), -- Resist Dagger
(10375, 4274, 1), -- Blunt Attack Weak Point
(10375, 4178, 2), -- BOSS Flamestrike
-- Warrior Zombie of Farakelsus
(10376, 4290, 1), -- Race
(10376, 4275, 3), -- Sacred Attack Weak Point
(10376, 4278, 1), -- Dark Attack
(10376, 4045, 1), -- Resist Full Magic Attack
(10376, 4273, 2), -- Resist Dagger
(10376, 4274, 1), -- Blunt Attack Weak Point
(10376, 4743, 1), -- BOSS Strike
-- Priest Zombie of Farakelsus
(10377, 4290, 1), -- Race
(10377, 4275, 3), -- Sacred Attack Weak Point
(10377, 4278, 1), -- Dark Attack
(10377, 4045, 1), -- Resist Full Magic Attack
(10377, 4273, 2), -- Resist Dagger
(10377, 4274, 1), -- Blunt Attack Weak Point
(10377, 4783, 1), -- BOSS Heal
(10377, 4213, 1), -- BOSS Haste
-- Madness Beast
(10378, 4494, 1), -- Raid Boss
(10378, 4935, 1), -- Raid Boss - Level 
(10378, 4292, 1), -- Race
(10378, 4045, 1), -- Resist Full Magic Attack
(10378, 4197, 2), -- Hold
-- Dementia Beast
(10379, 4292, 1), -- Race
(10379, 4045, 1), -- Resist Full Magic Attack
(10379, 4743, 1), -- BOSS Strike
-- Kaysha Herald of Icarus
(10380, 4494, 1), -- Raid Boss
(10380, 4936, 1), -- Raid Boss - Level 
(10380, 4298, 1), -- Race
(10380, 4278, 1), -- Dark Attack
(10380, 4333, 3), -- Resist Dark Attack
(10380, 4045, 1), -- Resist Full Magic Attack
(10380, 4192, 2), -- BOSS HP Drain
(10380, 4188, 2), -- Bleed
(10380, 4187, 2), -- Decrease Speed
-- Nightmare Flyer
(10381, 4292, 1), -- Race
(10381, 4045, 1), -- Resist Full Magic Attack
(10381, 4743, 1), -- BOSS Strike
-- Hostile Flyer
(10382, 4292, 1), -- Race
(10382, 4045, 1), -- Resist Full Magic Attack
-- Revenant of Sir Calibus
(10383, 4494, 1), -- Raid Boss
(10383, 4937, 1), -- Raid Boss - Level 
(10383, 4290, 1), -- Race
(10383, 4275, 3), -- Sacred Attack Weak Point
(10383, 4278, 1), -- Dark Attack
(10383, 4045, 1), -- Resist Full Magic Attack
(10383, 4273, 2), -- Resist Dagger
(10383, 4274, 1), -- Blunt Attack Weak Point
(10383, 4197, 3), -- Hold
-- Servant of Calibus
(10384, 4290, 1), -- Race
(10384, 4275, 3), -- Sacred Attack Weak Point
(10384, 4278, 1), -- Dark Attack
(10384, 4045, 1), -- Resist Full Magic Attack
(10384, 4273, 2), -- Resist Dagger
(10384, 4274, 1), -- Blunt Attack Weak Point
(10384, 4733, 3), -- BOSS Mortal Blow
-- Evil Spirit Tempest
(10385, 4494, 1), -- Raid Boss
(10385, 4938, 1), -- Raid Boss - Level 
(10385, 4298, 1), -- Race
(10385, 4278, 1), -- Dark Attack
(10385, 4333, 3), -- Resist Dark Attack
(10385, 4045, 1), -- Resist Full Magic Attack
(10385, 4192, 3), -- BOSS HP Drain
(10385, 4188, 3), -- Bleed
(10385, 4190, 3), -- Decrease MP
-- Ghost of Execution Ground
(10386, 4290, 1), -- Race
(10386, 4275, 3), -- Sacred Attack Weak Point
(10386, 4278, 1), -- Dark Attack
(10386, 4045, 1), -- Resist Full Magic Attack
(10386, 4273, 2), -- Resist Dagger
(10386, 4274, 1), -- Blunt Attack Weak Point
(10386, 4743, 3), -- BOSS Strike
-- Malignant Spirit of Execution Ground
(10387, 4290, 1), -- Race
(10387, 4275, 3), -- Sacred Attack Weak Point
(10387, 4278, 1), -- Dark Attack
(10387, 4045, 1), -- Resist Full Magic Attack
(10387, 4273, 2), -- Resist Dagger
(10387, 4274, 1), -- Blunt Attack Weak Point
-- Red Eye Captain Trakia
(10388, 4494, 1), -- Raid Boss
(10388, 4939, 1), -- Raid Boss - Level 
(10388, 4295, 1), -- Race
(10388, 4045, 1), -- Resist Full Magic Attack
(10388, 4743, 3), -- BOSS Strike
-- Red Eye Archer 
(10389, 4295, 1), -- Race
(10389, 4045, 1), -- Resist Full Magic Attack
-- Red Eye Guards
(10390, 4295, 1), -- Race
(10390, 4045, 1), -- Resist Full Magic Attack
-- Nurka's Messenger
(10391, 4494, 1), -- Raid Boss
(10391, 4940, 1), -- Raid Boss - Level 
(10391, 4295, 1), -- Race
(10391, 4045, 1), -- Resist Full Magic Attack
(10391, 4173, 3), -- BOSS Might
(10391, 4723, 3), -- BOSS Strike
(10391, 4738, 3), -- BOSS Spinning Slasher
-- Captain of Queen's Royal Guards
(10392, 4494, 1), -- Raid Boss
(10392, 4941, 1), -- Raid Boss - Level 
(10392, 4301, 1), -- Race
(10392, 4045, 1), -- Resist Full Magic Attack
(10392, 4197, 3), -- Hold
-- Marsh Stakato Noble
(10393, 4301, 1), -- Race
(10393, 4045, 1), -- Resist Full Magic Attack
(10393, 4733, 3), -- BOSS Mortal Blow
-- Premo Prime
(10394, 4494, 1), -- Raid Boss
(10394, 4942, 1), -- Raid Boss - Level 
(10394, 4295, 1), -- Race
(10394, 4045, 1), -- Resist Full Magic Attack
(10394, 4071, 4), -- Resist Archery
(10394, 4273, 2), -- Resist Dagger
(10394, 4274, 1), -- Blunt Attack Weak Point
(10394, 4176, 3), -- BOSS Reflect Damage
(10394, 4728, 3), -- Shock
(10394, 4738, 3), -- BOSS Spinning Slasher
-- Archon Suscepter
(10395, 4494, 1), -- Raid Boss
(10395, 4943, 1), -- Raid Boss - Level 
(10395, 4291, 1), -- Race
(10395, 4045, 1), -- Resist Full Magic Attack
(10395, 4178, 4), -- BOSS Flamestrike
-- Kusion Suscepter
(10396, 4291, 1), -- Race
(10396, 4045, 1), -- Resist Full Magic Attack
(10396, 4788, 4), -- BOSS Chant of Life
(10396, 4212, 4), -- BOSS Shield
-- Gustos Suscepter
(10397, 4291, 1), -- Race
(10397, 4045, 1), -- Resist Full Magic Attack
(10397, 4743, 4), -- BOSS Strike
-- Eye of Beleth
(10398, 4494, 1), -- Raid Boss
(10398, 4944, 1), -- Raid Boss - Level 
(10398, 4291, 1), -- Race
(10398, 4045, 1), -- Resist Full Magic Attack
(10398, 4194, 3), -- BOSS Aura Burn
(10398, 4182, 3), -- Poison
(10398, 4190, 3), -- Decrease MP
-- Apprentice of Watchman
(10399, 4291, 1), -- Race
(10399, 4045, 1), -- Resist Full Magic Attack
(10399, 4196, 3), -- Decrease Speed
(10399, 4202, 3), -- Hold
-- Page of Watchman
(10400, 4291, 1), -- Race
(10400, 4045, 1), -- Resist Full Magic Attack
(10400, 4783, 3), -- BOSS Heal
(10400, 4213, 3), -- BOSS Haste
-- Skyla
(10401, 4494, 1), -- Raid Boss
(10401, 4945, 1), -- Raid Boss - Level 
(10401, 4295, 1), -- Race
(10401, 4045, 1), -- Resist Full Magic Attack
(10401, 4197, 3), -- Hold
-- Retainer of Skyla
(10402, 4295, 1), -- Race
(10402, 4045, 1), -- Resist Full Magic Attack
(10402, 4733, 3), -- BOSS Mortal Blow
-- Follower of Skyla
(10403, 4295, 1), -- Race
(10403, 4045, 1), -- Resist Full Magic Attack
(10403, 4196, 3), -- Decrease Speed
(10403, 4204, 3), -- Bleed
-- Corsair Captain Kylon
(10404, 4494, 1), -- Raid Boss
(10404, 4946, 1), -- Raid Boss - Level 
(10404, 4290, 1), -- Race
(10404, 4275, 3), -- Sacred Attack Weak Point
(10404, 4278, 1), -- Dark Attack
(10404, 4045, 1), -- Resist Full Magic Attack
(10404, 4273, 2), -- Resist Dagger
(10404, 4274, 1), -- Blunt Attack Weak Point
(10404, 4197, 3), -- Hold
-- Kylon's Pirate
(10405, 4290, 1), -- Race
(10405, 4275, 3), -- Sacred Attack Weak Point
(10405, 4278, 1), -- Dark Attack
(10405, 4045, 1), -- Resist Full Magic Attack
(10405, 4273, 2), -- Resist Dagger
(10405, 4274, 1), -- Blunt Attack Weak Point
(10405, 4733, 3), -- BOSS Mortal Blow
-- Kylon's Mate
(10406, 4290, 1), -- Race
(10406, 4275, 3), -- Sacred Attack Weak Point
(10406, 4278, 1), -- Dark Attack
(10406, 4045, 1), -- Resist Full Magic Attack
(10406, 4273, 2), -- Resist Dagger
(10406, 4274, 1), -- Blunt Attack Weak Point
(10406, 4728, 3), -- Shock
-- Lord Ishka
(10407, 4494, 1), -- Raid Boss
(10407, 4947, 1), -- Raid Boss - Level 
(10407, 4290, 1), -- Race
(10407, 4275, 3), -- Sacred Attack Weak Point
(10407, 4278, 1), -- Dark Attack
(10407, 4045, 1), -- Resist Full Magic Attack
(10407, 4273, 2), -- Resist Dagger
(10407, 4274, 1), -- Blunt Attack Weak Point
(10407, 4197, 6), -- Hold
-- Ishka's Elite Officer
(10408, 4290, 1), -- Race
(10408, 4275, 3), -- Sacred Attack Weak Point
(10408, 4278, 1), -- Dark Attack
(10408, 4045, 1), -- Resist Full Magic Attack
(10408, 4273, 2), -- Resist Dagger
(10408, 4274, 1), -- Blunt Attack Weak Point
(10408, 4733, 5), -- BOSS Mortal Blow
-- Ishka's Elite Soldier
(10409, 4290, 1), -- Race
(10409, 4275, 3), -- Sacred Attack Weak Point
(10409, 4278, 1), -- Dark Attack
(10409, 4045, 1), -- Resist Full Magic Attack
(10409, 4273, 2), -- Resist Dagger
(10409, 4274, 1), -- Blunt Attack Weak Point
(10409, 4743, 5), -- BOSS Strike
-- Road Scavenger Leader
(10410, 4494, 1), -- Raid Boss
(10410, 4948, 1), -- Raid Boss - Level 
(10410, 4295, 1), -- Race
(10410, 4045, 1), -- Resist Full Magic Attack
(10410, 4197, 4), -- Hold
-- Road Scavenger Henchman
(10411, 4295, 1), -- Race
(10411, 4045, 1), -- Resist Full Magic Attack
(10411, 4726, 3), -- Shock
-- Necrosentinel Royal Guard
(10412, 4494, 1), -- Raid Boss
(10412, 4949, 1), -- Raid Boss - Level 
(10412, 4290, 1), -- Race
(10412, 4275, 3), -- Sacred Attack Weak Point
(10412, 4278, 1), -- Dark Attack
(10412, 4045, 1), -- Resist Full Magic Attack
(10412, 4273, 2), -- Resist Dagger
(10412, 4274, 1), -- Blunt Attack Weak Point
(10412, 4753, 4), -- BOSS Mortal Blow
-- Necrosentinel Archer
(10413, 4290, 1), -- Race
(10413, 4275, 3), -- Sacred Attack Weak Point
(10413, 4278, 1), -- Dark Attack
(10413, 4045, 1), -- Resist Full Magic Attack
(10413, 4273, 2), -- Resist Dagger
(10413, 4274, 1), -- Blunt Attack Weak Point
-- Necrosentinel Soldier
(10414, 4290, 1), -- Race
(10414, 4275, 3), -- Sacred Attack Weak Point
(10414, 4278, 1), -- Dark Attack
(10414, 4045, 1), -- Resist Full Magic Attack
(10414, 4273, 2), -- Resist Dagger
(10414, 4274, 1), -- Blunt Attack Weak Point
-- Nakondas
(10415, 4494, 1), -- Raid Boss
(10415, 4950, 1), -- Raid Boss - Level 
(10415, 4299, 1), -- Race
(10415, 4045, 1), -- Resist Full Magic Attack
(10415, 4191, 3), -- BOSS Windstrike
(10415, 4187, 3), -- Decrease Speed
(10415, 4185, 3), -- Sleep
-- Nakondas' Slave 
(10416, 4299, 1), -- Race
(10416, 4045, 1), -- Resist Full Magic Attack
(10416, 4194, 3), -- BOSS Aura Burn
(10416, 4200, 3), -- Decrease Atk.Speed
-- Nakondas' Chain 
(10417, 4298, 1), -- Race
(10417, 4278, 1), -- Dark Attack
(10417, 4333, 3), -- Resist Dark Attack
(10417, 4045, 1), -- Resist Full Magic Attack
(10417, 4783, 3), -- BOSS Heal
(10417, 4213, 3), -- BOSS Haste
-- Dread Avenger Kraven
(10418, 4494, 1), -- Raid Boss
(10418, 4951, 1), -- Raid Boss - Level 
(10418, 4290, 1), -- Race
(10418, 4275, 3), -- Sacred Attack Weak Point
(10418, 4278, 1), -- Dark Attack
(10418, 4045, 1), -- Resist Full Magic Attack
(10418, 4273, 2), -- Resist Dagger
(10418, 4274, 1), -- Blunt Attack Weak Point
(10418, 4197, 4), -- Hold
-- Dread Panther
(10419, 4292, 1), -- Race
(10419, 4045, 1), -- Resist Full Magic Attack
(10419, 4743, 4), -- BOSS Strike
-- Orfen's Handmaiden
(10420, 4494, 1), -- Raid Boss
(10420, 4952, 1), -- Raid Boss - Level 
(10420, 4301, 1), -- Race
(10420, 4045, 1), -- Resist Full Magic Attack
(10420, 4196, 4), -- Decrease Speed
(10420, 4189, 4), -- Paralysis
(10420, 4182, 4), -- Poison
-- Trisalim Escort
(10421, 4301, 1), -- Race
(10421, 4045, 1), -- Resist Full Magic Attack
(10421, 4196, 4), -- Decrease Speed
(10421, 4204, 4), -- Bleed
-- Page of Rotting Tree
(10422, 4294, 1), -- Race
(10422, 4045, 1), -- Resist Full Magic Attack
(10422, 4781, 4), -- BOSS Heal
(10422, 4212, 4), -- BOSS Shield
-- Fairy Queen Timiniel
(10423, 4494, 1), -- Raid Boss
(10423, 4953, 1), -- Raid Boss - Level 
(10423, 4302, 1), -- Race
(10423, 4045, 1), -- Resist Full Magic Attack
(10423, 4194, 5), -- BOSS Aura Burn
(10423, 4187, 5), -- Decrease Speed
(10423, 4190, 5), -- Decrease MP
-- Timiniel's Royal Guards
(10424, 4302, 1), -- Race
(10424, 4045, 1), -- Resist Full Magic Attack
-- Timiniel's Royal Guard Captain
(10425, 4302, 1), -- Race
(10425, 4045, 1), -- Resist Full Magic Attack
(10425, 4742, 5), -- BOSS Strike
-- Betrayer of Urutu Freki
(10426, 4494, 1), -- Raid Boss
(10426, 4954, 1), -- Raid Boss - Level 
(10426, 4295, 1), -- Race
(10426, 4045, 1), -- Resist Full Magic Attack
(10426, 4197, 2), -- Hold
-- Freki's Vampire Bat
(10427, 4292, 1), -- Race
(10427, 4045, 1), -- Resist Full Magic Attack
(10427, 4743, 2), -- BOSS Strike
-- Freki's Wild Bear
(10428, 4293, 1), -- Race
(10428, 4045, 1), -- Resist Full Magic Attack
(10428, 4728, 2), -- Shock
-- Mammon Collector Talos
(10429, 4494, 1), -- Raid Boss
(10429, 4983, 1), -- Raid Boss - Level 
(10429, 4298, 1), -- Race
(10429, 4045, 1), -- Resist Full Magic Attack
(10429, 4071, 4), -- Resist Archery
(10429, 4273, 2), -- Resist Dagger
(10429, 4274, 1), -- Blunt Attack Weak Point
(10429, 4197, 2), -- Hold
-- Talos' Cohort
(10430, 4298, 1), -- Race
(10430, 4045, 1), -- Resist Full Magic Attack
(10430, 4071, 4), -- Resist Archery
(10430, 4273, 2), -- Resist Dagger
(10430, 4274, 1), -- Blunt Attack Weak Point
(10430, 4743, 2), -- BOSS Strike
-- Flamestone Golem
(10431, 4494, 1), -- Raid Boss
(10431, 4984, 1), -- Raid Boss - Level 
(10431, 4291, 1), -- Race
(10431, 4045, 1), -- Resist Full Magic Attack
(10431, 4071, 4), -- Resist Archery
(10431, 4273, 2), -- Resist Dagger
(10431, 4274, 1), -- Blunt Attack Weak Point
(10431, 4178, 4), -- BOSS Flamestrike
-- Elemental of Flame
(10432, 4291, 1), -- Race
(10432, 4045, 1), -- Resist Full Magic Attack
(10432, 4071, 4), -- Resist Archery
(10432, 4273, 2), -- Resist Dagger
(10432, 4274, 1), -- Blunt Attack Weak Point
(10432, 4783, 4), -- BOSS Heal
(10432, 4211, 4), -- BOSS Might
-- Elemental of Spark
(10433, 4291, 1), -- Race
(10433, 4045, 1), -- Resist Full Magic Attack
(10433, 4071, 4), -- Resist Archery
(10433, 4273, 2), -- Resist Dagger
(10433, 4274, 1), -- Blunt Attack Weak Point
-- Bandit Leader Barda
(10434, 4494, 1), -- Raid Boss
(10434, 4955, 1), -- Raid Boss - Level 
(10434, 4295, 1), -- Race
(10434, 4045, 1), -- Resist Full Magic Attack
(10434, 4197, 5), -- Hold
-- Barda's Bandit
(10435, 4295, 1), -- Race
(10435, 4045, 1), -- Resist Full Magic Attack
(10435, 4743, 5), -- BOSS Strike
-- Barda's Shaman
(10436, 4295, 1), -- Race
(10436, 4045, 1), -- Resist Full Magic Attack
(10436, 4195, 5), -- BOSS Twister
(10436, 4199, 5), -- Decrease P.Atk
-- Timak Orc Gosmos
(10437, 4494, 1), -- Raid Boss
(10437, 4956, 1), -- Raid Boss - Level 
(10437, 4295, 1), -- Race
(10437, 4045, 1), -- Resist Full Magic Attack
(10437, 4071, 3), -- Resist Archery
(10437, 4273, 2), -- Resist Dagger
(10437, 4175, 4), -- BOSS Haste
(10437, 4723, 4), -- BOSS Strike
(10437, 4172, 4), -- Shock
-- Thief Kelbar
(10438, 4494, 1), -- Raid Boss
(10438, 4957, 1), -- Raid Boss - Level 
(10438, 4295, 1), -- Race
(10438, 4045, 1), -- Resist Full Magic Attack
(10438, 4071, 3), -- Resist Archery
(10438, 4197, 4), -- Hold
-- Kelbar's Inferior
(10439, 4295, 1), -- Race
(10439, 4045, 1), -- Resist Full Magic Attack
(10439, 4743, 4), -- BOSS Strike
-- Kelbar's Wizard
(10440, 4295, 1), -- Race
(10440, 4045, 1), -- Resist Full Magic Attack
(10440, 4191, 4), -- BOSS Windstrike
(10440, 4200, 4), -- Decrease Atk.Speed
-- Evil Spirit Cyrion
(10441, 4494, 1), -- Raid Boss
(10441, 4958, 1), -- Raid Boss - Level 
(10441, 4299, 1), -- Race
(10441, 4045, 1), -- Resist Full Magic Attack
(10441, 4276, 1), -- Archery Attack Weak Point
(10441, 4273, 3), -- Resist Dagger
(10441, 4195, 4), -- BOSS Twister
(10441, 4186, 4), -- Hold
(10441, 4187, 4), -- Decrease Speed
-- Evil Creature of Forest
(10442, 4293, 1), -- Race
(10442, 4045, 1), -- Resist Full Magic Attack
-- Anger of Forest
(10443, 4293, 1), -- Race
(10443, 4045, 1), -- Resist Full Magic Attack
(10443, 4743, 4), -- BOSS Strike
-- Enmity Ghost Ramdal
(10444, 4494, 1), -- Raid Boss
(10444, 4959, 1), -- Raid Boss - Level 
(10444, 4290, 1), -- Race
(10444, 4275, 3), -- Sacred Attack Weak Point
(10444, 4278, 1), -- Dark Attack
(10444, 4045, 1), -- Resist Full Magic Attack
(10444, 4273, 2), -- Resist Dagger
(10444, 4274, 1), -- Blunt Attack Weak Point
(10444, 4743, 6), -- BOSS Strike
-- Enmity Ghosts
(10445, 4290, 1), -- Race
(10445, 4275, 3), -- Sacred Attack Weak Point
(10445, 4278, 1), -- Dark Attack
(10445, 4045, 1), -- Resist Full Magic Attack
(10445, 4273, 2), -- Resist Dagger
(10445, 4274, 1), -- Blunt Attack Weak Point
-- Shooter of Enmity
(10446, 4290, 1), -- Race
(10446, 4275, 3), -- Sacred Attack Weak Point
(10446, 4278, 1), -- Dark Attack
(10446, 4045, 1), -- Resist Full Magic Attack
(10446, 4273, 2), -- Resist Dagger
(10446, 4274, 1), -- Blunt Attack Weak Point
-- Immortal Savior Mardil
(10447, 4494, 1), -- Raid Boss
(10447, 4960, 1), -- Raid Boss - Level 
(10447, 4298, 1), -- Race
(10447, 4278, 1), -- Dark Attack
(10447, 4333, 3), -- Resist Dark Attack
(10447, 4045, 1), -- Resist Full Magic Attack
(10447, 4178, 7), -- BOSS Flamestrike
-- Immortal Savior 
(10448, 4290, 1), -- Race
(10448, 4275, 3), -- Sacred Attack Weak Point
(10448, 4278, 1), -- Dark Attack
(10448, 4045, 1), -- Resist Full Magic Attack
(10448, 4273, 2), -- Resist Dagger
(10448, 4274, 1), -- Blunt Attack Weak Point
-- Immortal Guide
(10449, 4291, 1), -- Race
(10449, 4045, 1), -- Resist Full Magic Attack
(10449, 4783, 7), -- BOSS Heal
(10449, 4213, 7), -- BOSS Haste
-- Cherub Galaxia
(10450, 4494, 1), -- Raid Boss
(10450, 4961, 1), -- Raid Boss - Level 79
(10450, 4291, 1), -- Race
(10450, 4045, 1), -- Resist Full Magic Attack
(10450, 4194, 8), -- BOSS Aura Burn
(10450, 4183, 8), -- Decrease P.Atk
(10450, 4190, 8), -- Decrease MP
-- Galaxia's Guards
(10451, 4297, 1), -- Race
(10451, 4045, 1), -- Resist Full Magic Attack
(10451, 4194, 8), -- BOSS Aura Burn
(10451, 4202, 8), -- Hold
-- Messenger of Angel
(10452, 4297, 1), -- Race
(10452, 4045, 1), -- Resist Full Magic Attack
(10452, 4210, 8), -- BOSS Chant of Life
(10452, 4212, 8), -- BOSS Shield
-- Meanas Anor
(10453, 4494, 1), -- Raid Boss
(10453, 4962, 1), -- Raid Boss - Level 
(10453, 4299, 1), -- Race
(10453, 4045, 1), -- Resist Full Magic Attack
(10453, 4195, 7), -- BOSS Twister
(10453, 4184, 7), -- Decrease Atk.Speed
(10453, 4188, 7), -- Bleed
-- Wyvern of Marsh
(10454, 4299, 1), -- Race
(10454, 4045, 1), -- Resist Full Magic Attack
(10454, 4196, 6), -- Decrease Speed
(10454, 4204, 6), -- Bleed
-- Succubus of Marsh
(10455, 4298, 1), -- Race
(10455, 4278, 1), -- Dark Attack
(10455, 4333, 3), -- Resist Dark Attack
(10455, 4045, 1), -- Resist Full Magic Attack
(10455, 4780, 6), -- BOSS Heal
(10455, 4211, 6), -- BOSS Might
-- Mirror of Oblivion
(10456, 4494, 1), -- Raid Boss
(10456, 4963, 1), -- Raid Boss - Level 
(10456, 4291, 1), -- Race
(10456, 4045, 1), -- Resist Full Magic Attack
(10456, 4095, 1), -- Damage Shield
(10456, 4178, 4), -- BOSS Flamestrike
-- Shards of Oblivion
(10457, 4290, 1), -- Race
(10457, 4275, 3), -- Sacred Attack Weak Point
(10457, 4278, 1), -- Dark Attack
(10457, 4045, 1), -- Resist Full Magic Attack
(10457, 4273, 2), -- Resist Dagger
(10457, 4274, 1), -- Blunt Attack Weak Point
-- Shadow of Oblivion
(10458, 4290, 1), -- Race
(10458, 4275, 3), -- Sacred Attack Weak Point
(10458, 4278, 1), -- Dark Attack
(10458, 4045, 1), -- Resist Full Magic Attack
(10458, 4273, 2), -- Resist Dagger
(10458, 4274, 1), -- Blunt Attack Weak Point
(10458, 4783, 4), -- BOSS Heal
(10458, 4213, 4), -- BOSS Haste
-- Shards of Oblivion
(10459, 4290, 1), -- Race
(10459, 4275, 3), -- Sacred Attack Weak Point
(10459, 4278, 1), -- Dark Attack
(10459, 4045, 1), -- Resist Full Magic Attack
(10459, 4273, 2), -- Resist Dagger
(10459, 4274, 1), -- Blunt Attack Weak Point
-- Deadman Ereve
(10460, 4494, 1), -- Raid Boss
(10460, 4964, 1), -- Raid Boss - Level 
(10460, 4290, 1), -- Race
(10460, 4275, 3), -- Sacred Attack Weak Point
(10460, 4278, 1), -- Dark Attack
(10460, 4045, 1), -- Resist Full Magic Attack
(10460, 4273, 2), -- Resist Dagger
(10460, 4274, 1), -- Blunt Attack Weak Point
(10460, 4197, 5), -- Hold
-- Ereve's Knight
(10461, 4290, 1), -- Race
(10461, 4275, 3), -- Sacred Attack Weak Point
(10461, 4278, 1), -- Dark Attack
(10461, 4045, 1), -- Resist Full Magic Attack
(10461, 4273, 2), -- Resist Dagger
(10461, 4274, 1), -- Blunt Attack Weak Point
(10461, 4743, 5), -- BOSS Strike
-- Ereve's Squire
(10462, 4290, 1), -- Race
(10462, 4275, 3), -- Sacred Attack Weak Point
(10462, 4278, 1), -- Dark Attack
(10462, 4045, 1), -- Resist Full Magic Attack
(10462, 4273, 2), -- Resist Dagger
(10462, 4274, 1), -- Blunt Attack Weak Point
(10462, 4728, 5), -- Shock
-- Harit Guardian Garangky
(10463, 4494, 1), -- Raid Boss
(10463, 4965, 1), -- Raid Boss - Level 
(10463, 4293, 1), -- Race
(10463, 4045, 1), -- Resist Full Magic Attack
(10463, 4178, 5), -- BOSS Flamestrike
-- Garangky Guard Captain
(10464, 4295, 1), -- Race
(10464, 4045, 1), -- Resist Full Magic Attack
(10464, 4728, 5), -- Shock
-- Garangky Guard Shaman
(10465, 4295, 1), -- Race
(10465, 4045, 1), -- Resist Full Magic Attack
(10465, 4783, 5), -- BOSS Heal
(10465, 4212, 5), -- BOSS Shield
-- Garangky Guard 
(10466, 4295, 1), -- Race
(10466, 4045, 1), -- Resist Full Magic Attack
(10466, 4743, 5), -- BOSS Strike
-- Gorgolos
(10467, 4494, 1), -- Raid Boss
(10467, 4966, 1), -- Raid Boss - Level 
(10467, 4291, 1), -- Race
(10467, 4045, 1), -- Resist Full Magic Attack
(10467, 4281, 2), -- Wind Attack Weak Point
(10467, 4276, 1), -- Archery Attack Weak Point
(10467, 4084, 2), -- Resist Physical Attack
(10467, 4196, 6), -- Decrease Speed
(10467, 4185, 6), -- Sleep
(10467, 4190, 6), -- Decrease MP
-- Cursed Life
(10468, 4291, 1), -- Race
(10468, 4045, 1), -- Resist Full Magic Attack
(10468, 4281, 2), -- Wind Attack Weak Point
(10468, 4276, 1), -- Archery Attack Weak Point
(10468, 4084, 2), -- Resist Physical Attack
-- Cursed Soul
(10469, 4291, 1), -- Race
(10469, 4045, 1), -- Resist Full Magic Attack
(10469, 4281, 2), -- Wind Attack Weak Point
(10469, 4276, 1), -- Archery Attack Weak Point
(10469, 4084, 2), -- Resist Physical Attack
(10469, 4743, 6), -- BOSS Strike
-- Last Titan Utenus
(10470, 4494, 1), -- Raid Boss
(10470, 4967, 1), -- Raid Boss - Level 
(10470, 4300, 1), -- Race
(10470, 4045, 1), -- Resist Full Magic Attack
(10470, 4071, 4), -- Resist Archery
(10470, 4273, 2), -- Resist Dagger
(10470, 4274, 1), -- Blunt Attack Weak Point
(10470, 4197, 6), -- Hold
-- Utenus's Wizard
(10471, 4300, 1), -- Race
(10471, 4045, 1), -- Resist Full Magic Attack
(10471, 4071, 4), -- Resist Archery
(10471, 4273, 2), -- Resist Dagger
(10471, 4274, 1), -- Blunt Attack Weak Point
(10471, 4195, 6), -- BOSS Twister
(10471, 4205, 6), -- Paralysis
-- Utenus's Guard
(10472, 4291, 1), -- Race
(10472, 4045, 1), -- Resist Full Magic Attack
(10472, 4071, 4), -- Resist Archery
(10472, 4273, 2), -- Resist Dagger
(10472, 4274, 1), -- Blunt Attack Weak Point
(10472, 4743, 6), -- BOSS Strike
-- Grave Robber Kim
(10473, 4494, 1), -- Raid Boss
(10473, 4968, 1), -- Raid Boss - Level 
(10473, 4295, 1), -- Race
(10473, 4045, 1), -- Resist Full Magic Attack
(10473, 4197, 5), -- Hold
-- Kim's Gang
(10474, 4295, 1), -- Race
(10474, 4045, 1), -- Resist Full Magic Attack
(10474, 4728, 5), -- Shock
-- Ghost Knight Kabed
(10475, 4494, 1), -- Raid Boss
(10475, 4969, 1), -- Raid Boss - Level 
(10475, 4290, 1), -- Race
(10475, 4275, 3), -- Sacred Attack Weak Point
(10475, 4278, 1), -- Dark Attack
(10475, 4045, 1), -- Resist Full Magic Attack
(10475, 4273, 2), -- Resist Dagger
(10475, 4274, 1), -- Blunt Attack Weak Point
(10475, 4743, 5), -- BOSS Strike
-- Kabed's Soldier
(10476, 4290, 1), -- Race
(10476, 4275, 3), -- Sacred Attack Weak Point
(10476, 4278, 1), -- Dark Attack
(10476, 4045, 1), -- Resist Full Magic Attack
(10476, 4273, 2), -- Resist Dagger
(10476, 4274, 1), -- Blunt Attack Weak Point
-- Kabed's Archer
(10477, 4290, 1), -- Race
(10477, 4275, 3), -- Sacred Attack Weak Point
(10477, 4278, 1), -- Dark Attack
(10477, 4045, 1), -- Resist Full Magic Attack
(10477, 4273, 2), -- Resist Dagger
(10477, 4274, 1), -- Blunt Attack Weak Point
-- Shilen's Priest Hisilrome
(10478, 4494, 1), -- Raid Boss
(10478, 4970, 1), -- Raid Boss - Level 
(10478, 4292, 1), -- Race
(10478, 4045, 1), -- Resist Full Magic Attack
(10478, 4195, 6), -- BOSS Twister
(10478, 4182, 6), -- Poison
(10478, 4189, 6), -- Paralysis
-- Hisilrome's Servitor
(10479, 4292, 1), -- Race
(10479, 4045, 1), -- Resist Full Magic Attack
(10479, 4192, 6), -- BOSS HP Drain
(10479, 4204, 6), -- Bleed
-- Hisilrome's Page
(10480, 4292, 1), -- Race
(10480, 4045, 1), -- Resist Full Magic Attack
(10480, 4783, 6), -- BOSS Heal
(10480, 4211, 6), -- BOSS Might
-- Magus Kenishee
(10481, 4494, 1), -- Raid Boss
(10481, 4971, 1), -- Raid Boss - Level 
(10481, 4298, 1), -- Race
(10481, 4278, 1), -- Dark Attack
(10481, 4333, 3), -- Resist Dark Attack
(10481, 4045, 1), -- Resist Full Magic Attack
(10481, 4178, 5), -- BOSS Flamestrike
-- Shadow of Kenishee
(10482, 4298, 1), -- Race
(10482, 4278, 1), -- Dark Attack
(10482, 4333, 3), -- Resist Dark Attack
(10482, 4045, 1), -- Resist Full Magic Attack
(10482, 4743, 5), -- BOSS Strike
-- Phantom of Kenishee
(10483, 4298, 1), -- Race
(10483, 4278, 1), -- Dark Attack
(10483, 4333, 3), -- Resist Dark Attack
(10483, 4045, 1), -- Resist Full Magic Attack
(10483, 4788, 6), -- BOSS Chant of Life
(10483, 4212, 6), -- BOSS Shield
-- Zaken's Chief Mate Tillion
(10484, 4494, 1), -- Raid Boss
(10484, 4972, 1), -- Raid Boss - Level 
(10484, 4290, 1), -- Race
(10484, 4275, 3), -- Sacred Attack Weak Point
(10484, 4278, 1), -- Dark Attack
(10484, 4045, 1), -- Resist Full Magic Attack
(10484, 4273, 2), -- Resist Dagger
(10484, 4274, 1), -- Blunt Attack Weak Point
(10484, 4197, 5), -- Hold
-- Tillion's Inferior
(10485, 4290, 1), -- Race
(10485, 4275, 3), -- Sacred Attack Weak Point
(10485, 4278, 1), -- Dark Attack
(10485, 4045, 1), -- Resist Full Magic Attack
(10485, 4273, 2), -- Resist Dagger
(10485, 4274, 1), -- Blunt Attack Weak Point
(10485, 4743, 4), -- BOSS Strike
-- Tillion's Bat
(10486, 4292, 1), -- Race
(10486, 4045, 1), -- Resist Full Magic Attack
(10486, 4743, 4), -- BOSS Strike
-- Water Spirit Lian
(10487, 4494, 1), -- Raid Boss
(10487, 4973, 1), -- Raid Boss - Level 
(10487, 4302, 1), -- Race
(10487, 4045, 1), -- Resist Full Magic Attack
(10487, 4010, 3), -- Resist Water
(10487, 4279, 2), -- Fire Attack Weak Point
(10487, 4197, 4), -- Hold
-- Spirit of Gildor
(10488, 4302, 1), -- Race
(10488, 4045, 1), -- Resist Full Magic Attack
(10488, 4010, 3), -- Resist Water
(10488, 4279, 2), -- Fire Attack Weak Point
(10488, 4196, 3), -- Decrease Speed
(10488, 4200, 3), -- Decrease Atk.Speed
-- Guardian of Gildor
(10489, 4302, 1), -- Race
(10489, 4045, 1), -- Resist Full Magic Attack
(10489, 4010, 3), -- Resist Water
(10489, 4279, 2), -- Fire Attack Weak Point
(10489, 4743, 3), -- BOSS Strike
-- Gwindorr
(10490, 4494, 1), -- Raid Boss
(10490, 4974, 1), -- Raid Boss - Level 
(10490, 4292, 1), -- Race
(10490, 4045, 1), -- Resist Full Magic Attack
(10490, 4743, 4), -- BOSS Strike
-- Guard of Gwindorr
(10491, 4295, 1), -- Race
(10491, 4045, 1), -- Resist Full Magic Attack
-- Follower of Gwindorr
(10492, 4295, 1), -- Race
(10492, 4045, 1), -- Resist Full Magic Attack
-- Eva's Spirit Niniel
(10493, 4494, 1), -- Raid Boss
(10493, 4975, 1), -- Raid Boss - Level 
(10493, 4293, 1), -- Race
(10493, 4045, 1), -- Resist Full Magic Attack
(10493, 4191, 5), -- BOSS Windstrike
(10493, 4188, 5), -- Bleed
(10493, 4185, 5), -- Sleep
-- Spirit of Niniel
(10494, 4302, 1), -- Race
(10494, 4045, 1), -- Resist Full Magic Attack
(10494, 4733, 5), -- BOSS Mortal Blow
-- Spirit of Niniel
(10495, 4302, 1), -- Race
(10495, 4045, 1), -- Resist Full Magic Attack
-- Fafurion's Envoy Pingolpin
(10496, 4494, 1), -- Raid Boss
(10496, 4976, 1), -- Raid Boss - Level 
(10496, 4301, 1), -- Race
(10496, 4085, 1), -- Critical Power
(10496, 4086, 1), -- Critical Chance
(10496, 4045, 1), -- Resist Full Magic Attack
(10496, 4279, 2), -- Fire Attack Weak Point
(10496, 4197, 5), -- Hold
-- Fafurion's Envoy   
(10497, 4301, 1), -- Race
(10497, 4045, 1), -- Resist Full Magic Attack
(10497, 4743, 5), -- BOSS Strike
-- Fafurion's Henchman Istary
(10498, 4494, 1), -- Raid Boss
(10498, 4977, 1), -- Raid Boss - Level 
(10498, 4292, 1), -- Race
(10498, 4045, 1), -- Resist Full Magic Attack
(10498, 4191, 5), -- BOSS Windstrike
(10498, 4183, 5), -- Decrease P.Atk
(10498, 4182, 5), -- Poison
-- Alligator of Istary
(10499, 4292, 1), -- Race
(10499, 4045, 1), -- Resist Full Magic Attack
(10499, 4728, 4), -- Shock
-- Alligator of Istary
(10500, 4292, 1), -- Race
(10500, 4045, 1); -- Resist Full Magic Attack

INSERT INTO npcskills VALUES
-- Queen Ant
(12001, 4301, 1), -- Race
(12001, 4021, 1), -- Queen Ant
(12001, 4045, 1), -- Resist Full Magic Attack
(12001, 4017, 1),
(12001, 4018, 1),
(12001, 4019, 1),
-- Queen Ant Larva
(12002, 4301, 1), -- Race
(12002, 4045, 1), -- Resist Full Magic Attack
-- Nurse Ant
(12003, 4301, 1), -- Race
(12003, 4045, 1), -- Resist Full Magic Attack
-- Guard Ant
(12004, 4301, 1), -- Race
(12004, 4045, 1), -- Resist Full Magic Attack
-- Royal Guard Ant
(12005, 4301, 1), -- Race
(12005, 4045, 1), -- Resist Full Magic Attack
-- Kat the Cat
(12006, 4293, 1), -- Race
(12006, 4121, 1), -- Summoned Monster Magic Protection
(12006, 4025, 2), -- Master Recharge
-- Kat the Cat
(12007, 4293, 1), -- Race
(12007, 4121, 1), -- Summoned Monster Magic Protection
(12007, 4025, 3), -- Master Recharge
-- Kat the Cat
(12008, 4293, 1), -- Race
(12008, 4121, 1), -- Summoned Monster Magic Protection
(12008, 4025, 4), -- Master Recharge
-- Duke Lewin Waldner
(12009, 4290, 1), -- Race
(12009, 4045, 1), -- Resist Full Magic Attack
-- Cronenberg
(12010, 4290, 1), -- Race
(12010, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12011, 4290, 1), -- Race
(12011, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12012, 4290, 1), -- Race
(12012, 4045, 1), -- Resist Full Magic Attack
(12012, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12013, 4290, 1), -- Race
(12013, 4045, 1), -- Resist Full Magic Attack
(12013, 4027, 1), -- Gludio Heal
-- Court Guard
(12014, 4290, 1), -- Race
(12014, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12015, 4290, 1), -- Race
(12015, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12016, 4290, 1), -- Race
(12016, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12017, 4290, 1), -- Race
(12017, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12018, 4290, 1), -- Race
(12018, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12019, 4290, 1), -- Race
(12019, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12020, 4290, 1), -- Race
(12020, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12021, 4290, 1), -- Race
(12021, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12022, 4290, 1), -- Race
(12022, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12023, 4290, 1), -- Race
(12023, 4045, 1), -- Resist Full Magic Attack
-- Headquarters
(12024, 4290, 1), -- Race
(12024, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12025, 4290, 1), -- Race
(12025, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12026, 4290, 1), -- Race
(12026, 4045, 1), -- Resist Full Magic Attack
(12026, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12027, 4290, 1), -- Race
(12027, 4045, 1), -- Resist Full Magic Attack
(12027, 4027, 1), -- Gludio Heal
-- Court Guard
(12028, 4290, 1), -- Race
(12028, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12029, 4290, 1), -- Race
(12029, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12030, 4290, 1), -- Race
(12030, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12031, 4290, 1), -- Race
(12031, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12032, 4290, 1), -- Race
(12032, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12033, 4290, 1), -- Race
(12033, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12034, 4290, 1), -- Race
(12034, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12035, 4290, 1), -- Race
(12035, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12036, 4290, 1), -- Race
(12036, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12037, 4290, 1), -- Race
(12037, 4045, 1), -- Resist Full Magic Attack
-- Bloody Lord Nurka
(12038, 4295, 1), -- Race
(12038, 4045, 1), -- Resist Full Magic Attack
-- Partisan Healer
(12039, 4295, 1), -- Race
(12039, 4045, 1), -- Resist Full Magic Attack
-- Partisan Court Guard
(12040, 4295, 1), -- Race
(12040, 4045, 1), -- Resist Full Magic Attack
-- Partisan Court Guard
(12041, 4295, 1), -- Race
(12041, 4045, 1), -- Resist Full Magic Attack
-- Partisan Soldier
(12042, 4295, 1), -- Race
(12042, 4045, 1), -- Resist Full Magic Attack
-- Partisan Sorcerer
(12043, 4295, 1), -- Race
(12043, 4045, 1), -- Resist Full Magic Attack
-- Partisan Archer
(12044, 4295, 1), -- Race
(12044, 4045, 1), -- Resist Full Magic Attack
-- Bloody Lord Nurka
(12045, 4295, 1), -- Race
(12045, 4045, 1), -- Resist Full Magic Attack
-- Partisan Healer
(12046, 4295, 1), -- Race
(12046, 4045, 1), -- Resist Full Magic Attack
-- Partisan Court Guard
(12047, 4295, 1), -- Race
(12047, 4045, 1), -- Resist Full Magic Attack
-- Partisan Court Guard
(12048, 4295, 1), -- Race
(12048, 4045, 1), -- Resist Full Magic Attack
-- Partisan Soldier
(12049, 4295, 1), -- Race
(12049, 4045, 1), -- Resist Full Magic Attack
-- Partisan Sorcerer
(12050, 4295, 1), -- Race
(12050, 4045, 1), -- Resist Full Magic Attack
-- Partisan Archer
(12051, 4295, 1), -- Race
(12051, 4045, 1), -- Resist Full Magic Attack
-- Core
(12052, 4291, 1), -- Race
(12052, 4045, 1), -- Resist Full Magic Attack
-- Teleport Cube
(12053, 4290, 1), -- Race
(12053, 4021, 1), -- Queen Ant
(12053, 4045, 1), -- Resist Full Magic Attack
(12053, 4390, 1), -- NPC Abnormal Immunity
-- Death Knight
(12054, 4290, 1), -- Race
(12054, 4275, 3), -- Sacred Attack Weak Point
(12054, 4278, 1), -- Dark Attack
(12054, 4045, 1), -- Resist Full Magic Attack
-- Death Wraith
(12055, 4290, 1), -- Race
(12055, 4275, 3), -- Sacred Attack Weak Point
(12055, 4278, 1), -- Dark Attack
(12055, 4045, 1), -- Resist Full Magic Attack
(12055, 4001, 5), -- NPC Windstrike
-- Dicor
(12056, 4294, 1), -- Race
(12056, 4045, 1), -- Resist Full Magic Attack
(12056, 4035, 4), -- Poison
-- Validus
(12057, 4292, 1), -- Race
(12057, 4045, 1), -- Resist Full Magic Attack
-- Susceptor
(12058, 4291, 1), -- Race
(12058, 4045, 1), -- Resist Full Magic Attack
(12058, 4001, 4), -- NPC Windstrike
-- Perum
(12059, 4291, 1), -- Race
(12059, 4071, 4), -- Resist Archery
(12059, 4273, 2), -- Resist Dagger
(12059, 4274, 1), -- Blunt Attack Weak Point
(12059, 4045, 1), -- Resist Full Magic Attack
(12059, 4001, 4), -- NPC Windstrike
-- Premo
(12060, 4292, 1), -- Race
(12060, 4045, 1), -- Resist Full Magic Attack
-- Mew the Cat
(12061, 4293, 1), -- Race
(12061, 4121, 1), -- Summoned Monster Magic Protection
(12061, 4261, 2), -- Mega Storm Strike
-- Mew the Cat
(12062, 4293, 1), -- Race
(12062, 4121, 1), -- Summoned Monster Magic Protection
(12062, 4261, 3), -- Mega Storm Strike
-- Mew the Cat
(12063, 4293, 1), -- Race
(12063, 4121, 1), -- Summoned Monster Magic Protection
(12063, 4261, 4), -- Mega Storm Strike
-- Unicorn Boxer 
(12064, 4296, 1), -- Race
(12064, 4121, 1), -- Summoned Monster Magic Protection
(12064, 4025, 2), -- Master Recharge
-- Unicorn Boxer 
(12065, 4296, 1), -- Race
(12065, 4121, 1), -- Summoned Monster Magic Protection
(12065, 4025, 3), -- Master Recharge
-- Unicorn Boxer 
(12066, 4296, 1), -- Race
(12066, 4121, 1), -- Summoned Monster Magic Protection
(12066, 4025, 4), -- Master Recharge
-- Unicorn Mirage 
(12067, 4296, 1), -- Race
(12067, 4121, 1), -- Summoned Monster Magic Protection
(12067, 4261, 2), -- Mega Storm Strike
-- Unicorn Mirage 
(12068, 4296, 1), -- Race
(12068, 4121, 1), -- Summoned Monster Magic Protection
(12068, 4261, 3), -- Mega Storm Strike
-- Unicorn Mirage 
(12069, 4296, 1), -- Race
(12069, 4121, 1), -- Summoned Monster Magic Protection
(12069, 4261, 4), -- Mega Storm Strike
-- Shadow
(12070, 4298, 1), -- Race
(12070, 4121, 1), -- Summoned Monster Magic Protection
(12070, 4233, 1), -- Vampiric Attack
-- Shadow
(12071, 4298, 1), -- Race
(12071, 4121, 1), -- Summoned Monster Magic Protection
(12071, 4233, 1), -- Vampiric Attack
-- Shadow
(12072, 4298, 1), -- Race
(12072, 4121, 1), -- Summoned Monster Magic Protection
(12072, 4233, 1), -- Vampiric Attack
-- Silhouette
(12073, 4298, 1), -- Race
(12073, 4121, 1), -- Summoned Monster Magic Protection
(12073, 4260, 2), -- Steel Blood
-- Silhouette
(12074, 4298, 1), -- Race
(12074, 4121, 1), -- Summoned Monster Magic Protection
(12074, 4260, 3), -- Steel Blood
-- Silhouette
(12075, 4298, 1), -- Race
(12075, 4121, 1), -- Summoned Monster Magic Protection
(12075, 4260, 4), -- Steel Blood
-- Hatchling
(12076, 4299, 1), -- Race
(12076, 4121, 1), -- Summoned Monster Magic Protection
-- Wolf
(12077, 4293, 1), -- Race
(12077, 4121, 1), -- Summoned Monster Magic Protection
-- Teleport Cube
(12078, 4290, 1), -- Race
(12078, 4045, 1), -- Resist Full Magic Attack
(12078, 4390, 1), -- NPC Abnormal Immunity
-- Bloody Queen
(12079, 4298, 1), -- Race
(12079, 4278, 1), -- Dark Attack
(12079, 4333, 3), -- Resist Dark Attack
(12079, 4039, 6), -- NPC MP Drain
(12079, 4002, 6), -- NPC HP Drain
(12079, 4035, 6), -- Poison
-- Bloody Queen
(12080, 4298, 1), -- Race
(12080, 4278, 1), -- Dark Attack
(12080, 4333, 3), -- Resist Dark Attack
(12080, 4039, 6), -- NPC MP Drain
(12080, 4002, 6), -- NPC HP Drain
(12080, 4035, 6), -- Poison
-- Royal Gatekeeper
(12081, 4290, 1), -- Race
(12081, 4045, 1), -- Resist Full Magic Attack
-- Bearded Keltir
(12082, 4293, 1), -- Race
-- Aracna
(12083, 4301, 1), -- Race
(12083, 4279, 2), -- Fire Attack Weak Point
-- Bloody Pixy
(12084, 4302, 1), -- Race
-- Satyros
(12085, 4302, 1), -- Race
-- Pan
(12086, 4302, 1), -- Race
-- Pixy
(12087, 4302, 1), -- Race
-- Red Arachne
(12088, 4301, 1), -- Race
(12088, 4279, 2), -- Fire Attack Weak Point
-- Blight Treant
(12089, 4296, 1), -- Race
(12089, 4279, 2), -- Fire Attack Weak Point
(12089, 4277, 3), -- Resist Poison
-- Treant
(12090, 4296, 1), -- Race
(12090, 4279, 2), -- Fire Attack Weak Point
(12090, 4277, 3), -- Resist Poison
-- Pixy Murika
(12091, 4302, 1), -- Race
-- Treant Bremec
(12092, 4296, 1), -- Race
-- Duke Byron Ashton
(12093, 4290, 1), -- Race
(12093, 4045, 1), -- Resist Full Magic Attack
-- Ivano
(12094, 4290, 1), -- Race
(12094, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12095, 4290, 1), -- Race
(12095, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12096, 4290, 1), -- Race
(12096, 4045, 1), -- Resist Full Magic Attack
(12096, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12097, 4290, 1), -- Race
(12097, 4045, 1), -- Resist Full Magic Attack
(12097, 4027, 1), -- Gludio Heal
-- Court Guard
(12098, 4290, 1), -- Race
(12098, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12099, 4290, 1), -- Race
(12099, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12100, 4290, 1), -- Race
(12100, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12101, 4290, 1), -- Race
(12101, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12102, 4290, 1), -- Race
(12102, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12103, 4290, 1), -- Race
(12103, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12104, 4290, 1), -- Race
(12104, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12105, 4290, 1), -- Race
(12105, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12106, 4290, 1), -- Race
(12106, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12107, 4290, 1), -- Race
(12107, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12108, 4290, 1), -- Race
(12108, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12109, 4290, 1), -- Race
(12109, 4045, 1), -- Resist Full Magic Attack
(12109, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12110, 4290, 1), -- Race
(12110, 4045, 1), -- Resist Full Magic Attack
(12110, 4027, 1), -- Gludio Heal
-- Court Guard
(12111, 4290, 1), -- Race
(12111, 4045, 1), -- Resist Full Magic Attack
-- Court Guard
(12112, 4290, 1), -- Race
(12112, 4045, 1), -- Resist Full Magic Attack
-- Court Guard
(12113, 4290, 1), -- Race
(12113, 4045, 1), -- Resist Full Magic Attack
-- Court Guard
(12114, 4290, 1), -- Race
(12114, 4045, 1), -- Resist Full Magic Attack
-- Court Guard
(12115, 4290, 1), -- Race
(12115, 4045, 1), -- Resist Full Magic Attack
-- Court Guard
(12116, 4290, 1), -- Race
(12116, 4045, 1), -- Resist Full Magic Attack
-- Court Guard
(12117, 4290, 1), -- Race
(12117, 4045, 1), -- Resist Full Magic Attack
-- Court Guard
(12118, 4290, 1), -- Race
(12118, 4045, 1), -- Resist Full Magic Attack
-- Court Guard
(12119, 4290, 1), -- Race
(12119, 4045, 1), -- Resist Full Magic Attack
-- Court Guard
(12120, 4290, 1), -- Race
(12120, 4045, 1), -- Resist Full Magic Attack
-- Crosby
(12121, 4290, 1), -- Race
(12121, 4045, 1), -- Resist Full Magic Attack
-- Gibbson
(12122, 4290, 1), -- Race
(12122, 4045, 1), -- Resist Full Magic Attack
-- Dion Holy Artifact
(12123, 4290, 1), -- Race
(12123, 4045, 1), -- Resist Full Magic Attack
-- Baron Carmon Esthus
(12124, 4290, 1), -- Race
(12124, 4045, 1), -- Resist Full Magic Attack
-- Ulric
(12125, 4290, 1), -- Race
(12125, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12126, 4290, 1), -- Race
(12126, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12127, 4290, 1), -- Race
(12127, 4045, 1), -- Resist Full Magic Attack
(12127, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12128, 4290, 1), -- Race
(12128, 4045, 1), -- Resist Full Magic Attack
(12128, 4027, 1), -- Gludio Heal
-- Court Guard
(12129, 4290, 1), -- Race
(12129, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12130, 4290, 1), -- Race
(12130, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12131, 4290, 1), -- Race
(12131, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12132, 4290, 1), -- Race
(12132, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12133, 4290, 1), -- Race
(12133, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12134, 4290, 1), -- Race
(12134, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12135, 4290, 1), -- Race
(12135, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12136, 4290, 1), -- Race
(12136, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12137, 4290, 1), -- Race
(12137, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12138, 4290, 1), -- Race
(12138, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12139, 4290, 1), -- Race
(12139, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12140, 4290, 1), -- Race
(12140, 4045, 1), -- Resist Full Magic Attack
(12140, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12141, 4290, 1), -- Race
(12141, 4045, 1), -- Resist Full Magic Attack
(12141, 4027, 1), -- Gludio Heal
-- Court Guard
(12142, 4290, 1), -- Race
(12142, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12143, 4290, 1), -- Race
(12143, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12144, 4290, 1), -- Race
(12144, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12145, 4290, 1), -- Race
(12145, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12146, 4290, 1), -- Race
(12146, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12147, 4290, 1), -- Race
(12147, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12148, 4290, 1), -- Race
(12148, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12149, 4290, 1), -- Race
(12149, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12150, 4290, 1), -- Race
(12150, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12151, 4290, 1), -- Race
(12151, 4045, 1), -- Resist Full Magic Attack
-- Saul
(12152, 4290, 1), -- Race
(12152, 4045, 1), -- Resist Full Magic Attack
-- Holmes
(12153, 4290, 1), -- Race
(12153, 4045, 1), -- Resist Full Magic Attack
-- Giran Holy Artifact
(12154, 4290, 1), -- Race
(12154, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12155, 4290, 1), -- Race
(12155, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12156, 4290, 1), -- Race
(12156, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12157, 4290, 1), -- Race
(12157, 4045, 1), -- Resist Full Magic Attack
-- Mass Gatekeeper
(12158, 4290, 1), -- Race
(12158, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(12159, 4290, 1), -- Race
(12159, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12160, 4290, 1), -- Race
(12160, 4045, 1), -- Resist Full Magic Attack
-- Royal Gatekeeper
(12161, 4290, 1), -- Race
(12161, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12162, 4290, 1), -- Race
(12162, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12163, 4290, 1), -- Race
(12163, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12164, 4290, 1), -- Race
(12164, 4045, 1), -- Resist Full Magic Attack
-- Mass Gatekeeper
(12165, 4290, 1), -- Race
(12165, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(12166, 4290, 1), -- Race
(12166, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12167, 4290, 1), -- Race
(12167, 4045, 1), -- Resist Full Magic Attack
-- Royal Gatekeeper
(12168, 4290, 1), -- Race
(12168, 4045, 1), -- Resist Full Magic Attack
-- Orfen
(12169, 4296, 1), -- Race
(12169, 4062, 1), -- Orfen
(12169, 4045, 1), -- Resist Full Magic Attack
(12169, 4063, 1),
-- Raikel
(12170, 4292, 1), -- Race
(12170, 4045, 1), -- Resist Full Magic Attack
-- Raikel Leos
(12171, 4292, 1), -- Race
(12171, 4045, 1), -- Resist Full Magic Attack
-- Riba 
(12172, 4301, 1), -- Race
(12172, 4045, 1), -- Resist Full Magic Attack
-- Riba Iren
(12173, 4301, 1), -- Race
(12173, 4045, 1), -- Resist Full Magic Attack
-- Susceptor Prime
(12174, 4291, 1), -- Race
(12174, 4071, 4), -- Resist Archery
(12174, 4273, 2), -- Resist Dagger
(12174, 4274, 1), -- Blunt Attack Weak Point
-- Kat the Cat
(12175, 4293, 1), -- Race
(12175, 4121, 1), -- Summoned Monster Magic Protection
(12175, 4025, 5), -- Master Recharge
-- Kat the Cat
(12176, 4293, 1), -- Race
(12176, 4121, 1), -- Summoned Monster Magic Protection
(12176, 4025, 5), -- Master Recharge
-- Mew the Cat
(12177, 4293, 1), -- Race
(12177, 4121, 1), -- Summoned Monster Magic Protection
(12177, 4261, 5), -- Mega Storm Strike
-- Unicorn Boxer
(12178, 4296, 1), -- Race
(12178, 4121, 1), -- Summoned Monster Magic Protection
(12178, 4025, 5), -- Master Recharge
-- Unicorn Boxer
(12179, 4296, 1), -- Race
(12179, 4121, 1), -- Summoned Monster Magic Protection
(12179, 4025, 5), -- Master Recharge
-- Unicorn Mirage
(12180, 4296, 1), -- Race
(12180, 4121, 1), -- Summoned Monster Magic Protection
(12180, 4261, 5), -- Mega Storm Strike
-- Shadow
(12181, 4298, 1), -- Race
(12181, 4121, 1), -- Summoned Monster Magic Protection
(12181, 4233, 1), -- Vampiric Attack
-- Shadow
(12182, 4298, 1), -- Race
(12182, 4121, 1), -- Summoned Monster Magic Protection
(12182, 4233, 1), -- Vampiric Attack
-- Silhouette
(12183, 4298, 1), -- Race
(12183, 4121, 1), -- Summoned Monster Magic Protection
(12183, 4260, 5), -- Steel Blood
-- Dark Panther
(12184, 4293, 1), -- Race
(12184, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(12185, 4293, 1), -- Race
(12185, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(12186, 4293, 1), -- Race
(12186, 4121, 1), -- Summoned Monster Magic Protection
-- Mechanic Golem
(12187, 4291, 1), -- Race
(12187, 4121, 1), -- Summoned Monster Magic Protection
(12187, 4068, 3), -- Mechanical Cannon
-- Mechanic Golem
(12188, 4291, 1), -- Race
(12188, 4121, 1), -- Summoned Monster Magic Protection
(12188, 4068, 3), -- Mechanical Cannon
-- Mechanic Golem
(12189, 4291, 1), -- Race
(12189, 4121, 1), -- Summoned Monster Magic Protection
(12189, 4068, 4), -- Mechanical Cannon
-- Mechanic Golem
(12190, 4291, 1), -- Race
(12190, 4121, 1), -- Summoned Monster Magic Protection
(12190, 4068, 5), -- Mechanical Cannon
-- Mechanic Golem
(12191, 4291, 1), -- Race
(12191, 4121, 1), -- Summoned Monster Magic Protection
(12191, 4068, 5), -- Mechanical Cannon
-- Reanimated Man
(12192, 4290, 1), -- Race
(12192, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(12193, 4290, 1), -- Race
(12193, 4121, 1), -- Summoned Monster Magic Protection
-- Corrupted Man
(12194, 4290, 1), -- Race
(12194, 4121, 1), -- Summoned Monster Magic Protection
(12194, 4260, 4), -- Steel Blood
-- Corrupted Man
(12195, 4290, 1), -- Race
(12195, 4121, 1), -- Summoned Monster Magic Protection
(12195, 4260, 5), -- Steel Blood
-- Corrupted Man
(12196, 4290, 1), -- Race
(12196, 4121, 1), -- Summoned Monster Magic Protection
(12196, 4260, 5), -- Steel Blood
-- Life Cubic
(12197, 4297, 1), -- Race
(12197, 4045, 1), -- Resist Full Magic Attack
-- Life Cubic
(12198, 4297, 1), -- Race
(12198, 4045, 1), -- Resist Full Magic Attack
-- Life Cubic
(12199, 4297, 1), -- Race
(12199, 4045, 1), -- Resist Full Magic Attack
-- Storm Cubic
(12200, 4297, 1), -- Race
(12200, 4045, 1), -- Resist Full Magic Attack
-- Storm Cubic
(12201, 4297, 1), -- Race
(12201, 4045, 1), -- Resist Full Magic Attack
-- Storm Cubic
(12202, 4297, 1), -- Race
(12202, 4045, 1), -- Resist Full Magic Attack
-- Vampiric Cubic
(12203, 4297, 1), -- Race
(12203, 4045, 1), -- Resist Full Magic Attack
-- Vampiric Cubic
(12204, 4297, 1), -- Race
(12204, 4045, 1), -- Resist Full Magic Attack
-- Vampiric Cubic
(12205, 4297, 1), -- Race
(12205, 4045, 1), -- Resist Full Magic Attack
-- Phantom Cubic
(12206, 4297, 1), -- Race
(12206, 4045, 1), -- Resist Full Magic Attack
-- Phantom Cubic
(12207, 4297, 1), -- Race
(12207, 4045, 1), -- Resist Full Magic Attack
-- Phantom Cubic
(12208, 4297, 1), -- Race
(12208, 4045, 1), -- Resist Full Magic Attack
-- Viper Cubic
(12209, 4297, 1), -- Race
(12209, 4045, 1), -- Resist Full Magic Attack
-- Viper Cubic
(12210, 4297, 1), -- Race
(12210, 4045, 1), -- Resist Full Magic Attack
-- Antharas
(12211, 4299, 1), -- Race
(12211, 4045, 1), -- Resist Full Magic Attack
(12211, 4122, 1), -- Antharas
(12211, 4106, 1),
(12211, 4107, 1),
(12211, 4108, 1),
(12211, 4109, 1),
(12211, 4110, 1),
(12211, 4111, 1),
(12211, 4112, 1),
(12211, 4113, 1),
-- Balthus Vanik
(12212, 4290, 1), -- Race
(12212, 4045, 1), -- Resist Full Magic Attack
-- Quant
(12213, 4290, 1), -- Race
(12213, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12214, 4290, 1), -- Race
(12214, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12215, 4290, 1), -- Race
(12215, 4045, 1), -- Resist Full Magic Attack
(12215, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12216, 4290, 1), -- Race
(12216, 4045, 1), -- Resist Full Magic Attack
(12216, 4027, 1), -- Gludio Heal
-- Court Guard
(12217, 4290, 1), -- Race
(12217, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12218, 4290, 1), -- Race
(12218, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12219, 4290, 1), -- Race
(12219, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12220, 4290, 1), -- Race
(12220, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12221, 4290, 1), -- Race
(12221, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12222, 4290, 1), -- Race
(12222, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12223, 4290, 1), -- Race
(12223, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12224, 4290, 1), -- Race
(12224, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12225, 4290, 1), -- Race
(12225, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12226, 4290, 1), -- Race
(12226, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12227, 4290, 1), -- Race
(12227, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12228, 4290, 1), -- Race
(12228, 4045, 1), -- Resist Full Magic Attack
(12228, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12229, 4290, 1), -- Race
(12229, 4045, 1), -- Resist Full Magic Attack
(12229, 4027, 1), -- Gludio Heal
-- Court Guard
(12230, 4290, 1), -- Race
(12230, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12231, 4290, 1), -- Race
(12231, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12232, 4290, 1), -- Race
(12232, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12233, 4290, 1), -- Race
(12233, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12234, 4290, 1), -- Race
(12234, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12235, 4290, 1), -- Race
(12235, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12236, 4290, 1), -- Race
(12236, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12237, 4290, 1), -- Race
(12237, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12238, 4290, 1), -- Race
(12238, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12239, 4290, 1), -- Race
(12239, 4045, 1), -- Resist Full Magic Attack
-- Brasseur
(12240, 4290, 1), -- Race
(12240, 4045, 1), -- Resist Full Magic Attack
-- Sherwood
(12241, 4290, 1), -- Race
(12241, 4045, 1), -- Resist Full Magic Attack
-- Oren Holy Artifact
(12242, 4290, 1), -- Race
(12242, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12243, 4290, 1), -- Race
(12243, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12244, 4290, 1), -- Race
(12244, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12245, 4290, 1), -- Race
(12245, 4045, 1), -- Resist Full Magic Attack
-- Mass Gatekeeper
(12246, 4290, 1), -- Race
(12246, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(12247, 4290, 1), -- Race
(12247, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12248, 4290, 1), -- Race
(12248, 4045, 1), -- Resist Full Magic Attack
-- Royal Gatekeeper
(12249, 4290, 1), -- Race
(12249, 4045, 1), -- Resist Full Magic Attack
-- Heart of Warding
(12250, 4290, 1), -- Race
(12250, 4045, 1), -- Resist Full Magic Attack
-- Siege Golem
(12251, 4290, 1), -- Race
(12251, 4332, 1), -- Mental Aegis
(12251, 4079, 1), -- Siege Hammer
-- Sayres 
(12252, 4290, 1), -- Race
(12252, 4045, 1), -- Resist Full Magic Attack
-- Tyron
(12253, 4290, 1), -- Race
(12253, 4045, 1), -- Resist Full Magic Attack
-- Amadeo Cadmus
(12254, 4290, 1), -- Race
(12254, 4045, 1), -- Resist Full Magic Attack
-- Logan
(12255, 4290, 1), -- Race
(12255, 4045, 1), -- Resist Full Magic Attack
-- Radcliff
(12256, 4290, 1), -- Race
(12256, 4045, 1), -- Resist Full Magic Attack
-- Shamus
(12257, 4290, 1), -- Race
(12257, 4045, 1), -- Resist Full Magic Attack
-- Morpheus
(12258, 4290, 1), -- Race
(12258, 4045, 1), -- Resist Full Magic Attack
-- Messenger Ruford
(12259, 4290, 1), -- Race
(12259, 4045, 1), -- Resist Full Magic Attack
-- Event Manager
(12260, 4290, 1), -- Race
(12260, 4045, 1), -- Resist Full Magic Attack
-- Event Manager
(12261, 4290, 1), -- Race
(12261, 4045, 1), -- Resist Full Magic Attack
-- Event Manager
(12262, 4290, 1), -- Race
(12262, 4045, 1), -- Resist Full Magic Attack
-- Event Manager
(12263, 4290, 1), -- Race
(12263, 4045, 1), -- Resist Full Magic Attack
-- Event Manager
(12264, 4290, 1), -- Race
(12264, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12265, 4290, 1), -- Race
(12265, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12266, 4290, 1), -- Race
(12266, 4045, 1), -- Resist Full Magic Attack
(12266, 4114, 1), -- Aden Flame
-- Court Sorcerer
(12267, 4290, 1), -- Race
(12267, 4045, 1), -- Resist Full Magic Attack
(12267, 4115, 1), -- Aden Heal
-- Court Guard
(12268, 4290, 1), -- Race
(12268, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12269, 4290, 1), -- Race
(12269, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12270, 4290, 1), -- Race
(12270, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12271, 4290, 1), -- Race
(12271, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12272, 4290, 1), -- Race
(12272, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12273, 4290, 1), -- Race
(12273, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12274, 4290, 1), -- Race
(12274, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12275, 4290, 1), -- Race
(12275, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12276, 4290, 1), -- Race
(12276, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12277, 4290, 1), -- Race
(12277, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12278, 4290, 1), -- Race
(12278, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12279, 4290, 1), -- Race
(12279, 4045, 1), -- Resist Full Magic Attack
(12279, 4114, 1), -- Aden Flame
-- Court Sorcerer
(12280, 4290, 1), -- Race
(12280, 4045, 1), -- Resist Full Magic Attack
(12280, 4115, 1), -- Aden Heal
-- Court Guard
(12281, 4290, 1), -- Race
(12281, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12282, 4290, 1), -- Race
(12282, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12283, 4290, 1), -- Race
(12283, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12284, 4290, 1), -- Race
(12284, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12285, 4290, 1), -- Race
(12285, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12286, 4290, 1), -- Race
(12286, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12287, 4290, 1), -- Race
(12287, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12288, 4290, 1), -- Race
(12288, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12289, 4290, 1), -- Race
(12289, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12290, 4290, 1), -- Race
(12290, 4045, 1), -- Resist Full Magic Attack
-- Aden Holy Artifact
(12291, 4290, 1), -- Race
(12291, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12292, 4290, 1), -- Race
(12292, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12293, 4290, 1), -- Race
(12293, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12294, 4290, 1), -- Race
(12294, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12295, 4290, 1), -- Race
(12295, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12296, 4290, 1), -- Race
(12296, 4045, 1), -- Resist Full Magic Attack
-- Mass Gatekeeper
(12297, 4290, 1), -- Race
(12297, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(12298, 4290, 1), -- Race
(12298, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12299, 4290, 1), -- Race
(12299, 4045, 1), -- Resist Full Magic Attack
-- Royal Gatekeeper
(12300, 4290, 1), -- Race
(12300, 4045, 1), -- Resist Full Magic Attack
-- Mercenary
(12301, 4290, 1), -- Race
(12301, 4045, 1), -- Resist Full Magic Attack
-- Mercenary
(12302, 4290, 1), -- Race
(12302, 4045, 1), -- Resist Full Magic Attack
-- Mercenary
(12303, 4290, 1), -- Race
(12303, 4045, 1), -- Resist Full Magic Attack
-- Mercenary
(12304, 4290, 1), -- Race
(12304, 4045, 1), -- Resist Full Magic Attack
(12304, 4027, 1), -- Gludio Heal
-- Mercenary
(12305, 4290, 1), -- Race
(12305, 4045, 1), -- Resist Full Magic Attack
(12305, 4026, 1), -- Gludio Flame
-- Mercenary
(12306, 4290, 1), -- Race
(12306, 4045, 1), -- Resist Full Magic Attack
-- Mercenary
(12307, 4290, 1), -- Race
(12307, 4045, 1), -- Resist Full Magic Attack
-- Mercenary
(12308, 4290, 1), -- Race
(12308, 4045, 1), -- Resist Full Magic Attack
-- Mercenary
(12309, 4290, 1), -- Race
(12309, 4045, 1), -- Resist Full Magic Attack
(12309, 4027, 1), -- Gludio Heal
-- Mercenary
(12310, 4290, 1), -- Race
(12310, 4045, 1), -- Resist Full Magic Attack
(12310, 4026, 1), -- Gludio Flame
-- Hatchling of the Wind
(12311, 4299, 1), -- Race
(12311, 4121, 1), -- Summoned Monster Magic Protection
(12311, 4710, 1), -- Wild Stun
(12311, 4711, 1), -- Wild Defense
-- Hatchling of the Stars
(12312, 4299, 1), -- Race
(12312, 4121, 1), -- Summoned Monster Magic Protection
(12312, 4712, 1), -- Bright Burst
(12312, 4713, 1), -- Bright Heal
-- Hatchling of Twilight
(12313, 4299, 1), -- Race
(12313, 4121, 1), -- Summoned Monster Magic Protection
-- Life Control Tower
(12314, 4290, 1), -- Race
(12314, 4045, 1), -- Resist Full Magic Attack
-- Life Control Tower
(12315, 4290, 1), -- Race
(12315, 4045, 1), -- Resist Full Magic Attack
-- Greenspan
(12316, 4290, 1), -- Race
(12316, 4045, 1), -- Resist Full Magic Attack
-- Mercenary Manager Sanford
(12317, 4290, 1), -- Race
(12317, 4045, 1), -- Resist Full Magic Attack
-- Morrison
(12318, 4290, 1), -- Race
(12318, 4045, 1), -- Resist Full Magic Attack
-- Arvid
(12319, 4290, 1), -- Race
(12319, 4045, 1), -- Resist Full Magic Attack
-- Eldon
(12320, 4290, 1), -- Race
(12320, 4045, 1), -- Resist Full Magic Attack
-- Rodd
(12321, 4290, 1), -- Race
(12321, 4045, 1), -- Resist Full Magic Attack
-- Hall Doorman
(12322, 4290, 1), -- Race
(12322, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12323, 4290, 1), -- Race
(12323, 4045, 1), -- Resist Full Magic Attack
-- Teleport Cube
(12324, 4290, 1), -- Race
(12324, 4275, 3), -- Sacred Attack Weak Point
(12324, 4278, 1), -- Dark Attack
(12324, 4045, 1), -- Resist Full Magic Attack
(12324, 4390, 1), -- NPC Abnormal Immunity
-- Gatekeeper
(12325, 4290, 1), -- Race
(12325, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12326, 4290, 1), -- Race
(12326, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12327, 4290, 1), -- Race
(12327, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12328, 4290, 1), -- Race
(12328, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12329, 4290, 1), -- Race
(12329, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12330, 4290, 1), -- Race
(12330, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12331, 4290, 1), -- Race
(12331, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12332, 4290, 1), -- Race
(12332, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12333, 4290, 1), -- Race
(12333, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12334, 4290, 1), -- Race
(12334, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12335, 4290, 1), -- Race
(12335, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12336, 4290, 1), -- Race
(12336, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12337, 4290, 1), -- Race
(12337, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12338, 4290, 1), -- Race
(12338, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12339, 4290, 1), -- Race
(12339, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12340, 4290, 1), -- Race
(12340, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12341, 4290, 1), -- Race
(12341, 4045, 1), -- Resist Full Magic Attack
-- Flame Control Tower
(12342, 4290, 1), -- Race
(12342, 4045, 1), -- Resist Full Magic Attack
-- Flame Control Tower
(12343, 4290, 1), -- Race
(12343, 4045, 1), -- Resist Full Magic Attack
-- Kat the Cat
(12344, 4293, 1), -- Race
(12344, 4121, 1), -- Summoned Monster Magic Protection
(12344, 4025, 2), -- Master Recharge
-- Kat the Cat
(12345, 4293, 1), -- Race
(12345, 4121, 1), -- Summoned Monster Magic Protection
(12345, 4025, 3), -- Master Recharge
-- Kat the Cat
(12346, 4293, 1), -- Race
(12346, 4121, 1), -- Summoned Monster Magic Protection
(12346, 4025, 4), -- Master Recharge
-- Kat the Cat
(12347, 4293, 1), -- Race
(12347, 4121, 1), -- Summoned Monster Magic Protection
(12347, 4025, 5), -- Master Recharge
-- Mew the Cat
(12348, 4293, 1), -- Race
(12348, 4121, 1), -- Summoned Monster Magic Protection
(12348, 4261, 2), -- Mega Storm Strike
-- Mew the Cat
(12349, 4293, 1), -- Race
(12349, 4121, 1), -- Summoned Monster Magic Protection
(12349, 4261, 3), -- Mega Storm Strike
-- Mew the Cat
(12350, 4293, 1), -- Race
(12350, 4121, 1), -- Summoned Monster Magic Protection
(12350, 4261, 4), -- Mega Storm Strike
-- Mew the Cat
(12351, 4293, 1), -- Race
(12351, 4121, 1), -- Summoned Monster Magic Protection
(12351, 4261, 5), -- Mega Storm Strike
-- Mew the Cat
(12352, 4293, 1), -- Race
(12352, 4121, 1), -- Summoned Monster Magic Protection
(12352, 4261, 5), -- Mega Storm Strike
-- Unicorn Boxer
(12353, 4296, 1), -- Race
(12353, 4121, 1), -- Summoned Monster Magic Protection
(12353, 4025, 2), -- Master Recharge
-- Unicorn Boxer
(12354, 4296, 1), -- Race
(12354, 4121, 1), -- Summoned Monster Magic Protection
(12354, 4025, 3), -- Master Recharge
-- Unicorn Boxer
(12355, 4296, 1), -- Race
(12355, 4121, 1), -- Summoned Monster Magic Protection
(12355, 4025, 4), -- Master Recharge
-- Unicorn Boxer
(12356, 4296, 1), -- Race
(12356, 4121, 1), -- Summoned Monster Magic Protection
(12356, 4025, 5), -- Master Recharge
-- Unicorn Mirage
(12357, 4296, 1), -- Race
(12357, 4121, 1), -- Summoned Monster Magic Protection
(12357, 4261, 2), -- Mega Storm Strike
-- Unicorn Mirage
(12358, 4296, 1), -- Race
(12358, 4121, 1), -- Summoned Monster Magic Protection
(12358, 4261, 3), -- Mega Storm Strike
-- Unicorn Mirage
(12359, 4296, 1), -- Race
(12359, 4121, 1), -- Summoned Monster Magic Protection
(12359, 4261, 4), -- Mega Storm Strike
-- Unicorn Mirage
(12360, 4296, 1), -- Race
(12360, 4121, 1), -- Summoned Monster Magic Protection
(12360, 4261, 5), -- Mega Storm Strike
-- Unicorn Mirage
(12361, 4296, 1), -- Race
(12361, 4121, 1), -- Summoned Monster Magic Protection
(12361, 4261, 5), -- Mega Storm Strike
-- Shadow
(12362, 4298, 1), -- Race
(12362, 4121, 1), -- Summoned Monster Magic Protection
(12362, 4233, 1), -- Vampiric Attack
-- Shadow
(12363, 4298, 1), -- Race
(12363, 4121, 1), -- Summoned Monster Magic Protection
(12363, 4233, 1), -- Vampiric Attack
-- Shadow
(12364, 4298, 1), -- Race
(12364, 4121, 1), -- Summoned Monster Magic Protection
(12364, 4233, 1), -- Vampiric Attack
-- Shadow
(12365, 4298, 1), -- Race
(12365, 4121, 1), -- Summoned Monster Magic Protection
(12365, 4233, 1), -- Vampiric Attack
-- Silhouette
(12366, 4298, 1), -- Race
(12366, 4121, 1), -- Summoned Monster Magic Protection
(12366, 4260, 2), -- Steel Blood
-- Silhouette
(12367, 4298, 1), -- Race
(12367, 4121, 1), -- Summoned Monster Magic Protection
(12367, 4260, 3), -- Steel Blood
-- Silhouette
(12368, 4298, 1), -- Race
(12368, 4121, 1), -- Summoned Monster Magic Protection
(12368, 4260, 4), -- Steel Blood
-- Silhouette
(12369, 4298, 1), -- Race
(12369, 4121, 1), -- Summoned Monster Magic Protection
(12369, 4260, 5), -- Steel Blood
-- Silhouette
(12370, 4298, 1), -- Race
(12370, 4121, 1), -- Summoned Monster Magic Protection
(12370, 4260, 5), -- Steel Blood
-- Puss the Cat
(12371, 4293, 1), -- Race
(12371, 4045, 1), -- Resist Full Magic Attack
-- Baium
(12372, 4291, 1), -- Race
(12372, 4045, 1), -- Resist Full Magic Attack
(12372, 4256, 1), -- 100% stun resistance.
-- Archangel
(12373, 4297, 1), -- Race
(12373, 4045, 1), -- Resist Full Magic Attack
-- Zaken
(12374, 4223, 1), -- Face Chance: Night to Day
(12374, 4224, 1), -- Face Chance: Day to Night
(12374, 4290, 1), -- Race
(12374, 4045, 1), -- Resist Full Magic Attack
-- Wild Hog Cannon
(12375, 4290, 1), -- Race
(12375, 4332, 1), -- Mental Aegis
(12375, 4230, 1), -- Wild Cannon
-- Doll Blader
(12376, 4298, 1), -- Race
(12376, 4278, 1), -- Dark Attack
(12376, 4333, 3), -- Resist Dark Attack
(12376, 4071, 3), -- Resist Archery
(12376, 4085, 1), -- Critical Power
(12376, 4086, 1), -- Critical Chance
(12376, 4045, 1), -- Resist Full Magic Attack
-- Vale master
(12377, 4298, 1), -- Race
(12377, 4278, 1), -- Dark Attack
(12377, 4333, 3), -- Resist Dark Attack
(12377, 4071, 3), -- Resist Archery
(12377, 4085, 1), -- Critical Power
(12377, 4086, 1), -- Critical Chance
(12377, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12378, 4291, 1), -- Race
(12378, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12379, 4291, 1), -- Race
(12379, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12380, 4291, 1), -- Race
(12380, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12381, 4291, 1), -- Race
(12381, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12382, 4291, 1), -- Race
(12382, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12383, 4291, 1), -- Race
(12383, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12384, 4291, 1), -- Race
(12384, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12385, 4291, 1), -- Race
(12385, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12386, 4291, 1), -- Race
(12386, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12387, 4291, 1), -- Race
(12387, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12388, 4291, 1), -- Race
(12388, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12389, 4291, 1), -- Race
(12389, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12390, 4291, 1), -- Race
(12390, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12391, 4291, 1), -- Race
(12391, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12392, 4291, 1), -- Race
(12392, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12393, 4291, 1), -- Race
(12393, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12394, 4291, 1), -- Race
(12394, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12395, 4291, 1), -- Race
(12395, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12396, 4291, 1), -- Race
(12396, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12397, 4291, 1), -- Race
(12397, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12398, 4291, 1), -- Race
(12398, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12399, 4291, 1), -- Race
(12399, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12400, 4291, 1), -- Race
(12400, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12401, 4291, 1), -- Race
(12401, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12402, 4291, 1), -- Race
(12402, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12403, 4291, 1), -- Race
(12403, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12404, 4291, 1), -- Race
(12404, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12405, 4291, 1), -- Race
(12405, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12406, 4291, 1), -- Race
(12406, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12407, 4291, 1), -- Race
(12407, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12408, 4291, 1), -- Race
(12408, 4045, 1), -- Resist Full Magic Attack
-- Ancient Treasure Chest
(12409, 4291, 1), -- Race
(12409, 4045, 1), -- Resist Full Magic Attack
-- Mimic
(12410, 4291, 1), -- Race
-- Kat the Cat
(12411, 4293, 1), -- Race
(12411, 4121, 1), -- Summoned Monster Magic Protection
(12411, 4025, 6), -- Master Recharge
-- Kat the Cat
(12412, 4293, 1), -- Race
(12412, 4121, 1), -- Summoned Monster Magic Protection
(12412, 4025, 6), -- Master Recharge
-- Kat the Cat
(12413, 4293, 1), -- Race
(12413, 4121, 1), -- Summoned Monster Magic Protection
(12413, 4025, 6), -- Master Recharge
-- Kat the Cat
(12414, 4293, 1), -- Race
(12414, 4121, 1), -- Summoned Monster Magic Protection
(12414, 4025, 6), -- Master Recharge
-- Kat the Cat
(12415, 4293, 1), -- Race
(12415, 4121, 1), -- Summoned Monster Magic Protection
(12415, 4025, 6), -- Master Recharge
-- Kat the Cat
(12416, 4293, 1), -- Race
(12416, 4121, 1), -- Summoned Monster Magic Protection
(12416, 4025, 7), -- Master Recharge
-- Kat the Cat
(12417, 4293, 1), -- Race
(12417, 4121, 1), -- Summoned Monster Magic Protection
(12417, 4025, 7), -- Master Recharge
-- Kat the Cat
(12418, 4293, 1), -- Race
(12418, 4121, 1), -- Summoned Monster Magic Protection
(12418, 4025, 7), -- Master Recharge
-- Kat the Cat
(12419, 4293, 1), -- Race
(12419, 4121, 1), -- Summoned Monster Magic Protection
(12419, 4025, 7), -- Master Recharge
-- Mew the Cat
(12420, 4293, 1), -- Race
(12420, 4121, 1), -- Summoned Monster Magic Protection
(12420, 4261, 6), -- Mega Storm Strike
-- Mew the Cat
(12421, 4293, 1), -- Race
(12421, 4121, 1), -- Summoned Monster Magic Protection
(12421, 4261, 6), -- Mega Storm Strike
-- Mew the Cat
(12422, 4293, 1), -- Race
(12422, 4121, 1), -- Summoned Monster Magic Protection
(12422, 4261, 6), -- Mega Storm Strike
-- Mew the Cat
(12423, 4293, 1), -- Race
(12423, 4121, 1), -- Summoned Monster Magic Protection
(12423, 4261, 6), -- Mega Storm Strike
-- Mew the Cat
(12424, 4293, 1), -- Race
(12424, 4121, 1), -- Summoned Monster Magic Protection
(12424, 4261, 6), -- Mega Storm Strike
-- Mew the Cat
(12425, 4293, 1), -- Race
(12425, 4121, 1), -- Summoned Monster Magic Protection
(12425, 4261, 7), -- Mega Storm Strike
-- Mew the Cat
(12426, 4293, 1), -- Race
(12426, 4121, 1), -- Summoned Monster Magic Protection
(12426, 4261, 7), -- Mega Storm Strike
-- Mew the Cat
(12427, 4293, 1), -- Race
(12427, 4121, 1), -- Summoned Monster Magic Protection
(12427, 4261, 7), -- Mega Storm Strike
-- Mew the Cat
(12428, 4293, 1), -- Race
(12428, 4121, 1), -- Summoned Monster Magic Protection
(12428, 4261, 7), -- Mega Storm Strike
-- Unicorn Mirage
(12429, 4296, 1), -- Race
(12429, 4121, 1), -- Summoned Monster Magic Protection
(12429, 4261, 6), -- Mega Storm Strike
-- Unicorn Mirage
(12430, 4296, 1), -- Race
(12430, 4121, 1), -- Summoned Monster Magic Protection
(12430, 4261, 6), -- Mega Storm Strike
-- Unicorn Mirage
(12431, 4296, 1), -- Race
(12431, 4121, 1), -- Summoned Monster Magic Protection
(12431, 4261, 6), -- Mega Storm Strike
-- Unicorn Mirage
(12432, 4296, 1), -- Race
(12432, 4121, 1), -- Summoned Monster Magic Protection
(12432, 4261, 6), -- Mega Storm Strike
-- Unicorn Mirage
(12433, 4296, 1), -- Race
(12433, 4121, 1), -- Summoned Monster Magic Protection
(12433, 4261, 6), -- Mega Storm Strike
-- Unicorn Mirage
(12434, 4296, 1), -- Race
(12434, 4121, 1), -- Summoned Monster Magic Protection
(12434, 4261, 7), -- Mega Storm Strike
-- Unicorn Mirage
(12435, 4296, 1), -- Race
(12435, 4121, 1), -- Summoned Monster Magic Protection
(12435, 4261, 7), -- Mega Storm Strike
-- Unicorn Mirage
(12436, 4296, 1), -- Race
(12436, 4121, 1), -- Summoned Monster Magic Protection
(12436, 4261, 7), -- Mega Storm Strike
-- Unicorn Mirage
(12437, 4296, 1), -- Race
(12437, 4121, 1), -- Summoned Monster Magic Protection
(12437, 4261, 7), -- Mega Storm Strike
-- Unicorn Boxer
(12438, 4296, 1), -- Race
(12438, 4121, 1), -- Summoned Monster Magic Protection
(12438, 4025, 6), -- Master Recharge
-- Unicorn Boxer
(12439, 4296, 1), -- Race
(12439, 4121, 1), -- Summoned Monster Magic Protection
(12439, 4025, 6), -- Master Recharge
-- Unicorn Boxer
(12440, 4296, 1), -- Race
(12440, 4121, 1), -- Summoned Monster Magic Protection
(12440, 4025, 6), -- Master Recharge
-- Unicorn Boxer
(12441, 4296, 1), -- Race
(12441, 4121, 1), -- Summoned Monster Magic Protection
(12441, 4025, 6), -- Master Recharge
-- Unicorn Boxer
(12442, 4296, 1), -- Race
(12442, 4121, 1), -- Summoned Monster Magic Protection
(12442, 4025, 6), -- Master Recharge
-- Unicorn Boxer
(12443, 4296, 1), -- Race
(12443, 4121, 1), -- Summoned Monster Magic Protection
(12443, 4025, 7), -- Master Recharge
-- Unicorn Boxer
(12444, 4296, 1), -- Race
(12444, 4121, 1), -- Summoned Monster Magic Protection
(12444, 4025, 7), -- Master Recharge
-- Unicorn Boxer
(12445, 4296, 1), -- Race
(12445, 4121, 1), -- Summoned Monster Magic Protection
(12445, 4025, 7), -- Master Recharge
-- Unicorn Boxer
(12446, 4296, 1), -- Race
(12446, 4121, 1), -- Summoned Monster Magic Protection
(12446, 4025, 7), -- Master Recharge
-- Shadow
(12447, 4298, 1), -- Race
(12447, 4121, 1), -- Summoned Monster Magic Protection
(12447, 4233, 1), -- Vampiric Attack
-- Shadow
(12448, 4298, 1), -- Race
(12448, 4121, 1), -- Summoned Monster Magic Protection
(12448, 4233, 1), -- Vampiric Attack
-- Shadow
(12449, 4298, 1), -- Race
(12449, 4121, 1), -- Summoned Monster Magic Protection
(12449, 4233, 1), -- Vampiric Attack
-- Shadow
(12450, 4298, 1), -- Race
(12450, 4121, 1), -- Summoned Monster Magic Protection
(12450, 4233, 1), -- Vampiric Attack
-- Shadow
(12451, 4298, 1), -- Race
(12451, 4121, 1), -- Summoned Monster Magic Protection
(12451, 4233, 1), -- Vampiric Attack
-- Shadow
(12452, 4298, 1), -- Race
(12452, 4121, 1), -- Summoned Monster Magic Protection
(12452, 4233, 1), -- Vampiric Attack
-- Shadow
(12453, 4298, 1), -- Race
(12453, 4121, 1), -- Summoned Monster Magic Protection
(12453, 4233, 1), -- Vampiric Attack
-- Shadow
(12454, 4298, 1), -- Race
(12454, 4121, 1), -- Summoned Monster Magic Protection
(12454, 4233, 1), -- Vampiric Attack
-- Shadow
(12455, 4298, 1), -- Race
(12455, 4121, 1), -- Summoned Monster Magic Protection
(12455, 4233, 1), -- Vampiric Attack
-- Silhouette
(12456, 4298, 1), -- Race
(12456, 4121, 1), -- Summoned Monster Magic Protection
(12456, 4260, 6), -- Steel Blood
-- Silhouette
(12457, 4298, 1), -- Race
(12457, 4121, 1), -- Summoned Monster Magic Protection
(12457, 4260, 6), -- Steel Blood
-- Silhouette
(12458, 4298, 1), -- Race
(12458, 4121, 1), -- Summoned Monster Magic Protection
(12458, 4260, 6), -- Steel Blood
-- Silhouette
(12459, 4298, 1), -- Race
(12459, 4121, 1), -- Summoned Monster Magic Protection
(12459, 4260, 6), -- Steel Blood
-- Silhouette
(12460, 4298, 1), -- Race
(12460, 4121, 1), -- Summoned Monster Magic Protection
(12460, 4260, 6), -- Steel Blood
-- Silhouette
(12461, 4298, 1), -- Race
(12461, 4121, 1), -- Summoned Monster Magic Protection
(12461, 4260, 7), -- Steel Blood
-- Silhouette
(12462, 4298, 1), -- Race
(12462, 4121, 1), -- Summoned Monster Magic Protection
(12462, 4260, 7), -- Steel Blood
-- Silhouette
(12463, 4298, 1), -- Race
(12463, 4121, 1), -- Summoned Monster Magic Protection
(12463, 4260, 7), -- Steel Blood
-- Silhouette
(12464, 4298, 1), -- Race
(12464, 4121, 1), -- Summoned Monster Magic Protection
(12464, 4260, 7), -- Steel Blood
-- Reanimated Man
(12465, 4290, 1), -- Race
(12465, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(12466, 4290, 1), -- Race
(12466, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(12467, 4290, 1), -- Race
(12467, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(12468, 4290, 1), -- Race
(12468, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(12469, 4290, 1), -- Race
(12469, 4121, 1), -- Summoned Monster Magic Protection
-- Corrupted Man
(12470, 4290, 1), -- Race
(12470, 4121, 1), -- Summoned Monster Magic Protection
(12470, 4260, 6), -- Steel Blood
-- Corrupted Man
(12471, 4290, 1), -- Race
(12471, 4121, 1), -- Summoned Monster Magic Protection
(12471, 4260, 6), -- Steel Blood
-- Corrupted Man
(12472, 4290, 1), -- Race
(12472, 4121, 1), -- Summoned Monster Magic Protection
(12472, 4260, 7), -- Steel Blood
-- Dark Panther
(12473, 4293, 1), -- Race
(12473, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(12474, 4293, 1), -- Race
(12474, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(12475, 4293, 1), -- Race
(12475, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(12476, 4293, 1), -- Race
(12476, 4121, 1), -- Summoned Monster Magic Protection
-- Kai the Cat
(12477, 4293, 1), -- Race
(12477, 4121, 1), -- Summoned Monster Magic Protection
(12477, 4378, 4), -- Self Damage Shield
-- Kai the Cat
(12478, 4293, 1), -- Race
(12478, 4121, 1), -- Summoned Monster Magic Protection
(12478, 4378, 4), -- Self Damage Shield
-- Kai the Cat
(12479, 4293, 1), -- Race
(12479, 4121, 1), -- Summoned Monster Magic Protection
(12479, 4378, 5), -- Self Damage Shield
-- Kai the Cat
(12480, 4293, 1), -- Race
(12480, 4121, 1), -- Summoned Monster Magic Protection
(12480, 4378, 5), -- Self Damage Shield
-- Kai the Cat
(12481, 4293, 1), -- Race
(12481, 4121, 1), -- Summoned Monster Magic Protection
(12481, 4378, 5), -- Self Damage Shield
-- Kai the Cat
(12482, 4293, 1), -- Race
(12482, 4121, 1), -- Summoned Monster Magic Protection
(12482, 4378, 6), -- Self Damage Shield
-- Kai the Cat
(12483, 4293, 1), -- Race
(12483, 4121, 1), -- Summoned Monster Magic Protection
(12483, 4378, 6), -- Self Damage Shield
-- Kai the Cat
(12484, 4293, 1), -- Race
(12484, 4121, 1), -- Summoned Monster Magic Protection
(12484, 4378, 6), -- Self Damage Shield
-- Kai the Cat
(12485, 4293, 1), -- Race
(12485, 4121, 1), -- Summoned Monster Magic Protection
(12485, 4378, 6), -- Self Damage Shield
-- Kai the Cat
(12486, 4293, 1), -- Race
(12486, 4121, 1), -- Summoned Monster Magic Protection
(12486, 4378, 6), -- Self Damage Shield
-- Kai the Cat
(12487, 4293, 1), -- Race
(12487, 4121, 1), -- Summoned Monster Magic Protection
(12487, 4378, 7), -- Self Damage Shield
-- Kai the Cat
(12488, 4293, 1), -- Race
(12488, 4121, 1), -- Summoned Monster Magic Protection
(12488, 4378, 7), -- Self Damage Shield
-- Kai the Cat
(12489, 4293, 1), -- Race
(12489, 4121, 1), -- Summoned Monster Magic Protection
(12489, 4378, 7), -- Self Damage Shield
-- Unicorn Merrow
(12490, 4296, 1), -- Race
(12490, 4121, 1), -- Summoned Monster Magic Protection
(12490, 4137, 4), -- Hydro Screw
-- Unicorn Merrow
(12491, 4296, 1), -- Race
(12491, 4121, 1), -- Summoned Monster Magic Protection
(12491, 4137, 4), -- Hydro Screw
-- Unicorn Merrow
(12492, 4296, 1), -- Race
(12492, 4121, 1), -- Summoned Monster Magic Protection
(12492, 4137, 5), -- Hydro Screw
-- Unicorn Merrow
(12493, 4296, 1), -- Race
(12493, 4121, 1), -- Summoned Monster Magic Protection
(12493, 4137, 5), -- Hydro Screw
-- Unicorn Merrow
(12494, 4296, 1), -- Race
(12494, 4121, 1), -- Summoned Monster Magic Protection
(12494, 4137, 5), -- Hydro Screw
-- Unicorn Merrow
(12495, 4296, 1), -- Race
(12495, 4121, 1), -- Summoned Monster Magic Protection
(12495, 4137, 6), -- Hydro Screw
-- Unicorn Merrow
(12496, 4296, 1), -- Race
(12496, 4121, 1), -- Summoned Monster Magic Protection
(12496, 4137, 6), -- Hydro Screw
-- Unicorn Merrow
(12497, 4296, 1), -- Race
(12497, 4121, 1), -- Summoned Monster Magic Protection
(12497, 4137, 6), -- Hydro Screw
-- Unicorn Merrow
(12498, 4296, 1), -- Race
(12498, 4121, 1), -- Summoned Monster Magic Protection
(12498, 4137, 6), -- Hydro Screw
-- Unicorn Merrow
(12499, 4296, 1), -- Race
(12499, 4121, 1), -- Summoned Monster Magic Protection
(12499, 4137, 6), -- Hydro Screw
-- Unicorn Merrow
(12500, 4296, 1), -- Race
(12500, 4121, 1), -- Summoned Monster Magic Protection
(12500, 4137, 7), -- Hydro Screw
-- Unicorn Merrow
(12501, 4296, 1), -- Race
(12501, 4121, 1), -- Summoned Monster Magic Protection
(12501, 4137, 7), -- Hydro Screw
-- Unicorn Merrow
(12502, 4296, 1), -- Race
(12502, 4121, 1), -- Summoned Monster Magic Protection
(12502, 4137, 7), -- Hydro Screw
-- Soulless
(12503, 4298, 1), -- Race
(12503, 4121, 1), -- Summoned Monster Magic Protection
(12503, 4138, 4), -- NPC AE - Corpse Burst
(12503, 4259, 4), -- Toxic Smoke
(12503, 4140, 4), -- Contract Payment
-- Soulless
(12504, 4298, 1), -- Race
(12504, 4121, 1), -- Summoned Monster Magic Protection
(12504, 4138, 4), -- NPC AE - Corpse Burst
(12504, 4259, 4), -- Toxic Smoke
(12504, 4140, 4), -- Contract Payment
-- Soulless
(12505, 4298, 1), -- Race
(12505, 4121, 1), -- Summoned Monster Magic Protection
(12505, 4138, 5), -- NPC AE - Corpse Burst
(12505, 4259, 5), -- Toxic Smoke
(12505, 4140, 5), -- Contract Payment
-- Soulless
(12506, 4298, 1), -- Race
(12506, 4121, 1), -- Summoned Monster Magic Protection
(12506, 4138, 5), -- NPC AE - Corpse Burst
(12506, 4259, 5), -- Toxic Smoke
(12506, 4140, 5), -- Contract Payment
-- Soulless
(12507, 4298, 1), -- Race
(12507, 4121, 1), -- Summoned Monster Magic Protection
(12507, 4138, 5), -- NPC AE - Corpse Burst
(12507, 4259, 5), -- Toxic Smoke
(12507, 4140, 5), -- Contract Payment
-- Soulless
(12508, 4298, 1), -- Race
(12508, 4121, 1), -- Summoned Monster Magic Protection
(12508, 4138, 6), -- NPC AE - Corpse Burst
(12508, 4259, 6), -- Toxic Smoke
(12508, 4140, 6), -- Contract Payment
-- Soulless
(12509, 4298, 1), -- Race
(12509, 4121, 1), -- Summoned Monster Magic Protection
(12509, 4138, 6), -- NPC AE - Corpse Burst
(12509, 4259, 6), -- Toxic Smoke
(12509, 4140, 6), -- Contract Payment
-- Soulless
(12510, 4298, 1), -- Race
(12510, 4121, 1), -- Summoned Monster Magic Protection
(12510, 4138, 6), -- NPC AE - Corpse Burst
(12510, 4259, 6), -- Toxic Smoke
(12510, 4140, 6), -- Contract Payment
-- Soulless
(12511, 4298, 1), -- Race
(12511, 4121, 1), -- Summoned Monster Magic Protection
(12511, 4138, 6), -- NPC AE - Corpse Burst
(12511, 4259, 6), -- Toxic Smoke
(12511, 4140, 6), -- Contract Payment
-- Soulless
(12512, 4298, 1), -- Race
(12512, 4121, 1), -- Summoned Monster Magic Protection
(12512, 4138, 6), -- NPC AE - Corpse Burst
(12512, 4259, 6), -- Toxic Smoke
(12512, 4140, 6), -- Contract Payment
-- Soulless
(12513, 4298, 1), -- Race
(12513, 4121, 1), -- Summoned Monster Magic Protection
(12513, 4138, 7), -- NPC AE - Corpse Burst
(12513, 4259, 7), -- Toxic Smoke
(12513, 4140, 7), -- Contract Payment
-- Soulless
(12514, 4298, 1), -- Race
(12514, 4121, 1), -- Summoned Monster Magic Protection
(12514, 4138, 7), -- NPC AE - Corpse Burst
(12514, 4259, 7), -- Toxic Smoke
(12514, 4140, 7), -- Contract Payment
-- Soulless
(12515, 4298, 1), -- Race
(12515, 4121, 1), -- Summoned Monster Magic Protection
(12515, 4138, 7), -- NPC AE - Corpse Burst
(12515, 4259, 7), -- Toxic Smoke
(12515, 4140, 7), -- Contract Payment
-- __Big Boom
(12516, 4291, 1), -- Race
(12516, 4121, 1), -- Summoned Monster Magic Protection
-- Big Boom
(12517, 4291, 1), -- Race
(12517, 4121, 1), -- Summoned Monster Magic Protection
(12517, 4139, 6), -- Boom Attack
-- Big Boom
(12518, 4291, 1), -- Race
(12518, 4121, 1), -- Summoned Monster Magic Protection
(12518, 4139, 6), -- Boom Attack
-- Big Boom
(12519, 4291, 1), -- Race
(12519, 4121, 1), -- Summoned Monster Magic Protection
(12519, 4139, 6), -- Boom Attack
-- Big Boom
(12520, 4291, 1), -- Race
(12520, 4121, 1), -- Summoned Monster Magic Protection
(12520, 4139, 7), -- Boom Attack
-- Big Boom
(12521, 4291, 1), -- Race
(12521, 4121, 1), -- Summoned Monster Magic Protection
(12521, 4139, 7), -- Boom Attack
-- Mechanic Golem
(12522, 4291, 1), -- Race
(12522, 4121, 1), -- Summoned Monster Magic Protection
(12522, 4068, 6), -- Mechanical Cannon
-- Mechanic Golem
(12523, 4291, 1), -- Race
(12523, 4121, 1), -- Summoned Monster Magic Protection
(12523, 4068, 6), -- Mechanical Cannon
-- Mechanic Golem
(12524, 4291, 1), -- Race
(12524, 4121, 1), -- Summoned Monster Magic Protection
(12524, 4068, 7), -- Mechanical Cannon
-- Mechanic Golem
(12525, 4291, 1), -- Race
(12525, 4121, 1), -- Summoned Monster Magic Protection
(12525, 4068, 7), -- Mechanical Cannon
-- Wind Strider
(12526, 4299, 1), -- Race
(12526, 4121, 1), -- Summoned Monster Magic Protection
(12526, 4710, 1), -- Wild Stun
(12526, 4711, 1), -- Wild Defense
-- Star Strider
(12527, 4299, 1), -- Race
(12527, 4121, 1), -- Summoned Monster Magic Protection
(12527, 4712, 1), -- Bright Burst
(12527, 4713, 1), -- Bright Heal
-- Twilight Strider
(12528, 4299, 1), -- Race
(12528, 4121, 1), -- Summoned Monster Magic Protection
-- __Kai the Cat
(12529, 4293, 1), -- Race
(12529, 4121, 1), -- Summoned Monster Magic Protection
-- __Unicorn Merrow
(12530, 4296, 1), -- Race
(12530, 4121, 1), -- Summoned Monster Magic Protection
-- __Soulless
(12531, 4298, 1), -- Race
(12531, 4121, 1), -- Summoned Monster Magic Protection
-- Dietrich
(12532, 4290, 1), -- Race
(12532, 4275, 3), -- Sacred Attack Weak Point
(12532, 4278, 1), -- Dark Attack
(12532, 4071, 3), -- Resist Archery
(12532, 4095, 1), -- Damage Shield
(12532, 4085, 1), -- Critical Power
(12532, 4086, 1), -- Critical Chance
(12532, 4045, 1), -- Resist Full Magic Attack
-- Mikhail
(12533, 4290, 1), -- Race
(12533, 4275, 3), -- Sacred Attack Weak Point
(12533, 4278, 1), -- Dark Attack
(12533, 4071, 3), -- Resist Archery
(12533, 4095, 1), -- Damage Shield
(12533, 4085, 1), -- Critical Power
(12533, 4086, 1), -- Critical Chance
(12533, 4045, 1), -- Resist Full Magic Attack
-- Gustav
(12534, 4290, 1), -- Race
(12534, 4275, 3), -- Sacred Attack Weak Point
(12534, 4278, 1), -- Dark Attack
(12534, 4071, 3), -- Resist Archery
(12534, 4095, 1), -- Damage Shield
(12534, 4085, 1), -- Critical Power
(12534, 4086, 1), -- Critical Chance
(12534, 4045, 1), -- Resist Full Magic Attack
-- Baium
(12535, 4295, 1), -- Race
(12535, 4045, 1), -- Resist Full Magic Attack
-- Kai the Cat
(12536, 4293, 1), -- Race
(12536, 4121, 1), -- Summoned Monster Magic Protection
(12536, 4378, 7), -- Self Damage Shield
-- Unicorn Merrow
(12537, 4296, 1), -- Race
(12537, 4121, 1), -- Summoned Monster Magic Protection
(12537, 4137, 7), -- Hydro Screw
-- Soulless
(12538, 4298, 1), -- Race
(12538, 4121, 1), -- Summoned Monster Magic Protection
(12538, 4138, 7), -- NPC AE - Corpse Burst
(12538, 4259, 7), -- Toxic Smoke
(12538, 4140, 7), -- Contract Payment
-- Oel Mahum Berserker
(12539, 4295, 1), -- Race
(12539, 4226, 1), -- Ban Heal
-- Oel Mahum Scout
(12540, 4295, 1), -- Race
(12540, 4225, 1), -- Resist Shock
-- Oel Mahum Leader
(12541, 4295, 1), -- Race
-- Oel Mahum Cleric
(12542, 4295, 1), -- Race
-- Oel Mahum Thief
(12543, 4295, 1), -- Race
(12543, 4071, 3), -- Resist Archery
(12543, 4093, 1), -- Evasion
-- Pirate Zombie Captain
(12544, 4290, 1), -- Race
(12544, 4275, 3), -- Sacred Attack Weak Point
(12544, 4278, 1), -- Dark Attack
(12544, 4045, 1), -- Resist Full Magic Attack
-- Pirate Zombie
(12545, 4290, 1), -- Race
(12545, 4275, 3), -- Sacred Attack Weak Point
(12545, 4278, 1), -- Dark Attack
(12545, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12546, 4290, 1), -- Race
(12546, 4045, 1), -- Resist Full Magic Attack
-- Sentinel
(12547, 4290, 1), -- Race
(12547, 4045, 1), -- Resist Full Magic Attack
-- Sentry
(12548, 4290, 1), -- Race
(12548, 4045, 1), -- Resist Full Magic Attack
-- Defender
(12549, 4290, 1), -- Race
(12549, 4045, 1), -- Resist Full Magic Attack
-- Centurion
(12550, 4290, 1), -- Race
(12550, 4045, 1), -- Resist Full Magic Attack
-- Flag of Winner
(12551, 4290, 1), -- Race
(12551, 4045, 1), -- Resist Full Magic Attack
-- Red Flag
(12552, 4290, 1), -- Race
(12552, 4045, 1), -- Resist Full Magic Attack
-- Yellow Flag
(12553, 4290, 1), -- Race
(12553, 4045, 1), -- Resist Full Magic Attack
-- Green Flag
(12554, 4290, 1), -- Race
(12554, 4045, 1), -- Resist Full Magic Attack
-- Blue Flag
(12555, 4290, 1), -- Race
(12555, 4045, 1), -- Resist Full Magic Attack
-- Purple Flag
(12556, 4290, 1), -- Race
(12556, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12557, 4295, 1), -- Race
(12557, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12558, 4295, 1), -- Race
(12558, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(12559, 4295, 1), -- Race
(12559, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(12560, 4295, 1), -- Race
(12560, 4045, 1), -- Resist Full Magic Attack
-- Messenger
(12561, 4295, 1), -- Race
(12561, 4045, 1), -- Resist Full Magic Attack
-- Chamberlain
(12562, 4295, 1), -- Race
(12562, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12563, 4290, 1), -- Race
(12563, 4045, 1), -- Resist Full Magic Attack
-- Sin Eater
(12564, 4302, 1), -- Race
(12564, 4121, 1), -- Summoned Monster Magic Protection
-- Doom Servant
(12565, 4290, 1), -- Race
(12565, 4275, 3), -- Sacred Attack Weak Point
(12565, 4278, 1), -- Dark Attack
(12565, 4085, 1), -- Critical Power
(12565, 4045, 1), -- Resist Full Magic Attack
(12565, 4034, 6), -- Decrease Speed
-- Doom Guard
(12566, 4290, 1), -- Race
(12566, 4275, 3), -- Sacred Attack Weak Point
(12566, 4278, 1), -- Dark Attack
(12566, 4085, 1), -- Critical Power
(12566, 4045, 1), -- Resist Full Magic Attack
(12566, 4259, 6), -- Toxic Smoke
-- Doom Archer
(12567, 4290, 1), -- Race
(12567, 4275, 3), -- Sacred Attack Weak Point
(12567, 4278, 1), -- Dark Attack
(12567, 4085, 1), -- Critical Power
(12567, 4045, 1), -- Resist Full Magic Attack
(12567, 4074, 2), -- NPC Haste
-- Doom Trooper
(12568, 4290, 1), -- Race
(12568, 4275, 3), -- Sacred Attack Weak Point
(12568, 4278, 1), -- Dark Attack
(12568, 4045, 1), -- Resist Full Magic Attack
(12568, 4101, 6), -- NPC Spinning Slasher
-- Doom Warrior
(12569, 4290, 1), -- Race
(12569, 4275, 3), -- Sacred Attack Weak Point
(12569, 4278, 1), -- Dark Attack
(12569, 4071, 3), -- Resist Archery
(12569, 4085, 1), -- Critical Power
(12569, 4045, 1), -- Resist Full Magic Attack
(12569, 4030, 3), -- Enhance P. Atk.
-- Doom Knight
(12570, 4290, 1), -- Race
(12570, 4275, 3), -- Sacred Attack Weak Point
(12570, 4278, 1), -- Dark Attack
(12570, 4071, 3), -- Resist Archery
(12570, 4085, 1), -- Critical Power
(12570, 4045, 1), -- Resist Full Magic Attack
(12570, 4118, 7), -- Paralysis
-- Angelic Vortex
(12571, 4290, 1), -- Race
(12571, 4121, 1), -- Summoned Monster Magic Protection
(12571, 4390, 1), -- NPC Abnormal Immunity
-- Baron Lionel Hunter
(12572, 4290, 1), -- Race
(12572, 4045, 1), -- Resist Full Magic Attack
-- Schaht
(12573, 4290, 1), -- Race
(12573, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12574, 4290, 1), -- Race
(12574, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12575, 4290, 1), -- Race
(12575, 4045, 1), -- Resist Full Magic Attack
(12575, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12576, 4290, 1), -- Race
(12576, 4045, 1), -- Resist Full Magic Attack
(12576, 4027, 1), -- Gludio Heal
-- Court Guard
(12577, 4290, 1), -- Race
(12577, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12578, 4290, 1), -- Race
(12578, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12579, 4290, 1), -- Race
(12579, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12580, 4290, 1), -- Race
(12580, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12581, 4290, 1), -- Race
(12581, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12582, 4290, 1), -- Race
(12582, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12583, 4290, 1), -- Race
(12583, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12584, 4290, 1), -- Race
(12584, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12585, 4290, 1), -- Race
(12585, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12586, 4290, 1), -- Race
(12586, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12587, 4290, 1), -- Race
(12587, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12588, 4290, 1), -- Race
(12588, 4045, 1), -- Resist Full Magic Attack
(12588, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12589, 4290, 1), -- Race
(12589, 4045, 1), -- Resist Full Magic Attack
(12589, 4027, 1), -- Gludio Heal
-- Court Guard
(12590, 4290, 1), -- Race
(12590, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12591, 4290, 1), -- Race
(12591, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12592, 4290, 1), -- Race
(12592, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12593, 4290, 1), -- Race
(12593, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12594, 4290, 1), -- Race
(12594, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12595, 4290, 1), -- Race
(12595, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12596, 4290, 1), -- Race
(12596, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12597, 4290, 1), -- Race
(12597, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12598, 4290, 1), -- Race
(12598, 4045, 1), -- Resist Full Magic Attack
-- Guard
(12599, 4290, 1), -- Race
(12599, 4045, 1), -- Resist Full Magic Attack
-- Neurath
(12600, 4290, 1), -- Race
(12600, 4045, 1), -- Resist Full Magic Attack
-- Raybell
(12601, 4290, 1), -- Race
(12601, 4045, 1), -- Resist Full Magic Attack
-- Innadril Holy Artifact
(12602, 4290, 1), -- Race
(12602, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12603, 4290, 1), -- Race
(12603, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12604, 4290, 1), -- Race
(12604, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12605, 4290, 1), -- Race
(12605, 4045, 1), -- Resist Full Magic Attack
-- Mass Gatekeeper
(12606, 4290, 1), -- Race
(12606, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(12607, 4290, 1), -- Race
(12607, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12608, 4290, 1), -- Race
(12608, 4045, 1), -- Resist Full Magic Attack
-- Royal Gatekeeper
(12609, 4290, 1), -- Race
(12609, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12610, 4290, 1), -- Race
(12610, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12611, 4290, 1), -- Race
(12611, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12612, 4290, 1), -- Race
(12612, 4045, 1), -- Resist Full Magic Attack
-- Solinus
(12613, 4290, 1), -- Race
(12613, 4045, 1), -- Resist Full Magic Attack
-- Maruk Lord
(12614, 4290, 1), -- Race
(12614, 4275, 3), -- Sacred Attack Weak Point
(12614, 4278, 1), -- Dark Attack
(12614, 4002, 6), -- NPC HP Drain
(12614, 4094, 6), -- NPC Cancel Magic
(12614, 4098, 6), -- Magic Skill Block
(12614, 4046, 6), -- Sleep
-- Limal Karinness
(12615, 4298, 1), -- Race
(12615, 4278, 1), -- Dark Attack
(12615, 4333, 3), -- Resist Dark Attack
(12615, 4002, 6), -- NPC HP Drain
(12615, 4118, 6), -- Paralysis
(12615, 4047, 6), -- Hold
-- Karik
(12616, 4298, 1), -- Race
(12616, 4278, 1), -- Dark Attack
(12616, 4333, 3), -- Resist Dark Attack
(12616, 4085, 1), -- Critical Power
(12616, 4086, 1), -- Critical Chance
(12616, 4084, 4), -- Resist Physical Attack
(12616, 4071, 3), -- Resist Archery
(12616, 4047, 7), -- Hold
-- Santa Trainee
(12617, 4290, 1), -- Race
(12617, 4045, 1), -- Resist Full Magic Attack
-- Santa Trainee
(12618, 4290, 1), -- Race
(12618, 4045, 1), -- Resist Full Magic Attack
-- Christmas Tree
(12619, 4297, 1), -- Race
(12619, 4045, 1), -- Resist Full Magic Attack
-- Special Christmas Tree
(12620, 4297, 1), -- Race
(12620, 4045, 1), -- Resist Full Magic Attack
-- Wyvern
(12621, 4299, 1), -- Race
(12621, 4121, 1), -- Summoned Monster Magic Protection
(12621, 4289, 1), -- Wyvern Breath
-- Offering of Branded Elder
(12622, 4295, 1), -- Race
(12622, 4285, 4), -- Resist Sleep
(12622, 4287, 4), -- Resist Hold
(12622, 4273, 2), -- Resist Dagger
(12622, 4071, 4), -- Resist Archery
(12622, 4274, 1), -- Blunt Attack Weak Point
(12622, 4078, 3), -- NPC Flamestrike
-- Offering of the Branded
(12623, 4295, 1), -- Race
(12623, 4285, 4), -- Resist Sleep
(12623, 4287, 4), -- Resist Hold
(12623, 4071, 4), -- Resist Archery
(12623, 4379, 4), -- Resist Pole Arm
-- Offering of Branded Archer
(12624, 4295, 1), -- Race
(12624, 4285, 4), -- Resist Sleep
(12624, 4287, 4), -- Resist Hold
-- Offering of Branded Warrior
(12625, 4295, 1), -- Race
(12625, 4285, 4), -- Resist Sleep
(12625, 4287, 4), -- Resist Hold
(12625, 4273, 2), -- Resist Dagger
(12625, 4071, 3), -- Resist Archery
(12625, 4274, 1), -- Blunt Attack Weak Point
(12625, 4087, 3), -- NPC Blaze
-- Offering of Branded Follower
(12626, 4295, 1), -- Race
(12626, 4285, 4), -- Resist Sleep
(12626, 4287, 4), -- Resist Hold
(12626, 4379, 4), -- Resist Pole Arm
(12626, 4225, 3), -- Resist Shock
(12626, 4116, 6), -- Resist M. Atk.
(12626, 4151, 2), -- NPC Windstrike - Magic
(12626, 4160, 2), -- NPC Aura Burn - Magic
-- Offering of Branded Berserker
(12627, 4295, 1), -- Race
(12627, 4285, 4), -- Resist Sleep
(12627, 4287, 4), -- Resist Hold
(12627, 4273, 2), -- Resist Dagger
(12627, 4071, 3), -- Resist Archery
(12627, 4274, 1), -- Blunt Attack Weak Point
(12627, 4116, 6), -- Resist M. Atk.
(12627, 4244, 3), -- NPC Wild Sweep
-- Offering of Branded Zealot
(12628, 4295, 1), -- Race
(12628, 4285, 4), -- Resist Sleep
(12628, 4287, 4), -- Resist Hold
(12628, 4379, 4), -- Resist Pole Arm
(12628, 4225, 3), -- Resist Shock
(12628, 4032, 3), -- NPC Strike
-- Offering of Branded Marksman
(12629, 4295, 1), -- Race
(12629, 4285, 4), -- Resist Sleep
(12629, 4287, 4), -- Resist Hold
-- Offering of Branded Disciple
(12630, 4295, 1), -- Race
(12630, 4285, 4), -- Resist Sleep
(12630, 4287, 4), -- Resist Hold
(12630, 4032, 4), -- NPC Strike
-- Offering of Branded Saint
(12631, 4295, 1), -- Race
(12631, 4285, 4), -- Resist Sleep
(12631, 4287, 4), -- Resist Hold
(12631, 4273, 2), -- Resist Dagger
(12631, 4071, 3), -- Resist Archery
(12631, 4073, 4), -- Shock
-- Apostate's Offering Elder
(12632, 4295, 1), -- Race
(12632, 4285, 4), -- Resist Sleep
(12632, 4287, 4), -- Resist Hold
(12632, 4273, 2), -- Resist Dagger
(12632, 4071, 4), -- Resist Archery
(12632, 4274, 1), -- Blunt Attack Weak Point
(12632, 4078, 4), -- NPC Flamestrike
-- Apostate's Offering 
(12633, 4295, 1), -- Race
(12633, 4285, 4), -- Resist Sleep
(12633, 4287, 4), -- Resist Hold
(12633, 4071, 4), -- Resist Archery
(12633, 4379, 4), -- Resist Pole Arm
-- Apostate's Offering Archer
(12634, 4295, 1), -- Race
(12634, 4285, 4), -- Resist Sleep
(12634, 4287, 4), -- Resist Hold
-- Apostate's Offering Warrior
(12635, 4295, 1), -- Race
(12635, 4285, 4), -- Resist Sleep
(12635, 4287, 4), -- Resist Hold
(12635, 4273, 2), -- Resist Dagger
(12635, 4071, 3), -- Resist Archery
(12635, 4274, 1), -- Blunt Attack Weak Point
(12635, 4087, 4), -- NPC Blaze
-- Apostate's Offering Follower
(12636, 4295, 1), -- Race
(12636, 4285, 4), -- Resist Sleep
(12636, 4287, 4), -- Resist Hold
(12636, 4379, 4), -- Resist Pole Arm
(12636, 4225, 3), -- Resist Shock
(12636, 4116, 6), -- Resist M. Atk.
(12636, 4151, 4), -- NPC Windstrike - Magic
(12636, 4160, 4), -- NPC Aura Burn - Magic
-- Apostate's Offering Berserker
(12637, 4295, 1), -- Race
(12637, 4285, 4), -- Resist Sleep
(12637, 4287, 4), -- Resist Hold
(12637, 4273, 2), -- Resist Dagger
(12637, 4071, 3), -- Resist Archery
(12637, 4274, 1), -- Blunt Attack Weak Point
(12637, 4116, 6), -- Resist M. Atk.
(12637, 4244, 4), -- NPC Wild Sweep
-- Apostate's Offering Zealot
(12638, 4295, 1), -- Race
(12638, 4285, 4), -- Resist Sleep
(12638, 4287, 4), -- Resist Hold
(12638, 4379, 4), -- Resist Pole Arm
(12638, 4225, 3), -- Resist Shock
(12638, 4032, 4), -- NPC Strike
-- Apostate's Offering Marksman
(12639, 4295, 1), -- Race
(12639, 4285, 4), -- Resist Sleep
(12639, 4287, 4), -- Resist Hold
-- Apostate's Offering Disciple
(12640, 4295, 1), -- Race
(12640, 4285, 4), -- Resist Sleep
(12640, 4287, 4), -- Resist Hold
(12640, 4032, 5), -- NPC Strike
-- Apostate's Offering Saint
(12641, 4295, 1), -- Race
(12641, 4285, 4), -- Resist Sleep
(12641, 4287, 4), -- Resist Hold
(12641, 4273, 2), -- Resist Dagger
(12641, 4071, 3), -- Resist Archery
(12641, 4073, 5), -- Shock
-- Witch's Offering Elder
(12642, 4295, 1), -- Race
(12642, 4285, 4), -- Resist Sleep
(12642, 4287, 4), -- Resist Hold
(12642, 4273, 2), -- Resist Dagger
(12642, 4071, 4), -- Resist Archery
(12642, 4274, 1), -- Blunt Attack Weak Point
(12642, 4078, 5), -- NPC Flamestrike
-- Witch's Offering
(12643, 4295, 1), -- Race
(12643, 4285, 4), -- Resist Sleep
(12643, 4287, 4), -- Resist Hold
(12643, 4071, 4), -- Resist Archery
(12643, 4379, 4), -- Resist Pole Arm
-- Witch's Offering Archer
(12644, 4295, 1), -- Race
(12644, 4285, 4), -- Resist Sleep
(12644, 4287, 4), -- Resist Hold
-- Witch's Offering Warrior
(12645, 4295, 1), -- Race
(12645, 4285, 4), -- Resist Sleep
(12645, 4287, 4), -- Resist Hold
(12645, 4273, 2), -- Resist Dagger
(12645, 4071, 3), -- Resist Archery
(12645, 4274, 1), -- Blunt Attack Weak Point
(12645, 4087, 5), -- NPC Blaze
-- Witch's Offering Follower
(12646, 4295, 1), -- Race
(12646, 4285, 4), -- Resist Sleep
(12646, 4287, 4), -- Resist Hold
(12646, 4379, 4), -- Resist Pole Arm
(12646, 4225, 3), -- Resist Shock
(12646, 4116, 6), -- Resist M. Atk.
(12646, 4151, 5), -- NPC Windstrike - Magic
(12646, 4160, 5), -- NPC Aura Burn - Magic
-- Witch's Offering Berserker
(12647, 4295, 1), -- Race
(12647, 4285, 4), -- Resist Sleep
(12647, 4287, 4), -- Resist Hold
(12647, 4273, 2), -- Resist Dagger
(12647, 4071, 3), -- Resist Archery
(12647, 4274, 1), -- Blunt Attack Weak Point
(12647, 4116, 6), -- Resist M. Atk.
(12647, 4244, 5), -- NPC Wild Sweep
-- Witch's Offering Zealot
(12648, 4295, 1), -- Race
(12648, 4285, 4), -- Resist Sleep
(12648, 4287, 4), -- Resist Hold
(12648, 4379, 4), -- Resist Pole Arm
(12648, 4225, 3), -- Resist Shock
(12648, 4032, 5), -- NPC Strike
-- Witch's Offering Marksman
(12649, 4295, 1), -- Race
(12649, 4285, 4), -- Resist Sleep
(12649, 4287, 4), -- Resist Hold
-- Witch's Offering Disciple
(12650, 4295, 1), -- Race
(12650, 4285, 4), -- Resist Sleep
(12650, 4287, 4), -- Resist Hold
(12650, 4032, 6), -- NPC Strike
-- Witch's Offering Saint
(12651, 4295, 1), -- Race
(12651, 4285, 4), -- Resist Sleep
(12651, 4287, 4), -- Resist Hold
(12651, 4273, 2), -- Resist Dagger
(12651, 4071, 3), -- Resist Archery
(12651, 4073, 6), -- Shock
-- Dark Omen Offering Elder
(12652, 4295, 1), -- Race
(12652, 4285, 4), -- Resist Sleep
(12652, 4287, 4), -- Resist Hold
(12652, 4273, 2), -- Resist Dagger
(12652, 4071, 4), -- Resist Archery
(12652, 4274, 1), -- Blunt Attack Weak Point
(12652, 4078, 6), -- NPC Flamestrike
-- Dark Omen Offering
(12653, 4295, 1), -- Race
(12653, 4285, 4), -- Resist Sleep
(12653, 4287, 4), -- Resist Hold
(12653, 4071, 4), -- Resist Archery
(12653, 4379, 4), -- Resist Pole Arm
-- Dark Omen Offering Archer
(12654, 4295, 1), -- Race
(12654, 4285, 4), -- Resist Sleep
(12654, 4287, 4), -- Resist Hold
-- Dark Omen Offering Warrior
(12655, 4295, 1), -- Race
(12655, 4285, 4), -- Resist Sleep
(12655, 4287, 4), -- Resist Hold
(12655, 4273, 2), -- Resist Dagger
(12655, 4071, 3), -- Resist Archery
(12655, 4274, 1), -- Blunt Attack Weak Point
(12655, 4087, 6), -- NPC Blaze
-- Dark Omen Offering Follower
(12656, 4295, 1), -- Race
(12656, 4285, 4), -- Resist Sleep
(12656, 4287, 4), -- Resist Hold
(12656, 4379, 4), -- Resist Pole Arm
(12656, 4225, 3), -- Resist Shock
(12656, 4116, 6), -- Resist M. Atk.
(12656, 4151, 6), -- NPC Windstrike - Magic
(12656, 4160, 6), -- NPC Aura Burn - Magic
-- Dark Omen Offering Berserker
(12657, 4295, 1), -- Race
(12657, 4285, 4), -- Resist Sleep
(12657, 4287, 4), -- Resist Hold
(12657, 4273, 2), -- Resist Dagger
(12657, 4071, 3), -- Resist Archery
(12657, 4274, 1), -- Blunt Attack Weak Point
(12657, 4116, 6), -- Resist M. Atk.
(12657, 4244, 6), -- NPC Wild Sweep
-- Dark Omen Offering Zealot
(12658, 4295, 1), -- Race
(12658, 4285, 4), -- Resist Sleep
(12658, 4287, 4), -- Resist Hold
(12658, 4379, 4), -- Resist Pole Arm
(12658, 4225, 3), -- Resist Shock
(12658, 4032, 6), -- NPC Strike
-- Dark Omen Offering Marksman
(12659, 4295, 1), -- Race
(12659, 4285, 4), -- Resist Sleep
(12659, 4287, 4), -- Resist Hold
-- Dark Omen Offering Disciple
(12660, 4295, 1), -- Race
(12660, 4285, 4), -- Resist Sleep
(12660, 4287, 4), -- Resist Hold
(12660, 4032, 7), -- NPC Strike
-- Dark Omen Offering Saint
(12661, 4295, 1), -- Race
(12661, 4285, 4), -- Resist Sleep
(12661, 4287, 4), -- Resist Hold
(12661, 4273, 2), -- Resist Dagger
(12661, 4071, 3), -- Resist Archery
(12661, 4073, 7), -- Shock
-- Offering of Forbidden Path Elder
(12662, 4295, 1), -- Race
(12662, 4285, 4), -- Resist Sleep
(12662, 4287, 4), -- Resist Hold
(12662, 4273, 2), -- Resist Dagger
(12662, 4071, 4), -- Resist Archery
(12662, 4274, 1), -- Blunt Attack Weak Point
(12662, 4078, 7), -- NPC Flamestrike
-- Offering of Forbidden Path
(12663, 4295, 1), -- Race
(12663, 4285, 4), -- Resist Sleep
(12663, 4287, 4), -- Resist Hold
(12663, 4071, 4), -- Resist Archery
(12663, 4379, 4), -- Resist Pole Arm
-- Offering of Forbidden Path Archer
(12664, 4295, 1), -- Race
(12664, 4285, 4), -- Resist Sleep
(12664, 4287, 4), -- Resist Hold
-- Offering of Forbidden Path Warrior
(12665, 4295, 1), -- Race
(12665, 4285, 4), -- Resist Sleep
(12665, 4287, 4), -- Resist Hold
(12665, 4273, 2), -- Resist Dagger
(12665, 4071, 3), -- Resist Archery
(12665, 4274, 1), -- Blunt Attack Weak Point
(12665, 4087, 7), -- NPC Blaze
-- Offering of Forbidden Path Follower
(12666, 4295, 1), -- Race
(12666, 4285, 4), -- Resist Sleep
(12666, 4287, 4), -- Resist Hold
(12666, 4379, 4), -- Resist Pole Arm
(12666, 4225, 3), -- Resist Shock
(12666, 4116, 6), -- Resist M. Atk.
(12666, 4151, 7), -- NPC Windstrike - Magic
(12666, 4160, 7), -- NPC Aura Burn - Magic
-- Offering of Forbidden Path Berserker
(12667, 4295, 1), -- Race
(12667, 4285, 4), -- Resist Sleep
(12667, 4287, 4), -- Resist Hold
(12667, 4273, 2), -- Resist Dagger
(12667, 4071, 3), -- Resist Archery
(12667, 4274, 1), -- Blunt Attack Weak Point
(12667, 4116, 6), -- Resist M. Atk.
(12667, 4244, 7), -- NPC Wild Sweep
-- Offering of Forbidden Path Zealot
(12668, 4295, 1), -- Race
(12668, 4285, 4), -- Resist Sleep
(12668, 4287, 4), -- Resist Hold
(12668, 4379, 4), -- Resist Pole Arm
(12668, 4225, 3), -- Resist Shock
(12668, 4032, 7), -- NPC Strike
-- Offering of Forbidden Path Marksman
(12669, 4295, 1), -- Race
(12669, 4285, 4), -- Resist Sleep
(12669, 4287, 4), -- Resist Hold
-- Offering of Forbidden Path Disciple
(12670, 4295, 1), -- Race
(12670, 4285, 4), -- Resist Sleep
(12670, 4287, 4), -- Resist Hold
(12670, 4032, 7), -- NPC Strike
-- Offering of Forbidden Path Saint
(12671, 4295, 1), -- Race
(12671, 4285, 4), -- Resist Sleep
(12671, 4287, 4), -- Resist Hold
(12671, 4273, 2), -- Resist Dagger
(12671, 4071, 3), -- Resist Archery
(12671, 4073, 7), -- Shock
-- Offering of Branded Elder
(12672, 4295, 1), -- Race
(12672, 4285, 4), -- Resist Sleep
(12672, 4287, 4), -- Resist Hold
(12672, 4273, 2), -- Resist Dagger
(12672, 4071, 4), -- Resist Archery
(12672, 4274, 1), -- Blunt Attack Weak Point
(12672, 4078, 3), -- NPC Flamestrike
-- Offering of the Branded
(12673, 4295, 1), -- Race
(12673, 4285, 4), -- Resist Sleep
(12673, 4287, 4), -- Resist Hold
(12673, 4071, 4), -- Resist Archery
(12673, 4379, 4), -- Resist Pole Arm
-- Offering of Branded Archer
(12674, 4295, 1), -- Race
(12674, 4285, 4), -- Resist Sleep
(12674, 4287, 4), -- Resist Hold
-- Offering of Branded Warrior
(12675, 4295, 1), -- Race
(12675, 4285, 4), -- Resist Sleep
(12675, 4287, 4), -- Resist Hold
(12675, 4273, 2), -- Resist Dagger
(12675, 4071, 3), -- Resist Archery
(12675, 4274, 1), -- Blunt Attack Weak Point
(12675, 4087, 3), -- NPC Blaze
-- Offering of Branded Follower
(12676, 4295, 1), -- Race
(12676, 4285, 4), -- Resist Sleep
(12676, 4287, 4), -- Resist Hold
(12676, 4379, 4), -- Resist Pole Arm
(12676, 4225, 3), -- Resist Shock
(12676, 4116, 6), -- Resist M. Atk.
(12676, 4151, 2), -- NPC Windstrike - Magic
(12676, 4160, 2), -- NPC Aura Burn - Magic
-- Offering of Branded Berserker
(12677, 4295, 1), -- Race
(12677, 4285, 4), -- Resist Sleep
(12677, 4287, 4), -- Resist Hold
(12677, 4273, 2), -- Resist Dagger
(12677, 4071, 3), -- Resist Archery
(12677, 4274, 1), -- Blunt Attack Weak Point
(12677, 4116, 6), -- Resist M. Atk.
(12677, 4244, 3), -- NPC Wild Sweep
-- Offering of Branded Zealot
(12678, 4295, 1), -- Race
(12678, 4285, 4), -- Resist Sleep
(12678, 4287, 4), -- Resist Hold
(12678, 4379, 4), -- Resist Pole Arm
(12678, 4225, 3), -- Resist Shock
(12678, 4032, 3), -- NPC Strike
-- Offering of Branded Marksman
(12679, 4295, 1), -- Race
(12679, 4285, 4), -- Resist Sleep
(12679, 4287, 4), -- Resist Hold
-- Offering of Branded Disciple
(12680, 4295, 1), -- Race
(12680, 4285, 4), -- Resist Sleep
(12680, 4287, 4), -- Resist Hold
(12680, 4032, 4), -- NPC Strike
-- Offering of Branded Saint
(12681, 4295, 1), -- Race
(12681, 4285, 4), -- Resist Sleep
(12681, 4287, 4), -- Resist Hold
(12681, 4273, 2), -- Resist Dagger
(12681, 4071, 3), -- Resist Archery
(12681, 4073, 4), -- Shock
-- Apostate's Offering Elder
(12682, 4295, 1), -- Race
(12682, 4285, 4), -- Resist Sleep
(12682, 4287, 4), -- Resist Hold
(12682, 4273, 2), -- Resist Dagger
(12682, 4071, 4), -- Resist Archery
(12682, 4274, 1), -- Blunt Attack Weak Point
(12682, 4078, 4), -- NPC Flamestrike
-- Apostate's Offering 
(12683, 4295, 1), -- Race
(12683, 4285, 4), -- Resist Sleep
(12683, 4287, 4), -- Resist Hold
(12683, 4071, 4), -- Resist Archery
(12683, 4379, 4), -- Resist Pole Arm
-- Apostate's Offering Archer
(12684, 4295, 1), -- Race
(12684, 4285, 4), -- Resist Sleep
(12684, 4287, 4), -- Resist Hold
-- Apostate's Offering Warrior
(12685, 4295, 1), -- Race
(12685, 4285, 4), -- Resist Sleep
(12685, 4287, 4), -- Resist Hold
(12685, 4273, 2), -- Resist Dagger
(12685, 4071, 3), -- Resist Archery
(12685, 4274, 1), -- Blunt Attack Weak Point
(12685, 4087, 4), -- NPC Blaze
-- Apostate's Offering Follower
(12686, 4295, 1), -- Race
(12686, 4285, 4), -- Resist Sleep
(12686, 4287, 4), -- Resist Hold
(12686, 4379, 4), -- Resist Pole Arm
(12686, 4225, 3), -- Resist Shock
(12686, 4116, 6), -- Resist M. Atk.
(12686, 4151, 4), -- NPC Windstrike - Magic
(12686, 4160, 4), -- NPC Aura Burn - Magic
-- Apostate's Offering Berserker
(12687, 4295, 1), -- Race
(12687, 4285, 4), -- Resist Sleep
(12687, 4287, 4), -- Resist Hold
(12687, 4273, 2), -- Resist Dagger
(12687, 4071, 3), -- Resist Archery
(12687, 4274, 1), -- Blunt Attack Weak Point
(12687, 4116, 6), -- Resist M. Atk.
(12687, 4244, 4), -- NPC Wild Sweep
-- Apostate's Offering Zealot
(12688, 4295, 1), -- Race
(12688, 4285, 4), -- Resist Sleep
(12688, 4287, 4), -- Resist Hold
(12688, 4379, 4), -- Resist Pole Arm
(12688, 4225, 3), -- Resist Shock
(12688, 4032, 4), -- NPC Strike
-- Apostate's Offering Marksman
(12689, 4295, 1), -- Race
(12689, 4285, 4), -- Resist Sleep
(12689, 4287, 4), -- Resist Hold
-- Apostate's Offering Disciple
(12690, 4295, 1), -- Race
(12690, 4285, 4), -- Resist Sleep
(12690, 4287, 4), -- Resist Hold
(12690, 4032, 5), -- NPC Strike
-- Apostate's Offering Saint
(12691, 4295, 1), -- Race
(12691, 4285, 4), -- Resist Sleep
(12691, 4287, 4), -- Resist Hold
(12691, 4273, 2), -- Resist Dagger
(12691, 4071, 3), -- Resist Archery
(12691, 4073, 5), -- Shock
-- Witch's Offering Elder
(12692, 4295, 1), -- Race
(12692, 4285, 4), -- Resist Sleep
(12692, 4287, 4), -- Resist Hold
(12692, 4273, 2), -- Resist Dagger
(12692, 4071, 4), -- Resist Archery
(12692, 4274, 1), -- Blunt Attack Weak Point
(12692, 4078, 5), -- NPC Flamestrike
-- Witch's Offering
(12693, 4295, 1), -- Race
(12693, 4285, 4), -- Resist Sleep
(12693, 4287, 4), -- Resist Hold
(12693, 4071, 4), -- Resist Archery
(12693, 4379, 4), -- Resist Pole Arm
-- Witch's Offering Archer
(12694, 4295, 1), -- Race
(12694, 4285, 4), -- Resist Sleep
(12694, 4287, 4), -- Resist Hold
-- Witch's Offering Warrior
(12695, 4295, 1), -- Race
(12695, 4285, 4), -- Resist Sleep
(12695, 4287, 4), -- Resist Hold
(12695, 4273, 2), -- Resist Dagger
(12695, 4071, 3), -- Resist Archery
(12695, 4274, 1), -- Blunt Attack Weak Point
(12695, 4087, 5), -- NPC Blaze
-- Witch's Offering Follower
(12696, 4295, 1), -- Race
(12696, 4285, 4), -- Resist Sleep
(12696, 4287, 4), -- Resist Hold
(12696, 4379, 4), -- Resist Pole Arm
(12696, 4225, 3), -- Resist Shock
(12696, 4116, 6), -- Resist M. Atk.
(12696, 4151, 5), -- NPC Windstrike - Magic
(12696, 4160, 5), -- NPC Aura Burn - Magic
-- Witch's Offering Berserker
(12697, 4295, 1), -- Race
(12697, 4285, 4), -- Resist Sleep
(12697, 4287, 4), -- Resist Hold
(12697, 4273, 2), -- Resist Dagger
(12697, 4071, 3), -- Resist Archery
(12697, 4274, 1), -- Blunt Attack Weak Point
(12697, 4116, 6), -- Resist M. Atk.
(12697, 4244, 5), -- NPC Wild Sweep
-- Witch's Offering Zealot
(12698, 4295, 1), -- Race
(12698, 4285, 4), -- Resist Sleep
(12698, 4287, 4), -- Resist Hold
(12698, 4379, 4), -- Resist Pole Arm
(12698, 4225, 3), -- Resist Shock
(12698, 4032, 5), -- NPC Strike
-- Witch's Offering Marksman
(12699, 4295, 1), -- Race
(12699, 4285, 4), -- Resist Sleep
(12699, 4287, 4), -- Resist Hold
-- Witch's Offering Disciple
(12700, 4295, 1), -- Race
(12700, 4285, 4), -- Resist Sleep
(12700, 4287, 4), -- Resist Hold
(12700, 4032, 6), -- NPC Strike
-- Witch's Offering Saint
(12701, 4295, 1), -- Race
(12701, 4285, 4), -- Resist Sleep
(12701, 4287, 4), -- Resist Hold
(12701, 4273, 2), -- Resist Dagger
(12701, 4071, 3), -- Resist Archery
(12701, 4073, 6), -- Shock
-- Dark Omen Offering Elder
(12702, 4295, 1), -- Race
(12702, 4285, 4), -- Resist Sleep
(12702, 4287, 4), -- Resist Hold
(12702, 4273, 2), -- Resist Dagger
(12702, 4071, 4), -- Resist Archery
(12702, 4274, 1), -- Blunt Attack Weak Point
(12702, 4078, 6), -- NPC Flamestrike
-- Dark Omen Offering
(12703, 4295, 1), -- Race
(12703, 4285, 4), -- Resist Sleep
(12703, 4287, 4), -- Resist Hold
(12703, 4071, 4), -- Resist Archery
(12703, 4379, 4), -- Resist Pole Arm
-- Dark Omen Offering Archer
(12704, 4295, 1), -- Race
(12704, 4285, 4), -- Resist Sleep
(12704, 4287, 4), -- Resist Hold
-- Dark Omen Offering Warrior
(12705, 4295, 1), -- Race
(12705, 4285, 4), -- Resist Sleep
(12705, 4287, 4), -- Resist Hold
(12705, 4273, 2), -- Resist Dagger
(12705, 4071, 3), -- Resist Archery
(12705, 4274, 1), -- Blunt Attack Weak Point
(12705, 4087, 6), -- NPC Blaze
-- Dark Omen Offering Follower
(12706, 4295, 1), -- Race
(12706, 4285, 4), -- Resist Sleep
(12706, 4287, 4), -- Resist Hold
(12706, 4379, 4), -- Resist Pole Arm
(12706, 4225, 3), -- Resist Shock
(12706, 4116, 6), -- Resist M. Atk.
(12706, 4151, 6), -- NPC Windstrike - Magic
(12706, 4160, 6), -- NPC Aura Burn - Magic
-- Dark Omen Offering Berserker
(12707, 4295, 1), -- Race
(12707, 4285, 4), -- Resist Sleep
(12707, 4287, 4), -- Resist Hold
(12707, 4273, 2), -- Resist Dagger
(12707, 4071, 3), -- Resist Archery
(12707, 4274, 1), -- Blunt Attack Weak Point
(12707, 4116, 6), -- Resist M. Atk.
(12707, 4244, 7), -- NPC Wild Sweep
-- Dark Omen Offering Zealot
(12708, 4295, 1), -- Race
(12708, 4285, 4), -- Resist Sleep
(12708, 4287, 4), -- Resist Hold
(12708, 4379, 4), -- Resist Pole Arm
(12708, 4225, 3), -- Resist Shock
(12708, 4032, 6), -- NPC Strike
-- Dark Omen Offering Marksman
(12709, 4295, 1), -- Race
(12709, 4285, 4), -- Resist Sleep
(12709, 4287, 4), -- Resist Hold
-- Dark Omen Offering Disciple
(12710, 4295, 1), -- Race
(12710, 4285, 4), -- Resist Sleep
(12710, 4287, 4), -- Resist Hold
(12710, 4032, 7), -- NPC Strike
-- Dark Omen Offering Saint
(12711, 4295, 1), -- Race
(12711, 4285, 4), -- Resist Sleep
(12711, 4287, 4), -- Resist Hold
(12711, 4273, 2), -- Resist Dagger
(12711, 4071, 3), -- Resist Archery
(12711, 4073, 7), -- Shock
-- Offering of Forbidden Path Elder
(12712, 4295, 1), -- Race
(12712, 4285, 4), -- Resist Sleep
(12712, 4287, 4), -- Resist Hold
(12712, 4273, 2), -- Resist Dagger
(12712, 4071, 4), -- Resist Archery
(12712, 4274, 1), -- Blunt Attack Weak Point
(12712, 4078, 8), -- NPC Flamestrike
-- Offering of Forbidden Path
(12713, 4295, 1), -- Race
(12713, 4285, 4), -- Resist Sleep
(12713, 4287, 4), -- Resist Hold
(12713, 4071, 4), -- Resist Archery
(12713, 4379, 4), -- Resist Pole Arm
-- Offering of Forbidden Path Archer
(12714, 4295, 1), -- Race
(12714, 4285, 4), -- Resist Sleep
(12714, 4287, 4), -- Resist Hold
-- Offering of Forbidden Path Warrior
(12715, 4295, 1), -- Race
(12715, 4285, 4), -- Resist Sleep
(12715, 4287, 4), -- Resist Hold
(12715, 4273, 2), -- Resist Dagger
(12715, 4071, 3), -- Resist Archery
(12715, 4274, 1), -- Blunt Attack Weak Point
(12715, 4087, 9), -- NPC Blaze
-- Offering of Forbidden Path Follower
(12716, 4295, 1), -- Race
(12716, 4285, 4), -- Resist Sleep
(12716, 4287, 4), -- Resist Hold
(12716, 4379, 4), -- Resist Pole Arm
(12716, 4225, 3), -- Resist Shock
(12716, 4116, 6), -- Resist M. Atk.
(12716, 4151, 9), -- NPC Windstrike - Magic
(12716, 4160, 9), -- NPC Aura Burn - Magic
-- Offering of Forbidden Path Berserker
(12717, 4295, 1), -- Race
(12717, 4285, 4), -- Resist Sleep
(12717, 4287, 4), -- Resist Hold
(12717, 4273, 2), -- Resist Dagger
(12717, 4071, 3), -- Resist Archery
(12717, 4274, 1), -- Blunt Attack Weak Point
(12717, 4116, 6), -- Resist M. Atk.
(12717, 4244, 9), -- NPC Wild Sweep
-- Offering of Forbidden Path Zealot
(12718, 4295, 1), -- Race
(12718, 4285, 4), -- Resist Sleep
(12718, 4287, 4), -- Resist Hold
(12718, 4379, 4), -- Resist Pole Arm
(12718, 4225, 3), -- Resist Shock
(12718, 4032, 8), -- NPC Strike
-- Offering of Forbidden Path Marksman
(12719, 4295, 1), -- Race
(12719, 4285, 4), -- Resist Sleep
(12719, 4287, 4), -- Resist Hold
-- Offering of Forbidden Path Disciple
(12720, 4295, 1), -- Race
(12720, 4285, 4), -- Resist Sleep
(12720, 4287, 4), -- Resist Hold
(12720, 4032, 8), -- NPC Strike
-- Offering of Forbidden Path Saint
(12721, 4295, 1), -- Race
(12721, 4285, 4), -- Resist Sleep
(12721, 4287, 4), -- Resist Hold
(12721, 4273, 2), -- Resist Dagger
(12721, 4071, 3), -- Resist Archery
(12721, 4073, 9), -- Shock
-- Mercenary of Dawn
(12722, 4290, 1), -- Race
(12722, 4045, 1), -- Resist Full Magic Attack
-- Mercenary of Dawn
(12723, 4290, 1), -- Race
(12723, 4045, 1), -- Resist Full Magic Attack
-- Mercenary of Dawn
(12724, 4290, 1), -- Race
(12724, 4045, 1), -- Resist Full Magic Attack
-- Mercenary of Dawn
(12725, 4290, 1), -- Race
(12725, 4045, 1), -- Resist Full Magic Attack
(12725, 4027, 1), -- Gludio Heal
-- Mercenary of Dawn
(12726, 4290, 1), -- Race
(12726, 4045, 1), -- Resist Full Magic Attack
(12726, 4026, 1), -- Gludio Flame
-- Mercenary of Dawn
(12727, 4290, 1), -- Race
(12727, 4045, 1), -- Resist Full Magic Attack
-- Mercenary of Dawn
(12728, 4290, 1), -- Race
(12728, 4045, 1), -- Resist Full Magic Attack
-- Mercenary of Dawn
(12729, 4290, 1), -- Race
(12729, 4045, 1), -- Resist Full Magic Attack
-- Mercenary of Dawn
(12730, 4290, 1), -- Race
(12730, 4045, 1), -- Resist Full Magic Attack
(12730, 4027, 1), -- Gludio Heal
-- Mercenary of Dawn
(12731, 4290, 1), -- Race
(12731, 4045, 1), -- Resist Full Magic Attack
(12731, 4026, 1), -- Gludio Flame
-- Elite Mercenary Guild Member
(12732, 4290, 1), -- Race
(12732, 4045, 1), -- Resist Full Magic Attack
-- Elite Mercenary Guild Member
(12733, 4290, 1), -- Race
(12733, 4045, 1), -- Resist Full Magic Attack
-- Elite Mercenary Guild Member
(12734, 4290, 1), -- Race
(12734, 4045, 1), -- Resist Full Magic Attack
-- Elite Mercenary Guild Member
(12735, 4290, 1), -- Race
(12735, 4045, 1), -- Resist Full Magic Attack
(12735, 4027, 1), -- Gludio Heal
-- Elite Mercenary Guild Member
(12736, 4290, 1), -- Race
(12736, 4045, 1), -- Resist Full Magic Attack
(12736, 4026, 1), -- Gludio Flame
-- Elite Mercenary Guild Member
(12737, 4290, 1), -- Race
(12737, 4045, 1), -- Resist Full Magic Attack
-- Elite Mercenary Guild Member
(12738, 4290, 1), -- Race
(12738, 4045, 1), -- Resist Full Magic Attack
-- Elite Mercenary Guild Member
(12739, 4290, 1), -- Race
(12739, 4045, 1), -- Resist Full Magic Attack
-- Elite Mercenary Guild Member
(12740, 4290, 1), -- Race
(12740, 4045, 1), -- Resist Full Magic Attack
(12740, 4027, 1), -- Gludio Heal
-- Elite Mercenary Guild Member
(12741, 4290, 1), -- Race
(12741, 4045, 1), -- Resist Full Magic Attack
(12741, 4026, 1), -- Gludio Flame
-- Greater Recruit
(12742, 4290, 1), -- Race
(12742, 4045, 1), -- Resist Full Magic Attack
-- Greater Recruit
(12743, 4290, 1), -- Race
(12743, 4045, 1), -- Resist Full Magic Attack
-- Greater Recruit
(12744, 4290, 1), -- Race
(12744, 4045, 1), -- Resist Full Magic Attack
-- Greater Recruit
(12745, 4290, 1), -- Race
(12745, 4045, 1), -- Resist Full Magic Attack
(12745, 4027, 1), -- Gludio Heal
-- Greater Recruit
(12746, 4290, 1), -- Race
(12746, 4045, 1), -- Resist Full Magic Attack
(12746, 4026, 1), -- Gludio Flame
-- Greater Recruit
(12747, 4290, 1), -- Race
(12747, 4045, 1), -- Resist Full Magic Attack
-- Greater Recruit
(12748, 4290, 1), -- Race
(12748, 4045, 1), -- Resist Full Magic Attack
-- Greater Recruit
(12749, 4290, 1), -- Race
(12749, 4045, 1), -- Resist Full Magic Attack
-- Greater Recruit
(12750, 4290, 1), -- Race
(12750, 4045, 1), -- Resist Full Magic Attack
(12750, 4027, 1), -- Gludio Heal
-- Greater Recruit
(12751, 4290, 1), -- Race
(12751, 4045, 1), -- Resist Full Magic Attack
(12751, 4026, 1), -- Gludio Flame
-- Recruit
(12752, 4290, 1), -- Race
(12752, 4045, 1), -- Resist Full Magic Attack
-- Recruit
(12753, 4290, 1), -- Race
(12753, 4045, 1), -- Resist Full Magic Attack
-- Recruit
(12754, 4290, 1), -- Race
(12754, 4045, 1), -- Resist Full Magic Attack
-- Recruit
(12755, 4290, 1), -- Race
(12755, 4045, 1), -- Resist Full Magic Attack
(12755, 4027, 1), -- Gludio Heal
-- Recruit
(12756, 4290, 1), -- Race
(12756, 4045, 1), -- Resist Full Magic Attack
(12756, 4026, 1), -- Gludio Flame
-- Recruit
(12757, 4290, 1), -- Race
(12757, 4045, 1), -- Resist Full Magic Attack
-- Recruit
(12758, 4290, 1), -- Race
(12758, 4045, 1), -- Resist Full Magic Attack
-- Recruit
(12759, 4290, 1), -- Race
(12759, 4045, 1), -- Resist Full Magic Attack
-- Recruit
(12760, 4290, 1), -- Race
(12760, 4045, 1), -- Resist Full Magic Attack
(12760, 4027, 1), -- Gludio Heal
-- Recruit
(12761, 4290, 1), -- Race
(12761, 4045, 1), -- Resist Full Magic Attack
(12761, 4026, 1), -- Gludio Flame
-- Nephilim Mercenary
(12762, 4290, 1), -- Race
(12762, 4045, 1), -- Resist Full Magic Attack
-- Nephilim Mercenary
(12763, 4290, 1), -- Race
(12763, 4045, 1), -- Resist Full Magic Attack
(12763, 4026, 1), -- Gludio Flame
-- Treasures of the Festival
(12764, 4291, 1), -- Race
(12764, 4045, 1), -- Resist Full Magic Attack
(12764, 4116, 7), -- Resist M. Atk.
-- Treasures of the Festival
(12765, 4291, 1), -- Race
(12765, 4045, 1), -- Resist Full Magic Attack
(12765, 4116, 7), -- Resist M. Atk.
-- Treasures of the Festival
(12766, 4291, 1), -- Race
(12766, 4045, 1), -- Resist Full Magic Attack
(12766, 4116, 7), -- Resist M. Atk.
-- Treasures of the Festival
(12767, 4291, 1), -- Race
(12767, 4045, 1), -- Resist Full Magic Attack
(12767, 4116, 7), -- Resist M. Atk.
-- Treasures of the Festival
(12768, 4291, 1), -- Race
(12768, 4045, 1), -- Resist Full Magic Attack
(12768, 4116, 7), -- Resist M. Atk.
-- Treasures of the Festival
(12769, 4291, 1), -- Race
(12769, 4045, 1), -- Resist Full Magic Attack
(12769, 4116, 7), -- Resist M. Atk.
-- Treasures of the Festival
(12770, 4291, 1), -- Race
(12770, 4045, 1), -- Resist Full Magic Attack
(12770, 4116, 7), -- Resist M. Atk.
-- Treasures of the Festival
(12771, 4291, 1), -- Race
(12771, 4045, 1), -- Resist Full Magic Attack
(12771, 4116, 7), -- Resist M. Atk.
-- Treasures of the Festival
(12772, 4291, 1), -- Race
(12772, 4045, 1), -- Resist Full Magic Attack
(12772, 4116, 7), -- Resist M. Atk.
-- Treasures of the Festival
(12773, 4291, 1), -- Race
(12773, 4045, 1), -- Resist Full Magic Attack
(12773, 4116, 7), -- Resist M. Atk.
-- Young Pumpkin
(12774, 4290, 1), -- Race
(12774, 4045, 1), -- Resist Full Magic Attack
-- High Quality Pumpkin
(12775, 4290, 1), -- Race
(12775, 4045, 1), -- Resist Full Magic Attack
-- Low Quality Pumpkin
(12776, 4290, 1), -- Race
(12776, 4045, 1), -- Resist Full Magic Attack
-- Large Young Pumpkin
(12777, 4290, 1), -- Race
(12777, 4045, 1), -- Resist Full Magic Attack
-- High Quality Large Pumpkin
(12778, 4290, 1), -- Race
(12778, 4045, 1), -- Resist Full Magic Attack
-- Low Quality Large Pumpkin
(12779, 4290, 1), -- Race
(12779, 4045, 1), -- Resist Full Magic Attack
-- Baby Buffalo
(12780, 4293, 1), -- Race
(12780, 4045, 1), -- Resist Full Magic Attack
(12780, 4717, 1), -- Heal Trick
(12780, 4718, 1), -- Greater Heal Trick
-- Baby Kookaburra
(12781, 4293, 1), -- Race
(12781, 4045, 1), -- Resist Full Magic Attack
(12781, 4717, 1), -- Heal Trick
(12781, 4718, 1), -- Greater Heal Trick
-- Baby Cougar
(12782, 4293, 1), -- Race
(12782, 4045, 1), -- Resist Full Magic Attack
(12782, 4717, 1), -- Heal Trick
(12782, 4718, 1), -- Greater Heal Trick
-- Trained Kookaburra
(12783, 4293, 1), -- Race
(12783, 4303, 1), -- Strong Type
(12783, 4285, 4), -- Resist Sleep
(12783, 1086, 2), -- Haste
(12783, 1268, 3), -- Vampiric Rage
(12783, 1044, 3), -- Regeneration
(12783, 1045, 5), -- Bless the Body
(12783, 1240, 3), -- Guidance
(12783, 1206, 14), -- Wind Shackle
(12783, 122, 10), -- Hex
(12783, 1217, 24), -- Greater Heal
-- Trained Kookaburra
(12784, 4293, 1), -- Race
(12784, 4303, 1), -- Strong Type
(12784, 4285, 4), -- Resist Sleep
(12784, 1204, 2), -- Wind Walk
(12784, 1085, 3), -- Acumen
(12784, 1078, 5), -- Concentration
(12784, 1059, 3), -- Greater Empower
(12784, 1048, 5), -- Bless the Soul
(12784, 1160, 10), -- Slow
(12784, 1263, 8), -- Curse Gloom
(12784, 1013, 23), -- Recharge
-- Trained Buffalo
(12785, 4293, 1), -- Race
(12785, 4303, 1), -- Strong Type
(12785, 4285, 4), -- Resist Sleep
(12785, 1086, 2), -- Haste
(12785, 1268, 3), -- Vampiric Rage
(12785, 1044, 3), -- Regeneration
(12785, 1045, 5), -- Bless the Body
(12785, 1240, 3), -- Guidance
(12785, 1206, 15), -- Wind Shackle
(12785, 122, 11), -- Hex
(12785, 1217, 26), -- Greater Heal
-- Trained Buffalo
(12786, 4293, 1), -- Race
(12786, 4303, 1), -- Strong Type
(12786, 4285, 4), -- Resist Sleep
(12786, 1204, 2), -- Wind Walk
(12786, 1085, 3), -- Acumen
(12786, 1078, 5), -- Concentration
(12786, 1059, 3), -- Greater Empower
(12786, 1048, 5), -- Bless the Soul
(12786, 1160, 11), -- Slow
(12786, 1263, 9), -- Curse Gloom
(12786, 1013, 25), -- Recharge
-- Trained Cougar
(12787, 4293, 1), -- Race
(12787, 4303, 1), -- Strong Type
(12787, 4285, 4), -- Resist Sleep
(12787, 1086, 2), -- Haste
(12787, 1268, 3), -- Vampiric Rage
(12787, 1044, 3), -- Regeneration
(12787, 1045, 5), -- Bless the Body
(12787, 1240, 3), -- Guidance
(12787, 1206, 16), -- Wind Shackle
(12787, 122, 12), -- Hex
(12787, 1217, 28), -- Greater Heal
-- Trained Cougar
(12788, 4293, 1), -- Race
(12788, 4303, 1), -- Strong Type
(12788, 4285, 4), -- Resist Sleep
(12788, 1204, 2), -- Wind Walk
(12788, 1085, 3), -- Acumen
(12788, 1078, 6), -- Concentration
(12788, 1059, 3), -- Greater Empower
(12788, 1048, 5), -- Bless the Soul
(12788, 1160, 12), -- Slow
(12788, 1263, 10), -- Curse Gloom
(12788, 1013, 27), -- Recharge
-- Corpse of Deadman
(12789, 4290, 1), -- Race
(12789, 4275, 3), -- Sacred Attack Weak Point
(12789, 4278, 1), -- Dark Attack
(12789, 4279, 1), -- Fire Attack Weak Point
-- Rowell
(12790, 4290, 1), -- Race
(12790, 4045, 1), -- Resist Full Magic Attack
-- Alfred
(12791, 4290, 1), -- Race
(12791, 4045, 1), -- Resist Full Magic Attack
-- Daven
(12792, 4290, 1), -- Race
(12792, 4045, 1), -- Resist Full Magic Attack
-- Heinz
(12793, 4290, 1), -- Race
(12793, 4045, 1), -- Resist Full Magic Attack
-- Duke Mora Ken Abygail
(12794, 4290, 1), -- Race
(12794, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12795, 4290, 1), -- Race
(12795, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12796, 4290, 1), -- Race
(12796, 4045, 1), -- Resist Full Magic Attack
(12796, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12797, 4290, 1), -- Race
(12797, 4045, 1), -- Resist Full Magic Attack
(12797, 4027, 1), -- Gludio Heal
-- Guard
(12798, 4290, 1), -- Race
(12798, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12799, 4290, 1), -- Race
(12799, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12800, 4290, 1), -- Race
(12800, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12801, 4290, 1), -- Race
(12801, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12802, 4290, 1), -- Race
(12802, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12803, 4290, 1), -- Race
(12803, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12804, 4290, 1), -- Race
(12804, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12805, 4290, 1), -- Race
(12805, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12806, 4290, 1), -- Race
(12806, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12807, 4290, 1), -- Race
(12807, 4045, 1), -- Resist Full Magic Attack
-- Royal Knight
(12808, 4290, 1), -- Race
(12808, 4045, 1), -- Resist Full Magic Attack
-- Court Sorcerer
(12809, 4290, 1), -- Race
(12809, 4045, 1), -- Resist Full Magic Attack
(12809, 4026, 1), -- Gludio Flame
-- Court Sorcerer
(12810, 4290, 1), -- Race
(12810, 4045, 1), -- Resist Full Magic Attack
(12810, 4027, 1), -- Gludio Heal
-- Guard
(12811, 4290, 1), -- Race
(12811, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12812, 4290, 1), -- Race
(12812, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12813, 4290, 1), -- Race
(12813, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12814, 4290, 1), -- Race
(12814, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12815, 4290, 1), -- Race
(12815, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12816, 4290, 1), -- Race
(12816, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12817, 4290, 1), -- Race
(12817, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12818, 4290, 1), -- Race
(12818, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12819, 4290, 1), -- Race
(12819, 4045, 1), -- Resist Full Magic Attack
-- Bodyguard
(12820, 4290, 1), -- Race
(12820, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12821, 4290, 1), -- Race
(12821, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12822, 4290, 1), -- Race
(12822, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12823, 4290, 1), -- Race
(12823, 4045, 1), -- Resist Full Magic Attack
-- Mass Gatekeeper
(12824, 4290, 1), -- Race
(12824, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(12825, 4290, 1), -- Race
(12825, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12826, 4290, 1), -- Race
(12826, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12827, 4290, 1), -- Race
(12827, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12828, 4290, 1), -- Race
(12828, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12829, 4290, 1), -- Race
(12829, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12830, 4290, 1), -- Race
(12830, 4045, 1), -- Resist Full Magic Attack
-- Altar of Fire
(12831, 4290, 1), -- Race
(12831, 4045, 1), -- Resist Full Magic Attack
-- Altar of Water
(12832, 4290, 1), -- Race
(12832, 4045, 1), -- Resist Full Magic Attack
-- Adrienne
(12833, 4290, 1), -- Race
(12833, 4045, 1), -- Resist Full Magic Attack
-- Bianca
(12834, 4290, 1), -- Race
(12834, 4045, 1), -- Resist Full Magic Attack
-- Emma
(12835, 4290, 1), -- Race
(12835, 4045, 1), -- Resist Full Magic Attack
-- Gladys
(12836, 4290, 1), -- Race
(12836, 4045, 1), -- Resist Full Magic Attack
-- Regina
(12837, 4290, 1), -- Race
(12837, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12838, 4290, 1), -- Race
(12838, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12839, 4290, 1), -- Race
(12839, 4045, 1), -- Resist Full Magic Attack
-- Gatekeeper
(12840, 4290, 1), -- Race
(12840, 4045, 1), -- Resist Full Magic Attack
-- Mass Gatekeeper
(12841, 4290, 1), -- Race
(12841, 4045, 1), -- Resist Full Magic Attack
-- Outer Doorman
(12842, 4290, 1), -- Race
(12842, 4045, 1), -- Resist Full Magic Attack
-- Inner Doorman
(12843, 4290, 1), -- Race
(12843, 4045, 1), -- Resist Full Magic Attack
-- Tamutak
(12844, 4295, 1), -- Race
(12844, 4045, 1), -- Resist Full Magic Attack
-- Brakel
(12845, 4295, 1), -- Race
(12845, 4045, 1), -- Resist Full Magic Attack
-- Ruben
(12846, 4290, 1), -- Race
(12846, 4045, 1), -- Resist Full Magic Attack
-- Horner
(12847, 4290, 1), -- Race
(12847, 4045, 1), -- Resist Full Magic Attack
-- Bremmer
(12848, 4290, 1), -- Race
(12848, 4045, 1), -- Resist Full Magic Attack
-- Kalis
(12849, 4290, 1), -- Race
(12849, 4045, 1), -- Resist Full Magic Attack
-- Winker
(12850, 4290, 1), -- Race
(12850, 4045, 1), -- Resist Full Magic Attack
-- Black
(12851, 4290, 1), -- Race
(12851, 4045, 1), -- Resist Full Magic Attack
-- Dillon
(12852, 4290, 1), -- Race
(12852, 4045, 1), -- Resist Full Magic Attack
-- Boyer
(12853, 4290, 1), -- Race
(12853, 4045, 1), -- Resist Full Magic Attack
-- Tim
(12854, 4290, 1), -- Race
(12854, 4045, 1), -- Resist Full Magic Attack
-- Lowell
(12855, 4290, 1), -- Race
(12855, 4045, 1), -- Resist Full Magic Attack
-- Paranos
(12856, 4290, 1), -- Race
(12856, 4045, 1), -- Resist Full Magic Attack
-- Klingel
(12857, 4290, 1), -- Race
(12857, 4045, 1), -- Resist Full Magic Attack
-- Keffer
(12858, 4290, 1), -- Race
(12858, 4045, 1), -- Resist Full Magic Attack
-- Sand
(12859, 4290, 1), -- Race
(12859, 4045, 1), -- Resist Full Magic Attack
-- Teters
(12860, 4290, 1), -- Race
(12860, 4045, 1), -- Resist Full Magic Attack
-- Seth
(12861, 4290, 1), -- Race
(12861, 4045, 1), -- Resist Full Magic Attack
-- Ron
(12862, 4290, 1), -- Race
(12862, 4045, 1), -- Resist Full Magic Attack
-- Flynn
(12863, 4290, 1), -- Race
(12863, 4045, 1), -- Resist Full Magic Attack
-- Watkins
(12864, 4290, 1), -- Race
(12864, 4045, 1), -- Resist Full Magic Attack
-- Cohen
(12865, 4290, 1), -- Race
(12865, 4045, 1), -- Resist Full Magic Attack
-- Bint
(12866, 4290, 1), -- Race
(12866, 4045, 1), -- Resist Full Magic Attack
-- Bourdon
(12867, 4290, 1), -- Race
(12867, 4045, 1), -- Resist Full Magic Attack
-- Pery
(12868, 4290, 1), -- Race
(12868, 4045, 1), -- Resist Full Magic Attack
-- Gampert
(12869, 4290, 1), -- Race
(12869, 4045, 1), -- Resist Full Magic Attack
-- Gonti
(12870, 4290, 1), -- Race
(12870, 4045, 1), -- Resist Full Magic Attack
-- Baraha
(12871, 4290, 1), -- Race
(12871, 4045, 1), -- Resist Full Magic Attack
-- Vanhal
(12872, 4290, 1), -- Race
(12872, 4045, 1), -- Resist Full Magic Attack
-- Dan
(12873, 4290, 1), -- Race
(12873, 4045, 1), -- Resist Full Magic Attack
-- Briggs
(12874, 4290, 1), -- Race
(12874, 4045, 1), -- Resist Full Magic Attack
-- Stegmann
(12875, 4290, 1), -- Race
(12875, 4045, 1), -- Resist Full Magic Attack
-- Randolph
(12876, 4290, 1), -- Race
(12876, 4045, 1), -- Resist Full Magic Attack
-- Trotter
(12877, 4290, 1), -- Race
(12877, 4045, 1), -- Resist Full Magic Attack
-- Veder
(12878, 4290, 1), -- Race
(12878, 4045, 1), -- Resist Full Magic Attack
-- Danas
(12879, 4290, 1), -- Race
(12879, 4045, 1), -- Resist Full Magic Attack
-- Corey
(12880, 4290, 1), -- Race
(12880, 4045, 1), -- Resist Full Magic Attack
-- Barney
(12881, 4290, 1), -- Race
(12881, 4045, 1), -- Resist Full Magic Attack
-- Klett
(12882, 4290, 1), -- Race
(12882, 4045, 1), -- Resist Full Magic Attack
-- Tairee
(12883, 4290, 1), -- Race
(12883, 4045, 1), -- Resist Full Magic Attack
-- Tanner
(12884, 4290, 1), -- Race
(12884, 4045, 1), -- Resist Full Magic Attack
-- Cresson
(12885, 4290, 1), -- Race
(12885, 4045, 1), -- Resist Full Magic Attack
-- Crothers
(12886, 4290, 1), -- Race
(12886, 4045, 1), -- Resist Full Magic Attack
-- Biggerstaff
(12887, 4290, 1), -- Race
(12887, 4045, 1), -- Resist Full Magic Attack
-- Loken
(12888, 4290, 1), -- Race
(12888, 4045, 1), -- Resist Full Magic Attack
-- Carey
(12889, 4290, 1), -- Race
(12889, 4045, 1), -- Resist Full Magic Attack
-- Dianne
(12890, 4290, 1), -- Race
(12890, 4045, 1), -- Resist Full Magic Attack
-- Crissy
(12891, 4290, 1), -- Race
(12891, 4045, 1), -- Resist Full Magic Attack
-- Albert
(12892, 4290, 1), -- Race
(12892, 4045, 1), -- Resist Full Magic Attack
-- Korgen
(12893, 4290, 1), -- Race
(12893, 4045, 1), -- Resist Full Magic Attack
-- DiMaggio
(12894, 4290, 1), -- Race
(12894, 4045, 1), -- Resist Full Magic Attack
-- Branhillde
(12895, 4290, 1), -- Race
(12895, 4275, 3), -- Sacred Attack Weak Point
(12895, 4278, 1), -- Dark Attack
(12895, 4279, 1), -- Fire Attack Weak Point
-- Millicent
(12896, 4290, 1), -- Race
(12896, 4275, 3), -- Sacred Attack Weak Point
(12896, 4278, 1), -- Dark Attack
(12896, 4279, 1), -- Fire Attack Weak Point
-- Helga
(12897, 4290, 1), -- Race
(12897, 4275, 3), -- Sacred Attack Weak Point
(12897, 4278, 1), -- Dark Attack
(12897, 4279, 1), -- Fire Attack Weak Point
-- Aida
(12898, 4290, 1), -- Race
(12898, 4275, 3), -- Sacred Attack Weak Point
(12898, 4278, 1), -- Dark Attack
(12898, 4279, 1), -- Fire Attack Weak Point
-- Valakas
(12899, 4299, 1), -- Race
(12899, 4679, 1), -- Valakas
(12899, 4045, 1), -- Resist Full Magic Attack
-- Lavasaurus Elder
(12900, 4291, 1), -- Race
(12900, 4045, 1), -- Resist Full Magic Attack
(12900, 4607, 1), -- Magma Attack
-- Guardian of Border
(12901, 4290, 1), -- Race
(12901, 4045, 1), -- Resist Full Magic Attack
(12901, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12902, 4290, 1), -- Race
(12902, 4045, 1), -- Resist Full Magic Attack
(12902, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12903, 4290, 1), -- Race
(12903, 4045, 1), -- Resist Full Magic Attack
(12903, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12904, 4290, 1), -- Race
(12904, 4045, 1), -- Resist Full Magic Attack
(12904, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12905, 4290, 1), -- Race
(12905, 4045, 1), -- Resist Full Magic Attack
(12905, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12906, 4290, 1), -- Race
(12906, 4045, 1), -- Resist Full Magic Attack
(12906, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12907, 4290, 1), -- Race
(12907, 4045, 1), -- Resist Full Magic Attack
(12907, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12908, 4290, 1), -- Race
(12908, 4045, 1), -- Resist Full Magic Attack
(12908, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12909, 4290, 1), -- Race
(12909, 4045, 1), -- Resist Full Magic Attack
(12909, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12910, 4290, 1), -- Race
(12910, 4045, 1), -- Resist Full Magic Attack
(12910, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12911, 4290, 1), -- Race
(12911, 4045, 1), -- Resist Full Magic Attack
(12911, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12912, 4290, 1), -- Race
(12912, 4045, 1), -- Resist Full Magic Attack
(12912, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12913, 4290, 1), -- Race
(12913, 4045, 1), -- Resist Full Magic Attack
(12913, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12914, 4290, 1), -- Race
(12914, 4045, 1), -- Resist Full Magic Attack
(12914, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12915, 4290, 1), -- Race
(12915, 4045, 1), -- Resist Full Magic Attack
(12915, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12916, 4290, 1), -- Race
(12916, 4045, 1), -- Resist Full Magic Attack
(12916, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12917, 4290, 1), -- Race
(12917, 4045, 1), -- Resist Full Magic Attack
(12917, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12918, 4290, 1), -- Race
(12918, 4045, 1), -- Resist Full Magic Attack
(12918, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12919, 4290, 1), -- Race
(12919, 4062, 1), -- Orfen
(12919, 4045, 1), -- Resist Full Magic Attack
(12919, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12920, 4290, 1), -- Race
(12920, 4045, 1), -- Resist Full Magic Attack
(12920, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12921, 4290, 1), -- Race
(12921, 4045, 1), -- Resist Full Magic Attack
(12921, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12922, 4290, 1), -- Race
(12922, 4045, 1), -- Resist Full Magic Attack
(12922, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12923, 4290, 1), -- Race
(12923, 4045, 1), -- Resist Full Magic Attack
(12923, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12924, 4290, 1), -- Race
(12924, 4071, 4), -- Resist Archery
(12924, 4273, 2), -- Resist Dagger
(12924, 4274, 1), -- Blunt Attack Weak Point
(12924, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12925, 4290, 1), -- Race
(12925, 4121, 1), -- Summoned Monster Magic Protection
(12925, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12926, 4290, 1), -- Race
(12926, 4121, 1), -- Summoned Monster Magic Protection
(12926, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12927, 4290, 1), -- Race
(12927, 4121, 1), -- Summoned Monster Magic Protection
(12927, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12928, 4290, 1), -- Race
(12928, 4121, 1), -- Summoned Monster Magic Protection
(12928, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12929, 4290, 1), -- Race
(12929, 4121, 1), -- Summoned Monster Magic Protection
(12929, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12930, 4290, 1), -- Race
(12930, 4121, 1), -- Summoned Monster Magic Protection
(12930, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12931, 4290, 1), -- Race
(12931, 4121, 1), -- Summoned Monster Magic Protection
(12931, 4233, 1), -- Vampiric Attack
(12931, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12932, 4290, 1), -- Race
(12932, 4121, 1), -- Summoned Monster Magic Protection
(12932, 4233, 1), -- Vampiric Attack
(12932, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12933, 4290, 1), -- Race
(12933, 4121, 1), -- Summoned Monster Magic Protection
(12933, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12934, 4290, 1), -- Race
(12934, 4121, 1), -- Summoned Monster Magic Protection
(12934, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12935, 4290, 1), -- Race
(12935, 4121, 1), -- Summoned Monster Magic Protection
(12935, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12936, 4290, 1), -- Race
(12936, 4121, 1), -- Summoned Monster Magic Protection
(12936, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12937, 4290, 1), -- Race
(12937, 4121, 1), -- Summoned Monster Magic Protection
(12937, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12938, 4290, 1), -- Race
(12938, 4121, 1), -- Summoned Monster Magic Protection
(12938, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12939, 4290, 1), -- Race
(12939, 4121, 1), -- Summoned Monster Magic Protection
(12939, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12940, 4290, 1), -- Race
(12940, 4121, 1), -- Summoned Monster Magic Protection
(12940, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12941, 4290, 1), -- Race
(12941, 4121, 1), -- Summoned Monster Magic Protection
(12941, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12942, 4290, 1), -- Race
(12942, 4121, 1), -- Summoned Monster Magic Protection
(12942, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12943, 4290, 1), -- Race
(12943, 4121, 1), -- Summoned Monster Magic Protection
(12943, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12944, 4290, 1), -- Race
(12944, 4121, 1), -- Summoned Monster Magic Protection
(12944, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12945, 4290, 1), -- Race
(12945, 4121, 1), -- Summoned Monster Magic Protection
(12945, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12946, 4290, 1), -- Race
(12946, 4121, 1), -- Summoned Monster Magic Protection
(12946, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12947, 4290, 1), -- Race
(12947, 4045, 1), -- Resist Full Magic Attack
(12947, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12948, 4290, 1), -- Race
(12948, 4045, 1), -- Resist Full Magic Attack
(12948, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12949, 4290, 1), -- Race
(12949, 4045, 1), -- Resist Full Magic Attack
(12949, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12950, 4290, 1), -- Race
(12950, 4045, 1), -- Resist Full Magic Attack
(12950, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12951, 4290, 1), -- Race
(12951, 4045, 1), -- Resist Full Magic Attack
(12951, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12952, 4290, 1), -- Race
(12952, 4045, 1), -- Resist Full Magic Attack
(12952, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12953, 4290, 1), -- Race
(12953, 4045, 1), -- Resist Full Magic Attack
(12953, 4390, 1), -- NPC Abnormal Immunity
-- Guardian of Border
(12954, 4290, 1), -- Race
(12954, 4045, 1), -- Resist Full Magic Attack
(12954, 4390, 1), -- NPC Abnormal Immunity
-- Halisha's Officer
(12955, 4290, 1), -- Race
(12955, 4307, 1), -- Strong Type
(12955, 4275, 3), -- Sacred Attack Weak Point
(12955, 4278, 1), -- Dark Attack
(12955, 4285, 3), -- Resist Sleep
(12955, 4287, 3), -- Resist Hold
(12955, 4572, 9), -- NPC Triple Sonic Slash
(12955, 4589, 9), -- Decrease Speed
(12955, 4318, 1), -- Ultimate Buff
(12955, 4340, 1), -- Ultimate Buff, 2nd
(12955, 4341, 1), -- Ultimate Buff, 3rd
-- Halisha's Officer
(12956, 4290, 1), -- Race
(12956, 4307, 1), -- Strong Type
(12956, 4275, 3), -- Sacred Attack Weak Point
(12956, 4278, 1), -- Dark Attack
(12956, 4285, 3), -- Resist Sleep
(12956, 4287, 3), -- Resist Hold
(12956, 4572, 9), -- NPC Triple Sonic Slash
(12956, 4589, 9), -- Decrease Speed
(12956, 4694, 1), -- Ultimate Debuff
(12956, 4695, 1), -- Ultimate Debuff
(12956, 4696, 1), -- Ultimate Debuff
-- Halisha's Officer
(12957, 4290, 1), -- Race
(12957, 4307, 1), -- Strong Type
(12957, 4275, 3), -- Sacred Attack Weak Point
(12957, 4278, 1), -- Dark Attack
(12957, 4285, 5), -- Resist Sleep
(12957, 4287, 5), -- Resist Hold
(12957, 4671, 1), -- AV - Teleport
(12957, 4572, 9), -- NPC Triple Sonic Slash
(12957, 4589, 9), -- Decrease Speed
(12957, 4618, 1), -- NPC Cancel PC Target
(12957, 4657, 9), -- Hold
-- Halisha's Officer
(12958, 4290, 1), -- Race
(12958, 4307, 1), -- Strong Type
(12958, 4275, 3), -- Sacred Attack Weak Point
(12958, 4278, 1), -- Dark Attack
(12958, 4285, 3), -- Resist Sleep
(12958, 4287, 3), -- Resist Hold
(12958, 4572, 9), -- NPC Triple Sonic Slash
(12958, 4589, 9), -- Decrease Speed
(12958, 4318, 1), -- Ultimate Buff
(12958, 4340, 1), -- Ultimate Buff, 2nd
(12958, 4341, 1), -- Ultimate Buff, 3rd
-- Halisha's Officer
(12959, 4290, 1), -- Race
(12959, 4307, 1), -- Strong Type
(12959, 4275, 3), -- Sacred Attack Weak Point
(12959, 4278, 1), -- Dark Attack
(12959, 4285, 3), -- Resist Sleep
(12959, 4287, 3), -- Resist Hold
(12959, 4572, 9), -- NPC Triple Sonic Slash
(12959, 4589, 9), -- Decrease Speed
(12959, 4694, 1), -- Ultimate Debuff
(12959, 4695, 1), -- Ultimate Debuff
(12959, 4696, 1), -- Ultimate Debuff
-- Halisha's Officer
(12960, 4290, 1), -- Race
(12960, 4307, 1), -- Strong Type
(12960, 4275, 3), -- Sacred Attack Weak Point
(12960, 4278, 1), -- Dark Attack
(12960, 4285, 5), -- Resist Sleep
(12960, 4287, 5), -- Resist Hold
(12960, 4671, 1), -- AV - Teleport
(12960, 4572, 9), -- NPC Triple Sonic Slash
(12960, 4589, 9), -- Decrease Speed
(12960, 4618, 1), -- NPC Cancel PC Target
(12960, 4657, 9), -- Hold
-- Halisha's Officer
(12961, 4290, 1), -- Race
(12961, 4307, 1), -- Strong Type
(12961, 4275, 3), -- Sacred Attack Weak Point
(12961, 4278, 1), -- Dark Attack
(12961, 4285, 3), -- Resist Sleep
(12961, 4287, 3), -- Resist Hold
(12961, 4572, 9), -- NPC Triple Sonic Slash
(12961, 4589, 9), -- Decrease Speed
(12961, 4318, 1), -- Ultimate Buff
(12961, 4340, 1), -- Ultimate Buff, 2nd
(12961, 4341, 1), -- Ultimate Buff, 3rd
-- Halisha's Officer
(12962, 4290, 1), -- Race
(12962, 4307, 1), -- Strong Type
(12962, 4275, 3), -- Sacred Attack Weak Point
(12962, 4278, 1), -- Dark Attack
(12962, 4285, 3), -- Resist Sleep
(12962, 4287, 3), -- Resist Hold
(12962, 4572, 9), -- NPC Triple Sonic Slash
(12962, 4589, 9), -- Decrease Speed
(12962, 4694, 1), -- Ultimate Debuff
(12962, 4695, 1), -- Ultimate Debuff
(12962, 4696, 1), -- Ultimate Debuff
-- Halisha's Officer
(12963, 4290, 1), -- Race
(12963, 4307, 1), -- Strong Type
(12963, 4275, 3), -- Sacred Attack Weak Point
(12963, 4278, 1), -- Dark Attack
(12963, 4285, 5), -- Resist Sleep
(12963, 4287, 5), -- Resist Hold
(12963, 4671, 1), -- AV - Teleport
(12963, 4572, 9), -- NPC Triple Sonic Slash
(12963, 4589, 9), -- Decrease Speed
(12963, 4618, 1), -- NPC Cancel PC Target
(12963, 4657, 9), -- Hold
-- Halisha's Officer
(12964, 4290, 1), -- Race
(12964, 4307, 1), -- Strong Type
(12964, 4275, 3), -- Sacred Attack Weak Point
(12964, 4278, 1), -- Dark Attack
(12964, 4285, 3), -- Resist Sleep
(12964, 4287, 3), -- Resist Hold
(12964, 4572, 9), -- NPC Triple Sonic Slash
(12964, 4589, 9), -- Decrease Speed
(12964, 4318, 1), -- Ultimate Buff
(12964, 4340, 1), -- Ultimate Buff, 2nd
(12964, 4341, 1), -- Ultimate Buff, 3rd
-- Halisha's Officer
(12965, 4290, 1), -- Race
(12965, 4307, 1), -- Strong Type
(12965, 4275, 3), -- Sacred Attack Weak Point
(12965, 4278, 1), -- Dark Attack
(12965, 4285, 3), -- Resist Sleep
(12965, 4287, 3), -- Resist Hold
(12965, 4572, 9), -- NPC Triple Sonic Slash
(12965, 4589, 9), -- Decrease Speed
(12965, 4694, 1), -- Ultimate Debuff
(12965, 4695, 1), -- Ultimate Debuff
(12965, 4696, 1), -- Ultimate Debuff
-- Halisha's Officer
(12966, 4290, 1), -- Race
(12966, 4307, 1), -- Strong Type
(12966, 4275, 3), -- Sacred Attack Weak Point
(12966, 4278, 1), -- Dark Attack
(12966, 4285, 5), -- Resist Sleep
(12966, 4287, 5), -- Resist Hold
(12966, 4671, 1), -- AV - Teleport
(12966, 4572, 9), -- NPC Triple Sonic Slash
(12966, 4589, 9), -- Decrease Speed
(12966, 4618, 1), -- NPC Cancel PC Target
(12966, 4657, 9), -- Hold
-- Imperial Healer
(12967, 4301, 1), -- Race
(12967, 4307, 1), -- Strong Type
(12967, 4116, 7), -- Resist M. Atk.
(12967, 4071, 3), -- Resist Archery
(12967, 4285, 3), -- Resist Sleep
(12967, 4569, 9), -- NPC AE Solar Flare - Magic
(12967, 4640, 9), -- Sleep
(12967, 4613, 9), -- NPC Clan Heal
-- Imperial Mosaic
(12968, 4301, 1), -- Race
(12968, 4307, 1), -- Strong Type
(12968, 4084, 7), -- Resist Physical Attack
(12968, 4582, 9), -- Poison
-- Imperial Mosaic
(12969, 4301, 1), -- Race
(12969, 4307, 1), -- Strong Type
(12969, 4285, 3), -- Resist Sleep
(12969, 4573, 9), -- NPC Sonic Blaster
-- Imperial Mosaic
(12970, 4301, 1), -- Race
(12970, 4307, 1), -- Strong Type
(12970, 4116, 7), -- Resist M. Atk.
(12970, 4573, 9), -- NPC Sonic Blaster
(12970, 4032, 9), -- NPC Strike
-- Imperial Mosaic
(12971, 4301, 1), -- Race
(12971, 4307, 1), -- Strong Type
(12971, 4116, 7), -- Resist M. Atk.
(12971, 4582, 9), -- Poison
(12971, 4091, 1), -- NPC Ogre Stun
-- Imperial Mosaic
(12972, 4301, 1), -- Race
(12972, 4307, 1), -- Strong Type
(12972, 4285, 3), -- Resist Sleep
(12972, 4563, 9), -- NPC Solar Flare - Magic
(12972, 4160, 9), -- NPC Aura Burn - Magic
-- Imperial Mosaic
(12973, 4301, 1), -- Race
(12973, 4307, 1), -- Strong Type
(12973, 4084, 7), -- Resist Physical Attack
(12973, 4563, 9), -- NPC Solar Flare - Magic
(12973, 4160, 9), -- NPC Aura Burn - Magic
(12973, 4609, 4), -- NPC Clan Buff - Vampiric Rage
-- Imperial Mosaic
(12974, 4301, 1), -- Race
(12974, 4307, 1), -- Strong Type
(12974, 4084, 7), -- Resist Physical Attack
(12974, 4563, 9), -- NPC Solar Flare - Magic
(12974, 4160, 9), -- NPC Aura Burn - Magic
(12974, 4587, 9), -- Decrease P.Atk
-- Imperial Mosaic
(12975, 4301, 1), -- Race
(12975, 4307, 1), -- Strong Type
(12975, 4084, 7), -- Resist Physical Attack
(12975, 4033, 9), -- NPC Burn
(12975, 4160, 9), -- NPC Aura Burn - Magic
-- Beetle of Grave
(12976, 4301, 1), -- Race
(12976, 4305, 1), -- Strong Type
(12976, 4285, 3), -- Resist Sleep
(12976, 4287, 3), -- Resist Hold
(12976, 4572, 9), -- NPC Triple Sonic Slash
(12976, 4568, 9), -- NPC AE Solar Flare
(12976, 4576, 3), -- NPC Clan Buff - Damage Shield
-- Beetle of Grave
(12977, 4301, 1), -- Race
(12977, 4305, 1), -- Strong Type
(12977, 4285, 3), -- Resist Sleep
(12977, 4287, 3), -- Resist Hold
(12977, 4572, 9), -- NPC Triple Sonic Slash
(12977, 4568, 9), -- NPC AE Solar Flare
(12977, 4576, 3), -- NPC Clan Buff - Damage Shield
-- Beetle of Grave
(12978, 4301, 1), -- Race
(12978, 4305, 1), -- Strong Type
(12978, 4285, 3), -- Resist Sleep
(12978, 4287, 3), -- Resist Hold
(12978, 4572, 9), -- NPC Triple Sonic Slash
(12978, 4568, 9), -- NPC AE Solar Flare
(12978, 4576, 3), -- NPC Clan Buff - Damage Shield
-- Beetle of Grave
(12979, 4301, 1), -- Race
(12979, 4305, 1), -- Strong Type
(12979, 4285, 3), -- Resist Sleep
(12979, 4287, 3), -- Resist Hold
(12979, 4572, 9), -- NPC Triple Sonic Slash
(12979, 4568, 9), -- NPC AE Solar Flare
(12979, 4576, 3), -- NPC Clan Buff - Damage Shield
-- Beetle of Grave
(12980, 4301, 1), -- Race
(12980, 4305, 1), -- Strong Type
(12980, 4285, 3), -- Resist Sleep
(12980, 4155, 9), -- NPC Twister - Magic
(12980, 4160, 9), -- NPC Aura Burn - Magic
(12980, 4031, 3), -- Enhance P. Def.
-- Beetle of Grave
(12981, 4301, 1), -- Race
(12981, 4305, 1), -- Strong Type
(12981, 4285, 3), -- Resist Sleep
(12981, 4573, 9), -- NPC Sonic Blaster
(12981, 4032, 9), -- NPC Strike
-- Beetle of Grave
(12982, 4301, 1), -- Race
(12982, 4305, 1), -- Strong Type
(12982, 4285, 3), -- Resist Sleep
(12982, 4155, 9), -- NPC Twister - Magic
(12982, 4160, 9), -- NPC Aura Burn - Magic
-- Beetle of Grave
(12983, 4301, 1), -- Race
(12983, 4305, 1), -- Strong Type
(12983, 4285, 3), -- Resist Sleep
(12983, 4581, 9), -- Hold
(12983, 4074, 2), -- NPC Haste
-- Beetle of Grave
(12984, 4301, 1), -- Race
(12984, 4305, 1), -- Strong Type
(12984, 4071, 4), -- Resist Archery
(12984, 4285, 5), -- Resist Sleep
(12984, 4287, 5), -- Resist Hold
(12984, 4614, 9), -- NPC Death Bomb
-- Victim
(12985, 4290, 1), -- Race
(12985, 4697, 9), -- NPC Monster Hate
-- Victim
(12986, 4290, 1), -- Race
(12986, 4697, 9), -- NPC Monster Hate
-- Victim
(12987, 4290, 1), -- Race
(12987, 4697, 9), -- NPC Monster Hate
-- Victim
(12988, 4290, 1), -- Race
(12988, 4697, 9), -- NPC Monster Hate
-- Victim
(12989, 4290, 1), -- Race
(12989, 4697, 9), -- NPC Monster Hate
-- Victim
(12990, 4290, 1), -- Race
(12990, 4697, 9), -- NPC Monster Hate
-- Victim
(12991, 4290, 1), -- Race
(12991, 4697, 9), -- NPC Monster Hate
-- Victim
(12992, 4290, 1), -- Race
(12992, 4697, 9), -- NPC Monster Hate
-- Executioner of Halisha
(12993, 4298, 1), -- Race
(12993, 4307, 1), -- Strong Type
(12993, 4278, 1), -- Dark Attack
(12993, 4333, 3), -- Resist Dark Attack
(12993, 4084, 10), -- Resist Physical Attack
(12993, 4116, 9), -- Resist M. Atk.
(12993, 4582, 9), -- Poison
(12993, 4318, 1), -- Ultimate Buff
(12993, 4621, 9), -- NPC AE - 80% HP Drain
-- Executioner of Halisha
(12994, 4298, 1), -- Race
(12994, 4307, 1), -- Strong Type
(12994, 4278, 1), -- Dark Attack
(12994, 4333, 3), -- Resist Dark Attack
(12994, 4084, 9), -- Resist Physical Attack
(12994, 4116, 10), -- Resist M. Atk.
(12994, 4582, 9), -- Poison
(12994, 4318, 1), -- Ultimate Buff
(12994, 4657, 9), -- Hold
-- Executioner of Halisha
(12995, 4298, 1), -- Race
(12995, 4307, 1), -- Strong Type
(12995, 4278, 1), -- Dark Attack
(12995, 4333, 3), -- Resist Dark Attack
(12995, 4084, 10), -- Resist Physical Attack
(12995, 4116, 9), -- Resist M. Atk.
(12995, 4582, 9), -- Poison
(12995, 4318, 1), -- Ultimate Buff
(12995, 4621, 9), -- NPC AE - 80% HP Drain
-- Executioner of Halisha
(12996, 4298, 1), -- Race
(12996, 4307, 1), -- Strong Type
(12996, 4278, 1), -- Dark Attack
(12996, 4333, 3), -- Resist Dark Attack
(12996, 4084, 9), -- Resist Physical Attack
(12996, 4116, 10), -- Resist M. Atk.
(12996, 4582, 9), -- Poison
(12996, 4318, 1), -- Ultimate Buff
(12996, 4657, 9), -- Hold
-- Executioner of Halisha
(12997, 4298, 1), -- Race
(12997, 4307, 1), -- Strong Type
(12997, 4278, 1), -- Dark Attack
(12997, 4333, 3), -- Resist Dark Attack
(12997, 4084, 10), -- Resist Physical Attack
(12997, 4116, 9), -- Resist M. Atk.
(12997, 4582, 9), -- Poison
(12997, 4318, 1), -- Ultimate Buff
(12997, 4621, 9), -- NPC AE - 80% HP Drain
-- Executioner of Halisha
(12998, 4298, 1), -- Race
(12998, 4307, 1), -- Strong Type
(12998, 4278, 1), -- Dark Attack
(12998, 4333, 3), -- Resist Dark Attack
(12998, 4084, 9), -- Resist Physical Attack
(12998, 4116, 10), -- Resist M. Atk.
(12998, 4582, 9), -- Poison
(12998, 4318, 1), -- Ultimate Buff
(12998, 4657, 9), -- Hold
-- Executioner of Halisha
(12999, 4298, 1), -- Race
(12999, 4307, 1), -- Strong Type
(12999, 4278, 1), -- Dark Attack
(12999, 4333, 3), -- Resist Dark Attack
(12999, 4084, 10), -- Resist Physical Attack
(12999, 4116, 9), -- Resist M. Atk.
(12999, 4582, 9), -- Poison
(12999, 4318, 1), -- Ultimate Buff
(12999, 4621, 9); -- NPC AE - 80% HP Drain

INSERT INTO npcskills VALUES
-- Executioner of Halisha
(13000, 4298, 1), -- Race
(13000, 4307, 1), -- Strong Type
(13000, 4278, 1), -- Dark Attack
(13000, 4333, 3), -- Resist Dark Attack
(13000, 4084, 9), -- Resist Physical Attack
(13000, 4116, 10), -- Resist M. Atk.
(13000, 4582, 9), -- Poison
(13000, 4318, 1), -- Ultimate Buff
(13000, 4657, 9), -- Hold
-- Imperial Guard
(13001, 4290, 1), -- Race
(13001, 4307, 1), -- Strong Type
(13001, 4275, 3), -- Sacred Attack Weak Point
(13001, 4278, 1), -- Dark Attack
(13001, 4572, 9), -- NPC Triple Sonic Slash
-- Imperial Guard
(13002, 4290, 1), -- Race
(13002, 4307, 1), -- Strong Type
(13002, 4275, 3), -- Sacred Attack Weak Point
(13002, 4278, 1), -- Dark Attack
(13002, 4285, 3), -- Resist Sleep
(13002, 4573, 9), -- NPC Sonic Blaster
-- Imperial Guard
(13003, 4290, 1), -- Race
(13003, 4307, 1), -- Strong Type
(13003, 4275, 3), -- Sacred Attack Weak Point
(13003, 4278, 1), -- Dark Attack
(13003, 4573, 9), -- NPC Sonic Blaster
(13003, 4572, 9), -- NPC Triple Sonic Slash
-- Imperial Guard
(13004, 4290, 1), -- Race
(13004, 4307, 1), -- Strong Type
(13004, 4275, 3), -- Sacred Attack Weak Point
(13004, 4278, 1), -- Dark Attack
(13004, 4574, 9), -- NPC Sonic Storm
(13004, 4091, 1), -- NPC Ogre Stun
-- Imperial Guard
(13005, 4290, 1), -- Race
(13005, 4307, 1), -- Strong Type
(13005, 4275, 3), -- Sacred Attack Weak Point
(13005, 4278, 1), -- Dark Attack
(13005, 4285, 3), -- Resist Sleep
(13005, 4603, 9), -- Decrease P.Atk
(13005, 4561, 9), -- NPC Fire Burn - Magic
-- Imperial Guard
(13006, 4290, 1), -- Race
(13006, 4307, 1), -- Strong Type
(13006, 4275, 3), -- Sacred Attack Weak Point
(13006, 4278, 1), -- Dark Attack
(13006, 4603, 9), -- Decrease P.Atk
(13006, 4561, 9), -- NPC Fire Burn - Magic
(13006, 4609, 4), -- NPC Clan Buff - Vampiric Rage
-- Imperial Guard
(13007, 4290, 1), -- Race
(13007, 4307, 1), -- Strong Type
(13007, 4275, 3), -- Sacred Attack Weak Point
(13007, 4278, 1), -- Dark Attack
(13007, 4589, 9), -- Decrease Speed
(13007, 4561, 9), -- NPC Fire Burn - Magic
-- Halisha's Foreman
(13008, 4298, 1), -- Race
(13008, 4307, 1), -- Strong Type
(13008, 4278, 1), -- Dark Attack
(13008, 4333, 3), -- Resist Dark Attack
(13008, 4285, 3), -- Resist Sleep
(13008, 4287, 3), -- Resist Hold
(13008, 4073, 9), -- Shock
(13008, 4565, 9), -- NPC Eruption
(13008, 4601, 3), -- NPC Clan Buff - Acumen Focus
-- Halisha's Foreman
(13009, 4298, 1), -- Race
(13009, 4307, 1), -- Strong Type
(13009, 4278, 1), -- Dark Attack
(13009, 4333, 3), -- Resist Dark Attack
(13009, 4285, 3), -- Resist Sleep
(13009, 4287, 3), -- Resist Hold
(13009, 4073, 9), -- Shock
(13009, 4589, 9), -- Decrease Speed
(13009, 4694, 1), -- Ultimate Debuff
(13009, 4695, 1), -- Ultimate Debuff
(13009, 4696, 1), -- Ultimate Debuff
-- Halisha's Foreman
(13010, 4298, 1), -- Race
(13010, 4307, 1), -- Strong Type
(13010, 4278, 1), -- Dark Attack
(13010, 4333, 3), -- Resist Dark Attack
(13010, 4285, 5), -- Resist Sleep
(13010, 4287, 5), -- Resist Hold
(13010, 4073, 9), -- Shock
(13010, 4589, 9), -- Decrease Speed
(13010, 4618, 1), -- NPC Cancel PC Target
(13010, 4657, 9), -- Hold
-- Halisha's Foreman
(13011, 4298, 1), -- Race
(13011, 4307, 1), -- Strong Type
(13011, 4278, 1), -- Dark Attack
(13011, 4333, 3), -- Resist Dark Attack
(13011, 4285, 3), -- Resist Sleep
(13011, 4287, 3), -- Resist Hold
(13011, 4073, 9), -- Shock
(13011, 4565, 9), -- NPC Eruption
(13011, 4601, 3), -- NPC Clan Buff - Acumen Focus
-- Halisha's Foreman
(13012, 4298, 1), -- Race
(13012, 4307, 1), -- Strong Type
(13012, 4278, 1), -- Dark Attack
(13012, 4333, 3), -- Resist Dark Attack
(13012, 4285, 3), -- Resist Sleep
(13012, 4287, 3), -- Resist Hold
(13012, 4073, 9), -- Shock
(13012, 4589, 9), -- Decrease Speed
(13012, 4694, 1), -- Ultimate Debuff
(13012, 4695, 1), -- Ultimate Debuff
(13012, 4696, 1), -- Ultimate Debuff
-- Halisha's Foreman
(13013, 4298, 1), -- Race
(13013, 4307, 1), -- Strong Type
(13013, 4278, 1), -- Dark Attack
(13013, 4333, 3), -- Resist Dark Attack
(13013, 4285, 5), -- Resist Sleep
(13013, 4287, 5), -- Resist Hold
(13013, 4073, 9), -- Shock
(13013, 4589, 9), -- Decrease Speed
(13013, 4618, 1), -- NPC Cancel PC Target
(13013, 4657, 9), -- Hold
-- Halisha's Foreman
(13014, 4298, 1), -- Race
(13014, 4307, 1), -- Strong Type
(13014, 4278, 1), -- Dark Attack
(13014, 4333, 3), -- Resist Dark Attack
(13014, 4285, 3), -- Resist Sleep
(13014, 4287, 3), -- Resist Hold
(13014, 4073, 9), -- Shock
(13014, 4565, 9), -- NPC Eruption
(13014, 4601, 3), -- NPC Clan Buff - Acumen Focus
-- Halisha's Foreman
(13015, 4298, 1), -- Race
(13015, 4307, 1), -- Strong Type
(13015, 4278, 1), -- Dark Attack
(13015, 4333, 3), -- Resist Dark Attack
(13015, 4285, 3), -- Resist Sleep
(13015, 4287, 3), -- Resist Hold
(13015, 4073, 9), -- Shock
(13015, 4589, 9), -- Decrease Speed
(13015, 4694, 1), -- Ultimate Debuff
(13015, 4695, 1), -- Ultimate Debuff
(13015, 4696, 1), -- Ultimate Debuff
-- Halisha's Foreman
(13016, 4298, 1), -- Race
(13016, 4307, 1), -- Strong Type
(13016, 4278, 1), -- Dark Attack
(13016, 4333, 3), -- Resist Dark Attack
(13016, 4285, 5), -- Resist Sleep
(13016, 4287, 5), -- Resist Hold
(13016, 4073, 9), -- Shock
(13016, 4589, 9), -- Decrease Speed
(13016, 4618, 1), -- NPC Cancel PC Target
(13016, 4657, 9), -- Hold
-- Halisha's Foreman
(13017, 4298, 1), -- Race
(13017, 4307, 1), -- Strong Type
(13017, 4278, 1), -- Dark Attack
(13017, 4333, 3), -- Resist Dark Attack
(13017, 4285, 3), -- Resist Sleep
(13017, 4287, 3), -- Resist Hold
(13017, 4073, 9), -- Shock
(13017, 4565, 9), -- NPC Eruption
(13017, 4601, 3), -- NPC Clan Buff - Acumen Focus
-- Halisha's Foreman
(13018, 4298, 1), -- Race
(13018, 4307, 1), -- Strong Type
(13018, 4278, 1), -- Dark Attack
(13018, 4333, 3), -- Resist Dark Attack
(13018, 4285, 3), -- Resist Sleep
(13018, 4287, 3), -- Resist Hold
(13018, 4073, 9), -- Shock
(13018, 4589, 9), -- Decrease Speed
(13018, 4694, 1), -- Ultimate Debuff
(13018, 4695, 1), -- Ultimate Debuff
(13018, 4696, 1), -- Ultimate Debuff
-- Halisha's Foreman
(13019, 4298, 1), -- Race
(13019, 4307, 1), -- Strong Type
(13019, 4278, 1), -- Dark Attack
(13019, 4333, 3), -- Resist Dark Attack
(13019, 4285, 5), -- Resist Sleep
(13019, 4287, 5), -- Resist Hold
(13019, 4073, 9), -- Shock
(13019, 4589, 9), -- Decrease Speed
(13019, 4618, 1), -- NPC Cancel PC Target
(13019, 4657, 9), -- Hold
-- Evil Astrologer
(13020, 4298, 1), -- Race
(13020, 4307, 1), -- Strong Type
(13020, 4116, 7), -- Resist M. Atk.
(13020, 4071, 3), -- Resist Archery
(13020, 4285, 3), -- Resist Sleep
(13020, 4033, 9), -- NPC Burn
(13020, 4036, 9), -- Poison
(13020, 4613, 9), -- NPC Clan Heal
-- Evil Astrologer
(13021, 4298, 1), -- Race
(13021, 4307, 1), -- Strong Type
(13021, 4116, 7), -- Resist M. Atk.
(13021, 4071, 3), -- Resist Archery
(13021, 4285, 3), -- Resist Sleep
(13021, 4033, 9), -- NPC Burn
(13021, 4597, 9), -- Bleed
(13021, 4657, 9), -- Hold
(13021, 4613, 9), -- NPC Clan Heal
-- Knight of Darkness
(13022, 4290, 1), -- Race
(13022, 4307, 1), -- Strong Type
(13022, 4275, 3), -- Sacred Attack Weak Point
(13022, 4278, 1), -- Dark Attack
(13022, 4084, 7), -- Resist Physical Attack
(13022, 4582, 9), -- Poison
-- Knight of Darkness
(13023, 4290, 1), -- Race
(13023, 4307, 1), -- Strong Type
(13023, 4275, 3), -- Sacred Attack Weak Point
(13023, 4278, 1), -- Dark Attack
(13023, 4573, 9), -- NPC Sonic Blaster
-- Knight of Darkness
(13024, 4290, 1), -- Race
(13024, 4307, 1), -- Strong Type
(13024, 4275, 3), -- Sacred Attack Weak Point
(13024, 4278, 1), -- Dark Attack
(13024, 4116, 7), -- Resist M. Atk.
(13024, 4573, 9), -- NPC Sonic Blaster
(13024, 4032, 9), -- NPC Strike
-- Knight of Darkness
(13025, 4290, 1), -- Race
(13025, 4307, 1), -- Strong Type
(13025, 4275, 3), -- Sacred Attack Weak Point
(13025, 4278, 1), -- Dark Attack
(13025, 4116, 7), -- Resist M. Atk.
(13025, 4582, 9), -- Poison
(13025, 4091, 1), -- NPC Ogre Stun
-- Witch of dust
(13026, 4298, 1), -- Race
(13026, 4307, 1), -- Strong Type
(13026, 4278, 1), -- Dark Attack
(13026, 4333, 3), -- Resist Dark Attack
(13026, 4563, 9), -- NPC Solar Flare - Magic
(13026, 4160, 9), -- NPC Aura Burn - Magic
-- Witch of dust
(13027, 4298, 1), -- Race
(13027, 4307, 1), -- Strong Type
(13027, 4278, 1), -- Dark Attack
(13027, 4333, 3), -- Resist Dark Attack
(13027, 4084, 7), -- Resist Physical Attack
(13027, 4563, 9), -- NPC Solar Flare - Magic
(13027, 4160, 9), -- NPC Aura Burn - Magic
(13027, 4609, 4), -- NPC Clan Buff - Vampiric Rage
-- Witch of dust
(13028, 4298, 1), -- Race
(13028, 4307, 1), -- Strong Type
(13028, 4278, 1), -- Dark Attack
(13028, 4333, 3), -- Resist Dark Attack
(13028, 4084, 7), -- Resist Physical Attack
(13028, 4563, 9), -- NPC Solar Flare - Magic
(13028, 4160, 9), -- NPC Aura Burn - Magic
(13028, 4587, 9), -- Decrease P.Atk
-- Witch of dust
(13029, 4298, 1), -- Race
(13029, 4307, 1), -- Strong Type
(13029, 4278, 1), -- Dark Attack
(13029, 4333, 3), -- Resist Dark Attack
(13029, 4084, 7), -- Resist Physical Attack
(13029, 4033, 9), -- NPC Burn
(13029, 4160, 9), -- NPC Aura Burn - Magic
-- Signet of Emperor
(13030, 4301, 1), -- Race
(13030, 4307, 1), -- Strong Type
(13030, 4071, 4), -- Resist Archery
(13030, 4285, 5), -- Resist Sleep
(13030, 4287, 5), -- Resist Hold
(13030, 4614, 9), -- NPC Death Bomb
-- Charm of Corner
(13031, 4291, 1), -- Race
(13031, 4307, 1), -- Strong Type
(13031, 4084, 9), -- Resist Physical Attack
(13031, 4116, 9), -- Resist M. Atk.
(13031, 4277, 6), -- Resist Poison
(13031, 4284, 6), -- Resist Bleeding
(13031, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13032, 4291, 1), -- Race
(13032, 4307, 1), -- Strong Type
(13032, 4084, 9), -- Resist Physical Attack
(13032, 4116, 9), -- Resist M. Atk.
(13032, 4277, 6), -- Resist Poison
(13032, 4284, 6), -- Resist Bleeding
(13032, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13033, 4291, 1), -- Race
(13033, 4307, 1), -- Strong Type
(13033, 4084, 9), -- Resist Physical Attack
(13033, 4116, 9), -- Resist M. Atk.
(13033, 4277, 6), -- Resist Poison
(13033, 4284, 6), -- Resist Bleeding
(13033, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13034, 4291, 1), -- Race
(13034, 4307, 1), -- Strong Type
(13034, 4084, 9), -- Resist Physical Attack
(13034, 4116, 9), -- Resist M. Atk.
(13034, 4277, 6), -- Resist Poison
(13034, 4284, 6), -- Resist Bleeding
(13034, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13035, 4291, 1), -- Race
(13035, 4307, 1), -- Strong Type
(13035, 4084, 9), -- Resist Physical Attack
(13035, 4116, 9), -- Resist M. Atk.
(13035, 4277, 6), -- Resist Poison
(13035, 4284, 6), -- Resist Bleeding
(13035, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13036, 4291, 1), -- Race
(13036, 4307, 1), -- Strong Type
(13036, 4084, 9), -- Resist Physical Attack
(13036, 4116, 9), -- Resist M. Atk.
(13036, 4277, 6), -- Resist Poison
(13036, 4284, 6), -- Resist Bleeding
(13036, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13037, 4291, 1), -- Race
(13037, 4307, 1), -- Strong Type
(13037, 4084, 9), -- Resist Physical Attack
(13037, 4116, 9), -- Resist M. Atk.
(13037, 4277, 6), -- Resist Poison
(13037, 4284, 6), -- Resist Bleeding
(13037, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13038, 4291, 1), -- Race
(13038, 4307, 1), -- Strong Type
(13038, 4084, 9), -- Resist Physical Attack
(13038, 4116, 9), -- Resist M. Atk.
(13038, 4277, 6), -- Resist Poison
(13038, 4284, 6), -- Resist Bleeding
(13038, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13039, 4291, 1), -- Race
(13039, 4307, 1), -- Strong Type
(13039, 4084, 9), -- Resist Physical Attack
(13039, 4116, 9), -- Resist M. Atk.
(13039, 4277, 6), -- Resist Poison
(13039, 4284, 6), -- Resist Bleeding
(13039, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13040, 4291, 1), -- Race
(13040, 4307, 1), -- Strong Type
(13040, 4084, 9), -- Resist Physical Attack
(13040, 4116, 9), -- Resist M. Atk.
(13040, 4277, 6), -- Resist Poison
(13040, 4284, 6), -- Resist Bleeding
(13040, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13041, 4291, 1), -- Race
(13041, 4307, 1), -- Strong Type
(13041, 4084, 9), -- Resist Physical Attack
(13041, 4116, 9), -- Resist M. Atk.
(13041, 4277, 6), -- Resist Poison
(13041, 4284, 6), -- Resist Bleeding
(13041, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13042, 4291, 1), -- Race
(13042, 4307, 1), -- Strong Type
(13042, 4084, 9), -- Resist Physical Attack
(13042, 4116, 9), -- Resist M. Atk.
(13042, 4277, 6), -- Resist Poison
(13042, 4284, 6), -- Resist Bleeding
(13042, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13043, 4291, 1), -- Race
(13043, 4307, 1), -- Strong Type
(13043, 4084, 9), -- Resist Physical Attack
(13043, 4116, 9), -- Resist M. Atk.
(13043, 4277, 6), -- Resist Poison
(13043, 4284, 6), -- Resist Bleeding
(13043, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13044, 4291, 1), -- Race
(13044, 4307, 1), -- Strong Type
(13044, 4084, 9), -- Resist Physical Attack
(13044, 4116, 9), -- Resist M. Atk.
(13044, 4277, 6), -- Resist Poison
(13044, 4284, 6), -- Resist Bleeding
(13044, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13045, 4291, 1), -- Race
(13045, 4307, 1), -- Strong Type
(13045, 4084, 9), -- Resist Physical Attack
(13045, 4116, 9), -- Resist M. Atk.
(13045, 4277, 6), -- Resist Poison
(13045, 4284, 6), -- Resist Bleeding
(13045, 4628, 1), -- Mysterious Aura
-- Charm of Corner
(13046, 4291, 1), -- Race
(13046, 4307, 1), -- Strong Type
(13046, 4084, 9), -- Resist Physical Attack
(13046, 4116, 9), -- Resist M. Atk.
(13046, 4277, 6), -- Resist Poison
(13046, 4284, 6), -- Resist Bleeding
(13046, 4628, 1), -- Mysterious Aura
-- Archon of Halisha
(13047, 4298, 1), -- Race
(13047, 4307, 1), -- Strong Type
(13047, 4278, 1), -- Dark Attack
(13047, 4333, 3), -- Resist Dark Attack
(13047, 4285, 3), -- Resist Sleep
(13047, 4287, 3), -- Resist Hold
(13047, 4084, 9), -- Resist Physical Attack
(13047, 4116, 9), -- Resist M. Atk.
(13047, 4605, 9), -- Fire Weakness
(13047, 4589, 9), -- Decrease Speed
(13047, 4639, 3), -- NPC Clan Buff - Acumen Empower Berserk
-- Archon of Halisha
(13048, 4298, 1), -- Race
(13048, 4307, 1), -- Strong Type
(13048, 4278, 1), -- Dark Attack
(13048, 4333, 3), -- Resist Dark Attack
(13048, 4285, 3), -- Resist Sleep
(13048, 4287, 3), -- Resist Hold
(13048, 4084, 9), -- Resist Physical Attack
(13048, 4116, 9), -- Resist M. Atk.
(13048, 4605, 9), -- Fire Weakness
(13048, 4589, 9), -- Decrease Speed
(13048, 4639, 3), -- NPC Clan Buff - Acumen Empower Berserk
-- Archon of Halisha
(13049, 4298, 1), -- Race
(13049, 4307, 1), -- Strong Type
(13049, 4278, 1), -- Dark Attack
(13049, 4333, 3), -- Resist Dark Attack
(13049, 4285, 3), -- Resist Sleep
(13049, 4287, 3), -- Resist Hold
(13049, 4084, 9), -- Resist Physical Attack
(13049, 4116, 9), -- Resist M. Atk.
(13049, 4605, 9), -- Fire Weakness
(13049, 4589, 9), -- Decrease Speed
(13049, 4639, 3), -- NPC Clan Buff - Acumen Empower Berserk
-- Archon of Halisha
(13050, 4298, 1), -- Race
(13050, 4307, 1), -- Strong Type
(13050, 4278, 1), -- Dark Attack
(13050, 4333, 3), -- Resist Dark Attack
(13050, 4285, 3), -- Resist Sleep
(13050, 4287, 3), -- Resist Hold
(13050, 4084, 9), -- Resist Physical Attack
(13050, 4116, 9), -- Resist M. Atk.
(13050, 4605, 9), -- Fire Weakness
(13050, 4589, 9), -- Decrease Speed
(13050, 4639, 3), -- NPC Clan Buff - Acumen Empower Berserk
-- Archon of Halisha
(13051, 4298, 1), -- Race
(13051, 4307, 1), -- Strong Type
(13051, 4278, 1), -- Dark Attack
(13051, 4333, 3), -- Resist Dark Attack
(13051, 4285, 3), -- Resist Sleep
(13051, 4287, 3), -- Resist Hold
(13051, 4084, 9), -- Resist Physical Attack
(13051, 4116, 9), -- Resist M. Atk.
(13051, 4605, 9), -- Fire Weakness
(13051, 4589, 9), -- Decrease Speed
(13051, 4639, 3), -- NPC Clan Buff - Acumen Empower Berserk
-- Archon of Halisha
(13052, 4298, 1), -- Race
(13052, 4307, 1), -- Strong Type
(13052, 4278, 1), -- Dark Attack
(13052, 4333, 3), -- Resist Dark Attack
(13052, 4285, 3), -- Resist Sleep
(13052, 4287, 3), -- Resist Hold
(13052, 4084, 9), -- Resist Physical Attack
(13052, 4116, 9), -- Resist M. Atk.
(13052, 4605, 9), -- Fire Weakness
(13052, 4589, 9), -- Decrease Speed
(13052, 4639, 3), -- NPC Clan Buff - Acumen Empower Berserk
-- Archon of Halisha
(13053, 4298, 1), -- Race
(13053, 4307, 1), -- Strong Type
(13053, 4278, 1), -- Dark Attack
(13053, 4333, 3), -- Resist Dark Attack
(13053, 4285, 3), -- Resist Sleep
(13053, 4287, 3), -- Resist Hold
(13053, 4084, 9), -- Resist Physical Attack
(13053, 4116, 9), -- Resist M. Atk.
(13053, 4605, 9), -- Fire Weakness
(13053, 4589, 9), -- Decrease Speed
(13053, 4639, 3), -- NPC Clan Buff - Acumen Empower Berserk
-- Archon of Halisha
(13054, 4298, 1), -- Race
(13054, 4307, 1), -- Strong Type
(13054, 4278, 1), -- Dark Attack
(13054, 4333, 3), -- Resist Dark Attack
(13054, 4285, 3), -- Resist Sleep
(13054, 4287, 3), -- Resist Hold
(13054, 4084, 9), -- Resist Physical Attack
(13054, 4116, 9), -- Resist M. Atk.
(13054, 4605, 9), -- Fire Weakness
(13054, 4589, 9), -- Decrease Speed
(13054, 4639, 3), -- NPC Clan Buff - Acumen Empower Berserk
-- Shaman of Darkness
(13055, 4298, 1), -- Race
(13055, 4307, 1), -- Strong Type
(13055, 4278, 1), -- Dark Attack
(13055, 4333, 3), -- Resist Dark Attack
(13055, 4116, 7), -- Resist M. Atk.
(13055, 4071, 3), -- Resist Archery
(13055, 4285, 3), -- Resist Sleep
(13055, 4033, 9), -- NPC Burn
(13055, 4587, 9), -- Decrease P.Atk
(13055, 4613, 9), -- NPC Clan Heal
-- Shaman of Darkness
(13056, 4298, 1), -- Race
(13056, 4307, 1), -- Strong Type
(13056, 4278, 1), -- Dark Attack
(13056, 4333, 3), -- Resist Dark Attack
(13056, 4116, 7), -- Resist M. Atk.
(13056, 4071, 3), -- Resist Archery
(13056, 4285, 3), -- Resist Sleep
(13056, 4033, 9), -- NPC Burn
(13056, 4597, 9), -- Bleed
(13056, 4600, 9), -- Reducing P.Def Shock
(13056, 4613, 9), -- NPC Clan Heal
-- Assassin of Darkness
(13057, 4298, 1), -- Race
(13057, 4307, 1), -- Strong Type
(13057, 4278, 1), -- Dark Attack
(13057, 4333, 3), -- Resist Dark Attack
(13057, 4084, 7), -- Resist Physical Attack
(13057, 4560, 9), -- NPC Fire Burn
-- Assassin of Darkness
(13058, 4298, 1), -- Race
(13058, 4307, 1), -- Strong Type
(13058, 4278, 1), -- Dark Attack
(13058, 4333, 3), -- Resist Dark Attack
(13058, 4573, 9), -- NPC Sonic Blaster
-- Assassin of Darkness
(13059, 4298, 1), -- Race
(13059, 4307, 1), -- Strong Type
(13059, 4278, 1), -- Dark Attack
(13059, 4333, 3), -- Resist Dark Attack
(13059, 4084, 7), -- Resist Physical Attack
(13059, 4573, 9), -- NPC Sonic Blaster
(13059, 4560, 9), -- NPC Fire Burn
-- Assassin of Darkness
(13060, 4298, 1), -- Race
(13060, 4307, 1), -- Strong Type
(13060, 4278, 1), -- Dark Attack
(13060, 4333, 3), -- Resist Dark Attack
(13060, 4084, 7), -- Resist Physical Attack
(13060, 4560, 9), -- NPC Fire Burn
(13060, 4091, 1), -- NPC Ogre Stun
-- Caster of Darkness
(13061, 4298, 1), -- Race
(13061, 4307, 1), -- Strong Type
(13061, 4278, 1), -- Dark Attack
(13061, 4333, 3), -- Resist Dark Attack
(13061, 4157, 9), -- NPC Blaze - Magic
(13061, 4561, 9), -- NPC Fire Burn - Magic
-- Caster of Darkness
(13062, 4298, 1), -- Race
(13062, 4307, 1), -- Strong Type
(13062, 4278, 1), -- Dark Attack
(13062, 4333, 3), -- Resist Dark Attack
(13062, 4084, 7), -- Resist Physical Attack
(13062, 4157, 9), -- NPC Blaze - Magic
(13062, 4561, 9), -- NPC Fire Burn - Magic
(13062, 4575, 2), -- NPC Clan Buff - Haste
-- Caster of Darkness
(13063, 4298, 1), -- Race
(13063, 4307, 1), -- Strong Type
(13063, 4278, 1), -- Dark Attack
(13063, 4333, 3), -- Resist Dark Attack
(13063, 4084, 7), -- Resist Physical Attack
(13063, 4157, 9), -- NPC Blaze - Magic
(13063, 4561, 9), -- NPC Fire Burn - Magic
(13063, 4102, 2), -- Become weak against line of fire.
-- Caster of Darkness
(13064, 4298, 1), -- Race
(13064, 4307, 1), -- Strong Type
(13064, 4278, 1), -- Dark Attack
(13064, 4333, 3), -- Resist Dark Attack
(13064, 4084, 7), -- Resist Physical Attack
(13064, 4033, 9), -- NPC Burn
(13064, 4561, 9), -- NPC Fire Burn - Magic
-- Signet of Emperor
(13065, 4301, 1), -- Race
(13065, 4304, 1), -- Strong Type
(13065, 4071, 4), -- Resist Archery
(13065, 4285, 5), -- Resist Sleep
(13065, 4287, 5), -- Resist Hold
(13065, 4614, 9), -- NPC Death Bomb
-- Statue of Protection
(13066, 4291, 1), -- Race
(13066, 4307, 1), -- Strong Type
(13066, 4277, 6), -- Resist Poison
(13066, 4284, 6), -- Resist Bleeding
(13066, 4084, 7), -- Resist Physical Attack
(13066, 4116, 7), -- Resist M. Atk.
(13066, 4071, 3), -- Resist Archery
(13066, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13067, 4291, 1), -- Race
(13067, 4307, 1), -- Strong Type
(13067, 4277, 6), -- Resist Poison
(13067, 4284, 6), -- Resist Bleeding
(13067, 4084, 7), -- Resist Physical Attack
(13067, 4116, 7), -- Resist M. Atk.
(13067, 4071, 3), -- Resist Archery
(13067, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13068, 4291, 1), -- Race
(13068, 4307, 1), -- Strong Type
(13068, 4277, 6), -- Resist Poison
(13068, 4284, 6), -- Resist Bleeding
(13068, 4084, 7), -- Resist Physical Attack
(13068, 4116, 7), -- Resist M. Atk.
(13068, 4071, 3), -- Resist Archery
(13068, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13069, 4291, 1), -- Race
(13069, 4307, 1), -- Strong Type
(13069, 4277, 6), -- Resist Poison
(13069, 4284, 6), -- Resist Bleeding
(13069, 4084, 7), -- Resist Physical Attack
(13069, 4116, 7), -- Resist M. Atk.
(13069, 4071, 3), -- Resist Archery
(13069, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13070, 4291, 1), -- Race
(13070, 4307, 1), -- Strong Type
(13070, 4277, 6), -- Resist Poison
(13070, 4284, 6), -- Resist Bleeding
(13070, 4084, 7), -- Resist Physical Attack
(13070, 4116, 7), -- Resist M. Atk.
(13070, 4071, 3), -- Resist Archery
(13070, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13071, 4291, 1), -- Race
(13071, 4307, 1), -- Strong Type
(13071, 4277, 6), -- Resist Poison
(13071, 4284, 6), -- Resist Bleeding
(13071, 4084, 7), -- Resist Physical Attack
(13071, 4116, 7), -- Resist M. Atk.
(13071, 4071, 3), -- Resist Archery
(13071, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13072, 4291, 1), -- Race
(13072, 4307, 1), -- Strong Type
(13072, 4277, 6), -- Resist Poison
(13072, 4284, 6), -- Resist Bleeding
(13072, 4084, 7), -- Resist Physical Attack
(13072, 4116, 7), -- Resist M. Atk.
(13072, 4071, 3), -- Resist Archery
(13072, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13073, 4291, 1), -- Race
(13073, 4307, 1), -- Strong Type
(13073, 4277, 6), -- Resist Poison
(13073, 4284, 6), -- Resist Bleeding
(13073, 4084, 7), -- Resist Physical Attack
(13073, 4116, 7), -- Resist M. Atk.
(13073, 4071, 3), -- Resist Archery
(13073, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13074, 4291, 1), -- Race
(13074, 4307, 1), -- Strong Type
(13074, 4277, 6), -- Resist Poison
(13074, 4284, 6), -- Resist Bleeding
(13074, 4084, 7), -- Resist Physical Attack
(13074, 4116, 7), -- Resist M. Atk.
(13074, 4071, 3), -- Resist Archery
(13074, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13075, 4291, 1), -- Race
(13075, 4307, 1), -- Strong Type
(13075, 4277, 6), -- Resist Poison
(13075, 4284, 6), -- Resist Bleeding
(13075, 4084, 7), -- Resist Physical Attack
(13075, 4116, 7), -- Resist M. Atk.
(13075, 4071, 3), -- Resist Archery
(13075, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13076, 4291, 1), -- Race
(13076, 4307, 1), -- Strong Type
(13076, 4277, 6), -- Resist Poison
(13076, 4284, 6), -- Resist Bleeding
(13076, 4084, 7), -- Resist Physical Attack
(13076, 4116, 7), -- Resist M. Atk.
(13076, 4071, 3), -- Resist Archery
(13076, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13077, 4291, 1), -- Race
(13077, 4307, 1), -- Strong Type
(13077, 4277, 6), -- Resist Poison
(13077, 4284, 6), -- Resist Bleeding
(13077, 4084, 7), -- Resist Physical Attack
(13077, 4116, 7), -- Resist M. Atk.
(13077, 4071, 3), -- Resist Archery
(13077, 4274, 1), -- Blunt Attack Weak Point
-- Statue of Protection
(13078, 4291, 1), -- Race
(13078, 4307, 1), -- Strong Type
(13078, 4277, 6), -- Resist Poison
(13078, 4284, 6), -- Resist Bleeding
(13078, 4084, 7), -- Resist Physical Attack
(13078, 4116, 7), -- Resist M. Atk.
(13078, 4071, 3), -- Resist Archery
(13078, 4274, 1), -- Blunt Attack Weak Point
(13078, 4605, 9), -- Fire Weakness
(13078, 4620, 9), -- Paralysis
(13078, 4072, 9), -- Shock
(13078, 4616, 1), -- Fake Petrificiation
(13078, 4383, 1), -- NPC Hate Stone
-- Watchman of Grave
(13079, 4291, 1), -- Race
(13079, 4305, 1), -- Strong Type
(13079, 4285, 5), -- Resist Sleep
(13079, 4287, 5), -- Resist Hold
(13079, 4614, 9), -- NPC Death Bomb
(13079, 4613, 9), -- NPC Clan Heal
(13079, 4606, 9), -- Poison
-- Watchman of Grave
(13080, 4291, 1), -- Race
(13080, 4305, 1), -- Strong Type
(13080, 4285, 5), -- Resist Sleep
(13080, 4287, 5), -- Resist Hold
(13080, 4614, 9), -- NPC Death Bomb
(13080, 4613, 9), -- NPC Clan Heal
(13080, 4606, 9), -- Poison
-- Watchman of Grave
(13081, 4291, 1), -- Race
(13081, 4305, 1), -- Strong Type
(13081, 4285, 5), -- Resist Sleep
(13081, 4287, 5), -- Resist Hold
(13081, 4614, 9), -- NPC Death Bomb
(13081, 4613, 9), -- NPC Clan Heal
(13081, 4606, 9), -- Poison
-- Watchman of Grave
(13082, 4291, 1), -- Race
(13082, 4305, 1), -- Strong Type
(13082, 4285, 5), -- Resist Sleep
(13082, 4287, 5), -- Resist Hold
(13082, 4614, 9), -- NPC Death Bomb
(13082, 4613, 9), -- NPC Clan Heal
(13082, 4606, 9), -- Poison
-- Watchman of Grave
(13083, 4291, 1), -- Race
(13083, 4305, 1), -- Strong Type
(13083, 4285, 5), -- Resist Sleep
(13083, 4287, 5), -- Resist Hold
(13083, 4614, 9), -- NPC Death Bomb
(13083, 4613, 9), -- NPC Clan Heal
(13083, 4606, 9), -- Poison
-- Watchman of Grave
(13084, 4291, 1), -- Race
(13084, 4305, 1), -- Strong Type
(13084, 4285, 5), -- Resist Sleep
(13084, 4287, 5), -- Resist Hold
(13084, 4614, 9), -- NPC Death Bomb
(13084, 4613, 9), -- NPC Clan Heal
(13084, 4606, 9), -- Poison
-- Watchman of Grave
(13085, 4291, 1), -- Race
(13085, 4305, 1), -- Strong Type
(13085, 4285, 5), -- Resist Sleep
(13085, 4287, 5), -- Resist Hold
(13085, 4614, 9), -- NPC Death Bomb
(13085, 4613, 9), -- NPC Clan Heal
(13085, 4606, 9), -- Poison
-- Watchman of Grave
(13086, 4291, 1), -- Race
(13086, 4305, 1), -- Strong Type
(13086, 4285, 5), -- Resist Sleep
(13086, 4287, 5), -- Resist Hold
(13086, 4614, 9), -- NPC Death Bomb
(13086, 4613, 9), -- NPC Clan Heal
(13086, 4606, 9), -- Poison
-- Watchman of Grave
(13087, 4291, 1), -- Race
(13087, 4305, 1), -- Strong Type
(13087, 4285, 5), -- Resist Sleep
(13087, 4287, 5), -- Resist Hold
(13087, 4614, 9), -- NPC Death Bomb
(13087, 4613, 9), -- NPC Clan Heal
(13087, 4606, 9), -- Poison
-- Watchman of Grave
(13088, 4291, 1), -- Race
(13088, 4305, 1), -- Strong Type
(13088, 4285, 5), -- Resist Sleep
(13088, 4287, 5), -- Resist Hold
(13088, 4614, 9), -- NPC Death Bomb
(13088, 4613, 9), -- NPC Clan Heal
(13088, 4606, 9), -- Poison
-- Watchman of Grave
(13089, 4291, 1), -- Race
(13089, 4305, 1), -- Strong Type
(13089, 4285, 5), -- Resist Sleep
(13089, 4287, 5), -- Resist Hold
(13089, 4614, 9), -- NPC Death Bomb
(13089, 4613, 9), -- NPC Clan Heal
(13089, 4606, 9), -- Poison
-- Watchman of Grave
(13090, 4291, 1), -- Race
(13090, 4305, 1), -- Strong Type
(13090, 4285, 5), -- Resist Sleep
(13090, 4287, 5), -- Resist Hold
(13090, 4614, 9), -- NPC Death Bomb
(13090, 4613, 9), -- NPC Clan Heal
(13090, 4606, 9), -- Poison
-- Halisha's Treasure Box
(13091, 4291, 1), -- Race
(13091, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13092, 4291, 1), -- Race
(13092, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13093, 4291, 1), -- Race
(13093, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13094, 4291, 1), -- Race
(13094, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13095, 4291, 1), -- Race
(13095, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13096, 4291, 1), -- Race
(13096, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13097, 4291, 1), -- Race
(13097, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13098, 4291, 1), -- Race
(13098, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13099, 4291, 1), -- Race
(13099, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13100, 4291, 1), -- Race
(13100, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13101, 4291, 1), -- Race
(13101, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13102, 4291, 1), -- Race
(13102, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13103, 4291, 1), -- Race
(13103, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13104, 4291, 1), -- Race
(13104, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13105, 4291, 1), -- Race
(13105, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13106, 4291, 1), -- Race
(13106, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13107, 4291, 1), -- Race
(13107, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13108, 4291, 1), -- Race
(13108, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13109, 4291, 1), -- Race
(13109, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13110, 4291, 1), -- Race
(13110, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13111, 4291, 1), -- Race
(13111, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13112, 4291, 1), -- Race
(13112, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13113, 4291, 1), -- Race
(13113, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13114, 4291, 1), -- Race
(13114, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13115, 4291, 1), -- Race
(13115, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13116, 4291, 1), -- Race
(13116, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13117, 4291, 1), -- Race
(13117, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13118, 4291, 1), -- Race
(13118, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13119, 4291, 1), -- Race
(13119, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13120, 4291, 1), -- Race
(13120, 4045, 1), -- Resist Full Magic Attack
-- Treasure Box
(13121, 4291, 1), -- Race
(13121, 4045, 1), -- Resist Full Magic Attack
-- Fenril Hound Kerinne
(13122, 4293, 1), -- Race
(13122, 4045, 1), -- Resist Full Magic Attack
(13122, 4178, 9), -- BOSS Flamestrike
-- Kerinne's Ifrit
(13123, 4296, 1), -- Race
(13123, 4045, 1), -- Resist Full Magic Attack
(13123, 4209, 9), -- BOSS Heal
(13123, 4213, 9), -- BOSS Haste
-- Kerinne's Golem
(13124, 4291, 1), -- Race
(13124, 4045, 1), -- Resist Full Magic Attack
-- Fenril Hound Freki
(13125, 4293, 1), -- Race
(13125, 4045, 1), -- Resist Full Magic Attack
(13125, 4197, 9), -- Hold
-- Freki's Ifrit
(13126, 4296, 1), -- Race
(13126, 4045, 1), -- Resist Full Magic Attack
(13126, 4193, 9), -- BOSS Life Drain
(13126, 4203, 9), -- Decrease Speed
-- Freki's Golem
(13127, 4291, 1), -- Race
(13127, 4045, 1), -- Resist Full Magic Attack
-- Fenril Hound Uruz
(13128, 4293, 1), -- Race
(13128, 4045, 1), -- Resist Full Magic Attack
(13128, 4175, 9), -- BOSS Haste
(13128, 4170, 9), -- BOSS Mortal Blow
(13128, 4172, 9), -- Shock
-- Fenril Hound Kinaz
(13129, 4293, 1), -- Race
(13129, 4045, 1), -- Resist Full Magic Attack
(13129, 4195, 9), -- BOSS Twister
(13129, 4184, 9), -- Decrease Atk.Speed
(13129, 4188, 9), -- Bleed
-- Kinaz's Ifrit
(13130, 4296, 1), -- Race
(13130, 4045, 1), -- Resist Full Magic Attack
(13130, 4191, 9), -- BOSS Windstrike
(13130, 4201, 9), -- Sleep
-- Kinaz's Ifrit
(13131, 4296, 1), -- Race
(13131, 4045, 1), -- Resist Full Magic Attack
(13131, 4210, 9), -- BOSS Chant of Life
(13131, 4212, 9), -- BOSS Shield
-- Wings of Flame, Ixion
(13132, 4299, 1), -- Race
(13132, 4045, 1), -- Resist Full Magic Attack
(13132, 4175, 10), -- BOSS Haste
(13132, 4168, 10), -- BOSS Strike
(13132, 4172, 10), -- Shock
-- Inferno Golem
(13133, 4291, 1), -- Race
(13133, 4045, 1), -- Resist Full Magic Attack
(13133, 4179, 9), -- BOSS Strike
-- Inferno Golem
(13134, 4291, 1), -- Race
(13134, 4045, 1), -- Resist Full Magic Attack
-- Hauling Ifrit
(13135, 4296, 1), -- Race
(13135, 4045, 1), -- Resist Full Magic Attack
(13135, 4209, 9), -- BOSS Heal
(13135, 4211, 9), -- BOSS Might
-- Hauling Ifrit
(13136, 4296, 1), -- Race
(13136, 4045, 1), -- Resist Full Magic Attack
(13136, 4191, 9), -- BOSS Windstrike
(13136, 4199, 9), -- Decrease P.Atk
-- Cat Queen
(13137, 4293, 1), -- Race
(13137, 4121, 1), -- Summoned Monster Magic Protection
(13137, 4699, 1), -- Blessing of Queen
(13137, 4700, 1), -- Gift of Queen
(13137, 4701, 1), -- Cure of Queen
-- Cat Queen
(13138, 4293, 1), -- Race
(13138, 4121, 1), -- Summoned Monster Magic Protection
(13138, 4699, 1), -- Blessing of Queen
(13138, 4700, 1), -- Gift of Queen
(13138, 4701, 1), -- Cure of Queen
-- Cat Queen
(13139, 4293, 1), -- Race
(13139, 4121, 1), -- Summoned Monster Magic Protection
(13139, 4699, 1), -- Blessing of Queen
(13139, 4700, 1), -- Gift of Queen
(13139, 4701, 1), -- Cure of Queen
-- Cat Queen
(13140, 4293, 1), -- Race
(13140, 4121, 1), -- Summoned Monster Magic Protection
(13140, 4699, 2), -- Blessing of Queen
(13140, 4700, 2), -- Gift of Queen
(13140, 4701, 2), -- Cure of Queen
-- Cat Queen
(13141, 4293, 1), -- Race
(13141, 4121, 1), -- Summoned Monster Magic Protection
(13141, 4699, 2), -- Blessing of Queen
(13141, 4700, 2), -- Gift of Queen
(13141, 4701, 2), -- Cure of Queen
-- Cat Queen
(13142, 4293, 1), -- Race
(13142, 4121, 1), -- Summoned Monster Magic Protection
(13142, 4699, 2), -- Blessing of Queen
(13142, 4700, 2), -- Gift of Queen
(13142, 4701, 2), -- Cure of Queen
-- Cat Queen
(13143, 4293, 1), -- Race
(13143, 4121, 1), -- Summoned Monster Magic Protection
(13143, 4699, 3), -- Blessing of Queen
(13143, 4700, 3), -- Gift of Queen
(13143, 4701, 3), -- Cure of Queen
-- Cat Queen
(13144, 4293, 1), -- Race
(13144, 4121, 1), -- Summoned Monster Magic Protection
(13144, 4699, 3), -- Blessing of Queen
(13144, 4700, 3), -- Gift of Queen
(13144, 4701, 3), -- Cure of Queen
-- Cat Queen
(13145, 4293, 1), -- Race
(13145, 4121, 1), -- Summoned Monster Magic Protection
(13145, 4699, 3), -- Blessing of Queen
(13145, 4700, 3), -- Gift of Queen
(13145, 4701, 3), -- Cure of Queen
-- Cat Queen
(13146, 4293, 1), -- Race
(13146, 4121, 1), -- Summoned Monster Magic Protection
(13146, 4699, 3), -- Blessing of Queen
(13146, 4700, 3), -- Gift of Queen
(13146, 4701, 3), -- Cure of Queen
-- Cat Queen
(13147, 4293, 1), -- Race
(13147, 4121, 1), -- Summoned Monster Magic Protection
(13147, 4699, 3), -- Blessing of Queen
(13147, 4700, 3), -- Gift of Queen
(13147, 4701, 3), -- Cure of Queen
-- Cat Queen
(13148, 4293, 1), -- Race
(13148, 4121, 1), -- Summoned Monster Magic Protection
(13148, 4699, 3), -- Blessing of Queen
(13148, 4700, 3), -- Gift of Queen
(13148, 4701, 3), -- Cure of Queen
-- Cat Queen
(13149, 4293, 1), -- Race
(13149, 4121, 1), -- Summoned Monster Magic Protection
(13149, 4699, 3), -- Blessing of Queen
(13149, 4700, 3), -- Gift of Queen
(13149, 4701, 3), -- Cure of Queen
-- Cat Queen
(13150, 4293, 1), -- Race
(13150, 4121, 1), -- Summoned Monster Magic Protection
(13150, 4699, 3), -- Blessing of Queen
(13150, 4700, 3), -- Gift of Queen
(13150, 4701, 3), -- Cure of Queen
-- Unicorn Seraphim
(13151, 4296, 1), -- Race
(13151, 4121, 1), -- Summoned Monster Magic Protection
(13151, 4702, 1), -- Blessing of Seraphim
(13151, 4703, 1), -- Gift of Seraphim
(13151, 4704, 1), -- Cure of Seraphim
-- Unicorn Seraphim
(13152, 4296, 1), -- Race
(13152, 4121, 1), -- Summoned Monster Magic Protection
(13152, 4702, 1), -- Blessing of Seraphim
(13152, 4703, 1), -- Gift of Seraphim
(13152, 4704, 1), -- Cure of Seraphim
-- Unicorn Seraphim
(13153, 4296, 1), -- Race
(13153, 4121, 1), -- Summoned Monster Magic Protection
(13153, 4702, 1), -- Blessing of Seraphim
(13153, 4703, 1), -- Gift of Seraphim
(13153, 4704, 1), -- Cure of Seraphim
-- Unicorn Seraphim
(13154, 4296, 1), -- Race
(13154, 4121, 1), -- Summoned Monster Magic Protection
(13154, 4702, 2), -- Blessing of Seraphim
(13154, 4703, 2), -- Gift of Seraphim
(13154, 4704, 2), -- Cure of Seraphim
-- Unicorn Seraphim
(13155, 4296, 1), -- Race
(13155, 4121, 1), -- Summoned Monster Magic Protection
(13155, 4702, 2), -- Blessing of Seraphim
(13155, 4703, 2), -- Gift of Seraphim
(13155, 4704, 2), -- Cure of Seraphim
-- Unicorn Seraphim
(13156, 4296, 1), -- Race
(13156, 4121, 1), -- Summoned Monster Magic Protection
(13156, 4702, 2), -- Blessing of Seraphim
(13156, 4703, 2), -- Gift of Seraphim
(13156, 4704, 2), -- Cure of Seraphim
-- Unicorn Seraphim
(13157, 4296, 1), -- Race
(13157, 4121, 1), -- Summoned Monster Magic Protection
(13157, 4702, 3), -- Blessing of Seraphim
(13157, 4703, 3), -- Gift of Seraphim
(13157, 4704, 3), -- Cure of Seraphim
-- Unicorn Seraphim
(13158, 4296, 1), -- Race
(13158, 4121, 1), -- Summoned Monster Magic Protection
(13158, 4702, 3), -- Blessing of Seraphim
(13158, 4703, 3), -- Gift of Seraphim
(13158, 4704, 3), -- Cure of Seraphim
-- Unicorn Seraphim
(13159, 4296, 1), -- Race
(13159, 4121, 1), -- Summoned Monster Magic Protection
(13159, 4702, 3), -- Blessing of Seraphim
(13159, 4703, 3), -- Gift of Seraphim
(13159, 4704, 3), -- Cure of Seraphim
-- Unicorn Seraphim
(13160, 4296, 1), -- Race
(13160, 4121, 1), -- Summoned Monster Magic Protection
(13160, 4702, 3), -- Blessing of Seraphim
(13160, 4703, 3), -- Gift of Seraphim
(13160, 4704, 3), -- Cure of Seraphim
-- Unicorn Seraphim
(13161, 4296, 1), -- Race
(13161, 4121, 1), -- Summoned Monster Magic Protection
(13161, 4702, 3), -- Blessing of Seraphim
(13161, 4703, 3), -- Gift of Seraphim
(13161, 4704, 3), -- Cure of Seraphim
-- Unicorn Seraphim
(13162, 4296, 1), -- Race
(13162, 4121, 1), -- Summoned Monster Magic Protection
(13162, 4702, 3), -- Blessing of Seraphim
(13162, 4703, 3), -- Gift of Seraphim
(13162, 4704, 3), -- Cure of Seraphim
-- Unicorn Seraphim
(13163, 4296, 1), -- Race
(13163, 4121, 1), -- Summoned Monster Magic Protection
(13163, 4702, 3), -- Blessing of Seraphim
(13163, 4703, 3), -- Gift of Seraphim
(13163, 4704, 3), -- Cure of Seraphim
-- Unicorn Seraphim
(13164, 4296, 1), -- Race
(13164, 4121, 1), -- Summoned Monster Magic Protection
(13164, 4702, 3), -- Blessing of Seraphim
(13164, 4703, 3), -- Gift of Seraphim
(13164, 4704, 3), -- Cure of Seraphim
-- Nightshade
(13165, 4298, 1), -- Race
(13165, 4121, 1), -- Summoned Monster Magic Protection
(13165, 4705, 1), -- Curse of Shade
(13165, 4706, 1), -- Mass Curse of Shade
(13165, 4707, 1), -- Shade Sacrifice
-- Nightshade
(13166, 4298, 1), -- Race
(13166, 4121, 1), -- Summoned Monster Magic Protection
(13166, 4705, 1), -- Curse of Shade
(13166, 4706, 1), -- Mass Curse of Shade
(13166, 4707, 1), -- Shade Sacrifice
-- Nightshade
(13167, 4298, 1), -- Race
(13167, 4121, 1), -- Summoned Monster Magic Protection
(13167, 4705, 1), -- Curse of Shade
(13167, 4706, 1), -- Mass Curse of Shade
(13167, 4707, 1), -- Shade Sacrifice
-- Nightshade
(13168, 4298, 1), -- Race
(13168, 4121, 1), -- Summoned Monster Magic Protection
(13168, 4705, 2), -- Curse of Shade
(13168, 4706, 2), -- Mass Curse of Shade
(13168, 4707, 2), -- Shade Sacrifice
-- Nightshade
(13169, 4298, 1), -- Race
(13169, 4121, 1), -- Summoned Monster Magic Protection
(13169, 4705, 2), -- Curse of Shade
(13169, 4706, 2), -- Mass Curse of Shade
(13169, 4707, 2), -- Shade Sacrifice
-- Nightshade
(13170, 4298, 1), -- Race
(13170, 4121, 1), -- Summoned Monster Magic Protection
(13170, 4705, 2), -- Curse of Shade
(13170, 4706, 2), -- Mass Curse of Shade
(13170, 4707, 2), -- Shade Sacrifice
-- Nightshade
(13171, 4298, 1), -- Race
(13171, 4121, 1), -- Summoned Monster Magic Protection
(13171, 4705, 3), -- Curse of Shade
(13171, 4706, 3), -- Mass Curse of Shade
(13171, 4707, 3), -- Shade Sacrifice
-- Nightshade
(13172, 4298, 1), -- Race
(13172, 4121, 1), -- Summoned Monster Magic Protection
(13172, 4705, 3), -- Curse of Shade
(13172, 4706, 3), -- Mass Curse of Shade
(13172, 4707, 3), -- Shade Sacrifice
-- Nightshade
(13173, 4298, 1), -- Race
(13173, 4121, 1), -- Summoned Monster Magic Protection
(13173, 4705, 3), -- Curse of Shade
(13173, 4706, 3), -- Mass Curse of Shade
(13173, 4707, 3), -- Shade Sacrifice
-- Nightshade
(13174, 4298, 1), -- Race
(13174, 4121, 1), -- Summoned Monster Magic Protection
(13174, 4705, 3), -- Curse of Shade
(13174, 4706, 3), -- Mass Curse of Shade
(13174, 4707, 3), -- Shade Sacrifice
-- Nightshade
(13175, 4298, 1), -- Race
(13175, 4121, 1), -- Summoned Monster Magic Protection
(13175, 4705, 3), -- Curse of Shade
(13175, 4706, 3), -- Mass Curse of Shade
(13175, 4707, 3), -- Shade Sacrifice
-- Nightshade
(13176, 4298, 1), -- Race
(13176, 4121, 1), -- Summoned Monster Magic Protection
(13176, 4705, 3), -- Curse of Shade
(13176, 4706, 3), -- Mass Curse of Shade
(13176, 4707, 3), -- Shade Sacrifice
-- Nightshade
(13177, 4298, 1), -- Race
(13177, 4121, 1), -- Summoned Monster Magic Protection
(13177, 4705, 3), -- Curse of Shade
(13177, 4706, 3), -- Mass Curse of Shade
(13177, 4707, 3), -- Shade Sacrifice
-- Nightshade
(13178, 4298, 1), -- Race
(13178, 4121, 1), -- Summoned Monster Magic Protection
(13178, 4705, 3), -- Curse of Shade
(13178, 4706, 3), -- Mass Curse of Shade
(13178, 4707, 3), -- Shade Sacrifice
-- Cursed Man
(13179, 4290, 1), -- Race
(13179, 4121, 1), -- Summoned Monster Magic Protection
(13179, 4709, 1), -- Cursed Blow
(13179, 4708, 1), -- Cursed Strike
-- Cursed Man
(13180, 4290, 1), -- Race
(13180, 4121, 1), -- Summoned Monster Magic Protection
(13180, 4709, 2), -- Cursed Blow
(13180, 4708, 2), -- Cursed Strike
-- Cursed Man
(13181, 4290, 1), -- Race
(13181, 4121, 1), -- Summoned Monster Magic Protection
(13181, 4709, 3), -- Cursed Blow
(13181, 4708, 3), -- Cursed Strike
-- Cursed Man
(13182, 4290, 1), -- Race
(13182, 4121, 1), -- Summoned Monster Magic Protection
(13182, 4709, 4), -- Cursed Blow
(13182, 4708, 4), -- Cursed Strike
-- Cursed Man
(13183, 4290, 1), -- Race
(13183, 4121, 1), -- Summoned Monster Magic Protection
(13183, 4709, 5), -- Cursed Blow
(13183, 4708, 5), -- Cursed Strike
-- Cursed Man
(13184, 4290, 1), -- Race
(13184, 4121, 1), -- Summoned Monster Magic Protection
(13184, 4709, 6), -- Cursed Blow
(13184, 4708, 6), -- Cursed Strike
-- Cursed Man
(13185, 4290, 1), -- Race
(13185, 4121, 1), -- Summoned Monster Magic Protection
(13185, 4709, 7), -- Cursed Blow
(13185, 4708, 7), -- Cursed Strike
--  
(13186, 4291, 1), -- Race
-- Ghost Chamberlain of Elmoreden
(13187, 4290, 1), -- Race
(13187, 4045, 1), -- Resist Full Magic Attack
-- Ghost Chamberlain of Elmoreden
(13188, 4290, 1), -- Race
(13188, 4045, 1), -- Resist Full Magic Attack
-- Conquerors' Sepulcher Manager
(13189, 4291, 1), -- Race
(13189, 4045, 1), -- Resist Full Magic Attack
(13189, 4390, 1), -- NPC Abnormal Immunity
-- Emperors' Sepulcher Manager
(13190, 4291, 1), -- Race
(13190, 4045, 1), -- Resist Full Magic Attack
(13190, 4390, 1), -- NPC Abnormal Immunity
-- Great Sages' Sepulcher Manager
(13191, 4291, 1), -- Race
(13191, 4045, 1), -- Resist Full Magic Attack
(13191, 4390, 1), -- NPC Abnormal Immunity
-- Judges' Sepulcher Manager
(13192, 4291, 1), -- Race
(13192, 4045, 1), -- Resist Full Magic Attack
(13192, 4390, 1), -- NPC Abnormal Immunity
-- Baron's Hall Gatekeeper
(13193, 4291, 1), -- Race
(13193, 4045, 1), -- Resist Full Magic Attack
(13193, 4122, 1), -- Antharas
(13193, 4390, 1), -- NPC Abnormal Immunity
-- Viscount's Hall Gatekeeper
(13194, 4291, 1), -- Race
(13194, 4045, 1), -- Resist Full Magic Attack
(13194, 4390, 1), -- NPC Abnormal Immunity
-- Count's Hall Gatekeeper
(13195, 4291, 1), -- Race
(13195, 4045, 1), -- Resist Full Magic Attack
(13195, 4390, 1), -- NPC Abnormal Immunity
-- Marquis' Hall Gatekeeper
(13196, 4291, 1), -- Race
(13196, 4045, 1), -- Resist Full Magic Attack
(13196, 4390, 1), -- NPC Abnormal Immunity
-- Duke's Hall Gatekeeper
(13197, 4291, 1), -- Race
(13197, 4045, 1), -- Resist Full Magic Attack
(13197, 4390, 1), -- NPC Abnormal Immunity
-- Baron's Hall Gatekeeper
(13198, 4291, 1), -- Race
(13198, 4045, 1), -- Resist Full Magic Attack
(13198, 4390, 1), -- NPC Abnormal Immunity
-- Viscount's Hall Gatekeeper
(13199, 4291, 1), -- Race
(13199, 4045, 1), -- Resist Full Magic Attack
(13199, 4390, 1), -- NPC Abnormal Immunity
-- Count's Hall Gatekeeper
(13200, 4291, 1), -- Race
(13200, 4045, 1), -- Resist Full Magic Attack
(13200, 4390, 1), -- NPC Abnormal Immunity
-- Marquis' Hall Gatekeeper
(13201, 4291, 1), -- Race
(13201, 4045, 1), -- Resist Full Magic Attack
(13201, 4390, 1), -- NPC Abnormal Immunity
-- Duke's Hall Gatekeeper
(13202, 4291, 1), -- Race
(13202, 4045, 1), -- Resist Full Magic Attack
(13202, 4390, 1), -- NPC Abnormal Immunity
-- Baron's Hall Gatekeeper
(13203, 4291, 1), -- Race
(13203, 4045, 1), -- Resist Full Magic Attack
(13203, 4390, 1), -- NPC Abnormal Immunity
-- Viscount's Hall Gatekeeper
(13204, 4291, 1), -- Race
(13204, 4045, 1), -- Resist Full Magic Attack
(13204, 4390, 1), -- NPC Abnormal Immunity
-- Count's Hall Gatekeeper
(13205, 4291, 1), -- Race
(13205, 4045, 1), -- Resist Full Magic Attack
(13205, 4390, 1), -- NPC Abnormal Immunity
-- Marquis' Hall Gatekeeper
(13206, 4291, 1), -- Race
(13206, 4045, 1), -- Resist Full Magic Attack
(13206, 4390, 1), -- NPC Abnormal Immunity
-- Duke's Hall Gatekeeper
(13207, 4291, 1), -- Race
(13207, 4045, 1), -- Resist Full Magic Attack
(13207, 4390, 1), -- NPC Abnormal Immunity
-- Baron's Hall Gatekeeper
(13208, 4291, 1), -- Race
(13208, 4045, 1), -- Resist Full Magic Attack
(13208, 4390, 1), -- NPC Abnormal Immunity
-- Viscount's Hall Gatekeeper
(13209, 4291, 1), -- Race
(13209, 4045, 1), -- Resist Full Magic Attack
(13209, 4390, 1), -- NPC Abnormal Immunity
-- Count's Hall Gatekeeper
(13210, 4291, 1), -- Race
(13210, 4045, 1), -- Resist Full Magic Attack
(13210, 4390, 1), -- NPC Abnormal Immunity
-- Marquis' Hall Gatekeeper
(13211, 4291, 1), -- Race
(13211, 4045, 1), -- Resist Full Magic Attack
(13211, 4390, 1), -- NPC Abnormal Immunity
-- Duke's Hall Gatekeeper
(13212, 4291, 1), -- Race
(13212, 4045, 1), -- Resist Full Magic Attack
(13212, 4390, 1), -- NPC Abnormal Immunity
-- Otherworldly Invader Food
(13213, 4291, 1), -- Race
(13213, 4045, 1), -- Resist Full Magic Attack
-- Otherworldly Invader Food
(13214, 4291, 1), -- Race
(13214, 4045, 1), -- Resist Full Magic Attack
-- Dimension Invader Food
(13215, 4291, 1), -- Race
(13215, 4045, 1), -- Resist Full Magic Attack
-- Dimension Invader Food
(13216, 4291, 1), -- Race
(13216, 4045, 1), -- Resist Full Magic Attack
-- Purgatory Invader Food
(13217, 4291, 1), -- Race
(13217, 4045, 1), -- Resist Full Magic Attack
-- Purgatory Invader Food
(13218, 4291, 1), -- Race
(13218, 4045, 1), -- Resist Full Magic Attack
-- Forbidden Path Invader Food
(13219, 4291, 1), -- Race
(13219, 4045, 1), -- Resist Full Magic Attack
-- Forbidden Path Invader Food
(13220, 4291, 1), -- Race
(13220, 4045, 1), -- Resist Full Magic Attack
-- Dark Omen Invader Food
(13221, 4291, 1), -- Race
(13221, 4045, 1), -- Resist Full Magic Attack
-- Dark Omen Invader Food
(13222, 4291, 1), -- Race
(13222, 4045, 1), -- Resist Full Magic Attack
-- Messenger Invader Food
(13223, 4291, 1), -- Race
(13223, 4045, 1), -- Resist Full Magic Attack
-- Messenger Invader Food
(13224, 4291, 1), -- Race
(13224, 4045, 1), -- Resist Full Magic Attack
-- Flame of the Branded
(13225, 4291, 1), -- Race
(13225, 4304, 1), -- Strong Type
(13225, 4285, 4), -- Resist Sleep
(13225, 4287, 4), -- Resist Hold
(13225, 4071, 4), -- Resist Archery
(13225, 4116, 8), -- Resist M. Atk.
(13225, 4614, 4), -- NPC Death Bomb
-- Offering of the Branded, Evoked Spirit
(13226, 4295, 1), -- Race
(13226, 4285, 4), -- Resist Sleep
(13226, 4287, 4), -- Resist Hold
(13226, 4116, 6), -- Resist M. Atk.
(13226, 4572, 3), -- NPC Triple Sonic Slash
-- Flame of Apostates
(13227, 4291, 1), -- Race
(13227, 4304, 1), -- Strong Type
(13227, 4285, 4), -- Resist Sleep
(13227, 4287, 4), -- Resist Hold
(13227, 4071, 4), -- Resist Archery
(13227, 4116, 8), -- Resist M. Atk.
(13227, 4614, 5), -- NPC Death Bomb
-- Offering of Apostates, Evoked Spirit
(13228, 4295, 1), -- Race
(13228, 4285, 4), -- Resist Sleep
(13228, 4287, 4), -- Resist Hold
(13228, 4116, 6), -- Resist M. Atk.
(13228, 4572, 4), -- NPC Triple Sonic Slash
-- Flame of the Witch
(13229, 4291, 1), -- Race
(13229, 4304, 1), -- Strong Type
(13229, 4285, 4), -- Resist Sleep
(13229, 4287, 4), -- Resist Hold
(13229, 4071, 4), -- Resist Archery
(13229, 4116, 8), -- Resist M. Atk.
(13229, 4614, 6), -- NPC Death Bomb
-- Offering of the Witch, Evoked Spirit
(13230, 4295, 1), -- Race
(13230, 4285, 4), -- Resist Sleep
(13230, 4287, 4), -- Resist Hold
(13230, 4116, 6), -- Resist M. Atk.
(13230, 4572, 5), -- NPC Triple Sonic Slash
-- Flame of Dark Omen
(13231, 4291, 1), -- Race
(13231, 4304, 1), -- Strong Type
(13231, 4285, 4), -- Resist Sleep
(13231, 4287, 4), -- Resist Hold
(13231, 4071, 4), -- Resist Archery
(13231, 4116, 8), -- Resist M. Atk.
(13231, 4614, 7), -- NPC Death Bomb
-- Offering of Dark Omen, Evoked Spirit
(13232, 4295, 1), -- Race
(13232, 4285, 4), -- Resist Sleep
(13232, 4287, 4), -- Resist Hold
(13232, 4116, 6), -- Resist M. Atk.
(13232, 4572, 6), -- NPC Triple Sonic Slash
-- Flame of Forbidden Path
(13233, 4291, 1), -- Race
(13233, 4304, 1), -- Strong Type
(13233, 4285, 4), -- Resist Sleep
(13233, 4287, 4), -- Resist Hold
(13233, 4071, 4), -- Resist Archery
(13233, 4116, 8), -- Resist M. Atk.
(13233, 4614, 9), -- NPC Death Bomb
-- Offering of /Forbidden Path, Evoked Spirit
(13234, 4295, 1), -- Race
(13234, 4285, 4), -- Resist Sleep
(13234, 4287, 4), -- Resist Hold
(13234, 4116, 6), -- Resist M. Atk.
(13234, 4572, 8), -- NPC Triple Sonic Slash
-- Flame of the Branded
(13235, 4291, 1), -- Race
(13235, 4304, 1), -- Strong Type
(13235, 4285, 4), -- Resist Sleep
(13235, 4287, 4), -- Resist Hold
(13235, 4071, 4), -- Resist Archery
(13235, 4116, 8), -- Resist M. Atk.
(13235, 4614, 4), -- NPC Death Bomb
-- Offering of the Branded, Evoked Spirit
(13236, 4295, 1), -- Race
(13236, 4285, 4), -- Resist Sleep
(13236, 4287, 4), -- Resist Hold
(13236, 4116, 6), -- Resist M. Atk.
(13236, 4572, 3), -- NPC Triple Sonic Slash
-- Flame of Apostates
(13237, 4291, 1), -- Race
(13237, 4304, 1), -- Strong Type
(13237, 4285, 4), -- Resist Sleep
(13237, 4287, 4), -- Resist Hold
(13237, 4071, 4), -- Resist Archery
(13237, 4116, 8), -- Resist M. Atk.
(13237, 4614, 5), -- NPC Death Bomb
-- Offering of Apostates, Evoked Spirit
(13238, 4295, 1), -- Race
(13238, 4285, 4), -- Resist Sleep
(13238, 4287, 4), -- Resist Hold
(13238, 4116, 6), -- Resist M. Atk.
(13238, 4572, 4), -- NPC Triple Sonic Slash
-- Flame of the Witch
(13239, 4291, 1), -- Race
(13239, 4304, 1), -- Strong Type
(13239, 4285, 4), -- Resist Sleep
(13239, 4287, 4), -- Resist Hold
(13239, 4071, 4), -- Resist Archery
(13239, 4116, 8), -- Resist M. Atk.
(13239, 4614, 6), -- NPC Death Bomb
-- Offering of the Witch, Evoked Spirit
(13240, 4295, 1), -- Race
(13240, 4285, 4), -- Resist Sleep
(13240, 4287, 4), -- Resist Hold
(13240, 4116, 6), -- Resist M. Atk.
(13240, 4572, 5), -- NPC Triple Sonic Slash
-- Flame of Dark Omen
(13241, 4291, 1), -- Race
(13241, 4304, 1), -- Strong Type
(13241, 4285, 4), -- Resist Sleep
(13241, 4287, 4), -- Resist Hold
(13241, 4071, 4), -- Resist Archery
(13241, 4116, 8), -- Resist M. Atk.
(13241, 4614, 7), -- NPC Death Bomb
-- Offering of Dark Omen, Evoked Spirit
(13242, 4295, 1), -- Race
(13242, 4285, 4), -- Resist Sleep
(13242, 4287, 4), -- Resist Hold
(13242, 4116, 6), -- Resist M. Atk.
(13242, 4572, 6), -- NPC Triple Sonic Slash
-- Flame of Forbidden Path
(13243, 4291, 1), -- Race
(13243, 4304, 1), -- Strong Type
(13243, 4285, 4), -- Resist Sleep
(13243, 4287, 4), -- Resist Hold
(13243, 4071, 4), -- Resist Archery
(13243, 4116, 8), -- Resist M. Atk.
(13243, 4614, 9), -- NPC Death Bomb
-- Offering of Forbidden Path, Evoked Spirit
(13244, 4295, 1), -- Race
(13244, 4285, 4), -- Resist Sleep
(13244, 4287, 4), -- Resist Hold
(13244, 4116, 6), -- Resist M. Atk.
(13244, 4572, 8), -- NPC Triple Sonic Slash
-- Caught Frog
(13245, 4292, 1), -- Race
(13245, 4285, 4), -- Resist Sleep
(13245, 4287, 4), -- Resist Hold
(13245, 4116, 6), -- Resist M. Atk.
(13245, 4382, 1), -- Curse of Lake Ghost
-- Caught Undine
(13246, 4296, 1), -- Race
(13246, 4285, 4), -- Resist Sleep
(13246, 4287, 4), -- Resist Hold
(13246, 4116, 6), -- Resist M. Atk.
(13246, 4382, 2), -- Curse of Lake Ghost
-- Caught Rakul
(13247, 4290, 1), -- Race
(13247, 4285, 4), -- Resist Sleep
(13247, 4287, 4), -- Resist Hold
(13247, 4116, 6), -- Resist M. Atk.
(13247, 4382, 3), -- Curse of Lake Ghost
-- Caught Sea Giant
(13248, 4295, 1), -- Race
(13248, 4285, 4), -- Resist Sleep
(13248, 4287, 4), -- Resist Hold
(13248, 4116, 6), -- Resist M. Atk.
(13248, 4382, 4), -- Curse of Lake Ghost
-- Caught Sea Horse Soldier
(13249, 4296, 1), -- Race
(13249, 4285, 4), -- Resist Sleep
(13249, 4287, 4), -- Resist Hold
(13249, 4116, 6), -- Resist M. Atk.
(13249, 4382, 5), -- Curse of Lake Ghost
-- Caught Homunculus
(13250, 4291, 1), -- Race
(13250, 4285, 4), -- Resist Sleep
(13250, 4287, 4), -- Resist Hold
(13250, 4116, 6), -- Resist M. Atk.
(13250, 4382, 6), -- Curse of Lake Ghost
-- Caught Flava
(13251, 4294, 1), -- Race
(13251, 4285, 4), -- Resist Sleep
(13251, 4287, 4), -- Resist Hold
(13251, 4116, 6), -- Resist M. Atk.
(13251, 4382, 7), -- Curse of Lake Ghost
-- Caught Gigantic Eye
(13252, 4291, 1), -- Race
(13252, 4285, 4), -- Resist Sleep
(13252, 4287, 4), -- Resist Hold
(13252, 4116, 6), -- Resist M. Atk.
(13252, 4382, 9), -- Curse of Lake Ghost
-- Kat the Cat
(13253, 4293, 1), -- Race
(13253, 4121, 1), -- Summoned Monster Magic Protection
(13253, 4025, 8), -- Master Recharge
-- Kat the Cat
(13254, 4293, 1), -- Race
(13254, 4121, 1), -- Summoned Monster Magic Protection
(13254, 4025, 8), -- Master Recharge
-- Kat the Cat
(13255, 4293, 1), -- Race
(13255, 4121, 1), -- Summoned Monster Magic Protection
(13255, 4025, 8), -- Master Recharge
-- Kat the Cat
(13256, 4293, 1), -- Race
(13256, 4121, 1), -- Summoned Monster Magic Protection
(13256, 4025, 8), -- Master Recharge
-- Kat the Cat
(13257, 4293, 1), -- Race
(13257, 4121, 1), -- Summoned Monster Magic Protection
(13257, 4025, 8), -- Master Recharge
-- Kat the Cat
(13258, 4293, 1), -- Race
(13258, 4121, 1), -- Summoned Monster Magic Protection
(13258, 4025, 8), -- Master Recharge
-- Kat the Cat
(13259, 4293, 1), -- Race
(13259, 4121, 1), -- Summoned Monster Magic Protection
(13259, 4025, 9), -- Master Recharge
-- Kat the Cat
(13260, 4293, 1), -- Race
(13260, 4121, 1), -- Summoned Monster Magic Protection
(13260, 4025, 9), -- Master Recharge
-- Kat the Cat
(13261, 4293, 1), -- Race
(13261, 4121, 1), -- Summoned Monster Magic Protection
(13261, 4025, 9), -- Master Recharge
-- Kat the Cat
(13262, 4293, 1), -- Race
(13262, 4121, 1), -- Summoned Monster Magic Protection
(13262, 4025, 9), -- Master Recharge
-- Kat the Cat
(13263, 4293, 1), -- Race
(13263, 4121, 1), -- Summoned Monster Magic Protection
(13263, 4025, 9), -- Master Recharge
-- Kat the Cat
(13264, 4293, 1), -- Race
(13264, 4121, 1), -- Summoned Monster Magic Protection
(13264, 4025, 9), -- Master Recharge
-- Kat the Cat
(13265, 4293, 1), -- Race
(13265, 4121, 1), -- Summoned Monster Magic Protection
(13265, 4025, 10), -- Master Recharge
-- Kat the Cat
(13266, 4293, 1), -- Race
(13266, 4121, 1), -- Summoned Monster Magic Protection
(13266, 4025, 10), -- Master Recharge
-- Kat the Cat
(13267, 4293, 1), -- Race
(13267, 4121, 1), -- Summoned Monster Magic Protection
(13267, 4025, 10), -- Master Recharge
-- Kat the Cat
(13268, 4293, 1), -- Race
(13268, 4121, 1), -- Summoned Monster Magic Protection
(13268, 4025, 10), -- Master Recharge
-- Kat the Cat
(13269, 4293, 1), -- Race
(13269, 4121, 1), -- Summoned Monster Magic Protection
(13269, 4025, 10), -- Master Recharge
-- Kat the Cat
(13270, 4293, 1), -- Race
(13270, 4121, 1), -- Summoned Monster Magic Protection
(13270, 4025, 10), -- Master Recharge
-- Kat the Cat
(13271, 4293, 1), -- Race
(13271, 4121, 1), -- Summoned Monster Magic Protection
(13271, 4025, 11), -- Master Recharge
-- Kat the Cat
(13272, 4293, 1), -- Race
(13272, 4121, 1), -- Summoned Monster Magic Protection
(13272, 4025, 11), -- Master Recharge
-- Kat the Cat
(13273, 4293, 1), -- Race
(13273, 4121, 1), -- Summoned Monster Magic Protection
(13273, 4025, 11), -- Master Recharge
-- Kat the Cat
(13274, 4293, 1), -- Race
(13274, 4121, 1), -- Summoned Monster Magic Protection
(13274, 4025, 11), -- Master Recharge
-- Kat the Cat
(13275, 4293, 1), -- Race
(13275, 4121, 1), -- Summoned Monster Magic Protection
(13275, 4025, 11), -- Master Recharge
-- Kat the Cat
(13276, 4293, 1), -- Race
(13276, 4121, 1), -- Summoned Monster Magic Protection
(13276, 4025, 11), -- Master Recharge
-- Kat the Cat
(13277, 4293, 1), -- Race
(13277, 4121, 1), -- Summoned Monster Magic Protection
(13277, 4025, 12), -- Master Recharge
-- Kat the Cat
(13278, 4293, 1), -- Race
(13278, 4121, 1), -- Summoned Monster Magic Protection
(13278, 4025, 12), -- Master Recharge
-- Kat the Cat
(13279, 4293, 1), -- Race
(13279, 4121, 1), -- Summoned Monster Magic Protection
(13279, 4025, 12), -- Master Recharge
-- Kat the Cat
(13280, 4293, 1), -- Race
(13280, 4121, 1), -- Summoned Monster Magic Protection
(13280, 4025, 12), -- Master Recharge
-- Kat the Cat
(13281, 4293, 1), -- Race
(13281, 4121, 1), -- Summoned Monster Magic Protection
(13281, 4025, 12), -- Master Recharge
-- Kat the Cat
(13282, 4293, 1), -- Race
(13282, 4121, 1), -- Summoned Monster Magic Protection
(13282, 4025, 12), -- Master Recharge
-- Mew the Cat
(13283, 4293, 1), -- Race
(13283, 4121, 1), -- Summoned Monster Magic Protection
(13283, 4261, 8), -- Mega Storm Strike
-- Mew the Cat
(13284, 4293, 1), -- Race
(13284, 4121, 1), -- Summoned Monster Magic Protection
(13284, 4261, 8), -- Mega Storm Strike
-- Mew the Cat
(13285, 4293, 1), -- Race
(13285, 4121, 1), -- Summoned Monster Magic Protection
(13285, 4261, 8), -- Mega Storm Strike
-- Mew the Cat
(13286, 4293, 1), -- Race
(13286, 4121, 1), -- Summoned Monster Magic Protection
(13286, 4261, 8), -- Mega Storm Strike
-- Mew the Cat
(13287, 4293, 1), -- Race
(13287, 4121, 1), -- Summoned Monster Magic Protection
(13287, 4261, 8), -- Mega Storm Strike
-- Mew the Cat
(13288, 4293, 1), -- Race
(13288, 4121, 1), -- Summoned Monster Magic Protection
(13288, 4261, 8), -- Mega Storm Strike
-- Mew the Cat
(13289, 4293, 1), -- Race
(13289, 4121, 1), -- Summoned Monster Magic Protection
(13289, 4261, 9), -- Mega Storm Strike
-- Mew the Cat
(13290, 4293, 1), -- Race
(13290, 4121, 1), -- Summoned Monster Magic Protection
(13290, 4261, 9), -- Mega Storm Strike
-- Mew the Cat
(13291, 4293, 1), -- Race
(13291, 4121, 1), -- Summoned Monster Magic Protection
(13291, 4261, 9), -- Mega Storm Strike
-- Mew the Cat
(13292, 4293, 1), -- Race
(13292, 4121, 1), -- Summoned Monster Magic Protection
(13292, 4261, 9), -- Mega Storm Strike
-- Mew the Cat
(13293, 4293, 1), -- Race
(13293, 4121, 1), -- Summoned Monster Magic Protection
(13293, 4261, 9), -- Mega Storm Strike
-- Mew the Cat
(13294, 4293, 1), -- Race
(13294, 4121, 1), -- Summoned Monster Magic Protection
(13294, 4261, 9), -- Mega Storm Strike
-- Mew the Cat
(13295, 4293, 1), -- Race
(13295, 4121, 1), -- Summoned Monster Magic Protection
(13295, 4261, 10), -- Mega Storm Strike
-- Mew the Cat
(13296, 4293, 1), -- Race
(13296, 4121, 1), -- Summoned Monster Magic Protection
(13296, 4261, 10), -- Mega Storm Strike
-- Mew the Cat
(13297, 4293, 1), -- Race
(13297, 4121, 1), -- Summoned Monster Magic Protection
(13297, 4261, 10), -- Mega Storm Strike
-- Mew the Cat
(13298, 4293, 1), -- Race
(13298, 4121, 1), -- Summoned Monster Magic Protection
(13298, 4261, 10), -- Mega Storm Strike
-- Mew the Cat
(13299, 4293, 1), -- Race
(13299, 4121, 1), -- Summoned Monster Magic Protection
(13299, 4261, 10), -- Mega Storm Strike
-- Mew the Cat
(13300, 4293, 1), -- Race
(13300, 4121, 1), -- Summoned Monster Magic Protection
(13300, 4261, 10), -- Mega Storm Strike
-- Mew the Cat
(13301, 4293, 1), -- Race
(13301, 4121, 1), -- Summoned Monster Magic Protection
(13301, 4261, 11), -- Mega Storm Strike
-- Mew the Cat
(13302, 4293, 1), -- Race
(13302, 4121, 1), -- Summoned Monster Magic Protection
(13302, 4261, 11), -- Mega Storm Strike
-- Mew the Cat
(13303, 4293, 1), -- Race
(13303, 4121, 1), -- Summoned Monster Magic Protection
(13303, 4261, 11), -- Mega Storm Strike
-- Mew the Cat
(13304, 4293, 1), -- Race
(13304, 4121, 1), -- Summoned Monster Magic Protection
(13304, 4261, 11), -- Mega Storm Strike
-- Mew the Cat
(13305, 4293, 1), -- Race
(13305, 4121, 1), -- Summoned Monster Magic Protection
(13305, 4261, 11), -- Mega Storm Strike
-- Mew the Cat
(13306, 4293, 1), -- Race
(13306, 4121, 1), -- Summoned Monster Magic Protection
(13306, 4261, 11), -- Mega Storm Strike
-- Mew the Cat
(13307, 4293, 1), -- Race
(13307, 4121, 1), -- Summoned Monster Magic Protection
(13307, 4261, 12), -- Mega Storm Strike
-- Mew the Cat
(13308, 4293, 1), -- Race
(13308, 4121, 1), -- Summoned Monster Magic Protection
(13308, 4261, 12), -- Mega Storm Strike
-- Mew the Cat
(13309, 4293, 1), -- Race
(13309, 4121, 1), -- Summoned Monster Magic Protection
(13309, 4261, 12), -- Mega Storm Strike
-- Mew the Cat
(13310, 4293, 1), -- Race
(13310, 4121, 1), -- Summoned Monster Magic Protection
(13310, 4261, 12), -- Mega Storm Strike
-- Mew the Cat
(13311, 4293, 1), -- Race
(13311, 4121, 1), -- Summoned Monster Magic Protection
(13311, 4261, 12), -- Mega Storm Strike
-- Mew the Cat
(13312, 4293, 1), -- Race
(13312, 4121, 1), -- Summoned Monster Magic Protection
(13312, 4261, 12), -- Mega Storm Strike
-- Kai the Cat
(13313, 4293, 1), -- Race
(13313, 4121, 1), -- Summoned Monster Magic Protection
(13313, 4378, 8), -- Self Damage Shield
-- Kai the Cat
(13314, 4293, 1), -- Race
(13314, 4121, 1), -- Summoned Monster Magic Protection
(13314, 4378, 8), -- Self Damage Shield
-- Kai the Cat
(13315, 4293, 1), -- Race
(13315, 4121, 1), -- Summoned Monster Magic Protection
(13315, 4378, 8), -- Self Damage Shield
-- Kai the Cat
(13316, 4293, 1), -- Race
(13316, 4121, 1), -- Summoned Monster Magic Protection
(13316, 4378, 8), -- Self Damage Shield
-- Kai the Cat
(13317, 4293, 1), -- Race
(13317, 4121, 1), -- Summoned Monster Magic Protection
(13317, 4378, 8), -- Self Damage Shield
-- Kai the Cat
(13318, 4293, 1), -- Race
(13318, 4121, 1), -- Summoned Monster Magic Protection
(13318, 4378, 8), -- Self Damage Shield
-- Kai the Cat
(13319, 4293, 1), -- Race
(13319, 4121, 1), -- Summoned Monster Magic Protection
(13319, 4378, 9), -- Self Damage Shield
-- Kai the Cat
(13320, 4293, 1), -- Race
(13320, 4121, 1), -- Summoned Monster Magic Protection
(13320, 4378, 9), -- Self Damage Shield
-- Kai the Cat
(13321, 4293, 1), -- Race
(13321, 4121, 1), -- Summoned Monster Magic Protection
(13321, 4378, 9), -- Self Damage Shield
-- Kai the Cat
(13322, 4293, 1), -- Race
(13322, 4121, 1), -- Summoned Monster Magic Protection
(13322, 4378, 9), -- Self Damage Shield
-- Kai the Cat
(13323, 4293, 1), -- Race
(13323, 4121, 1), -- Summoned Monster Magic Protection
(13323, 4378, 9), -- Self Damage Shield
-- Kai the Cat
(13324, 4293, 1), -- Race
(13324, 4121, 1), -- Summoned Monster Magic Protection
(13324, 4378, 9), -- Self Damage Shield
-- Kai the Cat
(13325, 4293, 1), -- Race
(13325, 4121, 1), -- Summoned Monster Magic Protection
(13325, 4378, 10), -- Self Damage Shield
-- Kai the Cat
(13326, 4293, 1), -- Race
(13326, 4121, 1), -- Summoned Monster Magic Protection
(13326, 4378, 10), -- Self Damage Shield
-- Kai the Cat
(13327, 4293, 1), -- Race
(13327, 4121, 1), -- Summoned Monster Magic Protection
(13327, 4378, 10), -- Self Damage Shield
-- Kai the Cat
(13328, 4293, 1), -- Race
(13328, 4121, 1), -- Summoned Monster Magic Protection
(13328, 4378, 10), -- Self Damage Shield
-- Kai the Cat
(13329, 4293, 1), -- Race
(13329, 4121, 1), -- Summoned Monster Magic Protection
(13329, 4378, 10), -- Self Damage Shield
-- Kai the Cat
(13330, 4293, 1), -- Race
(13330, 4121, 1), -- Summoned Monster Magic Protection
(13330, 4378, 10), -- Self Damage Shield
-- Kai the Cat
(13331, 4293, 1), -- Race
(13331, 4121, 1), -- Summoned Monster Magic Protection
(13331, 4378, 11), -- Self Damage Shield
-- Kai the Cat
(13332, 4293, 1), -- Race
(13332, 4121, 1), -- Summoned Monster Magic Protection
(13332, 4378, 11), -- Self Damage Shield
-- Kai the Cat
(13333, 4293, 1), -- Race
(13333, 4121, 1), -- Summoned Monster Magic Protection
(13333, 4378, 11), -- Self Damage Shield
-- Kai the Cat
(13334, 4293, 1), -- Race
(13334, 4121, 1), -- Summoned Monster Magic Protection
(13334, 4378, 11), -- Self Damage Shield
-- Kai the Cat
(13335, 4293, 1), -- Race
(13335, 4121, 1), -- Summoned Monster Magic Protection
(13335, 4378, 11), -- Self Damage Shield
-- Kai the Cat
(13336, 4293, 1), -- Race
(13336, 4121, 1), -- Summoned Monster Magic Protection
(13336, 4378, 11), -- Self Damage Shield
-- Kai the Cat
(13337, 4293, 1), -- Race
(13337, 4121, 1), -- Summoned Monster Magic Protection
(13337, 4378, 12), -- Self Damage Shield
-- Kai the Cat
(13338, 4293, 1), -- Race
(13338, 4121, 1), -- Summoned Monster Magic Protection
(13338, 4378, 12), -- Self Damage Shield
-- Kai the Cat
(13339, 4293, 1), -- Race
(13339, 4121, 1), -- Summoned Monster Magic Protection
(13339, 4378, 12), -- Self Damage Shield
-- Kai the Cat
(13340, 4293, 1), -- Race
(13340, 4121, 1), -- Summoned Monster Magic Protection
(13340, 4378, 12), -- Self Damage Shield
-- Kai the Cat
(13341, 4293, 1), -- Race
(13341, 4121, 1), -- Summoned Monster Magic Protection
(13341, 4378, 12), -- Self Damage Shield
-- Kai the Cat
(13342, 4293, 1), -- Race
(13342, 4121, 1), -- Summoned Monster Magic Protection
(13342, 4378, 12), -- Self Damage Shield
-- Cat Queen
(13343, 4293, 1), -- Race
(13343, 4121, 1), -- Summoned Monster Magic Protection
(13343, 4699, 4), -- Blessing of Queen
(13343, 4700, 4), -- Gift of Queen
(13343, 4701, 4), -- Cure of Queen
-- Cat Queen
(13344, 4293, 1), -- Race
(13344, 4121, 1), -- Summoned Monster Magic Protection
(13344, 4699, 4), -- Blessing of Queen
(13344, 4700, 4), -- Gift of Queen
(13344, 4701, 4), -- Cure of Queen
-- Cat Queen
(13345, 4293, 1), -- Race
(13345, 4121, 1), -- Summoned Monster Magic Protection
(13345, 4699, 4), -- Blessing of Queen
(13345, 4700, 4), -- Gift of Queen
(13345, 4701, 4), -- Cure of Queen
-- Cat Queen
(13346, 4293, 1), -- Race
(13346, 4121, 1), -- Summoned Monster Magic Protection
(13346, 4699, 5), -- Blessing of Queen
(13346, 4700, 5), -- Gift of Queen
(13346, 4701, 5), -- Cure of Queen
-- Cat Queen
(13347, 4293, 1), -- Race
(13347, 4121, 1), -- Summoned Monster Magic Protection
(13347, 4699, 5), -- Blessing of Queen
(13347, 4700, 5), -- Gift of Queen
(13347, 4701, 5), -- Cure of Queen
-- Cat Queen
(13348, 4293, 1), -- Race
(13348, 4121, 1), -- Summoned Monster Magic Protection
(13348, 4699, 5), -- Blessing of Queen
(13348, 4700, 5), -- Gift of Queen
(13348, 4701, 5), -- Cure of Queen
-- Cat Queen
(13349, 4293, 1), -- Race
(13349, 4121, 1), -- Summoned Monster Magic Protection
(13349, 4699, 6), -- Blessing of Queen
(13349, 4700, 6), -- Gift of Queen
(13349, 4701, 6), -- Cure of Queen
-- Cat Queen
(13350, 4293, 1), -- Race
(13350, 4121, 1), -- Summoned Monster Magic Protection
(13350, 4699, 6), -- Blessing of Queen
(13350, 4700, 6), -- Gift of Queen
(13350, 4701, 6), -- Cure of Queen
-- Cat Queen
(13351, 4293, 1), -- Race
(13351, 4121, 1), -- Summoned Monster Magic Protection
(13351, 4699, 6), -- Blessing of Queen
(13351, 4700, 6), -- Gift of Queen
(13351, 4701, 6), -- Cure of Queen
-- Cat Queen
(13352, 4293, 1), -- Race
(13352, 4121, 1), -- Summoned Monster Magic Protection
(13352, 4699, 7), -- Blessing of Queen
(13352, 4700, 7), -- Gift of Queen
(13352, 4701, 7), -- Cure of Queen
-- Cat Queen
(13353, 4293, 1), -- Race
(13353, 4121, 1), -- Summoned Monster Magic Protection
(13353, 4699, 7), -- Blessing of Queen
(13353, 4700, 7), -- Gift of Queen
(13353, 4701, 7), -- Cure of Queen
-- Cat Queen
(13354, 4293, 1), -- Race
(13354, 4121, 1), -- Summoned Monster Magic Protection
(13354, 4699, 7), -- Blessing of Queen
(13354, 4700, 7), -- Gift of Queen
(13354, 4701, 7), -- Cure of Queen
-- Cat Queen
(13355, 4293, 1), -- Race
(13355, 4121, 1), -- Summoned Monster Magic Protection
(13355, 4699, 8), -- Blessing of Queen
(13355, 4700, 8), -- Gift of Queen
(13355, 4701, 8), -- Cure of Queen
-- Cat Queen
(13356, 4293, 1), -- Race
(13356, 4121, 1), -- Summoned Monster Magic Protection
(13356, 4699, 8), -- Blessing of Queen
(13356, 4700, 8), -- Gift of Queen
(13356, 4701, 8), -- Cure of Queen
-- Cat Queen
(13357, 4293, 1), -- Race
(13357, 4121, 1), -- Summoned Monster Magic Protection
(13357, 4699, 8), -- Blessing of Queen
(13357, 4700, 8), -- Gift of Queen
(13357, 4701, 8), -- Cure of Queen
-- Cat Queen
(13358, 4293, 1), -- Race
(13358, 4121, 1), -- Summoned Monster Magic Protection
(13358, 4699, 9), -- Blessing of Queen
(13358, 4700, 9), -- Gift of Queen
(13358, 4701, 9), -- Cure of Queen
-- Cat Queen
(13359, 4293, 1), -- Race
(13359, 4121, 1), -- Summoned Monster Magic Protection
(13359, 4699, 9), -- Blessing of Queen
(13359, 4700, 9), -- Gift of Queen
(13359, 4701, 9), -- Cure of Queen
-- Cat Queen
(13360, 4293, 1), -- Race
(13360, 4121, 1), -- Summoned Monster Magic Protection
(13360, 4699, 9), -- Blessing of Queen
(13360, 4700, 9), -- Gift of Queen
(13360, 4701, 9), -- Cure of Queen
-- Cat Queen
(13361, 4293, 1), -- Race
(13361, 4121, 1), -- Summoned Monster Magic Protection
(13361, 4699, 10), -- Blessing of Queen
(13361, 4700, 10), -- Gift of Queen
(13361, 4701, 10), -- Cure of Queen
-- Cat Queen
(13362, 4293, 1), -- Race
(13362, 4121, 1), -- Summoned Monster Magic Protection
(13362, 4699, 10), -- Blessing of Queen
(13362, 4700, 10), -- Gift of Queen
(13362, 4701, 10), -- Cure of Queen
-- Cat Queen
(13363, 4293, 1), -- Race
(13363, 4121, 1), -- Summoned Monster Magic Protection
(13363, 4699, 10), -- Blessing of Queen
(13363, 4700, 10), -- Gift of Queen
(13363, 4701, 10), -- Cure of Queen
-- Cat Queen
(13364, 4293, 1), -- Race
(13364, 4121, 1), -- Summoned Monster Magic Protection
(13364, 4699, 11), -- Blessing of Queen
(13364, 4700, 11), -- Gift of Queen
(13364, 4701, 11), -- Cure of Queen
-- Cat Queen
(13365, 4293, 1), -- Race
(13365, 4121, 1), -- Summoned Monster Magic Protection
(13365, 4699, 11), -- Blessing of Queen
(13365, 4700, 11), -- Gift of Queen
(13365, 4701, 11), -- Cure of Queen
-- Cat Queen
(13366, 4293, 1), -- Race
(13366, 4121, 1), -- Summoned Monster Magic Protection
(13366, 4699, 11), -- Blessing of Queen
(13366, 4700, 11), -- Gift of Queen
(13366, 4701, 11), -- Cure of Queen
-- Cat Queen
(13367, 4293, 1), -- Race
(13367, 4121, 1), -- Summoned Monster Magic Protection
(13367, 4699, 12), -- Blessing of Queen
(13367, 4700, 12), -- Gift of Queen
(13367, 4701, 12), -- Cure of Queen
-- Cat Queen
(13368, 4293, 1), -- Race
(13368, 4121, 1), -- Summoned Monster Magic Protection
(13368, 4699, 12), -- Blessing of Queen
(13368, 4700, 12), -- Gift of Queen
(13368, 4701, 12), -- Cure of Queen
-- Cat Queen
(13369, 4293, 1), -- Race
(13369, 4121, 1), -- Summoned Monster Magic Protection
(13369, 4699, 12), -- Blessing of Queen
(13369, 4700, 12), -- Gift of Queen
(13369, 4701, 12), -- Cure of Queen
-- Cat Queen
(13370, 4293, 1), -- Race
(13370, 4121, 1), -- Summoned Monster Magic Protection
(13370, 4699, 13), -- Blessing of Queen
(13370, 4700, 13), -- Gift of Queen
(13370, 4701, 13), -- Cure of Queen
-- Cat Queen
(13371, 4293, 1), -- Race
(13371, 4121, 1), -- Summoned Monster Magic Protection
(13371, 4699, 13), -- Blessing of Queen
(13371, 4700, 13), -- Gift of Queen
(13371, 4701, 13), -- Cure of Queen
-- Cat Queen
(13372, 4293, 1), -- Race
(13372, 4121, 1), -- Summoned Monster Magic Protection
(13372, 4699, 13), -- Blessing of Queen
(13372, 4700, 13), -- Gift of Queen
(13372, 4701, 13), -- Cure of Queen
-- Unicorn Boxer
(13373, 4296, 1), -- Race
(13373, 4121, 1), -- Summoned Monster Magic Protection
(13373, 4025, 8), -- Master Recharge
-- Unicorn Boxer
(13374, 4296, 1), -- Race
(13374, 4121, 1), -- Summoned Monster Magic Protection
(13374, 4025, 8), -- Master Recharge
-- Unicorn Boxer
(13375, 4296, 1), -- Race
(13375, 4121, 1), -- Summoned Monster Magic Protection
(13375, 4025, 8), -- Master Recharge
-- Unicorn Boxer
(13376, 4296, 1), -- Race
(13376, 4121, 1), -- Summoned Monster Magic Protection
(13376, 4025, 8), -- Master Recharge
-- Unicorn Boxer
(13377, 4296, 1), -- Race
(13377, 4121, 1), -- Summoned Monster Magic Protection
(13377, 4025, 8), -- Master Recharge
-- Unicorn Boxer
(13378, 4296, 1), -- Race
(13378, 4121, 1), -- Summoned Monster Magic Protection
(13378, 4025, 8), -- Master Recharge
-- Unicorn Boxer
(13379, 4296, 1), -- Race
(13379, 4121, 1), -- Summoned Monster Magic Protection
(13379, 4025, 9), -- Master Recharge
-- Unicorn Boxer
(13380, 4296, 1), -- Race
(13380, 4121, 1), -- Summoned Monster Magic Protection
(13380, 4025, 9), -- Master Recharge
-- Unicorn Boxer
(13381, 4296, 1), -- Race
(13381, 4121, 1), -- Summoned Monster Magic Protection
(13381, 4025, 9), -- Master Recharge
-- Unicorn Boxer
(13382, 4296, 1), -- Race
(13382, 4121, 1), -- Summoned Monster Magic Protection
(13382, 4025, 9), -- Master Recharge
-- Unicorn Boxer
(13383, 4296, 1), -- Race
(13383, 4121, 1), -- Summoned Monster Magic Protection
(13383, 4025, 9), -- Master Recharge
-- Unicorn Boxer
(13384, 4296, 1), -- Race
(13384, 4121, 1), -- Summoned Monster Magic Protection
(13384, 4025, 9), -- Master Recharge
-- Unicorn Boxer
(13385, 4296, 1), -- Race
(13385, 4121, 1), -- Summoned Monster Magic Protection
(13385, 4025, 10), -- Master Recharge
-- Unicorn Boxer
(13386, 4296, 1), -- Race
(13386, 4121, 1), -- Summoned Monster Magic Protection
(13386, 4025, 10), -- Master Recharge
-- Unicorn Boxer
(13387, 4296, 1), -- Race
(13387, 4121, 1), -- Summoned Monster Magic Protection
(13387, 4025, 10), -- Master Recharge
-- Unicorn Boxer
(13388, 4296, 1), -- Race
(13388, 4121, 1), -- Summoned Monster Magic Protection
(13388, 4025, 10), -- Master Recharge
-- Unicorn Boxer
(13389, 4296, 1), -- Race
(13389, 4121, 1), -- Summoned Monster Magic Protection
(13389, 4025, 10), -- Master Recharge
-- Unicorn Boxer
(13390, 4296, 1), -- Race
(13390, 4121, 1), -- Summoned Monster Magic Protection
(13390, 4025, 10), -- Master Recharge
-- Unicorn Boxer
(13391, 4296, 1), -- Race
(13391, 4121, 1), -- Summoned Monster Magic Protection
(13391, 4025, 11), -- Master Recharge
-- Unicorn Boxer
(13392, 4296, 1), -- Race
(13392, 4121, 1), -- Summoned Monster Magic Protection
(13392, 4025, 11), -- Master Recharge
-- Unicorn Boxer
(13393, 4296, 1), -- Race
(13393, 4121, 1), -- Summoned Monster Magic Protection
(13393, 4025, 11), -- Master Recharge
-- Unicorn Boxer
(13394, 4296, 1), -- Race
(13394, 4121, 1), -- Summoned Monster Magic Protection
(13394, 4025, 11), -- Master Recharge
-- Unicorn Boxer
(13395, 4296, 1), -- Race
(13395, 4121, 1), -- Summoned Monster Magic Protection
(13395, 4025, 11), -- Master Recharge
-- Unicorn Boxer
(13396, 4296, 1), -- Race
(13396, 4121, 1), -- Summoned Monster Magic Protection
(13396, 4025, 11), -- Master Recharge
-- Unicorn Boxer
(13397, 4296, 1), -- Race
(13397, 4121, 1), -- Summoned Monster Magic Protection
(13397, 4025, 12), -- Master Recharge
-- Unicorn Boxer
(13398, 4296, 1), -- Race
(13398, 4121, 1), -- Summoned Monster Magic Protection
(13398, 4025, 12), -- Master Recharge
-- Unicorn Boxer
(13399, 4296, 1), -- Race
(13399, 4121, 1), -- Summoned Monster Magic Protection
(13399, 4025, 12), -- Master Recharge
-- Unicorn Boxer
(13400, 4296, 1), -- Race
(13400, 4121, 1), -- Summoned Monster Magic Protection
(13400, 4025, 12), -- Master Recharge
-- Unicorn Boxer
(13401, 4296, 1), -- Race
(13401, 4121, 1), -- Summoned Monster Magic Protection
(13401, 4025, 12), -- Master Recharge
-- Unicorn Boxer
(13402, 4296, 1), -- Race
(13402, 4121, 1), -- Summoned Monster Magic Protection
(13402, 4025, 12), -- Master Recharge
-- Unicorn Mirage 
(13403, 4296, 1), -- Race
(13403, 4121, 1), -- Summoned Monster Magic Protection
(13403, 4261, 8), -- Mega Storm Strike
-- Unicorn Mirage 
(13404, 4296, 1), -- Race
(13404, 4121, 1), -- Summoned Monster Magic Protection
(13404, 4261, 8), -- Mega Storm Strike
-- Unicorn Mirage 
(13405, 4296, 1), -- Race
(13405, 4121, 1), -- Summoned Monster Magic Protection
(13405, 4261, 8), -- Mega Storm Strike
-- Unicorn Mirage 
(13406, 4296, 1), -- Race
(13406, 4121, 1), -- Summoned Monster Magic Protection
(13406, 4261, 8), -- Mega Storm Strike
-- Unicorn Mirage 
(13407, 4296, 1), -- Race
(13407, 4121, 1), -- Summoned Monster Magic Protection
(13407, 4261, 8), -- Mega Storm Strike
-- Unicorn Mirage 
(13408, 4296, 1), -- Race
(13408, 4121, 1), -- Summoned Monster Magic Protection
(13408, 4261, 8), -- Mega Storm Strike
-- Unicorn Mirage 
(13409, 4296, 1), -- Race
(13409, 4121, 1), -- Summoned Monster Magic Protection
(13409, 4261, 9), -- Mega Storm Strike
-- Unicorn Mirage 
(13410, 4296, 1), -- Race
(13410, 4121, 1), -- Summoned Monster Magic Protection
(13410, 4261, 9), -- Mega Storm Strike
-- Unicorn Mirage 
(13411, 4296, 1), -- Race
(13411, 4121, 1), -- Summoned Monster Magic Protection
(13411, 4261, 9), -- Mega Storm Strike
-- Unicorn Mirage 
(13412, 4296, 1), -- Race
(13412, 4121, 1), -- Summoned Monster Magic Protection
(13412, 4261, 9), -- Mega Storm Strike
-- Unicorn Mirage 
(13413, 4296, 1), -- Race
(13413, 4121, 1), -- Summoned Monster Magic Protection
(13413, 4261, 9), -- Mega Storm Strike
-- Unicorn Mirage 
(13414, 4296, 1), -- Race
(13414, 4121, 1), -- Summoned Monster Magic Protection
(13414, 4261, 9), -- Mega Storm Strike
-- Unicorn Mirage 
(13415, 4296, 1), -- Race
(13415, 4121, 1), -- Summoned Monster Magic Protection
(13415, 4261, 10), -- Mega Storm Strike
-- Unicorn Mirage 
(13416, 4296, 1), -- Race
(13416, 4121, 1), -- Summoned Monster Magic Protection
(13416, 4261, 10), -- Mega Storm Strike
-- Unicorn Mirage 
(13417, 4296, 1), -- Race
(13417, 4121, 1), -- Summoned Monster Magic Protection
(13417, 4261, 10), -- Mega Storm Strike
-- Unicorn Mirage 
(13418, 4296, 1), -- Race
(13418, 4121, 1), -- Summoned Monster Magic Protection
(13418, 4261, 10), -- Mega Storm Strike
-- Unicorn Mirage 
(13419, 4296, 1), -- Race
(13419, 4121, 1), -- Summoned Monster Magic Protection
(13419, 4261, 10), -- Mega Storm Strike
-- Unicorn Mirage 
(13420, 4296, 1), -- Race
(13420, 4121, 1), -- Summoned Monster Magic Protection
(13420, 4261, 10), -- Mega Storm Strike
-- Unicorn Mirage 
(13421, 4296, 1), -- Race
(13421, 4121, 1), -- Summoned Monster Magic Protection
(13421, 4261, 11), -- Mega Storm Strike
-- Unicorn Mirage 
(13422, 4296, 1), -- Race
(13422, 4121, 1), -- Summoned Monster Magic Protection
(13422, 4261, 11), -- Mega Storm Strike
-- Unicorn Mirage 
(13423, 4296, 1), -- Race
(13423, 4121, 1), -- Summoned Monster Magic Protection
(13423, 4261, 11), -- Mega Storm Strike
-- Unicorn Mirage 
(13424, 4296, 1), -- Race
(13424, 4121, 1), -- Summoned Monster Magic Protection
(13424, 4261, 11), -- Mega Storm Strike
-- Unicorn Mirage 
(13425, 4296, 1), -- Race
(13425, 4121, 1), -- Summoned Monster Magic Protection
(13425, 4261, 11), -- Mega Storm Strike
-- Unicorn Mirage 
(13426, 4296, 1), -- Race
(13426, 4121, 1), -- Summoned Monster Magic Protection
(13426, 4261, 11), -- Mega Storm Strike
-- Unicorn Mirage 
(13427, 4296, 1), -- Race
(13427, 4121, 1), -- Summoned Monster Magic Protection
(13427, 4261, 12), -- Mega Storm Strike
-- Unicorn Mirage 
(13428, 4296, 1), -- Race
(13428, 4121, 1), -- Summoned Monster Magic Protection
(13428, 4261, 12), -- Mega Storm Strike
-- Unicorn Mirage 
(13429, 4296, 1), -- Race
(13429, 4121, 1), -- Summoned Monster Magic Protection
(13429, 4261, 12), -- Mega Storm Strike
-- Unicorn Mirage 
(13430, 4296, 1), -- Race
(13430, 4121, 1), -- Summoned Monster Magic Protection
(13430, 4261, 12), -- Mega Storm Strike
-- Unicorn Mirage 
(13431, 4296, 1), -- Race
(13431, 4121, 1), -- Summoned Monster Magic Protection
(13431, 4261, 12), -- Mega Storm Strike
-- Unicorn Mirage 
(13432, 4296, 1), -- Race
(13432, 4121, 1), -- Summoned Monster Magic Protection
(13432, 4261, 12), -- Mega Storm Strike
-- Unicorn Merrow
(13433, 4296, 1), -- Race
(13433, 4121, 1), -- Summoned Monster Magic Protection
(13433, 4137, 8), -- Hydro Screw
-- Unicorn Merrow
(13434, 4296, 1), -- Race
(13434, 4121, 1), -- Summoned Monster Magic Protection
(13434, 4137, 8), -- Hydro Screw
-- Unicorn Merrow
(13435, 4296, 1), -- Race
(13435, 4121, 1), -- Summoned Monster Magic Protection
(13435, 4137, 8), -- Hydro Screw
-- Unicorn Merrow
(13436, 4296, 1), -- Race
(13436, 4121, 1), -- Summoned Monster Magic Protection
(13436, 4137, 8), -- Hydro Screw
-- Unicorn Merrow
(13437, 4296, 1), -- Race
(13437, 4121, 1), -- Summoned Monster Magic Protection
(13437, 4137, 8), -- Hydro Screw
-- Unicorn Merrow
(13438, 4296, 1), -- Race
(13438, 4121, 1), -- Summoned Monster Magic Protection
(13438, 4137, 8), -- Hydro Screw
-- Unicorn Merrow
(13439, 4296, 1), -- Race
(13439, 4121, 1), -- Summoned Monster Magic Protection
(13439, 4137, 9), -- Hydro Screw
-- Unicorn Merrow
(13440, 4296, 1), -- Race
(13440, 4121, 1), -- Summoned Monster Magic Protection
(13440, 4137, 9), -- Hydro Screw
-- Unicorn Merrow
(13441, 4296, 1), -- Race
(13441, 4121, 1), -- Summoned Monster Magic Protection
(13441, 4137, 9), -- Hydro Screw
-- Unicorn Merrow
(13442, 4296, 1), -- Race
(13442, 4121, 1), -- Summoned Monster Magic Protection
(13442, 4137, 9), -- Hydro Screw
-- Unicorn Merrow
(13443, 4296, 1), -- Race
(13443, 4121, 1), -- Summoned Monster Magic Protection
(13443, 4137, 9), -- Hydro Screw
-- Unicorn Merrow
(13444, 4296, 1), -- Race
(13444, 4121, 1), -- Summoned Monster Magic Protection
(13444, 4137, 9), -- Hydro Screw
-- Unicorn Merrow
(13445, 4296, 1), -- Race
(13445, 4121, 1), -- Summoned Monster Magic Protection
(13445, 4137, 10), -- Hydro Screw
-- Unicorn Merrow
(13446, 4296, 1), -- Race
(13446, 4121, 1), -- Summoned Monster Magic Protection
(13446, 4137, 10), -- Hydro Screw
-- Unicorn Merrow
(13447, 4296, 1), -- Race
(13447, 4121, 1), -- Summoned Monster Magic Protection
(13447, 4137, 10), -- Hydro Screw
-- Unicorn Merrow
(13448, 4296, 1), -- Race
(13448, 4121, 1), -- Summoned Monster Magic Protection
(13448, 4137, 10), -- Hydro Screw
-- Unicorn Merrow
(13449, 4296, 1), -- Race
(13449, 4121, 1), -- Summoned Monster Magic Protection
(13449, 4137, 10), -- Hydro Screw
-- Unicorn Merrow
(13450, 4296, 1), -- Race
(13450, 4121, 1), -- Summoned Monster Magic Protection
(13450, 4137, 10), -- Hydro Screw
-- Unicorn Merrow
(13451, 4296, 1), -- Race
(13451, 4121, 1), -- Summoned Monster Magic Protection
(13451, 4137, 11), -- Hydro Screw
-- Unicorn Merrow
(13452, 4296, 1), -- Race
(13452, 4121, 1), -- Summoned Monster Magic Protection
(13452, 4137, 11), -- Hydro Screw
-- Unicorn Merrow
(13453, 4296, 1), -- Race
(13453, 4121, 1), -- Summoned Monster Magic Protection
(13453, 4137, 11), -- Hydro Screw
-- Unicorn Merrow
(13454, 4296, 1), -- Race
(13454, 4121, 1), -- Summoned Monster Magic Protection
(13454, 4137, 11), -- Hydro Screw
-- Unicorn Merrow
(13455, 4296, 1), -- Race
(13455, 4121, 1), -- Summoned Monster Magic Protection
(13455, 4137, 11), -- Hydro Screw
-- Unicorn Merrow
(13456, 4296, 1), -- Race
(13456, 4121, 1), -- Summoned Monster Magic Protection
(13456, 4137, 11), -- Hydro Screw
-- Unicorn Merrow
(13457, 4296, 1), -- Race
(13457, 4121, 1), -- Summoned Monster Magic Protection
(13457, 4137, 12), -- Hydro Screw
-- Unicorn Merrow
(13458, 4296, 1), -- Race
(13458, 4121, 1), -- Summoned Monster Magic Protection
(13458, 4137, 12), -- Hydro Screw
-- Unicorn Merrow
(13459, 4296, 1), -- Race
(13459, 4121, 1), -- Summoned Monster Magic Protection
(13459, 4137, 12), -- Hydro Screw
-- Unicorn Merrow
(13460, 4296, 1), -- Race
(13460, 4121, 1), -- Summoned Monster Magic Protection
(13460, 4137, 12), -- Hydro Screw
-- Unicorn Merrow
(13461, 4296, 1), -- Race
(13461, 4121, 1), -- Summoned Monster Magic Protection
(13461, 4137, 12), -- Hydro Screw
-- Unicorn Merrow
(13462, 4296, 1), -- Race
(13462, 4121, 1), -- Summoned Monster Magic Protection
(13462, 4137, 12), -- Hydro Screw
-- Unicorn Seraphim
(13463, 4296, 1), -- Race
(13463, 4121, 1), -- Summoned Monster Magic Protection
(13463, 4702, 4), -- Blessing of Seraphim
(13463, 4703, 4), -- Gift of Seraphim
(13463, 4704, 4), -- Cure of Seraphim
-- Unicorn Seraphim
(13464, 4296, 1), -- Race
(13464, 4121, 1), -- Summoned Monster Magic Protection
(13464, 4702, 4), -- Blessing of Seraphim
(13464, 4703, 4), -- Gift of Seraphim
(13464, 4704, 4), -- Cure of Seraphim
-- Unicorn Seraphim
(13465, 4296, 1), -- Race
(13465, 4121, 1), -- Summoned Monster Magic Protection
(13465, 4702, 4), -- Blessing of Seraphim
(13465, 4703, 4), -- Gift of Seraphim
(13465, 4704, 4), -- Cure of Seraphim
-- Unicorn Seraphim
(13466, 4296, 1), -- Race
(13466, 4121, 1), -- Summoned Monster Magic Protection
(13466, 4702, 5), -- Blessing of Seraphim
(13466, 4703, 5), -- Gift of Seraphim
(13466, 4704, 5), -- Cure of Seraphim
-- Unicorn Seraphim
(13467, 4296, 1), -- Race
(13467, 4121, 1), -- Summoned Monster Magic Protection
(13467, 4702, 5), -- Blessing of Seraphim
(13467, 4703, 5), -- Gift of Seraphim
(13467, 4704, 5), -- Cure of Seraphim
-- Unicorn Seraphim
(13468, 4296, 1), -- Race
(13468, 4121, 1), -- Summoned Monster Magic Protection
(13468, 4702, 5), -- Blessing of Seraphim
(13468, 4703, 5), -- Gift of Seraphim
(13468, 4704, 5), -- Cure of Seraphim
-- Unicorn Seraphim
(13469, 4296, 1), -- Race
(13469, 4121, 1), -- Summoned Monster Magic Protection
(13469, 4702, 6), -- Blessing of Seraphim
(13469, 4703, 6), -- Gift of Seraphim
(13469, 4704, 6), -- Cure of Seraphim
-- Unicorn Seraphim
(13470, 4296, 1), -- Race
(13470, 4121, 1), -- Summoned Monster Magic Protection
(13470, 4702, 6), -- Blessing of Seraphim
(13470, 4703, 6), -- Gift of Seraphim
(13470, 4704, 6), -- Cure of Seraphim
-- Unicorn Seraphim
(13471, 4296, 1), -- Race
(13471, 4121, 1), -- Summoned Monster Magic Protection
(13471, 4702, 6), -- Blessing of Seraphim
(13471, 4703, 6), -- Gift of Seraphim
(13471, 4704, 6), -- Cure of Seraphim
-- Unicorn Seraphim
(13472, 4296, 1), -- Race
(13472, 4121, 1), -- Summoned Monster Magic Protection
(13472, 4702, 7), -- Blessing of Seraphim
(13472, 4703, 7), -- Gift of Seraphim
(13472, 4704, 7), -- Cure of Seraphim
-- Unicorn Seraphim
(13473, 4296, 1), -- Race
(13473, 4121, 1), -- Summoned Monster Magic Protection
(13473, 4702, 7), -- Blessing of Seraphim
(13473, 4703, 7), -- Gift of Seraphim
(13473, 4704, 7), -- Cure of Seraphim
-- Unicorn Seraphim
(13474, 4296, 1), -- Race
(13474, 4121, 1), -- Summoned Monster Magic Protection
(13474, 4702, 7), -- Blessing of Seraphim
(13474, 4703, 7), -- Gift of Seraphim
(13474, 4704, 7), -- Cure of Seraphim
-- Unicorn Seraphim
(13475, 4296, 1), -- Race
(13475, 4121, 1), -- Summoned Monster Magic Protection
(13475, 4702, 8), -- Blessing of Seraphim
(13475, 4703, 8), -- Gift of Seraphim
(13475, 4704, 8), -- Cure of Seraphim
-- Unicorn Seraphim
(13476, 4296, 1), -- Race
(13476, 4121, 1), -- Summoned Monster Magic Protection
(13476, 4702, 8), -- Blessing of Seraphim
(13476, 4703, 8), -- Gift of Seraphim
(13476, 4704, 8), -- Cure of Seraphim
-- Unicorn Seraphim
(13477, 4296, 1), -- Race
(13477, 4121, 1), -- Summoned Monster Magic Protection
(13477, 4702, 8), -- Blessing of Seraphim
(13477, 4703, 8), -- Gift of Seraphim
(13477, 4704, 8), -- Cure of Seraphim
-- Unicorn Seraphim
(13478, 4296, 1), -- Race
(13478, 4121, 1), -- Summoned Monster Magic Protection
(13478, 4702, 9), -- Blessing of Seraphim
(13478, 4703, 9), -- Gift of Seraphim
(13478, 4704, 9), -- Cure of Seraphim
-- Unicorn Seraphim
(13479, 4296, 1), -- Race
(13479, 4121, 1), -- Summoned Monster Magic Protection
(13479, 4702, 9), -- Blessing of Seraphim
(13479, 4703, 9), -- Gift of Seraphim
(13479, 4704, 9), -- Cure of Seraphim
-- Unicorn Seraphim
(13480, 4296, 1), -- Race
(13480, 4121, 1), -- Summoned Monster Magic Protection
(13480, 4702, 9), -- Blessing of Seraphim
(13480, 4703, 9), -- Gift of Seraphim
(13480, 4704, 9), -- Cure of Seraphim
-- Unicorn Seraphim
(13481, 4296, 1), -- Race
(13481, 4121, 1), -- Summoned Monster Magic Protection
(13481, 4702, 10), -- Blessing of Seraphim
(13481, 4703, 10), -- Gift of Seraphim
(13481, 4704, 10), -- Cure of Seraphim
-- Unicorn Seraphim
(13482, 4296, 1), -- Race
(13482, 4121, 1), -- Summoned Monster Magic Protection
(13482, 4702, 10), -- Blessing of Seraphim
(13482, 4703, 10), -- Gift of Seraphim
(13482, 4704, 10), -- Cure of Seraphim
-- Unicorn Seraphim
(13483, 4296, 1), -- Race
(13483, 4121, 1), -- Summoned Monster Magic Protection
(13483, 4702, 10), -- Blessing of Seraphim
(13483, 4703, 10), -- Gift of Seraphim
(13483, 4704, 10), -- Cure of Seraphim
-- Unicorn Seraphim
(13484, 4296, 1), -- Race
(13484, 4121, 1), -- Summoned Monster Magic Protection
(13484, 4702, 11), -- Blessing of Seraphim
(13484, 4703, 11), -- Gift of Seraphim
(13484, 4704, 11), -- Cure of Seraphim
-- Unicorn Seraphim
(13485, 4296, 1), -- Race
(13485, 4121, 1), -- Summoned Monster Magic Protection
(13485, 4702, 11), -- Blessing of Seraphim
(13485, 4703, 11), -- Gift of Seraphim
(13485, 4704, 11), -- Cure of Seraphim
-- Unicorn Seraphim
(13486, 4296, 1), -- Race
(13486, 4121, 1), -- Summoned Monster Magic Protection
(13486, 4702, 11), -- Blessing of Seraphim
(13486, 4703, 11), -- Gift of Seraphim
(13486, 4704, 11), -- Cure of Seraphim
-- Unicorn Seraphim
(13487, 4296, 1), -- Race
(13487, 4121, 1), -- Summoned Monster Magic Protection
(13487, 4702, 12), -- Blessing of Seraphim
(13487, 4703, 12), -- Gift of Seraphim
(13487, 4704, 12), -- Cure of Seraphim
-- Unicorn Seraphim
(13488, 4296, 1), -- Race
(13488, 4121, 1), -- Summoned Monster Magic Protection
(13488, 4702, 12), -- Blessing of Seraphim
(13488, 4703, 12), -- Gift of Seraphim
(13488, 4704, 12), -- Cure of Seraphim
-- Unicorn Seraphim
(13489, 4296, 1), -- Race
(13489, 4121, 1), -- Summoned Monster Magic Protection
(13489, 4702, 12), -- Blessing of Seraphim
(13489, 4703, 12), -- Gift of Seraphim
(13489, 4704, 12), -- Cure of Seraphim
-- Unicorn Seraphim
(13490, 4296, 1), -- Race
(13490, 4121, 1), -- Summoned Monster Magic Protection
(13490, 4702, 13), -- Blessing of Seraphim
(13490, 4703, 13), -- Gift of Seraphim
(13490, 4704, 13), -- Cure of Seraphim
-- Unicorn Seraphim
(13491, 4296, 1), -- Race
(13491, 4121, 1), -- Summoned Monster Magic Protection
(13491, 4702, 13), -- Blessing of Seraphim
(13491, 4703, 13), -- Gift of Seraphim
(13491, 4704, 13), -- Cure of Seraphim
-- Unicorn Seraphim
(13492, 4296, 1), -- Race
(13492, 4121, 1), -- Summoned Monster Magic Protection
(13492, 4702, 13), -- Blessing of Seraphim
(13492, 4703, 13), -- Gift of Seraphim
(13492, 4704, 13), -- Cure of Seraphim
-- Shadow
(13493, 4298, 1), -- Race
(13493, 4121, 1), -- Summoned Monster Magic Protection
(13493, 4233, 1), -- Vampiric Attack
-- Shadow
(13494, 4298, 1), -- Race
(13494, 4121, 1), -- Summoned Monster Magic Protection
(13494, 4233, 1), -- Vampiric Attack
-- Shadow
(13495, 4298, 1), -- Race
(13495, 4121, 1), -- Summoned Monster Magic Protection
(13495, 4233, 1), -- Vampiric Attack
-- Shadow
(13496, 4298, 1), -- Race
(13496, 4121, 1), -- Summoned Monster Magic Protection
(13496, 4233, 1), -- Vampiric Attack
-- Shadow
(13497, 4298, 1), -- Race
(13497, 4121, 1), -- Summoned Monster Magic Protection
(13497, 4233, 1), -- Vampiric Attack
-- Shadow
(13498, 4298, 1), -- Race
(13498, 4121, 1), -- Summoned Monster Magic Protection
(13498, 4233, 1), -- Vampiric Attack
-- Shadow
(13499, 4298, 1), -- Race
(13499, 4121, 1), -- Summoned Monster Magic Protection
(13499, 4233, 1), -- Vampiric Attack
-- Shadow
(13500, 4298, 1), -- Race
(13500, 4121, 1), -- Summoned Monster Magic Protection
(13500, 4233, 1), -- Vampiric Attack
-- Shadow
(13501, 4298, 1), -- Race
(13501, 4121, 1), -- Summoned Monster Magic Protection
(13501, 4233, 1), -- Vampiric Attack
-- Shadow
(13502, 4298, 1), -- Race
(13502, 4121, 1), -- Summoned Monster Magic Protection
(13502, 4233, 1), -- Vampiric Attack
-- Shadow
(13503, 4298, 1), -- Race
(13503, 4121, 1), -- Summoned Monster Magic Protection
(13503, 4233, 1), -- Vampiric Attack
-- Shadow
(13504, 4298, 1), -- Race
(13504, 4121, 1), -- Summoned Monster Magic Protection
(13504, 4233, 1), -- Vampiric Attack
-- Shadow
(13505, 4298, 1), -- Race
(13505, 4121, 1), -- Summoned Monster Magic Protection
(13505, 4233, 1), -- Vampiric Attack
-- Shadow
(13506, 4298, 1), -- Race
(13506, 4121, 1), -- Summoned Monster Magic Protection
(13506, 4233, 1), -- Vampiric Attack
-- Shadow
(13507, 4298, 1), -- Race
(13507, 4121, 1), -- Summoned Monster Magic Protection
(13507, 4233, 1), -- Vampiric Attack
-- Shadow
(13508, 4298, 1), -- Race
(13508, 4121, 1), -- Summoned Monster Magic Protection
(13508, 4233, 1), -- Vampiric Attack
-- Shadow
(13509, 4298, 1), -- Race
(13509, 4121, 1), -- Summoned Monster Magic Protection
(13509, 4233, 1), -- Vampiric Attack
-- Shadow
(13510, 4298, 1), -- Race
(13510, 4121, 1), -- Summoned Monster Magic Protection
(13510, 4233, 1), -- Vampiric Attack
-- Shadow
(13511, 4298, 1), -- Race
(13511, 4121, 1), -- Summoned Monster Magic Protection
(13511, 4233, 1), -- Vampiric Attack
-- Shadow
(13512, 4298, 1), -- Race
(13512, 4121, 1), -- Summoned Monster Magic Protection
(13512, 4233, 1), -- Vampiric Attack
-- Shadow
(13513, 4298, 1), -- Race
(13513, 4121, 1), -- Summoned Monster Magic Protection
(13513, 4233, 1), -- Vampiric Attack
-- Shadow
(13514, 4298, 1), -- Race
(13514, 4121, 1), -- Summoned Monster Magic Protection
(13514, 4233, 1), -- Vampiric Attack
-- Shadow
(13515, 4298, 1), -- Race
(13515, 4121, 1), -- Summoned Monster Magic Protection
(13515, 4233, 1), -- Vampiric Attack
-- Shadow
(13516, 4298, 1), -- Race
(13516, 4121, 1), -- Summoned Monster Magic Protection
(13516, 4233, 1), -- Vampiric Attack
-- Shadow
(13517, 4298, 1), -- Race
(13517, 4121, 1), -- Summoned Monster Magic Protection
(13517, 4233, 1), -- Vampiric Attack
-- Shadow
(13518, 4298, 1), -- Race
(13518, 4121, 1), -- Summoned Monster Magic Protection
(13518, 4233, 1), -- Vampiric Attack
-- Shadow
(13519, 4298, 1), -- Race
(13519, 4121, 1), -- Summoned Monster Magic Protection
(13519, 4233, 1), -- Vampiric Attack
-- Shadow
(13520, 4298, 1), -- Race
(13520, 4121, 1), -- Summoned Monster Magic Protection
(13520, 4233, 1), -- Vampiric Attack
-- Shadow
(13521, 4298, 1), -- Race
(13521, 4121, 1), -- Summoned Monster Magic Protection
(13521, 4233, 1), -- Vampiric Attack
-- Shadow
(13522, 4298, 1), -- Race
(13522, 4121, 1), -- Summoned Monster Magic Protection
(13522, 4233, 1), -- Vampiric Attack
-- Silhouette
(13523, 4298, 1), -- Race
(13523, 4121, 1), -- Summoned Monster Magic Protection
(13523, 4260, 8), -- Steal Blood
-- Silhouette
(13524, 4298, 1), -- Race
(13524, 4121, 1), -- Summoned Monster Magic Protection
(13524, 4260, 8), -- Steal Blood
-- Silhouette
(13525, 4298, 1), -- Race
(13525, 4121, 1), -- Summoned Monster Magic Protection
(13525, 4260, 8), -- Steal Blood
-- Silhouette
(13526, 4298, 1), -- Race
(13526, 4121, 1), -- Summoned Monster Magic Protection
(13526, 4260, 8), -- Steal Blood
-- Silhouette
(13527, 4298, 1), -- Race
(13527, 4121, 1), -- Summoned Monster Magic Protection
(13527, 4260, 8), -- Steal Blood
-- Silhouette
(13528, 4298, 1), -- Race
(13528, 4121, 1), -- Summoned Monster Magic Protection
(13528, 4260, 8), -- Steal Blood
-- Silhouette
(13529, 4298, 1), -- Race
(13529, 4121, 1), -- Summoned Monster Magic Protection
(13529, 4260, 9), -- Steal Blood
-- Silhouette
(13530, 4298, 1), -- Race
(13530, 4121, 1), -- Summoned Monster Magic Protection
(13530, 4260, 9), -- Steal Blood
-- Silhouette
(13531, 4298, 1), -- Race
(13531, 4121, 1), -- Summoned Monster Magic Protection
(13531, 4260, 9), -- Steal Blood
-- Silhouette
(13532, 4298, 1), -- Race
(13532, 4121, 1), -- Summoned Monster Magic Protection
(13532, 4260, 9), -- Steal Blood
-- Silhouette
(13533, 4298, 1), -- Race
(13533, 4121, 1), -- Summoned Monster Magic Protection
(13533, 4260, 9), -- Steal Blood
-- Silhouette
(13534, 4298, 1), -- Race
(13534, 4121, 1), -- Summoned Monster Magic Protection
(13534, 4260, 9), -- Steal Blood
-- Silhouette
(13535, 4298, 1), -- Race
(13535, 4121, 1), -- Summoned Monster Magic Protection
(13535, 4260, 10), -- Steal Blood
-- Silhouette
(13536, 4298, 1), -- Race
(13536, 4121, 1), -- Summoned Monster Magic Protection
(13536, 4260, 10), -- Steal Blood
-- Silhouette
(13537, 4298, 1), -- Race
(13537, 4121, 1), -- Summoned Monster Magic Protection
(13537, 4260, 10), -- Steal Blood
-- Silhouette
(13538, 4298, 1), -- Race
(13538, 4121, 1), -- Summoned Monster Magic Protection
(13538, 4260, 10), -- Steal Blood
-- Silhouette
(13539, 4298, 1), -- Race
(13539, 4121, 1), -- Summoned Monster Magic Protection
(13539, 4260, 10), -- Steal Blood
-- Silhouette
(13540, 4298, 1), -- Race
(13540, 4121, 1), -- Summoned Monster Magic Protection
(13540, 4260, 10), -- Steal Blood
-- Silhouette
(13541, 4298, 1), -- Race
(13541, 4121, 1), -- Summoned Monster Magic Protection
(13541, 4260, 11), -- Steal Blood
-- Silhouette
(13542, 4298, 1), -- Race
(13542, 4121, 1), -- Summoned Monster Magic Protection
(13542, 4260, 11), -- Steal Blood
-- Silhouette
(13543, 4298, 1), -- Race
(13543, 4121, 1), -- Summoned Monster Magic Protection
(13543, 4260, 11), -- Steal Blood
-- Silhouette
(13544, 4298, 1), -- Race
(13544, 4121, 1), -- Summoned Monster Magic Protection
(13544, 4260, 11), -- Steal Blood
-- Silhouette
(13545, 4298, 1), -- Race
(13545, 4121, 1), -- Summoned Monster Magic Protection
(13545, 4260, 11), -- Steal Blood
-- Silhouette
(13546, 4298, 1), -- Race
(13546, 4121, 1), -- Summoned Monster Magic Protection
(13546, 4260, 11), -- Steal Blood
-- Silhouette
(13547, 4298, 1), -- Race
(13547, 4121, 1), -- Summoned Monster Magic Protection
(13547, 4260, 12), -- Steal Blood
-- Silhouette
(13548, 4298, 1), -- Race
(13548, 4121, 1), -- Summoned Monster Magic Protection
(13548, 4260, 12), -- Steal Blood
-- Silhouette
(13549, 4298, 1), -- Race
(13549, 4121, 1), -- Summoned Monster Magic Protection
(13549, 4260, 12), -- Steal Blood
-- Silhouette
(13550, 4298, 1), -- Race
(13550, 4121, 1), -- Summoned Monster Magic Protection
(13550, 4260, 12), -- Steal Blood
-- Silhouette
(13551, 4298, 1), -- Race
(13551, 4121, 1), -- Summoned Monster Magic Protection
(13551, 4260, 12), -- Steal Blood
-- Silhouette
(13552, 4298, 1), -- Race
(13552, 4121, 1), -- Summoned Monster Magic Protection
(13552, 4260, 12), -- Steal Blood
-- Soulless
(13553, 4298, 1), -- Race
(13553, 4121, 1), -- Summoned Monster Magic Protection
(13553, 4138, 8), -- NPC AE - Corpse Burst
(13553, 4259, 8), -- Toxic Smoke
(13553, 4140, 8), -- Contract Payment
-- Soulless
(13554, 4298, 1), -- Race
(13554, 4121, 1), -- Summoned Monster Magic Protection
(13554, 4138, 8), -- NPC AE - Corpse Burst
(13554, 4259, 8), -- Toxic Smoke
(13554, 4140, 8), -- Contract Payment
-- Soulless
(13555, 4298, 1), -- Race
(13555, 4121, 1), -- Summoned Monster Magic Protection
(13555, 4138, 8), -- NPC AE - Corpse Burst
(13555, 4259, 8), -- Toxic Smoke
(13555, 4140, 8), -- Contract Payment
-- Soulless
(13556, 4298, 1), -- Race
(13556, 4121, 1), -- Summoned Monster Magic Protection
(13556, 4138, 8), -- NPC AE - Corpse Burst
(13556, 4259, 8), -- Toxic Smoke
(13556, 4140, 8), -- Contract Payment
-- Soulless
(13557, 4298, 1), -- Race
(13557, 4121, 1), -- Summoned Monster Magic Protection
(13557, 4138, 8), -- NPC AE - Corpse Burst
(13557, 4259, 8), -- Toxic Smoke
(13557, 4140, 8), -- Contract Payment
-- Soulless
(13558, 4298, 1), -- Race
(13558, 4121, 1), -- Summoned Monster Magic Protection
(13558, 4138, 8), -- NPC AE - Corpse Burst
(13558, 4259, 8), -- Toxic Smoke
(13558, 4140, 8), -- Contract Payment
-- Soulless
(13559, 4298, 1), -- Race
(13559, 4121, 1), -- Summoned Monster Magic Protection
(13559, 4138, 9), -- NPC AE - Corpse Burst
(13559, 4259, 9), -- Toxic Smoke
(13559, 4140, 9), -- Contract Payment
-- Soulless
(13560, 4298, 1), -- Race
(13560, 4121, 1), -- Summoned Monster Magic Protection
(13560, 4138, 9), -- NPC AE - Corpse Burst
(13560, 4259, 9), -- Toxic Smoke
(13560, 4140, 9), -- Contract Payment
-- Soulless
(13561, 4298, 1), -- Race
(13561, 4121, 1), -- Summoned Monster Magic Protection
(13561, 4138, 9), -- NPC AE - Corpse Burst
(13561, 4259, 9), -- Toxic Smoke
(13561, 4140, 9), -- Contract Payment
-- Soulless
(13562, 4298, 1), -- Race
(13562, 4121, 1), -- Summoned Monster Magic Protection
(13562, 4138, 9), -- NPC AE - Corpse Burst
(13562, 4259, 9), -- Toxic Smoke
(13562, 4140, 9), -- Contract Payment
-- Soulless
(13563, 4298, 1), -- Race
(13563, 4121, 1), -- Summoned Monster Magic Protection
(13563, 4138, 9), -- NPC AE - Corpse Burst
(13563, 4259, 9), -- Toxic Smoke
(13563, 4140, 9), -- Contract Payment
-- Soulless
(13564, 4298, 1), -- Race
(13564, 4121, 1), -- Summoned Monster Magic Protection
(13564, 4138, 9), -- NPC AE - Corpse Burst
(13564, 4259, 9), -- Toxic Smoke
(13564, 4140, 9), -- Contract Payment
-- Soulless
(13565, 4298, 1), -- Race
(13565, 4121, 1), -- Summoned Monster Magic Protection
(13565, 4138, 10), -- NPC AE - Corpse Burst
(13565, 4259, 10), -- Toxic Smoke
(13565, 4140, 10), -- Contract Payment
-- Soulless
(13566, 4298, 1), -- Race
(13566, 4121, 1), -- Summoned Monster Magic Protection
(13566, 4138, 10), -- NPC AE - Corpse Burst
(13566, 4259, 10), -- Toxic Smoke
(13566, 4140, 10), -- Contract Payment
-- Soulless
(13567, 4298, 1), -- Race
(13567, 4121, 1), -- Summoned Monster Magic Protection
(13567, 4138, 10), -- NPC AE - Corpse Burst
(13567, 4259, 10), -- Toxic Smoke
(13567, 4140, 10), -- Contract Payment
-- Soulless
(13568, 4298, 1), -- Race
(13568, 4121, 1), -- Summoned Monster Magic Protection
(13568, 4138, 10), -- NPC AE - Corpse Burst
(13568, 4259, 10), -- Toxic Smoke
(13568, 4140, 10), -- Contract Payment
-- Soulless
(13569, 4298, 1), -- Race
(13569, 4121, 1), -- Summoned Monster Magic Protection
(13569, 4138, 10), -- NPC AE - Corpse Burst
(13569, 4259, 10), -- Toxic Smoke
(13569, 4140, 10), -- Contract Payment
-- Soulless
(13570, 4298, 1), -- Race
(13570, 4121, 1), -- Summoned Monster Magic Protection
(13570, 4138, 10), -- NPC AE - Corpse Burst
(13570, 4259, 10), -- Toxic Smoke
(13570, 4140, 10), -- Contract Payment
-- Soulless
(13571, 4298, 1), -- Race
(13571, 4121, 1), -- Summoned Monster Magic Protection
(13571, 4138, 11), -- NPC AE - Corpse Burst
(13571, 4259, 11), -- Toxic Smoke
(13571, 4140, 11), -- Contract Payment
-- Soulless
(13572, 4298, 1), -- Race
(13572, 4121, 1), -- Summoned Monster Magic Protection
(13572, 4138, 11), -- NPC AE - Corpse Burst
(13572, 4259, 11), -- Toxic Smoke
(13572, 4140, 11), -- Contract Payment
-- Soulless
(13573, 4298, 1), -- Race
(13573, 4121, 1), -- Summoned Monster Magic Protection
(13573, 4138, 11), -- NPC AE - Corpse Burst
(13573, 4259, 11), -- Toxic Smoke
(13573, 4140, 11), -- Contract Payment
-- Soulless
(13574, 4298, 1), -- Race
(13574, 4121, 1), -- Summoned Monster Magic Protection
(13574, 4138, 11), -- NPC AE - Corpse Burst
(13574, 4259, 11), -- Toxic Smoke
(13574, 4140, 11), -- Contract Payment
-- Soulless
(13575, 4298, 1), -- Race
(13575, 4121, 1), -- Summoned Monster Magic Protection
(13575, 4138, 11), -- NPC AE - Corpse Burst
(13575, 4259, 11), -- Toxic Smoke
(13575, 4140, 11), -- Contract Payment
-- Soulless
(13576, 4298, 1), -- Race
(13576, 4121, 1), -- Summoned Monster Magic Protection
(13576, 4138, 11), -- NPC AE - Corpse Burst
(13576, 4259, 11), -- Toxic Smoke
(13576, 4140, 11), -- Contract Payment
-- Soulless
(13577, 4298, 1), -- Race
(13577, 4121, 1), -- Summoned Monster Magic Protection
(13577, 4138, 12), -- NPC AE - Corpse Burst
(13577, 4259, 12), -- Toxic Smoke
(13577, 4140, 12), -- Contract Payment
-- Soulless
(13578, 4298, 1), -- Race
(13578, 4121, 1), -- Summoned Monster Magic Protection
(13578, 4138, 12), -- NPC AE - Corpse Burst
(13578, 4259, 12), -- Toxic Smoke
(13578, 4140, 12), -- Contract Payment
-- Soulless
(13579, 4298, 1), -- Race
(13579, 4121, 1), -- Summoned Monster Magic Protection
(13579, 4138, 12), -- NPC AE - Corpse Burst
(13579, 4259, 12), -- Toxic Smoke
(13579, 4140, 12), -- Contract Payment
-- Soulless
(13580, 4298, 1), -- Race
(13580, 4121, 1), -- Summoned Monster Magic Protection
(13580, 4138, 12), -- NPC AE - Corpse Burst
(13580, 4259, 12), -- Toxic Smoke
(13580, 4140, 12), -- Contract Payment
-- Soulless
(13581, 4298, 1), -- Race
(13581, 4121, 1), -- Summoned Monster Magic Protection
(13581, 4138, 12), -- NPC AE - Corpse Burst
(13581, 4259, 12), -- Toxic Smoke
(13581, 4140, 12), -- Contract Payment
-- Soulless
(13582, 4298, 1), -- Race
(13582, 4121, 1), -- Summoned Monster Magic Protection
(13582, 4138, 12), -- NPC AE - Corpse Burst
(13582, 4259, 12), -- Toxic Smoke
(13582, 4140, 12), -- Contract Payment
-- Nightshade
(13583, 4298, 1), -- Race
(13583, 4121, 1), -- Summoned Monster Magic Protection
(13583, 4705, 4), -- Curse of Shade
(13583, 4706, 4), -- Mass Curse of Shade
(13583, 4707, 4), -- Shade Sacrifice
-- Nightshade
(13584, 4298, 1), -- Race
(13584, 4121, 1), -- Summoned Monster Magic Protection
(13584, 4705, 4), -- Curse of Shade
(13584, 4706, 4), -- Mass Curse of Shade
(13584, 4707, 4), -- Shade Sacrifice
-- Nightshade
(13585, 4298, 1), -- Race
(13585, 4121, 1), -- Summoned Monster Magic Protection
(13585, 4705, 4), -- Curse of Shade
(13585, 4706, 4), -- Mass Curse of Shade
(13585, 4707, 4), -- Shade Sacrifice
-- Nightshade
(13586, 4298, 1), -- Race
(13586, 4121, 1), -- Summoned Monster Magic Protection
(13586, 4705, 5), -- Curse of Shade
(13586, 4706, 5), -- Mass Curse of Shade
(13586, 4707, 5), -- Shade Sacrifice
-- Nightshade
(13587, 4298, 1), -- Race
(13587, 4121, 1), -- Summoned Monster Magic Protection
(13587, 4705, 5), -- Curse of Shade
(13587, 4706, 5), -- Mass Curse of Shade
(13587, 4707, 5), -- Shade Sacrifice
-- Nightshade
(13588, 4298, 1), -- Race
(13588, 4121, 1), -- Summoned Monster Magic Protection
(13588, 4705, 5), -- Curse of Shade
(13588, 4706, 5), -- Mass Curse of Shade
(13588, 4707, 5), -- Shade Sacrifice
-- Nightshade
(13589, 4298, 1), -- Race
(13589, 4121, 1), -- Summoned Monster Magic Protection
(13589, 4705, 6), -- Curse of Shade
(13589, 4706, 6), -- Mass Curse of Shade
(13589, 4707, 6), -- Shade Sacrifice
-- Nightshade
(13590, 4298, 1), -- Race
(13590, 4121, 1), -- Summoned Monster Magic Protection
(13590, 4705, 6), -- Curse of Shade
(13590, 4706, 6), -- Mass Curse of Shade
(13590, 4707, 6), -- Shade Sacrifice
-- Nightshade
(13591, 4298, 1), -- Race
(13591, 4121, 1), -- Summoned Monster Magic Protection
(13591, 4705, 6), -- Curse of Shade
(13591, 4706, 6), -- Mass Curse of Shade
(13591, 4707, 6), -- Shade Sacrifice
-- Nightshade
(13592, 4298, 1), -- Race
(13592, 4121, 1), -- Summoned Monster Magic Protection
(13592, 4705, 7), -- Curse of Shade
(13592, 4706, 7), -- Mass Curse of Shade
(13592, 4707, 7), -- Shade Sacrifice
-- Nightshade
(13593, 4298, 1), -- Race
(13593, 4121, 1), -- Summoned Monster Magic Protection
(13593, 4705, 7), -- Curse of Shade
(13593, 4706, 7), -- Mass Curse of Shade
(13593, 4707, 7), -- Shade Sacrifice
-- Nightshade
(13594, 4298, 1), -- Race
(13594, 4121, 1), -- Summoned Monster Magic Protection
(13594, 4705, 7), -- Curse of Shade
(13594, 4706, 7), -- Mass Curse of Shade
(13594, 4707, 7), -- Shade Sacrifice
-- Nightshade
(13595, 4298, 1), -- Race
(13595, 4121, 1), -- Summoned Monster Magic Protection
(13595, 4705, 8), -- Curse of Shade
(13595, 4706, 8), -- Mass Curse of Shade
(13595, 4707, 8), -- Shade Sacrifice
-- Nightshade
(13596, 4298, 1), -- Race
(13596, 4121, 1), -- Summoned Monster Magic Protection
(13596, 4705, 8), -- Curse of Shade
(13596, 4706, 8), -- Mass Curse of Shade
(13596, 4707, 8), -- Shade Sacrifice
-- Nightshade
(13597, 4298, 1), -- Race
(13597, 4121, 1), -- Summoned Monster Magic Protection
(13597, 4705, 8), -- Curse of Shade
(13597, 4706, 8), -- Mass Curse of Shade
(13597, 4707, 8), -- Shade Sacrifice
-- Nightshade
(13598, 4298, 1), -- Race
(13598, 4121, 1), -- Summoned Monster Magic Protection
(13598, 4705, 9), -- Curse of Shade
(13598, 4706, 9), -- Mass Curse of Shade
(13598, 4707, 9), -- Shade Sacrifice
-- Nightshade
(13599, 4298, 1), -- Race
(13599, 4121, 1), -- Summoned Monster Magic Protection
(13599, 4705, 9), -- Curse of Shade
(13599, 4706, 9), -- Mass Curse of Shade
(13599, 4707, 9), -- Shade Sacrifice
-- Nightshade
(13600, 4298, 1), -- Race
(13600, 4121, 1), -- Summoned Monster Magic Protection
(13600, 4705, 9), -- Curse of Shade
(13600, 4706, 9), -- Mass Curse of Shade
(13600, 4707, 9), -- Shade Sacrifice
-- Nightshade
(13601, 4298, 1), -- Race
(13601, 4121, 1), -- Summoned Monster Magic Protection
(13601, 4705, 10), -- Curse of Shade
(13601, 4706, 10), -- Mass Curse of Shade
(13601, 4707, 10), -- Shade Sacrifice
-- Nightshade
(13602, 4298, 1), -- Race
(13602, 4121, 1), -- Summoned Monster Magic Protection
(13602, 4705, 10), -- Curse of Shade
(13602, 4706, 10), -- Mass Curse of Shade
(13602, 4707, 10), -- Shade Sacrifice
-- Nightshade
(13603, 4298, 1), -- Race
(13603, 4121, 1), -- Summoned Monster Magic Protection
(13603, 4705, 10), -- Curse of Shade
(13603, 4706, 10), -- Mass Curse of Shade
(13603, 4707, 10), -- Shade Sacrifice
-- Nightshade
(13604, 4298, 1), -- Race
(13604, 4121, 1), -- Summoned Monster Magic Protection
(13604, 4705, 11), -- Curse of Shade
(13604, 4706, 11), -- Mass Curse of Shade
(13604, 4707, 11), -- Shade Sacrifice
-- Nightshade
(13605, 4298, 1), -- Race
(13605, 4121, 1), -- Summoned Monster Magic Protection
(13605, 4705, 11), -- Curse of Shade
(13605, 4706, 11), -- Mass Curse of Shade
(13605, 4707, 11), -- Shade Sacrifice
-- Nightshade
(13606, 4298, 1), -- Race
(13606, 4121, 1), -- Summoned Monster Magic Protection
(13606, 4705, 11), -- Curse of Shade
(13606, 4706, 11), -- Mass Curse of Shade
(13606, 4707, 11), -- Shade Sacrifice
-- Nightshade
(13607, 4298, 1), -- Race
(13607, 4121, 1), -- Summoned Monster Magic Protection
(13607, 4705, 12), -- Curse of Shade
(13607, 4706, 12), -- Mass Curse of Shade
(13607, 4707, 12), -- Shade Sacrifice
-- Nightshade
(13608, 4298, 1), -- Race
(13608, 4121, 1), -- Summoned Monster Magic Protection
(13608, 4705, 12), -- Curse of Shade
(13608, 4706, 12), -- Mass Curse of Shade
(13608, 4707, 12), -- Shade Sacrifice
-- Nightshade
(13609, 4298, 1), -- Race
(13609, 4121, 1), -- Summoned Monster Magic Protection
(13609, 4705, 12), -- Curse of Shade
(13609, 4706, 12), -- Mass Curse of Shade
(13609, 4707, 12), -- Shade Sacrifice
-- Nightshade
(13610, 4298, 1), -- Race
(13610, 4121, 1), -- Summoned Monster Magic Protection
(13610, 4705, 13), -- Curse of Shade
(13610, 4706, 13), -- Mass Curse of Shade
(13610, 4707, 13), -- Shade Sacrifice
-- Nightshade
(13611, 4298, 1), -- Race
(13611, 4121, 1), -- Summoned Monster Magic Protection
(13611, 4705, 13), -- Curse of Shade
(13611, 4706, 13), -- Mass Curse of Shade
(13611, 4707, 13), -- Shade Sacrifice
-- Nightshade
(13612, 4298, 1), -- Race
(13612, 4121, 1), -- Summoned Monster Magic Protection
(13612, 4705, 13), -- Curse of Shade
(13612, 4706, 13), -- Mass Curse of Shade
(13612, 4707, 13), -- Shade Sacrifice
-- Reanimated Man
(13613, 4290, 1), -- Race
(13613, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13614, 4290, 1), -- Race
(13614, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13615, 4290, 1), -- Race
(13615, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13616, 4290, 1), -- Race
(13616, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13617, 4290, 1), -- Race
(13617, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13618, 4290, 1), -- Race
(13618, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13619, 4290, 1), -- Race
(13619, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13620, 4290, 1), -- Race
(13620, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13621, 4290, 1), -- Race
(13621, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13622, 4290, 1), -- Race
(13622, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13623, 4290, 1), -- Race
(13623, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13624, 4290, 1), -- Race
(13624, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13625, 4290, 1), -- Race
(13625, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13626, 4290, 1), -- Race
(13626, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13627, 4290, 1), -- Race
(13627, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13628, 4290, 1), -- Race
(13628, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13629, 4290, 1), -- Race
(13629, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13630, 4290, 1), -- Race
(13630, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13631, 4290, 1), -- Race
(13631, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13632, 4290, 1), -- Race
(13632, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13633, 4290, 1), -- Race
(13633, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13634, 4290, 1), -- Race
(13634, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13635, 4290, 1), -- Race
(13635, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13636, 4290, 1), -- Race
(13636, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13637, 4290, 1), -- Race
(13637, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13638, 4290, 1), -- Race
(13638, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13639, 4290, 1), -- Race
(13639, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13640, 4290, 1), -- Race
(13640, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13641, 4290, 1), -- Race
(13641, 4121, 1), -- Summoned Monster Magic Protection
-- Reanimated Man
(13642, 4290, 1), -- Race
(13642, 4121, 1), -- Summoned Monster Magic Protection
-- Corrupted Man
(13643, 4290, 1), -- Race
(13643, 4121, 1), -- Summoned Monster Magic Protection
(13643, 4260, 8), -- Steal Blood
-- Corrupted Man
(13644, 4290, 1), -- Race
(13644, 4121, 1), -- Summoned Monster Magic Protection
(13644, 4260, 8), -- Steal Blood
-- Corrupted Man
(13645, 4290, 1), -- Race
(13645, 4121, 1), -- Summoned Monster Magic Protection
(13645, 4260, 8), -- Steal Blood
-- Corrupted Man
(13646, 4290, 1), -- Race
(13646, 4121, 1), -- Summoned Monster Magic Protection
(13646, 4260, 8), -- Steal Blood
-- Corrupted Man
(13647, 4290, 1), -- Race
(13647, 4121, 1), -- Summoned Monster Magic Protection
(13647, 4260, 8), -- Steal Blood
-- Corrupted Man
(13648, 4290, 1), -- Race
(13648, 4121, 1), -- Summoned Monster Magic Protection
(13648, 4260, 8), -- Steal Blood
-- Corrupted Man
(13649, 4290, 1), -- Race
(13649, 4121, 1), -- Summoned Monster Magic Protection
(13649, 4260, 9), -- Steal Blood
-- Corrupted Man
(13650, 4290, 1), -- Race
(13650, 4121, 1), -- Summoned Monster Magic Protection
(13650, 4260, 9), -- Steal Blood
-- Corrupted Man
(13651, 4290, 1), -- Race
(13651, 4121, 1), -- Summoned Monster Magic Protection
(13651, 4260, 9), -- Steal Blood
-- Corrupted Man
(13652, 4290, 1), -- Race
(13652, 4121, 1), -- Summoned Monster Magic Protection
(13652, 4260, 9), -- Steal Blood
-- Corrupted Man
(13653, 4290, 1), -- Race
(13653, 4121, 1), -- Summoned Monster Magic Protection
(13653, 4260, 9), -- Steal Blood
-- Corrupted Man
(13654, 4290, 1), -- Race
(13654, 4121, 1), -- Summoned Monster Magic Protection
(13654, 4260, 9), -- Steal Blood
-- Corrupted Man
(13655, 4290, 1), -- Race
(13655, 4121, 1), -- Summoned Monster Magic Protection
(13655, 4260, 10), -- Steal Blood
-- Corrupted Man
(13656, 4290, 1), -- Race
(13656, 4121, 1), -- Summoned Monster Magic Protection
(13656, 4260, 10), -- Steal Blood
-- Corrupted Man
(13657, 4290, 1), -- Race
(13657, 4121, 1), -- Summoned Monster Magic Protection
(13657, 4260, 10), -- Steal Blood
-- Corrupted Man
(13658, 4290, 1), -- Race
(13658, 4121, 1), -- Summoned Monster Magic Protection
(13658, 4260, 10), -- Steal Blood
-- Corrupted Man
(13659, 4290, 1), -- Race
(13659, 4121, 1), -- Summoned Monster Magic Protection
(13659, 4260, 10), -- Steal Blood
-- Corrupted Man
(13660, 4290, 1), -- Race
(13660, 4121, 1), -- Summoned Monster Magic Protection
(13660, 4260, 10), -- Steal Blood
-- Corrupted Man
(13661, 4290, 1), -- Race
(13661, 4121, 1), -- Summoned Monster Magic Protection
(13661, 4260, 11), -- Steal Blood
-- Corrupted Man
(13662, 4290, 1), -- Race
(13662, 4121, 1), -- Summoned Monster Magic Protection
(13662, 4260, 11), -- Steal Blood
-- Corrupted Man
(13663, 4290, 1), -- Race
(13663, 4121, 1), -- Summoned Monster Magic Protection
(13663, 4260, 11), -- Steal Blood
-- Corrupted Man
(13664, 4290, 1), -- Race
(13664, 4121, 1), -- Summoned Monster Magic Protection
(13664, 4260, 11), -- Steal Blood
-- Corrupted Man
(13665, 4290, 1), -- Race
(13665, 4121, 1), -- Summoned Monster Magic Protection
(13665, 4260, 11), -- Steal Blood
-- Corrupted Man
(13666, 4290, 1), -- Race
(13666, 4121, 1), -- Summoned Monster Magic Protection
(13666, 4260, 11), -- Steal Blood
-- Corrupted Man
(13667, 4290, 1), -- Race
(13667, 4121, 1), -- Summoned Monster Magic Protection
(13667, 4260, 12), -- Steal Blood
-- Corrupted Man
(13668, 4290, 1), -- Race
(13668, 4121, 1), -- Summoned Monster Magic Protection
(13668, 4260, 12), -- Steal Blood
-- Corrupted Man
(13669, 4290, 1), -- Race
(13669, 4121, 1), -- Summoned Monster Magic Protection
(13669, 4260, 12), -- Steal Blood
-- Corrupted Man
(13670, 4290, 1), -- Race
(13670, 4121, 1), -- Summoned Monster Magic Protection
(13670, 4260, 12), -- Steal Blood
-- Corrupted Man
(13671, 4290, 1), -- Race
(13671, 4121, 1), -- Summoned Monster Magic Protection
(13671, 4260, 12), -- Steal Blood
-- Corrupted Man
(13672, 4290, 1), -- Race
(13672, 4121, 1), -- Summoned Monster Magic Protection
(13672, 4260, 12), -- Steal Blood
-- Cursed Man
(13673, 4290, 1), -- Race
(13673, 4121, 1), -- Summoned Monster Magic Protection
(13673, 4709, 8), -- Cursed Blow
(13673, 4708, 8), -- Cursed Strike
-- Cursed Man
(13674, 4290, 1), -- Race
(13674, 4121, 1), -- Summoned Monster Magic Protection
(13674, 4709, 8), -- Cursed Blow
(13674, 4708, 8), -- Cursed Strike
-- Cursed Man
(13675, 4290, 1), -- Race
(13675, 4121, 1), -- Summoned Monster Magic Protection
(13675, 4709, 8), -- Cursed Blow
(13675, 4708, 8), -- Cursed Strike
-- Cursed Man
(13676, 4290, 1), -- Race
(13676, 4121, 1), -- Summoned Monster Magic Protection
(13676, 4709, 8), -- Cursed Blow
(13676, 4708, 8), -- Cursed Strike
-- Cursed Man
(13677, 4290, 1), -- Race
(13677, 4121, 1), -- Summoned Monster Magic Protection
(13677, 4709, 8), -- Cursed Blow
(13677, 4708, 8), -- Cursed Strike
-- Cursed Man
(13678, 4290, 1), -- Race
(13678, 4121, 1), -- Summoned Monster Magic Protection
(13678, 4709, 8), -- Cursed Blow
(13678, 4708, 8), -- Cursed Strike
-- Cursed Man
(13679, 4290, 1), -- Race
(13679, 4121, 1), -- Summoned Monster Magic Protection
(13679, 4709, 8), -- Cursed Blow
(13679, 4708, 8), -- Cursed Strike
-- Cursed Man
(13680, 4290, 1), -- Race
(13680, 4121, 1), -- Summoned Monster Magic Protection
(13680, 4709, 8), -- Cursed Blow
(13680, 4708, 8), -- Cursed Strike
-- Cursed Man
(13681, 4290, 1), -- Race
(13681, 4121, 1), -- Summoned Monster Magic Protection
(13681, 4709, 8), -- Cursed Blow
(13681, 4708, 8), -- Cursed Strike
-- Cursed Man
(13682, 4290, 1), -- Race
(13682, 4121, 1), -- Summoned Monster Magic Protection
(13682, 4709, 8), -- Cursed Blow
(13682, 4708, 8), -- Cursed Strike
-- Cursed Man
(13683, 4290, 1), -- Race
(13683, 4121, 1), -- Summoned Monster Magic Protection
(13683, 4709, 9), -- Cursed Blow
(13683, 4708, 9), -- Cursed Strike
-- Cursed Man
(13684, 4290, 1), -- Race
(13684, 4121, 1), -- Summoned Monster Magic Protection
(13684, 4709, 9), -- Cursed Blow
(13684, 4708, 9), -- Cursed Strike
-- Cursed Man
(13685, 4290, 1), -- Race
(13685, 4121, 1), -- Summoned Monster Magic Protection
(13685, 4709, 9), -- Cursed Blow
(13685, 4708, 9), -- Cursed Strike
-- Cursed Man
(13686, 4290, 1), -- Race
(13686, 4121, 1), -- Summoned Monster Magic Protection
(13686, 4709, 9), -- Cursed Blow
(13686, 4708, 9), -- Cursed Strike
-- Cursed Man
(13687, 4290, 1), -- Race
(13687, 4121, 1), -- Summoned Monster Magic Protection
(13687, 4709, 9), -- Cursed Blow
(13687, 4708, 9), -- Cursed Strike
-- Cursed Man
(13688, 4290, 1), -- Race
(13688, 4121, 1), -- Summoned Monster Magic Protection
(13688, 4709, 9), -- Cursed Blow
(13688, 4708, 9), -- Cursed Strike
-- Cursed Man
(13689, 4290, 1), -- Race
(13689, 4121, 1), -- Summoned Monster Magic Protection
(13689, 4709, 9), -- Cursed Blow
(13689, 4708, 9), -- Cursed Strike
-- Cursed Man
(13690, 4290, 1), -- Race
(13690, 4121, 1), -- Summoned Monster Magic Protection
(13690, 4709, 9), -- Cursed Blow
(13690, 4708, 9), -- Cursed Strike
-- Cursed Man
(13691, 4290, 1), -- Race
(13691, 4121, 1), -- Summoned Monster Magic Protection
(13691, 4709, 9), -- Cursed Blow
(13691, 4708, 9), -- Cursed Strike
-- Cursed Man
(13692, 4290, 1), -- Race
(13692, 4121, 1), -- Summoned Monster Magic Protection
(13692, 4709, 9), -- Cursed Blow
(13692, 4708, 9), -- Cursed Strike
-- Cursed Man
(13693, 4290, 1), -- Race
(13693, 4121, 1), -- Summoned Monster Magic Protection
(13693, 4709, 10), -- Cursed Blow
(13693, 4708, 10), -- Cursed Strike
-- Cursed Man
(13694, 4290, 1), -- Race
(13694, 4121, 1), -- Summoned Monster Magic Protection
(13694, 4709, 10), -- Cursed Blow
(13694, 4708, 10), -- Cursed Strike
-- Cursed Man
(13695, 4290, 1), -- Race
(13695, 4121, 1), -- Summoned Monster Magic Protection
(13695, 4709, 10), -- Cursed Blow
(13695, 4708, 10), -- Cursed Strike
-- Cursed Man
(13696, 4290, 1), -- Race
(13696, 4121, 1), -- Summoned Monster Magic Protection
(13696, 4709, 10), -- Cursed Blow
(13696, 4708, 10), -- Cursed Strike
-- Cursed Man
(13697, 4290, 1), -- Race
(13697, 4121, 1), -- Summoned Monster Magic Protection
(13697, 4709, 10), -- Cursed Blow
(13697, 4708, 10), -- Cursed Strike
-- Cursed Man
(13698, 4290, 1), -- Race
(13698, 4121, 1), -- Summoned Monster Magic Protection
(13698, 4709, 10), -- Cursed Blow
(13698, 4708, 10), -- Cursed Strike
-- Cursed Man
(13699, 4290, 1), -- Race
(13699, 4121, 1), -- Summoned Monster Magic Protection
(13699, 4709, 10), -- Cursed Blow
(13699, 4708, 10), -- Cursed Strike
-- Cursed Man
(13700, 4290, 1), -- Race
(13700, 4121, 1), -- Summoned Monster Magic Protection
(13700, 4709, 10), -- Cursed Blow
(13700, 4708, 10), -- Cursed Strike
-- Cursed Man
(13701, 4290, 1), -- Race
(13701, 4121, 1), -- Summoned Monster Magic Protection
(13701, 4709, 10), -- Cursed Blow
(13701, 4708, 10), -- Cursed Strike
-- Cursed Man
(13702, 4290, 1), -- Race
(13702, 4121, 1), -- Summoned Monster Magic Protection
(13702, 4709, 10), -- Cursed Blow
(13702, 4708, 10), -- Cursed Strike
-- Mechanic Golem
(13703, 4291, 1), -- Race
(13703, 4121, 1), -- Summoned Monster Magic Protection
(13703, 4068, 8), -- Mech Cannon
-- Mechanic Golem
(13704, 4291, 1), -- Race
(13704, 4121, 1), -- Summoned Monster Magic Protection
(13704, 4068, 8), -- Mech Cannon
-- Mechanic Golem
(13705, 4291, 1), -- Race
(13705, 4121, 1), -- Summoned Monster Magic Protection
(13705, 4068, 8), -- Mech Cannon
-- Mechanic Golem
(13706, 4291, 1), -- Race
(13706, 4121, 1), -- Summoned Monster Magic Protection
(13706, 4068, 8), -- Mech Cannon
-- Mechanic Golem
(13707, 4291, 1), -- Race
(13707, 4121, 1), -- Summoned Monster Magic Protection
(13707, 4068, 8), -- Mech Cannon
-- Mechanic Golem
(13708, 4291, 1), -- Race
(13708, 4121, 1), -- Summoned Monster Magic Protection
(13708, 4068, 8), -- Mech Cannon
-- Mechanic Golem
(13709, 4291, 1), -- Race
(13709, 4121, 1), -- Summoned Monster Magic Protection
(13709, 4068, 9), -- Mech Cannon
-- Mechanic Golem
(13710, 4291, 1), -- Race
(13710, 4121, 1), -- Summoned Monster Magic Protection
(13710, 4068, 9), -- Mech Cannon
-- Mechanic Golem
(13711, 4291, 1), -- Race
(13711, 4121, 1), -- Summoned Monster Magic Protection
(13711, 4068, 9), -- Mech Cannon
-- Mechanic Golem
(13712, 4291, 1), -- Race
(13712, 4121, 1), -- Summoned Monster Magic Protection
(13712, 4068, 9), -- Mech Cannon
-- Mechanic Golem
(13713, 4291, 1), -- Race
(13713, 4121, 1), -- Summoned Monster Magic Protection
(13713, 4068, 9), -- Mech Cannon
-- Mechanic Golem
(13714, 4291, 1), -- Race
(13714, 4121, 1), -- Summoned Monster Magic Protection
(13714, 4068, 9), -- Mech Cannon
-- Mechanic Golem
(13715, 4291, 1), -- Race
(13715, 4121, 1), -- Summoned Monster Magic Protection
(13715, 4068, 10), -- Mech Cannon
-- Mechanic Golem
(13716, 4291, 1), -- Race
(13716, 4121, 1), -- Summoned Monster Magic Protection
(13716, 4068, 10), -- Mech Cannon
-- Mechanic Golem
(13717, 4291, 1), -- Race
(13717, 4121, 1), -- Summoned Monster Magic Protection
(13717, 4068, 10), -- Mech Cannon
-- Mechanic Golem
(13718, 4291, 1), -- Race
(13718, 4121, 1), -- Summoned Monster Magic Protection
(13718, 4068, 10), -- Mech Cannon
-- Mechanic Golem
(13719, 4291, 1), -- Race
(13719, 4121, 1), -- Summoned Monster Magic Protection
(13719, 4068, 10), -- Mech Cannon
-- Mechanic Golem
(13720, 4291, 1), -- Race
(13720, 4121, 1), -- Summoned Monster Magic Protection
(13720, 4068, 10), -- Mech Cannon
-- Mechanic Golem
(13721, 4291, 1), -- Race
(13721, 4121, 1), -- Summoned Monster Magic Protection
(13721, 4068, 11), -- Mech Cannon
-- Mechanic Golem
(13722, 4291, 1), -- Race
(13722, 4121, 1), -- Summoned Monster Magic Protection
(13722, 4068, 11), -- Mech Cannon
-- Mechanic Golem
(13723, 4291, 1), -- Race
(13723, 4121, 1), -- Summoned Monster Magic Protection
(13723, 4068, 11), -- Mech Cannon
-- Mechanic Golem
(13724, 4291, 1), -- Race
(13724, 4121, 1), -- Summoned Monster Magic Protection
(13724, 4068, 11), -- Mech Cannon
-- Mechanic Golem
(13725, 4291, 1), -- Race
(13725, 4121, 1), -- Summoned Monster Magic Protection
(13725, 4068, 11), -- Mech Cannon
-- Mechanic Golem
(13726, 4291, 1), -- Race
(13726, 4121, 1), -- Summoned Monster Magic Protection
(13726, 4068, 11), -- Mech Cannon
-- Mechanic Golem
(13727, 4291, 1), -- Race
(13727, 4121, 1), -- Summoned Monster Magic Protection
(13727, 4068, 12), -- Mech Cannon
-- Mechanic Golem
(13728, 4291, 1), -- Race
(13728, 4121, 1), -- Summoned Monster Magic Protection
(13728, 4068, 12), -- Mech Cannon
-- Mechanic Golem
(13729, 4291, 1), -- Race
(13729, 4121, 1), -- Summoned Monster Magic Protection
(13729, 4068, 12), -- Mech Cannon
-- Mechanic Golem
(13730, 4291, 1), -- Race
(13730, 4121, 1), -- Summoned Monster Magic Protection
(13730, 4068, 12), -- Mech Cannon
-- Mechanic Golem
(13731, 4291, 1), -- Race
(13731, 4121, 1), -- Summoned Monster Magic Protection
(13731, 4068, 12), -- Mech Cannon
-- Mechanic Golem
(13732, 4291, 1), -- Race
(13732, 4121, 1), -- Summoned Monster Magic Protection
(13732, 4068, 12), -- Mech Cannon
-- Big Boom
(13733, 4291, 1), -- Race
(13733, 4121, 1), -- Summoned Monster Magic Protection
(13733, 4139, 8), -- Boom Attack
-- Big Boom
(13734, 4291, 1), -- Race
(13734, 4121, 1), -- Summoned Monster Magic Protection
(13734, 4139, 8), -- Boom Attack
-- Big Boom
(13735, 4291, 1), -- Race
(13735, 4121, 1), -- Summoned Monster Magic Protection
(13735, 4139, 8), -- Boom Attack
-- Big Boom
(13736, 4291, 1), -- Race
(13736, 4121, 1), -- Summoned Monster Magic Protection
(13736, 4139, 8), -- Boom Attack
-- Big Boom
(13737, 4291, 1), -- Race
(13737, 4121, 1), -- Summoned Monster Magic Protection
(13737, 4139, 8), -- Boom Attack
-- Big Boom
(13738, 4291, 1), -- Race
(13738, 4121, 1), -- Summoned Monster Magic Protection
(13738, 4139, 8), -- Boom Attack
-- Big Boom
(13739, 4291, 1), -- Race
(13739, 4121, 1), -- Summoned Monster Magic Protection
(13739, 4139, 9), -- Boom Attack
-- Big Boom
(13740, 4291, 1), -- Race
(13740, 4121, 1), -- Summoned Monster Magic Protection
(13740, 4139, 9), -- Boom Attack
-- Big Boom
(13741, 4291, 1), -- Race
(13741, 4121, 1), -- Summoned Monster Magic Protection
(13741, 4139, 9), -- Boom Attack
-- Big Boom
(13742, 4291, 1), -- Race
(13742, 4121, 1), -- Summoned Monster Magic Protection
(13742, 4139, 9), -- Boom Attack
-- Big Boom
(13743, 4291, 1), -- Race
(13743, 4121, 1), -- Summoned Monster Magic Protection
(13743, 4139, 9), -- Boom Attack
-- Big Boom
(13744, 4291, 1), -- Race
(13744, 4121, 1), -- Summoned Monster Magic Protection
(13744, 4139, 9), -- Boom Attack
-- Big Boom
(13745, 4291, 1), -- Race
(13745, 4121, 1), -- Summoned Monster Magic Protection
(13745, 4139, 10), -- Boom Attack
-- Big Boom
(13746, 4291, 1), -- Race
(13746, 4121, 1), -- Summoned Monster Magic Protection
(13746, 4139, 10), -- Boom Attack
-- Big Boom
(13747, 4291, 1), -- Race
(13747, 4121, 1), -- Summoned Monster Magic Protection
(13747, 4139, 10), -- Boom Attack
-- Big Boom
(13748, 4291, 1), -- Race
(13748, 4121, 1), -- Summoned Monster Magic Protection
(13748, 4139, 10), -- Boom Attack
-- Big Boom
(13749, 4291, 1), -- Race
(13749, 4121, 1), -- Summoned Monster Magic Protection
(13749, 4139, 10), -- Boom Attack
-- Big Boom
(13750, 4291, 1), -- Race
(13750, 4121, 1), -- Summoned Monster Magic Protection
(13750, 4139, 10), -- Boom Attack
-- Big Boom
(13751, 4291, 1), -- Race
(13751, 4121, 1), -- Summoned Monster Magic Protection
(13751, 4139, 11), -- Boom Attack
-- Big Boom
(13752, 4291, 1), -- Race
(13752, 4121, 1), -- Summoned Monster Magic Protection
(13752, 4139, 11), -- Boom Attack
-- Big Boom
(13753, 4291, 1), -- Race
(13753, 4121, 1), -- Summoned Monster Magic Protection
(13753, 4139, 11), -- Boom Attack
-- Big Boom
(13754, 4291, 1), -- Race
(13754, 4121, 1), -- Summoned Monster Magic Protection
(13754, 4139, 11), -- Boom Attack
-- Big Boom
(13755, 4291, 1), -- Race
(13755, 4121, 1), -- Summoned Monster Magic Protection
(13755, 4139, 11), -- Boom Attack
-- Big Boom
(13756, 4291, 1), -- Race
(13756, 4121, 1), -- Summoned Monster Magic Protection
(13756, 4139, 11), -- Boom Attack
-- Big Boom
(13757, 4291, 1), -- Race
(13757, 4121, 1), -- Summoned Monster Magic Protection
(13757, 4139, 12), -- Boom Attack
-- Big Boom
(13758, 4291, 1), -- Race
(13758, 4121, 1), -- Summoned Monster Magic Protection
(13758, 4139, 12), -- Boom Attack
-- Big Boom
(13759, 4291, 1), -- Race
(13759, 4121, 1), -- Summoned Monster Magic Protection
(13759, 4139, 12), -- Boom Attack
-- Big Boom
(13760, 4291, 1), -- Race
(13760, 4121, 1), -- Summoned Monster Magic Protection
(13760, 4139, 12), -- Boom Attack
-- Big Boom
(13761, 4291, 1), -- Race
(13761, 4121, 1), -- Summoned Monster Magic Protection
(13761, 4139, 12), -- Boom Attack
-- Big Boom
(13762, 4291, 1), -- Race
(13762, 4121, 1), -- Summoned Monster Magic Protection
(13762, 4139, 12), -- Boom Attack
-- Siege Golem
(13763, 4290, 1), -- Race
(13763, 4332, 1), -- Mental Aegis
-- Siege Golem
(13764, 4290, 1), -- Race
(13764, 4332, 1), -- Mental Aegis
-- Siege Golem
(13765, 4290, 1), -- Race
(13765, 4332, 1), -- Mental Aegis
-- Siege Golem
(13766, 4290, 1), -- Race
(13766, 4332, 1), -- Mental Aegis
-- Siege Golem
(13767, 4290, 1), -- Race
(13767, 4332, 1), -- Mental Aegis
-- Siege Golem
(13768, 4290, 1), -- Race
(13768, 4332, 1), -- Mental Aegis
-- Siege Golem
(13769, 4290, 1), -- Race
(13769, 4332, 1), -- Mental Aegis
-- Siege Golem
(13770, 4290, 1), -- Race
(13770, 4332, 1), -- Mental Aegis
-- Siege Golem
(13771, 4290, 1), -- Race
(13771, 4332, 1), -- Mental Aegis
-- Siege Golem
(13772, 4290, 1), -- Race
(13772, 4332, 1), -- Mental Aegis
-- Siege Golem
(13773, 4290, 1), -- Race
(13773, 4332, 1), -- Mental Aegis
-- Siege Golem
(13774, 4290, 1), -- Race
(13774, 4332, 1), -- Mental Aegis
-- Siege Golem
(13775, 4290, 1), -- Race
(13775, 4332, 1), -- Mental Aegis
-- Siege Golem
(13776, 4290, 1), -- Race
(13776, 4332, 1), -- Mental Aegis
-- Siege Golem
(13777, 4290, 1), -- Race
(13777, 4332, 1), -- Mental Aegis
-- Siege Golem
(13778, 4290, 1), -- Race
(13778, 4332, 1), -- Mental Aegis
-- Siege Golem
(13779, 4290, 1), -- Race
(13779, 4332, 1), -- Mental Aegis
-- Siege Golem
(13780, 4290, 1), -- Race
(13780, 4332, 1), -- Mental Aegis
-- Siege Golem
(13781, 4290, 1), -- Race
(13781, 4332, 1), -- Mental Aegis
-- Siege Golem
(13782, 4290, 1), -- Race
(13782, 4332, 1), -- Mental Aegis
-- Siege Golem
(13783, 4290, 1), -- Race
(13783, 4332, 1), -- Mental Aegis
-- Siege Golem
(13784, 4290, 1), -- Race
(13784, 4332, 1), -- Mental Aegis
-- Siege Golem
(13785, 4290, 1), -- Race
(13785, 4332, 1), -- Mental Aegis
-- Siege Golem
(13786, 4290, 1), -- Race
(13786, 4332, 1), -- Mental Aegis
-- Siege Golem
(13787, 4290, 1), -- Race
(13787, 4332, 1), -- Mental Aegis
-- Siege Golem
(13788, 4290, 1), -- Race
(13788, 4332, 1), -- Mental Aegis
-- Siege Golem
(13789, 4290, 1), -- Race
(13789, 4332, 1), -- Mental Aegis
-- Siege Golem
(13790, 4290, 1), -- Race
(13790, 4332, 1), -- Mental Aegis
-- Siege Golem
(13791, 4290, 1), -- Race
(13791, 4332, 1), -- Mental Aegis
-- Siege Golem
(13792, 4290, 1), -- Race
(13792, 4332, 1), -- Mental Aegis
-- Wild Hog Cannon
(13793, 4290, 1), -- Race
(13793, 4121, 1), -- Summoned Monster Magic Protection
(13793, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13794, 4290, 1), -- Race
(13794, 4121, 1), -- Summoned Monster Magic Protection
(13794, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13795, 4290, 1), -- Race
(13795, 4121, 1), -- Summoned Monster Magic Protection
(13795, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13796, 4290, 1), -- Race
(13796, 4121, 1), -- Summoned Monster Magic Protection
(13796, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13797, 4290, 1), -- Race
(13797, 4121, 1), -- Summoned Monster Magic Protection
(13797, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13798, 4290, 1), -- Race
(13798, 4121, 1), -- Summoned Monster Magic Protection
(13798, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13799, 4290, 1), -- Race
(13799, 4121, 1), -- Summoned Monster Magic Protection
(13799, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13800, 4290, 1), -- Race
(13800, 4121, 1), -- Summoned Monster Magic Protection
(13800, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13801, 4290, 1), -- Race
(13801, 4121, 1), -- Summoned Monster Magic Protection
(13801, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13802, 4290, 1), -- Race
(13802, 4121, 1), -- Summoned Monster Magic Protection
(13802, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13803, 4290, 1), -- Race
(13803, 4121, 1), -- Summoned Monster Magic Protection
(13803, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13804, 4290, 1), -- Race
(13804, 4121, 1), -- Summoned Monster Magic Protection
(13804, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13805, 4290, 1), -- Race
(13805, 4121, 1), -- Summoned Monster Magic Protection
(13805, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13806, 4290, 1), -- Race
(13806, 4121, 1), -- Summoned Monster Magic Protection
(13806, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13807, 4290, 1), -- Race
(13807, 4121, 1), -- Summoned Monster Magic Protection
(13807, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13808, 4290, 1), -- Race
(13808, 4121, 1), -- Summoned Monster Magic Protection
(13808, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13809, 4290, 1), -- Race
(13809, 4121, 1), -- Summoned Monster Magic Protection
(13809, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13810, 4290, 1), -- Race
(13810, 4121, 1), -- Summoned Monster Magic Protection
(13810, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13811, 4290, 1), -- Race
(13811, 4121, 1), -- Summoned Monster Magic Protection
(13811, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13812, 4290, 1), -- Race
(13812, 4121, 1), -- Summoned Monster Magic Protection
(13812, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13813, 4290, 1), -- Race
(13813, 4121, 1), -- Summoned Monster Magic Protection
(13813, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13814, 4290, 1), -- Race
(13814, 4121, 1), -- Summoned Monster Magic Protection
(13814, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13815, 4290, 1), -- Race
(13815, 4121, 1), -- Summoned Monster Magic Protection
(13815, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13816, 4290, 1), -- Race
(13816, 4121, 1), -- Summoned Monster Magic Protection
(13816, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13817, 4290, 1), -- Race
(13817, 4121, 1), -- Summoned Monster Magic Protection
(13817, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13818, 4290, 1), -- Race
(13818, 4121, 1), -- Summoned Monster Magic Protection
(13818, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13819, 4290, 1), -- Race
(13819, 4121, 1), -- Summoned Monster Magic Protection
(13819, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13820, 4290, 1), -- Race
(13820, 4121, 1), -- Summoned Monster Magic Protection
(13820, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13821, 4290, 1), -- Race
(13821, 4121, 1), -- Summoned Monster Magic Protection
(13821, 4230, 1), -- Wild Cannon
-- Wild Hog Cannon
(13822, 4290, 1), -- Race
(13822, 4121, 1), -- Summoned Monster Magic Protection
(13822, 4230, 1), -- Wild Cannon
-- Dark Panther
(13823, 4293, 1), -- Race
(13823, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13824, 4293, 1), -- Race
(13824, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13825, 4293, 1), -- Race
(13825, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13826, 4293, 1), -- Race
(13826, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13827, 4293, 1), -- Race
(13827, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13828, 4293, 1), -- Race
(13828, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13829, 4293, 1), -- Race
(13829, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13830, 4293, 1), -- Race
(13830, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13831, 4293, 1), -- Race
(13831, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13832, 4293, 1), -- Race
(13832, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13833, 4293, 1), -- Race
(13833, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13834, 4293, 1), -- Race
(13834, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13835, 4293, 1), -- Race
(13835, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13836, 4293, 1), -- Race
(13836, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13837, 4293, 1), -- Race
(13837, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13838, 4293, 1), -- Race
(13838, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13839, 4293, 1), -- Race
(13839, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13840, 4293, 1), -- Race
(13840, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13841, 4293, 1), -- Race
(13841, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13842, 4293, 1), -- Race
(13842, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13843, 4293, 1), -- Race
(13843, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13844, 4293, 1), -- Race
(13844, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13845, 4293, 1), -- Race
(13845, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13846, 4293, 1), -- Race
(13846, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13847, 4293, 1), -- Race
(13847, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13848, 4293, 1), -- Race
(13848, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13849, 4293, 1), -- Race
(13849, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13850, 4293, 1), -- Race
(13850, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13851, 4293, 1), -- Race
(13851, 4121, 1), -- Summoned Monster Magic Protection
-- Dark Panther
(13852, 4293, 1), -- Race
(13852, 4121, 1); -- Summoned Monster Magic Protection