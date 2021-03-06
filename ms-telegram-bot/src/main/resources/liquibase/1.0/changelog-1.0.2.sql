INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 14, 'REPLY_END', NULL);
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 11, 'NEXT', NULL);
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 13, 'REPLY',
        '\+(9[976]\d|8[987530]\d|6[987]\d|5[90]\d|42\d|3[875]\d|2[98654321]\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\d{1,14}$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 2, 'tourType', NULL);
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 15, 'CONTACT_INFO', NULL);
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 7, 'budget', '^\d{0,10}$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 5, 'travelStartDate',
        '^((19|20)\d\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 9, 'travelEndDate',
        '^((19|20)\d\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 1, NULL, NULL);
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 4, 'addressFrom', '^.{2,50}$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 8, NULL, '^.{2,50}$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 12, 'REPLY_START', '^.{2,50}$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 10, 'addressToUser', '^.{2,50}$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 3, 'addressToType', '^.{2,50}$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 6, 'travellerCount', '^.{1,50}$');
INSERT INTO "questions"(created_at, updated_at, id, state, regex)
VALUES (Current_timestamp, Current_timestamp, 16, 'END', NULL);