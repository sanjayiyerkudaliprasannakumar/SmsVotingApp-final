package voting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


	@Configuration
    @EnableWebSecurity
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    	 
    	@Autowired
    	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	  auth.inMemoryAuthentication().withUser("foo").password("bar").roles("USER");
    	}
     
    	@Override
    	protected void configure(HttpSecurity http) throws Exception {
     
    		http												
    		.csrf().disable()	
    		.authorizeRequests()
    		.antMatchers(HttpMethod.POST, "/api/v1/moderators").permitAll()
    		.antMatchers("/api/v1/polls/*").permitAll()
    		.anyRequest().hasAnyRole("USER").and().httpBasic();
    		
    		/*
    		http
    		.csrf().disable()
    		.authorizeRequests()
    		.antMatchers(HttpMethod.GET,"/moderators/*").hasRole("USER")
    		.antMatchers(HttpMethod.PUT,"/moderators/*").hasRole("USER").and()
    		.httpBasic();
    		
    		*/
    	/*  http.csrf().disable()
    	  	.authorizeRequests()
    	  	.antMatchers("/polls/*").permitAll()
    	    .antMatchers(HttpMethod.POST,"/moderators").permitAll()
    		.anyRequest().hasRole("USER").and()
    		.httpBasic();
    		
    	  */
    	}
    }
