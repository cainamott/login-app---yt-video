package io.github.mottcaina.loginapp.infra.security;

import io.github.mottcaina.loginapp.model.User;
import io.github.mottcaina.loginapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of(() -> "ROLE_USER"));
    }

}