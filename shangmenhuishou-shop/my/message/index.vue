<template>
	<view>
		<up-sticky bgColor="#FFFFFF">
			<view class="">
				<up-tabs :list="listTab" :scrollable="false" lineColor="#01AF74" @click="clickTab"
					:current="current"></up-tabs>
			</view>
		</up-sticky>

		<view v-for="(item, index) in list" :key="index" class="item" @click="goDet(item.content)">
			<view class="item_top">
				<view class="item_topTit">{{ item.title }}</view>
				<view v-if="item.status ==0" class="dian">
				</view>
			</view>
			<view class="tit">{{ item.content }}</view>

			<view class="tit" style="text-align: right;">{{ item.createTime }}
			</view>
			<view v-if="list.length-1!=index" style="width: 100%;height: 1rpx;background: #f5f5f5;margin-top: 30rpx;">
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
	import empty from '@/components/empty/empty.vue';
	const {
		proxy
	} = getCurrentInstance();
	// 创建响应式数据  
	const listTab = reactive([{
			name: '用户消息',
			type: 2
		},
		{
			name: '订单消息',
			type: 1
		}
	]);
	const current = ref(0);
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');


	onLoad(option => {
		getlist()
	});
	onShow(() => {

	});
	// 定义方法  
	const clickTab = (item) => {
		console.log('item', item);
		current.value = item.index
		page.value = 1
		getlist()
	};
	const getlist = () => {
		uni.showLoading({
			title: '加载中...'
		});
		let type = listTab[current.value].type
		let data = {
			page: page.value,
			limit: limit.value,
			type: type
		}
		status.value = 'loadmore';
		proxy.$http.postJson('/app/messages/page', data).then(res => {
			if (res.code === 0) {
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
			uni.stopPullDownRefresh(); // 停止刷新
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
	.item {
		background: #FFFFFF;
		padding: 20rpx 30rpx;

		.item_top {
			display: flex;
			justify-content: space-between;

			width: 100%;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;

			.item_topTit {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 30rpx;
				color: #1E1F31;
			}

			.dian {
				height: 25rpx;
				width: 25rpx;
				border-radius: 100rpx;
				background-color: red;
				color: #FFF;
				text-align: center;
			}
		}

		.tit {
			font-family: PingFang SC;
			color: #999999;
			font-size: 28rpx;
			margin-top: 10rpx;
		}
	}
</style>