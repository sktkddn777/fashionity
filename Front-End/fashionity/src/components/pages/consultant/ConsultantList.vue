<template lang="">
  <div class="container-fluid">
    <div class="row justify-content-space-around tools" ref="toolsContainer">
      <div class="col-3 search">
        <input
          type="text"
          id="search"
          name="search"
          placeholder="검색어를 입력하세요"
          v-model="nicknameInput"
          @input="updateInput()"
        />
      </div>
      <div class="col"></div>
      <button class="inactive-button">
        <router-link to="/consultant/myreservation/list" class="link">
          <div style="color: white">내 예약</div>
        </router-link>
      </button>
    </div>

    <div class="row" style="height: 30px"></div>
    <div class="container" ref="contentContainer">
      <div v-if="dataLoaded">
        <div
          class="row justify-content-start"
          style="justify-content: start"
          v-for="(arr, index) in postRow"
          :key="index"
        >
          <div
            class="col justify-content-start"
            v-for="post in arr"
            :key="post.post_seq"
            style="margin-bottom: 20px"
            @click="onclick(post)"
          >
            <consultant-block-vue :post="post"></consultant-block-vue>
          </div>
        </div>
      </div>
      <div v-else>Loading....</div>
    </div>
  </div>
</template>
<script>
import ConsultantBlockVue from "./ConsultantBlock.vue";
import axios from "axios";
export default {
  data() {
    return {
      keyword: "",
      page: 0,
      dataLoaded: false,
      loadingNextPage: false,
      itemPerRow: 4,
      nicknameInput: "",
      posts: [],
    };
  },
  components: {
    ConsultantBlockVue,
  },
  computed: {
    postRow() {
      const itemsPerRow = 4;
      const rows = [];
      for (let i = 0; i < this.posts.length; i += itemsPerRow) {
        rows.push(this.posts.slice(i, i + itemsPerRow));
      }
      return rows;
    },
  },
  async mounted() {
    // 스크롤 이벤트 리스너 추가
    window.addEventListener("scroll", this.handleScroll);
    let token = sessionStorage.getItem("token");
    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/consultants?page=${this.page}&nickname=${this.nicknameInput}`,
      headers:
        token === null
          ? null
          : {
              Authorization: `Bearer ${token}`,
            },
      method: "GET",
    })
      .then((data) => {
        this.posts = data.data.consultants;
        this.dataLoaded = true;
        this.page++;
      })
      .catch((exception) => {
        let data = exception.response;
        if (data.status === 401) {
          //유효기간이 다 된 토큰이면 일단 보여주셈
          axios({
            url: `${process.env.VUE_APP_API_URL}/api/v1/consultants?page=${this.page}&nickname=${this.nicknameInput}`,
            method: "GET",
          }).then((data) => {
            this.posts = data.data.consultants;
          });
        }
        this.dataLoaded = true;
      });
  },
  methods: {
    updateInput() {
      this.page = 0;
      this.posts = [];
      this.loadNextPage();
    },
    async loadNextPage() {
      if (this.loadingNextPage) return;

      this.loadingNextPage = true;

      let token = sessionStorage.getItem("token");

      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/consultants?page=${this.page}&nickname=${this.nicknameInput}`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "GET",
      })
        .then((response) => {
          const newPosts = response.data.consultants;
          if (newPosts.length > 0) {
            this.posts = [...this.posts, ...newPosts];
            this.page++;
          }
          this.loadingNextPage = false;
        })
        .catch((exception) => {
          let data = exception.response;
          if (data.status === 401) {
            //유효기간이 다 된 토큰이면 일단 보여주셈
            axios({
              url: `${process.env.VUE_APP_API_URL}/api/v1/consultants?page=${this.page}&nickname=${this.nicknameInput}`,
              method: "GET",
            }).then((response) => {
              const newPosts = response.data.posts;
              this.posts = [...this.posts, ...newPosts];
              this.loadingNextPage = false;
              this.page++;
            });
          }
          this.loadingNextPage = false;
        });
    },
    handleScroll() {
      // 현재 스크롤 위치
      const scrollY = window.scrollY;
      // 뷰포트의 높이
      const viewportHeight = window.innerHeight;
      // 문서 전체의 높이
      const fullHeight = document.documentElement.scrollHeight;
      // 뷰포트 하단에 도달했을 때 (여기서 200은 여유값을 의미합니다.)
      if (scrollY + viewportHeight >= fullHeight - 60) {
        if (!this.loadingNextPage) {
          this.loadNextPage();
        }
      }
    },
    onclick(post) {
      this.$router.push({
        name: "consultantDate",
        params: { nickname: post.nickname },
      });
    },
  },
  beforeUnmount() {
    // 컴포넌트가 파괴되기 전에 스크롤 이벤트 리스너 제거
    window.removeEventListener("scroll", this.handleScroll);
  },
};
</script>
<style scoped>
.tools {
  margin-bottom: 30px;
}
.tools,
.container {
  display: flex;
  justify-content: space-around;
}
.tools,
.container-fluid {
  flex-wrap: wrap;
}
#search {
  all: unset;
  border: 1px solid rgba(189, 189, 189, 0.8);
  color: #424242;
  font-size: 12px;
  width: 200px;
  height: 30px;
  border-radius: 20px;
}
.sort {
  white-space: nowrap;
}
.sortBtn {
  color: #bdbdbd;
}
.highlighted {
  color: #424242;
  font-weight: bold;
}
.inactive-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #ffffff;
}
</style>
