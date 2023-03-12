<template>
  <div class="btn btn-sm btn-tag">
    <span v-html="content"></span>
  </div>
</template>

<script>
export default {
  name: "GroupPurpose",
  props: ['purpose'],
  data() {
    return {
      content: null,
    }
  },
  mounted() {
    this.content = '#'+this.purpose.content
  }
}
</script>

<style scoped>

</style>