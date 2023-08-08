<template>
  <div>
    <first-component></first-component>
    <second-component></second-component>
  </div>
</template>

<script>
import FirstComponent from "../components/pages/consulting/Consulting-WebCam.vue";
import SecondComponent from "../components/pages/consulting/TheChatting.vue";

export default {
  components: {
    FirstComponent,
    SecondComponent,
  },
};
</script>

<style></style>
