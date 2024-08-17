package com.example.rh.RestController;

import com.example.rh.repository.RoleRepository;
import com.example.rh.repository.UserRepository;
import com.example.rh.Dto.*;
import com.example.rh.jwt.JwtUtils;
import com.example.rh.service.IUserService;
import com.example.rh.service.Exception.BusinessException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")

public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IUserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                                    IUserService userService, RoleRepository roleRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenVo> authenticateUser(@RequestBody Userrequest userRequest) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userRequest.username(), userRequest.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            TokenVo tokenVo = TokenVo.builder()
                    .jwtToken(jwt)
                    .username(userRequest.username())
                    .roles(authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .build();
            logger.info(userRequest.username() + " est connecté avec succès");
            return ResponseEntity.ok(tokenVo);
        } catch (BadCredentialsException e) {
            logger.error("Login ou mot de passe incorrect pour l'utilisateur: " + userRequest.username());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification de l'utilisateur: " + userRequest.username(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest createUserRequest) {

        if(userService.userExists(createUserRequest.username()))
        {
            return new ResponseEntity<>(String.format("User with username [%s] already exists", createUserRequest.username()), HttpStatus.BAD_REQUEST);
        }
        if(userService.userExistsByEmail(createUserRequest.email()))
        {
            return new ResponseEntity<>(String.format("User with email [%s] already exists", createUserRequest.email()), HttpStatus.BAD_REQUEST);
        }
        userService.save(UserVo.builder()
                .username(createUserRequest.username())
                .password(createUserRequest.password())
                .email(createUserRequest.email())
                .authorities(List.of(RoleVo.builder().authority("Role_stagiare").build()))
                .build());
        return new ResponseEntity<>(String.format("User [%s] created with success", createUserRequest.username()), HttpStatus.CREATED);
    }
}
