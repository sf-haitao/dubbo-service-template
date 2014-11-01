/*
Navicat MySQL Data Transfer

Source Server         : 115.29.16.189
Source Server Version : 50539
Source Host           : 115.29.16.189:3306
Source Database       : demo

Target Server Type    : MYSQL
Target Server Version : 50539
File Encoding         : 65001

Date: 2014-11-01 13:31:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `demo`
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id' ,
`username`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2

;

-- ----------------------------
-- Auto increment value for `demo`
-- ----------------------------
ALTER TABLE `demo` AUTO_INCREMENT=2;
