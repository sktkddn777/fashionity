import { apiInstance } from ".";

const api = apiInstance();

const baseURL = "http://localhost:8080/api/v1/auth";
async function login(data, success, fail) {
  await api.post(`${baseURL}/login`, data).then(success).catch(fail);
}

export { login };

// async function login() {
//   axios({
//     url: URL,
//     method: "POST",
//     data: {
//       id: user.id,
//       password: user.pw,
//     },
//   })
//     .then((res) => {
//       sessionStorage.setItem("token", res.data["accessToken"]);
//       sessionStorage.setItem("id", res.data["memberSeq"]);
//       commit("LOGIN", res.data);
//     })
//     .catch((res) => {
//       if (res.response.data.code === "M001")
//         toast.error("아이디가 없따!!", {
//           position: "bottom-right",
//           timeout: 2000,
//         });
//       else if (res.response.data.code === "A001") {
//         toast.error("비밀번호가 틀렸따!!", {
//           position: "bottom-right",
//           timeout: 2000,
//         });
//       }
//     });
// }
