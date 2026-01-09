-- ----------------------------
-- Table structure for leasing merchants
-- ----------------------------
CREATE TABLE IF NOT EXISTS merchant_lease (
  merchant_id int NOT NULL default 0,
  player_id int NOT NULL default 0,
  bid int,
  `type` int NOT NULL default 0,
  player_name varchar(35),
  PRIMARY KEY  (merchant_id,player_id,`type`)
);