package topology.wordCount;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : Frank Jiang
 * @Date : 15/05/2018 5:10 PM
 */
public class WordCountBolt extends BaseBasicBolt {
    //用来保存最后计算的结果，key ==> 单词，value ==> 单词个数
    Map<String, Integer> counters = new HashMap<String, Integer>();

    //该方法只会被调用一次，用来初始化
    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
    }

    /*
     * 将collector中的元素存放在成员变量counters(Map)中，
     * 如果counters(Map)中已经存在，getValue并对Value进行累加操作。
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String word = input.getString(0);
        if(!counters.containsKey(word)){
            counters.put(word, 1);
        }else {
            Integer count = counters.get(word) + 1;
            counters.put(word, count);
        }
        System.out.println(counters);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
