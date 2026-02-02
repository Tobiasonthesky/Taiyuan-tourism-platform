<template>
  <div class="admin-users">
    <el-card>
      <div slot="header">
        <span>用户管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回</el-button>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchKeyword" placeholder="用户名/昵称/邮箱" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchRole" placeholder="选择角色" clearable>
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 用户列表 -->
      <el-table :data="users" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="role" label="角色" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.role === 'admin' ? 'danger' : ''">
              {{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEditRole(scope.row)">修改角色</el-button>
            <el-button
              size="mini"
              :type="scope.row.status === 1 ? 'danger' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <Pagination
        :current-page="page"
        :page-size="size"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 修改角色对话框 -->
    <el-dialog title="修改角色" :visible.sync="roleDialogVisible" width="400px">
      <el-form>
        <el-form-item label="当前角色">
          <el-tag :type="currentUser.role === 'admin' ? 'danger' : ''">
            {{ currentUser.role === 'admin' ? '管理员' : '普通用户' }}
          </el-tag>
        </el-form-item>
        <el-form-item label="新角色">
          <el-select v-model="newRole" placeholder="选择角色">
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmRoleChange">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUsers, updateUserRole, updateUserStatus, deleteUser } from '@/api/admin'
import Pagination from '@/components/common/Pagination'

export default {
  name: 'AdminUsers',
  components: {
    Pagination
  },
  data() {
    return {
      users: [],
      searchKeyword: '',
      searchRole: '',
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      roleDialogVisible: false,
      currentUser: {},
      newRole: ''
    }
  },
  mounted() {
    this.loadUsers()
  },
  methods: {
    async loadUsers() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.size
        }
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword
        }
        if (this.searchRole) {
          params.role = this.searchRole
        }

        const res = await getUsers(params)
        if (res.code === 200) {
          this.users = res.data?.records || []
          this.total = res.data?.total || 0
        }
      } catch (error) {
        this.$message.error('加载用户列表失败')
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page = 1
      this.loadUsers()
    },
    handleReset() {
      this.searchKeyword = ''
      this.searchRole = ''
      this.page = 1
      this.loadUsers()
    },
    handleSizeChange(val) {
      this.size = val
      this.page = 1
      this.loadUsers()
    },
    handleCurrentChange(val) {
      this.page = val
      this.loadUsers()
    },
    handleEditRole(row) {
      this.currentUser = { ...row }
      this.newRole = row.role
      this.roleDialogVisible = true
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定删除该用户吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await deleteUser(row.id)
        if (res.code === 200) {
          this.$message.success('删除成功')
          this.loadUsers()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    async confirmRoleChange() {
      if (this.newRole === this.currentUser.role) {
        this.$message.warning('角色未改变')
        return
      }

      try {
        const res = await updateUserRole(this.currentUser.id, this.newRole)
        if (res.code === 200) {
          this.$message.success('修改成功')
          this.roleDialogVisible = false
          this.loadUsers()
        }
      } catch (error) {
        this.$message.error('修改失败')
      }
    },
    async handleToggleStatus(row) {
      const newStatus = row.status === 1 ? 0 : 1
      const action = newStatus === 1 ? '启用' : '禁用'

      try {
        const res = await updateUserStatus(row.id, newStatus)
        if (res.code === 200) {
          this.$message.success(`${action}成功`)
          this.loadUsers()
        }
      } catch (error) {
        this.$message.error(`${action}失败`)
      }
    },
    goBack() {
      this.$router.push('/admin')
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-users {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }
}
</style>

