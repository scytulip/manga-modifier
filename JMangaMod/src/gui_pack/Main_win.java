package gui_pack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import core_pack.AppSettings;
import core_pack.MangaImgCell;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;



public class Main_win {

	private JFrame frmMangaModifier;
	private final Action actOpenFile = new SwingAction();
	private final Action actSaveImg = new SwingAction_2();
	private final Action actMarkDial = new SwingAction_1();
	private final Action actMarkBG = new SwingAction_3();
	private final Action actWipe = new SwingAction_4();
	private JScrollPane mangaView;
	
	//Create a file chooser
	JFileChooser filech = null;
	//Create a manga image cell to be displayed //TBD: image cell list
	MangaImgCell manga_pic = null;
	//App configuration
	AppSettings app_set = null;
	//Manga display area
	ImageView manga_vw;

	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_win window = new Main_win();
					window.frmMangaModifier.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	}

	/**
	 * Create the application.
	 */
	public Main_win() {
	
		// Setup File Chooser Dialogue
		filech = new JFileChooser();
	    filech.addChoosableFileFilter(new FileNameExtensionFilter(
	            "Bitmap Images", "bmp"));
	    filech.addChoosableFileFilter(new FileNameExtensionFilter(
	            "PNG Images", "png"));
	    filech.addChoosableFileFilter(new FileNameExtensionFilter(
	            "GIF Images", "gif"));
	    filech.addChoosableFileFilter(new FileNameExtensionFilter(
	            "JPEG Images", "jpg", "jpeg"));
		
	    // Setup global status
	    app_set = new AppSettings();
	    app_set.addActEnAfterOpenFile(actSaveImg);
	    app_set.addActEnAfterOpenFile(actMarkDial);
	    app_set.addActEnAfterOpenFile(actMarkBG);
	    app_set.addActEnAfterMarkBG(actWipe);
	    app_set.initialize();
	    
	    initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMangaModifier = new JFrame();
		frmMangaModifier.setMinimumSize(new Dimension(400, 400));
		frmMangaModifier.getContentPane().setMinimumSize(new Dimension(300, 300));
		frmMangaModifier.setTitle(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.frmMangaModifier.title")); //$NON-NLS-1$ //$NON-NLS-2$
		frmMangaModifier.setBounds(100, 100, 722, 620);
		frmMangaModifier.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMangaModifier.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		frmMangaModifier.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnOpenFile = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton.text"));
		btnOpenFile.setFocusable(false);
		btnOpenFile.setFocusPainted(false);
		btnOpenFile.setHideActionText(true);
		btnOpenFile.setAction(actOpenFile);
		toolBar.add(btnOpenFile);
		
		JButton btnSaveFile = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton.text_1")); //$NON-NLS-1$ //$NON-NLS-2$
		btnSaveFile.setFocusable(false);
		btnSaveFile.setFocusPainted(false);
		btnSaveFile.setHideActionText(true);
		btnSaveFile.setAction(actSaveImg);
		toolBar.add(btnSaveFile);
		
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		frmMangaModifier.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		manga_vw = new ImageView(app_set);//Create a image view
		mangaView = new JScrollPane(manga_vw);

		mangaView.setMinimumSize(new Dimension(200, 200));
		splitPane.setLeftComponent(mangaView);
		
		JTabbedPane tabbedTools = new JTabbedPane(JTabbedPane.TOP);
		tabbedTools.setMaximumSize(new Dimension(230, 32767));
		tabbedTools.setMinimumSize(new Dimension(230, 200));
		tabbedTools.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		splitPane.setRightComponent(tabbedTools);
		
		JPanel panelDigWiper = new JPanel();
		tabbedTools.addTab(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.panelDigWiper.title"), null, panelDigWiper, null);
		panelDigWiper.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_8 = new JPanel();
		panelDigWiper.add(panel_8, BorderLayout.NORTH);
		panel_8.setLayout(new MigLayout("", "[::240px,grow,fill]", "[191px][49px][95px]"));
		
		JPanel panel = new JPanel();
		panel_8.add(panel, "cell 0 0,alignx center,aligny center");
		panel.setBorder(new TitledBorder(null, "Step1:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		panel_4.setLayout(new MigLayout("", "[grow,fill]", "[57px][60px,grow]"));
		
		JButton btnMarkDig = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton.text_3")); //$NON-NLS-1$ //$NON-NLS-2$
		btnMarkDig.setAction(actMarkDial);
		panel_4.add(btnMarkDig, "cell 0 0,alignx left,aligny center");
		btnMarkDig.setMnemonic('M');
		
		JPanel panel_3 = new JPanel();
		panel_4.add(panel_3, "cell 0 1,grow");
		panel_3.setLayout(new MigLayout("", "[80px][80px]", "[46px][46px][46px]"));
		
		JLabel label = new JLabel("Tolerance:");
		panel_3.add(label, "cell 0 0,alignx left,aligny top");
		
		JLabel lblRed = new JLabel(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.lblRed.text")); //$NON-NLS-1$ //$NON-NLS-2$
		panel_3.add(lblRed, "flowx,cell 0 1,alignx left,aligny top");
		
		JLabel lblBlue = new JLabel(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.lblBlue.text_1")); //$NON-NLS-1$ //$NON-NLS-2$
		panel_3.add(lblBlue, "flowx,cell 1 1,alignx left,aligny top");
		
		JLabel label_2 = new JLabel("Green:");
		panel_3.add(label_2, "flowx,cell 0 2,alignx left,aligny top");
		
		JLabel lblAlpha = new JLabel(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.lblAlpha.text")); //$NON-NLS-1$ //$NON-NLS-2$
		panel_3.add(lblAlpha, "flowx,cell 1 2");
		
		final JSpinner spRed = new JSpinner();
		spRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Integer x = (Integer)spRed.getValue();
				app_set.setTh_R(x.intValue());
			}
		});

		spRed.setModel(new SpinnerNumberModel(5, 0, 255, 1));
		spRed.setMinimumSize(new Dimension(42, 22));
		panel_3.add(spRed, "cell 0 1,growx");
		
		final JSpinner spGreen = new JSpinner();
		spGreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Integer x = (Integer)spGreen.getValue();
				app_set.setTh_G(x.intValue());
			}
		});
		spGreen.setModel(new SpinnerNumberModel(5, 0, 255, 1));
		spGreen.setMinimumSize(new Dimension(42, 22));
		panel_3.add(spGreen, "cell 0 2,growx");
		
		final JSpinner spBlue = new JSpinner();
		spBlue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Integer x = (Integer)spBlue.getValue();
				app_set.setTh_B(x.intValue());
			}
		});
		spBlue.setModel(new SpinnerNumberModel(5, 0, 255, 1));
		spBlue.setMinimumSize(new Dimension(42, 22));
		panel_3.add(spBlue, "cell 1 1,growx");
		
		final JSpinner spAlpha = new JSpinner();
		spAlpha.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Integer x = (Integer)spAlpha.getValue();
				app_set.setTh_A(x.intValue());
			}
		});
		spAlpha.setModel(new SpinnerNumberModel(5, 0, 255, 1));
		spAlpha.setMinimumSize(new Dimension(42, 22));
		panel_3.add(spAlpha, "cell 1 2,growx");
		
		JPanel panel_1 = new JPanel();
		panel_8.add(panel_1, "cell 0 1,alignx center,aligny center");
		panel_1.setBorder(new TitledBorder(null, "Step2:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setToolTipText((String) null);
		
		JButton btnFillinBackground = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnFillinBackground.text")); //$NON-NLS-1$ //$NON-NLS-2$
		btnFillinBackground.setAction(actMarkBG);
		btnFillinBackground.setMnemonic('B');
		btnFillinBackground.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		panel_1.setLayout(new MigLayout("", "[188px]", "[57px]"));
		panel_1.add(btnFillinBackground, "cell 0 0,grow");
		
		JPanel panel_2 = new JPanel();
		panel_8.add(panel_2, "cell 0 2,growx,aligny center");
		panel_2.setBorder(new TitledBorder(null, "Step3:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setToolTipText((String) null);
		panel_2.setLayout(new MigLayout("", "[grow,fill]", "[57px][50px,fill]"));
		
		JButton btnWipe = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnWipe.text")); //$NON-NLS-1$ //$NON-NLS-2$
		btnWipe.setAction(actWipe);
		btnWipe.setMnemonic('W');
		panel_2.add(btnWipe, "cell 0 0,grow");
		
		JProgressBar progressBar = new JProgressBar();
		panel_2.add(progressBar, "cell 0 1,grow");
		
		JPanel panelPtList = new JPanel();
		tabbedTools.addTab(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.panel_3.arg0"), null, panelPtList, null); //$NON-NLS-1$ //$NON-NLS-2$
		panelPtList.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_9 = new JPanel();
		panelPtList.add(panel_9, BorderLayout.SOUTH);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		JButton btnNewButton = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton.text_6")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton.setMnemonic('D');
		panel_9.add(btnNewButton, BorderLayout.EAST);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panelPtList.add(scrollPane_1, BorderLayout.CENTER);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {""};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane_1.setViewportView(list);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFocusTraversalKeysEnabled(true);
		menuBar.setFocusCycleRoot(true);
		frmMangaModifier.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mnFile.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenFile = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem.text_1")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmOpenFile.setAction(actOpenFile);
		mnFile.add(mntmOpenFile);
		
		JMenuItem mntmSaveFile = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem.text_2")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmSaveFile.setAction(actSaveImg);
		mnFile.add(mntmSaveFile);
		
		JMenu mnEdit = new JMenu(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mnEdit.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mnEdit.setMnemonic('E');
		menuBar.add(mnEdit);
		
		JMenu mnTools = new JMenu(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mnTools.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mnTools.setMnemonic('T');
		menuBar.add(mnTools);
		
		JMenu mnDiagWiper = new JMenu(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mnNewMenu.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mnDiagWiper.setIcon(new ImageIcon(Main_win.class.getResource("/app_res/broom.png")));
		mnDiagWiper.setMnemonic('W');
		mnTools.add(mnDiagWiper);
		
		JMenuItem mntmMkDiag = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem.text_3")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmMkDiag.setAction(actMarkDial);
		mnDiagWiper.add(mntmMkDiag);
		
		JMenuItem mntmMkBg = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem_1.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmMkBg.setAction(actMarkBG);
		mnDiagWiper.add(mntmMkBg);
		
		JMenuItem mntmWipe = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem_2.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmWipe.setAction(actWipe);
		mnDiagWiper.add(mntmWipe);
		
		JMenu mnHelp = new JMenu(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mnHelp.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);
	}

	private class SwingAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingAction() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/fileopen_L.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/fileopen_L.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_O);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actOpenFile.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actOpenFile.short description")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		public void actionPerformed(ActionEvent e) {
			//In response to open a file
			int returnVal = filech.showOpenDialog(frmMangaModifier);
			
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File img_list = filech.getSelectedFile();
				manga_pic = new MangaImgCell();
				if (manga_pic.setImgFile(img_list)) {
					app_set.setFileOpened();
					manga_vw.setManga(manga_pic);
					mangaView.revalidate();
				}			
			}
		}
	}
	
	private class SwingAction_2 extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingAction_2() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/filesave_L.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/filesave_L.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_S);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actSaveImg.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actSaveImg.short description")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_1 extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingAction_1() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/diag.png")));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/diag.png")));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actMarkDial.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actMarkDial.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_M);
		}
		public void actionPerformed(ActionEvent e) {
			app_set.setMarkDial(true);
			app_set.setMarkBG(false);
		}
	}
	private class SwingAction_3 extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingAction_3() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/out_diag.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/out_diag.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_B);
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actMarkBG.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actMarkBG.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
		}
		public void actionPerformed(ActionEvent e) {
			app_set.setMarkDial(false);
			app_set.setMarkBG(true);
		}
	}
	private class SwingAction_4 extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingAction_4() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/Erase.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/Erase.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_W);
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actWipe.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actWipe.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		}
		public void actionPerformed(ActionEvent e) {
			manga_vw.wipeDiag();
		}
	}
}