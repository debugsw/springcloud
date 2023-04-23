package com.spring.cloud.base.monitor.exception;

import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.admin.MQAdminExtInner;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.MQClientManager;
import org.apache.rocketmq.client.impl.factory.MQClientInstance;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.ServiceState;
import org.apache.rocketmq.common.ThreadFactoryImpl;
import org.apache.rocketmq.common.help.FAQUrl;
import org.apache.rocketmq.common.message.MessageDecoder;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.KVTable;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.logging.InternalLogger;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/21 10:54
 */
public class DefaultMQAdminExceptionImpl implements MQAdminException, MQAdminExtInner {

    private static final Set<String> SYSTEM_GROUP_SET = new HashSet<String>();

    static {
        SYSTEM_GROUP_SET.add( MixAll.DEFAULT_CONSUMER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.DEFAULT_PRODUCER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.TOOLS_CONSUMER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.SCHEDULE_CONSUMER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.FILTERSRV_CONSUMER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.MONITOR_CONSUMER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.CLIENT_INNER_PRODUCER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.SELF_TEST_PRODUCER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.SELF_TEST_CONSUMER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.ONS_HTTP_PROXY_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.CID_ONSAPI_PERMISSION_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.CID_ONSAPI_OWNER_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.CID_ONSAPI_PULL_GROUP );
        SYSTEM_GROUP_SET.add( MixAll.CID_SYS_RMQ_TRANS );
    }

    private final InternalLogger log = ClientLogger.getLog();
    private final DefaultMQAdminException defaultMQAdminException;
    private ServiceState serviceState = ServiceState.CREATE_JUST;
    private MQClientInstance mqClientInstance;
    private RPCHook rpcHook;
    private long timeoutMillis = 20000;
    private Random random = new Random();

    protected ThreadPoolExecutor threadPoolExecutor;

    public DefaultMQAdminExceptionImpl(DefaultMQAdminException defaultMQAdminException, long timeoutMillis) {
        this(defaultMQAdminException, null, timeoutMillis );
    }

    public DefaultMQAdminExceptionImpl(DefaultMQAdminException defaultMQAdminException, RPCHook rpcHook, long timeoutMillis) {
        this.defaultMQAdminException = defaultMQAdminException;
        this.rpcHook = rpcHook;
        this.timeoutMillis = timeoutMillis;
    }


    @Override
    public void start() throws MQClientException {
        switch (this.serviceState) {
            case CREATE_JUST:
                this.serviceState = ServiceState.START_FAILED;

                this.defaultMQAdminException.changeInstanceNameToPID();

                this.mqClientInstance = MQClientManager.getInstance().getOrCreateMQClientInstance( this.defaultMQAdminException, rpcHook );

                boolean registerOK = mqClientInstance.registerAdminExt( this.defaultMQAdminException.getAdminExtGroup(), this );
                if (!registerOK) {
                    this.serviceState = ServiceState.CREATE_JUST;
                    throw new MQClientException( "The adminExt group[" + this.defaultMQAdminException.getAdminExtGroup() + "] has created already, specified another name please." + FAQUrl.suggestTodo( FAQUrl.GROUP_NAME_DUPLICATE_URL ), null );
                }

                mqClientInstance.start();

                log.info( "the adminExt [{}] start OK", this.defaultMQAdminException.getAdminExtGroup() );

                this.serviceState = ServiceState.RUNNING;

                int theadPoolCoreSize = Integer.parseInt( System.getProperty( "rocketmq.admin.threadpool.coresize", "20" ) );

                this.threadPoolExecutor = new ThreadPoolExecutor( theadPoolCoreSize, 100, 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new ThreadFactoryImpl( "DefaultMQAdminExtImpl_" ) );

                break;
            case RUNNING:
            case START_FAILED:
            case SHUTDOWN_ALREADY:
                throw new MQClientException( "The AdminExt service state not OK, maybe started once, " + this.serviceState + FAQUrl.suggestTodo( FAQUrl.CLIENT_SERVICE_NOT_OK ), null );
            default:
                break;
        }
    }

    @Override
    public void shutdown() {
        switch (this.serviceState) {
            case CREATE_JUST:
                break;
            case RUNNING:
                this.mqClientInstance.unregisterAdminExt( this.defaultMQAdminException.getAdminExtGroup() );
                this.mqClientInstance.shutdown();

                log.info( "the adminExt [{}] shutdown OK", this.defaultMQAdminException.getAdminExtGroup() );
                this.serviceState = ServiceState.SHUTDOWN_ALREADY;
                this.threadPoolExecutor.shutdown();
                break;
            case SHUTDOWN_ALREADY:
                break;
            default:
                break;
        }
    }


    @Override
    public void updateBrokerConfig(String brokerAddr,
                                   Properties properties) throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, UnsupportedEncodingException, InterruptedException, MQBrokerException, MQClientException {
        this.mqClientInstance.getMQClientAPIImpl().updateBrokerConfig( brokerAddr, properties, timeoutMillis );
    }

    @Override
    public Properties getBrokerConfig(
            final String brokerAddr) throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, UnsupportedEncodingException, InterruptedException, MQBrokerException {
        return this.mqClientInstance.getMQClientAPIImpl().getBrokerConfig( brokerAddr, timeoutMillis );
    }

    @Override
    public TopicList fetchAllTopicList() throws RemotingException, MQClientException, InterruptedException {
        return this.mqClientInstance.getMQClientAPIImpl().getTopicListFromNameServer( timeoutMillis );
    }

    @Override
    public TopicList fetchTopicsByCLuster(
            String clusterName) throws RemotingException, MQClientException, InterruptedException {
        return this.mqClientInstance.getMQClientAPIImpl().getTopicsByCluster( clusterName, timeoutMillis );
    }

    @Override
    public KVTable fetchBrokerRuntimeStats(
            final String brokerAddr) throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException, MQBrokerException {
        return this.mqClientInstance.getMQClientAPIImpl().getBrokerRuntimeInfo( brokerAddr, timeoutMillis );
    }

    @Override
    public ClusterInfo examineBrokerClusterInfo() throws InterruptedException, MQBrokerException, RemotingTimeoutException, RemotingSendRequestException, RemotingConnectException {
        return this.mqClientInstance.getMQClientAPIImpl().getBrokerClusterInfo( timeoutMillis );
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
    public long searchOffset(MessageQueue messageQueue, long l) throws MQClientException {
        throw new RuntimeException( "not realization Interface" );
    }

    @Override
    public long maxOffset(MessageQueue messageQueue) {
        throw new RuntimeException( "not realization Interface" );
    }

    @Override
    public long minOffset(MessageQueue messageQueue) throws MQClientException {
        throw new RuntimeException( "not realization Interface" );
    }

    @Override
    public long earliestMsgStoreTime(MessageQueue messageQueue) throws MQClientException {
        throw new RuntimeException( "not realization Interface" );
    }

    @Override
    public MessageExt viewMessage(String s) {
        throw new RuntimeException( "not realization Interface" );
    }

    @Override
    public QueryResult queryMessage(String s, String s1, int i, long l, long l1) {
        throw new RuntimeException( "not realization Interface" );
    }

    @Override
    public MessageExt viewMessage(String topic,
                                  String msgId) throws RemotingException, MQBrokerException, InterruptedException, MQClientException {
        try {
            MessageDecoder.decodeMessageId( msgId );
            return this.viewMessage( msgId );
        } catch (Exception e) {
            log.warn( "the msgId maybe created by new client. msgId={}", msgId, e );
        }
        return this.mqClientInstance.getMQAdminImpl().queryMessageByUniqKey( topic, msgId );
    }


}
