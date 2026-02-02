<template>
  <div class="lazy-image-wrapper" :style="{ width: width, height: height }">
    <img
      v-if="loaded && src && src !== 'null' && src !== 'undefined'"
      :src="src"
      :alt="alt"
      :class="imageClass"
      @error="handleError"
    />
    <div
      v-else
      class="image-placeholder"
      :style="{ width: width, height: height }"
    >
      <i class="el-icon-picture-outline"></i>
      <p v-if="showText">{{ error ? '加载失败' : '加载中...' }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LazyImage',
  props: {
    src: {
      type: String,
      default: ''
    },
    alt: {
      type: String,
      default: ''
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: 'auto'
    },
    imageClass: {
      type: String,
      default: ''
    },
    showText: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loaded: false,
      error: false
    }
  },
  mounted() {
    this.loadImage()
  },
  watch: {
    src() {
      this.loaded = false
      this.error = false
      this.loadImage()
    }
  },
  methods: {
    loadImage() {
      if (!this.src || this.src === 'null' || this.src === 'undefined') {
        this.error = true
        this.loaded = false
        return
      }

      const img = new Image()
      img.onload = () => {
        this.loaded = true
        this.error = false
      }
      img.onerror = () => {
        this.error = true
        this.loaded = false
      }
      img.src = this.src
    },
    handleError() {
      this.error = true
      this.loaded = false
    }
  }
}
</script>

<style lang="scss" scoped>
.lazy-image-wrapper {
  position: relative;
  overflow: hidden;
  background: #f5f7fa;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: opacity 0.3s;
  }

  .image-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: #f5f7fa;
    color: #909399;

    i {
      font-size: 48px;
      margin-bottom: 10px;
    }

    p {
      font-size: 14px;
      margin: 0;
    }
  }
}
</style>

