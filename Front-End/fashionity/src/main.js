import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import Toast from "vue-toastification";
// import "v-calendar/dist/style.css";
// import VCalendar from "v-calendar";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

/* import the fontawesome core */
import { library } from "@fortawesome/fontawesome-svg-core";

/* import font awesome icon component */
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

/* import specific icons */
import { faUserSecret } from "@fortawesome/free-solid-svg-icons";

/* import toast css */
import "vue-toastification/dist/index.css";

import { loadFonts } from "./fontAwesomeIcon";
// import VueSidePanel from "vue3-side-panel";
// import "vue3-side-panel/dist/vue3-side-panel.css";
import "bootstrap/dist/css/bootstrap.css";
// import "bootstrap-vue-3/dist/bootstrap-vue-3.css";

/* add icons to the library */
library.add(faUserSecret);

loadFonts();

createApp(App)
  .use(router)
  .use(store)
  .use(vuetify)
  .use(Toast)
  .component("font-awesome-icon", FontAwesomeIcon)
  .mount("#app");
