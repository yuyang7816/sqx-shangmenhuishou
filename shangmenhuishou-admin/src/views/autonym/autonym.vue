<template>
	<div class="autonym-page">
		<div class="nf-page-hero autonym-page__hero">
			<span class="nf-page-hero__badge">回收员管理</span>
			<h2 class="nf-page-hero__title">招募回收员</h2>
			<p class="nf-page-hero__desc">查看与管理回收员招募申请，支持按姓名、联系电话筛选查询</p>
		</div>

		<div class="nf-filter-card autonym-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body autonym-page__filter-body">
				<!-- <span>状态：</span>
				<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="select()">
					<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp; -->
				<div class="autonym-page__field">
					<span class="autonym-page__label">姓名：</span>
					<el-input class="autonym-page__input" @keydown.enter.native="select" placeholder="请输入姓名"
						v-model="userName">
					</el-input>
				</div>
				<div class="autonym-page__field">
					<span class="autonym-page__label">联系电话：</span>
					<el-input class="autonym-page__input" @keydown.enter.native="select" placeholder="请输入联系电话"
						v-model="shopNameT">
					</el-input>
				</div>
				<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
				<el-button size="mini" type="primary" icon="el-icon-refresh" @click="cleans">重置</el-button>
			</div>
		</div>

		<div class="nf-table-card autonym-page__table-card">
		<el-table v-loading="tableDataLoading" :data="tableData.records" class="autonym-page__table">
			<el-table-column prop="id" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="name" label="姓名" width="100">
				<template slot-scope="scope">
					<span class="autonym-page__link"
						@click="updates(scope.row)">{{scope.row.name ? scope.row.name : '未绑定'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="联系电话" width="150">
				<template slot-scope="scope">
					<span>{{scope.row.phone }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="age" label="年龄" width="160">
			</el-table-column>
			<el-table-column prop="city" label="城市">
			</el-table-column>
			<el-table-column prop="idCardFront" label="身份证正面" width="150">
				<template slot-scope="scope">
					<div v-if="scope.row.idCardFront == null || scope.row.idCardFront == ''">
						暂无图片
					</div>
					<div v-if="scope.row.idCardFront" style="display:flex;flex-wrap: wrap;">
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="scope.row.idCardFront" alt="" slot="reference">
							<img style="width: 200px; height: 200px" :src="scope.row.idCardFront" alt="">
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="idCardBack" label="身份证反面" width="150">
				<template slot-scope="scope">
					<div v-if="scope.row.idCardBack == null || scope.row.idCardBack == ''">
						暂无图片
					</div>
					<div v-if="scope.row.idCardBack" style="display:flex;flex-wrap: wrap;">
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="scope.row.idCardBack" alt="" slot="reference">
							<img style="width: 200px; height: 200px" :src="scope.row.idCardBack" alt="">
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="remek" label="审核内容" width="160">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div
							style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">
							{{scope.row.remek}}</div>
						<div style="width: 400px;height: auto;word-break: break-all;max-height: 500px;overflow: auto">{{scope.row.remek}}</div>
					</el-popover>
				</template>
			</el-table-column> -->
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<!-- <el-table-column fixed='right' prop="status" label="状态" width="100">
				<template slot-scope="scope">
					<span v-if="scope.row.status == 0">待审核</span>
					<span v-if="scope.row.status == 1">审核成功</span>
					<span v-if="scope.row.status == 2">已拒绝</span>
				</template>
			</el-table-column> -->
			<!-- <el-table-column fixed='right' label="操作" width="160">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="scope.row.status !== 0 || !isAuth('autonym:jujue')"  style="margin: 5px;"
						@click="refuseClick(scope.row)">
						拒绝
					</el-button>
					<el-button size="mini" type="primary"  style="margin: 5px;"
						:disabled="scope.row.status !== 0 || !isAuth('autonym:tongguo')" @click="passClicks(scope.row)">
						通过
					</el-button>
					<el-button :disabled="!isAuth('autonymPeople:update')" size="mini" type="primary"
						@click="updatesShop(scope.$index, scope.row)" style="margin: 5px;">修改
					</el-button>
					<el-button size="mini" type="warning" :disabled="!isAuth('autonymPeople:update')" style="margin: 5px;"
						@click="classDelete(scope.row)">删除
					</el-button>
				</template>
			</el-table-column> -->
		</el-table>
			<div class="autonym-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</div>
		<!-- 审核拒绝弹框 -->
		<el-dialog title="审核拒绝" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入拒绝理由,文字在100以内">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改信息 -->
		<el-dialog :visible.sync="dialogFormVisibleXX" center title="修改" width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">企业名称：</span>
				<el-input v-model="name" placeholder="请输入企业名称" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
				<el-input v-model="phone" placeholder="请输入手机号" style="width:50%;"></el-input>
			</div>
			<div style="display: flex;align-items: center;margin: 2% 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺LOGO：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="shopImg" :action="$http.adornUrl('file/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess3">
						<img v-if="shopImg!=''&&shopImg!=null" :src="shopImg" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="display: flex;align-items: center;margin: 2% 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">营业执照：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="front" :action="$http.adornUrl('file/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess1">
						<img v-if="front!=''&&front!=null" :src="front" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺主页图：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in addressImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear1(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrl('file/upload')" list-type="picture-card"
							:show-file-list="false" :on-success="handleUploadSuccess3" :on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1"></el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺名称：</span>
				<el-input v-model="shopName" placeholder="请输入店铺名称" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺类型：</span>
				<el-input v-model="shopType" placeholder="请输入店铺类型" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">开店时间：</span>
				<el-time-select v-model="openTime" :picker-options="{
			  start: '00:00',
			  step: '00:15',
			  end: '24:00'
			}" placeholder="选择开店时间" style="width:50%;">
				</el-time-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">闭店时间：</span>
				<el-time-select v-model="closeTime" :picker-options="{
			  start: '00:00',
			  step: '00:15',
			  end: '24:00'
			}" placeholder="选择闭店时间" style="width:50%;">
				</el-time-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺地址：</span>
				<el-input v-model="address" placeholder="请输入店铺地址" type="text" style="width:50%;"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="onLine">
					<el-radio :label="1">上线</el-radio>
					<el-radio :label="2">下线</el-radio>
				</el-radio-group>
			</div> -->
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleXX = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
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
				content: '',
				status: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					}, {
						value: 1,
						label: '审核成功'
					}, {
						value: 2,
						label: '拒绝'
					}
				],
				userName: '',
				shopNameT: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				checkBoxData: [], //多选框选择的值
				shopId: '',

				dialogFormVisibleXX: false,
				shopName1: '',
				isActivity: '',
				region: '',
				userIdList: '',
				name: '',
				shopImg: '',
				phone: '',
				idNumber: '',
				front: '',
				cardBack: '',
				label: [],
				bq: '',
				shopName: '',
				shopType: '',
				details: '',
				score: '',
				address: '',
				shopUserId: '',
				onLine: 1,
				addressImg: [],
				percentage1: 0,
				openTime: '',
				closeTime: '',
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			//服务通过
			passClick(id) {
				this.status = 1
				this.content = '同意'
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
					url: this.$http.adornUrl(
						`student/auditStudentAuthentication/${ids}/${this.status}/${this.content}`),
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
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					}
				})
			},
			//单条服务通过
			passClicks(row) {
				this.status = 1
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl(`userCertification/auditorUserCertification`),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'status': this.status,
						'id': row.id,
						'remek': '通过'
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
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					}
				})
			},
			//服务拒绝
			refuseClick(rows) {
				console.log('rows', rows)
				this.status = 2
				this.shopId = rows.id
				this.helpTakeId = rows.userId
				this.content = ''
				this.dialogFormVisible = true
			},
			refuseto() {

				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				if (this.content.length > 100) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请拒绝理由不能超过100字',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl(`userCertification/auditorUserCertification`),
					method: 'post',
					data: this.$http.adornData({
						'status': this.status,
						'id': this.shopId,
						'remek': this.content
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
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.status = ''
				this.userName = ''
				this.shopNameT = ''
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/recruitment/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'status': this.status,
						'name': this.userName,
						'phone': this.shopNameT,
						'city': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].addressImg) {
							data.data.list[i].addressImg = data.data.list[i].addressImg.split(',')
						}
					}
					this.tableData = data.data
				})
				// let returnData = data.data;
			},
			// 修改信息
			releasNoticeTo() {
				if (this.shopImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传店铺LOGO',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入企业名称',
						type: 'warning'
					})
					return
				}
				if (this.phone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入手机号',
						type: 'warning'
					})
					return
				}
				if (this.shopName === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入店铺名称',
						type: 'warning'
					})
					return
				}
				if (this.shopType === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入店铺类型',
						type: 'warning'
					})
					return
				}
				if (this.address == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入店铺地址',
						type: 'warning'
					})
					return
				}
				if (this.openTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择开店时间',
						type: 'warning'
					})
					return
				}
				if (this.closeTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择闭店时间',
						type: 'warning'
					})
					return
				}

				var datas = {}
				datas.id = this.shopId
				datas.name = this.name
				datas.shopImg = this.shopImg
				datas.idNumber = this.idNumber
				datas.phone = this.phone
				datas.shopType = this.shopType
				datas.score = this.score
				datas.endTime = this.closeTime
				datas.address = this.address
				datas.details = this.details
				datas.front = this.front
				datas.addressImg = this.addressImg.toString()
				datas.userId = this.shopUserId;
				datas.shopName = this.shopName;
				datas.startTime = this.openTime
				datas.endTime = this.closeTime
				datas.label = this.label.toString()
				this.$http({
					url: this.$http.adornUrl('userCertification/updateCertification'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleXX = false
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
			// 修改弹框
			updatesShop(index, rows) {
				this.shopId = rows.id
				this.name = rows.name

				this.phone = rows.phone
				this.idNumber = rows.idNumber
				this.onLine = rows.onLine
				if (rows.label) {
					this.label = rows.label.split(',')
				}
				this.address = rows.address
				if (rows.shopImg) {
					this.shopImg = rows.shopImg
				} else {
					this.shopImg = ''
				}
				if (rows.front) {
					this.front = rows.front
				} else {
					this.front = ''
				}
				if (rows.addressImg) {
					this.addressImg = rows.addressImg
				} else {
					this.addressImg = []
				}
				this.percentage1 = 0
				this.shopName = rows.shopName;
				this.shopType = rows.shopType;
				this.score = rows.score;
				this.details = rows.details
				this.openTime = rows.startTime
				this.closeTime = rows.endTime
				this.shopUserId = rows.userId
				this.dialogFormVisibleXX = true

			},
			// 添加标签
			btnTj() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签名',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.label.push(this.bq)
					this.bq = ''
				}

			},
			// 删除标签
			dels1(index) {
				this.label.splice(index, 1);
				console.log(this.label)
			},
			// 营业执照
			handleAvatarSuccess1(file, fileList) {
				this.front = file.data.path;
			},
			// 身份证背面照
			handleAvatarSuccess2(file, fileList) {
				this.cardBack = file.data.path;
			},
			// 头像
			handleAvatarSuccess3(file, fileList) {
				this.shopImg = file.data.path;
			},
			//上传成功
			handleUploadSuccess3(file, fileList) {
				this.addressImg.push(file.data.path)
				console.log('this.contentImg', this.addressImg)
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			// 删除
			clear1(index) {
				this.addressImg.splice(index, 1);
			},
			// 删除实名认证
			classDelete(row) {
				this.$confirm(`确定要删除该信息吗`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('userCertification/deleteCertification'),
						method: 'post',
						params: this.$http.adornParams({
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
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style lang="scss" scoped>
.autonym-page {
	padding: 0;
	overflow-x: hidden;
}

.autonym-page__hero {
	margin-bottom: 12px;
}

.autonym-page__filter {
	margin-bottom: 12px;
}

.autonym-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.autonym-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.autonym-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.autonym-page__input {
	width: 200px;
}

.autonym-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.autonym-page__table-card >>> .el-table {
	border-radius: 0;
}

.autonym-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.autonym-page__table >>> .el-table__body td {
	background: #fff;
}

.autonym-page__link {
	color: var(--nf-brand, #01af74);
	cursor: pointer;
}

.autonym-page__link:hover {
	color: var(--nf-brand-deep, #017c52);
}

.autonym-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>

<style>
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
</style>