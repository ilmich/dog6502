package it.github.ilmich.dog6502.ui;

import java.awt.Component;
import java.io.File;

public class Listeners {
	
	public interface TabChangeListener {
		
		public void changeTab(Component currentComp);
		
	}
	
	public interface FileChangeListener {
		
		public static final int FILE_OPEN = 0;
		public static final int FILE_SAVE = 1;
		public static final int FILE_CLOSE = 2;
		
		public void openFile(File file);
		
		public void closeFile(File file);
		
		public void saveFile(File file);
		
	}

}
