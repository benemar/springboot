package it.cgmconsulting.mscomment.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.payload.request.CommentRequest;
import it.cgmconsulting.mscomment.payload.response.CommentResponse;
import it.cgmconsulting.mscomment.payload.response.UserResponse;
import it.cgmconsulting.mscomment.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommentService {

    @Autowired CommentRepository commentRepository;

    public void save(Comment c){
        commentRepository.save(c);
    }

    @CircuitBreaker(name="a-tempo", fallbackMethod = "checkIfPostExistFallBack")
    public boolean checkIfPostExist (long id){
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/post/verify-post/"+id;
        ResponseEntity<Boolean> response;
        try {
            response = restTemplate.getForEntity(uri, Boolean.class); // getForEntity restituisce oggetto risposta con HttpStatus.
        } catch(HttpClientErrorException e){
            log.error(e.getMessage());
            return false;
        }
        return response.getBody();
    }
    public boolean checkIfPostExistFallBack(Exception e){
        log.info("--- POST SERVICE UNAVAILABLE ("+e.getMessage()+") ---");
        return false;
    }

    @CircuitBreaker(name="a-tempo", fallbackMethod = "checkUserAndAuthorityFallBack")
    public boolean checkUserAndAuthority(long id, String authorityName){
        RestTemplate restTemplate = new RestTemplate();
        // SOLUZIONE 1
        //String uri = "http://localhost:8090/user?id="+id+"&authorityName="+authorityName;
        // boolean existsUser = restTemplate.getForObject(uri, Boolean.class);
        // SOLUZIONE 2
        String uri = "http://localhost:8090/user?id={id}&authorityName={authorityName}";
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class, id, authorityName);
        return existsUser;
    }
    public boolean checkUserAndAuthorityFallBack(Exception e){
        log.info("--- USER SERVICE UNAVAILABLE ("+e.getMessage()+") ---");
        return false;
    }

    public Comment fromRequestToEntity(CommentRequest cr){
        return new Comment(cr.getComment(), cr.getAuthor(), cr.getPost());
    }

    @CircuitBreaker(name="a-tentativi", fallbackMethod = "getByPostFallBack")
    public List<CommentResponse> getByPost(long postId){
        // chiamare il ms deli user e farsi restituire ua List<UserResponse> relativamente agli user con ruolo ROLE_READER
        // recuperare la lista dei commenti del post in oggetto -> List<CommentResponse> commentList = commentRepository.getByPost(postId)
        // cliclo annidato delle due liste per valorizzare l'attributo username di ogni elemento presente nella List<CommentResponse>
        List<CommentResponse> list = commentRepository.getByPost(postId);
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/user/ROLE_READER";

        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(UserResponse.class),
                new ParameterizedTypeReference<List<UserResponse>>(){}
        );
        List<UserResponse> listUser = response.getBody();

        for (CommentResponse c : list) {
                boolean interruzione = true; int i = 0;
                while(interruzione && i<listUser.size()) {
                    if(c.getUserId() == listUser.get(i).getId()) {
                        c.setUsername(listUser.get(i).getUsername());
                        interruzione = false;
                    }
                    i++;
                }
            if(c.isCensored())
                c.setComment("**************");
        }
        return list;
    }
    public List<CommentResponse> getByPostFallBack(Exception e){
        log.info("--- USER SERVICE UNAVAILABLE ("+e.getMessage()+") ---");
        return new ArrayList<>();
    }

    public List<Comment> getBackupComments(){
        return commentRepository.findAll();
    }
}

