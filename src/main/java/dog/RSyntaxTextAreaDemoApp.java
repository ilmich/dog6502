package dog;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public final class RSyntaxTextAreaDemoApp extends JFrame {


	private RSyntaxTextAreaDemoApp() {
		setRootPane(new DemoRootPane());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("RSyntaxTextArea Demo Application");
		pack();
	}


	public static void main(String[] args) {
		
	     // Set System L&F
        try {
			UIManager.setLookAndFeel(
			    UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SwingUtilities.invokeLater(() -> {			
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
			new RSyntaxTextAreaDemoApp().setVisible(true);
		});
	}


}