INSERT INTO BUILDS (TITLE, DESCRIPTION, LINK, IS_PUBLIC, IS_FINALIZED, USER_ID )
VALUES ('TestTitle2', 'TestDescription2', '7ebd8428-0be0-48c9-ba9c-9a6814af2fa3', true, false, 1);

INSERT INTO POSTS (ID, TITLE, CONTENT, CREATED_AT )
VALUES (1, 'TestTitle', 'TestDescription', NOW() );

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (1, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (32, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (25, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (51, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (18, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (39, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (10, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (61, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (54, 1);

INSERT INTO BUILDS (TITLE, DESCRIPTION, LINK, IS_PUBLIC, IS_FINALIZED, USER_ID )
VALUES ('TestTitle3', 'TestDescription3', '9566720e-1901-4b6d-8a3c-1c8a65231ae6', true, false, 1);


INSERT INTO BUILDS (TITLE, DESCRIPTION, LINK, IS_PUBLIC, IS_FINALIZED, USER_ID )
VALUES ('title', 'description', '0c74b16c-6f8e-418b-9001-4119777b7906', false, false, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (1, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (3, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (7, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (11, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (13, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (5, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (15, 1);

INSERT INTO BUILDS_COMPONENTS (COMPONENT_ID, BUILD_ID)
VALUES (17, 1);