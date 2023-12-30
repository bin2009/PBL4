package Server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class AddToGroup extends JFrame {

	private static String DB_URL = "jdbc:mySQL://localhost:3306/pbl4_ftp";
	private static String DB_USER = "root";
	private static String DB_PASSWORD = "";
	private String selectCBB;

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel tableModel = new DefaultTableModel();
	private JTextField textField;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					AddToGroup frame = new AddToGroup();
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
	public AddToGroup(String selectCBB) {
		this.selectCBB = selectCBB;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 783, 523);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 81, 693, 342);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		JButton btnNewButton = new JButton("OK");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.setBounds(432, 445, 110, 31);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("CANCEL");
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton_1.setBounds(552, 445, 110, 31);
		contentPane.add(btnNewButton_1);

		JLabel lblNewLabel = new JLabel("ADD USER TO GROUP");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(36, 10, 407, 33);
		contentPane.add(lblNewLabel);

		// ==========================================
		// ========= BUTTONN

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = table.getSelectedRows();
				if (selectedRows.length > 0) {
					// Yêu cầu xác nhận trước khi xóa
					int confirmation = JOptionPane.showConfirmDialog(null, "CONFIRM ADDITIONAL EMPLOYEES ABOVE?", "YES",
							JOptionPane.YES_NO_OPTION);
					// Kiểm tra xem người dùng đã xác nhận xóa hay chưa
					if (confirmation == JOptionPane.YES_OPTION) {
						for (int i = selectedRows.length - 1; i >= 0; i--) {
							int selectedRow = selectedRows[i];
							String username = (String) table.getValueAt(selectedRow, 4);
							int id = Integer.parseInt((String) table.getValueAt(selectedRow, 0));
							addToGroup(id);
						}
					}
					list();
					ServerFTP.viewCBB(selectCBB);
				}
			}
		});

		// ==========================================
		// ========= TABLEE

		tableModel.addColumn("IDNV");
		tableModel.addColumn("NAME");
		tableModel.addColumn("SDT");
		tableModel.addColumn("Email");
		tableModel.addColumn("Username");

		table = new JTable(tableModel);

		scrollPane.setViewportView(table);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textField.setBounds(422, 45, 307, 26);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("ENTER NAME");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(291, 45, 121, 26);
		contentPane.add(lblNewLabel_1);

		// ==========================================
		// ========= TEXT CHANGE

		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				// Đã có sự thay đổi trong văn bản (bao gồm khi thêm ký tự)
				System.out.println("Text changed: " + textField.getText());
				while (tableModel.getRowCount() > 0) {
					tableModel.removeRow(0);
				}
				search(textField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// Đã xóa ký tự hoặc xóa hết văn bản
				if (textField.getText().isEmpty()) {
					while (tableModel.getRowCount() > 0) {
						tableModel.removeRow(0);
					}
//					DanhsachSV();
					list();
					System.out.println("Text cleared");
				} else {
					System.out.println("Text changed: " + textField.getText());
					while (tableModel.getRowCount() > 0) {
						tableModel.removeRow(0);
					}
					search(textField.getText());
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// Thay đổi không ảnh hưởng đến văn bản hiển thị (ít phổ biến)
			}
		});

		list();
	}

	public void search(String search) {
		try {
			if (tableModel.getRowCount() > 0) {
				tableModel.setRowCount(0);
			}
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			// lấy ra idgroup muốn thêm
			String sql2 = "SELECT idgroup FROM groupofcompany WHERE namegroup = ?";
			PreparedStatement pst2 = connection.prepareStatement(sql2);
			pst2.setString(1, selectCBB);
			ResultSet resultSet2 = pst2.executeQuery();
			if (resultSet2.next()) {
				int idgroup = resultSet2.getInt("idgroup");
				String sql = "SELECT * FROM information ifor "
						+ "WHERE ifor.name LIKE ?" 
						+ "AND NOT EXISTS ("
						+ "    SELECT 1 FROM employeegroup eg " 
						+ "    WHERE eg.iduser = ifor.iduser AND eg.idgroup = ?"
						+ ")";

				PreparedStatement pst = connection.prepareStatement(sql);
				pst.setString(1, "%" + search + "%");
				pst.setInt(2, idgroup);
				ResultSet resultSet = pst.executeQuery();
				while (resultSet.next()) {
					String iduser = resultSet.getString("iduser");
					String name = resultSet.getString("name");
					String sdt = resultSet.getString("sdt");
					String email = resultSet.getString("email");
					String username = resultSet.getString("username");
					tableModel.addRow(new Object[] { iduser, name, sdt, email, username });
				}
				resultSet.close();
				pst.close();
			}
			pst2.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception or handle it appropriately
		}
	}

	public void list() {
		try {
			if (tableModel.getRowCount() > 0) {
				tableModel.setRowCount(0);
			}
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			// lấy ra idgroup muốn thêm
			String sql2 = "SELECT idgroup FROM groupofcompany WHERE namegroup = ?";
			PreparedStatement pst2 = connection.prepareStatement(sql2);
			pst2.setString(1, selectCBB);
			ResultSet resultSet2 = pst2.executeQuery();
			if (resultSet2.next()) {
				int idgroup = resultSet2.getInt("idgroup");
				String sql = "SELECT * FROM information ifor " + "WHERE NOT EXISTS (" + "SELECT 1 "
						+ "FROM employeegroup eg " + "WHERE eg.iduser = ifor.iduser " + "AND eg.idgroup = ?)";
				PreparedStatement pst = connection.prepareStatement(sql);
				pst.setInt(1, idgroup);
				ResultSet resultSet = pst.executeQuery();
				while (resultSet.next()) {
					String iduser = resultSet.getString("iduser");
					String name = resultSet.getString("name");
					String sdt = resultSet.getString("sdt");
					String email = resultSet.getString("email");
					String username = resultSet.getString("username");
					tableModel.addRow(new Object[] { iduser, name, sdt, email, username });
				}
				resultSet.close();
				pst.close();
			}
			pst2.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception or handle it appropriately
		}
	}

	public void addToGroup(int id) {
		try {
//			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//			String sql2 = "SELECT idgroup FROM groupofcompany WHERE namegroup = ?";
//			PreparedStatement pst2 = connection.prepareStatement(sql2);
//			pst2.setString(1, selectCBB);
//			ResultSet resultSet2 = pst2.executeQuery();
//			if (resultSet2.next()) {
//				int idgroup = resultSet2.getInt("idgroup");
//				String sql = "UPDATE account SET idgroup = ? WHERE username = ?";
//				PreparedStatement pst = connection.prepareStatement(sql);
//				pst.setInt(1, idgroup);
//				pst.setString(2, username);
//				int resultSet = pst.executeUpdate();
//				pst.close();
//			}
//			pst2.close();
//			connection.close();

			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String sql2 = "SELECT idgroup FROM groupofcompany WHERE namegroup = ?";
			PreparedStatement pst2 = connection.prepareStatement(sql2);
			pst2.setString(1, selectCBB);
			ResultSet resultSet2 = pst2.executeQuery();
			if (resultSet2.next()) {
				int idgroup = resultSet2.getInt("idgroup");
				String sql = "INSERT INTO employeegroup (iduser, idgroup) VALUES (?, ?)";
				PreparedStatement pst = connection.prepareStatement(sql);
				pst.setInt(1, id);
				pst.setInt(2, idgroup);
				int resultSet = pst.executeUpdate();
				pst.close();
			}
			pst2.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception or handle it appropriately
		}
	}
}
