import api from './api'

export const cartService = {
  getCart: () => {
    return api.get('/cart')
  },

  addToCart: (productId, quantity) => {
    return api.post('/cart/items', { productId, quantity })
  },

  updateCartItem: (itemId, quantity) => {
    return api.put(`/cart/items/${itemId}`, { quantity })
  },

  removeFromCart: (itemId) => {
    return api.delete(`/cart/items/${itemId}`)
  },

  clearCart: () => {
    return api.delete('/cart')
  },
}
