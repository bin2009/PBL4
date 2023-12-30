package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class XuLyServer implements Runnable {
	private static ServerSocket welcomeSocket;
	private int controlPort;
	static boolean serverRunning;
	private static JTextArea textArea;
	private ArrayList<String> listUserConnect = new ArrayList<String>();
	private boolean listUser = false;
	private static JTable table;
	private static DefaultTableModel tableModel;
	private static String DB_URL;
	private static String DB_USER;
	private static String DB_PASSWORD;
	private static JComboBox comboBox;
	
	private ArrayList<HandlerClient> clientHandlers = new ArrayList<>();
	private static List<Socket> connectedClients = new ArrayList<>();
	

	public XuLyServer(ServerSocket welcomeSocket, int controlPort, boolean serverRunning, JTextArea textArea,
			ArrayList<String> listUserConnect, boolean listUser, JTable table, DefaultTableModel tableModel,
			String dB_URL, String dB_USER, String dB_PASSWORD, JComboBox comboBox) {
		super();
		this.welcomeSocket = welcomeSocket;
		this.controlPort = controlPort;
		this.serverRunning = serverRunning;
		this.textArea = textArea;
		this.listUserConnect = listUserConnect;
		this.listUser = listUser;
		this.table = table;
		this.tableModel = tableModel;
		DB_URL = dB_URL;
		DB_USER = dB_USER;
		DB_PASSWORD = dB_PASSWORD;
		this.comboBox = comboBox;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			welcomeSocket = new ServerSocket(controlPort);
			ServerFTP.showAccountList();
			ServerFTP.setCBB(comboBox);
//			showAccountList();
//			setCBB(comboBox);
		} catch (IOException e) {
			System.out.println("Could not create server socket");
			System.exit(-1);
		}

		System.out.println("FTP Server started listening on port " + controlPort);
		int noOfThreads = 0;
		while (serverRunning) {
			try {
				Socket client = welcomeSocket.accept();
				connectedClients.add(client);
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
				
				// Thêm handler mới vào danh sách
                clientHandlers.add(w);
			} catch (IOException e) {
				System.out.println("Exception encountered on accept");
				e.printStackTrace();
			}
		}
		
//		ServerFTP.
		try {
			welcomeSocket.close();
			System.out.println("Server was stopped");
		} catch (IOException e) {
			System.out.println("Problem stopping server");
			System.exit(-1);
		}
	}
	
//	public void stopServer() {
//        try {
//            serverRunning = false;
//
//            // Đóng tất cả các kết nối client
//            for (HandlerClient handler : clientHandlers) {
////                handler.close();
////            	handler.DeleteUser();
//            	handler.close();
//            }
//
//            // Đóng ServerSocket
//            welcomeSocket.close();
//            welcomeSocket = null;
//
////            // Đợi cho luồng server kết thúc
////			if (Thread.currentThread() != starThread) {
////				starThread.join();
////			}
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
	
	public static void stopServer() {
        try {
            serverRunning = false;

            // Đóng tất cả các kết nối hiện tại
            for (Socket clientSocket : connectedClients) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            // Đóng ServerSocket
            if (welcomeSocket != null && !welcomeSocket.isClosed()) {
            	welcomeSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

	private static void setCBB(JComboBox<String> cbbGroup) {
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
}
