package com.kdt.kdtsecurity.Post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="posts")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController{
    private final PostService postService;

    @Operation(summary="List posts")
    @GetMapping
    public List<PostResponse> list(){
        return postService.list();
    }

    @Operation(summary = "Create post")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody PostCreateRequest request){
        return postService.create(request);
    }

    @Operation(summary = "Update post")
    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id,
                               @Valid @RequestBody PostUpdateRequest request){
        return this.postService.update(id,request);
    }

    @Operation(summary = "Delete post")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        this.postService.delete(id);
    }


}