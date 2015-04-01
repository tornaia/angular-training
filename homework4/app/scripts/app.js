(function () {
  'use strict';

  angular.module('auction', ['ngRoute', 'restangular'])
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
      // TODO had some problem when I tried to bind the object (parsed from the url) to number inputs in SearchController.js/SearchFormDirective.html. Inputs were expecting numbers but they got strings
    .directive('input', function () {
        return {
            restrict: 'E',
            require: '?ngModel',
            scope: {
                model: '=ngModel'
            },
            link: function (scope, element, attrs, ngModelCtrl) {
                if (ngModelCtrl === undefined) {
                    return;
                }
                if (attrs.type !== 'number') {
                    return;
                }
                if (scope.model && typeof scope.model === 'string') {
                    scope.model = parseInt(scope.model);
                }
                scope.$watch('model', function(val) {
                    if (typeof val === 'string') {
                        scope.model = parseInt(val);
                    }
                });
            }
        };
    })
    .config(['RestangularProvider', function (RestangularProvider) {
      RestangularProvider.setBaseUrl('data');
      RestangularProvider.setRequestSuffix('.json');
    }])
    .run(['$rootScope', function ($rootScope) {
      $rootScope.$on('$routeChangeSuccess', function (event, currentRoute) {
        $rootScope.pageTitle = currentRoute.title;
      });
    }]);
}());
