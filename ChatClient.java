import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
	public static void main(String[] args) throws UnknownHostException, IOException{
		
		try(
				Socket socket = new Socket("127.0.0.1", 1111);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				BufferedReader stdIn = new BufferedReader(
		                    new InputStreamReader(System.in));){
						
			Thread gettingMessages = new Thread(new GetMessages(socket));
			gettingMessages.start();
			
				String inputMessage;
				while((inputMessage = stdIn.readLine())!=null){
						out.write(inputMessage);
						out.newLine();
						out.flush();
				}
		}
	}	
		
}
