package reversi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Iago, Kaique, Doce pudim
 *
 */
@SuppressWarnings("serial")
public class main extends JFrame{
    
    private static JPanel pnlLeft;
    
    public main() {
    	
        super("ERA UMA VEZ UM PUDIM APAIXONADO");
        setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setSize(width/2, height/2);

        setLocationRelativeTo(null);
        
        try {
			pnlLeft = new ReversiGui();
		} catch (IOException e) {
			e.printStackTrace();
		}
        add(pnlLeft, BorderLayout.CENTER);
        
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new main();
    }
    
}
