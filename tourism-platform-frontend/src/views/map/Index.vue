<template>
  <div class="map-page">
    <div class="container">
      <h1 class="page-title">太原文旅地图</h1>
      
      <!-- 控制面板 -->
      <div class="map-controls">
        <el-card shadow="never" class="control-card">
          <div class="control-row">
            <div class="filter-group">
              <span class="label">类型筛选：</span>
              <el-checkbox-group v-model="selectedTypes" @change="handleTypeChange">
                <el-checkbox label="attraction">景点</el-checkbox>
                <el-checkbox label="restaurant">餐厅</el-checkbox>
                <el-checkbox label="hotel">酒店</el-checkbox>
                <el-checkbox label="culture">文化活动</el-checkbox>
              </el-checkbox-group>
            </div>
            
            <div class="search-group">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索地点..."
                prefix-icon="el-icon-search"
                style="width: 300px;"
                @keyup.enter.native="handleSearch"
                clearable
              />
              <el-button type="primary" @click="handleSearch" style="margin-left: 10px;">搜索</el-button>
            </div>
          </div>
          
          <div class="control-row" style="margin-top: 15px;">
            <div class="route-group">
              <span class="label">导航模式：</span>
              <el-radio-group v-model="routeMode" size="small">
                <el-radio-button label="driving">驾车</el-radio-button>
                <el-radio-button label="walking">步行</el-radio-button>
                <el-radio-button label="riding">骑行</el-radio-button>
              </el-radio-group>
            </div>
            
            <el-button type="success" size="small" @click="getCurrentLocation" :loading="locating">
              <i class="el-icon-location"></i> {{ locating ? '定位中...' : '定位当前位置' }}
            </el-button>
            
            <el-button type="primary" size="small" @click="refreshMap" :loading="refreshing">
              <i class="el-icon-refresh"></i> {{ refreshing ? '刷新中...' : '刷新地图' }}
            </el-button>
            
            <el-button type="info" size="small" @click="clearRoute">
              <i class="el-icon-close"></i> 清除路线
            </el-button>
            
            <el-button type="warning" size="small" @click="showHelpDialog = true">
              <i class="el-icon-question"></i> 使用说明
            </el-button>
          </div>
        </el-card>
      </div>
      
      <!-- 地图容器 -->
      <div class="map-wrapper">
        <div id="map-container" class="map-container"></div>
        
        <!-- 图例 -->
        <div class="map-legend">
          <div class="legend-item">
            <span class="legend-icon attraction"></span>
            <span>景点</span>
          </div>
          <div class="legend-item">
            <span class="legend-icon restaurant"></span>
            <span>餐厅</span>
          </div>
          <div class="legend-item">
            <span class="legend-icon hotel"></span>
            <span>酒店</span>
          </div>
          <div class="legend-item">
            <span class="legend-icon culture"></span>
            <span>文化活动</span>
          </div>
        </div>
      </div>

      <!-- 使用说明对话框 -->
      <el-dialog
        title="地图使用说明"
        :visible.sync="showHelpDialog"
        width="700px"
        :close-on-click-modal="false"
      >
        <div class="help-content">
          <el-collapse v-model="activeHelpItem" accordion>
            <el-collapse-item title="基础操作" name="basic">
              <div class="help-section">
                <h4><i class="el-icon-view"></i> 查看地图标记</h4>
                <ul>
                  <li>地图上显示不同颜色的标记，代表不同类型的旅游资源</li>
                  <li>蓝色标记：景点</li>
                  <li>绿色标记：餐厅</li>
                  <li>橙色标记：酒店</li>
                  <li>紫色标记：文化活动</li>
                  <li>点击标记可以查看详细信息</li>
                </ul>
              </div>
            </el-collapse-item>
            
            <el-collapse-item title="类型筛选" name="filter">
              <div class="help-section">
                <h4><i class="el-icon-menu"></i> 筛选显示内容</h4>
                <ul>
                  <li>使用顶部的复选框可以筛选要显示的类型</li>
                  <li>勾选"景点"：只显示景点标记</li>
                  <li>勾选"餐厅"：只显示餐厅标记</li>
                  <li>勾选"酒店"：只显示酒店标记</li>
                  <li>勾选"文化活动"：只显示文化活动标记</li>
                  <li>可以同时选择多个类型</li>
                </ul>
              </div>
            </el-collapse-item>
            
            <el-collapse-item title="搜索功能" name="search">
              <div class="help-section">
                <h4><i class="el-icon-search"></i> 搜索地点</h4>
                <ul>
                  <li>在搜索框中输入地点名称或关键词</li>
                  <li>按回车键或点击"搜索"按钮进行搜索</li>
                  <li>搜索结果会在地图上显示标记</li>
                  <li>点击搜索结果可以查看详细信息</li>
                  <li>地图会自动调整视野以显示所有搜索结果</li>
                </ul>
              </div>
            </el-collapse-item>
            
            <el-collapse-item title="定位功能" name="location">
              <div class="help-section">
                <h4><i class="el-icon-location"></i> 定位当前位置</h4>
                <ul>
                  <li>点击"定位当前位置"按钮获取您的位置</li>
                  <li>首次使用需要允许浏览器定位权限</li>
                  <li>定位成功后，地图上会显示蓝色定位图标</li>
                  <li>地图会自动移动到您的位置</li>
                  <li>点击定位图标可以查看当前位置的详细地址</li>
                </ul>
              </div>
            </el-collapse-item>
            
            <el-collapse-item title="导航功能" name="navigation">
              <div class="help-section">
                <h4><i class="el-icon-guide"></i> 路线导航</h4>
                <ul>
                  <li>点击地图上的标记，在弹出的信息窗口中点击"导航"按钮</li>
                  <li>选择导航模式：驾车、步行或骑行</li>
                  <li>导航会自动使用您当前定位的位置作为起点</li>
                  <li>如果没有定位，系统会自动尝试获取您的位置</li>
                  <li>导航信息会显示在右侧浮窗中，包括：</li>
                  <li style="margin-left: 20px;">- 起点和终点信息</li>
                  <li style="margin-left: 20px;">- 总距离和预计时间</li>
                  <li style="margin-left: 20px;">- 详细的路线指引步骤</li>
                  <li>点击"重新规划"可以刷新路线</li>
                  <li>点击"清除路线"可以清除地图上的路线</li>
                </ul>
              </div>
            </el-collapse-item>
            
            <el-collapse-item title="其他功能" name="other">
              <div class="help-section">
                <h4><i class="el-icon-refresh"></i> 刷新地图</h4>
                <ul>
                  <li>点击"刷新地图"按钮可以重新加载地图数据</li>
                  <li>刷新会清除所有标记和路线，但保留您的定位位置</li>
                  <li>刷新后会自动加载最新的景点、餐厅、酒店和文化活动信息</li>
                </ul>
                
                <h4 style="margin-top: 20px;"><i class="el-icon-zoom-in"></i> 地图操作</h4>
                <ul>
                  <li>鼠标滚轮：缩放地图</li>
                  <li>鼠标拖拽：移动地图</li>
                  <li>双击：放大地图</li>
                  <li>右键拖拽：旋转地图（3D视图）</li>
                  <li>右上角工具栏：提供缩放、定位等快捷操作</li>
                </ul>
              </div>
            </el-collapse-item>
          </el-collapse>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="showHelpDialog = false">我知道了</el-button>
        </div>
      </el-dialog>

      <!-- 导航信息浮窗 -->
      <transition name="slide-fade">
        <el-card v-if="showNavigationPanel" class="navigation-panel">
          <div slot="header" class="panel-header">
            <span class="panel-title">
              <i class="el-icon-guide"></i> 导航信息
            </span>
            <el-button
              type="text"
              icon="el-icon-close"
              @click="closeNavigationPanel"
              class="close-btn"
            ></el-button>
          </div>
          
          <div class="navigation-content" v-if="navigationInfo">
            <!-- 起点和终点 -->
            <div class="route-points">
              <div class="route-point origin">
                <div class="point-icon">
                  <i class="el-icon-location"></i>
                </div>
                <div class="point-info">
                  <div class="point-label">起点</div>
                  <div class="point-address">{{ navigationInfo.origin || '当前位置' }}</div>
                </div>
              </div>
              
              <div class="route-divider">
                <i class="el-icon-arrow-down"></i>
              </div>
              
              <div class="route-point destination">
                <div class="point-icon">
                  <i class="el-icon-flag"></i>
                </div>
                <div class="point-info">
                  <div class="point-label">终点</div>
                  <div class="point-address">{{ navigationInfo.destination || '目的地' }}</div>
                </div>
              </div>
            </div>

            <!-- 路线统计信息 -->
            <div class="route-stats">
              <div class="stat-item">
                <i class="el-icon-odometer"></i>
                <div class="stat-content">
                  <div class="stat-label">距离</div>
                  <div class="stat-value">{{ navigationInfo.distance || '计算中...' }}</div>
                </div>
              </div>
              <div class="stat-item">
                <i class="el-icon-time"></i>
                <div class="stat-content">
                  <div class="stat-label">预计时间</div>
                  <div class="stat-value">{{ navigationInfo.duration || '计算中...' }}</div>
                </div>
              </div>
              <div class="stat-item">
                <i class="el-icon-map-location"></i>
                <div class="stat-content">
                  <div class="stat-label">导航模式</div>
                  <div class="stat-value">{{ getRouteModeText(navigationInfo.mode) }}</div>
                </div>
              </div>
            </div>

            <!-- 路线步骤 -->
            <div class="route-steps" v-if="navigationInfo.steps && navigationInfo.steps.length > 0">
              <div class="steps-title">
                <i class="el-icon-sort"></i> 路线指引
              </div>
              <div class="steps-list">
                <div
                  v-for="(step, index) in navigationInfo.steps"
                  :key="index"
                  class="step-item"
                >
                  <div class="step-number">{{ index + 1 }}</div>
                  <div class="step-content">
                    <div class="step-instruction">
                      {{ formatInstruction(step.instruction || step.road || '继续前行') }}
                    </div>
                    <div class="step-details">
                      <span class="step-distance" v-if="formatDistance(step.distance)">
                        <i class="el-icon-odometer"></i>
                        {{ formatDistance(step.distance) }}
                      </span>
                      <span class="step-action" v-if="getActionType(step.instruction || step.road)">
                        <i :class="getActionIcon(step.instruction || step.road)"></i>
                        {{ getActionType(step.instruction || step.road) }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="navigation-actions">
              <el-button
                type="primary"
                size="small"
                icon="el-icon-refresh"
                @click="refreshRoute"
                :loading="routeRefreshing"
              >
                重新规划
              </el-button>
              <el-button
                size="small"
                icon="el-icon-close"
                @click="closeNavigationPanel"
              >
                关闭
              </el-button>
            </div>
          </div>
          
          <!-- 加载状态 -->
          <div v-else class="loading-state">
            <i class="el-icon-loading"></i>
            <p>正在规划路线...</p>
          </div>
        </el-card>
      </transition>
    </div>
  </div>
</template>

<script>
import { getAllMapData, planRoute, searchPlace } from '@/api/map'
import AMapLoader from '@amap/amap-jsapi-loader'

export default {
  name: 'MapPage',
  data() {
    return {
      map: null,
      AMap: null,
      markers: [],
      infoWindows: [],
      route: null,
      selectedTypes: ['attraction', 'restaurant', 'hotel', 'culture'],
      searchKeyword: '',
      routeMode: 'driving',
      locating: false,
      refreshing: false, // 是否正在刷新地图
      currentLocation: {
        lng: 112.549656,
        lat: 37.870451,
        address: '太原市中心'
      },
      currentLocationMarker: null, // 当前位置标记
      mapData: {
        attractions: [],
        restaurants: [],
        hotels: [],
        cultures: []
      },
      locationParams: null, // 用于存储URL参数中的定位信息
      showNavigationPanel: false, // 是否显示导航信息浮窗
      navigationInfo: null, // 导航信息
      routeRefreshing: false, // 是否正在重新规划路线
      currentDestination: null, // 当前目的地信息
      showHelpDialog: false, // 是否显示使用说明对话框
      activeHelpItem: 'basic' // 当前展开的帮助项
    }
  },
  mounted() {
    // 检查是否是页面刷新
    // 使用 performance API 检测页面加载类型
    let isPageRefresh = false
    try {
      // 方法1: 使用新的 PerformanceNavigationTiming API
      const navEntries = performance.getEntriesByType('navigation')
      if (navEntries && navEntries.length > 0) {
        const navType = navEntries[0].type
        // 'reload' 表示刷新，'navigate' 表示正常导航
        isPageRefresh = navType === 'reload'
      } else {
        // 方法2: 降级到旧的 performance.navigation API（已废弃但部分浏览器仍支持）
        if (performance.navigation) {
          // type 1 表示刷新
          isPageRefresh = performance.navigation.type === 1
        }
      }
    } catch (error) {
      console.warn('无法检测页面加载类型:', error)
      // 如果无法检测，默认不清除状态（保守策略）
      isPageRefresh = false
    }
    
    // 如果是页面刷新，清除所有状态
    if (isPageRefresh) {
      console.log('检测到页面刷新，清除地图状态')
      this.resetMapState()
      // 清除URL参数（如果有）
      if (this.$route.query.lng || this.$route.query.lat || this.$route.query.type || this.$route.query.id) {
        this.$router.replace({ path: this.$route.path })
      }
    } else {
      // 不是刷新，检查URL参数（可能是从其他页面跳转过来）
      this.checkLocationParams()
    }
    
    this.initMap()
  },
  beforeDestroy() {
    // 清理地图资源
    try {
      // 清除所有标记
      this.clearMarkers()
      
      // 清除当前位置标记
      if (this.currentLocationMarker) {
        try {
          this.currentLocationMarker.setMap(null)
          this.currentLocationMarker = null
        } catch (error) {
          console.error('清除当前位置标记失败:', error)
        }
      }
      
      // 清除路线
      if (this.route) {
        try {
          this.route.clear()
        } catch (e) {
          // 忽略错误
        }
        this.route = null
      }
      
      // 销毁地图
      if (this.map) {
        try {
          this.map.destroy()
        } catch (e) {
          // 忽略销毁错误
        }
        this.map = null
      }
      
      this.AMap = null
    } catch (error) {
      console.error('清理地图资源失败:', error)
    }
  },
  methods: {
    async initMap() {
      try {
        // 加载高德地图API
        const mapKey = process.env.VUE_APP_MAP_KEY || 'your-map-key'
        // 【关键修改1】读取安全密钥
        const mapSecret = process.env.VUE_APP_MAP_SECRET || ''
        
        // 详细检查API密钥配置
        if (!mapKey || mapKey === 'your-map-key' || mapKey.trim() === '') {
          const errorMsg = `
请配置高德地图API密钥！

配置步骤：
1. 在 tourism-platform-frontend 目录下创建或编辑 .env.development 文件
2. 添加以下内容：
   VUE_APP_MAP_KEY=你的高德地图API密钥
   VUE_APP_MAP_SECRET=你的高德地图安全密钥

3. 确保API密钥：
   - 平台类型为「Web端(JS API)」
   - 白名单包含 localhost 或设置为 *
   - Key已启用

4. 重启开发服务器（npm run serve）

当前配置值：
Key: ${mapKey || '未设置'}
安全密钥: ${mapSecret ? '已配置' : '未设置'}
          `
          console.error(errorMsg)
          throw new Error('API密钥未配置，请查看控制台详细说明')
        }

        // 【关键修改2】检查安全密钥是否配置
        if (!mapSecret || mapSecret.trim() === '') {
          throw new Error('请配置高德地图安全密钥（VUE_APP_MAP_SECRET），Web端JS API 2.0版本必须配置')
        }

        // 【关键修改3】全局配置安全密钥（高德地图JS API 2.0版本必需）
        window._AMapSecurityConfig = {
          securityJsCode: mapSecret
        }

        // 先加载基础插件，避免某些插件导致认证错误
        this.AMap = await AMapLoader.load({
          key: mapKey,
          // 【关键修改3】添加安全密钥参数（核心修复点）
          securityJsCode: mapSecret,
          version: '2.0',
          plugins: [
            'AMap.Marker',
            'AMap.InfoWindow',
            'AMap.ToolBar',
            'AMap.Scale',
            'AMap.Geolocation',
            'AMap.CitySearch', // 添加城市搜索插件，用于IP定位
            'AMap.Driving',
            'AMap.Walking',
            'AMap.Riding' // 添加骑行导航插件
          ]
        })

        // 创建地图实例
        this.map = new this.AMap.Map('map-container', {
          zoom: 13,
          center: [112.549248, 37.857014], // 太原市中心坐标
          mapStyle: 'amap://styles/normal'
        })

        // 添加工具条和比例尺
        this.map.addControl(new this.AMap.ToolBar({
          position: 'RT'
        }))
        this.map.addControl(new this.AMap.Scale({
          position: 'LB'
        }))

        // 加载地图数据
        await this.loadMapData()
        
        // 地图初始化完成后，检查是否需要定位到指定地点
        this.$nextTick(() => {
          this.handleLocationParams()
        })
      } catch (error) {
        console.error('地图加载失败:', error)
        const errorMsg = error.message || '未知错误'
        
        if (errorMsg.includes('USERKEY_PLAT_NOMATCH') || errorMsg.includes('key') || errorMsg.includes('安全密钥')) {
          this.$message.error('API密钥配置错误！请检查：1) .env文件中的VUE_APP_MAP_KEY和VUE_APP_MAP_SECRET是否正确 2) Key的平台类型是否为"Web端(JS API)" 3) 白名单是否包含当前域名')
        } else {
          this.$message.error('地图加载失败：' + errorMsg)
        }
        
        // 如果AMap未加载成功，设置默认值避免后续错误
        if (!this.AMap) {
          this.AMap = null
        }
        if (!this.map) {
          this.map = null
        }
      }
    },

    async loadMapData() {
      try {
        const res = await getAllMapData()
        console.log('地图数据API响应:', res)
        if (res.code === 200 && res.data) {
          // 处理美食数据，确保数据结构正确
          if (res.data.foods && Array.isArray(res.data.foods)) {
            // 如果API返回的是foods字段，将其映射到restaurants字段
            this.mapData = {
              ...res.data,
              restaurants: [...(res.data.restaurants || []), ...res.data.foods]
            }
            console.log('转换后的地图数据:', this.mapData)
          } else {
            this.mapData = res.data
            console.log('加载的地图数据:', this.mapData)
          }
          
          // 验证数据结构
          const dataKeys = Object.keys(this.mapData)
          const hasValidData = dataKeys.some(key => {
            const data = this.mapData[key]
            return Array.isArray(data) && data.length > 0
          })
          
          if (hasValidData) {
            console.log('检测到有效数据，开始渲染标记')
            // 确保地图已初始化后再渲染标记
            if (this.AMap && this.map) {
              this.renderMarkers()
            } else {
              console.warn('地图未初始化，延迟渲染标记')
              // 延迟一下再尝试渲染
              setTimeout(() => {
                if (this.AMap && this.map) {
                  this.renderMarkers()
                } else {
                  console.error('地图仍未初始化，无法渲染标记')
                }
              }, 500)
            }
          } else {
            console.warn('未检测到有效地图数据', this.mapData)
            this.$message.warning('当前无可用地图数据')
          }
        } else {
          console.error('地图数据API返回无效响应:', res)
          this.$message.error('加载地图数据失败：API返回无效响应')
        }
      } catch (error) {
        console.error('加载地图数据失败:', error)
        this.$message.error('加载地图数据失败：' + (error.message || '未知错误'))
      }
    },

    // 刷新地图
    async refreshMap() {
      if (!this.map) {
        this.$message.warning('地图未初始化')
        return
      }

      this.refreshing = true
      
      try {
        // 清除现有路线
        this.clearRoute()
        
        // 清除导航面板
        if (this.showNavigationPanel) {
          this.closeNavigationPanel()
        }
        
        // 清除所有标记（保留当前位置标记，因为它是独立存储的）
        this.markers.forEach(marker => {
          try {
            marker.setMap(null)
          } catch (error) {
            // 忽略清除错误
          }
        })
        this.markers = []
        
        // 清除信息窗口
        this.infoWindows.forEach(win => {
          try {
            win.close()
          } catch (error) {
            // 忽略关闭错误
          }
        })
        this.infoWindows = []
        
        // 重新加载地图数据
        await this.loadMapData()
        
        // 注意：当前位置标记（currentLocationMarker）会被保留，不会被清除
        // 这样可以保持用户的定位状态
        
        this.$message.success('地图已刷新')
      } catch (error) {
        console.error('刷新地图失败:', error)
        this.$message.error('刷新地图失败：' + (error.message || '未知错误'))
      } finally {
        this.refreshing = false
      }
    },

    renderMarkers() {
      // 检查地图是否已初始化
      if (!this.AMap || !this.map) {
        console.warn('地图未初始化，无法渲染标记')
        return
      }

      console.log('开始渲染标记，选中的类型:', this.selectedTypes)
      console.log('可用的地图数据:', this.mapData)
      
      // 清除现有标记
      this.clearMarkers()

      const typeMap = {
        attraction: { data: this.mapData.attractions || [], icon: 'attraction', color: '#409EFF', name: '景点' },
        restaurant: { data: this.mapData.restaurants || [], icon: 'restaurant', color: '#67C23A', name: '餐厅' },
        hotel: { data: this.mapData.hotels || [], icon: 'hotel', color: '#E6A23C', name: '酒店' },
        culture: { data: this.mapData.cultures || [], icon: 'culture', color: '#F56C6C', name: '文化活动' }
      }

      // 记录渲染统计
      const renderStats = {
        total: 0,
        byType: {},
        errors: 0
      }
      
      this.selectedTypes.forEach(type => {
        const config = typeMap[type]
        if (!config) {
          console.warn('未知的类型:', type)
          return
        }
        
        const data = config.data || []
        renderStats.byType[type] = {
          total: data.length,
          rendered: 0
        }
        
        console.log(`准备渲染${config.name}数据，共${data.length}条`)
        
        if (Array.isArray(data)) {
          data.forEach((item, index) => {
            try {
              if (item && item.longitude != null && item.latitude != null) {
                this.addMarker(item, config.icon, config.color)
                renderStats.total++
                renderStats.byType[type].rendered++
              } else {
                console.warn(`第${index}条${config.name}数据缺少有效坐标:`, item)
                renderStats.errors++
              }
            } catch (error) {
              console.error(`渲染第${index}条${config.name}数据失败:`, error, item)
              renderStats.errors++
            }
          })
        } else {
          console.warn(`${config.name}数据不是数组:`, data)
        }
      })

      console.log('标记渲染完成:', renderStats)
      
      // 调整地图视野
      if (this.markers.length > 0 && this.map) {
        try {
          this.map.setFitView(this.markers, false, [50, 50, 50, 50])
          console.log('地图视野已调整，显示所有标记')
        } catch (error) {
          console.error('调整地图视野失败:', error)
        }
      } else if (this.markers.length === 0) {
        console.warn('没有渲染任何标记，无法调整地图视野')
        this.$message.info('当前视图没有可显示的标记点')
      }
    },

    addMarker(item, type, color) {
      // 检查AMap是否已加载
      if (!this.AMap || !this.map) {
        console.warn('地图未初始化，无法添加标记')
        return
      }

      console.log('添加标记:', item.name, '类型:', type)
      
      // 验证坐标数据
      const lng = parseFloat(item.longitude)
      const lat = parseFloat(item.latitude)
      console.log('原始坐标:', item.longitude, item.latitude, '转换后:', lng, lat)
      
      if (isNaN(lng) || isNaN(lat)) {
        console.warn('无效的坐标数据:', item)
        return
      }

      try {
        // 创建自定义图标 - 使用Canvas绘制
        const icon = this.createMarkerIcon(type, color)

        const markerConfig = {
          position: [lng, lat],
          title: item.name || '',
          extData: { ...item, type }
        }

        // 只有当图标创建成功时才添加icon属性
        if (icon) {
          markerConfig.icon = icon
          // 设置图标偏移，让图标底部中心对准坐标点
          // 图标大小是 32x32，所以 offset 应该是 (-16, -32)
          markerConfig.offset = new this.AMap.Pixel(-16, -32)
        }

        const marker = new this.AMap.Marker(markerConfig)
        console.log('创建的标记:', marker)

        marker.on('click', () => {
          this.showInfoWindow(marker, item, type)
        })

        marker.setMap(this.map)
        this.markers.push(marker)
        console.log('标记添加成功，当前标记总数:', this.markers.length)
      } catch (error) {
        console.error('添加标记失败:', error, item)
      }
    },

    createMarkerIcon(type, color) {
      // 检查AMap是否已加载
      if (!this.AMap) {
        console.warn('AMap未加载，无法创建图标')
        return null
      }

      // 使用Canvas创建标记图标
      const canvas = document.createElement('canvas')
      canvas.width = 32
      canvas.height = 32
      const ctx = canvas.getContext('2d')

      if (!ctx) {
        console.warn('无法获取Canvas上下文')
        return null
      }

      // 绘制圆形背景
      ctx.beginPath()
      ctx.arc(16, 16, 14, 0, Math.PI * 2)
      ctx.fillStyle = color
      ctx.fill()
      ctx.strokeStyle = '#fff'
      ctx.lineWidth = 2
      ctx.stroke()

      // 绘制文字
      ctx.fillStyle = '#fff'
      ctx.font = 'bold 14px Arial'
      ctx.textAlign = 'center'
      ctx.textBaseline = 'middle'
      let text
      if (type === 'attraction') {
        text = '景'
      } else if (type === 'restaurant') {
        text = '餐'
      } else if (type === 'hotel') {
        text = '酒'
      } else if (type === 'culture') {
        text = '文'
      } else if (type === 'search') {
        text = '搜'
      } else {
        text = '位'
      }
      ctx.fillText(text, 16, 16)

      try {
        return new this.AMap.Icon({
          size: new this.AMap.Size(32, 32),
          image: canvas.toDataURL(),
          imageSize: new this.AMap.Size(32, 32)
        })
      } catch (error) {
        console.error('创建图标失败:', error)
        return null
      }
    },

    showInfoWindow(marker, item, type) {
      // 检查AMap是否已加载
      if (!this.AMap || !this.map) {
        console.warn('地图未初始化，无法显示信息窗口')
        return
      }

      // 关闭所有信息窗口
      this.infoWindows.forEach(win => {
        try {
          win.close()
        } catch (e) {
          // 忽略关闭错误
        }
      })
      this.infoWindows = []

      // 确保item包含type和id信息
      if (!item.type && type) {
        item.type = type
      }
      // 如果item没有id，尝试从marker的extData中获取
      if (!item.id && marker && marker.getExtData) {
        const extData = marker.getExtData()
        if (extData && extData.id) {
          item.id = extData.id
        }
        if (extData && extData.type && !item.type) {
          item.type = extData.type
        }
      }

      const imageUrl = item.coverImage || item.image || ''
      const imageHtml = imageUrl 
        ? `<img src="${imageUrl}" style="width: 200px; height: 120px; object-fit: cover; border-radius: 4px; margin-bottom: 10px;" onerror="this.style.display='none';" />`
        : ''

      let typeName = '位置'
      const itemType = item.type || type
      if (itemType === 'attraction') {
        typeName = '景点'
      } else if (itemType === 'restaurant') {
        typeName = '餐厅'
      } else if (itemType === 'hotel') {
        typeName = '酒店'
      } else if (itemType === 'culture') {
        typeName = '文化活动'
      }
      
      // 对于文化活动，地址字段可能是 activityLocation
      const address = item.address || item.activityLocation || '地址未知'
      
      // 创建信息窗口内容容器
      const contentDiv = document.createElement('div')
      contentDiv.style.padding = '10px'
      contentDiv.style.minWidth = '220px'
      
      const itemId = item.id || (marker.getExtData() && marker.getExtData().id) || Date.now()
      contentDiv.innerHTML = `
        <h3 style="margin: 0 0 10px 0; font-size: 16px; color: #303133;">${this.escapeHtml(item.name || '未知')}</h3>
        ${imageHtml}
        <p style="margin: 5px 0; color: #606266; font-size: 14px;">
          <i class="el-icon-location"></i> ${this.escapeHtml(address)}
        </p>
        ${itemType === 'culture' && item.activityTime ? `
        <p style="margin: 5px 0; color: #606266; font-size: 13px;">
          <i class="el-icon-time"></i> 活动时间：${this.escapeHtml(item.activityTime)}
        </p>
        ` : ''}
        <p style="margin: 5px 0; color: #909399; font-size: 12px;">
          <span style="background: ${itemType === 'culture' ? '#F56C6C' : itemType === 'attraction' ? '#409EFF' : itemType === 'restaurant' ? '#67C23A' : '#E6A23C'}; color: #fff; padding: 2px 6px; border-radius: 2px; font-size: 11px;">
            ${typeName}
          </span>
        </p>
        <div style="margin-top: 10px; display: flex; gap: 8px;">
          ${item.type && item.id ? `
          <button id="detail-btn-${itemId}" 
                  style="background: #67C23A; color: #fff; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; font-size: 12px;">
            <i class="el-icon-view"></i> 查看详情
          </button>
          ` : ''}
          <button id="nav-btn-${itemId}" 
                  style="background: #409EFF; color: #fff; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; font-size: 12px;">
            <i class="el-icon-guide"></i> 导航
          </button>
        </div>
      `

      // 绑定查看详情按钮点击事件
      const detailBtn = contentDiv.querySelector(`#detail-btn-${itemId}`)
      if (detailBtn && item.type && item.id) {
        detailBtn.addEventListener('click', () => {
          this.goToDetail(item.type, item.id)
        })
      }
      
      // 绑定导航按钮点击事件
      const navBtn = contentDiv.querySelector(`#nav-btn-${itemId}`)
      if (navBtn) {
        navBtn.addEventListener('click', () => {
          const lng = parseFloat(item.longitude)
          const lat = parseFloat(item.latitude)
          if (!isNaN(lng) && !isNaN(lat)) {
            const destinationName = item.name || '目的地'
            this.startNavigation(lng, lat, destinationName)
          } else {
            this.$message.warning('坐标数据无效')
          }
        })
      }

      try {
        // 高德地图 InfoWindow 的 offset 参数说明：
        // offset: new AMap.Pixel(x, y)
        // x: 水平偏移（正数向右，负数向左）
        // y: 垂直偏移（正数向下，负数向上）
        // 设置为 (0, 0) 时，弹窗底部小三角会精确指向标记点
        // 设置为 (0, -15) 时，弹窗会向上移动15px，底部小三角指向标记点上方15px的位置
        const infoWindow = new this.AMap.InfoWindow({
          content: contentDiv,
          offset: new this.AMap.Pixel(0, 0),  // 不偏移，弹窗底部小三角精确指向标记点
          closeWhenClickMap: true,  // 点击地图关闭弹窗
          autoMove: true  // 弹窗超出视野时自动移动地图
        })

        // 确保使用标记点的准确位置
        const position = marker.getPosition()
        infoWindow.open(this.map, position)
        this.infoWindows.push(infoWindow)
      } catch (error) {
        console.error('创建信息窗口失败:', error)
        this.$message.error('显示信息窗口失败')
      }
    },

    escapeHtml(text) {
      if (!text) return ''
      const div = document.createElement('div')
      div.textContent = text
      return div.innerHTML
    },

    async startNavigation(destLng, destLat, destinationName = '目的地') {
      if (!this.map) {
        this.$message.warning('地图未初始化')
        return
      }

      // 保存目的地信息
      this.currentDestination = {
        lng: destLng,
        lat: destLat,
        name: destinationName
      }

      let originLng, originLat
      let originAddress = '当前位置'

      // 优先使用用户实际定位的位置（检查是否有定位标记，说明用户已经定位过）
      if (this.currentLocationMarker && this.currentLocation && this.currentLocation.lng && this.currentLocation.lat) {
        // 用户已经定位过，使用定位的位置
        originLng = this.currentLocation.lng
        originLat = this.currentLocation.lat
        originAddress = this.currentLocation.address || '当前位置'
        console.log('使用定位位置作为起点:', { lng: originLng, lat: originLat, address: originAddress })
      } else {
        // 没有定位标记，必须尝试自动定位获取当前位置
        this.$message.info('正在获取您的位置，请稍候...')
        const locationSuccess = await this.getCurrentLocation()
        
        if (locationSuccess && this.currentLocation && this.currentLocation.lng && this.currentLocation.lat) {
          // 定位成功，使用定位位置
          originLng = this.currentLocation.lng
          originLat = this.currentLocation.lat
          originAddress = this.currentLocation.address || '当前位置'
          console.log('定位成功，使用定位位置作为起点:', { lng: originLng, lat: originLat, address: originAddress })
        } else {
          // 定位失败，不允许使用默认位置，提示用户先定位
          this.$message({
            message: '无法获取您的位置，无法进行导航。请先点击"定位当前位置"按钮获取您的位置，然后再进行导航。',
            type: 'warning',
            duration: 5000
          })
          console.log('定位失败，取消导航')
          return // 直接返回，不进行导航
        }
      }

      // 显示导航面板
      this.showNavigationPanel = true
      this.navigationInfo = {
        origin: originAddress,
        destination: destinationName,
        mode: this.routeMode,
        distance: '计算中...',
        duration: '计算中...',
        steps: []
      }

      await this.planRoute(originLng, originLat, destLng, destLat)
    },

    async planRoute(originLng, originLat, destLng, destLat) {
      try {
        // 清除现有路线
        this.clearRoute()

        // 更新导航信息状态
        if (this.navigationInfo) {
          this.navigationInfo.distance = '计算中...'
          this.navigationInfo.duration = '计算中...'
          this.navigationInfo.steps = []
        }

        const res = await planRoute(originLng, originLat, destLng, destLat, this.routeMode)
        
        if (res.code === 200 && res.data) {
          const routeData = res.data
          
          // 更新导航信息
          if (this.navigationInfo) {
            this.navigationInfo.distance = routeData.distanceText || routeData.distance || '未知'
            this.navigationInfo.duration = routeData.duration || routeData.durationText || '未知'
            this.navigationInfo.steps = routeData.steps || []
          }
          
          // 如果API返回了路线数据，使用高德地图路线规划插件
          if (routeData.success && routeData.steps) {
            this.drawRouteFromSteps(routeData.steps, originLng, originLat, destLng, destLat)
          } else {
            // 使用高德地图路线规划插件
            this.drawRouteWithPlugin(originLng, originLat, destLng, destLat, routeData)
          }

          this.$message.success('路线规划成功')
        }
      } catch (error) {
        console.error('加载路线失败:', error)
        // 检查是否是404错误
        if (error.response && error.response.status === 404) {
          this.$message.warning('路线规划接口不存在，使用前端地图API进行路线规划')
          // 降级到前端地图API
          this.drawRouteWithPlugin(originLng, originLat, destLng, destLat, null)
        } else {
          this.$message.error('路线规划失败：' + (error.response?.data?.message || error.message || '未知错误'))
          // 即使失败也尝试使用前端API
          this.drawRouteWithPlugin(originLng, originLat, destLng, destLat, null)
        }
      }
    },

    drawRouteWithPlugin(originLng, originLat, destLng, destLat, routeData = null) {
      if (!this.AMap || !this.map) {
        this.$message.warning('地图未初始化，无法规划路线')
        return
      }

      try {
        let routePlugin

        if (this.routeMode === 'driving') {
          routePlugin = new this.AMap.Driving({
            map: this.map,
            panel: null
          })
        } else if (this.routeMode === 'walking') {
          routePlugin = new this.AMap.Walking({
            map: this.map,
            panel: null
          })
        } else if (this.routeMode === 'riding') {
          // 骑行导航需要Riding插件
          if (!this.AMap.Riding) {
            this.$message.warning('骑行导航功能需要加载Riding插件，当前使用步行模式替代')
            routePlugin = new this.AMap.Walking({
              map: this.map,
              panel: null
            })
          } else {
            routePlugin = new this.AMap.Riding({
              map: this.map,
              panel: null
            })
          }
        } else {
          // 默认使用驾车模式
          routePlugin = new this.AMap.Driving({
            map: this.map,
            panel: null
          })
        }

        routePlugin.search(
          new this.AMap.LngLat(originLng, originLat),
          new this.AMap.LngLat(destLng, destLat),
          (status, result) => {
            if (status === 'complete') {
              this.route = routePlugin
              
              // 解析路线结果，更新导航信息
              this.parseRouteResult(result, routeData)
            } else {
              this.$message.error('路线规划失败：' + (result || '未知错误'))
              if (this.navigationInfo) {
                this.navigationInfo.distance = '规划失败'
                this.navigationInfo.duration = '规划失败'
              }
            }
          }
        )
      } catch (error) {
        console.error('路线规划插件初始化失败:', error)
        this.$message.error('路线规划功能初始化失败')
        if (this.navigationInfo) {
          this.navigationInfo.distance = '规划失败'
          this.navigationInfo.duration = '规划失败'
        }
      }
    },

    // 解析路线结果，提取导航信息
    parseRouteResult(result, routeData = null) {
      if (!this.navigationInfo) {
        return
      }

      try {
        // 优先使用后端返回的数据
        if (routeData) {
          if (routeData.distanceText) {
            this.navigationInfo.distance = routeData.distanceText
          }
          if (routeData.duration || routeData.durationText) {
            this.navigationInfo.duration = routeData.duration || routeData.durationText
          }
          if (routeData.steps && Array.isArray(routeData.steps) && routeData.steps.length > 0) {
            this.navigationInfo.steps = routeData.steps.map(step => {
              // 处理不同格式的step数据
              let instruction = step.instruction || step.road || step.action || ''
              let distance = step.distance || ''
              
              // 格式化距离，确保有单位
              distance = this.formatDistance(distance)
              
              return {
                instruction: instruction,
                distance: distance
              }
            })
            console.log('从后端数据提取到路径步骤:', this.navigationInfo.steps.length, '条')
          }
        }

        // 如果后端数据不完整或没有steps，尝试从高德地图API结果中提取
        if (result) {
          let route = null
          
          // 处理不同的数据结构
          if (result.routes && result.routes.length > 0) {
            route = result.routes[0]
          } else if (result.route) {
            route = result.route
          } else if (result.paths && result.paths.length > 0) {
            route = result.paths[0]
          }
          
          if (route) {
            // 提取距离
            if ((!this.navigationInfo.distance || this.navigationInfo.distance.includes('计算中') || this.navigationInfo.distance.includes('未知')) && route.distance) {
              const distanceKm = (route.distance / 1000).toFixed(2)
              this.navigationInfo.distance = `${distanceKm} 公里`
            }
            
            // 提取时间
            if ((!this.navigationInfo.duration || this.navigationInfo.duration.includes('计算中') || this.navigationInfo.duration.includes('未知')) && route.time) {
              const minutes = Math.floor(route.time / 60)
              const hours = Math.floor(minutes / 60)
              if (hours > 0) {
                this.navigationInfo.duration = `${hours}小时${minutes % 60}分钟`
              } else {
                this.navigationInfo.duration = `${minutes}分钟`
              }
            }
            
            // 提取路线步骤 - 优先从route.steps提取
            if ((!this.navigationInfo.steps || this.navigationInfo.steps.length === 0)) {
              let steps = null
              
              // 尝试多种可能的数据结构
              if (route.steps && Array.isArray(route.steps) && route.steps.length > 0) {
                steps = route.steps
              } else if (route.segments && Array.isArray(route.segments) && route.segments.length > 0) {
                // 某些API可能返回segments
                steps = route.segments
              } else if (result.steps && Array.isArray(result.steps) && result.steps.length > 0) {
                steps = result.steps
              }
              
              if (steps && steps.length > 0) {
                this.navigationInfo.steps = steps.map((step, index) => {
                  // 处理不同格式的step数据
                  let instruction = step.instruction || step.road || step.action || step.dir || ''
                  let distance = step.distance || 0
                  
                  // 如果instruction为空，尝试从其他字段获取
                  if (!instruction && step.assistant_action) {
                    instruction = step.assistant_action
                  }
                  
                  // 格式化距离，确保有单位
                  distance = this.formatDistance(distance)
                  
                  return {
                    instruction: instruction || `第${index + 1}步`,
                    distance: distance
                  }
                })
                console.log('从高德地图API提取到路径步骤:', this.navigationInfo.steps.length, '条', this.routeMode)
              }
            }
          }
        }
        
        // 如果仍然没有steps，尝试从result直接提取
        if ((!this.navigationInfo.steps || this.navigationInfo.steps.length === 0) && result) {
          if (result.steps && Array.isArray(result.steps) && result.steps.length > 0) {
            this.navigationInfo.steps = result.steps.map((step, index) => ({
              instruction: step.instruction || step.road || step.action || `第${index + 1}步`,
              distance: this.formatDistance(step.distance)
            }))
            console.log('从result直接提取到路径步骤:', this.navigationInfo.steps.length, '条')
          }
        }
        
        // 调试输出
        if (this.navigationInfo.steps && this.navigationInfo.steps.length > 0) {
          console.log('最终路径步骤数量:', this.navigationInfo.steps.length)
        } else {
          console.warn('未找到路径步骤数据，result结构:', result)
        }
      } catch (error) {
        console.error('解析路线结果失败:', error, result)
      }
    },

    drawRouteFromSteps(steps, originLng, originLat, destLng, destLat) {
      // 如果后端返回了步骤数据，更新导航信息
      if (this.navigationInfo && steps && Array.isArray(steps)) {
        this.navigationInfo.steps = steps.map(step => ({
          instruction: step.instruction || step.road || '',
          distance: this.formatDistance(step.distance)
        }))
      }
      // 使用插件绘制路线
      this.drawRouteWithPlugin(originLng, originLat, destLng, destLat)
    },

    async getCurrentLocation() {
      if (!this.AMap || !this.map) {
        this.$message.warning('地图未初始化，无法定位')
        return false
      }

      this.locating = true
      
      try {
        // 优先尝试GPS高精度定位
        const gpsSuccess = await this.tryGPSLocation()
        if (gpsSuccess) {
          this.locating = false
          return true
        }

        // GPS定位失败，尝试IP定位作为备选
        const ipLocationSuccess = await this.tryIPLocation()
        if (ipLocationSuccess) {
          this.locating = false
          return true
        }

        // 所有定位方式都失败，不设置默认位置，返回false
        this.locating = false
        return false
      } catch (error) {
        console.error('定位功能发生错误:', error)
        this.locating = false
        return false
      }
    },
    
    // 尝试GPS定位
    async tryGPSLocation() {
      if (!this.AMap || !this.map) {
        return false
      }
      
      try {
        // 配置定位选项
        const geolocation = new this.AMap.Geolocation({
          enableHighAccuracy: true, // 启用高精度定位
          timeout: 15000, // 15秒超时
          maximumAge: 30000, // 允许使用30秒内的缓存位置
          convert: true, // 自动偏移坐标，偏移后的坐标为高德坐标
          showButton: false, // 不显示定位按钮
          buttonDom: null,
          showMarker: true, // 显示定位标记
          showCircle: true, // 显示定位精度圆圈
          panToLocation: true, // 定位成功后自动移动地图
          zoomToAccuracy: true // 定位成功后自动调整地图缩放级别
        })

        // 使用Promise包装，便于处理超时
        const locationPromise = new Promise((resolve, reject) => {
          const timeoutId = setTimeout(() => {
            reject(new Error('GPS定位超时'))
          }, 16000) // 16秒超时

          geolocation.getCurrentPosition((status, result) => {
            clearTimeout(timeoutId)
            if (status === 'complete') {
              resolve(result)
            } else {
              reject(result || new Error('GPS定位失败'))
            }
          })
        })

        const result = await locationPromise
        
        // 定位成功
        const position = result.position
        if (position) {
          this.currentLocation = {
            lng: position.getLng(),
            lat: position.getLat()
          }
          
          // 添加逆地理编码获取具体地址
          await this.getAddressByLocation(this.currentLocation)
          
          // 在地图上显示当前位置标记
          this.showCurrentLocationMarker()
          
          // 移动地图到当前位置
          this.map.setCenter([this.currentLocation.lng, this.currentLocation.lat])
          this.map.setZoom(15) // 设置合适的缩放级别
          
          this.$message.success(`已定位到：${this.currentLocation.address || '当前位置'}`)
          return true
        } else {
          throw new Error('无法获取GPS位置信息')
        }
      } catch (error) {
        console.error('GPS定位失败:', error)
        // 提供更详细的错误反馈
        if (error.message && error.message.includes('timeout')) {
          console.error('GPS定位超时，请检查设备定位功能是否开启')
        } else if (error.message && error.message.includes('denied')) {
          console.error('GPS定位被拒绝，请在浏览器设置中允许定位权限')
        }
        return false
      }
    },

    // 通过经纬度获取具体地址（逆地理编码）
    async getAddressByLocation(location) {
      if (!this.AMap || !this.map || !location || !location.lng || !location.lat) {
        return
      }
      
      try {
        // 动态加载Geocoder插件
        await new Promise((resolve, reject) => {
          this.AMap.plugin('AMap.Geocoder', (error) => {
            if (error) {
              reject(error)
            } else {
              resolve()
            }
          })
        })
        
        // 创建Geocoder实例
        const geocoder = new this.AMap.Geocoder({
          radius: 1000,
          extensions: 'all'
        })
        
        // 获取地址信息
        const result = await new Promise((resolve, reject) => {
          geocoder.getAddress([location.lng, location.lat], (status, result) => {
            if (status === 'complete' && result && result.regeocode) {
              resolve(result)
            } else {
              reject(new Error('逆地理编码失败'))
            }
          })
        })
        
        // 更新currentLocation中的地址信息
        if (result.regeocode) {
          this.currentLocation.address = result.regeocode.formattedAddress || ''
          this.currentLocation.addressDetail = result.regeocode.addressComponent || {}
          
          // 记录详细地址信息到控制台
          console.log('当前位置详细信息:', {
            经纬度: [location.lng, location.lat],
            完整地址: this.currentLocation.address,
            省: this.currentLocation.addressDetail.province,
            市: this.currentLocation.addressDetail.city,
            区: this.currentLocation.addressDetail.district,
            街道: this.currentLocation.addressDetail.street,
            门牌号: this.currentLocation.addressDetail.streetNumber
          })
        }
      } catch (error) {
        console.error('获取地址信息失败:', error)
        // 不影响主要定位功能
      }
    },
    
    // IP定位作为备选方案
    async tryIPLocation() {
      if (!this.AMap || !this.map) {
        return false
      }

      return new Promise((resolve) => {
        try {
          // 检查CitySearch插件是否已加载
          if (!this.AMap.CitySearch) {
            // 动态加载插件
            this.AMap.plugin('AMap.CitySearch', () => {
              this.performIPLocation(resolve)
            })
          } else {
            this.performIPLocation(resolve)
          }
        } catch (error) {
          console.error('IP定位初始化失败:', error)
          resolve(false)
        }
      })
    },

    // 执行IP定位
    performIPLocation(resesolve) {
      try {
        const citySearch = new this.AMap.CitySearch()
        
        // 设置超时
        const timeoutId = setTimeout(() => {
          resesolve(false)
        }, 5000)

        citySearch.getLocalCity((status, result) => {
          clearTimeout(timeoutId)
          
          if (status === 'complete' && result && result.bounds) {
            try {
              // 使用城市中心点
              const center = result.bounds.getCenter()
              this.currentLocation = {
                lng: center.getLng(),
                lat: center.getLat()
              }
              
              // 获取具体地址信息
              this.getAddressByLocation(this.currentLocation)
              
              // 在地图上显示当前位置标记
              this.showCurrentLocationMarker()
              
              // 移动地图到当前位置
              this.map.setCenter([this.currentLocation.lng, this.currentLocation.lat])
              this.map.setZoom(13)
              this.$message.success('已定位到' + (result.city || '当前位置'))
              resesolve(true)
            } catch (error) {
              console.error('设置IP定位中心失败:', error)
              resesolve(false)
            }
          } else {
            resesolve(false)
          }
        })
      } catch (error) {
        console.error('IP定位执行失败:', error)
        resesolve(false)
      }
    },

    // 使用默认位置（太原市中心）
    useDefaultLocation() {
      if (!this.map) {
        return
      }

      // 使用太原市中心作为默认位置
      const defaultLocation = {
        lng: 112.549248,
        lat: 37.857014
      }

      this.currentLocation = defaultLocation
      
      // 获取具体地址信息
      this.getAddressByLocation(this.currentLocation)
      
      // 在地图上显示当前位置标记
      this.showCurrentLocationMarker()
      
      // 移动地图到默认位置
      this.map.setCenter([defaultLocation.lng, defaultLocation.lat])
      this.map.setZoom(13)
      
      this.$message({
        message: '定位失败，已定位到太原市中心。您可以：1) 允许浏览器定位权限 2) 在地图上点击设置起点 3) 使用搜索功能',
        type: 'info',
        duration: 5000
      })
    },
    
    // 在地图上显示当前位置标记
    showCurrentLocationMarker() {
      if (!this.AMap || !this.map || !this.currentLocation || !this.currentLocation.lng || !this.currentLocation.lat) {
        return
      }
      
      // 清除之前的当前位置标记
      if (this.currentLocationMarker) {
        this.currentLocationMarker.setMap(null)
        this.currentLocationMarker = null
      }
      
      // 创建当前位置标记图标（蓝色定位图标）
      const icon = new this.AMap.Icon({
        size: new this.AMap.Size(32, 32),
        image: 'https://webapi.amap.com/theme/v1.3/markers/n/loc.png', // 高德地图定位图标
        imageSize: new this.AMap.Size(32, 32)
      })
      
      // 创建标记
      this.currentLocationMarker = new this.AMap.Marker({
        position: [this.currentLocation.lng, this.currentLocation.lat],
        icon: icon,
        title: '当前位置',
        zIndex: 1000, // 设置较高的层级，确保显示在其他标记之上
        offset: new this.AMap.Pixel(-16, -32) // 调整图标位置，使图标底部对准位置点
      })
      
      // 添加标记到地图
      this.currentLocationMarker.setMap(this.map)
      
      // 创建信息窗口显示当前位置信息
      const infoWindow = new this.AMap.InfoWindow({
        content: `<div style="padding: 5px;">
          <div style="font-weight: bold; margin-bottom: 5px;">📍 当前位置</div>
          <div style="color: #666; font-size: 12px;">${this.currentLocation.address || '当前位置'}</div>
        </div>`,
        offset: new this.AMap.Pixel(0, -20),  // 向上偏移20px，让弹窗底部小三角对准标记点
        closeWhenClickMap: true,
        autoMove: true
      })
      
      // 点击标记时显示信息窗口
      this.currentLocationMarker.on('click', () => {
        infoWindow.open(this.map, this.currentLocationMarker.getPosition())
      })
      
      console.log('当前位置标记已添加到地图:', {
        经度: this.currentLocation.lng,
        纬度: this.currentLocation.lat,
        地址: this.currentLocation.address
      })
    },

    handleTypeChange() {
      this.renderMarkers()
    },

    async handleSearch() {
      if (!this.searchKeyword.trim()) {
        this.$message.warning('请输入搜索关键词')
        return
      }

      if (!this.map || !this.AMap) {
        this.$message.warning('地图未初始化，无法搜索')
        return
      }

      const keyword = this.searchKeyword.trim()
      console.log('开始搜索:', keyword)

      // 先尝试后端API搜索
      try {
        const res = await searchPlace(keyword, '太原')
        console.log('后端搜索响应:', res)
        
        if (res.code === 200 && res.data && Array.isArray(res.data) && res.data.length > 0) {
          // 后端搜索成功
          this.clearMarkers()
          
          let validCount = 0
          // 太原市的大致范围（用于过滤）
          const taiyuanBounds = {
            minLng: 112.3,
            maxLng: 112.8,
            minLat: 37.6,
            maxLat: 38.1
          }
          
          res.data.forEach(place => {
            if (place && place.longitude != null && place.latitude != null) {
              const lng = parseFloat(place.longitude)
              const lat = parseFloat(place.latitude)
              
              // 检查是否在太原范围内
              const inTaiyuan = lng >= taiyuanBounds.minLng && lng <= taiyuanBounds.maxLng &&
                               lat >= taiyuanBounds.minLat && lat <= taiyuanBounds.maxLat
              
              // 如果不在太原范围内，检查城市名称
              if (!inTaiyuan && place.city) {
                if (!place.city.includes('太原') && !place.city.includes('山西')) {
                  console.log('过滤非太原结果:', place.name, place.city, lng, lat)
                  return // 跳过非太原的结果
                }
              }
              
              try {
                this.addMarker(place, 'search', '#F56C6C')
                validCount++
                console.log('添加搜索结果:', place.name, place.city || '', lng, lat)
              } catch (error) {
                console.error('添加搜索标记失败:', error, place)
              }
            }
          })

          if (validCount > 0 && this.markers.length > 0) {
            try {
              // 检查搜索结果是否在太原范围内（粗略检查）
              const taiyuanBounds = {
                minLng: 112.3,
                maxLng: 112.8,
                minLat: 37.6,
                maxLat: 38.1
              }
              
              // 过滤出在太原范围内的标记
              const validMarkers = this.markers.filter(marker => {
                const pos = marker.getPosition()
                const lng = pos.getLng()
                const lat = pos.getLat()
                return lng >= taiyuanBounds.minLng && lng <= taiyuanBounds.maxLng &&
                       lat >= taiyuanBounds.minLat && lat <= taiyuanBounds.maxLat
              })
              
              if (validMarkers.length > 0) {
                // 使用有效标记调整视野
                this.map.setFitView(validMarkers, false, [50, 50, 50, 50])
                this.$message.success(`找到 ${validCount} 个结果`)
              } else {
                // 如果所有结果都不在太原范围内，定位到第一个结果但提示
                const firstMarker = this.markers[0]
                const pos = firstMarker.getPosition()
                this.map.setCenter([pos.getLng(), pos.getLat()])
                this.map.setZoom(15)
                this.$message.warning(`找到 ${validCount} 个结果，但可能不在太原范围内`)
              }
            } catch (error) {
              console.error('调整地图视野失败:', error)
              this.$message.success(`找到 ${validCount} 个结果`)
            }
            return // 搜索成功，退出
          }
        }
        
        // 后端搜索无结果，尝试前端搜索
        console.log('后端搜索无结果，尝试前端搜索')
        this.tryFrontendSearch(keyword)
        
      } catch (error) {
        console.error('后端搜索失败:', error)
        // 后端搜索失败，尝试前端搜索
        this.tryFrontendSearch(keyword)
      }
    },

    // 前端直接搜索（使用高德地图JS API）
    tryFrontendSearch(keyword) {
      if (!this.AMap || !this.map) {
        this.$message.error('地图未初始化，无法搜索')
        return
      }

      try {
        // 动态加载PlaceSearch插件
        this.AMap.plugin('AMap.PlaceSearch', (error) => {
          if (error) {
            console.error('加载PlaceSearch插件失败:', error)
            // 检查是否是INVALID_USER_SCODE错误
            const errorStr = error ? error.toString() : ''
            if (errorStr.includes('INVALID_USER_SCODE') || 
                errorStr.includes('安全密钥') ||
                errorStr.includes('安全域名')) {
              const errorMsg = `Error No API Key!`
              console.error(errorMsg)
              this.$message.error('地图API密钥配置错误，请查看控制台详细说明')
            } else {
              this.$message.error('加载搜索插件失败，请稍后重试')
            }
            return
          }
          
          const placeSearch = new this.AMap.PlaceSearch({
            city: '太原',
            citylimit: true,
            pageSize: 10,
            pageIndex: 1
          })

          placeSearch.search(keyword, (status, result) => {
            console.log('前端搜索回调 - status:', status, 'result:', result)
            
            if (status === 'complete' && result && result.poiList && result.poiList.pois) {
              const pois = result.poiList.pois
              if (pois.length > 0) {
                this.clearMarkers()
                
                let validCount = 0
                // 太原市的大致范围
                const taiyuanBounds = {
                  minLng: 112.3,
                  maxLng: 112.8,
                  minLat: 37.6,
                  maxLat: 38.1
                }
                
                pois.forEach(poi => {
                  if (poi.location) {
                    const lng = poi.location.lng
                    const lat = poi.location.lat
                    
                    // 检查是否在太原范围内
                    const inTaiyuan = lng >= taiyuanBounds.minLng && lng <= taiyuanBounds.maxLng &&
                                     lat >= taiyuanBounds.minLat && lat <= taiyuanBounds.maxLat
                    
                    // 如果不在太原范围内，检查城市信息
                    if (!inTaiyuan) {
                      const city = poi.cityname || poi.adname || ''
                      if (city && !city.includes('太原') && !city.includes('山西')) {
                        console.log('过滤非太原结果:', poi.name, city, lng, lat)
                        return // 跳过非太原的结果
                      }
                    }
                    
                    const place = {
                      id: poi.id,
                      name: poi.name,
                      address: poi.address,
                      longitude: lng,
                      latitude: lat,
                      type: poi.type,
                      tel: poi.tel,
                      city: poi.cityname || poi.adname
                    }
                    try {
                      this.addMarker(place, 'search', '#F56C6C')
                      validCount++
                      console.log('添加前端搜索结果:', poi.name, place.city || '', lng, lat)
                    } catch (error) {
                      console.error('添加前端搜索标记失败:', error)
                    }
                  }
                })

                if (validCount > 0 && this.markers.length > 0) {
                  try {
                    // 检查搜索结果是否在太原范围内
                    const taiyuanBounds = {
                      minLng: 112.3,
                      maxLng: 112.8,
                      minLat: 37.6,
                      maxLat: 38.1
                    }
                    
                    const validMarkers = this.markers.filter(marker => {
                      const pos = marker.getPosition()
                      const lng = pos.getLng()
                      const lat = pos.getLat()
                      return lng >= taiyuanBounds.minLng && lng <= taiyuanBounds.maxLng &&
                             lat >= taiyuanBounds.minLat && lat <= taiyuanBounds.maxLat
                    })
                    
                    if (validMarkers.length > 0) {
                      this.map.setFitView(validMarkers, false, [50, 50, 50, 50])
                      this.$message.success(`找到 ${validCount} 个结果（前端搜索）`)
                    } else {
                      // 如果结果不在太原，定位到第一个但提示
                      const firstMarker = this.markers[0]
                      const pos = firstMarker.getPosition()
                      this.map.setCenter([pos.getLng(), pos.getLat()])
                      this.map.setZoom(15)
                      this.$message.warning(`找到 ${validCount} 个结果，但可能不在太原范围内`)
                    }
                  } catch (error) {
                    console.error('调整地图视野失败:', error)
                    this.$message.success(`找到 ${validCount} 个结果（前端搜索）`)
                  }
                } else {
                  this.$message.info('未找到相关地点')
                }
              } else {
                this.$message.info(`未找到"${keyword}"相关地点，请尝试其他关键词`)
              }
            } else if (status === 'error') {
              console.error('前端搜索失败 - status:', status, 'result:', result)
              // 检查是否是INVALID_USER_SCODE错误
              const resultStr = result ? result.toString() : ''
              if (resultStr.includes('INVALID_USER_SCODE') || 
                  resultStr.includes('安全密钥') ||
                  resultStr.includes('安全域名')) {
                const errorMsg = `
前端地图API密钥配置错误！

错误类型：INVALID_USER_SCODE（安全密钥验证失败）

请检查：
1. Key类型是否为「Web端(JS API)」
2. 安全域名是否包含当前访问地址
3. Key是否已启用

当前Key：8d161195e187f5dc4ca5579fac8f7691
                `
                console.error(errorMsg)
                this.$message.error('地图API密钥配置错误，请查看控制台详细说明')
              } else {
                this.$message.warning('搜索失败，请检查：1) 后端API密钥配置 2) 网络连接')
              }
            } else {
              console.warn('前端搜索状态:', status, result)
              this.$message.info('未找到相关地点')
            }
          })
        })
      } catch (error) {
        console.error('前端搜索异常:', error)
        // 检查是否是INVALID_USER_SCODE错误
        const errorStr = error ? error.toString() : ''
        if (errorStr.includes('INVALID_USER_SCODE') || 
            errorStr.includes('安全密钥') ||
            errorStr.includes('安全域名')) {
          this.$message.error('地图API密钥配置错误，请查看控制台')
        } else {
          this.$message.error('搜索功能异常，请检查地图API配置')
        }
      }
    },

    clearRoute() {
      if (this.route) {
        try {
          this.route.clear()
        } catch (error) {
          console.error('清除路线失败:', error)
        }
        this.route = null
      }
      // 清除路线时不关闭导航面板，但可以清空步骤信息
      if (this.navigationInfo) {
        this.navigationInfo.steps = []
      }
    },
    
    // 检查URL参数，用于从详情页跳转过来时定位
    checkLocationParams() {
      const query = this.$route.query
      if (query.lng && query.lat) {
        // 保存参数，等待地图初始化完成后使用
        this.locationParams = {
          type: query.type || 'attraction',
          id: query.id,
          lng: parseFloat(query.lng),
          lat: parseFloat(query.lat),
          name: query.name || '位置'
        }
      }
    },
    
    // 处理定位参数，在地图初始化完成后调用
    handleLocationParams() {
      if (!this.locationParams || !this.map || !this.AMap) {
        return
      }
      
      const { lng, lat, name, type, id } = this.locationParams
      
      if (isNaN(lng) || isNaN(lat)) {
        console.warn('无效的坐标参数:', this.locationParams)
        return
      }
      
      try {
        // 定位到指定坐标
        this.map.setCenter([lng, lat])
        this.map.setZoom(16)
        
        // 创建并显示标记
        const marker = new this.AMap.Marker({
          position: [lng, lat],
          title: name,
          icon: this.createMarkerIcon(type || 'attraction')
        })
        
        marker.setMap(this.map)
        this.markers.push(marker)
        
        // 显示信息窗口
        const placeData = {
          id: id,
          name: name,
          longitude: lng,
          latitude: lat,
          type: type || 'attraction',
          address: ''
        }
        
        setTimeout(() => {
          this.showInfoWindow(marker, placeData)
        }, 500)
        
        // 清除参数，避免重复定位
        this.locationParams = null
        
        this.$message.success(`已定位到：${name}`)
      } catch (error) {
        console.error('定位失败:', error)
        this.$message.error('定位失败')
      }
    },
    
    // 跳转到详情页
    goToDetail(type, id) {
      if (!type || !id) {
        this.$message.warning('缺少必要参数')
        return
      }
      
      let path = ''
      switch (type) {
        case 'attraction':
          path = `/attraction/${id}`
          break
        case 'hotel':
          path = `/hotel/${id}`
          break
        case 'culture':
          path = `/culture/${id}`
          break
        case 'restaurant':
          // 餐厅可能来自美食的推荐餐厅，暂时跳转到美食列表
          this.$message.info('餐厅详情功能开发中')
          return
        default:
          this.$message.warning('未知的类型')
          return
      }
      
      if (path) {
        this.$router.push(path)
      }
    },

    clearMarkers() {
      this.markers.forEach(marker => {
        try {
          marker.setMap(null)
        } catch (error) {
          // 忽略清除错误
        }
      })
      this.markers = []
      this.infoWindows.forEach(win => {
        try {
          win.close()
        } catch (error) {
          // 忽略关闭错误
        }
      })
      this.infoWindows = []
    },

    // 关闭导航面板
    closeNavigationPanel() {
      this.showNavigationPanel = false
      this.navigationInfo = null
      this.currentDestination = null
    },

    // 重新规划路线
    async refreshRoute() {
      if (!this.currentDestination) {
        this.$message.warning('没有可重新规划的路线')
        return
      }

      this.routeRefreshing = true
      try {
        await this.startNavigation(
          this.currentDestination.lng,
          this.currentDestination.lat,
          this.currentDestination.name
        )
      } finally {
        this.routeRefreshing = false
      }
    },

    // 获取导航模式文本
    getRouteModeText(mode) {
      const modeMap = {
        driving: '驾车',
        walking: '步行',
        riding: '骑行'
      }
      return modeMap[mode] || mode || '驾车'
    },
    
    // 格式化距离，确保有单位
    formatDistance(distance) {
      if (!distance) return ''
      
      // 如果已经是字符串格式
      if (typeof distance === 'string') {
        // 检查是否已经包含单位
        if (distance.includes('米') || distance.includes('公里') || distance.includes('km') || distance.includes('m')) {
          return distance
        }
        // 如果是纯数字字符串，添加单位
        const num = parseFloat(distance)
        if (!isNaN(num)) {
          if (num >= 1000) {
            return `${(num / 1000).toFixed(2)}公里`
          } else {
            return `${num}米`
          }
        }
        return distance
      }
      
      // 如果是数字
      if (typeof distance === 'number') {
        if (distance >= 1000) {
          return `${(distance / 1000).toFixed(2)}公里`
        } else {
          return `${distance}米`
        }
      }
      
      return String(distance)
    },
    
    // 格式化指令文本，提取并清理内容
    formatInstruction(instruction) {
      if (!instruction) return '继续前行'
      
      // 移除指令中可能包含的距离信息（因为距离会单独显示）
      // 但保留主要指令内容
      let formatted = instruction.trim()
      
      // 如果指令中包含距离，可以保留（因为有些指令需要距离信息才有意义）
      // 例如："沿科明巷向西行驶192米左转" 中的距离是必要的
      
      return formatted
    },
    
    // 提取动作类型（左转、右转、直行等）
    getActionType(instruction) {
      if (!instruction) return ''
      
      const actionMap = {
        '左转': '左转',
        '右转': '右转',
        '直行': '直行',
        '掉头': '掉头',
        '向左前方': '左前方',
        '向右前方': '右前方',
        '向左后方': '左后方',
        '向右后方': '右后方',
        '进入': '进入',
        '驶入': '驶入',
        '驶出': '驶出',
        '到达': '到达'
      }
      
      for (const [key, value] of Object.entries(actionMap)) {
        if (instruction.includes(key)) {
          return value
        }
      }
      
      return ''
    },
    
    // 获取动作图标
    getActionIcon(instruction) {
      if (!instruction) return 'el-icon-right'
      
      if (instruction.includes('左转') || instruction.includes('向左')) {
        return 'el-icon-back'
      } else if (instruction.includes('右转') || instruction.includes('向右')) {
        return 'el-icon-right'
      } else if (instruction.includes('直行') || instruction.includes('继续')) {
        return 'el-icon-top'
      } else if (instruction.includes('掉头')) {
        return 'el-icon-refresh-left'
      } else if (instruction.includes('到达')) {
        return 'el-icon-location'
      }
      
      return 'el-icon-right'
    },
    
    // 重置地图状态（用于页面刷新时）
    resetMapState() {
      // 清除搜索关键词
      this.searchKeyword = ''
      
      // 清除路线规划相关状态
      this.showNavigationPanel = false
      this.navigationInfo = null
      this.currentDestination = null
      this.routeRefreshing = false
      
      // 清除路线（如果地图已初始化）
      if (this.route) {
        try {
          this.route.clear()
        } catch (error) {
          console.error('清除路线失败:', error)
        }
        this.route = null
      }
      
      // 清除定位参数
      this.locationParams = null
      
      // 清除所有标记和信息窗口（如果地图已初始化）
      if (this.map) {
        this.clearMarkers()
      }
      
      // 清除当前位置标记（刷新后重新定位）
      if (this.currentLocationMarker) {
        try {
          this.currentLocationMarker.setMap(null)
          this.currentLocationMarker = null
        } catch (error) {
          console.error('清除当前位置标记失败:', error)
        }
      }
      
      // 重置当前位置为默认值
      this.currentLocation = {
        lng: 112.549656,
        lat: 37.870451,
        address: '太原市中心'
      }
      
      // 重置类型筛选为默认值（显示所有类型）
      this.selectedTypes = ['attraction', 'restaurant', 'hotel', 'culture']
      
      // 重置导航模式为默认值
      this.routeMode = 'driving'
      
      console.log('地图状态已重置（页面刷新）')
    }
  }
}
</script>

<style lang="scss" scoped>
// 使用说明对话框样式
::v-deep .help-content {
  .help-section {
    padding: 10px 0;
    
    h4 {
      margin: 0 0 10px 0;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      display: flex;
      align-items: center;
      gap: 8px;
      
      i {
        font-size: 18px;
        color: #409EFF;
      }
    }
    
    ul {
      margin: 0;
      padding-left: 20px;
      color: #606266;
      line-height: 1.8;
      
      li {
        margin-bottom: 8px;
        
        &:last-child {
          margin-bottom: 0;
        }
      }
    }
  }
  
  .el-collapse {
    border: none;
    
    ::v-deep .el-collapse-item {
      margin-bottom: 10px;
      border: 1px solid #EBEEF5;
      border-radius: 4px;
      
      .el-collapse-item__header {
        padding: 15px 20px;
        font-weight: 600;
        color: #303133;
        background-color: #F5F7FA;
        border-radius: 4px 4px 0 0;
        
        &:hover {
          background-color: #ECF5FF;
        }
      }
      
      .el-collapse-item__content {
        padding: 20px;
        background-color: #fff;
      }
      
      &.is-active {
        .el-collapse-item__header {
          background-color: #ECF5FF;
          color: #409EFF;
        }
      }
    }
  }
}

.map-page {
  padding: 20px 0;
  min-height: calc(100vh - 200px);

  .container {
    width: 1200px;
    max-width: 100%;
    margin: 0 auto;
    padding: 0 20px;
  }

  .page-title {
    font-size: 32px;
    font-weight: 700;
    margin-bottom: 32px;
    color: #303133;
    position: relative;
    padding-bottom: 16px;
    letter-spacing: 0.5px;
  }

  .page-title::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 80px;
    height: 4px;
    background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
    border-radius: 2px;
  }

  .map-controls {
    margin-bottom: 20px;

    .control-card {
      border-radius: 8px;
    }

    .control-row {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 15px;

      .label {
        font-size: 14px;
        color: #606266;
        margin-right: 10px;
      }

      .filter-group {
        display: flex;
        align-items: center;
      }

      .search-group {
        display: flex;
        align-items: center;
        margin-left: auto;
      }

      .route-group {
        display: flex;
        align-items: center;
      }
    }
  }

  .map-wrapper {
    position: relative;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

    .map-container {
      width: 100%;
      height: 600px;
    }

    .map-legend {
      position: absolute;
      bottom: 20px;
      right: 20px;
      background: rgba(255, 255, 255, 0.95);
      padding: 10px 15px;
      border-radius: 4px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
      z-index: 1000;

      .legend-item {
        display: flex;
        align-items: center;
        margin-bottom: 8px;
        font-size: 13px;
        color: #606266;

        &:last-child {
          margin-bottom: 0;
        }

        .legend-icon {
          display: inline-block;
          width: 20px;
          height: 20px;
          border-radius: 50%;
          margin-right: 8px;
          border: 2px solid #fff;
          box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);

          &.attraction {
            background: #409EFF;
          }

          &.restaurant {
            background: #67C23A;
          }

          &.hotel {
            background: #E6A23C;
          }

          &.culture {
            background: #F56C6C;
          }
        }
      }
    }
  }

  /* 导航信息浮窗 */
  .navigation-panel {
    position: fixed;
    top: 50%;
    right: 20px;
    transform: translateY(-50%);
    width: 380px;
    max-height: 80vh;
    z-index: 2000;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
    border-radius: 12px;
    overflow: hidden;

    .panel-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0;

      .panel-title {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        display: flex;
        align-items: center;
        gap: 8px;

        i {
          color: #409EFF;
        }
      }

      .close-btn {
        padding: 0;
        font-size: 18px;
        color: #909399;

        &:hover {
          color: #303133;
        }
      }
    }

    .navigation-content {
      max-height: calc(80vh - 100px);
      overflow-y: auto;
    }

    .route-points {
      margin-bottom: 20px;

      .route-point {
        display: flex;
        align-items: flex-start;
        gap: 12px;
        padding: 12px;
        background: #f5f7fa;
        border-radius: 8px;
        margin-bottom: 8px;

        &.origin {
          .point-icon {
            background: #67C23A;
          }
        }

        &.destination {
          .point-icon {
            background: #F56C6C;
          }
        }

        .point-icon {
          width: 36px;
          height: 36px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          color: #fff;
          flex-shrink: 0;

          i {
            font-size: 18px;
          }
        }

        .point-info {
          flex: 1;
          min-width: 0;

          .point-label {
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }

          .point-address {
            font-size: 14px;
            color: #303133;
            line-height: 1.5;
            word-break: break-all;
          }
        }
      }

      .route-divider {
        text-align: center;
        color: #C0C4CC;
        margin: 8px 0;
        padding: 0 12px;

        i {
          font-size: 16px;
        }
      }
    }

    .route-stats {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 12px;
      margin-bottom: 20px;
      padding-bottom: 20px;
      border-bottom: 1px solid #EBEEF5;

      .stat-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        text-align: center;
        padding: 12px;
        background: #f5f7fa;
        border-radius: 8px;

        i {
          font-size: 24px;
          color: #409EFF;
          margin-bottom: 8px;
        }

        .stat-content {
          .stat-label {
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }

          .stat-value {
            font-size: 14px;
            font-weight: 600;
            color: #303133;
            word-break: break-all;
          }
        }
      }
    }

    .route-steps {
      margin-bottom: 20px;

      .steps-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 12px;
        display: flex;
        align-items: center;
        gap: 8px;

        i {
          color: #409EFF;
        }
      }

      .steps-list {
        max-height: 300px;
        overflow-y: auto;

        .step-item {
          display: flex;
          gap: 12px;
          padding: 12px;
          margin-bottom: 8px;
          background: #f5f7fa;
          border-radius: 8px;
          border-left: 3px solid #409EFF;

          .step-number {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            background: #409EFF;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            font-weight: 600;
            flex-shrink: 0;
          }

          .step-content {
            flex: 1;
            min-width: 0;

            .step-instruction {
              font-size: 14px;
              color: #303133;
              line-height: 1.6;
              margin-bottom: 8px;
              word-break: break-all;
              font-weight: 500;
            }

            .step-details {
              display: flex;
              align-items: center;
              gap: 12px;
              flex-wrap: wrap;
            }

            .step-distance {
              font-size: 13px;
              color: #409EFF;
              display: flex;
              align-items: center;
              gap: 4px;
              background: #ecf5ff;
              padding: 4px 8px;
              border-radius: 4px;
              
              i {
                font-size: 14px;
              }
            }

            .step-action {
              font-size: 13px;
              color: #67C23A;
              display: flex;
              align-items: center;
              gap: 4px;
              background: #f0f9ff;
              padding: 4px 8px;
              border-radius: 4px;
              
              i {
                font-size: 14px;
              }
            }
          }
        }
      }
    }

    .navigation-actions {
      display: flex;
      gap: 10px;
      padding-top: 15px;
      border-top: 1px solid #EBEEF5;

      .el-button {
        flex: 1;
      }
    }

    .loading-state {
      text-align: center;
      padding: 40px 20px;
      color: #909399;

      i {
        font-size: 32px;
        margin-bottom: 12px;
        color: #409EFF;
      }

      p {
        margin: 0;
        font-size: 14px;
      }
    }
  }

  /* 浮窗动画 */
  ::v-deep .slide-fade-enter-active {
    transition: all 0.3s ease-out;
  }

  ::v-deep .slide-fade-leave-active {
    transition: all 0.3s ease-in;
  }

  ::v-deep .slide-fade-enter {
    transform: translateX(100%) translateY(-50%);
    opacity: 0;
  }

  ::v-deep .slide-fade-leave-to {
    transform: translateX(100%) translateY(-50%);
    opacity: 0;
  }
}

@media (max-width: 768px) {
  .map-page {
    .page-title {
      font-size: 22px;
      margin-bottom: 20px;
    }

    .map-controls {
      .control-row {
        flex-direction: column;
        align-items: flex-start;

        .search-group {
          margin-left: 0;
          width: 100%;

          .el-input {
            width: 100% !important;
          }
        }
      }
    }

    .map-wrapper {
      .map-container {
        height: 500px;
      }
    }

    /* 移动端导航面板全屏显示 */
    .navigation-panel {
      position: fixed;
      top: 0;
      right: 0;
      left: 0;
      bottom: 0;
      width: 100%;
      max-height: 100vh;
      transform: none;
      border-radius: 0;
      z-index: 3000;

      .navigation-content {
        max-height: calc(100vh - 120px);
      }

      .route-stats {
        grid-template-columns: 1fr;
      }

      .route-steps .steps-list {
        max-height: 200px;
      }
    }

    .slide-fade-enter {
      transform: translateX(100%);
    }

    .slide-fade-leave-to {
      transform: translateX(100%);
    }
  }
}
</style>