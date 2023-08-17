<template lang="">
  <div
    class="container-fluid justify-content-center d-flex align-items-center"
    style="height: 65vh; margin-left: 4vw"
  >
    <div class="row" style="height: 55vh; width: 80%">
      <!-- 오전 -->
      <div class="row" align = "left"><h5><b>오전</b></h5></div>
      <div class="row d-flex">
        <div
          class="col-2 d-flex align-items-center justify-content-center"
          v-for="(time, index) in am1"
          :key="index"
        >
          <div v-if="timeList.includes(time)" class="time-block">
            {{ time }}:00
          </div>
          <div v-else class="time-non-block">{{ time }}:00</div>
        </div>
      </div>
      <div class="row d-flex">
        <div
          class="col-2 d-flex align-items-center justify-content-center"
          v-for="(time, index) in am2"
          :key="index"
        >
          <div
            v-if="timeList.includes(time)"
            class="time-block"
            @click="timeClick(time)"
          >
            {{ time }}:00
          </div>
          <div v-else class="time-non-block">{{ time }}:00</div>
        </div>
      </div>
      <div style = "height:2rem"></div>
      <!-- 오후 -->
      <div class="row" align = "left"><h5><b>오후</b></h5></div>
      <div class="row d-flex">
        <div
          class="col-2 d-flex align-items-center justify-content-center"
          v-for="(time, index) in pm1"
          :key="index"
        >
          <div v-if="timeList.includes(time)" class="time-block">
            {{ time }}:00
          </div>
          <div v-else class="time-non-block">{{ time }}:00</div>
        </div>
      </div>

      <div class="row d-flex">
        <div
          class="col-2 d-flex align-items-center justify-content-center"
          v-for="(time, index) in pm2"
          :key="index"
        >
          <div v-if="timeList.includes(time)" class="time-block">
            {{ time }}:00
          </div>
          <div v-else class="time-non-block">{{ time }}:00</div>
        </div>
      </div>

      <div class="row d-flex">
        <div
          class="col-2 d-flex align-items-center justify-content-center"
          v-for="(time, index) in pm3"
          :key="index"
        >
          <div v-if="timeList.includes(time)" class="time-block">
            {{ time }}:00
          </div>
          <div v-else class="time-non-block">{{ time }}:00</div>
        </div>
      </div>

      <!-- <div class="row">
        <div class="col"></div>
        <div class="col-3">
          <router-link class="link" to="/consultant/reservation/detail"
            ><button>NEXT</button></router-link
          >
        </div>
      </div> -->

      <!-- <div v-for="(time, index) in pm" :key="index">{{ time }}</div> -->
      <!-- <div>{{ this.scheduleList }}</div> -->
      <!-- <div>{{ this.currDate }}</div> -->
    </div>
  </div>
</template>
<script>
export default {
  date() {
    return {
      am1: [],
      am2: [],
      pm1: [],
      pm2: [],
      pm3: [],
      // pagaInfo: "date",
      currDate: Date,
      availTimeList: [],
      timeList: [],
      seq: Number,
      nickname: String,
    };
  },
  created() {
    this.am1 = [6, 7, 8];
    this.am2 = [9, 10, 11, 12];
    this.pm1 = [13, 14, 15, 16];
    this.pm2 = [17, 18, 19, 20];
    this.pm3 = [21, 22, 23, 24];
    this.currDate = this.$route.params.date;
    this.nickname = this.$route.params.nickname;

    this.availTimeList = [];
    this.timeList = [];
    for (let i = 0; i < this.scheduleList.length; i++) {
      // this.scheduleList[i].availableDateTime.toLocaleDateString();
      if (
        this.isSameDate(
          new Date(this.currDate),
          new Date(this.scheduleList[i].availableDateTime)
        )
      ) {
        this.availTimeList.push([
          this.scheduleList[i].scheduleSeq,
          new Date(this.scheduleList[i].availableDateTime).getHours(),
        ]);
        this.timeList.push(
          new Date(this.scheduleList[i].availableDateTime).getHours()
        );
        console.log("yes");
      }
    }
    console.log(this.availTimeList);
    console.log(this.timeList);
  },

  mounted() {
    //   this.availTimeList = [];
    //   this.timeList = [];
    //   for (let i = 0; i < this.scheduleList.length; i++) {
    //     // this.scheduleList[i].availableDateTime.toLocaleDateString();
    //     if (
    //       this.isSameDate(
    //         new Date(this.currDate),
    //         new Date(this.scheduleList[i].availableDateTime)
    //       )
    //     ) {
    //       this.availTimeList.push([
    //         this.scheduleList[i].scheduleSeq,
    //         new Date(this.scheduleList[i].availableDateTime).getHours(),
    //       ]);
    //       this.timeList.push(
    //         new Date(this.scheduleList[i].availableDateTime).getHours()
    //       );
    //       console.log("yes");
    //     }
    //   }
    //   console.log(this.availTimeList);
    //   console.log(this.timeList);
  },
  props: {
    scheduleList: [],
    date: Date,
  },
  methods: {
    isSameDate(date1, date2) {
      return (
        date1.getFullYear() === date2.getFullYear() &&
        date1.getMonth() === date2.getMonth() &&
        date1.getDate() === date2.getDate()
      );
    },

    timeClick(time) {
      // alert(time);
      for (let i = 0; i < this.availTimeList.length; i++) {
        if (time == this.availTimeList[i][1]) {
          // console.log("test");
          this.seq = this.availTimeList[i][0];
        }
      }
      // console.log("time : ", this.seq);
      this.$router.push({
        name: "consultantDetail",
        params: { seq: this.seq, nickname: this.nickname },
      });
    },
  },
};
</script>
<style scoped>
.time-block {
  border-radius: 4px;
  background-color: #2190ff;
  height: 5vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  margin-right: 10px;
  width: 6vw;
  color:white;
  /* padding-top: 10px; */
}

.time-non-block {
  border-radius: 4px;
  background-color: #cecece;
  height: 5vh;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  width: 6vw;
  /* padding-top: 10px; */
}

button {
  background: black;
  color: #fff;
  border: none;
  position: relative;
  height: 40px;
  font-size: 1.2em;
  padding: 0 2em;
  cursor: pointer;
  transition: 800ms ease all;
  outline: none;
}
button:hover {
  background: #fff;
  color: #424242;
}
button:before,
button:after {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  height: 2px;
  width: 0;
  background: #424242;
  transition: 400ms ease all;
}
button:after {
  right: inherit;
  top: inherit;
  left: 0;
  bottom: 0;
}
button:hover:before,
button:hover:after {
  width: 100%;
  transition: 800ms ease all;
}
</style>
