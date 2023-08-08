<template lang="">
  <div id="reservation-form" class="container-fluid scroll">
    <div class="row justify-content-center detail-title">
      상세 예약 정보 등록
    </div>
    <!-- 불러오기 버튼 -->
    <div class="row justify-content-end">
      <div class="col">
        <div class="detail-button">불러오기</div>
      </div>
    </div>

    <!-- 예약 정보 form  -->
    <div class="row">
      <form action="">
        <!-- 나이, 성별 -->
        <div class="row justify-content-center">
          <div class="col">나이</div>
          <div class="col">성별</div>
        </div>
        <div class="row justify-content-center">
          <div class="col">
            <input type="text" class="input-form" />
          </div>
          <div class="col">
            <fieldset>
              <label>
                <input type="radio" name="gender" value="m" checked />
                <span>남성</span>
              </label>

              <label>
                <input type="radio" name="gender" value="f" />
                <span>여성</span>
              </label>
            </fieldset>
          </div>
        </div>

        <div class="row" style="height: 40px"></div>

        <!-- 신장, 몸무게 -->
        <div class="row justify-content-center">
          <div class="col">신장</div>
          <div class="col">몸무게</div>
        </div>
        <div class="row justify-content-center">
          <div class="col">
            <input type="text" class="input-form" />
          </div>
          <div class="col">
            <input type="text" class="input-form" />
          </div>
        </div>

        <!-- 평소 나의 스타일 등록 -->
        <div class="row">
          <multi-image-upload></multi-image-upload>
        </div>
      </form>
    </div>
    <!-- <v-form ref="form" v-model="isValid">
  
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
      </div> -->

    <!-- <div class="row"> -->
    <!-- <v-btn
          v-bind:disabled="!isValid"
          v-bind:color="!isValid ? 'grey' : 'black'"
          class="register-button"
          block
          @click="register"
        >
          회원가입
        </v-btn> -->
    <!-- </div> -->
    <!-- </v-form> -->
  </div>
</template>
<script>
import MultiImageUpload from "../shared/MultiImageUpload.vue";

export default {
  data() {
    return {
      isValid: false,
    };
  },
  components: {
    MultiImageUpload,
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
#reservation-form {
  overflow: scroll;
  height: 300px;
  height: 65vh;
  margin-left: 4vw;
  padding-right: 5vw;
}
.detail-title {
  font-size: 20px;
}
.detail-button {
  width: 70px;
  font-size: 15px;
  color: #424242;
  background-color: #bdbdbd;
}

.scroll::-webkit-scrollbar {
  display: none;
}

.scroll {
  -ms-overflow-style: none; /* 인터넷 익스플로러 */
  scrollbar-width: none; /* 파이어폭스 */
}

.input-form {
  border: none;
  border-bottom: 1px solid black; /* 밑줄 스타일 지정 */
  padding: 5px;
}

label {
  font-size: 18px;
  line-height: 2rem;
  padding: 0.2em 0.4em;
}

[type="radio"],
span {
  vertical-align: middle;
}

[type="radio"] {
  appearance: none;
  border: max(2px, 0.1em) solid gray;
  border-radius: 50%;
  width: 1.25em;
  height: 1.25em;
  transition: border 0.5s ease-in-out;
}

[type="radio"]:checked {
  border: 0.4em solid #424242;
}

[type="radio"]:focus-visible {
  outline-offset: max(2px, 0.1em);
  outline: max(2px, 0.1em) dotted #424242;
}

[type="radio"]:hover {
  box-shadow: 0 0 0 max(4px, 0.2em) lightgray;
  cursor: pointer;
}

[type="radio"]:hover + span {
  cursor: pointer;
}

[type="radio"]:disabled {
  background-color: lightgray;
  box-shadow: none;
  opacity: 0.7;
  cursor: not-allowed;
}

[type="radio"]:disabled + span {
  opacity: 0.7;
  cursor: not-allowed;
}

/* Global CSS */
fieldset {
  display: flex;
  justify-content: center;
  border: none;
}

*,
*::before,
*::after {
  box-sizing: border-box;
}
</style>
