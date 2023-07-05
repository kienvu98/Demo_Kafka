package com.microservices.demo.elastic.query.service.security;

import com.microservices.demo.elastic.query.service.business.QueryUserService;
import com.microservices.demo.elastic.query.service.transformer.UserPermissionToUserDetailTransformer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TwitterQueryUserDetailService implements UserDetailsService {

    private final QueryUserService userService;

    private final UserPermissionToUserDetailTransformer userPermissionToUserDetailTransformer;

    public TwitterQueryUserDetailService(QueryUserService userService,
                                         UserPermissionToUserDetailTransformer userPermissionToUserDetailTransformer) {
        this.userService = userService;
        this.userPermissionToUserDetailTransformer = userPermissionToUserDetailTransformer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findAllPermisssionByUserName(username)
                .map(userPermissionToUserDetailTransformer::getUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("no user found with name {}" + username));
    }
}
