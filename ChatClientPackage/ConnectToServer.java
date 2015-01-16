package ChatClientPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectToServer implements Runnable {
	private String serverIpAddress;
	private Integer portNumber;
	private BufferedWriter out;
	private BufferedReader stdIn;
	private Socket socket;
	
	public ConnectToServer(String serverIpAddress, Integer portNumber) {
		this.serverIpAddress = serverIpAddress;
		this.portNumber = portNumber;
	}

	@Override
	public void run() {
		try{
			socket = new Socket(serverIpAddress, portNumber);
			out = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			stdIn = new BufferedReader(
					new InputStreamReader(System.in));

			
			String inputMessage;
			if ((inputMessage = stdIn.readLine()) != null) {
				out.write(inputMessage);
				out.newLine();
				out.flush();
			}
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
