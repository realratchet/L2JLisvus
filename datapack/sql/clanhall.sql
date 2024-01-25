-- ---------------------------
-- Table structure for clanhall
-- ---------------------------
CREATE TABLE IF NOT EXISTS `clanhall` (
  `id` int(11) NOT NULL default '0',
  `name` varchar(40) NOT NULL default '',
  `ownerId` int(11) NOT NULL default '0',
  `lease` int(10) NOT NULL default '0',
  `defaultBid` int(11) NOT NULL default '0',
  `desc` text NOT NULL,
  `location` varchar(15) NOT NULL default '',
  `paidUntil` decimal(20,0) NOT NULL default '0',
  `Grade` decimal(1,0) NOT NULL default '0',
  `paid` int(1) NOT NULL default '0',
  PRIMARY KEY `id` (`id`),
  KEY `ownerId` (`ownerId`)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT IGNORE INTO `clanhall` VALUES
('21', 'Partisan Hideaway', '0', '500000', '0', 'Ol Mahum Partisan Hideaway', 'Dion', '0', '0', '0'),
('22', 'Moonstone Hall', '0', '500000', '20000000', 'Clan hall located in the Town of Gludio', 'Gludio', '0', '2', '0'),
('23', 'Onyx Hall', '0', '500000', '20000000', 'Clan hall located in the Town of Gludio', 'Gludio', '0', '2', '0'),
('24', 'Topaz Hall', '0', '500000', '20000000', 'Clan hall located in the Town of Gludio', 'Gludio', '0', '2', '0'),
('25', 'Ruby Hall', '0', '500000', '20000000', 'Clan hall located in the Town of Gludio', 'Gludio', '0', '2', '0'),
('26', 'Crystal Hall', '0', '500000', '20000000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2', '0'),
('27', 'Onyx Hall', '0', '500000', '20000000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2', '0'),
('28', 'Sapphire Hall', '0', '500000', '20000000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2', '0'),
('29', 'Moonstone Hall', '0', '500000', '20000000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2', '0'),
('30', 'Emerald Hall', '0', '500000', '20000000', 'Clan hall located in Gludin Village', 'Gludin', '0', '2', '0'),
('31', 'The Atramental Barracks', '0', '500000', '8000000', 'Clan hall located in the Town of Dion', 'Dion', '0', '1', '0'),
('32', 'The Scarlet Barracks', '0', '500000', '8000000', 'Clan hall located in the Town of Dion', 'Dion', '0', '1', '0'),
('33', 'The Viridian Barracks', '0', '500000', '8000000', 'Clan hall located in the Town of Dion', 'Dion', '0', '1', '0'),
('34', 'Devastated Castle', '0', '500000', '0', 'Contestable Clan Hall', 'Aden', '0', '0', '0'),
('35', 'Bandit Stronghold', '0', '500000', '0', 'Contestable Clan Hall', 'Oren', '0', '0', '0'),
('36', 'The Golden Chamber', '0', '500000', '50000000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3', '0'),
('37', 'The Silver Chamber', '0', '500000', '50000000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3', '0'),
('38', 'The Mithril Chamber', '0', '500000', '50000000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3', '0'),
('39', 'Silver Manor', '0', '500000', '50000000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3', '0'),
('40', 'Gold Manor', '0', '500000', '50000000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3', '0'),
('41', 'The Bronze Chamber', '0', '500000', '50000000', 'Clan hall located in the Town of Aden', 'Aden', '0', '3', '0'),
('42', 'The Golden Chamber', '0', '500000', '50000000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3', '0'),
('43', 'The Silver Chamber', '0', '500000', '50000000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3', '0'),
('44', 'The Mithril Chamber', '0', '500000', '50000000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3', '0'),
('45', 'The Bronze Chamber', '0', '500000', '50000000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3', '0'),
('46', 'Silver Manor', '0', '500000', '50000000', 'Clan hall located in the Town of Giran', 'Giran', '0', '3', '0'),
('47', 'Moonstone Hall', '0', '500000', '50000000', 'Clan hall located in the Town of Goddard', 'Goddard', '0', '3', '0'),
('48', 'Onyx Hall', '0', '500000', '50000000', 'Clan hall located in the Town of Goddard', 'Goddard', '0', '3', '0'),
('49', 'Emerald Hall', '0', '500000', '50000000', 'Clan hall located in the Town of Goddard', 'Goddard', '0', '3', '0'),
('50', 'Sapphire Hall', '0', '500000', '50000000', 'Clan hall located in the Town of Goddard', 'Goddard', '0', '3', '0');