package OMOK;

import java.io.Serializable;



public class Board implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Board instance;
	private static final int COL = 10;
	private static final int ROW = 10;	
	private int[][] board = new int[ROW][COL]; //각 좌표마다 0일때 빈 공간, 1일때 흑, 2일때 백
	private int gameState = 0;	//0일때 게임 진행중, 1일때 게임 종료
	
	public Board() {}
	
	public static Board getInstance() {
		if(instance == null) {
			instance = new Board();
		}
		return instance;
	}
	
	public int[][] getBoard(){
		return board;
	}
	
	public int getGameState() {//GoModel 클래스에서 무한루프 탈출 플래그. 1이 반환되면 while 루프를 탈출한다.
		return gameState;
	}
	
	public void updateBoard() {
//		for (int i = 0; i < ROW; i++) {
//			for (int j = 0; j < COL; j++) {
//
//				System.out.print(board[i][j] + " ");
//
//			}
//
//			System.out.println();
//		}
		System.out.printf("   ┌");
		for (int i = 0; i < ROW; i++) {
			System.out.printf("─┬");
		}
		System.out.printf("─┐");
		System.out.println();
		for (int i = 0; i < COL; i++) {
			System.out.printf("%-3d", i);
			System.out.printf("├");
			for (int j = 0; j < ROW; j++) {
				if (board[i][j] == 1) {
					System.out.printf("─●");
				} else if (board[i][j] == 2) {
					System.out.printf("─○");
				} else {
					System.out.printf("─┼");
				}
			}
			System.out.printf("─┤");
			System.out.println();
		}
		System.out.printf("   └");
		for (int i =0; i < ROW; i++) {
			System.out.printf("─┴");
		}
		System.out.printf("─┘");
		System.out.println();
		System.out.printf("     ");
		for (int i = 0; i < ROW; i++) {
			System.out.printf("%d ", i);
		}
		System.out.println();
		
		// 상태체크: 흑의 승리
		if (horizontalCheck_Black() || diagCheck1_Black() || diagCheck2_Black() || verticalCheck_Black()) {
			System.out.println("BLACK WON");
			gameState = 1;
		}
		
		//상태체크: 백의 승리
		if(horizontalCheck_White()||diagCheck1_White()||diagCheck2_White()||verticalCheck_White()) {
			System.out.println("WHITE WON");
			gameState = 1;
		}
		
	}
	
	public void InsertStone(int x, int y, int stone){ //stone은 1일때 흑, 2일때 백
		if(board[y][x] == 0) {
			board[y][x] = stone;
		}
		else {
			System.out.println("이 자리에 돌을 둘 수 없습니다.");
		}
	}
	
	public  boolean horizontalCheck_Black() {
		int cnt = 0;
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				if (board[i][j] == 1) {
					cnt++;
				} else {
					cnt = 0;
				}
				if (cnt >= 5) {
					return true;
				}

			}
			cnt = 0;
		}
		return false;
	}

	public  boolean verticalCheck_Black() {
		int cnt = 0;
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW; j++) {
				if (board[j][i] == 1) {
					cnt++;
				} else {
					cnt = 0;
				}
				if (cnt >= 5) {
					return true;
				}
			}
			cnt = 0;
		}
		return false;
	}

	public  boolean diagCheck1_Black() { // '/'방향 체크
		int cnt = 0;
		int x;
		//int k = 2 * (ROW - 1);
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				x = i - j;
				if (x >= ROW || x < 0)
					continue;
				if (board[x][j] == 1) {

					cnt++;
				} else {
					cnt = 0;
				}
				
				
				if (cnt >= 5) {
					return true;
				}
			}

			cnt = 0;
		}
		return false;
	}

	public  boolean diagCheck2_Black() {	// '\'방향 체크
		int cnt = 0;

		int x;

		//int k = 2 * (ROW - 1);

		for (int i = ROW; i >= 0; i--) {

			for (int j = 0; j < COL;j++) {

				x = i + j;

				if (x >= ROW || x < 0)
					continue;
				if (board[x][j] == 1) {
					cnt++;
				}
				else {
					cnt = 0;
				}
				if(cnt >= 5) {
					return true;
				}

			}
			cnt = 0;


		}

		return false;
	}

	/**
	 * 백의 4방향 체크
	 * @return
	 */
	private  boolean verticalCheck_White() {
		int cnt = 0;
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW; j++) {
				if (board[j][i] == 2) {
					cnt++;
				} else {
					cnt = 0;
				}
				if (cnt >= 5) {
					return true;
				}
			}
			cnt = 0;
		}
		return false;

	}

	private boolean diagCheck2_White() {//	'\'방향 체크
		int cnt = 0;

		int x;

		//int k = 2 * (ROW - 1);

		for (int i = ROW; i >= 0; i--) {

			for (int j = 0; j < COL;j++) {

				x = i + j;

				if (x >= ROW || x < 0)
					continue;
				if (board[x][j] == 2) {
					cnt++;
				}
				else {
					cnt = 0;
				}
				if(cnt >= 5) {
					return true;
				}

			}
			cnt = 0;
		}

		return false;
	}

	private boolean diagCheck1_White() {	// '/'방향 체크
		int cnt = 0;
		int x;
		//int k = 2 * (ROW - 1);
		// int colCnt = 0;
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				x = i - j;
				//System.out.println("" + x + " " + j);
				if (x >= ROW || x < 0)
					continue;
				if (board[x][j] == 2) {
					 //board[x][j] = (char)(cnt++ + 'A');
					cnt++;
				} else {
					cnt = 0;
				}
				
				
				if (cnt >= 5) {
					return true;
				}
			}

			cnt = 0;
		}
		return false;
	}

	private boolean horizontalCheck_White() {
		int cnt = 0;
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				if (board[i][j] == 2) {
					cnt++;
				} else {
					cnt = 0;
				}
				if (cnt >= 5) {
					return true;
				}

			}
			cnt = 0;
		}
		return false;
	}
	
	
	
}
