<template>
	<div class="mission-complain-page">
		<div class="nf-page-hero mission-complain-page__hero">
			<span class="nf-page-hero__badge">服务管理</span>
			<h2 class="nf-page-hero__title">服务投诉</h2>
			<p class="nf-page-hero__desc">查看用户服务投诉记录，支持按投诉内容筛选，可查看详情并处理投诉</p>
		</div>

		<div class="nf-filter-card mission-complain-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body mission-complain-page__filter-body">
				<div class="mission-complain-page__field">
					<span class="mission-complain-page__label">投诉内容：</span>
					<el-input class="mission-complain-page__input" @keydown.enter.native="select" clearable
						placeholder="请输入投诉内容" v-model="content"></el-input>
				</div>
				<div class="mission-complain-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card mission-complain-page__table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list" class="mission-complain-page__table">
				<el-table-column fixed prop="id" label="id" width="80">
				</el-table-column>
				<el-table-column prop="nickName" label="用户昵称" width="150">
					<template slot-scope="scope">
						<span class="mission-complain-page__link" @click="updates(scope.row)">
							{{ scope.row.nickName ? scope.row.nickName : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="标题">
				</el-table-column>
				<el-table-column prop="picture" label="截图">
					<template slot-scope="scope">
						<div v-if="scope.row.picture == null || scope.row.picture == ''">
							暂无图片
						</div>
						<div v-else-if="scope.row.picture.includes(',')" class="mission-complain-page__pics">
							<div v-for="item in scope.row.picture.split(',')" :key="item" class="mission-complain-page__pic-item">
								<el-popover placement="top-start" title="" trigger="hover">
									<img class="mission-complain-page__thumb" :src="item" alt="" slot="reference">
									<img class="mission-complain-page__preview" :src="item" alt="">
								</el-popover>
							</div>
						</div>
						<div v-else>
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="mission-complain-page__thumb" :src="scope.row.picture" alt="" slot="reference">
								<img class="mission-complain-page__preview" :src="scope.row.picture" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="投诉内容">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span class="mission-complain-page__status" v-if="scope.row.state === 1">待审核</span>
						<span v-if="scope.row.state === 2">已审核</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="complainDetails(scope.row)">详情/处理
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="mission-complain-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>

		<!-- 服务投诉 -->
		<el-dialog title="服务投诉详情" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="投诉内容：" :label-width="formLabelWidth">
					<span>{{ form.content }}</span>
				</el-form-item>
				<el-form-item label="投诉截图：" :label-width="formLabelWidth">
					<div class="mission-complain-page__dialog-imgs">
						<div v-for="(item, index) of imgs" :key="index" class="mission-complain-page__dialog-img">
							<img :src="item" class="avatar" />
						</div>
					</div>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer" v-if="form.state == 1">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">处 理</el-button>
			</div>
		</el-dialog>
		<!-- 处理服务 -->
		<el-dialog title="处理服务" :visible.sync="dialogFormVisible1" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务状态：</span>
				<el-radio-group v-model="state">
					<el-radio :label="2">下架</el-radio>
					<el-radio :label="3">不下架</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="complainTo()">确 定</el-button>
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
				userId: '',
				content: '',
				form: {
					id: '',
					content: '',
					picture: '',
					state: ''
				},
				state: '',
				imgs: [],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: [],
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			// 用户详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			//删除服务投诉
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
								this.name = '';
								this.classifySelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 查询
			select() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.content = ''
				this.dataSelect()
			},
			complainDetails(row) {
				this.form.id = row.id
				this.form.content = row.content
				this.form.picture = row.picture
				this.form.state = row.state
				let imgs = this.form.picture.split(',')
				this.imgs = imgs;
				this.dialogFormVisible = true
			},
			// 处理
			amendNoticeTo() {
				this.dialogFormVisible1 = true
			},
			// 处理投诉
			complainTo() {
				if (this.state == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择服务状态',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl(`/helpComplaint/solveHelpComplaint/${this.form.id}/${this.state}`),
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
								this.dialogFormVisible = false
								this.dialogFormVisible1 = false
								this.dataSelect()
							}
						})
					}
				})
			},
			// 获取服务投诉列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpComplaint/selectHelpComplaintList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': this.userId,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style lang="scss" scoped>
.mission-complain-page {
	padding: 0;
	overflow-x: hidden;
}

.mission-complain-page__hero {
	margin-bottom: 12px;
}

.mission-complain-page__filter {
	margin-bottom: 12px;
}

.mission-complain-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.mission-complain-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.mission-complain-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.mission-complain-page__input {
	width: 200px;
}

.mission-complain-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.mission-complain-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.mission-complain-page__table-card >>> .el-table {
	border-radius: 0;
}

.mission-complain-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.mission-complain-page__table >>> .el-table__body td {
	background: #fff;
}

.mission-complain-page__link {
	color: #01af74;
	cursor: pointer;
}

.mission-complain-page__link:hover {
	color: #017c52;
}

.mission-complain-page__status {
	color: #01af74;
}

.mission-complain-page__pics {
	display: flex;
	flex-wrap: wrap;
}

.mission-complain-page__pic-item {
	margin: 2px;
}

.mission-complain-page__thumb {
	width: 50px;
	height: 50px;
}

.mission-complain-page__preview {
	width: 200px;
	height: 200px;
}

.mission-complain-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.mission-complain-page__dialog-imgs {
	display: flex;
	flex-wrap: wrap;
}

.mission-complain-page__dialog-img {
	margin-left: 10px;
	margin-top: 10px;
}

.mission-complain-page__dialog-img .avatar {
	width: 148px;
	height: 148px;
}
</style>
