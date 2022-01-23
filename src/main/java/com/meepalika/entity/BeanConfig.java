package com.meepalika.entity;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

//spring annotation
@Configuration
//spring jpa audit annotation
//annotation enables the auditing in jpa via annotation configuration
@EnableJpaAuditing(auditorAwareRef = "aware")
public class BeanConfig {

	@Value("${default.admin.user}")
	Long defaultadminuser;

	//helps to aware the application's current auditor.
	//this is some kind of user mostly.
	@Bean
	public AuditorAware<Long> aware() {

/*		Long defaultUserId = new Long(200L);



		if(MDC.get("loggedInUserStr") == null)
			return () -> Optional.of(defaultUserId);
		}else {
			Long userID = Long.parseLong(MDC.get("loggedInUserStr"));
			return () -> Optional.of(userID);
		}*/

		System.out.println("Long value "+defaultadminuser.longValue());
		if(MDC.get("loggedInUserStr") == null){
			return () -> Optional.of(defaultadminuser);
		}else{
			Long userID = Long.parseLong(MDC.get("loggedInUserStr"));
			return () -> Optional.of(userID);
		}

	}

}

