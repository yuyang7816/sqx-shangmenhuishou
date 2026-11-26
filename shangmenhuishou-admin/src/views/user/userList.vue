<template>
	<div class="user-list-page">
		<div class="nf-page-hero user-list-page__hero">
			<span class="nf-page-hero__badge">用户管理</span>
			<h2 class="nf-page-hero__title">用户列表</h2>
			<p class="nf-page-hero__desc">查看与管理平台用户信息，支持按昵称、手机号与邀请码筛选，可查看详情、切换状态与删除用户</p>
		</div>

		<div class="nf-filter-card user-list-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body user-list-page__filter-body">
				<div class="user-list-page__field">
					<span class="user-list-page__label">昵称：</span>
					<el-input class="user-list-page__input" @keydown.enter.native="select" clearable placeholder="请输入昵称"
						v-model="campus"></el-input>
				</div>
				<div class="user-list-page__field">
					<span class="user-list-page__label">手机号：</span>
					<el-input class="user-list-page__input" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>
				<div class="user-list-page__field">
					<span class="user-list-page__label">邀请码：</span>
					<el-input class="user-list-page__input" @keydown.enter.native="select" clearable placeholder="请输入邀请码"
						v-model="invitationCode"></el-input>
				</div>
				<div class="user-list-page__field">
					<span class="user-list-page__label">邀请人邀请码：</span>
					<el-input class="user-list-page__input" @keydown.enter.native="select" clearable placeholder="请输入邀请人邀请码"
						v-model="inviterCode"></el-input>
				</div>
				<div class="user-list-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<div class="user-list-page__tabs-wrap">
			<el-tabs v-model="activeName" class="user-list-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="全部用户" name="first">
					<div class="nf-table-card user-list-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records" class="user-list-page__table">
					<el-table-column prop="userId" label="编号" width="80" fixed="left"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="user-list-page__link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span class="user-list-page__link"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="userType" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.userType==2">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column> -->
					<!-- <el-table-column prop="rate" label="商家佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:update')"
								style="color: #01af74;background: #fff;border: none;" @click="xiugai(scope.row,'rate')">
								修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:update')"
								style="color: #01af74;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:update')"
								style="color: #01af74;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column> -->
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<!-- <el-button size="mini" type="warning" @click="zengsongVip(scope.row)" :disabled="!isAuth('userList:updateVip')" >修改会员</el-button> -->
							<!-- <el-button size="mini" type="warning" @click="updateVip(scope.row)" >赠送会员</el-button> -->
							<!-- <el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" :disabled="!isAuth('userList:updateVip')" v-if="scope.row.member==1" plain>取消会员</el-button> -->
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
						</template>
					</el-table-column>
						</el-table>
						<div class="user-list-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40, 50]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="普通用户" name="second">
					<div class="nf-table-card user-list-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records" class="user-list-page__table">
					<el-table-column prop="userId" label="编号" width="80" fixed="left"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="user-list-page__link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="phone" label="用户类型">
						<template slot-scope="scope">
							<span v-if=" scope.row.isAuthentication==1">个人认证</span>
							<span v-else-if=" scope.row.isAuthentication==2">企业认证</span>
							<span v-else-if=" scope.row.isAuthentication==3">个人企业认证</span>
							<span v-else>普通用户</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span class="user-list-page__link"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="userType" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.userType==2">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="商家佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #01af74;background: #fff;border: none;" @click="xiugai(scope.row,'rate')">
								修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #01af74;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #01af74;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
						</template>
					</el-table-column>
						</el-table>
						<div class="user-list-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40, 50]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="回收员用户" name="third">
					<div class="nf-table-card user-list-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records" class="user-list-page__table">
					<el-table-column prop="userId" label="编号" width="80" fixed="left"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="isAuthentication" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="user-list-page__link" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="phone" label="用户类型">
						<template slot-scope="scope">
							<span v-if=" scope.row.isAuthentication==1">个人认证</span>
							<span v-else-if=" scope.row.isAuthentication==2">企业认证</span>
							<span v-else-if=" scope.row.isAuthentication==3">个人企业认证</span>
							<span v-else>普通用户</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span class="user-list-page__link"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="userType" label="是否实名">
						<template slot-scope="scope">
							<span v-if="scope.row.userType==2">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="商家佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #01af74;background: #fff;border: none;" @click="xiugai(scope.row,'rate')">
								修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #01af74;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #01af74;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
						</template>
					</el-table-column>
						</el-table>
						<div class="user-list-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40, 50]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 修改比例 -->
			<el-dialog title="修改佣金比例" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">佣金比例：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						placeholder="请输入佣金比例"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改比例 -->
			<el-dialog title="修改会员" :visible.sync="dialogFormVisibleZs" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改天数：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						placeholder="请输入修改天数"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleZs = false">取 消</el-button>
					<el-button type="primary" @click="updateVip()">确 定</el-button>
				</div>
			</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 2,
				state: 'false',
				limit: 10,
				page: 1,
				phone: '',
				nickNam: '',
				platform: 'all',
				isRelation: -1,
				isTuan: -1,
				value: '',
				member: -1,
				payData: [],
				nickName: '',
				grade: '',
				name: '',
				activeName: 'first',
				tableDataLoading: true,
				tableData: [],
				platforms: [{
						value: 'all',
						label: '全部'
					}, {
						value: 'wap',
						label: '网站'
					}, {
						value: 'app',
						label: 'APP'
					}, {
						value: 'weixin',
						label: '微信公众号'
					},
					{
						value: 'mp',
						label: '微信小程序'
					}
				],
				campus: '',
				homeData1: [],
				isAuthentication: 0,
				dialogFormVisible2: false,
				proportion: '',
				texts: '',
				userIdss: '',
				isCompany: '',
				shopType: '',
				shopTypeList: [{
					value: '',
					label: '全部'
				}, {
					value: 1,
					label: '个人认证'
				}, {
					value: 2,
					label: '企业认证'
				}, ],
				companyName: '',
				invitationCode: '',
				inviterCode: '',
				dialogFormVisibleZs: false,
				isVip: '',
				type: 1,
			}
		},
		methods: {
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl('admin/base/user/update'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'status': val
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
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 详情跳转
			updates2(row) {
				// this.$router.push({
				// 	path: '/userDetail',
				// 	query: {
				// 		userId: row.superior
				// 	}
				// })
				if (row.inviterCode) {
					this.$http({
						url: this.$http.adornUrl('user/selectUserByInvitationCode'),
						method: 'get',
						params: this.$http.adornParams({
							'invitationCode': row.inviterCode
						})
					}).then(({
						data
					}) => {
						if (data.code == 0 && data.data) {
							this.$router.push({
								path: '/userDetail',
								query: {
									userId: data.data.userId
								}
							})
						} else if (data.code == 0 && data.data == null) {
							this.$message({
								message: '当前没有这个用户！',
								type: 'warning',
								duration: 1500,
								onClose: () => {}
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
				} else {
					this.$message({
						message: '未绑定邀请人',
						type: 'warning',
						duration: 1500,
						onClose: () => {}
					})
				}
			},
			handleClick(tab, event) {
				this.page = 1
				this.campus = ''
				this.phone = ''
				this.companyName = ''
				if (tab._props.label == '全部用户') {
					// this.isAuthentication = 0
					// this.isCompany = ''
					// this.isVip = ''
					this.shopType = ''
					this.dataSelect()
				}
				if (tab._props.label == '普通用户') {
					// this.isAuthentication = 1
					// this.isCompany = 2
					// this.isVip = 2
					this.shopType = 1
					this.dataSelect()
				}
				if (tab._props.label == '回收员用户') {
					// this.isAuthentication = 2
					this.shopType = 2
					// this.isCompany = ''
					// this.isVip = ''
					this.dataSelect()
				}
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.paySelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.paySelect()
			},
			// select自动选择
			selectTrigger() {
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			select2() {
				this.paySelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.campus = ''
				this.phone = ''
				// this.shopType = ''
				this.companyName = ''
				this.invitationCode = ''
				this.inviterCode = ''
				this.dataSelect()
			},
			//删除用户
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/base/user/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'userId': delid,
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
				})
			},
			// 封号
			updates1(row) {
			},
			// 获取社区数据列表
			homeSelect() {
				this.$http({
					url: this.$http.adornUrl('helpCampus/selectCampusList'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page':this.page,
						// 'limit':this.limit,
						// 'content':this.content
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.homeData1 = returnData
				})
			},
			// 筛选信息
			animeDat2(state) {
				this.dataSelect()
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/base/user/list'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						// 'isCompany': this.isCompany,
						'phone': this.phone,
						'username': this.campus,
						// 'isAuthentication': this.isAuthentication,
						'userType': this.shopType,
						// 'companyName': this.companyName,
						// 'invitationCode':this.invitationCode,
						// 'inviterCode':this.inviterCode,
						// 'isVip':this.isVip
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			xiugai(row, text) {
				if (text == 'rate') {
					this.proportion = row.rate
				}
				if (text == 'zhiRate') {
					this.proportion = row.zhiRate
				}
				if (text == 'feiRate') {
					this.proportion = row.feiRate
				}
				this.texts = text
				this.userIdss = row.userId
				this.dialogFormVisible2 = true
			},
			StairNoticeTo2() {
				var datas = {}
				if (this.texts == 'rate') {
					datas.rate = this.proportion
				}
				if (this.texts == 'zhiRate') {
					datas.zhiRate = this.proportion
				}
				if (this.texts == 'feiRate') {
					datas.feiRate = this.proportion
				}
				datas.userId = this.userIdss
				this.$http({
					url: this.$http.adornUrl('admin/base/user/update'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					console.log('data', data)
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible2 = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			// 增送会员
			updateVip(row) {

				this.$http({
					url: this.$http.adornUrl('user/giveUserVip'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': row.userId,
						'day': 30
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
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
			// 取消会员
			quxiaoVip(row) {
				this.$confirm(`确定要取消会员?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/removeUserVip'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'userId': row.userId
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
				})
			},
			// 修改会员弹框
			zengsongVip(row) {
				this.type = 1
				this.proportion = ''
				this.userIdss = row.userId
				this.dialogFormVisibleZs = true
			},
			// 设置会员
			updateVip(row) {
				if (this.proportion === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入天数',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('user/giveUserVip'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': this.userIdss,
						'day': this.proportion,
						'type': this.type
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
								this.dialogFormVisibleZs = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
		},
		mounted() {
			// this.homeSelect()
			this.dataSelect()
		}
	}
</script>

<style lang="scss" scoped>
.user-list-page {
	padding: 0;
	overflow-x: hidden;
}

.user-list-page__hero {
	margin-bottom: 12px;
}

.user-list-page__filter {
	margin-bottom: 12px;
}

.user-list-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.user-list-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.user-list-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.user-list-page__input {
	width: 150px;
}

.user-list-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.user-list-page__tabs-wrap {
	margin-bottom: 0;
}

.user-list-page__tabs {
	margin-bottom: 0;
}

.user-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.user-list-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.user-list-page__table-card >>> .el-table {
	border-radius: 0;
}

.user-list-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.user-list-page__table >>> .el-table__body td {
	background: #fff;
}

.user-list-page__link {
	color: #01af74;
	cursor: pointer;
}

.user-list-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.user-list-page__table >>> .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>