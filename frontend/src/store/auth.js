const auth = {
  namespaced: true,
  state: {
    isAuthenticated: false
  },
  mutations: {
    isAuthenticated (state, payload) {
      state.isAuthenticated = payload
    }
  },
  actions: {
    login ({commit}, payload) {
      commit('isAuthenticated', payload)
    },
    logout ({commit}) {
      commit('isAuthenticated', false)
    }
  },
  getters: {
    isAuthenticated (state) {
      return  state.isAuthenticated
    }
  }
}

export default auth
