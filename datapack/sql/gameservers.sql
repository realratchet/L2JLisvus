CREATE TABLE IF NOT EXISTS `gameservers` (
  `server_id` int NOT NULL default '0',
  `hexid` varchar(50) NOT NULL default '',
  `host` varchar(50) NOT NULL default '',
  PRIMARY KEY  (`server_id`)
);