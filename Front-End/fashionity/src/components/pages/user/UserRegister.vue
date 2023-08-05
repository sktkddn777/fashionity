<template>
  <div
    class="container-fluid d-flex align-items-center justify-content-center"
    style="height: 65vh"
  >
    <div class="row d-flex d-flex align-items-center justify-content-center">
      <v-sheet width="300" class="mx-auto">
        <v-form ref="form" v-model="isValid">
          <v-text-field
            @keyup="validate"
            v-model="id"
            :counter="20"
            :rules="idRules"
            label="아이디를 입력하시오"
            required
          ></v-text-field>
          <span class="register-id-check"></span>
          <v-text-field
            @keyup="validate"
            v-model="password"
            :counter="20"
            :rules="passwordRules"
            label="비밀번호를 입력하시오"
            required
          ></v-text-field>
          <span class="register-password-check"></span>
          <v-text-field
            @keyup="validate"
            v-model="nickname"
            :counter="12"
            :rules="nicknameRules"
            label="닉네임을 입력하시오"
            required
          ></v-text-field>
          <span class="register-nickname-check"></span>
          <v-text-field
            @keyup="validate"
            v-model="email"
            :rules="emailRules"
            label="이메일을 입력하시오"
            required
          ></v-text-field>
          <span class="register-email-check"></span>
          <v-checkbox
            @keyup="validate"
            v-model="checkbox"
            :rules="[(v) => !!v || '이용약관에 동의해주세요']"
            label="[필수] 만 14세 이상이며 모두 동의합니다."
            required
          ></v-checkbox>
          <div class="d-flex flex-column">
            <v-btn
              v-bind:disabled="!isValid"
              v-bind:color="!isValid ? 'grey' : 'blue'"
              class="register-button"
              block
              @click="register"
            >
              회원가입
            </v-btn>
          </div>
        </v-form>
      </v-sheet>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapActions } from "vuex";
import { useToast } from "vue-toastification";
const memberStore = "memberStore";
const toast = useToast();

export default {
  name: "UserRegister",
  data: () => ({
    id: "",
    id_check: "",
    password: "",
    password_check: "",
    nickname: "",
    nickname_check: "",
    email: "",
    email_check: "",
    checkbox: "",
    isValid: false,

    idRules: [
      (v) => !!v || "아이디를 입력해주세요",
      (v) => (v && v.length >= 6) || "아이디는 6자 이상으로 입력해주세요",
      (v) => (v && v.length <= 20) || "아이디는 20자 이하로 입력해주세요",
      (v) => {
        const pattern = /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,20}$/;
        return pattern.test(v) || "아이디는 영문, 숫자 혼합";
      },
    ],
    passwordRules: [
      (v) => !!v || "비밀번호를 입력해주세요",
      (v) => (v && v.length >= 8) || "비밀번호는 8자 이상으로 입력해주세요",
      (v) => (v && v.length <= 20) || "비밀번호는 20자 이하로 입력해주세요",
      (v) => {
        const pattern =
          /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$/;
        return pattern.test(v) || "비밀번호는 영문, 숫자, 특수문자 혼합";
      },
    ],
    nicknameRules: [
      (v) => !!v || "닉네임을 입력해주세요",
      (v) => (v && v.length >= 4) || "닉네임은 4자 이상으로 입력해주세요",
      (v) => (v && v.length <= 12) || "닉네임은 12자 이하로 입력해주세요",
      (v) => {
        const pattern = /^[a-zA-Z가-힣0-9]{4,12}$/;
        return pattern.test(v) || "닉네임은 영문, 숫자";
      },
    ],
    emailRules: [
      (v) => !!v || "이메일을 입력해주세요",
      (v) => {
        const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return pattern.test(v) || "이메일 형식에 맞지 않습니다";
      },
    ],
  }),

  methods: {
    ...mapActions(memberStore, ["registerAction"]),
    async validate() {
      const { valid } = await this.$refs.form.validate();
      if (valid) {
        this.isValid = true;
      } else {
        this.isValid = false;
      }
    },
    async register() {
      const idValid = await this.checkId();
      const nicknameValid = await this.checkNickname();
      const emailValid = await this.checkEmail();

      if (!idValid) {
        toast.error("아이디가 중복되었따!!", {
          position: "bottom-right",
          timeout: 2000,
        });
        return;
      }

      if (!emailValid) {
        toast.error("이메일이 중복되었따!!", {
          position: "bottom-right",
          timeout: 2000,
        });
        return;
      }

      if (!nicknameValid) {
        toast.error("닉네임이 중복되었따!!", {
          position: "bottom-right",
          timeout: 2000,
        });
        return;
      }

      const user = {
        id: this.id,
        password: this.password,
        nickname: this.nickname,
        email: this.email,
      };
      await this.registerAction(user);
    },

    async checkId() {
      const validate = await this.$refs.form.validate();
      if (validate.valid) {
        return axios({
          url: "http://localhost:8080/api/v1/auth/check/id",
          method: "GET",
          params: {
            id: this.id,
          },
        }).then((data) => {
          return data.data ? true : false;
        });
      }
    },

    async checkEmail() {
      const validate = await this.$refs.form.validate();
      if (validate.valid) {
        return axios({
          url: "http://localhost:8080/api/v1/auth/check/email",
          method: "GET",
          params: {
            email: this.email,
          },
        }).then((data) => {
          return data.data ? true : false;
        });
      }
    },
    async checkNickname() {
      const validate = await this.$refs.form.validate();
      if (validate.valid) {
        return axios({
          url: "http://localhost:8080/api/v1/auth/check/nickname",
          method: "GET",
          params: {
            nickname: this.nickname,
          },
        }).then((data) => {
          return data.data ? true : false;
        });
      }
    },
  },
};
</script>
