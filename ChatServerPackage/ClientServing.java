package ChatServerPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientServing implements Runnable {
	private Socket socket = null;
	private String clientsNickName = null;
	private Integer maximumNumberOfClients = 0;
	private static Integer connectedClients = 0;
	private static ArrayList<BufferedWriter> outAll = new ArrayList<BufferedWriter>();
	private BufferedWriter out;
	private static final String endConversationMessage = "BYE";

	public ClientServing(Socket socket, Integer maximumNumberOfClients) {
		this.socket = socket;
		this.maximumNumberOfClients = maximumNumberOfClients;
		System.out.println("New client joined socket.");
	}

	@Override
	public void run() {
		try {
			connectClient();
			out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			outAll.add(out);
			startChatting();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startChatting() throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));) {
			String inputLine;
			readMessageFromClient: while ((inputLine = in.readLine()) != null) {
				if (clientsNickName == null) {
					clientsNickName = inputLine;
					System.out.println(connectedClients
							+ "th client joined conversetion. Name: "
							+ clientsNickName);
				}
				if (clientsNickName != null) {
					if (inputLine.equalsIgnoreCase(endConversationMessage))
						break readMessageFromClient;
					System.out.println("Message from client " + clientsNickName
							+ ":" + inputLine);
					sendToAll(clientsNickName + ">" + inputLine);
				}
			}
		}
		disconnectClient();
	}

	private void disconnectClient() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		outAll.remove(out);
		removeClient();
		System.out.println(clientsNickName + " left conversetion. "
				+ connectedClients + "th connected.");
	}

	private void connectClient() {
		while (connectedClients >= maximumNumberOfClients) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("New client joined conversation.");
		addClient();
	}

	private void sendToAll(String message) throws IOException {
		Iterator<BufferedWriter> iterator = outAll.iterator();
		while (iterator.hasNext()) {
			BufferedWriter tmpOut = iterator.next();
			tmpOut.write(message);
			tmpOut.newLine();
			tmpOut.flush();
		}
	}

	public void addClient() {
		connectedClients++;
	}

	public void removeClient() {
		connectedClients--;
	}

	public Integer getconnectedClients() {
		return connectedClients;
	}

	public void endConnectionByServer() {
		try {
			sendToAll(endConversationMessage);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			disconnectClient();
		}
	}
}