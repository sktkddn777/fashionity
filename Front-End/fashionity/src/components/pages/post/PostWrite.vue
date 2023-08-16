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
      console.log("taglist = " + this.tagList);
      console.log("fileList = " + this.fileList);
      const postData = {
        images: this.fileList,
        content: this.contentInput,
        hashtags: this.tagList,
      };
      await this.callPostSaveAPI(postData);
      this.navigateToMain();
    },
    async callPostSaveAPI(postData) {
      let formData = new FormData();
      formData.append("content", postData.content);
      // formData.append("hashtags", JSON.stringify(postData.hashtags));
      console.log(
        "API postData = " + postData.images + " " + postData.images.length
      );

      // 이미지 업로드 처리
      for (let i = 0; i < postData.images.length; i++) {
        console.log(
          "포문 안에 있는 postData images 입니다 : " + postData.images[i]
        );
        formData.append("images", postData.images[i]);
      }
      console.log("포문 밖에 있는 formData images 입니다 : " + formData.images);
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
</style>
