package kafkaAndStorm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * @Author : Frank Jiang
 * @Date : 25/05/2018 11:29 AM
 */
public class ParserOrderMqBolt extends BaseRichBolt {
    private JedisPool pool;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        //change "maxActive" ==> "maxTotal" and "maxWait" ==> "maxWaitMillis" in all examples
        JedisPoolConfig config = new JedisPoolConfig();


    }

    @Override
    public void execute(Tuple tuple) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
