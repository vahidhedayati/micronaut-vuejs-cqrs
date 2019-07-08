<template  scope="{ example-component }">
  <section>
    <main-heading :headingText="headingText"/>
    <section-heading :headingText="sectionHeadingText"/>
    <form :action="formAction">
      <div class="section">
        <span class="form-padding">&nbsp</span>
        <!-- form-content was usage of vue component declared on parent page - sadly this only loads in only 1 given component for any given amount of pages that creates it -->
        <!-- form-content -->

        <!-- changed to use formdata which is a dynamic declaration in each parent page to the same dataset or a specific Vue file that contains form data -->




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
import Autocomplete from '@/components/form/Autocomplete'
import MainHeading from './MainHeading'
import SectionHeading from './SectionHeading'

  export default {
   props: ['headingText', 'sectionHeadingText','formAction', 'exampleComponent'],
    components: {
      MainHeading,
      SectionHeading,
      Autocomplete,
      "example-component": this.exampleComponent

    }
  }
</script>
<style>
</style>
