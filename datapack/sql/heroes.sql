CREATE TABLE IF NOT EXISTS `heroes` (
  `char_id` int NOT NULL default '0',
  `class_id` tinyint unsigned NOT NULL default '0',
  `count` smallint NOT NULL default '0',
  `played` decimal(1,0) NOT NULL default '0',
  `claimed` tinyint NOT NULL default '0',
  PRIMARY KEY  (`char_id`)
);