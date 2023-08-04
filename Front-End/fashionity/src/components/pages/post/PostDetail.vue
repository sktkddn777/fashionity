<template lang="">
  <div id="post-detail" class="container-fluid">
    <div class="row">
      <div class="col"></div>
      <div class="col-6">
        <!-- 작성자 정보 -->
        <div class="row post-detail-header">
          <div
            class="col post-detail-header-img"
            style="margin: auto; min-width: 70px"
          >
            <img
              src="../../../assets/img/hyeonwook.jpg"
              alt=""
              class="profile"
              style="width: 100%"
            />
          </div>
          <div class="col-8 post-detail-header-info">
            <div
              class="row post-detail-header-info-nickname fw-bold"
              style="font: bold"
            >
              hyeonwook_12
            </div>
            <div class="row post-detail-header-info-time fs-6">2시간 전</div>
          </div>
          <div class="col post-detail-header-follow" style="margin: auto">
            <div class="col align-self-center">
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
        <!-- 게시글 -->
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
                  src="../../../assets/img/testImage.jpeg"
                  class="d-block w-100"
                  alt="두 번째 사진"
                  style="aspect-ratio: 1 / 1"
                />
              </div>
              <div class="carousel-item">
                <img
                  src="../../../assets/img/KakaoTalk_20230803_125708971.jpg"
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
        <div class="row post-detail-like">
          <svg-icon type="mdi" :path="path"></svg-icon>
          <span style="text-align: left">좋아요 183개</span>
        </div>
        <div class="row post-detail-content">
          <span style="text-align: left">아... 하루만 못생겨보고 싶다..</span>
        </div>
        <!-- 댓글 -->
        <div class="row post-detail-comment" style="background-color: red">
          steststestet
        </div>
      </div>
      <div class="col"></div>
    </div>
  </div>
</template>
<script>
export default {};
</script>
<style>
.profile {
  height: 7vh;
  border-radius: 0%;
  object-fit: contain;
}
.post-detail-header {
  margin: auto;
}
</style>
