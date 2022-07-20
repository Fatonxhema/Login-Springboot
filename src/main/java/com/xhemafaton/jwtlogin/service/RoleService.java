package com.xhemafaton.jwtlogin.service;


import com.xhemafaton.jwtlogin.model.RoleModel;

import java.util.List;

public interface RoleService {
    RoleModel createRole(RoleModel roleModel);
    List<RoleModel> getAllRoles();
    RoleModel getRoleById(Long id);
    void deleteRoleById(Long roleId);
}
