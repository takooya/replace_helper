<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="i18n">
    <meta name="description" content="i18n">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>i18n转化</title>
    <script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="../js/index.js"></script>
    <link href="../css/index.css" rel="stylesheet" type="text/css">
</head>
<body>
<iframe name="hideIframe" style="display:none;"></iframe>
<ul>
    <li class="li-item">
        <span>请输入目标文件夹</span>
        <input class="left_space" id="dir_string" type="text" name="fname" style="width: 500px"/>
        <button class="left_space" id="dir_select" type="button">下载</button>
    </li>
    <li class="li-item">
        <span>上传整理后的properties文件, 并根据文件修改源代码</span>
        <form action="/write" method="post" enctype="multipart/form-data" target="hideIframe">
            <input type="file" name="file"/>
            <input type="text" name="sourcePath"/>
            <input type="submit"/>
        </form>
    </li>
    <li class="li-item">
        <span>检查import和autowired情况</span>
        <button class="left_space" id="check_show" type="button">检查</button>
    </li>
    <p></p>
    <p></p>
    <li class="li-item">
        <button id="init_show" type="button">浏览解析记录</button>
        <button class="left_space" id="table_show" type="button">浏览解析结果</button>
        <button class="left_space" id="write_show" type="button">浏览写文件结果</button>
    </li>
</ul>
<div>
    <div id="init_div" class="table_div">
        <span id="init_title" class="table_title">解析记录</span>
        <table id="init_info" border="1"></table>
    </div>

    <div id="parse_div" class="table_div">
        <span id="parse_title" class="table_title">解析结果</span>
        <table id="parse_info" border="1"></table>
    </div>

    <div id="write_div" class="table_div">
        <span id="write_title" class="table_title">写文件结果</span>
        <table id="write_info" border="1"></table>
    </div>

    <div id="check_div" class="table_div">
        <span id="check_title" class="table_title">检查结果</span>
        <table id="check_info" border="1"></table>
    </div>
</div>

</body>
</html>
