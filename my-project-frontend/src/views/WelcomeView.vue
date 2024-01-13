<template>
  <div style="width: 100vw; height:100vh; overflow: hidden; display: flex">
    <div style="flex: 1;">
      <el-image style="width: 100%; height: 100%" fit="cover"
        src="/_bg_sky2.webp" />

    </div>
    <div class="welcome-title">
      <div style="font-size:30px; font-weight: bold;">Sonna</div>
      <div style="margin-top: 10px">她沉鱼落雁、闭月羞花</div>
      <div style="margin-top: 5px">她风姿绰约、她风华绝代</div>
    </div>
    <div class="right-card">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"></component>
        </transition>
      </router-view>
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
