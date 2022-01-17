-- ----------------------------
-- Table structure for custom_etcitem
-- ----------------------------
DROP TABLE IF EXISTS `custom_etcitem`;
CREATE TABLE `custom_etcitem` (
  `item_id` decimal(11,0) NOT NULL DEFAULT '0',
  `name` varchar(100) DEFAULT NULL,
  `crystallizable` varchar(5) DEFAULT NULL,
  `item_type` varchar(14) DEFAULT NULL,
  `weight` decimal(4,0) DEFAULT NULL,
  `consume_type` varchar(9) DEFAULT NULL,
  `material` varchar(11) DEFAULT NULL,
  `crystal_type` varchar(4) NOT NULL DEFAULT 'none',
  `price` decimal(11,0) DEFAULT NULL,
  `crystal_count` int(4) DEFAULT NULL,
  `sellable` varchar(5) DEFAULT 'true',
  `dropable` varchar(5) DEFAULT 'true',
  `destroyable` varchar(5) DEFAULT 'true',
  `tradeable` varchar(5) DEFAULT 'true',
  `oldname` varchar(100) NOT NULL DEFAULT '',
  `oldtype` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`item_id`)
);