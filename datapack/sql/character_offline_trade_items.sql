CREATE TABLE IF NOT EXISTS `character_offline_trade_items` (
  `char_id` int NOT NULL DEFAULT '0',
  `item` int NOT NULL DEFAULT '0',
  `enchant_level` int NOT NULL DEFAULT '0',
  `count` bigint NOT NULL DEFAULT '0',
  `price` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_id`,`item`)
);