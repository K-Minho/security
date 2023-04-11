package xyz.cyanin.securityapp.core.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.cyanin.securityapp.model.User;
import xyz.cyanin.securityapp.model.UserRepository;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // /login + POST + FormURLEncoded + username + password 
    // 시큐리티는 이 필터를 인터셉트 함
    // jwt는 이 필터를 건드려줘야 함
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOP = userRepository.findByUsername(username);
        if(userOP.isPresent()){
            return new MyUserDetails(userOP.get());
        } else {
            return null; 
        }
    }


}
