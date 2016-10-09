<?php 
	class Personnel extends iBNC{
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
		function dangky(){
			$personnel_input["personnel_ten"] = $this->r->get_string("personnel_ten","POST");
			$personnel_input["personnel_loai"] = $this->r->get_string("personnel_loai","POST");
			$personnel_input["personnel_sdt"] = $this->r->get_string("personnel_sdt","POST");
			$personnel_input["personnel_dia_chi"] = $this->r->get_string("personnel_dia_chi","POST");
			$personnel_input["personnel_tong_tien_ban"] = $this->r->get_int("personnel_tong_tien_ban","POST");
			$personnel_input["personnel_tai_khoan"] = $this->r->get_string("personnel_tai_khoan","POST");
			$personnel_input["personnel_mat_khau"] = $this->r->get_string("personnel_mat_khau","POST");
		
			$this->checkAvailableAcountAndPassword($personnel_input["personnel_tai_khoan"],$personnel_input["personnel_mat_khau"]);
		
			$personnel = $this->model("ModelPersonnelPersonnel",$personnel_input,"personnel");

			try{
				$personnel->register();
				$response["status"] = true;
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);
		} 
		function dangnhap(){

			$personnel_input["personnel_tai_khoan"] = $this->r->get_string("personnel_tai_khoan","POST");
			$personnel_input["personnel_mat_khau"] = $this->r->get_string("personnel_mat_khau","POST");

			$this->checkAvailableAcountAndPassword($personnel_input["personnel_tai_khoan"],$personnel_input["personnel_mat_khau"]);

			$personnel = $this->model("ModelPersonnelPersonnel",$personnel_input,"personnel");

			try{
				@session_start();
				if(!isset($_SESSION[$personnel_input["personnel_tai_khoan"]]) || !$_SESSION[$personnel_input["personnel_tai_khoan"]]){
					$personnel->login();
					$_SESSION[$personnel_input["personnel_tai_khoan"]] = $personnel_input["personnel_tai_khoan"];
					$response["status"] = true;
				}
				else{
					$response['status'] = false;
					$response['message'] = "Account is loginning!";
				}
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);
		}
		function dangxuat(){

			$personnel_input["personnel_tai_khoan"] = $this->r->get_string("personnel_tai_khoan","POST");

			if(!$personnel_input["personnel_tai_khoan"]){
				$reponse["status"] = false;
				$response["message"] = "Account missing!";

				die(json_encode($response));
			}


			try{
				@session_start();
				if(!isset($_SESSION[$personnel_input["personnel_tai_khoan"]]) || !$_SESSION[$personnel_input["personnel_tai_khoan"]]){
					$response["status"] = false;
					$response["message"] = "Account is not login yet!";
				}
				else{
					$_SESSION[$personnel_input["personnel_tai_khoan"]] = null;
					$response["status"] = true;
				}
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}

			echo json_encode($response);
		}
		function doimatkhau(){
		
			$personnel_input["personnel_tai_khoan"] = $this->r->get_string("personnel_tai_khoan","POST");
			$personnel_input["personnel_mat_khau"] = $this->r->get_string("personnel_mat_khau","POST");

			$this->checkAvailableAcountAndPassword($personnel_input["personnel_tai_khoan"],$personnel_input["personnel_mat_khau"]);

			$new_password = $this->r->get_string("personnel_mat_khau_moi","POST");

			if(!$new_password){
				$response["status"] = false;
				$response["message"] = "Account missing!";
				die(json_encode($response));
			}
			else{
				$personnel = $this->model("ModelPersonnelPersonnel",$personnel_input,"personnel");
				try{
					$personnel->login();
					$personnel->changePassword($new_password);
					$response['status'] = true;
				}
				catch(Exception $e){
					$response["status"] = false;
					$response["message"] = $e->getMessage();
				}
				echo json_encode($response);
			}
		}
		private function checkAvailableAcountAndPassword($account,$password){

			if(!$account){
				$response["status"] = false;
				$response["message"] = "Account missing!";
				die(json_encode($response));
			}
			if(!$password){
				$response["status"] = false;
				$response["message"] = "Password missing!";
				die(json_encode($response));
			}
		}
	}
?>