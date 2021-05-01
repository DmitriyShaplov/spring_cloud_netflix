package ru.example.zuulservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * Конфигурационный класс для настройки
 * security на Zuul, ограничивающий доступ
 * к другим внутренним сервисам.
 */
@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //удостоверяемся, что у нас нет пользовательского состояния на беке
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //обрабатываем попытки неаутентифицированного доступа
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                //добавляем jwt фильтр для распознавания токенов
                .addFilterBefore(new JwtAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                //разрешаем обращаться к сервису аутентификации
                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                //настраиваем доступы к сервисам
                .antMatchers("/gallery/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();


    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (req, res, e) -> {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            //кастомизируем сообщение
            String json = String.format("{\"message\": \"%s\"", e.getMessage());
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(json);
        };
    }
}
