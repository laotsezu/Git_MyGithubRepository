<?php 
class Repository extends iBNC{
		public function __construct(){ 
			global $_B; 
			$this->r = $_B['r'];   
			$this->cache = $_B['cache'];   
			$controller = $this->r->get_string('controller','GET');
			$this->action = $this->r->get_string('action','GET');
			
			$this->d = array(); 

			if(method_exists($this, $controller)){
				$this->$controller();
			}
			else{
				$this->index();
			}
		} 
		function index(){ 			
			echo "123";
		}
		function kiemhang(){
			$phieu_kiem_hang["agency_id"] = $this->r->get_int("agency_id","POST");
			$phieu_kiem_hang["personnel_id"] = $this->r->get_int("personnel_id","POST");
			$phieu_kiem_hang["origin_goods_ids"] = $this->r->get_string("origin_goods_ids","POST");
			$phieu_kiem_hang["current_goods_ids"] = $this->r->get_string("current_goods_ids","POST");
			$phieu_kiem_hang["coupon_ghi_chu"] = $this->r->get_string("coupon_ghi_chu","POST");

			$this->check_emptys(
				array($phieu_kiem_hang["agency_id"]
					,$phieu_kiem_hang["personnel_id"]
					,$phieu_kiem_hang["origin_goods_ids"]
					,$phieu_kiem_hang["current_goods_ids"])
				);
			
			$coupon = $this->model("ModelRepositoryCoupon",$phieu_kiem_hang,"repository");

			$result = $coupon->accept();
			$response = $result;

			echo json_encode($response);
		}
		
		function nhaphang(){

			$phieu_nhap_hang["provider_id"] = $this->r->get_int("provider_id","POST");
			$phieu_nhap_hang["agency_id"] = $this->r->get_int("agency_id","POST");
			$phieu_nhap_hang["personnel_id"] = $this->r->get_int("personnel_id","POST");
			$phieu_nhap_hang["icoupon_tong_tien_hang"] = $this->r->get_int("icoupon_tong_tien_hang","POST");
			$phieu_nhap_hang["icoupon_giam_gia"] = $this->r->get_int("icoupon_giam_gia","POST");
			$phieu_nhap_hang["icoupon_tien_phai_tra"] = $this->r->get_int("icoupon_tien_phai_tra","POST");
			$phieu_nhap_hang["icoupon_tien_da_tra"] = $this->r->get_int("icoupon_tien_da_tra","POST");
			$phieu_nhap_hang["icoupon_ghi_chu"] = $this->r->get_string("icoupon_ghi_chu","POST");
			$phieu_nhap_hang["icoupon_goods_ids"] = $this->r->get_string("icoupon_goods_ids","POST");

			$this->check_emptys(
				array(
					$phieu_nhap_hang["agency_id"] 
					,$phieu_nhap_hang["personnel_id"]
					,$phieu_nhap_hang["icoupon_goods_ids"]
				);

			$this->check_moneys(
				array(
					$phieu_nhap_hang["icoupon_tong_tien_hang"]
					,$phieu_nhap_hang["icoupon_tien_phai_tra"] 
					,$phieu_nhap_hang["icoupon_tien_da_tra"] 
				));

			$icoupon = $this->model("ModelRepositoryICoupon",$phieu_nhap_hang,"repository");

			$result = $icoupon->accept();
			$response= $result;

			echo json_encode($response);
		}
		function duyetkho(){
			$start = $this->r->get_int("start","GET");
			$limit = $this->r->get_int("limit","GET");

			if(!$start)$start = 0;
			if(!$limit)$limit = 10;

			$goods = $this->model("ModelRepositoryGoods",null,"repository");
			$items = $goods->get($start,$limit);

			echo json_encode($items);
		}
	}

?>