-- ----------------------------
-- Table structure for seven_signs_festival
-- ----------------------------
CREATE TABLE IF NOT EXISTS `seven_signs_festival` (
	`festivalId` int NOT NULL default '0',
	`cabal` varchar(4) NOT NULL default '',
	`cycle` int NOT NULL default '0',
	`date` bigint default '0',
	`score` int NOT NULL default '0',
	`members` varchar(255) NOT NULL default '',
	PRIMARY KEY (`festivalId`,`cabal`,`cycle`)
);

INSERT IGNORE INTO `seven_signs_festival` VALUES 
(0, "dawn", 1, 0, 0, ""),
(1, "dawn", 1, 0, 0, ""),
(2, "dawn", 1, 0, 0, ""),
(3, "dawn", 1, 0, 0, ""),
(4, "dawn", 1, 0, 0, ""),
(0, "dusk", 1, 0, 0, ""),
(1, "dusk", 1, 0, 0, ""),
(2, "dusk", 1, 0, 0, ""),
(3, "dusk", 1, 0, 0, ""),
(4, "dusk", 1, 0, 0, "");