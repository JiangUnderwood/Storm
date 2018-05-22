package com.pwc.topology.logMonitor.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import com.pwc.topology.logMonitor.domain.Record;
import com.pwc.topology.logMonitor.utils.MonitorHandler;
import org.apache.log4j.Logger;

/**
 * 将触发信息保存到mysql数据库中去
 *
 * @Author : Frank Jiang
 * @Date : 18/05/2018 9:28 AM
 */
public class SaveMsg2Mysql extends BaseBasicBolt {
    private static Logger logger = Logger.getLogger(SaveMsg2Mysql.class);


    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Record record = (Record) input.getValueByField("record");
        MonitorHandler.save(record);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
