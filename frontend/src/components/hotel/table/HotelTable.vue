<template>
  <div>
    <table class="table">
      <thead class="thead-inverse">
      <tr>
        <th @click="sort('id')" :class="{sortable: this.column === 'id'}">
          ID
          <span :class="{blank:this.column != 'id',
          arrow: (this.column === 'id'),
          asc: (this.column === 'id' && this.currentSortDir==='asc'),
           desc: this.column === 'id' && this.currentSortDir==='desc'}">
          </span>

        </th>
        <th @click="sort('name')" :class="{sortable: this.column === 'name'}">
          Name
          <span :class="{blank:(this.column != 'name'),
          arrow: (this.column === 'name'),
          asc: (this.column === 'name' && this.currentSortDir==='asc'),
           desc: (this.column ==='name' && this.currentSortDir==='desc')}">
          </span>
        </th>
        <th @click="sort('code')" :class="{sortable: this.column === 'code'}">
          Code
          <span :class="{blank:(this.column != 'code'),
          arrow: (this.column === 'code'),
          asc: (this.column === 'code' && this.currentSortDir==='asc'),
           desc: (this.column ==='code' && this.currentSortDir==='desc')}">
          </span>
        </th>
        <th @click="sort('phone')" :class="{sortable: this.column === 'phone'}">
          phone
          <span :class="{blank:(this.column != 'phone'),
          arrow: (this.column === 'phone'),
          asc: (this.column === 'phone' && this.currentSortDir==='asc'),
           desc: (this.column ==='phone' && this.currentSortDir==='desc')}">
          </span>
        </th>

        <th @click="sort('email')" :class="{sortable: this.column === 'email'}">
          Email
          <span :class="{blank:(this.column != 'email'),
          arrow: (this.column === 'email'),
          asc: (this.column === 'email' && this.currentSortDir==='asc'),
           desc: (this.column ==='email' && this.currentSortDir==='desc')}">
          </span>
        </th>
        <th @click="sort('updateUser')" :class="{sortable: this.column === 'updateUser'}">
          updateUser
          <span :class="{blank:(this.column != 'updateUser'),
          arrow: (this.column === 'updateUser'),
          asc: (this.column === 'updateUser' && this.currentSortDir==='asc'),
           desc: (this.column ==='updateUser' && this.currentSortDir==='desc')}">
          </span>
        </th>
        <th @click="sort('lastUpdated')"  :class="{sortable: this.column === 'lastUpdated'}">
          last Updated
          <span :class="{blank:(this.column != 'lastUpdated'),
          arrow: (this.column === 'lastUpdated'),
          asc: (this.column === 'lastUpdated' && this.currentSortDir==='asc'),
           desc: (this.column ==='lastUpdated' && this.currentSortDir==='desc')}">
          </span>
        </th>
        <th>Action</th>
      </tr>
      </thead>
      <table-row v-for="hotel in hotels"
                 :key="hotel.id"
                 :hotel="hotel"
                 @hotel-update="updateHotels"
                 @refresh-list="refreshHotels"
                 @hotel-show="showHotel"
                 @remove-hotel="removeHotel"
                 @hotel-errors="errorHotels"
                 v-bind="{fetchHotels}"
      ></table-row> <!--2-->
    </table>

    <display-hotel-modal
      :show="showModal"
      :hotel="currentShowHotel"
      @close="closeModalPopup"></display-hotel-modal>

  </div>
</template>

<script>
import TableRow from './TableRow.vue';
import DisplayHotelModal from './DisplayHotelModal.vue';
export default {
   props: ['hotels', 'reload','fetchHotels','sortSearch'],
  components: { // <3>
    TableRow,
    DisplayHotelModal
  },
  data: function () {
  return {
    updatedResults:null,
    currentShowHotel:null,
    currentSort:'contractName',
    currentSortDir:'asc',
    column:'id',
    activeColumn:'id',
    currentStyle:'',
    showModal:false
  }
},

  methods: {
    sort: function (s) {
      this.column=s;
      this.activeColumn=s;
      if (s === this.currentSort) {
        this.currentSortDir = this.currentSortDir === 'asc' ? 'desc' : 'asc';

      }
      this.currentSort = s;
      this.sortSearch(s,this.currentSortDir)

    },
     updateHotels: function (hotel) {
        this.$emit('hotel-update',hotel);
      },
    removeHotel: function (hotel) {
      this.$emit('remove-hotel',hotel);
    },
    showHotel: function (hotel) {
      this.currentShowHotel=hotel;
      this.showModal=true;
      //
    },
    closeModalPopup: function () {
      this.showModal = false;
      this.currentShowHotel = null;
    },
    refreshHotels: function () {
      this.$emit('refresh-list');
    },
   errorHotels: function (errors) {
      this.$emit('hotel-errors',errors);
    },
  }
}
</script>

<style>
  th.active .arrow {
    opacity: 1;
  }

  .arrow {
    display: inline-block;
    vertical-align: middle;
    width: 0;
    height: 0;
    margin-left: 5px;
    opacity: 0.66;
  }

  .arrow.asc {
    border-left: 4px solid transparent;
    border-right: 4px solid transparent;
    border-bottom: 4px solid #42b983;
  }

  .arrow.desc {
    border-left: 4px solid transparent;
    border-right: 4px solid transparent;
    border-top: 4px solid #42b983;
  }
  th.active {
    color:orange !important;
  }


  blank {

  }
</style>
