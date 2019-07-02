<template>
  <div id="custom1">


    <ul v-show="errors.length>0"  class="errors">
      <li v-for="error in errors">
      actual_message: {{error}} -
      -- translated_code:  {{$t(error)}}
    </li>
    </ul>
    <search-form v-model="search"
                 @submit="searchHotels()"
                 :submittedForm="submittedForm"
                 :masterUser="masterUser"
                 @current-hotel="currentHotel"
                 @hotel-update="updateHotels"
                 @refresh-list="refreshHotels"
                 @hotel-errors="errorHotels"
    ></search-form>


    <ul v-show="successAdded"  class="success">
      <li >
      {{successAdded}}
    </li></ul>

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

        socket: null,
        submittedForm:false,
        submittedEdit:false,
        status: "disconnected",
        errors:[],
        successAdded:null,
        hotels: [],
        search:{name:''},
        hotel: {id:'',currentUser:''},
        serverURL: process.env.SERVER_URL,
        max: 10,
        offset:0,
        currentPage:1,
        currentSort:'',
        currentSortDir:'asc'
      }
    },
    created () {
      this.fetchData()

      /**
       * This is also sent as part of form submission - meaning to actually properly use this validation which is processed
       * via events to an undefined command handler - the currentUser is stored in physical Command.java object that exists on
       * all microservices
       */
      this.masterUser=Math.random().toString(36).substring(2) + (new Date()).getTime().toString(36);
      this.hotel.currentUser=this.masterUser;
    },
    computed:{
      ...mapGetters([
        'loadHotels',
        'total',
        'numberOfPages'

      ]),
    realHotels () {
      return this.loadHotels;

    },
    realTotal () {
      return this.total;
    },
    realPages () {
      return this.numberOfPages;
    },
    enableSearch() {
      return this.showSearch===true;
    }
  },
    methods: {


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
        this.$store.dispatch( {type:'initHotels',params:params});
      },
      pagechanged: function(page) {
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
        this.fetchHotels(0)
      },

      removeHotel: function (hotel) {
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
        this.$store.dispatch( {type:'updateHotels',hotel:cv});
      },
      errorHotels: function (errors) {
        console.log(' AHHHH '+JSON.stringify(errors))
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
        this.initialiseHotels(variables);
      },
      searchHotels: function () {
        var variables = $.param(this.search);
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
