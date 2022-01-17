-- ---------------------------
-- Table structure for grandboss_data
-- ---------------------------
CREATE TABLE IF NOT EXISTS grandboss_data (
  `boss_id` INTEGER NOT NULL DEFAULT 0,
  `loc_x` INTEGER NOT NULL DEFAULT 0,
  `loc_y` INTEGER NOT NULL DEFAULT 0,
  `loc_z` INTEGER NOT NULL DEFAULT 0,
  `heading` INTEGER NOT NULL DEFAULT 0,
  `respawn_time` BIGINT NOT NULL DEFAULT 0,
  `currentHP` DECIMAL(8,0) DEFAULT NULL,
  `currentMP` DECIMAL(8,0) DEFAULT NULL,
  `status` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY(`boss_id`)
);

INSERT IGNORE INTO `grandboss_data` VALUES
(12001, -21610, 181594, -5734, 0, 0, 229898, 667, 0),      -- Queen Ant
(12052, 17726, 108915, -6490, 0, 0, 162561, 575, 0),       -- Core
(12169, 55024, 17368, -5412, 0, 0, 325124, 1660, 0),   -- Orfen
(12211, 185708,114298,-8221,32768, 0, 13090000, 22197, 0), -- Antharas
(12372, 115213,16623,10080,41740, 0, 790857, 3347, 0),     -- Baium
(12374, 55275, 218880, -3217, 0, 0, 858518, 1975, 0),      -- Zaken
(12899, 213389,-115026,-1636,0, 0, 16660000, 22197, 0);  -- Valakas