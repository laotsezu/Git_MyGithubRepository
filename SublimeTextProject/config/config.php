<?php
/**
 * @Project ID BNC
 * @File /config/config.php
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 09/03/2014, 10:49 AM
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}

$_B = array();
$_B['db_host']				= '127.0.0.1';
$_B['db_user']  			= 'sql_kiot';
$_B['db_password'] 	 		= 'LWaPXIt2yY24xM6o4lgjb';
$_B['db_charset'] 			= 'utf8';
$_B['db_name']  			= 'db_kiot';
$_B['db_port']				=  4426; 
$_B['home'] 				= 'http://kiot.igarden.vn/';
$_B['theme']				= 'default';
$_B['home_theme']			= $_B['home'].'themes/'.$_B['theme'].'/';  

/*
*code response

1000 - Hello message

*/
?>