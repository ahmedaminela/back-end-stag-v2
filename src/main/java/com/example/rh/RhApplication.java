package com.example.rh;

import com.example.rh.Dto.*;
import com.example.rh.enums.Permissions;
import com.example.rh.enums.Roles;
import com.example.rh.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class RhApplication {

    public static void main(String[] args) {
        SpringApplication.run(RhApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner initDataBase(IUserService userService) {
        return args -> {
            // Check if any permissions already exist to avoid duplication
            if (userService.getAllPermissions().isEmpty()) {
                Arrays.stream(Permissions.values()).toList().forEach(permission ->
                        userService.save(PermissionVo.builder().authority(permission.name()).build()));
            }

            // Check if the roles already exist before creating them
            if (!userService.roleExists(Roles.Role_RH.name())) {
                RoleVo roleRH = RoleVo.builder()
                        .authority(Roles.Role_RH.name())
                        .authorities(List.of(
                                userService.getPermissionByName(Permissions.GERER_LES_DECISIONS_STAGES.name()),
                                userService.getPermissionByName(Permissions.AFFILIER_STAGE.name())
                        ))
                        .build();
                userService.save(roleRH);
            }

            if (!userService.roleExists(Roles.Role_encadrant.name())) {
                RoleVo roleEncadrant = RoleVo.builder()
                        .authority(Roles.Role_encadrant.name())
                        .authorities(List.of(
                                userService.getPermissionByName(Permissions.EVALUER_STAGIARE.name()),
                                userService.getPermissionByName(Permissions.ATTRIBUER_SUJET_STAGIARE.name())
                        ))
                        .build();
                userService.save(roleEncadrant);
            }

            if (!userService.roleExists(Roles.Role_stagiare.name())) {
                RoleVo roleStagiare = RoleVo.builder()
                        .authority(Roles.Role_stagiare.name())
                        .authorities(List.of(
                                userService.getPermissionByName(Permissions.CONSULTER_SJT_STAGE.name()),
                                userService.getPermissionByName(Permissions.POSTULER.name())
                        ))
                        .build();
                userService.save(roleStagiare);
            }

            // Check if the RH user already exists before creating it
            if (!userService.userExists("rhuser")) {
                UserVo rhUser = UserVo.builder()
                        .username("rhuser")
                        .password("rhpassword")
                        .email("rh@example.com")
                        .firstname("RH")
                        .lastname("User")
                        .authorities(List.of(userService.getRoleByName(Roles.Role_RH.name())))
                        .build();
                userService.save(rhUser);
            }

            // Check if the Encadrant user already exists before creating it
            if (!userService.userExists("encadrantuser")) {
                UserVo encadrantUser = UserVo.builder()
                        .username("encadrantuser")
                        .password("encadrantpassword")
                        .email("encadrant@example.com")
                        .firstname("Encadrant")
                        .lastname("User")
                        .authorities(List.of(userService.getRoleByName(Roles.Role_encadrant.name())))
                        .build();
                userService.save(encadrantUser);
            }
        };
    }
}
