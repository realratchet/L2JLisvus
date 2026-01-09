-- 
-- Table structure for table `custom_npcskills`
-- 
DROP TABLE IF EXISTS custom_npcskills;
CREATE TABLE custom_npcskills (
  npcid int NOT NULL default '0',
  skillid int NOT NULL default '0',
  level int NOT NULL default '0',
  PRIMARY KEY  (npcid,skillid,level)
);