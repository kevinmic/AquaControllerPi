CREATE TABLE job_schedule (
  scheduleId Integer NOT NULL PRIMARY KEY,
  jobId Integer NOT NULL REFERENCES job(jobId),
  type VARCHAR(124) NOT NULL,
  cronTimer VARCHAR(124),
  intervalTimer VARCHAR(124)
);
