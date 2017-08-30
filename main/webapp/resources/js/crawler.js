var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var $ = $;
window.onload = function () {
    new PageLoader().load();
};
var PageLoader = (function () {
    function PageLoader() {
    }
    PageLoader.prototype.load = function () {
        $("#slider").slider({
            range: "min", value: 5, min: 1, max: 360,
            slide: function (event, ui) {
                $("#time_limit").val(ui.value);
            }
        });
        var key = $("meta[name=key]").attr("content");
        if (key != undefined && key != null && key.length > 0) {
            $("#key_section").hide();
        }
        this.init = new CrawlerInitializer();
    };
    return PageLoader;
}());
var CrawlerResults = (function () {
    function CrawlerResults(resultView) {
        this.resultView = resultView;
        this.url = relHttpUrl("results");
        this.sentencesResults = new SentencesResults();
        this.imageResults = new ImagesResults();
        this.setEventHandlers();
    }
    CrawlerResults.prototype.show = function (data) {
        this.data = data;
        this.downloadView(data);
    };
    CrawlerResults.prototype.downloadView = function (data) {
        var _this = this;
        $(this.resultView).load(this.url, function () {
            var self = _this;
            $(document).ready(function () { return self.fillView(data); });
        });
    };
    CrawlerResults.prototype.fillView = function (data) {
        this.prepareCanvas();
        var sentences = data.sentences;
        var images = data.images;
        if (images != null && images != undefined) {
            this.imageResults.generateResults(images);
        }
        if (sentences != null && sentences != undefined) {
            this.sentencesResults.generateResults(sentences);
        }
        $("#target").html(data.target);
        $("#time").html(data.time);
        $("#sentencesInit").html(data.sentencesInit.join(", "));
        this.setEventHandlers();
    };
    CrawlerResults.prototype.setEventHandlers = function () {
        $("#return").off().on("click", function () { return window.location.reload(); });
    };
    CrawlerResults.prototype.prepareCanvas = function () {
        $("#tabs").tabs();
        // $("#found_sentences").accordion({collapsible: true, heightStyle: "content"});
        $("#found_sentences").empty();
        $("#found_images").empty();
    };
    return CrawlerResults;
}());
function relHttpUrl(relative) {
    var url = httpUrl();
    if (relative.charAt(relative.length) === "/")
        relative = relative.substr(0, relative.length - 2);
    url += relative;
    return url;
}
function httpUrl() {
    var url = window.location.href.toString().replace(window.location.hash, "").replace("#", "");
    if (url.charAt(url.length - 1) !== "/")
        url += "/";
    return url;
}
var ResultsGenerator = (function () {
    function ResultsGenerator() {
    }
    return ResultsGenerator;
}());
var SourcesGenerator = (function () {
    function SourcesGenerator() {
    }
    SourcesGenerator.prototype.createSourcesList = function (el) {
        var details = document.createElement("div");
        var sources = el.sources;
        sources.forEach(function (el) {
            var span = document.createElement("span");
            span.className = "result-list-element";
            var spanTimes = document.createElement("span");
            spanTimes.innerHTML = el.times + " times: ";
            spanTimes.className = "result-list-element-times";
            span.appendChild(spanTimes);
            span.innerHTML += el.content;
            details.appendChild(span);
        });
        return details;
    };
    return SourcesGenerator;
}());
var SentencesResults = (function (_super) {
    __extends(SentencesResults, _super);
    function SentencesResults() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.sourcesGenerator = new SourcesGenerator();
        return _this;
    }
    SentencesResults.prototype.generateResults = function (sentences) {
        var self = this;
        sentences.forEach(function (el) {
            $("#found_sentences").append(self.createSingleSentenceResult(el));
        });
    };
    SentencesResults.prototype.createSingleSentenceResult = function (el) {
        var container = document.createElement("div");
        container.className = "search-result-container";
        container.appendChild(this.createSection(el));
        container.appendChild(this.sourcesGenerator.createSourcesList(el));
        return container;
    };
    SentencesResults.prototype.createSection = function (el) {
        var h3 = document.createElement("h3");
        h3.className = "search-result-header";
        h3.innerHTML = el.content;
        return h3;
    };
    return SentencesResults;
}(ResultsGenerator));
var ImagesResults = (function (_super) {
    __extends(ImagesResults, _super);
    function ImagesResults() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    ImagesResults.prototype.generateResults = function (images) {
        var self = this;
        images.forEach(function (el) {
            $("#found_images").append(self.createTile(el));
        });
    };
    ImagesResults.prototype.createTile = function (el) {
        var a = document.createElement("a");
        a.href = el.content;
        a.target = "_blank";
        var container = document.createElement("div");
        container.className = "image-container";
        var image = document.createElement("img");
        image.className = "image-miniature";
        image.src = el.content;
        container.appendChild(image);
        a.appendChild(container);
        return a;
    };
    ImagesResults.prototype.createSources = function () {
    };
    return ImagesResults;
}(ResultsGenerator));
var CrawlerInitializer = (function () {
    function CrawlerInitializer() {
        this.progress = new ProgressBar(document.getElementById("progress"));
        this.crawlerClient = new CrawlerClient();
        this.container = document.getElementById("crawler");
        this.setEventHandlers();
    }
    CrawlerInitializer.prototype.setEventHandlers = function () {
        var self = this;
        $("#confirm_button").off().on("click", function () { return self.init(); });
    };
    CrawlerInitializer.prototype.init = function () {
        var _this = this;
        var self = this;
        var options = this.getOptions();
        if (!this.crawlerClient.isWaiting) {
            this.crawlerClient.requestSearch(options, function (futureData) {
                $(_this.container).load(relHttpUrl("results"), function () {
                    new CrawlerResults(document.getElementById("resultBox")).show(futureData);
                });
            }, function (data) {
                _this.progress.hide();
                _this.showErrors(data);
            });
            this.clearErrors();
            this.progress.show(options.timeLimit);
        }
        else {
            $.notify("Last search not finished yet...", "error");
        }
    };
    CrawlerInitializer.prototype.clearErrors = function () {
        $("#errors_holder").empty();
    };
    CrawlerInitializer.prototype.showErrors = function (data) {
        this.clearErrors();
        var errors = JSON.parse(data.responseText);
        errors.forEach(function (el) {
            var span = document.createElement("span");
            span.classList.add("error");
            span.innerHTML = el;
            $("#errors_holder").append(span);
        });
    };
    CrawlerInitializer.prototype.getOptions = function () {
        var self = this;
        return {
            url: $("#adress").val(),
            sentences: $("#sentences").val(),
            timeLimit: $("#time_limit").val(),
            type: $("input[name='type']:checked").val(),
            key: self.getKey(),
            ignoreCase: $("input[name='ignoreCase']").is(":checked"),
            lookForImages: $("input[name='lookForImages']").is(":checked")
        };
    };
    CrawlerInitializer.prototype.getKey = function () {
        var metaKey = $("meta[name=key]").attr("content");
        if (metaKey != undefined && metaKey != null && metaKey.length > 0) {
            return metaKey;
        }
        else {
            var inputKey = $("input[name=key]").val();
            return inputKey;
        }
    };
    return CrawlerInitializer;
}());
var CrawlerClient = (function () {
    function CrawlerClient() {
        this.crawlerAPIURL_HTTP = relHttpUrl("crawl");
    }
    CrawlerClient.prototype.requestSearch = function (options, callbackSuccess, callbackFail) {
        var _this = this;
        $.ajax(this.crawlerAPIURL_HTTP, {
            data: JSON.stringify(options),
            dataType: "json",
            contentType: "application/json",
            method: "POST",
            statusCode: { 400: function () { $.notify("Bad request.", "error"); } },
            timeout: options.timeout
        }).done(function (data) {
            _this.isWaiting = false;
            callbackSuccess(data);
        }).fail(function (data) {
            _this.isWaiting = false;
            callbackFail(data);
        });
        this.isWaiting = true;
    };
    return CrawlerClient;
}());
var ProgressBar = (function () {
    function ProgressBar(view) {
        this.view = view;
        this.time = 0;
        this.speedInMilis = 10;
        this.progressLevel = 0;
        $(view).progressbar();
    }
    ProgressBar.prototype.show = function (time) {
        this.reset();
        this.time = time;
        this.initTime = this.time;
        var change = (100 / time) * (this.speedInMilis / 1000);
        var self = this;
        this.counting = setInterval((function () {
            if (self.progressLevel < 100) {
                self.count(change);
            }
            else {
                self.hide();
            }
        }), this.speedInMilis);
    };
    ProgressBar.prototype.count = function (change) {
        var self = this;
        this.progressLevel += change;
        $(self.view).progressbar("option", "value", this.progressLevel);
    };
    ProgressBar.prototype.hide = function () {
        this.reset();
        $(this.view).progressbar("option", "value", 0);
    };
    ProgressBar.prototype.reset = function () {
        if (this.counting != null) {
            clearInterval(this.counting);
        }
        this.progressLevel = 0;
        this.time = 0;
        this.initTime = 0;
    };
    return ProgressBar;
}());
//# sourceMappingURL=crawler.js.map