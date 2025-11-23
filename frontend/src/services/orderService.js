import api from './api'

export const orderService = {
  createOrder: (orderData) => {
    return api.post('/orders', orderData)
  },

  getOrders: () => {
    return api.get('/orders')
  },

  getOrderById: (id) => {
    return api.get(`/orders/${id}`)
  },

  processPayment: (orderId, paymentData) => {
    return api.post(`/orders/${orderId}/payment`, paymentData)
  },

  downloadInvoice: (orderId) => {
    return api.get(`/orders/${orderId}/invoice`, { responseType: 'blob' })
  },
}
