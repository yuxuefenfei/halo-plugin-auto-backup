<script setup lang="ts">
import { VAlert } from "@halo-dev/components";
</script>

<template>
  <div class="px-4 py-3">
    <VAlert title="提示" :closable="false">
      <template #description>
        <ul>
          <li>1.插件重新启动时会自动进行系统备份</li>
          <li>2.Halo进程重新启动时会自动进行系统备份</li>
          <li>
            <strong>3.在插件管理菜单中可以配置备份速率</strong>
          </li>
        </ul>
      </template>
    </VAlert>
  </div>
</template>

<style lang="scss" scoped></style>
