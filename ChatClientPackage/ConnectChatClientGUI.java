package ChatClientPackage;

import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConnectChatClientGUI extends JFrame {
	private JPanel connectPane;
	private JTextField ipServerTextField;
	private JTextField portNuberTextField;
	private JTextField nickNameTextField;
	private String serverIpAddress, nickName;
	private Integer portNumber;
	private static ConnectChatClientGUI frame;
	private static final String regexIP = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ConnectChatClientGUI();
					frame.setTitle("£¹czenie z Serwerem");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ConnectChatClientGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		connectPane = new JPanel();
		connectPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(connectPane);
		connectPane.setLayout(null);
		JLabel lblIpSerwera = new JLabel("IP Serwera [np. 127.0.0.1]:");
		lblIpSerwera.setBounds(12, 37, 159, 16);
		connectPane.add(lblIpSerwera);
		ipServerTextField = new JTextField();
		ipServerTextField.setBounds(182, 34, 188, 22);
		connectPane.add(ipServerTextField);
		ipServerTextField.setColumns(10);
		JLabel lblPort = new JLabel("Port [1024-65535]:");
		lblPort.setBounds(56, 91, 115, 16);
		connectPane.add(lblPort);
		portNuberTextField = new JTextField();
		portNuberTextField.setBounds(182, 88, 188, 22);
		portNuberTextField.setColumns(10);
		connectPane.add(portNuberTextField);
		JLabel lblNick = new JLabel("Nick:");
		lblNick.setBounds(115, 151, 56, 16);
		connectPane.add(lblNick);
		nickNameTextField = new JTextField();
		nickNameTextField.setBounds(182, 148, 188, 22);
		connectPane.add(nickNameTextField);
		nickNameTextField.setColumns(10);
		JButton connectBtn = new JButton("Po\u0142\u0105cz");
		connectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				portNumber = Integer.parseInt(portNuberTextField.getText());
				serverIpAddress = ipServerTextField.getText();
				nickName = nickNameTextField.getText();
				if (!isIpAddressCorrect()) {
					showGivenValuesNotCorrectBox(serverIpAddress);
					System.out.println("IP not correct");
				} else if (!isPortCorrect()) {
					showGivenValuesNotCorrectBox(String.valueOf(portNumber));
					System.out.println("Port not correct");
				} else if (!isNickCorrect()) {
					showGivenValuesNotCorrectBox(nickName);
				} else {
					ConnectToServer connectToServerTask = new ConnectToServer(
							serverIpAddress, portNumber, nickName);
					Thread connectToServer = new Thread(connectToServerTask);
					frame.dispose();
					connectToServer.start();
				}
			}

			private void showGivenValuesNotCorrectBox(String wrongValue) {
				String message = "Podana warto\u015b\u0107: " + wrongValue
						+ " jest niepoprawna. Spróbuj jeszcze raz.";
				JOptionPane.showMessageDialog(null, message,
						"Niepoprawna wartosc", JOptionPane.WARNING_MESSAGE);
			}

			private boolean isPortCorrect() {
				if (portNumber < 1024 || portNumber > 65535)
					return false;
				else
					return true;
			}

			private boolean isIpAddressCorrect() {
				if (serverIpAddress.matches(regexIP))
					return true;
				else
					return false;
			}

			private boolean isNickCorrect() {
				if (nickName == null)
					return false;
				else
					return true;
			}
		});
		connectBtn.setBounds(273, 194, 97, 25);
		connectPane.add(connectBtn);
	}
}