package com.infinity.fashionity.comments.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureMockMvc
@SpringBootTest
class CommentIntegrationTest {
    @Autowired
    private MockMvc mvc;

    /**
     * 1. 댓글 정상 등록
     * 2. 인증되지 않은 사용자가 댓글을 달려했을 때 오류
     * 3. 존재하지 않는 Post ID로 접근했을 때 오류
     * 4. content가 null일 경우 오류
     * 5. content가 blank로 들어왔을 때 오류
     * 6. content가 200자를 넘겼을 때 오류
     * */
    @Nested
    @DisplayName("Comment Save Test")
    public class CommentSaveTest{

    }

    /**
     * 1. 댓글 정상 조회
     *      - 삭제된 댓글은 조회하지 않기
     *      - 삭제된 유저들의 댓글은 조회하지 않기
     * 2. 없는 페이지 조회 - 빈 list return
     * */
    @Nested
    @DisplayName("Comment List Test")
    public class CommentListTest{

    }

    /**
     * 1. 댓글 정상 수정
     * 2. 타인의 댓글 수정 오류
     * 3. 인증되지 않은 사용자의 댓글 수정 오류
     * 4. 존재하지 않는 포스트의 댓글 수정 오류
     * 5. 삭제된 포스트의 댓글 수정 오류
     * 6. 존재하지 않는 댓글 수정 오류
     * 7. 삭제된 댓글 수정 오류
     * 8. content가 null일 경우 오류
     * 9. content가 blank일 경우 오류
     * 10. content가 200자를 넘겼을 경우 오류
     * */
    @Nested
    @DisplayName("Comment Update Test")
    public class CommentUpdateTest{

    }

    /**
     * 1. 댓글 정상 삭제
     * 2. 인증되지 않은 사용자의 댓글 삭제 오류
     * 3. 타인의 댓글을 삭제 오류
     * 4. 존재하지 않는 포스트의 댓글 삭제 오류
     * 5. 삭제된 포스트의 댓글 삭제 오류
     * 6. 존재하지 않는 댓글 삭제 오류
     * 7. 삭제된 댓글 삭제 오류
     * */
    @Nested
    @DisplayName("Comment Delete Test")
    public class CommentDeleteTest{

    }

    /**
     * 1. 좋아요
     * 2. 좋아요 취소
     * 3. 삭제된 게시글의 댓글에 좋아요 오류
     * 4. 존재하지 않는 댓글에 좋아요 오류
     * 5. 삭제된 댓글 좋아요 오류
     * 6. 인증되지 않은 사용자 좋아요 요청 오류
     * */
    @Nested
    @DisplayName("Comment Like Test")
    public class CommentLikeTest{

    }

    /**
     * 1. 신고 성공
     *  - 신고 내용은 null가능
     * 2. 중복 신고 오류
     * 3. 존재하지 않는 게시글의 댓글 신고 오류
     * 4. 삭제된 게시글의 댓글 신고 오류
     * 5. 존재하지 않는 댓글 신고 오류
     * 6. 삭제된 댓글 신고 오류
     * 7. 카테고리 미설정 오류
     * */
    @Nested
    @DisplayName("Comment Report Test")
    public class CommentReportTest{

    }
}