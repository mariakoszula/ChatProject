package ChatClientPackage;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class ConversationChatClientGUI extends JFrame {

	private JPanel contentPane;
	private JTextField messageToSend;
	private JButton btnSend;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConversationChatClientGUI frame = new ConversationChatClientGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
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
		
		JTextArea incomingMessages = new JTextArea();
		scrollPane.setViewportView(incomingMessages);
		
		messageToSend = new JTextField();
		messageToSend.setBounds(12, 407, 279, 35);
		contentPane.add(messageToSend);
		messageToSend.setColumns(10);
		
		btnSend = new JButton("Wyœlij");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
			}
		});
		btnSend.setBounds(303, 407, 102, 35);
		contentPane.add(btnSend);
	}
}
