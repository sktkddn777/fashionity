<template>
  <v-sheet width="300" class="mx-auto">
    <v-form ref="form" lazy-validation @keyup="validate">
      <v-text-field
        v-model="id"
        :counter="10"
        :rules="idRules"
        label="아이디를 입력하시오"
        lazy-validation
        @keyup="checkId"
        required
      ></v-text-field>
      <span class="register-id-check"></span>
      <v-text-field
        v-model="password"
        :counter="10"
        :rules="passwordRules"
        label="비밀번호를 입력하시오"
        required
      ></v-text-field>
      <v-text-field
        v-model="nickname"
        :counter="10"
        :rules="nicknameRules"
        label="닉네임을 입력하시오"
        required
      ></v-text-field>
      <v-text-field
        v-model="email"
        :counter="10"
        :rules="emailRules"
        label="이메일을 입력하시오"
        required
      ></v-text-field>
      <v-checkbox
        v-model="checkbox"
        :rules="[(v) => !!v || '이용약관에 동의해주세요']"
        label="[필수] 만 14세 이상이며 모두 동의합니다."
        required
      ></v-checkbox>
      <div class="d-flex flex-column">
        <v-btn
          v-bind:disabled="isValidate == false"
          class="register-button"
          block
          @click="register"
        >
          회원가입
        </v-btn>
      </div>
    </v-form>
  </v-sheet>
</template>

<script>
import axios from "axios";
import { useToast } from "vue-toastification";
const toast = useToast();

export default {
  data: () => ({
    id: "",
    id_check: "",
    password: "",
    nickname: "",
    email: "",
    checkbox: "",
    isValidate: false,
    idRules: [
      (v) => !!v || "아이디를 입력해주세요",
      (v) => (v && v.length <= 10) || "Name must be less than 10 characters",
    ],
    passwordRules: [
      (v) => !!v || "비밀번호를 입력해주세요",
      (v) => (v && v.length <= 10) || "Name must be less than 10 characters",
    ],
    nicknameRules: [
      (v) => !!v || "닉네임을 입력해주세요",
      (v) => (v && v.length <= 10) || "Name must be less than 10 characters",
    ],
    emailRules: [
      (v) => !!v || "이메일을 입력해주세요",
      (v) => (v && v.length <= 10) || "Name must be less than 10 characters",
    ],
  }),

  methods: {
    async validate() {
      const { valid } = await this.$refs.form.validate();

      if (valid) this.isValidate = true;
    },
    register() {
      toast("hello");
    },

    async checkId() {
      // 모든 조건 만족해야 함
      const validate = await this.$refs.form.validate();
      if (validate.valid) {
        axios({
          url: "http://localhost/api/v1/members/validate",
          method: "GET",
          params: {
            id: this.id,
          },
        }).then((data) => {
          if (data.data) {
            this.id_check = `${this.id} 는 사용가능한 아이디입니다.`;
            document.querySelector(".register-id-check").innerHTML =
              this.id_check;
            document.querySelector(".register-button").classList.add("color");
          } else {
            this.id_check = `${this.id} 는 사용불가능한 아이디입니다.`;
            document.querySelector(".register-id-check").innerHTML =
              this.id_check;
            document
              .querySelector(".register-button")
              .classList.remove("color");
          }
          console.log(data);
        });
      }
    },
    async checkEmail() {},
  },
};
</script>
