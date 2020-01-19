package OMOK;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SERVER extends Thread{
	static List<Player> playerThreadList; //플레이어 객체를 담는 리스트. 
	//static으로 서버상에서는 1개의 보드만을 생성, 2명의 플레이어(클라이언트)가 공유한다
	
	public static void main(String[] args) {
		SERVER server = new SERVER();
		server.start();
	}
	
	public void run() {
		//playerThreadList = new ArrayList<Player>();
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(5001);
			System.out.println("서버를 켰습니다.");
			int numOfPlayers = 0;
			while(true) {
				if(numOfPlayers < 3) { //한번에 플레이어는 2명까지만 받음
					Player p1 = new Player(ss.accept());
					numOfPlayers+=1; //접속한 클라이언트의 수 갱신.
					System.out.println("플레이어"+numOfPlayers+"가 접속했습니다");
					p1.start();	
				}
			}
		}catch(Exception e) {e.printStackTrace();}
		finally {
			try {if(ss!=null) ss.close();}catch(Exception e) {}
		}
	}
	
}

class Player extends Thread{
	//Player opponent;
	Socket socket;
	BufferedReader in;
	ObjectOutputStream out;
	static Board board;
	static List<ObjectOutputStream> playerList = new ArrayList<ObjectOutputStream>();
	
	public Player(Socket socket) {
		System.out.println("test1 : 플레이어 스레드를 생성중...");
		this.socket = socket;
		board = Board.getInstance(); //보드에 싱글턴 패턴을 적용하여 1개의 보드객체를 공유

		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new ObjectOutputStream(socket.getOutputStream()); //보드를 통째로 직렬화해서 보냄
			playerList.add(out);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Player생성자에서 에러발생");
		}
	}
	
	
	public void run() {
		try {
			//board.updateBoard();
			while(true) {
				//System.out.println("test2");
				String request = in.readLine();
				System.out.println("test3 : x:y:color 형식의 문자열을 성공적으로 받았음");
				if(request == null) {	//한쪽 클라이언트가 꺼지면 InputStream이 NULL을 가지게 됨.
					printLog("Lost connection from the client...");
					//closeConnection(out);
					break;
				}
				board.updateBoard();
				//turnPrompt();
				
				//여기까지 왔다면 request는 값을 가지고 있다.
				String[] tokens = request.split(":"); //":" 기준으로 파싱한다
				System.out.println("test4 : (:) 기준으로 requst 값 파싱 성공");
				if("GAME".contentEquals(tokens[0])) {
					
					//request를 파싱한 결과로 보드를 업데이트 한다
					int x = Integer.parseInt(tokens[1]);
					int y = Integer.parseInt(tokens[2]);
					int stone = Integer.parseInt(tokens[3]);
					System.out.println("test5 : x,y,color 값: " + x + ", " + y + ", " + stone);
					board.InsertStone(x, y, stone);
					board.updateBoard();
				}
				//입력값을 받아 돌을 두었든 안두었든 현재 판을 플레이어들에게 브로드캐스트한다.
				//out.writeObject(board);//데이터 직렬화
				//out.writeUnshared(board.getBoard());
				//out.reset();
				broadcast(board);
				//out.flush();	//직렬화된 데이터 전달
				
				System.out.println("test6 : 직렬화한 보드를 보냈습니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {socket.close();}catch(IOException e) {}
		}
	}
	
	public void broadcast(Board b) throws Exception{ 
		//옵저버 패턴 적용: 게임상태가 변한 것을 클라이언트들에게 브로드캐스트로 알린다
        synchronized (playerList) {
            for(ObjectOutputStream p : playerList) {
				p.writeObject(board);//데이터 직렬화
				p.reset();	
            }
       }		
	}
	
	public void printLog(String log) {	//그냥 sysout을 메소드화 시켰음.
		System.out.println(log);
	}
	
}

