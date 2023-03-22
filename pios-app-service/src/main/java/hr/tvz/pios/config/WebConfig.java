package hr.tvz.pios.config;

import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.EncodedResourceResolver;

/** Konfiguracija za web. */
@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final PiosProperties piosProperties;

  public WebConfig(PiosProperties piosProperties) {
    this.piosProperties = piosProperties;
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/{spring:^[\\w\\-]+}").setViewName("forward:/");
    registry.addViewController("/**/{spring:^[\\w\\-]+}").setViewName("forward:/");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/", "/index.html")
        .addResourceLocations("classpath:/static/")
        .setCacheControl(CacheControl.noCache())
        .resourceChain(false)
        .addResolver(new EncodedResourceResolver());

    registry
        .addResourceHandler("/**")
        .addResourceLocations("classpath:/static/")
        .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
        .resourceChain(false)
        .addResolver(new EncodedResourceResolver());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    String[] corsOrigins = piosProperties.corsOrigins();
    if (ArrayUtils.isNotEmpty(corsOrigins)) {
      registry.addMapping("/api/**").allowedOrigins(corsOrigins).allowedMethods("*");
    }
  }
}
