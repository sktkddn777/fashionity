<template>
  <div>
    <h2>예약 가능일자 등록</h2>
    <h2>수정 못하니까 똑띠 생각하세요</h2>
  </div>

  <VCalendar
    :attributes="attributes"
    @dayclick="handleDateClick"
    style="width: 1000px"
  />

  <div>
    <label for="time-picker">시작시간:</label>
    <input
      type="time"
      id="time-picker"
      v-model="startTime"
      @input="handleTimeChange"
    />
  </div>
  <div>
    <label for="time-picker">종료시간:</label>
    <input
      type="time"
      id="time-picker"
      v-model="endTime"
      @input="handleTimeChange"
    />
  </div>
  <div>
    <v-btn @click="saveConsultantSchedule">등록하기</v-btn>
  </div>
</template>

<script>
import { ref } from "vue";

export default {
  components: {},
  setup() {
    const startTime = ref("");
    const endTime = ref("");

    const handleTimeChange = (event) => {
      const inputTime = event.target.value;
      const fixedTime = `${inputTime.split(":")[0]}:00`; // 분을 0으로 고정
      startTime.value = fixedTime;
    };

    return {
      startTime,
      endTime,
      handleTimeChange,
    };
  },
  name: "RConsultantSet",
  created() {},
  data() {
    return {
      selectedDate: null,
    };
  },
  methods: {
    handleDateClick({ date }) {
      // Do something with the clicked date, like displaying details or performing an action

      // 예약이 같은 날 여러개인 경우는 일단 생각 안함ㅎ
      this.selectedDate = date;
    },

    saveConsultantSchedule() {
      console.log(this.selectedDate);
      console.log(this.startTime);
      console.log(this.endTime);
    },
  },
};
</script>
<style scoped></style>
