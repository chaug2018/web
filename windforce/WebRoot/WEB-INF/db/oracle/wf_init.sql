DELETE FROM am_rolegrouprole;
DELETE FROM am_rolegrouppeople;
DELETE FROM am_rolepower;
DELETE FROM am_powerinfo;
DELETE FROM po_peopleinfo;
DELETE FROM po_organizeinfo;
DELETE FROM po_orgtyperelate;
DELETE FROM po_orgtype;
DELETE FROM mm_moduleinfo;
DELETE FROM am_roleinfo;
DELETE FROM am_rolegroupinfo; 

INSERT INTO po_orgtype (sid,orgtypename, orgtypestate, memo, tag) VALUES ('49E4BD36442189FAA2EF4CC1EF2F_004', '网点', 0, '网点', '');
INSERT INTO po_orgtype (sid,orgtypename,orgtypestate,memo,tag) VALUES ('65A2D56EDB3E4C14AF8858AF2C80188C','总行',0,'总行','');
INSERT INTO po_orgtype (sid,orgtypename,orgtypestate,memo,tag) VALUES ('D51E99BE443892600D643F09F63F_001','省级分行',0,'省级分行','');
INSERT INTO po_orgtype (sid,orgtypename,orgtypestate,memo,tag) VALUES ('D5AFC14640D3A4A0496D7E46D0E7_003','营业部',0,'营业部','');
INSERT INTO po_orgtype (sid,orgtypename,orgtypestate,memo,tag) VALUES ('DB009F5340209EF5DDEE83AEC206_002','支行',0,'支行','');

INSERT INTO po_orgtyperelate (parentorgtype, childorgtype, countlimit) VALUES ('65A2D56EDB3E4C14AF8858AF2C80188C','D51E99BE443892600D643F09F63F_001',-1);
INSERT INTO po_orgtyperelate (parentorgtype, childorgtype, countlimit) VALUES ('D51E99BE443892600D643F09F63F_001','D5AFC14640D3A4A0496D7E46D0E7_003',1);
INSERT INTO po_orgtyperelate (parentorgtype, childorgtype, countlimit) VALUES ('D51E99BE443892600D643F09F63F_001','DB009F5340209EF5DDEE83AEC206_002',-1);
INSERT INTO po_orgtyperelate (parentorgtype, childorgtype, countlimit) VALUES ('DB009F5340209EF5DDEE83AEC206_002','49E4BD36442189FAA2EF4CC1EF2F_004', -1);

INSERT INTO po_organizeinfo (sid,orgname,orgno,orgtype,orgstate,parentorg,managetype,includechild,memo,tag,orgLevel,orgPath) 
VALUES ('00000000000000000000000000000000','总行', 'root','65A2D56EDB3E4C14AF8858AF2C80188C',0,null,0, 1,'总行','', 0,'|root|');

INSERT INTO po_peopleinfo (sid,peoplecode,peoplename,peoplegender,pwd,peoplestate,organizeinfo,defaultdesktop,memo,tag,defaultconfig,lastchangepwdtime,passwordErrCount,
recentPasswordRecord) 
VALUES ('7B92AE0FC4B04DB48F1AFBDB22CD7188','super','超级管理员',0,'62b7cfe4e47c0d6198eed1e1ddd7ae40',0,'00000000000000000000000000000000',0, '超级管理员',' ',' ',current_date,0,' ');

INSERT INTO mm_moduleinfo (sid, parent_sid,name,entrypoint_url,entrypoint_type,showname,hotkeystr,order_id,module_level,tag,state,istaskview,isNavigation)
VALUES ('00000000000000000000000000000000',NULL,'银之杰公共平台','http://[server]:[port]/windforce/','url','银之杰公共平台','',1,0,'',1,0,0);
INSERT INTO mm_moduleinfo (sid, parent_sid,name,entrypoint_url,entrypoint_type,showname,hotkeystr,order_id,module_level,tag,state,istaskview,isNavigation)
VALUES ('12AEA1D005B04803B89664DAB3764EBE','00000000000000000000000000000000','机构人员管理','http://[server]:[port]/windforce/windforce/po/index.jsp','url','机构人员管理','',48,1,NULL,1,0,1);
INSERT INTO mm_moduleinfo (sid, parent_sid,name,entrypoint_url,entrypoint_type,showname,hotkeystr,order_id,module_level,tag,state,istaskview,isNavigation)
VALUES ('supermodule1','00000000000000000000000000000000','模块管理','http://[server]:[port]/windforce/windforce/mm/moduleManager.jsp','url','模块管理','',60,1,'',1,0,1);
INSERT INTO mm_moduleinfo (sid, parent_sid,name,entrypoint_url,entrypoint_type,showname,hotkeystr,order_id,module_level,tag,state,istaskview,isNavigation)
VALUES ('supermodule2','00000000000000000000000000000000','权限管理','http://[server]:[port]/windforce/windforce/am/','url','权限管理','',11,1,'',1,0,1);
INSERT INTO mm_moduleinfo (sid, parent_sid,name,entrypoint_url,entrypoint_type,showname,hotkeystr,order_id,module_level,tag,state,istaskview,isNavigation)
VALUES ('supermodule3','supermodule2','角色管理','http://[server]:[port]/windforce/roles_initRole.action','url','角色管理','',57,2,'',1,0,1);
INSERT INTO mm_moduleinfo (sid, parent_sid,name,entrypoint_url,entrypoint_type,showname,hotkeystr,order_id,module_level,tag,state,istaskview,isNavigation)
VALUES ('supermodule4','supermodule2','岗位管理','http://[server]:[port]/windforce/rolegroup_initRoleGroup.action','url','岗位管理','',62,2,'',1,0,1);

INSERT INTO am_powerinfo (sid,powername,powerstate,parentpower,resourceid,orderid,memo,tag) 
VALUES ('00000000000000000000000000000000','银之杰公共平台',0,NULL,'00000000000000000000000000000000',0,'银之杰公共平台','');
INSERT INTO am_powerinfo (sid,powername,powerstate,parentpower,resourceid,orderid,memo,tag)
VALUES ('78BE0700DB754EA8B731744CE4F16A8C','机构人员管理',0,'00000000000000000000000000000000','12AEA1D005B04803B89664DAB3764EBE',4,'机构人员管理','');
INSERT INTO am_powerinfo (sid,powername,powerstate,parentpower,resourceid,orderid,memo,tag)
VALUES ('superpower1','模块管理',0,'00000000000000000000000000000000','supermodule1',9,'模块管理','');
INSERT INTO am_powerinfo (sid,powername,powerstate,parentpower,resourceid,orderid,memo,tag)
VALUES ('superpower2','权限管理',0,'00000000000000000000000000000000','supermodule2',10,'权限管理','');
INSERT INTO am_powerinfo (sid,powername,powerstate,parentpower,resourceid,orderid,memo,tag)
VALUES ('superpower3','角色管理',0,'superpower2','supermodule3',0,'角色管理','');
INSERT INTO am_powerinfo (sid,powername,powerstate,parentpower,resourceid,orderid,memo,tag)
VALUES ('superpower4','岗位管理',0,'superpower2','supermodule4',1,'岗位管理','');

INSERT INTO am_roleinfo (sid,rolename,rolestate,orderid,memo,tag)
VALUES ('superrole','超级管理员',0,'0','内置账户[不能删除]','');

INSERT INTO am_rolegroupinfo (sid,rolegroupname,rolegroupstate,orderid,memo,tag)
VALUES ('supergroup','超级管理员组',0,'5','超级管理员组','');

INSERT INTO am_rolegrouprole (rolegroupsid, rolesid) VALUES ('supergroup', 'superrole');

INSERT INTO am_rolegrouppeople (peoplesid, rolegroupsid) VALUES ('7B92AE0FC4B04DB48F1AFBDB22CD7188', 'supergroup');

INSERT INTO am_rolepower (rolesid, powersid) VALUES ('superrole', '00000000000000000000000000000000');
INSERT INTO am_rolepower (rolesid, powersid) VALUES ('superrole', '78BE0700DB754EA8B731744CE4F16A8C');
INSERT INTO am_rolepower (rolesid, powersid) VALUES ('superrole', 'superpower1');
INSERT INTO am_rolepower (rolesid, powersid) VALUES ('superrole', 'superpower2');
INSERT INTO am_rolepower (rolesid, powersid) VALUES ('superrole', 'superpower3');
INSERT INTO am_rolepower (rolesid, powersid) VALUES ('superrole', 'superpower4');