package Client;

import java.awt.EventQueue;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.mysql.cj.x.protobuf.MysqlxNotice.Frame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.tree.TreeNode;
import javax.swing.JTextArea;

public class Home extends JFrame {

	// ===========================String
	private String nameuser;
	private String txt_selectedTree_client_text;

	// ============================== Control ==============================
	private static Socket socket;
	private static BufferedReader controlReader;
	private static PrintWriter controlWriter;
	// ============================== Data ==============================
	private static ServerSocket dataSocket_Server;
	private static Socket dataSocket;
	private static BufferedReader dataReader;
	private PrintWriter dataWriter;
	private static int dataPort;
	// ============================== Cây thư mục ==============================
	private static JTree tree; // server
	private static JTree tree_select;
	private JTree treeClient; // client
	private JTree treeClient_select;
	private static DefaultMutableTreeNode root;
	private static DefaultMutableTreeNode root_select;
	private DefaultMutableTreeNode rootClient;
	private DefaultMutableTreeNode rootClient_select;
	// ============================== JtextField ==============================
	private JTextField demo_server;
	private JTextField txt_path_client;
	// =================================Name group
	private String namegroup = null;
	// ============================== BOOLEAN ==============================
	private boolean mode = true;

	// ============================== SETTING ==============================
	private enum transferType {
		ASCII, BINARY
	}

	private transferType transferMode = transferType.ASCII;

	private enum transMode {
		PASV, ACTIVE
	}

	private transMode transmode = transMode.ACTIVE;
	// ======================================================================
	private JPanel contentPane;
	private JScrollPane scrollPaneClient;
	private JScrollPane scrollPaneClient_1;
	private JScrollPane scrollPaneClient_2;
	private JScrollPane scrollPaneClient_select;
	private JTextField demo;
	// ======================================================================
	private String treeClient_text;
	private static String treeClient_select_text = null;
	private String tree_text;
	private String tree_select_text = null;
	private JTextField txtCserver;
	private JLabel label_test;
	private static JTextArea textArea;
	// ======================================================================
	private ArrayList<String> listClient = new ArrayList<String>();
	private ArrayList<String> listServer = new ArrayList<String>();
	private SynchronizedClient syn;
	private static Home home;

	// ======================================================================
	// ========== Constructor to initialize the control connection ==========
	// ======================================================================

	public Home(Socket socket, BufferedReader controlReader, PrintWriter controlWriter, String nameuser) {
		try {
			// Chọn Look and Feel của Windows
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

			// Gọi updateUI để cập nhật giao diện người dùng hiện tại
			SwingUtilities.updateComponentTreeUI(SwingUtilities.getRootPane(new JFrame()));

		} catch (Exception e) {
			e.printStackTrace();
			// Xử lý ngoại lệ nếu có lỗi khi thiết lập Look and Feel mới
		}
		this.socket = socket;
		this.controlReader = controlReader;
		this.controlWriter = controlWriter;
		this.nameuser = nameuser;
		syn = new SynchronizedClient(socket, controlReader, controlWriter, Home.this);
		Thread thread = new Thread(syn);
		thread.start();
		syn.setNameuser(nameuser);

		createUI();
	}

	private void createUI() {
		
		setTitle("FTP CLIENT");
		JMenuBar bar = new JMenuBar();
		JMenu jMenu_mode = new JMenu("MODE");

		JMenuItem item_active = new JMenuItem("ACTIVE");
		JMenuItem item_pasv = new JMenuItem("PASIVE");

		jMenu_mode.add(item_active);
		jMenu_mode.addSeparator();
		jMenu_mode.add(item_pasv);

		JMenu jMenu_type = new JMenu("TYPE");

		JMenuItem item_binary = new JMenuItem("BINARY");
		JMenuItem item_ascii = new JMenuItem("ASCII");

		jMenu_type.add(item_binary);
		jMenu_type.addSeparator();
		jMenu_type.add(item_ascii);

		JMenu jmenu_setting = new JMenu("SETTING");

		JMenuItem item_ttcn = new JMenuItem("PERSONAL INFORMATION");
		JMenuItem item_quit = new JMenuItem("QUIT");
		JMenuItem item_rs = new JMenuItem("REFRESH");

		jmenu_setting.add(item_ttcn);
		jmenu_setting.addSeparator();
		jmenu_setting.add(item_rs);
		jmenu_setting.addSeparator();
		jmenu_setting.add(item_quit);

		JMenu jmenu_account = new JMenu("ACCOUNT");
		JMenuItem item_per = new JMenuItem("PERSONAL");
		JMenuItem item_com = new JMenuItem("COMPANY");
		jmenu_account.add(item_per);
		jmenu_account.addSeparator();
		jmenu_account.add(item_com);
		
		

		bar.add(jmenu_setting);
		bar.add(jMenu_mode);
		bar.add(jMenu_type);
		bar.add(jmenu_account);

		this.setJMenuBar(bar);

		JPopupMenu menu = new JPopupMenu();

		JMenuItem item_new = new JMenuItem("New Folder");
		JMenuItem item_upload = new JMenuItem("Upload");
		JMenuItem item_rename = new JMenuItem("Rename");
		JMenuItem item_delete = new JMenuItem("Delete");
		JMenuItem item_test = new JMenuItem("test");

		menu.add(item_new);
		menu.add(item_upload);
		menu.add(item_rename);
		menu.add(item_delete);
		menu.add(item_test);

		JPopupMenu menu_right = new JPopupMenu();

		JMenuItem item_new_right = new JMenuItem("New Folder");
		JMenuItem item_dowload_right = new JMenuItem("Dowload");
		JMenuItem item_rename_right = new JMenuItem("Rename");
		JMenuItem item_delete_right = new JMenuItem("Delete");
		JMenuItem item_test_rigtht = new JMenuItem("test");

		menu_right.add(item_new_right);
		menu_right.add(item_dowload_right);
		menu_right.add(item_rename_right);
		menu_right.add(item_delete_right);
		menu_right.add(item_test_rigtht);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(10, 10, 1504, 885);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height - 40);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		// =============================== JScrollPane ===============================

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 35, 1502, 160);
		contentPane.add(scrollPane_1);

		// =============================== JTextArea ===============================

		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);

		// =============================== JTextField ===============================

		demo_server = new JTextField();
		demo_server.setBounds(899, 710, 613, 30);
		contentPane.add(demo_server);
		demo_server.setColumns(10);

		txt_path_client = new JTextField();
		txt_path_client.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_path_client.setBounds(100, 207, 637, 33);
		contentPane.add(txt_path_client);
		txt_path_client.setColumns(10);

		demo = new JTextField();
		demo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		demo.setColumns(10);
		demo.setBounds(132, 710, 621, 30);
		contentPane.add(demo);

		txtCserver = new JTextField();
		txtCserver.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtCserver.setColumns(10);
		txtCserver.setBounds(899, 207, 613, 33);
		contentPane.add(txtCserver);

		// =============================== JLabel ===============================

		JLabel lblNewLabel = new JLabel("Local site:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblNewLabel.setBounds(10, 205, 91, 33);
		contentPane.add(lblNewLabel);

		JLabel lblRemoteSite = new JLabel("Remote site:");
		lblRemoteSite.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblRemoteSite.setBounds(786, 205, 142, 33);
		contentPane.add(lblRemoteSite);

		JLabel lblNewLabel_2 = new JLabel("MODE :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		lblNewLabel_2.setBounds(961, -24, 85, 60);
		contentPane.add(lblNewLabel_2);

		JLabel txt_mode = new JLabel("ACTIVE");
		txt_mode.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_mode.setBounds(1038, -24, 85, 60);
		contentPane.add(txt_mode);

		JLabel lblNewLabel_2_1 = new JLabel("TYPE :");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		lblNewLabel_2_1.setBounds(1146, -24, 75, 60);
		contentPane.add(lblNewLabel_2_1);

		JLabel txt_type = new JLabel("ASCII");
		txt_type.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txt_type.setBounds(1217, -17, 75, 47);
		contentPane.add(txt_type);

		// =============================== Tree ===============================

		rootClient = new DefaultMutableTreeNode("Root");
		treeClient = new JTree(rootClient);
		scrollPaneClient = new JScrollPane(treeClient);
		scrollPaneClient.setBounds(10, 250, 743, 191);
		contentPane.add(scrollPaneClient);
//		treeClient.setRootVisible(false);

		rootClient_select = new DefaultMutableTreeNode("Root Client Select");
		treeClient_select = new JTree(rootClient_select);
		scrollPaneClient_select = new JScrollPane(treeClient_select);
		scrollPaneClient_select.setBounds(10, 451, 743, 249);
		contentPane.add(scrollPaneClient_select);
		treeClient_select.setRootVisible(false);

		root = new DefaultMutableTreeNode("Root"); // Thay đổi tên thư mục gốc
		tree = new JTree(root);
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setBounds(769, 250, 743, 191);
		contentPane.add(scrollPane);
//		tree.setRootVisible(false);

		root_select = new DefaultMutableTreeNode("Root Select"); // Thay đổi tên thư mục gốc
		tree_select = new JTree(root_select);
		JScrollPane scrollPane_select = new JScrollPane(tree_select);
		scrollPane_select.setBounds(769, 451, 743, 249);
		contentPane.add(scrollPane_select);
		tree_select.setRootVisible(false);

		// ======================================================================
		// ============================ Action BAR =============================
		// ======================================================================

		item_ttcn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// TODO Auto-generated method stub
//					System.out.println("okok");
//					sendControl("INFO");
//					String resControl = controlReader.readLine();
//					String[] array = resControl.split(",");
//					System.out.println(resControl);
//					for (String string : array) {
//						System.out.println("test: " + string);
//					}
					PersonalInformation person = new PersonalInformation(controlReader, controlWriter);
					person.setVisible(true);
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});

		item_quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					QUIT(Home.this);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});

		item_active.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txt_mode.setText("ACTIVE");
				transmode = transMode.ACTIVE;
			}
		});

		item_pasv.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txt_mode.setText("PASVIVE");
				transmode = transMode.PASV;
			}
		});

		item_binary.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sendControl("TYPE I");
					String resControl = controlReader.readLine();
					textArea.append(resControl + "\n");
					if (resControl.startsWith("200")) {
						txt_type.setText("BINARY");
						transferMode = transferType.BINARY;
					}
				} catch (IOException e2) {
					// TODO: handle exception
				}
			}
		});

		item_ascii.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sendControl("TYPE A");
					String resControl = controlReader.readLine();
					textArea.append(resControl + "\n");
					if (resControl.startsWith("200")) {
						transferMode = transferType.ASCII;
						txt_type.setText("ASCII");
					}
				} catch (IOException e2) {
					// TODO: handle exception
				}
			}
		});

		item_per.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sendControl("PERSONAL");
					String resControl = controlReader.readLine();
					if (resControl.equals("PERSONAL")) {
						textArea.append("Đang Dùng Tài Khoản Cá Nhân" + "\n");
						JOptionPane.showMessageDialog(null, "USING A PERSONAL ACCOUNT!");
						EPRT();
						namegroup = null;
					}
				} catch (IOException e2) {
					// TODO: handle exception
				}
			}
		});

		item_com.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sendControl("COMPANY");
					String resControl = controlReader.readLine();
					if (resControl.startsWith("COMPANY")) {
						String respone = resControl.substring(8);
						String[] array = respone.split("/");
						String[] newArray = Arrays.copyOf(array, array.length - 1);
						JComboBox<String> comboBox = new JComboBox<>(newArray);
						System.out.println(respone);
						int result = JOptionPane.showOptionDialog(null, // Component cha (null để sử dụng mặc định)
								comboBox, // Component hiển thị (JComboBox)
								"Choose an Option", // Tiêu đề cửa sổ thông báo
								JOptionPane.OK_CANCEL_OPTION, // Kiểu thông báo (OK_CANCEL_OPTION cho nút "OK" và
																// "Cancel")
								JOptionPane.QUESTION_MESSAGE, // Kiểu thông báo (QUESTION_MESSAGE là biểu tượng thông
																// báo)
								null, // Icon (null để sử dụng mặc định)
								null, // Danh sách các lựa chọn (null vì đã sử dụng JComboBox)
								comboBox.getSelectedItem());
						if (result == JOptionPane.OK_OPTION) {
							Object selectedvalue = comboBox.getSelectedItem();
							if (selectedvalue != null) {
								String selectedString = selectedvalue.toString();
								namegroup = selectedString;

								syn.setNamegroup(selectedString);

								System.out.println(selectedString);
								// thay đổi đường dẫn đến group
								sendControl("COMPANY_GROUP " + selectedString);
								String res = controlReader.readLine();
								if (res.equals("COMPANY_GROUP")) {
									// refresh lại cây thư mục server
									EPRT();
								}
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "THE ACCOUNT IS NOT PARTICIPATING IN ANY GROUP!");
					}
				} catch (IOException e2) {
					// TODO: handle exception
				}
			}
		});

		// ======================================================================
		// ============================ Action MOUSE =============================
		// ======================================================================
		item_new.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("Enter the name of the folder you want to create");

				File f = new File(txt_path_client.getText() + File.separator + name);
				if (f.exists()) {
					JOptionPane.showMessageDialog(null, "Create failed: " + name);
				} else {
					f.mkdir();
					buildTreeClient_loc(treeClient_text);
					buildTreeClient();
				}
			}
		});

		item_rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("Nhập tên muốn đổi");

				if (treeClient_select_text == null) {
					int lastIndex = txt_path_client.getText().lastIndexOf(File.separator);
					String path = txt_path_client.getText().substring(0, lastIndex + 1);

					File f = new File(txt_path_client.getText());
					File newf = new File(path + File.separator + name);
					if (newf.exists()) {
						JOptionPane.showMessageDialog(null, "Rename failed: " + name);
					} else {
						f.renameTo(newf);
						buildTreeClient();
					}
				} else {
					File f = new File(txt_path_client.getText() + File.separator + treeClient_select_text);
					File newf = new File(txt_path_client.getText() + File.separator + name);

					if (newf.exists()) {
						JOptionPane.showMessageDialog(null, "Rename failed: " + name);
					} else {
						f.renameTo(newf);
						buildTreeClient_loc(treeClient_text);
						buildTreeClient();
					}

				}

			}
		});

		item_delete.addActionListener(new ActionListener() {

			private void deleteFolder(File f) {
				// xử lý lỗi : nếu file f không tồn tại
				if(!f.exists()) {
					JOptionPane.showMessageDialog(null, "File does not exist");
				} else {
					if (f.isFile()) {
						f.delete();
					} else {
						for (File file : f.listFiles()) {
							if (file.isFile()) {
								file.delete();
							} else {
								deleteFolder(file);
							}
						}
						f.delete();
					}
				}
				
				
				
//				if (f.isFile()) {		// mở cmt
//					f.delete();
//				} else {
//					for (File file : f.listFiles()) {
//						if (file.isFile()) {
//							file.delete();
//						} else {
//							deleteFolder(file);
//						}
//					}
//					f.delete();
//				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if (treeClient_select_text == null) {
					File f = new File(txt_path_client.getText());
					deleteFolder(f);
					buildTreeClient();
				} else {
					File f = new File(txt_path_client.getText() + File.separator + treeClient_select_text);
					deleteFolder(f);
					buildTreeClient_loc(treeClient_text);
					buildTreeClient();
				}

			}
		});

		item_test.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				for (String string : listClient) {
					System.out.println(string);
				}
			}
		});

		item_upload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Đây là nút upload");
				Upload();
				demo_server.setText("");
				demo.setText("");
			}
		});

		item_dowload_right.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Đây là nút upload");
				Dowload();
				demo_server.setText("");
				demo.setText("");
			}
		});

		item_new_right.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Đây là nút new folder right");
				NewFolder();
			}
		});

		item_test_rigtht.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				for (String string : listServer) {
					System.out.println(string);
				}
			}
		});

		item_delete_right.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Delete();
				demo_server.setText("");
//				txtCserver.setText("");
			}
		});

		item_rename_right.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// rename server
//				sendControl(treeClient_select_text);
				rename_Server();
			}
		});
		
		item_rs.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("test reset");
			}
		});
		

		// ======================================================================
		// ============================ Action Tree =============================
		// ======================================================================
		treeClient.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TreePath selectedPath = treeClient.getSelectionPath();
				listClient.clear();
				if (selectedPath != null) {
					Object[] pathObjects = selectedPath.getPath();
					StringBuilder fullPath = new StringBuilder();

					for (int i = 1; i < pathObjects.length; i++) {
						Object pathObject = pathObjects[i];
						if (pathObject instanceof DefaultMutableTreeNode) {
							Object userObject = ((DefaultMutableTreeNode) pathObject).getUserObject();
							if (userObject instanceof String) {
								fullPath.append((String) userObject);
								fullPath.append(File.separator); // Thêm dấu \ giữa các nút
							}
						}
					}

					if (fullPath.length() > 0 && fullPath.charAt(fullPath.length() - 1) == File.separatorChar) {
						fullPath.deleteCharAt(fullPath.length() - 1);
					}
					txt_selectedTree_client_text = fullPath.toString();
					listClient.add(fullPath.toString());
					treeClient_text = fullPath.toString();
				}
				root_select.removeAllChildren();
				treeClient_select_text = null;
				demo.setText(treeClient_select_text);
				buildTreeClient_loc(treeClient_text);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					menu.show(treeClient, e.getX(), e.getY());
				}
			}
		});

		treeClient_select.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
//				InputEvent inputEvent = e.getSourceTriggerEvent();
//				boolean isCtrlDown = inputEvent != null && inputEvent.isControlDown();

				// TODO Auto-generated method stub
				TreePath[] selectedPaths = treeClient_select.getSelectionPaths();
				listClient.clear();
				if (selectedPaths != null) {
					for (TreePath path : selectedPaths) {
						Object[] pathObjects = path.getPath();
						StringBuilder fullPath = new StringBuilder();

						for (int i = 1; i < pathObjects.length; i++) {
							Object pathObject = pathObjects[i];
							if (pathObject instanceof DefaultMutableTreeNode) {
								Object userObject = ((DefaultMutableTreeNode) pathObject).getUserObject();
								if (userObject instanceof String) {
									fullPath.append((String) userObject);
									fullPath.append(File.separator); // Thêm dấu \ giữa các nút
								}
							}
						}

						if (fullPath.length() > 0 && fullPath.charAt(fullPath.length() - 1) == File.separatorChar) {
							fullPath.deleteCharAt(fullPath.length() - 1);
						}
						System.out.println(fullPath.toString());
						listClient.add(fullPath.toString());

//						if (!listClient.contains(fullPath.toString())) {
//							System.out.println(fullPath.toString());
//							listClient.add(fullPath.toString());
//						}
//						
//						for (String string : listClient) {
//							System.out.println("start\n " + string);
//						}

						// Nếu Ctrl (hoặc Meta trên macOS) được nhấn, thêm vào listClient
//		                if (isCtrlDown) {
//		                    System.out.println(fullPath.toString());
//		                    listClient.add(fullPath.toString());
//		                } else {
//		                	listClient.clear();
//		                }
					}

				}

			}
		});

		treeClient_select.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TreePath selectedPath = treeClient_select.getSelectionPath();

				if (selectedPath != null) {
					Object[] pathObjects = selectedPath.getPath();
					StringBuilder fullPath = new StringBuilder();

					for (int i = 1; i < pathObjects.length; i++) {
						Object pathObject = pathObjects[i];
						if (pathObject instanceof DefaultMutableTreeNode) {
							Object userObject = ((DefaultMutableTreeNode) pathObject).getUserObject();
							if (userObject instanceof String) {
								fullPath.append((String) userObject);
								fullPath.append(File.separator); // Thêm dấu \ giữa các nút
							}
						}
					}

					if (fullPath.length() > 0 && fullPath.charAt(fullPath.length() - 1) == File.separatorChar) {
						fullPath.deleteCharAt(fullPath.length() - 1);
					}

					treeClient_select_text = fullPath.toString();
					demo.setText(fullPath.toString());
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					menu.show(treeClient_select, e.getX(), e.getY());
				}
			}
		});

		tree.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				root_select.removeAllChildren();
//				SelectFolder_Server(txt_selectedTree_server.getText());

				TreePath path = tree.getSelectionPath();
				Object[] pathObjects = path.getPath();
				StringBuilder fullPath = new StringBuilder();

				for (int i = 1; i < pathObjects.length; i++) {
					Object pathObject = pathObjects[i];
					if (pathObject instanceof DefaultMutableTreeNode) {
						Object userObject = ((DefaultMutableTreeNode) pathObject).getUserObject();
						if (userObject instanceof String) {
							fullPath.append((String) userObject);
							fullPath.append("\\"); // Thêm dấu \ giữa các nút
						}
					}
				}

				if (fullPath.length() > 0 && fullPath.charAt(fullPath.length() - 1) == '\\') {
					fullPath.deleteCharAt(fullPath.length() - 1);
				}

//				txt_selectedTree_server.setText(fullPath.toString());
				tree_text = fullPath.toString();
				txtCserver.setText(fullPath.toString());

				if (e.getClickCount() == 1) {
					// Lấy đối tượng nút được chọn
//					TreePath path = tree.getSelectionPath();
					if (path != null) {
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
						Object userObject = selectedNode.getUserObject();
//                        tree_select_text = userObject.toString();
						root_select.removeAllChildren();
						SelectFolder_Server(txtCserver.getText());
						tree_select_text = null;
						demo_server.setText(tree_select_text);

					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = tree.getClosestRowForLocation(e.getX(), e.getY());
					tree.setSelectionRow(row);
					menu_right.show(tree, e.getX(), e.getY());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
		tree_select.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath[] selectedPaths = tree_select.getSelectionPaths();
				listServer.clear();
				if (selectedPaths != null) {
					for (TreePath path : selectedPaths) {
						Object[] pathObjects = path.getPath();
						StringBuilder fullPath = new StringBuilder();

						for (int i = 1; i < pathObjects.length; i++) {
							Object pathObject = pathObjects[i];
							if (pathObject instanceof DefaultMutableTreeNode) {
								Object userObject = ((DefaultMutableTreeNode) pathObject).getUserObject();
								if (userObject instanceof String) {
									fullPath.append((String) userObject);
									fullPath.append(File.separator); // Thêm dấu \ giữa các nút
								}
							}
						}

						if (fullPath.length() > 0 && fullPath.charAt(fullPath.length() - 1) == File.separatorChar) {
							fullPath.deleteCharAt(fullPath.length() - 1);
						}

						listServer.add(fullPath.toString());
					}

				}

			}
		});

		tree_select.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					// Lấy đối tượng nút được chọn
					TreePath path = tree_select.getSelectionPath();
					if (path != null) {
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
						Object userObject = selectedNode.getUserObject();
						tree_select_text = userObject.toString();
						demo_server.setText(tree_select_text);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					menu_right.show(tree_select, e.getX(), e.getY());
				}
			}
		});

		JLabel lblNewLabel_1 = new JLabel("Select Client:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblNewLabel_1.setBounds(10, 710, 112, 30);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Select Server:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblNewLabel_1_1.setBounds(769, 710, 135, 30);
		contentPane.add(lblNewLabel_1_1);

		label_test = new JLabel("New label");
		label_test.setBounds(801, 3, 45, 13);
		contentPane.add(label_test);
		label_test.hide();

		setVisible(true);
		buildTreeClient();
		EPRT();
		// PASV();
	}

	private void rex3(String name, ArrayList<String> list) { // ok
		try {
			// ======================== port

			String resControl;
			String resData;
			String cmd = PORT_command();
//			sendControl(cmd);
			System.out.println("test cmd: " + cmd);

			String[] array = cmd.split(",");
			int port = Integer.parseInt(array[4]) * 256 + Integer.parseInt(array[5]);
			System.out.println("test port: " + port);

			// ======================= eprt

//			String resControl;
//			String resData;
//			String cmd = EPRT_command();
//			//
//			String[] splitArgs = cmd.split("\\|");
//			int port = Integer.parseInt(splitArgs[3]);

			// =================================
			sendControl(cmd);
			openDataConnectionActive(port);

			resControl = controlReader.readLine();
			if (resControl.startsWith("200")) {
				sendControl("LIST ");
				resControl = controlReader.readLine();
				if (resControl.startsWith("125")) {
					rex4(list);
					resControl = controlReader.readLine();
					if (resControl.startsWith("226")) {
						closeDataConnectionActive();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void rex4(ArrayList<String> list) { // ok
		try {
			String file = null;
			while (true) {
				file = dataReader.readLine();
				if (file.equals("")) {
					break;
				} else {
//					System.out.println(file);
					list.add(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ======================================================================
	// ===============================METHOD DOWLOAD ===============================
	// ======================================================================

	// ======================================================================
	// ===============================METHOD Upload ===============================
	// ======================================================================

	// ==================== Build tree Client ====================

	// ==================== REFRESH TREE CLIENT ====================

	private void refreshTreeClient(String path, JTree tree) { // OK
		treeClient_text = path;
		DefaultMutableTreeNode newRoot = creatTree(new File(path));
		tree.setModel(new DefaultTreeModel(newRoot));
	}

	// ==================== CREATE TREE CLIENT ====================

	private DefaultMutableTreeNode creatTree(File file) { // OK
		DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(file.getName());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					if (f.isDirectory())
						newRoot.add(creatTree(f));
				}
			}
		}

		return newRoot;
	}

	private DefaultMutableTreeNode creatTree_loc(File file) { // OK
		DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode(file.getName());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					newRoot.add(creatTree(f));
				}
			}
		}

		return newRoot;
	}

	// ==================== Build tree server ====================

	private void buildSelectTreeFromServer() { // OK
		try {
			String file = null;
			while (true) {
				file = dataReader.readLine();

				if (file.equals("")) {
//					System.out.println("end: " + file);
					break;
				} else {
//					System.out.println("file: " + file);

					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file);
					root_select.add(newNode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Cập nhật cây thư mục hiển thị
		tree_select.setModel(new DefaultTreeModel(root_select));
	}

	// ============================== EPRT ==============================

	// ============================================================

	// ========== EPRT - COMMAND ==========

	private static boolean isPortAvailable(int port) { // OK
		try (ServerSocket ignored = new ServerSocket(port)) {
			ignored.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// ============================== PASV ==============================

	// ==================== SEND CONTROL ==============================

	// ==================== SEND DATA ==============================

	// ==================== OPEN DATA PASSIVE ====================

	// ==================== OPEN DATA ACTIVE ====================

	// ==================== CLOSE DATA ACTIVE ====================

	private static void closeDataConnectionActive() { // OK
		try {
			dataReader.close();
			dataSocket.close();
			dataSocket_Server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataReader = null;
		dataSocket = null;
		dataSocket_Server = null;

	}

	private void closeDataConnectionPasv() { // OK
		try {
			dataReader.close();
			dataSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataReader = null;
		dataSocket = null;
		dataSocket_Server = null;

	}

	// ==================== DISPLAY TREE ==============================

	public static void expandFirstChildNodes(JTree tree) { // OK
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		int childCount = root.getChildCount();
		for (int i = 0; i < childCount; i++) {
			TreeNode child = root.getChildAt(i);
			TreePath path = new TreePath(root).pathByAddingChild(child);
			tree.expandPath(path);
		}
	}

	// ======================================================================================
	// ======================ORDER OF OPERATION=====================================
	// ======================================================================================

	// ============== CLIENT -> LOGIN -> BUILDTREE CLIENT
	private void buildTreeClient() {
		String pathClient = "D:\\BKDN\\CNTT\\CNTT_Nam_3\\";
//		String pathClient = "D:\\";
		refreshTreeClient(pathClient, treeClient);
	}

	// ==========CLIENT -> LOGIN -> BUILDTREE SERVER
	public static void EPRT() {
		try {
			// ======================== port

			String resControl;
			String resData;
			String cmd = PORT_command();
//			sendControl(cmd);
			System.out.println("test cmd: " + cmd);

			String[] array = cmd.split(",");
			int port = Integer.parseInt(array[4]) * 256 + Integer.parseInt(array[5]);
			System.out.println("test port: " + port);
			// ======================= eprt

//			String resControl;
//			String resData;
//			String cmd = EPRT_command();
			//
//			String[] splitArgs = cmd.split("\\|");
//			int port = Integer.parseInt(splitArgs[3]);

			// =================================
			sendControl(cmd);
			openDataConnectionActive(port);

			resControl = controlReader.readLine();
			textArea.append(resControl + "\n");
			if (resControl.startsWith("200")) {
				controlWriter.println("LOC");
				resControl = controlReader.readLine();
				textArea.append(resControl + "\n");
//				System.out.println(resControl);
				if (resControl.startsWith("125")) {
					root.removeAllChildren();
					root_select.removeAllChildren();
					tree_select.setModel(new DefaultTreeModel(root_select));
					// ===============
					root = new DefaultMutableTreeNode(dataReader.readLine());
					// ==============
					buildTreeFromServer();
					resControl = controlReader.readLine();
					textArea.append(resControl + "\n");
					if (resControl.startsWith("226")) {
						closeDataConnectionActive();
					}
				}
			}

			expandFirstChildNodes(tree);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ==========CLIENT -> TREECLIENT -> SELECTED
	private void buildTreeClient_loc(String args) {
		String pathClient = "D:\\BKDN\\CNTT\\CNTT_Nam_3\\";
//		String pathClient = "D:\\";
		refreshTreeClient_loc(pathClient + args);
	}

	private void refreshTreeClient_loc(String path) {
		txt_path_client.setText(path);
		DefaultMutableTreeNode newRoot = creatTree_loc(new File(path));
		treeClient_select.setModel(new DefaultTreeModel(newRoot));
	}

	// ==========CLIENT -> ITEM -> UPLOAD
	private void Upload() {
		try {
			String resControl = null;

			// ======= TH1 : kiểm tra 2 có chứa \ không
			if (tree_text.contains(File.separator)) {
				// ====== Thay đổi thư mục làm việc
				String[] array = tree_text.split("\\\\");
				int len = array.length;

				for (int i = 0; i < len; i++) {
					sendControl("CWD " + array[i]);
					resControl = controlReader.readLine();
					textArea.append(resControl + "\n");
					System.out.println(resControl);
				}
				System.out.println("\n\n ---------- 3 là: " + treeClient_select_text + "\n");
				// ====== Kiểm tra 3 khác rỗng
//				if (!treeClient_select_text.equals("")) {
				if (treeClient_select_text != null && !treeClient_select_text.isEmpty()) {
					for (String string : listClient) {
						String pathUpload = txt_path_client.getText() + File.separator + string;
						File f = new File(pathUpload);
						System.out.println(f.getName() + "abc");
						if (f.isFile()) {
//							sendControl("STOR " + );	
							System.out.println("Chạy hàm upload loc");
							Upload_loc(f);
//							treeClient_select_text = null;
						} else {
							UploadFolder(txt_path_client.getText() + File.separator + string);
//							sendControl("CWD ..");
//							resControl = controlReader.readLine();
//							System.out.println("\nCD cha 1: " + resControl);
//							treeClient_select_text = null;
//							JOptionPane.showMessageDialog(null, "Download successfully!");
						}
					}
				} else {// ====== 3 là rỗng
					System.out.println("\n--------------------2 có gạch và 3 rỗng-----------------------\n");
					UploadFolder(txt_path_client.getText());
//					JOptionPane.showMessageDialog(null, "Download successfully!");
//					sendControl("CWD ..");
//					resControl = controlReader.readLine();
//					System.out.println("\nCD cha 2: " + resControl);
//					treeClient_select_text = null;
				}

				// ====== Về lại đường dẫn làm việc ban đầu
				for (int i = 0; i < len; i++) {
					sendControl("CWD ..");
					resControl = controlReader.readLine();
					textArea.append(resControl + "\n");
					System.out.println(resControl);
				}

//				if (namegroup == null) {
//					EPRT();
//				} else {
//					sendControl("REFRESH");
//				}

//				SelectFolder_Server(tree_text);  	--ok

			}

			// ======= TH2 : 2 không chứa \
			else {
				// ====== Thay đổi thư mục làm việc
				sendControl("CWD " + tree_text);
				resControl = controlReader.readLine();
				System.out.println(resControl);

				// ====== Kiểm tra 3 khác rỗng
//				if (!treeClient_select_text.equals("")) {
				if (treeClient_select_text != null && !treeClient_select_text.isEmpty()) {
					// vòng lặp for
					for (String string : listClient) {
						String pathUpload = txt_path_client.getText() + File.separator + string;
						File f = new File(pathUpload);
						System.out.println(f.getName() + "abc");
						if (f.isFile()) {
//							sendControl("STOR " + );	
							System.out.println("Chạy hàm upload loc");
							Upload_loc(f);
//							treeClient_select_text = null;
						} else {
							UploadFolder(txt_path_client.getText() + File.separator + string);
//							sendControl("CWD ..");
//							resControl = controlReader.readLine();
//							System.out.println("\nCD cha 1: " + resControl);
//							treeClient_select_text = null;
//							JOptionPane.showMessageDialog(null, "Download successfully!");
						}
					}

					// ->>>>>>ok
//					String pathUpload = txt_path_client.getText() + File.separator + treeClient_select_text;
//					File f = new File(pathUpload);
//					System.out.println(f.getName() + "abc");
//					if (f.isFile()) {
////						sendControl("STOR " + );	
//						System.out.println("Chạy hàm upload loc");
//						Upload_loc(f);
////						treeClient_select_text = null;
//					} else {
//						UploadFolder(txt_path_client.getText() + File.separator + treeClient_select_text);
////						JOptionPane.showMessageDialog(null, "Download successfully!");
////						sendControl("CWD ..");
////						resControl = controlReader.readLine();
////						System.out.println("\nCD cha 3 : " + resControl);
////						treeClient_select_text = null;
//					}
				}

				// ====== 3 là rỗng
				else {
					UploadFolder(txt_path_client.getText());
//					JOptionPane.showMessageDialog(null, "Download successfully!");
//					sendControl("CWD ..");
//					resControl = controlReader.readLine();
//					System.out.println("\nCD cha 4 : " + resControl);
//					treeClient_select_text = null;
				}

				// ===== Thay đổi đường dẫn làm việc ban đầu
				sendControl("CWD ..");
				resControl = controlReader.readLine();
				System.out.println(resControl);

//				SelectFolder_Server(tree_text);	--ok
//				if (namegroup == null) {
//					EPRT();
//				} else {
//					sendControl("REFRESH");
//				}
			}
//			treeClient_select_text = null;
			// nếu đang ở chế độ personal
			if (namegroup == null) {
				EPRT();
			} else {
				sendControl("REFRESH");
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}

	private void Upload_loc(File f) { // upload file co thong bao
		try {
			if (transmode == transMode.ACTIVE) {
				// ======================== port

				String resControl;
				String resData;
				String cmd = PORT_command();
//				sendControl(cmd);
				System.out.println("test cmd: " + cmd);

				String[] array = cmd.split(",");
				int port = Integer.parseInt(array[4]) * 256 + Integer.parseInt(array[5]);
				System.out.println("test port: " + port);

				// ======================= eprt

//				String resControl;
//				String resData;
//				String cmd = EPRT_command();
//				//
//				String[] splitArgs = cmd.split("\\|");
//				int port = Integer.parseInt(splitArgs[3]);

				// =================================
				sendControl(cmd);
				openDataConnectionActive(port);

				resControl = controlReader.readLine();
				textArea.append(resControl + "\n");
				System.out.println("test ----" + resControl);
				if (resControl.startsWith("200")) {
					sendControl("STOR " + f.getName());
					resControl = controlReader.readLine();
					textArea.append(resControl + "\n");
					System.out.println(resControl);
					if (resControl.startsWith("150")) {
						Upload1_active(f.getAbsolutePath());
						resControl = controlReader.readLine();
						System.out.println(resControl);
						textArea.append(resControl + "\n");
						if (resControl.startsWith("226")) {
							System.out.println("Gửi file thành công");
//							JOptionPane.showMessageDialog(null, "Uploaded successfully: ");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Uploaded failed: ");
					}
				}
			} else {
				// gui lenh pasv
				PASV();
				String resControl;
				sendControl("STOR " + f.getName());
				resControl = controlReader.readLine();
				textArea.append(resControl + "\n");
				System.out.println(resControl);
				if (resControl.startsWith("150")) {
					Upload1_pasv(f.getAbsolutePath());
					resControl = controlReader.readLine();
					System.out.println(resControl);
					textArea.append(resControl + "\n");
					if (resControl.startsWith("226")) {
						System.out.println("Gửi file thành công");
//						JOptionPane.showMessageDialog(null, "Uploaded successfully: ");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Uploaded failed: ");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void UploadFolder(String args) {
		try {
			System.out.println("\ntest" + txt_path_client.getText() + "\n");
			String pathUpload_folder = args;
			System.out.println("args: " + args);
			File f = new File(pathUpload_folder);
			String resControl = null;

			// gọi lệnh tạo folder
			sendControl("XMKD " + f.getName());
			resControl = controlReader.readLine();
			System.out.println(resControl);
			textArea.append(resControl + "\n");

			if (resControl.startsWith("250")) {
				sendControl("CWD " + f.getName());
				resControl = controlReader.readLine();
				System.out.println(resControl);
				textArea.append(resControl + "\n");

				for (File file : f.listFiles()) {
					if (file.isFile()) {
						System.out.println("đệ quy upload file");
						Upload_loc_1(file);
					} else {
						UploadFolder(file.getAbsolutePath());
//						sendControl("CWD ..");
//						resControl = controlReader.readLine();
//						System.out.println("\nCD con: " + resControl);
					}

				}

				sendControl("CWD ..");
				resControl = controlReader.readLine();
				textArea.append(resControl + "\n");
				System.out.println("\nCD con: " + resControl);

			} else {
				JOptionPane.showMessageDialog(null, "Download failed!");
			}
		} catch (Exception e) {
		}
	}

	private void Upload_loc_1(File f) { // upload file kh thong bao
		try {
			if (transmode == transMode.ACTIVE) {
				// ======================== port

				String resControl;
				String resData;
				String cmd = PORT_command();
//				sendControl(cmd);
				System.out.println("test cmd: " + cmd);

				String[] array = cmd.split(",");
				int port = Integer.parseInt(array[4]) * 256 + Integer.parseInt(array[5]);
				System.out.println("test port: " + port);

				// ======================= eprt

//				String resControl;
//				String resData;
//				String cmd = EPRT_command();
//				//
//				String[] splitArgs = cmd.split("\\|");
//				int port = Integer.parseInt(splitArgs[3]);

				// =================================
				sendControl(cmd);
				openDataConnectionActive(port);

				resControl = controlReader.readLine();
				textArea.append(resControl + "\n");
				System.out.println("test ----" + resControl);
				if (resControl.startsWith("200")) {
					sendControl("STOR " + f.getName());
					resControl = controlReader.readLine();
					textArea.append(resControl + "\n");
					System.out.println(resControl);
					if (resControl.startsWith("150")) {
						Upload1_active(f.getAbsolutePath());
						resControl = controlReader.readLine();
						System.out.println(resControl);
						textArea.append(resControl + "\n");
						if (resControl.startsWith("226")) {
							System.out.println("Gửi file thành công");
//							JOptionPane.showMessageDialog(null, "Uploaded successfully: ");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Uploaded failed: ");
					}
				}
			} else {
				// gui lenh pasv
				PASV();
				String resControl;
				sendControl("STOR " + f.getName());
				resControl = controlReader.readLine();
				textArea.append(resControl + "\n");
				System.out.println(resControl);
				if (resControl.startsWith("150")) {
					Upload1_pasv(f.getAbsolutePath());
					resControl = controlReader.readLine();
					System.out.println(resControl);
					textArea.append(resControl + "\n");
					if (resControl.startsWith("226")) {
						System.out.println("Gửi file thành công");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Uploaded failed: ");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// ====================CLIENT -> UPLOAD -> DULIEU
	private void Upload1_active(String pathUpload) {
		System.out.println("Đang chạy Upload.");
		File f = new File(pathUpload);

		if (transferMode == transferType.BINARY) {
			BufferedOutputStream fout = null;
			BufferedInputStream fin = null;

			try {
				// create streams
				fout = new BufferedOutputStream(dataSocket.getOutputStream());
				fin = new BufferedInputStream(new FileInputStream(f));
			} catch (Exception e) {
			}

			// write file with buffer
			byte[] buf = new byte[1024];
			int l = 0;
			try {
				while ((l = fin.read(buf, 0, 1024)) != -1) {
					fout.write(buf, 0, l);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (transferMode == transferType.ASCII) {

			BufferedReader rin = null;
			PrintWriter rout = null;

			try {
				rin = new BufferedReader(new FileReader(f));
				rout = new PrintWriter(dataSocket.getOutputStream(), true);

			} catch (IOException e) {
			}

			String s;

			try {
				while ((s = rin.readLine()) != null) {
					rout.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				rout.close();
				rin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		closeDataConnectionActive();
	}

	private void Upload1_pasv(String pathUpload) {
		System.out.println("Đang chạy Upload.");
		File f = new File(pathUpload);

		if (transferMode == transferType.BINARY) {
			BufferedOutputStream fout = null;
			BufferedInputStream fin = null;

			try {
				// create streams
				fout = new BufferedOutputStream(dataSocket.getOutputStream());
				fin = new BufferedInputStream(new FileInputStream(f));
			} catch (Exception e) {
			}

			// write file with buffer
			byte[] buf = new byte[1024];
			int l = 0;
			try {
				while ((l = fin.read(buf, 0, 1024)) != -1) {
					fout.write(buf, 0, l);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				fin.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (transferMode == transferType.ASCII) {

			BufferedReader rin = null;
			PrintWriter rout = null;

			try {
				rin = new BufferedReader(new FileReader(f));
				rout = new PrintWriter(dataSocket.getOutputStream(), true);

			} catch (IOException e) {
			}

			String s;

			try {
				while ((s = rin.readLine()) != null) {
					rout.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				rout.close();
				rin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		closeDataConnectionPasv();
	}

	// ==============SERVER -> ITEM -> DOWLOAD
	private void Dowload() {
		try {
			String resControl;
			String path = txt_path_client.getText();
			// ====== TH1: ô 2 không chứa \
			if (!tree_text.contains(File.separator)) {
				// nếu có chọn ô 4
				System.out.println("hahahahah: " + tree_select_text);
				if (tree_select_text != null) {
					for (String string : listServer) {
						String nameDowload = string;
						File f = new File(path + File.separator + nameDowload);
						if (f.exists()) {
							JOptionPane.showMessageDialog(null, "Dowload failed : " + nameDowload);
						} else {
							// cd đến thư mục làm việc
							sendControl("CWD " + tree_text);
							resControl = controlReader.readLine();
							System.out.println("1 " + resControl);
							textArea.append(resControl + "\n");

							sendControl("DOWLOAD " + nameDowload);
							resControl = controlReader.readLine();
							System.out.println("rex : " + resControl);
							textArea.append(resControl + "\n");
							if (resControl.equals("File")) {
								rex(path, nameDowload);
							} else {
								rex2(nameDowload, path);
							}

							// quay về đường dẫn ban đầu
							sendControl("CWD ..");
							resControl = controlReader.readLine();
							System.out.println("1 " + resControl);
							textArea.append(resControl + "\n");
						}
					}
				} else {
					File f = new File(path + File.separator + tree_text);
					if (f.exists()) {
						JOptionPane.showMessageDialog(null, "Dowload failed");
					} else {
						rex2(tree_text, path);
					}
				}
			}
			// ô 2 có chứa /
			else {
				// có chọn ô 4
				if (tree_select_text != null) {
					// ====== Thay đổi thư mục làm việc
					String[] array = tree_text.split("\\\\");
					int len = array.length;

					for (String string : listServer) {

						String nameDowload = string;
						File f = new File(path + File.separator + nameDowload);
						if (f.exists()) {
							JOptionPane.showMessageDialog(null, "Dowload failed: " + nameDowload);
						} else {
							for (int i = 0; i < len; i++) {
								sendControl("CWD " + array[i]);
								resControl = controlReader.readLine();
								System.out.println(resControl);
								textArea.append(resControl + "\n");
							}

							sendControl("DOWLOAD " + nameDowload);
							resControl = controlReader.readLine();
							System.out.println("rex : " + resControl);
							textArea.append(resControl + "\n");
							if (resControl.equals("File")) {
								rex(path, nameDowload);
							} else {
								rex2(nameDowload, path);
							}

							for (int i = 0; i < len; i++) {
								sendControl("CWD ..");
								resControl = controlReader.readLine();
								System.out.println(resControl);
								textArea.append(resControl + "\n");
							}
						}
					}

				} else {
					// không chọn ô 4
					String[] array = tree_text.split("\\\\");
					int len = array.length;

					File f = new File(path + File.separator + array[len - 1]);
					if (f.exists()) {
						JOptionPane.showMessageDialog(null, "Dowload failed");
					} else {
						for (int i = 0; i < len - 1; i++) {
							sendControl("CWD " + array[i]);
							resControl = controlReader.readLine();
							System.out.println(resControl);
							textArea.append(resControl + "\n");
						}

						rex2(array[len - 1], path);

						for (int i = 0; i < len - 1; i++) {
							sendControl("CWD ..");
							resControl = controlReader.readLine();
							System.out.println(resControl);
							textArea.append(resControl + "\n");
						}
					}

				}
			}
			buildTreeClient();
//			buildTreeClient_loc(txt_selectedTree_client_text);	--ok
			// sau khi dowload -> refresh
//			if(namegroup == null) {
//				buildTreeClient();
//			} else {
//
//			}

		} catch (Exception e2) {
			// TODO: handle exception
		}
	}

	private void rex(String path, String name) {
		try {
			if (transmode == transMode.ACTIVE) {
				// ======================== port
				String resControl;
				String resData;
				String cmd = PORT_command();
//				sendControl(cmd);
				System.out.println("test cmd: " + cmd);

				String[] array = cmd.split(",");
				int port = Integer.parseInt(array[4]) * 256 + Integer.parseInt(array[5]);
				System.out.println("test port: " + port);

				// ======================= eprt

//				String resControl;
//				String resData;
//				String cmd = EPRT_command();
//				//
//				String[] splitArgs = cmd.split("\\|");
//				int port = Integer.parseInt(splitArgs[3]);

				// =================================
				sendControl(cmd);
				openDataConnectionActive(port);

				resControl = controlReader.readLine();
				System.out.println("rex1_1 : " + resControl);
				textArea.append(resControl + "\n");
				if (resControl.startsWith("200")) {
					sendControl("RETR " + name);
					resControl = controlReader.readLine();
					System.out.println("rex1_2" + resControl);
					textArea.append(resControl + "\n");
					if (resControl.startsWith("150")) {
						bin_1(path + File.separator + name);
						resControl = controlReader.readLine();
						System.out.println("rex1_3" + resControl);
						textArea.append(resControl + "\n");
						if (resControl.startsWith("226")) {
							System.out.println("Tải file thành công");
						}
					}
				}
			}

			// PASV

			else {
				PASV();
				String resControl;
				sendControl("RETR " + name);
				resControl = controlReader.readLine();
				System.out.println("rex1_2" + resControl);
				textArea.append(resControl + "\n");
				if (resControl.startsWith("150")) {
					bin_1_pasv(path + File.separator + name);
					resControl = controlReader.readLine();
					System.out.println("rex1_3" + resControl);
					textArea.append(resControl + "\n");
					if (resControl.startsWith("226")) {
						System.out.println("Tải file thành công");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void rex2(String name, String path) {
		try {
			String resControl;
			// tạo folder
			String newPath = path + File.separator + name;
			File f = new File(newPath);
			if (!f.exists()) {
				f.mkdir();
			}
			// cd đến folder làm việc
			sendControl("CWD " + name);
			resControl = controlReader.readLine();
			System.out.println("rex2_1" + resControl);
			textArea.append(resControl + "\n");

			ArrayList<String> list = new ArrayList<String>();
			rex3(name, list);
//			System.out.println(list.size());
			for (String string : list) {
				System.out.println("========= " + string);
				sendControl("DOWLOAD " + string);
				resControl = controlReader.readLine();
				textArea.append(resControl + "\n");
				if (resControl.equals("File")) {
					rex(newPath, string);
				} else {
					rex2(string, newPath);
				}
			}

			sendControl("CWD ..");
			resControl = controlReader.readLine();
			System.out.println("rex2_2" + resControl);
			textArea.append(resControl + "\n");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ============SERVER -> DOWLOAD -> DULIEU
	private void bin_1(String localFilePath) {
		System.out.println("Đang chạy Download.");

		if (transferMode == transferType.BINARY) {
			BufferedOutputStream fout = null;
			BufferedInputStream fin = null;

			try {
				fout = new BufferedOutputStream(new FileOutputStream(localFilePath));
				fin = new BufferedInputStream(dataSocket.getInputStream());

				byte[] buf = new byte[1024];
				int bytesRead;
				while ((bytesRead = fin.read(buf)) != -1) {
					fout.write(buf, 0, bytesRead);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fin != null)
						fin.close();
					if (fout != null)
						fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			BufferedReader rin = null;
			PrintWriter rout = null;

			try {
				rin = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
				rout = new PrintWriter(new FileOutputStream(localFilePath), true);

			} catch (IOException e) {
			}

			String s;

			try {
				while ((s = rin.readLine()) != null) {
					rout.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				rout.close();
				rin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		closeDataConnectionActive();
	}

	private void bin_1_pasv(String localFilePath) {
		System.out.println("Đang chạy Download.");

		if (transferMode == transferType.BINARY) {
			BufferedOutputStream fout = null;
			BufferedInputStream fin = null;

			try {
				fout = new BufferedOutputStream(new FileOutputStream(localFilePath));
				fin = new BufferedInputStream(dataSocket.getInputStream());

				byte[] buf = new byte[1024];
				int bytesRead;
				while ((bytesRead = fin.read(buf)) != -1) {
					fout.write(buf, 0, bytesRead);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fin != null)
						fin.close();
					if (fout != null)
						fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			BufferedReader rin = null;
			PrintWriter rout = null;

			try {
				rin = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
				rout = new PrintWriter(new FileOutputStream(localFilePath), true);

			} catch (IOException e) {
			}

			String s;

			try {
				while ((s = rin.readLine()) != null) {
					rout.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				rout.close();
				rin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		closeDataConnectionPasv();
	}

	// ============SERVER -> RENAME
	private void rename_Server() {
		try {

			// Hiển thị hộp thoại nhập tên
			String name = JOptionPane.showInputDialog("Nhập tên muốn thay đổi:");

//			if (name == null) {
//
//			}
			if (name != "" && name != null) {
				String resControl;
				// ===== TH1: ô 2 không có \
				if (!tree_text.contains(File.separator)) {
					if (tree_select_text != null) { // ktra ô 3 khác null
						// cd đến thư mục làm việc
						sendControl("CWD " + tree_text);
						resControl = controlReader.readLine();
						System.out.println("1 " + resControl);
						textArea.append(resControl + "\n");

						sendControl("RNFR " + tree_select_text);
						resControl = controlReader.readLine();
						textArea.append(resControl + "\n");
						if (resControl.startsWith("350")) {
							sendControl("RNTO " + name);
							resControl = controlReader.readLine();
							textArea.append(resControl + "\n");
							if (resControl.startsWith("250")) {
								// Hiển thị thông báo
								JOptionPane.showMessageDialog(null, "Renamed to: " + name);

								label_test.setText(name);
							}
						} else if(resControl.startsWith("550")) {
							JOptionPane.showMessageDialog(null, "Rename failed");
						}

						sendControl("CWD ..");
						resControl = controlReader.readLine();
						System.out.println("1 " + resControl);
						textArea.append(resControl + "\n");
					} else {
						sendControl("RNFR " + tree_text);
						resControl = controlReader.readLine();
						textArea.append(resControl + "\n");
						if (resControl.startsWith("350")) {
							sendControl("RNTO " + name);
							resControl = controlReader.readLine();
							textArea.append(resControl + "\n");
							if (resControl.startsWith("250")) {
								// Hiển thị thông báo
								JOptionPane.showMessageDialog(null, "Rename to: " + name);

								label_test.setText(name);

								txtCserver.setText("");
//								if(namegroup == null) {			//bo if
//									EPRT();
//								}
							}
						} else if(resControl.startsWith("550")) {
							JOptionPane.showMessageDialog(null, "Rename failed");
						}
					}
				}

				// ===== TH2: ô 2 có \

				else {

					if (tree_select_text == null) {
						String[] array = txtCserver.getText().split("\\\\");
						int len = array.length;
						for (int i = 0; i < len - 1; i++) {
							sendControl("CWD " + array[i]);
							resControl = controlReader.readLine();
							System.out.println(resControl);
							System.out.println(array[i]);
							textArea.append(resControl + "\n");
						}

						sendControl("RNFR " + array[len - 1]);
						resControl = controlReader.readLine();
						textArea.append(resControl + "\n");
						if (resControl.startsWith("350")) {
							sendControl("RNTO " + name);
							resControl = controlReader.readLine();
							textArea.append(resControl + "\n");
							if (resControl.startsWith("250")) {
								// Hiển thị thông báo
								JOptionPane.showMessageDialog(null, "Rename to: " + name);

								label_test.setText(name);

							}
						} else if (resControl.startsWith("550")) {
							JOptionPane.showMessageDialog(null, "Rename failed");

						}

						for (int i = 0; i < len - 1; i++) {
							sendControl("CWD ..");
							resControl = controlReader.readLine();
							System.out.println(resControl);
							System.out.println(array[i]);
							textArea.append(resControl + "\n");
						}

						txtCserver.setText("");
//						if(namegroup == null) {			//bo if
//							EPRT();
//						}
					} else {

						String[] array = txtCserver.getText().split("\\\\");
						int len = array.length;
						for (int i = 0; i < len; i++) {
							sendControl("CWD " + array[i]);
							resControl = controlReader.readLine();
							System.out.println(resControl);
							System.out.println(array[i]);
							textArea.append(resControl + "\n");
						}

						sendControl("RNFR " + tree_select_text);
						resControl = controlReader.readLine();
						textArea.append(resControl + "\n");
						if (resControl.startsWith("350")) {
							sendControl("RNTO " + name);
							resControl = controlReader.readLine();
							textArea.append(resControl + "\n");
							if (resControl.startsWith("250")) {
								// Hiển thị thông báo
								JOptionPane.showMessageDialog(null, "Rename to: " + name);

								label_test.setText(name);
							}
						} else if(resControl.startsWith("550")) {
							JOptionPane.showMessageDialog(null, "Rename failed");

						}

						for (int i = 0; i < len; i++) {
							sendControl("CWD ..");
							resControl = controlReader.readLine();
							System.out.println(resControl);
							System.out.println(array[i]);

						}

					}

				}
			}

			// sau khi thực thiện rename -> refresh
			if (namegroup == null) {
				EPRT();
			} else {
				sendControl("REFRESH");
			}

//			SelectFolder_Server(tree_text); --ok
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// ===============SYSTEM -> MODE -> PASV
	private void PASV() {
		try {
			sendControl("PASV ");
			String resControl = controlReader.readLine();

			System.out.println(resControl);

			int a = resControl.indexOf("(");
			resControl = resControl.substring(a, resControl.length() - 1);

			String[] parts = resControl.split(",");
			String ipAddress = parts[0].substring(parts[0].lastIndexOf("(") + 1) + "." + parts[1] + "." + parts[2] + "."
					+ parts[3];

			int port = Integer.parseInt(parts[4]) * 256 + Integer.parseInt(parts[5]);

			openDataConnectionPassive(ipAddress, port);

		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	// =================SERVER -> OPEN -> DATA -> PASV
	private void openDataConnectionPassive(String ip, int port) {
		try {
			dataSocket = new Socket(ip, port);

			System.out.println("pasv đang nhận kết quả trả về từ server bằng cổng: " + dataSocket.getLocalPort());
			System.out.println("pasv đang nhận kết quả trả về từ server bằng cổng: " + dataSocket.getPort());
			dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
//			System.out.println("Data connection - Passive Mode - established");
		} catch (IOException e) {
			System.out.println("Could not create data connection.");
			e.printStackTrace();
		}
	}

	// =================SERVER -> OPEN -> DATA -> ACTIVE (EPRT)
	private static void openDataConnectionActive(int port) {
		try {
			dataSocket_Server = new ServerSocket(port);
			dataSocket = dataSocket_Server.accept();
			System.out.println("active đang nhận kết quả trả về từ server bằng cổng: " + dataSocket.getLocalPort());
			System.out.println("active đang nhận kết quả trả về từ server bằng cổng: " + dataSocket.getPort());
			dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
//			System.out.println("Data connection - Passive Mode - established");
		} catch (IOException e) {
			System.out.println("Could not create data connection.");
			e.printStackTrace();
		}
	}

	// ==============CLIENT -> QUIT
	public static void QUIT(JFrame frame) {
		try {
			System.out.println("đang chạy hàm quit");
			sendControl("QUIT");
			String resControl = controlReader.readLine();
			if (resControl.startsWith("221")) {
				socket.close();
				frame.dispose();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ==========SERVER -> TREESERVER -> SELECTED
	private void SelectFolder_Server(String args) {
		root_select.removeAllChildren();
		List_command_loc(args);
	}

	private void List_command_loc(String args) {
		try {
			// ======================== port

			String resControl;
			String resData;
			String cmd = PORT_command();
//				sendControl(cmd);
			System.out.println("test cmd: " + cmd);

			String[] array1 = cmd.split(",");
			int port = Integer.parseInt(array1[4]) * 256 + Integer.parseInt(array1[5]);
			System.out.println("test port: " + port);

			// ======================= eprt

//				String resControl;
//				String resData;
//				String cmd = EPRT_command();
//				//
//				String[] splitArgs = cmd.split("\\|");
//				int port = Integer.parseInt(splitArgs[3]);

			// =================================
			sendControl(cmd);
			openDataConnectionActive(port);

			resControl = controlReader.readLine();
			if (resControl.startsWith("200")) {
				String[] array = args.split("\\\\");
				int len = array.length;

				for (int i = 0; i < len - 1; i++) {
					sendControl("CWD " + array[i]);
					resControl = controlReader.readLine();
					System.out.println(resControl);
				}

				sendControl("LIST " + array[len - 1]);
				resControl = controlReader.readLine();
//					System.out.println(resControl);
				if (resControl.startsWith("125")) {
					buildSelectTreeFromServer();
					resControl = controlReader.readLine();
					if (resControl.startsWith("226")) {
						closeDataConnectionActive();
					}
				}

				for (int i = 0; i < len - 1; i++) {
					sendControl("CWD ..");
					resControl = controlReader.readLine();
					System.out.println(resControl);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ===========SERVER -> NEW FOLDER
	private void NewFolder() {
		try {
			String resControl;
			// Hiển thị hộp thoại nhập tên
			String name = JOptionPane.showInputDialog("Enter the name of the folder you want to create:");
			// TH1 : ô 2 có \
			if (tree_text.contains(File.separator)) {
				String[] array = tree_text.split("\\\\");
				int len = array.length;

				for (int i = 0; i < len; i++) {
					sendControl("CWD " + array[i]);
					resControl = controlReader.readLine();
					System.out.println(resControl);
					System.out.println(array[i]);
//					textArea.append(resControl + "\n");
				}

				sendControl("XMKD " + name);
				resControl = controlReader.readLine();
				if (resControl.startsWith("250")) {
					textArea.append(resControl + "\n");
				} else {
					JOptionPane.showMessageDialog(null, "Create failed");
				}

				for (int i = 0; i < len; i++) {
					sendControl("CWD ..");
					resControl = controlReader.readLine();
					System.out.println(resControl);
					System.out.println(array[i]);
//					textArea.append(resControl + "\n");
				}

			} else {
				sendControl("CWD " + tree_text);
				resControl = controlReader.readLine();
				System.out.println(resControl);

				sendControl("XMKD " + name);
				resControl = controlReader.readLine();
				if (resControl.startsWith("250")) {
					textArea.append(resControl + "\n");
				} else {
					JOptionPane.showMessageDialog(null, "Create failed");
				}

				sendControl("CWD ..");
				resControl = controlReader.readLine();
				System.out.println(resControl);
			}
			if (namegroup == null) {
				EPRT();
			} else {
				sendControl("REFRESH");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ===========SERVER -> DELETE
	private void Delete() {
		if (tree_select_text == null) {
			try {
				String resControl = null;
				String pathRemove = txtCserver.getText();
				System.out.println("path" + pathRemove);

				// kiểm tra txt_selectedTree_server có chứa \ không
				if (pathRemove.contains(File.separator)) {
					String[] array = pathRemove.split("\\\\");
					int len = array.length;
					for (int i = 0; i < len - 1; i++) {
						sendControl("CWD " + array[i]);
						resControl = controlReader.readLine();
						System.out.println(resControl);
						System.out.println(array[i]);
						textArea.append(resControl + "\n");
					}
					sendControl("XRMD " + array[len - 1]);
					resControl = controlReader.readLine();
					System.out.println(resControl);
					textArea.append(resControl + "\n");
					
					
					// kiểm tra lỗi xóa folder
					if(resControl.startsWith("550")) {
						JOptionPane.showMessageDialog(null, "Delete failed!");
					}

//					SelectFolder_Server(array[len - 1]);
//
					for (int i = 0; i < len - 1; i++) {
						sendControl("CWD ..");
						resControl = controlReader.readLine();
						System.out.println(resControl);
						textArea.append(resControl + "\n");
					}
//					EPRT();

				} else {
					sendControl("XRMD " + pathRemove);
					resControl = controlReader.readLine();
					System.out.println(resControl);
					textArea.append(resControl + "\n");
					
					// kiểm tra lỗi xóa folder
					if(resControl.startsWith("550")) {
						JOptionPane.showMessageDialog(null, "Delete failed!");
					}
					
					
					
//					EPRT();
				}
			} catch (IOException e2) {
				System.out.println("Error folder");
			}
		} else {
			try {
				String resControl = null;
				String pathRemove = txtCserver.getText();
				System.out.println("path" + pathRemove);

				// kiểm tra txt_selectedTree_server có chứa \ không
				if (pathRemove.contains(File.separator)) {
					String[] array = pathRemove.split("\\\\");
					int len = array.length;
					for (int i = 0; i < len; i++) {
						sendControl("CWD " + array[i]);
						resControl = controlReader.readLine();
						System.out.println(resControl);
						textArea.append(resControl + "\n");
					}
					System.out.println("\n");

					sendControl("REMOVE " + tree_select_text);
					resControl = controlReader.readLine();
					System.out.println(resControl);
					textArea.append(resControl + "\n");
					
					// kiểm tra lỗi
					if(resControl.startsWith("550")) {
						JOptionPane.showMessageDialog(null, "Delete failed!");
					}

					System.out.println("\n" + tree_select_text + "\n");

//					SelectFolder_Server(array[len-1]);
					System.out.println("========================= test =====================" + pathRemove);

					for (int i = 0; i < len; i++) {
						sendControl("CWD ..");
						resControl = controlReader.readLine();
						System.out.println(resControl);
						textArea.append(resControl + "\n");
					}
//					EPRT();
					SelectFolder_Server(pathRemove);
				} else {
					sendControl("CWD " + pathRemove);
					resControl = controlReader.readLine();
					System.out.println(resControl);
					textArea.append(resControl + "\n");
//
					sendControl("REMOVE " + tree_select_text);
					resControl = controlReader.readLine();
					System.out.println(resControl);
					textArea.append(resControl + "\n");
					
					// kiểm tra lỗi
					if(resControl.startsWith("550")) {
						JOptionPane.showMessageDialog(null, "Delete failed!");
					}

					sendControl("CWD ..");
					resControl = controlReader.readLine();
					System.out.println(resControl);
					textArea.append(resControl + "\n");

//					EPRT();
					SelectFolder_Server(pathRemove);
				}
			} catch (IOException e2) {
				// TODO: handle exception
			}
		}
		if (namegroup == null) {
			EPRT();
		} else {
			sendControl("REFRESH");
		}
	}

	// ===========CLIENT -> PORT
	private static String PORT_command() {
		try {
			String host = IP();
			boolean portAvailable;
			Random random = new Random();

			do {
				// dataPort = 1024 + random.nextInt(64512);
				dataPort = 3024 + random.nextInt(64000);
				portAvailable = isPortAvailable(dataPort);
			} while (!portAvailable);

			String[] ipParts = host.split("\\.");
			int p1 = dataPort / 256;
			int p2 = dataPort % 256;
			String cmd = "PORT " + ipParts[0] + "," + ipParts[1] + "," + ipParts[2] + "," + ipParts[3] + "," + p1 + ","
					+ p2;

//			String cmd = "PORT " + 10 + "," + 10 + "," + 58 + "," + 136 + "," + p1 + "," + p2;
			return cmd;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private static String IP() {
		try {
			String ipv4 = null;
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
				String interfaceName = networkInterface.getDisplayName();
				if (interfaceName.contains("Wireless") || interfaceName.contains("Wi-Fi")) {
					for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
						if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
							ipv4 = inetAddress.getHostAddress();
						}
					}
				}
			}
			return ipv4;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	// ==========SEND CONTROL
	private static void sendControl(String mess) {
		controlWriter.println(mess);
	}

	// ===========EPRT -> COMMAND
	private String EPRT_command() {
		InetAddress loopbackAddress;
		String ipAddress;
		boolean ipv6Available = true;

		try {
			loopbackAddress = Inet6Address.getLoopbackAddress();
			ipAddress = loopbackAddress.getHostAddress();
		} catch (Exception e) {
			ipv6Available = false;
			loopbackAddress = InetAddress.getLoopbackAddress();
			ipAddress = loopbackAddress.getHostAddress();
		}

//		int dataPort;
		boolean portAvailable;
		Random random = new Random();

		do {
//			dataPort = 1024 + random.nextInt(64512);
			dataPort = 3024 + random.nextInt(64512);
			portAvailable = isPortAvailable(dataPort);
		} while (!portAvailable);

		String eprtCommand;
		if (ipv6Available) {
			eprtCommand = "EPRT |2|" + ipAddress + "|" + dataPort + "|";
		} else {
		}
		eprtCommand = "EPRT |1|" + ipAddress + "|" + dataPort + "|";

		return eprtCommand;
	}

	// ===========SERVER -> BUILDTREE
	private static void buildTreeFromServer() {
		try {
			String file = null;
			while (true) {
				// đọc chuỗi : 0 a.txt
				file = dataReader.readLine();

				if (file.equals("5 end")) {
//					System.out.println("end: " + file);
					break;
				} else {
//					System.out.println("file: " + file);
					// xử lý cắt chuỗi lấy ra level và fileName
//					String[] parts = file.split("\\s+");
					String[] parts = file.split("\\s{2,}");
					int count = parts.length;
					int level = Integer.parseInt(parts[count - 2]);
					String filePath = parts[count - 1];

					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(filePath);
					if (level == 0) {
						// Nếu là thư mục gốc, thêm vào nút gốc của cây
						root.setUserObject(newNode);
					} else {
						// Nếu không, tìm nút cha và thêm vào nút cha
						addNode(root, newNode, level - 1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Cập nhật cây thư mục hiển thị
		tree.setModel(new DefaultTreeModel(root));
	}

	private static void addNode(DefaultMutableTreeNode rootNode, DefaultMutableTreeNode newNode, int level) {
		if (level == 0) {
			rootNode.add(newNode);
		} else {
			for (int i = 0; i < rootNode.getChildCount(); i++) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
				addNode(childNode, newNode, level - 1);
			}
		}
	}

	// ===================SYN
	public static void EPRT_syn() {
		try {
			// ======================== port

			String resControl;
			String resData;
			String cmd = PORT_command();
//			sendControl(cmd);
			System.out.println("test cmd: " + cmd);

			String[] array = cmd.split(",");
			int port = Integer.parseInt(array[4]) * 256 + Integer.parseInt(array[5]);
			System.out.println("test port: " + port);
			// ======================= eprt

//			String resControl;
//			String resData;
//			String cmd = EPRT_command();
			//
//			String[] splitArgs = cmd.split("\\|");
//			int port = Integer.parseInt(splitArgs[3]);

			// =================================
			sendControl(cmd);
			openDataConnectionActive(port);

			resControl = controlReader.readLine();
			textArea.append(resControl + "\n");
			if (resControl.startsWith("200")) {
				controlWriter.println("LOCSYN");
				resControl = controlReader.readLine();
				textArea.append(resControl + "\n");
//				System.out.println(resControl);
				if (resControl.startsWith("125")) {
					root.removeAllChildren();
					root_select.removeAllChildren();
					tree_select.setModel(new DefaultTreeModel(root_select));
					// ===============
					root = new DefaultMutableTreeNode(dataReader.readLine());
					// ==============
					buildTreeFromServer();
					resControl = controlReader.readLine();
					textArea.append(resControl + "\n");
					if (resControl.startsWith("226")) {
						closeDataConnectionActive();
					}
				}
			}

			expandFirstChildNodes(tree);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
