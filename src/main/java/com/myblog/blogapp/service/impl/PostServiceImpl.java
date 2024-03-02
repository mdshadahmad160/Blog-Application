package com.myblog.blogapp.service.impl;
import com.myblog.blogapp.entities.Post;
import com.myblog.blogapp.exception.ResourceNotFoundException;
import com.myblog.blogapp.payload.PostDto;
import com.myblog.blogapp.payload.PostResponse;
import com.myblog.blogapp.repository.PostRepository;
import com.myblog.blogapp.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl  implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public PostDto createPost(PostDto postDto) {
         Post post=  mapToEntity(postDto);
        Post  postEntity = postRepository.save(post);
         PostDto dto= mapToDto(postEntity);
         return  dto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //It means that if sortDir is equal to ascending then it will display ascending or else descending
        // its acts like if else condition as well

        Pageable pageable= PageRequest.of(pageNo,pageSize, sort);//.by() is convert String into sort Object
         Page<Post> posts= postRepository.findAll(pageable);//it will give the current pageNumber,Size,TotalPages and TotalElements
        List<Post>  content=posts.getContent();//it will convert into List
        List<PostDto> contents= content.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(contents);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setLast(posts.isLast());
        return  postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
      Post post=  postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Post","id",id)
        );
        PostDto postDto = mapToDto(post);
        return postDto;
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        //get post by id from the database:
      Post post=  postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Post","id",id)
        );
      post.setTitle(postDto.getTitle());
      post.setDescription(postDto.getDescription());
      post.setContent(postDto.getContent());
      Post newPost= postRepository.save(post);
      return  mapToDto(newPost);
    }

    @Override
    public void deletePost(long id) {
        //get post by id from database
        Post post= postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id",id)
        );
        postRepository.deleteById(id);
    }
    //CONVERT DTO TO ENTITY

    public Post mapToEntity(PostDto postDto){
        Post post= mapper.map(postDto,Post.class);


//           Post post=new Post();
//           post.setTitle(postDto.getTitle());
//           post.setDescription(postDto.getDescription());
//           post.setContent(postDto.getContent());
           return  post;
       }
       //CONVERT ENTITY TO DTO

       public PostDto mapToDto(Post post){
        PostDto dto= mapper.map(post,PostDto.class);


//           PostDto dto=new PostDto();
//           dto.setId(post.getId());
//           dto.setTitle(post.getTitle());
//           dto.setDescription(post.getDescription());
//           dto.setContent(post.getContent());
         return  dto;
       }
}
