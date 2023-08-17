<template>
  <div class="container-fluid" style="width: 80vw">
    <div v-if="dataLoaded">
      <div
        class="row justify-content-start"
        style="margin-bottom: 20px"
        v-for="(row, index) in postRow"
        :key="index"
      >
        <div class="col-3" v-for="post in row" :key="post.postSeq">
          <my-post :post="post" />
        </div>
      </div>
    </div>
    <div v-else>Loading...</div>
  </div>
  <div class="row" style="height: 30px"></div>
</template>

<script>
import MyPost from "@/components/pages/user/MyPost.vue";
import axios from "axios";

export default {
  data() {
    return {
      page: 0,
      posts: [],
      dataLoaded: false,
      loadingNextPage: false,
      itemsPerRow: 4,
    };
  },
  components: {
    MyPost,
  },
  computed: {
    postRow() {
      const itemsPerRow = this.itemsPerRow;
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
    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/members/${this.$route.params.nickname}/liked?page=${this.page}`,
      headers: { Authorization: `Bearer ${sessionStorage.getItem("token")}` },
      method: "GET",
    })
      .then(({ data }) => {
        console.log(data);
        this.posts = data.profilePosts;
        this.dataLoaded = true;
        this.page++;
      })
      .catch((exception) => {
        console.log(exception);
        if (exception.response && exception.response.status === 401) {
          //유효기간이 다 된 토큰이면 일단 보여주셈
          axios({
            url: `${process.env.VUE_APP_API_URL}/api/v1/members/${this.$route.params.nickname}/liked?page=${this.page}`,
            method: "GET",
          }).then((data) => {
            this.posts = data.profilePosts;
            this.dataLoaded = true;
          });
        }
      });
  },
  methods: {
    updateInput() {
      this.sortBy(this.sorting);
    },
    async sortBy(order) {
      this.sorting = order;
      this.page = 0;
      this.posts = [];
      this.loadNextPage();
    },
    async loadNextPage() {
      let token = sessionStorage.getItem("token");

      if (this.loadingNextPage) return;
      this.loadingNextPage = true;
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/members/${this.$route.params.nickname}/liked?page=${this.page}`,
        headers: { Authorization: `Bearer ${token}` },
        method: "GET",
      })
        .then((response) => {
          const newPosts = response.profilePosts;
          this.posts = [...this.posts, ...newPosts];
          this.loadingNextPage = false;
          this.page++;
        })
        .catch((exception) => {
          // let data = exception.response;
          if (exception.response && exception.response.status === 401) {
            //유효기간이 다 된 토큰이면 일단 보여주셈
            axios({
              url: `${process.env.VUE_APP_API_URL}/api/v1/members/${this.$route.params.nickname}/liked?page=${this.page}`,
              method: "GET",
            }).then((response) => {
              const newPosts = response.profilePosts;
              this.posts = [...this.posts, ...newPosts];
              this.loadingNextPage = false;
              this.page++;
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
<style scoped>
/* .container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.row {
  display: flex;
  margin-bottom: 1.2rem;
} */
</style>
