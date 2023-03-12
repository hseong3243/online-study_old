<template>
  <div>
  <div class="d-flex justify-content-between border-top border-bottom pt-3 pb-3 post-simple" @click="showPost" v-if="!show">
    <div>
      <h3 ref="title" v-html="title"></h3>
      <span class="content" ref="content" v-html="content"></span>
      <div class="pt-3 post-writer">
        <span ref="memberName" v-html="memberName"></span>
        <span> · </span>
        <span ref="createdAt" v-html="createdAt"></span>
        <span> · </span>
        <span ref="category" v-html="category"></span>
      </div>
    </div>
    <div class="align-self-center">
      <div class="comment-count">
        <span v-html="comments.length"></span>
        <span>댓글</span>
      </div>
    </div>
  </div>
  <div class="border-top border-bottom pt-3 pb-3" v-if="show">
    <div class="float-end">
      <div class="d-flex gap-2">
        <button type="button" class="btn btn-sm" @click="update = !update" v-if="store.isEqualMember(this.post.memberId)">수정</button>
        <button type="button" class="btn btn-sm" @click="deletePost" v-if="store.isEqualMember(this.post.memberId)">삭제</button>
      </div>
    </div>
    <div class="d-flex flex-column">
      <h3 ref="innerTitle" v-html="title" v-if="!update"></h3>
      <span v-if="update">제목</span>
      <input type="text" class="form-control" v-model="title" v-if="update"/>
    </div>
    <span v-html="replaceBrTag(content)" v-if="!update"></span>
    <span v-if="update">내용</span>
    <textarea class="form-control" v-model="content" v-if="update" rows="8"/>
    <div class="pt-2 pb-4" v-if="update">
      <button class="float-end btn btn-outline-secondary" type="button" @click="updatePost">완료</button>
    </div>
    <div class="pt-3 post-writer" v-if="!update">
      <span ref="innerMemberName" v-html="memberName"></span>
      <span> · </span>
      <span ref="innerCreatedAt" v-html="createdAt"></span>
    </div>
    <div>
      <span>댓글</span>
    </div>
    <div class="mt-2" v-if="!update">
      <div class="pt-1 pb-1" v-for="comment in comments" :key="comment.commentId">
        <group-comment :comment="comment" @deleteComment="deleteComment"/>
      </div>
      <div class="d-flex mt-3">
        <input class="form-control rounded-start rounded-0" v-model="comment"/>
        <button type="button" class="btn btn-outline-secondary rounded-end rounded-0 text-nowrap"
                @click="writeComment">작성</button>
      </div>
    </div>

  </div>
  </div>
</template>

<script>
import {useTokenStore} from "@/store/token";
import axios from "axios";
import GroupComment from "@/components/group/GroupComment";

export default {
  name: "GroupPost",
  components: {
    GroupComment
  },
  data() {
    return{
      show: false,
      update: false,
      title: null,
      content: null,
      memberName: null,
      createdAt: null,
      category: null,
      memberId: null,
      comments: [],
      comment: null,
    }
  },
  props: ['post', 'groupId'],
  mounted() {
    this.title = this.post.title;
    this.content = this.post.content;
    this.memberName = this.post.memberName;
    this.category = this.getCategoryName(this.post.category)
    this.createdAt = this.getCreatedAt(this.post.createdAt);
    this.comments = this.post.comments

    this.$refs.title.textContent = this.title
    this.$refs.content.textContent = this.content
    this.$refs.memberName.textContent = this.memberName;
    this.$refs.createdAt.textContent = this.createdAt;
    this.$refs.category.textContent = this.category
  },
  setup() {
    const store = useTokenStore();
    return {
      store,
    }
  },
  watch: {
    comments() {
      console.log(this.comments)
    }
  },
  methods: {
    showPost() {
      this.show = !this.show
    },
    replaceBrTag(str) {
      if (str === undefined || str == null)
      {
        return "";
      }

      str = str.replace(/\r\n/ig, '<br>');
      str = str.replace(/\\n/ig, '<br>');
      str = str.replace(/\n/ig, '<br>');
      return str;
    },
    getCreatedAt(createdAt) {
      const time = Date.now();
      const seconds = Math.floor(time/1000) - createdAt
      const min = Math.floor(seconds/60);
      let hours
      let days;
      let month;
      if(min>=60) {
        hours = Math.floor(min/60);
      } else {
        return min + '분 전'
      }
      if(hours>=24) {
        days =  Math.floor(hours/24);
      } else {
        return hours + '시간 전'
      }
      if(days>30) {
        month =  Math.floor(days/30);
      }else {
        return days + '일 전'
      }
      if(month<=12) {
        return month + '달 전'
      }else {
        return month/12 + '년 전'
      }
    },
    getCategoryName(category) {
      switch (category) {
        case 'SMALLTALK':
          return '잡담'
        case 'QUESTION':
          return '질문'
        case 'INFO':
          return '정보'
      }
    },
    deletePost() {
      axios.delete("http://localhost:8000/group-service/groups/"+this.groupId+"/posts/"+this.post.postId)
      this.$router.go(this.$router.currentRoute)
    },
    updatePost() {
      let data = {
        title: this.title,
        content: this.content
      }
      axios.post("http://localhost:8000/group-service/groups/"+this.groupId+"/posts/"+this.post.postId, data)
      this.update = !this.update
    },
    async writeComment() {
      let data = {
        postId: this.post.postId,
        content: this.comment,
      }
      const response = await axios.post("http://localhost:8000/group-service/comments", data);
      const responseData = await axios.get('http://localhost:8000/group-service/comments/'+response.data);
      this.comments.push(responseData.data)
      this.comment = null
    },
    deleteComment(comment) {
      const index = this.comments.indexOf(comment);
      if(index !== -1)
        this.comments.splice(index, 1)
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500;700&display=swap');
h3 {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
.post-simple {

}
.post-simple:hover {
  background-color: #f8f8f8;
  cursor: pointer;
}
.post-writer {
  color: #A8ADAE;
}
.comment-count {
  width: 60px;
  height: 60px;
  text-align: center;
  justify-content: center;
  border: 1px solid #d6d6d6;
  border-radius: 50px;
  display: flex;
  flex-direction: column;
}
.content {
  color: #8F9D9D;
  display: inline-block;
}
</style>