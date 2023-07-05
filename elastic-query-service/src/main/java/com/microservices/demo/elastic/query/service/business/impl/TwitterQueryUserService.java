package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.elastic.query.service.business.QueryUserService;
import com.microservices.demo.elastic.query.service.dataccess.entity.UserPermission;
import com.microservices.demo.elastic.query.service.dataccess.repository.UserPermissionrepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TwitterQueryUserService implements QueryUserService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final UserPermissionrepository userPermissionrepository;

    public TwitterQueryUserService(UserPermissionrepository userPermissionrepository) {
        this.userPermissionrepository = userPermissionrepository;
    }

    @Override
    public Optional<List<UserPermission>> findAllPermisssionByUserName(String userName) {
        LOG.info("Finding permissions by username {}", userName);
        return userPermissionrepository.findUserPermissionByUserName(userName);
    }
}
