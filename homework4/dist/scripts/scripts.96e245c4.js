!function(){"use strict";angular.module("auction",["ngRoute","restangular"]).config(["$routeProvider",function(a){var b=function(a){return a+" | Auction"};a.when("/",{templateUrl:"views/home.html",controller:"HomeController",controllerAs:"ctrl",title:b("Home")}).when("/search",{templateUrl:"views/search.html",controller:"SearchController",controllerAs:"ctrl",title:b("Search")}).when("/product/:productId",{templateUrl:"views/product.html",controller:"ProductController",controllerAs:"ctrl",title:b("Product Details"),resolve:{product:["$route","ProductService",function(a,b){var c=parseInt(a.current.params.productId);return b.getProductById(c)}]}}).otherwise({redirectTo:"/"})}]).directive("input",function(){return{restrict:"E",require:"?ngModel",scope:{model:"=ngModel"},link:function(a,b,c,d){void 0!==d&&"number"===c.type&&(a.model&&"string"==typeof a.model&&(a.model=parseInt(a.model)),a.$watch("model",function(b){"string"==typeof b&&(a.model=parseInt(b))}))}}}).config(["RestangularProvider",function(a){a.setBaseUrl("/data"),a.setRequestSuffix(".json")}]).run(["$rootScope",function(a){a.$on("$routeChangeSuccess",function(b,c){a.pageTitle=c.title})}])}(),function(){"use strict";var a=function(a){var b=this;b.products=[],a.getProducts().then(function(a){b.products=a})};a.$inject=["ProductService"],angular.module("auction").controller("HomeController",a)}(),function(){"use strict";var a=function(a){this.product=a};a.$inject=["product"],angular.module("auction").controller("ProductController",a)}(),function(){"use strict";var a=function(a,b){var c=this;c.products=[],c.form={},c.findMore=function(){var b=$.param(c.form);a.path("/search").search(b)};var d=function(){return a.search()};b.find(d()).then(function(a){c.products=a});var e=!$.isEmptyObject(d());e&&(c.form=d()),c.form.category=void 0===c.form.category?"Category":c.form.category,c.form.lowPrice=void 0===c.form.lowPrice?0:c.form.lowPrice,c.form.highPrice=void 0===c.form.highPrice?500:c.form.highPrice,c.form.maxAuctionCloseDay=void 0===c.form.maxAuctionCloseDay?moment().format("DD/MM/YYYY"):c.form.maxAuctionCloseDay,c.form.numberOfBids=void 0===c.form.numberOfBids?1:c.form.numberOfBids};a.$inject=["$location","ProductService"],angular.module("auction").controller("SearchController",a)}(),function(){"use strict";var a=function(){return{scope:!1,restrict:"A",link:function(a,b){b.datepicker()}}};angular.module("auction").directive("auctionDatepicker",a)}(),function(){"use strict";var a=function(){return{scope:!1,restrict:"E",templateUrl:"views/partial/FooterDirective.html"}};angular.module("auction").directive("auctionFooter",a)}(),function(){"use strict";var a=function(){return{scope:!1,restrict:"E",templateUrl:"views/partial/NavbarDirective.html",controller:function(a,b){a.form={productName:""},a.search=function(){var c=$.param(a.form);b.path("/search").search(c),a.form={}}}}};angular.module("auction").directive("auctionNavbar",a)}(),function(){"use strict";var a=function(){return{scope:{minPrice:"@",maxPrice:"@",lowPrice:"=",highPrice:"="},restrict:"E",templateUrl:"views/partial/PriceRangeDirective.html",link:function(a,b){var c=angular.element(b).find("input[type=text]"),d=a.minPrice||0,e=a.maxPrice||500;a.lowPrice=a.lowPrice||d,a.highPrice=a.highPrice||e,c.slider({min:d,max:e,value:[a.lowPrice,a.highPrice]}),c.on("slideStop",function(b){a.$apply(function(){a.lowPrice!==b.value[0]&&(a.lowPrice=b.value[0]),a.highPrice!==b.value[1]&&(a.highPrice=b.value[1])})});var f=function(){return c.slider("getValue")},g=function(a,b){c.slider("setValue",[a,b])};a.$watch("lowPrice",function(a){g(a||d,f()[1])}),a.$watch("highPrice",function(a){g(f()[0],a||e)})}}};angular.module("auction").directive("auctionPriceRange",a)}(),function(){"use strict";var a=function(){return{scope:!1,restrict:"E",templateUrl:"views/partial/SearchFormDirective.html"}};angular.module("auction").directive("auctionSearchForm",a)}(),function(){"use strict";var a=function(a){this.Restangular=a};a.$inject=["Restangular"],a.prototype={getProducts:function(){return this.Restangular.all("products").getList()},find:function(a){return console.log("params: "+a),this.Restangular.all("products").getList(a)},getProductById:function(a){return this.Restangular.one("products",a).get()}},angular.module("auction").service("ProductService",a)}();