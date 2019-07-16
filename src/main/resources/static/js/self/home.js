/**
 *
 */
let alertTimeDiff = {
    searchText: "",
    searchKey: "",
    dateKey: "",
    fromDate: "",
    toDate: "",
    orderBy: "dparkTime",
    sort: "desc", //desc 降序 asc 升序
    pageNum: 0,//第几页
    pageSize: 10//一页几条数据
};
const IndexUtls = {
    getAlertTimeDiff: function () {
        preload.show();
        let request = alertTimeDiff;
        let rest = new REST("/api/home/getAlertTimeDiff");
        rest.post(request, '获取时间差报警失败', function (obj, err) {
            preload.hide();
            if (null != err) {

            } else {
                if (true == obj.success) {
                    console.log(obj.records);
                    if (null != obj.records && obj.records.length > 0) {
                        $("#tbMain").empty();
                        obj.records.forEach(v => {
                            let row = IndexUtls.getDataRow(v);
                            $("#tbMain").append(row);
                            Pagination.Page($(".ht-page"), alertTimeDiff.pageNum, obj.total, alertTimeDiff.pageSize);
                        });
                    } else {
                        alert("无数据");
                    }
                }
            }
        });
        return IndexUtls.getAlertTimeDiff;
    },
    getDataRow: function (h) {
        let row = document.createElement('tr') //创建行

        let ZeroCell = document.createElement('td') //创建第零列
        ZeroCell.innerHTML = h.detector_no; //填充数据
        row.appendChild(ZeroCell)

        let FirCell = document.createElement('td') //创建第一列
        FirCell.innerHTML = h.parklot_name; //填充数据
        row.appendChild(FirCell)

        let SecCell = document.createElement('td') //创建第二列
        SecCell.innerHTML = h.carNumber; //填充数据
        row.appendChild(SecCell)

        let ThirdCell = document.createElement('td')//创建第三列
        ThirdCell.innerHTML = h.dparkTime;
        row.appendChild(ThirdCell)

        let FourCell = document.createElement('td') //创建第四列
        FourCell.innerHTML = h.parkTime;
        row.appendChild(FourCell)

        let FifCell = document.createElement('td') //创建第五列
        FifCell.innerHTML = IndexUtls.transTime(h.parkTimeDeff);
        row.appendChild(FifCell)

        let SixCell = document.createElement('td')//创建第六列
        SixCell.innerHTML = h.dleaveTime;
        row.appendChild(SixCell)

        let SevCell = document.createElement('td') //创建第七列
        SevCell.innerHTML = h.leaveTime;
        row.appendChild(SevCell)

        let EightCell = document.createElement('td') //创建第八列
        EightCell.innerHTML = IndexUtls.transTime(h.leaveTimeDeff);
        row.appendChild(EightCell)

        let NineCell = document.createElement('td') //创建第八列
        NineCell.innerHTML = h.desc;
        row.appendChild(NineCell)

        return row;
    },
    transTime: function (deff) {
        if (deff == 0)
            return "";
        let h = parseInt(deff / 60);
        let m = deff % 60;
        return h + "小时" + m + "分钟"
    }
}

function search() { //点击搜索按钮进行查询
    let key = $("#searchkey").val();
    let text = $("#searchtext").val();
    if (key == 'alertType') {
        text = $("#type_sel").val();
    }
    else if (key == 'org_id') {
        text = $("#org_sel").val();
    }

    let dateKey = $("#searchtime").val();
    let datetext = $("#timequantum").val();

    alertTimeDiff.searchKey = key;
    alertTimeDiff.searchText = text;
    alertTimeDiff.dateKey = dateKey;
    alertTimeDiff.fromDate = datetext.split(' ')[0];
    alertTimeDiff.toDate = datetext.split(' ')[2];

    IndexUtls.getAlertTimeDiff()
}

function exportXls() { //点击导出按钮导出为excel
    let key = $("#searchkey").val();
    let text = $("#searchtext").val();

    if (key == 'alertType') {
        text = $("#type_sel").val();
    }
    else if (key == 'org_id') {
        text = $("#org_sel").val();
    }

    let dateKey = $("#searchtime").val();
    let datetext = $("#timequantum").val();

    alertTimeDiff.searchKey = key;
    alertTimeDiff.searchText = text;
    alertTimeDiff.dateKey = dateKey;
    if ('' != datetext) {
        alertTimeDiff.fromDate = datetext.split(' ')[0];
        alertTimeDiff.toDate = datetext.split(' ')[2];
    }

    var oA = document.createElement('a');
    let url = window.location.protocol + "//" + window.location.host + "/canalweb";
    url += "/api/home/export"
        + "?"
        + "searchKey=" + alertTimeDiff.searchKey + "&"
        + "searchText=" + alertTimeDiff.searchText + "&"
        + "dateKey=" + alertTimeDiff.dateKey + "&"
        + "fromDate=" + alertTimeDiff.fromDate + "&"
        + "toDate=" + alertTimeDiff.toDate + "&"
        + "orderBy=" + alertTimeDiff.orderBy + "&"
        + "sort=" + alertTimeDiff.sort;
    oA.href = url;
    oA.click();
}

$(document).ready(function () {
    setInterval(IndexUtls.getAlertTimeDiff(), 1 * 60 * 1000)
    $(".trangle span").click(function () {
        let par = $(this).parent();
        let pid = par.attr('id');
        let sort = $(this).attr('class');
        if (pid != '')
            alertTimeDiff.orderBy = pid;
        if (sort == 'asc' || sort == 'desc')
            alertTimeDiff.sort = sort;
        IndexUtls.getAlertTimeDiff();
    });
    $('.btn').click(function () {
        search();
    })
    $('.inp').bind('keyup', function (event) {  //使用回车按钮进行查询
        if (event.keyCode == "13") {
            search();
        }
    });
    $('.out').click(function () {
        exportXls();
    })
    $('#searchkey').change(function () {
        let p1 = $(this).children('option:selected').val();//这就是selected的值
        if (p1 == 'org_id') {
            $('#org_sel').show();
            $('#searchtext').hide();
            $('#type_sel').hide();
        }
        else if (p1 == 'alertType') {
            $('#type_sel').show();
            $('#org_sel').hide();
            $('#searchtext').hide();
        }
        else {
            $('#type_sel').hide();
            $('#org_sel').hide();
            $('#searchtext').show();
        }
    })

})

