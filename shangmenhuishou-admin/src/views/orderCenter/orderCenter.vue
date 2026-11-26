<template>
	<div class="order-center-page">
		<div class="nf-page-hero order-center-page__hero">
			<span class="nf-page-hero__badge">订单管理</span>
			<h2 class="nf-page-hero__title">订单中心</h2>
			<p class="nf-page-hero__desc">管理回收预约订单，支持按状态、用户、订单编号及时间筛选，可完成、取消、派单与导出</p>
		</div>

		<div class="nf-filter-card order-center-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body order-center-page__filter-body">
				<div class="order-center-page__field">
					<span class="order-center-page__label">状态：</span>
					<el-select v-model="status" class="order-center-page__select" @change="animeDat(status)">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="order-center-page__field">
					<span class="order-center-page__label">接单用户：</span>
					<el-input class="order-center-page__input" @keydown.enter.native="select" placeholder="请输入接单用户"
						v-model="userName"></el-input>
				</div>
				<div class="order-center-page__field">
					<span class="order-center-page__label">预约用户昵称：</span>
					<el-input class="order-center-page__input" @keydown.enter.native="select" placeholder="请输入预约用户昵称"
						v-model="ordersUserName"></el-input>
				</div>
				<div class="order-center-page__field">
					<span class="order-center-page__label">订单编号：</span>
					<el-input class="order-center-page__input" @keydown.enter.native="select" placeholder="请输入订单编号"
						v-model="ordersNo"></el-input>
				</div>
				<div class="order-center-page__field">
					<span class="order-center-page__label">开始时间：</span>
					<el-date-picker class="order-center-page__date" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="order-center-page__field">
					<span class="order-center-page__label">截止时间：</span>
					<el-date-picker class="order-center-page__date" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="order-center-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button size="mini" type="warning" icon="el-icon-download" @click="exportBtn">导出Excel</el-button>
				</div>
			</div>
			<div class="order-center-page__tip">* 导出订单提示：导出订单前请进行时间或者状态等筛选，否则导出订单量过多易出现卡顿或系统崩溃</div>
		</div>

		<div class="order-center-page__tabs-wrap">
			<el-tabs v-model="activeName" class="order-center-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="订单中心" name="first">
					<div class="nf-table-card order-center-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records" class="order-center-page__table">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="预约用户昵称">
					<template slot-scope="scope">
						<span class="order-center-page__link" @click="updates(scope.row.userId)">{{ scope.row.userName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="riderName" label="回收员">
					<template slot-scope="scope"> 
						<span v-if="scope.row.state==0"> - </span>
						<span v-else class="order-center-page__link" @click="updates(scope.row.wasteUserId)">{{ scope.row.wasteUserName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderNo" label="订单编号" width="150">
				</el-table-column>
				<el-table-column prop="addressName" label="预约用户姓名" >
				</el-table-column>
				<el-table-column prop="addressPhone" label="预约用户电话" width="100">
				</el-table-column>
				<el-table-column prop="province" label="预约用户地址" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.address}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="note" label="订单备注" width="150">
					<!-- <template slot-scope="scope">
						<span>{{scope.row.note}}</span>
						<el-button size="mini" type="primary" plain v-if="scope.row.state!=2&&scope.row.state!=3"
							@click="updateBz(scope.row)" style="margin:5px;">修改</el-button>
					</template> -->
				</el-table-column>
				<el-table-column prop="appointmentTime" label="预约时间" width="160">
					<!-- <template slot-scope="scope">
						<span style="color: red;font-weight: 600;">{{scope.row.appointmentTime}}</span>
						<el-button size="mini" type="primary" plain v-if="scope.row.state!=2&&scope.row.state!=3"
							@click="updateBz(scope.row)" style="margin:5px;">修改</el-button>
					</template> -->
				</el-table-column>
				<el-table-column prop="wasteTime" label="接单时间" width="160"></el-table-column>
				<el-table-column prop="distance" label="距离"></el-table-column>
				<!-- <el-table-column prop="orderNumber" label="时长" width="100">
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
						<span style="color: #f56c6c;" v-if="scope.row.paySumMoney&&scope.row.paySumMoney!=0">
							{{scope.row.paySumMoney}}
						</span>
						<span style="color: #f56c6c;" v-else>{{scope.row.money}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="refundSumMoney" label="退款金额">
					<template slot-scope="scope">
						<span style="color: #01af74;">
							{{ scope.row.refundSumMoney ? scope.row.refundSumMoney : '0' }}
						</span>
					</template>
				</el-table-column> -->
				<!-- <el-table-column prop="startTime" label="上门时间" width="100">
					<template slot-scope="scope">
						<span style="color: red;font-weight: 600;">{{scope.row.startTime}}</span>
						<el-button size="mini" type="primary" plain v-if="scope.row.state!=2&&scope.row.state!=3"
							@click="updateBz(scope.row)" style="margin:5px;">修改</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="remarks" label="备注" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.remarks}}</span>
						<el-button size="mini" type="primary" plain v-if="scope.row.state!=2&&scope.row.state!=3"
							@click="updateBz(scope.row)" style="margin:5px;">修改</el-button>
					</template>
				</el-table-column> -->
				<el-table-column prop="orderItems" label="预约物品列表" width="300">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.orderItems&&scope.row.orderItems.length>0">
							<div v-for="(item,index) in scope.row.orderItems" :key="index">
								{{index+1}}.{{item.categoryName}}/{{item.categoryChildrenName}}:{{item.price}}￥/{{item.unit}}
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="订单状态">
					<template slot-scope="scope"> 
						<span style="color: #f56c6c;" v-if="scope.row.status==0">待接单</span>
						<span style="color: #E87D0D;" v-if="scope.row.status==1">待服务</span>
						<span style="color: #009900;" v-if="scope.row.status==2">进行中</span>
						<span style="color: #999;" v-if="scope.row.status==3">已完成</span>
						<span style="color: #999;" v-if="scope.row.status==4">已取消</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="createTime" fixed="right" label="操作" width="180">
					<template slot-scope="scope">
						<!-- <el-button size="mini" type="primary" @click="orderDetails(scope.row)" style="margin: 3px;">详情
						</el-button> -->
						<!-- <el-button size="mini" type="primary" @click="bufeiBtn(scope.row)" style="margin: 3px;">补费列表
						</el-button> -->
						<el-button size="mini" type="primary" v-if="scope.row.status === 2 "
							:disabled="!isAuth('orderCenter:wancheng')" @click="wancheng(scope.row)"
							style="margin: 3px;">完成</el-button>
						<el-button size="mini" type="warning" plain v-if="scope.row.status === 0||scope.row.status === 1"
							:disabled="!isAuth('orderCenter:tuikuan')" @click="quxiaoBtn(scope.row)"
							style="margin: 3px;">取消</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('orderCenter:delete')"
							@click="deletes(scope.row)" style="margin: 3px;">删除</el-button>
						<el-button size="mini" type="primary" plain
							v-if="scope.row.status == 0"  @click="zhuandan(scope.row)"
							style="margin: 3px;">派单</el-button>
						<!-- <div v-if="scope.row.state==1||scope.row.state==2">
							<el-button style="margin:5px;" size="mini" type="warning" icon="document"
								@click="tuikuanBtnBf(scope.row)"
								v-if="scope.row.refundSumMoney<scope.row.moneys">部分退款
							</el-button>
							<el-button style="margin:5px;" size="mini" type="warning" icon="document"
								:disabled="!isAuth('orderCenter:tuikuan')" @click="tuikuanBtn(scope.row)"
								v-if="scope.row.refundSumMoney<scope.row.moneys">退全款
							</el-button>
							<el-button style="margin:5px;"
								v-if="scope.row.refundSumMoney==scope.row.moneys"
								disabled size="mini" type="warning">金额已退完</el-button>
						</div> -->
					</template>
				</el-table-column>
						</el-table>
						<div class="order-center-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
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
				<el-table-column prop="refundMoney" label="退款金额">
					<template slot-scope="scope">
						<span style="color: #01af74;">
							{{ scope.row.refundMoney ? scope.row.refundMoney : '0' }}
						</span>
					</template>
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
		<!-- 修改备注时间 -->
		<el-dialog title="修改备注时间" :visible.sync="dialogFormVisibleBz" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">预约日期：</span>
				<el-date-picker style="width: 50%;" v-model="serveTime" align="right" type="date" format="yyyy-MM-dd"
					value-format="yyyy-MM-dd" placeholder="选择开始时间">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">预约时间：</span>
				<div style="margin-left: 200px;position: relative;bottom: 30px;">
					<el-radio-group v-model="times" size="small">
						<el-radio v-for="(item,index) in yuyueTimeDime" :key="index" :label="item.serviceTime" border
							style="margin: 3px;">{{item.serviceTime}}</el-radio>
					</el-radio-group>
				</div>
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
		<!-- 部分退款 -->
		<el-dialog title="部分退款" :visible.sync="dialogFormVisibleBftk" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">订单总金额：</span>
				<span style="color: brown;"> ¥ {{zongjine}}</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">退款金额：</span>
				<el-input style="width:50%;" v-model="content" type="number" placeholder="请输入退款金额">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleBftk = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTk()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 师傅列表 -->
		<el-dialog title="员工列表" :visible.sync="dialogFormVisibleSf" center width="80%">
			<div style="margin:5px;display: inline-block;">
				<span>手机号:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入手机号"
					v-model="phoneU"></el-input>
			</div>&emsp;&emsp;
			<div style="margin:5px;display: inline-block;">
				<span>昵称:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入昵称"
					v-model="campus"></el-input>
			</div>&emsp;&emsp;
			<!-- <div style="margin:5px;display: inline-block;">
				<span>商家名称:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入商家名称"
					v-model="sexU"></el-input>
			</div>&emsp;&emsp; -->
			<div style="margin:5px;display: inline-block;">
				<span>邀请码:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入邀请码"
					v-model="invitationCode"></el-input>
			</div>&emsp;&emsp;
			<div style="margin:5px;display: inline-block;">
				<span>邀请人邀请码:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入邀请人邀请码"
					v-model="inviterCode"></el-input>
			</div>&emsp;&emsp;
			<div style="display: inline-block;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="selectU">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansU">重置
				</el-button>
			</div>
			<!-- <el-tab-pane label="员工用户" name="three"> -->
				<el-table v-loading="tableDataLoadingU" :data="userListData.records">
					<el-table-column prop="userId" label="编号" width="80" fixed="left"></el-table-column>
					<el-table-column fixed prop="name" label="姓名" width="150">
						<template slot-scope="scope">
							<span
								style="color: #f56c6c;">{{ scope.row.name ? scope.row.name : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="contact" label="电话" width="150">
					</el-table-column>
					<el-table-column prop="gender" label="性别" width="80">
						<template slot-scope="scope">
							<div v-if="scope.row.gender==1">男</div>
							<div v-else>女</div>
						</template>
					</el-table-column>
					<el-table-column prop="birthday" label="生日" width="150">
					</el-table-column>
					<el-table-column prop="idCard" label="身份证号码" width="200">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="userBtns(scope.row)">确认
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeU" @current-change="handleCurrentChangeU"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="userListData.total">
					</el-pagination>
				</div>
			<!-- </el-tab-pane> -->
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
				ordersUserName: '',
				type: '',
				id: '',
				statesnum: [{  //0=待接单；1=待服务；2=进行中；3=已完成；4=已取消；
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待接单'
					},
					{
						value: 1,
						label: '待服务'
					},
					{
						value: 2,
						label: '进行中'
					},
					{
						value: 3,
						label: '已完成'
					},
					{
						value: 4,
						label: '已取消'
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
				info3: {
					stockDate3: this.getNowTime3(), //日期
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

				dialogFormVisibleBz: false,
				ordersId: '',
				content: '',
				serveTime: '',
				shopName: '',
				yuyueTimeDime: [],
				times: '',
				timeNow: '',
				serviceUserId: '',
				// 不分退款新增
				zongjine: 0,
				dacefei: 0,
				xiangmujin: 0,
				dialogFormVisibleBftk: false,
				
				cityU: '',
				campus: '',
				phoneU: '',
				limit1: 10,
				page1: 1,
				activeNameU: 'three',
				isAuthentication: 2,
				isCompany: '',
				isVip: '',
				shopType: 2,
				invitationCode: '',
				inviterCode: '',
				ordersId: '',
				orderTakingUserId: '',
				userListData:{},
				dialogFormVisibleSf:false,
				sexU:'',
				tableDataLoadingU:false,
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
			//处理默认选中当前日期
			getNowTime3() {
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
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
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
					url: this.$http.adornUrl('admin/waste/order/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'type': this.type,
						'userName': this.userName,
						'status': this.status,
						'orderNo': this.ordersNo,
						'companyName': this.companyName,
						'startTime': this.startTime,
						'endTime': endTime,
						'orderTakingUserId': this.userIdShop,
						'ordersUserName': this.ordersUserName
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
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
				this.userIdShop = ''
				this.companyName = ''
				this.ordersUserName = ''
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
						url: this.$http.adornUrl('orders/refundOrders'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': delid,
							'orderType': 2
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
						'userName': this.userName,
						'status': this.status,
						'ordersNo': this.ordersNo,
						'startTime': this.startTime,
						'endTime': endTime,
						'companyName': this.companyName,
						'ordersUserName': this.ordersUserName
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
						this.userData = data.data
						// this.userData.push(data.data)
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
				if (this.$route.query.ordersNo) {
					this.ordersNo = this.$route.query.ordersNo
				} else {
					this.ordersNo = ''
				}
				this.dataSelect()
			},
			// 修改备注、时间
			updateBz(row) {
				this.timeNow = this.info3.stockDate3.slice(10, 16)
				var datas = row.startTime.slice(0, 10)
				var times = row.startTime.slice(10, 22)
				this.ordersId = row.ordersId
				this.serveTime = datas
				this.times = times
				this.content = row.remarks
				this.serviceUserId = row.serviceUserId
				this.yuyueTimeSelect()
				this.dialogFormVisibleBz = true
				console.log(this.timeNow)
			},
			// 修改
			replyNoticeTo() {
				var dataTimes = this.serveTime + ' ' + this.times
				this.$http({
					url: this.$http.adornUrl('orders/updateStartTime'),
					method: 'get',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'ordersId': this.ordersId,
						'startTime': dataTimes,
						'remarks': this.content,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisibleBz = false
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
			// 获取服务时间段
			yuyueTimeSelect() {
				this.$http({
					url: this.$http.adornUrl('serviceTime/selectOrderServiceTime'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': this.serviceUserId,
						'page': this.page,
						'limit': this.limit,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.yuyueTimeDime = returnData.records
				})
			},
			// 部分退款弹框
			tuikuanBtnBf(row) {
				this.content = ''
				this.ordersId = row.ordersId
				this.$http({
					url: this.$http.adornUrl('orders/selectRefundMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'orderType': 2,
						'ordersId': this.ordersId
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.zongjine = data.data
					} else {
						this.$message({
							message: '暂无数据',
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
				this.dialogFormVisibleBftk = true
			},
			// 确定退款
			replyNoticeTk(row) {
				if (this.content === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入退款金额',
						type: 'warning'
					});
					return
				}
				if (Number(this.content) <= 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入有效的金额',
						type: 'warning'
					});
					return
				}
				if (Number(this.content) > Number(this.zongjine)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入有效的金额',
						type: 'warning'
					});
					return
				}
				this.$confirm(`确定要退款吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/refundMoney'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': this.ordersId,
							'money': this.content,
							'orderType': 2,
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
									this.dialogFormVisibleBftk = false
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
			// 退全款
			tuikuanBtnQ(row) {
				let delid = row.ordersId
				this.$confirm(`确定退全款吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/refundOrders'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': delid,
							'orderType': 2
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
			// 转单弹框
			zhuandan(row) {
			
				this.ordersId = row.ordersId
				this.orderTakingUserId = row.orderTakingUserId
				this.page1 = 1
				this.campus = ''
				this.phoneU = ''
				this.sexU = ''
				this.invitationCode = ''
				this.inviterCode = ''
				this.dataSelectUser()
				this.dialogFormVisibleSf = true
			},
			// 确认选择
			userBtns(row) {
			
				this.$http({
					url: this.$http.adornUrl('orders/assignTask'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': row.userId,
						'ordersId': this.ordersId,
			
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
								this.dataSelect()
								this.dialogFormVisibleSf = false
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
			// 获取数据列表
			dataSelectUser() {
				this.tableDataLoadingU = true
				
				this.$http({
					url: this.$http.adornUrl('admin/userAuthentication/page'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'page': this.page1,
						'limit': this.limit1,
						'status': 2,
						'contact': this.phoneU,
						'name': this.campus,
						// 'isAuthentication': 0,
						// 'shopType': '',
						// 'companyName': this.sexU,
						// 'invitationCode': this.invitationCode,
						// 'inviterCode': this.inviterCode,
						// 'isVip': '',
						// 'companyId': this.orderTakingUserId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingU = false
					let returnData = data.data
					this.userListData = returnData
				})
			},
			// 查询
			selectU() {
				this.page1 = 1
				this.dataSelectUser()
			},
			// 重置
			cleansU() {
				this.page1 = 1
				this.campus = ''
				this.phoneU = ''
				this.sexU = ''
				this.invitationCode = ''
				this.inviterCode = ''
				this.dataSelectUser()
			},
			handleSizeChangeU(val) {
				this.limit1 = val;
				this.dataSelectUser()
			},
			handleCurrentChangeU(val) {
				this.page1 = val;
				this.dataSelectUser()
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
			if (this.$route.query.ordersNo) {
				this.ordersNo = this.$route.query.ordersNo
			} else {
				this.ordersNo = ''
			}
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelectS'
		}
	};
</script>

<style lang="scss" scoped>
.order-center-page {
	padding: 0;
	overflow-x: hidden;
}

.order-center-page__hero {
	margin-bottom: 12px;
}

.order-center-page__filter {
	margin-bottom: 12px;
}

.order-center-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.order-center-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.order-center-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.order-center-page__input {
	width: 200px;
}

.order-center-page__select {
	width: 150px;
}

.order-center-page__date {
	width: 160px;
}

.order-center-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.order-center-page__tip {
	margin-top: 12px;
	font-size: 12px;
	color: #e6a23c;
	line-height: 1.5;
}

.order-center-page__tabs-wrap {
	margin-bottom: 0;
}

.order-center-page__tabs {
	margin-bottom: 0;
}

.order-center-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.order-center-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.order-center-page__table-card >>> .el-table {
	border-radius: 0;
}

.order-center-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.order-center-page__table >>> .el-table__body td {
	background: #fff;
}

.order-center-page__link {
	color: #01af74;
	cursor: pointer;
}

.order-center-page__link:hover {
	color: #017c52;
}

.order-center-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>