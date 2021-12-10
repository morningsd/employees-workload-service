package edu.demian.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({"edu.demian"})
@EnableScheduling
public class ApplicationConfig {

}
