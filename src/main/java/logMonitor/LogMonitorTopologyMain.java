package logMonitor;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import logMonitor.bolt.FilterBolt;
import logMonitor.bolt.PrepareRecordBolt;
import logMonitor.bolt.SaveMsg2Mysql;
import logMonitor.spout.RandomSpout;
import logMonitor.spout.StringScheme;
import org.apache.log4j.Logger;

/**
 * 日志监控系统驱动类
 *
 * @Author : Frank Jiang
 * @Date : 23/05/2018 5:17 PM
 */
public class LogMonitorTopologyMain {
    private static Logger logger = Logger.getLogger(LogMonitorTopologyMain.class);

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        //使用TopologyBuilder构建驱动类
        TopologyBuilder builder = new TopologyBuilder();
        /*//设置kafka的zookeeper集群
        BrokerHosts hosts = new ZkHosts("mysql:2181");
        //初始化配置信息
        SpoutConfig spoutConfig = new SpoutConfig(hosts, "logmonitor", "/aaa", "log_monitor");
        //在topology中设置spout
        builder.setSpout("kafka-spout", new KafkaSpout(spoutConfig), 3);*/
        builder.setSpout("kafka-spout", new RandomSpout(new StringScheme()), 2);
        builder.setBolt("filter-bolt", new FilterBolt(), 3).shuffleGrouping("kafka-spout");
        builder.setBolt("prepareRecord-bolt", new PrepareRecordBolt(), 2).fieldsGrouping("filter-bolt", new Fields("appId"));
        builder.setBolt("saveMessage-bolt", new SaveMsg2Mysql(), 2).shuffleGrouping("prepareRecord-bolt");

        //启动topology的配置信息
        Config topologyConfig = new Config();
        //TOPOLOGY_DEBUG(setDebug),当它被设置成true的话，storm会记录下每个组件所发射的每条消息。
        //这在本地环境调试topology很有用，但是在线上这么做的话会影响性能
        topologyConfig.setDebug(true);
        //storm的运行有两种模式：本地模式和分布式模式.
        if (args != null && args.length > 0) {
            //定义你希望集群分配多少个工作进程给你来执行这个topology
            topologyConfig.setNumWorkers(2);
            //向集群提交topology
            StormSubmitter.submitTopologyWithProgressBar(args[0], topologyConfig, builder.createTopology());
        } else {
            topologyConfig.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("word-count", topologyConfig, builder.createTopology());
            Utils.sleep(10000000);
            cluster.shutdown();
        }
    }

}

