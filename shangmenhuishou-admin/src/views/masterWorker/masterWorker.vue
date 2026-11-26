<template>
	<div class="master-worker-page">
		<div class="nf-page-hero master-worker-page__hero">
			<span class="nf-page-hero__badge">师傅管理</span>
			<h2 class="nf-page-hero__title">师傅列表</h2>
			<p class="nf-page-hero__desc">管理已认证师傅信息，支持按姓名与电话筛选，可查看服务、订单、收益与评价明细</p>
		</div>

		<div class="nf-filter-card master-worker-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body master-worker-page__filter-body">
				<div class="master-worker-page__field">
					<span class="master-worker-page__label">姓名：</span>
					<el-input class="master-worker-page__input" @keydown.enter.native="shopSelect" placeholder="请输入姓名"
						v-model="shopName1">
					</el-input>
				</div>
				<div class="master-worker-page__field">
					<span class="master-worker-page__label">电话：</span>
					<el-input class="master-worker-page__input" @keydown.enter.native="shopSelect" placeholder="请输入电话"
						v-model="region">
					</el-input>
				</div>
				<div class="master-worker-page__actions">
					<el-button size="mini" type="primary" icon="el-icon-search" @click="shopSelect">查询</el-button>
					<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
				</div>
			</div>
		</div>

		<div class="master-worker-page__tabs-wrap">
			<el-tabs v-model="activeName" class="master-worker-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="师傅列表" name="first">
					<div class="nf-table-card master-worker-page__table-card">
						<el-table v-loading="tableDataLoading" :data="typeDatas.list" class="master-worker-page__table">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="name" label="姓名" width="100">
					<template slot-scope="scope">
						<span class="master-worker-page__link"
							@click="updates(scope.row)">{{scope.row.name ? scope.row.name : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="isRecommend" label="推荐" width="100">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.isRecommend"
							@change="change2(scope.row.isRecommend,scope.row)" :active-value="openValue1"
							:disabled="!isAuth('shopsList:update')" :inactive-value="closeValue1"
							active-color="#13ce66" inactive-color="#ff4949">
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
				<el-table-column prop="addressImg" label="师傅资质" width="250">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.addressImg">
							<div v-for="(item,index) in scope.row.addressImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
									<img style="width: 200px; height: auto;" :src="item" alt="">
								</el-popover>
							</div>
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
						<el-switch v-model="scope.row.onLine" @change="changeS(scope.row.onLine,scope.row.id)"
							:active-value="openValue" :disabled="!isAuth('userList:delete')"
							:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<!-- <el-table-column label="商家公告" prop="shopNotice" width="150">
				</el-table-column> -->
				<el-table-column label="操作" width="230" fixed="right">
					<template slot-scope="scope">
						
						<el-button size="mini" type="primary" @click="shopsListFw(scope.row)" style="margin: 5px;">
							服务列表
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListRw(scope.row)" style="margin: 5px;">
							任务订单
						</el-button>
						<el-button :disabled="!isAuth('shopsList:update')" size="mini" type="primary"
							@click="updatesShop(scope.$index, scope.row)" style="margin: 5px;">修改信息
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListSr(scope.row)" style="margin: 5px;">收益统计
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListQb(scope.row)" style="margin: 5px;">钱包信息
						</el-button>
						<el-button size="mini" type="primary" @click="shopsListYg(scope.row)" style="margin: 5px;">评价明细
						</el-button>
						
					</template>
				</el-table-column>
						</el-table>
						<div class="master-worker-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10,  20, 30, 50, 100]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="typeDatas.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 修改信息 -->
		<el-dialog :visible.sync="dialogFormVisible" center :title="titles">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
				<el-input v-model="name" placeholder="请输入姓名" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
				<el-input v-model="phone" placeholder="请输入手机号" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证号：</span>
				<el-input v-model="idNumber" placeholder="请输入身份证号" style="width:50%;"></el-input>
			</div>
			
			<div style="display: flex;align-items: center;margin: 2% 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="avatar"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess3">
						<img v-if="avatar!=''&&avatar!=null" :src="avatar" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="display: flex;align-items: center;margin: 2% 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证正面照：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="cardFront"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="cardFront!=''&&cardFront!=null" :src="cardFront" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="display: flex;align-items: center;margin: 2% 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证背面照：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="cardBack"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess2">
						<img v-if="cardBack!=''&&cardBack!=null" :src="cardBack" class="avatar" style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">师傅资质：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in addressImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="clear1(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrl('file/upload')"
							list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess3"
							:on-progress="onprogress1">
							<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
								:percentage="percentage1"></el-progress>
							<i v-else class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
				
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
				<span class="bqList" v-for="(item,index) in label" :key="index">{{item}}
					<span class="delss">
						<i class="el-icon-delete" @click="dels1(index)"></i>
					</span>
				</span>
				<el-input v-model="bq" style="width:200px;" placeholder="请输入标签"></el-input>
				<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
				</el-button>
				
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">年龄：</span>
				<el-input v-model="age" placeholder="请输入年龄" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务次数：</span>
				<el-input v-model="serviceNum" placeholder="请输入服务次数" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">评分：</span>
				<el-input v-model="score" placeholder="请输入评分" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">个人介绍：</span>
				<el-input v-model="details" type="textarea" :rows="3" placeholder="请输入个人介绍" style="width:50%;"></el-input>
			</div>
			
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">地址：</span>
				<el-input v-model="nowAddress"  placeholder="请输入地址"
					type="text" style="width:50%;"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="onLine">
					<el-radio :label="1">上线</el-radio>
					<el-radio :label="2">下线</el-radio>
				</el-radio-group>
			</div> -->
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
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
			<!-- <el-button style="margin-left:15px;"  size="mini" type="primary"
        icon="document" @click="closes2()">确定</el-button> -->
			<el-table v-loading="tableDataLoading11" :data="userData.list" :row-key="(row)=>{ return row.id}">
				<!-- <el-table-column :reserve-selection="true" type="selection" fixed>
        </el-table-column> -->
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

		<!-- 评价列表 -->
		<el-dialog title="评价列表" :visible.sync="dialogFormVisibleHd" center width="70%">
			
			<el-table v-loading="tableDataLoading" :data="huodongData.records">
				<el-table-column prop="evaluateId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="评论用户" width="120">
					<template slot-scope="scope">
						　<span style="color: #01af74;cursor: pointer;" @click="updatesvideo(scope.row)"
							v-text="scope.row.userName"></span>
					</template>
				</el-table-column>
				<el-table-column prop="evaluateMessage" label="评价内容">
				</el-table-column>
				<el-table-column prop="evaluateImg" label="评论图片">
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
				<el-table-column prop="satisfactionFlag" label="评分" width="160">
					<template slot-scope="scope">
						<span style="color: #01af74;cursor: pointer;"
							v-if="scope.row.satisfactionFlag==1">非常差</span>
						<span style="color: #01af74;cursor: pointer;" v-if="scope.row.satisfactionFlag==2">差</span>
						<span style="color: #01af74;cursor: pointer;" v-if="scope.row.satisfactionFlag==3">一般</span>
						<span style="color: #01af74;cursor: pointer;" v-if="scope.row.satisfactionFlag==4">满意</span>
						<span style="color: #01af74;cursor: pointer;"
							v-if="scope.row.satisfactionFlag==5">非常满意</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="评价时间" width="160">
				</el-table-column>
				<el-table-column prop="status" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('shopsList:delete')"
							@click="deleteuser(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 15, 20, 25]" :page-size="limitH" :current-page="pageH"
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
				      start: '08:30',
				      step: '00:15',
				      end: '18:30'
				    }">
				</el-time-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">结束时间：</span>

				<el-time-select placeholder="结束时间" v-model="endTime" :picker-options="{
				      start: '08:30',
				      step: '00:15',
				      end: '18:30',
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
		<!-- 服务列表 -->
		<el-dialog title="服务列表" :visible.sync="dialogFormVisibleFwddd" center width="70%">
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="listSelect">刷新
			</el-button>
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="addSelect">添加
			</el-button>
			<el-table v-loading="tableDataLoadingHd" :data="rwData">
				<el-table-column prop="masterServiceId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="fixedClassify.classifyName" label="分类名称">
				</el-table-column>
				<el-table-column prop="img" label="分类图片">
					<template slot-scope="scope">
						　<img v-if="scope.row.fixedClassify &&scope.row.fixedClassify.img!=''" :src="scope.row.fixedClassify.img" width="40" height="40" />
						<span v-else>暂无图片</span>
					</template>
				</el-table-column>
				<el-table-column prop="fixedClassify.money" label="一口价价格">
				</el-table-column>
				<el-table-column prop="fixedClassify.minOfferMoney" label="悬赏最低价">
				</el-table-column>
				<el-table-column prop="fixedClassify.minQuoteMoney" label="报价最低价">
				</el-table-column>
				<el-table-column prop="fixedClassify.minMasterMoney" label="师傅任务最低价">
				</el-table-column>
				<el-table-column prop="fixedClassify.updateTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
					<template slot-scope="scope">
						<el-button size="mini" type="warning" :disabled="!isAuth('classification:update')"
							@click="classDelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!-- <div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeFw" @current-change="handleCurrentChangeFw"
					:page-sizes="[10, 15, 20, 25]" :page-size="limitH" :current-page="pageH"
					layout="total,sizes, prev, pager, next" :total="rwData.totalCount">
				</el-pagination>
			</div> -->
		</el-dialog>
		<!-- 任务订单 -->
		<el-dialog title="任务订单" :visible.sync="dialogFormVisibleRwddd" center width="70%">
			<div style="display: inline-block;margin: 3px;">
				<span>服务类型：</span>
				<el-select v-model="taskTypeT" style="width:150px;margin-left: 10px;" @change="selectR()">
					<el-option v-for="item in parentIdList" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin: 3px;">
				<span>状态：</span>
				<el-select v-model="status1" style="width:150px;margin-left: 10px;" @change="selectR(status1)">
					<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin: 3px;">
				<span>服务分类：</span>
				<el-cascader style="width:150px;" v-model="gameIdsVal" :options="homeData1" placeholder="请选择服务分类"
					:show-all-levels="false"
					:props="{ expandTrigger: 'hover', value:'classifyId', label:'classifyName'}" @change="handleChange2"
					filterable>
				</el-cascader>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin: 3px;">
				<div style="position: relative;display: inline-block;">
					<span>手机号：</span>
					<el-input style="width: 200px;" @keydown.enter.native="selectR" placeholder="请输入手机号"
						v-model="phoneR"></el-input>&nbsp;&nbsp;
				</div>
			</div>
			<div style="display: inline-block;margin: 3px;">
				<div style="position: relative;display: inline-block;">
					<span>内容：</span>
					<el-input style="width: 200px;" @keydown.enter.native="selectR" placeholder="请输入内容"
						v-model="content1"></el-input>&nbsp;&nbsp;
				</div>
			</div>
			
			<div style="display: inline-block;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="selectR">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansR">重置
				</el-button>
			</div>
			<el-table v-loading="tableDataLoadingHd" :data="fuwuData.list">
				<el-table-column fixed prop="helpOrderId" label="编号" width="80"></el-table-column>
				<el-table-column fixed prop="userName" label="发单用户" width="100">
					<template slot-scope="scope">
						<span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.userId)">
							{{ scope.row.userName ? scope.row.userName : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="task_type" label="服务类型">
					<template slot-scope="scope">
						<span v-if="scope.row.task_type == 1">悬赏任务</span>
						<span v-if="scope.row.task_type == 2">报价任务</span>
						<span v-if="scope.row.task_type == 3">一口价任务</span>
						<span v-if="scope.row.task_type == 4">师傅任务</span>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="serviceName" label="服务分类"></el-table-column> -->
				<el-table-column prop="name" label="下单人姓名" width="100"></el-table-column>
				<el-table-column prop="phone" label="下单人手机号" width="120"></el-table-column>
				<el-table-column prop="province" label="下单人地址" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.province}}{{scope.row.city}}{{scope.row.district}}{{scope.row.detailsAddress}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="提交内容"></el-table-column>
				<el-table-column prop="money" label="接单价格" width="100"></el-table-column>
				
				<el-table-column prop="startTime" label="服务开始时间"></el-table-column>
				<el-table-column prop="endTime" label="服务结束时间"></el-table-column>
				<el-table-column prop="startTaskAddress" label="服务开始地址"></el-table-column>
				<el-table-column prop="endTaskAddress" label="服务结束地址"></el-table-column>
				<el-table-column prop="startTime" label="服务开始时间"></el-table-column>
				<el-table-column prop="endTime" label="服务结束时间"></el-table-column>
				
				<el-table-column prop="startTaskImg" label="服务开始前图片" width="180">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.startTaskImg">
							<div v-for="(item,index) in scope.row.startTaskImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px;height:auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="endTaskImg" label="服务结束后图片" width="180">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.endTaskImg">
							<div v-for="(item,index) in scope.row.endTaskImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px;height:auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="startAddress" label="服务开始地址" width="200"></el-table-column>
				<el-table-column prop="endAddress" label="服务结束地址" width="200"></el-table-column>
				<el-table-column prop="startTaskLongitude" label="服务开始经度"></el-table-column>
				<el-table-column prop="startTaskLatitude" label="服务开始纬度"></el-table-column>
				<el-table-column prop="endTaskLongitude" label="服务结束经度"></el-table-column>
				<el-table-column prop="endTaskLatitude" label="服务结束纬度"></el-table-column>
				<el-table-column prop="code" label="确认码"></el-table-column>
				<el-table-column prop="serviceTime" label="预约时间" fixed="right" width="80">
					<template slot-scope="scope">
						<span style="color: red;font-weight: 600;">{{scope.row.deliveryTime}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="remarks" label="订单备注" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.remarks ? scope.row.remarks : '未填写'}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="status" label="状态" width="100">
					<template slot-scope="scope">
						<span style="color: #01af74;" v-if="scope.row.status === 1 ">待服务</span>
						<span style="color: #01af74;" v-if="scope.row.status === 2 ">服务中</span>
						<span style="color: #01af74;" v-if="scope.row.status === 3 ">已完成</span>
						<span style="color: #999;" v-if="scope.row.status === 4 ">已取消</span>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeRw" @current-change="handleCurrentChangeRw"
					:page-sizes="[10, 15, 20, 25]" :page-size="limitH" :current-page="pageH"
					layout="total,sizes, prev, pager, next" :total="fuwuData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 任务分类弹框 -->
		<el-dialog title="任务分类" :visible.sync="dialogFormVisibleRwfl" center width="65%">
			<el-table v-loading="tableDataLoading" :data="tableDataFl" row-key="classifyId" default-expand-all>
				<el-table-column prop="classifyId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="classifyName" label="分类名称">
				</el-table-column>
				<el-table-column prop="img" label="分类图片">
					<template slot-scope="scope">
						　<img v-if="scope.row.img &&scope.row.img!=''" :src="scope.row.img" width="40" height="40" />
						<span v-else>暂无图片</span>
					</template>
				</el-table-column>
				<el-table-column prop="money" label="一口价价格">
				</el-table-column>
				<el-table-column prop="minOfferMoney" label="悬赏最低价">
				</el-table-column>
				<el-table-column prop="minQuoteMoney" label="报价最低价">
				</el-table-column>
				<el-table-column prop="minMasterMoney" label="师傅任务最低价">
				</el-table-column>
				
				<el-table-column prop="isEnable" label="状态">
					<template slot-scope="scope">
						<span v-if="scope.row.isEnable==1">上架</span>
						<span v-else>下架</span>
					</template>
				</el-table-column>
				<el-table-column prop="updateTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('classification:update')" v-if="scope.row.parentId!=0"
							@click="classAdd(scope.row)">添加
						</el-button>
					</template>
				</el-table-column>
			</el-table>
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
				phoneU:'',
				phone: '',
				nickName: '',
				limit: 10,
				page: 1,
				size11: 10,
				page11: 1,
				shopName1: '',
				isActivity: '',
				region: '',
				shopId: '',
				userIdList: '',
				name: '',
				avatar:'',
				idNumber:'',
				cardFront: '',
				cardBack:'',
				addressImg:[],
				label:[],
				bq:'',
				age:'',
				serviceNum:'',
				details:'',
				score:'',
				
				userData: [], //用户集合
				openTime: '',
				shopRate:'',
				closeTime: '',
				address:'',
				nowAddress: '',
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
				onLine: 1,
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
				limitH: 10,
				pageH: 1,
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
				dialogFormVisibleFwddd:false,
				dialogFormVisibleRwddd:false,
				fuwuData:{},
				rwData:[],
				gameIds: '',
				gameIdsVal: [],
				homeData1: [],
				taskTypeT: '',
				phoneR:'',
				status1: '',
				content1:'',
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
				statesnum: [{
						label: '全部',
						value: ''
					},
					
					{
						label: '待服务',
						value:1
					},
					{
						label: '进行中',
						value: 2
					},
					{
						label: '已完成',
						value: 3
					},
					{
						label: '已取消',
						value: 4
					},
				],
				dialogFormVisibleRwfl:false,
				tableDataFl: [],
				openValue1: 1,
				closeValue1: 2,
				percentage1:0,
			}
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},


			// tabs切换
			handleClick(tab, event) {
				this.page = 1
				this.limit = 10
				if (tab._props.label == '师傅列表') {

					this.dataSelect()
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
			
			// 图标上传
			handleAvatarSuccess(file) {
				this.img = file.data.path
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
						'type': '',
						'isRecommend':-1
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
			// 重置
			cleans11() {
				this.page11 = 1
				this.phoneU = ''
				this.nickName = ''
				this.dataSelect11()
			},
			// 修改信息
			releasNoticeTo() {
				if (this.avatar == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传头像',
						type: 'warning'
					})
					return
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
				if (this.phone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入手机号',
						type: 'warning'
					})
					return
				}
				if (this.idNumber == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入身份证号',
						type: 'warning'
					})
					return
				}
				if (this.age === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入年龄',
						type: 'warning'
					})
					return
				}
				if (this.serviceNum === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入服务次数',
						type: 'warning'
					})
					return
				}
				if (this.score === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入评分',
						type: 'warning'
					})
					return
				}
				
				if (this.nowAddress == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入地址',
						type: 'warning'
					})
					return
				}
				if (this.details == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入个人介绍',
						type: 'warning'
					})
					return
				}
				if(this.label.length==0){
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '标签不能为空',
						type: 'warning'
					});
					return
				}
				var datas = {}
				datas.id = this.shopId
				datas.name = this.name
				datas.avatar = this.avatar
				datas.idNumber = this.idNumber
				datas.phone = this.phone
				datas.serviceNum = this.serviceNum
				datas.score = this.score
				datas.endTime = this.closeTime
				datas.nowAddress = this.nowAddress
				datas.details = this.details
				datas.front = this.cardFront
				datas.back = this.cardBack
				datas.userId = this.shopUserId;
				datas.age = this.age;
				datas.label = this.label.toString()
				datas.addressImg = this.addressImg.toString()
				this.$http({
					url: this.$http.adornUrl('userCertification/updateCertification'),
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
				this.shopId = rows.id
				this.name = rows.name
				
				this.phone = rows.phone
				this.idNumber = rows.idNumber
				this.onLine = rows.onLine
				if (rows.label) {
					this.label = rows.label.split(',')
				}else{
					this.label = []
				}
				this.nowAddress = rows.nowAddress
				if(rows.avatar){
					this.avatar = rows.avatar
				}else{
					this.avatar = ''
				}
				if(rows.front){
					this.cardFront = rows.front
				}else{
					this.cardFront = ''
				}
				if(rows.back){
					this.cardBack = rows.back
				}else{
					this.cardBack = ''
				}
				if (rows.addressImg) {
					this.addressImg = rows.addressImg.split(',')
				}else{
					this.addressImg = []
				}
				
				this.age = rows.age;
				this.serviceNum = rows.serviceNum;
				this.score = rows.score;
				this.details = rows.details
				this.shopUserId = rows.userId
				this.dialogFormVisible = true

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
			shopsListFw(row) {
				this.pageH = 1
				this.shopId = row.userId
				this.dialogFormVisibleFwddd = true
				this.fuwuOrderSelect()
			},
			// 任务订单
			shopsListRw(row) {
				this.pageH = 1
				this.shopId = row.userId
				this.dialogFormVisibleRwddd = true
				this.renwuOrderSelect()
				this.homeSelect()
			},
			// 评价明细
			shopsListYg(row) {
				this.shopId = row.userId
				this.activityId = row.activityId
				this.dialogFormVisibleHd = true
				this.huodongSelect()
			},
			// 收入统计
			shopsListSr(row) {
				this.$router.push({
					path: '/masterWorkerDatas',
					query: {
						shopId: row.userId
					}
				})
			},
			
			// 钱包信息
			shopsListQb(row) {
				this.$router.push({
					path: '/masterWorkerWallet',
					query: {
						shopId: row.userId
					}
				})
			},

			// 获取师傅数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('userCertification/queryCertification'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': 1,
						'name': this.shopName1,
						'classify': 1,
						'phone':this.region,
						'isRecommend':-1,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
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
			// 身份证正面照
			handleAvatarSuccess1(file, fileList) {
				this.cardFront = file.data.path
			},
			// 身份证背面照
			handleAvatarSuccess2(file, fileList) {
				this.cardBack = file.data.path
			},
			// 头像
			handleAvatarSuccess3(file, fileList) {
				this.avatar = file.data.path
			},
			handleUploadSuccess3(file, fileList) {
				this.addressImg.push(file.data.path);
			},
			// 删除
			clear1(index) {
				this.addressImg.splice(index, 1);
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
				this.isActivity = ''
				this.dataSelect()
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
					url: this.$http.adornUrl('admin/evaluate/getUserEvaluate'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'riderUserId':this.shopId
					})
					// url: this.$http.adornUrl('user/getWorkerUserList'),
					// method: 'get',
					// params: this.$http.adornParams({
					// 	'page': this.page,
					// 	'limit': this.limit,
					// 	'companyId': this.shopId
					// })
				}).then(({
					data
				}) => {
					this.tableDataLoadingHd = false
					let returnData = data.data
					this.huodongData = returnData
				})
			},
			selectHd() {
				this.pageH = 1
				this.huodongSelect()
			},
			cleansHs() {
				this.pageH = 1
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
			
			// 状态
			changeS(onLine, userId) {
				this.$http({
					url: this.$http.adornUrl('userCertification/updateCertification'),
					method: 'post',
					data: this.$http.adornData({
						'onLine':onLine,
						'id':userId
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
					    type: 'error',
					    duration: 1500,
					    onClose: () => {
							 this.dataSelect()
						}
					  })
					}
				})
			},
			// 获取服务列表
			fuwuOrderSelect() {
				this.tableDataLoadingHd = true
				this.$http({
					url: this.$http.adornUrl('masterService/selectMasterServiceByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageH,
						'limit': this.limitH,
						'userId':this.shopId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingHd = false
					let returnData = data.data
					this.rwData = returnData
				})
			},
			handleSizeChangeFw(val) {
				this.limitH = val
				this.fuwuOrderSelect()
			},
			handleCurrentChangeFw(val) {
				this.pageH = val
				this.fuwuOrderSelect()
			},
			// 获取任务订单
			renwuOrderSelect() {
				this.tableDataLoadingHd = true
				this.$http({
					url: this.$http.adornUrl('help/selectRunHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageH,
						'limit': this.limitH,
						'status': this.status1,
						'userId':this.shopId,
						'content':this.content1,
						'phone':this.phoneR,
						'gameId':this.gameIds,
						'taskType':this.taskTypeT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoadingHd = false
					let returnData = data.data
					this.fuwuData = returnData
				})
			},
			handleSizeChangeRw(val) {
				this.limitH = val
				this.renwuOrderSelect()
			},
			handleCurrentChangeRw(val) {
				this.pageH = val
				this.renwuOrderSelect()
			},
			// 查询
			selectR() {
				this.pageH = 1
				this.renwuOrderSelect()
			},
			// 重置
			cleansR() {
				this.pageH = 1
				this.status1 = ''
				this.content1 = ''
				this.taskTypeT = ''
				this.phoneR = ''
				this.gameIds = ''
				this.gameIdsVal = [],
				this.renwuOrderSelect()
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
			// 获取任务分类列表
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
			handleChange2(value) {
				console.log('value', value)
				if (value.length == 2) {
					this.gameIds = value[1];
				}
				this.renwuOrderSelect()
			
			},
			// 任务分类弹框
			addSelect(){
				this.classSelect()
				this.dialogFormVisibleRwfl = true
			},
			// 获取服务分类数据
			classSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/fixedClassify/getTreeClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'classifyName':'',
						'userId':this.shopId
					})
				}).then(({
					data
				}) => {
					
						this.tableDataLoading = false
						let returnData = data.data
						this.tableDataFl = returnData
			
				})
			},
			listSelect(){
				this.fuwuOrderSelect()
			},
			// 添加任务分类
			classAdd(row){
				this.$http({
					url: this.$http.adornUrl('masterService/insertMasterService'),
					method: 'post',
					data: this.$http.adornData({
						'onLine':'',
						'masterUserId':this.shopId,
						'classifyId':row.classifyId
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleRwfl = false
					  this.$message({
					    message: '操作成功',
					    type: 'success',
					    duration: 1500,
					    onClose: () => {
					      this.fuwuOrderSelect()
					    }
					  })
					} else {
					  this.$message({
					    message: data.msg,
					    type: 'error',
					    duration: 1500,
					    onClose: () => {
							 this.fuwuOrderSelect()
						}
					  })
					}
				})
			},
			// 删除服务
			classDelete(row){
				this.$confirm(`确定要删除该服务吗`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('masterService/deleteMasterService'),
						method: 'post',
						params: this.$http.adornParams({
							'masterServiceId': row.masterServiceId
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
									this.fuwuOrderSelect()
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
			// 师傅推荐
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
.master-worker-page {
	padding: 0;
	overflow-x: hidden;
}

.master-worker-page__hero {
	margin-bottom: 12px;
}

.master-worker-page__filter {
	margin-bottom: 12px;
}

.master-worker-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.master-worker-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.master-worker-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.master-worker-page__input {
	width: 200px;
}

.master-worker-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.master-worker-page__tabs-wrap {
	margin-bottom: 0;
}

.master-worker-page__tabs {
	margin-bottom: 0;
}

.master-worker-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.master-worker-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.master-worker-page__table-card >>> .el-table {
	border-radius: 0;
}

.master-worker-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.master-worker-page__table >>> .el-table__body td {
	background: #fff;
}

.master-worker-page__link {
	color: #01af74;
	cursor: pointer;
}

.master-worker-page__pagination {
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
