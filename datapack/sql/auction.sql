-- ---------------------------
-- Table structure for auction
-- ---------------------------
CREATE TABLE IF NOT EXISTS `auction` (
  hall_id int(2) NOT NULL default '0',
  sellerId int(11) NOT NULL default '0',
  sellerName varchar(50) NOT NULL default 'NPC',
  sellerClanName varchar(50) NOT NULL default '',
  bid int(11) NOT NULL default '0',
  endDate decimal(20,0) NOT NULL default '0',
  PRIMARY KEY  (`hall_id`)
);