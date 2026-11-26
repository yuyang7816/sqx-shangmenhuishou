<template>
	<div class="app-page">
		<div class="nf-page-hero app-page__hero">
			<span class="nf-page-hero__badge">应用管理</span>
			<h2 class="nf-page-hero__title">升级配置</h2>
			<p class="nf-page-hero__desc">管理 uniapp 客户端版本升级地址、版本号及升级方式</p>
		</div>

		<div class="nf-filter-card app-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-setting"></i>
				<span>快捷操作</span>
			</div>
			<div class="nf-filter-card__body app-page__filter-body">
				<el-button size="mini" type="primary" icon="el-icon-refresh" @click="upgradeSelect">刷新数据</el-button>
				<el-button size="mini" type="primary" icon="el-icon-plus" :disabled="!isAuth('app:add')"
					@click="addUpgrade">添加升级</el-button>
			</div>
		</div>

		<div class="nf-table-card app-page__table-card">
			<el-table v-loading="tableDataLoading" :data="upgradeData1.records" class="app-page__table">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="wgtUrl" label="统一地址">
				</el-table-column>
				<el-table-column prop="androidWgtUrl" label="安卓">
				</el-table-column>
				<el-table-column prop="iosWgtUrl" label="苹果">
				</el-table-column>
				<el-table-column prop="version" label="安卓版本">
				</el-table-column>
				<el-table-column prop="iosVersion" label="ios版本">
				</el-table-column>
				<el-table-column prop="content" label="升级方式">
					<template slot-scope="scope">
						<span>{{scope.row.method == 'true' ? '强制升级' : '普通升级'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('app:update')"
							@click="upgradebj(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('app:delete')"
							@click="upgradelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="app-page__pagination">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="upgradeData1.total">
				</el-pagination>
			</div>
		</div>

		<!-- 添加升级弹框 -->
		<el-dialog title="添加升级" :visible.sync="dialogFormVisible3" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>统一地址：</span>
				<el-input style="width: 50%;" v-model="wgtUrl" placeholder="请输入统一地址"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>安卓地址：</span>
				<el-input style="width:50%;" v-model="androidWgtUrl" placeholder="请输入安卓地址"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>苹果地址：</span>
				<el-input style="width: 50%;" v-model="iosWgtUrl" placeholder="请输入苹果地址"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>安卓版本号：</span>
				<el-input style="width: 50%;" v-model="version" placeholder="请输入安卓版本号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>ios版本号：</span>
				<el-input style="width: 50%;" v-model="iosVersion" placeholder="请输入ios版本号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>升级方式：</span>
				<el-select v-model="method" placeholder="请选择升级方式" style="width:50%;">
					<el-option v-for="item in methods" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>升级内容：</span>
				<el-input style="width: 50%;" v-model="des" placeholder="请输入升级内容"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="upgradaddTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改升级弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible4" center>
			<el-form :model="formupgrad">
				<el-form-item label="统一地址：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.wgtUrl" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="安卓地址：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.androidWgtUrl" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="苹果地址：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.iosWgtUrl" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="安卓版本号：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.version" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="ios版本号：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.iosVersion" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="升级方式：" :label-width="formLabelWidth">
					<el-select v-model="formupgrad.method" placeholder="请选择升级方式" style="width:65%;">
						<el-option v-for="item in methods" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="升级内容：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.des" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible4 = false">取 消</el-button>
				<el-button type="primary" @click="upgradbjTo()">确 定</el-button>
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
				state: 1,
				url: '',
				title: '',
				type: '',
				id: '',
				nav: '',
				keyword: '',
				image_url: '',
				androidWgtUrl: '',
				iosWgtUrl: '',
				wgtUrl: '',
				version: '',
				iosVersion: '',
				des: '',
				checkBoxData: [], //多选框选择的值
				method: 'false',
				methods: [{
						value: 'false',
						label: '普通升级'
					},
					{
						value: 'true',
						label: '强制升级'
					}
				],
				formupgrad: {
					id: '',
					androidWgtUrl: '',
					iosWgtUrl: '',
					wgtUrl: '',
					version: '',
					des: '',
					method: '',
					iosVersion: ''
				},
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible3: false,
				dialogFormVisible4:false,
				tableData: [],
				homeData: [],
				choicenData2: [],
				upgradeData: [],
				upgradeData1: [],
				choicenData: [],
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
				],
				bannerData: [],
				form1: {
					id: '',
					url: '',
					image_url: ''
				},
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
				formcomp: {
					id: '',
					state: '',
					title: '',
					url: '',
					image_url: ''
				}
			}
		},
		methods: {

			handleSizeChange2(val) {
				this.limit = val;
				this.upgradeSelect()
			},
			handleCurrentChange2(val) {
				this.page = val;
				this.upgradeSelect()
			},

			handleClick(tab, event) {
				this.upgradeSelect()
			},
			// 添加升级弹框
			addUpgrade() {
				this.id = ''
				this.androidWgtUrl = ''
				this.iosWgtUrl = ''
				this.wgtUrl = ''
				this.version = ''
				this.des = ''
				this.method = ''
				this.iosVersion = ''
				this.dialogFormVisible3 = true
			},
			// 添加升级
			upgradaddTo() {
				if (this.wgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入统一地址',
						type: 'warning'
					});
					return
				}
				if (this.androidWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入安卓地址',
						type: 'warning'
					});
					return
				}
				if (this.iosWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入苹果地址',
						type: 'warning'
					});
					return
				}

				if (this.version == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入安卓版本号',
						type: 'warning'
					});
					return
				}
				if (this.iosVersion == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入苹果版本号',
						type: 'warning'
					});
					return
				}

				if (this.method == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择升级方式',
						type: 'warning'
					});
					return
				}
				if (this.des == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入升级内容',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/app/save'),
					method: 'post',
					data: this.$http.adornData({
						'androidWgtUrl': this.androidWgtUrl,
						'iosWgtUrl': this.iosWgtUrl,
						'wgtUrl': this.wgtUrl,
						'version': this.version,
						'iosVersion': this.iosVersion,
						'method': this.method,
						'des': this.des
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible3 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.upgradeSelect()
							}
						})
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
			// 修改升级弹框
			upgradebj(index, rows) {
				this.dialogFormVisible4 = true;
				this.formupgrad.androidWgtUrl = rows.androidWgtUrl;
				this.formupgrad.id = rows.id;
				this.formupgrad.iosWgtUrl = rows.iosWgtUrl;
				this.formupgrad.wgtUrl = rows.wgtUrl;
				this.formupgrad.version = rows.version;
				this.formupgrad.iosVersion = rows.iosVersion;
				this.formupgrad.des = rows.des;
				this.formupgrad.method = rows.method;
			},
			// 修改升级
			upgradbjTo() {
				this.$http({
					url: this.$http.adornUrl('admin/app/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.formupgrad.id,
						'androidWgtUrl': this.formupgrad.androidWgtUrl,
						'iosWgtUrl': this.formupgrad.iosWgtUrl,
						'wgtUrl': this.formupgrad.wgtUrl,
						'version': this.formupgrad.version,
						'iosVersion': this.formupgrad.iosVersion,
						'des': this.formupgrad.des,
						'method': this.formupgrad.method,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible4 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.upgradeSelect()
							}
						})
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
			// 删除升级
			upgradelete(row) {
				let delid = row.id
				this.id = delid
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/app/delete'),
						method: 'post',
						data: this.$http.adornData({
							'id':row.id
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.name = '';
									this.upgradeSelect()
								}
							})
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
				}).catch(() => {})
			},

			// 升级配置数据列表
			upgradeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/app/page'),
					method: 'post',
					data: this.$http.adornData({
						'page':this.page,
						'limit':this.limit
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.upgradeData1 = returnData
				})
			},

		},
		mounted() {
			console.log('···························')
			this.upgradeSelect()
		}
	};
</script>

<style lang="scss" scoped>
.app-page {
	padding: 0;
	overflow-x: hidden;
}

.app-page__hero {
	margin-bottom: 12px;
}

.app-page__filter {
	margin-bottom: 12px;
}

.app-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.app-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.app-page__table-card >>> .el-table {
	border-radius: 0;
}

.app-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.app-page__table >>> .el-table__body td {
	background: #fff;
}

.app-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
