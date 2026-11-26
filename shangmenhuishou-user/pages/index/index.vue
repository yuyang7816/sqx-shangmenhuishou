<template>
	<view class="">

		<view class="headTop">
			<view class="headTop_city " @tap="goSelectCity()">
				<image src="../../static/image/index/place.png" mode="widthFix">
				</image>
				<view class="localName">{{city?city:'请选择城市'}}</view>
			</view>
			<view class="headTop_search" @click="goSearch()">
				<image src="/static/image/index/sousuo.png"></image>
				<text>搜索你需要的服务</text>
			</view>

		</view>

		<!-- 轮播图 -->
		<view class="fw">
			<view class="fw-box">
				<swiper class="screen-swiper" :circular="true" :autoplay="true" interval="2500" duration="800">
					<swiper-item v-for="(item,index) in swiperList" :key="index" @click="goNav(item.url)">
						<image :src="item.imageUrl" mode="aspectFill"></image>
					</swiper-item>
				</swiper>
			</view>
		</view>
		<!-- 金刚区 -->
		<view class="gridbox " v-if="XCXIsSelect!='否'">
			<swiper class="swiper" duration="300" :style="{ height: categoryHeight }" @change="categoryChange">
				<swiper-item v-for="(nav, index5) in gridData" :key="index5">
					<view class="category-list">
						<view class="icon" v-for="(item,ind) in nav" :key="ind" @click="goNav(item.url)">
							<image mode="aspectFill" :src="item.imageUrl">
							</image>
							<view>{{ item.name }}</view>
						</view>
					</view>
				</swiper-item>
			</swiper>
			<view class="dots">
				<view v-for="(page, pageindex) in gridData" :key="pageindex"
					:class="{ active: pageindex == currentPageindex }"></view>
			</view>
		</view>

		<view class="yuyuebox" v-if="XCXIsSelect!='否'">
			<view class="yuyuebox_icon">
				<view class="yuyuebox_iconBox">
					<image src="../../static/image/index/yuyue.png" mode="widthFix"></image>
					<view class="">在线预约</view>
				</view>
				<view class="yuyuebox_iconJt">
					<image src="../../static/image/index/jiantu.png" mode="widthFix"></image>
				</view>
				<view class="yuyuebox_iconBox">
					<image src="../../static/image/index/shangmen.png" style="width: 44rpx;height: 44rpx;"></image>
					<view class="margin-top-xs">免费上门</view>
				</view>
				<view class="yuyuebox_iconJt">
					<image src="../../static/image/index/jiantu.png"
						style="width: 54rpx;height: 19rpx;margin-top: 20rpx;"></image>
				</view>
				<view class="yuyuebox_iconBox">
					<image src="../../static/image/index/huoli.png" style="width: 44rpx;height: 44rpx;"></image>
					<view class="margin-top-xs">立即获利</view>
				</view>
			</view>
			<view class="yuyuebox_btn" @click="goNav('/my/order/editor')">
				<image src="../../static/image/index/hsbtn.png" mode="widthFix"></image>
			</view>
		</view>


		<view class="listbox" v-for="(item,index) in list" :key='index' @click="goOrder(item)">
			<image :src="item.cover?item.cover: '../../static/logo.png'" mode="aspectFill"></image>
			<view class="listboxCot">
				<view class="listboxCot_title">{{item.name}}</view>
				<view v-if="item.appointmentNum" class="listboxCot_tit">已预约{{item.appointmentNum}}</view>
				<view class="listboxCot_label">
					<view v-for="(ite,ind) in item.tags" :key="ind" :class="ind%2===0?'label':'labels'">
						{{ite}}
					</view>
				</view>

				<view class="flex justify-end">
					<view class="btn">预约回收</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length==0"></empty>
		<!-- 加载数据 -->
		<up-loadmore :status="status" v-if="list.length>0" />



	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	import empty from '@/components/empty/empty.vue'
	const {
		proxy
	} = getCurrentInstance();
	const city = ref('');
	const latitude = ref('');
	const longitude = ref('');

	const swiperList = reactive([]); //轮播图
	const gridData = reactive([]); //金刚区
	const categoryHeight = ref('120rpx');
	const currentPageindex = ref(0);
	const userId = ref('');

	const orderList = reactive([]);

	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const arr = reactive([]); //小程序订阅模板
	const showModalMsg = ref(true); //小程序订阅弹框
	const XCXIsSelect = ref('否')

	onLoad(option => {
		getBannerList(); //获取轮播图
		getGrid(); //获取金刚区分类
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect')
		// getlist();
		// sys_wx_user_template_id	用户端微信小程序消息模板ID

		if (uni.getStorageSync('latitude') && uni.getStorageSync('longitude')) {
			latitude.value = uni.getStorageSync('latitude')
			longitude.value = uni.getStorageSync('longitude')
			selectCity(latitude.value, longitude.value);
		} else {
			uni.getLocation({
				type: 'gcj02', //wgs84  gcj02
				success: function(res) {
					console.log(res, '地理位置')
					latitude.value = res.latitude
					longitude.value = res.longitude
					uni.setStorageSync('latitude', res.latitude)
					uni.setStorageSync('longitude', res.longitude)
					selectCity(latitude.value, longitude.value);
				},
				fail: function() {
					console.log('获取地址失败')
				}
			})
		}

	});
	onShow(() => {

		// 用户端_师傅接单小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_take_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		// 用户端_师傅取消订单小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_cancel_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		// 用户端_订单完成小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_complete_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});


		userId.value = uni.getStorageSync('userId');
		if (userId.value) {
			// #ifdef MP-WEIXIN
			console.log(showModalMsg.value, 'showModalMsgshowModalMsg')
			if (showModalMsg.value) {
				openMsg()
			}
			// #endif
		}
	});
	/* 跳转服务详情 */
	const goOrder = (e) => {
		// #ifdef MP-WEIXIN
		if (uni.getStorageSync('sendMsg')) {
			uni.requestSubscribeMessage({
				tmplIds: arr,
				success(re) {
					var datas = JSON.stringify(re);
					if (datas.indexOf("accept") != -1) {
						console.log(re)
					}
				},
				fail: (res) => {
					console.log(res)
				}
			})
		}
		// #endif

		uni.navigateTo({
			url: '/pages/index/detail?id=' + e.id
		})
	};
	/* 跳转搜索 */
	const goSearch = () => {
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif

			uni.navigateTo({
				url: '/my/search/index'
			});
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 选择城市 */
	const goSelectCity = () => {

		uni.chooseLocation({
			success: function(res) {
				console.log('位置名称：' + res.name);
				console.log('详细地址：' + res.address);
				console.log('纬度：' + res.latitude);
				console.log('经度：' + res.longitude);
				latitude.value = res.latitude
				longitude.value = res.longitude
				uni.setStorageSync('latitude', res.latitude)
				uni.setStorageSync('longitude', res.longitude)
				selectCity(latitude.value, longitude.value);
			}
		});

	};
	/* 经纬度 获取省市区 */
	const selectCity = (latitude, longitude) => {
		proxy.$http.get('/app/map/reverseGeocode?lat=' + latitude + '&lng=' + longitude).then(res => {
			if (res.code == 0) {
				city.value = res.data.city ? res.data.city : res.data.province
				uni.setStorageSync('city', city.value)
				page.value = 1
				getlist();
			}
		});
	};
	/* 获取轮播图 */
	const getBannerList = () => {
		proxy.$http.postJson("/app/base/banner/page", {
			classify: 1, //分类1banner图 2金刚区分类	
			state: 1 //	状态1正常 2隐藏
		}).then(res => {
			if (res.code == 0) {
				swiperList.splice(0, swiperList.length, ...res.data.records);
			}
		});
	};
	/* 金刚区滑动底部样式变动 */
	const categoryChange = (e) => {
		console.log(e.detail.current)
		currentPageindex.value = e.detail.current
	};
	// 获取金刚区分类
	const getGrid = () => {
		proxy.$http.postJson("/app/base/banner/page", {
			classify: 2, //分类1banner图 2金刚区分类	
			state: 1 //	状态1正常 2隐藏
		}).then(res => {
			if (res.code == 0) {

				if (res.data.records.length > 0) {
					var datanew = chunk(res.data.records, 10)
					gridData.splice(0, gridData.length, ...datanew);
					if (res.data.records.length > 5) {
						categoryHeight.value = "140rpx"
					} else {
						categoryHeight.value = "140rpx"
					}
				} else {
					var datanew = chunk(gridData, 10)
					gridData.splice(0, gridData.length, ...datanew);
				}


			}
		});
	};
	// 传进数组和指定个数，进行拆分
	const chunk = (array, size) => {
		//获取数组的长度，如果你传入的不是数组，那么获取到的就是undefined
		const length = array.length
		//判断不是数组，或者size没有设置，size小于1，就返回空数组
		if (!length || !size || size < 1) {
			return []
		}
		//核心部分
		let index = 0 //用来表示切割元素的范围start
		let resIndex = 0 //用来递增表示输出数组的下标

		//根据length和size算出输出数组的长度，并且创建它。
		let result = new Array(Math.ceil(length / size))
		//进行循环
		while (index < length) {
			//循环过程中设置result[0]和result[1]的值。该值根据array.slice切割得到。
			result[resIndex++] = array.slice(index, (index += size))
		}
		//输出新数组
		return result
	};
	const goNav = (url) => {
		console.log(url)
		if (userId.value) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif

			uni.navigateTo({
				url
			})

		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
	};
	/* 获取回收列表 */
	const getlist = () => {

		proxy.$http.postJson("/app/wasteItem/page", {
			page: page.value,
			limit: limit.value,
			city: city.value
		}).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}

				res.data.records.forEach(d => {
					if (d.tags) {
						d.tags = d.tags.split(',')
					}
				});
				if (page.value === 1) {
					list.splice(0, list.length, ...res.data.records);
				} else {
					list.push(...res.data.records);
				}
				status.value = 'nomore';
			}
			status.value = 'nomore';
			uni.stopPullDownRefresh();
			uni.hideLoading()
		});
	};
	// 开启订阅消息
	const openMsg = () => {
		uni.getSetting({
			withSubscriptions: true, //是否获取用户订阅消息的订阅状态，默认false不返回
			success(ret) {
				console.log(ret, '========')
				if (ret.subscriptionsSetting.itemSettings) {
					uni.setStorageSync('sendMsg', true)
					uni.openSetting({ // 打开设置页 
						success(rea) {
							console.log(rea.authSetting)
						}
					});
				} else { // 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
					uni.setStorageSync('sendMsg', false)
					uni.showModal({
						title: '提示',
						content: '为了更好的体验,请绑定消息推送',
						confirmText: '确定',
						cancelText: '取消',
						success: function(res) {
							if (res.confirm) {
								wx.requestSubscribeMessage({
									tmplIds: arr,
									success(re) {
										var datas = JSON.stringify(re);
										if (datas.indexOf("accept") != -1) {
											console.log(re)
										}
									},
									fail: (res) => {
										console.log(res)
									}
								})
								showModalMsg.value = false
							} else if (res.cancel) {
								showModalMsg.value = true
							}
						}
					})
				}
			}
		})
	};
	onPullDownRefresh(() => {
		page.value = 1;
		getlist();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading';
			getlist();
		}
	});
</script>

<style lang="scss">
	.headTop {
		padding: 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		background: #01AF74;

		.headTop_city {
			display: flex;
			align-items: center;
			margin-right: 20rpx;

			image {
				width: 27rpx;
				height: 37rpx;
			}

			.localName {
				color: #FFFFFF;
				font-size: 26rpx;
				margin-left: 10rpx;
			}
		}

		.headTop_search {
			flex: 1;
			height: 68rpx;
			background: #F7F7F7;
			border-radius: 34rpx;
			display: flex;
			align-items: center;
			padding: 0 30rpx;

			text {
				font-family: PingFang SC;
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
				margin-left: 20rpx;
			}

			image {
				width: 36rpx;
				height: 34rpx;
			}
		}
	}

	.fw {
		width: 100%;
		background: linear-gradient(to bottom, #01AF74 5%, #F7F7F7 90%);

		.fw-box {
			margin: 0 30rpx;

			.screen-swiper {
				width: 100%;
				height: 240rpx;

				image {
					width: 100%;
					height: 240rpx;
					border-radius: 24rpx;
				}
			}
		}
	}

	.gridbox {
		background-color: #FFFFFF;
		border-radius: 24rpx;
		padding-bottom: 15rpx;
		margin: 20rpx 30rpx;


		.dots {
			display: flex;
			justify-content: center;
			height: 15rpx;
			width: 100%;

			view {
				width: 10rpx;
				height: 10rpx;
				border-radius: 55%;
				background: #c8c8c8;

				&.active {
					background-color: #01AF74;
				}
			}
		}

		.swiper {
			width: 100%;
			padding: 10rpx 0;



			.category-list {
				width: 100%;
				height: auto;
				display: flex;
				justify-content: flex-start;
				padding: 10rpx 0;
				flex-flow: wrap;

				.icon {
					width: 20%;
					display: flex;
					flex-flow: wrap;
					justify-content: center;
					margin-bottom: 30rpx;

					font-family: PingFang SC;
					font-weight: 400;
					font-size: 22rpx;
					color: #333333;

					image {
						width: 100rpx;
						height: 100rpx;
					}

					view {
						width: 100%;
						display: flex;
						justify-content: center;
					}

				}
			}
		}
	}

	// 预约模块
	.yuyuebox {
		border-radius: 24rpx;
		padding: 38rpx 30rpx 30rpx;
		margin: 20rpx 30rpx;
		background: #FFFFFF;

		.yuyuebox_icon {
			display: flex;
			justify-content: space-around;


			.yuyuebox_iconBox {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 22rpx;
				color: #01AF74;
				text-align: center;

				image {
					width: 44rpx;
					height: 44rpx;
				}
			}

			.yuyuebox_iconJt {
				image {
					width: 54rpx;
					height: 19rpx;
					margin-top: 20rpx;
				}
			}
		}

		.yuyuebox_btn {
			margin-top: 30rpx;
			text-align: center;

			image {
				width: 596rpx;
				height: 98rpx;
			}
		}
	}

	// 列表样式
	.listbox {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 20rpx;
		display: flex;

		image {
			width: 180rpx;
			height: 180rpx;
			border-radius: 8px;
		}

		.listboxCot {
			height: 180rpx;
			display: flex;
			justify-content: space-between;
			flex-direction: column;
			flex: 1;
			margin-left: 20rpx;

			.listboxCot_title {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 30rpx;
				color: #1E1F31;
				width: 450rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}

			.listboxCot_tit {
				font-family: PingFang SC;
				font-weight: 400;
				font-size: 22rpx;
				color: #999999;
			}

			.listboxCot_label {
				display: flex;
				align-items: center;

				.label {
					background: #EEFFF9;
					border-radius: 4rpx;
					font-family: PingFang SC;
					font-weight: 400;
					font-size: 20rpx;
					color: #01AF74;
					padding: 2rpx 10rpx;
					margin-right: 10rpx;
				}

				.labels {
					background: #FFEFED;
					border-radius: 4rpx;
					font-family: PingFang SC;
					font-weight: 400;
					font-size: 20rpx;
					color: #F75244;
					padding: 2rpx 10rpx;
					margin-right: 10rpx;
				}
			}

			.btn {
				width: 140rpx;
				height: 52rpx;
				background: #DDFFF3;
				border-radius: 26rpx;
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 24rpx;
				color: #01AF74;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}
	}
</style>