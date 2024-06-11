package it.cgmconsulting.msuser.controller;

import it.cgmconsulting.msuser.entity.User;
import it.cgmconsulting.msuser.payload.request.SignupRequest;
import it.cgmconsulting.msuser.payload.response.UserResponse;
import it.cgmconsulting.msuser.service.AuthorityService;
import it.cgmconsulting.msuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired UserService userService;
    @Autowired AuthorityService authorityService;

    @PutMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request){

        if(userService.existsByUsernameOrEmail(request.getUsername(), request.getEmail()))
            return new ResponseEntity<>("Username or email already in use", HttpStatus.BAD_REQUEST);

        User u = userService.fromRequestToUser(request);
        if(u.getUsername() == null)
            return new ResponseEntity<>("Authority non found", HttpStatus.BAD_REQUEST);

        userService.save(u);
            return new ResponseEntity<>("User "+u.getUsername()+" successfully registered", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> existsByIdAndAuthority(@RequestParam long id, @RequestParam String authorityName){
        return new ResponseEntity(userService.existsByIdAndAuthorityAuthorityName(id, authorityName), HttpStatus.OK);
    }

    @GetMapping("/{authorityName}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String authorityName){
        /* scommentare per testare circuit breaker
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        List<UserResponse> list = userService.getByRole(authorityName);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<?> getUser(@PathVariable long userId){
        return new ResponseEntity(userService.getUser(userId), HttpStatus.OK);
    }

    @GetMapping("backup")
    public ResponseEntity<?> getBackupUsers(){
        return new ResponseEntity(userService.getBackupUsers(), HttpStatus.OK);
    }

    @GetMapping("authority/backup")
    public ResponseEntity<?> getBackupAuthorities(){
        return new ResponseEntity(authorityService.getBackupAuthorities(), HttpStatus.OK);
    }

}
