create table gift_certificate
(
    id               int identity,
    name             varchar(45) not null,
    description      varchar(300)  not null,
    duration         int         not null,
    create_date      datetime    not null,
    last_update_date datetime    not null,
    price            double      not null
);

create table tag
(
    id   int identity,
    name varchar(45) null
);

create table certificate_tag_mapping
(
    id             int identity,
    certificate_id int not null,
    tag_id         int not null,
    foreign key (certificate_id) references gift_certificate (id)
    on delete cascade,
    foreign key (tag_id) references tag (id)
    on delete cascade
);


-- insert some test data
INSERT INTO gift_certificate (name, description, duration, create_date, last_update_date, price)
VALUES ('Free ride', 'nice gift', 10, '2005-10-11 19:01:30', '2005-10-11 19:01:30', 12.7);

INSERT INTO gift_certificate (name, description, duration, create_date, last_update_date, price)
VALUES ('Free shopping', 'good certificate', 20, '2010-09-03 13:09:30', '2010-09-03 13:09:30', 45.0);

INSERT INTO gift_certificate (name, description, duration, create_date, last_update_date, price)
VALUES ('Free meals', 'ok certificate', 30, '2018-06-01 08:46:30', '2018-06-01 08:46:30', 60.0);

INSERT INTO gift_certificate (name, description, duration, create_date, last_update_date, price)
VALUES ('5 free restaurant visits', 'you can order meals up to 100$ each visit', 60,
        '2019-11-22 20:31:10', '2019-11-22 20:31:10', 140.0);

INSERT INTO gift_certificate (name, description, duration, create_date, last_update_date, price)
VALUES ('trip to the amusement park', 'explore park with your family', 40,
        '2020-12-20 18:31:10', '2020-12-20 18:31:10', 190.0);

INSERT INTO tag (name) VALUES ('amusement park');
INSERT INTO tag (name) VALUES ('restaurant');
INSERT INTO tag (name) VALUES ('clothes shopping');
INSERT INTO tag (name) VALUES ('motorbike ride');
INSERT INTO tag (name) VALUES ('family');
INSERT INTO tag (name) VALUES ('one person');
INSERT INTO tag (name) VALUES ('traveling');
INSERT INTO tag (name) VALUES ('fun');

INSERT INTO certificate_tag_mapping (certificate_id, tag_id) VALUES (3, 1);
INSERT INTO certificate_tag_mapping (certificate_id, tag_id) VALUES (3, 5);
INSERT INTO certificate_tag_mapping (certificate_id, tag_id) VALUES (3, 7);
INSERT INTO certificate_tag_mapping (certificate_id, tag_id) VALUES (4, 0);
INSERT INTO certificate_tag_mapping (certificate_id, tag_id) VALUES (4, 4);
INSERT INTO certificate_tag_mapping (certificate_id, tag_id) VALUES (4, 7);