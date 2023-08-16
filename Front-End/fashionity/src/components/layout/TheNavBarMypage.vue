<template lang="">
  <div
    class="row justify-content-center"
    style="
      margin-bottom: 30px;
      border-style: solid;
      border-width: 1px;
      border-color: white white #bdbdbd white;
    "
  >
    <div class="col col-lg-2 header-tab">
      <router-link to="/post" style="text-decoration: none; color: #424242"
        >Home</router-link
      >
    </div>
    <div class="col col-lg-2 header-tab">
      <router-link
        to="/consultant"
        style="text-decoration: none; color: #424242"
        >Consultant</router-link
      >
    </div>
    <div class="col col-lg-2 header-tab point">
      <router-link to="/profile" style="text-decoration: none; color: #424242"
        >Mypage</router-link
      >
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      myNickname: null,
    };
  },
  computed: {
    profileLink() {
      return `/profile/${this.myNickname}`;
    },
  },
  created() {
    this.myNickname =
      this.$store.getters["memberStore/checkLoginUser"].nickname;
  },
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
