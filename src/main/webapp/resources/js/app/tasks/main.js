(function($){
    $(document).ready(function () {
        var TaskFormModel = Backbone.Model.extend({
    // Set default values.
            defaults: {},
    // Calculate amount.
            calculateAmount: function() {
                return this.get('price') * this.get('quantity');
            }
        });

        var taskFormModel = new TaskFormModel({
            price: 2,
            quantity: 3
        });

        var TaskFormView = Backbone.View.extend({
            template: _.template('\
                Price: <%= price %>.\
                Quantity: <%= quantity %>.\
                Amount: <%= amount %>.\
            '),
            // Render view.
            render: function () {
            // Generate HTML by rendering the template.
                var html = this.template({
                    // Pass model properties to the template.
                    price: this.model.get('price'),
                    quantity: this.model.get('quantity'),
                    // Calculate amount and pass it to the template.
                    amount: this.model.calculateAmount()
                });
                // Set html for the view element using jQuery.
                $(this.el).html(html);
            }
        });

        var taskFormView = new TaskFormView({
            model: taskFormModel,
            el: '#bs-content'
        });

        taskFormView.render();
    });
})(jQuery);
