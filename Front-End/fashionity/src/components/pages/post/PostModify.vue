<template lang="">
  <div id="post-detail" class="container-fluid">
    <div class="row">
      <div class="col"></div>
      <div class="col-6">
        <!-- 작성자 정보 -->
        <div class="post-detail-header">
          <div class="post-detail-header-img">
            <img
              :src="myProfileImg || '../img/unknown.e083a226.png'"
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
        <!-- 이미지 -->
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
          class="form-control content"
          rows="3"
          placeholder="내용을 입력해주세요."
          v-model="contentInput"
        ></textarea>
        <div class="post-write-button">
          <button
            type="button"
            class="btn btn-outline-dark"
            style="min-width: 70px"
          >
            <span style="font-size: smaller">&nbsp;취소&nbsp;</span>
          </button>
          <button
            type="button"
            class="btn btn-dark"
            style="min-width: 70px"
            @click="submitPost"
          >
            <span style="font-size: smaller">&nbsp;수정&nbsp;</span>
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
export default {
  data() {
    return {
      myNickname: this.$store.getters["memberStore/checkLoginUser"].nickname,
      myProfileImg:
        this.$store.getters["memberStore/checkLoginUser"].profileImage,
      tagInput: "",
      tagList: [],
      contentInput: "",
      imgList: [],
      fileList: [],
    };
  },
  computed: {
    seq() {
      return this.$route.params.seq;
    },
  },
  components: {
    MultiImageUpload,
  },
  methods: {
    addTag() {
      if (this.tagInput) {
        this.tagList.push(this.tagInput.substr(0, this.tagInput.length - 1));
        this.tagInput = "";
      }
    },
    async submitPost() {
      const postData = {
        images: this.fileList,
        content: this.contentInput,
        hashtag: this.tagList,
      };
      await this.callPostUpdateAPI(postData);
      this.navigateToMain();
    },
    async callPostUpdateAPI(postData) {
      let postSeq = this.$route.params.seq;

      console.log(postSeq);
      console.log(postData.images);
      console.log(postData.content);
      console.log(postData.hashtag);

      let formData = new FormData();
      formData.append("content", postData.content);
      for (let i = 0; i < postData.images.length; i++) {
        formData.append("images", postData.images[i]);
      }
      for (let i = 0; i < postData.hashtag.length; i++) {
        formData.append("hashtag", postData.hashtag[i]);
      }
      var token = sessionStorage.getItem("token");
      await axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${postSeq}`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
                "Content-Type": "multipart/form-data",
              },
        method: "PUT",
        data: formData,
      })
        .then((data) => {
          console.log("callPostUpdateAPI " + data.data.success);
        })
        .catch(() => {
          alert("실패!");
        });
    },
    updateImg(file) {
      this.fileList = file;
      console.log("파일임당", file);
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
.post-detail-image {
  margin-bottom: 10px;
}
.content {
  margin-top: 10px;
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
</style>
