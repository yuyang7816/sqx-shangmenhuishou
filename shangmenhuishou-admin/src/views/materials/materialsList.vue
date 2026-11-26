<template>
	<div class="materials-list-page">
		<div class="nf-page-hero materials-list-page__hero">
			<span class="nf-page-hero__badge">内容管理</span>
			<h2 class="nf-page-hero__title">帮助中心</h2>
			<p class="nf-page-hero__desc">管理帮助分类与文章，支持按分类名称与端类型筛选，可新增、编辑与删除</p>
		</div>

		<div v-show="activeName === 'first'" class="nf-filter-card materials-list-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body materials-list-page__filter-body">
				<div class="materials-list-page__field">
					<span class="materials-list-page__label">分类名称：</span>
					<el-input class="materials-list-page__input" @keydown.enter.native="select"
						v-model="helpClassifyNameT" placeholder="请输入分类名称"></el-input>
				</div>
				<div class="materials-list-page__field">
					<span class="materials-list-page__label">类型：</span>
					<el-select v-model="classifyTypes" class="materials-list-page__select" placeholder="请选择类型"
						@change="select(classifyTypes)">
						<el-option v-for="(item,index) in classifyTypesList" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="materials-list-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button size="mini" type="primary" icon="el-icon-plus" :disabled="!isAuth('materialsList:add')"
						@click="addNotice">添加分类</el-button>
				</div>
			</div>
		</div>

		<div v-show="activeName === 'second'" class="nf-filter-card materials-list-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation"></i>
				<span>快捷操作</span>
			</div>
			<div class="nf-filter-card__body materials-list-page__filter-body materials-list-page__filter-body--actions">
				<div class="materials-list-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-refresh" @click="selectW">刷新</el-button>
					<el-button size="mini" type="primary" icon="el-icon-plus" :disabled="!isAuth('materialsList:add')"
						@click="addNoticewz">添加文章</el-button>
				</div>
			</div>
		</div>

		<div class="materials-list-page__tabs-wrap">
			<el-tabs v-model="activeName" class="materials-list-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="分类管理" name="first">
					<div class="nf-table-card materials-list-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableDataF.records"
							class="materials-list-page__table">
							<el-table-column fixed prop="id" label="编号" width="80">
							</el-table-column>
							<el-table-column prop="clientType" label="类型">
								<template slot-scope="scope">
									<div v-if="scope.row.clientType==1">用户端</div>
									<div v-if="scope.row.clientType==2">回收员端</div>
								</template>
							</el-table-column>
							<el-table-column prop="name" label="分类名称">
							</el-table-column>
							<el-table-column prop="sort" label="排序">
							</el-table-column>
							<el-table-column label="操作" width="150">
								<template slot-scope="scope">
									<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
										@click="updates(scope.row)">编辑
									</el-button>
									<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
										@click="deletes(scope.row)">删除
									</el-button>
								</template>
							</el-table-column>
						</el-table>
					</div>
				</el-tab-pane>
				<el-tab-pane label="文章管理" name="second">
					<div class="nf-table-card materials-list-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records"
							class="materials-list-page__table">
							<el-table-column prop="id" label="编号" width="80">
							</el-table-column>
							<el-table-column prop="title" label="文章名称">
								<template slot-scope="scope">
									<el-popover placement="top-start" title="" trigger="hover">
										<div class="materials-list-page__ellipsis" slot="reference">{{scope.row.title}}</div>
										<div class="materials-list-page__popover">{{scope.row.title}}</div>
									</el-popover>
								</template>
							</el-table-column>
							<el-table-column prop="content" label="内容">
								<template slot-scope="scope">
									<el-popover placement="top-start" title="" trigger="hover">
										<div class="materials-list-page__ellipsis" slot="reference">{{scope.row.content}}</div>
										<div class="materials-list-page__popover">{{scope.row.content}}</div>
									</el-popover>
								</template>
							</el-table-column>
							<el-table-column prop="sort" label="排序" width="140">
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="160">
							</el-table-column>
							<el-table-column label="操作" width="160">
								<template slot-scope="scope">
									<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
										@click="updateswz(scope.$index, scope.row)">编辑
									</el-button>
									<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
										@click="deleteswz(scope.row)">删除
									</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="materials-list-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 添加分类弹框 -->
		<el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
				<el-select v-model="types" placeholder="请选择类型" style="width:50%;">
					<el-option v-for="(item,index) in typesList" :key="item.value" :label="item.label"
						:value="item.value">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
				<el-input style="width:50%;" v-model="helpClassifyName" placeholder="请输入分类名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input-number size="medium" v-model="sort"></el-input-number>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改分类弹框 -->
		<el-dialog title="修改分类" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="类型：" :label-width="formLabelWidth">
					<el-select v-model="form.types" placeholder="请选择类型" style="width:65%;">
						<el-option v-for="(item,index) in typesList" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<el-form :model="form">
				<el-form-item label="分类名称：" :label-width="formLabelWidth">
					<el-input v-model="form.helpClassifyName" style="width:65%;"></el-input>

				</el-form-item>
			</el-form>
			<el-form :model="form">
				<el-form-item label="排序：" :label-width="formLabelWidth">
					<el-input-number size="medium" v-model="form.sort"></el-input-number>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加文章弹框 -->
		<el-dialog title="添加文章" :visible.sync="dialogFormVisible2" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">文章分类：</span>
				<el-select v-model="classifyIdFl" placeholder="请选择类型" style="width: 80%;">
					<el-option v-for="(item,index) in classifyIds" :key="index" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">排序：</span>
				<el-input-number size="medium" v-model="sort"></el-input-number>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">文章名称：</span>
				<el-input style="width: 80%;" v-model="helpWordTitle" placeholder="请输入文章名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">文章内容：</span>
				<quill-editor ref="myTextEditor" v-model="helpWordContent" :options="quillOption"
					style="height: 400px;width: 80%;display: inline-table;margin-bottom: 60px;">
				</quill-editor>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTowz()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改文章弹框 -->
		<el-dialog title="修改文章" :visible.sync="dialogFormVisible3" center width="80%">
			<el-form :model="form">
				<el-form-item label="文章分类：" :label-width="formLabelWidth">
					<el-select v-model="formwz.helpClassifyId" placeholder="请选择类型" style="width:80%;">
						<el-option v-for="(item,index) in classifyIds" :key="item.index" :label="item.name"
							:value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="排序：" :label-width="formLabelWidth">
					<el-input-number size="medium" v-model="formwz.sort"></el-input-number>
				</el-form-item>
				<el-form-item label="文章名称：" :label-width="formLabelWidth">
					<el-input v-model="formwz.helpWordTitle" style="width:80%;"></el-input>
				</el-form-item>
				<el-form-item label="文章内容：" :label-width="formLabelWidth">
					<quill-editor ref="myTextEditor" v-model="formwz.helpWordContent" :options="quillOption"
						style="height: 400px;width: 80%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTowz()">确 定</el-button>
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
	import quillConfig from '../mission/quill-config.js'
	export default {
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				limit: 10,
				page: 1,
				state: 1,
				sort: 0,
				remark: '',
				createTime: '',
				helpClassifyName: '',
				helpClassifyId: '',
				classifyId: 1,
				classifyIds: [],
				form: {
					helpClassifyId: '',
					state: '',
					sort: '',
					helpClassifyName: '',
					createTime: '',
					remark: '',
					types: '',
				},
				helpWordTitle: '',
				helpWordContent: '',
				classifyIdFl: '',
				formwz: {
					helpWordId: '',
					picture: '',
					articleUrl: '',
					helpWordContent: '',
					helpWordTitle: '',
					helpClassifyId: '',
					createTime: '',
					sort: 0,
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				picture: '',
				picture2: '',
				articleUrl: '',

				title: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: {},
				tableDataF: {},
				parentIdnum: '',
				helpClassifyNameT: '',
				classnum: [{
						value: '0',
						label: '一级分类'
					},
					{
						value: '1',
						label: '二级分类'
					}
				],
				value: [],
				quillOption: quillConfig,
				helpWordTitleT: '',
				classifyTypes: '',
				classifyTypesList: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '用户端'
					},
					{
						value: 2,
						label: '回收员端'
					}
				],
				types: '',
				typesList: [{
						value: 1,
						label: '用户端'
					},
					{
						value: 2,
						label: '回收员端'
					}
				],
			}
		},
		methods: {
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
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '分类管理') {
					this.dataSelect()
				}
				if (tab._props.label == '文章管理') {
					// this.classifyId = 1
					this.dataSelectW()
					this.articleSelect()
				}
			},
			handleSizeChange(val) {
				this.limit = val;
				this.articleSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.articleSelect()
			},
			handleAvatarSuccess(file) {
				this.picture = file.data.path;
			},
			handleAvatarSuccess2(file2) {
				this.formwz.picture = file2.data;
			},
			// 添加分类弹框
			addNotice() {
				this.helpClassifyName = ''
				this.sort = 0
				this.types = ''
				this.dialogFormVisible = true
			},
			// 添加分类
			releasNoticeTo() {

				if (this.types == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择类型',
						type: 'warning'
					});
					return
				}
				if (this.helpClassifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择分类名称',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/helpCategory/save'),
					method: 'post',
					data: this.$http.adornData({
						'sort': this.sort,
						'name': this.helpClassifyName,
						'clientType': this.types
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
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
			},
			// 编辑分类弹框
			updates(rows) {
				this.form.sort = rows.sort;
				this.dialogFormVisible1 = true;
				this.form.helpClassifyId = rows.id;
				this.form.helpClassifyName = rows.name;
				this.form.types = rows.clientType
			},
			// 修改类别
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(`admin/helpCategory/update`),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.helpClassifyId,
						'name': this.form.helpClassifyName,
						'clientType': this.form.types,
						'sort': this.form.sort
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible1 = false
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
			},
			// 删除分类
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/helpCategory/delete'),
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
			// 添加文章弹框
			addNoticewz() {
				this.helpWordTitle = ''
				this.articleUrl = ''
				this.helpWordContent = ''
				this.picture = ''
				this.classifyIdFl = ''
				this.sort = 0
				this.dialogFormVisible2 = true
			},
			// 添加文章
			releasNoticeTowz() {
				if (this.classifyIdFl == '' || this.classifyIdFl == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择文章分类',
						type: 'warning'
					});
					return
				}
				if (this.helpWordTitle == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入文章标题',
						type: 'warning'
					});
					return
				}

				if (this.helpWordContent == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/helpArticle/save'),
					method: 'post',
					data: this.$http.adornData({
						'categoryId': this.classifyIdFl,
						'content': this.helpWordContent,
						'sort': this.sort,
						'title': this.helpWordTitle,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {

								this.articleSelect()
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
			// 编辑文章弹框
			updateswz(index, rows) {
				this.formwz.picture = rows.picture;
				this.dialogFormVisible3 = true;
				this.formwz.helpWordId = rows.id;
				this.formwz.helpClassifyId = rows.categoryId
				this.formwz.sort = rows.sort;
				this.formwz.helpWordContent = rows.content;
				this.formwz.helpWordTitle = rows.title;
				this.formwz.createTime = rows.createTime;
			},
			// 修改文章
			amendNoticeTowz() {
				if (this.formwz.helpClassifyId == '' || this.formwz.helpClassifyId == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择文章分类',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/helpArticle/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.formwz.helpWordId,
						'content': this.formwz.helpWordContent,
						'categoryId': this.formwz.helpClassifyId,
						'title': this.formwz.helpWordTitle,
						'sort': this.formwz.sort
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
								this.articleSelect()
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
			// 删除文章
			deleteswz(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/helpArticle/delete'),
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
									this.name = '';
									this.articleSelect()
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
			// 获取分类管理数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/helpCategory/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': 1,
						'limit': 100,
						'name': this.helpClassifyNameT,
						'clientType': this.classifyTypes
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataF = returnData
				})
			},
			// 分类无分页
			dataSelectW() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/helpCategory/page'),
					method: 'post',
					data: this.$http.adornData({
						'helpClassifyName': this.helpClassifyNameT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classifyId = data.data.records[0].id
					this.classifyIds = returnData.records
					this.articleSelect()
				})
			},
			// 获取文章管理数据
			articleSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/helpArticle/page'),
					method: 'post',
					data: this.$http.adornData({
						// 'helpClassifyId': this.classifyId,
						// 'helpWordTitle':this.helpWordTitleT,
						'page': this.page,
						'limit': this.limit

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.parentId = element
				})
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.helpClassifyNameT = ''
				this.classifyTypes = ''
				this.page = 1
				this.dataSelect()
			},
			// 查询
			selectW() {
				this.page = 1
				this.articleSelect()
			},
			// 重置
			cleansW() {
				this.helpWordTitleT = ''
				this.page = 1
				this.articleSelect()
			},
		},
		mounted() {
			this.dataSelect()
			this.dataSelectW()
		}
	};
</script>

<style lang="scss" scoped>
.materials-list-page {
	padding: 0;
	overflow-x: hidden;
}

.materials-list-page__hero {
	margin-bottom: 12px;
}

.materials-list-page__filter {
	margin-bottom: 12px;
}

.materials-list-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.materials-list-page__filter-body--actions {
	justify-content: flex-end;
}

.materials-list-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.materials-list-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.materials-list-page__input {
	width: 150px;
}

.materials-list-page__select {
	width: 250px;
}

.materials-list-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.materials-list-page__tabs-wrap {
	margin-bottom: 0;
}

.materials-list-page__tabs {
	margin-bottom: 0;
}

.materials-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.materials-list-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.materials-list-page__table-card >>> .el-table {
	border-radius: 0;
}

.materials-list-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.materials-list-page__table >>> .el-table__body td {
	background: #fff;
}

.materials-list-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.materials-list-page__ellipsis {
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 3;
	overflow: hidden;
	max-height: 80px;
}

.materials-list-page__popover {
	width: 300px;
}
</style>
