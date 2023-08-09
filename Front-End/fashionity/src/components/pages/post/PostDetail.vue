<template lang="">
  <div id="post-detail" class="container-fluid">
    <div class="row">
      <div class="col"></div>
      <div class="col-6">
        <!-- 작성자 정보 -->
        <div class="post-detail-header">
          <div class="post-detail-header-img">
            <img
              :src="this.post.profileImg || '../img/unknown.e083a226.png'"
              alt="profileImg"
              class="post-detail-profile"
            />
          </div>
          <div class="post-detail-header-info">
            <div class="post-detail-header-info-nickname fw-bold">
              <!-- hyeonwook_12 -->
              {{ this.post.name }}
            </div>
            <div
              class="post-detail-header-info-time fs-6"
              style="text-align: left"
            >
              {{ this.timeAgo(this.post.createdAt) }}
            </div>
          </div>
          <div class="post-detail-header-follow" style="margin-left: auto">
            <div class="align-self-center" @click="toggleFollowing">
              <button
                type="button"
                class="btn btn-outline-dark"
                style="min-width: 70px"
                v-if="this.post.following === true"
              >
                <span style="font-size: smaller">&nbsp;팔로우&nbsp;</span>
              </button>
              <button
                type="button"
                class="btn btn-dark"
                style="min-width: 70px"
                v-else
              >
                <span style="font-size: smaller">&nbsp;팔로잉&nbsp;</span>
              </button>
            </div>
          </div>
        </div>
        <!-- 이미지 -->
        <div class="row post-detail-image">
          <div id="carouselExampleIndicators" class="carousel slide">
            <div class="carousel-indicators">
              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="0"
                class="active"
                aria-current="true"
                aria-label="Slide 1"
              ></button>
              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="1"
                aria-label="Slide 2"
              ></button>
              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="2"
                aria-label="Slide 3"
              ></button>
            </div>
            <div
              class="carousel-inner"
              v-for="(item, i) in this.post.images"
              :key="i"
            >
              <div v-if="i == 0" class="carousel-item active">
                <img
                  :src="item"
                  class="d-block w-100"
                  alt="첫 번째 사진"
                  style="aspect-ratio: 1 / 1"
                />
              </div>
              <div v-else class="carousel-item">
                <img
                  :src="item"
                  class="d-block w-100"
                  alt="사진"
                  style="aspect-ratio: 1 / 1"
                />
              </div>
              <!-- <div class="carousel-item">
                <img
                  src="../../../assets/img/hyeonwook3.jpg"
                  class="d-block w-100"
                  alt="세 번째 사진"
                  style="aspect-ratio: 1 / 1"
                />
              </div> -->
            </div>
            <button
              class="carousel-control-prev"
              type="button"
              data-bs-target="#carouselExampleIndicators"
              data-bs-slide="prev"
            >
              <span
                class="carousel-control-prev-icon"
                aria-hidden="true"
              ></span>
              <span class="visually-hidden">Previous</span>
            </button>
            <button
              class="carousel-control-next"
              type="button"
              data-bs-target="#carouselExampleIndicators"
              data-bs-slide="next"
            >
              <span
                class="carousel-control-next-icon"
                aria-hidden="true"
              ></span>
              <span class="visually-hidden">Next</span>
            </button>
          </div>
        </div>
        <!-- 본문 내용 -->
        <div class="post-detail-like">
          <div class="post-detail-like-icon">
            <font-awesome-icon
              :icon="['fas', 'heart']"
              :color="post.liked === true ? 'red' : 'black'"
            />
          </div>
          <div>
            <span style="text-align: left">좋아요&nbsp;</span>
            <span class="fw-bold" style="text-align: left">{{
              post.likeCount
            }}</span>
            <span style="text-align: left">개</span>
          </div>
        </div>
        <div class="post-detail-content">
          <div style="text-align: left">{{ post.content }}</div>
          <div class="post-detail-content-hashtag">
            <a
              v-for="(tag, i) in post.hashtags"
              :key="i"
              style="color: skyblue"
            >
              {{ tag }} &nbsp;
            </a>
          </div>
        </div>
        <!-- 댓글 -->
        <div class="post-detail-comment-cnt">
          <span>댓글&nbsp;</span>
          <span class="fw-bold">{{ post.commentCount }}</span>
          <span>개</span>
        </div>

        <div v-for="(comment, index) in comments" :key="index">
          <the-comment></the-comment>
        </div>

        <div class="post-detail-comment-submit">
          <input
            class="form-control"
            type="text"
            placeholder="댓글을 입력해주세요."
          />
          <button type="button" class="btn btn-dark" style="min-width: 70px">
            <span style="font-size: smaller">&nbsp;등록&nbsp;</span>
          </button>
        </div>
      </div>
      <div class="col"></div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import TheComment from "./TheComment.vue";
export default {
  props: ["seq"],
  data() {
    return {
      post: {},
      comments: [],
      commentOpen: false,
    };
  },
  components: {
    TheComment,
  },
  async mounted() {
    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${this.seq}`,
      method: "get",
    }).then((data) => {
      this.post = data.data.post;
      console.log(this.post);
    });

    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${this.seq}/comments`,
      method: "get",
    }).then((data) => {
      this.comments = [...data.data.comments];
      console.log(this.comments);
    });
  },
  methods: {
    timeAgo(timestamp) {
      const currentTime = new Date();
      const targetTime = new Date(timestamp);
      const elapsedMilliseconds = currentTime - targetTime;
      const elapsedSeconds = Math.floor(elapsedMilliseconds / 1000);

      if (elapsedSeconds < 60) {
        return `${elapsedSeconds}초 전`;
      } else if (elapsedSeconds < 3600) {
        const minutes = Math.floor(elapsedSeconds / 60);
        return `${minutes}분 전`;
      } else if (elapsedSeconds < 86400) {
        const hours = Math.floor(elapsedSeconds / 3600);
        return `${hours}시간 전`;
      } else {
        const days = Math.floor(elapsedSeconds / 86400);
        return `${days}일 전`;
      }
    },
    toggleFollowing() {
      if (this.post.following) {
        this.callUnFollowingAPI(this.post.name);
      } else {
        this.callFollowingAPI(this.post.name);
      }
      this.post.following = !this.post.following;
    },
    callFollowingAPI(name) {
      let token = sessionStorage.getItem("token");
      // name = this.post.name;
      console.log(name);
      let body = {
        nickname: name,
      };
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/follows`,
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        method: "POST",
        data: body,
      }).then((data) => {
        this.post.following = data.data.success;
        console.log(this.post.following);
      });
    },
    callUnFollowingAPI(name) {
      let token = sessionStorage.getItem("token");
      // name = this.post.name;
      let body = {
        nickname: name,
      };
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/follows`,
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        method: "DELETE",
        data: body,
      }).then((data) => {
        this.post.following = data.data.success;
        console.log(this.post.following);
      });
    },
  },
};
</script>
<style scoped>
.post-detail-profile {
  height: 7vh;
  border-radius: 100%;
  object-fit: contain;
}
.profile-comment {
  height: 5vh;
  border-radius: 100%;
  object-fit: contain;
}
.post-detail-header {
  margin: auto;
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}
.post-detail-like {
  display: flex;
  gap: 5px;
  margin-top: 10px;
  margin-bottom: 10px;
}
.post-detail-content {
  margin-bottom: 10px;
}
.post-detail-content-hashtag {
  display: flex;
}
.post-detail-comment-cnt {
  color: grey;
  text-align: left;
  margin-bottom: 10px;
}
.post-detail-comment-content {
  display: flex;
}
.post-detail-comment-info {
  margin-left: auto;
  display: flex;
  gap: 10px;
}
.post-detail-comment-submit {
  display: flex;
}
</style>
