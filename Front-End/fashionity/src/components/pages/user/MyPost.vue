<template>
  <div>
    <div>{{ post.postThumbnailImage }}</div>
  </div>
</template>
<script>
export default {
  props: ["post"],
  data() {
    return {
      postSeq: "",
      postThumbnailImage: "",
    };
  },
};
</script>
