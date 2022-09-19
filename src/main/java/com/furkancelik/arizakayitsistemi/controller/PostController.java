package com.furkancelik.arizakayitsistemi.controller;

import com.furkancelik.arizakayitsistemi.annotation.CurrentUser;
import com.furkancelik.arizakayitsistemi.dto.PostDTO;
import com.furkancelik.arizakayitsistemi.dto.PostSubmitDTO;
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
    public Page<PostDTO> getAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getAll(pageable).map(PostDTO::new);
    }

    @GetMapping(value = {"/{id:[0-9]+}", "/user/{username}/{id:[0-9]+}"}) // id değerinin sayı olduğunu belirtmek regular exp kullanıldı. + işareti
    //Page<PostDTO>                        //birden fazla basamak için kullanıldı
    public ResponseEntity<?> getAllRelative(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                            @PathVariable(name = "id") Long id,
                                            @PathVariable(name = "username", required = false) String username,
                                            @RequestParam(name = "count", defaultValue = "false", required = false) boolean count,
                                            @RequestParam(name = "direction", defaultValue = "before", required = false) String direction) {
        if (count) {
            Long postCount = postService.getNewPostsCount(id, username);
            Map<String, Object> map = new HashMap<>();
            map.put("count", postCount);
            return ResponseEntity.ok(map);
        }
        if (direction.equals("after")) {
            List<PostDTO> newPosts = postService.getNewPosts(id, username, pageable.getSort()).stream().
                    map(PostDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(newPosts);
        }
        return ResponseEntity.ok(postService.getOldPosts(id, username, pageable).map(PostDTO::new));
    }

    @GetMapping("/user/{username}")
    public Page<PostDTO> getUsersPostsRelative(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                               @PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        return postService.getByUser(pageable, user).map(PostDTO::new);
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
