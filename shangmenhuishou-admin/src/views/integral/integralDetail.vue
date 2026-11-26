<template>
	<div class="integral-detail-page">
		<div class="integral-detail-page__nav">
			<a href="#" class="integral-detail-page__back-link" @click.prevent="prev">
				<icon-svg name="jiantou" class="integral-detail-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="integral-detail-page__nav-sep">|</span>
			<span class="integral-detail-page__nav-title">活动详情</span>
		</div>

		<div class="nf-page-hero integral-detail-page__hero">
			<span class="nf-page-hero__badge">积分管理</span>
			<h2 class="nf-page-hero__title">活动详情</h2>
			<p class="nf-page-hero__desc">查看积分推送活动的参与用户明细，含昵称、活动名称、描述、金额及创建时间</p>
		</div>

		<div class="nf-filter-card integral-detail-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body integral-detail-page__filter-body">
				<el-button size="mini" type="primary" icon="el-icon-refresh" @click="dataSelect">刷新数据</el-button>
			</div>
		</div>

		<div class="nf-table-card integral-detail-page__table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list" class="integral-detail-page__table">
				<el-table-column prop="nickName" label="昵称" width="150">
					<template slot-scope="scope">
						<span class="integral-detail-page__link">{{ scope.row.nickName ? scope.row.nickName : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="integralName" label="活动名称" width="150">
				</el-table-column>
				<el-table-column prop="describes" label="活动描述">
				</el-table-column>
				<el-table-column prop="money" label="活动金额">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div class="integral-detail-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 15, 20, 25]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
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
				tableDataLoading: true,
				tableData: []
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				let sendMoneyId = this.$route.query.sendMoneyId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('integral/selectSendMoneyDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'sendMoneyId': sendMoneyId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style lang="scss" scoped>
.integral-detail-page {
	padding: 0;
	overflow-x: hidden;
}

.integral-detail-page__nav {
	display: flex;
	align-items: center;
	margin-bottom: 12px;
	font-size: 14px;
	color: var(--nf-text-primary, #1a2433);
}

.integral-detail-page__back-link {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	color: var(--nf-brand, #01af74);
	text-decoration: none;
	cursor: pointer;
}

.integral-detail-page__back-link:hover {
	color: var(--nf-brand-deep, #017c52);
}

.integral-detail-page__back-icon {
	width: 1.2em;
	height: 1.2em;
}

.integral-detail-page__nav-sep {
	margin: 0 12px;
	color: var(--nf-border, #e8eaed);
}

.integral-detail-page__nav-title {
	font-size: 16px;
	font-weight: 600;
}

.integral-detail-page__hero {
	margin-bottom: 12px;
}

.integral-detail-page__filter {
	margin-bottom: 12px;
}

.integral-detail-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.integral-detail-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.integral-detail-page__table-card >>> .el-table {
	border-radius: 0;
}

.integral-detail-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.integral-detail-page__table >>> .el-table__body td {
	background: #fff;
}

.integral-detail-page__link {
	color: var(--nf-brand, #01af74);
	cursor: pointer;
}

.integral-detail-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
