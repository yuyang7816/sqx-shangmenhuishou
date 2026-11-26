<template>
	<div>

		<div style="width: 100%;text-align: right;">
			<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="select()">刷新
			</el-button>
			<el-button style="margin:10px;" size="mini" type="primary" icon="document"
				:disabled="!isAuth('classification:add')" @click="classAdd(0)">添加
			</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData" row-key="classifyId" default-expand-all>
			<el-table-column prop="classifyId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="classifyName" label="分类名称">
			</el-table-column>
			<el-table-column prop="img" label="分类图片">
				<template slot-scope="scope">
					　<img v-if="scope.row.img &&scope.row.img!=''" :src="scope.row.img" width="40" height="40" />
					<span v-else>暂无图片</span>
				</template>
			</el-table-column>
			<el-table-column prop="money" label="一口价价格">
			</el-table-column>
			<el-table-column prop="minOfferMoney" label="悬赏最低价">
			</el-table-column>
			<el-table-column prop="minQuoteMoney" label="报价最低价">
			</el-table-column>
			<el-table-column prop="minMasterMoney" label="师傅任务最低价">
			</el-table-column>
			
			<el-table-column prop="isEnable" label="状态">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.isEnable" @change="change(scope.row.classifyId,scope.row.isEnable)"
						:disabled="!isAuth('classification:update')" :active-value="openValue2"
						:inactive-value="closeValue2" active-color="#13ce66" inactive-color="#ff4949">
					</el-switch>
				</template>
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
		<!-- <div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
			</el-pagination>
		</div> -->

		<!-- 添加、修改服务分类 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible9" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类类型：</span>
				<el-select v-model="parentIdnum" placeholder="请选择分类类型" style="width:50%;" @change="parentIdnumBtn">
					<el-option v-for="item in classnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;" v-if="parentIdnum==2">
				<span style="width: 200px;display: inline-block;text-align: right;">上级分类：</span>
				<el-select v-model="parentId" style="width:50%;">
					<el-option v-for="item in classDatas" :key="item.classifyId" :label="item.classifyName" :value="item.classifyId">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务名称：</span>
				<el-input style="width:50%;" v-model="name" type="text" placeholder="请输入服务名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" min="0" placeholder="请输入服务排序"></el-input>
			</div>
			<div style="margin-bottom: 10px;display:flex;" v-if="parentIdnum==2">
				<span style="width: 200px;display: inline-block;text-align: right;">服务图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="img"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1" :on-progress="onprogress2">
						<!-- <el-progress type="circle" :percentage="100" status="success"></el-progress> -->
						<img v-if="img!=''" :src="img" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>

					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;" v-if="parentIdnum==2">
				<span style="width: 200px;display: inline-block;text-align: right;">一口价价格：</span>
				<el-input style="width:50%;" v-model="money" type="number" min="0" placeholder="请输入一口价价格"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="parentIdnum==2">
				<span style="width: 200px;display: inline-block;text-align: right;">悬赏最低价：</span>
				<el-input style="width:50%;" v-model="minOfferMoney" type="number" min="0" placeholder="请输入悬赏最低价">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="parentIdnum==2">
				<span style="width: 200px;display: inline-block;text-align: right;">报价最低价：</span>
				<el-input style="width:50%;" v-model="minQuoteMoney" type="number" min="0" placeholder="请输入报价最低价">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="parentIdnum==2">
				<span style="width: 200px;display: inline-block;text-align: right;">师傅任务最低价：</span>
				<el-input style="width:50%;" v-model="minMasterMoney" type="number" min="0" placeholder="请输入师傅任务最低价">
				</el-input>
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
				money: '',
				minOfferMoney: '',
				minQuoteMoney: '',
				classifyNames:'',
				minMasterMoney:'',
				sort:'',
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
					url: this.$http.adornUrl('admin/fixedClassify/getTreeClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'num': this.limit
						'classifyName':this.classifyNames
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
					this.titles = '修改服务分类'
					this.name = row.classifyName
					this.classifyId = row.classifyId
					this.sort = row.sort
					this.gameStatus = row.isEnable

					if (row.parentId == 0) {
						this.parentIdnum = 1
						this.img = ''
						this.money = ''
						this.minOfferMoney = ''
						this.minQuoteMoney = ''
						this.minMasterMoney = ''
					} else {
						this.parentIdnum = 2
						this.img = row.img
						this.money = row.money
						this.minOfferMoney = row.minOfferMoney
						this.minQuoteMoney = row.minQuoteMoney
						this.minMasterMoney = row.minMasterMoney
					}

					this.parentId = row.parentId
				} else {
					this.titles = '添加服务分类'
					this.name = ''
					this.classifyId = ''
					this.img = ''
					this.gameStatus = 1
					this.money = ''
					this.minOfferMoney = ''
					this.minQuoteMoney = ''
					this.minMasterMoney = ''
					this.parentIdnum = ''
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
				if (this.parentIdnum == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入选择分类类型',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入服务名称',
						type: 'warning'
					})
					return
				}
				if (this.parentIdnum == 2) {
					if (this.img == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请上传服务图片',
							type: 'warning'
						})
						return img
					}
					if (this.money === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入一口价价格',
							type: 'warning'
						})
						return
					}
					if (this.minOfferMoney == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入最低悬赏价格',
							type: 'warning'
						})
						return
					}
					if (this.minQuoteMoney == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入报价最低价',
							type: 'warning'
						})
						return
					}
					if (this.minMasterMoney == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入师傅任务最低价',
							type: 'warning'
						})
						return
					}
					
					
				}

				if (this.titles == '添加服务分类') {
					var urls = 'admin/fixedClassify/addClassify'
				} else {
					var urls = 'admin/fixedClassify/updateClassify'
				}
				if(this.parentId===0){
					var parentIds = ''
				}else{
					var parentIds = this.parentId
				}
				
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					params: this.$http.adornParams({
						'classifyName': this.name,
						'isEnable': this.gameStatus,
						'img': this.img,
						'classifyId': this.classifyId,
						'type': this.parentIdnum,
						'parentId': parentIds,
						'money': this.money,
						'minOfferMoney': this.minOfferMoney,
						'minQuoteMoney': this.minQuoteMoney,
						'sort':this.sort,
						'minMasterMoney':this.minMasterMoney,
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
						url: this.$http.adornUrl('admin/fixedClassify/deleteById'),
						method: 'get',
						params: this.$http.adornParams({
							'classifyId': row.classifyId
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
			// 获取分类数据
			classSelectList() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/fixedClassify/getTreeClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'classifyName': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classDatas = returnData
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

<style>
	.customWidth {
		width: 80% !important;
	}

	.adver_main.box {
		display: block;
		max-width: 100%;
		text-align: center;
		border: 1px dotted rgba(67, 79, 103, .4);
	}

	.cards {
		padding: 0 8px;
		margin-bottom: 15px;
	}

	.adver_main.box a {
		display: flex;
		justify-content: center;
		height: 150px;
		line-height: 150px;
		text-decoration: none
	}

	.bannerManin {
		border: 1px solid #e8e8e8;
		font-size: 14px;
		padding: 0 24px;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 113px;
		color: rgba(0, 0, 0, .65);
	}

	.bannerManin span {
		display: inline-block;
		margin-left: 5px;
	}

	.bannerManin img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
	}

	.bannerbtn {
		display: flex;
		border-top: none !important;
		border: 1px solid #e8e8e8;
		padding: 11px;
		font-size: 14px;
		color: #01af74;
	}

	.bannerbtn a {
		flex: 1;
		text-align: center;
		color: #01af74 !important;
		text-decoration: none;
	}

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}
</style>
