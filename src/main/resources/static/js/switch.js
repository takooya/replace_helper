/**
 * Created with JetBrains PhpStorm.
 * User: Administrator
 * Date: 13-8-19
 * Time: 下午2:58
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function () {
    //首页导航切换颜色
    $('.menu-item a').each(function (index_out) {
        $(this).mouseover(function () {
            if (!$(this).hasClass('active')) {
                $('.aliases').each(function (index_in) {
                    if (index_in == index_out) {
                        $(this).css('color', '#006eda');
                    }
                });
            }
        });

        $(this).mouseout(function () {
            if (!$(this).hasClass('active')) {
                $('.aliases').each(function (index_in) {
                    if (index_in == index_out) {
                        $(this).css('color', '#808080');
                    }
                });
            }
        });
    });

    //首页我们的优势图片切换
    $(".serBox").hover(
        function () {
            $(this).children().delay(600).stop(false, true);
            $(this).children(".pic1").animate({right:-380}, 580);
            $(this).children(".pic2").animate({left:0}, 400);
            $(this).children(".div-youshi1").animate({left:-240}, 400);
            $(this).children(".div-light-youshi1").animate({right:20}, 400);
        },
        function () {
            $(this).children().stop(true, true);
            $(this).children(".pic1").animate({right:270}, 400);
            $(this).children(".pic2").animate({left:-380}, 580);
            $(this).children(".div-youshi1").animate({left:240}, 400);
            $(this).children(".div-light-youshi1").animate({right:-360}, 400);
        }
    );

    $(".serBox2").hover(
        function () {
            $(this).children().delay(600).stop(false, true);
            $(this).children(".pic1").animate({right:-400}, 580);
            $(this).children(".pic2").animate({left:0}, 400);
            $(this).children(".div-youshi2").animate({left:-300}, 400);
            $(this).children(".div-light-youshi2").animate({right:32}, 400);
        },
        function () {
            $(this).children().stop(true, true);
            $(this).children(".pic1").animate({right:290}, 400);
            $(this).children(".pic2").animate({left:-400}, 580);
            $(this).children(".div-youshi2").animate({left:104}, 400);
            $(this).children(".div-light-youshi2").animate({right:-500}, 400);
        }
    );

    $(".serBox3").hover(
        function () {
            $(this).children().delay(600).stop(false, true);
            $(this).children(".pic1").animate({right:-180}, 580);
            $(this).children(".pic2").animate({left:0}, 400);
            $(this).children(".div-youshi3").animate({left:-240}, 400);
            $(this).children(".div-light-youshi3").animate({right:42}, 400);
        },
        function () {
            $(this).children().stop(true, true);
            $(this).children(".pic1").animate({right:70}, 400);
            $(this).children(".pic2").animate({left:-180}, 580);
            $(this).children(".div-youshi3").animate({left:18}, 400);
            $(this).children(".div-light-youshi3").animate({right:-240}, 400);
        }
    );

    $(".serBox4").hover(
        function () {
            $(this).children().delay(600).stop(false, true);
            $(this).children(".pic1").animate({right:-180}, 580);
            $(this).children(".pic2").animate({left:0}, 400);
            $(this).children(".div-youshi4").animate({left:-240}, 400);
            $(this).children(".div-light-youshi4").animate({right:42}, 400);
        },
        function () {
            $(this).children().stop(true, true);
            $(this).children(".pic1").animate({right:70}, 400);
            $(this).children(".pic2").animate({left:-180}, 580);
            $(this).children(".div-youshi4").animate({left:18}, 400);
            $(this).children(".div-light-youshi4").animate({right:-240}, 400);
        }
    );

    $(".serBox5").hover(
        function () {
            $(this).children().delay(600).stop(false, true);
            $(this).children(".pic1").animate({right:-185}, 580);
            $(this).children(".pic2").animate({left:0}, 400);
            $(this).children(".div-youshi5").animate({left:-200}, 400);
            $(this).children(".div-light-youshi5").animate({right:30}, 400);
        },
        function () {
            $(this).children().stop(true, true);
            $(this).children(".pic1").animate({right:75}, 400);
            $(this).children(".pic2").animate({left:-185}, 580);
            $(this).children(".div-youshi5").animate({left:30}, 400);
            $(this).children(".div-light-youshi5").animate({right:-200}, 400);
        }
    );

    $(".serBox6").hover(
        function () {
            $(this).children().delay(600).stop(false, true);
            $(this).children(".pic1").animate({right:-185}, 580);
            $(this).children(".pic2").animate({left:0}, 400);
            $(this).children(".div-youshi6").animate({left:-200}, 400);
            $(this).children(".div-light-youshi6").animate({right:22}, 400);
        },
        function () {
            $(this).children().stop(true, true);
            $(this).children(".pic1").animate({right:75}, 400);
            $(this).children(".pic2").animate({left:-185}, 580);
            $(this).children(".div-youshi6").animate({left:22}, 400);
            $(this).children(".div-light-youshi6").animate({right:-200}, 400);
        }
    );
    /**
     * 关于我们招聘内容切换
     */
    $('.workitem ').click(function(){

        var index = $(this).index();
        var lastliobj = $(this).parent().find('li').eq(index-1);
        var nextliobj = $(this).parent().find('li').eq(index+1);
        if($(this).hasClass('workon')){
            $(this).removeClass('workon').addClass('workleave');

        }else{
            $(this).removeClass('workleave').addClass('workon');

        }
    });
    $(".submenulist li").hover(function(){
        $(this).stop().animate({backgroundPosition:'0px 0px'},300,'swing',function(){
            $(this).find('a').css('color','#ffffff');
        });
        //$(this).stop().animate({backgroundPosition:'0px 0px'},300);

    },function(){
        $(this).stop().animate({backgroundPosition:'-188px 0'},300,'swing',function(){
            $(this).find('a').css('color','#595758');
        });
    });

});
