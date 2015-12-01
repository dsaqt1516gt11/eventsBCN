insert into users (id, name, password, email,photo) values (UNHEX('5852AF3E7C0211E5AC27001C42B86E50'), 'juan', UNHEX(MD5('juan')), 'juan@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('5852AF3E7C0211E5AC27001C42B86E50'),'registered');

insert into users (id, name, password, email,photo) values (UNHEX('BACE58A3736C11E588E7001C42B86E50'), 'aitor', UNHEX(MD5('aitor')), 'aitor@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('BACE58A3736C11E588E7001C42B86E50'),'registered');

insert into users (id, name, password, email,photo) values (UNHEX('DBBFDC7D737411E588E7001C42B86E50'), 'empresa1', UNHEX(MD5('empresa1')), 'empresa1@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('DBBFDC7D737411E588E7001C42B86E50'),'company');

insert into users (id, name, password, email,photo) values (UNHEX('DDFEC26C737411E588E7001C42B86E50'), 'empresa2', UNHEX(MD5('empresa2')), 'empresa2@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('DDFEC26C737411E588E7001C42B86E50'),'company');

insert into companies (id, name, description,location,coordinate,userid) values (UNHEX('05EB861C42E700E588DDFEC26C737411'), 'empresa1', 'la empresa numero 1', 'Campus Nord',geomfromtext('point(47.65100 -122.34900)'),UNHEX('DBBFDC7D737411E588E7001C42B86E50'));
insert into companies (id, name, description,location,coordinate,userid) values (UNHEX('56EB861C42E700E588DDFEC26C737411'), 'empresa2', 'la empresa numero 2', 'Campus Sud',geomfromtext('point(49.65100 -142.34900)'),UNHEX('DDFEC26C737411E588E7001C42B86E50'));


