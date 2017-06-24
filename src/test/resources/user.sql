DROP TABLE IF EXISTS "user";
CREATE TABLE "user" ("user_id" integer NOT NULL,"user_name" text,"password" text,"birth_date" integer,"sex" integer,"age" integer,"type" integer,"email" text,"inst" text,"address" text,"createTime" integer,"modifyTime" integer,PRIMARY KEY("user_id"));
INSERT INTO "user" VALUES (1, 'yangguo1', 'yangguo', 198601283714, 1, 32, 1, 'yangguo@outlook.com', 'basketball', 'shanghai', 201707231649, 201707231649);
INSERT INTO "user" VALUES (2, 'yangguo2', 'yangguo', 198601283714, 1, 32, 1, 'yangguo@outlook.com', 'basketball', 'shanghai', 201707231649, 201707231649);

