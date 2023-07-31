<template>
  <v-sheet width="300" class="mx-auto">
    <v-form ref="form">
      <v-text-field
        v-model="id"
        :counter="20"
        label="아이디를 입력하시오"
        required
      ></v-text-field>
      <span class="register-id-check"></span>
      <v-text-field
        v-model="password"
        :counter="20"
        label="비밀번호를 입력하시오"
        required
        :type="'password'"
        density="compact"
      ></v-text-field>
      <div class="d-flex flex-column">
        <v-btn
          color="blue"
          class="login-button"
          block
          @click="login"
        >
          로그인
        </v-btn>
      </div>
      <div>
        회원가입 | 아이디찾기 | 비밀번호찾기
      </div>
      <div class="flex-column">
        <v-btn
            color="#00BF18"
            block
        >
        <img src="@/assets/img/naver.png" style="height: 30px;"/> 
        <a>네이버로 로그인</a>
        </v-btn>
        <v-btn
            color="#FFE812"
            block
        >
        <img src="@/assets/img/kakao.png" style="height: 30px;"/>
        <a>카카오로 로그인</a>    
        </v-btn>
        <v-btn
            color="#FFFFFF"
            block
        >
        <img src="@/assets/img/google.png" style="height: 30px;"/>
        <a>구글로 로그인</a>
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
    password: "",
  }),
  
  methods: {
    async login() {
      if (!this.id) {
        toast.error("아이디가 비었따!!", {
          position: 'bottom-right',
          timeout: 2000
        })
        return;
      }

      if (!this.password) {
        toast.error("비밀번호가 비었따!!", {
          position: 'bottom-right',
          timeout: 2000
        })
        return;
      }
      axios({
        url: "http://localhost/api/v1/members/login",
        method: "POST",
        data: {
          id: this.id,
          password: this.password
        }
      }).then((data) => {
        console.log(data);
      }).catch((data) => {
        if (data.response.data.code === 'M001')
            toast.error("아이디가 없따!!", {
                position: 'bottom-right',
                timeout: 2000
            })
        else if (data.response.data.code === 'A001') {
            toast.error("비밀번호가 틀렸따!!", {
                position: 'bottom-right',
                timeout: 2000
            })
        }
      })
    },
  },

};
</script>