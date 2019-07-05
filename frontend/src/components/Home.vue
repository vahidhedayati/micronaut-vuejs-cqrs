<template>
    <div class="hello">
        <h5>{{ msg }}</h5>
        <h6>This is fab hotel using Micronaut. </h6>
      <tabs :options="{ defaultTabHash: 'search' }">
        <tab name="Tab1">
          <div class="section">
            Content for tab1
          </div>
        </tab>
        <tab name="Tab2">
          <div class="section">
          Content for tab2
          </div>
        </tab>
      </tabs>

      <table class="table">
        <thead class="thead-inverse">
        <tr>
          <th @click="sort('id')" >
            ID
          </th>
          <th @click="sort('name')" >
            Name
          </th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <td>1</td>
          <td>Some name</td>
        </tr>
        <tr>
          <td>2</td>
          <td>Some name2</td>
        </tr>
        </tbody>
      </table>
      <div class="section-heading">
        Section heading
      </div>
      <div class=" section">

          <div class="row no-padding">

            <div class="text-left form-label col-sm-2 section-field-label">
              Field  <span class="required-indicator"> * </span>
            </div>
            <div class="text-left col-sm-8">
              <field-select v-model="someObject" :field="'Something here'" :actualItem=null :values="models"></field-select>



              <autocomplete form-field="search"
                            @key-press="updateAutoCompleteItems"
                            @search-value="updateSearchValue"
                            @search-key="updateSearchKey"
                            key-field="customId" value-field="customName"
                            :items="[ {customName : 'Apple', customId:'1'} , {customName:'Banana', customId:'2'} ]" />


            </div>
          </div>
        <div class="row no-padding radio-buttons">



        <div class="text-left form-label col-sm-2 section-field-label"> Customs Radios <span class="required-indicator"> * </span></div>

        <div class="text-left col-sm-8 ">

            <span class="custom-control inline custom-radio">
              <input type="radio" class="custom-control-input" id="defaultUnchecked" name="defaultExampleRadios">
              <label class="custom-control-label" for="defaultUnchecked">Default unchecked</label>
            </span >

            <span class="custom-control inline  custom-radio">
              <input type="radio" class="custom-control-input" id="defaultChecked" name="defaultExampleRadios" checked>
              <label class="custom-control-label" for="defaultChecked">Default checked</label>
            </span>
        </div>

        </div>
        <div class="row no-padding check-boxes">
          <div class="text-left form-label col-sm-2 section-field-label"> Customs checkboxes <span class="required-indicator"> * </span></div>
          <div class="text-left col-sm-8 ">

      <span class="custom-control inline custom-checkbox ">
                   <input type="checkbox" class="custom-control-input" id="defaultUnchecked1" name="defaultExampleRadios1">
                   <label class="custom-control-label" for="defaultUnchecked1">Default unchecked</label>
                 </span >

                 <span class="custom-control inline  custom-checkbox">
                   <input type="checkbox" class="custom-control-input" id="defaultChecked1" name="defaultExampleRadios1" checked>
                   <label class="custom-control-label" for="defaultChecked1">Default checked</label>
                 </span>

        </div>
        </div>

      </div>

      <button class="btn btn-danger btn-save"@click.prevent="submitForm()">
        {{$t('save_button')}}
      </button>

      <button class="btn btn-cancel" @click="close()">
        {{$t('cancel_button')}}
      </button>

      <button class="btn btn-general"@click.prevent="submitForm()">
        {{$t('save_button')}}
      </button>


    </div>

</template>

<script>
  import {Tabs, Tab} from 'vue-tabs-component';
  import FieldSelect from './form/FieldSelect'
  import Autocomplete from './Autocomplete'
    export default {
      components: {
        Tabs,
        FieldSelect,
        Autocomplete

      },
      data: function () {
        return {
          someObject:{},
          models:[{id:'1', name:'test'}, {id:'2', name:'next item'},{id:'3', name:'Third item'},]
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
        name: 'Home',
        props: {
            msg: String
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    h3 {
        margin: 40px 0 0;
    }
    ul {
        list-style-type: none;
        padding: 0;
    }
    li {
        display: inline-block;
        margin: 0 10px;
    }
    a {
        color: #42b983;
    }
</style>
