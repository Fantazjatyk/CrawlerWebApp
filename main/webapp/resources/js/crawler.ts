var $: any = $;
window.onload = () => {
    new PageLoader().load();
};

class PageLoader {
    init;
    load() {
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
    }
}


class CrawlerResults {

    url: string = relHttpUrl("results");
    data;
    sentencesResults: SentencesResults = new SentencesResults();
    imageResults: ImagesResults = new ImagesResults();

    constructor(public resultView) {
        this.setEventHandlers();
    }

    show(data) {
        this.data = data;
        this.downloadView(data);
    }

    downloadView(data) {
        $(this.resultView).load(this.url, () => {
            var self = this;
            $(document).ready(() => self.fillView(data))
        });
    }

    fillView(data) {
        this.prepareCanvas();
        var sentences: Array<any> = data.sentences;
        var images: Array<any> = data.images;
        this.sentencesResults.generateResults(sentences);
        this.imageResults.generateResults(images);

        $("#target").html(data.target);
        $("#time").html(data.time);
        $("#sentencesInit").html(data.sentencesInit);
        this.setEventHandlers();
    }

    setEventHandlers() {
        $("#return").off().on("click", () => window.location.reload());
    }
    prepareCanvas() {
        $("#tabs").tabs();
        // $("#found_sentences").accordion({collapsible: true, heightStyle: "content"});
        $("#found_sentences").empty();
        $("#found_images").empty();
    }

}

function relHttpUrl(relative: string) {
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
abstract class ResultsGenerator {
    abstract generateResults(sentences);
}
class SourcesGenerator {

    createSourcesList(el) {
        var details = document.createElement("div");
        var sources: Array<any> = el.sources;

        sources.forEach((el) => {
            var span = document.createElement("span");
            span.className = "result-list-element";
            var spanTimes = document.createElement("span");
            spanTimes.innerHTML = el.times + " times: ";
            spanTimes.className = "result-list-element-times";
            span.appendChild(spanTimes);
            span.innerHTML += el.content;
            details.appendChild(span);
        })

        return details;
    }

}

class SentencesResults extends ResultsGenerator {

    sourcesGenerator: SourcesGenerator = new SourcesGenerator();
    generateResults(sentences) {
        var self = this;
        sentences.forEach((el) => {
            $("#found_sentences").append(self.createSingleSentenceResult(el));
        })
    }
    createSingleSentenceResult(el) {
        var container = document.createElement("div");
        container.className = "search-result-container";
        container.appendChild(this.createSection(el));
        container.appendChild(this.sourcesGenerator.createSourcesList(el));
        return container;
    }

    createSection(el) {
        var h3 = document.createElement("h3");
        h3.className = "search-result-header";
        h3.innerHTML = el.content;
        return h3;
    }

}

class ImagesResults extends ResultsGenerator {

    generateResults(images: Array<any>) {
        var self = this;
        images.forEach((el) => {
            $("#found_images").append(self.createTile(el));
        });
    }

    createTile(el) {
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
    }

    createSources() {

    }
}

class CrawlerInitializer {
    progress: ProgressBar;
    isWaiting: boolean;
    crawlerClient: CrawlerClient;
    container;
    crawlerResults: CrawlerResults;

    constructor() {
        this.progress = new ProgressBar(document.getElementById("progress"));
        this.crawlerClient = new CrawlerClient();
        this.container = document.getElementById("crawler");
        this.setEventHandlers();
    }

    setEventHandlers() {
        var self = this;
        $("#confirm_button").off().on("click", () => self.init());
    }

    init() {
        var self = this;
        var options = this.getOptions();
        if (!this.crawlerClient.isWaiting) {
            this.crawlerClient.requestSearch(options, (futureData) => {
                $(this.container).load(relHttpUrl("results"), () => {
                    new CrawlerResults(document.getElementById("resultBox")).show(futureData);
                });

            }, (data) => {
                this.progress.hide();
                this.showErrors(data);
            });
            this.clearErrors();
            this.progress.show(options.timeLimit);
        }
        else {
            $.notify("Last search not finished yet...", "error");
        }
    }

    clearErrors() {
        $("#errors_holder").empty();
    }
    showErrors(data) {
        this.clearErrors();
        var errors = JSON.parse(data.responseText);
        errors.forEach(el => {
            var span = document.createElement("span");
            span.classList.add("error");
            span.innerHTML = el;
            $("#errors_holder").append(span);
        });

    }

    getOptions() {
        var self = this;
        return {
            url: $("#adress").val(),
            sentences: $("#sentences").val(),
            timeLimit: $("#time_limit").val(),
            type: $("input[name='type']:checked").val(),
            key: self.getKey(),
            ignoreCase: $("input[name='ignoreCase']").is(":checked")

        }
    }

    private getKey() {
        var metaKey = $("meta[name=key]").attr("content");
        if (metaKey != undefined && metaKey != null && metaKey.length > 0) {
            return metaKey;
        }
        else {
            var inputKey = $("input[name=key]").val();
            return inputKey;
        }
    }
}

class CrawlerClient {

    crawlerAPIURL_HTTP: string = relHttpUrl("crawl");
    isWaiting: boolean;

    requestSearch(options, callbackSuccess, callbackFail) {

        $.ajax(this.crawlerAPIURL_HTTP,
            {
                data: JSON.stringify(options),
                dataType: "json",
                contentType: "application/json",
                method: "POST",
                statusCode: { 400: () => { $.notify("Bad request.", "error") } },
                timeout: options.timeout
            }).done((data) => {
                this.isWaiting = false;
                callbackSuccess(data);
            }).fail((data) => {
                this.isWaiting = false;
                callbackFail(data);
            });
        this.isWaiting = true;
    }
}

class ProgressBar {

    time: number = 0;
    initTime: number;
    speedInMilis = 10;
    progressLevel = 0;
    counting;

    constructor(public view) {
        $(view).progressbar();
    }

    show(time: number) {
        this.reset();
        this.time = time;
        this.initTime = this.time;
        var change = (100 / time) * (this.speedInMilis / 1000);
        var self = this;
        this.counting = setInterval((
            () => {
                if (self.progressLevel < 100) {
                    self.count(change)
                } else {
                    self.hide();
                }
            }
        ), this.speedInMilis);
    }

    count(change) {
        var self = this;
        this.progressLevel += change;
        $(self.view).progressbar("option", "value", this.progressLevel);
    }

    hide() {
        this.reset();
        $(this.view).progressbar("option", "value", 0);
    }


    reset() {
        if (this.counting != null) {
            clearInterval(this.counting);
        }
        this.progressLevel = 0;
        this.time = 0;
        this.initTime = 0;
    }
}
