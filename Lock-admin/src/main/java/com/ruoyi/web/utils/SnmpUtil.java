package com.ruoyi.web.utils;

import java.io.IOException;
import java.util.Arrays;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpUtil {

    public static final String SYS_DEC = "1.3.6.1.2.1.1.1";
    //设备开启时间
    public static final String SYS_UP_TIME = "1.3.6.1.2.1.1.5";
    //设备名称
    public static final String SYS_NAME = "1.3.6.1.2.1.1.3";
    //网卡接口速率
    public static final String IF_SPEED = "1.3.6.1.2.1.2.2.1.5.2";
    //网卡接口当前时刻进流量
    public static final String IF_IN_OCTETS = "1.3.6.1.2.1.2.2.1.10";
    //网卡接口当前时刻出流量
    public static final String IF_OUT_OCTETS = "1.3.6.1.2.1.2.2.1.16";
    public static final String NETWORK_PORT_NUMBER = "1.3.6.1.2.1.2.1";

    //1.3.6.1.2.1.2.1
    public static String getMessageByIpAndOid(String ip, String oid) throws IOException {
        String result = null;
        // 1. 创建 SNMP 管理器
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("private"));
        target.setAddress(GenericAddress.parse("udp:" + ip + "/161"));
        target.setRetries(2);
        target.setTimeout(1000);
        target.setVersion(SnmpConstants.version2c);
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        snmp.listen();
        // 2. 创建 OID
        OID oid1 = new OID(oid);

        // 3. 发送 SNMP 请求并处理响应
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(oid1));
        pdu.setType(PDU.GETNEXT);
        ResponseEvent event = snmp.send(pdu, target);
        PDU response = event.getResponse();

        if (response == null) {
            System.out.println("没有得到响应");
        } else {
            result = String.valueOf(response.get(0).getVariable());
        }

        // 4. 关闭 SNMP 管理器
        snmp.close();
        return result;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getForSnmp("192.168.0.32","private",IF_SPEED,PDU.GET));
            System.out.println(getForSnmp("192.168.0.32","private",NETWORK_PORT_NUMBER,PDU.GETNEXT));
            System.out.println(getForSnmp("192.168.0.32","private",IF_IN_OCTETS,PDU.GET));
            System.out.println(getForSnmp("192.168.0.32","private",IF_OUT_OCTETS,PDU.GET));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static void setInterfaceAdminStatus(String ip, int interfaceIndex, boolean enabled)
        throws IOException {
        String result = null;
        // 1. 创建 SNMP 管理器
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("private"));
        target.setAddress(GenericAddress.parse("udp:" + ip + "/161"));
        target.setRetries(2);
        target.setTimeout(1000);
        target.setVersion(SnmpConstants.version2c);
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);

        // 2. 创建 OID
        OID oid1 = new OID("1.3.6.1.2.1.2.2.1.7.1");
        VariableBinding vbs[] = new VariableBinding[]{
            new VariableBinding(oid1, new OctetString("1"))};
        // 3. 发送 SNMP 请求并处理响应
        PDU pdu = new PDU();
        pdu.setVariableBindings(Arrays.asList(vbs));
        String adminStatus = enabled ? "1" /* up */ : "2" /* down */;
        pdu.setType(PDU.SET);
        ResponseEvent event = snmp.send(pdu, target);
        PDU response = event.getResponse();

        if (response == null) {
            System.out.println("没有得到响应");
        } else {
            result = String.valueOf(response.get(0).getVariable());
        }
        System.out.println(result);
        // 4. 关闭 SNMP 管理器
        snmp.close();
    }

    public static String getForSnmp(String ip,String community,  String oid, int type) {
        Snmp snmp = null;
        try {
            String result = null;
            // 1. 创建 SNMP 管理器
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString("private"));
            target.setAddress(GenericAddress.parse("udp:" + ip + "/161"));
            target.setRetries(2);
            target.setTimeout(1000);
            target.setVersion(SnmpConstants.version2c);
            TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();
            // 2. 创建 OID
            OID oid1 = new OID(oid);
            // 3. 发送 SNMP 请求并处理响应
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(oid1));
            pdu.setType(type);
            ResponseEvent event = snmp.send(pdu, target);
            PDU response = event.getResponse();

            if (response != null) {
                result = String.valueOf(response.get(0).getVariable());
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                snmp.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
