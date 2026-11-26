<template>
  <div class="bond-list-page">
    <div class="bond-list-page__nav">
      <a href="#" class="bond-list-page__back-link" @click.prevent="prev">
        <icon-svg name="jiantou" class="bond-list-page__back-icon"></icon-svg>
        返回
      </a>
      <span class="bond-list-page__nav-sep">|</span>
      <span class="bond-list-page__nav-title">保证金明细</span>
    </div>

    <div class="nf-page-hero bond-list-page__hero">
      <span class="nf-page-hero__badge">商铺管理</span>
      <h2 class="nf-page-hero__title">保证金明细</h2>
      <p class="nf-page-hero__desc">查看商铺保证金缴纳与退还记录，含金额、标题、状态及创建时间</p>
    </div>

    <div class="nf-filter-card bond-list-page__filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body bond-list-page__filter-body">
        <el-button size="mini" type="primary" icon="el-icon-refresh" @click="dataSelect">刷新数据</el-button>
      </div>
    </div>

    <div class="nf-table-card bond-list-page__table-card">
      <el-table v-loading="tableDataLoading" :data="tableData.list" class="bond-list-page__table">
        <el-table-column fixed prop="id" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="money" label="保证金" width="100">
          <template slot-scope="scope">
            <span class="bond-list-page__money-out" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
            <span class="bond-list-page__money-in" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题">
        </el-table-column>
        <el-table-column prop="content" label="内容">
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150">
        </el-table-column>
        <el-table-column prop="type" label="状态" width="100" fixed="right">
          <template slot-scope="scope">
            <span v-if="scope.row.type == 1">已交保证金</span>
            <span v-if="scope.row.type == 2">已退保证金</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="bond-list-page__pagination">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next"
          :total="tableData.totalCount">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        limit: 10,
        page: 1,
        activeName: 'first',
        tableDataLoading: false,
        dialogFormVisible: false,
        tableData: {},
        shopId:'',
      }
    },
    methods: {
    

      handleSizeChange(val) {
        this.limit = val;
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val;
        this.dataSelect()
      },
			handleSizeChangeBzj(val) {
				this.limit = val
				this.baozhengjinSelect()
			},
			handleCurrentChangeBzj(val) {
				this.page = val
				this.baozhengjinSelect()
			},
      // 获取派单数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/shop/selectShopCashDepositList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'shopId': this.shopId
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData
        })
      },
      // 返回上一级
      prev() {
        this.$router.back()
      },
    },
    mounted() {
      this.shopId = this.$route.query.shopId
      this.dataSelect()
    }
  };
</script>

<style lang="scss" scoped>
.bond-list-page {
  padding: 0;
  overflow-x: hidden;
}

.bond-list-page__nav {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  color: var(--nf-text-primary, #1a2433);
}

.bond-list-page__back-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: var(--nf-brand, #01af74);
  text-decoration: none;
  cursor: pointer;
}

.bond-list-page__back-link:hover {
  color: var(--nf-brand-deep, #017c52);
}

.bond-list-page__back-icon {
  width: 1.2em;
  height: 1.2em;
}

.bond-list-page__nav-sep {
  margin: 0 12px;
  color: var(--nf-border, #e8eaed);
}

.bond-list-page__nav-title {
  font-size: 16px;
  font-weight: 600;
}

.bond-list-page__hero {
  margin-bottom: 12px;
}

.bond-list-page__filter {
  margin-bottom: 12px;
}

.bond-list-page__filter-body {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}

.bond-list-page__table-card {
  margin-bottom: 0;
  padding: 0;
  overflow: hidden;
}

.bond-list-page__table-card >>> .el-table {
  border-radius: 0;
}

.bond-list-page__table >>> .el-table__header th {
  background: var(--nf-page-bg, #f4f6fa);
  color: var(--nf-text-primary, #1a2433);
  font-weight: 600;
}

.bond-list-page__table >>> .el-table__body td {
  background: #fff;
}

.bond-list-page__money-out {
  color: #f56c6c;
}

.bond-list-page__money-in {
  color: #008200;
}

.bond-list-page__pagination {
  display: flex;
  justify-content: center;
  padding: 12px 16px;
  border-top: 1px solid var(--nf-border, #e8eaed);
  background: #fff;
}
</style>
