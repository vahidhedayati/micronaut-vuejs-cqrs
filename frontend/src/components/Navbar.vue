<template>
  <div id="menu_area" class="menu-area">
  <nav  id="nav" class="site-header navbar navbar-expand-lg navbar-static-top mainmenu" role="navigation">

    <localized-link
      tag="div"
      to="home"
      class="nav-item"
      active-class="active"
    >
    <a class="navbar-brand" href="/#" :title="$t('home.label')"><img src="../assets/github.svg" alt="Grails Logo"/></a>
    <span class="version-number">Version 0.1</span>
    </localized-link>
    <locale-switcher />
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent"
            aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">



      <ul class="nav navbar-nav ml-auto position-navigation">
  <li class="dropdown menu-options">
              <a class="btn btn-secondary dropdown-toggle"  data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="true"><font-awesome-icon icon="bars" /> Menu</a>


            <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
                 <localized-link  tag="li"  to="hotel" class="" active-class="active">
                    <a class="">{{$t('hotel_label')}}</a>
                  </localized-link>
                  <localized-link tag="li" to="users" class="" active-class="active">
                    <a class="">{{$t('users_label')}}</a>
                  </localized-link>
                  <localized-link tag="li" to="property" class="" active-class="active">
                    <a class="">{{$t('property_label')}}</a>
                  </localized-link>
                  <localized-link tag="li" to="propertysplit" class="" active-class="active">
                    <a class="">{{$t('propertysplit_label')}}</a>
                  </localized-link>

                  <localized-link tag="li" to="property2" class="" active-class="active">
                   <a class="">{{$t('propertysplit2_label')}}</a>
                  </localized-link>
                <li class="dropdown-divider"></li>
                <li class="dropdown-submenu">
                  <a  class="dropdown-item" tabindex="-1" href="#">Hover me for more options</a>
                  <ul class="dropdown-menu">
                    <li class="dropdown-item"><a tabindex="-1" href="#">Second level</a></li>
                    <li class="dropdown-submenu">
                      <a class="dropdown-item" href="#">Even More..</a>
                      <ul class="dropdown-menu">
                          <li class="dropdown-item"><a href="#">3rd level</a></li>
                            <li class="dropdown-submenu"><a class="dropdown-item" href="#">another level</a>
                            <ul class="dropdown-menu">
                                <li class="dropdown-item"><a href="#">4th level</a></li>
                                <li class="dropdown-item"><a href="#">4th level</a></li>
                                <li class="dropdown-item"><a href="#">4th level</a></li>
                            </ul>
                          </li>
                            <li class="dropdown-item"><a href="#">3rd level</a></li>
                      </ul>
                    </li>
                    <li class="dropdown-item"><a href="#">Second level</a></li>
                    <li class="dropdown-item"><a href="#">Second level</a></li>
                  </ul>
                </li>
              </ul>
       </li>
        <li class="dropdown">
          <a href="#" class="user-options dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
             aria-expanded="true"><font-awesome-icon icon="user" /> John Smith</a>
          <ul class="dropdown-menu">
            <li v-on:click="changePassword()"><a  >Change Password</a></li>
            <li v-on:click="showDetails()"><a  >My Details</a></li>

          </ul>
        </li>

        <li >
          <a class="logout-options" role="button" v-on:click="logout()"><font-awesome-icon icon="power-off" /> Log out</a>
        </li>
      </ul>
    </div>

  </nav>

    <user-password-modal
      :show="showPasswordModal"
      :password="currentPassword"
      @close="closePasswordPopup"></user-password-modal>

    <user-detail-modal
      :show="showDetailModal"
      :user="currentUser"
      @close="closeDetailPopup"></user-detail-modal>

  </div>
</template>

<script>
  import LocaleSwitcher from './LocaleSwitcher'
  import LocalizedLink from './LocalizedLink'
  import UserPasswordModal from './users/UserPasswordModal'
  import UserDetailModal from './users/UserDetailModal'
  import $ from 'jquery';

  $('.dropdown-menu a.dropdown-toggle').on('click', function(e) {
    if (!$(this).next().hasClass('show')) {
      $(this).parents('.dropdown-menu').first().find('.show').removeClass("show");
    }
    var $subMenu = $(this).next(".dropdown-menu");
    $subMenu.toggleClass('show');

    $(this).parents('li.nav-item.dropdown.show').on('hidden.bs.dropdown', function(e) {
      $('.dropdown-submenu .show').removeClass("show");
    });

    return false;
  });

  export default {
    data: function () {
      return {
        currentUser:{username:'johnsmith', firstName:'John', lastName:'Smith', password:'easypeasy', phoneNumber:'+44-123456789', messageNumber:'#johnsmith1', position:'Product Manager'},
        currentPassword:{current:'',newPassword:'',typedPassword:''},
        showPasswordModal:false,
        showDetailModal:false,
      }
    },

    methods: {
      closePasswordPopup: function () {
        this.showPasswordModal = false;
      },
      closeDetailPopup: function () {
        this.showDetailModal = false;
      },
      showDetails: function () {

        this.showDetailModal = true;
      },
      changePassword: function () {
        this.showPasswordModal = true;
      },
      logout: function () {

      }
    },
    components: {
      LocaleSwitcher,
      LocalizedLink,
      UserPasswordModal,
      UserDetailModal
    },
  }
</script>


<style scoped>

</style>
