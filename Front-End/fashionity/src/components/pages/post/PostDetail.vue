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
          <div class="post-detail-header-follow" style="margin-left: auto">
            <div class="align-self-center">
              <button
                type="button"
                class="btn btn-dark"
                style="min-width: 70px"
              >
                <span style="font-size: smaller">&nbsp;팔로우&nbsp;</span>
              </button>
            </div>
          </div>
        </div>
        <!-- 이미지 -->
        <div class="row post-detail-image">
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
        </div>
        <!-- 본문 내용 -->
        <div class="post-detail-like">
          <div class="post-detail-like-icon">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="16"
              height="16"
              fill="currentColor"
              class="bi bi-heart-fill"
              viewBox="0 0 16 16"
              style="color: red"
            >
              <path
                fill-rule="evenodd"
                d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"
              />
            </svg>
          </div>
          <span style="text-align: left">좋아요</span>
          <span class="fw-bold" style="text-align: left">183</span>
          <span style="text-align: left">개</span>
        </div>
        <div class="post-detail-content">
          <div style="text-align: left">아... 하루만 못생겨보고 싶다..</div>
          <div class="post-detail-content-hashtag">
            <a v-for="(tag, i) in hashtags" :key="i" style="color: skyblue">
              {{ tag }} &nbsp;
            </a>
          </div>
        </div>
        <!-- 댓글 -->
        <div>
          <div class="post-detail-comment-cnt">댓글 98개</div>
        </div>
        <div class="post-detail-header">
          <div class="post-detail-header-img">
            <img
              src="../../../assets/img/hyeonwook.jpg"
              alt=""
              class="profile-comment"
            />
          </div>
          <div>
            <div class="fw-bold" style="text-align: left">hyeonwook_12</div>
            <div style="text-align: left; font-size: 15px">
              안녕하세요ㅎ 선팔하고 갑니다ㅎ
            </div>
          </div>
          <div class="post-detail-comment-info">
            <div style="color: grey; font-size: 13px">1시간 전</div>
            <div>
              <div>...</div>
              <div class="post-detail-like-icon">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  fill="currentColor"
                  class="bi bi-heart-fill"
                  viewBox="0 0 16 16"
                  style="color: red"
                >
                  <path
                    fill-rule="evenodd"
                    d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"
                  />
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col"></div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      hashtags: ["#맞팔", "#팔로우", "#강남", "#역삼", "#인생네컷"],
    };
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
}
</style>
