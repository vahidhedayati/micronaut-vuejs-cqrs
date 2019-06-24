import axios from 'axios'
const instance = axios.create({
  baseURL: `http://localhost:8081/`
})
const state = {
    loadHotels: {},
    params:{},
    total: 0,
    numberOfPages: 0

};
const newObjInInitialArr = function(initialArr, newObject) {
  let id = newObject.id;
  let newArr = [];
  for (let i = 0; i < initialArr.length; i++) {
    if (id === initialArr[i].id) {
      newArr.push(newObject);
    } else {
      newArr.push(initialArr[i]);
    }
  }
  return newArr;
};
const removeObjInInitialArr = function(initialArr, newObject) {
  let id = newObject.id;
  let newArr = [];
  for (let i = 0; i < initialArr.length; i++) {
    if (id!= initialArr[i].id) {
      newArr.push(initialArr[i]);
    }
  }
  return newArr;
};

const updateObjectsInArr = function(initialArr, newArr) {
  let finalUpdatedArr = initialArr;
  for (let i = 0; i < newArr.length; i++) {
    finalUpdatedArr = newObjInInitialArr(finalUpdatedArr, newArr[i]);
  }

  return finalUpdatedArr
}
const removeObjectsInArr = function(initialArr, newArr) {
  let finalUpdatedArr = initialArr;
  for (let i = 0; i < newArr.length; i++) {
    finalUpdatedArr = removeObjInInitialArr(finalUpdatedArr, newArr[i]);
  }
  return finalUpdatedArr
}
const getters = {
    loadHotels() {
      return state.loadHotels;
    },
    total: state => state.total,
    numberOfPages: state => state.numberOfPages
};

/**
 * This is called through Hotel.vue - when initialiseHotels is called
 * it passes the params over to this action - also appended or defined in mutation below...
 * The output is then stored in this vuex object - called hotels -
 * hotels its total and number of pages become a vuex session contained list
 * each page change will update all these cache values
 * @type {boolean}
 */
const actions = {
    initHotels: ({commit}, params) => {
    instance.get('/list?'+params.params)
      .then((response) =>{
        state.loadHotels = response.data.instanceList;
        state.total = response.data.instanceTotal;
        state.numberOfPages = response.data.numberOfPages;
      });
    },
  updateHotels:  ({commit}, hotelObject) => {
    state.loadHotels=updateObjectsInArr(state.loadHotels, [hotelObject.hotel])
  },
  removeHotel:  ({commit}, hotelObject) => {
    state.loadHotels=removeObjectsInArr(state.loadHotels, [hotelObject.hotel])
  }
};

const mutations = {
    'SET_STORE' (state,hotels,total,numberOfPages) {
      state.loadHotels = hotels;
      state.total = total;
      state.numberOfPages = numberOfPages;
    }
};

export default {
    state,
    getters,
    actions,
  mutations: {
    initHotels(state, { params }) {
      localStorage.setItem('params', params)

    }
  }
}
