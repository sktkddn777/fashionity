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
import ConsultantMyList from "@/components/pages/consultant/ConsultantMyList";
import ConsultantReservationSubmit from "@/components/pages/consultant/ConsultantReservationSubmit";
import PostList from "../components/pages/post/PostList.vue";
import PostDetail from "../components/pages/post/PostDetail.vue";
import PostWrite from "../components/pages/post/PostWrite.vue";
import PostModify from "../components/pages/post/PostModify.vue";
import store from "@/store";
import ConsultingPage from "../components/pages/consulting/Consulting-WebCam.vue";
import ConsultingView from "../views/Consulting-WebCam-View.vue";
import ChattingPage from "../components/pages/consulting/TheChatting.vue";
import ProfileEdit from "@/components/pages/user/ProfileEdit";
import ProfileLiked from "@/components/pages/user/ProfileLiked";

import RConsultantMain from "../components/pages/consultant/consultantSide/RConsultantMain";
import ConsultantVue from "../components/pages/consultant/Consultant";
import RConsultantCheck from "../components/pages/consultant/consultantSide/RConsultantCheck";
import RConsultantCheckDetail from "../components/pages/consultant/consultantSide/RConsultantCheckDetail";
import RConsultantSet from "../components/pages/consultant/consultantSide/RConsultantSet";

const onlyAuthUser = async () => {
  const checkLoginUser = store.getters["memberStore/checkLoginUser"];
  const checkToken = store.getters["memberStore/checkToken"];

  if (checkLoginUser != null) {
    console.log("토큰 유효성 체크하러 가자!!!!");
    await store.dispatch("memberStore/getUserInfoAction");
  }
  if (!checkToken || checkLoginUser === null) {
    alert("로그인이 필요한 페이지입니다..");
    router.push({ name: "UserLogin" });
  } else {
    console.log("로그인 한 유저네용");
  }
};

const optionalAuthUser = async () => {
  const checkLoginUser = store.getters["memberStore/checkLoginUser"];
  const checkToken = store.getters["memberStore/checkToken"];

  if (checkLoginUser != null) {
    console.log("토큰 유효성 체크하러 가자!!!!");
    await store.dispatch("memberStore/getUserInfoAction");
  }

  if (!checkToken || checkLoginUser === null) {
    await store.dispatch("memberStore/logoutAction");
  }
};

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "main",
      redirect: "/post",
    },
    {
      path: "/profile",
      name: "profileView",
      component: ProfileView,
      children: [
        {
          path: ":nickname",
          name: "ProfilePage",
          beforeEnter: onlyAuthUser,
          component: ProfilePage,
        },
        {
          path: ":nickname/liked",
          name: "profileLiked",
          component: ProfileLiked,
        },
        {
          path: ":nickname/edit",
          name: "profileEdit",
          beforeEnter: onlyAuthUser,
          component: ProfileEdit,
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
      path: "/oauth/redirect",
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
          beforeEnter: optionalAuthUser,
          component: PostList,
        },
        {
          path: ":seq",
          name: "postDetail",
          component: PostDetail,
          props: true,
        },
        {
          path: "write",
          name: "postWrite",
          component: PostWrite,
        },
        {
          path: ":seq/modify",
          name: "postModify",
          component: PostModify,
        },
      ],
    },
    {
      path: "/consulting",
      name: "Consulting-WebCam-View",
      component: ConsultingView,
      children: [
        {
          path: "/meeting",
          name: "ConsultingPage",
          component: ConsultingPage,
        },
        {
          path: "/chatting",
          name: "TheChatting",
          component: ChattingPage,
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
          name: "ConsultantVue",
          component: ConsultantVue,
        },
        {
          path: "rc",
          name: "RConsultantMain",
          component: RConsultantMain,
          children: [
            {
              path: "",
              name: "RConsultantCheck",
              component: RConsultantCheck,
            },
            {
              path: "set",
              name: "RConsultantSet",
              component: RConsultantSet,
            },
            {
              path: "/detail/:value",
              name: "RConsultantCheckDetail",
              component: RConsultantCheckDetail,
              beforeEnter: optionalAuthUser,
              props: true,
            },
          ],
        },
        {
          path: "rm",
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
            {
              path: "confirm",
              name: "sumbit",
              component: ConsultantReservationSubmit,
            },
          ],
        },
        {
          path: "myreservation/list",
          name: "consultant-myreservation",
          component: ConsultantMyList,
        },
      ],
    },
  ],
});

export default router;
