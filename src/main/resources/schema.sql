use meepalika;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(2000) DEFAULT NULL,
  `user_registration_number` varchar(1000) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `gender` varchar(6) DEFAULT NULL,
  `address` varchar(1000) DEFAULT NULL,
  `ssn` varchar(255) DEFAULT NULL,
  `country_code` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `occupation` varchar(50) DEFAULT NULL,
  `active` tinyint DEFAULT '1',
  `zipcode` varchar(255) DEFAULT NULL,
  `user_media_storage_path` varchar(4000) DEFAULT NULL,
  `created_by` bigint NOT NULL ,
  `modified_by` bigint NOT NULL ,
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `mobile_number_UNIQUE` (`mobile_number`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `created_by` bigint NOT NULL ,
    `modified_by` bigint NOT NULL ,
    `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `is_primary_role` tinyint DEFAULT '0',
  `active` tinyint DEFAULT '1',
  `created_by` bigint NOT NULL ,
   `modified_by` bigint NOT NULL ,
   `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `modified_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_role_user_id_idx` (`user_id`),
  KEY `user_role_role_id_idx` (`role_id`),
  CONSTRAINT `user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
  CONSTRAINT `user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `logo_storage_path` varchar(1000) DEFAULT NULL,
  `account_verified` int DEFAULT '0',
    `created_by` bigint NOT NULL ,
  `modified_by` bigint NOT NULL ,
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


ALTER TABLE `meepalika`.`role`
CHANGE COLUMN `role_id` `id` BIGINT NOT NULL AUTO_INCREMENT ;


ALTER TABLE `meepalika`.`user`
ADD COLUMN `account_id` BIGINT NOT NULL AFTER `id`;

CREATE TABLE `meepalika`.`product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `price` DECIMAL(10,2) NULL,
  `active` TINYINT NULL,
  `pictureUrl` VARCHAR(1024) NULL,
  `created_by` BIGINT NULL,
  `modified_by` BIGINT NULL,
  `created_on` DATETIME NULL,
  `modified_on` DATETIME NULL,
  PRIMARY KEY (`id`));



CREATE TABLE `meepalika`.`order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NULL,
  `created_by` BIGINT NULL,
  `modified_by` BIGINT NULL,
  `created_on` DATETIME NULL,
  `modified_on` DATETIME NULL,
  PRIMARY KEY (`id`));

