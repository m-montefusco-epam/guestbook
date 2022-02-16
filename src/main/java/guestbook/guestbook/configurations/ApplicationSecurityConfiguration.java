package guestbook.guestbook.configurations;

import guestbook.guestbook.auths.GuestBookAccountDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Massimo_Montefusco
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    final Logger logger = LoggerFactory.getLogger(ApplicationSecurityConfiguration.class);

    @Autowired
    private GuestBookAccountDetailsService guestBookAccountDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(guestBookAccountDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(11));
        provider.setAuthoritiesMapper(authoritiesMapper());
        return provider;
    }

    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper(){
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(true);
        authorityMapper.setDefaultAuthority("GUEST");
        return authorityMapper;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("HttpSecurity configuration: " + http);
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/webjars/**","/**").permitAll()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/username", "/currentusername").permitAll()
                .antMatchers("/write","/upload").hasAnyRole("USER")
                .antMatchers("/manage", "/modify", "/update" ).hasAnyRole("ADMIN")
                .antMatchers("/editpost/**","/approvepost/**","/deletepost/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
            .loginPage("/login").permitAll()
            .and()
            .logout().invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/logout-success").permitAll();
        logger.info("HttpSecurity configuration completed");
    }

}
