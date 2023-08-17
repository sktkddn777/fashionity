<template lang="">
  <div class="container-fliud" style="height: 85vh">
    <div class="row">
      <div class="col-3" style="height: 85vh; margin-left: 5vw">
        <div class="row d-flex justify-content-center">
          <div class="col" style="margin-top: 10%; height: 50%">
            <img
              v-if="this.consultantInfo.profileUrl"
              src="this.consultantInfo.profileUrl"
              alt=""
              class="profile"
              style="width: 50%"
            />
            <img
              src="../../../assets/img/unknown.png"
              alt=""
              class="profile"
              style="width: 50%"
            />
          </div>
        </div>
        <div class="row" style="height: 30px"></div>
        <div class="row">
          <div
            v-for="(review, index) in this.reviewList"
            :key="index"
            class="col review scroll"
          >
            <consultant-review :review="review"></consultant-review>
            <!-- <consultant-review></consultant-review>
            <consultant-review></consultant-review>
            <consultant-review></consultant-review>
            <consultant-review></consultant-review>
            <consultant-review></consultant-review>
            <consultant-review></consultant-review> -->
          </div>
        </div>
      </div>
      <div class="col-8" style="height: 75vh">
        <router-view :scheduleList="scheduleList"></router-view>
        <div class="row">
          <div class="col"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import ConsultantReview from "@/components/pages/consultant/ConsultantReview.vue";
import axios from "axios";
export default {
  data() {
    return {
      nickname: "sangu", // 임시로 사용할 예정 나중에 list 부분 수정되면 수정하자
      consultantInfo: {},
      reviewList: [],
      scheduleList: [],
    };
  },
  components: {
    ConsultantReview,
  },
  props: {},
  async mounted() {
    let token = sessionStorage.getItem("token");

    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/${this.nickname}`,
      headers:
        token === null
          ? null
          : {
              Authorization: `Bearer ${token}`,
            },
      method: "GET",
    }).then((data) => {
      this.consultantInfo = data.data.consultant[0];
      // console.log(this.consultantInfo);
      this.reviewList = this.consultantInfo.reviews;
      this.scheduleList = this.consultantInfo.schedules;
      // console.log("review : ", this.reviewList);
      // console.log("schedule : ", this.scheculeList);
    });
  },
};
</script>
<style scoped>
.profile {
  padding-top: 1px;
  height: 50%;
  border-radius: 70%;
  object-fit: cover;
}

.review {
  overflow: scroll;
  height: 350px;
}

/* ( 크롬, 사파리, 오페라, 엣지 ) 동작 */
.scroll::-webkit-scrollbar {
  display: none;
}

.scroll {
  -ms-overflow-style: none; /* 인터넷 익스플로러 */
  scrollbar-width: none; /* 파이어폭스 */
}
</style>
