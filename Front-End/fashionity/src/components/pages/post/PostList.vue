<template lang="">
  <div class="container-fluid">
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
        <span>최신순</span>
        <span> | </span>
        <span>인기순</span>
      </div>
    </div>

    <div class="row" style="height: 30px"></div>

    <div class="container">
      <div class="row" style="justify-content: center">
        <div class="col">
          <router-link to="/post/detail" class="link"
            ><the-post></the-post
          ></router-link>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
      </div>

      <div class="row" style="height: 30px"></div>

      <div class="row">
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
      </div>

      <div class="row" style="height: 40px"></div>
    </div>
  </div>
</template>
<script>
import ThePost from "./ThePost.vue";

export default {
  data() {
    return {
      keyword: "",
      page: 0,
      posts: [],
      dataLoaded: false,
      loadingNextPage:false,
      itemPerRow: 4,
    };
  },
  components: {
    ThePost,
    TheNavBarPost,
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
      url: `${process.env.VUE_APP_API_URL}/api/v1/posts`,
      headers:{
        "Authorization" : `Bearer ${token}`
      },
      method: "GET",
    }).then((data) => {
      this.posts = data.data.posts;
      console.log(this.posts);
      this.dataLoaded = true;
    }).catch((exception)=>{
      let data = (exception.response.data);
      if(data.code === 'A004'){//유효기간이 다 된 토큰이면 일단 보여주셈
        axios({
          url:`${process.env.VUE_APP_API_URL}/api/v1/posts`,
          method:'GET'
        }).then((data)=>{
          this.posts = data.data.posts;
          this.dataLoaded = true;
        })
      }
    });
  },
  methods: {
    async loadNextPage(){
      if(this.loadingNextPage) return;

      this.loadingNextPage = true;
      this.page++;

      let token = sessionStorage.getItem("token");

      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts?page=${this.page}`,
        headers:{
          "Authorization" : `Bearer ${token}`
        },
        method:"GET"
      }).then((response)=>{
        const newPosts = response.data.posts;
        this.posts = [...this.posts,...newPosts];
        this.loadingNextPage = false;
      }).catch(exception=>{
        this.loadingNextPage = false;
        console.log(exception);
      })
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
<style lang=""></style>
