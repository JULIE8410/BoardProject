package board.impl;

import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import board.Board;

public class BoardDao {

	private String driver;
	private String url;
	private String user;
	private String pw;

	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;

	private void init() {
		String path = "config/db.properties";
		Properties profile = new Properties();

		try {
			profile.load(new FileReader(path));
			driver = profile.getProperty("driver");
			url = profile.getProperty("url");
			user = profile.getProperty("user");
			pw = profile.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BoardDao() {
		init();
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertBoard(Board board) {
		String sql = "{call write_board(?,?,?)}";
		try {
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, board.getTitle());
			cstmt.setString(2, board.getContent());
			cstmt.setString(3, board.getWriter()); 
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertReply(Board board) {
		String sql = "{call reply_board(?,?,?,?)}";
		try {
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.setString(1, board.getTitle());
			cstmt.setString(2, board.getContent());
			cstmt.setString(3, board.getWriter()); 
			cstmt.setInt(4, board.getParentNo());
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getMemberInfo(String id, String pw) {
		String sql = "select id, pw, name from login_test where id = ? and pw = ?"; 
		String name = null;
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, pw);
			rs = psmt.executeQuery();
			if (rs.next()) { 
				name = rs.getString("name"); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	public List<Board> getBoardList() {
		String sql = "select * from board where parent_no is null order by 1 desc";
		List<Board> list = new ArrayList<Board>();
		Board board = null;
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				board = new Board();
				board.setBoardNo(rs.getInt("board_no"));
				board.setTitle(rs.getString("title"));
				board.setWriter(rs.getString("writer"));
				board.setCreationDate(rs.getString("creation_date"));
				list.add(board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Board getBoard(int boardNo) { 
		String sql = "select * from board where board_no = ?";
		Board board = null;
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, boardNo);
			rs = psmt.executeQuery();
			if (rs.next()) {
				board = new Board();
				board.setBoardNo(rs.getInt("board_no"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setWriter(rs.getString("writer"));
				board.setCreationDate(rs.getString("creation_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return board;
	}

	public void updateBoard(Board board) {
		String sql = "update board set title = ?, content = ? where board_no = ?";

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, board.getTitle());
			psmt.setString(2, board.getContent());
			psmt.setInt(3, board.getBoardNo());
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Board> getRelpyList(int boardNo) {
		String sql = "select * from board where parent_no = ?";
		List<Board> list = new ArrayList<Board>();
		Board board = null;
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, boardNo);
			rs = psmt.executeQuery();
			while (rs.next()) {
				board = new Board();
				board.setBoardNo(rs.getInt("board_no"));
				board.setContent(rs.getString("content"));
				board.setWriter(rs.getString("writer"));
				board.setParentNo(boardNo);
				list.add(board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void deleteBoard(int boardNo) {
		String sql = "delete from board where board_no = ?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, boardNo);
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
