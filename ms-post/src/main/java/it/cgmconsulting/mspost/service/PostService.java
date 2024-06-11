package it.cgmconsulting.mspost.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.payload.response.CommentResponse;
import it.cgmconsulting.mspost.payload.response.PostDetailResponse;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import it.cgmconsulting.mspost.payload.response.UserResponse;
import it.cgmconsulting.mspost.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostService {

    @Autowired PostRepository postRepository;

    public void save(Post p){
        postRepository.save(p);
    }

    public Optional<Post> findById(long id){
        return postRepository.findById(id);
    }

    public boolean existsByTitleAndIdNot(String title, long postId){
        return postRepository.existsByTitleAndIdNot(title, postId);
    }

    public boolean existsByTitle(String title){
        return postRepository.existsByTitle(title);
    }

    public Post fromRequestToEntity(PostRequest pr) {
        Post p = new Post(
                pr.getTitle(),
                pr.getOverview(),
                pr.getContent(),
                pr.getAuthor()
        );
        return p;
    }

    public boolean checkTitle(String title){
        return postRepository.existsByTitle(title);
    }

    @CircuitBreaker(name = "a-tempo", fallbackMethod = "checkUserAndAuthorityFallBack" )
    public boolean checkUserAndAuthority(long id, String authorityName){
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/user?id={id}&authorityName={authorityName}";
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class, id, authorityName);
        return existsUser;
    }
    public boolean checkUserAndAuthorityFallBack(Exception e){
        log.info("--- User Microservice not AVAILABLE ("+e.getMessage()+")---");
        return false;
    }


    @CircuitBreaker(name="a-tempo", fallbackMethod = "getPostsFallBack")
    public List<PostResponse> getPosts() {
        List<PostResponse> list = postRepository.getPosts();
        // richiamare il microservizio msuser e farmi restituire una lista(UserResponse) di utenti che siano ROLE_EDITOR
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/user/ROLE_EDITOR";

        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(UserResponse.class),
                new ParameterizedTypeReference<List<UserResponse>>(){}
        );
        List<UserResponse> listUser = response.getBody();

        // Cicli list e per ogni match tra id dello user e author di PostResponse, setto lo username

        for (PostResponse p : list) {
            for (UserResponse u : listUser) {
                if (p.getAuthor() == (u.getId())) {
                    p.setAuthorUsername(u.getUsername());
                }
            }
        }
        return list;
    }

    // Nel caso in cui la chiamata al microservizio degli user fallisse (vedi metodo getPosts)
    // restituisco comunque una lista di PostResponse ma senza lo username dell'autore del post
    public List<PostResponse> getPostsFallBack(Exception e){
        log.info("RESILIENCE4J: ms-user not AVAILABLE. "+e.getMessage());
        List<PostResponse> list = postRepository.getPosts();
        return list;
    }

    public boolean existsByIdAndPublishedTrue(long id){
        return postRepository.existsByIdAndPublishedTrue(id);
    }


    public PostResponse getPost(long postId){
        return postRepository.getPost(postId);
    }

    @CircuitBreaker(name="a-tempo", fallbackMethod = "getUserFallBack")
    public UserResponse getUser(long userId){
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/user/id/"+userId;

        ResponseEntity<UserResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(UserResponse.class),
                new ParameterizedTypeReference<UserResponse>(){}
        );
        UserResponse u = response.getBody();
        return u;
    }
    public UserResponse getUserFallBack(Exception e){
        log.info("RESILIENCE4J: ms-user not AVAILABLE. "+e.getMessage());
        UserResponse ur = null;
        return ur;
    }

    @CircuitBreaker(name = "a-tentativi", fallbackMethod = "getCommentsByPostFallBack" )
    public List<CommentResponse> getCommentsByPost(long postId){
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/comment/"+postId;
        ResponseEntity<List<CommentResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(CommentResponse.class),
                new ParameterizedTypeReference<List<CommentResponse>>(){}
        );
        List<CommentResponse> comments = response.getBody();
        return comments;
    }
    public List<CommentResponse> getCommentsByPostFallBack(Exception e){
        log.info("---- COMMENT MICROSEVICE NOT AVAILABLE ("+e.getMessage()+") ----");
        return new ArrayList<>();
    }

    public PostDetailResponse fromPostResponseToPostDetailResponse(PostResponse p, List<CommentResponse> comments, double average){
        return new PostDetailResponse(
                p.getId(),
                p.getTitle(),
                p.getOverview(),
                p.getAuthor(),
                p.getAuthorUsername(),
                comments,
                average
        );
    }

    @CircuitBreaker(name="a-tentativi", fallbackMethod = "getAvgRatePostFallBack")
    public double getAvgRatePost(long postId){
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/rating/"+postId;
        double rateAvg = restTemplate.getForObject(uri, Double.class);
        return rateAvg;
    }
    public double getAvgRatePostFallBack(Exception e){
        log.info("---- RATING MICROSEVICE NOT AVAILABLE ("+e.getMessage()+") ----");
        return 0d;
    }

    public Optional<Post> findByIdAndPublishedTrue(long postId){
        return postRepository.findByIdAndPublishedTrue(postId);
    }

    public List<Post> getBackupPosts(){
        return postRepository.findAll();
    }

}
