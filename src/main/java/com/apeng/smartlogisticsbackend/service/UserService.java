package com.apeng.smartlogisticsbackend.service;

import com.apeng.smartlogisticsbackend.dto.RegisterRequest;
import com.apeng.smartlogisticsbackend.entity.User;
import com.apeng.smartlogisticsbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow();
    }

    /**
     * 注册
     * @param registerRequest 注册请求
     * @return 用户名
     */
    public String register(RegisterRequest registerRequest) {
        validateUniqueUsername(registerRequest);
        return userRepository.save(new User(registerRequest.username(), registerRequest.password())).getUsername();
    }

    private void validateUniqueUsername(RegisterRequest registerRequest) {
        userRepository.findById(registerRequest.username()).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already used!");
        });
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString();
            }
        }
        return "Anonymous";
    }

}
