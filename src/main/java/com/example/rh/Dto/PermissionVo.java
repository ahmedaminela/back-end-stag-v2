package com.example.rh.Dto;

import com.example.rh.model.Permission;
import lombok.*;

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
