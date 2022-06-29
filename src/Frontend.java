import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import SupportClasses.*;

import javax.swing.*;

/**
 * References:
 * https://github.com/karthikrangasai/Extendible-Hashing-Simulator
 * https://github.com/angelosps/Extendible-Hashing
 * https://github.com/hoytak/hashreduce
 * */

public class Frontend {
	
	static int blockingFactor = 0;
	static int globalDepth = 0;
	static int localDepth = 0;
	static int m = 0;
	static boolean validInput = false;
	static Directory fileDirectory = null;

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					createFrontend();
				}
			});
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	private static void createFrontend() {
		JFrame frame = new JFrame("Dynamic Hashing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(frame.getContentPane());
		
		Insets insets = frame.getInsets();
		frame.setSize(720 , 750);
		frame.setVisible(true);
	}

	private static void addComponentsToPane(Container contentPane) {
		contentPane.setLayout(null);
		java.util.regex.Pattern regexPattern = java.util.regex.Pattern.compile("[0-9]+");
		
		JLabel heading = new JLabel("Dynamic Hashing Simulator");
		JLabel details = new JLabel();
		JLabel inputLabel = new JLabel("Please Enter the Initial Keys Here:");
		JLabel errorLabel = new JLabel("Error Label");
		JLabel hashValueLabel = new JLabel("Hash value :");
		JLabel hashValue = new JLabel();
		JLabel bucketNameLabel = new JLabel("Bucket is:");
		JLabel bucketName = new JLabel();
		JLabel info  = new JLabel();
		JLabel paraminfo  = new JLabel("Enter Parameters: (BF-GD-LD-M)");
		JTextField parametersInput = new JTextField("");
		JTextField input = new JTextField("");
		
		//Buttons
        JButton insert = new JButton("INSERT");
        JButton search = new JButton("SEARCH");
        JButton delete = new JButton("DELETE");
		JButton submit = new JButton("SUBMIT");
		
		
		contentPane.add(heading);
		heading.setBounds(250, 40, 250, 30);
		
		contentPane.add(paraminfo);
		paraminfo.setBounds(75, 80, 250, 20);
		
		contentPane.add(parametersInput);
		parametersInput.setBounds(350, 80, 100, 20);
		
		contentPane.add(info);
		info.setBounds(150, 300, 400, 350);
		
		contentPane.add(details);
		details.setBounds(100, 120, 600, 20);
		
		contentPane.add(inputLabel);
		inputLabel.setBounds(200, 150, 375, 20);
		
		contentPane.add(input);
		input.setBounds(420, 150, 70, 20);
		
		// Submit
		contentPane.add(submit);
		submit.setBounds(475, 80, 100, 20);
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String inputString = parametersInput.getText();
				StringTokenizer s = new StringTokenizer(inputString, "-");
				if(s.countTokens() == 4) {
					blockingFactor = Integer.parseInt(s.nextToken());
					globalDepth = Integer.parseInt(s.nextToken());
					localDepth = Integer.parseInt(s.nextToken());
					m = Integer.parseInt(s.nextToken());
					details.setText("Blocking Factor = " + blockingFactor + ", Initial Global Depth = " + globalDepth + ", Initial Local Depth = " + localDepth + ", M = " + m);
					fileDirectory = new Directory(globalDepth, localDepth, blockingFactor, m);
					validInput = true;
					info.setText("");
					
				}else {
					info.setText("Please Input Parameters");
					System.out.println("Please Input Parameters");
				}		
			}
			
		});
		
		// Insert
		contentPane.add(insert);
		insert.setBounds(175, 180, 100, 30);
		insert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(validInput) {
					String inputStr = input.getText();
					input.setText("");
					System.out.println(">> Inserting key " + inputStr);
					java.util.regex.Matcher matchInput = regexPattern.matcher(inputStr);
					if(matchInput.matches()){
						errorLabel.setText("");
						int key = Integer.parseInt(inputStr);
						boolean keyPresent = fileDirectory.searchKey(key);
						if(!keyPresent){
							fileDirectory.insertKey(key);
							hashValue.setText(fileDirectory.getHashValue());
							bucketName.setText(fileDirectory.getCurrentBucket());
							info.setText(fileDirectory.guiOutput());
							System.out.println(fileDirectory);
						} else {
							info.setText("Cannot add duplicates keys !!");
							System.out.println("Cannot add duplicates keys !!");
						}
					} else {
						info.setText("Please enter a proper input !!");
						System.out.println("Please enter a proper input !!");
					}
					
				}else {
					info.setText("Please Enter a Key");
					//System.out.println("Please Enter a Key");
				}
			}
			
		});
		
		// Search
		contentPane.add(search);
		search.setBounds(300, 180, 100, 30);
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(validInput) {
					String inputStr = input.getText();
					input.setText("");
					System.out.println(">> Searching key " + inputStr);
					java.util.regex.Matcher matchInput = regexPattern.matcher(inputStr);
					if(matchInput.matches()){
						errorLabel.setText("");
						int key = Integer.parseInt(inputStr);
						boolean keyPresent = fileDirectory.searchKey(key);
						hashValue.setText(fileDirectory.getHashValue());
						if(keyPresent){
							bucketName.setText(fileDirectory.getCurrentBucket());
							info.setText("Key is present in the bucket above.");
							System.out.println("Key is present");
						} else {
							bucketName.setText("Key doesn't exist.");
							info.setText("");
							System.out.println("Key is not present");
						}
					} else {
						info.setText("Please enter a proper input !!");
						System.out.println("Please enter a proper input !!");
					}
					
				}else {
					info.setText("Please Enter a Key");
					System.out.println("Please Enter a Key");
				}
				
			}
			
		});
		
		// Delete
		contentPane.add(delete);
		delete.setBounds(425, 180, 100, 30);
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(validInput) {
					String inputStr = input.getText();
					input.setText("");
					System.out.println(">> Deleting key " + inputStr);
					java.util.regex.Matcher matchInput = regexPattern.matcher(inputStr);
					if(matchInput.matches()){
						errorLabel.setText("");
						int key = Integer.parseInt(inputStr);
						fileDirectory.deleteKey(key);
						hashValue.setText(fileDirectory.getHashValue());
						bucketName.setText(fileDirectory.getCurrentBucket());
						info.setText(fileDirectory.guiOutput());
						System.out.println(fileDirectory);
					} else {
						info.setText("Please enter a proper input !!");
						System.out.println("Please enter a proper input !!");
					}
				}else {
					info.setText("Please Enter a Key");
					System.out.println("Please Enter a Key");
				}
			}
			
		});
		
		contentPane.add(hashValueLabel);
		hashValueLabel.setBounds(150, 220, 100, 30);
        
		contentPane.add(hashValue);
		hashValue.setBounds(250, 220, 100, 30);

		contentPane.add(bucketNameLabel);
        bucketNameLabel.setBounds(400, 220, 75, 30);

        contentPane.add(bucketName);
        bucketName.setBounds(480, 220, 150, 30);
		
		
	}


}
