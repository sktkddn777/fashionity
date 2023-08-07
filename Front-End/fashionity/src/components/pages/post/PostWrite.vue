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
              hyeonwook_12
            </div>
            <div
              class="post-detail-header-info-time fs-6"
              style="text-align: left"
            >
              2시간 전
            </div>
          </div>
        </div>
        <!-- 이미지 -->
        <div>
          <v-file-input
            label="File input"
            variant="underlined"
            multiple
          ></v-file-input>
        </div>
        <!-- <div class="row post-detail-image">
          <div>
            <v-file-input
              label="File input"
              variant="underlined"
              multiple
            ></v-file-input>
          </div>
          <div id="carouselExampleIndicators" class="carousel slide">
            <div class="carousel-indicators">
              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="0"
                class="active"
                aria-current="true"
                aria-label="Slide 1"
              ></button>
              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="1"
                aria-label="Slide 2"
              ></button>
              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="2"
                aria-label="Slide 3"
              ></button>
            </div>
            <div class="carousel-inner">
              <div class="carousel-item active">
                <img
                  src="../../../assets/img/hyeonwook.jpg"
                  class="d-block w-100"
                  alt="첫 번째 사진"
                  style="aspect-ratio: 1 / 1"
                />
              </div>
              <div class="carousel-item">
                <img
                  src="../../../assets/img/hyeonwook2.jpg"
                  class="d-block w-100"
                  alt="두 번째 사진"
                  style="aspect-ratio: 1 / 1"
                />
              </div>
              <div class="carousel-item">
                <img
                  src="../../../assets/img/hyeonwook3.jpg"
                  class="d-block w-100"
                  alt="세 번째 사진"
                  style="aspect-ratio: 1 / 1"
                />
              </div>
            </div>
            <button
              class="carousel-control-prev"
              type="button"
              data-bs-target="#carouselExampleIndicators"
              data-bs-slide="prev"
            >
              <span
                class="carousel-control-prev-icon"
                aria-hidden="true"
              ></span>
              <span class="visually-hidden">Previous</span>
            </button>
            <button
              class="carousel-control-next"
              type="button"
              data-bs-target="#carouselExampleIndicators"
              data-bs-slide="next"
            >
              <span
                class="carousel-control-next-icon"
                aria-hidden="true"
              ></span>
              <span class="visually-hidden">Next</span>
            </button>
          </div>
        </div> -->
        <!-- 본문 내용 -->
        <textarea
          class="form-control"
          rows="1"
          placeholder="해시태그를 입력해주세요."
        ></textarea>
        <br />
        <textarea
          class="form-control"
          rows="3"
          placeholder="본문 내용을 입력해주세요."
        ></textarea>
      </div>
      <div class="col"></div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
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
</style>
