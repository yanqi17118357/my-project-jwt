import { unauthorized } from '@/net';
import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'welcome',
      component: () => import('@/views/WelcomeView.vue'),
      children: [
        {
          path: '',
          name: 'welcome-login',
          component: () => import('@/views/welcome/LoginPage.vue'),
        }, {
          path: 'register',
          name: 'welcome-register',
          component: () => import('@/views/welcome/RegisterPage.vue'),
        }, {
          path: 'reset',
          name: 'welcome-reset',
          component: () => import('@/views/welcome/ResetPage.vue'),
        }
      ]
    }, {
      path: '/index',
      name: 'index',
      component: () => import('@/views/IndexView.vue'),
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isUnauthorized = unauthorized()
  // 用户如果已经登录就不让他回到登录或者注册页面了，如果没有登录则不能访问主页面
  if (to.name.startsWith('welcome-') && !isUnauthorized) {
    next('/index')
  } else if (to.fullPath.startsWith('/index') && isUnauthorized) {
    next('/')
  } else {
    next()
  }
})

export default router
