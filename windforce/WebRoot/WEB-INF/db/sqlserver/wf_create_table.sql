CREATE TABLE po_orgtype
(
   sid            VARCHAR (32) NOT NULL,
   orgtypename    VARCHAR (100) NOT NULL,
   orgtypestate   SMALLINT DEFAULT (0) NOT NULL,
   memo           VARCHAR (512),
   tag            VARCHAR (512)
);

ALTER TABLE po_orgtype ADD CONSTRAINT pk_po_orgtype PRIMARY KEY(sid);

CREATE TABLE po_peopleinfo
(
   sid                    VARCHAR (32) NOT NULL,
   peoplecode             VARCHAR (20) NOT NULL,
   peoplename             VARCHAR (100) NOT NULL,
   peoplegender           TINYINT DEFAULT (0),
   pwd                    VARCHAR (64) NOT NULL,
   peoplestate            SMALLINT DEFAULT (0) NOT NULL,
   organizeinfo           VARCHAR (32) NOT NULL,
   defaultdesktop         TINYINT NOT NULL,
   memo                   VARCHAR (512),
   tag                    VARCHAR (512),
   defaultconfig          VARCHAR (32),
   lastchangepwdtime      DATETIME,
   passwordErrCount       INT,
   recentPasswordRecord   VARCHAR (512)
);

ALTER TABLE po_peopleinfo ADD CONSTRAINT pk_po_peopleinfo PRIMARY KEY (sid);

CREATE INDEX idx_po_peoplecode
   ON po_peopleinfo (peoplecode ASC);

CREATE TABLE poam_operateLogInfo
(
   sid                 VARCHAR (32) NOT NULL,
   manageType          INT NOT NULL,
   manageObjectType    INT NOT NULL,
   manageObjectSid     VARCHAR (32) NOT NULL,
   peopleCode          VARCHAR (20),
   peopleName          VARCHAR (100),
   bankNo              VARCHAR (32),
   roleName            VARCHAR (100),
   roleMemo            VARCHAR (512),
   orgTypeName         VARCHAR (100),
   orgTypeMemo         VARCHAR (512),
   operateMemo         VARCHAR (512),
   operatePeopleSid    VARCHAR (32) NOT NULL,
   operatePeopleCode   VARCHAR (20) NOT NULL,
   operatePeopleName   VARCHAR (100) NOT NULL,
   operateDate         DATETIME NOT NULL
);

ALTER TABLE poam_operateLogInfo ADD CONSTRAINT pk_poam_operateLog PRIMARY KEY (sid);

CREATE TABLE po_currentlogin
(
   loginid     NUMERIC (19, 0) IDENTITY (1, 1) NOT NULL,
   peoplesid   VARCHAR (32) NOT NULL,
   loginip     VARCHAR (20) NOT NULL,
   sessionid   VARCHAR (128) NOT NULL,
   logindate   VARCHAR (10) NOT NULL,
   logintime   VARCHAR (10) NOT NULL
);

ALTER TABLE po_currentlogin ADD CONSTRAINT pk_po_currentlogin PRIMARY KEY (loginid);



CREATE INDEX idx_po_cl_ps
   ON po_currentlogin (peoplesid ASC);



CREATE INDEX idx_po_cl_si
   ON po_currentlogin (sessionid ASC);



CREATE TABLE Param_ModuleFlow
(
   autoid NUMERIC (19, 0) IDENTITY (1,1) NOT NULL,
   ModuleID             VARCHAR (32),
   AppId                VARCHAR (32),
   ProcessDefKey        VARCHAR (32),
   ProcessDefShowname   VARCHAR (32),
   TaskName             VARCHAR (32),
   TaskShowname         VARCHAR (32)
);

ALTER TABLE Param_ModuleFlow ADD CONSTRAINT pk_Param_ModuleFlow PRIMARY KEY (autoid);



CREATE TABLE mm_moduleinfo
(
   sid               VARCHAR (32) NOT NULL,
   parent_sid        VARCHAR (32),
   name              VARCHAR (50),
   entrypoint_url    VARCHAR (200) NOT NULL,
   entrypoint_type   VARCHAR (20),
   showname          VARCHAR (200),
   isNavigation      INT DEFAULT (0) NOT NULL,
   istaskview        INT DEFAULT (0) NOT NULL,
   hotkeystr         VARCHAR (10),
   order_id          INT NOT NULL,
   module_level      INT,
   tag               VARCHAR (64),
   state             INT
);

ALTER TABLE mm_moduleinfo ADD CONSTRAINT pk_mm_moduleinfo PRIMARY KEY (sid);



CREATE INDEX idx_mm_mi_url
   ON mm_moduleinfo (entrypoint_url ASC);

ALTER TABLE mm_moduleinfo ADD CONSTRAINT FK_mm_mi_mi FOREIGN KEY (parent_sid) REFERENCES mm_moduleinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;


CREATE TABLE am_powerinfo
(
   sid           VARCHAR (32) NOT NULL,
   powername     VARCHAR (100) NOT NULL,
   powerstate    SMALLINT DEFAULT (0) NOT NULL,
   parentpower   VARCHAR (32),
   resourceid    VARCHAR (64) NOT NULL,
   orderid       SMALLINT NOT NULL,
   memo          VARCHAR (512),
   tag           VARCHAR (512)
);

ALTER TABLE am_powerinfo ADD CONSTRAINT pk_am_powerinfo PRIMARY KEY (sid);



CREATE INDEX idx_am_pi_pp
   ON am_powerinfo (parentpower ASC);



CREATE INDEX idx_am_pi_ri
   ON am_powerinfo (resourceid ASC);

ALTER TABLE am_powerinfo ADD CONSTRAINT fk_am_pi_pi FOREIGN KEY (parentpower) REFERENCES am_powerinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;



CREATE TABLE am_powermutex
(
   powersid1   VARCHAR (32) NOT NULL,
   powersid2   VARCHAR (32) NOT NULL
);

ALTER TABLE am_powermutex ADD CONSTRAINT pk_am_powermutex PRIMARY KEY (powersid1, powersid2);
ALTER TABLE am_powermutex ADD CONSTRAINT fk_am_pi_pm2 FOREIGN KEY (powersid2) REFERENCES am_powerinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE am_powermutex ADD CONSTRAINT fk_am_pi_pm1 FOREIGN KEY (powersid1) REFERENCES am_powerinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;



CREATE TABLE am_rolegroupinfo
(
   sid              VARCHAR (32) NOT NULL,
   rolegroupname    VARCHAR (100) NOT NULL,
   rolegroupstate   SMALLINT DEFAULT (0) NOT NULL,
   orderid          SMALLINT NOT NULL,
   memo             VARCHAR (512),
   tag              VARCHAR (512)
);

ALTER TABLE am_rolegroupinfo ADD CONSTRAINT pk_am_rolegroupinfo PRIMARY KEY (sid);


CREATE TABLE am_rolegroupmutex
(
   rolegroupsid1   VARCHAR (32) NOT NULL,
   rolegroupsid2   VARCHAR (32) NOT NULL
);

ALTER TABLE am_rolegroupmutex ADD CONSTRAINT pk_am_rolegroupmute PRIMARY KEY (rolegroupsid1, rolegroupsid2);
ALTER TABLE am_rolegroupmutex ADD CONSTRAINT fk_am_rgi_rgm1 FOREIGN KEY (rolegroupsid1) REFERENCES am_rolegroupinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE am_rolegroupmutex ADD CONSTRAINT fk_am_rgi_rgm2 FOREIGN KEY (rolegroupsid2) REFERENCES am_rolegroupinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;


CREATE TABLE am_roleinfo
(
   sid         VARCHAR (32) NOT NULL,
   rolename    VARCHAR (100) NOT NULL,
   rolestate   SMALLINT DEFAULT (0) NOT NULL,
   orderid     SMALLINT NOT NULL,
   memo        VARCHAR (512),
   tag         VARCHAR (512)
);

ALTER TABLE am_roleinfo ADD CONSTRAINT pk_am_roleinfo PRIMARY KEY (sid);



CREATE TABLE am_rolemutex
(
   rolesid1   VARCHAR (32) NOT NULL,
   rolesid2   VARCHAR (32) NOT NULL
);

ALTER TABLE am_rolemutex ADD CONSTRAINT pk_am_rolemutex PRIMARY KEY (rolesid1, rolesid2);
ALTER TABLE am_rolemutex ADD CONSTRAINT fk_am_ri_rm2 FOREIGN KEY (rolesid2) REFERENCES am_roleinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE am_rolemutex ADD CONSTRAINT fk_am_ri_rm1 FOREIGN KEY (rolesid1) REFERENCES am_roleinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;



CREATE TABLE am_rolegrouporgtype
(
   orgtypesid     VARCHAR (32) NOT NULL,
   rolegroupsid   VARCHAR (32) NOT NULL
);

ALTER TABLE am_rolegrouporgtype ADD CONSTRAINT pk_am_rolegrouporgt PRIMARY KEY (orgtypesid, rolegroupsid);
ALTER TABLE am_rolegrouporgtype ADD CONSTRAINT fk_am_rgi_ot FOREIGN KEY (rolegroupsid) REFERENCES am_rolegroupinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE am_rolegrouporgtype ADD CONSTRAINT fk_po_ot_rgi FOREIGN KEY (orgtypesid) REFERENCES po_orgtype (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;


CREATE TABLE am_rolegrouppeople
(
   peoplesid      VARCHAR (32) NOT NULL,
   rolegroupsid   VARCHAR (32) NOT NULL
);

ALTER TABLE am_rolegrouppeople ADD CONSTRAINT pk_am_rolegrouppeop PRIMARY KEY (peoplesid, rolegroupsid);
ALTER TABLE am_rolegrouppeople ADD CONSTRAINT fk_po_rgi_pi FOREIGN KEY (rolegroupsid) REFERENCES am_rolegroupinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE am_rolegrouppeople ADD CONSTRAINT fk_po_pi_rgi FOREIGN KEY (peoplesid) REFERENCES po_peopleinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;



CREATE TABLE am_rolegrouprole
(
   rolegroupsid   VARCHAR (32) NOT NULL,
   rolesid        VARCHAR (32) NOT NULL
);

ALTER TABLE am_rolegrouprole ADD CONSTRAINT pk_am_rolegrouprole PRIMARY KEY (rolesid, rolegroupsid);
ALTER TABLE am_rolegrouprole ADD CONSTRAINT fk_am_rgi_ri FOREIGN KEY (rolegroupsid) REFERENCES am_rolegroupinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE am_rolegrouprole ADD CONSTRAINT fk_am_ri_rgi FOREIGN KEY (rolesid) REFERENCES am_roleinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;


CREATE TABLE am_rolepower
(
   rolesid    VARCHAR (32) NOT NULL,
   powersid   VARCHAR (32) NOT NULL
);

ALTER TABLE am_rolepower ADD CONSTRAINT pk_am_rolepower PRIMARY KEY (powersid, rolesid);
ALTER TABLE am_rolepower ADD CONSTRAINT fk_am_pi_ri FOREIGN KEY (powersid) REFERENCES am_powerinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE am_rolepower ADD CONSTRAINT fk_am_ri_pi FOREIGN KEY (rolesid) REFERENCES am_roleinfo (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE po_organizeinfo
(
   sid            VARCHAR (32) NOT NULL,
   orgname        VARCHAR (100) NOT NULL,
   orgno          VARCHAR (32) NOT NULL,
   orgtype        VARCHAR (32) NOT NULL,
   orgstate       SMALLINT DEFAULT (0) NOT NULL,
   parentorg      VARCHAR (32),
   managetype     TINYINT DEFAULT (1) NOT NULL,
   includechild   TINYINT DEFAULT (1) NOT NULL,
   memo           VARCHAR (512),
   tag            VARCHAR (512),
   orgLevel       INT,
   orgPath        VARCHAR (128)
);

ALTER TABLE po_organizeinfo ADD CONSTRAINT pk_po_organizeinfo PRIMARY KEY (sid);


CREATE INDEX idx_po_oi_ot
   ON po_organizeinfo (orgtype ASC);



CREATE INDEX idx_po_oi_po
   ON po_organizeinfo (parentorg ASC);

ALTER TABLE po_organizeinfo ADD CONSTRAINT fk_po_ot_oi FOREIGN KEY (orgtype) REFERENCES po_orgtype (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;



CREATE TABLE po_orgtyperelate
(
   parentorgtype   VARCHAR (32) NOT NULL,
   childorgtype    VARCHAR (32) NOT NULL,
   countlimit      INT DEFAULT ( (-1)) NOT NULL
);

ALTER TABLE po_orgtyperelate ADD CONSTRAINT pk_po_orgtyperelate PRIMARY KEY (parentorgtype, childorgtype);
ALTER TABLE po_orgtyperelate ADD CONSTRAINT fk_po_ot_otrp FOREIGN KEY (parentorgtype) REFERENCES po_orgtype (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE po_orgtyperelate ADD CONSTRAINT fk_po_ot_otrc FOREIGN KEY (childorgtype) REFERENCES po_orgtype (sid) ON UPDATE NO ACTION ON DELETE NO ACTION;