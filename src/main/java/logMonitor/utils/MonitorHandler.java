package logMonitor.utils;


import logMonitor.dao.LogMonitorDao;
import com.pwc.topology.logMonitor.domain.*;
import logMonitor.domain.*;
import topology.logMonitor.domain.*;
import logMonitor.mail.MailInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 过滤规则信息
 *
 * @Author : Frank Jiang
 * @Date : 17/05/2018 6:52 PM
 */
public class MonitorHandler {
    private static Logger logger = Logger.getLogger(MonitorHandler.class);

    private static List<App> appList;

    private static List<User> userList;

    //key ==> appId,    value ==> app下所有rule列表
    private static Map<String, List<Rule>> ruleMap;
    //key ==> appId,    value ==> app下所有user列表
    private static Map<String, List<User>> userMap;

    //定时加载配置文件的标识
    private static boolean reloaded = false;
    //定时加载配置文件的标识
    private static long nextReload = 0L;

    static {
        load();
    }

    /**
     * 加载数据模型，主要是用户列表、应用管理列表、组合规则模型、组合用户模型
     */
    public static synchronized void load() {
        if (userList == null) {
            userList = loadUserList();
        }
        if (appList == null) {
            appList = loadAppList();
        }
        if (ruleMap == null) {
            ruleMap = loadRuleMap();
        }
        if (userMap == null) {
            userMap = loadUserMap();
        }
    }

    /**
     * 访问数据库获取所有有效的应用列表
     *
     * @return
     */
    private static List<App> loadAppList() {
        return new LogMonitorDao().getAppList();
    }

    /**
     * 访问数据库获取所有有效的用户列表
     *
     * @return
     */
    private static List<User> loadUserList() {
        return new LogMonitorDao().getUserList();
    }

    /**
     * 封装应用与规则对应的map
     *
     * @return
     */
    private static Map<String, List<Rule>> loadRuleMap() {
        //应用的appId作为key，该应用的所有规则对象列表为value。
        Map<String, List<Rule>> appRuleMap = new HashMap<>();
        LogMonitorDao dao = new LogMonitorDao();
        List<Rule> rules = dao.getRuleList();
        for (Rule rule : rules) {
            String appIdStr = rule.getAppId() + "";
            if (appRuleMap.containsKey(appIdStr)) {
                appRuleMap.get(appIdStr).add(rule);
            } else {
                appRuleMap.put(appIdStr, new ArrayList<Rule>());
            }
        }
        return appRuleMap;
    }

    /**
     * 封装应用与用户对应的map
     *
     * @return
     */
    private static Map<String, List<User>> loadUserMap() {
        //应用的appId作为key，该应用的所有负责人对象列表为value。
        Map<String, List<User>> appUserMap = new HashMap<>();
        LogMonitorDao dao = new LogMonitorDao();
        List<User> usersBelowApp = null;
        for (App app : appList) {
            String userIdStrs = app.getUserId();
            usersBelowApp = queryUsersByIds(userIdStrs.split(","));
            appUserMap.put(app.getId() + "", usersBelowApp);
        }
        return appUserMap;
    }

    private static List<User> queryUsersByIds(String... userIds) {
        List<User> users = new ArrayList<>();
        for (String userId : userIds) {
            for (User user : userList) {
                if (Integer.parseInt(userId) == user.getId())
                    users.add(user);
            }
        }
        return users;
    }

    /**
     * 解析输入的日志，将数据按照一定的规则进行分割，
     * 判断日志是否合法，主要校验日志所属应用的appId是否存在
     *
     * @param line 一条日志
     * @return
     */
    public static Message parse(String line) {
        //日志内容分为两个部分：由5个$$$$$符号作为分隔符，第一部分为appId，第二部分为日志内容
        String[] messages = line.split("\\$\\$\\$\\$\\$");
        //对日志进行校验
        if (messages.length != 2) {
            return null;
        }
        String appId = messages[0],
                info = messages[1];
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(info)) {
            return null;
        }
        //检验当前日志所属的appId是否经过授权
        if (checkIsValid(appId)) {
            Message message = new Message();
            message.setAppId(appId);
            message.setLine(info);
            return message;
        }
        return null;
    }

    /**
     * 验证appId是否经过授权
     *
     * @param appId
     * @return
     */
    private static boolean checkIsValid(String appId) {
        try {
            for (App app : appList) {
                if (app.getId() == Integer.parseInt(appId)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 对日志进行规则判断，看看是否触发规则
     *
     * @param message
     * @return
     */
    public static boolean trigger(Message message) {
        //如果规则模型为空，需要初始化加载规则模型
        if (ruleMap == null) load();
        //从规则模型中取出当前appId配置的规则
        List<Rule> rulesBelowApp = ruleMap.get(message.getAppId());
        for (Rule rule : rulesBelowApp) {
            //如果日志中包含过滤过的关键词，即视为匹配成功
            if (message.getLine().contains(rule.getKeyword())) {
                message.setRuleId(rule.getId() + "");
                message.setKeyword(rule.getKeyword());
                return true;
            }
        }

        return false;
    }

    private static synchronized void reloadDataModel() {
        if (reloaded) {
            long start = System.currentTimeMillis();
            userList = loadUserList();
            appList = loadAppList();
            ruleMap = loadRuleMap();
            userMap = loadUserMap();
            reloaded = false;
            nextReload = 0L;
            logger.info("配置文件reload完成，时间：" + getDateTime() + "; 耗时：" + (System.currentTimeMillis() - start) / 1000 + "s");
        }
    }

    /**
     * 定时加载配置信息
     * 配合reloadDataModel模块一起使用。
     * 实现原理如下：
     * 1、获取分钟的数据值，当分钟数据是10的倍数，就会触发reloadDataModel方法，简称reload时间。
     * 2、reloadDataModel方式是线程安全的，其他线程不再重复操作，设置了一个标识符reloaded。
     * 3、为了保证当前线程操作完毕之后，其他线程不再重复操作，设置了一个标识符reloaded。
     * 在非reload时间段时，reloaded一直被置为true；
     * 在reloaded时间段，第一个线程进入reloadDataModel后，加载完毕之前会将reloaded置为false。
     */
    public static void scheduleLoad() {
        String dateTime = getDateTime();
        int nowMinute = Integer.parseInt(dateTime.split(":")[1]);
        //每十分钟重加载一次
        if (nowMinute % 10 == 0) {
            reloadDataModel();
        } else {
            reloaded = true;
        }
    }

    private static String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    /**
     * 告警模块，用来发送邮件和短信
     * 短信功能由于短信资源匮乏，目前默认返回已发送
     *
     * @param appId
     * @param message
     */
    public static void notify(String appId, Message message) {
        //通过appId获取应用负责人的对象
        List<User> users = getUserIdsByAppId(appId);
        //发送邮件
        if (sendMail(appId, users, message)) {
            message.setIsEmail(1);
        }
        //发送短信
        if (sendSMS(appId, users, message)) {
            message.setIsPhone(1);
        }
    }

    /**
     * 通过app编号，获取当前app的所有负责人列表
     *
     * @param appId
     * @return
     */
    private static List<User> getUserIdsByAppId(String appId) {
        return userMap.get(appId);
    }

    /**
     * 发送短信的模块
     * 由于短信资源匮乏，目前该功能不开启，默认true，即短信发送成功。
     * 目前发送短信功能使用的是外部接口，外部接口的并发性不能保证，会影响storm程序运行的效率。
     * 后期可以改造为将短信数据发送到外部的消息队列里中，然后创建一个worker去发送消息。
     *
     * @param appId
     * @param users
     * @param message
     * @return
     */
    private static boolean sendSMS(String appId, List<User> users, Message message) {
        List<String> mobileList = new ArrayList<>();
        for (User user : users) {
            mobileList.add(user.getMobile());
        }
        for (App app : appList) {
            if (app.getId() == Integer.valueOf(appId.trim())) {
                message.setAppName(app.getName());
                break;
            }
        }
        String content = "系统【" + message.getAppName() + "】在 " + getDateTime() + " 触发规则 "
                + message.getRuleId() + ",关键字：" + message.getKeyword();
        //SMSBase.sendSms(listToStringFormat(mobileList), content);
        return true;
    }

    /**
     * 发送邮件
     * 后期可以改造为将邮件数据发送到外部的消息队列里，然后创建一个worker去发送短信
     *
     * @param appId
     * @param users
     * @param message
     * @return
     */
    private static boolean sendMail(String appId, List<User> users, Message message) {
        List<String> receivers = new ArrayList<>();
        for (User user : users) {
            receivers.add(user.getEmail());
        }
        for (App app : appList) {
            if (app.getId() == Integer.valueOf(appId.trim())) {
                message.setAppName(app.getName());
                break;
            }
        }
        if (receivers.size() > 1) {
            String date = getDateTime();
            String content = "系统【" + message.getAppName() + "】在 " + date + " 触发规则 "
                    + message.getRuleId() + ", 过滤关键字为：" + message.getKeyword() + ", 错误内容："
                    + message.getLine();
            MailInfo mailInfo = new MailInfo("系统运行日志监控", content, receivers, null);
        }
        return false;
    }

    /**
     * 保存触发规则的信息，将触发信息写入到mysql数据库中。
     *
     * @param record
     */
    public static void save(Record record) {
        new LogMonitorDao().saveRecord(record);
    }
}
