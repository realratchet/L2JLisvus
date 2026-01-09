CREATE TABLE IF NOT EXISTS `character_offline_trade` (
  `char_id` int NOT NULL,
  `time` bigint unsigned NOT NULL DEFAULT '0',
  `type` tinyint NOT NULL DEFAULT '0',
  `title` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`char_id`)
);