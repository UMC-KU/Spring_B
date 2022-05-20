package com.ykj.service;

import com.ykj.entity.Notice;

import java.sql.SQLException;
import java.util.List;

public interface NoticeService {
    List<Notice> getNoticeList();

    int insert(Notice notice) throws SQLException, ClassNotFoundException;

    int update(Notice notice) throws SQLException, ClassNotFoundException;

    int delete(int id) throws ClassNotFoundException, SQLException;

    int getCount() throws ClassNotFoundException, SQLException;


}
