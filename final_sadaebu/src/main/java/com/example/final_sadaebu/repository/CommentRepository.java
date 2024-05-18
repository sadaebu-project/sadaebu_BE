package com.example.final_sadaebu.repository;

//public interface CommentRepository extends JpaRepository<Comment, Long> {
//    List<Comment> findByPost(Post post); // 매개변수의 타입을 Post로 변경합니다.
//}

//@Repository
//public interface CommentRepository extends JpaRepository<Comment, Long> {
//    // 필요한 경우 여기에 맞춤 쿼리 메소드를 정의할 수 있습니다.
//}

import com.example.final_sadaebu.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
