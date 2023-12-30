package Server;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.NonWritableChannelException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableModel;
import javax.swing.JComboBox;

public class ServerFTP extends JFrame {

	private static final String DB_URL = "jdbc:mySQL://localhost:3306/pbl4_ftp";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	private XuLy x;
	private Thread thread;

	private static Map<String, String> account = new HashMap<>();

//	private int controlPort = 21;
	private int controlPort;
	private ServerSocket welcomeSocket;
	boolean serverRunning = true;

	private static DefaultTableModel tableModel;
	private static DefaultTableModel tableModel_1;
	private static DefaultTableModel tableModel_2;

	private String checkUser;

	private boolean listUser = false;

	private String selectCBB;
	private String selectTree;

	private static final long serialVersionUID = 1L;
	private static JTextArea textArea;
	private ArrayList<String> listUserConnect = new ArrayList<String>();
	private List<String> listConnectedUsers = new List<String>() {
		@Override
		public <T> T[] toArray(T[] a) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] toArray() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<String> subList(int fromIndex, int toIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String set(int index, String element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String remove(int index) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean remove(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public ListIterator<String> listIterator(int index) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ListIterator<String> listIterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int lastIndexOf(Object o) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Iterator<String> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int indexOf(Object o) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String get(int index) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean contains(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean addAll(int index, Collection<? extends String> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends String> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void add(int index, String element) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean add(String e) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	private static JTable table;
	private JTable table_1;
	private static JTable table_2;

	private static DefaultMutableTreeNode root;
	private static String currDirectory = "D:\\server\\FTP\\";
	private JTextField txt_port;
	private static JComboBox comboBox;
	private static JTree tree;

	private Thread starThread;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			// Chọn Look and Feel của Windows
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

			// Gọi updateUI để cập nhật giao diện người dùng hiện tại
			SwingUtilities.updateComponentTreeUI(SwingUtilities.getRootPane(new JFrame()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFTP server = new ServerFTP();
					server.setVisible(true);
//					new ServerWorker(server).execute();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerFTP() {
		getContentPane().setLayout(null);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height - 40);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setTitle("FTP SERVER");

		Font titleFont = new Font("Arial", Font.BOLD, 16);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 96, 501, 183);
		getContentPane().add(scrollPane);

		textArea = new JTextArea(1, 1);
		scrollPane.setViewportView(textArea);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(521, 10, 991, 269);
		getContentPane().add(scrollPane_1);

		table = new JTable();

		table_1 = new JTable();
//		scrollPane_2.setViewportView(table_1);

		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 501, 76);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("PORT");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(21, 24, 73, 35);
		panel.add(lblNewLabel);

		txt_port = new JTextField();
		txt_port.setFont(new Font("Arial", Font.PLAIN, 18));
		txt_port.setBounds(78, 27, 184, 30);
		panel.add(txt_port);
		txt_port.setColumns(10);

		JButton btn_start = new JButton("START");
		btn_start.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btn_start.setBounds(272, 26, 101, 31);
		panel.add(btn_start);

		JButton btnStop = new JButton("STOP");
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnStop.setBounds(383, 27, 101, 31);
		btnStop.setEnabled(false);
		panel.add(btnStop);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(521, 289, 991, 466);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(10, 62, 971, 394);
		panel_1.add(scrollPane_4);

		table_2 = new JTable((TableModel) null);

		comboBox = new JComboBox();
		comboBox.setBounds(640, 25, 341, 26);
		panel_1.add(comboBox);

		JLabel lblNewLabel_1 = new JLabel("GROUP");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(532, 26, 78, 26);
		panel_1.add(lblNewLabel_1);

		// ===================================================
		// ========== MENU MOUSEE

		JMenuItem item_new = new JMenuItem("NEW ACCOUNT");
		JPopupMenu menu_head_1 = new JPopupMenu();
		menu_head_1.add(item_new);

		JMenuItem item_delete_account = new JMenuItem("DELETE");
		JMenuItem item_reset_pass = new JMenuItem("RESET PASSWORD");
		JPopupMenu menu_account = new JPopupMenu();
		menu_account.add(item_delete_account);
		menu_account.add(item_reset_pass);

		JMenuItem item_new_group = new JMenuItem("NEW GROUP");
		JPopupMenu menu_cbb = new JPopupMenu();
		menu_cbb.add(item_new_group);

		JMenuItem item_add_to_group = new JMenuItem("ADD");
		JMenuItem item_delete_group = new JMenuItem("DELETE GROUP");
		JPopupMenu menu_group = new JPopupMenu();
		menu_group.add(item_add_to_group);
		menu_group.add(item_delete_group);

		JMenuItem item_delete_from_group = new JMenuItem("DELETE");
		JPopupMenu menu_dele = new JPopupMenu();
		menu_dele.add(item_delete_from_group);

		JMenuItem item_rename_tree = new JMenuItem("RENAME");
		JMenuItem item_delete_tree = new JMenuItem("DELETE");
		JPopupMenu menu_tree = new JPopupMenu();
		menu_tree.add(item_rename_tree);
		menu_tree.add(item_delete_tree);

		// ===================================================
		// ========== COMBOBOX MOUSEE

		comboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					// Check if the right mouse button is released
					menu_cbb.show(comboBox, e.getX(), e.getY());
				}
			}
		});

		// ===================================================
		// ========== ITEM MENU MOUSEE

		item_delete_account.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = table.getSelectedRows();
				if (selectedRows.length > 0) {
					int confirmation = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO DELETE?",
							"CONFIRM", JOptionPane.YES_NO_OPTION);
					if (confirmation == JOptionPane.YES_OPTION) {
						for (int i = selectedRows.length - 1; i >= 0; i--) {
							int selectedRow = selectedRows[i];

							int id = Integer.parseInt((String) table.getValueAt(selectedRow, 0));
							String username = (String) table.getValueAt(selectedRow, 4);
							System.out.println("babab: " + id + " " + username);

							deleteUser(id, username);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please choose at least one row!", "Warning",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		item_reset_pass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				int confirmation = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO RESET PASSWORD?",
						"CONFIRM", JOptionPane.YES_NO_OPTION);
				if (confirmation == JOptionPane.YES_OPTION) {
					String username = (String) table.getValueAt(index, 4);
					resetPassword(username);
				}
			}
		});

		item_new.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				newAccount();
			}
		});

		item_new_group.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				newGroup();
			}
		});

		item_add_to_group.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				AddToGroup addToGroup = new AddToGroup(selectCBB);
				addToGroup.setVisible(true);
			}
		});

		item_delete_from_group.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = table_2.getSelectedRow();
				int confirmation = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO DELETE?", "CONFIRM",
						JOptionPane.YES_NO_OPTION);
				if (confirmation == JOptionPane.YES_OPTION) {
					int id = Integer.parseInt((String) table_2.getValueAt(index, 0));
					deleteUserFromGroup(id);
				}
			}
		});

		item_delete_group.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteGroup();
			}
		});

		item_rename_tree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File f = new File(currDirectory + File.separator + selectTree);
				String name = JOptionPane.showInputDialog("ENTER NEW FOLDER NAME");
				File newf = new File(currDirectory + File.separator + name);
				f.renameTo(newf);
				refreshTree();
			}
		});

		// ================================================
		// ========== BORDERR

		Border compound2 = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "ACCOUNT PERSONAL",
						TitledBorder.DEFAULT_JUSTIFICATION, // Sử dụng DEFAULT_JUSTIFICATION
						TitledBorder.DEFAULT_POSITION, titleFont), // Sử dụng font tùy chỉnh
				BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Đặt padding cho border

		Border compound1 = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Connected Users",
						TitledBorder.DEFAULT_JUSTIFICATION, // Sử dụng DEFAULT_JUSTIFICATION
						TitledBorder.DEFAULT_POSITION, titleFont), // Sử dụng font tùy chỉnh
				BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Đặt padding cho border

		Border compound3 = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "FOLDER SERVER",
						TitledBorder.DEFAULT_JUSTIFICATION, // Sử dụng DEFAULT_JUSTIFICATION
						TitledBorder.DEFAULT_POSITION, titleFont), // Sử dụng font tùy chỉnh
				BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Đặt padding cho border

		Border compound4 = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "ACCOUNT COMPANY",
						TitledBorder.DEFAULT_JUSTIFICATION, // Sử dụng DEFAULT_JUSTIFICATION
						TitledBorder.DEFAULT_POSITION, titleFont), // Sử dụng font tùy chỉnh
				BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Đặt padding cho border

		Border compound5 = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "FTP SERVER",
						TitledBorder.DEFAULT_JUSTIFICATION, // Sử dụng DEFAULT_JUSTIFICATION
						TitledBorder.DEFAULT_POSITION, titleFont), // Sử dụng font tùy chỉnh
				BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Đặt padding cho border

		// ===========================================
		// ========== SELECT COMBOBOXX

//		comboBox.addMouseListener(new () {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String selectedOption = comboBox.getSelectedItem().toString();
//				viewCBB(selectedOption);
//			}
//		});

		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selectCBB = comboBox.getSelectedItem().toString();
					System.out.println(selectCBB);
					viewCBB(selectCBB);
				}
			}
		});

		// ==========================================
		// ========== JScrollPane

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(10, 289, 501, 466);
		getContentPane().add(scrollPane_3);

		// =========================================
		// ========== TREE

		root = new DefaultMutableTreeNode("Root");
		tree = new JTree(root);
		scrollPane_3.setViewportView(tree);

		DefaultMutableTreeNode newRoot = creatTree(new File(currDirectory));
		tree.setModel(new DefaultTreeModel(newRoot));

		// ===================================================
		// ========== SET TABLEE

		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableModel_2 = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableModel_1 = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Chia cột
		tableModel.addColumn("IDNV");
		tableModel.addColumn("NAME");
		tableModel.addColumn("SDT");
		tableModel.addColumn("Email");
		tableModel.addColumn("Username");

		tableModel_2.addColumn("IDNV");
		tableModel_2.addColumn("NAME");
		tableModel_2.addColumn("SDT");
		tableModel_2.addColumn("Email");
		tableModel_2.addColumn("Username");

		tableModel_1.addColumn("ID");
		tableModel_1.addColumn("Name");
		tableModel_1.addColumn("SDT");
		tableModel_1.addColumn("Email");
		tableModel_1.addColumn("Username");
		tableModel_1.addColumn("Password");

		table = new JTable(tableModel);
		table_2 = new JTable(tableModel_2);

		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table_2.getColumnModel().getColumn(0).setPreferredWidth(10);

		// Xử lý sự kiện khi click vào 1 hàng trong bảng
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					String selectedUsername = (String) table.getValueAt(selectedRow, 1);
					String selectedPassword = (String) table.getValueAt(selectedRow, 2);
				}
			}
		});

		// =====================================================
		// ============ TABLE MOUSEE

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = table.rowAtPoint(e.getPoint());
					int column = table.columnAtPoint(e.getPoint());

					if (row >= 0 && row < table.getRowCount() && column >= 0 && column < table.getColumnCount()) {
						table.setRowSelectionInterval(row, row); // Chọn hàng được nhấp chuột
						menu_account.show(table, e.getX(), e.getY()); // Hiển thị menu ngữ cảnh

						// Lấy ra tên user được chọn
						Object userSelect = table.getValueAt(row, 4);
						System.out.println(userSelect.toString());
					}
				}
			}
		});

		JTableHeader tableHeader = table_2.getTableHeader();
		tableHeader.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					menu_group.show(tableHeader, e.getX(), e.getY());
				}
			}
		});

		JTableHeader tableHeader1 = table.getTableHeader();
		tableHeader1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					menu_head_1.show(tableHeader1, e.getX(), e.getY());
				}
			}
		});

		table_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = table_2.rowAtPoint(e.getPoint());
					int column = table_2.columnAtPoint(e.getPoint());

					if (row >= 0 && row < table_2.getRowCount() && column >= 0 && column < table_2.getColumnCount()) {
						table_2.setRowSelectionInterval(row, row); // Chọn hàng được nhấp chuột
						menu_dele.show(table_2, e.getX(), e.getY()); // Hiển thị menu ngữ cảnh

						// Lấy ra tên user được chọn
						Object userSelect = table_2.getValueAt(row, 4);
						System.out.println(userSelect.toString());
					}
				}
			}
		});

		// =====================================================
		// ============ TREE MOUSEE

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = tree.getRowForLocation(e.getX(), e.getY());
					if (row != -1) {
						TreePath path = tree.getPathForRow(row);
						tree.setSelectionPath(path);
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
						Object userObject = selectedNode.getUserObject();
						System.out.println("Right-clicked on: " + userObject);
						selectTree = (String) userObject;
						System.out.println(selectTree);
						menu_tree.show(tree, e.getX(), e.getY());
					}
				}
			}
		});

		// =====================================================
		// ================ JScrollPanee

		scrollPane_1.setViewportView(table);
		scrollPane_4.setViewportView(table_2);

		// =====================================================
		// ================ SET BORDERR

		scrollPane_1.setBorder(compound2);
		scrollPane_3.setBorder(compound3);
		scrollPane.setBorder(compound1);
		panel_1.setBorder(compound4);
		panel.setBorder(compound5);

		// =====================================================
		// ================ BUTTONN

		btn_start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				controlPort = Integer.parseInt(txt_port.getText());
				x = new XuLy(controlPort, serverRunning, textArea, listUserConnect, listUser, comboBox);
				thread = new Thread(x);
				thread.start();

				btn_start.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});

		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (x != null) {
					x.stopServer();
					thread = null;
				}
				tableModel.setRowCount(0);
//				root.removeAllChildren();
				tableModel_1.setRowCount(0);
				tableModel_2.setRowCount(0);
				comboBox.removeAllItems();
				btn_start.setEnabled(true);
				btnStop.setEnabled(false);
			}
		});

		// ==============================================
		// =========== RUNN

//		showAccountList();
//		// showAccountSignup();
//		setCBB(comboBox);
		// ========================================

	}

	// =====================================================================
	// ==================___________METHOD_________=========================
	// =====================================================================

	// ===================================================
	// ========== NEW ACCOUNT

	private static void newAccount() {
		NewAccount newac = new NewAccount(DB_URL, DB_USER, DB_PASSWORD);
		newac.setVisible(true);
	}

	// ===================================================
	// ========== RESET PASSWORD

	private static void resetPassword(String username) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String sql = "UPDATE account SET password = ? WHERE username = ?";
			PreparedStatement pst = connection.prepareStatement(sql);
			pst.setString(1, "ftpserver");
			pst.setString(2, username);

			int result = pst.executeUpdate();
			if (result > 0) {
				JOptionPane.showMessageDialog(null, "PASSWORD CHANGE SUCCESSFUL!");
			}
			pst.close();
			connection.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "PASSWORD CHANGE FAILED");
		}
	}

	// ===================================================
	// ========== NEW GROUP

	private static void newGroup() {
		try {
			String name = JOptionPane.showInputDialog("ENTER NEW GROUP NAME!");
			File folder = new File(currDirectory + File.separator + name);
			if (folder.exists()) {
				JOptionPane.showMessageDialog(null, "PLEASE DELETE OR CHANGE THE PREVIOUS LINK!");
			} else {
				if (name != null && !name.trim().isEmpty()) {
					Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
					// kiểm tra tên group đã tồn tại hay chưa ?
					String sql1 = "SELECT * FROM groupofcompany WHERE namegroup = ?";
					PreparedStatement pst1 = connection.prepareStatement(sql1);
					pst1.setString(1, name);
					ResultSet resultSet = pst1.executeQuery();
					if (resultSet.next()) {
						JOptionPane.showMessageDialog(null, "GROUP NAME ALREADY EXISTS!");
					} else {
						String sql = "INSERT INTO groupofcompany (namegroup) VALUES (?)";
						PreparedStatement pst = connection.prepareStatement(sql);
						pst.setString(1, name);

						int result = pst.executeUpdate();
						if (result > 0) {
							File f = new File(currDirectory + File.separator + name);
							f.mkdir();
							JOptionPane.showMessageDialog(null, "CREATE GROUP SUCCESSFULLY!");
							refreshTree();
							setCBB(comboBox);
						}
						pst.close();
					}
					pst1.close();
					connection.close();
				} else {
					JOptionPane.showMessageDialog(null, "GROUP NAME IS EMPTY!");
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "CREATE GROUP FAILED!");
		}
	}

	// ===============================================
	// ======== SET COMBOBOXXX

	public static void setCBB(JComboBox<String> cbbGroup) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT namegroup FROM groupofcompany");

			// Clear existing items in the JComboBox
			cbbGroup.removeAllItems();

			// Tạo một Vector để lưu trữ dữ liệu từ cơ sở dữ liệu
			Vector<String> data = new Vector<>();
			data.add("Chọn Group");

			while (resultSet.next()) {
				data.add(resultSet.getString("namegroup"));
			}

			// Set the data vector to the JComboBox
			cbbGroup.setModel(new DefaultComboBoxModel<>(data));

			resultSet.close();
			statement.close();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ================================================
	// =========VIEW COMBOBOXXX

	public static void viewCBB(String select) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String sql = "SELECT ifor.iduser, ifor.name, ifor.sdt, ifor.email, ifor.username "
					+ "FROM information ifor " + "JOIN employeegroup eg ON ifor.iduser = eg.iduser "
					+ "JOIN groupofcompany gr ON gr.idgroup = eg.idgroup " + "WHERE gr.namegroup = ?";
			PreparedStatement pst = connection.prepareStatement(sql);
			pst.setString(1, select);
			ResultSet resultSet = pst.executeQuery();
			if (tableModel_2.getRowCount() > 0) {
				tableModel_2.setRowCount(0);
			}
			while (resultSet.next()) {
				String iduser = resultSet.getString("ifor.iduser");
				String name = resultSet.getString("ifor.name");
				String sdt = resultSet.getString("ifor.sdt");
				String email = resultSet.getString("ifor.email");
				String username = resultSet.getString("ifor.username");
				tableModel_2.addRow(new Object[] { iduser, name, sdt, email, username });

				// Set chiều rộng cột cụ thể (ví dụ: cột "Name" có chiều rộng 150 pixels)
				TableColumn column = table_2.getColumnModel().getColumn(1); // 1 là chỉ số của cột "Name"
				column.setPreferredWidth(150);

				TableColumn column2 = table_2.getColumnModel().getColumn(3); // 1 là chỉ số của cột "Name"
				column2.setPreferredWidth(150);
			}

			resultSet.close();
			pst.close();
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ================================================
	// ========== refresh tree

	public static void refreshTree() {
		root.removeAllChildren();
		DefaultMutableTreeNode newRoot = creatTree(new File(currDirectory));
		tree.setModel(new DefaultTreeModel(newRoot));
	}

	// ================================================
	// ========== create tree

	private static DefaultMutableTreeNode creatTree(File file) {
		DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(file.getName());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
//					if (f.isDirectory())
					newRoot.add(creatTree(f));
				}
			}
		}
		return newRoot;
	}

	// ===================================================
	// ========== DELETE USER

	private void deleteUser(int id, String username) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String sql3 = "SELECT * FROM employeegroup WHERE iduser = ?";
			PreparedStatement pst3 = connection.prepareStatement(sql3);
			pst3.setInt(1, id);
			ResultSet resultSet = pst3.executeQuery();
			if (resultSet.next()) {
				String sql4 = "DELETE FROM employeegroup WHERE iduser = ?";
				PreparedStatement pst4 = connection.prepareStatement(sql4);
				pst4.setInt(1, id);
				int result4 = pst4.executeUpdate();
			}

			String sql1 = "DELETE FROM information WHERE iduser = ?";
			PreparedStatement pst1 = connection.prepareStatement(sql1);
			pst1.setInt(1, id);
			int result1 = pst1.executeUpdate();

			String sql2 = "DELETE FROM account WHERE username = ?";
			PreparedStatement pst2 = connection.prepareStatement(sql2);
			pst2.setString(1, username);
			int result2 = pst2.executeUpdate();

			if (result1 > 0 && result2 > 0) {
				JOptionPane.showMessageDialog(null, "DELETE ACCOUNT SUCCESSFULLY!");
				showAccountList();
				refreshTree();
				viewCBB(selectCBB);
			}
			pst1.close();
			pst2.close();
			pst3.close();
			connection.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "DELETE ACCOUNT FAILED");
		}
	}

	// ===================================================
	// ========== DELETE FROM GROUP

	private void deleteUserFromGroup(int id) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String sql3 = "SELECT * FROM groupofcompany WHERE namegroup = ?";
			PreparedStatement pst3 = connection.prepareStatement(sql3);
			pst3.setString(1, selectCBB);
			ResultSet resultSet = pst3.executeQuery();
			if (resultSet.next()) {
				int idgroup = resultSet.getInt("idgroup");
				String sql4 = "DELETE FROM employeegroup WHERE iduser = ? and idgroup = ?";
				PreparedStatement pst4 = connection.prepareStatement(sql4);
				pst4.setInt(1, id);
				pst4.setInt(2, idgroup);
				int result4 = pst4.executeUpdate();	
				if (result4 > 0) {
					JOptionPane.showMessageDialog(null, "DELETE ACCOUNT SUCCESSFULLY!");
					showAccountList();
					refreshTree();
					viewCBB(selectCBB);
				}
				pst4.close();
			}
			pst3.close();
			connection.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "DELETE ACCOUNT FAILED");
		}
	}

	// ===================================================
	// ========== DELETE GROUP

	private void deleteGroup() {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String sql1 = "SELECT * FROM groupofcompany WHERE namegroup = ?";
			PreparedStatement pst1 = connection.prepareStatement(sql1);
			pst1.setString(1, selectCBB);
			ResultSet resultSet = pst1.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("idgroup");
				String sql2 = "DELETE FROM employeegroup WHERE idgroup = ?";
				PreparedStatement pst2 = connection.prepareStatement(sql2);
				pst2.setInt(1, id);
				int result2 = pst2.executeUpdate();

				String sql3 = "DELETE FROM groupofcompany WHERE idgroup = ?";
				PreparedStatement pst3 = connection.prepareStatement(sql3);
				pst3.setInt(1, id);
				int result3 = pst3.executeUpdate();

				if (result2 > 0 && result3 > 0) {
					JOptionPane.showMessageDialog(null, "DELETE GROUP SUCCESSFULLY!");
					showAccountList();
					refreshTree();
					viewCBB(selectCBB);
					setCBB(comboBox);
				}
				pst2.close();
				pst3.close();
			}
			pst1.close();
			connection.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "DELETE GROUP FAILED");
		}
	}

	// ===================================================
	// ========== ACCOUNTT

	public static void showAccountList() {
		try {
			System.out.println("ahahha");
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM information");
			if (tableModel.getRowCount() > 0) {
				tableModel.setRowCount(0);
			}
			while (resultSet.next()) {
				String iduser = resultSet.getString("iduser");
				String name = resultSet.getString("name");
				String sdt = resultSet.getString("sdt");
				String email = resultSet.getString("email");
				String username = resultSet.getString("username");
//				if (namegroup == "") {
//					tableModel.addRow(new Object[] { iduser, name, sdt, email, username, password, "okok" });
//				} else {
				tableModel.addRow(new Object[] { iduser, name, sdt, email, username });
//				}
			}
			// Set chiều rộng cột cụ thể (ví dụ: cột "Name" có chiều rộng 150 pixels)
			TableColumn column = table.getColumnModel().getColumn(1); // 1 là chỉ số của cột "Name"
			column.setPreferredWidth(150);

			TableColumn column2 = table.getColumnModel().getColumn(3); // 1 là chỉ số của cột "Name"
			column2.setPreferredWidth(150);

			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ===================================================
	// ========== LIST USER CONNECT

	public void addConnectedUsers(String s) {
		listConnectedUsers.add(s);
		textArea.append(s + "\n");
	}

	public boolean checkConnected(String username) {
		if (textArea.getText().contains(username)) {
			return false;
		}
		return true;
	}

	// ===================================================
	// ========== SERVERR

	public ServerFTP(String s) {
	}

	public void startServer() {
		try {
			welcomeSocket = new ServerSocket(controlPort);
		} catch (IOException e) {
			System.out.println("Could not create server socket");
			System.exit(-1);
		}

		System.out.println("FTP Server started listening on port " + controlPort);
		int noOfThreads = 0;
		while (serverRunning) {
			try {
				Socket client = welcomeSocket.accept();
				// Port for incoming dataConnection (for passive mode) is the controlPort +
				// number of created threads + 1
				int dataPort = controlPort + noOfThreads + 1;
				int data = 20;
				System.out.println(dataPort);
				// Create new worker thread for new connection
				HandlerClient w = new HandlerClient(client, dataPort, textArea, listUserConnect, listUser);
				System.out.println("New connection received. Worker was created.");
				noOfThreads++;
				w.start();
			} catch (IOException e) {
				System.out.println("Exception encountered on accept");
				e.printStackTrace();
			}
		}
		try {
			welcomeSocket.close();
			System.out.println("Server was stopped");
		} catch (IOException e) {
			System.out.println("Problem stopping server");
			System.exit(-1);
		}
	}

//	private static class ServerWorker extends SwingWorker<Void, Void> {
//		private final ServerFTP server;
//
//		public ServerWorker(ServerFTP server) {
//			this.server = server;
//		}
//
//		@Override
//		protected Void doInBackground() throws Exception {
//			server.startServer();
//			return null;
//		}
//	}
}
