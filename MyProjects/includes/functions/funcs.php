<?php
/**
 * @Project ID BNC
 * @File /modules/home/main.php
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 09/04/2014, 02:19 PM
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}
class Funcs extends iBNC{
	function get_new_blog() {
		global $_B;
        $blog_detail = $this->getModel('blog_detail');
        $blog_detail->orderBy('created','DESC');
        $data['moinhat'] = $blog_detail->get(null,array(0,5));
        foreach ($data['moinhat'] as &$key) {
        	$key['created'] = date('Y-m-d h:i:s',$key['created']);
        	$key['img'] = $_B['upload_path'].$key['img'];
        }
        return $data;
    }
}