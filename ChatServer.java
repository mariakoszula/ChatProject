import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatServer {
	Integer maximumNumberOfClients = 0;
	Integer portNumber = 0;
	boolean isServerRunning = false;
	JFrame frame;
	JPanel panel;
	JLabel firstLabel, secondLabel;
	JTextField portNumberField, maximumNumberOfClientsField;
	Thread clientThread;
	JButton stopAndStartServerButton;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ChatServer chatServer = new ChatServer();
				chatServer.startServerGUI();
			}
		});

	}

	public void startServerGUI() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame = new JFrame("Chat Server nie dzia³a");
				panel = new JPanel();

				firstLabel = new JLabel("Numer Portu [1024-65535]: ");
				secondLabel = new JLabel(
						"Maksymalna liczba klientów [min. 2]: ");

				portNumberField = new JTextField(5);
				maximumNumberOfClientsField = new JTextField(10);
				stopAndStartServerButton = new JButton("Uruchom Serwer");
				stopAndStartServerButton.setActionCommand("START");

				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				panel.add(firstLabel);
				panel.add(portNumberField);
				panel.add(secondLabel);
				panel.add(maximumNumberOfClientsField);
				panel.add(BorderLayout.CENTER, stopAndStartServerButton);

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(BorderLayout.CENTER, panel);
				frame.setSize(350, 350);
				frame.setVisible(true);
				stopAndStartServerButton.addActionListener(new RunServer());

			}

		});
	}

	public void stopServerGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setTitle("Chat Server Dzia³a");
				stopAndStartServerButton.setText("Zatrzymaj Serwer");
				stopAndStartServerButton.setActionCommand("STOP");
			}
		});

	}

	public class RunServer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			String action = event.getActionCommand();

			if (action.equals("START"))
				if (!isServerRunning)
					try {
						startServer();
						stopServerGUI();
					} catch (IOException e) {
						e.printStackTrace();
					}
			if (action.equals("STOP"))
				stopServer();
			startServerGUI();
		}

		private void startServer() throws IOException {
			portNumber = Integer.parseInt(portNumberField.getText());
			maximumNumberOfClients = Integer
					.parseInt(maximumNumberOfClientsField.getText());
			isServerRunning = true;
			try (ServerSocket serverSocket = new ServerSocket(portNumber, maximumNumberOfClients);) {
				System.out.println("Server is running.");
				while (isServerRunning) {
					Socket clientSocket = serverSocket.accept();
					clientThread = new Thread(new ClientServing(clientSocket,
							maximumNumberOfClients));
					clientThread.start();
				}
			}

		}

		public void stopServer() {
			Thread.interrupted();
			System.out.println("Server was stopped.");
			isServerRunning = false;
		}

	}
}
