<template lang="">
  <div
    class="container-fluid justify-content-center d-flex align-items-center"
    style="height: 65vh; margin-left: 4vw"
  >
    <div class="row" style="height: 55vh; width: 80%">
      <!-- 오전 -->
      <div class="row">오전</div>
      <div class="row d-flex">
        <div
          class="col-3 time-block d-flex align-items-center justify-content-center"
          v-for="(time, index) in am1"
          :key="index"
        >
          {{ time }}
        </div>
      </div>
      <div class="row d-flex">
        <div
          class="col-3 time-block d-flex align-items-center justify-content-center"
          v-for="(time, index) in am2"
          :key="index"
        >
          {{ time }}
        </div>
      </div>
      <!-- 오후 -->
      <div class="row">오후</div>
      <div class="row d-flex">
        <div
          class="col-3 time-block d-flex align-items-center justify-content-center"
          v-for="(time, index) in pm1"
          :key="index"
        >
          {{ time }}
        </div>
      </div>
      <div class="row d-flex">
        <div
          class="col-3 time-block d-flex align-items-center justify-content-center"
          v-for="(time, index) in pm2"
          :key="index"
        >
          {{ time }}
        </div>
      </div>

      <!-- <div v-for="(time, index) in pm" :key="index">{{ time }}</div> -->
    </div>
  </div>
</template>
<script>
export default {
  date() {
    return {
      am1: [],
      am2: [],
      pm1: [],
      pm2: [],
    };
  },
  created() {
    this.am1 = ["6:00", "7:00", "8:00"];
    this.am2 = ["9:00", "10:00", "11:00", "12:00"];
    this.pm1 = ["13:00", "14:00", "15:00", "16:00"];
    this.pm2 = ["17:00", "18:00", "19:00", "20:00"];
  },
};
</script>
<style scoped>
.time-block {
  background-color: #3399ff;
  height: 5vh;
  text-align: center;
  margin-right: 10px;
  width: 6vw;
  /* padding-top: 10px; */
}
</style>
