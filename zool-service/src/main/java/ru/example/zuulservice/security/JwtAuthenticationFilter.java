package ru.example.zuulservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    public JwtAuthenticationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //получаем хедер с jwt токеном из заголовков запроса
        String header = request.getHeader(jwtConfig.getHeader());

        //если хедера нет, или он начинается не с того префикса - просто идем дальше по цепочке фильтров
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        //просто убираем Bearer вначале, получая токен
        String token = header.replace(jwtConfig.getPrefix(), "");

        //могут быть выброшены исключения, например, если токен просрочен
        try {
            //Валидируем токен
            Claims claims = Jwts.parser()
                    //здесь можно было указать публичный ключ или симметричный как в нашем случае
                    .setSigningKey(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if (username != null) {
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");

                //ну и собственно создаем объект Аутентификации и помещаем его в контекст спринга.
                //при передаче туда ролей он сразу становится аутентифицированным
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            //убедимся, что контекст пустой, в случае ошибки (токен невалиден или истек)
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
