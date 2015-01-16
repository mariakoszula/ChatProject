/**
 * ConversationService is responsible for running GUI for chat - ConversationChatClientGUI and receiving messages from client.
 * One special message is defined to disconnect the Client from the Server when the client closes the connection or when the Server closes its connection.
 * 
 * @author Koszucka Maria
 * @version 1.0
 */


package ChatClientPackage;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class ConversationService implements Runnable {
	public static boolean isConnectionEnded = false;
	private static final String endConnnectionMessageFromServer = "SpecialMessage@@BYE@@";

	@Override
	public void run() {

		try {
			ConversationChatClientGUI frame = new ConversationChatClientGUI();
			frame.setTitle("Witaj " + ConnectToServer.nickName);
			frame.setVisible(true);

			frame.addWindowListener(new java.awt.event.WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					try {
						ConnectToServer.out
								.write(endConnnectionMessageFromServer);
						ConnectToServer.out.newLine();
						ConnectToServer.out.flush();
						isConnectionEnded = true;
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					super.windowClosing(e);
				}

			});

			try (BufferedReader in = new BufferedReader(new InputStreamReader(
					ConnectToServer.socket.getInputStream()));) {
				String sendedMessages = null;
				while (!isConnectionEnded) {
					if (sendedMessages != null
							&& sendedMessages
									.equals(endConnnectionMessageFromServer)) {
						isConnectionEnded = true;
						frame.dispose();
						JOptionPane.showMessageDialog(null,
								"Polaczenie zakonczone przez Serwer.",
								"Blad Serwera", JOptionPane.ERROR_MESSAGE);
					}
					sendedMessages = in.readLine();
					frame.getIncomingMessagesTextArea().append(
							sendedMessages + "\n");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ConnectToServer.out != null) {
				try {
					ConnectToServer.out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ConnectToServer.socket != null) {
				try {
					ConnectToServer.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
