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

const updateObjectsInArr = function(initialArr, newArr) {
  let finalUpdatedArr = initialArr;
  for (let i = 0; i < newArr.length; i++) {
    finalUpdatedArr = newObjInInitialArr(finalUpdatedArr, newArr[i]);
  }

  return finalUpdatedArr
}
const getters = {
    loadHotels() {
     // console.log('ahhh '+state.loadHotels)
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
      console.log("params = "+params.params)
    instance.get('/list?'+params.params)
      .then((response) =>{
        console.log('DATA '+JSON.stringify(response.data.instanceList));
        //commit('SET_STORE', response.data.instanceList,response.data.instanceTotal,response.data.numberOfPages);
        //commit('SET_TOTAL', response.data.instanceTotal);
        //commit('SET_PAGES', response.data.numberOfPages);

        state.loadHotels = response.data.instanceList;
        state.total = response.data.instanceTotal;
        state.numberOfPages = response.data.numberOfPages;

        //console.log(' -STATE 00>> '+JSON.stringify(state.loadHotels))
      });
    },
  updateHotels:  ({commit}, hotelObject) => {
      console.log('updatting '+JSON.stringify(hotelObject.hotel)+" with ID: "+hotelObject.hotel.id)
      state.loadHotels=updateObjectsInArr(state.loadHotels, [hotelObject.hotel])
  }
};

const mutations = {
    'SET_STORE' (state,hotels,total,numberOfPages) {
      console.log('sseting up hotels as '+JSON.stringify(hotels))
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
