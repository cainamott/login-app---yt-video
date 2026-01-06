package io.github.mottcaina.loginapp.controller;

import io.github.mottcaina.loginapp.DTO.LoginRequestDTO;
import io.github.mottcaina.loginapp.DTO.RegisterRequestDTO;
import io.github.mottcaina.loginapp.DTO.ResponseDTO;
import io.github.mottcaina.loginapp.infra.security.CustomUserDetailService;
import io.github.mottcaina.loginapp.infra.security.TokenService;
import io.github.mottcaina.loginapp.model.User;
import io.github.mottcaina.loginapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO body){

        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("USER NOT FOUND"));
        if(passwordEncoder.matches(body.password(), user.getPassword())){
             String token = this.tokenService.generateToken(user);
             System.out.println("----------"+token+"----------");
             return ResponseEntity.ok(new ResponseDTO(token, user.getName()));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO body){

        Optional<User> user = this.userRepository.findByEmail(body.email());

          if(user.isEmpty()){
              User newUser = new User();
              newUser.setPassword(passwordEncoder.encode(body.password()));
              newUser.setEmail(body.email());
              newUser.setName(body.name());

              User savedUser = userRepository.save(newUser);
              String token = tokenService.generateToken(savedUser);
              System.out.println("----------"+token+"----------");
              return ResponseEntity.ok(new ResponseDTO(token, newUser.getName()));
          }


        return ResponseEntity.badRequest().build();
    }

}
