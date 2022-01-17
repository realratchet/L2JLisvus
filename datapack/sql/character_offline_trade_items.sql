CREATE TABLE IF NOT EXISTS `character_offline_trade_items` (
  `char_id` int(10) NOT NULL DEFAULT '0',
  `item` int(10) NOT NULL DEFAULT '0',
  `enchant_level` int(10) NOT NULL DEFAULT '0',
  `count` bigint(20) NOT NULL DEFAULT '0',
  `price` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_id`,`item`)
);