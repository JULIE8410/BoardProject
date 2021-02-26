package board.impl;

import java.util.List;

import board.Board;
import board.BoardService;

public class BoardServiceImpl implements BoardService {
	BoardDao dao = new BoardDao();

	@Override
	public String checkLogin(String id, String pw) {
		return dao.getMemberInfo(id, pw);
	}

	@Override
	public void writeBoard(Board board) {
		dao.insertBoard(board);
	}

	@Override
	public void writeReply(Board board) {
		dao.insertReply(board);
	}

	@Override
	public void updateBoard(Board board) {
		dao.updateBoard(board);
	}

	@Override
	public List<Board> getBoardList() {
		return dao.getBoardList();
	}

	@Override
	public Board getBoard(int boardNo) {
		return dao.getBoard(boardNo);
	}

	@Override
	public List<Board> getRelpyList(int boardNo) {
		return dao.getRelpyList(boardNo);
	}
	
	public void deleteBoard(int boardNo) {
		dao.deleteBoard(boardNo);
	}
	
}
