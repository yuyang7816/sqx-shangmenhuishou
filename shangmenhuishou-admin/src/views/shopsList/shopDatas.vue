<template>
  <div class="sd-page">
    <div class="sd-page__nav">
      <a href="#" class="sd-page__back-link" @click.prevent="prev">
        <icon-svg name="jiantou" class="sd-page__back-icon"></icon-svg>
        返回
      </a>
      <span class="sd-page__nav-sep">|</span>
      <span class="sd-page__nav-title">收入统计</span>
    </div>

    <div class="sd-page__top">
      <div class="nf-page-hero sd-page__hero">
        <span class="nf-page-hero__badge">数据中心</span>
        <h2 class="nf-page-hero__title">商铺收入统计</h2>
        <p class="nf-page-hero__desc">按天/月/年查看商铺服务与任务订单收入及退款核心指标</p>
      </div>
      <div class="nf-filter-card sd-page__filter">
        <div class="nf-filter-card__head">
          <i class="el-icon-search"></i>
          <span>筛选条件</span>
        </div>
        <div class="nf-filter-card__body sd-page__filter-body">
          <el-select v-model="flag" class="sd-page__filter-select" @change="orderfenxi">
            <el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          <el-date-picker
            v-if="flag==1"
            class="sd-page__filter-date"
            v-model="info.stockDate"
            align="right"
            type="date"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            placeholder="选择开始时间"
            @change="animeOrder">
          </el-date-picker>
          <el-date-picker
            v-if="flag==2"
            class="sd-page__filter-date"
            v-model="info.stockDate"
            align="right"
            type="date"
            format="yyyy-MM"
            value-format="yyyy-MM"
            placeholder="选择开始时间"
            @change="animeOrder">
          </el-date-picker>
          <el-date-picker
            v-if="flag==3"
            class="sd-page__filter-date"
            v-model="info.stockDate"
            align="right"
            type="date"
            format="yyyy"
            value-format="yyyy"
            placeholder="选择开始时间"
            @change="animeOrder">
          </el-date-picker>
        </div>
      </div>
    </div>

    <div class="sd-page__stats" v-loading="tableDataLoading1">
      <div class="sd-page__kpi-row">
        <div class="sd-kpi">
          <div class="sd-kpi__icon"><i class="el-icon-money"></i></div>
          <div class="sd-kpi__body">
            <div class="sd-kpi__label">服务订单总金额</div>
            <div class="sd-kpi__value"><span>{{ serviceMoney.serviceMoney ? serviceMoney.serviceMoney : 0 }}</span>元</div>
          </div>
        </div>
        <div class="sd-kpi">
          <div class="sd-kpi__icon"><i class="el-icon-s-order"></i></div>
          <div class="sd-kpi__body">
            <div class="sd-kpi__label">任务订单总金额</div>
            <div class="sd-kpi__value"><span>{{ serviceMoney.helpMoney ? serviceMoney.helpMoney : 0 }}</span>元</div>
          </div>
        </div>
        <div class="sd-kpi">
          <div class="sd-kpi__icon"><i class="el-icon-refresh-left"></i></div>
          <div class="sd-kpi__body">
            <div class="sd-kpi__label">服务退款总金额</div>
            <div class="sd-kpi__value"><span>{{ refundMoney.serviceRefundMoney ? refundMoney.serviceRefundMoney : 0 }}</span>笔</div>
          </div>
        </div>
        <div class="sd-kpi">
          <div class="sd-kpi__icon"><i class="el-icon-refresh"></i></div>
          <div class="sd-kpi__body">
            <div class="sd-kpi__label">任务退款总金额</div>
            <div class="sd-kpi__value"><span>{{ refundMoney.helpRefundMoney ? refundMoney.helpRefundMoney : 0 }}</span>笔</div>
          </div>
        </div>
      </div>

      <div class="sd-page__detail-grid">
        <div class="sd-detail-col">
          <div class="sd-detail-col__head">服务订单</div>
          <div class="sd-stat-tiles">
            <div class="sd-stat-tile">
              <div class="sd-stat-tile__label">服务总订单数</div>
              <div class="sd-stat-tile__value"><span>{{ serviceCount.serviceCounts ? helperOrderAllMoney : 0 }}</span>元</div>
            </div>
            <div class="sd-stat-tile">
              <div class="sd-stat-tile__label">服务退款总订单数</div>
              <div class="sd-stat-tile__value"><span>{{ refundCount.serviceRefundCount ? refundCount.serviceRefundCount : 0 }}</span>笔</div>
            </div>
          </div>
        </div>
        <div class="sd-detail-col">
          <div class="sd-detail-col__head">任务订单</div>
          <div class="sd-stat-tiles">
            <div class="sd-stat-tile">
              <div class="sd-stat-tile__label">任务总订单数</div>
              <div class="sd-stat-tile__value"><span>{{ serviceCount.helpCounts ? serviceCount.helpCounts : 0 }}</span>元</div>
            </div>
            <div class="sd-stat-tile">
              <div class="sd-stat-tile__label">任务退款总订单数</div>
              <div class="sd-stat-tile__value"><span>{{ refundCount.helpRefundCount ? refundCount.helpRefundCount : 0 }}</span>笔</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div>
    <!-- <el-table v-loading="tableDataLoading" :data="tableData2.list">
      <el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
      <el-table-column fixed prop="userName" label="下单用户">
        <template slot-scope="scope">
          <span style="color: #01af74;cursor: pointer;" @click="updates(scope.row)">
            {{ scope.row.userName ? scope.row.userName : '未绑定' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="shopName" label="商铺"></el-table-column>
      <el-table-column prop="orderType" label="订单类型">
        <template slot-scope="scope">
          <div>{{scope.row.orderType==1?'到店取餐':'外卖配送'}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="orderType" label="配送信息" width="300">
        <template slot-scope="scope">
          <div v-if="scope.row.orderType==2">
            <div>订餐人：{{scope.row.address.userName}}</div>
            <div>订餐人电话：{{scope.row.address.userPhone}}</div>
            <div>订餐地址：{{scope.row.address.address}}({{scope.row.address.addressDetail}})</div>
          </div>
          <div v-else style="text-align: center;width: 100%;"> - </div>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="内容" width="300">
        <template slot-scope="scope">
          <div>
            <div v-for="(item,index) in scope.row.orderGoodsList" :key="index">
              <span>{{index+1}}.
                商品名：{{item.goodsName}},</span><span>数量：{{item.goodsNum}},</span><span>规格：{{item.skuMessage}}</span>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="payMoney" label="接单价格" width="100"></el-table-column>
      <el-table-column prop="payTime" label="支付时间"></el-table-column>
      <el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
      <el-table-column fixed="right" prop="status" label="状态" width="250">
        <template slot-scope="scope">
          <span style="color: #01af74;" v-if="scope.row.status === 0 ">待结算</span>
          <span style="color: #01af74;" v-if="scope.row.status === 1 ">待支付</span>
          <span style="color: #01af74;" v-if="scope.row.status === 3 "> {{scope.row.orderType==1?'待取餐':'待配送'}}</span>
          <span style="color: #01af74;" v-if="scope.row.status === 4 ">已完成</span>
          <span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center;margin-top: 10px;">
      <el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
        :page-sizes="[10, 20, 30, 40,50,100]" :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next"
        :total="tableData2.totalCount">
      </el-pagination>
    </div> -->
    <!-- 任务拒绝弹框 -->
    <el-dialog title="任务拒绝" :visible.sync="dialogFormVisible7" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
        <el-input style="width:50%;" v-model="content" type="text" placeholder="请输入拒绝理由"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible7 = false">取 消</el-button>
        <el-button type="primary" @click="refuseto()">确 定</el-button>
      </div>
    </el-dialog>
    <!--    图片展示-->
    <el-dialog title="图片" :visible.sync="dialogVisible" width="35%" :before-close="handleClose">
      <span v-if="imageUrl.length>0" v-for="(item, index) in imageUrl" :key="index" style="width: 100px;
        height: 100px;display:inline-block;">
        <!--      <el-image style = "width: 100px; height: 100px" :src = "item" :preview-src-list="imageUrl"></el-image>-->
        <img :src="item" alt="" style="width: 100px; height: 100px">
      </span>
      <span v-else>暂无图片</span>
    </el-dialog>
  </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        dialogVisible: false,
        imageUrl: [],
        limit: 10,
        page: 1,
        phone: '',
        status: '',
        describes: '',
        token: '',
        taskNum: '',
        taskMoney: '',
        statesnum: [{
            label: '全部',
            value: 0
          },
          {
            label: '待接单',
            value: 2
          },
          {
            label: '待送达',
            value: 3
          },
          {
            label: '已完成',
            value: 4
          },
          {
            label: '已下架',
            value: 5
          },
        ],
        taskstates: [{
            label: '全部',
            value: ''
          },
          {
            label: '待结算',
            value: 0
          },
          {
            label: '待支付',
            value: 1
          },
          {
            label: '待取餐', //待取餐、派送中
            value: 3
          },
          {
            label: '已完成',
            value: 4
          },
          {
            label: '已取消',
            value: 5
          }
        ],
        shopId: '',
        shopList: '',
        sort: 10,
        id: '',
        btnChangeEnable: true,
        helpTakeId: '',
        helpMaintainId: '',
        content: '',
        classifyIcon: '',
        classifyId: '',
        classifyUrl: '',
        classifyName: '',
        classifyDeatilsName: '',
        formLabelWidth: '200px',
        activeName: 'first',
        tableDataLoading: false,
        tableDataLoading1:false,
        dialogFormVisible: false,
        dialogFormVisible1: false,
        dialogFormVisible2: false,
        dialogFormVisible3: false,
        dialogFormVisible4: false,
        dialogFormVisible5: false,
        dialogFormVisible6: false,
        dialogFormVisible7: false,
        dialogFormVisible8: false,
        tableData: [],
        tableData2: {},
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
        protectjieData: [],
        protectpaiData: [],
        helpTaskData: [],
        safeguardData: [],
        classifyData: [],
        checkBoxData: [], //多选框选择的值
        checkBoxData2: [], //多选框选择的值
        userName: '',
        taskStat: {},
		serviceMoney:{},
		refundCount:{},
		refundMoney:{},
		serviceCount:{},
    serviceRefundMoney:0,
        startTime: '',
        endTime: '',
        info: {
          stockDate: this.getNowTime(), //日期
        },
        info1: {
          stockDate1: this.getNowTime1(), //日期
        },
        info2: {
          stockDate2: this.getNowTime2(), //日期
        },
        flag: '1',
        flags: [{
          value: '1',
          label: '按天查询'
        }, {
          value: '2',
          label: '按月查询'
        }, {
          value: '3',
          label: '按年查询'
        }],
    }
  },
  methods: {
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
      //处理默认选中当前日期
      getNowTime() {
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
      timeDate() {
        this.classifySelect()
        // this.taskdataSelect()
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
      // 订单分析选择日期
      animeOrder() {
        this.classifySelect()
        // this.taskdataSelect()
      },
      // 订单分析年月日
      orderfenxi(value) {
        let vanumber = value
        // if (vanumber === 'day') {
        //   this.flag = 'day'
          this.classifySelect()
          // this.taskdataSelect()
        // }
        // if (vanumber === 'month') {
        //   this.flag = 'month'
        //   this.classifySelect()
        //   // this.taskdataSelect()
        // }
        // if (vanumber === 'year') {
        //   this.flag = 'year'
        //   this.classifySelect()
        //   // this.taskdataSelect()
        // }
      },
      // 图标上传一级分类
      handleAvatarSuccess(file) {
        this.classifyIcon = file.data.path
      },
      // 图标上传一级分类编辑
      handleAvatarSuccess2(file) {
        this.form.classifyIcon = file.data.path
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
        this.limit = val
        this.classifySelect()
      },
      handleCurrentChange2(val) {
        this.page = val
        this.classifySelect()
      },
      handleSizeChange3(val) {
        this.limit = val
        this.taskdataSelect()
      },
      handleCurrentChange3(val) {
        this.page = val
        this.taskdataSelect()
      },
      // tabs切换
      handleClick(tab, event) {

        if (tab._props.label == '订单管理') {
          this.page = 1
          this.limit = 10
          this.taskdataSelect()
        }

      },
      // 下架
      soldClick(row) {
        this.$confirm(`确定要下架此任务?`, '提示', {
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
      //查看照片
      refund(e) {
        this.imageUrl = []
        if (e.image != null) {
          let img = e.image.split(',')
          if (img.length != 0) {
            this.imageUrl = img
          }
        }
        this.dialogVisible = true
      },
      handleClose(done) {
        // this.$confirm('确认关闭？')
        //   .then(_ => {
        done()
        // })
        // .catch(_ => {});
      },
      // 添加一级分类弹框
      classifyStair() {
        this.dialogFormVisible = true
      },
      // 添加一级分类确定
      StairNoticeTo() {
        if (this.classifyName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入分类名称',
            type: 'warning'
          })
          return
        }
        // if (this.classifyUrl == '') {
        //     this.$notify({
        //         title: '提示',
        //         duration: 1800,
        //         message: '请输入分类链接',
        //         type: 'warning'
        //     });
        //     return
        // }
        if (this.describes == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入分类描述',
            type: 'warning'
          })
          return
        }
        if (this.classifyIcon == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传分类图标',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl('helpClassify/saveHelpClassify'),
          method: 'post',
          data: this.$http.adornData({
            'classifyName': this.classifyName,
            'state': this.state,
            'sort': this.sort,
            'classifyIcon': this.classifyIcon,
            'classifyUrl': this.classifyUrl,
            'describes': this.describes
          })
        }).then(({
          data
        }) => {
          this.dialogFormVisible = false
          this.$message({
            message: '一级分类添加成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.classifyUrl = ''
              this.classifyName = ''
              this.classifyIcon = ''
              this.describes = ''
              this.classifySelect()
            }
          })
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
          this.dialogFormVisible2 = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.classifySelect()
            }
          })
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
            url: this.$http.adornUrl(`helpClassify/deleteClassifyById/?id=${delid}`),
            method: 'post',
            data: this.$http.adornData({})
          }).then(({
            data
          }) => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.name = ''
                this.classifySelect()
              }
            })
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
        this.phone = ''
        this.status = ''
        this.userName = ''
        this.page = 1
        this.taskdataSelect()
      },
      // 查询
      select() {
        this.page = 1
        this.limit = 10
        this.dataSelect()
      },
      // 重置
      cleans() {
        this.page = 1
        this.phone = ''
        this.status = 0
        this.content = ''
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
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('help/selectStatusHelpOrder'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'phone': this.phone,
            'status': this.status,
            'content': this.content
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData = returnData
        })
      },
      // 获取接单数据列表
      taskdataSelect() {
        this.tableDataLoading = true
        this.shopId = this.$route.query.shopId
        this.$http({
          url: this.$http.adornUrl('admin/goodsShop/selectStoreMessage'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            // 'startTime': this.startTime,
            // 'endTime': this.endTime,
            'date': this.info.stockDate,
            'dateType': this.flag,
            'shopId': this.shopId
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData2 = returnData
          for (var i in this.tableData2.list) {
            if (this.tableData2.list[i].address) {
              this.tableData2.list[i].address = JSON.parse(this.tableData2.list[i].address)
            }
          }
        })
      },
      // 商铺统计
      classifySelect() {
        this.tableDataLoading1 = true
        this.$http({
          url: this.$http.adornUrl('orders/selectStaffUserStatisticsByTime'),
          method: 'get',
          params: this.$http.adornParams({
            'companyId': this.shopId,
            // 'startTime': this.startTime,
            // 'endTime': this.endTime
            'time': this.info.stockDate,
            'flag': this.flag
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading1 = false
          let returnData = data.data
          this.taskStat = returnData
		  this.serviceMoney = returnData.serviceMoney
		  this.refundCount = returnData.refundCount
		  this.refundMoney = returnData.refundMoney
		  this.serviceCount = returnData.serviceCount
        })
      },
      // 餐品完成
      wancheng(row) {
        this.$http({
          url: this.$http.adornUrl('admin/order/updateOrder'),
          method: 'post',
          data: this.$http.adornData({
            'status': 4,
            'orderId': row.orderId,
            'orderNumber': row.orderNumber
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
                this.taskdataSelect()
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
      // 返回上一级
      prev() {
        this.$router.back()
      },
    },
    mounted() {
      this.shopId = this.$route.query.shopId
      this.classifySelect()
      // this.taskdataSelect()

    },
    // watch: {
    //   '$route': 'taskdataSelect'

    // }
  }
</script>

<style scoped>
.sd-page {
  padding: 0;
  overflow-x: hidden;
}

.sd-page__nav {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  color: var(--nf-text-primary, #1a2433);
}
.sd-page__back-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: var(--nf-brand, #01af74);
  text-decoration: none;
  cursor: pointer;
}
.sd-page__back-link:hover {
  color: var(--nf-brand-deep, #017c52);
}
.sd-page__back-icon {
  width: 1.2em;
  height: 1.2em;
}
.sd-page__nav-sep {
  margin: 0 12px;
  color: var(--nf-border, #e8eaed);
}
.sd-page__nav-title {
  font-size: 16px;
  font-weight: 600;
}

.sd-page__top {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
  align-items: stretch;
}
.sd-page__hero {
  flex: 1;
  margin-bottom: 0;
}
.sd-page__filter {
  flex: 0 0 320px;
  margin-bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.sd-page__filter-body {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}
.sd-page__filter-select {
  width: 140px;
}
.sd-page__filter-date {
  width: 160px;
}

.sd-page__kpi-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 12px;
}
.sd-kpi {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: var(--nf-card-bg, #fff);
  border: 1px solid var(--nf-border, #e8eaed);
  border-radius: var(--nf-radius-md, 8px);
  box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
}
.sd-kpi__icon {
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
.sd-kpi__body {
  min-width: 0;
}
.sd-kpi__label {
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.sd-kpi__value {
  font-size: 13px;
  color: var(--nf-text-primary, #1a2433);
}
.sd-kpi__value span {
  font-size: 22px;
  font-weight: 600;
  color: var(--nf-brand, #01af74);
  margin-right: 4px;
}

.sd-page__detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 12px;
}
.sd-detail-col {
  background: var(--nf-card-bg, #fff);
  border: 1px solid var(--nf-border, #e8eaed);
  border-radius: var(--nf-radius-md, 8px);
  box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
  padding: 16px;
}
.sd-detail-col__head {
  font-size: 14px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--nf-border, #e8eaed);
}
.sd-stat-tiles {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}
.sd-stat-tile {
  padding: 12px;
  background: var(--nf-page-bg, #f4f6fa);
  border-radius: var(--nf-radius-sm, 6px);
  border: 1px solid var(--nf-border, #e8eaed);
}
.sd-stat-tile__label {
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.sd-stat-tile__value {
  font-size: 13px;
  color: var(--nf-text-primary, #1a2433);
}
.sd-stat-tile__value span {
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-brand, #01af74);
  margin-right: 4px;
}

@media (max-width: 1200px) {
  .sd-page__kpi-row {
    grid-template-columns: repeat(2, 1fr);
  }
  .sd-page__detail-grid {
    grid-template-columns: 1fr;
  }
}
@media (max-width: 860px) {
  .sd-page__top {
    flex-direction: column;
  }
  .sd-page__filter {
    flex: 1 1 auto;
  }
  .sd-page__kpi-row {
    grid-template-columns: 1fr;
  }
}
</style>
