<template>
  <div class="d-flex justify-content-between mb-2">
    <div>
    <span>카테고리 선택</span>
    <select class="form-select" aria-label="Default select example" v-model="category">
      <option value="QUESTION">질문</option>
      <option value="INFO">정보</option>
      <option value="SMALLTALK">잡담</option>
    </select>
    </div>
    <div class="align-self-center">

    </div>
  </div>
  <div>
    <span>제목</span>
    <input type="text" class="form-control" v-model="title"/>
  </div>
  <div class="mt-2">
    <span>내용</span>
    <textarea class="form-control" rows="8" v-model="content"/>
  </div>
  <div class="pt-3 pb-2">
  <button type="button" class="float-end btn btn-sm btn-outline-secondary" @click="writePost">완료</button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "GroupPostWrite",
  props: ['groupId'],
  data() {
    return {
      title: null,
      content: null,
      category: 'SMALLTALK',

    }
  },
  methods: {
    writePost() {
      let data = {
        title: this.title,
        content: this.content,
        category: this.category,
      }

      console.log(data);
      axios.post("http://localhost:8000/group-service/groups/"+this.groupId+"/posts", data);
      this.$router.go(this.$router.currentRoute)
    }
  }
}
</script>

<style scoped>

</style>