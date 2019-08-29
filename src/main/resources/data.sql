insert into media_file(id, url) values(1, '/images/default/eastjun_profile.jpg');
insert into User(id, name, email, password, media_file_id) values (999, 'john', 'john123@example.com', '123456789', 1);
insert into User(id, name, email, password, media_file_id) values (1000, 'paul', 'paul123@example.com', '123456789', 1);
insert into User(id, name, email, password, media_file_id) values (777, 'test', 'test@test.com', '12345678', 1);
insert into Post(id, contents, count_of_like, author_id) values (999, 'ex1', 0, 999);
insert into Post(id, contents, count_of_like, author_id) values (1000, 'ex2', 0, 1000);
insert into Post(id, contents, count_of_like, author_id) values (777, 'test', 0, 777);
insert into Post(id, contents, count_of_like, author_id) values (888, 'deleted', 0, 777);