package com.spring.cloud.dev;

import java.sql.*;
import java.util.Date;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/6/25 10:43
 */
public class DatabaseMonitor {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java DatabaseMonitor <dbUrl> <dbUsername> <dbPassword>");
            System.exit(1);
        }

        String dbUrl = args[0];
        String dbUsername = args[1];
        String dbPassword = args[2];

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            Statement stmt = conn.createStatement();
            while (true) {
                // 获取当前时间、最大连接数、线程缓存大小、InnoDB缓冲池大小和当前连接数
                ResultSet rs = stmt.executeQuery("SELECT NOW(), @@max_connections, @@thread_cache_size, @@innodb_buffer_pool_size, COUNT(*) FROM information_schema.processlist WHERE db = 'your_database_name'");
                if (rs.next()) {
                    Date currentTime = rs.getTimestamp(1);
                    int maxConnections = rs.getInt(2);
                    int threadCacheSize = rs.getInt(3);
                    int innodbBufferPoolSize = rs.getInt(4);
                    int currentConnections = rs.getInt(5);
                    System.out.println(currentTime + ": Max Connections=" + maxConnections + ", Thread Cache Size=" + threadCacheSize + ", InnoDB Buffer Pool Size=" + innodbBufferPoolSize + ", Current Connections=" + currentConnections);
                }
                Thread.sleep(1000); // 暂停1秒
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
