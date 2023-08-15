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
            <div v-if="!isLogin"></div>
            <div v-else class="post-detail-header-modal align-self-center">
              <div v-if="!this.post.myPost" @click="toggleFollowing">
                <button
                  type="button"
                  class="active-button"
                  style="min-width: 70px"
                  v-if="this.post.following === false"
                >
                  <span style="font-size: smaller">&nbsp;팔로우&nbsp;</span>
                </button>
                <button
                  type="button"
                  class="inactive-button"
                  style="min-width: 70px"
                  v-else
                >
                  <span style="font-size: smaller">&nbsp;팔로잉&nbsp;</span>
                </button>
              </div>
              <div v-else></div>
              <div>
                <v-menu>
                  <template v-slot:activator="{ props }">
                    <font-awesome-icon
                      v-bind="props"
                      :icon="['fas', 'ellipsis']"
                      style="color: #999999"
                    />
                  </template>
                  <v-list>
                    <v-list-item v-if="!this.post.myPost">
                      <!-- 신고 모달 -->
                      <report-modal :seq="this.post.postSeq"></report-modal>
                    </v-list-item>
                    <v-list-item v-else>
                      <router-link
                        :to="{ path: `/post/${seq}/modify` }"
                        class="link"
                      >
                        <v-list-item-title type="button"
                          >수정</v-list-item-title
                        >
                      </router-link>
                      <v-list-item-title type="button" @click="deleteConfirm"
                        >삭제</v-list-item-title
                      >
                    </v-list-item>
                  </v-list>
                </v-menu>
              </div>
            </div>
          </div>
        </div>
        <!-- 이미지 -->
        <div class="row post-detail-image">
          <div id="carouselExampleIndicators" class="carousel slide">
            <div class="carousel-inner">
              <div
                v-for="(item, i) in this.post.images"
                :key="i"
                :class="['carousel-item', i === 0 ? 'active' : '']"
              >
                <img
                  :src="item"
                  class="d-block w-100"
                  alt="사진"
                  style="aspect-ratio: 1 / 1"
                />
              </div>
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
          <div v-if="!isLogin" @click="loginAlert">
            <font-awesome-icon :icon="['fas', 'heart']" />
          </div>
          <div v-else class="post-detail-like-icon" @click="toggleLike">
            <font-awesome-icon
              :icon="['fas', 'heart']"
              :color="post.liked === true ? 'red' : 'grey'"
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
              #{{ tag }} &nbsp;
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
          <the-comment :comment="comment"></the-comment>
        </div>

        <div class="post-detail-comment-submit">
          <input
            v-model="commentContent"
            class="form-control"
            type="text"
            placeholder="댓글을 입력해주세요."
          />
          <button
            type="button"
            class="active-button"
            style="min-width: 70px"
            @click="submitComment"
          >
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
import { mapState } from "vuex";
import ReportModal from "./ReportModal.vue";
const memberStore = "memberStore";
export default {
  props: ["seq"],
  computed: {
    ...mapState(memberStore, ["isLogin", "loginUser"]),
  },
  data() {
    return {
      post: {},
      comments: [],
      commentOpen: false,
      like: "",
      likeCount: "",
      commentContent: "",
    };
  },
  watch: {
    comments(newVal) {
      this.comments = newVal;
    },
  },
  components: {
    TheComment,
    ReportModal,
  },
  async mounted() {
    let token = sessionStorage.getItem("token");
    axios({
      headers:
        token === null
          ? null
          : {
              Authorization: `Bearer ${token}`,
            },
      url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${this.seq}`,
      method: "get",
    }).then((data) => {
      this.post = data.data.post;
      console.log(this.post);
      this.like = this.post.liked;
      this.likeCount = this.post.likeCount;
    });
    this.callCommentListApi();
  },
  methods: {
    callCommentListApi() {
      let token = sessionStorage.getItem("token");
      axios({
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${this.seq}/comments`,
        method: "get",
      }).then((data) => {
        this.comments = [...data.data.comments];
      });
    },

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
    async toggleFollowing() {
      if (this.post.following === true) {
        await this.callUnFollowingAPI(this.post.name);
      } else {
        await this.callFollowingAPI(this.post.name);
      }
    },
    async callFollowingAPI(name) {
      let token = sessionStorage.getItem("token");
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
        this.success = data.data.success;
        this.post.following = !this.post.following;
      });
    },
    async callUnFollowingAPI(name) {
      let token = sessionStorage.getItem("token");
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
        this.success = data.data.success;
        this.post.following = !this.post.following;
      });
    },
    toggleLike() {
      this.callLikeAPI(this.post.liked);
      this.post.liked = !this.post.liked;
      this.post.liked ? this.post.likeCount++ : this.post.likeCount--;
    },
    callLikeAPI(status) {
      let token = sessionStorage.getItem("token");

      let body = {
        is_like: status,
      };
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${this.post.postSeq}/like`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "POST",
        data: body,
      }).then((data) => {
        this.post.liked = data.data.like;
      });
    },
    loginAlert() {
      alert("로그인해주세요.");
    },
    deleteConfirm() {
      if (confirm("삭제하시겠습니까?")) {
        this.deletePost();
      }
    },
    async deletePost() {
      try {
        await this.callDeleteAPI();
        alert("삭제되었습니다.");
        this.navigateToMain();
      } catch (error) {
        console.error("게시글 삭제 실패", error);
      }
    },
    navigateToMain() {
      this.$router.push("/post");
    },
    callDeleteAPI() {
      let token = sessionStorage.getItem("token");
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${this.post.postSeq}`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "DELETE",
      }).then((data) => {
        console.log(data);
      });
    },
    async submitComment() {
      if (!this.isLogin) {
        this.loginAlert();
      } else {
        let isBlank = this.isBlank(this.commentContent);
        if (isBlank) {
          alert("댓글을 입력해주세요.");
        } else {
          await this.callCommentSaveAPI(this.commentContent);
          this.callCommentListApi();
          this.commentContent = "";
        }
      }
    },
    isBlank(commentContent) {
      return commentContent === null || commentContent.trim() === "";
    },
    callCommentSaveAPI(content) {
      let token = sessionStorage.getItem("token");
      let postSeq = this.$route.params.seq;
      const body = {
        content: content,
      };
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${postSeq}/comments`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
              },
        method: "POST",
        data: body,
      }).then((data) => {
        console.log(data);
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
.post-detail-header-modal {
  display: flex;
  gap: 20px;
}
.active-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
}
.inactive-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #ffffff;
}
</style>
