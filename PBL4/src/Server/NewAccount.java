package Server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class NewAccount extends JFrame {

	private static String DB_URL;
	private static String DB_USER;
	private static String DB_PASSWORD;
	private String currDirectory = "D:\\server\\FTP\\";

	private JPanel contentPane;
	private JButton btn_ok;
	private JLabel lblNewLabel_1;
	private JTextField txt_user;

	private JTextField txt_name;
	private JTextField txt_sdt;
	private JTextField txt_email;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_8;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_4;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					NewAccount frame = new NewAccount();
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
	public NewAccount(String DB_URL, String DB_USER, String DB_PASSWORD) {
		this.DB_URL = DB_URL;
		this.DB_USER = DB_USER;
		this.DB_PASSWORD = DB_PASSWORD;

		// ========================================================
		// ============ FRAME

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 558, 342);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btn_ok = new JButton("CREATE");
		btn_ok.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_ok.setBounds(248, 250, 97, 34);
		contentPane.add(btn_ok);

		lblNewLabel_1 = new JLabel("USERNAME");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(74, 195, 98, 24);
		contentPane.add(lblNewLabel_1);

		txt_user = new JTextField();
		txt_user.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_user.setColumns(10);
		txt_user.setBounds(220, 193, 250, 36);
		contentPane.add(txt_user);

		JLabel lblNewLabel_2_1 = new JLabel("NAME");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2_1.setBounds(74, 60, 48, 24);
		contentPane.add(lblNewLabel_2_1);

		txt_name = new JTextField();
		txt_name.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_name.setColumns(10);
		txt_name.setBounds(220, 55, 250, 36);
		contentPane.add(txt_name);

		JLabel lblNewLabel_1_1 = new JLabel("SDT");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1.setBounds(74, 103, 48, 24);
		contentPane.add(lblNewLabel_1_1);

		txt_sdt = new JTextField();
		txt_sdt.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_sdt.setColumns(10);
		txt_sdt.setBounds(220, 101, 250, 36);
		contentPane.add(txt_sdt);

		JLabel lblNewLabel_1_2 = new JLabel("EMAIL");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2.setBounds(74, 149, 61, 24);
		contentPane.add(lblNewLabel_1_2);

		txt_email = new JTextField();
		txt_email.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_email.setColumns(10);
		txt_email.setBounds(220, 147, 250, 36);
		contentPane.add(txt_email);

		JButton btn_ok_1 = new JButton("CANCEL");
		btn_ok_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_ok_1.setBounds(355, 250, 97, 34);
		contentPane.add(btn_ok_1);

		JLabel lblNewLabel_3 = new JLabel("CREATE ACCOUNT");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_3.setBounds(168, 11, 209, 34);
		contentPane.add(lblNewLabel_3);

		lblNewLabel_6 = new JLabel("*");
		lblNewLabel_6.setForeground(Color.RED);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_6.setBounds(181, 204, 18, 14);
		contentPane.add(lblNewLabel_6);

		lblNewLabel_8 = new JLabel("*");
		lblNewLabel_8.setForeground(Color.RED);
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_8.setBounds(142, 65, 18, 14);
		contentPane.add(lblNewLabel_8);

		lblNewLabel_2 = new JLabel("*");
		lblNewLabel_2.setForeground(Color.RED);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(142, 108, 18, 14);
		contentPane.add(lblNewLabel_2);

		lblNewLabel_4 = new JLabel("*");
		lblNewLabel_4.setForeground(Color.RED);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_4.setBounds(142, 154, 18, 14);
		contentPane.add(lblNewLabel_4);

		// =====================================================
		// ==========BUTTONN

		btn_ok_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		btn_ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newAccount();
			}
		});

	}

	// =====================================================================
	// ==================___________METHOD_________=========================
	// =====================================================================

	// =====================================================
	// ========== NEW ACCOUNT

	
	private void newAccount() {
		try {
			String name = txt_name.getText();
			String sdt = txt_sdt.getText();
			String email = txt_email.getText();
			String username = txt_user.getText();
			String password = "ftpserver";

			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			
			// kiểm tra folder đã tồn tại hay chưa
			File checkFile = new File(currDirectory + File.separator + username);
			if (checkFile.exists()) {
				JOptionPane.showMessageDialog(null, "PLEASE DELETE OR CHANGE THE PREVIOUS LINK!");
			} else {
				// nếu folder chưa tồn tại thì kiểm tra tài khoản đã tồn tại hay chưa
				String sql = "SELECT * FROM account WHERE username = ?";
				PreparedStatement pst = connection.prepareStatement(sql);
				pst.setString(1, username);
				ResultSet rs1 = pst.executeQuery();
				if(rs1.next()) {
					// tài khoản đã tồn tại
					JOptionPane.showMessageDialog(null, "Account already exists");
				} else {
					// nếu tài khoản chưa tồn tại
					// thêm tài khoản
					String sql2 = "INSERT INTO account (username, password) VALUES (?, ?)";
					PreparedStatement pst2 = connection.prepareStatement(sql2);
					pst2.setString(1, username);
					pst2.setString(2, password);
					int resultSet2 = pst2.executeUpdate();

					String sql3 = "INSERT INTO information (name, sdt, email, username) VALUES (?, ?, ?, ?)";
					PreparedStatement pst3 = connection.prepareStatement(sql3);
					pst3.setString(1, name);
					pst3.setString(2, sdt);
					pst3.setString(3, email);
					pst3.setString(4, username);

					int resultSet = pst3.executeUpdate();

					if (resultSet > 0 && resultSet2 > 0) {
						JOptionPane.showMessageDialog(null, "CREATE NEW ACCOUNT SUCCESSFUL");
						File f = new File(currDirectory + File.separator + username);
						f.mkdir();

						ServerFTP.showAccountList();
						ServerFTP.refreshTree();
						dispose();
					}
					pst2.close();
					pst3.close();
					pst.close();
					connection.close();
				}
			}
			
			
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "CREATE NEW ACCOUNT FAILED");
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
