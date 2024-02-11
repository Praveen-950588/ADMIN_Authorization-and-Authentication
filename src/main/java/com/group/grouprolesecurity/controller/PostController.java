package com.group.grouprolesecurity.controller;

import com.group.grouprolesecurity.entity.Post;
import com.group.grouprolesecurity.entity.PostStatus;
import com.group.grouprolesecurity.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/created")
    public String createPost(@RequestBody Post post, Principal principal){

        post.setStatus(PostStatus.PENDING);
        post.setUserName(principal.getName());
        postRepository.save(post);
        return principal.getName() + " Your post published successfully , Required ADMIN/MODERATOR Action";
    }

    @GetMapping("/approvePost/{postId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MODERATOR')")
    public String approvePost(@PathVariable int postId){

      Post post =  postRepository.findById(postId).get();
      post.setStatus(PostStatus.APPROVED);
      postRepository.save(post);
      return "Post Approved !!";
    }

    @GetMapping("/approveAll")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MODERATOR')")
    public String approveAll(){
       postRepository.findAll().stream().filter(post -> post.getStatus().equals(PostStatus.PENDING))
               .forEach(post ->{post.setStatus(PostStatus.APPROVED);
               postRepository.save(post);
               });
       return "Approved all posts";
    }

    @GetMapping("/removePost/{postId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MODERATOR')")
    public String removePost(@PathVariable int postId){

        Post post =  postRepository.findById(postId).get();
        post.setStatus(PostStatus.REJECTED);
        postRepository.save(post);
        return "Post Rejected !!";
    }

    @GetMapping("/rejectAll")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MODERATOR')")
    public String rejectAll(){
        postRepository.findAll().stream().filter(post -> post.getStatus().equals(PostStatus.PENDING))
                .forEach(post ->{post.setStatus(PostStatus.REJECTED);
                    postRepository.save(post);
                });
        return "Rejected all posts";
    }

    @GetMapping("/veiwAll")
    public List<Post> viewAll(){
      return postRepository.findAll().stream().filter(post -> post.getStatus().equals(PostStatus.APPROVED)).collect(Collectors.toList());
    }

}
