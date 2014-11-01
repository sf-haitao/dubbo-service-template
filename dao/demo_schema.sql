use demo;

-- ----------------------------
-- Table structure for `demo`
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id' ,
`username`  varchar(255) NULL DEFAULT NULL COMMENT '用户名' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;
