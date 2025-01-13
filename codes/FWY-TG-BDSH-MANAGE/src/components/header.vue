<template>
  <div id="head">
    <div class="login-out">
      <el-dropdown>
        <div
          style="color:#ef8619;display: flex;justify-content: center;align-items: center;"
        >
          <img
            v-if="headUrl === '' || headUrl === null || !headUrl"
            src="../assets/images/head.jpg"
            alt=""
            style="width:0.5rem"
          />
          <img v-else :src="headUrl" alt="" style="width:0.5rem" />
          <span class="el-dropdown-link" style="font-size:0.18rem;color:#fff">
            {{ name }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
        </div>
        <el-dropdown-menu style="dispaly: block!important" slot="dropdown">
          <!-- <el-dropdown-item>
            <span class="login-btn">
              <span style="font-size:0.2rem" @click="getPath('/info')">个人中心</span>
            </span>
          </el-dropdown-item> -->
          <el-dropdown-item @click.native="dialogVisible = true">
            <span class="login-btn">
              <span style="font-size:0.16rem">修改密码</span>
            </span>
          </el-dropdown-item>
          <el-dropdown-item @click.native="geOut('/')">
            <span class="login-btn">
              <span style="font-size:0.16rem">退出登录</span>
            </span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog
      title="重置密码"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
      <span>
        <el-form
          :model="ruleForm2"
          status-icon
          :rules="rules2"
          ref="ruleForm2"
          label-width="100px"
          class="demo-ruleForm"
        >
          <el-form-item label="密码" prop="pass">
            <el-input
              type="password"
              v-model="ruleForm2.pass"
              auto-complete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPass">
            <el-input
              type="password"
              v-model="ruleForm2.confirmPass"
              auto-complete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="">
            <el-button
              type="success"
              style="margin-left: -65%;margin-top: 12px;"
              @click="next('ruleForm2')"
              >确定</el-button
            >
          </el-form-item>
        </el-form>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import Serve from "@/serve/hc-api/common";
import { Message } from "element-ui";
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.ruleForm2.confirmPass !== "") {
          this.$refs.ruleForm2.validateField("confirmPass");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm2.pass) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      name: "",
      ruleForm2: {
        confirmPass: "",
        pass: ""
      },
      dialogVisible: false,
      rules2: {
        pass: [
          { validator: validatePass, trigger: "blur" },
          { min: 6, max: 16, message: "长度在 6 到 16 个字符", trigger: "blur" }
        ],
        confirmPass: [
          { validator: validatePass2, trigger: "blur" },
          { min: 6, max: 16, message: "长度在 6 到 16 个字符", trigger: "blur" }
        ]
      },
      setTime: null,
      headUrl: "",
      userType: null
    };
  },
  created() {
    this.getStartInfo();
  },
  methods: {
    getStartInfo() {
      this.userType = sessionStorage.getItem("userType");
      if (this.userType === "1") {
        Serve.userInfo().then(res => {
          if (res.data) {
            this.name = res.data.data.name;
            this.headUrl = res.data.data.headUrl;
          }
        });
      } else {
        Serve.getMerchantAccountInfo().then(res => {
          if (res.data) {
            this.name = res.data.data.name;
            this.headUrl = res.data.data.headUrl;
          }
        });
      }
    },
    getPath(val) {
      this.$router.push(val);
    },
    // 退出登录
    geOut(val) {
      if (this.userType === "1") {
        Serve.loginOut().then(res => {
          if (res.data.code === 200) {
            this.$router.push(val);
          }
        });
      } else {
        this.$router.push(val);
      }
    },
    handleClose() {
      this.dialogVisible = false;
    },
    // 步骤条
    next(val) {
      var _this = this;
      this.$refs[val].validate(valid => {
        if (valid) {
          Serve.editPwd({ pwd: _this.ruleForm2.pass }).then(res => {
            if (res.data.code === 200) {
              Message.success("修改成功，即将跳转登录页");
              _this.setTime = setInterval(function() {
                _this.$router.push("/");
              }, 2000);
            }
          });
        } else {
          return false;
        }
      });
    }
  },
  beforeDestroy() {
    // 清除定时器
    clearInterval(this.setTime);
  }
};
</script>
<style lang="less" scoped>
#head {
  background: linear-gradient(163.85deg, #4b94f2 0%, #4393fa 100%) !important;
  // width: 19.2rem;
  width: 100%;
  height: 0.6rem;
  display: flex;
  // background: #F2991D;
  justify-content: flex-end;
  align-items: center;
  padding: 0 0.5rem 0 0.2rem;
  box-sizing: border-box;
  border-bottom: 1px solid #fff;
  .login-out {
    // width: 5rem;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 0.18rem !important;
    .tx {
      color: #333;
      margin: 0 0.2rem;
      cursor: pointer;
    }
    img {
      width: 0.3rem !important;
      border-radius: 50%;
      margin-right: 0.1rem;
    }
    .login-btn {
      font-size: 0.18rem;
      i {
        cursor: pointer;
      }
    }
  }
}
.el-dropdown-menu {
  padding: 0 10px;
  /deep/ .el-dropdown-menu__item {
    color: #4b94f2;
    font-size: 0.18rem !important;
    // border-bottom: 1px solid #ef8619!important;
  }
  .el-dropdown-menu__item:hover {
    background: #4b94f2 !important;
    color: #fff !important;
    // border-bottom: 1px solid #fff!important;
  }
}
</style>
