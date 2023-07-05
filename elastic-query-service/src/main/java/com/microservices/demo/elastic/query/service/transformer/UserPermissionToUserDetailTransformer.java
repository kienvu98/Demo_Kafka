package com.microservices.demo.elastic.query.service.transformer;

import com.microservices.demo.elastic.query.service.dataccess.entity.UserPermission;
import com.microservices.demo.elastic.query.service.security.PermissionType;
import com.microservices.demo.elastic.query.service.security.TwitterQueryUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPermissionToUserDetailTransformer {

    public TwitterQueryUser getUserDetails(List<UserPermission> userPermissions) {
        return TwitterQueryUser.builder()
                .userName(userPermissions.get(0).getUserName())
                .permission(userPermissions.stream().
                        collect(Collectors.toMap(UserPermission::getDocumentId,
                                permission -> PermissionType.valueOf(permission.getPermissionType())))).build();
    }
}
