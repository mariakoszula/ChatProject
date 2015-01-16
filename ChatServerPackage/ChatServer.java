/**
 * ChatServer is startup class for Server application which listens on specified port and accepts Clients.
 * This class is responsible for creating GUI, starting and stopping ChatServer.
 * 
 * Usage: 
 * In startServerGUI the port number, maximum clients in socket and maximum client in conversation need to be provided by the user.
 * After running the server the stopServerGUI is shown with one button which is used for stopping the ChatServer. It send to all connected clients
 * special message to close the connection.
 * 
 * @TODOs: For the ChatServer what is not implemented yet: 
 * 1) store clients nickNames to avoid the situation they are the same, 
 * 2) inform chat Clients which were not accepted to the conversation or to the socket, that they have to wait or the connection for them failed 
 * (so far is only possible to see from the server site in output)
 * 3) figure out how the backlog value in SocketServer constructor works, it did not work as expected as first, for example if it is setup to value 2 it does not reject connection
 * probably it is only maximum queue length, so it is used in the situation when the Server is overloaded, but have to confim it. 
 * 
 * @author Koszucka Maria
 * @version 1.0
 */

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
	private Integer maximumNumberOfClientsOnChat = 0;
	private Integer maximumNumberOfClientsInSocket = 5;
	private Integer portNumber = 0;
	boolean isServerRunning = false;
	private JFrame frame;
	private JPanel panel;
	private JLabel firstLabel, secondLabel, thirdLabel, fourthLabel;
	private JTextField portNumberField, maximumNumberOfClientsOnChatField, maximumNumberOfClientsInSocketField;
	private JButton stopAndStartServerButton;
	private ServerSocket serverSocket = null;
	private RunningServer runServerTask  = null;
	private Integer backlog = 2;

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
				stopAndStartServerButton.addActionListener(new StopAndStartServer());
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

	private class StopAndStartServer implements ActionListener {

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
