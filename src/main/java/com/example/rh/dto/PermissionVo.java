package com.example.rh.dto;

import com.example.rh.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionVo {
    private int id;
    private String authority;
    public static PermissionVo fromEntity(Permission permission) {
        return PermissionVo.builder()
                .id(permission.getId())
                .authority(permission.getAuthority())
                .build();
    }
}
