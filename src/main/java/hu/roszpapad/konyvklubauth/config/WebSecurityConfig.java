package hu.roszpapad.konyvklubauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    //
    @Autowired
    DataSource dataSource;

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new JdbcUserDetails();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() throws Exception {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceBean());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

//    @Autowired
//    @Profile("dev_jwt")
//    public void globalUserDetailsJWT(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().passwordEncoder(passwordEncoder).dataSource(dataSource);
//    }

    @Autowired
//    @Profile("dev_jdbcTokenStore")
    public void globalUserDetailsJDBC(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }



    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
//		http.authorizeRequests().antMatchers("/login","/logout.do", "/approval/revoke").permitAll()
//		.antMatchers("/oauth/token/revokeById/**").permitAll()
//		.antMatchers("/tokens/**").permitAll()
//		.and()
//                .formLogin()
//                .loginProcessingUrl("/login.do")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .loginPage("/login")
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout.do"))
//                .and()
//                .userDetailsService(userDetailsServiceBean());
        // @formatter:on
        http.authorizeRequests().antMatchers("/login").permitAll()
                .antMatchers("/oauth/token/revokeById/**").permitAll()
                .antMatchers("/tokens/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().userDetailsService(userDetailsServiceBean()).cors().disable();
        ;

//        http.requestMatchers()
//                .antMatchers("/login", "/oauth/authorize")
//                .and()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .permitAll()
// .and()
//                .userDetailsService(userDetailsServiceBean());
    }
}
