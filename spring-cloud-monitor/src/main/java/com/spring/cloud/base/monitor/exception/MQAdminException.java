package com.spring.cloud.base.monitor.exception;


import org.apache.rocketmq.client.MQAdmin;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.KVTable;
import org.apache.rocketmq.common.protocol.body.TopicList;
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
public interface MQAdminException extends MQAdmin {


    void start() throws MQClientException;

    void shutdown();

    void updateBrokerConfig(final String brokerAddr, final Properties properties) throws RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException, UnsupportedEncodingException, InterruptedException, MQBrokerException, MQClientException;

    Properties getBrokerConfig(final String brokerAddr) throws RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException, UnsupportedEncodingException, InterruptedException, MQBrokerException;

    TopicList fetchAllTopicList() throws RemotingException, MQClientException, InterruptedException;

    TopicList fetchTopicsByCLuster(
            String clusterName) throws RemotingException, MQClientException, InterruptedException;

    KVTable fetchBrokerRuntimeStats(
            final String brokerAddr) throws RemotingConnectException, RemotingSendRequestException,
            RemotingTimeoutException, InterruptedException, MQBrokerException;

    ClusterInfo examineBrokerClusterInfo() throws InterruptedException, MQBrokerException, RemotingTimeoutException,
            RemotingSendRequestException, RemotingConnectException;


}
