package com.myblog.blogapp.controller;
import com.myblog.blogapp.payload.PostDto;
import com.myblog.blogapp.payload.PostResponse;
import com.myblog.blogapp.service.PostService;
import com.myblog.blogapp.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @PreAuthorize("hasRole('ADMIN')")//it can Access Only Admin Role Post will be happen Only By The Admin
    @PostMapping
    //public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, BindingResult bindingResult){
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto,BindingResult bindingResult){
        if (bindingResult.hasErrors()){

            // return new ResponseEntity<>(postService.createPost(postDto),HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
         return new ResponseEntity<>( postService.createPost(postDto), HttpStatus.CREATED);

    }
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = AppConstants. DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false) String sortDir
            ){
         return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }
    @GetMapping("/{id}")
      public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(postService.getPostById(id));
      }
      //Update post by id:
    @PutMapping("/api/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,@PathVariable(name = "id") long id){
          PostDto dto=postService.updatePost(postDto,id);
          return  new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //Delete post by id:
    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
       postService.deletePost(id);
       return  new ResponseEntity<>("Post Entity Deleted Successfully: ",HttpStatus.OK);
    }

}

