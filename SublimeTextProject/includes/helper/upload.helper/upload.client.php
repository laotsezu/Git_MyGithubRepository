<?php
/*
 * Upload client for BNC
 * @version   1.0 - @author    Quang Chau Tran <quangchauvn@gmail.com>
 * @currentVersion 2.0 - @author Huong Nguyen Ba <nguyenbahuong156@gmail.com>
 */
class BncUpload {
	private $options;
	private $ext;
	private $image;
	private $newImage;
	private $origWidth;
	private $origHeight;
	private $resizeWidth;
	private $resizeHeight; 
	private $adminUrl;
	public function __construct($options = null) {

		$this->options = array(
			'sv_url'            => 'http://tin12.com/cdn/index.php?data=',
			'sv_url_del'        => 'http://tin12.com/cdn/qc.php', 
			'thumb'             => array('100'),
			'tmp_upload'        => 'tmp/tmp_upload',
			'max_size'          => 1000,
			'type_file'         => 'img',
			'df_module'         => 'news',
			'df_field_in'       => 'file',
			'write_file'        => false,
		);
		if ($options) {
			$this->options = $this->array_replace($this->options, $options);
		}
		$this->adminUrl       = 'http://tin12.com/admin'; 
	}
	public function del($name, $idw = 0) {
		$ch       = curl_init();
		$name     = json_encode($name);
		$name     = base64_encode($name);
		$postData = array(
			'link' => $name,
		);
		curl_setopt($ch, CURLOPT_URL, $this->options['sv_url_del']);
		curl_setopt($ch, CURLOPT_USERPWD, 'banlamgiday:toimuonxoaanh@123!');
		curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		$response = curl_exec($ch);
		$response = json_decode($response, true);
		if ($response['status'] == true) {
			return true;
		} else {
			return $response['error'];
		}
		curl_close($ch);
	}
	public function upload($idw = 1, $module = null, $name_field = null, $name_up = '', $thumb = null) {
		//set base
		if (!isset($_FILES[$name_field]) || $_FILES[$name_field]['error'] != 0) {

			return false;

		}

		$mtime     = explode(' ', microtime());
		$timestamp = $mtime[1];
		$file      = $_FILES[$name_field];
		$ext       = $this->getExtension($file['name']);
		$file_     = $_FILES[$name_field]['tmp_name'];
		$name_file = $timestamp . '_' . $this->fix_file_name($_FILES[$name_field]['name']) . '.' . $ext;

		if ($this->options['type_file'] == 'audio' || $this->options['type_file'] == 'flash' || $this->options['type_file'] == 'document') {
			move_uploaded_file($file_, $this->options['tmp_upload'] . '/' . $name_file);
			$data = array(
				'idw'       => $idw,
				'module'    => null,
				'type_file' => $this->options['type_file'],
			);
		} else {
			//get info
			$module     = ($module != null) ? $module : $this->options['df_module'];
			$name_field = ($name_field != null) ? $name_field : $this->options['df_field_in'];
			if (!empty($name_up)) {
				$name_field = $timestamp . '_' . $this->fix_file_name($name_up) . '.' . $ext;
			}
			$thumb = ($thumb != null) ? $thumb : $this->options['thumb'];

			move_uploaded_file($file_, $this->options['tmp_upload'] . '/' . $name_file);
			$this->resize($this->options['tmp_upload'] . '/' . $name_file);
			/*
			 * Đóng dấu ảnh
			 */

			 
			//data to sv
			$data = array(
				'idw'       => $idw,
				'module'    => $module,
				'field'     => 'file',
				'type_file' => 'img',
			);

			if (!empty($name_up)) {
				$data['file_name'] = $this->fix_file_name($name_up);
			}

			//list tao thumbnail
			$data['thumb'] = $thumb;
		}

		$data       = json_encode($data);
		$database64 = base64_encode($data);

		$ch = curl_init();
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_URL, $this->options['sv_url'] . $database64);
		$postData = array(
			'file' => '@' . $this->options['tmp_upload'] . '/' . $name_file,
		);
		curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);
		$response = curl_exec($ch);

		curl_close($ch);
		@unlink($this->options['tmp_upload'] . '/' . $name_file);
		$response = json_decode($response, true);

		// if ($this->options['type_file']=='audio') {
		// 	 return $response;
		// }
		if ($response['status'] == true) {
			return $response[$this->options['type_file']];
		} else {
			return $response;
			return false;
		}

	}

	public function uploadCloneVatGia($idw, $module = null, $image_name) {

		$data = array(
			'idw'       => $idw,
			'module'    => $module,
			'field'     => 'file',
			'type_file' => 'img',
		);

		$data       = json_encode($data);
		$database64 = base64_encode($data);

		$ch = curl_init();
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_URL, $this->options['sv_url'] . $database64);
		$postData = array(
			'file' => '@' . $this->options['tmp_upload'] . '/' . $image_name,
		);
		curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);
		$response = curl_exec($ch);
		curl_close($ch);

		@unlink(DIR_TMP . 'tmp_upload/' . $image_name);
		$response = json_decode($response, true);

		// if ($this->options['type_file']=='audio') {
		// 	 return $response;
		// }
		if ($response['status'] == true) {
			return $response['img'];
		} else {
			return $response;
			return false;
		}

	}
	public function uploadMedia($idw, $uid = null) {

		$bncUpload = array(
			'logined' => true,
			'idw'     => $idw,
			'uid'     => $uid,
			'hash'    => md5('@gmail.com' . $idw),
		);
		$bncUpload = json_encode($bncUpload);
		$bncUpload = base64_encode($bncUpload);
		$bncUpload = $this->encode($bncUpload, '@gmail.com');
		return $bncUpload;
	}
	public function writeFile($idw, $filename = 'setting.json', $content = null, $type = 'GET') {
		$ch       = curl_init();
		$content  = base64_encode($content);
		$postData = array(
			'idw'      => $idw,
			'filename' => $filename,
			'content'  => $content,
			'type'     => $type,
		);

		curl_setopt($ch, CURLOPT_URL, $this->options['sv_url_write_file']);
		curl_setopt($ch, CURLOPT_USERPWD, 'banlamgiday:toimuonghifile@!2123!');
		curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		$response = curl_exec($ch);
		return $response;
	}
	private function encode($string, $key) {
		$key    = sha1($key);
		$strLen = strlen($string);
		$keyLen = strlen($key);
		$hash   = '';
		for ($i = 0; $i < $strLen; $i++) {
			$ordStr = ord(substr($string, $i, 1));
			$j      = ($keyLen > 0) ? $keyLen : 0;
			$ordKey = ord(substr($key, $j, 1));
			$j++;
			$hash .= strrev(base_convert(dechex($ordStr + $ordKey), 16, 36));
		}
		return $hash;
	}
	private function resize($filename) {
		$this->setImage($filename);

		if ($this->origWidth > $this->options['max_size']) {
			$this->resizeWidth  = $this->options['max_size'];
			$this->resizeHeight = $this->resizeHeightByWidth($this->resizeWidth);
			$this->newImage     = imagecreatetruecolor($this->resizeWidth, $this->resizeHeight);
			imagecopyresampled($this->newImage, $this->image, 0, 0, 0, 0, $this->resizeWidth, $this->resizeHeight, $this->origWidth, $this->origHeight);
			$this->saveImage($filename, 80);
		}

	}
	private function setImage($filename) {
		global $_VG;
		$size      = getimagesize($filename);
		$this->ext = $size['mime'];

		switch ($this->ext) {
			// Image is a JPG
			case 'image/jpg':
			case 'image/jpeg':
				// create a jpeg extension
				$this->image = imagecreatefromjpeg($filename);
				break;

			// Image is a GIF
			case 'image/gif':
				$this->image = @imagecreatefromgif($filename);
				break;

			// Image is a PNG
			case 'image/png':
				$this->image = @imagecreatefrompng($filename);
				break;

			// Mime type not found
			default:
				//throw new Exception("File is not an image, please use another file type.", 1);
				$_VG['error'] = 'dinh dang khong cho phep';
		}

		$this->origWidth  = imagesx($this->image);
		$this->origHeight = imagesy($this->image);
	}
	public function saveImage($savePath, $imageQuality = "100") {
		switch ($this->ext) {
			case 'image/jpg':
			case 'image/jpeg':
				// Check PHP supports this file type
				if (imagetypes() & IMG_JPG) {
					imagejpeg($this->newImage, $savePath, $imageQuality);
				}
				break;

			case 'image/gif':
				// Check PHP supports this file type
				if (imagetypes() & IMG_GIF) {
					imagegif($this->newImage, $savePath);
				}
				break;

			case 'image/png':
				$invertScaleQuality = 9 - round(($imageQuality / 100) * 9);

				// Check PHP supports this file type
				if (imagetypes() & IMG_PNG) {
					imagepng($this->newImage, $savePath, $invertScaleQuality);
				}
				break;
		}

		imagedestroy($this->newImage);
	}
	private function resizeHeightByWidth($width) {
		return floor(($this->origHeight / $this->origWidth) * $width);
	}
	private function array_replace($a, $b) {
		foreach ($b as $key => $val) {
			$a[$key] = $val;
		}
		return $a;
	}
	private function getExtension($str) {
		$i = strrpos($str, ".");
		if (!$i) {return "";}
		$l   = strlen($str) - $i;
		$ext = substr($str, $i + 1, $l);
		return $ext;
	}
	private function fixtitle($str) {
		$str = preg_replace("/(à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ)/i", 'a', $str);
		$str = preg_replace("/(è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ)/i", 'e', $str);
		$str = preg_replace("/(ì|í|ị|ỉ|ĩ)/i", 'i', $str);
		$str = preg_replace("/(ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ)/i", 'o', $str);
		$str = preg_replace("/(ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ)/i", 'u', $str);
		$str = preg_replace("/(ỳ|ý|ỵ|ỷ|ỹ)/i", 'y', $str);
		$str = preg_replace("/(đ)/i", 'd', $str);
		$str = preg_replace("/(À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ)/", 'A', $str);
		$str = preg_replace("/(È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ)/", 'E', $str);
		$str = preg_replace("/(Ì|Í|Ị|Ỉ|Ĩ)/", 'I', $str);
		$str = preg_replace("/(Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ)/", 'O', $str);
		$str = preg_replace("/(Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ)/", 'U', $str);
		$str = preg_replace("/(Ỳ|Ý|Ỵ|Ỷ|Ỹ)/", 'Y', $str);
		$str = preg_replace("/(Đ)/", 'D', $str);
		$str = str_replace(" ", "-", str_replace("&*#39;", "", $str));
		$str = strtolower($str);
		$ext = array('.mp3', '.wmv', '.mpeg', '.mp4a', '.acc', '.doc', '.xls', '.xlsx', '.ppt', '.csv', '.rtf', '.swf', '.pdf', '.m4a', '.wav', '.txt');
		foreach ($ext as $v) {
			$str = preg_replace('/' . $v . '$/', "", $str);
		}
		return $str;
	}
	private function fix_file_name($name) {
		$name = $this->fixtitle($name);
		$name = substr($name, 0, 50);
		return $name;
	}

}
