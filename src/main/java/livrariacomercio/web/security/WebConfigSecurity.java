package livrariacomercio.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable().authorizeRequests()
      .antMatchers(HttpMethod.GET,"/lojacomercio").permitAll()
      .antMatchers(HttpMethod.GET,"/").permitAll()
      .antMatchers(HttpMethod.GET,"/cadastroproduto").hasAuthority("ROLE_ADMIN")
      .antMatchers(HttpMethod.GET,"/adminarea").hasAuthority("ROLE_ADMIN")
      .antMatchers(HttpMethod.POST,"/cadastrousuario").permitAll()
      .antMatchers(HttpMethod.GET,"/finalizar").authenticated()
      .antMatchers(HttpMethod.GET,"/meucadastro").authenticated()
      .antMatchers(HttpMethod.GET,"/editarsenha").authenticated()
     //.antMatchers(HttpMethod.GET,"/finalizar").authenticated()
      .and().formLogin().permitAll()
      .loginPage("/login")
      .defaultSuccessUrl("/")
      .failureUrl("/login?error=true")
      .and().logout().logoutSuccessUrl("/login")
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(implementacaoUserDetailsService)
		 .passwordEncoder(new BCryptPasswordEncoder());

	}

}
