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

     bid: function (params, productObjectToUpdate) {
       return this.Restangular.all('bid').post(params).then(
           function(res) {
               // console.log("success", JSON.stringify(res));
               if (res.purchased == "true") {
                   alert('Purchased! Your price was higher than the reserved price');
                   return;
               }
               productObjectToUpdate.bidStatus = res.bidStatus;
               productObjectToUpdate.topBidAmount = res.topBidAmount;
               productObjectToUpdate.topBidderName = res.topBidderName;
               productObjectToUpdate.numberOfBids = res.numberOfBids;
           },
           function (res) {
               // console.log("fail", JSON.stringify(res.data));
               alert('Bid rejected! ' + JSON.stringify(res.data));
           }
       );
     }
  };

  // Register the service within AngularJS DI container.
  angular.module('auction').service('BidService', BidService);
}());
