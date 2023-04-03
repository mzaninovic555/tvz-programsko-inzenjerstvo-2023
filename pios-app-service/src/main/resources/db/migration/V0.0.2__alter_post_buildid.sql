drop table if exists posts;

create table POSTS
(
  ID      integer primary key,
  TITLE   VARCHAR(100) not null,
  CONTENT TEXT
);

alter table POSTS
  add foreign key (ID) references BUILDS (ID);
