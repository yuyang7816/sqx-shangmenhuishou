<template>
  <div
    class="site-wrapper site-page--main nf-main-shell"
    :class="{ 'site-sidebar--fold': sidebarFold }"
    v-loading.fullscreen.lock="loading"
    element-loading-text="正在加载系统..."
    element-loading-background="rgba(244, 246, 250, 0.88)">
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div
        class="site-content__wrapper nf-main-shell__content"
        :style="{ 'min-height': documentClientHeight + 'px' }">
        <main-content v-if="!$store.state.common.contentIsNeedRefresh" />
      </div>
    </template>
  </div>
</template>

<script>
  import MainNavbar from './main-navbar'
  import MainSidebar from './main-sidebar'
  import MainContent from './main-content'
  export default {
    provide () {
      return {
        // 刷新
        refresh () {
          this.$store.commit('common/updateContentIsNeedRefresh', true)
          this.$nextTick(() => {
            this.$store.commit('common/updateContentIsNeedRefresh', false)
          })
        }
      }
    },
    data () {
      return {
        loading: true
      }
    },
    components: {
      MainNavbar,
      MainSidebar,
      MainContent
    },
    computed: {
      documentClientHeight: {
        get () { return this.$store.state.common.documentClientHeight },
        set (val) { this.$store.commit('common/updateDocumentClientHeight', val) }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      userId: {
        get () { return this.$store.state.user.id },
        set (val) { this.$store.commit('user/updateId', val) }
      },
      userName: {
        get () { return this.$store.state.user.name },
        set (val) { this.$store.commit('user/updateName', val) }
      }
    },
    created () {
      this.getUserInfo()
    },
    mounted () {
      this.resetDocumentClientHeight()
    },
    methods: {
      // 重置窗口可视高度
      resetDocumentClientHeight () {
        this.documentClientHeight = document.documentElement['clientHeight']
        window.onresize = () => {
          this.documentClientHeight = document.documentElement['clientHeight']
        }
      },
      // 获取当前管理员信息
      getUserInfo () {
        this.$http({
          url: this.$http.adornUrl('sys/user/info'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.loading = false
            this.userId = data.data.user.userId
            this.userName = data.data.user.username
          }
        })
      }
    }
  }
</script>

<style lang="scss">
.site-wrapper.site-page--main.nf-main-shell {
  position: relative;
  min-height: 100vh;
  overflow-x: hidden;
  background: var(--nf-page-bg, #f4f6fa);

  .nf-main-shell__content,
  .site-content__wrapper {
    background: var(--nf-page-bg, #f4f6fa);
    overflow-x: hidden;
  }

  .el-loading-mask {
    background-color: rgba(244, 246, 250, 0.88) !important;
  }

  .el-loading-spinner {
    margin-top: -28px;

    .path {
      stroke: var(--nf-brand, #01af74);
    }

    i {
      color: var(--nf-brand, #01af74);
      font-size: 36px;
    }
  }

  .el-loading-text {
    margin-top: var(--nf-space-sm, 12px);
    color: var(--nf-text-secondary, #909399);
    font-size: 14px;
    letter-spacing: 0.02em;
  }
}
</style>
