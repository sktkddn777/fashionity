package com.infinity.fashionity.comments.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.comments.dto.*;
import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.comments.entity.CommentLikeEntity;
import com.infinity.fashionity.comments.entity.CommentLikeKey;
import com.infinity.fashionity.comments.entity.CommentReportKey;
import com.infinity.fashionity.comments.repository.CommentLikeRepository;
import com.infinity.fashionity.comments.repository.CommentReportRepository;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.auth.dto.LoginDTO;
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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.controller;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * TODO
 * post_seq 와 comment가 포함된 post_seq가 다른경우도 추가
 */
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class CommentIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CommentReportRepository commentReportRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

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
    private final String CONTENT_TYPE = "application/json";

    private final Charset CHARSET = StandardCharsets.UTF_8;

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
            for (int i = 0; i < 3; i++) {
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
                for (int i = 0; i < memberList.size() * 2; i++) {
                    CommentEntity comment = CommentEntity.builder()
                            .member(memberList.get(i % memberList.size()))
                            .post(post)
                            .content(Long.toString(post.getSeq()).concat("post\'s comment").concat(Integer.toString(i)))
                            .build();
                    commentList.add(comment);
                }
                commentRepository.saveAll(commentList);
                postComments.put(post.getSeq(), commentList);
            }
        }
    }
//
//    @Test
//    @Commit
//    public void a(){}

    public LoginDTO.Response memberLogin(Long seq) throws Exception {
        List<MemberRoleEntity> roles = new ArrayList<>();
        roles.add(MemberRoleEntity.builder()
                .member(MemberEntity.builder().seq(seq).build())
                .memberRole(MemberRole.USER)
                .build());
        return LoginDTO.Response.builder()
                .accessToken(jwtProvider.createAccessToken(seq, roles))
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
        //댓글을 다는 사람의 seq
        Long randomMemberSeq;
        //accessToken
        String token;

        //댓글이 달리는 post의 주인 seq
        Long randomTargetMemberSeq;
        //댓글이 달리는 포스트의 seq
        Long randomTargetPostSeq;

        CommentSaveDTO.Request request;

        /**
         * 인자를 받아 등록 요청을 보내주는 메서드
         */
        public ResultActions sendRequest(Long postSeq, String token, CommentSaveDTO.Request content) throws Exception {
            MockHttpServletRequestBuilder authorization = post(BASE_URL.concat("/posts/{postSeq}/comments"), postSeq)
                    .contentType(CONTENT_TYPE)
                    .characterEncoding(CHARSET);
            if (!StringUtils.isBlank(token)) {
                authorization.header("Authorization", "Bearer ".concat(token));
            }
            if (content != null) {
                authorization.content(mapper.writeValueAsString(content));
            }
            return mvc.perform(authorization);
        }

        @BeforeEach
        public void saveInit() throws Exception {
            Random random = new Random();
            //댓글을 다는 사람
            randomMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();
            //accessToken
            token = memberLogin(randomMemberSeq).getAccessToken();
            //댓글이 달리는 게시글의 주인
            randomTargetMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();

            //댓글을 달 게시판
            List<PostEntity> posts = memberPosts.get(randomTargetMemberSeq);
            randomTargetPostSeq = posts.get(Math.abs(random.nextInt()) % posts.size()).getSeq();

            //등록할 comment
            request = CommentSaveDTO.Request.builder()
                    .content("comment!!!!!")
                    .build();
        }

        @Test
        @DisplayName("- 댓글 정상 등록")
        public void commentsSaveSuccessTest() throws Exception {
            //검증
            ResultActions resultActions = sendRequest(randomTargetPostSeq, token, request)
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
            resultActions.andExpect(jsonPath("$.seq", is(newComment.get().getSeq().intValue())));
        }

        @Test
        @DisplayName("- 인증되지 않은 사용자가 댓글을 달려했을 때 오류")
        public void unAuthenticatedUserSavesCommentsTest() throws Exception {
            ErrorCode code = ErrorCode.UNAUTHENTICATED_MEMBER;
            //검증
            sendRequest(randomTargetPostSeq, null, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 존재하지 않는 Post Seq로 접근했을 때 오류")
        public void commentsSaveWithUnknownPostSeqTest() throws Exception {
            Long unknownSeq = Long.MAX_VALUE;

            ErrorCode code = ErrorCode.POST_NOT_FOUND;
            Long unknownPostSeq = Long.MAX_VALUE;
            //검증
            sendRequest(unknownPostSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- content가 null일 경우 오류")
        public void commentsSaveWithNullContentTest() throws Exception {
            ErrorCode code = ErrorCode.INVALID_INPUT_VALUE;

            request.setContent(null);
            //검증
            sendRequest(randomTargetPostSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is("content를 입력해주세요")));
        }

        @Test
        @DisplayName("- content가 blank로 들어왔을 때 오류")
        public void commentsSaveWithBlankContentTest() throws Exception {
            ErrorCode code = ErrorCode.INVALID_INPUT_VALUE;


            request.setContent(" ");
            //검증
            sendRequest(randomTargetPostSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is("content를 입력해주세요")));
        }

        @Test
        @DisplayName("- content가 200자를 넘겼을 때 오류")
        public void commentsSaveWithContentOverlengthTest() throws Exception {
            ErrorCode code = ErrorCode.INVALID_INPUT_VALUE;


            request.setContent(StringUtils.randomSting(205));
            //검증
            sendRequest(randomTargetPostSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is("최대 200자까지 입력할 수 있습니다.")));
        }

        @Test
        @DisplayName("- 존재하지 않는 사용자가 댓글을 달려했을 때 오류")
        public void commentsSaveWithUnsavedUserTest() throws Exception {
            //존재하지 않는 사용자로 로그인
            token = memberLogin(Long.MAX_VALUE).getAccessToken();
            ErrorCode code = ErrorCode.MEMBER_NOT_FOUND;

            //검증
            sendRequest(randomTargetPostSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }
    }

    /**
     * - 댓글 정상 조회
     * - 삭제된 댓글은 조회하지 않기
     * - 삭제된 유저들의 댓글은 조회하지 않기
     * - 없는 페이지 조회 - 빈 list return
     * - 존재하지 않는 Post Seq로 접근했을 때 오류
     */
    @Nested
    @DisplayName("Comment List Test")
    public class CommentListTest {
        //댓글을 조회하는 사람의 seq
        Long randomMemberSeq;
        //accessToken
        String token;

        //댓글을 조회할 post의 주인 seq
        Long randomTargetMemberSeq;
        //댓글을 조회할 포스트의 seq
        Long randomTargetPostSeq;

        //해당 게시글의 댓글들
        List<CommentEntity> comments;

        final int DEFAULT_SIZE = 50;
        final int DEFAULT_PAGE = 0;

        /**
         * 인자를 받아 조회 요청을 보내주는 메서드
         */
        public ResultActions sendRequest(Long postSeq, String token, int page, int size) throws Exception {
            MockHttpServletRequestBuilder authorization = get(BASE_URL.concat("/posts/{postSeq}/comments"), postSeq)
                    .param("page", Integer.toString(page))
                    .param("size", Integer.toString(size))
                    .contentType(CONTENT_TYPE)
                    .characterEncoding(CHARSET);
            if (!StringUtils.isBlank(token)) {
                authorization.header("Authorization", "Bearer ".concat(token));
            }
            return mvc.perform(authorization);
        }

        @BeforeEach
        public void listInit() throws Exception {
            Random random = new Random();
            //댓글을 조회하는 사람
            randomMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();
            //accessToken
            token = memberLogin(randomMemberSeq).getAccessToken();
            //댓글을 조회할 게시글의 주인
            randomTargetMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();

            //댓글을 조회할 게시글
            List<PostEntity> posts = memberPosts.get(randomTargetMemberSeq);
            randomTargetPostSeq = posts.get(Math.abs(random.nextInt()) % posts.size()).getSeq();

            //해당 게시글에 조금 더 댓글을 추가
            for (int i = 0; i < 100; i++) {
                CommentEntity comment = CommentEntity.builder()
                        .member(memberList.get(i % memberList.size()))
                        .post(PostEntity.builder()
                                .seq(randomTargetPostSeq)
                                .build())
                        .content("additional comment".concat(Integer.toString(i)))
                        .build();
                Thread.sleep(10);
                commentRepository.save(comment);//약간의 시간차를 주기위해 따로 저장
            }

            comments = commentRepository.findAllByPost(PostEntity.builder()
                    .seq(randomTargetPostSeq)
                    .build());
        }

        //최신순으로 조회
        @Test
        @DisplayName("- 댓글 정상 조회")
        public void getCommentsListSuccessTest() throws Exception {
            //검증용 comments를 생성날짜 기준 내림차순(최신순)으로 정렬
            Collections.sort(comments, new Comparator<CommentEntity>() {
                @Override
                public int compare(CommentEntity o1, CommentEntity o2) {
                    LocalDateTime createdAt1 = o1.getCreatedAt();
                    LocalDateTime createdAt2 = o2.getCreatedAt();
                    return createdAt2.compareTo(createdAt1);
                }
            });
            //기댓값 구해오기
            List<Comment> expectedComments = comments.stream()
                    .map(entity -> {
                        boolean isLike = entity.getLikes().stream()
                                .filter(e -> e.getMember().getSeq() == randomMemberSeq)
                                .findAny()
                                .isPresent();
                        return Comment.builder()
                                .nickname(entity.getMember().getNickname())
                                .content(entity.getContent())
                                .memberSeq(entity.getMember().getSeq())
                                .profileImg(entity.getMember().getProfileUrl())
                                .createdAt(entity.getCreatedAt().withNano(0))
                                .updatedAt(entity.getUpdatedAt().withNano(0))
                                .likeCnt(entity.getLikes().size())
                                .liked(isLike)
                                .build();
                    })
                    .collect(Collectors.toList());

            for (int page = 0; page < comments.size() / DEFAULT_SIZE; page++) {
                MvcResult result = sendRequest(randomTargetPostSeq, token, page, DEFAULT_SIZE)
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();

                String response = result.getResponse().getContentAsString();
                CommentListDTO.Response list = mapper.readValue(response, CommentListDTO.Response.class);

                boolean expectedPrev = page != 0;//첫번째 페이지가 아니면 prev는 true, 아니면 false
                boolean expectedNext = page != (comments.size() / DEFAULT_SIZE);//마지막 페이지가 아니면 next는 true, 아니면 false
                int expectedPage = page;//조회한 페이지

                List<Comment> expects = expectedComments
                        .subList(page*DEFAULT_SIZE, Math.min(page*DEFAULT_SIZE + DEFAULT_SIZE, comments.size()));

                assertAll("comment list",
                        () -> assertThat(list.isPrev()).isEqualTo(expectedPrev),
                        () -> assertThat(list.isNext()).isEqualTo(expectedNext),
                        () -> assertThat(list.getPage()).isEqualTo(expectedPage),
                        () -> {
                            for(int i=0;i<expects.size();i++){
                                Comment comment = list.getComments().get(i);
                                Comment expected = expects.get(i);
                                assertThat(comment.getContent()).isEqualTo(expected.getContent());
                            }
                        }
                );
            }
        }

        @Test
        @DisplayName("- 없는 페이지 조회 - 빈 list return")
        public void getCommentsListWithEmptyTest() throws Exception {
            int page = 987654321;

            sendRequest(randomTargetPostSeq,token,page,DEFAULT_SIZE)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.prev",is(true)))
                    .andExpect(jsonPath("$.next",is(false)))
                    .andExpect(jsonPath("$.page",is(page)))
                    .andExpect(jsonPath("$.comments.length()",is(0)));
        }

        @Test
        @DisplayName("- 존재하지 않는 Post Seq로 접근했을 때 오류")
        public void commentsSaveWithUnknownPostSeqTest() throws Exception {
            int page = 0;
            randomTargetPostSeq = Long.MAX_VALUE;
            ErrorCode code = ErrorCode.POST_NOT_FOUND;

            sendRequest(randomTargetPostSeq,token,page,DEFAULT_SIZE)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code",is(code.getCode())))
                    .andExpect(jsonPath("$.message",is(code.getMessage())));
        }
    }

    /**
     * - 댓글 정상 수정
     * - 타인의 댓글 수정 오류
     * - 인증되지 않은 사용자의 댓글 수정 오류
     * - 존재하지 않는 포스트의 댓글 수정 오류
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
    public class CommentUpdateTest {
        //댓글을 단 사람의 seq
        Long randomMemberSeq;
        //댓글이 달리는 post의 주인 seq
        Long randomTargetMemberSeq;
        //댓글이 달리는 포스트의 seq
        Long randomTargetPostSeq;
        //수정할 comment의 seq
        Long randomTargetCommentSeq;
        //유저의 accessToken
        String token;

        CommentUpdateDTO.Request request;

        /**
         * 인자를 받아 수정 요청을 보내주는 메서드
         */
        public ResultActions sendRequest(Long postSeq, Long commentSeq, String token, CommentUpdateDTO.Request content) throws Exception {
            MockHttpServletRequestBuilder authorization = put(BASE_URL.concat("/posts/{postSeq}/comments/{commentSeq}"), postSeq, commentSeq)
                    .contentType(CONTENT_TYPE)
                    .characterEncoding(CHARSET);

            if (!StringUtils.isBlank(token)) {
                authorization.header("Authorization", "Bearer ".concat(token));
            }
            if (content != null) {
                authorization.content(mapper.writeValueAsString(content));
            }
            return mvc.perform(authorization);
        }

        @BeforeEach
        public void updateInit() throws Exception {
            Random random = new Random();
            //댓글을 다는 사람
            randomMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();
            //accessToken
            token = memberLogin(randomMemberSeq).getAccessToken();
            //댓글이 달리는 게시글의 주인
            randomTargetMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();

            //댓글을 달 게시판
            List<PostEntity> posts = memberPosts.get(randomTargetMemberSeq);
            randomTargetPostSeq = posts.get(Math.abs(random.nextInt()) % posts.size()).getSeq();

            //수정할 댓글의 seq
            randomTargetCommentSeq = postComments.get(randomTargetPostSeq).stream()
                    .filter(comment -> comment.getMember().getSeq() == randomMemberSeq)
                    .findAny()
                    .get()
                    .getSeq();

            //등록할 comment
            request = CommentUpdateDTO.Request.builder()
                    .content("forUpdate!!!!!")
                    .build();
        }

        @Test
        @DisplayName("- 댓글 정상 수정")
        public void commentsUpdateSuccessTest() throws Exception {
            //댓글 수정 요청 및 결과
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)));

            commentRepository.flush();
            Optional<CommentEntity> result = commentRepository.findById(randomTargetCommentSeq);
            assertThat(result).isNotEmpty();
            CommentEntity updated = result.get();
            assertAll("assert update",
                    () -> assertThat(updated.getSeq()).isEqualTo(randomTargetCommentSeq),
                    () -> assertThat(updated.getContent()).isEqualTo(request.getContent()),
                    () -> assertThat(updated.getUpdatedAt()).isNotEqualTo(updated.getCreatedAt()));
        }

        @Test
        @DisplayName("- 타인의 댓글 수정 오류")
        public void updateOthersCommentTest() throws Exception {
            //타인의 comment seq
            Long otherCommentSeq = postComments.get(randomTargetPostSeq).stream()
                    .filter(comment -> comment.getMember().getSeq() != randomMemberSeq)
                    .findAny()
                    .get()
                    .getSeq();

            //기대되는 error code
            ErrorCode errorCode = ErrorCode.HANDLE_ACCESS_DENIED;

            //send
            sendRequest(randomTargetPostSeq, otherCommentSeq, token, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is(errorCode.getMessage())));
        }

        @Test
        @DisplayName("- 존재하지 않는 포스트의 댓글 수정 오류")
        public void commentsUpdateWithNonexistentPostTest() throws Exception {
            //없는 post번호
            Long unknownPostSeq = Long.MAX_VALUE;

            //기대되는 error code
            ErrorCode errorCode = ErrorCode.POST_NOT_FOUND;

            //send
            sendRequest(unknownPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is(errorCode.getMessage())));
        }

        @Test
        @DisplayName("- 삭제된 포스트의 댓글 수정 오류")
        public void commentsUpdateWithDeletedPostTest() throws Exception {
            //포스트를 미리 삭제
            postRepository.deleteById(randomTargetPostSeq);

            //기대되는 error code
            ErrorCode errorCode = ErrorCode.POST_NOT_FOUND;

            //send
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is(errorCode.getMessage())));
        }

        @Test
        @DisplayName("- 인증되지 않은 사용자의 댓글 수정 오류")
        public void commentUpdateWithUnauthenticatedUserTest() throws Exception {
            //error code
            ErrorCode errorCode = ErrorCode.UNAUTHENTICATED_MEMBER;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, null, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is(errorCode.getMessage())));
        }

        @Test
        @DisplayName("- 존재하지 않는 댓글 수정 오류")
        public void commentsUpdateWithNonexistentTest() throws Exception {
            //존재하지 않는 댓글 seq
            Long nonexistCommentSeq = Long.MAX_VALUE;
            //error code
            ErrorCode errorCode = ErrorCode.COMMENT_NOT_FOUND;

            sendRequest(randomTargetPostSeq, nonexistCommentSeq, token, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is(errorCode.getMessage())));
        }

        @Test
        @DisplayName("- 삭제된 댓글 수정 오류")
        public void commentsUpdateWithDeletedTest() throws Exception {
            //댓글 삭제
            commentRepository.deleteById(randomTargetCommentSeq);

            //error code
            ErrorCode errorCode = ErrorCode.COMMENT_NOT_FOUND;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is(errorCode.getMessage())));
        }

        @Test
        @DisplayName("- content가 null일 경우 오류")
        public void commentsUpdateWithNullContentTest() throws Exception {
            //content를 null
            request.setContent(null);

            //error code
            ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is("content를 입력해주세요")));
        }

        @Test
        @DisplayName("- content가 blank일 경우 오류")
        public void commentsUpdateWithBlankContentTest() throws Exception {
            //content를 blank로
            request.setContent("     ");

            //error code
            ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is("content를 입력해주세요")));
        }

        @Test
        @DisplayName("- content가 200자를 넘겼을 경우 오류")
        public void commentsUpdateWithOverlengthContentTest() throws Exception {
            //content를 200자 초과로
            request.setContent(StringUtils.randomSting(1000));

            //error code
            ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is("최대 200자까지 입력할 수 있습니다.")));
        }

        @Test
        @DisplayName("- 삭제된 사용자의 댓글 수정 오류")
        public void commentsUpdateWithDeletedUserTest() throws Exception {
            //member를 삭제
            memberRepository.deleteById(randomMemberSeq);

            //error code
            ErrorCode errorCode = ErrorCode.MEMBER_NOT_FOUND;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(errorCode.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(errorCode.getCode())))
                    .andExpect(jsonPath("$.message", is(errorCode.getMessage())));
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
    public class CommentDeleteTest {
        //댓글을 단 사람의 seq
        Long randomMemberSeq;
        //댓글이 달리는 post의 주인 seq
        Long randomTargetMemberSeq;
        //댓글이 달리는 포스트의 seq
        Long randomTargetPostSeq;
        //삭제할 comment의 seq
        Long randomTargetCommentSeq;
        //유저의 accessToken
        String token;

        CommentDeleteDTO.Request request;

        /**
         * 인자를 받아 삭제 요청을 보내주는 메서드
         */
        public ResultActions sendRequest(Long postSeq, Long commentSeq, String token, CommentDeleteDTO.Request content) throws Exception {
            MockHttpServletRequestBuilder authorization = delete(BASE_URL.concat("/posts/{postSeq}/comments/{commentSeq}"), postSeq, commentSeq)
                    .contentType(CONTENT_TYPE)
                    .characterEncoding(CHARSET);

            if (!StringUtils.isBlank(token)) {
                authorization.header("Authorization", "Bearer ".concat(token));
            }
            if (content != null) {
                authorization.content(mapper.writeValueAsString(content));
            }
            return mvc.perform(authorization);
        }

        @BeforeEach
        public void deleteInit() throws Exception {
            Random random = new Random();
            //댓글을 단 사람
            randomMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();
            //accessToken
            token = memberLogin(randomMemberSeq).getAccessToken();
            //댓글이 달리는 게시글의 주인
            randomTargetMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();

            //댓글을 삭제할 게시판
            List<PostEntity> posts = memberPosts.get(randomTargetMemberSeq);
            randomTargetPostSeq = posts.get(Math.abs(random.nextInt()) % posts.size()).getSeq();

            //삭제할 댓글의 seq
            randomTargetCommentSeq = postComments.get(randomTargetPostSeq).stream()
                    .filter(comment -> comment.getMember().getSeq() == randomMemberSeq)
                    .findAny()
                    .get()
                    .getSeq();
        }

        @Test
        @DisplayName("- 댓글 정상 삭제")
        public void commentsDeleteSuccessTest() throws Exception {
            //삭제
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, null)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)));

            //db에 잘 삭제됐는지 확인
            assertThat(commentRepository.findById(randomTargetCommentSeq)).isEmpty();
            //다른 post의 댓글들은 잘 살아있는지 확인
            List<CommentEntity> allByPost = commentRepository.findAllByPost(PostEntity.builder()
                    .seq(randomTargetPostSeq)
                    .build());
            assertThat(allByPost.size()).isEqualTo(postComments.get(randomTargetPostSeq).size() - 1);
        }

        @Test
        @DisplayName("- 인증되지 않은 사용자의 댓글 삭제 오류")
        public void commentsDeletedByUnauthenticatedUserTest() throws Exception {
            //error code
            ErrorCode code = ErrorCode.UNAUTHENTICATED_MEMBER;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, null, null)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 타인의 댓글을 삭제 오류")
        public void commentsDeletedByOtherUserTest() throws Exception {
            //다른 사람의 comment로 변경
            randomTargetCommentSeq = postComments.get(randomTargetPostSeq).stream()
                    .filter(comment -> comment.getMember().getSeq() != randomMemberSeq)
                    .findAny()
                    .get()
                    .getSeq();

            //error code
            ErrorCode code = ErrorCode.HANDLE_ACCESS_DENIED;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, null)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 존재하지 않는 포스트의 댓글 삭제 오류")
        public void commentsDeleteWithNonexistentPostSeqTest() throws Exception {
            //존재하지 않는 post의 seq로 셋팅
            randomTargetPostSeq = Long.MAX_VALUE;

            //error code
            ErrorCode code = ErrorCode.POST_NOT_FOUND;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, null)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 삭제된 포스트의 댓글 삭제 오류")
        public void commentsDeleteWithDeletedPostSeqTest() throws Exception {
            //일단 post 삭제
            postRepository.deleteById(randomTargetPostSeq);

            //error code
            ErrorCode code = ErrorCode.POST_NOT_FOUND;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, null)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 존재하지 않는 댓글 삭제 오류")
        public void nonexistentCommentDeleteTest() throws Exception {
            //없는 comment로 변경
            randomTargetCommentSeq = Long.MAX_VALUE;

            //error code
            ErrorCode code = ErrorCode.COMMENT_NOT_FOUND;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, null)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 삭제된 댓글 삭제 오류")
        public void commentsDeleteWithDeletedCommentTest() throws Exception {
            //삭제시킴
            commentRepository.deleteById(randomTargetCommentSeq);

            //error code
            ErrorCode code = ErrorCode.COMMENT_NOT_FOUND;

            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, null)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
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
    public class CommentLikeTest {
        //댓글을 조회하는 사람의 seq
        Long randomMemberSeq;
        //accessToken
        String token;

        //댓글이 달리는 post의 주인 seq
        Long randomTargetMemberSeq;
        //댓글이 달리는 포스트의 seq
        Long randomTargetPostSeq;

        //좋아요 한 댓글들
        List<CommentEntity> likes;
        //좋아요 하지않은 댓글들
        List<CommentEntity> unlikes;

        /**
         * 인자를 받아 등록 요청을 보내주는 메서드
         */
        public ResultActions sendRequest(Long postSeq, Long commentSeq, String token, CommentLikeDTO.Request content) throws Exception {
            MockHttpServletRequestBuilder authorization = post(BASE_URL.concat("/posts/{postSeq}/comments/{commentSeq}/like"), postSeq, commentSeq)
                    .contentType(CONTENT_TYPE)
                    .characterEncoding(CHARSET);
            if (!StringUtils.isBlank(token)) {
                authorization.header("Authorization", "Bearer ".concat(token));
            }
            if (content != null) {
                authorization.content(mapper.writeValueAsString(content));
            }
            return mvc.perform(authorization);
        }

        @BeforeEach
        public void likeInit() throws Exception {
            Random random = new Random();
            //댓글을 다는 사람
            randomMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();
            //accessToken
            token = memberLogin(randomMemberSeq).getAccessToken();
            //댓글이 달리는 게시글의 주인
            randomTargetMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();

            //댓글을 조회할 게시글
            List<PostEntity> posts = memberPosts.get(randomTargetMemberSeq);
            randomTargetPostSeq = posts.get(Math.abs(random.nextInt()) % posts.size()).getSeq();

            //좋아요 셋팅
            likes = new ArrayList<>();
            unlikes = new ArrayList<>();

            Collections.shuffle(postComments.get(randomTargetPostSeq));
            List<CommentEntity> comments = postComments.get(randomTargetPostSeq);
            for (int i = 0; i < comments.size(); i++) {
                CommentEntity comment = comments.get(i);
                if (i < comments.size() / 2) {
                    CommentLikeEntity like = CommentLikeEntity.builder()
                            .comment(comment)
                            .member(MemberEntity.builder()
                                    .seq(randomMemberSeq)
                                    .build())
                            .build();

                    commentLikeRepository.save(like);
                    likes.add(comment);
                } else {
                    unlikes.add(comment);
                }
            }
        }

        @Test
        @DisplayName("- 좋아요")
        public void commentLikeTest() throws Exception {
            //좋아요 할 좋아요 상태가 아닌 댓글의 seq
            Long targetCommentSeq = unlikes.get(0)
                    .getSeq();

            //검증
            sendRequest(randomTargetPostSeq, targetCommentSeq, token, null)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.like", is(true)));

            //db에 저장됐는지 확인
            assertThat(commentLikeRepository.findById(CommentLikeKey.builder()
                    .comment(targetCommentSeq)
                    .member(randomMemberSeq)
                    .build())).isNotEmpty();
        }

        @Test
        @DisplayName("- 좋아요 취소")
        public void commentUnlikeTest() throws Exception {
            //좋아요 취소 할 좋아요 상태인 댓글의 seq
            Long targetCommentSeq = likes.get(0)
                    .getSeq();

            //검증
            sendRequest(randomTargetPostSeq, targetCommentSeq, token, null)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.like", is(false)));

            //db에 삭제됐는지 확인
            assertThat(commentLikeRepository.findById(CommentLikeKey.builder()
                    .comment(targetCommentSeq)
                    .member(randomMemberSeq)
                    .build())).isEmpty();
        }

        @Test
        @DisplayName("- 삭제된 게시글의 댓글에 좋아요 오류")
        public void commentLikeWithDeletedCommentTest() throws Exception {
            //해당 게시글 삭제
            postRepository.deleteById(randomTargetPostSeq);

            //좋아요 할 좋아요 상태가 아닌 댓글의 seq
            Long targetCommentSeq = unlikes.get(0)
                    .getSeq();

            //Error Code
            ErrorCode code = ErrorCode.POST_NOT_FOUND;

            //검증
            sendRequest(randomTargetPostSeq, targetCommentSeq, token, null)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 존재하지 않는 댓글에 좋아요 오류")
        public void commentLikeWithNonexistentCommentTest() throws Exception {
            //없는 댓글의 seq
            Long targetCommentSeq = Long.MAX_VALUE;

            //Error Code
            ErrorCode code = ErrorCode.COMMENT_NOT_FOUND;

            //검증
            sendRequest(randomTargetPostSeq, targetCommentSeq, token, null)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 인증되지 않은 사용자 좋아요 요청 오류")
        public void commentLikeWithUnauthenticatedUserTest() throws Exception {
            //좋아요 할 좋아요 상태가 아닌 댓글의 seq
            Long targetCommentSeq = unlikes.get(0)
                    .getSeq();

            //Error Code
            ErrorCode code = ErrorCode.UNAUTHENTICATED_MEMBER;

            //검증
            sendRequest(randomTargetPostSeq, targetCommentSeq, null, null)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }
    }

    /**
     * - 신고 성공
     * - 신고 내용은 null가능
     * - 신고 내용은 blank가능
     * - 중복 신고 오류
     * - 존재하지 않는 게시글의 댓글 신고 오류
     * - 삭제된 게시글의 댓글 신고 오류
     * - 존재하지 않는 댓글 신고 오류
     * - 삭제된 댓글 신고 오류
     * - 카테고리 미설정 오류
     */
    @Nested
    @DisplayName("Comment Report Test")
    public class CommentReportTest {
        //댓글을 신고할 사람의 seq
        Long randomMemberSeq;
        //accessToken
        String token;

        //댓글이 달리는 post의 주인 seq
        Long randomTargetMemberSeq;
        //댓글이 달리는 포스트의 seq
        Long randomTargetPostSeq;
        //신고할 댓글의 seq
        Long randomTargetCommentSeq;

        //request
        CommentReportDTO.Request request;

        /**
         * 인자를 받아 등록 요청을 보내주는 메서드
         */
        public ResultActions sendRequest(Long postSeq, Long commentSeq, String token, CommentReportDTO.Request content) throws Exception {
            MockHttpServletRequestBuilder authorization = post(BASE_URL.concat("/posts/{postSeq}/comments/{commentSeq}/report"), postSeq, commentSeq)
                    .contentType(CONTENT_TYPE)
                    .characterEncoding(CHARSET);
            if (!StringUtils.isBlank(token)) {
                authorization.header("Authorization", "Bearer ".concat(token));
            }
            if (content != null) {
                authorization.content(mapper.writeValueAsString(content));
            }
            return mvc.perform(authorization);
        }

        @BeforeEach
        public void reportInit() throws Exception {
            Random random = new Random();
            //댓글을 다는 사람
            randomMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();
            //accessToken
            token = memberLogin(randomMemberSeq).getAccessToken();
            //댓글이 달리는 게시글의 주인
            randomTargetMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();

            //댓글을 조회할 게시글
            List<PostEntity> posts = memberPosts.get(randomTargetMemberSeq);
            randomTargetPostSeq = posts.get(Math.abs(random.nextInt()) % posts.size()).getSeq();

            //신고할 댓글 하나를 고름
            randomTargetCommentSeq = postComments.get(randomTargetPostSeq).stream()
                    .findAny()
                    .get()
                    .getSeq();

            request = CommentReportDTO.Request.builder()
                    .reportCategory("report category")
                    .reportContent("report content")
                    .build();
        }

        @Test
        @DisplayName("- 신고 성공")
        public void commentReportSuccessTest() throws Exception {
            //검증
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)));

            //db검증
            assertThat(commentReportRepository.findById(CommentReportKey.builder()
                    .comment(randomTargetCommentSeq)
                    .member(randomMemberSeq)
                    .build()))
                    .isNotEmpty();
        }

        @Test
        @DisplayName("- 신고 내용은 null가능")
        public void commentReportWithNullContentSuccessTest() throws Exception {
            //신고 내용 null셋팅
            request.setReportContent(null);

            //검증
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)));

            //db검증
            assertThat(commentReportRepository.findById(CommentReportKey.builder()
                    .comment(randomTargetCommentSeq)
                    .member(randomMemberSeq)
                    .build()))
                    .isNotEmpty();
        }

        @Test
        @DisplayName("- 신고 내용은 blank가능")
        public void commentReportWithBlankContentSuccessTest() throws Exception {
            //신고 내용 null셋팅
            request.setReportContent("                    ");

            //검증
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)));

            //db검증
            assertThat(commentReportRepository.findById(CommentReportKey.builder()
                    .comment(randomTargetCommentSeq)
                    .member(randomMemberSeq)
                    .build()))
                    .isNotEmpty();
        }

        @Test
        @DisplayName("- 중복 신고 오류")
        public void commentReportDuplicatedTest() throws Exception {
            //검증, 첫번째 신고는 성공
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)));

            //두번째 신고는 실패
            ErrorCode code = ErrorCode.COMMENT_REPORT_ALREADY_EXIST;
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 존재하지 않는 게시글의 댓글 신고 오류")
        public void commentReportWithNonexistentPostTest() throws Exception {
            //존재하지 않는 게시글의 seq
            randomTargetPostSeq = Long.MAX_VALUE;

            //검증
            ErrorCode code = ErrorCode.POST_NOT_FOUND;
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 삭제된 게시글의 댓글 신고 오류")
        public void commentReportWithDeletedPostTest() throws Exception {
            //삭제된 게시글의 seq
            postRepository.deleteById(randomTargetPostSeq);

            //검증
            ErrorCode code = ErrorCode.POST_NOT_FOUND;
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 존재하지 않는 댓글 신고 오류")
        public void commentReportWithDeletedCommentTest() throws Exception {
            //존재하지 않는 댓글의 seq
            randomTargetCommentSeq = Long.MAX_VALUE;

            //검증
            ErrorCode code = ErrorCode.COMMENT_NOT_FOUND;
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 삭제된 댓글 신고 오류")
        public void commentReportWith() throws Exception {
            //삭제된 게시글의 seq
            commentRepository.deleteById(randomTargetCommentSeq);

            //검증
            ErrorCode code = ErrorCode.COMMENT_NOT_FOUND;
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }

        @Test
        @DisplayName("- 카테고리 미설정 오류")
        public void commentReportWithoutCategory() throws Exception {
            //카테고리 null설정
            request.setReportCategory(null);

            //검증
            ErrorCode code = ErrorCode.MISSING_INPUT_VALUE;
            sendRequest(randomTargetPostSeq, randomTargetCommentSeq, token, request)
                    .andExpect(status().is(code.getStatus().value()))
                    .andExpect(jsonPath("$.code", is(code.getCode())))
                    .andExpect(jsonPath("$.message", is(code.getMessage())));
        }
    }
}