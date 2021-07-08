package uk.co.mmscomputing.device.twain;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.util.JarImageIcon;

public class TwainPanel extends JComponent implements TwainConstants,ScannerListener{

  Scanner scanner=null;
  JButton acqbutton=null;
  JButton selbutton=null;
  JCheckBox guicheckbox=null;

  public TwainPanel(Scanner scanner){
	    this.scanner=scanner;
	    scanner.addListener(this);
	    setLayout(new GridLayout(1,0));
	    
	    // 重新布局按钮位置将扫描放第一位，设置和选择驱动放到最后
	    acqbutton=new JButton("扫描",new JarImageIcon(getClass(),"32x32/scanner.png"));
	    acqbutton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "acquire"));

	    add(acqbutton);

	    JPanel p=new JPanel();
	    p.setBorder(BorderFactory.createEtchedBorder());
	    guicheckbox = new JCheckBox("扫描设置");
	    // guicheckbox.setSelected(true);
	    p.add(guicheckbox);
	    add(p);

	    selbutton=new JButton("选择驱动",new JarImageIcon(getClass(),"32x32/list.png"));
	    selbutton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "select"));
	    add(selbutton);

	    if(jtwain.getSource().isBusy()){                   // applets might not
															// be in state 3
	      acqbutton.setEnabled(false);
	      selbutton.setEnabled(false);
	      guicheckbox.setEnabled(false);
	    }
	  }
  
  public TwainPanel(Scanner scanner,JPanel gui,GridBagLayout gridbag,GridBagConstraints c){
    this.scanner=scanner;
    scanner.addListener(this);
    // setLayout(new GridLayout(1,0));
    setLayout(gridbag);
    
    // 重新布局按钮位置将扫描放第一位，设置和选择驱动放到最后
    acqbutton=new JButton("扫描",new JarImageIcon(getClass(),"32x32/scanner.png"));
    acqbutton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "acquire"));
    c.gridx=0;
    c.gridy=0;
    c.gridwidth = 1;                // reset to the default
    c.gridheight = 2;
    gridbag.setConstraints(acqbutton,c);
    gui.add(acqbutton);

    JPanel p=new JPanel();
    p.setBorder(BorderFactory.createEtchedBorder());
    guicheckbox = new JCheckBox("扫描设置");
    // guicheckbox.setSelected(true);
    p.add(guicheckbox);
    c.gridx=5;
    c.gridy=0;
    c.gridwidth = 1;                // reset to the default
    c.gridheight = 1;
    gridbag.setConstraints(p,c);
    gui.add(p);

    selbutton=new JButton("选择驱动",new JarImageIcon(getClass(),"32x32/list.png"));
    selbutton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "select"));
    c.gridx=6;
    c.gridy=0;
    c.gridwidth = 1;                // reset to the default
    c.gridheight = 1;
    gridbag.setConstraints(selbutton,c);
    gui.add(selbutton);

    if(jtwain.getSource().isBusy()){                   // applets might not be
														// in state 3
      acqbutton.setEnabled(false);
      selbutton.setEnabled(false);
      guicheckbox.setEnabled(false);
    }
  }

  public void acquire(){scanner.acquire();}		         // acquire
															// BufferedImage
															// from
															// selected/default
															// twain source
  public void select(){scanner.select();}  		         // select twain data
															// source

  public void update(ScannerIOMetadata.Type type, final ScannerIOMetadata md){
    if(md instanceof TwainIOMetadata){
      TwainIOMetadata metadata=(TwainIOMetadata)md;
      TwainSource source=metadata.getSource();

      if(metadata.isState(STATE_SRCMNGOPEN)){          // state = 3
        if(source.isBusy()){
          acqbutton.setEnabled(false);
          selbutton.setEnabled(false);
          guicheckbox.setEnabled(false);
        }else{
          acqbutton.setEnabled(true);
          selbutton.setEnabled(true);
          guicheckbox.setEnabled(true);
        }
      }else if(metadata.isState(STATE_SRCOPEN)){       // state = 4; can
														// inquire capabilities
														// now
        if(source.isUIControllable()){                 // if it is possible to
														// hide source's GUI
          source.setShowUI(guicheckbox.isSelected());  // then use checkbox
														// value
        }else{
          if(!guicheckbox.isSelected()){System.out.println("9\bCannot hide twain source's GUI.");}
          guicheckbox.setSelected(true);               // else set to true
														// whatever the user
														// selected
        }
      }
    }
  }
}

