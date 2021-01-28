package com.zh.springsecurity.config;

import com.zh.springsecurity.service.SpringDataUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
//开启使用注解的方式校验授权
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //注入数据源
    private final DataSource dateSource;
    private final SpringDataUserDetailsService detailsService;

    public WebSecurityConfig(DataSource dateSource, SpringDataUserDetailsService detailsService) {
        this.dateSource = dateSource;
        this.detailsService = detailsService;
    }

    //配置对象
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dateSource);
        //自动建表   记住我功能
//        repository.setCreateTableOnStartup(true);
        return repository;
    }

    //定义用户信息服务（查询用户信息）
//    @Bean
//    public UserDetailsService userDetailsService(){
//        //内存方式校验用户
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
//        manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
//        return manager;
//    }

    //密码编码器
//    @Bean       //明文比对
//    public PasswordEncoder passwordEncoder(){
//        return NoOpPasswordEncoder.getInstance();
//    }

    @Bean     //常用的加密
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/unanuth");
        http
//                .csrf().disable()   //屏蔽csrf登录限制
                .authorizeRequests()
//                .antMatchers("/r/r1").hasAuthority("p2")
//                .antMatchers("/r/r2").hasAnyAuthority("p2,p3")
                .antMatchers("/r/**").authenticated()//所有/r/**的请求必须认证通过
                .anyRequest().permitAll()//除了/r/**，其它的请求可以访问

                .and()
                .formLogin()//允许表单登录
                .loginPage("/login-view")//登录页面
                .loginProcessingUrl("/login") //对应表单提交的action地址
                .successForwardUrl("/login-success")//自定义登录成功的页面地址
                .failureForwardUrl("/login-error")

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)//是否创建session的控制

                .and()
                .logout()
                .logoutUrl("/logout")   //还可以添加一些handler的退出配置
                .logoutSuccessUrl("/login-view?logout")

                .and().rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60)  //时长秒
                .userDetailsService(detailsService);  //放置的是userDetailsService的实现类

    }
}
