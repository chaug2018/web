package uk.co.mmscomputing.device.twain;

import java.util.Vector;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.*;
import javax.swing.*;

import java.io.*;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.stream.*;


import uk.co.mmscomputing.device.scanner.*;

public class TwainScanner extends Scanner implements TwainConstants{

  TwainScanner(){
    metadata=new TwainIOMetadata();
    jtwain.setScanner(this);
  }

// call native routine; TwainScanner -> jtwain.class -> jtwain.dll

  public void select(){		      	       // select twain source
    try{
      jtwain.select(this);
    }catch(Exception e){
      metadata.setException(e);
      fireListenerUpdate(metadata.EXCEPTION);
    }
  }
 
  public void acquire(){                 // initiate scan
    try{
      jtwain.acquire(this);
    }catch(Exception e){
    	System.out.println("888888888888888888");
      metadata.setException(e);
      fireListenerUpdate(metadata.EXCEPTION);
    }
  }

// callback function jtwain.dll -> jtwain.class -> TwainScanner.class

  void setImage(BufferedImage image){    // received an image
    try{
      metadata.setImage(image);
      fireListenerUpdate(metadata.ACQUIRED);
    }catch(Exception e){
      metadata.setException(e);
      fireListenerUpdate(metadata.EXCEPTION);
    }
  }

  void setImage(File file){              // received an image using file transfer
    try{
      metadata.setFile(file);
      fireListenerUpdate(metadata.FILE);
    }catch(Exception e){
      metadata.setException(e);
      fireListenerUpdate(metadata.EXCEPTION);
    }
  }

  void setImageBuffer(TwainTransfer.MemoryTransfer.Info info,byte[] buf){
    // we don't do anything here yet
    System.out.println(info.toString());
  }


  protected void negotiateCapabilities(TwainSource source){     
    // Called in jtwain when source is in state 4: negotiate capabilities
    ((TwainIOMetadata)metadata).setSource(source);
    fireListenerUpdate(metadata.NEGOTIATE);
  }

  void setState(TwainSource source){
    metadata.setState(source.getState());
    ((TwainIOMetadata)metadata).setSource(source);
/*
    if(metadata.isState(STATE_SRCOPEN)){          // state = 4
      if(metadata.getLastState()<metadata.getState()){
        negotiateCapabilities(((TwainIOMetadata)metadata).getSource());
      }
    }
*/
    fireListenerUpdate(metadata.STATECHANGE);
  }

  void signalInfo(String msg,int val){
    metadata.setInfo(msg+" [0x"+Integer.toHexString(val)+"]");
    fireListenerUpdate(metadata.INFO);
  }

  void signalException(String msg){
    Exception e=new TwainIOException(getClass().getName()+".setException:\n    "+msg);
    metadata.setException(e);
    fireListenerUpdate(metadata.EXCEPTION);
  }

  public JComponent getScanGUI(){return new TwainPanel(this);}
  public JComponent getScanGUI(JPanel gui,GridBagLayout gridbag,GridBagConstraints c){return new TwainPanel(this,gui,gridbag,c);}
  public boolean isAPIInstalled(){return jtwain.isInstalled();}

  protected void finalize()throws Throwable{
    System.err.println(getClass().getName()+".finalize:");
  }

  static public Scanner getDevice(){
    return new TwainScanner();
  }
}
