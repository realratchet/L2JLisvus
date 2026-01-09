
-- ----------------------------
-- Table structure for auto_announcements
-- ----------------------------
CREATE TABLE IF NOT EXISTS `auto_announcements` (
  `id` INT NOT NULL,
  `initial` BIGINT NOT NULL,
  `delay` BIGINT NOT NULL,
  `cycle` INT NOT NULL,
  `memo` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`)
);
