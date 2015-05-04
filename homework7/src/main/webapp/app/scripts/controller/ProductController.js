(function () {
  'use strict';

  var ProductController = function (
      $location,
      searchFormService,
      product,
      bidService,
      toaster) {
    this.$location = $location;
    this.searchFormService = searchFormService;
    this.product = product;
    this.bidService = bidService;
    this.toaster = toaster;
  };

  ProductController.prototype = {
    find: function () {
      this.$location
        .path('/search')
        .search(this.searchFormService.toRequestParams());
    },
      placeBid: function() {
          this.bidService.bid({'product' : this.product, 'amount' : this.amount});
      }
  };

  ProductController.$inject = [
    '$location',
    'SearchFormService',
    'product',
    'BidService',
    'toaster'
  ];
  angular.module('auction').controller('ProductController', ProductController);
}());
