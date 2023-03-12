<template>
  <div class="MyContainer m-auto">
    <form>
      <div class="MyContainer mt-5 d-flex flex-column align-items-center">
        <h3>로그인</h3>
        <div class="mt-3 mb-3 col-4">
          <input type="text" class="form-control" v-model="email" placeholder="아이디">
        </div>
        <div class="mb-3 col-4">
          <input type="password" class="form-control" v-model="password" placeholder="비밀번호">
<!--          <a href="#" class="text-black text-decoration-none">비밀번호를 잊으셨나요?</a>-->
        </div>
        <button type="button" class="btn btn-bread col-4 mb-3" v-on:click="login">로그인</button>
        <router-link to="/signup" class="btn btn-secondary col-4 mb-3">회원가입</router-link>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import {useTokenStore} from "@/store/token";
export default {
  name: 'MyLogin',
  data() {
    return {
      email: '',
      password: '',
    }
  },
  methods: {
    test() {
      this.store.test = '얍';
      console.log(this.store.test);
    },
    login: function () {
      let data = {
        email: this.email,
        password: this.password
      }

      const config = {
        headers: {
          'Access-Control-Allow-Origin': "http://localhost:8080",
        }
      }

      axios.post("http://localhost:8000/member-service/login", data, config)
      .then((response) => {
        console.log(response);
        const token = response.headers.token;
        const memberId = response.headers.memberid;
        console.log(token);
        console.log(memberId);
        // localStorage.setItem("Authorization", token);
        // localStorage.setItem("memberId", memberId);
        const store = useTokenStore();
        store.token = token;
        store.memberId = memberId;
        this.$router.push('/')
      })
      .catch(function (error) {
        alert("아이디/패스워드가 일치하지 않습니다.")
        console.log(error)
      })
    },
  }
}
</script>

<style scoped>
router-link {
  z-index: 1;
}
button {
  z-index: 1;
}
.btn-bread {
 background: #F6E2BD;
}
.MyContainer {
  width: 1100px;
}
</style>