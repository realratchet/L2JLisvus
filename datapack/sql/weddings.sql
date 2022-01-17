-- ----------------------------
-- Table structure for `weddings`
-- ----------------------------

CREATE TABLE IF NOT EXISTS `weddings` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `player1_id` INT(11) NOT NULL DEFAULT 0,
  `player2_id` INT(11) NOT NULL DEFAULT 0,
  `married` TINYINT UNSIGNED NOT NULL DEFAULT 0,
  `affiance_date` BIGINT NOT NULL DEFAULT 0,
  `wedding_date` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY  (`id`)
);