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



ALTER TABLE `meepalika`.`order`
ADD COLUMN `orderedBy` BIGINT NOT NULL AFTER `status`;


CREATE TABLE `meepalika`.`order_details` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `ordered_by` BIGINT NOT NULL,
  `productId` BIGINT NULL,
  `quantity` BIGINT NULL,
  `created_by` BIGINT NULL,
  `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` BIGINT NULL,
  `modified_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `KK_ORDERID_idx` (`order_id` ASC) VISIBLE,
  INDEX `FK_ORDEREDBY_idx` (`ordered_by` ASC) VISIBLE,
  CONSTRAINT `FK_ORDERID`
    FOREIGN KEY (`order_id`)
    REFERENCES `meepalika`.`order` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_ORDEREDBY`
    FOREIGN KEY (`ordered_by`)
    REFERENCES `meepalika`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


ALTER TABLE `meepalika`.`order_details`
ADD COLUMN `status` VARCHAR(45) NULL AFTER `ordered_by`;


CREATE TABLE `meepalika`.`shipping_address` (
  `id` BIGINT NOT NULL,
  `order_id` BIGINT NULL,
  `address1` VARCHAR(255) NULL,
  `address2` VARCHAR(255) NULL,
  `city` VARCHAR(255) NULL,
  `state` VARCHAR(255) NULL,
  `country` VARCHAR(255) NULL,
  `contactNumber` VARCHAR(45) NULL,
  `pincode` VARCHAR(45) NULL,
  `landmark` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_SHIP_ORDERID_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `FK_SHIP_ORDERID`
    FOREIGN KEY (`order_id`)
    REFERENCES `meepalika`.`order` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



alter table `meepalika`.`shipping_address`

ADD COLUMN   `created_by` BIGINT NULL,
ADD COLUMN `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN `modified_by` BIGINT NULL,
ADD COLUMN  `modified_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

ALTER TABLE `meepalika`.`order`
ADD COLUMN `shipping_address_id` BIGINT NULL AFTER `orderedBy`;


ALTER TABLE `meepalika`.`order`
ADD INDEX `FK_ORDER_SHIPPING_idx1` (`shipping_address_id` ASC) VISIBLE;
;
ALTER TABLE `meepalika`.`order`
ADD CONSTRAINT `FK_ORDER_SHIPPING`
  FOREIGN KEY (`shipping_address_id`)
  REFERENCES `meepalika`.`shipping_address` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;



ALTER TABLE `meepalika`.`shipping_address`
DROP FOREIGN KEY `FK_SHIP_ORDERID`;
ALTER TABLE `meepalika`.`shipping_address`
;
ALTER TABLE `meepalika`.`shipping_address` ALTER INDEX `FK_SHIP_ORDERID_idx` VISIBLE;
ALTER TABLE `meepalika`.`shipping_address`
ADD CONSTRAINT `FK_SHIP_ORDERID`
  FOREIGN KEY (`order_id`)
  REFERENCES `meepalika`.`order` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
