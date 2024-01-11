import axios from "axios";
import { ElMessage } from "element-plus";

const authItemName = "access_token"

const defaultFailure = (message, code, url) => {
  console.warn(`请求地址：${url}，状态码：${code}，错误信息：${message}`)
  ElMessage.warning(message)
}

const defaultError = (err) => {
  console.error(err)
  ElMessage.warning('发生了一些错误，请联系管理员')
}

function takeAccessToken() {
  const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName)
  if (!str) return null
  const authObj = JSON.parse(str)
  if (authObj.expire <= new Date()) {
    deleteAccessToken()
    ElMessage.warning('登录状态已过期，请重新登录')
    return null
  }
  return authObj.token
}

function storeAccessToken(token, remember, expire) {
  const authObj = { token: token, expire: expire }
  const str = JSON.stringify(authObj)
  if (remember)
    localStorage.setItem(authItemName, str)
  else
    sessionStorage.setItem(authItemName, str)
}

function deleteAccessToken() {
  localStorage.removeItem(authItemName)
  sessionStorage.removeItem(authItemName)
}

function internalPost(url, data, header, success, failure, error = defaultError) {
  axios.post(url, data, { headers: header }).then(({ data }) => {
    if (data.code === 200) {
      success(data.data)
    } else {
      failure(data.message, data.code, url)
    }
  }).catch(err => error(err))
}

function internalGet(url, header, success, failure, error = defaultError) {
  axios.get(url, { headers: header }).then(({ data }) => {
    if (data.code === 200) {
      success(data.data)
    } else {
      failure(data.message, data.code, url)
    }
  }).catch(err => error(err))
}

function login(username, password, remember, success, failure = defaultFailure) {
  internalPost('/api/auth/login', {
    username: username,
    password: password
  }, {
    'Content-Type': 'application/x-www-form-urlencoded'
  }, (data) => {
    storeAccessToken(data.token, remember, data.expire)
    ElMessage.success(`登录成功，欢迎 ${data.username} 来到我们的系统`)
    success(data)
  }, failure)
}

export { login }
