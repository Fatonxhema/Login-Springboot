package com.xhemafaton.jwtlogin.controller;

import com.xhemafaton.jwtlogin.model.RoleModel;
import com.xhemafaton.jwtlogin.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public RoleModel createRole(@RequestBody RoleModel roleModel) {
        return roleService.createRole(roleModel);
    }

    @GetMapping("/getAllRoles")
    public List<RoleModel> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PreAuthorize("hasRole('ADMIN') or principal.userId == #userId")
    @DeleteMapping("/roles/{userId}/{roleId}")
    public void deleteRole(@PathVariable Long userId, @PathVariable Long roleId) {
        roleService.deleteRoleById(roleId);
    }
}
