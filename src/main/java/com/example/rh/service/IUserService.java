package com.example.rh.service;

import com.example.rh.dto.*;

import java.util.List;

public interface IUserService {
    void save(UserVo user);
    void save(RoleVo role);
    void save(PermissionVo vo);
    RoleVo getRoleByName(String role);
    PermissionVo getPermissionByName(String authority);
    List<PermissionVo> getAllPermissions();
    boolean roleExists(String roleName);
    boolean userExists(String username);
    boolean userExistsByEmail(String email);

}
