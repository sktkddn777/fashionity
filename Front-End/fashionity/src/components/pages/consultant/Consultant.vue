<template>
  <div></div>
</template>

<script>
import router from "@/router";

export default {
  name: "ConsultantVue",
  created() {
    // axios.getMemberRole
    // if 역할 중에 컨설턴트가 있다면 ConsultantMain뷰로
    router.push({ name: "RConsultantMain" });
  },
  // components: {},
  // props: {},
  data: () => ({}),
  // methods: {},
};
</script>

<style scoped></style>
