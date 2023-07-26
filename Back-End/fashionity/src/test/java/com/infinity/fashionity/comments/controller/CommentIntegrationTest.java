package com.infinity.fashionity.comments.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
     * - 댓글 정상 등록
     * - 인증되지 않은 사용자가 댓글을 달려했을 때 오류
     * - 존재하지 않는 Post Seq로 접근했을 때 오류
     * - content가 null일 경우 오류
     * - content가 blank로 들어왔을 때 오류
     * - content가 200자를 넘겼을 때 오류
     * - 존재하지 않는 사용자가 댓글을 달려했을 때 오류
     * */
    @Nested
    @DisplayName("Comment Save Test")
    public class CommentSaveTest{
        @Test
        @DisplayName("- 댓글 정상 등록")
        public void commentsSaveSuccessTest(){

        }

        @Test
        @DisplayName("- 인증되지 않은 사용자가 댓글을 달려했을 때 오류")
        public void unAuthenticatedUserSavesCommentsTest(){

        }

        @Test
        @DisplayName("- 존재하지 않는 Post Seq로 접근했을 때 오류")
        public void commentsSaveWithUnknownPostSeqTest(){

        }

        @Test
        @DisplayName("- content가 null일 경우 오류")
        public void commentsSaveWithNullContentTest(){

        }

        @Test
        @DisplayName("- content가 blank로 들어왔을 때 오류")
        public void commentsSaveWithBlankContentTest(){

        }

        @Test
        @DisplayName("- content가 200자를 넘겼을 때 오류")
        public void commentsSaveWithContentOverlengthTest(){

        }

        @Test
        @DisplayName("- 존재하지 않는 사용자가 댓글을 달려했을 때 오류")
        public void commentsSaveWithUnsavedUserTest(){

        }
    }

    /**
     * - 댓글 정상 조회
     *      - 삭제된 댓글은 조회하지 않기
     *      - 삭제된 유저들의 댓글은 조회하지 않기
     * - 없는 페이지 조회 - 빈 list return
     * */
    @Nested
    @DisplayName("Comment List Test")
    public class CommentListTest{
        @Test
        @DisplayName("- 댓글 정상 조회")
        public void getCommentsListSuccessTest(){

        }

        @Test
        @DisplayName("- 없는 페이지 조회 - 빈 list return")
        public void getCommentsListWithEmptyTest(){

        }

        @Test
        @DisplayName("- 존재하지 않는 Post Seq로 접근했을 때 오류")
        public void commentsSaveWithUnknownPostSeqTest(){

        }

        @Test
        @DisplayName("- content가 null일 경우 오류")
        public void commentsSaveWithNullContentTest(){

        }

        @Test
        @DisplayName("- content가 blank로 들어왔을 때 오류")
        public void commentsSaveWithBlankContentTest(){

        }

        @Test
        @DisplayName("- content가 200자를 넘겼을 때 오류")
        public void commentsSaveWithContentOverlengthTest(){

        }
    }

    /**
     * - 댓글 정상 수정
     * - 타인의 댓글 수정 오류
     * - 인증되지 않은 사용자의 댓글 수정 오류
     * - 삭제된 포스트의 댓글 수정 오류
     * - 존재하지 않는 댓글 수정 오류
     * - 삭제된 댓글 수정 오류
     * - content가 null일 경우 오류
     * - content가 blank일 경우 오류
     * - content가 200자를 넘겼을 경우 오류
     * - 삭제된 유저 or 존재하지 않는 사용자의 댓글 수정 오류
     * */
    @Nested
    @DisplayName("Comment Update Test")
    public class CommentUpdateTest{
        @Test
        @DisplayName("- 댓글 정상 수정")
        public void commentsUpdateSuccessTest(){

        }

        @Test
        @DisplayName("- 타인의 댓글 수정 오류")
        public void updateOthersCommentTest(){

        }

        @Test
        @DisplayName("- 삭제된 포스트의 댓글 수정 오류")
        public void commentsUpdateWithNonexistentPostTest(){

        }

        @Test
        @DisplayName("- 인증되지 않은 사용자의 댓글 수정 오류")
        public void commentUpdateWithUnauthenticatedUserTest(){

        }

        @Test
        @DisplayName("- 존재하지 않는 댓글 수정 오류")
        public void commentsUpdateWithNonexistentTest(){

        }

        @Test
        @DisplayName("- 삭제된 댓글 수정 오류")
        public void commentsUpdateWithDeletedTest(){

        }

        @Test
        @DisplayName("- content가 null일 경우 오류")
        public void commentsUpdateWithNullContentTest(){

        }

        @Test
        @DisplayName("- content가 blank일 경우 오류")
        public void commentsUpdateWithBlankContentTest(){

        }

        @Test
        @DisplayName("- content가 200자를 넘겼을 경우 오류")
        public void commentsUpdateWithOverlengthContentTest(){

        }

        @Test
        @DisplayName("- 삭제된 사용자의 댓글 수정 오류")
        public void commentsUpdateWithDeletedUserTest(){

        }
    }

    /**
     * - 댓글 정상 삭제
     * - 인증되지 않은 사용자의 댓글 삭제 오류
     * - 타인의 댓글을 삭제 오류
     * - 존재하지 않는 포스트의 댓글 삭제 오류
     * - 삭제된 포스트의 댓글 삭제 오류
     * - 존재하지 않는 댓글 삭제 오류
     * - 삭제된 댓글 삭제 오류
     * */
    @Nested
    @DisplayName("Comment Delete Test")
    public class CommentDeleteTest{
        @Test
        @DisplayName("- 댓글 정상 삭제")
        public void commentsDeleteSuccessTest(){

        }

        @Test
        @DisplayName("- 인증되지 않은 사용자의 댓글 삭제 오류")
        public void commentsDeletedByUnauthenticatedUserTest(){

        }

        @Test
        @DisplayName("- 타인의 댓글을 삭제 오류")
        public void commentsDeletedByOtherUserTest(){

        }

        @Test
        @DisplayName("- 존재하지 않는 포스트의 댓글 삭제 오류")
        public void commentsDeleteWithNonexistentPostSeqTest(){

        }

        @Test
        @DisplayName("- 삭제된 포스트의 댓글 삭제 오류")
        public void commentsDeleteWithDeletedPostSeqTest(){

        }

        @Test
        @DisplayName("- 존재하지 않는 댓글 삭제 오류")
        public void nonexistentCommentDeleteTest(){

        }

        @Test
        @DisplayName("- 삭제된 댓글 삭제 오류")
        public void commentsDeleteWithDeletedCommentTest(){

        }
    }

    /**
     * - 좋아요
     * - 좋아요 취소
     * - 삭제된 게시글의 댓글에 좋아요 오류
     * - 존재하지 않는 댓글에 좋아요 오류
     * - 인증되지 않은 사용자 좋아요 요청 오류
     * */
    @Nested
    @DisplayName("Comment Like Test")
    public class CommentLikeTest{
        @Test
        @DisplayName("- 좋아요")
        public void commentLikeTest(){

        }

        @Test
        @DisplayName("- 좋아요 취소")
        public void commentUnlikeTest(){

        }

        @Test
        @DisplayName("- 삭제된 게시글의 댓글에 좋아요 오류")
        public void commentLikeWithDeletedCommentTest(){

        }

        @Test
        @DisplayName("- 존재하지 않는 댓글에 좋아요 오류")
        public void commentLikeWithNonexistentCommentTest(){

        }

        @Test
        @DisplayName("- 인증되지 않은 사용자 좋아요 요청 오류")
        public void commentLikeWithUnauthenticatedUserTest(){

        }
    }

    /**
     * - 신고 성공
     *  - 신고 내용은 null가능
     * - 중복 신고 오류
     * - 존재하지 않는 게시글의 댓글 신고 오류
     * - 삭제된 게시글의 댓글 신고 오류
     * - 존재하지 않는 댓글 신고 오류
     * - 삭제된 댓글 신고 오류
     * - 카테고리 미설정 오류
     * */
    @Nested
    @DisplayName("Comment Report Test")
    public class CommentReportTest{
        @Test
        @DisplayName("- 신고 성공")
        public void commentReportSuccessTest(){

        }

        @Test
        @DisplayName("- 중복 신고 오류")
        public void commentReportDuplicatedTest(){

        }

        @Test
        @DisplayName("- 존재하지 않는 게시글의 댓글 신고 오류")
        public void commentReportWithNonexistentPostTest(){

        }

        @Test
        @DisplayName("- 삭제된 게시글의 댓글 신고 오류")
        public void commentReportWithDeletedPostTest(){

        }

        @Test
        @DisplayName("- 존재하지 않는 댓글 신고 오류")
        public void commentReportWithDeletedCommentTest(){

        }

        @Test
        @DisplayName("- 삭제된 댓글 신고 오류")
        public void commentReportWith(){

        }
    }
}