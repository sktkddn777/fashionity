import axios from "axios";

axios.defaults.withCredentials = true;

function apiInstance() {
  const instance = axios.create({
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

export { apiInstance };
