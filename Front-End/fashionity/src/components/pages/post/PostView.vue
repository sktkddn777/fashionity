<template lang="">
  <div class="container-fluid">
    <the-nav-bar-post></the-nav-bar-post>
    <router-view></router-view>
  </div>
</template>
<script>
import TheNavBarPost from "@/components/layout/TheNavBarPost.vue";
// import PostList from "@/components/pages/post/PostList.vue";

export default {
  data() {
    return {};
  },
  components: {
    TheNavBarPost,
    // PostList,
  },
};
</script>
<style scoped></style>
