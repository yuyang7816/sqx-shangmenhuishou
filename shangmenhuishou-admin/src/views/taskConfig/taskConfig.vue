<template>
	<div class="task-config-page">
		<div class="nf-page-hero task-config-page__hero">
			<span class="nf-page-hero__badge">服务配置</span>
			<h2 class="nf-page-hero__title">服务配置</h2>
			<p class="nf-page-hero__desc">管理系统服务相关配置项，支持查看与编辑各项配置内容</p>
		</div>

		<div class="nf-filter-card task-config-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body task-config-page__filter-body">
				<el-button size="mini" type="primary" icon="el-icon-refresh" @click="dataSelect">刷新</el-button>
			</div>
		</div>

		<div class="nf-table-card task-config-page__table-card">
			<el-table v-loading="tableDataLoading" :data="renwuData" class="task-config-page__table">
				<el-table-column fixed prop="type" label="编号" align="center" width="80">
				</el-table-column>
				<el-table-column prop="min" label="类型">
				</el-table-column>
				<el-table-column prop="value" label="内容" width="500">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
							@click="renwuAmend(scope.$index, scope.row)">编辑
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- 修改服务弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form1">
				<el-form-item label="配置类型：" :label-width="formLabelWidth">
					<el-input v-model="form1.min" style="width:65%;" readonly></el-input>
				</el-form-item>
				<el-form-item label="内容：" :label-width="formLabelWidth">
					<el-input v-model="form1.value" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="renwuNoticeTo()">确 定</el-button>
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
				state: 1,
				openValue: 1,
				closeValue: 2,
				classify: 1,
				money: '',
				condition: 'renwu',
				activeName: 'first',
				dialogFormVisible: false,
				tableDataLoading: true,
				dialogFormVisible1: false,
				formLabelWidth: '200px',
				tableData: [],
				renwuData: [],
				form: {
					id: '',
					rate: ''
				},
				form1: {
					id: '',
					min: '',
					value: '',
					type: '',
				},
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect();
			},
			// 修改服务弹框
			renwuAmend(index, rows) {
				this.dialogFormVisible1 = true;
				this.form1.id = rows.id;
				this.form1.type = rows.type
				this.form1.min = rows.min;
				this.form1.value = rows.value;
				this.form1.max = rows.max;
				this.form1.createAt = rows.createAt;
				this.form1.conditionFrom = rows.conditionFrom
			},
			// 修改服务
			renwuNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form1.id,
						'type': this.form1.type,
						'value': this.form1.value,
						'min': this.form1.min,
						'max': this.form1.max,
						'createAt': this.form1.createAt,
						'conditionFrom': this.form1.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
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
						this.$message.error(data.msg)
					}
				})
			},
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`common/type/condition/${this.condition}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.renwuData = returnData
					}
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style lang="scss" scoped>
.task-config-page {
	padding: 0;
	overflow-x: hidden;
}

.task-config-page__hero {
	margin-bottom: 12px;
}

.task-config-page__filter {
	margin-bottom: 12px;
}

.task-config-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.task-config-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.task-config-page__table-card >>> .el-table {
	border-radius: 0;
}

.task-config-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.task-config-page__table >>> .el-table__body td {
	background: #fff;
}
</style>
