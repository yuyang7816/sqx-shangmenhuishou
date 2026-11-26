<template>
	<div class="vue-mchat-page">
		<div class="nf-page-hero vue-mchat-page__hero">
			<span class="nf-page-hero__badge">聊天管理</span>
			<h2 class="nf-page-hero__title">聊天会话</h2>
			<p class="nf-page-hero__desc">与用户实时在线沟通，支持文字与图片消息，可查看用户详情与删除会话</p>
		</div>

		<div class="nf-filter-card vue-mchat-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body vue-mchat-page__filter-body">
				<div class="vue-mchat-page__field">
					<span class="vue-mchat-page__label">联系人：</span>
					<el-input class="vue-mchat-page__input" placeholder="搜索联系人" suffix-icon="el-icon-search"
						v-model="userName" clearable @change="changeSearch" @input="searchChange"></el-input>
				</div>
			</div>
		</div>

		<div class="nf-table-card vue-mchat-page__chat-card">
			<div class="whole-wrapper">
				<!-- 左边的聊天列表 -->
				<div class="left_whole_wrapper">
					<div class="left_wrapper" style="overflow-y: auto" v-if="chatList">
						<div class="left-item" style="position: relative; cursor: pointer"
							v-bind:class="{ active: index == current }" v-for="(item, index) in chatList" :key="index"
							@click="clickItem(item, index)">
							<div class="left-item-left">
								<div class="left-item-head" v-if="item.userHead == null || item.userHead == ''">
									<img src="~@/assets/img/head.png" />
								</div>
								<div class="left-item-head" v-else><img :src="item.userHead" /></div>
								<div class="left-item-info">
									<div class="left-item-info_title">{{ item.userName }}</div>
									<div class="left-item-info_content">{{ item.content }}</div>
								</div>
							</div>
							<div class="left-item-time">
								<div class="left-item-time-time" v-if="item.contentTime == null"></div>
								<div class="left-item-time-time" v-else>
									{{ item.contentTime.substring(11, 16) }}
								</div>
								<div class="left-item-time-number" style="position: absolute; left: 59px; top: 8px"
									v-if="item.storeCount">
									{{ item.storeCount }}
								</div>
								<div class="vue-mchat-page__delete-btn" @click.stop="deletes(item)"
									v-if="isAuth('vueMchat:delete')">
									<img src="~@/assets/img/delete.png" alt="" width="25" height="25" />
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 中间的对话-->
				<div class="center_whole_wrapper">
					<div class="center_wrapper">
						<!-- 上面的表头 -->
						<div class="center_wrapper_top">
							<div class="center_wrapper_top_name">{{ name }}</div>
							<el-button v-if="name" size="mini" type="primary" class="vue-mchat-page__detail-btn"
								@click="updates(userId)">用户详情
							</el-button>
						</div>
						<!-- 中间的聊天框，尝试模拟数据 -->
						<div class="center_wrapper_center" id="ele" style="overflow-y: auto" ref="dialogue_box">
							<div v-for="(item, index) in dialogueList" :key="index" :class="
                item.sendType == '1'
                  ? center_wrapper_center_item
                  : center_wrapper_center_item_reserve
              ">
								<div class="left-item-head">
									<img v-if="item.sendType == '1' && userHead !== null" :src="userHead" />
									<img v-else-if="item.sendType == '1' && userHead == null"
										src="~@/assets/img/head.png" />
									<img v-else src="~@/assets/img/avatar.png" />
								</div>
								<!-- 文字 -->
								<div class="center-item-info_wrapper" v-if="item.type == 1">
									<div class="center-item-tip">
										<div class="center-item-info">
											{{ item.content }}
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<!-- 图片 -->
								<div class="center-item-info_wrapper" v-if="item.type == 2">
									<div class="center-item-tip">
										<div>
											<el-popover placement="top-start" title="" trigger="hover">
												<img style="width: 100px; height: 100px" :src="item.content" alt=""
													slot="reference" />
												<img style="width: 300px; height: 300px" :src="item.content" alt="" />
											</el-popover>
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<!-- 订单 -->
								<div class="center-item-info_wrapper" v-if="item.type == 6">
									<div class="center-item-tip vue-mchat-page__order-tip"
										@click="orderDetails(item.content.ordersNo)">
										<div>
											<div style="display: flex">
												<img :src="item.content.homepageImg" alt="" width="40" height="40" />
												<div
													style="width: 145px;font-size: 13px;line-height: 20px;margin-left: 5px;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;">
													{{ item.content.myLevel }}
												</div>
											</div>
											<p class="hoverp" style="font-size: 13px; color: #999999">
												<span v-for="(item1, index1) in item.content.gameName"
													:key="index1">{{item1}}</span>
											</p>
											<p style="font-size: 13px; color: #999999">
												创建时间:{{ item.content.updateTime }}
											</p>
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
								<!-- 商品 -->
								<div class="center-item-info_wrapper" v-if="item.type == 7">
									<div class="center-item-tip vue-mchat-page__goods-tip">
										<div class="">
											<img :src="item.content.homepageImg" alt="" width="160" height="120" />
											<p style="width: 160px; line-height: 20px; margin-bottom: 0">
												{{ item.content.myLevel }}
											</p>
											<p style="color: #f56c6c; margin-bottom: 0">
												¥{{ item.content.money }}
											</p>
										</div>
									</div>
									<div class="center-item-time">{{ item.createTime }}</div>
								</div>
							</div>
						</div>
						<!-- 下面的输入框，发送 -->
						<div class="center_wrapper_right">
							<div class="icon-list">
								<div class="icon-item">
									<el-upload class="avatar-uploader" v-model="coverImg"
										:action="$http.adornUrl('file/upload')"
										:show-file-list="false" :on-success="handleAvatarSuccess"
										:before-upload="beforeAvatarUpload">
										<div class="icon-item" title="发送文件">
											<img class="icon-item-img" src="~@/assets/img/wenjian.png" />
										</div>
									</el-upload>
								</div>
							</div>
							<el-input type="textarea" id="textarea" :rows="2" placeholder="请输入内容" v-model="content"
								@keyup.enter.native="sendTextarea(1)"></el-input>
							<el-button type="primary" class="vue-mchat-page__send-btn" v-if="content"
								@click="sendTextarea(1)">发送
							</el-button>
							<el-button type="primary" class="vue-mchat-page__send-btn" v-else disabled
								@click="sendTextarea(1)">发送
							</el-button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				chatList: [],
				page: 1,
				size: 5,
				storeId: 0,
				targetId: "",
				userName: "",
				userId: "",
				chatId: "",
				type: "",
				current: 0,
				coverImg: "",
				sendType: "2",
				// 表情包列表
				faceList: [],
				// 发送文字
				content: "",
				// 模拟对话
				name: "",
				userHead: "",
				numlist: {},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				emojiShow: false,
				dialogueInfo: {},
				dialogueList: [],
				loadText: "加载中...",
				pageNum: 1,
				loadText1: "加载中...",
				pageNum1: 1,
				center_wrapper_center_item: "center_wrapper_center_item",
				center_wrapper_center_item_reserve: "center_wrapper_center_item_reserve",
			};
		},
		created() {
			this.loadEmojis();
		},
		watch: {
			$route: function(to, from) {
				if (to.name == "vueMchat") {
					let ele = document.getElementById("ele");
					this.$nextTick(() => {
            if(le.scrollHeight){
              ele.scrollTop = ele.scrollHeight;
            }
						
					});
				}
			},
		},
		activated() {
			let {
				userId,
				userName
			} = this.$route.query;
			if (userId && userName) {
				this.userName = userName;
				this.current = 0;
			} else {
				this.userName = "";
			}
			this.dataSelect();
			this.initWebSocket();
		},
		methods: {
			initWebSocket() {

				this.websock = new WebSocket(
					this.$http.adornWss("websocket/0"));
				this.websock.onmessage = this.websocketonmessage;
				this.websock.onerror = this.websocketonerror;
				this.websock.onopen = this.websocketonopen;
				this.websock.onclose = this.websocketclose;

			},
			websocketonopen() {
				console.log("WebSocket连接成功");

			},
			websocketonerror() {
				console.log("WebSocket连接失败");
			},
			websocketonmessage(e) {
				let msg = JSON.parse(e.data);
				this.dialogueList.push(msg);
				console.log(msg);

				let ele = document.getElementById("ele");
				this.dataSelect();
				this.$nextTick(() => {
					ele.scrollTop = ele.scrollHeight;
				});
			},
			websocketclose(e) {
				console.log("已关闭连接", e);
			},
			loadEmojis() {
				const appData = require("@/assets/img/emojis.json");
				for (let i in appData) {
					this.faceList.push(appData[i].char);
				}
			},
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear();
				var month = now.getMonth();
				var date = now.getDate();
				var hour = now.getHours();
				var minute = now.getMinutes();
				var second = now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hour}:${minute}:${second}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			updates(userId) {
				this.$router.push({
					path: "/userDetail",
					query: {
						userId: userId
					}
				});
			},
			orderDetails(ordersNo) {
				this.$router.push({
					path: "/orderCenter",
					query: {
						ordersNo: ordersNo
					}
				});
			},
			getEmo(index) {
				var textArea = document.getElementById("textarea");

				function changeSelectedText(obj, str) {
					if (window.getSelection) {
						textArea.setRangeText(str);
						textArea.selectionStart += str.length;
						textArea.focus();
					} else if (document.selection) {
						obj.focus();
						var sel = document.selection.createRange();
						sel.text = str;
					}
				}
				changeSelectedText(textArea, this.faceList[index]);
				this.description = textArea.value;
				this.emojiShow = false;
				return;
			},
			changeSearch(e) {
				if (!e) {
					this.userName = ""
					this.dataSelect();
				}
			},
			searchChange() {
				this.dataSelect();
			},
			clickItem(item, index) {
				this.current = index;
				this.userId = item.userId;
				this.name = item.userName;
				this.userHead = item.userHead;
				this.chatId = item.chatId;
				this.chatList[index].storeCount = 0
				this.clickItem2();
				this.initWebSocket();
			},


			clickItem2() {
				this.$http({
					url: this.$http.adornUrl("chatsContent/storeList"),
					method: "get",
					params: this.$http.adornParams({
						chatId: this.chatId,
					}),
				}).then(({
					data
				}) => {
					for(var i in data.data){
						if(data.data[i].type==6||data.data[i].type==7){
							var datas = JSON.parse(data.data[i].content)
							console.log('datas',data.data[i].content)
							data.data[i].content = datas
						}
					}
					let returnData = data.data;
					this.dialogueList = returnData;
					let ele = document.getElementById("ele");
					this.$nextTick(() => {
						ele.scrollTop = ele.scrollHeight;
					});
					
				});
			},
			sendTextarea(type) {
				let data = {
					content: this.content,
					type: type,
					createTime: this.info.stockDate,
					sendType: this.sendType,
					userId: this.userId,
					storeId: this.storeId,
					chatId: this.chatId,
				};
				this.numlist = data;
				data = JSON.stringify(data);
				this.websock.send(data);
				this.content = "";
				this.dialogueList.push(this.numlist);
				let ele = document.getElementById("ele");
				this.$nextTick(() => {
					ele.scrollTop = ele.scrollHeight;
				});
				this.initWebSocket();
				this.dataSelect();
				this.clickItem2()
			},
			deletes(item) {
				this.$confirm(`确定删除聊天记录，删除后不能恢复?`, "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						this.$http({
							url: this.$http.adornUrl(`chats/delete/?id=${item.chatId}`),
							method: "get",
							params: this.$http.adornParams({}),
						}).then(({
							data
						}) => {
							this.$message({
								message: "删除成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.dataSelect();
								},
							});
						});
					})
					.catch(() => {});
			},
			handleAvatarSuccess(file, fileList) {
				this.coverImg = file.data.path;
				this.content = this.coverImg;
				this.sendTextarea(2);
			},
			dataSelect() {
				this.tableDataLoading = true;
				this.$http({
					url: this.$http.adornUrl("chats/list"),
					method: "get",
					params: this.$http.adornParams({
						userName: this.userName,
						storeId: this.storeId,
					}),
				}).then(({
					data
				}) => {
					this.tableDataLoading = false;
					let returnData = data.data;
					this.chatList = returnData;

					this.clickItem(this.chatList[this.current], this.current);
				});
			},
			beforeAvatarUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				var extension;
				if (testmsg == 'png' || testmsg == 'jpg' || testmsg == 'jpeg' || testmsg == 'gif') {
					extension = testmsg
				}
				if (!extension) {
					this.$message({
						message: "只能上传图片！",
						type: 'error'
					})
				}
				return extension;
			},
		},
		mounted() {
		},
		destroyed() {
			window.clearInterval(this.timer);
			if (this.websock) {
				this.websock.close();
			}
		},
	};
</script>

<style lang="scss" scoped>
.vue-mchat-page {
	padding: 0;
	overflow-x: hidden;
}

.vue-mchat-page__hero {
	margin-bottom: 12px;
}

.vue-mchat-page__filter {
	margin-bottom: 12px;
}

.vue-mchat-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.vue-mchat-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.vue-mchat-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.vue-mchat-page__input {
	width: 280px;
}

.vue-mchat-page__chat-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.whole-wrapper {
	display: flex;
	background: #fff;
	min-height: calc(100vh - 320px);
	height: 560px;
	position: relative;
	border-radius: var(--nf-radius-md, 8px);
}

/* 左边部分 */
.left_whole_wrapper {
	display: flex;
	flex-direction: column;
	width: 28%;
	min-width: 240px;
	border-right: 1px solid var(--nf-border, #e8eaed);
}

.left_wrapper {
	flex: 1;
	background-color: var(--nf-page-bg, #f4f6fa);
}

.left-item {
	display: flex;
	justify-content: space-between;
	padding: 14px 16px;
	background-color: #fff;
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	transition: background-color 0.15s ease;
}

.left-item:hover {
	background-color: #fafbfc;
}

.left-item.active {
	background-color: var(--nf-brand-lite, #e1f5ee);
	border-left: 3px solid var(--nf-brand, #01af74);
	padding-left: 13px;
}

.left-item-left {
	display: flex;
	min-width: 0;
}

.left-item-head>img {
	width: 44px;
	height: 44px;
	border-radius: 50%;
	object-fit: cover;
}

.left-item-info {
	margin-left: 12px;
	max-width: 135px;
	display: flex;
	flex-direction: column;
	justify-content: space-around;
	min-width: 0;
}

.left-item-info_title {
	font-size: 14px;
	font-weight: 500;
	text-align: left;
	color: var(--nf-text-primary, #1a2433);
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.left-item-info_content {
	font-size: 13px;
	text-align: left;
	color: var(--nf-text-secondary, #5a6578);
	margin-top: 4px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.left-item-time {
	display: flex;
	flex-direction: column;
	text-align: right;
	align-items: flex-end;
	flex-shrink: 0;
}

.left-item-time-time {
	font-size: 12px;
	text-align: right;
	color: var(--nf-text-secondary, #5a6578);
}

.left-item-time-number {
	width: 20px;
	height: 20px;
	line-height: 20px;
	border-radius: 50%;
	background: #ff4949;
	color: #ffffff;
	text-align: center;
	font-size: 12px;
	margin-top: 5px;
}

.vue-mchat-page__delete-btn {
	position: relative;
	top: 6px;
	cursor: pointer;
	opacity: 0.7;
	transition: opacity 0.15s ease;
}

.vue-mchat-page__delete-btn:hover {
	opacity: 1;
}

/* 中间部分 */
.center_whole_wrapper {
	flex: 1;
	background: var(--nf-page-bg, #f4f6fa);
	min-width: 0;
}

.center_wrapper {
	height: 100%;
	display: flex;
	flex-direction: column;
}

.center_wrapper_top {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 16px 20px;
	flex-shrink: 0;
	position: relative;
	background: #fff;
	border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.center_wrapper_top_name {
	font-size: 16px;
	font-weight: 600;
	text-align: left;
	color: var(--nf-text-primary, #1a2433);
}

.vue-mchat-page__detail-btn {
	border-radius: 6px;
}

.center_wrapper_center {
	flex: 1;
	padding: 16px 20px 0;
	overflow-y: auto;
}

.center_wrapper_center_item {
	display: flex;
	margin-bottom: 24px;
}

.center_wrapper_right {
	flex-shrink: 0;
	background-color: #ffffff;
	padding: 12px 20px 16px;
	position: relative;
	border-top: 1px solid var(--nf-border, #e8eaed);
}

.center-item-info_wrapper {
	margin: 0 12px;
}

.center-item-tip {
	display: flex;
	align-items: center;
}

.center_wrapper_center_item .center-item-info {
	background: #ffffff;
	border-radius: 8px;
	border: 1px solid var(--nf-border, #e8eaed);
}

.center_wrapper_center_item_reserve .center-item-info {
	background: var(--nf-brand-lite, #e1f5ee);
	border-radius: 8px;
	border: 1px solid rgba(1, 175, 116, 0.25);
}

.center-item-info {
	max-width: 336px;
	font-size: 14px;
	text-align: left;
	color: var(--nf-text-primary, #1a2433);
	line-height: 22px;
	padding: 12px 14px;
	word-wrap: break-word;
	word-break: break-all;
}

.center-item-time {
	font-size: 12px;
	text-align: left;
	color: var(--nf-text-secondary, #5a6578);
	line-height: 22px;
	margin-top: 8px;
}

.center_wrapper_center_item_reserve {
	display: flex;
	flex-direction: row-reverse;
	margin-bottom: 24px;
}

.center_wrapper_center_item_reserve .center-item-time {
	text-align: right;
}

.center_wrapper_center_item_reserve .center-item-tip {
	display: flex;
	flex-direction: row-reverse;
	align-items: center;
}

.vue-mchat-page__order-tip {
	background-color: #fff;
	cursor: pointer;
	padding: 10px;
	border-radius: 8px;
	border: 1px solid var(--nf-border, #e8eaed);
}

.vue-mchat-page__goods-tip {
	background-color: #fff;
	padding: 10px;
	border-radius: 8px;
	border: 1px solid var(--nf-border, #e8eaed);
}

.icon-list {
	display: flex;
	margin-bottom: 8px;
}

.icon-list>div:not(:first-child) {
	margin-left: 15px;
}

.icon-item-img {
	width: 25px;
}

.whole-wrapper>>>.el-input__inner {
	border-radius: 6px;
	border-color: var(--nf-border, #e8eaed);
}

.whole-wrapper>>>.el-textarea__inner {
	min-height: 72px;
	resize: none;
	border: none;
	background: #ffffff;
	padding: 4px 0;
	box-shadow: none;
}

.vue-mchat-page__send-btn {
	width: 88px;
	height: 36px;
	position: absolute;
	bottom: 16px;
	right: 20px;
	font-size: 13px;
	border-radius: 6px;
	background-color: transparent !important;
	border-color: var(--nf-brand, #01af74) !important;
	color: var(--nf-brand, #01af74) !important;
}

.vue-mchat-page__send-btn:hover:not(:disabled),
.vue-mchat-page__send-btn:focus:not(:disabled) {
	background-color: var(--nf-brand-lite, #e1f5ee) !important;
	color: var(--nf-brand-deep, #017c52) !important;
	border-color: var(--nf-brand-deep, #017c52) !important;
}

.vue-mchat-page__send-btn.is-disabled {
	background-color: transparent !important;
	border-color: var(--nf-border, #e8eaed) !important;
	color: var(--nf-text-secondary, #5a6578) !important;
	opacity: 0.6;
}

.loading {
	text-align: center;
	color: var(--nf-text-secondary, #5a6578);
}

.loading span {
	margin-left: 10px;
}

.center_wrapper_center_item_reserve,
.center_wrapper_center_item {
	position: relative;
}

.center_wrapper_center_item_reserve .center-item-info::after {
	content: "";
	position: absolute;
	right: 55px;
	top: 12px;
	width: 0;
	height: 0;
	border: 5px solid var(--nf-brand-lite, #e1f5ee);
	border-top-color: transparent;
	border-right-color: transparent;
	border-bottom-color: transparent;
}

.center_wrapper_center_item .center-item-info::after {
	content: "";
	position: absolute;
	left: 55px;
	top: 12px;
	width: 0;
	height: 0;
	border: 5px solid #ffffff;
	border-top-color: transparent;
	border-left-color: transparent;
	border-bottom-color: transparent;
}
</style>

<style lang="scss">
.el-popover {
	height: 200px;
	width: 400px;
	overflow: scroll;
	overflow-x: auto;
	font-size: 20px;
}
</style>
