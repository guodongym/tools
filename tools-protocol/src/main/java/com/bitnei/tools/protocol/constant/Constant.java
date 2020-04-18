package com.bitnei.tools.protocol.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 常量
 *
 * @author zhaogd
 */
public class Constant {

    /*-------------------------------标点符号-------------------------------------*/

    /**
     * 13位数字最大值
     */
    public static final long TIMESTAMP_MAX = 9999999999999L;

    /**
     * 空格
     */
    public static final String SPACES = " ";
    /**
     * 空字符串
     */
    public static final String EMPTY = "";
    /**
     * 逗号
     */
    public static final String COMMA = ",";
    /**
     * 句号
     */
    public static final String PERIOD = ".";
    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";
    /**
     * 中横线
     */
    public static final String HYPHEN = "-";
    /**
     * 冒号
     */
    public static final String COLON = ":";
    /**
     * 竖线
     */
    public static final String VERTICAL_LINE = "|";
    /**
     * 竖线正则
     */
    public static final String VERTICAL_LINE_REG = "\\|";
    /**
     * 换行符
     */
    public static final String NEWLINE = "\r\n";
    /**
     * 反斜杠
     */
    public static final String BACKSLASH = "/";
    /**
     * 匹配空白字符
     */
    public static final String BLANK_SPACE = "\\s+";


    /*-------------------------------内部协议字段常量-------------------------------------*/

    public final static String VID = "VID";
    public final static String VIN = "VIN";
    public static final String TIME = "TIME";

    public static final String IS_ONLINE = "10002";
    public static final String IS_CHARGE = "10003";
    public static final String ONLINE_UTC = "10005";

    /**
     * 32960协议中表示充电状态的标识
     */
    public static final List<String> CHARGE_STATE = Arrays.asList("1", "2", "4");

    /**
     * 充电/在线状态的是否标识
     */
    public static final String YES = "1";
    public static final String NO = "0";


    /**
     * 当前行政区域编码
     */
    public static final String GPS_REGION_KEY = "GPS_REGION";
    /**
     * 经度
     */
    public static final String LONGITUDE_KEY = "2502";
    /**
     * 纬度
     */
    public static final String LATITUDE_KEY = "2503";
    /**
     * 仪表里程
     */
    public static final String METER_MILEAGE_KEY = "2202";

    public static final String MILEAGE_KEY = "mileage";
    public static final String GPS_MILEAGE_KEY = "gpsMileage";

    public static final String MILEAGE_DIFFERENCE_KEY = "mileageDifference";
    public static final String GPS_MILEAGE_DIFFERENCE_KEY = "gpsMileageDifference";


    /*-------------------------------报文类型-------------------------------------*/

    /**
     * 指令类型
     */
    public final static String SUBMIT = "SUBMIT";
    /**
     * 消息类型, 数字表示与国标文档一致
     */
    public static final String TYPE = "TYPE";
    /**
     * 消息类型, 英文标识 自定义
     */
    public static final String MESSAGETYPE = "MESSAGETYPE";
    /**
     * 消息类型 原始报文
     */
    public static final String PACKET = "PACKET";
    /**
     * 消息类型 实时数据
     */
    public static final String REALTIME = "REALTIME";
    /**
     * 消息类型 报警数据
     */
    public static final String ALARM = "ALARM";
    /**
     * 消息类型 转发数据
     */
    public static final String FORWARD = "FORWARD";
    /**
     * 消息类型 登录报文
     */
    public static final String LOGIN = "LOGIN";
    /**
     * 消息类型 登出报文
     */
    public static final String LOGOUT = "LOGOUT";
    /**
     * 消息类型 补发数据
     */
    public static final String HISTORY = "HISTORY";
    /**
     * 消息类型 链接状态通知
     */
    public static final String LINKSTATUS = "LINKSTATUS";
    /**
     * 消息类型 状态信息上报
     */
    public static final String TERMSTATUS = "TERMSTATUS";
    /**
     * 消息类型 车辆运行状态
     */
    public static final String CARSTATUS = "CARSTATUS";


    /*-------------------------------Channel标识符常量-------------------------------------*/

    /**
     * 发送给channel的标记
     */
    public static final String CHANNEL_KEY = "CHANNEL_KEY";
    /**
     * 补发数据channel
     */
    public static final String CHANNEL_HISTORY = "HISTORY";
    /**
     * 实时信息channel
     */
    public static final String CHANNEL_REALINFO = "REALINFO";
    /**
     * 默认Channel,不满足条件的其他报文会放入此Channel
     */
    public static final String CHANNEL_DEFAULT = "DEFAULT";


    /*-------------------------------HDFS文件路径标识常量-------------------------------------*/

    /**
     * HDFS目录标识，根据报文类型生成
     */
    public static final String HDFS_PATH_KEY = "HDFS_PATH";
    /**
     * 报文时间字段转为yyyy/MM/dd格式，为HDFS分目录使用
     */
    public static final String TIME_PATH = "TIME_PATH";
    /**
     * 默认的路径
     */
    public static final String PATH_DEFAULT = "default";
    /**
     * 实时报文存储路径
     */
    public static final String PATH_REALINFO = "realinfo";
    /**
     * 登录报文存储路径
     */
    public static final String PATH_LOGIN = "login";

}
