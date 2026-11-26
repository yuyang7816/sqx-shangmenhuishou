<template>
	<view>
		<up-sticky>
			<view class="search-box">
				<up-search placeholder="搜索你需要的内容" v-model="keyword" shape="square" actionText="取消"
					@custom="custom" @search="search"></up-search>
			</view>
		</up-sticky>

		<view class="keyword_box" v-if="!isTrue">
			<view class="keyword_boxHead">
				<view class="keyword_boxHead_tit">历史记录</view>
				<view class="keyword_boxHead_img" @tap="oldDelete">
					<image src="../static/delete.png" mode="widthFix"></image>
				</view>
			</view>
			<view class="keyword" v-if="Historylist.length!=0">
				<view v-for="(item,index) in Historylist" :key="index" @click="bindTab(item.keyword)">
					{{item.keyword}}
				</view>
			</view>
			
			<view class="keyword_boxEmpy">暂无历史记录</view>

		</view>
		
		<view class="" v-else>
			
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
	const keyword = ref('');
	const Historylist = reactive([]);
	
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const isTrue = ref(false);

	onLoad(option => {

	});
	onShow(() => {
		getHistory()
	});
	/* 跳转服务详情 */
	const goOrder = (e) => {
		uni.navigateTo({
			url: '/pages/index/detail?id=' + e.id
		})
	};
	const custom = () => {
		console.log('=====')
		uni.navigateBack()
	};
	/* 查询搜索历史 */
	const getHistory = () => {
		proxy.$http.postJson('/app/searchHistory/list', {
			userId:uni.getStorageSync('userId')
		}).then(res => {
			if (res.code == 0) {
				Historylist.splice(0, Historylist.length, ...res.data);
			}
		});
	};
	/* 清除历史搜索 */
	const oldDelete = () => {
		uni.showModal({
			content: '确定清除历史搜索记录？',
			success: (res) => {
				if (res.confirm) {
					console.log('用户点击确定');
					proxy.$http.get('/app/searchHistory/clear').then(res => {
						if (res.code == 0) {
							getHistory()
						}
					});
				} else if (res.cancel) {
					console.log('用户点击取消');
				}
			}
		});
	};
	
	const bindTab=(e)=>{
		console.log(e)
		keyword.value = e
		isTrue.value = true
		page.value = 1
		getlist()
	};
	const search=(e)=>{
		console.log(e)
		isTrue.value = true
		page.value = 1
		getlist()
	};
	/* 获取回收列表 */
	const getlist = () => {

		proxy.$http.postJson("/app/searchHistory/search", {
			page: page.value,
			limit: limit.value,
			keyword:keyword.value
		}).then(res => {
			if (res.code == 0&&res.data) {
				pages.value = res.data.wasteItems.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				
				res.data.wasteItems.records.forEach(d=>{
					if(d.tags){
						d.tags = d.tags.split(',')
					}
				});
				if (page.value === 1) {
					list.splice(0, list.length, ...res.data.wasteItems.records);
				} else {
					list.push(...res.data.wasteItems.records);
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
	page {
		background: #f2f2f2;
	}

	.search-box {
		background: #FFFFFF;
		padding: 30rpx;
	}

	.keyword_box {
		padding: 0 30rpx;
		background: linear-gradient(to bottom,#FFFFFF 80%,#f2f2f2);
		height: 80vh;

		.keyword_boxHead {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.keyword_boxHead_tit {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 28rpx;
				color: #333333;
			}

			.keyword_boxHead_img {
				image {
					width: 30rpx;
					height: 30rpx;
				}
			}
		}

		.keyword {
			display: flex;
			align-items: center;
			flex-wrap: wrap;
			

			view {
				border-radius: 10rpx;
				padding: 15rpx 20rpx;
				margin: 10rpx 20rpx 10rpx 0;
				background-color: #F5F5F5;

				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #333333;
			}
		}
		.keyword_boxEmpy{
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #999999;
			display: flex;
			align-items: center;
			justify-content: center;
			padding: 30rpx 0;
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