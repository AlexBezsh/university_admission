
-- Database schema backup. Move it to src/main/resources and Spring Boot will execute it.
-- Don't forget to place 'spring.datasource.initialization-mode=always' and 'spring.jpa.hibernate.ddl-auto=none' in application.properties
-- It is highly recommended to check model and database conformity after initialization: 'spring.jpa.hibernate.ddl-auto=validate'

-- -----------------------------------------------------
-- Schema university_admission
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `university_admission` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `university_admission` ;

DROP TABLE IF EXISTS `university_admission`.`marks` ;
DROP TABLE IF EXISTS `university_admission`.`enrollment` ;
DROP TABLE IF EXISTS `university_admission`.`faculty_subjects` ;
DROP TABLE IF EXISTS `university_admission`.`user_role` ;
DROP TABLE IF EXISTS `university_admission`.`faculty` ;
DROP TABLE IF EXISTS `university_admission`.`user` ;
DROP TABLE IF EXISTS `university_admission`.`subject` ;

-- -----------------------------------------------------
-- Table `university_admission`.`faculty`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `university_admission`.`faculty` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name_en` VARCHAR(255) NOT NULL,
  `name_ua` VARCHAR(255) NOT NULL,
  `status` ENUM('ACTIVE', 'CLOSED') NOT NULL DEFAULT 'ACTIVE',
  `description_en` TEXT NOT NULL,
  `description_ua` TEXT NOT NULL,
  `state_funded_places` INT NOT NULL,
  `contract_places` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_ua_UNIQUE` (`name_ua` ASC) INVISIBLE,
  UNIQUE INDEX `name_en_UNIQUE` (`name_en` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `university_admission`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `university_admission`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `full_name` VARCHAR(127) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `status` ENUM('ACTIVE', 'BLOCKED', 'ENROLLED') NOT NULL DEFAULT 'ACTIVE',
  `city` VARCHAR(127) NOT NULL,
  `region` VARCHAR(127) NOT NULL,
  `education` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `university_admission`.`enrollment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `university_admission`.`enrollment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `faculty_id` BIGINT NOT NULL,
  `status` ENUM('NEW', 'APPROVED', 'FINALIZED') NOT NULL DEFAULT 'NEW',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_faculty_UNIQUE` (`user_id` ASC, `faculty_id` ASC) VISIBLE,
  INDEX `FK_enrollment_faculty_idx` (`faculty_id` ASC) VISIBLE,
  CONSTRAINT `FK_enrollment_faculty`
    FOREIGN KEY (`faculty_id`)
    REFERENCES `university_admission`.`faculty` (`id`)
    ON DELETE RESTRICT,
  CONSTRAINT `FK_enrollment_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `university_admission`.`user` (`id`)
    ON DELETE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `university_admission`.`subject`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `university_admission`.`subject` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `type` ENUM('SCHOOL', 'EXAM') NOT NULL,
  `name_en` VARCHAR(127) NOT NULL,
  `name_ua` VARCHAR(127) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `type_name_ua_UNIQUE` (`type` ASC, `name_ua` ASC) INVISIBLE,
  UNIQUE INDEX `type_name_en_UNIQUE` (`type` ASC, `name_en` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 39
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `university_admission`.`faculty_subjects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `university_admission`.`faculty_subjects` (
  `faculty_id` BIGINT NOT NULL,
  `subjects_id` BIGINT NOT NULL,
  UNIQUE INDEX `faculty_subject_UNIQUE` (`faculty_id` ASC, `subjects_id` ASC) VISIBLE,
  INDEX `FK_subject_faculty` (`subjects_id` ASC) VISIBLE,
  CONSTRAINT `FK_faculty_subject`
    FOREIGN KEY (`faculty_id`)
    REFERENCES `university_admission`.`faculty` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `FK_subject_faculty`
    FOREIGN KEY (`subjects_id`)
    REFERENCES `university_admission`.`subject` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `university_admission`.`mark`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `university_admission`.`marks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `enrollment_id` BIGINT NOT NULL,
  `subject_id` BIGINT NOT NULL,
  `mark` DECIMAL(19, 2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `enrollment_subject_UNIQUE` (`enrollment_id` ASC, `subject_id` ASC) VISIBLE,
  INDEX `FK_mark_subject_idx` (`subject_id` ASC) INVISIBLE,
  CONSTRAINT `FK_mark_enrollment`
    FOREIGN KEY (`enrollment_id`)
    REFERENCES `university_admission`.`enrollment` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `FK_mark_subject`
    FOREIGN KEY (`subject_id`)
    REFERENCES `university_admission`.`subject` (`id`)
    ON DELETE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `university_admission`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `university_admission`.`user_role` (
  `user_id` BIGINT NOT NULL,
  `roles` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`user_id`, `roles`),
  CONSTRAINT `FK_user_role`
    FOREIGN KEY (`user_id`)
    REFERENCES `university_admission`.`user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;