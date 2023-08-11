import { apiInstance } from ".";

const api = apiInstance();

const authURL = process.env.VUE_APP_API_URL + "/api/v1/auth";

async function login(data, success, fail) {
  await api.post(`${authURL}/login`, data).then(success).catch(fail);
}

async function register(data, success, fail) {
  await api.post(`${authURL}/register`, data).then(success).catch(fail);
}

async function logout(success, fail) {
  await api
    .post(`${authURL}/logout`, {
      Headers: { Authorization: `Bearer ${sessionStorage.getItem("token")}` },
    })
    .then(success)
    .catch(fail);
}

async function tokenRegeneration(data, success, fail) {
  await api.post(`${authURL}/reissue`, data).then(success).catch(fail);
}

export { login, register, logout, tokenRegeneration };
