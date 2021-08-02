package com.jonatan.simplespringapi.api.model;

import lombok.Data;

@Data
public class RoleToUserForm {
    private String userLogin;
    private String roleName;
}
