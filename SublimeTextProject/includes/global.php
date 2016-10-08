<?php
/**
 * @Project ID BNC
 * @File /includes/global.php
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 09/03/2014, 10:43 AM
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}
include(DIR_ROOT.'config/config.php');

// include(DIR_ROOT.'config/config_'.$_SERVER['HTTP_HOST'].'.php');
include(DIR_CLASS."model.php");    
include(DIR_CLASS."cache.php");  
include(DIR_CLASS."db/mysqliDB.php");  
include(DIR_CLASS."request.php");    
include(DIR_CLASS."template.php");
include(DIR_CLASS."ibnc.php");              
include(DIR_CLASS."BaseStatistic.php");              
include(DIR_CLASS."BaseModel.php");              
include(DIR_CLASS."pagination.php");              
include(DIR_FUNS."global.php");    

   
//default time zone
date_default_timezone_set('Asia/Ho_Chi_Minh');   
session_start();  
ob_start();  


 
$_B['url'] = curPageURL();  
$iBNC = new iBNC();  
$_B['cache'] = new CacheBNC(); 
$_B['time'] = time(); 
 
 
 
$mod = $_B['r']->get_string('mod','GET');

if( empty($mod))
	$mod = 'home'; 

include(DIR_MOD.$mod.'/main.php');

$iBNC = new $mod();
$iBNC->end();