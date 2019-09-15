package org.unitar.invoices.repository;

import org.unitar.invoices.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    public List<Role> getRoleByRoleName(String roleName);
}
