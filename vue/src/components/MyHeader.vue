<template>
  <div class="background border-2 border-bottom">
    <div class="MyContainer d-flex justify-content-between m-auto p-2 ps-4 pe-4">
      <div class="d-flex col-6 justify-content-between align-items-center">
        <router-link to="/" class="Title">나의 공부방</router-link>
        <router-link to="/studyrooms" class="Link">공부방</router-link>
        <router-link to="/groups" class="Link" href="#">그룹</router-link>
        <router-link to="/records" class="Link">공부 기록</router-link>
        <router-link to="/test" class="Link" v-if="tokenStore.getMemberId">프로필</router-link>
        <router-link to="" class="Link text-white no-pointer" v-if="!tokenStore.getMemberId">공사중</router-link>
<!--        <button @click="getToken"></button>-->
      </div>
      <div class="d-flex align-items-center gap-2">
        <router-link to="/login" class="Link" v-if="!tokenStore.isLogin" >로그인</router-link>
        <a class="Link" @click="logout()" v-if="tokenStore.isLogin" >로그아웃</a>
<!--        <img :src="require('@/assets/img/icon-profile.png')" v-if="tokenStore.isLogin"-->
<!--             class="profile" alt="targetTime" >-->
      </div>
    </div>
  </div>
</template>

<script>

import {useTokenStore} from "@/store/token";

export default {
  name: 'MyHeader',
  components: {
  },
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore
    }
  },
  data() {
    return {
      hover: false,
    }
  },
  methods: {
    doMouseOver() {
      this.hover = true;
    },
    doMouseLeave() {
      this.hover = false;
    },
    logout() {
      this.tokenStore.logout()
      this.$router.push('/')
    }
  },
}
</script>


<style scoped>
@import 'bootstrap/dist/css/bootstrap.min.css';
.v-application {
  font-family: 'NanumGothic', sans-serif !important;
}
@font-face {
  font-family: 'NanumGothic';
  src: url('/src/assets/fonts/NanumGothic-Bold.ttf') format('truetype');
  font-weight: 700;
}
img {
  width: 40px;
  height: 40px;
}
.profile {
  cursor: pointer;
}
.profile-hover {
  width: 300px;
  height: 400px;
  position: absolute;
  background: white;
}
.MyContainer {
  width: 1100px;
}
.background {
  min-width: 1100px;
}
.no-pointer {
  cursor: default;
}


.Title {
  font-size: 25px;
  font-weight: 500;
  color: black;
  text-decoration: none;
}
.Title:hover {
  color: black;
}
.Link {
  color: black;
  text-decoration: none;
}
.Link:hover {
  color: #dcba6f;
}
a {
  text-decoration: none;
  color: black;
  font-family: 'NanumGothic', 'Noto Sans KR', sans-serif;
  cursor: pointer;
}
.navbar-brand:hover {
  color: black;
}
.nav-link {
  color: black;
}
.nav-link:hover {
  color: #dcba6f;
}
</style>