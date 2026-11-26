<template>
	<div class="specifications-page">
		<div class="nf-page-hero specifications-page__hero">
			<span class="nf-page-hero__badge">废品管理</span>
			<h2 class="nf-page-hero__title">价格配置</h2>
			<p class="nf-page-hero__desc">管理各分类下的回收价格规则，支持按单位/数量配置价格，可新增、修改与删除</p>
		</div>

		<div class="nf-filter-card specifications-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body specifications-page__filter-body">
				<el-button size="mini" type="primary" icon="el-icon-refresh" plain @click="select()">刷新</el-button>
			</div>
		</div>

		<div class="nf-table-card specifications-page__table-card">
			<div class="specifications-page__toolbar">
				<el-button size="mini" type="primary" icon="el-icon-plus"
					:disabled="!isAuth('classification:add')" @click="classAdd(0)">添加
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.records" row-key="classifyId"
				default-expand-all class="specifications-page__table">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="classifyName" label="分类名称">
				</el-table-column>
				<el-table-column prop="unit" label="规则(单位/数量)">
				</el-table-column>
				<el-table-column prop="price" label="价格">
				</el-table-column>
				<el-table-column prop="sort" label="排序序号">
				</el-table-column>
				<el-table-column prop="updateTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="240" fixed='right' align="center">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('classification:update')"
							@click="classAdd(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('classification:delete')"
							@click="classdelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="specifications-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</div>

		<!-- 添加、修改服务分类 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible9" center>
			<div style="margin-bottom: 10px;" v-if="titles == '添加'">
				<span style="width: 200px;display: inline-block;text-align: right;">一级分类：</span>
				<el-select v-model="parentId" style="width:50%;" @change="classSelectListEr()">
					<el-option v-for="item in classDatas" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;"  v-if="titles == '添加'">
				<span style="width: 200px;display: inline-block;text-align: right;">二级分类：</span>
				<el-select v-model="categoryId" style="width:50%;">
					<el-option v-for="item in classDatasEr" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</div>
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
				<el-button @click="dialogFormVisible9 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto1()">确 定</el-button>
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
				tableData: [],
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
				categoryId:'',
				money: '',
				minOfferMoney: '',
				minQuoteMoney: '',
				classifyNames:'',
				minMasterMoney:'',
				sort:'',
				classDatasEr:[],
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
				this.$http({ 
					url: this.$http.adornUrl('admin/waste/price/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'categoryId':this.classifyNames
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
					this.titles = '修改'
					this.name = row.unit
					this.classifyId = row.id
					this.sort = row.sort
					this.categoryId = row.categoryId
					this.money = row.price
				} else {
					this.titles = '添加'
					this.name = ''
					this.categoryId = ''
					this.classifyId = ''
					this.money = ''
					this.parentId = ''
					this.sort = ''
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
			// 提交修改、添加服务分类
			refuseto1() {
				if (this.parentId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入选择一级分类',
						type: 'warning'
					})
					return
				}
				if (this.categoryId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择二级分类',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入规则(单位/数量)',
						type: 'warning'
					})
					return
				}
				if (this.money === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入价格',
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
						'categoryId': this.categoryId,
						'id': this.classifyId,
						'price': this.money,
						'sort':this.sort,
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
								this.classSelect()
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
			// s删除服务分类
			classdelete(row) {
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
									this.classSelect()
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
			// 获取一级分类数据
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
			// 获取二级分类数据
			classSelectListEr() {
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
					
					let returnData = data.data;
					this.classDatasEr = returnData.records
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
		},
		mounted() {
			this.classSelect()

		}
	}
</script>

<style lang="scss" scoped>
.specifications-page {
	padding: 0;
	overflow-x: hidden;
}

.specifications-page__hero {
	margin-bottom: 12px;
}

.specifications-page__filter {
	margin-bottom: 12px;
}

.specifications-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.specifications-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.specifications-page__toolbar {
	display: flex;
	justify-content: flex-end;
	padding: 12px 16px;
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.specifications-page__table-card >>> .el-table {
	border-radius: 0;
}

.specifications-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.specifications-page__table >>> .el-table__body td {
	background: #fff;
}

.specifications-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
