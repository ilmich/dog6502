package it.github.ilmich.dog6502.ui;

import java.io.File;

import javax.swing.JTextArea;

import it.github.ilmich.dog6502.Dog6502;
import it.github.ilmich.dog6502.ui.Listeners.FileChangeListener;

public class Console extends JTextArea implements FileChangeListener {
	
	public static Console build() {
		Console tis = new Console();
		
		Dog6502.instance.fileListeners.add(tis);
		
		return tis;
	}

	@Override
	public void openFile(File file) {
		append("Opened file " + file.getAbsolutePath());
	}

	@Override
	public void closeFile(File file) {
		append("Closed file " + file.getAbsolutePath());		
	}

	@Override
	public void saveFile(File file) {
		append("Saved file " + file.getAbsolutePath());
	}


}
