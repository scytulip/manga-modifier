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
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;
import core_pack.AppSettings;
import core_pack.MangaImgCell;



public class Main_win {
	
	// Tab indexes
	final static int TAB_FILELIST = 0;
	final static int TAB_WIPE = 1;
	
	//Create a file chooser
	private JFileChooser filech = null;
	//Create a manga image cell to be displayed //TBD: image cell list
	private DefaultListModel manga_pics = null;
	//App configuration
	private AppSettings app_set = null;
	//Manga display area
	private ImageView manga_vw;
	

	private JFrame frmMangaModifier;
	private JScrollPane mangaView;
	private JList lvAllFiles;
	private JTabbedPane tabbedTools;
	private final Action actOpenFile = new SwingAction();
	private final Action actSaveImg = new SwingAction_2();
	private final Action actMarkDial = new SwingAction_1();
	private final Action actMarkBG = new SwingAction_3();
	private final Action actWipe = new SwingAction_4();
	private final Action actSaveImgAs = new SwingAction_5();
	private final Action actFileListPrev = new SwingAction_6();
	private final Action actFileListNext = new SwingAction_7();
	private final Action actExit = new SwingAction_8();
	private final Action actSaveAll = new SwingAction_9();
	private final Action actUndo = new SwingAction_10();
	private final Action actRedo = new SwingAction_11();
	private final Action actCopy = new SwingAction_12();
	private final Action actZoomIn = new SwingAction_13();
	private final Action actZoomOut = new SwingAction_14();
	private final Action actFitWin = new SwingAction_15();
	
	

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
	
		/* Setup File Chooser Dialogue */
		filech = new JFileChooser();
	    filech.addChoosableFileFilter(new FileNameExtensionFilter(
	            "Bitmap Images", "bmp"));
	    filech.addChoosableFileFilter(new FileNameExtensionFilter(
	            "GIF Images", "gif"));
	    filech.addChoosableFileFilter(new FileNameExtensionFilter(
	            "JPEG Images", "jpg", "jpeg"));
	    filech.addChoosableFileFilter(new FileNameExtensionFilter(
	            "PNG Images", "png"));
	    filech.setMultiSelectionEnabled(true);
		
	    /* Setup global status */
	    app_set = new AppSettings();
	    app_set.addActEnAfterOpenFile(actSaveImg);
	    app_set.addActEnAfterOpenFile(actMarkDial);
	    app_set.addActEnAfterOpenFile(actMarkBG);
	    app_set.addActEnAfterOpenFile(actWipe);
	    app_set.addActEnAfterOpenFile(actFileListNext);
	    app_set.addActEnAfterOpenFile(actFileListPrev);
	    app_set.addActEnAfterOpenFile(actSaveImgAs);
	    app_set.addActEnAfterOpenFile(actSaveAll);
	    app_set.addActEnAfterOpenFile(actZoomIn);
	    app_set.addActEnAfterOpenFile(actZoomOut);
	    app_set.addActEnAfterOpenFile(actFitWin);
	    app_set.initialize();
	    
	    /* Initialize image lists */
	    manga_pics = new DefaultListModel();
	    		
	    initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		actCopy.setEnabled(false);
		actRedo.setEnabled(false);
		actUndo.setEnabled(false);
		frmMangaModifier = new JFrame();
		frmMangaModifier.setMinimumSize(new Dimension(600, 400));
		frmMangaModifier.getContentPane().setMinimumSize(new Dimension(300, 300));
		frmMangaModifier.setTitle(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.frmMangaModifier.title")); //$NON-NLS-1$ //$NON-NLS-2$
		frmMangaModifier.setBounds(100, 100, 821, 687);
		frmMangaModifier.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMangaModifier.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		frmMangaModifier.getContentPane().add(toolBar, BorderLayout.WEST);
		
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
		
		JButton button = toolBar.add(actSaveImgAs);
		button.setFocusPainted(false);
		button.setFocusable(false);
		button.setHideActionText(true);
		
		JButton btnNewButton_5 = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton_5.text")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_5.setHideActionText(true);
		btnNewButton_5.setFocusable(false);
		btnNewButton_5.setFocusTraversalKeysEnabled(false);
		btnNewButton_5.setAction(actSaveAll);
		toolBar.add(btnNewButton_5);
		toolBar.addSeparator();
		
		JButton btnNewButton = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton.text_13")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton.setFocusable(false);
		btnNewButton.setFocusTraversalKeysEnabled(false);
		btnNewButton.setHideActionText(true);
		btnNewButton.setAction(actFileListPrev);
		toolBar.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton_1.text_2")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_1.setFocusTraversalKeysEnabled(false);
		btnNewButton_1.setFocusable(false);
		btnNewButton_1.setHideActionText(true);
		btnNewButton_1.setAction(actFileListNext);
		toolBar.add(btnNewButton_1);
		toolBar.addSeparator();
		
		JButton btnNewButton_2 = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton_2.text_2")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_2.setHideActionText(true);
		btnNewButton_2.setFocusable(false);
		btnNewButton_2.setFocusTraversalKeysEnabled(false);
		btnNewButton_2.setAction(actUndo);
		toolBar.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton_3.text")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_3.setHideActionText(true);
		btnNewButton_3.setFocusable(false);
		btnNewButton_3.setFocusTraversalKeysEnabled(false);
		btnNewButton_3.setAction(actRedo);
		toolBar.add(btnNewButton_3);
		toolBar.addSeparator();
		
		JButton btnNewButton_4 = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton_4.text")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_4.setHideActionText(true);
		btnNewButton_4.setFocusable(false);
		btnNewButton_4.setFocusTraversalKeysEnabled(false);
		btnNewButton_4.setAction(actCopy);
		toolBar.add(btnNewButton_4);
		
		JButton btnNewButton_6 = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton_6.text_1")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_6.setHideActionText(true);
		btnNewButton_6.setFocusable(false);
		btnNewButton_6.setFocusTraversalKeysEnabled(false);
		btnNewButton_6.setAction(actFitWin);
		toolBar.addSeparator();
		toolBar.add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.btnNewButton_7.text")); //$NON-NLS-1$ //$NON-NLS-2$
		btnNewButton_7.setHideActionText(true);
		btnNewButton_7.setFocusable(false);
		btnNewButton_7.setFocusTraversalKeysEnabled(false);
		btnNewButton_7.setAction(actZoomIn);
		toolBar.add(btnNewButton_7);
		
		JButton button_1 = new JButton("New button");
		button_1.setHideActionText(true);
		button_1.setFocusable(false);
		button_1.setFocusTraversalKeysEnabled(false);
		button_1.setAction(actZoomOut);
		toolBar.add(button_1);
		
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		frmMangaModifier.getContentPane().add(splitPane);
		
		manga_vw = new ImageView(app_set);//Create a image view
		mangaView = new JScrollPane(manga_vw);

		mangaView.setMinimumSize(new Dimension(200, 200));
		splitPane.setLeftComponent(mangaView);
		
		tabbedTools = new JTabbedPane(JTabbedPane.TOP);
		tabbedTools.setMaximumSize(new Dimension(230, 32767));
		tabbedTools.setMinimumSize(new Dimension(230, 200));
		tabbedTools.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		splitPane.setRightComponent(tabbedTools);
		
		JPanel panelFileList = new JPanel();
		tabbedTools.addTab(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.panel_5.title"), null, panelFileList, null); //$NON-NLS-1$ //$NON-NLS-2$
		panelFileList.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setPreferredSize(new Dimension(100, 2));
		panelFileList.add(scrollPane_1, BorderLayout.CENTER);
		
		lvAllFiles = new JList();
		lvAllFiles.setFocusable(false);
		lvAllFiles.setFocusTraversalKeysEnabled(false);
		lvAllFiles.setVisibleRowCount(5);
		lvAllFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		PreviewLabel k = new PreviewLabel(manga_vw, lvAllFiles);
		lvAllFiles.setCellRenderer(k);
		lvAllFiles.addListSelectionListener(k);
		scrollPane_1.setViewportView(lvAllFiles);
		
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
		panel_1.setLayout(new MigLayout("", "[grow,fill]", "[57px]"));
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
		
		JMenuBar menuBar = new JMenuBar();
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
		
		JMenuItem mntmSaveAll = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem_1.text_2")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmSaveAll.setAction(actSaveAll);
		mnFile.add(mntmSaveAll);
		
		JMenuItem mntmSaveAs = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem.text_4")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmSaveAs.setAction(actSaveImgAs);
		mnFile.add(mntmSaveAs);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmPrevFile = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem.text_5")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmPrevFile.setAction(actFileListPrev);
		mnFile.add(mntmPrevFile);
		
		JMenuItem mntmNextFile = new JMenuItem("New menu item");
		mntmNextFile.setAction(actFileListNext);
		mnFile.add(mntmNextFile);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmExit = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem_1.text_1")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmExit.setAction(actExit);
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mnEdit.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mnEdit.setMnemonic('E');
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem.text_6")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmUndo.setAction(actUndo);
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem_1.text_3")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmRedo.setAction(actRedo);
		mnEdit.add(mntmRedo);
		
		JSeparator separator_2 = new JSeparator();
		mnEdit.add(separator_2);
		
		JMenuItem mntmCopy = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem_2.text_1")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmCopy.setAction(actCopy);
		mnEdit.add(mntmCopy);
		
		JSeparator separator_3 = new JSeparator();
		mnEdit.add(separator_3);
		
		JMenuItem mntmZoomIn = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem_3.text_1")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmZoomIn.setAction(actZoomIn);
		mnEdit.add(mntmZoomIn);
		
		JMenuItem mntmZoomOut = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem_4.text_1")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmZoomOut.setAction(actZoomOut);
		mnEdit.add(mntmZoomOut);
		
		JMenuItem mntmFitWin = new JMenuItem(ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.mntmNewMenuItem_5.text")); //$NON-NLS-1$ //$NON-NLS-2$
		mntmFitWin.setAction(actFitWin);
		mnEdit.add(mntmFitWin);
		
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
		 * Action: Open file
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
			
			int returnVal = filech.showOpenDialog(frmMangaModifier);
			int j;
			MangaImgCell manga_cell;
			
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				tabbedTools.setSelectedIndex(TAB_FILELIST);
				
				File[] img_list = filech.getSelectedFiles();
		
				lvAllFiles.setModel(manga_pics);
				
				for (j=0; j<img_list.length; j++)
				{
					manga_cell = new MangaImgCell();
					if (manga_cell.setImgFile(img_list[j])) {
						manga_pics.addElement(manga_cell);
					} else {
						manga_cell = null;
					}
				}
				if (manga_pics.size()>0) {
					// TODO: Close files
					app_set.setFileOpened();
					lvAllFiles.setSelectedIndex(0);
				}
			}
		}
	}
	
	
	private class SwingAction_2 extends AbstractAction {
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
			tabbedTools.setSelectedIndex(TAB_WIPE);
			app_set.setWkStatus(AppSettings.WS_MARK_DIAG);
		}
	}
	private class SwingAction_3 extends AbstractAction {
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
			tabbedTools.setSelectedIndex(TAB_WIPE);
			app_set.setWkStatus(AppSettings.WS_MARK_BKG);
		}
	}
	private class SwingAction_4 extends AbstractAction {
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
			tabbedTools.setSelectedIndex(TAB_WIPE);
			manga_vw.wipeDiag();
		}
	}
	private class SwingAction_5 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_5() {
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/filesaveas_L.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/filesaveas_L.png")));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.action.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.action.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_A);
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_6 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_6() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/last_blue.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/last_blue.png")));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actFileListPrev.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actFileListPrev.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_V);
		}
		public void actionPerformed(ActionEvent e) {
			int idx = lvAllFiles.getSelectedIndex();
			if (idx != -1 && idx > 0) { lvAllFiles.setSelectedIndex(idx-1); }
		}
	}
	private class SwingAction_7 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_7() {
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK));
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/next_blue.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/next_blue.png")));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actFileListNext.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actFileListNext.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_N);
		}
		public void actionPerformed(ActionEvent e) {
			int idx = lvAllFiles.getSelectedIndex();
			if (idx != -1 && idx < manga_pics.size()-1) { lvAllFiles.setSelectedIndex(idx+1); }
		}
	}
	private class SwingAction_8 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_8() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/exit.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/exit.png")));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actExit.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actExit.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_X);
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_9 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_9() {
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/filesaveall_L .png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/filesaveall_L .png")));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actionSaveAll.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actionSaveAll.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_L);
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_10 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_10() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/undo_img.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/undo_img.png")));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actUndo.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actUndo.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_U);
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_11 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_11() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/redo_img.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/redo_img.png")));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actRedo.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actRedo.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_R);
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_12 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_12() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/copy.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/copy.png")));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actCopy.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actCopy.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_C);
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	private class SwingAction_13 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_13() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/zoomin.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/zoomin.png")));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actZoomIn.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actZoomIn.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_I);
		}
		public void actionPerformed(ActionEvent e) {
			manga_vw.setZoomIn(); // Zoom in
		}
	}
	private class SwingAction_14 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_14() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/zoomout.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/zoomout.png")));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actZoomOut.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actZoomOut.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_O);
		}
		public void actionPerformed(ActionEvent e) {
			manga_vw.setZoomOut(); // Zoom out
		}
	}
	private class SwingAction_15 extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction_15() {
			putValue(SMALL_ICON, new ImageIcon(Main_win.class.getResource("/app_res/fitwindow.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(Main_win.class.getResource("/app_res/fitwindow.png")));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
			putValue(NAME, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actFitWin.name")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(SHORT_DESCRIPTION, ResourceBundle.getBundle("gui_pack.LangPack").getString("Main_win.actFitWin.short description")); //$NON-NLS-1$ //$NON-NLS-2$
			putValue(MNEMONIC_KEY, KeyEvent.VK_F);
		}
		public void actionPerformed(ActionEvent e) {
			manga_vw.setZoomFit(); // Auto fit
		}
	}
}
