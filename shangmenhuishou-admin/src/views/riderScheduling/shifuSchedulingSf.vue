<template>
	<div class="shifu-scheduling-sf-page">
		<div class="nf-page-hero shifu-scheduling-sf-page__hero">
			<span class="nf-page-hero__badge">师傅调度</span>
			<h2 class="nf-page-hero__title">师傅地图</h2>
			<p class="nf-page-hero__desc">在地图上查看已认证师傅实时位置，支持按城市与姓名筛选，点击标记可查看待服务订单</p>
		</div>

		<div class="nf-filter-card shifu-scheduling-sf-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body shifu-scheduling-sf-page__filter-body">
				<div class="shifu-scheduling-sf-page__field">
					<span class="shifu-scheduling-sf-page__label">城市：</span>
					<el-input class="shifu-scheduling-sf-page__input" @keydown.enter.native="handleChange55"
						placeholder="请输入城市" v-model="city"></el-input>
				</div>
				<div class="shifu-scheduling-sf-page__field">
					<span class="shifu-scheduling-sf-page__label">师傅姓名：</span>
					<el-input class="shifu-scheduling-sf-page__input" @keydown.enter.native="handleChange55"
						placeholder="请输入师傅姓名" v-model="userName"></el-input>
				</div>
				<div class="shifu-scheduling-sf-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="handleChange55">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card shifu-scheduling-sf-page__map-card">
			<div id="container" class="shifu-scheduling-sf-page__map"></div>
		</div>

		<div>
			<!-- 待服务订单 -->
			<el-dialog title="待服务订单" :visible.sync="dialogFormVisible2" center width="70%" style="z-index: 1000;">
				<!-- <div style="display: inline-block;margin: 3px;">
					<div style="position: relative;display: inline-block;margin: 3px;">
						<span>订单类型：</span>
						<el-select v-model="type" style="width:150px;margin-left: 10px;" @change="animeDat(type)">
							<el-option v-for="item in typeNum" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>&nbsp;&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin: 3px;">
						<span>下单人用户名：</span>
						<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入下单人用户名"
							v-model="userName"></el-input>&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;margin: 3px;">
						<span>订单编号：</span>
						<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单编号"
							v-model="ordersNo"></el-input>&nbsp;&nbsp;
					</div>
				</div> -->
				<div style="display: inline-block;text-align: right;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="animeDat">刷新
					</el-button>
					<!-- <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansDet">
						重置
					</el-button> -->

				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="helpOrderId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="发单用户" width="100">
						<template slot-scope="scope">
							<span class="shifu-scheduling-sf-page__link" @click="updates(scope.row.userId)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="task_type" label="服务类型">
						<template slot-scope="scope">
							<span v-if="scope.row.task_type == 1">悬赏任务</span>
							<span v-if="scope.row.task_type == 2">报价任务</span>
							<span v-if="scope.row.task_type == 3">一口价任务</span>
							<span v-if="scope.row.task_type == 4">师傅任务</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="serviceName" label="服务分类"></el-table-column> -->
					<el-table-column prop="name" label="下单人姓名" width="100"></el-table-column>
					<el-table-column prop="phone" label="下单人手机号" width="120"></el-table-column>
					<el-table-column prop="province" label="下单人地址" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.detailsAddress}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="提交内容"></el-table-column>
					<el-table-column prop="money" label="接单价格" width="100"></el-table-column>
					
					<!-- <el-table-column prop="service_num" label="服务次数"></el-table-column>
					<el-table-column prop="score" label="评分"></el-table-column> -->
					<el-table-column prop="startTaskAddress" label="服务开始地址"></el-table-column>
					<el-table-column prop="endTaskAddress" label="服务结束地址"></el-table-column>
					<el-table-column prop="startTime" label="服务开始时间"></el-table-column>
					<el-table-column prop="endTime" label="服务结束时间"></el-table-column>
					
					<el-table-column prop="startTaskImg" label="服务开始前图片" width="180">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.startTaskImg">
								<div v-for="(item,index) in scope.row.startTaskImg.split(',')" :key="index">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
											slot="reference">
										<img style="width: 300px;height:auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="endTaskImg" label="服务结束后图片" width="180">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.endTaskImg">
								<div v-for="(item,index) in scope.row.endTaskImg.split(',')" :key="index">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
											slot="reference">
										<img style="width: 300px;height:auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="startAddress" label="服务开始地址" width="200"></el-table-column>
					<el-table-column prop="endAddress" label="服务结束地址" width="200"></el-table-column>
					<el-table-column prop="startTaskLongitude" label="服务开始经度"></el-table-column>
					<el-table-column prop="startTaskLatitude" label="服务开始纬度"></el-table-column>
					<el-table-column prop="endTaskLongitude" label="服务结束经度"></el-table-column>
					<el-table-column prop="endTaskLatitude" label="服务结束纬度"></el-table-column>
					<el-table-column prop="code" label="确认码"></el-table-column>
					<el-table-column prop="serviceTime" label="预约时间" fixed="right" width="80">
						<template slot-scope="scope">
							<span style="color: red;font-weight: 600;">{{scope.row.deliveryTime}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="remarks" label="订单备注" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.remarks ? scope.row.remarks : '未填写'}}</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color: #01af74;" v-if="scope.row.status === 1 ">待服务</span>
							<span style="color: #01af74;" v-if="scope.row.status === 2 ">服务中</span>
							<span style="color: #01af74;" v-if="scope.row.status === 3 ">已完成</span>
							<span style="color: #999;" v-if="scope.row.status === 4 ">已取消</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 加钟记录 -->
			<el-dialog title="加钟记录" :visible.sync="dialogFormVisible" center>

				<el-table v-loading="tableDataLoadingU" :data="userData.list">
					<el-table-column prop="chatContentId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="content" label="信息内容">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间">
					</el-table-column>
				</el-table>
			</el-dialog>
		</div>
	</div>
</template>

<script>
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
	var geocoder, map, markersArray, infoWindow = [];
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				shopName1: '',
				shopId: '',
				shopName: '',
				openTime: '',
				closeTime: '',
				detailedAddress: '',
				shopLng: '',
				shopLat: '',
				percentage1: 0, //进度条
				img: [], //任务轮播图
				// img: '',
				classn: '',
				value: [],
				parentId: 0,
				parentIdnum: '',

				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible: false,
				typeDatas: {},
				typeDatas1: {},
				titles: '添加',
				// 地图相关
				longitude: '', //经度
				latitude: '', //维度
				loadingMap: true,
				BMap: '',
				map: '',
				geoc: '',
				keyword: '',
				showMap: false,
				pointLngLat: '',
				center: {
					lng: 108.948024,
					lat: 34.263161
				},
				lng: 108.948024,
				lat: 34.263161,
				storeAddress: [],
				storeAddre: '请选择城市',
				form: {
					province: '',
					city: '', //市
					district: '', //区
				},
				dialogImageUrl: [],
				dialogVisible: false,
				erweimaImg: '',
				activeName: 'first',
				shopTypeName: '',
				shopTypId: '',
				titles1: '',
				labels: [], //标签集合
				bq: '', //输入的标签名
				province: '',
				city: '', //市
				district: '', //区
				options: regionData,
				shopCover: '',
				shopTypenum: [],
				typeDatas2: [],
				shopType: '',
				geometries: [],
				center: '',
				map: '',
				lists: [],
				index1: 0,
				tableData: {},
				hospitalId: '',
				ordersNo: '',
				userName: '',
				type: '',
				marker: '',
				infoWindowList: [],
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '陪诊订单'
					},
					{
						value: 2,
						label: '陪护订单'
					},
				],
				userData: [],
				tableDataLoadingU: false,
				dialogFormVisible: false,
				limit1: 10,
				page1: 1,
				phone: '',
				campus: '',
				ordersId: '',
				pd: '',
				titles: '信息详情',
				dialogFormVisibleDel: false,
				detaDatle: {},
				datleIndex: 1,
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val
				this.dataSelectoder()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelectoder()
			},
			handleSizeChangeU(val) {
				this.limit1 = val;
				this.dataSelectUser()()
			},
			handleCurrentChangeU(val) {
				this.page1 = val;
				this.dataSelectUser()
			},
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75", "来点小收入");
				geolocation.getIpLocation(this.showPosition, this.showErr);
			},
			showPosition(position) {
				this.setMap();
			},
			showErr(e) {
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				var center = new TMap.LatLng(this.latitude, this.longitude);
				if (map) {
					map.destroy();
				}
				map = []
				infoWindow = []
				// 初始化地图
				map = new TMap.Map('container', {
					zoom: 13, // 设置地图缩放级别
					pitch: 30,
					center: center, // 设置地图中心点坐标
					// mapStyleId: "style1", //个性化样式
					baseMap: {
						type: 'vector',
						// features: ['base', 'building3d'], // 隐藏矢量文字
					},
				});
				console.log('this.geometries', this.geometries)
				// marker文字在图片内
				/* if (this.marker) {
					this.marker.setMap(null);
				}else{
					
				} */

				this.marker = new TMap.MultiMarker({
					map: map,
					styles: {
						default: new TMap.MarkerStyle({
							width: 66, // 宽度
							height: 66, // 高度
							anchor: {
								x: 46,
								y: 46
							}, // 锚点位置
							src: 'https://mapapi.qq.com/web/lbs/visualizationApi/demo/img/circle.png', // 标注点图片url或base64地址
							color: '#fff', // 标注点文本颜色
							size: 14, // 标注点文本文字大小
							offset: {
								x: 0,
								y: 16
							}, // 标注点文本文字基于direction方位的偏移属性
						}),
					},
					geometries: this.geometries
				});
				/* if(this.infoWindowList && this.infoWindowList.length>0){
					for(var i in this.infoWindowList){
						var infoWindow=this.infoWindowList[i];
						infoWindow.close()
					}
				} */


				//初始化infoWindow
				console.log('this.geometries', this.geometries.length)
				// infoWindow.clear();
				for (var i in this.geometries) {
					infoWindow = new TMap.InfoWindow({
						map: map,
						position: new TMap.LatLng(this.geometries[i].datas.latitude, this.geometries[i].datas
							.longitude),
						offset: {
							x: 0,
							y: -32
						} //设置信息窗相对position偏移像素
					});
					// infoWindow.clears();//关闭信息窗
					// infoWindow.setContent('待服务数量：1'); //设置信息窗内容
					// infoWindow.setContent('待服务数量：' + this.geometries[i].datas.ordersCount); //设置信息窗内容
					if(this.geometries[i].datas.serviceNum){
						var serviceNum = this.geometries[i].datas.serviceNum
					}else{
						var serviceNum =0
					}
					if(this.geometries[i].datas.score){
						var score = this.geometries[i].datas.score
					}else{
						var score =0
					}
					infoWindow.setContent('服务次数：' + serviceNum+'</br> 评分：' + score); //设置信息窗内容
					infoWindow.open(); //初始信息窗开启
					//this.infoWindowList.push(infoWindow);
					// infoWindow.close(); //关闭信息窗
				}
				let that = this
				this.marker.on("click", function(evt) {
					that.hospitalId = evt.geometry.datas.userId
					that.dataSelectoder()
					that.dialogFormVisible2 = true
				})

			},
			// 地图定位
			select() {
				console.log(this.detailedAddress)
				// this.address = this.province+ this.city+this.district+this.address
				var addr = this.province + this.city + this.district + this.address
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + addr +
					'&key=5M6BZ-6YPLJ-AZRFX-D2XM3-UWTDT-SMF34&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					console.log('response', response)
					that.longitude = response.result.location.lng; // 经度
					that.latitude = response.result.location.lat; // 纬度
					// that.address = response.result.title
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			// 医院点击事件
			bindClick() {
				this.dialogFormVisible2 = true
				this.dataSelectoder()
			},
			phoneSelect() {
				this.select()
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			// 获取省市区
			handleChange5(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				if (this.city == '市辖区') {
					this.city = this.province
				}
				this.dataSelect()
			},

			handleChange55(value) {

				this.dataSelect()
			},
			handleChange() {
				this.$router.push({
					path: '/riderScheduling',
				})
			},
			dataSelect() {
				this.$http({
					url: this.$http.adornUrl('userCertification/queryCertification'),
					method: 'get',
					params: this.$http.adornParams({
						// 'classifyId': this.type,
						'city': this.city,
						'name': this.userName,
						'isRecommend':-1,
					})
				}).then(({
					data
				}) => {
					console.log(data)
					var arr = []
					var arr1 = []
					if (data.data.length > 0) {

						for (var i in data.data) {
							if (data.data[i].latitude && data.data[i].longitude) {
								var obj = {}
								obj.position = new TMap.LatLng(data.data[i].latitude, data.data[i].longitude)
								obj.datas = data.data[i]
								obj.img = data.data[i].addressImg
								obj.content = data.data[i].name
								// obj['position'] = new TMap.LatLng(data.data.records[i].latitude, data.data
								// 	.records[i].hospitalLng)
								// obj['datas'] = data.data.records[i]
								// obj['img'] = data.data.records[i].icon
								// obj['content'] = data.data.records[i].artificerName
								// arr.push(new TMap.LatLng(data.data.records[i].latitude, data.data.records[i]
								// 	.hospitalLng))
								arr.push(obj)
								arr1.push(obj)
								this.longitude = data.data[0].longitude
								this.latitude = data.data[0].latitude
							} else {
								this.longitude = this.lng
								this.latitude = this.lat
							}

						}

						this.geometries = arr
						this.lists = arr1
						this.setMap();

					} else {
						this.longitude = this.lng
						this.latitude = this.lat
						this.$message({
							message: '暂无数据',
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
						this.setMap();
					}

				})
			},
			// 重置
			cleans() {
				this.province = ''
				this.city = ''
				this.district = ''
				this.storeAddress = []
				this.storeAddre = '请选择城市'
				this.userName = ''
				this.type = ''
				this.dataSelect()
			},
			// 详情跳转
			updates(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelectoder()
			},
			cleansDet() {
				this.page = 1
				this.type = ''
				this.userName = ''
				this.ordersNo = ''
				this.dataSelectoder()
			},

			// 获取骑手订单
			dataSelectoder() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('help/selectRunHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': 6,
						'userId':this.hospitalId,
						'content':'',
						'phone':'',
						'gameId':'',
						'taskType':''
					})
					
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 转单弹框
			zhuandan(row, pd) {
				this.ordersId = row.ordersId
				if (pd) {
					this.pd = pd
				} else {
					this.pd = ''
				}

				this.dataSelectUser()
				this.dialogFormVisible = true
			},
			// 确认选择
			userBtns(row) {
				let url = ''
				if (this.pd) {
					url = 'orders/distributionOrder'
				} else {
					url = 'orders/giveOrdersUser'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					params: this.$http.adornParams({
						'userId': row.userId,
						'orderId': this.ordersId,
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
								this.dataSelectoder()
								this.dataSelect()
								this.dialogFormVisible = false
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
			// 获取数据列表
			dataSelectUser() {
				this.tableDataLoadingU = true
				this.$http({
					url: this.$http.adornUrl('artificer/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'phone': '',
						'name': '',
						'status': '',
						'city': '',
						'parentId': this.ordersId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingU = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			selectU() {
				this.page = 1
				this.dataSelectUser()
			},
			// 重置
			cleansU() {
				this.page1 = 1
				this.campus = ''
				this.phone = ''
				this.dataSelectUser()
			},
			
			// 项目类型
			fwSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '服务类型'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.typeNum = returnData
				})
			},
		},
		created() {

		},
		mounted() {
			let data = {
				key: "ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75", //申请的密钥
				output: "jsonp",
			};
			let url = "https://apis.map.qq.com/ws/location/v1/ip"
			this.$jsonp(url, data).then(res => {
				console.log('获取Ip定位成功----')

				this.lng = res.result.location.lng
				this.lat = res.result.location.lat

			}).catch(err => {
				this.$toast('获取Ip定位失败--', err.message)
			})
			// this.setMap();
			// this.getMyLocation()
			this.dataSelect()
			// this.fwSelect()

		}
	}
</script>

<style lang="scss" scoped>
.shifu-scheduling-sf-page {
	padding: 0;
	overflow-x: hidden;
}

.shifu-scheduling-sf-page__hero {
	margin-bottom: 12px;
}

.shifu-scheduling-sf-page__filter {
	margin-bottom: 12px;
}

.shifu-scheduling-sf-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.shifu-scheduling-sf-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shifu-scheduling-sf-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.shifu-scheduling-sf-page__input {
	width: 200px;
}

.shifu-scheduling-sf-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shifu-scheduling-sf-page__map-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.shifu-scheduling-sf-page__map {
	width: 100%;
	height: 80vh;
	min-height: 480px;
	position: relative;
}

.shifu-scheduling-sf-page__link {
	color: #01af74;
	cursor: pointer;
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

	.csssprite {
		width: 100% !important;
		height: 100% !important;
		border-radius: 50% !important;
		min-width: 100% !important;
		max-width: 100% !important;
		min-height: 100% !important;
		max-height: 100% !important;
		border: 3px solid red !important;
	}
</style>