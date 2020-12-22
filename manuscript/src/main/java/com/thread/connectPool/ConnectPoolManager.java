package com.thread.connectPool;

import java.sql.Connection;
import java.util.List;
import java.util.Vector;

/**
 * 数据库连接池实现
 *  1.
 *  2.1 初始化空闲线程
 *  3.1 调用getConnection方法   获取链接
 * Created by lei on 2020/7/20.
 */
public class ConnectPoolManager {
    //空闲连接池
    private List<Connection> freeConnection = new Vector<Connection>();
    //活动链接池
    private List<Connection> activeConnection = new Vector<Connection>();


}
