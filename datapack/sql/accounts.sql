-- ---------------------------
-- Table structure for accounts
-- ---------------------------
CREATE TABLE IF NOT EXISTS `accounts` (
  `login` VARCHAR(45) NOT NULL default '',
  `password` VARCHAR(45) ,
  `last_active` DECIMAL(20),
  `access_level` INT,
  `last_ip` VARCHAR(20),
  `last_server` int(4) default 1,
  PRIMARY KEY (`login`)
);
