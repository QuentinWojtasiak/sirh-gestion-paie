package dev.paie.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "dev.paie.services", "dev.paie.util" })
// @Import(JddConfig.class)
// @ImportResource("classpath:jdd-config.xml")
public class ServicesConfig {

}