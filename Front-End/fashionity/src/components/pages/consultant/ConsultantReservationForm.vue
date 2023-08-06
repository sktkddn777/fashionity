<template lang="">
  <div class="container-fluid" style="height: 65vh">
    <div class="row justify-content-center detail-title">상세 예약 정보 등록</div>
    <!-- 불러오기 버튼 -->
    <div class="row justify-content-end">
      <div class="col-1 detail-button">불러오기</div>
    </div>
    <v-form ref="form" v-model="isValid">
      <!-- 나이/성별 선택  -->
      <div class="row">
        <div class="col-2">
          <div class="row justify-content-center">나이</div>
          <div class="row justify-content-center">
            <v-text-field
              @keyup="validate"
              v-model="id"
              :counter="20"
              :rules="idRules"
              label="아이디를 입력하시오"
              required
            ></v-text-field>
          </div>
        </div>
        <div class="col">
          <v-text-field
            @keyup="validate"
            v-model="id"
            :counter="20"
            :rules="idRules"
            label="아이디를 입력하시오"
            required
          ></v-text-field>
        </div>
      </div>
      <!-- 신장, 몸무게 -->

      <div class="row justify-content-center">
        <div class="col-3">
          <div class="row justify-content-center">나이</div>
          <div class="col">
            <v-text-field
              @keyup="validate"
              v-model="id"
              :counter="20"
              :rules="idRules"
              label="아이디를 입력하시오"
              required
            ></v-text-field>
          </div>
        </div>

        <div class="col-3">
          <div class="row justify-content-center">나이</div>
          <div class="row justify-content-center">
            <v-text-field
              @keyup="validate"
              v-model="id"
              :counter="20"
              :rules="idRules"
              label="아이디를 입력하시오"
              required
            ></v-text-field>
          </div>
        </div>
      </div>

      <v-btn
        v-bind:disabled="!isValid"
        v-bind:color="!isValid ? 'grey' : 'black'"
        class="register-button"
        block
        @click="register"
      >
        회원가입
      </v-btn>
    </v-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      isValid: false,
    };
  },
  methods: {
    async validate() {
      const { valid } = await this.$refs.form.validate();
      if (valid) {
        this.isValid = true;
      } else {
        this.isValid = false;
      }
    },
  },
  created() {
    this.isVaild = false;
  },
};
</script>
<style scoped>
.detail-title {
  font-size: 20px;
}
.detail-button {
  border: solid 1px #424242;
}
</style>
