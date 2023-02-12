package com.xhemafaton.jwtlogin.service;

import com.xhemafaton.jwtlogin.entity.RoleEntity;
import com.xhemafaton.jwtlogin.entity.UserEntity;
import com.xhemafaton.jwtlogin.model.RoleModel;
import com.xhemafaton.jwtlogin.model.UserModel;
import com.xhemafaton.jwtlogin.repository.RoleRepository;
import com.xhemafaton.jwtlogin.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel register(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userModel, userEntity);//it does not do a deep copy

        Set<RoleEntity> roleEntities = new HashSet<>();
        //fetch every role from DB based on role id and than set this role to user entity roles
        for (RoleModel rm : userModel.getRoles()) {
            Optional<RoleEntity> optRole = roleRepository.findById(rm.getId());
            if (optRole.isPresent()) {
                roleEntities.add(optRole.get());
            }
        }
        userEntity.setRoles(roleEntities);
        userEntity.setPassword(this.passwordEncoder.encode(userModel.getPassword()));
        userEntity = userRepository.save(userEntity);

        BeanUtils.copyProperties(userEntity, userModel);

        //convert RoleEntities to RoleModels
        Set<RoleModel> roleModels = new HashSet<>();
        RoleModel rm = null;
        for (RoleEntity re : userEntity.getRoles()) {
            rm = new RoleModel();
            rm.setName(re.getName());
            rm.setId(re.getId());
            roleModels.add(rm);
        }
        userModel.setRoles(roleModels);
        return userModel;
    }

    //validate the user by username
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userEntity, userModel);

        //convert RoleEntities to RoleModels
        Set<RoleModel> roleModels = new HashSet<>();
        RoleModel rm = null;
        for (RoleEntity re : userEntity.getRoles()) {
            rm = new RoleModel();
            rm.setName(re.getName());
            rm.setId(re.getId());
            roleModels.add(rm);
        }

        userModel.setRoles(roleModels);
        return userModel;
    }


}
