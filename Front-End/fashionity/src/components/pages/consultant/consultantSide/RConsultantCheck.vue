<template>
  <VCalendar
    :attributes="attributes"
    @dayclick="handleDateClick"
    v-model="selectedDate"
    style="width: 1000px"
  />
</template>

<script>
import axios from "axios";

export default {
  created() {
    const nickname = this.$store.getters["memberStore/checkLoginUser"].nickname;
    let token = sessionStorage.getItem("token");

    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/${nickname}/reservations`,
      headers: { Authorization: `Bearer ${token}` },
      method: "GET",
    })
      .then(({ data }) => {
        for (let i = 0; i < data.consultantReservationSummaries.length; i++) {
          let time = data.consultantReservationSummaries[i].reservationDateTime;
          this.attributes[0].dates.push(
            new Date(time[0], time[1] - 1, time[2])
          );
        }
      })
      .catch((response) => {
        console.log(response);
      });
  },
  data() {
    return {
      reservationSeq: null,
      selectedDate: null,
      // Attributes are supplied as an array
      attributes: [
        // This is a single attribute
        {
          // Attribute type definitions
          content: "blue", // Boolean, String, Object
          dot: true, // Boolean, String, Object

          customData: {},
          // We also need some dates to know where to display the attribute
          // We use a single date here, but it could also be an array of dates,
          //  a date range or a complex date pattern.
          dates: [],
          // Think of `order` like `z-index`
          order: 0,
        },
      ],
    };
  },
  methods: {
    handleDateClick({ date }) {
      // Do something with the clicked date, like displaying details or performing an action
      console.log("Clicked date:", date);
    },
  },
};
</script>
<style scoped></style>
