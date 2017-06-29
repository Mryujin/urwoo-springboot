package com.urwoo.mongodb.config;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "mongodb")
@Data
public class MongodbConfig {

    private List<ServerConfig> servers;


    @Bean
    public MongoCredential mongoCredential(@Value("${spring.data.mongodb.cache.username}") String userName,
                                           @Value("${spring.data.mongodb.cache.password}") String password,
                                           @Value("${spring.data.mongodb.cache.database}") String databse) {
        return MongoCredential.createScramSha1Credential(userName, databse, password.toCharArray());
    }

    @Bean
    public MongoClient mongoClientSub(@Qualifier("mongoCredential") MongoCredential mongoCredential) {
        List<ServerAddress> serverList = new ArrayList<>();
        getServers().forEach(e -> {
            serverList.add(new ServerAddress(e.getIp(), e.getPort()));
        });
        return new MongoClient(serverList, Arrays.asList(mongoCredential), getConfOptions());
    }

    @Bean
    public static MongoClient mongoClient(@Value("${spring.data.mongodb.uri}") String uri) {
        MongoClientURI mongoURI = new MongoClientURI(uri);
        return new MongoClient(mongoURI);
    }

    @Bean
    public static MongoDatabase mongoDatabase(@Qualifier("mongoClient") MongoClient mongoClient,
                                              @Value("${spring.data.mongodb.database}") String databse) {
        return mongoClient.getDatabase(databse);
    }

    @Bean
    public static MongoDatabase mongoCacheDatabase(@Qualifier("mongoClientSub") MongoClient mongoClient,
                                                   @Value("${spring.data.mongodb.cache.database}") String databse) {
        return mongoClient.getDatabase(databse);
    }

    @Bean
    public static DB mongoDB(@Qualifier("mongoClient") MongoClient mongoClient,
                             @Value("${spring.data.mongodb.database}") String databse) {
        return new DB(mongoClient, databse);
    }

    @Bean
    public static GridFS gridFS(@Qualifier("mongoDB") DB mongoDB,
                                @Value("${spring.data.mongodb.database}") String databse) {
        return new GridFS(mongoDB, databse);
    }

    public static MongoClientOptions getConfOptions() {
        return new MongoClientOptions.Builder().socketKeepAlive(true)       // 是否保持长链接
                .connectTimeout(1000 * 60 * 5)                              // 链接超时时间
                .socketTimeout(1000 * 60 * 5)                               // read数据超时时间
                .readPreference(ReadPreference.primary())                   // 优先策略
                .connectionsPerHost(30)                                     // 每个host最大请求数
                .maxWaitTime(1000 * 60 * 5)                                 // 长链接的最大等待时间
                .threadsAllowedToBlockForConnectionMultiplier(50)           // 一个socket最大的等待请求数
                .writeConcern(WriteConcern.ACKNOWLEDGED).writeConcern(WriteConcern.JOURNALED)
                /*
                 * .writeConcern(WriteConcern.W2) .writeConcern(new
                 * WriteConcern(2, 200, false, true))
                 * .writeConcern(WriteConcern.REPLICA_ACKNOWLEDGED)
                 */
                .build();
    }
}
