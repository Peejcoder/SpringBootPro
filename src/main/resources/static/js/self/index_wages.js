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
let titles = ['工作时间', 'Pos登录名', '姓名', '管理站编号', '岗位工资',
    '司龄工资', '加班工资', '月任务工资', '规范工资', '夜班补贴', '总工资'];

let tableData = [];

let selectedIndex = -1;

let pageIndex = 0;

let pageSize = 100;

let pageStart = 0;

let searchParam;

let pageSum = 0;

let table;

let isMoreData = false;

const Index_Wages = {
    jugeIsEmpty: function (str) {
        if (str == '' || str == null || str == undefined) {
            return true
        }
        return false
    },
    createPerson: function (work_month, user_name, real_name, org_id, salary1, salary2, salary3, salary4, salary5, salary6, total_salary) {
        let o = new Object();
        o.workMonth = work_month;
        o.userName = user_name;
        o.realName = real_name;
        o.orgId = org_id;
        o.salary1 = salary1;
        o.salary2 = salary2;
        o.salary3 = salary3;
        o.salary4 = salary4;
        o.salary5 = salary5;
        o.salary6 = salary6;
        o.totalSalary = total_salary;
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
        //$("#editWagesInfo").modal()
        if (table.rows('.selected').data().length) {
            $("#editWagesInfo").modal()
            let rowData = table.rows('.selected').data()[0];
            let inputs = $("#editWagesModal").find('input')
            for (let i = 0; i < inputs.length; i++) {
                $(inputs[i]).val(rowData[i + 1])
            }
            $("#orgId_edit").val(rowData[4]);
        } else {
            alert('请选择修改人员');
        }
    },

    showAddModal: function () {
        $("#addWagesInfo").modal();
        Index_Wages.initAddSelect();
    },

    initAddSelect: function () {
        let selects = $("#addWagesInfo").find('select')
        for (let i = 0; i < selects.length; i++) {
            $(selects[i]).val(0)
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
        pageSum = Index_Wages.getPageSum();
        Index_Wages.jumpToPage(pageIndex)
    },

    jumpToPage: function (page) {
        $("#wages").dataTable().fnPageChange(pageIndex);
    },

    getPageSum: function () {
        if (table == null) {
            return 0;
        }
        return table.page.info();
    },

    initCal: function () {
        $.ajax({
            url: urlPre + "/api/home/testCal",
            type: "POST",
            contentType: 'application/json;charset=UTF-8',
            success: function (res) {
                console.log("---> cal")
            },
            error: function (errs) {
                console.log(errs);
            }
        })
    },
    searchUserInfo: function (param) {
        Index_Wages.showLayer();
        $.ajax({
            url: urlPre + "/api/home/getSalary",
            type: "POST",
            timeout: 10000,
            data: JSON.stringify(param),
            contentType: 'application/json;charset=UTF-8',
            success: function (res) {
                if (param.pageNum == pageStart) {
                    Index_Wages.jumpToPage(pageStart)
                }
                //console.log(new Date().Format("yyyy-MM-dd HH:mm:ss"));
                $.each(res, function (i, field) {
                    tableData.push(['', field.workMonth, field.userName, field.realName, field.orgId, field.salary1, field.salary2, field.salary3, field.salary4, field.salary5, field.salary6, field.totalSalary])
                });
                Index_Wages.addMoreData();
                Index_Wages.closeLayer();
            },
            error: function (errs) {
                console.log(errs);
                Index_Wages.closeLayer();
                CommUtil.showAlertLayer("查询失败");
            }
        })
    },
    updateUserInfo: function (workMonth, userName, realName, orgId, salary1, salary2, salary3, salary4, salary5, salary6, totalSalary, oTable) {
        Index_Wages.showLayer();
        let wagesInfo1 = Index_Wages.createPerson(workMonth, userName, realName, orgId, salary1, salary2, salary3, salary4, salary5, salary6, totalSalary);
        let personList = [];
        personList.push(wagesInfo1);
        $.ajax({
            url: urlPre + "/api/home/updateSalaryInfo",
            type: "POST",
            timeout: 10000,
            data: JSON.stringify(personList),
            contentType: 'application/json;charset=UTF-8',
            success: function (res) {
                if (res) {
                    oTable.fnUpdate(['', workMonth, userName, realName, orgId, salary1, salary2, salary3, salary4, salary5, salary6, totalSalary], selectedIndex);
                    Index_Wages.closeLayer();
                } else {
                    Index_Wages.closeLayer();
                    CommUtil.showAlertLayer("更新失败")
                }
            },
            error: function (errs) {
                console.log(errs);
                Index_Wages.closeLayer();
                CommUtil.showAlertLayer("更新失败")
            }
        })
    },

    addUserInfo: function (workMonth, userName, realName, orgId, task, workDays, workType, level, workAge, enterDate, rework, oTable) {
        table.row.add(['', workMonth, userName, realName, orgId, task, workDays, workType, level, workAge, enterDate, rework]).draw();
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

    calCount: function () {
        let salary1 = $("#salary1_edit").val();
        let salary2 = $("#salary2_edit").val();
        let salary3 = $("#salary3_edit").val();
        let salary4 = $("#salary4_edit").val();
        let salary5 = $("#salary5_edit").val();
        let salary6 = $("#salary6_edit").val();
        $("#total_salary_edit").val(CommUtil.toFiexdNum(CommUtil.toFiexdNum(Number(salary1), 2) + CommUtil.toFiexdNum(Number(salary2), 2) + CommUtil.toFiexdNum(Number(salary3), 2) + CommUtil.toFiexdNum(Number(salary4), 2) + CommUtil.toFiexdNum(Number(salary5), 2) + CommUtil.toFiexdNum(Number(salary6), 2), 2))
    },
    exportWagesXls: function (param) { //点击导出按钮导出为excel
        let oA = document.createElement('a');
        let url = window.location.protocol + "//" + window.location.host + "";
        url += urlPre + "/api/home/exportSalaryXls"
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
    table = $('#wages').DataTable({
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
    $('#editWagesInfo').modal({
        backdrop: false,
        keyboard: true,
        show: false
    });
    $('#addWagesInfo').modal({
        backdrop: false,
        keyboard: true,
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
    $('#wages tbody').on('click', 'tr', function () {
        let index = $(this).context._DT_RowIndex; //行号
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            selectedIndex = -1;
        } else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
            selectedIndex = index;
            Index_Wages.showEditModal();
        }
    });

    $('#btn_edit').click(function () {
        Index_Wages.showEditModal();
    });
    $('#btn_add').click(function () {
        Index_Wages.showAddModal();
    });
    $("#saveEdit").click(function () {
        let workMonth = $("#workMonth_edit").val();
        let userName = $("#userName_edit").val();
        let realName = $("#realName_edit").val();
        let orgId = $("#orgId_edit").val();
        let salary1 = $("#salary1_edit").val();
        let salary2 = $("#salary2_edit").val();
        let salary3 = $("#salary3_edit").val();
        let salary4 = $("#salary4_edit").val();
        let salary5 = $("#salary5_edit").val();
        let salary6 = $("#salary6_edit").val();
        let total_salary = $("#total_salary_edit").val();
        let oTable = $('#wages').dataTable();
        Index_Wages.updateUserInfo(workMonth, userName, realName, orgId, salary1, salary2, salary3, salary4, salary5, salary6, total_salary, oTable)
    });
    $("#saveAdd").click(function () {
        let workMonth = $("#workMonthAdd").val();
        let userName = $("#userNameAdd").val();
        let realName = $("#realNameAdd").val();
        let orgId = $("#orgIdAdd").val();
        let salary1 = $("#salary1Add").val();
        let salary2 = $("#salary2Add").val();
        let salary3 = $("#salary3Add").val();
        let salary4 = $("#salary4Add").val();
        let salary5 = $("#salary5Add").val();
        let salary6 = $("#salary6Add").val();
        let total_salary = $("#total_salary_edit").val();
        let oTable = $('#wages').dataTable();
        Index_Wages.updateUserInfo(workMonth, userName, realName, orgId, salary1, salary2, salary3, salary4, salary5, salary6, total_salary, oTable);
        //Index_Wages.updateUserInfo(workMonth, userName, realName, orgId, task, workDays, workType, level, workAge, enterDate, rework, oTable)
        Index_Wages.addUserInfo(workMonth, userName, realName, orgId, salary1, salary2, salary3, salary4, salary5, salary6, total_salary, oTable)
    });
    $("#cancelEdit").click(function () {
        $("#editBookModal").find('input').val('')
    });
    $("#cancelAdd").click(function () {
        $("#addPersonModal").find('input').val('')
    });

    $("#btn_cal").click(function () {
        Index_Wages.initCal();
    });
    $("#btn_export").click(function () {
        if (searchParam == null) {
            CommUtil.showAlertLayer("请查询数据后导出")
            return
        }
        Index_Wages.exportWagesXls(searchParam);
    });

    $('#searchWages').click(function () {
        let orgId = $("#orgIdInput").val();
        if (Index_Wages.jugeIsEmpty($("#workMonthInput").val())) {
            CommUtil.showAlertLayer("请选择查询时间");
            return
        }
        loadData();
    });

    $('#wages').on('page.dt', function () {
        let info = table.page.info();
        pageIndex = info.page;
        if (info.page == (pageSum.pages - 1)) {
            searchParam.pageNum++;
            Index_Wages.searchUserInfo(searchParam);
        }
    });
    //$("#wages_length").css("display","none");
    $("#wages_length select").find("option[value='100']").hide().css("color", "#f23023");
    //年月选择器
    laydate.render({
        elem: '#workMonthInput'
        , type: 'month'
    });

    $("#wages_paginate").on("click", "a", function () {
        console.log("clicked")
    });

    $("#wages_filter input").addClass("form-control serachInput200");

    $("#salary1_edit").bind('input propertychange', function () {
        Index_Wages.calCount();
    });
    $("#salary2_edit").bind('input propertychange', function () {
        Index_Wages.calCount();
    });
    $("#salary3_edit").bind('input propertychange', function () {
        Index_Wages.calCount();
    });
    $("#salary4_edit").bind('input propertychange', function () {
        Index_Wages.calCount();
    });
    $("#salary5_edit").bind('input propertychange', function () {
        Index_Wages.calCount();
    });
    $("#salary6_edit").bind('input propertychange', function () {
        Index_Wages.calCount();
    });
    $("#workMonthInput").val(CommUtil.getNowDay())

    $(document).ready(function () {
        loadData();
    });
    function loadData() {
        let tal = $("#wages").DataTable();
        tal.clear().draw();
        tableData = [];
        searchParam = Index_Wages.createSearchParam($("#userNameInput").val(), $("#realNameInput").val(), $("#orgIdInput").val(), $("#workMonthInput").val());
        searchParam.pageNum = pageStart;
        Index_Wages.searchUserInfo(searchParam);
    }
});
