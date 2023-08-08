import { createRouter, createWebHistory } from "vue-router";
import ProfilePage from "@/components/pages/user/Profile";
import ProfileView from "@/views/ProfileView";
import UserView from "@/views/UserView";
import UserRegister from "@/components/pages/user/UserRegister";
import UserLogin from "@/components/pages/user/UserLogin";
import UserFindId from "@/components/pages/user/UserFindId";
import UserReissuePw from "@/components/pages/user/UserReissuePw";
import Oauth2Redirect from "@/components/pages/oauth2/Oauth2Redirect";
import PostView from "../components/pages/post/PostView.vue";
import ConsultantList from "@/components/pages/consultant/ConsultantList";
import ConsultantReservation from "@/components/pages/consultant/ConsultantReservation";
import ConsultantReservationDate from "@/components/pages/consultant/ConsultantReservationDate";
import ConsultantView from "@/components/pages/consultant/ConsultantView";
import ConsultantReservationTime from "@/components/pages/consultant/ConsultantReservationTime";
import ConsultantReservationForm from "@/components/pages/consultant/ConsultantReservationForm";
import PostList from "../components/pages/post/PostList.vue";
import PostDetail from "../components/pages/post/PostDetail.vue";
import PostWrite from "../components/pages/post/PostWrite.vue";
import PostModify from "../components/pages/post/PostModify.vue";
// import store from "@/store";

// const onlyAuthUser = async (to, from, next) => {
//   const checkUserInfo = store.getters["memberStore/checkUserInfo"];
//   const checkToken = store.getters["memberStore/checkToken"];
//   let token = sessionStorage.getItem("access-token");
//   console.log("로그인 처리 전", checkUserInfo,  token);

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
        {
          path: "/liked",
          name: "likedList",
          // component: ,
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
        {
          path: "findId",
          name: "UserFindId",
          component: UserFindId,
        },
        {
          path: "findPw",
          name: "UserReissuePw",
          component: UserReissuePw,
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
          name: "postList",
          component: PostList,
        },
        {
          path: "detail",
          name: "postDetail",
          component: PostDetail,
        },
        {
          path: "write",
          name: "postWrite",
          component: PostWrite,
        },
        {
          path: "modify",
          name: "postModify",
          component: PostModify,
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
          name: "consultantview",
          component: ConsultantList,
        },
        {
          path: "reservation",
          name: "reservation",
          component: ConsultantReservation,
          children: [
            {
              path: "",
              name: "consultantDate",
              component: ConsultantReservationDate,
            },
            {
              path: "time",
              name: "consultantTime",
              component: ConsultantReservationTime,
            },
            {
              path: "detail",
              name: "consultantDetail",
              component: ConsultantReservationForm,
            },
          ],
        },
      ],
    },
  ],
});

export default router;
