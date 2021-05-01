package ru.example.authservice.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс служит только для того чтобы найти пользователя по username.
 */
@Service
public class UserDetailsServiceIImpl implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //хардкодим пользаков
        final List<AppUser> users = List.of(
                new AppUser(1, "user", encoder.encode("user"), "USER"),
                new AppUser(2, "admin", encoder.encode("admin"), "ADMIN")
        );

        //вытаскиваем
        for (AppUser user : users) {
            if (user.getUsername().equals(username)) {
                return User.builder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build();
            }
        }
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class AppUser {
        private Integer id;
        private String username;
        private String password;
        private String role;
    }
}
