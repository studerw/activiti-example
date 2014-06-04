var app = app || {};
app.User = Backbone.Model.extend({
    idAttribute: "userName"
});

app.UserList = Backbone.Collection.extend({
    model: app.User,
    url: SERVLET_CONTEXT + '/users'
});

app.UserView = Backbone.View.extend({
    tagName: 'div',
    className: 'userContainer',
    template: _.template($('#userTemplate').html()),
    render: function () {
// tmpl is a function that takes a JSON object and returns html
// this.el is what we defined in tagName. use $el to get access
// to jQuery html() function
        this.$el.html(this.template(this.model.toJSON()));
        return this;
    }
});

app.UserListView = Backbone.View.extend({
    el: '#userSelForm',
    optionTpl: _.template('<option value="<@= userName @>"><@= firstName @> <@= lastName @></option>'),
    initialize: function (users) {
        console.log('UserListView:initialize()');
        this.collection = new app.UserList(users);
        this.render();
    },
    render: function () {
        console.log('UserListView:render()');

        this.collection.each(function (item) {
            console.log('appending: ' + item.toJSON());
            this.$('select').append(this.optionTpl(item.toJSON()));
        }, this);
    },

    renderUser: function (user) {
        var userView = new app.UserView({
            model: user
        });
        this.$el.append(userView.render().el);
    },

    events: {
        'change #userSel': 'userSelected'
    },

    userSelected: function(){
        alert($('#userSel').val());
    }
});

$(function () {
    console.log("starting app");

    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: SERVLET_CONTEXT + '/users',
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            console.dir(data);
            if (!data.success) {
                alert("There was an error getting the app groups");
                return;
            }
            var userList = new app.UserListView(data.data);
        },
        error: function (error) {
            alert("There was an error getting the groups");
        }
    });


});
