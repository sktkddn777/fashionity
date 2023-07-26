<template lang="">
  <div>
    <the-nav-bar></the-nav-bar>
    <div class="container">
      <div class="row">
        <div class="col-1" id="logo">Fashionity</div>
      </div>
      <div class="row justify-content-center">
        <div class="col col-lg-2 header-tab">HOME</div>
        <div class="col col-lg-2 header-tab">CONSULTING</div>
        <div class="col col-lg-2 header-tab">LIVE</div>
        <div class="col col-lg-2 header-tab">MY PAGE</div>
      </div>
    </div>
  </div>
</template>
<script>
import TheNavBar from "./TheNavBar.vue";

export default {
  components: {
    TheNavBar,
  },
};
</script>
<style>
.header-tab {
  font-style: normal;
  font-size: 25px;
  line-height: 38px;

  text-align: center;
}
#logo {
  margin-left: 3%;
  font-style: normal;
  font-size: 50px;
}
</style>
