(function () {
  'use strict';

  var SearchController = function ($location, productService) {
    var _this = this;
    _this.products = [];
    _this.form = {};

    _this.findMore = function() {
        var queryString = $.param(_this.form);
        $location.path('/search').search(queryString);
    };

    var getSearchCriteriaFromUrl = function() {
        return $location.search();
    };

    productService.find(getSearchCriteriaFromUrl())
        .then(function (data) {
            _this.products = data;
    });

    // TODO sanitize or validate? what about years ago bookmarked pages?
    // its clear that is not an issue now but what about real apps?
    // Is it a good practice/common to handle them (=pay extra effort) or
    // just don't care. I guess its a business decision... :)
    var isSearchCriteriaGiven = !$.isEmptyObject(getSearchCriteriaFromUrl());
    if (isSearchCriteriaGiven) {
        _this.form = getSearchCriteriaFromUrl();
    }

    _this.form.category = _this.form.category === undefined ? 'Category' : _this.form.category;
    _this.form.lowPrice = _this.form.lowPrice === undefined ? 0 : _this.form.lowPrice;
    _this.form.highPrice = _this.form.highPrice === undefined ? 500 : _this.form.highPrice;
    _this.form.maxAuctionCloseDay = _this.form.maxAuctionCloseDay === undefined ? moment().format('DD/MM/YYYY') : _this.form.maxAuctionCloseDay;
    _this.form.numberOfBids = _this.form.numberOfBids === undefined ? 1 : _this.form.numberOfBids;
  };

  SearchController.$inject = ['$location', 'ProductService'];
  angular.module('auction').controller('SearchController', SearchController);
}());
