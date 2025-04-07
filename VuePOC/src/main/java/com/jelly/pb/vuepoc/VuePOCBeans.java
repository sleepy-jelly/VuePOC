package com.jelly.pb.vuepoc;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class VuePOCBeans {
	
	   
	private static final String X_REQUESTED_WITH ="X-Requested-With";
	
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    
    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        // Explicitly list all allowed headers
        config.setAllowedHeaders(List.of( 
            ORIGIN,
            ACCESS_CONTROL_ALLOW_ORIGIN,
            CONTENT_TYPE,
            ACCEPT,
            AUTHORIZATION,
            X_REQUESTED_WITH,
            ACCESS_CONTROL_REQUEST_METHOD,
            ACCESS_CONTROL_REQUEST_HEADERS,
            ACCESS_CONTROL_ALLOW_CREDENTIALS,
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        // Explicitly list all exposed headers
        config.setExposedHeaders(List.of(
            ORIGIN,
            ACCESS_CONTROL_ALLOW_ORIGIN,
            CONTENT_TYPE,
            ACCEPT,
            AUTHORIZATION,
            X_REQUESTED_WITH,
            ACCESS_CONTROL_REQUEST_METHOD,
            ACCESS_CONTROL_REQUEST_HEADERS,
            ACCESS_CONTROL_ALLOW_CREDENTIALS
        ));
        // Explicitly list all allowed methods
        config.setAllowedMethods(List.of(
            GET.name(),
            POST.name(),
            PUT.name(),
            PATCH.name(),
            DELETE.name(),
            OPTIONS.name()
        ));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    } 
    
    
    @Bean
   	SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
   	    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
   	    sessionFactory.setDataSource(dataSource);
   	    sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mappers/**/*Mapper.xml"));
   	    sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatisConfig.xml")); 
//   	    org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//   	    configuration.setMapUnderscoreToCamelCase(true);
//   	    sessionFactory.setConfiguration(configuration);
   	    
   	    return sessionFactory.getObject();
   	}
	

}
