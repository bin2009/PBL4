package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class XulySYN implements Runnable {
	private String namegroup;
	private Socket socketSYN;
	private BufferedReader reader;
	private PrintWriter writer;
	private SynchronizedClient synchronizedClient;

	public XulySYN(String namegroup, Socket socketSYN, SynchronizedClient synchronizedClient) {
		super();
		this.namegroup = namegroup;
		this.socketSYN = socketSYN;
		this.synchronizedClient = synchronizedClient;
		try {
			reader = new BufferedReader(new InputStreamReader(socketSYN.getInputStream()));
			writer = new PrintWriter(socketSYN.getOutputStream());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String res = reader.readLine();
			System.out.println(namegroup);
			System.out.println(res);
//			if (res.equals(namegroup)) {
//				synchronizedClient.refreshGroup();
//			}

			if (res.startsWith("stop")) {
				synchronizedClient.quit();
				closeDataSYN();
			} else if (res.startsWith("refresh")) {
				String cmd = res.substring(8);
				if (cmd.equals(namegroup)) {
					synchronizedClient.refreshGroup();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void closeDataSYN() {
		try {
			reader.close();
			writer.close();
			socketSYN.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader = null;
		writer = null;
		socketSYN = null;
	}

}
