<template>

  <nav  id="nav" class="site-header navbar navbar-expand-lg navbar-static-top" role="navigation">

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


      <ul class="nav navbar-nav ml-auto">
        <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
           aria-expanded="true"><font-awesome-icon icon="bars" />Menu</a>
        <ul class="dropdown-menu">
          <li >
              <localized-link
                tag="li"
                to="hotel"
                class=""
                active-class="active"
              >

                <a class="nav-link">
                  {{$t('hotel_label')}}
                </a>
              </localized-link>
          </li>
          <li>
              <localized-link
                tag="li"
                to="users"
                class=""
                active-class="active"
              >
                <a class="nav-link">
                  {{$t('users_label')}}
                </a>
              </localized-link>
          </li>
        </ul>
        </li>




        <li class="dropdown">

          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
             aria-expanded="true"><font-awesome-icon icon="user" /> John Smith</a>

          <ul class="dropdown-menu">

            <li >
              <a href="" >
                <i class="fa fa-user-secret">ss</i>
              </a>
            </li>

            <li><a href="" >
              <i class="fa fa-globe"> ss</i>
            </a>


            </li>
          </ul>
        </li>

        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
             aria-expanded="true">Status <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li>Option 1</li>
            <li>Option 1</li>
          </ul>
        </li>





      </ul>
    </div>

  </nav>
</template>

<script>
  import LocaleSwitcher from './LocaleSwitcher'
  import LocalizedLink from './LocalizedLink'

  export default {

    components: {
      LocaleSwitcher,
      LocalizedLink
    },
  }
</script>


<style scoped>
  /* ... */
</style>
