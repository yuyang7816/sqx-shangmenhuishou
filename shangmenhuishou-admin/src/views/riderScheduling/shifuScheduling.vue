<template>
  <div class="rider-scheduling-page">
    <div class="nf-page-hero rider-scheduling-page__hero">
      <span class="nf-page-hero__badge">员工调度</span>
      <h2 class="nf-page-hero__title">员工调度</h2>
      <p class="nf-page-hero__desc">在地图上查看员工实时位置与工作状态，点击地图标记可查看待服务订单并指派</p>
    </div>

    <div class="nf-filter-card rider-scheduling-page__filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-s-operation"></i>
        <span>快捷操作</span>
      </div>
      <div class="nf-filter-card__body rider-scheduling-page__filter-body">
        <a href="#" class="rider-scheduling-page__back" @click.prevent="prev">
          <icon-svg name="jiantou" class="rider-scheduling-page__back-icon"></icon-svg>
          返回
        </a>
        <div class="rider-scheduling-page__actions">
          <el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="handleChange55">刷新</el-button>
        </div>
      </div>
    </div>

    <div class="nf-table-card rider-scheduling-page__map-card">
      <div id="container" class="rider-scheduling-page__map" v-loading="tableDataLoadingDt"></div>
    </div>

    <!-- 待服务订单 -->
    <el-dialog title="待服务订单" :visible.sync="dialogFormVisible2" center width="70%" style="z-index: 1000;">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <!-- 待服务订单 -->
        <el-tab-pane label="服务订单" name="first">
          <div style="display: inline-block;text-align: right;">
            <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="animeDat">刷新
            </el-button>
            <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="zhipaiDet">
              指派订单
            </el-button>
          </div>
          <el-table v-loading="tableDataLoading" :data="tableData.records">
            <el-table-column prop="ordersId" label="编号" width="80">
            </el-table-column>
            <el-table-column prop="ordersNo" label="订单编号" width="150">
            </el-table-column>
            <el-table-column prop="myLevel" label="标题" width="150">
            </el-table-column>
            <el-table-column prop="detailsAddress" label="服务信息" width="200">
              <template slot-scope="scope">
                <div>联系人姓名：{{scope.row.name}}</div>
                <div>联系人联系：{{scope.row.phone}}</div>
                <div>服务地址：{{scope.row.province}}{{scope.row.city}}{{scope.row.detailsAddress}}
                  {{scope.row.district}}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="startTime" label="上门时间" width="100">
            </el-table-column>
            <el-table-column prop="remarks" label="备注" width="200">
            </el-table-column>
            <el-table-column prop="oldMoney" label="价格" width="100">
            </el-table-column>
            <el-table-column prop="payMoney" label="支付金额">
              <template slot-scope="scope">
                <span style="color: #f56c6c;">{{scope.row.payMoney}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="orderNumber" label="时长" width="100">
            </el-table-column>
            <el-table-column prop="unit" label="单位" width="100">
            </el-table-column>

            <el-table-column prop="code" label="确认码">
            </el-table-column>
            <el-table-column prop="state" label="订单状态" fixed="right" width="80">
              <template slot-scope="scope">
                <span style="color: #f56c6c;" v-if="scope.row.state==0">待支付</span>
                <span style="color: #E87D0D;" v-if="scope.row.state==1">进行中</span>
                <span style="color: #009900;" v-if="scope.row.state==2">已完成</span>
                <span style="color: #999;" v-if="scope.row.state==3">已退款</span>
                <span style="color: #999;" v-if="scope.row.state==4">待服务</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="160">
            </el-table-column>
          </el-table>
          <div style="text-align: center;margin-top: 10px;">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
              layout="total,sizes, prev, pager, next" :total="tableData.total">
            </el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane label="任务订单" name="daili">
          <div style="display: inline-block;text-align: right;">
            <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="animeDatRw">刷新
            </el-button>
            <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="zhipaiDetRw">
              指派订单
            </el-button>
          </div>
          <el-table v-loading="tableDataLoading" :data="tableData.records">
            <el-table-column fixed prop="helpOrderId" label="编号" width="80"></el-table-column>
            <el-table-column fixed prop="userName" label="发单用户" width="100">
              <template slot-scope="scope">
                <span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.userId)">
                  {{ scope.row.userName ? scope.row.userName : '未绑定' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="taskType" label="服务类型">
              <template slot-scope="scope">
                <span v-if="scope.row.taskType == 1">悬赏任务</span>
                <span v-if="scope.row.taskType == 2">报价任务</span>
                <span v-if="scope.row.taskType == 3">一口价任务</span>
              </template>
            </el-table-column>
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
                      <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
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
                      <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="" slot="reference">
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

            </el-table-column>
            <el-table-column prop="remarks" label="订单备注" width="200">

            </el-table-column>
            <el-table-column fixed="right" prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <span style="color: #01af74;" v-if="scope.row.status === 1 ">待服务</span>
                <span style="color: #01af74;" v-if="scope.row.status === 2 ">服务中</span>
                <span style="color: #01af74;" v-if="scope.row.status === 3 ">已完成</span>
                <span style="color: #999;" v-if="scope.row.status === 4 ">已取消</span>
              </template>
            </el-table-column>
            </el-table-column>
          </el-table>
          <div style="text-align: center;margin-top: 10px;">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
              layout="total,sizes, prev, pager, next" :total="tableData.total">
            </el-pagination>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <!-- 服务订单列表 -->
    <el-dialog title="服务订单列表" :visible.sync="dialogFormVisible" center width="80%">
      <div style="display: inline-block;">
        <div style="position: relative;display: inline-block;margin: 3px;">
          <span>订单类型：</span>
          <el-select v-model="typeFw" style="width:150px;margin-left: 10px;" @change="selectFw()">
            <el-option v-for="item in typeNum" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>&nbsp;&nbsp;&nbsp;
        </div>

        <div style="position: relative;display: inline-block;margin: 3px;">
          <span>下单人用户名：</span>
          <el-input style="width: 200px;" @keydown.enter.native="selectFw" placeholder="请输入下单人用户名"
            v-model="userNameFw"></el-input>&nbsp;&nbsp;
        </div>
        <div style="position: relative;display: inline-block;margin: 3px;">
          <span>订单编号：</span>
          <el-input style="width: 200px;" @keydown.enter.native="selectFw" placeholder="请输入订单编号"
            v-model="ordersNoFw"></el-input>&nbsp;&nbsp;
        </div>
        <div style="margin:5px;display: inline-block;">
          <span>开始时间：</span>
          <el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTimeFw" align="right" type="datetime"
            format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
          </el-date-picker>&nbsp;&nbsp;&nbsp;
        </div>
        <div style="margin:5px;display: inline-block;">
          <span>截止时间：</span>
          <el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTimeFw" align="right" type="datetime"
            format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
          </el-date-picker>
        </div>
        <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="selectFw">查询
        </el-button>
        <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansFw">重置
        </el-button>
      </div>
      <el-table v-loading="tableDataLoadingU" :data="userData.list">
        <el-table-column prop="ordersId" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="riderName" label="接单员工">
          <template slot-scope="scope">
            <span v-if="scope.row.state==0"> - </span>
            <span v-else style="color: #009900;">{{ scope.row.riderName?scope.row.riderName:'未绑定' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="下单用户">
          <template slot-scope="scope">
            <span style="color: #008000;cursor: pointer;" @click="updates(scope.row.userId)"
              v-if="scope.row.ordersType==1">{{ scope.row.ordersUserName }}</span>
            <span style="color: #008000;cursor: pointer;" @click="updates(scope.row.userId)"
              v-if="scope.row.ordersType==2">{{ scope.row.ordersUserName }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="companyName" label="商家名称" width="150">
        </el-table-column>
        <el-table-column prop="ordersNo" label="订单编号" width="150">
        </el-table-column>
        <el-table-column prop="ordersType" label="订单类型">
          <template slot-scope="scope">
            <span v-if="scope.row.ordersType==1">服务订单</span>
            <span v-if="scope.row.ordersType==2">会员订单</span>
          </template>
        </el-table-column>
        <el-table-column prop="myLevel" label="标题" width="150">
        </el-table-column>
        <el-table-column prop="oldMoney" label="发布价格" width="100">
        </el-table-column>
        <el-table-column prop="money" label="普通用户价格" width="100">
        </el-table-column>
        <el-table-column prop="memberMoney" label="会员价格" width="100">
        </el-table-column>
        <el-table-column prop="rate" label="商家佣金">
          <template slot-scope="scope">
            <span>{{scope.row.rate?scope.row.rate:'0'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="couponMoney" label="优惠金额" width="100">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.couponMoney}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payMoney" label="支付金额">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.payMoney}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderNumber" label="时长" width="100">
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="100">
        </el-table-column>
        <el-table-column prop="detailsAddress" label="服务信息" width="200">
          <template slot-scope="scope">
            <div>联系人姓名：{{scope.row.name}}</div>
            <div>联系人联系：{{scope.row.phone}}</div>
            <div>服务地址：{{scope.row.province}}{{scope.row.city}}{{scope.row.detailsAddress}}
              {{scope.row.district}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="上门时间" width="100">
        </el-table-column>
        <el-table-column prop="remarks" label="备注" width="200">
        </el-table-column>
        <el-table-column prop="code" label="确认码">
        </el-table-column>
        <el-table-column prop="state" label="订单状态" fixed="right" width="80">
          <template slot-scope="scope">
            <span style="color: #f56c6c;" v-if="scope.row.state==0">待支付</span>
            <span style="color: #E87D0D;" v-if="scope.row.state==1">进行中</span>
            <span style="color: #009900;" v-if="scope.row.state==2">已完成</span>
            <span style="color: #999;" v-if="scope.row.state==3">已退款</span>
            <span style="color: #999;" v-if="scope.row.state==4">待服务</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
        </el-table-column>
        <el-table-column prop="createTime" fixed="right" label="操作" width="160">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('shopsList:paidan')" @click="querenBtns(scope.row)"
              style="margin: 3px;" v-if="scope.row.state === 4 ">
              派单</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChangeU" @current-change="handleCurrentChangeU"
          :page-sizes="[10, 20, 30, 40]" :page-size="limit1" :current-page="page1"
          layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
        </el-pagination>
      </div>
      </el-table>
    </el-dialog>
    <!-- 信息详情 -->
    <el-dialog title="任务订单列表" :visible.sync="dialogFormVisibleDel" center width="80%">
      <div style="display: inline-block;">
        <div style="display: inline-block;margin: 3px;">
          <span>服务类型：</span>
          <el-select v-model="taskTypeT" style="width:150px;margin-left: 10px;" @change="phoneSelect()">
            <el-option v-for="item in parentIdList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        <div style="position: relative;display: inline-block;margin: 10px 0px;">
          <span>手机号:</span>
          <el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入手机号" v-model="phoneR">
          </el-input>&nbsp;&nbsp;
        </div>
        <!-- <div style="position: relative;display: inline-block;margin: 10px 0px;">
						<span>昵称:</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入昵称"
							v-model="userName">
						</el-input>&nbsp;&nbsp;
					</div> -->
        <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="phoneSelect">查询
        </el-button>
        <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansRw">重置
        </el-button>
      </div>
      <el-table v-loading="tableDataLoadingU" :data="tableData2.list">
        <el-table-column fixed prop="helpOrderId" label="编号" width="80"></el-table-column>
        <el-table-column fixed prop="userName" label="发单用户" width="100">
          <template slot-scope="scope">
            <span style="color: #01af74;cursor: pointer;" @click="updates(scope.row.userId)">
              {{ scope.row.userName ? scope.row.userName : '未绑定' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="taskType" label="服务类型">
          <template slot-scope="scope">
            <span v-if="scope.row.taskType == 1">悬赏任务</span>
            <span v-if="scope.row.taskType == 2">报价任务</span>
            <span v-if="scope.row.taskType == 3">一口价任务</span>
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
        </el-table-column>
        <el-table-column prop="serviceTime" label="预约时间" fixed="right" width="80">
        </el-table-column>
        <el-table-column prop="remarks" label="订单备注" width="200">
        </el-table-column>
        <el-table-column fixed="right" prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <span style="color: #01af74;" v-if="scope.row.status === 1 ">待服务</span>
            <span style="color: #01af74;" v-if="scope.row.status === 2 ">服务中</span>
            <span style="color: #01af74;" v-if="scope.row.status === 3 ">已完成</span>
            <span style="color: #999;" v-if="scope.row.status === 4 ">已取消</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作" width="100" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('shopsList:paidan')"
              @click="querenBtnsRw(scope.row)" v-if="scope.row.status === 1 ">
              派单</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChangeRw" @current-change="handleCurrentChangeRw"
          :page-sizes="[10, 20, 30, 40]" :page-size="limit1" :current-page="page1"
          layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
        </el-pagination>
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
  var geocoder, map, markersArray, infoWindow = [];
  export default {
    data() {
      return {
        limit: 10,
        page: 1,
        detailedAddress: '',
        value: [],

        tableDataLoading: false,
        dialogFormVisible1: false,
        dialogFormVisible2: false,
        dialogFormVisible3: false,
        dialogFormVisible: false,
        // 地图相关
        longitude: '', //经度
        latitude: '', //维度
        loadingMap: true,
        BMap: '',
        map: '',
        geoc: '',
        keyword: '',
        showMap: false,
        pointLngLat: '',
        center: {
          lng: 121.47088, //上海121.47088  西安108.948024
          lat: 31.22100 //上海31.22100  西安34.263161
        },
        lng: 121.47088,
        lat: 31.22100,
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
        shopTypenum: [],
        typeDatas2: [],
        shopType: '',
        geometries: [],
        map: '',
        lists: [],
        index1: 0,
        tableData: {},
        hospitalId: '', //员工id
        ordersNo: '',
        userName: '',
        type: '',
        marker: '',
        infoWindowList: [],


        userData: [],
        tableDataLoadingU: false,
        limit1: 10,
        page1: 1,
        phone: '',
        campus: '',
        ordersId: '',
        pd: '',
        titles: '信息详情',
        dialogFormVisibleDel: false,
        detaDatle: {},
        datleIndex: 1,

        userNameFw: '',
        ordersNoFw: '',
        startTimeFw: '',
        endTimeFw: '',
        typeFw: '',
        typeNum: [{
            value: '',
            label: '全部'
          },
          {
            value: 1,
            label: '服务订单'
          },
          {
            value: 2,
            label: '会员订单'
          },
        ],
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
          }
        ],
        taskTypeT: '',
        phoneR: '',
        tableData2: {},
        tableDataLoadingDt: false,

      }
    },
    methods: {
      // 返回上一级
      prev() {
        this.$router.back()
      },
      handleClick(tab, event) {
        this.page = 1
        if (tab._props.label == '服务订单') {
          this.dataSelectoder()
        }
        if (tab._props.label == '任务订单') {
          this.dataSelectoderRw()
        }

      },
      handleSizeChange(val) {
        this.limit = val
        this.dataSelectoder()
      },
      handleCurrentChange(val) {
        this.page = val
        this.dataSelectoder()
      },
      handleSizeChangeU(val) {
        this.limit1 = val;
        this.dataSelectUser()()
      },
      handleCurrentChangeU(val) {
        this.page1 = val;
        this.dataSelectUser()
      },
      handleSizeChangeRw(val) {
        this.limit1 = val;
        this.taskdataSelect()()
      },
      handleCurrentChangeRw(val) {
        this.page1 = val;
        this.taskdataSelect()
      },
      //定位获得当前位置信息
      getMyLocation() {
        var geolocation = new qq.maps.Geolocation("ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75", "来点小收入");
        geolocation.getIpLocation(this.showPosition, this.showErr);


      },
      showPosition(position) {
        console.log(position.coords.latitude)
        console.log(position.coords.longitude)
        if (position.coords.latitude) {
          this.latitude = position.coords.latitude
        }
        if (position.coords.longitude) {
          this.longitude = position.coords.longitude
        }

        this.setMap();
      },
      showErr(e) {
        this.getMyLocation(); //定位失败再请求定位，测试使用
      },
      //位置信息在地图上展示
      setMap() {
        var center = new TMap.LatLng(this.latitude, this.longitude);
        if (map) {
          map.destroy();
        }
        map = []
        infoWindow = []
        // 初始化地图
        map = new TMap.Map('container', {
          zoom: 13, // 设置地图缩放级别
          pitch: 30,
          center: center, // 设置地图中心点坐标
          // mapStyleId: "style1", //个性化样式
          baseMap: {
            type: 'vector',
            // features: ['base', 'building3d'], // 隐藏矢量文字
          },
        });
        //  // var center = new TMap.LatLng('31.22100', '121.47088'); // 设置中心点坐标
        // var center = new TMap.LatLng(this.latitude, this.longitude); // 设置中心点坐标
        // var centerHeight = new TMap.LatLng(this.latitude, this.longitude); // 带高度的坐标

        // // 初始化地图
        // var map = new TMap.Map('container', {
        // 	center: center,
        // 	zoom: 13,
        // });

        // var ipLocation = new TMap.service.IPLocation();
        // var ipInput = '114.80.68.233';
        // var params = ipInput ? { ip: ipInput } : {};
        // console.log('ipLocation------',new TMap.service.IPLocation('') )
        // ipLocation
        // 	.locate(params)
        // 	.then((result2) => {
        // 		// 未给定ip地址则默认使用请求端的ip
        // 		var {
        // 			result
        // 		} = result2;
        // 		console.log('------------',result,'---',esult.location.lat,result.location.lng)

        // 		markers.updateGeometries([{
        // 			id: 'main',
        // 			position: result.location, // 将所得位置绘制在地图上
        // 		}, ]);
        // 		map.setCenter(result.location);
        // 		console.log('-------------定位成功')
        // 		document.getElementById(
        // 			'ipLocationResult'
        // 		).innerText = `您的IP/您输入的IP所在位置：${result.ad_info.nation}，${result.ad_info.province}`;
        // 	})
        // 	.catch((error) => {
        // 		console.log('-------------失败')
        // 		document.getElementById(
        // 			'ipLocationResult'
        // 		).innerText = `错误：${error.status}，${error.message}`;
        // 	});
        // 	return

        // 初始化label
        var label = new TMap.MultiLabel({
          id: 'label-layer',
          map: map,
          styles: {
            label: new TMap.LabelStyle({
              color: '#3777FF', // 颜色属性
              size: 20, // 文字大小属性
              offset: {
                x: 0,
                y: 0
              }, // 文字偏移属性单位为像素
              angle: 0, // 文字旋转属性
              alignment: 'center', // 文字水平对齐属性
              verticalAlignment: 'middle', // 文字垂直对齐属性
            }),
          },
        });
        // var center = new TMap.LatLng(this.latitude, this.longitude);
        // if (map) {
        // 	map.destroy();
        // }
        // map = []
        // infoWindow = []
        // // 初始化地图
        // map = new TMap.Map('container', {
        // 	zoom: 13, // 设置地图缩放级别
        // 	pitch: 30,
        // 	center: center, // 设置地图中心点坐标
        // 	// mapStyleId: "style1", //个性化样式
        // 	baseMap: {
        // 		type: 'vector',
        // 		features: ['base', 'building3d'], // 隐藏矢量文字
        // 	},
        // });
        // console.log('this.geometries', this.geometries)
        // // marker文字在图片内
        // /* if (this.marker) {
        // 	this.marker.setMap(null);
        // }else{

        // } */
        var QsUrl =
          "data:image/png;base64,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"
        this.marker = new TMap.MultiMarker({
          map: map,
          styles: {
            default: new TMap.MarkerStyle({
              width: 40, // 宽度
              height: 40, // 高度
              anchor: {
                x: 46,
                y: 46
              }, // 锚点位置
              src: QsUrl, // 标注点图片url或base64地址
              color: '#ff4949', // 标注点文本颜色
              size: 14, // 标注点文本文字大小
              offset: {
                x: 0,
                y: 26
              }, // 标注点文本文字基于direction方位的偏移属性
            }),
          },
          geometries: this.geometries
        });
        /* if(this.infoWindowList && this.infoWindowList.length>0){
        	for(var i in this.infoWindowList){
        		var infoWindow=this.infoWindowList[i];
        		infoWindow.close()
        	}
        } */


        //初始化infoWindow
        console.log('this.geometries', this.geometries.length)
        // infoWindow.clear();
        for (var i in this.geometries) {
          infoWindow = new TMap.InfoWindow({
            map: map,
            position: new TMap.LatLng(this.geometries[i].datas.latitude, this.geometries[i].datas
              .longitude),
            offset: {
              x: -26,
              y: -45
            } //设置信息窗相对position偏移像素
          });
          if (this.geometries[i].datas.workStatus == 1) {
            var texts = '忙碌中···'
          } else {
            var texts = '空闲中···'
          }
          // infoWindow.clears();//关闭信息窗
          // infoWindow.setContent('待服务数量：1'); //设置信息窗内容
          infoWindow.setContent('当前状态：' + texts); //设置信息窗内容
          infoWindow.open(); //初始信息窗开启
          //this.infoWindowList.push(infoWindow);
          // infoWindow.close(); //关闭信息窗
        }
        let that = this
        that.marker.on("click", function(evt) {
          that.hospitalId = evt.geometry.datas.userId
          that.dataSelectoder()
          that.dialogFormVisible2 = true
        })

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

      // 获取省市区
      handleChange55(value) {
        this.dataSelect()
      },

      dataSelect() {
        this.tableDataLoadingDt = true
        let companyId = this.$route.query.companyId
        this.$http({
          url: this.$http.adornUrl('user/getWorkerUserList'),
          method: 'get',
          params: this.$http.adornParams({
            'companyId': companyId
            // 'province': this.province,
            // 'city': this.city,
            // 'district': this.district
          })
        }).then(({
          data
        }) => {
          console.log(data)
          var arr = []
          var arr1 = []
          this.geometries = []
          this.lists = []
          if (data.data.records.length > 0) {

            for (var i in data.data.records) {
              if (data.data.records[i].latitude && data.data.records[i].longitude) {
                var obj = {}
                obj.styleId = 'big'
                obj.position = new TMap.LatLng(data.data.records[i].latitude, data.data.records[i]
                  .longitude)
                obj.datas = data.data.records[i]
                obj.content = data.data.records[i].userName
                // obj['position'] = new TMap.LatLng(data.data.records[i].latitude, data.data
                // 	.records[i].hospitalLng)
                // obj['datas'] = data.data.records[i]
                obj['img'] = data.data.records[i].avatar
                // obj['content'] = data.data.records[i].userName
                // arr.push(new TMap.LatLng(data.data.records[i].latitude, data.data.records[i]
                // 	.hospitalLng))
                arr.push(obj)
                arr1.push(obj)
                this.longitude = data.data.records[i].longitude
                this.latitude = data.data.records[i].latitude
              }

            }

            this.geometries = arr
            this.lists = arr1
            this.setMap();

          } else {
            this.longitude = this.lng
            this.latitude = this.lat
            this.$message({
              message: '暂无数据',
              type: 'warning',
              duration: 1500,
              onClose: () => {}
            })
            this.setMap();
          }
          this.tableDataLoadingDt = false
        })
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
      // 刷新事件
      animeDat(state) {
        this.page = 1
        this.dataSelectoder()
      },
      // 获取员工 服务 待服务订单
      dataSelectoder() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('orders/getStaffOrderList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'userId': this.hospitalId
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData

        })
      },
      // 指派订单弹框
      zhipaiDet(row, pd) {
        // this.ordersId = row.ordersId

        this.dataSelectUser()
        this.dialogFormVisible = true
      },
      // 获取服务订单数据列表
      dataSelectUser() {
        this.tableDataLoadingU = true
        let companyId = this.$route.query.companyId
        this.$http({
          url: this.$http.adornUrl('orders/queryOrders'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page1,
            'limit': this.limit1,
            'type': this.typeFw,
            'name': this.userNameFw,
            'status': 4,
            'ordersNo': this.ordersNoFw,
            'companyName': '',
            'startTime': this.startTimeFw,
            'endTime': this.endTimeFw,
            'orderTakingUserId': companyId
          })
        }).then(({
          data
        }) => {
          this.tableDataLoadingU = false
          let returnData = data.data
          this.userData = returnData
        })
      },
      // 确认派发服务订单
      querenBtns(row) {
        this.$confirm(`确定派单给此员工?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('orders/assignTask'),
            method: 'post',
            params: this.$http.adornParams({
              'userId': this.hospitalId,
              'ordersId': row.ordersId
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
                  this.dialogFormVisible = false
                  this.dataSelectoder()
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
      // 查询
      selectFw() {
        this.page1 = 1
        this.dataSelectUser()
      },
      // 重置
      cleansFw() {
        this.page1 = 1
        this.userNameFw = ''
        this.typeFw = ''
        this.ordersNoFw = ''
        this.startTimeFw = ''
        this.endTimeFw = ''
        this.dataSelectUser()
      },
      // 刷新任务事件
      animeDatRw(state) {
        this.page = 1
        this.dataSelectoderRw()
      },
      // 获取员工 任务 待服务订单
      dataSelectoderRw() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('help/getStaffOrder'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'userId': this.hospitalId
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData

        })
      },
      // 指派任务订单弹框
      zhipaiDetRw(row, pd) {
        this.taskdataSelect()
        this.dialogFormVisibleDel = true
      },
      // 获取接单数据列表
      taskdataSelect() {
        this.tableDataLoadingU = true
        let companyId = this.$route.query.companyId
        this.$http({
          url: this.$http.adornUrl('help/selectHelpTakeList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page1,
            'limit': this.limit1,
            'status': 1,
            'rideUserId': companyId,
            'phone': this.phoneR,
            'gameId': '',
            'startTime': this.startTimeFw,
            'endTime': this.endTimeFw,
            'taskType': this.taskTypeT
          })
        }).then(({
          data
        }) => {
          this.tableDataLoadingU = false
          let returnData = data.data
          this.tableData2 = returnData
        })
      },
      phoneSelect() {
        this.page1 = 1
        this.taskdataSelect()
      },
      // 重置
      cleansRw() {
        this.page1 = 1
        this.phoneR = ''
        this.taskTypeT = ''
        this.startTimeFw = ''
        this.endTimeFw = ''
        this.taskdataSelect()
      },
      // 确认派单 （任务）
      querenBtnsRw(row) {
        this.$confirm(`确定派单给此员工?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('help/distributionOrder'),
            method: 'post',
            params: this.$http.adornParams({
              'companyId': this.hospitalId,
              'takeId': row.helpTakeId
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
                  this.dialogFormVisibleDel = false
                  this.dataSelectoderRw()
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
    },

    mounted() {
      let data = {
        key: "ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75", //申请的密钥
        output: "jsonp",
      };
      let url = "https://apis.map.qq.com/ws/location/v1/ip"
      this.$jsonp(url, data).then(res => {
        console.log('获取Ip定位成功----')

        this.lng = res.result.location.lng
        this.lat = res.result.location.lat

      }).catch(err => {
        this.$toast('获取Ip定位失败--', err.message)
      })
      this.dataSelect()

    }
  }
</script>

<style lang="scss" scoped>
.rider-scheduling-page {
  padding: 0;
  overflow-x: hidden;
}

.rider-scheduling-page__hero {
  margin-bottom: 12px;
}

.rider-scheduling-page__filter {
  margin-bottom: 12px;
}

.rider-scheduling-page__filter-body {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
  justify-content: space-between;
}

.rider-scheduling-page__back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: var(--nf-brand, #01af74);
  text-decoration: none;
  cursor: pointer;
}

.rider-scheduling-page__back:hover {
  color: var(--nf-brand-deep, #017c52);
}

.rider-scheduling-page__back-icon {
  width: 1.2em;
  height: 1.2em;
}

.rider-scheduling-page__actions {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.rider-scheduling-page__map-card {
  margin-bottom: 0;
  padding: 0;
  overflow: hidden;
}

.rider-scheduling-page__map {
  width: 100%;
  height: 80vh;
  min-height: 480px;
  position: relative;
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

  .csssprite {
    width: 100% !important;
    height: 100% !important;
    border-radius: 50% !important;
    min-width: 100% !important;
    max-width: 100% !important;
    min-height: 100% !important;
    max-height: 100% !important;
    border: 3px solid red !important;
  }
</style>
