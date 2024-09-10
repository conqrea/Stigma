import javax.swing.*;
import java.net.URL;

public class MainForm extends JFrame {
	
	JTabbedPane tabbedPane = new JTabbedPane();

    public MainForm() {
        setTitle("Lostark Stigma");
        URL imgURL = MainForm.class.getClassLoader().getResource("sticon.png");
        ImageIcon img = new ImageIcon(imgURL);
        setIconImage(img.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        
        tabbedPane.addTab("Main", new MainPanel());
        tabbedPane.addTab("Add/Delete", new ManagePanel());
        tabbedPane.addTab("Import", new ImportPanel());
        
        add(tabbedPane);
    }

    public static void main(String[] args) {
        MainForm frame = new MainForm();
        frame.setVisible(true);
    }
}