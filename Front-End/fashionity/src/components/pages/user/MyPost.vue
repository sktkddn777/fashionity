<template>
  <div>
    <router-link :to="{ path: `/post/${postSeq}` }">
      <img :src="postThumbnailImage" alt="" class="fixed-image-size" />
    </router-link>
  </div>
</template>
<script>
export default {
  props: ["post"],
  data() {
    return {
      postSeq: this.post.postSeq,
      postThumbnailImage: this.post.postThumbnailImage,
    };
  },
};
</script>
<style scoped>
.fixed-image-size {
  width: 100%;
  aspect-ratio: 1/1;

  object-fit: cover;
}
</style>
