package com.xhemafaton.jwtlogin.repository;

import com.xhemafaton.jwtlogin.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {

}
