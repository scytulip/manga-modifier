package core_pack;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import javax.swing.Action;

public class AppSettings {
	
	private boolean bFileOpened = false; // Sign: Current file is opened
	private List<Action> lstEnAfterOpenFile = null; // List of actions to be enabled after file open
	private int intMaxUnitIncrement = 1; //Max unit increment of the scrolling behavior
	
	private boolean bBGMarked = false; // Sign: At least one Background is marked
	private List<Action> lstEnAfterMarkBG = null; // List of actions to be enabled after marking background
	
	private boolean bMarkDial = false; // Sign: Dialogues could be marked
	private boolean bMarkBG = false; // Sign: Background could be marked
	
	private int th_A = 5, th_R = 5, th_G = 5, th_B = 5; // RGB edge threshold values
	
	public AppSettings() {
		lstEnAfterOpenFile = new ArrayList<Action>();
		lstEnAfterMarkBG = new ArrayList<Action>();
	}
	
	// Initialization
	public void initialize() {
		setFileClosed();
		setBGUnmarked();
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
	
	// BGogue marking status
	public void setBGMarked() { 
		bBGMarked = true; 
		enableActions(lstEnAfterMarkBG);
	}
	public void setBGUnmarked() { 
		bBGMarked = false; 
		disableActions(lstEnAfterMarkBG);
	}
	
	// Set actions to be enabled/disable after file is opened/closed
	public void addActEnAfterOpenFile(Action act) {
		lstEnAfterOpenFile.add(act);
	}
	public void addActEnAfterMarkBG(Action act) {
		lstEnAfterMarkBG.add(act);
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

	public int getTh_A() {
		return th_A;
	}

	public void setTh_A(int th_A) {
		this.th_A = th_A;
	}

	public int getTh_R() {
		return th_R;
	}

	public void setTh_R(int th_R) {
		this.th_R = th_R;
	}

	public int getTh_G() {
		return th_G;
	}

	public void setTh_G(int th_G) {
		this.th_G = th_G;
	}

	public int getTh_B() {
		return th_B;
	}

	public void setTh_B(int th_B) {
		this.th_B = th_B;
	}

	public boolean isMarkDial() {
		return bMarkDial;
	}

	public void setMarkDial(boolean bMarkDial) {
		this.bMarkDial = bMarkDial;
	}

	public boolean isMarkBG() {
		return bMarkBG;
	}

	public void setMarkBG(boolean bMarkBG) {
		this.bMarkBG = bMarkBG;
	}
}
