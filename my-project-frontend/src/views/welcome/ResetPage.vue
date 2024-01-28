<script setup>
import { EditPen, Lock, Message } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { get, post } from '@/net';
import { computed, reactive, ref } from 'vue';
import router from '@/router';


const coldTime = ref(0)
const formRef = ref()
const active = ref(0)

const form = reactive({
  email: '',
  code: '',
  password: '',
  confirm_password: '',
})

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
        if (coldTime.value === 0) {
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

function confirmReset() {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/reset-confirm', {
        email: form.email,
        code: form.code,
      }, () => active.value++)
    }
  })
}

function doReset() {
  formRef.value.validate((valid) => {
    if(valid) {
      post('/api/auth/reset-password', {...form}, () => {
        ElMessage.success('密码重置成功，请重新登录')
        router.push('/')
      })
    }
  })
}
</script>

<template>
  <div class="text-center mx-5">
    <div class="mt-30px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="验证电子邮件"></el-step>
        <el-step title="重置密码"></el-step>
      </el-steps>
    </div>
    <div v-if="active === 0">
      <div class="mt-80px">
        <div class="text-25px font-bold">重置密码</div>
        <div class="text-14px text-gray-500">请输入需要重置密码的电子邮件地址</div>
      </div>
      <div class="mt-50px">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email" type="email" placeholder="电子邮件地址">
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
                <el-input v-model="form.code" maxlength="6" type="text" placeholder="请输入验证码">
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
        <div class="mt-80px">
          <el-button @click="confirmReset" class="w-270px" type="primary" plain>验证电子邮件</el-button>
        </div>
      </div>
    </div>
    <div class="mx-5" v-if="active === 1">
      <div class="mt-80px">
        <div class="text-25px font-bold">重置密码</div>
        <div class="text-14px text-gray-500">请设置新的密码，务必牢记，以防丢失</div>
      </div>
      <div class="mt-50px">
        <el-form :model="form" :rules="rules" ref="formRef">
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
        </el-form>
        <div class="mt-80px">
          <el-button @click="doReset" class="w-270px" type="danger" plain>立即重置密码</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
