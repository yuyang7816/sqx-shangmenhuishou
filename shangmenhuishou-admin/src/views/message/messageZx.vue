<template>
	<div class="message-zx-page">
		<div class="nf-page-hero message-zx-page__hero">
			<span class="nf-page-hero__badge">消息管理</span>
			<h2 class="nf-page-hero__title">注销信息</h2>
			<p class="nf-page-hero__desc">查看用户注销申请，支持查看用户详情、审核注销状态及执行注销操作</p>
		</div>

		<div class="message-zx-page__tabs-wrap">
			<el-tabs v-model="activeName" class="message-zx-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="注销信息" name="first">
					<div class="nf-table-card message-zx-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records" class="message-zx-page__table">
							<el-table-column prop="id" label="编号" width="80">
							</el-table-column>
							<el-table-column prop="userName" label="用户名称" width="150">
								<template slot-scope="scope">
									<span class="message-zx-page__link"
										@click="updataDetails(scope.row,scope.row.userEntity)">
										{{ scope.row.userName ? scope.row.userName : '未绑定' }}
									</span>
								</template>
							</el-table-column>
							<el-table-column prop="title" label="标题">
							</el-table-column>
							<el-table-column prop="content" label="内容">
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="160">
							</el-table-column>
							<el-table-column prop="status" label="状态" width="100">
								<template slot-scope="scope">
									<span v-if="scope.row.status == 1">待注销</span>
									<span v-if="scope.row.status == 2">已注销</span>
									<span v-if="scope.row.status == 3">已撤销</span>
								</template>
							</el-table-column>
							<el-table-column label="操作" width="120">
								<template slot-scope="scope">
									<el-button size="mini" type="danger" :disabled="!isAuth('messageZx:shenhe')"
										v-if="scope.row.status == 1" @click="deleteuser(scope.row)">注销用户</el-button>
									<span v-if="scope.row.status == 2">用户已注销</span>
								</template>
							</el-table-column>
						</el-table>
						<div class="message-zx-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<el-dialog title="审核处理" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="radio">
					<el-radio :label="0">通过</el-radio>
					<el-radio :label="1">驳回</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="radio===1">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">驳回理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入驳回理由">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="radio===0">
				<span style="width: 200px;display: inline-block;text-align: right;">处理结果：</span>
				<el-radio-group v-model="clStatus">
					<el-radio :label="0">封号</el-radio>
					<el-radio :label="1">下架</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
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
				state: '',
				content: '',

				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				radio: '',
				clStatus: '',
				messageId: '',
				platform: '',
				platform1: '',

			}
		},
		methods: {
			// 详情跳转
			updataDetails(row, userEntity) {
				if (userEntity != null) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					});
				} else {
					this.$message({
						message: '当前用户已注销',
						type: 'warning',
						duration: 1500,
						onClose: () => {}
					})
				}

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
				if (tab._props.label == '注销信息') {
					this.page = 1
					this.limit = 10
					this.state = ''
					this.dataSelect()
				}
			},

			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/cancelAccount/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'status': this.state,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 注销
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定要注销该用户?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/cancelAccount/cancel'),
						method: 'get',
						params: this.$http.adornData({
							'id': row.id
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

			//审核弹框
			refuseClick(rows) {
				this.radio = ''
				this.clStatus = ''
				this.content = ''
				this.messageId = rows.id
				this.platform = rows.platform
				this.dialogFormVisible = true
			},
			//提交审核
			refuseto() {
				if (this.radio === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}
				if (this.radio === 1) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入拒绝理由',
							type: 'warning'
						});
						return
					}
				}
				if (this.radio === 0) {
					if (this.clStatus === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择处理结果',
							type: 'warning'
						});
						return
					}
				}
				var status = ''
				if (this.radio === 1) {
					status = 1
				} else {
					if (this.clStatus === 0) {
						status = 2
					}
					if (this.clStatus === 1) {
						status = 3
					}
				}

				this.$http({
					url: this.$http.adornUrl('message/auditMessage'),
					method: 'post',
					params: this.$http.adornParams({
						'messageId': this.messageId,
						'status': status,
						'auditContent': this.content
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
								this.content = ''
								this.status = -1
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
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style lang="scss" scoped>
.message-zx-page {
	padding: 0;
	overflow-x: hidden;
}

.message-zx-page__hero {
	margin-bottom: 12px;
}

.message-zx-page__tabs-wrap {
	margin-bottom: 0;
}

.message-zx-page__tabs {
	margin-bottom: 0;
}

.message-zx-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.message-zx-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.message-zx-page__table-card >>> .el-table {
	border-radius: 0;
}

.message-zx-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.message-zx-page__table >>> .el-table__body td {
	background: #fff;
}

.message-zx-page__link {
	color: #01af74;
	cursor: pointer;
}

.message-zx-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
