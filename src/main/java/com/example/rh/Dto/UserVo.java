package com.example.rh.Dto;

import com.example.rh.model.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVo implements UserDetails {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    protected String firstname;
    protected String lastname;
    private List<RoleVo> authorities = new ArrayList<RoleVo>();
    private String email;


}
