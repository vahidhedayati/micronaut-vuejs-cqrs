<template>
  <modal :show="show" @close="close"  v-if="show">
    <div class="modal-header section-heading">
      {{$t('my_details')}}
    </div>
    <div class="modal-body">
      <div class="row no-padding">
        <div class="text-left form-label col-sm-4">
          {{$t('first_name')}}
        </div>
        <div class="text-left col-sm-8">
          {{user.firstName}}
        </div>

        <div class="text-left form-label col-sm-4">
          {{$t('last_name')}}
        </div>
        <div class="text-left col-sm-8">
          {{user.lastName}}
        </div>
        <div class="text-left form-label col-sm-4">
          {{$t('phone_number')}}
        </div>
        <div class="text-left col-sm-8">
          {{user.phoneNumber}}
        </div>
        <div class="text-left form-label col-sm-4">
          {{$t('message_number')}}
        </div>
        <div class="text-left col-sm-8">
          {{user.messageNumber}}
        </div>
        <div class="text-left form-label col-sm-4">
          {{$t('position_role')}}

        </div>
        <div class="text-left col-sm-8">
          {{user.position}}
        </div>
      </div>
      <div class="modal-footer text-left">
        <button class="btn btn-danger" @click="close()">
          {{$t('close_button')}}
        </button>
      </div>
    </div>
  </modal>
</template>
<script>
  import Datepicker from 'vuejs-datepicker';
  import modal from '../Modal'
  import moment from 'moment';
  export default {
    props: ['show','user' ],

    data: function () {
      return {
        errorsDetail:[]
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
