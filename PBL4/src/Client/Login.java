package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JPasswordField;

public class Login extends JFrame {

	// Control
	private static Socket socket;
	private BufferedReader controlReader;
	private PrintWriter controlWriter;

	private JPanel contentPane;
	private JTextField txt_ip;
	private JLabel lblPort;
	private JTextField txt_controlPort;
	private JLabel lblUsername;
	private JTextField txt_user;
	private JLabel lblPassword;
	private JTextField txt_pass;
	private JPasswordField txt_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("FTP CLIENT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 346, 302);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("IP");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(43, 27, 62, 13);
		contentPane.add(lblNewLabel);

		txt_ip = new JTextField();
		txt_ip.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_ip.setText("localhost");
		txt_ip.setBounds(162, 19, 131, 28);
		contentPane.add(txt_ip);
		txt_ip.setColumns(10);

		lblPort = new JLabel("PORT");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPort.setBounds(43, 76, 62, 13);
		contentPane.add(lblPort);

		txt_controlPort = new JTextField();
		txt_controlPort.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_controlPort.setText("21");
		txt_controlPort.setColumns(10);
		txt_controlPort.setBounds(162, 68, 131, 28);
		contentPane.add(txt_controlPort);

		lblUsername = new JLabel("USERNAME");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsername.setBounds(43, 124, 109, 13);
		contentPane.add(lblUsername);

		txt_user = new JTextField();
		txt_user.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_user.setText("loc");
		txt_user.setColumns(10);
		txt_user.setBounds(162, 116, 131, 28);
		contentPane.add(txt_user);

		lblPassword = new JLabel("PASSWORD");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPassword.setBounds(43, 171, 109, 13);
		contentPane.add(lblPassword);

		txt_pass = new JTextField();
		txt_pass.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_pass.setText("ftpserver");
		txt_pass.setColumns(10);
		txt_pass.setBounds(151, 236, 131, 19);
		contentPane.add(txt_pass);
		txt_pass.hide();

		JButton btn_login = new JButton("LOGIN");
		btn_login.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_login.setBounds(168, 217, 102, 21);
		contentPane.add(btn_login);
		
		JButton btn_cancel = new JButton("CLOSE");
		
		btn_cancel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_cancel.setBounds(62, 217, 96, 21);
		contentPane.add(btn_cancel);
		
		txt_password = new JPasswordField();
		txt_password.setBounds(162, 167, 131, 28);
		contentPane.add(txt_password);

		// -------------------------button---------------------------------
		
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (txt_ip.getText().isBlank() || txt_controlPort.getText().isEmpty()
							|| txt_user.getText().isEmpty() || txt_password.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please Enter Enough Information!");
					}
//					if (txt_ip.getText().isEmpty()) {
//						JOptionPane.showMessageDialog(null, "Nhập Địa Chỉ Máy Chủ Kết Nối!");
//					} else if (txt_controlPort.getText().isEmpty()) {
//						JOptionPane.showMessageDialog(null, "Nhập Port Kết Nối!");
//					} else if (txt_user.getText().isEmpty()) {
//						JOptionPane.showMessageDialog(null, "Nhập Username!");
//					} else if (txt_pass.getText().isEmpty()) {
//						JOptionPane.showMessageDialog(null, "Nhập Password!");
//					}
					else {
						String IP = txt_ip.getText();
						int controlPort = Integer.parseInt(txt_controlPort.getText());
						if (testConnection(IP, controlPort, 5000)) {
//							socket = new Socket(IP, controlPort);

							// khởi tạo điều khiển control
							controlReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							controlWriter = new PrintWriter(socket.getOutputStream(), true);
							String respone = null;

							// started
							respone = controlReader.readLine();
							System.out.println(respone);

							// gủi UTF8
							controlWriter.println("OTPS UTF8 ON");
							respone = controlReader.readLine();
							System.out.println(respone);

							// gửi username
							controlWriter.println("USER " + txt_user.getText());
							respone = controlReader.readLine();
							System.out.println(respone);
							if (respone.startsWith("530")) {
								JOptionPane.showMessageDialog(null, respone);
//								controlWriter.print("QUIT");
//								String res = controlReader.readLine();
								socket.close();
							} else {
								// gửi passworrd
								controlWriter.println("PASS " + txt_password.getText());
								respone = controlReader.readLine();
								System.out.println(respone);

								if (respone.startsWith("230")) {

									respone = controlReader.readLine();
									System.out.println(respone);

									Home home = new Home(socket, controlReader, controlWriter, txt_user.getText());
									home.setVisible(true);
									setVisible(false);
								} else {
									JOptionPane.showMessageDialog(null, respone);
								}

							}
						}

					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
	}

	// kiểm tra kết nối đến server
	public static boolean testConnection(String host, int port, int timeout) {
        try {
        	socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

	// Hàm chờ phản hồi với thời gian chờ
	private String waitForResponse(BufferedReader reader) throws IOException {
		long startTime = System.currentTimeMillis();
		long timeout = 5000; // 5 seconds timeout, you can adjust this value

		while (!reader.ready()) {
			if (System.currentTimeMillis() - startTime > timeout) {
				throw new IOException("Timeout waiting for server response");
			}
		}

		return reader.readLine();
	}
}
