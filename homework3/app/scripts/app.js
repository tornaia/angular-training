(function () {
  'use strict';

  angular.module('auction', ['ngRoute'])
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
          title: title('Search')
        })
        .when('/product/:productId', {
          templateUrl: 'views/product.html',
          controller: 'ProductDetailsController',
          controllerAs: 'ctrl',
          title: title('Product'),
          resolve: {
              product: ['$route', 'ProductService', function ($route, productService) {
                  // TODO put the product's Title somehow into the title (?)
                return productService.getProductById(parseInt($route.current.params.productId));
              }]
          }
        })
        .otherwise({
           redirectTo: '/'
         });
    }])
    .filter('deadline', function() {
          return function(input) {
              return moment().add(input, 'hours').calendar();
          };
      })
    .run(['$rootScope', function ($rootScope) {
      $rootScope.$on('$routeChangeSuccess', function (event, currentRoute) {
        $rootScope.pageTitle = currentRoute.title;
      });
    }]);
}());
