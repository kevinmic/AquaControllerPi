CREATE TABLE action (
  actionId Integer NOT NULL PRIMARY KEY,
  actionType VARCHAR(124) NOT NULL,
  name VARCHAR(124) NOT NULL,
  actionJson text NOT NULL
);

