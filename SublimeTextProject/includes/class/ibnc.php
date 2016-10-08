<?php
/**
 * @Project iBNC
 * @File /includes/class/ibnc.php
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 29/07/2015
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}
class iBNC{
	protected $input = array();
	protected $output = array();
	protected $output_type;
	protected $output_view;
	protected $mysql_db = null;
	protected $mysql_models = array();
	public $name_cookie = 'ttatgt';
	public $status = array(
		0 => 'Mới đặt lịch',
		1 => 'Đã xác minh khách hàng',
		2 => 'Đã thực hiện dịch vụ',
		3 => 'Đã huỷ dịch vụ'
	);
	public function __construct(){ 
		
	}
	/**
	 * Gửi dữ liệu để trả về cho client
	 */
	protected function view($output=array(),$output_type='html',$output_view='index'){
		$this->output_type = $output_type;
		$this->output_view = $output_view;
		$this->output = $output;
	}

	protected function model($model,$input=null,$module = null){
		if($module == null){
			$module = get_class($this);
		}
		
		include_once(DIR_MOD.strtolower($module).'/model/'.$model.'.php'); 

		return new $model($input);
	}
	/**
	 * Tra ve Model
	 */
	protected function getModel($name,$type='mysql'){
		$type_model = 'getModel_'.$type;
		return $this->$type_model($name);
	}
	/**
	 * Tra ve Model Mysql
	 */
	private function getModel_mysql($name){ 
		if($this->mysql_db == null){
			$this->mysql_db = db_connect();
		} 

		if( array_key_exists($name,$this->mysql_models) ){
			return $this->mysql_models[$name];
		}
		else{
			$this->mysql_models[$name] = new Model($this->mysql_db,$name);
			return $this->mysql_models[$name];
		} 
	}
	/**
	 * Nơi cuối con đường 
	 */
	public function end(){
		global $_B;
		switch ($this->output_type) {
			case 'html':
				$common = $this->getCommon(); 
				$d = $this->output; 
				$temp = new Template();
				include $temp->load($this->output_view);
				break;
			case 'js':
				header("Content-Type: application/javascript");
				$common = $this->getCommon(); 
				$d = $this->output; 
				$temp = new Template();
				include $temp->load($this->output_view);
				break;
			case 'json':
				$common = $this->getCommon(); 
				$d = $this->output; 
				header('Content-Type: application/json');
				echo json_encode($d); 
				break;
			
			default:
				die();
				break;
		}
		//cache or anything here :))))
	}
	private function getCommon(){ 
		return array();
	}
	protected function getMenu(){
		$cat = $this->getModel('1_cat');
		$domain = $this->getModel('1_domain');

		$cat->where('status',1);
		$d['c'] = $cat->get();

		$domain->where('status',1);
		$d['d'] = $domain->get();

		return $d;
	}
	protected function timeStamp($w_time,$w_date){
		///Time Stamp
			$w_time=explode(":",$w_time);
				$s=0;
				$h=(int)$w_time[0];
				$i=(int)$w_time[1];
				if(!preg_match('/PM|pm|Pm/',$w_time[1]))
						$h+=12;

			$tmp=explode('/',$w_date);

			if(count($tmp)>=2){
					$d=(int)$tmp[0];
					$m=(int)$tmp[1];
					$y=(int)$tmp[2];
			}
			else{
				$d=date("d");
				$m=date("m");
				$y=date("y");
				if(preg_match('/qua|Qua/',$w_date)){
					$d--;
				}
			}
		return mktime($h,$i,$s,$m,$d,$y);
	}


	protected function rebuild_date( $format, $time = 0 )
	{
	    if ( ! $time ) $time = time();

		$lang = array();
		$lang['sun'] = 'CN';
		$lang['mon'] = 'T2';
		$lang['tue'] = 'T3';
		$lang['wed'] = 'T4';
		$lang['thu'] = 'T5';
		$lang['fri'] = 'T6';
		$lang['sat'] = 'T7';
		$lang['sunday'] = 'Chủ nhật';
		$lang['monday'] = 'Thứ hai';
		$lang['tuesday'] = 'Thứ ba';
		$lang['wednesday'] = 'Thứ tư';
		$lang['thursday'] = 'Thứ năm';
		$lang['friday'] = 'Thứ sáu';
		$lang['saturday'] = 'Thứ bảy';
		$lang['january'] = 'Tháng Một';
		$lang['february'] = 'Tháng Hai';
		$lang['march'] = 'Tháng Ba';
		$lang['april'] = 'Tháng Tư';
		$lang['may'] = 'Tháng Năm';
		$lang['june'] = 'Tháng Sáu';
		$lang['july'] = 'Tháng Bảy';
		$lang['august'] = 'Tháng Tám';
		$lang['september'] = 'Tháng Chín';
		$lang['october'] = 'Tháng Mười';
		$lang['november'] = 'Tháng M. một';
		$lang['december'] = 'Tháng M. hai';
		$lang['jan'] = 'T01';
		$lang['feb'] = 'T02';
		$lang['mar'] = 'T03';
		$lang['apr'] = 'T04';
		$lang['may2'] = 'T05';
		$lang['jun'] = 'T06';
		$lang['jul'] = 'T07';
		$lang['aug'] = 'T08';
		$lang['sep'] = 'T09';
		$lang['oct'] = 'T10';
		$lang['nov'] = 'T11';
		$lang['dec'] = 'T12';

	    $format = str_replace( "r", "D, d M Y H:i:s O", $format );
	    $format = str_replace( array( "D", "M" ), array( "[D]", "[M]" ), $format );
	    $return = date( $format, $time );

	    $replaces = array(
	        '/\[Sun\](\W|$)/' => $lang['sun'] . "$1",
	        '/\[Mon\](\W|$)/' => $lang['mon'] . "$1",
	        '/\[Tue\](\W|$)/' => $lang['tue'] . "$1",
	        '/\[Wed\](\W|$)/' => $lang['wed'] . "$1",
	        '/\[Thu\](\W|$)/' => $lang['thu'] . "$1",
	        '/\[Fri\](\W|$)/' => $lang['fri'] . "$1",
	        '/\[Sat\](\W|$)/' => $lang['sat'] . "$1",
	        '/\[Jan\](\W|$)/' => $lang['jan'] . "$1",
	        '/\[Feb\](\W|$)/' => $lang['feb'] . "$1",
	        '/\[Mar\](\W|$)/' => $lang['mar'] . "$1",
	        '/\[Apr\](\W|$)/' => $lang['apr'] . "$1",
	        '/\[May\](\W|$)/' => $lang['may2'] . "$1",
	        '/\[Jun\](\W|$)/' => $lang['jun'] . "$1",
	        '/\[Jul\](\W|$)/' => $lang['jul'] . "$1",
	        '/\[Aug\](\W|$)/' => $lang['aug'] . "$1",
	        '/\[Sep\](\W|$)/' => $lang['sep'] . "$1",
	        '/\[Oct\](\W|$)/' => $lang['oct'] . "$1",
	        '/\[Nov\](\W|$)/' => $lang['nov'] . "$1",
	        '/\[Dec\](\W|$)/' => $lang['dec'] . "$1",
	        '/Sunday(\W|$)/' => $lang['sunday'] . "$1",
	        '/Monday(\W|$)/' => $lang['monday'] . "$1",
	        '/Tuesday(\W|$)/' => $lang['tuesday'] . "$1",
	        '/Wednesday(\W|$)/' => $lang['wednesday'] . "$1",
	        '/Thursday(\W|$)/' => $lang['thursday'] . "$1",
	        '/Friday(\W|$)/' => $lang['friday'] . "$1",
	        '/Saturday(\W|$)/' => $lang['saturday'] . "$1",
	        '/January(\W|$)/' => $lang['january'] . "$1",
	        '/February(\W|$)/' => $lang['february'] . "$1",
	        '/March(\W|$)/' => $lang['march'] . "$1",
	        '/April(\W|$)/' => $lang['april'] . "$1",
	        '/May(\W|$)/' => $lang['may'] . "$1",
	        '/June(\W|$)/' => $lang['june'] . "$1",
	        '/July(\W|$)/' => $lang['july'] . "$1",
	        '/August(\W|$)/' => $lang['august'] . "$1",
	        '/September(\W|$)/' => $lang['september'] . "$1",
	        '/October(\W|$)/' => $lang['october'] . "$1",
	        '/November(\W|$)/' => $lang['november'] . "$1",
	        '/December(\W|$)/' => $lang['december'] . "$1" );

	    return preg_replace( array_keys( $replaces ), array_values( $replaces ), $return );
	}
	public function timeAgo($time)
	{
	   $periods = array("giây", "phút", "giờ", "ngày", "tuần", "tháng", "năm", "thập niên");
	   $lengths = array("60","60","24","7","4.35","12","10");

	   $now = time();

	       $difference     = $now - $time;
	       $tense         = "trước";
	   if( $difference > 604800){
	   		return $this->rebuild_date('d D M, Y',$time);
	   }
	   for($j = 0; $difference >= $lengths[$j] && $j < count($lengths)-1; $j++) {
	       $difference /= $lengths[$j];
	   }

	   $difference = round($difference);

	   // if($difference != 1) {
	   //     $periods[$j].= "s";
	   // }

	   return "$difference $periods[$j] ".$tense;
	}
		function super_check_empty($index , $value){
			if(!$value){
				$response["status"] = false;
				$response["message"] = "Request failed ,Field $index empty!";

				die(json_encode($response));
			}
		}
		function check_empty($value){
			if(!$value){
				$response["status"] = false;
				$response["message"] = "Request failed ,some fields empty!";

				die(json_encode($response));
			}
		}
		function check_money($value){
			if($value < 0){
				$response["status"] = false;
				$response["message"] = "Request failed ,money unavailable!";

				die(json_encode($response));
			}
		}
		/*
		$this->check_moneys(array(
			,
			,
			,
			,
			,
			,

		));*/

		function check_moneys($array){
			for($i = 0; $i < count($array) ;$i++){
				if($array[$i] < 0){
					$response["status"] = false;
					$response["message"] = "Request failed ,money unavailable!";

					die(json_encode($response));
				}
			}
		}

		/*$this->check_emptys(array(

				,
				,
				,
				,
				,
				,

			));
		*/

		function check_emptys($array){
			for($i = 0; $i < count($array) ;$i++){
				if(!$array[$i]){
					$response["status"] = false;
					$response["message"] = "Request failed ,some fields empty!";

					die(json_encode($response));
				}
			}
		}
}