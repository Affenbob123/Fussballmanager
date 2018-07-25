package fussballmanager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import fussballmanager.service.spiel.Spiel;

/**
 * Spring Boot Configuration. 
 * 
 * @author katz.bastian
 */
@Configuration
public class FussballmanagerConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
        .antMatchers("/login**", "/logout**", "/accounterstellen").permitAll()
        .antMatchers("/admin", "/h2_console/**").hasRole("ADMIN")
		.anyRequest().fullyAuthenticated()
		.and().formLogin().loginPage("/login").loginProcessingUrl("/login").and()
		.logout().permitAll();
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}
}

