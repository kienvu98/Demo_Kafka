package com.microservices.demo.elastic.query.service.business;

import com.microservices.demo.elastic.query.service.dataccess.entity.UserPermission;

import java.util.List;
import java.util.Optional;

public interface QueryUserService {
    Optional<List<UserPermission>> findAllPermisssionByUserName(String userName);
}
