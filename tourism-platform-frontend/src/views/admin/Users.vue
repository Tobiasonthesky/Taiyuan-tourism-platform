<template>
  <div class="admin-users">
    <el-card>
      <div slot="header" class="card-header">
        <span>{{ $t('admin.userManagement') }}</span>
        <div class="card-actions">
          <el-button type="text" @click="goBack">{{ $t('common.back') }}</el-button>
        </div>
      </div>

      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item :label="$t('user.keyword')">
          <el-input v-model="searchKeyword" :placeholder="$t('user.keywordPlaceholder')" clearable @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item :label="$t('user.role')">
          <el-select v-model="searchRole" :placeholder="$t('user.selectRole')" clearable>
            <el-option :label="$t('user.normalUser')" value="user" />
            <el-option :label="$t('user.admin')" value="admin" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>

      <!-- 用户列表 -->
      <el-table :data="users" v-loading="loading" border>
        <el-table-column prop="id" :label="$t('common.id')" width="80" />
        <el-table-column prop="username" :label="$t('user.username')" width="120" />
        <el-table-column prop="nickname" :label="$t('user.nickname')" width="120" />
        <el-table-column prop="email" :label="$t('user.email')" width="180" />
        <el-table-column prop="phone" :label="$t('user.phone')" width="120" />
        <el-table-column prop="role" :label="$t('user.role')" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.role === 'admin' ? 'danger' : ''">
              {{ scope.row.role === 'admin' ? $t('user.admin') : $t('user.normalUser') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('user.status')" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? $t('user.enabled') : $t('user.disabled') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('user.createTime')" width="180" />
        <el-table-column :label="$t('common.actions')" width="320" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEditRole(scope.row)">{{ $t('user.editRole') }}</el-button>
            <el-button
              size="mini"
              :type="scope.row.status === 1 ? 'danger' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.status === 1 ? $t('user.disable') : $t('user.enable') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
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
    <el-dialog :title="$t('user.editRole')" :visible.sync="roleDialogVisible" width="400px">
      <el-form>
        <el-form-item :label="$t('user.currentRole')">
          <el-tag :type="currentUser.role === 'admin' ? 'danger' : ''">
            {{ currentUser.role === 'admin' ? $t('user.admin') : $t('user.normalUser') }}
          </el-tag>
        </el-form-item>
        <el-form-item :label="$t('user.newRole')">
          <el-select v-model="newRole" :placeholder="$t('user.selectRole')">
            <el-option :label="$t('user.normalUser')" value="user" />
            <el-option :label="$t('user.admin')" value="admin" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="roleDialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="confirmRoleChange">{{ $t('common.confirm') }}</el-button>
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
        this.$message.error(this.$t('common.operateFailed'))
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
        await this.$confirm(this.$t('admin.confirmDeleteUser'), this.$t('admin.tip'), {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        })
        const res = await deleteUser(row.id)
        if (res.code === 200) {
          this.$message.success(this.$t('common.deleteSuccess'))
          this.loadUsers()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(this.$t('common.deleteFailed'))
        }
      }
    },
    async confirmRoleChange() {
      if (this.newRole === this.currentUser.role) {
        this.$message.warning(this.$t('user.roleNotChanged'))
        return
      }

      try {
        const res = await updateUserRole(this.currentUser.id, this.newRole)
        if (res.code === 200) {
          this.$message.success(this.$t('common.updateSuccess'))
          this.roleDialogVisible = false
          this.loadUsers()
        }
      } catch (error) {
        this.$message.error(this.$t('common.updateFailed'))
      }
    },
    async handleToggleStatus(row) {
      const newStatus = row.status === 1 ? 0 : 1

      try {
        const res = await updateUserStatus(row.id, newStatus)
        if (res.code === 200) {
          this.$message.success(newStatus === 1 ? this.$t('user.enableSuccess') : this.$t('user.disableSuccess'))
          this.loadUsers()
        }
      } catch (error) {
        this.$message.error(newStatus === 1 ? this.$t('user.enableFailed') : this.$t('user.disableFailed'))
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

