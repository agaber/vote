package dev.agaber.vote.service.server;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcConfiguration implements WebMvcConfigurer {
  private static final Logger logger = LoggerFactory.getLogger(WebMvcConfiguration.class);
  private static final Splitter SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

  /**
   * Comma separated list of origins allowed to make cross scripting (CORS) requests.
   * Set it to "*" to allow a local UI to connect to a local API backend.
   * Leave empty to unset (recommended). See https://tinyurl.com/yckysx4f for docs.
   */
  @Value("${dev.agaber.allowedOrigins}")
  private String allowedOrigins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    var allowed = SPLITTER.splitToList(allowedOrigins);
    logger.atInfo().log("dev.agaber.vote.allowedOrigins = {}", allowed);
    if (!allowed.isEmpty()) {
      registry.addMapping("/**").allowedOrigins(allowed.toArray(String[]::new));
    }
  }
}
