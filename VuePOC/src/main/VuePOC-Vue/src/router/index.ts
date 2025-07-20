import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import UserDashBoardView from '@/views/UserDashBoardView.vue'
import GeneralQuestionView from '@/views/GeneralQuestionView.vue'
import AudioSummaryView from '@/views/AudioSummaryView.vue'
import ContactView from '@/views/ContactView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: LoginView,
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/dash-board',
      name: 'dash-board',
      component: UserDashBoardView,
      meta: { requiresAuth: true }
    },
    {
      path: '/OpenAI/general-question',
      name: 'OpenAI/general-question',
      component: GeneralQuestionView,
      meta: { requiresAuth: true }
    },
    {
      path: '/OpenAI/audio-summary',
      name: 'OpenAI/audio-summary',
      component: AudioSummaryView,
      meta: { requiresAuth: true }
    },
    {
      path: '/contact',
      name: 'contact',
      component: ContactView,
      meta: { requiresAuth: false }
    }
  ],
});

router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresAuth) {
    try {
      const res = await fetch('/api/login/checkSession', {
        method: 'GET',
        credentials: 'include'
      });
      const result = await res.json();
      
      if (result.authenticated) {
        next();
      } else {
        next('/login');
      }
    } catch (error) {
      console.error('Auth check failed:', error);
      next('/login');
    }
  } else {
    next();
  }
});

export default router;