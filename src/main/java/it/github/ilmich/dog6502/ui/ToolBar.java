package it.github.ilmich.dog6502.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import it.github.ilmich.dog6502.Dog6502;
import it.github.ilmich.dog6502.ui.Listeners.TabChangeListener;

public class ToolBar extends JToolBar implements ActionListener, TabChangeListener {
	
	public JButton newFile;
	public JButton openFile;
	public JButton saveFile;
	public JButton saveAllFile;
	public JButton undo;
	public JButton redo;
	public Component currentTab;
	
	public ToolBar() {
		super();
		setFloatable(false);
		setBorder(null);
		setRollover(false);
	}
	
	public static ToolBar build() {
		ToolBar tb = new ToolBar();
		
		ImageIcon icon = null;		
		try {
			icon = new ImageIcon(ImageIO.read(tb.getClass().getResourceAsStream("/icons/file-new.png")));
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
        
		tb.newFile = new JButton();
		tb.newFile.setBorderPainted(false);
		tb.newFile.setAction(Dog6502.instance.NewAction);
		tb.newFile.setIcon(icon);
		tb.newFile.setText("New");
        tb.add(tb.newFile);
        
        tb.openFile = new JButton();
        try {
			icon = new ImageIcon(ImageIO.read(tb.getClass().getResourceAsStream("/icons/file-open.png")));
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        tb.openFile.setBorderPainted(false);
        tb.openFile.setAction(Dog6502.instance.OpenAction);
        tb.openFile.setIcon(icon);       
        tb.openFile.setText("Open");
        tb.add(tb.openFile);        
        
        tb.saveFile = new JButton();
        try {
			icon = new ImageIcon(ImageIO.read(tb.getClass().getResourceAsStream("/icons/file-save.png")));
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        tb.saveFile.setBorderPainted(false);
        //tb.saveFile.setAction(Dog6502.pane.OpenAction);
        tb.saveFile.setIcon(icon);       
        tb.saveFile.setText("Save");
        
        tb.add(tb.saveFile);
        
        tb.saveAllFile = new JButton();
        try {
			icon = new ImageIcon(ImageIO.read(tb.getClass().getResourceAsStream("/icons/file-save-all.png")));
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        tb.saveAllFile.setBorderPainted(false);
        //tb.saveAllFile.setAction(Dog6502.instance.OpenAction);
        tb.saveAllFile.setIcon(icon);       
        tb.saveAllFile.setText("Save All");
        
        tb.add(tb.saveAllFile);
        
        tb.undo = new JButton();
        try {
			icon = new ImageIcon(ImageIO.read(tb.getClass().getResourceAsStream("/icons/arrow_undo.png")));
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        tb.undo.setBorderPainted(false);
        //tb.undo.setAction(Dog6502.pane.OpenAction);
        tb.undo.addActionListener(tb);
        tb.undo.setIcon(icon);       
        tb.undo.setText("Undo");
        
        tb.add(tb.undo);
        
        tb.redo = new JButton();
        try {
			icon = new ImageIcon(ImageIO.read(tb.getClass().getResourceAsStream("/icons/arrow_redo.png")));
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        tb.redo.setBorderPainted(false);
        //tb.redo.setAction(Dog6502.pane.OpenAction);
        tb.redo.addActionListener(tb);
        tb.redo.setIcon(icon);       
        tb.redo.setText("Redo");
        
        tb.add(tb.redo);
        
        Dog6502.instance.tabListeners.add(tb);
       
		return tb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (currentTab != null) {
			RSyntaxTextArea dog = (RSyntaxTextArea) currentTab;
			if (e.getSource().equals(undo)) {
				if (dog.canUndo()) {
					dog.undoLastAction();
				}
				return ;
			}
			if (e.getSource().equals(redo)) {
				if (dog.canRedo()) {
					dog.redoLastAction();
				}
				return ;
			}
		}
	}

	@Override
	public void changeTab(Component currentComp) {
		currentTab = currentComp;
	}

}
