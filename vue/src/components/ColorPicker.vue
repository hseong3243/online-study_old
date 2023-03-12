<template>
  <div @click="$emit('close-picker')">
    <swatches v-model="colors"/>
  </div>
</template>

<script>
import Swatches from 'vue-color'
export default {
  name: "ColorPicker",
  components: {
    Swatches
  },
  props: ['element'],
  data() {
    return {
      colors: {
        hex: '#194d33',
        hsl: {h:150, s:0.5, l:0.2, a:1},
        hsv: {h:150, s:0.66, v:0.30, a:1},
        rgba:{r:25,g:77,b:51,a:1},
        a:1
      }
    }
  },
  methods: {
    changeBack(element) {
      let el = document.getElementById(element)
      console.log(el)
      el.style.backgroundColor = this.colors.hex
    }
  },
  watch: {
    'colors':'changeBack'
  }
}
</script>

<style scoped>

</style>