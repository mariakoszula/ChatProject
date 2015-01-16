package ChatClientPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

public class ConversationService implements Runnable {
	public static final String endConectionMessage = "BYE";
	public static boolean isConnectionEndedByClient = false;
	public static BufferedWriter out = null;

	@Override
	public void run() {

		try {
			out = new BufferedWriter(new OutputStreamWriter(
					ConnectToServer.socket.getOutputStream()));
			ConversationChatClientGUI frame = new ConversationChatClientGUI();
			frame.setVisible(true);

			try (BufferedReader in = new BufferedReader(new InputStreamReader(
					ConnectToServer.socket.getInputStream()));) {
				String sendedMessages;
				while ((sendedMessages = in.readLine()) != null) {
					frame.getIncomingMessagesTextArea().append(
							sendedMessages + "\n");
					if (sendedMessages.equalsIgnoreCase(endConectionMessage)
							|| isConnectionEndedByClient)
						break;
				}
			}

			if (isConnectionEndedByClient)
				frame.dispose();

			JOptionPane.showMessageDialog(null, "Rozmowa zakonczona.",
					"Koniec", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
