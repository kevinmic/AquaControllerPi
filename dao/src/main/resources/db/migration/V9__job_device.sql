CREATE TABLE job_device (
  jobId Integer NOT NULL REFERENCES job(jobId),
  deviceId Integer NOT NULL REFERENCES device(deviceId),
  PRIMARY KEY (jobId, deviceId)
);
