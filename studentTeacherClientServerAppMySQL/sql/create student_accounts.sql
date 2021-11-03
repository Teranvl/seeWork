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