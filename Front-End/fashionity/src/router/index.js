import { createRouter, createWebHistory } from "vue-router";
import ProfilePage from "../components/pages/user/Profile.vue";
import ProfileView from "../views/ProfileView.vue";
import UserView from "../views/UserView.vue";
import UserRegister from "../components/pages/user/UserRegister.vue";
import UserLogin from "../components/pages/user/UserLogin.vue";

import PostView from "../components/pages/post/PostView.vue";
import PostList from "../components/pages/post/PostList.vue";
import ConsultantList from "@/components/pages/consultant/ConsultantList";
import ConsultantReservation from "@/components/pages/consultant/ConsultantReservation";
import ConsultantReservationDate from "@/components/pages/consultant/ConsultantReservationDate";
import ConsultantView from "@/components/pages/consultant/ConsultantView";
import PostDetail from "../components/pages/post/PostDetail.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "main",
      component: PostView,
      children: [
        {
          path: "/",
          name: "home",
          component: PostList,
        },
      ],
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
      ],
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
          name: "detail",
          component: PostDetail,
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
          name: "consultantList",
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
