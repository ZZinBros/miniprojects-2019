insert into media_file(id, url) values(1, '/images/default/eastjun_profile.jpg');
insert into User(id, name, email, password, media_file_id) values (999, 'john', 'john123@example.com', '123456789', 1);
insert into User(id, name, email, password, media_file_id) values (1000, 'paul', 'paul123@example.com', '123456789', 1);
insert into User(id, name, email, password, media_file_id) values (777, 'test', 'test@test.com', '12345678', 1);
insert into User(id, name, email, password, media_file_id) values (444, 'friend1', 'friend1@test.com', '12345678', 1);
insert into User(id, name, email, password, media_file_id) values (445, 'friend2', 'friend2@test.com', '12345678', 1);

insert into Post(id, contents, count_of_like, author_id) values (999, 'ex1', 0, 999);
insert into Post(id, contents, count_of_like, author_id) values (1000, 'ex2', 0, 1000);
insert into Post(id, contents, count_of_like, author_id) values (777, 'test', 0, 777);
insert into Post(id, contents, count_of_like, author_id) values (888, 'deleted', 0, 777);

insert into Friend(id, owner_id, slave_id) values (444, 444, 445);
insert into Friend(id, owner_id, slave_id) values (445, 445, 444);

insert into post_notification(id, checked, type, notified_user_id, post_id, publisher_id) values (999, false, 'CREATED', 777, 999, 1000);
insert into post_notification(id, checked, type, notified_user_id, post_id, publisher_id) values (1000, false, 'CREATED', 777, 1000, 1000);
insert into post_notification(id, checked, type, notified_user_id, post_id, publisher_id) values (1001, false, 'CREATED', 777, 777, 1000);
insert into post_notification(id, checked, type, notified_user_id, post_id, publisher_id) values (1002, false, 'CREATED', 777, 888, 1000);
