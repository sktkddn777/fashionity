<template lang="">
  <div class="container-fluid">
    <the-nav-bar-post></the-nav-bar-post>

    <div class="row justify-content-space-around">
      <div class="col-3">
        <input
          type="text"
          id="search"
          name="search"
          placeholder="검색어를 입력하세요"
          style="border: 1px solid #bdbdbd"
        />
      </div>
      <div class="col"></div>
      <div class="col-3">
        <span
          class="sortBtn"
          @click="sortBy('popular')"
          :class="{ highlighted: sorting === 'popular' }"
          >인기순</span
        >
        <span> | </span>
        <span
          class="sortBtn"
          @click="sortBy('latest')"
          :class="{ highlighted: sorting === 'latest' }"
          >최신순</span
        >
      </div>
    </div>

    <div class="row" style="height: 30px"></div>

    <div class="container">
      <div v-if="dataLoaded">
        <div
          class="row"
          style="justify-content: center"
          v-for="(arr, index) in postRow"
          :key="index"
        >
          <div class="col" v-for="post in arr" :key="post.post_seq">
            <router-link
              :to="{ path: `/post/${post.post_seq}` }"
              style="text-decoration: none; color: #424242"
            >
              <the-post :post="post" />
            </router-link>
          </div>
        </div>
      </div>
      <div v-else>Loading....</div>
    </div>
  </div>
</template>
<script>
import ThePost from "./ThePost.vue";
import axios from "axios";

export default {
  data() {
    return {
      keyword: "",
      page: 0,
      posts: [],
      dataLoaded: false,
      loadingNextPage: false,
      itemPerRow: 4,
      sorting: "popular",
    };
  },
  components: {
    ThePost,
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
    // this.loadNextPage();
    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/posts?page=${this.page++}&s=${
        this.sorting
      }`,
      headers:
        token === null
          ? null
          : {
              Authorization: `Bearer ${token}`,
            },
      method: "GET",
    })
      .then((data) => {
        this.posts = data.data.posts;
        console.log(this.posts);
        this.dataLoaded = true;
      })
      .catch((exception) => {
        let data = exception.response.data;
        if (data.code === "A004") {
          //유효기간이 다 된 토큰이면 일단 보여주셈
          axios({
            url: `${process.env.VUE_APP_API_URL}/api/v1/posts`,
            method: "GET",
          }).then((data) => {
            this.posts = data.data.posts;
            this.dataLoaded = true;
          });
        }
      });
  },
  methods: {
    async sortBy(order) {
      console.log("order = " + order);
      this.sorting = order;
      this.page = 0;
      this.posts = [];
      this.loadNextPage();
    },
    async loadNextPage() {
      if (this.loadingNextPage) return;

      this.loadingNextPage = true;

      let token = sessionStorage.getItem("token");

      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts?page=${this
          .page++}&s=${this.sorting}`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "GET",
      })
        .then((response) => {
          const newPosts = response.data.posts;
          this.posts = [...this.posts, ...newPosts];
          this.loadingNextPage = false;
        })
        .catch((exception) => {
          let data = exception.response.data;
          if (data.code === "A004") {
            //유효기간이 다 된 토큰이면 일단 보여주셈
            axios({
              url: `${process.env.VUE_APP_API_URL}/api/v1/posts`,
              method: "GET",
            }).then((data) => {
              this.posts = data.data.posts;
              this.dataLoaded = true;
            });
          }
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
      if (scrollY + viewportHeight >= fullHeight - 200) {
        this.loadNextPage();
      }
    },
  },
  beforeUnmount() {
    // 컴포넌트가 파괴되기 전에 스크롤 이벤트 리스너 제거
    window.removeEventListener("scroll", this.handleScroll);
  },
};
</script>
<style>
.sortBtn {
  color: #bdbdbd;
}
.highlighted {
  color: #424242;
  font-weight: bold; /* 원하는 스타일로 변경 */
}
</style>
