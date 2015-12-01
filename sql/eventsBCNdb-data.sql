insert into users (id, name, password, email,photo) values (UNHEX('5852AF3E7C0211E5AC27001C42B86E50'), 'juan', UNHEX(MD5('juan')), 'juan@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('5852AF3E7C0211E5AC27001C42B86E50'),'registered');

insert into users (id, name, password, email,photo) values (UNHEX('BACE58A3736C11E588E7001C42B86E50'), 'aitor', UNHEX(MD5('aitor')), 'aitor@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('BACE58A3736C11E588E7001C42B86E50'),'registered');

insert into users (id, name, password, email,photo) values (UNHEX('DBBFDC7D737411E588E7001C42B86E50'), 'empresa1', UNHEX(MD5('empresa1')), 'empresa1@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('DBBFDC7D737411E588E7001C42B86E50'),'company');

insert into users (id, name, password, email,photo) values (UNHEX('DDFEC26C737411E588E7001C42B86E50'), 'empresa2', UNHEX(MD5('empresa2')), 'empresa2@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('DDFEC26C737411E588E7001C42B86E50'),'company');


