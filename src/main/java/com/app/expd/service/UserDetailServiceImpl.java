package com.app.expd.service;

import com.app.expd.exceptions.LetsTalkException;
import com.app.expd.models.User;
import com.app.expd.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
       User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Not a valid username"));
        return new org.springframework.security
                .core.userdetails.User(user.getUsername(),user.getPassword(),
                user.getEnabled(),true,true
                ,true,getAuthorities("ROLE_"+user.getRole().toString()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
