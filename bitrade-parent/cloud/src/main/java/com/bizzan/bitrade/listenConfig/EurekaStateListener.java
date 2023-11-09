package com.bizzan.bitrade.listenConfig;

import com.netflix.appinfo.InstanceInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.mail.javamail.MimeMailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class EurekaStateListener {
	
	private final static Logger logger = LoggerFactory.getLogger(EurekaStateListener.class);
	 
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;
    @Value("${spark.system.host}")
    private String host;
    @Value("${spark.system.name}")
    private String company;

    @Value("${spark.system.admins}")
    private String admins;
    
    @Value("${spark.system.admin-phones}")
    private String adminPhones;
    
	@EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceCanceledEvent event) {
        String msg="您的服务"+event.getAppName()+"\n"+event.getServerId()+"已下线";
        logger.info(msg);
    }
 
    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        String msg="服务"+instanceInfo.getAppName()+"\n"+  instanceInfo.getHostName()+":"+ instanceInfo.getPort()+ " \nip: " +instanceInfo.getIPAddr() +"进行注册";
        logger.info(msg);
    }
 
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        logger.info("服务{}进行续约", event.getServerId() +"  "+ event.getAppName());
    }
 
    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        logger.info("注册中心启动,{}", System.currentTimeMillis());
    }
 
    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        logger.info("注册中心服务端启动,{}", System.currentTimeMillis());
    }

}
