#----------------------------
# Table structure for helper_buff_list
#----------------------------
DROP TABLE IF EXISTS `helper_buff_list`;
CREATE TABLE `helper_buff_list` (
`id` int(11) NOT NULL default '0',
`skill_id` int(10) unsigned NOT NULL default '0',
`name` varchar(25) NOT NULL default '',
`skill_level` int(10) unsigned NOT NULL default '0',
`lower_level` int(10) unsigned NOT NULL default '0',
`upper_level` int(10) unsigned NOT NULL default '0',
`is_magic_class` TINYINT UNSIGNED NOT NULL DEFAULT '0',
PRIMARY KEY  (`id`)
);

#----------------------------
# Records for table helper_buff_list
#----------------------------

insert  into helper_buff_list values 
(0, 4322, 'WindWalk', 1, 8, 24, 0),
(1, 4323, 'Shield', 1, 11, 24, 0), 
(2, 4338, 'Life Cubic', 1, 16, 19, 0), 
(3, 4324, 'Bless the Body', 1, 12, 23, 0), 
(4, 4325, 'Vampiric Rage', 1, 13, 22, 0), 
(5, 4326, 'Regeneration', 1, 14, 21, 0), 
(6, 4327, 'Haste', 1, 15, 20, 0), 
(7, 4322, 'WindWalk', 1, 8, 24, 1),
(8, 4323, 'Shield', 1, 11, 24, 1), 
(9, 4338, 'Life Cubic', 1, 16, 19, 1), 
(10, 4328, 'Bless the Soul', 1, 12, 23, 1), 
(11, 4329, 'Acumen', 1, 13, 22, 1), 
(12, 4330, 'Concentration', 1, 14, 21, 1), 
(13, 4331, 'Empower', 1, 15, 20, 1);