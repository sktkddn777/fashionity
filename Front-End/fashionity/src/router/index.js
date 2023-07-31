import { createRouter, createWebHistory } from "vue-router";
import ProfilePage from "../components/pages/user/Profile.vue";
import ProfileView from "../views/ProfileView.vue";
import PostView from "../components/pages/post/PostView.vue";
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
      path: "/post",
      name: "postView",
      component: PostView,
      children: [
        {
          path: "",
          name: "home",
          component: ProfilePage,
        },
      ],
    },
  ],
});

export default router;
