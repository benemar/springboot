package it.cgmconsulting.mscomment.payload.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class CommentRequest {

    @NotBlank @Size(max=255)
    private String comment;
    @Min(1)
    private long author;
    @NotBlank
    private String authorityName;
    @Min(1)
    private long post;
}
