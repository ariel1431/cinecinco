package com.QuintoTrainee.CineCinco.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.QuintoTrainee.CineCinco.Oauth2.CustomOAuth2User;
import com.QuintoTrainee.CineCinco.Oauth2.CustomOAuth2UserService;

import com.QuintoTrainee.CineCinco.services.UsuarioService;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired	
	public UsuarioService usuarioService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(usuarioService).
		passwordEncoder(new BCryptPasswordEncoder());
	}
	
	private final String[] ADMIN_PATHLIST = {
			"/ABM/**"	
	};
	
	private final String[] PERMIT_ALL_PATHLIST = {
			"/css/",
			"/js/","/",
			"/registro/",
			"/registro_usuario",
			"/login/",
			"/oauth2/**",
			"/index/",

			"/portada/{idPelicula}",
			"/img/*",
			"/pelicula/**"
			
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(PERMIT_ALL_PATHLIST).permitAll()
				.antMatchers("/butaca/**","/compra/**","/inicio","/portada/redirigir/butacasFuncion").hasAnyRole("CLIENTE","ADMINISTRADOR")
				.antMatchers(ADMIN_PATHLIST).hasRole("ADMINISTRADOR")
				.and().formLogin()
					.loginPage("/login")
						.loginProcessingUrl("/logincheck")
						.usernameParameter("email")
						.passwordParameter("password")
						.defaultSuccessUrl("/inicio")
						.failureUrl("/login?error=error")

						.permitAll()
                    .and()
				    .logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
					 .and()
		                .oauth2Login()
		                    .loginPage("/login")
		                    .userInfoEndpoint()
		                         .userService(oAuthUserService)
		                    .and()
		                    .successHandler( new AuthenticationSuccessHandler() {
		    					
		    					@Override
		    					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		    							Authentication authentication) throws IOException, ServletException {
		    						System.out.println("AuthenticationSuccessHandler invoked");
		    						System.out.println("Authentication name: " + authentication.getName());
		    						
		    						CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
		    						usuarioService.processOAuthPostLogin(oauthUser);
		    						
		    						response.sendRedirect("/login");
		    					}
		    				})
				.and().csrf()					
				.disable();
	}
	
    @Autowired
	private CustomOAuth2UserService oAuthUserService;
    
}
