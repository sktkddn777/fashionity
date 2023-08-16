<template>
  <div class="container-fluid">
    <the-nav-bar-mypage></the-nav-bar-mypage>
    <div class="block" style="height: 3rem"></div>
    <div class="container" style="flex-direction: column">
      <div
        class="black-bg"
        v-if="displayProfileImageUpload"
        style="z-index: 1050"
      >
        <div class="imodal">
          <div>
            <button id="followingsbtn" class="close" @click="showImageUpload">
              ×
            </button>
            <profile-image-updated-vue />
          </div>
        </div>
      </div>
      <div class="edit-info row" style="justify-content: center" align="left">
        <div class="row">
          <form v-on:submit.prevent="editProfile">
            <div>
              <div
                class="profileImg-buttons"
                style="
                  display: flex;
                  justify-content: center;
                  flex-direction: row;
                "
              >
                <div
                  class="profile-main-photo"
                  v-if="!displayProfileImageUpload"
                >
                  <img
                    class="image-box"
                    width="200"
                    height="200"
                    :src="profileUrl"
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
                  <button class="inactive-button" style="margin-right: 0.3rem">
                    사진 삭제
                  </button>
                  <!-- <button class="change-password-button" style="margin-right: 0.3rem">
                    비밀번호 변경
                  </button> -->
                </div>
              </div>
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
                <input type="submit" value="수정하기" />
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
// import FormData from "form-data";
import TheNavBarMypage from "@/components/layout/TheNavBarMypage.vue";
import axios from "axios";
import memberStore from "@/store/modules/memberStore";
import ProfileImageUpdatedVue from "@/components/pages/shared/ProfileImageUpload.vue";
import store from "@/store";

let token = sessionStorage.getItem("token");

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
      this.displayProfileImageUpload = !this.displayProfileImageUpload;
    },
    // async fetchImageAsFile(profileUrl) {
    //   if(profileUrl !== null){
    //     const response = await fetch(profileUrl);
    //     const data = await response.blob();
    //     const ext = profileUrl.split("/").pop();
    //     const filename = profileUrl.split("/").pop()
    //     const metadata = {type:`image/${ext}`}
    //     return new File([data],filename, metadata);

    //   }
    // },
    // async editProfile() {
    //   // const updatedProfile = {
    //   //   profileImage: this.selectedImage,
    //   //   nickname: this.nickname,
    //   //   profileIntro: this.profileIntro,
    //   // };
    //   const formData = new FormData();
    //   if (this.selectedImage !== null) {
    //     formData.append("profileImage", this.selectedImage, this.selectedImage.name);
    //   }
    //   else {
    //     const file = await this.fetchImageAsFile(this.profileUrl)
    //     formData.append("profileImage", file)}
    //   // formData.append("profileImage", this.selectedImage);
    //   formData.append("nickname", this.nickname);
    //   formData.append("profileIntro", this.profileIntro);
    //   console.log(this.selectedImage)
    //   console.log(this.nickname)
    //   console.log(this.profileIntro)

    //   for (let key of formData.keys()){
    //     console.log(key)
    //   }
    //   for (let value of formData.values()){
    //     console.log(value)
    //   }

    //   axios({
    //     method: "post",
    //     url: `${process.env.VUE_APP_API_URL}/api/v1/members/edit`,
    //     data: formData,
    //     headers: { Authorization: `Bearer ${token}`, "Content-Type": "multipart/form-data"},
    //   })
    //     .then((data) => {
    //       console.log("=======성공성공========");
    //       console.log(data);
    //       console.log("Profile updated successfully!");
    //     })
    //     .catch((error) => {
    //       // console.log(formData)
    //       console.log("=======에러에러========");
    //       console.log(error);
    //     });
    // },

    // handleImageChange(event) {
    //   this.selectedImage = event.target.files[0];

    //   if (this.selectedImage) {
    //     const reader = new FileReader();

    //     reader.onload = (event) => {
    //       this.profileUrl = event.target.result;
    //     };

    //     reader.readAsDataURL(this.selectedImage);
    //   }

    //   // Clear the input value to allow selecting the same image again
    //   this.$refs.uploadInput.value = "";
    // },
  },
};
</script>
<style>
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
