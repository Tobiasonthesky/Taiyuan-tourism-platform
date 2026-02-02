<template>
  <div class="skeleton-wrapper">
    <div v-if="type === 'card'" class="skeleton-card">
      <div class="skeleton-image"></div>
      <div class="skeleton-content">
        <div class="skeleton-title"></div>
        <div class="skeleton-text"></div>
        <div class="skeleton-text short"></div>
      </div>
    </div>
    <div v-else-if="type === 'list'" class="skeleton-list">
      <div v-for="i in count" :key="i" class="skeleton-item">
        <div class="skeleton-avatar"></div>
        <div class="skeleton-info">
          <div class="skeleton-line"></div>
          <div class="skeleton-line short"></div>
        </div>
      </div>
    </div>
    <div v-else-if="type === 'table'" class="skeleton-table">
      <div v-for="i in count" :key="i" class="skeleton-row">
        <div class="skeleton-cell" v-for="j in columns" :key="j"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SkeletonComponent',
  props: {
    type: {
      type: String,
      default: 'card',
      validator: value => ['card', 'list', 'table'].includes(value)
    },
    count: {
      type: Number,
      default: 1
    },
    columns: {
      type: Number,
      default: 4
    }
  }
}
</script>

<style lang="scss" scoped>
.skeleton-wrapper {
  .skeleton-card {
    background: #fff;
    border-radius: 4px;
    overflow: hidden;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .skeleton-image {
      width: 100%;
      height: 200px;
      background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
      background-size: 200% 100%;
      animation: skeleton-loading 1.5s ease-in-out infinite;
    }

    .skeleton-content {
      padding: 15px;

      .skeleton-title {
        height: 20px;
        width: 60%;
        background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
        background-size: 200% 100%;
        animation: skeleton-loading 1.5s ease-in-out infinite;
        border-radius: 4px;
        margin-bottom: 10px;
      }

      .skeleton-text {
        height: 16px;
        width: 100%;
        background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
        background-size: 200% 100%;
        animation: skeleton-loading 1.5s ease-in-out infinite;
        border-radius: 4px;
        margin-bottom: 8px;

        &.short {
          width: 80%;
        }
      }
    }
  }

  .skeleton-list {
    .skeleton-item {
      display: flex;
      padding: 15px;
      border-bottom: 1px solid #ebeef5;

      .skeleton-avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
        background-size: 200% 100%;
        animation: skeleton-loading 1.5s ease-in-out infinite;
        margin-right: 15px;
      }

      .skeleton-info {
        flex: 1;

        .skeleton-line {
          height: 16px;
          width: 100%;
          background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
          background-size: 200% 100%;
          animation: skeleton-loading 1.5s ease-in-out infinite;
          border-radius: 4px;
          margin-bottom: 8px;

          &.short {
            width: 60%;
          }
        }
      }
    }
  }

  .skeleton-table {
    .skeleton-row {
      display: flex;
      padding: 12px 0;
      border-bottom: 1px solid #ebeef5;

      .skeleton-cell {
        flex: 1;
        height: 20px;
        margin: 0 10px;
        background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
        background-size: 200% 100%;
        animation: skeleton-loading 1.5s ease-in-out infinite;
        border-radius: 4px;
      }
    }
  }
}

@keyframes skeleton-loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}
</style>

