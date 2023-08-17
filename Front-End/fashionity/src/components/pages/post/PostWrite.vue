<template lang="">
  <div id="post-detail" class="container-fluid">
    <div class="row">
      <div class="col"></div>
      <div class="col-6">
        <!-- 작성자 정보 -->
        <div class="post-detail-header">
          <div class="post-detail-header-img">
            <img
              :src="getProfileUrl() || '../img/unknown.e083a226.png'"
              alt="profileImg"
              class="profile"
            />
          </div>
          <div class="post-detail-header-info">
            <div class="post-detail-header-info-nickname fw-bold">
              {{ this.myNickname }}
            </div>
          </div>
        </div>
        <!--이미지 업로드-->
        <multi-image-upload @updateImg="updateImg"></multi-image-upload>
        <br />
        <div style="text-align: left">
          <textarea
            class="form-control"
            rows="1"
            placeholder="해시태그를 입력해주세요."
            v-model="tagInput"
            @keyup.enter="addTag"
          ></textarea>
          <span
            v-for="(tag, index) in tagList"
            :key="index"
            class="tag"
            style="color: skyblue"
          >
            #{{ tag }}&nbsp;
          </span>
        </div>
        <br />
        <textarea
          class="form-control"
          rows="3"
          placeholder="내용을 입력해주세요."
          v-model="contentInput"
          required
        ></textarea>

        <div class="post-write-button">
          <button
            type="button"
            class="inactive-button"
            style="min-width: 70px"
            @click="navigateToMain"
          >취소
          </button>
          <button
            type="button"
            class="active-button"
            style="min-width: 70px"
            @click="submitPost"
          >
          등록
          </button>
        </div>
      </div>
      <div class="col"></div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import MultiImageUpload from "../shared/MultiImageUpload.vue";
import { mapState } from "vuex";
const memberStore = "memberStore";
export default {
  data() {
    return {
      myNickname: this.$store.getters["memberStore/checkLoginUser"].nickname,
      tagInput: "",
      tagList: [],
      contentInput: "",
      imgList: [],
      fileList: [],
    };
  },
  components: {
    MultiImageUpload,
  },
  computed: {
    ...mapState(memberStore, ["loginUser"]),
  },
  methods: {
    getProfileUrl() {
      return this.loginUser.profileUri;
    },
    addTag() {
      if (this.tagInput) {
        this.tagList.push(this.tagInput.substr(0, this.tagInput.length - 1));
        this.tagInput = "";
      }
    },
    async submitPost() {
      if (this.fileList.length == 0) {
        alert("이미지를 등록해주세요.");
      } else if (!this.contentInput || this.contentInput.trim() === "") {
        alert("내용을 입력해주세요.");
      } else {
        const postData = {
          images: this.fileList,
          content: this.contentInput,
          hashtags: this.tagList,
        };
        await this.callPostSaveAPI(postData);
        this.navigateToMain();
      }
    },
    async callPostSaveAPI(postData) {
      let formData = new FormData();
      formData.append("content", postData.content);
      // 이미지 업로드 처리
      for (let i = 0; i < postData.images.length; i++) {
        formData.append("images", postData.images[i]);
      }
      for (let i = 0; i < postData.hashtags.length; i++) {
        formData.append("hashtags", postData.hashtags[i]);
      }
      var token = sessionStorage.getItem("token");
      await axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
                "Content-Type": "multipart/form-data",
              },
        method: "POST",
        data: formData,
      })
        .then((data) => {
          console.log("callPostSaveAPI " + data.data.postSeq);
        })
        .catch(() => {
          alert("게시글이 등록되지 않았습니다.");
        });
    },
    updateImg(file) {
      this.fileList = file;
    },
    navigateToMain() {
      this.$router.push("/post");
    },
  },
};
</script>
<style scoped>
.profile {
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
.post-write-button {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}
.active-button {
  width: 80px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
}
.inactive-button {
  width: 80px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #ffffff;
}
</style>
