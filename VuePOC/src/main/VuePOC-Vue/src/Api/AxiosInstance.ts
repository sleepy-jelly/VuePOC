import axios, { type AxiosInstance } from 'axios';

const apiRequester: AxiosInstance = axios.create({
  baseURL: 'http://localhost:5173/api/',
  timeout: 5000,
}); 

export default apiRequester; //  default export 
  