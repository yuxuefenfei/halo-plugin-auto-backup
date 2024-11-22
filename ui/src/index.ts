import { type BackupTab, definePlugin } from "@halo-dev/console-shared";
import { markRaw } from "vue";
import AutoBackupView from "@/views/AutoBackupView.vue";

export default definePlugin({
  components: {},
  routes: [],
  extensionPoints: {
    "backup:tabs:create": (): BackupTab[] | Promise<BackupTab[]> => {
      return [
        {
          id: "autoBackup",
          label: "自动备份",
          component: markRaw(AutoBackupView),
          permissions: [],
        },
      ];
    },
  },
});
