## my-project-jwt

SpringBoot3+Vue3 前后端分离项目 基于Jwt的校验方案


### 密码重置接口设计
#### 方案一
用户先带着验证码请求对应接口，然后后端存储对应用户已经通过的标记，用户填写新的验证码之后，然后请求重置密码的接口，接口验证是否已经通过，然后才重置密码

#### 方案二

用户带着验证码请求对应接口，然后后端仅对验证码是否正确进行验证，用户填写新的密码之后，请求重置密码接口，不仅需要带上密码还要之前的验证码一起，然后再次验证验证码，如果正确，就重置密码
