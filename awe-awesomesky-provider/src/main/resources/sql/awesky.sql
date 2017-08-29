#tb_customer
CREATE TABLE `tb_customer` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `user_account` VARCHAR(32) NOT NULL,
  `user_name` VARCHAR(8) DEFAULT NULL,
  `user_password` VARCHAR(32) NOT NULL,
  `user_phone` VARCHAR(18) DEFAULT NULL,
  `user_sex` VARCHAR(2) DEFAULT NULL,
  `user_age` INT(3) DEFAULT NULL,
  `user_birth` DATETIME DEFAULT NULL,
  `user_email` VARCHAR(64) DEFAULT NULL,
  `user_headUrl` VARCHAR(128) DEFAULT NULL,
  `user_role_id` INT(11) NOT NULL,
  `user_signature` VARCHAR(128) DEFAULT NULL,
  `user_joinTime` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8`tb_resources`

#tb_resources
CREATE TABLE `tb_resources` (
  `id` INT(32) NOT NULL AUTO_INCREMENT,
  `res_name` VARCHAR(64) DEFAULT NULL,
  `role_id` INT(32) NOT NULL,
  `res_key` VARCHAR(64) NOT NULL COMMENT '这个权限KEY是唯一的，新增时要注意',
  `res_level` INT(32) DEFAULT NULL,
  `res_url` VARCHAR(64) NOT NULL COMMENT 'URL地址．例如：/videoType/query　　不需要项目名和http://xxx:8080',
  `res_type` VARCHAR(32) DEFAULT NULL,
  `res_descr` VARCHAR(64) DEFAULT NULL COMMENT '资源描述',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8

#tb_role

CREATE TABLE `tb_role` (
  `id` INT(64) NOT NULL AUTO_INCREMENT,
  `role_key` VARCHAR(64) NOT NULL,
  `role_name` VARCHAR(18) DEFAULT NULL,
  `role_descr` VARCHAR(64) DEFAULT NULL,
  `role_enable` INT(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8