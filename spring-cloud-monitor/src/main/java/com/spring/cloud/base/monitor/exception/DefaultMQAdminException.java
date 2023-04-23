package com.spring.cloud.base.monitor.exception;

import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.KVTable;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.common.topic.TopicValidator;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/21 10:54
 */

public class DefaultMQAdminException extends ClientConfig implements MQAdminException {

    private final DefaultMQAdminExceptionImpl defaultMQAdminExceptionImpl;
    private String adminExtGroup = "admin_ext_group";
    private String createTopicKey = TopicValidator.AUTO_CREATE_TOPIC_KEY_TOPIC;
    private long timeoutMillis = 5000;

    public DefaultMQAdminException() {
        this.defaultMQAdminExceptionImpl = new DefaultMQAdminExceptionImpl( this, null, timeoutMillis );
    }

    public DefaultMQAdminException(long timeoutMillis) {
        this.defaultMQAdminExceptionImpl = new DefaultMQAdminExceptionImpl( this, null, timeoutMillis );
    }

    public DefaultMQAdminException(RPCHook rpcHook) {
        this.defaultMQAdminExceptionImpl = new DefaultMQAdminExceptionImpl( this, rpcHook, timeoutMillis );
    }

    public DefaultMQAdminException(RPCHook rpcHook, long timeoutMillis) {
        this.defaultMQAdminExceptionImpl = new DefaultMQAdminExceptionImpl( this, rpcHook, timeoutMillis );
    }

    public DefaultMQAdminException(final String adminExtGroup, long timeoutMillis) {
        this.adminExtGroup = adminExtGroup;
        this.defaultMQAdminExceptionImpl = new DefaultMQAdminExceptionImpl( this, timeoutMillis );
    }


    @Override
    public void createTopic(String s, String s1, int i) throws MQClientException {
        throw new RuntimeException( "not realization Interface" );
    }

    @Override
    public void createTopic(String s, String s1, int i, int i1) throws MQClientException {
        throw new RuntimeException( "not realization Interface" );
    }

    @Override
    public long searchOffset(MessageQueue mq, long timestamp) throws MQClientException {
        return defaultMQAdminExceptionImpl.searchOffset( mq, timestamp );
    }

    @Override
    public long maxOffset(MessageQueue mq) throws MQClientException {
        return defaultMQAdminExceptionImpl.maxOffset( mq );
    }

    @Override
    public long minOffset(MessageQueue mq) throws MQClientException {
        return defaultMQAdminExceptionImpl.minOffset( mq );
    }

    @Override
    public long earliestMsgStoreTime(MessageQueue mq) throws MQClientException {
        return defaultMQAdminExceptionImpl.earliestMsgStoreTime( mq );
    }

    @Override
    public MessageExt viewMessage(
            String offsetMsgId) throws RemotingException, MQBrokerException, InterruptedException, MQClientException {
        return defaultMQAdminExceptionImpl.viewMessage( offsetMsgId );
    }

    @Override
    public QueryResult queryMessage(String topic, String key, int maxNum, long begin, long end)
            throws MQClientException, InterruptedException {
        return defaultMQAdminExceptionImpl.queryMessage( topic, key, maxNum, begin, end );
    }

    @Override
    public void start() throws MQClientException {
        defaultMQAdminExceptionImpl.start();
    }

    @Override
    public void shutdown() {
        defaultMQAdminExceptionImpl.shutdown();
    }

    @Override
    public void updateBrokerConfig(String brokerAddr,
                                   Properties properties) throws RemotingConnectException, RemotingSendRequestException,
            RemotingTimeoutException, UnsupportedEncodingException, InterruptedException, MQBrokerException, MQClientException {
        defaultMQAdminExceptionImpl.updateBrokerConfig( brokerAddr, properties );
    }

    @Override
    public Properties getBrokerConfig(final String brokerAddr) throws RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException, UnsupportedEncodingException, InterruptedException, MQBrokerException {
        return defaultMQAdminExceptionImpl.getBrokerConfig( brokerAddr );
    }

    @Override
    public TopicList fetchAllTopicList() throws RemotingException, MQClientException, InterruptedException {
        return this.defaultMQAdminExceptionImpl.fetchAllTopicList();
    }

    @Override
    public TopicList fetchTopicsByCLuster(
            String clusterName) throws RemotingException, MQClientException, InterruptedException {
        return this.defaultMQAdminExceptionImpl.fetchTopicsByCLuster( clusterName );
    }

    @Override
    public KVTable fetchBrokerRuntimeStats(
            final String brokerAddr) throws RemotingConnectException, RemotingSendRequestException,
            RemotingTimeoutException, InterruptedException, MQBrokerException {
        return this.defaultMQAdminExceptionImpl.fetchBrokerRuntimeStats( brokerAddr );
    }

    @Override
    public ClusterInfo examineBrokerClusterInfo() throws InterruptedException, RemotingConnectException, RemotingTimeoutException,
            RemotingSendRequestException, MQBrokerException {
        return defaultMQAdminExceptionImpl.examineBrokerClusterInfo();
    }

    @Override
    public MessageExt viewMessage(String topic, String msgId)
            throws RemotingException, MQBrokerException, InterruptedException, MQClientException {
        return this.defaultMQAdminExceptionImpl.viewMessage( topic, msgId );
    }

    public String getAdminExtGroup() {
        return adminExtGroup;
    }


}
