<template>
  <div class="profile-container">
    <div class="profile-title">프로필</div>
    <div class="profile-main">
      <!-- 사용자 프로필 사진 -->
      <div class="profile-main-photo">
        <img class="image-box" width="200" height="200" />
      </div>
      <div class="profile-main-form">
        <v-form disabled>
          <div class="profile-main-form-text">
            <!-- 프로필 정보 -->
            <div class="profile-main-form-text-nickname">
              <div>닉네임</div>
              <v-text-field
                v-model="state.nickname"
                density="compact"
              ></v-text-field>
            </div>
            <div class="m-top-d">
              <div>한줄 소개</div>
              <v-text-field
                v-model="state.profileIntro"
                density="compact"
              ></v-text-field>
            </div>
            <div class="m-top-d">
              <div>손민수</div>
              <v-text-field density="compact">손민수</v-text-field>
            </div>
          </div>
        </v-form>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted } from "vue";
import { reactive } from "vue";
// import { useRouter } from "vue-router";

export default {
  name: "ProfilePage",
  setup() {
    // const router = useRouter();
    const state = reactive({
      model: null,
      nickname: "",
      profileIntro: "",
    });
    // const store = useStore(); -> vuex

    // 초기화면 세팅
    onMounted(() => {
      const previews = document.querySelectorAll(".image-box");
      state.nickname = "uzu_munzi";
      state.profileIntro = "우주먼지의 데일리룩 기록들";
      previews[0].src = require(`@/assets/img/panda.png`);
    });

    return {
      state,
    };
  },

  // components: {},
  // props: {},
  // data: () => ({}),
  // methods: {},
};
</script>

<style scoped>
.image-box {
  border-radius: 50%;
}
.profile_icon {
  font-size: 50px;
  color: white;
}
.upload-btn {
  display: inline-block;
  cursor: pointer;
}
input[type="file"] {
  display: none;
}

.select {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 110px;
  width: 110px;
  border-radius: 55px;
  background-color: #ff0a54;
}

.profile-main-button-text {
  margin-top: 3px;
}
.img_box {
  width: 200px;
  margin: 100px;
  background-color: red;
}
.hover_bigger {
  height: 110px;
  width: 110px;
}
.hover_bigger:hover {
  border-radius: 50%;
  height: 114px;
  width: 114px;
}
.m-top-d {
  margin-top: -5px;
}
.profile-input-icon:hover {
  cursor: pointer;
}
.profile-input-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 30px;
  position: relative;
  width: 49px;
  height: 49px;
  background-color: white;
  border: 1px solid rgb(209, 209, 209);
  top: -100px;
  left: 215px;
  box-shadow: 1px 1px 2px 0.3px;
}

.profile-main-button {
  margin-top: 20px;
  justify-content: space-between;
  width: 270px;
  height: 30px;
  display: flex;
}
.profile-main-photo {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 270px;
  height: 290px;
  object-fit: block;
}
.profile-main-photo:hover {
  cursor: pointer;
}
.profile-main-form-text {
  font-family: appleM;
  font-size: 14px;
}
.profile-main-form {
  width: 270px;
  height: 290px;
}
.profile-title {
  font-family: appleB;
  font-size: 50px;
  text-align: center;
  width: 300px;
  height: 80px;
}
.profile-main {
  margin-top: 15px;
  align-items: center;
  display: flex;
  justify-content: space-evenly;
  width: 100%;
  height: 300px;
}
.profile-photo {
  margin-top: -30px;
  width: 1000px;
  height: 200px;
}
.profile-container {
  font-family: appleB;
  margin-top: 95px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
