import { createRouter, createWebHistory } from "vue-router";
import ProfilePage from "../components/pages/user/Profile.vue";
import ProfileView from "../views/ProfileView.vue";
import UserView from "../views/UserView.vue";
import UserRegister from "../components/pages/user/UserRegister.vue";
import UserLogin from "../components/pages/user/UserLogin.vue";

import PostView from "../components/pages/post/PostList.vue";
import ConsultantList from "@/components/pages/consultant/ConsultantList";
import ConsultantReservation from "@/components/pages/consultant/ConsultantReservation";
import ConsultantReservationDate from "@/components/pages/consultant/ConsultantReservationDate";
import ConsultantView from "@/components/pages/consultant/ConsultantView";
import ConsultantReservationTime from "@/components/pages/consultant/ConsultantReservationTime";
import ConsultantReservationForm from "@/components/pages/consultant/ConsultantReservationForm";
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
              name: "date",
              component: ConsultantReservationDate,
            },
            {
              path: "time",
              name: "time",
              component: ConsultantReservationTime,
            },
            {
              path: "detail",
              name: "detail",
              component: ConsultantReservationForm,
            },
          ],
        },
      ],
    },
  ],
});

export default router;
