import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ManagePanel extends JPanel {
    private JTextField nicknameField;
    private JTextField reasonArea;
    private JTextField imageUrlField;
    private JTextField postUrlField;
    private JTextField deleteField;

    public ManagePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel addPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        addPanel.setBorder(BorderFactory.createTitledBorder("추가"));
        
        JLabel nickL = new JLabel("추가할 닉네임");
        nickL.setFont(new Font("Serif", Font.BOLD, 20));
        addPanel.add(nickL);
        nicknameField = new JTextField(20);
        addPanel.add(nicknameField);

        JLabel reasonL = new JLabel("추가 사유");
        reasonL.setFont(new Font("Serif", Font.BOLD, 20));
        addPanel.add(reasonL);
        reasonArea = new JTextField();
        addPanel.add(new JScrollPane(reasonArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        JLabel imgL = new JLabel("이미지 주소 첨부 (선택)");
        imgL.setFont(new Font("Serif", Font.BOLD, 20));
        addPanel.add(imgL);
        imageUrlField = new JTextField(20);
        addPanel.add(imageUrlField);

        JLabel postL = new JLabel("게시글 주소 첨부 (선택)");
        postL.setFont(new Font("Serif", Font.BOLD, 20));
        addPanel.add(postL);
        postUrlField = new JTextField(20);
        addPanel.add(postUrlField);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("추가");
        buttonPanel.add(addButton);

        addPanel.add(new JPanel());
        addPanel.add(buttonPanel);

        add(addPanel, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEntry();
            }
        });

        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        deletePanel.setBorder(BorderFactory.createTitledBorder("삭제"));
        deletePanel.add(new JLabel("삭제할 닉네임"));
        deleteField = new JTextField(20);
        deletePanel.add(deleteField);
        JButton deleteButton = new JButton("삭제");
        deletePanel.add(deleteButton);
        
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteEntry();
            }
        });
        
        deleteField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteEntry();
            }
        });

        add(deletePanel, BorderLayout.SOUTH);
    }

    private void addEntry() {
        String nickname = nicknameField.getText();
        String reason = reasonArea.getText();
        String imageUrl = imageUrlField.getText();
        String postUrl = postUrlField.getText();

        AddUser(nickname, reason, imageUrl, postUrl);
        
        nicknameField.setText("");
        reasonArea.setText("");
        imageUrlField.setText("");
        postUrlField.setText("");
    }
    
    private void deleteEntry() {
        String nickname = deleteField.getText();

        DelUser(nickname);
        
        deleteField.setText("");
    }
    
	public void AddUser(String nickname, String rs, String img, String post) {
    	
    	try {
            URL url = new URL("https://developer-lostark.game.onstove.com/characters/"+URLEncoder.encode(nickname, "UTF-8")+"/siblings");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");
            
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAyMTI2MjcifQ.RPoWkvz1HKIHkJK3oo65MxNDbBCUQ95Qo5nOASungbDOlfJbp-1dVkdwWNcy7-6dPE2776kEL5t56Day-eOLcnfjTXeyOQm7bRyVJXB34LcVDavB6GkZrahomIkDMVoAQp5ZWjLKh6gt_Skz0tL06CneMd5FKMWNYwG9RVnggleggidmN3mvv3eCzCL2M51sg3kisd0HpH735DcPN2nEULqp4oudsfY3smbvOltzePIWw5Es5nQ1p7Jtfyf8-y4ME_Vyp3BT-Yl-rTZKc7z_h2APnfUxSlJRJTk7Gk1TSlT0KCP-kAvPzMaw4eh4jTXluwyBqSe988BSUx3IWyPLtQ");
            
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            System.out.println(response.toString());

            try (FileWriter writer = new FileWriter("Response.txt", true)) {
            	ArrayList<String> nicknames = new ArrayList<>();
            	String reason = rs;
            	String imgURL = img;
            	String postURL = post;
            	String[] tokens = response.toString().split("\"CharacterName\":\"");
            	
            	for(int i = 1; i < tokens.length; i++) {
            		nicknames.add(tokens[i].split("\"")[0]);            		
            	}
            	
            	writer.write(' ');
            	for(String s : nicknames)
            		writer.write(s + " ");
            	writer.write("\n");
            	writer.write(reason + "\n");
            	writer.write(imgURL + "\n");
            	writer.write(postURL + "\n");
            	
            	
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	void DelUser(String nickname) {
		if(nickname.equals("") || nickname.contains(" ")) {
			JOptionPane.showMessageDialog(null, "해당 닉네임은 사용할 수 없습니다.", nickname, JOptionPane.INFORMATION_MESSAGE);
		}
		
		try (BufferedReader reader = new BufferedReader(new FileReader("Response.txt"))) {
            String dummy = "";
			String line;

            boolean isFind = false;
            int l = 0;
            while ((line = reader.readLine()) != null) {
                if (l%4==0 && line.contains(" " + nickname + " ")) {
                    isFind = true;
                    break;
                }
                dummy += line + "\r\n";
            }
            if(isFind) {
            	reader.readLine();
            	reader.readLine();
            	reader.readLine();
            	while ((line = reader.readLine()) != null) {
                    dummy += line + "\r\n";
                }
            	reader.close();
            	try (BufferedWriter writer = new BufferedWriter(new FileWriter("Response.txt"))) {
            		writer.write(dummy);
            		writer.close();
            	}
            	catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else {
            	JOptionPane.showMessageDialog(null, "해당 닉네임을 찾을 수 없습니다.", nickname, JOptionPane.INFORMATION_MESSAGE);
            	reader.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }    	
    }
}