package edu.demian.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("edu.demian")
@PropertySource("classpath:application.properties")
public class SpringConfig {

}
