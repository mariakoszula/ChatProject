package ChatServerPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RunningServer implements Runnable {
	private ServerSocket serverSocket;
	private Integer maximumNumberOfClientsOnChat;
	private Integer maximumNumberOfClientsInSocket;
	private Thread clientThread;
	private ClientServing servingTask = null;
	public RunningServer(ServerSocket serverSocket,
			Integer maximumNumberOfClientsOnChat, Integer maximumNumberOfClientsInSocket) {
		this.serverSocket = serverSocket;
		this.maximumNumberOfClientsOnChat = maximumNumberOfClientsOnChat;
		this.maximumNumberOfClientsInSocket = maximumNumberOfClientsInSocket;
	}

	@Override
	public void run() {
			try {

				System.out.println("Server is running.");
				while (true) {
					if(servingTask == null || servingTask.getconnectedClients() < maximumNumberOfClientsInSocket){
						Socket clientSocket = serverSocket.accept();
						servingTask = new ClientServing(clientSocket, maximumNumberOfClientsOnChat);
						clientThread = new Thread(servingTask);
						clientThread.start();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
			

	}
	public void sendEndConversetionMessageToClients(){
		if(servingTask != null) servingTask.endConnectionByServer();
	}
}
