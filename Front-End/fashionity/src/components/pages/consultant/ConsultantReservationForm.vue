<template lang="">
  <div id="reservation-form" class="container-fluid scroll">
    <div class="row justify-content-center detail-title">상세 예약 정보 등록</div>
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
            <div class="pretty p-icon p-round">
              <input type="radio" name="icon" />
              <div class="state p-primary-o">
                <i class="icon mdi mdi-check"></i>
                <label>Dollar</label>
              </div>
            </div>

            <div class="pretty p-icon p-round">
              <input type="radio" name="icon" />
              <div class="state p-success-o">
                <i class="icon mdi mdi-check"></i>
                <label>Euro</label>
              </div>
            </div>
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
</style>
