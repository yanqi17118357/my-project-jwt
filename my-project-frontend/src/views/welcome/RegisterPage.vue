<script setup>
import { computed, reactive, ref } from 'vue';
import router from '@/router';
import { Lock, User, Message, EditPen } from '@element-plus/icons-vue'
import { get, post } from '@/net';
import { ElMessage } from 'element-plus';

const coldTime = ref(0)
const formRef = ref()

const form = reactive({
  username: '',
  password: '',
  confirm_password: '',
  email: '',
  code: '',
})

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名只能包含中文、英文、数字'))
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { validator: validateUsername, trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirm_password: [
    { validator: validatePassword, trigger: ['blur', 'change'] }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

function askCode() {
  if (isEmailVaild) {
    coldTime.value = 60
    get(`/api/auth/ask-code?email=${form.email}&type=register`, () => {
      ElMessage.success('验证码已发送至你的邮箱，请注意查收')
      const handle = setInterval(() => {
          coldTime.value--
          if(coldTime.value === 0) {
            clearInterval(handle)
          }
        }, 1000)
    }, (message) => {
      ElMessage.error(message)
      coldTime.value = 0
    })
  } else {
    ElMessage.error('请输入正确的邮箱地址')
  }
}

const isEmailVaild = computed(() => /^[\w.-]+@[\w-]+\.\w+$/.test(form.email))

function register() {
  formRef.value.validate((valid) => {
    if(valid) {
      post('/api/auth/register', {...form}, () => {
        ElMessage.success('注册成功')
        router.push('/')
      })
    } else {
      ElMessage.error('请检查表单')
    }
  })
}
</script>

<template>
  <div class="text-center mx-5">
    <div class="mt-150px">
      <div class="text-25px font-bold">注册新用户</div>
      <div class="text-14px text-gray-500">欢迎注册我们的学习平台，请填写以下信息</div>
    </div>
    <div class="mt-50px">
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名">
            <template #prefix>
              <el-icon>
                <User />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码" show-password>
            <template #prefix>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="confirm_password">
          <el-input v-model="form.confirm_password" maxlength="20" type="password" placeholder="确认密码" show-password>
            <template #prefix>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" type="email" placeholder="邮箱">
            <template #prefix>
              <el-icon>
                <Message />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row class="w-full" :gutter="10">
            <el-col :span="17">
              <el-input v-model="form.code" maxlength="6" type="text" placeholder="验证码">
                <template #prefix>
                  <el-icon>
                    <EditPen />
                  </el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="5">
              <el-button @click="askCode" :disabled="!isEmailVaild || coldTime > 0" type="primary">
                {{ coldTime ? `${coldTime}秒后重试` : '获取验证码' }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div class="mt-40px">
      <el-button @click="register" class="w-270px" type="success" plain>立即注册</el-button>
    </div>
    <el-divider>
      <span class="text-13px text-gray-500">已有账号👇</span>
    </el-divider>
    <div>
      <el-button class="w-270px" @click="router.push('/')" type="warning" plain>立即登录</el-button>
    </div>
  </div>
</template>
