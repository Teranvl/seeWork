CREATE DATABASE university;

USE university;

create table student_accounts
(
id int not null auto_increment,
primary key(id),
login_value varchar(50) not null,
password_value varchar(50) not null
);

insert into student_accounts
(login_value, 
password_value)
values
('student', 'student'),
('student', 'free'),
('s', '1');

create table teacher_accounts
(
id int not null auto_increment,
primary key(id),
login_value varchar(50) not null,
password_value varchar(50) not null
);

insert into teacher_accounts
(login_value, 
password_value)
values
('teacher', 'super'),
('teacher', 'pyper'),
('t', '1');

CREATE TABLE students
(
student_id INT NOT NULL AUTO_INCREMENT,
PRIMARY KEY(student_id),
first_name VARCHAR(30) NOT NULL,
last_name VARCHAR(30) NOT NULL
);