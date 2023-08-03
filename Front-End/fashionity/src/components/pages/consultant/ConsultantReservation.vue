<template lang="">
  <div class="container-fliud" style="height: 50%">
    <div class="row">
      <div class="col-3">
        <div class="row d-flex justify-content-center">
          <div class="col" style="margin-top: 10%; height: 50%">
            <img
              src="../../../assets/img/panda.png"
              alt=""
              class="profile"
              style="width: 50%"
            />
          </div>
        </div>
        <div class="row" style="height: 30px"></div>
        <div class="row">
          <div class="col review">
            <consultant-review></consultant-review>
            <consultant-review></consultant-review>
            <consultant-review></consultant-review>
            <consultant-review></consultant-review>
            <consultant-review></consultant-review>
            <consultant-review></consultant-review>
            <consultant-review></consultant-review>
          </div>
        </div>
      </div>
      <div class="col-8">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>
<script>
import ConsultantReview from "@/components/pages/consultant/ConsultantReview.vue";
export default {
  data() {
    return {};
  },
  components: {
    ConsultantReview,
  },
};
</script>
<style>
.profile {
  padding-top: 1px;
  height: 50%;
  border-radius: 70%;
  object-fit: cover;
}

.review {
  -ms-overflow-style: none; /* 인터넷 익스플로러 */
  scrollbar-width: none; /* 파이어폭스 */
}
</style>
