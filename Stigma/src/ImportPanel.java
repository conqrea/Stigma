import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ImportPanel extends JPanel {
    private JButton importButton;
    private JButton getListButton;

    public ImportPanel() {
        setLayout(null);

        importButton = new JButton("Import");
        importButton.setBounds(840, 20, 100, 30);
        add(importButton);
        
        getListButton = new JButton("Get List");
        getListButton.setBounds(720, 20, 100, 30);
        add(getListButton);

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    readFile(selectedFile);
                }
            }
        });
        
        getListButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
            	try {
					getDriveList();
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
            }
        });
    }

    private void readFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String nick, rs, img, post;
                nick = line.split(" ")[1];
                System.out.println(nick);
                rs = reader.readLine();
                img = reader.readLine();
                post = reader.readLine();
            	AddUser(nick,rs,img,post);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void getDriveList() throws IOException, URISyntaxException {
    	String PCName = System.getProperty("user.name");
    	
    	System.out.println(PCName);
    	
    	Desktop.getDesktop().browse(new URI("https://drive.usercontent.google.com/u/0/uc?id=1DsAzrML0PqMPuZ1BG282-MhaUJlJukcr&export=download"));
    	while(!(new File("C:/Users/"+PCName+"/Downloads/Response.txt").exists())) {}
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/"+PCName+"/Downloads/Response.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String nick, rs, img, post;
                nick = line.split(" ")[1];
                System.out.println(nick);
                rs = reader.readLine();
                img = reader.readLine();
                post = reader.readLine();
            	AddUser(nick,rs,img,post);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new File("C:/Users/"+PCName+"/Downloads/Response.txt").delete();
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
}