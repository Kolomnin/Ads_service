package com.example.finaltask.service;

import com.example.finaltask.mapping.CommentMapper;
import com.example.finaltask.model.dto.CommentDTO;
import com.example.finaltask.model.dto.ResponseWrapperComment;
import com.example.finaltask.model.entity.Comment;
import com.example.finaltask.repository.AdsRepository;
import com.example.finaltask.repository.CommentRepository;
import com.example.finaltask.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final AdsService adsService;

    private final UserRepository userRepository;

    private final AdsRepository adsRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper,
                          AdsService adsService, UserRepository userRepository, AdsRepository adsRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.adsService = adsService;
        this.userRepository = userRepository;
        this.adsRepository = adsRepository;
    }

//    public CommentDTO addComment(CreateCommentDTO createCommentDTO, Integer id, @NonNull Authentication authentication) {
//        System.out.println(createCommentDTO.getText());
//        Integer userId = userRepository.findByEmail(authentication.getName()).get().getId();
//        System.out.println("id комментарий"+userId);
//        Comment comment = commentMapper.toEntity(createCommentDTO);
//        Ads ads = adsService.getAdsById(id).orElseThrow();
//        comment.setAds(ads);
//        comment.setCreatedAt(LocalDateTime.now());
//        comment.setAuthorId(userRepository.findByEmail(authentication.getName()).orElseThrow());
//        commentRepository.save(comment);
//        return commentMapper.toDto(comment);
//    }
    public CommentDTO addComment(Integer id, CommentDTO commentDto, Authentication authentication) {
        if (!adsRepository.existsById(id)) {
            throw new IllegalArgumentException("Ad not found");
        }
        Comment newComment = commentMapper.toEntity(commentDto);
        newComment.setAds(adsRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Ad not found")));
        newComment.setAuthorId(userRepository.findByEmail(authentication.getName()).orElseThrow());
        commentRepository.save(newComment);
        return commentMapper.toDto(newComment);
    }

    public ResponseWrapperComment getAllCommentsByAdsId(Integer id) {
        List<Comment> comments = commentRepository.findAllByAds_Id((id));
        ResponseWrapperComment responseWrapperComment = new ResponseWrapperComment();
        responseWrapperComment.setCount(comments.size());
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOS.add(commentMapper.toDto(comment));
        }
        responseWrapperComment.setResults(commentDTOS);
        return responseWrapperComment;
    }

//    public void getCommentById(Long id){
//
//    }
    public void deleteCommentById(Integer id) {
        commentRepository.deleteById(id);
    }

    public CommentDTO editComment(CommentDTO commentDTO) {
        Comment comment = commentMapper.toEntity(commentDTO);
        commentRepository.save(comment);
        CommentDTO commentDTO1 = commentMapper.toDto(commentRepository.findById(comment.getId()).orElseThrow());
        return commentDTO1;
    }


}
