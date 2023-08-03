package com.infinity.fashionity.comments.controller;

import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.comments.entity.CommentLikeEntity;
import com.infinity.fashionity.comments.repository.CommentLikeRepository;
import com.infinity.fashionity.comments.repository.CommentReportRepository;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    /**
     * 해당 게시물의 댓글들, 댓글들의 좋아요 개수 및 자신이 좋아요한 상태를 출력
     * */
    void findAllByPostWithCommentsAndLikeCountTest() throws InterruptedException {
        List<MemberEntity> memberList = new ArrayList<>();
        for(int i=0;i<4;i++){
            MemberEntity member = MemberEntity.builder()
                    .nickname("user"+i)
                    .id("tester"+i)
                    .email("tester"+i+"@gmail.com")
                    .password("password")
                    .sns(false)
                    .build();
            memberList.add(member);
        }
        PostEntity post = PostEntity.builder()
                .member(memberList.get(0))
                .content("1번 게시글")
                .build();
        memberRepository.saveAll(memberList);
        postRepository.save(post);
        List<CommentEntity> commentList = new ArrayList<>();
        for(int i=0;i<3;i++){
            CommentEntity comment = CommentEntity.builder()
                    .member(memberList.get(i))
                    .post(post)
                    .content(i+"번째 댓글")
                    .build();
            commentList.add(comment);
            commentRepository.save(comment);
            Thread.sleep(500);
        }
//        commentRepository.saveAll(commentList);

        //1번과 2번이 1번댓글을 좋아요, 1,3,4번이 2번 댓글 좋아요, 3번이 3번댓글 좋아요를 누름
        List<CommentLikeEntity> likes = new ArrayList<>();
        likes.add(likeComment(memberList, commentList,0,0));
        likes.add(likeComment(memberList, commentList,1,0));
        likes.add(likeComment(memberList, commentList,0,1));
        likes.add(likeComment(memberList, commentList,2,1));
        likes.add(likeComment(memberList, commentList,3,1));
        likes.add(likeComment(memberList, commentList,2,2));

        commentLikeRepository.saveAll(likes);
        
        //댓글을 오름차순 정렬, 입력의 반대로 정렬
        Collections.reverse(commentList);
        //조회하는 사람
        MemberEntity user = memberList.get(0);

        Page<Object[]> createdAt = commentRepository
                .findAllByPostWithCommentsAndLikeCount(post,user, PageRequest.of(0, 10, Sort.by("createdAt").descending()));

        
        List<Object[]> content = createdAt.getContent();
        //기대되는 좋아요 개수
        int[] expectedLikeCount = new int[commentList.size()];
        //기대되는 like 상태
        boolean[] expectedLikeStatus = new boolean[commentList.size()]; 
        for(int i=0;i<commentList.size();i++){
            CommentEntity comment = commentList.get(i);
            //해당 댓글의 좋아요 개수를 가져옴
            expectedLikeCount[i] = likes.stream()
                    .filter(c->c.getComment().getSeq() == comment.getSeq())
                    .collect(Collectors.toList()).size();
            
            //해당 댓글에 user가 좋아요를 누른지 확인
            expectedLikeStatus[i] = likes.stream()
                    .filter(c->c.getComment().getSeq() == comment.getSeq())
                    .filter(c->c.getMember().getSeq()== user.getSeq())
                    .findAny().isPresent();
        }
        
        for(int i=0;i<content.size();i++){
            Object[] obj = content.get(i);
            System.out.println(Arrays.toString(obj));
            CommentEntity comment = (CommentEntity) obj[0];
            int likesCount = ((Long)obj[1]).intValue();
            boolean liked = ((Boolean)obj[2]).booleanValue();

            assertThat(comment.getSeq()).isEqualTo(commentList.get(i).getSeq());
            assertThat(likesCount).isEqualTo(expectedLikeCount[i]);
            assertThat(liked).isEqualTo(expectedLikeStatus[i]);
        }
    }

    private static CommentLikeEntity likeComment(List<MemberEntity> memberList, List<CommentEntity> commentList,int memberIndex,int commentIndex) {
        CommentLikeEntity like = CommentLikeEntity.builder()
                .member(memberList.get(memberIndex))
                .comment(commentList.get(commentIndex))
                .build();
        return like;
    }
}
