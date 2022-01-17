-- ---------------------------
-- Table structure for character_buff_schemes
-- ---------------------------
CREATE TABLE IF NOT EXISTS character_buff_schemes (
  char_id INT NOT NULL DEFAULT 0,
  scheme_name VARCHAR(16) NOT NULL,
  skills VARCHAR(500) NOT NULL,
  PRIMARY KEY (char_id,scheme_name)
);