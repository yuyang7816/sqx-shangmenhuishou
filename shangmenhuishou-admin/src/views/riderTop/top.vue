<template>
	<div class="rider-top-page">
		<div class="nf-page-hero rider-top-page__hero">
			<span class="nf-page-hero__badge">排行榜管理</span>
			<h2 class="nf-page-hero__title">收益排行榜</h2>
			<p class="nf-page-hero__desc">查看师傅与商家收益排行数据，支持切换榜单类型并刷新最新统计</p>
		</div>

		<div class="nf-filter-card rider-top-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>操作</span>
			</div>
			<div class="nf-filter-card__body rider-top-page__filter-body">
				<div class="rider-top-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-refresh" @click="cleans">刷新</el-button>
				</div>
			</div>
		</div>

		<div class="rider-top-page__tabs-wrap">
			<el-tabs v-model="activeName" class="rider-top-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="师傅收益排行榜" name="first">
					<div class="nf-table-card rider-top-page__table-card">
						<el-table v-loading="tableDataLoading" :data="homeData.records" class="rider-top-page__table">
							<el-table-column fixed prop="userId" label="编号" width="80">
							</el-table-column>
							<el-table-column prop="userName" label="师傅昵称">
								<template slot-scope="scope">
									<span class="rider-top-page__link"
										@click="updatesvideo(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
								</template>
							</el-table-column>
							<el-table-column prop="nickName" label="头像">
								<template slot-scope="scope">
									<div v-if="scope.row.avatar">
										<img :src="scope.row.avatar" width="40" height="40" />
									</div>
									<div v-else>暂无头像</div>
								</template>
							</el-table-column>
							<el-table-column prop="money" label="总收益">
							</el-table-column>
						</el-table>
						<div class="rider-top-page__pagination">
							<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
								:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="homeData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="商家收益排行榜" name="shop">
					<div class="nf-table-card rider-top-page__table-card">
						<el-table v-loading="tableDataLoading" :data="homeData.records" class="rider-top-page__table">
							<el-table-column fixed prop="userId" label="编号" width="80">
							</el-table-column>
							<el-table-column prop="userName" label="商家昵称">
								<template slot-scope="scope">
									<span class="rider-top-page__link"
										@click="updatesvideo(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
								</template>
							</el-table-column>
							<el-table-column prop="nickName" label="头像">
								<template slot-scope="scope">
									<div v-if="scope.row.avatar">
										<img :src="scope.row.avatar" width="40" height="40" />
									</div>
									<div v-else>暂无头像</div>
								</template>
							</el-table-column>
							<el-table-column prop="money" label="总收益">
							</el-table-column>
						</el-table>
						<div class="rider-top-page__pagination">
							<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
								:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="homeData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				state: '',
				limit: 10,
				title: '',
				type: 1,
				activeName: 'first',
				tableDataLoading: true,
				homeData: {},
				flag: 1,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
				var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
				var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			handleSizeChange1(val) {
				this.limit = val
				this.homeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.homeSelect()
			},
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '师傅收益排行榜') {
					this.type = 1
					this.homeSelect()
				}
				if (tab._props.label == '代理排行榜') {
					this.type = 2
					this.homeSelect()
				}
				if (tab._props.label == '商家收益排行榜') {
					this.type = 3
					this.homeSelect()
				}
			},
			// 信息数据
			homeSelect() {

				if (this.type == 1) {
					this.$http({
						url: this.$http.adornUrl('orders/getStaffInfo'),
						method: 'get',
						params: this.$http.adornParams({
							'page': this.page,
							'limit': this.limit,
							// 'type': this.type,
							// 'time': this.info.stockDate,
							// 'flag': this.flag,
						})
					}).then(({
						data
					}) => {
						this.tableDataLoading = false
						let returnData = data.data
						this.homeData = data.data
					})
				}
				if (this.type == 2) {
					this.$http({
						url: this.$http.adornUrl('orders/getAgentProfit'),
						method: 'get',
						params: this.$http.adornParams({
							'page': this.page,
							'limit': this.limit,
							// 'type': this.type,
							// 'time': this.info.stockDate,
							// 'flag': this.flag,
						})
					}).then(({
						data
					}) => {
						this.tableDataLoading = false
						let returnData = data.data
						this.homeData = data.data
					})
				}
				if (this.type == 3) {
					this.$http({
						url: this.$http.adornUrl('orders/getShopInfo'),
						method: 'get',
						params: this.$http.adornParams({
							'page': this.page,
							'limit': this.limit,
							// 'type': this.type,
							// 'time': this.info.stockDate,
							// 'flag': this.flag,
						})
					}).then(({
						data
					}) => {
						this.tableDataLoading = false
						let returnData = data.data
						this.homeData = data.data
					})
				}


			},

			// 详情跳转
			updatesvideo(row) {
				if (row.userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					})
				} else {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '未绑定用户',
						type: 'warning'
					})
					return
				}

			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.homeSelect()
			},
			// 重置
			cleans() {
				this.address = ''
				this.flag = ''
				this.info.stockDate = this.getNowTime()
				this.homeSelect()
			},
			// 订单分析选择日期
			animeOrder() {
				console.log('info', this.info)
				this.homeSelect()
				// this.colonel()
			},
			// 订单分析年月日
			orderfenxi(value) {
				this.page = 1
				let vanumber = value
				this.flag = value
				this.homeSelect()

			},
		},
		mounted() {
			this.homeSelect()
		}
	}
</script>

<style lang="scss" scoped>
.rider-top-page {
	padding: 0;
	overflow-x: hidden;
}

.rider-top-page__hero {
	margin-bottom: 12px;
}

.rider-top-page__filter {
	margin-bottom: 12px;
}

.rider-top-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.rider-top-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.rider-top-page__tabs-wrap {
	margin-bottom: 0;
}

.rider-top-page__tabs {
	margin-bottom: 0;
}

.rider-top-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.rider-top-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.rider-top-page__table-card >>> .el-table {
	border-radius: 0;
}

.rider-top-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.rider-top-page__table >>> .el-table__body td {
	background: #fff;
}

.rider-top-page__link {
	color: #01af74;
	cursor: pointer;
}

.rider-top-page__link:hover {
	color: #017c52;
}

.rider-top-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
