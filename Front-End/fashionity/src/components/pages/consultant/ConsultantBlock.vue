<template lang="">
  <div
    id="post_outer"
    class="container-fluid"
    style="background-color: white; min-width: 220px; min-height: 300px"
  >
    <div class="row justify-content-center">
      <!-- 컨설턴트 썸네일 -->
      <router-link
        :to="{ path: `` }"
        class="img-link"
        style="text-decoration: none; color: #424242"
      >
        <img :src="this.post.profileUrl || '../img/unknown.e083a226.png'" alt="" />
      </router-link>
      <!-- 게시글 정보 -->
      <div class="col align-self-center align-middle post-detail">
        <!-- 프로필 정보 및 좋아요 -->
        <!-- 프로필 정보 -->
        <router-link :to="{ path: `` }" style="text-decoration: none; color: #424242">
          <div class="col align-self-center align-middle post-detail-writer">
            <!-- <img class="profile" src="@/assets/img/unknown.png" /> -->
            <img class="profile" :src="this.post.profileUrl || '../img/unknown.e083a226.png'" />
            <span class="post-detail-font">{{ post.nickname }}</span>
          </div>
        </router-link>
        <div class="col post-detail-like">
          <font-awesome-icon
            class="post-detail-like-icon"
            :icon="['fas', 'star']"
            :style="{ color: 'yellow' }"
          />
          <span>{{ post.avgGrade }} | {{ post.totalCnt }}개 리뷰</span>
        </div>
      </div>
      <!-- 게시글 -->
      <div class="row post-font justify-content-center">
        {{ post.profileIntro }}
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: ["post"],
  data() {
    return {};
  },
  mounted() {
    this.cutText();
  },
  methods: {
    cutText() {
      const textContainers = document.querySelectorAll(".post-font");
      for (var i = 0; i < textContainers.length; i++) {
        const textContainer = textContainers[i];
        if (textContainer.scrollWidth > textContainer.clientWidth) {
          while (textContainer.scrollWidth > textContainer.clientWidth) {
            textContainer.textContent = textContainer.textContent.slice(0, -1);
          }
          textContainer.textContent = textContainer.textContent.slice(0, -3) + "...";
        }
      }
    },
  },
};
</script>
<style scoped>
/* .profile {
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
} */

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
  font-size: 14px;
  color: rgba(66, 66, 66, 0.8);
  height: 45px;
  padding: 5px 15px;
  display: flex;
  align-items: center;
  justify-content: left;
  text-align: left;

  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
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
