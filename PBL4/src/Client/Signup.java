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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class Signup extends JFrame {

	private JPanel contentPane;
	private JTextField txt_ip;
	private JTextField txt_port;
	private JLabel lblPassword;
	private JButton btn_ok;
	private JLabel lblNewLabel_1;
	private JTextField txt_user;
	private JLabel lblNewLabel_2;
	private JTextField txt_pass;

	private Login login;
	private JTextField txt_name;
	private JTextField txt_sdt;
	private JTextField txt_email;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_7;
	private JLabel lblNewLabel_8;

	public Signup() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 544, 472);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("LOCALHOST");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(71, 60, 108, 24);
		contentPane.add(lblNewLabel);

		txt_ip = new JTextField();
		txt_ip.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_ip.setText("localhost");
		txt_ip.setBounds(217, 56, 250, 33);
		contentPane.add(txt_ip);
		txt_ip.setColumns(10);

		txt_port = new JTextField();
		txt_port.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_port.setText("21");
		txt_port.setColumns(10);
		txt_port.setBounds(217, 100, 250, 33);
		contentPane.add(txt_port);

		lblPassword = new JLabel("PORT");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPassword.setBounds(71, 104, 48, 24);
		contentPane.add(lblPassword);

		btn_ok = new JButton("SIGNUP");
		btn_ok.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_ok.setBounds(238, 384, 97, 34);
		contentPane.add(btn_ok);

		lblNewLabel_1 = new JLabel("USERNAME");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(71, 283, 98, 24);
		contentPane.add(lblNewLabel_1);

		txt_user = new JTextField();
		txt_user.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_user.setText("loc");
		txt_user.setColumns(10);
		txt_user.setBounds(217, 281, 250, 36);
		contentPane.add(txt_user);

		lblNewLabel_2 = new JLabel("PASSWORD");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(71, 329, 98, 24);
		contentPane.add(lblNewLabel_2);

		txt_pass = new JTextField();
		txt_pass.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_pass.setText("123");
		txt_pass.setColumns(10);
		txt_pass.setBounds(217, 327, 250, 36);
		contentPane.add(txt_pass);

		JLabel lblNewLabel_2_1 = new JLabel("NAME");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2_1.setBounds(71, 148, 48, 24);
		contentPane.add(lblNewLabel_2_1);

		txt_name = new JTextField();
		txt_name.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_name.setColumns(10);
		txt_name.setBounds(217, 143, 250, 36);
		contentPane.add(txt_name);

		JLabel lblNewLabel_1_1 = new JLabel("SDT");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1.setBounds(71, 191, 98, 24);
		contentPane.add(lblNewLabel_1_1);

		txt_sdt = new JTextField();
		txt_sdt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_sdt.setText("loc");
		txt_sdt.setColumns(10);
		txt_sdt.setBounds(217, 189, 250, 36);
		contentPane.add(txt_sdt);

		JLabel lblNewLabel_1_2 = new JLabel("EMAIL");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2.setBounds(71, 237, 98, 24);
		contentPane.add(lblNewLabel_1_2);

		txt_email = new JTextField();
		txt_email.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_email.setText("loc");
		txt_email.setColumns(10);
		txt_email.setBounds(217, 235, 250, 36);
		contentPane.add(txt_email);
		
		JButton btn_ok_1 = new JButton("CANCEL");
		btn_ok_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_ok_1.setBounds(345, 384, 97, 34);
		contentPane.add(btn_ok_1);
		
		JLabel lblNewLabel_3 = new JLabel("SIGNUP ACCOUNT");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_3.setBounds(168, 11, 209, 34);
		contentPane.add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("*");
		lblNewLabel_4.setForeground(Color.RED);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_4.setBounds(178, 68, 18, 14);
		contentPane.add(lblNewLabel_4);
		
		lblNewLabel_5 = new JLabel("*");
		lblNewLabel_5.setForeground(Color.RED);
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_5.setBounds(129, 113, 18, 14);
		contentPane.add(lblNewLabel_5);
		
		lblNewLabel_6 = new JLabel("*");
		lblNewLabel_6.setForeground(Color.RED);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_6.setBounds(178, 292, 18, 14);
		contentPane.add(lblNewLabel_6);
		
		lblNewLabel_7 = new JLabel("*");
		lblNewLabel_7.setForeground(Color.RED);
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_7.setBounds(178, 338, 18, 14);
		contentPane.add(lblNewLabel_7);
		
		lblNewLabel_8 = new JLabel("*");
		lblNewLabel_8.setForeground(Color.RED);
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_8.setBounds(129, 157, 18, 14);
		contentPane.add(lblNewLabel_8);

		btn_ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (txt_ip.getText().isBlank() || txt_port.getText().isEmpty() || txt_name.getText().isEmpty()
							|| txt_user.getText().isEmpty() || txt_pass.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please Enter Required Fields!");
					} else {
						Socket socket = new Socket(txt_ip.getText(), Integer.parseInt(txt_port.getText()));
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
						String res = reader.readLine();
						System.out.println(res);
						String user = txt_user.getText();
						String pass = txt_pass.getText();
						String name = txt_name.getText();
						String sdt = txt_sdt.getText();
						String email = txt_email.getText();
						writer.println("SIGNUP " + name + "/" + sdt + "/" + email + "/" + user + "/" + pass);
						res = reader.readLine();
						System.out.println(res);
						if (res.equals("Register Failed!")) {
						} else if (res.equals("Sign Up Success!")) {
							writer.println("QUIT");
							socket.close();
							dispose();
						}
					}
				} catch (Exception e2) {
					// Handle exceptions by displaying an error message
				}
			}
		});

	}
}
