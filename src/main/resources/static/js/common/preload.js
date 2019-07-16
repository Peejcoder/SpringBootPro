var preload = {
    show: function() {
        if ($('#loader-wrapper').length <= 0) {
            $('body').append('<div id="loader-wrapper">' +
                    '<div class="loader-panel">' + 
                    '<div class="sk-fading-circle">' +
                    '<div class="sk-circle1 sk-circle"></div>' +
                    '<div class="sk-circle2 sk-circle"></div>' + 
                    '<div class="sk-circle3 sk-circle"></div>' +
                    '<div class="sk-circle4 sk-circle"></div>' +
                    '<div class="sk-circle5 sk-circle"></div>' +
                    '<div class="sk-circle6 sk-circle"></div>' +
                    '<div class="sk-circle7 sk-circle"></div>' +
                    '<div class="sk-circle8 sk-circle"></div>' +
                    '<div class="sk-circle9 sk-circle"></div>' +
                    '<div class="sk-circle10 sk-circle"></div>' +
                    '<div class="sk-circle11 sk-circle"></div>' +
                    '<div class="sk-circle12 sk-circle"></div>' +
                    '<div></div>' +
                    '</div>');
        }
        
        $('#loader-wrapper').show();
    },
    
    hide: function() {
        $('#loader-wrapper').hide();
    }
}