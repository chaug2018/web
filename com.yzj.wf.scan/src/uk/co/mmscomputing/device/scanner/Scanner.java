package uk.co.mmscomputing.device.scanner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.co.mmscomputing.device.twain.TwainScanner;

abstract public class Scanner {

	static protected boolean installed = false;

	public abstract boolean isAPIInstalled();

	protected Vector listeners = new Vector(); // list of scanner event
												// listeners
	protected ScannerIOMetadata metadata = new ScannerIOMetadata(); // information
																	// structure

	public abstract void select();

	public abstract void acquire();

	public void addListener(ScannerListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ScannerListener listener) {
		listeners.remove(listener);
	}

	public void fireListenerUpdate(ScannerIOMetadata.Type type) {
		for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
			// System.out.println("now time = "+new Date());
			ScannerListener listener = (ScannerListener) e.nextElement();
			listener.update(type, metadata);
		}
	}

	public JComponent getScanGUI() {
		return new JLabel("Dummy Scanner GUI");
	}

	public JComponent getScanGUI(JPanel gui, GridBagLayout gridbag,
			GridBagConstraints c) {
		return new JLabel("Dummy Scanner GUI");
	}

	static public Scanner getDevice() {
		String osname = System.getProperty("os.name");
		if (osname.startsWith("Windows")) {
			Scanner scanner = TwainScanner.getDevice();
			if (scanner.isAPIInstalled()) {
				return scanner;
			}
		}
		return null;
	}
}
