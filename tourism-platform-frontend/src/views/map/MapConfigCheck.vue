<template>
  <div class="config-check">
    <el-alert
      v-if="!mapKeyConfigured"
      title="API密钥未配置"
      type="error"
      :closable="false"
      show-icon>
      <div slot="description">
        <p>请在 <code>.env.development</code> 文件中配置 <code>VUE_APP_MAP_KEY</code></p>
        <p>当前值：{{ currentMapKey || '未设置' }}</p>
      </div>
    </el-alert>
    
    <el-alert
      v-else-if="mapKeyInvalid"
      title="API密钥可能无效"
      type="warning"
      :closable="false"
      show-icon>
      <div slot="description">
        <p>请检查：</p>
        <ul>
          <li>Key的平台类型是否为「Web端(JS API)」</li>
          <li>白名单是否包含当前域名（开发环境建议设置为 <code>*</code>）</li>
          <li>Key是否已启用</li>
        </ul>
      </div>
    </el-alert>
  </div>
</template>

<script>
export default {
  name: 'MapConfigCheck',
  data() {
    return {
      currentMapKey: process.env.VUE_APP_MAP_KEY,
      mapKeyConfigured: false,
      mapKeyInvalid: false
    }
  },
  mounted() {
    this.checkConfig()
  },
  methods: {
    checkConfig() {
      const key = process.env.VUE_APP_MAP_KEY
      this.mapKeyConfigured = key && key !== 'your-map-key' && key.trim() !== ''
      this.mapKeyInvalid = this.mapKeyConfigured && key.length < 20 // 高德Key通常较长
    }
  }
}
</script>

<style scoped>
.config-check {
  margin-bottom: 20px;
}
code {
  background: #f5f5f5;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: monospace;
}
ul {
  margin: 10px 0;
  padding-left: 20px;
}
</style>
