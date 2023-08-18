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
                    </button>
                  </div>
                  <button
                    class="inactive-button"
                    style="margin-right: 0.3rem"
                    @click="deleteProfileImage"
                  >
                    사진 삭제
                  </button>
                </div>
              </div>
            </div>
            <div v-if="displayProfileImageUpload">
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
              <input
                id="nickname"
                type="text"
                v-model="nickname"
                @input="checkNicknameLength"
              />
              <p v-if="isNicknameTooLong" class="warning" style="color: red">
                닉네임이 너무 깁니다. 최대 13자까지 입력 가능합니다.
              </p>
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
import { mapState, mapGetters, mapActions } from "vuex";
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
      isNicknameTooLong: false,
      isValid: true,
    };
  },
  created() {
    this.getProfile();
  },
  computed: {
    ...mapState(memberStore, ["isLogin", "loginUser"]),
    ...mapGetters(["checkUserInfo"]),
  },
  methods: {
    ...mapActions("memberStore", ["updateUserInfoAction"]),
    checkNicknameLength() {
      if (this.nickname.length >= 13) {
        this.isNicknameTooLong = true;
        this.isValid = false;
      } else {
        this.isNicknameTooLong = false;
        this.isValid = true;
      }
    },
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
        if (data.profileIntro != null) {
          this.profileIntro = data.profileIntro;
        }
      });
    },
    async deleteProfile() {
      let token = sessionStorage.getItem("token");
      alert("계정이 삭제됩니다.");
      axios({
        method: "put",
        url: `${process.env.VUE_APP_API_URL}/api/v1/members/delete`,
        headers: { Authorization: `Bearer ${token}` },
      })
        .then(() => {})
        .catch((error) => {
          if (error.resopnse.status === 401) {
            alert(error.response.data.message);
          }
        });
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
    deleteProfileImage() {
      this.profileImage = "@/assets/img/unknown.png";
      this.profileUrl = null;
    },
    updateImg(file) {
      this.fileList = file;
    },
    navigateToProfile() {
      this.$router.push(`/profile/${this.nickname}`);
    },
    editProfile() {
      if (!this.isValid) {
        alert("값을 다시 확인해주세요");
        return;
      }
      const updatedProfile = {
        images: this.fileList,
        nickname: this.nickname,
        profileIntro: this.profileIntro,
      };
      this.callProfileEditAPI(updatedProfile);
    },
    async callProfileEditAPI(updatedProfile) {
      let formData = new FormData();
      formData.append("nickname", updatedProfile.nickname);
      formData.append("profileIntro", updatedProfile.profileIntro);

      // 이미지 업로드 처리
      if (updatedProfile.images.length >= 1) {
        for (let i = 0; i < updatedProfile.images.length; i++) {
          formData.append("profileImage", updatedProfile.images[i]);
        }
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
          this.updateUserInfoAction(data.data);
          this.navigateToProfile();
        })
        .catch((error) => {
          if (error.response.status === 400) {
            alert(error.response.data.message);
          } else {
            alert("서버 에러");
          }
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
