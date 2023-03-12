<template>
  <div class="d-flex justify-content-between">
    <div class="col-9">
      <div class="pb-2">
        <span v-if="dataReady">전체 {{this.postData.totalPages}}페이지 중 {{this.postData.number+1}}페이지</span>
      </div>
      <div class="d-flex justify-content-between">
        <div>
          <input type="text" class="form-control"/>
        </div>
        <button type="button" class="btn btn-outline-secondary" @click="write = true" v-if="!write">글작성</button>
        <button type="button" class="btn btn-outline-secondary" @click="write = false" v-if="write">취소</button>
      </div>
      <div class="border-top pt-3 pb-3 mt-3" v-if="write">
        <group-post-write :group-id="groupId"/>
      </div>
      <div class="mt-3">
        <template v-for="post in posts" :key="post.postId">
          <group-post :post="post" :group-id="groupId" @addDeletePost="addDeletePost" v-if="post !== null"/>
        </template>
      </div>
      <div class="d-flex justify-content-center mt-3 gap-1">
        <button class="community-page btn btn-sm btn-outline-secondary"
                v-if="dataReady && hasBeforePage(this.postData.number+1)"
                @click="beforePages(this.postData.number)">...</button>
          <button class="community-page btn btn-sm btn-outline-secondary"
                  v-for="(page, index) in pages" :key="index" :ref="setPageRef"
                  @click="pageClick(page)">{{page}}</button>
        <button class="community-page btn btn-sm btn-outline-secondary"
                v-if="dataReady && hasNextPage(this.postData.number)"
                @click="nextPages(this.postData.number)">...</button>
      </div>
    </div>
    <div class="category p-2">
      <div class="border rounded-1 p-2">
        <p>분류</p>
        <div class="d-flex flex-wrap gap-2">
        <button class="btn btn-sm btn-outline-secondary" @click="selectCategory('ALL')">전체</button>
        <button class="btn btn-sm btn-outline-secondary" @click="selectCategory('QUESTION')">질문</button>
        <button class="btn btn-sm btn-outline-secondary" @click="selectCategory('INFO')">정보</button>
        <button class="btn btn-sm btn-outline-secondary" @click="selectCategory('SMALLTALK')">잡담</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GroupPost from "@/components/group/GroupPost";
import axios from "axios";
import GroupPostWrite from "@/components/group/GroupPostWrite";
import {useGroupStore} from "@/store/group";
import {useTokenStore} from "@/store/token";

export default {
  name: "GroupCommunity",
  components: {
    GroupPost,
    GroupPostWrite,
  },
  props: ['groupId', 'group'],
  data() {
    return {
      write: false,
      postData: null,
      posts: null,
      dataReady: false,
      pages: null,
      deletePost: 0,
      beforeActive: 0,
      pageRefs: [],
    }
  },
  setup() {
    const groupStore = useGroupStore();
    const tokenStore = useTokenStore();
    return {
      groupStore,
      tokenStore
    }
  },
  mounted() {
    const memberId = this.tokenStore.getMemberId;
    const members = this.group.members;

    const isMember = members.filter(member => member.memberId===memberId);
    if(isMember.length === 0) {
      alert('그룹 멤버만 이용 가능합니다.')
      this.groupStore.changeCategory('INTRO')
    }

    this.groupStore.changePostCategory('ALL')
    this.getPosts(0, 10).then(data => {
      this.postData = data
      this.posts = this.postData.data
      this.pages = this.getPages(this.postData.number, this.postData.totalPages)
      this.dataReady = true

    })


    },
  watch: {
    deletePost() {
      this.getPosts(0, 10).then(data => {
        this.postData = data
        this.posts = this.postData.data
        this.pages = this.getPages(this.postData.number, this.postData.totalPages)
        this.dataReady = true
      })
    },

  },
  methods: {
    async getPostsByCategory(category, page, size) {
      const response = await axios.get('http://localhost:8000/group-service/groups/'+this.groupId+'/posts', {
        params: {
          category: category,
          page: page,
          size: size
        }
      });

      return response.data
    },
    async getPosts(page, size) {
      const response = await axios.get("http://localhost:8000/group-service/groups/"+this.groupId+"/posts", {
        params: {
          page: page,
          size: size
        }
      });

      return response.data;
    },
    selectCategory(category) {
      if(category === 'ALL'){
        this.getPosts(0, 10).then(data => {
          this.postData = data;
          this.posts = this.postData.data
          this.pages = this.getPages(this.postData.number, this.postData.totalPages)
          this.groupStore.changePostCategory(category)
          this.dataReady = true
        })
      }
      else {
        this.getPostsByCategory(category, 0, 10).then(data => {
          this.postData = data;
          this.posts = this.postData.data
          this.pages = this.getPages(this.postData.number, this.postData.totalPages)
          this.groupStore.changePostCategory(category)
          this.dataReady = true
        })
      }
    },
    getPages(page, totalPages) {
      let list = [];
      const startPage = this.getStartPage(page);
      console.log(startPage)
      for(let i=startPage; i<startPage+5; i++) {
        list.push(i);
        if(i >= totalPages) {
          break;
        }
      }
      return list;
    },
    getStartPage(page) {
      return Math.floor(page / 5)*5 + 1;
    },
    addDeletePost() {
      this.deletePost++;
      console.log(this.deletePost)
    }
    ,
    async pageClick(page) {
      const category = this.groupStore.getPostCategory;
      this.dataReady = false;
      if(category === 'ALL') {
        this.getPosts(page - 1, 10).then(data => {
          this.postData = data;
          this.posts = this.postData.data
          this.pages = this.getPages(this.postData.number, this.postData.totalPages)
          this.dataReady = true
        })
      }
      else {
        this.getPostsByCategory(category, page-1, 10).then(data => {
          this.postData = data;
          this.posts = this.postData.data
          this.pages = this.getPages(this.postData.number, this.postData.totalPages)
          this.dataReady = true
        })
      }
    },
    setPageRef(el) {
      if(el) {
        this.pageRefs.push(el)
      }
    },
    hasBeforePage(page) {
      return page > 5;
    },
    hasNextPage(page) {
      return this.getStartPage(page)+4 < this.postData.totalPages
    },
    nextPages(page) {
      const startPage = this.getStartPage(page+5);
      this.pageClick(startPage);
    },
    beforePages(page) {
      const startPage = this.getStartPage(page-5);
      this.pageClick(startPage);
    }

  }
}
</script>

<style scoped>
p {
  margin-top: 8px;
}
.community-page {
  z-index: 1;
}
.category {
  width: 150px;
}
</style>