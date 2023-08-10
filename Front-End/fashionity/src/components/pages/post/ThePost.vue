<template lang="">
  <div
    id="post_outer"
    class="container-fluid"
    style="background-color: white; min-width: 220px; min-height: 300px"
  >
    <div class="row justify-content-center">
      <!-- 게시글 썸네일 -->
      <router-link
        :to="{ path: `/post/${post.post_seq}` }"
        class="img-link"
        style="text-decoration: none; color: #424242"
      >
        <img :src="post.images[0]" alt="" />
      </router-link>
      <!-- 게시글 정보 -->
      <div class="col align-self-center align-middle post-detail">
        <!-- 프로필 정보 및 좋아요 -->
        <!-- 프로필 정보 -->
        <router-link
          :to="{ path: `` }"
          style="text-decoration: none; color: #424242"
        >
          <div class="col align-self-center align-middle post-detail-writer">
            <!-- <img class="profile" src="@/assets/img/unknown.png" /> -->
            <img
              class="profile"
              :src="this.post.profile_img || '../img/unknown.e083a226.png'"
            />
            <span class="post-detail-font">{{ post.name }}</span>
          </div>
        </router-link>
        <div class="col post-detail-like" @click="toggleLike">
          <font-awesome-icon
            class="post-detail-like-icon"
            :icon="['fas', 'heart']"
            v-if="this.like"
            :style="{ color: 'red' }"
          />
          <font-awesome-icon
            class="post-detail-like-icon"
            :icon="['fas', 'heart']"
            v-else
            :style="{ color: '#DCDCDC' }"
          />
          <span>{{ this.like_count }}</span>
        </div>
      </div>
      <!-- 게시글 -->
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
      })
        .then((data) => {
          this.like = data.data.like;
          this.like ? this.like_count++ : this.like_count--;
        })
        .catch((data) => {
          console.log(data);
          if (data.response.status === 401) {
            alert("로그인을 진행해주세요");
          } else {
            console.log("error");
          }
        });
    },
  },
};
</script>
<style scoped>
#post_outer {
  border: 1px solid rgba(189, 189, 189, 0.3);
  max-width: 250px;
  border-radius: 20px;
  padding: 0px;
}
.img-link {
  text-decoration: none;
  top: 0;
  left: 0;
  /* height: 50%; */
  /* max-width: 250px; */
  border-radius: 20px;
}

.img-link > img {
  width: 100%;
  height: 100%;
  border-radius: 20px;
}

.profile {
  height: 20px;
  width: 20px;
  border-radius: 70%;
  object-fit: contain;
  margin-right: 10px;
}

.post-font {
  font-size: 11px;
  color: #bdbdbd;
  height: 45px;
  padding: 5px;
  display: flex;
  align-items: center;
  justify-content: left;
  text-align: left;
}

.profile {
}
.post-detail-font {
  font-size: 12px;
  font-weight: 800;
}

.post-detail {
  display: flex;
  overflow: visible;
  height: 35px;
  align-items: center;
}
.post-detail-writer {
  padding-left: 10px;
  display: flex;
  justify-content: left;
  align-content: center;
}

.post-detail-like {
  display: flex;
  justify-content: right;
  padding-right: 10px;
  align-items: center;
}
.post-detail-like-icon {
}
.post-detail-like > span {
  font-size: 12px;
  color: #bdbdbd;
  margin-left: 10px;
}
</style>
