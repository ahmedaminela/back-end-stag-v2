package com.example.rh.service;

import com.example.rh.repository.PermissionRepository;
import com.example.rh.repository.RoleRepository;
import com.example.rh.repository.UserRepository;
import com.example.rh.service.Exception.BusinessException;
import lombok.AllArgsConstructor;
import com.example.rh.dto.PermissionVo;
import com.example.rh.dto.RoleVo;
import com.example.rh.dto.UserVo;
import com.example.rh.model.Permission;
import com.example.rh.model.Role;
import com.example.rh.model.User;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements IUserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load the user entity and map it to UserVo
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Flatten the authorities (roles and permissions) into GrantedAuthority
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .flatMap(role -> role.getAuthorities().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getAuthority())))
                .collect(Collectors.toList());

        // Add roles as authorities
        authorities.addAll(user.getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList()));

        // Return a Spring Security User object with authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    @Override
    public void save(UserVo userVo) {
        User user = modelMapper.map(userVo, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Map roles and their permissions
        List<Role> roles = user.getAuthorities().stream()
                .map(roleVo -> roleRepository.findByAuthority(roleVo.getAuthority())
                        .orElseThrow(() -> new BusinessException("Role not found: " + roleVo.getAuthority())))
                .collect(Collectors.toList());

        user.setAuthorities(roles);
        userRepository.save(user);
    }

    @Override
    public void save(RoleVo roleVo) {
        Role role = modelMapper.map(roleVo, Role.class);
        role.setAuthorities(role.getAuthorities().stream()
                .map(permissionVo -> permissionRepository.findByAuthority(permissionVo.getAuthority())
                        .orElseThrow(() -> new BusinessException("Permission not found: " + permissionVo.getAuthority())))
                .collect(Collectors.toList()));
        roleRepository.save(role);
    }

    @Override
    public void save(PermissionVo vo) {
        permissionRepository.save(modelMapper.map(vo, Permission.class));
    }

    @Override
    public RoleVo getRoleByName(String authority) {
        return modelMapper.map(roleRepository.findByAuthority(authority)
                .orElseThrow(() -> new BusinessException("Role not found: " + authority)), RoleVo.class);
    }

    @Override
    public PermissionVo getPermissionByName(String authority) {
        return modelMapper.map(permissionRepository.findByAuthority(authority)
                .orElseThrow(() -> new BusinessException("Permission not found: " + authority)), PermissionVo.class);
    }

    @Override
    public List<PermissionVo> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permission -> modelMapper.map(permission, PermissionVo.class))
                .collect(Collectors.toList());
    }

    public boolean roleExists(String roleName) {
        return roleRepository.findByAuthority(roleName).isPresent();
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
