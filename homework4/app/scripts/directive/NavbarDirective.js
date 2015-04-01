(function () {
  'use strict';

  var navbarDirectiveFactory = function () {
    return {
      scope: false,
      restrict: 'E',
      templateUrl: 'views/partial/NavbarDirective.html',
        controller: ['$scope', '$location', function($scope, $location) {
            $scope.form = {productName: ''};
            $scope.search = function() {
                var queryString = $.param($scope.form);
                $location.path('/search').search(queryString);
                $scope.form = {};
            };
        }]
    };
  };

  angular.module('auction').directive('auctionNavbar', navbarDirectiveFactory);
}());
