package com.bizzan.bitrade.config;

import com.bizzan.bitrade.util.BigDecimalToDecimal128Converter;
import com.bizzan.bitrade.util.Decimal128ToBigDecimalConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Arrays;

@Configuration
@ConditionalOnProperty(name="spring.data.mongodb.uri")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String uri ;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Bean(name="newMongoTemplate")
    public MongoTemplate getMongoTemplate(MongoDatabaseFactory dbFactory,MappingMongoConverter converter) {
        MongoTemplate template = new MongoTemplate(dbFactory, converter);
        return template;
    }
    @Bean
    @Override
    public MongoDatabaseFactory mongoDbFactory(){
        return new SimpleMongoClientDatabaseFactory(uri);
    }
    @Bean
    @Override
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDatabaseFactory, MongoCustomConversions customConversions, MongoMappingContext mappingContext) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
        converter.setCustomConversions(customConversions);
        converter.setCodecRegistryProvider(mongoDatabaseFactory);
        return converter;
    }
    @Bean
    public MongoCustomConversions mongoCustomConversions() {

        return new MongoCustomConversions(
                Arrays.asList(
                        new BigDecimalToDecimal128Converter(),
                        new Decimal128ToBigDecimalConverter()));
    }

    @Override
    protected String getDatabaseName() {
        return this.database;
    }
}
