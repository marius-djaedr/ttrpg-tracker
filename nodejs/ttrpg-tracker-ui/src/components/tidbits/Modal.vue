<script setup>
    const emit = defineEmits(['cancel','submit'])
    
    function cancel(){
        emit('cancel');
    }
    function submit(){
        emit('submit');
    }
</script>

<template>
    <div class="modal-backdrop">
        <div class="modal">
            <header class="modal-header">
                <slot name="header">
                    This is the default title!
                </slot>
                <button type="button" class="btn btn-default btn-sm btn-close" @click="cancel">x</button>
            </header>

            <section class="modal-body">
                <slot name="body">
                    This is the default body!
                </slot>
            </section>

            <footer class="modal-footer">
                <slot name="footer">
                </slot>
                <button type="button" class="btn btn-danger" @click="cancel">Cancel</button>
                <button type="button" class="btn btn-success" @click="submit">Submit</button>
            </footer>

        </div>
    </div>
</template>

<style>
  .modal-backdrop {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.3);
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .modal {
    background: var(--color-background);
    box-shadow: 2px 2px 20px 1px;
    overflow-x: auto;
    display: flex;
    flex-direction: column;
  }

  .modal-header,
  .modal-footer {
    padding: 15px;
    display: flex;
  }

  .modal-header {
    position: relative;
    border-bottom: 1px solid #eeeeee;
    color: #4AAE9B;
    justify-content: space-between;
  }

  .modal-footer {
    border-top: 1px solid #eeeeee;
    flex-direction: row;
    justify-content: flex-end;
  }

  .modal-body {
    position: relative;
    padding: 20px 10px;
  }

  .btn-close {
    position: absolute;
    top: 0;
    right: 0;
    cursor: pointer;
  }
</style>