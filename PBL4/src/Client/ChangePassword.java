package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChangePassword extends JFrame {

	private BufferedReader controlReader;
	private PrintWriter controlWriter;
	private String[] array;

	private JPanel contentPane;
	private JTextField txt_old;
	private JPasswordField txt_new;
	private JPasswordField txt_re_new;

	public ChangePassword(BufferedReader controlReader, PrintWriter controlWriter, String[] array) {
		this.controlReader = controlReader;
		this.controlWriter = controlWriter;
		this.array = array;
		
		setTitle("Change Password");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("CHANGE PASSWORD");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(104, 10, 215, 37);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("ENTER NEW PASSWORD");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(56, 122, 299, 22);
		contentPane.add(lblNewLabel_1);

//		txt_new = new JTextField();
//		txt_new.setFont(new Font("Tahoma", Font.PLAIN, 18));
//		txt_new.setBounds(56, 154, 299, 30);
//		contentPane.add(txt_new);
//		txt_new.setColumns(10);

		JLabel lblNewLabel_1_1 = new JLabel("RE-ENTER NEW PASSWORD");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1.setBounds(56, 194, 299, 22);
		contentPane.add(lblNewLabel_1_1);

		JButton btn_change = new JButton("CHANGE");
		btn_change.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_change.setBounds(86, 273, 104, 30);
		contentPane.add(btn_change);

		JButton btn_cancel = new JButton("CANCEL");

		btn_cancel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_cancel.setBounds(220, 273, 104, 30);
		contentPane.add(btn_cancel);

		JLabel lblNewLabel_1_2 = new JLabel("OLD PASSWORD");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2.setBounds(56, 46, 299, 22);
		contentPane.add(lblNewLabel_1_2);

		txt_old = new JTextField();
		txt_old.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_old.setColumns(10);
		txt_old.setBounds(56, 78, 299, 30);
		contentPane.add(txt_old);

		txt_old.setText(array[2]);
		txt_old.setEditable(false);

		txt_new = new JPasswordField();
		txt_new.setBounds(56, 154, 299, 30);
		contentPane.add(txt_new);

		txt_re_new = new JPasswordField();
		txt_re_new.setBounds(56, 226, 299, 30);
		contentPane.add(txt_re_new);

		btn_change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (txt_new.getPassword().length == 0) {
						JOptionPane.showMessageDialog(null, "Please enter a new password!");
					} else if (txt_re_new.getPassword().length == 0) {
						JOptionPane.showMessageDialog(null, "Please confirm new password!");
					} else if (txt_new.getPassword() == txt_re_new.getPassword()) {
						JOptionPane.showMessageDialog(null, "Confirmation password does not match!");
					} else {
						controlWriter.println("CHANGE " + array[0] + "," + new String(txt_new.getPassword()));
						String resControl = controlReader.readLine();

						if (resControl.startsWith("230")) {
							JOptionPane.showMessageDialog(null, "Change password sucessfully!");
							dispose();
							PersonalInformation.refresh();
						} else {
							JOptionPane.showMessageDialog(null, "Change password failed!");
						}
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});

		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

	}
}
