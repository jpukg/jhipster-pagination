package io.github.jhipster.config;

import io.github.jhipster.config.pagination.PageAndSortResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Additional WEB MVC configuration.
 *
 * @author Przemek Nowak [przemek.nowak.pl@gmail.com]
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public PageAndSortResolver pageAndSortQueryResolver() {
        return new PageAndSortResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(pageAndSortQueryResolver());
    }

}
