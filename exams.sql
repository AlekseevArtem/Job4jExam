create table exams (
  id integer primary key,
  name text
);
create table questions (
  id integer primary key,
  desc text,
  correct integer,
  exam_id integer,
  foreign key(exam_id) references exams(id)
);
create table options (
  id integer primary key,
  name text,
  question_id integer references questions(id)
);
insert into exams (name) values ('Java Base Exam');
insert into questions (desc, correct, exam_id) values ('What is JDK?', 3, 1);
insert into options (name, question_id) values ('first option', 1);
insert into options (name, question_id) values ('second option', 1);
insert into options (name, question_id) values ('third correct option', 1);
insert into options (name, question_id) values ('fourth option', 1);
insert into questions (desc, correct, exam_id) values ('How many primitive variables does Java have?', 1, 1);
insert into options (name, question_id) values ('first correct option', 2);
insert into options (name, question_id) values ('second option', 2);
insert into options (name, question_id) values ('third option', 2);
insert into options (name, question_id) values ('fourth option', 2);