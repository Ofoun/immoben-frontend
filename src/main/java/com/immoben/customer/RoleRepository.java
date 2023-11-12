package com.immoben.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.immoben.common.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

}

