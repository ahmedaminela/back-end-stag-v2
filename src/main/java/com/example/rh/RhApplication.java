package com.example.rh;

import com.example.rh.dto.*;
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
            if (!userService.roleExists(Roles.ROLE_RH.name())) {
                RoleVo roleRH = RoleVo.builder()
                        .authority(Roles.ROLE_RH.name())
                        .authorities(List.of(
                                userService.getPermissionByName(Permissions.CREATE_STAGE.name()),
                                userService.getPermissionByName(Permissions.VIEW_CANDIDATES.name()),
                                userService.getPermissionByName(Permissions.ACCEPT_APPLICATION.name()),
                                userService.getPermissionByName(Permissions.REJECT_APPLICATION.name()),
                                userService.getPermissionByName(Permissions.ASSIGN_ENCADRANT.name()),
                                userService.getPermissionByName(Permissions.UPDATE_STAGE.name()),
                                userService.getPermissionByName(Permissions.DELETE_STAGE.name()),
                                userService.getPermissionByName(Permissions.GET_STAGE.name()),
                                userService.getPermissionByName(Permissions.GET_ALL_STAGES.name()),
                                userService.getPermissionByName(Permissions.GET_STAGES_BY_STATE.name())
                        ))
                        .build();
                userService.save(roleRH);
            }

            if (!userService.roleExists(Roles.ROLE_ENCADRANT.name())) {
                RoleVo roleEncadrant = RoleVo.builder()
                        .authority(Roles.ROLE_ENCADRANT.name())
                        .authorities(List.of(
                                userService.getPermissionByName(Permissions.UPDATE_STAGE_STATE.name()),
                                userService.getPermissionByName(Permissions.UPDATE_STAGE.name()),
                                userService.getPermissionByName(Permissions.GET_STAGE.name()),
                                userService.getPermissionByName(Permissions.GET_ALL_STAGES.name())
                        ))
                        .build();
                userService.save(roleEncadrant);
            }

            if (!userService.roleExists(Roles.ROLE_STAGIAIRE.name())) {
                RoleVo roleStagiaire = RoleVo.builder()
                        .authority(Roles.ROLE_STAGIAIRE.name())
                        .authorities(List.of(
                                userService.getPermissionByName(Permissions.APPLY_FOR_STAGE.name()),
                                userService.getPermissionByName(Permissions.GET_STAGE.name()),
                                userService.getPermissionByName(Permissions.GET_ALL_STAGES.name()),
                                userService.getPermissionByName(Permissions.GET_STAGES_BY_STATE.name())
                        ))
                        .build();
                userService.save(roleStagiaire);
            }

        };
    }
}
