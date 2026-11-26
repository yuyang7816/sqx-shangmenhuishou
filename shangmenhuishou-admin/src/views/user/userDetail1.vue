<template>
	<div class="user-detail-page">
		<div class="user-detail-page__nav">
			<a href="#" class="user-detail-page__back-link" @click.prevent="prev">
				<icon-svg name="jiantou" class="user-detail-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="user-detail-page__nav-sep">|</span>
			<span class="user-detail-page__nav-title">用户详情</span>
		</div>

		<div class="nf-page-hero user-detail-page__hero">
			<span class="nf-page-hero__badge">用户管理</span>
			<h2 class="nf-page-hero__title">用户详情</h2>
			<p class="nf-page-hero__desc">查看用户基本信息、钱包流水、订单记录与地址信息，支持修改余额、状态及资料</p>
		</div>

		<div class="user-detail-page__kpi-row">
			<div class="user-detail-page__kpi">
				<div class="user-detail-page__kpi-icon"><i class="el-icon-s-order"></i></div>
				<div class="user-detail-page__kpi-body">
					<div class="user-detail-page__kpi-label">本月接单数量</div>
					<div class="user-detail-page__kpi-value">
						<span>{{ tablenum.takeOrdersCount ? tablenum.takeOrdersCount : 0 }}</span>笔
					</div>
				</div>
			</div>
			<div class="user-detail-page__kpi">
				<div class="user-detail-page__kpi-icon"><i class="el-icon-money"></i></div>
				<div class="user-detail-page__kpi-body">
					<div class="user-detail-page__kpi-label">本月充值金额</div>
					<div class="user-detail-page__kpi-value">
						<span>{{ tablemoney.consume ? tablemoney.consume : 0 }}</span>元
					</div>
				</div>
			</div>
			<div class="user-detail-page__kpi">
				<div class="user-detail-page__kpi-icon"><i class="el-icon-document"></i></div>
				<div class="user-detail-page__kpi-body">
					<div class="user-detail-page__kpi-label">本月订单数量</div>
					<div class="user-detail-page__kpi-value">
						<span>{{ tablemoney.myOrdersCount ? tablemoney.myOrdersCount : 0 }}</span>笔
					</div>
				</div>
			</div>
			<div class="user-detail-page__kpi">
				<div class="user-detail-page__kpi-icon"><i class="el-icon-wallet"></i></div>
				<div class="user-detail-page__kpi-body">
					<div class="user-detail-page__kpi-label">本月提现金额</div>
					<div class="user-detail-page__kpi-value">
						<span>{{ tablemoney.income ? tablemoney.income : 0 }}</span>元
					</div>
				</div>
			</div>
		</div>

		<div class="user-detail-page__tabs-wrap">
			<el-tabs v-model="activeName" class="user-detail-page__tabs" @tab-click="handleClick">
			<el-tab-pane label="用户详情" name="first">
				<div class="nf-table-card user-detail-page__detail-card">
				<div class="detailtabel">
					<div class="table_main">
						<table>
							<tbody>
								<tr>
									<th>本月接单数量</th>
									<td>{{ tablenum.takeOrdersCount ? `${tablenum.takeOrdersCount}笔` : '0笔' }}</td>
									<th>本月充值金额</th>
									<td>{{ tablemoney.consume ? `${tablemoney.consume}元` : '0元' }}</td>
									<th>本月邀请数量</th>
									<td class="border-rt">{{ tablemoney.count ? `${tablemoney.count}人` : '0人' }}
									</td>
								</tr>
								<tr>
									<th>本月订单数量</th>
									<td>{{ tablemoney.myOrdersCount ? `${tablemoney.myOrdersCount}笔` : '0笔' }}</td>
									<th>本月提现金额</th>
									<td>{{ tablemoney.income ? `${tablemoney.income}元` : '0元' }}</td>
									<th>openId</th>
									<td class="border-rt">{{ tableData.openId ? tableData.openId : '未绑定' }}</td>
								</tr>
								<!-- <tr>
									<th>商家佣金比例</th>
									<td>{{ tableData.rate ? `${tableData.rate}` : '0' }}</td>
									<th>一级佣金比例</th>
									<td>{{ tableData.zhiRate ? `${tableData.zhiRate}` : '0' }}</td>
									<th>二级佣金比例</th>
									<td class="border-rt">{{ tableData.feiRate ? tableData.feiRate : '0' }}</td>
								</tr> -->
								<tr>
									<th>邀请人邀请码</th>
									<td>{{ tableData.inviterCode }}
										<span style="color: #01af74;cursor:pointer;"
											@click="userChange(tableData,10)">修改</span>
									</td>
									<th>余额</th>
									<td style="color: rgb(245, 108, 108)">
										{{ tablemoney.balance ? `${tablemoney.balance}元` : '0元' }}
										<el-button size="mini" :disabled="!isAuth('userList:updateJe')"
											style="color: #01af74;background: #fff;border: none;"
											@click="rechargenone(tableData.balance,1)">修改金额</el-button>
									</td>
									<th>保证金</th>
									<td class="border-rt">
										{{ tablemoney.deposit ? `${tablemoney.deposit}元` : '0元' }}
										<el-button size="mini" :disabled="!isAuth('userList:updateJe')"
											style="color: #01af74;background: #fff;border: none;"
											@click="rechargenoneB(tablemoney.deposit,2)">修改金额</el-button>
									</td>
								</tr>
								<tr>
									<th>微信名称</th>
									<td>
										{{ tableData.userName }}
										<span style="color: #01af74;cursor:pointer;"
											@click="userChange(tableData,1)">修改</span>
									</td>
									<th>手机号</th>
									<td>{{ tableData.phone ? tableData.phone : '未绑定' }}
										<span style="color: #01af74;cursor:pointer;"
											@click="userChange(tableData,4)">修改</span>
									</td>
									<th>图像</th>
									<td class="border-rt">
										<img v-if="tableData.avatar==null" src="~@/assets/img/avatar.png" alt=""
											width="80" height="80">
										<img v-else :src="tableData.avatar" width="80" height="80" />
										<span style="color: #01af74;cursor:pointer;"
											@click="userChange(tableData,2)">修改</span>
									</td>
								</tr>
								<tr>
									<th>微信收款码</th>
									<td>
										<div v-if="tableData.withdrawalAccounts&&tableData.withdrawalAccounts.length>0">
											<div v-for="(item,index) in tableData.withdrawalAccounts" :key="index">
												<div v-if="item.type==2">
													<el-popover placement="top-start" title="" trigger="hover">
														<img style="width: 50px; height: 50px" :src="item.accountNumber"
															alt="" slot="reference">
														<img style="width: 300px; height: 300px"
															:src="item.accountNumber" alt="">
													</el-popover>
												</div>
											</div>
										</div>
									</td>
									<th>支付宝姓名</th>
									<td>
										<div v-if="tableData.withdrawalAccounts&&tableData.withdrawalAccounts.length>0">
											<div v-for="(item,index) in tableData.withdrawalAccounts" :key="index">
												<div v-if="item.type==1">
													{{ item.accountName ? item.accountName : '未绑定' }}
												</div>
											</div>
										</div>
										<div v-else>-</div>
									</td>
									<th>支付宝账号</th>
									<td class="border-rt">
										<div v-if="tableData.withdrawalAccounts&&tableData.withdrawalAccounts.length>0">
											<div v-for="(item,index) in tableData.withdrawalAccounts" :key="index">
												<div v-if="item.type==1">
													{{ item.accountNumber ? item.accountNumber : '未绑定' }}
												</div>
											</div>
										</div>
										<div v-else>-</div>
									</td>
								</tr>
								<tr>
									<th>邀请码</th>
									<td>{{ tableData.invitationCode }}
										<span style="color: #01af74;cursor:pointer;"
											@click="userChange(tableData,11)">修改</span>
									</td>
									<th>创建时间</th>
									<td>{{ tableData.createTime }}</td>
									<th>用户状态</th>
									<td class=" border-rt">
										<span v-if="tableData.status == 1 ">正常</span>
										<span v-if="tableData.status == 2 " style="color: #f56c6c;">禁用</span>
										<el-button size="mini" :disabled="!isAuth('userList:update')"
											style="color: #01af74;background: #fff;border: none;"
											@click="stateChange(tableData.userId,tableData.status)">更改状态</el-button>
									</td>
								</tr>
								<tr>
									<th>性别</th>
									<td>
										<span v-if="tableData.sex==0">未设置</span>
										<span v-if="tableData.sex==1">男</span>
										<span v-if="tableData.sex==2">女</span>
										<el-button size="mini" style="color: #01af74;background: #fff;border: none;"
											@click="sexChange(tableData.userId,tableData.sex)">修改</el-button>
									</td>
									<th>年龄</th>
									<td>
										<span v-if="tableData.age">{{tableData.age}}</span>
										<span v-else>未设置</span>
										<el-button size="mini" style="color: #01af74;background: #fff;border: none;"
											@click="ageChange(tableData.userId,tableData.age)">修改</el-button>
									</td>
									<th>认证类型</th>
									<td class=" border-rt">
										<span v-if="tableData.isAuthentication">已实名</span>
										<span v-else>未实名,普通用户</span>
									</td>
								</tr>
								<tr>
									<th class="border-bt">编号</th>
									<td class="border-bt">
										{{ tableData.userId }}
									</td>
									<th class="border-bt"></th>
									<td class="border-bt">
									</td>
									<th class="border-bt"></th>
									<td class="border-bt border-rt">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="提现记录" name="sixth">
				<div class="nf-table-card user-detail-page__table-card">
				<el-table v-loading="tableDataLoading" :data="withdrawData.records" class="user-detail-page__table">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="withdrawalAccount.accountNumber" label="支付宝账号" width="150">
						<template slot-scope="scope">
							<span v-if="scope.row.withdrawalAccount&&scope.row.withdrawalAccount.type==1">{{scope.row.withdrawalAccount.accountNumber}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="withdrawalAccount.accountName" label="支付宝名称" width="100">
					</el-table-column>
					<el-table-column prop="cardFront" label="收款二维码" width="150">
						<template slot-scope="scope">
								<span v-if="scope.row.withdrawalAccount&&scope.row.withdrawalAccount.type==2">
									<div v-if="scope.row.withdrawalAccount.accountNumber" style="display:flex;flex-wrap: wrap;">
										<el-popover placement="top-start" title="" trigger="hover">
											<img style="width: 50px; height: 50px" :src="scope.row.withdrawalAccount.accountNumber" alt="" slot="reference">
											<img style="width: 200px; height: 200px" :src="scope.row.withdrawalAccount.accountNumber" alt="">
										</el-popover>
									</div>
								</span>
						</template>
					</el-table-column>
					<el-table-column prop="userEntity.phone" label="手机号" width="150">
					</el-table-column>
					<el-table-column prop="accountType" label="提现方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.accountType == 1">微信</span>
							<span v-else-if="scope.row.accountType == 2">支付宝</span>
							<span v-else>暂无</span>
						</template>
					</el-table-column>
					<el-table-column prop="amount" label="提现金额" width="100">
					</el-table-column>
					<el-table-column prop="actualAmount" label="实际到账金额" width="100">
					</el-table-column>
					<el-table-column prop="serviceFee" label="手续费" width="100">
					</el-table-column>
					<el-table-column prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.status == 1">待转账</span>
							<span v-if="scope.row.status == 2">已转账</span>
							<span v-if="scope.row.status == 3">已拒绝</span>
						</template>
					</el-table-column>
					<el-table-column prop="reason" label="拒绝原因" width="220">
					</el-table-column>
					<el-table-column prop="createTime" label="申请时间" width="170">
					</el-table-column>
					<el-table-column prop="withdrawTime" label="转账/拒绝时间" width="180">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="withdrawData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="钱包明细" name="eighth">
				<div class="nf-table-card user-detail-page__table-card">
				<el-table v-loading="tableDataLoading" :data="walletData.records" class="user-detail-page__table">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="transactionType" label="类型" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.transactionType==1">充值</span>
							<span v-if="scope.row.transactionType==2">消费</span>
							<span v-if="scope.row.transactionType==3">退款</span>
							<span v-if="scope.row.transactionType==5">系统调整</span>
							<span v-if="scope.row.transactionType==6">退保证金</span>
						</template>
					</el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="description" label="内容"></el-table-column>
					<el-table-column prop="amount" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.direction==2">- {{ scope.row.amount }}</span>
							<span style="color: #006600;" v-if="scope.row.direction==1">+ {{ scope.row.amount }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="walletData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="充值明细" name="seventh">
				<!-- <div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="timeDate2">
						查询</el-button>
				</div> -->
				<div class="nf-table-card user-detail-page__table-card">
				<el-table v-loading="tableDataLoading" :data="rechargeData.records" class="user-detail-page__table">
					<el-table-column prop="id" label="编号" width="50"></el-table-column>
					<el-table-column prop="orderNo" label="充值订单号" width="200"></el-table-column>
					<el-table-column prop="amount" label="充值金额"></el-table-column>
					<!-- <el-table-column prop="userId" label="会员编号"></el-table-column> -->
					<el-table-column label="支付渠道">
						<template slot-scope="scope">
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.paymentChannel == 'wx'">微信</span>
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.paymentChannel == 'ali'">支付宝</span>
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.paymentChannel == 'paypal'">PayPal</span>
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.paymentChannel == 'stripe'">Stripe</span>
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.paymentChannel == 'balance'">余额支付</span>
						</template>
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.status == 0">待支付</span>
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.status == 1">支付成功</span>
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.status == 2">已关闭</span>
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.status == 3">支付失败</span>
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.status == 4">退款中</span>
							<span style="color: #01af74;cursor: pointer;" v-if="scope.row.status == 5">已退款</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="170">
					</el-table-column>
					<el-table-column prop="payTime" label="支付时间" width="170">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="rechargeData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="保证金明细" name="baozhengjin">
				<div class="nf-table-card user-detail-page__table-card">
				<el-table v-loading="tableDataLoading" :data="walletData.records" class="user-detail-page__table">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="amount" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.direction==2">- {{ scope.row.amount }}</span>
							<span style="color: #006600;" v-if="scope.row.direction==1">+ {{ scope.row.amount }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChangeBzj" @current-change="handleCurrentChangeBzj"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="walletData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的订单" name="dingdan">
				<div class="nf-table-card user-detail-page__table-card">
				<el-table v-loading="tableDataLoading" :data="dingdanData.records" class="user-detail-page__table">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="riderName" label="回收员">
						<template slot-scope="scope"> 
							<span v-if="scope.row.state==0"> - </span>
							<span v-else style="color: #009900;cursor: pointer;"  @click="updates(scope.row.wasteUserId)">{{ scope.row.wasteUserName }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="orderNo" label="订单编号" width="180">
					</el-table-column>
					<el-table-column prop="addressName" label="预约用户姓名" >
					</el-table-column>
					<el-table-column prop="addressPhone" label="预约用户电话" width="150">
					</el-table-column>
					<el-table-column prop="province" label="预约用户地址" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.address}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="note" label="订单备注" width="150">
					</el-table-column>
					<el-table-column prop="appointmentTime" label="预约时间" width="160">
					</el-table-column>
					<el-table-column prop="wasteTime" label="接单时间" width="160"></el-table-column>
					<el-table-column prop="distance" label="距离"></el-table-column>
					<el-table-column prop="orderItems" label="预约物品列表" width="300">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.orderItems&&scope.row.orderItems.length>0">
								<div v-for="(item,index) in scope.row.orderItems" :key="index">
									{{index+1}}.{{item.categoryName}}/{{item.categoryChildrenName}}:{{item.price}}￥/{{item.unit}}
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="订单状态">
						<template slot-scope="scope"> 
							<span style="color: #f56c6c;" v-if="scope.row.status==0">待接单</span>
							<span style="color: #E87D0D;" v-if="scope.row.status==1">待服务</span>
							<span style="color: #009900;" v-if="scope.row.status==2">进行中</span>
							<span style="color: #999;" v-if="scope.row.status==3">已完成</span>
							<span style="color: #999;" v-if="scope.row.status==4">已取消</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange10" @current-change="handleCurrentChange10"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="dingdanData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="我的发布" name="fabu">
				<el-table v-loading="tableDataLoading" :data="takeData.list">
					<el-table-column prop="id" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="isRecommend" label="是否推荐" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.isRecommend==0">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="homepageImg" label="封面图">
						<template slot-scope="scope">
							　　<img v-if="scope.row.homepageImg &&scope.row.homepageImg!=''" :src="scope.row.homepageImg"
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="homepageImg" label="详情图" width="180">
						<template slot-scope="scope">
							　　<img v-for="(item,index) in scope.row.detailsImg" :key="index" :src="item" width="40"
								height="40" style="margin:3px" />
						</template>
					</el-table-column>
					<el-table-column prop="myLevel" label="标题" width="150">
					</el-table-column>
					<el-table-column prop="gameName" label="服务名称" width="150">
					</el-table-column>
					<el-table-column prop="oldMoney" label="发布价格" width="100">
					</el-table-column>
					<el-table-column prop="money" label="普通用户价格" width="100">
					</el-table-column>
					<el-table-column prop="memberMoney" label="会员价格" width="100">
					</el-table-column>
					<el-table-column prop="minNum" label="最低服务时间" width="100">
					</el-table-column>
					<el-table-column prop="unit" label="单位" width="100">
					</el-table-column>
					<el-table-column prop="salesNum" label="销量" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.salesNum">{{scope.row.salesNum}}</span>
							<span v-else>0</span>
						</template>
					</el-table-column>
					<el-table-column prop="city" label="城市" width="100">
					</el-table-column>
					<el-table-column prop="money" label="服务地区" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.region?scope.row.region:'不限地区'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="count" label="接单人数" width="140">
					</el-table-column>
					<el-table-column prop="orderScore" label="评分" width="100">
					</el-table-column>
					<el-table-column prop="detailadd" label="详细地址" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.detailadd?scope.row.detailadd:'暂无地址'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="longitude" label="经度" width="140">
					</el-table-column>
					<el-table-column prop="latitude" label="纬度" width="140">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color: #01af74;" v-if="scope.row.status === 0 ">进行中</span>
							<span style="color: red;" v-if="scope.row.status === 1 ">待审核</span>
							<span style="color: #999;" v-if="scope.row.status === 2 ">已取消</span>
							<span style="color: #999;" v-if="scope.row.status === 3 ">拒绝</span>
						</template>
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120" fixed='right' align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" v-if="scope.row.status != 1 "
								@click="plCompile(scope.row)">评论
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange5" @current-change="handleCurrentChange5"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="takeData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="我的接单" name="jiedan">
				<div class="nf-table-card user-detail-page__table-card">
				<el-table v-loading="tableDataLoading" :data="jiedanData.records" class="user-detail-page__table">
					<el-table-column prop="id" label="编号" width="80"></el-table-column>
					<el-table-column prop="orderNo" label="订单编号"></el-table-column>
					<el-table-column prop="ordersNo" label="发布人">
						<template slot-scope="scope">
							<span style="color: #f56c6c;cursor:pointer"
								@click="updates1(scope.row.userId)">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="ordersNo" label="发布人头像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="addressName" label="预约用户姓名" >
					</el-table-column>
					<el-table-column prop="addressPhone" label="预约用户电话" width="150">
					</el-table-column>
					<el-table-column prop="province" label="预约用户地址" width="200">
						<template slot-scope="scope">
							<span>{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.address}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="note" label="订单备注" width="150">
					</el-table-column>
					<el-table-column prop="appointmentTime" label="预约时间" width="160">
					</el-table-column>
					<el-table-column prop="wasteTime" label="接单时间" width="160"></el-table-column>
					<el-table-column prop="distance" label="距离"></el-table-column>
					<el-table-column prop="orderItems" label="预约物品列表" width="300">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.orderItems&&scope.row.orderItems.length>0">
								<div v-for="(item,index) in scope.row.orderItems" :key="index">
									{{index+1}}.{{item.categoryName}}/{{item.categoryChildrenName}}:{{item.price}}￥/{{item.unit}}
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="订单状态">
						<template slot-scope="scope"> 
							<span style="color: #f56c6c;" v-if="scope.row.status==0">待接单</span>
							<span style="color: #E87D0D;" v-if="scope.row.status==1">待服务</span>
							<span style="color: #009900;" v-if="scope.row.status==2">进行中</span>
							<span style="color: #999;" v-if="scope.row.status==3">已完成</span>
							<span style="color: #999;" v-if="scope.row.status==4">已取消</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange6" @current-change="handleCurrentChange6"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="jiedanData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的地址" name="dizhi">
				<div class="nf-table-card user-detail-page__table-card">
				<el-table v-loading="tableDataLoading" :data="dizhiData.records" class="user-detail-page__table">
					<el-table-column prop="id" label="编号" width="80"></el-table-column>
					<el-table-column prop="name" label="姓名" width="150"></el-table-column>
					<el-table-column prop="phone" label="电话" width="160"></el-table-column>
					<el-table-column prop="province" label="地址">
						<template slot-scope="scope">
							<span>{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.address}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column fixed="right" prop="isDefault" label="是否默认" width="100">
						<template slot-scope="scope">
							<span style="color: red;" v-if="scope.row.isDefault === 1 ">是</span>
							<span style="color: #01af74;" v-else>否</span>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail-page__pagination">
					<el-pagination @size-change="handleSizeChange11" @current-change="handleCurrentChange11"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="dizhiData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			
			<!-- 我的投诉 -->
			<!-- <el-tab-pane label="我的投诉" name="tousu">
				<el-table v-loading="tableDataLoading" :data="tousuData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="被投诉用户">
						<template slot-scope="scope">
							<div style="color: #0066CC;cursor: pointer;" @click="updates1(scope.row.byUserId)">
								{{scope.row.byUserName}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="投诉类型">
						<template slot-scope="scope">
							<div v-if="scope.row.platform==1">聊天投诉</div>
							<div v-if="scope.row.platform==2">用户投诉</div>
							<div v-if="scope.row.platform==3">任务投诉</div>
							<div v-if="scope.row.platform==4">服务投诉</div>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="投诉内容" width="200">
					</el-table-column>
					<el-table-column prop="image" label="图片" width="150">
						<template slot-scope="scope">
							<div v-for="(item,index) in scope.row.image" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<div v-if="scope.row.status==0">待审核</div>
							<div v-else>已审核</div>
						</template>
					</el-table-column>
					<el-table-column prop="status" label="处理结果">
						<template slot-scope="scope">
							<div v-if="scope.row.status==0">待处理</div>
							<div v-if="scope.row.status==1">不处理</div>
							<div v-if="scope.row.status==2">已封号</div>
							<div v-if="scope.row.status==3">已删除</div>
						</template>
					</el-table-column>
					<el-table-column prop="auditContent" label="审核内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange23" @current-change="handleCurrentChange23"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tousuData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			
			<!-- 我的常用语 -->
			<!-- <el-tab-pane label="我的常用语" name="guanjianci">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="add">添加
				</el-button>
				<el-table v-loading="tableDataLoading" :data="guanjianciData.list">
					<el-table-column prop="id" label="编号" width="80"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="createAt" label="操作" width="180">
						<template slot-scope="scope">
							<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
								@click="updateGjc(scope.row)">编辑
							</el-button>
							<el-button style="margin-left:15px;" size="mini" type="danger" icon="document"
								@click="deletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeCyy" @current-change="handleCurrentChangeCyy"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="guanjianciData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 服务订单统计 -->
			<!-- <el-tab-pane label="服务订单统计" name="fuwudindan"
				v-if="tableData.isAuthentication==2||tableData.isAuthentication==3">
				<div style="position: relative;display: inline-block;margin: 3px;">
					<span>员工昵称：</span>
					<el-input style="width: 200px;" @keydown.enter.native="selectF" placeholder="请输入员工昵称"
						v-model="userNameF"></el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;margin: 3px;">
					<span>员工电话：</span>
					<el-input style="width: 200px;" @keydown.enter.native="selectF" placeholder="请输入员工电话"
						v-model="phoneF"></el-input>&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="selectF">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansF">重置
				</el-button>
				<el-table v-loading="tableDataLoading" :data="wududindanData.records">
					<el-table-column prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column prop="userName" label="员工昵称"></el-table-column>
					<el-table-column prop="phone" label="员工电话"></el-table-column>
					<el-table-column prop="picture" label="员工头像">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
								暂无图片
							</div>
							<div v-else>
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="counts" label="服务单量"></el-table-column>
					<el-table-column prop="money" label="金额"></el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeFwdd" @current-change="handleCurrentChangeFwdd"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="wududindanData.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="我的优惠券" name="youhuiquan">
				<el-table v-loading="tableDataLoading" :data="youhuiquanData.records">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称">
					</el-table-column>
					<el-table-column prop="couponPicture" label="图片">
						<template slot-scope="scope">
							<img :src="scope.row.couponPicture" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="money" label="优惠券金额">
					</el-table-column>
					<el-table-column prop="minMoney" label="最低消费">
					</el-table-column>
					<el-table-column prop="createTime" label="领取时间">
					</el-table-column>
					<el-table-column prop="expirationTime" label="过期时间">
					</el-table-column>
					<el-table-column prop="expirationTime" label="状态">
						<template slot-scope="scope">
							<span v-if="scope.row.status==0">待使用</span>
							<span v-else-if="scope.row.status==1">已使用</span>
							<span v-else-if="scope.row.status==2">已失效</span>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeYhq" @current-change="handleCurrentChangeYhq"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="youhuiquanData.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- 不可提现添加金额 -->
			<el-dialog title="修改金额" :visible.sync="dialogFormVisible1" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="'incr'">添加</el-radio>
						<el-radio :label="'decr'">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改年龄 -->
			<el-dialog title="修改年龄" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">年龄：</span>
					<el-input style="width:50%;" v-model="age" type="number" :min="0" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 评论列表 -->
			<el-dialog title="评论列表" :visible.sync="dialogFormVisible10" center>
				<el-table v-loading="tableDataLoading" :data="pinglunData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="评论用户" width="120">
						<template slot-scope="scope">
							　<span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.userId)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像" width="100">
						<template slot-scope="scope">
							　　<img v-if="scope.row.avatar && scope.row.avatar!=''" :src="scope.row.avatar" width="40"
								height="40" />
							<span v-else>暂无图片</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="评论内容">
					</el-table-column>
					<el-table-column prop="createTime" label="评论时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="pinglundelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange13" @current-change="handleCurrentChange13"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit2" :current-page="page2"
						layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 聊天内容 -->
			<el-dialog title="聊天内容" :visible.sync="dialogFormVisible4" center width="80%">
				<el-table v-loading="tableDataLoading1" :data="huihuaData.list">
					<el-table-column prop="chatContentId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称">
						<template slot-scope="scope">
							<!-- <div style="color: #01af74;cursor: pointer;" @click="updates(scope.row)">
								{{scope.row.userName ? scope.row.userName : '未绑定'}}
							</div> -->
							<el-button size="mini" style="color: #01af74;background: #fff;border: none;" type="primary"
								:disabled="!isAuth('userList:viewDetails')" @click="updates(scope.row.userId)">
								{{scope.row.userName ? scope.row.userName : '未绑定'}}
							</el-button>
						</template>
					</el-table-column>
					<el-table-column prop="title" label="用户头像">
						<template slot-scope="scope">
							<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
								暂无图片
							</div>
							<div v-else>
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="信息内容" width="380">
						<template slot-scope="scope">
							<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
							<div v-if="scope.row.messageType==2">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.content" alt="">
								</el-popover>
							</div>
							<div v-if="scope.row.messageType==3">
								<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
							</div>
							<div v-if="scope.row.messageType==4">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px"
										:src="'https://h5.dingjianpp.com/emoji1/'+scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px"
										:src="'https://h5.dingjianpp.com/emoji1/'+scope.row.content" alt="">
								</el-popover>
							</div>
							<div v-if="scope.row.messageType==5">
								<!-- @click="mapBtn(scope.row.content)" -->
								<span><i class="el-icon-location"
										style="width: 30px;cursor: pointer;color: #01af74;"></i>{{scope.row.content.address}}
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeLtnr" @current-change="handleCurrentChangeLtnr"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next" :total="huihuaData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 添加/修改常用语 -->
			<el-dialog :title="title2" :visible.sync="dialogFormVisibleCyy" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">常用语：</span>
					<el-input style="width:50%;" v-model="contents" type="text" placeholder="请输入常用语"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleCyy = false">取 消</el-button>
					<el-button type="primary" @click="addGuanjianci()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改信息 -->
			<el-dialog title="修改信息" :visible.sync="dialogFormVisible3" center>
				<div style="margin-bottom: 10px;" v-if="userType==4">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
					<el-input style="width: 50%;" v-model="phoneU" placeholder="请输入手机号"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">昵称：</span>
					<el-input style="width: 50%;" v-model="nameUser" placeholder="请输入昵称"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
					<div
						style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
						<el-upload class="avatar-uploader" v-model="imageUser" :action="$http.adornUrl('file/upload')"
							:show-file-list="false" :on-success="handleAvatarSuccess1">
							<img v-if="imageUser" :src="imageUser" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==10">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请人邀请码：</span>
					<el-input style="width: 50%;" v-model="invitationU" placeholder="请输入邀请人邀请码"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==11">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请码：</span>
					<el-input style="width: 50%;" v-model="invitationCodeU" placeholder="请输入邀请码"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==3">
					<span style="width: 200px;display: inline-block;text-align: right;">性别：</span>
					<el-radio-group v-model="genderU">
						<el-radio :label="1">男</el-radio>
						<el-radio :label="2">女</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==5">
					<span style="width: 200px;display: inline-block;text-align: right;">年龄：</span>
					<el-input style="width: 50%;" v-model="ageU" placeholder="请输入年龄"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==6">
					<span style="width: 200px;display: inline-block;text-align: right;">微信号：</span>
					<el-input style="width: 50%;" v-model="wxCodeU" placeholder="请输入微信号"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="userNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改认证信息 -->
			<el-dialog title="修改认证信息" :visible.sync="dialogFormVisibleRz" center>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
					<el-input style="width: 50%;" v-model="name" placeholder="请输入昵称"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
					<el-input style="width: 50%;" v-model="phone" placeholder="请输入手机号"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">身份证号：</span>
					<el-input style="width: 50%;" v-model="idNumber" placeholder="请输入身份证号"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">身份证正面照：</span>
					<div
						style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
						<el-upload class="avatar-uploader" v-model="back" :action="$http.adornUrl('file/upload')"
							:show-file-list="false" :on-success="handleAvatarSuccess2">
							<img v-if="back" :src="back" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">身份证背面照：</span>
					<div
						style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
						<el-upload class="avatar-uploader" v-model="front" :action="$http.adornUrl('file/upload')"
							:show-file-list="false" :on-success="handleAvatarSuccess3">
							<img v-if="front" :src="front" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
					<span class="bqList" v-for="(item,index) in label" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="dels1(index)"></i>
						</span>
					</span>
					<el-input v-model="bq" style="width:200px;" placeholder="请输入标签"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
					</el-button>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">年龄：</span>
					<el-input v-model="age" placeholder="请输入年龄" style="width:50%;"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">服务次数：</span>
					<el-input v-model="serviceNum" placeholder="请输入服务次数" style="width:50%;"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">评分：</span>
					<el-input v-model="score" placeholder="请输入评分" style="width:50%;"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">个人介绍：</span>
					<el-input v-model="details" type="textarea" :rows="3" placeholder="请输入个人介绍"
						style="width:50%;"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">城市：</span>
					<el-input v-model="city" placeholder="请输入城市" type="text" style="width:50%;"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">地址：</span>
					<el-input v-model="address" placeholder="请输入地址" type="text" style="width:50%;"
						@keydown.enter.native="selectDt"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
					<el-input v-model="longitude" placeholder="请输入经度" disabled type="text"
						style="width:50%;"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
					<el-input v-model="latitude" placeholder="请输入纬度" disabled type="text" style="width:50%;"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
					<el-radio-group v-model="onLine">
						<el-radio :label="1">上线</el-radio>
						<el-radio :label="2">下线</el-radio>
					</el-radio-group>
				</div> -->
				<div v-if="userType==1">
					<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
					<el-input style="width: 50%;" v-model="name" placeholder="请输入昵称"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">商铺名称：</span>
					<el-input style="width: 50%;" v-model="shopName" placeholder="请输入昵称"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">商铺电话：</span>
					<el-input style="width: 50%;" v-model="phone" placeholder="请输入手机号"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">商铺类型：</span>
					<el-input style="width: 50%;" v-model="shopType" placeholder="请输入身份证号"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">店铺地址：</span>
					<el-input style="width: 50%;" v-model="address" placeholder="请输入店铺地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">营业时间：</span>
					<el-time-select v-model="startTime" :picker-options="{
					    start: '00:00',
					    step: '00:01',
					    end: '23:50'
					  }" placeholder="选择时间" style="width: 150px;">
					</el-time-select> ~
					<el-time-select v-model="endTime" :picker-options="{
					    start: '00:00',
					    step: '00:01',
					    end: '23:59'
					  }" placeholder="选择时间" style="width: 150px;">
					</el-time-select>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">商铺/企业LOGO：</span>
					<div
						style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
						<el-upload class="avatar-uploader" v-model="shopImg" :action="$http.adornUrl('file/upload')"
							:show-file-list="false" :on-success="handleAvatarSuccess4">
							<img v-if="shopImg" :src="shopImg" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="userType==2">
					<span style="width: 200px;display: inline-block;text-align: right;">店铺/企业主页图：</span>
					<div
						style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
						<el-upload class="avatar-uploader" v-model="addressImg"
							:action="$http.adornUrl('file/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess5">
							<img v-if="addressImg" :src="addressImg" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleRz = false">取 消</el-button>
					<el-button type="primary" @click="userNoticeToRz()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
		</div>
	</div>
</template>

<script>
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	var geocoder, map, markersArray = [];
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 0,
				state: 0,
				limit: 10,
				page: 1,
				limit2: 10,
				page2: 1,
				userId: '',
				money: '',
				title: '',
				content: '',
				phone: '',
				startTime: '',
				endTime: '',
				invitationCode: '',
				feiDatamin: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				tablejifen: '',
				autonymData: {},
				tablemoney: {},
				tablenum: {},
				tableData: {},
				userData: {},
				fangkeData: {},
				yuangongData: {},
				tableDatamin: [],
				withdrawData: {},
				walletData: {},
				rechargeData: {},
				sendData: [],
				takeData: {},
				dizhiData: {},
				jiedanData: {},
				dingdanData: {},
				fensiData: {},
				guanzhuData: {},
				wannengData: {},
				wannengOrderData: {},
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible10: false,
				tableDataLoading: true,
				relation_id: '',
				relationId: '',
				tkstatus: '',
				invitation: '',
				nowMonthOver: '',
				lastMonthOver: '',
				nowMonthPaid: '',
				lastMonthPaid: '',
				nowMonthOrderNumber: '',
				nowDayOrderNumber: '',
				hasCash: '',
				search: '',
				classifyIds: 0,
				statusId: -1,
				myPhone: '',
				type: '',
				age: '',
				renwuId: '',
				pinglunData: {},
				tousuData: {},
				states: '',
				tuanduiData: {},
				types: 1,
				typesList: [{
						type: 1,
						name: '一级'
					},
					{
						type: 2,
						name: '二级'
					}
				],
				liaotianData: {},
				huihuaData: {},
				guanjianciData: {},
				wududindanData: {},
				wannengdingdanData: {},
				yuyueTimeDime: [],
				tableDataLoading1: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				title2: '',
				contents: '',
				chatConversationId: '',
				page1: 1,
				limit1: 10,
				userNameF: '',
				phoneF: '',
				dialogFormVisibleCyy: false,
				conters: '',
				nameUser: '',
				imageUser: '',
				invitationCodeU: '',
				invitationU: '',
				phoneU: '',
				genderU: '',
				ageU: '',
				wxCodeU: '',

				userType: 1,
				typesPl: 1,
				youhuiquanData: {},
				bufeiData: {},
				shopData: {},

				// 认证信息
				dialogFormVisibleRz: false,
				address: '',
				addressImg: '',
				back: '',
				endTime: '',
				front: '',
				idNumber: '',
				name: '',
				phone: '',
				shopImg: '',
				shopName: '',
				shopType: '',
				startTime: '',
				tablemoneyB: '',
				typeType: 1,
				label: [],
				bq: '',
				age: '',
				serviceNum: '',
				details: '',
				score: '',
				address: '',
				city: '',
				longitude: '',
				latitude: '',

			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			// 详情跳转
			updates1(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '用户详情') {
					this.dataSelect()
				}

				if (tab._props.label == '认证信息') {
					this.shopSelect()
				}
				if (tab._props.label == '商家详情') {
					this.shopSelect()
				}
				if (tab._props.label == '提现记录') {
					this.limit = 10
						this.page = 1
						this.withdrawSelect()
				}
				if (tab._props.label == '钱包明细') {
					this.limit = 10
					this.page = 1
					this.walletSelect()
				}
				if (tab._props.label == '充值明细') {
					this.limit = 10
					this.page = 1
					this.rechargeSelect()
				}
				if (tab._props.label == '保证金明细') {
					this.limit = 10
					this.page = 1
					this.baozhengjinSelect()
				}
				if (tab._props.label == '我的接单') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.jiedanSelect()
				}
				if (tab._props.label == '我的发布') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.takeSelect()
				}
				if (tab._props.label == '我的订单') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.dingdanSelect()
				}
				if (tab._props.label == '我的足迹') {
					this.limit = 10
					this.page = 1
					this.state = 8
					this.flag = 1
					this.userSelect()
				}
				if (tab._props.label == '我的粉丝') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.fensiSelect()
				}
				if (tab._props.label == '我的关注') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.guanzhuSelect()
				}
				if (tab._props.label == '我的访客') {
					this.limit = 10
					this.page = 1
					this.state = 8
					this.flag = 1
					this.fangkeSelect()
				}
				if (tab._props.label == '我的地址') {
					this.limit = 10
					this.page = 1
					this.state = 8
					this.flag = 1
					this.dizhiSelect()
				}
				if (tab._props.label == '任务派单') {
					this.autonymSelect()
				}
				if (tab._props.label == '任务订单') {
					this.autonymOrderSelect()
				}
				if (tab._props.label == '我的投诉') {
					this.limit = 10
					this.page = 1
					this.states = 3
					this.tousuSelect()
				}
				if (tab._props.label == '我的团队') {
					this.limit = 10
					this.page = 1
					this.types = 1
					this.tuanduiSelect()
				}
				if (tab._props.label == '员工列表') {
					this.limit = 10
					this.page = 1
					this.yuangongSelect()
				}
				if (tab._props.label == '聊天记录') {
					this.limit = 10
					this.page = 1
					this.liaotianSelect()
				}
				if (tab._props.label == '我的常用语') {
					this.limit = 10
					this.page = 1
					this.guanjianciSelect()
				}
				if (tab._props.label == '服务订单统计') {
					this.limit = 10
					this.page = 1
					this.wududindanSelect()
				}
				if (tab._props.label == '万能订单统计') {
					this.limit = 10
					this.page = 1
					this.wannengdingdanSelect()
				}
				if (tab._props.label == '预约时间表') {
					this.yuyueTimeSelect()
				}
				if (tab._props.label == '我的评价') {
					this.page = 1
					this.limit = 10
					this.typesPl = 1
					this.autonymSelectPj()
				}
				if (tab._props.label == '我收到的评价') {
					this.page = 1
					this.limit = 10
					this.typesPl = 2
					this.autonymSelectPj()
				}
				if (tab._props.label == '我的优惠券') {
					this.page = 1
					this.limit = 10
					this.youhuiquanSelect()
				}
				if (tab._props.label == '补费记录') {
					this.page = 1
					this.limit = 10
					this.bufeiSelect()
				}

			},
			handleSizeChange(val) {
				this.limit = val
				this.teamSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.teamSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},
			handleSizeChange2(val) {
				this.limit = val
				this.withdrawSelect()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.withdrawSelect()
			},
			handleSizeChange3(val) {
				this.limit = val
				this.walletSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.walletSelect()
			},
			handleSizeChange4(val) {
				this.limit = val
				this.rechargeSelect()
			},
			handleCurrentChange4(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleSizeChange5(val) {
				this.limit = val
				this.sendSelect()
			},
			handleCurrentChange5(val) {
				this.page = val
				this.sendSelect()
			},
			handleSizeChange6(val) {
				this.limit = val
				this.jiedanSelect()
			},
			handleCurrentChange6(val) {
				this.page = val
				this.jiedanSelect()
			},

			handleSizeChange7(val) {
				this.limit = val
				this.feiSelect()
			},
			handleCurrentChange7(val) {
				this.page = val
				this.feiSelect()
			},
			handleSizeChange8(val) {
				this.limit = val
				this.fensiSelect()
			},
			handleCurrentChange8(val) {
				this.page = val
				this.fensiSelect()
			},
			handleSizeChange9(val) {
				this.limit = val
				this.guanzhuSelect()
			},
			handleCurrentChange9(val) {
				this.page = val
				this.guanzhuSelect()
			},
			handleSizeChange10(val) {
				this.limit = val
				this.dingdanSelect()
			},
			handleCurrentChange10(val) {
				this.page = val
				this.dingdanSelect()
			},
			handleSizeChange11(val) {
				this.limit = val
				this.dizhiSelect()
			},
			handleCurrentChange11(val) {
				this.page = val
				this.dizhiSelect()
			},
			handleSizeChange12(val) {
				this.limit = val
				this.tuanduiSelect()
			},
			handleCurrentChange12(val) {
				this.page = val
				this.tuanduiSelect()
			},
			handleSizeChange13(val) {
				this.limit2 = val
				this.pinglunSelect(this.renwuId)
			},
			handleCurrentChange13(val) {
				this.page2 = val
				this.pinglunSelect(this.renwuId)
			},
			handleSizeChange14(val) {
				this.limit = val
				this.autonymSelect()
			},
			handleCurrentChange14(val) {
				this.page = val
				this.autonymSelect()
			},
			handleSizeChange15(val) {
				this.limit = val
				this.autonymOrderSelect()
			},
			handleCurrentChange15(val) {
				this.page = val
				this.autonymOrderSelect()
			},
			handleSizeChange23(val) {
				this.limit = val
				this.tousuSelect()
			},
			handleCurrentChange23(val) {
				this.page = val
				this.tousuSelect()
			},
			handleSizeChangeDd(val) {
				this.limit = val
				this.yuangongSelect()
			},
			handleCurrentChangeDd(val) {
				this.page = val
				this.yuangongSelect()
			},
			handleSizeChangeLtjl(val) {
				this.limit = val
				this.liaotianSelect()
			},
			handleCurrentChangeLtjl(val) {
				this.page = val
				this.liaotianSelect()
			},
			handleSizeChangeLtnr(val) {
				this.limit1 = val
				this.neirongSelect(this.chatConversationId)
			},
			handleCurrentChangeLtnr(val) {
				this.page1 = val
				this.neirongSelect(this.chatConversationId)
			},
			handleSizeChangeCyy(val) {
				this.limit = val
				this.guanjianciSelect()
			},
			handleCurrentChangeCyy(val) {
				this.page = val
				this.guanjianciSelect()
			},
			handleSizeChangeFwdd(val) {
				this.limit = val
				this.wududindanSelect()
			},
			handleCurrentChangeFwdd(val) {
				this.page = val
				this.wududindanSelect()
			},
			handleSizeChangeWndd(val) {
				this.limit = val
				this.wannengdingdanSelect()
			},
			handleCurrentChangeWndd(val) {
				this.page = val
				this.wannengdingdanSelect()
			},
			handleSizeChangePj(val) {
				this.limit = val
				this.autonymSelectPj()
			},
			handleCurrentChangePj(val) {
				this.page = val
				this.autonymSelectPj()
			},
			handleSizeChangeYhq(val) {
				this.limit = val
				this.youhuiquanSelect()
			},
			handleCurrentChangeYhq(val) {
				this.page = val
				this.youhuiquanSelect()
			},
			handleSizeChangeBf(val) {
				this.limit = val
				this.bufeiSelect()
			},
			handleCurrentChangeBf(val) {
				this.page = val
				this.bufeiSelect()
			},
			handleSizeChangeBzj(val) {
				this.limit = val
				this.baozhengjinSelect()
			},
			handleCurrentChangeBzj(val) {
				this.page = val
				this.baozhengjinSelect()
			},
			timeDate2() {
				this.rechargeSelect()
			},
			// 可提现金额
			withdraw(id, val) {
				if (val == 0) {
					this.$message({
						message: '可提现金额为0元',
						type: 'error',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				} else {
					this.$confirm(`确定要推送提现消息吗?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl(`user/notification/${id}`),
							method: 'post',
							data: this.$http.adornData({})
						}).then(({
							data
						}) => {
							if (data.code == 0) {
								this.$message({
									message: '推送成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.dataSelect()
									}
								})
							} else {
								this.$message.error(data.msg)
							}
						})
					}).catch(() => {})
				}
			},
			// 不可提现金额充值
			rechargenone(safetyMoney, index) {
				this.typeType = index
				this.type = ''
				this.money = ''
				this.tablemoneyB = safetyMoney
				this.dialogFormVisible1 = true
			},
			StairNoticeTo2() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'age': this.age
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						console.log('data', data)
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
						this.$message.error(data.msg)
					}
				})
			},
			// 更改状态
			stateChange(userId,status) {
				if(status==1){
					var val = 2
				}else{
					var val = 1
				}
				this.$confirm(`确定要更改用户状态吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/base/user/update'),
						method: 'post',
						data: this.$http.adornData({
							'userId':userId,
							'status':val
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message.error(data.msg)
						}
					})
				}).catch(() => {})
			},
			// 修改性别
			sexChange(userId, sex) {
				var sexs = 1;
				if (sex == 1) {
					sexs = 2
				}
				if (sex == 2) {
					sexs = 1
				}
				this.$confirm(`确定要更改用户性别吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'sex': sexs
						})
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
									this.dataSelect()
								}
							})
						} else {
							this.$message.error(data.msg)
						}
					})
				}).catch(() => {})
			},
			ageChange(userId, age) {
				this.age = age
				this.dialogFormVisible2 = true
			},
			// 获取数据列表
			dataSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/base/user/info'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data
						this.tablenum = returnData
						this.tablemoney = returnData
						this.tableData = returnData
						// this.phone = returnData.userEntity.phone
						// this.invitationCode = returnData.userEntity.invitationCode
					}
				})
			},
			// 获取团队成员
			teamSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/selectInviteByUserIdLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableDatamin = returnData
				})
			},
			// 非直属
			feiSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectInvitationCodeByUserIdLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'invitationCode': this.invitationCode
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.feiDatamin = returnData
				})
			},
			// 我的足迹数据
			userSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userBrowse/myBrowse'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 获取用户提现记录
			withdrawSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/withdrawRecord/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			// 获取钱包明细
			walletSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/userWalletTransaction/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.walletData = returnData
				})
			},
			// 获取充值明细
			rechargeSelect() {
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/userPaymentRecord/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						// 'endTime': this.endTime,
						// 'startTime': this.startTime,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						let returnData = data.data
						this.rechargeData = returnData
					}else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 2500,
							onClose: () => {
							}
						})
					}
				})
			},
			// 获取保证金明细
			baozhengjinSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/depositRecord/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.walletData = returnData
				})
			},
			// 获取接单
			sendSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('help/selectRunHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.sendData = returnData
				})
			},
			// 我的发布
			takeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('orderTaking/queryAllOrderTaking'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.takeData = returnData
					for (var i in this.takeData.list) {
						if (this.takeData.list[i].detailsImg) {
							this.takeData.list[i].detailsImg = this.takeData.list[i].detailsImg.split(',')
						}
					}
				})
			},
			// 我的接单
			jiedanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/waste/order/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'wasteUserId': userId 
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.jiedanData = returnData
				})
			},
			// 我的订单
			dingdanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/waste/order/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'userId': userId 
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.dingdanData = returnData
				})
			},
			// 任务派单
			autonymSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('help/selectCreateHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.wannengData = returnData
				})
			},
			// 任务订单
			autonymOrderSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('help/selectRunHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.wannengOrderData = returnData
				})
			},
			// 信息数据
			InformationSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl(`information/selectInformationList`),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
						'page': this.page,
						'limit': this.limit,
						'search': this.search,
						'classify': this.classifyIds,
						'status': this.statusId,
						'phone': this.myPhone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.records) {
						if (data.data.records[i].img) {
							data.data.records[i].imgs = data.data.records[i].img.split(',')
						}
					}
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.status = -1
				this.classify = 0
				this.phone = ''
				this.search = ''
				this.page = 1
				this.InformationSelect()
			},
			// 我的访客
			fangkeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userBrowse/myVisitor'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fangkeData = returnData
				})
			},
			// 我的地址
			dizhiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/address/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.dizhiData = returnData
				})
			},
			// 我的粉丝
			fensiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userFollow/selectFans'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fensiData = returnData
				})
			},
			// 我的关注
			guanzhuSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userFollow/selectMyFollow'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.guanzhuData = returnData
				})
			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStateById?userId=${userId}`),
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
								this.fangkeSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			//删除足迹
			deleteuser(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('userBrowse/deleteMyBrowse?id=' + delid),
						method: 'post',
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.userSelect()
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
			// 删除我的访客
			deleteuser1(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('userBrowse/deleteMyVisitor?id=' + delid),
						method: 'post',
						params: this.$http.adornData({
							'id': delid
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
									this.fangkeSelect()
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
			// 员工列表
			yuangongSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'companyId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.yuangongData = returnData
				})
			},
			// 关注/取关
			select(row) {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('userFollow/insert?userId=' + userId + '&followUserId=' + row.userId),
					method: 'post',
					params: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.fensiSelect()
								this.guanzhuSelect()
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
			},
			// 查看评论
			plCompile(row) {
				console.log(row)
				this.renwuId = row.id
				this.dialogFormVisible10 = true
				this.pinglunSelect(row.id)
			},
			// 用户评论列表
			pinglunSelect(id) {
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl('takingComment/selectOrderTakingComment'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page2,
						'limit': this.limit2,
						'id': id
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.pinglunData = returnData
				})
			},
			// 删除评论信息
			pinglundelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`takingComment/deleteOrderTakingComment/${delid}`),
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
									this.pinglunSelect(this.renwuId)
								}
							})
						} else {
							this.$message.error(data.msg)
						}
					})
				}).catch(() => {})
			},
			// 我的投诉
			tousuSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.states,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].image) {
							data.data.list[i].image = data.data.list[i].image.split(',')
						}
					}
					let returnData = data.data;
					this.tousuData = returnData;
				})
			},
			// 我的tuandui
			tuanduiSelect() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectTeamUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tuanduiData = returnData
				})
			},
			// 等级选择事件
			animeDat(state) {
				this.page = 1
				this.tuanduiSelect()
			},
			// 我的团队收入
			tuanduiSelect1() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectTeamOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tuanduiData = returnData
				})
			},
			// 我的聊天会话
			liaotianSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.data.list.length > 0) {
						for (var i in data.data.list) {
							if (data.data.list[i].messageType == 5) {
								data.data.list[i].content = JSON.parse(data.data.list[i].content)
							}
						}
					}
					let returnData = data.data;
					this.liaotianData = returnData;
				})
			},
			// 聊天内容
			complainDetails(row) {
				this.chatConversationId = row.chatConversationId
				this.neirongSelect(this.chatConversationId)
				this.dialogFormVisible4 = true
			},
			// 获取聊天内容列表
			neirongSelect(chatConversationId) {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('chat/selectChatContent'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					if (data.data.list.length > 0) {
						for (var i in data.data.list) {
							if (data.data.list[i].messageType == 5) {
								data.data.list[i].content = JSON.parse(data.data.list[i].content)
							}
						}
					}
					let returnData = data.data;
					this.huihuaData = returnData
				})
			},
			// 我的常用语
			guanjianciSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/page/20/' + this.page + '/' + this.limit),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.guanjianciData = returnData
				})
			},
			// 添加关键词
			add() {
				console.log('关键词')
				this.title2 = '添加关键词'
				this.contents = ''
				this.guanjianciId = ''
				this.dialogFormVisibleCyy = true
			},
			// 修改关键词
			updateGjc(row) {
				this.title2 = '修改关键词'
				this.contents = row.content
				this.guanjianciId = row.id
				this.dialogFormVisibleCyy = true
			},
			addGuanjianci() {
				var datas = {}
				let userId = this.$route.query.userId
				if (this.guanjianciId == '') {
					var url = 'message/add'
					datas.state = 20
					datas.userId = userId
					datas.content = this.contents
				}
				if (this.guanjianciId != '') {
					var url = 'message/update'
					datas.state = 20
					datas.userId = userId
					datas.content = this.contents
					datas.id = this.guanjianciId
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible3 = false
								this.dialogFormVisibleCyy = false
								this.guanjianciSelect()
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
			},
			deletes(row) {
				let userId = this.$route.query.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`message/delete/${row.id}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.guanjianciSelect()
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
			// 跳转员工调度
			diaodu() {
				let userId = this.$route.query.userId
				this.$router.push({
					path: '/riderScheduling',
					query: {
						companyId: userId
					}
				})
			},
			// 服务订单统计
			wududindanSelect() {
				this.tableDataLoading = true
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('orders/selectStaffUserOrdersByTime'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'companyId': userId,
						'phone': this.phoneF,
						'userName': this.userNameF,
						'startTime': this.startTime,
						'endTime': this.endTime,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.wududindanData = returnData
				})
			},
			selectF() {
				this.page = 1
				this.wududindanSelect()
			},
			cleansF() {
				this.page = 1
				this.phoneF = ''
				this.userNameF = ''
				this.startTime = ''
				this.endTime = ''
				this.wududindanSelect()
			},
			// 服务订单统计
			wannengdingdanSelect() {
				this.tableDataLoading = true
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('help/orderStatistics'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'companyId': userId,
						'phone': this.phoneF,
						'userName': this.userNameF,
						'startTime': this.startTime,
						'endTime': this.endTime,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.wannengdingdanData = returnData
				})
			},
			selectF() {
				this.page = 1
				this.wannengdingdanSelect()
			},
			cleansF() {
				this.page = 1
				this.phoneF = ''
				this.userNameF = ''
				this.startTime = ''
				this.endTime = ''
				this.wannengdingdanSelect()
			},
			handleAvatarSuccess1(file, fileList) {
				this.imageUser = file.data.path
			},
			handleAvatarSuccess2(file, fileList) {
				this.back = file.data.path
			},
			handleAvatarSuccess3(file, fileList) {
				this.front = file.data.path
			},
			handleAvatarSuccess4(file, fileList) {
				this.shopImg = file.data.path
			},
			handleAvatarSuccess5(file, fileList) {
				this.addressImg = file.data.path
			},
			// 添加标签
			btnTj() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签名',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.label.push(this.bq)
					this.bq = ''
				}
			},
			// 删除标签
			dels1(index) {
				this.label.splice(index, 1);
				console.log(this.label)
			},
			userChange(row, index) {
				this.userType = index
				this.imageUser = row.avatar
				this.nameUser = row.userName
				this.invitationU = row.inviterCode
				this.invitationCodeU = row.invitationCode
				this.phoneU = row.phone
				this.genderU = row.gender
				this.ageU = row.age
				this.wxCodeU = row.wxCode
				this.userId = this.$route.query.userId
				this.dialogFormVisible3 = true
			},
			userNoticeTo() {
				var datas = {}
				datas.userId = this.$route.query.userId
				if (this.userType == 1) {
					datas.userName = this.nameUser
				}
				if (this.userType == 2) {
					datas.avatar = this.imageUser
				}
				if (this.userType == 3) {
					datas.gender = this.genderU
				}
				if (this.userType == 4) {
					datas.phone = this.phoneU
				}
				if (this.userType == 5) {
					datas.age = this.ageU
				}
				if (this.userType == 6) {
					datas.wxCode = this.wxCodeU
				}
				if (this.userType == 11) {
					datas.invitationCode = this.invitationCodeU
				}
				if (this.userType == 10) {
					datas.inviterCode = this.invitationU
				}

				this.$http({
					url: this.$http.adornUrl('admin/base/user/update'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisible3 = false
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 预约时间表数据
			yuyueTimeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('serviceTime/selectServiceTime'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
						'page': this.page,
						'limit': this.limit,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.yuyueTimeDime = returnData
				})
			},
			// 我的评价
			autonymSelectPj() {
				let userId = this.$route.query.userId
				// if (this.types == 1) {
				// 	var userid1 = userId
				// 	var userid2 = ''
				// } else {
				// 	var userid1 = ''
				// 	var userid2 = userId
				// }
				var datas = {}
				datas.page = this.page
				datas.limit = this.limit
				if (this.typesPl == 1) {
					datas.userId = userId
				}
				if (this.typesPl == 2) {
					datas.riderUserId = userId
				}
				this.$http({
					url: this.$http.adornUrl('admin/evaluate/getUserEvaluate'),
					method: 'get',
					params: this.$http.adornParams(datas)
				}).then(({
					data
				}) => {

					this.tableDataLoading = false
					let returnData = data.data
					this.autonymData = returnData
				})
			},
			// 筛选信息
			animeDat2(state) {
				this.autonymSelectPj()
			},
			// 我的优惠券
			youhuiquanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/couponUser/getMyCouponList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'status': '',
						'phone': '',
						'couponName': '',
						'userName': ''
					})
				}).then(({
					data
				}) => {
					console.log(data)
					this.tableDataLoading = false
					let returnData = data.data
					this.youhuiquanData = returnData
				})
			},
			// 获取补费列表数据
			bufeiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/repairRecord/getRecordList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.bufeiData = data.data
					this.tableDataLoading = false

				})
			},
			// 获取 实名信息
			shopSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/userAuthentication/info'),
					method: 'post',
					data: this.$http.adornData({
						'id': userId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data
						this.shopData = returnData
					}
				})
			},
			// 跳转地图
			mapBtn(row) {
			},
			// 修改认证信息弹框
			updateChange(row, index) {
				this.userType = index

				if (index == 1) {
					this.back = row.back
					this.front = row.front
					this.idNumber = row.idNumber
					this.name = row.name
					this.phone = row.phone
					this.label = row.label.split(',')
					this.bq = ''
					this.age = row.age
					this.serviceNum = row.serviceNum
					this.details = row.details
					this.score = row.score
					this.address = row.address
					this.city = row.city
					this.longitude = row.longitude
					this.latitude = row.latitude
					this.getMyLocation()

				} else {
					this.name = row.name
					this.address = row.address
					this.addressImg = row.addressImg
					this.endTime = row.endTime
					this.phone = row.phone
					this.shopImg = row.shopImg
					this.shopName = row.shopName
					this.shopType = row.shopType
					this.startTime = row.startTime

				}
				this.dialogFormVisibleRz = true
			},
			userNoticeToRz() {
				var datas = {}
				datas.userId = this.$route.query.userId
				if (this.userType == 1) {
					datas.back = this.back
					datas.front = this.front
					datas.idNumber = this.idNumber
					datas.name = this.name
					datas.phone = this.phone
					datas.userName = this.nameUser
					datas.label = this.label.toString()
					datas.age = this.age
					datas.serviceNum = this.serviceNum
					datas.details = this.details
					datas.score = this.score
					datas.address = this.address
					datas.longitude = this.longitude
					datas.latitude = this.latitude

				}
				if (this.userType == 2) {
					datas.name = this.name
					datas.address = this.address
					datas.addressImg = this.addressImg
					datas.endTime = this.endTime
					datas.phone = this.phone
					datas.shopImg = this.shopImg
					datas.shopName = this.shopName
					datas.shopType = this.shopType
					datas.startTime = this.startTime
				}

				this.$http({
					url: this.$http.adornUrl('user/updateCertification'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
						this.dialogFormVisibleRz = false
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 修改保证金
			rechargenoneB(safetyMoney, index) {
				// if (index == 2) {
				this.typeType = index
				this.type = ''
				this.money = ''
				this.tablemoneyB = safetyMoney
				this.dialogFormVisible1 = true
				// }
			},
			StairNoticeTo1() {
				let userId = this.$route.query.userId
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改类型',
						type: 'warning'
					})
					return
				}
				if (this.money === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				if (this.type == 'decr') {
					if (this.money > this.tablemoneyB) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '最高金额为' + this.tablemoneyB,
							type: 'warning'
						})
						return
					}
				}
				if (this.typeType == 1) {
					this.$http({
						url: this.$http.adornUrl('admin/userWallet/updateBalance'),
						method: 'post',
						// params: this.$http.adornParams({
						data: this.$http.adornData({
							'userId':userId,
							'operationType':this.type,
							'amount':this.money
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							this.dialogFormVisible1 = false
						} else {
							this.$message.error(data.msg)
						}
					})
				} else {
					this.$http({
						url: this.$http.adornUrl('admin/base/user/updateDeposit'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'operationType': this.type,
							'amount': this.money
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							this.dialogFormVisible1 = false
						} else {
							this.$message.error(data.msg)
						}
					})
				}

			},
			// 地图-------------
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("DSQBZ-5MM3P-HEODO-VG6IX-SBRJE-PSBNX", "省钱兄家政服务");
				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				// this.latitude = position.lat;
				// this.longitude = position.lng;
				// this.city = position.city;
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}

				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度

					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng.getLng() + '&key=5M6BZ-6YPLJ-AZRFX-D2XM3-UWTDT-SMF34&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response, response.result.address_component.city)
						that.address = response.result.address
						// that.city = response.result.address_component.city
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					if (!marker) {
						marker = new qq.maps.Marker({
							map: map,
							position: event.latLng
						});
					} else {
						marker.setPosition(event.latLng)
					}
					// markersArray.push(marker);
				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
			},
			// 地图定位
			selectDt() {
				// console.log(this.detailadd, this.detailadd.replace(/^\s+|\s+$/gm, ''))

				if (this.address == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入详细地址',
						type: 'warning'
					});
					return
				} else {
					var add = this.address
					let that = this
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + add +
						'&key=5M6BZ-6YPLJ-AZRFX-D2XM3-UWTDT-SMF34&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						// handle success
						if (response.message == '查询无结果') {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: '详细地址输入有误，请重新输入',
								type: 'warning'
							});
							return
						}
						console.log('response', response)
						that.longitude = response.result.location.lng; // 经度
						that.latitude = response.result.location.lat; // 纬度
						// that.city = response.result.address_components.city;
						// that.detailadd = response.result.address_components.street
						// that.detailadd = response.result.address_components.province + response.result
						// 	.address_components.city + response.result.address_components.district + response.result.title
						that.setMap()
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
				}
			},
		},
		mounted() {
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelect'
		}
	}
</script>

<style lang="scss" scoped>
.user-detail-page {
	padding: 0;
	overflow-x: hidden;
}

.user-detail-page__nav {
	display: flex;
	align-items: center;
	margin-bottom: 12px;
	font-size: 14px;
}

.user-detail-page__back-link {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	color: var(--nf-text-secondary, #5a6578);
	text-decoration: none;
}

.user-detail-page__back-link:hover {
	color: var(--nf-brand-deep, #017c52);
}

.user-detail-page__back-icon {
	width: 1.2em;
	height: 1.2em;
}

.user-detail-page__nav-sep {
	margin: 0 12px;
	color: var(--nf-border, #e8eaed);
}

.user-detail-page__nav-title {
	font-size: 16px;
	font-weight: 600;
}

.user-detail-page__hero {
	margin-bottom: 12px;
}

.user-detail-page__kpi-row {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 12px;
	margin-bottom: 12px;
}

.user-detail-page__kpi {
	display: flex;
	align-items: center;
	gap: 12px;
	padding: 16px;
	background: #fff;
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-md, 8px);
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.user-detail-page__kpi-icon {
	flex-shrink: 0;
	width: 44px;
	height: 44px;
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: var(--nf-radius-md, 8px);
	background: var(--nf-brand-lite, #e1f5ee);
	color: var(--nf-brand, #01af74);
	font-size: 20px;
}

.user-detail-page__kpi-body {
	min-width: 0;
}

.user-detail-page__kpi-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
	margin-bottom: 4px;
}

.user-detail-page__kpi-value {
	font-size: 13px;
	color: var(--nf-text-primary, #1a2433);
}

.user-detail-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: var(--nf-brand, #01af74);
}

.user-detail-page__tabs-wrap {
	margin-bottom: 0;
}

.user-detail-page__tabs {
	margin-bottom: 0;
}

.user-detail-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.user-detail-page__detail-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.user-detail-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.user-detail-page__table-card >>> .el-table {
	border-radius: 0;
}

.user-detail-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.user-detail-page__table >>> .el-table__body td {
	background: #fff;
}

.user-detail-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.user-detail-page__link {
	color: var(--nf-brand, #01af74);
	cursor: pointer;
}

.user-detail-page__link:hover {
	color: var(--nf-brand-deep, #017c52);
}

.detailtabel h2 {
	margin-top: 0;
}

.detailtabel .table_main table {
	width: 100%;
	border-collapse: collapse;
}

.detailtabel .table_main {
	border: none;
}

.detailtabel table tr {
	border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.detailtabel table tr th {
	background-color: var(--nf-page-bg, #f4f6fa);
	padding: 12px 16px;
	border-right: 1px solid var(--nf-border, #e8eaed);
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	width: 14%;
}

.detailtabel table tr td {
	padding: 12px 16px;
	border-right: 1px solid var(--nf-border, #e8eaed);
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
	color: var(--nf-text-primary, #1a2433);
}

.detailtabel table th {
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
	font-size: 13px;
	line-height: 1.5;
}

.detailtabel .el-button--mini {
	color: var(--nf-brand, #01af74);
	background: #fff;
	border: none;
	padding: 0 4px;
}

.border-rt {
	border-right: none !important;
}

.border-bt {
	border-bottom: none !important;
}

@media (max-width: 1200px) {
	.user-detail-page__kpi-row {
		grid-template-columns: repeat(2, 1fr);
	}
}

@media (max-width: 640px) {
	.user-detail-page__kpi-row {
		grid-template-columns: 1fr;
	}
}
</style>