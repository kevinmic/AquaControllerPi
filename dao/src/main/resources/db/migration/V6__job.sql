CREATE TABLE job (
  jobId Integer NOT NULL PRIMARY KEY,
  type VARCHAR(124) NOT NULL,
  name VARCHAR(124) NOT NULL,
  jobDataJson text NOT NULL
);

