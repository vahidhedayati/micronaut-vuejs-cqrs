<template>
  <modal :show="show" @close="close"  v-if="show">
    <div class="modal-header">
      <h3>Display user {{user.name}}</h3>
    </div>
    <div class="modal-body">
      <div class="row">
      <div class="form-label col-sm-12">
        username
        {{user.username}}
      </div>

      <div class="form-label col-sm-12">
        firstname
        {{user.firstname}}
      </div>

      <div class="form-label col-sm-12">
        lastname
        {{user.surname}}
      </div>
      <div class="form-label col-sm-12">
        last updated
        {{ user.lastUpdated | shortMoment() }}
      </div>

    </div>
      <div class="modal-footer text-right">
           <button class="modal-default-button" @click="close()">
             Close
           </button>
      </div>
    </div>
  </modal>
</template>

<script>
  import Datepicker from 'vuejs-datepicker';
  import modal from '../../Modal'
  import moment from 'moment';
  export default {
    props: ['show','user' ],

    data: function () {
      return {
        modalErrors:[],
      };
    },
    components: {
      modal,
      Datepicker,
      moment
    },
    filters: {
      moment: function (date) {
        return moment(date).format('MMMM Do YYYY, h:mm:ss a');
      },
      shortMoment: function (date) {
        return moment(date).format('DD MMM YYYY');
      }

    },
    methods: {
      moment: function () {
        return moment();
      },
      close: function () {
        this.$emit('close');
      }
    }
  }

</script>
<style>
  .dateField input {
    color: red;
    max-width:90px !important;
  }
</style>
