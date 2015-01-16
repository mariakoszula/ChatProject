package ChatServerPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RunningServer implements Runnable {
	private ServerSocket serverSocket;
	private Integer maximumNumberOfClientsOnChat;
	private Integer maximumNumberOfClientsInSocket;
	private Integer clientsInSocket = 0 ;
	private Thread clientThread;
	private ClientServing servingTask = null;
	private Socket clientSocket = null;

	public RunningServer(ServerSocket serverSocket,
			Integer maximumNumberOfClientsOnChat,
			Integer maximumNumberOfClientsInSocket) {
		this.serverSocket = serverSocket;
		this.maximumNumberOfClientsOnChat = maximumNumberOfClientsOnChat;
		this.maximumNumberOfClientsInSocket = maximumNumberOfClientsInSocket;
	}

	@Override
	public void run() {
		try {
			System.out.println("Server is running.");
			while (true) {
				if (clientsInSocket < maximumNumberOfClientsInSocket) {
					clientSocket = serverSocket.accept();
					clientsInSocket++;
					
					servingTask = new ClientServing(clientSocket,
							maximumNumberOfClientsOnChat);
					clientThread = new Thread(servingTask);
					clientThread.start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
			if(clientSocket != null)
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(clientSocket == null){
				servingTask.disconnectClient();
			}
	}

	public void sendEndConversetionMessageToClients() {
		if (servingTask != null)
			servingTask.endConnectionByServer();
	}
}