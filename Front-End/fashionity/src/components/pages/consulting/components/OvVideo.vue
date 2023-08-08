<template>
  <video ref="myWebCam" class="webcam" autoplay />
</template>
<style>
@import url("./OvVideo.css");
</style>
<script>
export default {
  name: "OvVideo",

  props: {
    streamManager: Object,
  },

  mounted() {
    this.streamManager.addVideoElement(this.$el);
  },
};
</script>