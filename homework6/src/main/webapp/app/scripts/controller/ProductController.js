(function () {
  'use strict';

  var ProductController = function (
      $location,
      searchFormService,
      product,
      bidService) {
    this.$location = $location;
    this.searchFormService = searchFormService;
    this.product = product;
    this.bidService = bidService;
  };

  ProductController.prototype = {
    find: function () {
      this.$location
        .path('/search')
        .search(this.searchFormService.toRequestParams());
    },
      placeBid: function() {
          this.bidService.bid({'product' : this.product, 'amount' : this.amount}, this.product);
      }
  };

  ProductController.$inject = [
    '$location',
    'SearchFormService',
    'product',
    'BidService'
  ];
  angular.module('auction').controller('ProductController', ProductController);
}());
