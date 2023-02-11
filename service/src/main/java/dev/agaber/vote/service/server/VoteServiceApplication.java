package dev.agaber.vote.service.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for this service. It will launch an embedded Tomcat server.
 *
 * <p>It will also scan packages for classes annotated with RestController. By default, this only
 * scans the same package of the Application class. To scan other packages we need to explicitly
 * declare them (there's more than one way to do this).
 */
@SpringBootApplication(scanBasePackages = {"dev.agaber.vote.service.elections"})
public class VoteServiceApplication {

  public static void main(String... args) {
    SpringApplication.run(VoteServiceApplication.class, args);
  }
}

// Note: If I ever get uncomfortable with having this class auto-detected:
// https://www.baeldung.com/spring-boot-main-class