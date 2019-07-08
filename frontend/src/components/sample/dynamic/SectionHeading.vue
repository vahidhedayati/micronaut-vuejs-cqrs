<template>
<div class="section-heading">
    {{headingText}}
</div>
</template>
<script>
  export default {
    name: 'section-heading',
    props: {
      headingText: {
        default: 'Please set a value'
      }
    }
  }
</script>
