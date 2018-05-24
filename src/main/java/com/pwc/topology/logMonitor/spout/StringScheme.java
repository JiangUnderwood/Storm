package com.pwc.topology.logMonitor.spout;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.List;

/**
 * @Author : Frank Jiang
 * @Date : 23/05/2018 5:46 PM
 */
public class StringScheme implements Scheme {
    @Override
    public List<Object> deserialize(byte[] bytes) {
        try {
            return new Values(new String(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("line");
    }
}
