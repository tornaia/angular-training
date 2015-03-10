function goToSearchResultPage() {
    $('#indexPage').hide();
    $('#searchResultPage').show();
}

$(function() {
    var featuredProductTemplate = Handlebars.compile($("#featured-product-template").html());
    var searchResultTemplateLg = Handlebars.compile($("#search-result-template-lg").html());
    var searchResultTemplateMdSm = Handlebars.compile($("#search-result-template-md-sm").html());
    var searchResultTemplateXs = Handlebars.compile($("#search-result-template-xs").html());

    function generateFeaturedProductList(data) {
        var generatedFeaturedContent = '';
        data.items.forEach(function(item) {
            generatedFeaturedContent = generatedFeaturedContent + featuredProductTemplate(item);
        });
        $("#featuredProductList").html(generatedFeaturedContent);
    }

    function generateSearchResultLists(data) {
        var generatedSearchResultListLg = '';
        var generatedSearchResultListMdSm = '';
        var generatedSearchResultListXs = '';
        data.items.forEach(function(item) {
            generatedSearchResultListLg = generatedSearchResultListLg + searchResultTemplateLg(item);
            generatedSearchResultListMdSm = generatedSearchResultListMdSm + searchResultTemplateMdSm(item);
            generatedSearchResultListXs = generatedSearchResultListXs + searchResultTemplateXs(item);
        });
        $("#searchResultListLg").html(generatedSearchResultListLg);
        $("#searchResultListMdSm").html(generatedSearchResultListMdSm);
        $("#searchResultListXs").html(generatedSearchResultListXs);
    }

    $('#searchResultPage').hide();

    $("#currentPriceSlider-lg").slider({
        range: true,
        min: 900,
        max: 1800,
        values: [ 1000, 1700 ]
    });
    $("#currentPriceSlider-md-sm").slider({
        range: true,
        min: 900,
        max: 1800,
        values: [ 1000, 1700 ]
    });
    $("#currentPriceSlider-xs").slider({
        range: true,
        min: 900,
        max: 1800,
        values: [ 1000, 1700 ]
    });

    $.ajax({
        dataType: 'json',
        url: 'assets/data/featured-products.json',
        success: generateFeaturedProductList
    });
    $.ajax({
        dataType: 'json',
        url: 'assets/data/search-results.json',
        success: generateSearchResultLists
    });
});