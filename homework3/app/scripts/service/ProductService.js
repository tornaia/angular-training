(function () {
  'use strict';

  var ProductService = function ($http) {
    // Instance attributes go here:
    this.$http = $http;
  };

  /** List all dependencies required by the service. */
  ProductService.$inject = ['$http'];

  // Instance methods go here:
  ProductService.prototype = {

    /** Returns the list of all available products on the server. */
    getProducts: function () {
      return this.$http.get('data/products-featured.json')
          .then(function (resp) { return resp.data; });
    },

    /** Finds products with specified criteria.
      * NOTE: Search criteria are not implemented yet.
      */
    find: function () {
      return this.$http.get('data/products-search.json')
          .then(function (resp) { return resp.data; });
    },
    getProductById: function(productId) {
        return this.find().then(function(resp) {
            // http://stackoverflow.com/questions/13789618/differences-between-lodash-and-underscore
            for (var i=0;i<resp.length;++i) {
                var item = resp[i];
                if (item.id === productId) {
                    return item;
                }
            }
            throw 'Product with given id was not found: ' + productId;
        });
    }
  };

  // Register the service within AngularJS DI container.
  angular.module('auction').service('ProductService', ProductService);
}());
