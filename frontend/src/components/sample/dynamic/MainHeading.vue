<template>
    <div class="main-heading">
      {{headingText}}
    </div>
</template>
<script>
  export default {
    name: 'main-heading',
    props: {
      headingText: {
        default: 'Please set a value'
      }
    }
  }
</script>
