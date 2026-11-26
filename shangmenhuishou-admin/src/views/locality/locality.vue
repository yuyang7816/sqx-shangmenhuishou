<template>
	<div class="locality-page">
		<div class="nf-page-hero locality-page__hero">
			<span class="nf-page-hero__badge">废品管理</span>
			<h2 class="nf-page-hero__title">项目列表</h2>
			<p class="nf-page-hero__desc">管理回收服务项目，支持按一级/二级分类、标题与城市筛选，可新增、修改与删除项目</p>
		</div>

		<div class="nf-filter-card locality-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body locality-page__filter-body">
				<div class="locality-page__field">
					<span class="locality-page__label">一级分类：</span>
					<el-select v-model="parentIdT" class="locality-page__select" @change="phoneSelectP()">
						<el-option v-for="item in homeData" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</div>
				<div class="locality-page__field">
					<span class="locality-page__label">二级分类：</span>
					<el-select v-model="classifyIdT" class="locality-page__select" :disabled="parentIdT==''"
						@change="phoneSelect()">
						<el-option v-for="item in homeDataErT" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</div>
				<div class="locality-page__field">
					<span class="locality-page__label">标题：</span>
					<el-input class="locality-page__input" @keydown.enter.native="phoneSelect" placeholder="请输入标题"
						v-model="titleT">
					</el-input>
				</div>
				<div class="locality-page__field">
					<span class="locality-page__label">城市：</span>
					<el-input class="locality-page__input" @keydown.enter.native="phoneSelect" placeholder="请输入城市"
						v-model="region1">
					</el-input>
				</div>
				<div class="locality-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
					<el-button size="mini" type="primary" icon="el-icon-plus" :disabled="!isAuth('locality:add')"
						@click="choiaddNotice()">添加</el-button>
				</div>
			</div>
		</div>

		<div class="locality-page__tabs-wrap">
			<el-tabs v-model="activeName" class="locality-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="服务列表" name="third">
					<div class="nf-table-card locality-page__table-card">
						<el-table v-loading="tableDataLoading" :data="choicenData.records" class="locality-page__table">
					<el-table-column prop="id" label="编号" width="80" fixed="left">
					</el-table-column>
					<!-- <el-table-column prop="isRecommend" label="推荐" width="80" fixed="left">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isRecommend"
								@change="change2(scope.row.isRecommend,scope.row)" :active-value="openValue1"
								:disabled="!isAuth('locality:update')" :inactive-value="closeValue1"
								active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column> -->
					<el-table-column prop="cover" label="封面图">
						<template slot-scope="scope">
							　　<img v-if="scope.row.cover &&scope.row.cover!=''" :src="scope.row.cover" width="40"
								height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="pic" label="详情图" width="180">
						<template slot-scope="scope">
							<div v-if="scope.row.pic">
								<img v-for="(item,index) in scope.row.pic.split(',')" :key="index" :src="item"
									width="40" height="40" style="margin:3px" />
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="name" label="标题" width="150" fixed="left">
					</el-table-column>
					<el-table-column prop="categoryName" label="分类名称" width="150">
					</el-table-column>
					<el-table-column prop="price" label="价格">
					</el-table-column>
					<el-table-column prop="tags" label="标签">
					</el-table-column>
					<el-table-column prop="city" label="城市">
					</el-table-column>
					<el-table-column prop="appointmentNum" label="预约数">
						<template slot-scope="scope">
							<span v-if="scope.row.appointmentNum">{{scope.row.appointmentNum}}</span>
							<span v-else>0</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="160" fixed='right' >
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('locality:update')"
								@click="choiCompile(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="choidelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
						</el-table>
						<div class="locality-page__pagination">
							<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="choicenData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 添加信息弹框 -->
		<el-dialog :title="renwuTitles" customClass="customWidth" :visible.sync="dialogFormVisible5" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">一级分类：</span>
						<el-select v-model="parentId" style="width:45%;" @change="onChang3">
							<el-option v-for="(item,index) in homeData" :key="index" :label="item.name"
								:value="item.id">
							</el-option>
						</el-select>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">二级分类：</span>
						<el-select v-model="categoryId" style="width:45%;">
							<el-option v-for="(item,index) in homeDataEr" :key="index" :label="item.name"
								:value="item.id">
							</el-option>
						</el-select>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">城市：</span>
						<el-input v-model="city" style="width:45%;" placeholder="请输入城市">
						</el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
						<el-input v-model="name" style="width:45%;" placeholder="请输入标题">
						</el-input>
					</div>
					<div style="margin-bottom: 10px;display:flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">主页图：</span>
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="cover"
								:action="$http.adornUrlUp('file/upload')" :show-file-list="false"
								:on-success="handleAvatarSuccess5" :on-progress="onprogress">
								<img v-if="cover!=''" :src="cover" class="avatar"
									style="border-radius: 6px;width: 148px;height: 148px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
								<el-progress v-if="percentage>0&&percentage<100" type="circle" :percentage="percentage">
								</el-progress>
							</el-upload>
						</div>
					</div>
					<div style="margin-bottom: 10px;display:flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">详情图：</span>
						<div class="imgs" v-for="(item,index) in pic" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear(index)"></i>
							</span>
						</div>
						<div class="imgs" style="width: 50%;">
							<el-upload :action="$http.adornUrlUp('file/upload')" list-type="picture-card"
								:show-file-list="false" :on-success="handleUploadSuccess" :on-progress="onprogress1">
								<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
									:percentage="percentage1"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">价格：</span>
						<el-input v-model="price" type="number" style="width:45%;" min="0"
							placeholder="请输入价格"></el-input>
					</div>
					<!-- <div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">是否推荐：</span>
						<el-radio-group v-model="status">
							<el-radio :label="0">上架</el-radio>
							<el-radio :label="2">下架</el-radio>
						</el-radio-group>
					</div> -->
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">预约数：</span>
						<el-input style="width:50%;" v-model="appointmentNum" type="number" min="0"
							placeholder="请输入预约数"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
						<span class="bqList" v-for="(item,index) in tags" :key="index">{{item}}
							<span class="delss">
								<i class="el-icon-delete" @click="dels1(index)"></i>
							</span>
						</span>
						<el-input v-model="bq" style="width:200px;" placeholder="请输入标签"></el-input>
						<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
						</el-button>
					</div>
					<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
						<el-button @click="dialogFormVisible5 = false">取 消</el-button>
						<el-button type="primary" @click="addmissionNoticeTo()">确 定</el-button>
					</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from './quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				
				page: 1,
				limit: 10,
				openValue1: '0',
				closeValue1: '1',
				
				activeName: 'third',
				tableDataLoading: true,
				dialogFormVisible5: false,
				homeData: [],
				homeDataEr:[],
				homeDataErT: [],
				
				classifyList: [{
						id: '',
						name: '全部'
					},
					{
						id: 0,
						name: '是'
					},
					{
						id: 1,
						name: '否'
					}
				],
				choicenData: [],
				percentage:0,
				search: '',
				classifyIdT: '',
				parentIdT:'',
				classifyId: [],
				isRecommendT: '',
				titleT: '',
				statusId: '',
				statuss: [{
						id: '',
						title: '全部'
					},
					{
						id: 0,
						title: '进行中'
					},
					{
						id: 1,
						title: '待审核'
					},
					{
						id: 2,
						title: '已取消'
					},
					{
						id: 3,
						title: '拒绝'
					}
				],
				// 发布信息
				ids: '', //信息id
				multipleSelection: [],
				renwuTitles: '添加',
				percentage1: 0,
				region1: '',
				
				danwei: [],
				// --------
				categoryName: "",
				id: 0,
				categoryId: 0,
				name: "",
				cover: "",
				pic: [],
				tags: [],
				appointmentNum: 0,
				price: 0,
				city: "",
				bq:'',
				parentId: 0,
				


			}
		},
		methods: {
			
			handleSizeChange1(val) {
				this.limit = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},
			
			handleClick(tab, event) {
				
				this.page = 1
				if (tab._props.label == '服务列表') {
					this.type = 2
					this.InformationSelect()
				}
				
			},
			phoneSelectP(){
				this.classifyIdT = ''
				this.classSelectListT()
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.status = ''
				this.statusId = ''
				this.titleT = ''
				this.isRecommendT = ''
				this.search = ''
				this.region1 = ''
				
				this.classifyId = []
				this.classifyIdT = ''
				this.parentIdT = ''
				this.page = 1
				this.InformationSelect()
			},
			// 获取一级分类
			homeSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/waste/category/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': 1,
						'limit': 100,
						'parentId':0,
						'level':'',
						'name':'',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.homeData = returnData.records
					if(this.renwuTitles == '修改'){
						this.classSelectList()
					}
				})
			},
			// 获取二级分类数据
			classSelectList() {
				this.$http({
					url: this.$http.adornUrl('admin/waste/category/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': 1,
						'limit': 100,
						'parentId':this.parentId,
						'level':'',
						'name':'',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.homeDataEr = returnData.records
				})
			},
			// 获取二级分类数据
			classSelectListT() {
				this.$http({
					url: this.$http.adornUrl('admin/waste/category/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': 1,
						'limit': 100,
						'parentId':this.parentIdT,
						'level':'',
						'name':'',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.homeDataErT = returnData.records
				})
			},
			// 信息数据
			InformationSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/wasteItem/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'categoryId': this.classifyIdT,
						// 'status': this.statusId,
						'name': this.titleT,
						'city': this.region1,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.choicenData = returnData
					console.log('``````````````', this.choicenData)

				})
			},
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.cover = file.data.path;
			},
			onprogress(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage = parseInt(event.percent)
			},
			
			// 获取二级分类
			onChang3() {
				this.categoryId = ''
				this.classSelectList()
				
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
					this.tags.push(this.bq)
					this.bq = ''
				}

			},
			// 删除标签
			dels1(index) {
				this.tags.splice(index, 1);
				console.log(this.tags)
			},
			//添加
			choiaddNotice() {
				this.renwuTitles = '添加'
				this.id = ''
				this.categoryId = ''
				this.name = ''
				this.cover = ''
				this.pic = []
				this.tags = []
				this.appointmentNum = 0
				this.price = ''
				this.city = ''
				this.bq = ''
				this.parentId = ''
				this.dialogFormVisible5 = true
				this.homeSelect()
			
			},
			// 修改信息弹框
			choiCompile(row) {
				this.renwuTitles = '修改'
				if (row) {
					this.id = row.id
					this.categoryId = row.categoryId
					this.name = row.name
					this.cover = row.cover
					if(row.pic){
						this.pic = row.pic.split(",")
					}else{
						this.pic = []
					}
					if(row.tags){
						this.tags = row.tags.split(",")
					}else{
						this.tags = []
					}
					this.appointmentNum = row.appointmentNum
					this.price = row.price
					this.city = row.city
					this.bq = ''
					this.parentId = row.categoryParentId
				}
				this.homeSelect()
				this.dialogFormVisible5 = true
			},
			// 确定添加
			addmissionNoticeTo() {
				if (this.categoryId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择二级分类',
						type: 'warning'
					});
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					});
					return
				}
				if (this.cover == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传封面图',
						type: 'warning'
					});
					return
				}
				if (this.pic.length == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传详情图',
						type: 'warning'
					});
					return
				}
				if (this.tags.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签',
						type: 'warning'
					});
					return
				}
				if (this.price == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入价格',
						type: 'warning'
					});
					return
				}
				if (this.appointmentNum === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入预约数',
						type: 'warning'
					});
					return
				}
				if (this.city == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入城市',
						type: 'warning'
					});
					return
				}
				let urls = ''
				if (this.renwuTitles == '添加') {
					urls = 'admin/wasteItem/save'
				} else {
					urls = 'admin/wasteItem/update'
				}
				let that = this
				setTimeout(function() {
					that.$http({
						url: that.$http.adornUrl(urls),
						method: 'post',
						data: that.$http.adornData({
							// params: that.$http.adornParams({
							'price': that.price,
							'city': that.city,
							'appointmentNum': that.appointmentNum,
							'id': that.id,
							'cover': that.cover,
							'pic': that.pic.toString(),
							'tags': that.tags.toString(),
							'name': that.name,
							'categoryId': that.categoryId,
						})
					}).then(({
						data
					}) => {
						console.log('提交结果', data)
						if (data.code == 0) {
							that.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									that.dialogFormVisible5 = false
									that.InformationSelect()
								}
							})
						} else {
							that.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'warning'
							});
						}
					})
				}, 1000)
			},
			// 删除
			choidelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/wasteItem/delete'),
						method: 'post',
						data: this.$http.adornData({
							'id':row.id
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
									this.InformationSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			
			// 批量删除
			choideletes() {
				console.log(this.multipleSelection)
				let delid = this.multipleSelection.toString()
				this.$confirm(`确定删除信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`information/deleteInformationById?ids=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.InformationSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			// 服务推荐
			change2(val, row) {
				this.$http({
					url: this.$http.adornUrl(`orderTaking/updateOrderTakingRecommend/${row.id}`),
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
								this.InformationSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.InformationSelect()
							}
						})
					}
				})
			},
			// 删除
			clear(index) {
				this.pic.splice(index, 1);
			},
			//上传成功
			handleUploadSuccess(file, fileList) {
				this.pic.push(file.data.path)
				console.log('this.pic', this.pic)
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			// 获取单位
			xianshi() {
				this.$http({
					url: this.$http.adornUrl('common/type/615'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.danwei = data.data.value.split(',')
					}
				})
			},
			
		},
		mounted() {
			console.log('this.$route.query.userName', this.$route.query.userName)
			if (this.$route.query.userName) {
				this.search = this.$route.query.userName
			} else {
				this.search = ''
			}
			this.InformationSelect()
			this.homeSelect()
			// this.xianshi()
		},
		
	}
</script>

<style lang="scss" scoped>
.locality-page {
	padding: 0;
	overflow-x: hidden;
}

.locality-page__hero {
	margin-bottom: 12px;
}

.locality-page__filter {
	margin-bottom: 12px;
}

.locality-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.locality-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.locality-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.locality-page__select,
.locality-page__input {
	width: 150px;
}

.locality-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.locality-page__tabs-wrap {
	margin-bottom: 0;
}

.locality-page__tabs {
	margin-bottom: 0;
}

.locality-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.locality-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.locality-page__table-card >>> .el-table {
	border-radius: 0;
}

.locality-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.locality-page__table >>> .el-table__body td {
	background: #fff;
}

.locality-page__pagination {
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

	.ql-editor {
		word-break: break-all;
	}

	.texts {
		width: 100%;
	}

	.texts img {
		max-width: 100% !important;
	}
</style>