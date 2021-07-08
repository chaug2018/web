package uk.co.mmscomputing.device.twain;

import java.io.*;

abstract class TwainTransfer implements TwainConstants{

  TwainSource source;

  TwainTransfer(TwainSource source){
    this.source=source;
  }

  abstract void initiate()throws TwainIOException;
  abstract void finish()throws TwainIOException;
  abstract void cancel()throws TwainIOException;
  abstract void cleanup()throws TwainIOException;

  static class NativeTransfer extends TwainTransfer{

    byte[] imageHandle=new byte[4];

    NativeTransfer(TwainSource source){
      super(source);
    }

    void initiate()throws TwainIOException{
      source.call(DG_IMAGE,DAT_IMAGENATIVEXFER,MSG_GET,imageHandle);
    }

    void finish()throws TwainIOException{
      int handle=jtwain.getINT32(imageHandle,0);
//      System.err.println("Handle = 0x"+Integer.toHexString(handle));
      jtwain.transferNativeImage(handle);         // convert DIB into BufferedImage and tell listeners about image
    }

    void cancel()throws TwainIOException{
    }

    void cleanup()throws TwainIOException{
    }
  }

  static class FileTransfer extends TwainTransfer{

    File file;

    FileTransfer(TwainSource source){
      super(source);
    }

    void initiate()throws TwainIOException{
      int iff=source.getIFF();
      try{
        File dir=new File(System.getProperty("user.home"),"mmsc/tmp");dir.mkdirs();
        file=File.createTempFile("mmsctwain",ImageFileFormatExts[iff],dir);
      }catch(Exception e){
        file=new File("c:\\mmsctwain.bmp");
      }
      System.err.println(file.getPath());

      byte[] setup=new byte[260];                                           // TW_SETUPFILEXFER
      jtwain.setString(setup,0,file.getPath());                             // file path for new image
      jtwain.setINT16(setup,256,iff);                                       // setup.Format = i.e. TWFF_BMP;
      jtwain.setINT16(setup,258,0);                                         // setup.VRefNum = 0;  Mac only

      source.call(DG_CONTROL,DAT_SETUPFILEXFER,MSG_SET,setup);              // tell source file path and image type
			source.call(DG_IMAGE,DAT_IMAGEFILEXFER,MSG_GET,null);                 // tranfer data
    }

    void finish()throws TwainIOException{
      jtwain.transferFileImage(file);                                       // tell listeners about new image file
    }

    void cancel()throws TwainIOException{
      if((file!=null)&&file.exists()){
        file.delete();                                                      // delete file if it exists
      }
    }

    void cleanup()throws TwainIOException{
    }
  }

  static class MemoryTransfer extends TwainTransfer{

    byte[] imx=new byte[38];                                                // TW_IMAGEMEMXFER

    MemoryTransfer(TwainSource source){
      super(source);
    }

    void initiate()throws TwainIOException{
      byte[] setup=new byte[12];                                            // TW_SETUPMEMXFER
      source.call(DG_CONTROL,DAT_SETUPMEMXFER,MSG_GET,setup);               // get preferred buffer size
      int preferred=jtwain.getINT32(setup,8);
      jtwain.nnew(imx,preferred);                                           // allocate memory buffer
      while(true){                                                          // while(call does not throw TwainIOException){
  		  source.call(DG_IMAGE,DAT_IMAGEMEMXFER,MSG_GET,imx);                 //   tranfer data
        int    bytesWritten=jtwain.getINT32(imx,22);
        byte[] buf=jtwain.ncopy(imx,bytesWritten);                          //   copy from native buffer to java buffer
        if(buf!=null){
          jtwain.transferMemoryBuffer(new Info(imx),buf);                   //   and tell listeners about new image buffer
        }
      }
    }

    void finish()throws TwainIOException{
      int    bytesWritten=jtwain.getINT32(imx,22);
      byte[] buf=jtwain.ncopy(imx,bytesWritten);                            //   copy from native buffer to java buffer
      if(buf!=null){
        jtwain.transferMemoryBuffer(new Info(imx),buf);                     //   and tell listeners about new image data buffer
      }
    }

    void cancel()throws TwainIOException{
    }

    void cleanup()throws TwainIOException{
      jtwain.ndelete(imx);
    }

    static class Info{

      private byte[] imx;

      Info(byte[] imx){
        this.imx=imx;
      }

      int getCompression(){return jtwain.getINT16(imx,0);}
      int getBytesPerRow(){return jtwain.getINT32(imx,2);}
      int getWidth(){return jtwain.getINT32(imx,6);}         // columns
      int getHeight(){return jtwain.getINT32(imx,10);}       // rows
      int getTop(){return jtwain.getINT32(imx,14);}          // xoffset
      int getLeft(){return jtwain.getINT32(imx,18);}         // yoffset
      int getLength(){return jtwain.getINT32(imx,22);}       // bytesWritten

      public String toString(){
        String s=getClass().getName()+"\n";
        s+="\tcompression = "+getCompression()+"\n";
        s+="\tbytes per row = "+getBytesPerRow()+"\n";
        s+="\ttop = "+getTop()+" left = "+getLeft()+" width = "+getWidth()+" height = "+getHeight()+"\n";
        s+="\tbytes = "+getLength()+"\n";
        return s;
      }
    }
  }
}

/*
System.err.println("min: "+jtwain.getINT32(setup,0));
System.err.println("max: "+jtwain.getINT32(setup,4));
System.err.println("preferred: "+preferred);
*/

/*
System.err.println("compression "+jtwain.getINT16(imx,0));
System.err.println("bytesPerRow "+jtwain.getINT32(imx,2));
System.err.println("columns "+jtwain.getINT32(imx,6));
System.err.println("rows "+jtwain.getINT32(imx,10));
System.err.println("xoffset "+jtwain.getINT32(imx,14));
System.err.println("yoffset "+jtwain.getINT32(imx,18));
System.err.println("bytes written "+jtwain.getINT32(imx,22));

System.err.println("imx flags 0x"+Integer.toHexString(jtwain.getINT32(imx,26)));
System.err.println("imx len "+jtwain.getINT32(imx,30));
System.err.println("imx theMem 0x"+Integer.toHexString(jtwain.getINT32(imx,34)));
*/
