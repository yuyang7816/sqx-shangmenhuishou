<template>
	<view class="content">
		<view class="search-box">
			<!-- mSearch组件 如果使用原样式，删除组件元素-->
			<!-- <mSearch class="mSearch-input-box" :mode="2" button="inside" :placeholder="defaultKeyword"
				@search="doSearch(false)" @input="inputChange" @confirm="doSearch(false)" v-model="keyword"></mSearch> -->
			<!-- 原样式 如果使用原样式，恢复下方注销代码 -->

			<!-- <view class="input-box">
				<input type="text" :adjust-position="true" :placeholder="defaultKeyword" @input="inputChange" v-model="keyword" @confirm="doSearch(false)"
				 placeholder-class="placeholder-class" confirm-type="search">
			</view>
			<view class="search-btn" @tap="doSearch(false)">搜索</view> -->
			<u-search style="width: 100%;" placeholder="输入搜索内容" :focus="true" v-model="keyword" :show-action="true"
				:animation="true" shape="square" bg-color="#F7F7F7" color="#1A1A1A" action-text="取消" @custom="goBack()"
				@search="doSearch(false)"></u-search>
			<!-- 原样式 end -->

		</view>
		<!-- #ifdef MP-WEIXIN -->
		<view style="position: relative;top: 90rpx;left: 0;right: 0;z-index:1;">
			<!-- #endif -->
			<!-- #ifndef MP-WEIXIN -->
			<view style="position: relative;top: 46px;left: 0;right: 0;z-index:1;">
				<!-- #endif -->
				<view class="search-keyword">
					<scroll-view class="keyword-list-box" v-show="isShowKeywordList" scroll-y>
						<view class="margin-lr-sm padding-top-16 ">
							<view class="flex justify-between padding-sm radius margin-top-xs"
								v-for="(item, index) in keywordList" :key="index" @click="goOrder(item)"
								style="background-color: #FFFFFF;">
								<image :src="item.image?item.image: '../../../static/logo.png'"
									style="width: 200rpx;height: 200rpx;border-radius: 10rpx;"></image>
								<view class="flex-sub margin-left text-white flex flex-direction justify-between">
									<view class=" text-30"
										style="display: inline-block;width: 420rpx; overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
										{{item.title}}
									</view>
									<view class="flex" style="align-items: center;font-size: 24rpx;" v-if="item.count">
										<view style="color: #999999;padding: 5rpx 00rpx;">
											已预约{{item.sales}}
										</view>
									</view>
									<view class="flex" style="align-items: center;font-size: 24rpx;">
										<view class="margin-right-sm"
											style="color: #01AF74;background: #DDFFF3; padding: 5rpx 10rpx;border-radius: 5rpx;">
											{{item.labels[0]}}
										</view>
										<view
											style="color: #F75244;background: #FFDFDC; padding: 5rpx 10rpx;border-radius: 5rpx;">
											{{item.labels[1]}}
										</view>
									</view>
									<view style="width: 100%;display: flex;justify-content: end;align-items: center;">
										<view class="text-sm"
											style="background:#DDFFF3;color:#01AF74;padding: 12rpx 38rpx;border-radius: 45rpx;">
											预约回收
										</view>
									</view>
								</view>
							</view>
						</view>
						<!-- <view v-if="keywordList.length == 0">
						暂无数据
					</view> -->
						<empty v-if="keywordList.length == 0"></empty>
					</scroll-view>
					<scroll-view class="keyword-box" v-show="!isShowKeywordList" scroll-y>
						<!-- <view class="keyword-block">
						<view class="keyword-list-header">
							<view>热门搜索</view>
							<view>
								<image @tap="hotToggle" :src="'/static/images/index/attention'+forbid+'.png'"></image>
							</view>
						</view>
						<view class="keyword" v-if="forbid==''">
							<view v-for="(keyword,index) in hotKeywordList" @tap="doSearch(keyword)" :key="index">
								{{keyword}}
							</view>
						</view>
						<view class="hide-hot-tis" v-else>
							<view>当前搜热已隐藏</view>
						</view>
					</view> -->
						<view class="keyword-block" v-if="oldKeywordList.length>0">
							<view class="keyword-list-header">
								<view>历史记录</view>
								<view>
									<image @tap="oldDelete" src="/static/images/index/delete.png"></image>
								</view>
							</view>
							<view class="keyword">
								<view v-for="(keyword,index) in oldKeywordList" @tap="doSearch(keyword.search_name)"
									:key="index">
									{{keyword.search_name}}
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
			</view>
		</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				defaultKeyword: "",
				keyword: "",
				oldKeywordList: [], //历史记录
				hotKeywordList: [], //热搜
				keywordList: [], //搜索列表
				forbid: '',
				isShowKeywordList: false,
				limit: 10,
				page: 1,
				userId: '',
				isVip: false,
				
			}
		},
		onLoad() {
			this.userId = uni.getStorageSync('userId')
			if (this.userId) {
				this.getSearchList()
			}
			this.isVip = uni.getStorageSync('isVIP')

		},
		methods: {
			// 获取搜索历史
			getSearchList() {
				this.$Request.get("/api/index/index/search_history").then(res => {
					if (res.code == 1 && res.data) {
						this.oldKeywordList = res.data
						// for (let i = 0; i < this.oldKeywordList.length; i++) {
						// 	this.oldKeywordList[i].gameName = this.oldKeywordList[i].gameName.split(",");
						// }
						// this.hotKeywordList = res.data.allSerchName


					}
				});
			},
			//清除历史搜索
			oldDelete() {
				uni.showModal({
					content: '确定清除历史搜索记录？',
					success: (res) => {
						if (res.confirm) {
							console.log('用户点击确定');
							this.$Request.get("/api/index/index/del_search_history").then(res => {
								if (res.code == 1) {
									this.getSearchList()
								}
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			//执行搜索
			doSearch(keyword) {
				this.keyword = keyword === false ? this.keyword : keyword;
				this.isShowKeywordList = true;
				if (!this.keyword) {
					uni.showToast({
						title: '请输入内容',
						icon: 'none',
						duration: 1000
					})
					return
				}

				let data = {
					search: this.keyword,
					num: this.limit,
					page: this.page,
					// condition: this.value1, //智能优选
					// salesNum: this.value2, //不限男女
					// by: this.value3, //价格
					// city: uni.getStorageSync('city'),
					// latitude: uni.getStorageSync('latitude'),
					// longitude: uni.getStorageSync('longitude')
				}
				this.$Request.get("/api/index/index/search_artificer", data).then(res => {
					if (res.code == 1) {
						if (this.page == 1) this.keywordList = [];
						for (let i = 0; i < res.data.data.length; i++) {
							if (res.data.data[i].labels) {
								res.data.data[i].labels = res.data.data[i].labels.split(",");
							}
						}
						this.keywordList = [...this.keywordList, ...res.data.data]

					}
				});
			},
			// 点击取消返回首页
			goBack() {
				uni.navigateBack()
			},
			//热门搜索开关
			hotToggle() {
				this.forbid = this.forbid ? '' : '_forbid';
			},
			// 跳转订单
			goOrder(e) {
				if (this.userId) {
					uni.navigateTo({
						url: '/pages/index/game/order?id=' + e.id
					});
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
			},

		},
		onReachBottom: function() {
			if (this.keyword) {
				this.page = this.page + 1;
				this.doSearch(false);
			}

		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.doSearch(false);
		},
	}
</script>
<style>
	page {
		background-color: #F7F7F7;
	}

	.bg {
		background-color: #FFFFFF;
	}

	.search-box {
		width: 100%;
		/* background-color: rgb(242, 242, 242); */
		padding: 15rpx 2.5%;
		display: flex;
		justify-content: space-between;
		position: sticky;
		top: 0;
		background-color: #FFFFFF;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		z-index: 9;
		/* #ifdef H5 */
		position: fixed;
		top: 45px;
		left: 0;
		right: 0;
		/* #endif */
	}

	.search-box .mSearch-input-box {
		width: 100%;
	}

	.search-box .input-box {
		width: 85%;
		flex-shrink: 1;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.search-box .search-btn {
		width: 15%;
		margin: 0 0 0 2%;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-shrink: 0;
		font-size: 28rpx;
		color: #fff;
		background: linear-gradient(to right, #ff9801, #ff570a);
		border-radius: 60rpx;
	}

	.search-box .input-box>input {
		width: 100%;
		height: 60rpx;
		font-size: 32rpx;
		border: 0;
		border-radius: 60rpx;
		-webkit-appearance: none;
		-moz-appearance: none;
		appearance: none;
		padding: 0 3%;
		margin: 0;
		background-color: #ffffff;
	}

	.placeholder-class {
		color: #9e9e9e;
	}

	.search-keyword {
		width: 100%;

	}

	.keyword-list-box {
		height: calc(100vh - 110rpx);
		padding-top: 10rpx;
		/* border-radius: 20rpx 20rpx 0 0; */
		/* background-color: #fff; */
	}

	.keyword-entry-tap {
		background-color: #eee;
	}

	.keyword-entry {
		width: 94%;
		height: 80rpx;
		margin: 0 3%;
		font-size: 30rpx;
		color: #333;
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: solid 1rpx #e7e7e7;
	}

	.keyword-entry image {
		width: 60rpx;
		height: 60rpx;
	}

	.keyword-entry .keyword-text,
	.keyword-entry .keyword-img {
		height: 80rpx;
		display: flex;
		align-items: center;
	}

	.keyword-entry .keyword-text {
		width: 90%;
	}

	.keyword-entry .keyword-img {
		width: 10%;
		justify-content: center;
	}

	.keyword-box {
		height: calc(100vh - 110rpx);
		/* border-radius: 20rpx 20rpx 0 0; */
		background-color: #F7F7F7;
	}

	.keyword-box .keyword-block {
		padding: 10rpx 0;
	}

	.keyword-box .keyword-block .keyword-list-header {
		/* width: 94%; */
		padding: 10rpx 3%;
		font-size: 27rpx;
		font-weight: 700;
		/* color: #FFFFFF; */
		display: flex;
		justify-content: space-between;
	}

	.keyword-box .keyword-block .keyword-list-header image {
		width: 40rpx;
		height: 40rpx;
	}

	.keyword-box .keyword-block .keyword {
		width: 94%;
		padding: 3px 3%;
		display: flex;
		flex-flow: wrap;
		justify-content: flex-start;
	}

	.keyword-box .keyword-block .hide-hot-tis {
		display: flex;
		justify-content: center;
		font-size: 28rpx;
		color: #FFFFFF;
	}

	.keyword-box .keyword-block .keyword>view {
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 10rpx;
		padding: 0 20rpx;
		margin: 10rpx 20rpx 10rpx 0;
		height: 60rpx;
		font-size: 28rpx;
		background-color: #FFFFFF;
		color: #343546;
	}
</style>