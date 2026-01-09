--
-- Table structure for `clan_wars`
--

CREATE TABLE IF NOT EXISTS `clan_wars` (
  `clan1` INT NOT NULL default 0,
  `clan2` INT NOT NULL default 0,
  PRIMARY KEY (`clan1`,`clan2`)
);



