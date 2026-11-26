<template>
	<div class="staff-list-page">
		<div class="nf-page-hero staff-list-page__hero">
			<span class="nf-page-hero__badge">团队管理</span>
			<h2 class="nf-page-hero__title">员工列表</h2>
			<p class="nf-page-hero__desc">查看与管理回收员工信息，支持按时间、电话与昵称筛选，可查看派单记录与指派订单</p>
		</div>

		<div class="nf-filter-card staff-list-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body staff-list-page__filter-body">
				<div class="staff-list-page__field">
					<span class="staff-list-page__label">开始时间：</span>
					<el-date-picker class="staff-list-page__date" v-model="startTime" align="right" type="datetime"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="staff-list-page__field">
					<span class="staff-list-page__label">截止时间：</span>
					<el-date-picker class="staff-list-page__date" v-model="endTime" align="right" type="datetime"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="staff-list-page__field">
					<span class="staff-list-page__label">电话：</span>
					<el-input class="staff-list-page__input" @keydown.enter.native="timeDate2" clearable
						placeholder="请输入电话" v-model="phone">
					</el-input>
				</div>
				<div class="staff-list-page__field">
					<span class="staff-list-page__label">昵称：</span>
					<el-input class="staff-list-page__input" @keydown.enter.native="timeDate2" clearable
						placeholder="请输入昵称" v-model="nickName"></el-input>
				</div>
				<div class="staff-list-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="timeDate2">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh" @click="refresh1">刷新</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card staff-list-page__table-card">
			<el-table v-loading="tableDataLoading" :data="rechargeData.content" class="staff-list-page__table">
				<el-table-column fixed prop="userId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="nickName" label="员工昵称">
					<template slot-scope="scope">
						<span class="staff-list-page__link"
							@click="updates(scope.row)">{{scope.row.nickName ? scope.row.nickName : 'null'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="电话">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersCount" label="订单数量">
					<template slot-scope="scope">
						<span>{{scope.row.ordersCount ? scope.row.ordersCount : '0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersPayMoney" label="订单金额">
					<template slot-scope="scope">
						<span>{{scope.row.ordersPayMoney ? scope.row.ordersPayMoney : '0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersPayMoney" label="操作" width="200">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" icon="el-icon-view" @click="paidanBtn(scope.row)">查看派单</el-button>
						<el-button size="mini" type="primary" icon="el-icon-s-order" @click="zhipaiBtn(scope.row)">指派订单</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="staff-list-page__pagination">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 15 , 20]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum2">
				</el-pagination>
			</div>
		</div>

		<!-- 派单列表 -->
		<el-dialog title="派单列表" :visible.sync="dialogFormVisibleP" center width="80%">
			<div style="margin:5px;display: inline-block;">
				<span>订单状态：</span>
				<el-select v-model="status" placeholder="请选择订单状态" style="width:150px;" @change="select()">
					<el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>

			<div style="margin:5px;display: inline-block;">
				<span>订单号：</span>
				<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入订单号"
					v-model="orderNum"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<!-- <div style="margin:5px;display: inline-block;">
				<span>门店名称：</span>
				<el-input style="width: 180px;" @keydown.enter.native="select" clearable placeholder="请输入门店名称"
					v-model="laundryName"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div> -->
			<div style="margin:5px;display: inline-block;">
				<span>收货人手机号：</span>
				<el-input style="width: 180px;" @keydown.enter.native="select" clearable placeholder="请输入收货人手机号"
					v-model="phone">
				</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>

			<div style="margin:5px;display: inline-block;">
				<span>下单开始时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime1" align="right"
					type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin:5px;display: inline-block;">
				<span>下单截止时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime1" align="right" type="datetime"
					format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
				</el-date-picker>
			</div>
			<div style="margin:5px;display: inline-block;">
				<span>预约开始时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTimeYy" align="right"
					type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin:5px;display: inline-block;">
				<span>预约截止时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTimeYy" align="right"
					type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
				</el-date-picker>
			</div>
			<div style="display: inline-block;">
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">刷新
				</el-button>
			</div>
			<el-table v-loading="tableDataLoadingP" :data="tableData.content">
				<el-table-column prop="id" label="编号" width="60" fixed>
				</el-table-column>
				<el-table-column prop="img" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.img" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" fixed label="商品标题" width="100">
				</el-table-column>
				<el-table-column prop="oneGoodsType.name" label="一级商品分类">
				</el-table-column>
				<el-table-column prop="twoGoodsType.name" label="二级商品分类">
				</el-table-column>
				<el-table-column prop="detailJson" label="商品规格">
					<template slot-scope="scope">
						<span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="number" label="商品个数">
				</el-table-column>
				<el-table-column prop="descrition" label="订单备注" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderNum" label="订单号" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.orderNum}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="团长用户" fixed="left">
					<template slot-scope="scope">
						<span style="color: #01af74;cursor:pointer;"
							@click="updates(scope.row.teamId)">{{scope.row.teamUserName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="consignee" label="收货人" width="120">
				</el-table-column>
				<el-table-column prop="mobile" label="收货人手机号" width="120">
				</el-table-column>
				<el-table-column prop="detail" label="详细地址" width="120">
					<template slot-scope="scope">
						<span>{{scope.row.provinces}}{{scope.row.detail}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="startImg" label="服务前图片" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.startImg == null || scope.row.startImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.startImg" style="display:flex;flex-wrap: wrap;">
							<div v-for="(item,index) in scope.row.startImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin-left: 5px;" :src="item" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="endImg" label="服务后图片" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.endImg == null || scope.row.endImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.endImg" style="display:flex;flex-wrap: wrap;">
							<div v-for="(item,index) in scope.row.endImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin-left: 5px;" :src="item" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="item" alt="">
								</el-popover>
							</div>

						</div>
					</template>
				</el-table-column>
				<el-table-column prop="code" label="确认码" width="80">
				</el-table-column>
				<el-table-column prop="refund" label="退款原因" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.refund ? scope.row.refund : '未退款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="refusedRefund" label="驳回原因" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.refusedRefund ? scope.row.refusedRefund : '未拒绝'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="payWay" label="支付方式" width="120">
					<template slot-scope="scope">
						<span style="color: #01af74;" v-if="scope.row.payWay === 1 ">微信</span>
						<span style="color: #01af74;" v-if="scope.row.payWay === 2 ">微信公众号</span>
						<span style="color: #01af74;" v-if="scope.row.payWay === 3 ">微信小程序</span>
						<span style="color: #01af74;" v-if="scope.row.payWay === 4 ">app支付宝</span>
						<span style="color: #01af74;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
						<span style="color: #01af74;" v-if="scope.row.payWay === 6 ">零钱</span>
						<span style="color: #01af74;" v-if="scope.row.payWay === 7 ">积分</span>
						<span style="color: #01af74;" v-if="scope.row.payWay === null ">未支付</span>
					</template>
				</el-table-column>
				<el-table-column prop="serviceTime" label="预约时间" fixed="right" width="80">
					<template slot-scope="scope">
						<span style="color: red;font-weight: 600;">{{scope.row.serviceTime}}</span>

					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>

				<el-table-column prop="payTime" label="付款时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="startTime" label="服务开始时间" width="160">
				</el-table-column>
				<el-table-column prop="endTime" label="服务结束时间" width="160">
				</el-table-column>
				<el-table-column prop="finishTime" label="收货时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.finishTime ? scope.row.finishTime : '未收货'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="isOver" label="是否超时" width="50">
					<template slot-scope="scope">
						<span v-if="scope.row.isOver==1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="staffUserId" label="是否派单" width="50" fixed="right">
					<template slot-scope="scope">
						<span>{{scope.row.staffUserId ? '是' : '否'}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="status" label="订单状态" width="80">
					<template slot-scope="scope">
						<span style="color: #01af74;" v-if="scope.row.status == 1">待付款</span>
						<span style="color: #01af74;" v-if="scope.row.status == 2">待服务</span>
						<span style="color: #01af74;" v-if="scope.row.status == 3">服务中</span>
						<!-- <span style="color: #01af74;" v-if="scope.row.status == 4">待送达</span>
						<span style="color: #01af74;" v-if="scope.row.status == 5">已送达</span>
						<span style="color: #01af74;" v-if="scope.row.status == 6">待取衣</span> -->
						<span style="color: #01af74;" v-if="scope.row.status == 7">已完结</span>
						<span style="color: #01af74;" v-if="scope.row.status == 8">申请退款</span>
						<span style="color: #01af74;" v-if="scope.row.status == 9">已退款</span>
						<span style="color: #01af74;" v-if="scope.row.status == 10">拒绝退款</span>
						<span style="color: #01af74;" v-if="scope.row.status == 11">已评价</span>
						<span style="color: #999;" v-if="scope.row.status == 12">已取消</span>

					</template>
				</el-table-column>

			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20,30,50,100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				size: 10,
				page1: 1,
				size1: 10,
				startTime: '',
				endTime: '',
				totalnum2: 0,
				tableDataLoading: false,
				rechargeData: {},

				item: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				nickName: '',
				phone: '',
				dialogFormVisibleP: false,
				tableDataLoadingP: false,
				tableData: {},
				orderNum: '',
				title: '',
				mobile: '',
				isExpress: '',
				status: 0,
				orderTypes: [{
						value: 0,
						label: '全部'
					},

					{
						value: 2,
						label: '待服务'
					},
					{
						value: 3,
						label: '服务中'
					},
					{
						value: 7,
						label: '已完结'
					}
				],
				staffUserId: '',
				laundryName: '',
				phoneP: '',
				startTimeYy: '',
				endTimeYy: '',
				startTime1: '',
				endTime1: '',
			}
		},
		methods: {
			// 会员详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.teamId
					}
				})
			},

			timeDate2() {
				this.rechargeSelect()
			},
			// 日期选择
			animeDat(value) {
				let vanumber = value
				if (vanumber === 3) {
					this.withdrawSelect()
				}
				if (vanumber === 2) {
					this.item = this.info.stockDate
					this.info.stockDate = this.info.stockDate.substring(0, 7)
					this.withdrawSelect()
					this.info.stockDate = this.item
				}
				if (vanumber === 1) {
					this.item = this.info.stockDate
					this.info.stockDate = this.info.stockDate.substring(0, 4)
					this.withdrawSelect()
					this.info.stockDate = this.item
				}
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},

			handleSizeChange1(val) {
				this.size = val
				this.rechargeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleSizeChange(val) {
				this.size1 = val
				this.dataSelectP()
			},
			handleCurrentChange(val) {
				this.page1 = val
				this.dataSelectP()
			},
			// 刷新
			refresh1() {
				this.rechargeSelect()
			},
			// 获取充值记录
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectStaffUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'nickName': this.nickName,
						'phone': this.phone

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.rechargeData = returnData
					this.totalnum2 = returnData.totalElements
				})
			},
			// 查看派单弹框
			paidanBtn(row) {
				if (row.staffUserId) {
					this.staffUserId = row.staffUserId
					this.startTimeYy = this.info.stockDate
					this.endTimeYy = this.info.stockDate
					this.dataSelectP()
				}
				this.dialogFormVisibleP = true
			},
			// 指派订单弹框
			zhipaiBtn(row) {
				this.$router.push({
					path: '/order',
					query: {
						from: 'staffList'
					}
				})
			},
			// 获取订单数据
			dataSelectP() {
				if (this.endTime1 == '') {
					this.endTime1 = this.info.stockDate
				}
				if (this.startTime1 == '') {
					this.startTime1 = this.info2.stockDate2
				}
				let page = this.page1 - 1
				this.tableDataLoadingP = true
				this.$http({
					url: this.$http.adornUrl('orders/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size1,
						'orderNum': this.orderNum,
						'status': this.status,
						'laundryName': this.laundryName,
						'phone': this.phoneP,
						'startTime': this.startTime1,
						'endTime': this.endTime1,
						'staffUserId': this.staffUserId,
						'startServiceTime': this.startTimeYy,
						'endServiceTime': this.endTimeYy,
						'staffName': '',
						'oneTypeId': '',
						'twoTypeId': '',

					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingP = false
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			//搜索
			select() {
				this.page1 = 1
				this.size1 = 10
				this.dataSelectP()
			},
			cleans() {
				this.page1 = 1
				this.orderNum = ''
				this.status = 0
				this.startTime1 = ''
				this.endTime1 = ''
				this.laundryName = ''
				this.phone = ''
				this.startTimeYy = ''
				this.endTimeYy = ''
				this.dataSelectP()
			},
		},
		mounted() {
			this.rechargeSelect()
		}
	}
</script>

<style lang="scss" scoped>
.staff-list-page {
	padding: 0;
	overflow-x: hidden;
}

.staff-list-page__hero {
	margin-bottom: 12px;
}

.staff-list-page__filter {
	margin-bottom: 12px;
}

.staff-list-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.staff-list-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.staff-list-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.staff-list-page__input {
	width: 150px;
}

.staff-list-page__date {
	width: 160px;
}

.staff-list-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.staff-list-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.staff-list-page__table-card >>> .el-table {
	border-radius: 0;
}

.staff-list-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.staff-list-page__table >>> .el-table__body td {
	background: #fff;
}

.staff-list-page__link {
	color: #01af74;
	cursor: pointer;
}

.staff-list-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
