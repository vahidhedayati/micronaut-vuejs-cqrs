import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import VueAxios from 'vue-axios'
import createPersistedState from 'vuex-persistedstate'
import hotels from './modules/hotels';


Vue.use(Vuex)
Vue.use(VueAxios, axios)

const store = new Vuex.Store({
  plugins: [createPersistedState()],
  modules: {
    hotels
  }
})

export default store
