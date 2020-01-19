package OMOK;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ClientThread extends Thread {
	private Socket socket;
	private ObjectInputStream in;
	private PrintWriter out;
	private Board board;
	//public int[][] board;
	
	private Scanner sc;
	
	public ClientThread(Socket socket) {
		
		sc = new Scanner(System.in);
		this.socket = socket;
		try {
			in = new ObjectInputStream (socket.getInputStream());
			out = new PrintWriter((socket.getOutputStream()));	//문자열만 보냄. "GAME:x:y:stone"
		}catch(IOException e) {e.printStackTrace();}
	}
	
//	public void printBoard() {
//		for (int i = 0; i < 10; i++) {
//			//System.out.println(Arrays.toString(board));
//			for (int j = 0; j < 10; j++) {
//				System.out.print(board[i][j] + " ");
//			}
//			System.out.println();
//		}
//	}
	
	@Override
	public void run() {
		try {
			
			System.out.println("test1");
			out.println("START");
			out.flush();
			while(true) {
				board=(Board)in.readObject();
				//board=(int[][])in.readObject();
				//in.reset();
				System.out.println("test2 직렬화된 보드를 받았습니다.");
				board.updateBoard();
				//printBoard();
				System.out.print("x:y:color형태로 입력하세요 : ");
				System.out.println("test3");
				String request = sc.nextLine();
				System.out.println("test4");
				out.println("GAME:"+request);//flush를 포함하고 있는 메소드. 바로 전송됨.
				//out.println("GAME:1:1:1");
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.close();
		}
	}
}
