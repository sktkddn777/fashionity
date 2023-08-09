<template lang="">
  <div id="post outer" class="container-fluid" style="background-color: white">
    <div class="row justify-content-center">
      <img
        :src="post.images[0]"
        alt=""
        style="
          top: 0;
          left: 0;
          height: 50%;
          max-width: 250px;
          border-radius: 20px;
        "
      />
      <div class="row align-self-center align-middle">
        <div
          class="col align-self-center align-middle"
          style="display: flex; justify-content: center; align-items: center"
        >
          <img class="profile" src="@/assets/img/unknown.png" />
          <span class="post-font">{{ post.name }}</span>
        </div>
        <div @click="toggleLike">
          <div
            class="col post-font align-self-center"
            v-if="this.like === true"
          >
            하트
          </div>
          <div class="col post-font align-self-center" v-else>빈하트</div>
        </div>
        <div>
          {{ this.like_count }}
        </div>
      </div>
      <div class="row post-font justify-content-center">
        {{ post.content }}
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";

export default {
  props: ["post"],
  data() {
    return {
      like: this.post.liked,
      like_count: this.post.like_count,
    };
  },
  methods: {
    toggleLike() {
      this.callLikeAPI(this.like);
      //해당 post의 좋아요를 변경
      this.like = !this.like;
      this.like ? this.like_count++ : this.like_count--;
    },
    callLikeAPI(status) {
      let token = sessionStorage.getItem("token");
      let body = {
        is_like: status,
      };
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${this.post.post_seq}/like`,
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        method: "POST",
        data: body,
      }).then((data) => {
        this.like = data.data.like;
      });
    },
  },
};
</script>

<style scoped>
.profile {
  height: 20px;
  width: 20px;
  border-radius: 70%;
  object-fit: contain;
  margin-right: 10px;
}

.outer {
  display: flex;
  justify-content: center;
}

.post-font {
  font-size: 12px;
}
</style>
