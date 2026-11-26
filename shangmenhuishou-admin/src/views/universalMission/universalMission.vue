<template>
	<div class="universal-mission-page">
		<div class="nf-page-hero universal-mission-page__hero">
			<span class="nf-page-hero__badge">任务管理</span>
			<h2 class="nf-page-hero__title">派单列表</h2>
			<p class="nf-page-hero__desc">管理派单订单，支持按状态、服务类型、分类、手机号、内容与订单号筛选，可进行派单、补费、退款等操作</p>
		</div>

		<div class="nf-filter-card universal-mission-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body universal-mission-page__filter-body">
				<div class="universal-mission-page__field">
					<span class="universal-mission-page__label">状态：</span>
					<el-select v-model="status1" class="universal-mission-page__select" @change="animeDat()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="universal-mission-page__field">
					<span class="universal-mission-page__label">服务类型：</span>
					<el-select v-model="taskTypeT" class="universal-mission-page__select" @change="animeDat()">
						<el-option v-for="item in parentIdList" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="universal-mission-page__field">
					<span class="universal-mission-page__label">服务分类：</span>
					<el-cascader class="universal-mission-page__cascader" v-model="gameIdsVal" :options="homeData1"
						placeholder="请选择服务分类" :show-all-levels="false"
						:props="{ expandTrigger: 'hover', value:'classifyId', label:'classifyName'}" @change="handleChange2"
						filterable>
					</el-cascader>
				</div>
				<div class="universal-mission-page__field">
					<span class="universal-mission-page__label">手机号：</span>
					<el-input class="universal-mission-page__input" @keydown.enter.native="select1" placeholder="请输入手机号"
						v-model="phone1">
					</el-input>
				</div>
				<div class="universal-mission-page__field">
					<span class="universal-mission-page__label">内容：</span>
					<el-input class="universal-mission-page__input" @keydown.enter.native="select1" placeholder="请输入内容"
						v-model="content1"></el-input>
				</div>
				<div class="universal-mission-page__field">
					<span class="universal-mission-page__label">订单号：</span>
					<el-input class="universal-mission-page__input" @keydown.enter.native="phoneSelect" placeholder="请输入订单号"
						v-model="orderNoT"></el-input>
				</div>
				<div class="universal-mission-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="select1">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button :disabled="!isAuth('universalMission:add')" size="mini" type="primary" icon="el-icon-plus"
						@click="classifyStair(1)">添加</el-button>
				</div>
			</div>
		</div>

		<div class="universal-mission-page__tabs-wrap">
			<el-tabs v-model="activeName" class="universal-mission-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="派单管理" name="first">
					<div class="nf-table-card universal-mission-page__table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list" class="universal-mission-page__table">
				<el-table-column fixed prop="id" label="编号"></el-table-column>
				<el-table-column fixed prop="userName" label="发布用户">
					<template slot-scope="scope">
						<span class="universal-mission-page__link" @click="updates(scope.row.userId)">
							{{ scope.row.userName ? scope.row.userName : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column label="发布用户图像" width="120">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column fixed prop="helpTakeUserName" label="接单用户" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.helpTake ">
							<span v-if="scope.row.helpTake.user" class="universal-mission-page__link"
								@click="updates(scope.row.helpTake.user.userId)">
								{{ scope.row.helpTake.user? scope.row.helpTake.user.userName : '未绑定' }}
							</span>
						</span>
						<span v-else> - </span>
					</template>
				</el-table-column>
				<el-table-column prop="taskType" label="服务类型">
					<template slot-scope="scope">
						<span v-if="scope.row.taskType == 1">悬赏任务</span>
						<span v-if="scope.row.taskType == 2">报价任务</span>
						<span v-if="scope.row.taskType == 3">一口价任务</span>
						<span v-if="scope.row.taskType == 4">师傅任务</span>
					</template>
				</el-table-column>
				<el-table-column prop="serviceName" label="服务分类"></el-table-column>
				<el-table-column prop="content" label="内容" width="200"></el-table-column>
				<el-table-column prop="image" label="服务要求图片" width="180">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.image">
							<div v-for="(item,index) in scope.row.image.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px;height:auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="orderNo" label="订单号" width="200"></el-table-column>
				<el-table-column prop="name" label="服务人姓名"></el-table-column>
				<el-table-column prop="phone" label="服务人电话"></el-table-column>
				<el-table-column prop="money" label="发布价格">
					<template slot-scope="scope">
						<span class="universal-mission-page__link"
							v-if="scope.row.paySumMoney&&scope.row.paySumMoney!=0">
							{{scope.row.paySumMoney}}
						</span>
						<span class="universal-mission-page__link" v-else>
							{{ scope.row.money ? scope.row.money : '0' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="commission" label="接单价格"></el-table-column>
				<el-table-column prop="helpTake.zhiMoney" label="一级佣金">
					<template slot-scope="scope">
						<span class="universal-mission-page__link" v-if="scope.row.helpTake"
							@click="updates(scope.row.helpTake.zhiUserId)">
							{{ scope.row.helpTake.zhiMoney ? scope.row.helpTake.zhiMoney : '0' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="helpTake.feiMoney" label="二级佣金">
					<template slot-scope="scope">
						<span class="universal-mission-page__link" v-if="scope.row.helpTake"
							@click="updates(scope.row.helpTake.feiUserId)">
							{{ scope.row.helpTake.feiMoney ? scope.row.helpTake.feiMoney : '0' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="helpTake.pingMoney" label="平台佣金">
					<template slot-scope="scope">
						<span class="universal-mission-page__link" v-if="scope.row.helpTake">
							{{ scope.row.helpTake.pingMoney ? scope.row.helpTake.pingMoney : '0' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="refundSumMoney" label="退款金额">
					<template slot-scope="scope">
						<span class="universal-mission-page__link">
							{{ scope.row.refundSumMoney ? scope.row.refundSumMoney : '0' }}
						</span>
					</template>
				</el-table-column>

				<el-table-column prop="maxPeople" label="报价人数"></el-table-column>
				<el-table-column prop="refundContent" label="退费备注" width="200">
					<template slot-scope="scope">
						<el-popover placement="top-start" title="" trigger="hover" v-if="scope.row.refundContent">
							<div v-for="(item,index) in scope.row.refundContent.split(',')" :key="index"
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
								slot="reference">
								{{item}}
							</div>
							<div style="width: 400px;height: auto;word-break: break-all;max-height: 500px;overflow: auto"
								v-for="(item,index) in scope.row.refundContent.split(',')" :key="index">
								{{item}}
							</div>
						</el-popover>
					</template>
				</el-table-column>
				<el-table-column prop="feeContent" label="补费备注" width="200">
					<template slot-scope="scope">
						<el-popover placement="top-start" title="" trigger="hover" v-if="scope.row.feeContent">
							<div v-for="(item,index) in scope.row.feeContent.split(',')" :key="index"
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
								slot="reference">
								{{item}}
							</div>
							<div style="width: 400px;height: auto;word-break: break-all;max-height: 500px;overflow: auto"
								v-for="(item,index) in scope.row.feeContent.split(',')" :key="index">
								{{item}}
							</div>
						</el-popover>
					</template>
				</el-table-column>
				<el-table-column prop="code" label="确认码"></el-table-column>
				<el-table-column prop="province" label="省"></el-table-column>
				<el-table-column prop="city" label="市"></el-table-column>
				<el-table-column prop="district" label="区"></el-table-column>
				<el-table-column prop="detailsAddress" label="服务人详细地址" width="160"></el-table-column>
				<el-table-column prop="longitude" label="经度" width="160"></el-table-column>
				<el-table-column prop="latitude" label="纬度" width="160"></el-table-column>

				<el-table-column prop="helpTake.startTaskAddress" label="服务开始地址"></el-table-column>
				<el-table-column prop="helpTake.endTaskAddress" label="服务结束地址"></el-table-column>
				<el-table-column prop="helpTake.startTime" label="服务开始时间"></el-table-column>
				<el-table-column prop="helpTake.endTime" label="服务结束时间"></el-table-column>


				<el-table-column prop="helpTake.startTaskImg" label="服务开始前图片" width="180">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;"
							v-if="scope.row.helpTake&&scope.row.helpTake.startTaskImg">
							<div v-for="(item,index) in scope.row.helpTake.startTaskImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px;height:auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="helpTake.endTaskImg" label="服务结束后图片" width="180">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;"
							v-if="scope.row.helpTake&&scope.row.helpTake.endTaskImg">
							<div v-for="(item,index) in scope.row.helpTake.endTaskImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px;height:auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>

				<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
				<el-table-column prop="deliveryTime" label="预约时间" fixed="right" width="80">
					<template slot-scope="scope">
						<span class="universal-mission-page__time">{{scope.row.deliveryTime}}</span>
						<el-button size="mini" class="universal-mission-page__text-btn"
							@click="rechargenone(scope.row,2)">修改</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="remarks" label="订单备注" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.remarks ? scope.row.remarks : '未填写'}}</span>
						<el-button size="mini" class="universal-mission-page__text-btn"
							@click="rechargenone(scope.row,1)">修改</el-button>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="status" label="状态" width="100">
					<template slot-scope="scope">
						<span class="universal-mission-page__status--muted" v-if="scope.row.status === 0 ">待支付</span>
						<span class="universal-mission-page__status--active" v-if="scope.row.status === 1 ">待接单</span>
						<span class="universal-mission-page__status--active" v-if="scope.row.status === 2 ">待服务</span>
						<span class="universal-mission-page__status--active" v-if="scope.row.status === 3 ">进行中</span>
						<span class="universal-mission-page__status--active" v-if="scope.row.status === 4 ">已完成</span>
						<span v-if="scope.row.status === 5 ">已取消</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="200">
					<template slot-scope="scope">
						<!-- <el-button v-if="scope.row.status === 2 || scope.row.status === 3" size="mini" type="primary"
							:disabled="!isAuth('universalMission:sold')" @click="soldClick(scope.row)">下架
						</el-button>
						<el-button v-else size="mini" type="primary" disabled>下架</el-button> -->
						<el-button size="mini" type="primary" @click="bufeiBtn(scope.row)" style="margin: 3px;">补费列表
						</el-button>
						<el-button size="mini" type="warning"
							:disabled="!isAuth('universalMission:update') ||scope.row.status === 5 ||scope.row.status === 4"
							@click="classifyQx(scope.row)" style="margin: 3px;">取消派单
						</el-button>
						<!-- <el-button size="mini" type="primary" :disabled="!isAuth('universalMission:update')"
							@click="classifyStair(2, scope.row)" style="margin: 3px;">编辑
						</el-button> -->

						<el-button size="mini" type="primary"
							:disabled="!isAuth('universalMission:update')||scope.row.status === 5||scope.row.status === 4||scope.row.status === 1 "
							@click="classifySd(scope.row)" style="margin: 3px;">确认完成
						</el-button>


						<el-button size="mini" type="warning" plain v-if="scope.row.taskType == 2"
							@click="classifyBj(scope.row)" style="margin: 3px;">报价列表
						</el-button>
						<el-button size="mini" type="primary" style="margin: 3px;" @click="plCompile(scope.row)">评论列表
						</el-button>
						<el-button size="mini" type="warning" plain
							v-if="scope.row.status != 0&&scope.row.status != 4&&scope.row.status != 5&&scope.row.taskType != 2"
							:disabled="!isAuth('universalMission:update')" @click="zhuandan(1,scope.row)"
							style="margin: 3px;">派单</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('universalMission:delete')"
							@click="deleteStair(scope.row)" style="margin: 3px;">删除
						</el-button>

						<div v-if="scope.row.status==3||scope.row.status == 4">
							<el-button style="margin:5px;" size="mini" type="warning" icon="document"
								@click="tuikuanBtnBf(scope.row)" v-if="scope.row.refundSumMoney<scope.row.moneys">部分退款
							</el-button>
							<el-button style="margin:5px;" size="mini" type="warning" icon="document"
								:disabled="!isAuth('orderCenter:tuikuan')" @click="tuikuanBtn(scope.row)"
								v-if="scope.row.refundSumMoney<scope.row.moneys">退全款
							</el-button>
							<el-button style="margin:5px;" v-if="scope.row.refundSumMoney==scope.row.moneys" disabled
								size="mini" type="warning">金额已退完</el-button>
						</div>

					</template>
				</el-table-column>
			</el-table>
			<div class="universal-mission-page__stats">
				<span class="universal-mission-page__stat-item">本页累计总金额统计：<em>{{totalMoney.toFixed(2)}}</em>元</span>
				<span class="universal-mission-page__stat-item">一级佣金统计：<em>{{totalMoneyS.toFixed(2)}}</em>元</span>
				<span class="universal-mission-page__stat-item">二级佣金统计：<em>{{totalMoneyTy.toFixed(2)}}</em>元</span>
				<span class="universal-mission-page__stat-item">平台佣金统计：<em>{{totalMoneyP.toFixed(2)}}</em>元</span>
			</div>
			<div class="universal-mission-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
				</el-pagination>
			</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 服务拒绝弹框 -->
		<el-dialog title="服务拒绝" :visible.sync="dialogFormVisible7" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入拒绝理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible7 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">发布人：</span>
				<el-input v-model="nickName1" @focus="userselect" style="width:50%;" placeholder="请选择发布人">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务类型：</span>
				<el-select v-model="parentId" style="width:50%;">
					<el-option v-for="item in parentIdList" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin-bottom: 10px;" v-if="parentId==4">
				<span style="width: 200px;display: inline-block;text-align: right;">选择师傅：</span>
				<el-input style="width:50%;" v-model="masterUserName" type="text" placeholder="请选择师傅"
					@focus="zhuandan1(2)">
				</el-input>

			</div>
			<div style="margin-bottom: 10px;" v-if="parentId==4">
				<span style="width: 200px;display: inline-block;text-align: right;">师傅服务：</span>
				<el-select v-model="fixedClassifyId" style="width:50%;" @change="handleChange3">
					<el-option v-for="item in homeData1" :key="item.value" :label="item.classifyName"
						:value="item.classifyId">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;" v-if="parentId!=4">
				<span style="width: 200px;display: inline-block;text-align: right;">服务分类：</span>
				<!-- <el-select v-model="serviceName" style="width:50%;">
					<el-option v-for="item in homeData1" :key="item.value" :label="item.gameName"
						:value="item.id">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp; -->
				<el-cascader style="width:50%;" v-model="fixedClassifyIds" :options="homeData1"
					:placeholder="fixedClassifyIdTitle" :show-all-levels="false"
					:props="{ expandTrigger: 'hover', value:'classifyId', label:'classifyName'}" @change="handleChange1"
					filterable>
				</el-cascader>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">上门时间：</span>
				<!-- <el-input style="width:50%;" v-model="deliveryTime" type="text" placeholder="请选择上门时间"></el-input> -->
				<el-date-picker style="width:50%;" v-model="deliveryTime" type="datetime" format="yyyy-MM-dd HH:mm"
					value-format="yyyy-MM-dd HH:mm" placeholder="选择上门时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">具体要求：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="2" placeholder="请输入具体要求">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">要求图片：</span>
				<div class="imgs" v-for="(item,index) in image" :key="index">
					<img width="100%" class="images" height="100%" :src="item" alt="">
					<span class="dels">
						<i class="el-icon-delete" @click="clear(index)"></i>
					</span>
				</div>

				<div class="imgs" style="width: 50%;">
					<el-upload :action="$http.adornUrlUp('file/upload')" list-type="picture-card"
						:show-file-list="false" :on-success="handleUploadSuccess" :on-progress="onprogress1">
						<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
							:percentage="percentage1"></el-progress>
						<i v-else class="el-icon-plus"></i>
					</el-upload>

				</div>
			</div>

			<div style="margin-bottom: 10px;" v-if="parentId != 2"> <!-- v-if="titles == '添加'" -->
				<span style="width: 200px;display: inline-block;text-align: right;">发布价格：</span>
				<el-input style="width:50%;" v-model="commission" type="text" placeholder="请输入发布价格"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="parentId == 2"> <!-- v-if="titles == '添加'" -->
				<span style="width: 200px;display: inline-block;text-align: right;">期望价格：</span>
				<el-input style="width:50%;" v-model="commission" type="text" placeholder="请输入期望价格"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;" v-if="titles == '修改'">
				<span style="width: 200px;display: inline-block;text-align: right;">发布价格：</span>
				<el-input style="width:50%;" v-model="money" type="text" placeholder="请输入发布价格"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="titles == '修改'">
				<span style="width: 200px;display: inline-block;text-align: right;">接单价格：</span>
				<el-input style="width:50%;" v-model="commission" type="text" disabled placeholder="请输入接单价格"></el-input>
			</div> -->
			<div style="margin-bottom: 10px;" v-if="parentId==2">
				<span style="width: 200px;display: inline-block;text-align: right;">报价人数：</span>
				<el-input style="width:50%;" v-model="maxPeople" type="text" placeholder="请输入报价人数"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
				<el-input style="width:50%;" v-model="name" type="text" placeholder="请输入姓名"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
				<el-input style="width:50%;" v-model="phoneA" type="text" placeholder="请输入手机号"></el-input>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务地区：</span>
				<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" v-model="storeAddress"
					filterable :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
				</el-cascader>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">详细地址：</span>
				<el-input v-model="detailsAddress" @keydown.enter.native="select" style="width:50%;"
					placeholder="请输入详细地址">
				</el-input>
				<el-button type="primary" plain size="mini" @click="select()">获取经纬度</el-button>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
				<el-input v-model="latitude" style="width:50%;" placeholder="请输入纬度"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
				<el-input v-model="longitude" style="width:50%;" placeholder="请输入经度"></el-input>
			</div>
			<div>
				<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="70%">
			<div style="margin:2% 0;display: inline-block;">
				<span>手机号</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phoneU"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				<span>昵称</span>
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list">
				<el-table-column fixed prop="nickName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" fixed='right'>
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<div style="margin-bottom: 10px;" v-if="types==1">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="conters" type="textarea" :rows="4" placeholder="请输入备注"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="types==2">
				<span style="width: 200px;display: inline-block;text-align: right;">预约时间：</span>

				<el-date-picker v-model="conters" type="datetime" placeholder="选择日期时间" format="yyyy-MM-dd HH:mm"
					value-format="yyyy-MM-dd HH:mm">
				</el-date-picker>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 报价列表  localhost:8180/sqx_fast/admin/HelpBidding/getBiddingListByOrderId?id=230-->
		<el-dialog title="报价列表" :visible.sync="dialogFormVisibleBj" center>
			<el-table width="780px" v-loading="tableDataLoading4" :data="baojiaList">
				<el-table-column fixed prop="biddingId" width="80" label="编号">
				</el-table-column>
				<el-table-column prop="nickName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;cursor: pointer;"
							@click="updates(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="offerMoney" label="报价金额">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>

			</el-table>
		</el-dialog>
		<!-- 评论列表 -->
		<el-dialog title="评论列表" :visible.sync="dialogFormVisible10" center>
			<el-table v-loading="tableDataLoadingPl" :data="pinglunData.records">
				<el-table-column prop="evaluateId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="评论用户" width="120">
					<template slot-scope="scope">
						　<span style="color: #01af74;cursor: pointer;" @click="updatesvideo(scope.row)"
							v-text="scope.row.userName"></span>
					</template>
				</el-table-column>
				<el-table-column prop="evaluateMessage" label="评论内容">
				</el-table-column>
				<el-table-column prop="evaluateImg" label="评论图片" width="200">
					<template slot-scope="scope">
						<div v-if="scope.row.evaluateImg">
							<span v-for="(item,index) in scope.row.evaluateImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="item" alt="">
								</el-popover>
							</span>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="satisfactionFlag" label="满意度" width="80">
					<template slot-scope="scope">
						<span style="color: red;cursor: pointer;" v-if="scope.row.satisfactionFlag==1">非常差</span>
						<span style="color: orange;cursor: pointer;" v-if="scope.row.satisfactionFlag==2">差</span>
						<span style="color: #999;cursor: pointer;" v-if="scope.row.satisfactionFlag==3">一般</span>
						<span class="universal-mission-page__link" v-if="scope.row.satisfactionFlag==4">满意</span>
						<span class="universal-mission-page__link" v-if="scope.row.satisfactionFlag==5">非常满意</span>
					</template>
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
				<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
					:page-sizes="[10, 20, 30, 40]" :page-size="size2" :current-page="page2"
					layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.total">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 补费列表 -->
		<el-dialog title="补费列表" :visible.sync="dialogFormVisibleBf" center width="80%">
			<el-table v-loading="tableDataLoading2" :data="userData">
				<el-table-column prop="recordId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						　<span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.userId)"
							v-text="scope.row.userName"></span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="补费金额">
				</el-table-column>
				<el-table-column prop="money" label="实际到账金额">
				</el-table-column>
				<el-table-column prop="refundMoney" label="退款金额">
					<template slot-scope="scope">
						<span style="color: #01af74;">
							{{ scope.row.refundMoney ? scope.row.refundMoney : '0' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="rideName" label="接收用户">
					<template slot-scope="scope">
						　<span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.rideId)"
							v-text="scope.row.rideName"></span>
					</template>
				</el-table-column>
				<el-table-column prop="payWay" label="支付方式">
					<template slot-scope="scope">
						<span v-if="scope.row.payWay==0">余额</span>
						<span v-if="scope.row.payWay==1">微信</span>
						<span v-if="scope.row.payWay==2">支付宝</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1">已支付</span>
						<span v-if="scope.row.status==2">已退回</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
			</el-table>
		</el-dialog>
		<!-- 师傅列表 -->
		<el-dialog title="师傅列表" :visible.sync="dialogFormVisibleSf" center width="80%">
			<div style="margin:5px;display: inline-block;">
				<span>手机号:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入手机号"
					v-model="phoneU"></el-input>
			</div>&emsp;&emsp;
			<div style="margin:5px;display: inline-block;">
				<span>昵称:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入昵称"
					v-model="campus"></el-input>
			</div>&emsp;&emsp;
			<div style="margin:5px;display: inline-block;">
				<span>商家名称:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入商家名称"
					v-model="sexU"></el-input>
			</div>&emsp;&emsp;
			<div style="margin:5px;display: inline-block;">
				<span>邀请码:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入邀请码"
					v-model="invitationCode"></el-input>
			</div>&emsp;&emsp;
			<div style="margin:5px;display: inline-block;">
				<span>邀请人邀请码:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入邀请人邀请码"
					v-model="inviterCode"></el-input>
			</div>&emsp;&emsp;

			<div style="display: inline-block;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="selectU">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansU">重置
				</el-button>
			</div>
			<el-tabs v-model="activeNameU" @tab-click="handleClickU">
				<el-tab-pane label="商家用户" name="first">
					<el-table v-loading="tableDataLoadingU" :data="userListData.list">
						<el-table-column prop="userId" label="编号" width="80" fixed="left"></el-table-column>
						<el-table-column fixed prop="userName" label="昵称" width="150">
							<template slot-scope="scope">
								<span
									style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
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
								<span class="universal-mission-page__link" @click="updates(scope.row)">
									{{ scope.row.phone ? scope.row.phone : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="用户类型">
							<template slot-scope="scope">
								<span v-if=" scope.row.isAuthentication==1">个人认证</span>
								<span v-else-if=" scope.row.isAuthentication==2">企业认证</span>
								<span v-else-if=" scope.row.isAuthentication==3">个人企业认证</span>
								<span v-else>普通用户</span>
							</template>
						</el-table-column>
						<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
						<el-table-column label="邀请人邀请码">
							<template slot-scope="scope">
								<span
									style="color: #01af74;cursor: pointer;">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiFuBaoName" label="是否实名">
							<template slot-scope="scope">
								<span v-if="scope.row.isAuthentication==1">是</span>
								<span v-else>否</span>
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
						</el-table-column>
						<el-table-column prop="rate" label="商家佣金比例" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="feiRate" label="二级佣金比例" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
						<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="userBtns(scope.row)">确认
								</el-button>

							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeU" @current-change="handleCurrentChangeU"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit1" :current-page="page1"
							layout="total,sizes, prev, pager, next,jumper" :total="userListData.totalCount">
						</el-pagination>
					</div>
				</el-tab-pane>
				<el-tab-pane label="师傅用户" name="two">
					<el-table v-loading="tableDataLoadingU" :data="userListData.list">
						<el-table-column prop="userId" label="编号" width="80" fixed="left"></el-table-column>
						<el-table-column fixed prop="userName" label="昵称" width="150">
							<template slot-scope="scope">
								<span
									style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
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
								<span class="universal-mission-page__link" @click="updates(scope.row)">
									{{ scope.row.phone ? scope.row.phone : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="用户类型">
							<template slot-scope="scope">
								<span v-if=" scope.row.isAuthentication==1">个人认证</span>
								<span v-else-if=" scope.row.isAuthentication==2">企业认证</span>
								<span v-else-if=" scope.row.isAuthentication==3">个人企业认证</span>
								<span v-else>普通用户</span>
							</template>
						</el-table-column>
						<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
						<el-table-column label="邀请人邀请码">
							<template slot-scope="scope">
								<span
									style="color: #01af74;cursor: pointer;">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiFuBaoName" label="是否实名">
							<template slot-scope="scope">
								<span v-if="scope.row.isAuthentication==1">是</span>
								<span v-else>否</span>
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
						</el-table-column>
						<el-table-column prop="rate" label="商家佣金比例" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="feiRate" label="二级佣金比例" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
						<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="userBtns(scope.row)">确认
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeU" @current-change="handleCurrentChangeU"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit1" :current-page="page1"
							layout="total,sizes, prev, pager, next,jumper" :total="userListData.totalCount">
						</el-pagination>
					</div>
				</el-tab-pane>
				<el-tab-pane label="员工用户" name="three">
					<el-table v-loading="tableDataLoadingU" :data="userListData.list">
						<el-table-column prop="userId" label="编号" width="80" fixed="left"></el-table-column>
						<el-table-column fixed prop="userName" label="昵称" width="150">
							<template slot-scope="scope">
								<span
									style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
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
								<span class="universal-mission-page__link" @click="updates(scope.row)">
									{{ scope.row.phone ? scope.row.phone : '未绑定' }}
								</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="用户类型">
							<template slot-scope="scope">
								<span v-if=" scope.row.isAuthentication==1">个人认证</span>
								<span v-else-if=" scope.row.isAuthentication==2">企业认证</span>
								<span v-else-if=" scope.row.isAuthentication==3">个人企业认证</span>
								<span v-else>普通用户</span>
							</template>
						</el-table-column>
						<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
						<el-table-column label="邀请人邀请码">
							<template slot-scope="scope">
								<span
									style="color: #01af74;cursor: pointer;">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiFuBaoName" label="是否实名">
							<template slot-scope="scope">
								<span v-if="scope.row.isAuthentication==1">是</span>
								<span v-else>否</span>
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
						</el-table-column>
						<el-table-column prop="rate" label="商家佣金比例" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="feiRate" label="二级佣金比例" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
						<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="userBtns(scope.row)">确认
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChangeU" @current-change="handleCurrentChangeU"
							:page-sizes="[10, 20, 30, 40]" :page-size="limit1" :current-page="page1"
							layout="total,sizes, prev, pager, next,jumper" :total="userListData.totalCount">
						</el-pagination>
					</div>
				</el-tab-pane>
			</el-tabs>
		</el-dialog>
		<!-- 部分退款 -->
		<el-dialog title="部分退款" :visible.sync="dialogFormVisibleBftk" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">订单总金额：</span>
				<span style="color: brown;"> ¥ {{zongjine}}</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">退款金额：</span>
				<el-input style="width:50%;" v-model="content" type="number" placeholder="请输入退款金额">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleBftk = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTk()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 师傅列表 -->
		<el-dialog title="师傅列表" :visible.sync="dialogFormVisibleSf1" center width="80%">
			<div style="margin:5px;display: inline-block;">
				<span>手机号:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入手机号"
					v-model="phoneU"></el-input>
			</div>&emsp;&emsp;
			<div style="margin:5px;display: inline-block;">
				<span>姓名:</span>
				<el-input style="width: 150px;" @keydown.enter.native="selectU" clearable placeholder="请输入姓名"
					v-model="campus"></el-input>
			</div>&emsp;&emsp;

			<div style="display: inline-block;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="selectU">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansU">重置
				</el-button>
			</div>
			<el-table v-loading="tableDataLoadingU" :data="userListData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="name" label="姓名" width="100">
					<template slot-scope="scope">
						<span class="universal-mission-page__link"
							@click="updates(scope.row)">{{scope.row.name ? scope.row.name : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="isRecommend" label="推荐" width="100">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.isRecommend" @change="change2(scope.row.isRecommend,scope.row)"
							:active-value="openValue1" :disabled="!isAuth('shopsList:update')"
							:inactive-value="closeValue1" active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="avatar" label="头像" width="80">
					<template slot-scope="scope">
						<div v-if="scope.row.avatar" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
							</el-popover>
						</div>
						<div v-else>
							暂无图片
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="电话" width="150">
				</el-table-column>
				<el-table-column prop="idNumber" label="身份证号码" width="200">
				</el-table-column>
				<el-table-column prop="cardFront" label="身份证正面" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.front == null || scope.row.front == ''">
							暂无图片
						</div>
						<div v-if="scope.row.front" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.front" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.front" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="cardBack" label="身份证反面" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.back == null || scope.row.back == ''">
							暂无图片
						</div>
						<div v-if="scope.row.back" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.back" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.back" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column label="标签" prop="label">
				</el-table-column>
				<el-table-column label="年龄" prop="age">
				</el-table-column>
				<el-table-column label="个人介绍" prop="details" width="300">
				</el-table-column>

				<el-table-column label="服务次数" prop="serviceNum">
				</el-table-column>
				<el-table-column label="评分" prop="score">
				</el-table-column>
				<el-table-column label="地址" prop="nowAddress" width="200">
				</el-table-column>
				<el-table-column label="创建时间" prop="createTime" width="160">
				</el-table-column>
				<el-table-column prop="onLine" fixed="right" label="接单状态">
					<template slot-scope="scope">
						<span v-if="scope.row.onLine==1" style="color: #13ce66;">上线</span>
						<span v-else style="color: #ff4949;">下线</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="userBtns2(scope.row)">确认
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeU" @current-change="handleCurrentChangeU"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userListData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../locality/quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				dialogVisible: false,
				totalMoney: 0,
				limit: 10,
				page: 1,
				size1: 10,
				page1: 1,
				size2: 10,
				page2: 1,
				phone1: '',
				status1: '',
				describes: '',
				taskNum: '',
				taskMoney: '',
				statesnum: [{
						label: '全部',
						value: ''
					},
					{
						label: '待支付',
						value: 0
					},
					{
						label: '待接单',
						value: 1
					},
					{
						label: '待服务',
						value: 2
					},
					{
						label: '进行中',
						value: 3
					},
					{
						label: '已完成',
						value: 4
					},
					{
						label: '已取消',
						value: 5
					},
				],
				taskstates: [{
						label: '全部',
						value: 0
					},
					{
						label: '已接单',
						value: 1
					},
					{
						label: '已送达',
						value: 2
					}
				],
				sort: 10,
				id: '',
				btnChangeEnable: true,
				helpTakeId: '',
				helpMaintainId: '',
				content1: '',
				classifyIcon: '',
				classifyId: '',
				classifyUrl: '',
				classifyName: '',
				classifyDeatilsName: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading4: false,
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible7: false,
				tableData: [],
				tableData2: [],
				form: {
					id: '',
					classifyName: '',
					classifyUrl: '',
					classifyIcon: '',
					describes: ''
				},
				form2: {
					id: '',
					classifyDeatilsName: '',
					classifyUrl: '',
					classifyIcon: '',
					taskNum: '',
					taskMoney: '',
				},
				classifyData: {},
				checkBoxData: [], //多选框选择的值
				checkBoxData2: [], //多选框选择的值
				quillOption: quillConfig,
				options: regionData,
				storeAddress: [],
				storeAddre: '请选择城市',
				deliveryTime: '', //上门时间
				content: '', //服务的具体要求
				commission: '', //发布价格
				money: '',
				maxPeople: '',
				name: '', //地址姓名
				phone: '', //手机号
				phoneA: '',
				phone1: '',
				detailsAddress: '', //地址详细
				latitude: '', //精度
				longitude: '', //维度
				province: '', //省
				city: '', //市
				district: '', //区
				serviceName: '',
				fixedClassifyId: '', //服务类型
				fixedClassifyIds: [],
				fixedClassifyIdTitle: '请选择服务分类',
				userIdss: '', //用户id
				nickName1: '',
				phoneU: '',
				phone1: '',
				nickName2: '',
				userIds: {}, //用户列表
				titles: '添加',
				homeData1: [],
				gameId: '',
				gameIdT: [],
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
				parentId: '',
				parentIdList: [{
						label: '悬赏任务',
						value: 1
					},
					{
						label: '报价任务',
						value: 2
					},
					{
						label: '一口价任务',
						value: 3
					},
					{
						label: '师傅任务',
						value: 4
					}
				],
				gameIds: '',
				gameIdsVal: [],
				taskTypeT: '',
				orderNoT: '',
				dialogFormVisible1: false,
				orderId: '',
				conters: '',
				types: 1,
				dialogFormVisibleBj: false,
				baojiaList: [],
				pinglunData: {},
				tableDataLoadingPl: false,
				renwuId: '',
				dialogFormVisible10: false,
				image: [],
				percentage: 0, //进度条
				percentage2: 0, //进度条
				dialogFormVisibleBf: false,
				tableDataLoading2: false,
				userData: [],

				totalMoney: 0,
				totalMoneyS: 0,
				totalMoneyTj: 0,
				totalMoneyTy: 0,
				totalMoneyP: 0,

				percentage1: 0,
				dialogFormVisibleSf: false,
				dialogFormVisibleSf1: false,
				tableDataLoadingU: false,
				userListData: {},
				sexU: '',
				sexNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '男'
					},
					{
						value: 2,
						label: '女'
					},
				],
				cityU: '',
				campus: '',
				phoneU: '',
				limit1: 10,
				page1: 1,
				activeNameU: 'first',
				isAuthentication: 2,
				isCompany: '',
				isVip: '',
				shopType: 2,
				invitationCode: '',
				inviterCode: '',
				ordersId: '',
				// 不分退款新增
				zongjine: 0,
				dacefei: 0,
				xiangmujin: 0,
				dialogFormVisibleBftk: false,
				masterUserId: '',
				masterUserName: '',
			}
		},
		methods: {
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
			// 详情跳转
			updates(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}
			},
			// 图标上传一级分类
			handleAvatarSuccess(file) {
				this.classifyIcon = file.data.path
			},
			// 图标上传一级分类编辑
			handleAvatarSuccess2(file) {
				this.form.classifyIcon = file.data.path
			},
			handleSizeChangeU(val) {
				this.limit1 = val;
				this.dataSelectUser()()
			},
			handleCurrentChangeU(val) {
				this.page1 = val;
				this.dataSelectUser()
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 多选
			changeFun2(val) {
				this.checkBoxData2 = val
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleSizeChange3(val) {
				this.size2 = val
				this.pinglunSelect(this.renwuId)
			},
			handleCurrentChange3(val) {
				this.page2 = val
				this.pinglunSelect(this.renwuId)
			},
			// tabs切换
			handleClick(tab, event) {
				this.phone1 = ''
				this.gameId = ''
				this.status1 = ''
				if (tab._props.label == '派单管理') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '接单管理') {
					this.page = 1
					this.limit = 10
					this.taskdataSelect()
				}
			},
			// 下架
			soldClick(row) {
				this.$confirm(`确定要下架此服务?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`help/outHelpOrder/?helpOrderId=${row.id}`),
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
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}

					})
				}).catch(() => {})
			},
			// 添加弹框
			classifyStair(index, row) {
				this.homeSelect()
				if (index == 1) {
					this.titles = '添加'
					this.deliveryTime = ''
					this.content = ''
					this.commission = ''
					this.name = ''
					this.phoneA = ''
					this.detailsAddress = ''
					this.latitude = ''
					this.longitude = ''
					this.province = ''
					this.city = ''
					this.district = ''
					this.fixedClassifyId = ''
					this.fixedClassifyIds = []
					this.fixedClassifyIdTitle = '请选择服务分类'
					this.userIdss = ''
					this.nickName1 = ''
					this.id = ''
					this.parentId = ''
					this.maxPeople = ''
					this.storeAddress = []
					this.image = []
					this.storeAddre = '请选择城市'
					this.masterUserId = ''
					this.masterUserName = ''

				} else {
					this.titles = '修改'
					this.id = row.id
					this.nickName1 = row.userName
					this.userIdss = row.userId
					this.deliveryTime = row.deliveryTime
					this.maxPeople = row.maxPeople
					this.content = row.content
					this.commission = row.commission
					// this.money = row.money
					this.name = row.name
					this.phoneA = row.phone
					this.detailsAddress = row.detailsAddress
					this.latitude = row.latitude
					this.longitude = row.longitude
					this.province = row.province
					this.city = row.city
					this.district = row.district
					this.fixedClassifyId = row.fixedClassifyId
					this.fixedClassifyIds = []
					this.fixedClassifyIds.push(this.fixedClassifyId)
					this.fixedClassifyIdTitle = row.serviceName
					this.parentId = Number(row.taskType)
					if (row.image) {
						this.image = row.image.split(',')
					}
					this.storeAddre = this.province + '/' + this.city + '/' + this.district
					this.masterUserId = row.masterUserId
					if (row.helpTake && row.helpTake.user) {
						this.masterUserName = row.helpTake.user.userName
					} else {
						this.masterUserName = ''
					}




					// masterUserId:'',
					// masterUserName:'',
				}

				this.getMyLocation()
				this.dialogFormVisible = true
			},
			// 添加确定
			StairNoticeTo() {
				if (this.userIdss == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择发布人',
						type: 'warning'
					})
					return
				}
				if (this.deliveryTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择期望送达时间',
						type: 'warning'
					})
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入具体要求',
						type: 'warning'
					})
					return
				}
				if (this.commission == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入发布价格',
						type: 'warning'
					})
					return
				}
				if (this.parentId != 4) {

					if (this.fixedClassifyId == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入服务类型',
							type: 'warning'
						})
						return
					}
				}

				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入姓名',
						type: 'warning'
					})
					return
				}
				if (this.phoneA == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入电话',
						type: 'warning'
					})
					return
				}

				if (this.province == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择省市区',
						type: 'warning'
					})
					return
				}
				if (this.detailsAddress == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入详细地址',
						type: 'warning'
					})
					return
				}
				if (this.image.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传服务图片',
						type: 'warning'
					})
					return
				}

				if (this.parentId == 1) {
					if (this.commission < this.classifyData.minOfferMoney) {

						this.$notify({
							title: '提示',
							duration: 1800,
							message: '发布价格不能低于：' + this.classifyData.minOfferMoney,
							type: 'warning'
						})
						return
					}
				}
				if (this.parentId == 2) {
					if (this.commission < this.classifyData.minQuoteMoney) {

						this.$notify({
							title: '提示',
							duration: 1800,
							message: '期望价格不能低于：' + this.classifyData.minQuoteMoney,
							type: 'warning'
						})
						return
					}
					if (this.maxPeople == '') {

						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入报价人数',
							type: 'warning'
						})
						return
					}
				}
				if (this.parentId == 3) {
					if (this.commission < this.classifyData.money) {

						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入佣金不能低于：' + this.classifyData.money,
							type: 'warning'
						})
						return
					}


				}

				if (this.titles == '添加') {
					var urls = 'help/saveHelpOrder'
				} else {
					var urls = 'help/updateHelpOrder'
				}
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					data: this.$http.adornData({
						'deliveryTime': this.deliveryTime,
						'content': this.content,
						'commission': this.commission,
						'name': this.name,
						'phone': this.phoneA,
						'detailsAddress': this.detailsAddress,
						'latitude': this.latitude,
						'longitude': this.longitude,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'gameId': this.fixedClassifyId,
						'userId': this.userIdss,
						'id': this.id,
						'fixedClassifyId': this.fixedClassifyId,
						'taskType': this.parentId,
						'image': this.image.toString(),
						'maxPeople': this.maxPeople,
						'masterUserId': this.masterUserId,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '添加成功',
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

							}
						})
					}

				})
			},
			// 修改一级分类
			compile(index, rows) {
				this.dialogFormVisible2 = true
				this.form.id = rows.id
				this.form.classifyName = rows.classifyName
				this.form.classifyUrl = rows.classifyUrl
				this.form.classifyIcon = rows.classifyIcon
				this.form.describes = rows.describes
			},
			// 修改一级分类确定
			CompileNoticeTo() {
				if (this.form.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				if (this.form.describes == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类描述',
						type: 'warning'
					})
					return
				}
				if (this.form.classifyIcon == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传分类图标',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('helpClassify/updateHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'classifyName': this.form.classifyName,
						'classifyUrl': this.form.classifyUrl,
						'classifyIcon': this.form.classifyIcon,
						'describes': this.form.describes
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
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
			},
			//删除一级
			deleteStair(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('help/deleteHelpOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'helpOrderId': delid
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
			// 取消派单
			classifyQx(row) {
				let delid = row.id
				this.$confirm(`确定取消派单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('help/outHelpOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'helpOrderId': delid
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '取消成功',
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
			// 确认送达
			classifySd(row) {
				let delid = row.id
				this.$confirm(`确认已经送达?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('help/closeOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'helpOrderId': delid
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
				}).catch(() => {})
			},
			// 手机号搜索
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.phone1 = ''
				this.status1 = ''
				this.startTime = ''
				this.endTime = ''
				this.taskdataSelect()
			},
			// 查询
			select1() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.phone1 = ''
				this.status1 = ''
				this.content1 = ''
				this.gameIds = ''
				this.gameIdsVal = []
				this.taskTypeT = ''
				this.orderNoT = ''

				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			animeDat2(state) {
				this.page = 1
				this.taskdataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.totalMoney = 0
				this.totalMoneyS = 0
				this.totalMoneyTy = 0
				this.totalMoneyP = 0
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('help/selectStatusHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone1,
						'status': this.status1,
						'content': this.content1,
						'gameId': this.gameIds,
						'taskType': this.taskTypeT,
						'orderNo': this.orderNoT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].money) {
							this.totalMoney = this.totalMoney + data.data.list[i].money
						}
						if (data.data.list[i].helpTake && data.data.list[i].helpTake.zhiMoney) {
							this.totalMoneyS = this.totalMoneyS + data.data.list[i].helpTake.zhiMoney
						}
						if (data.data.list[i].helpTake && data.data.list[i].helpTake.feiMoney) {
							this.totalMoneyTy = this.totalMoneyTy + data.data.list[i].helpTake.feiMoney
						}

						if (data.data.list[i].helpTake && data.data.list[i].helpTake.pingMoney) {
							this.totalMoneyP = this.totalMoneyP + data.data.list[i].helpTake.pingMoney
						}
						if (data.data.list[i].paySumMoney) {
							data.data.list[i].moneys = data.data.list[i].paySumMoney;
						} else {
							data.data.list[i].moneys = data.data.list[i].money;
						}
					}

					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 获取接单数据列表
			taskdataSelect() {
				this.totalMoney = 0
				this.tableDataLoading = true
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('help/selectHelpTakeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone1,
						'status': this.status1,
						'gameId': this.gameId,
						'startTime': this.startTime,
						'endTime': endTime,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
					for (var i in this.tableData2.list) {
						this.tableData2.list[i].goodsPriceZ = 0
						this.totalMoney = this.totalMoney + this.tableData2.list[i].money

					}
					this.totalMoney = this.totalMoney.toFixed(2)
				})
			},
			// 获取省市区
			handleChange55(value) {
				console.log('value', value, this.$refs['cascaderAddr'].getCheckedNodes()[0].pathLabels)
				// value = this.$refs['cascaderAddr'].currentLabels
				value = this.$refs['cascaderAddr'].getCheckedNodes()[0].pathLabels
				this.province = value[0]
				this.region = value[1]
				this.district = value[2]
				// this.form.city = value[1]
				if (this.region == '市辖区') {
					this.region = this.province
				}
				// if (this.form.city == '市辖区') {
				// 	this.region = this.province
				// }
				// var regions = this.region
				// regions = regions.slice(0, regions.length - 1)
				// this.region = regions
				// console.log('regions', regions)
				console.log(this.$refs['cascaderAddr'].currentLabels)
			},
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
						that.detailsAddress = response.result.address
						that.city = response.result.address_component.city
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
			select() {
				console.log(this.detailsAddress, this.detailsAddress.replace(/^\s+|\s+$/gm, ''))

				if (this.detailsAddress == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入详细地址',
						type: 'warning'
					});
					return
				} else {
					var add = this.province + this.city + this.district + this.detailsAddress
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
						that.city = response.result.address_components.city;
						// 	.city + response.result.address_components.district + response.result.title
						that.setMap()
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
				}

			},
			// 获取用户列表弹框
			userselect() {
				this.page1 = 1
				this.phoneU = ''
				this.nickName2 = ''
				this.dialogFormVisible3 = true

				this.userClass()
			},
			// 获取用户列表
			userClass() {
				//userCertification/queryUserCertification?page=1&limit=10&name=&phone=&classify=1  已实名的个人用户列表
				this.tableDataLoading4 = true
				this.$http({ //user/selectUserList?page=1&limit=10&isCompany=2&phone=&userName=&isAuthentication=1  普通用户列表
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'userName': this.nickName2,
						'phone': this.phoneU,
						'isCompany': '',
						'isAuthentication': 0,
						'isVip': '',
						'shopType': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page1 = 1
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.page1 = 1
				this.phoneU = ''
				this.nickName2 = ''

				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				this.phoneA = row.phone
				this.dialogFormVisible3 = false
			},
			// 获取服务分类启用列表
			homeSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/fixedClassify/getTreeClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'classifyName': ''
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.homeData1 = []
					if (data.data) {
						for (var i in data.data) {
							if (data.data[i].children.length > 0) {
								for (var j in data.data[i].children) {
									if (data.data[i].children[j].children.length == 0) {
										data.data[i].children[j].children = null
									}
								}
							}

							this.homeData1.push(data.data[i])
						}
					}
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
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('help/helpTakeListExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'phone': this.phone1,
						'status': this.status1,
						'gameId': this.gameId,
						'startTime': this.startTime,
						'endTime': endTime,
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
						elink.download = '万能订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			handleChange1(value) {
				console.log('value', value)
				if (value.length == 2) {
					this.gameId = value[1];
					this.fixedClassifyId = value[1];
				}
				for (var i in this.homeData1) {
					if (value[0] == this.homeData1[i].classifyId) {
						for (var j in this.homeData1[i].children) {
							if (value[1] == this.homeData1[i].children[j].classifyId) {
								this.classifyData = this.homeData1[i].children[j]
							}
						}
					}
				}
				// this.dataSelect()
				// this.value.forEach(element => {
				// 	this.gameName = element;
				// });
				console.log('this.classifyData', this.classifyData)
			},
			handleChange2(value) {
				console.log('value', value)
				if (value.length == 2) {
					this.gameIds = value[1];
				}
				this.dataSelect()

			},
			// 修改弹框
			rechargenone(row, index) {
				this.conters = ''
				this.orderId = row.id
				this.types = index
				this.dialogFormVisible1 = true
			},
			StairNoticeTo1() {
				if (this.types == 1) {

					if (this.conters == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入备注信息',
							type: 'warning'
						})
						return
					}
					var url = 'help/updateTimeAndRemarks'
					var datas = {}
					datas.helpOrderId = this.orderId
					datas.remarks = this.conters
				}
				if (this.types == 2) {
					if (this.conters == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择预约时间',
							type: 'warning'
						})
						return
					}
					var url = 'help/updateTimeAndRemarks'
					var datas = {}
					datas.helpOrderId = this.orderId
					datas.deliveryTime = this.conters
				}

				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams(datas)
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
			},
			// 报价弹框
			classifyBj(row) {
				this.homeSelectB(row.id)
				this.dialogFormVisibleBj = true
			},
			//
			homeSelectB(id) {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('admin/HelpBidding/getBiddingListByOrderId'),
					method: 'get',
					params: this.$http.adornParams({
						'id': id
					})
				}).then(({
					data
				}) => {
					this.baojiaList = data.data
					this.tableDataLoading4 = false
				})
			},
			// 查看评论
			plCompile(row) {
				console.log(row)
				this.renwuId = row.orderNo

				this.dialogFormVisible10 = true
				this.pinglunSelect(row.orderNo)
			},
			// 用户评论列表
			pinglunSelect(indentNumber) {
				this.tableDataLoadingPl = true
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl('admin/evaluate/getUserEvaluate'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page2,
						'limit': this.size2,
						'indentNumber': indentNumber,
						'evaluateType': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingPl = false
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
						url: this.$http.adornUrl('admin/evaluate/deleteEvaluate'),
						method: 'get',
						params: this.$http.adornParams({
							'evaluateId': row.id
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
									this.pinglunSelect(this.renwuId)
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
			// 删除
			clear(index) {
				this.image.splice(index, 1);
			},
			//上传成功
			handleUploadSuccess(file, fileList) {
				this.image.push(file.data.path)
				console.log('this.detailsImg', this.image)
				// this.image += file.data + ','
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			// 补费列表弹框
			bufeiBtn(row) {
				this.bufeiSelect(row.orderNo)

			},
			// 获取补费列表数据
			bufeiSelect(orderNo) {
				this.tableDataLoading2 = true
				this.$http({
					url: this.$http.adornUrl('admin/repairRecord/getRecordByOrderNo'),
					method: 'get',
					params: this.$http.adornParams({
						'orderType': 1,
						'orderNo': orderNo
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.dialogFormVisibleBf = true

						this.userData = data.data
						// this.userData.push(data.data)
						this.tableDataLoading2 = false
					} else {
						this.$message({
							message: '暂无数据',
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}


				})
			},
			// 转单弹框
			zhuandan(index, row) {
				this.types = index
				if (row) {
					this.ordersId = row.id
					this.ordersType = row.ordersType
				}

				this.page1 = 1
				this.campus = ''
				this.phoneU = ''
				this.sexU = ''
				this.invitationCode = ''
				this.inviterCode = ''
				this.dataSelectUser()
				this.dialogFormVisibleSf = true
			},
			// 确认选择
			userBtns(row) {

				this.$http({
					url: this.$http.adornUrl('help/adminDistributionOrder'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': row.userId,
						'orderId': this.ordersId,
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
								this.dialogFormVisibleSf = false
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
			// tabs切换
			handleClickU(tab, event) {
				this.page1 = 1
				this.campus = ''
				this.phoneU = ''
				this.sexU = ''
				this.invitationCode = ''
				this.inviterCode = ''
				if (tab._props.label == '商家用户') {
					this.isAuthentication = 2
					this.shopType = 2
					this.isCompany = ''
					this.isVip = ''
					this.dataSelectUser()
				}
				if (tab._props.label == '师傅用户') {
					this.isAuthentication = 2
					this.isCompany = ''
					this.isVip = ''
					this.shopType = 1
					this.dataSelectUser()
				}

				if (tab._props.label == '员工用户') {
					this.isAuthentication = 0
					this.isCompany = 1
					this.isVip = ''
					this.shopType = ''
					this.dataSelectUser()
				}
			},
			// 获取数据列表
			dataSelectUser() {
				this.tableDataLoadingU = true
				var authentication = ''
				if (this.ordersType == 1) {
					authentication = ''
				} else {
					authentication = 2
				}
				if (this.types == 1) {
					this.$http({
						url: this.$http.adornUrl('user/selectUserList'),
						method: 'get',
						params: this.$http.adornParams({
							'page': this.page1,
							'limit': this.limit1,
							'isCompany': this.isCompany,
							'phone': this.phoneU,
							'userName': this.campus,
							'isAuthentication': this.isAuthentication,
							'shopType': this.shopType,
							'companyName': this.sexU,
							'invitationCode': this.invitationCode,
							'inviterCode': this.inviterCode,
							'isVip': this.isVip,
						})
					}).then(({
						data
					}) => {
						this.tableDataLoadingU = false
						let returnData = data.data
						this.userListData = returnData
					})
				} else {
					this.$http({
						url: this.$http.adornUrl('userCertification/queryCertification'),
						method: 'get',
						params: this.$http.adornParams({
							'page': this.page1,
							'limit': this.limit1,
							'status': 1,
							'name': this.campus,
							'classify': 1,
							'phone': this.phoneU,
							'isRecommend': -1,
						})
					}).then(({
						data
					}) => {
						this.tableDataLoadingU = false
						let returnData = data.data
						this.userListData = returnData
					})
				}

			},
			// 查询
			selectU() {
				this.page1 = 1
				this.dataSelectUser()
			},
			// 重置
			cleansU() {
				this.page1 = 1
				this.campus = ''
				this.phoneU = ''
				this.sexU = ''
				this.invitationCode = ''
				this.inviterCode = ''
				this.dataSelectUser()
			},
			// 部分退款弹框
			tuikuanBtnBf(row) {
				this.content = ''
				this.ordersId = row.id
				// this.zongjine = Number(row.sumMoney)
				// this.dacefei = row.taxiMoney
				// this.xiangmujin = Number(row.sumMoney)-Number(row.taxiMoney)

				this.$http({
					url: this.$http.adornUrl('orders/selectRefundMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'orderType': 1,
						'ordersId': this.ordersId
					})
				}).then(({
					data
				}) => {
					if (data.data) {
						this.zongjine = data.data
					} else {
						this.$message({
							message: '暂无数据',
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}


				})


				this.dialogFormVisibleBftk = true
			},
			// 确定退款
			replyNoticeTk(row) {
				if (this.content === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入退款金额',
						type: 'warning'
					});
					return
				}
				if (Number(this.content) <= 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入有效的金额',
						type: 'warning'
					});
					return
				}
				if (Number(this.content) > Number(this.zongjine)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入有效的金额',
						type: 'warning'
					});
					return
				}
				this.$confirm(`确定要退款吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/refundMoney'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': this.ordersId,
							'money': this.content,
							'orderType': 1,
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
									this.dialogFormVisibleBftk = false
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
			// 退全款
			tuikuanBtn(row) {
				let delid = row.id
				this.$confirm(`确定退全款吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/refundOrders'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': delid,
							'orderType': 1
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
			zhuandan1(index) {
				this.types = index
				this.isAuthentication = 2
				this.isCompany = ''
				this.isVip = ''
				this.shopType = 1
				this.page1 = 1
				this.campus = ''
				this.phoneU = ''
				this.sexU = ''
				this.invitationCode = ''
				this.inviterCode = ''
				this.dataSelectUser()
				this.dialogFormVisibleSf1 = true
			},
			// 选择师傅
			userBtns2(row) {
				this.masterUserId = row.userId
				this.masterUserName = row.name
				this.fuwuOrderSelect()
				this.dialogFormVisibleSf1 = false
			},
			// 获取服务列表
			fuwuOrderSelect() {
				this.$http({
					url: this.$http.adornUrl('masterService/selectMasterServiceByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 20,
						'userId': this.masterUserId
					})
				}).then(({
					data
				}) => {
					1
					let returnData = data.data
					if (data.data) {
						for (var i in data.data) {
							data.data[i].classifyName = data.data[i].fixedClassify.classifyName
						}
					}

					this.homeData1 = returnData
				})
			},
			handleChange3(value) {


				for (var i in this.homeData1) {
					if (this.fixedClassifyId == this.homeData1[i].classifyId) {
						for (var j in this.homeData1[i].children) {
							if (value[1] == this.homeData1[i].children[j].classifyId) {
								this.classifyData = this.homeData1[i].children[j]
							}
						}
					}
				}
				// this.dataSelect()
				// this.value.forEach(element => {
				// 	this.gameName = element;
				// });
				console.log('this.classifyData', this.classifyData)
			},
		},
		mounted() {
			this.homeSelect()
			this.dataSelect()
		}
	}
</script>

<style lang="scss" scoped>
.universal-mission-page {
	padding: 0;
	overflow-x: hidden;
}

.universal-mission-page__hero {
	margin-bottom: 12px;
}

.universal-mission-page__filter {
	margin-bottom: 12px;
}

.universal-mission-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.universal-mission-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.universal-mission-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.universal-mission-page__select {
	width: 150px;
}

.universal-mission-page__cascader {
	width: 150px;
}

.universal-mission-page__input {
	width: 200px;
}

.universal-mission-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.universal-mission-page__tabs-wrap {
	margin-bottom: 0;
}

.universal-mission-page__tabs {
	margin-bottom: 0;
}

.universal-mission-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.universal-mission-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.universal-mission-page__table-card >>> .el-table {
	border-radius: 0;
}

.universal-mission-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.universal-mission-page__table >>> .el-table__body td {
	background: #fff;
}

.universal-mission-page__link {
	color: #01af74;
	cursor: pointer;
}

.universal-mission-page__link:hover {
	color: #017c52;
}

.universal-mission-page__time {
	color: #e74c3c;
	font-weight: 600;
}

.universal-mission-page__text-btn {
	color: #01af74;
	background: #fff;
	border: none;
	padding: 0 4px;
}

.universal-mission-page__text-btn:hover {
	color: #017c52;
	background: #fff;
}

.universal-mission-page__status--active {
	color: #01af74;
}

.universal-mission-page__status--muted {
	color: #999;
}

.universal-mission-page__stats {
	display: flex;
	flex-wrap: wrap;
	gap: 16px;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
}

.universal-mission-page__stat-item em {
	font-style: normal;
	font-weight: 600;
	color: #01af74;
}

.universal-mission-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>

<style>
	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}
</style>