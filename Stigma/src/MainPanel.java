import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MainPanel extends JPanel {
    public MainPanel() {
        setLayout(new BorderLayout());

        JPanel mainContentPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel imageLabel = new JLabel("", JLabel.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);

        JLabel postLabel = new JLabel("", JLabel.CENTER);
        postLabel.setOpaque(true);
        postLabel.setBackground(Color.WHITE);
        postLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        leftPanel.add(imageLabel, BorderLayout.CENTER);
        leftPanel.add(postLabel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel reasonLabel = new JLabel("", JLabel.CENTER);
        reasonLabel.setOpaque(true);
        reasonLabel.setBackground(Color.WHITE);
        reasonLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        reasonLabel.setFont(new Font("Serif", Font.BOLD, 20));
        
        JLabel charLabel = new JLabel("", JLabel.CENTER);
        charLabel.setOpaque(true);
        charLabel.setBackground(Color.WHITE);
        charLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JScrollPane charPane = new JScrollPane();
        DefaultListModel<String> charModel = new DefaultListModel<>();
        JList<String> charList = new JList<>(charModel);
        charPane.setViewportView(charList);
        
        charList.setOpaque(true);
        charList.setBackground(Color.WHITE);
        charList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        rightPanel.add(reasonLabel);
        rightPanel.add(charPane);
        
        mainContentPanel.add(leftPanel);
        mainContentPanel.add(rightPanel);

        add(mainContentPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bottomPanel.add(new JLabel("검색할 닉네임"));
        JTextField srcnick = new JTextField(20);
        bottomPanel.add(srcnick);
        
        JButton searchBT = new JButton("검색");
        bottomPanel.add(searchBT);
        
        ActionListener searching = new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		imageLabel.setIcon(null);
        		postLabel.setText("");
        		reasonLabel.setText("");
        		charList.removeAll();
        		charModel.removeAllElements();
        		
        		if(srcnick.getText().equals("") || srcnick.getText().contains(" ")) {
        			JOptionPane.showMessageDialog(null, "해당 닉네임은 사용할 수 없습니다.", srcnick.getText(), JOptionPane.INFORMATION_MESSAGE);
        			srcnick.removeAll();
        			return;
        		}
        		
        		try (BufferedReader reader = new BufferedReader(new FileReader("Response.txt"))) {
                    String line;
                    String[] nicks = null;
                	String rs = new String();
                	String imgU = new String(); 
                	URI postU;
                    boolean isFind = false;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(" " + srcnick.getText() + " ")) {
                            isFind = true;
                            nicks = line.split(" ");
                            break;
                        }
                        reader.readLine();
                        reader.readLine();
                        reader.readLine();
                    }
                    if(isFind) {
                    	rs = reader.readLine();
                    	imgU = reader.readLine();
                    	postU = new URI(reader.readLine());
						System.out.println(rs);
						System.out.println(imgU);
						System.out.println(postU);
                    	BufferedImage image = ImageIO.read(new URL(imgU));
                    	imageLabel.setIcon(new ImageIcon(image));
                    	postLabel.setText("<html><a href=\"\">"+postU.toString()+"</a></html>");
                    	postLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    	postLabel.addMouseListener(new MouseListener() {
							@Override
							public void mouseClicked(MouseEvent e) {
								try {
									Desktop.getDesktop().browse(postU);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}

							@Override
							public void mousePressed(MouseEvent e) {}

							@Override
							public void mouseReleased(MouseEvent e) {}

							@Override
							public void mouseEntered(MouseEvent e) {}

							@Override
							public void mouseExited(MouseEvent e) {}
                    	});
                    	reasonLabel.setText(rs);
                    	for(String nick : nicks) {
                    		charModel.addElement(nick);
                    	}
                    	srcnick.removeAll();
                    }
                    else {
                    	JOptionPane.showMessageDialog(null, "해당 닉네임을 찾을 수 없습니다.", srcnick.getText(), JOptionPane.INFORMATION_MESSAGE);
                    	srcnick.removeAll();
                    }

                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e2) {
					e2.printStackTrace();
				}
            }
        };
        
        searchBT.addActionListener(searching);
        srcnick.addActionListener(searching);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
