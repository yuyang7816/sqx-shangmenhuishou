<template>
	<div class="chat-record-page">
		<div class="nf-page-hero chat-record-page__hero">
			<span class="nf-page-hero__badge">聊天管理</span>
			<h2 class="nf-page-hero__title">聊天记录</h2>
			<p class="nf-page-hero__desc">查看用户聊天会话列表，支持按昵称筛选，可查看详细聊天内容与删除记录</p>
		</div>

		<div class="nf-filter-card chat-record-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body chat-record-page__filter-body">
				<div class="chat-record-page__field">
					<span class="chat-record-page__label">用户昵称：</span>
					<el-input class="chat-record-page__input" @keydown.enter.native="select" clearable
						placeholder="请输入用户昵称" v-model="nickName">
					</el-input>
				</div>
				<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
				<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
			</div>
		</div>

		<div class="nf-table-card chat-record-page__table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list" class="chat-record-page__table">
				<el-table-column fixed prop="chatConversationId" label="id" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称" width="150">
					<template slot-scope="scope">
						<div class="chat-record-page__link" @click="updates(scope.row.userId)">
							{{scope.row.userName ? scope.row.userName : '未绑定'}}</div>
						<div class="chat-record-page__link" @click="updates(scope.row.byUserId)">
							{{scope.row.byUserName ? scope.row.byUserName : '未绑定'}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="picture" label="用户头像" width="80">
					<template slot-scope="scope">
						<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
							暂无图片
						</div>
						<div v-else>
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="chat-record-page__thumb" :src="scope.row.avatar" alt="" slot="reference">
								<img class="chat-record-page__preview" :src="scope.row.avatar" alt="">
							</el-popover>
						</div>
						<div v-if="scope.row.byAvatar == null || scope.row.byAvatar == ''">
							暂无图片
						</div>
						<div v-else class="chat-record-page__avatar-gap">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="chat-record-page__thumb" :src="scope.row.byAvatar" alt="" slot="reference">
								<img class="chat-record-page__preview" :src="scope.row.byAvatar" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="最新聊天内容">
					<template slot-scope="scope">
						<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
						<div v-if="scope.row.messageType==2">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="chat-record-page__thumb" :src="scope.row.content" alt="" slot="reference">
								<img class="chat-record-page__preview" :src="scope.row.content" alt="">
							</el-popover>
						</div>
						<div v-if="scope.row.messageType==3">
							<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
						</div>
						<div v-if="scope.row.messageType==4">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="chat-record-page__thumb"
									:src="'https://taskshop.xianmxkj.com/emoji/'+scope.row.content" alt="" slot="reference">
								<img class="chat-record-page__preview"
									:src="'https://taskshop.xianmxkj.com/emoji/'+scope.row.content" alt="">
							</el-popover>
						</div>
						<div v-if="scope.row.messageType==5">
							<span><i class="el-icon-location chat-record-page__location-icon"></i>{{scope.row.content.address}}
							</span>
						</div>
						<div v-if="scope.row.messageType==6">
							<span>订单咨询：{{scope.row.content.ordersNo}}
								<span class="chat-record-page__link" @click="orderBtn(scope.row.content.ordersNo)">查看</span>
							</span>
							<div class="chat-record-page__order-card">
								<div class="chat-record-page__order-img">
									<el-popover placement="top-start" title="" trigger="hover">
										<img class="chat-record-page__thumb" :src="scope.row.content.homepageImg" alt=""
											slot="reference">
										<img class="chat-record-page__preview" :src="scope.row.content.homepageImg"
											alt="">
									</el-popover>
								</div>
								<div class="chat-record-page__order-info">
									<div class="chat-record-page__order-level">{{scope.row.content.myLevel}}</div>
									<div class="chat-record-page__order-games">
										<span v-for="(item,index) in scope.row.content.gameName" :key="index">{{item}}
										</span>
									</div>
									<div class="chat-record-page__order-price">¥{{scope.row.content.payMoney}}</div>
								</div>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="updateTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed='right' label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="complainDetails(scope.row)"
							class="chat-record-page__action-btn">聊天内容
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('chatRecord:delete')"
							class="chat-record-page__action-btn" @click="deleteuserHh(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="chat-record-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>

		<!-- 聊天内容 -->
		<el-dialog title="聊天内容" :visible.sync="dialogFormVisible" center width="80%">
			<div class="chat-record-page__dialog-filter">
				<div class="chat-record-page__field">
					<span class="chat-record-page__label">内容：</span>
					<el-input class="chat-record-page__input" @keydown.enter.native="select1" clearable
						placeholder="请输入聊天内容" v-model="content">
					</el-input>
				</div>
				<el-button size="mini" type="primary" icon="el-icon-search" @click="select1">查询</el-button>
				<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans1">重置</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="userData.list" class="chat-record-page__dialog-table">
				<el-table-column prop="chatContentId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称" width="120">
					<template slot-scope="scope">
						<div class="chat-record-page__link" @click="updates(scope.row.userId)">
							{{scope.row.userName ? scope.row.userName : '未绑定'}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="用户头像" width="80">
					<template slot-scope="scope">
						<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
							暂无图片
						</div>
						<div v-else>
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="chat-record-page__thumb" :src="scope.row.avatar" alt="" slot="reference">
								<img class="chat-record-page__preview" :src="scope.row.avatar" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="信息内容">
					<template slot-scope="scope">
						<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
						<div v-if="scope.row.messageType==2">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="chat-record-page__thumb" :src="scope.row.content" alt="" slot="reference">
								<img class="chat-record-page__preview" :src="scope.row.content" alt="">
							</el-popover>
						</div>
						<div v-if="scope.row.messageType==3">
							<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
						</div>
						<div v-if="scope.row.messageType==4">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="chat-record-page__thumb"
									:src="'https://taskshop.xianmxkj.com/emoji/'+scope.row.content" alt=""
									slot="reference">
								<img class="chat-record-page__preview"
									:src="'https://taskshop.xianmxkj.com/emoji/'+scope.row.content" alt="">
							</el-popover>
						</div>
						<div v-if="scope.row.messageType==5">
							<span><i class="el-icon-location chat-record-page__location-icon"></i>{{scope.row.content.address}}
							</span>
						</div>
						<div v-if="scope.row.messageType==6">
							<span>订单咨询：{{scope.row.content.ordersNo}}
								<span class="chat-record-page__link" @click="orderBtn(scope.row.content.ordersNo)">查看</span>
							</span>
							<div class="chat-record-page__order-card">
								<div class="chat-record-page__order-img">
									<el-popover placement="top-start" title="" trigger="hover">
										<img class="chat-record-page__thumb" :src="scope.row.content.homepageImg"
											alt="" slot="reference">
										<img class="chat-record-page__preview" :src="scope.row.content.homepageImg"
											alt="">
									</el-popover>
								</div>
								<div class="chat-record-page__order-info">
									<div class="chat-record-page__order-level">{{scope.row.content.myLevel}}</div>
									<div class="chat-record-page__order-games">
										<span v-for="(item,index) in scope.row.content.gameName" :key="index">{{item}}
										</span>
									</div>
									<div class="chat-record-page__order-price">¥{{scope.row.content.payMoney}}</div>
								</div>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="150">
				</el-table-column>
				<el-table-column prop="createTime" label="操作" width="100" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('chatRecord:delete')"
							@click="deleteuser(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="chat-record-page__dialog-pagination">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="userData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 处理服务 -->
		<el-dialog title="处理服务" :visible.sync="dialogFormVisible1" center>
			<div class="chat-record-page__form-row">
				<span class="chat-record-page__form-label">服务状态：</span>
				<el-radio-group v-model="state">
					<el-radio :label="2">下架</el-radio>
					<el-radio :label="3">不下架</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="complainTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				userId: '',
				content: '',
				nickName: '',
				form: {
					id: '',
					content: '',
					picture: '',
					state: ''
				},
				state: '',
				imgs: [],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: {},
				userData: {},
				chatConversationId: '',
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val;
				this.neirongSelect(this.chatConversationId)
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.neirongSelect(this.chatConversationId)
			},
			// 用户详情跳转
			updates(userid) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userid
					}
				})
			},
			//删除服务投诉
			deleteuserHh(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('chat/deleteChatConversation'),
						method: 'post',
						params: this.$http.adornParams({
						 							'chatConversationId':row.chatConversationId
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
						 									this.dataSelect()
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
				}).catch(() => {})
			},
			// 查询
			select() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.content = ''
				this.dataSelect()
			},
			// 聊天内容
			complainDetails(row) {
				this.chatConversationId = row.chatConversationId
				this.neirongSelect(this.chatConversationId)
				this.dialogFormVisible = true
			},
			select1() {
				this.page = 1
				this.neirongSelect(this.chatConversationId)
			},
			// 重置
			cleans1() {
				this.content = ''
				this.page = 1
				this.neirongSelect(this.chatConversationId)
			},
			// 处理
			amendNoticeTo() {
				this.dialogFormVisible1 = true
			},
			// 处理投诉
			complainTo() {
				this.$http({
					url: this.$http.adornUrl(`/helpComplaint/solveHelpComplaint/${this.form.id}/${this.state}`),
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
								this.dialogFormVisible = false
								this.dialogFormVisible1 = false
								this.dataSelect()
							}
						})
					}
				})
			},
			// 获取聊天会话列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'userId': this.userId
						'nickName': this.nickName
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.data.list.length > 0) {
						for (var i in data.data.list) {
							if (data.data.list[i].messageType == 5 || data.data.list[i].messageType == 6) {
								data.data.list[i].content = JSON.parse(data.data.list[i].content)
								console.log('data.data.list[i].content', data.data.list[i].content)
							}
						}
					}
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 获取聊天内容列表
			neirongSelect(chatConversationId) {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('chat/selectChatContent'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'chatConversationId': this.chatConversationId,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.data.list.length > 0) {
						for (var i in data.data.list) {
							if (data.data.list[i].messageType == 5 || data.data.list[i].messageType == 6) {
								data.data.list[i].content = JSON.parse(data.data.list[i].content)
							}
						}
					}
					let returnData = data.data;
					this.userData = returnData
				})
			},
			//删除聊天
			deleteuser(row) {
				// let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('chat/deleteChatContentById?chatContentId=' + row
							.chatContentId),
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
									this.neirongSelect(this.chatConversationId)
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
				})
			},
			orderBtn(ordersNo) {
				this.dialogFormVisible = false
				this.$router.push({
					path: '/orderCenter',
					query: {
						ordersNo: ordersNo
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style lang="scss" scoped>
.chat-record-page {
	padding: 0;
	overflow-x: hidden;
}

.chat-record-page__hero {
	margin-bottom: 12px;
}

.chat-record-page__filter {
	margin-bottom: 12px;
}

.chat-record-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.chat-record-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.chat-record-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.chat-record-page__input {
	width: 200px;
}

.chat-record-page__link {
	color: #01af74;
	cursor: pointer;
}

.chat-record-page__location-icon {
	width: 30px;
	cursor: pointer;
	color: #01af74;
}

.chat-record-page__thumb {
	width: 50px;
	height: 50px;
}

.chat-record-page__preview {
	width: 200px;
	height: 200px;
}

.chat-record-page__avatar-gap {
	margin-top: 5px;
}

.chat-record-page__order-card {
	margin-top: 5px;
	overflow: hidden;
}

.chat-record-page__order-img {
	float: left;
	margin: 5px;
}

.chat-record-page__order-info {
	margin-left: 60px;
}

.chat-record-page__order-level {
	color: #1a2433;
	font-weight: 600;
}

.chat-record-page__order-games {
	font-size: 12px;
	line-height: 12px;
}

.chat-record-page__order-price {
	color: #e53935;
}

.chat-record-page__action-btn {
	margin: 5px;
}

.chat-record-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.chat-record-page__table-card >>> .el-table {
	border-radius: 0;
}

.chat-record-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.chat-record-page__table >>> .el-table__body td {
	background: #fff;
}

.chat-record-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.chat-record-page__dialog-filter {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
	margin-bottom: 12px;
}

.chat-record-page__dialog-pagination {
	display: flex;
	justify-content: center;
	margin-top: 12px;
}

.chat-record-page__dialog-table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.chat-record-page__form-row {
	margin-bottom: 10px;
}

.chat-record-page__form-label {
	width: 200px;
	display: inline-block;
	text-align: right;
}
</style>
