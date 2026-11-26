<template>
	<div>
		<div style="display: inline-block;">
			<span>开始时间：</span>
			<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right" type="datetime"
				format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
			</el-date-picker>&nbsp;&nbsp;&nbsp;
			<span>截止时间：</span>
			<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="datetime"
				format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
			</el-date-picker>
			<el-input style="width: 150px;" @keydown.enter.native="timeDate2" clearable placeholder="请输入手机号"
				v-model="phone">
			</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			<el-input style="width: 150px;" @keydown.enter.native="timeDate2" clearable placeholder="微信昵称筛选"
				v-model="nickName"></el-input>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="timeDate2">查询
			</el-button>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh1">刷新
			</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="rechargeData.content">
			<el-table-column fixed prop="teamId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="nickName" label="团长名称">
				<template slot-scope="scope">
					<span style="color: #01af74;cursor: pointer;"
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
			<el-table-column prop="ordersPayMoney" label="团队收益">
				<template slot-scope="scope">
					<span>{{scope.row.ordersPayMoney ? scope.row.ordersPayMoney : '0'}}</span>
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
				:page-sizes="[10, 15 , 20]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next" :total="totalnum2">
			</el-pagination>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				size: 10,
				startTime: '',
				endTime: '',
				totalnum2: 0,
				tableDataLoading: false,
				rechargeData: [],

				item: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				nickName: '',
				phone: '',
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
					url: this.$http.adornUrl('orders/selectTeamUserList'),
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
			}
		},
		mounted() {
			this.rechargeSelect()
		}
	}
</script>

<style scoped="scoped">
	.box {
		height: 110px;
		padding: 24px;
		border: 1px solid #eee;
		margin: 15px 10px;
	}

	.box_num {
		font-size: 14px;
		color: #66b1ff;
	}

	.box_num .box_color {
		color: #333;
		font-size: 14px;
		margin-bottom: 15px;
	}

	.box_num div span {
		font-size: 20px;
		margin-left: 5px;
	}

	.text_color {
		color: #01af74;
	}

	.text_color span {
		margin-right: 5px;
	}
</style>
