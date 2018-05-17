package com.pwc.topology.logMonitor.dao;


import com.pwc.topology.logMonitor.domain.App;
import com.pwc.topology.logMonitor.domain.Record;
import com.pwc.topology.logMonitor.domain.Rule;
import com.pwc.topology.logMonitor.domain.User;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

/**
 * @Author : Frank Jiang
 * @Date : 17/05/2018 11:13 AM
 */
public class LogMonitorDao {

    private static Logger logger = Logger.getLogger(LogMonitorDao.class);
    private JdbcTemplate jdbcTemplate;

    public LogMonitorDao() {
        this.jdbcTemplate = new JdbcTemplate(DataSourceUtil.getDataSource());
    }

    public static void main(String[] args) {
        System.out.println("start ... ");
        //测试数据库连接及相关方法是否正常
        LogMonitorDao dao = new LogMonitorDao();
        //打印所有的规则信息
        List<Rule> rules = dao.getRuleList();
        for (Rule rule : rules) {
            System.out.println(rule);
        }
        System.out.println("--------------********************---------------");
        //打印所有的应用信息
        List<App> apps = dao.getAppList();
        for (App app : apps) {
            System.out.println(app);
        }
        System.out.println("--------------********************---------------");
        //打印所有的用户信息
        List<User> users = dao.getUserList();
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("--------------********************---------------");
    }

    /**
     * 查询所有规则信息
     *
     * @return
     */
    public List<Rule> getRuleList() {
        String sql = "SELECT id, name, keyword, isValid, appId FROM log_monitor_rule WHERE isValid = 1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Rule>(Rule.class));
    }

    /**
     * 查询所有应用信息
     *
     * @return
     */
    public List<App> getAppList() {
        String sql = "SELECT id, name, isOnline, typeId, userId FROM log_monitor_app WHERE isOnline = 1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<App>(App.class));
    }

    /**
     * 查询所有用户信息
     *
     * @return
     */
    public List<User> getUserList() {
        String sql = "SELECT id, name, mobile, email, isValid FROM log_monitor_user WHERE isValid = 1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
    }

    /**
     * 插入触发规则的信息
     *
     * @param record
     */
    public void saveRecord(Record record) {
        String sql = "INSERT INTO log_monitor_rule_record (appId, ruleId, isEmail, isPhone, isClose, noticeInfo, updateDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, record.getAppId(), record.getRuleId(), record.getIsEmail(), record.getIsPhone(), record.getLine(), new Date());
    }
}

