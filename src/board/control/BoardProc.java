package board.control;

import java.util.List;
import java.util.Scanner;

import board.Board;
import board.impl.BoardDao;
import board.impl.BoardServiceImpl;

public class BoardProc {
	Scanner sc = new Scanner(System.in);
	String id, pw, name;
	int parentNo;
	BoardServiceImpl service = new BoardServiceImpl();

	public void execute() {
		checkLogin();
		while (true) {
			System.out.println("Select a menu.  1.Insert 2.Update 3.Delete 4.Search 5.List 9.Exit");
			int menu = 0;
			menu = sc.nextInt();
			sc.nextLine();
			switch (menu) {
			case 1: // 입력
				writeBoard();
				break;
			case 2: // 수정
				updateBoard();
				break;
			case 3: // 삭제
				deleteBoard();
				break;
			case 4: // 조회
				getBoard();
				break;
			case 5: // 리스트
				getBoardList();
				break;
			case 9:
				System.out.println("Exit the program");
				System.exit(0);
			}
		}
	}



	public void writeBoard() {
		System.out.println("Enter the title: ");
		String title = sc.nextLine();
		System.out.println("Enter the content: ");
		String content = sc.nextLine();
		Board board = new Board(title, content, id);
		service.writeBoard(board); // 호출
	}

	private void deleteBoard() {
		System.out.println("Enter the board_no you want to delete");
		int num = sc.nextInt();
		sc.nextLine();
		service.deleteBoard(num);

	}

	private void updateBoard() {
		Board board = new Board();
		System.out.println("Enter the board_no you want to update");
		board.setBoardNo(sc.nextInt());
		sc.nextLine();
		System.out.println("Enter the updated title");
		board.setTitle(sc.nextLine());
		System.out.println("Enter the updated content");
		board.setContent(sc.nextLine());
		service.updateBoard(board);
		getBoardList();
	}

	public void checkLogin() {
		while (true) {
			System.out.println("Enter your ID : ");
			id = sc.nextLine();
			System.out.println("Enter your Password: ");
			pw = sc.nextLine();
			name = service.checkLogin(id, pw);
			// name이 null이거나 name의 값이 없는지 체크.. 참고)첫번째 조건을 만족하면 두번째 조건 실행됨
			if (name == null || name.equals(""))
				System.out.println("Failed login. Please check your ID and PW");
			else {
				System.out.println("Welcome " + name);
				break;
			}
		}
	}

	public void getBoardList() {
		List<Board> board = service.getBoardList();
		BoardDao dao = new BoardDao();
		board = dao.getBoardList();
		System.out.println("Board_no" + "\t" + "Title" + "\t" + "Writer" + "\t" + "Date");

		System.out.println("==========================================");
		for (Board b : board) {
			System.out.print(b.getBoardNo() + "\t\t");
			System.out.print(b.getTitle() + "\t");
			System.out.print(b.getWriter() + "\t");
			System.out.println(b.getCreationDate());
		}

	}

	public void getBoard() {
		System.out.println("Enter the board_no you want to search");
		int boardNo = sc.nextInt();
		sc.nextLine();
		System.out.println("-----[Original]-----");
		Board board = service.getBoard(boardNo);
		System.out.println("No : " + board.getBoardNo());
		System.out.println("Title : " + board.getTitle());
		System.out.println("Content : " + board.getContent());
		System.out.println("Writer : " + board.getWriter());
		System.out.println("Date : " + board.getCreationDate());

		System.out.println("-----[Reply]-----");
		List<Board> boardReply = service.getRelpyList(boardNo);
		BoardDao dao = new BoardDao();
		boardReply = dao.getRelpyList(boardNo);
		for (Board b : boardReply) {
			System.out.print("Reply) " + b.getBoardNo() + " ");
			System.out.print(b.getContent() + " ");
			System.out.print(b.getWriter() + " ");
			System.out.println(b.getParentNo() + " ");
		}
		parentNo = board.getBoardNo();
		System.out.println("1.Leave a replay 2. Move to the parent folder");
		int num = sc.nextInt();
		sc.nextLine();
		if (num == 1) {
			writeReply();
		} else {
			
		}
	}
	
	private void writeReply() {
		System.out.println("Title: ");
		String title = sc.nextLine();
		System.out.println("Content: ");
		String content = sc.nextLine();
		Board board = new Board(title, content, id, parentNo);
		service.writeReply(board);

	}

}
