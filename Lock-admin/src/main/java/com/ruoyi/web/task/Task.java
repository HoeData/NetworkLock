//package com.ruoyi.web.task;
//
//import com.alibaba.fastjson2.JSON;
//import com.ruoyi.web.controller.pda.PdaDataSynchronizationController;
//import com.ruoyi.web.domain.LockMonitorFlow;
//import com.ruoyi.web.domain.LockWarnInfo;
//import com.ruoyi.web.domain.vo.MonitorPortViewVO;
//import com.ruoyi.web.service.ILockMonitorFlowService;
//import com.ruoyi.web.service.ILockPortInfoService;
//import com.ruoyi.web.service.ILockWarnInfoService;
//import com.ruoyi.web.utils.SnmpUtil;
//import java.util.Date;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.snmp4j.PDU;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@EnableScheduling
//@RequiredArgsConstructor
//@Slf4j
//public class Task {
//
//    private final ILockPortInfoService portInfoService;
//    private final ILockMonitorFlowService monitorFlowService;
//    private final ILockWarnInfoService warnInfoService;
//
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void monitorAbnormalTraffic() {
//        List<MonitorPortViewVO> viewVOList = portInfoService.getMonitorPortList();
//        if (viewVOList.size() > 0) {
//            for (MonitorPortViewVO vo : viewVOList) {
//                try {
//                    if(!StringUtils.contains(vo.getPortIndex(),"console")){
//                        String inFlow = SnmpUtil.getForSnmp(vo.getIp(), vo.getCommunity(),
//                            SnmpUtil.IF_IN_OCTETS + "." + vo.getPortIndex(), PDU.GET);
//                        String outFlow = SnmpUtil.getForSnmp(vo.getIp(), vo.getCommunity(),
//                            SnmpUtil.IF_OUT_OCTETS + "." + vo.getPortIndex(), PDU.GET);
//                        addWarn(inFlow, outFlow, vo.getPortId());
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        }
//    }
//
//    public void addWarn(String inFlow, String outFlow, Integer portInfoId) {
//        long inflowLong = 0;
//        long outFlowLong = 0;
//        if (StringUtils.isNotBlank(inFlow)) {
//            inflowLong = Long.parseLong(inFlow);
//        }
//        if (StringUtils.isNotBlank(outFlow)) {
//            outFlowLong = Long.parseLong(outFlow);
//        }
//        LockWarnInfo warnInfo = warnInfoService.getLastNoConfirmByPortInfoId(portInfoId);
//        if (null == warnInfo) {
//            LockMonitorFlow old = monitorFlowService.getLastForPortId(portInfoId);
//            if (null != old) {
//                String oldInFlow = old.getInFlow();
//                String olOutFlow = old.getOutFlow();
//                if ((inflowLong - Long.parseLong(oldInFlow)) / 1024 > 1024 * 3
//                    || (outFlowLong - Long.parseLong(olOutFlow)) / 1024 > 1024 * 3) {
//                    warnInfo = new LockWarnInfo();
//                    warnInfo.setPortId(portInfoId);
//                    warnInfo.setCreateTime(new Date());
//                    warnInfoService.save(warnInfo);
//                }
//            }
//        }
//        LockMonitorFlow lockMonitorFlow = new LockMonitorFlow();
//        lockMonitorFlow.setPortId(portInfoId);
//        lockMonitorFlow.setInFlow(inFlow);
//        lockMonitorFlow.setOutFlow(outFlow);
//        lockMonitorFlow.setCreateTime(new Date());
//        monitorFlowService.save(lockMonitorFlow);
//    }
//
//}
