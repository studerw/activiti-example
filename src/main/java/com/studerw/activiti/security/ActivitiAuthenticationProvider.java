package com.studerw.activiti.security;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service("activitiAuthenticationProvider")
public class ActivitiAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final Logger log = Logger.getLogger(ActivitiAuthenticationProvider.class);

    protected IdentityService identityService;

    @Autowired
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.trace("authenticate()");
        log.debug("authenticating: " + authentication);
        String clearText = String.valueOf(authentication.getCredentials());
        UserDetails userDetails = this.retrieveUser(authentication.getName(), (UsernamePasswordAuthenticationToken) authentication);

        if (!Objects.equal(clearText, userDetails.getPassword())) {
            throw new BadCredentialsException("invalid password");
        }
        if (!userDetails.isEnabled()) {
            throw new BadCredentialsException("User not enabled");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.trace("additionalAuthenticationChecks()");
        log.debug("isEnabled: " + userDetails.isEnabled());
        if (!userDetails.isEnabled()) {
            throw new BadCredentialsException("User not enabled");
        }
    }

    @Override
    @Transactional(readOnly = true)
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.trace("retrieveUser()");
        log.debug("retrieving user: " + username);
        User user;
        try {
            user = this.read(username);
            if (user == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User " + username + " cannot be found");
        }

        String userName = user.getId();
        String pw = user.getPassword();
        List<Group> groups = this.identityService.createGroupQuery().groupMember(userName).groupType("security-role").list();
        List<String> groupStr = Lists.newArrayList();
        for (Group g : groups) {
            groupStr.add(g.getId());
        }
        Collection<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(Joiner.on(",").skipNulls().join(groupStr));
        boolean enabled = groupStr.contains("user");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(userName, pw, enabled, true, true, true, auths);
        log.debug("returning new userDetails: " + userDetails);
        return userDetails;
    }

    @Transactional(readOnly = true)
    protected User read(String name) {
        log.trace("read()");
        log.debug("reading user: " + name);
        User user = this.identityService.createUserQuery().userId(name).singleResult();
        return user;
    }
}


