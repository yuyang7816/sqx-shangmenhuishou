<template>
	<div class="mission-details-page">
		<div class="mission-details-page__nav">
			<a href="#" class="mission-details-page__back-link" @click.prevent="prev">
				<icon-svg name="jiantou" class="mission-details-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="mission-details-page__nav-sep">|</span>
			<span class="mission-details-page__nav-title">派单详情</span>
		</div>

		<div class="nf-page-hero mission-details-page__hero">
			<span class="nf-page-hero__badge">服务管理</span>
			<h2 class="nf-page-hero__title">派单详情</h2>
			<p class="nf-page-hero__desc">查看派单基本信息与接单记录，支持查看提交验证图</p>
		</div>

		<div class="nf-filter-card mission-details-page__info-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-document"></i>
				<span>派单信息</span>
			</div>
			<div class="nf-filter-card__body mission-details-page__info-body">
				<div class="mission-details-page__detail-table">
					<table>
						<tbody>
							<tr>
								<th>用户名称</th>
								<td>{{ tableData.nickName }}</td>
								<th>标题</th>
								<td>{{ tableData.title }}</td>
								<th class="mission-details-page__col-wide">服务要求</th>
								<td class="border-rt">{{ tableData.content }}</td>
							</tr>
							<tr>
								<th>验证信息</th>
								<td>{{ tableData.verifyContent }}</td>
								<th>打开内容</th>
								<td class="mission-details-page__col-wide">{{ tableData.openContent }}</td>
								<th>编号</th>
								<td class="border-rt">{{ tableData.id }}</td>
							</tr>
							<tr>
								<th>数量</th>
								<td>{{ tableData.taskNum }}</td>
								<th>接单数量</th>
								<td>{{ tableData.endNum }}</td>
								<th>发布时间</th>
								<td class="border-rt">{{ tableData.createTime }}</td>
							</tr>
							<tr>
								<th>截至时间</th>
								<td>{{ tableData.endTime }}</td>
								<th>审核时间（按分）</th>
								<td>{{ tableData.auditTime }}</td>
								<th>服务限时（按分）</th>
								<td class="border-rt">{{ tableData.restrictTime }}</td>
							</tr>
							<tr>
								<th>状态</th>
								<td v-if="tableData.state == 0">待审核</td>
								<td v-if="tableData.state == 1">进行中</td>
								<td v-if="tableData.state == 2">已结束</td>
								<td v-if="tableData.state == 3">已拒绝</td>
								<td v-if="tableData.state == 4">已结算</td>
								<th>打开方式</th>
								<td>
									<span v-if="tableData.openType == 1">链接打开</span>
									<span v-if="tableData.openType == 2">口令打开</span>
									<span v-if="tableData.openType == 3">扫码打开</span>
								</td>
								<th>打开app分类</th>
								<td class="border-rt">{{ tableData.openApp }}</td>
							</tr>
							<tr>
								<th class="border-bt">验证信息</th>
								<td class="border-bt">{{ tableData.verifyContent }}</td>
								<th class="border-bt">价格(原价)</th>
								<td class="border-bt">{{ tableData.taskOriginalPrice }}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div class="nf-table-card mission-details-page__table-card">
			<div class="mission-details-page__table-head">
				<i class="el-icon-s-order"></i>
				<span>接单列表</span>
			</div>
			<el-table v-loading="tableDataLoading" :data="taskData.list" class="mission-details-page__table">
				<el-table-column fixed prop="id" label="接单id">
				</el-table-column>
				<el-table-column prop="nickName" label="接单人名称" width="150">
				</el-table-column>
				<el-table-column prop="content" label="提交内容" width="150">
					<template slot-scope="scope">
						<span>{{ scope.row.content }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="money" label="价格(接单时收取的价格)" width="180">
				</el-table-column>
				<el-table-column prop="zhiMoney" label="直属收益">
				</el-table-column>
				<el-table-column prop="feiMoney" label="非直属收益" width="150">
				</el-table-column>
				<el-table-column prop="pingMoney" label="平台收益">
				</el-table-column>
				<el-table-column prop="auditContent" label="拒绝原因" width="160">
				</el-table-column>
				<el-table-column prop="category" label="拒绝类型" width="160">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="auditTime" label="审核时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span class="mission-details-page__status" v-if="scope.row.state === 0">接单成功</span>
						<span class="mission-details-page__status" v-if="scope.row.state === 1">提交待审核</span>
						<span class="mission-details-page__status" v-if="scope.row.state === 2">审核成功</span>
						<span class="mission-details-page__status" v-if="scope.row.state === 3">已拒绝</span>
						<span class="mission-details-page__status" v-if="scope.row.state === 4">已维权</span>
						<span class="mission-details-page__status" v-if="scope.row.state === 5">已放弃</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="check(scope.$index, scope.row)">查看
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="mission-details-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="taskData.totalCount">
				</el-pagination>
			</div>
		</div>

		<el-dialog title="查看" :visible.sync="dialogFormVisible" center>
			<div v-for="(item, index) in helpTaskData" :key="index" class="mission-details-page__dialog-step">
				<span>步骤 {{ index + 1 }}</span>
				<p class="mission-details-page__dialog-label">验证图</p>
				<img :src="item.picture" alt="" width="260" height="460">
			</div>
			<div v-if="helpTaskData == ''" class="mission-details-page__dialog-empty">暂无内容</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				state: 0,
				limit: 5,
				page: 1,
				dialogFormVisible: false,
				tableDataLoading: true,
				tableData: {},
				taskData: [],
				helpTaskData: []
			}
		},
		methods: {
			prev() {
				this.$router.back()
			},
			handleSizeChange(val) {
				this.limit = val
				this.taskSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.taskSelect()
			},
			check(index, rows) {
				this.$http({
					url: this.$http.adornUrl('helpTask/selectSendOrderDetailsById'),
					method: 'get',
					params: this.$http.adornParams({
						'id': rows.id
					})
				}).then(({ data }) => {
					if (data.code === 0) {
						let returnData = data.data
						this.dialogFormVisible = true
						this.helpTaskData = returnData
					}
				})
			},
			dataSelect() {
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTask/selectTakeOrderDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'id': id
					})
				}).then(({ data }) => {
					if (data.code === 0) {
						let returnData = data.data
						this.tableData = returnData
					}
				})
			},
			taskSelect() {
				this.tableDataLoading = true
				let page = this.page - 1
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTask/selectSendOrderByTaskList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.limit,
						'id': id
					})
				}).then(({ data }) => {
					if (data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.taskData = returnData
					}
				})
			}
		},
		mounted() {
			this.dataSelect()
			this.taskSelect()
		}
	}
</script>

<style lang="scss" scoped>
.mission-details-page {
	padding: 0;
	overflow-x: hidden;
}

.mission-details-page__nav {
	display: flex;
	align-items: center;
	margin-bottom: 12px;
	font-size: 14px;
	color: var(--nf-text-primary, #1a2433);
}

.mission-details-page__back-link {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	color: var(--nf-brand, #01af74);
	text-decoration: none;
	cursor: pointer;
}

.mission-details-page__back-link:hover {
	color: var(--nf-brand-deep, #017c52);
}

.mission-details-page__back-icon {
	width: 1.2em;
	height: 1.2em;
}

.mission-details-page__nav-sep {
	margin: 0 12px;
	color: var(--nf-border, #e8eaed);
}

.mission-details-page__nav-title {
	font-size: 16px;
	font-weight: 600;
}

.mission-details-page__hero {
	margin-bottom: 12px;
}

.mission-details-page__info-card {
	margin-bottom: 12px;
	padding: 16px;
}

.mission-details-page__info-body {
	padding: 0;
}

.mission-details-page__detail-table {
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-sm, 6px);
	overflow: hidden;
}

.mission-details-page__detail-table table {
	width: 100%;
	border-collapse: collapse;
}

.mission-details-page__detail-table tr {
	border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.mission-details-page__detail-table tr:last-child {
	border-bottom: none;
}

.mission-details-page__detail-table th {
	background: var(--nf-page-bg, #f4f6fa);
	padding: 12px 16px;
	border-right: 1px solid var(--nf-border, #e8eaed);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
	font-size: 13px;
	line-height: 1.5;
	white-space: nowrap;
}

.mission-details-page__detail-table td {
	padding: 12px 16px;
	border-right: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
	color: var(--nf-text-primary, #1a2433);
	font-size: 13px;
	line-height: 1.5;
	word-break: break-all;
}

.mission-details-page__col-wide {
	width: 180px;
}

.border-rt {
	border-right: none !important;
}

.border-bt {
	border-bottom: none !important;
}

.mission-details-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.mission-details-page__table-head {
	display: flex;
	align-items: center;
	gap: 8px;
	padding: 12px 16px;
	font-size: 14px;
	font-weight: 600;
	color: var(--nf-text-primary, #1a2433);
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.mission-details-page__table-card >>> .el-table {
	border-radius: 0;
}

.mission-details-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.mission-details-page__table >>> .el-table__body td {
	background: #fff;
}

.mission-details-page__status {
	color: var(--nf-brand, #01af74);
}

.mission-details-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.mission-details-page__dialog-step {
	text-align: center;
	margin-bottom: 16px;
}

.mission-details-page__dialog-label {
	color: #f56c6c;
	margin: 8px 0;
}

.mission-details-page__dialog-empty {
	text-align: center;
	color: var(--nf-text-secondary, #5a6578);
}
</style>
