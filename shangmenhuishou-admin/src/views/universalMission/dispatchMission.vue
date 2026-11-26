<template>
	<div class="dispatch-mission-page">
		<div class="nf-page-hero dispatch-mission-page__hero">
			<span class="nf-page-hero__badge">任务管理</span>
			<h2 class="nf-page-hero__title">接单列表</h2>
			<p class="nf-page-hero__desc">查看接单订单记录，支持按状态、服务类型、分类、手机号及时间筛选查询与导出</p>
		</div>

		<div class="nf-filter-card dispatch-mission-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body dispatch-mission-page__filter-body">
				<div class="dispatch-mission-page__field">
					<span class="dispatch-mission-page__label">状态：</span>
					<el-select v-model="status1" class="dispatch-mission-page__select" @change="animeDat2()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="dispatch-mission-page__field">
					<span class="dispatch-mission-page__label">服务类型：</span>
					<el-select v-model="taskTypeT" class="dispatch-mission-page__select" @change="animeDat2()">
						<el-option v-for="item in parentIdList" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="dispatch-mission-page__field">
					<span class="dispatch-mission-page__label">服务分类：</span>
					<el-cascader class="dispatch-mission-page__cascader" v-model="gameIdsVal" :options="homeData1"
						placeholder="请选择服务分类" :show-all-levels="false"
						:props="{ expandTrigger: 'hover', value:'classifyId', label:'classifyName'}" @change="handleChange2"
						filterable>
					</el-cascader>
				</div>
				<div class="dispatch-mission-page__field">
					<span class="dispatch-mission-page__label">下单人手机号：</span>
					<el-input class="dispatch-mission-page__input" @keydown.enter.native="phoneSelect"
						placeholder="请输入下单人手机号" v-model="phone1">
					</el-input>
				</div>
				<div class="dispatch-mission-page__field">
					<span class="dispatch-mission-page__label">订单号：</span>
					<el-input class="dispatch-mission-page__input" @keydown.enter.native="phoneSelect"
						placeholder="请输入订单号" v-model="orderNoT"></el-input>
				</div>
				<div class="dispatch-mission-page__field">
					<span class="dispatch-mission-page__label">开始时间：</span>
					<el-date-picker class="dispatch-mission-page__date" v-model="startTime" align="right" type="datetime"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="dispatch-mission-page__field">
					<span class="dispatch-mission-page__label">截止时间：</span>
					<el-date-picker class="dispatch-mission-page__date" v-model="endTime" align="right" type="datetime"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="dispatch-mission-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
					<el-button size="mini" type="warning" icon="el-icon-download" @click="exportBtn">导出Excel</el-button>
				</div>
			</div>
			<p class="dispatch-mission-page__export-tip">* 导出订单提示：导出订单前请进行时间或者状态等筛选，否则导出订单量过多易出现卡顿或系统崩溃</p>
		</div>

		<div class="nf-table-card dispatch-mission-page__table-card">
		<el-table v-loading="tableDataLoading" :data="tableData2.list" class="dispatch-mission-page__table">
			<el-table-column fixed prop="helpOrderId" label="编号" width="80"></el-table-column>
			<el-table-column fixed prop="userName" label="发单用户" width="100">
				<template slot-scope="scope">
					<span class="dispatch-mission-page__link" @click="updates(scope.row.userId)">
						{{ scope.row.userName ? scope.row.userName : '未绑定' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column fixed prop="helpTakeUserName" label="接单用户" width="100">
				<template slot-scope="scope">
					<span class="dispatch-mission-page__link" @click="updates(scope.row.helpTakeUserId)">
						{{ scope.row.helpTakeUserName ? scope.row.helpTakeUserName : '未绑定' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="task_type" label="服务类型">
				<template slot-scope="scope">
					<span v-if="scope.row.taskType == 1">悬赏任务</span>
					<span v-if="scope.row.taskType == 2">报价任务</span>
					<span v-if="scope.row.taskType == 3">一口价任务</span>
					<span v-if="scope.row.taskType == 4">师傅任务</span>
				</template>
			</el-table-column>
			<el-table-column prop="serviceName" label="服务分类"></el-table-column>
			<el-table-column prop="name" label="下单人姓名" width="100"></el-table-column>
			<el-table-column prop="phone" label="下单人手机号" width="120"></el-table-column>
			<el-table-column prop="province" label="下单人地址" width="200">
				<template slot-scope="scope">
					<span>{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.detailsAddress}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="orderNo" label="订单号" width="200"></el-table-column>
			<el-table-column prop="content" label="提交内容"></el-table-column>
			<el-table-column prop="money" label="接单价格" width="100"></el-table-column>
			<el-table-column prop="zhiMoney" label="一级佣金">
				<template slot-scope="scope">
					<span class="dispatch-mission-page__link" @click="updates(scope.row.zhiUserId)">
						{{ scope.row.zhiMoney ? scope.row.zhiMoney : '0' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="feiMoney" label="二级佣金">
				<template slot-scope="scope">
					<span class="dispatch-mission-page__link" @click="updates(scope.row.feiUserId)">
						{{ scope.row.feiMoney ? scope.row.feiMoney : '0' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="pingMoney" label="平台佣金">
				<template slot-scope="scope">
					<span class="dispatch-mission-page__link">
						{{ scope.row.pingMoney ? scope.row.pingMoney : '0' }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="startTime" label="服务开始时间"></el-table-column>
			<el-table-column prop="endTime" label="服务结束时间"></el-table-column>
			<el-table-column prop="startTaskAddress" label="服务开始地址"></el-table-column>
			<el-table-column prop="endTaskAddress" label="服务结束地址"></el-table-column>
			<el-table-column prop="startTaskLongitude" label="服务开始经度"></el-table-column>
			<el-table-column prop="startTaskLatitude" label="服务开始纬度"></el-table-column>
			<el-table-column prop="endTaskLongitude" label="服务结束经度"></el-table-column>
			<el-table-column prop="endTaskLatitude" label="服务结束纬度"></el-table-column>

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
			<el-table-column prop="helpTake.endTaskImg" label="服务结束后图片" width="180">
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
			<el-table-column prop="code" label="确认码"></el-table-column>
			<el-table-column prop="serviceTime" label="预约时间" fixed="right" width="120">
				<template slot-scope="scope">
					<span class="dispatch-mission-page__time">{{scope.row.deliveryTime}}</span>
					<el-button size="mini" class="dispatch-mission-page__text-btn"
						@click="rechargenone(scope.row,2)">修改</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="remarks" label="订单备注" width="200">
				<template slot-scope="scope">
					<span>{{scope.row.remarks ? scope.row.remarks : '未填写'}}</span>
					<el-button size="mini" class="dispatch-mission-page__text-btn"
						@click="rechargenone(scope.row,1)">修改</el-button>
				</template>
			</el-table-column>
			<el-table-column fixed="right" prop="status" label="状态" width="100">
				<template slot-scope="scope">
					<span class="dispatch-mission-page__status--active" v-if="scope.row.status === 1 ">待服务</span>
					<span class="dispatch-mission-page__status--active" v-if="scope.row.status === 2 ">服务中</span>
					<span class="dispatch-mission-page__status--active" v-if="scope.row.status === 3 ">已完成</span>
					<span class="dispatch-mission-page__status--muted" v-if="scope.row.status === 4 ">已取消</span>
				</template>
			</el-table-column>
			<!-- <el-table-column fixed="right" prop="status" label="操作" width="80">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" v-if="scope.row.status != 1 " @click="plCompile(scope.row)">评论
					</el-button>
				</template>
			</el-table-column> -->
		</el-table>
		<div class="dispatch-mission-page__stats">
			<span class="dispatch-mission-page__stat-item">本页累计总金额统计：<em>{{totalMoney.toFixed(2)}}</em>元</span>
			<span class="dispatch-mission-page__stat-item">一级佣金统计：<em>{{totalMoneyS.toFixed(2)}}</em>元</span>
			<span class="dispatch-mission-page__stat-item">二级佣金统计：<em>{{totalMoneyTy.toFixed(2)}}</em>元</span>
			<span class="dispatch-mission-page__stat-item">平台佣金统计：<em>{{totalMoneyP.toFixed(2)}}</em>元</span>
		</div>
		<div class="dispatch-mission-page__pagination">
			<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
				:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
			</el-pagination>
		</div>
		</div>
		<!-- 服务拒绝弹框 -->
		<el-dialog title="服务拒绝" :visible.sync="dialogFormVisible7" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入拒绝理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible7 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<div style="margin-bottom: 10px;" v-if="types==1">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="conters" type="textarea" :rows="4" placeholder="请输入备注"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="types==2">
				<span style="width: 200px;display: inline-block;text-align: right;">预约时间：</span>

				<el-date-picker v-model="conters" type="datetime" placeholder="选择日期时间" format="yyyy-MM-dd HH:mm"
					value-format="yyyy-MM-dd HH:mm">
				</el-date-picker>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 评论列表 -->
		<el-dialog title="评论列表" :visible.sync="dialogFormVisible10" center>
			<el-table v-loading="tableDataLoading" :data="pinglunData.records">
				<el-table-column prop="evaluateId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="评论用户" width="120">
					<template slot-scope="scope">
						　<span style="color: #01af74;cursor: pointer;" @click="updatesvideo(scope.row)"
							v-text="scope.row.userName"></span>
					</template>
				</el-table-column>
				<el-table-column prop="avatar" label="用户头像" width="100">
					<template slot-scope="scope">
						　　<img v-if="scope.row.avatar && scope.row.avatar!=''" :src="scope.row.avatar" width="40"
							height="40" />
						<span v-else>暂无图片</span>
					</template>
				</el-table-column>
				<el-table-column prop="evaluateMessage" label="评论内容">
				</el-table-column>
				<el-table-column prop="evaluateImg" label="评论图片" width="200">
					<template slot-scope="scope">
						<div v-if="scope.row.evaluateImg">
							<span v-for="(item,index) in scope.row.evaluateImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="item" alt="">
								</el-popover>
							</span>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="satisfactionFlag" label="评分" width="80">
					<template slot-scope="scope">
						<span style="color: red;cursor: pointer;" v-if="scope.row.satisfactionFlag==1">非常差</span>
						<span style="color: orange;cursor: pointer;" v-if="scope.row.satisfactionFlag==2">差</span>
						<span style="color: #999;cursor: pointer;" v-if="scope.row.satisfactionFlag==3">一般</span>
						<span style="color: #01af74;cursor: pointer;" v-if="scope.row.satisfactionFlag==4">满意</span>
						<span style="color: #01af74;cursor: pointer;" v-if="scope.row.satisfactionFlag==5">非常满意</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="评论时间" width="180">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
							@click="pinglundelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangePl" @current-change="handleCurrentChangePl"
					:page-sizes="[10, 20, 30, 40]" :page-size="size2" :current-page="page2"
					layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../locality/quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				dialogVisible: false,
				totalMoney: 0,
				limit: 10,
				page: 1,
				size1: 10,
				page1: 1,
				size2: 10,
				page2: 1,
				phone1: '',
				status1: '',
				describes: '',
				taskNum: '',
				taskMoney: '',
				statesnum: [{
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
				taskstates: [{
						label: '全部',
						value: 'all'
					},
					{
						label: '已接单',
						value: 1
					},
					{
						label: '已送达',
						value: 2
					}
				],
				sort: 10,
				id: '',
				btnChangeEnable: true,
				helpTakeId: '',
				helpMaintainId: '',
				content1: '',
				classifyIcon: '',
				classifyId: '',
				classifyUrl: '',
				classifyName: '',
				classifyDeatilsName: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading4: false,
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible7: false,
				tableData: {},
				tableData2: [],
				form: {
					id: '',
					classifyName: '',
					classifyUrl: '',
					classifyIcon: '',
					describes: ''
				},
				form2: {
					id: '',
					classifyDeatilsName: '',
					classifyUrl: '',
					classifyIcon: '',
					taskNum: '',
					taskMoney: '',
				},
				classifyData: [],
				checkBoxData: [], //多选框选择的值
				checkBoxData2: [], //多选框选择的值
				quillOption: quillConfig,
				options: regionData,
				storeAddress: [],
				storeAddre: '请选择城市',
				deliveryTime: '', //上门时间
				content: '', //服务的具体要求
				commission: '', //小费
				name: '', //地址姓名
				phone: '', //手机号
				detailsAddress: '', //地址详细
				latitude: '', //精度
				longitude: '', //维度
				province: '', //省
				city: '', //市
				district: '', //区
				serviceName: '', //服务类型
				userIdss: '', //用户id
				nickName1: '',
				phone: '',
				phone1: '',
				nickName2: '',
				userIds: {}, //用户列表
				titles: '添加',
				homeData1: [],
				gameId: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
				types: 2,
				gameIdsVal: [],
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
					},
					{
						label: '师傅任务',
						value: 4
					}
				],
				taskTypeT: '',
				orderNoT:'',
				dialogFormVisible1: false,
				orderId: '',
				conters: '',
				pinglunData: {},
				tableDataLoadingPl: false,
				dialogFormVisible10: false,
				renwuId: '',
				
				totalMoney: 0,
				totalMoneyS:0,
				totalMoneyTj:0,
				totalMoneyTy:0,
				totalMoneyP:0,

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
			updates(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			// 图标上传一级分类
			handleAvatarSuccess(file) {
				this.classifyIcon = file.data.path.path
			},
			// 图标上传一级分类编辑
			handleAvatarSuccess2(file) {
				this.form.classifyIcon = file.data.path
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 多选
			changeFun2(val) {
				this.checkBoxData2 = val
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleSizeChange3(val) {
				this.limit = val
				this.taskdataSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.taskdataSelect()
			},
			handleSizeChangePl(val) {
				this.size2 = val
				this.pinglunSelect(this.renwuId)
			},
			handleCurrentChangePl(val) {
				this.page2 = val
				this.pinglunSelect(this.renwuId)
			},
			// tabs切换
			handleClick(tab, event) {
				this.phone1 = ''
				this.gameId = ''
				this.status1 = 'all'
				if (tab._props.label == '派单管理') {
					this.page = 1
					this.limit = 10
					this.types = 1
					this.dataSelect()
				}
				if (tab._props.label == '接单管理') {
					this.page = 1
					this.limit = 10
					this.types = 2
					this.dataSelect()
				}
			},
			// 下架
			soldClick(row) {
				this.$confirm(`确定要下架此服务?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`help/outHelpOrder/?helpOrderId=${row.id}`),
						method: 'post',
						data: this.$http.adornData({})
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
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}

					})
				}).catch(() => {})
			},
			// 添加弹框
			classifyStair(index, row) {
				if (index == 1) {
					this.titles = '添加'
					this.deliveryTime = ''
					this.content = ''
					this.commission = ''
					this.name = ''
					this.phone = ''
					this.detailsAddress = ''
					this.latitude = ''
					this.longitude = ''
					this.province = ''
					this.city = ''
					this.district = ''
					this.serviceName = ''
					this.userIdss = ''
					this.nickName1 = ''
					this.id = ''
					this.storeAddress = []
					this.storeAddre = '请选择城市'

				} else {
					this.titles = '修改'
					this.id = row.id
					this.nickName1 = row.userName
					this.userIdss = row.userId
					this.deliveryTime = row.deliveryTime
					this.content = row.content
					this.commission = row.commission
					this.name = row.name
					this.phone = row.phone
					this.detailsAddress = row.detailsAddress
					this.latitude = row.latitude
					this.longitude = row.longitude
					this.province = row.province
					this.city = row.city
					this.district = row.district
					this.serviceName = row.serviceName
					this.storeAddre = this.province + '/' + this.city + '/' + this.district
				}
				this.homeSelect()
				this.getMyLocation()
				this.dialogFormVisible = true
			},
			// 添加确定
			StairNoticeTo() {
				if (this.userIdss == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择发布人',
						type: 'warning'
					})
					return
				}
				if (this.deliveryTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择期望送达时间',
						type: 'warning'
					})
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入具体要求',
						type: 'warning'
					})
					return
				}
				if (this.commission == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入佣金',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入姓名',
						type: 'warning'
					})
					return
				}
				if (this.phone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入电话',
						type: 'warning'
					})
					return
				}
				if (this.serviceName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入服务类型',
						type: 'warning'
					})
					return
				}
				if (this.province == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择省市区',
						type: 'warning'
					})
					return
				}
				if (this.detailsAddress == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入详细地址',
						type: 'warning'
					})
					return
				}
				if (this.titles == '添加') {
					var urls = 'help/saveHelpOrder'
				} else {
					var urls = 'help/updateHelpOrder'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					data: this.$http.adornData({
						'deliveryTime': this.deliveryTime,
						'content': this.content,
						'commission': this.commission,
						'name': this.name,
						'phone': this.phone,
						'detailsAddress': this.detailsAddress,
						'latitude': this.latitude,
						'longitude': this.longitude,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'gameId': this.serviceName,
						'userId': this.userIdss,
						'id': this.id
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '添加成功',
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
							onClose: () => {

							}
						})
					}

				})
			},
			// 修改一级分类
			compile(index, rows) {
				this.dialogFormVisible2 = true
				this.form.id = rows.id
				this.form.classifyName = rows.classifyName
				this.form.classifyUrl = rows.classifyUrl
				this.form.classifyIcon = rows.classifyIcon
				this.form.describes = rows.describes
			},
			// 修改一级分类确定
			CompileNoticeTo() {
				if (this.form.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				if (this.form.describes == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类描述',
						type: 'warning'
					})
					return
				}
				if (this.form.classifyIcon == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传分类图标',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('helpClassify/updateHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'classifyName': this.form.classifyName,
						'classifyUrl': this.form.classifyUrl,
						'classifyIcon': this.form.classifyIcon,
						'describes': this.form.describes
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible2 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {}
					})
				})
			},
			//删除一级
			deleteStair(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('help/deleteHelpOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'helpOrderId': delid
						})
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
				}).catch(() => {})
			},
			// 取消派单
			classifyQx(row) {
				let delid = row.id
				this.$confirm(`确定取消派单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('help/outHelpOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'helpOrderId': delid
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '取消成功',
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
				}).catch(() => {})
			},
			// 确认送达
			classifySd(row) {
				let delid = row.id
				this.$confirm(`确认已经送达?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('help/closeOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'helpOrderId': delid
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 手机号搜索
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.phone1 = ''
				this.status1 = ''
				this.startTime = ''
				this.endTime = ''
				this.gameIdsVal = []
				this.taskTypeT = ''
				this.gameId = ''
				 				this.orderNoT = ''
				this.taskdataSelect()
			},
			// 查询
			select1() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.phone1 = ''
				this.status1 = 'all'
				this.content1 = ''
				this.gameId = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			animeDat2(state) {
				this.page = 1
				this.taskdataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('admin/order/mission_order_list'),
					method: 'post',
					params: this.$http.adornParams({
						'page': this.page,
						'num': this.limit,
						'mobile': this.phone1,
						'status': this.status1,
						'content': this.content1,
						'game_id': this.gameId,
						'type': this.types,
						'str_time': this.startTime,
						'end_time': endTime,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 获取接单数据列表
			taskdataSelect() {
				this.totalMoney = 0
				this.totalMoneyS = 0
				this.totalMoneyTy = 0
				this.totalMoneyP = 0
				this.tableDataLoading = true
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('help/selectHelpTakeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone1,
						'status': this.status1,
						'gameId': this.gameId,
						'startTime': this.startTime,
						'endTime': endTime,
						'taskType': this.taskTypeT,
						'orderNo':this.orderNoT,
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
					let returnData = data.data
					this.tableData2 = returnData
					// for (var i in this.tableData2.list) {
					// 	this.tableData2.list[i].goodsPriceZ = 0
					// 	this.totalMoney = this.totalMoney + this.tableData2.list[i].money
						

					// }
					// this.totalMoney = this.totalMoney.toFixed(2)
				})
			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.region = value[1]
				this.district = value[2]
				// this.form.city = value[1]
				if (this.region == '市辖区') {
					this.region = this.province
				}
				// if (this.form.city == '市辖区') {
				// 	this.region = this.province
				// }
				// var regions = this.region
				// regions = regions.slice(0, regions.length - 1)
				// this.region = regions
				// console.log('regions', regions)
				console.log(this.$refs['cascaderAddr'].currentLabels)
			},
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("DSQBZ-5MM3P-HEODO-VG6IX-SBRJE-PSBNX", "省钱兄家政服务");
				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				// this.latitude = position.lat;
				// this.longitude = position.lng;
				// this.city = position.city;
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}

				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度

					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng.getLng() + '&key=5M6BZ-6YPLJ-AZRFX-D2XM3-UWTDT-SMF34&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response, response.result.address_component.city)
						that.detailsAddress = response.result.address
						that.city = response.result.address_component.city
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					if (!marker) {
						marker = new qq.maps.Marker({
							map: map,
							position: event.latLng
						});
					} else {
						marker.setPosition(event.latLng)
					}

					// markersArray.push(marker);

				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
			},
			// 地图定位
			select() {
				console.log(this.detailsAddress, this.detailsAddress.replace(/^\s+|\s+$/gm, ''))

				if (this.detailsAddress == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入详细地址',
						type: 'warning'
					});
					return
				} else {
					var add = this.province + this.city + this.district + this.detailsAddress
					let that = this
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + add +
						'&key=5M6BZ-6YPLJ-AZRFX-D2XM3-UWTDT-SMF34&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						// handle success
						if (response.message == '查询无结果') {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '详细地址输入有误，请重新输入',
								type: 'warning'
							});
							return
						}
						console.log('response', response)
						that.longitude = response.result.location.lng; // 经度
						that.latitude = response.result.location.lat; // 纬度
						that.city = response.result.address_components.city;
						// 	.city + response.result.address_components.district + response.result.title
						that.setMap()
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
				}

			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible3 = true
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				let phone = -1
				if (this.phone) {
					phone = this.phone
				}
				let nickName = -1
				if (this.nickName) {
					nickName = this.nickName
				}
				this.$http({
					url: this.$http.adornUrl('userCertification/queryUserCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'name': this.nickName2,
						'phone': this.phone1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.tableDataLoading4 = true

				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				this.phone = row.phone
				// if (this.nickName == '' || this.nickName == null) {
				// 	this.nickName = row.phone
				// }
				this.dialogFormVisible3 = false
			},
			// 获取服务分类启用列表
			homeSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/fixedClassify/getTreeClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'classifyName': ''
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.homeData1 = []
					if (data.data) {
						for (var i in data.data) {
							if (data.data[i].children.length > 0) {
								for (var j in data.data[i].children) {
									if (data.data[i].children[j].children.length == 0) {
										data.data[i].children[j].children = null
									}
								}
							}

							this.homeData1.push(data.data[i])
						}
					}
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
					url: this.$http.adornUrl('help/helpTakeListExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'phone': this.phone1,
						'status': this.status1,
						'gameId': this.gameId,
						'startTime': this.startTime,
						'endTime': endTime,
						'taskType': this.taskTypeT,
						 						'orderNo':this.orderNoT,
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
						elink.download = '接单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			handleChange2(value) {
				console.log('value', value)
				if (value.length == 2) {
					this.gameId = value[1];
				}
				this.taskdataSelect()

			},
			// 修改弹框
			rechargenone(row, index) {
				this.conters = ''
				this.orderId = row.helpOrderId
				this.types = index
				this.dialogFormVisible1 = true
			},
			StairNoticeTo1() {
				if (this.types == 1) {

					if (this.conters == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入备注信息',
							type: 'warning'
						})
						return
					}
					var url = 'help/updateTimeAndRemarks'
					var datas = {}
					datas.helpOrderId = this.orderId
					datas.remarks = this.conters
				}
				if (this.types == 2) {
					if (this.conters == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择预约时间',
							type: 'warning'
						})
						return
					}
					var url = 'help/updateTimeAndRemarks'
					var datas = {}
					datas.helpOrderId = this.orderId
					datas.deliveryTime = this.conters
				}

				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.taskdataSelect()
							}
						})
						this.dialogFormVisible1 = false
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 查看评论
			plCompile(row) {
				console.log(row)
				this.renwuId = row.indentNumber

				this.dialogFormVisible10 = true
				this.pinglunSelect(row.indentNumber)
			},
			// 用户评论列表
			pinglunSelect(indentNumber) {
				this.tableDataLoadingPl = true
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl('admin/evaluate/getUserEvaluate'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page2,
						'limit': this.size2,
						'indentNumber': indentNumber,
						'evaluateType': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingPl = false
					let returnData = data.data
					this.pinglunData = returnData


				})
			},
			// 删除评论信息
			pinglundelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/evaluate/deleteEvaluate'),
						method: 'get',
						params: this.$http.adornParams({
							'evaluateId': row.id
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.pinglunSelect(this.renwuId)
							}
						})
					})
				}).catch(() => {})
			},
		},
		mounted() {
			this.homeSelect()
			this.taskdataSelect()
		}
	}
</script>

<style lang="scss" scoped>
.dispatch-mission-page {
	padding: 0;
	overflow-x: hidden;
}

.dispatch-mission-page__hero {
	margin-bottom: 12px;
}

.dispatch-mission-page__filter {
	margin-bottom: 12px;
}

.dispatch-mission-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.dispatch-mission-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.dispatch-mission-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.dispatch-mission-page__input {
	width: 200px;
}

.dispatch-mission-page__select {
	width: 150px;
}

.dispatch-mission-page__cascader {
	width: 150px;
}

.dispatch-mission-page__date {
	width: 160px;
}

.dispatch-mission-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.dispatch-mission-page__export-tip {
	margin: 0;
	padding: 0 16px 12px;
	font-size: 12px;
	color: #e6a23c;
	line-height: 1.5;
}

.dispatch-mission-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.dispatch-mission-page__table-card >>> .el-table {
	border-radius: 0;
}

.dispatch-mission-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.dispatch-mission-page__table >>> .el-table__body td {
	background: #fff;
}

.dispatch-mission-page__link {
	color: #01af74;
	cursor: pointer;
}

.dispatch-mission-page__link:hover {
	color: #017c52;
}

.dispatch-mission-page__time {
	color: #e74c3c;
	font-weight: 600;
}

.dispatch-mission-page__text-btn {
	color: #01af74;
	background: #fff;
	border: none;
	padding: 0 4px;
}

.dispatch-mission-page__text-btn:hover {
	color: #017c52;
	background: #fff;
}

.dispatch-mission-page__status--active {
	color: #01af74;
}

.dispatch-mission-page__status--muted {
	color: #999;
}

.dispatch-mission-page__stats {
	display: flex;
	flex-wrap: wrap;
	gap: 16px;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
}

.dispatch-mission-page__stat-item em {
	font-style: normal;
	font-weight: 600;
	color: #01af74;
}

.dispatch-mission-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
