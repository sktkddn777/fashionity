<template>
  <div class="container-fluid">
    <the-nav-bar-mypage></the-nav-bar-mypage>
    <div class="profile-container">
      <!-- <div class="profile-title">프로필</div> -->
      <div class="profile-main">
        <div style = "display:flex; flex-direction: row; justify-content: center;">
          <!-- 사용자 프로필 사진 -->
          <div class="profile-main-photo">
            <img class="image-box" width="150" height="150" />
          </div>
          <div class = "infos" style = "display:flex; flex-direction: column;">
            <div class="profile-main-form" style = "display:flex; flex-direction: row; justify-content: start;">
            <v-form disabled class="d-flex align-self-center">
              <div class="profile-main-form-text" style = "display:flex; flex-direction: column; ">
                <!-- 프로필 정보 -->
                <div class="profile-main-form-text-nickname" style = "display: flex ; jus tify-self : start; ">
                  <!-- <div>닉네임</div> -->
                  <v-text-field
                    v-model="state.nickname"
                    density="compact"
                  ></v-text-field>
                </div>
                <div class="m-top-d" style = "width:20rem;" align = "left">
                  <v-text-field
                    v-model="state.profileIntro"
                    density="compact"
                    min = 0
                    max = 50 
                  ></v-text-field>
                </div>
                <div class="m-top-d" style = "display:flex; justify-content: start;">
                  <button class = "follow-button" onclick = "">손민수</button>
                  <button class = "unfollow-button">언민수</button>
                </div>
              </div>
            </v-form>
          </div>
          </div>

        </div>
        <div class = "profile-followings-info" style = "display:flex; flex-direction: row; justify-content: center;">
          <v-form>
              <div class="posts-cnt" style = "float: left; margin-left: 1rem; margin-right:1rem">
                <div><b>Posts</b></div>
                <div>PostsCnt</div>
              </div>
              <div class = "followers-cnt" style = "float: left;  margin-left: 1rem;  margin-right:1rem">
                <div><b>Followers</b></div>
                <div>FollowersCnt</div>
              </div>
              <div class = "followings-cnt" style = "float: left;  margin-left: 1rem; margin-right:1rem">
                <div><b>Followings</b></div>
                <div>FollowingsCnt</div>
              </div>
          </v-form>
        </div>
      </div>
    </div>
      <div
      class="row justify-content-center"
      style="
        margin-top: 2rem;
        margin-bottom: 30px;
        border-style: solid;
        border-width: 1px;
        border-color: white white #bdbdbd white;
      "
      ><div class="col col-lg-2 header-tab">
        <router-link to="/posts" style="text-decoration: none; color: #424242; font-size:1.2rem"
          >Posts</router-link
        >
      </div>
      <div class="col col-lg-2 header-tab point">
        <router-link to="/liked" style="text-decoration: none; color: #424242 ; font-size:1.2rem"
          >Liked</router-link
        >
      </div>
    </div>
    <div class="row" style="height: 30px"></div>

    <div class="container">
      <div class="row" style="justify-content: center">
        <div class="col">
          <the-post>post</the-post>
        </div>
        <div class="col">
          <the-post>post</the-post>
        </div>
        <div class="col">
          <the-post>post</the-post>
        </div>
        <div class="col">
          <the-post>post</the-post>
        </div>
      </div>

      <div class="row" style="height: 30px"></div>

      <div class="row">
        <div class="col">
          <the-post>post</the-post>
        </div>
        <div class="col">
          <the-post>post</the-post>
        </div>
        <div class="col">
          <the-post>post</the-post>
        </div>
        <div class="col">
          <the-post>post</the-post>
        </div>
      </div>

      <div class="row" style="height: 40px"></div>
    </div>


  </div>
</template>

<script>
import { onMounted } from "vue";
import { reactive } from "vue";
// import { useRouter } from "vue-router";
import TheNavBarMypage from "@/components/layout/TheNavBarMypage.vue";

export default {
  name: "ProfilePage",
  components: {
    TheNavBarMypage,
  },
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
  width: 250px;
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
  display: flex;
  flex-direction: column;
  align-items: center;
}
.follow-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191FF;
  color: #fffFFF;
}
.unfollow-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #fffFFF;
}
</style>
