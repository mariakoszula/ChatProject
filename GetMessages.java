import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class GetMessages implements Runnable{
	private Socket socket = null;
	
	public GetMessages(Socket s){
		socket = s;
	}

	@Override
	public void run() {
		try(
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             ){
			String userInput;
			while((userInput = in.readLine())!=null){
				System.out.println(userInput);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
