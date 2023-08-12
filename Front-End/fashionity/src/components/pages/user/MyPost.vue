<template>
  <div>
    <img :src="postThumbnailImage" alt="" class = "fixed-image-size">
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
<style>
.fixed-image-size {
  width: 200px;
  height: 200px; 
  object-fit: cover; 
}
</style>



