package OMOK;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CLIENT {
	private static final int PORT = 5001;
	private static Socket socket;
	
	public static void connectToServer() {
			String serverIP = "192.168.23.14";
			try {
				socket = new Socket(serverIP, PORT);
			}catch(IOException e) {
				System.out.println("서버에 연결하는데 문제가 발생했습니다.");
				e.printStackTrace();
				if(socket != null) try {socket.close();}catch(IOException i) {}
			}
	}
	
	public static void main(String[] args) {
		connectToServer();
		new ClientThread(socket).start();
	}
}
