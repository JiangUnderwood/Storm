package com.pwc.topology.logMonitor.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.pwc.topology.logMonitor.domain.Record;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;

/**
 * @Author : Frank Jiang
 * @Date : 17/05/2018 11:16 AM
 */
public class DataSourceUtil {

    private static Logger logger = Logger.getLogger(DataSourceUtil.class);
    private static DataSource dataSource;

    static {
        dataSource = new ComboPooledDataSource("logMonitor");
    }

    public static synchronized DataSource getDataSource(){
        if(dataSource == null){
            dataSource = new ComboPooledDataSource();
        }
        return dataSource;
    }

    public static void main(String[] args) {
        JdbcTemplate template = new JdbcTemplate(dataSource);

       /* Record record = new Record();
        record.setAppId(1111);
        record.setRuleId(1);
        record.setIsEmail(0);
        record.setIsPhone(1);
        record.setIsClose(0);
        String insertSql = "INSERT INTO log_monitor_rule_record (appId, ruleId, isEmail, isPhone, isClose, updataDate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        template.update(insertSql, record.getAppId(), record.getRuleId(), record.getIsEmail(), record.getIsPhone(), record.getIsClose(), new Date());*/

        String sql = "SELECT id,appId,ruleId,isEmail,isPhone FROM log_monitor_rule_record WHERE appId =1111";
        System.out.println(template.query(sql, new BeanPropertyRowMapper<Record>(Record.class)));
    }
}
