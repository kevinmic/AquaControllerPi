CREATE TABLE action_schedule (
  scheduleId Integer NOT NULL PRIMARY KEY,
  actionId Integer NOT NULL REFERENCES action(actionId),
  type VARCHAR(124) NOT NULL,
  cronTimer VARCHAR(124),
  intervalTimer VARCHAR(124)
);
