<template>
	<view>
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
	const id = ref('');
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');

	onLoad(option => {
		if (option.name) {
			uni.setNavigationBarTitle({
				title: option.name
			})
		}
		if (option.id) {
			id.value = option.id
		}
		getlist()
	});
	onShow(() => {

	});
	/* 跳转服务详情 */
	const goOrder = (e) => {
		uni.navigateTo({
			url: '/pages/index/detail?id=' + e.id
		})
	};
	/* 获取回收列表 */
	const getlist = () => {

		proxy.$http.postJson("/app/wasteItem/pageByCategoryId", {
			page: page.value,
			limit: limit.value,
			categoryId: id.value
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
				font-size: 28rpx;
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