package uk.co.mmscomputing.device.twain;

import javax.swing.JOptionPane;

import com.yzj.wf.scan.gui.batchtree.BatchTree;

public class TwainSourceManager implements TwainConstants{

  private TwainSource   source;

  TwainSourceManager(int hwnd){
    source=new TwainSource(this,hwnd,false);
    source.getDefault();
  }

  int getConditionCode()throws TwainIOException{           // [1] 7-219
    byte[] status=new byte[4];                             // TW_STATUS
    int    rc    =jtwain.callSourceManager(DG_CONTROL,DAT_STATUS,MSG_GET,status);
    if(rc!=TWRC_SUCCESS){
      throw new TwainResultException("Cannot retrieve twain source manager's status.",rc);
    }
    return jtwain.getINT16(status,0);
  }

  public void call(int dg,int dat,int msg,byte[] data)throws TwainIOException{
//	  System.out.println("call1 time = "+new Date());
    int rc=jtwain.callSourceManager(dg,dat,msg,data);
//    System.out.println("call2 time = "+new Date());
    switch(rc){
    case TWRC_SUCCESS:      return;
    case TWRC_FAILURE:      throw new TwainFailureException(getConditionCode());
    case TWRC_CANCEL:       throw new TwainResultException.Cancel();
    case TWRC_CHECKSTATUS:  throw new TwainResultException.CheckStatus();
    case TWRC_XFERDONE:     throw new TwainResultException.TransferDone();
    case TWRC_ENDOFLIST:    throw new TwainResultException.EndOfList();
    default:                throw new TwainResultException("Failed to call source.",rc);
    }
  }

  TwainSource getSource(){return source;}

  public TwainSource selectSource(TwainIdentity identity)throws TwainIOException{       
    source.get(identity);return source;
  }

  TwainSource selectSource()throws TwainIOException{       
    source.checkState(3);
    source.setBusy(true);                                  // tell TwainPanel to disable GUI
    try{
      source.userSelect();                                 // new source in state 3
      return source;
    }catch(TwainResultException.Cancel trec){
      return source;
//  }catch(ThreadDeath e){
//    Applet: Select dialog enabled while user reloads webpage. 
//    Happens only first time.
    }finally{
      source.setBusy(false);                               // tell TwainPanel to enable GUI
    }
  }

  TwainSource openSource()throws TwainIOException{ 
//	  System.out.println("open1 time = "+new Date());
    source.checkState(3);                                  // old source not enabled
//    System.out.println("open2 time = "+new Date());
    source.setBusy(true);                                  // tell TwainPanel to disable GUI
//    System.out.println("open3 time = "+new Date());
    try{
      source.open();
//      System.out.println("open4 time = "+new Date());
      source.setState(4);
      if(!source.isDeviceOnline()){
    	  String notice="无法调用扫描仪,请检查扫描仪驱动是否已正常安装.";
    		JOptionPane.showMessageDialog(BatchTree.getInstance().getView(), notice, "提示信息",
					JOptionPane.INFORMATION_MESSAGE);
        throw new TwainIOException("Selected twain source is not online.");
      }
      return source;
    }catch(TwainResultException.Cancel trec){
      source.setBusy(false);                               // tell TwainPanel to enable GUI
      return source;
    }catch(TwainIOException tioe){  
      source.setBusy(false);                               // tell TwainPanel to enable GUI
      throw tioe;
    }
  }
}
