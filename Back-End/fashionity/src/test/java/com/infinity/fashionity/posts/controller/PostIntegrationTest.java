package com.infinity.fashionity.posts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.comments.repository.CommentLikeRepository;
import com.infinity.fashionity.comments.repository.CommentReportRepository;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.auth.dto.LoginDTO;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.dto.PostListDTO;
import com.infinity.fashionity.posts.dto.PostReportDTO;
import com.infinity.fashionity.posts.dto.PostSaveDTO;
import com.infinity.fashionity.posts.entity.HashtagEntity;
import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.entity.PostHashtagEntity;
import com.infinity.fashionity.posts.entity.PostImageEntity;
import com.infinity.fashionity.posts.repository.PostImageRepository;
import com.infinity.fashionity.posts.repository.PostRepository;
import com.infinity.fashionity.security.service.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class PostIntegrationTest {

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
    private PostImageRepository postImageRepository;

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
    public Map<Long, List<PostImageEntity>> postImages;

    @BeforeEach
    public void dummyInsert() {
        memberList = new ArrayList<>();
        memberPosts = new HashMap<>();
        postComments = new HashMap<>();
        postImages = new HashMap<>();

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
        // 각 포스트마다 이미지 등록
        for(PostEntity post : postList) {
            List<PostImageEntity> imageList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                PostImageEntity image = PostImageEntity.builder()
                        .post(post)
                        .url("imageURL".concat(Integer.toString(i)))
                        .build();
                imageList.add(image);
            }
            postImages.put(post.getSeq(), imageList);
        }
        }
    }

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
     * 게시글 등록
     * (수많은 예외들... 짜야함)
     */

    @Nested
    @DisplayName("Post Save Test")
    public class PostSaveTest{
        // 게시글 작성자
        Long randomMemberSeq;
        // accessToken
        String token;

        PostSaveDTO.Request request;

        /**
         * 인자를 받아 등록 요청을 보내주는 메서드
         */
        public ResultActions sendRequest(String token, PostSaveDTO.Request content) throws Exception{
            MockHttpServletRequestBuilder authorization = post(BASE_URL.concat("/posts"))
                    .contentType(CONTENT_TYPE)
                    .characterEncoding(CHARSET);
            if(!StringUtils.isBlank(token)){
                authorization.header("Authorization", "Bearer ".concat(token));
            }
            if(content != null){
                authorization.content(mapper.writeValueAsString(content));
            }
            return mvc.perform(authorization);
        }

        @BeforeEach
        public void saveInit() throws Exception{
            Random random = new Random();
            // 게시글 작성자
            randomMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();
            // accessToken
            token = memberLogin(randomMemberSeq).getAccessToken();
            // 등록할 이미지
            ArrayList<String> images = new ArrayList<>();
            for(int i = 0; i < 3; i++){
                images.add("imageUrl".concat(Integer.toString(i)));
            }
            // 등록할 해시태그
            ArrayList<String> hashtags = new ArrayList<>();
            for(int i = 0; i < 3; i++){
                hashtags.add("hashtag".concat(Integer.toString(i)));
            }
            // 등록할 게시글
            request = PostSaveDTO.Request.builder()
                    .content("posts content!!!!!")
                    .memberSeq(randomMemberSeq)
//                    .images(images)
                    .hashtag(hashtags)
                    .build();
        }

        @Test
        @DisplayName("- 게시글 정상 등록")
        public void postsSaveSuccessTest() throws Exception{
            // 검증
            ResultActions resultActions = sendRequest(token, request)
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success", is(true)));

            // 저장된 게시글 가져오기
            List<PostEntity> origin = memberPosts.get(randomMemberSeq);
            Optional<PostEntity> newPost = postRepository.findAllByMember(MemberEntity.builder()
                            .seq(randomMemberSeq)
                            .build())
                    .stream().filter(p -> !origin.contains(p))
                            .findAny();

            assertThat(newPost).isNotEmpty();
            resultActions.andExpect(jsonPath("$.postSeq", is(newPost.get().getSeq().intValue())));
        }
    }

    /**
     * 게시글 전체 조회
     */
//    @Nested
//    @DisplayName("Post List Test")
//    public class PostListTest{
//        // 게시글을 조회하는 사람의 seq
//        Long randomMemberSeq;
//        // accessToken
//        String token;
//        // 게시글 목록
//        List<PostEntity> posts;
//
//        final int DEFAULT_SIZE = 50;
//        final int DEFAULT_PAGE = 0;
//
//        /**
//         * 인자를 받아 조회 요청을 보내주는 메서드
//         */
//        public ResultActions sendRequest(String token, PostListDTO.Request content) throws Exception {
//            MockHttpServletRequestBuilder authorization = get(BASE_URL.concat("/posts/{postSeq}/comments"))
//                    .param("page", Integer.toString(content.getPage()))
//                    .param("size", Integer.toString(content.getSize()))
//                    .contentType(CONTENT_TYPE)
//                    .characterEncoding(CHARSET);
//            if (!StringUtils.isBlank(token)) {
//                authorization.header("Authorization", "Bearer ".concat(token));
//            }
//            return mvc.perform(authorization);
//        }
//
//        @BeforeEach
//        public void listInit() throws Exception{
//            Random random = new Random();
//            // 게시글 조회하는 사람
//            randomMemberSeq = memberList.get(Math.abs(random.nextInt()) % memberList.size()).getSeq();
//            // accessToken
//            token = memberLogin(randomMemberSeq).getAccessToken();
//
//            // 최신순 정렬을 위해 게시글 추가
//            for(int i = 3; i < 10; i++){
//                // 등록할 게시글
//                PostEntity post = PostEntity.builder()
//                        .member(memberList.get(i % memberList.size()))
//                        .content("content".concat(Integer.toString(i)))
//                        .build();
//                PostImageEntity postImageEntity = PostImageEntity.builder()
//                        .post(post)
//                        .url("imageURL".concat(Integer.toString(i)))
//                        .build();
//                postImageRepository.save(postImageEntity);
//
//                Thread.sleep(10);
//                postRepository.save(post);
//            }
//
//
//            // 최신순 조회
//            public void getAllPostsByCreatedAtSuccessTest() {
//                // 검증용 게시글 최신순 정렬
//            Collections.sort(posts, new Comparator<PostEntity>() {
//                @Override
//                public int compare(PostEntity o1, PostEntity o2) {
//                    LocalDateTime createdAt1 = o1.getCreatedAt();
//                    LocalDateTime createdAt2 = o2.getCreatedAt();
//                    return createdAt2.compareTo(createdAt1);
//                }
//            });
//            // 기댓값 구해오기
//            }
//        }
//    }
}
