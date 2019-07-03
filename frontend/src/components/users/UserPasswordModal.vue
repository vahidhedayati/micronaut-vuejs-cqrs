<template>
  <modal :show="show" @close="close"  v-if="show">
    <div class="modal-header section-heading">

      {{$t('change_password')}}
    </div>
    <div class="modal-body">
      <div class="row no-padding">
      <div class="text-left form-label col-sm-4">
        {{$t('current_password')}} <span class="required-indicator"> * </span>
      </div>
      <div class="text-left form-control col-sm-8">
      <input type="password" v-model="password.current" />
      </div>

        <div class="text-left form-label col-sm-4">
          {{$t('new_password')}} <span class="required-indicator"> * </span>
        </div>
        <div class="text-left form-control col-sm-8">
          <input type="password" v-model="password.newPassword" />
        </div>
        <div class="text-left form-label col-sm-4">
          {{$t('retype_password')}} <span class="required-indicator"> * </span>
        </div>
        <div class="text-left form-control col-sm-8">
          <input type="password" v-model="password.typedPassword" />
        </div>

      </div>

      <div class="modal-footer text-left">
        <button class="btn btn-danger" @click="close()">
          {{$t('save_button')}}
        </button>

        <button class="btn" @click="close()">
          {{$t('cancel_button')}}
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
    props: ['show','password' ],

    data: function () {
      return {
        errors:[]
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
