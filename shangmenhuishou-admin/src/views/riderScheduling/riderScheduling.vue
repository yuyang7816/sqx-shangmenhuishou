<template>
	<div>
		<div style="display: inline-block;font-size:18px;margin-bottom: 15px;">
			<a href="#" @click="prev" style="text-decoration:none;font-size: 14px;">
				<icon-svg name="jiantou" style="width: 1.2em;height: 1.2em;position: relative;top: 0.3em;"></icon-svg>
				返回
			</a>
			<span style="display: inline-block;margin: 0 15px;color: #D9D9D9;">|</span>
			<span>员工调度</span>
		</div>
		<div>
			<div style="margin-bottom: 10px;display: inline-block;width: 100%;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="handleChange55">
					刷新</el-button>
				<!-- <span style="display: inline-block; width: 200px;text-align: right;">城市：</span>
				<el-cascader style="width:30%" size="large" :options="options" ref="cascaderAddr" v-model="storeAddress"
					:placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
				</el-cascader>&nbsp;&nbsp;&nbsp;&nbsp;
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="handleChange55">
					刷新</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button> -->
			</div>

		</div>
		<!-- <img src="../../assets/img/qishou.png" /> -->
		<div id="container1" style="width:95%;height:80vh;margin-left: 2.5%;"></div>
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
	var geocoder, map, markersArray = [];
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
			}
		},
		methods: {

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
				var center = new TMap.LatLng(this.latitude, this.longitude); // 设置中心点坐标
				var centerHeight = new TMap.LatLng(this.latitude, this.longitude); // 带高度的坐标
				// 初始化地图
				var map = new TMap.Map('container', {
					center: center,
					zoom: 13,
				});
				// 初始化label
				var label = new TMap.MultiLabel({
					id: 'label-layer',
					map: map,
				 styles: {
						label: new TMap.LabelStyle({
							color: '#3777FF', // 颜色属性
							size: 20, // 文字大小属性
							offset: {
								x: 0,
								y: 0
							}, // 文字偏移属性单位为像素
							angle: 0, // 文字旋转属性
							alignment: 'center', // 文字水平对齐属性
							verticalAlignment: 'middle', // 文字垂直对齐属性
						}),
					},
				});
				
				
				// var center = new qq.maps.LatLng(this.latitude, this.longitude);
				// var map = new qq.maps.Map(document.getElementById("container1"), {
				// 	center: center,
				// 	zoom: 20
				// });
				// var infoWin = new qq.maps.InfoWindow({
				// 	map: map
				// });
				// // var latlngs = [
				// //     new qq.maps.LatLng(39.91374, 116.37333),
				// //     new qq.maps.LatLng(39.91347, 116.39336),
				// //     new qq.maps.LatLng(39.90184, 116.41306)
				// // ];
				var latlngs = this.lists

				// //根据图片大小调整 界点
				// var anchor = new qq.maps.Point(0, 10),
				// 	size = new qq.maps.Size(42, 42),
				// 	origin = new qq.maps.Point(0, 0),
				// 	markerIcon = new qq.maps.MarkerImage(
				// 		"https://pt2.0.xianmxkj.com/file/uploadPath/2022/01/25/5fa97fc6845cff012b9f2ae755445b7c.png",
				// 		// size,
				// 		// origin,
				// 		// anchor
				// 	);

				for (var i = 0; i < latlngs.length; i++) {
					(function(n) {
						//实例标注
						var marker = new qq.maps.Marker({
							position: latlngs[n].position,
							map: map
						});
						var markerIcon = new qq.maps.MarkerImage(
							latlngs[i].img,
							size,
							origin,
							anchor
						);
						//设置每个标注的样式
						marker.setIcon(markerIcon);
						//标注点击事件
						qq.maps.event.addListener(marker, 'click', function() {
							console.log('nnnnnnnn', latlngs[n].styleId)
							var that = this
							window.location.href = "/userDetail?userId=" + latlngs[n].styleId;
							// hash变化时触发
							window.onhashchange = function() {
								//  console.log('current hash:', window.location.hash);
							}
							// infoWin.open();
							// infoWin.setContent('<div style="text-align:center;white-space:' +
							//     'nowrap;margin:10px;">这是第 ' +
							//     n + ' 个标注</div>');
							// infoWin.setPosition(latlngs[n]);
							// that.index1 = n
							// console.log('```````````````````',that.index1)
							// that.updates(n)
						});
					})(i);
				}

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
			phoneSelect() {
				this.select()
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			// 获取省市区
			handleChange55(value) {
				// value = this.$refs['cascaderAddr'].currentLabels
				// this.province = value[0]
				// this.city = value[1]
				// this.district = value[2]
				// if (this.city == '市辖区') {
				// 	this.city = this.province
				// }
				this.dataSelect()
			},
			dataSelect() {
				let companyId = this.$route.query.companyId
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'companyId':companyId
						// 'province': this.province,
						// 'city': this.city,
						// 'district': this.district
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
								obj['position'] = new qq.maps.LatLng(data.data[i].latitude, data.data[i].longitude)
								obj['styleId'] = data.data[i].id
								obj['img'] = data.data[i].avatar
								arr.push(new qq.maps.LatLng(data.data[i].latitude, data.data[i].longitude))
								arr1.push(obj)
								this.longitude = data.data[0].longitude
								this.latitude = data.data[0].latitude
							}

						}
						this.geometries = arr
						this.lists = arr1
						console.log('this.lists', this.lists)
						this.setMap();

					} else {
						this.geometries = []
						
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
				this.dataSelect()
			},
			updates(index) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: this.lists[index].userId
					}
				})
			},
			// 返回上一级
			prev() {
				this.$router.back()
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
				
				this.longitude = res.result.location.lng
				this.latitude = res.result.location.lat
			
			}).catch(err => {
				this.$toast('获取Ip定位失败--', err.message)
			})
			
			// this.getMyLocation()
			this.dataSelect()

		}
	}
</script>

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
		border: 3px solid #07BA6D !important;
	}
</style>
