package com.infinity.fashionity.posts.repository;

import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.entity.PostReportEntity;
import com.infinity.fashionity.posts.entity.PostReportKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReportEntity, PostReportKey>{
}
