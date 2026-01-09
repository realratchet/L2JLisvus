--
-- Table structure for table `custom_minions`
--

DROP TABLE IF EXISTS `custom_minions`;
CREATE TABLE `custom_minions` (
  `boss_id` int NOT NULL default '0',
  `minion_id` int NOT NULL default '0',
  `amount_min` int NOT NULL default '0',
  `amount_max` int NOT NULL default '0',
  PRIMARY KEY  (`boss_id`,`minion_id`)
);