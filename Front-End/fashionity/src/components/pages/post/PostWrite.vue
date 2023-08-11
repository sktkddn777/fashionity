<template lang="">
  <div id="post-detail" class="container-fluid">
    <div class="row">
      <div class="col"></div>
      <div class="col-6">
        <!-- 작성자 정보 -->
        <div class="post-detail-header">
          <div class="post-detail-header-img">
            <img
              src="../../../assets/img/hyeonwook.jpg"
              alt=""
              class="profile"
            />
          </div>
          <div class="post-detail-header-info">
            <div class="post-detail-header-info-nickname fw-bold">
              유저 닉네임
            </div>
          </div>
        </div>
        <!--이미지 업로드-->
        <multi-image-upload></multi-image-upload>
        <br />
        <textarea
          class="form-control"
          rows="1"
          placeholder="해시태그를 입력해주세요."
        ></textarea>
        <br />
        <textarea
          class="form-control"
          rows="3"
          placeholder="내용을 입력해주세요."
        ></textarea>
        <div class="post-write-button">
          <button
            type="button"
            class="btn btn-outline-dark"
            style="min-width: 70px"
          >
            <span style="font-size: smaller">&nbsp;취소&nbsp;</span>
          </button>
          <button type="button" class="btn btn-dark" style="min-width: 70px">
            <span style="font-size: smaller">&nbsp;등록&nbsp;</span>
          </button>
        </div>
      </div>
      <div class="col"></div>
    </div>
  </div>
</template>
<script>
import MultiImageUpload from "../shared/MultiImageUpload.vue";
export default {
  data() {
    return {};
  },
  components: {
    MultiImageUpload,
  },
};
</script>
<style scoped>
.profile {
  height: 7vh;
  border-radius: 100%;
  object-fit: contain;
}
.profile-comment {
  height: 5vh;
  border-radius: 100%;
  object-fit: contain;
}
.post-detail-header {
  margin: auto;
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}
.post-detail-like {
  display: flex;
  gap: 5px;
  margin-top: 10px;
  margin-bottom: 10px;
}
.post-detail-content {
  margin-bottom: 10px;
}
.post-detail-content-hashtag {
  display: flex;
}
.post-detail-comment-cnt {
  color: grey;
  text-align: left;
  margin-bottom: 10px;
}
.post-detail-comment-content {
  display: flex;
}
.post-detail-comment-info {
  margin-left: auto;
  display: flex;
  gap: 10px;
}
.post-detail-comment-submit {
  display: flex;
}
.post-write-button {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}
</style>
