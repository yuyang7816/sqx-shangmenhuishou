<template>
	<div class="master-position-page">
		<div class="nf-page-hero master-position-page__hero">
			<span class="nf-page-hero__badge">师傅管理</span>
			<h2 class="nf-page-hero__title">师傅位置记录</h2>
			<p class="nf-page-hero__desc">查看师傅实时位置信息，支持按师傅姓名与电话筛选查询</p>
		</div>

		<div class="nf-filter-card master-position-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body master-position-page__filter-body">
				<div class="master-position-page__field">
					<span class="master-position-page__label">师傅姓名：</span>
					<el-input class="master-position-page__input" @keydown.enter.native="select" placeholder="请输入师傅姓名"
						v-model="artificerName">
					</el-input>
				</div>
				<div class="master-position-page__field">
					<span class="master-position-page__label">电话：</span>
					<el-input class="master-position-page__input" @keydown.enter.native="select" placeholder="请输入电话"
						v-model="phone">
					</el-input>
				</div>
				<div class="master-position-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<div class="master-position-page__tabs-wrap">
			<el-tabs v-model="activeName" class="master-position-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="位置列表" name="first">
					<div class="nf-table-card master-position-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.list"
							class="master-position-page__table">
							<el-table-column fixed prop="id" label="编号" width="80">
								<template slot-scope="scope">
									<span>{{ scope.$index + 1 }}</span>
								</template>
							</el-table-column>
							<el-table-column prop="masterName" label="师傅姓名" width="100">
								<template slot-scope="scope">
									<span class="master-position-page__link" @click="updates(scope.row.userId)">
										{{ scope.row.masterName ? scope.row.masterName : '未绑定' }}
									</span>
								</template>
							</el-table-column>
							<el-table-column prop="avatar" label="头像">
								<template slot-scope="scope">
									<img v-if="scope.row.avatar == null" src="~@/assets/img/avatar.png" alt=""
										width="40" height="40">
									<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
								</template>
							</el-table-column>
							<el-table-column prop="phone" label="电话" width="160">
							</el-table-column>
							<el-table-column prop="address" label="地址" width="200"></el-table-column>
							<el-table-column prop="longitude" label="经度" width="160">
							</el-table-column>
							<el-table-column prop="latitude" label="纬度" width="160">
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
						</el-table>
						<div class="master-position-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				classify: 1,
				indentNumber: '',
				imageUrl: '',
				url: '',
				complaintState: '',
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				tableData: [],
				checkBoxData: [], //多选框选择的值
				statesnum: [{
						label: '全部',
						value: ''
					},
					{
						label: '可申诉',
						value: 1
					},
					{
						label: '申诉中',
						value: 2
					},
					{
						label: '申诉未通过',
						value: 3
					},
					{
						label: '申诉通过',
						value: 4
					},
					{
						label: '待审核',
						value: 5
					},
					{
						label: '投诉未通过',
						value: 6
					},
				],
				content: '',
				status: 1,
				helpTakeId: '',
				indentNumbers: '',
				complaintId: '',
				message: '',
				title: '',
				type: 3,
				form: {
					id: '',
					illegal: '',
					forfeitMoney: '',
				},
				formLabelWidth: '200px',
				dialogFormVisible1: false,
				guangaoData: {},
				illegal: '',
				illegalId: '',
				forfeitMoney: '',
				illegalList: [],
				phone: '',
				artificerName: '',

			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '位置列表') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}

			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},

			// 查询资源列表
			select() {
				this.page = 1
				this.limit = 10

				this.dataSelect()
			},
			// 重置资源列表
			cleans() {
				this.phone = ''
				this.artificerName = ''
				this.illegalId = ''
				this.page = 1
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.complaintState = state
				console.log(state)
				this.dataSelect()
			},
			animeDat1(state) {
				this.page = 1
				this.illegalId = state
				this.dataSelect()
			},


			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/selectMasterLocation'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'masterName': this.artificerName,
						'phone': this.phone,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
					}
				})
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
			// 处理
			classdelete(row) {
				this.$confirm(`确定处理该投诉?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('message/auditMessage'),
						method: 'post',
						params: this.$http.adornParams({
							'ids': row.id,
							'status': 1,
							'auditContent': '',
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
				}).catch(() => {})
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style lang="scss" scoped>
.master-position-page {
	padding: 0;
	overflow-x: hidden;
}

.master-position-page__hero {
	margin-bottom: 12px;
}

.master-position-page__filter {
	margin-bottom: 12px;
}

.master-position-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.master-position-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.master-position-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.master-position-page__input {
	width: 200px;
}

.master-position-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.master-position-page__tabs-wrap {
	margin-bottom: 0;
}

.master-position-page__tabs {
	margin-bottom: 0;
}

.master-position-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.master-position-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.master-position-page__table-card >>> .el-table {
	border-radius: 0;
}

.master-position-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.master-position-page__table >>> .el-table__body td {
	background: #fff;
}

.master-position-page__link {
	color: #01af74;
	cursor: pointer;
}

.master-position-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>

<style>
	.customWidth {
		width: 80% !important;
	}
</style>
