package com.sct.redistest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_BLOCK_WHEN_EXHAUSTED;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_EVICTION_POLICY_CLASS_NAME;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_FAIRNESS;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_JMX_ENABLE;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_LIFO;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_TEST_ON_BORROW;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_TEST_ON_CREATE;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_TEST_ON_RETURN;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_TEST_WHILE_IDLE;
import static org.apache.commons.pool2.impl.BaseObjectPoolConfig.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
import static org.apache.commons.pool2.impl.GenericObjectPoolConfig.*;

@Configuration
@PropertySource("classpath:config/redis.properties")
public class SpringDataRedisConfig {

    @Value("${redis.pool.maxTotal}")
    private int maxTotal = DEFAULT_MAX_TOTAL;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle = DEFAULT_MAX_IDLE;
    @Value("${redis.pool.minIdle}")
    private int minIdle = DEFAULT_MIN_IDLE;

    @Value("${redis.pool.lifo}")
    private boolean lifo = DEFAULT_LIFO;
    @Value("${redis.pool.fairness}")
    private boolean fairness = DEFAULT_FAIRNESS;
    @Value("${redis.pool.maxWaitMillis}")
    private long maxWaitMillis = DEFAULT_MAX_WAIT_MILLIS;
    @Value("${redis.pool.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis = DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    @Value("${redis.pool.softMinEvictableIdleTimeMillis}")
    private long softMinEvictableIdleTimeMillis = DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    @Value("${redis.pool.numTestsPerEvictionRun}")
    private int numTestsPerEvictionRun = DEFAULT_NUM_TESTS_PER_EVICTION_RUN;
    @Value("${redis.pool.evictionPolicyClassName}")
    private String evictionPolicyClassName = DEFAULT_EVICTION_POLICY_CLASS_NAME;
    @Value("${redis.pool.testOnCreate}")
    private boolean testOnCreate = DEFAULT_TEST_ON_CREATE;
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow = DEFAULT_TEST_ON_BORROW;
    @Value("${edis.pool.testOnBorrow}")
    private boolean testOnReturn = DEFAULT_TEST_ON_RETURN;
    @Value("${redis.pool.testWhileIdle}")
    private boolean testWhileIdle = DEFAULT_TEST_WHILE_IDLE;
    @Value("${redis.pool.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis = DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
    @Value("${redis.pool.blockWhenExhausted}")
    private boolean blockWhenExhausted = DEFAULT_BLOCK_WHEN_EXHAUSTED;
    @Value("${redis.pool.jmxEnabled}")
    private boolean jmxEnabled = DEFAULT_JMX_ENABLE;

    @Value("${redis.hostName}")
    private String hostName = "localhost";
    @Value("${redis.port}")
    private int port = Protocol.DEFAULT_PORT;
    @Value("${redis.timeout}")
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.usePool}")
    private boolean usePool = true;
    @Value("${redis.dbIndex}")
    private int dbIndex = 0;
    @Value("${redis.convertPipelineAndTxResults}")
    private boolean convertPipelineAndTxResults = true;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(this.maxTotal);
        jedisPoolConfig.setMaxIdle(this.maxIdle);
        jedisPoolConfig.setMinIdle(this.minIdle);
        jedisPoolConfig.setLifo(this.lifo);
        jedisPoolConfig.setFairness(this.fairness);
        jedisPoolConfig.setMaxWaitMillis(this.maxWaitMillis);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(this.softMinEvictableIdleTimeMillis);
        jedisPoolConfig.setNumTestsPerEvictionRun(this.numTestsPerEvictionRun);
        jedisPoolConfig.setEvictionPolicyClassName(this.evictionPolicyClassName);
        jedisPoolConfig.setTestOnCreate(this.testOnCreate);
        jedisPoolConfig.setTestOnBorrow(this.testOnBorrow);
        jedisPoolConfig.setTestOnReturn(this.testOnReturn);
        jedisPoolConfig.setTestWhileIdle(this.testWhileIdle);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
        jedisPoolConfig.setBlockWhenExhausted(this.blockWhenExhausted);
        jedisPoolConfig.setJmxEnabled(this.jmxEnabled);

        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(hostName);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setTimeout(timeout);
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig());

        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate jedisPool() throws Exception {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

}
