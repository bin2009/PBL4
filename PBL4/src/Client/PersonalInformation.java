package Client;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class PersonalInformation extends JFrame {
	
	private static BufferedReader controlReader;
	private static PrintWriter controlWriter;
	private static String[] array;

	private JPanel contentPane;
	private static JTextField txt_name;
	private static JTextField txt_sdt;
	private static JTextField txt_email;
	private static JTextField txt_user;
	private static JTextField txt_pass;
	private static JPasswordField txt_password;
	
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					PersonalInformation frame = new PersonalInformation( controlReader,  controlWriter, array);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}


	public PersonalInformation(BufferedReader controlReader, PrintWriter controlWriter) {
		this.controlReader = controlReader;
		this.controlWriter = controlWriter;
		
		try {
			controlWriter.println("INFO");
			String resControl = controlReader.readLine();
			array = resControl.split(",");
			System.out.println(resControl);
			for (String string : array) {
				System.out.println("test: " + string);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		setTitle("Personal Information");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 389);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(PersonalInformation.class.getResource("icon_per.png"))));
		lblNewLabel.setBounds(33, 10, 160, 174);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("NAME");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(198, 34, 105, 28);
		contentPane.add(lblNewLabel_1);
		
		txt_name = new JTextField();
		txt_name.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_name.setBounds(303, 34, 186, 28);
		contentPane.add(txt_name);
		txt_name.setColumns(10);
		
		txt_sdt = new JTextField();
		txt_sdt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_sdt.setColumns(10);
		txt_sdt.setBounds(303, 84, 186, 28);
		contentPane.add(txt_sdt);
		
		JLabel lblNewLabel_1_1 = new JLabel("SDT");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1.setBounds(198, 84, 105, 28);
		contentPane.add(lblNewLabel_1_1);
		
		txt_email = new JTextField();
		txt_email.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_email.setColumns(10);
		txt_email.setBounds(303, 142, 186, 28);
		contentPane.add(txt_email);
		
		JLabel lblNewLabel_1_2 = new JLabel("EMAIL");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2.setBounds(198, 142, 105, 28);
		contentPane.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("USERNAME");
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2_1.setBounds(198, 194, 105, 28);
		contentPane.add(lblNewLabel_1_2_1);
		
		txt_user = new JTextField();
		txt_user.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_user.setColumns(10);
		txt_user.setBounds(303, 194, 186, 28);
		contentPane.add(txt_user);
		
		JLabel lblNewLabel_1_2_2 = new JLabel("PASSWORD");
		lblNewLabel_1_2_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2_2.setBounds(198, 249, 105, 28);
		contentPane.add(lblNewLabel_1_2_2);
		
		txt_pass = new JTextField();
		txt_pass.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_pass.setColumns(10);
		txt_pass.setBounds(303, 287, 186, 28);
		contentPane.add(txt_pass);
		txt_pass.hide();
		
		txt_password = new JPasswordField();
		txt_password.setBounds(303, 249, 186, 28);
		contentPane.add(txt_password);
		
		JButton btn_change = new JButton("CHANGE");

		btn_change.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_change.setBounds(33, 217, 126, 28);
		contentPane.add(btn_change);
		
		JButton btn_edit = new JButton("EDIT");
		btn_edit.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_edit.setBounds(33, 182, 126, 28);
		contentPane.add(btn_edit);
		
		// Thêm icon
		URL url_iconPer = PersonalInformation.class.getResource("icon_person.png");
		Image img = Toolkit.getDefaultToolkit().createImage(url_iconPer);
		
		this.setIconImage(img);
		
		txt_name.disable();
		txt_sdt.disable();
		txt_email.disable();
		txt_user.disable();
		txt_pass.disable();
		txt_password.disable();
		
		txt_name.setText(array[3]);
		txt_sdt.setText(array[4]);
		txt_email.setText(array[5]);
		txt_user.setText(array[1]);
		txt_pass.setText(array[2]);
		txt_password.setText(array[2]);
		
		JButton btn_cancel = new JButton("CANCEL");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btn_cancel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_cancel.setBounds(33, 250, 126, 28);
		contentPane.add(btn_cancel);
		
		
		
		
		btn_edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditInfomation edit = new EditInfomation(controlReader, controlWriter, array);
				edit.setVisible(true);
			}
		});
		
		
		btn_change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangePassword change = new ChangePassword(controlReader, controlWriter, array);
				change.setVisible(true);
			}
		});
		
	}
	
	
	public static void refresh() {
		try {
			controlWriter.println("INFO");
			String resControl = controlReader.readLine();
			array = resControl.split(",");
			System.out.println(resControl);
			for (String string : array) {
				System.out.println("test: " + string);
			}
			txt_name.setText(array[3]);
			txt_sdt.setText(array[4]);
			txt_email.setText(array[5]);
			txt_user.setText(array[1]);
			txt_pass.setText(array[2]);
			txt_password.setText(array[2]);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
