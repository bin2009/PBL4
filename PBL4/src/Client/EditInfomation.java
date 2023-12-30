package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
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

public class EditInfomation extends JFrame {

	private BufferedReader controlReader;
	private PrintWriter controlWriter;
	private String[] array;

	private JPanel contentPane;
	private JTextField txt_name;
	private JTextField txt_sdt;
	private JTextField txt_email;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					EditInfomation frame = new EditInfomation();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public EditInfomation(BufferedReader controlReader, PrintWriter controlWriter, String[] array) {
		this.controlReader = controlReader;
		this.controlWriter = controlWriter;
		this.array = array;
		setTitle("Edit Information");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("EDIT INFOMATION");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNewLabel.setBounds(100, 10, 247, 43);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("NAME");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(62, 79, 67, 22);
		contentPane.add(lblNewLabel_1);

		txt_name = new JTextField();
		txt_name.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_name.setBounds(127, 79, 193, 22);
		contentPane.add(txt_name);
		txt_name.setColumns(10);

		JLabel lblNewLabel_1_1 = new JLabel("SDT");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1.setBounds(62, 111, 67, 22);
		contentPane.add(lblNewLabel_1_1);

		txt_sdt = new JTextField();
		txt_sdt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_sdt.setColumns(10);
		txt_sdt.setBounds(127, 111, 193, 22);
		contentPane.add(txt_sdt);

		JLabel lblNewLabel_1_1_1 = new JLabel("EMAIL");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1_1.setBounds(62, 143, 67, 22);
		contentPane.add(lblNewLabel_1_1_1);

		txt_email = new JTextField();
		txt_email.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_email.setColumns(10);
		txt_email.setBounds(127, 143, 193, 22);
		contentPane.add(txt_email);

		JButton btn_ok = new JButton("OK");
		btn_ok.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_ok.setBounds(108, 189, 97, 31);
		contentPane.add(btn_ok);

		JButton btn_cancel = new JButton("CANCEL");

		btn_cancel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_cancel.setBounds(223, 189, 97, 31);
		contentPane.add(btn_cancel);

		txt_name.setText(array[3]);
		txt_sdt.setText(array[4]);
		txt_email.setText(array[5]);
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		btn_ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controlWriter.println("EDIT " + array[0] + "," + txt_name.getText() + "," + txt_sdt.getText() + ","
							+ txt_email.getText());
					
					String res = controlReader.readLine();
					if(res.startsWith("230")) {
						JOptionPane.showMessageDialog(null, "Update Account Sucessfully!");
						dispose();
						PersonalInformation.refresh();
					} else {
						JOptionPane.showMessageDialog(null, "Update Account Failed!");
						txt_name.setText(array[3]);
						txt_sdt.setText(array[4]);
						txt_email.setText(array[5]);
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});
	}
}
