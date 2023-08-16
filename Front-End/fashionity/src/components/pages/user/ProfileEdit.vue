<template>
  <div class="container-fluid">
    <the-nav-bar-mypage></the-nav-bar-mypage>
    <div class="block" style="height: 3rem"></div>
    <div class="container" style="flex-direction: column">
      <div class="edit-info row" style="justify-content: center" align="left">
        <div class="row">
          <form v-on:submit.prevent>
            <div>
              <div
                v-if="!displayProfileImageUpload"
                class="profileImg-buttons"
                style="
                  display: flex;
                  justify-content: center;
                  flex-direction: row;
                "
              >
                <div class="profile-main-photo">
                  <img
                    class="image-box"
                    width="200"
                    height="200"
                    :src="profileUrl || require('@/assets/img/unknown.png')"
                    alt=""
                    ref="profileImage"
                  />
                </div>
                <div
                  class="buttons"
                  style="display: flex; align-items: flex-end"
                >
                  <div class="filebox">
                    <button
                      class="active-button"
                      style="margin-right: 0.3rem"
                      @click="showImageUpload"
                    >
                      사진 변경
                      <!-- <label for="upload">사진 변경</label>
                      <input type="file" id = "upload" ref = "uploadInput" @change="handleImageChange"> -->
                    </button>
                  </div>
                  <button
                    class="inactive-button"
                    style="margin-right: 0.3rem"
                    @click="deleteProfileImage"
                  >
                    사진 삭제
                  </button>
                  <!-- <button class="change-password-button" style="margin-right: 0.3rem">
                    비밀번호 변경
                  </button> -->
                </div>
              </div>
            </div>
            <div v-if="displayProfileImageUpload">
              <!-- <profile-image-updated-vue /> -->
              <profile-image-updated-vue
                @updateImg="updateImg"
                @cancel-upload="showImageUpload"
              />
            </div>
            <div style="height: 5rem"></div>
            <h5><b>아이디</b></h5>
            <div>{{ id }}</div>
            <hr />
            <h5><b>이메일</b></h5>
            <div>{{ email }}</div>
            <hr />
            <div>
              <h5>
                <label for="nickname"><b>닉네임</b></label>
              </h5>
              <input id="nickname" type="text" v-model="nickname" />
              <hr />
            </div>
            <div>
              <h5>
                <label for="profileIntro"><b>한줄 소개</b></label>
              </h5>
              <input
                id="profileIntro"
                type="text"
                v-model="profileIntro"
                style="width: 500px"
              />
              <hr />
            </div>
            <br />
            <div style="display: flex; justify-content: flex-end">
              <button class="active-button" style="margin-right: 0.3rem">
                <input type="submit" value="수정하기" @click="editProfile" />
              </button>
              <button class="delete-button" @click="delteProfileAndLogout">
                탈퇴하기
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <div class=" " style="height: 2rem"></div>
</template>
<script>
import { mapState, mapGetters } from "vuex";
import { useCookies } from "vue3-cookies";
import TheNavBarMypage from "@/components/layout/TheNavBarMypage.vue";
import axios from "axios";
import memberStore from "@/store/modules/memberStore";
import ProfileImageUpdatedVue from "@/components/pages/shared/ProfileImageUpload.vue";
import store from "@/store";

const { cookies } = useCookies();

export default {
  name: "ProfileEdit",
  components: {
    TheNavBarMypage,
    ProfileImageUpdatedVue,
  },
  data() {
    return {
      profileUrl: "",
      id: "",
      nickname: "",
      email: "",
      profileIntro: "",
      displayProfileImageUpload: false,
      profileImage: "",
      fileList: [],
    };
  },
  created() {
    this.getProfile();
  },
  computed: {
    ...mapState(memberStore, ["isLogin", "loginUser"]),
    ...mapGetters(["checkUserInfo"]),
    // selectedImagePreview(){
    //   if (this.selectedImage){
    //     return URL.createObjectURL(this.selectedImage)
    //   }
    //   return "";
    // }
  },
  methods: {
    getProfile() {
      let token = sessionStorage.getItem("token");
      axios({
        method: "get",
        url: `${process.env.VUE_APP_API_URL}/api/v1/members`,
        headers: { Authorization: `Bearer ${token}` },
      }).then(({ data }) => {
        this.nickname = data.nickname;
        this.profileUrl = data.profileUrl;
        this.id = data.id;
        this.email = data.email;
        this.profileIntro = data.profileIntro;
        console.log(data);
      });
    },
    async deleteProfile() {
      let token = sessionStorage.getItem("token");
      alert("계정이 삭제됩니다.");
      axios({
        method: "put",
        url: `${process.env.VUE_APP_API_URL}/api/v1/members/delete`,
        headers: { Authorization: `Bearer ${token}` },
      }).then((data) => console.log(data.data.success));
    },
    async delteProfileAndLogout() {
      await this.deleteProfile();
      store.commit("memberStore/LOGOUT");
      store.commit("memberStore/SET_IS_VALID_TOKEN", false);
      cookies.remove("refreshToken");
      this.$router.push("/");
    },
    showImageUpload() {
      console.log("before: " + this.displayProfileImageUpload);
      this.displayProfileImageUpload = !this.displayProfileImageUpload;
      console.log("after: " + this.displayProfileImageUpload);
    },
    deleteProfileImage() {
      this.profileImage = "@/assets/img/unknown.png";
      this.profileUrl = null;
    },
    updateImg(file) {
      this.fileList = file;
      console.log("파일임당", file);
    },
    navigateToProfile() {
      console.log(this.nickname);
      this.$router.push(`/profile/${this.nickname}`);
    },
    async urlToFile(profileUrl) {
      if (profileUrl !== null) {
        const response = await fetch(profileUrl);
        const data = await response.blob();
        const ext = profileUrl.split(".").pop();
        const filename = profileUrl.split("/").pop();
        const metadata = { type: `image/${ext}` };
        return new File([data], filename, metadata);
      }
    },
    async editProfile() {
      console.log("fileList = ", this.fileList);
      const updatedProfile = {
        images: this.fileList,
        nickname: this.nickname,
        profileIntro: this.profileIntro,
      };
      await this.callProfileEditAPI(updatedProfile);
      this.navigateToProfile();
    },
    async callProfileEditAPI(updatedProfile) {
      let formData = new FormData();
      formData.append("nickname", updatedProfile.nickname);
      formData.append("profileIntro", updatedProfile.profileIntro);
      // 이미지 업로드 처리
      if (updatedProfile.images.length >= 1) {
        for (let i = 0; i < updatedProfile.images.length; i++) {
          console.log(
            "포문 안에 있는 postData images 입니다 : " +
              updatedProfile.images[i]
          );
          formData.append("profileImage", updatedProfile.images[i]);
        }
      }
      // else {
      //   formData.append("profileImage", this.urlToFile(this.profileUrl));
      // }

      for (let { key, value } of (formData.keys(), formData.values())) {
        console.log({ key, value });
      }

      let token = sessionStorage.getItem("token");
      await axios({
        method: "patch",
        url: `${process.env.VUE_APP_API_URL}/api/v1/members/edit`,
        data: formData,
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      })
        .then((data) => {
          console.log("=======성공성공========");
          console.log(data);
          console.log("Profile updated successfully!");
        })
        .catch((error) => {
          // console.log(formData)
          console.log("=======에러에러========");
          console.log(error);
        });
    },
  },
};
</script>
<style scoped>
.image-box {
  border-radius: 50%;
}
.profile-main-photo {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 300px;
  height: 200px;
  object-fit: block;
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
.change-password-button {
  width: 120px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #ffffff;
}
.delete-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #ed4141;
  color: #ffffff;
}
.filebox label {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
  margin-right: 0.3rem;
  text-align: center;
  line-height: 40px;
}
.filebox input[type="file"] {
  width: 0;
  height: 0;
  opacity: 0;
}
.black-bg {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
  display: flex;
  justify-content: center;
}
.imodal {
  width: 35%;
  height: 50%;
  background: white;
  border-radius: 10px;
  padding: 20px;
}
.close {
  position: absolute;
  right: 35%;
  font-size: 2rem;
  color: #bdbdbd;
}
</style>
