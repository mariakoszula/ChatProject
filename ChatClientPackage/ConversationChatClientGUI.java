package ChatClientPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ConversationChatClientGUI extends JFrame {


	private JPanel contentPane;
	private JTextField messageToSend;
	private JButton btnSend;
	private JTextArea incomingMessages;


	public ConversationChatClientGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 408, 381);
		contentPane.add(scrollPane);
		
		incomingMessages = new JTextArea();
		scrollPane.setViewportView(incomingMessages);
		incomingMessages.setEditable(false);
		
		messageToSend = new JTextField();
		messageToSend.setBounds(12, 407, 279, 35);
		contentPane.add(messageToSend);
		messageToSend.setColumns(10);
		
		btnSend = new JButton("Wyœlij");
		btnSend.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				String message = messageToSend.getText();
				try {
					sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			private void sendMessage(String message) throws IOException {
				ConversationService.out.write(message);
				ConversationService.out.newLine();
				ConversationService.out.flush();
					if(message.equals(ConversationService.endConectionMessage)){
						ConversationService.isConnectionEndedByClient = true;
					}
			}
			
		});
		btnSend.setBounds(303, 407, 102, 35);
		contentPane.add(btnSend);
		
	}
	
	public JTextArea getIncomingMessagesTextArea(){
		return incomingMessages;
	}
	
}
