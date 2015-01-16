package ChatClientPackage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ConnectToServer implements Runnable {
	private String serverIpAddress;
	private Integer portNumber;
	public static String nickName;
	public static Socket socket;
	public static BufferedWriter out = null;

	public ConnectToServer(String serverIpAddress, Integer portNumber, String nickName) {
		this.serverIpAddress = serverIpAddress;
		this.portNumber = portNumber;
		this.nickName = nickName;
	}

	@Override
	public void run() {
		try {
			socket = new Socket(serverIpAddress, portNumber);
			out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			out.write(nickName);
			out.newLine();
			out.flush();

			ConversationService startConversationService = new ConversationService();
			Thread conversation = new Thread(startConversationService);
			conversation.start();

		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Polaczenie sie nie powidlo nieznany HOST.",
					"Blad", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Polaczenie sie nie powidlo.",
					"Blad", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 

	}
}
