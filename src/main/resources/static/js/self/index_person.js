let reworkEumn = {
    '1': '是',
    '0': '否'
};
let reworkRevEumn = {
    '是': '1',
    '否': '0'
};
let levelEumn = {
    '1': '一星级',
    '2': '二星级',
    '3': '三星级',
    '4': '四星级',
    '5': '五星级',
};
let levelRevEumn = {
    '一星级': '1',
    '二星级': '2',
    '三星级': '3',
    '四星级': '4',
    '五星级': '5',
};
let workTypeRevEumn = {
    '白三休一以上': '0',
    '白三休一以下': '1',
    '中三休一以上': '2',
    '中白三休一以下': '3',
    '夜班': '4',
};
let workTypeEumn = {
    '0': '白三休一以上',
    '1': '白三休一以下',
    '2': '中三休一以上',
    '3': '中白三休一以下',
    '4': '夜班',
};
let data = [
    [
        '', '', '', '', '', '', '', '', '', '', '', ''
    ]
];
let titles = ['工作时间', 'Pos登录名', '姓名', '管理站编号', '当月任务',
    '当月应上班天数', '当月上班类型', '星级', '司龄', '入职时间', '是否为返聘'];

let tableData = [];

let selectedIndex = -1;

let pageIndex = 0;

let pageSize = 100;

let pageStart = 0;

let searchParam;

let pageSum = 0;

let table;

let isMoreData = false;


const Index_Person = {
    jugeIsEmpty: function (str) {
        if (str == '' || str == null || str == undefined) {
            return true
        }
        return false
    },
    createPerson: function (workMonth, userName, realName, orgId, task, workDays, workType, level, workAge, enterDate, rework) {
        let o = new Object();
        o.workMonth = workMonth;
        o.userName = userName;
        o.realName = realName;
        o.orgId = orgId;
        o.task = task;
        o.workDays = workDays;
        o.workType = workType;
        o.level = level;
        o.workAge = workAge;
        o.enterDate = enterDate;
        o.rework = rework;
        return o;
    },
    createSearchParam: function (userName, realName, orgId, workMonth, pageNum) {
        let o = new Object();
        o.userName = userName;
        o.realName = realName;
        o.orgId = orgId;
        o.workMonth = workMonth;
        if (pageNum == undefined || pageNum == null) {
            o.pageNum = 0;
        } else {
            o.pageNum = pageNum;
        }
        o.pageSize = pageSize;
        return o;
    },
    showEditModal: function () {
        if (userLogin.userType != '1') {
            CommUtil.showLayer("无权限操作");
            return
        }
        if (table.rows('.selected').data().length) {
            $("#editPersonInfo").modal()
            let rowData = table.rows('.selected').data()[0];
            let inputs = $("#editBookModal").find('input')
            for (let i = 0; i < inputs.length; i++) {
                $(inputs[i]).val(rowData[i + 1])
            }
            $("#orgId").val(rowData[4])
            $("#workType").val(workTypeRevEumn[rowData[7]])
            $("#level").val(levelRevEumn[rowData[8]])
            $("#rework").val(reworkRevEumn[rowData[11]])
        } else {
            alert('请选择修改人员');
        }
    },
    addMoreData: function () {
        if (tableData.length <= 0) {
            return
        }
        if (tableData.length >= pageSize) {
            isMoreData = true;
            table.rows.add(tableData.splice(0, pageSize)).draw();
        } else {
            isMoreData = false;
            table.rows.add(tableData.splice(0, tableData.length)).draw();
        }
        pageSum = Index_Person.getPageSum();
        Index_Person.jumpToPage(pageIndex)
    },

    jumpToPage: function (page) {
        $("#person").dataTable().fnPageChange(pageIndex);
    },

    getPageSum: function () {
        if (table == null) {
            return 0;
        }
        return table.page.info();
    },

    initPageSum: function () {
        pageSum = Index_Person.getPageSum();
    },

    searchUserInfo: function (param) {
        Index_Person.showLayer();
        $.ajax({
            url: urlPre + "/api/home/getUserInfo",
            type: "POST",
            timeout: 10000,
            data: JSON.stringify(param),
            contentType: 'application/json;charset=UTF-8',
            success: function (res) {

                if (param.pageNum == pageStart) {
                    Index_Person.jumpToPage(pageStart)
                }
                //console.log(new Date().Format("yyyy-MM-dd HH:mm:ss"));
                $.each(res, function (i, field) {
                    tableData.push(['', field.workMonth, field.userName, field.realName, field.orgId, field.task, field.workDays, workTypeEumn[field.workType], levelEumn[field.level], field.workAge, field.enterDate, reworkEumn[field.rework]])
                });
                Index_Person.addMoreData();
                Index_Person.closeLayer();
            },
            error: function (errs) {
                console.log(errs);
                Index_Person.closeLayer();
                CommUtil.showAlertLayer("查询失败");
            }
        })
    },
    updateUserInfo: function (workMonth, userName, realName, orgId, task, workDays, workType, level, workAge, enterDate, rework, oTable) {
        Index_Person.showLayer();
        let pesonInfo1 = Index_Person.createPerson(workMonth, userName, realName, orgId, task, workDays, workType, level, workAge, enterDate, rework);
        let personList = [];
        personList.push(pesonInfo1);
        $.ajax({
            url: urlPre + "/api/home/updateUserInfo",
            type: "POST",
            timeout: 10000,
            data: JSON.stringify(personList),
            contentType: 'application/json;charset=UTF-8',
            success: function (res) {
                if (res) {
                    oTable.fnUpdate(['', workMonth, userName, realName, orgId, task, workDays, workTypeEumn[workType], levelEumn[level], workAge, enterDate, reworkEumn[rework]], selectedIndex);
                    Index_Person.closeLayer();
                } else {
                    Index_Person.closeLayer();
                    CommUtil.showAlertLayer("更新失败")
                }
            },
            error: function (errs) {
                console.log(errs);
                Index_Person.closeLayer();
                CommUtil.showAlertLayer("更新失败")
            }
        })
    },
    showLayer: function () {
        layer.load(2, {
            time: 20000
        });
    },
    closeLayer: function () {
        layer.closeAll('loading');
    },

    encodeDate: function (date) {
        if (typeof String.prototype.startsWith != 'function')
            String.prototype.startsWith = function (str) {
                return this.slice(0, str.length) == str;
            };
        if (typeof String.prototype.endsWith != 'function') {
            String.prototype.endsWith = function (str) {
                return this.slice(-str.length) == str;
            };
        }
        let sCompareStr = date;//比较字符串
        let sBeCompareStr = " 00:00:00";//被比较字符串
        if (sCompareStr.endsWith(sBeCompareStr)) {//这里可以替换为startsWith
            return sCompareStr
        }
        return sCompareStr + sBeCompareStr
    },
    exportPersonXls: function (param) { //点击导出按钮导出为excel
        let oA = document.createElement('a');
        let url = window.location.protocol + "//" + window.location.host + "";
        url += urlPre + "/api/home/exportPersonXls"
            + "?"
            + "userName=" + param.userName + "&"
            + "realName=" + param.realName + "&"
            + "orgId=" + param.orgId + "&"
            + "workMonth=" + param.workMonth + "&"
            + "pageNum=" + param.pageNum + "&"
            + "pageSize=" + param.pageSize;
        oA.href = url;
        oA.target = "_blank";
        oA.click();
    },
};


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

$(document).ready(function () {
    data = [];
    table = $('#person').DataTable({
        data: data,
        "pagingType": "full_numbers",
        "bSort": true,
        "language": {
            "sProcessing": "处理中...",
            "sLengthMenu": "显示 _MENU_ 项结果",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
            "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "sUrl": "",
            "sEmptyTable": "暂无数据",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页"
            },
            "oAria": {
                "sSortAscending": ": 以升序排列此列",
                "sSortDescending": ": 以降序排列此列"
            }
        },
        "columnDefs": [{
            "searchable": false,
            "orderable": true,
            "targets": 0
        }],
        "order": [[1, 'asc']]
    });
    $('#editPersonInfo').modal({
        backdrop: false,
        keyboard: false,
        show: false
    });
    table.on('order.dt search.dt', function () {
        table.column(0, {
            search: 'applied',
            order: 'applied'
        }).nodes().each(function (cell, i) {
            cell.innerHTML = i + 1;
        });
    }).draw();
    $('#person tbody').on('click', 'tr', function () {
        let index = $(this).context._DT_RowIndex; //行号
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedIndex = -1;
        } else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
            selectedIndex = index;
            Index_Person.showEditModal();
        }
    });

    $('#btn_edit').click(function () {
        Index_Person.showEditModal();
    });
    $("#saveEdit").click(function () {
        let workMonth = $("#workMonth").val();
        let userName = $("#userName").val();
        let realName = $("#realName").val();
        let orgId = $("#orgId").val();
        let task = $("#task").val();
        let workDays = $("#workDays").val();
        let workType = $("#workType").val();
        let level = $("#level").val();
        let workAge = $("#workAge").val();
        let enterDate = Index_Person.encodeDate($("#enterDate").val());
        let rework = $("#rework").val();
        let oTable = $('#person').dataTable();
        Index_Person.updateUserInfo(workMonth, userName, realName, orgId, task, workDays, workType, level, workAge, enterDate, rework, oTable)
    })
    $("#cancelEdit").click(function () {
        $("#editBookModal").find('input').val('')
    })
    $('#searchPerson').click(function () {
        let orgId = $("#orgIdInput").val();
        if (Index_Person.jugeIsEmpty($("#workMonthInput").val())) {
            CommUtil.showAlertLayer("请选择查询时间");
            return;
        }
        /*if (Index_Person.jugeIsEmpty($("#userNameInput").val()) && Index_Person.jugeIsEmpty($("#realNameInput").val()) && (orgId == 0 || orgId == '0')) {
            CommUtil.showAlertLayer("请输入或者选择查询条件");
            return
        }*/
        loadData();

    });
    $("#btn_export").click(function () {
        if (searchParam == null) {
            CommUtil.showAlertLayer("请查询数据后导出")
            return
        }
        Index_Person.exportPersonXls(searchParam);
    });
    $('#person').on('page.dt', function () {
        let info = table.page.info();
        pageIndex = info.page;
        if (info.page == (pageSum.pages - 1) && isMoreData) {
            searchParam.pageNum++;
            Index_Person.searchUserInfo(searchParam);
        }
    });
    $("#person_length").on("click", function () {
        Index_Person.initPageSum();
    });
    //$("#person_length").css("display","none");
    $("#person_filter input").addClass("form-control serachInput200");
    $("#person_length select").find("option[value='100']").hide().css("color", "#f23023");

    //年月选择器
    laydate.render({
        elem: '#workMonthInput'
        , type: 'month'
        , trigger: 'click' //采用click弹出
    });
    //时间选择器
    laydate.render({
        elem: '#enterDate'
        , type: 'datetime'
        , trigger: 'click' //采用click弹出
    });
    $("#workMonthInput").val(CommUtil.getNowDay());
    $(document).ready(function () {
        loadData();
    });

    function loadData() {
        let tal = $("#person").DataTable();
        tal.clear().draw();
        tableData = [];
        searchParam = Index_Person.createSearchParam($("#userNameInput").val(), $("#realNameInput").val(), $("#orgIdInput").val(), $("#workMonthInput").val());
        searchParam.pageNum = pageStart;
        Index_Person.searchUserInfo(searchParam);
    }
});
