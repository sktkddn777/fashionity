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
                      <v-list-item-title
                        type="button"
                        data-bs-toggle="modal"
                        data-bs-target="#reportModal"
                        >신고</v-list-item-title
                      >
                      <!-- report Modal -->
                      <div
                        class="modal fade"
                        id="reportModal"
                        tabindex="-1"
                        aria-labelledby="reportModalLabel"
                        aria-hidden="true"
                      >
                        <div class="modal-dialog">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h1
                                class="modal-title fs-5"
                                id="reportModalLabel"
                              >
                                신고
                              </h1>
                              <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                              ></button>
                            </div>
                            <div class="modal-body">
                              <report-modal></report-modal>
                            </div>
                            <div class="modal-footer">
                              <button
                                type="button"
                                class="btn btn-outline-secondary"
                                data-bs-dismiss="modal"
                              >
                                취소
                              </button>
                              <button type="button" class="btn btn-primary">
                                신고
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </v-list-item>
                    <v-list-item v-else>
                      <router-link to="/post/modify" class="link">
                        <v-list-item-title type="button"
                          >수정</v-list-item-title
                        >
                      </router-link>
                      <v-list-item-title
                        type="button"
                        data-bs-toggle="modal"
                        data-bs-target="#deleteModal"
                        >삭제</v-list-item-title
                      >
                      <div
                        class="modal fade"
                        id="deleteModal"
                        tabindex="-1"
                        aria-labelledby="deleteModalLabel"
                        aria-hidden="true"
                      >
                        <div class="modal-dialog">
                          <div class="modal-content">
                            <div class="modal-header">
                              <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close"
                              ></button>
                            </div>
                            <div class="modal-body" style="text-align: center">
                              정말 삭제하시겠습니까?
                            </div>
                            <div class="modal-footer">
                              <button
                                type="button"
                                class="btn btn-outline-secondary"
                                data-bs-dismiss="modal"
                              >
                                아니오
                              </button>
                              <button type="button" class="btn btn-primary">
                                &nbsp;&nbsp;네&nbsp;&nbsp;
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
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
import { mapState } from "vuex";
import ReportModal from "./ReportModal.vue";
const memberStore = "memberStore";
export default {
  props: ["seq"],
  computed: {
    ...mapState(memberStore, ["isLogin"]),
  },
  data() {
    return {
      post: {},
      comments: [],
      commentOpen: false,
      like: "",
      likeCount: "",
    };
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
      if (this.post.following === true) {
        this.callUnFollowingAPI(this.post.name);
      } else {
        this.callFollowingAPI(this.post.name);
      }
      this.post.following = !this.post.following;
    },
    callFollowingAPI(name) {
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
        this.following = data.data.success;
      });
    },
    callUnFollowingAPI(name) {
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
        this.following = data.data.success;
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
</style>
