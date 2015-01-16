/**
 * ClientServing is responsible for receiving the messages and to send them to each connected client,
 * it count the number of fully connected clients, which means they can send and receive messages.
 * Keeps in queue the accepted by the socket clients and waits until the place in conversation will be opened for them. 
 * It defines what to do if the Chat Server is closed. 
 * 
 * @author Koszucka Maria
 * @version 1.0
 */
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
	private static final String endConversationMessage = "SpecialMessage@@BYE@@";
	private static final Integer waitForConnectionTimeMS = 5000;

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
			getMessages: while ((inputLine = in.readLine()) != null) {
				if (clientsNickName == null) {
					clientsNickName = inputLine;
					System.out.println(connectedClients
							+ "th client joined conversetion. Name: "
							+ clientsNickName);
				}
				else {
					if(inputLine.equals(endConversationMessage)){
						break getMessages;
					}
					sendToAll(clientsNickName + ">" + inputLine);
				}
			}
		}
		disconnectClient();
	}

	public void disconnectClient() {
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
			System.out.println("Someone waits for place in conversation. Retring after "+waitForConnectionTimeMS + "ms");
			try {
				Thread.sleep(waitForConnectionTimeMS);
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