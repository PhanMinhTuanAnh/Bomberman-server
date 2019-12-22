

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
	private int port;
	public static ArrayList<Socket> listSocket;
	public static Socket SocketContact = null;
	public static Map<Integer, Boolean> isSendedMap;
	public static int userId = 0;
	public static boolean canStart = true;
	public static boolean isUserAccept = false;
	public static Connection cndb;
	
	public Server(int port) {
		this.port = port;
		
	}
	
	public void excute() throws IOException {
		ServerSocket server = new ServerSocket(port);
		
		while(true) {
			Socket socket1 = server.accept();
			System.out.println("Client " + socket1.getPort() + " Connected");
			Server.listSocket.add(socket1);
			Server.isSendedMap.put(socket1.getPort(), false);
			ReadServer read1 = new ReadServer(socket1, ++userId);

			read1.start();
		}
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Server Started ...");
		
		listSocket = new ArrayList<Socket>();
		isSendedMap = new HashMap<Integer, Boolean>();
		
		Server server = new Server(15790);
		server.excute();
	}
	
	public static boolean checkUser(String name, String password) throws SQLException {
		try {
			cndb = ConnectDatabase.getMySQLConnection();
			System.out.println("perfect");
		} catch (Exception e) {
			System.out.println("Error From My SQL");
		}
		
		Statement statement = cndb.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
		String query = "Select * from user";
		ResultSet rs = statement.executeQuery(query);
		System.out.println("123");

		while(rs.next()) {
			System.out.println(rs.getString("username"));
			System.out.println(rs.getString("password"));
			if(rs.getString("username").equalsIgnoreCase(name)) {
				if(rs.getString("password").equalsIgnoreCase(password))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	
}


class ReadServer extends Thread {
	int userId;
	int isDuo;
	Socket serveraccept;
	boolean isSendedMap = false;

	public ReadServer(Socket serveraccept, int userId) {
		this.serveraccept = serveraccept;
		this.userId = userId;
	}
	
	@SuppressWarnings("resource")
	@Override
	public void run() {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		FileInputStream fis = null;
		byte[]b = new byte[2002];
		OutputStream os = null;
		while(true) {
			try {
				dos = new DataOutputStream(serveraccept.getOutputStream());
				dis = new DataInputStream(serveraccept.getInputStream());
				String request = dis.readUTF();
				if(request.equalsIgnoreCase("check login")) {
					String name = dis.readUTF();
					String password = dis.readUTF();
					System.out.println("123");
					if(Server.checkUser(name, password))
						dos.writeUTF("OK");
					else
						dos.writeUTF("NO");
				}
				if(request.equalsIgnoreCase("get file")) {
					//Truyền map từ server
					fis =  new FileInputStream("D:\\JAvaws\\ServerBomberman\\res\\levels\\Level1.txt");
					fis.read(b, 0, b.length);
					os = serveraccept.getOutputStream();
					os.write(b, 0, b.length);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}




















