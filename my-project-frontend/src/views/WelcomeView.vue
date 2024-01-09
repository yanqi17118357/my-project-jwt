<template>
  <div style="width: 100vw; height:100vh; overflow: hidden; display: flex">
    <div style="flex: 1;">
      <el-image style="width: 100%; height: 100%" fit="cover"
        src="/_bg_sky2.jpg" />

    </div>
    <div class="welcome-title">
      <div style="font-size:30px; font-weight: bold;">1111111111111111</div>
      <div style="margin-top: 10px">2222222222222222222222222222222</div>
      <div style="margin-top: 5px">33333333333333333333333</div>
    </div>
    <div class="right-card">
      <router-view/>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
.right-card {
  width: 400px;
  z-index: 1;
  background-color: white;
}
</style>
