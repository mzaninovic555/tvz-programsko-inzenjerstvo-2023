create table ROLES
(
  ID   SERIAL primary key,
  ROLE VARCHAR(10) unique not null
);

create table USERS
(
  ID            SERIAL      primary key,
  USERNAME      VARCHAR(20) unique not null,
  PASSWORD      TEXT,
  EMAIL         VARCHAR(100),
  DESCRIPTION   TEXT,
  CREATION_DATE TIMESTAMP                   default current_timestamp,
  IS_ACTIVATED  boolean            not null default false,
  IS_ACTIVE     boolean default true,
  ROLE_ID       BIGINT,
  ACCOUNT_TYPE  VARCHAR NOT NULL
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
  USER_ID     BIGINT,
  FINALIZED   BOOLEAN NOT NULL DEFAULT FALSE
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
  RATING  SMALLINT   not null,
  COMPONENT_ID BIGINT not null,
  USER_ID BIGINT not null
);

alter table REVIEWS
  add foreign key (USER_ID) references USERS (ID);
ALTER TABLE REVIEWS
  ADD CONSTRAINT REVIEW_UNIQUE UNIQUE(user_id, component_id);

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
  MANUFACTURER_ID BIGINT        not null
);

alter table COMPONENTS
  add foreign key (MANUFACTURER_ID) references MANUFACTURERS (ID);


alter table REVIEWS
  add foreign key (COMPONENT_ID) references COMPONENTS (ID);


create table BUILDS_COMPONENTS
(
  COMPONENT_ID BIGINT not null,
  BUILD_ID     BIGINT not null
);
alter table BUILDS_COMPONENTS
  add foreign key (COMPONENT_ID) references COMPONENTS (ID);
alter table BUILDS_COMPONENTS
  add foreign key (BUILD_ID) references BUILDS (ID);

CREATE TABLE WISHLIST
(
  ID SERIAL primary key,
  USER_ID BIGINT not null,
  COMPONENT_ID BIGINT not null,
  CREATION_DATE TIMESTAMP default current_timestamp
);

ALTER TABLE WISHLIST
  ADD FOREIGN KEY (USER_ID) references USERS (ID);

ALTER TABLE WISHLIST
  ADD FOREIGN KEY (COMPONENT_ID) references COMPONENTS (ID);
