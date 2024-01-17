<script setup>
import { login } from '@/net';
import router from '@/router';
import { User } from '@element-plus/icons-vue'
import { Lock } from '@element-plus/icons-vue'
import { reactive, ref } from 'vue';

const formRef = ref()

const form = reactive({
  username: '',
  password: '',
  remember: false,
})

const rule = {
  username: [
    {required: true, message: '请输入用户名'}
  ],
  password: [
    {required: true, message: '请输入密码'}
  ]
}

function userLogin() {
  formRef.value.validate((valid) => {
    if(valid) {
      login(form.username, form.password, form.remember, () => router.push('/index'))
    }
  })
}

</script>

<template>
  <div class="text-center mx-5">
    <div class="mt-150px">
      <div class="text-25px font-bold">登录</div>
      <div class="text-14px text-gray-500">在进入系统之前，请先输入用户名和密码进行登录</div>
    </div>
    <div class="mt-50px">
      <el-form :model="form" :rules="rule" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名/邮箱">
            <template #prefix>
              <el-icon>
                <User />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
            <template #prefix>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12" style="text-align: left;">
            <el-form-item prop="remember">
              <el-checkbox label="记住我" v-model="form.remember" />
            </el-form-item>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <el-link>忘记密码？</el-link>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="mt-40px">
      <el-button class="w-270px" @click="userLogin" type="success" plain>立即登录</el-button>
    </div>
    <el-divider>
      <span class="text-13px text-gray-500">没有账号👇</span>
    </el-divider>
    <div>
      <el-button class="w-270px" @click="router.push('/register')" type="warning" plain>立即注册</el-button>
    </div>
  </div>
</template>


