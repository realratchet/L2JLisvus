-- ---------------------------
-- Table structure for grandboss_list
-- ---------------------------

CREATE TABLE IF NOT EXISTS grandboss_list (
  `player_id` int NOT NULL,
  `zone` int NOT NULL,
  PRIMARY KEY (`player_id`,`zone`));