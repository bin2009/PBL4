package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class HandlerClient extends Thread {

	private boolean debugMode = true;

	private enum transferType {
		ASCII, BINARY
	}

	private enum userStatus {
		NOTLOGGEDIN, ENTEREDUSERNAME, LOGGEDIN
	}

	private String ipAddressSYN;
	private int portSYN;

	// Path information
	private String root;
	private String currDirectory;
	private String path;
	private String currDirectory_refresh = null;
	private String fileSeparator = "/";

	// control connection
	private Socket controlSocket;
	private PrintWriter controlOutWriter;
	private BufferedReader controlIn;
	// database connection
	private static final String DB_URL = "jdbc:mySQL://localhost:3306/pbl4_ftp";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	// data Connection
	private ServerSocket dataSocket;
	private Socket dataConnection;
	private PrintWriter dataOutWriter;

	private int dataPort;
	private transferType transferMode = transferType.BINARY;

	// data syn
	private Socket dataSocketSYN;
	private PrintWriter dataWriterSYN;

	// user properly logged in?
	private userStatus currentUserStatus = userStatus.NOTLOGGEDIN;

	private boolean quitCommandLoop = false;
	private boolean listUser;
//	private InetAddress ip;

	// ====================== Personal/Group
	private String namegroup = null;

	private File old;
	private String un = new String();
	private ServerFTP server;

	private static JTextArea area;
	private ArrayList<String> listUserConnect = new ArrayList<String>();

	public HandlerClient(Socket client, int dataPort, JTextArea area, ArrayList<String> listUserConnect,
			boolean listUser) {
		super();
		this.controlSocket = client;
		this.dataPort = dataPort;
		this.area = area;
		this.listUserConnect = listUserConnect;
		this.listUser = listUser;
//		this.currDirectory = System.getProperty("user.dir") + "\\test";
		this.currDirectory = "D:\\Server\\FTP\\";
//		this.currDirectory ="D:\\BKDN\\CNTT\\CNTT_Nam_3\\PBL\\";
		this.path = "D:\\Server\\FTP\\";
//		this.path = "D:\\BKDN\\CNTT\\CNTT_Nam_3\\PBL\\";
//		this.currDirectory = "D:\\BKDN\\CNTT\\CNTT_Nam_3\\DienToanDamMay1";
//		try {
//			this.ip = InetAddress.getLocalHost();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		this.root = System.getProperty("user.dir");
	}

	/**
	 * Run method required by Java thread model
	 */
	public void run() {
		server = new ServerFTP("Hello");
		debugOutput("Current working directory " + this.currDirectory);
		try {
			// Input from client
			controlIn = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));

			// Output to client, automatically flushed after each print
			controlOutWriter = new PrintWriter(controlSocket.getOutputStream(), true);

			// Greeting
			sendMsgToClient("220 Welcome to the FTP-Server");

			// Get new command from client
			while (!quitCommandLoop) {
				executeCommand(controlIn.readLine());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Clean up
			try {
				controlIn.close();
				controlOutWriter.close();
				controlSocket.close();
				DeleteUser();
				debugOutput("Sockets closed and worker stopped");
			} catch (IOException e) {
				e.printStackTrace();
				debugOutput("Could not close sockets");
			}
		}

	}


	private void executeCommand(String c) {
		System.out.println("\n" + c);
		// split command and arguments
		int index = c.indexOf(' ');
		String command = ((index == -1) ? c.toUpperCase() : (c.substring(0, index)).toUpperCase());
		String args = ((index == -1) ? null : c.substring(index + 1));

		debugOutput("Command: " + command + " Args: " + args);

		// dispatcher mechanism for different commands
		switch (command) {
		case "USER":
			handleUser(args);
			break;

		case "PASS":
			handlePass(args);
			break;

//		case "SIGNUP":
//			handleSignup(args);
//			break;

		case "INFO":
			handleInfo();
			break;

		case "EDIT":
			handleEdit(args);
			if (listUser) {
				ServerFTP.showAccountList();
//				AccountManager.showAccountList();
			}
			break;

		case "CHANGE":
			handleChange(args);
//			AccountManager accountManager = new AccountManager();
//			accountManager.showAccountList();
			if (listUser) {
				ServerFTP.showAccountList();
//				AccountManager.showAccountList();
			}
			break;

		case "CWD": // đổi tên thư mục
			handleCd(args);
			break;

		case "REMOVE":
			handleRemove(args);
			break;

		case "DELE":
			handleDelete(args);
			break;

		case "RMDIR":
			handleDeleteFolder(args);
			break;

		case "LOC":
			handleNlst_loc(args);
			break;

		case "LOCSYN":
			handleLocSYN(args);
			break;

		case "LIST": // kiểm tra đã thiết lập kết nối chưa, sau đó gửi tệp / thư mục
			handleNlst(args);
			break;

		case "NLST":
			handleNlst(args);
			break;

		case "PWD":
		case "XPWD": // gửi tên thư mục làm việc hiện tại
			handlePwd();
			break;

		case "QUIT":
			handleQuit();
			break;

		case "PASV":
			handlePasv();
			break;

		case "EPSV":
			handleEpsv();
			break;

		case "SYST":
			handleSyst();
			break;

		case "FEAT":
			handleFeat();
			break;

		case "PORT":
			handlePort(args);
			break;

		case "SYN":
			handleSYN(args);
			break;

		case "EPRT":
			handleEPort(args);
			break;

		case "RETR":
			handleRetr(args);
			ServerFTP.refreshTree();
//			XuLy.refreshTree(namegroup);
			break;

		case "DOWLOAD_FOLDER":
			handleDowload_folder(args);
			break;

		case "DOWLOAD":
			handleDowload(args);
			break;

		case "MKD":
		case "XMKD":
			handleMkd(args);
			ServerFTP.refreshTree();
//			XuLy.refreshTree(namegroup);
			break;

		case "RMD":
		case "XRMD":
			handleDeleteFolder(args);
//			handleRmd(args);
			break;

		case "TYPE":
			handleType(args);
			break;

		case "STOR":
			handleStor(args);
			ServerFTP.refreshTree();
//			XuLy.refreshTree(namegroup);
			break;

		case "STOR_FOLDER":
			handleStor_folder(args);
			ServerFTP.refreshTree();
//			XuLy.refreshTree(namegroup);
			break;

		case "RNFR":
			handleRename(args);
			ServerFTP.refreshTree();
//			XuLy.refreshTree(namegroup);
			break;

		case "RNTO":
			handleRNTO(args);
			ServerFTP.refreshTree();
//			XuLy.refreshTree(namegroup);
			break;

		case "CLOSE":
			handleClose();
			break;

		case "PERSONAL":
			handlePersonal();
			break;

		case "COMPANY":
			handleCompany();
			break;

		case "COMPANY_GROUP":
			handleCompanyGroup(args);
			break;

		case "REFRESH":
			XuLy.refreshTree(namegroup);
			break;
			
		
		default:
			sendMsgToClient("501 Unknown command");
			break;

		}

	}

	private void handleCompany() {
		// TODO Auto-generated method stub
		try {
			int iduser = 0;
			String respone = "";
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			// lấy ra iduser từ username
			String query = "SELECT * FROM information WHERE username = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, un);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				iduser = rs.getInt("iduser");
			}
			pst.close();
			// lấy ra idgroup mà user kết nối
			String query2 = "SELECT * FROM employeegroup WHERE iduser = ?";
			PreparedStatement pst2 = conn.prepareStatement(query2);
			pst2.setInt(1, iduser);
			ResultSet rs2 = pst2.executeQuery();
			while (rs2.next()) {
				int idgroup = rs2.getInt("idgroup");
				// lấy ra name group
				String query3 = "SELECT * FROM groupofcompany WHERE idgroup = ?";
				PreparedStatement pst3 = conn.prepareStatement(query3);
				pst3.setInt(1, idgroup);
				ResultSet rs3 = pst3.executeQuery();
				if (rs3.next()) {
					String namegroup = rs3.getString("namegroup");
					respone += namegroup + "/";
				}
				pst3.close();
			}
			respone += "stop";
			pst2.close();
			conn.close();
			sendMsgToClient("COMPANY " + respone);
		} catch (SQLException e) {
			// TODO: handle exception
			sendMsgToClient("FAILED");
		}
	}

	private void handlePersonal() {
		// ACCOUNT PERSONAL
//		currDirectory = path + File.separator + un;
		currDirectory = path + un;
		namegroup = null;
		sendMsgToClient("PERSONAL");
	}

	private void handleCompanyGroup(String args) {
		currDirectory_refresh = args;
		currDirectory = path + args;
		namegroup = args;
		sendMsgToClient("COMPANY_GROUP");
	}

	public void handleRefresh(String name) {
//		openDataSYN(ipAddressSYN, portSYN);
		System.out.println("test đồng bộ: " + name);
		currDirectory = path + currDirectory_refresh;
		sendSYN("refresh " + name);
//		sendSYN("refresh " + name + "," + un);
//		closeSYN();
	}

	private void handleChange(String args) {
		// CHANGE PASSWORD
		try {
			String[] array = args.split(",");
			// tìm người dùng theo iduser
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String query = "SELECT * FROM information WHERE iduser = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, array[0]);
			System.out.println(array[0]);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				// update password
				String username = rs.getString("username");
				System.out.println(username);
				String query2 = "UPDATE account SET password = ? WHERE username = ?";
				PreparedStatement pst2 = conn.prepareStatement(query2);
				pst2.setString(1, array[1]);
				System.out.println(array[1]);
				pst2.setString(2, username);
				int rs2 = pst2.executeUpdate();
				if (rs2 > 0) {
					sendMsgToClient("230 Change password sucessfully");
				} else {
					sendMsgToClient("530 Change password failed");
				}
				pst2.close();
			}
			pst.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void handleEdit(String args) {
		// EDIT INFOMATION
		String[] array = args.split(",");
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String query = "UPDATE information SET name = ?, sdt = ?, email = ? WHERE iduser = ?";

			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, array[1]);
			pstmt.setString(2, array[2]);
			pstmt.setString(3, array[3]);
			pstmt.setString(4, array[0]);

			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				sendMsgToClient("230 Update account sucessfully");
			} else {
				sendMsgToClient("530 Update account failed");
			}

			conn.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void handleInfo() {
		// TODO Auto-generated method stub
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//			String query = "SELECT * FROM account WHERE username = ?";
			String query = "SELECT ifo.iduser, ifo.name, ifo.sdt, ifo.email, ifo.username, ac.password "
					+ "FROM information ifo " + "JOIN account ac "
					+ "WHERE ifo.username = ac.username and ifo.username = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, un);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String iduser = rs.getString("ifo.iduser");
				String name = rs.getString("ifo.name");
				String sdt = rs.getString("ifo.sdt");
				String email = rs.getString("ifo.email");
				String username = rs.getString("ifo.username");
				String password = rs.getString("ac.password");
				Personal personal = new Personal(iduser, name, sdt, email, username, password);
				sendMsgToClient(personal.toString());
			} else {

			}

			conn.close();
		} catch (Exception e) {
		}
	}

	private void handleClose() {
		// TODO Auto-generated method stub
		try {
			controlSocket.close();
			listUserConnect.remove(un);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void stopServer() {
		try {
			sendMsgToClient("SYN STOP");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void DeleteUser() {
		listUserConnect.remove(un);
//		area.append("User: " + un + " Disconnected!\n");
	}

//	private void handleSignup(String args) {
//		// kiểm tra user và pass đã có chưa
//		String[] array = args.split("/");
//		String name = array[0];
//		String sdt = array[1];
//		String email = array[2];
//		String username = array[3];
//		String password = array[4];
//		try {
//			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
////			String query = "SELECT * FROM account WHERE username = ?";
////			PreparedStatement pstmt = conn.prepareStatement(query);
////			pstmt.setString(1, username);
////			ResultSet rs = pstmt.executeQuery();
////			
////			if(rs.next()) {
////				sendMsgToClient("Register Failed!");
////			} else {
////				String queryAdd = "INSERT INTO account (id ,username, password) VALUES (?, ?)";
////				PreparedStatement pstmtAdd = conn.prepareStatement(query);
////				pstmtAdd.setString(1, username);
////				pstmtAdd.setString(2, password);
////				sendDataMsgToClient("Sign Up Success!");
////			}
//
//			String queryAdd = "INSERT INTO account (name, sdt, email, username, password, status) VALUES (?, ?, ?, ?, ?, ?)";
//			PreparedStatement pstmtAdd = conn.prepareStatement(queryAdd);
//			pstmtAdd.setString(1, name);
//			pstmtAdd.setString(2, sdt);
//			pstmtAdd.setString(3, email);
//			pstmtAdd.setString(4, username);
//			pstmtAdd.setString(5, password);
//			pstmtAdd.setInt(6, 0);
//			int rowsInserted = pstmtAdd.executeUpdate();
//
//			if (rowsInserted > 0) {
//				sendMsgToClient("Sign Up Success!");
////				AccountManager.showAccountList();
//			} else {
//				sendMsgToClient("Register Failed!");
//			}
////			controlSocket.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}

	private void handleRemove(String args) {
		// TODO Auto-generated method stub
		String path = currDirectory + File.separator + args;
		File f = new File(path);

		if (f.isFile()) {
			handleDelete(args);
		} else if (f.isDirectory()) {
			handleDeleteFolder(args);
		} else {
			// xử lý
		}

	}

	private void handleMove(String args) {
		// TODO Auto-generated method stub
		String[] array = args.split(File.separator);

		for (int i = 1; i < array.length; i++) {
			handleCd(array[i]);
		}
	}

	private void sendMsgToClient(String msg) {
		controlOutWriter.println(msg);
	}

	private void sendMsgToClient1(int msg) {
		controlOutWriter.print(msg);
	}

//	public void sendSYN(String msg) {
//		dataWriterSYN.println(msg);
//	}
	
	public void sendSYN(String msg) {
		// opendata -> send -> close
		try {
			dataSocketSYN = new Socket(ipAddressSYN, portSYN);
			dataWriterSYN = new PrintWriter(dataSocketSYN.getOutputStream(), true);
			debugOutput("Data SYN established");
		} catch (IOException e) {
			debugOutput("Could not connect to client data socket SYN");
			e.printStackTrace();
		}
		
		dataWriterSYN.println(msg);
		
		try {
			dataWriterSYN.close();
			dataSocketSYN.close();
			dataWriterSYN = null;
			dataSocketSYN = null;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	
	private void sendDataMsgToClient(String msg) {
		if (dataConnection == null || dataConnection.isClosed()) {
			sendMsgToClient("425 No data connection was established");
			debugOutput("Cannot send message, because no data connection is established");
		} else {
			dataOutWriter.print(msg + '\r' + '\n');
		}

	}

	private void sendDataMsgToClient1(int msg) {
		if (dataConnection == null || dataConnection.isClosed()) {
			sendMsgToClient("425 No data connection was established");
			debugOutput("Cannot send message, because no data connection is established");
		} else {
			dataOutWriter.print(msg + '\r' + '\n');
		}

	}

	private void openDataSYN(String ip, int port) {
		try {
			dataSocketSYN = new Socket(ip, port);
			dataWriterSYN = new PrintWriter(dataSocketSYN.getOutputStream(), true);
			debugOutput("Data SYN established");
		} catch (IOException e) {
			debugOutput("Could not connect to client data socket SYN");
			e.printStackTrace();
		}
	}

	
	private void openDataConnectionPassive(int port) {

		try {
			dataSocket = new ServerSocket(port);
			dataConnection = dataSocket.accept();
			dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);
			debugOutput("Data connection - Passive Mode - established");

		} catch (IOException e) {
			debugOutput("Could not create data connection.");
			e.printStackTrace();
		}

	}


	private void openDataConnectionActive(String ipAddress, int port) {
		try {
			dataConnection = new Socket(ipAddress, port);
			dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);
			debugOutput("Data connection - Active Mode - established");
		} catch (IOException e) {
			debugOutput("Could not connect to client data socket");
			e.printStackTrace();
		}
	}

	private void openDataConnectionActive_test(String ipAddress, int port) {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			System.out.println(ip);
			dataConnection = new Socket(ipAddress, port, ip, 9013);

//			System.out.println(dataConnection.getLocalPort());
//			System.out.println(dataConnection.getPort());
//			dataConnection = new Socket(ipAddress, port);
			dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);
			debugOutput("Data connection - Active Mode - established");
		} catch (IOException e) {
			debugOutput("Could not connect to client data socket");
			e.printStackTrace();
		}
	}

	
	private void closeDataConnection() {
		try {
			dataOutWriter.close();
			dataConnection.close();
			if (dataSocket != null) {
				dataSocket.close();
			}

			debugOutput("Data connection was closed");
		} catch (IOException e) {
			debugOutput("Could not close data connection");
			e.printStackTrace();
		}
		dataOutWriter = null;
		dataConnection = null;
		dataSocket = null;
	}

	private void closeSYN() {
		try {
			dataWriterSYN.close();
			dataSocketSYN.close();
			debugOutput("SYN connection was closed");
		} catch (IOException e) {
			debugOutput("Could not close data connection");
			e.printStackTrace();
		}
		dataWriterSYN = null;
		dataSocketSYN = null;
	}

	
	public boolean authenticateUser(String username) {
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String query = "SELECT * FROM account WHERE username = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

//	private void handleUser(String username) {
//
//		if (authenticateUser(username) && server.checkConnected(username)) {
//			sendMsgToClient("331 User name okay, need password");
//			currentUserStatus = userStatus.ENTEREDUSERNAME;
//			un = username;
//			listUserConnect.add(username);
//		} else if (currentUserStatus == userStatus.LOGGEDIN && !server.checkConnected(username)) {
//			sendMsgToClient("530 User already logged in");
//		} else {
//			sendMsgToClient("m");
//		}
//	}

	private void handleUser(String username) {
		// Kiểm tra account đã được active chưa
//		if (checkAccount(username)) {
//			
//		} else {
//			sendMsgToClient("530 Unverified Account");
//		}

		if (listUserConnect.contains(username)) {
			sendMsgToClient("530 User already logged in");
			quitCommandLoop = true;
		} else {
			if (authenticateUser(username)) {
				sendMsgToClient("331 User name okay, need password");
				currentUserStatus = userStatus.ENTEREDUSERNAME;
				un = username;
				listUserConnect.add(username);
			} else {
				sendMsgToClient("530 User Not Found!");
			}
		}
	}

	
	public boolean authenticatePassword(String password) {
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			String query = "SELECT * FROM account WHERE username = ? and password = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, un);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

//	private boolean checkAccount(String username) {
//		try {
//			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//			String query = "SELECT * FROM account WHERE username = ? AND status = ?";
//			PreparedStatement pstmt = conn.prepareStatement(query);
//			pstmt.setString(1, username);
//			pstmt.setString(2, "1");
//			ResultSet rs = pstmt.executeQuery();
//
//			return rs.next();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

	private void handlePass(String password) {
//		if(currentUserStatus == userStatus.ENTEREDUSERNAME && !authenticatePassword(password)) {
//			sendMsgToClient("530 WRONG PASSWORD");
//			try {
//				controlSocket.close();
//				Thread.currentThread().interrupt();
//			} catch (IOException e) {
//			}
//		}
		// User has entered a valid username and password is correct
		if (currentUserStatus == userStatus.ENTEREDUSERNAME && authenticatePassword(password)) {
			currentUserStatus = userStatus.LOGGEDIN;
			sendMsgToClient("230-Welcome to FTP SERVER");
			sendMsgToClient("230 User logged in successfully");
			server.addConnectedUsers(un);
			listUser = true;
			area.append("User " + un + " Connected!\n");
			currDirectory = path + un + File.separator;
		}

		// User is already logged in
		else if (currentUserStatus == userStatus.LOGGEDIN) {
			sendMsgToClient("530 User already logged in");
			DeleteUser();
		}

		// Wrong password
		else {
			sendMsgToClient("530 Not logged in");
			DeleteUser();
		}

	}

	private void handleCwd(String args) {
		String filename = currDirectory;

		// go one level up (cd ..)
		if (args.equals("..")) {
			int ind = filename.lastIndexOf(fileSeparator);
			if (ind > 0) {
				filename = filename.substring(0, ind);
				debugOutput(filename);
			}
		}

		// if argument is anything else (cd . does nothing)
		else if ((args != null) && (!args.equals("."))) {
			filename = filename + File.separator + args;
		}

		// check if file exists, is directory and is not above root directory
		File f = new File(filename);

		if (f.exists() && f.isDirectory()) {
			currDirectory = filename;
			sendMsgToClient("250 The current directory has been changed to " + currDirectory);
		} else {
			sendMsgToClient("550 Requested action not taken. File unavailable.");
		}
	}

	private void handleCd_loc(String args) {
		String fileName = currDirectory;

		if (args.equals("..")) {
			int ind = fileName.lastIndexOf(File.separator);
			if (ind > 0) {
				fileName = fileName.substring(0, ind);
				debugOutput(fileName);
			}
		} else if ((args != null) && (!args.equals("."))) {
			fileName = fileName + File.separator + args;
		}

		File f = new File(fileName);

		if (f.exists() && f.isDirectory()) {
			currDirectory = fileName;
			sendMsgToClient("250 The current directory has been changed to " + currDirectory);
		} else {
			sendMsgToClient("550 Requested action not taken. File unavailable.");
		}
	}

	private void handleCd(String args) {
		String fileName = currDirectory;

		if (args.equals("..")) {
			int ind = fileName.lastIndexOf(File.separator);
			if (ind > 0) {
				fileName = fileName.substring(0, ind);
//				fileName = fileName.substring(0, ind) + File.separator;
				debugOutput(fileName);
			}
		} else if ((args != null) && (!args.equals("."))) {
//			fileName = fileName + File.separator + args;
////			fileName = fileName +  args;
//			debugOutput(fileName);
			
			if(fileName.endsWith(File.separator)) {
				fileName = fileName +  args;
			} else {
				fileName = fileName + File.separator + args;
			}
			debugOutput(fileName);
		}

		File f = new File(fileName);

		if (f.exists() && f.isDirectory()) {
			currDirectory = fileName;
			sendMsgToClient("250 The current directory has been changed to " + currDirectory);
		} else {
			sendMsgToClient("550 Requested action not taken. File unavailable.");
		}
	}

	
	private void handleNlst(String args) {
		if (dataConnection == null || dataConnection.isClosed()) {
			sendMsgToClient("425 No data connection was established");
		} else {

			String[] dirContent = nlstHelper(args);

			if (dirContent == null) {
				sendMsgToClient("550 File does not exist.");
			} else {
				sendMsgToClient("125 Opening ASCII mode data connection for file list.");

				for (int i = 0; i < dirContent.length; i++) {
					sendDataMsgToClient(dirContent[i]);
//					sendMsgToClient1(i);
				}
				sendDataMsgToClient("");

				sendMsgToClient("226 Transfer complete.");
				closeDataConnection();

			}

		}

	}

	private void handleNlst_loc(String args) {
		if (dataConnection == null || dataConnection.isClosed()) {
			sendMsgToClient("425 No data connection was established");
		} else {
			// mảng file name
			String[] dirContent = nlstHelper_loc(args);

			if (dirContent == null) {
				sendMsgToClient("550 File does not exist.");
			} else {
				sendMsgToClient("125 Opening ASCII mode data connection for file list.");

				// ==============
				if (namegroup == null) {
					sendDataMsgToClient(un);
				} else {
					sendDataMsgToClient(namegroup);
				}
				// ===============

				for (int i = 0; i < dirContent.length; i++) {
					sendDataMsgToClient(dirContent[i]);
				}
				sendDataMsgToClient("5 end");
				sendMsgToClient("226 Transfer complete.");
				closeDataConnection();
			}
		}
	}

	private void handleLocSYN(String args) {
		if (dataConnection == null || dataConnection.isClosed()) {
			sendMsgToClient("425 No data connection was established");
		} else {
			// mảng file name
			String[] dirContent = nlstHelper_loc_syn(args);

			if (dirContent == null) {
				sendMsgToClient("550 File does not exist.");
			} else {
				sendMsgToClient("125 Opening ASCII mode data connection for file list.");

				// ==============
				if (namegroup == null) {
					sendDataMsgToClient(un);
				} else {
					sendDataMsgToClient(namegroup);
				}
				// ===============

				for (int i = 0; i < dirContent.length; i++) {
					sendDataMsgToClient(dirContent[i]);
				}
				sendDataMsgToClient("5 end");
				sendMsgToClient("226 Transfer complete.");
				closeDataConnection();
			}
		}
	}

	
	private String[] nlstHelper(String args) {
		// Construct the name of the directory to list.
		String filename = currDirectory;
		if (args != null) {
			filename = filename + fileSeparator + args;
		}

		// Now get a File object, and see if the name we got exists and is a
		// directory.
		File f = new File(filename);

		if (f.exists() && f.isDirectory()) {
			return f.list();
		} else if (f.exists() && f.isFile()) {
			String[] allFiles = new String[1];
			allFiles[0] = f.getName();
			return allFiles;
		} else {
			return null;
		}
	}

//	private String[] nlstHelper(String args) {
//		// Construct the path to the directory to list.
//		String path = currDirectory; // currentDirectory là thư mục hiện tại
//		if (args != null) {
//			path = path + File.separator + args;
//		}
//
//		// Now get a File object for the specified path.
//		File directory = new File(path);
//
//		if (directory.exists() && directory.isDirectory()) {
//			// Gọi phương thức đệ quy để lấy danh sách các tệp tin và thư mục bên trong
//			ArrayList<String> namesList = listFilesRecursive(directory, 0);
//			return namesList.toArray(new String[0]);
//		} else if (directory.exists() && directory.isFile()) {
//			return new String[] { directory.getName() };
//		} else {
//			return null;
//		}
//	}

	private String[] nlstHelper_loc(String args) {
		String path = currDirectory; // currentDirectory là đường dẫn thư mục hiện tại
		if (args != null) {
			path = path + File.separator + args;
		}

		File directory = new File(path);

		if (directory.exists() && directory.isDirectory()) {
			// Gọi phương thức đệ quy để lấy danh sách các tệp tin và thư mục bên trong
			ArrayList<String> namesList = listFilesRecursive_loc(directory, 1);
			return namesList.toArray(new String[0]);
		} else if (directory.exists() && directory.isFile()) {
			return null;
		} else {
			return null;
		}
	}

	private String[] nlstHelper_loc_syn(String args) {
		String filepath = path + namegroup + File.separator; // currentDirectory là đường dẫn thư mục hiện tại
		if (args != null) {
			filepath = filepath + File.separator + args;
		}

		File directory = new File(filepath);

		if (directory.exists() && directory.isDirectory()) {
			// Gọi phương thức đệ quy để lấy danh sách các tệp tin và thư mục bên trong
			ArrayList<String> namesList = listFilesRecursive_loc(directory, 1);
			return namesList.toArray(new String[0]);
		} else if (directory.exists() && directory.isFile()) {
			return null;
		} else {
			return null;
		}
	}

	private ArrayList<String> listFilesRecursive(File directory, int depth) {
		String prefix = "";
		for (int i = 0; i < depth; i++) {
			prefix += " ";
		}

		ArrayList<String> namesList = new ArrayList<>();
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					namesList.add(prefix + file.getName()); // Lấy tên tệp tin
				} else if (file.isDirectory()) {
					// Gọi đệ quy để lấy danh sách tệp tin và thư mục bên trong thư mục con
					ArrayList<String> subDirFiles = listFilesRecursive(file, depth + 1);
					namesList.add(prefix + file.getName()); // Thêm thư mục con vào danh sách
					namesList.addAll(subDirFiles); // Thêm các tệp con bên trong thư mục con
				}
			}
		}
		return namesList;
	}

	private ArrayList<String> listFilesRecursive_loc(File directory, int depth) {
		String prefix = "";
		for (int i = 0; i < depth; i++) {
			prefix += "  ";
		}

		ArrayList<String> namesList = new ArrayList<>();
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
//					namesList.add(prefix + depth + "  " + file.getName()); // Lấy tên tệp tin
				} else if (file.isDirectory()) {
					// Gọi đệ quy để lấy danh sách tệp tin và thư mục bên trong thư mục con
					ArrayList<String> subDirFiles = listFilesRecursive_loc(file, depth + 1);
					namesList.add(prefix + depth + "  " + file.getName()); // Thêm thư mục con vào danh sách
					namesList.addAll(subDirFiles); // Thêm các tệp con bên trong thư mục con
				}
			}
		}
		return namesList;
	}

	private void handlePort(String args) {
//		// Extract IP address and port number from arguments
//		String[] stringSplit = args.split(",");
//		String hostName = stringSplit[0] + "." + stringSplit[1] + "." + stringSplit[2] + "." + stringSplit[3];
//
//		int p = Integer.parseInt(stringSplit[4]) * 256 + Integer.parseInt(stringSplit[5]);
//
//		// Initiate data connection to client
//		openDataConnectionActive_test(hostName, p);
//		sendMsgToClient("200 Command OK");

		String[] splitArgs = args.split(",");

		if (splitArgs.length >= 4) {
			String ipAddress = splitArgs[0] + "." + splitArgs[1] + "." + splitArgs[2] + "." + splitArgs[3];
			System.out.println(ipAddress);
			try {
				int port = Integer.parseInt(splitArgs[4]) * 256 + Integer.parseInt(splitArgs[5]);
				System.out.println(port);
				// Initiate data connection to client
				openDataConnectionActive(ipAddress, port);
				sendMsgToClient("200 Command OK");

			} catch (NumberFormatException e) {
				sendMsgToClient("501 Invalid port number");
			}

		} else {
			sendMsgToClient("501 Invalid arguments");
		}
	}

	private void handleSYN(String args) {
		String[] splitArgs = args.split(",");

		if (splitArgs.length >= 4) {
//			String ipAddress = splitArgs[0] + "." + splitArgs[1] + "." + splitArgs[2] + "." + splitArgs[3];
			ipAddressSYN = splitArgs[0] + "." + splitArgs[1] + "." + splitArgs[2] + "." + splitArgs[3];

			System.out.println(ipAddressSYN);
			try {
//				int port = Integer.parseInt(splitArgs[4]) * 256 + Integer.parseInt(splitArgs[5]);
				portSYN = Integer.parseInt(splitArgs[4]) * 256 + Integer.parseInt(splitArgs[5]);
				System.out.println(portSYN);
				// Initiate data connection to client
//	ok			openDataSYN(ipAddressSYN, portSYN);
				sendMsgToClient("2009 SYN OK");
//				fcloseSYN();
			} catch (NumberFormatException e) {
				sendMsgToClient("2810 SYN NO");
			}

		} else {
			sendMsgToClient("501 Invalid arguments");
		}
	}

	
	private void handleEPort(String args) {
		final String IPV4 = "1";
		final String IPV6 = "2";

		String[] splitArgs = args.split("\\|");

		if (splitArgs.length >= 4) {
			String ipVersion = splitArgs[1];
			String ipAddress = splitArgs[2];

			if (IPV4.equals(ipVersion) || IPV6.equals(ipVersion)) {
				try {
					int port = Integer.parseInt(splitArgs[3]);

					// Initiate data connection to client
					openDataConnectionActive(ipAddress, port);
					sendMsgToClient("200 Command OK");
				} catch (NumberFormatException e) {
					sendMsgToClient("501 Invalid port number");
				}
			} else {
				sendMsgToClient("501 Unsupported IP version");
			}
		} else {
			sendMsgToClient("501 Invalid arguments");
		}
	}

	private void handlePwd() {
		sendMsgToClient("257 \"" + currDirectory + "\"");
	}

	
	private void handlePasv() {
		// Using fixed IP for connections on the same machine
		// For usage on separate hosts, we'd need to get the local IP address from
		// somewhere
		// Java sockets did not offer a good method for this
		String myIp;
		try {
			myIp = InetAddress.getLocalHost().getHostAddress();
			System.out.println(myIp);
//		String myIp = "192.168.1.14";
			String myIpSplit[] = myIp.split("\\.");
			int p1 = dataPort / 256;
			int p2 = dataPort % 256;

//			int p1 = 135;
//			int p2 = 445;

			sendMsgToClient("227 Entering Passive Mode (" + myIpSplit[0] + "," + myIpSplit[1] + "," + myIpSplit[2] + ","
					+ myIpSplit[3] + "," + p1 + "," + p2 + ")");

			openDataConnectionPassive(dataPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void handleEpsv() {
		sendMsgToClient("229 Entering Extended Passive Mode (|||" + dataPort + "|)");
		openDataConnectionPassive(dataPort);
	}

	
	private void handleQuit() {
		sendMsgToClient("221 Closing connection");
		quitCommandLoop = true;
		area.append("User: " + un + " Disconnected!\n");
	}

	private void handleSyst() {
		sendMsgToClient("215 COMP4621 FTP Server Homebrew");
	}

	
	private void handleFeat() {
		sendMsgToClient("211-Extensions supported:");
		sendMsgToClient("211 END");
	}

	
	private void handleMkd(String args) {
		// Allow only alphanumeric characters
		if (args != null) {
			File dir = new File(currDirectory + fileSeparator + args);

			if (!dir.mkdir()) {
				sendMsgToClient("550 Failed to create new directory");
				debugOutput("Failed to create new directory");
			} else {
				sendMsgToClient("250 Directory successfully created");
			}
		} else {
			sendMsgToClient("550 Invalid name");
		}

	}

	
	private void handleRmd(String dir) {
		String filename = currDirectory;

		// only alphanumeric folder names are allowed
		if (dir != null) {
			filename = filename + fileSeparator + dir;

			// check if file exists, is directory
			File d = new File(filename);

			if (d.exists() && d.isDirectory()) {
				d.delete();

				sendMsgToClient("250 Directory was successfully removed");
			} else {
				sendMsgToClient("550 Requested action not taken. File unavailable.");
			}
		} else {
			sendMsgToClient("550 Invalid file name.");
		}

	}

	private void handleDelete(String dir) {
		// TODO Auto-generated method stub
		String filename = currDirectory;

		// only alphanumeric folder names are allowed
		if (dir != null) {
			filename = filename + fileSeparator + dir;
			debugOutput(filename);
			File d = new File(filename);
			if (d.exists()) {
				d.delete();
				sendMsgToClient("250 File was successfully removed");
			} else {
				sendMsgToClient("550 Requested action not taken. File unavailable.");
			}
		} else {
			sendMsgToClient("550 Invalid file name.");
		}
	}

	private void handleDeleteFolder(String dir) {
		String filename = currDirectory;

		// only alphanumeric folder names are allowed
		if (dir != null) {
			filename = filename + File.separator + dir;
			File d = new File(filename);
			if (d.exists()) {
				DELETE(d);
				sendMsgToClient("250 File was successfully removed");
			} else {
				sendMsgToClient("550 Requested action not taken. File unavailable.");
			}
		} else {
			sendMsgToClient("550 Invalid file name.");
		}
	}

	public void DELETE(File pathFileDelete) {
		if (pathFileDelete.isFile())
			pathFileDelete.delete();
		else {
			for (File file : pathFileDelete.listFiles()) {
				if (file.isFile())
					file.delete();
				else {
					DELETE(file);
				}
			}
			pathFileDelete.delete();
		}
	}

	
	private void handleType(String mode) {
		if (mode.toUpperCase().equals("A")) {
			transferMode = transferType.ASCII;
			sendMsgToClient("200 OK");
		} else if (mode.toUpperCase().equals("I")) {
			transferMode = transferType.BINARY;
			sendMsgToClient("200 OK");
		} else
			sendMsgToClient("504 Not OK");
		;

	}

	
	private void handleDowload(String args) {
		File f = new File(currDirectory + File.separator + args);

		if (f.isFile() && f.exists()) {
			sendMsgToClient("File");
//			handleRetr(args);
		} else if (f.isDirectory() && f.exists()) {
			sendMsgToClient("Folder");
		}

	}

	private void handleDowload_folder(String args) {

		sendMsgToClient("150 Opening binary mode data connection for requested folder " + args);

		loc(args);

//		sendMsgToClient("226 ");
	}

	private void loc(String args) {
		try {
			File folder = new File(currDirectory + File.separator + args);
			for (File f : folder.listFiles()) {
				sendMsgToClient("file " + f.getName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// đệ quy dowload

	private void folder(File folder) {
		// gửi tên folder
		sendMsgToClient("folder " + folder.getName());

		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				folder(f);
			} else {
				sendMsgToClient("file " + f.getName());
//				handleRetr(f.getName());
			}
		}
	}

	private void handleRetrFolder(File folder) {
		sendMsgToClient("folder " + folder.getName());

		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				handleRetrFolder(file); // Recursively handle subfolders
			} else {
				handleRetr(file.getName()); // Handle individual files
			}
		}
	}

	private void handleRetr(String file) {
		File f = new File(currDirectory + File.separator + file);

		if (!f.exists()) {
			sendMsgToClient("550 File does not exist");
		}

		else {

//			 Binary mode
			if (transferMode == transferType.BINARY) {
				BufferedOutputStream fout = null;
				BufferedInputStream fin = null;

				sendMsgToClient("150 Opening binary mode data connection for requested file " + f.getName());

				try {
					fout = new BufferedOutputStream(dataConnection.getOutputStream());
					fin = new BufferedInputStream(new FileInputStream(f));
				} catch (Exception e) {
					debugOutput("Could not create file streams");
				}

				debugOutput("Starting file transmission of " + f.getName());

				byte[] buf = new byte[1024];
				int bytesRead;
				try {
					while ((bytesRead = fin.read(buf)) != -1) {
						fout.write(buf, 0, bytesRead);
					}
				} catch (IOException e) {
					debugOutput("Could not read from or write to file streams");
					e.printStackTrace();
				}

				try {
					fin.close();
					fout.close();
				} catch (IOException e) {
					debugOutput("Could not close file streams");
					e.printStackTrace();
				}

				debugOutput("Completed file transmission of " + f.getName());

				sendMsgToClient("226 File transfer successful. Closing data connection.");

			}

			// ASCII mode
			else {
				sendMsgToClient("150 Opening ASCII mode data connection for requested file " + f.getName());

				BufferedReader rin = null;
				PrintWriter rout = null;

				try {
					rin = new BufferedReader(new FileReader(f));
					rout = new PrintWriter(dataConnection.getOutputStream(), true);

				} catch (IOException e) {
					debugOutput("Could not create file streams");
				}

				String s;

				try {
					while ((s = rin.readLine()) != null) {
						rout.println(s);
					}
				} catch (IOException e) {
					debugOutput("Could not read from or write to file streams");
					e.printStackTrace();
				}

				try {
					rout.close();
					rin.close();
				} catch (IOException e) {
					debugOutput("Could not close file streams");
					e.printStackTrace();
				}
				sendMsgToClient("226 File transfer successful. Closing data connection.");
			}

		}
		closeDataConnection();

	}

	
	private void handleStor(String file) {
		if (file == null) {
			sendMsgToClient("501 No filename given");
		} else {
			File f = new File(currDirectory + fileSeparator + file);

			if (f.exists()) {
				sendMsgToClient("550 File already exists");
			}

			else {

//				 Binary mode
				if (transferMode == transferType.BINARY) {
					BufferedOutputStream fout = null;
					BufferedInputStream fin = null;

					sendMsgToClient("150 Opening binary mode data connection for requested file " + f.getName());

					try {
						// create streams
						fout = new BufferedOutputStream(new FileOutputStream(f));
						fin = new BufferedInputStream(dataConnection.getInputStream());
					} catch (Exception e) {
						debugOutput("Could not create file streams");
					}

					debugOutput("Start receiving file " + f.getName());

					// write file with buffer
					byte[] buf = new byte[1024];
					int l = 0;
					try {
						while ((l = fin.read(buf, 0, 1024)) != -1) {
							fout.write(buf, 0, l);
						}
					} catch (IOException e) {
						debugOutput("Could not read from or write to file streams");
						e.printStackTrace();
					}

					// close streams
					try {
						fin.close();
						fout.close();
					} catch (IOException e) {
						debugOutput("Could not close file streams");
						e.printStackTrace();
					}

					debugOutput("Completed receiving file " + f.getName());

					sendMsgToClient("226 File transfer successful. Closing data connection.");

				}

//				// ASCII mode
				else {
					sendMsgToClient("150 Opening ASCII mode data connection for requested file " + f.getName());

					BufferedReader rin = null;
					PrintWriter rout = null;

					try {
						rin = new BufferedReader(new InputStreamReader(dataConnection.getInputStream()));
						rout = new PrintWriter(new FileOutputStream(f), true);

					} catch (IOException e) {
						debugOutput("Could not create file streams");
					}

					String s;

					try {
						while ((s = rin.readLine()) != null) {
							rout.println(s);
						}
					} catch (IOException e) {
						debugOutput("Could not read from or write to file streams");
						e.printStackTrace();
					}

					try {
						rout.close();
						rin.close();
					} catch (IOException e) {
						debugOutput("Could not close file streams");
						e.printStackTrace();
					}
					sendMsgToClient("226 File transfer successful. Closing data connection.");
				}

			}
			closeDataConnection();
//			XuLy.refreshTree(namegroup);	//ok
		}

	}

	private void handleStor_folder(String args) {

	}

	private void handleRename(String args) {
		try {
//			old = new File(currDirectory + File.separator + args);
//			sendMsgToClient("350 Requested file action pending further information.");

////			String resControl = controlIn.readLine();
////
////			File newFile = new File(currDirectory + File.separator + resControl);
////			boolean check = old.renameTo(newFile);
////			if (check)
////				sendMsgToClient("accept");
//			
			
			// kiểm tra lỗi
			File f = new File(currDirectory + File.separator + args);
			if(!f.exists()) {
				sendMsgToClient("550 File not found");
			} else {
				old = new File(currDirectory + File.separator + args);
				sendMsgToClient("350 Requested file action pending further information.");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void handleRNTO(String args) {
		File newFile = new File(currDirectory + File.separator + args);
		boolean check = old.renameTo(newFile);
		if (check)
			sendMsgToClient("250 Requested file action okay, completed");
	}

	
	private void debugOutput(String msg) {
		if (debugMode) {
			System.out.println("Thread " + this.getId() + ": " + msg);
		}
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

	public void cleanup() {
		try {
			// Đóng các đối tượng Input/Output
			if (controlOutWriter != null) {
				controlOutWriter.close();
			}
			if (controlIn != null) {
				controlIn.close();
			}
			if (dataOutWriter != null) {
				dataOutWriter.close();
			}

			// Đóng Socket control
			if (controlSocket != null && !controlSocket.isClosed()) {
				controlSocket.close();
			}

			// Đóng Socket data
			if (dataSocket != null && !dataSocket.isClosed()) {
				dataSocket.close();
			}

			// Đóng Socket dataSYN
			if (dataSocketSYN != null && !dataSocketSYN.isClosed()) {
				dataSocketSYN.close();
			}

			// Đóng ServerSocket data
			if (dataConnection != null && !dataConnection.isClosed()) {
				dataConnection.close();
			}

			// Đóng ServerSocket dataSYN
			if (dataSocketSYN != null && !dataSocketSYN.isClosed()) {
				dataSocketSYN.close();
			}

			// Thêm các đối tượng khác cần được đóng ở đây

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
