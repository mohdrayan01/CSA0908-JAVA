CREATE DATABASE IF NOT EXISTS campus_events;
USE campus_events;
CREATE TABLE IF NOT EXISTS events(
 event_id INT AUTO_INCREMENT PRIMARY KEY,
 event_name VARCHAR(120) NOT NULL UNIQUE);
CREATE TABLE IF NOT EXISTS registrations(
 registration_id INT AUTO_INCREMENT PRIMARY KEY,
 student_id VARCHAR(20) NOT NULL,
 student_name VARCHAR(120) NOT NULL,
 email VARCHAR(180) NOT NULL,
 event_id INT NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT uq_student_event UNIQUE(student_id,event_id),
 CONSTRAINT fk_registration_event FOREIGN KEY(event_id) REFERENCES events(event_id));
INSERT INTO events(event_name) VALUES ('Cultural Night'),('Photography Workshop'),('Music Fest')
ON DUPLICATE KEY UPDATE event_name=VALUES(event_name);