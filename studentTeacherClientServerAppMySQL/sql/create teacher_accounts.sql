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