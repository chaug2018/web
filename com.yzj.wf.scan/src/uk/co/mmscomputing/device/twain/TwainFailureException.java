package uk.co.mmscomputing.device.twain;


public class TwainFailureException extends TwainResultException{

  private int cc;

  public TwainFailureException(int cc){
    super(createMessage("Failed during call to twain source.",cc),TwainConstants.TWRC_FAILURE);
  }

  public TwainFailureException(String msg,int cc){
    super(createMessage(msg,cc),TwainConstants.TWRC_FAILURE);
  }

  public int getConditionCode(){return cc;}

  static private String createMessage(String msg,int cc){
    try{
      return msg+"\n\tcc="+TwainConstants.info[cc];
    }catch(Exception e){
      return msg+"\n\tcc=0x"+Integer.toHexString(cc);
    }
  }
}