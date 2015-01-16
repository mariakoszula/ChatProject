package ChatServerPackage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatServer {
	Integer maximumNumberOfClientsOnChat = 0;
	Integer maximumNumberOfClientsInSocket = 5;
	Integer portNumber = 0;
	boolean isServerRunning = false;
	JFrame frame;
	JPanel panel;
	JLabel firstLabel, secondLabel, thirdLabel, fourthLabel;
	JTextField portNumberField, maximumNumberOfClientsOnChatField, maximumNumberOfClientsInSocketField;
	Thread clientThread;
	JButton stopAndStartServerButton;
	ServerSocket serverSocket = null;
	RunningServer runServerTask  = null;
	private Integer backlog = 100;

	public static void main(String[] args) {
				ChatServer chatServer = new ChatServer();
				chatServer.mainServerGUI();
				chatServer.startServerGUI();
	}

	public void mainServerGUI() {
				frame = new JFrame();
				panel = new JPanel();

				firstLabel = new JLabel();
				secondLabel = new JLabel();
				thirdLabel = new JLabel();
				fourthLabel = new JLabel();
		
				stopAndStartServerButton = new JButton();
				stopAndStartServerButton.addActionListener(new RunServer());
				portNumberField = new JTextField(5);
				maximumNumberOfClientsOnChatField = new JTextField(10);
				maximumNumberOfClientsInSocketField = new JTextField(10);
				
				
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(BorderLayout.CENTER, panel);
				frame.setSize(650, 650);
				frame.setVisible(true);
				
	}
	public void startServerGUI(){
		panel.removeAll();
		panel.repaint();
		panel.revalidate();
		frame.setTitle("Chat Server nie dzia³a");
		firstLabel.setText("Numer Portu [1024-65535]: ");
		secondLabel.setText("Maksymalna liczba klientów zaakceptowanych przez serwer [min. 2]: ");
		thirdLabel.setText("Maksymalna liczba klientow rozmawiajacych [min. 2]*: ");
		fourthLabel.setText("*liczba klientow na rozmawiajacych musi byc mniejsza lub rowna liczbie klientow zaakceptowanych");
		stopAndStartServerButton.setText("Uruchom Serwer");
		stopAndStartServerButton.setActionCommand("START");
		
		
		panel.add(firstLabel);
		panel.add(portNumberField);
		panel.add(secondLabel);
		panel.add(maximumNumberOfClientsInSocketField);
		panel.add(thirdLabel);
		panel.add(maximumNumberOfClientsOnChatField);
		panel.add(fourthLabel);
		panel.add(stopAndStartServerButton);
		
	}

	public void stopServerGUI() {
				panel.removeAll();
				panel.repaint();
				panel.revalidate();
				frame.setTitle("Chat Server Dzia³a");
				panel.add(stopAndStartServerButton);
				stopAndStartServerButton.setText("Zatrzymaj Serwer");
				stopAndStartServerButton.setActionCommand("STOP");
	}

	private class RunServer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String action = event.getActionCommand();

			if (action.equals("START"))
				if (!isServerRunning){
					try {
						startServer();
						if(isServerRunning){
							stopServerGUI();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			if (action.equals("STOP")){
				stopServer();
				startServerGUI();
			}
		}

		private void startServer() throws IOException {
			portNumber = Integer.parseInt(portNumberField.getText());
			maximumNumberOfClientsOnChat = Integer
					.parseInt(maximumNumberOfClientsOnChatField.getText());
			maximumNumberOfClientsInSocket = Integer.parseInt(maximumNumberOfClientsInSocketField.getText());
			if(!isServerRunning && isCorrectPort() && isCorrectMaxNumberOfClient()){
				serverSocket = new ServerSocket(portNumber, backlog);
				runServerTask = new RunningServer(serverSocket, maximumNumberOfClientsOnChat, maximumNumberOfClientsInSocket);
				Thread runServer = new Thread(runServerTask);
				runServer.start();
				isServerRunning = true;
			}
		}
		
		

		private boolean isCorrectMaxNumberOfClient() {
			if(maximumNumberOfClientsOnChat < 2 || maximumNumberOfClientsInSocket < 2 || maximumNumberOfClientsInSocket < maximumNumberOfClientsOnChat){
				JOptionPane.showMessageDialog(frame, "Niepoprawna maksymalna liczba klientow.");
				return false;
			}
			else
				return true;
		}
		private boolean isCorrectPort() {
			if (portNumber < 1024 || portNumber > 65535){
				JOptionPane.showMessageDialog(frame, "Port niepoprawny.");
				return false;
			}
			else
				return true;
		}

		private void stopServer() {
			if (serverSocket != null){
				try {
					if(runServerTask != null) runServerTask.sendEndConversetionMessageToClients();
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			System.out.println("Server was stopped.");
			isServerRunning = false;
			}
		}

	}
}
