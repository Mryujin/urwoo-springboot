/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50027
Source Host           : localhost:3306
Source Database       : springboot-shiro

Target Server Type    : MYSQL
Target Server Version : 50027
File Encoding         : 65001

Date: 2017-05-03 01:32:20
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `shiro_rights`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_rights`;
CREATE TABLE `shiro_rights` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(32) collate utf8_bin NOT NULL,
  `type` int(1) NOT NULL,
  `uri` varchar(64) collate utf8_bin NOT NULL,
  `right_code` varchar(64) collate utf8_bin NOT NULL,
  `parent_id` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of shiro_rights
-- ----------------------------

-- ----------------------------
-- Table structure for `shiro_role_rights`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role_rights`;
CREATE TABLE `shiro_role_rights` (
  `id` int(11) NOT NULL auto_increment,
  `role_id` int(11) NOT NULL,
  `right_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of shiro_role_rights
-- ----------------------------

-- ----------------------------
-- Table structure for `shiro_roles`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_roles`;
CREATE TABLE `shiro_roles` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(32) collate utf8_bin NOT NULL,
  `role_code` varchar(32) collate utf8_bin default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of shiro_roles
-- ----------------------------

-- ----------------------------
-- Table structure for `shiro_user_roles`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user_roles`;
CREATE TABLE `shiro_user_roles` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of shiro_user_roles
-- ----------------------------

-- ----------------------------
-- Table structure for `shiro_users`
-- ----------------------------
DROP TABLE IF EXISTS `shiro_users`;
CREATE TABLE `shiro_users` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(32) collate utf8_bin NOT NULL,
  `username` varchar(32) collate utf8_bin NOT NULL,
  `password` varchar(32) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of shiro_users
-- ----------------------------
INSERT INTO `shiro_users` VALUES ('1', 'zs', 'zhangsan', '123');
INSERT INTO `shiro_users` VALUES ('2', 'ls', 'lisi', '123');
