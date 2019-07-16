Date.prototype.Format = function (fmt) {
    let o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (let k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
let userLogin;

let urlPre = "/payroll";
const CommUtil = {
    showLayer: function (str) {
        layer.msg((str == undefined || str == null) ? '加载中' : str, {icon: 4});
    },

    showAlertLayer: function (str) {
        layer.alert((str == undefined || str == null) ? '加载中' : str, {icon: 5});
    },

    closeLayer: function () {
        layer.closeAll()
    },

    getNowDay: function () {
        let nowDay = new Date(); //上月日期
        let toDay = nowDay.Format("yyyy-MM-dd");
        let day26 = nowDay.Format("yyyy-MM") + "-26";
        if (CommUtil.tab(toDay, day26)) {
            return new Date().Format("yyyy-MM");
        } else {
            let lastMonthDate = new Date(); //上月日期
            lastMonthDate.setDate(1);
            lastMonthDate.setMonth(lastMonthDate.getMonth() - 1);
            return lastMonthDate.Format("yyyy-MM");
        }
    },

    toFiexdNum: function (num, s) {
        let times = Math.pow(10, s);
        let des = num * times + 0.5;
        des = parseInt(des, 10) / times;
        return des
    },


    tab: function (date1, date2) {
        let oDate1 = new Date(date1);
        let oDate2 = new Date(date2);
        if (oDate1.getTime() > oDate2.getTime()) {
            return true;
        } else {
            return false;
        }
    },
};