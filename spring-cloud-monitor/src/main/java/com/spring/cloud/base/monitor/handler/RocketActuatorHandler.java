package com.spring.cloud.base.monitor.handler;

import com.spring.cloud.base.monitor.exception.DefaultMQAdminException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.KVTable;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/21 15:53
 */
@Slf4j
public class RocketActuatorHandler {
    private final DefaultMQAdminException mqAdmin;

    public RocketActuatorHandler(DefaultMQAdminException mqAdmin) {
        this.mqAdmin = mqAdmin;
    }

    /**
     * 获取集群信息
     *
     * @return
     */
    public Serializable collectClusterNum() {
        ClusterInfo clusterInfo = null;
        String errMsg = null;
        try {
            clusterInfo = mqAdmin.examineBrokerClusterInfo();
        } catch (InterruptedException | RemotingSendRequestException
                 | RemotingTimeoutException | RemotingConnectException
                 | MQBrokerException e) {
            log.error( e.getMessage(), e );
            errMsg = e.getMessage();
        }
        return null == clusterInfo ? errMsg : (HashMap<String, BrokerData>) clusterInfo.getBrokerAddrTable();
    }

    /**
     * 获取rocket TPS
     *
     * @return
     * @throws Exception
     */
    public Double collectClusterTps() throws Exception {
        ClusterInfo clusterInfo = mqAdmin.examineBrokerClusterInfo();
        double totalTps = 0d;
        for (Map.Entry<String, BrokerData> stringBrokerDataEntry : clusterInfo.getBrokerAddrTable().entrySet()) {
            BrokerData brokerData = stringBrokerDataEntry.getValue();
            // 选择 Master 节点
            String brokerAddr = brokerData.getBrokerAddrs().get( MixAll.MASTER_ID );
            if (StringUtils.isNotBlank( brokerAddr )) {
                KVTable runtimeStatsTable = mqAdmin.fetchBrokerRuntimeStats( brokerAddr );
                HashMap<String, String> runtimeStatus = runtimeStatsTable.getTable();
                double putTps = Math.ceil( Double.parseDouble( runtimeStatus.get( "putTps" ).split( " " )[0] ) );
                totalTps = totalTps + putTps;
            }
        }
        return totalTps;
    }
}
