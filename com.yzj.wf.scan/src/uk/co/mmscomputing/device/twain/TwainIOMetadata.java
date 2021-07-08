package uk.co.mmscomputing.device.twain;

import java.io.IOException;

import uk.co.mmscomputing.device.scanner.*;

public class TwainIOMetadata extends ScannerIOMetadata{

  static public final String[] TWAIN_STATE = {
    "",
    "Pre-Session",
    "Source Manager Loaded",
    "Source Manager Open",
    "Source Open",
    "Source Enabled",
    "Transfer Ready",
    "Transferring Data",
  };

  public String      getStateStr(){ return TWAIN_STATE[getState()];}

  private TwainSource source=null;

  public TwainSource getSource(){return source;}
  public void        setSource(TwainSource source){this.source=source;}
}