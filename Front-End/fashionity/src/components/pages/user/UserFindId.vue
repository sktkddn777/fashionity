<template>
  <div
    class="container-fluid d-flex align-items-center justify-content-center"
    style="height: 65vh"
  >
    <div class="row d-flex d-flex align-items-center justify-content-center">
      <div style="border-bottom-style: inset; width: 150px">아이디 찾기</div>
      <div style="height: 10vh"></div>
      <v-sheet width="300" class="mx-auto">
        <v-form ref="form">
          <v-text-field
            v-model="email"
            :rules="emailRules"
            label="이메일을 입력하시오"
            required
          ></v-text-field>
          <div class="d-flex flex-column">
            <v-btn
              v-bind:color="'blue'"
              class="findId-button"
              block
              @click="findIdByEmail"
            >
              확인
            </v-btn>
          </div>
        </v-form>
      </v-sheet>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import router from "@/router";
import { useToast } from "vue-toastification";

const toast = useToast();

export default {
  name: "UserFindId",
  data: () => ({
    email: "",
    email_check: "",
    checkbox: "",

    emailRules: [
      (v) => !!v || "이메일을 입력해주세요",
      (v) => {
        const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return pattern.test(v) || "이메일 형식에 맞지 않습니다";
      },
    ],
  }),

  methods: {
    async findIdByEmail() {
      const data = {
        email: this.email,
      };

      const emailValid = await this.checkEmail();
      if (emailValid) {
        toast.error("가입하지 않은 사용자입니다.", {
          position: "bottom-right",
          timeout: 2000,
        });
        return;
      }

      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/auth/find/id`,
        method: "POST",
        data: data,
      })
        .then(({ response }) => {
          console.log("[FindID] : " + response.data);
        })
        .catch(({ response }) => {
          if (response.data.code === "M001")
            console.log("[FindID error] : " + response.data.message);

          if (response.data.code === "C007")
            console.log("[FindID error] : " + response.data.message);
        });
      toast.success("아이디를 메일로 보냈습니다.");
      router.push({ name: "UserLogin" });
    },

    async checkEmail() {
      const validate = await this.$refs.form.validate();
      if (validate.valid) {
        return axios({
          url: `${process.env.VUE_APP_API_URL}/api/v1/auth/check/email`,
          method: "GET",
          params: {
            email: this.email,
          },
        }).then((data) => {
          return data.data ? true : false;
        });
      }
    },
  },
};
</script>
