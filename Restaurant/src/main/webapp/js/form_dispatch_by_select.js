$('.select_send_ajax').on('change', function() {
    $(this.form).submit();
});

$('form').submit(function(event) { //Отправка всех форм
    console.log(123);
    event.preventDefault();
});