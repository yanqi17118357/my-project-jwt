<template>
  <div style="text-align: center; margin: 0 20px;">
    <div style="margin-top: 150px;">
      <div style="font-size: 25px; font-weight: bold;">注册新用户</div>
      <div style="font-size: 14px; color: grey;">欢迎注册我们的学习平台，请填写以下信息</div>
    </div>
    <div style="margin-top: 50px;">
      <el-form :model="form" :rules="rule">
        <el-form-item>
          <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名">
            <template #prefix>
              <el-icon><User/></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
            <template #prefix>
              <el-icon><Lock/></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.confirmPassword" maxlength="20" type="password" placeholder="确认密码">
            <template #prefix>
              <el-icon><Lock/></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.email" maxlength="20" type="email" placeholder="邮箱">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-row :gutter="10" style="width: 100%;">
            <el-col :span="17">
              <el-input v-model="form.code" maxlength="6" type="text" placeholder="验证码">
                <template #prefix>
                  <el-icon><EditPen /></el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="5">
              <el-button type="primary">获取验证码</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 40px;">
      <el-button style="width: 270px;" type="success" plain>立即注册</el-button>
    </div>
    <el-divider>
      <span style="font-size: 13px; color: grey;">已有账号👇</span>
    </el-divider>
    <div>
      <el-button @click="router.push('/')" style="width: 270px;" type="warning" plain>立即登录</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import router from '@/router';
import {Lock, User, Message, EditPen} from '@element-plus/icons-vue'
const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  code: '',
})

const rule = {

}
</script>

<style scoped>

</style>
