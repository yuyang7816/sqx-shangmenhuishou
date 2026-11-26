<template>
	<div class="shops-order-page">
		<div class="shops-order-page__nav">
			<a href="#" class="shops-order-page__back" @click.prevent="prev">
				<icon-svg name="jiantou" class="shops-order-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="shops-order-page__nav-sep">|</span>
			<span class="shops-order-page__nav-title">任务订单列表</span>
		</div>

		<div class="nf-page-hero shops-order-page__hero">
			<span class="nf-page-hero__badge">订单管理</span>
			<h2 class="nf-page-hero__title">任务订单列表</h2>
			<p class="nf-page-hero__desc">查看与管理任务订单，支持按状态、服务类型、手机号与昵称筛选，可派单、修改预约时间与备注</p>
		</div>

		<div class="nf-filter-card shops-order-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body shops-order-page__filter-body">
				<div class="shops-order-page__field">
					<span class="shops-order-page__label">状态：</span>
					<el-select v-model="status" class="shops-order-page__select-sm" @change="animeDat2(status)">
						<el-option v-for="item in taskstates" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="shops-order-page__field">
					<span class="shops-order-page__label">服务类型：</span>
					<el-select v-model="taskTypeT" class="shops-order-page__select-sm" @change="phoneSelect()">
						<el-option v-for="item in parentIdList" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="shops-order-page__field">
					<span class="shops-order-page__label">手机号：</span>
					<el-input class="shops-order-page__input" @keydown.enter.native="phoneSelect" placeholder="请输入手机号"
						v-model="phone">
					</el-input>
				</div>
				<div class="shops-order-page__field">
					<span class="shops-order-page__label">骑手电话：</span>
					<el-input class="shops-order-page__input" @keydown.enter.native="phoneSelect" placeholder="请输入骑手电话"
						v-model="riderPhone">
					</el-input>
				</div>
				<div class="shops-order-page__field">
					<span class="shops-order-page__label">昵称：</span>
					<el-input class="shops-order-page__input" @keydown.enter.native="phoneSelect" placeholder="请输入昵称"
						v-model="userName">
					</el-input>
				</div>
				<div class="shops-order-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
				</div>
			</div>
		</div>

		<div class="shops-order-page__tabs-wrap">
			<el-tabs v-model="activeName" class="shops-order-page__tabs">
				<el-tab-pane label="任务订单列表" name="first">
					<div class="nf-table-card shops-order-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData2.list" class="shops-order-page__table">
			<el-table-column fixed prop="helpOrderId" label="编号" width="80"></el-table-column>
			<el-table-column fixed prop="userName" label="发单用户" width="100">
				<template slot-scope="scope">
					<span class="shops-order-page__link" @click="updates(scope.row.userId)">
						{{ scope.row.userName ? scope.row.userName : '未绑定' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column fixed prop="userName" label="接单员工" width="100">
				<template slot-scope="scope">
					<span class="shops-order-page__link" @click="updates(scope.row.companyId)">
						{{ scope.row.rideName ? scope.row.rideName : '-' }}
					</span>
				</template>
			</el-table-column>

			<el-table-column prop="taskType" label="服务类型">
				<template slot-scope="scope">
					<span v-if="scope.row.taskType == 1">悬赏任务</span>
					<span v-if="scope.row.taskType == 2">报价任务</span>
					<span v-if="scope.row.taskType == 3">一口价任务</span>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="serviceName" label="服务分类"></el-table-column> -->
			<el-table-column prop="name" label="下单人姓名" width="100"></el-table-column>
			<el-table-column prop="phone" label="下单人手机号" width="120"></el-table-column>
			<el-table-column prop="province" label="下单人地址" width="200">
				<template slot-scope="scope">
					<span>{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.detailsAddress}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="content" label="提交内容"></el-table-column>
			<el-table-column prop="money" label="接单价格" width="100"></el-table-column>

			<el-table-column prop="startTime" label="服务开始时间"></el-table-column>
			<el-table-column prop="endTime" label="服务结束时间"></el-table-column>
			<el-table-column prop="startTaskAddress" label="服务开始地址"></el-table-column>
			<el-table-column prop="endTaskAddress" label="服务结束地址"></el-table-column>
			<el-table-column prop="startTime" label="服务开始时间"></el-table-column>
			<el-table-column prop="endTime" label="服务结束时间"></el-table-column>

			<el-table-column prop="startTaskImg" label="服务开始前图片" width="180">
				<template slot-scope="scope">
					<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.startTaskImg">
						<div v-for="(item,index) in scope.row.startTaskImg.split(',')" :key="index">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 300px;height:auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="endTaskImg" label="服务结束后图片" width="180">
				<template slot-scope="scope">
					<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.endTaskImg">
						<div v-for="(item,index) in scope.row.endTaskImg.split(',')" :key="index">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 300px;height:auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="startAddress" label="服务开始地址" width="200"></el-table-column>
			<el-table-column prop="endAddress" label="服务结束地址" width="200"></el-table-column>
			<el-table-column prop="startTaskLongitude" label="服务开始经度"></el-table-column>
			<el-table-column prop="startTaskLatitude" label="服务开始纬度"></el-table-column>
			<el-table-column prop="endTaskLongitude" label="服务结束经度"></el-table-column>
			<el-table-column prop="endTaskLatitude" label="服务结束纬度"></el-table-column>
			<el-table-column prop="code" label="确认码"></el-table-column>
			<el-table-column prop="serviceTime" label="预约时间" fixed="right" width="80">
				<template slot-scope="scope">
					<span style="color: red;font-weight: 600;">{{scope.row.deliveryTime}}</span>
					<el-button size="mini" type="primary" plain
						@click="updateBz(scope.row)" style="margin:5px;">修改</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="remarks" label="订单备注" width="200">
				<template slot-scope="scope">
					<span>{{scope.row.remarks ? scope.row.remarks : '未填写'}}</span>
					<el-button size="mini" type="primary" plain
						@click="updateBz(scope.row)" style="margin:5px;">修改</el-button>
				</template>
			</el-table-column>
			<el-table-column fixed="right" prop="status" label="状态" width="100">
				<template slot-scope="scope">
					<span class="shops-order-page__status--active" v-if="scope.row.status === 1 ">待服务</span>
					<span class="shops-order-page__status--active" v-if="scope.row.status === 2 ">服务中</span>
					<span class="shops-order-page__status--active" v-if="scope.row.status === 3 ">已完成</span>
					<span class="shops-order-page__status--muted" v-if="scope.row.status === 4 ">已取消</span>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="操作" width="100" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('shopsList:paidan')"
						@click="zuandanBtn(scope.row)"  v-if="scope.row.status === 1 ">
						派单</el-button>
				</template>
			</el-table-column>
						</el-table>
						<div class="shops-order-page__summary" v-if="totalMoney">本页累计交易统计：{{totalMoney}}元</div>
						<div class="shops-order-page__pagination">
							<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 聊天内容 -->
		<el-dialog title="聊天内容" :visible.sync="dialogFormVisible" center>
			<el-table v-loading="tableDataLoading1" :data="userData.list">
				<el-table-column prop="chatContentId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						<div v-if="scope.row.shopId">
							{{ scope.row.shopName}}
						</div>
						<div v-else style="color: #01af74;cursor: pointer;" @click="details(scope.row)">
							{{scope.row.userName ? scope.row.userName : scope.row.riderName}}
						</div>

					</template>
				</el-table-column>
				<el-table-column prop="title" label="用户头像">
					<template slot-scope="scope">
						<div>
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px"
									:src="scope.row.shopCover?scope.row.shopCover:scope.row.riderAvatar?scope.row.riderAvatar:scope.row.userAvatar"
									alt="" slot="reference">
								<img style="width: 200px; height: 200px"
									:src="scope.row.shopCover?scope.row.shopCover:scope.row.riderAvatar?scope.row.riderAvatar:scope.row.userAvatar"
									alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="信息内容" width="280">
					<template slot-scope="scope">
						<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
						<div v-if="scope.row.messageType==2">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.content" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.content" alt="">
							</el-popover>
						</div>
						<div v-if="scope.row.messageType==3">
							<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
						</div>
						<div v-if="scope.row.messageType==4">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px"
									:src="'https://smhsadmin.xianmxkj.com/emoji/'+scope.row.content" alt=""
									slot="reference">
								<img style="width: 200px; height: 200px"
									:src="'https://smhsadmin.xianmxkj.com/emoji/'+scope.row.content" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="150">
				</el-table-column>
				<el-table-column prop="createTime" label="操作" width="100" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
							@click="deleteuser(scope.row)">
							删除</el-button>
					</template>
				</el-table-column>

			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
					layout="total,sizes, prev, pager, next" :total="userData.totalCount">
				</el-pagination>
			</div>
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
						<el-button size="mini" type="danger"
							@click="querenBtns(scope.row)">确认派单</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 15, 20, 25]" :page-size="limitH" :current-page="pageH"
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
				activeName: 'first',
				limit: 10,
				page: 1,
				totalMoney: 0,
				phone: '',
				status: '',
				taskstates: [{
						label: '全部',
						value: ''
					},
					{
						label: '待服务',
						value: 1
					},
					{
						label: '进行中',
						value: 2
					},
					{
						label: '已完成',
						value: 3
					},
					{
						label: '已取消',
						value: 4
					},
				],
				orderType: '',
				shopId: '',
				shopList: '',
				id: '',
				content: '',
				tableDataLoading: false,
				tableDataLoading1: false,
				dialogFormVisible: false,
				tableData2: {},

				userName: '',
				orderNumber: '',
				chatConversationId: '',
				userData: {},
				limit1: 10,
				page1: 1,
				riderPhone: '',

				dialogFormVisibleHd: false,
				tableDataLoadingY:false,
				huodongData:{},
				parentIdList: [{
						label: '悬赏任务',
						value: 1
					},
					{
						label: '报价任务',
						value: 2
					},
					{
						label: '一口价任务',
						value: 3
					}
				],
				taskTypeT: '',

				dialogFormVisibleBz:false,
				ordersId:'',
				content: '',
				serveTime:'',
				shopName:'',
        limitH: 10,
        pageH: 1,
			}
		},
		methods: {
			// 详情跳转
			updates(userId) {
				if(userId){
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}else{
					this.$message({
						message: '用户未绑定',
						type: 'warning',
						duration: 1500,
						onClose: () => {}
					})
				}

			},
			// 详情跳转
			details(row) {
				if (!row.shopId) {

				}
				let userId = row.userId ? row.userId : row.riderId
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
    handleSizeChange(val) {
    	this.limitH = val;
    	this.huodongSelect()
    },
    handleCurrentChange(val) {
    	this.pageH = val;
    	this.huodongSelect()
    },
			handleSizeChange3(val) {
				this.limit = val
				this.taskdataSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.taskdataSelect()
			},
			handleSizeChange1(val) {
				this.limit1 = val;
				this.neirongSelect(this.chatConversationId)
			},
			handleCurrentChange1(val) {
				this.page1 = val;
				this.neirongSelect(this.chatConversationId)
			},


			// 手机号搜索
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.phone = ''
				this.riderPhone = ''
				this.status = ''
				this.userName = ''
				this.orderNumber = ''
				this.orderType = ''
				this.taskTypeT = ''
				this.page = 1
				this.taskdataSelect()
			},

			animeDat2(state) {
				this.page = 1
				this.taskdataSelect()
			},
			// 获取接单数据列表
			taskdataSelect() {
				let userId = this.$route.query.userId
        this.tableDataLoading =true
				this.$http({
					url: this.$http.adornUrl('help/selectHelpTakeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': this.status,
						'rideUserId': userId,
						'phone': this.phone,
						'gameId': '',
						'startTime': '',
						'endTime': '',
						'taskType': this.taskTypeT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
				})

			},
			// 商铺列表
			classifySelect() {
				this.$http({
					url: this.$http.adornUrl('admin/goods/selectAllShopList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.shopList = returnData
				})
			},
			// 餐品完成
			wancheng(row) {

				if (row.status == 3) {
					var status1 = 4
				}
				if (row.status == 6) {
					var status1 = 3
				}
				this.$http({
					url: this.$http.adornUrl('admin/order/updateOrder'),
					method: 'post',
					data: this.$http.adornData({
						'status': status1,
						'orderId': row.orderId,
						'orderNumber': row.orderNumber
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
								this.taskdataSelect()
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
			},

			// 返回上一级
			prev() {
				this.$router.back()
			},

			select1() {
				this.page1 = 1
				this.neirongSelect(this.chatConversationId)
			},
			// 重置
			cleans1() {
				this.content = ''
				this.page1 = 1
				this.neirongSelect(this.chatConversationId)
			},

			// 获取聊天内容列表
			neirongSelect(chatConversationId) {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('shop/ordersChat/selectGameChatDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'ordersId': this.chatConversationId,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.userData = returnData
				})
			},
			//删除聊天
			deleteuser(row) {
				// let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('shop/ordersChat/deleteOrdersChat?chatContentId=' + row
							.chatContentId),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.neirongSelect(this.chatConversationId)
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
			// 转单弹框
			zuandanBtn(row){
        this.pageH = 1
				this.shopId = row.helpTakeId
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
						'page': this.pageH,
						'limit': this.limitJ,
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
			querenBtns(row){
				this.$confirm(`确定派单给此员工?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('help/distributionOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'companyId':row.userId,
							'takeId':this.shopId
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
									this.taskdataSelect()
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
				this.serveTime = row.deliveryTime
				this.content = row.remarks
				this.dialogFormVisibleBz = true
			},
			// 修改
			replyNoticeTo() {

				this.$http({
					url: this.$http.adornUrl('artificer/updateOrders'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'ordersId': this.ordersId,
						'serveTime': this.serveTime,
						'remark': this.content,
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
								this.taskdataSelect()
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

			// this.classifySelect()
			this.taskdataSelect()

		},
		watch: {
			'$route': 'taskdataSelect'
		}
	}
</script>

<style lang="scss" scoped>
.shops-order-page {
	padding: 0;
	overflow-x: hidden;
}

.shops-order-page__nav {
	display: flex;
	align-items: center;
	font-size: 14px;
	margin-bottom: 12px;
	color: var(--nf-text-primary, #1a2433);
}

.shops-order-page__back {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	text-decoration: none;
	color: var(--nf-text-secondary, #5a6578);
	transition: color 0.2s;

	&:hover {
		color: #01af74;
	}
}

.shops-order-page__back-icon {
	width: 1.2em;
	height: 1.2em;
}

.shops-order-page__nav-sep {
	margin: 0 12px;
	color: #d9d9d9;
}

.shops-order-page__nav-title {
	font-size: 16px;
	font-weight: 500;
}

.shops-order-page__hero {
	margin-bottom: 12px;
}

.shops-order-page__filter {
	margin-bottom: 12px;
}

.shops-order-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.shops-order-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shops-order-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.shops-order-page__input {
	width: 200px;
}

.shops-order-page__select-sm {
	width: 150px;
}

.shops-order-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shops-order-page__tabs-wrap {
	margin-bottom: 0;
}

.shops-order-page__tabs {
	margin-bottom: 0;
}

.shops-order-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.shops-order-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.shops-order-page__table-card >>> .el-table {
	border-radius: 0;
}

.shops-order-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.shops-order-page__table >>> .el-table__body td {
	background: #fff;
}

.shops-order-page__link {
	color: #01af74;
	cursor: pointer;
}

.shops-order-page__status--active {
	color: #01af74;
}

.shops-order-page__status--muted {
	color: #999;
}

.shops-order-page__summary {
	padding: 12px 16px;
	font-size: 14px;
	color: #b94a48;
	font-weight: 600;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.shops-order-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
