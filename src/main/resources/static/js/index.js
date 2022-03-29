function callGet(url) {
    return new Promise((resolve, reject) => {
        $.ajax({
            //请求方式
            type: "GET",
            //请求地址
            url: url,
            //请求成功
            success: function (result) {
                if (result.code !== 200) {
                    alert(result.message);
                    reject(result)
                }
                resolve(result.data);
            },
            //请求失败，包含具体的错误信息
            error: function (e) {
                reject(reject)
            }
        });
    })
}

function callPost(url, param) {
    return new Promise((resolve, reject) => {
        $.ajax({
            //请求方式
            type: "POST",
            //请求的媒体类型
            contentType: "application/json;charset=UTF-8",
            //请求地址
            url: url,
            //数据，json字符串
            data: JSON.stringify(param),
            //请求成功
            success: function (result) {
                if (result.code !== 200) {
                    alert(result.message);
                    reject(result)
                }
                resolve(result.data);
            },
            //请求失败，包含具体的错误信息
            error: function (e) {
                reject(reject)
            }
        });
    })
}

$(document).ready(function () {
    let getDefaultPromise = callGet("http://localhost:609/getDefault");
    getDefaultPromise.then(result => {
        $("#dir_string").val(result.value);
    });
    $('.table_div').hide();
    $('#init_show').click(function () {
        $('.table_div').hide();
        let path = $("#dir_string").attr("value");
        callGet("http://localhost:609/dictInfo/initList").then(result => {
            $('#init_div').show();
            var strHtml = '';
            for (var record in result) {
                strHtml += '<tr>' +
                    '<td>' + result[record].code + '</td>' +
                    '<td>' + result[record].value + '</td>' +
                    '<td>' + result[record].createTime + '</td>' +
                    '</tr>';
            }
            $('#init_title').html("解析文件夹：<b>" + path + "</b>的记录");
            $('#init_info').html('<tr><th>id</th><th>路径</th><th>创建时间</th></tr>' + strHtml);
        });
    });
    $('#table_show').click(function () {
        $('.table_div').hide();
        callGet("http://localhost:609/readProp").then(result => {
            $('#parse_div').show();
            var strHtml = '';
            var title = '';
            for (var record in result) {
                title = result[record].filename;
                strHtml += '<tr><td>' + result[record].pkey + '</td><td>' + result[record].value + '</td></tr>'
            }
            $('#parse_title').html("扫描文件夹：<b>" + title + "</b>的结果");
            $('#parse_info').html('<tr><th>key</th><th>中文</th></tr>' + strHtml)
        });
    });
    $('#write_show').click(function () {
        $('.table_div').hide();
        let path = $("#dir_string").attr("value");
        callPost("http://localhost:609/writeResult/selectList", {path: path}).then(result => {
            $('#write_div').show();
            console.log(result);
            var strHtml = '';
            for (var record in result) {
                strHtml += '<tr>' +
                    '<td>' + result[record].content + '</td>' +
                    '<td>' + result[record].filePath + '</td>' +
                    '<td>' + result[record].comment + '</td>' +
                    '</tr>';
            }
            $('#write_title').html("写文件夹：<b>" + path + "</b>的结果");
            $('#write_info').html('<tr><th>处理内容</th><th>文件路径</th><th>处理结果</th></tr>' + strHtml);
        });
    });
    $('#check_show').click(function () {
        $('.table_div').hide();
        let path = $("#dir_string").attr("value");
        callPost("http://localhost:609/checkImportAndAutowired", {sourcePath: path}).then(result => {
            $('#check_div').show();
            console.log(result);
            var strHtml = '';
            // for (var record in result) {
            //     strHtml += '<tr>' +
            //         '<td>' + result[record].content + '</td>' +
            //         '<td>' + result[record].filePath + '</td>' +
            //         '<td>' + result[record].comment + '</td>' +
            //         '</tr>';
            // }
            $('#check_title').html("检查：<b>" + path + "</b>的结果");
            // $('#check_info').html('<tr><th>处理内容</th><th>文件路径</th><th>处理结果</th></tr>' + strHtml);
            $('#check_info').html('<tr><th>处理内容</th><th>文件路径</th><th>处理结果</th></tr>');
        });
    });
    $('#dir_select').click(function () {
        var form = $('<form method="POST" action="http://localhost:609/initProp">');
        form.append('<input type="hidden" name="sourcePath" value="' + $("#dir_string").attr("value") + '">');
        $('body').append(form);
        form.submit();
    });
});
