package com.microservices.demo.elastic.query.service.dataccess.repository;


import com.microservices.demo.elastic.query.service.dataccess.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPermissionrepository extends JpaRepository<UserPermission, UUID> {

    @Query(nativeQuery = true , value = "select p.user_permission_id, u.username, d.document_id, p.permission_type " +
            "from user u, user_permissions p, documents d " +
            "where u.id = p.user_id " +
            "and d.id = p.document_id " +
            "and u.username =: username" )
    Optional<List<UserPermission>> findUserPermissionByUserName(@Param("username") String username);
}
