package com.ccic.payroll.dbutil;


import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(name = "db.properties", value = {"classpath:db.properties"})
public class MySQLDataSource {
    private static Logger log = LoggerFactory.getLogger(MySQLDataSource.class);
    private static String DB_HOST;
    private static int DB_PORT;
    private static String DB_NAME;
    private static String DB_USER;
    private static String DB_USER_PASSWORD;

    private String DB2_HOST;
    private int DB2_PORT;
    private  String DB2_NAME;
    private String DB2_USER;
    private String DB2_USER_PASSWORD;

    public MySQLDataSource(@Value(value = "${db_host}") String db_host,
                           @Value(value = "${db_port}") int db_port,
                           @Value(value = "${db_name}") String db_name,
                           @Value(value = "${db_user}") String db_user,
                           @Value(value = "${db_user_password}") String db_password,
                           @Value(value = "${db2_host}") String db2_host,
                           @Value(value = "${db2_port}") int db2_port,
                           @Value(value = "${db2_name}") String db2_name,
                           @Value(value = "${db2_user}") String db2_user,
                           @Value(value = "${db2_user_password}") String db2_password
    ) {
        this.DB_HOST=db_host;
        this.DB_PORT = db_port;
        this.DB_NAME = db_name;
        this.DB_USER = db_user;
        this.DB_USER_PASSWORD = db_password;

        this.DB2_HOST=db2_host;
        this.DB2_PORT = db2_port;
        this.DB2_NAME = db2_name;
        this.DB2_USER = db2_user;
        this.DB2_USER_PASSWORD = db2_password;

    }

    @Bean
    public BasicDataSource1 getDataSource() {
        log.info("本地数据源1......." + DB_HOST + "  " + DB_USER_PASSWORD);

        BasicDataSource1 ds = new BasicDataSource1();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(String.format("jdbc:mysql://%s:%d/%s?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf8", DB_HOST, DB_PORT, DB_NAME));

        ds.setUsername(DB_USER);
        ds.setPassword(DB_USER_PASSWORD);
        // setMaxOpenPreparedStatements(10);
        ds.setMinIdle(20);
        ds.setMaxIdle(20);
        ds.setInitialSize(20); // 20
        // ds.setMaxWaitMillis(10000);

        return ds;
    }

    @Bean
    public BasicDataSource2 getDataSource2() {

        log.info("远程数据源2......." + DB2_HOST + "  " + DB2_USER_PASSWORD);

        BasicDataSource2 ds = new BasicDataSource2();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(String.format("jdbc:mysql://%s:%d/%s?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf8", DB2_HOST, DB2_PORT, DB2_NAME));

        ds.setUsername(DB2_USER);
        ds.setPassword(DB2_USER_PASSWORD);
        // setMaxOpenPreparedStatements(10);
        ds.setMinIdle(20);
        ds.setMaxIdle(20);
        ds.setInitialSize(20); // 20
        // ds.setMaxWaitMillis(10000);

        return ds;
    }
}
