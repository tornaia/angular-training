(function () {
  'use strict';

  angular.module('auction', ['ngRoute', 'ngAnimate', 'restangular', 'toaster'])
    .config(['$routeProvider', function ($routeProvider) {
      var title = function (page) {
        return page + ' | Auction';
      };

      $routeProvider
        .when('/', {
          templateUrl: 'views/home.html',
          controller: 'HomeController',
          controllerAs: 'ctrl',
          title: title('Home')
        })
        .when('/search', {
          templateUrl: 'views/search.html',
          controller: 'SearchController',
          controllerAs: 'ctrl',
          reloadOnSearch: false,
          title: title('Search')
        })
        .when('/product/:productId', {
          templateUrl: 'views/product.html',
          controller: 'ProductController',
          controllerAs: 'ctrl',
          title: title('Product Details'),
          resolve: {
            product: ['$route', 'ProductService', function ($route, productService) {
              var productId = parseInt($route.current.params.productId);
              return productService.getProductById(productId);
            }]
          }
        })
        .otherwise({
           redirectTo: '/'
        });
    }])
    .config(['RestangularProvider', function (RestangularProvider) {
      // we are in the app folder so ..
      RestangularProvider.setBaseUrl('../auction');
      // RestangularProvider.setRequestSuffix('.json');

      var contextRootWithoutApp = window.location.pathname.replace(new RegExp('app/$'), '');
      var bidEndpointUrl = ((window.location.protocol === "https:") ? "wss://" : "ws://") + window.location.hostname + (((window.location.port != 80) && (window.location.port != 443)) ? ":" + window.location.port : "") + contextRootWithoutApp + 'bid';
      console.log('BidEndpointUrl: ' + bidEndpointUrl);
      var ws = new WebSocket(bidEndpointUrl);
      ws.onopen = function() {
          console.log("Socket has been opened");
          ws.send("register session");
      };
      ws.onmessage = function(message) {
          var jsonData = JSON.parse(message.data);
          if (jsonData.userId != null) {
              console.log('set user http header to ' + jsonData.userId);
              RestangularProvider.setDefaultHeaders({userId: jsonData.userId});
              return;
          }
          console.log("Push notification from endpoint: " + message.data);
          window.globalBidServicePushNotificationHandler.call(this, jsonData);
      };

    }])
    .run(['$rootScope', function ($rootScope) {
      $rootScope.$on('$routeChangeSuccess', function (event, currentRoute) {
        $rootScope.pageTitle = currentRoute.title;
      });
    }]);
}());
