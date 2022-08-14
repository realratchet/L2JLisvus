--
-- Table structure for `clan_wars`
--

CREATE TABLE IF NOT EXISTS `clan_wars` (
  `clan1` varchar(35) NOT NULL default '',
  `clan2` varchar(35) NOT NULL default '',
  PRIMARY KEY (`clan1`,`clan2`)
);



