<template>
	<div class="coupon-yhq-list-page">
		<div class="nf-page-hero coupon-yhq-list-page__hero">
			<span class="nf-page-hero__badge">营销管理</span>
			<h2 class="nf-page-hero__title">优惠券领取列表</h2>
			<p class="nf-page-hero__desc">查看用户领取优惠券记录，支持按状态、手机号、用户名及优惠券名称筛选查询</p>
		</div>

		<div class="nf-filter-card coupon-yhq-list-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body coupon-yhq-list-page__filter-body">
				<div class="coupon-yhq-list-page__field">
					<span class="coupon-yhq-list-page__label">状态：</span>
					<el-select v-model="state" class="coupon-yhq-list-page__select" @change="animeDat(state)">
						<el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="coupon-yhq-list-page__field">
					<span class="coupon-yhq-list-page__label">手机号：</span>
					<el-input class="coupon-yhq-list-page__input" @keydown.enter.native="animeDat" placeholder="请输入手机号"
						v-model="phone">
					</el-input>
				</div>
				<div class="coupon-yhq-list-page__field">
					<span class="coupon-yhq-list-page__label">用户名：</span>
					<el-input class="coupon-yhq-list-page__input" @keydown.enter.native="animeDat" placeholder="请输入用户名"
						v-model="userName">
					</el-input>
				</div>
				<div class="coupon-yhq-list-page__field">
					<span class="coupon-yhq-list-page__label">优惠券名称：</span>
					<el-input class="coupon-yhq-list-page__input" @keydown.enter.native="animeDat" placeholder="请输入优惠券名称"
						v-model="couponName">
					</el-input>
				</div>
				<div class="coupon-yhq-list-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="animeDat">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<div class="coupon-yhq-list-page__tabs-wrap">
			<el-tabs v-model="activeName" class="coupon-yhq-list-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="优惠券列表" name="seventh">
					<div class="nf-table-card coupon-yhq-list-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records" class="coupon-yhq-list-page__table">
							<el-table-column fixed prop="id" label="编号" width="80">
							</el-table-column>
							<el-table-column fixed prop="nickName" label="昵称" width="150">
								<template slot-scope="scope">
									<span class="coupon-yhq-list-page__link"
										@click="updataDetails(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
								</template>
							</el-table-column>
							<el-table-column label="图像">
								<template slot-scope="scope">
									<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
									<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
								</template>
							</el-table-column>
							<el-table-column prop="phone" label="手机号">
							</el-table-column>
							<el-table-column prop="couponName" label="优惠券名称">
							</el-table-column>
							<el-table-column prop="couponPicture" label="图片">
								<template slot-scope="scope">
									<img :src="scope.row.couponPicture" alt="" width="40" height="40">
								</template>
							</el-table-column>
							<el-table-column prop="money" label="优惠券金额">
							</el-table-column>
							<el-table-column prop="minMoney" label="最低消费">
							</el-table-column>
							<!-- <el-table-column prop="needIntegral" label="兑换积分">
        </el-table-column> -->
							<el-table-column prop="createTime" label="领取时间">
							</el-table-column>
							<el-table-column prop="expirationTime" label="过期时间">
							</el-table-column>
							<el-table-column prop="expirationTime" label="状态">
								<template slot-scope="scope">
									<span v-if="scope.row.status==0">待使用</span>
									<span v-else-if="scope.row.status==1">已使用</span>
									<span v-else-if="scope.row.status==2">已失效</span>
								</template>
							</el-table-column>
						</el-table>
						<div class="coupon-yhq-list-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30,50,100]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next" :total="tableData.total">
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
				limit: 10,
				page: 1,
				state: '',
				url: '',
				title: '',
				type: '',
				id: '',
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
				],
				phone: '',
				content: '',
				activeName: 'seventh',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				tableData: [],
				typeList: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待使用'
					},
					{
						value: 1,
						label: '已使用'
					},
					{
						value: 2,
						label: '已失效'
					}
				],
				phone: '',
				couponName:'',
				userName:'',
			}
		},
		methods: {
			// 详情跳转
			updataDetails(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				});
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleClick(tab, event) {

				if (tab._props.label == '优惠券列表') {
					this.page = 1
					this.limit = 10
					this.state = 8
					this.dataSelect()
				}
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/couponUser/getMyCouponList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': '',
						'status': this.state,
						'phone': this.phone,
						'couponName':this.couponName,
						'userName':this.userName
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			animeDat() {
				this.page = 1
				this.dataSelect()
			},
			cleans() {
				this.page = 1
				this.phone = ''
				this.state = ''
				this.couponName = ''
				this.userName = ''
				this.dataSelect()
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style lang="scss" scoped>
.coupon-yhq-list-page {
	padding: 0;
	overflow-x: hidden;
}

.coupon-yhq-list-page__hero {
	margin-bottom: 12px;
}

.coupon-yhq-list-page__filter {
	margin-bottom: 12px;
}

.coupon-yhq-list-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.coupon-yhq-list-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.coupon-yhq-list-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.coupon-yhq-list-page__input {
	width: 200px;
}

.coupon-yhq-list-page__select {
	width: 150px;
}

.coupon-yhq-list-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.coupon-yhq-list-page__tabs-wrap {
	margin-bottom: 0;
}

.coupon-yhq-list-page__tabs {
	margin-bottom: 0;
}

.coupon-yhq-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.coupon-yhq-list-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.coupon-yhq-list-page__table-card >>> .el-table {
	border-radius: 0;
}

.coupon-yhq-list-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.coupon-yhq-list-page__table >>> .el-table__body td {
	background: #fff;
}

.coupon-yhq-list-page__link {
	color: #01af74;
	cursor: pointer;
}

.coupon-yhq-list-page__link:hover {
	color: #017c52;
}

.coupon-yhq-list-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
