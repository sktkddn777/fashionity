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
              class="row post-detail-header-inform-nickname fw-bold"
              style="font: bold"
            >
              hyeonwook_12
            </div>
            <div class="row post-detail-header-inform-information fs-6">
              췍! 췍! 암더
            </div>
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
        <div class="row post-detail-image" style="background-color: grey">
          <v-carousel>
            <v-carousel-item
              src="../../../assets/img/hyeonwook.jpg"
              cover
            ></v-carousel-item>

            <v-carousel-item
              src="../../../assets/img/KakaoTalk_20230803_125708971.jpg"
              cover
            ></v-carousel-item>

            <v-carousel-item
              src="../../../assets/img/testImage.jpeg"
              cover
            ></v-carousel-item>
          </v-carousel>
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
