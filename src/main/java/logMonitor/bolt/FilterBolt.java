package logMonitor.bolt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import logMonitor.domain.Message;
import logMonitor.utils.MonitorHandler;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * 过滤规则信息
 *
 * @Author : Frank Jiang
 * @Date : 17/05/2018 6:40 PM
 */
//BaseRichBolt 需要手动调用ack方法，BaseBasicBolt由storm框架自动调用ack方法
public class FilterBolt extends BaseBasicBolt {
    private static Logger logger = Logger.getLogger(FilterBolt.class);

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取kafkaSpout发送出来的数据
        String line = input.getString(0);
        //对数据进行解析
        /*  appid     content
         *  1         error: Caused by: java.lang.NoClassDefFoundError: com/starit/gejie/SysNameDao
         */
        Message message = MonitorHandler.parse(line);
        if (message == null) {
            return;
        }
        if (MonitorHandler.trigger(message)) {
            collector.emit(new Values(message.getAppId(), message));
        }

        //定时更新规则信息
        MonitorHandler.scheduleLoad();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("appId", "message"));
    }
}
