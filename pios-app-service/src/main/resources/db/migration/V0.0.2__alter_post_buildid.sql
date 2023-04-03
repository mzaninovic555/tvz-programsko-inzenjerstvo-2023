drop table if exists posts;

create table POSTS
(
  id         integer primary key,
  title      varchar(100) not null,
  content    text,
  created_at timestamp not null default current_timestamp
);

alter table POSTS
  add foreign key (ID) references BUILDS (ID);
