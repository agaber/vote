package dev.agaber.vote.service.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * The main entry point for this service. It will launch an embedded Tomcat server.
 *
 * <p>It will also scan packages for classes annotated with RestController. By default, this only
 * scans the same package of the Application class. To scan other packages we need to explicitly
 * declare them (there's more than one way to do this).
 */
@SpringBootApplication(
    scanBasePackages = {
        "dev.agaber.vote.service.elections",
        "dev.agaber.vote.service.server",
        "dev.agaber.vote.service.storage",
    })
@EnableMongoRepositories("dev.agaber.vote.service.storage")
public class VoteServiceApplication {
  private static final Logger logger = LoggerFactory.getLogger(VoteServiceApplication.class);

  @Bean
  public ServletRegistrationBean servletRegistrationBean(){
    return new ServletRegistrationBean(new HealthzServlet(), "/healthz");
  }

  public static void main(String... args) {
    logger.atInfo().log("Staring...");
    SpringApplication.run(VoteServiceApplication.class, args);
  }
}

// Note: If I ever get uncomfortable with having this class auto-detected:
// https://www.baeldung.com/spring-boot-main-class
