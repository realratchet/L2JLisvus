CREATE TABLE IF NOT EXISTS siege_clans (
   castle_id int NOT NULL default 0,
   clan_id int NOT NULL default 0,
   type int default NULL,
   PRIMARY KEY  (clan_id,castle_id)
);