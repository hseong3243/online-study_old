<template>
  <div>
    <div class="d-flex" v-if="update">
      <input class="form-control rounded-start rounded-0" v-model="content"/>
      <button type="button" class="btn btn-outline-secondary rounded-end rounded-0 text-nowrap"
              @click="updateComment">작성</button>
    </div>
    <div v-if="!update">
    <span v-html="comment.memberName"></span>
    <span class="ms-3" v-html="content"></span>
    <div class="float-end" v-if="store.isEqualMember(comment.memberId)">
      <button type="button" class="btn btn-sm" @click="update = !update">수정</button>
      <button type="button" class="btn btn-sm" @click="deleteComment">삭제</button>
    </div>
    </div>
  </div>
</template>

<script>
import {useTokenStore} from "@/store/token";
import axios from "axios";

export default {
  name: "GroupComment",
  props: ['comment'],
  data() {
    return {
      content: this.comment.content,
      update: false,
    }
  },
  setup() {
    const store = useTokenStore();
    return {
      store
    }
  },
  methods: {
    updateComment() {
      let data = {
        content: this.content,
      }
      axios.post('http://localhost:8000/group-service/comments/'+this.comment.commentId, data)
      this.update=false;
    },
    deleteComment() {
      axios.delete('http://localhost:8000/group-service/comments/'+this.comment.commentId)
      this.$emit('deleteComment', this.comment)
    }
  }
}
</script>

<style scoped>

</style>