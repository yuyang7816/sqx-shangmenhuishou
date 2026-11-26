<template>
	<div class="autonym-people-page">
		<div class="nf-page-hero autonym-people-page__hero">
			<span class="nf-page-hero__badge">用户认证</span>
			<h2 class="nf-page-hero__title">个人认证</h2>
			<p class="nf-page-hero__desc">查看与管理用户个人实名认证申请，支持按状态、姓名、电话筛选及审核操作</p>
		</div>

		<div class="nf-filter-card autonym-people-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body autonym-people-page__filter-body">
				<div class="autonym-people-page__field">
					<span class="autonym-people-page__label">状态：</span>
					<el-select v-model="statusT" class="autonym-people-page__select" @change="select()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="autonym-people-page__field">
					<span class="autonym-people-page__label">姓名：</span>
					<el-input class="autonym-people-page__input" @keydown.enter.native="select" placeholder="请输入姓名"
						v-model="userNameT">
					</el-input>
				</div>
				<div class="autonym-people-page__field">
					<span class="autonym-people-page__label">电话：</span>
					<el-input class="autonym-people-page__input" @keydown.enter.native="select" placeholder="请输入电话"
						v-model="contactT">
					</el-input>
				</div>
				<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
				<el-button size="mini" type="primary" icon="el-icon-refresh" @click="cleans">重置</el-button>
			</div>
		</div>

		<div class="nf-table-card autonym-people-page__table-card">
		<el-table v-loading="tableDataLoading" :data="tableData.records" class="autonym-people-page__table">
			<el-table-column prop="id" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="name" label="姓名" width="100">
				<template slot-scope="scope">
					<span class="autonym-people-page__link"
						@click="updates(scope.row)">{{scope.row.name ? scope.row.name : '未绑定'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="contact" label="电话" width="150">
			</el-table-column>
			<el-table-column prop="gender" label="性别" width="80">
				<template slot-scope="scope">
					<div v-if="scope.row.gender==1">男</div>
					<div v-else>女</div>
				</template>
			</el-table-column>
			<el-table-column prop="birthday" label="生日" width="150">
			</el-table-column>
			<el-table-column prop="idCard" label="身份证号码" width="200">
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
			<!-- <el-table-column prop="credential" label="证书" width="250">
				<template slot-scope="scope">
					<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.credential">
						<div v-for="(item,index) in scope.row.credential.split(',')" :key="index">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
								<img style="width: 200px; height: auto;" :src="item" alt="">
							</el-popover>
						</div>
					</div>
				</template>
			</el-table-column> -->
			<el-table-column prop="reason" label="审核内容" width="160">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div
							style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;" slot="reference">
							{{scope.row.reason}}</div>
						<div style="width: 400px;height: auto;word-break: break-all;max-height: 500px;overflow: auto">{{scope.row.reason}}</div>
					</el-popover>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column fixed='right' prop="status" label="状态" width="100">
				<template slot-scope="scope">
					<span v-if="scope.row.status == 1">待审核</span>
					<span v-if="scope.row.status == 2">审核成功</span>
					<span v-if="scope.row.status == 3">已拒绝</span>
				</template>
			</el-table-column>
			<el-table-column fixed='right' label="操作" width="160">
				<template slot-scope="scope">   <!--  || !isAuth('autonympop:tongguo')|| !isAuth('autonympop:jujue') -->
					<el-button size="mini" type="primary" :disabled="scope.row.status !== 1 "
						@click="refuseClick(scope.row)" style="margin: 5px;">
						拒绝
					</el-button>
					<el-button size="mini" type="primary" style="margin: 5px;"
						:disabled="scope.row.status != 1" @click="passClicks(scope.row)">
						通过
					</el-button>
					<el-button  size="mini" type="primary"
						@click="updatesShop(scope.$index, scope.row)" style="margin: 5px;">修改
					</el-button>
					<el-button size="mini" type="warning" style="margin: 5px;"
						@click="classDelete(scope.row)">删除
					</el-button>
				</template>
			</el-table-column>
		</el-table>
			<div class="autonym-people-page__pagination">
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
				<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
				<el-input v-model="name" placeholder="请输入姓名" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
				<el-input v-model="phone" placeholder="请输入手机号" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">性别：</span>
				<el-radio-group v-model="gender">
					<el-radio :label="1">男</el-radio>
					<el-radio :label="2">女</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">生日：</span>
				<!-- <el-input v-model="birthday" placeholder="请输入手机号" style="width:50%;"></el-input> -->
				<el-date-picker style="width: 50%;" v-model="birthday" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择日期">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证号：</span>
				<el-input v-model="idNumber" placeholder="请输入身份证号" style="width:50%;"></el-input>
			</div>
		<!-- <div style="display: flex;align-items: center;margin: 2% 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="avatar"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess3">
						<img v-if="avatar!=''&&avatar!=null" :src="avatar" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div> -->
			<div style="display: flex;align-items: center;margin: 2% 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证正面照：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="cardFront"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="cardFront!=''&&cardFront!=null" :src="cardFront" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="display: flex;align-items: center;margin: 2% 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证背面照：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="cardBack"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess2">
						<img v-if="cardBack!=''&&cardBack!=null" :src="cardBack" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<!-- <div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">证书：</span>
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
			</div> -->
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
				<span class="bqList" v-for="(item,index) in label" :key="index">{{item}}
					<span class="delss">
						<i class="el-icon-delete" @click="dels1(index)"></i>
					</span>
				</span>
				<el-input v-model="bq" style="width:200px;" placeholder="请输入标签"></el-input>
				<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
				</el-button>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">年龄：</span>
				<el-input v-model="age" placeholder="请输入年龄" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务次数：</span>
				<el-input v-model="serviceNum" placeholder="请输入服务次数" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">评分：</span>
				<el-input v-model="score" placeholder="请输入评分" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">个人介绍：</span>
				<el-input v-model="details" type="textarea" :rows="3" placeholder="请输入个人介绍" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">城市：</span>
				<el-input v-model="city"  placeholder="请输入城市"
					type="text" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">详细地址：</span>
				<el-input v-model="address"  placeholder="请输入详细地址"
					type="text" style="width:50%;"  @keydown.enter.native="selectDt"></el-input>
				<el-button type="primary" size="mini" plain @click="selectDt()">获取经纬度</el-button>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
				<el-input v-model="longitude"  placeholder="请输入经度" disabled
					type="text" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
				<el-input v-model="latitude"  placeholder="请输入纬度" disabled
					type="text" style="width:50%;"></el-input>
			</div> -->
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="onLine">
					<el-radio :label="1">上线</el-radio>
					<el-radio :label="2">下线</el-radio>
				</el-radio-group>
			</div> -->
			<!-- <div>
				<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
			</div> -->
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleXX = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	var geocoder, map, markersArray = [];
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
						value: 1,
						label: '待审核'
					}, {
						value: 2,
						label: '审核成功'
					}, {
						value: 3,
						label: '拒绝'
					}
				],
				statusT:'',
				userNameT: '',
				contactT:'',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				checkBoxData: [], //多选框选择的值
				shopId:'',
				
				dialogFormVisibleXX:false,
				shopName1: '',
				isActivity: '',
				region: '',
				userIdList: '',
				gender:1,
				name: '',
				avatar:'',
				phone:'',
				idNumber:'',
				cardFront: '',
				cardBack:'',
				label:[],
				bq:'',
				age:'',
				serviceNum:'',
				details:'',
				score:'',
				birthday:'',
				address:'',
				city:'',
				longitude:'',
				latitude:'',
				shopUserId:'',
				onLine: 1,
				addressImg: [],
				percentage1: 0,
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
				this.status = 2
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl('admin/userAuthentication/audit'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'status': this.status,
						'id': row.id,
						'reason': '通过'
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
				console.log('rows',rows)
				this.status = 3
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
					url: this.$http.adornUrl('admin/userAuthentication/audit'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'status': this.status,
						'id': this.shopId,
						'reason': this.content
					})
				}).then(({
					data
				}) => {
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
				this.statusT= ''
				this.userNameT= ''
				this.contactT= ''
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/userAuthentication/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'status': this.statusT,
						'name': this.userNameT,
						'contact': this.contactT,
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
				// if (this.avatar == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传头像',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入姓名',
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
				if (this.idNumber == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入身份证号',
						type: 'warning'
					})
					return
				}
				if (this.gender === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择性别',
						type: 'warning'
					})
					return
				}
				// if (this.serviceNum === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入服务次数',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.score === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入评分',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				
				// if (this.city == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入城市',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.address == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入地址',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.longitude == ''||this.latitude == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '经纬度不能为空',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				
				// if (this.details == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入个人介绍',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if(this.label.length==0){
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '标签不能为空',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				
				var datas = {}
				datas.id = this.shopId
				datas.name = this.name
				datas.avatar = this.avatar
				datas.idCard = this.idNumber
				datas.contact = this.phone
				datas.serviceNum = this.serviceNum
				datas.birthday = this.birthday
				// datas.endTime = this.closeTime
				// datas.address = this.address
				// datas.city = this.city
				// datas.longitude = this.longitude
				// datas.latitude = this.latitude
				
				datas.details = this.details
				datas.idCardFront = this.cardFront
				datas.idCardBack = this.cardBack
				datas.userId = this.shopUserId;
				datas.gender = this.gender;
				// datas.label = this.label.toString()
				datas.credential = this.addressImg.toString()
				this.$http({
					url: this.$http.adornUrl('admin/userAuthentication/update'),
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
				
				this.phone = rows.contact
				this.idNumber = rows.idCard
				this.birthday = rows.birthday
				this.onLine = rows.onLine
				if (rows.label) {
					this.label = rows.label.split(',')
				}else{
					this.label = []
				}
				
				if(rows.city){
					this.city = rows.city
				}else{
					this.city = ''
				}
				if(rows.longitude){
					this.longitude = rows.longitude
					this.latitude = rows.latitude
				}else{
					this.longitude = ''
					this.latitude = ''
				}
				
				this.address = rows.address
				if(rows.avatar){
					this.avatar = rows.avatar
				}else{
					this.avatar = ''
				}
				if(rows.idCardFront){
					this.cardFront = rows.idCardFront
				}else{
					this.cardFront = ''
				}
				if(rows.idCardBack){
					this.cardBack = rows.idCardBack
				}else{
					this.cardBack = ''
				}
				if (rows.credential) {
					this.addressImg = rows.credential
				} else {
					this.addressImg = []
				}
				this.age = rows.age;
				this.serviceNum = rows.serviceNum;
				this.score = rows.score;
				this.details = rows.details
				this.shopUserId = rows.userId
				this.gender = rows.gender
				// this.getMyLocation()
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
			// 身份证正面照
			handleAvatarSuccess1(file, fileList) {
				this.cardFront = file.data.path;
			},
			// 身份证背面照
			handleAvatarSuccess2(file, fileList) {
				this.cardBack = file.data.path;
			},
			// 头像
			handleAvatarSuccess3(file, fileList) {
				this.avatar = file.data.path;
			},
			// 删除实名认证
			classDelete(row){
				this.$confirm(`确定要删除该信息吗`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/userAuthentication/delete'),
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
			// 地图-------------
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("DSQBZ-5MM3P-HEODO-VG6IX-SBRJE-PSBNX", "省钱兄家政服务");
				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				// this.latitude = position.lat;
				// this.longitude = position.lng;
				// this.city = position.city;
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件
			
				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}
			
				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度
			
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng.getLng() + '&key=5M6BZ-6YPLJ-AZRFX-D2XM3-UWTDT-SMF34&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response, response.result.address_component.city)
						that.address = response.result.address
						// that.city = response.result.address_component.city
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					if (!marker) {
						marker = new qq.maps.Marker({
							map: map,
							position: event.latLng
						});
					} else {
						marker.setPosition(event.latLng)
					}
			
					// markersArray.push(marker);
			
				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
			},
			// 地图定位
			selectDt() {
				// console.log(this.detailadd, this.detailadd.replace(/^\s+|\s+$/gm, ''))
			
				if (this.address == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入详细地址',
						type: 'warning'
					});
					return
				} else {
					var add = this.address
					let that = this
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + add +
						'&key=5M6BZ-6YPLJ-AZRFX-D2XM3-UWTDT-SMF34&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						// handle success
						if (response.message == '查询无结果') {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '详细地址输入有误，请重新输入',
								type: 'warning'
							});
							return
						}
						console.log('response', response)
						that.longitude = response.result.location.lng; // 经度
						that.latitude = response.result.location.lat; // 纬度
						// that.city = response.result.address_components.city;
						// that.detailadd = response.result.address_components.street
						// that.detailadd = response.result.address_components.province + response.result
						// 	.address_components.city + response.result.address_components.district + response.result.title
						that.setMap()
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
				}
			
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
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style lang="scss" scoped>
.autonym-people-page {
	padding: 0;
	overflow-x: hidden;
}

.autonym-people-page__hero {
	margin-bottom: 12px;
}

.autonym-people-page__filter {
	margin-bottom: 12px;
}

.autonym-people-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.autonym-people-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.autonym-people-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.autonym-people-page__input {
	width: 200px;
}

.autonym-people-page__select {
	width: 150px;
}

.autonym-people-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.autonym-people-page__table-card >>> .el-table {
	border-radius: 0;
}

.autonym-people-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.autonym-people-page__table >>> .el-table__body td {
	background: #fff;
}

.autonym-people-page__link {
	color: var(--nf-brand, #01af74);
	cursor: pointer;
}

.autonym-people-page__link:hover {
	color: var(--nf-brand-deep, #017c52);
}

.autonym-people-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>

<style>
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
