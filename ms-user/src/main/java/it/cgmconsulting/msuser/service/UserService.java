package it.cgmconsulting.msuser.service;

import it.cgmconsulting.msuser.entity.Authority;
import it.cgmconsulting.msuser.entity.User;
import it.cgmconsulting.msuser.payload.request.SignupRequest;
import it.cgmconsulting.msuser.payload.response.UserBackupResponse;
import it.cgmconsulting.msuser.payload.response.UserResponse;
import it.cgmconsulting.msuser.repository.AuthorityRepository;
import it.cgmconsulting.msuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired UserRepository userRepository;
    @Autowired AuthorityService authorityService;
    @Autowired PasswordEncoder passwordEncoder;

    public boolean existsByUsernameOrEmail(String a, String b){
        return userRepository.existsByUsernameOrEmail(a, b);
    }

    public void save(User u){
        userRepository.save(u);
    }

    public User fromRequestToUser(SignupRequest sr){
        User u = new User();
        Optional<Authority> a = authorityService.findByAuthorityName(sr.getAuthorityName());
        if(a.isEmpty())
            return u;

        u.setAuthority(a.get());
        u.setEmail(sr.getEmail());
        u.setUsername(sr.getUsername());
        u.setPassword(passwordEncoder.encode(sr.getPassword()));
        return u;
    }

    public boolean existsByIdAndAuthorityAuthorityName(long id, String authorityName){
        return userRepository.existsByIdAndAuthorityAuthorityName(id, authorityName);
    }

    public List<UserResponse> getByRole(String authorityName){
        return userRepository.getByRole(authorityName);
    }

    public UserResponse getUser(long userId){
        return userRepository.getUser(userId);
    }

    public List<UserBackupResponse> getBackupUsers(){
        return userRepository.getBackupUsers();
    }
}
