(function ($) {
  'use strict';

  $(function () {
    $('[data-add-cart]').on('click', function () {
      const button = $(this);
      const productId = Number(button.data('product-id'));
      const customerId = Number(button.data('customer-id') || 1);
      const status = $(`#stock-${productId}`);
      const original = button.text();

      button.prop('disabled', true).text('Adding…');
      $.ajax({
        url: `/api/cart/items?customerId=${customerId}`,
        method: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({ productId, quantity: 1 })
      }).done(payload => {
        button.text('Added');
        status.text(`${payload.itemCount} item(s) in cart · ₹${Number(payload.subtotal).toFixed(2)}`);
        $('#cart-count').text(payload.itemCount);
      }).fail(xhr => {
        const message = xhr.responseJSON?.message || 'Could not add this item.';
        status.text(message);
        button.text(original);
        button.prop('disabled', false);
      }).always(() => {
        window.setTimeout(() => {
          if (button.text() === 'Added') button.text(original);
          button.prop('disabled', false);
        }, 1000);
      });
    });

    $('[data-check-stock]').on('click', function () {
      const productId = Number($(this).data('product-id'));
      const label = $(`#stock-${productId}`);
      $.getJSON(`/api/products/${productId}/stock`)
        .done(data => label.text(data.active ? `${data.stock} unit(s) currently available` : 'Currently unavailable'))
        .fail(() => label.text('Stock lookup failed. Please retry.'));
    });

    $('form[data-validate]').on('submit', function (event) {
      if (!this.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
      }
      $(this).addClass('was-validated');
    });

    $('[data-confirm]').on('click', function (event) {
      if (!window.confirm($(this).data('confirm') || 'Continue with this action?')) event.preventDefault();
    });
  });
})(window.jQuery);
