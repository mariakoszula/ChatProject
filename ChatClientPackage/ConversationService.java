package ChatClientPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class ConversationService implements Runnable{
	private static final String endConectionMessage = "BYE";
	
	@Override
	public void run() {
		out = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		stdIn = new BufferedReader(
				new InputStreamReader(System.in));

		Thread gettingMessages = new Thread(new GetMessages(socket,
				endConectionMessage));
		gettingMessages.start();
		
		String inputMessage;
		while ((inputMessage = stdIn.readLine()) != null) {
			out.write(inputMessage);
			out.newLine();
			out.flush();
			if (inputMessage.equalsIgnoreCase(endConectionMessage))
				break;

		}

	}

}
