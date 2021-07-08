DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM All_Sequences
    WHERE UPPER (sequence_name) = 'PO_CL_LI';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'DROP SEQUENCE PO_CL_LI';
   END IF;
END;
/

DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM All_Sequences
    WHERE UPPER (sequence_name) = 'PARAMGROUP_ID';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'DROP SEQUENCE PARAMGROUP_ID';
   END IF;
END;
/

DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM all_indexes
    WHERE UPPER (index_name) = 'IDX_PO_PEOPLECODE';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop index idx_po_peoplecode';
   END IF;
END;
/

DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM all_indexes
    WHERE UPPER (index_name) = 'IDX_PO_CL_PS';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop index idx_po_cl_ps';
   END IF;
END;
/

DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM all_indexes
    WHERE UPPER (index_name) = 'IDX_PO_CL_SI';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop index idx_po_cl_si';
   END IF;
END;
/

DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM all_indexes
    WHERE UPPER (index_name) = 'IDX_MM_MI_URL';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop index idx_mm_mi_url';
   END IF;
END;
/

DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM all_indexes
    WHERE UPPER (index_name) = 'IDX_AM_PI_PP';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop index idx_am_pi_pp';
   END IF;
END;
/

DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM all_indexes
    WHERE UPPER (index_name) = 'IDX_AM_PI_RI';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop index idx_am_pi_ri';
   END IF;
END;
/

DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM all_indexes
    WHERE UPPER (index_name) = 'IDX_PO_OI_OT';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop index idx_po_oi_ot';
   END IF;
END;
/

DECLARE
   cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO cnt
     FROM all_indexes
    WHERE UPPER (index_name) = 'idx_po_oi_po';

   IF cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop index idx_po_oi_po';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'PO_ORGTYPE';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table PO_ORGTYPE cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'PO_PEOPLEINFO';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table PO_PEOPLEINFO cascade constraints';
   END IF;
END;
/


DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'poam_operateLogInfo';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table poam_operateLogInfo cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'PO_CURRENTLOGIN';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table po_currentlogin cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'PARAM_MODULEFLOW';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table Param_ModuleFlow cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'MM_MODULEINFO';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table mm_moduleinfo cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_POWERINFO';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_powerinfo cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_POWERMUTEX';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_powermutex cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_ROLEGROUPINFO';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_rolegroupinfo cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_ROLEGROUPMUTEX';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_rolegroupmutex cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_ROLEINFO';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_roleinfo cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_ROLEMUTEX';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_rolemutex cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_ROLEGROUPORGTYPE';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_rolegrouporgtype cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_ROLEGROUPPEOPLE';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_rolegrouppeople cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_ROLEGROUPROLE';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_rolegrouprole cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'AM_ROLEPOWER';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table am_rolepower cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'PO_ORGANIZEINFO';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table po_organizeinfo cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'PO_ORGTYPERELATE';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table po_orgtyperelate cascade constraints';
   END IF;
END;
/

DECLARE
   v_cnt   NUMBER;
BEGIN
   SELECT COUNT (*)
     INTO v_cnt
     FROM user_tables
    WHERE UPPER (table_name) = 'POAM_OPERATELOGINFO';

   IF v_cnt > 0
   THEN
      EXECUTE IMMEDIATE 'drop table poam_operateloginfo cascade constraints';
   END IF;
END;
/