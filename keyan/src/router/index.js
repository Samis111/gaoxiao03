import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginPage from '../views/LoginPage.vue'
import RegisterPage from '../views/RegisterPage.vue'
import StudentHome from '../components/StudentHome.vue'
import AdminHome from '../components/AdminHome.vue'
import Password from '../views/student/Password.vue'
import ProfileForm from '../views/student/ProfileForm.vue'
import Pigeonhole from '../views/student/Pigeonhole.vue'
import ResearchDeclaration from '../views/student/ResearchDeclaration.vue'
import ResearchDeclarationForm from '../views/student/ResearchDeclarationForm.vue'
import ReviewStatus from '../views/student/ReviewStatus.vue'
import ProjectDetails from '../views/student/ProjectDetails.vue'
import ProjectManagement from '../views/admin/ProjectManagement.vue'
import AReviewStatus from '../views/admin/AReviewStatus.vue'
import AProjectDetails from '../views/admin/AProjectDetails.vue'
import ArchivePage from '../views/admin/ArchivePage.vue'
import UserManagement from '../views/admin/UserManagement.vue'
import UserManagement1 from '../views/admin/UserManagement1.vue'



import Pigeonholes from '../views/admin/Pigeonholes.vue'
import ProfessionalManagement from '@/views/admin/professionalManagement.vue'
import InstituteManagement from '@/views/admin/instituteManagement.vue'

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Login',
    component: LoginPage,
    meta: { show: true, public: true }
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterPage,
    meta: { show: true, public: true }
  },
  {
    path: '/student-home',
    name: 'StudentHome',
    component: StudentHome,
    meta: { requiresAuth: true, role: 'student', show: false },
    children: [
      {
        path: 'password',
        component: Password
      },
      {
        path: 'profile-form',
        component: ProfileForm
      },
      {
        path: 'research-declaration',
        component: ResearchDeclaration
      },
      {
        path: 'research-declaration-form',
        component: ResearchDeclarationForm
      },
      {
        path: 'review-status',
        component: ReviewStatus,
      },
      {
        path: 'project-details',
        component: ProjectDetails
      },
      {
        path: 'pigeonhole',
        component: Pigeonhole,
      }
    ]
  },
  {
    path: '/admin-home',
    name: 'AdminHome',
    component: AdminHome,
    meta: { requiresAuth: true, role: 'admin', show: false },
    children: [
      {
        path: 'project-management',
        component: ProjectManagement
      },
      {
        path: 'review-status',
        component: AReviewStatus
      },
      {
        path: 'project-details',
        component: AProjectDetails
      },
      {
        path: 'archive-page',
        component: ArchivePage
      },
      {
        path: 'user-management',
        component: UserManagement
      },
      {
        path: 'user-management1',
        component: UserManagement1
      },

      {
        path: 'pigeonholes',
        component: Pigeonholes
      },
      {
        path: 'instituteManagement',
        component: InstituteManagement
      },
      {
        path: 'professionalManagement',
        component: ProfessionalManagement
      }
    ]
  },
  {
    path: '/superadmin-home',
    name: 'AdminHome',
    component: AdminHome,
    meta: { requiresAuth: true, role: 'admin', show: false },
    children: [
      {
        path: 'project-management',
        component: ProjectManagement
      },
      {
        path: 'review-status',
        component: AReviewStatus
      },
      {
        path: 'project-details',
        component: AProjectDetails
      },
      {
        path: 'archive-page',
        component: ArchivePage
      },
      {
        path: 'user-management',
        component: UserManagement
      },
      {
        path: 'pigeonholes',
        component: Pigeonholes
      },
      {
        path: 'instituteManagement',
        component: InstituteManagement
      },
      {
        path: 'professionalManagement',
        component: ProfessionalManagement
      }
    ]
  },
  {
    path: '/',
    name: 'Login',
    component: LoginPage,
    meta: { show: true }
  },
  {
    path: '/admin-home/archive-page',
    name: 'ArchivePage',
    component: () => import('@/views/admin/Pigeonholes.vue'),
    props: (route) => ({
      id: route.query.id,
      projectName: route.query.projectName,
      submitter: route.query.submitter,
      submissionDate: route.query.submissionDate
    })
  },
]

const router = new VueRouter({
  routes,
  routes,
  mode: 'history',
  base: process.env.BASE_URL,
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isLogin = sessionStorage.getItem('isLogin') === 'true'; // 确保是布尔值
  const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
  console.log('Route guard:', {
    isLogin,
    userInfo,
    path: to.path,
    role: userInfo.role
  });

  // 不需要登录的路由
  const publicPages = ['/', '/register'];

  // 如果是公共页面
  if (publicPages.includes(to.path)) {
    // 已登录用户访问登录页或注册页，重定向到对应首页
    if (isLogin) {
      if (userInfo.role === 'student') {
        next('/student-home');
      } else if (userInfo.role === 'admin') {
        next('/admin-home');
      } else if (userInfo.role === 'superadmin') {
        next('/superadmin-home');
      } else {
        next('/'); // 默认重定向到登录页
      }
    } else {
      next(); // 未登录用户访问公共页面，直接放行
    }
    return;
  }

  // 需要登录但未登录，重定向到登录页
  // if (!isLogin) {
  //   console.log('需要登录，重定向到登录页');
  //   next('/');
  //   return;
  // }

  // 检查角色权限
  // if (to.path.startsWith('/admin') && userInfo.role !== 'admin') {
  //   console.log('非管理员访问管理页面');
  //   next('/student-home'); // 或者重定向到无权限页面
  // } else if (to.path.startsWith('/student') && userInfo.role !== 'student') {
  //   console.log('非学生访问学生页面');
  //   next('/admin-home'); // 或者重定向到无权限页面
  // } else if (to.path.startsWith('/superadmin') && userInfo.role !== 'superadmin') {
  //   console.log('非超级管理员访问超级管理员页面');
  //   next('/superadmin-home'); // 或者重定向到无权限页面
  // } else {
    next(); // 角色匹配，放行
  // }

  if (to.meta.public) {
    if (isLogin) {
      next(userInfo.role === 'student' ? '/student-home' : userInfo.role === 'admin' ? '/admin-home' : '/superadmin-home');
    } else {
      next();
    }
  } else if (to.meta.requiresAuth && !isLogin) {
    next('/');
  } else if (to.meta.role && userInfo.role !== to.meta.role) {
    next(userInfo.role === 'student' ? '/student-home' : userInfo.role === 'admin' ? '/admin-home' : '/superadmin-home');
  } else {
    next();
  }
});

export default router
