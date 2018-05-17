package com.pwc.topology.wordCount;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * @Author : Frank Jiang
 * @Date : 15/05/2018 5:05 PM
 */
public class RandomSentenceSpout extends BaseRichSpout {

    // 用来收集Spout输出的tuple
    SpoutOutputCollector collector = null;
    Random random = null;

    //该方法调用一次，主要由storm框架传入SpoutOutputCollector
    @Override
    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        this.collector = collector;
        random = new Random();
    }

    //该方法会被循环调用
    @Override
    public void nextTuple() {
        String[] sentences = new String[] {
                "the cow jumped over the moon",
                "an apple a day keeps the doctor away",
                "four score and seven years ago",
                "snow white and the seven dwarfs",
                "I am at two with nature"
        };
        String sentence = sentences[random.nextInt(sentences.length - 2)];
        collector.emit(new Values(sentence));
    }

    //消息源可以发射多条消息流stream，多条消息流可以理解为多种类型的数据
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence"));
    }
}
