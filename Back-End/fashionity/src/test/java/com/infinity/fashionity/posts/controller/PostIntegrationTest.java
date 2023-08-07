package com.infinity.fashionity.posts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.comments.entity.CommentLikeEntity;
import com.infinity.fashionity.comments.repository.CommentLikeRepository;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.consultants.entity.ImageEntity;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.image.repository.ImageRepository;
import com.infinity.fashionity.image.service.ImageService;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.dto.PostListDTO;
import com.infinity.fashionity.posts.entity.*;
import com.infinity.fashionity.posts.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class PostIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    private final String BASE_URL = "/api/v1/posts";

    private List<MemberEntity> members = new ArrayList<>();
    private List<PostEntity> postLists = new ArrayList<>();
    private Map<Long,List<PostEntity>> posts = new HashMap<>();
    private Map<Long,List<CommentEntity>> comments = new HashMap<>();
    private Map<Long,List<PostImageEntity>> images = new HashMap<>();
    private Map<Long,List<PostHashtagEntity>> hashtags = new HashMap<>();
    private Map<Long,List<PostLikeEntity>> postLikes = new HashMap<>();
    private Map<Long,List<CommentLikeEntity>> commentLikes = new HashMap<>();

    public int getRandom(int size){
        Random random = new Random();
        return Math.abs(random.nextInt())%size;
    }

    @BeforeEach
    public void insertDummies(){
        //member setting
        IntStream.rangeClosed(1,5).forEach(i->{
            MemberEntity member = MemberEntity.builder()
                    .id("tester".concat(Integer.toString(i)))
                    .password("Abcdefg123!")
                    .nickname("tester".concat(Integer.toString(i)))
                    .sns(false)
                    .email("tester".concat(Integer.toString(i)).concat("@gmail.com"))
                    .build();
            member.getMemberRoles().add(MemberRoleEntity.builder()
                    .member(member)
                    .memberRole(MemberRole.USER)
                    .build());
            members.add(member);
        });
        memberRepository.saveAllAndFlush(members);

        //posts setting
        for(MemberEntity member : members){
            List<PostEntity> postList = new ArrayList<>();
            for(int i=0;i<3;i++){
                //post setting
                PostEntity post = PostEntity.builder()
                        .member(member)
                        .content(member.getNickname().concat("\'s ").concat(Integer.toString(i)).concat(" post"))
                        .build();

                postList.add(post);
                postLists.add(post);
                postRepository.saveAndFlush(post);

                //image setting
                List<PostImageEntity> postImages = new ArrayList<>();
                for(int j=0;j<3;j++){
                    PostImageEntity imageEntity = PostImageEntity.builder()
                            .post(post)
                            .url("image_url"+member.getSeq()*i*j)
                            .name("image_name"+member.getSeq()*i*j)
                            .build();
                    postImages.add(imageEntity);
                }
                postImageRepository.saveAllAndFlush(postImages);
                images.put(post.getSeq(),postImages);
                post.getPostImages().addAll(postImages);

                //hashtags setting
                List<PostHashtagEntity> postHashtags = new ArrayList<>();
                for(int j=0;j<4;j++){
                    PostHashtagEntity hashtagEntity = PostHashtagEntity.builder()
                            .post(post)
                            .hashtag(HashtagEntity.builder()
                                    .name("hashtag"+(member.getSeq()*i*j))
                                    .build())
                            .build();

                    postHashtags.add(hashtagEntity);
                }
                postHashtagRepository.saveAllAndFlush(postHashtags);
                hashtags.put(post.getSeq(),postHashtags);
                post.getPostHashtags().addAll(postHashtags);

                //comments setting
                List<CommentEntity> commentEntities = new ArrayList<>();
                for(int j=0;j<getRandom(members.size());j++){
                    MemberEntity randomMember = members.get(getRandom(members.size()));
                    CommentEntity comment = CommentEntity.builder()
                            .content("commment!")
                            .member(randomMember)
                            .post(post)
                            .build();
                    commentEntities.add(comment);
                    commentRepository.saveAndFlush(comment);

                    List<CommentLikeEntity> cles = new ArrayList<>();
                    for(int k=0;k<getRandom(members.size());k++){
                        CommentLikeEntity cle = CommentLikeEntity.builder()
                                .comment(comment)
                                .member(members.get(k))
                                .build();
                        cles.add(cle);
                    }
                    commentLikeRepository.saveAllAndFlush(cles);
                    commentLikes.put(comment.getSeq(),cles);
                }
                comments.put(post.getSeq(),commentEntities);

                //post like setting
                List<PostLikeEntity> postLikesEntities = new ArrayList<>();
                for(int j=0;j<getRandom(members.size());j++){
                    MemberEntity m = members.get(j);
                    PostLikeEntity pl = PostLikeEntity.builder()
                            .post(post)
                            .member(m)
                            .build();

                    postLikesEntities.add(pl);
                }
                postLikes.put(post.getSeq(),postLikesEntities);
                postLikeRepository.saveAllAndFlush(postLikesEntities);
            }
        }
    }

    @Nested
    @DisplayName("Posts List test")
    public class PostsGetAllTest{
        //조회하는 사람의 seq
        private Long memberSeq;

        PostListDTO.Request dto;

        private ResultActions sendRequest(String token,PostListDTO.Request dto) throws Exception {
            MockHttpServletRequestBuilder authorization = get(BASE_URL)
                    .param("page", Integer.toString(dto.getPage()))
                    .param("size", Integer.toString(dto.getSize()))
                    .param("s", dto.getS());

            if(!StringUtils.isBlank(token)){
                authorization.header("Authorization","Bearer ".concat(token));
            }
            return mvc.perform(authorization);
        }

        @BeforeEach
        public void initMemberSeq(){
            Random random = new Random();
            memberSeq = members.get(Math.abs(random.nextInt())%members.size()).getSeq();

            dto = PostListDTO.Request.builder()
                    .build();
        }

        @Test
        @DisplayName("success with default get without authorization")
        public void successWithDefaultGetWithoutAuthorization() throws Exception {
            MvcResult result = sendRequest(null, dto)
                    .andExpect(status().isOk())
                    .andReturn();


            //expected values
            List<PostListDTO.Post> expected = postLists.stream()
                    .sorted((o1, o2) -> {
                        int l1 = postLikeRepository.findAllByPost(o1).size();
                        int l2 = postLikeRepository.findAllByPost(o2).size();
                        if(l1 == l2){
                            return o2.getCreatedAt().compareTo(o1.getCreatedAt());
                        }
                        else return Integer.compare(l2,l1);
                    })
                    .map(postEntity -> PostListDTO.Post.builder()
                            .postSeq(postEntity.getSeq())
                            .likeCount(postLikes.get(postEntity.getSeq()).size())
                            .profileImg(postEntity.getMember().getProfileUrl())
                            .commentCount(comments.get(postEntity.getSeq()).size())
                            .liked(false)//로그인하지 않은 유저이므로 like는 false
                            .content(postEntity.getContent())
                            .images(images.get(postEntity.getSeq()).stream().map(e -> e.getUrl()).collect(Collectors.toList()))
                            .name(postEntity.getMember().getNickname())
                            .build())
                    .collect(Collectors.toList());

            //actual
            PostListDTO.Response response = mapper.readValue(result.getResponse().getContentAsString(), PostListDTO.Response.class);

            //comment count 해결하기
            for(int i=0;i<response.getPosts().size();i++){
                PostListDTO.Post post = response.getPosts().get(i);
                assertThat(post).isSameAs(expected.get(i));
            }
        }
    }
}