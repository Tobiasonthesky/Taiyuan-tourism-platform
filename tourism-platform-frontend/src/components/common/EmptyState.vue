<template>
  <div class="empty-state">
    <div class="empty-icon">
      <i :class="icon"></i>
    </div>
    <div class="empty-text">
      <h3>{{ title }}</h3>
      <p>{{ description }}</p>
    </div>
    <div class="empty-action" v-if="showAction">
      <el-button type="primary" @click="$emit('action')">{{ actionText }}</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EmptyState',
  props: {
    icon: {
      type: String,
      default: 'el-icon-box'
    },
    title: {
      type: String,
      default: '暂无数据'
    },
    description: {
      type: String,
      default: '暂时没有相关内容'
    },
    showAction: {
      type: Boolean,
      default: false
    },
    actionText: {
      type: String,
      default: '去添加'
    }
  }
}
</script>

<style lang="scss" scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;

  .empty-icon {
    font-size: 80px;
    color: #c0c4cc;
    margin-bottom: 20px;
  }

  .empty-text {
    margin-bottom: 20px;

    h3 {
      font-size: 18px;
      color: #606266;
      margin: 0 0 10px 0;
    }

    p {
      font-size: 14px;
      color: #909399;
      margin: 0;
    }
  }

  .empty-action {
    margin-top: 10px;
  }
}
</style>

