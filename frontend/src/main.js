// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import axios from 'axios'
import VueAxios from 'vue-axios'
import Vuetify from 'vuetify'
import VeeValidate from 'vee-validate';
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import './assets/css/grails.css'
import './assets/css/main.css'
import {Tabs, Tab} from 'vue-tabs-component';
import VueDropdown from 'vue-dynamic-dropdown'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret, faUser, faBars, faSignOutAlt , faPowerOff } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

Vue.config.productionTip = false
Vue.router = router;
Vue.use(VueAxios, axios);
Vue.use(BootstrapVue);
Vue.use(Vuetify);
Vue.use(VeeValidate);
Vue.component('vue-dropdown', VueDropdown);
Vue.component('tabs', Tabs);
Vue.component('tab', Tab);
Vue.config.productionTip = false;

library.add(faUserSecret)
library.add(faUser)
library.add(faBars)
library.add(faSignOutAlt)
library.add(faPowerOff)

Vue.component('font-awesome-icon', FontAwesomeIcon)

//Global mixin
Vue.mixin({
  methods: {
    pressed(val) {
      alert(val);
    }
  },
  data() {
    return {
      item: ''
    }
  }
});

/**
 * Below element provide a custom input which does pattern matching
 * <custom-input-required  pattern="(?=.*[A-Z]).{2,3}" v-model="hotel.name" title="Hotel code: Upper Case A-Z 2 to 3 characters only "></custom-input-required>
 *
 * <custom-input-required  pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}" v-model="hotel.name"
 * title="Must contain at least one number and one uppercase and lowercase letter, and be between 8 and 16 characters."></custom-input-required>
 */
Vue.component('custom-input-required',{
  props: ['pattern', 'title', 'vModel'],
  template:`<input type="text"  required :pattern="pattern" :title="title" :v-model="vModel"/>`
  }
);

/**
 * Below element provide a custom input - which does pattern matching
 * <custom-input pattern="(?=.*[A-Z]).{2,3}" v-model="hotel.name" title="Hotel code: Upper Case A-Z 2 to 3 characters only "></custom-input-required>
 *
 * <custom-input name="test" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}" v-model="hotel.name"
 * title="Must contain at least one number and one uppercase and lowercase letter, and be between 8 and 16 characters."></custom-input-required>
 */

Vue.component('custom-input',{
    props: ['pattern', 'title', 'vModel'],
    template:`<input type="text" :pattern="pattern" :title="title" :v-model="vModel"/>`
  }
)

// Global event bus
Vue.prototype.$eventHub = new Vue();


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },

  template: '<App/>'
})
