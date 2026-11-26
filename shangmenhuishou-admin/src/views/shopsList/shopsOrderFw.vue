<template>
	<div class="shops-order-fw-page">
		<div class="shops-order-fw-page__nav">
			<a href="#" class="shops-order-fw-page__back" @click.prevent="prev">
				<icon-svg name="jiantou" class="shops-order-fw-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="shops-order-fw-page__nav-sep">|</span>
			<span class="shops-order-fw-page__nav-title">服务订单列表</span>
		</div>

		<div class="nf-page-hero shops-order-fw-page__hero">
			<span class="nf-page-hero__badge">订单管理</span>
			<h2 class="nf-page-hero__title">服务订单列表</h2>
			<p class="nf-page-hero__desc">查看与管理服务订单与会员订单，支持按类型、状态、下单人、订单编号与时间范围筛选，可导出、派单与处理退款</p>
		</div>

		<div class="nf-filter-card shops-order-fw-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body shops-order-fw-page__filter-body">
				<div class="shops-order-fw-page__field">
					<span class="shops-order-fw-page__label">订单类型：</span>
					<el-select v-model="type" class="shops-order-fw-page__select" @change="animeDat(type)">
						<el-option v-for="item in typeNum" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="shops-order-fw-page__field">
					<span class="shops-order-fw-page__label">状态：</span>
					<el-select v-model="status" class="shops-order-fw-page__select" @change="animeDat(status)">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="shops-order-fw-page__field">
					<span class="shops-order-fw-page__label">下单人用户名：</span>
					<el-input class="shops-order-fw-page__input" @keydown.enter.native="select" placeholder="请输入下单人用户名"
						v-model="userName"></el-input>
				</div>
				<div class="shops-order-fw-page__field">
					<span class="shops-order-fw-page__label">订单编号：</span>
					<el-input class="shops-order-fw-page__input" @keydown.enter.native="select" placeholder="请输入订单编号"
						v-model="ordersNo"></el-input>
				</div>
				<div class="shops-order-fw-page__field">
					<span class="shops-order-fw-page__label">开始时间：</span>
					<el-date-picker class="shops-order-fw-page__date" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="shops-order-fw-page__field">
					<span class="shops-order-fw-page__label">截止时间：</span>
					<el-date-picker class="shops-order-fw-page__date" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="shops-order-fw-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button size="mini" type="warning" icon="el-icon-download" @click="exportBtn">导出Excel</el-button>
				</div>
			</div>
			<p class="shops-order-fw-page__export-tip">* 导出订单提示：导出订单前请进行时间或者状态等筛选，否则导出订单量过多易出现卡顿或系统崩溃</p>
		</div>

		<div class="shops-order-fw-page__tabs-wrap">
			<el-tabs v-model="activeName" class="shops-order-fw-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="订单中心" name="first">
					<div class="nf-table-card shops-order-fw-page__table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list" class="shops-order-fw-page__table">
					<el-table-column prop="ordersId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="riderName" label="接单员工">
						<template slot-scope="scope">
							<span v-if="scope.row.state==0"> - </span>
							<span v-else
								style="color: #009900;">{{ scope.row.riderName?scope.row.riderName:'未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span class="shops-order-fw-page__link" @click="updates(scope.row.userId)"
								v-if="scope.row.ordersType==1">{{ scope.row.ordersUserName }}</span>
							<span class="shops-order-fw-page__link" @click="updates(scope.row.userId)"
								v-if="scope.row.ordersType==2">{{ scope.row.ordersUserName }}</span>
						</template>
					</el-table-column>

					<el-table-column prop="companyName" label="商家名称" width="150">
					</el-table-column>
					<el-table-column prop="ordersNo" label="订单编号" width="150">
					</el-table-column>
					<el-table-column prop="ordersType" label="订单类型">
						<template slot-scope="scope">
							<span v-if="scope.row.ordersType==1">服务订单</span>
							<span v-if="scope.row.ordersType==2">会员订单</span>
						</template>
					</el-table-column>
					<el-table-column prop="myLevel" label="标题" width="150">
					</el-table-column>
					<el-table-column prop="oldMoney" label="发布价格" width="100">
					</el-table-column>
					<el-table-column prop="money" label="普通用户价格" width="100">
					</el-table-column>
					<el-table-column prop="memberMoney" label="会员价格" width="100">
					</el-table-column>
					<el-table-column prop="rate" label="商家佣金">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金">
						<template slot-scope="scope">
							<span style="color: #DD0000;cursor: pointer;"
								@click="updates(scope.row.zhiUserId)">{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金">
						<template slot-scope="scope">
							<span style="color: #DD0000;cursor: pointer;"
								@click="updates(scope.row.feiUserId)">{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="pingRate" label="平台金额">
						<template slot-scope="scope">
							<span>{{scope.row.pingRate?scope.row.pingRate:'0'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="时长" width="100">
					</el-table-column>
					<el-table-column prop="unit" label="单位" width="100">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.couponMoney}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="payMoney" label="支付金额">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.payMoney}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiMoney" label="一级佣金">
						<template slot-scope="scope">
							<span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.zhiUserId)">
								{{ scope.row.zhiMoney ? scope.row.zhiMoney : '0' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiMoney" label="二级佣金">
						<template slot-scope="scope">
							<span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.feiUserId)">
								{{ scope.row.feiMoney ? scope.row.feiMoney : '0' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="pingMoney" label="平台佣金">
						<template slot-scope="scope">
							<span style="color: #01af74;cursor: pointer;">
								{{ scope.row.pingMoney ? scope.row.pingMoney : '0' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="detailsAddress" label="服务信息" width="200">
						<template slot-scope="scope">
							<div>联系人姓名：{{scope.row.name}}</div>
							<div>联系人联系：{{scope.row.phone}}</div>
							<div>服务地址：{{scope.row.province}}{{scope.row.city}}{{scope.row.detailsAddress}}
								{{scope.row.district}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="startTime" label="上门时间" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.startTime}}</span>
							<el-button size="mini" type="primary" plain 
								@click="updateBz(scope.row)" style="margin:5px;">修改</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="remarks" label="备注" width="200">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.remark}}</span>
							<el-button size="mini" type="primary" plain 
								@click="updateBz(scope.row)" style="margin:5px;">修改</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="code" label="确认码">
					</el-table-column>
					<el-table-column prop="startServiceTime" label="服务开始时间"></el-table-column>
					<el-table-column prop="endServiceTime" label="服务结束时间"></el-table-column>
					<el-table-column prop="startServiceImg" label="服务开始前图片" width="180">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.startServiceImg">
								<div v-for="(item,index) in scope.row.startServiceImg.split(',')" :key="index">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
											slot="reference">
										<img style="width: 300px;height:auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="endServiceImg" label="服务结束后图片" width="180">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.endServiceImg">
								<div v-for="(item,index) in scope.row.endServiceImg.split(',')" :key="index">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
											slot="reference">
										<img style="width: 300px;height:auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="startAddress" label="服务开始地址" width="200"></el-table-column>
					<el-table-column prop="endAddress" label="服务结束地址" width="200"></el-table-column>
					<el-table-column prop="startServiceLongitude" label="服务开始经度"></el-table-column>
					<el-table-column prop="startServiceLatitude" label="服务开始纬度"></el-table-column>
					<el-table-column prop="endServiceLongitude" label="服务结束经度"></el-table-column>
					<el-table-column prop="endServiceLatitude" label="服务结束纬度"></el-table-column>
					<el-table-column prop="state" label="订单状态" fixed="right" width="80">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.state==0">待支付</span>
							<span style="color: #E87D0D;" v-if="scope.row.state==1">进行中</span>
							<span style="color: #009900;" v-if="scope.row.state==2">已完成</span>
							<span style="color: #999;" v-if="scope.row.state==3">已退款</span>
							<span style="color: #999;" v-if="scope.row.state==4">待服务</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="createTime" fixed="right" label="操作" width="160">
						<template slot-scope="scope">
							<!-- <el-button size="mini" type="primary" @click="orderDetails(scope.row)" style="margin: 3px;">详情
							</el-button> -->
							<el-button size="mini" type="primary" @click="bufeiBtn(scope.row)" style="margin: 3px;">补费列表
							</el-button>
							<el-button size="mini" type="primary" v-if="scope.row.state === 1 "
								:disabled="!isAuth('orderCenter:wancheng')" @click="wancheng(scope.row)"
								style="margin: 3px;">完成</el-button>
							<el-button size="mini" type="warning" v-if="scope.row.state === 1"
								:disabled="!isAuth('orderCenter:tuikuan')" @click="tuikuanBtn(scope.row)"
								style="margin: 3px;">退款</el-button>
							<el-button size="mini" type="warning" plain v-if="scope.row.state === 4"
								:disabled="!isAuth('orderCenter:tuikuan')" @click="quxiaoBtn(scope.row)"
								style="margin: 3px;">取消</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('orderCenter:delete')"
								@click="deletes(scope.row)" style="margin: 3px;">删除</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('shopsList:paidan')"
								@click="zuandanBtn(scope.row)" style="margin: 3px;" v-if="scope.row.state === 4 ">
								派单</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="shops-order-fw-page__stats">
					<span>本页累计总金额统计：{{totalMoney.toFixed(2)}}元</span>
					<span>一级佣金统计：{{totalMoneyS.toFixed(2)}}元</span>
					<span>二级佣金统计：{{totalMoneyTy.toFixed(2)}}元</span>
					<span>平台佣金统计：{{totalMoneyP.toFixed(2)}}元</span>
				</div>
				<div class="shops-order-fw-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 补费列表 -->
			<el-dialog title="补费列表" :visible.sync="dialogFormVisible" center width="80%">
				<el-table v-loading="tableDataLoading2" :data="userData">
					<el-table-column prop="recordId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称">
						<template slot-scope="scope">
							　<span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.userId)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="payMoney" label="补费金额">
					</el-table-column>
					<el-table-column prop="money" label="实际到账金额">
					</el-table-column>
					<el-table-column prop="rideName" label="接收用户">
						<template slot-scope="scope">
							　<span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.rideId)"
								v-text="scope.row.rideName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="payWay" label="支付方式">
						<template slot-scope="scope">
							<span v-if="scope.row.payWay==0">余额</span>
							<span v-if="scope.row.payWay==1">微信</span>
							<span v-if="scope.row.payWay==2">支付宝</span>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<span v-if="scope.row.status==1">已支付</span>
							<span v-if="scope.row.status==2">已退回</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
			</el-dialog>
			<!-- 员工列表 -->
			<el-dialog title="员工列表" :visible.sync="dialogFormVisibleHd" center width="70%">
				<!-- <div style="margin:2% 0;display: inline-block;">
					<el-input style="width: 180px;" @keydown.enter.native="select" clearable placeholder="请输入活动名称"
						v-model="integralName"></el-input>
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="selectHd">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleansHs">重置
					</el-button>
				</div> -->
				<el-table v-loading="tableDataLoadingY" :data="huodongData.list">
					<el-table-column prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column prop="userName" label="用户名">
						<template slot-scope="scope">
							<span style="color: #f56c6c;cursor:pointer"
								@click="updates(scope.row.userId)">{{ scope.row.userName }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="电话">
					</el-table-column>
					<el-table-column prop="updateTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" @click="querenBtns(scope.row)">确认派单</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 15, 20, 25]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="huodongData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 修改备注时间 -->
			<el-dialog title="修改备注时间" :visible.sync="dialogFormVisibleBz" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">预约时间：</span>
					<el-date-picker style="width: 50%;" v-model="serveTime" align="right"
						type="datetime" format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">备注：</span>
					<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入备注">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleBz = false">取 消</el-button>
					<el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
	</div>

</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 2,
				status: '',
				totalMoney: 0,
				userName: '',
				type: '',
				id: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待支付'
					},
					{
						value: 1,
						label: '进行中'
					},
					{
						value: 2,
						label: '已完成'
					},
					{
						value: 3,
						label: '已退款'
					},
					{
						value: 4,
						label: '待服务'
					}
				],
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '服务订单'
					},
					{
						value: 2,
						label: '会员订单'
					},
				],
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				phone: '',
				content: '',
				flag: '',
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				tableData: [],
				ordersNo: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
				tableDataLoading2: false,
				userData: [],

				totalMoney: 0,
				totalMoneyS: 0,
				totalMoneyTj: 0,
				totalMoneyTy: 0,
				totalMoneyP: 0,
				userIdShop: '',
				companyName: '',

				dialogFormVisibleHd: false,
				tableDataLoadingY: false,
				huodongData: {},
				
				dialogFormVisibleBz:false,
				ordersId:'',
				content: '',
				serveTime:'',
				shopName:'',

			}
		},
		methods: {
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
				if (tab._props.label == '用户反馈') {
					this.page = 1
					this.limit = 10
					this.state = 2
					this.dataSelect()
				}
			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible2 = true
			},

			// 获取数据列表
			dataSelect() {
				this.totalMoney = 0
				this.tableDataLoading = true
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('orders/queryOrders'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'type': this.type,
						'name': this.userName,
						'status': this.status,
						'ordersNo': this.ordersNo,
						'companyName': this.companyName,
						'startTime': this.startTime,
						'endTime': endTime,
						'orderTakingUserId': this.userIdShop
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].money) {
							this.totalMoney = this.totalMoney + data.data.list[i].money
						}
						if (data.data.list[i].zhiMoney) {
							this.totalMoneyS = this.totalMoneyS + data.data.list[i].zhiMoney
						}
						if (data.data.list[i].feiMoney) {
							this.totalMoneyTy = this.totalMoneyTy + data.data.list[i].feiMoney
						}

						if (data.data.list[i].pingMoney) {
							this.totalMoneyP = this.totalMoneyP + data.data.list[i].pingMoney
						}
					}
					let returnData = data.data;
					this.tableData = returnData
					// for (var i in this.tableData.list) {
					// 	this.tableData.list[i].goodsPriceZ = 0
					// 	this.totalMoney = this.totalMoney + this.tableData.list[i].payMoney

					// 	// 	for (var j in this.tableData.list[i].orderGoodsList) {
					// 	// 		this.tableData.list[i].orderGoodsList[j].goodsPriceZ = (this.tableData.list[i]
					// 	// 			.orderGoodsList[j]
					// 	// 			.goodsPrice * this.tableData.list[i].orderGoodsList[j].goodsNum)

					// 	// 		this.tableData.list[i].goodsPriceZ = this.tableData.list[i].goodsPriceZ + this
					// 	// 			.tableData.list[i].orderGoodsList[j].goodsPriceZ
					// 	// 	}


					// }
					// this.totalMoney = this.totalMoney.toFixed(2)

				})
			},
			// 详情跳转
			updates(userId) {
				if (userId) {
					if (userId) {
						this.$router.push({
							path: '/userDetail',
							query: {
								userId: userId
							}
						})
					}
				}


			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.userName = ''
				this.status = ''
				this.type = ''
				this.ordersNo = ''
				this.startTime = ''
				this.endTime = ''
				this.companyName = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			// 删除订单
			deletes(row) {
				let delid = row.ordersId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/deleteOrders?id=${delid}`),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				})
			},
			// 退款
			tuikuanBtn(row) {
				let delid = row.ordersId
				this.$confirm(`确定要进行退款操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/cancelOrder?id=${row.ordersId}&status=3`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			// 餐品完成
			wancheng(row) {

				this.$confirm(`是否确认完成订单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/cancelOrder?id=${row.ordersId}&status=2`),
						method: 'get',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			// 取消订单
			quxiaoBtn(row) {
				this.$confirm(`是否要取消订单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/cancelOrder?id=${row.ordersId}&status=3`),
						method: 'get',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			// 导出
			exportBtn() {
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('orders/ordersListExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'type': this.type,
						'name': this.userName,
						'status': this.status,
						'ordersNo': this.ordersNo,
						'startTime': this.startTime,
						'endTime': endTime,
						'companyName': this.companyName
					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 订单详情跳转
			orderDetails(row) {
				this.$router.push({
					path: "/orderDetails",
					query: {
						id: row.ordersId
					}
				});
			},
			// 补费列表弹框
			bufeiBtn(row) {
				this.bufeiSelect(row.ordersNo)

			},
			// 获取补费列表数据
			bufeiSelect(orderNo) {
				this.tableDataLoading2 = true
				this.$http({
					url: this.$http.adornUrl('admin/repairRecord/getRecordByOrderNo'),
					method: 'get',
					params: this.$http.adornParams({
						'orderType': 2,
						'orderNo': orderNo
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.dialogFormVisible = true
						this.userData = []
						this.userData.push(data.data)
						this.tableDataLoading2 = false
					} else {
						this.$message({
							message: '暂无数据',
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}


				})
			},
			dataSelectS() {
				if (this.$route.query.userId) {
					this.userIdShop = this.$route.query.userId
					this.companyName = this.$route.query.companyName
				} else {
					this.userIdShop = ''
					this.companyName = ''
				}
				this.dataSelect()
			},
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 转单弹框
			zuandanBtn(row) {
				this.shopId = row.ordersId
				this.dialogFormVisibleHd = true
				this.huodongSelect()
			},
			// 获取员工列表
			huodongSelect() {
				var userId = this.$route.query.userId
				this.tableDataLoadingHd = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'companyId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingHd = false
					let returnData = data.data
					this.huodongData = returnData
				})
			},
			// 确认派单
			querenBtns(row) {
				this.$confirm(`确定派单给此员工?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/assignTask'),
						method: 'post',
						params: this.$http.adornParams({
							'userId': row.userId,
							'ordersId': this.shopId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dialogFormVisibleHd = false
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			// 修改备注、时间
			updateBz(row){
				this.ordersId = row.ordersId
				this.serveTime = row.startTime
				this.content = row.remark
				this.dialogFormVisibleBz = true
			},
			// 修改
			replyNoticeTo() {
				
				this.$http({
					url: this.$http.adornUrl('orders/updateStartTime'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'ordersId': this.ordersId,
						'startTime': this.serveTime,
						'remarks': this.content,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisibleBz = false
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}
					
				})
			},
		},
		mounted() {
			console.log('this.$route.query.userId', this.$route.query.userId)
			if (this.$route.query.userId) {
				this.userIdShop = this.$route.query.userId
				this.companyName = this.$route.query.companyName
			} else {
				this.userIdShop = ''
				this.companyName = ''
			}
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelectS'
		}
	};
</script>

<style lang="scss" scoped>
.shops-order-fw-page {
	padding: 0;
	overflow-x: hidden;
}

.shops-order-fw-page__nav {
	display: flex;
	align-items: center;
	margin-bottom: 12px;
	font-size: 14px;
}

.shops-order-fw-page__back {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	text-decoration: none;
	color: var(--nf-text-secondary, #5a6578);
	cursor: pointer;
}

.shops-order-fw-page__back:hover {
	color: #01af74;
}

.shops-order-fw-page__back-icon {
	width: 1.2em;
	height: 1.2em;
}

.shops-order-fw-page__nav-sep {
	margin: 0 12px;
	color: #d9d9d9;
}

.shops-order-fw-page__nav-title {
	font-size: 16px;
	color: var(--nf-text-primary, #1a2433);
	font-weight: 500;
}

.shops-order-fw-page__hero {
	margin-bottom: 12px;
}

.shops-order-fw-page__filter {
	margin-bottom: 12px;
}

.shops-order-fw-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.shops-order-fw-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shops-order-fw-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.shops-order-fw-page__input {
	width: 200px;
}

.shops-order-fw-page__select {
	width: 150px;
}

.shops-order-fw-page__date {
	width: 160px;
}

.shops-order-fw-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shops-order-fw-page__export-tip {
	margin: 0;
	padding: 8px 16px 12px;
	font-size: 13px;
	color: #e6a23c;
	line-height: 1.5;
}

.shops-order-fw-page__tabs-wrap {
	margin-bottom: 0;
}

.shops-order-fw-page__tabs {
	margin-bottom: 0;
}

.shops-order-fw-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.shops-order-fw-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.shops-order-fw-page__table-card >>> .el-table {
	border-radius: 0;
}

.shops-order-fw-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.shops-order-fw-page__table >>> .el-table__body td {
	background: #fff;
}

.shops-order-fw-page__link {
	color: #01af74;
	cursor: pointer;
}

.shops-order-fw-page__stats {
	display: flex;
	flex-wrap: wrap;
	gap: 16px;
	padding: 12px 16px;
	font-size: 14px;
	color: #b94a48;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.shops-order-fw-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
