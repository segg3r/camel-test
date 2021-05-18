-- User
CREATE SEQUENCE "user_seq"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL;

CREATE TABLE "user"
(    id NUMBER(19,0) not null,
     email VARCHAR2(255 CHAR) not null,
     CONSTRAINT user_pk PRIMARY KEY (id)
) TABLESPACE ${tablespace};

-- Song play
CREATE SEQUENCE  "song_play_seq"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;

CREATE TABLE "song_play"
(    id NUMBER(19,0) not null,
     duration_ms NUMBER(19,0) not null,
     song_id NUMBER(19,0) not null,
     user_id NUMBER(19,0) not null,
     review_text VARCHAR2(255 CHAR),
     CONSTRAINT song_play_pk PRIMARY KEY (id),
     CONSTRAINT song_play_user_fk
         FOREIGN KEY (user_id)
             REFERENCES "user"(id)
) TABLESPACE ${tablespace} ;

create index "song_play_user_idx" on "song_play"(user_id);