<template>
	<div class="mission-page">
		<div class="nf-page-hero mission-page__hero">
			<span class="nf-page-hero__badge">服务管理</span>
			<h2 class="nf-page-hero__title">服务列表</h2>
			<p class="nf-page-hero__desc">管理派单、接单与分类信息，支持按片区、状态、手机号与内容筛选，可添加服务与维护分类</p>
		</div>

		<div class="mission-page__tabs-wrap">
			<el-tabs v-model="activeName" class="mission-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="派单管理" name="first">
					<div class="nf-filter-card mission-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-search"></i>
							<span>筛选条件</span>
						</div>
						<div class="nf-filter-card__body mission-page__filter-body">
							<div class="mission-page__field">
								<span class="mission-page__label">片区筛选：</span>
								<el-select v-model="campus" class="mission-page__select" @change="animeDat(campus)">
									<el-option v-for="item in homeData1" :key="item.campusId" :label="item.campusName"
										:value="item.campusId">
									</el-option>
								</el-select>
							</div>
							<div class="mission-page__field">
								<span class="mission-page__label">状态：</span>
								<el-select v-model="status" class="mission-page__select" @change="animeDat(status)">
									<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
									</el-option>
								</el-select>
							</div>
							<div class="mission-page__field">
								<span class="mission-page__label">手机号：</span>
								<el-input class="mission-page__input" @keydown.enter.native="select" placeholder="请输入手机号" v-model="phone">
								</el-input>
							</div>
							<div class="mission-page__field">
								<span class="mission-page__label">内容：</span>
								<el-input class="mission-page__input" @keydown.enter.native="select" placeholder="请输入内容"
									v-model="content"></el-input>
							</div>
							<div class="mission-page__actions">
								<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
								<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
								<el-button size="mini" type="primary" icon="el-icon-plus" @click="add">添加</el-button>
							</div>
						</div>
					</div>
					<div class="nf-table-card mission-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.list" class="mission-page__table">
							<el-table-column prop="id" label="编号"></el-table-column>
							<el-table-column prop="orderNo" label="订单编号" width="180"></el-table-column>
							<el-table-column prop="content" label="内容" width="200"></el-table-column>
							<el-table-column prop="phone" label="电话" width="150"></el-table-column>
							<el-table-column prop="site" label="地址" width="160">
								<template slot-scope="scope">
									<span>{{scope.row.site}}{{scope.row.address}}</span>
								</template>
							</el-table-column>
							<el-table-column prop="deliveryTime" label="期望送达时间" width="160"></el-table-column>
							<el-table-column prop="userType" label="跑腿员分类" width="160">
								<template slot-scope="scope">
									<span v-if="scope.row.userType == 0">不限</span>
									<span v-if="scope.row.userType == 1">限男生</span>
									<span v-if="scope.row.userType == 2">限女生</span>
								</template>
							</el-table-column>
							<el-table-column prop="commission" label="佣金" width="160"></el-table-column>
							<el-table-column prop="code" label="确认码" width="160"></el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
							<el-table-column prop="campus" label="片区" width="160"></el-table-column>
							<el-table-column fixed="right" prop="status" label="状态" width="100">
								<template slot-scope="scope">
									<span class="mission-page__status" v-if="scope.row.status === 2 ">待接单</span>
									<span class="mission-page__status" v-if="scope.row.status === 3 ">进行中</span>
									<span class="mission-page__status" v-if="scope.row.status === 4 ">已完成</span>
									<span v-if="scope.row.status === 5 ">已取消</span>
								</template>
							</el-table-column>
							<el-table-column fixed="right" label="操作" width="180">
								<template slot-scope="scope">
									<el-button v-if="scope.row.status === 2 || scope.row.status === 3" size="mini" type="primary"
										:disabled="!isAuth('mission:sold')" @click="soldClick(scope.row)">下架
									</el-button>
									<el-button v-else size="mini" type="primary" disabled>下架</el-button>
									<el-button size="mini" type="primary" :disabled="!isAuth('financeList:refund')"
										@click="refund(scope.row)">查看照片</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="mission-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="接单管理" name="fourthly">
					<div class="nf-filter-card mission-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-search"></i>
							<span>筛选条件</span>
						</div>
						<div class="nf-filter-card__body mission-page__filter-body">
							<div class="mission-page__field">
								<span class="mission-page__label">片区筛选：</span>
								<el-select v-model="campus" class="mission-page__select" @change="animeDat2(campus)">
									<el-option v-for="item in homeData1" :key="item.campusId" :label="item.campusName"
										:value="item.campusId">
									</el-option>
								</el-select>
							</div>
							<div class="mission-page__field">
								<span class="mission-page__label">状态：</span>
								<el-select v-model="status" class="mission-page__select" @change="animeDat2(status)">
									<el-option v-for="item in taskstates" :key="item.value" :label="item.label" :value="item.value">
									</el-option>
								</el-select>
							</div>
							<div class="mission-page__field">
								<span class="mission-page__label">手机号：</span>
								<el-input class="mission-page__input" @keydown.enter.native="phoneSelect" placeholder="请输入手机号"
									v-model="phone"></el-input>
							</div>
							<div class="mission-page__actions">
								<el-button size="mini" type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
								<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
							</div>
						</div>
					</div>
					<div class="nf-table-card mission-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData2.list" class="mission-page__table">
							<el-table-column fixed prop="nickName" label="用户姓名">
								<template slot-scope="scope">
									<span class="mission-page__link" @click="updates(scope.row)">
										{{ scope.row.nickName ? scope.row.nickName : '未绑定' }}
									</span>
								</template>
							</el-table-column>
							<el-table-column prop="phone" label="派单人手机号"></el-table-column>
							<el-table-column prop="site" label="服务地址">
								<template slot-scope="scope">
									<span>{{scope.row.site}}{{scope.row.address}}</span>
								</template>
							</el-table-column>
							<el-table-column prop="content" label="服务内容"></el-table-column>
							<el-table-column prop="distances" label="距离"></el-table-column>
							<el-table-column prop="money" label="接单价格" width="100"></el-table-column>
							<el-table-column prop="deliveryTime" label="期望送达时间"></el-table-column>
							<el-table-column fixed="right" prop="status" label="状态" width="100">
								<template slot-scope="scope">
									<span class="mission-page__status" v-if="scope.row.status === 0 ">待确认</span>
									<span class="mission-page__status" v-if="scope.row.status === 1 ">已接单</span>
									<span class="mission-page__status" v-if="scope.row.status === 2 ">已送达</span>
								</template>
							</el-table-column>
						</el-table>
						<div class="mission-page__pagination">
							<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData2.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="分类管理" name="third">
					<div class="nf-filter-card mission-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-s-operation"></i>
							<span>快捷操作</span>
						</div>
						<div class="nf-filter-card__body mission-page__filter-body">
							<div class="mission-page__actions">
								<el-button :disabled="!isAuth('mission:add')" size="mini" type="primary"
									icon="el-icon-plus" @click="classifyStair()">添加分类</el-button>
							</div>
						</div>
					</div>
					<div class="nf-table-card mission-page__table-card">
						<el-table v-loading="tableDataLoading" :data="classifyData" class="mission-page__table">
							<el-table-column prop="classifyName" label="名称">
							</el-table-column>
							<el-table-column prop="classifyIcon" label="图标">
								<template slot-scope="scope">
									<img :src="scope.row.classifyIcon" width="40" height="40" />
								</template>
							</el-table-column>
							<el-table-column prop="describes" label="描述">
							</el-table-column>
							<el-table-column label="操作" width="150">
								<template slot-scope="scope">
									<el-button size="mini" type="danger" :disabled="!isAuth('mission:delete')"
										@click="deleteStair(scope.row)">删除
									</el-button>
									<el-button size="mini" type="primary" :disabled="!isAuth('mission:update')"
										@click="compile(scope.$index, scope.row)">编辑
									</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="mission-page__pagination">
							<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="classifyData.length">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 添加分类 -->
		<el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
				<el-input style="width:50%;" v-model="classifyName" type="text" placeholder="请输入分类名称"></el-input>
			</div>
		<!-- <div style="margin-bottom: 10px;">
		<span style="width: 200px;display: inline-block;text-align: right;">分类链接：</span>
		<el-input style="width:50%;" v-model="classifyUrl" type="text" placeholder="请输入分类链接"></el-input>
	  </div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类描述：</span>
				<el-input style="width:50%;" v-model="describes" type="text" placeholder="请输入分类描述"></el-input>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类图标：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="classifyIcon"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess">
						<img v-if="classifyIcon" :src="classifyIcon" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改分类 -->
		<el-dialog title="编辑分类" :visible.sync="dialogFormVisible2" center>
			<el-form :model="form">
				<el-form-item label="分类名称：" :label-width="formLabelWidth">
					<el-input v-model="form.classifyName" style="width:65%;"></el-input>
				</el-form-item>
				<!-- <el-form-item label="分类链接：" :label-width="formLabelWidth" >
		  <el-input v-model="form.classifyUrl" style="width:65%;"></el-input>
		</el-form-item> -->
				<el-form-item label="分类描述：" :label-width="formLabelWidth">
					<el-input v-model="form.describes" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="分类图标：" :label-width="formLabelWidth">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="classifyIcon"
							:action="$http.adornUrl('file/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="form.classifyIcon" :src="form.classifyIcon" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="CompileNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
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
		<!--  图片展示-->
		<el-dialog title="图片" :visible.sync="dialogVisible" width="35%" :before-close="handleClose">
			<div v-if="imageUrl.length>0">
				<span v-for="(item, index) in imageUrl" :key="index" style="width: 100px;
			      height: 100px;display:inline-block;">
					<!--  <el-image style = "width: 100px; height: 100px" :src = "item" :preview-src-list="imageUrl"></el-image>-->
					<img :src="item" alt="" style="width: 100px; height: 100px">
				</span>
			</div>
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
				status: 0,
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
						label: '进行中',
						value: 3
					},
					{
						label: '已完成',
						value: 4
					},
					{
						label: '已取消',
						value: 5
					},
				],
				taskstates: [{
						label: '全部',
						value: 0
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
				protectjieData: [],
				protectpaiData: [],
				helpTaskData: [],
				safeguardData: [],
				classifyData: [],
				checkBoxData: [], //多选框选择的值
				checkBoxData2: [], //多选框选择的值
				campus: '',
				homeData1: [],
			}
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
			// 图标上传分类
			handleAvatarSuccess(file) {
				this.classifyIcon = file.data.path.path
			},
			// 图标上传分类编辑
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
			// tabs切换
			handleClick(tab, event) {
				this.campus = ''
				this.status = 0
				if (tab._props.label == '派单管理') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '接单管理') {
					this.page = 1
					this.limit = 10
					this.taskdataSelect()
				}
				if (tab._props.label == '分类管理') {
					this.page = 1
					this.limit = 10
					this.classifySelect()
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
			//查看照片
			refund(e) {
				this.imageUrl = []
				if (e.image != null && e.image != '') {
					let img = e.image.split(',')
					if (img.length != 0) {
						this.imageUrl = img
					}
				}
				this.dialogVisible = true
			},
			handleClose(done) {
				// this.$confirm('确认关闭？')
				//   .then(_ => {
				done()
				// })
				// .catch(_ => {});
			},
			// 添加分类弹框
			classifyStair() {
				this.dialogFormVisible = true
			},
			// 添加分类确定
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
				// if (this.classifyUrl == '') {
				//     this.$notify({
				//         title: '提示',
				//         duration: 1800,
				//         message: '请输入分类链接',
				//         type: 'warning'
				//     });
				//     return
				// }
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
						message: '分类添加成功',
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
			// 修改分类
			compile(index, rows) {
				this.dialogFormVisible2 = true
				this.form.id = rows.id
				this.form.classifyName = rows.classifyName
				this.form.classifyUrl = rows.classifyUrl
				this.form.classifyIcon = rows.classifyIcon
				this.form.describes = rows.describes
			},
			// 修改分类确定
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
			//删除分类
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
				this.status = 0
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
				this.phone = ''
				this.status = 0
				this.content = ''
				this.dataSelect()
			},
			//添加
			add() {
				this.$router.push({
					path: '/missionAdd',
					query: {

					}
				})
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			animeDat2(state) {
				this.taskdataSelect()
			},
			// 获取片区数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpCampus/selectCampusList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData1 = returnData
				})
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
						'content': this.content,
						'campus': this.campus
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
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('help/selectHelpTakeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone,
						'status': this.status,
						'campus': this.campus
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
					for (var i in this.tableData2.list) {
						if (this.tableData2.list[i].distance) {
							this.tableData2.list[i].distances = Math.floor(this.tableData2.list[i].distance)
							if (this.tableData2.list[i].distance > 1000) {
								this.tableData2.list[i].distances = ((Math.floor(this.tableData2.list[i]
										.distance) / 1000)
									.toFixed(2)) + 'km'
							} else {
								this.tableData2.list[i].distances = (Math.floor(this.tableData2.list[i]
									.distance)) + 'm'
							}
						}
					}
				})
			},
			// 分类管理
			classifySelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpClassify/select'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.classifyData = returnData
				})
			},
		},
		mounted() {
			this.homeSelect()
			this.dataSelect()
		}
	}
</script>

<style lang="scss" scoped>
.mission-page {
	padding: 0;
	overflow-x: hidden;
}

.mission-page__hero {
	margin-bottom: 12px;
}

.mission-page__filter {
	margin-bottom: 12px;
}

.mission-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.mission-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.mission-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.mission-page__select,
.mission-page__input {
	width: 150px;
}

.mission-page__input {
	width: 200px;
}

.mission-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.mission-page__tabs-wrap {
	margin-bottom: 0;
}

.mission-page__tabs {
	margin-bottom: 0;
}

.mission-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.mission-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.mission-page__table-card >>> .el-table {
	border-radius: 0;
}

.mission-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.mission-page__table >>> .el-table__body td {
	background: #fff;
}

.mission-page__status {
	color: #01af74;
}

.mission-page__link {
	color: #01af74;
	cursor: pointer;
}

.mission-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
