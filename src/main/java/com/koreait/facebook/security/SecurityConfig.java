package com.koreait.facebook.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration//설정파일
@EnableWebSecurity //안주면 설정은 됐는데 시큐리티를 안킴
public class SecurityConfig extends WebSecurityConfigurerAdapter {//오버라이딩 하고 싶어서 ( 디폴트값으로 사용 안하려구!!)
    @Autowired
    private UserDetailsService userDetails;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/pic/**","/css/**", "/js/**", "/img/**", "/error", "favicon.ico", "/resources/**"); //static resouse 부분
    }//바로 dis로 보내줌

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();//공격 차단

        http.authorizeRequests()//모두 접근허용
                .antMatchers("/user/login", "/user/join", "/user/auth").permitAll()
                .anyRequest().authenticated();//나머진 인증처리 //컨트롤러쪽

        http.formLogin()
                .loginPage("/user/login")
                .usernameParameter("email") //UserEntity
                .passwordParameter("pw")
                .defaultSuccessUrl("/feed/home");

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/user/login")
                .invalidateHttpSession(true);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());//bCrypt사용가능
    }//오버라이드 함으로서 userDetails 부분을 우리가 implemet한 UserDetailServiceImpl을 받을 수 있다
    //Bean등록 되있는 BCryptPasswordEncoder()사용할수있다.
}
