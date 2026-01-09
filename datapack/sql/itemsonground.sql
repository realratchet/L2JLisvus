-- ---------------------------
-- Table structure for `itemsonground`
-- ---------------------------
CREATE TABLE IF NOT EXISTS `itemsonground` (
  `object_id` int NOT NULL default '0',
  `item_id` int default NULL,
  `count` int default NULL,
  `enchant_level` int default NULL,
  `x` int default NULL,
  `y` int default NULL,
  `z` int default NULL,
  `drop_time` decimal(20,0) default NULL,
  `equipable` tinyint default '0',
  PRIMARY KEY  (`object_id`)
);