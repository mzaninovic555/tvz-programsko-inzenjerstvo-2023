create table ROLES
(
  ID   SERIAL primary key,
  ROLE VARCHAR(10) unique not null
);

create table USERS
(
  ID            SERIAL      primary key,
  USERNAME      VARCHAR(20) unique not null,
  PASSWORD      TEXT               not null,
  EMAIL         VARCHAR(100)       not null,
  DESCRIPTION   TEXT,
  CREATION_DATE TIMESTAMP                   default current_timestamp,
  IS_ACTIVATED  boolean            not null default false,
  ROLE_ID       SERIAL             not null
);

alter table USERS
  add foreign key (ROLE_ID) references ROLES (ID);

create table BUILDS
(
  ID          SERIAL primary key,
  TITLE       VARCHAR(50),
  DESCRIPTION TEXT,
  LINK        VARCHAR(255) not null,
  IS_PUBLIC   BOOLEAN      not null default false,
  USER_ID     SERIAL       not null
);

alter table BUILDS
  add foreign key (USER_ID) references USERS (ID);

create table POSTS
(
  ID      SERIAL primary key,
  TITLE   VARCHAR(100) not null,
  CONTENT TEXT
);

alter table POSTS
  add foreign key (ID) references BUILDS (ID);

create table REVIEWS
(
  ID      SERIAL not null,
  RATING  CHAR   not null,
  POST_ID SERIAL not null,
  USER_ID SERIAL not null
);

alter table REVIEWS
  add foreign key (POST_ID) references POSTS (ID);
alter table REVIEWS
  add foreign key (USER_ID) references USERS (ID);

create table MANUFACTURERS
(
  ID   SERIAL primary key,
  NAME VARCHAR(50) not null
);

create table COMPONENTS
(
  ID              SERIAL primary key,
  NAME            VARCHAR(255)  not null,
  PRICE           DECIMAL(8, 2) not null,
  TYPE            VARCHAR(20)   not null,
  DATA            JSON,
  IMAGE_BASE64    TEXT,
  MANUFACTURER_ID SERIAL        not null
);

alter table COMPONENTS
  add foreign key (MANUFACTURER_ID) references MANUFACTURERS (ID);

create table BUILDS_COMPONENTS
(
  COMPONENT_ID SERIAL not null,
  BUILD_ID     SERIAL not null
);
alter table BUILDS_COMPONENTS
  add foreign key (COMPONENT_ID) references COMPONENTS (ID);
alter table BUILDS_COMPONENTS
  add foreign key (BUILD_ID) references BUILDS (ID);