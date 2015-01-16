package ChatClientPackage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class GetMessages implements Runnable{
	private Socket socket = null;
	private String endConectionMessage;
	
	public GetMessages(Socket socket, String endConectionMessage){
		this.socket = socket;
		this.endConectionMessage = endConectionMessage;
		
	}

	@Override
	public void run() {
		try(
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             ){
			String sendedMessages;
			while((sendedMessages = in.readLine())!=null){
				System.out.println(sendedMessages);
				if(sendedMessages.equalsIgnoreCase(endConectionMessage)) break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
