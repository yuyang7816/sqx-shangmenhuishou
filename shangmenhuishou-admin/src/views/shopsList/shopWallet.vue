<template>
	<div class="shop-wallet-page">
		<div class="shop-wallet-page__nav">
			<a href="#" class="shop-wallet-page__back-link" @click.prevent="prev">
				<icon-svg name="jiantou" class="shop-wallet-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="shop-wallet-page__nav-sep">|</span>
			<span class="shop-wallet-page__nav-title">商铺钱包</span>
		</div>

		<div class="nf-page-hero shop-wallet-page__hero">
			<span class="nf-page-hero__badge">商家管理</span>
			<h2 class="nf-page-hero__title">商铺钱包</h2>
			<p class="nf-page-hero__desc">查看商铺可提现与保证金余额，管理提现账号，以及钱包明细、提现记录与保证金流水</p>
		</div>

		<div class="shop-wallet-page__kpi-row">
			<div class="shop-wallet-page__kpi">
				<div class="shop-wallet-page__kpi-icon"><i class="el-icon-wallet"></i></div>
				<div class="shop-wallet-page__kpi-body">
					<div class="shop-wallet-page__kpi-label">可提现金额</div>
					<div class="shop-wallet-page__kpi-value">
						<span>{{ taskStat.money ? taskStat.money : 0 }}</span>元
					</div>
				</div>
			</div>
			<div class="shop-wallet-page__kpi">
				<div class="shop-wallet-page__kpi-icon"><i class="el-icon-s-finance"></i></div>
				<div class="shop-wallet-page__kpi-body">
					<div class="shop-wallet-page__kpi-label">保证金金额</div>
					<div class="shop-wallet-page__kpi-value">
						<span>{{ taskStat.safetyMoney ? taskStat.safetyMoney : 0 }}</span>元
					</div>
				</div>
			</div>
		</div>

		<div class="shop-wallet-page__tabs-wrap">
			<el-tabs v-model="activeName" class="shop-wallet-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="钱包明细" name="first">
					<div class="nf-table-card shop-wallet-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData2.list" class="shop-wallet-page__table">
							<el-table-column prop="id" label="编号" width="100">
							</el-table-column>
							<el-table-column prop="title" label="标题">
							</el-table-column>
							<el-table-column prop="content" label="内容">
							</el-table-column>
							<el-table-column prop="money" label="金额" width="100">
								<template slot-scope="scope">
									<span class="shop-wallet-page__amount--minus" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
									<span class="shop-wallet-page__amount--plus" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
								</template>
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="180">
							</el-table-column>
						</el-table>
						<div class="shop-wallet-page__pagination">
							<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
								:page-sizes="[10, 20, 30, 40,50,100]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="提现记录" name="second">
					<div class="nf-table-card shop-wallet-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData2.list" class="shop-wallet-page__table">
							<el-table-column fixed prop="id" label="编号" width="80">
							</el-table-column>
							<el-table-column prop="classify" label="提现方式" width="100">
								<template slot-scope="scope">
									<span>{{scope.row.classify==1 ? '支付宝提现' : '微信提现'}}</span>
								</template>
							</el-table-column>
							<el-table-column prop="zhifubao" label="支付宝账号" width="150">
								<template slot-scope="scope">
									<span class="shop-wallet-page__link"
										@click="updates(scope.row)">{{scope.row.zhifubao ? scope.row.zhifubao : '未绑定'}}</span>
								</template>
							</el-table-column>
							<el-table-column prop="zhifubaoName" label="支付宝名称" width="100">
								<template slot-scope="scope">
									<span>{{scope.row.zhifubaoName ? scope.row.zhifubaoName : '未绑定'}}</span>
								</template>
							</el-table-column>
							<el-table-column prop="money" label="提现金额" width="100">
							</el-table-column>
							<el-table-column prop="state" label="状态" width="100" fixed="right">
								<template slot-scope="scope">
									<span class="shop-wallet-page__link" v-if="scope.row.state == 0">待转账</span>
									<span v-if="scope.row.state == 1">已转账</span>
									<span v-if="scope.row.state == -1">已拒绝</span>
								</template>
							</el-table-column>
							<el-table-column prop="refund" label="拒绝原因" width="220">
							</el-table-column>
							<el-table-column prop="createAt" label="申请时间" width="170">
							</el-table-column>
							<el-table-column prop="outAt" label="转账/拒绝时间" width="180">
							</el-table-column>
							<el-table-column prop="orderNumber" label="转账订单号" width="150">
							</el-table-column>
						</el-table>
						<div class="shop-wallet-page__pagination">
							<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
								:page-sizes="[10, 20, 30, 40,50,100]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="提现账号" name="third">
					<div class="nf-table-card shop-wallet-page__form-card">
						<div class="shop-wallet-page__form-body">
							<div class="shop-wallet-page__form-row">
								<span class="shop-wallet-page__form-label">支付宝名称：</span>
								<el-input class="shop-wallet-page__form-input" @keydown.enter.native="shopSelect"
									placeholder="请输入支付宝名称" v-model="tixianData.zhiFuBaoName">
								</el-input>
							</div>
							<div class="shop-wallet-page__form-row">
								<span class="shop-wallet-page__form-label">支付宝账号：</span>
								<el-input class="shop-wallet-page__form-input" @keydown.enter.native="select" clearable
									placeholder="请输入支付宝账号" v-model="tixianData.zhiFuBao">
								</el-input>
							</div>
							<div class="shop-wallet-page__form-row shop-wallet-page__form-row--top">
								<span class="shop-wallet-page__form-label">提现二维码：</span>
								<div class="shop-wallet-page__upload-box">
									<el-upload class="avatar-uploader" v-model="tixianData.wxImg"
										:action="$http.adornUrl('file/upload')" :show-file-list="false"
										:on-success="handleAvatarSuccess1">
										<img v-if="tixianData.wxImg" :src="tixianData.wxImg"
											class="shop-wallet-page__avatar" />
										<i v-else class="el-icon-plus avatar-uploader-icon shop-wallet-page__upload-icon"></i>
									</el-upload>
								</div>
							</div>
							<div class="shop-wallet-page__form-actions">
								<el-button type="primary" icon="document" @click="querenBtn">确认</el-button>
							</div>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="保证金明细" name="baozhengjin">
					<div class="nf-table-card shop-wallet-page__table-card">
						<el-table v-loading="tableDataLoading" :data="walletData.records" class="shop-wallet-page__table">
							<el-table-column prop="id" label="编号" width="100"></el-table-column>
							<el-table-column prop="title" label="标题"></el-table-column>
							<el-table-column prop="content" label="内容"></el-table-column>
							<el-table-column prop="money" label="金额" width="100">
								<template slot-scope="scope">
									<span class="shop-wallet-page__amount--minus" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
									<span class="shop-wallet-page__amount--plus" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
								</template>
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
						</el-table>
						<div class="shop-wallet-page__pagination">
							<el-pagination @size-change="handleSizeChangeBzj" @current-change="handleCurrentChangeBzj"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="walletData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 任务拒绝弹框 -->
		<el-dialog title="任务拒绝" :visible.sync="dialogFormVisible7" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入拒绝理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible7 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!--    图片展示-->
		<el-dialog title="图片" :visible.sync="dialogVisible" width="35%" :before-close="handleClose">
			<span v-if="imageUrl.length>0" v-for="(item, index) in imageUrl" :key="index" style="width: 100px;
        height: 100px;display:inline-block;">
				<img :src="item" alt="" style="width: 100px; height: 100px">
			</span>
			<span v-else>暂无图片</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				dialogVisible: false,
				imageUrl: [],
				limit: 10,
				page: 1,
				phone: '',
				status: '',
				describes: '',
				token: '',
				taskNum: '',
				taskMoney: '',
				statesnum: [{
						label: '全部',
						value: 0
					},
					{
						label: '待接单',
						value: 2
					},
					{
						label: '待送达',
						value: 3
					},
					{
						label: '已完成',
						value: 4
					},
					{
						label: '已下架',
						value: 5
					},
				],
				taskstates: [{
						label: '全部',
						value: ''
					},
					{
						label: '待结算',
						value: 0
					},
					{
						label: '待支付',
						value: 1
					},
					{
						label: '待取餐', //待取餐、派送中
						value: 3
					},
					{
						label: '已完成',
						value: 4
					},
					{
						label: '已取消',
						value: 5
					}
				],
				shopId: '',
				shopList: '',
				sort: 10,
				id: '',
				btnChangeEnable: true,
				helpTakeId: '',
				helpMaintainId: '',
				content: '',
				classifyIcon: '',
				classifyId: '',
				classifyUrl: '',
				classifyName: '',
				classifyDeatilsName: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				tableData: [],
				tableData2: {},
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
				protectjieData: [],
				protectpaiData: [],
				helpTaskData: [],
				safeguardData: [],
				classifyData: [],
				checkBoxData: [], //多选框选择的值
				checkBoxData2: [], //多选框选择的值
				userName: '',
				taskStat: {},
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				flag: 'day',
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
				tixianData: {},
				walletData:{},
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime1() {
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
			getNowTime2() {
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
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			timeDate() {
				this.classifySelect()
				this.taskdataSelect()
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
			// 订单分析选择日期
			animeOrder() {
				this.classifySelect()
				this.taskdataSelect()
			},
			// 订单分析年月日
			orderfenxi(value) {
				let vanumber = value
				if (vanumber === 'day') {
					this.flag = 'day'
					this.classifySelect()
					this.taskdataSelect()
				}
				if (vanumber === 'month') {
					this.flag = 'month'
					this.classifySelect()
					this.taskdataSelect()
				}
				if (vanumber === 'year') {
					this.flag = 'year'
					this.classifySelect()
					this.taskdataSelect()
				}
			},
			// 图标上传一级分类
			handleAvatarSuccess(file) {
				this.classifyIcon = file.data.path
			},
			// 图标上传一级分类编辑
			handleAvatarSuccess2(file) {
				this.form.classifyIcon = file.data.path
			},
			// 提现二维码
			handleAvatarSuccess1(file, fileList) {
				this.tixianData.wxImg = file.data.path
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
				this.limit = val
				this.classifySelect()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.classifySelect()
			},
			handleSizeChange3(val) {
				this.limit = val
				this.taskdataSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.taskdataSelect()
			},
			handleSizeChangeBzj(val) {
				this.limit = val
				this.baozhengjinSelect()
			},
			handleCurrentChangeBzj(val) {
				this.page = val
				this.baozhengjinSelect()
			},
			// tabs切换
			handleClick(tab, event) {

				if (tab._props.label == '钱包明细') {
					this.page = 1
					this.limit = 10
					this.taskdataSelect()
				}
				if (tab._props.label == '提现记录') {
					this.page = 1
					this.limit = 10
					this.taskdataSelectTx()
				}
				if (tab._props.label == '提现账号') {
					this.taskdataSelectZhb()
				}
				if (tab._props.label == '保证金明细') {
					this.limit = 10
					this.page = 1
					this.baozhengjinSelect()
				}
				

			},
			// 下架
			soldClick(row) {
				this.$confirm(`确定要下架此任务?`, '提示', {
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
			//查看照片
			refund(e) {
				this.imageUrl = []
				if (e.image != null) {
					let img = e.image.split(',')
					if (img.length != 0) {
						this.imageUrl = img
					}
				}
				this.dialogVisible = true
			},
			handleClose(done) {
				done()
			},
			// 添加一级分类弹框
			classifyStair() {
				this.dialogFormVisible = true
			},
			// 添加一级分类确定
			StairNoticeTo() {
				if (this.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				if (this.describes == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类描述',
						type: 'warning'
					})
					return
				}
				if (this.classifyIcon == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传分类图标',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('helpClassify/saveHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'classifyName': this.classifyName,
						'state': this.state,
						'sort': this.sort,
						'classifyIcon': this.classifyIcon,
						'classifyUrl': this.classifyUrl,
						'describes': this.describes
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '一级分类添加成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classifyUrl = ''
							this.classifyName = ''
							this.classifyIcon = ''
							this.describes = ''
							this.classifySelect()
						}
					})
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
						onClose: () => {
							this.classifySelect()
						}
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
						url: this.$http.adornUrl(`helpClassify/deleteClassifyById/?id=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = ''
								this.classifySelect()
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
				this.phone = ''
				this.status = ''
				this.userName = ''
				this.page = 1
				this.taskdataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.phone = ''
				this.status = 0
				this.content = ''
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
				this.$http({
					url: this.$http.adornUrl('help/selectStatusHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone,
						'status': this.status,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 获取钱包明细数据列表
			taskdataSelect() {
				this.tableDataLoading = true
				this.shopId = this.$route.query.shopId
				this.$http({
					url: this.$http.adornUrl('userMoney/balanceDetailed'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': this.shopId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
					for (var i in this.tableData2.list) {
						if (this.tableData2.list[i].address) {
							this.tableData2.list[i].address = JSON.parse(this.tableData2.list[i].address)
						}
					}
				})
			},
			taskdataSelectTx() {
				this.tableDataLoading = true
				this.shopId = this.$route.query.shopId
				this.$http({
					url: this.$http.adornUrl('cash/selectShopPayDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': this.shopId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
					for (var i in this.tableData2.list) {
						if (this.tableData2.list[i].address) {
							this.tableData2.list[i].address = JSON.parse(this.tableData2.list[i].address)
						}
					}
				})
			},
			// 商铺统计
			classifySelect() {
				this.$http({
					url: this.$http.adornUrl('userMoney/selectMyMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': this.shopId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.taskStat = returnData
				})
			},
			// 餐品完成
			wancheng(row) {
				this.$http({
					url: this.$http.adornUrl('admin/order/updateOrder'),
					method: 'post',
					data: this.$http.adornData({
						'status': 4,
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
			// 支付宝信息
			taskdataSelectZhb() {
				this.tableDataLoading = true
				this.shopId = this.$route.query.shopId
				this.$http({
					url: this.$http.adornUrl(`user/${this.shopId}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data.userEntity
					this.tixianData = returnData

				})
			},
			querenBtn() {
				if (this.tixianData.zhiFuBaoName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入支付宝名称',
						type: 'warning'
					})
					return
				}
				if (this.tixianData.zhiFuBao == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入支付宝账号',
						type: 'warning'
					})
					return
				}

				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'zhiFuBao': this.tixianData.zhiFuBao,
						'zhiFuBaoName': this.tixianData.zhiFuBaoName,
						'userId': this.shopId,
						'wxImg': this.tixianData.wxImg,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {

							this.taskdataSelectZhb()
						}
					})
				})
			},
			// 获取保证金明细
			baozhengjinSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('userMoney/getBondDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': this.shopId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.walletData = returnData
				})
			},
		},
		mounted() {
			this.shopId = this.$route.query.shopId
			this.classifySelect()
			this.taskdataSelect()

		},
		watch: {
			'$route': 'taskdataSelect'

		}
	}
</script>

<style lang="scss" scoped>
.shop-wallet-page {
	padding: 0;
	overflow-x: hidden;
}

.shop-wallet-page__nav {
	display: flex;
	align-items: center;
	margin-bottom: 12px;
	font-size: 14px;
	color: var(--nf-text-primary, #1a2433);
}

.shop-wallet-page__back-link {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	color: var(--nf-brand, #01af74);
	text-decoration: none;
	cursor: pointer;
}

.shop-wallet-page__back-link:hover {
	color: var(--nf-brand-deep, #017c52);
}

.shop-wallet-page__back-icon {
	width: 1.2em;
	height: 1.2em;
}

.shop-wallet-page__nav-sep {
	margin: 0 12px;
	color: var(--nf-border, #e8eaed);
}

.shop-wallet-page__nav-title {
	font-size: 16px;
	font-weight: 600;
}

.shop-wallet-page__hero {
	margin-bottom: 12px;
}

.shop-wallet-page__kpi-row {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 12px;
	margin-bottom: 12px;
}

.shop-wallet-page__kpi {
	display: flex;
	align-items: center;
	gap: 12px;
	padding: 16px;
	background: var(--nf-card-bg, #fff);
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-md, 8px);
	box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
}

.shop-wallet-page__kpi-icon {
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

.shop-wallet-page__kpi-body {
	min-width: 0;
}

.shop-wallet-page__kpi-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
	margin-bottom: 4px;
}

.shop-wallet-page__kpi-value {
	font-size: 13px;
	color: var(--nf-text-primary, #1a2433);
}

.shop-wallet-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: var(--nf-brand, #01af74);
	margin-right: 4px;
}

.shop-wallet-page__tabs-wrap {
	margin-bottom: 0;
}

.shop-wallet-page__tabs {
	margin-bottom: 0;
}

.shop-wallet-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.shop-wallet-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.shop-wallet-page__table-card >>> .el-table {
	border-radius: 0;
}

.shop-wallet-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.shop-wallet-page__table >>> .el-table__body td {
	background: #fff;
}

.shop-wallet-page__link {
	color: var(--nf-brand, #01af74);
	cursor: pointer;
}

.shop-wallet-page__link:hover {
	color: var(--nf-brand-deep, #017c52);
}

.shop-wallet-page__amount--minus {
	color: #f56c6c;
}

.shop-wallet-page__amount--plus {
	color: #017c52;
}

.shop-wallet-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.shop-wallet-page__form-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 16px;
	overflow: hidden;
}

.shop-wallet-page__form-body {
	display: flex;
	flex-direction: column;
	gap: 12px;
}

.shop-wallet-page__form-row {
	display: flex;
	align-items: center;
	gap: 12px;
}

.shop-wallet-page__form-row--top {
	align-items: flex-start;
}

.shop-wallet-page__form-label {
	flex: 0 0 120px;
	text-align: right;
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	line-height: 32px;
}

.shop-wallet-page__form-row--top .shop-wallet-page__form-label {
	line-height: 1.5;
	padding-top: 6px;
}

.shop-wallet-page__form-input {
	width: 200px;
}

.shop-wallet-page__upload-box {
	width: 148px;
	height: 148px;
	background-color: #fbfdff;
	border: 1px dashed var(--nf-border, #c0ccda);
	border-radius: var(--nf-radius-sm, 6px);
	text-align: center;
	line-height: 148px;
	overflow: hidden;
}

.shop-wallet-page__avatar {
	width: 148px;
	height: 148px;
	display: block;
}

.shop-wallet-page__upload-icon {
	font-size: 28px;
	color: #8c939d;
}

.shop-wallet-page__form-actions {
	padding-top: 12px;
	padding-left: 132px;
}

@media (max-width: 960px) {
	.shop-wallet-page__kpi-row {
		grid-template-columns: 1fr;
	}
}
</style>
