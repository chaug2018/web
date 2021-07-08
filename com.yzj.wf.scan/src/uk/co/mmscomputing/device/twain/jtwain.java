package uk.co.mmscomputing.device.twain;

import java.io.*;
import java.awt.image.*;
import java.lang.ref.*;
import java.lang.reflect.*;
import java.util.Date;

import uk.co.mmscomputing.util.*;

public class jtwain implements TwainConstants{

  static private boolean            installed;
  static private Thread             nativeThread;                         // make sure to call DSM_ENTRY only from this native thread!

  static private TwainSourceManager sourceManager;                        // applet: this object will live as long as browser window is open
  static private WeakReference      scanner;                              // applet: set to new object if applet reloads

  static private native int         ninitLib();                           // initialize native library
  static private native void        nstart();                             // run native event loop

// java -> native
  static private native void   ntrigger(Object caller,int cmd);           // call cbexecute from native thread 

// TwainSourceManager -> native
  static private native int    ncallSourceManager(int dg,int dat,int msg,byte[] data)throws TwainException;

// TwainSource -> native
  static         native byte[] ngetContainer(int type, int handle);       // copy container pointed to by 'handle' into byte buffer
  static         native int    nsetContainer(int type, byte[] container); // copy byte buffer into native memory buffer return handle
  static         native void   nfree(int handle);                         // free native memory
  static private native int    ncallSource(byte[] source,int dg,int dat,int msg,byte[] data)throws TwainException;

// for twain native transfer mode
  static private native Object ntransferImage(int imagePtr);              // returns BufferedImage object

// for twain memory transfer mode
  static         native void   nnew(byte[] twImageMemXfer,int size);      // allocate native memory block
  static         native byte[] ncopy(byte[] twImageMemXfer,int size);     // copy native memory block into java byte array
  static         native void   ndelete(byte[] twImageMemXfer);            // free native memory block

  static public boolean isInstalled(){return installed;}  

  static int  getINT16(byte[] buf,int off){return (buf[off++]&0x00FF)|(buf[off]<<8);}
  static void setINT16(byte[] buf,int off,int i){buf[off++]=(byte)i;buf[off++]=(byte)(i>>8);}
  static int  getINT32(byte[] buf,int off){return (buf[off++]&0x00FF)|((buf[off++]&0x00FF)<<8)|((buf[off++]&0x00FF)<<16)|(buf[off]<<24);}
  static void setINT32(byte[] buf,int off,int i){buf[off++]=(byte)i;buf[off++]=(byte)(i>>8);buf[off++]=(byte)(i>>16);buf[off++]=(byte)(i>>24);}

  static double getFIX32(byte[] buf,int off){
    int whole =((buf[off++]&0x00FF)|(buf[off++]<<8));
    int frac  =((buf[off++]&0x00FF)|((buf[off]&0x00FF)<<8));
    return ((double)whole)+((double)frac)/65536.0;            // [1] 4-76
  }

  static void setFIX32(byte[] buf,int off,double d){
    int value =(int)(d*65536.0+((d<0)?(-0.5):0.5));           // [1] 4-78 + fix for negative numbers
    setINT16(buf,off  ,value>>16);                            // whole
    setINT16(buf,off+2,value&0x0000FFFF);                     // frac
  }

  static void setString(byte[] buf,int off,String s){
    System.arraycopy(s.getBytes(),0,buf,0,s.length());
  }

  static int callSourceManager(int dg,int dat,int msg,byte[] data)throws TwainIOException{
//	  System.out.println("callSourceManager1 time = "+new Date());
    if(Thread.currentThread()!=nativeThread){
      throw new TwainIOException("Call twain source manager only from within native thread.");
    }
    try{
//    System.out.println("callSourceManager2 time = "+new Date());
      return ncallSourceManager(dg,dat,msg,data);
    }catch(TwainException te){                                // disaster: source exception in native code
      installed=false;throw te;                               // disable jtwain, no calls to twain anymore                                                               
    }
  }

  static int callSource(byte[] source,int dg,int dat,int msg,byte[] data)throws TwainIOException{
    if(Thread.currentThread()!=nativeThread){
      throw new TwainIOException("Call twain source manager only from within native thread.");
    }
    try{
      return ncallSource(source,dg,dat,msg,data);
    }catch(TwainException tioe){                              // disaster: source exception in native code
      installed=false;throw tioe;                             // disable jtwain, no calls to twain anymore                                                               
    }
  }

  static public TwainSourceManager getSourceManager(){
    try{                                                      // wait until native thread is up and running
      while((sourceManager==null)&&installed){Thread.currentThread().sleep(100);} 
    }catch(Exception e){}
    if(!installed){return null;}
    return sourceManager;
  }

  static public TwainSource getSource(){
    try{                                                      // wait until native thread is up and running
      while((sourceManager==null)&&installed){Thread.currentThread().sleep(100);} 
    }catch(Exception e){}
    if(!installed){return null;}
    return sourceManager.getSource();
  }

  static private void trigger(Object caller,int cmd){
    try{                                                      // wait until native thread is up and running
      while((sourceManager==null)&&installed){Thread.currentThread().sleep(100);} 
      if(installed){ntrigger(caller,cmd);}
    }catch(Exception e){}
  }

  static public  void callSetSource(Object caller){trigger(caller,0);} // caller.setSource(sourceManager.getSource());

  static public void setScanner(TwainScanner s){             // use of weak reference to TwainScanner object 
    scanner=new WeakReference(s);                             // allows garbage collection in applets
  }

  static private TwainScanner getScanner(){
    return (TwainScanner)scanner.get();
  }

  static public void select(TwainScanner sc)throws TwainIOException{
    setScanner(sc);
    sourceManager.getSource().checkState(3);
    trigger(sc,1);
  }

  static public void acquire(TwainScanner sc)throws TwainIOException{
    setScanner(sc);
    sourceManager.getSource().checkState(3);
    trigger(sc,2);
  }

  static private Method getMethod(Class clazz,String name,Class[] paramTypes)throws NoSuchMethodException{
    if(clazz==null){ throw new NoSuchMethodException();}
    try{
      return clazz.getDeclaredMethod(name,paramTypes);
    }catch(NoSuchMethodException nsme){
      return getMethod(clazz.getSuperclass(),name,paramTypes);
    }
  }

  static private void cbexecute(Object caller,int cmd){  // callback function cpp -> java; in windows thread;
    TwainSource source=sourceManager.getSource();
    try{
      switch(cmd){
      case 0:                                            // an object wants access to current twain source
       Class clazz =caller.getClass();
        try{                                             // introspection
          Method method=getMethod(clazz,"setSource",new Class[]{TwainSource.class});
          method.invoke(caller,new Object[]{source});    // caller.setSource(source);
        }catch(Throwable e){e.printStackTrace();}        // cannot find method: programming error
        break;
      case 1:                                            // select
        source=sourceManager.selectSource();
        System.out.println(source.toString());
        break;
      case 2:                                            // acquire
        source=sourceManager.openSource();
        try{
          source.enable();
        }catch(TwainIOException e){
          source.close();throw e;
        }
        break;
      }
    }catch(Throwable e){
      signalException(e.getMessage());
      e.printStackTrace();
    }
  }

  static private int cbhandleGetMessage(int msg){        // callback function cpp -> java; windows thread;
    try{
      return sourceManager.getSource().handleGetMessage(msg);
    }catch(Throwable e){
      signalException(e.getMessage());
      e.printStackTrace();
      return TWRC_NOTDSEVENT;
    }
  }
  
  static void signalStateChange(TwainSource source){
    TwainScanner scanner=getScanner();
    if(scanner!=null){
      scanner.setState(source);
    }
  }

  static void signalException(String msg){
    TwainScanner scanner=getScanner();
    if(scanner!=null){
      scanner.signalException(msg);
    }
  }

  static void negotiateCapabilities(TwainSource source){
    TwainScanner scanner=getScanner();
    if(scanner!=null){
      scanner.negotiateCapabilities(source);
    }
  }

  static void transferNativeImage(int dibHandle){
    BufferedImage image=(BufferedImage)ntransferImage(dibHandle);
    if(image!=null){
      TwainScanner scanner=getScanner();
      if(scanner!=null){
        scanner.setImage(image);
      }
    }
  }

  static void transferFileImage(File file){
    if(file!=null){
      TwainScanner scanner=getScanner();
      if(scanner!=null){
        scanner.setImage(file);
      }
    }
  }

  static void transferMemoryBuffer(TwainTransfer.MemoryTransfer.Info info,byte[] buf){
    TwainScanner scanner=getScanner();
    if(scanner!=null){
      scanner.setImageBuffer(info,buf);
    }
  }

  static{
    installed=JarLib.load(jtwain.class,"jtwain");  //  win : load library 'jtwain.dll'
    if(installed){
//      javax.imageio.ImageIO.scanForPlugins();
      nativeThread=new Thread(){
        public void run(){
          try{
            int hwnd=ninitLib();
            if(hwnd!=0){
              sourceManager=new TwainSourceManager(hwnd);
              nstart();
            }
          }catch(Throwable e){
            System.err.println("uk.co.mmscomputing.device.twain.jtwain\t\n"+e.getMessage());
            System.err.println(e);
            e.printStackTrace();
          }
          installed=false;
        }
      };
      nativeThread.start();
    }
  }
}

// [1] Twain Spec 1.9
