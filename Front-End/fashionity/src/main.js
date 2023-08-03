import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
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

import "v-calendar/dist/style.css";
import VCalendar from "v-calendar";

/* add icons to the library */
library.add(faUserSecret);

createApp(App)
  .use(router)
  .use(vuetify)
  .use(Toast)
  .use(VCalendar, {})
  .component("font-awesome-icon", FontAwesomeIcon)
  .mount("#app");
