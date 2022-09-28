package com.furkancelik.arizakayitsistemi.controller;

import com.furkancelik.arizakayitsistemi.annotation.CurrentUser;
import com.furkancelik.arizakayitsistemi.dto.post.PostDTO;
import com.furkancelik.arizakayitsistemi.dto.post.PostSubmitDTO;
import com.furkancelik.arizakayitsistemi.dto.post.PostUpdateDTO;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.service.PostService;
import com.furkancelik.arizakayitsistemi.service.UserService;
import com.furkancelik.arizakayitsistemi.shared.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/1.0/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("#user.username == principal.username")  // # parametre için
    public GenericResponse createPost(@Valid @RequestBody PostSubmitDTO post, @CurrentUser User user) {
        postService.createPost(post, user);
        return new GenericResponse("Post saved");
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                @RequestParam(name = "category", required = false) Long categoryID, @CurrentUser User user) {
        return ResponseEntity.ok(postService.getAll(pageable, user, categoryID).map(PostDTO::new));
    }

    @PutMapping
    public PostDTO updatePost(@Valid @RequestBody PostUpdateDTO postUpdateDTO){
        return new PostDTO(postService.updatePost(postUpdateDTO));
    }

    @GetMapping(value = "/get/{id:[0-9]+}")
    public PostDTO getByID(@PathVariable("id") Long id){
        return new PostDTO(postService.findByID(id));
    }

    @GetMapping(value = {"/{id:[0-9]+}", "/user/{username}/{id:[0-9]+}"}) // id değerinin sayı olduğunu belirtmek regular exp kullanıldı. + işareti
    //Page<PostDTO>                        //birden fazla basamak için kullanıldı
    public ResponseEntity<?> getAllRelative(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                            @PathVariable(name = "id") Long id,
                                            @PathVariable(name = "username", required = false) String username,
                                            @RequestParam(name = "category", required = false) Long categoryID,
                                            @RequestParam(name = "count", defaultValue = "false", required = false) boolean count,
                                            @RequestParam(name = "direction", defaultValue = "before", required = false) String direction,
                                            @CurrentUser User user) {
        if (count) {
            Long postCount = postService.getNewPostsCount(id, username, user, categoryID);
            Map<String, Object> map = new HashMap<>();
            map.put("count", postCount);
            return ResponseEntity.ok(map);
        }
        if (direction.equals("after")) {
            List<PostDTO> newPosts = postService.getNewPosts(id, username, user, categoryID, pageable.getSort()).stream().
                    map(PostDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(newPosts);
        }
        return ResponseEntity.ok(postService.getOldPosts(id, username, user, categoryID, pageable).map(PostDTO::new));
    }

    @GetMapping("/user/{username}")
    public Page<PostDTO> getUsersPostsRelative(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                               @PathVariable("username") String username,
                                               @RequestParam(name = "category", required = false) Long categoryID,
                                               @CurrentUser User currentUser) {
        User user = userService.findByUsername(username);
        return postService.getByUser(pageable, user, currentUser, categoryID).map(PostDTO::new);
    }

    @DeleteMapping(value = "/{id:[0-9]+}")
    @PreAuthorize("@postSecurityService.isAllowedToDelete(#id, principal)") // postSecurityService bu şekilde kullanabiliriz
    public GenericResponse deleteByID(@PathVariable("id") Long id){
        postService.deleteByID(id);
        return new GenericResponse("Post deleted");
    }


//    @GetMapping("/user/{username}/{postID:[0-9]+}")
//    public ResponseEntity<?> getUsersOldPosts(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
//                                              @PathVariable("username") String username,
//                                              @PathVariable("postID") Long postID,
//                                              @RequestParam(name = "count", defaultValue = "false", required = false) boolean count,
//                                              @RequestParam(name = "direction", defaultValue = "before", required = false) String direction) {
//        User user = userService.findByUsername(username);
//        if (count) {
//            Long postCount = postService.getUsersNewPostCount(postID, user);
//            Map<String, Object> map = new HashMap<>();
//            map.put("count", postCount);
//            return ResponseEntity.ok(map);
//        }
//        if (direction.equals("after")) {
//            List<PostDTO> newPosts = postService.getUsersNewPosts(user, postID, pageable.getSort()).stream().
//                    map(PostDTO::new).collect(Collectors.toList());
//            return ResponseEntity.ok(newPosts);
//        }
//        return ResponseEntity.ok(postService.getByUserRelative(pageable, user, postID).map(PostDTO::new));
//    }
}
