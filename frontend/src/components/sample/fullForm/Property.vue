<template>
  <section>

    <div class="main-heading">
      Create a property
    </div>

      <div class="section-heading">
        Property Name & Address
      </div>
    <form >
      <div class="section">
        <span class="form-padding">&nbsp</span>
        <div class="form-group form-row">
          <label class=" col-sm-2 col-form-label mr-2">
            Property Name <span class="required-indicator"> * </span>
          </label>
          <div class="col-sm-6">
            <input type="text"  class="form-control" name="propertyName"  v-validate="'required|min:6|max:35'"  >
            <span v-show="errors.has('propertyName')" class="clear alert alert-danger">{{ errors.first('propertyName') }}</span>
          </div>
        </div>
        <div class="form-group form-row">
          <label class=" col-sm-2 col-form-label mr-2">
            Street Address <span class="required-indicator"> * </span>
          </label>
          <div class="col-sm-6">
            <input type="text"  class="form-control" name="address1" v-validate="'required|min:6|max:35'">
            <span v-show="errors.has('address1')" class="clear alert alert-danger">{{ errors.first('address1') }}</span>
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
                          fieldName="city"
                            @key-press="updateAutoCompleteItems"
                            @search-value="updateSearchValue"
                            @search-key="updateSearchKey"
                            placeholder="Enter or select City"
                            key-field="customId" value-field="customName"
 v-validate="'required|min:1'"
                            :items="[ {customName : 'London', customId:'1'} , {customName:'Manchester', customId:'2'} ]" />
 <span v-show="errors.has('city')" class="clear alert alert-danger">{{ errors.first('city') }}</span>
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

        <span class="form-padding">&nbsp</span>
      </div>
      <span class="form-padding">&nbsp</span>
      <div class="form-row  form-group">
        <div class="col-md-offset-3">
    <button class="btn btn-danger btn-save"@click.prevent="submitForm()">
      {{$t('save_button')}}
    </button>
        </div>

        <div class="col-md-offset-3">
    <button class="btn btn-cancel" @click="close()">
      {{$t('cancel_button')}}
    </button>
        </div>
      </div>
    </form>

  </section>
</template>
<script>
  import Autocomplete from '../../form/Autocomplete'

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
<style>
</style>
