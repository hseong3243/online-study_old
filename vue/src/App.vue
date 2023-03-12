<template>
  <div id="app">
    <my-header/>
    <div id="wrapper">
      <router-view/>
    </div>
    <footer></footer>
  </div>
</template>

<script>
import MyHeader from "@/components/MyHeader";
export default {
  name: 'App',
  components: {
    MyHeader,
  },
  computed: {
  },
  methods: {

  }
}
</script>

<style>
#app{
  height: auto;
  min-height: 100%;
}
footer{
  height: 50px;
  position: relative;
  transform: translateY(-100%);
}
</style>
