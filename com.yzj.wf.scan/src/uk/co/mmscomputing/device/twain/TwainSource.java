package uk.co.mmscomputing.device.twain;

import javax.swing.JOptionPane;

public class TwainSource extends TwainIdentity implements TwainConstants{

  private boolean busy;
  private int     state;
  private int     hWnd;

  private int     showUI     = 1;               // show DS GUI
  private int     modalUI    = 0;               // show modeless GUI

  private int     iff        = TWFF_BMP;        // image file format

  TwainSource(TwainSourceManager manager,int hwnd,boolean bus){
    super(manager);
    this.hWnd=hwnd;
    this.busy=bus;
    this.state=3;
  }

  byte[] getIdentity(){return identity;}
  int    getIFF(){return iff;}

  public boolean isBusy(){             return busy;}
         void    setBusy(boolean b){   busy=b; jtwain.signalStateChange(this);}
  public int     getState(){           return state;}
         void    setState(int s){      state=s;jtwain.signalStateChange(this);}

  void checkState(int state)throws TwainIOException{
    if(this.state==state){return;}
    throw new TwainIOException(getClass().getName()+".checkState:\n\tSource not in state "+state+" but in state "+this.state+".");
  }

  int getConditionCode()throws TwainIOException{
    byte[] status=new byte[4];                     // TW_STATUS
    int    rc    =jtwain.callSource(identity,DG_CONTROL,DAT_STATUS,MSG_GET,status);
    if(rc!=TWRC_SUCCESS){
      throw new TwainResultException("Cannot retrieve twain source's status.",rc);
    }
    return jtwain.getINT16(status,0);
  }

  public void call(int dg,int dat,int msg,byte[] data)throws TwainIOException{
    int rc=jtwain.callSource(identity,dg,dat,msg,data);
    switch(rc){
    case TWRC_SUCCESS:          return;
    case TWRC_FAILURE:          throw new TwainFailureException(getConditionCode());
    case TWRC_CHECKSTATUS:      throw new TwainResultException.CheckStatus();
    case TWRC_CANCEL:           throw new TwainResultException.Cancel();
    case TWRC_DSEVENT:          return;
    case TWRC_NOTDSEVENT:       throw new TwainResultException.NotDSEvent();
    case TWRC_XFERDONE:         throw new TwainResultException.TransferDone();
//    case TWRC_ENDOFLIST:        throw new TwainResultException.EndOfList();
//    case TWRC_INFONOTSUPPORTED: throw new TwainResultException.InfoNotSupported();
//    case TWRC_DATANOTAVAILABLE: throw new TwainResultException.DataNotAvailable();
    default:                    throw new TwainResultException("Failed to call source.",rc);
    }
  }

  public TwainCapability[] getCapabilities()throws TwainIOException{
    return TwainCapability.getCapabilities(this);
  }

  public TwainCapability getCapability(int cap)throws TwainIOException{     // use only in state 4
    return new TwainCapability(this,cap);
  }

  public void setShowUI(boolean enable){showUI=(enable)?1:0;}
  public void setModalUI(boolean enable){modalUI=(enable)?1:0;}

  void enable()throws TwainIOException{                         // state 4 -> 5
    System.out.println("state 4 start ");
	  checkState(4);
	  System.out.println("state 4 end ");
	  jtwain.negotiateCapabilities(this);                         // still in state 4 tell application to 
                                                                // negotiate capabilities and defaults               
    int xfer=new TwainCapability.XferMech(this).intValue();     // what transfer mode do we use
    if(xfer==TWSX_NATIVE){                                      // if native transfer (dib)
    }else if(xfer==TWSX_FILE){                                  // if file transfer mode
      iff=new TwainCapability.ImageFileFormat(this).intValue(); // cache file format
    }
    byte[] gui=new byte[8];                                     // TW_USERINTERFACE
    jtwain.setINT16(gui,0,showUI);                              // 1: show gui; 0: do not show gui
    jtwain.setINT16(gui,2,modalUI);
    jtwain.setINT32(gui,4,hWnd);                                // set parent window
    try{
      call(DG_CONTROL,DAT_USERINTERFACE,MSG_ENABLEDS,gui);      // enable source; pop up gui if ShowGUI=true
      setState(5);
    }catch(TwainResultException.CheckStatus trecs){             // ShowGUI=false not supported
      setState(5);                                              // continue with GUI
                                    // to do: check status; don't have a source that does this.
    }catch(TwainResultException.Cancel trec){
      close();        
    }
  }

  private void transfer(TwainTransfer tt)throws TwainIOException{           // 5 -> 6
    byte[] pendingXfers=new byte[6];                                        // TW_PENDINGXFERS
    int    rc;

    long time = System.currentTimeMillis();//20090303天津银行扫描仪测试
    do{
      setState(6);
      jtwain.setINT16(pendingXfers,0,0);                                    // pendingXfer.Count = 0;  
      try{
        tt.initiate();
      }catch(TwainResultException.TransferDone tretd){                      // state 7: memory allocated
        setState(7);
        tt.finish();
      }catch(TwainResultException.Cancel trec){                             // state 7:
        tt.cancel();
        try{
          call(DG_CONTROL,DAT_PENDINGXFERS,MSG_ENDXFER,pendingXfers);       // tell source we are done with image
          if(jtwain.getINT16(pendingXfers,0) > 0){
            call(DG_CONTROL,DAT_PENDINGXFERS,MSG_RESET,pendingXfers);       // tell source to cancel pending images
          }
          setState(5);
        }catch(TwainIOException tioe){
          jtwain.signalException(getClass().getName()+".transfer:\n\t"+tioe);
        }
      }catch(TwainFailureException tfe){                                    // state 6: no memory allocated
        jtwain.signalException(getClass().getName()+".transfer:\n\t"+tfe);
      }finally{
        tt.cleanup();
        if(state>5){
          call(DG_CONTROL,DAT_PENDINGXFERS,MSG_ENDXFER,pendingXfers);       // tell source we are done with image
          if(jtwain.getINT16(pendingXfers,0)==0){
            setState(5);
          }
        }
      }
    }while(jtwain.getINT16(pendingXfers,0)!=0);                             // ADF scanner: pendingXfers = -1 if not known
    if(showUI==0){                                                          // User cannot close source
      disable();                                                            // hence close here
      close();
    }
  //20090303天津银行扫描仪测试
    time = System.currentTimeMillis() - time;
    System.out.println("3\b扫描总时间 : " + time+" ms");
    /*JOptionPane.showMessageDialog(null,
    		"扫描总时间 ："+time+" ms", "Exception",
    		JOptionPane.INFORMATION_MESSAGE);*/
  }

  void transferImage()throws TwainIOException{
    switch(getXferMech()){
    case TWSX_NATIVE:   transfer(new TwainTransfer.NativeTransfer(this));  break;
    case TWSX_FILE:   transfer(new TwainTransfer.NativeTransfer(this));  break;
    case TWSX_MEMORY:   transfer(new TwainTransfer.NativeTransfer(this));  break;
//    case TWSX_FILE:     transfer(new TwainTransfer.FileTransfer(this));    break;
//    case TWSX_MEMORY:   transfer(new TwainTransfer.MemoryTransfer(this));  break;
    default:                                            // shouldn't be here
      System.out.println(getClass().getName()+".transferImage:\n\tDo not support this transfer mode: "+getXferMech()); 
      System.err.println(getClass().getName()+".transferImage:\n\tDo not support this transfer mode: "+getXferMech()); 
      break;
    }
  }

  void disable()throws TwainIOException{                    //  state 5 -> 4
    if(state<5){return;}

    byte[] gui=new byte[8];                                 // TW_USERINTERFACE
    jtwain.setINT16(gui,0,-1);                              // TWON_DONTCARE8
    jtwain.setINT16(gui,2,0);
    jtwain.setINT32(gui,4,hWnd);                            // set parent window

    call(DG_CONTROL,DAT_USERINTERFACE,MSG_DISABLEDS,gui);
    setState(4);
  }

  void close()throws TwainIOException{                      //  state 4 -> 3
    if(state!=4){return;}
    super.close();
    busy=false;
    setState(3);
  }

  int handleGetMessage(int msgPtr)throws TwainIOException{  // callback functions cpp -> java; windows thread;
    if(state<5){return TWRC_NOTDSEVENT;}
    try{
      byte[] event=new byte[6];                             // TW_EVENT
      jtwain.setINT32(event,0,msgPtr);                      // twEvent.pEvent=(TW_MEMREF)&msg;
      jtwain.setINT16(event,4,0);                           // twEvent.TWMessage=MSG_NULL;

      call(DG_CONTROL,DAT_EVENT,MSG_PROCESSEVENT,event);    // [1] 7 - 162

      int message=jtwain.getINT16(event,4);                 // if event was handled by source
      switch (message){                                     // any messages from source
      case MSG_XFERREADY:                                   // state 5 -> 6                   
        transferImage();                                    // source has an image for us
        break;
      case MSG_CLOSEDSOK:                                   // Do not use DG_CONTROL/DAT_USERINTERFACE/MSG_ENABLEDSUIONLY, 
                                                            // hence shouldn't get this event
      case MSG_CLOSEDSREQ:                                  // source wants us to close it
        disable();
        close();
        break;
      case MSG_DEVICEEVENT:                                 // Do not allow this event, hence don't get this event
      case MSG_NULL:                                        // Event fully processed by source
      default:
        break;
      }
      return TWRC_DSEVENT;
    }catch(TwainResultException.NotDSEvent trendse){        // if event was not handled by source
      return TWRC_NOTDSEVENT;
    }
  }

  public int getXferMech()throws TwainIOException{
    return new TwainCapability.XferMech(this).intValue();
  }

  public void setXferMech(int mech){
    try{
      switch(mech){
      case TWSX_NATIVE:
      case TWSX_FILE:
      case TWSX_MEMORY:                     break;
      default:          mech=TWSX_NATIVE;   break;
      }
      TwainCapability tc;
      tc=getCapability(ICAP_XFERMECH);                      // TW_ENUMERATION/TW_UINT16
      tc.setCurrentValue(mech);
    }catch(TwainIOException e){                             // Shouldn't happen must be supported by all sources.
      jtwain.signalException(getClass().getName()+".setXferMech:\n\t"+e);
    }
  }

  public void setImageFileFormat(int iff){
    try{
      TwainCapability tc;
      switch(iff){
      case TWFF_TIFF:      case TWFF_BMP:     case TWFF_JFIF:
      case TWFF_TIFFMULTI: case TWFF_PNG:
        break;
      default:
        iff=TWFF_BMP;                                       // (must be supported by all windows sources)
        break;
      }
      tc=getCapability(ICAP_IMAGEFILEFORMAT);               // TW_ENUMERATION/TW_UINT16
      tc.setCurrentValue(iff);
    }catch(Exception e){
      jtwain.signalException(getClass().getName()+".setImageFileFormat:\n\t"+e);
    }
  }

  public boolean isUIControllable(){
    try{                                                    // TW_ONEVALUE/TW_BOOL if >=1.6 TWAIN compliant
      return getCapability(CAP_UICONTROLLABLE).booleanValue(); 
    }catch(Exception e){                                    // if some error assume source does not support ShowUI=false
      jtwain.signalException(getClass().getName()+".isUIControllable:\n\t"+e);return false;                                         
    }
  }

  public boolean isDeviceOnline(){                          // [1] 9-369 CAP_DEVICEONLINE
    try{                                                    // TW_ONEVALUE/TW_BOOL
      return getCapability(CAP_DEVICEONLINE).booleanValue();
    }catch(Exception e){                                    // if some error assume source is on
      jtwain.signalException(getClass().getName()+".isOnline:\n\t"+e);return true;                                          
    }
  }

//  public TwainFileSystem getFileSystem(){return new TwainFileSystem(this);}
}


