package it.github.ilmich.dog6502.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import it.github.ilmich.dog6502.Dog6502;
import it.github.ilmich.dog6502.ui.Listeners.FileChangeListener;
import it.github.ilmich.dog6502.utils.Log;

public class TabbedPane extends JTabbedPane implements ChangeListener {

	public static TabbedPane build() {
		TabbedPane tis = new TabbedPane();
		tis.addChangeListener(tis);
		return tis;
	}

	public RSyntaxTextArea makeTextPanel(File file) {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);

		if (file != null) {
			try {
				textArea.setText(Files.readString(file.toPath()));
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			textArea.setCaretPosition(0);
		}
		return textArea;
	}

	public void newFile() {
		RSyntaxTextArea comp = makeTextPanel(null);
		Log.debug("new file");
		if (comp != null) {
			RTextScrollPane scrollPane = new RTextScrollPane(comp, true);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

			addTab("new", scrollPane);
			// tabs.set(getTabCount() - 1, comp);
			setSelectedIndex(getTabCount() - 1);
			setTabComponentAt(getTabCount() - 1, new ButtonTabComponent(this));
		}
	}

	public void openFile(File file) {
		JComponent comp = makeTextPanel(file);

		if (comp != null) {
			RTextScrollPane scrollPane = new RTextScrollPane(comp, true);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

			addTab(file.getName(), scrollPane);
			setSelectedIndex(getTabCount() - 1);
			setTabComponentAt(getTabCount() - 1, new ButtonTabComponent(this));
			Dog6502.instance.onFileListener(file, FileChangeListener.FILE_OPEN);
		}
	}

	// stolen from
	// https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabComponentsDemoProject/src/components/ButtonTabComponent.java
	public class ButtonTabComponent extends JPanel {
		private final JTabbedPane pane;

		private final MouseListener buttonMouseListener = new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				Component component = e.getComponent();
				if (component instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) component;
					button.setBorderPainted(true);
				}
			}

			public void mouseExited(MouseEvent e) {
				Component component = e.getComponent();
				if (component instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) component;
					button.setBorderPainted(false);
				}
			}
		};

		public ButtonTabComponent(final JTabbedPane pane) {
			// unset default FlowLayout' gaps
			super(new FlowLayout(FlowLayout.LEFT, 0, 0));
			if (pane == null) {
				throw new NullPointerException("TabbedPane is null");
			}
			this.pane = pane;
			setOpaque(false);

			// make JLabel read titles from JTabbedPane
			JLabel label = new JLabel() {
				public String getText() {
					int i = pane.indexOfTabComponent(ButtonTabComponent.this);
					if (i != -1) {
						return pane.getTitleAt(i);
					}
					return null;
				}
			};

			add(label);
			// add more space between the label and the button
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			// tab button
			JButton button = new TabButton();
			add(button);
			// add more space to the top of the component
			setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		}

		private class TabButton extends JButton implements ActionListener {
			public TabButton() {
				int size = 17;
				setPreferredSize(new Dimension(size, size));
				setToolTipText("close this tab");
				// Make the button looks the same for all Laf's
				setUI(new BasicButtonUI());
				// Make it transparent
				setContentAreaFilled(false);
				// No need to be focusable
				setFocusable(false);
				setBorder(BorderFactory.createEtchedBorder());
				setBorderPainted(false);
				// Making nice rollover effect
				// we use the same listener for all buttons
				addMouseListener(buttonMouseListener);
				setRolloverEnabled(true);
				// Close the proper tab by clicking the button
				addActionListener(this);
			}

			public void actionPerformed(ActionEvent e) {
				int i = pane.indexOfTabComponent(ButtonTabComponent.this);
				if (i != -1) {
					pane.remove(i);
				}
			}

			// we don't want to update UI for this button
			public void updateUI() {
			}

			// paint the cross
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				// shift the image for pressed buttons
				if (getModel().isPressed()) {
					g2.translate(1, 1);
				}
				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.BLACK);
				if (getModel().isRollover()) {
					g2.setColor(Color.MAGENTA);
				}
				int delta = 6;
				g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
				g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
				g2.dispose();
			}
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (getTabCount() > 0) {
			Component comp = getComponentAt(getSelectedIndex());
			if (comp instanceof RTextScrollPane) {
				RSyntaxTextArea txt = (RSyntaxTextArea) ((RTextScrollPane) comp).getViewport().getView();
				Dog6502.instance.onChangeTab(txt);
			}
		}
	}
}
