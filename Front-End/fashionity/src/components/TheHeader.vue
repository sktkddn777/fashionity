<template lang="">
  <div>
    <the-nav-bar></the-nav-bar>

    <div class="container">
      <div class="row">
        <div class="col-1" id="logo">Fashionity</div>
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
#logo {
  margin-left: 3%;
  font-style: normal;
  font-weight: 600;
  font-size: 50px;
}
</style>
