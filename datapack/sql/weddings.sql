-- ----------------------------
-- Table structure for `weddings`
-- ----------------------------

CREATE TABLE IF NOT EXISTS `weddings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `player1_id` INT NOT NULL DEFAULT 0,
  `player2_id` INT NOT NULL DEFAULT 0,
  `married` TINYINT UNSIGNED NOT NULL DEFAULT 0,
  `affiance_date` BIGINT NOT NULL DEFAULT 0,
  `wedding_date` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY  (`id`)
);