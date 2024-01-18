<script setup>
import { EditPen, Lock, Message } from '@element-plus/icons-vue';
import { reactive, ref } from 'vue';

const active = ref(0)
const form = reactive({
  email: '',
  code: '',
  password: '',
  confirm_password: '',
})
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
        <el-form :model="form">
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
                <el-button type="primary">获取验证码</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
        <div class="mt-80px">
          <el-button @click="active = 1" class="w-270px" type="primary" plain>验证电子邮件</el-button>
        </div>
      </div>
    </div>
    <div class="mx-5" v-if="active === 1">
      <div class="mt-80px">
        <div class="text-25px font-bold">重置密码</div>
        <div class="text-14px text-gray-500">请设置新的密码，务必牢记，以防丢失</div>
      </div>
      <div class="mt-50px">
        <el-form :model="form">
          <el-form-item prop="password">
            <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
              <template #prefix>
                <el-icon>
                  <Lock />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="confirm_password">
            <el-input v-model="form.confirm_password" maxlength="20" type="password" placeholder="确认密码">
              <template #prefix>
                <el-icon>
                  <Lock />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
        <div class="mt-80px">
          <el-button @click="active++" class="w-270px" type="danger" plain>立即重置密码</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
