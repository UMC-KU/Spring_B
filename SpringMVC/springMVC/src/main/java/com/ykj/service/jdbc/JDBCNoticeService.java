package com.ykj.service.jdbc;

import com.ykj.entity.Notice;
import com.ykj.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("service")
public class JDBCNoticeService implements NoticeService {
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
//    String url = "jdbc:mysql://localhost:3306/newlecture1";
//    String user = "YKJ";
//    String password = "991911";
//    String driver = "com.mysql.cj.jdbc.Driver";

    public List<Notice> getNoticeList() {

        List<Notice> noticeList = new ArrayList<>();

        try {
            String sql = "select * from notice";
            Connection con = dataSource.getConnection();

//            Class.forName(driver);
//            Connection con = DriverManager.getConnection(url, user,password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String writerId = rs.getString("writer_id");
                Date regDate = rs.getDate("regDate");
                String content = rs.getString("content");
                int hit = rs.getInt("hit");
                String files = rs.getString("files");


                Notice notice = new Notice(id,title,writerId,regDate,content,hit,files);
                noticeList.add(notice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return noticeList;
    }

    @Override
    public int insert(Notice notice) throws SQLException, ClassNotFoundException {
        return 0;
    }

    @Override
    public int update(Notice notice) throws SQLException, ClassNotFoundException {
        return 0;
    }

    @Override
    public int delete(int id) throws ClassNotFoundException, SQLException {
        return 0;
    }

    @Override
    public int getCount() throws ClassNotFoundException, SQLException {
        return 0;
    }
    @Override
    public String toString() {
        return "JDBCNoticeService{" +
                "dataSource=" + dataSource +
                '}';
    }
}
