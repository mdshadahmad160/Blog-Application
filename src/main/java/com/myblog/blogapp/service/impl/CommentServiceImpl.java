package com.myblog.blogapp.service.impl;
import com.myblog.blogapp.entities.Comment;
import com.myblog.blogapp.entities.Post;
import com.myblog.blogapp.exception.ResourceNotFoundException;
import com.myblog.blogapp.payload.CommentDto;
import com.myblog.blogapp.repository.CommentRepository;
import com.myblog.blogapp.repository.PostRepository;
import com.myblog.blogapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CommentServiceImpl  implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public CommentDto createComment(long postId,CommentDto commentDto){
       Post post=  postRepository.findById(postId).orElseThrow(
                 ()-> new ResourceNotFoundException("Post","id",postId)

         );
     Comment comment=mapToComment(commentDto);
     comment.setPost(post);
        Comment newComment= commentRepository.save(comment);
     CommentDto dto=   mapToDto(newComment);
        return dto;
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
       List<Comment> comments= commentRepository.findByPostId(postId);
       //convert Comment To DTO:
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        Post post=postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id",postId)
        );
     Comment comment=   commentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Comment","id",id)
        );

         comment.setName(commentDto.getName());
         comment.setEmail(commentDto.getEmail());
         comment.setBody(commentDto.getBody());
        Comment updatedComment= commentRepository.save(comment);


         //convert entity to dto
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post=postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id",postId)
        );
        Comment comment=commentRepository.findById(commentId).orElseThrow(
                ()-> new ResourceNotFoundException("Comment","id",commentId)
        );
        commentRepository.deleteById(commentId);
    }

    //CONVERT DTO TO ENTITY
    Comment mapToComment(CommentDto commentDto){

        Comment comment= mapper.map(commentDto,Comment.class);
//        Comment comment=new Comment();
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;
      }
      //CONVERT ENTITY TO DTO
      CommentDto mapToDto(Comment comment){
      CommentDto commentDto= mapper.map(comment,CommentDto.class);


//        CommentDto commentDto=new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return commentDto;
      }
}
