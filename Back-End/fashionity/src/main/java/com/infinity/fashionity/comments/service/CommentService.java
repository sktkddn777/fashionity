package com.infinity.fashionity.comments.service;

import com.infinity.fashionity.comments.dto.*;

public interface CommentService {
    CommentListDTO.Response getList(CommentListDTO.Request dto);
    CommentSaveDTO.Response save(CommentSaveDTO.Request dto);
    CommentUpdateDTO.Response update(CommentUpdateDTO.Request dto);
    CommentReportDTO.Response report(CommentReportDTO.Request dto);

    CommentLikeDTO.Response like(CommentLikeDTO.Request dto);
    CommentDeleteDTO.Response delete(CommentDeleteDTO.Request dto);
}
