package com.pwc.topology.logMonitor.dao;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @Author : Frank Jiang
 * @Date : 17/05/2018 11:13 AM
 */
public class LogMonitorDao {

    private static Logger logger = Logger.getLogger(LogMonitorDao.class);
    private JdbcTemplate jdbcTemplate;

    public LogMonitorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new JdbcTemplate();
    }
}
