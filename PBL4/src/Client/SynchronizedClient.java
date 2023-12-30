//package Client;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.Collections;
//import java.util.Enumeration;
//import java.util.Random;
//
//import javax.swing.JFrame;
//
//public class SynchronizedClient implements Runnable {
//	private Socket socket;
//	private BufferedReader controlReader;
//	private PrintWriter controlWriter;
//	private int dataPort;
//	private String namegroup = null;
//	private String nameuser = null;
//
//	private ServerSocket dataSocket_Server;
//	private Socket dataSocket;
//	private BufferedReader dataReader;
//	private JFrame frame;
//
//	public SynchronizedClient(Socket socket, BufferedReader controlReader, PrintWriter controlWriter, JFrame frame) {
//		super();
//		this.socket = socket;
//		this.controlReader = controlReader;
//		this.controlWriter = controlWriter;
//		this.frame = frame;
//	}
//
//	@Override
//	public void run() {
//		try {
//			String cmd = PORT_command();
//			String[] array = cmd.split(",");
//			int port = Integer.parseInt(array[4]) * 256 + Integer.parseInt(array[5]);
//			System.out.println("đồng bộ port: " + port);
//
//			controlWriter.println(cmd);
//			openDataSYN(port);
//
//			String resControl;
//
//			resControl = controlReader.readLine();
//			if (resControl.startsWith("2009")) {
//				while (true) {
//					String dataSYN = dataReader.readLine();
//					System.out.println(dataSYN);
//					if (dataSYN.startsWith("stop")) {
//						Home.QUIT(frame);
//						closeDataSYN();
//					} else if(dataSYN.startsWith("refresh")) {
//						String res = dataSYN.substring(8);
//						if(res.equals(namegroup)) {
//							Home.EPRT_syn();
//						}
////						
//						
////						String res = dataSYN.substring(8);
////						String[] array2 = res.split(",");
////						if(array2[1].equals(namegroup) && !array2[2].equals(nameuser)) {
////							Home.EPRT_syn();
////						}
//					}
//
//				}
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//	
//	public void setNamegroup(String name) {
//		namegroup = name;
//	}
//	
//	public void setNameuser(String name) {
//		nameuser = name;
//	}
//
////	private void openDataSYN(int port) {
////		try {
////			dataSocket_Server = new ServerSocket(port);
//////			dataSocket_Server = new ServerSocket(port, backlog, bindAddr);
//////			dataSocket = new Socke
////			dataSocket = dataSocket_Server.accept();
////			System.out.println("đang nhận kết quả trả về từ server bằng cổng: " + dataSocket.getLocalPort());
////			System.out.println("đang nhận kết quả trả về từ server bằng cổng: " + dataSocket.getPort());
////			dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
//////			System.out.println("Data connection - Passive Mode - established");
////		} catch (IOException e) {
////			System.out.println("Could not create data connection.");
////			e.printStackTrace();
////		}
////	}
//	
//	private void openDataSYN(int port) {
//		try {
//			dataSocket_Server = new ServerSocket(port);
////			dataSocket_Server = new ServerSocket(port, backlog, bindAddr);
////			dataSocket = new Socke
//			while(true) {
//				Socket dataSYNSocket = dataSocket_Server.accept();
////				dataSocket = dataSocket_Server.accept();
//				System.out.println("đang nhận kết quả trả về từ server bằng cổng: " + dataSYNSocket.getLocalPort());
//				System.out.println("đang nhận kết quả trả về từ server bằng cổng: " + dataSYNSocket.getPort());
//				dataReader = new BufferedReader(new InputStreamReader(dataSYNSocket.getInputStream()));
////			System.out.println("Data connection - Passive Mode - established");
//			}
//		} catch (IOException e) {
//			System.out.println("Could not create data connection.");
//			e.printStackTrace();
//		}
//	}
//
//	private void closeDataSYN() {
//		try {
//			dataReader.close();
//			if (dataSocket != null) {
//				dataSocket.close();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dataReader = null;
//		dataSocket = null;
//	}
//
//	private String PORT_command() {
//		try {
//			String host = IP();
//			Random random = new Random();
//			dataPort = 1024 + random.nextInt(54000);
//			String[] ipParts = host.split("\\.");
//			int p1 = dataPort / 256;
//			int p2 = dataPort % 256;
//			String cmd = "SYN " + ipParts[0] + "," + ipParts[1] + "," + ipParts[2] + "," + ipParts[3] + "," + p1 + ","
//					+ p2;
//
////			String cmd = "PORT " + 10 + "," + 10 + "," + 58 + "," + 136 + "," + p1 + "," + p2;
//			return cmd;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return null;
//	}
//
//	private static String IP() {
//		try {
//			String ipv4 = null;
//			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
//			for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
//				String interfaceName = networkInterface.getDisplayName();
//				if (interfaceName.contains("Wireless") || interfaceName.contains("Wi-Fi")) {
//					for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
//						if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
//							ipv4 = inetAddress.getHostAddress();
//						}
//					}
//				}
//			}
//			return ipv4;
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return null;
//	}
//
//}



package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

import javax.swing.JFrame;

public class SynchronizedClient implements Runnable {
	private Socket socket;
	private BufferedReader controlReader;
	private PrintWriter controlWriter;
	private int dataPort;
	private String namegroup = null;
	private String nameuser = null;

	private ServerSocket dataSocket_Server;
	private Socket dataSocket;
	private JFrame frame;

	public SynchronizedClient(Socket socket, BufferedReader controlReader, PrintWriter controlWriter, JFrame frame) {
		super();
		this.socket = socket;
		this.controlReader = controlReader;
		this.controlWriter = controlWriter;
		this.frame = frame;
	}

	@Override
	public void run() {
		try {
			String cmd = PORT_command();
			String[] array = cmd.split(",");
			int port = Integer.parseInt(array[4]) * 256 + Integer.parseInt(array[5]);
			System.out.println("đồng bộ port: " + port);

			controlWriter.println(cmd);
//		ok	openDataSYN(port);

			String resControl;

			resControl = controlReader.readLine();
			System.out.println("đang chạy đồng bộ: "+ resControl);
			
			
			if (resControl.startsWith("2009")) {
				openDataSYN(port);
				
				
				
//				while (true) {
//					String dataSYN = dataReader.readLine();
//					System.out.println(dataSYN);
//					if (dataSYN.startsWith("stop")) {
//						Home.QUIT(frame);
//						closeDataSYN();
//					} else if(dataSYN.startsWith("refresh")) {
//						String res = dataSYN.substring(8);
//						if(res.equals(namegroup)) {
//							Home.EPRT_syn();
//						}
////						
//						
////						String res = dataSYN.substring(8);
////						String[] array2 = res.split(",");
////						if(array2[1].equals(namegroup) && !array2[2].equals(nameuser)) {
////							Home.EPRT_syn();
////						}
//					}
//
//				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void refreshGroup() {
		Home.EPRT_syn();
	}

	public void quit() {
		Home.QUIT(frame);
	}
	
	public void setNamegroup(String name) {
		namegroup = name;
	}
	
	public void setNameuser(String name) {
		nameuser = name;
	}

//	private void openDataSYN(int port) {
//		try {
//			dataSocket_Server = new ServerSocket(port);
////			dataSocket_Server = new ServerSocket(port, backlog, bindAddr);
////			dataSocket = new Socke
//			dataSocket = dataSocket_Server.accept();
//			System.out.println("đang nhận kết quả trả về từ server bằng cổng: " + dataSocket.getLocalPort());
//			System.out.println("đang nhận kết quả trả về từ server bằng cổng: " + dataSocket.getPort());
//			dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
////			System.out.println("Data connection - Passive Mode - established");
//		} catch (IOException e) {
//			System.out.println("Could not create data connection.");
//			e.printStackTrace();
//		}
//	}
	
	private void openDataSYN(int port) {
		try {
			dataSocket_Server = new ServerSocket(port);
			while(true) {
				Socket dataSYNSocket = dataSocket_Server.accept();
				System.out.println("đang nhận kết quả trả về từ server bằng cổng: " + dataSYNSocket.getLocalPort());
				System.out.println("đang nhận kết quả trả về từ server bằng cổng: " + dataSYNSocket.getPort());
				
				//gọi luồng xử lý sendSYN
				Thread th = new Thread(new XulySYN(namegroup, dataSYNSocket, this));
				th.start();
			}
		} catch (IOException e) {
			System.out.println("Could not create data connection.");
			e.printStackTrace();
		}
	}

	private String PORT_command() {
		try {
			String host = IP();
			Random random = new Random();
			dataPort = 1024 + random.nextInt(54000);
			String[] ipParts = host.split("\\.");
			int p1 = dataPort / 256;
			int p2 = dataPort % 256;
			String cmd = "SYN " + ipParts[0] + "," + ipParts[1] + "," + ipParts[2] + "," + ipParts[3] + "," + p1 + ","
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

}
