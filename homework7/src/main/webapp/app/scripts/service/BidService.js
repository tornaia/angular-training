(function () {
  'use strict';

  var BidService = function (Restangular) {
    // Instance attributes go here:
    this.Restangular = Restangular;
  };

  /** List all dependencies required by the service. */
  BidService.$inject = ['Restangular'];

  // Instance methods go here:
  BidService.prototype = {

     bid: function (params) {
       return this.Restangular.all('bid').post(params).then(
           function(res) {
               console.log("bid request sent");
           },
           function (res) {
               console.log("bid request failed");
           }
       );
     },
    callback: function(response) {
        var scope = angular.element($("#productElement")).scope();
        scope.$apply(function() {
            if (response.purchased == "true") {
                scope.ctrl.toaster.pop({
                    type: 'success',
                    title: 'Purchased',
                    body: 'Your price was higher than the reserved price',
                    showCloseButton: true
                });
                return;
            } else if (response.bid == 'added') {
                scope.ctrl.toaster.pop({
                    type: 'info',
                    title: 'Success',
                    body: 'Bid added',
                    showCloseButton: true
                });
                return;
            } else if (response.errorMsg) {
                scope.ctrl.toaster.pop({
                    type: 'error',
                    title: 'Error',
                    body: response.errorMsg,
                    showCloseButton: true
                });
                return;
            }

            scope.ctrl.product.bidStatus = response.bidStatus;
            scope.ctrl.product.reservedPrice = response.reservedPrice;
            scope.ctrl.product.topBidAmount = response.topBidAmount;
            scope.ctrl.product.topBidderName = response.topBidderName;
            scope.ctrl.product.numberOfBids = response.numberOfBids;
        });
     }
  };

    // tornaia: ugly hack... somehow I have to wire together the angular and the websocket world
    // better solution: http://clintberry.com/2013/angular-js-websocket-service/
    window.globalBidServicePushNotificationHandler = BidService.prototype.callback;

  // Register the service within AngularJS DI container.
  angular.module('auction').service('BidService', BidService);
}());
