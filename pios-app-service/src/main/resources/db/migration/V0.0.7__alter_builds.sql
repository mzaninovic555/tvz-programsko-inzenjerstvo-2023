ALTER TABLE BUILDS
  ALTER COLUMN USER_ID DROP NOT NULL;

ALTER TABLE BUILDS
  ADD COLUMN FINALIZED BOOLEAN NOT NULL DEFAULT FALSE;