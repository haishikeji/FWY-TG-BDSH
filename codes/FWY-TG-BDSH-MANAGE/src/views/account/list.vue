<template>
  <div id="agent-admin-list">
    <div class="search">
      <div class="btn">
        <el-button type="success" @click="getAdd({ id: 'add' })"
          >新增</el-button
        >
      </div>
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="">
          <el-input v-model="form.name" placeholder="请输入账号昵称"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="">
          <!-- <el-input v-model="form.disabled" placeholder="请选择状态"></el-input> -->
          <el-select v-model="form.disabled">
            <el-option label="全部状态" value=""></el-option>
            <el-option label="有效" value="1"></el-option>
            <el-option label="无效" value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="tableData" style="width: 100%" border stripe>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="账号">
              <span>{{ props.row.account }}</span>
            </el-form-item>
            <el-form-item label="昵称">
              <span>{{ props.row.name }}</span>
            </el-form-item>
            <el-form-item label="头像">
              <img
                @click="getimg(props.row.headUrl)"
                :src="props.row.headUrl"
                alt=""
                style="width:0.4rem"
              />
            </el-form-item>
            <el-form-item label="手机号">
              <span>{{ props.row.phone }}</span>
            </el-form-item>
            <el-form-item label="角色">
              <span>{{ props.row.roleName }}</span>
            </el-form-item>
            <el-form-item label="状态">
              <span>{{ props.row.disabled }}</span>
            </el-form-item>
            <el-form-item label="创建人">
              <span>{{ props.row.createUser }}</span>
            </el-form-item>
            <el-form-item label="创建时间">
              <span>{{ props.row.createTime }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        v-for="(item, index) in tableTitle"
        :key="index"
        :prop="item.prop"
        :label="item.label"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column fixed="right" label="操作" width="340">
        <template slot-scope="scope">
          <!-- <el-button type="success" size="small">详情</el-button> -->
          <el-button type="success" size="small" @click="getAdd(scope.row)"
            >修改信息</el-button
          >
          <!-- <el-button type="warning" size="small" @click="getPwd(scope.row)">修改密码</el-button> -->
          <el-button
            @click="getType(scope.row)"
            v-html="scope.row.disabled === '有效' ? '禁用' : '启用'"
            type="warning"
            size="small"
          ></el-button>
          <el-button type="danger" size="small" @click="del(scope.row)"
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
    <!-- 新增 -->
    <el-dialog
      :title="title"
      :visible.sync="editPop"
      width="40%"
      :before-close="editPopClose"
    >
      <el-form
        ref="editForm"
        :rules="rules"
        :model="editForm"
        label-width="120px"
        style="text-align:left"
      >
        <el-form-item label="账号名" prop="account">
          <el-input
            type="text"
            v-model="editForm.account"
            placeholder="请输入账号"
          ></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="name">
          <el-input
            type="text"
            v-model="editForm.name"
            placeholder="请输入昵称"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input
            type="text"
            v-model="editForm.phone"
            placeholder="请输入手机号"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pwd" v-if="type === 'add'">
          <el-input
            type="password"
            v-model="editForm.pwd"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="上传头像" v-if="type === 'edit'">
          <el-upload
            class="upload-demo"
            action=""
            :beforeUpload="beforeUploadPicture"
            style="text-align: left"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            {{ fileName }}
          </el-upload>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="editForm.roleId" @change="getss">
            <el-option
              v-for="(item, index) in roles"
              :key="index"
              :label="item.roleName"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <!-- <el-input type="" v-model="editForm.role" placeholder="请选择角色"></el-input> -->
        </el-form-item>
        <el-form-item label="">
          <el-button
            style="text-align:right"
            type="success"
            @click="getEditPop('editForm')"
            >确定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 图片大图 -->
    <el-dialog class="delet" title="" :visible.sync="bigImg" width="4rem">
      <img :src="linkUrk" alt="" style="width:100%;" />
    </el-dialog>
    <!-- 否定类 -->
    <el-dialog title="" :visible.sync="delet" width="30%">
      <span style="font-size: 0.2rem">是否确认{{ negative }}？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="delet = false">取消</el-button>
        <el-button type="primary" @click="getnegative">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import Serve from "@/serve/hc-api/acount";
import { Message } from "element-ui";
import formRules from "@/serve/formRules";
export default {
  components: {},
  data() {
    return {
      name: "",
      roles: [],
      // 否定类操作
      delet: false,
      ids: "",
      negative: "",
      // 大图
      bigImg: false,
      linkUrk: "",
      // 列表
      tableTitle: [
        { prop: "account", label: "账号" },
        { prop: "name", label: "昵称" },
        // { prop: 'nick', label: '昵称' },
        { prop: "phone", label: "手机号" },
        { prop: "roleName", label: "角色" },
        { prop: "disabled", label: "状态" }
      ],
      tableData: [],
      total: null,
      // 搜索
      form: {
        name: "",
        phone: "",
        disabled: "",
        current: 1,
        size: 10
      },
      timeOption: [],
      // 新增编辑
      title: "",
      type: "",
      editForm: {
        account: "",
        name: "",
        phone: "",
        pwd: "",
        roleId: "",
        headUrl: ""
      },
      editPop: false,
      rules: {
        account: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { min: 1, max: 10, message: "长度在 1 到 10 个字符", trigger: "blur" }
        ],
        pwd: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 32, message: "长度在 6 到 32 个字符", trigger: "blur" }
        ],
        roleId: [{ required: true, message: "请选择角色", trigger: "change" }],
        phone: [
          { required: true, validator: formRules.validPhone, trigger: "blur" }
        ],
        name: [{ required: true, message: "请输入昵称", trigger: "blur" }]
      },
      fileName: ""
    };
  },
  created() {
    this.getStartInfo();
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
    /**
     * 获取初始信息
     */
    getStartInfo() {
      // 获取角色列表
      Serve.roleList().then(res => {
        if (res.data.code === 200) {
          this.roles = res.data.data.records;
        }
      });
    },
    // 查看图片大图
    getimg(val) {
      this.bigImg = true;
      this.linkUrk = val;
    },
    // 获取表格
    getList(val) {
      Serve.listAcount(val).then(res => {
        if (res.data && res.data.data !== null) {
          this.tableData = res.data.data.records;
          this.total = res.data.data.total;
        }
      });
    },
    handleSizeChange(val) {
      this.form.size = val;
    },
    handleCurrentChange(val) {
      this.form.current = val;
    },
    /**
     * 表格操作
     */
    // 删除
    del(val) {
      this.ids = val.id;
      this.delet = true;
      this.negative = "删除";
    },
    getnegative() {
      Serve.delAcount({ id: this.ids }).then(res => {
        if (res.data) {
          Message.success("删除成功");
          this.delet = false;
          this.getList(this.form);
        }
      });
    },
    // 禁用启用
    getType(val) {
      let a;
      if (val.disabled === "有效") {
        a = "0";
      } else {
        a = "1";
      }
      Serve.typeAcount({ id: val.id, disabled: a }).then(res => {
        if (res.data.code === 200) {
          this.getList(this.form);
        }
      });
    },
    /**
     * 新增编辑
     */
    // 新增
    editPopClose() {
      this.editPop = false;
    },
    getAdd(val) {
      this.editPop = true;
      if (val.id === "add") {
        this.editForm = {
          account: "",
          name: "",
          phone: "",
          pwd: "",
          roleId: "",
          headUrl: ""
        };
        this.type = "add";
        this.title = "新增";
      } else {
        this.editForm = {
          account: val.account,
          name: val.name,
          phone: val.phone,
          roleId: val.roleId,
          headUrl: val.headUrl,
          id: val.id
        };
        this.type = "edit";
        this.title = "编辑";
      }
    },
    getss() {
      console.log(this.editForm.roleId);
    },
    getEditPop(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          for (const item of this.roles) {
            if (item.id === this.editForm.roleId) {
              this.editForm.roleName = item.roleName;
            }
          }
          if (this.type === "add") {
            console.log(this.editForm, "kankankan");
            Serve.addAcount(this.editForm).then(res => {
              if (res.data) {
                Message.success("新增成功");
                this.editPop = false;
                this.getList(this.form);
              }
            });
          } else {
            Serve.editAcount(this.editForm).then(res => {
              if (res.data) {
                Message.success("编辑成功");
                this.editPop = false;
                this.type = "";
                this.getList(this.form);
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    // 编辑上传头像
    beforeUploadPicture(file) {
      this.fileName = file.name;
      this.formdata = new FormData();
      this.formdata.append("file", file);
      Serve.fileup(this.formdata).then(res => {
        if (res.data) {
          Message.success("上传成功");
          this.editForm.headUrl = res.data.data;
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
<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>
