class REST {
    constructor(url) {
        this.url = window.location.protocol + "//" + window.location.host +"/canalweb";
        this.content_url = url;
    }

    post(posData, customError, callback) {
        let self = this;

        $.ajax({
            url: this.url + this.content_url,
            data: JSON.stringify(posData),
            contentType: 'application/json;charset=UTF-8',
            type: 'post',
            dataType: 'json',
            success: function (obj) {
                var message = null;
                if (obj.success != undefined) {
                    if (false == obj.success) {
                        message = obj.description;
                    }

                } else {
                    message = '无效的返回类型!';
                }
                callback(obj, message);

            },
            error: function (xhr, status) {
                callback(null, 'Call ' + self.url + self.content_url + ' failure. (' + status + ')');
            }
        });
    }

    get(customError, callback) {
        let self = this;

        $.ajax({
            url: this.url + this.content_url,
            data: null,
            contentType: 'application/json;charset=UTF-8',
            type: 'get',
            dataType: 'json',
            success: function (obj) {
                var message = null;
                if (obj.success != undefined) {
                    if (false == obj.success) {
                        message = obj.message;
                    }

                } else {
                    message = '无效的返回类型!';
                }
                callback(obj, message);

            },
            error: function (xhr, status) {
                callback(null, 'Call ' + self.url + self.content_url + ' failure. (' + status + ')');
            }
        });
    }
}