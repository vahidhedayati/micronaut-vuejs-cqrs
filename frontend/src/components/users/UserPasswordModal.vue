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
      <div class="text-left col-sm-8">
      <input type="password" v-model="password.current" class="form-control" placeholder="Current Password"  />
      </div>

        <div class="text-left form-label col-sm-4">
          {{$t('new_password')}} <span class="required-indicator"> * </span>
        </div>
        <div class="text-left  col-sm-8">
          <input type="password"  name="password" placeholder="New Password" class="form-control"  v-validate="'required|min:6|max:35'" ref="password" />
          <!-- <span v-show="errors.has('password')" class="alert alert-danger">{{ errors.first('password') }}</span> -->


        </div>
        <div class="text-left form-label col-sm-4">
          {{$t('retype_password')}} <span class="required-indicator"> * </span>
        </div>
        <div class="text-left  col-sm-8">
          <input type="password"  v-validate="'required|confirmed:password'"  placeholder="Password, Again"  class="form-control" name="password2" data-vv-as="password"/>
        <!--  <span v-show="errors.has('password_confirmation')" class="alert alert-danger">{{ errors.first('password_confirmation') }}</span> -->
        </div>


        <div class="alert alert-danger" v-show="errors.any()">
          <div v-if="errors.has('password')">
            {{ errors.first('password') }}
          </div>
          <div v-if="errors.has('password2')">
            {{ errors.first('password2') }}
          </div>
        </div>

      </div>

      <div class="modal-footer text-left">
        <button class="btn btn-danger btn-save"@click.prevent="submitForm()">
          {{$t('save_button')}}
        </button>

        <button class="btn btn-cancel" @click="close()">
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
        errors2:[]
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
      },
      submitForm (e) {

          this.submit();

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
