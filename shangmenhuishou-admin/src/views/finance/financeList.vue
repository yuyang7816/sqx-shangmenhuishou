<template>
	<div class="finance-list-page">
		<div class="nf-page-hero finance-list-page__hero">
			<span class="nf-page-hero__badge">财务管理</span>
			<h2 class="nf-page-hero__title">财务中心</h2>
			<p class="nf-page-hero__desc">管理用户提现申请、提现统计与保证金流水，支持筛选查询、批量转账与数据导出</p>
		</div>

		<div class="finance-list-page__tabs-wrap">
			<el-tabs v-model="activeName" class="finance-list-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="提现管理" name="first">
					<div class="nf-filter-card finance-list-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-search"></i>
							<span>筛选条件</span>
						</div>
						<div class="nf-filter-card__body finance-list-page__filter-body">
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">状态：</span>
								<el-select v-model="statusT" placeholder="请选择类型" class="finance-list-page__select" @change="phoneSelect()">
									<el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
									</el-option>
								</el-select>
							</div>
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">用户昵称：</span>
								<el-input class="finance-list-page__input" @keydown.enter.native="phoneSelect" clearable placeholder="请输入用户昵称"
									v-model="zhifubaoName"></el-input>
							</div>
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">手机号：</span>
								<el-input class="finance-list-page__input" @keydown.enter.native="phoneSelect" clearable placeholder="请输入手机号"
									v-model="phone"></el-input>
							</div>
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">支付宝账号：</span>
								<el-input class="finance-list-page__input finance-list-page__input--wide" @keydown.enter.native="phoneSelect" clearable placeholder="请输入支付宝账号"
									v-model="zhifubao"></el-input>
							</div>
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">提现方式：</span>
								<el-select v-model="classify" placeholder="请选择提现方式" class="finance-list-page__select" @change="phoneSelect()">
									<el-option v-for="item in classifyStates" :key="item.value" :label="item.label" :value="item.value">
									</el-option>
								</el-select>
							</div>
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">开始时间：</span>
								<el-date-picker class="finance-list-page__date" v-model="startTime" align="right"
									type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
								</el-date-picker>
							</div>
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">截止时间：</span>
								<el-date-picker class="finance-list-page__date" v-model="endTime" align="right"
									type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
								</el-date-picker>
							</div>
							<div class="finance-list-page__actions">
								<el-button size="mini" type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
								<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
							</div>
						</div>
					</div>
					<div class="nf-table-card finance-list-page__table-card">
						<div class="finance-list-page__toolbar">
							<p class="finance-list-page__tip">* 导出提示：导出数据前请进行时间或者状态等筛选，否则导出数据量过多易出现卡顿或系统崩溃</p>
							<div class="finance-list-page__toolbar-actions">
								<el-button size="mini" type="primary" icon="el-icon-s-finance"
									@click="transferClcik()" :disabled="checkBoxData.length <= 0 || !isAuth('financeList:transfer')">
									批量转账
								</el-button>
								<el-button size="mini" type="warning" icon="el-icon-download" @click="exportBtn" :disabled="!isAuth('financeList:daochu')">导出Excel</el-button>
							</div>
						</div>
						<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData" class="finance-list-page__table">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称" width="150">
					<template slot-scope="scope">
						<span class="finance-list-page__link"
							@click="updates(scope.row)">{{scope.row.userEntity ? scope.row.userEntity.userName : '未绑定'}}</span>
					</template>
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
								<div v-if="scope.row.withdrawalAccount.accountNumber" class="finance-list-page__qr-wrap">
									<el-popover placement="top-start" title="" trigger="hover">
										<img class="finance-list-page__thumb" :src="scope.row.withdrawalAccount.accountNumber" alt="" slot="reference">
										<img class="finance-list-page__preview" :src="scope.row.withdrawalAccount.accountNumber" alt="">
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
						<span class="finance-list-page__link" v-if="scope.row.status == 1">待转账</span>
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
				<!-- <el-table-column prop="orderNumber" label="转账订单号" width="150">
				</el-table-column> -->
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('financeList:transfer') ||scope.row.status != 1"
							@click="batch(scope.row)">转账
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('financeList:refund') || scope.row.status != 1"
							@click="refund(scope.row)">拒绝
						</el-button>
					</template>
				</el-table-column>
			</el-table>
						<div class="finance-list-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="totalnum">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
		<!-- <el-tab-pane label="收入统计" name="second">
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right"
					type="datetime" format="yyyy-MM-dd hh:mm:ss" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择开始时间"
					@change="animeSelect">
				</el-date-picker>
			</div>
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">发布服务支出金额</div>
							<div class="text_color"><span>{{MoneyData.sumPrice ? MoneyData.sumPrice : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">用户收益</div>
							<div class="text_color">
								<span>{{MoneyData.sumMoneyBySend ? MoneyData.sumMoneyBySend : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">平台服务收益</div>
							<div class="text_color">
								<span>{{MoneyData.sumMoneyByProfit ? MoneyData.sumMoneyByProfit  : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
			</el-row>
		</el-tab-pane> -->
		<el-tab-pane label="提现统计" name="third">
					<div class="nf-filter-card finance-list-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-search"></i>
							<span>筛选条件</span>
						</div>
						<div class="nf-filter-card__body finance-list-page__filter-body">
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">查询方式：</span>
								<el-select v-model="flag" class="finance-list-page__select" @change="animeDat2">
									<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
									</el-option>
								</el-select>
							</div>
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">开始时间：</span>
								<el-date-picker class="finance-list-page__date finance-list-page__date--wide" v-model="info1.stockDate1" align="right"
									type="date	" format="yyyy-MM-dd hh:mm:ss" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择开始时间"
									@change="animeSelect2">
								</el-date-picker>
							</div>
						</div>
					</div>
					<div class="finance-list-page__kpi-row">
						<div class="finance-list-page__kpi">
							<div class="finance-list-page__kpi-icon"><i class="el-icon-money"></i></div>
							<div class="finance-list-page__kpi-body">
								<div class="finance-list-page__kpi-label">累计提现金额</div>
								<div class="finance-list-page__kpi-value">
									<span>{{withdrawData.totalAmount?withdrawData.totalAmount:0}}</span>元
								</div>
							</div>
						</div>
						<div class="finance-list-page__kpi">
							<div class="finance-list-page__kpi-icon"><i class="el-icon-s-order"></i></div>
							<div class="finance-list-page__kpi-body">
								<div class="finance-list-page__kpi-label">提现次数</div>
								<div class="finance-list-page__kpi-value">
									<span>{{withdrawData.totalCount?withdrawData.totalCount:0}}</span>笔
								</div>
							</div>
						</div>
						<div class="finance-list-page__kpi">
							<div class="finance-list-page__kpi-icon"><i class="el-icon-time"></i></div>
							<div class="finance-list-page__kpi-body">
								<div class="finance-list-page__kpi-label">待转账次数</div>
								<div class="finance-list-page__kpi-value">
									<span>{{withdrawData.waitCount?withdrawData.waitCount:0}}</span>笔
								</div>
							</div>
						</div>
					</div>
				</el-tab-pane>
		<!-- <el-tab-pane label="支付统计" name="sixth">
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat3">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right"
					type="datetime" format="yyyy-MM-dd hh:mm:ss" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择开始时间"
					@change="animeSelect3">
				</el-date-picker>
			</div>
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">充值总金额</div>
							<div class="text_color"><span>{{rechgeData.allMoney ? rechgeData.allMoney : 0}}</span>元
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">微信公众号充值金额</div>
							<div class="text_color">
								<span>{{rechgeData.wxOfficial ? rechgeData.wxOfficial : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">微信小程序充值金额</div>
							<div class="text_color">
								<span>{{rechgeData.wxCourse ? rechgeData.wxCourse : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">微信app充值金额</div>
							<div class="text_color">
								<span>{{rechgeData.wxApp ? rechgeData.wxApp : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">支付宝App充值金额</div>
							<div class="text_color">
								<span>{{rechgeData.zfbApp ? rechgeData.zfbApp : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">支付宝H5充值金额</div>
							<div class="text_color">
								<span>{{rechgeData.zfbH5 ? rechgeData.zfbH5 : 0}}</span>元</div>
						</div>
					</div>
				</el-col>
			</el-row>
		</el-tab-pane>
		<el-tab-pane label="支付记录" name="fourth">
			<div style="margin:2% 0;display: inline-block;">
				<span>状态：</span>
				<el-select v-model="state" placeholder="请选择状态" style="width:150px;" @change="selectTrigger(state)">
					<el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;">
				<span>开始时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
				<span>截止时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="date"
					format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
				</el-date-picker>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="timeDate">查询
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="rechargeData.list">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
					<template slot-scope="scope">
						<span style="color: #01af74;cursor: pointer;"
							@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderId" label="充值订单号" width="250">
				</el-table-column>
				<el-table-column prop="money" label="充值金额" width="100">
				</el-table-column>
				<el-table-column label="分类" width="120">
					<template slot-scope="scope">
						<span style="color: #01af74;cursor: pointer;" v-if="scope.row.classify == 1">app微信</span>
						<span style="color: #01af74;cursor: pointer;" v-else-if="scope.row.classify == 2">微信公众号</span>
						<span style="color: #01af74;cursor: pointer;" v-else-if="scope.row.classify == 3">微信小程序</span>
						<span style="color: #01af74;cursor: pointer;" v-else-if="scope.row.classify == 4">支付宝</span>
						<span style="color: #01af74;cursor: pointer;" v-else-if="scope.row.classify == 5">支付宝</span>
						<span style="color: #01af74;cursor: pointer;" v-else>暂无</span>
					</template>
				</el-table-column>
				<el-table-column label="状态" width="120">
					<template slot-scope="scope">
						<span style="color: #01af74;" v-if="scope.row.state == 0">待充值</span>
						<span style="color: #01af74;" v-if="scope.row.state == 1">充值成功</span>
						<span style="color: #01af74;" v-if="scope.row.state == 2">充值失败</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="170">
				</el-table-column>
				<el-table-column prop="payTime" label="支付时间" width="170">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[5, 10, 15]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="rechargeData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane> -->
		<el-tab-pane label="保证金统计" name="eight">
					<div class="nf-filter-card finance-list-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-search"></i>
							<span>筛选条件</span>
						</div>
						<div class="nf-filter-card__body finance-list-page__filter-body">
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">用户名称：</span>
								<el-input class="finance-list-page__input" @keydown.enter.native="rechargeSelect" clearable placeholder="请输入用户名称"
									v-model="userNameT"></el-input>
							</div>
							<div class="finance-list-page__field">
								<span class="finance-list-page__label">手机号：</span>
								<el-input class="finance-list-page__input" @keydown.enter.native="rechargeSelect" clearable placeholder="请输入手机号"
									v-model="phoneT"></el-input>
							</div>
							<div class="finance-list-page__actions">
								<el-button size="mini" type="primary" icon="el-icon-search" @click="rechargeSelect">查询</el-button>
								<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleansRecharge">重置</el-button>
							</div>
						</div>
					</div>
					<div class="nf-table-card finance-list-page__table-card">
						<el-table v-loading="tableDataLoading" :data="rechargeData.records" class="finance-list-page__table">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
					<template slot-scope="scope">
						<span class="finance-list-page__link"
							@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号"></el-table-column>
				<el-table-column prop="title" label="标题"></el-table-column>
				<el-table-column prop="content" label="内容"></el-table-column>
				<el-table-column prop="money" label="金额" width="100">
					<template slot-scope="scope">
						<span class="finance-list-page__amount--minus" v-if="scope.row.direction==2">- {{ scope.row.amount }}</span>
						<span class="finance-list-page__amount--plus" v-if="scope.row.direction==1">+ {{ scope.row.amount }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
			</el-table>
						<div class="finance-list-page__pagination">
							<el-pagination @size-change="handleSizeChangeBzj" @current-change="handleCurrentChangeBzj"
								:page-sizes="[5, 10, 15]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="rechargeData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 拒绝弹框 -->
		<el-dialog title="拒绝" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="拒绝原因：" :label-width="formLabelWidth">
					<el-input v-model="form.content" type="textarea" rows="4" placeholder="请输入拒绝原因"
						style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refundNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				balance: -1,
				zhifubao: '',
				tkstatus: -1,
				state: -1,
				statusT:'',
				statusList:[
					{
							label: '全部',
							value: ''
						},
						{
							label: '待转账',
							value: 1
						},
						{
							label: '已转账',
							value: 2
						},
						{
							label: '已拒绝',
							value: 3
						}
				],
				states: [{
						label: '全部',
						value: -1
					},
					{
						label: '待充值',
						value: 0
					},
					{
						label: '充值成功',
						value: 1
					},
					{
						label: '充值失败',
						value: 2
					}
				],
				zhifubaoName: '',
				content: '',
				startTime: '',
				endTime: '',
				cashId: '',
				type: -1,
				totalnum: 0,
				totalnum2: 0,
				sqxTotal: '',
				userTotal: '',
				taunTotal: '',
				relationId: '',
				sumMoneyByTime: '',
				money: '',
				activeName: 'first',
				formLabelWidth: '200px',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				tableDataLoading: false,
				tableData: [],
				rechargeData: {},
				rechgeData: {},
				withdrawData: {},
				depositData: [],
				MoneyData: {},
				checkBoxData: [], //多选框选择的值
				flag: 'day',
				form: {
					cashId: '',
					content: '',
				},
				form2: {
					id: '',
					money: '',
					createTime: ''
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				flags: [{
					value: 'day',
					label: '按天查询'
				}, {
					value: 'month',
					label: '按月查询'
				}, {
					value: 'year',
					label: '按年查询'
				}],
				phone:'',
				classify:'',
				classifyStates: [{
						label: '全部',
						value: ''
					},
					{
						label: '微信',
						value: 1
					},
					{
						label: '支付宝',
						value: 2
					}
				],
				userNameT:'',
				phoneT:'',
				
			}
		},
		methods: {
			// 会员详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 批量转账
			transferClcik(id) {

				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				for (var i in ids) {
					this.$http({
						url: this.$http.adornUrl(`cash/alipay/${ids[i]}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'warning'
							})
							return
						}else {
							this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'error'
							})
							return
						}
					})
				}
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			//转账
			batch(row) {
				if (row.state == 1) {
					this.$message({
						message: '已转账，请勿重复操作！',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$confirm(`确定转账?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/withdrawRecord/confirm'),
						method: 'post',
						data: this.$http.adornData({
							'id':row.id,
							'status':2,
							'reason':'同意',
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: data.msg,
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}else{
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
						
					})
				})
			},
			// 拒绝
			refund(row) {
				if (row.state == -1) {
					this.$message({
						message: '已拒绝，请勿重复操作！',
						type: 'error',
						duration: 1500
					})
				} else {
					this.dialogFormVisible = true
					this.form.content = ''
					this.form.cashId = row.id
				}
			},
			// 拒绝操作
			refundNoticeTo() {
				if (this.form.content == '') {
					this.$message({
						message: '请输入拒绝原因',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/withdrawRecord/confirm'),
					method: 'post',
					data: this.$http.adornData({
						'id':this.form.cashId,
						'status':3,
						'reason':this.form.content,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						return
					}else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
							}
						})
						return
					}
				})
			},
			timeDate() {
				this.rechargeSelect()
			},
			// 日期选择
			animeDat(value) {
				let vanumber = value
				this.incomeSelect()
				// if (vanumber === 1) {
				// 	this.flag = 1
				// 	this.incomeSelect()
				// }
				// if (vanumber === 2) {
				// 	this.flag = 2
				// 	this.incomeSelect()
				// }
				// if (vanumber === 3) {
				// 	this.flag = 3
				// 	this.incomeSelect()
				// }
			},
			// 日期选择2
			animeDat2(value) {
				let vanumber = value
				this.withdrawSelect()
				// if (vanumber === 1) {
				// 	this.flag = 1
				// 	this.withdrawSelect()
				// }
				// if (vanumber === 2) {

				// 	this.flag = 2
				// 	this.withdrawSelect()
				// }
				// if (vanumber === 3) {
				// 	this.flag = 3
				// 	this.withdrawSelect()
				// }
			},
			// 日期选择3
			animeDat3(value) {
				let vanumber = value
				this.rechSelect()
				// if (vanumber === 1) {
				// 	this.flag = 1
				// 	this.rechSelect()
				// }
				// if (vanumber === 2) {

				// 	this.flag = 2
				// 	this.rechSelect()
				// }
				// if (vanumber === 3) {
				// 	this.flag = 3
				// 	this.rechSelect()
				// }
			},
			animeSelect() {
				this.incomeSelect()
			},
			animeSelect2() {
				this.withdrawSelect()
			},
			animeSelect3() {
				this.rechSelect()
			},
			//处理默认选中当前日期
			getNowTime1() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			getNowTime2() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			//处理默认选中当前日期
			getNowTime() {
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
				this.rechargeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleSizeChangeBzj(val) {
				this.limit = val
				this.baozhengjinSelect()
			},
			handleCurrentChangeBzj(val) {
				this.page = val
				this.baozhengjinSelect()
			},

			handleClick(tab, event) {
				this.startTime= ''
				this.endTime= ''
				if (tab._props.label == '提现管理') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '收入统计') {
					this.flag = 'day'
					this.incomeSelect()
				}
				if (tab._props.label == '充值统计') {
					this.flag = 'day'
					this.rechSelect()
				}
				if (tab._props.label == '提现统计') {
					this.flag = 'day'
					this.withdrawSelect()
				}
				if (tab._props.label == '支付记录') {
					this.page = 1
					this.limit = 10
					this.rechargeSelect()
				}
				if (tab._props.label == '保证金统计') {
					this.page = 1
					this.limit = 10
					this.baozhengjinSelect()
				}
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans2() {
				this.zhifubao = ''
				this.zhifubaoName = ''
				this.phone = ''
				this.startTime= ''
				this.endTime= ''
				this.classify = ''
				this.statusT = ''
				this.page = 1
				this.dataSelect()
			},
			// 支付宝账号查询
			//     zfbselect () {
			//       if (this.zhifubao == '') {
			//         this.$notify({
			//           title: '提示',
			//           duration: 1800,
			//           message: '请输入支付宝账号',
			//           type: 'error'
			//         })
			//         return
			//       }
			// this.page = 1
			//       this.tableDataLoading = true
			//       this.$http({
			//         url: this.$http.adornUrl('user/selectPayDetails'),
			//         method: 'get',
			//         params: this.$http.adornParams({
			// 	  'page':this.page,
			// 	  'limit':this.limit,
			// 	  'zhifubao':this.zhifubao
			//   })
			//       }).then(({data}) => {
			//         this.tableDataLoading = false
			//         let returnData = data.data
			//        this.tableData = returnData.list
			//        this.totalnum = returnData.totalCount
			//       })
			//     },
			//     // 支付宝名称
			//     qdselect () {
			//       if (this.zhifubaoName == '') {
			//         this.$notify({
			//           title: '提示',
			//           duration: 1800,
			//           message: '请输入渠道账号',
			//           type: 'error'
			//         })
			//         return
			//       }
			//       this.tableDataLoading = true
			//       this.$http({
			//         url: this.$http.adornUrl('user/selectPayDetails'),
			//         method: 'get',
			//         params: this.$http.adornParams({
			// 	  'page':this.page,
			// 	  'limit':this.limit,
			// 	  'zhifubaoName':this.zhifubaoName
			//   })
			//       }).then(({data}) => {
			//         this.tableDataLoading = false
			//         let returnData = data.data
			//         this.tableData = returnData.list
			//         this.totalnum = returnData.totalCount
			//       })
			//     },
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/withdrawRecord/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'userName': this.zhifubaoName,
						'accountNumber': this.zhifubao,
						'phone':this.phone,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'accountType':this.classify,
						'status':this.statusT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData.records
					this.totalnum = returnData.total
				})
			},
			// 收入统计
			incomeSelect() {
				this.$http({
					url: this.$http.adornUrl('statistical/statisticsIncomeMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.MoneyData = returnData
					}
				})
			},
			// 充值统计
			rechSelect() {
				this.$http({
					url: this.$http.adornUrl('cash/payMember'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.rechgeData = returnData
					}
				})
			},
			// 提现统计
			withdrawSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/index/withdrawStatistics'),
					method: 'post',
					data: this.$http.adornData({
						'findDate': this.info1.stockDate1,
						'findType': this.flag
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			selectTrigger() {
				this.rechargeSelect()
			},
			// 获取充值记录
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info2.stockDate2
				}
				if (this.startTime == '') {
					this.startTime = this.info.stockDate
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRecharge'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'startTime': this.startTime,
						'endTime': this.endTime
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.rechargeData = returnData
					this.totalnum2 = returnData.totalCount
				})
			},
			// 导出
			exportBtn() {
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				// var endTime = this.endTime
				// if (this.endTime != '') {
				// 	endTime = this.endTime + " 23:59:59"
				// }
				this.$http({
					url: this.$http.adornUrl('cash/exportExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'zhifubaoName': this.zhifubaoName,
						'zhifubao': this.zhifubao,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'phone':this.phone,
						'classify':this.classify
					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '提现列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 保证金统计
			baozhengjinSelect(){
				this.tableDataLoading = true
				// 统计
				// this.$http({
				// 	url: this.$http.adornUrl('user/getSafetyMoneyData'),
				// 	method: 'get',
				// 	params: this.$http.adornParams({
				// 		'time': this.info1.stockDate1,
				// 		'flag': this.flag
				// 	})
				// }).then(({
				// 	data
				// }) => {
				// 	if (data.code == 0) {
				// 		let returnData = data.data
				// 		this.rechgeData = returnData
				// 	}
				// })

				// 记录
				this.$http({
					url: this.$http.adornUrl('admin/depositRecord/page'),
					method: 'post',
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						'direction': '',
						'userName':this.userNameT,
						'phone':this.phoneT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.rechargeData = returnData
					this.totalnum2 = returnData.totalCount
				})
			},
			// 查询
			rechargeSelect() {
				this.page = 1
				this.baozhengjinSelect()
			},
			// 重置
			cleansRecharge() {
				this.phoneT = ''
				this.userNameT= ''
				this.page = 1
				this.baozhengjinSelect()
			},
			animeDatB(){
				this.page = 1
				this.baozhengjinSelect()
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style lang="scss" scoped>
.finance-list-page {
	padding: 0;
	overflow-x: hidden;
}

.finance-list-page__hero {
	margin-bottom: 12px;
}

.finance-list-page__filter {
	margin-bottom: 12px;
}

.finance-list-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.finance-list-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.finance-list-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.finance-list-page__input {
	width: 150px;
}

.finance-list-page__input--wide {
	width: 200px;
}

.finance-list-page__select {
	width: 150px;
}

.finance-list-page__date {
	width: 160px;
}

.finance-list-page__date--wide {
	width: 200px;
}

.finance-list-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.finance-list-page__tabs-wrap {
	margin-bottom: 0;
}

.finance-list-page__tabs {
	margin-bottom: 0;
}

.finance-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.finance-list-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.finance-list-page__table-card >>> .el-table {
	border-radius: 0;
}

.finance-list-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	justify-content: space-between;
	gap: 12px;
	padding: 12px 16px;
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.finance-list-page__tip {
	margin: 0;
	font-size: 12px;
	color: #e6a23c;
	flex: 1;
	min-width: 200px;
}

.finance-list-page__toolbar-actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
	flex-shrink: 0;
}

.finance-list-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.finance-list-page__table >>> .el-table__body td {
	background: #fff;
}

.finance-list-page__link {
	color: #01af74;
	cursor: pointer;
}

.finance-list-page__link:hover {
	color: #017c52;
}

.finance-list-page__qr-wrap {
	display: flex;
	flex-wrap: wrap;
}

.finance-list-page__thumb {
	width: 50px;
	height: 50px;
	border-radius: 6px;
	object-fit: cover;
}

.finance-list-page__preview {
	width: 200px;
	height: 200px;
	border-radius: 6px;
	object-fit: cover;
}

.finance-list-page__amount--minus {
	color: #f56c6c;
}

.finance-list-page__amount--plus {
	color: #017c52;
}

.finance-list-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.finance-list-page__kpi-row {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 12px;
	margin-bottom: 0;
}

.finance-list-page__kpi {
	display: flex;
	align-items: center;
	gap: 12px;
	padding: 16px;
	background: var(--nf-card-bg, #fff);
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-md, 8px);
	box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
}

.finance-list-page__kpi-icon {
	flex-shrink: 0;
	width: 44px;
	height: 44px;
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: var(--nf-radius-sm, 6px);
	background: var(--nf-brand-lite, #e1f5ee);
	color: var(--nf-brand, #01af74);
	font-size: 20px;
}

.finance-list-page__kpi-body {
	min-width: 0;
}

.finance-list-page__kpi-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
	margin-bottom: 4px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.finance-list-page__kpi-value {
	font-size: 13px;
	color: var(--nf-text-primary, #1a2433);
}

.finance-list-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: var(--nf-brand, #01af74);
	margin-right: 4px;
}

@media (max-width: 960px) {
	.finance-list-page__kpi-row {
		grid-template-columns: 1fr;
	}
}
</style>
