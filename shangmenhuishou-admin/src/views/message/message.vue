<template>
	<div class="message-page">
		<div class="nf-page-hero message-page__hero">
			<span class="nf-page-hero__badge">消息管理</span>
			<h2 class="nf-page-hero__title">消息中心</h2>
			<p class="nf-page-hero__desc">查看用户反馈与用户消息，支持浏览联系方式、消息内容及跳转用户详情</p>
		</div>

		<div class="message-page__tabs-wrap">
			<el-tabs v-model="activeName" class="message-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="用户反馈" name="first">
					<div class="nf-table-card message-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records" class="message-page__table">
							<el-table-column prop="id" label="编号" width="80">
							</el-table-column>
							<el-table-column prop="contact" label="联系方式">
							</el-table-column>
							<el-table-column prop="content1" label="内容">
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="160">
							</el-table-column>
						</el-table>
						<div class="message-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="用户消息" name="fourth">
					<div class="nf-table-card message-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableDataG.records" class="message-page__table">
							<el-table-column prop="id" label="编号" width="80">
							</el-table-column>
							<el-table-column prop="type" label="消息类型">
								<template slot-scope="scope">
									<span v-if="scope.row.type == 1">系统公告</span>
									<span v-if="scope.row.type == 2">用户消息</span>
								</template>
							</el-table-column>
							<el-table-column prop="userName" label="用户名称">
							</el-table-column>
							<el-table-column prop="title" label="消息标题">
							</el-table-column>
							<el-table-column prop="content" label="消息内容">
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间">
							</el-table-column>
							<el-table-column label="操作">
								<template slot-scope="scope">
									<el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
									</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="message-page__pagination">
							<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableDataG.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<el-dialog title="投诉处理" :visible.sync="dialogFormVisible" center>
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
					<!-- <el-radio :label="1" v-if="platform==3">删除</el-radio> -->
					<el-radio :label="3">处理</el-radio>
					<el-radio :label="2">不处理</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="radio===0">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">处理说明：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入处理说明">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog :title="titles" :visible.sync="dialogFormVisibleG" center>

			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">公告内容：</span>
				<el-input style="width:50%;" v-model="title" type="textarea" :rows="4" placeholder="请输入公告内容">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
				<el-input style="width:50%;" v-model="url" type="text" placeholder="请输入链接">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleG = false">取 消</el-button>
				<el-button type="primary" @click="refusetoBtn()">确 定</el-button>
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
				state: 2,
				url: '',
				title: '',
				type: '',
				id: '',
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
				],
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
				phone: '',
				content: '',
				flag: '',
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				tableData: [],
				tableDataTs: {},
				tableDataG: {},
				radio: '',
				clStatus: '',
				messageId: '',
				platform: '',
				platform1: '',
				platformList: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '聊天投诉'
					},
					{
						id: 2,
						name: '用户投诉'
					},
					{
						id: 3,
						name: '任务投诉'
					},
					{
						id: 4,
						name: '服务投诉'
					}
				],
				status: '',
				statusT: '',
				statusList: [{
						id: '',
						name: '全部'
					},
					{
						id: 0,
						name: '待处理'
					},
					{
						id: 1,
						name: '已驳回'
					},
					{
						id: 2,
						name: '已封号'
					},
					{
						id: 3,
						name: '已删除'
					},
					{
						id: 4,
						name: '不处理'
					},
					{
						id: 5,
						name: '已处理'
					}
				],
				dialogFormVisibleG: false,
				titles: '',
			}
		},
		methods: {
			// 详情跳转
			updataDetails(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				});
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val;
				this.dataSelect1()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.dataSelect1()
			},
			handleClick(tab, event) {
				this.status = ''
				this.statusT = ''
				if (tab._props.label == '用户反馈') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '用户消息') {
					this.page = 1
					this.limit = 5
					this.state = 1
					this.dataSelect1()
				}
				if (tab._props.label == '系统消息') {
					this.page = 1
					this.limit = 10
					this.state = 2
					this.dataSelect1()
				}
				// if (tab._props.label == '消息推送') {
				// 	this.page = 1
				// 	this.limit = 5
				// 	this.state = 8
				// 	this.dataSelect()
				// }
				// if (tab._props.label == '投诉消息') {
				// 	this.page = 1
				// 	this.limit = 10
				// 	this.state = 3
				// 	this.platform1 = ''
				// 	this.dataSelect1()
				// }
			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible2 = true
			},
			// 消息推送
			magNoticeTo() {
				if (this.flag == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择类型',
						type: 'warning'
					});
					return
				}
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					});
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				if (this.flag == 1) {
					if (this.phone == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入用户手机号',
							type: 'warning'
						});
						return
					}
					this.$http({
						url: this.$http.adornUrl('/cash/sendMsg'),
						method: 'get',
						params: this.$http.adornParams({
							'title': this.title,
							'content': this.content,
							'phone': this.phone,
							'flag': this.flag
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible2 = false
						if (data.code == -100) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
				if (this.flag == 2) {
					this.$http({
						url: this.$http.adornUrl('/cash/sendMsg'),
						method: 'get',
						params: this.$http.adornParams({
							'title': this.title,
							'content': this.content,
							'flag': this.flag,
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible2 = false
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/feedback/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					
						for (var i in data.data.records) {
							var a = data.data.records[i].opinion.toString()
							var b = JSON.parse(a)
							if(b.content!=undefined){
								data.data.records[i].content1 = b.content
							}else{
								data.data.records[i].content1 = ''
							}
							
							console.log(data.data.records[i].content1,'key', b.content)
						}
						let returnData = data.data;
						this.tableData = returnData
						console.log('---', this.tableData.records)

				})
			},
			// 获取数据列表
			dataSelect1() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/messages/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'type': this.state,
						// 'platform': this.platform1,
						// 'status': this.statusT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					// if (this.state == 2) {
					// 	for (var i in data.data.list) {
					// 		var a = data.data.list[i].content.toString()
					// 		var b = JSON.parse(a)
					// 		data.data.list[i].content = b.content
					// 		// console.log('key', b.content)
					// 	}
					// }
					// for (var i in data.data.list) {
					// 	if (data.data.list[i].image) {
					// 		data.data.list[i].image = data.data.list[i].image.split(',')
					// 	}

					// }

					let returnData = data.data;
					this.tableDataG = returnData

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
				if (this.radio === 0) {
					if (this.content === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入处理说明',
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
					if (this.clStatus === 2) {
						status = 4
					}
					if (this.clStatus === 3) {
						status = 5
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
								this.status = ''
								this.dataSelect1()
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
			animeDat4() {
				this.page = 1
				this.dataSelect1()
			},
			// 添加/修改弹框
			addClick(row) {
				if (row) {
					this.titles = '修改公告'
					this.title = row.title
					this.url = row.url
					this.id = row.id
				} else {
					this.titles = '添加公告'
					this.title = ''
					this.url = ''
					this.id = ''
				}
				this.dialogFormVisibleG = true
			},
			refusetoBtn() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入公告内容',
						type: 'warning'
					});
					return
				}
				if (this.title.length > 36) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '公告内容最多可输入36字',
						type: 'warning'
					});
					return
				}
				if (this.titles == '添加公告') {
					var url = 'message/add'
				} else {
					var url = 'message/update'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'title': this.title,
						'id': this.id,
						'url': this.url,
						'state': 1
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleG = false
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
			// 删除信息
			deleteClick(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`message/delete/${delid}`),
						method: 'get',
						params: this.$http.adornData({})
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

		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style lang="scss" scoped>
.message-page {
	padding: 0;
	overflow-x: hidden;
}

.message-page__hero {
	margin-bottom: 12px;
}

.message-page__tabs-wrap {
	margin-bottom: 0;
}

.message-page__tabs {
	margin-bottom: 0;
}

.message-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.message-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.message-page__table-card >>> .el-table {
	border-radius: 0;
}

.message-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.message-page__table >>> .el-table__body td {
	background: #fff;
}

.message-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
