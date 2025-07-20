<script lang="ts" setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import apiRequester from '@/Api/AxiosInstance';

//HashMap to Record  -> when having validated Error will save here
const fieldErrors = ref<Record<string, string>>({});


const router = useRouter();

const inputUserId = ref('');
const inputUserPw = ref('');
const inputUserNm = ref('');
const inputUserAfltNm = ref('');
const inputUserMblTelno = ref('');
const inputUserEmlAddr = ref('');
const inputGndrCd = ref(''); 
const inputUntyTrmsAgreYn = ref(false);

const submitted = ref(false);

const isFormValid = () => {
  return (
    inputUserId.value.trim() !== '' &&
    inputUserPw.value.trim() !== '' &&
    inputUserNm.value.trim() !== '' &&
    inputUserAfltNm.value.trim() !== '' &&
    inputUserEmlAddr.value.trim() !== '' &&
    inputUntyTrmsAgreYn.value
  );
};

const signUp = () => {
  submitted.value = true;


  // clear previous errors
  fieldErrors.value = {};


  if (!isFormValid()) {
    return;
  }

  apiRequester.post('/login/registerProcess', {
    userId: inputUserId.value,
    userPw: inputUserPw.value,
    userNm: inputUserNm.value,
    userAfltNm: inputUserAfltNm.value,
    userMblTelno: inputUserMblTelno.value,
    userEmlAddr: inputUserEmlAddr.value,
    gndrCd: inputGndrCd.value,
    untyTrmsAgreYn: inputUntyTrmsAgreYn.value ? 'Y' : 'N'
  })
  .then(response => {
    router.push('/login');
  })
  .catch(error => {
    if (error.response?.status === 400 && Array.isArray(error.response.data.errors)) {
      const mappedErrors: Record<string, string> = {};

      const errorMsg: string[] = [];

      error.response.data.errors.forEach((err: any) => {
        if (err.field && err.defaultMessage) {
          mappedErrors[err.field] = err.defaultMessage
          errorMsg.push(err.defaultMessage +' \n')
        }
      });
      alert(errorMsg)

      fieldErrors.value = mappedErrors;
      console.log('Backend validation errors:', mappedErrors);
    } else if(error.response?.status === 409){
      alert("Username (userId) already exists. Please choose a different Username")
    } 
  })
};

</script>

<template>
  <main>
      <div class="row justify-content-center">
        <div class="col-md-6 col-md-8">
          <!-- form card here -->
              <div class="row justify-content-center">
                <div class="col-lg-4 col-md-6 d-flex flex-column align-items-center justify-content-center">
                  <div class="d-flex justify-content-center py-4">
                    <a href="/home" class="logo d-flex align-items-center w-auto">
                      <span class="d-none d-lg-block">sleepy-jelly VuePOC</span>
                    </a>
                  </div>

                  <div class="card mb-3">
                    <div class="card-body">
                      <div class="pt-4 pb-2">
                        <h5 class="card-title text-center pb-0 fs-4">Create an Account</h5>
                        <p class="text-center small">Enter your personal details to create account</p>
                      </div>

                      <form class="row g-3" @submit.prevent="signUp">
                        <!-- Name -->
                        <div class="col-12">
                          <label class="form-label">Your Name</label>
                          <input v-model="inputUserNm" type="text" class="form-control"
                                :class="{ 'is-invalid': submitted && !inputUserNm.trim() }" />
                          <div v-if="submitted && !inputUserNm.trim()" class="invalid-feedback">
                            Please enter your name.
                          </div>
                          <!-- <span v-if="fieldErrors.userNm">{{ fie ldErrors.userNm }}</span> -->
                        </div>

                        <!-- Affiliation -->
                        <div class="col-12">
                          <label class="form-label">Your Affiliation Name</label>
                          <input v-model="inputUserAfltNm" type="text" class="form-control"
                                :class="{ 'is-invalid': submitted && !inputUserAfltNm.trim() }" />
                          <div v-if="submitted && !inputUserAfltNm.trim()" class="invalid-feedback">
                            Please enter your affiliation name.
                          </div>
                        </div>

                        <!-- Email -->
                        <div class="col-12">
                          <label class="form-label">Your Email</label>
                          <input v-model="inputUserEmlAddr" type="email" class="form-control"
                                :class="{ 'is-invalid': submitted && !inputUserEmlAddr.trim() }" />
                          <div v-if="submitted && !inputUserEmlAddr.trim()" class="invalid-feedback">
                            Please enter your email.
                          </div>
                        </div>

                        <!-- UserId -->
                        <div class="col-12">
                          <label class="form-label">Username</label>
                          <div class="input-group has-validation">
                            <span class="input-group-text">@</span>
                            <input v-model="inputUserId" type="text" class="form-control"
                                  :class="{ 'is-invalid': submitted && !inputUserId.trim() }" />
                            <div v-if="submitted && !inputUserId.trim()" class="invalid-feedback">
                              Please enter a username.
                            </div>
                          </div>
                        </div>

                        <!-- Password -->
                        <div class="col-12">
                          <label class="form-label">Password</label>
                          <input v-model="inputUserPw" type="password" class="form-control"
                                :class="{ 'is-invalid': submitted && !inputUserPw.trim() }" />
                          <div v-if="submitted && !inputUserPw.trim()" class="invalid-feedback">
                            Please enter your password.
                          </div>
                        </div>

                        <!-- UserMobileTelephoneNumber -->
                        <div class="col-12">
                          <label class="form-label">Telephone Number</label>
                          <div class="input-group has-validation">
                            <input v-model="inputUserMblTelno" type="tel" class="form-control"
                                  :class="{ 'is-invalid': submitted && !inputUserMblTelno.trim() }"  
                                   pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}"
                                   placeholder="010-1234-5678"

                                  />
                            <div v-if="submitted && !inputUserMblTelno.trim()" class="invalid-feedback">
                              Please enter a Telephone Number.
                            </div>
                          </div>
                        </div>


                        <!-- Terms -->
                        <div class="col-12">
                          <div class="form-check">
                            <input class="form-check-input" type="checkbox" v-model="inputUntyTrmsAgreYn"
                                  :class="{ 'is-invalid': submitted && !inputUntyTrmsAgreYn }" />
                            <label class="form-check-label">
                              I agree and accept the <a href="#">terms and conditions</a>
                            </label>
                            <div v-if="submitted && !inputUntyTrmsAgreYn" class="invalid-feedback">
                              You must agree before submitting.
                            </div>
                          </div>
                        </div>

                        <!-- Submit -->
                        <div class="col-12">
                          <button type="submit" class="btn btn-primary w-100">Create Account</button>
                        </div>

                        <!-- Already have account -->
                        <div class="col-12">
                          <p class="small mb-0">
                            Already have an account? <a href="/login">Log in</a>
                          </p>
                        </div>
                      </form>
                    </div>
                  </div>

                  <div class="credits">
                    Designed by <a href="https://bootstrapmade.com/">BootstrapMade</a>
                  </div>
                </div>
              </div>
            </div>
        </div>
  </main>
</template>


<style scoped>
  @import '../../src/index.css';
  input {
  border: 1px solid #9b3939;
}
form-control {
  border-bottom-color : rgb(134, 183, 2554);

}
</style>