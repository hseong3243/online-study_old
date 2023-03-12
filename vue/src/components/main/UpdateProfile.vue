<template>
  <div class="MyContainer m-auto">
    <form>
      <div class="loginContainer mt-5 d-flex flex-column align-items-center">
        <h3>이름 변경</h3>
        <div class="mb-3 col-4">
          <label for="name">닉네임</label>
          <input type="text" id="name" class="form-control" v-model="name" placeholder="nickname">
        </div>
        <div class="d-grid mt-3 col-4">
          <button class="btn btn-lg btn-bread rounded-1" v-on:click="submitForm">가입하기</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: "UpdateProfile"
}
</script>

<style scoped>
.MyContainer {
  width: 1200px;
}
.btn-bread {
  background: #F6E2BD;
}
.btn:hover {
  background: #F6E2BD;
}
</style>