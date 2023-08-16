<template lang="">
  <div class="container-fliud" style="height: 85vh">
    <div class="row">
      <div class="col-3" style="height: 85vh; margin-left: 5vw">
        <div class="row d-flex justify-content-center">
          <div class="col" style="margin-top: 10%; height: 50%">
            <img
              src="../../../assets/img/imgtemp.jpg"
              alt=""
              class="profile"
              style="width: 50%"
            />
          </div>
        </div>
        <div class="row" style="height: 30px"></div>
        <div class="row">
          <div class="col review scroll">
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
      <div class="col-8" style="height: 75vh">
        <router-view></router-view>
        <div class="row">
          <div class="col"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import ConsultantReview from "@/components/pages/consultant/ConsultantReview.vue";
export default {
  data() {
    return {
      // propInfo: "",
    };
  },
  components: {
    ConsultantReview,
  },
  props: {},
  // methods: {
  //   propChange(prop) {
  //     console.log("AAAAAAAAAAAAAAAAAA");
  //     this.propInfo = prop;
  //     console.log("mount 후", this.propInfo);
  //   },
  // },
  // mounted() {
  //   this.propInfo = "date";
  //   console.log(this.propInfo);
  // },
  // watch: {
  //   propInfo(newVal) {
  //     this.propInfo = newVal;
  //     console.log("watch", this.propInfo);
  //   },
  // },
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
