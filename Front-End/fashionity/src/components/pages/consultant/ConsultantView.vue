<template lang="">
  <div class="container-fluid">
    <the-nav-bar-consultant></the-nav-bar-consultant>
    <router-view></router-view>
    <RConsultantCheckVue></RConsultantCheckVue>
  </div>
</template>
<script>
import TheNavBarConsultant from "@/components/layout/TheNavBarConsultant.vue";
import RConsultantCheckVue from "./consultantSide/RConsultantCheckVue.vue";

export default {
  data() {
    return {};
  },
  components: {
    TheNavBarConsultant,
    RConsultantCheckVue,
  },
};
</script>
<style></style>
