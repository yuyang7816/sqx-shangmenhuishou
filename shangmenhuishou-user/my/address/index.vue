<template>
	<view style="padding-bottom: 200rpx;">


		<view class="addbox" v-for="(item,index) in list" :key="index" @click="changeAdd(item)">
			<view class="addbox_Top">
				<view class="addbox_TopInfo">
					<view class="addbox_TopInfo_Title">
						{{item.name}}
						<text>{{item.phone}}</text>
					</view>
					<view class="addbox_TopInfo_Tit">{{item.address}}</view>
				</view>
				<image src="/my/static/bianji.png" @click.stop="goAdd(item.id)"></image>
			</view>
			<view class="addbox_bot" :class="item.isDefault==1?'justify-between':'justify-end'">
				<view class="addbox_bot_mr" v-if="item.isDefault==1">
					<image src="/my/static/addIn.png"></image>
					<text>默认地址</text>
				</view>
				<view class="addbox_bot_dete" @click.stop='deleteAddressList(item)'>删除</view>
			</view>
		</view>

		<empty v-if="list.length==0"></empty>
		<!-- 加载数据 -->
		<up-loadmore :status="status" v-if="list.length>0" />


		<view class="tabber">
			<view class="btn" @click="goAdd()">添加地址</view>
		</view>
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
	const citys = ref('');
	const addlist = reactive([]);
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const detailaddress = ref('');
	const latitude = ref('');
	const longitude = ref('');
	const province = ref('');
	const city = ref('');
	const county = ref('');
	const cityaddress = ref('');
	const addIndex = ref('');


	onLoad(option => {
		if (option.addIndex) {
			addIndex.value = option.addIndex
		}
		citys.value = uni.getStorageSync('city')
	});
	onShow(() => {
		page.value = 1
		getAddlist();
	});
	/* 选择地址 */
	const changeAdd = (ite) => {
		uni.$emit('AddUpdate', ite)
		uni.navigateBack()

	};
	/* 添加-编辑- 地址 */
	const goAdd = (id) => {
		if (id) {
			uni.navigateTo({
				url: '/my/address/add?addressId=' + id
			})
		} else {
			uni.navigateTo({
				url: '/my/address/add'
			})
		}

	};

	// 删除
	const deleteAddressList = (e) => {
		uni.showModal({
			title: '温馨提示',
			content: '您确定要删除此地址信息吗?',
			showCancel: true,
			cancelText: '取消',
			confirmText: '确认',
			success: res => {
				if (res.confirm) {
					proxy.$http.postJson('/app/address/delete', {
						id: e.id
					}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: '删除成功！',
								icon: 'none'
							})
							page.value = 1
							getAddlist();
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					});
				}
			}
		});
	};
	const getAddlist = () => {
		let data = {
			page: page.value,
			limit: limit.value,
		}
		status.value = 'loadmore';
		proxy.$http.postJson('/app/address/page', data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
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
	onPullDownRefresh(() => {
		page.value = 1;
		getAddlist();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading';
			getAddlist();
		}
	});
</script>

<style lang="scss">
	page {
		background: #F5F5F5;
	}

	.addbox {
		background: #FFFFFF;
		margin-bottom: 20rpx;
		padding: 30rpx;

		.addbox_Top {
			display: flex;
			align-items: center;
			border-bottom: 1rpx solid #E6E6E6;
			padding-bottom: 20rpx;

			.addbox_TopInfo {
				flex: 1;
				border-right: 1rpx solid #E6E6E6;
				margin-right: 40rpx;

				.addbox_TopInfo_Title {
					font-family: PingFang SC;
					font-weight: bold;
					font-size: 32rpx;
					color: #1A1A1A;

					text {
						margin-left: 20rpx;
					}
				}

				.addbox_TopInfo_Tit {
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 28rpx;
					color: #1A1A1A;
					margin-top: 10rpx;
				}
			}

			image {
				width: 34rpx;
				height: 34rpx;
			}
		}


		.addbox_bot {
			display: flex;
			align-items: center;
			padding-top: 20rpx;

			.addbox_bot_mr {
				display: flex;
				align-items: center;

				image {
					width: 32rpx;
					height: 32rpx;
				}

				text {
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 28rpx;
					color: #999999;
					margin-left: 10rpx;
				}
			}

			.addbox_bot_dete {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 28rpx;
				color: #999999;
			}
		}
	}


	.tabber {
		position: fixed;
		bottom: 0;
		background: #F5F5F5;
		padding: 30rpx;
		left: 0;
		right: 0;

		.btn {
			width: 100%;
			height: 90rpx;
			background: #01AF74;
			border-radius: 49rpx;
			font-family: Source Han Sans CN;
			font-weight: 500;
			font-size: 28rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
</style>