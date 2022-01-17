-- ----------------------------
-- Table structure for custom_droplist
-- ----------------------------
DROP TABLE IF EXISTS `custom_droplist`;
CREATE TABLE `custom_droplist` (
  `mobId` int(11) NOT NULL DEFAULT '0',
  `itemId` int(11) NOT NULL DEFAULT '0',
  `min_drop` int(11) NOT NULL DEFAULT '0',
  `max_drop` int(11) NOT NULL DEFAULT '0',
  `category` int(11) NOT NULL DEFAULT '0',
  `chance` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`mobId`,`itemId`,`category`),
  KEY `key_mobId` (`mobId`)
);