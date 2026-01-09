-- ---------------------------
-- Table structure for `clanhall_functions`
-- ---------------------------
CREATE TABLE IF NOT EXISTS `clanhall_functions` (
  `hall_id` int NOT NULL default '0',
  `type` int NOT NULL default '0',
  `lvl` int NOT NULL default '0',
  `lease` int NOT NULL default '0',
  `rate` decimal(20,0) NOT NULL default '0',
  `endTime` decimal(20,0) NOT NULL default '0',
  PRIMARY KEY  (`hall_id`,`type`)
);
