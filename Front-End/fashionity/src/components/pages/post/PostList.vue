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
        <span>최신순</span>
        <span> | </span>
        <span>인기순</span>
      </div>
    </div>

    <div class="row" style="height: 30px"></div>

    <div class="container">
      <div v-if = "dataLoaded">
        <div class="row" style="justify-content: center" v-for="(arr,index) in postRow" :key="index">
          <div class="col" v-for="post in arr" :key="post.post_seq" >
            <the-post :post="post"/>
          </div>
        </div>
      </div>
      <div v-else>
        Loading....
      </div>
    </div>
  </div>
</template>
<script>
import ThePost from "./ThePost.vue";
import TheNavBarPost from "@/components/layout/TheNavBarPost.vue";
import axios from 'axios';

export default {
  data() {
    return {
      keyword: "",
      page:0,
      posts:[],
      dataLoaded:false,
      itemPerRow:4,
    };
  },
  components: {
    ThePost,
    TheNavBarPost,
  },
  computed:{
    postRow(){
      const itemsPerRow = 4;
      const rows = [];
      for (let i = 0; i < this.posts.length; i += itemsPerRow) {
        rows.push(this.posts.slice(i, i + itemsPerRow));
      }
      return rows;
    },
  },
  async mounted(){
    axios({
      url:"http://localhost/api/v1/posts",
      method:"GET"
    }).then((data)=>{
      this.posts = data.data.posts;
      this.dataLoaded = true;
    })
  },
  methods:{
    
  }
};
</script>
<style lang=""></style>
