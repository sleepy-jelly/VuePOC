<template>
    <div class="container">
      <section class="section register min-vh-100 d-flex flex-column align-items-center justify-content-center py-4">
        <div class="container">
          <div class="row justify-content-center">
            <div class="col-lg-4 col-md-6 d-flex flex-column align-items-center justify-content-center">
              <div class="d-flex justify-content-center py-4">
                <router-link to="/login/login-page" class="logo d-flex align-items-center w-auto">
                  <span class="d-none d-lg-block">sleepy-jelly VuePOC</span>
                </router-link>
              </div>
  
              <div class="card mb-3">
                <div class="card-body">
                  <div class="pt-4 pb-2">
                    <h5 class="card-title text-center pb-0 fs-4">Login to Your Account</h5>
                    <p class="text-center small">Enter your userId & password to login</p>
                  </div>
  
                  <div v-if="errors.general" class="alert alert-danger" role="alert">
                    {{ errors.general }}
                  </div>
  
                  <form @submit.prevent="handleSubmit" class="row g-3 needs-validation" novalidate>
                    <div class="col-12">
                      <label for="userId" class="form-label">User ID</label>
                      <div class="input-group has-validation">
                        <span class="input-group-text" id="inputGroupPrepend">@</span>
                        <input
                          id="userId"
                          type="text"
                          class="form-control"
                          :class="{ 'is-invalid': errors.userId }"
                          v-model="formData.userId"
                          :disabled="isLoading"
                          required
                        />
                        <div class="invalid-feedback" v-if="errors.userId">{{ errors.userId }}</div>
                      </div>
                    </div>
  
                    <div class="col-12">
                      <label for="userPw" class="form-label">Password</label>
                      <input
                        id="userPw"
                        type="password"
                        class="form-control"
                        :class="{ 'is-invalid': errors.userPw }"
                        v-model="formData.userPw"
                        :disabled="isLoading"
                        required
                      />
                      <div class="invalid-feedback" v-if="errors.userPw">{{ errors.userPw }}</div>
                    </div>
  
                    <div class="col-12">
                      <div class="form-check">
                        <input
                          id="rememberMe"
                          type="checkbox"
                          class="form-check-input"
                          v-model="formData.rememberMe"
                          :disabled="isLoading"
                        />
                        <label for="rememberMe" class="form-check-label">Remember me</label>
                      </div>
                    </div>
  
                    <div class="col-12">
                      <button class="btn btn-primary w-100" type="submit" :disabled="isLoading">
                        <template v-if="isLoading">
                          <span class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                          Logging in...
                        </template>
                        <template v-else>
                          Login
                        </template>
                      </button>
                    </div>
  
                    <div class="col-12">
                      <p class="small mb-0">
                        Don't have an account?
                        <a href="/api/login/viewPageRegister">Create an account</a>
                      </p>
                    </div>
                  </form>
                </div>
              </div>
  
            </div>
          </div>
        </div>
      </section>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import apiRequester from '@/Api/AxiosInstance'; // Update this path to your Axios instance
  
  interface LoginFormData {
    userId: string;
    userPw: string;
    rememberMe: boolean;
  }
  
  interface LoginError {
    userId?: string;
    userPw?: string;
    general?: string;
  }
  
  const router = useRouter();
  const isLoading = ref(false);
  
  const formData = reactive<LoginFormData>({
    userId: '',
    userPw: '',
    rememberMe: false
  });
  
  const errors = reactive<LoginError>({});
  
  const validateForm = (): boolean => {
    errors.userId = '';
    errors.userPw = '';
    errors.general = '';
  
    let isValid = true;
  
    if (!formData.userId.trim()) {
      errors.userId = 'User ID is required';
      isValid = false;
    }
  
    if (!formData.userPw) {
      errors.userPw = 'Password is required';
      isValid = false;
    }
  
    return isValid;
  };
  
  const handleSubmit = async () => {
    if (!validateForm()) return;
  
    isLoading.value = true;
  
    const params = new URLSearchParams();
    params.append('userId', formData.userId);
    params.append('userPw', formData.userPw);
  
    try {
      const response = await apiRequester.post('/login/loginProcess', params, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      });
  
      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        apiRequester.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;
        router.push('/dash-board');
      } else {
        errors.general = 'Invalid credentials';
      }
    } catch (err) {
      console.error('Login error:', err);
      errors.general = 'An error occurred during login. Please try again.';
    } finally {
      isLoading.value = false;
    }
  };
  </script>
<style scoped>
  @import '../../src/index.css';
</style>