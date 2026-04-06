import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080'
});

// Este "interceptor" roda antes de cada requisição que o axios faz
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token'); // Pega o token salvo no login

    if (token) {
        config.headers.Authorization = `Bearer ${token}`; // Adiciona o cabeçalho Bearer
    }

    return config;
}, (error) => {
    return Promise.reject(error);
});

export default api;