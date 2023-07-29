package com.infinity.fashionity.comments.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.comments.dto.CommentSaveDTO;
import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.dto.LoginDTO;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.repository.PostRepository;
import com.infinity.fashionity.security.service.JwtProvider;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.events.Comment;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.controller;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class CommentIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper mapper;

    public final String BASE_URL = "/api/v1";
    public List<MemberEntity> memberList;
    public Map<Long, List<PostEntity>> memberPosts;
    public Map<Long, List<CommentEntity>> postComments;

    @BeforeEach
    public void dummyInsert() {
        memberList = new ArrayList<>();
        memberPosts = new HashMap<>();
        postComments = new HashMap<>();
        //member등록
        IntStream.rangeClosed(1, 5).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                    .id("tester".concat(Integer.toString(i)))
                    .password(passwordEncoder.encode("Abcdefg123!"))
                    .nickname("tester".concat(Integer.toString(i)))
                    .email("tester".concat(Integer.toString(i)).concat("@gmail.com"))
                    .sns(false)
                    .profileUrl("profileUrl".concat(Integer.toString(i)))
                    .profileIntro("profileIntro".concat(Integer.toString(i)))
                    .gender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE)
                    .height(150.0f)
                    .weight(55.0f)
                    .build();
            member.getMemberRoles().add(MemberRoleEntity.builder()
                    .member(member)
                    .memberRole(MemberRole.USER)
                    .build());

            memberList.add(member);
        });
        memberRepository.saveAll(memberList);

        //각 멤버마다 post등록
        for (MemberEntity member : memberList) {
            List<PostEntity> postList = new ArrayList<>();
            for(int i=0;i<3;i++){
                PostEntity post = PostEntity.builder()
                        .member(member)
                        .content(member.getNickname().concat("\'s post").concat(Integer.toString(i)))
                        .build();
                postList.add(post);
            }
            postRepository.saveAll(postList);
            memberPosts.put(member.getSeq(), postList);

            for (PostEntity post : postList) {
                //각 포스트마다 댓글 등록
                List<CommentEntity> commentList = new ArrayList<>();
                for(int i=0 ;i< memberList.size() ;i++){
                    CommentEntity comment = CommentEntity.builder()
                            .member(memberList.get(i))
                            .post(post)
                            .content(Long.toString(post.getSeq()).concat("포스트의 댓글").concat(Integer.toString(i)))
                            .build();
                    commentList.add(comment);
                }
                commentRepository.saveAll(commentList);
                postComments.put(post.getSeq(), commentList);
            }
        }
    }

    public LoginDTO.Response memberLogin(Long seq) throws Exception {
        List<MemberRoleEntity> roles = new ArrayList<>();
        roles.add(MemberRoleEntity.builder()
                .member(MemberEntity.builder().seq(seq).build())
                .memberRole(MemberRole.USER)
                .build());
        return LoginDTO.Response.builder()
                .accessToken(jwtProvider.createAccessToken(seq,roles))
                .build();
    }

    /**
     * - 댓글 정상 등록
     * - 인증되지 않은 사용자가 댓글을 달려했을 때 오류
     * - 존재하지 않는 Post Seq로 접근했을 때 오류
     * - content가 null일 경우 오류
     * - content가 blank로 들어왔을 때 오류
     * - content가 200자를 넘겼을 때 오류
     * - 존재하지 않는 사용자가 댓글을 달려했을 때 오류
     */
    @Nested
    @DisplayName("Comment Save Test")
    public class CommentSaveTest {
        Long randomMemberSeq;//댓글을 다는 사람의 seq
        Long randomTargetMemberSeq;//댓글이 달리는 post의 주인 seq
        Long randomTargetPostSeq;//댓글이 달리는 포스트의 seq

        CommentSaveDTO.Request request;

        @BeforeEach
        public void saveInit(){
            Random random = new Random();
            //댓글을 다는 사람
            randomMemberSeq = memberList.get(Math.abs(random.nextInt())%memberList.size()).getSeq();
            //댓글이 달리는 게시글의 주인
            randomTargetMemberSeq = memberList.get(Math.abs(random.nextInt())%memberList.size()).getSeq();

            //댓글을 달 게시판
            List<PostEntity> posts = memberPosts.get(randomTargetMemberSeq);
            randomTargetPostSeq = posts.get(Math.abs(random.nextInt())%posts.size()).getSeq();

            //등록할 comment
            request = CommentSaveDTO.Request.builder()
                    .content("comment!!!!!")
                    .build();
        }

        @Test
        @DisplayName("- 댓글 정상 등록")
        public void commentsSaveSuccessTest() throws Exception {
            //로그인한 결과 accessToken을 받아옴
            LoginDTO.Response token = memberLogin(randomMemberSeq);

            //검증
            ResultActions resultActions = mvc.perform(post(BASE_URL.concat("/posts/{postSeq}/comments"), randomTargetPostSeq)
                            .header("Authorization", "Bearer " + token.getAccessToken())
                            .contentType("application/json")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(CommentController.class))
                    .andExpect(handler().methodName("saveComment"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success", is(true)));

            //저장된 상태의 comments를 가져옴
            List<CommentEntity> origin = postComments.get(randomTargetPostSeq);
            Optional<CommentEntity> newComment = commentRepository.findAllByPost(PostEntity.builder()
                            .seq(randomTargetPostSeq)
                            .build())
                    .stream().filter(c -> !origin.contains(c))
                    .findAny();

            assertThat(newComment).isNotEmpty();
            resultActions.andExpect(jsonPath("$.seq",is(newComment.get().getSeq().intValue())));
        }

        @Test
        @DisplayName("- 인증되지 않은 사용자가 댓글을 달려했을 때 오류")
        public void unAuthenticatedUserSavesCommentsTest() throws Exception {
            ErrorCode code = ErrorCode.UNAUTHENTICATED_MEMBER;
            //검증
            ResultActions resultActions = mvc.perform(post(BASE_URL.concat("/posts/{postSeq}/comments"), randomTargetPostSeq)
                            .contentType("application/json")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code",is(code.getCode())))
                    .andExpect(jsonPath("$.message",is(code.getMessage())));
        }

        @Test
        @DisplayName("- 존재하지 않는 Post Seq로 접근했을 때 오류")
        public void commentsSaveWithUnknownPostSeqTest() throws Exception {
            Long unknownSeq = Long.MAX_VALUE;
            //로그인한 결과 accessToken을 받아옴
            LoginDTO.Response token = memberLogin(randomMemberSeq);

            ErrorCode code = ErrorCode.POST_NOT_FOUND;
            Long unknownPostSeq = Long.MAX_VALUE;
            //검증
            ResultActions resultActions = mvc.perform(post(BASE_URL.concat("/posts/{postSeq}/comments"), unknownPostSeq)
                            .header("Authorization", "Bearer " + token.getAccessToken())
                            .contentType("application/json")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code",is(code.getCode())))
                    .andExpect(jsonPath("$.message",is(code.getMessage())));
        }

        @Test
        @DisplayName("- content가 null일 경우 오류")
        public void commentsSaveWithNullContentTest() throws Exception{
            //로그인한 결과 accessToken을 받아옴
            LoginDTO.Response token = memberLogin(randomMemberSeq);
            ErrorCode code = ErrorCode.INVALID_INPUT_VALUE;


            request.setContent(null);
            //검증
            ResultActions resultActions = mvc.perform(post(BASE_URL.concat("/posts/{postSeq}/comments"), randomTargetPostSeq)
                            .header("Authorization","Bearer "+token.getAccessToken())
                            .contentType("application/json")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code",is(code.getCode())))
                    .andExpect(jsonPath("$.message",is("content를 입력해주세요")));
        }

        @Test
        @DisplayName("- content가 blank로 들어왔을 때 오류")
        public void commentsSaveWithBlankContentTest() throws Exception {
            //로그인한 결과 accessToken을 받아옴
            LoginDTO.Response token = memberLogin(randomMemberSeq);
            ErrorCode code = ErrorCode.INVALID_INPUT_VALUE;


            request.setContent(" ");
            //검증
            ResultActions resultActions = mvc.perform(post(BASE_URL.concat("/posts/{postSeq}/comments"), randomTargetPostSeq)
                            .header("Authorization","Bearer "+token.getAccessToken())
                            .contentType("application/json")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code",is(code.getCode())))
                    .andExpect(jsonPath("$.message",is("content를 입력해주세요")));
        }

        @Test
        @DisplayName("- content가 200자를 넘겼을 때 오류")
        public void commentsSaveWithContentOverlengthTest() throws Exception {
            //로그인한 결과 accessToken을 받아옴
            LoginDTO.Response token = memberLogin(randomMemberSeq);
            ErrorCode code = ErrorCode.INVALID_INPUT_VALUE;


            request.setContent(StringUtils.randomSting(205));
            //검증
            ResultActions resultActions = mvc.perform(post(BASE_URL.concat("/posts/{postSeq}/comments"), randomTargetPostSeq)
                            .header("Authorization","Bearer "+token.getAccessToken())
                            .contentType("application/json")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code",is(code.getCode())))
                    .andExpect(jsonPath("$.message",is("최대 200자까지 입력할 수 있습니다.")));
        }

        @Test
        @DisplayName("- 존재하지 않는 사용자가 댓글을 달려했을 때 오류")
        public void commentsSaveWithUnsavedUserTest() throws Exception {
            //로그인한 결과 accessToken을 받아옴
            LoginDTO.Response token = memberLogin(Long.MAX_VALUE);
            ErrorCode code = ErrorCode.MEMBER_NOT_FOUND;

            //검증
            ResultActions resultActions = mvc.perform(post(BASE_URL.concat("/posts/{postSeq}/comments"), randomTargetPostSeq)
                            .header("Authorization","Bearer "+token.getAccessToken())
                            .contentType("application/json")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code",is(code.getCode())))
                    .andExpect(jsonPath("$.message",is(code.getMessage())));
        }
    }

    /**
     * - 댓글 정상 조회
     * - 삭제된 댓글은 조회하지 않기
     * - 삭제된 유저들의 댓글은 조회하지 않기
     * - 없는 페이지 조회 - 빈 list return
     */
    @Nested
    @DisplayName("Comment List Test")
    @Disabled
    public class CommentListTest {
        @Test
        @DisplayName("- 댓글 정상 조회")
        public void getCommentsListSuccessTest() {

        }

        @Test
        @DisplayName("- 없는 페이지 조회 - 빈 list return")
        public void getCommentsListWithEmptyTest() {

        }

        @Test
        @DisplayName("- 존재하지 않는 Post Seq로 접근했을 때 오류")
        public void commentsSaveWithUnknownPostSeqTest() {

        }

        @Test
        @DisplayName("- content가 null일 경우 오류")
        public void commentsSaveWithNullContentTest() {

        }

        @Test
        @DisplayName("- content가 blank로 들어왔을 때 오류")
        public void commentsSaveWithBlankContentTest() {

        }

        @Test
        @DisplayName("- content가 200자를 넘겼을 때 오류")
        public void commentsSaveWithContentOverlengthTest() {

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
     */
    @Nested
    @DisplayName("Comment Update Test")
    @Disabled
    public class CommentUpdateTest {
        @Test
        @DisplayName("- 댓글 정상 수정")
        public void commentsUpdateSuccessTest() {

        }

        @Test
        @DisplayName("- 타인의 댓글 수정 오류")
        public void updateOthersCommentTest() {

        }

        @Test
        @DisplayName("- 삭제된 포스트의 댓글 수정 오류")
        public void commentsUpdateWithNonexistentPostTest() {

        }

        @Test
        @DisplayName("- 인증되지 않은 사용자의 댓글 수정 오류")
        public void commentUpdateWithUnauthenticatedUserTest() {

        }

        @Test
        @DisplayName("- 존재하지 않는 댓글 수정 오류")
        public void commentsUpdateWithNonexistentTest() {

        }

        @Test
        @DisplayName("- 삭제된 댓글 수정 오류")
        public void commentsUpdateWithDeletedTest() {

        }

        @Test
        @DisplayName("- content가 null일 경우 오류")
        public void commentsUpdateWithNullContentTest() {

        }

        @Test
        @DisplayName("- content가 blank일 경우 오류")
        public void commentsUpdateWithBlankContentTest() {

        }

        @Test
        @DisplayName("- content가 200자를 넘겼을 경우 오류")
        public void commentsUpdateWithOverlengthContentTest() {

        }

        @Test
        @DisplayName("- 삭제된 사용자의 댓글 수정 오류")
        public void commentsUpdateWithDeletedUserTest() {

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
     */
    @Nested
    @DisplayName("Comment Delete Test")
    @Disabled
    public class CommentDeleteTest {
        @Test
        @DisplayName("- 댓글 정상 삭제")
        public void commentsDeleteSuccessTest() {

        }

        @Test
        @DisplayName("- 인증되지 않은 사용자의 댓글 삭제 오류")
        public void commentsDeletedByUnauthenticatedUserTest() {

        }

        @Test
        @DisplayName("- 타인의 댓글을 삭제 오류")
        public void commentsDeletedByOtherUserTest() {

        }

        @Test
        @DisplayName("- 존재하지 않는 포스트의 댓글 삭제 오류")
        public void commentsDeleteWithNonexistentPostSeqTest() {

        }

        @Test
        @DisplayName("- 삭제된 포스트의 댓글 삭제 오류")
        public void commentsDeleteWithDeletedPostSeqTest() {

        }

        @Test
        @DisplayName("- 존재하지 않는 댓글 삭제 오류")
        public void nonexistentCommentDeleteTest() {

        }

        @Test
        @DisplayName("- 삭제된 댓글 삭제 오류")
        public void commentsDeleteWithDeletedCommentTest() {

        }
    }

    /**
     * - 좋아요
     * - 좋아요 취소
     * - 삭제된 게시글의 댓글에 좋아요 오류
     * - 존재하지 않는 댓글에 좋아요 오류
     * - 인증되지 않은 사용자 좋아요 요청 오류
     */
    @Nested
    @DisplayName("Comment Like Test")
    @Disabled
    public class CommentLikeTest {
        @Test
        @DisplayName("- 좋아요")
        public void commentLikeTest() {

        }

        @Test
        @DisplayName("- 좋아요 취소")
        public void commentUnlikeTest() {

        }

        @Test
        @DisplayName("- 삭제된 게시글의 댓글에 좋아요 오류")
        public void commentLikeWithDeletedCommentTest() {

        }

        @Test
        @DisplayName("- 존재하지 않는 댓글에 좋아요 오류")
        public void commentLikeWithNonexistentCommentTest() {

        }

        @Test
        @DisplayName("- 인증되지 않은 사용자 좋아요 요청 오류")
        public void commentLikeWithUnauthenticatedUserTest() {

        }
    }

    /**
     * - 신고 성공
     * - 신고 내용은 null가능
     * - 중복 신고 오류
     * - 존재하지 않는 게시글의 댓글 신고 오류
     * - 삭제된 게시글의 댓글 신고 오류
     * - 존재하지 않는 댓글 신고 오류
     * - 삭제된 댓글 신고 오류
     * - 카테고리 미설정 오류
     */
    @Nested
    @DisplayName("Comment Report Test")
    @Disabled
    public class CommentReportTest {
        @Test
        @DisplayName("- 신고 성공")
        public void commentReportSuccessTest() {

        }

        @Test
        @DisplayName("- 중복 신고 오류")
        public void commentReportDuplicatedTest() {

        }

        @Test
        @DisplayName("- 존재하지 않는 게시글의 댓글 신고 오류")
        public void commentReportWithNonexistentPostTest() {

        }

        @Test
        @DisplayName("- 삭제된 게시글의 댓글 신고 오류")
        public void commentReportWithDeletedPostTest() {

        }

        @Test
        @DisplayName("- 존재하지 않는 댓글 신고 오류")
        public void commentReportWithDeletedCommentTest() {

        }

        @Test
        @DisplayName("- 삭제된 댓글 신고 오류")
        public void commentReportWith() {

        }
    }
}