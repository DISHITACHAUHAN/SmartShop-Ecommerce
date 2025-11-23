import api from './api'

export const authService = {
  register: (userData) => {
    return api.post('/auth/register', userData)
  },

  login: (credentials) => {
    return api.post('/auth/login', credentials)
  },

  getUserProfile: () => {
    return api.get('/auth/profile')
  },

  updateProfile: (userData) => {
    return api.put('/auth/profile', userData)
  },
}
