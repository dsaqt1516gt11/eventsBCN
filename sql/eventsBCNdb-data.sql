insert into users (id, name, password, email,photo) values (UNHEX('5852AF3E7C0211E5AC27001C42B86E50'), 'juan', UNHEX(MD5('juan')), 'juan@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('5852AF3E7C0211E5AC27001C42B86E50'),'registered');

insert into users (id, name, password, email,photo) values (UNHEX('BACE58A3736C11E588E7001C42B86E50'), 'aitor', UNHEX(MD5('aitor')), 'aitor@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('BACE58A3736C11E588E7001C42B86E50'),'registered');

insert into users (id, name, password, email,photo) values (UNHEX('DBBFDC7D737411E588E7001C42B86E50'), 'empresa1', UNHEX(MD5('empresa1')), 'empresa1@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('DBBFDC7D737411E588E7001C42B86E50'),'company');

insert into users (id, name, password, email,photo) values (UNHEX('DDFEC26C737411E588E7001C42B86E50'), 'empresa2', UNHEX(MD5('empresa2')), 'empresa2@eventsbcn.com','http://www.mendozapost.com/files/image/7/7142/54b6f4c45797b_1420_!.jpg?s=270345070aa93e05e936c1b6f31c0904&d=1421508947');
insert into user_roles (userid, role) values (UNHEX('DDFEC26C737411E588E7001C42B86E50'),'company');

insert into companies(id, name, description,location,latitude,longitude,userid) values(UNHEX('21EB861C42E700E588DDFEC26C737422'),'empresa1','la empresa numero 1','Campus Nord',1,-1,UNHEX('DBBFDC7D737411E588E7001C42B86E50'));
insert into companies(id, name, description,location,latitude,longitude,userid) values(UNHEX('56EB861C42E700E588DDFEC26C737411'),'empresa2','la empresa numero 2','Campus Sud',2,-2,UNHEX('DDFEC26C737411E588E7001C42B86E50'));

insert into auth_tokens (userid, token) values (UNHEX('5852AF3E7C0211E5AC27001C42B86E50'), UNHEX('5852AF3E7C0211E5AC27001C42B86E96'));
insert into auth_tokens (userid, token) values (UNHEX('BACE58A3736C11E588E7001C42B86E50'), UNHEX('BACE58A3736C11E588E7001C42B86E97'));
insert into auth_tokens (userid, token) values (UNHEX('DBBFDC7D737411E588E7001C42B86E50'), UNHEX('DBBFDC7D737411E588E7001C42B86E98'));
insert into auth_tokens (userid, token) values (UNHEX('DDFEC26C737411E588E7001C42B86E50'), UNHEX('DDFEC26C737411E588E7001C42B86E99'));

insert into events (id,title,description,date,photo,category,companyid) values(unhex('56E8B61C42E700E588DDFEC26C737411'),'evento1','este es el evento 1','2015-12-2 12:50','http://i1.cdnds.net/13/40/300x225/movies-american-wedding-sean-william-scott.jpg','bar',unhex('21EB861C42E700E588DDFEC26C737422'));
insert into events (id,title,description,date,photo,category,companyid) values(unhex('56EB861C42E708E588DDFEC26C737411'),'evento2','este es el evento 2','2015-12-3 12:50','http://i1.cdnds.net/13/40/300x225/movies-american-wedding-sean-william-scott.jpg','cine',unhex('21EB861C42E700E588DDFEC26C737422'));
insert into events (id,title,description,date,photo,category,companyid) values(unhex('56EB861C42E700E588DDFEC26C732341'),'evento3','este es el evento 3','2015-12-4 12:50','http://i1.cdnds.net/13/40/300x225/movies-american-wedding-sean-william-scott.jpg','teatro',unhex('56EB861C42E700E588DDFEC26C737411'));
insert into events (id,title,description,date,photo,category,companyid) values(unhex('65EB861C42E700E588DDFEC26C737411'),'evento4','este es el evento 4','2015-12-5 12:50','http://i1.cdnds.net/13/40/300x225/movies-american-wedding-sean-william-scott.jpg','discoteca',unhex('56EB861C42E700E588DDFEC26C737411'));

insert into categories_user (userid, category) values(unhex('5852AF3E7C0211E5AC27001C42B86E50'),'cine');
insert into categories_user (userid, category) values(unhex('5852AF3E7C0211E5AC27001C42B86E50'),'bar');

insert into categories_user (userid, category) values(unhex('BACE58A3736C11E588E7001C42B86E50'),'discoteca');
insert into categories_user (userid, category) values(unhex('BACE58A3736C11E588E7001C42B86E50'),'restaurante');


