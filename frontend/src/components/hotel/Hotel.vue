<template>
  <div id="custom1">
    <ul id="logs">
      <li v-for="log in logs" class="log">
        {{ log.event }}: {{ log.data }}
      </li>
    </ul>
    <!-- The refresh and hotel errors are being returned by CountryForm which is the add tab of search form -->
    <search-form v-model="search"
                 @submit="searchHotels()"
                 :submittedForm="submittedForm"
                 :masterUser="masterUser"
                 @current-hotel="currentHotel"
                 @hotel-update="updateHotels"
                 @refresh-list="refreshHotels"
                 @hotel-errors="errorHotels"
    ></search-form>

    <!-- any errors caused by any underlying processes on this page -->
    <ul v-show="errors.length>0"  class="errors"><li v-for="error in errors">
     actual_message: {{error}} -
             -- translated_code:  {{$t(error)}}
    </li></ul>
    <ul v-show="successAdded"  class="success">
      <li >
      {{successAdded}}
    </li></ul>

    <!-- this loads up the entire hotel listing -->
    <hotel-table :hotels="realHotels"
                 :submittedForm="submittedEdit"
                 :masterUser="masterUser"
                   v-bind="{fetchHotels,sortSearch}"
                   @hotel-update="updateHotels"
                    @current-hotel="currentHotel"
                 @form-status="updateAddForm"
                 @remove-hotel="removeHotel"
                   @refresh-list="refreshHotels"
                   @hotel-errors="errorHotels"
    >
    </hotel-table>


    <!-- generic pagination -->
    <Pagination
      :maxVisibleButtons=3
      :totalPages="numberOfPages"
      :total="total"
      @sortSearch="sortSearch"
      :currentPage="currentPage"
      @pagechanged="pagechanged"/>
  </div>
</template>

<script>
  import $ from 'jquery';

  import SearchForm from './SearchForm'
  import HotelService from '@/services/HotelService'
  import HotelTable from './table/HotelTable'
  import Pagination from '../Pagination'
  import moment from 'moment';
  import { mapGetters } from 'vuex'

  /*
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
  */

  export default {
    components: {
      SearchForm,

      HotelTable,
      Pagination,
      moment
    },
    data: function () {
      return {
        message: "",
        currentUser:"",
        masterUser:"",
        logs: [],
        socket: null,
        //socketIo:{currentUser:'', errors:{}, success:false, },
        submittedForm:false,
        submittedEdit:false,
        status: "disconnected",

        errors:[],
        successAdded:null,
        hotels: [],
        search:{name:''},
        hotel: {id:'',currentUser:''},
        serverURL: process.env.SERVER_URL,
        //total: 0,
        max: 10,
        offset:0,
        currentPage:1,
        //numberOfPages:0,
        currentSort:'',
        currentSortDir:'asc'
      }
    },
    created () {
      this.fetchData()
      this.connect();
      /**
       * This is also sent as part of form submission - meaning to actually properly use this validation which is processed
       * via events to an undefined command handler - the currentUser is stored in physical Command.java object that exists on
       * all microservices
       */
      this.masterUser=Math.random().toString(36).substring(2) + (new Date()).getTime().toString(36);
      this.hotel.currentUser=this.masterUser;
    },
    mounted: function () {	//#E

      console.log('hotels when mounted'+this.$store.getters.hotels);


    },
    computed:{
      ...mapGetters([
        'loadHotels',
        'total',
        'numberOfPages'

      ]),
    // Create another computed property to call your mapped getter while passing the argument
    realHotels () {
      // setTimeout(() => {return this.loadVehicles}, 1000)
      //setTimeout(() => {return this.$store.getters.loadVehicles()}, 1000)

      return this.loadHotels;

    },
    // Create another computed property to call your mapped getter while passing the argument
    realTotal () {
      return this.total;
    },
    // Create another computed property to call your mapped getter while passing the argument
    realPages () {
      return this.numberOfPages;
    },


    //This is now ignored this was sorting based on paginated data
    //simply replace vehicle in vehicles to vehicle in sortedCats above line 14/15
    enableSearch() {
      console.log(' +'+this.searchDetails+' '+this.showSearch)
      return this.showSearch===true;
    }
  },
    methods: {
      connect() {
        console.log("About to connect to websocket")
        this.socket = new WebSocket("ws://localhost:8082/ws/process");
        this.socket.onopen = () => {
          this.status = "connected";
          this.logs.push({ event: "Connected to", data: 'ws://localhost:8082'})
          console.log("Connected ")
          // this.socket.send(this.hotel.currentUser)
          this.socket.send(JSON.stringify({currentUser:this.hotel.currentUser,eventType:'userForm'}));
          /**
           * we only get a message back when something has gone wrong in gateway-command process action
           * or there was success on the form from handler as in validation checks passed
           */
          this.socket.onmessage = ({data}) => {
            console.log(" actual data"+data)
            console.log(' data '+JSON.parse(data).status)//JSON.parse(JSON.stringify(data)).currentUser)
            if (JSON.parse(data).currentUser===this.hotel.currentUser ) {
              var currentStatus = JSON.parse(data).status;
              console.log(" status "+currentStatus)
              if (currentStatus==='error') {
                this.submittedForm=false;
                console.log("Yes adding"+JSON.parse(data).errors.errorMessage+" has errors "+JSON.parse(data).errors)
                //errors:[],
                //JSON.parse(JSON.stringify(data)).errors.forEach((item) => {
                  //this.errors.push(item)
                //});
                this.errors=JSON.parse(data).errors
                //this.errorHotels(this.errors)
              } else if (currentStatus==='success') {
                this.submittedForm=false;
                //this.$emit('refresh-list')


                /**
                 * When we save a brand new record - we don't have its ID as yet - the command handler now does a lookup
                 * sets id in WebsocketMessage which is relayed back to user via websockets
                 * the hotel object locally was updated when user clicked submit they triggered event which sent that data
                 * to overwrite the hotel object in data above from hotelForm
                 * this now sets its actual id from remote action that was successful - making it full object
                 */
                this.hotel.id=JSON.parse(data).id

                /**
                 * This simply appends dynamic hotel back to the existing list refer to /store/models/hotels.js
                 */
                this.$store.dispatch( {type:'updateHotels',hotel:this.hotel});

                this.successAdded="Hotel "+this.hotel.name+" added";
                this.hotel={id:'',currentUser:''}
                //this.$emit('update-vehicles', this.hotel);

              }
            }
            this.logs.push({ event: "Recieved message", data });
          };
        };

      },

      disconnect() {
        this.socket.close();
        this.status = "disconnected";
        this.logs = [];
      },
      sendMessage() {
        console.log("Sending message "+this.hotel.currentUser)
        this.socket.send(JSON.stringify({currentUser:this.hotel.currentUser,eventType:'userForm'}));
        //this.logs.push({ event: "Sent message", data: this.message });
        this.message = "";
      },

      fetchData: async function () {
        try {
          Promise.all([
            this.fetchHotels(0)
          ])
        } catch (error) {
          console.log(error)
        }
      },
      initialiseHotels(params){
        console.log(' getting /list'+params)
        this.$store.dispatch( {type:'initHotels',params:params});
        /*

        // This is replaced by store.dispatch which handles lookup in vuex store /store/models/hotel.js defined in /store/index.js
        return HotelService.fetchRoot('/list?'+params)
          .then((res) => {
          if (res) {
            console.log('full results '+JSON.stringify(res));
            if (res.data.instanceList) {
              console.log("rr "+res.data.instanceList)
              this.hotels = res.data.instanceList;
              this.total=res.data.instanceTotal;
              this.numberOfPages=res.data.numberOfPages;
            } else {
              if (res.data) {
                //console.log("rr "+res.data.objects)
                this.hotels = res.data;
              }
            }
          }
        });
        */
      },
      pagechanged: function(page) {
        console.log("Page = "+page)
        this.currentPage = page;
        this.offset=(page*this.max)-this.max
        this.fetchHotels(this.offset)
      },
      sortSearch(currentSort,currentSortDir) {
        var variables = $.param(this.search);
        this.currentSort=currentSort;
        this.currentSortDir=currentSortDir;
        variables+="&sort="+currentSort+"&order="+currentSortDir+'&offset='+ this.offset;
        this.initialiseHotels(variables);
      },
      refreshHotels: function () {
        console.log('refreshing entire list after a new entry was added')
        this.fetchHotels(0)
      },
      /*updateHotels: function (hotel) {
        this.errors=[];
        console.log('hotel.vue updating hotel list')
        this.hotels=updateObjectsInArr(this.hotels, [hotel])
      },
      */
      removeHotel: function (hotel) {
        console.log('hotel.vue removing hotel from list')
        //this.$emit('remove-hotel',hotelId);
        this.$store.dispatch( {type:'removeHotel',hotel:hotel});
      },
      currentHotel: function(ho) {
        this.errors=[];
        this.successAdded=null;
        this.hotel=ho;
      },

      updateAddForm: function(ho) {
        this.submittedForm=ho;
      },

      updateHotels: function(cv) {
        //This is the Hotel object which is emitted by HotelTable to this parent page -
        // This now calls vuex storage and updates cache value with the new vehicle in its existing array of vehicles
        console.log('updateing updateHotels ---<<<<'+cv.id)
        this.$store.dispatch( {type:'updateHotels',hotel:cv});
      },
      errorHotels: function (errors) {
        console.log('hotelTable.vue updating error list')
        this.errors=errors;
      },
      fetchHotels: function (pageNumber) {
        var variables=''
        if (this.search) {
          variables += $.param(this.search);
        }
        if (this.currentSort) {
          variables+="&sort="+this.currentSort+"&order="+this.currentSortDir;
        }
        if (variables!='') {
          variables+='&offset='+pageNumber
        } else {
          variables='?offset='+pageNumber
        }
        console.log("Fetching hotels "+pageNumber)
        this.initialiseHotels(variables);
      },
      searchHotels: function () {
        var variables = $.param(this.search);
        console.log('variables: '+variables)
        this.initialiseHotels(variables);
      }
    }
  }
</script>
<style>
  #custom {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    text-align: center;
    color: #2c3e50;
  }
  .reduceZoom {
    zoom:25.50%;

  }
</style>
