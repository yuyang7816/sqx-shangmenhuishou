<template>
	<div class="classification-page">
		<div class="nf-page-hero classification-page__hero">
			<span class="nf-page-hero__badge">废品管理</span>
			<h2 class="nf-page-hero__title">废品分类</h2>
			<p class="nf-page-hero__desc">管理一级与二级废品分类，支持分类图片、二级分类及价格规则配置</p>
		</div>

		<div class="nf-filter-card classification-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body classification-page__filter-body">
				<el-button size="mini" type="primary" icon="el-icon-refresh" plain @click="select()">刷新</el-button>
			</div>
		</div>

		<div class="nf-table-card classification-page__table-card">
			<div class="classification-page__toolbar">
				<el-button size="mini" type="primary" icon="document"
					:disabled="!isAuth('classification:add')" @click="classAdd(0)">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.records" class="classification-page__table">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="name" label="分类名称">
				</el-table-column>
				<el-table-column prop="cover" label="分类图片">
					<template slot-scope="scope">
						<img v-if="scope.row.cover && scope.row.cover != ''" :src="scope.row.cover" width="40" height="40"
							class="classification-page__thumb" />
						<span v-else class="classification-page__no-img">暂无图片</span>
					</template>
				</el-table-column>
				<el-table-column prop="appointmentNum" label="预约次数">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" plain
							@click="classErList(scope.row)">二级分类
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('classification:update')"
							@click="classAdd(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('classification:delete')"
							@click="classdelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="classification-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</div>

		<!-- 添加、修改服务分类 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible9" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类类型：</span>
				<el-select v-model="parentIdnum" placeholder="请选择分类类型" style="width:50%;" @change="parentIdnumBtn">
					<el-option v-for="item in classnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;" v-if="parentIdnum==2">
				<span style="width: 200px;display: inline-block;text-align: right;">上级分类：</span>
				<el-select v-model="parentId" style="width:50%;">
					<el-option v-for="item in classDatas" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
				<el-input style="width:50%;" v-model="name" type="text" placeholder="请输入分类名称"></el-input>
			</div>
			
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="img"
						:action="$http.adornUrlUp('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1" :on-progress="onprogress2">
						<!-- <el-progress type="circle" :percentage="100" status="success"></el-progress> -->
						<img v-if="img!=''" :src="img" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>

					</el-upload>
				</div>
			</div>
			
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible9 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto1()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="二级分类" :visible.sync="dialogFormVisible1" center width="80%" :before-close="handleClose">
			<el-table v-loading="tableDataLoading1" :data="classDatasEr">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="name" label="分类名称">
				</el-table-column>
				<el-table-column prop="cover" label="分类图片">
					<template slot-scope="scope">
						　<img v-if="scope.row.cover &&scope.row.cover!=''" :src="scope.row.cover" width="40" height="40" />
						<span v-else>暂无图片</span>
					</template>
				</el-table-column>
				<el-table-column prop="appointmentNum" label="预约次数">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" style="margin: 3px;" plain
							@click="classJgList(scope.row)">价格配置
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('classification:update')" style="margin: 3px;"
							@click="classAdd(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('classification:delete')" style="margin: 3px;"
							@click="classdelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-dialog>
		<el-dialog title="价格配置" :visible.sync="dialogFormVisible2" center width="75%">
			<div style="text-align: right;margin-right: 20px;">
				<el-button size="mini" type="primary" :disabled="!isAuth('classification:update')"
					@click="classJgAdd()">添加
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading2" :data="tableDataJg">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="unit" label="规则(单位/数量)">
				</el-table-column>
				<el-table-column prop="price" label="价格">
				</el-table-column>
				<el-table-column prop="sort" label="排序序号">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('classification:update')"
							@click="classJgAdd(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('classification:delete')"
							@click="classJgdelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-dialog>
		<!-- 添加、修改服务分类 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible3" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">规则(单位/数量)：</span>
				<el-input style="width:50%;" v-model="name" type="text" placeholder="请输入规则(单位/数量)，如：5-10kg/1件"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">价格：</span>
				<el-input style="width:50%;" v-model="money" type="number" min="0" placeholder="请输入价格"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" min="0" placeholder="请输入排序"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto2()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {

		data() {
			return {
				page: 1,
				limit: 10,
				openValue2: 1,
				closeValue2: 2,
				tableDataLoading: true,
				dialogFormVisible9: false,
				tableData: {},
				titles: '添加服务分类',
				name: '',
				classifyId: '',
				img: '',
				gameStatus: '',
				parentIdnum: '',
				classnum: [{
						value: 1,
						label: '一级分类'
					},
					{
						value: 2,
						label: '二级分类'
					}
				],
				classDatas: [],
				parentId: '',
				money: '',
				minOfferMoney: '',
				minQuoteMoney: '',
				classifyNames:'',
				minMasterMoney:'',
				sort:'',
				dialogFormVisible1:false,
				tableDataLoading1:false,
				classDatasEr:[],
				tableDataLoading2:false,
				dialogFormVisible2:false,
				dialogFormVisible3:false,
				tableDataJg:[],
				id:'',
				page2:1,
				limit2:10,
				
			}
		},
		methods: {
			// 刷新
			select() {
				this.page = 1
				this.classSelect()
			},

			handleSizeChange(val) {
				this.limit = val
				this.classSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.classSelect()
			},

			// 封面图片上传
			handleAvatarSuccess1(file) {
				this.img = file.data.path;
			},
			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				// this.percentage2 = parseInt(event.percent)
			},

			// 获取服务分类数据
			classSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/waste/category/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'parentId':0,
						'level':'',
						'name':'',
					})
				}).then(({
					data
				}) => {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData

				})
			},
			// 添加服务分类
			classAdd(row) {
				this.classSelectList()
				console.log('row', row)
				if (row != 0) {
					this.titles = '修改分类'
					this.name = row.name
					this.classifyId = row.id
					this.img = row.cover

					if (row.parentId == 0) {
						this.parentIdnum = 1
						
					} else {
						this.parentIdnum = 2
					}

					this.parentId = row.parentId
				} else {
					this.titles = '添加分类'
					this.name = ''
					this.classifyId = ''
					this.img = ''
					this.parentIdnum = ''
					this.parentId = ''
				}
				this.dialogFormVisible9 = true

			},
			// 启用与否
			change(id, status) {
				this.$http({
					url: this.$http.adornUrl('admin/fixedClassify/updateClassify'),
					method: 'post',
					params: this.$http.adornParams({
						'classifyId': id,
						'isEnable': status
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
								this.classSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.classSelect()
							}
						})
					}

				})
			},
			// 提交修改、添加分类
			refuseto1() {
				if (this.parentIdnum == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入选择分类类型',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				if (this.img == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传分类图片',
						type: 'warning'
					})
					return img
				}
				

				if (this.titles == '添加分类') {
					var urls = 'admin/waste/category/save'
				} else {
					var urls = 'admin/waste/category/update'
				}
				if(this.parentId===0){
					var parentIds = ''
				}else{
					var parentIds = this.parentId
				}
				
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					data: this.$http.adornData({
						'name': this.name,
						'cover': this.img,
						'id': this.classifyId,
						'level': this.parentIdnum,
						'parentId': parentIds,
						
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible9 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								if(this.parentId==0){
									this.classSelect()
								}else{
									this.classSelectListEr()
								}
								
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
			// 删除服务分类
			classdelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/waste/category/delete'),
						method: 'post',
						data: this.$http.adornData({
							'id': row.id
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									if(this.parentId==0){
										this.classSelect()
									}else{
										this.classSelectListEr()
									}
								}
							})
						} else {
							this.$message({
								message: data.smg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				}).catch(() => {})
			},
			// 获取分类数据
			classSelectList() {
				this.$http({
					url: this.$http.adornUrl('admin/waste/category/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': 1,
						'limit': 100,
						'parentId':0,
						'level':'',
						'name':'',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.classDatas = returnData.records
				})
			},
			parentIdnumBtn(){
				if(this.parentIdnum==1){
					this.parentId = 0
				}else{
					this.parentId = ''
				}
				console.log('this.parentIdnum:'+this.parentIdnum,'---',this.parentId)
			},
			// 二级分类
			classErList(row){
				this.parentId = row.id
				
				this.classSelectListEr()
				this.dialogFormVisible1 = true
				
			},
			// 获取分类数据
			classSelectListEr() {
				
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/waste/category/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': 1,
						'limit': 100,
						'parentId':this.parentId,
						'level':'',
						'name':'',
					})
				}).then(({
					data
				}) => {
					
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.classDatasEr = returnData.records
				})
			},
			handleClose(done){
				this.parentId = 0
				this.dialogFormVisible1 = false
			},
			// 价格配置 弹框
			classJgList(row){
				this.categoryId = row.id
				this.classJgSelect()
				this.dialogFormVisible2 = true
			},
			// 获取 价格配置 数据
			classJgSelect() {
				this.tableDataLoading2 = true
				this.$http({ 
					url: this.$http.adornUrl('admin/waste/price/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': 1,
						'limit': 20,
						'categoryId':this.categoryId
					})
				}).then(({
					data
				}) => {
					
						this.tableDataLoading2 = false
						let returnData = data.data
						this.tableDataJg = returnData.records
			
				})
			},
			// 添加/修改 价格配置 弹框
			classJgAdd(row) {
				if (row) {
					this.titles = '修改'
					this.name = row.unit
					this.id = row.id
					this.sort = row.sort
					this.money = row.price
				} else {
					this.titles = '添加'
					this.name = ''
					this.money = ''
					this.sort = ''
					this.id = ''
				}
				this.dialogFormVisible3 = true
			},
			// 提交 修改/添加 价格配置
			refuseto2() {
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入规则(单位/数量)',
						type: 'warning'
					})
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入价格',
						type: 'warning'
					})
					return 
				}
				if (this.sort == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入排序',
						type: 'warning'
					})
					return 
				}
			
				if (this.titles == '添加') {
					var urls = 'admin/waste/price/save'
				} else {
					var urls = 'admin/waste/price/update'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					data: this.$http.adornData({
						'unit': this.name,
						'price': this.money,
						'id': this.id,
						'sort': this.sort,
						'categoryId': this.categoryId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible3 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.classJgSelect()
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
			// 删除 价格配置
			classJgdelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/waste/price/delete'),
						method: 'post',
						data: this.$http.adornData({
							'id': row.id
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
										this.classJgSelect()
									
								}
							})
						} else {
							this.$message({
								message: data.smg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
			
					})
				}).catch(() => {})
			},
		},
		mounted() {
			this.classSelect()

		}
	}
</script>

<style lang="scss" scoped>
.classification-page {
	padding: 0;
	overflow-x: hidden;
}

.classification-page__hero {
	margin-bottom: 12px;
}

.classification-page__filter {
	margin-bottom: 12px;
}

.classification-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.classification-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.classification-page__toolbar {
	display: flex;
	justify-content: flex-end;
	padding: 12px 16px;
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.classification-page__table-card >>> .el-table {
	border-radius: 0;
}

.classification-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.classification-page__table >>> .el-table__body td {
	background: #fff;
}

.classification-page__thumb {
	border-radius: 4px;
	object-fit: cover;
	vertical-align: middle;
}

.classification-page__no-img {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
}

.classification-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
