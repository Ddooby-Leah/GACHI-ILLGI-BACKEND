package com.ddooby.gachiillgi.domain.service.impl;

import com.ddooby.gachiillgi.base.enums.UserStatusEnum;
import com.ddooby.gachiillgi.base.enums.exception.AuthErrorCodeEnum;
import com.ddooby.gachiillgi.base.enums.exception.UserErrorCodeEnum;
import com.ddooby.gachiillgi.base.exception.BizException;
import com.ddooby.gachiillgi.domain.entity.User;
import com.ddooby.gachiillgi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component("userDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        return userRepository.findOneWithUserAuthorityByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new BizException(UserErrorCodeEnum.USER_NOT_FOUND));
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {
        if (user.getActivated() != UserStatusEnum.ACTIVATED) {
            throw new BizException(AuthErrorCodeEnum.MUST_MAIL_VERIFICATION);
        }

        List<GrantedAuthority> grantedAuthorities = user.getUserAuthoritySet().stream()
                .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.getAuthority().getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities);
    }
}
