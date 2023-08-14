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
          <button class="change-password-button" style="margin-right: 0.3rem">
            비밀번호 변경
          </button>
        </div>
      </div>
    </div>

    <div class="edit-info" style="justify-content: center" align="left">
      <div>아이디</div>
      <div><input v-model="id" style="width: 300px" /></div>
      <div>닉네임</div>
      <div><input v-model="nickname" style="width: 300px" /></div>
      <div>이메일</div>
      <div><input v-model="email" style="width: 300px" /></div>
      <div>한줄소개</div>
      <div>
        <textarea v-model="profileIntro" style="width: 300px"></textarea>
      </div>
    </div>

    <button>수정하기</button>
    <button>탈퇴하기</button>
  </div>
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
    editProfile() {},
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
</style>
