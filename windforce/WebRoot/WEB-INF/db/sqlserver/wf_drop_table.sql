IF EXISTS
      (SELECT object_name (object_id) tableName, name, type_desc
         FROM sys.indexes
        WHERE name = 'idx_po_peoplecode')
DROP INDEX po_peopleinfo.idx_po_peoplecode;

IF EXISTS
      (SELECT object_name (object_id) tableName, name, type_desc
         FROM sys.indexes
        WHERE name = 'idx_po_cl_ps')
DROP INDEX po_currentlogin.idx_po_cl_ps;

IF EXISTS
      (SELECT object_name (object_id) tableName, name, type_desc
         FROM sys.indexes
        WHERE name = 'idx_po_cl_si')
DROP INDEX po_currentlogin.idx_po_cl_si;

IF EXISTS
      (SELECT object_name (object_id) tableName, name, type_desc
         FROM sys.indexes
        WHERE name = 'idx_mm_mi_url')
DROP INDEX mm_moduleinfo.idx_mm_mi_url;

IF EXISTS
      (SELECT object_name (object_id) tableName, name, type_desc
         FROM sys.indexes
        WHERE name = 'idx_am_pi_pp')
DROP INDEX am_powerinfo.idx_am_pi_pp;

IF EXISTS
      (SELECT object_name (object_id) tableName, name, type_desc
         FROM sys.indexes
        WHERE name = 'idx_am_pi_ri')
DROP INDEX am_powerinfo.idx_am_pi_ri;

IF EXISTS
      (SELECT object_name (object_id) tableName, name, type_desc
         FROM sys.indexes
        WHERE name = 'idx_po_oi_ot')
DROP INDEX po_organizeinfo.idx_po_oi_ot;

IF EXISTS
      (SELECT object_name (object_id) tableName, name, type_desc
         FROM sys.indexes
        WHERE name = 'idx_po_oi_po')
DROP INDEX po_organizeinfo.idx_po_oi_po;

IF object_id ('po_orgtype', 'u') IS NOT NULL
   BEGIN
      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_po_ot_rgi')
  ALTER TABLE am_rolegrouporgtype  DROP CONSTRAINT fk_po_ot_rgi;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_po_ot_oi')
  ALTER TABLE po_organizeinfo  DROP CONSTRAINT fk_po_ot_oi;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_po_ot_otrp')
  ALTER TABLE po_orgtyperelate  DROP CONSTRAINT fk_po_ot_otrp;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_po_ot_otrc')
  ALTER TABLE po_orgtyperelate  DROP CONSTRAINT fk_po_ot_otrc;

   DROP TABLE po_orgtype;
   END

IF object_id ('po_peopleinfo', 'u') IS NOT NULL
   BEGIN
      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_po_pi_rgi')
    ALTER TABLE am_rolegrouppeople  DROP CONSTRAINT fk_po_pi_rgi;

  DROP TABLE po_peopleinfo;
   END

IF object_id ('poam_operateLogInfo', 'u') IS NOT NULL
   BEGIN
  DROP TABLE poam_operateLogInfo;
   END

IF object_id ('po_currentlogin', 'u') IS NOT NULL
   BEGIN
  DROP TABLE po_currentlogin;
   END

IF object_id ('Param_ModuleFlow', 'u') IS NOT NULL
   BEGIN
  DROP TABLE Param_ModuleFlow;
   END

IF object_id ('mm_moduleinfo', 'u') IS NOT NULL
   BEGIN
      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'FK_mm_mi_mi')
ALTER TABLE mm_moduleinfo  DROP CONSTRAINT FK_mm_mi_mi;

  DROP TABLE mm_moduleinfo;
   END

IF object_id ('am_powerinfo', 'u') IS NOT NULL
   BEGIN
      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_pi_ri')
ALTER TABLE am_rolepower  DROP CONSTRAINT fk_am_pi_ri;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_pi_pi')
ALTER TABLE am_powerinfo  DROP CONSTRAINT fk_am_pi_pi;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_pi_pm2')
ALTER TABLE am_powermutex  DROP CONSTRAINT fk_am_pi_pm2;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_pi_pm1')
ALTER TABLE am_powermutex  DROP CONSTRAINT fk_am_pi_pm1;

  DROP TABLE am_powerinfo;
   END

IF object_id ('am_powermutex', 'u') IS NOT NULL
   BEGIN
  DROP TABLE am_powermutex;
   END

IF object_id ('am_rolegroupinfo', 'u') IS NOT NULL
   BEGIN
      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_rgi_ot')
ALTER TABLE am_rolegrouporgtype  DROP CONSTRAINT fk_am_rgi_ot;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_po_rgi_pi')
ALTER TABLE am_rolegrouppeople  DROP CONSTRAINT fk_po_rgi_pi;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_rgi_ri')
ALTER TABLE am_rolegrouprole  DROP CONSTRAINT fk_am_rgi_ri;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_rgi_rgm1')
ALTER TABLE am_rolegroupmutex  DROP CONSTRAINT fk_am_rgi_rgm1;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_rgi_rgm2')
ALTER TABLE am_rolegroupmutex  DROP CONSTRAINT fk_am_rgi_rgm2;

  DROP TABLE am_rolegroupinfo;
   END


IF object_id ('am_rolegroupmutex', 'u') IS NOT NULL
   BEGIN
  DROP TABLE am_rolegroupmutex;
   END

IF object_id ('am_roleinfo', 'u') IS NOT NULL
   BEGIN
      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_ri_rm2')
ALTER TABLE am_rolemutex  DROP CONSTRAINT fk_am_ri_rm2;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_ri_rm1')
ALTER TABLE am_rolemutex  DROP CONSTRAINT fk_am_ri_rm1;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_ri_rgi')
ALTER TABLE am_rolegrouprole  DROP CONSTRAINT fk_am_ri_rgi;

      IF EXISTS
            (SELECT *
               FROM sysobjects
              WHERE name = 'fk_am_ri_pi')
ALTER TABLE am_rolepower  DROP CONSTRAINT fk_am_ri_pi;

  DROP TABLE am_roleinfo;
   END

IF object_id ('am_rolemutex', 'u') IS NOT NULL
   BEGIN
  DROP TABLE am_rolemutex;
   END

IF object_id ('am_rolegrouporgtype', 'u') IS NOT NULL
   BEGIN
  DROP TABLE am_rolegrouporgtype;
   END

IF object_id ('am_rolegrouppeople', 'u') IS NOT NULL
   BEGIN
  DROP TABLE am_rolegrouppeople;
   END

IF object_id ('am_rolegrouprole', 'u') IS NOT NULL
   BEGIN
  DROP TABLE am_rolegrouprole;
   END

IF object_id ('am_rolepower', 'u') IS NOT NULL
   BEGIN
  DROP TABLE am_rolepower;
   END

IF object_id ('po_organizeinfo', 'u') IS NOT NULL
   BEGIN
  DROP TABLE po_organizeinfo;
   END

IF object_id ('po_orgtyperelate', 'u') IS NOT NULL
   BEGIN
  DROP TABLE po_orgtyperelate;
   END