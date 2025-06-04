<script setup lang="ts">
// App.vue 作为根组件，主要负责渲染路由视图
</script>

<template>
  <router-view></router-view>
</template>

<style>
/* 重置默认样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  line-height: 1.5;
  color: #2c3e50;
  background-color: #f5f5f5;
}

/* 全局链接样式 */
a {
  text-decoration: none;
  color: inherit;
}

/* 全局按钮样式 */
button {
  cursor: pointer;
  font-family: inherit;
}

/* 全局输入框样式 */
input, select, textarea {
  font-family: inherit;
  font-size: inherit;
}
</style>
