BEGIN;

CREATE TABLE `users` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(100) NOT NULL,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `password` VARCHAR(255) NULL DEFAULT NULL,
    `role` VARCHAR(20) NOT NULL,
    `sign_in_provider` VARCHAR(20) NULL DEFAULT NULL,
    `created_date` DATETIME NOT NULL,
    `last_modified_date` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `UsersonEmail` (`email`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB 
AUTO_INCREMENT=1;

COMMIT;