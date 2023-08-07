import { createRouter, createWebHistory } from "vue-router";
import ProfilePage from "../components/pages/user/Profile.vue";
import ProfileView from "../views/ProfileView.vue";
import ConsultingPage from "../components/pages/consulting/Consulting-WebCam.vue";
import ConsultingView from "../views/Consulting-WebCam-View.vue";

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
      path: "/consulting",
      name: "Consulting-WebCam-View",
      component: ConsultingView,
      children: [
        {
          path: "",
          name: "ConsultingPage",
          component: ConsultingPage,
        },
      ],
    },
  ],
});

export default router;
