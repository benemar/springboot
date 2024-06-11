package it.cgmconsulting.msrating.service;

import it.cgmconsulting.msrating.entity.Rating;
import it.cgmconsulting.msrating.payload.response.RatingBackupResponse;
import it.cgmconsulting.msrating.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RatingService {

    @Autowired RatingRepository ratingRepository;

    public void save(Rating r){
        ratingRepository.save(r);
    }

    public double getAverage(long postId){
        return ratingRepository.getAverage(postId);
    }

    public boolean checkUserAndAuthority(long id, String authorityName){
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/user?id={id}&authorityName={authorityName}";
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class, id, authorityName);
        return existsUser;
    }

    public List<RatingBackupResponse> getBackupRatings(){
        return ratingRepository.getBackupRatings();
    }
}
