package it.github.ilmich.dog6502;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import it.github.ilmich.dog6502.ui.Console;
import it.github.ilmich.dog6502.ui.Listeners.FileChangeListener;
import it.github.ilmich.dog6502.ui.Listeners.TabChangeListener;
import it.github.ilmich.dog6502.ui.TabbedPane;
import it.github.ilmich.dog6502.ui.ToolBar;
import it.github.ilmich.dog6502.utils.Log;

public class Dog6502 extends JFrame {

	private static final String TAG = Dog6502.class.getCanonicalName();
	public List<TabChangeListener> tabListeners = new ArrayList<TabChangeListener>();
	public List<FileChangeListener> fileListeners = new ArrayList<FileChangeListener>();
	public static Dog6502 instance = null;
	private TabbedPane pane;
	private Console console;
	private ToolBar tool;
	
	public Dog6502() {
		Log.debug(TAG, "Start application");
		instance = this;
		initUI();
	}
	
	public void onChangeTab(Component comp) {
		for (TabChangeListener tabChangeListener : tabListeners) {
			tabChangeListener.changeTab(comp);
		}
	}
	
	public void onFileListener(File file, int what) {
		for (FileChangeListener fileListener : fileListeners) {
			if (what == FileChangeListener.FILE_OPEN) {
				fileListener.openFile(file);
				continue;
			}
			if (what == FileChangeListener.FILE_CLOSE) {
				fileListener.closeFile(file);
				continue;
			}
			if (what == FileChangeListener.FILE_SAVE) {
				fileListener.saveFile(file);
				continue;
			}
		}
	}

	private void initUI() {
		Log.debug(TAG, "initUI()");
		createMenuBar();
		// central area
		pane = TabbedPane.build();
		console = Console.build();
		JSplitPane spane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,pane,console);  
		spane.setResizeWeight(0.8);
		add(spane, BorderLayout.CENTER);

		// toolbar
		tool = ToolBar.build();
		add(tool, BorderLayout.NORTH);
		
		//setup main window
		setTitle("Dog6502");		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void createMenuBar() {
		Log.debug(TAG, "createMenuBar()");
		var menuBar = new JMenuBar();
		var exitIcon = new ImageIcon("src/resources/exit.png");

		var fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		var eMenuItem = new JMenuItem("Exit", exitIcon);
		eMenuItem.setMnemonic(KeyEvent.VK_E);
		eMenuItem.setToolTipText("Exit application");
		eMenuItem.addActionListener((event) -> System.exit(0));

		fileMenu.add(eMenuItem);
		menuBar.add(fileMenu);

		setJMenuBar(menuBar);
	}
	
	public Action OpenAction = new AbstractAction() {

		final JFileChooser fc = new JFileChooser();
		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = fc.showOpenDialog(Dog6502.instance);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				// This is where a real application would open the file.
				Log.debug("Opening: " + file.getPath());
				pane.openFile(file);
			} else {
				Log.debug("Open command cancelled by user.");
			}
		}
	};
	
	public Action NewAction = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			pane.newFile();
		}
	};

	public static void main(String[] args) {
		Log.DEBUG();
		EventQueue.invokeLater(() -> {
			var ex = new Dog6502();
			ex.setVisible(true);
		});
	}
}
