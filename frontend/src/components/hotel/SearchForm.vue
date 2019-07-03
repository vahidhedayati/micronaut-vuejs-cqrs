<template id="search-contract-template" xmlns="http://www.w3.org/1999/xhtml">
  <div>
    <tabs :options="{ defaultTabHash: 'search' }">
      <tab name="search">
    <div id="inputRow" class="row">
      <div class="col-sm-2">
        <div class="input-group">
          <input type="text" class="form-control" placeholder="name" v-model.lazy="search.name">
        </div>
      </div>
      <div class="col-sm-1">
        <div class="btn-group" role="group" aria-label="Search Hotels">
          <button type="button" class="btn btn-primary" @click="submit()">Search</button>
        </div>
      </div>
    </div>
      </tab>
      <tab name="add">
        <hotel-form
          @refresh-list="refreshHotels"
          @hotel-errors="errorHotels"
          :submittedForm="updateAddForm"
          :masterUser="masterUser"
          @current-hotel="currentHotel"
          @hotel-update="updateHotels"
        >
        </hotel-form>
      </tab>
      <app-header headerText="Hotel listing" headerImage="hotel"></app-header>
    </tabs>
  </div>
</template>
<script>
  import {Tabs, Tab} from 'vue-tabs-component';
  import HotelForm from './HotelForm'
  import AppHeader from '../AppHeader'

  export default {
    components: {
      Tabs,
      AppHeader,
      HotelForm
    },
    props: ['search','masterUser','submittedForm'],
    model: {
      prop: 'search',
      event: 'change',
    },
    methods: {
      submit () {
        this.$emit('submit')
      },
      refreshHotels: function () {
          this.$emit('refresh-list');
      },
      errorHotels: function (errors) {
          this.$emit('hotel-errors',errors);
      },
      currentHotel: function(ho) {
        this.$emit('current-hotel',  ho);
      },

      updateAddForm: function(ho) {
        this.submittedForm=ho;
      },
      updateHotels: function(cv) {
        this.$emit('hotel-update',  cv);
      },
    }
  }
</script>
<style>

</style>
