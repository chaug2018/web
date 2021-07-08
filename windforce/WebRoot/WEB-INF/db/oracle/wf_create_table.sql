CREATE TABLE PO_ORGTYPE
(
   sid            VARCHAR2 (32) NOT NULL,
   orgtypename    VARCHAR2 (100) NOT NULL,
   orgtypestate   NUMBER (5) DEFAULT (0) NOT NULL,
   memo           VARCHAR2 (512),
   tag            VARCHAR2 (512)
)
TABLESPACE WFDATA;

ALTER TABLE po_orgtype ADD CONSTRAINT pk_po_orgtype PRIMARY KEY(sid);



CREATE TABLE PO_PEOPLEINFO
(
   sid                    VARCHAR2 (32) NOT NULL,
   peoplecode             VARCHAR2 (20) NOT NULL,
   peoplename             VARCHAR2 (100) NOT NULL,
   peoplegender           NUMBER (3) DEFAULT (0),
   pwd                    VARCHAR2 (64) NOT NULL,
   peoplestate            NUMBER (5) DEFAULT (0) NOT NULL,
   organizeinfo           VARCHAR2 (32) NOT NULL,
   defaultdesktop         NUMBER (3) NOT NULL,
   memo                   VARCHAR2 (512),
   tag                    VARCHAR2 (512),
   defaultconfig          VARCHAR2 (32),
   lastchangepwdtime      DATE,
   passwordErrCount       NUMBER (10),
   recentPasswordRecord   VARCHAR2 (512)
)
TABLESPACE WFDATA;

ALTER TABLE po_peopleinfo ADD CONSTRAINT pk_po_peopleinfo PRIMARY KEY (sid);



CREATE INDEX idx_po_peoplecode
   ON po_peopleinfo (peoplecode ASC)
   TABLESPACE WFIDX;



CREATE TABLE poam_operateLogInfo
(
   sid                 VARCHAR2 (32) NOT NULL,
   manageType          NUMBER (10) NOT NULL,
   manageObjectType    NUMBER (10) NOT NULL,
   manageObjectSid     VARCHAR (32) NOT NULL,
   peopleCode          VARCHAR2 (20),
   peopleName          VARCHAR2 (100),
   bankNo              VARCHAR2 (32),
   roleName            VARCHAR2 (100),
   roleMemo            VARCHAR2 (512),
   orgTypeName         VARCHAR2 (100),
   orgTypeMemo         VARCHAR2 (512),
   operateMemo         VARCHAR2 (512),
   operatePeopleSid    VARCHAR2 (32) NOT NULL,
   operatePeopleCode   VARCHAR2 (20) NOT NULL,
   operatePeopleName   VARCHAR2 (100) NOT NULL,
   operateDate         DATE NOT NULL
)
TABLESPACE WFDATA;

ALTER TABLE poam_operateLogInfo ADD CONSTRAINT pk_poam_operateLog PRIMARY KEY (sid);


CREATE TABLE po_currentlogin
(
   loginid     NUMBER (19) NOT NULL,
   peoplesid   VARCHAR2 (32) NOT NULL,
   loginip     VARCHAR2 (20) NOT NULL,
   sessionid   VARCHAR2 (128) NOT NULL,
   logindate   VARCHAR2 (10) NOT NULL,
   logintime   VARCHAR2 (10) NOT NULL
)
TABLESPACE WFDATA;



CREATE SEQUENCE PO_CL_LI
   START WITH 1
   MAXVALUE 999999999999999999999999999
   MINVALUE 1
   NOCYCLE
   CACHE 20
   NOORDER;


ALTER TABLE po_currentlogin ADD CONSTRAINT pk_po_currentlogin PRIMARY KEY (loginid);



CREATE INDEX idx_po_cl_ps
   ON po_currentlogin (peoplesid ASC)
   TABLESPACE WFIDX;



CREATE INDEX idx_po_cl_si
   ON po_currentlogin (sessionid ASC)
   TABLESPACE WFIDX;



CREATE TABLE Param_ModuleFlow
(
   autoid               NUMBER (19) NOT NULL,
   ModuleID             VARCHAR2 (32),
   AppId                VARCHAR2 (32),
   ProcessDefKey        VARCHAR2 (32),
   ProcessDefShowname   VARCHAR2 (32),
   TaskName             VARCHAR2 (32),
   TaskShowname         VARCHAR2 (32)
)
TABLESPACE WFDATA;



CREATE SEQUENCE PARAMGROUP_ID
   START WITH 1
   MAXVALUE 999999999999999999999999999
   MINVALUE 1
   NOCYCLE
   CACHE 20
   NOORDER;


ALTER TABLE Param_ModuleFlow ADD CONSTRAINT pk_Param_ModuleFlow PRIMARY KEY (autoid);



CREATE TABLE mm_moduleinfo
(
   sid               VARCHAR2 (32) NOT NULL,
   parent_sid        VARCHAR2 (32),
   name              VARCHAR2 (50),
   entrypoint_url    VARCHAR2 (200) NOT NULL,
   entrypoint_type   VARCHAR2 (20),
   showname          VARCHAR2 (200),
   isNavigation      NUMBER (10) DEFAULT (0) NOT NULL,
   istaskview        NUMBER (10) DEFAULT (0) NOT NULL,
   hotkeystr         VARCHAR2 (10),
   order_id          NUMBER (10) NOT NULL,
   module_level      NUMBER (10),
   tag               VARCHAR2 (64),
   state             NUMBER (10)
)
TABLESPACE WFDATA;

ALTER TABLE mm_moduleinfo ADD CONSTRAINT pk_mm_moduleinfo PRIMARY KEY (sid);



CREATE INDEX idx_mm_mi_url
   ON mm_moduleinfo (entrypoint_url ASC)
   TABLESPACE WFIDX;

ALTER TABLE mm_moduleinfo ADD CONSTRAINT FK_mm_mi_mi FOREIGN KEY (parent_sid) REFERENCES mm_moduleinfo (sid);



CREATE TABLE am_powerinfo
(
   sid           VARCHAR2 (32) NOT NULL,
   powername     VARCHAR2 (100) NOT NULL,
   powerstate    NUMBER (5) DEFAULT (0) NOT NULL,
   parentpower   VARCHAR2 (32),
   resourceid    VARCHAR2 (64) NOT NULL,
   orderid       NUMBER (5) NOT NULL,
   memo          VARCHAR2 (512),
   tag           VARCHAR2 (512)
)
TABLESPACE WFDATA;

ALTER TABLE am_powerinfo ADD CONSTRAINT pk_am_powerinfo PRIMARY KEY (sid);


CREATE INDEX idx_am_pi_pp
   ON am_powerinfo (parentpower ASC)
   TABLESPACE WFIDX;



CREATE INDEX idx_am_pi_ri
   ON am_powerinfo (resourceid ASC)
   TABLESPACE WFIDX;

ALTER TABLE am_powerinfo ADD CONSTRAINT fk_am_pi_pi FOREIGN KEY (parentpower) REFERENCES am_powerinfo (sid);



CREATE TABLE am_powermutex
(
   powersid1   VARCHAR2 (32) NOT NULL,
   powersid2   VARCHAR2 (32) NOT NULL
)
TABLESPACE WFDATA;

ALTER TABLE am_powermutex ADD CONSTRAINT pk_am_powermutex PRIMARY KEY (powersid1, powersid2);
ALTER TABLE am_powermutex ADD CONSTRAINT fk_am_pi_pm2 FOREIGN KEY (powersid2) REFERENCES am_powerinfo (sid);
ALTER TABLE am_powermutex ADD CONSTRAINT fk_am_pi_pm1 FOREIGN KEY (powersid1) REFERENCES am_powerinfo (sid);



CREATE TABLE am_rolegroupinfo
(
   sid              VARCHAR2 (32) NOT NULL,
   rolegroupname    VARCHAR2 (100) NOT NULL,
   rolegroupstate   NUMBER (5) DEFAULT (0) NOT NULL,
   orderid          NUMBER (5) NOT NULL,
   memo             VARCHAR2 (512),
   tag              VARCHAR2 (512)
)
TABLESPACE WFDATA;

ALTER TABLE am_rolegroupinfo ADD CONSTRAINT pk_am_rolegroupinfo PRIMARY KEY (sid);



CREATE TABLE am_rolegroupmutex
(
   rolegroupsid1   VARCHAR2 (32) NOT NULL,
   rolegroupsid2   VARCHAR2 (32) NOT NULL
)
TABLESPACE WFDATA;

ALTER TABLE am_rolegroupmutex ADD CONSTRAINT pk_am_rolegroupmute PRIMARY KEY (rolegroupsid1, rolegroupsid2);
ALTER TABLE am_rolegroupmutex ADD CONSTRAINT fk_am_rgi_rgm1 FOREIGN KEY (rolegroupsid1) REFERENCES am_rolegroupinfo (sid);
ALTER TABLE am_rolegroupmutex ADD CONSTRAINT fk_am_rgi_rgm2 FOREIGN KEY (rolegroupsid2) REFERENCES am_rolegroupinfo (sid);



CREATE TABLE am_roleinfo
(
   sid         VARCHAR2 (32) NOT NULL,
   rolename    VARCHAR2 (100) NOT NULL,
   rolestate   NUMBER (5) DEFAULT (0) NOT NULL,
   orderid     NUMBER (5) NOT NULL,
   memo        VARCHAR2 (512),
   tag         VARCHAR2 (512)
)
TABLESPACE WFDATA;

ALTER TABLE am_roleinfo ADD CONSTRAINT pk_am_roleinfo PRIMARY KEY (sid);



CREATE TABLE am_rolemutex
(
   rolesid1   VARCHAR2 (32) NOT NULL,
   rolesid2   VARCHAR2 (32) NOT NULL
)
TABLESPACE WFDATA;

ALTER TABLE am_rolemutex ADD CONSTRAINT pk_am_rolemutex PRIMARY KEY (rolesid1, rolesid2);
ALTER TABLE am_rolemutex ADD CONSTRAINT fk_am_ri_rm2 FOREIGN KEY (rolesid2) REFERENCES am_roleinfo (sid);
ALTER TABLE am_rolemutex ADD CONSTRAINT fk_am_ri_rm1 FOREIGN KEY (rolesid1) REFERENCES am_roleinfo (sid);



CREATE TABLE am_rolegrouporgtype
(
   orgtypesid     VARCHAR2 (32) NOT NULL,
   rolegroupsid   VARCHAR2 (32) NOT NULL
)
TABLESPACE WFDATA;

ALTER TABLE am_rolegrouporgtype ADD CONSTRAINT pk_am_rolegrouporgt PRIMARY KEY (orgtypesid, rolegroupsid);
ALTER TABLE am_rolegrouporgtype ADD CONSTRAINT fk_am_rgi_ot FOREIGN KEY (rolegroupsid) REFERENCES am_rolegroupinfo (sid);
ALTER TABLE am_rolegrouporgtype ADD CONSTRAINT fk_po_ot_rgi FOREIGN KEY (orgtypesid) REFERENCES po_orgtype (sid);



CREATE TABLE am_rolegrouppeople
(
   peoplesid      VARCHAR2 (32) NOT NULL,
   rolegroupsid   VARCHAR2 (32) NOT NULL
)
TABLESPACE WFDATA;

ALTER TABLE am_rolegrouppeople ADD CONSTRAINT pk_am_rolegrouppeop PRIMARY KEY (peoplesid, rolegroupsid);
ALTER TABLE am_rolegrouppeople ADD CONSTRAINT fk_po_rgi_pi FOREIGN KEY (rolegroupsid) REFERENCES am_rolegroupinfo (sid);
ALTER TABLE am_rolegrouppeople ADD CONSTRAINT fk_po_pi_rgi FOREIGN KEY (peoplesid) REFERENCES po_peopleinfo (sid);



CREATE TABLE am_rolegrouprole
(
   rolegroupsid   VARCHAR2 (32) NOT NULL,
   rolesid        VARCHAR2 (32) NOT NULL
)
TABLESPACE WFDATA;

ALTER TABLE am_rolegrouprole ADD CONSTRAINT pk_am_rolegrouprole PRIMARY KEY (rolesid, rolegroupsid);
ALTER TABLE am_rolegrouprole ADD CONSTRAINT fk_am_rgi_ri FOREIGN KEY (rolegroupsid) REFERENCES am_rolegroupinfo (sid);
ALTER TABLE am_rolegrouprole ADD CONSTRAINT fk_am_ri_rgi FOREIGN KEY (rolesid) REFERENCES am_roleinfo (sid);



CREATE TABLE am_rolepower
(
   rolesid    VARCHAR2 (32) NOT NULL,
   powersid   VARCHAR2 (32) NOT NULL
)
TABLESPACE WFDATA;

ALTER TABLE am_rolepower ADD CONSTRAINT pk_am_rolepower PRIMARY KEY (powersid, rolesid);
ALTER TABLE am_rolepower ADD CONSTRAINT fk_am_pi_ri FOREIGN KEY (powersid) REFERENCES am_powerinfo (sid);
ALTER TABLE am_rolepower ADD CONSTRAINT fk_am_ri_pi FOREIGN KEY (rolesid) REFERENCES am_roleinfo (sid);



CREATE TABLE po_organizeinfo
(
   sid            VARCHAR2 (32) NOT NULL,
   orgname        VARCHAR2 (100) NOT NULL,
   orgno          VARCHAR2 (32) NOT NULL,
   orgtype        VARCHAR2 (32) NOT NULL,
   orgstate       NUMBER (5) DEFAULT (0) NOT NULL,
   parentorg      VARCHAR2 (32),
   managetype     NUMBER (3) DEFAULT (1) NOT NULL,
   includechild   NUMBER (3) DEFAULT (1) NOT NULL,
   memo           VARCHAR2 (512),
   tag            VARCHAR2 (512),
   orgLevel       NUMBER (10),
   orgPath        VARCHAR2 (128)
)
TABLESPACE WFDATA;

ALTER TABLE po_organizeinfo ADD CONSTRAINT pk_po_organizeinfo PRIMARY KEY (sid);



CREATE INDEX idx_po_oi_ot
   ON po_organizeinfo (orgtype ASC)
   TABLESPACE WFIDX;



CREATE INDEX idx_po_oi_po
   ON po_organizeinfo (parentorg ASC)
   TABLESPACE WFIDX;

ALTER TABLE po_organizeinfo ADD CONSTRAINT fk_po_ot_oi FOREIGN KEY (orgtype) REFERENCES po_orgtype (sid);



CREATE TABLE po_orgtyperelate
(
   parentorgtype   VARCHAR2 (32) NOT NULL,
   childorgtype    VARCHAR2 (32) NOT NULL,
   countlimit      NUMBER (10) DEFAULT ( (-1)) NOT NULL
)
TABLESPACE WFDATA;

ALTER TABLE po_orgtyperelate ADD CONSTRAINT pk_po_orgtyperelate PRIMARY KEY (parentorgtype, childorgtype);
ALTER TABLE po_orgtyperelate ADD CONSTRAINT fk_po_ot_otrp FOREIGN KEY (parentorgtype) REFERENCES po_orgtype (sid);
ALTER TABLE po_orgtyperelate ADD CONSTRAINT fk_po_ot_otrc FOREIGN KEY (childorgtype) REFERENCES po_orgtype (sid);