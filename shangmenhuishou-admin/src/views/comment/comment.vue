<template>
	<div class="comment-page">
		<div class="nf-page-hero comment-page__hero">
			<span class="nf-page-hero__badge">评价管理</span>
			<h2 class="nf-page-hero__title">评价中心</h2>
			<p class="nf-page-hero__desc">查看与管理用户评价，支持按审核状态、用户、订单号及评论内容筛选，可审核与删除评论</p>
		</div>

		<div class="nf-filter-card comment-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body comment-page__filter-body">
				<div class="comment-page__field">
					<span class="comment-page__label">审核状态：</span>
					<el-select v-model="status" placeholder="请选择审核状态" @change="select" class="comment-page__select">
						<el-option v-for="item in statusSe" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="comment-page__field">
					<span class="comment-page__label">评论用户：</span>
					<el-input class="comment-page__input" @keydown.enter.native="select" clearable placeholder="请输入评论用户"
						v-model="userName"></el-input>
				</div>
				<div class="comment-page__field">
					<span class="comment-page__label">被评用户：</span>
					<el-input class="comment-page__input" @keydown.enter.native="select" clearable placeholder="请输入被评用户"
						v-model="riderUserName"></el-input>
				</div>
				<div class="comment-page__field">
					<span class="comment-page__label">订单号：</span>
					<el-input class="comment-page__input" @keydown.enter.native="select" clearable placeholder="请输入订单号"
						v-model="goodsName"></el-input>
				</div>
				<div class="comment-page__field">
					<span class="comment-page__label">评论内容/关键字：</span>
					<el-input class="comment-page__input comment-page__input--wide" @keydown.enter.native="select" clearable
						placeholder="请输入评论内容/关键字" v-model="content"></el-input>
				</div>
				<div class="comment-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<div class="comment-page__tabs-wrap">
			<el-tabs v-model="activeName" class="comment-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="评论列表" name="first">
					<div class="nf-table-card comment-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records" class="comment-page__table">
							<el-table-column prop="evaluateId" label="编号" width="80">
							</el-table-column>
							<el-table-column prop="userName" label="评论用户" width="150">
								<template slot-scope="scope">
									<span class="comment-page__link" @click="updates(scope.row.userId)">{{
										scope.row.userName ? scope.row.userName :
										'未绑定' }}</span>
								</template>
							</el-table-column>
							<el-table-column prop="riderUserName" label="被评用户" width="150">
								<template slot-scope="scope">
									<span class="comment-page__link" @click="updates(scope.row.riderUserId)">{{
										scope.row.riderUserName ? scope.row.riderUserName
										: '未绑定' }}</span>
								</template>
							</el-table-column>
							<el-table-column prop="indentNumber" label="订单号">
							</el-table-column>
							<el-table-column prop="evaluateMessage" label="信息内容">
							</el-table-column>
							<el-table-column prop="evaluateImg" label="评论图片">
								<template slot-scope="scope">
									<div v-if="scope.row.evaluateImg">
										<span v-for="(item, index) in scope.row.evaluateImg.split(',')" :key="index">
											<el-popover placement="top-start" title="" trigger="hover">
												<img class="comment-page__thumb" :src="item" alt="" slot="reference">
												<img class="comment-page__preview" :src="item" alt="">
											</el-popover>
										</span>
									</div>
								</template>
							</el-table-column>
							<el-table-column prop="satisfactionFlag" label="评分" width="80">
							</el-table-column>
							<el-table-column prop="remarks" label="审核备注" width="80">
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="150">
							</el-table-column>
							<el-table-column prop="status" fixed="right" label="状态" width="100">
								<template slot-scope="scope">
									{{ scope.row.status == 0 ? '待审核' : (scope.row.status == 1 ? '已通过' : '已拒绝') }}
								</template>
							</el-table-column>
							<el-table-column fixed="right" label="操作" width="150">
								<template slot-scope="scope">
									<el-button size="mini" v-if="scope.row.status == 0" type="primary"
										:disabled="!isAuth('comment:check')" @click="shenhe(scope.row)">审核
									</el-button>
									<el-button size="mini" type="danger" :disabled="!isAuth('comment:delete')"
										@click="deletes(scope.row)">删除
									</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="comment-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
								layout="total,sizes, prev, pager, next" :total="tableData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 回复 -->
		<el-dialog title="回复" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="回复内容：" :label-width="formLabelWidth">
					<el-input v-model="form.reply" type="textarea" :rows="4" style="width:65%;" placeholder="请输入回复内容">
					</el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 审核弹窗 -->
		<el-dialog title="审核" :visible.sync="dialogVisiblesh" center>
			<div>
				<span style="width: 200px;text-align: right;display: inline-block;">审核类型：</span>
				<span>
					<el-radio-group v-model="radio">
						<el-radio :label="1">通过</el-radio>
						<el-radio :label="2">拒绝</el-radio>
					</el-radio-group>
				</span>
			</div>
			<div>
				<span style="width: 200px;text-align: right;display: inline-block;position: relative;top: -70px;">审核原因：</span>
				<el-input v-model="remarkssh" style="margin-top: 5px;display: inline-block;width: 50%;" type="textarea" :rows="4"
					placeholder="请输入审核原因"></el-input>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisiblesh = false">取 消</el-button>
				<el-button type="primary" @click="startCheck()">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
export default {
	data() {
		return {
			status:'',
			statusSe: [{
				value: '',
				label: '全部'
			}, {
				value: 0,
				label: '待审核'
			}, {
				value: 1,
				label: '已通过'
			}, {
				value: 2,
				label: '已拒绝'
			}],
			radio: 1,
			dialogVisiblesh: false,
			size: 10,
			page: 1,
			goodsId: '',
			form: {
				commentId: '',
				reply: ''
			},
			scoreType: 0,
			scoreTypes: [{
				value: 0,
				label: '全部'
			},
			{
				value: 1,
				label: '好评'
			},
			{
				value: 2,
				label: '中评'
			},
			{
				value: 3,
				label: '差评'
			}
			],
			formLabelWidth: '200px',
			tableDataLoading: false,
			dialogFormVisible: false,
			tableData: [],
			content: '',
			title: '',
			userName: '',
			phone: '',
			activeName: 'first',
			orderNumber: '',
			goodsName: '',
			shopName: '',
			riderUserName: '',
			evaluateId: '',//评论id
			remarkssh: '',//审核原因
		}
	},
	methods: {
		//审核
		startCheck() {
			if(this.radio==2){
				if (this.remarkssh == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入审核原因',
						type: 'warning'
					});
					return
				}
			}
			
				let info = {
					evaluateId: this.evaluateId,
					status: this.radio,
					remarks: this.remarkssh
				}
				this.$http({
					url: this.$http.adornUrl('admin/evaluate/examine'),
					method: 'get',
					params: this.$http.adornParams(info)
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
						this.dialogVisiblesh = false
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
							}
						})
					}

				})
		},
		//审核弹窗
		shenhe(info, type) {
			this.evaluateId = info.evaluateId
			this.remarkssh = ''
			this.dialogVisiblesh = true
		},
		// 返回上一级
		prev() {
			this.$router.back()
		},
		handleSizeChange(val) {
			this.size = val;
			this.dataSelect()
		},
		handleCurrentChange(val) {
			this.page = val;
			this.dataSelect()
		},
		handleSizeChangeDt(val) {
			this.size = val;
			this.dataSelectDt()
		},
		handleCurrentChangeDt(val) {
			this.page = val;
			this.dataSelectDt()
		},
		// tabs切换
		handleClick(tab, event) {
			this.phone = ''
			this.page = 1
			this.userName = ''
			if (tab._props.label == '评论列表') {
				this.dataSelect()
			}
			if (tab._props.label == '动态评论') {
				this.dataSelectDt()
			}
		},
		// 刷新
		refresh() {
			this.dataSelect()
		},
		//搜索
		select() {
			this.page = 1
			this.dataSelect()
		},
		// 重置
		cleans() {
			this.title = ''
			this.userName = ''
			this.phone = ''
			this.content = ''
			this.orderNumber = ''
			this.goodsName = ''
			this.shopName = ''
			this.riderUserName = ''
			this.status = ''
			this.page = 1
			this.dataSelect()
		},
		// 刷新
		refreshDt() {
			this.dataSelectDt()
		},
		//搜索
		selectDt() {
			this.page = 1
			this.dataSelectDt()
		},
		// 重置
		cleansDt() {
			this.userName = ''
			this.phone = ''
			this.content = ''
			this.page = 1
			this.dataSelectDt()
		},
		// 回复
		replyClick(row) {
			this.dialogFormVisible = true
			this.form.commentId = row.commentId
			this.form.reply = row.reply
		},
		replyNoticeTo() {
			this.$http({
				url: this.$http.adornUrl('selfGoodsComment/reply'),
				method: 'get',
				params: this.$http.adornParams({
					'commentId': this.form.commentId,
					'reply': this.form.reply,
				})
			}).then(({
				data
			}) => {
				this.$message({
					message: '回复成功',
					type: 'success',
					duration: 1500,
					onClose: () => {
						this.dataSelect()
					}
				})
				this.dialogFormVisible = false
			})
		},
		// 删除评论
		deletes(row) {
			this.$confirm(`确定删除此条信息?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http({
					url: this.$http.adornUrl('admin/evaluate/deleteEvaluate'),
					method: 'get',
					params: this.$http.adornParams({
						'evaluateId': row.evaluateId
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
							onClose: () => { }
						})
					}
				})
			}).catch(() => { })
		},
		// 获取服务评论数据
		dataSelect() {
			this.tableDataLoading = true
			this.$http({
				url: this.$http.adornUrl('admin/evaluate/getUserEvaluate'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.size,
					'userName': this.userName,
					'riderUserName': this.riderUserName,
					'evaluateMessage': this.content,
					'status':this.status,
					// 'phone': this.orderNumber,
					'indentNumber': this.goodsName,
					// 'shopName': this.shopName,
					// 'userId': '',
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data;
				this.tableData = returnData
			})
		},
		// 获取动态评论
		dataSelectDt() {
			this.tableDataLoading = true
			this.$http({
				url: this.$http.adornUrl('app/gambit/selectCommentPageAdmin'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.size,
					'userName': this.userName,
					'content': this.content,
					'phone': this.phone
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data;
				this.tableData = returnData
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
		// 商品跳转
		toGoods(title) {
			this.$router.push({
				path: '/shopAdmin1',
				query: {
					title: title
				}
			})
		},
		// 删除动态评论
		pldeletes(row) {
			this.$confirm(`确定删除此条信息?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http({
					url: this.$http.adornUrl(`gambit/deleteCommentById`),
					method: 'post',
					params: this.$http.adornParams({
						'commentId': row.commentId
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '删除成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelectDt()
						}
					})
				})
			})
		},
	},
	activated() {
		this.dataSelect()
	}
};
</script>

<style lang="scss" scoped>
.comment-page {
	padding: 0;
	overflow-x: hidden;
}

.comment-page__hero {
	margin-bottom: 12px;
}

.comment-page__filter {
	margin-bottom: 12px;
}

.comment-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.comment-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.comment-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.comment-page__input {
	width: 150px;
}

.comment-page__input--wide {
	width: 200px;
}

.comment-page__select {
	width: 150px;
}

.comment-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.comment-page__tabs-wrap {
	margin-bottom: 0;
}

.comment-page__tabs {
	margin-bottom: 0;
}

.comment-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.comment-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.comment-page__table-card >>> .el-table {
	border-radius: 0;
}

.comment-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.comment-page__table >>> .el-table__body td {
	background: #fff;
}

.comment-page__link {
	color: #01af74;
	cursor: pointer;
}

.comment-page__link:hover {
	color: #017c52;
}

.comment-page__thumb {
	width: 50px;
	height: 50px;
	margin: 3px;
	border-radius: 6px;
	object-fit: cover;
}

.comment-page__preview {
	width: 200px;
	height: 200px;
	border-radius: 6px;
	object-fit: cover;
}

.comment-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
