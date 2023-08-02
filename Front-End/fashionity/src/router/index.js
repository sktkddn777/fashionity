import { createRouter, createWebHistory } from "vue-router";
import ProfilePage from "../components/pages/user/Profile.vue";
import ProfileView from "../views/ProfileView.vue";
import UserView from "../views/UserView.vue";
import UserRegister from "../components/pages/user/UserRegister.vue";
import UserLogin from "../components/pages/user/UserLogin.vue";

import PostView from "../components/pages/post/PostList.vue";
import ConsultantView from "../components/pages/consultant/ConsultantList";

const router = createRouter({
  history: createWebHistory(),
  routes: [
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
          component: ConsultantView,
        },
      ],
    },
  ],
});

export default router;
