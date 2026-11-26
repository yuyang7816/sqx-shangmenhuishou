<template>
	<div class="shops-list-page">
		<div class="nf-page-hero shops-list-page__hero">
			<span class="nf-page-hero__badge">商家管理</span>
			<h2 class="nf-page-hero__title">商家列表</h2>
			<p class="nf-page-hero__desc">管理已认证商家信息，支持按绑定用户、店铺名称与手机号筛选，可查看服务、订单、员工与钱包等</p>
		</div>

		<div class="nf-filter-card shops-list-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body shops-list-page__filter-body">
				<div class="shops-list-page__field">
					<span class="shops-list-page__label">绑定用户：</span>
					<el-input class="shops-list-page__input" @keydown.enter.native="shopSelect" placeholder="请输入绑定用户"
						v-model="shopName1">
					</el-input>
				</div>
				<div class="shops-list-page__field">
					<span class="shops-list-page__label">店铺名称：</span>
					<el-input class="shops-list-page__input" @keydown.enter.native="shopSelect" placeholder="请输入店铺名称"
						v-model="shopNameT">
					</el-input>
				</div>
				<div class="shops-list-page__field">
					<span class="shops-list-page__label">用户手机号：</span>
					<el-input class="shops-list-page__input" @keydown.enter.native="shopSelect" placeholder="请输入用户手机号"
						v-model="region">
					</el-input>
				</div>
				<div class="shops-list-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="shopSelect">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
				</div>
			</div>
		</div>

		<div class="shops-list-page__tabs-wrap">
			<el-tabs v-model="activeName" class="shops-list-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="商铺列表" name="first">
					<div class="nf-table-card shops-list-page__table-card">
						<el-table v-loading="tableDataLoading" :data="typeDatas.records" class="shops-list-page__table">
				<el-table-column label="编号" prop="userId" width="80" fixed="left">
				</el-table-column>
				<el-table-column label="绑定用户" prop="userId" width="150">
					<template slot-scope="scope">
						<span class="shops-list-page__link"
							@click='goUserInfo(scope.row.userId)'>{{scope.row.userId ? scope.row.userName : '暂无'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="isRecommend" label="推荐" width="100">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.isRecommend"
							@change="change2(scope.row.isRecommend,scope.row)" :active-value="openValue1"
							:disabled="!isAuth('locality:update')" :inactive-value="closeValue1"
							active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column label="店铺名称" prop="shopName" fixed="left">
				</el-table-column>
				<el-table-column label="店铺手机号" prop="shopPhone" width="120">
				</el-table-column>
				<el-table-column label="用户手机号" prop="phone" width="120">
				</el-table-column>
				<el-table-column label="商铺类型" prop="shopType">
				</el-table-column>
				<el-table-column prop="shopImg" label="LOGO" width="150">
					<template slot-scope="scope">

						<div style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.shopImg" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="scope.row.shopImg" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="addressImg" label="封面图" width="150">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.addressImg">
							<div v-for="(item,index) in scope.row.addressImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px;height:auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column label="商铺佣金比例" prop="shopTypeName" width="150">
					<template slot-scope="scope">
						<span>{{scope.row.rate?scope.row.rate:0}}</span>
						<el-button size="mini" type="danger" style="color: #01af74;background: #fff;border: none;"
							:disabled="!isAuth('shopsList:update')" @click="rechargenone(scope.row,3)">修改佣金比例
						</el-button>
					</template>
				</el-table-column>
				<el-table-column label="开店时间" prop="startTime">
					<template slot-scope="scope">
						<span>{{scope.row.startTime}}</span>
					</template>
				</el-table-column>
				<el-table-column label="闭店时间" prop="startTime">
					<template slot-scope="scope">
						<span>{{scope.row.endTime}}</span>
					</template>
				</el-table-column>
				<el-table-column label="地址" prop="address" width="200">
				</el-table-column>
				<el-table-column label="创建时间" prop="createTime" width="160">
				</el-table-column>
				<el-table-column prop="status" fixed="right" label="状态">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.status" @change="changeS(scope.row.status,scope.row.userId)"
							:active-value="openValue" :disabled="!isAuth('userList:delete')"
							:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="230" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="shopsListAdmin(scope.row)" style="margin: 5px;">
							服务列表
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListOrder(scope.row)" style="margin: 5px;">
							服务订单
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListRw(scope.row)" style="margin: 5px;">
							任务订单
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListYg(scope.row)" style="margin: 5px;">
							员工列表
						</el-button>
						<el-button :disabled="!isAuth('shopsList:update')" size="mini" type="primary"
							@click="updatesShop(scope.$index, scope.row)" style="margin: 5px;">修改信息
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListSr(scope.row)" style="margin: 5px;">数据统计
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListQb(scope.row)" style="margin: 5px;">商铺钱包
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListTime(scope.row)" style="margin: 5px;">
							时间管理
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListPh(scope.row)" style="margin: 5px;">
							员工排行
						</el-button>
					</template>
				</el-table-column>
						</el-table>
						<div class="shops-list-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10,  20, 30, 50, 100]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="typeDatas.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 修改商户 -->
		<el-dialog :visible.sync="dialogFormVisible" center :title="titles">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺名称：</span>
				<el-input v-model="shopName" placeholder="请输入店铺名称" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺手机号：</span>
				<el-input v-model="shopPhone" placeholder="请输入店铺手机号" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">用户手机号：</span>
				<el-input v-model="phone" placeholder="请输入用户手机号" style="width:50%;"></el-input>
			</div>
			
			<div style="display: flex;align-items: center;margin: 2% 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">LOGO：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="shopCover"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="shopCover" :src="shopCover" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">封面图：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in addressImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload action="https://admin.wanxiujiang.com/sqx_fast/file/upload"
							list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess"
							:on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1"></el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺类型：</span>
				<el-input v-model="shopType" placeholder="请输入店铺类型" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">开店时间：</span>
				<el-time-select v-model="openTime" :picker-options="{
            start: '00:00',
            step: '00:15',
            end: '24:00'
          }" placeholder="选择开店时间" style="width:50%;">
				</el-time-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">闭店时间：</span>
				<el-time-select v-model="closeTime" :picker-options="{
            start: '00:00',
            step: '00:15',
            end: '24:00'
          }" placeholder="选择闭店时间" style="width:50%;">
				</el-time-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺地址：</span>
				<el-input v-model="detailedAddress"  placeholder="请输入店铺地址"
					type="text" style="width:50%;"></el-input> <!-- @keydown.enter.native="phoneSelect" -->
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 店铺二维码 -->
		<el-dialog :visible.sync="dialogFormVisible2" center title="店铺二维码">
			<div>
				<img :src="erweimaImg" style="width: 50%;margin-left: 25%;" />
				<div style="text-align: center;margin-top: 20px;">
					<el-button type="primary" @click="chakan()">打开窗口查看</el-button>
				</div>
			</div>
		</el-dialog>
		<!-- 添加、修改类型 -->
		<el-dialog :visible.sync="dialogFormVisible3" center :title="titles1">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">类型名称：</span>
				<el-input v-model="shopTypeName" placeholder="请输入类型名称" style="width:50%;"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo1()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改保证金 金额 -->
		<el-dialog title="修改金额" :visible.sync="dialogFormVisible4" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
				<el-radio-group v-model="type">
					<el-radio :label="1">添加</el-radio>
					<el-radio :label="2">减少</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
				<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
					placeholder="请输入金额"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible4 = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改佣金比例 -->
		<el-dialog title="修改佣金比例" :visible.sync="dialogFormVisible7" center>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">佣金比例：</span>
				<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
					placeholder="请输入佣金比例,0.1为10%"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible7 = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeToYj()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改配送费 金额 -->
		<el-dialog title="修改配送费" :visible.sync="dialogFormVisible6" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">配送费：</span>
				<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
					placeholder="请输入配送费"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible6 = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改密码 -->
		<el-dialog title="修改密码" :visible.sync="dialogFormVisible5" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">新密码：</span>
				<el-input style="width:50%;" v-model="newPassword" type="password" :controls="false"
					placeholder="请输入新密码">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">确认密码：</span>
				<el-input style="width:50%;" v-model="querenPassword" type="password" :controls="false"
					placeholder="请输入确认密码">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible5 = false">取 消</el-button>
				<el-button type="primary" @click="updatePassword()">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible11" center>

			<el-input style="width: 150px;" @keydown.enter.native="select11" clearable placeholder="请输入手机号"
				v-model="phoneU">
			</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			<el-input style="width: 150px;" @keydown.enter.native="select11" clearable placeholder="请输入昵称"
				v-model="nickName">
			</el-input>
			<div style="display: inline-block;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select11">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans11">重置
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading11" :data="userData.list" :row-key="(row)=>{ return row.id}">
				<el-table-column prop="userId" label="编号">
				</el-table-column>
				<el-table-column fixed prop="userName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未设置' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号" width="120">
					<template slot-scope="scope">
						<span style="color: #01af74;cursor: pointer;"
							@click="closes11(scope.row)">{{ scope.row.phone ? scope.row.phone : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="platform" label="来源">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column prop="createTime" label="操作">
					<template slot-scope="scope">
						<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
							@click="closes2(scope.row)">
							确定</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange11" @current-change="handleCurrentChange11"
					:page-sizes="[10, 20, 30, 50]" :page-size="size11" :current-page="page11"
					layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>

		<!-- 员工列表 -->
		<el-dialog title="员工列表" :visible.sync="dialogFormVisibleHd" center width="70%">
			<div style="margin-bottom: 10px;display: inline-block;width: 100%;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="diaodu()">
					员工调度</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="addYg">添加员工
				</el-button>
			</div>
			<el-table v-loading="tableDataLoadingHd" :data="huodongData.records">
				<el-table-column prop="userId" label="编号" width="80"></el-table-column>
				<el-table-column prop="userName" label="用户名">
					<template slot-scope="scope">
						<span style="color: #f56c6c;cursor:pointer"
							@click="goUserInfo(scope.row.userId)">{{ scope.row.userName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="avatar" label="用户头像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="电话">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="updateTime" label="状态" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.workStatus==0" style="color: #008000;">空闲中</span>
						<span v-if="scope.row.workStatus==1" style="color: red;">忙碌中</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('shopsList:delete')"
							@click="deleteuser(scope.row)">移除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeYg" @current-change="handleCurrentChangeYg"
					:page-sizes="[10, 15, 20, 25]" :page-size="limitYg" :current-page="pageYg"
					layout="total,sizes, prev, pager, next" :total="huodongData.total">
				</el-pagination>
			</div>
		</el-dialog>

		<!-- 预约时间列表 -->
		<el-dialog title="预约时间列表" :visible.sync="dialogFormVisibleTime" center width="70%">
			<div style="text-align: right;margin: 10px;">
				<el-button size="mini" type="primary" :disabled="!isAuth('shopsList:add')" @click="updataTime()">添加
				</el-button>
			</div>

			<el-table v-loading="tableDataLoadingTime" :data="yuyueTimeDime">
				<el-table-column prop="serviceTimeId" label="编号" width="80"></el-table-column>
				<el-table-column prop="serviceTime" label="接单时间"></el-table-column>
				<el-table-column prop="num" label="数量"></el-table-column>
				<el-table-column prop="sort" label="排序"></el-table-column>
				<el-table-column prop="createTime" label="创建时间"></el-table-column>
				<el-table-column fixed="right" label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('shopsList:update')"
							@click="updataTime(scope.row)">修改</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('shopsList:delete')"
							@click="deleteTime(scope.row)">删除</el-button>
					</template>
				</el-table-column>

			</el-table>
		</el-dialog>
		<!-- 添加、修改预约时间  -->
		<el-dialog :title="titlesTime" :visible.sync="dialogFormVisibleTimeAU" center width="70%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">开始时间：</span>

				<el-time-select placeholder="起始时间" v-model="startTime" :picker-options="{
				      start: '00:00',
				      step: '00:01',
				      end: '23:59'
				    }">
				</el-time-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">结束时间：</span>

				<el-time-select placeholder="结束时间" v-model="endTime" :picker-options="{
				      start: '00:00',
				      step: '00:01',
				      end: '23:59',
				      minTime: startTime
				    }">
				</el-time-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">数量：</span>
				<el-input style="width:50%;" v-model="num" type="number" :min="0" :max="100" :controls="false"
					placeholder="请输入数量"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" :min="0" :controls="false"
					placeholder="请输入排序"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleTimeAU = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeToTime()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
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
	var geocoder, map, markersArray = [];
	export default {
		data() {
			return {
				phone: '',
				phoneU:'',
				nickName: '',
				limit: 10,
				page: 1,
				size11: 10,
				page11: 1,
				shopName1: '',
				isActivity: '',
				region: '',
				shopNameT:'',
				shopId: '',
				userIdList: '',
				shopName: '',
				userData: {}, //用户集合
				openTime: '',
				shopRate:'',
				closeTime: '',
				detailedAddress: '',
				shopLng: '',
				shopLat: '',
				percentage1: 0, //进度条
				img: [], //任务轮播图
				// img: '',
				classn: '',
				value: '',
				parentId: 0,
				parentIdnum: '',

				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableDataLoading11: false,
				dialogFormVisible: false,
				dialogFormVisible11: false, //用户列表
				typeDatas: {},
				typeDatas1: {},
				titles: '添加',
				// 地图相关
				longitude: '', //维度
				latitude: '', //经度
				loadingMap: true,
				BMap: '',
				map: '',
				geoc: '',
				keyword: '',
				showMap: false,
				pointLngLat: '',
				center: {
					lng: 109.45744048529967,
					lat: 36.49771311230842
				},
				storeAddress: [],
				storeAddre: '请选择城市',
				form: {
					province: '',
					city: '', //市
					district: '', //区
				},
				dialogImageUrl: [],
				dialogVisible: false,
				erweimaImg: '',
				activeName: 'first',
				shopTypeName: '',
				shopTypId: '',
				titles1: '',
				labels: [], //标签集合
				bq: '', //输入的标签名
				province: '',
				city: '', //市
				district: '', //区
				options: regionData,
				shopCover: '',
				addressImg:[],
				shopTypenum: [],
				typeDatas2: [],
				shopType: '',

				type: '',
				money: '',
				classIndex: '',
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				tablemoney: 0,
				dataForm: {
					id: 0,
					userName: '',
					password: '',
					comfirmPassword: '',
					salt: '',
					email: '',
					mobile: '',
					roleIdList: [],
					shopIdList: [],
					status: 1,
				},
				newPassword: '',
				querenPassword: '',
				bannedFlag: '',
				exemptErrandFlag: 1, //是否见面配送费0开启 1关闭
				errandMoney: 0,
				exemptMinMoney: 0,
				minimumDelivery: 0,
				autoSendOrder: 1,
				autoAcceptOrder: 1,
				isReceiveSubscribe: 1,
				putawayFlag: 1,
				distributionDistance: 3000,
				facility: 1,
				shopUserId: '',
				shopUserName: '',
				shopPhone: '', //商户关联手机号
				snCode: '',

				dialogFormVisibleHd: false,
				dialogFormVisibleXq: false,
				tableDataLoadingHd: false,
				huodongData: {},
				limitYg: 10,
				pageYg: 1,
				integralName: '',
				xiangqingData: {},
				statusmain: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '否'
					},
					{
						value: 1,
						label: '是'
					}
				],
				openValue: 1,
				closeValue: 2,

				activityId: '',
				// 预约时间
				dialogFormVisibleTime: false,
				tableDataLoadingTime: false,
				yuyueTimeDime: [],
				titlesTime: '',
				dialogFormVisibleTimeAU: false,
				num: '',
				startTime: '',
				endTime: '',
				serviceTimeId: '',
				sort: '',
				openValue1: 1,
				closeValue1: 2,
			}
		},
		methods: {



			// tabs切换
			handleClick(tab, event) {
				this.page = 1
				this.limit = 10
				if (tab._props.label == '商铺列表') {

					this.dataSelect()
				}
				if (tab._props.label == '商家类型') {

					this.dataSelect1()
				}

			},

			//用户模块的分页处理
			handleSizeChange11(val) {
				this.size11 = val;
				this.dataSelect11()
			},
			//用户模块的分页处理
			handleCurrentChange11(val) {
				this.page11 = val;
				this.dataSelect11()
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
				this.dataSelect1()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.dataSelect1()
			},
			handleSizeChangeYg(val) {
				this.limitYg = val
				this.huodongSelect()
			},
			handleCurrentChangeYg(val) {
				this.pageYg = val
				this.huodongSelect()
			},
			// 图标上传
			handleAvatarSuccess(file) {
				this.img = file.data.path
			},
			// 添加店铺
			addNotice() {
				this.titles = '添加'
				this.shopId = ''
				this.shopName = ''
				this.shopType = ''
				this.openTime = ''
				this.shopRate = ''
				this.closeTime = ''
				this.shopCover = ''
				this.addressImg = []
				this.dialogImageUrl = []
				this.img = []
				this.detailedAddress = ''
				this.shopLng = ''
				this.shopLat = ''
				this.longitude = '' //维度
				this.latitude = '' //经度
				this.bannedFlag = ''
				this.errandMoney = 0
				this.exemptErrandFlag = 1
				this.facility = 1
				this.snCode = ''
				this.value = ''
				this.distributionDistance = 3000
				this.exemptMinMoney = 0
				this.minimumDelivery = 0
				this.autoSendOrder = 1
				this.autoAcceptOrder = 1
				this.isReceiveSubscribe = 1
				this.putawayFlag = 1
				this.labels = []
				this.shopPhone = ''
				this.shopUserId = ''
				this.shopUserName = '',
					this.storeAddress = [],
					this.storeAddre = '请选择城市',
					this.dialogFormVisible = true
				// this.getMyLocation();
			},


			// 获取用户数据列表
			dataSelect11() {
				this.tableDataLoading11 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page11,
						'limit': this.size11,
						'shopAdminFlag': '',
						'phone': this.phoneU,
						'userName': this.nickName,
						'type': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading11 = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			select11() {
				this.page11 = 1
				this.dataSelect11()
			},
			//查看用户
			selectUserId() {
				this.dialogFormVisible11 = true
				this.page11 = 1
				this.phoneU = ''
				this.nickName = ''
				this.dataSelect11()
			},

			closes11(item) {
				this.shopUserId = item.userId
				this.shopUserName = item.userName
				this.dialogFormVisible11 = false
			},
			// 重置
			cleans11() {
				this.page11 = 1
				this.phoneU = ''
				this.nickName = ''
				this.dataSelect11()
			},
			// 添加店铺
			releasNoticeTo() {


				if (this.shopPhone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入商户关联手机号',
						type: 'warning'
					})
					return
				}
				if (this.shopName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入店铺名称',
						type: 'warning'
					})
					return
				}
				if (this.openTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择开店时间',
						type: 'warning'
					})
					return
				}
				if (this.closeTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择闭店时间',
						type: 'warning'
					})
					return
				}
				if (this.detailedAddress == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入店铺地址',
						type: 'warning'
					})
					return
				}
				var datas = {}
				datas.shopName = this.shopName
				datas.startTime = this.openTime

				datas.shopRate = this.shopRate
				datas.endTime = this.closeTime
				datas.address = this.detailedAddress


				datas.shopImg = this.shopCover
				datas.addressImg = this.addressImg.toString()
				datas.userId = this.shopUserId;
				datas.phone = this.phone;
				datas.shopPhone = this.shopPhone;
				datas.shopType = this.shopType

				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
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
			// 修改弹框
			updatesShop(index, rows) {
				this.titles = '修改'
				this.shopId = rows.shopId
				this.shopName = rows.shopName
				if (rows.shopBanner != null) {
					this.dialogImageUrl = rows.shopBanner
					this.img = rows.shopBanner
				}
				if (rows.shopLable) {
					this.labels = rows.shopLable.split(',')
				}
				this.openTime = rows.startTime
				this.closeTime = rows.endTime
				this.detailedAddress = rows.address
				this.shopCover = rows.shopImg
				if (rows.addressImg) {
					this.addressImg = rows.addressImg.split(',')
				}
				this.shopRate = rows.shopRate;
				this.province = rows.province;
				this.district = rows.district;
				this.shopType = rows.shopType;
				this.bannedFlag = rows.bannedFlag;
				if (rows.errandMoney != null) {
					this.errandMoney = rows.errandMoney
				} else {
					this.errandMoney = 0
				}
				if (rows.exemptMinMoney != null) {
					this.exemptMinMoney = rows.exemptMinMoney
				} else {
					this.exemptMinMoney = 0
				}

				this.minimumDelivery = rows.minimumDelivery
				this.shopPhone = rows.shopPhone
				this.phone = rows.phone
				this.shopUserId = rows.userId

				if (rows.facility) {
					this.facility = rows.facility
				}
				this.snCode = rows.snCode
				this.value = rows.value
				if (rows.exemptErrandFlag != null) {
					this.exemptErrandFlag = rows.exemptErrandFlag
				}

				if (rows.autoSendOrder != null) {
					this.autoSendOrder = rows.autoSendOrder
				}
				if (rows.autoAcceptOrder != null) {
					this.autoAcceptOrder = rows.autoAcceptOrder
				}
				if (rows.isReceiveSubscribe != null) {
					this.isReceiveSubscribe = rows.isReceiveSubscribe
				}
				if (rows.exemptErrandFlag == 1) {
					this.exemptMinMoney = 0
				}
				this.putawayFlag = rows.putawayFlag
				this.distributionDistance = rows.distributionDistance
				this.storeAddre = this.province + '/' + this.city + '/' + this.district
				// this.getMyLocation();
				this.dialogFormVisible = true

			},
			// 查看二维码
			erweima(row) {

				console.log('row.shopId', row.shopId)
				this.dialogFormVisible2 = true
				this.erweimaImg = this.$http.adornUrl('invite/shopQr?shopId=' + row.shopId)

			},
			chakan() {
				window.open(this.erweimaImg, '_blank');
			},
			// 删除分类
			deletes(row) {
				// if (row.children == null || row.children.length == 0) {
				this.$confirm(`确定删除此条信息?删除后将无法恢复`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/goods/deleteGoodsShop'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.shopId
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
							}
						})
					}

					})
				}).catch(() => {})
				// } else {
				//   this.$message({
				//     message: '请先删除',
				//     type: 'error',
				//     duration: 1500,
				//   })
				//   return
				// }
			},
			// 封禁
			fengjin(row) {
				if (row.bannedFlag == 1) {
					var bannedFlag = 0
					var texts = '确定要解封此商铺吗?'
				} else {
					var bannedFlag = 1
					var texts = '确定要封禁此商铺吗?'

				}

				this.$confirm(texts, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/goods/updateGoodsShop'),
						method: 'post',
						data: this.$http.adornData({
							'shopId': row.shopId,
							'bannedFlag': bannedFlag
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
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

			//查看用户详情
			goUserInfo(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}


			},

			// 服务列表
			shopsListAdmin(row) {
				this.$router.push({
					path: '/locality',
					query: {
						userName: row.userName,
						userId: row.userId
					}
				})
			},
			// 订单列表
			shopsListOrder(row) {
				this.$router.push({
					path: '/shopsOrderFw',
					query: {
						companyName: row.shopName,
						userId: row.userId

					}
				})
			},
			// 任务订单
			shopsListRw(row) {
				this.$router.push({
					path: '/shopsOrder',
					query: {
						userId: row.userId

					}
				})
			},
			// 员工列表
			shopsListYg(row) {
				this.pageYg = 1
				this.shopId = row.userId
				this.activityId = row.activityId
				this.dialogFormVisibleHd = true
				this.huodongSelect()
			},
			// 收入统计
			shopsListSr(row) {
				this.$router.push({
					path: '/shopDatas',
					query: {
						shopId: row.userId
					}
				})
			},
			// 保证金明细
			shopsListBzj(row) {
				this.$router.push({
					path: '/bondList',
					query: {
						shopId: row.shopId
					}
				})
			},

			// 评价明细
			shopsListPJ(row) {
				this.$router.push({
					path: '/pingjiaList',
					query: {
						shopId: row.shopId
					}
				})
			},
			// 商铺骗保
			shopsListQb(row) {
				this.$router.push({
					path: '/shopWallet',
					query: {
						shopId: row.userId
					}
				})
			},

			// 优惠券管理
			shopsListYhq(row) {
				this.$router.push({
					path: '/couponShop',
					query: {
						shopId: row.userId
					}
				})
			},
			// 获取商户数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('userCertification/queryUserCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'name': this.shopName1,
						'phone': this.region,
						'shopName':this.shopNameT,
						'classify': 2,
						'isRecommend':-1,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					// for (var i in data.data.list) {
					// 	if (data.data.list[i].shopBanner) {
					// 		data.data.list[i].shopBanner = data.data.list[i].shopBanner.split(',')
					// 	}

					// }
					let returnData = data.data
					this.typeDatas = returnData
				})
			},
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75", "来点小收入");
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
						.latLng
						.getLng() + '&key=5M6BZ-6YPLJ-AZRFX-D2XM3-UWTDT-SMF34&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response, response.result.formatted_addresses.recommend)
						that.detailedAddress = response.result.formatted_addresses.recommend

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
					var marker = new qq.maps.Marker({
						map: map,
						position: event.latLng
					});
					markersArray.push(marker);

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
				console.log(this.detailedAddress)
				// this.address = this.province+ this.city+this.district+this.address
				var addr = this.province + this.city + this.district + this.address
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + addr +
					'&key=5M6BZ-6YPLJ-AZRFX-D2XM3-UWTDT-SMF34&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					console.log('response', response)
					that.longitude = response.result.location.lng; // 经度
					that.latitude = response.result.location.lat; // 纬度
					// that.address = response.result.title
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			phoneSelect() {
				this.select()
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			// LOGo
			handleAvatarSuccess1(file, fileList) {
				this.shopCover = file.data.path
			},
			// 封面图
			handleAvatarSuccess2(file, fileList) {
				this.addressImg = file.data.path
			},
			// 删除
			clear(index) {
				this.addressImg.splice(index, 1);
			},
			//上传成功
			handleUploadSuccess(file, fileList) {
				this.addressImg.push(file.data.path)
				console.log('this.detailsImg', this.addressImg)
				// this.image += file.data + ','
			},

			// 轮播图片上传
			handleRemove(file) {
				console.log(file, this.img)
				this.img.push(file.data.path);
			},
			handleDownload(file) {

				console.log(file, this.img)
			},
			// 删除轮播图
			dels(index) {
				this.img.splice(index, 1);
				console.log(this.img)
			},
			shopSelect() {
				this.page = 1
				this.dataSelect()
			},
			cleans2(state) {
				this.page = 1
				this.shopName1 = ''
				this.region = ''
				this.shopNameT = ''
				this.isActivity = ''
				this.dataSelect()
			},
			// 获取分类数据
			dataSelect1() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/shoptype/selectShopTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.typeDatas1 = returnData
				})
			},
			// 获取分类数据不带分页
			dataSelect2() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/shoptype/selectShopTypeList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.typeDatas2 = returnData
				})
			},
			// 添加类型
			addNotice1(row) {
				if (row != 1) {
					this.titles1 = '修改类型'
					this.shopTypId = row.id
					this.shopTypeName = row.shopTypeName
				} else {
					this.titles1 = '添加类型'
					this.shopTypId = ''
					this.shopTypeName = ''
				}
				this.dialogFormVisible3 = true

			},
			// 确认添加
			releasNoticeTo1() {
				if (this.shopTypeName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入类型名称',
						type: 'warning'
					})
					return
				}
				if (this.titles1 == '添加类型') {
					var url = 'admin/shoptype/insertShopType'
				}
				if (this.titles1 == '修改类型') {
					var url = 'admin/shoptype/upateShopType'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'id': this.shopTypId,
						'shopTypeName': this.shopTypeName
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible3 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect1()
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
			// 删除类型
			deletes1(row) {
				this.$confirm(`确定删除此条信息?删除后将无法恢复`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/shoptype/deleteShopType'),
						method: 'post',
						params: this.$http.adornParams({
							'shopTypeId': row.id
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
									this.dataSelect1()
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
					this.labels.push(this.bq)
					this.bq = ''
				}

			},
			// 删除标签
			dels1(index) {
				this.labels.splice(index, 1);
				console.log(this.labels)
			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				if (this.city == '市辖区') {
					this.city = this.province
				}
				console.log(this.$refs['cascaderAddr'].currentLabels)
			},
			shopTypeSelect() {

			},
			// 修改保证金
			rechargenone(row, index) {
				this.shopId = row.userId
				if (index == 2) {
					this.type = ''
					this.money = 0
					this.tablemoney = row.cashDeposit
					this.dialogFormVisible4 = true
				}
				if (index == 1) {
					this.bannedFlag = row.bannedFlag
					this.money = row.errandMoney
					this.dialogFormVisible6 = true
				}
				if (index == 3) {
					this.bannedFlag = row.bannedFlag
					this.money = row.rate
					this.dialogFormVisible7 = true
				}

			},
			StairNoticeTo2() {
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				this.money = Number(this.money)
				this.$http({
					url: this.$http.adornUrl('admin/goods/updateGoodsShop'),
					method: 'post',
					data: this.$http.adornData({
						'shopId': this.shopId,
						'errandMoney': this.money,
						'bannedFlag': this.bannedFlag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '添加金额成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.money = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible6 = false
					} else {
						this.$message.error(data.msg)
					}

				})
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

				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				if (this.type == 2) {
					if (this.money > this.tablemoney) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '最高金额为' + this.tablemoney,
							type: 'warning'
						})
						return
					}
				}
				this.$http({
					url: this.$http.adornUrl('user/updateSafetyMoney'),
					method: 'post',
					params: this.$http.adornParams({
						'userId':this.shopId,
						'type':this.type,
						'money':this.money
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '添加金额成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.money = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible4 = false
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 修改佣金比例
			StairNoticeToYj() {
				this.money = Number(this.money)
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入佣金比例',
						type: 'warning'
					})
					return
				}
				if (this.money >1) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '佣金比例输入范围在0~1之间',
						type: 'warning'
					})
					return
				}

				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.shopId,
						'rate': this.money,
						// 'bannedFlag': this.bannedFlag
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
								this.money = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible7 = false
					} else {
						this.$message.error(data.msg)
					}

				})

			},
			updateMima(row) {
				console.log('修改密码')
				this.init(row.adminUserId)
				this.dialogFormVisible5 = true
			},
			init(id) {
				this.$http({
					url: this.$http.adornUrl(`sys/user/info/${id}`),
					method: 'get',
					params: this.$http.adornParams()
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dataForm.userName = data.user.username
						this.dataForm.salt = data.user.salt
						this.dataForm.email = data.user.email
						this.dataForm.mobile = data.user.mobile
						this.dataForm.roleIdList = data.user.roleIdList
						this.dataForm.shopIdList = data.user.shopIdList
						this.dataForm.status = data.user.status
						this.dataForm.id = data.user.userId
					}
				})
			},
			// 确认修改
			updatePassword() {
				if (this.newPassword == '' || this.querenPassword == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '新密码或确认密码不能为空！',
						type: 'warning'
					})
					return
				}
				if (this.newPassword != this.querenPassword) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '两次密码输入不一致！',
						type: 'warning'
					})
					return
				}
				this.dataForm.password = this.newPassword
				this.$http({
					url: this.$http.adornUrl(`/sys/user/updateAdmin`),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.dataForm.id,
						// 'username': this.dataForm.userName,
						'password': this.dataForm.password,
						'salt': this.dataForm.salt,
						// 'email': this.dataForm.email,
						// 'mobile': this.dataForm.mobile,
						// 'status': this.dataForm.status,
						// 'roleIdList': this.dataForm.roleIdList,
						//  'shopIdList': this.dataForm.shopIdList
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible5 = false
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			closes2(row) {
				// if (row.userName) {
				// 	this.shopUserName = row.userName
				// } else {
				// 	this.shopUserName = row.phone
				// }
				this.$http({
					url: this.$http.adornUrl('orders/insertAdminStaffUser'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': this.shopId,
						'phone': row.phone
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
								this.dialogFormVisible11 = false
								this.huodongSelect()
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

			// 获取员工列表
			huodongSelect() {
				this.tableDataLoadingHd = true
				this.$http({
					url: this.$http.adornUrl('user/getWorkerUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageYg,
						'limit': this.limitYg,
						'companyId': this.shopId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingHd = false
					let returnData = data.data
					this.huodongData = returnData
				})
			},
			selectHd() {
				this.pageYg = 1
				this.huodongSelect()
			},
			cleansHs() {
				this.pageYg = 1
				this.integralName = ''
				this.huodongSelect()
			},
			// 确认参加活动
			querenBtnHd(row) {
				this.$confirm(`确定要参加这个活动吗`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/activityManage/shopJoinActivity'),
						method: 'get',
						params: this.$http.adornParams({
							'shopId': this.shopId,
							'activityId': row.activityId
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
									this.dialogFormVisibleHd = false
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
			// 查看活动
			looksHd(row) {
				this.$http({
					url: this.$http.adornUrl('admin/activityManage/getActivityInfo?=2'),
					method: 'get',
					params: this.$http.adornParams({
						'activityId': row.activityId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.xiangqingData = returnData
				})
				this.dialogFormVisibleXq = true
			},
			// 是否推荐活动商家
			changeTj(row) {
				this.$http({
					url: this.$http.adornUrl('admin/goods/updateGoodsShop'),
					method: 'post',
					data: this.$http.adornData({
						'isRecommend': row.isRecommend,
						'shopId': row.shopId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible1 = false
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
			// 减免开关
			onFlag() {
				if (this.exemptErrandFlag == 1) {
					this.exemptMinMoney = 0
					this.minimumDelivery = 0
				}
			},
			// 跳转员工调度
			diaodu() {
				this.$router.push({
					path: '/riderScheduling',
					query: {
						companyId: this.shopId
					}
				})
			},
			// 添加员工弹框
			addYg() {
				this.dialogFormVisible11 = true
				this.page11 = 1
				this.phone = ''
				this.nickName = ''
				this.dataSelect11()
			},
			// 移除员工
			deleteuser(row) {
				this.$confirm(`确定要移除这个员工吗`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('orders/cancelStaffUser'),
						method: 'post',
						params: this.$http.adornParams({
							'userId': this.shopId,
							'phone': row.phone
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
									// this.dialogFormVisibleHd = false
									this.huodongSelect()
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
			// 时间管理
			shopsListTime(row) {
				this.shopId = row.userId
				this.dialogFormVisibleTime = true
				this.yuyueTimeSelect()
			},
			// 预约时间表数据
			yuyueTimeSelect() {
				this.tableDataLoadingTime = true
				this.$http({
					url: this.$http.adornUrl('serviceTime/selectServiceTime'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': this.shopId,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingTime = false
					let returnData = data.data;
					this.yuyueTimeDime = returnData
				})
			},
			// 添加、修改预约时间弹框
			updataTime(row) {
				if (row) {
					this.titlesTime = '修改预约时间'
					this.startTime = row.serviceTime.slice(0, 5)
					this.endTime = row.serviceTime.slice(6, 11)
					this.num = row.num
					this.serviceTimeId = row.serviceTimeId
					this.sort = row.sort
					console.log('row.serviceTime', row.serviceTime, 'this.startTime', this.startTime, 'this.endTime', this
						.endTime)
				} else {
					this.titlesTime = '添加预约时间'
					this.startTime = ''
					this.endTime = ''
					this.num = ''
				}
				this.dialogFormVisibleTimeAU = true
			},
			// 确认添加、修改时间
			StairNoticeToTime() {
				if (this.startTime === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择开始时间',
						type: 'warning'
					})
					return
				}
				if (this.endTime === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择结束时间',
						type: 'warning'
					})
					return
				}
				if (this.num === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入数量',
						type: 'warning'
					})
					return
				}
				if (this.sort === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入排序',
						type: 'warning'
					})
					return
				}
				if (this.titlesTime == '添加预约时间') {
					var url = 'serviceTime/insertServiceTime'
				} else {
					var url = 'serviceTime/updateServiceTime'
				}
				var serviceTime = this.startTime + '~' + this.endTime
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'companyId': this.shopId,
						'serviceTimeId': this.serviceTimeId,
						'num': this.num,
						'serviceTime': serviceTime,
						'sort': this.sort
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
								this.dialogFormVisibleTimeAU = false
								this.yuyueTimeSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 删除预约时间
			deleteTime(row) {
				this.$confirm(`确定要删除预约时间吗`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('serviceTime/deleteServiceTime'),
						method: 'get',
						params: this.$http.adornParams({
							'serviceTimeId': row.serviceTimeId
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
									this.yuyueTimeSelect()
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
			// 员工排行
			shopsListPh(row) {
				this.$router.push({
					path: '/yuangongtop',
					query: {
						shopId: row.userId
					}
				})
			},
			// 状态
			changeS(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
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
			},
			// 商家推荐
			change2(val, row) {
				this.$http({
					url: this.$http.adornUrl('userCertification/updateCertification'),
					method: 'post',
					data: this.$http.adornData({
						'id':row.id,
						'isRecommend':row.isRecommend
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}else{
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
			},
		},
		mounted() {
			this.dataSelect()
			// this.dataSelect2()
		}
	}
</script>

<style lang="scss" scoped>
.shops-list-page {
	padding: 0;
	overflow-x: hidden;
}

.shops-list-page__hero {
	margin-bottom: 12px;
}

.shops-list-page__filter {
	margin-bottom: 12px;
}

.shops-list-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.shops-list-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shops-list-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.shops-list-page__input {
	width: 200px;
}

.shops-list-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shops-list-page__tabs-wrap {
	margin-bottom: 0;
}

.shops-list-page__tabs {
	margin-bottom: 0;
}

.shops-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.shops-list-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.shops-list-page__table-card >>> .el-table {
	border-radius: 0;
}

.shops-list-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.shops-list-page__table >>> .el-table__body td {
	background: #fff;
}

.shops-list-page__link {
	color: #01af74;
	cursor: pointer;
}

.shops-list-page__pagination {
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

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}

	.divs img {
		width: 100%;
	}
</style>
