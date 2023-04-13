package com.spring.cloud.base.utils;

import com.mybatisflex.core.MybatisFlexBootstrap;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import com.spring.cloud.base.mapper.AccountMapper;
import com.zaxxer.hikari.HikariDataSource;

import java.util.List;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/12 15:30
 */
public class HelloWorld {
    public static void main(String... args) {

        //创建数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://rm-bp134v0ek*xn118296wto.mysql.rds.aliy*1uncs.com:3306/demo");
        dataSource.setUsername("*****");
        dataSource.setPassword("*****");

        //配置数据源
        MybatisFlexBootstrap.getInstance()
                .setDataSource(dataSource)
                .addMapper(AccountMapper.class)
                .start();

        //获取 mapper
        AccountMapper mapper = MybatisFlexBootstrap.getInstance()
                .getMapper(AccountMapper.class);

        //示例1：查询 id=1 的数据
        Account account = mapper.selectOneById(1);

        //示例2：者使用 Db + Row 查询
        String sql = "select * from tb_account where age > ?";
        List<Row> rows = Db.selectListBySql(sql, 18);
    }
}
