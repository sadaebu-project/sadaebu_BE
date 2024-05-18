package com.example.final_sadaebu.controller;

//@Slf4j //로깅 위한 어노테이션
//@RestController// REST API 용 컨트롤러
//@RequestMapping("/api/posts")
//public class PostController {
//
//    @Autowired
//    private PostRepository postRepository;
//    //모든 게시글 조회
//    @GetMapping
//    public List<Post> getAllPosts(){
//        return postRepository.findAll();
//    }
//    //특정 id의 게시글 조회
//    @GetMapping("/{id}")
//    public Post getPostById(@PathVariable Long id){
//        return postRepository.findById(id).orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다. ID : "+ id));
//    }
//
//    //새로운 게시글 작성
//    @PostMapping
//    public Post createPost (@RequestBody Post post){
//        return postRepository.save(post);
//    }
//
//    //특정 id의 게시글 수정
//    @PatchMapping("/{id}")
//    public ResponseEntity<Post> partiallyUpdatePost(@PathVariable Long id, @RequestBody Post updatedPost){
//        Optional<Post> optionalPost = postRepository.findById(id);
//
//        if (optionalPost.isPresent()) {
//            Post existingPost = optionalPost.get();
//
//            //내용이 주어진 경우에만 업데이트 -> 아이디는 수정 불 가
//            if(updatedPost.getContent() != null){
//                existingPost.setContent(updatedPost.getContent());
//            }
//            postRepository.save(existingPost);
//            return ResponseEntity.ok(existingPost);
//        }
//        else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    //특정 id의 게시글 삭제
//    @DeleteMapping("/{id}")
//    public void deletePost(@PathVariable Long id){
//        postRepository.deleteById(id);
//    }
//}


import com.example.final_sadaebu.entity.Comment;
import com.example.final_sadaebu.entity.Post;
import com.example.final_sadaebu.repository.CommentRepository;
import com.example.final_sadaebu.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j //로깅 위한 어노테이션
@RestController// REST API 용 컨트롤러
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    // 모든 게시글 조회
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 특정 id의 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // 새로운 게시글 작성
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post savedPost = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    // 특정 id의 게시글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Post> partiallyUpdatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            // 내용이 주어진 경우에만 업데이트
            if (updatedPost.getContent() != null) {
                existingPost.setContent(updatedPost.getContent());
            }
            postRepository.save(existingPost);
            return ResponseEntity.ok(existingPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 id의 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 게시글의 댓글 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID : " + postId));
        return ResponseEntity.ok(post.getComments());
    }

    // 특정 게시글에 댓글 작성
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addCommentToPost(@PathVariable Long postId, @RequestBody Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID : " + postId));
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }
}