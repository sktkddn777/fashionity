<template lang="">
  <div class="container-fluid">
    <the-nav-bar-consultant></the-nav-bar-consultant>
    <router-view></router-view>
  </div>
</template>
<script>
import TheNavBarConsultant from "@/components/layout/TheNavBarConsultant.vue";
export default {
  data() {
    return {};
  },
  components: {
    TheNavBarConsultant,
  },
};
</script>
<style></style>
