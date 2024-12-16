package fsk.glcc.gevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//	(exclude = {
//		DataSourceAutoConfiguration.class,
//        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class 
//        })
public class GeventsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeventsApplication.class, args);
	}

}
