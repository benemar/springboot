package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.payload.response.CommentResponse;
import it.cgmconsulting.mspost.payload.response.PostDetailResponse;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import it.cgmconsulting.mspost.payload.response.UserResponse;
import it.cgmconsulting.mspost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class PostController {

    @Autowired PostService postService;

    @PutMapping
    public ResponseEntity<?> save(@RequestBody @Valid PostRequest request){
        if(!postService.checkUserAndAuthority(request.getAuthor(), "ROLE_EDITOR"))
            return new ResponseEntity<>("NO AUTHOR FOUND", HttpStatus.NOT_FOUND);
        if(postService.checkTitle(request.getTitle()))
            return new ResponseEntity<>("The post with title "+request.getTitle()+" already exists", HttpStatus.BAD_REQUEST);
        Post p = postService.fromRequestToEntity(request);
        postService.save(p);
        return new ResponseEntity<>("The post "+p.getTitle()+" has been saved with id "+p.getId(), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid PostRequest request, @PathVariable long id){

        Optional<Post> p = postService.findById(id);

        if (p.isEmpty())
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);

        if (request.getAuthor() != p.get().getAuthor())
            return new ResponseEntity<>("Solo l'autore del post può modificarlo", HttpStatus.FORBIDDEN);

        if (postService.existsByTitleAndIdNot(request.getTitle(), p.get().getId()))
            return new ResponseEntity<>("Title already exists", HttpStatus.BAD_REQUEST);

        p.get().setTitle(request.getTitle());
        p.get().setOverview(request.getOverview());
        p.get().setContent(request.getContent());
        p.get().setPublished(false);

        return new ResponseEntity<String>("Post updated", HttpStatus.OK);

    }

    @PatchMapping
    @Transactional
    public ResponseEntity<?> publishPost(@RequestParam long postId, @RequestParam long userId ){
        if(!postService.checkUserAndAuthority(userId, "ROLE_ADMIN"))
            return new ResponseEntity<>("You are not the administrator", HttpStatus.NOT_FOUND);

        Optional<Post> p = postService.findById(postId);
        if (p.isEmpty())
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);

        p.get().setPublished(true);
        return new ResponseEntity<String>("Post published", HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<?> getPosts(){
        List<PostResponse> list = postService.getPosts();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("verify-post/{id}")
    public ResponseEntity<?> verifyPost(@PathVariable @Min(1) long id){
        // verificare se post esiste e se pubblicato
        if (!postService.existsByIdAndPublishedTrue(id))
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPostDetail(@PathVariable long id){

        // recuperare il dettaglio del post
        PostResponse p = postService.getPost(id);
        if(p == null)
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);
        // recuperare username dell'author del post
        UserResponse u = postService.getUser(p.getAuthor());
        if(u == null)
            return new ResponseEntity("Author of Post not found", HttpStatus.NOT_FOUND);
        p.setAuthorUsername(u.getUsername());
        // recuperare i commenti del post e per ognuno di essi trovare lo username di chi l'ha scritto.
        List<CommentResponse> comments = postService.getCommentsByPost(p.getId());
        // calcolo voto medio del post
        double average = postService.getAvgRatePost(id);
        PostDetailResponse pr = postService.fromPostResponseToPostDetailResponse(p,comments, average);

        return new ResponseEntity(pr,HttpStatus.OK);

    }

    @GetMapping("backup")
    public ResponseEntity getBackupPosts(){
        return new ResponseEntity(postService.getBackupPosts(),HttpStatus.OK);
    }

}
