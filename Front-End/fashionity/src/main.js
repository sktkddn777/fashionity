import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import Toast from "vue-toastification";

import "v-calendar/dist/style.css";

import { setupCalendar, Calendar, DatePicker } from "v-calendar";
import "v-calendar/style.css";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

import { library } from "@fortawesome/fontawesome-svg-core";

import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

import { faUserSecret } from "@fortawesome/free-solid-svg-icons";

import "vue-toastification/dist/index.css";

import { loadFonts } from "./fontAwesomeIcon";

import "bootstrap/dist/css/bootstrap.css";

import VCalendar from "v-calendar";
import "v-calendar/style.css";

library.add(faUserSecret);

loadFonts();

createApp(App)
  .use(router)
  .use(store)
  .use(vuetify)
  .use(Toast)
  .use(setupCalendar, {})
  .use(VCalendar, {})
  .component("font-awesome-icon", FontAwesomeIcon)
  .component("VCalendar", Calendar)
  .component("VDatePicker", DatePicker)
  .mount("#app");
