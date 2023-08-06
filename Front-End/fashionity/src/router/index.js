import { createRouter, createWebHistory } from "vue-router";
import ProfilePage from "../components/pages/user/Profile.vue";
import ProfileView from "../views/ProfileView.vue";
import UserView from "../views/UserView.vue";
import UserRegister from "../components/pages/user/UserRegister.vue";
import UserLogin from "../components/pages/user/UserLogin.vue";
import Oauth2Redirect from "../components/pages/oauth2/Oauth2Redirect.vue";
import PostView from "../components/pages/post/PostList.vue";
import ConsultantList from "@/components/pages/consultant/ConsultantList";
import ConsultantReservation from "@/components/pages/consultant/ConsultantReservation";
import ConsultantReservationDate from "@/components/pages/consultant/ConsultantReservationDate";
import ConsultantView from "@/components/pages/consultant/ConsultantView";

// import store from "@/store";

// const onlyAuthUser = async (to, from, next) => {
//   const checkUserInfo = store.getters["memberStore/checkUserInfo"];
//   const checkToken = store.getters["memberStore/checkToken"];
//   let token = sessionStorage.getItem("access-token");
//   console.log("로그인 처리 전", checkUserInfo, token);

//   if (checkUserInfo != null && token) {
//     console.log("토큰 유효성 체크하러 가자!!!!");
//     await store.dispatch("memberStore/getUserInfo", token);
//   }
//   if (!checkToken || checkUserInfo === null) {
//     alert("로그인이 필요한 페이지입니다..");
//     // next({ name: "login" });
//     router.push({ name: "userlogin" });
//   } else {
//     console.log("로그인 했다!!!!!!!!!!!!!.");
//     next();
//   }
// };

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "main",
      component: PostView,
    },
    {
      path: "/profile",
      name: "profileView",
      component: ProfileView,
      children: [
        {
          path: "",
          name: "ProfilePage",
          component: ProfilePage,
        },
      ],
    },

    {
      path: "/user",
      name: "userView",
      component: UserView,
      children: [
        {
          path: "register",
          name: "UserRegister",
          component: UserRegister,
        },
        {
          path: "login",
          name: "UserLogin",
          component: UserLogin,
        },
        {
          path: "logout",
          name: "UserLogout",
        },
      ],
    },
    {
      path: "/oauth2/redirect",
      component: Oauth2Redirect,
    },

    {
      path: "/post",
      name: "postView",
      component: PostView,
      children: [
        {
          path: "",
          name: "home",
          component: PostView,
        },
      ],
    },

    {
      path: "/consultant",
      name: "consultantView",
      component: ConsultantView,
      children: [
        {
          path: "",
          name: "home",
          component: ConsultantList,
        },
        {
          path: "reservation",
          name: "reservation",
          component: ConsultantReservation,
          children: [
            {
              path: "date",
              name: "date",
              component: ConsultantReservationDate,
            },
          ],
        },
        // {
        //   path: "detail",
        //   name: "detail",
        //   component: ConsultantDetailDate,
        // }
      ],
    },
  ],
});

export default router;
