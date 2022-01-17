-- 
-- Table structure for table `custom_npcskills`
-- 
DROP TABLE IF EXISTS custom_npcskills;
CREATE TABLE custom_npcskills (
  npcid int(11) NOT NULL default '0',
  skillid int(11) NOT NULL default '0',
  level int(11) NOT NULL default '0',
  PRIMARY KEY  (npcid,skillid,level)
);