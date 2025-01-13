<template>
  <div id="agent-admin-list">
    <div class="search">
      <div class="btn">
        <el-button type="success" @click="getAdd">新增</el-button>
      </div>
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="">
          <el-input v-model="form.name" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-input v-model="form.phone" placeholder="请输入号码"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="tableData" style="width: 100%" border stripe>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form
            label-position="left"
            label-width="100px"
            inline
            class="demo-table-expand"
          >
            <el-form-item label="姓名">
              <span>{{ props.row.name }}</span>
            </el-form-item>
            <el-form-item label="账号">
              <span>{{ props.row.account }}</span>
            </el-form-item>
            <el-form-item label="地址">
              <span>{{ props.row.address }}</span>
            </el-form-item>
            <el-form-item label="号码">
              <span>{{ props.row.merchantPhone }}</span>
            </el-form-item>
            <el-form-item label="提成标准">
              <span>{{ props.row.commission }}</span>
            </el-form-item>
            <!-- <el-form-item label="生日">
                    <span>{{ props.row.birthday }}</span>
                  </el-form-item>
                  <el-form-item label="性别">
                    <span>{{ props.row.sex }}</span>
                  </el-form-item> -->
            <el-form-item label="状态">
              <span>{{ props.row.isFreeze }}</span>
            </el-form-item>
            <!-- <el-form-item label="更新人">
                    <span>{{ props.row.updateUser }}</span>
                  </el-form-item>
                  <el-form-item label="更新时间">
                    <span>{{ props.row.updateTime }}</span>
                  </el-form-item>
                  <el-form-item label="创建人">
                    <span>{{ props.row.createUser }}</span>
                  </el-form-item> -->
            <!-- <el-form-item label="创建时间">
                    <span>{{ props.row.createTime }}</span>
                  </el-form-item> -->
          </el-form>
        </template>
      </el-table-column>
      <el-table-column type="index" label="编号" width="60"></el-table-column>
      <el-table-column
        v-for="(item, index) in tableTitle"
        :key="index + ''"
        :prop="item.prop"
        :label="item.label"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column fixed="right" label="操作" width="240">
        <template slot-scope="scope">
          <el-button @click="getInfo(scope.row)" type="success" size="small"
            >详情</el-button
          >
          <el-button
            @click="getType(scope.row)"
            v-html="scope.row.isFreeze === '有效' ? '禁用' : '启用'"
            type="warning"
            size="small"
          ></el-button>
          <el-button @click="getDel(scope.row)" type="danger" size="small"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="text-align: right">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="form.currentPage4"
        :page-sizes="[10, 20, 30]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
        >
      </el-pagination>
    </div>
    <!-- 编辑 -->
    <el-dialog
      title="新增营销账户"
      :visible.sync="editPop"
      width="30%"
      :before-close="editPopClose"
    >
      <el-form ref="reviewForm" :model="editForm" label-width="120px">
        <el-form-item label="姓名">
          <el-input
            type=""
            v-model="editForm.name"
            placeholder="请输入姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input
            type=""
            v-model="editForm.merchantPhone"
            placeholder="请输入电话号码"
          ></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input
            type=""
            v-model="editForm.address"
            placeholder="请输入地址"
          ></el-input>
        </el-form-item>
        <el-form-item label="提成标准">
          <el-input
            type=""
            v-model="editForm.commission"
            placeholder="请输入提成标准"
          ></el-input>
        </el-form-item>
        <el-form-item label="营销账号">
          <el-input
            type=""
            v-model="editForm.account"
            placeholder="请输入营销账号"
          ></el-input>
        </el-form-item>
        <el-form-item label="营销账号密码">
          <el-input
            type=""
            v-model="editForm.password"
            placeholder="请输入营销账号密码"
          ></el-input>
        </el-form-item>
      </el-form>
      <el-button
        style="margin-left: 4.2rem;text-align:right"
        type="success"
        @click="getEditPop"
        >确定</el-button
      >
    </el-dialog>
  </div>
</template>
<script>
import Serve from "@/serve/hc-api/bus";
import { Message } from "element-ui";
export default {
  components: {},
  data() {
    return {
      name: "",
      tableTitle: [
        { prop: "name", label: "姓名" },
        { prop: "account", label: "账号" },
        { prop: "address", label: "地址" },
        { prop: "merchantPhone", label: "号码" },
        { prop: "merchantNum", label: "商家数量" },
        { prop: "commission", label: "提成标准" },
        // { prop: 'sex', label: '性别' },
        // { prop: 'amount', label: '账户余额' },
        // { prop: 'freeze', label: '冻结金额' },
        { prop: "isFreeze", label: "状态" }
      ],
      tableData: [],
      total: null,
      form: {
        name: "",
        phone: "",
        currentPage4: 1,
        currentSize: 10
      },
      timeOption: [],
      // 编辑
      editForm: {
        name: "",
        merchantPhone: "",
        commission: "",
        account: "",
        password: "",
        address: "",
        merchantType: 1
      },
      editPop: false
    };
  },
  beforeRouteEnter(to, from, next) {
    if (from.path === "/busdetail") {
      to.meta.isBack = true;
    } else {
      to.meta.isBack = false;
    }
    next();
  },
  activated() {
    // 如果从其他页面进入需要重新请求数据
    if (!this.$route.meta.isBack) {
      this.form = {
        name: "",
        phone: "",
        currentPage4: 1,
        currentSize: 10
      };
    }
    this.getList(this.form);
    this.$route.meta.isBack = false;
  },
  created() {
    this.getList();
  },
  watch: {
    form: {
      handler(val, oldVal) {
        this.getList(val);
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    // 获取表格
    getList(val) {
      Serve.getMarketingPage(val).then(res => {
        if (res.data && res.data.data !== null) {
          this.tableData = res.data.data.records;
          for (const item of this.tableData) {
            item.adress = `${item.country}-${item.province}-${item.city}`;
            item.sex = `${item.gender} === 1 ? 男 : 女`;
            item.sex =
              item.gender === "1"
                ? "男"
                : item.gender === "0"
                ? "女"
                : "暂无信息";
          }
          this.total = res.data.data.total;
        }
      });
    },
    getDel(row) {
      this.$confirm("确认删除？")
        .then(() => {
          Serve.deleBus({ id: row.id }).then(res => {
            if (res.code === 200) {
              this.$message.success(res.msg);
            }
            this.getList(this.form);
          });
        })
        .catch(() => {});
    },
    handleSizeChange(val) {
      this.form.currentSize = val;
    },
    handleCurrentChange(val) {
      this.form.currentPage4 = val;
    },
    // 详情
    getInfo(val) {
      this.$router.push({
        name: "busdetail",
        query: { id: val.id, type: "detail" }
      });
    },
    // 禁用启用
    getType(val) {
      let a;
      if (val.isFreeze === "无效") {
        a = "1";
      } else {
        a = "0";
      }
      Serve.typeBus({ id: val.id, isFreeze: a }).then(res => {
        if (res.data.code === 200) {
          this.getList(this.form);
        }
      });
    },
    // 编辑
    editPopClose() {
      this.editPop = false;
    },
    getAdd() {
      this.editPop = true;
      this.editForm = {};
    },
    getEditPop() {
      this.editForm.merchantType = 2;
      console.log(this.editForm);
      Serve.addBus(this.editForm).then(res => {
        if (res.data) {
          Message.success("新增成功");
          this.editPop = false;
          this.getList(this.form);
        }
      });
    }
  }
};
</script>
<style lang="less" scoped>
#agent-admin-list {
  width: 100%;
  height: 100%;
  background: #fff;
  .search {
    width: 100%;
    height: 0.8rem;
    display: flex;
    justify-content: space-between;
    box-sizing: border-box;
    align-items: center;
    .btn {
      display: flex;
      margin-left: 0.2rem;
      .el-button {
        padding: 0.1rem 0.12rem;
        height: 0.4rem;
      }
    }
    .el-form {
      display: flex;
      justify-content: space-around;
      margin-top: 0.25rem;
      .block {
        margin-right: 0.2rem;
      }
    }
  }
}
</style>
