<template>
  <div class="MyContainer m-auto">
    <form>
      <div class="loginContainer mt-5 d-flex flex-column align-items-center">
        <h3>회원가입</h3>
        <div class="mt-3 mb-3 col-4">
          <label for="email">이메일</label>
          <input id="email" type="text" class="form-control" v-model="email" placeholder="name@example.com">
        </div>
        <div class="mb-3 col-4">
          <label for="password">비밀번호</label>
          <input id="password" type="password" class="form-control" v-model="password" placeholder="*******"/>
        </div>
        <div class="mb-3 col-4">
          <label for="passwordCheck">비밀번호 확인</label>
          <input type="password" id="passwordCheck" class="form-control" v-model="passwordCheck" placeholder="*******"/>
          <div v-if="passwordCheckValid">
            비밀번호가 일치하지 않습니다.
          </div>
        </div>
        <div class="mb-3 col-4">
          <label for="name">닉네임</label>
          <input type="text" id="name" class="form-control" v-model="name" placeholder="nickname">
        </div>
        <div class="col-4">
          <select class="form-select" aria-label="Default select example" v-model="studyCategory">
            <option value="ALL">분류</option>
            <option value="HIGH">고등학생</option>
            <option value="UNIV">대학생</option>
            <option value="CERT">자격증</option>
            <option value="LANG">어학</option>
            <option value="GOV">공무원</option>
            <option value="JOB">취업</option>
            <option value="ETC">기타</option>
          </select>
        </div>
        <div class="d-grid mt-3 col-4">
          <button type="button" class="btn btn-lg btn-bread " v-on:click="submitForm">가입하기</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "MySignUp",
  data: function () {
    return {
      email: '',
      password: '',
      passwordCheck: '',
      name: '',
      studyCategory: 'ALL',
    }
  },
  computed: {
    passwordCheckValid() {
      return this.password !== this.passwordCheck;
    }
  },
  methods: {
    submitForm() {
      if(this.studyCategory === 'ALL')
        this.studyCategory = 'ETC'

      let data = {
        email: this.email,
        password: this.password,
        name: this.name,
        studyCategory: this.studyCategory
      }

      let passwordCheck = this.passwordCheck;

      if(data.password !== passwordCheck) {
        alert("동일한 패스워드를 입력해주세요.")
      }
      axios.post('http://localhost:8000/member-service/members', data)
      .then((response) => {
        console.log(response);
        this.$router.push('/')
      })
      .catch(function (error) {
        console.log(error);
        alert('회원가입 실패. 잠시 후에 다시 시도해주세요.')
      })
    }
  }
}
</script>

<style scoped>
button {
  z-index: 1;
}
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