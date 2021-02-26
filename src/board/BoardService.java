package board;

import java.util.List;

public interface BoardService {
	public String checkLogin(String id, String pw);

	public void writeBoard(Board board);

	public void writeReply(Board board); 

	public void updateBoard(Board board); 

	public List<Board> getBoardList();

	public Board getBoard(int boardNo); 

	public List<Board> getRelpyList(int boardNo); 

}
