package core_pack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;

public class AppSettings {
	
	public static final int WS_IDLE = 0;
	public static final int WS_MARK_DIAG = 1;
	public static final int WS_MARK_BKG = 2;
	
	List<Action> lstEnAfterOpenFile = null; // List of actions to be enabled after file open
	
	boolean bFileOpened = false; // Sign: Current file is opened
	
	int intMaxUnitIncrement = 30; //Max unit increment of the scrolling behavior
	int th_A = 5, th_R = 5, th_G = 5, th_B = 5; // RGB edge threshold values
	int wkStatus = 0; // Work space status
	
	int colorDiagMask = 0x77ffff00; // Color of dialogue masks
	int colorBkgMask = 0x770000ff; // Color of background masks
	int colorFilling = 0xffffffff; // Color of dialogue filling
	
	// Work space status control
	public int getWkStatus() {
		return wkStatus;
	}

	public void setWkStatus(int wkStatus) {
		this.wkStatus = wkStatus;
	}
	
	// Initialization
	public void initialize() {
		setFileClosed();
	}
	
	// File status marker
	public void setFileOpened() { 
		bFileOpened = true; 
		enableActions(lstEnAfterOpenFile);
	}
	public void setFileClosed() { 
		bFileOpened = false; 
		disableActions(lstEnAfterOpenFile);
	}
	
	// Set actions to be enabled/disable after file is opened/closed
	public void addActEnAfterOpenFile(Action act) {
		lstEnAfterOpenFile.add(act);
	}
	
	// Action operations
	private void enableActions(List<Action> lst) {
		Iterator<Action> i = lst.iterator();
		while (i.hasNext()) {
			Action act = (Action)i.next();
			act.setEnabled(true);
		}
	}
	private void disableActions(List<Action> lst) {
		Iterator<Action> i = lst.iterator();
		while (i.hasNext()) {
			Action act = i.next();
			act.setEnabled(false);
		}
	}

	// Increment parameters
	public int getIntMaxUnitIncrement() {
		return intMaxUnitIncrement;
	}

	public void setIntMaxUnitIncrement(int intMaxUnitIncrement) {
		this.intMaxUnitIncrement = intMaxUnitIncrement;
	}

	public int getTh_A() {return th_A;}
	public void setTh_A(int th_A) {this.th_A = th_A;}
	public int getTh_R() {return th_R;}
	public void setTh_R(int th_R) {this.th_R = th_R;}
	public int getTh_G() {return th_G;}
	public void setTh_G(int th_G) {this.th_G = th_G;}
	public int getTh_B() {return th_B;}
	public void setTh_B(int th_B) {this.th_B = th_B;}

	public int getColorDiagMask() {
		return colorDiagMask;
	}

	public void setColorDiagMask(int colorDiagMask) {
		this.colorDiagMask = colorDiagMask;
	}

	public int getColorBkgMask() {
		return colorBkgMask;
	}

	public void setColorBkgMask(int colorBkgMask) {
		this.colorBkgMask = colorBkgMask;
	}

	public int getColorFilling() {
		return colorFilling;
	}

	public void setColorFilling(int colorFilling) {
		this.colorFilling = colorFilling;
	}

	public AppSettings() {
		lstEnAfterOpenFile = new ArrayList<Action>();
	}

}
