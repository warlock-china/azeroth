package cn.com.warlock.common.util;

import java.net.InetAddress;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class NodeNameHolder {

    private static String nodeId;

    public static String getNodeId() {
        if (nodeId != null)
            return nodeId;
        try {
            nodeId = InetAddress.getLocalHost().getHostName() + "_"
                     + RandomStringUtils.random(6, true, true).toLowerCase();
        } catch (Exception e) {
            nodeId = UUID.randomUUID().toString();
        }
        return nodeId;
    }

}
