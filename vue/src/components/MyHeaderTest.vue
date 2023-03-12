<template>
  <div class="border-2 border-bottom">
  </div>
</template>

<script>

import {useTokenStore} from "@/store/token";

export default {
  name: "MyHeaderTest",
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore,
    }
  },
  mounted() {
    console.log(this.tokenStore)
  }

}
</script>

<style scoped>

</style>