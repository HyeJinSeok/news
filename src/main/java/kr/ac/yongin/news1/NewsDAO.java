package kr.ac.yongin.news1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String JDBC_URL = "jdbc:h2:C:/Users/u3afg/wjbook";

    // DB 연결을 가져오는 메서드
    public Connection open(){
        Connection conn = null;

        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(JDBC_URL, "jwbook", "1234");
        } catch(Exception e){e.printStackTrace();}
        return conn;
    }

    // 뉴스 기사 목록 전체를 가져오는 메서드
    public List<News> getAll() throws Exception {
        Connection conn = open();
        List<News> newlist = new ArrayList<>();

        String sql = "select aid, title, PARSEDATETIME(date, 'yyyy-MM-dd HH:mm:ss') AS cdate from news";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        try(conn; pstmt; rs) {
            while(rs.next()){
                News n = new News();
                n.setAid(rs.getInt("aid"));
                n.setTitle(rs.getString("title"));
                n.setDate(rs.getString("cdate"));
                newlist.add(n);
            }
            return newlist;
        }
    }

    // 뉴스 한 개를 클릭했을 떄 세부 내용을 보여주는 메서드
    public News getNews(int aid) throws SQLException {
        Connection conn = open();
        News n = new News();
        String sql = "select aid, title, PARSEDATETIME(date, 'yyyy-MM-dd HH:mm:ss') AS cdate, content from news where aid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, aid);
        ResultSet rs = pstmt.executeQuery();
        rs.next();

        try(conn; pstmt; rs) {
            n.setAid(rs.getInt("aid"));
            n.setTitle(rs.getString("title"));
            n.setImg(rs.getString("img"));
            n.setDate(rs.getString("cdate"));
            n.setContent(rs.getString("content"));
            pstmt.executeQuery();
            return n;
        }
    }

    // 뉴스 추가 메서드
    public void addNews(News n) throws Exception {
        Connection conn = open();
        String sql = "insert into news(title, img, date, content) values(?,?,CURRENT_TIMESTAMP(),?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        try(conn; pstmt; ) {
            pstmt.setString(1, n.getTitle());
            pstmt.setString(2, n.getImg());
            pstmt.setString(3, n.getContent());
            pstmt.executeUpdate();
        }
    }

    // 뉴스 삭제 메서드
    public void delNews(int aid) throws SQLException {
        Connection conn = open();
        String sql = "delete from news where aid=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        try(conn; pstmt; ) {
            pstmt.setInt(1, aid);
            if(pstmt.executeUpdate() == 0){
                throw new SQLException("DB에러");
            }
        }
    }

}
