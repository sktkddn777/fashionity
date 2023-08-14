<template>
  <div class="container-fluid">
    <the-nav-bar-mypage></the-nav-bar-mypage>
    <div>
      <div
        class="profileImg-buttons"
        style="display: flex; justify-content: center; flex-direction: row"
      >
        <div class="profile-main-photo">
          <img
            class="image-box"
            width="200"
            height="200"
            :src="profileUrl"
            alt=""
          />
        </div>
        <div class="buttons" style="display: flex; align-items: flex-end">
          <button class="active-button" style="margin-right: 0.3rem">
            사진 변경
          </button>
          <button class="inactive-button" style="margin-right: 0.3rem">
            사진 삭제
          </button>
          <!-- <button class="change-password-button" style="margin-right: 0.3rem">
            비밀번호 변경
          </button> -->
        </div>
      </div>
    </div>
    <div class="block" style="height: 3rem"></div>
    <div class="container" style="flex-direction: column">
      <div class="edit-info row" style="justify-content: center" align="left">
        <div class="row">
          <form v-on:submit.prevent="editProfile">
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
          </form>
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row" style="width: 85vw">
        <!-- <div class="p" style="background-color: red"></div> -->
        <div class="p-2 justify-content-end" style="text-align: end">
          <button
            class="active-button"
            style="margin-right: 0.3rem"
            @click="editProfile"
          >
            수정하기
          </button>
          <button class="delete-button" @click="deleteProfile">탈퇴하기</button>
        </div>
        <div class="p-2 d-flex justify-content-start">
          <!-- <button class="delete-button">탈퇴하기</button> -->
        </div>
      </div>

      <!-- <div class="buttons col" style="align-items: flex-end" align="right">
          
        </div> -->
    </div>
  </div>
  <div class=" " style="height: 2rem"></div>
</template>
<script>
import TheNavBarMypage from "@/components/layout/TheNavBarMypage.vue";
import axios from "axios";

let token = sessionStorage.getItem("token");

export default {
  name: "ProfileEdit",
  components: {
    TheNavBarMypage,
  },
  data() {
    return {
      profileUrl: "",
      id: "",
      nickname: "",
      email: "",
      profileIntro: "",
    };
  },
  created() {
    this.getProfile();
  },
  methods: {
    async submitProfile() {},
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
    async editProfile() {
      const updatedProfile = {
        profileImage: this.profileUrl,
        nickname: this.nickname,
        profileIntro: this.profileIntro,
      };
      axios({
        method: "post",
        url: `${process.env.VUE_APP_API_URL}/api/v1/members/edit`,
        data: updatedProfile,
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((data) => {
          console.log(data);
          console.log("Profile updated successfully!");
        })
        .catch((error) => {
          console.log(updatedProfile);
          console.log(error);
        });
    },
    async deleteProfile() {
      alert("삭제됩니다");
      axios({
        method: "delete",
        url: `${process.env.VUE_APP_API_URL}/api/v1/members/delete`,
        headers: { Authorization: `Bearer ${token}` },
      }).then((data) => console.log(data.success));
    },
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
</style>
