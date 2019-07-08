import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/views/Home.vue'
import Hotel from '@/components/hotel/Hotel'
import User from '@/components/users/User'
import Property from '@/components/sample/fullForm/Property'
import PropertySplit from '@/components/sample/dynamicForm/PropertySplit'
import Property2 from '@/components/sample/dynamicForm2/Property2'
Vue.use(Router)
import { defaultLocale } from '../components/i18n/i18n'
export default new Router({
  routes: [
    {
        path: '/home',
        name: 'home',
        component: Home
    },
    {
      path: '/',
      redirect: `/${defaultLocale}`,
    },
    {
      path: '/:locale',
      component: {
        template: '<router-view />',
      },
      children: [
        {
          path: 'home',
          component: Home
        },
        {
          path: 'hotel/:id',
          component: {
            template: '<div>hotelShow</div>',
          },
        },
        {
          path: 'hotel',
          component: Hotel
        },
        {
          path: 'users',
          component: User
        },
        {
          path: 'property',
          component: Property
        },
        {
          path: 'propertysplit',
          component: PropertySplit
        },
        {
          path: 'property2',
          component: Property2
        },
      ],
    },
    {
      path: '/hotel',
      name: 'hotel',
      component: Hotel
    },
    {
      path: '/users',
      name: 'users',
      component: User
    }
  ]
})
