package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextArea;

////public class XuLy implements Runnable {
////    private static ServerSocket welcomeSocket;
////    private int controlPort;
////    private boolean serverRunning;
////    private JTextArea textArea;
////    private ArrayList<String> listUserConnect = new ArrayList<>();
////    private boolean listUser = false;
////    private JComboBox comboBox;
////
////    private ArrayList<HandlerClient> clientHandlers = new ArrayList<>();
////    private List<Socket> connectedClients = new ArrayList<>();
////
////    private boolean isRunning = true;
////
////    public XuLy(ServerSocket welcomeSocket, int controlPort, boolean serverRunning, JTextArea textArea,
////            ArrayList<String> listUserConnect, boolean listUser, JComboBox comboBox) {
////        super();
////        this.welcomeSocket = welcomeSocket;
////        this.controlPort = controlPort;
////        this.serverRunning = serverRunning;
////        this.textArea = textArea;
////        this.listUserConnect = listUserConnect;
////        this.listUser = listUser;
////        this.comboBox = comboBox;
////    }
////
////    @Override
////    public void run() {
////        while (isRunning) {
////            startServer();
////        }
////    }
////
////    public void startServer() {
////        try {
////            welcomeSocket = new ServerSocket(controlPort);
////        } catch (IOException e) {
////            System.out.println("Could not create server socket");
////            System.exit(-1);
////        }
////
////        ServerFTP.showAccountList();
////        ServerFTP.setCBB(comboBox);
////        System.out.println("FTP Server started listening on port " + controlPort);
////        int noOfThreads = 0;
////        while (serverRunning == true && isRunning) {
////            try {
////                Socket client = welcomeSocket.accept();
////                connectedClients.add(client);
////                int dataPort = controlPort + noOfThreads + 1;
////                int data = 20;
////                System.out.println(dataPort);
////                HandlerClient w = new HandlerClient(client, dataPort, textArea, listUserConnect, listUser);
////                System.out.println("New connection received. Worker was created.");
////                noOfThreads++;
////                w.start();
////                clientHandlers.add(w);
////            } catch (IOException e) {
////                if (isRunning) {
////                    System.out.println("Exception encountered on accept");
////                    e.printStackTrace();
////                }
////            }
////        }
////        try {
////            welcomeSocket.close();
////            System.out.println("Server was stopped");
////        } catch (IOException e) {
////            if (isRunning) {
////                System.out.println("Problem stopping server");
////                e.printStackTrace();
////            }
////        }
////    }
////
////    public void stopServer() {
////        try {
////            serverRunning = false;
////            isRunning = false;
////
////            for (HandlerClient clientSocket : clientHandlers) {
////                try {
////                    clientSocket.sendSYN("stop");
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                }
////            }
////
////            if (welcomeSocket != null && !welcomeSocket.isClosed()) {
////                try {
////                    welcomeSocket.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////                welcomeSocket = null;
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////
////    public void stop() {
////        isRunning = false;
////    }
////}
//

public class XuLy implements Runnable {
    private ServerSocket welcomeSocket;
    private int controlPort;
    private boolean serverRunning;
    private JTextArea textArea;
    private ArrayList<String> listUserConnect = new ArrayList<String>();
    private boolean listUser = false;
    private JComboBox comboBox;

    private static ArrayList<HandlerClient> clientHandlers = new ArrayList<>();
    private List<Socket> connectedClients = new ArrayList<>();

    private boolean isRunning = true;

    public XuLy(int controlPort, boolean serverRunning, JTextArea textArea,
            ArrayList<String> listUserConnect, boolean listUser, JComboBox comboBox) {
        super();
        this.controlPort = controlPort;
        this.serverRunning = serverRunning;
        this.textArea = textArea;
        this.listUserConnect = listUserConnect;
        this.listUser = listUser;
        this.comboBox = comboBox;
    }

    @Override
    public void run() {
        while (isRunning) {
            startServer();
        }
    }

    public void startServer() {
        try {
            welcomeSocket = new ServerSocket(controlPort);

            ServerFTP.showAccountList();
            ServerFTP.setCBB(comboBox);
            System.out.println("FTP Server started listening on port " + controlPort);
            int noOfThreads = 0;
            while (serverRunning == true) {
                try {
                    Socket client = welcomeSocket.accept();
                    connectedClients.add(client);
                    int dataPort = controlPort + noOfThreads + 1;
                    int data = 20;
                    System.out.println(dataPort);
                    HandlerClient w = new HandlerClient(client, dataPort, textArea, listUserConnect, listUser);
                    clientHandlers.add(w);
                    System.out.println("New connection received. Worker was created.");
                    noOfThreads++;
                    w.start();
                } catch (IOException e) {
                    System.out.println("Exception encountered on accept");
//                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Could not create server socket");
            e.printStackTrace();
        } finally {
            try {
                if (welcomeSocket != null && !welcomeSocket.isClosed()) {
                    welcomeSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        try {
            serverRunning = false;
            isRunning = false;

            for (HandlerClient clientSocket : clientHandlers) {
                try {
                    clientSocket.sendSYN("stop");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            welcomeSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isRunning = false;
    }
    
    public static void refreshTree(String namegroup) {
    	for (HandlerClient clientSocket : clientHandlers) {
            try {
                clientSocket.handleRefresh(namegroup);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
//
//
//
//public class XuLy implements Runnable {
//    private int controlPort;
//    private boolean serverRunning;
//    private JTextArea textArea;
//    private ArrayList<String> listUserConnect = new ArrayList<String>();
//    private boolean listUser = false;
//    private JComboBox comboBox;
//
//    private ArrayList<HandlerClient> clientHandlers = new ArrayList<>();
//    private List<Socket> connectedClients = new ArrayList<>();
//
//    private boolean isRunning = true;
//
//    public XuLy(int controlPort, boolean serverRunning, JTextArea textArea,
//            ArrayList<String> listUserConnect, boolean listUser, JComboBox comboBox) {
//        super();
//        this.controlPort = controlPort;
//        this.serverRunning = serverRunning;
//        this.textArea = textArea;
//        this.listUserConnect = listUserConnect;
//        this.listUser = listUser;
//        this.comboBox = comboBox;
//    }
//
//    @Override
//    public void run() {
//        while (isRunning) {
//            startServer();
//        }
//    }
//
//    public void startServer() {
//        try {
//            try (ServerSocket welcomeSocket = new ServerSocket(controlPort)) {
//                ServerFTP.showAccountList();
//                ServerFTP.setCBB(comboBox);
//                System.out.println("FTP Server started listening on port " + controlPort);
//                int noOfThreads = 0;
//                while (serverRunning == true) {
//                    try {
//                        Socket client = welcomeSocket.accept();
//                        if (!serverRunning) {
//                            break; // Exit the loop if serverRunning is false
//                        }
//                        connectedClients.add(client);
//                        int dataPort = controlPort + noOfThreads + 1;
//                        int data = 20;
//                        System.out.println(dataPort);
//                        HandlerClient w = new HandlerClient(client, dataPort, textArea, listUserConnect, listUser);
//                        System.out.println("New connection received. Worker was created.");
//                        noOfThreads++;
//                        w.start();
//                        clientHandlers.add(w);
//                    } catch (IOException e) {
//                        System.out.println("Exception encountered on accept");
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Could not create server socket");
//            e.printStackTrace();
//        }
//    }
//
////    public void stopServer() {
////        try {
////            serverRunning = false;
////            isRunning = false;
////
////            for (HandlerClient clientSocket : clientHandlers) {
////                try {
////                    clientSocket.sendSYN("stop");
////                } catch (Exception ex) {
////                    ex.printStackTrace();
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//    public void cleanup() {
//    	if (welcomeSocket != null && !welcomeSocket.isClosed()) {
//            welcomeSocket.close();
//        }
//    	
//        // Đóng tất cả các client socket và dừng tất cả các luồng
//        for (HandlerClient clientSocket : clientHandlers) {
//            try {
//                clientSocket.sendSYN("stop");
//                clientSocket.cleanup(); // Thêm một phương thức cleanup trong HandlerClient để giải phóng tài nguyên
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        clientHandlers.clear();
//        connectedClients.forEach(socket -> {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        connectedClients.clear();
//    }
//
//    
//    
//    public void stopServer() {
//        try {
//            serverRunning = false;
//            isRunning = false;
//            cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void stop() {
//        isRunning = false;
//    }
//}

//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.JComboBox;
//import javax.swing.JTextArea;
//
//public class XuLy implements Runnable {
//    private ServerSocket welcomeSocket;
//    private int controlPort;
//    private boolean serverRunning;
//    private JTextArea textArea;
//    private ArrayList<String> listUserConnect = new ArrayList<>();
//    private boolean listUser = false;
//    private JComboBox comboBox;
//
//    private ArrayList<HandlerClient> clientHandlers = new ArrayList<>();
//    private List<Socket> connectedClients = new ArrayList<>();
//
//    private boolean isRunning = true;
//
//    public XuLy(int controlPort, boolean serverRunning, JTextArea textArea,
//            ArrayList<String> listUserConnect, boolean listUser, JComboBox comboBox) {
//        super();
//        this.controlPort = controlPort;
//        this.serverRunning = serverRunning;
//        this.textArea = textArea;
//        this.listUserConnect = listUserConnect;
//        this.listUser = listUser;
//        this.comboBox = comboBox;
//    }
//
//    @Override
//    public void run() {
//        while (isRunning) {
//            startServer();
//        }
//    }
//
//    public void startServer() {
//        try {
//            welcomeSocket = new ServerSocket(controlPort);
//            ServerFTP.showAccountList();
//            ServerFTP.setCBB(comboBox);
//            System.out.println("FTP Server started listening on port " + controlPort);
//            int noOfThreads = 0;
//            while (serverRunning && isRunning) {
//                try {
//                    Socket client = welcomeSocket.accept();
//                    if (!serverRunning) {
//                        break; // Exit the loop if serverRunning is false
//                    }
//                    connectedClients.add(client);
//                    int dataPort = controlPort + noOfThreads + 1;
//                    int data = 20;
//                    System.out.println(dataPort);
//                    HandlerClient w = new HandlerClient(client, dataPort, textArea, listUserConnect, listUser);
//                    System.out.println("New connection received. Worker was created.");
//                    noOfThreads++;
//                    w.start();
//                    clientHandlers.add(w);
//                } catch (IOException e) {
//                    if (isRunning) {
//                        System.out.println("Exception encountered on accept");
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } catch (IOException e) {
//            if (isRunning) {
//                System.out.println("Could not create server socket");
//                e.printStackTrace();
//            }
//        } finally {
////            cleanup();
//        }
//    }
//
//    public void cleanup() {
//        try {
//            if (welcomeSocket != null && !welcomeSocket.isClosed()) {
//                welcomeSocket.close();
//            }
//
//            // Đóng tất cả các client socket và dừng tất cả các luồng
//            for (HandlerClient clientSocket : clientHandlers) {
//                try {
//                    clientSocket.sendSYN("stop");
//                    clientSocket.cleanup(); // Thêm một phương thức cleanup trong HandlerClient để giải phóng tài nguyên
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//            clientHandlers.clear();
//            connectedClients.forEach(socket -> {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            connectedClients.clear();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void stopServer() {
//        try {
//            serverRunning = false;
//            isRunning = false;
//            cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void stop() {
//        isRunning = false;
//    }
//}
