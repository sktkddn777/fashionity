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
        <multi-image-upload @uploadImg="uploadImg"></multi-image-upload>
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
      images: [],
    };
  },
  components: {
    MultiImageUpload,
  },
  methods: {
    addTag() {
      if (this.tagInput) {
        this.tagList.push(this.tagInput);
        this.tagInput = "";
      }
    },
    submitPost() {
      const postData = {
        images: [],
        content: this.contentInput,
        hashtags: this.tagList,
      };
      this.callPostSaveAPI(postData);
    },
    callPostSaveAPI(postData) {
      let token = sessionStorage.getItem("token");
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "POST",
        data: postData,
      }).then((data) => {
        console.log(data.data.postSeq);
      });
    },
    uploadImg(images) {
      this.images = images;
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
