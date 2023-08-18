<template lang="">
  <div
    class="row justify-content-center"
    style="
      margin-bottom: 30px;
      border-width: 1px;
      border-color: white white #bdbdbd white;
    "
  >
    <div class="col col-lg-2 header-tab point">
      <router-link
        to="/consultant/rc"
        style="text-decoration: none; color: #424242"
        >예약확인</router-link
      >
    </div>
    <div class="col col-lg-2 header-tab">
      <router-link
        to="/consultant/rc/set"
        style="text-decoration: none; color: #424242"
        >예약설정</router-link
      >
    </div>
  </div>
</template>
<script>
export default {
  name: "TheNavCheckReservationVue",
};
</script>

<style scoped>
* {
  font-size: 25px;
}
.point {
  font-weight: bold;
}
</style>
