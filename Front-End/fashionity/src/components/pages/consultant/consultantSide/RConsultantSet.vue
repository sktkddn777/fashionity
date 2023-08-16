<template>
  <div class="container">
    <VCalendar
      :attributes="attributes"
      @dayclick="handleDateClick"
      style="width: 100vh"
      class="calendar"
    />

    <reservation-list-vue class="rlist" :reservations="reservationData" />
  </div>
  <div class="time-input-wrapper">
    <div>
      <label for="time-picker">시작시간:</label>
      <input
        type="time"
        id="time-picker"
        v-model="startTime"
        @input="handleStartTimeChange"
      />
    </div>
    <div>
      <label for="time-picker">종료시간:</label>
      <input
        type="time"
        id="time-picker"
        v-model="endTime"
        @input="handleEndTimeChange"
      />
    </div>
    <div>
      <v-btn @click="saveConsultantSchedule">등록하기</v-btn>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
}

.calendar {
  flex: 2;
}

.rlist {
  flex: 1;
}
</style>

<script>
import axios from "axios";
import { ref } from "vue";
import { useToast } from "vue-toastification";
import ReservationListVue from "./layout/ReservationList.vue";

const toast = useToast();

export default {
  setup() {
    const startTime = ref("");
    const endTime = ref("");

    const handleStartTimeChange = (event) => {
      const inputTime = event.target.value;
      const fixedTime = `${inputTime.split(":")[0]}:00`; // 분을 0으로 고정
      startTime.value = fixedTime;
    };

    const handleEndTimeChange = (event) => {
      const inputTime = event.target.value;
      const fixedTime = `${inputTime.split(":")[0]}:00`; // 분을 0으로 고정
      endTime.value = fixedTime;
    };

    return {
      startTime,
      endTime,
      handleStartTimeChange,
      handleEndTimeChange,
    };
  },
  name: "RConsultantSet",
  components: {
    ReservationListVue,
  },
  created() {},
  data() {
    return {
      selectedDate: null,
      reservationData: [],
    };
  },
  methods: {
    handleDateClick({ date }) {
      this.reservationData = [];
      // Do something with the clicked date, like displaying details or performing an action

      // 예약이 같은 날 여러개인 경우는 일단 생각 안함ㅎ
      this.selectedDate = date;
      let token = sessionStorage.getItem("token");

      let month = date.getMonth() + 1;
      if (month < 10) month = "0" + month.toString();

      const dateTime = `${date.getFullYear()}-${month}-${date.getDate()}`;

      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/reservation/myschedule`,
        headers: { Authorization: `Bearer ${token}` },
        method: "GET",
        params: {
          dateTime: dateTime,
        },
      })
        .then(({ data }) => {
          console.log(data);
          for (let i = 0; i < data.unAvailableDateTimes.length; i++) {
            let time = data.unAvailableDateTimes[i].unAvailableDateTime;

            this.reservationData.push({
              id: data.unAvailableDateTimes[i].scheduleSeq,
              time: `${time[0]}-${time[1]}-${time[2]} ${time[3]}:00:00`,
              profileImage: "@/assets/img/hyeonwook.jpg",
            });
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },

    saveConsultantSchedule() {
      if (this.selectedDate === null) {
        toast.error("날짜를 선택하세용용뇽");
        return;
      }
      if (this.startTime === "") {
        toast.error("시작 시간을 선택하세용용뇽");
        return;
      }
      if (this.endTime === "") {
        toast.error("끝나는 시간을 선택하세용용뇽");
        return;
      }

      let timeArr = [];
      let startHour = this.startTime.split(":")[0];
      let endHour = this.endTime.split(":")[0];
      for (startHour; startHour <= endHour; startHour++) {
        const year = this.selectedDate.getFullYear();
        let month = parseInt(this.selectedDate.getMonth()) + 1;
        const date = this.selectedDate.getDate();

        if (month < 10) month = "0" + month;
        timeArr.push(`${year}-${month}-${date} ${startHour}:00:00`);
      }

      const data = {
        availableDateTimes: timeArr,
      };

      axios({
        url:
          process.env.VUE_APP_API_URL +
          "/api/v1/consultants/reservation/schedule",
        headers: { Authorization: `Bearer ${sessionStorage.getItem("token")}` },
        method: "POST",
        data: data,
      })
        .then((response) => {
          console.log("success: " + response);
        })
        .catch((error) => {
          console.log("fail: " + error);
        });
    },
  },
};
</script>
