import api from './api'

export const productService = {
  getAllProducts: (filters = {}) => {
    return api.get('/products', { params: filters })
  },

  getProductById: (id) => {
    return api.get(`/products/${id}`)
  },

  searchProducts: (query) => {
    return api.get('/products/search', { params: { q: query } })
  },

  createProduct: (productData) => {
    return api.post('/products', productData)
  },

  updateProduct: (id, productData) => {
    return api.put(`/products/${id}`, productData)
  },

  deleteProduct: (id) => {
    return api.delete(`/products/${id}`)
  },
}
