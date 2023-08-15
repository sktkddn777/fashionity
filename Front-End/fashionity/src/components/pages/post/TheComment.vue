<template>
  <div id="comment">
    <!--댓글 프로필 이미지-->
    <div class="post-detail-header">
      <div class="post-detail-header-img">
        <img
          :src="comment.profileImg || '../img/unknown.e083a226.png'"
          alt="profileImg"
          class="profile-comment"
        />
      </div>
      <!--댓글 내용-->
      <div>
        <div class="fw-bold" style="text-align: left">
          {{ comment.nickname }}
        </div>
        <div v-if="!isChecked" style="text-align: left; font-size: 15px">
          {{ comment.content }}
        </div>
        <div v-else class="post-detail-comment-submit">
          <input v-model="content" class="form-control" type="text" />
          <button type="button" class="active-button" @click="modifyComment">
            <span style="font-size: smaller">&nbsp;수정&nbsp;</span>
          </button>
        </div>
      </div>
      <!--댓글 정보-->
      <div class="post-detail-comment-info">
        <div style="color: grey; font-size: 13px">
          {{ this.timeAgo(comment.createdAt) }}
        </div>
        <div v-if="!isLogin" @click="loginAlert">
          <font-awesome-icon :icon="['fas', 'heart']" style="color: grey" />
        </div>
        <div v-else class="post-detail-like-icon" @click="toggleLike">
          <font-awesome-icon
            :icon="['fas', 'heart']"
            :color="comment.liked === true ? 'red' : 'grey'"
          />
        </div>
        <div v-if="!isLogin"></div>
        <v-menu v-else>
          <template v-slot:activator="{ props }">
            <font-awesome-icon
              v-bind="props"
              :icon="['fas', 'ellipsis']"
              style="color: #999999"
            />
          </template>

          <v-list>
            <v-list-item v-if="comment.myComment">
              <v-list-item-title type="button" @click="toggleInput"
                >수정</v-list-item-title
              >
              <v-list-item-title
                type="button"
                data-bs-toggle="modal"
                data-bs-target="#deleteModal"
                >삭제</v-list-item-title
              >
              <!-- delete Modal -->
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
            <v-list-item v-else>
              <report-modal></report-modal>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </div>
  </div>
</template>
<script>
import ReportModal from "./ReportModal.vue";
import axios from "axios";
import { mapState } from "vuex";
const memberStore = "memberStore";
export default {
  props: ["comment"],
  components: {
    ReportModal,
  },
  data() {
    return {
      isChecked: false,
      currComment: "",
      content: "",
      like: "",
      likeCnt: "",
    };
  },
  computed: {
    ...mapState(memberStore, ["isLogin", "loginUser"]),
  },
  mounted() {
    this.currComment = this.comment;
    this.content = this.comment.content;
    console.log(this.comment);
  },
  methods: {
    toggleInput() {
      this.isChecked = !this.isChecked;
    },
    modifyComment() {
      if (this.content === null || this.content.trim() === "") {
        alert("댓글을 입력해주세요.");
      } else {
        this.callCommentModifyAPI(this.content);
      }
    },
    callCommentModifyAPI(content) {
      let token = sessionStorage.getItem("token");
      let postSeq = this.$route.params.seq;
      let body = {
        commentSeq: this.comment.commentSeq,
        content: content,
      };
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${postSeq}/comments/${body.commentSeq}`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "PUT",
        data: body,
      }).then((data) => {
        console.log(data);
        this.currComment.content = content;
        this.toggleInput();
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
    loginAlert() {
      alert("로그인해주세요.");
    },
    toggleLike() {
      this.callLikeAPI(this.currComment.commentSeq);
      this.currComment.liked = !this.currComment.liked;
      this.currComment.liked
        ? this.currComment.likeCount++
        : this.currComment.likeCount--;
    },
    callLikeAPI(commentSeq) {
      let token = sessionStorage.getItem("token");
      let postSeq = this.$route.params.seq;
      let body = {
        commentSeq: commentSeq,
      };
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${postSeq}/comments/${commentSeq}/like`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "POST",
        data: body,
      }).then((data) => {
        this.currComment.liked = data.data.like;
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
.active-button {
  width: 60px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
}
</style>
