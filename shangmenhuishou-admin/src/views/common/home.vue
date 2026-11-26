<template>
	<div class="home-page">
		<!-- hero + 筛选 -->
		<div class="home-page__top">
			<div class="nf-page-hero home-page__hero">
				<span class="nf-page-hero__badge">数据中心</span>
				<h2 class="nf-page-hero__title">数据概览</h2>
				<p class="nf-page-hero__desc">实时查看用户、订单与交易核心指标</p>
			</div>
			<div class="nf-filter-card home-page__filter">
				<div class="nf-filter-card__head">
					<i class="el-icon-search"></i>
					<span>筛选条件</span>
				</div>
				<div class="nf-filter-card__body home-page__filter-body">
					<el-select v-model="flag" class="home-page__filter-select" @change="orderfenxi">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
					<el-date-picker
						class="home-page__filter-date"
						v-model="info.stockDate"
						align="right"
						type="date"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间"
						@change="animeOrder">
					</el-date-picker>
				</div>
			</div>
		</div>

		<!-- KPI 四列 -->
		<div class="home-page__kpi-row">
			<div class="home-kpi">
				<div class="home-kpi__icon"><i class="el-icon-user-solid"></i></div>
				<div class="home-kpi__body">
					<div class="home-kpi__label">总用户数</div>
					<div class="home-kpi__value"><span>{{ tableData.userCount ? tableData.userCount : 0 }}</span>人</div>
				</div>
			</div>
			<div class="home-kpi">
				<div class="home-kpi__icon"><i class="el-icon-s-order"></i></div>
				<div class="home-kpi__body">
					<div class="home-kpi__label">总订单数</div>
					<div class="home-kpi__value"><span>{{ tableData.orderCount ? tableData.orderCount : 0 }}</span>单</div>
				</div>
			</div>
			<div class="home-kpi">
				<div class="home-kpi__icon"><i class="el-icon-money"></i></div>
				<div class="home-kpi__body">
					<div class="home-kpi__label">总预估交易金额</div>
					<div class="home-kpi__value"><span>{{ tableData.amount ? tableData.amount : 0 }}</span>元</div>
				</div>
			</div>
			<div class="home-kpi">
				<div class="home-kpi__icon"><i class="el-icon-user"></i></div>
				<div class="home-kpi__body">
					<div class="home-kpi__label">今日新增</div>
					<div class="home-kpi__value"><span>{{ tableData.todayUserCount ? tableData.todayUserCount : 0 }}</span>人</div>
				</div>
			</div>
		</div>

		<!-- 双列明细 -->
		<div class="home-page__detail-grid">
			<div class="home-detail-col">
				<div class="home-detail-col__head">用户增长</div>
				<div class="home-stat-tiles">
					<div class="home-stat-tile">
						<div class="home-stat-tile__label">今日新增</div>
						<div class="home-stat-tile__value"><span>{{ tableData.todayUserCount ? tableData.todayUserCount : 0 }}</span>人</div>
					</div>
					<div class="home-stat-tile">
						<div class="home-stat-tile__label">本月新增</div>
						<div class="home-stat-tile__value"><span>{{ tableData.monthUserCount ? tableData.monthUserCount : 0 }}</span>人</div>
					</div>
					<div class="home-stat-tile">
						<div class="home-stat-tile__label">本年新增</div>
						<div class="home-stat-tile__value"><span>{{ tableData.yearUserCount ? tableData.yearUserCount : 0 }}</span>人</div>
					</div>
				</div>
			</div>
			<div class="home-detail-col">
				<div class="home-detail-col__head">订单与交易</div>
				<div class="home-stat-tiles">
					<div class="home-stat-tile">
						<div class="home-stat-tile__label">今日订单数</div>
						<div class="home-stat-tile__value"><span>{{ tableData.dayOrderCount ? tableData.dayOrderCount : 0 }}</span>单</div>
					</div>
					<div class="home-stat-tile">
						<div class="home-stat-tile__label">本月订单数</div>
						<div class="home-stat-tile__value"><span>{{ tableData.monthOrderCount ? tableData.monthOrderCount : 0 }}</span>单</div>
					</div>
					<div class="home-stat-tile">
						<div class="home-stat-tile__label">本年订单数</div>
						<div class="home-stat-tile__value"><span>{{ tableData.yearOrderCount ? tableData.yearOrderCount : 0 }}</span>单</div>
					</div>
					<div class="home-stat-tile">
						<div class="home-stat-tile__label">今日预估交易金额</div>
						<div class="home-stat-tile__value"><span>{{ tableData.dayAmount ? tableData.dayAmount : 0 }}</span>元</div>
					</div>
					<div class="home-stat-tile">
						<div class="home-stat-tile__label">本月预估交易金额</div>
						<div class="home-stat-tile__value"><span>{{ tableData.monthAmount ? tableData.monthAmount : 0 }}</span>元</div>
					</div>
					<div class="home-stat-tile">
						<div class="home-stat-tile__label">本年预估交易金额</div>
						<div class="home-stat-tile__value"><span>{{ tableData.yearAmount ? tableData.yearAmount : 0 }}</span>元</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 分析 tabs -->
		<div class="home-page__analysis">
			<el-tabs v-model="activeName" class="home-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="用户分析" name="user">
					<div class="nf-table-card home-page__tab-card">
						<div class="home-stat-tiles home-stat-tiles--inline">
							<div class="home-stat-tile">
								<div class="home-stat-tile__label">总用户人数</div>
								<div class="home-stat-tile__value"><span>{{ taskStatUser.userCount ? taskStatUser.userCount : 0 }}</span>人</div>
							</div>
							<div class="home-stat-tile">
								<div class="home-stat-tile__label">普通用户人数</div>
								<div class="home-stat-tile__value"><span>{{ taskStatUser.normalUserCount ? taskStatUser.normalUserCount : 0 }}</span>人</div>
							</div>
							<div class="home-stat-tile">
								<div class="home-stat-tile__label">回收员用户数</div>
								<div class="home-stat-tile__value"><span>{{ taskStatUser.wasteUserCount ? taskStatUser.wasteUserCount : 0 }}</span>人</div>
							</div>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="订单分析" name="dingdan">
					<div class="nf-table-card home-page__tab-card">
						<div class="home-stat-tiles home-stat-tiles--inline">
							<div class="home-stat-tile">
								<div class="home-stat-tile__label">总订单数</div>
								<div class="home-stat-tile__value"><span>{{ taskStat.totalCount ? taskStat.totalCount : 0 }}</span>单</div>
							</div>
							<div
								class="home-stat-tile"
								v-if="taskStat.orderStatisticsList && taskStat.orderStatisticsList.length > 0"
								v-for="(item, index) in taskStat.orderStatisticsList"
								:key="index">
								<div class="home-stat-tile__label">{{ item.desc }}</div>
								<div class="home-stat-tile__value"><span>{{ item.count ? item.count : 0 }}</span>单</div>
							</div>
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
				limit: 5,
				page: 1,
				tableData: {},
				tableData2: {
					list: [],
					totalCount: 0
				},
				colonelData: [],
				commodityData: [],
				userjfData: [],
				incomeData: [],
				activeName: 'user',
				tableDataLoading: true,
				flag: 1,
				usersumData: {},
				taskStatF: {},
				taskStat: {},
				data: '',
				time: '',
				platform: '',
				type: '',
				balance: -1,
				flag: 'day',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 'day',
					label: '按天查询'
				}, {
					value: 'month',
					label: '按月查询'
				}, {
					value: 'year',
					label: '按年查询'
				}],
				taskStatTj:{},
				taskStatZf:{},
				taskStatUser:{},
				taskStatYj:{},
				chatNumTs: 0,
				chatNumQy: 0,
				chatNumSf: 0,
				chatNumPj: 0,
				chatNumFw: 0,
			}
		},
		mounted() {
			// this.colonel()
			this.taskDataUser()
			this._getData() //用户统计
			// this.taskDataD()
			// this.taskDataZf()
			// this.incomeSelect()//收入
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			updates1(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId1
					}
				})
			},
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId2
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val;
				this.colonel();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.colonel();
			},
			// 用户分析选择日期
			animeDat() {
				this._getData()
			},
			// 订单分析选择日期
			animeOrder() {
				// this.taskData()
				// this.colonel()
				this.taskDataUser()
				this.taskDataD()
				// this.taskDataZf()
				// this.taskDataYj()
			},
			// 订单分析年月日
			orderfenxi(value) {
				let vanumber = value
				// if (vanumber === 1) {
				// 	this.flag = 1
					this.taskDataUser()
					this.taskDataD()
				// }
				// if (vanumber === 2) {
				// 	this.flag = 2
				// 	this.taskData()
				// 	this.colonel()
				// 	this.taskDataD()
				// 	this.taskDataZf()
				// 	this.taskDataUser()
				// 	this.taskDataYj()
				// }
				// if (vanumber === 3) {
				// 	this.flag = 3
				// 	this.taskData()
				// 	this.colonel()
				// 	this.taskDataD()
				// 	this.taskDataZf()
				// 	this.taskDataUser()
				// 	this.taskDataYj()
				// }
			},
			// Tabs点击事件
			handleClick(tab, event) {
				if (tab._props.label == '用户分析') {
					this.taskDataUser()
				}
				if (tab._props.label == '订单分析') {
					this.taskDataD()
				}
				if (tab._props.label == '支付统计(总)') {
					this.taskDataZf()
				}
				
				if (tab._props.label == '平台佣金统计') {
					this.taskDataYj()
				}
				if (tab._props.label == '待处理事项') {
					this.chatNumTs = this.$cookie.get('numberTs')
					this.chatNumQy = this.$cookie.get('numberDrz')
					this.chatNumSf = this.$cookie.get('numberDts')
					this.chatNumPj = this.$cookie.get('numberDsh')
					this.chatNumFw = this.$cookie.get('numberDfw')
					// this.dataSelect()
					// this.dataSelectPy()
					// this.dataSelectJjqz()
					// this.dataSelectYh()
				}
				
				
				
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 用户统计
			_getData() {
				this.$http({
					url: this.$http.adornUrl('admin/index/indexStatistics'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data;
						this.tableData = returnData
					}

				})
			},
			// 收入统计
			//  incomeSelect(){
			//  this.$http({
			// url: this.$http.adornUrl('statistical/income'),
			// method: 'get',
			// params: this.$http.adornParams({
			// })
			//  }).then(({data}) => {
			//   if(data.code == 0){
			// 	  let returnData = data.data;
			// 	  this.incomeData = returnData
			//   }
			//  })
			//  },
			//服务分析
			taskData() {
				this.$http({
					url: this.$http.adornUrl('orderTaking/taskAnalysis'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.taskStatF = returnData
				})
			},
			// 服务收入分析
			colonel() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orderTaking/incomeAnalysis'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						let returnData = data.data
						this.tableData2 = returnData 
					}
				})
			},
			// 订单分析
			taskDataD() {
				this.$http({
					url: this.$http.adornUrl('admin/index/orderStatistics'),
					method: 'post',
					data: this.$http.adornData({
						'findDate': this.info.stockDate,
						'findType': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.taskStat = returnData
				})
			},
			// 订单总统计
			taskDataTj(){
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersToTal'),
					method: 'get',
					params: this.$http.adornParams({
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.taskStatTj = returnData
				})
			},
			// 支付统计 
			taskDataZf() {
				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersPayStatistics'),
					method: 'get',
					data: this.$http.adornData({
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.taskStatZf = returnData
				})
			},
			//用户分析
			taskDataUser() {
				this.$http({
					url: this.$http.adornUrl('admin/index/userStatistics'),
					method: 'post',
					data: this.$http.adornData({
						'findDate': this.info.stockDate,
						'findType': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.taskStatUser = returnData
				})
			},
			// 平台佣金统计
			taskDataYj(){
				this.$http({
					url: this.$http.adornUrl('cash/commissionCensus'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.taskStatYj = returnData
				})
			},
			updatesD1() {
				this.$router.push({
					path: '/message',
					query: {
					}
				})
			},
			updatesD2() {
				this.$router.push({
					path: '/autonymPeople',
					query: {
						status: 1
					}
				})
			},
			updatesD3() {
				this.$router.push({
					path: '/autonym',
					query: {
						status: 1
					}
				})
			},
			updatesD4() {
				this.$router.push({
					path: '/comment',
					query: {
						status: 1
					}
				})
			},
			updatesD5(){
				this.$router.push({
					path: '/locality',
					query: {
						status: 1
					}
				})
			},
		}
	}
</script>

<style scoped>
.home-page {
	padding: 0;
	overflow-x: hidden;
}

/* hero + 筛选并排 */
.home-page__top {
	display: flex;
	gap: 12px;
	margin-bottom: 12px;
	align-items: stretch;
}
.home-page__hero {
	flex: 1;
	margin-bottom: 0;
}
.home-page__filter {
	flex: 0 0 320px;
	margin-bottom: 0;
	display: flex;
	flex-direction: column;
	justify-content: center;
}
.home-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}
.home-page__filter-select {
	width: 140px;
}
.home-page__filter-date {
	width: 160px;
}

/* KPI 四列 */
.home-page__kpi-row {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 12px;
	margin-bottom: 12px;
}
.home-kpi {
	display: flex;
	align-items: center;
	gap: 12px;
	padding: 16px;
	background: var(--nf-card-bg, #fff);
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-md, 8px);
	box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
}
.home-kpi__icon {
	flex-shrink: 0;
	width: 44px;
	height: 44px;
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: var(--nf-radius-sm, 6px);
	background: var(--nf-brand-lite, #e1f5ee);
	color: var(--nf-brand, #01af74);
	font-size: 20px;
}
.home-kpi__body {
	min-width: 0;
}
.home-kpi__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
	margin-bottom: 4px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
.home-kpi__value {
	font-size: 13px;
	color: var(--nf-text-primary, #1a2433);
}
.home-kpi__value span {
	font-size: 22px;
	font-weight: 600;
	color: var(--nf-brand, #01af74);
	margin-right: 4px;
}

/* 双列明细 */
.home-page__detail-grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 12px;
	margin-bottom: 12px;
}
.home-detail-col {
	background: var(--nf-card-bg, #fff);
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-md, 8px);
	box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
	padding: 16px;
}
.home-detail-col__head {
	font-size: 14px;
	font-weight: 600;
	color: var(--nf-text-primary, #1a2433);
	margin-bottom: 12px;
	padding-bottom: 8px;
	border-bottom: 1px solid var(--nf-border, #e8eaed);
}
.home-stat-tiles {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 12px;
}
.home-stat-tile {
	padding: 12px;
	background: var(--nf-page-bg, #f4f6fa);
	border-radius: var(--nf-radius-sm, 6px);
	border: 1px solid var(--nf-border, #e8eaed);
}
.home-stat-tile__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
	margin-bottom: 6px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
.home-stat-tile__value {
	font-size: 13px;
	color: var(--nf-text-primary, #1a2433);
}
.home-stat-tile__value span {
	font-size: 18px;
	font-weight: 600;
	color: var(--nf-brand, #01af74);
	margin-right: 4px;
}

/* tabs 零外距 + 表格白卡 */
.home-page__analysis {
	margin-bottom: 0;
}
.home-page__tabs {
	margin-bottom: 0;
}
.home-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}
.home-page__tab-card {
	margin-bottom: 0;
	margin-top: 0;
}
.home-stat-tiles--inline {
	grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
}

/* 响应式 */
@media (max-width: 1200px) {
	.home-page__kpi-row {
		grid-template-columns: repeat(2, 1fr);
	}
	.home-page__detail-grid {
		grid-template-columns: 1fr;
	}
}
@media (max-width: 860px) {
	.home-page__top {
		flex-direction: column;
	}
	.home-page__filter {
		flex: 1 1 auto;
	}
	.home-page__kpi-row {
		grid-template-columns: 1fr;
	}
	.home-stat-tiles {
		grid-template-columns: repeat(2, 1fr);
	}
}
</style>
