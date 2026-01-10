-- ---------------------------
-- Table structure for character_recipebook
-- ---------------------------
CREATE TABLE IF NOT EXISTS character_recipebook (
  char_id int NOT NULL default 0,
  id int NOT NULL default 0,
  class_index INT NOT NULL DEFAULT 0,
  type INT NOT NULL default 0,
  PRIMARY KEY  (id,char_id)
);