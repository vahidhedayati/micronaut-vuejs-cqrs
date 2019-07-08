<template>
<div>
actual raw form from Dynamic Form original --
<div class="form-group form-row">
  <label class=" col-sm-2 col-form-label mr-2">
    Property Name <span class="required-indicator"> * </span>
  </label>
  <div class="col-sm-6">
    <input type="text"  class="form-control" >
  </div>
</div>
<div class="form-group form-row">
  <label class=" col-sm-2 col-form-label mr-2">
    Street Address <span class="required-indicator"> * </span>
  </label>
  <div class="col-sm-6">
    <input type="text"  class="form-control" >
    <span class="new-line">&nbsp</span>
      <input type="text"  class="form-control" >
  </div>
</div>
<div class="form-group form-row">
  <label class=" col-sm-2 col-form-label mr-2">
    City <span class="required-indicator"> * </span>
  </label>
  <div class="col-sm-4">
    <autocomplete form-field="search"
                @key-press="updateAutoCompleteItems"
                @search-value="updateSearchValue"
                @search-key="updateSearchKey"
                placeholder="Enter or select City"
                key-field="customId" value-field="customName"
                :items="[ {customName : 'London', customId:'1'} , {customName:'Manchester', customId:'2'} ]" />
  </div>
</div>

<div class="form-group form-row">
  <label class=" col-sm-2 col-form-label mr-2">
   Country/State
  </label>
  <div class="col-sm-4">
    <input type="text"  class="form-control" >
  </div>
</div>
<div class="form-group form-row">
  <label class=" col-sm-2 col-form-label mr-2">
    Post/Zip Code
  </label>
  <div class="col-sm-4">
    <input type="text"  class="form-control" >
  </div>
</div>

<div class="form-group form-row">
  <label class=" col-sm-2 col-form-label mr-2">
    Country <span class="required-indicator"> * </span>
  </label>
  <div class="col-sm-4">
    <autocomplete form-field="search"
                  @key-press="updateAutoCompleteItems"
                  @search-value="updateSearchValue"
                  @search-key="updateSearchKey"
                  placeholder="Enter or select Country"
                  key-field="customId" value-field="customName"

                  :items="[ {customName : 'United Kingdom', customId:'1'} , {customName:'United States', customId:'2'} ]" />

  </div>
</div>
<div class="form-group form-row">
  <label class=" col-sm-2 col-form-label mr-2">
    Phone Number <span class="required-indicator"> * </span>
  </label>
  <div class="col-sm-4">
    <input type="text"  class="form-control" >
  </div>
</div>

<div class="form-group form-row">
  <label class=" col-sm-2 col-form-label mr-2">
    Email Address <span class="required-indicator"> * </span>
  </label>
  <div class="col-sm-6">
    <input type="text"  class="form-control" >
  </div>
</div>

</div>
</template>

<script>
  import Autocomplete from '@/components/form/Autocomplete'

  export default {
    components: {


      Autocomplete

    },
     methods: {
           sort: function (s) {
             this.column=s;
             this.activeColumn=s;
             if (s === this.currentSort) {
               this.currentSortDir = this.currentSortDir === 'asc' ? 'desc' : 'asc';

             }
             this.currentSort = s;


           },
           updateAutoCompleteItems: function (searchValue) {
             if (searchValue.length>2) {

             }
           },
           updateSearchValue: function (value) {
             //this.hotel.updateUserName=value
           },
           updateSearchKey: function (key) {
             //this.hotel.updateUserId=key
           },
         },
  }
</script>
