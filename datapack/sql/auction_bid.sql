-- ---------------------------
-- Table structure for auction_bid
-- ---------------------------
CREATE TABLE IF NOT EXISTS auction_bid (
  hall_id int(2) NOT NULL default 0,
  bidderId int NOT NULL default 0,
  bidderName varchar(50) NOT NULL,
  clan_name varchar(50) NOT NULL,
  maxBid int(11) NOT NULL default 0,
  time_bid decimal(20,0) NOT NULL default '0',
  PRIMARY KEY  (hall_id, bidderId)
);
