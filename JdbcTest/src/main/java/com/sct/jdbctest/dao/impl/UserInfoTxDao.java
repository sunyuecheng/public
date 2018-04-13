package com.sct.jdbctest.dao.impl;

import com.sct.jdbctest.dao.IUserInfoDao;
import com.sct.jdbctest.dao.util.SqlDataMapping;
import com.sct.jdbctest.entity.UserInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("UserInfoDao")
public class UserInfoTxDao extends SqlDataMapping implements IUserInfoDao {
    private static final Logger logger = Logger.getLogger(UserInfoTxDao.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public boolean insertUserInfo(UserInfo userInfo) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        boolean ret = false;
        String sqlCmdFormat = "INSERT INTO t_user_info (id, name, password, real_name, id_card, phone_num, register_date, last_view_date) VALUES (?,?,?,?,?,?,?,?)";

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            ret = jdbcTemplate.update(sqlCmdFormat, new Object[]{userInfo.getId(), userInfo.getName(), userInfo.getPassword(),
                    userInfo.getRealName(), userInfo.getIdCard(), userInfo.getPhoneNum(), dateFormat.format(userInfo.getRegisterDate()),
                    dateFormat.format(userInfo.getLastViewDate())}) >= 0;
        } catch (Exception e) {
            logger.error("Insert user info error,error info("+ e.getMessage() +")");
        }
        return ret;
    }

    public boolean deleteUserInfoById(String id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        boolean ret = false;
        String sqlCmdFormat = "DELETE t_user_info WHERE id=?";

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            ret = jdbcTemplate.update(sqlCmdFormat, new Object[]{id}) >= 0;
        } catch (Exception e) {
            logger.error("Delete user info error,error info("+ e.getMessage() +")");
        }
        return ret;
    }

    public UserInfo findUserInfoById(String id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        UserInfo retUserInfo = null;

        String sqlCmdFormat = "SELECT * FROM t_user_info WHERE id=?";
        try {
            retUserInfo = (UserInfo) queryForObject(jdbcTemplate, sqlCmdFormat, new Object[]{id}, UserInfo.class);
        } catch (Exception e) {
            logger.error("Get user info error,error info(" + e.getMessage() + ")!");
        }

        return retUserInfo;
    }

    public Long getUserInfoCount() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long ret = null;

        String sqlCmdFormat = "SELECT COUNT(*) FROM t_user_info";
        try {
            ret = jdbcTemplate.queryForObject(sqlCmdFormat, new Object[]{},Long.class);
        } catch (Exception e) {
            logger.error("Get user info error,error info(" + e.getMessage() + ")!");
        }
        return ret;
    }

    public List<UserInfo> getUserInfoListByLastViewDate(Long beginDate, Long endDate, int pageIndex, int pageSize) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<UserInfo> retUserInfoList = new ArrayList<UserInfo>();

        String sqlCmdFormat = "SELECT * FROM t_user_info WHERE last_view_date <= ? AND last_view_date >= ? ORDER BY last_view_date DESC";
        String countSqlCmdFormat = "SELECT COUNT(*) FROM t_user_info WHERE last_view_date <= ? AND last_view_date >= ? ORDER BY last_view_date DESC";

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            retUserInfoList = queryForPageObjectList(jdbcTemplate, sqlCmdFormat, new Object[]{dateFormat.format(new Date(endDate)), dateFormat.format(new Date(beginDate))},
                    countSqlCmdFormat, new Object[]{dateFormat.format(new Date(endDate)), dateFormat.format(new Date(beginDate))}, pageIndex, pageSize, UserInfo.class);
        } catch (Exception e) {
            logger.error("Get user info list error,error info(" + e.getMessage() + ")!");
        }

        return retUserInfoList;
    }
}
